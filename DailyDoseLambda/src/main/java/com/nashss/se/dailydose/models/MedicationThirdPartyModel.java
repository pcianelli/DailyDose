package com.nashss.se.dailydose.models;

import java.util.Objects;

public class MedicationThirdPartyModel {
    private final String activeIngredient;
    private final String indicationsAndUsage;
    private final String warnings;
    private final String doNotUse;

    private MedicationThirdPartyModel(String activeIngredient, String indicationsAndUsage, String warnings, String doNotUse) {
        this.activeIngredient = activeIngredient;
        this.indicationsAndUsage = indicationsAndUsage;
        this.warnings = warnings;
        this.doNotUse = doNotUse;
    }

    public String getActiveIngredient() {
        return activeIngredient;
    }

    public String getIndicationsAndUsage() {
        return indicationsAndUsage;
    }

    public String getWarnings() {
        return warnings;
    }

    public String getDoNotUse() {
        return doNotUse;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicationThirdPartyModel that = (MedicationThirdPartyModel) o;
        return Objects.equals(activeIngredient, that.activeIngredient) && Objects.equals(indicationsAndUsage, that.indicationsAndUsage) && Objects.equals(warnings, that.warnings) && Objects.equals(doNotUse, that.doNotUse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activeIngredient, indicationsAndUsage, warnings, doNotUse);
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String activeIngredient;
        private String indicationsAndUsage;
        private String warnings;
        private String doNotUse;

        public Builder withActiveIngredient(String activeIngredient) {
            this.activeIngredient = activeIngredient;
            return this;
        }

        public Builder withIndicationsAndUsage(String indicationsAndUsage) {
            this.indicationsAndUsage = indicationsAndUsage;
            return this;
        }

        public Builder withWarnings(String warnings) {
            this.warnings = warnings;
            return this;
        }

        public Builder withDoNotUse(String doNotUse) {
            this.doNotUse = doNotUse;
            return this;
        }

        public MedicationThirdPartyModel build() {
            return new MedicationThirdPartyModel(activeIngredient, indicationsAndUsage, warnings, doNotUse);
        }
    }
}
