package com.example.demo;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
public class Coupon {
	private final Campaign campaign;
	private Integer couponCode;
	private Integer used;

	public String toSQL() {
		return """
				INSERT INTO COUPON (COUPON_CODE, USED, CAMPAIGN_NAME) VALUES ('%s', %s, '%s');
				""".formatted(CampaignGeneratorSQL.valueParser.apply(couponCode), Math.round(used != null ? used : 0), CampaignGeneratorSQL.valueParser.apply(campaign.getVCampaignName()));
	}
}
