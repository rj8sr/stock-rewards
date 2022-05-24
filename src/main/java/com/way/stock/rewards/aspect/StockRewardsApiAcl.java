package com.way.stock.rewards.aspect;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.way.core.util.constants.SupportAppConstants;
import com.way.stock.rewards.service.StockRewardsApiAclService;

/**
 * This class is used to check the method level authorization. Controller
 * methods can be accessed only if logged in user has access entries in
 * database.
 */

// @Component
// @Aspect
public class StockRewardsApiAcl {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private StockRewardsApiAclService stockRewardsApiAclService;

	@Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
	public void controller() {
		// controller()
	}

	@Pointcut("execution(* com.way.stock.rewards.controller.*.*(..))")
	protected void allMethod() {
		// allMethod()
	}

	/**
	 * authorizeAdvice.
	 *
	 * @throws Throwable
	 */
	@Around("controller() && allMethod()")
	public Object authorizeAdvice(ProceedingJoinPoint joinPoint) throws Throwable {

		Object returnValue = null;
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		String requestUrl = request.getRequestURI();

		logger.info("request.getRequestURI() :{}", request.getRequestURI());
		logger.info("request.getContextPath() :{}", request.getContextPath());
		logger.info("request.getServletPath() :{}", request.getServletPath());
		logger.info("request.getPathInfo() :{}", request.getPathInfo());

		Authentication authObj = SecurityContextHolder.getContext().getAuthentication();

		String userRole = null;
		if (authObj instanceof AnonymousAuthenticationToken) {
			userRole = "Guest";
		} else if (authObj instanceof OAuth2Authentication) {
			userRole = authObj.getAuthorities().toString();
		}
		userRole = userRole.replaceAll("\\[", "").replaceAll("\\]", "");

		requestUrl = requestUrl.substring(requestUrl.indexOf("/", requestUrl.indexOf("/") + 1));

		Integer userId = (Integer) request.getAttribute(SupportAppConstants.USER_ID_REQUEST_ATTRIBUTE);

		/*
		 * roles given by new way-auth will have "ROLE_" prepended. So until we
		 * completely remove way-service for authentication below logic should execute
		 */

		if (userRole.contains("ROLE_")) {
			userRole = userRole.replaceAll("ROLE_", "");
		}

		Integer privileges = stockRewardsApiAclService.getPrivilege(requestUrl, userRole, userId);

		if (privileges != null && privileges > 0) {// Has privilege
			returnValue = joinPoint.proceed();

		} else {
			throw new InsufficientAuthenticationException("Access Denied");
		}

		return returnValue;

	}

}
