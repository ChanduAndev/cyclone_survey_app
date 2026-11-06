package com.apepdcl.gis_survey.crash_report;
import android.app.Application;
import android.content.Intent;

import com.github.anrwatchdog.ANRError;
import com.github.anrwatchdog.ANRWatchDog;


/**
 * Created by SOUBHAGYA SWAIN on 07-08-2026.
 */


public class ANRWatchDogManager {

    public static void init(Application application) {

        new ANRWatchDog(30000) // 5 seconds
                .setANRListener(error -> {

                    try {

                        String report = getANRReport(error);

                        Intent intent = new Intent(application, CrashActivity.class);
                        intent.putExtra("report", report);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        application.startActivity(intent);

                        // Give CrashActivity a moment to start
                        Thread.sleep(1000);

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(10);
                    }

                })
                .start();
    }

    private static String getANRReport(ANRError error) {

        StringBuilder sb = new StringBuilder();

        sb.append("=========== ANR DETECTED ===========\n\n");

        sb.append(error.toString());

        return sb.toString();
    }
}
