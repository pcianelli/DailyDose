package com.nashss.se.dailydose.activity;

import com.nashss.se.dailydose.activity.requests.AddMedicationRequest;
import com.nashss.se.dailydose.activity.results.AddMedicationResult;
import com.nashss.se.dailydose.converters.ModelConverter;
import com.nashss.se.dailydose.dynamodb.MedicationDao;
import com.nashss.se.dailydose.dynamodb.models.Medication;
import com.nashss.se.dailydose.exceptions.InvalidAttributeValueException;
import com.nashss.se.dailydose.models.MedicationModel;

import com.nashss.se.dailydose.utils.IdUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;

import javax.inject.Inject;

/**
 * Implementation of the AddMedicationActivity for the DailyDose's AddMedicationActivity API.
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

        IdUtils.validateMedicationName(medName);
        IdUtils.validMedNameNotBlank(medName);

        Medication medication = new Medication();
        medication.setCustomerId(addMedicationRequest.getCustomerId());
        medication.setMedName(addMedicationRequest.getMedName());
        if (addMedicationRequest.getMedInfo() == null || addMedicationRequest.getMedInfo().equals("")) {
            medication.setMedInfo("");
        } else {
            medication.setMedInfo(addMedicationRequest.getMedInfo());
        }

        medicationDao.addMedication(medication);

        MedicationModel medicationModel = new ModelConverter().toMedicationModel(medication, new HashSet<>());

        return AddMedicationResult.builder()
                .withMedicationModel(medicationModel)
                .build();
    }
}
