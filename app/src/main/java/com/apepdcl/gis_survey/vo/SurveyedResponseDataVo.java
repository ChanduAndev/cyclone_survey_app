package com.apepdcl.gis_survey.vo;

import com.google.gson.annotations.SerializedName;

public class SurveyedResponseDataVo {
    @SerializedName("FEEDER_ID")
    private String FEEDER_ID;
    @SerializedName("POLE_NO")
    private String POLE_NO;
    @SerializedName("GLAT")
    private String GLAT;
    @SerializedName("GLONG")
    private String GLONG;


    @SerializedName("ID")
    private String ID;

    @SerializedName("POLE_TYPE")
    private String POLE_TYPE;

    @SerializedName("POLE_HEIGHT")
    private String POLE_HEIGHT;

    @SerializedName("POLE_SUPPORT")
    private String POLE_SUPPORT;

    @SerializedName("PIN_CUT")
    private String PIN_CUT;

    @SerializedName("POLE_CONCRETE")
    private String POLE_CONCRETE;

    @SerializedName("IS_DOUBLEFEEDING")
    private String IS_DOUBLEFEEDING;

    @SerializedName("INSULATOR_TYPE")
    private String INSULATOR_TYPE;

    @SerializedName("LINE_CONFIG")
    private String LINE_CONFIG;

    @SerializedName("POLE_PAINT_CODE")
    private String POLE_PAINT_CODE;


    @SerializedName("FROM_POLE")
    private int FROM_POLE;


    public int getFROM_POLE() {
        return FROM_POLE;
    }

    public void setFROM_POLE(int FROM_POLE) {
        this.FROM_POLE = FROM_POLE;
    }

    public String getPOLE_PAINT_CODE() {
        return POLE_PAINT_CODE;
    }

    public void setPOLE_PAINT_CODE(String POLE_PAINT_CODE) {
        this.POLE_PAINT_CODE = POLE_PAINT_CODE;
    }

    public String getLINE_CONFIG() {
        return LINE_CONFIG;
    }

    public void setLINE_CONFIG(String LINE_CONFIG) {
        this.LINE_CONFIG = LINE_CONFIG;
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

    public String getPIN_CUT() {
        return PIN_CUT;
    }

    public void setPIN_CUT(String PIN_CUT) {
        this.PIN_CUT = PIN_CUT;
    }

    public String getPOLE_CONCRETE() {
        return POLE_CONCRETE;
    }

    public void setPOLE_CONCRETE(String POLE_CONCRETE) {
        this.POLE_CONCRETE = POLE_CONCRETE;
    }

    public String getIS_DOUBLEFEEDING() {
        return IS_DOUBLEFEEDING;
    }

    public void setIS_DOUBLEFEEDING(String IS_DOUBLEFEEDING) {
        this.IS_DOUBLEFEEDING = IS_DOUBLEFEEDING;
    }

    public String getINSULATOR_TYPE() {
        return INSULATOR_TYPE;
    }

    public void setINSULATOR_TYPE(String INSULATOR_TYPE) {
        this.INSULATOR_TYPE = INSULATOR_TYPE;
    }




    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setFEEDER_ID(String FEEDER_ID) {
        this.FEEDER_ID = FEEDER_ID;
    }

    public String getFEEDER_ID() {
        return FEEDER_ID;
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
}

