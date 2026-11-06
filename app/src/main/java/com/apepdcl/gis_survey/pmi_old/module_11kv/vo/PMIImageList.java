package com.apepdcl.gis_survey.pmi_old.module_11kv.vo;

import com.google.gson.annotations.SerializedName;

public class PMIImageList {
    @SerializedName("FEEDER_ID")
    String FEEDER_ID;
    @SerializedName("POLE_NO")
    String POLE_NO;
    @SerializedName("IMAGE_1")
    String IMAGE_1;
    @SerializedName("IMAGE_2")
    String IMAGE_2;
    @SerializedName("IMAGE_3")
    String IMAGE_3;

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

    public String getIMAGE_1() {
        return IMAGE_1;
    }

    public void setIMAGE_1(String IMAGE_1) {
        this.IMAGE_1 = IMAGE_1;
    }

    public String getIMAGE_2() {
        return IMAGE_2;
    }

    public void setIMAGE_2(String IMAGE_2) {
        this.IMAGE_2 = IMAGE_2;
    }

    public String getIMAGE_3() {
        return IMAGE_3;
    }

    public void setIMAGE_3(String IMAGE_3) {
        this.IMAGE_3 = IMAGE_3;
    }
}
