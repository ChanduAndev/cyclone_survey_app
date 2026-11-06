package com.apepdcl.gis_survey.PMI.models;


/**
 * Created by SOUBHAGYA SWAIN on 29-03-2025.
 */
public class ImageModel {
    private String imagePath;
    private String subID;
    private String IMAGE_DESC;
    private String IMAGE_TYPE;
    private String VOLTAGE, POSITION, AI_RESPONSE, LEAN_ANGLE,POSTITION_XARM;

    public String getPOSTITION_XARM() {
        return POSTITION_XARM;
    }

    public void setPOSTITION_XARM(String POSTITION_XARM) {
        this.POSTITION_XARM = POSTITION_XARM;
    }

    public String getVOLTAGE() {
        return VOLTAGE;
    }

    public void setVOLTAGE(String VOLTAGE) {
        this.VOLTAGE = VOLTAGE;
    }

    public String getPOSITION() {
        return POSITION;
    }

    public void setPOSITION(String POSITION) {
        this.POSITION = POSITION;
    }

    public String getAI_RESPONSE() {
        return AI_RESPONSE;
    }

    public void setAI_RESPONSE(String AI_RESPONSE) {
        this.AI_RESPONSE = AI_RESPONSE;
    }

    public String getLEAN_ANGLE() {
        return LEAN_ANGLE;
    }

    public void setLEAN_ANGLE(String LEAN_ANGLE) {
        this.LEAN_ANGLE = LEAN_ANGLE;
    }

    public String getIMAGE_DESC() {
        return IMAGE_DESC;
    }

    public void setIMAGE_DESC(String IMAGE_DESC) {
        this.IMAGE_DESC = IMAGE_DESC;
    }

    public String getIMAGE_TYPE() {
        return IMAGE_TYPE;
    }

    public void setIMAGE_TYPE(String IMAGE_TYPE) {
        this.IMAGE_TYPE = IMAGE_TYPE;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getSubID() {
        return subID;
    }

    public void setSubID(String subID) {
        this.subID = subID;
    }
}
