package com.apepdcl.gis_survey.pmi_old.module_11kv.vo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PMIImageRequest {
    @SerializedName("FEEDER_ID")
    String FEEDER_ID;
    @SerializedName("SURVEY_DATA")
    List<PMIImageList> imageList;

    public String getFEEDER_ID() {
        return FEEDER_ID;
    }

    public void setFEEDER_ID(String FEEDER_ID) {
        this.FEEDER_ID = FEEDER_ID;
    }

    public List<PMIImageList> getImageList() {
        return imageList;
    }

    public void setImageList(List<PMIImageList> imageList) {
        this.imageList = imageList;
    }
}
