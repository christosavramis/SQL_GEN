package com.example.demo;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class Coupon {
	private final Campaign campaign;
	private String couponCode;
	private Integer used;

	public String toSQL() {
		return """
				INSERT INTO COUPON (COUPON_CODE, USED, CAMPAIGN_NAME) VALUES ('%s', %d, '%s');
				""".formatted(couponCode, used, campaign.getName());
	}

	public void clear() {
		couponCode = null;
		used = null;
	}
}
