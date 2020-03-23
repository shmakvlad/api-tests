package com.affise.api.payloads.Offers;

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
public class Offer{

	@JsonProperty("uniqIpOnly")
	private int uniqIpOnly;

	@JsonProperty("notes")
	private Object notes;

	@JsonProperty("rejectNotUniqIp")
	private int rejectNotUniqIp;

	@JsonProperty("strictly_connection_type")
	private Object strictlyConnectionType;

	@JsonProperty("creatives")
	private List<Object> creatives;

	@JsonProperty("restriction_isp")
	private Object restrictionIsp;

	@JsonProperty("caps_status")
	private List<String> capsStatus;

	@JsonProperty("is_redirect_overcap")
	private boolean isRedirectOvercap;

	@JsonProperty("strictly_devices")
	private Object strictlyDevices;

	@JsonProperty("minimal_click_session")
	private String minimalClickSession;

	@JsonProperty("domain_url")
	private String domainUrl;

	@JsonProperty("click_session")
	private String clickSession;

	@JsonProperty("auto_offer_connect")
	private Object autoOfferConnect;

	@JsonProperty("logo")
	private String logo;

	@JsonProperty("epc")
	private int epc;

	@JsonProperty("id")
	private int id;

	@JsonProperty("redirect_type")
	private Object redirectType;

	@JsonProperty("landings")
	private List<Object> landings;

	@JsonProperty("cross_postback_url")
	private Object crossPostbackUrl;

	@JsonProperty("kpi")
	private Kpi kpi;

	@JsonProperty("sub_restrictions")
	private Object subRestrictions;

	@JsonProperty("hash_password")
	private Object hashPassword;

	@JsonProperty("hold_period")
	private int holdPeriod;

	@JsonProperty("is_cpi")
	private boolean isCpi;

	@JsonProperty("creatives_zip")
	private Object creativesZip;

	@JsonProperty("strictly_country")
	private int strictlyCountry;

	@JsonProperty("tags")
	private List<Object> tags;

	@JsonProperty("macro_url")
	private String macroUrl;

	@JsonProperty("disallowed_ip")
	private String disallowedIp;

	@JsonProperty("hide_payments")
	private boolean hidePayments;

	@JsonProperty("caps_timezone")
	private Object capsTimezone;

	@JsonProperty("status")
	private String status;

	@JsonProperty("stop_at")
	private Object stopAt;

	@JsonProperty("sources")
	private List<Object> sources;

	@JsonProperty("payments")
	private List<PaymentsItem> payments;

	@JsonProperty("url_preview")
	private Object urlPreview;

	@JsonProperty("privacy")
	private String privacy;

	@JsonProperty("created_at")
	private String createdAt;

	@JsonProperty("allowed_ip")
	private String allowedIp;

	@JsonProperty("disabled_by")
	private Object disabledBy;

	@JsonProperty("title")
	private String title;

	@JsonProperty("start_at")
	private Object startAt;

	@JsonProperty("allow_impressions")
	private boolean allowImpressions;

	@JsonProperty("caps")
	private List<Object> caps;

	@JsonProperty("search_empty_sub")
	private Object searchEmptySub;

	@JsonProperty("strictly_os")
	private List<Object> strictlyOs;

	@JsonProperty("commission_tiers")
	private List<Object> commissionTiers;

	@JsonProperty("logo_source")
	private Object logoSource;

	@JsonProperty("targeting")
	private List<Object> targeting;

	@JsonProperty("hide_referer")
	private int hideReferer;

	@JsonProperty("required_approval")
	private boolean requiredApproval;

	@JsonProperty("updated_at")
	private String updatedAt;

	@JsonProperty("trafficback_url")
	private Object trafficbackUrl;

	@JsonProperty("use_http")
	private boolean useHttp;

	@JsonProperty("categories")
	private List<Object> categories;

	@JsonProperty("advertiser")
	private String advertiser;

	@JsonProperty("strictly_isp")
	private List<Object> strictlyIsp;

	@JsonProperty("enabled_commission_tiers")
	private boolean enabledCommissionTiers;

	@JsonProperty("hide_caps")
	private Object hideCaps;

	@JsonProperty("smartlink_categories")
	private List<Object> smartlinkCategories;

	@JsonProperty("description_lang")
	private Object descriptionLang;

	@JsonProperty("allow_deeplink")
	private int allowDeeplink;

	@JsonProperty("external_offer_id")
	private Object externalOfferId;

	@JsonProperty("full_categories")
	private List<Object> fullCategories;

	@JsonProperty("offer_id")
	private String offerId;

	@JsonProperty("url")
	private String url;

	@JsonProperty("is_top")
	private int isTop;

	@JsonProperty("partner_payments")
	private List<Object> partnerPayments;

	@JsonProperty("cr")
	private int cr;

	@JsonProperty("io_document")
	private Object ioDocument;

	@JsonProperty("preview_url")
	private Object previewUrl;

	@JsonProperty("sub_accounts")
	private Object subAccounts;

	@JsonProperty("notice_percent_overcap")
	private Object noticePercentOvercap;

	@JsonProperty("use_https")
	private boolean useHttps;

	@JsonProperty("disabled_choice_postback_status")
	private boolean disabledChoicePostbackStatus;

	@JsonProperty("caps_goal_overcap")
	private String capsGoalOvercap;

	@JsonProperty("strictly_brands")
	private Object strictlyBrands;

}