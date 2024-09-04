package com.example.demo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data @EqualsAndHashCode
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
		StringBuilder sb = new StringBuilder();
		sb.append("SET schema ANYTIME;\n");
		if (!campaign.isEmpty()) {
			sb.append(campaign.toSQL());
		}

		if (!productLineCampaign.isEmpty()) {
			sb.append(productLineCampaign.toSQL()).append("\n");
		}

		if (!coupon.isEmpty()) {
			sb.append(coupon.toSQL()).append("\n");
		}

		return sb.toString();
	}
}
