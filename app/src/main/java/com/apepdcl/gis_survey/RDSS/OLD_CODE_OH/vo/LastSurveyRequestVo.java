package com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo;

import com.google.gson.annotations.SerializedName;

public class LastSurveyRequestVo {
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
