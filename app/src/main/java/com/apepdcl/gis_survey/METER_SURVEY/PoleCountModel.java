package com.apepdcl.gis_survey.METER_SURVEY;


/**
 * Created by SOUBHAGYA SWAIN on 18-06-2026.
 */
public class PoleCountModel {
    private  String COUNT_POLE_ID;
    private  String COUNT_POLE_NO;
    private  String COUNT_TOTAL;
    private  String REC_ID;
    private  String IMAGE;
    private  String IS_IMAGE_TRANSMITTED;
    private  String IS_IMAGE_TRANSMITTED_PC_LOC;

    public String getIS_IMAGE_TRANSMITTED_PC_LOC() {
        return IS_IMAGE_TRANSMITTED_PC_LOC;
    }

    public void setIS_IMAGE_TRANSMITTED_PC_LOC(String IS_IMAGE_TRANSMITTED_PC_LOC) {
        this.IS_IMAGE_TRANSMITTED_PC_LOC = IS_IMAGE_TRANSMITTED_PC_LOC;
    }

    public String getIS_IMAGE_TRANSMITTED() {
        return IS_IMAGE_TRANSMITTED;
    }

    public void setIS_IMAGE_TRANSMITTED(String IS_IMAGE_TRANSMITTED) {
        this.IS_IMAGE_TRANSMITTED = IS_IMAGE_TRANSMITTED;
    }

    public String getIMAGE() {
        return IMAGE;
    }

    public void setIMAGE(String IMAGE) {
        this.IMAGE = IMAGE;
    }

    public String getREC_ID() {
        return REC_ID;
    }

    public void setREC_ID(String REC_ID) {
        this.REC_ID = REC_ID;
    }

    public String getCOUNT_POLE_ID() {
        return COUNT_POLE_ID;
    }

    public void setCOUNT_POLE_ID(String COUNT_POLE_ID) {
        this.COUNT_POLE_ID = COUNT_POLE_ID;
    }

    public String getCOUNT_POLE_NO() {
        return COUNT_POLE_NO;
    }

    public void setCOUNT_POLE_NO(String COUNT_POLE_NO) {
        this.COUNT_POLE_NO = COUNT_POLE_NO;
    }

    public String getCOUNT_TOTAL() {
        return COUNT_TOTAL;
    }

    public void setCOUNT_TOTAL(String COUNT_TOTAL) {
        this.COUNT_TOTAL = COUNT_TOTAL;
    }
}
