package com.example.demo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

@Data @EqualsAndHashCode
public class Campaign {
	private String name;
	private String couponId;
	private Double layoutId;
	private String percentage;
	private String volume;
	private String description;
	private String discountTitle;
	private String recommendationCode;
	private String couponValidationPattern;
	private boolean atWork = true;

	public String getVCampaignName() {
		return (atWork ? "AT_WORK_" + name.trim().toUpperCase() : name.trim().toUpperCase()).replaceAll("[^a-zA-Z0-9]", "_");
	}
	public String toSQL() {
		Map<String, Object> map = new HashMap<>();
		if (!StringUtils.isEmpty(name)) {
			map.put("NAME", getVCampaignName());
		}
		if (!StringUtils.isEmpty(couponId)) {
			map.put("COUPON_ID", couponId);
		}
		if (layoutId != null) {
			map.put("LAYOUT_ID", layoutId);
		}
		if (!StringUtils.isEmpty(percentage)) {
			map.put("PERCENTAGE", percentage);
		}
		if (!StringUtils.isEmpty(volume)) {
			map.put("VOLUME", volume);
		}
		if (!StringUtils.isEmpty(description)) {
			map.put("DESCRIPTION", description);
		}
		if (!StringUtils.isEmpty(discountTitle)) {
			map.put("DISCOUNT_TITLE", discountTitle);
		}
		if (!StringUtils.isEmpty(couponValidationPattern)) {
			map.put("COUPON_VALIDATION_PATTERN", couponValidationPattern);
		}
		if (!StringUtils.isEmpty(recommendationCode)) {
			map.put("RECOMMENDATION_CODE", recommendationCode);
		}

		return "INSERT INTO CAMPAIGN (%s) VALUES (%s);".formatted(String.join(", ", map.keySet()), String.join(", ", map.values().stream().map(CampaignGeneratorSQL.valueParser).map(Object::toString).toArray(String[]::new)));
	}

	public boolean isEmpty() {
		boolean emptyStrings = Stream.of(name, couponId, percentage, volume, description, discountTitle, recommendationCode, couponValidationPattern).allMatch(StringUtils::isEmpty);
		boolean emptyNumbers = Stream.of(layoutId).allMatch(Objects::isNull);
		return emptyNumbers && emptyStrings;
	}
}
