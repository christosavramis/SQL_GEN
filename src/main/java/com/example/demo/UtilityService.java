package com.example.demo;

import org.apache.commons.lang3.StringUtils;

public class UtilityService {
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

    public static String assembleCouponCode(String campaignCouponId) {
        String beforeCheckDigit = "1" + StringUtils.leftPad(campaignCouponId, 6, "0") + "00000001";
        return beforeCheckDigit + calculateLuhnCheckDigit(beforeCheckDigit);
    }

    public static void main(String[] args) {
        UtilityService utilityService = new UtilityService();
        System.out.println(assembleCouponCode("2232"));
    }
}
