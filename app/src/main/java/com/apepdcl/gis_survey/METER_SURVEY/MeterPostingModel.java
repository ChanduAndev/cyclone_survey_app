package com.apepdcl.gis_survey.METER_SURVEY;


/**
 * Created by SOUBHAGYA SWAIN on 09-06-2026.
 */
public class MeterPostingModel {
     String REF_ID;
     String POLE_ID;
     String POLE_NO;
     String SERVICE_NUMBER;
     String DTR_CODE;
     String SC_GLAT;
     String SC_GLONG;
     String IMAGE;
     String REMARKS;
     String DTR_NAME;
     String UKSC_NO;
     String GPS_ACCURACY;
     String CURRENT_LAT;
     String CURRENT_LON;
     String IS_IMAGE_TRANSMITTED;
     String LT_CKT_CODE;
     String SC_NAME;
     String SURVEY_MODE;
     String DEVICE_ID;

    public String getDEVICE_ID() {
        return DEVICE_ID;
    }

    public void setDEVICE_ID(String DEVICE_ID) {
        this.DEVICE_ID = DEVICE_ID;
    }
    
    public String getSURVEY_MODE() {
        return SURVEY_MODE;
    }

    public void setSURVEY_MODE(String SURVEY_MODE) {
        this.SURVEY_MODE = SURVEY_MODE;
    }

    public String getSC_NAME() {
        return SC_NAME;
    }

    public void setSC_NAME(String SC_NAME) {
        this.SC_NAME = SC_NAME;
    }

    public String getLT_CKT_CODE() {
        return LT_CKT_CODE;
    }

    public void setLT_CKT_CODE(String LT_CKT_CODE) {
        this.LT_CKT_CODE = LT_CKT_CODE;
    }

    public String getIS_IMAGE_TRANSMITTED() {
        return IS_IMAGE_TRANSMITTED;
    }

    public void setIS_IMAGE_TRANSMITTED(String IS_IMAGE_TRANSMITTED) {
        this.IS_IMAGE_TRANSMITTED = IS_IMAGE_TRANSMITTED;
    }

    public String getGPS_ACCURACY() {
        return GPS_ACCURACY;
    }

    public void setGPS_ACCURACY(String GPS_ACCURACY) {
        this.GPS_ACCURACY = GPS_ACCURACY;
    }

    public String getCURRENT_LAT() {
        return CURRENT_LAT;
    }

    public void setCURRENT_LAT(String CURRENT_LAT) {
        this.CURRENT_LAT = CURRENT_LAT;
    }

    public String getCURRENT_LON() {
        return CURRENT_LON;
    }

    public void setCURRENT_LON(String CURRENT_LON) {
        this.CURRENT_LON = CURRENT_LON;
    }

    public String getUKSC_NO() {
        return UKSC_NO;
    }

    public void setUKSC_NO(String UKSC_NO) {
        this.UKSC_NO = UKSC_NO;
    }

    public String getDTR_NAME() {
        return DTR_NAME;
    }

    public void setDTR_NAME(String DTR_NAME) {
        this.DTR_NAME = DTR_NAME;
    }

    public String getREF_ID() {
        return REF_ID;
    }

    public void setREF_ID(String REF_ID) {
        this.REF_ID = REF_ID;
    }

    public String getPOLE_ID() {
        return POLE_ID;
    }

    public void setPOLE_ID(String POLE_ID) {
        this.POLE_ID = POLE_ID;
    }

    public String getPOLE_NO() {
        return POLE_NO;
    }

    public void setPOLE_NO(String POLE_NO) {
        this.POLE_NO = POLE_NO;
    }

    public String getSERVICE_NUMBER() {
        return SERVICE_NUMBER;
    }

    public void setSERVICE_NUMBER(String SERVICE_NUMBER) {
        this.SERVICE_NUMBER = SERVICE_NUMBER;
    }

    public String getDTR_CODE() {
        return DTR_CODE;
    }

    public void setDTR_CODE(String DTR_CODE) {
        this.DTR_CODE = DTR_CODE;
    }

    public String getLATITUDE() {
        return SC_GLAT;
    }

    public void setLATITUDE(String LATITUDE) {
        this.SC_GLAT = LATITUDE;
    }

    public String getLONGITUDE() {
        return SC_GLONG;
    }

    public void setLONGITUDE(String LONGITUDE) {
        this.SC_GLONG = LONGITUDE;
    }

    public String getIMAGE() {
        return IMAGE;
    }

    public void setIMAGE(String IMAGE) {
        this.IMAGE = IMAGE;
    }

    public String getREMARKS() {
        return REMARKS;
    }

    public void setREMARKS(String REMARKS) {
        this.REMARKS = REMARKS;
    }
}
