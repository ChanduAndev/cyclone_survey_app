package com.apepdcl.gis_survey.estimates.models;

public class SectionsModel {
    private boolean selected;
    private String SECTION_NAME;
    private String SUBDIV_NAME;
    private String CIR_NAME;
    private String SECTION_ID;
    private String DIV_NAME;
    private String UCODE;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getSECTION_NAME() {
        return SECTION_NAME;
    }

    public void setSECTION_NAME(String SECTION_NAME) {
        this.SECTION_NAME = SECTION_NAME;
    }

    public String getSUBDIV_NAME() {
        return SUBDIV_NAME;
    }

    public void setSUBDIV_NAME(String SUBDIV_NAME) {
        this.SUBDIV_NAME = SUBDIV_NAME;
    }

    public String getCIR_NAME() {
        return CIR_NAME;
    }

    public void setCIR_NAME(String CIR_NAME) {
        this.CIR_NAME = CIR_NAME;
    }

    public String getSECTION_ID() {
        return SECTION_ID;
    }

    public void setSECTION_ID(String SECTION_ID) {
        this.SECTION_ID = SECTION_ID;
    }

    public String getDIV_NAME() {
        return DIV_NAME;
    }

    public void setDIV_NAME(String DIV_NAME) {
        this.DIV_NAME = DIV_NAME;
    }

    public String getUCODE() {
        return UCODE;
    }

    public void setUCODE(String UCODE) {
        this.UCODE = UCODE;
    }
}
