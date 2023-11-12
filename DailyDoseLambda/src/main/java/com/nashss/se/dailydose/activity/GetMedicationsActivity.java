package com.nashss.se.dailydose.activity;

import com.nashss.se.dailydose.activity.requests.GetMedicationsRequest;
import com.nashss.se.dailydose.activity.results.GetMedicationsResult;
import com.nashss.se.dailydose.converters.ModelConverter;
import com.nashss.se.dailydose.dynamodb.MedicationDao;

import com.nashss.se.dailydose.dynamodb.NotificationDao;
import com.nashss.se.dailydose.dynamodb.models.Medication;
import com.nashss.se.dailydose.dynamodb.models.Notification;
import com.nashss.se.dailydose.models.MedicationModel;
import com.nashss.se.dailydose.models.NotificationModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

/**
 * Implementation of the GetMedicationsActivity for the DailyDose GetMedications API.
 * <p>
 * This API allows the customer to get the list medications they are tracking.
 */
public class GetMedicationsActivity {
    private final Logger log = LogManager.getLogger();
    private final MedicationDao medicationDao;
    private final NotificationDao notificationDao;
    private final ModelConverter modelConverter;

    /**
     * Instantiates a new GetMedicationsActivity object.
     *
     * @param medicationDao medicationDao to access the medications table.
     * @param notificationDao notificationDao to access the notification table.
     */
    @Inject
    public GetMedicationsActivity(MedicationDao medicationDao, NotificationDao notificationDao) {
        this.medicationDao = medicationDao;
        this.notificationDao = notificationDao;
        modelConverter = new ModelConverter();
    }

    /**
     * This method handles the incoming request by retrieving all medications from the database
     * and a set of Notifications from the notification table.
     * <p>
     * It then returns a List of MedicationModels.
     * <p>
     * If the medication or notification does not exist, this should return an empty list.
     *
     * @param getMedicationsRequest request object
     * @return getMedicationResult result object containing the list of medicationModels
     */
    public GetMedicationsResult handleRequest(final GetMedicationsRequest getMedicationsRequest) {
        log.info("Received GetMedicationsRequest {}", getMedicationsRequest);

        String customerId = getMedicationsRequest.getCustomerId();
        String medName = getMedicationsRequest.getMedName();

        List<Medication> medicationList = medicationDao.getMedications(customerId, medName);

        List<MedicationModel> medicationModelList = new ArrayList<>();

        for(Medication medication: medicationList) {
            Set<Notification> notificationSet = notificationDao.getNotifications(customerId, medication.getMedName());
            Set<NotificationModel> notificationModelSet = modelConverter.toNotificationModelSet(notificationSet);

            MedicationModel medicationModel = modelConverter.toMedicationModel(medication, notificationModelSet);
            medicationModelList.add(medicationModel);
        }

        return GetMedicationsResult.builder()
                .withMedicationModelList(medicationModelList)
                .build();
    }
}
