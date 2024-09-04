package com.example.demo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductLineCampaign {
	private final Campaign campaign;
	private List<ProductLineKey> productLineKeys = new ArrayList<>();

	public String toSQL() {
		return """
				INSERT INTO PRODUCT_LINE_CAMPAIGN (PRODUCT_LINE_KEY, CAMPAIGN_NAME) (SELECT PL.STRATEGY_KEY, '%s' FROM PRODUCT_LINE PL WHERE PL.STRATEGY_KEY IN (%s));
				""".formatted(campaign.getName(), productLineKeys);
	}

	public void clear() {
		productLineKeys.clear();
	}
}
