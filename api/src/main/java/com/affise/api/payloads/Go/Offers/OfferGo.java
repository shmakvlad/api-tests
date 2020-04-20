package com.affise.api.payloads.Go.Offers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Generated;
import java.util.List;

@Slf4j
@Getter
@Setter
@Accessors(fluent = true)
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Generated("com.robohorse.robopojogenerator")
public class OfferGo{

	@JsonProperty("notes")
	private String notes;

	@JsonProperty("plugins")
	private List<Object> plugins;

	@JsonProperty("creatives")
	private List<Object> creatives;

	@JsonProperty("payouts")
	private List<PayoutsItem> payouts;

	@JsonProperty("incomplete")
	private boolean incomplete;

	@JsonProperty("logo")
	private String logo;

	@JsonProperty("epc")
	private int epc;

	@JsonProperty("id")
	private int id;

	@JsonProperty("redirect_type")
	private String redirectType;

	@JsonProperty("cpi")
	private boolean cpi;

	@JsonProperty("landings")
	private List<LandingsItem> landings;

	@JsonProperty("imp_destination_url")
	private String impDestinationUrl;

	@JsonProperty("allowed_ips")
	private List<String> allowedIps;

	@JsonProperty("cross_postback_url")
	private String crossPostbackUrl;

	@JsonProperty("kpi")
	private Kpi kpi;

	@JsonProperty("privacy_level")
	private String privacyLevel;

	@JsonProperty("traffic_sources")
	private List<Object> trafficSources;

	@JsonProperty("hold_period")
	private int holdPeriod;

	@JsonProperty("tags")
	private List<Object> tags;

	@JsonProperty("reject_not_unique_ip")
	private boolean rejectNotUniqueIp;

	@JsonProperty("release_date")
	private Object releaseDate;

	@JsonProperty("hide_payments")
	private boolean hidePayments;

	@JsonProperty("status")
	private String status;

	@JsonProperty("unique_ip")
	private boolean uniqueIp;

	@JsonProperty("traffic_back_url")
	private String trafficBackUrl;

	@JsonProperty("status_change_notification")
	private boolean statusChangeNotification;

	@JsonProperty("description")
	private Description description;

	@JsonProperty("created_at")
	private String createdAt;

	@JsonProperty("title")
	private String title;

	@JsonProperty("allow_impressions")
	private boolean allowImpressions;

	@JsonProperty("tracking_macro")
	private String trackingMacro;

	@JsonProperty("advertiser_id")
	private String advertiserId;

	@JsonProperty("caps")
	private Caps caps;

	@JsonProperty("commission_tiers")
	private CommissionTiers commissionTiers;

	@JsonProperty("top")
	private boolean top;

	@JsonProperty("updated_at")
	private String updatedAt;

	@JsonProperty("categories")
	private List<String> categories;

	@JsonProperty("tracking_url")
	private String trackingUrl;

	@JsonProperty("targetings")
	private List<Object> targetings;

	@JsonProperty("stop_date")
	private Object stopDate;

	@JsonProperty("external_offer_id")
	private String externalOfferId;

	@JsonProperty("change_postback_status")
	private boolean changePostbackStatus;

	@JsonProperty("secure_postback_code")
	private String securePostbackCode;

	@JsonProperty("affiliate_notification_interval")
	private String affiliateNotificationInterval;

	@JsonProperty("tracking_domain")
	private String trackingDomain;

	@JsonProperty("cr")
	private int cr;

	@JsonProperty("click_session_lifespan")
	private String clickSessionLifespan;

	@JsonProperty("min_click_session_lifespan")
	private String minClickSessionLifespan;

	@JsonProperty("io_document")
	private String ioDocument;

	@JsonProperty("allow_deep_links")
	private boolean allowDeepLinks;

	@JsonProperty("preview_url")
	private String previewUrl;

	@JsonProperty("smartilnk_ids")
	private List<Object> smartilnkIds;
}