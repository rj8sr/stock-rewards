package com.way.stock.rewards.service;

import javax.validation.Valid;

import com.way.core.util.dto.PaginationData;
import com.way.exception.util.WayServiceException;
import com.way.stock.rewards.request.dto.JournalRequestDto;
import com.way.stock.rewards.request.dto.OrderRequestDto;
import com.way.stock.rewards.request.dto.RegisterUserRequestDto;
import com.way.stock.rewards.response.AccountActivityDetailsResponse;
import com.way.stock.rewards.response.AccountDetailsResponse;
import com.way.stock.rewards.response.JournalOrderResponse;
import com.way.stock.rewards.response.OrderDetailsResponse;
import com.way.stock.rewards.response.UserAccountInfoResponse;
import com.way.stock.rewards.response.dto.PositionDetailResponseDto;

public interface TradingService {
	AccountDetailsResponse getAccountDetails(Integer userId) throws WayServiceException;

	PaginationData<AccountActivityDetailsResponse> getAccountActivityDetails(Integer page, Integer size, Integer range,
			Integer userId) throws WayServiceException;

	PaginationData<PositionDetailResponseDto> getAccountPositionDetails(Integer page, Integer size, Integer userId)
			throws WayServiceException;

	UserAccountInfoResponse registerUser(@Valid RegisterUserRequestDto registerUserRequestDto, String ipAddress,
			Integer userId) throws WayServiceException;

	OrderDetailsResponse buySecurityOrder(@Valid OrderRequestDto orderRequest, Integer userId)
			throws WayServiceException;

	OrderDetailsResponse sellSecurityOrderWithQuantity(String securitySymbol, String quantity, Integer userId)
			throws WayServiceException;

	OrderDetailsResponse sellSecurityOrder(String securitySymbol, Integer userId) throws WayServiceException;

	JournalOrderResponse createJournalSecurityOrder(@Valid JournalRequestDto journalRequestDto, Integer userId)
			throws WayServiceException;

	JournalOrderResponse createJournalCashOrder(@Valid JournalRequestDto journalRequestDto, Integer userId)
			throws WayServiceException;

}
