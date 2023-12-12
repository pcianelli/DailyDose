package com.nashss.se.dailydose.activity;


import com.nashss.se.dailydose.activity.requests.GetMedicationThirdPartyRequest;
import com.nashss.se.dailydose.activity.results.GetMedicationThirdPartyResult;
import com.nashss.se.dailydose.exceptions.InvalidAttributeValueException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class GetMedicationThirdPartyActivityTest {
    @Mock
    private HttpClient httpClient;
    @InjectMocks
    private GetMedicationThirdPartyActivity activity;
    @Mock
    HttpRequest httpRequest;
    @Mock
    HttpResponse<String> httpResponse;

    private final Logger log = LogManager.getLogger();

    @BeforeEach
    void setup() {
        openMocks(this);
    }

    @Test
    void handleRequest_withValidMedNameAndRequest_ReturnsValidResponse() throws IOException, InterruptedException, URISyntaxException {
        // GIVEN
        GetMedicationThirdPartyRequest request = GetMedicationThirdPartyRequest.builder().withGenericName("Albuterol").build();
        String medName = request.getGenericName();

        String expectedUrl = GetMedicationThirdPartyActivity.API_BASE_URL + medName;
        String mockedJsonResponse = "{\"results\":[{\"active_ingredient\":\"MockedIngredient\",\"indications_and_usage\":\"MockedUsage\",\"warnings\":\"MockedWarnings\",\"do_not_use\":\"MockedDoNotUse\"}]}";


        when(httpRequest.uri()).thenReturn(new URI(expectedUrl));
        when(httpResponse.body()).thenReturn(mockedJsonResponse);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);

        // WHEN
        GetMedicationThirdPartyResult result = activity.handleRequest(request);

        // THEN
        assertNotNull(result);
    }

    @Test
    void handleRequest_withMedNameNotFound_ThrowsIllegalArgumentException() throws IOException, InterruptedException {
        //GIVEN
        GetMedicationThirdPartyRequest request = GetMedicationThirdPartyRequest.builder().withGenericName("medNameNotFound").build();
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenThrow(IllegalArgumentException.class);

        //THEN and WHEN
        assertThrows(IllegalArgumentException.class, () -> activity.handleRequest(request));

    }

    @Test
    void handleRequest_withInvalidMedName_ThrowsInvalidAttributeValueException() throws IOException, InterruptedException {
        //GIVEN
        GetMedicationThirdPartyRequest request = GetMedicationThirdPartyRequest.builder().withGenericName("@@###$$!!").build();
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenThrow(InvalidAttributeValueException.class);

        //WHEN and THEN
        assertThrows(InvalidAttributeValueException.class, () -> activity.handleRequest(request));
    }

    @Test
    void handleRequest_withMedNameBlank_ThrowsIllegalArgumentException() throws IOException, InterruptedException {
        //GIVEN
        GetMedicationThirdPartyRequest request = GetMedicationThirdPartyRequest.builder().withGenericName("").build();
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenThrow(IllegalArgumentException.class);

        //WHEN and THEN
        assertThrows(IllegalArgumentException.class, () -> activity.handleRequest(request));
    }
}