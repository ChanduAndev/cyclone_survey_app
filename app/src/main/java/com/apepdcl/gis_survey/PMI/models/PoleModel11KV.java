package com.apepdcl.gis_survey.PMI.models;

import java.io.Serializable;

public class PoleModel11KV implements Serializable {
    private String FEEDER_ID,POLE_NO,GLAT,GLONG;
    private String POLE_ID,POLE_CONFIG,POLE_TYPE,POLE_HEIGHT,POLE_SUPPORT,POLE_STRUCTURE;
    private String AI_FLAG,GRP_ID,DEFECT_SUB_TYPE,IMG_URL;

    public String getIMG_URL() {
        return IMG_URL;
    }

    public void setIMG_URL(String IMG_URL) {
        this.IMG_URL = IMG_URL;
    }

    public String getAI_FLAG() {
        return AI_FLAG;
    }

    public void setAI_FLAG(String AI_FLAG) {
        this.AI_FLAG = AI_FLAG;
    }

    public String getGRP_ID() {
        return GRP_ID;
    }

    public void setGRP_ID(String GRP_ID) {
        this.GRP_ID = GRP_ID;
    }

    public String getDEFECT_SUB_TYPE() {
        return DEFECT_SUB_TYPE;
    }

    public void setDEFECT_SUB_TYPE(String DEFECT_SUB_TYPE) {
        this.DEFECT_SUB_TYPE = DEFECT_SUB_TYPE;
    }

    private String DISTANCE;

    private String DEFECT_GROUP;
    private String DEFECT_TYPE;

    private String IS_RECTIFIED,AI_PENDING;

    public String getPOLE_STRUCTURE() {
        return POLE_STRUCTURE;
    }

    public void setPOLE_STRUCTURE(String POLE_STRUCTURE) {
        this.POLE_STRUCTURE = POLE_STRUCTURE;
    }

    public String getIS_RECTIFIED() {
        return IS_RECTIFIED;
    }

    public void setIS_RECTIFIED(String IS_RECTIFIED) {
        this.IS_RECTIFIED = IS_RECTIFIED;
    }

    public String getAI_PENDING() {
        return AI_PENDING;
    }

    public void setAI_PENDING(String AI_PENDING) {
        this.AI_PENDING = AI_PENDING;
    }

    public String getDEFECT_GROUP() {
        return DEFECT_GROUP;
    }

    public void setDEFECT_GROUP(String DEFECT_GROUP) {
        this.DEFECT_GROUP = DEFECT_GROUP;
    }

    public String getDEFECT_TYPE() {
        return DEFECT_TYPE;
    }

    public void setDEFECT_TYPE(String DEFECT_TYPE) {
        this.DEFECT_TYPE = DEFECT_TYPE;
    }

    public String getDISTANCE() {
        return DISTANCE;
    }

    public void setDISTANCE(String DISTANCE) {
        this.DISTANCE = DISTANCE;
    }

    public String getFEEDER_ID() {
        return FEEDER_ID;
    }

    public void setFEEDER_ID(String FEEDER_ID) {
        this.FEEDER_ID = FEEDER_ID;
    }

    public String getPOLE_NO() {
        return POLE_NO;
    }

    public void setPOLE_NO(String POLE_NO) {
        this.POLE_NO = POLE_NO;
    }

    public String getGLAT() {
        return GLAT;
    }

    public void setGLAT(String GLAT) {
        this.GLAT = GLAT;
    }

    public String getGLONG() {
        return GLONG;
    }

    public void setGLONG(String GLONG) {
        this.GLONG = GLONG;
    }

    public String getPOLE_ID() {
        return POLE_ID;
    }

    public void setPOLE_ID(String POLE_ID) {
        this.POLE_ID = POLE_ID;
    }

    public String getPOLE_CONFIG() {
        return POLE_CONFIG;
    }

    public void setPOLE_CONFIG(String POLE_CONFIG) {
        this.POLE_CONFIG = POLE_CONFIG;
    }

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

    public String getPOLE_SUPPORT() {
        return POLE_SUPPORT;
    }

    public void setPOLE_SUPPORT(String POLE_SUPPORT) {
        this.POLE_SUPPORT = POLE_SUPPORT;
    }
}
