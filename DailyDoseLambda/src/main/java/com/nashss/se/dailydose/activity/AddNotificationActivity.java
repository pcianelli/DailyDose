package com.nashss.se.dailydose.activity;

import com.nashss.se.dailydose.activity.requests.AddNotificationRequest;
import com.nashss.se.dailydose.activity.results.AddNotificationResult;
import com.nashss.se.dailydose.converters.LocalTimeConverter;
import com.nashss.se.dailydose.converters.ModelConverter;
import com.nashss.se.dailydose.dynamodb.NotificationDao;
import com.nashss.se.dailydose.dynamodb.models.Notification;
import com.nashss.se.dailydose.models.NotificationModel;
import com.nashss.se.dailydose.utils.IdUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

/**
 * Implementation of the AddNotificationActivity for the DailyDose's AddNotificationActivity API.
 * This API allows the user to add a notification to their healthChart.
 */
public class AddNotificationActivity {
    private final Logger log = LogManager.getLogger();
    private final NotificationDao notificationDao;
    private final LocalTimeConverter timeConverter;

    /**
     * Instantiates a new AddNotificationActivity object.
     *
     * @param notificationDao  to access the notification table.
     */
    @Inject
    public AddNotificationActivity(NotificationDao notificationDao) {
        this.notificationDao = notificationDao;
        timeConverter = new LocalTimeConverter();
    }

    /**
     * This method handles the incoming request by adding a notification to the database.
     * It then returns a AddNotificationResult.
     * <p>
     * If the notification already exists, appropriate message should be returned.
     * If the medName is blank, appropriate message should be returned.
     * If medInfo is blank or null, should return blank string
     *
     * @param addNotificationRequest request object to add a notification
     * @return addNotificationResult result object indicating the outcome of the creation process
     */
    public AddNotificationResult handleRequest(final AddNotificationRequest addNotificationRequest) {
        log.info("Received AddNotificationRequest {}", addNotificationRequest);

        String medName = addNotificationRequest.getMedName();
        String time = addNotificationRequest.getTime();

        // Check for invalid characters in the name
      IdUtils.validateMedicationName(medName);
      IdUtils.validMedNameNotBlank(medName);
      IdUtils.validTime(time);

        Notification notification = new Notification();
        notification.setCustomerId(addNotificationRequest.getCustomerId());
        notification.setNotificationId(IdUtils.generateNotificationId());
        notification.setMedName(addNotificationRequest.getMedName());
        notification.setTime(timeConverter.unconvert(addNotificationRequest.getTime()));

        Notification result = notificationDao.addNotification(notification);

        ModelConverter modelConverter = new ModelConverter();
        NotificationModel notificationModel = modelConverter.toNotificationModel(result);

        return AddNotificationResult.builder()
                .withNotificationModel(notificationModel)
                .build();
    }
}
