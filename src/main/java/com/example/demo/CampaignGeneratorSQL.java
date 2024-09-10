package com.example.demo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.function.Function;

@Data @EqualsAndHashCode
public class CampaignGeneratorSQL {
	private Campaign campaign = new Campaign();
	private ProductLineCampaign productLineCampaign = new ProductLineCampaign(campaign);
	private Coupon coupon = new Coupon(campaign);

	public String toSQL() {
		return "SET schema ANYTIME;\n" +
				campaign.toSQL() +
				productLineCampaign.toSQL() + "\n" +
				coupon.toSQL() + "\n";
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

	public static CampaignGeneratorSQL sample() {
		CampaignGeneratorSQL campaignGeneratorSQL = new CampaignGeneratorSQL();
		Campaign campaign = new Campaign();
		campaign.setName("Campaign Name");
		campaign.setCouponId(1);
		campaign.setLayoutId(1);
		campaign.setPercentage(10);
		campaign.setVolume(100);
		campaign.setDescription("Description");
		campaign.setDiscountTitle("Discount Title");
		campaign.setRecommendationCode("Recommendation Code");
		campaignGeneratorSQL.setCampaign(campaign);
		Coupon coupon = new Coupon(campaign);
		coupon.setCouponCode(123456);
		coupon.setUsed(0);
		campaignGeneratorSQL.setCoupon(coupon);
		ProductLineCampaign productLineCampaign = new ProductLineCampaign(campaign);
		productLineCampaign.getProductLineKeys().addAll(List.of(ProductLineKey.MOTOR, ProductLineKey.BTM, ProductLineKey.SMART_DRIVE));
		campaignGeneratorSQL.setProductLineCampaign(productLineCampaign);
		return campaignGeneratorSQL;
	}

}
