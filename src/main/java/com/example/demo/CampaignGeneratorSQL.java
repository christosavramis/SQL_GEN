package com.example.demo;

import com.example.demo.data.Campaign;
import com.example.demo.data.Coupon;
import com.example.demo.data.ProductLineCampaign;
import com.example.demo.data.ProductLineKey;
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
				campaign.toSQL() + "\n" +
				coupon.toSQL() + "\n" +
				productLineCampaign.toSQL();
	}

	public String getFileName () {
		return "anygr-campaign-" + campaign.getVCampaignName() + "-enable.sql";
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
		campaign.setName("ΟΗΒ");
		campaign.setCouponId(2232);
		campaign.setPercentage(5);
		campaign.setDescription("OHB-HELLAS (ΠΡΟΣΩΠΙΚΟ) - Έκπτωση 5%");
		campaign.setDiscountTitle("OHB-HELLAS");
		campaign.setRecommendationCode("1550109");
		campaignGeneratorSQL.setCampaign(campaign);
		Coupon coupon = new Coupon(campaign);
		campaignGeneratorSQL.setCoupon(coupon);
		ProductLineCampaign productLineCampaign = new ProductLineCampaign(campaign);
		productLineCampaign.getProductLineKeys().addAll(List.of(ProductLineKey.MOTOR, ProductLineKey.BTM, ProductLineKey.SMART_DRIVE, ProductLineKey.PROPERTY));
		campaignGeneratorSQL.setProductLineCampaign(productLineCampaign);
		return campaignGeneratorSQL;
	}

}
