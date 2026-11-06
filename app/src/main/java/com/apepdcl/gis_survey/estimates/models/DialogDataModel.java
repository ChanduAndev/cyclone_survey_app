package com.apepdcl.gis_survey.estimates.models;

public class DialogDataModel {
    private String ID;
    private String DESCRIPTION;
    private String RECORD_STATUS;

    private String IS_ENABLED;

    public String getIS_ENABLED() {
        return IS_ENABLED;
    }

    public void setIS_ENABLED(String IS_ENABLED) {
        this.IS_ENABLED = IS_ENABLED;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public String getRECORD_STATUS() {
        return RECORD_STATUS;
    }

    public void setRECORD_STATUS(String RECORD_STATUS) {
        this.RECORD_STATUS = RECORD_STATUS;
    }
}
