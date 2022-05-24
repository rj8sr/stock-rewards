package com.way.stock.rewards.service;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.way.core.util.constants.CustomErrorCodes;
import com.way.exception.constants.ExceptionModule;
import com.way.exception.util.BaseService;
import com.way.exception.util.WayResponseCode;
import com.way.exception.util.WayServiceException;
import com.way.stock.rewards.constants.AlpacaErrorCode;
import com.way.stock.rewards.constants.AlpacaErrorMessage;
import com.way.stock.rewards.constants.StockReward;
import com.way.stock.rewards.properties.AlpacaProperties;
import com.way.stock.rewards.repository.StockRewardsRepository;
import com.way.stock.rewards.request.dto.JournalRequestDto;
import com.way.stock.rewards.request.dto.OrderRequestDto;
import com.way.stock.rewards.request.dto.RegisterUserRequestDto;
import com.way.stock.rewards.response.dto.AccountDetailsResponseDto;
import com.way.stock.rewards.response.dto.JournalOrderResponseDto;
import com.way.stock.rewards.response.dto.OrderDetailsResponseDto;
import com.way.stock.rewards.response.dto.PositionDetailResponseDto;
import com.way.stock.rewards.response.dto.UserAccountInfoResponseDto;

/**
 * This Service layer methods is used to getting request details from
 * TradingService layer methods for creating a request and returning the
 * successful response to TradingService layer methods by using the third party
 * Api's integration and for fetching details from database we use
 * stockRewardsRepository methods
 *
 */
@Service
public class AlpacaServiceImpl extends BaseService implements AlpacaService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	AlpacaProperties alpacaProperties;

	@Autowired
	StockRewardsRepository stockRewardsRepository;

	/**
	 * This method is use to fetch the user account details by using user's
	 * accountId which are fetching from database by using it's userId and on
	 * successful creation of request it will return getAccountDetails
	 *
	 * @param userId
	 * @return accountDetails
	 * @throws WayServiceException
	 *
	 */
	@Override
	public AccountDetailsResponseDto getAccountDetails(Integer userId) throws WayServiceException {
		String methodArgs = getExceptionUtil().methodInputArgsAsString(userId);
		CloseableHttpClient httpClient = null;
		AccountDetailsResponseDto accountDetails = null;
		try {
			objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
			String accountId = stockRewardsRepository.fetchAccountIdByUserId(userId);

			httpClient = HttpClientBuilder.create().build();
			String url = alpacaProperties.getUrl() + StockReward.ALPACA_COMMON_URL + accountId + "/account";

			HttpGet httpGet = new HttpGet(url);
			httpGet.setHeader(StockReward.AUTHORIZATION, StockReward.BASIC + alpacaProperties.getApiKey());

			CloseableHttpResponse response = httpClient.execute(httpGet);

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				String responseJson = EntityUtils.toString(response.getEntity());
				accountDetails = objectMapper.readValue(responseJson, AccountDetailsResponseDto.class);
				logger.debug("successfully fetched the userAccount details: (AlpacaService Layer) {}", userId);
			} else {
				throw new WayServiceException(WayResponseCode.WAY_NOT_FOUND, AlpacaErrorCode.NOT_FOUND,
						AlpacaErrorMessage.ACCOUNT_NOT_FOUND);
			}
		} catch (Exception e) {
			logger.error("getAccountDetails:(AlpacaService Layer) {}", methodArgs);
			handleExceptions(e, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(), "getAccountDetails",
					methodArgs);
		} finally {
			try {
				if (httpClient != null)
					httpClient.close();
			} catch (Exception ex) {
				logger.error(CustomErrorCodes.GENERAL_EXCEPTION);
			}
		}
		return accountDetails;
	}

	/**
	 * This method is use to fetch the user account position details by using user's
	 * accountId which are fetching from database by using it's userId and on
	 * successful creation of request it will return positionsDetails
	 *
	 * @param userId
	 * @return positionsDetails
	 * @throws WayServiceException
	 */
	@Override
	public List<PositionDetailResponseDto> getAccountPositionDetails(Integer userId) throws WayServiceException {
		String methodArgs = getExceptionUtil().methodInputArgsAsString(userId);

		CloseableHttpClient httpClient = null;
		PositionDetailResponseDto[] positionsDetails = null;
		try {
			objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
			String accountId = stockRewardsRepository.fetchAccountIdByUserId(userId);

			httpClient = HttpClientBuilder.create().build();
			String url = alpacaProperties.getUrl() + StockReward.ALPACA_COMMON_URL + accountId + "/positions";

			HttpGet httpGet = new HttpGet(url);
			httpGet.setHeader(StockReward.AUTHORIZATION, StockReward.BASIC + alpacaProperties.getApiKey());

			CloseableHttpResponse response = httpClient.execute(httpGet);

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				String responseJson = EntityUtils.toString(response.getEntity());
				JSONArray responseArray = new JSONArray(responseJson);

				positionsDetails = objectMapper.readValue(responseArray.toString(), PositionDetailResponseDto[].class);

				logger.debug("successfully fetched the userAccount position details: (AlpacaService Layer) {}", userId);
			} else {
				throw new WayServiceException(WayResponseCode.WAY_NOT_FOUND, AlpacaErrorCode.NOT_FOUND,
						AlpacaErrorMessage.ACCOUNT_NOT_FOUND);
			}
		} catch (Exception e) {
			logger.error("getAccountPositionDetails:(AlpacaService Layer) {}", methodArgs);
			handleExceptions(e, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(),
					"getAccountPositionDetails", methodArgs);
		} finally {
			try {
				if (httpClient != null)
					httpClient.close();
			} catch (Exception ex) {
				logger.error(CustomErrorCodes.GENERAL_EXCEPTION);
			}
		}
		return Arrays.asList(positionsDetails);
	}

	/**
	 * This method is used to register a user in reference with
	 * registerUserRequestDto through which we are passing the details to register a
	 * user request and if request is successfully processed then we will return the
	 * successfully created user details response
	 *
	 * @param userId
	 * @param RegisterUserRequestDto
	 * @return userAccountInfo
	 * @throws WayServiceException
	 */
	@Override
	public UserAccountInfoResponseDto registerUser(Integer userId, @Valid RegisterUserRequestDto registerUserRequestDto)
			throws WayServiceException {
		String methodArgs = getExceptionUtil().methodInputArgsAsString(userId);
		CloseableHttpClient httpClient = null;
		UserAccountInfoResponseDto userAccountInfo = null;
		try {
			objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
			httpClient = HttpClientBuilder.create().build();

			String url = alpacaProperties.getUrl() + "/v1/accounts";
			String jsonRequest = objectMapper.writeValueAsString(registerUserRequestDto);
			StringEntity requestEntity = new StringEntity(jsonRequest, ContentType.APPLICATION_JSON);

			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader(StockReward.AUTHORIZATION, StockReward.BASIC + alpacaProperties.getApiKey());
			httpPost.setEntity(requestEntity);

			CloseableHttpResponse response = httpClient.execute(httpPost);

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				String responseJson = EntityUtils.toString(response.getEntity());
				userAccountInfo = objectMapper.readValue(responseJson, UserAccountInfoResponseDto.class);
				logger.debug("successfully registered the user: (AlpacaService Layer) {}", userId);
			} else if (statusCode == HttpStatus.SC_BAD_REQUEST) {
				throw new WayServiceException(WayResponseCode.WAY_INTERNAL_SERVER_ERROR, AlpacaErrorCode.BAD,
						AlpacaErrorMessage.INVALID_BODY_REQUEST);
			} else if (statusCode == HttpStatus.SC_CONFLICT) {
				throw new WayServiceException(WayResponseCode.WAY_INTERNAL_SERVER_ERROR, AlpacaErrorCode.CONFLICT,
						AlpacaErrorMessage.EMAIL_ALREADY_REGISTERED);
			} else if (statusCode == HttpStatus.SC_UNPROCESSABLE_ENTITY) {
				throw new WayServiceException(WayResponseCode.WAY_INTERNAL_SERVER_ERROR,
						AlpacaErrorCode.UNPROCESSABLE_ENTITY, AlpacaErrorMessage.UNPPROCCESSABLE_ENTITY);
			} else if (statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
				throw new WayServiceException(WayResponseCode.WAY_INTERNAL_SERVER_ERROR,
						AlpacaErrorCode.INTERNAL_SERVER_ERROR, AlpacaErrorMessage.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			logger.error("registerUser:(AlpacaService Layer) {}", methodArgs);
			handleExceptions(e, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(), "registerUser",
					methodArgs);
		} finally {
			try {
				if (httpClient != null)
					httpClient.close();
			} catch (Exception ex) {
				logger.error(CustomErrorCodes.GENERAL_EXCEPTION);
			}
		}
		return userAccountInfo;
	}

	/**
	 * This method is use to create buy order for the accountId of the Firm which we
	 * are fetching from database and with reference with orderRequestDto that will
	 * tell which security with orderType ,either it will be market orderType or
	 * limit orderType and if order is successfully processed then it will return
	 * successfully created order response
	 *
	 * @param userId
	 * @param OrderRequestDto
	 * @return orderDetail
	 * @throws WayServiceException
	 */
	@Override
	public OrderDetailsResponseDto buySecurityOrder(@Valid OrderRequestDto orderRequestDto, Integer userId)
			throws WayServiceException {
		String methodArgs = getExceptionUtil().methodInputArgsAsString(userId);

		CloseableHttpClient httpClient = null;
		OrderDetailsResponseDto orderDetails = null;
		try {
			objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
			String accountId = stockRewardsRepository.fetchAccountIdOfFirm(userId);

			httpClient = HttpClientBuilder.create().build();
			String url = alpacaProperties.getUrl() + StockReward.ALPACA_COMMON_URL + accountId + "/orders";

			String jsonRequest = objectMapper.writeValueAsString(orderRequestDto);
			StringEntity requestEntity = new StringEntity(jsonRequest, ContentType.APPLICATION_JSON);

			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader(StockReward.AUTHORIZATION, StockReward.BASIC + alpacaProperties.getApiKey());
			httpPost.setEntity(requestEntity);

			CloseableHttpResponse response = httpClient.execute(httpPost);

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {

				String responseJson = EntityUtils.toString(response.getEntity());
				orderDetails = objectMapper.readValue(responseJson, OrderDetailsResponseDto.class);
				logger.debug("successfully processed the buyOrder details: (AlpacaService Layer)");

			} else if (statusCode == HttpStatus.SC_FORBIDDEN) {
				throw new WayServiceException(WayResponseCode.WAY_INTERNAL_SERVER_ERROR, AlpacaErrorCode.FORBIDDEN,
						AlpacaErrorMessage.FORBIDDEN);
			} else if (statusCode == HttpStatus.SC_UNPROCESSABLE_ENTITY) {
				throw new WayServiceException(WayResponseCode.WAY_INTERNAL_SERVER_ERROR,
						AlpacaErrorCode.UNPROCESSABLE_ENTITY, AlpacaErrorMessage.UNPPROCCESSABLE_ENTITY);
			}
		} catch (Exception e) {
			logger.error("buySecurityOrder:(AlpacaService Layer) {}", methodArgs);
			handleExceptions(e, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(), "buySecurityOrder",
					methodArgs);
		} finally {
			try {
				if (httpClient != null)
					httpClient.close();
			} catch (Exception ex) {
				logger.error(CustomErrorCodes.GENERAL_EXCEPTION);
			}
		}
		return orderDetails;
	}

	/**
	 * This method is use to create sell order for the accountId of the user which
	 * we are fetching from database by it's userId and a security symbol ,that is
	 * particularly which security we want to sell for an accountId and if order is
	 * successfully processed then it will return successfully created order
	 * response
	 *
	 * @param userId
	 * @param securitySymbol
	 * @return orderDetail
	 * @throws WayServiceException
	 */
	@Override
	public OrderDetailsResponseDto sellSecurityOrder(String securitySymbol, Integer userId) throws WayServiceException {
		String methodArgs = getExceptionUtil().methodInputArgsAsString(userId);

		CloseableHttpClient httpClient = null;
		OrderDetailsResponseDto orderDetails = null;
		try {
			objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
			String accountId = stockRewardsRepository.fetchAccountIdByUserId(userId);
			httpClient = HttpClientBuilder.create().build();

			String url = alpacaProperties.getUrl() + StockReward.ALPACA_COMMON_URL + accountId + "/positions/"
					+ securitySymbol;

			HttpDelete httpDelete = new HttpDelete(url);
			httpDelete.setHeader(StockReward.AUTHORIZATION, StockReward.BASIC + alpacaProperties.getApiKey());

			CloseableHttpResponse response = httpClient.execute(httpDelete);

			int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_OK) {
				String responseJson = EntityUtils.toString(response.getEntity());
				orderDetails = objectMapper.readValue(responseJson, OrderDetailsResponseDto.class);
				logger.debug("successfully processed the sell order: (AlpacaService Layer) {}", userId);
			} else if (statusCode == HttpStatus.SC_NOT_FOUND) {
				throw new WayServiceException(WayResponseCode.WAY_NOT_FOUND, AlpacaErrorCode.NOT_FOUND,
						AlpacaErrorMessage.ORDER_NOT_FOUND);
			}
		} catch (Exception e) {
			logger.error("sellSecurityOrder:(AlpacaService Layer) {}", methodArgs);
			handleExceptions(e, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(), "sellSecurityOrder",
					methodArgs);
		} finally {
			try {
				if (httpClient != null)
					httpClient.close();
			} catch (Exception ex) {
				logger.error(CustomErrorCodes.GENERAL_EXCEPTION);
			}
		}
		return orderDetails;
	}

	/**
	 * This method is use to create sell order for the accountId of the user which
	 * we are fetching from database by it's userId and a security symbol and a
	 * quantity that is, particularly which security with how much as its quantity
	 * and for which accountId and if order is successfully processed then it will
	 * return successfully created order response
	 *
	 * @param userId
	 * @param securitySymbol
	 * @param quantity
	 * @return orderDetail
	 * @throws WayServiceException
	 */
	@Override
	public OrderDetailsResponseDto sellSecurityOrderWithQuantity(String securitySymbol, String quantity, Integer userId)
			throws WayServiceException {
		String methodArgs = getExceptionUtil().methodInputArgsAsString(userId);

		CloseableHttpClient httpClient = null;
		OrderDetailsResponseDto orderDetails = null;
		try {
			objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
			String accountId = stockRewardsRepository.fetchAccountIdByUserId(userId);
			httpClient = HttpClientBuilder.create().build();

			String url = alpacaProperties.getUrl() + StockReward.ALPACA_COMMON_URL + accountId + "/positions/"
					+ securitySymbol + "?qty=" + quantity;

			HttpDelete httpDelete = new HttpDelete(url);
			httpDelete.setHeader(StockReward.AUTHORIZATION, StockReward.BASIC + alpacaProperties.getApiKey());

			CloseableHttpResponse response = httpClient.execute(httpDelete);

			int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_OK) {
				String responseJson = EntityUtils.toString(response.getEntity());
				orderDetails = objectMapper.readValue(responseJson, OrderDetailsResponseDto.class);
				logger.debug("successfully processed the sell order with quantity: (AlpacaService Layer) {}", userId);
			} else if (statusCode == HttpStatus.SC_NOT_FOUND) {
				throw new WayServiceException(WayResponseCode.WAY_NOT_FOUND, AlpacaErrorCode.NOT_FOUND,
						AlpacaErrorMessage.ORDER_NOT_FOUND);
			}
		} catch (Exception e) {
			logger.error("sellSecurityOrderWithQuantity:(AlpacaService Layer) {}", methodArgs);
			handleExceptions(e, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(),
					"sellSecurityOrderWithQuantity", methodArgs);
		} finally {
			try {
				if (httpClient != null)
					httpClient.close();
			} catch (Exception ex) {
				logger.error(CustomErrorCodes.GENERAL_EXCEPTION);
			}
		}
		return orderDetails;
	}

	/**
	 * This method is use to create a journal security order in reference with
	 * journalRequestDto through which we are passing the details to create a
	 * journal security order request and if request is successfully processed then
	 * it will return successfully created order response
	 *
	 * @param userId
	 * @param JournalRequestDto
	 * @return securityOrderDetail
	 * @throws WayServiceException
	 */
	@Override
	public JournalOrderResponseDto createJournalSecurityOrder(JournalRequestDto journalRequestDto, Integer userId)
			throws WayServiceException {
		String methodArgs = getExceptionUtil().methodInputArgsAsString(userId);

		CloseableHttpClient httpClient = null;
		JournalOrderResponseDto securityOrderDetail = null;
		try {
			objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

			httpClient = HttpClientBuilder.create().build();

			String url = alpacaProperties.getUrl() + "/v1/journals";
			String jsonRequest = objectMapper.writeValueAsString(journalRequestDto);
			StringEntity requestEntity = new StringEntity(jsonRequest, ContentType.APPLICATION_JSON);

			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader(StockReward.AUTHORIZATION, StockReward.BASIC + alpacaProperties.getApiKey());
			httpPost.setEntity(requestEntity);
			CloseableHttpResponse response = httpClient.execute(httpPost);

			int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_OK) {
				String responseJson = EntityUtils.toString(response.getEntity());
				securityOrderDetail = objectMapper.readValue(responseJson, JournalOrderResponseDto.class);
				logger.debug("successfully processed the journal order: (AlpacaService Layer)");
			} else if (statusCode == HttpStatus.SC_BAD_REQUEST) {
				throw new WayServiceException(WayResponseCode.WAY_INTERNAL_SERVER_ERROR, AlpacaErrorCode.BAD,
						AlpacaErrorMessage.INVALID_BODY_REQUEST);
			} else if (statusCode == HttpStatus.SC_FORBIDDEN) {
				throw new WayServiceException(WayResponseCode.WAY_NOT_FOUND, AlpacaErrorCode.FORBIDDEN,
						AlpacaErrorMessage.INSUFFICIENT_ASSETS);
			} else if (statusCode == HttpStatus.SC_NOT_FOUND) {
				throw new WayServiceException(WayResponseCode.WAY_NOT_FOUND, AlpacaErrorCode.NOT_FOUND,
						AlpacaErrorMessage.ACCOUNT_NOT_FOUND);
			} else if (statusCode == HttpStatus.SC_UNPROCESSABLE_ENTITY) {
				throw new WayServiceException(WayResponseCode.WAY_INTERNAL_SERVER_ERROR,
						AlpacaErrorCode.UNPROCESSABLE_ENTITY, AlpacaErrorMessage.STOCK_JOURNAL_LIMIT_EXCEED);
			}
		} catch (Exception e) {
			logger.error("createJournalSecurityOrder:(AlpacaService Layer) {}", methodArgs);
			handleExceptions(e, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(),
					"createJournalSecurityOrder", methodArgs);
		} finally {
			try {
				if (httpClient != null)
					httpClient.close();
			} catch (Exception ex) {
				logger.error(CustomErrorCodes.GENERAL_EXCEPTION);
			}
		}
		return securityOrderDetail;
	}

	/**
	 * This method is use to create a journal cash order in reference with
	 * journalRequestDto through which we are passing the details to create a
	 * journal cash order request and if request is successfully processed then it
	 * will return successfully created order response
	 *
	 * @param userId
	 * @param JournalRequestDto
	 * @return cashOrderDetail
	 * @throws WayServiceException
	 */
	@Override
	public JournalOrderResponseDto createJournalCashOrder(@Valid JournalRequestDto journalRequestDto, Integer userId)
			throws WayServiceException {
		String methodArgs = getExceptionUtil().methodInputArgsAsString(userId);

		CloseableHttpClient httpClient = null;
		JournalOrderResponseDto cashOrderDetail = null;
		try {
			objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

			httpClient = HttpClientBuilder.create().build();

			String url = alpacaProperties.getUrl() + "/v1/journals";
			String jsonRequest = objectMapper.writeValueAsString(journalRequestDto);
			StringEntity requestEntity = new StringEntity(jsonRequest, ContentType.APPLICATION_JSON);

			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader(StockReward.AUTHORIZATION, StockReward.BASIC + alpacaProperties.getApiKey());
			httpPost.setEntity(requestEntity);
			CloseableHttpResponse response = httpClient.execute(httpPost);

			int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_OK) {
				String responseJson = EntityUtils.toString(response.getEntity());
				cashOrderDetail = objectMapper.readValue(responseJson, JournalOrderResponseDto.class);
				logger.debug("successfully processed the journal order: (AlpacaService Layer)");
			} else if (statusCode == HttpStatus.SC_BAD_REQUEST) {
				throw new WayServiceException(WayResponseCode.WAY_INTERNAL_SERVER_ERROR, AlpacaErrorCode.BAD,
						AlpacaErrorMessage.INVALID_BODY_REQUEST);
			} else if (statusCode == HttpStatus.SC_FORBIDDEN) {
				throw new WayServiceException(WayResponseCode.WAY_NOT_FOUND, AlpacaErrorCode.FORBIDDEN,
						AlpacaErrorMessage.INSUFFICIENT_AMOUNT);
			} else if (statusCode == HttpStatus.SC_NOT_FOUND) {
				throw new WayServiceException(WayResponseCode.WAY_NOT_FOUND, AlpacaErrorCode.NOT_FOUND,
						AlpacaErrorMessage.ACCOUNT_NOT_FOUND);
			}
		} catch (Exception e) {
			logger.error("createJournalCashOrder:(AlpacaService Layer) {}", methodArgs);
			handleExceptions(e, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(),
					"createJournalCashOrder", methodArgs);
		} finally {
			try {
				if (httpClient != null)
					httpClient.close();
			} catch (Exception ex) {
				logger.error(CustomErrorCodes.GENERAL_EXCEPTION);
			}
		}
		return cashOrderDetail;
	}
}