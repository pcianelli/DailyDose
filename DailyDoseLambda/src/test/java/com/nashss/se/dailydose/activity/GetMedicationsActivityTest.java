package com.nashss.se.dailydose.activity;

import com.nashss.se.dailydose.dynamodb.MedicationDao;
import com.nashss.se.dailydose.dynamodb.NotificationDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.MockitoAnnotations.openMocks;

class GetMedicationsActivityTest {
    @Mock
    private MedicationDao medicationDao;
    @Mock
    private NotificationDao notificationDao;
    @InjectMocks
    GetMedicationsActivity activity;

    @BeforeEach
    void setup() {openMocks(this);}

    @Test
    void handleRequest_withCustomerIdAndStartKeyMedNameIsNull_ReturnsListOfMedicationModel() {

    }

    @Test
    void handleRequest_withCustomerIdAndStartKeyMedNameIsNotNull_ReturnsListOfMedicationModel() {

    }



}