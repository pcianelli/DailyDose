package com.nashss.se.dailydose.activity;

import com.nashss.se.dailydose.activity.requests.UpdateMedicationInfoRequest;
import com.nashss.se.dailydose.activity.results.UpdateMedicationInfoResult;
import com.nashss.se.dailydose.converters.ModelConverter;
import com.nashss.se.dailydose.dynamodb.MedicationDao;
import com.nashss.se.dailydose.dynamodb.models.Medication;
import com.nashss.se.dailydose.exceptions.MedicationNotFoundException;
import com.nashss.se.dailydose.models.MedicationModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;

import javax.inject.Inject;

public class UpdateMedicationInfoActivity {

    /**
     * Implementation of the UpdateMedicationInfoActivity for the DailyDose's UpdateMedicationInfo API.
     * This API allows the customer to update their saved medication's information.
     */

    private final Logger log = LogManager.getLogger();
    private final MedicationDao medicationDao;
    private final ModelConverter modelConverter;

    /**
     * Instantiates a new UpdateMedicationInfoActivity object.
     *
     * @param medicationDao MedicationDao to access the medication table.
     */
    @Inject
    public UpdateMedicationInfoActivity(MedicationDao medicationDao) {
        this.medicationDao = medicationDao;
        this.modelConverter = new ModelConverter();
    }

    /**
     * This method handles the incoming request by retrieving the medication, updating it,
     * and persisting the medication.
     * <p>
     * It then returns the updated medication.
     * <p>
     * If the medication does not exist, this should throw a MedicationNotFoundException.
     * <p>
     * If the provided medication name or customer ID has invalid characters, throws an
     * InvalidAttributeValueException
     * <p>
     * If the request tries to update the customer ID,
     * this should throw an InvalidAttributeChangeException
     *
     * @param updateMedicationInfoRequest request object containing the medication ID,
     *                                    medication name, and medication info associated with it
     * @return updateMedicationInfoResult result object containing the API defined {@link MedicationModel}
     */
    public UpdateMedicationInfoResult handleRequest(final UpdateMedicationInfoRequest updateMedicationInfoRequest) {
        log.info("Received UpdatedMedicationInfoRequest {}", updateMedicationInfoRequest);

        Medication medication = medicationDao.getOneMedication(updateMedicationInfoRequest.getCustomerId(),
                updateMedicationInfoRequest.getMedName());

        if (!medication.getMedName().equals(updateMedicationInfoRequest.getMedName())) {
            throw new MedicationNotFoundException("Medication must exist in your healthChart");
        }

//        if (updateMedicationInfoRequest.getMedInfo() == null || updateMedicationInfoRequest.getMedInfo().equals("")) {
//            medication.setMedInfo("");
//        } else {
            medication.setMedInfo(updateMedicationInfoRequest.getMedInfo());
//        }

        medication = medicationDao.addMedication(medication);

        MedicationModel medicationModel = modelConverter.toMedicationModel(medication, new HashSet<>());

        return UpdateMedicationInfoResult.builder()
                .withMedicationModel(medicationModel)
                .build();
    }
}
