package com.apepdcl.gis_survey.estimates.models;

import androidx.annotation.NonNull;

public class LTLineConfigModel {
    private String PHASE;
    private String LINE_CONFIG;
    private String TYPE;
    private String FACTOR;

    public String getPHASE() {
        return PHASE;
    }

    public void setPHASE(String PHASE) {
        this.PHASE = PHASE;
    }

    public String getLINE_CONFIG() {
        return LINE_CONFIG;
    }

    public void setLINE_CONFIG(String LINE_CONFIG) {
        this.LINE_CONFIG = LINE_CONFIG;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getFACTOR() {
        return FACTOR;
    }

    public void setFACTOR(String FACTOR) {
        this.FACTOR = FACTOR;
    }

    @NonNull
    @Override
    public String toString() {
        return LINE_CONFIG;
    }
}
