package com.apepdcl.gis_survey.module_11KV.models;

import com.google.gson.annotations.SerializedName;

public class DTRMasterRequestVo {

    @SerializedName("VERSION")
    private String VERSION;

    @SerializedName("IMEI_NO")
    private String IMEI_NO;

    @SerializedName("FEEDER_ID")
    private String FEEDER_ID;

  /*  @SerializedName("SESSION_ID")
    private String SESSION_ID;*/

    public String getVERSION() {
        return VERSION;
    }

    public void setVERSION(String VERSION) {
        this.VERSION = VERSION;
    }

    public String getIMEI_NO() {
        return IMEI_NO;
    }

    public void setIMEI_NO(String IMEI_NO) {
        this.IMEI_NO = IMEI_NO;
    }

    public String getFEEDER_ID() {
        return FEEDER_ID;
    }

    public void setFEEDER_ID(String FEEDER_ID) {
        this.FEEDER_ID = FEEDER_ID;
    }
}
