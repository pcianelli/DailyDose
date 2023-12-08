package com.nashss.se.dailydose.activity.requests;

import com.nashss.se.dailydose.activity.results.GetMedicationThirdPartyResult;

/**
 * This class represents a request to get the medication information from the openFDA API.
 * It is used as a part of the GetMedicationThirdPartyActivity API.
 */
public class GetMedicationThirdPartyRequest {
    private final String genericName;

    public GetMedicationThirdPartyRequest(String genericName) {
        this.genericName = genericName;
    }

    public String getGenericName() {
        return genericName;
    }

    /**
     * This method returns a string representation of the GetMedicationThirdPartyRequest object.
     *
     * @return a string representing the object.
     */
    @Override
    public String toString() {
        return "GetMedicationThirdPartyRequest{" +
                "genericName='" + genericName + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String genericName;

        public Builder withGenericName(String genericName) {
            this.genericName = genericName;
            return this;
        }

        public GetMedicationThirdPartyRequest build() {
            return new GetMedicationThirdPartyRequest(genericName);
        }
    }
}
