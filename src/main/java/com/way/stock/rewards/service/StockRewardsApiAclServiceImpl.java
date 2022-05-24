package com.way.stock.rewards.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.way.exception.constants.ExceptionModule;
import com.way.exception.util.BaseService;
import com.way.exception.util.WayServiceException;
import com.way.stock.rewards.repository.ApiPrivilegeMapRepository;

@Service
public class StockRewardsApiAclServiceImpl extends BaseService implements StockRewardsApiAclService {

	Logger logger = LoggerFactory.getLogger(StockRewardsApiAclServiceImpl.class);

	@Autowired
	private ApiPrivilegeMapRepository apiPrivilegeMapRepository;

	@Override
	public Integer getPrivilege(String requestUrl, String userRole, Integer userId) throws WayServiceException {

		String methodArgs = getExceptionUtil().methodInputArgsAsString(requestUrl, userRole, userId);
		Integer privileges = 0;

		try {
			for (String curRole : userRole.split(",")) {
				privileges = apiPrivilegeMapRepository.getPrivilege(requestUrl, curRole.trim(), userId);
				if (privileges != null && privileges > 0) {
					break;
				}
			}
		} catch (Exception ex) {
			logger.error("getPrivilege:{}", methodArgs);
			handleExceptions(ex, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(), "getPrivilege",
					methodArgs);
		}

		return privileges;

	}

}
