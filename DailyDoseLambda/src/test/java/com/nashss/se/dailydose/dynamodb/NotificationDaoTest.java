package com.nashss.se.dailydose.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.nashss.se.dailydose.converters.LocalTimeConverter;
import com.nashss.se.dailydose.dynamodb.models.Notification;
import com.nashss.se.dailydose.exceptions.NotificationNotFoundException;
import com.nashss.se.dailydose.metrics.MetricsPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class  NotificationDaoTest {

    @Mock
    private DynamoDBMapper dynamoDBMapper;
    @Mock
    private MetricsPublisher metricsPublisher;
    @Mock
    private PaginatedQueryList<Notification> queryPaginatedResult;
    @InjectMocks
    private NotificationDao notificationDao;
    private List<Notification> notificationList;
    private LocalTimeConverter converter;

    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    @Test
    void getNotifications_withNoNotificationsOnTheTable_ReturnsEmptySet() {
        //GIVEN
        String customerId = "1111";
        String medName1 = "medName1";
        Set<Notification> emptySet = new HashSet<>();
        PaginatedQueryList<Notification> paginatedQueryList = Mockito.mock(PaginatedQueryList.class);
        Mockito.when(paginatedQueryList.isEmpty()).thenReturn(true);

        ArgumentCaptor<DynamoDBQueryExpression<Notification>> captor = ArgumentCaptor.forClass(DynamoDBQueryExpression.class);
        when(dynamoDBMapper.query(eq(Notification.class), any(DynamoDBQueryExpression.class))).thenReturn(paginatedQueryList);

        //WHEN
        Set<Notification> result = notificationDao.getNotifications(customerId, medName1);

        //THEN
        assertEquals(emptySet, result, "Should return an empty Set");
        verify(dynamoDBMapper, times(1)).query(eq(Notification.class), captor.capture());
    }

    @Test
    void getNotifications_withNotificationsOnTheTable_ReturnsSetOfNotifications() {
        //GIVEN
        String customerId = "1111";
        String medName1 = "medName1";
        Set<Notification> notificationSet = new HashSet<>();
        LocalTime currentTime = LocalTime.now();
        LocalTime futureTimeTen = currentTime.plusMinutes(10);
        LocalTime earlierTime10 = currentTime.minusMinutes(10);


        Notification notification1 = new Notification();
        Notification notification2 = new Notification();
        Notification notification3 = new Notification();

        notification1.setCustomerId(customerId);
        notification1.setMedName(medName1);
        notification1.setTime(currentTime);
        notification2.setCustomerId(customerId);
        notification2.setMedName(medName1);
        notification2.setTime(futureTimeTen);
        notification3.setCustomerId(customerId);
        notification3.setMedName(medName1);
        notification3.setTime(earlierTime10);

        notificationSet.add(notification1);
        notificationSet.add(notification2);
        notificationSet.add(notification3);

        when(queryPaginatedResult.isEmpty()).thenReturn(false);
        when(queryPaginatedResult.iterator()).thenReturn(notificationSet.iterator());

        ArgumentCaptor<DynamoDBQueryExpression<Notification>> captor = ArgumentCaptor.forClass(DynamoDBQueryExpression.class);
        when(dynamoDBMapper.query(eq(Notification.class), any(DynamoDBQueryExpression.class))).thenReturn(queryPaginatedResult);

        //WHEN
        Set<Notification> result = notificationDao.getNotifications(customerId, medName1);

        //THEN
        assertEquals(notificationSet, result, "Expected query to return 3 notifications");
        assertEquals(3, result.size(), "Expected size of paginated list to be 3");

    }

    @Test
    void getTimeNotifications_withNoNotificationsOnTheTable_ReturnsEmptyList() {
        //GIVEN
        String customerId = "1111";
        converter = new LocalTimeConverter();
        LocalTime currentTime = LocalTime.now();
        // Convert currentTime to a string using the converter
        String currentTimeStr = converter.convert(currentTime);
        notificationList = new ArrayList<>();

        PaginatedQueryList<Notification> paginatedQueryList = Mockito.mock(PaginatedQueryList.class);
        Mockito.when(paginatedQueryList.isEmpty()).thenReturn(true);

        ArgumentCaptor<DynamoDBQueryExpression<Notification>> captor = ArgumentCaptor.forClass(DynamoDBQueryExpression.class);
        when(dynamoDBMapper.query(eq(Notification.class), any(DynamoDBQueryExpression.class))).thenReturn(paginatedQueryList);

        //WHEN
        List<Notification> result = notificationDao.getTimeNotifications(customerId, currentTimeStr);

        //THEN
        assertEquals(notificationList, result, "Expect result to return an empty List");
        verify(dynamoDBMapper, times(1)).query(eq(Notification.class), captor.capture());

    }

    @Test
    void getTimeNotifications_withNotificationsOnTheTableInTimeRange_ReturnsListOfNotifications15MinutesBeforeAndAfter() {

        String customerId = "1111";
        String medName1 = "medName1";
        converter = new LocalTimeConverter();
        notificationList = new ArrayList<>();

        Notification notification1 = new Notification();
        Notification notification2 = new Notification();
        Notification notification3 = new Notification();

        LocalTime currentTime = LocalTime.now();
        String currentTimeStr = converter.convert(currentTime);
        LocalTime futureTimeTen = currentTime.plusMinutes(10);
        String currentTimeStr2 = converter.convert(futureTimeTen);
        LocalTime earlierTime10 = currentTime.minusMinutes(10);
        String currentTimeStr3 = converter.convert(earlierTime10);

        notification1.setCustomerId(customerId);
        notification1.setMedName(medName1);
        notification1.setTime(currentTime);
        notification2.setCustomerId(customerId);
        notification2.setMedName(medName1);
        notification2.setTime(futureTimeTen);
        notification3.setCustomerId(customerId);
        notification3.setMedName(medName1);
        notification3.setTime(earlierTime10);

        notificationList.add(notification1);
        notificationList.add(notification2);
        notificationList.add(notification3);


        ArgumentCaptor<DynamoDBQueryExpression<Notification>> captor = ArgumentCaptor.forClass(DynamoDBQueryExpression.class);
        when(dynamoDBMapper.query(eq(Notification.class), any(DynamoDBQueryExpression.class))).thenReturn(queryPaginatedResult);

        //WHEN
        List<Notification> result = notificationDao.getTimeNotifications(customerId, converter.convert(currentTime));

        //THEN
        verify(dynamoDBMapper, times(1)).query(eq(Notification.class), captor.capture());
    }

    @Test
    public void addNotification_withValidNotification_addsNotificationToTableReturnNotification() {
        //GIVEN
        converter = new LocalTimeConverter();
        Notification notification = new Notification();
        notification.setCustomerId("customerId");
        notification.setNotificationId("notificationId");
        notification.setMedName("medName");
        notification.setTime(converter.unconvert("08:30:00"));

        //WHEN
        Notification result = notificationDao.addNotification(notification);

        //THEN
        verify(dynamoDBMapper).save(notification);
        assertEquals(notification, result);
    }

    @Test
    public void addNotification_withNullNotification_returnsIllegalArgumentException() {
        //GIVEN
        Notification notification = null;

        //WHEN AND THEN
        assertThrows(IllegalArgumentException.class, () -> notificationDao.addNotification(notification));
    }

    @Test
    public void removeNotification_withValidNotification_removesNotificationFromTableReturnNotificaiton() {
        //GIVEN
        converter = new LocalTimeConverter();
        Notification notification = new Notification();
        notification.setCustomerId("customerId");
        notification.setNotificationId("notificationId");
        notification.setMedName("medName");
        notification.setTime(converter.unconvert("08:30:00"));

        //WHEN
        Notification result = notificationDao.removeNotification(notification);

        //THEN
        verify(dynamoDBMapper).delete(notification);
        assertEquals(notification, result);

    }

    @Test
    public void removeNotification_withNullNotification_returnsIllegalArgumentException() {
        //GIVEN
        Notification notification = null;

        //WHEN AND THEN
        assertThrows(IllegalArgumentException.class, () -> notificationDao.removeNotification(notification));
    }

    @Test
    public void getOneNotification_withPopulatedNotificationsGSI_returnsOneNotificationWithNotificationId() {
        // GIVEN
        String customerId = "1111";
        String medName = "medName";
        converter = new LocalTimeConverter();
        LocalTime time = LocalTime.now();
        String time1 = converter.convert(time);

        Notification notification = new Notification();
        notification.setCustomerId(customerId);
        notification.setMedName(medName);
        notification.setTime(time);

        Notification notification2 = new Notification();
        notification2.setCustomerId(customerId);
        notification2.setNotificationId("2222");
        notification2.setMedName(medName);
        notification2.setTime(time);

        Set<Notification> notificationSet = new HashSet<>();
        notificationSet.add(notification2);

        // Configure the mock to return the expected result
        when(queryPaginatedResult.size()).thenReturn(1);
        when(queryPaginatedResult.get(0)).thenReturn(notification2);
        when(dynamoDBMapper.query(eq(Notification.class), any(DynamoDBQueryExpression.class)))
                .thenReturn(queryPaginatedResult);

        // WHEN
        Notification result = notificationDao.getOneNotification(customerId, medName, time1);

        // THEN
        assertEquals(notification2, result, "Expected query to return 1 notification");
        assertNotNull(result.getNotificationId());
        verify(dynamoDBMapper, times(1)).query(eq(Notification.class), any(DynamoDBQueryExpression.class));
    }
    @Test
    public void getOneNotification_withNoNotificationsGSI_returnsNotificationNotFoundExceptionException() {
        // GIVEN
        String customerId = "1111";
        String medName = "medName";
        converter = new LocalTimeConverter();
        LocalTime time = LocalTime.now();
        String time1 = converter.convert(time);

        Set<Notification> notificationSet = new HashSet<>();

        // Configure the mock to return the expected result
        when(queryPaginatedResult.size()).thenReturn(0);
        when(queryPaginatedResult.get(0)).thenReturn(null);
        when(dynamoDBMapper.query(eq(Notification.class), any(DynamoDBQueryExpression.class)))
                .thenReturn(queryPaginatedResult);

        assertThrows(NotificationNotFoundException.class, () -> notificationDao.getOneNotification(customerId, medName, time1));
    }

    @Test
    public void getTimeNotifications_withPopulatedNotificationsOnTimeIndexGSI_ReturnsListOfNotifications() {
        // GIVEN
        String customerId = "1111";
        converter = new LocalTimeConverter();
        LocalTime time = LocalTime.parse("12:00:00");
        String time1 = converter.convert(time);

        Notification notification = new Notification();
        notification.setCustomerId(customerId);
        notification.setNotificationId("2222");
        notification.setMedName("MedName1");
        notification.setTime(time);

        Notification notification2 = new Notification();
        notification2.setCustomerId(customerId);
        notification2.setNotificationId("3333");
        notification2.setMedName("medName2");
        notification2.setTime(time);

        List<Notification> notificationList = new ArrayList<>();
        notificationList.add(notification);
        notificationList.add(notification2);

        when(queryPaginatedResult.iterator()).thenReturn(notificationList.iterator());

        when(dynamoDBMapper.query(eq(Notification.class), any(DynamoDBQueryExpression.class)))
                .thenReturn(queryPaginatedResult);

        //WHEN
        List<Notification> resultList = notificationDao.getTimeNotifications(customerId, time1);

        //THEN
        assertEquals(notificationList, resultList, "Expected query to return 2 notifications");
        verify(dynamoDBMapper, times(1)).query(eq(Notification.class), any(DynamoDBQueryExpression.class));
    }

    @Test
    public void getTimeNotifications_withNoNotificationsOnTheOnTimeIndexGSI_ReturnsEmptyList() {
        //GIVEN
        String customerId = "1111";
        converter = new LocalTimeConverter();
        LocalTime time = LocalTime.parse("12:00:00");
        String time1 = converter.convert(time);
        List<Notification> emptyList = new ArrayList<>();

        PaginatedQueryList<Notification> paginatedQueryList = Mockito.mock(PaginatedQueryList.class);
        Mockito.when(paginatedQueryList.isEmpty()).thenReturn(true);

        ArgumentCaptor<DynamoDBQueryExpression<Notification>> captor = ArgumentCaptor.forClass(DynamoDBQueryExpression.class);
        when(dynamoDBMapper.query(eq(Notification.class), any(DynamoDBQueryExpression.class))).thenReturn(paginatedQueryList);

        //WHEN
        List<Notification> result = notificationDao.getTimeNotifications(customerId, time1);

        //THEN
        assertEquals(emptyList, result, "Should return an empty List");
        verify(dynamoDBMapper, times(1)).query(eq(Notification.class), captor.capture());
    }

}