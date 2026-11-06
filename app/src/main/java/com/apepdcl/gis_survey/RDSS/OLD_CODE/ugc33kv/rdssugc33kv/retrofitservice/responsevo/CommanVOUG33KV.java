package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.retrofitservice.responsevo;

import com.google.gson.annotations.SerializedName;

public class CommanVOUG33KV {

    @SerializedName("STATUS")
    private String STATUS;

    @SerializedName("RESPONSE")
    private Object RESPONSE;

    @SerializedName("ERR_MSG")
    private String ERR_MSG;


    public Object getRESPONSE() {
        return RESPONSE;
    }

    public void setRESPONSE(Object RESPONSE) {
        this.RESPONSE = RESPONSE;
    }

    public String getERR_MSG() {
        return ERR_MSG;
    }

    public void setERR_MSG(String ERR_MSG) {
        this.ERR_MSG = ERR_MSG;
    }


    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }


}
