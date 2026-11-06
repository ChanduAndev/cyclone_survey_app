package com.apepdcl.gis_survey.PMI.rectification.offline;

import com.apepdcl.gis_survey.PMI.models.OfflineDataFor11;

import java.util.ArrayList;

public class OfflineSurveyLists {

    private ArrayList<OfflineDataFor11> aiList;
    private ArrayList<OfflineDataFor11> nonAiList;

    public OfflineSurveyLists() {
        aiList = new ArrayList<>();
        nonAiList = new ArrayList<>();
    }

    public ArrayList<OfflineDataFor11> getAiList() {
        return aiList;
    }

    public ArrayList<OfflineDataFor11> getNonAiList() {
        return nonAiList;
    }
}
