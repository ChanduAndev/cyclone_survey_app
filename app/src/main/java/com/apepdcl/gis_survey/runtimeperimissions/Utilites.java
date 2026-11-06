package com.apepdcl.gis_survey.runtimeperimissions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;

/**
 * Created by EPDCL on 3/13/2017.
 */
public class Utilites {
    public static boolean isStoragePermissionGranted(final Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(Manifest.permission.INTERNET)
                    /*== PackageManager.PERMISSION_GRANTED && context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)*/
                    == PackageManager.PERMISSION_GRANTED && context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED &&  context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED &&  context.checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE)
                    == PackageManager.PERMISSION_GRANTED &&  context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED &&  context.checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                AppData.getInstance().setPermission(true);
                ///  UnfoldPrintsLog.v("TAG", "Permission is granted");

                return true;
            } else {
                AppData.getInstance().setPermission(false);
                //  UnfoldPrintsLog.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions((Activity) context, new String[]
                        {Manifest.permission.INTERNET,
                                /*Manifest.permission.WRITE_EXTERNAL_STORAGE,*/
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_NETWORK_STATE,
                                Manifest.permission.CAMERA,
                                Manifest.permission.READ_PHONE_STATE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            //UnfoldPrintsLog.v("TAG", "Permission is granted");
            AppData.getInstance().setPermission(true);
            return true;
        }
    }
}
