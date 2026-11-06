package com.apepdcl.gis_survey.vo;

import com.google.gson.annotations.SerializedName;

public class LTWoLockReqVo {


   @SerializedName("FEEDER_ID")
    private String FEEDER_ID;

    @SerializedName("LT_DTR_CODE")
    private String LT_DTR_CODE;

    @SerializedName("LT_DTR_CKT_CODE")
    private String LT_DTR_CKT_CODE;
    @SerializedName("WO_ID")
    private String WO_ID;

    @SerializedName("WO_STATUS")
    private String WO_STATUS;

    @SerializedName("UPDATED_BY")
    private String UPDATED_BY;

    public String getFEEDER_ID() {
        return FEEDER_ID;
    }

    public void setFEEDER_ID(String FEEDER_ID) {
        this.FEEDER_ID = FEEDER_ID;
    }

    public String getLT_DTR_CODE() {
        return LT_DTR_CODE;
    }

    public void setLT_DTR_CODE(String LT_DTR_CODE) {
        this.LT_DTR_CODE = LT_DTR_CODE;
    }

    public String getLT_DTR_CKT_CODE() {
        return LT_DTR_CKT_CODE;
    }

    public void setLT_DTR_CKT_CODE(String LT_DTR_CKT_CODE) {
        this.LT_DTR_CKT_CODE = LT_DTR_CKT_CODE;
    }

    public String getWO_ID() {
        return WO_ID;
    }

    public void setWO_ID(String WO_ID) {
        this.WO_ID = WO_ID;
    }

    public String getWO_STATUS() {
        return WO_STATUS;
    }

    public void setWO_STATUS(String WO_STATUS) {
        this.WO_STATUS = WO_STATUS;
    }

    public String getUPDATED_BY() {
        return UPDATED_BY;
    }

    public void setUPDATED_BY(String UPDATED_BY) {
        this.UPDATED_BY = UPDATED_BY;
    }
}
