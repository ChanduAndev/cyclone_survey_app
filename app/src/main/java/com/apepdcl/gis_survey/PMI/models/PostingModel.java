package com.apepdcl.gis_survey.PMI.models;

import java.util.ArrayList;

public class PostingModel {

    private String DEFECT_ID, POLE_ID, VOLTAGE_LEVEL, DTR_CODE, LT_CKT_CODE, FEEDER_ID, DEFECT_TYPE, DEFECT_SUB_TYPE, DEFECT_SUB_TYPE_ID, IMAGE, AI_IMAGE, DTR_IMAGE, USERID, USERNAME, RECT_REMARKS;
    private String DEFECT_BY_USER,AI_FLAG,GRP_ID;
    private String POLE_LAT,POLE_LONG,POLE_CONFIG;
    public String getPOLE_CONFIG() {
        return POLE_CONFIG;
    }
    public void setPOLE_CONFIG(String POLE_CONFIG) {
        this.POLE_CONFIG = POLE_CONFIG;
    }



    public String getPOLE_LAT() {
        return POLE_LAT;
    }

    public void setPOLE_LAT(String POLE_LAT) {
        this.POLE_LAT = POLE_LAT;
    }

    public String getPOLE_LONG() {
        return POLE_LONG;
    }

    public void setPOLE_LONG(String POLE_LONG) {
        this.POLE_LONG = POLE_LONG;
    }

    public String getGRP_ID() {
        return GRP_ID;
    }

    public void setGRP_ID(String GRP_ID) {
        this.GRP_ID = GRP_ID;
    }

    public String getAI_FLAG() {
        return AI_FLAG;
    }

    public void setAI_FLAG(String AI_FLAG) {
        this.AI_FLAG = AI_FLAG;
    }

    public String getDEFECT_BY_USER() {
        return DEFECT_BY_USER;
    }

    public void setDEFECT_BY_USER(String DEFECT_BY_USER) {
        this.DEFECT_BY_USER = DEFECT_BY_USER;
    }

    private String POLE_NO;
    private String ID;
    private String IMAGE_TYPE;

    public String getAI_IMAGE() {
        return AI_IMAGE;
    }

    public void setAI_IMAGE(String AI_IMAGE) {
        this.AI_IMAGE = AI_IMAGE;
    }

    private String VOLTAGE, POSITION, AI_RESPONSE, LEAN_ANGLE, POSTITION_XARM, CYCLONE, POLE_RATIO, PLINTH_RATIO, CURRENT_LAT, CURRENT_LONG, POLE_BBOX, CROP_BBOX;

    private String FROM;
    private String DEFECT_GROUP;

    public String getDEFECT_GROUP() {
        return DEFECT_GROUP;
    }

    public void setDEFECT_GROUP(String DEFECT_GROUP) {
        this.DEFECT_GROUP = DEFECT_GROUP;
    }

    public String getFROM() {
        return FROM;
    }

    public void setFROM(String FROM) {
        this.FROM = FROM;
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

    public String getCURRENT_LONG() {
        return CURRENT_LONG;
    }

    public void setCURRENT_LONG(String CURRENT_LONG) {
        this.CURRENT_LONG = CURRENT_LONG;
    }

    public String getCURRENT_LAT() {
        return CURRENT_LAT;
    }

    public void setCURRENT_LAT(String CURRENT_LAT) {
        this.CURRENT_LAT = CURRENT_LAT;
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

    public String getCYCLONE() {
        return CYCLONE;
    }

    public void setCYCLONE(String CYCLONE) {
        this.CYCLONE = CYCLONE;
    }

    private ArrayList<AIResponseModel> aiResponseList;

    public ArrayList<AIResponseModel> getAiResponseModel() {
        return aiResponseList;
    }

    public void setAiResponseModel(ArrayList<AIResponseModel> aiResponseModel) {
        this.aiResponseList = aiResponseModel;
    }

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

    public String getIMAGE_TYPE() {
        return IMAGE_TYPE;
    }

    public void setIMAGE_TYPE(String IMAGE_TYPE) {
        this.IMAGE_TYPE = IMAGE_TYPE;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPOLE_NO() {
        return POLE_NO;
    }

    public void setPOLE_NO(String POLE_NO) {
        this.POLE_NO = POLE_NO;
    }

    public String getDEFECT_SUB_TYPE_ID() {
        return DEFECT_SUB_TYPE_ID;
    }

    public void setDEFECT_SUB_TYPE_ID(String DEFECT_SUB_TYPE_ID) {
        this.DEFECT_SUB_TYPE_ID = DEFECT_SUB_TYPE_ID;
    }

    public String getDEFECT_ID() {
        return DEFECT_ID;
    }

    public String getLT_CKT_CODE() {
        return LT_CKT_CODE;
    }

    public void setLT_CKT_CODE(String LT_CKT_CODE) {
        this.LT_CKT_CODE = LT_CKT_CODE;
    }

    public void setDEFECT_ID(String DEFECT_ID) {
        this.DEFECT_ID = DEFECT_ID;
    }

    public String getPOLE_ID() {
        return POLE_ID;
    }

    public void setPOLE_ID(String POLE_ID) {
        this.POLE_ID = POLE_ID;
    }

    public String getVOLTAGE_LEVEL() {
        return VOLTAGE_LEVEL;
    }

    public void setVOLTAGE_LEVEL(String VOLTAGE_LEVEL) {
        this.VOLTAGE_LEVEL = VOLTAGE_LEVEL;
    }

    public String getDTR_CODE() {
        return DTR_CODE;
    }

    public void setDTR_CODE(String DTR_CODE) {
        this.DTR_CODE = DTR_CODE;
    }

    public String getFEEDER_ID() {
        return FEEDER_ID;
    }

    public void setFEEDER_ID(String FEEDER_ID) {
        this.FEEDER_ID = FEEDER_ID;
    }

    public String getDEFECT_TYPE() {
        return DEFECT_TYPE;
    }

    public void setDEFECT_TYPE(String DEFECT_TYPE) {
        this.DEFECT_TYPE = DEFECT_TYPE;
    }

    public String getDEFECT_SUB_TYPE() {
        return DEFECT_SUB_TYPE;
    }

    public void setDEFECT_SUB_TYPE(String DEFECT_SUB_TYPE) {
        this.DEFECT_SUB_TYPE = DEFECT_SUB_TYPE;
    }

    public String getIMAGE() {
        return IMAGE;
    }

    public void setIMAGE(String IMAGE) {
        this.IMAGE = IMAGE;
    }

    public String getDTR_IMAGE() {
        return DTR_IMAGE;
    }

    public void setDTR_IMAGE(String DTR_IMAGE) {
        this.DTR_IMAGE = DTR_IMAGE;
    }

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getRECT_REMARKS() {
        return RECT_REMARKS;
    }

    public void setRECT_REMARKS(String RECT_REMARKS) {
        this.RECT_REMARKS = RECT_REMARKS;
    }

}
