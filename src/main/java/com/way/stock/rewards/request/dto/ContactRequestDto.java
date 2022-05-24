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
public class ContactRequestDto {

	@ApiModelProperty(required = false)
	@JsonProperty("email_address")
	private String emailAddress;

	@ApiModelProperty(required = true)
	@NotNull(message = "Phone Number Can't be null")
	@JsonProperty("phone_number")
	private String phoneNumber;

	@ApiModelProperty(required = true)
	@NotNull(message = "Street Address Can't be null")
	@JsonProperty("street_address")
	private List<String> streetAddress;

	@ApiModelProperty(required = true)
	@NotNull(message = "City Can't be null")
	@JsonProperty("city")
	private String city;

	@ApiModelProperty(required = true)
	@NotNull(message = "State Can't be null")
	@JsonProperty("state")
	private String state;

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public List<String> getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(List<String> streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
