package com.apepdcl.gis_survey.estimates.models;

import androidx.annotation.NonNull;

public class MaterialMasterModel {
    private String MATERIALPART1;
    private String MATERIALCODE;
    private String GROUPDESC1;
    private String MATERIAL_TYPE;
    private String GROUPCODE;
    private String UOM;
    private String VOLTAGE_LEVEL;
    private String HT_11KV;
    private String HT_33KV;
    private String LT;
    private String MATERIAL;
    private String POLE_TYPE;
    private String POLE_HEIGHT;
    private String MT_FLAG;


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

    public String getVOLTAGE_LEVEL() {
        return VOLTAGE_LEVEL;
    }

    public void setVOLTAGE_LEVEL(String VOLTAGE_LEVEL) {
        this.VOLTAGE_LEVEL = VOLTAGE_LEVEL;
    }

    public String getHT_11KV() {
        return HT_11KV;
    }

    public void setHT_11KV(String HT_11KV) {
        this.HT_11KV = HT_11KV;
    }

    public String getHT_33KV() {
        return HT_33KV;
    }

    public void setHT_33KV(String HT_33KV) {
        this.HT_33KV = HT_33KV;
    }

    public String getLT() {
        return LT;
    }

    public void setLT(String LT) {
        this.LT = LT;
    }

    public String getMATERIAL() {
        return MATERIAL;
    }

    public void setMATERIAL(String MATERIAL) {
        this.MATERIAL = MATERIAL;
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

    public String getMT_FLAG() {
        return MT_FLAG;
    }

    public void setMT_FLAG(String MT_FLAG) {
        this.MT_FLAG = MT_FLAG;
    }

    @NonNull
    @Override
    public String toString() {
        return MATERIALPART1;
    }
}
