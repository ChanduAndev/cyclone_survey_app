package com.apepdcl.gis_survey.estimates;

import com.google.gson.annotations.SerializedName;

public class SuveyedResponseVoEstimates {
    @SerializedName("STATUS")
    private String STATUS;
    @SerializedName("MESSAGE")
    private String MESSAGE;
    @SerializedName("DATA")
    private String DATA;

    public void setDATA(String DATA) {
        this.DATA = DATA;
    }

    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getDATA() {
        return DATA;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }

    public String getSTATUS() {
        return STATUS;
    }
}
