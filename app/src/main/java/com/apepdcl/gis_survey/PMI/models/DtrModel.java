package com.apepdcl.gis_survey.PMI.models;

import com.google.gson.annotations.SerializedName;

public class DtrModel {

    @SerializedName("SECTION_NAME")
    private String sectionName;

    @SerializedName("CAPACITY")
    private int capacity;

    @SerializedName("PHASE")
    private String phase;

    @SerializedName("DTR_CODE")
    private String dtrCode;

    @SerializedName("DTR_LOCATION")
    private String dtrLocation;

    public String getSectionName() {
        return sectionName;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getPhase() {
        return phase;
    }

    public String getDtrCode() {
        return dtrCode;
    }

    public String getDtrLocation() {
        return dtrLocation;
    }
}
