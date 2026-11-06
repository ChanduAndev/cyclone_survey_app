package com.apepdcl.gis_survey.pmi_old.module_11kv.vo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PMILTImageRequest {
    @SerializedName("FEEDER_ID")
    String FEEDER_ID;
    @SerializedName("SURVEY_DATA")
    List<PMILTImagesModel> imagesList;

    public String getFEEDER_ID() {
        return FEEDER_ID;
    }

    public void setFEEDER_ID(String FEEDER_ID) {
        this.FEEDER_ID = FEEDER_ID;
    }

    public List<PMILTImagesModel> getImagesList() {
        return imagesList;
    }

    public void setImagesList(List<PMILTImagesModel> imagesList) {
        this.imagesList = imagesList;
    }
}
