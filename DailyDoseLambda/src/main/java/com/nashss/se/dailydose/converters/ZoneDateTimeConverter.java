package com.nashss.se.dailydose.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZoneDateTimeConverter implements DynamoDBTypeConverter<String, ZonedDateTime>{
    @Override
    public String convert(ZonedDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ISO_INSTANT);
    }

    @Override
    public ZonedDateTime unconvert(String dateTimeRepresentation) {
        return ZonedDateTime.parse(dateTimeRepresentation);
    }

}
