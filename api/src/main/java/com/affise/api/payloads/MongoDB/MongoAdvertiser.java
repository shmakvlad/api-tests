package com.affise.api.payloads.MongoDB;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;

import java.util.Date;

import static com.affise.api.config.Config.getConfig;

@Data
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.ALWAYS)         // Write null values for default
@JsonInclude(JsonInclude.Include.NON_DEFAULT)      // Only include values
public class MongoAdvertiser {

	@JsonInclude(JsonInclude.Include.ALWAYS)
	@JsonProperty("sub_account_2_except")
	private boolean subAccount2Except;

	@JsonInclude
	@JsonProperty("country")
	private String country = "";

	@JsonInclude
	@JsonProperty("note")
	private String note = "";

	@JsonInclude
	@JsonProperty("vat_code")
	private String vatCode = "";

	@JsonInclude
	@JsonProperty("address2")
	private String address2 = "";

	@JsonInclude
	@JsonProperty("city")
	private String city = "";

	@JsonInclude
	@JsonProperty("address1")
	private String address1 = "";

	@JsonInclude
	@JsonProperty("created_at")
	private Date createdAt = new Date();

	@JsonInclude
	@JsonProperty("secure_code")
	private String secureCode= "";

	@JsonInclude
	@JsonProperty("title")
	private String title = "";

	@JsonInclude
	@JsonProperty("client_id")
	private Long clientId = Long.parseLong(getConfig().clientId());

	@JsonInclude
	@JsonProperty("sub_account_1_except")
	private boolean subAccount1Except;

	@JsonInclude
	@JsonProperty("url")
	private String url = "";

	@JsonInclude
	@JsonProperty("zip_code")
	private String zipCode = "";

	@JsonInclude
	@JsonProperty("skype")
	private String skype = "";

	@JsonInclude
	@JsonProperty("sub_account_2")
	private String subAccount2 = "";

	@JsonInclude
	@JsonProperty("sub_account_1")
	private String subAccount1 = "";

	@JsonInclude
	@JsonProperty("updated_at")
	private Date updatedAt = new Date();

	@JsonInclude
	@JsonProperty("user_id")
	private ObjectId userId = new ObjectId("000000000000000000000000");

	@JsonProperty("manager_id")
	private ObjectId managerId;

	@JsonInclude
	@JsonProperty("contact")
	private String contact = "";

	@JsonProperty("_id")
	private ObjectId id;

	@JsonInclude
	@JsonProperty("email")
	private String email = "";
}