package com.apepdcl.gis_survey.pmi_old.module_11kv.vo;

import com.google.gson.annotations.SerializedName;

public class PMIFeederMasterResponseDataVo {
    @SerializedName("EHT_SS")
    private String EHT_SS;
    @SerializedName("EHT_FEDER")
    private String EHT_FEDER;
    @SerializedName("SESSION_ID")
    private String SESSION_ID;
    @SerializedName("WO_ID")
    private String WO_ID;
    @SerializedName("IS_FEEDER_TYPE")
    private String IS_FEEDER_TYPE;

    public String getIS_FEEDER_TYPE() {
        return IS_FEEDER_TYPE;
    }

    public void setIS_FEEDER_TYPE(String IS_FEEDER_TYPE) {
        this.IS_FEEDER_TYPE = IS_FEEDER_TYPE;
    }

    public String getWO_ID() {
        return WO_ID;
    }

    public void setWO_ID(String WO_ID) {
        this.WO_ID = WO_ID;
    }

    public String getSESSION_ID() {
        return SESSION_ID;
    }

    public void setSESSION_ID(String SESSION_ID) {
        this.SESSION_ID = SESSION_ID;
    }

    public String getEHT_SS() {
        return EHT_SS;
    }

    public void setEHT_SS(String EHT_SS) {
        this.EHT_SS = EHT_SS;
    }

    public String getEHT_FEDER() {
        return EHT_FEDER;
    }

    public void setEHT_FEDER(String EHT_FEDER) {
        this.EHT_FEDER = EHT_FEDER;
    }
}
