package com.noobsunited.goLazy.modules;

import android.app.Application;

import com.noobsunited.goLazy.services.SettingsService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by mark on 1-8-16.
 */
@Module
public class SettingsModule {

    @Provides
    @Singleton
    SettingsService providesSettingsService(Application application) {
        return new SettingsService(application.getApplicationContext());
    }

}
