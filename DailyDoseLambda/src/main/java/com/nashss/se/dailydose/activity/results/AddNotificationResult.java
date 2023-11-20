package com.nashss.se.dailydose.activity.results;

import com.nashss.se.dailydose.models.NotificationModel;

public class AddNotificationResult {
    private final NotificationModel notificationModel;

    private AddNotificationResult(NotificationModel notificationModel) {
        this.notificationModel = notificationModel;
    }

    public NotificationModel getNotificationModel() {
        return notificationModel;
    }

    @Override
    public String toString() {
        return "AddNotificationResult{" +
                "notificationModel=" + notificationModel +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builer() {
        return new Builder();
    }

    public static class Builder {
        private NotificationModel notificationModel;

        public Builder withNotificationModel(NotificationModel notificationModel) {
            this.notificationModel = notificationModel;
            return this;
        }

        public AddNotificationResult build() {
            return new AddNotificationResult(notificationModel);
        }
    }
}
