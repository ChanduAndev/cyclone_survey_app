package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class IncrementalFeederMasterResponseVoRDSSUg11Kv {
    @SerializedName("STATUS" )
    private String STATUS;
    @SerializedName("ERR_MSG" )
    private String ERR_MSG;
    @SerializedName("COM_TYPE_SUBTYPE_VER" )
    private String COM_TYPE_SUBTYPE_VER;
    @SerializedName("APK")
    private String APK;

    public String getAPK() {
        return APK;
    }

    public void setAPK(String APK) {
        this.APK = APK;
    }

    @SerializedName("RESPONSE" )
    List<IncrementalFeederMasterResponseDataVoRDSSUg11Kv> RESPONSE =null;

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

    public List<IncrementalFeederMasterResponseDataVoRDSSUg11Kv> getRESPONSE() {
        return RESPONSE;
    }

    public void setRESPONSE(List<IncrementalFeederMasterResponseDataVoRDSSUg11Kv> RESPONSE) {
        this.RESPONSE = RESPONSE;
    }
}
