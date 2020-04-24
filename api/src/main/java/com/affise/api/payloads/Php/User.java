package com.affise.api.payloads.Php;

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
public class User{

	@JsonProperty("skype")
	private Object skype;

	@JsonProperty("updated_at")
	private String updatedAt;

	@JsonProperty("api_key")
	private String apiKey;

	@JsonProperty("roles")
	private List<String> roles;

	@JsonProperty("last_name")
	private String lastName;

	@JsonProperty("created_at")
	private String createdAt;

	@JsonProperty("id")
	private String id;

	@JsonProperty("work_hours")
	private Object workHours;

	@JsonProperty("first_name")
	private String firstName;

	@JsonProperty("email")
	private String email;

}