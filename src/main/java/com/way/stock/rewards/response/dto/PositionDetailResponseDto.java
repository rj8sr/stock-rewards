package com.way.stock.rewards.response.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.way.core.util.dto.BaseResponseDto;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PositionDetailResponseDto extends BaseResponseDto implements Serializable {

	private static final long serialVersionUID = -49890034307840567L;

	@JsonProperty("asset_id")
	private String assetId;

	@JsonProperty("symbol")
	private String symbol;

	@JsonProperty("exchange")
	private String exchange;

	@JsonProperty("asset_class")
	private String assetClass;

	@JsonProperty("asset_marginable")
	private String assetMarginable;

	@JsonProperty("qty")
	private String quantity;

	@JsonProperty("avg_entry_price")
	private String averageEntryPrice;

	@JsonProperty("side")
	private String side;

	@JsonProperty("market_value")
	private String marketValue;

	@JsonProperty("cost_basis")
	private String costBasis;

	@JsonProperty("unrealized_pl")
	private String unrealizedPl;

	@JsonProperty("unrealized_plpc")
	private String unrealizedPlpc;

	@JsonProperty("unrealized_intraday_pl")
	private String unrealizedIntradayPl;

	@JsonProperty("unrealized_intraday_plpc")
	private String unrealizedIntradayPlpc;

	@JsonProperty("current_price")
	private String currentPrice;

	@JsonProperty("lastday_price")
	private String lastdayPrice;

	@JsonProperty("change_today")
	private String changeToday;

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

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public String getAssetClass() {
		return assetClass;
	}

	public void setAssetClass(String assetClass) {
		this.assetClass = assetClass;
	}

	public String getAssetMarginable() {
		return assetMarginable;
	}

	public void setAssetMarginable(String assetMarginable) {
		this.assetMarginable = assetMarginable;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getAverageEntryPrice() {
		return averageEntryPrice;
	}

	public void setAverageEntryPrice(String averageEntryPrice) {
		this.averageEntryPrice = averageEntryPrice;
	}

	public String getSide() {
		return side;
	}

	public void setSide(String side) {
		this.side = side;
	}

	public String getMarketValue() {
		return marketValue;
	}

	public void setMarketValue(String marketValue) {
		this.marketValue = marketValue;
	}

	public String getCostBasis() {
		return costBasis;
	}

	public void setCostBasis(String costBasis) {
		this.costBasis = costBasis;
	}

	public String getUnrealizedPl() {
		return unrealizedPl;
	}

	public void setUnrealizedPl(String unrealizedPl) {
		this.unrealizedPl = unrealizedPl;
	}

	public String getUnrealizedPlpc() {
		return unrealizedPlpc;
	}

	public void setUnrealizedPlpc(String unrealizedPlpc) {
		this.unrealizedPlpc = unrealizedPlpc;
	}

	public String getUnrealizedIntradayPl() {
		return unrealizedIntradayPl;
	}

	public void setUnrealizedIntradayPl(String unrealizedIntradayPl) {
		this.unrealizedIntradayPl = unrealizedIntradayPl;
	}

	public String getUnrealizedIntradayPlpc() {
		return unrealizedIntradayPlpc;
	}

	public void setUnrealizedIntradayPlpc(String unrealizedIntradayPlpc) {
		this.unrealizedIntradayPlpc = unrealizedIntradayPlpc;
	}

	public String getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(String currentPrice) {
		this.currentPrice = currentPrice;
	}

	public String getLastdayPrice() {
		return lastdayPrice;
	}

	public void setLastdayPrice(String lastdayPrice) {
		this.lastdayPrice = lastdayPrice;
	}

	public String getChangeToday() {
		return changeToday;
	}

	public void setChangeToday(String changeToday) {
		this.changeToday = changeToday;
	}

}
