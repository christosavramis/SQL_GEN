package com.example.demo.data;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

@Data
@EqualsAndHashCode
public class Campaign {
    private static final String AT_WORK_PREFIX = "ATWORK_";
    private String name;
    private Integer couponId;
    private Integer layoutId = 1;

    private Integer percentage;
    private Integer volume = -1;
    private String description;
    private String discountTitle;
    private String recommendationCode;
    private String couponValidationPattern;
    private boolean atWork = true;

    public String getVCampaignName() {
        return atWork ? AT_WORK_PREFIX + name : name;
    }

    public String toSQL() {
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();
        addColumnIfNotNull(columns, values, "NAME", getVCampaignName());
        addColumnIfNotNull(columns, values, "COUPON_ID", couponId);
        addColumnIfNotNull(columns, values, "LAYOUT_ID", layoutId);
        addColumnIfNotNull(columns, values, "PERCENTAGE", percentage);
        addColumnIfNotNull(columns, values, "VOLUME", volume);
        addColumnIfNotNull(columns, values, "DESCRIPTION", description);
        addColumnIfNotNull(columns, values, "DISCOUNT_TITLE", discountTitle);
        addColumnIfNotNull(columns, values, "RECOMMENDATION_CODE", recommendationCode);
        addColumnIfNotNull(columns, values, "COUPON_VALIDATION_PATTERN", couponValidationPattern);
		trim(columns, values);
        return "INSERT INTO CAMPAIGN (%s) %n VALUES (%s);".formatted(columns, values);
    }
	private void trim(StringBuilder columns, StringBuilder values) {
		columns.delete(columns.length() - 2, columns.length());
		values.delete(values.length() - 2, values.length());
	}
    private void addColumnIfNotNull(StringBuilder columns, StringBuilder values, String columnName, Object value) {
        if (value != null) {
            columns.append(columnName).append(", ");
            if (value instanceof String) {
                values.append("'%s', ".formatted(value));
            } else {
                values.append("%s, ".formatted(value));
            }
        }
    }
}
