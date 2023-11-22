package com.nashss.se.dailydose.activity;

import com.nashss.se.dailydose.activity.requests.GetNotificationsRequest;
import com.nashss.se.dailydose.activity.results.GetNotificationsResult;
import com.nashss.se.dailydose.converters.ModelConverter;
import com.nashss.se.dailydose.dynamodb.NotificationDao;
import com.nashss.se.dailydose.dynamodb.models.Notification;
import com.nashss.se.dailydose.models.NotificationModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the GetNotificationsActivity for the DailyDose GetNotifications API.
 * <p>
 * This API allows the customer to get the list Notifications they when the alarm is set.
 */
public class GetNotificationsActivity {
    private final Logger log = LogManager.getLogger();
    private final NotificationDao notificationDao;
    private final ModelConverter modelConverter;

    /**
     * Instantiates a new GetNotificationsActivity object.
     *
     * @param notificationDao notificationDao to access the notification table.
     */
    @Inject
    public GetNotificationsActivity(NotificationDao notificationDao) {
        this.notificationDao = notificationDao;
        modelConverter = new ModelConverter();
    }

    /**
     * This method handles the incoming request by retrieving all notifications for a time from the database
     * and a set of Notifications from the notification table.
     * <p>
     * It then returns a List of NotificationModels.
     * <p>
     * If the notification does not exist, this should return an empty list.
     *
     * @param getNotificationsRequest request object
     * @return getNotificationsResult result object containing the list of notificationModels
     */
    public GetNotificationsResult handleRequest(final GetNotificationsRequest getNotificationsRequest) {
        log.info("Received GetNotificationsRequest {}", getNotificationsRequest);

        List<Notification> notificationList = notificationDao.getTimeNotifications(getNotificationsRequest.getCustomerId(), getNotificationsRequest.getTime());

        List<NotificationModel> notificationModelList = new ArrayList<>();

        for(Notification notification: notificationList) {
            notificationModelList.add(modelConverter.toNotificationModel(notification));
        }

        return GetNotificationsResult.builder()
                .withNotificationModelList(notificationModelList)
                .build();
    }
}