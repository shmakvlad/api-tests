package com.affise.api.payloads.Affiliates;

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
public class CustomFieldsItem{

	@JsonProperty("name")
	private String name;

	@JsonProperty("label")
	private String label;

	@JsonProperty("id")
	private int id;

	@JsonProperty("value")
	private String value;

}