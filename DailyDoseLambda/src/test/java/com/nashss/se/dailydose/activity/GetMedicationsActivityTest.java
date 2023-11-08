package com.nashss.se.dailydose.activity;

import com.nashss.se.dailydose.activity.requests.GetMedicationsRequest;
import com.nashss.se.dailydose.activity.results.GetMedicationsResult;
import com.nashss.se.dailydose.dynamodb.MedicationDao;
import com.nashss.se.dailydose.dynamodb.NotificationDao;
import com.nashss.se.dailydose.dynamodb.models.Medication;
import com.nashss.se.dailydose.dynamodb.models.Notification;
import com.nashss.se.dailydose.models.MedicationModel;
import com.nashss.se.dailydose.test.helper.MedicationTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
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
        //GIVEN
        LocalTime currentTime = LocalTime.now();
        LocalTime futureTimeTen = currentTime.plusMinutes(10);

        String customerId = "1111";
        String medName = null;

        String medName1 = "medName1";
        String medName2 = "medName2";

        List<Medication> medicationList = MedicationTestHelper.generateMedicationList(6, customerId);

        Notification notification1 = new Notification();
        notification1.setCustomerId(customerId);
        notification1.setMedName(medName1);
        notification1.setTime(currentTime);

        Notification notification2 = new Notification();
        notification2.setCustomerId(customerId);
        notification2.setCustomerId(medName2);
        notification2.setTime(futureTimeTen);

        Set<Notification> notificationSet = new HashSet<>();
        notificationSet.add(notification1);
        notificationSet.add(notification2);

        when(notificationDao.getNotifications(customerId, medName)).thenReturn(notificationSet);
        when(medicationDao.getMedications(customerId, medName)).thenReturn(medicationList);

        GetMedicationsRequest request = GetMedicationsRequest.builder()
                .withCustomerId(customerId)
                .withMedName(medName)
                .build();

        //WHEN
        GetMedicationsResult result = activity.handleRequest(request);

        //THEN
        List<MedicationModel> resultList = result.getMedicationModelList();
        String lastCustomerId = resultList.get(resultList.size()-1).getCustomerId();
        String lastMedName = resultList.get(resultList.size()-1).getMedName();

        for(int i = 0; i<resultList.size(); i++){
            assertEquals(medicationList.get(i).getCustomerId(), resultList.get(i).getCustomerId());
            assertEquals(medicationList.get(i).getMedName(), resultList.get(i).getMedName());
            assertEquals(medicationList.get(i).getMedInfo(), resultList.get(i).getMedInfo());
            System.out.println(resultList.get(i).getMedName());
        }
        assertNotNull(resultList.get(0).getNotificationTimes());
        assertNotNull(resultList.get(1).getNotificationTimes());
        assertEquals(customerId, lastCustomerId, "CustomerId's " +
                "should stay the same and should be future startId");
        assertEquals("medName5", lastMedName, "Future " +
                "start medName should be equal to last item medName of limit 5");
    }

    @Test
    void handleRequest_withCustomerIdAndStartKeyMedNameIsNotNull_ReturnsListOfMedicationModel() {
        //GIVEN

        String customerId = "1111";

        List<Medication> medicationList = MedicationTestHelper.generateMedicationList(6, customerId);

        String startMedName = medicationList.get(0).getMedName();

        Set<Notification> notificationSet = new HashSet<>();


        when(notificationDao.getNotifications(customerId, startMedName)).thenReturn(notificationSet);
        when(medicationDao.getMedications(customerId, startMedName)).thenReturn(medicationList);

        GetMedicationsRequest request = GetMedicationsRequest.builder()
                .withCustomerId(customerId)
                .withMedName(startMedName)
                .build();

        //WHEN
        GetMedicationsResult result = activity.handleRequest(request);

        //THEN
        List<MedicationModel> resultList = result.getMedicationModelList();
        String lastCustomerId = resultList.get(resultList.size()-1).getCustomerId();
        String lastMedName = resultList.get(resultList.size()-1).getMedName();

        assertEquals(startMedName, resultList.get(0).getMedName());
        assertEquals(customerId, resultList.get(0).getCustomerId());
        assertEquals("medInfo0", resultList.get(0).getMedInfo());
        assertNotNull(resultList.get(0).getNotificationTimes());
        assertEquals(customerId, lastCustomerId, "CustomerId's should " +
                "stay the same and should be future startId");
        assertEquals("medName5", lastMedName, "Future start " +
                "medName should be equal to last item medName of limit 5");

        for (MedicationModel medicationModel : resultList) {
            System.out.println(medicationModel.getMedName());
        }
    }
}