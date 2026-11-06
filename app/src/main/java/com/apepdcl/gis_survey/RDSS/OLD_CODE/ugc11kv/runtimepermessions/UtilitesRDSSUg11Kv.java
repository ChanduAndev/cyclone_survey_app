package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.runtimepermessions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;

/**
 * Created by EPDCL on 3/13/2017.
 */
public class UtilitesRDSSUg11Kv {
    public static boolean isStoragePermissionGranted(final Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(Manifest.permission.INTERNET)
                    == PackageManager.PERMISSION_GRANTED && context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED &&  context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED &&  context.checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE)
                    == PackageManager.PERMISSION_GRANTED &&  context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
              AppDataRDSSUg11Kv.getInstance().setPermission(true);
                ///  UnfoldPrintsLog.v("TAG", "Permission is granted");

                return true;
            } else {
                AppDataRDSSUg11Kv.getInstance().setPermission(false);
                //  UnfoldPrintsLog.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions((Activity) context, new String[]
                        {Manifest.permission.INTERNET,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_NETWORK_STATE,
                                Manifest.permission.READ_PHONE_STATE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            //UnfoldPrintsLog.v("TAG", "Permission is granted");
            AppDataRDSSUg11Kv.getInstance().setPermission(true);
            return true;
        }
    }
}
