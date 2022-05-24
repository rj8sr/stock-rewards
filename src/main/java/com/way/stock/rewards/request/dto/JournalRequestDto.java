package com.way.stock.rewards.request.dto;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.way.stock.rewards.enums.JournalType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JournalRequestDto {

	@ApiModelProperty(required = true)
	@NotNull(message = "From Account Can't be null")
	@JsonProperty("from_account")
	private String fromAccount;

	@ApiModelProperty(required = true)
	@NotNull(message = "Entry Type Can't be null")
	@JsonProperty("entry_type")
	private JournalType entryType;

	@ApiModelProperty(required = false)
	@JsonProperty("symbol")
	private String symbol;

	@ApiModelProperty(required = false)
	@JsonProperty("qty")
	private String quantity;

	@ApiModelProperty(required = true)
	@NotNull(message = "To Account Can't be null")
	@JsonProperty("to_account")
	private String toAccount;

	@ApiModelProperty(required = true)
	@NotNull(message = "Description Can't be null")
	@JsonProperty("description")
	private String description;

	@ApiModelProperty(required = false)
	@JsonProperty("amount")
	private String amount;

	public String getFromAccount() {
		return fromAccount;
	}

	public void setFromAccount(String fromAccount) {
		this.fromAccount = fromAccount;
	}

	public JournalType getEntryType() {
		return entryType;
	}

	public void setEntryType(JournalType entryType) {
		this.entryType = entryType;
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

	public String getToAccount() {
		return toAccount;
	}

	public void setToAccount(String toAccount) {
		this.toAccount = toAccount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

}
