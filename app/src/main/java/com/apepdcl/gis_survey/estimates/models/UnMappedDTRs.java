package com.apepdcl.gis_survey.estimates.models;

public class UnMappedDTRs {
    public String FEEDER_ID;
    public String FEEDER_NAME;
    public String PHASE;
    public String CAPACITY;
    public String DTR_STRUCTURE_CODE;
    public String SECTION_ID;
    public String NO_OF_LT_CKTS;

    public String getNO_OF_LT_CKTS() {
        return NO_OF_LT_CKTS;
    }

    public void setNO_OF_LT_CKTS(String NO_OF_LT_CKTS) {
        this.NO_OF_LT_CKTS = NO_OF_LT_CKTS;
    }

    public String WO_MAPPED;

    public String getWO_MAPPED() {
        return WO_MAPPED;
    }

    public void setWO_MAPPED(String WO_MAPPED) {
        this.WO_MAPPED = WO_MAPPED;
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

    public String getPHASE() {
        return PHASE;
    }

    public void setPHASE(String PHASE) {
        this.PHASE = PHASE;
    }

    public String getCAPACITY() {
        return CAPACITY;
    }

    public void setCAPACITY(String CAPACITY) {
        this.CAPACITY = CAPACITY;
    }

    public String getDTR_STRUCTURE_CODE() {
        return DTR_STRUCTURE_CODE;
    }

    public void setDTR_STRUCTURE_CODE(String DTR_STRUCTURE_CODE) {
        this.DTR_STRUCTURE_CODE = DTR_STRUCTURE_CODE;
    }

    public String getSECTION_ID() {
        return SECTION_ID;
    }

    public void setSECTION_ID(String SECTION_ID) {
        this.SECTION_ID = SECTION_ID;
    }
}
