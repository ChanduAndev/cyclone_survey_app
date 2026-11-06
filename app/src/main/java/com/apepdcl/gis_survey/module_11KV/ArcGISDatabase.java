package com.apepdcl.gis_survey.module_11KV;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

import com.apepdcl.gis_survey.module_11KV.models.DTRMaster_vo;
import com.apepdcl.gis_survey.module_11KV.models.DistandeVo;
import com.apepdcl.gis_survey.module_11KV.models.FeederDataVo;
import com.apepdcl.gis_survey.module_11KV.models.FeederServiceMasterResponseVo;
import com.apepdcl.gis_survey.module_11KV.models.IncrementalFeederDataVo;
import com.apepdcl.gis_survey.module_11KV.models.IncrementalSurveyVo;
import com.apepdcl.gis_survey.module_11KV.models.LoginDataResponse;
import com.apepdcl.gis_survey.module_11KV.models.MULTIVOLTAGEMaster_vo;
import com.apepdcl.gis_survey.module_11KV.models.MakeMaster_vo;
import com.apepdcl.gis_survey.module_11KV.models.SSMasterVo;
import com.apepdcl.gis_survey.module_11KV.models.SurveyVo;
import com.apepdcl.gis_survey.module_11KV.models.WoStatusMasterVo;
import com.apepdcl.gis_survey.vo.SurveyDataVo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ArcGISDatabase extends SQLiteOpenHelper {

    // Database Version
    // private static final int DATABASE_VERSION = 1;
    //private static final int DATABASE_VERSION = 2; //CHANGED ON 05/07/24

    private static final int DATABASE_VERSION = 10; //Modified 10-01-2025

    // Database Name
    //   private static final String DATABASE_NAME = "GIS_11KV_SURVEYDB";

    private static final String DATABASE_NAME = "JHC11KV_SURVEYDB";

    //Tables Name
    private static final String ARCGIS_TABLE = "ARCGIS_TABLE";
    private static final String ARCGIS_TABLE_LAT_LANG_EDIT = "ARCGIS_TABLE_LAT_LANG_EDIT";
    private static final String ARCGIS_TABLE_SOURCE_POLE_EDIT = "ARCGIS_TABLE_SOURCE_POLE_EDIT";
    private static final String ARCGIS_TABLE_POLE_DETAILS_EDIT = "ARCGIS_TABLE_POLE_DETAILS_EDIT";

    private static final String ARCGIS_TABLE_REPEAT = "ARCGIS_TABLE_REPEAT";

    private static final String FEEDERMASTER_TABLE = "FEEDERMASTER_TABLE";
    private static final String EHTMASTER_TABLE = "EHTMASTER_TABLE";
    private static final String USERMASTER_TABLE = "USERMASTER_TABLE";
    private static final String SSMASTER_TABLE = "SSMASTER_TABLE";
    private static final String DTR_MASTER_TABLE = "DTR_MASTER_TABLE";
    private static final String MAKE_MASTER_TABLE = "MAKE_MASTER_TABLE";
    private static final String FEEDER_SERVICE_MASTER_TABLE = "FEEDER_SERVICE_MASTER_TABLE";
    private static final String MULTIVOLTAGE_MASTERS_TABLE = "MULTIVOLTAGE_MASTERS_TABLE";

    private static final String CMP_EHTMASTER_TABLE = "CMP_EHTMASTER_TABLE";

    private static final String INCREMENTAL_EHTMASTER_TABLE = "INCREMENTAL_EHTMASTER_TABLE";
    private static final String INCREMENTAL_ARCGIS_TABLE = "INCREMENTAL_ARCGIS_TABLE";
    private static final String INCREMENTALSSMASTER_TABLE = "INCREMENTALSSMASTER_TABLE";
    private static final String INCREMENTAL_DTR_MASTER_TABLE = "INCREMENTAL_DTR_MASTER_TABLE";
    private static final String INCREMENTAL_FEEDER_SERVICE_MASTER_TABLE = "INCREMENTAL_FEEDER_SERVICE_MASTER_TABLE";
    private static final String INCREMENTAL_MAKE_MASTER_TABLE = "INCREMENTAL_MAKE_MASTER_TABLE";
    private static final String INCREMENTAL_MULTIVOLTAGE_MASTERS_TABLE = "INCREMENTAL_MULTIVOLTAGE_MASTERS_TABLE";
    private static final String WO_STATUS_TABLE = "WO_STATUS_TABLE";


    private static final String FEEDER_ID = "FEEDER_ID";
    private static final String POLE_NO = "POLE_NO";
    private static final String FROM_POLE = "FROM_POLE";
    private static final String POLE_TYPE = "POLE_TYPE";
    private static final String POLE_HEIGHT = "POLE_HEIGHT";
    private static final String POLE_SUPPORT = "POLE_SUPPORT";
    private static final String TAPPING_POINT = "TAPPING_POINT";
    private static final String CONDUCTOR_SIZE = "CONDUCTOR_SIZE";
    private static final String POLE_CONFIGURATION = "POLE_CONFIGURATION";
    private static final String PINPOINT = "PINPOINT";
    private static final String POLE_PAINTINGCODE = "POLE_PAINTINGCODE";
    private static final String NO_CKT = "NO_CKT";
    private static final String CKT_TYPE = "CKT_TYPE";
    private static final String LAT = "LAT";
    private static final String LAN = "LAN";
    private static final String FLAG1 = "FLAG1";
    private static final String FLAG2 = "FLAG2";
    private static final String FLAG3 = "FLAG3";
    private static final String FLAG4 = "FLAG4";
    //FeederMaster
    private static final String SECTION_ID = "SECTION_ID";
    private static final String EHT_SS_CODE = "EHT_SS_CODE";
    private static final String ALLOW_SURVEY = "ALLOW_SURVEY";
    private static final String EHT_SS_NAME = "EHT_SS_NAME";
    private static final String VOLTAGE_LEVEL = "VOLTAGE_LEVEL";
    private static final String FEEDER_NAME_33KV = "FEEDER_NAME_33KV";
    private static final String FEEDER_CODE_33KV = "FEEDER_CODE_33KV";
    private static final String ROLE = "ROLE";
    private static final String OFFICE_ID = "OFFICE_ID";
    private static final String SECTION_NAME = "SECTION_NAME";
    private static final String SEC_CODE = "SEC_CODE";
    private static final String IS_ENDPOLE = "IS_ENDPOLE";
    private static final String LINE_TYPE = "LINE_TYPE";
    private static final String NOOF_CIRCUITS = "NOOF_CIRCUITS";
    private static final String POLE_STRUCTURE = "POLE_STRUCTURE";
    private static final String SS_NAME = "SS_NAME";
    private static final String DIFF_VOLTAGE = "DIFF_VOLTAGE";
    private static final String SS_ID = "SS_ID";
    private static final String STJOINT_RMU = "STJOINT_RMU";
    private static final String RMU_TYPE = "RMU_TYPE";
    private static final String REMARKS = "REMARKS";
    private static final String TAPPING_LOCNAME = "TAPPING_LOCNAME";
    private static final String TAPPING_POINTS = "TAPPING_POINTS";
    private static final String DTR_CODE1 = "DTR_CODE1";
    private static final String DTR_PHASE1 = "DTR_PHASE1";
    private static final String DTR_SNO1 = "DTR_SNO1";
    private static final String DTR_CAPACITY1 = "DTR_CAPACITY1";
    private static final String DTR_MAKE1 = "DTR_MAKE1";
    private static final String DTR_STRUCTURE1 = "DTR_STRUCTURE1";
    private static final String NO_LI_CKT1 = "NO_LI_CKT1";
    private static final String DTR_AB1 = "DTR_AB1";
    private static final String DTR_LOCATION_NAME = "DTR_LOCATION_NAME";
    private static final String DTR_NO_OF_LT_CIRCUIT = "DTR_NO_OF_LT_CIRCUIT";
    private static final String HT_SERVICE_NO = "HT_SERVICE_NO";
    private static final String PC_LINE_CAPACITOR = "PC_LINE_CAPACITOR";
    private static final String PC_AUTO_RECLOSURE = "PC_AUTO_RECLOSURE";

    private static final String TYPE_OF_LOAD = "TYPE_OF_LOAD";
    private static final String DTR_LOCATION_TYPE = "DTR_LOCATION_TYPE";
    private static final String IS_LTCKT_AVAILABLE = "IS_LTCKT_AVAILABLE";
    private static final String NO_OF_AB_SWITCHES = "NO_OF_AB_SWITCHES";


    //DTR Master
    private static final String DTR_FEEDER_ID = "DTR_FEEDER_ID";
    private static final String DTR_OFFICE_ID = "DTR_OFFICE_ID";
    private static final String DTR_SECTION_NAME = "DTR_SECTION_NAME";
    private static final String DTR_PHASE = "DTR_PHASE";
    private static final String DTR_CAPACITY = "DTR_CAPACITY";
    private static final String DTR_MAKE = "DTR_MAKE";
    private static final String DTR_STRUCTURE_CODE = "DTR_STRUCTURE_CODE";
    private static final String DTR_FEEDER_NAME = "DTR_FEEDER_NAME";
    private static final String DTR_SECTION_ID = "DTR_SECTION_ID";
    private static final String DTR_LOCATION = "DTR_LOCATION";
    private static final String DTR_MAKER = "DTR_MAKER";
    private static final String DTR_SLNO = "DTR_SLNO";
    private static final String WORK_ORDER = "WORK_ORDER";
    private static final String EST_NO = "EST_NO";

    //Make Master
    private static final String ID = "ID";
    private static final String GPS_ACCURACY = "GPS_ACCURACY";
    private static final String DEVICE_ID = "DEVICE_ID";
    private static final String DTR_MAKE_M = "DTR_MAKE_M";
    private static final String DTR_RECORD_STATUS = "DTR_RECORD_STATUS";


    //Feeder Service Master
    private static final String FS_FEEDER_ID = "FS_FEEDER_ID";
    private static final String FS_CONSUMER_NUMBER = "FS_CONSUMER_NUMBER";
    private static final String FS_CONSUMER_NAME = "FS_CONSUMER_NAME";

    //Multivoltage
    private static final String DTR_ID = "DTR_ID";
    private static final String VOLTAGE = "VOLTAGE";
    private static final String CKT_NO = "CKT_NO";
    private static final String CKT_VALUE = "CKT_VALUE";
    private static final String RECORD_STATUS = "RECORD_STATUS";

    //INCREMENTAL
    private static final String WORKORDER_ID = "WORKORDER_ID";
    private static final String STATUS = "STATUS";
    private static final String WO_STATUS = "WO_STATUS";

    private static final String WO_ID = "WO_ID";
    private static final String SURVEY_STATUS = "SURVEY_STATUS";
    private static final String IS_FEEDER_TYPE = "IS_FEEDER_TYPE";
    private static final String SURVEY_TYPE = "SURVEY_TYPE";

    //DEFECTS AND CROSSING
    private static final String DEFFECTS_AVAILABLE = "DEFFECTS_AVAILABLE";
    private static final String POLE_DEFECTS = "POLE_DEFECTS";
    private static final String CONDUCTOR_DEFECTS = "CONDUCTOR_DEFECTS";
    private static final String AB_SWITCH_DEFECTS = "AB_SWITCH_DEFECTS";
    private static final String INSULATOR_DEFECTS = "INSULATOR_DEFECTS";
    private static final String CROSSARM_DEFECTS = "CROSSARM_DEFECTS";
    private static final String TOPCLEAT_DEFECTS = "TOPCLEAT_DEFECTS";
    private static final String STRUT_DEFECTS = "STRUT_DEFECTS";
    private static final String STRAY_DEFECTS = "STRAY_DEFECTS";
    private static final String HTLT_CLEARANCE = "HTLT_CLEARANCE";
    private static final String INTERMEDIATE_POLE = "INTERMEDIATE_POLE";
    private static final String RESTRINGING = "RESTRINGING";
    private static final String TREE_CLEARANCE = "TREE_CLEARANCE";
    private static final String OTHER_DEFFECTS = "OTHER_DEFFECTS";
    private static final String CROSSING_TYPE = "CROSSING_TYPE";

    //MULTI CKT
    private static final String MC_33KV_FEEDER_ID_1 = "MC_33KV_FEEDER_ID_1";
    private static final String MC_33KV_FEEDER_ID_2 = "MC_33KV_FEEDER_ID_2";
    private static final String MC_33KV_FEEDER_ID_3 = "MC_33KV_FEEDER_ID_3";
    private static final String MC_33KV_POLE_1 = "MC_33KV_POLE_1";
    private static final String MC_33KV_POLE_2 = "MC_33KV_POLE_2";
    private static final String MC_33KV_POLE_3 = "MC_33KV_POLE_3";
    private static final String MC_11KV_FEEDER_ID_1 = "MC_11KV_FEEDER_ID_1";
    private static final String MC_11KV_FEEDER_ID_2 = "MC_11KV_FEEDER_ID_2";
    private static final String MC_11KV_FEEDER_ID_3 = "MC_11KV_FEEDER_ID_3";
    private static final String MC_11KV_POLE_1 = "MC_11KV_POLE_1";
    private static final String MC_11KV_POLE_2 = "MC_11KV_POLE_2";
    private static final String MC_11KV_POLE_3 = "MC_11KV_POLE_3";
    private static final String MC_LT_FEEDER_ID_1 = "MC_LT_FEEDER_ID_1";
    private static final String MC_LT_FEEDER_ID_2 = "MC_LT_FEEDER_ID_2";
    private static final String MC_LT_FEEDER_ID_3 = "MC_LT_FEEDER_ID_3";
    private static final String MC_LT_POLE_1 = "MC_LT_POLE_1";
    private static final String MC_LT_POLE_2 = "MC_LT_POLE_2";
    private static final String MC_LT_POLE_3 = "MC_LT_POLE_3";

    private static final String NO_33KV = "NO_33KV";
    private static final String NO_11KV = "NO_11KV";
    private static final String NO_LT = "NO_LT";

    private static final String NUM_OTHER_CABLES = "NUM_OTHER_CABLES";

    //CROSSING NEW
    private static final String CROSS_LT_CKT = "CROSS_LT_CKT";
    private static final String CROSS_6_3KV_ID = "CROSS_6_3KV_ID";
    private static final String CROSS_11KV_ID = "CROSS_11KV_ID";
    private static final String CROSS_33KV_ID = "CROSS_33KV_ID";
    private static final String CROSS_EHT = "CROSS_EHT";
    private static final String CROSS_COMMUN = "CROSS_COMMUN";
    private static final String CROSS_REMARKS = "CROSS_REMARKS";

    //JHC PHASE 1
    private static final String POLE_SURVEY_TYPE = "POLE_SURVEY_TYPE";
    private static final String LINE_SURVEY_TYPE = "LINE_SURVEY_TYPE";
    private static final String DTR_SURVEY_TYPE = "DTR_SURVEY_TYPE";
    private static final String LT_SURVEY_TYPE = "LT_SURVEY_TYPE";

    //DTR AVAILABLE
    private static final String DTR_AVAILABLE = "DTR_AVAILABLE";
    private static final String DTR_REMARKS = "DTR_REMARKS";


    //DTR AVAILABLE
    private static final String POLE_CONCRETE = "POLE_CONCRETE";

    //CURRENT LOCATION
    private static final String CURRENT_LAT = "CURRENT_LAT";
    private static final String CURRENT_LONG = "CURRENT_LONG";

    //MULTI CKT NOT IN LIST
    private static final String MC_33KV_NOT_IN_LIST_1 = "MC_33KV_NOT_IN_LIST_1";
    private static final String MC_33KV_NOT_IN_LIST_2 = "MC_33KV_NOT_IN_LIST_2";
    private static final String MC_33KV_NOT_IN_LIST_3 = "MC_33KV_NOT_IN_LIST_3";
    private static final String MC_11KV_NOT_IN_LIST_1 = "MC_11KV_NOT_IN_LIST_1";
    private static final String MC_11KV_NOT_IN_LIST_2 = "MC_11KV_NOT_IN_LIST_2";
    private static final String MC_11KV_NOT_IN_LIST_3 = "MC_11KV_NOT_IN_LIST_3";
    private static final String MC_LT_NOT_IN_LIST_1 = "MC_LT_NOT_IN_LIST_1";
    private static final String MC_LT_NOT_IN_LIST_2 = "MC_LT_NOT_IN_LIST_2";
    private static final String MC_LT_NOT_IN_LIST_3 = "MC_LT_NOT_IN_LIST_3";

    //RMU
    private static final String RMU_MAKE = "RMU_MAKE";
    private static final String RMU_SL_NO = "RMU_SL_NO";


    //IMAGES
    private static final String IMAGE_11KV_TABLE = "IMAGE_11KV_TABLE";
    private static final String IMAGE_1 = "IMAGE_1";
    private static final String IMAGE_2 = "IMAGE_2";
    private static final String IMAGE_3 = "IMAGE_3";

    ///MODIFICATIONS STARTS
    private String PACKAGE_NO="PACKAGE_NO",DT_VOLATAGE_RATIO="DT_VOLATAGE_RATIO",DT_MANF_DT="DT_MANF_DT",DT_INSTL_DT="DT_INSTL_DT",RDSS_SCHEME_TYPE="RDSS_SCHEME_TYPE", CIRCUIT_TYPE="CIRCUIT_TYPE", ADDRESS_1 ="ADDRESS_1", ADDRESS_2 ="ADDRESS_2";
    private String TOWN = "TOWN", VILLAGE="VILLAGE", BLOCK="BLOCK", STATE="STATE";
    ///MODIFICATIONS ENDS


    String CREATE_IMAGE_11KV_TABLE = "CREATE TABLE " + IMAGE_11KV_TABLE + "(" + FEEDER_ID + " TEXT ," + POLE_NO + " INTEGER," + IMAGE_1 + " TEXT ," + IMAGE_2 + " TEXT ," + IMAGE_3 + " TEXT " + ")";


    String CREATE_MULTIVOLTAGE_MASTERS_TABLE = "CREATE TABLE " + MULTIVOLTAGE_MASTERS_TABLE + "("
            + DTR_ID + " TEXT ,"
            + VOLTAGE + " TEXT ,"
            + CKT_NO + " TEXT ,"
            + CKT_VALUE + " TEXT ,"
            + RECORD_STATUS + " TEXT " + ")";


    String CREATE_INCREMENTAL_MULTIVOLTAGE_MASTERS_TABLE = "CREATE TABLE " + INCREMENTAL_MULTIVOLTAGE_MASTERS_TABLE + "("
            + DTR_ID + " TEXT ,"
            + VOLTAGE + " TEXT ,"
            + CKT_NO + " TEXT ,"
            + CKT_VALUE + " TEXT ,"
            + RECORD_STATUS + " TEXT " + ")";


    String CREATE_FEEDER_SERVICE_MASTERS_TABLE = "CREATE TABLE " + FEEDER_SERVICE_MASTER_TABLE + "("
            + FS_FEEDER_ID + " TEXT ,"
            + FS_CONSUMER_NUMBER + " TEXT ,"
            + FS_CONSUMER_NAME + " TEXT " + ")";

    String CREATE_INCREMENTAL_FEEDER_SERVICE_MASTERS_TABLE = "CREATE TABLE " + INCREMENTAL_FEEDER_SERVICE_MASTER_TABLE + "("
            + FS_FEEDER_ID + " TEXT ,"
            + FS_CONSUMER_NUMBER + " TEXT ,"
            + FS_CONSUMER_NAME + " TEXT " + ")";


    String CREATE_MAKE_MASTERS_TABLE = "CREATE TABLE " + MAKE_MASTER_TABLE + "("
            + DTR_ID + " TEXT ,"
            + DTR_MAKE_M + " TEXT ,"
            + DTR_RECORD_STATUS + " TEXT " + ")";


    String CREATE_INCREMENTAL_MAKE_MASTERS_TABLE = "CREATE TABLE " + INCREMENTAL_MAKE_MASTER_TABLE + "("
            + DTR_ID + " TEXT ,"
            + DTR_MAKE_M + " TEXT ,"
            + DTR_RECORD_STATUS + " TEXT " + ")";


    String CREATE_DTR_MASTERS_TABLE = "CREATE TABLE " + DTR_MASTER_TABLE + "("
            + DTR_FEEDER_ID + " TEXT ,"
            + DTR_OFFICE_ID + " TEXT ,"
            + DTR_SECTION_NAME + " TEXT ,"
            + DTR_PHASE + " TEXT ,"
            + DTR_CAPACITY + " TEXT ,"
            + DTR_MAKE + " TEXT ,"
            + DTR_STRUCTURE_CODE + " TEXT ,"
            + DTR_FEEDER_NAME + " TEXT ,"
            + DTR_SECTION_ID + " TEXT ,"
            + DTR_LOCATION + " TEXT ,"
            + DTR_MAKER + " TEXT ,"
            + DTR_SLNO + " TEXT ,"
            + WORK_ORDER + " TEXT ,"
            + EST_NO + " TEXT "
            +
            ")";


    String CREATE_INCREMENTAL_DTR_MASTERS_TABLE = "CREATE TABLE " + INCREMENTAL_DTR_MASTER_TABLE + "("
            + DTR_FEEDER_ID + " TEXT ,"
            + DTR_OFFICE_ID + " TEXT ,"
            + DTR_SECTION_NAME + " TEXT ,"
            + DTR_PHASE + " TEXT ,"
            + DTR_CAPACITY + " TEXT ,"
            + DTR_MAKE + " TEXT ,"
            + DTR_STRUCTURE_CODE + " TEXT ,"
            + DTR_FEEDER_NAME + " TEXT ,"
            + DTR_SECTION_ID + " TEXT ,"
            + DTR_LOCATION + " TEXT ,"
            + DTR_MAKER + " TEXT ,"
            + DTR_SLNO + " TEXT "
            + ")";


    String CREATE_SSMASTER_TABLE = "CREATE TABLE " + SSMASTER_TABLE + "("
            + EHT_SS_CODE + " TEXT ,"
            + EHT_SS_NAME + " TEXT ,"
            + FEEDER_CODE_33KV + " TEXT ,"
            + FLAG1 + " TEXT ,"
            + FLAG2 + " TEXT ,"
            + FLAG3 + " TEXT " + ")";


    String CREATE_INCREMENTALSSMASTER_TABLE = "CREATE TABLE " + INCREMENTALSSMASTER_TABLE + "("
            + EHT_SS_CODE + " TEXT ,"
            + EHT_SS_NAME + " TEXT ,"
            + FEEDER_CODE_33KV + " TEXT ,"
            + FLAG1 + " TEXT ,"
            + FLAG2 + " TEXT ,"
            + FLAG3 + " TEXT " + ")";

    String CREATE_USERMASTER_TABLE = "CREATE TABLE " + USERMASTER_TABLE + "("
            + ROLE + " TEXT ," + OFFICE_ID + " TEXT ," + SECTION_NAME + " TEXT ," + SEC_CODE + " TEXT ," + FLAG1 + " TEXT ," + FLAG2 + " TEXT ," + FLAG3 + " TEXT " + ")";


    String CREATE_EHTMASTER_TABLE = "CREATE TABLE " + EHTMASTER_TABLE + "("
            + EHT_SS_CODE + " TEXT, "
            + EHT_SS_NAME + " TEXT, "
            + FEEDER_NAME_33KV + " TEXT, "
            + FEEDER_CODE_33KV + " TEXT, "
            + WO_ID + " TEXT, "
            + IS_FEEDER_TYPE + " TEXT, "
            + ALLOW_SURVEY + " TEXT, "
            + FLAG3 + " TEXT"
            + ")";


//    String CREATE_EHTMASTER_TABLE = "CREATE TABLE " + EHTMASTER_TABLE + "("
//            + EHT_SS_CODE + " TEXT ,"
//            + EHT_SS_NAME + " TEXT ,"
//            + FEEDER_NAME_33KV + " TEXT ,"
//            + FEEDER_CODE_33KV + " TEXT ,"
//            + WO_ID + " TEXT ,"
//            + IS_FEEDER_TYPE + " TEXT ,"
//            + FLAG3 + " TEXT " + ")";


    String CREATE_ARCGIS_TABLE_LAT_LANG_EDIT = "CREATE TABLE " + ARCGIS_TABLE_LAT_LANG_EDIT + " (" +
            POLE_NO + " INTEGER, " +
            FEEDER_ID + " TEXT, " +
            LAT + " TEXT, " +
            LAN + " TEXT, " +
            ID + " TEXT, " +
            "UNIQUE (" + POLE_NO + ", " + FEEDER_ID + ") ON CONFLICT REPLACE" +
            ");";

    String CREATE_ARCGIS_TABLE_SOURCE_POLE_EDIT = "CREATE TABLE "
            + ARCGIS_TABLE_SOURCE_POLE_EDIT + " ("
            + ID + " TEXT, "
            + FEEDER_ID + " TEXT, "
            + FROM_POLE + " TEXT"
            + ");";


    String CREATE_ARCGIS_TABLE_POLE_DETAILS_EDIT = "CREATE TABLE "
            + ARCGIS_TABLE_POLE_DETAILS_EDIT + " ("
            + POLE_TYPE + " TEXT, "
            + POLE_HEIGHT + " TEXT, "
            + POLE_SUPPORT + " TEXT, "
            + POLE_STRUCTURE + " TEXT, "
            + PINPOINT + " TEXT, "
            + POLE_CONCRETE + " TEXT, "
            + POLE_PAINTINGCODE + " TEXT, "
            + ID + " TEXT, "
            + FEEDER_ID + " TEXT"
            + ");";


    String CREATE_CMP_EHTMASTER_TABLE = "CREATE TABLE " + CMP_EHTMASTER_TABLE + "("
            + EHT_SS_CODE + " TEXT ," + EHT_SS_NAME + " TEXT ," + FEEDER_NAME_33KV + " TEXT ," + FEEDER_CODE_33KV + " TEXT ," + FLAG1 + " TEXT ," + FLAG2 + " TEXT ," + FLAG3 + " TEXT " + ")";


    String CREATE_INCREMENTAL_EHTMASTER_TABLE = "CREATE TABLE " + INCREMENTAL_EHTMASTER_TABLE + "("
            + EHT_SS_CODE + " TEXT ," + EHT_SS_NAME + " TEXT ," + FEEDER_NAME_33KV + " TEXT ," + FEEDER_CODE_33KV + " TEXT ," + WORKORDER_ID + " TEXT ," + STATUS + " TEXT ," + FROM_POLE + " TEXT ," + IS_FEEDER_TYPE + " TEXT ," + FLAG3 + " TEXT " + ")";


    /* String CREATE_FEEDERMASTER_TABLE = "CREATE TABLE " + FEEDERMASTER_TABLE + "("
             + SECTION_ID + " TEXT ," + EHT_SS_CODE + " TEXT ," + EHT_SS_NAME + " TEXT ," + VOLTAGE_LEVEL + " TEXT," + FEEDER_NAME_33KV + " TEXT ," + FEEDER_CODE_33KV + " TEXT ," + FLAG1 + " TEXT ," + FLAG2 + " TEXT ," + FLAG3 + " TEXT " + ")";*/
    String CREATE_ARCGIS_TABLE = "CREATE TABLE " + ARCGIS_TABLE + "("
            + FEEDER_ID + " TEXT ,"
            + POLE_NO + " INTEGER UNIQUE,"
            + FROM_POLE + " INTEGER ,"
            + POLE_TYPE + " TEXT,"
            + POLE_HEIGHT + " TEXT ,"
            + POLE_SUPPORT + " TEXT ,"
            + TAPPING_POINT + " TEXT ,"
            + CONDUCTOR_SIZE + " TEXT ,"
            + POLE_CONFIGURATION + " TEXT ,"
            + PINPOINT + " TEXT ,"
            + POLE_PAINTINGCODE + " TEXT ,"
            + LAT + " TEXT ,"
            + LAN + " TEXT ,"
            + FLAG1 + " TEXT ,"
            + NO_CKT + " TEXT ,"
            + CKT_TYPE + " TEXT ,"
            + IS_ENDPOLE + " TEXT ,"
            + LINE_TYPE + " TEXT ,"
            + NOOF_CIRCUITS + " TEXT ,"
            + SS_NAME + " TEXT ,"
            + POLE_STRUCTURE + " TEXT ,"
            + DIFF_VOLTAGE + " TEXT ,"
            + SS_ID + " TEXT ,"
            + STJOINT_RMU + " TEXT ,"
            + RMU_TYPE + " TEXT ,"
            + REMARKS + " TEXT ,"
            + TAPPING_LOCNAME + " TEXT ,"
            + TAPPING_POINTS + " TEXT ,"
            + DTR_CODE1 + " TEXT ,"
            + DTR_PHASE1 + " TEXT ,"
            + DTR_SNO1 + " TEXT ,"
            + DTR_CAPACITY1 + " TEXT ,"
            + DTR_MAKE1 + " TEXT ,"
            + DTR_STRUCTURE1 + " TEXT ,"
            + NO_LI_CKT1 + " TEXT ,"
            + DTR_AB1 + " TEXT ,"
            + VOLTAGE_LEVEL + " TEXT ,"
            + DTR_LOCATION_NAME + " TEXT ,"
            + DTR_NO_OF_LT_CIRCUIT + " TEXT ,"
            + HT_SERVICE_NO + " TEXT ,"
            + PC_LINE_CAPACITOR + " TEXT ,"
            + PC_AUTO_RECLOSURE + " TEXT ,"
            + TYPE_OF_LOAD + " TEXT ,"
            + DTR_LOCATION_TYPE + " TEXT ,"
            + IS_LTCKT_AVAILABLE + " TEXT ,"
            + NO_OF_AB_SWITCHES + " TEXT ,"
            + WO_ID + " TEXT ,"
            + IS_FEEDER_TYPE + " TEXT ,"
            + SURVEY_TYPE + " TEXT ,"

            + DEFFECTS_AVAILABLE + " TEXT ,"
            + POLE_DEFECTS + " TEXT ,"
            + CONDUCTOR_DEFECTS + " TEXT ,"
            + AB_SWITCH_DEFECTS + " TEXT ,"
            + INSULATOR_DEFECTS + " TEXT ,"
            + CROSSARM_DEFECTS + " TEXT ,"
            + TOPCLEAT_DEFECTS + " TEXT ,"
            + STRUT_DEFECTS + " TEXT ,"
            + STRAY_DEFECTS + " TEXT ,"
            + HTLT_CLEARANCE + " TEXT ,"
            + INTERMEDIATE_POLE + " TEXT ,"
            + RESTRINGING + " TEXT ,"
            + TREE_CLEARANCE + " TEXT ,"
            + OTHER_DEFFECTS + " TEXT ,"
            + CROSSING_TYPE + " TEXT ,"

            + MC_33KV_FEEDER_ID_1 + " TEXT ,"
            + MC_33KV_FEEDER_ID_2 + " TEXT ,"
            + MC_33KV_FEEDER_ID_3 + " TEXT ,"
            + MC_33KV_POLE_1 + " TEXT ,"
            + MC_33KV_POLE_2 + " TEXT ,"
            + MC_33KV_POLE_3 + " TEXT ,"

            + MC_11KV_FEEDER_ID_1 + " TEXT ,"
            + MC_11KV_FEEDER_ID_2 + " TEXT ,"
            + MC_11KV_FEEDER_ID_3 + " TEXT ,"
            + MC_11KV_POLE_1 + " TEXT ,"
            + MC_11KV_POLE_2 + " TEXT ,"
            + MC_11KV_POLE_3 + " TEXT ,"

            + MC_LT_FEEDER_ID_1 + " TEXT ,"
            + MC_LT_FEEDER_ID_2 + " TEXT ,"
            + MC_LT_FEEDER_ID_3 + " TEXT ,"
            + MC_LT_POLE_1 + " TEXT ,"
            + MC_LT_POLE_2 + " TEXT ,"
            + MC_LT_POLE_3 + " TEXT ,"

            + NO_33KV + " TEXT ,"
            + NO_11KV + " TEXT ,"
            + NO_LT + " TEXT ,"
            + NUM_OTHER_CABLES + " TEXT ,"

            + CROSS_LT_CKT + " TEXT ,"
            + CROSS_6_3KV_ID + " TEXT ,"
            + CROSS_11KV_ID + " TEXT ,"
            + CROSS_33KV_ID + " TEXT ,"
            + CROSS_EHT + " TEXT ,"
            + CROSS_COMMUN + " TEXT ,"
            + CROSS_REMARKS + " TEXT ,"

            + POLE_SURVEY_TYPE + " TEXT ,"
            + LINE_SURVEY_TYPE + " TEXT ,"
            + DTR_SURVEY_TYPE + " TEXT ,"
            + LT_SURVEY_TYPE + " TEXT ,"

            + DTR_AVAILABLE + " TEXT ,"
            + POLE_CONCRETE + " TEXT ,"

            + CURRENT_LAT + " TEXT ,"
            + CURRENT_LONG + " TEXT ,"

            + MC_33KV_NOT_IN_LIST_1 + " TEXT ,"
            + MC_33KV_NOT_IN_LIST_2 + " TEXT ,"
            + MC_33KV_NOT_IN_LIST_3 + " TEXT ,"
            + MC_11KV_NOT_IN_LIST_1 + " TEXT ,"
            + MC_11KV_NOT_IN_LIST_2 + " TEXT ,"
            + MC_11KV_NOT_IN_LIST_3 + " TEXT ,"
            + MC_LT_NOT_IN_LIST_1 + " TEXT ,"
            + MC_LT_NOT_IN_LIST_2 + " TEXT ,"
            + MC_LT_NOT_IN_LIST_3 + " TEXT ,"
            + RMU_MAKE + " TEXT ,"
            + RMU_SL_NO + " TEXT ,"
            + DTR_REMARKS + " TEXT ,"
            + ID + " TEXT ,"
            + GPS_ACCURACY + " TEXT ,"
            + DEVICE_ID + " TEXT ,"

            + PACKAGE_NO + " TEXT ,"
            + DT_VOLATAGE_RATIO + " TEXT ,"
            + DT_MANF_DT + " TEXT ,"
            + DT_INSTL_DT + " TEXT ,"
            + RDSS_SCHEME_TYPE + " TEXT ,"
            + CIRCUIT_TYPE + " TEXT ,"
            + ADDRESS_1 + " TEXT ,"
            + ADDRESS_2 + " TEXT ,"
            + BLOCK + " TEXT ,"
            + VILLAGE + " TEXT ,"
            + TOWN + " TEXT ,"
            + STATE + " TEXT ,"


            + FLAG4 + " TEXT " + ")";

    String CREATE_TABLE_REPEAT = "CREATE TABLE " + ARCGIS_TABLE_REPEAT + "("
            + FEEDER_ID + " TEXT ,"
            + POLE_NO + " INTEGER UNIQUE,"
            + FROM_POLE + " INTEGER ,"
            + POLE_TYPE + " TEXT,"
            + POLE_HEIGHT + " TEXT ,"
            + POLE_SUPPORT + " TEXT ,"
            + TAPPING_POINT + " TEXT ,"
            + CONDUCTOR_SIZE + " TEXT ,"
            + POLE_CONFIGURATION + " TEXT ,"
            + PINPOINT + " TEXT ,"
            + POLE_PAINTINGCODE + " TEXT ,"
            + LAT + " TEXT ,"
            + LAN + " TEXT ,"
            + FLAG1 + " TEXT ,"
            + NO_CKT + " TEXT ,"
            + CKT_TYPE + " TEXT ,"
            + IS_ENDPOLE + " TEXT ,"
            + LINE_TYPE + " TEXT ,"
            + NOOF_CIRCUITS + " TEXT ,"
            + SS_NAME + " TEXT ,"
            + POLE_STRUCTURE + " TEXT ,"
            + DIFF_VOLTAGE + " TEXT ,"
            + SS_ID + " TEXT ,"
            + STJOINT_RMU + " TEXT ,"
            + RMU_TYPE + " TEXT ,"
            + REMARKS + " TEXT ,"
            + TAPPING_LOCNAME + " TEXT ,"
            + TAPPING_POINTS + " TEXT ,"
            + DTR_CODE1 + " TEXT ,"
            + DTR_PHASE1 + " TEXT ,"
            + DTR_SNO1 + " TEXT ,"
            + DTR_CAPACITY1 + " TEXT ,"
            + DTR_MAKE1 + " TEXT ,"
            + DTR_STRUCTURE1 + " TEXT ,"
            + NO_LI_CKT1 + " TEXT ,"
            + DTR_AB1 + " TEXT ,"
            + VOLTAGE_LEVEL + " TEXT ,"
            + DTR_LOCATION_NAME + " TEXT ,"
            + DTR_NO_OF_LT_CIRCUIT + " TEXT ,"
            + HT_SERVICE_NO + " TEXT ,"
            + PC_LINE_CAPACITOR + " TEXT ,"
            + PC_AUTO_RECLOSURE + " TEXT ,"
            + TYPE_OF_LOAD + " TEXT ,"
            + DTR_LOCATION_TYPE + " TEXT ,"
            + IS_LTCKT_AVAILABLE + " TEXT ,"
            + NO_OF_AB_SWITCHES + " TEXT ,"
            + WO_ID + " TEXT ,"
            + IS_FEEDER_TYPE + " TEXT ,"
            + SURVEY_TYPE + " TEXT ,"

            + DEFFECTS_AVAILABLE + " TEXT ,"
            + POLE_DEFECTS + " TEXT ,"
            + CONDUCTOR_DEFECTS + " TEXT ,"
            + AB_SWITCH_DEFECTS + " TEXT ,"
            + INSULATOR_DEFECTS + " TEXT ,"
            + CROSSARM_DEFECTS + " TEXT ,"
            + TOPCLEAT_DEFECTS + " TEXT ,"
            + STRUT_DEFECTS + " TEXT ,"
            + STRAY_DEFECTS + " TEXT ,"
            + HTLT_CLEARANCE + " TEXT ,"
            + INTERMEDIATE_POLE + " TEXT ,"
            + RESTRINGING + " TEXT ,"
            + TREE_CLEARANCE + " TEXT ,"
            + OTHER_DEFFECTS + " TEXT ,"
            + CROSSING_TYPE + " TEXT ,"

            + POLE_SURVEY_TYPE + " TEXT ,"
            + LINE_SURVEY_TYPE + " TEXT ,"
            + DTR_SURVEY_TYPE + " TEXT ,"
            + LT_SURVEY_TYPE + " TEXT ,"

            + POLE_CONCRETE + " TEXT ,"

            + FLAG4 + " TEXT " + ")";


    String CREATE_INCREMENTAL_ARCGIS_TABLE = "CREATE TABLE IF NOT EXISTS " + INCREMENTAL_ARCGIS_TABLE + "("
            + FEEDER_ID + " TEXT ,"
            + POLE_NO + " INTEGER UNIQUE,"
            + FROM_POLE + " INTEGER ,"
            + POLE_TYPE + " TEXT,"
            + POLE_HEIGHT + " TEXT ,"
            + POLE_SUPPORT + " TEXT ,"
            + TAPPING_POINT + " TEXT ,"
            + CONDUCTOR_SIZE + " TEXT ,"
            + POLE_CONFIGURATION + " TEXT ,"
            + PINPOINT + " TEXT ,"
            + POLE_PAINTINGCODE + " TEXT ,"
            + LAT + " TEXT ,"
            + LAN + " TEXT ,"
            + FLAG1 + " TEXT ,"
            + NO_CKT + " TEXT ,"
            + CKT_TYPE + " TEXT ,"
            + IS_ENDPOLE + " TEXT ,"
            + LINE_TYPE + " TEXT ,"
            + NOOF_CIRCUITS + " TEXT ,"
            + SS_NAME + " TEXT ,"
            + POLE_STRUCTURE + " TEXT ,"
            + DIFF_VOLTAGE + " TEXT ,"
            + SS_ID + " TEXT ,"
            + STJOINT_RMU + " TEXT ,"
            + RMU_TYPE + " TEXT ,"
            + REMARKS + " TEXT ,"
            + TAPPING_LOCNAME + " TEXT ,"
            + TAPPING_POINTS + " TEXT ,"
            + DTR_CODE1 + " TEXT ,"
            + DTR_PHASE1 + " TEXT ,"
            + DTR_SNO1 + " TEXT ,"
            + DTR_CAPACITY1 + " TEXT ,"
            + DTR_MAKE1 + " TEXT ,"
            + DTR_STRUCTURE1 + " TEXT ,"
            + NO_LI_CKT1 + " TEXT ,"
            + DTR_AB1 + " TEXT ,"
            + VOLTAGE_LEVEL + " TEXT ,"
            + DTR_LOCATION_NAME + " TEXT ,"
            + DTR_NO_OF_LT_CIRCUIT + " TEXT ,"
            + HT_SERVICE_NO + " TEXT ,"
            + PC_LINE_CAPACITOR + " TEXT ,"
            + PC_AUTO_RECLOSURE + " TEXT ,"
            + TYPE_OF_LOAD + " TEXT ,"
            + DTR_LOCATION_TYPE + " TEXT ,"
            + IS_LTCKT_AVAILABLE + " TEXT ,"
            + NO_OF_AB_SWITCHES + " TEXT ,"
            + WORKORDER_ID + " TEXT ," + WO_STATUS + " TEXT ," + IS_FEEDER_TYPE + " TEXT ," + SURVEY_TYPE + " TEXT ," + FLAG3 + " TEXT " + ")";


    String CREATE_WO_STATUS_MASTER_TABLE = "CREATE TABLE " + WO_STATUS_TABLE + "("
            + FEEDER_ID + " TEXT ," + WO_ID + " TEXT ," + WO_STATUS + " TEXT ," + FLAG1 + " TEXT ," + FLAG2 + " TEXT ," + FLAG3 + " TEXT " + ")";


    public ArcGISDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ARCGIS_TABLE);
        // db.execSQL(CREATE_FEEDERMASTER_TABLE);
        db.execSQL(CREATE_USERMASTER_TABLE);
        db.execSQL(CREATE_EHTMASTER_TABLE);
        db.execSQL(CREATE_SSMASTER_TABLE);
        db.execSQL(CREATE_DTR_MASTERS_TABLE);
        db.execSQL(CREATE_MAKE_MASTERS_TABLE);
        db.execSQL(CREATE_FEEDER_SERVICE_MASTERS_TABLE);
        db.execSQL(CREATE_MULTIVOLTAGE_MASTERS_TABLE);

        db.execSQL(CREATE_INCREMENTAL_EHTMASTER_TABLE);
        db.execSQL(CREATE_INCREMENTAL_ARCGIS_TABLE);
        db.execSQL(CREATE_INCREMENTALSSMASTER_TABLE);
        db.execSQL(CREATE_INCREMENTAL_DTR_MASTERS_TABLE);
        db.execSQL(CREATE_INCREMENTAL_FEEDER_SERVICE_MASTERS_TABLE);
        db.execSQL(CREATE_INCREMENTAL_MAKE_MASTERS_TABLE);
        db.execSQL(CREATE_INCREMENTAL_MULTIVOLTAGE_MASTERS_TABLE);
        db.execSQL(CREATE_CMP_EHTMASTER_TABLE);
        db.execSQL(CREATE_WO_STATUS_MASTER_TABLE);
        db.execSQL(CREATE_TABLE_REPEAT);
        db.execSQL(CREATE_IMAGE_11KV_TABLE);
        db.execSQL(CREATE_ARCGIS_TABLE_LAT_LANG_EDIT);
        db.execSQL(CREATE_ARCGIS_TABLE_SOURCE_POLE_EDIT);
        db.execSQL(CREATE_ARCGIS_TABLE_POLE_DETAILS_EDIT);


        // db.execSQL("CREATE TABLE if/* private static final String FALG ="NO_POLES";
        //    private static final String NO_POLES ="NO_POLES";
        //    private static final String NO_POLES ="NO_POLES";
        //    private static final String NO_POLES ="NO_POLES";*/ not exists PROFILE_MASTER (USERNAME  TEXT , ROLE  TEXT,OFFICE_ID TEXT,NAME TEXT,SECTION_NAME TEXT, SUBDIVISION_NAME TEXT,DIVISION_NAME TEXT,CIRCLE_NAME TEXT)");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + ARCGIS_TABLE);
        //db.execSQL("DROP TABLE IF EXISTS " + FEEDERMASTER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + USERMASTER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + EHTMASTER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SSMASTER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DTR_MASTER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MAKE_MASTER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + FEEDER_SERVICE_MASTER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MULTIVOLTAGE_MASTERS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + INCREMENTAL_EHTMASTER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + INCREMENTAL_ARCGIS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + INCREMENTALSSMASTER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + INCREMENTAL_DTR_MASTER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + INCREMENTAL_FEEDER_SERVICE_MASTER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + INCREMENTAL_MAKE_MASTER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + INCREMENTAL_MULTIVOLTAGE_MASTERS_TABLE);

        db.execSQL("DROP TABLE IF EXISTS " + CMP_EHTMASTER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + WO_STATUS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ARCGIS_TABLE_REPEAT);
        db.execSQL("DROP TABLE IF EXISTS " + IMAGE_11KV_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ARCGIS_TABLE_LAT_LANG_EDIT);
        db.execSQL("DROP TABLE IF EXISTS " + ARCGIS_TABLE_SOURCE_POLE_EDIT);
        db.execSQL("DROP TABLE IF EXISTS " + ARCGIS_TABLE_POLE_DETAILS_EDIT);


        // Create tables again
        onCreate(db);
    }


    public void clearWoStatus() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + WO_STATUS_TABLE);
    }

    public void clearIncrementalEhtMaster() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + INCREMENTAL_EHTMASTER_TABLE);
    }

    public void clearIncrementalSurveyedData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + INCREMENTAL_ARCGIS_TABLE);
    }

    public void clearLatLngSurveyData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + ARCGIS_TABLE_LAT_LANG_EDIT);
    }

    public void clearSourcePoleData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + ARCGIS_TABLE_SOURCE_POLE_EDIT);
    }

    public void clearEditPoleData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + ARCGIS_TABLE_POLE_DETAILS_EDIT);
    }

    public void clearssIncrementalMasterData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + INCREMENTALSSMASTER_TABLE);
    }

    public void clearIncrementalDTRMasterData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + INCREMENTAL_DTR_MASTER_TABLE);
    }

    public void clearIncrementalFeederServiceMasterData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + INCREMENTAL_FEEDER_SERVICE_MASTER_TABLE);
    }

    public void clearIncrementalMakeMaster() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + INCREMENTAL_MAKE_MASTER_TABLE);
    }

    public void clearIncrementalMultivoltageeMasterData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + INCREMENTAL_MULTIVOLTAGE_MASTERS_TABLE);
    }


    public void upDate(String sQuery) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sQuery);

    }


    public void clearMakeMaster() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + MAKE_MASTER_TABLE);
    }

    public void clearFeederMaster() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + FEEDERMASTER_TABLE);
    }

    public void clearSurveyedData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + ARCGIS_TABLE);
    }

    public void clearImageData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + IMAGE_11KV_TABLE);
    }

    public void clearUserdData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + USERMASTER_TABLE);
    }

    public void clearFeederData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + EHTMASTER_TABLE);
    }

    public void clearCmpFeederData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + CMP_EHTMASTER_TABLE);
    }

    public void clearssMasterData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + SSMASTER_TABLE);
    }

    public void clearDTRMasterData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + DTR_MASTER_TABLE);
    }

    public void clearFeederServiceMasterData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + FEEDER_SERVICE_MASTER_TABLE);
    }

    public void clearMultivoltageeMasterData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + MULTIVOLTAGE_MASTERS_TABLE);
    }

    public void clearData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + ARCGIS_TABLE);
        db.execSQL("DELETE FROM " + USERMASTER_TABLE);
        db.execSQL("DELETE FROM " + EHTMASTER_TABLE);
        db.execSQL("DELETE FROM " + DTR_MASTER_TABLE);
        db.execSQL("DELETE FROM " + SSMASTER_TABLE);
        db.execSQL("DELETE FROM " + MAKE_MASTER_TABLE);
        db.execSQL("DELETE FROM " + FEEDER_SERVICE_MASTER_TABLE);
        db.execSQL("DELETE FROM " + MULTIVOLTAGE_MASTERS_TABLE);
        db.execSQL("DELETE FROM " + INCREMENTAL_EHTMASTER_TABLE);
        db.execSQL("DELETE FROM " + INCREMENTAL_ARCGIS_TABLE);
        db.execSQL("DELETE FROM " + INCREMENTALSSMASTER_TABLE);
        db.execSQL("DELETE FROM " + INCREMENTAL_DTR_MASTER_TABLE);
        db.execSQL("DELETE FROM " + INCREMENTAL_FEEDER_SERVICE_MASTER_TABLE);
        db.execSQL("DELETE FROM " + INCREMENTAL_MAKE_MASTER_TABLE);
        db.execSQL("DELETE FROM " + INCREMENTAL_MULTIVOLTAGE_MASTERS_TABLE);
        db.execSQL("DELETE FROM " + CMP_EHTMASTER_TABLE);
        db.execSQL("DELETE FROM " + WO_STATUS_TABLE);
        db.execSQL("DELETE FROM " + ARCGIS_TABLE_REPEAT);
        db.execSQL("DELETE FROM " + IMAGE_11KV_TABLE);
        db.execSQL("DELETE FROM " + ARCGIS_TABLE_LAT_LANG_EDIT);
        db.execSQL("DELETE FROM " + ARCGIS_TABLE_SOURCE_POLE_EDIT);
        db.execSQL("DELETE FROM " + ARCGIS_TABLE_POLE_DETAILS_EDIT);

    }


    public int getObjectId() {
        int stringCount = 0;
        // Select All Query
        try {
            String selectQuery = "select MAX(POLE_NO) as Result from ARCGIS_TABLE group by POLE_NO order by Result desc ";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                stringCount = cursor.getInt(0);
            }

            db.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
        // return contact list
        return stringCount;
    }

    public int getSamePole(String feeder_id, String pole_no) {
        // System.out.println("POLE FEEDER ID : "+feeder_id);
        // System.out.println("POLE NO : "+pole_no);
        int stringCount = 10;
        // Select All Query
        try {


            String query = "SELECT * FROM ARCGIS_TABLE WHERE MC_33KV_FEEDER_ID_1 = ? AND MC_33KV_POLE_1 = ?";
            SQLiteDatabase database = getWritableDatabase();
            Cursor cursor = database.rawQuery(query, new String[]{feeder_id, pole_no});
            if (cursor.moveToFirst()) {
                return 1;
            } else {
                return 0;
            }


           /* String selectQuery = "SELECT * FROM  ARCGIS_TABLE WHERE "+MC_33KV_FEEDER_ID_1+" = "+feeder_id +" AND "+MC_33KV_POLE_1+" = "+pole_no;
            System.out.println("SAME POLE QUERY : "+selectQuery);
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                stringCount = cursor.getInt(0);
            }

            db.close();*/
        } catch (Exception e) {
            e.printStackTrace();

        }
        // return contact list
        return stringCount;
    }


    public boolean addEditPoleDetailsSurvey(SurveyVo surveyDataVo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {
            values.put(ID, surveyDataVo.getID());
            values.put(FEEDER_ID, surveyDataVo.getFeeder_id());
            values.put(POLE_TYPE, surveyDataVo.getPole_type());
            values.put(POLE_HEIGHT, surveyDataVo.getPole_height());
            values.put(POLE_STRUCTURE, surveyDataVo.getPoleStructure());
            values.put(POLE_SUPPORT, surveyDataVo.getPole_support());
            values.put(POLE_PAINTINGCODE, surveyDataVo.getPole_paintingcode());
            values.put(PINPOINT, surveyDataVo.getPinpoint());
            values.put(POLE_CONCRETE, surveyDataVo.getPOLE_CONCRETE());

            // Check if record already exists
            String selection = ID + "=? AND " + FEEDER_ID + "=?";
            String[] selectionArgs = {
                    String.valueOf(surveyDataVo.getPole_no()),
                    surveyDataVo.getFeeder_id()
            };

            Cursor cursor = db.query(
                    ARCGIS_TABLE_POLE_DETAILS_EDIT,
                    null,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            boolean exists = cursor != null && cursor.moveToFirst();

            if (cursor != null) cursor.close();

            if (exists) {
                // Update
                int rows = db.update(ARCGIS_TABLE_POLE_DETAILS_EDIT, values, selection, selectionArgs);
                db.close();
                return rows > 0;
            } else {
                // Insert
                long result = db.insert(ARCGIS_TABLE_POLE_DETAILS_EDIT, null, values);
                db.close();
                return result != -1;
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return false;
        }
    }


    public List<SurveyVo> getEditPoleSurveyData() {

        List<SurveyVo> list = new ArrayList<>();

        String query = "SELECT FEEDER_ID, POLE_TYPE, POLE_HEIGHT, " +
                "POLE_SUPPORT, POLE_STRUCTURE, POLE_CONCRETE, " +
                "POLE_PAINTINGCODE, PINPOINT, ID " +
                "FROM " + ARCGIS_TABLE_POLE_DETAILS_EDIT;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    SurveyVo vo = new SurveyVo();

                    vo.setFeeder_id(cursor.getString(cursor.getColumnIndexOrThrow("FEEDER_ID")));
                    vo.setPole_type(cursor.getString(cursor.getColumnIndexOrThrow("POLE_TYPE")));
                    vo.setPole_height(cursor.getString(cursor.getColumnIndexOrThrow("POLE_HEIGHT")));
                    vo.setPole_support(cursor.getString(cursor.getColumnIndexOrThrow("POLE_SUPPORT")));
                    vo.setPoleStructure(cursor.getString(cursor.getColumnIndexOrThrow("POLE_STRUCTURE")));
                    vo.setPOLE_CONCRETE(cursor.getString(cursor.getColumnIndexOrThrow("POLE_CONCRETE")));
                    vo.setPole_paintingcode(cursor.getString(cursor.getColumnIndexOrThrow("POLE_PAINTINGCODE")));
                    vo.setPinpoint(cursor.getString(cursor.getColumnIndexOrThrow("PINPOINT")));
                    vo.setID(cursor.getString(cursor.getColumnIndexOrThrow("ID")));

                    list.add(vo);

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return list;
    }


    public boolean addEditSourcePoleSurvey(SurveyVo surveyDataVo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {
            values.put(ID, surveyDataVo.getID());
            values.put(FEEDER_ID, surveyDataVo.getFeeder_id());
            values.put(FROM_POLE, surveyDataVo.getFrom_pole());
            // Check if record already exists
            String selection = ID + "=? AND " + FEEDER_ID + "=?";
            String[] selectionArgs = {
                    String.valueOf(surveyDataVo.getPole_no()),
                    surveyDataVo.getFeeder_id()
            };

            Cursor cursor = db.query(
                    ARCGIS_TABLE_SOURCE_POLE_EDIT,
                    null,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            boolean exists = cursor != null && cursor.moveToFirst();

            if (cursor != null) cursor.close();

            if (exists) {
                // Update
                int rows = db.update(ARCGIS_TABLE_SOURCE_POLE_EDIT, values, selection, selectionArgs);
                db.close();
                return rows > 0;
            } else {
                // Insert
                long result = db.insert(ARCGIS_TABLE_SOURCE_POLE_EDIT, null, values);
                db.close();
                return result != -1;
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return false;
        }
    }

    public List<SurveyVo> getEditSourcePoleSurveyData() {

        String query = "";
        ArrayList<SurveyVo> surveyDataVoList = new ArrayList<>();

        query = "SELECT FEEDER_ID, FROM_POLE, ID FROM ARCGIS_TABLE_SOURCE_POLE_EDIT";


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, null);


            if (cursor.moveToFirst()) {
                do {
                    SurveyVo surveyDataVo = new SurveyVo();
                    surveyDataVo.setFeeder_id(cursor.getString(0));
                    surveyDataVo.setFrom_pole(cursor.getInt(1));
                    surveyDataVo.setID(cursor.getString(2));
                    surveyDataVoList.add(surveyDataVo);

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return surveyDataVoList;
    }

    public int getIncrementalObjectId() {
        int stringCount = 0;
        // Select All Query
        try {
            String selectQuery = "select MAX(POLE_NO) as Result from INCREMENTAL_ARCGIS_TABLE group by POLE_NO order by Result desc ";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                stringCount = cursor.getInt(0);
            }

            db.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
        // return contact list
        return stringCount;
    }

    public List<String> getIncrementalWorkOrderStatus(String condition) {
        List<String> stringList = new ArrayList<>();
        // Select All Query
        String selectQuery = condition;


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    String string = cursor.getString(0);
                    stringList.add(string);
                } while (cursor.moveToNext());
            }
        } finally {
            db.close();
        }
        // return contact list
        return stringList;
    }

    public int getObjectIdCount() {
        int stringCount = 0;
        // Select All Query
        try {
            String selectQuery = "select MAX(POLE_NO) as Result from ARCGIS_TABLE WHERE FLAG1='N' group by POLE_NO order by Result desc ";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                stringCount = cursor.getInt(0);
            }

            db.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
        // return contact list
        return stringCount;
    }


    public int getIncrementalObjectIdCount() {
        int stringCount = 0;
        // Select All Query
        try {
            String selectQuery = "select MAX(POLE_NO) as Result from INCREMENTAL_ARCGIS_TABLE WHERE FLAG1='N' group by POLE_NO order by Result desc ";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                stringCount = cursor.getInt(0);
            }

            db.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
        // return contact list
        return stringCount;
    }

    public String getFeederId(String query) {
        String feederId = "";
        // Select All Query
        try {
            String selectQuery = query;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                feederId = cursor.getString(0);
            }

            db.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
        // return contact list
        return feederId;
    }

    public List<String> getSSName(String query) {
        List<String> ssList = new ArrayList<>();
        String feederId = "";
        // Select All Query
        try {
            String selectQuery = query;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                feederId = cursor.getString(0);
                ssList.add(feederId);
            }
            while (cursor.moveToNext()) ;

            db.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
        // return contact list
        return ssList;
    }

    public List<String> getFeederName() {
        List<String> feederList = new ArrayList<>();
        String feederName = "";
        // Select All Query
        try {
            String selectQuery = "SELECT FEEDER_NAME_33KV FROM FEEDERMASTER_TABLE";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                feederName = cursor.getString(0);
                feederList.add(feederName);
            }
            while (cursor.moveToNext()) ;


        } catch (Exception e) {
            e.printStackTrace();

        }
        // return contact list
        return feederList;
    }

    public List<String> getEHTSSNAME() {
        List<String> stringList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT DISTINCT  EHT_SS_CODE|| '-' ||EHT_SS_NAME FROM EHTMASTER_TABLE WHERE FEEDER_CODE_33KV IS NOT NULL";


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    String string = cursor.getString(0);
                    stringList.add(string);
                } while (cursor.moveToNext());
            }
        } finally {
            db.close();
        }
        // return contact list
        return stringList;
    }


    public List<String> getCmpEHTSSNAME() {
        List<String> stringList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT DISTINCT  EHT_SS_CODE|| '-' ||EHT_SS_NAME FROM CMP_EHTMASTER_TABLE WHERE FEEDER_CODE_33KV IS NOT NULL";


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    String string = cursor.getString(0);
                    stringList.add(string);
                } while (cursor.moveToNext());
            }
        } finally {
            db.close();
        }
        // return contact list
        return stringList;
    }


    public List<String> getIncrementalEHTSSNAME() {
        List<String> stringList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT DISTINCT  EHT_SS_CODE|| '-' ||EHT_SS_NAME FROM INCREMENTAL_EHTMASTER_TABLE WHERE FEEDER_CODE_33KV IS NOT NULL";


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    String string = cursor.getString(0);
                    stringList.add(string);
                } while (cursor.moveToNext());
            }
        } finally {
            db.close();
        }
        // return contact list
        return stringList;
    }

    public List<String> getSSNAMEList() {
        List<String> stringList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT EHT_SS_NAME FROM SSMASTER_TABLE";


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    String string = cursor.getString(0);
                    stringList.add(string);
                } while (cursor.moveToNext());
            }
        } finally {
            db.close();
        }
        // return contact list
        return stringList;
    }


    public List<String> getComplaint(String condition) {
        List<String> stringList = new ArrayList<>();
        // Select All Query
        String selectQuery = condition;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    String string = cursor.getString(0);
                    stringList.add(string);
                } while (cursor.moveToNext());
            }
        } finally {
            db.close();
        }
        // return contact list
        return stringList;
    }


    public boolean addSSMasterData(SSMasterVo ssMasterVo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EHT_SS_CODE, ssMasterVo.getFEEDING_SS_CODE());
        values.put(EHT_SS_NAME, ssMasterVo.getFEEDING_SS_NAME());
        values.put(FEEDER_CODE_33KV, ssMasterVo.getFEEDER_CODE_33KV());

        long a = db.insert(SSMASTER_TABLE, null, values);
        if (a == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }


    public List<SSMasterVo> getSSMasterData() {
        ArrayList<SSMasterVo> ssResponseList = new ArrayList<SSMasterVo>();

        String query = "SELECT * FROM SSMASTER_TABLE";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {

                SSMasterVo ssMasterVo = new SSMasterVo();
                //vo.setLat(cursor.getString(0));
                ssMasterVo.setFEEDING_SS_CODE(cursor.getString(0));
                ssMasterVo.setFEEDING_SS_NAME(cursor.getString(1));
                ssMasterVo.setFEEDER_CODE_33KV(cursor.getString(2));

                //  vo.setSECTION_CODE(cursor.getString(8));
                ssResponseList.add(ssMasterVo);
            } while (cursor.moveToNext());
        }

        db.close();
        return ssResponseList;
    }

    public boolean addUserMasterData(LoginDataResponse loginDataResponse) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ROLE, loginDataResponse.getROLE());
        values.put(OFFICE_ID, loginDataResponse.getOFFICE_ID());
        values.put(SECTION_NAME, loginDataResponse.getSECTION_NAME());
        values.put(SEC_CODE, loginDataResponse.getSEC_CODE());

        long a = db.insert(USERMASTER_TABLE, null, values);
        if (a == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }

    public List<LoginDataResponse> getUserMasterData() {
        ArrayList<LoginDataResponse> loginDataResponses = new ArrayList<LoginDataResponse>();

        String query = "SELECT * FROM USERMASTER_TABLE";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {

                LoginDataResponse loginVo = new LoginDataResponse();
                //vo.setLat(cursor.getString(0));
                loginVo.setROLE(cursor.getString(0));
                loginVo.setOFFICE_ID(cursor.getString(1));
                loginVo.setSECTION_NAME(cursor.getString(2));
                loginVo.setSEC_CODE(cursor.getString(3));
                //  vo.setSECTION_CODE(cursor.getString(8));
                loginDataResponses.add(loginVo);
            } while (cursor.moveToNext());
        }

        db.close();
        return loginDataResponses;
    }

//+ EHT_SS_CODE + " TEXT ," + EHT_SS_NAME + " TEXT ," + FEEDER_NAME_33KV + " TEXT ," + FEEDER_CODE_33KV + " TEXT ," + FLAG1 +

    public boolean addFeederData(FeederDataVo feederDataVo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EHT_SS_CODE, feederDataVo.getEHT_SS_CODE());
        values.put(EHT_SS_NAME, feederDataVo.getEHT_SS_NAME());
        values.put(FEEDER_NAME_33KV, feederDataVo.getFEEDER_NAME_33KV());
        values.put(FEEDER_CODE_33KV, feederDataVo.getFEEDER_CODE_33KV());
        // values.put(FEEDER_CODE_33KV, feederDataVo.getFEEDER_CODE_33KV());
        values.put(WO_ID, feederDataVo.getWO_ID());
        values.put(IS_FEEDER_TYPE, feederDataVo.getIS_FEEDER_TYPE());
        values.put(ALLOW_SURVEY, feederDataVo.getALLOW_SURVEY());


        long a = db.insert(EHTMASTER_TABLE, null, values);
        if (a == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }

    public boolean addCmpFeederData(FeederDataVo feederDataVo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EHT_SS_CODE, feederDataVo.getEHT_SS_CODE());
        values.put(EHT_SS_NAME, feederDataVo.getEHT_SS_NAME());
        values.put(FEEDER_NAME_33KV, feederDataVo.getFEEDER_NAME_33KV());
        values.put(FEEDER_CODE_33KV, feederDataVo.getFEEDER_CODE_33KV());

        long a = db.insert(CMP_EHTMASTER_TABLE, null, values);
        if (a == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }

    public List<FeederDataVo> getFeerData() {
        ArrayList<FeederDataVo> feederDataVos = new ArrayList<FeederDataVo>();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String query = "SELECT * FROM EHTMASTER_TABLE";

            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {

                    FeederDataVo feederDataVo = new FeederDataVo();
                    //vo.setLat(cursor.getString(0));
                    feederDataVo.setEHT_SS_CODE(cursor.getString(0));
                    feederDataVo.setEHT_SS_NAME(cursor.getString(1));
                    feederDataVo.setFEEDER_NAME_33KV(cursor.getString(2));
                    feederDataVo.setFEEDER_CODE_33KV(cursor.getString(3));
                    //  vo.setSECTION_CODE(cursor.getString(8));
                    feederDataVos.add(feederDataVo);
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return feederDataVos;
    }


    public List<FeederDataVo> getFeerDataByName(String ehtSsName) {
        List<FeederDataVo> feederDataVos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM " + EHTMASTER_TABLE + " WHERE " + FEEDER_NAME_33KV + " = ?";
            cursor = db.rawQuery(query, new String[]{ehtSsName});

            if (cursor.moveToFirst()) {
                do {
                    FeederDataVo feederDataVo = new FeederDataVo();
                    feederDataVo.setEHT_SS_CODE(cursor.getString(0));
                    feederDataVo.setEHT_SS_NAME(cursor.getString(1));
                    feederDataVo.setFEEDER_NAME_33KV(cursor.getString(2));
                    feederDataVo.setFEEDER_CODE_33KV(cursor.getString(3));
                    feederDataVo.setALLOW_SURVEY(cursor.getString(6));
                    // Add other fields if needed
                    feederDataVos.add(feederDataVo);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return feederDataVos;
    }



  /*  public boolean addFeederMasterData(FeederMasterResponseDataVo masterResponseDataVo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        *//*values.put(SECTION_ID,masterResponseDataVo.getSECTION_ID());
        values.put(EHT_SS_CODE, masterResponseDataVo.getEHT_SS_CODE());
        values.put(EHT_SS_NAME, masterResponseDataVo.getEHT_SS_NAME());
        values.put(VOLTAGE_LEVEL, masterResponseDataVo.getVOLTAGE_LEVEL());
        values.put(FEEDER_NAME_33KV, masterResponseDataVo.getFEEDER_NAME_33KV());
        values.put(FEEDER_CODE_33KV, masterResponseDataVo.getFEEDER_CODE_33KV());*//*
        long a = db.insert(FEEDERMASTER_TABLE, null, values);
        if(a==-1){
            db.close();
            return false;
        }else {
            db.close();
            return true;
        }

    }*/


    /* public List<FeederMasterResponseDataVo> getFeederMasterData(){
         ArrayList<FeederMasterResponseDataVo> masterResponseDataVosList= new ArrayList<FeederMasterResponseDataVo>();

         String  query = "SELECT * FROM ARCGIS_TABLE";
         SQLiteDatabase db = this.getWritableDatabase();
         Cursor cursor = db.rawQuery(query, null);
         if (cursor.moveToFirst()) {
             do {

                 FeederMasterResponseDataVo feederVo = new FeederMasterResponseDataVo();
                 //vo.setLat(cursor.getString(0));
                *//* feederVo.setSECTION_ID(cursor.getString(0));
                feederVo.setEHT_SS_CODE(cursor.getString(1));
                feederVo.setEHT_SS_NAME(cursor.getString(2));
                feederVo.setVOLTAGE_LEVEL(cursor.getString(3));
                feederVo.setFEEDER_NAME_33KV(cursor.getString(4));
                feederVo.setFEEDER_CODE_33KV(cursor.getString(5));*//*


                //  vo.setSECTION_CODE(cursor.getString(8));
                masterResponseDataVosList.add(feederVo);
            } while (cursor.moveToNext());
        }

        db.close();
        return masterResponseDataVosList;
    }*/
    public boolean addSurveyData(SurveyVo surveyVo) {
        boolean status = false;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(FEEDER_ID, surveyVo.getFeeder_id());
            values.put(POLE_NO, surveyVo.getPole_no());
            values.put(FROM_POLE, surveyVo.getFrom_pole());
            values.put(POLE_TYPE, surveyVo.getPole_type());
            values.put(POLE_HEIGHT, surveyVo.getPole_height());
            // values.put(NOOF_CIRCUITS, surveyVo.getNoof_circuits());
            values.put(POLE_SUPPORT, surveyVo.getPole_support());
            values.put(TAPPING_POINT, surveyVo.getTapping_point());
            values.put(CONDUCTOR_SIZE, surveyVo.getConductor_size());
            values.put(POLE_CONFIGURATION, surveyVo.getPole_configuration());
            values.put(PINPOINT, surveyVo.getPinpoint());
            values.put(POLE_PAINTINGCODE, surveyVo.getPole_paintingcode());
            values.put(LAT, surveyVo.getLat());
            values.put(LAN, surveyVo.getLan());
            values.put(FLAG1, surveyVo.getPastSurveyFlag());
            values.put(NO_CKT, surveyVo.getIsMultiCircuit());
            values.put(CKT_TYPE, surveyVo.getCircuitType());
            values.put(IS_ENDPOLE, surveyVo.getIsEndPole());
            values.put(LINE_TYPE, surveyVo.getLineType());
            values.put(NOOF_CIRCUITS, surveyVo.getNoofCircuits());
            values.put(SS_NAME, surveyVo.getSs_name());
            values.put(POLE_STRUCTURE, surveyVo.getPoleStructure());
            values.put(DIFF_VOLTAGE, surveyVo.getDiffVoltage());
            values.put(SS_ID, surveyVo.getSsId());
            values.put(STJOINT_RMU, surveyVo.getStjoint_rmu());
            values.put(RMU_TYPE, surveyVo.getRmu_type());
            values.put(REMARKS, surveyVo.getRemarks());
            values.put(TAPPING_LOCNAME, surveyVo.getTappingLocation());
            values.put(TAPPING_POINTS, surveyVo.getNoofTappings());

            values.put(DTR_CODE1, surveyVo.getDtr_code());
            values.put(DTR_PHASE1, surveyVo.getDtr_phase());
            values.put(DTR_SNO1, surveyVo.getDtr_scno());
            values.put(DTR_CAPACITY1, surveyVo.getDtr_capacity());
            values.put(DTR_MAKE1, surveyVo.getDtr_make());
            values.put(DTR_STRUCTURE1, surveyVo.getDtr_structure());
            values.put(NO_LI_CKT1, surveyVo.getNo_lt_ckt());
            values.put(DTR_AB1, surveyVo.getDtr_ab_switch());
            values.put(VOLTAGE_LEVEL, surveyVo.getVoltage_level());
            values.put(DTR_LOCATION_NAME, surveyVo.getDtr_location_name());
            values.put(DTR_NO_OF_LT_CIRCUIT, surveyVo.getDtr_no_of_circuit());
            values.put(HT_SERVICE_NO, surveyVo.getHt_service_number());
            values.put(PC_LINE_CAPACITOR, surveyVo.getPc_line_capacitor());
            values.put(PC_AUTO_RECLOSURE, surveyVo.getPc_auto_reclosure());

            values.put(TYPE_OF_LOAD, surveyVo.getType_of_load());
            values.put(DTR_LOCATION_TYPE, surveyVo.getDtr_location_type());
            values.put(IS_LTCKT_AVAILABLE, surveyVo.getIs_ltckt_avalable());
            values.put(NO_OF_AB_SWITCHES, surveyVo.getNo_of_ab_switches());
            values.put(WO_ID, surveyVo.getWoId());
            values.put(IS_FEEDER_TYPE, surveyVo.getIsFeederType());

            //DEFECTS AND CROSSING
            values.put(DEFFECTS_AVAILABLE, surveyVo.getDEFFECTS_AVAILABLE());
            values.put(POLE_DEFECTS, surveyVo.getPOLE_DEFECTS());
            values.put(CONDUCTOR_DEFECTS, surveyVo.getCONDUCTOR_DEFECTS());
            values.put(AB_SWITCH_DEFECTS, surveyVo.getAB_SWITCH_DEFECTS());
            values.put(INSULATOR_DEFECTS, surveyVo.getINSULATOR_DEFECTS());
            values.put(CROSSARM_DEFECTS, surveyVo.getCROSSARM_DEFECTS());
            values.put(TOPCLEAT_DEFECTS, surveyVo.getTOPCLEAT_DEFECTS());
            values.put(STRUT_DEFECTS, surveyVo.getSTRUT_DEFECTS());
            values.put(STRAY_DEFECTS, surveyVo.getSTRAY_DEFECTS());
            values.put(HTLT_CLEARANCE, surveyVo.getHTLT_CLEARANCE());
            values.put(INTERMEDIATE_POLE, surveyVo.getINTERMEDIATE_POLE());
            values.put(RESTRINGING, surveyVo.getRESTRINGING());
            values.put(TREE_CLEARANCE, surveyVo.getTREE_CLEARANCE());
            values.put(OTHER_DEFFECTS, surveyVo.getOTHER_DEFFECTS());
            values.put(CROSSING_TYPE, surveyVo.getCROSSING_TYPE());

            //MULTI CKT
            values.put(MC_33KV_FEEDER_ID_1, surveyVo.getMC_33KV_FEEDER_ID_1());
            values.put(MC_33KV_FEEDER_ID_2, surveyVo.getMC_33KV_FEEDER_ID_2());
            values.put(MC_33KV_FEEDER_ID_3, surveyVo.getMC_33KV_FEEDER_ID_3());
            values.put(MC_33KV_POLE_1, surveyVo.getMC_33KV_POLE_1());
            values.put(MC_33KV_POLE_2, surveyVo.getMC_33KV_POLE_2());
            values.put(MC_33KV_POLE_3, surveyVo.getMC_33KV_POLE_3());

            values.put(MC_11KV_FEEDER_ID_1, surveyVo.getMC_11KV_FEEDER_ID_1());
            values.put(MC_11KV_FEEDER_ID_2, surveyVo.getMC_11KV_FEEDER_ID_2());
            values.put(MC_11KV_FEEDER_ID_3, surveyVo.getMC_11KV_FEEDER_ID_3());
            values.put(MC_11KV_POLE_1, surveyVo.getMC_11KV_POLE_1());
            values.put(MC_11KV_POLE_2, surveyVo.getMC_11KV_POLE_2());
            values.put(MC_11KV_POLE_3, surveyVo.getMC_11KV_POLE_3());

            values.put(MC_LT_FEEDER_ID_1, surveyVo.getMC_LT_FEEDER_ID_1());
            values.put(MC_LT_FEEDER_ID_2, surveyVo.getMC_LT_FEEDER_ID_2());
            values.put(MC_LT_FEEDER_ID_3, surveyVo.getMC_LT_FEEDER_ID_3());
            values.put(MC_LT_POLE_1, surveyVo.getMC_LT_POLE_1());
            values.put(MC_LT_POLE_2, surveyVo.getMC_LT_POLE_2());
            values.put(MC_LT_POLE_3, surveyVo.getMC_LT_POLE_3());

            values.put(NO_33KV, surveyVo.getNO_OF33KV_CKTS());
            values.put(NO_11KV, surveyVo.getNO_OF11KV_CKTS());
            values.put(NO_LT, surveyVo.getNO_OF_LT_CKTS());


            values.put(NUM_OTHER_CABLES, surveyVo.getNUM_OTHER_CABLES());

            //CROSSING NEW
            values.put(CROSS_LT_CKT, surveyVo.getCROSS_LT_CKT());
            values.put(CROSS_6_3KV_ID, surveyVo.getCROSS_6_3KV_ID());
            values.put(CROSS_11KV_ID, surveyVo.getCROSS_11KV_ID());
            values.put(CROSS_33KV_ID, surveyVo.getCROSS_33KV_ID());
            values.put(CROSS_EHT, surveyVo.getCROSS_EHT());
            values.put(CROSS_COMMUN, surveyVo.getCROSS_COMMUN());
            values.put(CROSS_REMARKS, surveyVo.getCROSS_REMARKS());

            //JHC PHASE 1
            values.put(POLE_SURVEY_TYPE, surveyVo.getPOLE_SURVEY_TYPE());
            values.put(LINE_SURVEY_TYPE, surveyVo.getLINE_SURVEY_TYPE());
            values.put(DTR_SURVEY_TYPE, surveyVo.getDTR_SURVEY_TYPE());
            values.put(LT_SURVEY_TYPE, surveyVo.getLT_SURVEY_TYPE());

            //DTR AVAILABLE
            values.put(DTR_AVAILABLE, surveyVo.getDTR_AVAILABLE());
            values.put(POLE_CONCRETE, surveyVo.getPOLE_CONCRETE());
            values.put(CURRENT_LAT, surveyVo.getCURRENT_LAT());
            values.put(CURRENT_LONG, surveyVo.getCURRENT_LONG());
            values.put(DTR_REMARKS, surveyVo.getDTR_REMARKS());

            //MULTI CKT NOT IN LIST
            values.put(MC_33KV_NOT_IN_LIST_1, surveyVo.getMC_33KV_NOT_IN_LIST_1());
            values.put(MC_33KV_NOT_IN_LIST_2, surveyVo.getMC_33KV_NOT_IN_LIST_2());
            values.put(MC_33KV_NOT_IN_LIST_3, surveyVo.getMC_33KV_NOT_IN_LIST_3());
            values.put(MC_11KV_NOT_IN_LIST_1, surveyVo.getMC_11KV_NOT_IN_LIST_1());
            values.put(MC_11KV_NOT_IN_LIST_2, surveyVo.getMC_11KV_NOT_IN_LIST_2());
            values.put(MC_11KV_NOT_IN_LIST_3, surveyVo.getMC_11KV_NOT_IN_LIST_3());
            values.put(MC_LT_NOT_IN_LIST_1, surveyVo.getMC_LT_NOT_IN_LIST_1());
            values.put(MC_LT_NOT_IN_LIST_2, surveyVo.getMC_LT_NOT_IN_LIST_2());
            values.put(MC_LT_NOT_IN_LIST_3, surveyVo.getMC_LT_NOT_IN_LIST_3());

            //RMU
            values.put(RMU_MAKE, surveyVo.getRMU_MAKE());
            values.put(RMU_SL_NO, surveyVo.getRMU_SL_NO());
            values.put(ID, surveyVo.getID());
            values.put(GPS_ACCURACY, surveyVo.getGPS_ACCURACY());
            values.put(DEVICE_ID, surveyVo.getDEVICE_ID());

            ///MODIFICATIONS STARTS
            values.put(PACKAGE_NO,surveyVo.getPACKAGE_NO());
            values.put(DT_VOLATAGE_RATIO,surveyVo.getDT_VOLATAGE_RATIO());
            values.put(DT_MANF_DT,surveyVo.getDT_MANF_DT());
            values.put(DT_INSTL_DT,surveyVo.getDT_INSTL_DT());
            values.put(RDSS_SCHEME_TYPE,surveyVo.getRDSS_SCHEME_TYPE());
            values.put(CIRCUIT_TYPE,surveyVo.getCIRCUIT_TYPE());
            values.put(ADDRESS_1,surveyVo.getADDRESS_1());
            values.put(ADDRESS_2,surveyVo.getADDRESS_2());
            values.put(BLOCK,surveyVo.getBlock());
            values.put(VILLAGE,surveyVo.getVillage());
            values.put(TOWN,surveyVo.getTown());
            values.put(STATE,surveyVo.getState());
           // System.out.println("ADDRESS 1 IN DB : "+surveyVo.getADDRESS_1());
           // System.out.println("ADDRESS 2 IN DB : "+surveyVo.getADDRESS_2());
            ///MODIFICATIONS ENDS


            long a = db.insert(ARCGIS_TABLE, null, values);
            if (a == -1) {
                db.close();
                status = false;
            } else {
                db.close();
                status = true;
            }

        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return status;
    }

    public boolean updateSurveyData(SurveyVo surveyVo) {
        boolean status = false;
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();

            // Only put non-null or valid fields (so we don't overwrite existing data)
            if (surveyVo.getFeeder_id() != null) values.put(FEEDER_ID, surveyVo.getFeeder_id());
            if (surveyVo.getPole_no() != 0) values.put(POLE_NO, surveyVo.getPole_no());
            if (surveyVo.getFrom_pole() != 0) values.put(FROM_POLE, surveyVo.getFrom_pole());
            if (surveyVo.getPole_type() != null) values.put(POLE_TYPE, surveyVo.getPole_type());
            if (surveyVo.getPole_height() != null)
                values.put(POLE_HEIGHT, surveyVo.getPole_height());
            if (surveyVo.getPole_support() != null)
                values.put(POLE_SUPPORT, surveyVo.getPole_support());
            if (surveyVo.getTapping_point() != null)
                values.put(TAPPING_POINT, surveyVo.getTapping_point());
            if (surveyVo.getConductor_size() != null)
                values.put(CONDUCTOR_SIZE, surveyVo.getConductor_size());
            if (surveyVo.getPole_configuration() != null)
                values.put(POLE_CONFIGURATION, surveyVo.getPole_configuration());
            if (surveyVo.getPinpoint() != null) values.put(PINPOINT, surveyVo.getPinpoint());
            if (surveyVo.getPole_paintingcode() != null)
                values.put(POLE_PAINTINGCODE, surveyVo.getPole_paintingcode());
            if (surveyVo.getLat() != null) values.put(LAT, surveyVo.getLat());
            if (surveyVo.getLan() != null) values.put(LAN, surveyVo.getLan());
            if (surveyVo.getPastSurveyFlag() != null)
                values.put(FLAG1, surveyVo.getPastSurveyFlag());
            if (surveyVo.getIsMultiCircuit() != null)
                values.put(NO_CKT, surveyVo.getIsMultiCircuit());
            if (surveyVo.getCircuitType() != null) values.put(CKT_TYPE, surveyVo.getCircuitType());
            if (surveyVo.getIsEndPole() != null) values.put(IS_ENDPOLE, surveyVo.getIsEndPole());
            if (surveyVo.getLineType() != null) values.put(LINE_TYPE, surveyVo.getLineType());
            if (surveyVo.getNoofCircuits() != null)
                values.put(NOOF_CIRCUITS, surveyVo.getNoofCircuits());
            if (surveyVo.getSs_name() != null) values.put(SS_NAME, surveyVo.getSs_name());
            if (surveyVo.getPoleStructure() != null)
                values.put(POLE_STRUCTURE, surveyVo.getPoleStructure());
            if (surveyVo.getDiffVoltage() != null)
                values.put(DIFF_VOLTAGE, surveyVo.getDiffVoltage());
            if (surveyVo.getSsId() != null) values.put(SS_ID, surveyVo.getSsId());
            if (surveyVo.getStjoint_rmu() != null)
                values.put(STJOINT_RMU, surveyVo.getStjoint_rmu());
            if (surveyVo.getRmu_type() != null) values.put(RMU_TYPE, surveyVo.getRmu_type());
            if (surveyVo.getRemarks() != null) values.put(REMARKS, surveyVo.getRemarks());
            if (surveyVo.getTappingLocation() != null)
                values.put(TAPPING_LOCNAME, surveyVo.getTappingLocation());
            if (surveyVo.getNoofTappings() != null)
                values.put(TAPPING_POINTS, surveyVo.getNoofTappings());

            if (surveyVo.getDtr_code() != null) values.put(DTR_CODE1, surveyVo.getDtr_code());
            if (surveyVo.getDtr_phase() != null) values.put(DTR_PHASE1, surveyVo.getDtr_phase());
            if (surveyVo.getDtr_scno() != null) values.put(DTR_SNO1, surveyVo.getDtr_scno());
            if (surveyVo.getDtr_capacity() != null)
                values.put(DTR_CAPACITY1, surveyVo.getDtr_capacity());
            if (surveyVo.getDtr_make() != null) values.put(DTR_MAKE1, surveyVo.getDtr_make());
            if (surveyVo.getDtr_structure() != null)
                values.put(DTR_STRUCTURE1, surveyVo.getDtr_structure());
            if (surveyVo.getNo_lt_ckt() != null) values.put(NO_LI_CKT1, surveyVo.getNo_lt_ckt());
            if (surveyVo.getDtr_ab_switch() != null)
                values.put(DTR_AB1, surveyVo.getDtr_ab_switch());
            if (surveyVo.getVoltage_level() != null)
                values.put(VOLTAGE_LEVEL, surveyVo.getVoltage_level());
            if (surveyVo.getDtr_location_name() != null)
                values.put(DTR_LOCATION_NAME, surveyVo.getDtr_location_name());
            if (surveyVo.getDtr_no_of_circuit() != null)
                values.put(DTR_NO_OF_LT_CIRCUIT, surveyVo.getDtr_no_of_circuit());
            if (surveyVo.getHt_service_number() != null)
                values.put(HT_SERVICE_NO, surveyVo.getHt_service_number());
            if (surveyVo.getPc_line_capacitor() != null)
                values.put(PC_LINE_CAPACITOR, surveyVo.getPc_line_capacitor());
            if (surveyVo.getPc_auto_reclosure() != null)
                values.put(PC_AUTO_RECLOSURE, surveyVo.getPc_auto_reclosure());

            if (surveyVo.getType_of_load() != null)
                values.put(TYPE_OF_LOAD, surveyVo.getType_of_load());
            if (surveyVo.getDtr_location_type() != null)
                values.put(DTR_LOCATION_TYPE, surveyVo.getDtr_location_type());
            if (surveyVo.getIs_ltckt_avalable() != null)
                values.put(IS_LTCKT_AVAILABLE, surveyVo.getIs_ltckt_avalable());
            if (surveyVo.getNo_of_ab_switches() != null)
                values.put(NO_OF_AB_SWITCHES, surveyVo.getNo_of_ab_switches());
            if (surveyVo.getWoId() != null) values.put(WO_ID, surveyVo.getWoId());
            if (surveyVo.getIsFeederType() != null)
                values.put(IS_FEEDER_TYPE, surveyVo.getIsFeederType());

            // DEFECTS AND CROSSING
            if (surveyVo.getDEFFECTS_AVAILABLE() != null)
                values.put(DEFFECTS_AVAILABLE, surveyVo.getDEFFECTS_AVAILABLE());
            if (surveyVo.getPOLE_DEFECTS() != null)
                values.put(POLE_DEFECTS, surveyVo.getPOLE_DEFECTS());
            if (surveyVo.getCONDUCTOR_DEFECTS() != null)
                values.put(CONDUCTOR_DEFECTS, surveyVo.getCONDUCTOR_DEFECTS());
            if (surveyVo.getAB_SWITCH_DEFECTS() != null)
                values.put(AB_SWITCH_DEFECTS, surveyVo.getAB_SWITCH_DEFECTS());
            if (surveyVo.getINSULATOR_DEFECTS() != null)
                values.put(INSULATOR_DEFECTS, surveyVo.getINSULATOR_DEFECTS());
            if (surveyVo.getCROSSARM_DEFECTS() != null)
                values.put(CROSSARM_DEFECTS, surveyVo.getCROSSARM_DEFECTS());
            if (surveyVo.getTOPCLEAT_DEFECTS() != null)
                values.put(TOPCLEAT_DEFECTS, surveyVo.getTOPCLEAT_DEFECTS());
            if (surveyVo.getSTRUT_DEFECTS() != null)
                values.put(STRUT_DEFECTS, surveyVo.getSTRUT_DEFECTS());
            if (surveyVo.getSTRAY_DEFECTS() != null)
                values.put(STRAY_DEFECTS, surveyVo.getSTRAY_DEFECTS());
            if (surveyVo.getHTLT_CLEARANCE() != null)
                values.put(HTLT_CLEARANCE, surveyVo.getHTLT_CLEARANCE());
            if (surveyVo.getINTERMEDIATE_POLE() != null)
                values.put(INTERMEDIATE_POLE, surveyVo.getINTERMEDIATE_POLE());
            if (surveyVo.getRESTRINGING() != null)
                values.put(RESTRINGING, surveyVo.getRESTRINGING());
            if (surveyVo.getTREE_CLEARANCE() != null)
                values.put(TREE_CLEARANCE, surveyVo.getTREE_CLEARANCE());
            if (surveyVo.getOTHER_DEFFECTS() != null)
                values.put(OTHER_DEFFECTS, surveyVo.getOTHER_DEFFECTS());
            if (surveyVo.getCROSSING_TYPE() != null)
                values.put(CROSSING_TYPE, surveyVo.getCROSSING_TYPE());

            // Multi Circuit Fields
            if (surveyVo.getMC_33KV_FEEDER_ID_1() != null)
                values.put(MC_33KV_FEEDER_ID_1, surveyVo.getMC_33KV_FEEDER_ID_1());
            if (surveyVo.getMC_33KV_FEEDER_ID_2() != null)
                values.put(MC_33KV_FEEDER_ID_2, surveyVo.getMC_33KV_FEEDER_ID_2());
            if (surveyVo.getMC_33KV_FEEDER_ID_3() != null)
                values.put(MC_33KV_FEEDER_ID_3, surveyVo.getMC_33KV_FEEDER_ID_3());
            if (surveyVo.getMC_33KV_POLE_1() != null)
                values.put(MC_33KV_POLE_1, surveyVo.getMC_33KV_POLE_1());
            if (surveyVo.getMC_33KV_POLE_2() != null)
                values.put(MC_33KV_POLE_2, surveyVo.getMC_33KV_POLE_2());
            if (surveyVo.getMC_33KV_POLE_3() != null)
                values.put(MC_33KV_POLE_3, surveyVo.getMC_33KV_POLE_3());
            if (surveyVo.getMC_11KV_FEEDER_ID_1() != null)
                values.put(MC_11KV_FEEDER_ID_1, surveyVo.getMC_11KV_FEEDER_ID_1());
            if (surveyVo.getMC_11KV_FEEDER_ID_2() != null)
                values.put(MC_11KV_FEEDER_ID_2, surveyVo.getMC_11KV_FEEDER_ID_2());
            if (surveyVo.getMC_11KV_FEEDER_ID_3() != null)
                values.put(MC_11KV_FEEDER_ID_3, surveyVo.getMC_11KV_FEEDER_ID_3());
            if (surveyVo.getMC_11KV_POLE_1() != null)
                values.put(MC_11KV_POLE_1, surveyVo.getMC_11KV_POLE_1());
            if (surveyVo.getMC_11KV_POLE_2() != null)
                values.put(MC_11KV_POLE_2, surveyVo.getMC_11KV_POLE_2());
            if (surveyVo.getMC_11KV_POLE_3() != null)
                values.put(MC_11KV_POLE_3, surveyVo.getMC_11KV_POLE_3());
            if (surveyVo.getMC_LT_FEEDER_ID_1() != null)
                values.put(MC_LT_FEEDER_ID_1, surveyVo.getMC_LT_FEEDER_ID_1());
            if (surveyVo.getMC_LT_FEEDER_ID_2() != null)
                values.put(MC_LT_FEEDER_ID_2, surveyVo.getMC_LT_FEEDER_ID_2());
            if (surveyVo.getMC_LT_FEEDER_ID_3() != null)
                values.put(MC_LT_FEEDER_ID_3, surveyVo.getMC_LT_FEEDER_ID_3());
            if (surveyVo.getMC_LT_POLE_1() != null)
                values.put(MC_LT_POLE_1, surveyVo.getMC_LT_POLE_1());
            if (surveyVo.getMC_LT_POLE_2() != null)
                values.put(MC_LT_POLE_2, surveyVo.getMC_LT_POLE_2());
            if (surveyVo.getMC_LT_POLE_3() != null)
                values.put(MC_LT_POLE_3, surveyVo.getMC_LT_POLE_3());

            if (surveyVo.getNO_OF33KV_CKTS() != null)
                values.put(NO_33KV, surveyVo.getNO_OF33KV_CKTS());
            if (surveyVo.getNO_OF11KV_CKTS() != null)
                values.put(NO_11KV, surveyVo.getNO_OF11KV_CKTS());
            if (surveyVo.getNO_OF_LT_CKTS() != null) values.put(NO_LT, surveyVo.getNO_OF_LT_CKTS());
            if (surveyVo.getNUM_OTHER_CABLES() != null)
                values.put(NUM_OTHER_CABLES, surveyVo.getNUM_OTHER_CABLES());

            // CROSSING NEW
            if (surveyVo.getCROSS_LT_CKT() != null)
                values.put(CROSS_LT_CKT, surveyVo.getCROSS_LT_CKT());
            if (surveyVo.getCROSS_6_3KV_ID() != null)
                values.put(CROSS_6_3KV_ID, surveyVo.getCROSS_6_3KV_ID());
            if (surveyVo.getCROSS_11KV_ID() != null)
                values.put(CROSS_11KV_ID, surveyVo.getCROSS_11KV_ID());
            if (surveyVo.getCROSS_33KV_ID() != null)
                values.put(CROSS_33KV_ID, surveyVo.getCROSS_33KV_ID());
            if (surveyVo.getCROSS_EHT() != null) values.put(CROSS_EHT, surveyVo.getCROSS_EHT());
            if (surveyVo.getCROSS_COMMUN() != null)
                values.put(CROSS_COMMUN, surveyVo.getCROSS_COMMUN());
            if (surveyVo.getCROSS_REMARKS() != null)
                values.put(CROSS_REMARKS, surveyVo.getCROSS_REMARKS());

            // JHC PHASE 1
            if (surveyVo.getPOLE_SURVEY_TYPE() != null)
                values.put(POLE_SURVEY_TYPE, surveyVo.getPOLE_SURVEY_TYPE());
            if (surveyVo.getLINE_SURVEY_TYPE() != null)
                values.put(LINE_SURVEY_TYPE, surveyVo.getLINE_SURVEY_TYPE());
            if (surveyVo.getDTR_SURVEY_TYPE() != null)
                values.put(DTR_SURVEY_TYPE, surveyVo.getDTR_SURVEY_TYPE());
            if (surveyVo.getLT_SURVEY_TYPE() != null)
                values.put(LT_SURVEY_TYPE, surveyVo.getLT_SURVEY_TYPE());
            if (surveyVo.getDTR_AVAILABLE() != null)
                values.put(DTR_AVAILABLE, surveyVo.getDTR_AVAILABLE());
            if (surveyVo.getPOLE_CONCRETE() != null)
                values.put(POLE_CONCRETE, surveyVo.getPOLE_CONCRETE());
            if (surveyVo.getCURRENT_LAT() != null)
                values.put(CURRENT_LAT, surveyVo.getCURRENT_LAT());
            if (surveyVo.getCURRENT_LONG() != null)
                values.put(CURRENT_LONG, surveyVo.getCURRENT_LONG());
            if (surveyVo.getDTR_REMARKS() != null)
                values.put(DTR_REMARKS, surveyVo.getDTR_REMARKS());

            if (surveyVo.getID() != null)
                values.put(ID, surveyVo.getID());

            // MULTI CKT NOT IN LIST
            if (surveyVo.getMC_33KV_NOT_IN_LIST_1() != null)
                values.put(MC_33KV_NOT_IN_LIST_1, surveyVo.getMC_33KV_NOT_IN_LIST_1());
            if (surveyVo.getMC_33KV_NOT_IN_LIST_2() != null)
                values.put(MC_33KV_NOT_IN_LIST_2, surveyVo.getMC_33KV_NOT_IN_LIST_2());
            if (surveyVo.getMC_33KV_NOT_IN_LIST_3() != null)
                values.put(MC_33KV_NOT_IN_LIST_3, surveyVo.getMC_33KV_NOT_IN_LIST_3());
            if (surveyVo.getMC_11KV_NOT_IN_LIST_1() != null)
                values.put(MC_11KV_NOT_IN_LIST_1, surveyVo.getMC_11KV_NOT_IN_LIST_1());
            if (surveyVo.getMC_11KV_NOT_IN_LIST_2() != null)
                values.put(MC_11KV_NOT_IN_LIST_2, surveyVo.getMC_11KV_NOT_IN_LIST_2());
            if (surveyVo.getMC_11KV_NOT_IN_LIST_3() != null)
                values.put(MC_11KV_NOT_IN_LIST_3, surveyVo.getMC_11KV_NOT_IN_LIST_3());
            if (surveyVo.getMC_LT_NOT_IN_LIST_1() != null)
                values.put(MC_LT_NOT_IN_LIST_1, surveyVo.getMC_LT_NOT_IN_LIST_1());
            if (surveyVo.getMC_LT_NOT_IN_LIST_2() != null)
                values.put(MC_LT_NOT_IN_LIST_2, surveyVo.getMC_LT_NOT_IN_LIST_2());
            if (surveyVo.getMC_LT_NOT_IN_LIST_3() != null)
                values.put(MC_LT_NOT_IN_LIST_3, surveyVo.getMC_LT_NOT_IN_LIST_3());

            // RMU
            if (surveyVo.getRMU_MAKE() != null) values.put(RMU_MAKE, surveyVo.getRMU_MAKE());
            if (surveyVo.getRMU_SL_NO() != null) values.put(RMU_SL_NO, surveyVo.getRMU_SL_NO());

            // ✅ Update condition
            String whereClause = FEEDER_ID + " = ? AND " + POLE_NO + " = ?";
            String[] whereArgs = {
                    surveyVo.getFeeder_id(),
                    String.valueOf(surveyVo.getPole_no())
            };

            if (values.size() > 0) {
                int rows = db.update(ARCGIS_TABLE, values, whereClause, whereArgs);
                status = rows > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            status = false;
        } finally {
            db.close();
        }

        return status;
    }

    public List<SurveyVo> getPoleLatLanData() {
        List<SurveyVo> surveyList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String[] columns = {POLE_NO, LAT, LAN};
            cursor = db.query(
                    ARCGIS_TABLE,    // Table name
                    columns,         // Columns to return
                    null,            // WHERE clause (null = all rows)
                    null,            // WHERE arguments
                    null,            // GROUP BY
                    null,            // HAVING
                    null             // ORDER BY
            );

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    SurveyVo surveyVo = new SurveyVo();

                    // getInt or getString depending on your datatype
                    // Example: if pole_no is INT, use getInt
                    try {
                        surveyVo.setPole_no(cursor.getInt(cursor.getColumnIndexOrThrow(POLE_NO)));
                    } catch (Exception e) {
                        surveyVo.setPole_no(0); // default if conversion fails
                    }

                    surveyVo.setLat(cursor.getString(cursor.getColumnIndexOrThrow(LAT)));
                    surveyVo.setLan(cursor.getString(cursor.getColumnIndexOrThrow(LAN)));

                    surveyList.add(surveyVo);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return surveyList;
    }


    public List<SurveyVo> checkMultiCktForLT() {
        String query = "SELECT * FROM ARCGIS_TABLE WHERE FLAG1='N'";
        ArrayList<SurveyVo> surveyVoArrayList = new ArrayList<SurveyVo>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                SurveyVo vo = new SurveyVo();
                vo.setMC_33KV_FEEDER_ID_1(cursor.getString(64));
                vo.setMC_33KV_FEEDER_ID_2(cursor.getString(65));
                vo.setMC_33KV_FEEDER_ID_3(cursor.getString(66));
                vo.setMC_33KV_POLE_1(cursor.getString(67));
                vo.setMC_33KV_POLE_2(cursor.getString(68));
                vo.setMC_33KV_POLE_3(cursor.getString(69));

                vo.setMC_11KV_FEEDER_ID_1(cursor.getString(70));
                vo.setMC_11KV_FEEDER_ID_2(cursor.getString(71));
                vo.setMC_11KV_FEEDER_ID_3(cursor.getString(72));
                vo.setMC_11KV_POLE_1(cursor.getString(73));
                vo.setMC_11KV_POLE_2(cursor.getString(74));
                vo.setMC_11KV_POLE_3(cursor.getString(75));

                vo.setMC_LT_FEEDER_ID_1(cursor.getString(76));
                vo.setMC_LT_FEEDER_ID_2(cursor.getString(77));
                vo.setMC_LT_FEEDER_ID_3(cursor.getString(78));
                vo.setMC_LT_POLE_1(cursor.getString(79));
                vo.setMC_LT_POLE_2(cursor.getString(80));
                vo.setMC_LT_POLE_3(cursor.getString(81));
                surveyVoArrayList.add(vo);
            } while (cursor.moveToNext());

        }
        db.close();
        return surveyVoArrayList;

    }
//OLD
//    public ArrayList<SurveyVo> getSurveyDataByPoleNo(int poleNo) {
//        ArrayList<SurveyVo> surveyVoArrayList = new ArrayList<>();
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        try {
//            String query = "SELECT POLE_NO, VOLTAGE_LEVEL FROM ARCGIS_TABLE WHERE pole_no = ?";
//            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(poleNo)});
//
//            if (cursor.moveToFirst()) {
//                do {
//                    SurveyVo vo = new SurveyVo();
//                    vo.setPole_no(cursor.getInt(0));
//                    vo.setVoltage_level(cursor.getString(1));
//
//                    surveyVoArrayList.add(vo);
//                } while (cursor.moveToNext());
//            }
//            cursor.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            db.close();
//        }
//
//        return surveyVoArrayList;
//    }

    //NEW
    public ArrayList<SurveyVo> getSurveyDataByPoleNo(int poleNo) {
        ArrayList<SurveyVo> surveyVoArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM ARCGIS_TABLE WHERE pole_no = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(poleNo)});

            if (cursor.moveToFirst()) {
                do {
                    SurveyVo vo = new SurveyVo();

                    vo.setFeeder_id(cursor.getString(0));
                    vo.setPole_no(cursor.getInt(1));
                    vo.setFrom_pole(cursor.getInt(2));
                    vo.setPole_type(cursor.getString(3));
                    vo.setPole_height(cursor.getString(4));
                    vo.setPole_support(cursor.getString(5));
                    vo.setTapping_point(cursor.getString(6));
                    vo.setConductor_size(cursor.getString(7));
                    vo.setPole_configuration(cursor.getString(8));
                    vo.setPinpoint(cursor.getString(9));
                    vo.setPole_paintingcode(cursor.getString(10));
                    vo.setLat(cursor.getString(11));
                    vo.setLan(cursor.getString(12));
                    vo.setPastSurveyFlag(cursor.getString(13));
                    vo.setIsMultiCircuit(cursor.getString(14));
                    vo.setCircuitType(cursor.getString(15));
                    vo.setIsEndPole(cursor.getString(16));
                    vo.setLineType(cursor.getString(17));
                    vo.setNoofCircuits(cursor.getString(18));
                    vo.setSs_name(cursor.getString(19));
                    vo.setPoleStructure(cursor.getString(20));
                    vo.setDiffVoltage(cursor.getString(21));
                    vo.setSsId(cursor.getString(22));
                    vo.setStjoint_rmu(cursor.getString(23));
                    vo.setRmu_type(cursor.getString(24));
                    vo.setRemarks(cursor.getString(25));
                    vo.setTappingLocation(cursor.getString(26));
                    vo.setNoofTappings(cursor.getString(27));

                    vo.setDtr_code(cursor.getString(28));
                    vo.setDtr_phase(cursor.getString(29));
                    vo.setDtr_scno(cursor.getString(30));
                    vo.setDtr_capacity(cursor.getString(31));
                    vo.setDtr_make(cursor.getString(32));
                    vo.setDtr_structure(cursor.getString(33));
                    vo.setNo_lt_ckt(cursor.getString(34));
                    vo.setDtr_ab_switch(cursor.getString(35));
                    vo.setVoltage_level(cursor.getString(36));
                    vo.setDtr_location_name(cursor.getString(37));
                    vo.setDtr_no_of_circuit(cursor.getString(38));
                    vo.setHt_service_number(cursor.getString(39));
                    vo.setPc_line_capacitor(cursor.getString(40));
                    vo.setPc_auto_reclosure(cursor.getString(41));
                    vo.setType_of_load(cursor.getString(42));
                    vo.setDtr_location_type(cursor.getString(43));
                    vo.setIs_ltckt_avalable(cursor.getString(44));
                    vo.setNo_of_ab_switches(cursor.getString(45));
                    vo.setWoId(cursor.getString(46));
                    vo.setIsFeederType(cursor.getString(47));
                    vo.setSurvaytype(cursor.getString(48));

                    vo.setDEFFECTS_AVAILABLE(cursor.getString(49));
                    vo.setPOLE_DEFECTS(cursor.getString(50));
                    vo.setCONDUCTOR_DEFECTS(cursor.getString(51));
                    vo.setAB_SWITCH_DEFECTS(cursor.getString(52));
                    vo.setINSULATOR_DEFECTS(cursor.getString(53));
                    vo.setCROSSARM_DEFECTS(cursor.getString(54));
                    vo.setTOPCLEAT_DEFECTS(cursor.getString(55));
                    vo.setSTRUT_DEFECTS(cursor.getString(56));
                    vo.setSTRAY_DEFECTS(cursor.getString(57));
                    vo.setHTLT_CLEARANCE(cursor.getString(58));
                    vo.setINTERMEDIATE_POLE(cursor.getString(59));
                    vo.setRESTRINGING(cursor.getString(60));
                    vo.setTREE_CLEARANCE(cursor.getString(61));
                    vo.setOTHER_DEFFECTS(cursor.getString(62));
                    vo.setCROSSING_TYPE(cursor.getString(63));

                    // MULTI CKT
                    vo.setMC_33KV_FEEDER_ID_1(cursor.getString(64));
                    vo.setMC_33KV_FEEDER_ID_2(cursor.getString(65));
                    vo.setMC_33KV_FEEDER_ID_3(cursor.getString(66));
                    vo.setMC_33KV_POLE_1(cursor.getString(67));
                    vo.setMC_33KV_POLE_2(cursor.getString(68));
                    vo.setMC_33KV_POLE_3(cursor.getString(69));

                    vo.setMC_11KV_FEEDER_ID_1(cursor.getString(70));
                    vo.setMC_11KV_FEEDER_ID_2(cursor.getString(71));
                    vo.setMC_11KV_FEEDER_ID_3(cursor.getString(72));
                    vo.setMC_11KV_POLE_1(cursor.getString(73));
                    vo.setMC_11KV_POLE_2(cursor.getString(74));
                    vo.setMC_11KV_POLE_3(cursor.getString(75));

                    vo.setMC_LT_FEEDER_ID_1(cursor.getString(76));
                    vo.setMC_LT_FEEDER_ID_2(cursor.getString(77));
                    vo.setMC_LT_FEEDER_ID_3(cursor.getString(78));
                    vo.setMC_LT_POLE_1(cursor.getString(79));
                    vo.setMC_LT_POLE_2(cursor.getString(80));
                    vo.setMC_LT_POLE_3(cursor.getString(81));

                    vo.setNO_OF33KV_CKTS(cursor.getString(82));
                    vo.setNO_OF11KV_CKTS(cursor.getString(83));
                    vo.setNO_OF_LT_CKTS(cursor.getString(84));
                    vo.setNUM_OTHER_CABLES(cursor.getString(85));

                    // CROSSING NEW
                    vo.setCROSS_LT_CKT(cursor.getString(86));
                    vo.setCROSS_6_3KV_ID(cursor.getString(87));
                    vo.setCROSS_11KV_ID(cursor.getString(88));
                    vo.setCROSS_33KV_ID(cursor.getString(89));
                    vo.setCROSS_EHT(cursor.getString(90));
                    vo.setCROSS_COMMUN(cursor.getString(91));
                    vo.setCROSS_REMARKS(cursor.getString(92));

                    // JHC PHASE 1
                    vo.setPOLE_SURVEY_TYPE(cursor.getString(93));
                    vo.setLINE_SURVEY_TYPE(cursor.getString(94));
                    vo.setDTR_SURVEY_TYPE(cursor.getString(95));
                    vo.setLT_SURVEY_TYPE(cursor.getString(96));

                    // DTR AVAILABLE
                    vo.setDTR_AVAILABLE(cursor.getString(97));
                    vo.setPOLE_CONCRETE(cursor.getString(98));

                    vo.setCURRENT_LAT(cursor.getString(99));
                    vo.setCURRENT_LONG(cursor.getString(100));

                    // MULTI CKT NOT IN LIST
                    vo.setMC_33KV_NOT_IN_LIST_1(cursor.getString(101));
                    vo.setMC_33KV_NOT_IN_LIST_2(cursor.getString(102));
                    vo.setMC_33KV_NOT_IN_LIST_3(cursor.getString(103));
                    vo.setMC_11KV_NOT_IN_LIST_1(cursor.getString(104));
                    vo.setMC_11KV_NOT_IN_LIST_2(cursor.getString(105));
                    vo.setMC_11KV_NOT_IN_LIST_3(cursor.getString(106));
                    vo.setMC_LT_NOT_IN_LIST_1(cursor.getString(107));
                    vo.setMC_LT_NOT_IN_LIST_2(cursor.getString(108));
                    vo.setMC_LT_NOT_IN_LIST_3(cursor.getString(109));

                    // RMU
                    vo.setRMU_MAKE(cursor.getString(110));
                    vo.setRMU_SL_NO(cursor.getString(111));
                    vo.setDTR_REMARKS(cursor.getString(112));
                    vo.setID(cursor.getString(113));

                    surveyVoArrayList.add(vo);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return surveyVoArrayList;
    }


    public ArrayList<SurveyVo> getSurveyData(String strCondition) {
        String query = "";
        ArrayList<SurveyVo> surveyVoArrayList = new ArrayList<SurveyVo>();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (strCondition.equals("")) {
                query = "SELECT * FROM  ARCGIS_TABLE";
            } else {

                query = strCondition;

            }


            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {

                    SurveyVo vo = new SurveyVo();
                    //vo.setLat(cursor.getString(0));
                    vo.setFeeder_id(cursor.getString(0));
                    vo.setPole_no(cursor.getInt(1));
                    vo.setFrom_pole(cursor.getInt(2));
                    vo.setPole_type(cursor.getString(3));
                    vo.setPole_height(cursor.getString(4));
                    // vo.setNoof_circuits(cursor.getString(5));
                    vo.setPole_support(cursor.getString(5));
                    vo.setTapping_point(cursor.getString(6));
                    vo.setConductor_size(cursor.getString(7));
                    vo.setPole_configuration(cursor.getString(8));
                    vo.setPinpoint(cursor.getString(9));
                    vo.setPole_paintingcode(cursor.getString(10));
                    vo.setLat(cursor.getString(11));
                    vo.setLan(cursor.getString(12));
                    vo.setPastSurveyFlag(cursor.getString(13));
                    vo.setIsMultiCircuit(cursor.getString(14));
                    vo.setCircuitType(cursor.getString(15));
                    vo.setIsEndPole(cursor.getString(16));
                    vo.setLineType(cursor.getString(17));
                    vo.setNoofCircuits(cursor.getString(18));
                    vo.setSs_name(cursor.getString(19));
                    vo.setPoleStructure(cursor.getString(20));
                    vo.setDiffVoltage(cursor.getString(21));
                    vo.setSsId(cursor.getString(22));
                    vo.setStjoint_rmu(cursor.getString(23));
                    vo.setRmu_type(cursor.getString(24));
                    vo.setRemarks(cursor.getString(25));
                    vo.setTappingLocation(cursor.getString(26));
                    vo.setNoofTappings(cursor.getString(27));

                    vo.setDtr_code(cursor.getString(28));
                    vo.setDtr_phase(cursor.getString(29));
                    vo.setDtr_scno(cursor.getString(30));
                    vo.setDtr_capacity(cursor.getString(31));
                    vo.setDtr_make(cursor.getString(32));
                    vo.setDtr_structure(cursor.getString(33));
                    vo.setNo_lt_ckt(cursor.getString(34));
                    vo.setDtr_ab_switch(cursor.getString(35));
                    vo.setVoltage_level(cursor.getString(36));
                    vo.setDtr_location_name(cursor.getString(37));
                    vo.setDtr_no_of_circuit(cursor.getString(38));
                    vo.setHt_service_number(cursor.getString(39));
                    vo.setPc_line_capacitor(cursor.getString(40));
                    vo.setPc_auto_reclosure(cursor.getString(41));
                    vo.setType_of_load(cursor.getString(42));
                    vo.setDtr_location_type(cursor.getString(43));
                    vo.setIs_ltckt_avalable(cursor.getString(44));
                    vo.setNo_of_ab_switches(cursor.getString(45));
                    vo.setWoId(cursor.getString(46));
                    vo.setIsFeederType(cursor.getString(47));
                    vo.setSurvaytype(cursor.getString(48));

                    vo.setDEFFECTS_AVAILABLE(cursor.getString(49));
                    vo.setPOLE_DEFECTS(cursor.getString(50));
                    vo.setCONDUCTOR_DEFECTS(cursor.getString(51));
                    vo.setAB_SWITCH_DEFECTS(cursor.getString(52));
                    vo.setINSULATOR_DEFECTS(cursor.getString(53));
                    vo.setCROSSARM_DEFECTS(cursor.getString(54));
                    vo.setTOPCLEAT_DEFECTS(cursor.getString(55));
                    vo.setSTRUT_DEFECTS(cursor.getString(56));
                    vo.setSTRAY_DEFECTS(cursor.getString(57));
                    vo.setHTLT_CLEARANCE(cursor.getString(58));
                    vo.setINTERMEDIATE_POLE(cursor.getString(59));
                    vo.setRESTRINGING(cursor.getString(60));
                    vo.setTREE_CLEARANCE(cursor.getString(61));
                    vo.setOTHER_DEFFECTS(cursor.getString(62));
                    vo.setCROSSING_TYPE(cursor.getString(63));

                    //MULTI CKT
                    vo.setMC_33KV_FEEDER_ID_1(cursor.getString(64));
                    vo.setMC_33KV_FEEDER_ID_2(cursor.getString(65));
                    vo.setMC_33KV_FEEDER_ID_3(cursor.getString(66));
                    vo.setMC_33KV_POLE_1(cursor.getString(67));
                    vo.setMC_33KV_POLE_2(cursor.getString(68));
                    vo.setMC_33KV_POLE_3(cursor.getString(69));

                    vo.setMC_11KV_FEEDER_ID_1(cursor.getString(70));
                    vo.setMC_11KV_FEEDER_ID_2(cursor.getString(71));
                    vo.setMC_11KV_FEEDER_ID_3(cursor.getString(72));
                    vo.setMC_11KV_POLE_1(cursor.getString(73));
                    vo.setMC_11KV_POLE_2(cursor.getString(74));
                    vo.setMC_11KV_POLE_3(cursor.getString(75));

                    vo.setMC_LT_FEEDER_ID_1(cursor.getString(76));
                    vo.setMC_LT_FEEDER_ID_2(cursor.getString(77));
                    vo.setMC_LT_FEEDER_ID_3(cursor.getString(78));
                    vo.setMC_LT_POLE_1(cursor.getString(79));
                    vo.setMC_LT_POLE_2(cursor.getString(80));
                    vo.setMC_LT_POLE_3(cursor.getString(81));

                    vo.setNO_OF33KV_CKTS(cursor.getString(82));
                    vo.setNO_OF11KV_CKTS(cursor.getString(83));
                    vo.setNO_OF_LT_CKTS(cursor.getString(84));
                    vo.setNUM_OTHER_CABLES(cursor.getString(85));

                    //CROSSING NEW
                    vo.setCROSS_LT_CKT(cursor.getString(86));
                    vo.setCROSS_6_3KV_ID(cursor.getString(87));
                    vo.setCROSS_11KV_ID(cursor.getString(88));
                    vo.setCROSS_33KV_ID(cursor.getString(89));
                    vo.setCROSS_EHT(cursor.getString(90));
                    vo.setCROSS_COMMUN(cursor.getString(91));
                    vo.setCROSS_REMARKS(cursor.getString(92));

                    //JHC PHASE 1
                    vo.setPOLE_SURVEY_TYPE(cursor.getString(93));
                    vo.setLINE_SURVEY_TYPE(cursor.getString(94));
                    vo.setDTR_SURVEY_TYPE(cursor.getString(95));
                    vo.setLT_SURVEY_TYPE(cursor.getString(96));

                    //DTR AVAILABLE
                    vo.setDTR_AVAILABLE(cursor.getString(97));
                    vo.setPOLE_CONCRETE(cursor.getString(98));

                    vo.setCURRENT_LAT(cursor.getString(99));
                    vo.setCURRENT_LONG(cursor.getString(100));


                    //MULTI CKT NOT IN LIST
                    vo.setMC_33KV_NOT_IN_LIST_1(cursor.getString(101));
                    vo.setMC_33KV_NOT_IN_LIST_2(cursor.getString(102));
                    vo.setMC_33KV_NOT_IN_LIST_3(cursor.getString(103));
                    vo.setMC_11KV_NOT_IN_LIST_1(cursor.getString(104));
                    vo.setMC_11KV_NOT_IN_LIST_2(cursor.getString(105));
                    vo.setMC_11KV_NOT_IN_LIST_3(cursor.getString(106));
                    vo.setMC_LT_NOT_IN_LIST_1(cursor.getString(107));
                    vo.setMC_LT_NOT_IN_LIST_2(cursor.getString(108));
                    vo.setMC_LT_NOT_IN_LIST_3(cursor.getString(109));

                    //RMU
                    vo.setRMU_MAKE(cursor.getString(110));
                    vo.setRMU_SL_NO(cursor.getString(111));
                    vo.setDTR_REMARKS(cursor.getString(112));
                    vo.setID(cursor.getString(113));
                    vo.setGPS_ACCURACY(cursor.getString(114));
                    vo.setDEVICE_ID(cursor.getString(115));

                    //MODIFICATIONS
                    vo.setPACKAGE_NO(cursor.getString(116));
                    vo.setDT_VOLATAGE_RATIO(cursor.getString(117));
                    vo.setDT_MANF_DT(cursor.getString(118));
                    vo.setDT_INSTL_DT(cursor.getString(119));
                    vo.setRDSS_SCHEME_TYPE(cursor.getString(120));
                    vo.setCIRCUIT_TYPE(cursor.getString(121));
                    vo.setADDRESS_1(cursor.getString(122));
                    vo.setADDRESS_2(cursor.getString(123));
                    vo.setBlock(cursor.getString(124));
                    vo.setVillage(cursor.getString(125));
                    vo.setTown(cursor.getString(126));
                    vo.setState(cursor.getString(127));


                    surveyVoArrayList.add(vo);
                } while (cursor.moveToNext());
            }
            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }

        return surveyVoArrayList;
    }

    public ArrayList<SurveyVo> getSurveyDataByDistance(double currentLat, double currentLon) {
        ArrayList<SurveyVo> surveyVoArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM ARCGIS_TABLE WHERE LAT IS NOT NULL AND LAN IS NOT NULL";
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    String latStr = cursor.getString(cursor.getColumnIndexOrThrow("LAT"));
                    String lonStr = cursor.getString(cursor.getColumnIndexOrThrow("LAN"));

                    if (latStr != null && lonStr != null && !latStr.isEmpty() && !lonStr.isEmpty()) {
                        double distance = getDistanceBetweenPoles(latStr, lonStr, String.valueOf(currentLat), String.valueOf(currentLon));
                        System.out.println("DISTANCE_DBDBDBDBDBD-->" + distance);

                        if (distance <= 500) {
                            SurveyVo vo = new SurveyVo();

                            vo.setFeeder_id(cursor.getString(0));
                            vo.setPole_no(cursor.getInt(1));
                            vo.setFrom_pole(cursor.getInt(2));
                            vo.setPole_type(cursor.getString(3));
                            vo.setPole_height(cursor.getString(4));
                            vo.setPole_support(cursor.getString(5));
                            vo.setTapping_point(cursor.getString(6));
                            vo.setConductor_size(cursor.getString(7));
                            vo.setPole_configuration(cursor.getString(8));
                            vo.setPinpoint(cursor.getString(9));
                            vo.setPole_paintingcode(cursor.getString(10));
                            vo.setLat(cursor.getString(11));
                            vo.setLan(cursor.getString(12));
                            vo.setPastSurveyFlag(cursor.getString(13));
                            vo.setIsMultiCircuit(cursor.getString(14));
                            vo.setCircuitType(cursor.getString(15));
                            vo.setIsEndPole(cursor.getString(16));
                            vo.setLineType(cursor.getString(17));
                            vo.setNoofCircuits(cursor.getString(18));
                            vo.setSs_name(cursor.getString(19));
                            vo.setPoleStructure(cursor.getString(20));
                            vo.setDiffVoltage(cursor.getString(21));
                            vo.setSsId(cursor.getString(22));
                            vo.setStjoint_rmu(cursor.getString(23));
                            vo.setRmu_type(cursor.getString(24));
                            vo.setRemarks(cursor.getString(25));
                            vo.setTappingLocation(cursor.getString(26));
                            vo.setNoofTappings(cursor.getString(27));

                            vo.setDtr_code(cursor.getString(28));
                            vo.setDtr_phase(cursor.getString(29));
                            vo.setDtr_scno(cursor.getString(30));
                            vo.setDtr_capacity(cursor.getString(31));
                            vo.setDtr_make(cursor.getString(32));
                            vo.setDtr_structure(cursor.getString(33));
                            vo.setNo_lt_ckt(cursor.getString(34));
                            vo.setDtr_ab_switch(cursor.getString(35));
                            vo.setVoltage_level(cursor.getString(36));
                            vo.setDtr_location_name(cursor.getString(37));
                            vo.setDtr_no_of_circuit(cursor.getString(38));
                            vo.setHt_service_number(cursor.getString(39));
                            vo.setPc_line_capacitor(cursor.getString(40));
                            vo.setPc_auto_reclosure(cursor.getString(41));
                            vo.setType_of_load(cursor.getString(42));
                            vo.setDtr_location_type(cursor.getString(43));
                            vo.setIs_ltckt_avalable(cursor.getString(44));
                            vo.setNo_of_ab_switches(cursor.getString(45));
                            vo.setWoId(cursor.getString(46));
                            vo.setIsFeederType(cursor.getString(47));
                            vo.setSurvaytype(cursor.getString(48));

                            vo.setDEFFECTS_AVAILABLE(cursor.getString(49));
                            vo.setPOLE_DEFECTS(cursor.getString(50));
                            vo.setCONDUCTOR_DEFECTS(cursor.getString(51));
                            vo.setAB_SWITCH_DEFECTS(cursor.getString(52));
                            vo.setINSULATOR_DEFECTS(cursor.getString(53));
                            vo.setCROSSARM_DEFECTS(cursor.getString(54));
                            vo.setTOPCLEAT_DEFECTS(cursor.getString(55));
                            vo.setSTRUT_DEFECTS(cursor.getString(56));
                            vo.setSTRAY_DEFECTS(cursor.getString(57));
                            vo.setHTLT_CLEARANCE(cursor.getString(58));
                            vo.setINTERMEDIATE_POLE(cursor.getString(59));
                            vo.setRESTRINGING(cursor.getString(60));
                            vo.setTREE_CLEARANCE(cursor.getString(61));
                            vo.setOTHER_DEFFECTS(cursor.getString(62));
                            vo.setCROSSING_TYPE(cursor.getString(63));

                            // MULTI CKT
                            vo.setMC_33KV_FEEDER_ID_1(cursor.getString(64));
                            vo.setMC_33KV_FEEDER_ID_2(cursor.getString(65));
                            vo.setMC_33KV_FEEDER_ID_3(cursor.getString(66));
                            vo.setMC_33KV_POLE_1(cursor.getString(67));
                            vo.setMC_33KV_POLE_2(cursor.getString(68));
                            vo.setMC_33KV_POLE_3(cursor.getString(69));

                            vo.setMC_11KV_FEEDER_ID_1(cursor.getString(70));
                            vo.setMC_11KV_FEEDER_ID_2(cursor.getString(71));
                            vo.setMC_11KV_FEEDER_ID_3(cursor.getString(72));
                            vo.setMC_11KV_POLE_1(cursor.getString(73));
                            vo.setMC_11KV_POLE_2(cursor.getString(74));
                            vo.setMC_11KV_POLE_3(cursor.getString(75));

                            vo.setMC_LT_FEEDER_ID_1(cursor.getString(76));
                            vo.setMC_LT_FEEDER_ID_2(cursor.getString(77));
                            vo.setMC_LT_FEEDER_ID_3(cursor.getString(78));
                            vo.setMC_LT_POLE_1(cursor.getString(79));
                            vo.setMC_LT_POLE_2(cursor.getString(80));
                            vo.setMC_LT_POLE_3(cursor.getString(81));

                            vo.setNO_OF33KV_CKTS(cursor.getString(82));
                            vo.setNO_OF11KV_CKTS(cursor.getString(83));
                            vo.setNO_OF_LT_CKTS(cursor.getString(84));
                            vo.setNUM_OTHER_CABLES(cursor.getString(85));

                            // CROSSING NEW
                            vo.setCROSS_LT_CKT(cursor.getString(86));
                            vo.setCROSS_6_3KV_ID(cursor.getString(87));
                            vo.setCROSS_11KV_ID(cursor.getString(88));
                            vo.setCROSS_33KV_ID(cursor.getString(89));
                            vo.setCROSS_EHT(cursor.getString(90));
                            vo.setCROSS_COMMUN(cursor.getString(91));
                            vo.setCROSS_REMARKS(cursor.getString(92));

                            // JHC PHASE 1
                            vo.setPOLE_SURVEY_TYPE(cursor.getString(93));
                            vo.setLINE_SURVEY_TYPE(cursor.getString(94));
                            vo.setDTR_SURVEY_TYPE(cursor.getString(95));
                            vo.setLT_SURVEY_TYPE(cursor.getString(96));

                            // DTR AVAILABLE
                            vo.setDTR_AVAILABLE(cursor.getString(97));
                            vo.setPOLE_CONCRETE(cursor.getString(98));

                            vo.setCURRENT_LAT(cursor.getString(99));
                            vo.setCURRENT_LONG(cursor.getString(100));

                            // MULTI CKT NOT IN LIST
                            vo.setMC_33KV_NOT_IN_LIST_1(cursor.getString(101));
                            vo.setMC_33KV_NOT_IN_LIST_2(cursor.getString(102));
                            vo.setMC_33KV_NOT_IN_LIST_3(cursor.getString(103));
                            vo.setMC_11KV_NOT_IN_LIST_1(cursor.getString(104));
                            vo.setMC_11KV_NOT_IN_LIST_2(cursor.getString(105));
                            vo.setMC_11KV_NOT_IN_LIST_3(cursor.getString(106));
                            vo.setMC_LT_NOT_IN_LIST_1(cursor.getString(107));
                            vo.setMC_LT_NOT_IN_LIST_2(cursor.getString(108));
                            vo.setMC_LT_NOT_IN_LIST_3(cursor.getString(109));

                            // RMU
                            vo.setRMU_MAKE(cursor.getString(110));
                            vo.setRMU_SL_NO(cursor.getString(111));
                            vo.setDTR_REMARKS(cursor.getString(112));
                            vo.setID(cursor.getString(112));

                            surveyVoArrayList.add(vo);
                        }
                    }
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return surveyVoArrayList;
    }

    public ArrayList<SurveyVo> getSurveyDataByDistance(double currentLat, double currentLon, String feeder_id) {
        ArrayList<SurveyVo> surveyVoArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM ARCGIS_TABLE WHERE LAT IS NOT NULL AND LAN IS NOT NULL AND FEEDER_ID =?";
            cursor = db.rawQuery(query, new String[]{feeder_id});

            if (cursor.moveToFirst()) {
                do {
                    String latStr = cursor.getString(cursor.getColumnIndexOrThrow("LAT"));
                    String lonStr = cursor.getString(cursor.getColumnIndexOrThrow("LAN"));

                    if (latStr != null && lonStr != null && !latStr.isEmpty() && !lonStr.isEmpty()) {
                        double distance = getDistanceBetweenPoles(latStr, lonStr, String.valueOf(currentLat), String.valueOf(currentLon));
                        System.out.println("DISTANCE_DBDBDBDBDBD-->" + distance);

                        if (distance <= 500) {
                            SurveyVo vo = new SurveyVo();

                            vo.setFeeder_id(cursor.getString(0));
                            vo.setPole_no(cursor.getInt(1));
                            vo.setFrom_pole(cursor.getInt(2));
                            vo.setPole_type(cursor.getString(3));
                            vo.setPole_height(cursor.getString(4));
                            vo.setPole_support(cursor.getString(5));
                            vo.setTapping_point(cursor.getString(6));
                            vo.setConductor_size(cursor.getString(7));
                            vo.setPole_configuration(cursor.getString(8));
                            vo.setPinpoint(cursor.getString(9));
                            vo.setPole_paintingcode(cursor.getString(10));
                            vo.setLat(cursor.getString(11));
                            vo.setLan(cursor.getString(12));
                            vo.setPastSurveyFlag(cursor.getString(13));
                            vo.setIsMultiCircuit(cursor.getString(14));
                            vo.setCircuitType(cursor.getString(15));
                            vo.setIsEndPole(cursor.getString(16));
                            vo.setLineType(cursor.getString(17));
                            vo.setNoofCircuits(cursor.getString(18));
                            vo.setSs_name(cursor.getString(19));
                            vo.setPoleStructure(cursor.getString(20));
                            vo.setDiffVoltage(cursor.getString(21));
                            vo.setSsId(cursor.getString(22));
                            vo.setStjoint_rmu(cursor.getString(23));
                            vo.setRmu_type(cursor.getString(24));
                            vo.setRemarks(cursor.getString(25));
                            vo.setTappingLocation(cursor.getString(26));
                            vo.setNoofTappings(cursor.getString(27));

                            vo.setDtr_code(cursor.getString(28));
                            vo.setDtr_phase(cursor.getString(29));
                            vo.setDtr_scno(cursor.getString(30));
                            vo.setDtr_capacity(cursor.getString(31));
                            vo.setDtr_make(cursor.getString(32));
                            vo.setDtr_structure(cursor.getString(33));
                            vo.setNo_lt_ckt(cursor.getString(34));
                            vo.setDtr_ab_switch(cursor.getString(35));
                            vo.setVoltage_level(cursor.getString(36));
                            vo.setDtr_location_name(cursor.getString(37));
                            vo.setDtr_no_of_circuit(cursor.getString(38));
                            vo.setHt_service_number(cursor.getString(39));
                            vo.setPc_line_capacitor(cursor.getString(40));
                            vo.setPc_auto_reclosure(cursor.getString(41));
                            vo.setType_of_load(cursor.getString(42));
                            vo.setDtr_location_type(cursor.getString(43));
                            vo.setIs_ltckt_avalable(cursor.getString(44));
                            vo.setNo_of_ab_switches(cursor.getString(45));
                            vo.setWoId(cursor.getString(46));
                            vo.setIsFeederType(cursor.getString(47));
                            vo.setSurvaytype(cursor.getString(48));

                            vo.setDEFFECTS_AVAILABLE(cursor.getString(49));
                            vo.setPOLE_DEFECTS(cursor.getString(50));
                            vo.setCONDUCTOR_DEFECTS(cursor.getString(51));
                            vo.setAB_SWITCH_DEFECTS(cursor.getString(52));
                            vo.setINSULATOR_DEFECTS(cursor.getString(53));
                            vo.setCROSSARM_DEFECTS(cursor.getString(54));
                            vo.setTOPCLEAT_DEFECTS(cursor.getString(55));
                            vo.setSTRUT_DEFECTS(cursor.getString(56));
                            vo.setSTRAY_DEFECTS(cursor.getString(57));
                            vo.setHTLT_CLEARANCE(cursor.getString(58));
                            vo.setINTERMEDIATE_POLE(cursor.getString(59));
                            vo.setRESTRINGING(cursor.getString(60));
                            vo.setTREE_CLEARANCE(cursor.getString(61));
                            vo.setOTHER_DEFFECTS(cursor.getString(62));
                            vo.setCROSSING_TYPE(cursor.getString(63));

                            // MULTI CKT
                            vo.setMC_33KV_FEEDER_ID_1(cursor.getString(64));
                            vo.setMC_33KV_FEEDER_ID_2(cursor.getString(65));
                            vo.setMC_33KV_FEEDER_ID_3(cursor.getString(66));
                            vo.setMC_33KV_POLE_1(cursor.getString(67));
                            vo.setMC_33KV_POLE_2(cursor.getString(68));
                            vo.setMC_33KV_POLE_3(cursor.getString(69));

                            vo.setMC_11KV_FEEDER_ID_1(cursor.getString(70));
                            vo.setMC_11KV_FEEDER_ID_2(cursor.getString(71));
                            vo.setMC_11KV_FEEDER_ID_3(cursor.getString(72));
                            vo.setMC_11KV_POLE_1(cursor.getString(73));
                            vo.setMC_11KV_POLE_2(cursor.getString(74));
                            vo.setMC_11KV_POLE_3(cursor.getString(75));

                            vo.setMC_LT_FEEDER_ID_1(cursor.getString(76));
                            vo.setMC_LT_FEEDER_ID_2(cursor.getString(77));
                            vo.setMC_LT_FEEDER_ID_3(cursor.getString(78));
                            vo.setMC_LT_POLE_1(cursor.getString(79));
                            vo.setMC_LT_POLE_2(cursor.getString(80));
                            vo.setMC_LT_POLE_3(cursor.getString(81));

                            vo.setNO_OF33KV_CKTS(cursor.getString(82));
                            vo.setNO_OF11KV_CKTS(cursor.getString(83));
                            vo.setNO_OF_LT_CKTS(cursor.getString(84));
                            vo.setNUM_OTHER_CABLES(cursor.getString(85));

                            // CROSSING NEW
                            vo.setCROSS_LT_CKT(cursor.getString(86));
                            vo.setCROSS_6_3KV_ID(cursor.getString(87));
                            vo.setCROSS_11KV_ID(cursor.getString(88));
                            vo.setCROSS_33KV_ID(cursor.getString(89));
                            vo.setCROSS_EHT(cursor.getString(90));
                            vo.setCROSS_COMMUN(cursor.getString(91));
                            vo.setCROSS_REMARKS(cursor.getString(92));

                            // JHC PHASE 1
                            vo.setPOLE_SURVEY_TYPE(cursor.getString(93));
                            vo.setLINE_SURVEY_TYPE(cursor.getString(94));
                            vo.setDTR_SURVEY_TYPE(cursor.getString(95));
                            vo.setLT_SURVEY_TYPE(cursor.getString(96));

                            // DTR AVAILABLE
                            vo.setDTR_AVAILABLE(cursor.getString(97));
                            vo.setPOLE_CONCRETE(cursor.getString(98));

                            vo.setCURRENT_LAT(cursor.getString(99));
                            vo.setCURRENT_LONG(cursor.getString(100));

                            // MULTI CKT NOT IN LIST
                            vo.setMC_33KV_NOT_IN_LIST_1(cursor.getString(101));
                            vo.setMC_33KV_NOT_IN_LIST_2(cursor.getString(102));
                            vo.setMC_33KV_NOT_IN_LIST_3(cursor.getString(103));
                            vo.setMC_11KV_NOT_IN_LIST_1(cursor.getString(104));
                            vo.setMC_11KV_NOT_IN_LIST_2(cursor.getString(105));
                            vo.setMC_11KV_NOT_IN_LIST_3(cursor.getString(106));
                            vo.setMC_LT_NOT_IN_LIST_1(cursor.getString(107));
                            vo.setMC_LT_NOT_IN_LIST_2(cursor.getString(108));
                            vo.setMC_LT_NOT_IN_LIST_3(cursor.getString(109));

                            // RMU
                            vo.setRMU_MAKE(cursor.getString(110));
                            vo.setRMU_SL_NO(cursor.getString(111));
                            vo.setDTR_REMARKS(cursor.getString(112));
                            vo.setID(cursor.getString(113));

                            surveyVoArrayList.add(vo);
                        }
                    }
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return surveyVoArrayList;
    }


    private double getDistanceBetweenPoles(String polLat, String polLon, String cLat, String cLan) {
        System.out.println("CURRENT LOCATION LAT DB : " + cLan);
        System.out.println("CURRENT LOCATION LON DB: " + cLat);
        System.out.println("POLE LOCATION LAT DB: " + polLat);
        System.out.println("POLE LOCATION LON DB: " + polLon);
        Location currentStartPoint = new Location("locationC");
        currentStartPoint.setLatitude(Double.parseDouble(cLan));
        currentStartPoint.setLongitude(Double.parseDouble(cLat));

        Location currentEndPoint = new Location("locationC");
        currentEndPoint.setLatitude(Double.parseDouble(polLat));
        currentEndPoint.setLongitude(Double.parseDouble(polLon));
        double currentDistance = currentStartPoint.distanceTo(currentEndPoint);
        System.out.println("DISTANCE_DB: " + currentDistance);
        return currentDistance;

    }


    public boolean addImageData(SurveyVo surveyDataVo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put(FEEDER_ID, surveyDataVo.getFeeder_id());
            values.put(POLE_NO, surveyDataVo.getPole_no());
            values.put(IMAGE_1, surveyDataVo.getIMAGE_1());
            values.put(IMAGE_2, surveyDataVo.getIMAGE_2());
            values.put(IMAGE_3, surveyDataVo.getIMAGE_3());


        } catch (Exception e) {
            e.printStackTrace();
            //  Log.v("SQLLLLLL==>",""+e.getMessage());
        }
        long a = db.insert(IMAGE_11KV_TABLE, null, values);
        if (a == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }

    public ArrayList<SurveyVo> getImageData(String strCondition) {
        String query = "";
        ArrayList<SurveyVo> surveyVoArrayList = new ArrayList<SurveyVo>();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (strCondition.equals("")) {
                query = "SELECT * FROM  IMAGE_11KV_TABLE";
            } else {

                query = strCondition;

            }


            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {

                    SurveyVo vo = new SurveyVo();
                    //vo.setLat(cursor.getString(0));
                    vo.setFeeder_id(cursor.getString(0));
                    vo.setPole_no(cursor.getInt(1));
                    vo.setIMAGE_1(cursor.getString(2));
                    vo.setIMAGE_2(cursor.getString(3));
                    vo.setIMAGE_3(cursor.getString(4));

                    surveyVoArrayList.add(vo);
                } while (cursor.moveToNext());
            }
            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }

        return surveyVoArrayList;
    }


    public boolean addSurveyDatarepeat(SurveyVo surveyVo) {
        boolean status = false;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(FEEDER_ID, surveyVo.getFeeder_id());
            values.put(POLE_NO, surveyVo.getPole_no());
            values.put(FROM_POLE, surveyVo.getFrom_pole());
            values.put(POLE_TYPE, surveyVo.getPole_type());
            values.put(POLE_HEIGHT, surveyVo.getPole_height());
            // values.put(NOOF_CIRCUITS, surveyVo.getNoof_circuits());
            values.put(POLE_SUPPORT, surveyVo.getPole_support());
            values.put(TAPPING_POINT, surveyVo.getTapping_point());
            values.put(CONDUCTOR_SIZE, surveyVo.getConductor_size());
            values.put(POLE_CONFIGURATION, surveyVo.getPole_configuration());
            values.put(PINPOINT, surveyVo.getPinpoint());
            values.put(POLE_PAINTINGCODE, surveyVo.getPole_paintingcode());
            values.put(LAT, surveyVo.getLat());
            values.put(LAN, surveyVo.getLan());
            values.put(FLAG1, surveyVo.getPastSurveyFlag());
            values.put(NO_CKT, surveyVo.getIsMultiCircuit());
            values.put(CKT_TYPE, surveyVo.getCircuitType());
            values.put(IS_ENDPOLE, surveyVo.getIsEndPole());
            values.put(LINE_TYPE, surveyVo.getLineType());
            values.put(NOOF_CIRCUITS, surveyVo.getNoofCircuits());
            values.put(SS_NAME, surveyVo.getSs_name());
            values.put(POLE_STRUCTURE, surveyVo.getPoleStructure());
            values.put(DIFF_VOLTAGE, surveyVo.getDiffVoltage());
            values.put(SS_ID, surveyVo.getSsId());
            values.put(STJOINT_RMU, surveyVo.getStjoint_rmu());
            values.put(RMU_TYPE, surveyVo.getRmu_type());
            values.put(REMARKS, surveyVo.getRemarks());
            values.put(TAPPING_LOCNAME, surveyVo.getTappingLocation());
            values.put(TAPPING_POINTS, surveyVo.getNoofTappings());

            values.put(DTR_CODE1, surveyVo.getDtr_code());
            values.put(DTR_PHASE1, surveyVo.getDtr_phase());
            values.put(DTR_SNO1, surveyVo.getDtr_scno());
            values.put(DTR_CAPACITY1, surveyVo.getDtr_capacity());
            values.put(DTR_MAKE1, surveyVo.getDtr_make());
            values.put(DTR_STRUCTURE1, surveyVo.getDtr_structure());
            values.put(NO_LI_CKT1, surveyVo.getNo_lt_ckt());
            values.put(DTR_AB1, surveyVo.getDtr_ab_switch());
            values.put(VOLTAGE_LEVEL, surveyVo.getVoltage_level());
            values.put(DTR_LOCATION_NAME, surveyVo.getDtr_location_name());
            values.put(DTR_NO_OF_LT_CIRCUIT, surveyVo.getDtr_no_of_circuit());
            values.put(HT_SERVICE_NO, surveyVo.getHt_service_number());
            values.put(PC_LINE_CAPACITOR, surveyVo.getPc_line_capacitor());
            values.put(PC_AUTO_RECLOSURE, surveyVo.getPc_auto_reclosure());

            values.put(TYPE_OF_LOAD, surveyVo.getType_of_load());
            values.put(DTR_LOCATION_TYPE, surveyVo.getDtr_location_type());
            values.put(IS_LTCKT_AVAILABLE, surveyVo.getIs_ltckt_avalable());
            values.put(NO_OF_AB_SWITCHES, surveyVo.getNo_of_ab_switches());
            values.put(WO_ID, surveyVo.getWoId());
            values.put(IS_FEEDER_TYPE, surveyVo.getIsFeederType());
            values.put(SURVEY_TYPE, surveyVo.getSurvaytype());

            //DEFECTS AND CROSSING
            values.put(DEFFECTS_AVAILABLE, surveyVo.getDEFFECTS_AVAILABLE());
            values.put(POLE_DEFECTS, surveyVo.getPOLE_DEFECTS());
            values.put(CONDUCTOR_DEFECTS, surveyVo.getCONDUCTOR_DEFECTS());
            values.put(AB_SWITCH_DEFECTS, surveyVo.getAB_SWITCH_DEFECTS());
            values.put(INSULATOR_DEFECTS, surveyVo.getINSULATOR_DEFECTS());
            values.put(CROSSARM_DEFECTS, surveyVo.getCROSSARM_DEFECTS());
            values.put(TOPCLEAT_DEFECTS, surveyVo.getTOPCLEAT_DEFECTS());
            values.put(STRUT_DEFECTS, surveyVo.getSTRUT_DEFECTS());
            values.put(STRAY_DEFECTS, surveyVo.getSTRAY_DEFECTS());
            values.put(HTLT_CLEARANCE, surveyVo.getHTLT_CLEARANCE());
            values.put(INTERMEDIATE_POLE, surveyVo.getINTERMEDIATE_POLE());
            values.put(RESTRINGING, surveyVo.getRESTRINGING());
            values.put(TREE_CLEARANCE, surveyVo.getTREE_CLEARANCE());
            values.put(OTHER_DEFFECTS, surveyVo.getOTHER_DEFFECTS());
            values.put(CROSSING_TYPE, surveyVo.getCROSSING_TYPE());

            //JHC PHASE 1
            values.put(POLE_SURVEY_TYPE, surveyVo.getPOLE_SURVEY_TYPE());
            values.put(LINE_SURVEY_TYPE, surveyVo.getLINE_SURVEY_TYPE());
            values.put(DTR_SURVEY_TYPE, surveyVo.getDTR_SURVEY_TYPE());
            values.put(LT_SURVEY_TYPE, surveyVo.getLT_SURVEY_TYPE());

            values.put(POLE_CONCRETE, surveyVo.getPOLE_CONCRETE());


            long a = db.insert(ARCGIS_TABLE_REPEAT, null, values);
            if (a == -1) {
                db.close();
                status = false;
            } else {
                db.close();
                status = true;
            }

        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return status;
    }

    public ArrayList<SurveyVo> getSurveyDataREPEAT(String strCondition) {
        String query = "";
        ArrayList<SurveyVo> surveyVoArrayList = new ArrayList<SurveyVo>();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (strCondition.equals("")) {
                query = "SELECT * FROM  ARCGIS_TABLE_REPEAT";
            } else {

                query = strCondition;

            }


            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {

                    SurveyVo vo = new SurveyVo();
                    //vo.setLat(cursor.getString(0));
                    vo.setFeeder_id(cursor.getString(0));
                    vo.setPole_no(cursor.getInt(1));
                    vo.setFrom_pole(cursor.getInt(2));
                    vo.setPole_type(cursor.getString(3));
                    vo.setPole_height(cursor.getString(4));
                    // vo.setNoof_circuits(cursor.getString(5));
                    vo.setPole_support(cursor.getString(5));
                    vo.setTapping_point(cursor.getString(6));
                    vo.setConductor_size(cursor.getString(7));
                    vo.setPole_configuration(cursor.getString(8));
                    vo.setPinpoint(cursor.getString(9));
                    vo.setPole_paintingcode(cursor.getString(10));
                    vo.setLat(cursor.getString(11));
                    vo.setLan(cursor.getString(12));
                    vo.setPastSurveyFlag(cursor.getString(13));
                    vo.setIsMultiCircuit(cursor.getString(14));
                    vo.setCircuitType(cursor.getString(15));
                    vo.setIsEndPole(cursor.getString(16));
                    vo.setLineType(cursor.getString(17));
                    vo.setNoofCircuits(cursor.getString(18));
                    vo.setSs_name(cursor.getString(19));
                    vo.setPoleStructure(cursor.getString(20));
                    vo.setDiffVoltage(cursor.getString(21));
                    vo.setSsId(cursor.getString(22));
                    vo.setStjoint_rmu(cursor.getString(23));
                    vo.setRmu_type(cursor.getString(24));
                    vo.setRemarks(cursor.getString(25));
                    vo.setTappingLocation(cursor.getString(26));
                    vo.setNoofTappings(cursor.getString(27));

                    vo.setDtr_code(cursor.getString(28));
                    vo.setDtr_phase(cursor.getString(29));
                    vo.setDtr_scno(cursor.getString(30));
                    vo.setDtr_capacity(cursor.getString(31));
                    vo.setDtr_make(cursor.getString(32));
                    vo.setDtr_structure(cursor.getString(33));
                    vo.setNo_lt_ckt(cursor.getString(34));
                    vo.setDtr_ab_switch(cursor.getString(35));
                    vo.setVoltage_level(cursor.getString(36));
                    vo.setDtr_location_name(cursor.getString(37));
                    vo.setDtr_no_of_circuit(cursor.getString(38));
                    vo.setHt_service_number(cursor.getString(39));
                    vo.setPc_line_capacitor(cursor.getString(40));
                    vo.setPc_auto_reclosure(cursor.getString(41));
                    vo.setType_of_load(cursor.getString(42));
                    vo.setDtr_location_type(cursor.getString(43));
                    vo.setIs_ltckt_avalable(cursor.getString(44));
                    vo.setNo_of_ab_switches(cursor.getString(45));
                    vo.setWoId(cursor.getString(46));
                    vo.setIsFeederType(cursor.getString(47));
                    vo.setSurvaytype(cursor.getString(48));

                    //JHC PHASE 1
                    vo.setPOLE_SURVEY_TYPE(cursor.getString(64));
                    vo.setLINE_SURVEY_TYPE(cursor.getString(65));
                    vo.setDTR_SURVEY_TYPE(cursor.getString(66));
                    vo.setLT_SURVEY_TYPE(cursor.getString(67));

                    vo.setPOLE_CONCRETE(cursor.getString(68));


                    surveyVoArrayList.add(vo);
                } while (cursor.moveToNext());
            }
            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }

        return surveyVoArrayList;
    }

    public List<SurveyVo> getData() {
        ArrayList<SurveyVo> surveyVoArrayList = new ArrayList<SurveyVo>();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String query = "SELECT * FROM ARCGIS_TABLE ORDER BY POLE_NO";

            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {

                    SurveyVo vo = new SurveyVo();
                    //vo.setLat(cursor.getString(0));
                    vo.setFeeder_id(cursor.getString(0));
                    vo.setPole_no(cursor.getInt(1));
                    vo.setFrom_pole(cursor.getInt(2));
                    vo.setPole_type(cursor.getString(3));
                    vo.setPole_height(cursor.getString(4));
                    // vo.setNoof_circuits(cursor.getString(5));
                    vo.setPole_support(cursor.getString(5));
                    vo.setTapping_point(cursor.getString(6));
                    vo.setConductor_size(cursor.getString(7));
                    vo.setPole_configuration(cursor.getString(8));
                    vo.setPinpoint(cursor.getString(9));
                    vo.setPole_paintingcode(cursor.getString(10));
                    vo.setLat(cursor.getString(11));
                    vo.setLan(cursor.getString(12));
                    vo.setPastSurveyFlag(cursor.getString(13));
                    vo.setIsMultiCircuit(cursor.getString(14));
                    vo.setCircuitType(cursor.getString(15));
                    vo.setIsEndPole(cursor.getString(16));
                    vo.setLineType(cursor.getString(17));
                    vo.setNoofCircuits(cursor.getString(18));
                    vo.setSs_name(cursor.getString(19));
                    vo.setPoleStructure(cursor.getString(20));
                    vo.setDiffVoltage(cursor.getString(21));
                    vo.setSsId(cursor.getString(22));
                    vo.setStjoint_rmu(cursor.getString(23));
                    vo.setRmu_type(cursor.getString(24));
                    vo.setRemarks(cursor.getString(25));
                    vo.setTappingLocation(cursor.getString(26));
                    vo.setNoofTappings(cursor.getString(27));
                    vo.setDtr_code(cursor.getString(28));
                    vo.setDtr_phase(cursor.getString(29));
                    vo.setDtr_scno(cursor.getString(30));
                    vo.setDtr_capacity(cursor.getString(31));
                    vo.setDtr_make(cursor.getString(32));
                    vo.setDtr_structure(cursor.getString(33));
                    vo.setNo_lt_ckt(cursor.getString(34));
                    vo.setDtr_ab_switch(cursor.getString(35));
                    vo.setVoltage_level(cursor.getString(36));
                    vo.setDtr_location_name(cursor.getString(37));
                    vo.setDtr_no_of_circuit(cursor.getString(38));
                    vo.setHt_service_number(cursor.getString(39));
                    vo.setPc_line_capacitor(cursor.getString(40));
                    vo.setPc_auto_reclosure(cursor.getString(41));
                    vo.setType_of_load(cursor.getString(42));
                    vo.setDtr_location_type(cursor.getString(43));
                    vo.setIs_ltckt_avalable(cursor.getString(44));
                    vo.setNo_of_ab_switches(cursor.getString(45));
                    vo.setWoId(cursor.getString(46));
                    vo.setIsFeederType(cursor.getString(47));
                    vo.setSurvaytype(cursor.getString(48));
                    surveyVoArrayList.add(vo);
                } while (cursor.moveToNext());
            }
            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }

        return surveyVoArrayList;
    }

    public List<SurveyVo> getSurveyDetailsByPoleNo(int poleNo) {
        ArrayList<SurveyVo> surveyVoArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase(); // use readable for select
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM ARCGIS_TABLE WHERE POLE_NO = ? ORDER BY POLE_NO";
            cursor = db.rawQuery(query, new String[]{String.valueOf(poleNo)});

            if (cursor.moveToFirst()) {
                do {
                    SurveyVo vo = new SurveyVo();
                    vo.setPole_no(cursor.getInt(1));
                    vo.setFrom_pole(cursor.getInt(2));
                    vo.setPole_configuration(cursor.getString(8));
                    vo.setLat(cursor.getString(11));
                    vo.setLan(cursor.getString(12));
                    vo.setDtr_code(cursor.getString(28));

                    surveyVoArrayList.add(vo);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return surveyVoArrayList;
    }


    public List<SurveyVo> getOnlyLatAndLon() {
        ArrayList<SurveyVo> surveyVoArrayList = new ArrayList<SurveyVo>();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String query = "SELECT * FROM ARCGIS_TABLE ORDER BY POLE_NO";

            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {

                    SurveyVo vo = new SurveyVo();
                    //vo.setLat(cursor.getString(0));

                    vo.setPole_no(cursor.getInt(1));
                    vo.setFrom_pole(cursor.getInt(2));
                    vo.setPole_configuration(cursor.getString(8));
                    vo.setLat(cursor.getString(11));
                    vo.setLan(cursor.getString(12));
                    vo.setDtr_code(cursor.getString(28));

                    surveyVoArrayList.add(vo);
                } while (cursor.moveToNext());
            }
            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }

        return surveyVoArrayList;
    }

    public boolean addDTRMasterData(DTRMaster_vo dtrMaster_vo) {
        boolean status = false;
        SQLiteDatabase db = this.getWritableDatabase();
        try {

            ContentValues values = new ContentValues();
            values.put(DTR_FEEDER_ID, dtrMaster_vo.getDTR_FEEDER_ID());
            values.put(DTR_OFFICE_ID, dtrMaster_vo.getDTR_OFFICE_ID());
            values.put(DTR_SECTION_NAME, dtrMaster_vo.getDTR_SECTION_NAME());
            values.put(DTR_PHASE, dtrMaster_vo.getDTR_PHASE());
            values.put(DTR_CAPACITY, dtrMaster_vo.getDTR_CAPACITY());
            values.put(DTR_MAKE, dtrMaster_vo.getDTR_MAKE());
            values.put(DTR_STRUCTURE_CODE, dtrMaster_vo.getDTR_DTR_STRUCTURE_CODE());
            values.put(DTR_FEEDER_NAME, dtrMaster_vo.getDTR_FEEDER_NAME());
            values.put(DTR_SECTION_ID, dtrMaster_vo.getDTR_SECTION_ID());
            values.put(DTR_LOCATION, dtrMaster_vo.getDTR_LOCATION());
            values.put(DTR_MAKER, dtrMaster_vo.getDTR_MAKER());
            values.put(DTR_SLNO, dtrMaster_vo.getDTR_SLNO());
            values.put(WORK_ORDER, dtrMaster_vo.getWORK_ORDER());
            values.put(EST_NO, dtrMaster_vo.getEST_NO());

            long a = db.insert(DTR_MASTER_TABLE, null, values);
            if (a == -1) {
                db.close();
                status = false;
            } else {
                db.close();
                status = true;
            }
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return status;
    }

    public List<DTRMaster_vo> getDTRMasterData() {
        ArrayList<DTRMaster_vo> dtrmaster_list = new ArrayList<DTRMaster_vo>();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String query = "SELECT * FROM DTR_MASTER_TABLE";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    DTRMaster_vo Vo = new DTRMaster_vo();
                    Vo.setDTR_FEEDER_ID(cursor.getString(0));
                    Vo.setDTR_OFFICE_ID(cursor.getString(1));
                    Vo.setDTR_SECTION_NAME(cursor.getString(2));
                    Vo.setDTR_PHASE(cursor.getString(3));
                    Vo.setDTR_CAPACITY(cursor.getString(4));
                    Vo.setDTR_MAKE(cursor.getString(5));
                    Vo.setDTR_DTR_STRUCTURE_CODE(cursor.getString(6));
                    Vo.setDTR_FEEDER_NAME(cursor.getString(7));
                    Vo.setDTR_SECTION_ID(cursor.getString(8));
                    Vo.setDTR_LOCATION(cursor.getString(9));
                    Vo.setDTR_MAKER(cursor.getString(10));
                    Vo.setDTR_SLNO(cursor.getString(11));
                    Vo.setWORK_ORDER(cursor.getString(12));
                    Vo.setEST_NO(cursor.getString(13));
                    dtrmaster_list.add(Vo);
                } while (cursor.moveToNext());
            }
            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return dtrmaster_list;
    }

    public List<DTRMaster_vo> getDTRMasterData(String structureCode) {
        ArrayList<DTRMaster_vo> dtrmaster_list = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM DTR_MASTER_TABLE WHERE DTR_STRUCTURE_CODE = ?";
            cursor = db.rawQuery(query, new String[]{structureCode});

            if (cursor.moveToFirst()) {
                do {
                    DTRMaster_vo Vo = new DTRMaster_vo();
                    Vo.setDTR_FEEDER_ID(cursor.getString(0));
                    Vo.setDTR_OFFICE_ID(cursor.getString(1));
                    Vo.setDTR_SECTION_NAME(cursor.getString(2));
                    Vo.setDTR_PHASE(cursor.getString(3));
                    Vo.setDTR_CAPACITY(cursor.getString(4));
                    Vo.setDTR_MAKE(cursor.getString(5));
                    Vo.setDTR_DTR_STRUCTURE_CODE(cursor.getString(6));
                    Vo.setDTR_FEEDER_NAME(cursor.getString(7));
                    Vo.setDTR_SECTION_ID(cursor.getString(8));
                    Vo.setDTR_LOCATION(cursor.getString(9));
                    Vo.setDTR_MAKER(cursor.getString(10));
                    Vo.setDTR_SLNO(cursor.getString(11));
                    dtrmaster_list.add(Vo);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            //  Log.e("DTRMasterData", "Error fetching data: " + e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            db.close();
        }

        return dtrmaster_list;
    }

    public boolean addMakeMasterData(MakeMaster_vo dtrMaster_vo) {
        boolean status = false;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(DTR_ID, dtrMaster_vo.getID());
            values.put(DTR_MAKE_M, dtrMaster_vo.getDTR_MAKE());
            values.put(DTR_RECORD_STATUS, dtrMaster_vo.getDTR_RECORD_STATUS());

            long a = db.insert(MAKE_MASTER_TABLE, null, values);
            if (a == -1) {
                db.close();
                status = false;
            } else {
                db.close();
                status = true;
            }
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return status;
    }

    public List<MakeMaster_vo> getMakeMasterData() {
        ArrayList<MakeMaster_vo> dtrmaster_list = new ArrayList<MakeMaster_vo>();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String query = "SELECT * FROM DTR_MASTER_TABLE";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    MakeMaster_vo Vo = new MakeMaster_vo();
                    Vo.setID(cursor.getString(0));
                    Vo.setDTR_MAKE(cursor.getString(1));
                    Vo.setDTR_RECORD_STATUS(cursor.getString(2));
                    dtrmaster_list.add(Vo);
                } while (cursor.moveToNext());
            }
            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return dtrmaster_list;
    }

    public ArrayList<String> getAll_DtrStructureSubStationMaster() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> dtr_structuctre_master_vo = new ArrayList<String>();
        String selectQuery = "SELECT  DISTINCT DTR_SECTION_NAME FROM DTR_MASTER_TABLE";
        // String selectQuery = "SELECT DISTINCT  DTR_SECTION_ID|| '-' ||DTR_SECTION_NAME FROM EHTMASTER_TABLE WHERE DTR_MASTER_TABLE IS NOT NULL";

        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    dtr_structuctre_master_vo.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }
        } finally {
            db.close();
        }
        return dtr_structuctre_master_vo;
    }

    public ArrayList<String> getAll_DtrStructureCodeMaster(String condition) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> dtr_structuctre_master_vo = new ArrayList<String>();
        String selectQuery = "SELECT DISTINCT DTR_STRUCTURE_CODE FROM DTR_MASTER_TABLE";

        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    dtr_structuctre_master_vo.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }
        } finally {
            db.close();
        }
        return dtr_structuctre_master_vo;
    }


    public boolean addFeederServiceMasterData(FeederServiceMasterResponseVo vo) {
        boolean status = false;
        SQLiteDatabase db = this.getWritableDatabase();
        try {

            ContentValues values = new ContentValues();
            values.put(FS_FEEDER_ID, vo.getFEEDER_ID());
            values.put(FS_CONSUMER_NUMBER, vo.getCONSUMER_NUMBER());
            values.put(FS_CONSUMER_NAME, vo.getCONSUMER_NAME());


            long a = db.insert(FEEDER_SERVICE_MASTER_TABLE, null, values);
            if (a == -1) {
                db.close();
                status = false;
            } else {
                db.close();
                status = true;
            }
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return status;
    }

    public List<FeederServiceMasterResponseVo> getFeederServiceMasterData() {
        ArrayList<FeederServiceMasterResponseVo> dtrmaster_list = new ArrayList<FeederServiceMasterResponseVo>();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String query = "SELECT * FROM DTR_MASTER_TABLE";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    FeederServiceMasterResponseVo Vo = new FeederServiceMasterResponseVo();
                    Vo.setFEEDER_ID(cursor.getString(0));
                    Vo.setCONSUMER_NUMBER(cursor.getString(1));
                    Vo.setCONSUMER_NAME(cursor.getString(2));
                    dtrmaster_list.add(Vo);
                } while (cursor.moveToNext());
            }
            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return dtrmaster_list;
    }

    public boolean checkValueMethod(String ht_number) {
        boolean status = false;
        SQLiteDatabase sqldb = this.getWritableDatabase();
        try {
            String Query = "Select * from " + FEEDER_SERVICE_MASTER_TABLE + " where " + FS_CONSUMER_NUMBER + " = '" + ht_number + "'";
            Cursor cursor = sqldb.rawQuery(Query, null);
            if (cursor.getCount() <= 0) {
                cursor.close();
                return false;
            } else {
                status = true;
            }
            cursor.close();
            sqldb.close();
        } catch (Exception e) {
            sqldb.close();
        } finally {
            sqldb.close();
        }
        return status;
    }

    public boolean checkValueDTRCodeMethod(String s) {
        boolean status = false;
        SQLiteDatabase sqldb = this.getWritableDatabase();
        try {
            String Query = "Select * from " + DTR_MASTER_TABLE + " where " + DTR_STRUCTURE_CODE + " = '" + s + "'";
            Cursor cursor = sqldb.rawQuery(Query, null);
            if (cursor.getCount() <= 0) {
                cursor.close();
                return false;
            } else {
                status = true;
            }
            cursor.close();
            sqldb.close();
        } catch (Exception e) {
            sqldb.close();
        } finally {
            sqldb.close();
        }
        return status;

    }

    public boolean checkValueDTRCodeLoacalSave(String s) {
        boolean status = false;
        SQLiteDatabase sqldb = this.getWritableDatabase();
        try {
            String Query = "Select * from " + ARCGIS_TABLE + " where " + DTR_CODE1 + " = '" + s + "'";
            Cursor cursor = sqldb.rawQuery(Query, null);
            if (cursor.getCount() <= 0) {
                cursor.close();
                return false;
            } else {
                status = true;
            }
            cursor.close();
            sqldb.close();
        } catch (Exception e) {
            sqldb.close();
        } finally {
            sqldb.close();
        }
        return status;

    }


    public boolean addMultiVoltageMasterData(MULTIVOLTAGEMaster_vo vo) {
        boolean status = false;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(DTR_ID, UUID.randomUUID().toString());
            values.put(VOLTAGE, vo.getVOLTAGE());
            values.put(CKT_NO, vo.getCKT_NO());
            values.put(CKT_VALUE, vo.getCKT_VALUE());
            values.put(RECORD_STATUS, vo.getRECORD_STATUS());

            long a = db.insert(MULTIVOLTAGE_MASTERS_TABLE, null, values);
            if (a == -1) {
                db.close();
                status = false;
            } else {
                db.close();
                status = true;
            }
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return status;
    }

    public List<MULTIVOLTAGEMaster_vo> getMultiVoltageMasterData() {
        ArrayList<MULTIVOLTAGEMaster_vo> dtrmaster_list = new ArrayList<MULTIVOLTAGEMaster_vo>();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String query = "SELECT * FROM MULTIVOLTAGE_MASTERS_TABLE";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    MULTIVOLTAGEMaster_vo Vo = new MULTIVOLTAGEMaster_vo();
                    Vo.setID(cursor.getString(0));
                    Vo.setVOLTAGE(cursor.getString(1));
                    Vo.setCKT_NO(cursor.getString(2));
                    Vo.setCKT_VALUE(cursor.getString(3));
                    Vo.setRECORD_STATUS(cursor.getString(4));

                    dtrmaster_list.add(Vo);
                } while (cursor.moveToNext());
            }
            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return dtrmaster_list;
    }


    //Incremental


    public boolean addIncrementFeederData(IncrementalFeederDataVo incrementalFeederDataVo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EHT_SS_CODE, incrementalFeederDataVo.getEHT_SS_CODE());
        values.put(EHT_SS_NAME, incrementalFeederDataVo.getEHT_SS_NAME());
        values.put(FEEDER_NAME_33KV, incrementalFeederDataVo.getFEEDER_NAME_33KV());
        values.put(FEEDER_CODE_33KV, incrementalFeederDataVo.getFEEDER_CODE_33KV());
        values.put(WORKORDER_ID, incrementalFeederDataVo.getWORKORDER_ID());
        values.put(STATUS, incrementalFeederDataVo.getSTATUS());
        values.put(FROM_POLE, incrementalFeederDataVo.getFROM_POLE());
        values.put(IS_FEEDER_TYPE, incrementalFeederDataVo.getIS_FEEDER_TYPE());
        long a = db.insert(INCREMENTAL_EHTMASTER_TABLE, null, values);
        if (a == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }

    public List<IncrementalFeederDataVo> getIncrementalFeerData() {
        ArrayList<IncrementalFeederDataVo> feederDataVosList = new ArrayList<IncrementalFeederDataVo>();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String query = "SELECT * FROM INCREMENTAL_EHTMASTER_TABLE";

            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {

                    IncrementalFeederDataVo incrementalFeederDataVo = new IncrementalFeederDataVo();
                    //vo.setLat(cursor.getString(0));
                    incrementalFeederDataVo.setEHT_SS_CODE(cursor.getString(0));
                    incrementalFeederDataVo.setEHT_SS_NAME(cursor.getString(1));
                    incrementalFeederDataVo.setFEEDER_NAME_33KV(cursor.getString(2));
                    incrementalFeederDataVo.setFEEDER_CODE_33KV(cursor.getString(3));
                    incrementalFeederDataVo.setWORKORDER_ID(cursor.getString(4));
                    incrementalFeederDataVo.setSTATUS(cursor.getString(5));
                    incrementalFeederDataVo.setFROM_POLE(cursor.getString(6));
                    incrementalFeederDataVo.setIS_FEEDER_TYPE(cursor.getString(7));
                    //  vo.setSECTION_CODE(cursor.getString(8));
                    feederDataVosList.add(incrementalFeederDataVo);
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return feederDataVosList;
    }


    public boolean addIncrementalSSMasterData(SSMasterVo ssMasterVo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EHT_SS_CODE, ssMasterVo.getFEEDING_SS_CODE());
        values.put(EHT_SS_NAME, ssMasterVo.getFEEDING_SS_NAME());
        values.put(FEEDER_CODE_33KV, ssMasterVo.getFEEDER_CODE_33KV());

        long a = db.insert(INCREMENTALSSMASTER_TABLE, null, values);
        if (a == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }


    public List<SSMasterVo> getIncrementalSSMasterData() {
        ArrayList<SSMasterVo> ssResponseList = new ArrayList<SSMasterVo>();

        String query = "SELECT * FROM INCREMENTALSSMASTER_TABLE";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {

                SSMasterVo ssMasterVo = new SSMasterVo();
                //vo.setLat(cursor.getString(0));
                ssMasterVo.setFEEDING_SS_CODE(cursor.getString(0));
                ssMasterVo.setFEEDING_SS_NAME(cursor.getString(1));
                ssMasterVo.setFEEDER_CODE_33KV(cursor.getString(2));

                //  vo.setSECTION_CODE(cursor.getString(8));
                ssResponseList.add(ssMasterVo);
            } while (cursor.moveToNext());
        }

        db.close();
        return ssResponseList;
    }


    public boolean addIncrementalSurveyDTRMasterData(DTRMaster_vo dtrMaster_vo) {
        boolean status = false;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(DTR_FEEDER_ID, dtrMaster_vo.getDTR_FEEDER_ID());
            values.put(DTR_OFFICE_ID, dtrMaster_vo.getDTR_OFFICE_ID());
            values.put(DTR_SECTION_NAME, dtrMaster_vo.getDTR_SECTION_NAME());
            values.put(DTR_PHASE, dtrMaster_vo.getDTR_PHASE());
            values.put(DTR_CAPACITY, dtrMaster_vo.getDTR_CAPACITY());
            values.put(DTR_MAKE, dtrMaster_vo.getDTR_MAKE());
            values.put(DTR_STRUCTURE_CODE, dtrMaster_vo.getDTR_DTR_STRUCTURE_CODE());
            values.put(DTR_FEEDER_NAME, dtrMaster_vo.getDTR_FEEDER_NAME());
            values.put(DTR_SECTION_ID, dtrMaster_vo.getDTR_SECTION_ID());
            values.put(DTR_LOCATION, dtrMaster_vo.getDTR_LOCATION());
            values.put(DTR_MAKER, dtrMaster_vo.getDTR_MAKER());
            values.put(DTR_SLNO, dtrMaster_vo.getDTR_SLNO());

            long a = db.insert(INCREMENTAL_DTR_MASTER_TABLE, null, values);
            if (a == -1) {
                db.close();
                status = false;
            } else {
                db.close();
                status = true;
            }
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return status;
    }


    public boolean addIncrementalFeederServiceMasterData(FeederServiceMasterResponseVo vo) {
        boolean status = false;
        SQLiteDatabase db = this.getWritableDatabase();
        try {

            ContentValues values = new ContentValues();
            values.put(FS_FEEDER_ID, vo.getFEEDER_ID());
            values.put(FS_CONSUMER_NUMBER, vo.getCONSUMER_NUMBER());
            values.put(FS_CONSUMER_NAME, vo.getCONSUMER_NAME());


            long a = db.insert(INCREMENTAL_FEEDER_SERVICE_MASTER_TABLE, null, values);
            if (a == -1) {
                db.close();
                status = false;
            } else {
                db.close();
                status = true;
            }
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return status;
    }


    public boolean addIncrementalMakeMasterData(MakeMaster_vo dtrMaster_vo) {
        boolean status = false;
        SQLiteDatabase db = this.getWritableDatabase();
        try {

            ContentValues values = new ContentValues();
            values.put(DTR_ID, dtrMaster_vo.getID());
            values.put(DTR_MAKE_M, dtrMaster_vo.getDTR_MAKE());
            values.put(DTR_RECORD_STATUS, dtrMaster_vo.getDTR_RECORD_STATUS());

            long a = db.insert(INCREMENTAL_MAKE_MASTER_TABLE, null, values);
            if (a == -1) {
                db.close();
                status = false;
            } else {
                db.close();
                status = true;
            }
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return status;
    }

    public boolean addIncrementalMultiVoltageMasterData(MULTIVOLTAGEMaster_vo vo) {
        boolean status = false;
        SQLiteDatabase db = this.getWritableDatabase();
        try {

            ContentValues values = new ContentValues();
            values.put(DTR_ID, UUID.randomUUID().toString());
            values.put(VOLTAGE, vo.getVOLTAGE());
            values.put(CKT_NO, vo.getCKT_NO());
            values.put(CKT_VALUE, vo.getCKT_VALUE());
            values.put(RECORD_STATUS, vo.getRECORD_STATUS());

            long a = db.insert(INCREMENTAL_MULTIVOLTAGE_MASTERS_TABLE, null, values);
            if (a == -1) {
                db.close();
                status = false;
            } else {
                db.close();
                status = true;
            }
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return status;
    }


    public boolean addIncrementalSurveyData(IncrementalSurveyVo surveyVo) {
        boolean status = false;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(FEEDER_ID, surveyVo.getFeeder_id());
            values.put(POLE_NO, surveyVo.getPole_no());
            values.put(FROM_POLE, surveyVo.getFrom_pole());
            values.put(POLE_TYPE, surveyVo.getPole_type());
            values.put(POLE_HEIGHT, surveyVo.getPole_height());
            // values.put(NOOF_CIRCUITS, surveyVo.getNoof_circuits());
            values.put(POLE_SUPPORT, surveyVo.getPole_support());
            values.put(TAPPING_POINT, surveyVo.getTapping_point());
            values.put(CONDUCTOR_SIZE, surveyVo.getConductor_size());
            values.put(POLE_CONFIGURATION, surveyVo.getPole_configuration());
            values.put(PINPOINT, surveyVo.getPinpoint());
            values.put(POLE_PAINTINGCODE, surveyVo.getPole_paintingcode());
            values.put(LAT, surveyVo.getLat());
            values.put(LAN, surveyVo.getLan());
            values.put(FLAG1, surveyVo.getPastSurveyFlag());
            values.put(NO_CKT, surveyVo.getIsMultiCircuit());
            values.put(CKT_TYPE, surveyVo.getCircuitType());
            values.put(IS_ENDPOLE, surveyVo.getIsEndPole());
            values.put(LINE_TYPE, surveyVo.getLineType());
            values.put(NOOF_CIRCUITS, surveyVo.getNoofCircuits());
            values.put(SS_NAME, surveyVo.getSs_name());
            values.put(POLE_STRUCTURE, surveyVo.getPoleStructure());
            values.put(DIFF_VOLTAGE, surveyVo.getDiffVoltage());
            values.put(SS_ID, surveyVo.getSsId());
            values.put(STJOINT_RMU, surveyVo.getStjoint_rmu());
            values.put(RMU_TYPE, surveyVo.getRmu_type());
            values.put(REMARKS, surveyVo.getRemarks());
            values.put(TAPPING_LOCNAME, surveyVo.getTappingLocation());
            values.put(TAPPING_POINTS, surveyVo.getNoofTappings());

            values.put(DTR_CODE1, surveyVo.getDtr_code());
            values.put(DTR_PHASE1, surveyVo.getDtr_phase());
            values.put(DTR_SNO1, surveyVo.getDtr_scno());
            values.put(DTR_CAPACITY1, surveyVo.getDtr_capacity());
            values.put(DTR_MAKE1, surveyVo.getDtr_make());
            values.put(DTR_STRUCTURE1, surveyVo.getDtr_structure());
            values.put(NO_LI_CKT1, surveyVo.getNo_lt_ckt());
            values.put(DTR_AB1, surveyVo.getDtr_ab_switch());
            values.put(VOLTAGE_LEVEL, surveyVo.getVoltage_level());
            values.put(DTR_LOCATION_NAME, surveyVo.getDtr_location_name());
            values.put(DTR_NO_OF_LT_CIRCUIT, surveyVo.getDtr_no_of_circuit());
            values.put(HT_SERVICE_NO, surveyVo.getHt_service_number());
            values.put(PC_LINE_CAPACITOR, surveyVo.getPc_line_capacitor());
            values.put(PC_AUTO_RECLOSURE, surveyVo.getPc_auto_reclosure());

            values.put(TYPE_OF_LOAD, surveyVo.getType_of_load());
            values.put(DTR_LOCATION_TYPE, surveyVo.getDtr_location_type());
            values.put(IS_LTCKT_AVAILABLE, surveyVo.getIs_ltckt_avalable());
            values.put(NO_OF_AB_SWITCHES, surveyVo.getNo_of_ab_switches());
            values.put(NO_OF_AB_SWITCHES, surveyVo.getNo_of_ab_switches());
            values.put(WORKORDER_ID, surveyVo.getWorkOrder());
            values.put(WO_STATUS, surveyVo.getWoStatus());
            values.put(IS_FEEDER_TYPE, surveyVo.getIsFeederType());
            values.put(SURVEY_TYPE, surveyVo.getSURVEY_TYPE());


            long a = db.insert(INCREMENTAL_ARCGIS_TABLE, null, values);
            if (a == -1) {
                db.close();
                status = false;
            } else {
                db.close();
                status = true;
            }

        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return status;
    }


    public ArrayList<IncrementalSurveyVo> getIncrementalSurveyData(String strCondition) {
        String query = "";
        ArrayList<IncrementalSurveyVo> incrementalSurveyVosList = new ArrayList<IncrementalSurveyVo>();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (strCondition.equals("")) {
                query = "SELECT * FROM INCREMENTAL_ARCGIS_TABLE ORDER BY POLE_NO";
            } else {

                query = strCondition;

            }


            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {

                    IncrementalSurveyVo incrementalSurveyVo = new IncrementalSurveyVo();
                    //vo.setLat(cursor.getString(0));
                    incrementalSurveyVo.setFeeder_id(cursor.getString(0));
                    incrementalSurveyVo.setPole_no(cursor.getInt(1));
                    incrementalSurveyVo.setFrom_pole(cursor.getInt(2));
                    incrementalSurveyVo.setPole_type(cursor.getString(3));
                    incrementalSurveyVo.setPole_height(cursor.getString(4));
                    // vo.setNoof_circuits(cursor.getString(5));
                    incrementalSurveyVo.setPole_support(cursor.getString(5));
                    incrementalSurveyVo.setTapping_point(cursor.getString(6));
                    incrementalSurveyVo.setConductor_size(cursor.getString(7));
                    incrementalSurveyVo.setPole_configuration(cursor.getString(8));
                    incrementalSurveyVo.setPinpoint(cursor.getString(9));
                    incrementalSurveyVo.setPole_paintingcode(cursor.getString(10));
                    incrementalSurveyVo.setLat(cursor.getString(11));
                    incrementalSurveyVo.setLan(cursor.getString(12));
                    incrementalSurveyVo.setPastSurveyFlag(cursor.getString(13));
                    incrementalSurveyVo.setIsMultiCircuit(cursor.getString(14));
                    incrementalSurveyVo.setCircuitType(cursor.getString(15));
                    incrementalSurveyVo.setIsEndPole(cursor.getString(16));
                    incrementalSurveyVo.setLineType(cursor.getString(17));
                    incrementalSurveyVo.setNoofCircuits(cursor.getString(18));
                    incrementalSurveyVo.setSs_name(cursor.getString(19));
                    incrementalSurveyVo.setPoleStructure(cursor.getString(20));
                    incrementalSurveyVo.setDiffVoltage(cursor.getString(21));
                    incrementalSurveyVo.setSsId(cursor.getString(22));
                    incrementalSurveyVo.setStjoint_rmu(cursor.getString(23));
                    incrementalSurveyVo.setRmu_type(cursor.getString(24));
                    incrementalSurveyVo.setRemarks(cursor.getString(25));
                    incrementalSurveyVo.setTappingLocation(cursor.getString(26));
                    incrementalSurveyVo.setNoofTappings(cursor.getString(27));

                    incrementalSurveyVo.setDtr_code(cursor.getString(28));
                    incrementalSurveyVo.setDtr_phase(cursor.getString(29));
                    incrementalSurveyVo.setDtr_scno(cursor.getString(30));
                    incrementalSurveyVo.setDtr_capacity(cursor.getString(31));
                    incrementalSurveyVo.setDtr_make(cursor.getString(32));
                    incrementalSurveyVo.setDtr_structure(cursor.getString(33));
                    incrementalSurveyVo.setNo_lt_ckt(cursor.getString(34));
                    incrementalSurveyVo.setDtr_ab_switch(cursor.getString(35));
                    incrementalSurveyVo.setVoltage_level(cursor.getString(36));
                    incrementalSurveyVo.setDtr_location_name(cursor.getString(37));
                    incrementalSurveyVo.setDtr_no_of_circuit(cursor.getString(38));
                    incrementalSurveyVo.setHt_service_number(cursor.getString(39));
                    incrementalSurveyVo.setPc_line_capacitor(cursor.getString(40));
                    incrementalSurveyVo.setPc_auto_reclosure(cursor.getString(41));
                    incrementalSurveyVo.setType_of_load(cursor.getString(42));
                    incrementalSurveyVo.setDtr_location_type(cursor.getString(43));
                    incrementalSurveyVo.setIs_ltckt_avalable(cursor.getString(44));
                    incrementalSurveyVo.setNo_of_ab_switches(cursor.getString(45));
                    incrementalSurveyVo.setWorkOrder(cursor.getString(46));
                    incrementalSurveyVo.setWoStatus(cursor.getString(47));
                    incrementalSurveyVo.setIsFeederType(cursor.getString(48));
                    incrementalSurveyVo.setSURVEY_TYPE(cursor.getString(49));
                    incrementalSurveyVosList.add(incrementalSurveyVo);
                } while (cursor.moveToNext());
            }
            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }

        return incrementalSurveyVosList;
    }


    public ArrayList<String> getIncrementalAll_DtrStructureSubStationMaster() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> dtr_structuctre_master_vo = new ArrayList<String>();
        String selectQuery = "SELECT  DISTINCT DTR_SECTION_NAME FROM INCREMENTAL_DTR_MASTER_TABLE";
        // String selectQuery = "SELECT DISTINCT  DTR_SECTION_ID|| '-' ||DTR_SECTION_NAME FROM EHTMASTER_TABLE WHERE DTR_MASTER_TABLE IS NOT NULL";

        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    dtr_structuctre_master_vo.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }
        } finally {
            db.close();
        }
        return dtr_structuctre_master_vo;
    }


    public boolean checkValueIncrementalDTRCodeMethod(String s) {
        boolean status = false;
        SQLiteDatabase sqldb = this.getWritableDatabase();
        try {
            String Query = "Select * from " + INCREMENTAL_DTR_MASTER_TABLE + " where " + DTR_STRUCTURE_CODE + " = '" + s + "'";
            Cursor cursor = sqldb.rawQuery(Query, null);
            if (cursor.getCount() <= 0) {
                cursor.close();
                return false;
            } else {
                status = true;
            }
            cursor.close();
            sqldb.close();
        } catch (Exception e) {
            sqldb.close();
        } finally {
            sqldb.close();
        }
        return status;

    }


    public boolean checkIncrementalValueDTRCodeLoacalSave(String s) {
        boolean status = false;
        SQLiteDatabase sqldb = this.getWritableDatabase();
        try {
            String Query = "Select * from " + INCREMENTAL_ARCGIS_TABLE + " where " + DTR_CODE1 + " = '" + s + "'";
            Cursor cursor = sqldb.rawQuery(Query, null);
            if (cursor.getCount() <= 0) {
                cursor.close();
                return false;
            } else {
                status = true;
            }
            cursor.close();
            sqldb.close();
        } catch (Exception e) {
            sqldb.close();
        } finally {
            sqldb.close();
        }
        return status;

    }


    public boolean checkIncrementalValueMethod(String ht_number) {
        boolean status = false;
        SQLiteDatabase sqldb = this.getWritableDatabase();
        try {
            String Query = "Select * from " + INCREMENTAL_FEEDER_SERVICE_MASTER_TABLE + " where " + FS_CONSUMER_NUMBER + " = '" + ht_number + "'";
            Cursor cursor = sqldb.rawQuery(Query, null);
            if (cursor.getCount() <= 0) {
                cursor.close();
                return false;
            } else {
                status = true;
            }
            cursor.close();
            sqldb.close();
        } catch (Exception e) {
            sqldb.close();
        } finally {
            sqldb.close();
        }
        return status;
    }


    public boolean addWoStatusMasterData(WoStatusMasterVo woStatusMasterVo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FEEDER_ID, woStatusMasterVo.getFEEDER_ID());
        values.put(WO_ID, woStatusMasterVo.getWO_ID());
        values.put(WO_STATUS, woStatusMasterVo.getWO_STATUS());
        long a = db.insert(WO_STATUS_TABLE, null, values);
        if (a == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }

    public List<WoStatusMasterVo> getWoStatusMasterData() {
        ArrayList<WoStatusMasterVo> woStatusMasterVosList = new ArrayList<WoStatusMasterVo>();

        String query = "SELECT * FROM WO_STATUS_TABLE";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                WoStatusMasterVo woStatusMasterVo = new WoStatusMasterVo();
                //vo.setLat(cursor.getString(0));
                woStatusMasterVo.setFEEDER_ID(cursor.getString(0));
                woStatusMasterVo.setWO_ID(cursor.getString(1));
                woStatusMasterVo.setWO_STATUS(cursor.getString(2));

                //  vo.setSECTION_CODE(cursor.getString(8));
                woStatusMasterVosList.add(woStatusMasterVo);
            } while (cursor.moveToNext());
        }

        db.close();
        return woStatusMasterVosList;
    }

    public boolean addSurveyDataLatLan(SurveyVo surveyDataVo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {
            values.put(POLE_NO, surveyDataVo.getPole_no());
            values.put(FEEDER_ID, surveyDataVo.getFeeder_id());
            values.put(LAT, surveyDataVo.getLat());
            values.put(LAN, surveyDataVo.getLan());
            values.put(ID, surveyDataVo.getID());

            // Check if record already exists
            String selection = POLE_NO + "=? AND " + FEEDER_ID + "=?";
            String[] selectionArgs = {
                    String.valueOf(surveyDataVo.getPole_no()),
                    surveyDataVo.getFeeder_id()
            };

            Cursor cursor = db.query(ARCGIS_TABLE_LAT_LANG_EDIT, null, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                // Record exists → update
                int rows = db.update(ARCGIS_TABLE_LAT_LANG_EDIT, values, selection, selectionArgs);
                cursor.close();
                db.close();
                return rows > 0;
            } else {
                // Record doesn’t exist → insert
                long result = db.insert(ARCGIS_TABLE_LAT_LANG_EDIT, null, values);
                if (cursor != null) cursor.close();
                db.close();
                return result != -1;
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return false;
        }
    }

    public ArrayList<SurveyVo> getEditLatLanData() {
        ArrayList<SurveyVo> surveyVoArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        try {
            String query = "SELECT * FROM " + ARCGIS_TABLE_LAT_LANG_EDIT;
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    SurveyVo surveyVo = new SurveyVo();
                    surveyVo.setPole_no(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(POLE_NO))));
                    surveyVo.setFeeder_id(cursor.getString(cursor.getColumnIndexOrThrow(FEEDER_ID)));
                    surveyVo.setLat(cursor.getString(cursor.getColumnIndexOrThrow(LAT)));
                    surveyVo.setLan(cursor.getString(cursor.getColumnIndexOrThrow(LAN)));
                    surveyVo.setID(cursor.getString(cursor.getColumnIndexOrThrow(ID)));

                    surveyVoArrayList.add(surveyVo);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return surveyVoArrayList;
    }


    public List<DistandeVo> getDistance() {
        List<DistandeVo> distandeVoArrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  POLE_NO,LAT,LAN  FROM INCREMENTAL_ARCGIS_TABLE  WHERE ROWID IN(SELECT MAX(ROWID) FROM INCREMENTAL_ARCGIS_TABLE  WHERE POLE_NO=(\n" +
                "SELECT  MAX(POLE_NO) FROM INCREMENTAL_ARCGIS_TABLE)) ";


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    DistandeVo distandeVo = new DistandeVo();
                    distandeVo.setPOLE_NO(cursor.getString(0));
                    distandeVo.setLAT(cursor.getString(1));
                    distandeVo.setLAN(cursor.getString(2));
                    distandeVoArrayList.add(distandeVo);
                } while (cursor.moveToNext());
            }
        } finally {
            db.close();
        }
        // return contact list
        return distandeVoArrayList;
    }


}
