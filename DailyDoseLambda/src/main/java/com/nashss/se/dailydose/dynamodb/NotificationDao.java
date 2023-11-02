package com.nashss.se.dailydose.dynamodb;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.nashss.se.dailydose.converters.LocalTimeConverter;
import com.nashss.se.dailydose.dynamodb.models.Notification;
import com.nashss.se.dailydose.exceptions.NotificationNotFoundException;
import com.nashss.se.dailydose.metrics.MetricsConstants;
import com.nashss.se.dailydose.metrics.MetricsPublisher;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Accesses data for aa medication using {@link Notification} to represent the model in DynamoDB.
 */
@Singleton
public class NotificationDao {
    private final DynamoDBMapper dynamoDbMapper;
    private final MetricsPublisher metricsPublisher;
    private final LocalTimeConverter converter;

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
     * Perform a search (via a "query") of the notifications table for notifications matching the given customerId and medName criteria.
     *
     * @param customerId customerId search criteria.
     * @param medName the medication name
     * @return a Set of Notification objects that match the search criteria.
     */
    public Set<Notification> getNotifications (String customerId, String medName) {

        if(medName == null) {
            throw new NotificationNotFoundException("Medication Name cannot be null");
        }

        Map<String, AttributeValue> valueMap = new HashMap<>();
        valueMap.put(":customerId", new AttributeValue().withS(customerId));
        valueMap.put(":medName", new AttributeValue().withS(medName));

        DynamoDBQueryExpression<Notification> queryExpression = new DynamoDBQueryExpression<Notification>()
                .withKeyConditionExpression("customerId = :customerId AND medName = :medName")
                .withExpressionAttributeValues(valueMap);

        PaginatedQueryList<Notification> notificationsList = dynamoDbMapper.query(Notification.class, queryExpression);
        if(notificationsList.isEmpty()) {
           metricsPublisher.addCount(MetricsConstants.GETNOTIFICATIONS_NOTIFATIONSNOTFOUND_COUNT, 1);
           return new HashSet<>();
        } else {
            metricsPublisher.addCount(MetricsConstants.GETNOTIFICATIONS_NOTIFATIONSNOTFOUND_COUNT, 0);
            return new HashSet<>(notificationsList);
        }
    }

    /**
     * Perform a search (via a "query") of the TimeIndex GSI table for notifications matching the given customerId and time criteria.
     *
     * @param customerId customerId search criteria.
     * @param time the time of the notification that is set.
     * @return a List of Notification objects that match the search criteria.
     */
    public List<Notification> getTimeNotifications (String customerId, String time) {

        LocalTime tenMinutesBefore = converter.unconvert(time).minusMinutes(15);
        LocalTime tenMinutesAfter = converter.unconvert(time).plusMinutes(15);

        Map<String, AttributeValue> valueMap = new HashMap<>();
        valueMap.put(":customerId", new AttributeValue().withS(customerId));
        valueMap.put(":startTime", new AttributeValue().withS(converter.convert(tenMinutesBefore)));
        valueMap.put(":endTime", new AttributeValue().withS(converter.convert(tenMinutesAfter)));

        DynamoDBQueryExpression<Notification> queryExpression = new DynamoDBQueryExpression<Notification>()
                .withIndexName("TimeIndex")
                .withConsistentRead(false)
                .withKeyConditionExpression("customerId = :customerId and time between :startTime and :endTime")
                .withExpressionAttributeValues(valueMap)
                .withExpressionAttributeNames(Collections.singletonMap("#time", "time"));

        return dynamoDbMapper.query(Notification.class, queryExpression);
    }
}
