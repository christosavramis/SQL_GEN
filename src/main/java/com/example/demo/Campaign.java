package com.example.demo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Data @EqualsAndHashCode
public class Campaign {
	private String name;
	private String couponId;
	private String layoutId;
	private String percentage;
	private String volume;
	private String description;
	private String discountTitle;
	private String recommendationCode;
	private String couponValidationPattern;

	public void clear() {
		name = null;
		couponId = null;
		layoutId = null;
		percentage = null;
		volume = null;
		description = null;
		discountTitle = null;
		couponValidationPattern = null;
		recommendationCode = null;
	}
	public String toSQL() {
		Map<String, Object> map = new HashMap<>();
		if (!StringUtils.isEmpty(name)) {
			map.put("name", name);
		}
		if (!StringUtils.isEmpty(couponId)) {
			map.put("couponId", couponId);
		}
		if (!StringUtils.isEmpty(layoutId)) {
			map.put("layoutId", layoutId);
		}
		if (!StringUtils.isEmpty(percentage)) {
			map.put("percentage", percentage);
		}
		if (!StringUtils.isEmpty(volume)) {
			map.put("volume", volume);
		}
		if (!StringUtils.isEmpty(description)) {
			map.put("description", description);
		}
		if (!StringUtils.isEmpty(discountTitle)) {
			map.put("discountTitle", discountTitle);
		}
		if (!StringUtils.isEmpty(couponValidationPattern)) {
			map.put("couponValidationPattern", couponValidationPattern);
		}
		if (!StringUtils.isEmpty(recommendationCode)) {
			map.put("recommendationCode", recommendationCode);
		}

		return "INSERT INTO CAMPAIGN (%s) VALUES (%s);".formatted(String.join(", ", map.keySet()), String.join(", ", map.values().stream().map(Object::toString).toArray(String[]::new)));
	}
}
