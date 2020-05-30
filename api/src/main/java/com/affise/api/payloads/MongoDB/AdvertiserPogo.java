package com.affise.api.payloads.MongoDB;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class AdvertiserPogo{

	@BsonProperty("date")
	private Date date;

	@BsonProperty("note")
	private String note;

	@BsonProperty("country")
	private String country;

	@BsonProperty("vat_code")
	private String vatCode;

	@BsonId
	@BsonProperty("manager")
	private ObjectId manager;

	@BsonProperty("city")
	private String city;

	@BsonProperty("address_1")
	private String address1;

	@BsonProperty("hash_password")
	private String hashPassword;

	@BsonProperty("address_2")
	private String address2;

	@BsonProperty("allowed_ip")
	private List<String> allowedIp;

	@BsonProperty("title")
	private String title;

	@BsonProperty("zip_code")
	private Object zipCode;

	@BsonProperty("declined_partners")
	private List<String> declinedPartners;

	@BsonProperty("skype")
	private String skype;

	@BsonProperty("site_url")
	private String siteUrl;

	@BsonProperty("updated_at")
	private Date updatedAt;

	@BsonProperty("sub_accounts")
	private SubAccounts subAccounts;

	@BsonProperty("contact")
	private String contact;

	@BsonProperty("disallowed_ip")
	private List<String> disallowedIp;

	@BsonId
	@BsonProperty("_id")
	private ObjectId id;

	@BsonProperty("email")
	private String email;

	@BsonProperty("disabled_choice_postback_status")
	private boolean disabledChoicePostbackStatus;
}