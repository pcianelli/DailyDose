package com.nashss.se.dailydose.metrics;

public class MetricsConstants {

    public static final String SERVICE = "Service";

    public static final String SERVICE_NAME = "DailyDose";

    public static final String NAMESPACE_NAME = "U3/DailyDose";

    public static final String GETMEDICATIONS_MEDICATIONNOTFOUND_COUNT = "GetMedications" +
            ".MedicationNotFoundException.Count";
    public static final String GETNOTIFICATIONS_NOTIFATIONSNOTFOUND_COUNT = "GetNotifications" +
            ".NotificationNotFoundException.Count";

    public static final String GETONEMEDICATION_SUCCESS_COUNT = "GetMedications.MedicationNotFoundException.Count";

    public static final String GETONEMEDICATION_FAIL_COUNT = "GetMedications.MedicationNotFoundException.Count";

    public static final String ADDMEDICATION_SUCCESS_COUNT = "GetMedications.MedicationNotFoundException.Count";
    public static final String ADDMEDICATION_FAIL_COUNT = "GetMedications.MedicationNotFoundException.Count";

    public static final String REMOVEMEDICATION_SUCCESS_COUNT = "GetMedications.MedicationNotFoundException.Count";
    public static final String REMOVEMEDICATION_FAIL_COUNT = "GetMedications.MedicationNotFoundException.Count";

    public static final String ADDNOTIFICATION_SUCCESS_COUNT = "GetNotifications.NotificationNotFoundException.Count";
    public static final String ADDNOTIFICATION_FAIL_COUNT = "GetNotifications.NotificationNotFoundException.Count";

    public static final String REMOVENOTIFICATION_SUCCESS_COUNT =
            "GetNotifications.NotificationNotFoundException.Count";
    public static final String REMOVENOTIFICATION_FAIL_COUNT = "GetNotifications.NotificationNotFoundException.Count";
}
