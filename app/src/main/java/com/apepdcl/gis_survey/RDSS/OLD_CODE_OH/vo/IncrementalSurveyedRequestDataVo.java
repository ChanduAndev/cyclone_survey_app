package com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo;

import com.google.gson.annotations.SerializedName;

public class IncrementalSurveyedRequestDataVo {
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

    @SerializedName("DTR_CODE")
    private String DTR_CODE;

    @SerializedName("DTR_PHASE")
    private String DTR_PHASE;

    @SerializedName("DTR_CAPACITY")
    private String DTR_CAPACITY;

    @SerializedName("DTR_MAKE")
    private String DTR_MAKE;

    @SerializedName("DTR_STRUCTURE")
    private String DTR_STRUCTURE;

    @SerializedName("DTR_AB")
    private String DTR_AB;

    @SerializedName("DTR_LOC_NAME")
    private String DTR_LOC_NAME;

    @SerializedName("DTR_SNO")
    private String DTR_SNO;

    @SerializedName("NO_LT_CKT")
    private String NO_LT_CKT;

    @SerializedName("HT_SCNO")
    private String HT_SCNO;

    @SerializedName("CAPACITY_RATING")
    private String CAPACITY_RATING;

    @SerializedName("RECLOSER_TYPE")
    private String RECLOSER_TYPE;



    @SerializedName("TYPE_OF_LOAD")
    private String TYPE_OF_LOAD;

    @SerializedName("DTR_LOCATION_TYPE")
    private String DTR_LOCATION_TYPE;

    @SerializedName("IS_LTCKT_AVAILABLE")
    private String IS_LTCKT_AVAILABLE;


  /*  @SerializedName("NO_ABSWITCHS")
    private String NO_OF_AB_SWITCHES;*/

    @SerializedName("WO_STATUS")
    private String WO_STATUS;


    @SerializedName("WO_ID")
    private String WO_ID;

    @SerializedName("IS_FEEDER_TYPE")
    private String IS_FEEDER_TYPE;

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

    /*public String getNO_OF_AB_SWITCHES() {
        return NO_OF_AB_SWITCHES;
    }

    public void setNO_OF_AB_SWITCHES(String NO_OF_AB_SWITCHES) {
        this.NO_OF_AB_SWITCHES = NO_OF_AB_SWITCHES;
    }*/

    public String getTYPE_OF_LOAD() {
        return TYPE_OF_LOAD;
    }

    public void setTYPE_OF_LOAD(String TYPE_OF_LOAD) {
        this.TYPE_OF_LOAD = TYPE_OF_LOAD;
    }

    public String getDTR_LOCATION_TYPE() {
        return DTR_LOCATION_TYPE;
    }

    public void setDTR_LOCATION_TYPE(String DTR_LOCATION_TYPE) {
        this.DTR_LOCATION_TYPE = DTR_LOCATION_TYPE;
    }

    public String getIS_LTCKT_AVAILABLE() {
        return IS_LTCKT_AVAILABLE;
    }

    public void setIS_LTCKT_AVAILABLE(String IS_LTCKT_AVAILABLE) {
        this.IS_LTCKT_AVAILABLE = IS_LTCKT_AVAILABLE;
    }

    public String getCAPACITY_RATING() {
        return CAPACITY_RATING;
    }

    public void setCAPACITY_RATING(String CAPACITY_RATING) {
        this.CAPACITY_RATING = CAPACITY_RATING;
    }

    public String getRECLOSER_TYPE() {
        return RECLOSER_TYPE;
    }

    public void setRECLOSER_TYPE(String RECLOSER_TYPE) {
        this.RECLOSER_TYPE = RECLOSER_TYPE;
    }

    public String getHT_SCNO() {
        return HT_SCNO;
    }

    public void setHT_SCNO(String HT_SCNO) {
        this.HT_SCNO = HT_SCNO;
    }

    public String getNO_LT_CKT() {
        return NO_LT_CKT;
    }

    public void setNO_LT_CKT(String NO_LT_CKT) {
        this.NO_LT_CKT = NO_LT_CKT;
    }

    public String getDTR_SNO() {
        return DTR_SNO;
    }

    public void setDTR_SNO(String DTR_SNO) {
        this.DTR_SNO = DTR_SNO;
    }

    public String getDTR_LOC_NAME() {
        return DTR_LOC_NAME;
    }

    public void setDTR_LOC_NAME(String DTR_LOC_NAME) {
        this.DTR_LOC_NAME = DTR_LOC_NAME;
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

    public String getDTR_STRUCTURE() {
        return DTR_STRUCTURE;
    }

    public void setDTR_STRUCTURE(String DTR_STRUCTURE) {
        this.DTR_STRUCTURE = DTR_STRUCTURE;
    }

    public String getDTR_AB() {
        return DTR_AB;
    }

    public void setDTR_AB(String DTR_AB) {
        this.DTR_AB = DTR_AB;
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

    public IncrementalSurveyedRequestDataVo() {
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
