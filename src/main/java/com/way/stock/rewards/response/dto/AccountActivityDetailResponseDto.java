package com.way.stock.rewards.response.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.way.core.util.dto.BaseResponseDto;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountActivityDetailResponseDto extends BaseResponseDto implements Serializable {

	private static final long serialVersionUID = 5190339011921781243L;

	@JsonProperty("id")
	private String activityId;

	@JsonProperty("account_id")
	private String accountId;

	@JsonProperty("activity_type")
	private String activityType;

	@JsonProperty("date")
	private String date;

	@JsonProperty("net_amount")
	private String netAmount;

	@JsonProperty("description")
	private String description;

	@JsonProperty("symbol")
	private String symbol;

	@JsonProperty("qty")
	private String quantity;

	@JsonProperty("price")
	private String price;

	@JsonProperty("status")
	private String status;

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(String netAmount) {
		this.netAmount = netAmount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

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

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}