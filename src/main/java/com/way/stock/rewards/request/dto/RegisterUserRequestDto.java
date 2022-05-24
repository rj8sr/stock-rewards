package com.way.stock.rewards.request.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class RegisterUserRequestDto {

	@ApiModelProperty(required = true)
	@NotNull(message = "Contact Information can't be null")
	private ContactRequestDto contact;

	@ApiModelProperty(required = true)
	@NotNull(message = "Identity Information can't be null")
	private IdentityRequestDto identity;

	@ApiModelProperty(required = true)
	@NotNull(message = "Disclosures Information cant be null")
	private DisclosuresRequestDto disclosures;

	@ApiModelProperty(required = true)
	@NotNull(message = "Agreement Information can't be null")
	private List<AgreementRequestDto> agreements;

	public ContactRequestDto getContact() {
		return contact;
	}

	public void setContact(ContactRequestDto contact) {
		this.contact = contact;
	}

	public IdentityRequestDto getIdentity() {
		return identity;
	}

	public void setIdentity(IdentityRequestDto identity) {
		this.identity = identity;
	}

	public DisclosuresRequestDto getDisclosures() {
		return disclosures;
	}

	public void setDisclosures(DisclosuresRequestDto disclosures) {
		this.disclosures = disclosures;
	}

	public List<AgreementRequestDto> getAgreements() {
		return agreements;
	}

	public void setAgreements(List<AgreementRequestDto> agreements) {
		this.agreements = agreements;
	}

}
