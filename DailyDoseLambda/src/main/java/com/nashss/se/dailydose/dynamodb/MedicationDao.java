package com.nashss.se.dailydose.dynamodb;

import com.nashss.se.dailydose.dynamodb.models.Medication;
import com.nashss.se.dailydose.exceptions.MedicationNotFoundException;
import com.nashss.se.dailydose.metrics.MetricsConstants;
import com.nashss.se.dailydose.metrics.MetricsPublisher;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Accesses data for aa medication using {@link Medication} to represent the model in DynamoDB.
 */
@Singleton
public class MedicationDao {
    private final DynamoDBMapper dynamoDbMapper;
    private final MetricsPublisher metricsPublisher;
    private final Logger log = LogManager.getLogger();


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
     * @return a List of Medication objects that match the search criteria.
     */
    public List<Medication> getMedications(String customerId) {

        Medication medication = new Medication();
        medication.setCustomerId(customerId);

        DynamoDBQueryExpression<Medication> queryExpression = new DynamoDBQueryExpression<Medication>()
                .withHashKeyValues(medication);

        QueryResultPage<Medication> medicationQueryResults = dynamoDbMapper
                .queryPage(Medication.class, queryExpression);

        if (medicationQueryResults.getResults().isEmpty()) {
            metricsPublisher.addCount(MetricsConstants.GETMEDICATIONS_MEDICATIONNOTFOUND_COUNT, 1);
            return Collections.emptyList();
        }

        metricsPublisher.addCount(MetricsConstants.GETMEDICATIONS_MEDICATIONNOTFOUND_COUNT, 0);
        return medicationQueryResults.getResults();
    }

    public Medication getOneMedication(String customerId, String medName) {

        Medication resultMedication = this.dynamoDbMapper.load(Medication.class, customerId, medName);

        if (resultMedication == null) {
            metricsPublisher.addCount(MetricsConstants.GETONEMEDICATION_SUCCESS_COUNT, 1);
            throw new MedicationNotFoundException("Could not find the medication " + medName);
        }

        else {
            metricsPublisher.addCount(MetricsConstants.GETONEMEDICATION_FAIL_COUNT, 1);
            return resultMedication;
        }
    }

    /**
     * Creates a medication in the database.
     * @param medication the medication object to be stored
     * @return the medication added;
     */
    public Medication addMedication(Medication medication) {
        if (medication == null) {
            throw new IllegalArgumentException("medication cannot be null");
        }
        try {
            dynamoDbMapper.save(medication);
            metricsPublisher.addCount(MetricsConstants.ADDMEDICATION_SUCCESS_COUNT, 1);
        } catch (Exception e) {
            log.error("Error creating medication to add", e);
            metricsPublisher.addCount(MetricsConstants.ADDMEDICATION_FAIL_COUNT, 1);
        }
        return medication;
    }

    /**
     * Removes a medication in the database.
     * @param medication the medication object to be removed
     * @return medication removed;
     */
    public Medication removeMedication(Medication medication) {
        if (medication == null) {
            throw new IllegalArgumentException("medication cannot be null");
        }
        try {
            dynamoDbMapper.delete(medication);
            metricsPublisher.addCount(MetricsConstants.REMOVEMEDICATION_SUCCESS_COUNT, 1);
        } catch (Exception e) {
            log.error("Error deleting medication to add", e);
            metricsPublisher.addCount(MetricsConstants.REMOVEMEDICATION_FAIL_COUNT, 1);
        }
        return medication;
    }
}
