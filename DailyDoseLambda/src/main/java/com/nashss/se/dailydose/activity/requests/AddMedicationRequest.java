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

    /**
     * This method returns a new Builder object for building a AddMedicationsRequest object.
     *
     * @return a new Builder object.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * This class provides a builder for the AddMedicationsRequest object.
     */
    public static class Builder {

        private String customerId;
        private String medName;
        private String medInfo;

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
         * This method sets an medInfo field.
         * @param medInfo is the attribute field of the Medications table
         * @return Builder with medInfo set.
         */
        public Builder withMedInfo(String medInfo) {
            this.medInfo = medInfo;
            return this;
        }

        /**
         * This method builds and returns a new AddMedicationsRequest object.
         *
         * @return a new AddMedicationsRequest object.
         */
        public AddMedicationRequest build() {
            return new AddMedicationRequest(customerId, medName, medInfo);
        }
    }
}
