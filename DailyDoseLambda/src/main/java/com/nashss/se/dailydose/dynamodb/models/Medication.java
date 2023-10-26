package com.nashss.se.dailydose.dynamodb.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@DynamoDBTable(tableName = "medications")
public class Medication {
    private String customerId;
    private String medName;
    private String medInfo;
    private Set<String> notificationTimes;


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


    @DynamoDBAttribute(attributeName = "medInfo")
    public String getMedInfo() {
        return medInfo;
    }

    public void setMedInfo(String medInfo) {
        this.medInfo = medInfo;
    }

    /**
     *
     * @return notificationTimes in a new Hashset
     */
    @DynamoDBAttribute(attributeName = "notificationTimes")
    public Set<String> getNotificationTimes() {
        // normally, we would prefer to return an empty Set if there are no
        // notificationTimes, but DynamoDB doesn't represent empty Sets...needs to be null
        // instead
        if (null == notificationTimes) {
            return null;
        }
        return new HashSet<>(notificationTimes);
    }

    /**
     * Sets the notificationTimes for this medicationList as a copy of input, or null if input is null.
     *
     * @param notificationTimes Set of tags for this medicationList
     */
    public void setNotificationTimes(Set<String> notificationTimes) {
        // see comment in getNotificationTimes()
        if (null == notificationTimes) {
            this.notificationTimes = null;
        } else {
            this.notificationTimes = new HashSet<>(notificationTimes);
        }
        this.notificationTimes = notificationTimes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Medication that = (Medication) o;
        return Objects.equals(customerId, that.customerId) && Objects.equals(medName, that.medName) && Objects.equals(medInfo, that.medInfo) && Objects.equals(notificationTimes, that.notificationTimes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, medName, medInfo, notificationTimes);
    }
}
