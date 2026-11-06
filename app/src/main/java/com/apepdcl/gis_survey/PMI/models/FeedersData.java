package com.apepdcl.gis_survey.PMI.models;

import java.io.Serializable;

public class FeedersData implements Serializable {
    private String FEEDER_ID;
    private String VOLTAGE_LEVEL;
    private String FEEDER_NAME;
    private int FDR_INDEX;
    private String SS_ID;
    private String SS_NAME;
    private String CABLE_SIZE;
    private String REMARKS;
    private String DTR_PHASE;
    private String DTR_CAPACITY;
    private String DTR_CODE;
    private String DTR_LOCATION;

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

    public String getDTR_PHASE() {
        return DTR_PHASE;
    }

    public void setDTR_PHASE(String DTR_PHASE) {
        this.DTR_PHASE = DTR_PHASE;
    }

    public String getDTR_CAPACITY() {
        return DTR_CAPACITY;
    }

    public void setDTR_CAPACITY(String DTR_CAPACITY) {
        this.DTR_CAPACITY = DTR_CAPACITY;
    }

    public String getREMARKS() {
        return REMARKS;
    }

    public void setREMARKS(String REMARKS) {
        this.REMARKS = REMARKS;
    }

    public String getCABLE_SIZE() {
        return CABLE_SIZE;
    }

    public void setCABLE_SIZE(String CABLE_SIZE) {
        this.CABLE_SIZE = CABLE_SIZE;
    }

    public String getSS_ID() {
        return SS_ID;
    }

    public void setSS_ID(String SS_ID) {
        this.SS_ID = SS_ID;
    }

    public String getSS_NAME() {
        return SS_NAME;
    }

    public void setSS_NAME(String SS_NAME) {
        this.SS_NAME = SS_NAME;
    }

    public int getFDR_INDEX() {
        return FDR_INDEX;
    }

    public void setFDR_INDEX(int FDR_INDEX) {
        this.FDR_INDEX = FDR_INDEX;
    }

    // Default constructor
    public FeedersData() {}

    // Getters and setters
    public String getFeeder_id() {
        return FEEDER_ID;
    }

    public void setFeeder_id(String feeder_id) {
        this.FEEDER_ID = feeder_id;
    }

    public String getVoltage_level() {
        return VOLTAGE_LEVEL;
    }

    public void setVoltage_level(String voltage_level) {
        this.VOLTAGE_LEVEL = voltage_level;
    }

    public String getFeeder_name() {
        return FEEDER_NAME;
    }

    public void setFeeder_name(String feeder_name) {
        this.FEEDER_NAME = feeder_name;
    }
}
