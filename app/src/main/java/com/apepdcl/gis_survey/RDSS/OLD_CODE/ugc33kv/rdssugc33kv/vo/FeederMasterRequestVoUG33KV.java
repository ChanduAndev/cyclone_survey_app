package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo;

import com.google.gson.annotations.SerializedName;

public class FeederMasterRequestVoUG33KV {
    @SerializedName("IMEI_NO")
    private String IMEI_NO;
    @SerializedName("VERSION")
    private String VERSION;
    @SerializedName("USER_ID")
    private String USER_ID;
    @SerializedName("SESSION_ID")
    private String SESSION_ID;

    public String getIMEI_NO() {
        return IMEI_NO;
    }

    public void setIMEI_NO(String IMEI_NO) {
        this.IMEI_NO = IMEI_NO;
    }

    public String getVERSION() {
        return VERSION;
    }

    public void setVERSION(String VERSION) {
        this.VERSION = VERSION;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getSESSION_ID() {
        return SESSION_ID;
    }

    public void setSESSION_ID(String SESSION_ID) {
        this.SESSION_ID = SESSION_ID;
    }
}
