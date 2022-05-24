package com.way.stock.rewards.response.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAccountInfoResponseDto implements Serializable {

	private static final long serialVersionUID = 4381081562115986628L;

	@JsonProperty("id")
	private String accountId;

	@JsonProperty("account_number")
	private String accountNumber;

	@JsonProperty("status")
	private String status;

	@JsonProperty("currency")
	private String currency;

	@JsonProperty("last_equity")
	private String lastEquity;

	@JsonProperty("created_At")
	private String createdAt;

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

	public String getLastEquity() {
		return lastEquity;
	}

	public void setLastEquity(String lastEquity) {
		this.lastEquity = lastEquity;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

}
