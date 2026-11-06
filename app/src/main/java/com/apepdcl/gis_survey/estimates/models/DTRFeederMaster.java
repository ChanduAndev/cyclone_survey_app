package com.apepdcl.gis_survey.estimates.models;

import android.os.Parcelable;

import java.io.Serializable;

public class DTRFeederMaster implements Serializable {
    String SECTION_NAME;
    String DTR_STRUCTURE_CODE;
    String CAPACITY;
    String PHASE;
    String DTR_LOCATION;
    String DTR_MAKE;
    String DTR_SLNO;
    String SECTION_ID;
    String FEEDER_ID;
    String FEEDER_NAME;
    String IS_MAPPED;
    String NO_OF_LT_CKTS;
    String WO_MAPPED;
    String MAPPED_LOCATION_NO;
    String GLAT;
    String GLONG;

    public String getGLAT() {
        return GLAT;
    }

    public void setGLAT(String GLAT) {
        this.GLAT = GLAT;
    }

    public String getGLONG() {
        return GLONG;
    }

    public void setGLONG(String GLONG) {
        this.GLONG = GLONG;
    }

    public String getMAPPED_LOCATION_NO() {
        return MAPPED_LOCATION_NO;
    }

    public void setMAPPED_LOCATION_NO(String MAPPED_LOCATION_NO) {
        this.MAPPED_LOCATION_NO = MAPPED_LOCATION_NO;
    }

    public String getWO_MAPPED() {
        return WO_MAPPED;
    }

    public void setWO_MAPPED(String WO_MAPPED) {
        this.WO_MAPPED = WO_MAPPED;
    }

    public String getNO_OF_LT_CKTS() {
        return NO_OF_LT_CKTS;
    }

    public void setNO_OF_LT_CKTS(String NO_OF_LT_CKTS) {
        this.NO_OF_LT_CKTS = NO_OF_LT_CKTS;
    }

    public String getSECTION_NAME() {
        return SECTION_NAME;
    }

    public void setSECTION_NAME(String SECTION_NAME) {
        this.SECTION_NAME = SECTION_NAME;
    }

    public String getDTR_STRUCTURE_CODE() {
        return DTR_STRUCTURE_CODE;
    }

    public void setDTR_STRUCTURE_CODE(String DTR_STRUCTURE_CODE) {
        this.DTR_STRUCTURE_CODE = DTR_STRUCTURE_CODE;
    }

    public String getCAPACITY() {
        return CAPACITY;
    }

    public void setCAPACITY(String CAPACITY) {
        this.CAPACITY = CAPACITY;
    }

    public String getPHASE() {
        return PHASE;
    }

    public void setPHASE(String PHASE) {
        this.PHASE = PHASE;
    }

    public String getDTR_LOCATION() {
        return DTR_LOCATION;
    }

    public void setDTR_LOCATION(String DTR_LOCATION) {
        this.DTR_LOCATION = DTR_LOCATION;
    }

    public String getDTR_MAKE() {
        return DTR_MAKE;
    }

    public void setDTR_MAKE(String DTR_MAKE) {
        this.DTR_MAKE = DTR_MAKE;
    }

    public String getDTR_SLNO() {
        return DTR_SLNO;
    }

    public void setDTR_SLNO(String DTR_SLNO) {
        this.DTR_SLNO = DTR_SLNO;
    }

    public String getSECTION_ID() {
        return SECTION_ID;
    }

    public void setSECTION_ID(String SECTION_ID) {
        this.SECTION_ID = SECTION_ID;
    }

    public String getFEEDER_ID() {
        return FEEDER_ID;
    }

    public void setFEEDER_ID(String FEEDER_ID) {
        this.FEEDER_ID = FEEDER_ID;
    }

    public String getFEEDER_NAME() {
        return FEEDER_NAME;
    }

    public void setFEEDER_NAME(String FEEDER_NAME) {
        this.FEEDER_NAME = FEEDER_NAME;
    }

    public String getIS_MAPPED() {
        return IS_MAPPED;
    }

    public void setIS_MAPPED(String IS_MAPPED) {
        this.IS_MAPPED = IS_MAPPED;
    }
}
