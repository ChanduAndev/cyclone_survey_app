package com.apepdcl.gis_survey.vo;

/**
 * Created by Soubhagya Swain on 12,April,2024
 */
public class MultiCktVO {

    private  String VOLTAGE_LEVEL;
    private  String MC_FEEDER_ID ;
    private  String MC_FEEDER_NAME ;
    private  String MC_POLE_TYPE ;
    private  String MC_POLE_HEIGHT;
    private  int MC_POLE_NO;
    private  String GLAT;
    private  String GLON;
    private  String SHOW_NAME;

    public String getSHOW_NAME() {
        return SHOW_NAME;
    }

    public void setSHOW_NAME(String SHOW_NAME) {
        this.SHOW_NAME = SHOW_NAME;
    }

    public String getGLAT() {
        return GLAT;
    }

    public void setGLAT(String GLAT) {
        this.GLAT = GLAT;
    }

    public String getGLON() {
        return GLON;
    }

    public void setGLON(String GLON) {
        this.GLON = GLON;
    }

    public String getVOLTAGE_LEVEL() {
        return VOLTAGE_LEVEL;
    }

    public void setVOLTAGE_LEVEL(String VOLTAGE_LEVEL) {
        this.VOLTAGE_LEVEL = VOLTAGE_LEVEL;
    }

    public String getMC_FEEDER_ID() {
        return MC_FEEDER_ID;
    }

    public void setMC_FEEDER_ID(String MC_FEEDER_ID) {
        this.MC_FEEDER_ID = MC_FEEDER_ID;
    }

    public String getMC_FEEDER_NAME() {
        return MC_FEEDER_NAME;
    }

    public void setMC_FEEDER_NAME(String MC_FEEDER_NAME) {
        this.MC_FEEDER_NAME = MC_FEEDER_NAME;
    }

    public String getMC_POLE_TYPE() {
        return MC_POLE_TYPE;
    }

    public void setMC_POLE_TYPE(String MC_POLE_TYPE) {
        this.MC_POLE_TYPE = MC_POLE_TYPE;
    }

    public String getMC_POLE_HEIGHT() {
        return MC_POLE_HEIGHT;
    }

    public void setMC_POLE_HEIGHT(String MC_POLE_HEIGHT) {
        this.MC_POLE_HEIGHT = MC_POLE_HEIGHT;
    }

    public int getMC_POLE_NO() {
        return MC_POLE_NO;
    }

    public void setMC_POLE_NO(int MC_POLE_NO) {
        this.MC_POLE_NO = MC_POLE_NO;
    }
}
