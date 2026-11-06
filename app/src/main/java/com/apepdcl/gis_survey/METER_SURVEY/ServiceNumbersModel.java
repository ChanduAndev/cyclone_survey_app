package com.apepdcl.gis_survey.METER_SURVEY;


/**
 * Created by SOUBHAGYA SWAIN on 11-06-2026.
 */
public class ServiceNumbersModel {
    String SERVICE_NUMBER;
    String UKSC_NUMBER;
    String AREA_CODE;
    String AREA_NAME;
    String METER_NO;

    String CONSUMER_NAME;

    public String getSERVICE_NUMBER() {
        return SERVICE_NUMBER;
    }

    public void setSERVICE_NUMBER(String SERVICE_NUMBER) {
        this.SERVICE_NUMBER = SERVICE_NUMBER;
    }

    public String getUKSC_NUMBER() {
        return UKSC_NUMBER;
    }

    public void setUKSC_NUMBER(String UKSC_NUMBER) {
        this.UKSC_NUMBER = UKSC_NUMBER;
    }

    public String getAREA_CODE() {
        return AREA_CODE;
    }

    public void setAREA_CODE(String AREA_CODE) {
        this.AREA_CODE = AREA_CODE;
    }

    public String getAREA_NAME() {
        return AREA_NAME;
    }

    public void setAREA_NAME(String AREA_NAME) {
        this.AREA_NAME = AREA_NAME;
    }

    public String getMETER_NO() {
        return METER_NO;
    }

    public void setMETER_NO(String METER_NO) {
        this.METER_NO = METER_NO;
    }

    public String getCONSUMER_NAME() {
        return CONSUMER_NAME;
    }

    public void setCONSUMER_NAME(String CONSUMER_NAME) {
        this.CONSUMER_NAME = CONSUMER_NAME;
    }
}
