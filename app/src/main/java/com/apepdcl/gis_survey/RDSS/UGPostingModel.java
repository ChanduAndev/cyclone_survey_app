package com.apepdcl.gis_survey.RDSS;


import com.apepdcl.gis_survey.PMI.models.FeedersData;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by SOUBHAGYA SWAIN on 19-05-2025.
 */
public class UGPostingModel implements Serializable {

    private String LOCATION_ID;
    private String LOCATION_NO;
    private String SOURCE_LOCATION;
    private String SS_NAME;
    private String SS_ID;
    private String SS_TYPE;
    private String CREATED_BY;
    private String WIDTH;
    private String DEPTH;
    private String UTILITY;
    private String COUNT_CABLES;
    private String CABLE_33KV;
    private String CABLE_11KV;
    private String CABLE_LT;
    private String LAT;
    private String LON;
    private String VOLTAGE;
    private String IS_FRESH_SURVEY;
    private String FROM_LAT;
    private String FROM_LON;
    private String LOCATION_TYPE;
    private String RMU_TYPE;
    private String DTR_CAPACITY;
    private String HT_SCNO;
    private String UTILITY_TYPE;
    private String LOCATION_REMARKS;
    private String ID;
    private String MODE;
    private String CABLE_SIZE;
    private String PROTECTION_PIPE;

    private String REMARKS;
    private String DTR_PHASE;
    private String DTR_CODE;
    private String DTR_LOCATION;
    private String GPS_ACCURACY;

    public String getGPS_ACCURACY() {
        return GPS_ACCURACY;
    }

    public void setGPS_ACCURACY(String GPS_ACCURACY) {
        this.GPS_ACCURACY = GPS_ACCURACY;
    }

    public String getREMARKS() {
        return REMARKS;
    }

    public void setREMARKS(String REMARKS) {
        this.REMARKS = REMARKS;
    }

    public String getDTR_PHASE() {
        return DTR_PHASE;
    }

    public void setDTR_PHASE(String DTR_PHASE) {
        this.DTR_PHASE = DTR_PHASE;
    }

    public String getDTR_CODE() {
        return DTR_CODE;
    }

    public void setDTR_CODE(String DTR_CODE) {
        this.DTR_CODE = DTR_CODE;
    }

    public String getDTR_LOCATION() {
        return DTR_LOCATION;
    }

    public void setDTR_LOCATION(String DTR_LOCATION) {
        this.DTR_LOCATION = DTR_LOCATION;
    }

    public String getPROTECTION_PIPE() {
        return PROTECTION_PIPE;
    }

    public void setPROTECTION_PIPE(String PROTECTION_PIPE) {
        this.PROTECTION_PIPE = PROTECTION_PIPE;
    }

    public String getCABLE_SIZE() {
        return CABLE_SIZE;
    }

    public void setCABLE_SIZE(String CABLE_SIZE) {
        this.CABLE_SIZE = CABLE_SIZE;
    }

    public String getMODE() {
        return MODE;
    }

    public void setMODE(String MODE) {
        this.MODE = MODE;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    private ArrayList<FeedersData> FEEDERS;

    public ArrayList<FeedersData> getFeedersData() {
        return FEEDERS;
    }

    public void setFeedersData(ArrayList<FeedersData> FEEDERS) {
        this.FEEDERS = FEEDERS;
    }



    public String getLOCATION_REMARKS() {
        return LOCATION_REMARKS;
    }

    public void setLOCATION_REMARKS(String LOCATION_REMARKS) {
        this.LOCATION_REMARKS = LOCATION_REMARKS;
    }

    public String getUTILITY_TYPE() {
        return UTILITY_TYPE;
    }

    public void setUTILITY_TYPE(String UTILITY_TYPE) {
        this.UTILITY_TYPE = UTILITY_TYPE;
    }

    public String getLOCATION_TYPE() {
        return LOCATION_TYPE;
    }

    public void setLOCATION_TYPE(String LOCATION_TYPE) {
        this.LOCATION_TYPE = LOCATION_TYPE;
    }

    public String getRMU_TYPE() {
        return RMU_TYPE;
    }

    public void setRMU_TYPE(String RMU_TYPE) {
        this.RMU_TYPE = RMU_TYPE;
    }

    public String getDTR_CAPACITY() {
        return DTR_CAPACITY;
    }

    public void setDTR_CAPACITY(String DTR_CAPACITY) {
        this.DTR_CAPACITY = DTR_CAPACITY;
    }

    public String getHT_SCNO() {
        return HT_SCNO;
    }

    public void setHT_SCNO(String HT_SCNO) {
        this.HT_SCNO = HT_SCNO;
    }

    public String getFROM_LAT() {
        return FROM_LAT;
    }

    public void setFROM_LAT(String FROM_LAT) {
        this.FROM_LAT = FROM_LAT;
    }

    public String getFROM_LON() {
        return FROM_LON;
    }

    public void setFROM_LON(String FROM_LON) {
        this.FROM_LON = FROM_LON;
    }

    public String getIS_FRESH_SURVEY() {
        return IS_FRESH_SURVEY;
    }

    public void setIS_FRESH_SURVEY(String IS_FRESH_SURVEY) {
        this.IS_FRESH_SURVEY = IS_FRESH_SURVEY;
    }

    public String getVOLTAGE() {
        return VOLTAGE;
    }

    public void setVOLTAGE(String VOLTAGE) {
        this.VOLTAGE = VOLTAGE;
    }

    public String getLOCATION_ID() {
        return LOCATION_ID;
    }

    public void setLOCATION_ID(String LOCATION_ID) {
        this.LOCATION_ID = LOCATION_ID;
    }

    public String getLOCATION_NO() {
        return LOCATION_NO;
    }

    public void setLOCATION_NO(String LOCATION_NO) {
        this.LOCATION_NO = LOCATION_NO;
    }

    public String getSOURCE_LOCATION() {
        return SOURCE_LOCATION;
    }

    public void setSOURCE_LOCATION(String SOURCE_LOCATION) {
        this.SOURCE_LOCATION = SOURCE_LOCATION;
    }

    public String getSS_NAME() {
        return SS_NAME;
    }

    public void setSS_NAME(String SS_NAME) {
        this.SS_NAME = SS_NAME;
    }

    public String getSS_ID() {
        return SS_ID;
    }

    public void setSS_ID(String SS_ID) {
        this.SS_ID = SS_ID;
    }

    public String getSS_TYPE() {
        return SS_TYPE;
    }

    public void setSS_TYPE(String SS_TYPE) {
        this.SS_TYPE = SS_TYPE;
    }

    public String getCREATED_BY() {
        return CREATED_BY;
    }

    public void setCREATED_BY(String CREATED_BY) {
        this.CREATED_BY = CREATED_BY;
    }

    public String getWIDTH() {
        return WIDTH;
    }

    public void setWIDTH(String WIDTH) {
        this.WIDTH = WIDTH;
    }

    public String getDEPTH() {
        return DEPTH;
    }

    public void setDEPTH(String DEPTH) {
        this.DEPTH = DEPTH;
    }

    public String getUTILITY() {
        return UTILITY;
    }

    public void setUTILITY(String UTILITY) {
        this.UTILITY = UTILITY;
    }

    public String getCOUNT_CABLES() {
        return COUNT_CABLES;
    }

    public void setCOUNT_CABLES(String COUNT_CABLES) {
        this.COUNT_CABLES = COUNT_CABLES;
    }

    public String getCABLE_33KV() {
        return CABLE_33KV;
    }

    public void setCABLE_33KV(String CABLE_33KV) {
        this.CABLE_33KV = CABLE_33KV;
    }

    public String getCABLE_11KV() {
        return CABLE_11KV;
    }

    public void setCABLE_11KV(String CABLE_11KV) {
        this.CABLE_11KV = CABLE_11KV;
    }

    public String getCABLE_LT() {
        return CABLE_LT;
    }

    public void setCABLE_LT(String CABLE_LT) {
        this.CABLE_LT = CABLE_LT;
    }

    public String getLAT() {
        return LAT;
    }

    public void setLAT(String LAT) {
        this.LAT = LAT;
    }

    public String getLON() {
        return LON;
    }

    public void setLON(String LON) {
        this.LON = LON;
    }
}
