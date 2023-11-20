package com.nashss.se.dailydose.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * This class represents a request to add a notification.
 * It is used as a part of the AddNotificationActivity API.
 */
@JsonDeserialize(builder = AddNotificationRequest.Builder.class)
public class AddNotificationRequest {
    private final String customerId;
    private final String medName;
    private final String time;

    private AddNotificationRequest(String customerId, String medName, String time) {
        this.customerId = customerId;
        this.medName = medName;
        this.time = time;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getMedName() {
        return medName;
    }

    public String getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "addNotificationRequest{" +
                "customerId='" + customerId + '\'' +
                ", medName='" + medName + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    /**
     * This method returns a new Builder object for building a AddNotificationRequest object.
     *
     * @return a new Builder object.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * This class provides a builder for the AddNotificationRequest object.
     */
    @JsonPOJOBuilder
    public static class Builder {
        private String customerId;
        private String medName;
        private String time;

        /**
         * This method sets an customerId field.
         * @param customerId is the partition key of the Notifications table
         * @return Builder with customerId set.
         */
        public Builder withCustomerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        /**
         * This method sets an medName field.
         * @param medName is the partition key of the Notifications table
         * @return Builder with medName set.
         */
        public Builder withMedName(String medName) {
            this.medName = medName;
            return this;
        }

        /**
         * This method sets a time field.
         * @param time is the partition key of the Notifications table
         * @return Builder with time set.
         */
        public Builder withTime(String time) {
            this.time = time;
            return this;
        }

        /**
         * This method builds and returns a new AddNotificationRequest object.
         *
         * @return a new AddNotificationRequest object.
         */
        public AddNotificationRequest build() {
            return new AddNotificationRequest(customerId, medName, time);
        }
    }

}
