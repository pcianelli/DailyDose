package com.nashss.se.dailydose.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * This class represents a request to add a medication.
 * It is used as a part of the AddMedicationActivity API.
 */
@JsonDeserialize(builder = AddMedicationRequest.Builder.class)
public class AddMedicationRequest {
    private final String customerId;
    private final String medName;
    private final String medInfo;

    private AddMedicationRequest(String customerId, String medName, String medInfo) {
        this.customerId = customerId;
        this.medName = medName;
        this.medInfo = medInfo;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getMedName() {
        return medName;
    }

    public String getMedInfo() {
        return medInfo;
    }

    @Override
    public String toString() {
        return "AddMedicationRequest{" +
                "customerId='" + customerId + '\'' +
                ", medName='" + medName + '\'' +
                ", medInfo='" + medInfo + '\'' +
                '}';
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {

        private String customerId;
        private String medName;
        private String medInfo;

        public Builder withCustomerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder withMedName(String medName) {
            this.medName = medName;
            return this;
        }

        public Builder withMedInfo(String medInfo) {
            this.medInfo = medInfo;
            return this;
        }

        public AddMedicationRequest build() {
            return new AddMedicationRequest(customerId, medName, medInfo);
        }
    }
}
