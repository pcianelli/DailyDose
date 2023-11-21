package com.nashss.se.dailydose.dependency;

import com.nashss.se.dailydose.activity.AddMedicationActivity;
import com.nashss.se.dailydose.activity.AddNotificationActivity;
import com.nashss.se.dailydose.activity.GetMedicationsActivity;

import com.nashss.se.dailydose.activity.RemoveMedicationActivity;

import com.nashss.se.dailydose.activity.UpdateMedicationInfoActivity;
import dagger.Component;

import javax.inject.Singleton;

/**
 * Dagger component for providing dependency injection in the DailyDoseLambda.
 */
@Singleton
@Component(modules = {DaoModule.class, MetricsModule.class})
public interface ServiceComponent {

    /**
     * Provides the relevant activity.
     * @return GetMedicationsActivity
     */
    GetMedicationsActivity provideGetMedicationsActivity();

    /**
     * Provides the relevant activity.
     * @return AddMedicationsActivity
     */
    AddMedicationActivity provideAddMedicationActivity();

    /**
     * Provides the relevant activity.
     * @return RemoveMedicationsActivity
     */
    RemoveMedicationActivity provideRemoveMedicationActivity();

    /**
     * Provides the relevant activity.
     * @return UpdateMedicationInfoActivity
     */
    UpdateMedicationInfoActivity provideUpdateMedicationInfo();

    /**
     * Provides the relevant activity.
     * @return addNotificationActivity
     */
    AddNotificationActivity provideAddNotificationActivity();
}
