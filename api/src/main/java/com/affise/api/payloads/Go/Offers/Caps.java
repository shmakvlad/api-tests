package com.affise.api.payloads.Go.Offers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Generated;
import java.util.List;

@Slf4j
@Getter
@Setter
@Accessors(fluent = true)
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Generated("com.robohorse.robopojogenerator")
public class Caps{

	@JsonProperty("default_goal_overcap")
	private String defaultGoalOvercap;

	@JsonProperty("timezone")
	private String timezone;

	@JsonProperty("is_overcap")
	private boolean isOvercap;

	@JsonProperty("currency")
	private String currency;

	@JsonProperty("conversion_status")
	private List<String> conversionStatus;

	@JsonProperty("is_partner_hide")
	private boolean isPartnerHide;

	@JsonProperty("caps")
	private List<CapsItem> caps;
}