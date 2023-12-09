package com.nashss.se.dailydose.activity;
import com.nashss.se.dailydose.activity.requests.GetMedicationThirdPartyRequest;
import com.nashss.se.dailydose.activity.results.GetMedicationThirdPartyResult;
import com.nashss.se.dailydose.models.MedicationThirdPartyModel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.nashss.se.dailydose.utils.IdUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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
     * @throws IOException throws IOException if URI cant be built
     * @throws InterruptedException throws InterruptedException if response cannot return from API
     * @throws URISyntaxException throws URISyntaxException if response cannot parsed
     */
    public GetMedicationThirdPartyResult handleRequest(
            final GetMedicationThirdPartyRequest getMedicationThirdPartyRequest)
            throws IOException, InterruptedException, URISyntaxException {

        String apiUrl = "https://api.fda.gov/drug/label.json?search=openfda.generic_name=" +
                getMedicationThirdPartyRequest.getGenericName();

        IdUtils.validateMedicationName(getMedicationThirdPartyRequest.getGenericName());
        IdUtils.validMedNameNotBlank(getMedicationThirdPartyRequest.getGenericName());

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI(apiUrl))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(getResponse.body());

        if(rootNode.path("error").path("code").asText().equals("NOT_FOUND")) {
            throw new IllegalArgumentException("Not found");
        }

        String activeIngredient = rootNode.path("results").path(0).path("active_ingredient").asText();
        String indicationsAndUsage = rootNode.path("results").path(0).path("indications_and_usage").asText();
        String warnings = rootNode.path("results").path(0).path("warnings").asText();
        String doNotUse = rootNode.path("results").path(0).path("do_not_use").asText();

        MedicationThirdPartyModel medicationThirdPartyModel = MedicationThirdPartyModel.builder()
                .withActiveIngredient(activeIngredient)
                .withIndicationsAndUsage(indicationsAndUsage)
                .withWarnings(warnings)
                .withDoNotUse(doNotUse)
                .build();

        return GetMedicationThirdPartyResult.builder()
                .withMedicationThirdPartyModel(medicationThirdPartyModel)
                .build();
    }
}
