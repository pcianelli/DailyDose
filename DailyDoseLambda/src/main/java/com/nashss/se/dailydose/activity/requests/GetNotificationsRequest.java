package com.nashss.se.dailydose.activity.requests;

/**
 * This class represents a request to get all notifications for a specific time window.
 * It is used as a part of the GetNotificationsActivity API.
 */
public class GetNotificationsRequest {
    private final String customerId;
    private final String time;

    private GetNotificationsRequest(String customerId, String time) {
        this.customerId = customerId;
        this.time = time;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getTime() {
        return time;
    }

    /**
     * This method returns a string representation of the GetNotificationsRequest object.
     *
     * @return a string representing the object.
     */
    @Override
    public String toString() {
        return "GetNotificationsRequest{" +
                "customerId='" + customerId + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String customerId;
        private String time;

        public Builder withCustomerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder withTime(String time) {
            this.time = time;
            return this;
        }

        public GetNotificationsRequest build() {
            return new GetNotificationsRequest(customerId, time);
        }
    }
}
