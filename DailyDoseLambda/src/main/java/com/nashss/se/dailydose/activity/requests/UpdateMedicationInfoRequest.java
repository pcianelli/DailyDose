package com.nashss.se.dailydose.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = UpdateMedicationInfoRequest.Builder.class)
public class UpdateMedicationInfoRequest {
    private final String customerId;
    private final String medName;
    private final String medInfo;

    private UpdateMedicationInfoRequest(String customerId, String medName, String medInfo) {
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
        return "UpdateMedicationInfoRequest{" +
                "customerId='" + customerId + '\'' +
                ", medName='" + medName + '\'' +
                ", medInfo='" + medInfo + '\'' +
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
        private String medInfo;

        public Builder withCustomerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder withMedName(String medName) {
            this.medName = medName;
            return this;
        }

        public Builder withMedInfo(String medInfo) {
            this.medInfo = medInfo;
            return this;
        }

        public UpdateMedicationInfoRequest build() {
            return new UpdateMedicationInfoRequest(customerId, medName, medInfo);
        }
    }
}
