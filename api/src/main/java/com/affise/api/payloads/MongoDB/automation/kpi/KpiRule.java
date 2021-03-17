package api.automation.kpi;

import api.shared.Pagination;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(fluent = true)
@Data
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class KpiRule {

	@JsonProperty("message")
	private Kpi kpi;

	@JsonProperty("status")
	private int status;

	@JsonProperty("error")
	private String error;

	@JsonProperty("items")
	private List<Kpi> kpiList;

	@JsonProperty("pagination")
	private Pagination pagination;

}