package com.nashss.se.dailydose.models;

import java.util.Objects;
import java.util.Set;

public class MedicationModel {
    private final String customerId;
    private final String medName;
    private final String medInfo;
    private final Set<String> notificationTimes;

    private MedicationModel(String customerId, String medName, String medInfo, Set<String> notificationTimes) {
        this.customerId = customerId;
        this.medName = medName;
        this.medInfo = medInfo;
        this.notificationTimes = notificationTimes;
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

    /**
     *
     * @return Set of notification times
     */
    public Set<String> getNotificationTimes() {
        return notificationTimes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicationModel that = (MedicationModel) o;
        return Objects.equals(customerId, that.customerId) && Objects.equals(medName, that.medName) && Objects.equals(medInfo, that.medInfo) && Objects.equals(notificationTimes, that.notificationTimes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, medName, medInfo, notificationTimes);
    }


    /**
     *
     * @return new Builder of MedicationModel
     */
    public static Builder builder() {return new Builder();}

    public static class Builder {
        private String customerId;
        private String medName;
        private String medInfo;
        private Set<String> notificationTimes;


        /**
         * @param buildCustomerId string
         * @return this
         */
        public Builder withCustomerId(String buildCustomerId) {
            this.customerId = buildCustomerId;
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

        /**
         * @param buildNotificationTimes Set of strings
         * @return this
         */
        public Builder withNotificationTimes(Set<String> buildNotificationTimes) {
            this.notificationTimes = buildNotificationTimes;
            return this;
        }
    }
    public MedicationModel build() {
        return new MedicationModel(customerId, medName, medInfo, notificationTimes);
    }
}
