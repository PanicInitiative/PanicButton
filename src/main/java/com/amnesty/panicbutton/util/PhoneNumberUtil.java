package com.amnesty.panicbutton.util;

public class PhoneNumberUtil {
    private static int LIMIT = 2;

    public static String mask(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() < LIMIT) return phoneNumber;
        int length = phoneNumber.length();
        String prefix = phoneNumber.substring(0, length - LIMIT).replaceAll(".", "*");
        return prefix + phoneNumber.substring(length - LIMIT);
    }
}
