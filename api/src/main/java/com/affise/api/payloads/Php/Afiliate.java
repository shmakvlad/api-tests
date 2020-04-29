package com.affise.api.payloads.Php;

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
public class Afiliate {

    @JsonProperty("country")
    private Object country;

    @JsonProperty("notes")
    private String notes;

    @JsonProperty("manager")
    private Object manager;

    @JsonProperty("level")
    private int level;

    @JsonProperty("city")
    private Object city;

    @JsonProperty("customFields")
    private List<Object> customFields;

    @JsonProperty("address_1")
    private Object address1;

    @JsonProperty("address_2")
    private Object address2;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("login")
    private Object login;

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
    private Object balance;

    @JsonProperty("api_key")
    private String apiKey;

    @JsonProperty("phone")
    private Object phone;

    @JsonProperty("sub_accounts")
    private List<Object> subAccounts;

    @JsonProperty("contactPerson")
    private String contactPerson;

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

    @JsonProperty("password")
    private String password;

}