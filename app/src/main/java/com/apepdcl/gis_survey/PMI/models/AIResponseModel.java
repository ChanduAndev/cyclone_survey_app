package com.apepdcl.gis_survey.PMI.models;


/**
 * Created by SOUBHAGYA SWAIN on 07-10-2025.
 */
public class AIResponseModel {
    private String VOLTAGE, POSITION, AI_RESPONSE, LEAN_ANGLE, POSTITION_XARM, POLE_RATIO, PLINTH_RATIO;
    private String POLE_BBOX,CROP_BBOX;
    private String VD_DEPTH,VC_REASON;
    private String IMAGE;

    public String getIMAGE() {
        return IMAGE;
    }

    public void setIMAGE(String IMAGE) {
        this.IMAGE = IMAGE;
    }

    public String getVD_DEPTH() {
        return VD_DEPTH;
    }

    public void setVD_DEPTH(String VD_DEPTH) {
        this.VD_DEPTH = VD_DEPTH;
    }

    public String getVC_REASON() {
        return VC_REASON;
    }

    public void setVC_REASON(String VC_REASON) {
        this.VC_REASON = VC_REASON;
    }

    public String getCROP_BBOX() {
        return CROP_BBOX;
    }

    public void setCROP_BBOX(String CROP_BBOX) {
        this.CROP_BBOX = CROP_BBOX;
    }

    public String getPOLE_BBOX() {
        return POLE_BBOX;
    }

    public void setPOLE_BBOX(String POLE_BBOX) {
        this.POLE_BBOX = POLE_BBOX;
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

    public String getPOSTITION_XARM() {
        return POSTITION_XARM;
    }

    public void setPOSTITION_XARM(String POSTITION_XARM) {
        this.POSTITION_XARM = POSTITION_XARM;
    }

    public String getPOLE_RATIO() {
        return POLE_RATIO;
    }

    public void setPOLE_RATIO(String POLE_RATIO) {
        this.POLE_RATIO = POLE_RATIO;
    }

    public String getPLINTH_RATIO() {
        return PLINTH_RATIO;
    }

    public void setPLINTH_RATIO(String PLINTH_RATIO) {
        this.PLINTH_RATIO = PLINTH_RATIO;
    }


}
