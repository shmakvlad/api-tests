package com.affise.api.payloads.MongoDB;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.bson.codecs.pojo.annotations.BsonProperty;

import javax.annotation.processing.Generated;

@Data
//@Accessors(chain = true) -||- enable for AdvertiserPogo
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.ALWAYS)
@Generated("com.robohorse.robopojogenerator")
public class SubAccount{

	@JsonProperty("except")
	@BsonProperty("except")
	private boolean except;

	@JsonProperty("value")
	@BsonProperty("value")
	private String value;

}