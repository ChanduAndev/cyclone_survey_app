package com.apepdcl.gis_survey.vo;

import com.google.gson.annotations.SerializedName;

public class IncrementalSurveyedDataReqVo {

    @SerializedName("IMEI_NO")
    private String IMEI_NO;
    @SerializedName("VERSION")
    private String VERSION;
    @SerializedName("SESSION_ID")
    private String SESSION_ID;
    @SerializedName("DTR_STRUCTURE")
    private String DTR_STRUCTURE;
    @SerializedName("LT_CKTCODE")
    private String LT_CKTCODE;
    @SerializedName("SEC_CODE")
    private String SEC_CODE;

    @SerializedName("POLE_NO")
    private String POLE_NO;
    @SerializedName("WO_ID")
    private String WO_ID;

    public String getWO_ID() {
        return WO_ID;
    }

    public void setWO_ID(String WO_ID) {
        this.WO_ID = WO_ID;
    }

    public String getPOLE_NO() {
        return POLE_NO;
    }

    public void setPOLE_NO(String POLE_NO) {
        this.POLE_NO = POLE_NO;
    }

    public String getSEC_CODE() {
        return SEC_CODE;
    }

    public void setSEC_CODE(String SEC_CODE) {
        this.SEC_CODE = SEC_CODE;
    }

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

    public String getSESSION_ID() {
        return SESSION_ID;
    }

    public void setSESSION_ID(String SESSION_ID) {
        this.SESSION_ID = SESSION_ID;
    }

    public String getDTR_STRUCTURE() {
        return DTR_STRUCTURE;
    }

    public void setDTR_STRUCTURE(String DTR_STRUCTURE) {
        this.DTR_STRUCTURE = DTR_STRUCTURE;
    }

    public String getLT_CKTCODE() {
        return LT_CKTCODE;
    }

    public void setLT_CKTCODE(String LT_CKTCODE) {
        this.LT_CKTCODE = LT_CKTCODE;
    }
}
