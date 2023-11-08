package com.nashss.se.dailydose.converters;

import com.nashss.se.dailydose.dynamodb.models.Medication;
import com.nashss.se.dailydose.dynamodb.models.Notification;
import com.nashss.se.dailydose.models.MedicationModel;
import com.nashss.se.dailydose.models.NotificationModel;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ModelConverterTest {
    private ModelConverter modelConverter = new ModelConverter();
    private final List<Medication> listData = new ArrayList<>();
    private final Set<Notification> setData = new HashSet<>();
    private LocalTimeConverter timeConverter = new LocalTimeConverter();

    @Test
    public void toMedicationModel_withAMedication_convertsToMedicationModel() {
        //GIVEN
        Medication medication = new Medication();
        medication.setCustomerId("customerId");
        medication.setMedName("medName");
        medication.setMedInfo("medInfo");
        Set<NotificationModel> notificationModelSet = new HashSet<>();


        //WHEN
        MedicationModel result = modelConverter.toMedicationModel(medication, notificationModelSet);

        //THEN
        assertEquals(medication.getCustomerId(), result.getCustomerId());
        assertEquals(medication.getMedName(), result.getMedName());
        assertEquals(medication.getMedInfo(), result.getMedInfo());
        assertEquals(notificationModelSet, result.getNotificationTimes());
    }

    @Test
    public void toMedicationModelList_withMedicationAndSetOfNotifications_convertsToListOfMedicationModels() {
        //GIVEN
        Medication medication1 = new Medication();
        medication1.setCustomerId("customerId");
        medication1.setMedName("medName");
        medication1.setMedInfo("medInfo");

        Medication medication2 = new Medication();
        medication2.setCustomerId("customerId2");
        medication2.setMedName("medName2");
        medication2.setMedInfo("medInfo2");

        Set<NotificationModel> notificationModelSet1 = new HashSet<>();

        listData.add(medication1);
        listData.add(medication2);

        //WHEN
        List<MedicationModel> medicationModelList = modelConverter.toMedicationModelList(listData, notificationModelSet1);

        //THEN
        MedicationModel medicationResult1 = medicationModelList.get(0);
        MedicationModel medicationModel1 = modelConverter.toMedicationModel(medication1,notificationModelSet1);
        assertEquals(medicationModel1, medicationResult1);
    }

    @Test
    public void toNotificationModel_withNotification_convertsToNotificationModel() {
        //GIVEN
        Notification notification = new Notification();
        notification.setCustomerId("customerId");
        notification.setMedName("medName");
        notification.setTime(LocalTime.now());

        //WHEN
       NotificationModel result = modelConverter.toNotificationModel(notification);

       //THEN
        assertEquals(notification.getMedName(), result.getMedName());
        assertEquals(notification.getCustomerId(), result.getCustomerId());
        assertEquals(notification.getTime(), timeConverter.unconvert(result.getTime()));
    }

    @Test
    public void toNotificationModelSet_withSetOfNotifications_convertsToSetOfNotificationModels() {
        //GIVEN
        Notification notification = new Notification();
        notification.setCustomerId("customerId");
        notification.setMedName("medName");
        notification.setTime(LocalTime.now());

        Notification notification2 = new Notification();
        notification2.setCustomerId("customerId");
        notification2.setMedName("medName2");
        notification2.setTime(LocalTime.now());

        setData.add(notification);
        setData.add(notification2);

        //WHEN
        Set<NotificationModel> result = modelConverter.toNotificationModelSet(setData);

        //THEN
        NotificationModel notificationModel = modelConverter.toNotificationModel(notification);
        for(int i = 0; i<result.size(); i++) {
            assertTrue(result.contains(notificationModel));
        }

    }
}