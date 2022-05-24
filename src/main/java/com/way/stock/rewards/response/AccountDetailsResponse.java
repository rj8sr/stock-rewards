package com.way.stock.rewards.response;

import com.way.core.util.dto.BaseResponseDto;
import com.way.stock.rewards.response.dto.AccountDetailsResponseDto;

public class AccountDetailsResponse extends BaseResponseDto {

	private static final long serialVersionUID = 5783969662298391505L;
	private AccountDetailsResponseDto accountDetails;

	public AccountDetailsResponseDto getAccountDetails() {
		return accountDetails;
	}

	public void setAccountDetails(AccountDetailsResponseDto accountDetails) {
		this.accountDetails = accountDetails;
	}

}
