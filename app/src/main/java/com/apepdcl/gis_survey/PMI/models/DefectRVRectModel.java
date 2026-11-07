package com.apepdcl.gis_survey.PMI.models;

import java.util.ArrayList;

public class DefectRVRectModel {
    ArrayList<DefectsRectModel> defectsModelArrayList;
    String defect;
    private int selectedRadioButtonId;

    public int getSelectedRadioButtonId() {
        return selectedRadioButtonId;
    }

    public void setSelectedRadioButtonId(int selectedRadioButtonId) {
        this.selectedRadioButtonId = selectedRadioButtonId;
    }

    boolean isDefectSelected;

    public boolean isDefectSelected() {
        return isDefectSelected;
    }

    public void setDefectSelected(boolean defectSelected) {
        isDefectSelected = defectSelected;
    }

    public String getDefect() {
        return defect;
    }

    public void setDefect(String defect) {
        this.defect = defect;
    }

    public ArrayList<DefectsRectModel> getDefectsModelArrayList() {
        return defectsModelArrayList;
    }

    public void setDefectsModelArrayList(ArrayList<DefectsRectModel> defectsModelArrayList) {
        this.defectsModelArrayList = defectsModelArrayList;
    }
}
