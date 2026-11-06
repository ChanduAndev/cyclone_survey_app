package com.apepdcl.gis_survey.pmi_old.module_11kv.vo;

import com.apepdcl.gis_survey.pmi_old.module_33kv.vo.PMISSMasterVo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PMILastSurveyResponseSSDataVo {

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
    List<PMILastSurveyResponseDataVo> RESPONSE =null;
    @SerializedName("SS_MASTER" )
    List<PMISSMasterVo> SS_MASTER =null;

    public List<PMISSMasterVo> getSS_MASTER() {
        return SS_MASTER;
    }

    public void setSS_MASTER(List<PMISSMasterVo> SS_MASTER) {
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

    public List<PMILastSurveyResponseDataVo> getRESPONSE() {
        return RESPONSE;
    }

    public void setRESPONSE(List<PMILastSurveyResponseDataVo> RESPONSE) {
        this.RESPONSE = RESPONSE;
    }

}
