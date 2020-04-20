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
public class PayoutsItem{

	@JsonProperty("sub6")
	private List<String> sub6;

	@JsonProperty("sub7")
	private List<String> sub7;

	@JsonProperty("include_regions")
	private boolean includeRegions;

	@JsonProperty("sub4")
	private List<String> sub4;

	@JsonProperty("sub5")
	private List<String> sub5;

	@JsonProperty("cities")
	private List<Integer> cities;

	@JsonProperty("sub2")
	private List<String> sub2;

	@JsonProperty("devices")
	private List<String> devices;

	@JsonProperty("sub3")
	private List<String> sub3;

	@JsonProperty("oses")
	private List<String> oses;

	@JsonProperty("sub1")
	private List<String> sub1;

	@JsonProperty("countries")
	private List<String> countries;

	@JsonProperty("sub8")
	private List<String> sub8;

	@JsonProperty("goal_title")
	private String goalTitle;

	@JsonProperty("payouts")
	private int payouts;

	@JsonProperty("goal_value")
	private String goalValue;

	@JsonProperty("payment_type")
	private String paymentType;

	@JsonProperty("total")
	private int total;

	@JsonProperty("affiliates")
	private List<Integer> affiliates;

	@JsonProperty("currency")
	private String currency;

	@JsonProperty("exclude_countries")
	private boolean excludeCountries;
}