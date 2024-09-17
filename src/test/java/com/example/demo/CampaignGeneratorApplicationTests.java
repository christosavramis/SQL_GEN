package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CampaignGeneratorApplicationTests {

	@Test
	void contextLoads() {
		CampaignGeneratorSQL campaignGeneratorSQL = CampaignGeneratorSQL.sample();
		String sql = campaignGeneratorSQL.toSQL();
		String matchingSQL = """
				SET schema ANYTIME;
				INSERT INTO CAMPAIGN (NAME, COUPON_ID, LAYOUT_ID, PERCENTAGE, VOLUME, DESCRIPTION, DISCOUNT_TITLE, RECOMMENDATION_CODE)
				VALUES ('ATWORK_ΟΗΒ', 2232, 1, 5, -1, 'OHB-HELLAS (ΠΡΟΣΩΠΙΚΟ) - Έκπτωση 5%','OHB-HELLAS', '1550109');
				INSERT INTO COUPON (COUPON_CODE, USED, CAMPAIGN_NAME) VALUES ('1002232000000013', 0, 'ATWORK_ΟΗΒ');
				INSERT INTO PRODUCT_LINE_CAMPAIGN (PRODUCT_LINE_KEY, CAMPAIGN_NAME) (SELECT PL.STRATEGY_KEY, 'ATWORK_ΟΗΒ' FROM PRODUCT_LINE PL WHERE PL.STRATEGY_KEY IN ('MOTOR','BTM','SMART_DRIVE','PROPERTY'));
				""";
		assert sql.equals(matchingSQL);
	}

}
