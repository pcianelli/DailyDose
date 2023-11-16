package com.nashss.se.dailydose.activity;

import com.nashss.se.dailydose.activity.requests.RemoveMedicationRequest;
import com.nashss.se.dailydose.activity.results.RemoveMedicationResult;
import com.nashss.se.dailydose.converters.ModelConverter;
import com.nashss.se.dailydose.dynamodb.MedicationDao;
import com.nashss.se.dailydose.dynamodb.models.Medication;
import com.nashss.se.dailydose.exceptions.InvalidAttributeValueException;
import com.nashss.se.dailydose.models.MedicationModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;

import javax.inject.Inject;

/**
 * Implementation of the RemoveMedicationActivity for the DailyDose's RemoveMedicationActivity API.
 * This API allows the user to remove a medication from their healthChart.
 */
public class RemoveMedicationActivity {
    private final Logger log = LogManager.getLogger();
    private final MedicationDao medicationDao;

    /**
     * Instantiates a new RemoveMedicationActivity object.
     *
     * @param medicationDao  to access the medication table.
     */
    @Inject
    public RemoveMedicationActivity(MedicationDao medicationDao) {
        this.medicationDao = medicationDao;
    }

    /**
     * This method handles the incoming request by removing a medication from the database.
     * It then returns a RemoveMedicationResult.
     * <p>
     * If the medication does not exist, appropriate message should be returned.
     *
     * @param removeMedicationRequest request object to remove a medication
     * @return removeMedicationResult result object indicating the outcome of the removal process
     */
    public RemoveMedicationResult handleRequest(final RemoveMedicationRequest removeMedicationRequest) {
        log.info("Received RemoveMedicationRequest {}", removeMedicationRequest);

        String medName = removeMedicationRequest.getMedName();
        // Check for invalid characters in the name
        if (!medName.matches("[a-zA-Z0-9 ]*")) {
            throw new InvalidAttributeValueException("Invalid characters in the vendor name.");
        }
        if (medName.equals("")) {
            throw new IllegalArgumentException("MedName cannot be null or blank");
        }

        Medication medication = new Medication();
        medication.setCustomerId(removeMedicationRequest.getCustomerId());
        medication.setMedName(removeMedicationRequest.getMedName());

        medicationDao.removeMedication(medication);

        MedicationModel medicationModel = new ModelConverter().toMedicationModel(medication, new HashSet<>());

        return RemoveMedicationResult.builder()
                .withMedicationModel(medicationModel)
                .build();
    }
}
