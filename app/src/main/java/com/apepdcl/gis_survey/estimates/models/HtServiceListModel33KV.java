package com.apepdcl.gis_survey.estimates.models;

import androidx.annotation.NonNull;

public class HtServiceListModel33KV {
    private String HTSC_NAME;
    private String HT_SCNO;
    private String FEEDER_ID;

    public String getHTSC_NAME() {
        return HTSC_NAME;
    }

    public void setHTSC_NAME(String HTSC_NAME) {
        this.HTSC_NAME = HTSC_NAME;
    }

    public String getHT_SCNO() {
        return HT_SCNO;
    }

    public void setHT_SCNO(String HT_SCNO) {
        this.HT_SCNO = HT_SCNO;
    }

    public String getFEEDER_ID() {
        return FEEDER_ID;
    }

    public void setFEEDER_ID(String FEEDER_ID) {
        this.FEEDER_ID = FEEDER_ID;
    }

    @NonNull
    @Override
    public String toString() {
        if (getHT_SCNO().isEmpty() || getHT_SCNO() == null) {
            return getHTSC_NAME();
        } else {
            return getHT_SCNO() + "~" + getHTSC_NAME();
        }

    }
}
