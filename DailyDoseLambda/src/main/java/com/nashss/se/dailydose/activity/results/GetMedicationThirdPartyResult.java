package com.nashss.se.dailydose.activity.results;

import com.nashss.se.dailydose.models.MedicationThirdPartyModel;

public class GetMedicationThirdPartyResult {
    private final MedicationThirdPartyModel medicationThirdPartyModel;

    public GetMedicationThirdPartyResult(MedicationThirdPartyModel medicationThirdPartyModel) {
        this.medicationThirdPartyModel = medicationThirdPartyModel;
    }

    public MedicationThirdPartyModel getMedicationThirdPartyModel() {
        return medicationThirdPartyModel;
    }

    @Override
    public String toString() {
        return "GetMedicationThirdPartyResult{" +
                "medicationThirdPartyModel=" + medicationThirdPartyModel +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private MedicationThirdPartyModel medicationThirdPartyModel;

        public Builder withMedicationThirdPartyModel(MedicationThirdPartyModel medicationThirdPartyModel) {
            this.medicationThirdPartyModel = medicationThirdPartyModel;
            return this;
        }

        public GetMedicationThirdPartyResult build() {
                return new GetMedicationThirdPartyResult(medicationThirdPartyModel);
        }
    }
}
