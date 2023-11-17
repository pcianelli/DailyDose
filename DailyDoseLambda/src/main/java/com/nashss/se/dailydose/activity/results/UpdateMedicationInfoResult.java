package com.nashss.se.dailydose.activity.results;

import com.nashss.se.dailydose.models.MedicationModel;

public class UpdateMedicationInfoResult {
    private final MedicationModel medicationModel;

    private UpdateMedicationInfoResult(MedicationModel medicationModel) {
        this.medicationModel = medicationModel;
    }

    public MedicationModel getMedicationModel() {
        return medicationModel;
    }

    @Override
    public String toString() {
        return "UpdateMedicationInfoResult{" +
                "medicationModel=" + medicationModel +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private MedicationModel medicationModel;

        public Builder withMedicationModel(MedicationModel medicationModel) {
            this.medicationModel = medicationModel;
            return this;
        }

        public UpdateMedicationInfoResult build() {
            return new UpdateMedicationInfoResult(medicationModel);
        }
    }
}
