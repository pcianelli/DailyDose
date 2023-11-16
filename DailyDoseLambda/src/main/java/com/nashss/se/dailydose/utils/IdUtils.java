package com.nashss.se.dailydose.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * Various helpful utilities for generating an customerId.
 */
public class IdUtils {
    private static final Pattern INVALID_CHARACTER_PATTERN = Pattern.compile("[\"'\\\\]");
    private static final int MAX_ID_LENGTH = 8;

    /**
     * Instantiates a new IdUtils object.
     */
    private IdUtils() {
    }

    /**
     * If the parameter does not have any invalid characters.
     * @param stringToValidate The string that is valid or not.
     * @return A boolean true or false.
     */
    public static boolean isValidString(String stringToValidate) {
        if (StringUtils.isBlank(stringToValidate)) {
            return false;
        } else {
            return !INVALID_CHARACTER_PATTERN.matcher(stringToValidate).find();
        }
    }

    /**
     * Generates a NotificationId.
     * @return A String of random Alphanumeric characters with max length of 8.
     */
    public static String generateNotificationId() {
        return RandomStringUtils.randomAlphanumeric(MAX_ID_LENGTH);
    }

}
