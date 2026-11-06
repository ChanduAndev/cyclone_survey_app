package com.apepdcl.gis_survey;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Soubhagya Swain on 01,April,2024
 */
public class LTImagesModel {
    @SerializedName("DTR_STRUCTURE")
    String DTR_STRUCTURE;
    @SerializedName("LT_CKT_CODE")
    String LT_CKT_CODE;
    @SerializedName("POLE_NO")
    String POLE_NO;
    @SerializedName("IMAGE_1")
    String IMAGE_1;
    @SerializedName("IMAGE_2")
    String IMAGE_2;
    @SerializedName("IMAGE_3")
    String IMAGE_3;



    public String getDTR_STRUCTURE() {
        return DTR_STRUCTURE;
    }

    public void setDTR_STRUCTURE(String DTR_STRUCTURE) {
        this.DTR_STRUCTURE = DTR_STRUCTURE;
    }

    public String getLT_CKT_CODE() {
        return LT_CKT_CODE;
    }

    public void setLT_CKT_CODE(String LT_CKT_CODE) {
        this.LT_CKT_CODE = LT_CKT_CODE;
    }

    public String getIMAGE_1() {
        return IMAGE_1;
    }

    public void setIMAGE_1(String IMAGE_1) {
        this.IMAGE_1 = IMAGE_1;
    }

    public String getIMAGE_2() {
        return IMAGE_2;
    }

    public void setIMAGE_2(String IMAGE_2) {
        this.IMAGE_2 = IMAGE_2;
    }

    public String getIMAGE_3() {
        return IMAGE_3;
    }

    public void setIMAGE_3(String IMAGE_3) {
        this.IMAGE_3 = IMAGE_3;
    }

    public String getPOLE_NO() {
        return POLE_NO;
    }

    public void setPOLE_NO(String POLE_NO) {
        this.POLE_NO = POLE_NO;
    }
}
