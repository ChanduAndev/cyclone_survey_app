package com.apepdcl.gis_survey.estimates.models;

/**
 * Created by Soubhagya Swain on 13,June,2024
 */
public class WorkInvolvementModel {
    String EST_NO;
    String REF_ID;
    String WORK_TYPE;
    String SURVEY_TYPE;
    String FEEDER_NAME;
    String SS_DTR_MTR;
    String TAPPING_LOCATION;
    String SS_DTR_MTR_QTY;
    String IS_SURVEY_COMPLETED;
    String FLAG;
    String IS_33KV_SURVEY_COMPLETED;
    String IS_11KV_SURVEY_COMPLETED;
    String IS_LT_SURVEY_COMPLETED;
    String IS_11KV_SELECTED;
    String IS_33KV_SELECTED;
    String IS_LT_SELECTED;
    String FEEDER_ID;
    int STATUS;
    int FDR_COUNT;
    String SS_ID;
    String SS_NAME;
    String SS_TYPE;
    String CHECKED;
    int FDR_INDEX;
    String GIS_ID;
    String WORK_ORDER;

    String LT_CKT_CODE;

    public String getLT_CKT_CODE() {
        return LT_CKT_CODE;
    }

    public void setLT_CKT_CODE(String LT_CKT_CODE) {
        this.LT_CKT_CODE = LT_CKT_CODE;
    }

    public String getEST_NO() {
        return EST_NO;
    }

    public String getGIS_ID() {
        return GIS_ID;
    }

    public void setGIS_ID(String GIS_ID) {
        this.GIS_ID = GIS_ID;
    }

    public void setEST_NO(String EST_NO) {
        this.EST_NO = EST_NO;
    }

    public String getREF_ID() {
        return REF_ID;
    }

    public void setREF_ID(String REF_ID) {
        this.REF_ID = REF_ID;
    }

    public String getWORK_TYPE() {
        return WORK_TYPE;
    }

    public void setWORK_TYPE(String WORK_TYPE) {
        this.WORK_TYPE = WORK_TYPE;
    }

    public String getIS_11KV_SURVEY_COMPLETED() {
        return IS_11KV_SURVEY_COMPLETED;
    }

    public void setIS_11KV_SURVEY_COMPLETED(String IS_11KV_SURVEY_COMPLETED) {
        this.IS_11KV_SURVEY_COMPLETED = IS_11KV_SURVEY_COMPLETED;
    }

    public String getSURVEY_TYPE() {
        return SURVEY_TYPE;
    }

    public void setSURVEY_TYPE(String SURVEY_TYPE) {
        this.SURVEY_TYPE = SURVEY_TYPE;
    }

    public String getFEEDER_NAME() {
        return FEEDER_NAME;
    }

    public void setFEEDER_NAME(String FEEDER_NAME) {
        this.FEEDER_NAME = FEEDER_NAME;
    }

    public String getSS_DTR_MTR() {
        return SS_DTR_MTR;
    }

    public void setSS_DTR_MTR(String SS_DTR_MTR) {
        this.SS_DTR_MTR = SS_DTR_MTR;
    }

    public String getTAPPING_LOCATION() {
        return TAPPING_LOCATION;
    }

    public String getIS_33KV_SURVEY_COMPLETED() {
        return IS_33KV_SURVEY_COMPLETED;
    }

    public void setIS_33KV_SURVEY_COMPLETED(String IS_33KV_SURVEY_COMPLETED) {
        this.IS_33KV_SURVEY_COMPLETED = IS_33KV_SURVEY_COMPLETED;
    }

    public void setTAPPING_LOCATION(String TAPPING_LOCATION) {
        this.TAPPING_LOCATION = TAPPING_LOCATION;
    }

    public String getSS_DTR_MTR_QTY() {
        return SS_DTR_MTR_QTY;
    }

    public void setSS_DTR_MTR_QTY(String SS_DTR_MTR_QTY) {
        this.SS_DTR_MTR_QTY = SS_DTR_MTR_QTY;
    }

    public String getIS_SURVEY_COMPLETED() {
        return IS_SURVEY_COMPLETED;
    }

    public void setIS_SURVEY_COMPLETED(String IS_SURVEY_COMPLETED) {
        this.IS_SURVEY_COMPLETED = IS_SURVEY_COMPLETED;
    }

    public String getFLAG() {
        return FLAG;
    }

    public String getIS_11KV_SELECTED() {
        return IS_11KV_SELECTED;
    }

    public void setIS_11KV_SELECTED(String IS_11KV_SELECTED) {
        this.IS_11KV_SELECTED = IS_11KV_SELECTED;
    }

    public void setFLAG(String FLAG) {
        this.FLAG = FLAG;
    }

    public String getIS_LT_SURVEY_COMPLETED() {
        return IS_LT_SURVEY_COMPLETED;
    }

    public void setIS_LT_SURVEY_COMPLETED(String IS_LT_SURVEY_COMPLETED) {
        this.IS_LT_SURVEY_COMPLETED = IS_LT_SURVEY_COMPLETED;
    }

    public String getIS_33KV_SELECTED() {
        return IS_33KV_SELECTED;
    }

    public void setIS_33KV_SELECTED(String IS_33KV_SELECTED) {
        this.IS_33KV_SELECTED = IS_33KV_SELECTED;
    }

    public String getIS_LT_SELECTED() {
        return IS_LT_SELECTED;
    }

    public void setIS_LT_SELECTED(String IS_LT_SELECTED) {
        this.IS_LT_SELECTED = IS_LT_SELECTED;
    }

    public String getFEEDER_ID() {
        return FEEDER_ID;
    }

    public void setFEEDER_ID(String FEEDER_ID) {
        this.FEEDER_ID = FEEDER_ID;
    }

    public int getFDR_COUNT() {
        return FDR_COUNT;
    }

    public void setFDR_COUNT(int FDR_COUNT) {
        this.FDR_COUNT = FDR_COUNT;
    }

    public int getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(int STATUS) {
        this.STATUS = STATUS;
    }

    public String getSS_ID() {
        return SS_ID;
    }

    public void setSS_ID(String SS_ID) {
        this.SS_ID = SS_ID;
    }

    public String getSS_NAME() {
        return SS_NAME;
    }

    public void setSS_NAME(String SS_NAME) {
        this.SS_NAME = SS_NAME;
    }

    public String getSS_TYPE() {
        return SS_TYPE;
    }

    public void setSS_TYPE(String SS_TYPE) {
        this.SS_TYPE = SS_TYPE;
    }

    public String getCHECKED() {
        return CHECKED;
    }

    public void setCHECKED(String CHECKED) {
        this.CHECKED = CHECKED;
    }

    public int getFDR_INDEX() {
        return FDR_INDEX;
    }

    public void setFDR_INDEX(int FDR_INDEX) {
        this.FDR_INDEX = FDR_INDEX;
    }

    public String getWORK_ORDER() {
        return WORK_ORDER;
    }

    public void setWORK_ORDER(String WORK_ORDER) {
        this.WORK_ORDER = WORK_ORDER;
    }
}
