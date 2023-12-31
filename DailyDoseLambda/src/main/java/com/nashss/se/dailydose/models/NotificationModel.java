package com.nashss.se.dailydose.models;

import java.util.Objects;

public class NotificationModel {
    private final String customerId;
    private final String notificationId;
    private final String medName;
    private final String time;


    private NotificationModel(String customerId, String notificationId, String medName, String time) {
        this.customerId = customerId;
        this.notificationId = notificationId;
        this.medName = medName;
        this.time = time;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public String getMedName() {
        return medName;
    }

    public String getTime() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NotificationModel that = (NotificationModel) o;
        return Objects.equals(customerId, that.customerId) &&
                Objects.equals(notificationId, that.notificationId) &&
                Objects.equals(medName, that.medName) && Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, notificationId, medName, time);
    }

    /**
     *
     * @return new Builder of NotificationModel
     */

    public static NotificationModel.Builder builder() {
        return new NotificationModel.Builder();
    }

    public static class Builder {
        private String customerId;
        private String notificationId;
        private String medName;
        private String time;


        /**
         * @param buildCustomerId string
         * @return this
         */
        public Builder withCustomerId(String buildCustomerId) {
            this.customerId = buildCustomerId;
            return this;
        }

        /**
         * @param buildNotificationId string
         * @return this
         */
        public Builder withNotificationId(String buildNotificationId) {
            this.notificationId = buildNotificationId;
            return this;
        }

        /**
         * @param buildMedName string
         * @return this
         */
        public Builder withMedName(String buildMedName) {
            this.medName = buildMedName;
            return this;
        }

        /**
         * @param buildTime string
         * @return this
         */
        public Builder withTime(String buildTime) {
            this.time = buildTime;
            return this;
        }

        /**
         * @return NotificationModel
         */
        public NotificationModel build() {
            return new NotificationModel(customerId, notificationId, medName, time);
        }
    }
}
