package com.apepdcl.gis_survey.module_11KV.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SurveyVo  implements Serializable {
    private String feeder_id;
    private int pole_no;
    private int from_pole;
    private String pole_type;
    private String pole_height;
    private String pole_support;
    private String tapping_point;
    private String conductor_size;
    private String pole_configuration;
    private String pinpoint;
    private String pole_paintingcode;
    private String lat;
    private String lan;
    private String pastSurveyFlag;
    private String isMultiCircuit;
    private String circuitType;
    private String isEndPole;
    private String lineType;
    private String noofCircuits;
    // private String cableSize;\
    private String ss_name;
    private String poleStructure;
    private String diffVoltage;
    private String ssId;
    private String stjoint_rmu;
    private String rmu_type;
    private String remarks;
    private String noofTappings;
    private String tappingLocation;

    private String dtr_code;
    private String dtr_phase;
    private String dtr_scno;
    private String dtr_capacity;
    private String dtr_make;
    private String dtr_structure;
    private String dtr_location_name;
    private String no_lt_ckt;
    private String dtr_ab_switch;
    private String voltage_level;
    private String dtr_no_of_circuit;
    private String ht_service_number;
    private String pc_line_capacitor;
    private String pc_auto_reclosure;

    private String type_of_load;
    private String dtr_location_type;
    private String is_ltckt_avalable;
    private String no_of_ab_switches;
    private String woId;
    private String isFeederType;

    //NEW
    private String DEFFECTS_AVAILABLE;
    private String POLE_DEFECTS;
    private String CONDUCTOR_DEFECTS;
    private String AB_SWITCH_DEFECTS;
    private String INSULATOR_DEFECTS;
    private String CROSSARM_DEFECTS;
    private String TOPCLEAT_DEFECTS;
    private String STRUT_DEFECTS;
    private String STRAY_DEFECTS;
    private String HTLT_CLEARANCE;
    private String INTERMEDIATE_POLE;
    private String RESTRINGING;
    private String TREE_CLEARANCE;
    private String OTHER_DEFFECTS;
    private String CROSSING_TYPE;

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

    private String MC_33KV_NOT_IN_LIST_1 = "N";
    private String MC_33KV_NOT_IN_LIST_2 = "N";
    private String MC_33KV_NOT_IN_LIST_3 = "N";
    private String MC_11KV_NOT_IN_LIST_1 = "N";
    private String MC_11KV_NOT_IN_LIST_2 = "N";
    private String MC_11KV_NOT_IN_LIST_3 = "N";
    private String MC_LT_NOT_IN_LIST_1 = "N";
    private String MC_LT_NOT_IN_LIST_2 = "N";
    private String MC_LT_NOT_IN_LIST_3 = "N";

    private String NO_OF33KV_CKTS;
    private String NO_OF11KV_CKTS;
    private String NO_OF_LT_CKTS;

    //OTHER CABLES
    private String NUM_OTHER_CABLES;
    // , , , ,,,

    //CROSSINGS AND REMARKS
    private String CROSS_LT_CKT;
    private String CROSS_6_3KV_ID;
    private String CROSS_11KV_ID;
    private String CROSS_33KV_ID;
    private String CROSS_EHT;
    private String CROSS_COMMUN;
    private String CROSS_REMARKS;

    //JHC PHASE 1
    private String POLE_SURVEY_TYPE;
    private String LINE_SURVEY_TYPE;
    private String DTR_SURVEY_TYPE;
    private String LT_SURVEY_TYPE;

    //IS DTR AVAILABLE
    private String DTR_AVAILABLE;

    @SerializedName("DTR_REMARKS")
    private String DTR_REMARKS;


    //POLE_CONCRETE
    private String POLE_CONCRETE;

    //CURRENT LOCATION
    private String CURRENT_LAT;
    private String CURRENT_LONG;

    //RMU
    private String RMU_MAKE;
    private String RMU_SL_NO;
    private String ID;
    private String GPS_ACCURACY;
    private String DEVICE_ID;

    public String getDEVICE_ID() {
        return DEVICE_ID;
    }

    public void setDEVICE_ID(String DEVICE_ID) {
        this.DEVICE_ID = DEVICE_ID;
    }

    ///MODIFICATION CODE STARTS

    private String PACKAGE_NO,DT_VOLATAGE_RATIO,DT_MANF_DT,DT_INSTL_DT,RDSS_SCHEME_TYPE, CIRCUIT_TYPE,ADDRESS_1,ADDRESS_2;
    String  town, block, village, state;

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public String getPACKAGE_NO() {
        return PACKAGE_NO;
    }

    public void setPACKAGE_NO(String PACKAGE_NO) {
        this.PACKAGE_NO = PACKAGE_NO;
    }

    public String getDT_VOLATAGE_RATIO() {
        return DT_VOLATAGE_RATIO;
    }

    public void setDT_VOLATAGE_RATIO(String DT_VOLATAGE_RATIO) {
        this.DT_VOLATAGE_RATIO = DT_VOLATAGE_RATIO;
    }

    public String getDT_MANF_DT() {
        return DT_MANF_DT;
    }

    public void setDT_MANF_DT(String DT_MANF_DT) {
        this.DT_MANF_DT = DT_MANF_DT;
    }

    public String getDT_INSTL_DT() {
        return DT_INSTL_DT;
    }

    public void setDT_INSTL_DT(String DT_INSTL_DT) {
        this.DT_INSTL_DT = DT_INSTL_DT;
    }

    public String getRDSS_SCHEME_TYPE() {
        return RDSS_SCHEME_TYPE;
    }

    public void setRDSS_SCHEME_TYPE(String RDSS_SCHEME_TYPE) {
        this.RDSS_SCHEME_TYPE = RDSS_SCHEME_TYPE;
    }

    public String getCIRCUIT_TYPE() {
        return CIRCUIT_TYPE;
    }

    public void setCIRCUIT_TYPE(String CIRCUIT_TYPE) {
        this.CIRCUIT_TYPE = CIRCUIT_TYPE;
    }

    ///MODIFICATION CODE ENDS

    public String getGPS_ACCURACY() {
        return GPS_ACCURACY;
    }

    public void setGPS_ACCURACY(String GPS_ACCURACY) {
        this.GPS_ACCURACY = GPS_ACCURACY;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getRMU_MAKE() {
        return RMU_MAKE;
    }

    public void setRMU_MAKE(String RMU_MAKE) {
        this.RMU_MAKE = RMU_MAKE;
    }

    public String getRMU_SL_NO() {
        return RMU_SL_NO;
    }

    public void setRMU_SL_NO(String RMU_SL_NO) {
        this.RMU_SL_NO = RMU_SL_NO;
    }

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

    public String getDTR_AVAILABLE() {
        return DTR_AVAILABLE;
    }

    public void setDTR_AVAILABLE(String DTR_AVAILABLE) {
        this.DTR_AVAILABLE = DTR_AVAILABLE;
    }

    public String getDTR_REMARKS() {
        return DTR_REMARKS;
    }

    public void setDTR_REMARKS(String DTR_REMARKS) {
        this.DTR_REMARKS = DTR_REMARKS;
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

    public String getDTR_SURVEY_TYPE() {
        return DTR_SURVEY_TYPE;
    }

    public void setDTR_SURVEY_TYPE(String DTR_SURVEY_TYPE) {
        this.DTR_SURVEY_TYPE = DTR_SURVEY_TYPE;
    }

    public String getLT_SURVEY_TYPE() {
        return LT_SURVEY_TYPE;
    }

    public void setLT_SURVEY_TYPE(String LT_SURVEY_TYPE) {
        this.LT_SURVEY_TYPE = LT_SURVEY_TYPE;
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

    public String getNUM_OTHER_CABLES() {
        return NUM_OTHER_CABLES;
    }

    public void setNUM_OTHER_CABLES(String NUM_OTHER_CABLES) {
        this.NUM_OTHER_CABLES = NUM_OTHER_CABLES;
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

    public String getNO_OF_LT_CKTS() {
        return NO_OF_LT_CKTS;
    }

    public void setNO_OF_LT_CKTS(String NO_OF_LT_CKTS) {
        this.NO_OF_LT_CKTS = NO_OF_LT_CKTS;
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

    public String getCROSSING_TYPE() {
        return CROSSING_TYPE;
    }

    public void setCROSSING_TYPE(String CROSSING_TYPE) {
        this.CROSSING_TYPE = CROSSING_TYPE;
    }

    public String getSurvaytype() {
        return survaytype;
    }

    public void setSurvaytype(String survaytype) {
        this.survaytype = survaytype;
    }

    private String survaytype;

    public String getIsFeederType() {
        return isFeederType;
    }

    public void setIsFeederType(String isFeederType) {
        this.isFeederType = isFeederType;
    }

    public String getWoId() {
        return woId;
    }

    public void setWoId(String woId) {
        this.woId = woId;
    }

    public String getNo_of_ab_switches() {
        return no_of_ab_switches;
    }

    public void setNo_of_ab_switches(String no_of_ab_switches) {
        this.no_of_ab_switches = no_of_ab_switches;
    }

    public String getType_of_load() {
        return type_of_load;
    }

    public void setType_of_load(String type_of_load) {
        this.type_of_load = type_of_load;
    }

    public String getDtr_location_type() {
        return dtr_location_type;
    }

    public void setDtr_location_type(String dtr_location_type) {
        this.dtr_location_type = dtr_location_type;
    }

    public String getIs_ltckt_avalable() {
        return is_ltckt_avalable;
    }

    public void setIs_ltckt_avalable(String is_ltckt_avalable) {
        this.is_ltckt_avalable = is_ltckt_avalable;
    }

    public String getPc_line_capacitor() {
        return pc_line_capacitor;
    }

    public void setPc_line_capacitor(String pc_line_capacitor) {
        this.pc_line_capacitor = pc_line_capacitor;
    }

    public String getPc_auto_reclosure() {
        return pc_auto_reclosure;
    }

    public void setPc_auto_reclosure(String pc_auto_reclosure) {
        this.pc_auto_reclosure = pc_auto_reclosure;
    }

    public String getHt_service_number() {
        return ht_service_number;
    }

    public void setHt_service_number(String ht_service_number) {
        this.ht_service_number = ht_service_number;
    }

    public String getDtr_no_of_circuit() {
        return dtr_no_of_circuit;
    }

    public void setDtr_no_of_circuit(String dtr_no_of_circuit) {
        this.dtr_no_of_circuit = dtr_no_of_circuit;
    }

    public String getDtr_location_name() {
        return dtr_location_name;
    }

    public void setDtr_location_name(String dtr_location_name) {
        this.dtr_location_name = dtr_location_name;
    }

    public String getVoltage_level() {
        return voltage_level;
    }

    public void setVoltage_level(String voltage_level) {
        this.voltage_level = voltage_level;
    }

    public String getDtr_code() {
        return dtr_code;
    }

    public void setDtr_code(String dtr_code) {
        this.dtr_code = dtr_code;
    }

    public String getDtr_phase() {
        return dtr_phase;
    }

    public void setDtr_phase(String dtr_phase) {
        this.dtr_phase = dtr_phase;
    }

    public String getDtr_scno() {
        return dtr_scno;
    }

    public void setDtr_scno(String dtr_scno) {
        this.dtr_scno = dtr_scno;
    }

    public String getDtr_capacity() {
        return dtr_capacity;
    }

    public void setDtr_capacity(String dtr_capacity) {
        this.dtr_capacity = dtr_capacity;
    }

    public String getDtr_make() {
        return dtr_make;
    }

    public void setDtr_make(String dtr_make) {
        this.dtr_make = dtr_make;
    }

    public String getDtr_structure() {
        return dtr_structure;
    }

    public void setDtr_structure(String dtr_structure) {
        this.dtr_structure = dtr_structure;
    }

    public String getNo_lt_ckt() {
        return no_lt_ckt;
    }

    public void setNo_lt_ckt(String no_lt_ckt) {
        this.no_lt_ckt = no_lt_ckt;
    }

    public String getDtr_ab_switch() {
        return dtr_ab_switch;
    }

    public void setDtr_ab_switch(String dtr_ab_switch) {
        this.dtr_ab_switch = dtr_ab_switch;
    }

    public String getNoofTappings() {
        return noofTappings;
    }

    public void setNoofTappings(String noofTappings) {
        this.noofTappings = noofTappings;
    }

    public String getTappingLocation() {
        return tappingLocation;
    }

    public void setTappingLocation(String tappingLocation) {
        this.tappingLocation = tappingLocation;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStjoint_rmu() {
        return stjoint_rmu;
    }

    public void setStjoint_rmu(String stjoint_rmu) {
        this.stjoint_rmu = stjoint_rmu;
    }

    public String getRmu_type() {
        return rmu_type;
    }

    public void setRmu_type(String rmu_type) {
        this.rmu_type = rmu_type;
    }

    public String getSsId() {
        return ssId;
    }

    public void setSsId(String ssId) {
        this.ssId = ssId;
    }

    public String getDiffVoltage() {
        return diffVoltage;
    }

    public void setDiffVoltage(String diffVoltage) {
        this.diffVoltage = diffVoltage;
    }

    public String getPoleStructure() {
        return poleStructure;
    }

    public void setPoleStructure(String poleStructure) {
        this.poleStructure = poleStructure;
    }

    public String getSs_name() {
        return ss_name;
    }

    public void setSs_name(String ss_name) {
        this.ss_name = ss_name;
    }

    public String getLineType() {
        return lineType;
    }

    public void setLineType(String lineType) {
        this.lineType = lineType;
    }

    public String getNoofCircuits() {
        return noofCircuits;
    }

    public void setNoofCircuits(String noofCircuits) {
        this.noofCircuits = noofCircuits;
    }

    /* public String getCableSize() {
         return cableSize;
     }

     public void setCableSize(String cableSize) {
         this.cableSize = cableSize;
     }
 */
    public String getIsEndPole() {
        return isEndPole;
    }

    public void setIsEndPole(String isEndPole) {
        this.isEndPole = isEndPole;
    }

    public String getIsMultiCircuit() {
        return isMultiCircuit;
    }

    public void setIsMultiCircuit(String isMultiCircuit) {
        this.isMultiCircuit = isMultiCircuit;
    }

    public String getCircuitType() {
        return circuitType;
    }

    public void setCircuitType(String circuitType) {
        this.circuitType = circuitType;
    }

    public String getPastSurveyFlag() {
        return pastSurveyFlag;
    }

    public void setPastSurveyFlag(String pastSurveyFlag) {
        this.pastSurveyFlag = pastSurveyFlag;
    }

    public String getPinpoint() {
        return pinpoint;
    }

    public void setPinpoint(String pinpoint) {
        this.pinpoint = pinpoint;
    }

    public String getFeeder_id() {
        return feeder_id;
    }

    public void setFeeder_id(String feeder_id) {
        this.feeder_id = feeder_id;
    }

    public int getPole_no() {
        return pole_no;
    }

    public void setPole_no(int pole_no) {
        this.pole_no = pole_no;
    }

    public int getFrom_pole() {
        return from_pole;
    }

    public void setFrom_pole(int from_pole) {
        this.from_pole = from_pole;
    }

    public String getPole_type() {
        return pole_type;
    }

    public void setPole_type(String pole_type) {
        this.pole_type = pole_type;
    }

    public String getPole_height() {
        return pole_height;
    }

    public void setPole_height(String pole_height) {
        this.pole_height = pole_height;
    }


    public String getPole_support() {
        return pole_support;
    }

    public void setPole_support(String pole_support) {
        this.pole_support = pole_support;
    }

    public String getTapping_point() {
        return tapping_point;
    }

    public void setTapping_point(String tapping_point) {
        this.tapping_point = tapping_point;
    }

    public String getConductor_size() {
        return conductor_size;
    }

    public void setConductor_size(String conductor_size) {
        this.conductor_size = conductor_size;
    }

    public String getPole_configuration() {
        return pole_configuration;
    }

    public void setPole_configuration(String pole_configuration) {
        this.pole_configuration = pole_configuration;
    }

    public String getPole_paintingcode() {
        return pole_paintingcode;
    }

    public void setPole_paintingcode(String pole_paintingcode) {
        this.pole_paintingcode = pole_paintingcode;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLan() {
        return lan;
    }

    public void setLan(String lan) {
        this.lan = lan;
    }


}
