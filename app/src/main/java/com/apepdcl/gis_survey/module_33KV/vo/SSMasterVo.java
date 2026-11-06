package com.apepdcl.gis_survey.module_33KV.vo;

import com.google.gson.annotations.SerializedName;

public class SSMasterVo {

    @SerializedName("FEEDING_SS_CODE")
    private String FEEDING_SS_CODE;
    @SerializedName("FEEDING_SS_NAME")
    private String FEEDING_SS_NAME;
    @SerializedName("FEEDER_CODE_33KV")
    private String FEEDER_CODE_33KV;

    @SerializedName("SS_LATITUDE")
    private String LAT;

    @SerializedName("SS_LONGITUDE")
    private String LAN;

    @SerializedName("MAX_DISTANCE")
    private String MAX_DISTANCE;



    public String getFEEDING_SS_CODE() {
        return FEEDING_SS_CODE;
    }

    public void setFEEDING_SS_CODE(String FEEDING_SS_CODE) {
        this.FEEDING_SS_CODE = FEEDING_SS_CODE;
    }

    public String getFEEDING_SS_NAME() {
        return FEEDING_SS_NAME;
    }

    public void setFEEDING_SS_NAME(String FEEDING_SS_NAME) {
        this.FEEDING_SS_NAME = FEEDING_SS_NAME;
    }

    public String getFEEDER_CODE_33KV() {
        return FEEDER_CODE_33KV;
    }

    public void setFEEDER_CODE_33KV(String FEEDER_CODE_33KV) {
        this.FEEDER_CODE_33KV = FEEDER_CODE_33KV;
    }

    public String getLAT() {
        return LAT;
    }

    public void setLAT(String LAT) {
        this.LAT = LAT;
    }

    public String getLAN() {
        return LAN;
    }

    public void setLAN(String LAN) {
        this.LAN = LAN;
    }

    public String getMAX_DISTANCE() {
        return MAX_DISTANCE;
    }

    public void setMAX_DISTANCE(String MAX_DISTANCE) {
        this.MAX_DISTANCE = MAX_DISTANCE;
    }
}
