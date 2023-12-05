package com.nashss.se.dailydose.dynamodb.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Objects;

@DynamoDBTable(tableName = "medications")
public class Medication {
    private String customerId;
    private String medName;
    private String medInfo;


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
        if(medInfo == null || medInfo.equals("")) {
            this.medInfo = "";
        } else {
            this.medInfo = medInfo;
        }
    }

    /**
     *
     * @return notifications in a new Hashset
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Medication that = (Medication) o;
        return Objects.equals(customerId, that.customerId) && Objects.equals(medName, that.medName) &&
                Objects.equals(medInfo, that.medInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, medName, medInfo);
    }
}
