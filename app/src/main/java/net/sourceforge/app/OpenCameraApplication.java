package net.sourceforge.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;

import com.apepdcl.gis_survey.GlobalLocationManager;
import com.apepdcl.gis_survey.crash_report.ANRWatchDogManager;
import com.apepdcl.gis_survey.crash_report.ActivityTracker;
import com.apepdcl.gis_survey.crash_report.CrashHandler;

/** We override the Application class to implement the workaround at
 *  https://issuetracker.google.com/issues/36972466#comment14 for Google bug crash. It seems ugly,
 *  but Google consider this a low priority despite calling these "bad behaviours" in applications!
 */
public class OpenCameraApplication extends Application {
    private static final String TAG = "OpenCameraApplication";

    @Override
    public void onCreate() {
        if( MyDebug.LOG )
            Log.d(TAG, "onCreate");
        super.onCreate();
        checkAppReplacingState();

//        registerActivityLifecycleCallbacks(new ActivityTracker());
//
//        Thread.setDefaultUncaughtExceptionHandler(
//                new CrashHandler(this)
//        );
//
//        ANRWatchDogManager.init(this);

        GlobalLocationManager.getInstance().init(this);
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityResumed(Activity activity) {
                GlobalLocationManager.getInstance().setCurrentActivity(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {
              GlobalLocationManager.getInstance().clearCurrentActivity(activity);
            }

            @Override public void onActivityCreated(Activity activity, Bundle bundle) {}
            @Override public void onActivityStarted(Activity activity) {}
            @Override public void onActivityStopped(Activity activity) {}
            @Override public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {}
            @Override public void onActivityDestroyed(Activity activity) {}
        });
    }

    private void checkAppReplacingState() {
        if( MyDebug.LOG )
            Log.d(TAG, "checkAppReplacingState");
        if( getResources() == null ) {
            Log.e(TAG, "app is replacing, kill");
            Process.killProcess(Process.myPid());
        }
    }
}
