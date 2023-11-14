package com.nashss.se.dailydose.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.dailydose.activity.requests.AddMedicationRequest;
import com.nashss.se.dailydose.activity.results.AddMedicationResult;

public class AddMedicationLambda
        extends LambdaActivityRunner<AddMedicationRequest, AddMedicationResult>
        implements RequestHandler<AuthenticatedLambdaRequest<AddMedicationRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<AddMedicationRequest> input, Context context) {
        return super.runActivity(
                () -> {
                    AddMedicationRequest unauthenticatedRequest = input.fromBody(AddMedicationRequest.class);
                    return input.fromUserClaims(claims ->
                            AddMedicationRequest.builder()
                                    .withCustomerId(claims.get("email"))
                                    .withMedName(unauthenticatedRequest.getMedName())
                                    .withMedInfo(unauthenticatedRequest.getMedInfo())
                                    .build());
                },
                (request, serviceComponent) ->
                        serviceComponent.provideAddMedicationActivity().handleRequest(request)
        );
    }
}
