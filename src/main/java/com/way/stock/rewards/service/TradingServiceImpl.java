package com.way.stock.rewards.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.way.core.util.dto.PaginationData;
import com.way.core.util.dto.PaginationUtil;
import com.way.exception.constants.ExceptionModule;
import com.way.exception.util.BaseService;
import com.way.exception.util.WayServiceException;
import com.way.stock.rewards.constants.StockReward;
import com.way.stock.rewards.datasource.config.ConnectionUtil;
import com.way.stock.rewards.enums.OrderType;
import com.way.stock.rewards.repository.StockRewardsRepository;
import com.way.stock.rewards.request.dto.AgreementRequestDto;
import com.way.stock.rewards.request.dto.JournalRequestDto;
import com.way.stock.rewards.request.dto.OrderRequestDto;
import com.way.stock.rewards.request.dto.RegisterUserRequestDto;
import com.way.stock.rewards.response.AccountActivityDetailsResponse;
import com.way.stock.rewards.response.AccountDetailsResponse;
import com.way.stock.rewards.response.JournalOrderResponse;
import com.way.stock.rewards.response.OrderDetailsResponse;
import com.way.stock.rewards.response.UserAccountInfoResponse;
import com.way.stock.rewards.response.dto.AccountDetailsResponseDto;
import com.way.stock.rewards.response.dto.JournalOrderResponseDto;
import com.way.stock.rewards.response.dto.OrderDetailsResponseDto;
import com.way.stock.rewards.response.dto.PositionDetailResponseDto;
import com.way.stock.rewards.response.dto.UserAccountInfoResponseDto;
import com.way.stock.rewards.util.DateUtil;

/**
 * This Service layer methods is used to passing details from controller methods
 * to AlpacaService layer methods internally and getting response from alpaca
 * service layer methods and saving that response into the database using
 * stockRewardsRepository methods and passing response back to controller layer
 * methods
 *
 */
@Service
public class TradingServiceImpl extends BaseService implements TradingService {

	@Autowired
	private ConnectionUtil connectionUtil;

	@Autowired
	private AlpacaService alpacaService;

	@Autowired
	StockRewardsRepository stockRewardsRepository;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * This method is used to fetch account details by passing userId into the
	 * alpacaService.getAccountDetails() method which will return the account
	 * details response and we will pass that response to the controller layer
	 *
	 * @param userId
	 * @return accountDetails
	 * @throws WayServiceException
	 *
	 */
	@Override
	public AccountDetailsResponse getAccountDetails(Integer userId) throws WayServiceException {
		String methodArgs = getExceptionUtil().methodInputArgsAsString(userId);
		AccountDetailsResponse accountDetails = null;
		AccountDetailsResponseDto accountDetailsResponse = null;
		try {
			accountDetails = new AccountDetailsResponse();
			accountDetailsResponse = alpacaService.getAccountDetails(userId);
			accountDetails.setAccountDetails(accountDetailsResponse);
			logger.debug("successfully fetched the userAccount details (TradingService Layer) {}", userId);
		} catch (Exception ex) {
			logger.error("getAccountDetails:(TradingService Layer) {}", methodArgs);
			handleExceptions(ex, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(), "getAccountDetails",
					methodArgs);
		}
		return accountDetails;
	}

	/**
	 * This method is used to fetch account activity details in a paginated manner
	 * by passing userId into the method and fetch the orderDetails and journalOrder
	 * details from database and combine them and return them as activity details in
	 * paginated form and we will pass that response to the controller layer
	 *
	 * @param userId
	 * @param page
	 * @param size
	 * @return paginationData
	 * @throws WayServiceException
	 */
	@Override
	public PaginationData<AccountActivityDetailsResponse> getAccountActivityDetails(Integer page, Integer size,
			Integer range, Integer userId) throws WayServiceException {
		String methodArgs = getExceptionUtil().methodInputArgsAsString(userId);
		PaginationData<AccountActivityDetailsResponse> paginationData = null;
		try {
			String accountId = stockRewardsRepository.fetchAccountIdByUserId(userId);
			List<AccountActivityDetailsResponse> securityOrderDetails = stockRewardsRepository
					.fetchSecurityOrderDetailsByUserId(page, size, range, userId);
			logger.debug("successfully fetched the user's securityOrderDetails(TradingService Layer) {}", userId);

			List<AccountActivityDetailsResponse> journalOrderDetails = stockRewardsRepository
					.fetchJournalOrderDetailsByAccountId(page, size, range, accountId);
			logger.debug("successfully fetched the user's journalOrderDetails(TradingService Layer) {}", userId);

			List<AccountActivityDetailsResponse> activityDetails = Stream.of(securityOrderDetails, journalOrderDetails)
					.flatMap(Collection::stream)
					.sorted(Comparator.comparing(AccountActivityDetailsResponse::getCreatedDate).reversed())
					.collect(Collectors.toList());

			paginationData = PaginationUtil.getPaginationDataObj(activityDetails, page, size, activityDetails.size());
			logger.debug("successfully fetched the userAccount activity details (TradingService Layer) {}", userId);
		} catch (Exception ex) {
			logger.error("getAccountActivityDetails:(TradingService Layer) {}", methodArgs);
			handleExceptions(ex, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(),
					"getAccountActivityDetails", methodArgs);
		}
		return paginationData;
	}

	/**
	 * This method is used to fetch account position details by passing userId into
	 * the alpacaService.getAccountPositionDetails() method which will return the
	 * position details response and we will pass that response to the controller
	 * layer
	 *
	 * @param userId
	 * @return positionDetails
	 * @throws WayServiceException
	 */
	@Override
	public PaginationData<PositionDetailResponseDto> getAccountPositionDetails(Integer page, Integer size,
			Integer userId) throws WayServiceException {
		String methodArgs = getExceptionUtil().methodInputArgsAsString(userId);
		PaginationData<PositionDetailResponseDto> positionDetailsInfo = null;
		try {
			List<PositionDetailResponseDto> positionDetails = alpacaService.getAccountPositionDetails(userId);
			positionDetailsInfo = PaginationUtil.getPaginationDataObj(positionDetails, page, size,
					positionDetails.size());
			logger.debug("successfully fetched the userAccount position details (TradingService Layer) {}", userId);
		} catch (Exception ex) {
			logger.error("getAccountPositionDetails:(TradingService Layer) {}", methodArgs);
			handleExceptions(ex, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(),
					"getAccountPositionDetails", methodArgs);
		}
		return positionDetailsInfo;
	}

	/**
	 * This method is used to register user by passing userId and
	 * registerUserRequestDto into the alpacaService.registerUser() method which
	 * will return the successfully created registerUser response and we save that
	 * response into the database and we will pass that response to the controller
	 * layer
	 *
	 *
	 * @param userId
	 * @param RegisterUserRequestDto
	 * @return registerUser
	 * @throws WayServiceException
	 */
	@Override
	public UserAccountInfoResponse registerUser(@Valid RegisterUserRequestDto registerUserRequestDto, String ipAddress,
			Integer userId) throws WayServiceException {
		String methodArgs = getExceptionUtil().methodInputArgsAsString(userId);
		UserAccountInfoResponse registerUser = null;
		UserAccountInfoResponseDto userDetailResponse = null;
		try {

			String userEmailAddress = stockRewardsRepository.fetchUserEmailAddressByUserId(userId);
			registerUserRequestDto.getContact().setEmailAddress(userEmailAddress);

			List<AgreementRequestDto> agreementDetails = registerUserRequestDto.getAgreements().stream()
					.map(agreements -> {
						AgreementRequestDto agreementDetail = new AgreementRequestDto();
						agreementDetail.setAgreement(agreements.getAgreement());
						agreementDetail.setIpAddress(ipAddress);
						agreementDetail.setSignedAt(DateUtil.getCurrentDateTime());
						return agreementDetail;
					}).collect(Collectors.toList());

			registerUserRequestDto.setAgreements(agreementDetails);
			registerUser = new UserAccountInfoResponse();
			userDetailResponse = alpacaService.registerUser(userId, registerUserRequestDto);
			saveUserAccountInformation(userId, userDetailResponse, registerUserRequestDto);
			registerUser.setUserAccountInfo(userDetailResponse);
			logger.debug("successfully registered the user (TradingService Layer) {}", userId);
		} catch (Exception ex) {
			logger.error("registerUser:(TradingService Layer) {}", methodArgs);
			handleExceptions(ex, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(), "registerUser",
					methodArgs);
		}
		return registerUser;
	}

	/**
	 * This method is used to buy a security by passing userId and orderRequestDto
	 * into the alpacaService.buySecurityOrder() method which will return the
	 * successfully processed buy order response and we save that response into the
	 * database and we will pass that response to the controller layer
	 *
	 * @param userId
	 * @param OrderRequestDto
	 * @return buySecurityOrder
	 * @throws WayServiceException
	 */
	@Override
	public OrderDetailsResponse buySecurityOrder(@Valid OrderRequestDto orderRequestDto, Integer userId)
			throws WayServiceException {
		String methodArgs = getExceptionUtil().methodInputArgsAsString(userId);
		OrderDetailsResponse buySecurityOrder = null;
		OrderDetailsResponseDto orderDetails = null;
		try {
			orderRequestDto.setSide(StockReward.BUY);
			if (orderRequestDto.getType().equals(OrderType.market)) {
				orderRequestDto.setLimitPrice(null);
			}
			buySecurityOrder = new OrderDetailsResponse();
			orderDetails = alpacaService.buySecurityOrder(orderRequestDto, userId);
			saveBuyOrderDetails(userId, orderDetails);
			buySecurityOrder.setOrderDetails(Arrays.asList(orderDetails));
			logger.debug("successfully processed the buy order (TradingService Layer)");
		} catch (Exception ex) {
			logger.error("buySecurityOrder:(TradingService Layer) {}", methodArgs);
			handleExceptions(ex, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(), "buySecurityOrder",
					methodArgs);
		}
		return buySecurityOrder;
	}

	/**
	 * This method is used to sell a security with quantity by passing userId
	 * ,securitySymbol and quantity into the
	 * alpacaService.sellSecurityOrderWithQuantity() method which will return the
	 * successfully processed sell order response and we save that response into the
	 * database and we will pass that response to the controller layer
	 *
	 * @param userId
	 * @param securitySymbol
	 * @param quantity
	 * @return sellOrderWithQuantity
	 * @throws WayServiceException
	 */
	@Override
	public OrderDetailsResponse sellSecurityOrderWithQuantity(String securitySymbol, String quantity, Integer userId)
			throws WayServiceException {
		String methodArgs = getExceptionUtil().methodInputArgsAsString(userId);
		OrderDetailsResponse sellOrderWithQuantity = null;
		OrderDetailsResponseDto sellOrderWithQuantityResponse = null;
		try {
			sellOrderWithQuantity = new OrderDetailsResponse();
			sellOrderWithQuantityResponse = alpacaService.sellSecurityOrderWithQuantity(securitySymbol, quantity,
					userId);
			saveSellOrderWithQuantityDetails(userId, sellOrderWithQuantityResponse);
			sellOrderWithQuantity.setOrderDetails(Arrays.asList(sellOrderWithQuantityResponse));
			logger.debug("successfully processed a sell order with quantity (TradingService Layer) {}", userId);
		} catch (Exception ex) {
			logger.error("sellSecurityOrderWithQuantity:(TradingService Layer) {}", methodArgs);
			handleExceptions(ex, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(),
					"sellSecurityOrderWithQuantity", methodArgs);
		}
		return sellOrderWithQuantity;
	}

	/**
	 * This method is used to sell a security by passing userId and securitySymbol
	 * into the alpacaService.sellSecurityOrder() method which will return the
	 * successfully processed sell order response and we save that response into the
	 * database and we will pass that response to the controller layer
	 *
	 * @param userId
	 * @param securitySymbol
	 * @return sellOrderDetail
	 * @throws WayServiceException
	 */
	@Override
	public OrderDetailsResponse sellSecurityOrder(String securitySymbol, Integer userId) throws WayServiceException {
		String methodArgs = getExceptionUtil().methodInputArgsAsString(userId);
		OrderDetailsResponse sellOrderDetail = null;
		OrderDetailsResponseDto orderDetailsResponse = null;
		try {
			sellOrderDetail = new OrderDetailsResponse();
			orderDetailsResponse = alpacaService.sellSecurityOrder(securitySymbol, userId);
			saveSellOrderDetails(userId, orderDetailsResponse);
			sellOrderDetail.setOrderDetails(Arrays.asList(orderDetailsResponse));
			logger.debug("successfully processed sell order (TradingService Layer) {}", userId);
		} catch (Exception ex) {
			logger.error("sellSecurityOrder:(TradingService Layer) {}", methodArgs);
			handleExceptions(ex, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(), "sellSecurityOrder",
					methodArgs);
		}
		return sellOrderDetail;
	}

	/**
	 * This method is used to save the sellOrderDetails response
	 *
	 * @param userId
	 * @param sellOrderResponse
	 * @throws WayServiceException
	 */
	public void saveSellOrderDetails(Integer userId, OrderDetailsResponseDto sellOrderResponse)
			throws WayServiceException {
		String methodArgs = getExceptionUtil().methodInputArgsAsString(userId);
		Connection conn = null;
		try {
			conn = connectionUtil.getConnection();
			conn.setAutoCommit(false);
			stockRewardsRepository.saveSecurityOrderDetails(conn, userId, sellOrderResponse);
			conn.commit();
			logger.debug("successfully save sell order details(TradingService Layer) {}", userId);
		} catch (Exception ex) {
			// Rolling back the transaction if there is exception
			try {
				conn.rollback();
			} catch (SQLException e) {
				logger.error(StockReward.SAVE_ORDER_DETAILS + StockReward.TRADING_LOG_INFO, methodArgs);
				handleExceptions(ex, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(),
						StockReward.SAVE_ORDER_DETAILS, methodArgs);
			}
			logger.error(StockReward.SAVE_ORDER_DETAILS + StockReward.TRADING_LOG_INFO, methodArgs);
			handleExceptions(ex, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(),
					StockReward.SAVE_ORDER_DETAILS, methodArgs);
		} finally {
			ConnectionUtil.release(conn);
		}

	}

	/**
	 * This method is used to create a journal security order by passing userId and
	 * journalRequestDto into the alpacaService.createJournalSecurityOrder() method
	 * which will return the successfully processed journal security order response
	 * and we save that response into the database and we will pass that response to
	 * the controller layer
	 *
	 * @param userId
	 * @param JournalRequestDto
	 * @return journalSecurityOrder
	 * @throws WayServiceException
	 */
	@Override
	public JournalOrderResponse createJournalSecurityOrder(JournalRequestDto journalRequestDto, Integer userId)
			throws WayServiceException {
		String methodArgs = getExceptionUtil().methodInputArgsAsString(userId);
		JournalOrderResponse journalSecurityOrder = null;
		JournalOrderResponseDto journalSecurityOrderResponse = null;
		try {
			journalSecurityOrder = new JournalOrderResponse();
			journalSecurityOrderResponse = alpacaService.createJournalSecurityOrder(journalRequestDto, userId);
			saveJournalOrderDetails(userId, journalSecurityOrderResponse);
			journalSecurityOrder.setJournalOrderDetails(journalSecurityOrderResponse);
			logger.debug("successfully processed journal security order (TradingService Layer)");
		} catch (Exception ex) {
			logger.error("createJournalSecurityOrder:(TradingService Layer) {}", methodArgs);
			handleExceptions(ex, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(),
					"createJournalSecurityOrder", methodArgs);
		}
		return journalSecurityOrder;
	}

	/**
	 * This method is used to create a journal cash order by passing userId and
	 * journalRequestDto into the alpacaService.createJournalCashOrder() method
	 * which will return the successfully processed journal cash order response and
	 * we save that response into the database and we will pass that response to the
	 * controller layer
	 *
	 * @param userId
	 * @param JournalRequestDto
	 * @return journalCashOrder
	 * @throws WayServiceException
	 */
	@Override
	public JournalOrderResponse createJournalCashOrder(@Valid JournalRequestDto journalRequestDto, Integer userId)
			throws WayServiceException {
		String methodArgs = getExceptionUtil().methodInputArgsAsString(userId);
		JournalOrderResponse journalCashOrder = null;
		JournalOrderResponseDto journalCashOrderResponse = null;
		try {
			journalCashOrder = new JournalOrderResponse();
			journalCashOrderResponse = alpacaService.createJournalCashOrder(journalRequestDto, userId);
			saveJournalOrderDetails(userId, journalCashOrderResponse);
			journalCashOrder.setJournalOrderDetails(journalCashOrderResponse);
			logger.debug("successfully processed journal cash order (TradingService Layer)");
		} catch (Exception ex) {
			logger.error("createJournalCashOrder:(TradingService Layer) {}", methodArgs);
			handleExceptions(ex, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(),
					"createJournalCashOrder", methodArgs);
		}
		return journalCashOrder;
	}

	/**
	 * This method is used to save the buyOrderDetails response
	 *
	 * @param userId
	 * @param orderDetails
	 * @throws WayServiceException
	 */
	public void saveBuyOrderDetails(Integer userId, OrderDetailsResponseDto orderDetails) throws WayServiceException {
		String methodArgs = getExceptionUtil().methodInputArgsAsString(userId);
		Connection conn = null;
		try {
			conn = connectionUtil.getConnection();
			conn.setAutoCommit(false);
			userId = 5;
			stockRewardsRepository.saveSecurityOrderDetails(conn, userId, orderDetails);
			conn.commit();
			logger.debug("successfully save buy order details (TradingService Layer) {}", userId);
		} catch (Exception ex) {
			// Rolling back the transaction if there is exception
			try {
				conn.rollback();
			} catch (SQLException e) {
				logger.error(StockReward.SAVE_ORDER_DETAILS + StockReward.TRADING_LOG_INFO, methodArgs);
				handleExceptions(ex, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(),
						StockReward.SAVE_ORDER_DETAILS, methodArgs);
			}
			logger.error(StockReward.SAVE_ORDER_DETAILS + StockReward.TRADING_LOG_INFO, methodArgs);
			handleExceptions(ex, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(),
					StockReward.SAVE_ORDER_DETAILS, methodArgs);
		} finally {
			ConnectionUtil.release(conn);
		}
	}

	/**
	 * This method is used to save the sellOrder with quantity Details response
	 *
	 * @param userId
	 * @param orderResponseDetails
	 * @throws WayServiceException
	 */
	public void saveSellOrderWithQuantityDetails(Integer userId, OrderDetailsResponseDto orderResponseDetails)
			throws WayServiceException {
		String methodArgs = getExceptionUtil().methodInputArgsAsString(userId);
		Connection conn = null;
		try {
			conn = connectionUtil.getConnection();
			conn.setAutoCommit(false);
			stockRewardsRepository.saveSecurityOrderDetails(conn, userId, orderResponseDetails);
			conn.commit();
			logger.debug("successfully save sell oder with quantity details (TradingService Layer) {}", userId);
		} catch (Exception ex) {
			// Rolling back the transaction if there is exception
			try {
				conn.rollback();
			} catch (SQLException e) {
				logger.error(StockReward.SAVE_ORDER_DETAILS + StockReward.TRADING_LOG_INFO, methodArgs);
				handleExceptions(ex, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(),
						StockReward.SAVE_ORDER_DETAILS, methodArgs);
			}
			logger.error(StockReward.SAVE_ORDER_DETAILS + StockReward.TRADING_LOG_INFO, methodArgs);
			handleExceptions(ex, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(),
					StockReward.SAVE_ORDER_DETAILS, methodArgs);
		} finally {
			ConnectionUtil.release(conn);
		}
	}

	/**
	 * This method is used to save the journal order response
	 *
	 * @param userId
	 * @param journalOrderResponse
	 * @throws WayServiceException
	 */
	public void saveJournalOrderDetails(Integer userId, JournalOrderResponseDto journalOrderResponse)
			throws WayServiceException {
		String methodArgs = getExceptionUtil().methodInputArgsAsString(userId);
		Connection conn = null;
		try {
			conn = connectionUtil.getConnection();
			conn.setAutoCommit(false);
			stockRewardsRepository.saveJournalOrderDetails(conn, userId, journalOrderResponse);
			conn.commit();
			logger.debug("successfully save journal order details (TradingService Layer)");
		} catch (Exception ex) {
			// Rolling back the transaction if there is exception
			try {
				conn.rollback();
			} catch (SQLException e) {
				logger.error("saveJournalOrderDetails:(TradingService Layer) {}", methodArgs);
				handleExceptions(ex, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(),
						"saveJournalOrderDetails", methodArgs);
			}
			logger.error("saveJournalOrderDetails:(TradingService Layer) {}", methodArgs);
			handleExceptions(ex, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(),
					"saveJournalOrderDetails", methodArgs);
		} finally {
			ConnectionUtil.release(conn);
		}
	}

	/**
	 * This method is used to save the user account information along with
	 * agreements and disclosures with quantity Details response
	 *
	 * @param userId
	 * @param registeredUserInfo
	 * @param registerUserRequestDto
	 * @throws WayServiceException
	 */
	public void saveUserAccountInformation(Integer userId, UserAccountInfoResponseDto registeredUserInfo,
			RegisterUserRequestDto registerUserRequestDto) throws WayServiceException {
		String methodArgs = getExceptionUtil().methodInputArgsAsString(userId);
		Connection conn = null;
		try {
			conn = connectionUtil.getConnection();
			conn.setAutoCommit(false);
			Integer userAccountInfoId = stockRewardsRepository.saveUserAccountInfo(conn, userId, registeredUserInfo);
			stockRewardsRepository.saveUserDisclosures(conn, userId, registerUserRequestDto, userAccountInfoId);
			stockRewardsRepository.saveUserAgreements(conn, userId, registerUserRequestDto.getAgreements(),
					userAccountInfoId);
			conn.commit();
			logger.debug("successfully saved userAccount info: (TradingService Layer) {}", userId);
		} catch (Exception ex) {
			// Rolling back the transaction if there is exception
			try {
				conn.rollback();
			} catch (SQLException e) {
				logger.error("saveUserAccountInformation:(TradingService Layer) {}", methodArgs);
				handleExceptions(ex, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(),
						"saveUserAccountInformation", methodArgs);
			}
			logger.error("saveUserAccountInformation:(TradingService Layer) {}", methodArgs);
			handleExceptions(ex, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(),
					"saveUserAccountInformation", methodArgs);
		} finally {
			ConnectionUtil.release(conn);
		}
	}

}
