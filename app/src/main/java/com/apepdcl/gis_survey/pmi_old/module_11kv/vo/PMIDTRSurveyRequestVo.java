package com.apepdcl.gis_survey.pmi_old.module_11kv.vo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PMIDTRSurveyRequestVo {
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
    @SerializedName("DTR_CODE")
    private String DTR_CODE;
    @SerializedName("CREATED_BY")
    private String CREATED_BY;
    @SerializedName("DTR_REQUST")
    List<PMIDTRSurveyRequestDataVo> DTR_REQUST;

    public String getDTR_CODE() {
        return DTR_CODE;
    }

    public void setDTR_CODE(String DTR_CODE) {
        this.DTR_CODE = DTR_CODE;
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

    public String getCREATED_BY() {
        return CREATED_BY;
    }

    public void setCREATED_BY(String CREATED_BY) {
        this.CREATED_BY = CREATED_BY;
    }

    public List<PMIDTRSurveyRequestDataVo> getDTR_REQUST() {
        return DTR_REQUST;
    }

    public void setDTR_REQUST(List<PMIDTRSurveyRequestDataVo> DTR_REQUST) {
        this.DTR_REQUST = DTR_REQUST;
    }
}
