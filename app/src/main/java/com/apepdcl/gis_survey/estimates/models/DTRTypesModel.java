package com.apepdcl.gis_survey.estimates.models;

import androidx.annotation.NonNull;

public class DTRTypesModel {
    private String MATERIALPART1;
    private String MATERIALCODE;
    private String GROUPDESC1;
    private String MATERIAL_TYPE;
    private String GROUPCODE;
    private String UOM;
    private String CAPACITY;
    private String PHASE;

    public String getMATERIALPART1() {
        return MATERIALPART1;
    }

    public void setMATERIALPART1(String MATERIALPART1) {
        this.MATERIALPART1 = MATERIALPART1;
    }

    public String getMATERIALCODE() {
        return MATERIALCODE;
    }

    public void setMATERIALCODE(String MATERIALCODE) {
        this.MATERIALCODE = MATERIALCODE;
    }

    public String getGROUPDESC1() {
        return GROUPDESC1;
    }

    public void setGROUPDESC1(String GROUPDESC1) {
        this.GROUPDESC1 = GROUPDESC1;
    }

    public String getMATERIAL_TYPE() {
        return MATERIAL_TYPE;
    }

    public void setMATERIAL_TYPE(String MATERIAL_TYPE) {
        this.MATERIAL_TYPE = MATERIAL_TYPE;
    }

    public String getGROUPCODE() {
        return GROUPCODE;
    }

    public void setGROUPCODE(String GROUPCODE) {
        this.GROUPCODE = GROUPCODE;
    }

    public String getUOM() {
        return UOM;
    }

    public void setUOM(String UOM) {
        this.UOM = UOM;
    }

    public String getCAPACITY() {
        return CAPACITY;
    }

    public void setCAPACITY(String CAPACITY) {
        this.CAPACITY = CAPACITY;
    }

    public String getPHASE() {
        return PHASE;
    }

    public void setPHASE(String PHASE) {
        this.PHASE = PHASE;
    }

    @NonNull
    @Override
    public String toString() {
        return MATERIALPART1;
    }
}
