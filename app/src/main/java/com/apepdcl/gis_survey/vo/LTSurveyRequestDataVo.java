package com.apepdcl.gis_survey.vo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LTSurveyRequestDataVo {


    @SerializedName("SEC_CODE")
    private String SEC_CODE;

    @SerializedName("IME_NO")
    private String IME_NO;

    @SerializedName("FEEDER_ID")
    private String FEEDER_ID;


    @SerializedName("DTR_STRUCTURE")
    private String DTR_STRUCTURE;

    @SerializedName("LT_CKT_CODE")
    private String LT_CKT_CODE;

    @SerializedName("END_POLE")
    private String END_POLE;

    @SerializedName("LINE_TYPE")
    private String LINE_TYPE;

    @SerializedName("LINE_CONFIG")
    private String LINE_CONFIG;

    @SerializedName("IS_OTHERSCS")
    private String IS_OTHERSCS;

    @SerializedName("IS_STREETLIGHT")
    private String IS_STREETLIGHT;



    @SerializedName("NO_AGL_SCS")
    private String NO_AGL_SCS;


    @SerializedName("NO_AQUA_SCS")
    private String NO_AQUA_SCS;


    @SerializedName("NO_INDUSTRIAL_SCS")
    private String NO_INDUSTRIAL_SCS;

    @SerializedName("POLE_NO")
    private String POLE_NO;


    @SerializedName("FROM_POLE")
    private String FROM_POLE;

//from



    @SerializedName("POLE_TYPE")
    private String POLE_TYPE;


    @SerializedName("POLE_HEIGHT")
    private String POLE_HEIGHT;


    @SerializedName("POLE_SUPPORT")
    private String POLE_SUPPORT;


    @SerializedName("PIN_CUT")
    private String PIN_CUT;


    @SerializedName("IS_DOUBLEFEEDING")
    private String IS_DOUBLEFEEDING;


    @SerializedName("TAPPING_POINT")
    private String TAPPING_POINT;


    @SerializedName("TAPPING_LOCNAME")
    private String TAPPING_LOCNAME;


    @SerializedName("TAPPING_POINTS_NO")
    private String TAPPING_POINTS_NO;


    @SerializedName("CONDUCTOR_SIZE")
    private String CONDUCTOR_SIZE;


    @SerializedName("IS_MULTICKT")
    private String IS_MULTICKT;

    //10


    @SerializedName("CKT_NO")
    private String CKT_NO;


    @SerializedName("CKT_TYPE")
    private String CKT_TYPE;


    @SerializedName("NO_33KV")
    private String NO_33KV;


    @SerializedName("NO_11KV")
    private String NO_11KV;


    @SerializedName("NO_6_3KV")
    private String NO_6_3KV;


    @SerializedName("NO_LT")
    private String NO_LT;


    @SerializedName("POLE_PAINT_CODE")
    private String POLE_PAINT_CODE;


    @SerializedName("CHECK_CONDITION")
    private String CHECK_CONDITION;


    @SerializedName("GLAT")
    private String GLAT;


    @SerializedName("GLONG")
    private String GLONG;

    //20

    @SerializedName("LOCATION_TYPE")
    private String LOCATION_TYPE;


    /*@SerializedName("RMU_TYPE")
    private String RMU_TYPE;*/


    @SerializedName("TOTAL_SERVICE")
    private String TOTAL_SERVICE;


   /* @SerializedName("RECORD_STATUS")
    private String RECORD_STATUS;
*/

    @SerializedName("REMARKS")
    private String REMARKS;

    @SerializedName("SERVICE_PILLERTYPE")
    private String  SERVICE_PILLERTYPE;

    @SerializedName("FEEDERPILLET_OUTGN_CKT")
    private String  FEEDERPILLET_OUTGN_CKT;

    @SerializedName("CABLE_SIZE")
    private String  CABLE_SIZE;

    @SerializedName("NO_CABLES")
    private String  NO_CABLES;
    @SerializedName("CREATED_BY")
    private String  CREATED_BY;

    @SerializedName("SECTION_ID")
    private String  SECTION_ID;

    @SerializedName("IS_CROSSINGS_AV")
    private String IS_CROSSINGS_AV;

    @SerializedName("LT_CROSS")
    private String LT_CROSS;

    @SerializedName("LT_6_3KV_CROSS")
    private String LT_6_3KV_CROSS;

    @SerializedName("LT_11KV_CROSS")
    private String LT_11KV_CROSS;

    @SerializedName("LT_33KV_CROSS")
    private String LT_33KV_CROSS;

    @SerializedName("TC_CROSS")
    private String TC_CROSS;

    @SerializedName("TM_CROSS")
    private String TM_CROSS;

    //NEW
    @SerializedName("DEFFECTS_AVAILABLE")
    private String DEFFECTS_AVAILABLE;
    @SerializedName("POLE_DEFFECTS")
    private String POLE_DEFFECTS;
    @SerializedName("CONDUCTOR_DEFFECTS")
    private String CONDUCTOR_DEFFECTS;
    @SerializedName("INSULATOR_DEFFECTS")
    private String INSULATOR_DEFFECTS;
    @SerializedName("CROSSARM_DEFFECTS")
    private String CROSSARM_DEFFECTS;
    @SerializedName("STRUT_DEFFECTS")
    private String STRUT_DEFFECTS;
    @SerializedName("STRAY_DEFFECTS")
    private String STRAY_DEFFECTS;
    @SerializedName("HTLT_CLEARANCE")
    private String HTLT_CLEARANCE;
    @SerializedName("INTERMEDIATE_POLE")
    private String INTERMEDIATE_POLE;
    @SerializedName("RESTRINGING")
    private String RESTRINGING;
    @SerializedName("OTHER_DEFFECTS")
    private String OTHER_DEFFECTS;

    @SerializedName("NUM_SCS")
    private String NUM_SCS;

    @SerializedName("IMAGE_1")
    private String IMAGE_1;
    @SerializedName("IMAGE_2")
    private String IMAGE_2;
    @SerializedName("IMAGE_3")
    private String IMAGE_3;

    @SerializedName("MC")
    List<MultiRequestVO> MC;

    @SerializedName("NUM_OTHER_CABLES")
    private String NUM_OTHER_CABLES;

    //JHC PHASE 1
    @SerializedName("POLE_SURVEY_TYPE")
    private String POLE_SURVEY_TYPE;

    @SerializedName("LINE_SURVEY_TYPE")
    private String LINE_SURVEY_TYPE;

    //POLE CONCRETE
    @SerializedName("POLE_CONCRETE")
    private String POLE_CONCRETE;

    //CURRENT LOCATION
    @SerializedName("CURRENT_LONG")
    private String CURRENT_LONG;
    @SerializedName("CURRENT_LAT")
    private String CURRENT_LAT;

    //ADDED ON 26-07-2024
    @SerializedName("STL_METER")
    private String STL_METER;
    @SerializedName("STL_SWITCH")
    private String STL_SWITCH;
    @SerializedName("STL_SOURCE")
    private String STL_SOURCE;

    @SerializedName("GPS_ACCURACY")
    private String GPS_ACCURACY;

    @SerializedName("RDSS_SCHEME_TYPE")
    private String RDSS_SCHEME_TYPE;

    @SerializedName("ADDRESS_1")
    private String ADDRESS_1;

    @SerializedName("ADDRESS_2")
    private String ADDRESS_2;

    @SerializedName("BLOCK_PREMISES")
    private String BLOCK_PREMISES;

    @SerializedName("VILLAGE")
    private String VILLAGE;

    @SerializedName("TOWN_CITY")
    private String TOWN_CITY;

    @SerializedName("STATE")
    private String STATE;

    public String getBLOCK_PREMISES() {
        return BLOCK_PREMISES;
    }

    public void setBLOCK_PREMISES(String BLOCK_PREMISES) {
        this.BLOCK_PREMISES = BLOCK_PREMISES;
    }

    public String getVILLAGE() {
        return VILLAGE;
    }

    public void setVILLAGE(String VILLAGE) {
        this.VILLAGE = VILLAGE;
    }

    public String getTOWN_CITY() {
        return TOWN_CITY;
    }

    public void setTOWN_CITY(String TOWN_CITY) {
        this.TOWN_CITY = TOWN_CITY;
    }

    public String getSTATE() {
        return STATE;
    }

    public void setSTATE(String STATE) {
        this.STATE = STATE;
    }

    public String getADDRESS_1() {
        return ADDRESS_1;
    }

    public void setADDRESS_1(String ADDRESS_1) {
        this.ADDRESS_1 = ADDRESS_1;
    }

    public String getADDRESS_2() {
        return ADDRESS_2;
    }

    public void setADDRESS_2(String ADDRESS_2) {
        this.ADDRESS_2 = ADDRESS_2;
    }

    public String getRDSS_SCHEME_TYPE() {
        return RDSS_SCHEME_TYPE;
    }

    public void setRDSS_SCHEME_TYPE(String RDSS_SCHEME_TYPE) {
        this.RDSS_SCHEME_TYPE = RDSS_SCHEME_TYPE;
    }

    public String getGPS_ACCURACY() {
        return GPS_ACCURACY;
    }

    public void setGPS_ACCURACY(String GPS_ACCURACY) {
        this.GPS_ACCURACY = GPS_ACCURACY;
    }

    public String getSTL_METER() {
        return STL_METER;
    }

    public void setSTL_METER(String STL_METER) {
        this.STL_METER = STL_METER;
    }

    public String getSTL_SWITCH() {
        return STL_SWITCH;
    }

    public void setSTL_SWITCH(String STL_SWITCH) {
        this.STL_SWITCH = STL_SWITCH;
    }

    public String getSTL_SOURCE() {
        return STL_SOURCE;
    }

    public void setSTL_SOURCE(String STL_SOURCE) {
        this.STL_SOURCE = STL_SOURCE;
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

    public String getPOLE_CONCRETE() {
        return POLE_CONCRETE;
    }

    public void setPOLE_CONCRETE(String POLE_CONCRETE) {
        this.POLE_CONCRETE = POLE_CONCRETE;
    }

    public String getPOLE_SURVEY_TYPE() {
        return POLE_SURVEY_TYPE;
    }

    public void setPOLE_SURVEY_TYPE(String POLE_SURVEY_TYPE) {
        this.POLE_SURVEY_TYPE = POLE_SURVEY_TYPE;
    }

    public String getLINE_SURVEY_TYPE() {
        return LINE_SURVEY_TYPE;
    }

    public void setLINE_SURVEY_TYPE(String LINE_SURVEY_TYPE) {
        this.LINE_SURVEY_TYPE = LINE_SURVEY_TYPE;
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

    public String getIMAGE_1() {
        return IMAGE_1;
    }

    public void setIMAGE_1(String IMAGE_1) {
        this.IMAGE_1 = IMAGE_1;
    }

    public String getIMAGE_2() {
        return IMAGE_2;
    }

    public void setIMAGE_2(String IMAGE_2) {
        this.IMAGE_2 = IMAGE_2;
    }

    public String getIMAGE_3() {
        return IMAGE_3;
    }

    public void setIMAGE_3(String IMAGE_3) {
        this.IMAGE_3 = IMAGE_3;
    }

    public String getNUM_SCS() {
        return NUM_SCS;
    }

    public void setNUM_SCS(String NUM_SCS) {
        this.NUM_SCS = NUM_SCS;
    }

    public String getOTHER_DEFFECTS() {
        return OTHER_DEFFECTS;
    }

    public void setOTHER_DEFFECTS(String OTHER_DEFFECTS) {
        this.OTHER_DEFFECTS = OTHER_DEFFECTS;
    }

    public String getDEFFECTS_AVAILABLE() {
        return DEFFECTS_AVAILABLE;
    }

    public void setDEFFECTS_AVAILABLE(String DEFFECTS_AVAILABLE) {
        this.DEFFECTS_AVAILABLE = DEFFECTS_AVAILABLE;
    }

    public String getPOLE_DEFFECTS() {
        return POLE_DEFFECTS;
    }

    public void setPOLE_DEFFECTS(String POLE_DEFFECTS) {
        this.POLE_DEFFECTS = POLE_DEFFECTS;
    }

    public String getCONDUCTOR_DEFFECTS() {
        return CONDUCTOR_DEFFECTS;
    }

    public void setCONDUCTOR_DEFFECTS(String CONDUCTOR_DEFFECTS) {
        this.CONDUCTOR_DEFFECTS = CONDUCTOR_DEFFECTS;
    }

    public String getINSULATOR_DEFFECTS() {
        return INSULATOR_DEFFECTS;
    }

    public void setINSULATOR_DEFFECTS(String INSULATOR_DEFFECTS) {
        this.INSULATOR_DEFFECTS = INSULATOR_DEFFECTS;
    }

    public String getCROSSARM_DEFFECTS() {
        return CROSSARM_DEFFECTS;
    }

    public void setCROSSARM_DEFFECTS(String CROSSARM_DEFFECTS) {
        this.CROSSARM_DEFFECTS = CROSSARM_DEFFECTS;
    }

    public String getSTRUT_DEFFECTS() {
        return STRUT_DEFFECTS;
    }

    public void setSTRUT_DEFFECTS(String STRUT_DEFFECTS) {
        this.STRUT_DEFFECTS = STRUT_DEFFECTS;
    }

    public String getSTRAY_DEFFECTS() {
        return STRAY_DEFFECTS;
    }

    public void setSTRAY_DEFFECTS(String STRAY_DEFFECTS) {
        this.STRAY_DEFFECTS = STRAY_DEFFECTS;
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

    public String getIS_CROSSINGS_AV() {
        return IS_CROSSINGS_AV;
    }

    public void setIS_CROSSINGS_AV(String IS_CROSSINGS_AV) {
        this.IS_CROSSINGS_AV = IS_CROSSINGS_AV;
    }

    public String getLT_CROSS() {
        return LT_CROSS;
    }

    public void setLT_CROSS(String LT_CROSS) {
        this.LT_CROSS = LT_CROSS;
    }

    public String getLT_6_3KV_CROSS() {
        return LT_6_3KV_CROSS;
    }

    public void setLT_6_3KV_CROSS(String LT_6_3KV_CROSS) {
        this.LT_6_3KV_CROSS = LT_6_3KV_CROSS;
    }

    public String getLT_11KV_CROSS() {
        return LT_11KV_CROSS;
    }

    public void setLT_11KV_CROSS(String LT_11KV_CROSS) {
        this.LT_11KV_CROSS = LT_11KV_CROSS;
    }

    public String getLT_33KV_CROSS() {
        return LT_33KV_CROSS;
    }

    public void setLT_33KV_CROSS(String LT_33KV_CROSS) {
        this.LT_33KV_CROSS = LT_33KV_CROSS;
    }

    public String getTC_CROSS() {
        return TC_CROSS;
    }

    public void setTC_CROSS(String TC_CROSS) {
        this.TC_CROSS = TC_CROSS;
    }

    public String getTM_CROSS() {
        return TM_CROSS;
    }

    public void setTM_CROSS(String TM_CROSS) {
        this.TM_CROSS = TM_CROSS;
    }

    public String getCHECK_CONDITION() {
        return CHECK_CONDITION;
    }

    public void setCHECK_CONDITION(String CHECK_CONDITION) {
        this.CHECK_CONDITION = CHECK_CONDITION;
    }
/* @SerializedName("DTR_METER_AVAILABLE")//change after
    private String  DTR_METER_AVAILABLE;

    @SerializedName("DTR_NO_OF_METERS")//change after
    private String  DTR_NO_OF_METERS;*/

    /*public String getDTR_METER_AVAILABLE() {
        return DTR_METER_AVAILABLE;
    }

    public void setDTR_METER_AVAILABLE(String DTR_METER_AVAILABLE) {
        this.DTR_METER_AVAILABLE = DTR_METER_AVAILABLE;
    }

    public String getDTR_NO_OF_METERS() {
        return DTR_NO_OF_METERS;
    }

    public void setDTR_NO_OF_METERS(String DTR_NO_OF_METERS) {
        this.DTR_NO_OF_METERS = DTR_NO_OF_METERS;
    }
*/
    public String getSECTION_ID() {
        return SECTION_ID;
    }

    public void setSECTION_ID(String SECTION_ID) {
        this.SECTION_ID = SECTION_ID;
    }

    public String getCREATED_BY() {
        return CREATED_BY;
    }

    public void setCREATED_BY(String CREATED_BY) {
        this.CREATED_BY = CREATED_BY;
    }

    public String getSERVICE_PILLERTYPE() {
        return SERVICE_PILLERTYPE;
    }

    public void setSERVICE_PILLERTYPE(String SERVICE_PILLERTYPE) {
        this.SERVICE_PILLERTYPE = SERVICE_PILLERTYPE;
    }

    public String getFEEDERPILLET_OUTGN_CKT() {
        return FEEDERPILLET_OUTGN_CKT;
    }

    public void setFEEDERPILLET_OUTGN_CKT(String FEEDERPILLET_OUTGN_CKT) {
        this.FEEDERPILLET_OUTGN_CKT = FEEDERPILLET_OUTGN_CKT;
    }

    public String getCABLE_SIZE() {
        return CABLE_SIZE;
    }

    public void setCABLE_SIZE(String CABLE_SIZE) {
        this.CABLE_SIZE = CABLE_SIZE;
    }

    public String getNO_CABLES() {
        return NO_CABLES;
    }

    public void setNO_CABLES(String NO_CABLES) {
        this.NO_CABLES = NO_CABLES;
    }

    public String getSEC_CODE() {
        return SEC_CODE;
    }

    public void setSEC_CODE(String SEC_CODE) {
        this.SEC_CODE = SEC_CODE;
    }

    public String getIME_NO() {
        return IME_NO;
    }

    public void setIME_NO(String IME_NO) {
        this.IME_NO = IME_NO;
    }

    public String getFEEDER_ID() {
        return FEEDER_ID;
    }

    public void setFEEDER_ID(String FEEDER_ID) {
        this.FEEDER_ID = FEEDER_ID;
    }

    public String getDTR_STRUCTURE() {
        return DTR_STRUCTURE;
    }

    public void setDTR_STRUCTURE(String DTR_STRUCTURE) {
        this.DTR_STRUCTURE = DTR_STRUCTURE;
    }

    public String getLT_CKT_CODE() {
        return LT_CKT_CODE;
    }

    public void setLT_CKT_CODE(String LT_CKT_CODE) {
        this.LT_CKT_CODE = LT_CKT_CODE;
    }

    public String getEND_POLE() {
        return END_POLE;
    }

    public void setEND_POLE(String END_POLE) {
        this.END_POLE = END_POLE;
    }

    public String getLINE_TYPE() {
        return LINE_TYPE;
    }

    public void setLINE_TYPE(String LINE_TYPE) {
        this.LINE_TYPE = LINE_TYPE;
    }

    public String getLINE_CONFIG() {
        return LINE_CONFIG;
    }

    public void setLINE_CONFIG(String LINE_CONFIG) {
        this.LINE_CONFIG = LINE_CONFIG;
    }

    public String getIS_OTHERSCS() {
        return IS_OTHERSCS;
    }

    public void setIS_OTHERSCS(String IS_OTHERSCS) {
        this.IS_OTHERSCS = IS_OTHERSCS;
    }

    public String getIS_STREETLIGHT() {
        return IS_STREETLIGHT;
    }

    public void setIS_STREETLIGHT(String IS_STREETLIGHT) {
        this.IS_STREETLIGHT = IS_STREETLIGHT;
    }

    public String getNO_AGL_SCS() {
        return NO_AGL_SCS;
    }

    public void setNO_AGL_SCS(String NO_AGL_SCS) {
        this.NO_AGL_SCS = NO_AGL_SCS;
    }

    public String getNO_AQUA_SCS() {
        return NO_AQUA_SCS;
    }

    public void setNO_AQUA_SCS(String NO_AQUA_SCS) {
        this.NO_AQUA_SCS = NO_AQUA_SCS;
    }

    public String getNO_INDUSTRIAL_SCS() {
        return NO_INDUSTRIAL_SCS;
    }

    public void setNO_INDUSTRIAL_SCS(String NO_INDUSTRIAL_SCS) {
        this.NO_INDUSTRIAL_SCS = NO_INDUSTRIAL_SCS;
    }

    public String getPOLE_NO() {
        return POLE_NO;
    }

    public void setPOLE_NO(String POLE_NO) {
        this.POLE_NO = POLE_NO;
    }

    public String getFROM_POLE() {
        return FROM_POLE;
    }

    public void setFROM_POLE(String FROM_POLE) {
        this.FROM_POLE = FROM_POLE;
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

    public String getPIN_CUT() {
        return PIN_CUT;
    }

    public void setPIN_CUT(String PIN_CUT) {
        this.PIN_CUT = PIN_CUT;
    }

    public String getIS_DOUBLEFEEDING() {
        return IS_DOUBLEFEEDING;
    }

    public void setIS_DOUBLEFEEDING(String IS_DOUBLEFEEDING) {
        this.IS_DOUBLEFEEDING = IS_DOUBLEFEEDING;
    }

    public String getTAPPING_POINT() {
        return TAPPING_POINT;
    }

    public void setTAPPING_POINT(String TAPPING_POINT) {
        this.TAPPING_POINT = TAPPING_POINT;
    }

    public String getTAPPING_LOCNAME() {
        return TAPPING_LOCNAME;
    }

    public void setTAPPING_LOCNAME(String TAPPING_LOCNAME) {
        this.TAPPING_LOCNAME = TAPPING_LOCNAME;
    }

    public String getTAPPING_POINTS_NO() {
        return TAPPING_POINTS_NO;
    }

    public void setTAPPING_POINTS_NO(String TAPPING_POINTS_NO) {
        this.TAPPING_POINTS_NO = TAPPING_POINTS_NO;
    }

    public String getCONDUCTOR_SIZE() {
        return CONDUCTOR_SIZE;
    }

    public void setCONDUCTOR_SIZE(String CONDUCTOR_SIZE) {
        this.CONDUCTOR_SIZE = CONDUCTOR_SIZE;
    }

    public String getIS_MULTICKT() {
        return IS_MULTICKT;
    }

    public void setIS_MULTICKT(String IS_MULTICKT) {
        this.IS_MULTICKT = IS_MULTICKT;
    }

    public String getCKT_NO() {
        return CKT_NO;
    }

    public void setCKT_NO(String CKT_NO) {
        this.CKT_NO = CKT_NO;
    }

    public String getCKT_TYPE() {
        return CKT_TYPE;
    }

    public void setCKT_TYPE(String CKT_TYPE) {
        this.CKT_TYPE = CKT_TYPE;
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

    public String getNO_6_3KV() {
        return NO_6_3KV;
    }

    public void setNO_6_3KV(String NO_6_3KV) {
        this.NO_6_3KV = NO_6_3KV;
    }

    public String getNO_LT() {
        return NO_LT;
    }

    public void setNO_LT(String NO_LT) {
        this.NO_LT = NO_LT;
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

    public String getLOCATION_TYPE() {
        return LOCATION_TYPE;
    }

    public void setLOCATION_TYPE(String LOCATION_TYPE) {
        this.LOCATION_TYPE = LOCATION_TYPE;
    }



    public String getTOTAL_SERVICE() {
        return TOTAL_SERVICE;
    }

    public void setTOTAL_SERVICE(String TOTAL_SERVICE) {
        this.TOTAL_SERVICE = TOTAL_SERVICE;
    }



    public String getREMARKS() {
        return REMARKS;
    }

    public void setREMARKS(String REMARKS) {
        this.REMARKS = REMARKS;
    }
}
