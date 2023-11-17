package com.nashss.se.dailydose.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.dailydose.activity.requests.UpdateMedicationInfoRequest;
import com.nashss.se.dailydose.activity.results.UpdateMedicationInfoResult;

public class UpdateMedicationInfoLambda
        extends LambdaActivityRunner<UpdateMedicationInfoRequest, UpdateMedicationInfoResult>
        implements RequestHandler<AuthenticatedLambdaRequest<UpdateMedicationInfoRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<UpdateMedicationInfoRequest> input, Context context) {
        return super.runActivity(
                () -> {
                    UpdateMedicationInfoRequest unauthenticatedRequest = input.fromBody(UpdateMedicationInfoRequest.class);
                    return input.fromUserClaims(claims ->
                            UpdateMedicationInfoRequest.builder()
                                    .withCustomerId(claims.get("email"))
                                    .withMedName(unauthenticatedRequest.getMedName())
                                    .withMedInfo(unauthenticatedRequest.getMedInfo())
                                    .build());
                },
                (request, serviceComponent) ->
                        serviceComponent.provideUpdateMedicationInfo().handleRequest(request)
        );
    }
}
