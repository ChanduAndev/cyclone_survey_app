package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.utilits;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

public class CommanUtilitsUG33KV {

    public static ProgressDialog progressDialog(Context context) {
        ProgressDialog prgDialog = new ProgressDialog(context);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        return prgDialog;
    }



    public static String getIMEI_NO(Context context) {
        String uid;
        try {
            // For Android 10 and above, use ANDROID_ID instead of IMEI
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                uid = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            } else {
                TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    uid = tManager.getImei(); // Requires permission for < Q
                } else {
                    uid = tManager.getDeviceId(); // Also needs permission
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            uid = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return uid;
    }


    //    public static String getIMEI_NO(Context context) {
//        String uid = null;
//        try {
//            TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//            if (Build.VERSION.SDK_INT >= 23) {
//                //   if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                //  return false;
//                uid = tManager.getDeviceId();
//              /*  }else {
//                    uid="" ;
//                }*/
//            }else if (Build.VERSION.SDK_INT >= 26) {
//                uid=tManager.getImei();
//            }
//            else {
//                uid = tManager.getDeviceId();
//            }
//            //  uid = tManager.getDeviceId();
//        }catch (SecurityException e){
//            e.printStackTrace();
//            uid= Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
//        }
//        return uid;
//    }
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
