package com.nashss.se.dailydose.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Formatter;

public class LocalTimeConverter implements DynamoDBTypeConverter<String, LocalTime> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    @Override
    public String convert(LocalTime localTime) {
        if (localTime == null) {
            return null;
        }
        return localTime.format(FORMATTER);
    }

    @Override
    public LocalTime unconvert(String localTimeRepresentation) {
        return LocalTime.parse(localTimeRepresentation, FORMATTER);
    }

}
