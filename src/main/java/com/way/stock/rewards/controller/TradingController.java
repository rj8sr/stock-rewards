package com.way.stock.rewards.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.way.core.util.dto.BaseResponseDto;
import com.way.core.util.dto.PaginationData;
import com.way.exception.constants.ExceptionModule;
import com.way.exception.util.BaseController;
import com.way.exception.util.WayControllerException;
import com.way.stock.rewards.constants.StockReward;
import com.way.stock.rewards.request.dto.JournalRequestDto;
import com.way.stock.rewards.request.dto.OrderRequestDto;
import com.way.stock.rewards.request.dto.RegisterUserRequestDto;
import com.way.stock.rewards.response.AccountActivityDetailsResponse;
import com.way.stock.rewards.response.AccountDetailsResponse;
import com.way.stock.rewards.response.JournalOrderResponse;
import com.way.stock.rewards.response.OrderDetailsResponse;
import com.way.stock.rewards.response.UserAccountInfoResponse;
import com.way.stock.rewards.response.dto.PositionDetailResponseDto;
import com.way.stock.rewards.service.TradingService;
import com.way.stock.rewards.util.UserInfoUtil;

/**
 * This Controller layer is used to passing details to TradingService layer
 * methods which internally call AlpacaService layer methods and as a resultant
 * we populate the response
 *
 */
@Controller
@RequestMapping("/v1/trading")
public class TradingController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	TradingService tradingService;

	/**
	 * This method will return the accountDetails of the user in response , either
	 * by passing the userId into the request parameter or fetching it from the
	 * request and we will pass it to the TradingService.getAccountDetails() method
	 * which will internally call AlpacaService.getAccountDetails() method and as a
	 * resultant we will populate the response
	 *
	 * @param userId
	 * @param request
	 * @return accountDetailsResponse
	 * @throws WayControllerException
	 */
	@GetMapping("/account/details")
	public @ResponseBody ResponseEntity<BaseResponseDto> getAccountDetails(
			@RequestParam(value = "userId", required = false) Integer userId, HttpServletRequest request)
			throws WayControllerException {
		String methodArgs = getExceptionUtil().methodInputArgsAsString(userId);
		AccountDetailsResponse accountDetailsResponse = null;
		try {
			if (userId == null) {
				userId = getUserIdFromRequest(request);
			}
			accountDetailsResponse = tradingService.getAccountDetails(userId);
			logger.debug("successfully fetched the userAccount Details (Trading Controller layer) {}", userId);
		} catch (Exception ex) {
			logger.error("getAccountDetails:(Trading Controller layer) {}", methodArgs);
			handleExceptions(ex, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(), "getAccountDetails",
					methodArgs);
		}
		return populateResponse(StockReward.SUCCESSFULLY_FETCHED_ACCOUNT_DETAILS, accountDetailsResponse);
	}

	/**
	 * This method will return the successfully registered user's data in response,
	 * we will pass data we collected from registerUserRequestDto ,request to the
	 * TradingService.registerUser() method which will internally call
	 * AlpacaService.registerUser() method and as a resultant we will populate the
	 * response
	 *
	 * @param registerUserRequestDto
	 * @param request
	 * @return userRegistrationResponse
	 * @throws WayControllerException
	 */
	@PostMapping("/account/register/user")
	public @ResponseBody ResponseEntity<BaseResponseDto> registerUser(
			@RequestParam(value = "userId", required = false) Integer userId,
			@RequestBody @Valid RegisterUserRequestDto registerUserRequestDto, HttpServletRequest request)
			throws WayControllerException {
		String methodArgs = getExceptionUtil().methodInputArgsAsString(registerUserRequestDto);
		UserAccountInfoResponse userRegistrationResponse = null;
		String ipAddress = UserInfoUtil.getUserIpFromRequest(request);
		try {
			if (userId == null) {
				userId = getUserIdFromRequest(request);
			}
			userRegistrationResponse = tradingService.registerUser(registerUserRequestDto, ipAddress, userId);
			logger.debug("successfully registered the user (Trading Controller layer) {}", userId);
		} catch (Exception ex) {
			logger.error("registerUser:(Trading Controller layer) {}", methodArgs);
			handleExceptions(ex, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(), "registerUser",
					methodArgs);
		}
		return populateResponse(StockReward.SUCCESSFULLY_REGISTERED_USER, userRegistrationResponse);
	}

	/**
	 * This method will return the activity details of a particular of the user in
	 * response , either by passing the userId into the request parameter or
	 * fetching it from the request and we will pass it to the
	 * TradingService.getAccountActivityDetails() method and as a resultant we will
	 * populate the response in paginated manner
	 *
	 * @param userId
	 * @param request
	 * @return orderDetails
	 * @throws WayControllerException
	 */
	@GetMapping("/account/activity/details")
	public @ResponseBody ResponseEntity<? extends BaseResponseDto> getAccountActivityDetails(
			@RequestParam(value = "page") Integer page, @RequestParam(value = "size") Integer size,
			@RequestParam(value = "range", required = false) Integer range,
			@RequestParam(value = "userId", required = false) Integer userId, HttpServletRequest request,
			Pageable pageble) throws WayControllerException {
		String methodArgs = getExceptionUtil().methodInputArgsAsString(userId);
		PaginationData<AccountActivityDetailsResponse> activityDetails = null;
		try {
			if (userId == null) {
				userId = getUserIdFromRequest(request);
			}
			activityDetails = tradingService.getAccountActivityDetails(page, size, range, userId);
			logger.debug("successfully fetched the userAccount activity details (Trading Controller layer) {}", userId);
		} catch (Exception ex) {
			logger.error("getAccountActivityDetails:(Trading Controller layer) {}", methodArgs);
			handleExceptions(ex, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(),
					"getAccountActivityDetails", methodArgs);
		}
		return populatePaginationResponse(StockReward.SUCCESSFULLY_FETCHED_ACTIVITY_DETAILS, activityDetails);
	}

	/**
	 * This method will return the account position Details of the user in response,
	 * either by passing the userId into the request parameter or fetching it from
	 * the request and we will pass it to the
	 * TradingService.getAccountActivityDetails() method which will internally call
	 * AlpacaService.getAccountActivityDetails() method and as a resultant we will
	 * populate the response
	 *
	 * @param userId
	 * @param request
	 * @return positionDetail
	 * @throws WayControllerException
	 */
	@GetMapping("/account/position/details")
	public @ResponseBody ResponseEntity<? extends BaseResponseDto> getAccountPositionDetails(
			@RequestParam(value = "page") Integer page, @RequestParam(value = "size") Integer size,
			@RequestParam(value = "userId", required = false) Integer userId, HttpServletRequest request)
			throws WayControllerException {
		String methodArgs = getExceptionUtil().methodInputArgsAsString(userId);
		PaginationData<PositionDetailResponseDto> positionDetail = null;
		try {
			if (userId == null) {
				userId = getUserIdFromRequest(request);
			}
			positionDetail = tradingService.getAccountPositionDetails(page, size, userId);
			logger.debug("successfully fetched the userAccount position details (Trading Controller layer) {}", userId);
		} catch (Exception ex) {
			logger.error("getAccountPositionDetails:(Trading Controller layer) {}", methodArgs);
			handleExceptions(ex, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(),
					"getAccountPositionDetails", methodArgs);
		}
		return populatePaginationResponse(StockReward.SUCCESSFULLY_FETCHED_POSITION_DETAILS, positionDetail);
	}

	/**
	 * This method will return the successfully buy security order data in response,
	 * we will pass data we collected from orderRequest and request to the
	 * TradingService.buySecurityOrder() method which will internally call
	 * AlpacaService.buySecurityOrder() method and as a resultant we will populate
	 * the response
	 *
	 * @param request
	 * @param orderRequest
	 * @return buyOrderDetails
	 * @throws WayControllerException
	 */
	@PostMapping("/account/buy/order")
	public @ResponseBody ResponseEntity<BaseResponseDto> buySecurityOrder(
			@RequestParam(value = "userId", required = false) Integer userId,
			@RequestBody @Valid OrderRequestDto orderRequest, HttpServletRequest request)
			throws WayControllerException {
		String methodArgs = getExceptionUtil().methodInputArgsAsString(orderRequest);
		OrderDetailsResponse buyOrderDetails = null;
		try {
			if (userId == null) {
				userId = getUserIdFromRequest(request);
			}
			buyOrderDetails = tradingService.buySecurityOrder(orderRequest, userId);
			logger.debug("successfully processed a buy order (Trading Controller layer)");
		} catch (Exception ex) {
			logger.error("buySecurityOrder:(Trading Controller layer) {} ", methodArgs);
			handleExceptions(ex, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(), "buySecurityOrder",
					methodArgs);
		}
		return populateResponse(StockReward.SUCCESSFULLY_MADE_BUY_ORDER, buyOrderDetails);
	}

	/**
	 * This method will return the successfully sell security order data in
	 * response, we will pass data we collected from parameters to the
	 * TradingService.sellSecurityOrder() ,
	 * tradingService.sellSecurityOrderWithQuantity methods which will internally
	 * call AlpacaService.sellSecurityOrder(),
	 * AlpacaService.sellSecurityOrderWithQuantity() respectively methods and as a
	 * resultant we will populate the response
	 *
	 * @param securitySymbol
	 * @param quantity
	 * @param request
	 * @return sellOrderDetails
	 * @throws WayControllerException
	 */
	@DeleteMapping("/account/order/sell/{symbol}")
	public @ResponseBody ResponseEntity<BaseResponseDto> sellSecurityOrder(
			@PathVariable("symbol") String securitySymbol,
			@RequestParam(value = "qty", required = false) String quantity,
			@RequestParam(value = "userId", required = false) Integer userId, HttpServletRequest request)
			throws WayControllerException {
		String methodArgs = getExceptionUtil().methodInputArgsAsString();
		OrderDetailsResponse sellOrderDetails = null;
		try {
			if (userId == null) {
				userId = getUserIdFromRequest(request);
			}
			if (quantity == null) {
				sellOrderDetails = tradingService.sellSecurityOrder(securitySymbol, userId);
				logger.debug("successfully processed a sellOrder (Trading Controller layer) {}", userId);
			} else {
				sellOrderDetails = tradingService.sellSecurityOrderWithQuantity(securitySymbol, quantity, userId);
				logger.debug("successfully processed a sellOrder with quantity (Trading Controller layer) {}", userId);
			}
		} catch (Exception ex) {
			logger.error("sellSecurityOrder: (Trading Controller layer) {}", methodArgs);
			handleExceptions(ex, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(), "sellSecurityOrder",
					methodArgs);
		}
		return populateResponse(StockReward.SUCCESSFULLY_MADE_SELL_ORDER, sellOrderDetails);
	}

	/**
	 * This method will return the successfully create journal security order data
	 * in response, we will pass data we collected from journalRequestDto and
	 * request to the TradingService.createJournalSecurityOrder() method which will
	 * internally call AlpacaService.createJournalSecurityOrder() method and as a
	 * resultant we will populate the response
	 *
	 * @param journalRequestDto
	 * @param request
	 * @return journalSecurityOrderDetail
	 * @throws WayControllerException
	 */
	@PostMapping("/account/journal/security/order")
	public @ResponseBody ResponseEntity<BaseResponseDto> createJournalSecurityOrder(
			@RequestBody @Valid JournalRequestDto journalRequestDto,
			@RequestParam(value = "userId", required = false) Integer userId, HttpServletRequest request)
			throws WayControllerException {
		String methodArgs = getExceptionUtil().methodInputArgsAsString(journalRequestDto);
		JournalOrderResponse journalSecurityOrderDetail = null;
		try {
			if (userId == null) {
				userId = getUserIdFromRequest(request);
			}
			journalSecurityOrderDetail = tradingService.createJournalSecurityOrder(journalRequestDto, userId);
			logger.debug("successfully processed a journal security order (Trading Controller layer)");
		} catch (Exception ex) {
			logger.error("createJournalSecurityOrder:(Trading Controller layer) {}", methodArgs);
			handleExceptions(ex, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(),
					"createJournalSecurityOrder", methodArgs);
		}
		return populateResponse(StockReward.SUCCESSFULLY_MADE_JOURNAL_ORDER, journalSecurityOrderDetail);
	}

	/**
	 * This method will return the successfully create journal cash order data in
	 * response, we will pass data we collected from journalRequestDto and request
	 * to the TradingService.createJournalCashOrder() method which will internally
	 * call AlpacaService.createJournalCashOrder() method and as a resultant we will
	 * populate the response
	 * 
	 * @param journalRequestDto
	 * @param userId
	 * @param request
	 * @return journalCashOrderDetail
	 * @throws WayControllerException
	 */
	@PostMapping("/account/journal/cash/order")
	public @ResponseBody ResponseEntity<BaseResponseDto> createJournalCashOrder(
			@RequestBody @Valid JournalRequestDto journalRequestDto,
			@RequestParam(value = "userId", required = false) Integer userId, HttpServletRequest request)
			throws WayControllerException {
		String methodArgs = getExceptionUtil().methodInputArgsAsString(journalRequestDto);
		JournalOrderResponse journalCashOrderDetail = null;
		try {
			if (userId == null) {
				userId = getUserIdFromRequest(request);
			}
			journalCashOrderDetail = tradingService.createJournalCashOrder(journalRequestDto, userId);
			logger.debug("successfully processed a journal cash order (Trading Controller layer)");
		} catch (Exception ex) {
			logger.error("createJournalCashOrder:(Trading Controller layer) {}", methodArgs);
			handleExceptions(ex, logger, userId, ExceptionModule.STOCK_REWARDS, this.getClass(),
					"createJournalCashOrder", methodArgs);
		}
		return populateResponse(StockReward.SUCCESSFULLY_MADE_JOURNAL_ORDER, journalCashOrderDetail);
	}

}
