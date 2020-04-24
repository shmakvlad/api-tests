package com.affise.api.payloads.Php.Affiliates;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.annotation.processing.Generated;

@Getter
@Setter
@Accessors(fluent = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.robohorse.robopojogenerator")
public class Balance{

	@JsonProperty("BTC")
	private Currency bTC;

	@JsonProperty("EUR")
	private Currency eUR;

	@JsonProperty("USD")
	private Currency uSD;

	@JsonProperty("RUB")
	private Currency rUB;

}