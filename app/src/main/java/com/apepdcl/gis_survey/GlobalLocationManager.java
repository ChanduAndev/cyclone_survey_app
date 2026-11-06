package com.apepdcl.gis_survey;


/**
 * Created by SOUBHAGYA SWAIN on 16-04-2026.
 */


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.apepdcl.gis.app.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;


public class GlobalLocationManager {

    private static GlobalLocationManager instance;

    private Context appContext;
    private Activity currentActivity;
    private BottomSheetDialog bottomSheetDialog;
    private boolean wasLocationOff = false;

    private GlobalLocationManager() {
    }

    public static GlobalLocationManager getInstance() {
        if (instance == null) {
            instance = new GlobalLocationManager();
        }
        return instance;
    }

    public void init(Context context) {
        this.appContext = context.getApplicationContext();

        IntentFilter filter = new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION);
        appContext.registerReceiver(locationReceiver, filter);
    }

    public void setCurrentActivity(Activity activity) {
        this.currentActivity = activity;
        checkLocationStatus();
    }

    public void clearCurrentActivity(Activity activity) {
        if (this.currentActivity == activity) {
            this.currentActivity = null;
        }
    }

    private final BroadcastReceiver locationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkLocationStatus();
        }
    };

    public void checkLocationStatus() {
        if (currentActivity == null) return;

        boolean enabled = isLocationEnabled();

        if (!enabled) {
            showBottomSheet();
            wasLocationOff = true;
        } else {
            dismissBottomSheet();

            if (wasLocationOff) {
                Snackbar.make(
                        currentActivity.findViewById(android.R.id.content),
                        "Location restored",
                        Snackbar.LENGTH_SHORT
                ).show();

                wasLocationOff = false;
            }
        }
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager =
                (LocationManager) appContext.getSystemService(Context.LOCATION_SERVICE);

        boolean gps = false;
        boolean network = false;

        try {
            gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ignored) {
        }

        try {
            network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ignored) {
        }

        return gps || network;
    }

    private void showBottomSheet() {
        if (currentActivity == null) return;

        if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) {
            return;
        }

        View view = LayoutInflater.from(currentActivity)
                .inflate(R.layout.bottom_sheet_location_status, null);

        TextView tvMessage = view.findViewById(R.id.tvMessage);
        Button btnEnable = view.findViewById(R.id.btnEnable);

        tvMessage.setText("Location is turned off");

        btnEnable.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            currentActivity.startActivity(intent);
        });

        bottomSheetDialog = new BottomSheetDialog(currentActivity);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();
    }

    private void dismissBottomSheet() {
        if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) {
            bottomSheetDialog.dismiss();
        }
    }
}
