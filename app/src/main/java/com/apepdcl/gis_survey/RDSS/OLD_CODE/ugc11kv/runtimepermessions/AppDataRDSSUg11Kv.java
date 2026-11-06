package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.runtimepermessions;

/**
 * Created by EPDCL on 3/13/2017.
 */
public class AppDataRDSSUg11Kv {
    private static AppDataRDSSUg11Kv instance;
    private boolean isPermission;
    public static AppDataRDSSUg11Kv getInstance() {
        if (instance == null) {
            instance = new AppDataRDSSUg11Kv();
        }
        return instance;
    }


    public boolean isPermission() {
        return isPermission;
    }

    public void setPermission(boolean permission) {
        isPermission = permission;
    }

}
