package com.apepdcl.gis_survey.vo;

import com.google.gson.annotations.SerializedName;

public class LTCktsResponseDataVo {
    @SerializedName("DTR_CODE")
    private String DTR_CODE;

    @SerializedName("DTR_PHASE")
    private String DTR_PHASE;

    @SerializedName("DTR_LOCATION")
    private String DTR_LOCATION;

    @SerializedName("LT_CKT_CODE")
    private String LT_CKTCODE;

    @SerializedName("FEEDER_ID")
    private String FEEDER_ID;

    @SerializedName("IS_LTCKT_AVAILABLE")
    private String IS_LTCKT_AVAILABLE;

    public String getIS_LTCKT_AVAILABLE() {
        return IS_LTCKT_AVAILABLE;
    }

    public void setIS_LTCKT_AVAILABLE(String IS_LTCKT_AVAILABLE) {
        this.IS_LTCKT_AVAILABLE = IS_LTCKT_AVAILABLE;
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
