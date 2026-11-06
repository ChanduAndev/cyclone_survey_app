package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LastSurveyResponseSSDataVoRDSSUg11Kv {
    @SerializedName("STATUS" )
    private String STATUS;
    @SerializedName("ERR_MSG" )
    private String ERR_MSG;
    @SerializedName("COM_TYPE_SUBTYPE_VER" )
    private String COM_TYPE_SUBTYPE_VER;
    @SerializedName("SESSION_ID")
    String SESSION_ID;
    @SerializedName("APK")
    private String APK;
    @SerializedName("RESPONSE" )
    List<LastSurveyResponseDataVoRDSSUg11Kv> RESPONSE =null;
    @SerializedName("SS_MASTER" )
    List<SSMasterVoRDSSUg11Kv> SS_MASTER =null;

    public List<SSMasterVoRDSSUg11Kv> getSS_MASTER() {
        return SS_MASTER;
    }

    public void setSS_MASTER(List<SSMasterVoRDSSUg11Kv> SS_MASTER) {
        this.SS_MASTER = SS_MASTER;
    }

    public String getAPK() {
        return APK;
    }

    public void setAPK(String APK) {
        this.APK = APK;
    }

    public String getSESSION_ID() {
        return SESSION_ID;
    }

    public void setSESSION_ID(String SESSION_ID) {
        this.SESSION_ID = SESSION_ID;
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

    public List<LastSurveyResponseDataVoRDSSUg11Kv> getRESPONSE() {
        return RESPONSE;
    }

    public void setRESPONSE(List<LastSurveyResponseDataVoRDSSUg11Kv> RESPONSE) {
        this.RESPONSE = RESPONSE;
    }
}
