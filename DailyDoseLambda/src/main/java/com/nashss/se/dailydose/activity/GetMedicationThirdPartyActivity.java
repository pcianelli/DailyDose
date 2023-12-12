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
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;

/**
 * Implementation of the GetMedicationThirdPartyActivity for the openFDA API.
 * <p>
 * This API allows the customer to get the medication information about
 * warnings and other info with the medication they are tracking.
 */
public class GetMedicationThirdPartyActivity {
    private final Logger log = LogManager.getLogger();
    static final String API_BASE_URL = "https://api.fda.gov/drug/label.json?search=openfda.generic_name=";

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

        String apiUrl = API_BASE_URL + getMedicationThirdPartyRequest.getGenericName();

        IdUtils.validateMedicationName(getMedicationThirdPartyRequest.getGenericName());
        IdUtils.validMedNameNotBlank(getMedicationThirdPartyRequest.getGenericName());

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI(apiUrl))
                .build();
        log.info(getRequest.toString());

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
        log.info(getResponse.toString());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(getResponse.body());
        log.info(rootNode.toString());

        JsonNode resultsNode = rootNode.path("results");
        if (resultsNode.isArray() && resultsNode.size() > 0) {
            JsonNode firstResultNode = resultsNode.get(0);

            // Initialize variables for fields
            String activeIngredient = null;
            String indicationsAndUsage = null;
            String warnings = null;
            String doNotUse = null;

            // Loop through the children nodes of firstResultNode
            Iterator<Map.Entry<String, JsonNode>> fieldsIterator = firstResultNode.fields();
            while (fieldsIterator.hasNext()) {
                Map.Entry<String, JsonNode> field = fieldsIterator.next();
                String fieldName = field.getKey();
                JsonNode fieldValue = field.getValue();

                // Check for the desired field names and extract their values
                if ("active_ingredient".equals(fieldName)) {
                    if (fieldValue.isArray() && fieldValue.size() > 0) {
                        activeIngredient = fieldValue.get(0).asText();
                    }
                } else if ("indications_and_usage".equals(fieldName)) {
                    indicationsAndUsage = fieldValue.asText();
                } else if ("warnings".equals(fieldName)) {
                    warnings = fieldValue.asText();
                } else if ("do_not_use".equals(fieldName)) {
                    doNotUse = fieldValue.asText();
                }
            }

            // Create the MedicationThirdPartyModel
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

        log.error("No results found in the API response.");
        throw new IllegalArgumentException("No results found");
    }
}
