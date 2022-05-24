package com.way.stock.rewards.response;

import com.way.core.util.dto.BaseResponseDto;
import com.way.stock.rewards.response.dto.UserAccountInfoResponseDto;

public class UserAccountInfoResponse extends BaseResponseDto {

	private static final long serialVersionUID = 4165704133142311861L;
	private UserAccountInfoResponseDto userAccountInfo;

	public UserAccountInfoResponseDto getUserAccountInfo() {
		return userAccountInfo;
	}

	public void setUserAccountInfo(UserAccountInfoResponseDto userAccountInfo) {
		this.userAccountInfo = userAccountInfo;
	}

}
