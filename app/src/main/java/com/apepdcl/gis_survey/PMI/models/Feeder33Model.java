package com.apepdcl.gis_survey.PMI.models;

import com.google.gson.annotations.SerializedName;

public class Feeder33Model {
    @SerializedName("DIVISION_ID")
    private String dev_id;

    @SerializedName("DIVISION_NAME")
    private String DIVISION_NAME;

    @SerializedName("EHT_SS_NAME")
    private String EHT_SS_NAME;

    @SerializedName("EHT_SS_CODE")
    private String EHT_SS_CODE;

    @SerializedName("FEEDER_CODE_33KV")
    private String FEEDER_CODE_33KV;

    @SerializedName("FEEDER_NAME_33KV")
    private String FEEDER_NAME_33KV;

    @SerializedName("POLES")
    private String poles;

    public String getDev_id() {
        return dev_id;
    }

    public void setDev_id(String dev_id) {
        this.dev_id = dev_id;
    }

    public String getDIVISION_NAME() {
        return DIVISION_NAME;
    }

    public void setDIVISION_NAME(String DIVISION_NAME) {
        this.DIVISION_NAME = DIVISION_NAME;
    }

    public String getEHT_SS_NAME() {
        return EHT_SS_NAME;
    }

    public void setEHT_SS_NAME(String EHT_SS_NAME) {
        this.EHT_SS_NAME = EHT_SS_NAME;
    }

    public String getEHT_SS_CODE() {
        return EHT_SS_CODE;
    }

    public void setEHT_SS_CODE(String EHT_SS_CODE) {
        this.EHT_SS_CODE = EHT_SS_CODE;
    }

    public String getFEEDER_CODE_33KV() {
        return FEEDER_CODE_33KV;
    }

    public void setFEEDER_CODE_33KV(String FEEDER_CODE_33KV) {
        this.FEEDER_CODE_33KV = FEEDER_CODE_33KV;
    }

    public String getFEEDER_NAME_33KV() {
        return FEEDER_NAME_33KV;
    }

    public void setFEEDER_NAME_33KV(String FEEDER_NAME_33KV) {
        this.FEEDER_NAME_33KV = FEEDER_NAME_33KV;
    }

    public String getPoles() {
        return poles;
    }

    public void setPoles(String poles) {
        this.poles = poles;
    }
}
