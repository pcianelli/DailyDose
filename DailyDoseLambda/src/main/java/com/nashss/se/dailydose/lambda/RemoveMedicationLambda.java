package com.nashss.se.dailydose.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.dailydose.activity.requests.RemoveMedicationRequest;
import com.nashss.se.dailydose.activity.results.RemoveMedicationResult;

public class RemoveMedicationLambda
        extends LambdaActivityRunner<RemoveMedicationRequest, RemoveMedicationResult>
        implements RequestHandler<AuthenticatedLambdaRequest<RemoveMedicationRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<RemoveMedicationRequest> input, Context context) {
        return super.runActivity(
                () -> {
                    RemoveMedicationRequest unauthenticatedRequest = input.fromPath(RemoveMedicationRequest.class);
                    return input.fromUserClaims(claims ->
                            RemoveMedicationRequest.builder()
                                    .withCustomerId(claims.get("email"))
                                    .withMedName(unauthenticatedRequest.getMedName())
                                    .build());
                },
                (request, serviceComponent) ->
                        serviceComponent.provideRemoveMedicationActivity().handleRequest(request)
        );
    }
}
