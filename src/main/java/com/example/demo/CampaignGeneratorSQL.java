package com.example.demo;

import lombok.Data;

@Data
public class CampaignGeneratorSQL {
	private String text;
	private Campaign campaign = new Campaign();
	private ProductLineCampaign productLineCampaign = new ProductLineCampaign(campaign);
	private Coupon coupon = new Coupon(campaign);
	public void clear() {
		campaign.clear();
		productLineCampaign.clear();
		coupon.clear();
	}

	public String toSQL() {
		return """
				SET schema ANYTIME;
				%s
				%s
				%s
				""".formatted(campaign.toSQL(), productLineCampaign.toSQL(), coupon.toSQL());
	}
}
