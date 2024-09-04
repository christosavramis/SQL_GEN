package com.example.demo;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
public class Coupon {
	private final Campaign campaign;
	private String couponCode;
	private Double used;

	public String toSQL() {
		return """
				INSERT INTO COUPON (COUPON_CODE, USED, CAMPAIGN_NAME) VALUES ('%s', %s, '%s');
				""".formatted(couponCode, Math.round(used != null ? used : 0), campaign.getName());
	}

	public void clear() {
		couponCode = null;
		used = null;
	}

	public boolean isEmpty() {
		return StringUtils.isEmpty(couponCode) && used == null;
	}
}
