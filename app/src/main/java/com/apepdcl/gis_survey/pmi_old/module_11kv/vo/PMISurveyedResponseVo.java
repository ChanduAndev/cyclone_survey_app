package com.apepdcl.gis_survey.pmi_old.module_11kv.vo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PMISurveyedResponseVo {
    @SerializedName("STATUS")
    private String STATUS;
    @SerializedName("ERR_MSG")
    private String ERR_MSG;
    @SerializedName("SESSION_ID")
    private String SESSION_ID;
    @SerializedName("APK")
    private String APK;
    @SerializedName("RESPONSE")
    List<PMISurveyedResponseDataVo> RESPONSE;
    @SerializedName("DTR_DATA")
    private PMIGis11KvSurveyDTRVo DTR_DATA;

    public PMIGis11KvSurveyDTRVo getDTR_DATA() {
        return DTR_DATA;
    }

    public void setDTR_DATA(PMIGis11KvSurveyDTRVo DTR_DATA) {
        this.DTR_DATA = DTR_DATA;
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

    public String getSESSION_ID() {
        return SESSION_ID;
    }

    public void setSESSION_ID(String SESSION_ID) {
        this.SESSION_ID = SESSION_ID;
    }

    public String getAPK() {
        return APK;
    }

    public void setAPK(String APK) {
        this.APK = APK;
    }

    public List<PMISurveyedResponseDataVo> getRESPONSE() {
        return RESPONSE;
    }

    public void setRESPONSE(List<PMISurveyedResponseDataVo> RESPONSE) {
        this.RESPONSE = RESPONSE;
    }
}
