package com.affise.api.payloads;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.processing.Generated;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.affise.api.generatedata.Generations.generateEmail;
import static com.affise.api.generatedata.Generations.generatePassword;


@Slf4j
@Getter
@Setter
@Accessors(fluent = true)
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.robohorse.robopojogenerator")
public class AffiliateGoApi {

	@JsonProperty("affiliate_manager_id")
	private String affiliateManagerId;

	@JsonProperty("password")
	private String password;

	@JsonProperty("sub_account_2_except")
	private Object subAccount2Except;

	@JsonProperty("affiliate_id")
	private Object affiliateId;

	@JsonProperty("created_at")
	private String createdAt;

	@JsonProperty("sub_account_1_except")
	private Object subAccount1Except;

	@JsonProperty("sub_account_2")
	private String subAccount2;

	@JsonProperty("sub_account_1")
	private String subAccount1;

	@JsonProperty("updated_at")
	private String updatedAt;

	@JsonProperty("user_id")
	private String userId;

	@JsonProperty("api_key")
	private String apiKey;

	@JsonProperty("name")
	private String name;

	@JsonProperty("email")
	private String email;

	@JsonProperty("status")
	private String status;

	public static void main(String[] args) {
		AffiliateGoApi request = new AffiliateGoApi()
				.email(generateEmail())
				.password(generatePassword());

		AffiliateGoApi response = new AffiliateGoApi()
				.email(generateEmail())
				.password(generatePassword());

		showAllProps(request, response);
	}

	@SneakyThrows
	private static void showAllProps(Object input, Object output){
		ArrayList<Field> requestParams = new ArrayList<Field>();
		List<String> deleteKeys = Arrays.asList("password", "api_key", "log");

		for(Field field: input.getClass().getDeclaredFields()){
			boolean flag = true;
				for (String s : deleteKeys){
					if (field.getName().equals(s) || field.get(input) == null){
						flag = false;
						break;
					}
				}
			if (flag) requestParams.add(field);
		}

		boolean status = true;
		for (Field reqField : requestParams){
			Field resField = output.getClass().getDeclaredField(reqField.getName());
			if (resField.get(output) == null){
				log.error("[FAILED]  Missing field in response: {}", reqField.getName());
				status = false;
				break;
			}
			else if (reqField.get(input).equals(resField.get(output))) {
				log.info("[SUCCESS] Values are equal: " + reqField.getName() + " || Ac: " + resField.get(output) + " -||- Ex: " + reqField.get(input));
			}
			else {
				log.error("[FAILED]  Values are not equal: " + reqField.getName() + " || Ac: " + resField.get(output) + " -||- Ex: " + reqField.get(input));
				status = false;
				break;
			}
		}
		System.out.println(status);
	}

}