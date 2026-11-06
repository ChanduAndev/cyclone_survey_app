package com.apepdcl.gis_survey.vo;

import com.google.gson.annotations.SerializedName;

public class LTCktsReqVo {
    @SerializedName("ASSIGNED_TO")
    private String ASSIGNED_TO;
    @SerializedName("IMEI_NO")
    private String IMEI_NO;
    @SerializedName("VERSION")
    private String VERSION;
    @SerializedName("SECTION_ID")
    private String SECTION_ID;


    public String getASSIGNED_TO() {
        return ASSIGNED_TO;
    }

    public void setASSIGNED_TO(String ASSIGNED_TO) {
        this.ASSIGNED_TO = ASSIGNED_TO;
    }

    public String getIMEI_NO() {
        return IMEI_NO;
    }

    public void setIMEI_NO(String IMEI_NO) {
        this.IMEI_NO = IMEI_NO;
    }

    public String getVERSION() {
        return VERSION;
    }

    public void setVERSION(String VERSION) {
        this.VERSION = VERSION;
    }

    public String getSECTION_ID() {
        return SECTION_ID;
    }

    public void setSECTION_ID(String SECTION_ID) {
        this.SECTION_ID = SECTION_ID;
    }


}
