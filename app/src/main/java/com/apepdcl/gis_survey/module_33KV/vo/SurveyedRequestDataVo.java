package com.apepdcl.gis_survey.module_33KV.vo;

import com.apepdcl.gis_survey.vo.MultiRequestVO;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SurveyedRequestDataVo {
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

    @SerializedName("WO_ID")
    private String WO_ID;
    @SerializedName("IS_FEEDER_TYPE")
    private String IS_FEEDER_TYPE;

    @SerializedName("HT_SCNO")
    private String HT_SCNO;


    @SerializedName("SURVEY_TYPE")
    private String SURVEY_TYPE;

    //MULTI CIRCUIT
    @SerializedName("MC")
    List<MultiRequestVO> MC;
    @SerializedName("NO_33KV")
    private String NO_33KV;
    @SerializedName("NO_11KV")
    private String NO_11KV;
    @SerializedName("NO_LT")
    private String NO_LT;

    //DEFECTS
    @SerializedName("DEFFECTS_AVAILABLE")
    private String DEFFECTS_AVAILABLE;
    @SerializedName("POLE_DEFECTS")
    private String POLE_DEFECTS;
    @SerializedName("CONDUCTOR_DEFECTS")
    private String CONDUCTOR_DEFECTS;
    @SerializedName("AB_SWITCH_DEFECTS")
    private String AB_SWITCH_DEFECTS;
    @SerializedName("INSULATOR_DEFECTS")
    private String INSULATOR_DEFECTS;
    @SerializedName("CROSSARM_DEFECTS")
    private String CROSSARM_DEFECTS	;
    @SerializedName("TOPCLEAT_DEFECTS")
    private String TOPCLEAT_DEFECTS;
    @SerializedName("STRUT_DEFECTS")
    private String STRUT_DEFECTS;
    @SerializedName("STRAY_DEFECTS")
    private String STRAY_DEFECTS;
    @SerializedName("HTLT_CLEARANCE")
    private String HTLT_CLEARANCE;
    @SerializedName("INTERMEDIATE_POLE")
    private String INTERMEDIATE_POLE;
    @SerializedName("RESTRINGING")
    private String RESTRINGING;
    @SerializedName("TREE_CLEARANCE")
    private String TREE_CLEARANCE;
    @SerializedName("OTHER_DEFFECTS")
    private String OTHER_DEFFECTS;

    //CROSSING
    @SerializedName("CROSS_LT_CKT")
    private String CROSS_LT_CKT;
    @SerializedName("CROSS_6_3KV_ID")
    private String CROSS_6_3KV_ID;
    @SerializedName("CROSS_11KV_ID")
    private String CROSS_11KV_ID;
    @SerializedName("CROSS_33KV_ID")
    private String CROSS_33KV_ID;
    @SerializedName("CROSS_EHT")
    private String CROSS_EHT;
    @SerializedName("CROSS_COMMUN")
    private String CROSS_COMMUN;
    @SerializedName("CROSS_REMARKS")
    private String CROSS_REMARKS;
    @SerializedName("CROSSING_TYPE")
    private String CROSSING_TYPE;

    //OTHER CABLES
    @SerializedName("NUM_OTHER_CABLES")
    private String NUM_OTHER_CABLES;

    //POLE CONCRETE
    @SerializedName("POLE_CONCRETE")
    private String POLE_CONCRETE;

    //POLE CONCRETE
    @SerializedName("CURRENT_LAT")
    private String CURRENT_LAT;
    @SerializedName("CURRENT_LONG")
    private String CURRENT_LONG;

    @SerializedName("GPS_ACCURACY")
    private String GPS_ACCURACY;

    @SerializedName("DEVICE_ID")
    private String DEVICE_ID;

    public String getDEVICE_ID() {
        return DEVICE_ID;
    }

    public void setDEVICE_ID(String DEVICE_ID) {
        this.DEVICE_ID = DEVICE_ID;
    }

    public String getGPS_ACCURACY() {
        return GPS_ACCURACY;
    }

    public void setGPS_ACCURACY(String GPS_ACCURACY) {
        this.GPS_ACCURACY = GPS_ACCURACY;
    }

    public String getCURRENT_LAT() {
        return CURRENT_LAT;
    }

    public void setCURRENT_LAT(String CURRENT_LAT) {
        this.CURRENT_LAT = CURRENT_LAT;
    }

    public String getCURRENT_LONG() {
        return CURRENT_LONG;
    }

    public void setCURRENT_LONG(String CURRENT_LONG) {
        this.CURRENT_LONG = CURRENT_LONG;
    }

    public String getPOLE_CONCRETE() {
        return POLE_CONCRETE;
    }

    public void setPOLE_CONCRETE(String POLE_CONCRETE) {
        this.POLE_CONCRETE = POLE_CONCRETE;
    }

    public String getDEFFECTS_AVAILABLE() {
        return DEFFECTS_AVAILABLE;
    }

    public void setDEFFECTS_AVAILABLE(String DEFFECTS_AVAILABLE) {
        this.DEFFECTS_AVAILABLE = DEFFECTS_AVAILABLE;
    }

    public String getPOLE_DEFECTS() {
        return POLE_DEFECTS;
    }

    public void setPOLE_DEFECTS(String POLE_DEFECTS) {
        this.POLE_DEFECTS = POLE_DEFECTS;
    }

    public String getCONDUCTOR_DEFECTS() {
        return CONDUCTOR_DEFECTS;
    }

    public void setCONDUCTOR_DEFECTS(String CONDUCTOR_DEFECTS) {
        this.CONDUCTOR_DEFECTS = CONDUCTOR_DEFECTS;
    }

    public String getAB_SWITCH_DEFECTS() {
        return AB_SWITCH_DEFECTS;
    }

    public void setAB_SWITCH_DEFECTS(String AB_SWITCH_DEFECTS) {
        this.AB_SWITCH_DEFECTS = AB_SWITCH_DEFECTS;
    }

    public String getINSULATOR_DEFECTS() {
        return INSULATOR_DEFECTS;
    }

    public void setINSULATOR_DEFECTS(String INSULATOR_DEFECTS) {
        this.INSULATOR_DEFECTS = INSULATOR_DEFECTS;
    }

    public String getCROSSARM_DEFECTS() {
        return CROSSARM_DEFECTS;
    }

    public void setCROSSARM_DEFECTS(String CROSSARM_DEFECTS) {
        this.CROSSARM_DEFECTS = CROSSARM_DEFECTS;
    }

    public String getTOPCLEAT_DEFECTS() {
        return TOPCLEAT_DEFECTS;
    }

    public void setTOPCLEAT_DEFECTS(String TOPCLEAT_DEFECTS) {
        this.TOPCLEAT_DEFECTS = TOPCLEAT_DEFECTS;
    }

    public String getSTRUT_DEFECTS() {
        return STRUT_DEFECTS;
    }

    public void setSTRUT_DEFECTS(String STRUT_DEFECTS) {
        this.STRUT_DEFECTS = STRUT_DEFECTS;
    }

    public String getSTRAY_DEFECTS() {
        return STRAY_DEFECTS;
    }

    public void setSTRAY_DEFECTS(String STRAY_DEFECTS) {
        this.STRAY_DEFECTS = STRAY_DEFECTS;
    }

    public String getHTLT_CLEARANCE() {
        return HTLT_CLEARANCE;
    }

    public void setHTLT_CLEARANCE(String HTLT_CLEARANCE) {
        this.HTLT_CLEARANCE = HTLT_CLEARANCE;
    }

    public String getINTERMEDIATE_POLE() {
        return INTERMEDIATE_POLE;
    }

    public void setINTERMEDIATE_POLE(String INTERMEDIATE_POLE) {
        this.INTERMEDIATE_POLE = INTERMEDIATE_POLE;
    }

    public String getRESTRINGING() {
        return RESTRINGING;
    }

    public void setRESTRINGING(String RESTRINGING) {
        this.RESTRINGING = RESTRINGING;
    }

    public String getTREE_CLEARANCE() {
        return TREE_CLEARANCE;
    }

    public void setTREE_CLEARANCE(String TREE_CLEARANCE) {
        this.TREE_CLEARANCE = TREE_CLEARANCE;
    }

    public String getOTHER_DEFFECTS() {
        return OTHER_DEFFECTS;
    }

    public void setOTHER_DEFFECTS(String OTHER_DEFFECTS) {
        this.OTHER_DEFFECTS = OTHER_DEFFECTS;
    }

    public String getCROSS_LT_CKT() {
        return CROSS_LT_CKT;
    }

    public void setCROSS_LT_CKT(String CROSS_LT_CKT) {
        this.CROSS_LT_CKT = CROSS_LT_CKT;
    }

    public String getCROSS_6_3KV_ID() {
        return CROSS_6_3KV_ID;
    }

    public void setCROSS_6_3KV_ID(String CROSS_6_3KV_ID) {
        this.CROSS_6_3KV_ID = CROSS_6_3KV_ID;
    }

    public String getCROSS_11KV_ID() {
        return CROSS_11KV_ID;
    }

    public void setCROSS_11KV_ID(String CROSS_11KV_ID) {
        this.CROSS_11KV_ID = CROSS_11KV_ID;
    }

    public String getCROSS_33KV_ID() {
        return CROSS_33KV_ID;
    }

    public void setCROSS_33KV_ID(String CROSS_33KV_ID) {
        this.CROSS_33KV_ID = CROSS_33KV_ID;
    }

    public String getCROSS_EHT() {
        return CROSS_EHT;
    }

    public void setCROSS_EHT(String CROSS_EHT) {
        this.CROSS_EHT = CROSS_EHT;
    }

    public String getCROSS_COMMUN() {
        return CROSS_COMMUN;
    }

    public void setCROSS_COMMUN(String CROSS_COMMUN) {
        this.CROSS_COMMUN = CROSS_COMMUN;
    }

    public String getCROSS_REMARKS() {
        return CROSS_REMARKS;
    }

    public void setCROSS_REMARKS(String CROSS_REMARKS) {
        this.CROSS_REMARKS = CROSS_REMARKS;
    }

    public String getCROSSING_TYPE() {
        return CROSSING_TYPE;
    }

    public void setCROSSING_TYPE(String CROSSING_TYPE) {
        this.CROSSING_TYPE = CROSSING_TYPE;
    }

    public String getNUM_OTHER_CABLES() {
        return NUM_OTHER_CABLES;
    }

    public void setNUM_OTHER_CABLES(String NUM_OTHER_CABLES) {
        this.NUM_OTHER_CABLES = NUM_OTHER_CABLES;
    }

    public List<MultiRequestVO> getMC() {
        return MC;
    }

    public void setMC(List<MultiRequestVO> MC) {
        this.MC = MC;
    }

    public String getNO_33KV() {
        return NO_33KV;
    }

    public void setNO_33KV(String NO_33KV) {
        this.NO_33KV = NO_33KV;
    }

    public String getNO_11KV() {
        return NO_11KV;
    }

    public void setNO_11KV(String NO_11KV) {
        this.NO_11KV = NO_11KV;
    }

    public String getNO_LT() {
        return NO_LT;
    }

    public void setNO_LT(String NO_LT) {
        this.NO_LT = NO_LT;
    }

    public String getSURVEY_TYPE() {
        return SURVEY_TYPE;
    }

    public void setSURVEY_TYPE(String SURVEY_TYPE) {
        this.SURVEY_TYPE = SURVEY_TYPE;
    }

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

    public SurveyedRequestDataVo() {
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
