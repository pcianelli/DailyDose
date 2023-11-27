package com.nashss.se.dailydose.activity.results;

import com.nashss.se.dailydose.models.NotificationModel;

import java.util.List;

public class GetNotificationsResult {
    private final List<NotificationModel> notificationModelList;

    private GetNotificationsResult(List<NotificationModel> notificationModelList) {
        this.notificationModelList = notificationModelList;
    }

    public List<NotificationModel> getNotificationModelList() {
        return notificationModelList;
    }

    @Override
    public String toString() {
        return "GetNotificationsResult{" +
                "notificationModelList=" + notificationModelList +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<NotificationModel> notificationModelList;

        public Builder withNotificationModelList(List<NotificationModel> notificationModelList) {
            this.notificationModelList = notificationModelList;
            return this;
        }

        public GetNotificationsResult build() {
            return new GetNotificationsResult(notificationModelList);
        }
    }
}
