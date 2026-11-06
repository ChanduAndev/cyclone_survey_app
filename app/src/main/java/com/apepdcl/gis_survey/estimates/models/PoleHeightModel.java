package com.apepdcl.gis_survey.estimates.models;

import androidx.annotation.NonNull;

public class PoleHeightModel {
    private  String MATERIALPART1 ;
    private  String MATERIALCODE;
    private  String GROUPDESC1;
    private  String MATERIAL_TYPE;
    private  String GROUPCODE;
    private  String UOM;
    private  String POLE_TYPE;
    private  String POLE_HEIGHT;

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

    public String getPOLE_TYPE() {
        return POLE_TYPE;
    }

    public void setPOLE_TYPE(String POLE_TYPE) {
        this.POLE_TYPE = POLE_TYPE;
    }

    public String getPOLE_HEIGHT() {
        return POLE_HEIGHT;
    }

    public void setPOLE_HEIGHT(String POLE_HEIGHT) {
        this.POLE_HEIGHT = POLE_HEIGHT;
    }

    @NonNull
    @Override
    public String toString() {
        return MATERIALPART1;
    }
}
