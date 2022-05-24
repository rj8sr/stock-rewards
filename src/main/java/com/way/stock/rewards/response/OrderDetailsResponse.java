package com.way.stock.rewards.response;

import java.util.List;

import com.way.core.util.dto.BaseResponseDto;
import com.way.stock.rewards.response.dto.OrderDetailsResponseDto;

public class OrderDetailsResponse extends BaseResponseDto {

	private static final long serialVersionUID = -6146883790583777972L;
	private List<OrderDetailsResponseDto> orderDetails;

	public List<OrderDetailsResponseDto> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(List<OrderDetailsResponseDto> orderDetails) {
		this.orderDetails = orderDetails;
	}

}
