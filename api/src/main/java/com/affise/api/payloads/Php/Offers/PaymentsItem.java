package com.affise.api.payloads.Php.Offers;

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
public class PaymentsItem{

	@JsonProperty("sub6")
	private Object sub6;

	@JsonProperty("sub7")
	private Object sub7;

	@JsonProperty("sub4")
	private Object sub4;

	@JsonProperty("goal")
	private String goal;

	@JsonProperty("sub5")
	private Object sub5;

	@JsonProperty("cities")
	private List<Object> cities;

	@JsonProperty("os")
	private List<Object> os;

	@JsonProperty("sub2")
	private Object sub2;

	@JsonProperty("devices")
	private List<Object> devices;

	@JsonProperty("sub3")
	private Object sub3;

	@JsonProperty("with_regions")
	private boolean withRegions;

	@JsonProperty("sub1")
	private Object sub1;

	@JsonProperty("country_exclude")
	private boolean countryExclude;

	@JsonProperty("countries")
	private List<Object> countries;

	@JsonProperty("title")
	private String title;

	@JsonProperty("type")
	private String type;

	@JsonProperty("url")
	private Object url;

	@JsonProperty("sub8")
	private Object sub8;

	@JsonProperty("total")
	private int total;

	@JsonProperty("revenue")
	private int revenue;

	@JsonProperty("currency")
	private String currency;

}