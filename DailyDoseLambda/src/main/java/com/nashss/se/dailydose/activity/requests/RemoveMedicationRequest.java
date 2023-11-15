package com.nashss.se.dailydose.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * This class represents a request to remove a medication.
 * It is used as a part of the RemoveMedicationActivity API.
 */
@JsonDeserialize(builder = RemoveMedicationRequest.Builder.class)
public class RemoveMedicationRequest {
    private final String customerId;
    private final String medName;

    private RemoveMedicationRequest(String customerId, String medName) {
        this.customerId = customerId;
        this.medName = medName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getMedName() {
        return medName;
    }

    @Override
    public String toString() {
        return "RemoveMedicationRequest{" +
                "customerId='" + customerId + '\'' +
                ", medName='" + medName + '\'' +
                '}';
    }
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String customerId;
        private String medName;

        public Builder withCustomerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder withMedName(String medName) {
            this.medName = medName;
            return this;
        }

        public RemoveMedicationRequest build() {
            return new RemoveMedicationRequest(customerId, medName);
        }
    }

}
