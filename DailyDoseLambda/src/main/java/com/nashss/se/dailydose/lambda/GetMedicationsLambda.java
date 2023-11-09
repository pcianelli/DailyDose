package com.nashss.se.dailydose.lambda;

import com.nashss.se.dailydose.activity.requests.GetMedicationsRequest;
import com.nashss.se.dailydose.activity.results.GetMedicationsResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class GetMedicationsLambda
        extends LambdaActivityRunner<GetMedicationsRequest, GetMedicationsResult>
        implements RequestHandler<AuthenticatedLambdaRequest<GetMedicationsRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetMedicationsRequest> input, Context context) {
        return super.runActivity(() -> {
                GetMedicationsRequest unauthenticatedRequest = input.fromBody(GetMedicationsRequest.class);
                return input.fromUserClaims(claims ->
                    GetMedicationsRequest.builder()
                        .withCustomerId(claims.get("email"))
                        .withMedName(unauthenticatedRequest.getMedName())
                            .build());
        },
            (request, serviceComponent) ->
                serviceComponent.provideGetMedicationsActivity().handleRequest(request)
        );
    }
}
