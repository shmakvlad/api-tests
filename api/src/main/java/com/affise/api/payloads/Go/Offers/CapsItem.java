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
public class CapsItem{

	@JsonProperty("timeframe")
	private String timeframe;

	@JsonProperty("goal_type")
	private String goalType;

	@JsonProperty("is_remaining")
	private boolean isRemaining;

	@JsonProperty("reset_to_value")
	private int resetToValue;

	@JsonProperty("affiliates")
	private List<Integer> affiliates;

	@JsonProperty("id")
	private String id;

	@JsonProperty("countries")
	private List<String> countries;

	@JsonProperty("affiliate_type")
	private String affiliateType;

	@JsonProperty("country_type")
	private String countryType;

	@JsonProperty("type")
	private String type;

	@JsonProperty("value")
	private int value;

	@JsonProperty("goals")
	private List<String> goals;
}