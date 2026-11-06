package com.apepdcl.gis_survey.pmi_old.module_11kv.vo;

public class PMISurveyDataVo {
    private String DTR_STRUCTURE;
    private String LT_CIRCUIT;
    private int POLE_NO;
    private int FROM_POLE;
    private String LINE_TYPE;
    private String IS_END_POLE;
    private String LINE_CONFIGURATION;
    private String IS_STREETLIGHT_AV;
    private String IS_SERVICES_OTHERTHAN_SL;
    // private String AGL_SERVICES;
    private String NO_AGL_SERVICES;
    //  private String AQUA_SERVICES;
    private String NO_AQUA_SERVICES;
    //  private String INDUSTRIAL_SERVICES;
    private String NO_INDUSTRIAL_SERVICES;
    private String PIN_CUT;
    private String IS_DOUBLE_FEEDING;
    private String POLE_TYPE;
    private String POLE_HEIGHT;
    private String STRUT_STAY;
    private String TAPPING_POINT;
    private String NO_OF_SPURLINES;
    private String TAPPING_LOCATION;
    private String CONDUCTOR_SIZE;
    private String IS_MULTI_CIRCUIT;
    private String NO_OF_CIRCUITS;
    private String MULTI_VOLTAGE;
    //private String _33KV_VOLTAGE_CKTS;
    private String NO_OF33KV_CKTS;
    //private String _11KV_VOLTAGE_CKTS;
    private String NO_OF11KV_CKTS;
    //private String _6_3KV_VOLTAGE_CKTS;
    private String NO_OF6_3KV_VOLTAGE_CKTS;
    // private String LT_VOLTAGE_CKTS;
    private String NO_OF_LT_CKTS;
    private String POLE_PAINTING_CODE;
    private String REMARKS;
    private String LAT;
    private String LAN;
    private String SURVEY_FLAG;
    private String FEEDER_ID;
    private String LOCATION_TYPE;
    private String SERVICE_PILLERTYPE;
    private String FEEDERPILLET_OUTGN_CKT;
    private String CABLE_SIZE;
    private String NO_CABLES;
    private String DTR_METER_AVILABLE;
    private String DTR_NO_OF_METERS;

    private String IS_CROSSINGS_AV;
    private String NOOF_6_3KV_CROSSINGS;
    private String NOOF_11KV_CROSSINGS;
    private String NOOF_33KV_CROSSINGS;
    private String NOOF_LT_CROSSINGS;
    private String NOOF_TC_CROSSINGS;
    private String NOOF_TM_CROSSINGS;

    //NEW
    private   String DEFFECTS_AVAILABLE;
    private   String POLE_DEFFECTS;
    private   String CONDUCTOR_DEFFECTS;
    private   String INSULATOR_DEFFECTS;
    private   String CROSSARM_DEFFECTS;
    private   String STRUT_DEFFECTS;
    private   String STRAY_DEFFECTS;
    private   String HTLT_CLEARANCE;
    private   String INTERMEDIATE_POLE;
    private   String RESTRINGING;
    private   String OTHER_DEFECTS;
    private   String NUM_SCS;

    //IMAGES
    private String IMAGE_1;
    private String IMAGE_2;
    private String IMAGE_3;

    //MULTI CKT
    private String MC_33KV_FEEDER_ID_1;
    private String MC_33KV_FEEDER_ID_2;
    private String MC_33KV_FEEDER_ID_3;
    private String MC_33KV_POLE_1;
    private String MC_33KV_POLE_2;
    private String MC_33KV_POLE_3;


    private String MC_11KV_FEEDER_ID_1;
    private String MC_11KV_FEEDER_ID_2;
    private String MC_11KV_FEEDER_ID_3;
    private String MC_11KV_POLE_1;
    private String MC_11KV_POLE_2;
    private String MC_11KV_POLE_3;


    private String MC_LT_FEEDER_ID_1;
    private String MC_LT_FEEDER_ID_2;
    private String MC_LT_FEEDER_ID_3;
    private String MC_LT_POLE_1;
    private String MC_LT_POLE_2;
    private String MC_LT_POLE_3;

    private String MC_33KV_NOT_IN_LIST_1="N";
    private String MC_33KV_NOT_IN_LIST_2="N";
    private String MC_33KV_NOT_IN_LIST_3="N";
    private String MC_11KV_NOT_IN_LIST_1="N";
    private String MC_11KV_NOT_IN_LIST_2="N";
    private String MC_11KV_NOT_IN_LIST_3="N";
    private String MC_LT_NOT_IN_LIST_1="N";
    private String MC_LT_NOT_IN_LIST_2="N";
    private String MC_LT_NOT_IN_LIST_3="N";



    //OTHER CABLES
    private String NUM_OTHER_CABLES;

    //JHC PHASE 1
    private String POLE_SURVEY_TYPE;
    private String LINE_SURVEY_TYPE;

    //OTHER CABLES
    private String POLE_CONCRETE;

    //CURRENT LOCATION
    private String CURRENT_LAT;
    private String CURRENT_LONG;

    public String getMC_33KV_NOT_IN_LIST_1() {
        return MC_33KV_NOT_IN_LIST_1;
    }

    public void setMC_33KV_NOT_IN_LIST_1(String MC_33KV_NOT_IN_LIST_1) {
        this.MC_33KV_NOT_IN_LIST_1 = MC_33KV_NOT_IN_LIST_1;
    }

    public String getMC_33KV_NOT_IN_LIST_2() {
        return MC_33KV_NOT_IN_LIST_2;
    }

    public void setMC_33KV_NOT_IN_LIST_2(String MC_33KV_NOT_IN_LIST_2) {
        this.MC_33KV_NOT_IN_LIST_2 = MC_33KV_NOT_IN_LIST_2;
    }

    public String getMC_33KV_NOT_IN_LIST_3() {
        return MC_33KV_NOT_IN_LIST_3;
    }

    public void setMC_33KV_NOT_IN_LIST_3(String MC_33KV_NOT_IN_LIST_3) {
        this.MC_33KV_NOT_IN_LIST_3 = MC_33KV_NOT_IN_LIST_3;
    }

    public String getMC_11KV_NOT_IN_LIST_1() {
        return MC_11KV_NOT_IN_LIST_1;
    }

    public void setMC_11KV_NOT_IN_LIST_1(String MC_11KV_NOT_IN_LIST_1) {
        this.MC_11KV_NOT_IN_LIST_1 = MC_11KV_NOT_IN_LIST_1;
    }

    public String getMC_11KV_NOT_IN_LIST_2() {
        return MC_11KV_NOT_IN_LIST_2;
    }

    public void setMC_11KV_NOT_IN_LIST_2(String MC_11KV_NOT_IN_LIST_2) {
        this.MC_11KV_NOT_IN_LIST_2 = MC_11KV_NOT_IN_LIST_2;
    }

    public String getMC_11KV_NOT_IN_LIST_3() {
        return MC_11KV_NOT_IN_LIST_3;
    }

    public void setMC_11KV_NOT_IN_LIST_3(String MC_11KV_NOT_IN_LIST_3) {
        this.MC_11KV_NOT_IN_LIST_3 = MC_11KV_NOT_IN_LIST_3;
    }

    public String getMC_LT_NOT_IN_LIST_1() {
        return MC_LT_NOT_IN_LIST_1;
    }

    public void setMC_LT_NOT_IN_LIST_1(String MC_LT_NOT_IN_LIST_1) {
        this.MC_LT_NOT_IN_LIST_1 = MC_LT_NOT_IN_LIST_1;
    }

    public String getMC_LT_NOT_IN_LIST_2() {
        return MC_LT_NOT_IN_LIST_2;
    }

    public void setMC_LT_NOT_IN_LIST_2(String MC_LT_NOT_IN_LIST_2) {
        this.MC_LT_NOT_IN_LIST_2 = MC_LT_NOT_IN_LIST_2;
    }

    public String getMC_LT_NOT_IN_LIST_3() {
        return MC_LT_NOT_IN_LIST_3;
    }

    public void setMC_LT_NOT_IN_LIST_3(String MC_LT_NOT_IN_LIST_3) {
        this.MC_LT_NOT_IN_LIST_3 = MC_LT_NOT_IN_LIST_3;
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

    public String getMC_LT_FEEDER_ID_1() {
        return MC_LT_FEEDER_ID_1;
    }

    public void setMC_LT_FEEDER_ID_1(String MC_LT_FEEDER_ID_1) {
        this.MC_LT_FEEDER_ID_1 = MC_LT_FEEDER_ID_1;
    }

    public String getMC_LT_FEEDER_ID_2() {
        return MC_LT_FEEDER_ID_2;
    }

    public void setMC_LT_FEEDER_ID_2(String MC_LT_FEEDER_ID_2) {
        this.MC_LT_FEEDER_ID_2 = MC_LT_FEEDER_ID_2;
    }

    public String getMC_LT_FEEDER_ID_3() {
        return MC_LT_FEEDER_ID_3;
    }

    public void setMC_LT_FEEDER_ID_3(String MC_LT_FEEDER_ID_3) {
        this.MC_LT_FEEDER_ID_3 = MC_LT_FEEDER_ID_3;
    }

    public String getMC_LT_POLE_1() {
        return MC_LT_POLE_1;
    }

    public void setMC_LT_POLE_1(String MC_LT_POLE_1) {
        this.MC_LT_POLE_1 = MC_LT_POLE_1;
    }

    public String getMC_LT_POLE_2() {
        return MC_LT_POLE_2;
    }

    public void setMC_LT_POLE_2(String MC_LT_POLE_2) {
        this.MC_LT_POLE_2 = MC_LT_POLE_2;
    }

    public String getMC_LT_POLE_3() {
        return MC_LT_POLE_3;
    }

    public void setMC_LT_POLE_3(String MC_LT_POLE_3) {
        this.MC_LT_POLE_3 = MC_LT_POLE_3;
    }

    public String getMC_33KV_FEEDER_ID_1() {
        return MC_33KV_FEEDER_ID_1;
    }

    public void setMC_33KV_FEEDER_ID_1(String MC_33KV_FEEDER_ID_1) {
        this.MC_33KV_FEEDER_ID_1 = MC_33KV_FEEDER_ID_1;
    }

    public String getMC_33KV_FEEDER_ID_2() {
        return MC_33KV_FEEDER_ID_2;
    }

    public void setMC_33KV_FEEDER_ID_2(String MC_33KV_FEEDER_ID_2) {
        this.MC_33KV_FEEDER_ID_2 = MC_33KV_FEEDER_ID_2;
    }

    public String getMC_33KV_FEEDER_ID_3() {
        return MC_33KV_FEEDER_ID_3;
    }

    public void setMC_33KV_FEEDER_ID_3(String MC_33KV_FEEDER_ID_3) {
        this.MC_33KV_FEEDER_ID_3 = MC_33KV_FEEDER_ID_3;
    }

    public String getMC_33KV_POLE_1() {
        return MC_33KV_POLE_1;
    }

    public void setMC_33KV_POLE_1(String MC_33KV_POLE_1) {
        this.MC_33KV_POLE_1 = MC_33KV_POLE_1;
    }

    public String getMC_33KV_POLE_2() {
        return MC_33KV_POLE_2;
    }

    public void setMC_33KV_POLE_2(String MC_33KV_POLE_2) {
        this.MC_33KV_POLE_2 = MC_33KV_POLE_2;
    }

    public String getMC_33KV_POLE_3() {
        return MC_33KV_POLE_3;
    }

    public void setMC_33KV_POLE_3(String MC_33KV_POLE_3) {
        this.MC_33KV_POLE_3 = MC_33KV_POLE_3;
    }

    public String getMC_11KV_FEEDER_ID_1() {
        return MC_11KV_FEEDER_ID_1;
    }

    public void setMC_11KV_FEEDER_ID_1(String MC_11KV_FEEDER_ID_1) {
        this.MC_11KV_FEEDER_ID_1 = MC_11KV_FEEDER_ID_1;
    }

    public String getMC_11KV_FEEDER_ID_2() {
        return MC_11KV_FEEDER_ID_2;
    }

    public void setMC_11KV_FEEDER_ID_2(String MC_11KV_FEEDER_ID_2) {
        this.MC_11KV_FEEDER_ID_2 = MC_11KV_FEEDER_ID_2;
    }

    public String getMC_11KV_FEEDER_ID_3() {
        return MC_11KV_FEEDER_ID_3;
    }

    public void setMC_11KV_FEEDER_ID_3(String MC_11KV_FEEDER_ID_3) {
        this.MC_11KV_FEEDER_ID_3 = MC_11KV_FEEDER_ID_3;
    }

    public String getMC_11KV_POLE_1() {
        return MC_11KV_POLE_1;
    }

    public void setMC_11KV_POLE_1(String MC_11KV_POLE_1) {
        this.MC_11KV_POLE_1 = MC_11KV_POLE_1;
    }

    public String getMC_11KV_POLE_2() {
        return MC_11KV_POLE_2;
    }

    public void setMC_11KV_POLE_2(String MC_11KV_POLE_2) {
        this.MC_11KV_POLE_2 = MC_11KV_POLE_2;
    }

    public String getMC_11KV_POLE_3() {
        return MC_11KV_POLE_3;
    }

    public void setMC_11KV_POLE_3(String MC_11KV_POLE_3) {
        this.MC_11KV_POLE_3 = MC_11KV_POLE_3;
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

    public String getOTHER_DEFECTS() {
        return OTHER_DEFECTS;
    }

    public void setOTHER_DEFECTS(String OTHER_DEFECTS) {
        this.OTHER_DEFECTS = OTHER_DEFECTS;
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

    public String getNOOF_6_3KV_CROSSINGS() {
        return NOOF_6_3KV_CROSSINGS;
    }

    public void setNOOF_6_3KV_CROSSINGS(String NOOF_6_3KV_CROSSINGS) {
        this.NOOF_6_3KV_CROSSINGS = NOOF_6_3KV_CROSSINGS;
    }

    public String getNOOF_11KV_CROSSINGS() {
        return NOOF_11KV_CROSSINGS;
    }

    public void setNOOF_11KV_CROSSINGS(String NOOF_11KV_CROSSINGS) {
        this.NOOF_11KV_CROSSINGS = NOOF_11KV_CROSSINGS;
    }

    public String getNOOF_33KV_CROSSINGS() {
        return NOOF_33KV_CROSSINGS;
    }

    public void setNOOF_33KV_CROSSINGS(String NOOF_33KV_CROSSINGS) {
        this.NOOF_33KV_CROSSINGS = NOOF_33KV_CROSSINGS;
    }

    public String getNOOF_LT_CROSSINGS() {
        return NOOF_LT_CROSSINGS;
    }

    public void setNOOF_LT_CROSSINGS(String NOOF_LT_CROSSINGS) {
        this.NOOF_LT_CROSSINGS = NOOF_LT_CROSSINGS;
    }

    public String getNOOF_TC_CROSSINGS() {
        return NOOF_TC_CROSSINGS;
    }

    public void setNOOF_TC_CROSSINGS(String NOOF_TC_CROSSINGS) {
        this.NOOF_TC_CROSSINGS = NOOF_TC_CROSSINGS;
    }

    public String getNOOF_TM_CROSSINGS() {
        return NOOF_TM_CROSSINGS;
    }

    public void setNOOF_TM_CROSSINGS(String NOOF_TM_CROSSINGS) {
        this.NOOF_TM_CROSSINGS = NOOF_TM_CROSSINGS;
    }

    public String getDTR_METER_AVILABLE() {
        return DTR_METER_AVILABLE;
    }

    public void setDTR_METER_AVILABLE(String DTR_METER_AVILABLE) {
        this.DTR_METER_AVILABLE = DTR_METER_AVILABLE;
    }

    public String getDTR_NO_OF_METERS() {
        return DTR_NO_OF_METERS;
    }

    public void setDTR_NO_OF_METERS(String DTR_NO_OF_METERS) {
        this.DTR_NO_OF_METERS = DTR_NO_OF_METERS;
    }

    public String getLOCATION_TYPE() {
        return LOCATION_TYPE;
    }

    public void setLOCATION_TYPE(String LOCATION_TYPE) {
        this.LOCATION_TYPE = LOCATION_TYPE;
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

    public String getFEEDER_ID() {
        return FEEDER_ID;
    }

    public void setFEEDER_ID(String FEEDER_ID) {
        this.FEEDER_ID = FEEDER_ID;
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

    public String getSURVEY_FLAG() {
        return SURVEY_FLAG;
    }

    public void setSURVEY_FLAG(String SURVEY_FLAG) {
        this.SURVEY_FLAG = SURVEY_FLAG;
    }

    public String getDTR_STRUCTURE() {
        return DTR_STRUCTURE;
    }

    public void setDTR_STRUCTURE(String DTR_STRUCTURE) {
        this.DTR_STRUCTURE = DTR_STRUCTURE;
    }

    public String getLT_CIRCUIT() {
        return LT_CIRCUIT;
    }

    public void setLT_CIRCUIT(String LT_CIRCUIT) {
        this.LT_CIRCUIT = LT_CIRCUIT;
    }



    public String getLINE_TYPE() {
        return LINE_TYPE;
    }

    public void setLINE_TYPE(String LINE_TYPE) {
        this.LINE_TYPE = LINE_TYPE;
    }

    public String getIS_END_POLE() {
        return IS_END_POLE;
    }

    public void setIS_END_POLE(String IS_END_POLE) {
        this.IS_END_POLE = IS_END_POLE;
    }

    public String getLINE_CONFIGURATION() {
        return LINE_CONFIGURATION;
    }

    public void setLINE_CONFIGURATION(String LINE_CONFIGURATION) {
        this.LINE_CONFIGURATION = LINE_CONFIGURATION;
    }

    public String getIS_STREETLIGHT_AV() {
        return IS_STREETLIGHT_AV;
    }

    public void setIS_STREETLIGHT_AV(String IS_STREETLIGHT_AV) {
        this.IS_STREETLIGHT_AV = IS_STREETLIGHT_AV;
    }

    public String getIS_SERVICES_OTHERTHAN_SL() {
        return IS_SERVICES_OTHERTHAN_SL;
    }

    public void setIS_SERVICES_OTHERTHAN_SL(String IS_SERVICES_OTHERTHAN_SL) {
        this.IS_SERVICES_OTHERTHAN_SL = IS_SERVICES_OTHERTHAN_SL;
    }

    public String getNO_AGL_SERVICES() {
        return NO_AGL_SERVICES;
    }

    public void setNO_AGL_SERVICES(String NO_AGL_SERVICES) {
        this.NO_AGL_SERVICES = NO_AGL_SERVICES;
    }

    public String getNO_AQUA_SERVICES() {
        return NO_AQUA_SERVICES;
    }

    public void setNO_AQUA_SERVICES(String NO_AQUA_SERVICES) {
        this.NO_AQUA_SERVICES = NO_AQUA_SERVICES;
    }

    public String getNO_INDUSTRIAL_SERVICES() {
        return NO_INDUSTRIAL_SERVICES;
    }

    public void setNO_INDUSTRIAL_SERVICES(String NO_INDUSTRIAL_SERVICES) {
        this.NO_INDUSTRIAL_SERVICES = NO_INDUSTRIAL_SERVICES;
    }

    public String getPIN_CUT() {
        return PIN_CUT;
    }

    public void setPIN_CUT(String PIN_CUT) {
        this.PIN_CUT = PIN_CUT;
    }

    public String getIS_DOUBLE_FEEDING() {
        return IS_DOUBLE_FEEDING;
    }

    public void setIS_DOUBLE_FEEDING(String IS_DOUBLE_FEEDING) {
        this.IS_DOUBLE_FEEDING = IS_DOUBLE_FEEDING;
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

    public String getSTRUT_STAY() {
        return STRUT_STAY;
    }

    public void setSTRUT_STAY(String STRUT_STAY) {
        this.STRUT_STAY = STRUT_STAY;
    }

    public String getTAPPING_POINT() {
        return TAPPING_POINT;
    }

    public void setTAPPING_POINT(String TAPPING_POINT) {
        this.TAPPING_POINT = TAPPING_POINT;
    }

    public String getNO_OF_SPURLINES() {
        return NO_OF_SPURLINES;
    }

    public void setNO_OF_SPURLINES(String NO_OF_SPURLINES) {
        this.NO_OF_SPURLINES = NO_OF_SPURLINES;
    }

    public String getTAPPING_LOCATION() {
        return TAPPING_LOCATION;
    }

    public void setTAPPING_LOCATION(String TAPPING_LOCATION) {
        this.TAPPING_LOCATION = TAPPING_LOCATION;
    }

    public String getCONDUCTOR_SIZE() {
        return CONDUCTOR_SIZE;
    }

    public void setCONDUCTOR_SIZE(String CONDUCTOR_SIZE) {
        this.CONDUCTOR_SIZE = CONDUCTOR_SIZE;
    }

    public String getIS_MULTI_CIRCUIT() {
        return IS_MULTI_CIRCUIT;
    }

    public void setIS_MULTI_CIRCUIT(String IS_MULTI_CIRCUIT) {
        this.IS_MULTI_CIRCUIT = IS_MULTI_CIRCUIT;
    }

    public String getNO_OF_CIRCUITS() {
        return NO_OF_CIRCUITS;
    }

    public void setNO_OF_CIRCUITS(String NO_OF_CIRCUITS) {
        this.NO_OF_CIRCUITS = NO_OF_CIRCUITS;
    }

    public String getMULTI_VOLTAGE() {
        return MULTI_VOLTAGE;
    }

    public void setMULTI_VOLTAGE(String MULTI_VOLTAGE) {
        this.MULTI_VOLTAGE = MULTI_VOLTAGE;
    }

    public String getNO_OF33KV_CKTS() {
        return NO_OF33KV_CKTS;
    }

    public void setNO_OF33KV_CKTS(String NO_OF33KV_CKTS) {
        this.NO_OF33KV_CKTS = NO_OF33KV_CKTS;
    }

    public String getNO_OF11KV_CKTS() {
        return NO_OF11KV_CKTS;
    }

    public void setNO_OF11KV_CKTS(String NO_OF11KV_CKTS) {
        this.NO_OF11KV_CKTS = NO_OF11KV_CKTS;
    }

    public String getNO_OF6_3KV_VOLTAGE_CKTS() {
        return NO_OF6_3KV_VOLTAGE_CKTS;
    }

    public void setNO_OF6_3KV_VOLTAGE_CKTS(String NO_OF6_3KV_VOLTAGE_CKTS) {
        this.NO_OF6_3KV_VOLTAGE_CKTS = NO_OF6_3KV_VOLTAGE_CKTS;
    }

    public String getNO_OF_LT_CKTS() {
        return NO_OF_LT_CKTS;
    }

    public void setNO_OF_LT_CKTS(String NO_OF_LT_CKTS) {
        this.NO_OF_LT_CKTS = NO_OF_LT_CKTS;
    }

    public String getPOLE_PAINTING_CODE() {
        return POLE_PAINTING_CODE;
    }

    public void setPOLE_PAINTING_CODE(String POLE_PAINTING_CODE) {
        this.POLE_PAINTING_CODE = POLE_PAINTING_CODE;
    }

    public String getREMARKS() {
        return REMARKS;
    }

    public void setREMARKS(String REMARKS) {
        this.REMARKS = REMARKS;
    }

    public String getLAT() {
        return LAT;
    }

    public void setLAT(String LAT) {
        this.LAT = LAT;
    }

    public String getLAN() {
        return LAN;
    }

    public void setLAN(String LAN) {
        this.LAN = LAN;
    }
}
