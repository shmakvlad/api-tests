package com.affise.api.payloads.Go.Affiliates;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.processing.Generated;
import java.util.List;


@Slf4j
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Generated("com.robohorse.robopojogenerator")
public class AffiliateBuilder {

    @JsonProperty("affiliate_manager_id")
    private String affiliateManagerId;

    @JsonProperty("notes")
    private Object notes;

    @JsonProperty("meta")
    private Object meta;

    @JsonProperty("contacts")
    private Object contacts;

    @JsonProperty("referral")
    private Object referral;

    @JsonProperty("password")
    private String password;

    @JsonProperty("contact_person")
    private String contactPerson;

    @JsonProperty("sub_account_2_except")
    private Object subAccount2Except;

    @JsonProperty("affiliate_id")
    private Object affiliateId;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("sub_account_1_except")
    private Object subAccount1Except;

    @JsonProperty("sub_account_2")
    private String subAccount2;

    @JsonProperty("tags")
    private List<String> tags;

    @JsonProperty("sub_account_1")
    private String subAccount1;

    @JsonProperty("updated_at")
    private String updatedAt;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("api_key")
    private String apiKey;

    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("status")
    private String status;

}
