package com.apepdcl.gis_survey.crash_report;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Created by SOUBHAGYA SWAIN on 07-08-2026.
 */



public class ActivityTracker implements Application.ActivityLifecycleCallbacks {

    private static Activity currentActivity;

    @Override
    public void onActivityResumed(Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityPaused(Activity activity) { }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) { }

    @Override
    public void onActivityStarted(Activity activity) { }

    @Override
    public void onActivityStopped(Activity activity) { }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) { }

    @Override
    public void onActivityDestroyed(Activity activity) { }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }
}
