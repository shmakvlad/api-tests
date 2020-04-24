package com.affise.api.payloads.Php.Offers;

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
public class Kpi{

	@JsonProperty("ru")
	private String ru;

	@JsonProperty("vi")
	private String vi;

	@JsonProperty("pt")
	private String pt;

	@JsonProperty("ka")
	private String ka;

	@JsonProperty("en")
	private String en;

	@JsonProperty("my")
	private String my;

	@JsonProperty("es")
	private String es;

}