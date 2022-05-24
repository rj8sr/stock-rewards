package com.way.stock.rewards.response;

import com.way.core.util.dto.BaseResponseDto;
import com.way.stock.rewards.response.dto.JournalOrderResponseDto;

public class JournalOrderResponse extends BaseResponseDto {

	private static final long serialVersionUID = -5046931397433867578L;
	private JournalOrderResponseDto journalOrderDetails;

	public JournalOrderResponseDto getJournalOrderDetails() {
		return journalOrderDetails;
	}

	public void setJournalOrderDetails(JournalOrderResponseDto journalOrderDetails) {
		this.journalOrderDetails = journalOrderDetails;
	}

}
