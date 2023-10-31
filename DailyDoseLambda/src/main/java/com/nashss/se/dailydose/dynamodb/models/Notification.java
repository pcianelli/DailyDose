package com.nashss.se.dailydose.dynamodb.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Objects;

@DynamoDBTable(tableName = "notifications")
public class Notification {
    private String customerId;
    private String time;
    private String medName;
    private String medInfo;

    @DynamoDBHashKey(attributeName = "customerId")
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @DynamoDBRangeKey(attributeName = "time")
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @DynamoDBAttribute(attributeName = "medName")
    public String getMedName() {
        return medName;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    @DynamoDBAttribute(attributeName = "medInfo")
    public String getMedInfo() {
        return medInfo;
    }

    public void setMedInfo(String medInfo) {
        this.medInfo = medInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return Objects.equals(customerId, that.customerId) && Objects.equals(time, that.time) && Objects.equals(medName, that.medName) && Objects.equals(medInfo, that.medInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, time, medName, medInfo);
    }
}
