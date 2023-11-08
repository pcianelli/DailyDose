package com.nashss.se.dailydose.activity.results;

import com.nashss.se.dailydose.models.MedicationModel;

import java.util.List;

public class GetMedicationsResult {
    private final List<MedicationModel> medicationModelList;
    private final String customerId;
    private final String medName;

    private GetMedicationsResult(List<MedicationModel> medicationModelList, String customerId, String medName) {
        this.medicationModelList = medicationModelList;
        this.customerId = customerId;
        this.medName = medName;
    }

    public List<MedicationModel> getMedicationModelList() {
        return medicationModelList;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getMedName() {
        return medName;
    }

    @Override
    public String toString() {
        return "GetMedicationsResult{" +
                "medicationModelList=" + medicationModelList +
                ", customerId='" + customerId + '\'' +
                ", medName='" + medName + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private List<MedicationModel> medicationModelList;
        private String customerId;
        private String medName;

        public Builder withMedicationModelList(List<MedicationModel> medicationModelList) {
            this.medicationModelList = medicationModelList;
            return this;
        }

        public Builder withCustomerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder withMedName(String medName) {
            this.medName = medName;
            return this;
        }

        public GetMedicationsResult build() {
            return new GetMedicationsResult(medicationModelList, customerId, medName);
        }
    }
}
