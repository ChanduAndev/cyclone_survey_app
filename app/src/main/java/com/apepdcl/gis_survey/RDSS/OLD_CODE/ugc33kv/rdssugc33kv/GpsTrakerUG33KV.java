package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

public class GpsTrakerUG33KV implements LocationListener {

    private final Context mContext;

    public boolean isGPSEnabled = false;

    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;

    Location location;
    double latitude;
    double longitude;

    /**
     * The minimum distance to change Updates in meters
     */
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;

    /**
     * The minimum time between updates in milliseconds
     */
    private static final long MIN_TIME_BW_UPDATES = 1;

    protected LocationManager locationManager;

    public GpsTrakerUG33KV(Context context) {
        this.mContext = context;
        getLocation();
    }



    /**
     * Function to get the user's current location
     */
    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(Context.LOCATION_SERVICE);

            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);



            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
            }

            if (isGPSEnabled == false && isNetworkEnabled == false) {
                /**
                 * no network provider is enabled
                 */
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    location = null;
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                /**
                 * if GPS Enabled get lat/long using GPS Services
                 */
                if (isGPSEnabled) {
                    location = null;
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    /**
     * Stop using GPS listener Calling this function will stop using GPS in our
     * app
     */
    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(GpsTrakerUG33KV.this);
        }
    }

    /**
     * Function to get latitude
     */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        return latitude;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }




    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

}
