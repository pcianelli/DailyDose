package com.nashss.se.dailydose.dynamodb.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.nashss.se.dailydose.converters.LocalTimeConverter;

import java.time.LocalTime;
import java.util.Objects;

@DynamoDBTable(tableName = "notifications")
public class Notification {
    private String customerId;
    private String medName;
    private LocalTime time;


    @DynamoDBHashKey(attributeName = "customerId")
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @DynamoDBRangeKey(attributeName = "medName")
    public String getMedName() {
        return medName;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    @DynamoDBAttribute(attributeName = "time")
    @DynamoDBTypeConverted(converter = LocalTimeConverter.class)
    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "TimeIndex", attributeName = "customerId")
    public String getTimeIndexCustomerId() {
        return customerId;
    }

    public void setTimeIndexCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @DynamoDBIndexRangeKey(globalSecondaryIndexName = "TimeIndex", attributeName = "time")
    @DynamoDBTypeConverted(converter = LocalTimeConverter.class)
    public LocalTime getTimeIndexTime() {
        return time;
    }

    public void setTimeIndexTime(LocalTime time) {
        this.time = time;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Notification that = (Notification) o;
        return Objects.equals(customerId, that.customerId) &&
                Objects.equals(medName, that.medName) && Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, medName, time);
    }
}
