package com.apepdcl.gis_survey.vo;

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

    @SerializedName("OFFICE_TYPE")
    private String OFFICE_TYPE;

    @SerializedName("CIRCLE")
    private String CIRCLE;

    @SerializedName("USERID")
    private String USERID;

    @SerializedName("DIV_ID")
    private String DIV_ID;

    @SerializedName("ACCESS_TOKEN")
    private String ACCESS_TOKEN;

    @SerializedName("REFRESH_TOKEN")
    private String REFRESH_TOKEN;

    @SerializedName("NAME")
    private String NAME;

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getACCESS_TOKEN() {
        return ACCESS_TOKEN;
    }

    public void setACCESS_TOKEN(String ACCESS_TOKEN) {
        this.ACCESS_TOKEN = ACCESS_TOKEN;
    }

    public String getREFRESH_TOKEN() {
        return REFRESH_TOKEN;
    }

    public void setREFRESH_TOKEN(String REFRESH_TOKEN) {
        this.REFRESH_TOKEN = REFRESH_TOKEN;
    }

    public String getDIV_ID() {
        return DIV_ID;
    }

    public void setDIV_ID(String DIV_ID) {
        this.DIV_ID = DIV_ID;
    }

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public String getOFFICE_TYPE() {
        return OFFICE_TYPE;
    }

    public void setOFFICE_TYPE(String OFFICE_TYPE) {
        this.OFFICE_TYPE = OFFICE_TYPE;
    }

    public String getCIRCLE() {
        return CIRCLE;
    }

    public void setCIRCLE(String CIRCLE) {
        this.CIRCLE = CIRCLE;
    }

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
