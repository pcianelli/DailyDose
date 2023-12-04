package com.nashss.se.dailydose.activity;

import com.nashss.se.dailydose.activity.requests.AddNotificationRequest;
import com.nashss.se.dailydose.activity.results.AddNotificationResult;
import com.nashss.se.dailydose.converters.LocalTimeConverter;
import com.nashss.se.dailydose.dynamodb.NotificationDao;
import com.nashss.se.dailydose.dynamodb.models.Notification;
import com.nashss.se.dailydose.exceptions.InvalidAttributeValueException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class AddNotificationActivityTest {
    @Mock
    private NotificationDao notificationDao;

    private AddNotificationActivity addNotificationActivity;

    @BeforeEach
    void setup() {
        openMocks(this);
        addNotificationActivity = new AddNotificationActivity(notificationDao);
    }

    @Test
    public void handleRequest_withValidRequest_createsAndSavesNotification() {
        //GIVEN
        LocalTimeConverter timeConverter = new LocalTimeConverter();
        String customerId = "customerId";
        String medName = "medName";
        String time = "08:30:00";

        AddNotificationRequest request = AddNotificationRequest.builder()
                .withCustomerId(customerId)
                .withMedName(medName)
                .withTime(time)
                .build();

        Notification notification = new Notification();
        notification.setCustomerId(customerId);
        notification.setMedName(medName);
        notification.setTime(timeConverter.unconvert(time));
        notification.setNotificationId("generatedId");

        when(notificationDao.addNotification(any(Notification.class))).thenReturn(notification);

        //WHEN
        AddNotificationResult result = addNotificationActivity.handleRequest(request);

        //THEN
        verify(notificationDao).addNotification(any(Notification.class));

        assertEquals(customerId, result.getNotificationModel().getCustomerId());
        assertEquals(medName, result.getNotificationModel().getMedName());
        assertEquals(time, result.getNotificationModel().getTime());

        assertNotNull(result.getNotificationModel());
        assertNotNull(result.getNotificationModel().getNotificationId());
    }

    @Test
    public void handleRequest_MedNameIsBlank_throwsIllegalArgumentException() {
        //GIVEN
        String customerId = "customerId";
        String medName = "";
        String time = "08:30:00";

        AddNotificationRequest request = AddNotificationRequest.builder()
                .withCustomerId(customerId)
                .withMedName(medName)
                .withTime(time)
                .build();

        //THEN AND WHEN
        assertThrows(IllegalArgumentException.class, () -> addNotificationActivity.handleRequest(request));

    }

    @Test
    public void handleRequest_invalidName_throwsIllegalArgumentException() {
        //GIVEN
        String customerId = "customerId";
        String medName = "medName??@@";
        String time = "08:30:00";

        AddNotificationRequest request = AddNotificationRequest.builder()
                .withCustomerId(customerId)
                .withMedName(medName)
                .withTime(time)
                .build();


        //THEN AND WHEN
        assertThrows(InvalidAttributeValueException.class, () -> addNotificationActivity.handleRequest(request));
    }

    @Test
    public void handleRequest_timeIsBlank_throwsIllegalArgumentException() {
        //GIVEN
        String customerId = "customerId";
        String medName = "medName";
        String time = "";

        AddNotificationRequest request = AddNotificationRequest.builder()
                .withCustomerId(customerId)
                .withMedName(medName)
                .withTime(time)
                .build();

        //THEN AND WHEN
        assertThrows(IllegalArgumentException.class, () -> addNotificationActivity.handleRequest(request));

    }

}