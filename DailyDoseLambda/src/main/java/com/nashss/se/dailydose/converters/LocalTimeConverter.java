package com.nashss.se.dailydose.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeConverter implements DynamoDBTypeConverter<String, LocalTime> {
    @Override
    public String convert(LocalTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ISO_INSTANT);
    }

    @Override
    public LocalTime unconvert(String localTimeRepresentation) {
        return LocalTime.parse(localTimeRepresentation);
    }

}
