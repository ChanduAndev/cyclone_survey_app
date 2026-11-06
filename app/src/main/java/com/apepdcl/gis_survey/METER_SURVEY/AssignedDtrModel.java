package com.apepdcl.gis_survey.METER_SURVEY;


import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by SOUBHAGYA SWAIN on 17-06-2026.
 */
public class AssignedDtrModel implements Serializable {
    private String REC_ID;
    private String USERID;
    private String USER_NAME;
    private String ROLE;
    private String DTR_CODE;
    private String DTR_LOCATION;
    private String DTR_GLAT;
    private String DTR_GLONG;
    private String IS_LTCKT_AVAILABLE;
    private String CREATED_DT;
    private String SURVEY_COMPLETED;
    private String COMPLETION_DT;
    private String NO_OF_SCS;
    private String SECTION_NAME;
    private String UNMAPPED_POLES;
    private String UNMAPPED_SERVICES;
    private String LOAD_TYPE_ASGNED;
    private String LOAD_TYPE_SURVEYED;
    private String SECTION_ID;
    private String MOBILE_NO;

    public String getSECTION_ID() {
        return SECTION_ID;
    }

    public void setSECTION_ID(String SECTION_ID) {
        this.SECTION_ID = SECTION_ID;
    }

    public String getMOBILE_NO() {
        return MOBILE_NO;
    }

    public void setMOBILE_NO(String MOBILE_NO) {
        this.MOBILE_NO = MOBILE_NO;
    }

    public String getREC_ID() {
        return REC_ID;
    }

    public void setREC_ID(String REC_ID) {
        this.REC_ID = REC_ID;
    }

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    public String getROLE() {
        return ROLE;
    }

    public void setROLE(String ROLE) {
        this.ROLE = ROLE;
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

    public String getDTR_GLAT() {
        return DTR_GLAT;
    }

    public void setDTR_GLAT(String DTR_GLAT) {
        this.DTR_GLAT = DTR_GLAT;
    }

    public String getDTR_GLONG() {
        return DTR_GLONG;
    }

    public void setDTR_GLONG(String DTR_GLONG) {
        this.DTR_GLONG = DTR_GLONG;
    }

    public String getIS_LTCKT_AVAILABLE() {
        return IS_LTCKT_AVAILABLE;
    }

    public void setIS_LTCKT_AVAILABLE(String IS_LTCKT_AVAILABLE) {
        this.IS_LTCKT_AVAILABLE = IS_LTCKT_AVAILABLE;
    }

    public String getCREATED_DT() {
        return CREATED_DT;
    }

    public void setCREATED_DT(String CREATED_DT) {
        this.CREATED_DT = CREATED_DT;
    }

    public String getSURVEY_COMPLETED() {
        return SURVEY_COMPLETED;
    }

    public void setSURVEY_COMPLETED(String SURVEY_COMPLETED) {
        this.SURVEY_COMPLETED = SURVEY_COMPLETED;
    }

    public String getCOMPLETION_DT() {
        return COMPLETION_DT;
    }

    public void setCOMPLETION_DT(String COMPLETION_DT) {
        this.COMPLETION_DT = COMPLETION_DT;
    }

    public String getNO_OF_SCS() {
        return NO_OF_SCS;
    }

    public void setNO_OF_SCS(String NO_OF_SCS) {
        this.NO_OF_SCS = NO_OF_SCS;
    }

    public String getSECTION_NAME() {
        return SECTION_NAME;
    }

    public void setSECTION_NAME(String SECTION_NAME) {
        this.SECTION_NAME = SECTION_NAME;
    }

    public String getUNMAPPED_POLES() {
        return UNMAPPED_POLES;
    }

    public void setUNMAPPED_POLES(String UNMAPPED_POLES) {
        this.UNMAPPED_POLES = UNMAPPED_POLES;
    }

    public String getUNMAPPED_SERVICES() {
        return UNMAPPED_SERVICES;
    }

    public void setUNMAPPED_SERVICES(String UNMAPPED_SERVICES) {
        this.UNMAPPED_SERVICES = UNMAPPED_SERVICES;
    }

    public String getLOAD_TYPE_ASGNED() {
        return LOAD_TYPE_ASGNED;
    }

    public void setLOAD_TYPE_ASGNED(String LOAD_TYPE_ASGNED) {
        this.LOAD_TYPE_ASGNED = LOAD_TYPE_ASGNED;
    }

    public String getLOAD_TYPE_SURVEYED() {
        return LOAD_TYPE_SURVEYED;
    }

    public void setLOAD_TYPE_SURVEYED(String LOAD_TYPE_SURVEYED) {
        this.LOAD_TYPE_SURVEYED = LOAD_TYPE_SURVEYED;
    }
}
