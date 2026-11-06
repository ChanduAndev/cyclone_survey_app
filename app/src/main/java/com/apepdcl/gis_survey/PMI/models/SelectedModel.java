package com.apepdcl.gis_survey.PMI.models;


import android.graphics.Bitmap;

/**
 * Created by SOUBHAGYA SWAIN on 29-03-2025.
 */
public class SelectedModel {
    private String selectedSubtype;
    private Bitmap selectedImage;

    public String getSelectedSubtype() {
        return selectedSubtype;
    }

    public void setSelectedSubtype(String selectedSubtype) {
        this.selectedSubtype = selectedSubtype;
    }

    public Bitmap getSelectedImage() {
        return selectedImage;
    }

    public void setSelectedImage(Bitmap selectedImage) {
        this.selectedImage = selectedImage;
    }
}
