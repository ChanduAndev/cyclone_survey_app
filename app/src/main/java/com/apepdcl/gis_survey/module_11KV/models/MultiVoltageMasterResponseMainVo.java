package com.apepdcl.gis_survey.module_11KV.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MultiVoltageMasterResponseMainVo {
    @SerializedName("STATUS" )
    private String STATUS;

    @SerializedName("ERR_MSG" )
    private String ERR_MSG;

    @SerializedName("COM_TYPE_SUBTYPE_VER" )
    private String COM_TYPE_SUBTYPE_VER;

    @SerializedName("APK")
    private String APK;

    @SerializedName("RESPONSE" )
    List<MULTIVOLTAGEMaster_vo> RESPONSE =null;

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

    public String getAPK() {
        return APK;
    }

    public void setAPK(String APK) {
        this.APK = APK;
    }

    public List<MULTIVOLTAGEMaster_vo> getRESPONSE() {
        return RESPONSE;
    }

    public void setRESPONSE(List<MULTIVOLTAGEMaster_vo> RESPONSE) {
        this.RESPONSE = RESPONSE;
    }
}
