package com.apepdcl.gis_survey.module_11KV.models;

public class FeederDataVo {
    private String EHT_SS_CODE;
    private String EHT_SS_NAME;
    private String FEEDER_NAME_33KV;
    private String FEEDER_CODE_33KV;
    private String WO_ID;
    private String IS_FEEDER_TYPE;
    private String ALLOW_SURVEY;

    public String getALLOW_SURVEY() {
        return ALLOW_SURVEY;
    }

    public void setALLOW_SURVEY(String ALLOW_SURVEY) {
        this.ALLOW_SURVEY = ALLOW_SURVEY;
    }

    public String getIS_FEEDER_TYPE() {
        return IS_FEEDER_TYPE;
    }

    public void setIS_FEEDER_TYPE(String IS_FEEDER_TYPE) {
        this.IS_FEEDER_TYPE = IS_FEEDER_TYPE;
    }

    public String getWO_ID() {
        return WO_ID;
    }

    public void setWO_ID(String WO_ID) {
        this.WO_ID = WO_ID;
    }

    public String getEHT_SS_CODE() {
        return EHT_SS_CODE;
    }

    public void setEHT_SS_CODE(String EHT_SS_CODE) {
        this.EHT_SS_CODE = EHT_SS_CODE;
    }

    public String getEHT_SS_NAME() {
        return EHT_SS_NAME;
    }

    public void setEHT_SS_NAME(String EHT_SS_NAME) {
        this.EHT_SS_NAME = EHT_SS_NAME;
    }

    public String getFEEDER_NAME_33KV() {
        return FEEDER_NAME_33KV;
    }

    public void setFEEDER_NAME_33KV(String FEEDER_NAME_33KV) {
        this.FEEDER_NAME_33KV = FEEDER_NAME_33KV;
    }

    public String getFEEDER_CODE_33KV() {
        return FEEDER_CODE_33KV;
    }

    public void setFEEDER_CODE_33KV(String FEEDER_CODE_33KV) {
        this.FEEDER_CODE_33KV = FEEDER_CODE_33KV;
    }
}
