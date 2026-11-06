package com.apepdcl.gis_survey.module_33KV.vo;

import com.google.gson.annotations.SerializedName;

public class TruncDataRequestVo {
    @SerializedName("FEEDER_ID")
    String FEEDER_ID;
    @SerializedName("USER_ID")
    String USER_ID;
    @SerializedName("SESSION_ID")
    String SESSION_ID;
    @SerializedName("VERSION")
    String VERSION;
    @SerializedName("IMEI_NO")
    String IMEI_NO;
    @SerializedName("TRUCK_POLE_NO")
    private int TRUCK_POLE_NO;

    public int getTRUCK_POLE_NO() {
        return TRUCK_POLE_NO;
    }

    public void setTRUCK_POLE_NO(int TRUCK_POLE_NO) {
        this.TRUCK_POLE_NO = TRUCK_POLE_NO;
    }

    public String getFEEDER_ID() {
        return FEEDER_ID;
    }

    public void setFEEDER_ID(String FEEDER_ID) {
        this.FEEDER_ID = FEEDER_ID;
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

    public String getVERSION() {
        return VERSION;
    }

    public void setVERSION(String VERSION) {
        this.VERSION = VERSION;
    }

    public String getIMEI_NO() {
        return IMEI_NO;
    }

    public void setIMEI_NO(String IMEI_NO) {
        this.IMEI_NO = IMEI_NO;
    }
}
