package com.example.demo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.function.Function;

@Data @EqualsAndHashCode
public class CampaignGeneratorSQL {
	private Campaign campaign = new Campaign();
	private ProductLineCampaign productLineCampaign = new ProductLineCampaign(campaign);
	private Coupon coupon = new Coupon(campaign);

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

	public String getFileName () {
		return campaign.getVCampaignName() + ".sql";
	}

	public static final Function<Object, String> valueParser = value -> {
		if (value instanceof String checkedValue) {
			return "'%s'".formatted(checkedValue);
		} else if (value instanceof Double checkedValue) {
			return "%s".formatted(Math.round(checkedValue));
		} else {
			return value.toString();
		}
	};

}
