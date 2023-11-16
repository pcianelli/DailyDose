package com.nashss.se.dailydose.activity;

import com.nashss.se.dailydose.activity.requests.UpdateMedicationInfoRequest;
import com.nashss.se.dailydose.activity.results.UpdateMedicationInfoResult;
import com.nashss.se.dailydose.dynamodb.MedicationDao;
import com.nashss.se.dailydose.dynamodb.models.Medication;
import com.nashss.se.dailydose.exceptions.MedicationNotFoundException;
import com.nashss.se.dailydose.metrics.MetricsPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class UpdateMedicationInfoActivityTest {
    @Mock
    private MedicationDao medicationDao;
    @Mock
    private MetricsPublisher metricsPublisher;
    private UpdateMedicationInfoActivity updateMedicationInfoActivity;

    @BeforeEach
    public void setup() {
        openMocks(this);
        updateMedicationInfoActivity = new UpdateMedicationInfoActivity(medicationDao);
    }

    @Test
    public void handleRequest_goodRequest_updatesMedicationInfo() {
        //GIVEN
        String customerId = "customerId";
        String medName = "medName";
        String medInfo = "NewMedInfo";

        UpdateMedicationInfoRequest request = UpdateMedicationInfoRequest.builder()
                                                .withCustomerId(customerId)
                                                .withMedName(medName)
                                                .withMedInfo(medInfo)
                                                .build();

        Medication startingMedication = new Medication();
        startingMedication.setCustomerId(customerId);
        startingMedication.setMedName(medName);
        startingMedication.setMedInfo("OldMedInfo");

        when(medicationDao.getOneMedication(customerId, medName)).thenReturn(startingMedication);
        when(medicationDao.addMedication(startingMedication)).thenReturn(startingMedication);

        //WHEN
        UpdateMedicationInfoResult result = updateMedicationInfoActivity.handleRequest(request);

        //THEN
        assertEquals(customerId, result.getMedicationModel().getCustomerId());
        assertEquals(medName,result.getMedicationModel().getMedName());
        assertEquals(medInfo, result.getMedicationModel().getMedInfo());

    }

    @Test
    public void handleRequest_medicationDoesNotExist_throwsMedicationNotFoundException() {
        //GIVEN
        String customerId = "customerId";
        String medName = "medName";
        String medInfo = "NewMedInfo";

        UpdateMedicationInfoRequest request = UpdateMedicationInfoRequest.builder()
                .withCustomerId(customerId)
                .withMedName(medName)
                .withMedInfo(medInfo)
                .build();



        Medication differentMedNameMedication = new Medication();
        differentMedNameMedication.setCustomerId(customerId);
        differentMedNameMedication.setMedName("OtherMedName");
        differentMedNameMedication.setMedInfo(medInfo);

        when(medicationDao.getOneMedication(customerId, medName)).thenReturn(differentMedNameMedication);

        //THEN
        assertThrows(MedicationNotFoundException.class, () -> updateMedicationInfoActivity.handleRequest(request));
    }

}