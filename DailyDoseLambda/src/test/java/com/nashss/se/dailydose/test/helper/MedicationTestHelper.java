package com.nashss.se.dailydose.test.helper;

import com.nashss.se.dailydose.dynamodb.models.Medication;

import java.util.ArrayList;
import java.util.List;

public class MedicationTestHelper {
    public static Medication generateMedication(int sequenceNumber, String customerId) {
        Medication medication = new Medication();
        medication.setCustomerId(customerId);
        medication.setMedName("medName" + sequenceNumber);
        medication.setMedInfo("medInfo" + sequenceNumber);
        return medication;
    }

    public static List<Medication> generateMedicationList(int size, String customerId) {
        List<Medication> medicationList = new ArrayList<>();
        for (int i = 0; i<size; i++) {
            medicationList.add(generateMedication(i, customerId));
        }
        return medicationList;
    }
}
