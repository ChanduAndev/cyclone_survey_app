package com.apepdcl.gis_survey.vo;

import com.google.gson.annotations.SerializedName;

public class DTRDetailsReqestVo {
    @SerializedName("IMEI_NO")
    private String IMEI_NO;


    @SerializedName("VERSION")
    private String VERSION;

    @SerializedName("DTR_CODE")
    private String DTR_CODE;

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

    public String getDTR_CODE() {
        return DTR_CODE;
    }

    public void setDTR_CODE(String DTR_CODE) {
        this.DTR_CODE = DTR_CODE;
    }
}
