package com.swang.smartart.core.util;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * Created by swang on 5/13/2015.
 */
public class CommonUtil {

    public static String generateRandomNumber(int length) {
        return RandomStringUtils.random(length, false, true);
    }

    public String generateRandomString(int length) {
        return RandomStringUtils.random(length, true, false);
    }

    public String generateRandomAlphanumeric(int length) {
        return RandomStringUtils.random(length, true, true);
    }
}
