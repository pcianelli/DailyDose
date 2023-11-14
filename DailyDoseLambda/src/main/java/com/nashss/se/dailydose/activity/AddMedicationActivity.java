package com.nashss.se.dailydose.activity;

import com.nashss.se.dailydose.activity.requests.AddMedicationRequest;
import com.nashss.se.dailydose.activity.results.AddMedicationResult;
import com.nashss.se.dailydose.dynamodb.MedicationDao;
import com.nashss.se.dailydose.dynamodb.models.Medication;
import com.nashss.se.dailydose.exceptions.InvalidAttributeValueException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

/**
 * Implementation of the AddMedicationActivity for the DailyDose's AddMedicationActivity  API.
 * This API allows the user to add a medication to their healthChart.
 */
public class AddMedicationActivity {
    private final Logger log = LogManager.getLogger();
    private final MedicationDao medicationDao;

    /**
     * Instantiates a new AddMedicationActivity object.
     *
     * @param medicationDao  to access the medication table.
     */
    @Inject
    public AddMedicationActivity(MedicationDao medicationDao) {
        this.medicationDao = medicationDao;
    }

    /**
     * This method handles the incoming request by adding a medication to the database.
     * It then returns a AddMedicationResult.
     * <p>
     * If the medication already exists, appropriate message should be returned.
     * If medInfo is blank or null, should return blank string
     *
     * @param addMedicationRequest request object to add a medication
     * @return addMedicationResult result object indicating the outcome of the creation process
     */
    public AddMedicationResult handleRequest(final AddMedicationRequest addMedicationRequest) {
        log.info("Received AddMedicationRequest {}", addMedicationRequest);

        String medName = addMedicationRequest.getMedName();
        // Check for invalid characters in the name
        if (!medName.matches("[a-zA-Z0-9 ]*")) {
            throw new InvalidAttributeValueException("Invalid characters in the vendor name.");
        }

        Medication medication = new Medication();
        medication.setCustomerId(addMedicationRequest.getCustomerId());
        medication.setMedName(addMedicationRequest.getMedName());
        if(addMedicationRequest.getMedInfo() == null || addMedicationRequest.getMedInfo().equals("")) {
            medication.setMedInfo("");
        } else {
            medication.setMedInfo(addMedicationRequest.getMedInfo());
        }

        boolean success = medicationDao.addMedication(medication);
        String message = success ? "Medication added successfully." : "Failed to add Medication.";

        return AddMedicationResult.builder()
                .withCustomerId(medication.getCustomerId())
                .withMedName(medication.getMedName())
                .withSuccess(success)
                .withMessage(message)
                .build();
    }
}
