package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo;

import com.google.gson.annotations.SerializedName;

public class FeederMasterResponseDataVo_Two {

    @SerializedName("LAYOUT_NAME")
    private String LAYOUT_NAME;
    @SerializedName("FEEDER_ID")
    private String FEEDER_ID;
    @SerializedName("LAYOUT_ID")
    private String LAYOUT_ID;

    public String getLAYOUT_NAME() {
        return LAYOUT_NAME;
    }

    public void setLAYOUT_NAME(String LAYOUT_NAME) {
        this.LAYOUT_NAME = LAYOUT_NAME;
    }

    public String getFEEDER_ID() {
        return FEEDER_ID;
    }

    public void setFEEDER_ID(String FEEDER_ID) {
        this.FEEDER_ID = FEEDER_ID;
    }

    public String getLAYOUT_ID() {
        return LAYOUT_ID;
    }

    public void setLAYOUT_ID(String LAYOUT_ID) {
        this.LAYOUT_ID = LAYOUT_ID;
    }
}
