package com.apepdcl.gis_survey.runtimeperimissions;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

/**
 * Created by balachandramouli.t on 3/14/2016.
 */
public class ConnectivityChecking {
    public static boolean checkConnection(Context cont)
    {
        ConnectivityManager connMgr = (ConnectivityManager)cont.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }else
        {

            return false;
        }

    }

    public static String getIMEI_NO(Context context) {
        String uid = null;
        try {
            TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= 23) {
                //   if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                //  return false;
                uid = tManager.getDeviceId();
              /*  }else {
                    uid="" ;
                }*/
            }else if (Build.VERSION.SDK_INT >= 26) {
                uid=tManager.getImei();
            }
            else {
                uid = tManager.getDeviceId();
            }
            //  uid = tManager.getDeviceId();
        }catch (SecurityException e){
            e.printStackTrace();
            uid= Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return uid;
    }
}
