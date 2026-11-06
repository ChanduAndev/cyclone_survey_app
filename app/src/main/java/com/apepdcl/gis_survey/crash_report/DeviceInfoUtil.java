package com.apepdcl.gis_survey.crash_report;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by SOUBHAGYA SWAIN on 07-08-2026.
 */




public class DeviceInfoUtil {

    public static String generateReport(Context context,
                                        Activity currentActivity,
                                        Throwable throwable) {

        StringBuilder report = new StringBuilder();

        report.append("==============================\n");
        report.append("        CRASH REPORT\n");
        report.append("==============================\n\n");

        report.append(getDeviceInfo());
        report.append(getMemoryInfo(context));
        report.append(getStorageInfo());
        report.append(getCurrentScreen(currentActivity));
        report.append(getStackTrace(throwable));

        return report.toString();
    }

    //======================================================
    // Device Info
    //======================================================

    private static String getDeviceInfo() {

        StringBuilder sb = new StringBuilder();

        sb.append("DEVICE INFORMATION\n");
        sb.append("------------------------------\n");

        sb.append("Manufacturer : ").append(Build.MANUFACTURER).append("\n");
        sb.append("Brand        : ").append(Build.BRAND).append("\n");
        sb.append("Model        : ").append(Build.MODEL).append("\n");
        sb.append("Android      : ").append(Build.VERSION.RELEASE).append("\n");
        sb.append("SDK          : ").append(Build.VERSION.SDK_INT).append("\n\n");

        return sb.toString();
    }

    //======================================================
    // RAM + Heap
    //======================================================

    private static String getMemoryInfo(Context context) {

        StringBuilder sb = new StringBuilder();

        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        ActivityManager.MemoryInfo memoryInfo =
                new ActivityManager.MemoryInfo();

        activityManager.getMemoryInfo(memoryInfo);

        Runtime runtime = Runtime.getRuntime();

        long maxHeap = runtime.maxMemory();
        long totalHeap = runtime.totalMemory();
        long freeHeap = runtime.freeMemory();
        long usedHeap = totalHeap - freeHeap;

        sb.append("MEMORY INFORMATION\n");
        sb.append("------------------------------\n");

        sb.append("Available RAM : ")
                .append(formatSize(memoryInfo.availMem)).append("\n");

        sb.append("Total RAM     : ")
                .append(formatSize(memoryInfo.totalMem)).append("\n");

        sb.append("Low Memory    : ")
                .append(memoryInfo.lowMemory).append("\n");

        sb.append("Heap Used     : ")
                .append(formatSize(usedHeap)).append("\n");

        sb.append("Heap Free     : ")
                .append(formatSize(freeHeap)).append("\n");

        sb.append("Heap Max      : ")
                .append(formatSize(maxHeap)).append("\n");

        sb.append("Memory Class  : ")
                .append(activityManager.getMemoryClass())
                .append(" MB\n");

        sb.append("Large Heap    : ")
                .append(activityManager.getLargeMemoryClass())
                .append(" MB\n");

        sb.append("\n");

        return sb.toString();
    }

    //======================================================
    // Storage
    //======================================================

    private static String getStorageInfo() {

        StatFs statFs =
                new StatFs(Environment.getDataDirectory().getPath());

        long total =
                statFs.getTotalBytes();

        long free =
                statFs.getAvailableBytes();

        long used =
                total - free;

        StringBuilder sb = new StringBuilder();

        sb.append("STORAGE INFORMATION\n");
        sb.append("------------------------------\n");

        sb.append("Total Storage : ")
                .append(formatSize(total)).append("\n");

        sb.append("Used Storage  : ")
                .append(formatSize(used)).append("\n");

        sb.append("Free Storage  : ")
                .append(formatSize(free)).append("\n\n");

        return sb.toString();
    }

    //======================================================
    // Current Screen
    //======================================================

    private static String getCurrentScreen(Activity activity) {

        StringBuilder sb = new StringBuilder();

        sb.append("CURRENT SCREEN\n");
        sb.append("------------------------------\n");

        if (activity != null) {
            sb.append(activity.getClass().getName()).append("\n\n");
        } else {
            sb.append("Unknown\n\n");
        }

        return sb.toString();
    }

    //======================================================
    // Full StackTrace
    //======================================================

    private static String getStackTrace(Throwable throwable) {

        StringBuilder sb = new StringBuilder();

        sb.append("STACK TRACE\n");
        sb.append("------------------------------\n");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        throwable.printStackTrace(pw);

        Throwable cause = throwable.getCause();

        while (cause != null) {

            pw.println("\n\nCaused By:\n");

            cause.printStackTrace(pw);

            cause = cause.getCause();
        }

        sb.append(sw.toString());

        return sb.toString();
    }

    //======================================================

    private static String formatSize(long size) {

        if (size <= 0) return "0";

        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};

        int digitGroups =
                (int) (Math.log10(size) / Math.log10(1024));

        return String.format("%.2f %s",
                size / Math.pow(1024, digitGroups),
                units[digitGroups]);
    }

}
