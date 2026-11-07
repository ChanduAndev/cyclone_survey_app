package com.apepdcl.gis_survey.PMI.models;

import java.util.List;

public class DefectGroupRectModel {
    private String defectGroup;
    private String voltageLevel;

    public String getVoltageLevel() {
        return voltageLevel;
    }

    public void setVoltageLevel(String voltageLevel) {
        this.voltageLevel = voltageLevel;
    }

    private boolean expanded;
    private boolean isPhotoNeeded;
    private boolean isImageSelected;
    String image ="";
    private List<DefectsModel> defects;

    public void setDefectGroup(String defectGroup) {
        this.defectGroup = defectGroup;
    }

    public void setDefects(List<DefectsModel> defects) {
        this.defects = defects;
    }

    public boolean isPhotoNeeded() {
        return isPhotoNeeded;
    }

    public void setPhotoNeeded(boolean photoNeeded) {
        isPhotoNeeded = photoNeeded;
    }

    public boolean isImageSelected() {
        return isImageSelected;
    }

    public void setImageSelected(boolean imageSelected) {
        isImageSelected = imageSelected;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public DefectGroupRectModel(String defectGroup, List<DefectsModel> defects) {
        this.defectGroup = defectGroup;
        this.defects = defects;
        this.expanded = false;
    }

    public String getDefectGroup() {
        return defectGroup;
    }

    public List<DefectsModel> getDefects() {
        return defects;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
