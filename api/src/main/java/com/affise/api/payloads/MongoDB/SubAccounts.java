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
@Accessors(fluent = true)
//@Accessors(chain = true) -||- enable for AdvertiserPogo
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.ALWAYS)
@Generated("com.robohorse.robopojogenerator")
public class SubAccounts{

	@JsonProperty("1")
	@BsonProperty("1")
	private SubAccount sub1;

	@JsonProperty("2")
	@BsonProperty("2")
	private SubAccount sub2;

}