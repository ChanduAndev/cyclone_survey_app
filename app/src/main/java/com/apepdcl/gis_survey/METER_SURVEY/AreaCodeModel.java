package com.apepdcl.gis_survey.METER_SURVEY;

public class AreaCodeModel {

    private String areaCode;
    private String areaName;
    private boolean selected;

    public AreaCodeModel(String areaCode, String areaName) {
        this.areaCode = areaCode;
        this.areaName = areaName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}