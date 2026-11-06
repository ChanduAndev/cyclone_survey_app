package com.apepdcl.gis_survey.module_11KV.models;

import com.google.gson.annotations.SerializedName;

public class GisCoordinatsVO {

    @SerializedName("POLE_TYPE")
    String POLE_TYPE;

    @SerializedName("POLE_LEGTH")
    String POLE_HEIGHT;

    @SerializedName("GLONG")
    String X_VALUE;

    @SerializedName("GLAT")
    String Y_VALUE;

    public String getPOLE_TYPE() {
        return POLE_TYPE;
    }

    public void setPOLE_TYPE(String POLE_TYPE) {
        this.POLE_TYPE = POLE_TYPE;
    }

    public String getPOLE_HEIGHT() {
        return POLE_HEIGHT;
    }

    public void setPOLE_HEIGHT(String POLE_HEIGHT) {
        this.POLE_HEIGHT = POLE_HEIGHT;
    }

    public String getX_VALUE() {
        return X_VALUE;
    }

    public void setX_VALUE(String x_VALUE) {
        X_VALUE = x_VALUE;
    }

    public String getY_VALUE() {
        return Y_VALUE;
    }

    public void setY_VALUE(String y_VALUE) {
        Y_VALUE = y_VALUE;
    }
}
