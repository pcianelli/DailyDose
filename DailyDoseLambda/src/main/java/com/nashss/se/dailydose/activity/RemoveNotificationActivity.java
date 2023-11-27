package com.nashss.se.dailydose.activity;

import com.nashss.se.dailydose.activity.requests.RemoveNotificationRequest;
import com.nashss.se.dailydose.activity.results.RemoveNotificationResult;
import com.nashss.se.dailydose.converters.ModelConverter;
import com.nashss.se.dailydose.dynamodb.NotificationDao;
import com.nashss.se.dailydose.dynamodb.models.Notification;
import com.nashss.se.dailydose.exceptions.InvalidAttributeValueException;
import com.nashss.se.dailydose.models.NotificationModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

/**
 * Implementation of the RemoveNotificationActivity for the DailyDose's RemoveNotificationActivity API.
 * This API allows the user to remove a notification from their healthChart.
 */
public class RemoveNotificationActivity {
    private final Logger log = LogManager.getLogger();
    private final NotificationDao notificationDao;

    /**
     * Instantiates a new RemoveNotificationActivity object.
     *
     * @param notificationDao  to access the notifications table.
     */
    @Inject
    public RemoveNotificationActivity(NotificationDao notificationDao) {
        this.notificationDao = notificationDao;
    }

    /**
     * This method handles the incoming request by removing a notification from the database.
     * It then returns a RemoveNotificationResult.
     * <p>
     * If the notification does not exist, appropriate message should be returned.
     *
     * @param removeNotificationRequest request object to remove a notification
     * @return removeNotificationResult result object indicating the outcome of the removal process
     */
    public RemoveNotificationResult handleRequest(final RemoveNotificationRequest removeNotificationRequest) {
        log.info("Received RemoveNotificationRequest {}", removeNotificationRequest);

        String medName = removeNotificationRequest.getMedName();
        String time = removeNotificationRequest.getTime();
        // Check for invalid characters in the name
        if (!medName.matches("[a-zA-Z0-9 ]*")) {
            throw new InvalidAttributeValueException("Invalid characters in the medication name.");
        }
        if (medName.equals("")) {
            throw new IllegalArgumentException("MedName cannot be null or blank");
        }
        if (time.equals("") || time == null) {
            throw new IllegalArgumentException("time cannot be null or blank");
        }

        Notification notification = notificationDao.getOneNotification(removeNotificationRequest.getCustomerId(),
                removeNotificationRequest.getMedName(), removeNotificationRequest.getTime());

        Notification result = notificationDao.removeNotification(notification);

        ModelConverter modelConverter = new ModelConverter();
        NotificationModel notificationModel = modelConverter.toNotificationModel(result);

        return RemoveNotificationResult.builder()
                .withNotificationModel(notificationModel)
                .build();
    }
}
