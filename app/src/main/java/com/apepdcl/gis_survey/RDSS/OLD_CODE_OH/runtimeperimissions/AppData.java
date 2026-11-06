package com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.runtimeperimissions;

/**
 * Created by EPDCL on 3/13/2017.
 */
public class AppData {
    private static AppData instance;
    private boolean isPermission;
    public static AppData getInstance() {
        if (instance == null) {
            instance = new AppData();
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
