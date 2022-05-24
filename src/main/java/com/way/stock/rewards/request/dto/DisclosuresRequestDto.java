package com.way.stock.rewards.request.dto;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DisclosuresRequestDto {

	@ApiModelProperty(required = true)
	@NotNull(message = "isControlPerson Can't be null")
	@JsonProperty("is_control_person")
	private boolean isControlPerson;

	@ApiModelProperty(required = true)
	@NotNull(message = "isAffiliatedExchangeOrFinra Can't be null")
	@JsonProperty("is_affiliated_exchange_or_finra")
	private boolean isAffiliatedExchangeOrFinra;

	@ApiModelProperty(required = true)
	@NotNull(message = "isPoliticallyExposed Can't be null")
	@JsonProperty("is_politically_exposed")
	private boolean isPoliticallyExposed;

	@ApiModelProperty(required = true)
	@NotNull(message = "immediateFamilyExposed Can't be null")
	@JsonProperty("immediate_family_exposed")
	private boolean immediateFamilyExposed;

	public boolean isControlPerson() {
		return isControlPerson;
	}

	public void setControlPerson(boolean isControlPerson) {
		this.isControlPerson = isControlPerson;
	}

	public boolean isAffiliatedExchangeOrFinra() {
		return isAffiliatedExchangeOrFinra;
	}

	public void setAffiliatedExchangeOrFinra(boolean isAffiliatedExchangeOrFinra) {
		this.isAffiliatedExchangeOrFinra = isAffiliatedExchangeOrFinra;
	}

	public boolean isPoliticallyExposed() {
		return isPoliticallyExposed;
	}

	public void setPoliticallyExposed(boolean isPoliticallyExposed) {
		this.isPoliticallyExposed = isPoliticallyExposed;
	}

	public boolean isImmediateFamilyExposed() {
		return immediateFamilyExposed;
	}

	public void setImmediateFamilyExposed(boolean immediateFamilyExposed) {
		this.immediateFamilyExposed = immediateFamilyExposed;
	}

}
