package com.apepdcl.gis_survey.module_11KV;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Soubhagya Swain on 01,April,2024
 */
public class ImageRequest {
    @SerializedName("FEEDER_ID")
    String FEEDER_ID;
    @SerializedName("SURVEY_DATA")
    List<ImageList> imageList;

    public String getFEEDER_ID() {
        return FEEDER_ID;
    }

    public void setFEEDER_ID(String FEEDER_ID) {
        this.FEEDER_ID = FEEDER_ID;
    }

    public List<ImageList> getImageList() {
        return imageList;
    }

    public void setImageList(List<ImageList> imageList) {
        this.imageList = imageList;
    }
}
