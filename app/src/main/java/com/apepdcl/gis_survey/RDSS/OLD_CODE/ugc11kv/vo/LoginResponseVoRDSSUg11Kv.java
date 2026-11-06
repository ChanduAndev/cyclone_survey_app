package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo;


import com.google.gson.annotations.SerializedName;

public class LoginResponseVoRDSSUg11Kv {
    @SerializedName("STATUS")
    private String STATUS;
    @SerializedName("ERR_MSG")
    private String ERR_MSG;
    @SerializedName("COM_TYPE_SUBTYPE_VER")
    private String COM_TYPE_SUBTYPE_VER;
    @SerializedName("RESPONSE")
    private LoginDataResponseRDSSUg11Kv RESPONSE;
    @SerializedName("SESSION_ID")
    private String SESSION_ID;
    @SerializedName("APK")
    private String APK;

    public String getSEC_CODE() {
        return SEC_CODE;
    }

    public void setSEC_CODE(String SEC_CODE) {
        this.SEC_CODE = SEC_CODE;
    }

    @SerializedName("SEC_CODE")
    private String SEC_CODE;

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

    public LoginDataResponseRDSSUg11Kv getRESPONSE() {
        return RESPONSE;
    }

    public void setRESPONSE(LoginDataResponseRDSSUg11Kv RESPONSE) {
        this.RESPONSE = RESPONSE;
    }

    public String getSESSION_ID() {
        return SESSION_ID;
    }

    public void setSESSION_ID(String SESSION_ID) {
        this.SESSION_ID = SESSION_ID;
    }
}
