package com.nashss.se.dailydose.activity.requests;

/**
 * This class represents a request to get all medications.
 * It is used as a part of the GetMedicationsActivity API.
 */
public class GetMedicationsRequest {
    private final String customerId;
    private final String medName;

    private GetMedicationsRequest(String customerId, String medName) {
        this.customerId = customerId;
        this.medName = medName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getMedName() {
        return medName;
    }

    /**
     * This method returns a string representation of the GetMedicationsRequest object.
     *
     * @return a string representing the object.
     */
    @Override
    public String toString() {
        return "GetMedicationsRequest{" +
                "customerId='" + customerId + '\'' +
                ", medName='" + medName + '\'' +
                '}';
    }

    /**
     * This method returns a new Builder object for building a GetMedicationsRequest object.
     *
     * @return a new Builder object.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * This class provides a builder for the GetMedicationsRequest object.
     */
    public static class Builder {
        private String customerId;
        private String medName;

        /**
         * This method sets an customerId field.
         * @param setCustomerId is the partition key of the Medications table
         * @return Builder with customerId set.
         */
        public Builder withCustomerId(String setCustomerId) {
            this.customerId = setCustomerId;
            return this;
        }

        /**
         * This method sets an medName field.
         * @param setMedName is the partition key of the Medications table
         * @return Builder with medName set.
         */
        public Builder withMedName(String setMedName) {
            this.medName = setMedName;
            return this;
        }

        /**
         * This method builds and returns a new GetMedicationsRequest object.
         *
         * @return a new GetMedicationsRequest object.
         */
        public GetMedicationsRequest build() {
            return new GetMedicationsRequest(customerId, medName);
        }
    }
}
