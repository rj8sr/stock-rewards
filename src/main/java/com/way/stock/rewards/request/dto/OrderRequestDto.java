package com.way.stock.rewards.request.dto;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.way.stock.rewards.enums.OrderType;
import com.way.stock.rewards.enums.TimeInForce;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderRequestDto {

	@ApiModelProperty(required = true)
	@NotNull(message = "Symbol Can't be null")
	@JsonProperty("symbol")
	private String symbol;

	@ApiModelProperty(required = true)
	@NotNull(message = "Quantity Can't be null")
	@JsonProperty("qty")
	private String quantity;

	@ApiModelProperty(required = false)
	@JsonProperty("side")
	private String side;

	@ApiModelProperty(required = true)
	@NotNull(message = "Type Can't be null")
	@JsonProperty("type")
	private OrderType type;

	@ApiModelProperty(required = true)
	@NotNull(message = "Time in force Can't be null")
	@JsonProperty("time_in_force")
	private TimeInForce timeInForce;

	@ApiModelProperty(required = false)
	@JsonProperty("limit_price")
	private String limitPrice;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getSide() {
		return side;
	}

	public void setSide(String side) {
		this.side = side;
	}

	public OrderType getType() {
		return type;
	}

	public void setType(OrderType type) {
		this.type = type;
	}

	public TimeInForce getTimeInForce() {
		return timeInForce;
	}

	public void setTimeInForce(TimeInForce timeInForce) {
		this.timeInForce = timeInForce;
	}

	public String getLimitPrice() {
		return limitPrice;
	}

	public void setLimitPrice(String limitPrice) {
		this.limitPrice = limitPrice;
	}

}
