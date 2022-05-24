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
public class AgreementRequestDto {

	@ApiModelProperty(required = true)
	@NotNull(message = "agreement Can't be null")
	@JsonProperty("agreement")
	private String agreement;

	@ApiModelProperty(required = false)
	@JsonProperty("signed_at")
	private String signedAt;

	@ApiModelProperty(required = false)
	@JsonProperty("ip_address")
	private String ipAddress;

	public String getAgreement() {
		return agreement;
	}

	public void setAgreement(String agreement) {
		this.agreement = agreement;
	}

	public String getSignedAt() {
		return signedAt;
	}

	public void setSignedAt(String signedAt) {
		this.signedAt = signedAt;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

}
