package com.affise.api.payloads.Go.Offers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Generated;

@Slf4j
@Getter
@Setter
@Accessors(fluent = true)
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Generated("com.robohorse.robopojogenerator")public class Description{

	@JsonProperty("ru")
	private String ru;

	@JsonProperty("ch")
	private String ch;

	@JsonProperty("en")
	private String en;
}