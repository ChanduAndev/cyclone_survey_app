package com.apepdcl.gis_survey.vo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class IncrementalLTSurveyRequestVo {
    @SerializedName("IMEI_NO")
    private String IMEI_NO;
    @SerializedName("VERSION")
    private String VERSION;
    @SerializedName("SEC_CODE")
    private String SEC_CODE;
    @SerializedName("OFFICE_ID")
    private String OFFICE_ID;
    @SerializedName("FEEDER_ID")
    private String FEEDER_ID;
    @SerializedName("CREATED_BY")
    private String CREATED_BY;
    @SerializedName("LT_REQUST")
    List<IncrementalLTSurveyRequestDataVo> LT_REQUST;

    @SerializedName("WORK_ORDER")
    List<LTWoLockReqVo> WORK_ORDER;

    public List<LTWoLockReqVo> getWORK_ORDER() {
        return WORK_ORDER;
    }

    public void setWORK_ORDER(List<LTWoLockReqVo> WORK_ORDER) {
        this.WORK_ORDER = WORK_ORDER;
    }

    public String getCREATED_BY() {
        return CREATED_BY;
    }

    public void setCREATED_BY(String CREATED_BY) {
        this.CREATED_BY = CREATED_BY;
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

    public String getSEC_CODE() {
        return SEC_CODE;
    }

    public void setSEC_CODE(String SEC_CODE) {
        this.SEC_CODE = SEC_CODE;
    }

    public String getOFFICE_ID() {
        return OFFICE_ID;
    }

    public void setOFFICE_ID(String OFFICE_ID) {
        this.OFFICE_ID = OFFICE_ID;
    }

    public String getFEEDER_ID() {
        return FEEDER_ID;
    }

    public void setFEEDER_ID(String FEEDER_ID) {
        this.FEEDER_ID = FEEDER_ID;
    }

    public List<IncrementalLTSurveyRequestDataVo> getLT_REQUST() {
        return LT_REQUST;
    }

    public void setLT_REQUST(List<IncrementalLTSurveyRequestDataVo> LT_REQUST) {
        this.LT_REQUST = LT_REQUST;
    }
}
