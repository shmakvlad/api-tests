package api.automation.kpi;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.mongojack.Id;

@Data
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Kpi{

	@JsonProperty("offers")
	private List<String> offers;

	@JsonProperty("goal2")
	private String goal2;

	@JsonInclude
	@JsonProperty("sub")
	private String sub;

	@JsonProperty("goal1")
	private String goal1;

	@JsonProperty("period")
	private String period;

	@JsonProperty("action_type")
	private String actionType;

	@JsonProperty("kpi")
	private Double kpi;

	@JsonInclude
	@JsonProperty("change_to")
	private String changeTo;

	@JsonInclude
	@JsonProperty("affiliates")
	private List<String> affiliates;

	@JsonProperty("id")
	private String id;

	@JsonProperty("_id")
	private ObjectId mongoId = new ObjectId();

	@JsonInclude
	@JsonProperty("notify_manager")
	private Object notifyManager;

	@JsonProperty("message")
	private String message;
}