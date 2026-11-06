package com.apepdcl.gis_survey.estimates.sou;

public class MaterialModelPole {
    String SURVEY_CODE;
    String SURVEY_TYPE;
    String POLE_CONFIG;
    String MATERIAL_CODE;
    String MATERIAL_DESC;
    String UOM;
    String QTY;
    String DATA_ID;
    String id;

    String DATA_TYPE;
    String MAX;

    public String getDATA_TYPE() {
        return DATA_TYPE;
    }

    public void setDATA_TYPE(String DATA_TYPE) {
        this.DATA_TYPE = DATA_TYPE;
    }

    public String getMAX() {
        return MAX;
    }

    public void setMAX(String MAX) {
        this.MAX = MAX;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDATA_ID() {
        return DATA_ID;
    }

    public void setDATA_ID(String DATA_ID) {
        this.DATA_ID = DATA_ID;
    }

    public String getSURVEY_CODE() {
        return SURVEY_CODE;
    }

    public void setSURVEY_CODE(String SURVEY_CODE) {
        this.SURVEY_CODE = SURVEY_CODE;
    }

    public String getSURVEY_TYPE() {
        return SURVEY_TYPE;
    }

    public void setSURVEY_TYPE(String SURVEY_TYPE) {
        this.SURVEY_TYPE = SURVEY_TYPE;
    }

    public String getPOLE_CONFIG() {
        return POLE_CONFIG;
    }

    public void setPOLE_CONFIG(String POLE_CONFIG) {
        this.POLE_CONFIG = POLE_CONFIG;
    }

    public String getMATERIAL_CODE() {
        return MATERIAL_CODE;
    }

    public void setMATERIAL_CODE(String MATERIAL_CODE) {
        this.MATERIAL_CODE = MATERIAL_CODE;
    }

    public String getMATERIAL_DESC() {
        return MATERIAL_DESC;
    }

    public void setMATERIAL_DESC(String MATERIAL_DESC) {
        this.MATERIAL_DESC = MATERIAL_DESC;
    }

    public String getUOM() {
        return UOM;
    }

    public void setUOM(String UOM) {
        this.UOM = UOM;
    }

    public String getQTY() {
        return QTY;
    }

    public void setQTY(String QTY) {
        this.QTY = QTY;
    }
}
