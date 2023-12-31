package com.nashss.se.dailydose.dynamodb;

import com.nashss.se.dailydose.converters.LocalTimeConverter;
import com.nashss.se.dailydose.dynamodb.models.Notification;
import com.nashss.se.dailydose.exceptions.NotificationNotFoundException;
import com.nashss.se.dailydose.metrics.MetricsConstants;
import com.nashss.se.dailydose.metrics.MetricsPublisher;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Accesses data for aa medication using {@link Notification} to represent the model in DynamoDB.
 */
@Singleton
public class NotificationDao {
    private final DynamoDBMapper dynamoDbMapper;
    private final MetricsPublisher metricsPublisher;
    private final LocalTimeConverter converter;
    private final Logger log = LogManager.getLogger();

    /**
     * Instantiates a NotificationDao object.
     *
     * @param dynamoDbMapper   the {@link DynamoDBMapper} used to interact with the medications table
     * @param metricsPublisher the {@link MetricsPublisher} used to record metrics.
     */
    @Inject
    public NotificationDao(DynamoDBMapper dynamoDbMapper, MetricsPublisher metricsPublisher) {
        this.dynamoDbMapper = dynamoDbMapper;
        this.metricsPublisher = metricsPublisher;
        converter = new LocalTimeConverter();
    }

    /**
     * Perform a search (via a "query") of the notifications table for
     * notifications matching the given customerId and medName criteria.
     *
     * @param customerId customerId search criteria.
     * @param medName the medication name.
     * @return a Set of Notification objects that match the search criteria.
     */
    public Set<Notification> getNotifications(String customerId, String medName) {

        if (medName == null) {
            throw new NotificationNotFoundException("Medication Name cannot be null");
        }

        Map<String, AttributeValue> valueMap = new HashMap<>();
        valueMap.put(":customerId", new AttributeValue().withS(customerId));
        valueMap.put(":medName", new AttributeValue().withS(medName));

        DynamoDBQueryExpression<Notification> queryExpression = new DynamoDBQueryExpression<Notification>()
                .withIndexName("MedNameIndex")
                .withConsistentRead(false)
                .withKeyConditionExpression("customerId = :customerId AND medName = :medName")
                .withExpressionAttributeValues(valueMap);

        PaginatedQueryList<Notification> notificationsList = dynamoDbMapper.query(Notification.class, queryExpression);
        if (notificationsList.isEmpty()) {
            metricsPublisher.addCount(MetricsConstants.GETNOTIFICATIONS_NOTIFATIONSNOTFOUND_COUNT, 1);
            return new HashSet<>();
        } else {
            metricsPublisher.addCount(MetricsConstants.GETNOTIFICATIONS_NOTIFATIONSNOTFOUND_COUNT, 0);
            return new HashSet<>(notificationsList);
        }
    }

    /**
     * Perform a search (via a "query") of the notifications table for a
     * notification matching the given customerId, medName criteria and time.
     *
     * @param customerId customerId search criteria.
     * @param medName the medication name search criteria
     * @param time the time search criteria.
     * @return a Notification object that match the search criteria with
     * proper notificationId contained in the object.
     */
    public Notification getOneNotification(String customerId, String medName, String time) {
        if (medName == null) {
            throw new NotificationNotFoundException("Medication Name cannot be null");
        }
        if (time == null) {
            throw new NotificationNotFoundException("Time cannot be null");
        }

        Map<String, AttributeValue> valueMap = new HashMap<>();
        valueMap.put(":customerId", new AttributeValue().withS(customerId));
        valueMap.put(":medName", new AttributeValue().withS(medName));
        valueMap.put(":time", new AttributeValue().withS(time));

        DynamoDBQueryExpression<Notification> queryExpression = new DynamoDBQueryExpression<Notification>()
                .withIndexName("MedNameIndex")
                .withConsistentRead(false)
                .withKeyConditionExpression("customerId = :customerId AND medName = :medName")
                .withFilterExpression("#time = :time")
                .withExpressionAttributeNames(Collections.singletonMap("#time", "time"))
                .withExpressionAttributeValues(valueMap);

        PaginatedQueryList<Notification> resultNotifications =
                dynamoDbMapper.query(Notification.class, queryExpression);

        if (resultNotifications.size() > 0) {
            metricsPublisher.addCount(MetricsConstants.GETNOTIFICATIONS_NOTIFATIONSNOTFOUND_COUNT, 0);
            return resultNotifications.get(0);
        } else {
            metricsPublisher.addCount(MetricsConstants.GETNOTIFICATIONS_NOTIFATIONSNOTFOUND_COUNT, 1);
            throw new NotificationNotFoundException("No matching notification found");
        }
    }

    /**
     * Perform a search (via a "query") of the TimeIndex GSI table for
     * notifications matching the given customerId and time criteria.
     *
     * @param customerId customerId search criteria.
     * @param time the time of the notification that is set.
     * @return a List of Notification objects that match the search criteria.
     */
    public List<Notification> getTimeNotifications(String customerId, String time) {

        if (time == null) {
            throw new NotificationNotFoundException("Time cannot be null");
        }

        LocalTime fifteenMinutesBefore = converter.unconvert(time).minusMinutes(15);
        LocalTime fifteenMinutesAfter = converter.unconvert(time).plusMinutes(15);

        Map<String, AttributeValue> valueMap = new HashMap<>();
        valueMap.put(":customerId", new AttributeValue().withS(customerId));
        valueMap.put(":startTime", new AttributeValue().withS(converter.convert(fifteenMinutesBefore)));
        valueMap.put(":endTime", new AttributeValue().withS(converter.convert(fifteenMinutesAfter)));

        Map<String, String> expressionAttributeNames = new HashMap<>();
        expressionAttributeNames.put("#customerAttr", "customerId");
        expressionAttributeNames.put("#timeAttr", "time");

        DynamoDBQueryExpression<Notification> queryExpression = new DynamoDBQueryExpression<Notification>()
                .withIndexName("TimeIndex")
                .withConsistentRead(false)
                .withKeyConditionExpression("#customerAttr = :customerId and #timeAttr between :startTime and :endTime")
                .withExpressionAttributeValues(valueMap)
                .withExpressionAttributeNames(expressionAttributeNames);


        PaginatedQueryList<Notification> paginatedQueryList = dynamoDbMapper.query(Notification.class, queryExpression);

        if (paginatedQueryList.isEmpty()) {
            metricsPublisher.addCount(MetricsConstants.GETNOTIFICATIONS_NOTIFATIONSNOTFOUND_COUNT, 1);
            return new ArrayList<>();
        } else {
            metricsPublisher.addCount(MetricsConstants.GETNOTIFICATIONS_NOTIFATIONSNOTFOUND_COUNT, 0);
            return paginatedQueryList;
        }
    }

    /**
     * adds a notification to the notifications table.
     *
     * @param notification notification criteria to add.
     * @return a Notification object that you added.
     */
    public Notification addNotification(Notification notification) {
        if (notification == null) {
            metricsPublisher.addCount(MetricsConstants.ADDNOTIFICATION_FAIL_COUNT, 1);
            throw new IllegalArgumentException("notification cannot be null");
        }

        dynamoDbMapper.save(notification);
        metricsPublisher.addCount(MetricsConstants.ADDNOTIFICATION_SUCCESS_COUNT, 1);

        return notification;
    }

    /**
     * deletes a notification from the notifications table.
     *
     * @param notification notification criteria to add.
     * @return a Notification object that you deleted.
     */
    public Notification removeNotification(Notification notification) {
        if (notification == null) {
            metricsPublisher.addCount(MetricsConstants.REMOVENOTIFICATION_FAIL_COUNT, 1);
            throw new IllegalArgumentException("notification cannot be null");
        }

        dynamoDbMapper.delete(notification);
        metricsPublisher.addCount(MetricsConstants.REMOVENOTIFICATION_SUCCESS_COUNT, 1);

        return notification;
    }
}
