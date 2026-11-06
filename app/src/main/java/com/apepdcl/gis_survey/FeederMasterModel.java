package com.apepdcl.gis_survey;

/**
 * Created by Soubhagya Swain on 28,May,2024
 */
public class FeederMasterModel {
    private String SEC_ID;
    private String FEEDER_ID;
    private String FEEDER_NAME;

    public String getSEC_ID() {
        return SEC_ID;
    }

    public void setSEC_ID(String SEC_ID) {
        this.SEC_ID = SEC_ID;
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
}
