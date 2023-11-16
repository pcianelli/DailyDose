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

    /**
     * This method returns a new Builder object for building a RemoveMedicationsRequest object.
     *
     * @return a new Builder object.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * This class provides a builder for the RemoveMedicationsRequest object.
     */
    public static class Builder {
        private String customerId;
        private String medName;

        /**
         * This method sets an customerId field.
         * @param customerId is the partition key of the Medications table
         * @return Builder with customerId set.
         */
        public Builder withCustomerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        /**
         * This method sets an medName field.
         * @param medName is the partition key of the Medications table
         * @return Builder with medName set.
         */
        public Builder withMedName(String medName) {
            this.medName = medName;
            return this;
        }

        /**
         * This method builds and returns a new RemoveMedicationsRequest object.
         *
         * @return a new RemoveMedicationsRequest object.
         */
        public RemoveMedicationRequest build() {
            return new RemoveMedicationRequest(customerId, medName);
        }
    }

}
