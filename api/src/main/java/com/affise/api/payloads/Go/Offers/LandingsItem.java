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
@Generated("com.robohorse.robopojogenerator")
public class LandingsItem{

	@JsonProperty("preview_url")
	private String previewUrl;

	@JsonProperty("id")
	private int id;

	@JsonProperty("title")
	private String title;

	@JsonProperty("type")
	private String type;

	@JsonProperty("tracking_url")
	private String trackingUrl;
}