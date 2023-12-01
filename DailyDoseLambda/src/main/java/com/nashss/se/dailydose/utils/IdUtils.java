package com.nashss.se.dailydose.utils;

import com.nashss.se.dailydose.exceptions.InvalidAttributeValueException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * Various helpful utilities for generating an customerId.
 */
public class IdUtils {
    private static final Pattern INVALID_NAME_CHARACTER_PATTERN = Pattern.compile("[a-zA-Z0-9 ]*");
    private static final int MAX_ID_LENGTH = 8;

    /**
     * Validates a medication name.
     *
     * @param medName The medication name to validate.
     * @throws InvalidAttributeValueException if the medication name is invalid.
     */
    public static void validateMedicationName(String medName) throws InvalidAttributeValueException {
        if (medName == null || !INVALID_NAME_CHARACTER_PATTERN.matcher(medName).matches()) {
            throw new InvalidAttributeValueException("Invalid characters in the medication name or name is null.");
        }
    }

    /**
     * Validates a medication name.
     *
     * @param medName The medication name to validate.
     * @throws IllegalArgumentException if the medication name is blank.
     */
    public static void validMedNameNotBlank(String medName) throws IllegalArgumentException{
        if (medName.equals("")) {
            throw new IllegalArgumentException("Invalid characters in the medication name or name is blank");
        }
    }

    /**
     * Validates a time.
     *
     * @param time The time to validate.
     * @throws IllegalArgumentException if the time is null or blank.
     */
    public static void validTime(String time) {
        if(time == null || StringUtils.isBlank(time)) {
            throw new IllegalArgumentException("Time cannot be null or blank");
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
