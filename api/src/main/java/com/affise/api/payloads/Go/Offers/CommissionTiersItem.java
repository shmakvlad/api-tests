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
public class CommissionTiersItem{

	@JsonProperty("timeframe")
	private String timeframe;

	@JsonProperty("goal_type")
	private String goalType;

	@JsonProperty("modifier_value")
	private int modifierValue;

	@JsonProperty("modifier_type")
	private String modifierType;

	@JsonProperty("affiliates")
	private List<Object> affiliates;

	@JsonProperty("id")
	private String id;

	@JsonProperty("target_goals")
	private List<Object> targetGoals;

	@JsonProperty("affiliate_type")
	private String affiliateType;

	@JsonProperty("type")
	private String type;

	@JsonProperty("value")
	private int value;

	@JsonProperty("goals")
	private List<Object> goals;
}