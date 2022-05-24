package com.way.stock.rewards.request.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IdentityRequestDto {

	@ApiModelProperty(required = true)
	@JsonProperty("given_name")
	@NotNull(message = "Given Name Can't be null")
	private String givenName;

	@ApiModelProperty(required = true)
	@JsonProperty("family_name")
	@NotNull(message = "Family Name Can't be null")
	private String familyName;

	@ApiModelProperty(required = true)
	@JsonProperty("date_of_birth")
	@NotNull(message = "Date of Birth Can't be null")
	private String dateOfBirth;

	@ApiModelProperty(required = true)
	@JsonProperty("country_of_tax_residence")
	@NotNull(message = "Country Of TaxResidence Can't be null")
	private String countryOfTaxResidence;

	@ApiModelProperty(required = true)
	@JsonProperty("funding_source")
	@NotNull(message = "Funding Source Can't be null")
	private List<String> fundingSource;

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getCountryOfTaxResidence() {
		return countryOfTaxResidence;
	}

	public void setCountryOfTaxResidence(String countryOfTaxResidence) {
		this.countryOfTaxResidence = countryOfTaxResidence;
	}

	public List<String> getFundingSource() {
		return fundingSource;
	}

	public void setFundingSource(List<String> fundingSource) {
		this.fundingSource = fundingSource;
	}

}
