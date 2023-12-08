package com.nashss.se.dailydose.lambda;

import com.nashss.se.dailydose.activity.requests.GetMedicationThirdPartyRequest;
import com.nashss.se.dailydose.activity.results.GetMedicationThirdPartyResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.io.IOException;
import java.net.URISyntaxException;

public class GetMedicationThirdPartyLambda
        extends LambdaActivityRunner<GetMedicationThirdPartyRequest, GetMedicationThirdPartyResult>
        implements RequestHandler<LambdaRequest<GetMedicationThirdPartyRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(LambdaRequest<GetMedicationThirdPartyRequest> input, Context context) {
        return super.runActivity(
            () -> input.fromPath(path ->
                GetMedicationThirdPartyRequest.builder()
                    .withGenericName(path.get("medName"))
                    .build()),
            (request, serviceComponent) -> {
                try {
                    return serviceComponent.provideGetMedicationThirdPartyActivity().handleRequest(request);
                } catch (IOException | URISyntaxException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        );
    }
}
