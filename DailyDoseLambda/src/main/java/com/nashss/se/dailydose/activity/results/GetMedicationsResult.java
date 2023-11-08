package com.nashss.se.dailydose.activity.results;

import com.nashss.se.dailydose.models.MedicationModel;

import java.util.List;

public class GetMedicationsResult {
    private final List<MedicationModel> medicationModelList;


    private GetMedicationsResult(List<MedicationModel> medicationModelList) {
        this.medicationModelList = medicationModelList;
    }

    public List<MedicationModel> getMedicationModelList() {
        return medicationModelList;
    }

    @Override
    public String toString() {
        return "GetMedicationsResult{" +
                "medicationModelList=" + medicationModelList +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private List<MedicationModel> medicationModelList;

        public Builder withMedicationModelList(List<MedicationModel> medicationModelList) {
            this.medicationModelList = medicationModelList;
            return this;
        }

        public GetMedicationsResult build() {
            return new GetMedicationsResult(medicationModelList);
        }
    }
}
