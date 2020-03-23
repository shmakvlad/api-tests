package com.affise.api.payloads;

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
public class Advertiser{

	@JsonProperty("note")
	private Object note;

	@JsonProperty("country")
	private Object country;

	@JsonProperty("vat_code")
	private Object vatCode;

	@JsonProperty("manager")
	private String manager;

	@JsonProperty("city")
	private Object city;

	@JsonProperty("address_1")
	private Object address1;

	@JsonProperty("address_2")
	private Object address2;

	@JsonProperty("hash_password")
	private Object hashPassword;

	@JsonProperty("allowed_ip")
	private List<Object> allowedIp;

	@JsonProperty("title")
	private String title;

	@JsonProperty("url")
	private Object url;

	@JsonProperty("zip_code")
	private Object zipCode;

	@JsonProperty("skype")
	private Object skype;

	@JsonProperty("updated_at")
	private String updatedAt;

	@JsonProperty("sub_accounts")
	private Object subAccounts;

	@JsonProperty("contact")
	private Object contact;

	@JsonProperty("disallowed_ip")
	private List<Object> disallowedIp;

	@JsonProperty("manager_obj")
	private Object managerObj;

	@JsonProperty("id")
	private String id;

	@JsonProperty("email")
	private Object email;


}