package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo;

import com.google.gson.annotations.SerializedName;

public class FeederServiceMasterResponseVoRDSSUg11Kv {

    @SerializedName("FEEDER_ID")
    private String FEEDER_ID;

    @SerializedName("CONSUMER_NUMBER")
    private String CONSUMER_NUMBER;

    @SerializedName("CONSUMER_NAME")
    private String CONSUMER_NAME;

    public String getFEEDER_ID() {
        return FEEDER_ID;
    }

    public void setFEEDER_ID(String FEEDER_ID) {
        this.FEEDER_ID = FEEDER_ID;
    }

    public String getCONSUMER_NUMBER() {
        return CONSUMER_NUMBER;
    }

    public void setCONSUMER_NUMBER(String CONSUMER_NUMBER) {
        this.CONSUMER_NUMBER = CONSUMER_NUMBER;
    }

    public String getCONSUMER_NAME() {
        return CONSUMER_NAME;
    }

    public void setCONSUMER_NAME(String CONSUMER_NAME) {
        this.CONSUMER_NAME = CONSUMER_NAME;
    }
}
