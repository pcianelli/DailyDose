package com.nashss.se.dailydose.dynamodb;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.nashss.se.dailydose.dynamodb.models.Medication;
import com.nashss.se.dailydose.metrics.MetricsPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class MedicationDaoTest {
    @Mock
    private DynamoDBMapper dynamoDBMapper;
    @Mock
    private MetricsPublisher metricsPublisher;
    @Mock
    private QueryResultPage<Medication> queryResult;
    @Mock
    private List medicationList;
    @InjectMocks
    private MedicationDao medicationDao;

    @BeforeEach
    public void setup() {
        initMocks(this);
    }

    @Test
    public void getMedications_withPopulatedMedicationsTableAndExclusiveStartKeyNull_returnsPaginatedList5Medications () {
        //GIVEN
        String customerId = "1111";
        int limit = 5;

        when(dynamoDBMapper.queryPage(eq(Medication.class), any(DynamoDBQueryExpression.class))).thenReturn(queryResult);
        when(queryResult.getResults()).thenReturn(medicationList);
        ArgumentCaptor<DynamoDBQueryExpression<Medication>> captor = ArgumentCaptor.forClass(DynamoDBQueryExpression.class);

        //WHEN
        List<Medication> result = medicationDao.getMedications(customerId, null);

        //THEN
        assertEquals(result, medicationList, "Expected list of medicationList to be what was returned by DynamoDB");
        verify(dynamoDBMapper).queryPage(eq(Medication.class), captor.capture());

        Medication queriedMedication = captor.getValue().getHashKeyValues();
        Map<String, AttributeValue> queriedExclusiveStartKey = captor.getValue().getExclusiveStartKey();
        int queriedLimit = captor.getValue().getLimit();

        assertEquals(customerId, queriedMedication.getCustomerId(), "Expected query expression to query for partition key: " +
                customerId);
        assertNull(queriedExclusiveStartKey, "Expected query expression to not include an exclusive start key");
        assertEquals(limit, queriedLimit, "Expected query expression to query with limit " + limit);
    }

    @Test
    public void getMedications_exclusiveStartNotNull_returnsListWith5Medications () {

    }

    @Test
    public void getMedications_withNullOnMedicationsTable_returnsEmptyList () {


    }

}