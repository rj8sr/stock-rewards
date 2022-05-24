package com.way.stock.rewards;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.way.authutil.exception.CustomOauthException;
import com.way.authutil.filter.WayMetadataFilter;
import com.way.util.propertiesutil.OAuth2Properties;

@Configuration
@EnableResourceServer
@Order(1)
public class WayStockRewardsResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Autowired
	@Qualifier("customTokenService")
	private AuthorizationServerTokenServices customTokenService;

	@Autowired
	private WayMetadataFilter wayMetadataFilter;

	@Autowired
	private OAuth2Properties oauthProperties;

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.resourceId(oauthProperties.getResourceIds()).stateless(true)
				.tokenServices((ResourceServerTokenServices) customTokenService);
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {

		return new AuthenticationEntryPoint() {

			@Autowired
			@Qualifier("handlerExceptionResolver")
			HandlerExceptionResolver resolver;

			@Override
			public void commence(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException authException) throws IOException, ServletException {
				resolver.resolveException(request, response, null, authException);

			}
		};
	}

	/*
	 * For Handling oauth exceptions which happens prior to controllers . Auth
	 * exceptions are thrown at multiple levels so we handle each in its own way
	 * Type 2: few exceptions are caught by this
	 */
	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		final OAuth2AccessDeniedHandler handler = new OAuth2AccessDeniedHandler();
		handler.setExceptionTranslator(exception -> {
			if (exception instanceof OAuth2Exception) {
				OAuth2Exception oAuth2Exception = (OAuth2Exception) exception;
				return ResponseEntity.status(oAuth2Exception.getHttpErrorCode())
						.body(new CustomOauthException(oAuth2Exception.getMessage()));
			} else {
				throw exception;
			}
		});
		return handler;
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();

		// All public faced API's should contain public in the URL. And We skip auth
		// check for those .

		http.anonymous().and().addFilterAfter(wayMetadataFilter, FilterSecurityInterceptor.class).authorizeRequests()
				.antMatchers("/**/public/**").permitAll().and().authorizeRequests().anyRequest().authenticated().and()
				.logout().logoutSuccessUrl("/").permitAll();

		/*
		 * Future reference on how to implement scope for each client
		 * .authorizeRequests() .anyRequest().access(
		 * "#oauth2.hasAnyScope('way-web-admin','way-web-consumer')")
		 */
	}

	@Bean
	public HttpFirewall allowUrlBackSlashHttpFirewall() {
		return new DefaultHttpFirewall();
	}
}
