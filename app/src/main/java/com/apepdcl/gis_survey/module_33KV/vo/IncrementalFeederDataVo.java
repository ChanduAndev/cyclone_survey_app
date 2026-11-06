package com.apepdcl.gis_survey.module_33KV.vo;

public class IncrementalFeederDataVo {
    private String EHT_SS_CODE;
    private String EHT_SS_NAME;
    private String FEEDER_NAME_33KV;
    private String FEEDER_CODE_33KV;
    private String WORKORDER_ID;
    private String STATUS;
    private String FROM_POLE;
    public String getSTATUS() {
        return STATUS;
    }

    public String getFROM_POLE() {
        return FROM_POLE;
    }

    public void setFROM_POLE(String FROM_POLE) {
        this.FROM_POLE = FROM_POLE;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getWORKORDER_ID() {
        return WORKORDER_ID;
    }

    public void setWORKORDER_ID(String WORKORDER_ID) {
        this.WORKORDER_ID = WORKORDER_ID;
    }

    public String getEHT_SS_CODE() {
        return EHT_SS_CODE;
    }

    public void setEHT_SS_CODE(String EHT_SS_CODE) {
        this.EHT_SS_CODE = EHT_SS_CODE;
    }

    public String getEHT_SS_NAME() {
        return EHT_SS_NAME;
    }

    public void setEHT_SS_NAME(String EHT_SS_NAME) {
        this.EHT_SS_NAME = EHT_SS_NAME;
    }

    public String getFEEDER_NAME_33KV() {
        return FEEDER_NAME_33KV;
    }

    public void setFEEDER_NAME_33KV(String FEEDER_NAME_33KV) {
        this.FEEDER_NAME_33KV = FEEDER_NAME_33KV;
    }

    public String getFEEDER_CODE_33KV() {
        return FEEDER_CODE_33KV;
    }

    public void setFEEDER_CODE_33KV(String FEEDER_CODE_33KV) {
        this.FEEDER_CODE_33KV = FEEDER_CODE_33KV;
    }
}
