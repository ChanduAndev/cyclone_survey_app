package com.apepdcl.gis_survey.module_33KV.vo;


import com.google.gson.annotations.SerializedName;

public class LoginResponseVo {
    @SerializedName("STATUS")
    private String STATUS;
    @SerializedName("ERR_MSG")
    private String ERR_MSG;
    @SerializedName("COM_TYPE_SUBTYPE_VER")
    private String COM_TYPE_SUBTYPE_VER;
    @SerializedName("RESPONSE")
    private LoginDataResponse RESPONSE;
    @SerializedName("SESSION_ID")
    private String SESSION_ID;
    @SerializedName("APK")
    private String APK;

    public String getAPK() {
        return APK;
    }

    public void setAPK(String APK) {
        this.APK = APK;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getERR_MSG() {
        return ERR_MSG;
    }

    public void setERR_MSG(String ERR_MSG) {
        this.ERR_MSG = ERR_MSG;
    }

    public String getCOM_TYPE_SUBTYPE_VER() {
        return COM_TYPE_SUBTYPE_VER;
    }

    public void setCOM_TYPE_SUBTYPE_VER(String COM_TYPE_SUBTYPE_VER) {
        this.COM_TYPE_SUBTYPE_VER = COM_TYPE_SUBTYPE_VER;
    }

    public LoginDataResponse getRESPONSE() {
        return RESPONSE;
    }

    public void setRESPONSE(LoginDataResponse RESPONSE) {
        this.RESPONSE = RESPONSE;
    }

    public String getSESSION_ID() {
        return SESSION_ID;
    }

    public void setSESSION_ID(String SESSION_ID) {
        this.SESSION_ID = SESSION_ID;
    }
}
