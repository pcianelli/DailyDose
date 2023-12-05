package com.nashss.se.dailydose.activity;

import com.nashss.se.dailydose.activity.requests.RemoveMedicationRequest;
import com.nashss.se.dailydose.activity.results.RemoveMedicationResult;
import com.nashss.se.dailydose.dynamodb.MedicationDao;
import com.nashss.se.dailydose.dynamodb.models.Medication;
import com.nashss.se.dailydose.exceptions.InvalidAttributeValueException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class RemoveMedicationActivityTest {
    @Mock
    private MedicationDao medicationDao;
    private RemoveMedicationActivity removeMedicationActivity;

    @BeforeEach
    void setUp() {
        openMocks(this);
        removeMedicationActivity = new RemoveMedicationActivity(medicationDao);
    }

    @Test
    public void handleRequest_withValidCustomerIdAndMedName_removesAndReturnsMedication() {
        //GIVEN

        String customerId = "customerId";
        String medName = "medName";
        Medication medication = new Medication();
        medication.setCustomerId(customerId);
        medication.setMedName(medName);

        RemoveMedicationRequest request = RemoveMedicationRequest.builder()
                .withCustomerId(customerId)
                .withMedName(medName)
                .build();

        when(medicationDao.getOneMedication(customerId, medName)).thenReturn(medication);

        //WHEN
        RemoveMedicationResult result = removeMedicationActivity.handleRequest(request);

        //THEN
        verify(medicationDao).removeMedication(any(Medication.class));

        assertEquals(customerId, result.getMedicationModel().getCustomerId());
        assertEquals(medName, result.getMedicationModel().getMedName());
        assertNotNull(result.getMedicationModel().getNotificationTimes());
    }

    @Test
    public void handleRequest_invalidName_throwsInvalidAttributeValueException() {
        //GIVEN
        String customerId = "customerId";
        String medName = "medName??";

        RemoveMedicationRequest request = RemoveMedicationRequest.builder()
                .withCustomerId(customerId)
                .withMedName(medName)
                .build();

        //WHEN AND THEN
        assertThrows(InvalidAttributeValueException.class, () -> removeMedicationActivity.handleRequest(request));
    }

    @Test
    public void handleRequest_MedNameIsBlank_throwsIllegalArgumentException() {
        //GIVEN
        String customerId = "customerId";
        String medName = "";

        RemoveMedicationRequest request = RemoveMedicationRequest.builder()
                .withCustomerId(customerId)
                .withMedName(medName)
                .build();

        //WHEN AND THEN
        assertThrows(IllegalArgumentException.class, () -> removeMedicationActivity.handleRequest(request));
    }
}