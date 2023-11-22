package com.nashss.se.dailydose.activity.results;

import com.nashss.se.dailydose.models.NotificationModel;

public class RemoveNotificationResult {
    private final NotificationModel notificationModel;

    private RemoveNotificationResult(NotificationModel notificationModel) {
        this.notificationModel = notificationModel;
    }

    public NotificationModel getNotificationModel() {
        return notificationModel;
    }

    @Override
    public String toString() {
        return "RemoveNotificationResult{" +
                "notificationModel=" + notificationModel +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private NotificationModel notificationModel;

        public Builder withNotificationModel(NotificationModel notificationModel) {
            this.notificationModel = notificationModel;
            return this;
        }

        public RemoveNotificationResult build() {
            return new RemoveNotificationResult(notificationModel);
        }
    }
}
