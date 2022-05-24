package com.way.stock.rewards.service;

import java.util.List;

import javax.validation.Valid;

import com.way.exception.util.WayServiceException;
import com.way.stock.rewards.request.dto.JournalRequestDto;
import com.way.stock.rewards.request.dto.OrderRequestDto;
import com.way.stock.rewards.request.dto.RegisterUserRequestDto;
import com.way.stock.rewards.response.dto.AccountDetailsResponseDto;
import com.way.stock.rewards.response.dto.JournalOrderResponseDto;
import com.way.stock.rewards.response.dto.OrderDetailsResponseDto;
import com.way.stock.rewards.response.dto.PositionDetailResponseDto;
import com.way.stock.rewards.response.dto.UserAccountInfoResponseDto;

public interface AlpacaService {
	AccountDetailsResponseDto getAccountDetails(Integer userId) throws WayServiceException;

	List<PositionDetailResponseDto> getAccountPositionDetails(Integer userId) throws WayServiceException;

	UserAccountInfoResponseDto registerUser(Integer userId, @Valid RegisterUserRequestDto registerUserRequestDto)
			throws WayServiceException;

	OrderDetailsResponseDto buySecurityOrder(@Valid OrderRequestDto orderRequestDto, Integer userId)
			throws WayServiceException;

	OrderDetailsResponseDto sellSecurityOrder(String securitySymbol, Integer userId) throws WayServiceException;

	OrderDetailsResponseDto sellSecurityOrderWithQuantity(String securitySymbol, String quantity, Integer userId)
			throws WayServiceException;

	JournalOrderResponseDto createJournalSecurityOrder(JournalRequestDto journalRequestDto, Integer userId)
			throws WayServiceException;

	JournalOrderResponseDto createJournalCashOrder(@Valid JournalRequestDto journalRequestDto, Integer userId)
			throws WayServiceException;

}
