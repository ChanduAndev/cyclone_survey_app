package com.apepdcl.gis_survey.module_33KV.vo;

import com.google.gson.annotations.SerializedName;

public class LoginDataResponse {
    @SerializedName("ROLE")
    private String ROLE;
    @SerializedName("OFFICE_ID")
    private String OFFICE_ID;
    @SerializedName("SECTION_NAME")
    private String SECTION_NAME;
    @SerializedName("SEC_CODE")
    private String SEC_CODE;

    public String getROLE() {
        return ROLE;
    }

    public void setROLE(String ROLE) {
        this.ROLE = ROLE;
    }

    public String getOFFICE_ID() {
        return OFFICE_ID;
    }

    public void setOFFICE_ID(String OFFICE_ID) {
        this.OFFICE_ID = OFFICE_ID;
    }

    public String getSECTION_NAME() {
        return SECTION_NAME;
    }

    public void setSECTION_NAME(String SECTION_NAME) {
        this.SECTION_NAME = SECTION_NAME;
    }

    public String getSEC_CODE() {
        return SEC_CODE;
    }

    public void setSEC_CODE(String SEC_CODE) {
        this.SEC_CODE = SEC_CODE;
    }
}
