package com.way.stock.rewards.response.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.way.core.util.dto.BaseResponseDto;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDetailsResponseDto extends BaseResponseDto implements Serializable {

	private static final long serialVersionUID = -3694682111520799581L;

	private Integer userId;

	@JsonProperty("id")
	private String orderId;

	@JsonProperty("client_order_id")
	private String clientOrderId;

	@JsonProperty("created_at")
	private String createdAt;

	@JsonProperty("updated_at")
	private String updatedAt;

	@JsonProperty("submitted_at")
	private String submittedAt;

	@JsonProperty("filled_at")
	private String filledAt;

	@JsonProperty("expired_at")
	private String expiredAt;

	@JsonProperty("canceled_at")
	private String canceledAt;

	@JsonProperty("failed_at")
	private String failedAt;

	@JsonProperty("asset_id")
	private String assetId;

	@JsonProperty("symbol")
	private String symbol;

	@JsonProperty("asset_class")
	private String assetClass;

	@JsonProperty("qty")
	private String quantity;

	@JsonProperty("filled_qty")
	private String filledQuantity;

	@JsonProperty("filled_avg_price")
	private String filledAveragePrice;

	@JsonProperty("type")
	private String type;

	@JsonProperty("side")
	private String side;

	@JsonProperty("time_in_force")
	private String timeInForce;

	@JsonProperty("limit_price")
	private String limitPrice;

	@JsonProperty("stop_price")
	private String stopPrice;

	@JsonProperty("status")
	private String status;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getClientOrderId() {
		return clientOrderId;
	}

	public void setClientOrderId(String clientOrderId) {
		this.clientOrderId = clientOrderId;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getSubmittedAt() {
		return submittedAt;
	}

	public void setSubmittedAt(String submittedAt) {
		this.submittedAt = submittedAt;
	}

	public String getFilledAt() {
		return filledAt;
	}

	public void setFilledAt(String filledAt) {
		this.filledAt = filledAt;
	}

	public String getExpiredAt() {
		return expiredAt;
	}

	public void setExpiredAt(String expiredAt) {
		this.expiredAt = expiredAt;
	}

	public String getCanceledAt() {
		return canceledAt;
	}

	public void setCanceledAt(String canceledAt) {
		this.canceledAt = canceledAt;
	}

	public String getFailedAt() {
		return failedAt;
	}

	public void setFailedAt(String failedAt) {
		this.failedAt = failedAt;
	}

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getAssetClass() {
		return assetClass;
	}

	public void setAssetClass(String assetClass) {
		this.assetClass = assetClass;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getFilledQuantity() {
		return filledQuantity;
	}

	public void setFilledQuantity(String filledQuantity) {
		this.filledQuantity = filledQuantity;
	}

	public String getFilledAveragePrice() {
		return filledAveragePrice;
	}

	public void setFilledAveragePrice(String filledAveragePrice) {
		this.filledAveragePrice = filledAveragePrice;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSide() {
		return side;
	}

	public void setSide(String side) {
		this.side = side;
	}

	public String getTimeInForce() {
		return timeInForce;
	}

	public void setTimeInForce(String timeInForce) {
		this.timeInForce = timeInForce;
	}

	public String getLimitPrice() {
		return limitPrice;
	}

	public void setLimitPrice(String limitPrice) {
		this.limitPrice = limitPrice;
	}

	public String getStopPrice() {
		return stopPrice;
	}

	public void setStopPrice(String stopPrice) {
		this.stopPrice = stopPrice;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
