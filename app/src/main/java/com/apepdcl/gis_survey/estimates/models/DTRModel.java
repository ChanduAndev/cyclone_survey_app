package com.apepdcl.gis_survey.estimates.models;

/**
 * Created by Soubhagya Swain on 24,June,2024
 */
public class DTRModel {
    String EST_NO;
    String POLE_NO;
    String LATITUDE;
    String LONGITUDE;
    String IS_LT_CKT_AVAILABLE;
    String NO_OF_LT_CKT;
    String SURVEY_STATUS;

    public String getEST_NO() {
        return EST_NO;
    }

    public void setEST_NO(String EST_NO) {
        this.EST_NO = EST_NO;
    }

    public String getPOLE_NO() {
        return POLE_NO;
    }

    public void setPOLE_NO(String POLE_NO) {
        this.POLE_NO = POLE_NO;
    }

    public String getLATITUDE() {
        return LATITUDE;
    }

    public void setLATITUDE(String LATITUDE) {
        this.LATITUDE = LATITUDE;
    }

    public String getLONGITUDE() {
        return LONGITUDE;
    }

    public void setLONGITUDE(String LONGITUDE) {
        this.LONGITUDE = LONGITUDE;
    }

    public String getIS_LT_CKT_AVAILABLE() {
        return IS_LT_CKT_AVAILABLE;
    }

    public void setIS_LT_CKT_AVAILABLE(String IS_LT_CKT_AVAILABLE) {
        this.IS_LT_CKT_AVAILABLE = IS_LT_CKT_AVAILABLE;
    }

    public String getNO_OF_LT_CKT() {
        return NO_OF_LT_CKT;
    }

    public void setNO_OF_LT_CKT(String NO_OF_LT_CKT) {
        this.NO_OF_LT_CKT = NO_OF_LT_CKT;
    }

    public String getSURVEY_STATUS() {
        return SURVEY_STATUS;
    }

    public void setSURVEY_STATUS(String SURVEY_STATUS) {
        this.SURVEY_STATUS = SURVEY_STATUS;
    }
}
