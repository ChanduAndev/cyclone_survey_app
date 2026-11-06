package com.apepdcl.gis_survey.PMI.models;


import java.util.ArrayList;

/**
 * Created by SOUBHAGYA SWAIN on 21-03-2025.
 */
public class DefectRVModel {
    ArrayList<DefectsModel> defectsModelArrayList;
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

    public ArrayList<DefectsModel> getDefectsModelArrayList() {
        return defectsModelArrayList;
    }

    public void setDefectsModelArrayList(ArrayList<DefectsModel> defectsModelArrayList) {
        this.defectsModelArrayList = defectsModelArrayList;
    }
}
