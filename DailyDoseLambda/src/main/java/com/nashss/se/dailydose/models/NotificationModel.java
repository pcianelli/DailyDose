package com.nashss.se.dailydose.models;

import java.util.Objects;

public class NotificationModel{
    private final String customerId;
    private final String time;
    private final String medName;
    private final String medInfo;

    private NotificationModel(String customerId, String time, String medName, String medInfo) {
        this.customerId = customerId;
        this.time = time;
        this.medName = medName;
        this.medInfo = medInfo;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getTime() {
        return time;
    }

    public String getMedName() {
        return medName;
    }

    public String getMedInfo() {
        return medInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationModel that = (NotificationModel) o;
        return Objects.equals(customerId, that.customerId) && Objects.equals(time, that.time) && Objects.equals(medName, that.medName) && Objects.equals(medInfo, that.medInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, time, medName, medInfo);
    }

    /**
     *
     * @return new Builder of NotificationModel
     */
    public static MedicationModel.Builder builder() {return new MedicationModel.Builder();}

    public static class Builder {
        private String customerId;
        private String time;
        private String medName;
        private String medInfo;

        /**
         * @param buildCustomerId string
         * @return this
         */
        public Builder withCustomerId(String buildCustomerId) {
            this.customerId = buildCustomerId;
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
         * @param buildMedName string
         * @return this
         */
        public Builder withMedName(String buildMedName) {
            this.medName = buildMedName;
            return this;
        }

        /**
         * @param buildMedInfo string
         * @return this
         */
        public Builder withMedInfo(String buildMedInfo) {
            this.medInfo = buildMedInfo;
            return this;
        }
    }
    public NotificationModel build() {
        return new NotificationModel(customerId, time, medName, medInfo);
    }
}
