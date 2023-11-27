package com.nashss.se.dailydose.lambda;

import com.nashss.se.dailydose.activity.requests.RemoveNotificationRequest;
import com.nashss.se.dailydose.activity.results.RemoveNotificationResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class RemoveNotificationLambda
        extends LambdaActivityRunner<RemoveNotificationRequest, RemoveNotificationResult>
        implements RequestHandler<AuthenticatedLambdaRequest<RemoveNotificationRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<RemoveNotificationRequest> input, Context context) {
        return super.runActivity(
            () -> {
                RemoveNotificationRequest unauthenticatedRequest = input.fromPath(path ->
                    RemoveNotificationRequest.builder()
                        .withMedName(path.get("medName"))
                        .withTime(path.get("time"))
                        .build());
                return input.fromUserClaims(claims ->
                    RemoveNotificationRequest.builder()
                        .withCustomerId(claims.get("email"))
                        .withMedName(unauthenticatedRequest.getMedName())
                        .withTime(unauthenticatedRequest.getTime())
                        .build());
            },
            (request, serviceComponent) ->
                serviceComponent.provideRemoveNotificationActivity().handleRequest(request)
        );
    }
}
