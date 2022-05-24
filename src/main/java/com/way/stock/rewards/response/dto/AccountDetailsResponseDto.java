package com.way.stock.rewards.response.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDetailsResponseDto implements Serializable {

	private static final long serialVersionUID = 8554462075792573718L;

	@JsonProperty("id")
	private String accountId;

	@JsonProperty("account_number")
	private String accountNumber;

	@JsonProperty("status")
	private String status;

	@JsonProperty("currency")
	private String currency;

	@JsonProperty("buying_power")
	private String buyingPower;

	@JsonProperty("regt_buying_power")
	private String regtBuyingPower;

	@JsonProperty("daytrading_buying_power")
	private String daytradingBuyingPower;

	@JsonProperty("non_marginable_buying_power")
	private String nonMarginableBuyingPower;

	@JsonProperty("cash")
	private String cash;

	@JsonProperty("cash_withdrawable")
	private String cashWithdrawable;

	@JsonProperty("cash_transferable")
	private String cashTransferable;

	@JsonProperty("pending_transfer_out")
	private String pendingTransferOut;

	@JsonProperty("pending_transfer_in")
	private String pendingTransferIn;

	@JsonProperty("portfolio_value")
	private String portfolioValue;

	@JsonProperty("pattern_day_trader")
	private String patternDayTrader;

	@JsonProperty("trading_blocked")
	private String tradingBlocked;

	@JsonProperty("transfers_blocked")
	private String transfersBlocked;

	@JsonProperty("account_blocked")
	private String accountBlocked;

	@JsonProperty("created_at")
	private String createdAt;

	@JsonProperty("trade_suspended_by_user")
	private String tradeSuspendedByUser;

	@JsonProperty("multiplier")
	private String multiplier;

	@JsonProperty("shorting_enabled")
	private String shortingEnabled;

	@JsonProperty("equity")
	private String equity;

	@JsonProperty("last_equity")
	private String lastEquity;

	@JsonProperty("long_market_value")
	private String longMarketValue;

	@JsonProperty("short_market_value")
	private String shortMarketValue;

	@JsonProperty("initial_margin")
	private String initialMargin;

	@JsonProperty("maintenance_margin")
	private String maintenanceMargin;

	@JsonProperty("last_maintenance_margin")
	private String lastMaintenanceMargin;

	@JsonProperty("sma")
	private String sma;

	@JsonProperty("daytrade_count")
	private String daytradeCount;

	@JsonProperty("previous_close")
	private String previousClose;

	@JsonProperty("last_long_market_value")
	private String lastLongMarketValue;

	@JsonProperty("last_short_market_value")
	private String lastShortMarketValue;

	@JsonProperty("last_cash")
	private String lastCash;

	@JsonProperty("last_initial_margin")
	private String lastInitialMargin;

	@JsonProperty("last_regt_buying_power")
	private String lastRegtBuyingPower;

	@JsonProperty("last_daytrading_buying_power")
	private String lastDaytradingBuyingPower;

	@JsonProperty("last_buying_power")
	private String lastBuyingPower;

	@JsonProperty("last_daytrade_count")
	private String lastDaytradeCount;

	@JsonProperty("clearing_broker")
	private String clearingBroker;

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getBuyingPower() {
		return buyingPower;
	}

	public void setBuyingPower(String buyingPower) {
		this.buyingPower = buyingPower;
	}

	public String getRegtBuyingPower() {
		return regtBuyingPower;
	}

	public void setRegtBuyingPower(String regtBuyingPower) {
		this.regtBuyingPower = regtBuyingPower;
	}

	public String getDaytradingBuyingPower() {
		return daytradingBuyingPower;
	}

	public void setDaytradingBuyingPower(String daytradingBuyingPower) {
		this.daytradingBuyingPower = daytradingBuyingPower;
	}

	public String getNonMarginableBuyingPower() {
		return nonMarginableBuyingPower;
	}

	public void setNonMarginableBuyingPower(String nonMarginableBuyingPower) {
		this.nonMarginableBuyingPower = nonMarginableBuyingPower;
	}

	public String getCash() {
		return cash;
	}

	public void setCash(String cash) {
		this.cash = cash;
	}

	public String getCashWithdrawable() {
		return cashWithdrawable;
	}

	public void setCashWithdrawable(String cashWithdrawable) {
		this.cashWithdrawable = cashWithdrawable;
	}

	public String getCashTransferable() {
		return cashTransferable;
	}

	public void setCashTransferable(String cashTransferable) {
		this.cashTransferable = cashTransferable;
	}

	public String getPendingTransferOut() {
		return pendingTransferOut;
	}

	public void setPendingTransferOut(String pendingTransferOut) {
		this.pendingTransferOut = pendingTransferOut;
	}

	public String getPendingTransferIn() {
		return pendingTransferIn;
	}

	public void setPendingTransferIn(String pendingTransferIn) {
		this.pendingTransferIn = pendingTransferIn;
	}

	public String getPortfolioValue() {
		return portfolioValue;
	}

	public void setPortfolioValue(String portfolioValue) {
		this.portfolioValue = portfolioValue;
	}

	public String getPatternDayTrader() {
		return patternDayTrader;
	}

	public void setPatternDayTrader(String patternDayTrader) {
		this.patternDayTrader = patternDayTrader;
	}

	public String getTradingBlocked() {
		return tradingBlocked;
	}

	public void setTradingBlocked(String tradingBlocked) {
		this.tradingBlocked = tradingBlocked;
	}

	public String getTransfersBlocked() {
		return transfersBlocked;
	}

	public void setTransfersBlocked(String transfersBlocked) {
		this.transfersBlocked = transfersBlocked;
	}

	public String getAccountBlocked() {
		return accountBlocked;
	}

	public void setAccountBlocked(String accountBlocked) {
		this.accountBlocked = accountBlocked;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getTradeSuspendedByUser() {
		return tradeSuspendedByUser;
	}

	public void setTradeSuspendedByUser(String tradeSuspendedByUser) {
		this.tradeSuspendedByUser = tradeSuspendedByUser;
	}

	public String getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(String multiplier) {
		this.multiplier = multiplier;
	}

	public String getShortingEnabled() {
		return shortingEnabled;
	}

	public void setShortingEnabled(String shortingEnabled) {
		this.shortingEnabled = shortingEnabled;
	}

	public String getEquity() {
		return equity;
	}

	public void setEquity(String equity) {
		this.equity = equity;
	}

	public String getLastEquity() {
		return lastEquity;
	}

	public void setLastEquity(String lastEquity) {
		this.lastEquity = lastEquity;
	}

	public String getLongMarketValue() {
		return longMarketValue;
	}

	public void setLongMarketValue(String longMarketValue) {
		this.longMarketValue = longMarketValue;
	}

	public String getShortMarketValue() {
		return shortMarketValue;
	}

	public void setShortMarketValue(String shortMarketValue) {
		this.shortMarketValue = shortMarketValue;
	}

	public String getInitialMargin() {
		return initialMargin;
	}

	public void setInitialMargin(String initialMargin) {
		this.initialMargin = initialMargin;
	}

	public String getMaintenanceMargin() {
		return maintenanceMargin;
	}

	public void setMaintenanceMargin(String maintenanceMargin) {
		this.maintenanceMargin = maintenanceMargin;
	}

	public String getLastMaintenanceMargin() {
		return lastMaintenanceMargin;
	}

	public void setLastMaintenanceMargin(String lastMaintenanceMargin) {
		this.lastMaintenanceMargin = lastMaintenanceMargin;
	}

	public String getSma() {
		return sma;
	}

	public void setSma(String sma) {
		this.sma = sma;
	}

	public String getDaytradeCount() {
		return daytradeCount;
	}

	public void setDaytradeCount(String daytradeCount) {
		this.daytradeCount = daytradeCount;
	}

	public String getPreviousClose() {
		return previousClose;
	}

	public void setPreviousClose(String previousClose) {
		this.previousClose = previousClose;
	}

	public String getLastLongMarketValue() {
		return lastLongMarketValue;
	}

	public void setLastLongMarketValue(String lastLongMarketValue) {
		this.lastLongMarketValue = lastLongMarketValue;
	}

	public String getLastShortMarketValue() {
		return lastShortMarketValue;
	}

	public void setLastShortMarketValue(String lastShortMarketValue) {
		this.lastShortMarketValue = lastShortMarketValue;
	}

	public String getLastCash() {
		return lastCash;
	}

	public void setLastCash(String lastCash) {
		this.lastCash = lastCash;
	}

	public String getLastInitialMargin() {
		return lastInitialMargin;
	}

	public void setLastInitialMargin(String lastInitialMargin) {
		this.lastInitialMargin = lastInitialMargin;
	}

	public String getLastRegtBuyingPower() {
		return lastRegtBuyingPower;
	}

	public void setLastRegtBuyingPower(String lastRegtBuyingPower) {
		this.lastRegtBuyingPower = lastRegtBuyingPower;
	}

	public String getLastDaytradingBuyingPower() {
		return lastDaytradingBuyingPower;
	}

	public void setLastDaytradingBuyingPower(String lastDaytradingBuyingPower) {
		this.lastDaytradingBuyingPower = lastDaytradingBuyingPower;
	}

	public String getLastBuyingPower() {
		return lastBuyingPower;
	}

	public void setLastBuyingPower(String lastBuyingPower) {
		this.lastBuyingPower = lastBuyingPower;
	}

	public String getLastDaytradeCount() {
		return lastDaytradeCount;
	}

	public void setLastDaytradeCount(String lastDaytradeCount) {
		this.lastDaytradeCount = lastDaytradeCount;
	}

	public String getClearingBroker() {
		return clearingBroker;
	}

	public void setClearingBroker(String clearingBroker) {
		this.clearingBroker = clearingBroker;
	}

}
