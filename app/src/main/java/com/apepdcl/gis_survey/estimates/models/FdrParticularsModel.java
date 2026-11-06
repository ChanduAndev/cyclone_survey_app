package com.apepdcl.gis_survey.estimates.models;

import java.io.Serializable;

public class FdrParticularsModel implements Serializable {
    private String FEEDER;
    private String VOLTAGE_LEVEL;
    private String SURVEY_TYPE;
    private String SS_NAME;
    private int FDR_INDEX;
    private int FDR_COUNT;
    private String LT_CKT_CODE;
    private int POLE_COUNT;

    public int getPOLE_COUNT() {
        return POLE_COUNT;
    }

    public void setPOLE_COUNT(int POLE_COUNT) {
        this.POLE_COUNT = POLE_COUNT;
    }

    private String LONGITUDE;
    private String LATITUDE;

    public String getLONGITUDE() {
        return LONGITUDE;
    }

    public void setLONGITUDE(String LONGITUDE) {
        this.LONGITUDE = LONGITUDE;
    }

    public String getLATITUDE() {
        return LATITUDE;
    }

    public void setLATITUDE(String LATITUDE) {
        this.LATITUDE = LATITUDE;
    }

    public String getLT_CKT_CODE() {
        return LT_CKT_CODE;
    }

    public void setLT_CKT_CODE(String LT_CKT_CODE) {
        this.LT_CKT_CODE = LT_CKT_CODE;
    }

    public String getFEEDER() {
        return FEEDER;
    }

    public void setFEEDER(String FEEDER) {
        this.FEEDER = FEEDER;
    }

    public String getVOLTAGE_LEVEL() {
        return VOLTAGE_LEVEL;
    }

    public String getSURVEY_TYPE() {
        return SURVEY_TYPE;
    }

    public void setSURVEY_TYPE(String SURVEY_TYPE) {
        this.SURVEY_TYPE = SURVEY_TYPE;
    }

    public void setVOLTAGE_LEVEL(String VOLTAGE_LEVEL) {
        this.VOLTAGE_LEVEL = VOLTAGE_LEVEL;
    }

    public String getSS_NAME() {
        return SS_NAME;
    }

    public void setSS_NAME(String SS_NAME) {
        this.SS_NAME = SS_NAME;
    }

    public int getFDR_INDEX() {
        return FDR_INDEX;
    }

    public void setFDR_INDEX(int FDR_INDEX) {
        this.FDR_INDEX = FDR_INDEX;
    }

    public int getFDR_COUNT() {
        return FDR_COUNT;
    }

    public void setFDR_COUNT(int FDR_COUNT) {
        this.FDR_COUNT = FDR_COUNT;
    }
}
