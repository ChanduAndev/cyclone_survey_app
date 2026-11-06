package com.apepdcl.gis_survey.vo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Soubhagya Swain on 16,April,2024
 */
public class MultiRequestVO {
    @SerializedName("VOLTAGE_LEVEL")
    private String VOLTAGE_LEVEL;
    @SerializedName("FEEDER_ID")
    private String FEEDER_ID;
    @SerializedName("LOCATION_NO")
    private String LOCATION_NO;
    @SerializedName("MANUAL_ENTRY")
    private String MANUAL_ENTRY;

    public String getMANUAL_ENTRY() {
        return MANUAL_ENTRY;
    }

    public void setMANUAL_ENTRY(String MANUAL_ENTRY) {
        this.MANUAL_ENTRY = MANUAL_ENTRY;
    }

    public String getVOLTAGE_LEVEL() {
        return VOLTAGE_LEVEL;
    }

    public void setVOLTAGE_LEVEL(String VOLTAGE_LEVEL) {
        this.VOLTAGE_LEVEL = VOLTAGE_LEVEL;
    }

    public String getFEEDER_ID() {
        return FEEDER_ID;
    }

    public void setFEEDER_ID(String FEEDER_ID) {
        this.FEEDER_ID = FEEDER_ID;
    }

    public String getLOCATION_NO() {
        return LOCATION_NO;
    }

    public void setLOCATION_NO(String LOCATION_NO) {
        this.LOCATION_NO = LOCATION_NO;
    }
}
