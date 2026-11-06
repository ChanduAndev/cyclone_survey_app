package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.runtimeperimissions;

/**
 * Created by EPDCL on 3/13/2017.
 */
public class AppDataUG33KV {
    private static AppDataUG33KV instance;
    private boolean isPermission;
    public static AppDataUG33KV getInstance() {
        if (instance == null) {
            instance = new AppDataUG33KV();
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
