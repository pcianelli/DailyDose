package com.nashss.se.dailydose.dynamodb;

import com.nashss.se.dailydose.dynamodb.models.Medication;
import com.nashss.se.dailydose.metrics.MetricsConstants;
import com.nashss.se.dailydose.metrics.MetricsPublisher;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Accesses data for aa medication using {@link Medication} to represent the model in DynamoDB.
 */
@Singleton
public class MedicationDao {
    private static final int PAGINATED_LIMIT = 5;
    private final DynamoDBMapper dynamoDbMapper;
    private final MetricsPublisher metricsPublisher;


    /**
     * Instantiates a MedicationDao object.
     *
     * @param dynamoDbMapper   the {@link DynamoDBMapper} used to interact with the medications table
     * @param metricsPublisher the {@link MetricsPublisher} used to record metrics.
     */
    @Inject
    public MedicationDao(DynamoDBMapper dynamoDbMapper, MetricsPublisher metricsPublisher) {
        this.dynamoDbMapper = dynamoDbMapper;
        this.metricsPublisher = metricsPublisher;
    }

    /**
     * Perform a search (via a "query") of the medications table for medications matching the given medication criteria.
     *
     * @param customerId medications containing search criteria.
     * @param exclusiveStartMedName start key for pagination
     * @return a List of Medication objects that match the search criteria.
     */
    public List<Medication> getMedications(String customerId, String exclusiveStartMedName) {

        Medication medication = new Medication();
        medication.setCustomerId(customerId);

        Map<String, AttributeValue> exclusiveStartKey = null;
        if (exclusiveStartMedName != null) {
            exclusiveStartKey = new HashMap<>();
            exclusiveStartKey.put("customerId", new AttributeValue().withS(customerId));
            exclusiveStartKey.put("medName", new AttributeValue().withS(exclusiveStartMedName));
        }

        DynamoDBQueryExpression<Medication> queryExpression = new DynamoDBQueryExpression<Medication>()
                .withHashKeyValues(medication)
                .withLimit(PAGINATED_LIMIT)
                .withExclusiveStartKey(exclusiveStartKey);

        QueryResultPage<Medication> medicationQueryResults = dynamoDbMapper
                .queryPage(Medication.class, queryExpression);

        if (medicationQueryResults.getResults().isEmpty()) {
            metricsPublisher.addCount(MetricsConstants.GETMEDICATIONS_MEDICATIONNOTFOUND_COUNT, 1);
            return Collections.emptyList();
        }
        metricsPublisher.addCount(MetricsConstants.GETMEDICATIONS_MEDICATIONNOTFOUND_COUNT, 0);

        return medicationQueryResults.getResults();
    }
}
