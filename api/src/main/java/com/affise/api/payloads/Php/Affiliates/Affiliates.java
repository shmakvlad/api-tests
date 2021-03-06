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
public class Affiliates{

	@JsonProperty("partner")
	private Partner partner;

	@JsonProperty("id")
	private int id;

	@JsonProperty("status")
	private int status;

	@Override
	public String toString() {
		return "Affiliates{" +
				"partner=" + partner +
				", id=" + id +
				", status=" + status +
				'}';
	}
}