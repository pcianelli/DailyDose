package com.nashss.se.dailydose.activity.results;

import com.nashss.se.dailydose.models.MedicationModel;

public class RemoveMedicationResult {
    private final MedicationModel medicationModel;

    private RemoveMedicationResult(MedicationModel medicationModel) {
        this.medicationModel = medicationModel;
    }

    public MedicationModel getMedicationModel() {
        return medicationModel;
    }

    @Override
    public String toString() {
        return "RemoveMedicationResult{" +
                "medicationModel=" + medicationModel +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private MedicationModel medicationModel;

        public Builder withMedicationModel(MedicationModel medicationModel) {
            this.medicationModel = medicationModel;
            return this;
        }

        public RemoveMedicationResult build() {
            return new RemoveMedicationResult(medicationModel);
        }
    }
}
