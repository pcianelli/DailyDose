package com.nashss.se.dailydose.activity;

import com.nashss.se.dailydose.activity.requests.RemoveNotificationRequest;
import com.nashss.se.dailydose.activity.results.RemoveNotificationResult;
import com.nashss.se.dailydose.converters.LocalTimeConverter;
import com.nashss.se.dailydose.dynamodb.NotificationDao;
import com.nashss.se.dailydose.dynamodb.models.Notification;
import com.nashss.se.dailydose.exceptions.InvalidAttributeValueException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class RemoveNotificationActivityTest {
    @Mock
    private NotificationDao notificationDao;
    private RemoveNotificationActivity removeNotificationActivity;

    @BeforeEach
    void setup() {
        openMocks(this);
        removeNotificationActivity = new RemoveNotificationActivity(notificationDao);
    }

    @Test
    public void handleRequest_withValidCustomerIdMedNameAndTime_removesAndReturnsNotification() {
        //GIVEN
        String customerId = "customerId";
        String medName = "medName";
        LocalTimeConverter timeConverter = new LocalTimeConverter();
        String time = timeConverter.convert(LocalTime.now());

        Notification notification = new Notification();
        notification.setCustomerId(customerId);
        notification.setNotificationId("2222");
        notification.setMedName(medName);
        notification.setTime(LocalTime.now());

        RemoveNotificationRequest request = RemoveNotificationRequest.builder()
                .withCustomerId(customerId)
                .withMedName(medName)
                .withTime(time)
                .build();

        when(notificationDao.getOneNotification(customerId, medName, time)).thenReturn(notification);
        when(notificationDao.removeNotification(notification)).thenReturn(notification);

        //WHEN
        RemoveNotificationResult result = removeNotificationActivity.handleRequest(request);

        //THEN
        verify(notificationDao).removeNotification(any(Notification.class));
        assertNotNull(result.getNotificationModel().getNotificationId());
        assertEquals(customerId, result.getNotificationModel().getCustomerId());
        assertEquals(medName, result.getNotificationModel().getMedName());
        assertEquals(time, result.getNotificationModel().getTime());

    }

    @Test
    public void handleRequest_withInvalidMedName_throwsInvalidAttributeValueException() {
        //GIVEN
        String customerId = "customerId";
        String medName = "medName??";
        LocalTimeConverter timeConverter = new LocalTimeConverter();
        String time = timeConverter.convert(LocalTime.now());

        RemoveNotificationRequest request = RemoveNotificationRequest.builder()
                .withCustomerId(customerId)
                .withMedName(medName)
                .withTime(time)
                .build();

        assertThrows(InvalidAttributeValueException.class, () -> removeNotificationActivity.handleRequest(request));
    }

    @Test
    public void handleRequest_withBlankMedName_throwsIllegalArgumentException() {
        //GIVEN
        String customerId = "customerId";
        String medName = "";
        LocalTimeConverter timeConverter = new LocalTimeConverter();
        String time = timeConverter.convert(LocalTime.now());

        RemoveNotificationRequest request = RemoveNotificationRequest.builder()
                .withCustomerId(customerId)
                .withMedName(medName)
                .withTime(time)
                .build();

        assertThrows(IllegalArgumentException.class, () -> removeNotificationActivity.handleRequest(request));
    }

    @Test
    public void handleRequest_withBlankTime_throwsIllegalArgumentException() {
        //GIVEN
        String customerId = "customerId";
        String medName = "medName";
        String time = "";

        RemoveNotificationRequest request = RemoveNotificationRequest.builder()
                .withCustomerId(customerId)
                .withMedName(medName)
                .withTime(time)
                .build();

        assertThrows(IllegalArgumentException.class, () -> removeNotificationActivity.handleRequest(request));
    }
}