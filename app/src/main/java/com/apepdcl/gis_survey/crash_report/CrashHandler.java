package com.apepdcl.gis_survey.crash_report;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

/**
 * Created by SOUBHAGYA SWAIN on 07-08-2026.
 */


public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private final Application application;
    private final Thread.UncaughtExceptionHandler defaultHandler;

    public CrashHandler(Application app) {
        application = app;
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {


        Activity activity = ActivityTracker.getCurrentActivity();
        String report = DeviceInfoUtil.generateReport(
                application,
                activity,
                throwable
        );

        Intent intent = new Intent(application, CrashActivity.class);
        intent.putExtra("report", report.toString());

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        application.startActivity(intent);

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);




    }
}
