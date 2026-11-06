package com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SurveyedRequestVo {
    @SerializedName("OFFICE_ID")
    String OFFICE_ID;
    @SerializedName("USER_ID")
    String USER_ID;
    @SerializedName("ASSERT_ID")
    String ASSERT_ID;
    @SerializedName("IMEI_NO")
    String IMEI_NO;
    @SerializedName("SESSION_ID")
    String SESSION_ID;
    @SerializedName("FEEDER_ID")
    String FEEDER_ID;
    @SerializedName("IS_SURVEY_COM")
    String IS_SURVEY_COM;

    public String getFEEDER_ID() {
        return FEEDER_ID;
    }

    public void setFEEDER_ID(String FEEDER_ID) {
        this.FEEDER_ID = FEEDER_ID;
    }

    public String getIS_SURVEY_COM() {
        return IS_SURVEY_COM;
    }

    public void setIS_SURVEY_COM(String IS_SURVEY_COM) {
        this.IS_SURVEY_COM = IS_SURVEY_COM;
    }

    @SerializedName("SURVEY_DATA" )
    List<SurveyedRequestDataVo> SURVEY_DATA =null;

    public String getSESSION_ID() {
        return SESSION_ID;
    }

    public void setSESSION_ID(String SESSION_ID) {
        this.SESSION_ID = SESSION_ID;
    }

    public String getOFFICE_ID() {
        return OFFICE_ID;
    }

    public void setOFFICE_ID(String OFFICE_ID) {
        this.OFFICE_ID = OFFICE_ID;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getASSERT_ID() {
        return ASSERT_ID;
    }

    public void setASSERT_ID(String ASSERT_ID) {
        this.ASSERT_ID = ASSERT_ID;
    }

    public String getIMEI_NO() {
        return IMEI_NO;
    }

    public void setIMEI_NO(String IMEI_NO) {
        this.IMEI_NO = IMEI_NO;
    }

    public List<SurveyedRequestDataVo> getSURVEY_DATA() {
        return SURVEY_DATA;
    }

    public void setSURVEY_DATA(List<SurveyedRequestDataVo> SURVEY_DATA) {
        this.SURVEY_DATA = SURVEY_DATA;
    }
}
