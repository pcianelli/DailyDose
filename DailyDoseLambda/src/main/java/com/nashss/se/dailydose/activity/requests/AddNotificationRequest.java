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

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {
        private String customerId;
        private String medName;
        private String time;

        public Builder withCustomerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder withMedName(String medName) {
            this.medName = medName;
            return this;
        }

        public Builder withTime(String time) {
            this.time = time;
            return this;
        }

        public AddNotificationRequest build() {
            return new AddNotificationRequest(customerId, medName, time);
        }
    }

}
