package com.nashss.se.dailydose.lambda;

import com.nashss.se.dailydose.activity.requests.AddNotificationRequest;
import com.nashss.se.dailydose.activity.results.AddNotificationResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;


public class AddNotificationLambda
        extends LambdaActivityRunner<AddNotificationRequest, AddNotificationResult>
        implements RequestHandler<AuthenticatedLambdaRequest<AddNotificationRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<AddNotificationRequest> input, Context context) {
        return super.runActivity(
            () -> {
                AddNotificationRequest unauthenticatedRequest = input.fromBody(AddNotificationRequest.class);
                return input.fromUserClaims(claims ->
                    AddNotificationRequest.builder()
                        .withCustomerId(claims.get("email"))
                        .withMedName(unauthenticatedRequest.getMedName())
                        .withTime(unauthenticatedRequest.getTime())
                        .build());
            },
            (request, serviceComponent) ->
                serviceComponent.provideAddNotificationActivity().handleRequest(request)
        );
    }
}
