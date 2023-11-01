package com.nashss.se.dailydose.activity;

import com.nashss.se.dailydose.dynamodb.MedicationDao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

/**
 * Implementation of the GetMedicationsActivity for the DailyDose GetMedications API.
 * <p>
 * This API allows the customer to get the list medications they are tracking.
 */
public class GetMedicationsActivity {
    private final Logger log = LogManager.getLogger();
    private final MedicationDao medicationDao;
    /**
     * Instantiates a new GetMedicationsActivity object.
     *
     * @param medicationDao medicationDao to access the medications table.
     */
    @Inject
    public GetMedicationsActivity(MedicationDao medicationDao) {
        this.medicationDao = medicationDao;
    }
}
