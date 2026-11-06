package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.retrofitservice.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ravinder.vakiti on 9/21/2019.
 */
public class OtherReplacementRequestVORDSSUg11Kv {
    @SerializedName("ASSIGN_TO")
    private String ASSIGN_TO;

    public String getASSIGN_TO() {
        return ASSIGN_TO;
    }

    public void setASSIGN_TO(String ASSIGN_TO) {
        this.ASSIGN_TO = ASSIGN_TO;
    }
}


