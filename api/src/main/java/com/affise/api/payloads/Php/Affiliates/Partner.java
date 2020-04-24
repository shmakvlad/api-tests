package com.affise.api.payloads.Php.Affiliates;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.annotation.processing.Generated;
import java.util.List;

@Getter
@Setter
@Accessors(fluent = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.robohorse.robopojogenerator")
public class Partner{

	@JsonProperty("country")
	private Object country;

	@JsonProperty("notes")
	private String notes;

	@JsonProperty("manager")
	private Object manager;

	@JsonProperty("contactPerson")
	private String contactPerson;

	@JsonProperty("level")
	private int level;

	@JsonProperty("city")
	private Object city;

	@JsonProperty("customFields")
	private List<CustomFieldsItem> customFields;

	@JsonProperty("address_1")
	private Object address1;

	@JsonProperty("address_2")
	private Object address2;

	@JsonProperty("created_at")
	private String createdAt;

	@JsonProperty("login")
	private String login;

	@JsonProperty("payment_systems")
	private List<Object> paymentSystems;

	@JsonProperty("zip_code")
	private Object zipCode;

	@JsonProperty("ref")
	private Object ref;

	@JsonProperty("updated_at")
	private String updatedAt;

	@JsonProperty("ref_percent")
	private Object refPercent;

	@JsonProperty("balance")
	private Balance balance;

	@JsonProperty("api_key")
	private String apiKey;

	@JsonProperty("phone")
	private Object phone;

	@JsonProperty("sub_accounts")
	private List<Object> subAccounts;

	@JsonProperty("offersCount")
	private int offersCount;

	@JsonProperty("name")
	private Object name;

	@JsonProperty("id")
	private int id;

	@JsonProperty("email")
	private String email;

	@JsonProperty("status")
	private String status;

	@Override
	public String toString() {
		return "Partner{" +
				"country=" + country +
				", notes='" + notes + '\'' +
				", manager=" + manager +
				", level=" + level +
				", city=" + city +
				", customFields=" + customFields +
				", address1=" + address1 +
				", address2=" + address2 +
				", createdAt='" + createdAt + '\'' +
				", login='" + login + '\'' +
				", paymentSystems=" + paymentSystems +
				", zipCode=" + zipCode +
				", ref=" + ref +
				", updatedAt='" + updatedAt + '\'' +
				", refPercent=" + refPercent +
				", balance=" + balance +
				", apiKey='" + apiKey + '\'' +
				", phone=" + phone +
				", subAccounts=" + subAccounts +
				", offersCount=" + offersCount +
				", name=" + name +
				", id=" + id +
				", email='" + email + '\'' +
				", status='" + status + '\'' +
				'}';
	}
}