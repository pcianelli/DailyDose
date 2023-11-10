package com.nashss.se.dailydose.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class IdUtils {
    private static final Pattern INVALID_CHARACTER_PATTERN = Pattern.compile("[\"'\\\\]");
    static final int MAX_ID_LENGTH = 8;

    private IdUtils() {
    }

    public static boolean isValidString(String stringToValidate) {
        if (StringUtils.isBlank(stringToValidate)) {
            return false;
        } else {
            return !INVALID_CHARACTER_PATTERN.matcher(stringToValidate).find();
        }
    }

    public static String generateNotificationId() {
        return RandomStringUtils.randomAlphanumeric(MAX_ID_LENGTH);
    }

}
