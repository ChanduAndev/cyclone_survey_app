package com.apepdcl.gis_survey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Soubhagya Swain on 01,April,2024
 */
public class LTImageRequest {
    @SerializedName("FEEDER_ID")
    String FEEDER_ID;
    @SerializedName("SURVEY_DATA")
    List<LTImagesModel> imagesList;

    public String getFEEDER_ID() {
        return FEEDER_ID;
    }

    public void setFEEDER_ID(String FEEDER_ID) {
        this.FEEDER_ID = FEEDER_ID;
    }

    public List<LTImagesModel> getImagesList() {
        return imagesList;
    }

    public void setImagesList(List<LTImagesModel> imagesList) {
        this.imagesList = imagesList;
    }
}
