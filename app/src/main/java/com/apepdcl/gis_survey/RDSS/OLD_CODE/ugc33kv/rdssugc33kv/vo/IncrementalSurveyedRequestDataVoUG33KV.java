package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo;

import com.google.gson.annotations.SerializedName;

public class IncrementalSurveyedRequestDataVoUG33KV {
    @SerializedName("FEEDER_ID")
    private String FEEDER_ID;
    @SerializedName("VOLTAGE_LEVEL")
    private String VOLTAGE_LEVEL;
    @SerializedName("POLE_NO")
    private int POLE_NO;
    @SerializedName("FROM_POLE")
    private int FROM_POLE;
    @SerializedName("POLE_TYPE")
    private String POLE_TYPE;
    @SerializedName("POLE_HEIGHT")
    private String POLE_HEIGHT;
   /* @SerializedName("NO_CIRCUITS")
    private String NO_CIRCUITS;*/
    @SerializedName("POLE_SUPPORT")
    private String POLE_SUPPORT;
    @SerializedName("SEC_CODE")
    private String SEC_CODE;
    @SerializedName("CREATED_BY")
    private String CREATED_BY;
    @SerializedName("TAPPING_POINT")
    private String TAPPING_POINT;
    @SerializedName("CONDUCTOR_SIZE")
    private String CONDUCTOR_SIZE;
    @SerializedName("POLE_PAINT_CODE")
    private String POLE_PAINT_CODE;
    @SerializedName("GLAT")
    private String GLAT;
    @SerializedName("GLONG")
    private String GLONG;
    @SerializedName("IME_NO")
    private String IME_NO;
    @SerializedName("POLE_CONFIG")
    private String POLE_CONFIG;
    @SerializedName("PIN_CUT")
    private String PIN_CUT;
    @SerializedName("NO_CKT")
    private String NO_CKT;
    @SerializedName("CKT_TYPE")
    private String CKT_TYPE;
    @SerializedName("END_POLE")
    private String END_POLE;
    @SerializedName("LINE_TYPE")
    private String LINE_TYPE;
    @SerializedName("CKT_NO")
    private String CKT_NO;
    @SerializedName("SS_NAME")
    private String SS_NAME;
    @SerializedName("DIFF_VOLTAGES")
    private String DIFF_VOLTAGES;
    @SerializedName("POLE_STRUCTURE")
    private String POLE_STRUCTURE;
    @SerializedName("SS_ID")
    private String SS_ID;
    @SerializedName("STJOINT_RMU")
    private String STJOINT_RMU;
    @SerializedName("RMU_TYPE")
    private String RMU_TYPE;
    @SerializedName("REMARKS")
    private String REMARKS;
    @SerializedName("TAPPING_LOCNAME")
    private String TAPPING_LOCNAME;
    @SerializedName("TAPPING_POINTS")
    private String TAPPING_POINTS;
    @SerializedName("WO_STATUS")
    private String WO_STATUS;
    @SerializedName("WO_ID")
    private String WO_ID;
    @SerializedName("IS_FEEDER_TYPE")
    private String IS_FEEDER_TYPE;
    @SerializedName("HT_SCNO")
    private String HT_SCNO;

    public String getHT_SCNO() {
        return HT_SCNO;
    }

    public void setHT_SCNO(String HT_SCNO) {
        this.HT_SCNO = HT_SCNO;
    }

    public String getIS_FEEDER_TYPE() {
        return IS_FEEDER_TYPE;
    }

    public void setIS_FEEDER_TYPE(String IS_FEEDER_TYPE) {
        this.IS_FEEDER_TYPE = IS_FEEDER_TYPE;
    }

    public String getWO_STATUS() {
        return WO_STATUS;
    }

    public void setWO_STATUS(String WO_STATUS) {
        this.WO_STATUS = WO_STATUS;
    }

    public String getWO_ID() {
        return WO_ID;
    }

    public void setWO_ID(String WO_ID) {
        this.WO_ID = WO_ID;
    }

    public String getREMARKS() {
        return REMARKS;
    }

    public void setREMARKS(String REMARKS) {
        this.REMARKS = REMARKS;
    }

    public String getTAPPING_LOCNAME() {
        return TAPPING_LOCNAME;
    }

    public void setTAPPING_LOCNAME(String TAPPING_LOCNAME) {
        this.TAPPING_LOCNAME = TAPPING_LOCNAME;
    }

    public String getTAPPING_POINTS() {
        return TAPPING_POINTS;
    }

    public void setTAPPING_POINTS(String TAPPING_POINTS) {
        this.TAPPING_POINTS = TAPPING_POINTS;
    }

    public IncrementalSurveyedRequestDataVoUG33KV() {
    }

    public String getSTJOINT_RMU() {
        return STJOINT_RMU;
    }

    public void setSTJOINT_RMU(String STJOINT_RMU) {
        this.STJOINT_RMU = STJOINT_RMU;
    }

    public String getRMU_TYPE() {
        return RMU_TYPE;
    }

    public void setRMU_TYPE(String RMU_TYPE) {
        this.RMU_TYPE = RMU_TYPE;
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

    public String getDIFF_VOLTAGES() {
        return DIFF_VOLTAGES;
    }

    public void setDIFF_VOLTAGES(String DIFF_VOLTAGES) {
        this.DIFF_VOLTAGES = DIFF_VOLTAGES;
    }

    public String getPOLE_STRUCTURE() {
        return POLE_STRUCTURE;
    }

    public void setPOLE_STRUCTURE(String POLE_STRUCTURE) {
        this.POLE_STRUCTURE = POLE_STRUCTURE;
    }

    public String getLINE_TYPE() {
        return LINE_TYPE;
    }

    public void setLINE_TYPE(String LINE_TYPE) {
        this.LINE_TYPE = LINE_TYPE;
    }

    public String getCKT_NO() {
        return CKT_NO;
    }

    public void setCKT_NO(String CKT_NO) {
        this.CKT_NO = CKT_NO;
    }

    public String getEND_POLE() {
        return END_POLE;
    }

    public void setEND_POLE(String END_POLE) {
        this.END_POLE = END_POLE;
    }

    public String getIME_NO() {
        return IME_NO;
    }

    public void setIME_NO(String IME_NO) {
        this.IME_NO = IME_NO;
    }

    public String getPOLE_CONFIG() {
        return POLE_CONFIG;
    }

    public void setPOLE_CONFIG(String POLE_CONFIG) {
        this.POLE_CONFIG = POLE_CONFIG;
    }

    public String getPIN_CUT() {
        return PIN_CUT;
    }

    public void setPIN_CUT(String PIN_CUT) {
        this.PIN_CUT = PIN_CUT;
    }

    public String getNO_CKT() {
        return NO_CKT;
    }

    public void setNO_CKT(String NO_CKT) {
        this.NO_CKT = NO_CKT;
    }

    public String getCKT_TYPE() {
        return CKT_TYPE;
    }

    public void setCKT_TYPE(String CKT_TYPE) {
        this.CKT_TYPE = CKT_TYPE;
    }

    public String getSEC_CODE() {
        return SEC_CODE;
    }

    public void setSEC_CODE(String SEC_CODE) {
        this.SEC_CODE = SEC_CODE;
    }

    public String getCREATED_BY() {
        return CREATED_BY;
    }

    public void setCREATED_BY(String CREATED_BY) {
        this.CREATED_BY = CREATED_BY;
    }

    public String getTAPPING_POINT() {
        return TAPPING_POINT;
    }

    public void setTAPPING_POINT(String TAPPING_POINT) {
        this.TAPPING_POINT = TAPPING_POINT;
    }

    public String getCONDUCTOR_SIZE() {
        return CONDUCTOR_SIZE;
    }

    public void setCONDUCTOR_SIZE(String CONDUCTOR_SIZE) {
        this.CONDUCTOR_SIZE = CONDUCTOR_SIZE;
    }

    public String getPOLE_PAINT_CODE() {
        return POLE_PAINT_CODE;
    }

    public void setPOLE_PAINT_CODE(String POLE_PAINT_CODE) {
        this.POLE_PAINT_CODE = POLE_PAINT_CODE;
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

    public int getPOLE_NO() {
        return POLE_NO;
    }

    public void setPOLE_NO(int POLE_NO) {
        this.POLE_NO = POLE_NO;
    }

    public int getFROM_POLE() {
        return FROM_POLE;
    }

    public void setFROM_POLE(int FROM_POLE) {
        this.FROM_POLE = FROM_POLE;
    }

    public String getFEEDER_ID() {
        return FEEDER_ID;
    }

    public void setFEEDER_ID(String FEEDER_ID) {
        this.FEEDER_ID = FEEDER_ID;
    }

    public String getVOLTAGE_LEVEL() {
        return VOLTAGE_LEVEL;
    }

    public void setVOLTAGE_LEVEL(String VOLTAGE_LEVEL) {
        this.VOLTAGE_LEVEL = VOLTAGE_LEVEL;
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

   /* public String getNO_CIRCUITS() {
        return NO_CIRCUITS;
    }

    public void setNO_CIRCUITS(String NO_CIRCUITS) {
        this.NO_CIRCUITS = NO_CIRCUITS;
    }*/

    public String getPOLE_SUPPORT() {
        return POLE_SUPPORT;
    }

    public void setPOLE_SUPPORT(String POLE_SUPPORT) {
        this.POLE_SUPPORT = POLE_SUPPORT;
    }
}
