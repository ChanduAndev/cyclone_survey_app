package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo;

import com.google.gson.annotations.SerializedName;

public class WoStatusMasterVoUG33KV {
    @SerializedName("FEEDER_ID")
    private String FEEDER_ID;
    @SerializedName("WO_ID")
    private String WO_ID;
    @SerializedName("WO_STATUS")
    private String WO_STATUS;

    @SerializedName("UPDATED_BY")
    private String UPDATED_BY;

    @SerializedName("UPDATED_DATE")
    private String UPDATED_DATE;

    public String getUPDATED_BY() {
        return UPDATED_BY;
    }

    public void setUPDATED_BY(String UPDATED_BY) {
        this.UPDATED_BY = UPDATED_BY;
    }

    public String getUPDATED_DATE() {
        return UPDATED_DATE;
    }

    public void setUPDATED_DATE(String UPDATED_DATE) {
        this.UPDATED_DATE = UPDATED_DATE;
    }

    public String getFEEDER_ID() {
        return FEEDER_ID;
    }

    public void setFEEDER_ID(String FEEDER_ID) {
        this.FEEDER_ID = FEEDER_ID;
    }

    public String getWO_ID() {
        return WO_ID;
    }

    public void setWO_ID(String WO_ID) {
        this.WO_ID = WO_ID;
    }

    public String getWO_STATUS() {
        return WO_STATUS;
    }

    public void setWO_STATUS(String WO_STATUS) {
        this.WO_STATUS = WO_STATUS;
    }
}
