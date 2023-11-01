package com.nashss.se.dailydose.dynamodb.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Objects;

@DynamoDBTable(tableName = "notifications")
public class Notification {
    private String customerId;
    private String medName;
    private String time;


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
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
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
    public String getTimeIndexTime() {
        return time;
    }

    public void setTimeIndexTime(String time) {
        this.time = time;
    }

    @DynamoDBAttribute(attributeName = "medName") // Include medName in the GSI
    public String getTimeIndexMedName() {
        return medName;
    }

    public void setTimeIndexMedName(String medName) {
        this.medName = medName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return Objects.equals(customerId, that.customerId) && Objects.equals(medName, that.medName) && Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, medName, time);
    }
}
