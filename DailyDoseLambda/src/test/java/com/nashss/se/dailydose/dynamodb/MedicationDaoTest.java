package com.nashss.se.dailydose.dynamodb;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.nashss.se.dailydose.dynamodb.models.Medication;
import com.nashss.se.dailydose.metrics.MetricsPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
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

    private List<Medication> medicationList;
    @InjectMocks
    private MedicationDao medicationDao;

    @BeforeEach
    public void setup() {
        initMocks(this);
    }

    @Test
    public void getMedications_withPopulatedMedicationsTable_returnsPaginatedListMedications () {
        //GIVEN
        String customerId = "1111";

        Medication medication1 = new Medication();
        Medication medication2 = new Medication();
        Medication medication3 = new Medication();
        Medication medication4 = new Medication();
        Medication medication5 = new Medication();

        medication1.setCustomerId(customerId);
        medication1.setMedName("med1Name");
        medication1.setMedInfo("med info");
        medication2.setCustomerId(customerId);
        medication2.setMedName("med2Name");
        medication2.setMedInfo("med info");
        medication3.setCustomerId(customerId);
        medication3.setMedName("med3Name");
        medication3.setMedInfo("med info");
        medication4.setCustomerId(customerId);
        medication4.setMedName("med4Name");
        medication4.setMedInfo("med info");
        medication5.setCustomerId(customerId);
        medication5.setMedName("med5Name");
        medication5.setMedInfo("med info");

        medicationList = new ArrayList<>();
        medicationList.add(medication1);
        medicationList.add(medication2);
        medicationList.add(medication3);
        medicationList.add(medication4);
        medicationList.add(medication5);

        int limit = 5;

        when(dynamoDBMapper.queryPage(eq(Medication.class), any(DynamoDBQueryExpression.class))).thenReturn(queryResult);
        when(queryResult.getResults()).thenReturn(medicationList);
        ArgumentCaptor<DynamoDBQueryExpression<Medication>> captor = ArgumentCaptor.forClass(DynamoDBQueryExpression.class);

        //WHEN
        List<Medication> result = medicationDao.getMedications(customerId);

        //THEN
        assertEquals(result, medicationList, "Expected list of medicationList to be what was returned by DynamoDB");
        verify(dynamoDBMapper).queryPage(eq(Medication.class), captor.capture());

        Medication queriedMedication = captor.getValue().getHashKeyValues();

        assertEquals(customerId, queriedMedication.getCustomerId(), "Expected query expression to query for partition key: " +
                customerId);
        assertEquals(5, result.size(), "Expected size of paginated list to be 5");
    }

    @Test
    public void getMedications_withNullOnMedicationsTable_returnsEmptyList () {
        // GIVEN
        QueryResultPage<Medication> queryResultPage = new QueryResultPage<>();
        queryResultPage.setResults(Collections.emptyList());

        String customerId = "1111";
        ArgumentCaptor<DynamoDBQueryExpression<Medication>> captor = ArgumentCaptor.forClass(DynamoDBQueryExpression.class);
        when(dynamoDBMapper.queryPage(eq(Medication.class), any(DynamoDBQueryExpression.class))).thenReturn(queryResultPage);

        // WHEN
        List<Medication> result = medicationDao.getMedications(customerId);

        // THEN
        assertEquals(Collections.emptyList(), result, "should return an emptyList");
        verify(dynamoDBMapper, times(1)).queryPage(eq(Medication.class), captor.capture());
    }

    @Test
    public void addMedication_withValidMedication_addsMedicationToTableReturnsSuccess() {
        //GIVEN
        Medication medication = new Medication();
        medication.setCustomerId("customerId");
        medication.setMedName("medName");
        medication.setMedInfo("medInfo");


        //WHEN
        Medication medicationrResult = medicationDao.addMedication(medication);

        //THEN
        verify(dynamoDBMapper).save(medication);
        assertEquals(medication, medicationrResult);
    }

    @Test
    public void addMedication_withNullMedication_returnsIllegalArgumentException() {
        //GIVEN
        Medication medication = null;

        //WHEN and THEN
        assertThrows(IllegalArgumentException.class, () -> medicationDao.addMedication(medication));
    }

    @Test
    public void removeMedication_withValidMedication_removesMedicationFromTableReturnsMedication() {
        //GIVEN
        Medication medication = new Medication();
        medication.setCustomerId("customerId");
        medication.setMedName("medName");
        medication.setMedInfo("medInfo");


        //WHEN
        Medication medicationrResult = medicationDao.removeMedication(medication);

        //THEN
        verify(dynamoDBMapper).delete(medication);
        assertEquals(medication, medicationrResult);
    }

    @Test
    public void removeMedication_withNullMedication_returnsIllegalArgumentException() {
        //GIVEN
        Medication medication = null;

        //WHEN and THEN
        assertThrows(IllegalArgumentException.class, () -> medicationDao.removeMedication(medication));
    }

}