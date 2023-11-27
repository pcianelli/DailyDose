package com.nashss.se.dailydose.lambda;

import com.nashss.se.dailydose.activity.requests.GetNotificationsRequest;
import com.nashss.se.dailydose.activity.results.GetNotificationsResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class GetNotificationsLambda
        extends LambdaActivityRunner<GetNotificationsRequest, GetNotificationsResult>
        implements RequestHandler<AuthenticatedLambdaRequest<GetNotificationsRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetNotificationsRequest> input, Context context) {
        return super.runActivity(
            () -> {
                GetNotificationsRequest unauthenticatedRequest = input.fromQuery(query ->
                    GetNotificationsRequest.builder()
                        .withTime(query.get("time"))
                        .build());
                return input.fromUserClaims(claims ->
                    GetNotificationsRequest.builder()
                        .withCustomerId(claims.get("email"))
                        .withTime(unauthenticatedRequest.getTime())
                        .build());
            },
            (request, serviceComponent) ->
                serviceComponent.provideGetNotificationsActivity().handleRequest(request)
        );
    }
}
