package com.apepdcl.gis_survey.crash_report;


import android.app.Application;



/**
 * Created by SOUBHAGYA SWAIN on 07-08-2026.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new ActivityTracker());

        Thread.setDefaultUncaughtExceptionHandler(
                new CrashHandler(this)
        );

        ANRWatchDogManager.init(this);
    }

}
