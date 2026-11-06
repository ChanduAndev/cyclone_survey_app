package com.apepdcl.gis_survey.estimates.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DTRCapacities {

    private String phase;
    private String capacity;

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }
}
