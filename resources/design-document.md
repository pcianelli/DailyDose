# Design Document

## _DailyDose_ Design

## 1. Problem Statement

_In today's rapidly expanding pharmaceutical industry, managing a diverse array of medications, along with their specific dosages, schedules, and usage instructions, can pose a significant challenge, particularly for an aging population. This is where the DailyDose app steps in to simplify the process._

_DailyDose allows users to create a detailed health chart where they can effortlessly catalog all their medications that they are currently prescribed. With the app, users can schedule medication alarms, view vital details such as dosage and dietary requirements, or simply just review their full health chart of medications._

_DailyDose - your personalized medication management solution, bringing ease, organization, and safety to your daily healthcare routine._

## 2. Top Questions to Resolve in Review

_List the most important questions you have about your design, or things that you are still debating internally that you might like help working through._

1. How many tables will I need?
2. How will I incorporate the notification feature with a timer?
3. Will I need to use a GSI for my table?

## 3. Use Cases



U1. _As a user, I would like to be able to create an account._

U2. _As a user, I would like to be able to login to my account._

U3. _As a user, a banner will display and notify me if I am supposed to take a medication, it will include dosage and other info, should only be posted for duration of 15 minutes before and after the alarm._

U4. _As a user, I would like to be able to view my health chart with my medications, medication info, and the alarms that are set for those medications._

U5. _As a user, I would like to be able to add a medication to my chart._

U6. _As a user, I would like to be able to remove a medication from my health chart._

U7. _As a user, I would like to be able to add information about my medication (ex. if I need to take with food, dosage or how many pills to take at once, or other needed info)._

U8. _As a user, I would like to be able to set an alarm for when I need to take my medication._

U9. _As a user, I would like to be able to update my medication information section_

U10. _As a user, I would like to be able to change the alarm time._

U11. _As a user, I would like to remove the alarm/ have no alarm._


### Stretch Use Cases:
U12. _The alarm set should send a push notification, text, and email._

U13. _The app should keep track of how many pills are left and notify the User one week out to refill the prescription._

U14. _The user can click on a medication in their health chart, and it will bring them to a medication page with information about the medication (ex. Risks, allergies, etc.)_

U15. _As a user, I would like to view my medication history health chart._

## 4. Project Scope

_Clarify which parts of the problem you intend to solve. It helps reviewers know what questions to ask to make sure you are solving for what you say and stops discussions from getting sidetracked by aspects you do not intend to handle in your design._

### 4.1. In Scope

_- View your health chart, create/login to account_
_- Add and remove medications from your health chart_
_- Set an alarm of when to take your medication_
_- Update the medication information and alarms set_

### 4.2. Out of Scope

_Providing information about how to get a medication refill, or doctor information_

# 5. Proposed Architecture Overview

_I will use API Gateway and Lambda to create seven endpoints (GetMedicationsLambda, AddMedicationLambda, RemoveMedicationLambda, UpdateMedicationLambda, AddNotificationLambda, RemoveNotificationLambda, GetNotificationLambda) that will handle the creation, update, and retrieval of medications on the client healthChart and Notifications to satisfy my
requirements._

_I will store Medications in a dynamoDbTable. I will store Notifications in a dynamoDbTable._

# 6. API

## 6.1. Public Models

```
// MedicationModel

String customerId;
String medName;
String medInfo;
Set<Notification> notifications;
```

```
// NotificationModel

String customerId;
String medName;
String time;
```

###Notes - inorder to to populate the medication model fully, the activity calls on the medication dao to get the medName, and med Info, and also calls the notification dao to get the set of notifications and query the notifications table based on the customerId and medName to get all the notifications for that medName. The MedicationModel is used for the health chart.

## 6.2. _Get All Medications Endpoint_

* Accepts `GET` requests to `/medications/`
* Query on just hashkey.  Medication table based on customerId and returns all Medications for a customerId.
* If there are no medications on the table, return an empty Set.

![Sequence Diagram Get Medications.png](images%2FdesignImages%2FSequence%20Diagram%20Get%20Medications.png)

## 6.3 _Add Medication Endpoint_

* Accepts `POST` requests to `/medications/:medName`
* Accepts a customer ID and a medName to be added.
    * For security concerns, we will validate the provided med name does not
      contain invalid characters: `" ' \`
    * If the med name contains invalid characters, will throw an
      `InvalidAttributeValueException`
    * If the med can not be added to the Medication Table, will throw an `UnableToAddMedicationToTableException`_

![Sequence diagram add medication.png](images%2FdesignImages%2FSequence%20diagram%20add%20medication.png)


## 6.4 _Remove Medication Endpoint_

* Accepts `DELETE` requests to `/medications/:medName`
* Accepts a customer ID and a medName to be Deleted.
    * For security concerns, we will validate the provided med name does not
      contain invalid characters: `" ' \`
    * If the med name contains invalid characters, will throw an
      `InvalidAttributeValueException`
    * If no med found on the table, will throw an `UnableToFindMedicationException`
    * If the med can not be deleted to the Medication Table, will throw an `UnableToDeleteMedicationException`
  
![Sequence Diagram remove medication.png](images%2FdesignImages%2FSequence%20Diagram%20remove%20medication.png)


## 6.5 _Update Medication Endpoint_
* Accepts `PUT` requests to `/medications/:medName`
* Accepts a customer ID and a medName to be updated.
    * For security concerns, we will validate the provided med name does not
      contain invalid characters: `" ' \`
    * If the med name contains invalid characters, will throw an
      `InvalidAttributeValueException`
    * If no med found on the table, will throw an `UnableToFindMedicationTableException`
    * If the med can not be updated on the Medication Table, will throw an `UnableToUpdateMedicationTableException`
  
![Sequence Diagram Update Medication.png](images%2FdesignImages%2FSequence%20Diagram%20Update%20Medication.png)


## 6.6 _Get Notification Endpoint_
* Accepts `GET` requests to `/notifications/:time` 
* Query notification gsi if the parameter passed in is time, returns all Notifications for that customerId at that time window of 15 minutes before and 15 minutes after that time. This is used to populate the banner only, not the health chart. 
    * If there are no notifications on the table, return an empty Set.

![Sequence Diagram Get Notifications.png](images%2FdesignImages%2FSequence%20Diagram%20Get%20Notifications.png)

## 6.7 _Add Notification Endpoint_

* Accepts `POST` requests to `/notifications/:time/medName`
* Accepts a customer ID and a time to be added.

    * If the time contains invalid characters, will throw an
      `InvalidAttributeValueException`
    * If the notification can not be added to the Notifications Table, will throw an `UnableToAddNotificationToTableException`_

![Sequence Diagram Add Notification.png](images%2FdesignImages%2FSequence%20Diagram%20Add%20Notification.png)


## 6.8 _Remove Notification Endpoint_
* Accepts `DELETE` requests to `/notifications/:time/medName`
* Accepts a customer ID and a time to delete notification.
    * If the time contains invalid characters, will throw an
      `InvalidAttributeValueException`
    * If no notification found on the table, will throw an `UnableToFindNotificationException`
    * If the notification can not be deleted to the Medication Table, will throw an `UnableToDeleteNotificationException`

![Sequence Diagram Remove Notification.png](images%2FdesignImages%2FSequence%20Diagram%20Remove%20Notification.png)


# 7. Tables

### 7.1. `Medications`
```
customer_id // partition key, string
med_name // sort key, string 
med_info // string 
```

### 7.2. `Notifications`
```
customer_id // partition key, string
med_name // sort key, string 
time // string 
```

### 7.2. `GSI Notifications`
```
customer_id // partition key, string
time // sort key, string 
med_name // string 
```

# 8. Pages

![Pages Design jamboard DailyDose.png](images/designImages/Pages%20Design%20jamboard%20DailyDose.png)

![Pages2 Design Jamboard DailyDose.png](images%2FdesignImages%2FPages2%20Design%20Jamboard%20DailyDose.png)
