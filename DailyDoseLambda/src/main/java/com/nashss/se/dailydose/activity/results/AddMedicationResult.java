package com.nashss.se.dailydose.activity.results;

public class AddMedicationResult {
    private final String customerId;
    private final String medName;
    private final boolean success;
    private final String message;

    private AddMedicationResult(String customerId, String medName, boolean success, String message) {
        this.customerId = customerId;
        this.medName = medName;
        this.success = success;
        this.message = message;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getMedName() {
        return medName;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "AddMedicationResult{" +
                "customerId='" + customerId + '\'' +
                ", medName='" + medName + '\'' +
                ", success=" + success +
                ", message='" + message + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String customerId;
        private String medName;
        private boolean success;
        private String message;

        public Builder withCustomerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder withMedName(String medName) {
            this.medName = medName;
            return this;
        }

        public Builder withSuccess(boolean success) {
            this.success = success;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public AddMedicationResult build() {
            return new AddMedicationResult(customerId, medName, success, message);
        }
    }
}
