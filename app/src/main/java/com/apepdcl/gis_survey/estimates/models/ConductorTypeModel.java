package com.apepdcl.gis_survey.estimates.models;

import androidx.annotation.NonNull;

public class ConductorTypeModel {
    private String MATERIALPART1;
    private String MATERIALCODE;
    private String GROUPDESC1;
    private String MATERIAL_TYPE;
    private String GROUPCODE;
    private String UOM;
    private String CABLE_TYPE;
    private String CORE;
    private String CABLE_SIZE;
    private String VOLTAGE_LEVEL;
    private String HT_11KV;
    private String HT_33KV;
    private String LT;

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

    public String getCABLE_TYPE() {
        return CABLE_TYPE;
    }

    public void setCABLE_TYPE(String CABLE_TYPE) {
        this.CABLE_TYPE = CABLE_TYPE;
    }

    public String getCORE() {
        return CORE;
    }

    public void setCORE(String CORE) {
        this.CORE = CORE;
    }

    public String getCABLE_SIZE() {
        return CABLE_SIZE;
    }

    public void setCABLE_SIZE(String CABLE_SIZE) {
        this.CABLE_SIZE = CABLE_SIZE;
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

    public String getLT() {
        return LT;
    }

    public void setLT(String LT) {
        this.LT = LT;
    }

    public String getHT_33KV() {
        return HT_33KV;
    }

    public void setHT_33KV(String HT_33KV) {
        this.HT_33KV = HT_33KV;
    }

    @NonNull
    @Override
    public String toString() {
        return MATERIALPART1;
    }
}
