package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

public class CommanUtilitsRDSSUg11Kv {

    public static ProgressDialog progressDialog(Context context) {
        ProgressDialog prgDialog = new ProgressDialog(context);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        return prgDialog;
    }

    public static String getIMEI_NO(Context context) {
        String uid = null;
        try {
            TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= 23) {
                //   if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                //  return false;
              //  uid = tManager.getDeviceId();
                uid =   Settings.Secure.getString(context.getContentResolver(), "android_id");
              /*  }else {
                    uid="" ;
                }*/
            }else if (Build.VERSION.SDK_INT >= 26) {
                uid =   Settings.Secure.getString(context.getContentResolver(), "android_id");
            }
         /*   else {
                uid = tManager.getDeviceId();

            }*/
             else {
                uid =   Settings.Secure.getString(context.getContentResolver(), "android_id");
            }
            //  uid = tManager.getDeviceId();
        }catch (SecurityException e){
            e.printStackTrace();
            uid =   Settings.Secure.getString(context.getContentResolver(), "android_id");
        }
        return uid;
    }

    public static String isChecking(String tempcond) {

        String tempcond1 = null;
        try {
            tempcond1 = tempcond==null ||tempcond.equals("null") || tempcond.equals("") ? "" : tempcond.toUpperCase();

            return tempcond1;
        } catch (Exception e) {
            tempcond1 = "";
            return tempcond1;
        }
    }

}
