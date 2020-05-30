package com.affise.api.payloads.MongoDB;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

import javax.annotation.processing.Generated;
import java.util.Date;
import java.util.List;

@Data
@Slf4j
@Getter
@Setter
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.ALWAYS)         // Write null values for default
//@JsonInclude(JsonInclude.Include.NON_DEFAULT)  // Only include values
@Generated("com.robohorse.robopojogenerator")
public class AdvertiserMongoDB {

	@JsonProperty("date")
	private Date date;

	@JsonProperty("note")
	private Object note;

	@JsonProperty("country")
	private Object country;

	@JsonProperty("vat_code")
	private Object vatCode;

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

	@JsonProperty("disallowed_ip")
	private List<Object> disallowedIp;

	@JsonProperty("title")
	private Object title;

	@JsonProperty("zip_code")
	private Object zipCode;

	@JsonProperty("skype")
	private Object skype;

	@JsonProperty("updated_at")
	private Date updatedAt;

	@JsonProperty("site_url")
	private Object siteUrl;

	@JsonProperty("sub_accounts")
	private SubAccounts subAccounts;

	@JsonProperty("contact")
	private Object contact;

	@JsonProperty("manager")
	private ObjectId manager;

	@JsonProperty("_id")
	private ObjectId id;

	@JsonProperty("email")
	private Object email;

	@JsonProperty("disabled_choice_postback_status")
	private boolean disabledChoicePostbackStatus;
}