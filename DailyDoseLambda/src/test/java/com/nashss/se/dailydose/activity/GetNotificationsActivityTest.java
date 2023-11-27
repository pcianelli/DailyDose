package com.nashss.se.dailydose.activity;


import com.nashss.se.dailydose.activity.requests.GetNotificationsRequest;
import com.nashss.se.dailydose.activity.results.GetNotificationsResult;
import com.nashss.se.dailydose.converters.LocalTimeConverter;
import com.nashss.se.dailydose.converters.ModelConverter;
import com.nashss.se.dailydose.dynamodb.NotificationDao;
import com.nashss.se.dailydose.dynamodb.models.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class GetNotificationsActivityTest {
    @Mock
    private NotificationDao notificationDao;

    @InjectMocks
    GetNotificationsActivity activity;

    @BeforeEach
    void setup() {
        openMocks(this);
    }

    @Test
    public void handleRequest_withCustomerIdAndTime_ReturnsListOfNotificationModel() {
        //GIVEN
        String customerId = "1111";
        LocalTimeConverter converter = new LocalTimeConverter();
        LocalTime time = LocalTime.parse("12:00:00");
        String time1 = converter.convert(time);

        Notification notification = new Notification();
        notification.setCustomerId(customerId);
        notification.setNotificationId("2222");
        notification.setMedName("medName1");
        notification.setTime(time);

        Notification notification2 = new Notification();
        notification2.setCustomerId(customerId);
        notification2.setNotificationId("3333");
        notification2.setMedName("medName2");
        notification2.setTime(time);

        List<Notification> notificationList = new ArrayList<>();
        notificationList.add(notification);
        notificationList.add(notification2);

        when(notificationDao.getTimeNotifications(customerId, time1)).thenReturn(notificationList);

        GetNotificationsRequest request = GetNotificationsRequest.builder()
                .withCustomerId(customerId)
                .withTime(time1)
                .build();

        //WHEN
        GetNotificationsResult result = activity.handleRequest(request);

        //THEN
        assertNotNull(result.getNotificationModelList().get(0));
        assertNotNull(result.getNotificationModelList().get(1));
        assertEquals("medName1", result.getNotificationModelList().get(0).getMedName());
        assertEquals(time1, result.getNotificationModelList().get(0).getTime());
        assertEquals("medName2", result.getNotificationModelList().get(1).getMedName());
        assertEquals(time1, result.getNotificationModelList().get(1).getTime());
    }
}