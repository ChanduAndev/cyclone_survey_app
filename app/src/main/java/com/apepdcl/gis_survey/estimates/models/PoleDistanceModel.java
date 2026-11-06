package com.apepdcl.gis_survey.estimates.models;

public class PoleDistanceModel {
    private String POLE_TYPE;
    private String HEIGHT;
    private int MAX_DISTANCE;
    private String VOLTAGE;
    private String CROSSING;
    private String LINE_TYPE;

    public String getPOLE_TYPE() {
        return POLE_TYPE;
    }

    public void setPOLE_TYPE(String POLE_TYPE) {
        this.POLE_TYPE = POLE_TYPE;
    }

    public String getHEIGHT() {
        return HEIGHT;
    }

    public void setHEIGHT(String HEIGHT) {
        this.HEIGHT = HEIGHT;
    }

    public int getMAX_DISTANCE() {
        return MAX_DISTANCE;
    }

    public void setMAX_DISTANCE(int MAX_DISTANCE) {
        this.MAX_DISTANCE = MAX_DISTANCE;
    }

    public String getVOLTAGE() {
        return VOLTAGE;
    }

    public void setVOLTAGE(String VOLTAGE) {
        this.VOLTAGE = VOLTAGE;
    }

    public String getCROSSING() {
        return CROSSING;
    }

    public void setCROSSING(String CROSSING) {
        this.CROSSING = CROSSING;
    }

    public String getLINE_TYPE() {
        return LINE_TYPE;
    }

    public void setLINE_TYPE(String LINE_TYPE) {
        this.LINE_TYPE = LINE_TYPE;
    }
}
