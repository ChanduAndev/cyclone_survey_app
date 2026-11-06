package com.apepdcl.gis_survey.module_11KV.models;

import com.google.gson.annotations.SerializedName;

public class LastSurveyResponseDataVo {
    @SerializedName("FEEDER_ID")
    private String FEEDER_ID;
    @SerializedName("POLE_NO")
    private String POLE_NO;
    @SerializedName("GLAT")
    private String GLAT;
    @SerializedName("GLONG")
    private String GLONG;
    @SerializedName("TAPPING_LOCNAME")
    private String TAPPING_LOCNAME;
    @SerializedName("POLE_CONFIG")
    private String POLE_CONFIG;

    @SerializedName("POLE_TYPE")
    private String POLE_TYPE;

    @SerializedName("POLE_HEIGHT")
    private String POLE_HEIGHT;

    @SerializedName("POLE_SUPPORT")
    private String POLE_SUPPORT;

    @SerializedName("POLE_PAINT_CODE")
    private String POLE_PAINT_CODE;

    @SerializedName("PIN_CUT")
    private String PIN_CUT;

    @SerializedName("FROM_POLE")
    private String FROM_POLE;

    @SerializedName("Id")
    private String Id;

    @SerializedName("POLE_CONCRETE")
    private String POLE_CONCRETE;

    @SerializedName("POLE_STRUCTURE")
    private String POLE_STRUCTURE;

    public String getPOLE_CONCRETE() {
        return POLE_CONCRETE;
    }

    public void setPOLE_CONCRETE(String POLE_CONCRETE) {
        this.POLE_CONCRETE = POLE_CONCRETE;
    }

    public String getPOLE_STRUCTURE() {
        return POLE_STRUCTURE;
    }

    public void setPOLE_STRUCTURE(String POLE_STRUCTURE) {
        this.POLE_STRUCTURE = POLE_STRUCTURE;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
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

    public String getPOLE_SUPPORT() {
        return POLE_SUPPORT;
    }

    public void setPOLE_SUPPORT(String POLE_SUPPORT) {
        this.POLE_SUPPORT = POLE_SUPPORT;
    }

    public String getPOLE_PAINT_CODE() {
        return POLE_PAINT_CODE;
    }

    public void setPOLE_PAINT_CODE(String POLE_PAINT_CODE) {
        this.POLE_PAINT_CODE = POLE_PAINT_CODE;
    }

    public String getPIN_CUT() {
        return PIN_CUT;
    }

    public void setPIN_CUT(String PIN_CUT) {
        this.PIN_CUT = PIN_CUT;
    }

    public String getFROM_POLE() {
        return FROM_POLE;
    }

    public void setFROM_POLE(String FROM_POLE) {
        this.FROM_POLE = FROM_POLE;
    }

    @SerializedName("VOLTAGE_LEVEL")
    private String VOLTAGE_LEVEL;

    public String getPOLE_CONFIG() {
        return POLE_CONFIG;
    }

    public void setPOLE_CONFIG(String POLE_CONFIG) {
        this.POLE_CONFIG = POLE_CONFIG;
    }

    public String getTAPPING_LOCNAME() {
        return TAPPING_LOCNAME;
    }

    public void setTAPPING_LOCNAME(String TAPPING_LOCNAME) {
        this.TAPPING_LOCNAME = TAPPING_LOCNAME;
    }

    public String getFEEDER_ID() {
        return FEEDER_ID;
    }

    public void setFEEDER_ID(String FEEDER_ID) {
        this.FEEDER_ID = FEEDER_ID;
    }

    public String getPOLE_NO() {
        return POLE_NO;
    }

    public void setPOLE_NO(String POLE_NO) {
        this.POLE_NO = POLE_NO;
    }

    public String getGLAT() {
        return GLAT;
    }

    public void setGLAT(String GLAT) {
        this.GLAT = GLAT;
    }

    public String getGLONG() {
        return GLONG;
    }

    public void setGLONG(String GLONG) {
        this.GLONG = GLONG;
    }

    public String getVOLTAGE_LEVEL() {
        return VOLTAGE_LEVEL;
    }

    public void setVOLTAGE_LEVEL(String VOLTAGE_LEVEL) {
        this.VOLTAGE_LEVEL = VOLTAGE_LEVEL;
    }
}
