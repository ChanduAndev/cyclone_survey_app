package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.runtimeperimissions;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by balachandramouli.t on 3/14/2016.
 */
public class ConnectivityCheckingUG33KV {
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


}
