package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo;

import com.google.gson.annotations.SerializedName;

public class IncrementalFeederMasterResponseDataVoUG33KV {
    @SerializedName("EHT_SS")
    private String EHT_SS;
    @SerializedName("EHT_FEDER")
    private String EHT_FEDER;
    @SerializedName("SESSION_ID")
    private String SESSION_ID;

    @SerializedName("WORK_ORDER")
    private String WORK_ORDER;

    @SerializedName("WORK_STATUS")
    private String WORK_STATUS;

    @SerializedName("FROM_POLE")
    private String FROM_POLE;

    public String getFROM_POLE() {
        return FROM_POLE;
    }

    public void setFROM_POLE(String FROM_POLE) {
        this.FROM_POLE = FROM_POLE;
    }

    public String getWORK_ORDER() {
        return WORK_ORDER;
    }

    public void setWORK_ORDER(String WORK_ORDER) {
        this.WORK_ORDER = WORK_ORDER;
    }

    public String getWORK_STATUS() {
        return WORK_STATUS;
    }

    public void setWORK_STATUS(String WORK_STATUS) {
        this.WORK_STATUS = WORK_STATUS;
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
