package com.nashss.se.dailydose.activity;

import com.nashss.se.dailydose.activity.requests.GetMedicationThirdPartyRequest;
import com.nashss.se.dailydose.activity.results.GetMedicationThirdPartyResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

/**
 * Implementation of the GetMedicationThirdPartyActivity for the openFDA API.
 * <p>
 * This API allows the customer to get the medication information about
 * warnings and other info with the medication they are tracking.
 */
public class GetMedicationThirdPartyActivity {
    private final Logger log = LogManager.getLogger();

    /**
     * Instantiates a new GetMedicationThirdPartyActivity object.
     */
    @Inject
    public GetMedicationThirdPartyActivity() {
    }

    /**
     * This method handles the incoming request by retrieving information from openFDA API
     * <p>
     * It then returns a MedicationThirdPartyModel.
     * <p>
     * If there is an error retrieving the data from the API, should throw illegalArgumentException
     *
     * @param getMedicationThirdPartyRequest request object
     * @return getMedicationThirdPartyResult result object containing the medicationThirdPartyModel
     */
    public GetMedicationThirdPartyResult handleRequest(final GetMedicationThirdPartyRequest getMedicationThirdPartyRequest) {


        return GetMedicationThirdPartyResult.builder()
                .build();
    }
}
