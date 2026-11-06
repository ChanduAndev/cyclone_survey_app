package com.apepdcl.gis_survey.pmi_old.module_11kv.vo;

import com.google.gson.annotations.SerializedName;

public class PMILTCktsDataVo {
    @SerializedName("DTR_CODE")
    private String DTR_CODE;
    @SerializedName("DTR_PHASE")
    private String DTR_PHASE;
    @SerializedName("DTR_LOCATION")
    private String DTR_LOCATION;
    @SerializedName("LT_CKTCODE")
    private String LT_CKTCODE;
    @SerializedName("FEEDER_ID")
    private String FEEDER_ID;
    @SerializedName("WO_ID")
    private String WO_ID;

    @SerializedName("STATUS")
    private String STATUS;
    @SerializedName("TAPPING_POLE")
    private String TAPPING_POLE;

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getTAPPING_POLE() {
        return TAPPING_POLE;
    }

    public void setTAPPING_POLE(String TAPPING_POLE) {
        this.TAPPING_POLE = TAPPING_POLE;
    }

    public String getWO_ID() {
        return WO_ID;
    }

    public void setWO_ID(String WO_ID) {
        this.WO_ID = WO_ID;
    }

    public String getFEEDER_ID() {
        return FEEDER_ID;
    }

    public void setFEEDER_ID(String FEEDER_ID) {
        this.FEEDER_ID = FEEDER_ID;
    }

    public String getDTR_CODE() {
        return DTR_CODE;
    }

    public void setDTR_CODE(String DTR_CODE) {
        this.DTR_CODE = DTR_CODE;
    }

    public String getDTR_PHASE() {
        return DTR_PHASE;
    }

    public void setDTR_PHASE(String DTR_PHASE) {
        this.DTR_PHASE = DTR_PHASE;
    }

    public String getDTR_LOCATION() {
        return DTR_LOCATION;
    }

    public void setDTR_LOCATION(String DTR_LOCATION) {
        this.DTR_LOCATION = DTR_LOCATION;
    }

    public String getLT_CKTCODE() {
        return LT_CKTCODE;
    }

    public void setLT_CKTCODE(String LT_CKTCODE) {
        this.LT_CKTCODE = LT_CKTCODE;
    }
}
