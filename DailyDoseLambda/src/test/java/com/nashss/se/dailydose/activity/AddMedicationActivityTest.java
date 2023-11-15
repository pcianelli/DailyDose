package com.nashss.se.dailydose.activity;

import com.nashss.se.dailydose.activity.requests.AddMedicationRequest;
import com.nashss.se.dailydose.activity.results.AddMedicationResult;
import com.nashss.se.dailydose.dynamodb.MedicationDao;
import com.nashss.se.dailydose.dynamodb.models.Medication;
import com.nashss.se.dailydose.exceptions.InvalidAttributeValueException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

class AddMedicationActivityTest {
    @Mock
    private MedicationDao medicationDao;

    private AddMedicationActivity addMedicationActivity;

    @BeforeEach
    void setUp() {
        openMocks(this);
        addMedicationActivity = new AddMedicationActivity(medicationDao);
    }

    @Test
    public void handleRequest_withValidCustomerIdMedNameAndMedInfo_createsAndSavesMedication() {
        //GIVEN
        String customerId = "customerId";
        String medName = "medName";
        String medInfo = "medInfo";

        AddMedicationRequest request = AddMedicationRequest.builder()
                .withCustomerId(customerId)
                .withMedName(medName)
                .withMedInfo(medInfo)
                .build();

        //WHEN
        AddMedicationResult result = addMedicationActivity.handleRequest(request);

        //THEN
        verify(medicationDao).addMedication(any(Medication.class));

        assertEquals(customerId, result.getMedicationModel().getCustomerId());
        assertEquals(medName, result.getMedicationModel().getMedName());
        assertEquals(medInfo, result.getMedicationModel().getMedInfo());
        assertNotNull(result.getMedicationModel().getNotificationTimes());
    }

    @Test
    public void handleRequest_withValidCustomerIdMedNameAndMedInfoIsNull_createsAndSavesMedication() {
        //GIVEN
        String customerId = "customerId";
        String medName = "medName";
        String medInfo = null;

        AddMedicationRequest request = AddMedicationRequest.builder()
                .withCustomerId(customerId)
                .withMedName(medName)
                .withMedInfo(medInfo)
                .build();

        //WHEN
        AddMedicationResult result = addMedicationActivity.handleRequest(request);

        //THEN
        verify(medicationDao).addMedication(any(Medication.class));

        assertEquals(customerId, result.getMedicationModel().getCustomerId());
        assertEquals(medName, result.getMedicationModel().getMedName());
        assertNotNull(result.getMedicationModel().getMedInfo());
        assertNotNull(result.getMedicationModel().getNotificationTimes());
    }

    @Test
    public void handleRequest_invalidName_throwsInvalidAttributeValueException() {
        //GIVEN
        String customerId = "customerId";
        String medName = "medName??";
        String medInfo = null;

        AddMedicationRequest request = AddMedicationRequest.builder()
                .withCustomerId(customerId)
                .withMedName(medName)
                .withMedInfo(medInfo)
                .build();

        //WHEN AND THEN
        assertThrows(InvalidAttributeValueException.class, () -> addMedicationActivity.handleRequest(request));
    }
    @Test
    public void handleRequest_MedNameIsBlank_throwsIllegalArgumentException() {
        //GIVEN
        String customerId = "customerId";
        String medName = "";
        String medInfo = null;

        AddMedicationRequest request = AddMedicationRequest.builder()
                .withCustomerId(customerId)
                .withMedName(medName)
                .withMedInfo(medInfo)
                .build();

        //WHEN AND THEN
        assertThrows(IllegalArgumentException.class, () -> addMedicationActivity.handleRequest(request));
    }
}