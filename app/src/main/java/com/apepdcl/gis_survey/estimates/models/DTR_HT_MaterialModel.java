package com.apepdcl.gis_survey.estimates.models;

import androidx.annotation.NonNull;

public class DTR_HT_MaterialModel {
    private String MATERIALCODE;
    private String MATERAIL_DESC;
    private String GROUPCODE;
    private String PHASE;
    private String CAPACITY;
    private String MATERIAL_TYPE;
    private String GROUPDESC1;
    private String UOM;


    private String SURVEY_CODE;

    public String getSURVEY_CODE() {
        return SURVEY_CODE;
    }

    public void setSURVEY_CODE(String SURVEY_CODE) {
        this.SURVEY_CODE = SURVEY_CODE;
    }

    public String getMATERIALCODE() {
        return MATERIALCODE;
    }

    public String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMATERIALCODE(String MATERIALCODE) {
        this.MATERIALCODE = MATERIALCODE;
    }

    public String getMATERAIL_DESC() {
        return MATERAIL_DESC;
    }

    public void setMATERAIL_DESC(String MATERAIL_DESC) {
        this.MATERAIL_DESC = MATERAIL_DESC;
    }

    public String getGROUPCODE() {
        return GROUPCODE;
    }

    public void setGROUPCODE(String GROUPCODE) {
        this.GROUPCODE = GROUPCODE;
    }

    public String getPHASE() {
        return PHASE;
    }

    public void setPHASE(String PHASE) {
        this.PHASE = PHASE;
    }

    public String getCAPACITY() {
        return CAPACITY;
    }

    public void setCAPACITY(String CAPACITY) {
        this.CAPACITY = CAPACITY;
    }

    public String getMATERIAL_TYPE() {
        return MATERIAL_TYPE;
    }

    public void setMATERIAL_TYPE(String MATERIAL_TYPE) {
        this.MATERIAL_TYPE = MATERIAL_TYPE;
    }

    public String getGROUPDESC1() {
        return GROUPDESC1;
    }

    public void setGROUPDESC1(String GROUPDESC1) {
        this.GROUPDESC1 = GROUPDESC1;
    }

    public String getUOM() {
        return UOM;
    }

    public void setUOM(String UOM) {
        this.UOM = UOM;
    }
    @NonNull
    @Override
    public String toString() {
        return MATERAIL_DESC;
    }
}
