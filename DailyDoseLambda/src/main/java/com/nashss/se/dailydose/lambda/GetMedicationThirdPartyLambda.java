package com.nashss.se.dailydose.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.dailydose.activity.requests.GetMedicationThirdPartyRequest;
import com.nashss.se.dailydose.activity.results.GetMedicationThirdPartyResult;

public class GetMedicationThirdPartyLambda
        extends LambdaActivityRunner<GetMedicationThirdPartyRequest, GetMedicationThirdPartyResult>
        implements RequestHandler<LambdaRequest<GetMedicationThirdPartyRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(LambdaRequest<GetMedicationThirdPartyRequest> input, Context context) {
        return super.runActivity(
            ()-> input.fromPath(path ->
                GetMedicationThirdPartyRequest.builder()
                    .withGenericName(path.get("medName"))
                    .build()),
            (request, serviceComponent) ->
                serviceComponent.provideGetMedicationThirdPartyActivity().handleRequest(request)
        );
    }
}
