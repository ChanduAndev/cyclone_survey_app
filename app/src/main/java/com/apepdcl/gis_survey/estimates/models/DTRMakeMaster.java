package com.apepdcl.gis_survey.estimates.models;

import androidx.annotation.NonNull;

public class DTRMakeMaster {
    private String DTR_MAKE;
    private String DTR_NAME;
    private String ID;

    public String getDTR_MAKE() {
        return DTR_MAKE;
    }

    public void setDTR_MAKE(String DTR_MAKE) {
        this.DTR_MAKE = DTR_MAKE;
    }

    public String getDTR_NAME() {
        return DTR_NAME;
    }

    public void setDTR_NAME(String DTR_NAME) {
        this.DTR_NAME = DTR_NAME;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @NonNull
    @Override
    public String toString() {
        return DTR_MAKE;
    }
}
