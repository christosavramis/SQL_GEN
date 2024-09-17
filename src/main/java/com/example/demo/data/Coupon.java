package com.example.demo.data;

import com.example.demo.CampaignGeneratorSQL;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class Coupon {
	private final Campaign campaign;
	private Integer used = 0;

	public String toSQL() {
		String couponCode = assembleCouponCode(campaign.getCouponId().toString());
		return """
				INSERT INTO COUPON (COUPON_CODE, USED, CAMPAIGN_NAME) VALUES (%s, %s, %s);
				""".formatted(CampaignGeneratorSQL.valueParser.apply(couponCode), used != null ? used : 0, CampaignGeneratorSQL.valueParser.apply(campaign.getVCampaignName()));
	}

	private static int calculateLuhnCheckDigit(String input) {
		int sum = 0;
		boolean alternate = true;

		// Iterate from right to left, ignoring the last character
		for (int i = input.length() - 1; i >= 0; i--) {
			int n = Character.getNumericValue(input.charAt(i));

			// Every second digit (from the right) is doubled
			if (alternate) {
				n *= 2;
				// If doubling results in a number greater than 9, subtract 9
				if (n > 9) {
					n -= 9;
				}
			}

			sum += n;
			alternate = !alternate;
		}

		// Calculate the check digit that would make the sum a multiple of 10
		return (10 - (sum % 10)) % 10;
	}

	private static String assembleCouponCode(String campaignCouponId) {
		String beforeCheckDigit = "1" + StringUtils.leftPad(campaignCouponId, 6, "0") + "00000001";
		return beforeCheckDigit + calculateLuhnCheckDigit(beforeCheckDigit);
	}
}
