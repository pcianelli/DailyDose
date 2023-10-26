package com.nashss.se.dailydose.dependency;

import com.nashss.se.dailydose.activity.GetMedicationsActivity;
import dagger.Component;

import javax.inject.Singleton;

/**
 * Dagger component for providing dependency injection in the Music Playlist Service.
 */
@Singleton
@Component(modules = {DaoModule.class, MetricsModule.class})
public interface ServiceComponent {

    /**
     * Provides the relevant activity.
     * @return GetMedicationsActivity
     */
    GetMedicationsActivity provideGetMedicationsActivity();
}
