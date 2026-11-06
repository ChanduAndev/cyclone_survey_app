package com.apepdcl.gis_survey.pmi_old.module_11kv.vo;

import com.google.gson.annotations.SerializedName;

public class PMIDTRDetailsResponseVo {
    @SerializedName("DTR_CODE")
    private String DTR_CODE;
    @SerializedName("DTR_PHASE")
    private String DTR_PHASE;
    @SerializedName("DTR_SNO")
    private String DTR_SNO;
    @SerializedName("DTR_CAPACITY")
    private String DTR_CAPACITY;
    @SerializedName("DTR_MAKE")
    private String DTR_MAKE;
    @SerializedName("TYPE_OF_LOAD")
    private String TYPE_OF_LOAD;
    @SerializedName("STATUS")
    private String STATUS;
    @SerializedName("ERR_MSG")
    private String ERR_MSG;

    @SerializedName("GLAT")
    private String GLAT;

    @SerializedName("GLONG")
    private String GLONG;

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

    public String getDTR_CODE() {
        return DTR_CODE;
    }

    public void setDTR_CODE(String DTR_CODE) {
        this.DTR_CODE = DTR_CODE;
    }

    public String getDTR_PHASE() {
        return DTR_PHASE;
    }

    public void setDTR_PHASE(String DTR_PHASE) {
        this.DTR_PHASE = DTR_PHASE;
    }

    public String getDTR_SNO() {
        return DTR_SNO;
    }

    public void setDTR_SNO(String DTR_SNO) {
        this.DTR_SNO = DTR_SNO;
    }

    public String getDTR_CAPACITY() {
        return DTR_CAPACITY;
    }

    public void setDTR_CAPACITY(String DTR_CAPACITY) {
        this.DTR_CAPACITY = DTR_CAPACITY;
    }

    public String getDTR_MAKE() {
        return DTR_MAKE;
    }

    public void setDTR_MAKE(String DTR_MAKE) {
        this.DTR_MAKE = DTR_MAKE;
    }

    public String getTYPE_OF_LOAD() {
        return TYPE_OF_LOAD;
    }

    public void setTYPE_OF_LOAD(String TYPE_OF_LOAD) {
        this.TYPE_OF_LOAD = TYPE_OF_LOAD;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getERR_MSG() {
        return ERR_MSG;
    }

    public void setERR_MSG(String ERR_MSG) {
        this.ERR_MSG = ERR_MSG;
    }
}
