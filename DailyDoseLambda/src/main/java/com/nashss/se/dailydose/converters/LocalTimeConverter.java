package com.nashss.se.dailydose.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class LocalTimeConverter implements DynamoDBTypeConverter<String, LocalTime> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    @Override
    public String convert(LocalTime localTime) {
        if (localTime == null) {
            return null;
        }
        LocalTime truncatedTime = localTime.truncatedTo(ChronoUnit.SECONDS);
        return truncatedTime.format(FORMATTER);
    }

    @Override
    public LocalTime unconvert(String localTimeRepresentation) {
        return LocalTime.parse(localTimeRepresentation, FORMATTER);
    }

}
