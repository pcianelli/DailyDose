package com.nashss.se.dailydose.converters;

import com.nashss.se.dailydose.dynamodb.models.Medication;
import com.nashss.se.dailydose.dynamodb.models.Notification;
import com.nashss.se.dailydose.models.MedicationModel;
import com.nashss.se.dailydose.models.NotificationModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ModelConverter {
    private LocalTimeConverter converter = new LocalTimeConverter();



    /**
     * Converts a provided Medication into a MedicationModel representation.
     *
     * @param medication the Medication to convert to MedicationModel
     * @return the converted medicationModel with fields mapped from medication
     */
    public MedicationModel toMedicationModel(Medication medication, Set<NotificationModel> notificationModelSet) {
        return MedicationModel.builder()
                .withCustomerId(medication.getCustomerId())
                .withMedName(medication.getMedName())
                .withMedInfo(medication.getMedInfo())
                .withNotifications(notificationModelSet)
                .build();
    }

    /**
     * Converts a List of Medication into a list of MedicationModels.
     *
     * @param medications the Medication to convert to MedicationModelList
     * @return the converted medicationModelList with fields mapped from medication
     */
    public List<MedicationModel> toMedicationModelList(List<Medication> medications, Set<NotificationModel> notificationModelSet) {
        List<MedicationModel> medicationModels = new ArrayList<>();
        medications.forEach(medication -> medicationModels.add(toMedicationModel(medication, notificationModelSet)));
        return medicationModels;
    }


    /**
     * Converts a set of Notifications into a set of NotificationModels.
     *
     * @param notifications the Notifications to convert to NotificationModels
     * @return the converted notificationModelSet with fields mapped from notification
     */
    public Set<NotificationModel> toNotificationModelSet(Set<Notification> notifications) {
        Set<NotificationModel> notificationModel = new HashSet<>();
        notifications.forEach(notification -> notificationModel.add(toNotificationModel(notification)));
        return notificationModel;
    }

    /**
     * Converts a provided Notification into a NotificationModel representation.
     *
     * @param notification the Notification to convert to NotificationModel
     * @return the converted NotificationModel with fields mapped from Notification
     */
    public NotificationModel toNotificationModel (Notification notification) {
        return NotificationModel.builder()
                .withCustomerId(notification.getCustomerId())
                .withMedName(notification.getMedName())
                .withTime(converter.convert(notification.getTime()))
                .build();
    }
}
