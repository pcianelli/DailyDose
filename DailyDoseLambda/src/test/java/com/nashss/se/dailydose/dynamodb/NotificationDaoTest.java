package com.nashss.se.dailydose.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.nashss.se.dailydose.dynamodb.models.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class NotificationDaoTest {

    @Mock
    private DynamoDBMapper dynamoDBMapper;
    @Mock
    private PaginatedQueryList<Notification> queryResult;
    @InjectMocks
    private NotificationDao notificationDao;
    private List<Notification> notificationList;

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
        queryResult = new PaginatedQueryList<>();

        ArgumentCaptor<DynamoDBQueryExpression<Notification>> captor = ArgumentCaptor.forClass(DynamoDBQueryExpression.class);
        when(dynamoDBMapper.query(eq(Notification.class), any(DynamoDBQueryExpression.class))).thenReturn(queryResult);

        //WHEN
        Set<Notification> result = notificationDao.getNotifications(customerId, medName1);

        //THEN
        assertEquals(emptySet, result, "Should return an empty Set");
        verify(dynamoDBMapper, times(1)).queryPage(eq(Notification.class), captor.capture());
    }

    @Test
    void getNotifications_withNotificationsOnTheTable_ReturnsSetOfNotifications() {
    }

    @Test
    void getTimeNotifications_withNoNotificationsOnTheTable_ReturnsEmptyList() {
    }

    @Test
    void getTimeNotifications_withNotificationsOnTheTable_ReturnsListOfNotifications() {
    }
}