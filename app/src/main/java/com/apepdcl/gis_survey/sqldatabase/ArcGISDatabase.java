package com.apepdcl.gis_survey.sqldatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;

import com.apepdcl.gis_survey.vo.DTRDetailsResponseVo;
import com.apepdcl.gis_survey.vo.DTRSurveyVo;
import com.apepdcl.gis_survey.vo.DistandeVo;
import com.apepdcl.gis_survey.vo.IncrementalDTRSurveyVo;
import com.apepdcl.gis_survey.vo.IncrementalSurveyDataVo;
import com.apepdcl.gis_survey.vo.LTCktsDataVo;
import com.apepdcl.gis_survey.vo.LTCktsResponseDataVo;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.apepdcl.gis_survey.vo.MultiCktVO;
import com.apepdcl.gis_survey.vo.SurveyDataVo;
import com.apepdcl.gis_survey.vo.WoStatusMasterVo;

import java.util.ArrayList;
import java.util.List;

public class ArcGISDatabase extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 14;

    // Database Name
    private static final String DATABASE_NAME = "GIS_LT_SURVEYDB";

    private static final String USERMASTER_TABLE = "USERMASTER_TABLE";
    private static final String SURVEY_TABLE = "SURVEY_TABLE";
    private static final String SURVEY_TABLE_REPEAT = "SURVEY_TABLE_REPEAT";
    private static final String SURVEY_TABLE_LAT_LANG = "SURVEY_TABLE_LAT_LANG";
    private static final String SURVEY_TABLE_POLE_DETAILS_EDIT = "SURVEY_TABLE_POLE_DETAILS_EDIT";

    private static final String SURVEY_TABLE_SOURCE_POLE_EDIT = "SURVEY_TABLE_SOURCE_POLE_EDIT";

    private static final String INCREMENTALSURVEY_TABLE = "INCREMENTALSURVEY_TABLE";
    private static final String DTR_TABLE = "DTR_TABLE";
    private static final String CMPDTR_TABLE = "CMPDTR_TABLE";
    private static final String DTRSURVEY_TABLE = "DTRSURVEY_TABLE";
    private static final String DTRDETAILS_TABLE = "DTRDETAILS_TABLE";
    private static final String INCREMENTAL_DTR_TABLE = "INCREMENTAL_DTR_TABLE";
    private static final String INCREMENTAL_DTRDETAILS_TABLE = "INCREMENTAL_DTRDETAILS_TABLE";
    private static final String INCREMENTAL_DTRSURVEY_TABLE = "INCREMENTAL_DTRSURVEY_TABLE";
    private static final String WO_STATUS_TABLE = "WO_STATUS_TABLE";


    private static final String ROLE = "ROLE";
    private static final String OFFICE_ID = "OFFICE_ID";
    private static final String SECTION_NAME = "SECTION_NAME";
    private static final String SEC_CODE = "SEC_CODE";
    private static final String OFFICE_TYPE = "OFFICE_TYPE";
    private static final String CIRCLE = "CIRCLE";
    private static final String USERID = "USERID";
    private static final String DIV_ID = "DIV_ID";
    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String REFRESH_TOKEN = "REFRESH_TOKEN";
    private static final String NAME = "NAME";
    private static final String FLAG1 = "FLAG1";
    private static final String FLAG2 = "FLAG2";
    private static final String FLAG3 = "FLAG3";
    private static final String FLAG4 = "FLAG4";

    //OH
    private static final String DTR_STRUCTURE = "DTR_STRUCTURE";
    private static final String LT_CIRCUIT = "LT_CIRCUIT";
    private static final String POLE_NO = "POLE_NO";
    private static final String FROM_POLE = "FROM_POLE";
    private static final String LINE_TYPE = "LINE_TYPE";
    private static final String IS_END_POLE = "IS_END_POLE";
    private static final String LINE_CONFIGURATION = "LINE_CONFIGURATION";
    private static final String INSULATOR_TYPE = "INSULATOR_TYPE";
    private static final String IS_STREETLIGHT_AV = "IS_STREETLIGHT_AV";
    private static final String IS_SERVICES_OTHERTHAN_SL = "IS_SERVICES_OTHERTHAN_SL";
    private static final String AGL_SERVICES = "AGL_SERVICES";
    private static final String NO_AGL_SERVICES = "NO_AGL_SERVICES";
    private static final String AQUA_SERVICES = "AQUA_SERVICES";
    private static final String NO_AQUA_SERVICES = "NO_AQUA_SERVICES";
    private static final String INDUSTRIAL_SERVICES = "INDUSTRIAL_SERVICES";
    private static final String NO_INDUSTRIAL_SERVICES = "NO_INDUSTRIAL_SERVICES";
    private static final String PIN_CUT = "PIN_CUT";
    private static final String IS_DOUBLE_FEEDING = "IS_DOUBLE_FEEDING";
    private static final String POLE_TYPE = "POLE_TYPE";
    private static final String POLE_HEIGHT = "POLE_HEIGHT";
    private static final String STRUT_STAY = "STRUT_STAY";
    private static final String TAPPING_POINT = "TAPPING_POINT";
    private static final String NO_OF_SPURLINES = "NO_OF_SPURLINES";
    private static final String TAPPING_LOCATION = "TAPPING_LOCATION";
    private static final String CONDUCTOR_SIZE = "CONDUCTOR_SIZE";
    private static final String IS_MULTI_CIRCUIT = "IS_MULTI_CIRCUIT";
    private static final String NO_OF_CIRCUITS = "NO_OF_CIRCUITS";
    private static final String MULTI_VOLTAGE = "MULTI_VOLTAGE";
    private static final String _33KV_VOLTAGE_CKTS = "_33KV_VOLTAGE_CKTS";
    private static final String NO_OF33KV_CKTS = "NO_OF33KV_CKTS";
    private static final String _11KV_VOLTAGE_CKTS = "_11KV_VOLTAGE_CKTS";
    private static final String NO_OF11KV_CKTS = "NO_OF11KV_CKTS";
    private static final String _6_3KV_VOLTAGE_CKTS = "_6_3KV_VOLTAGE_CKTS";
    private static final String NO_OF6_3KV_VOLTAGE_CKTS = "NO_OF6_3KV_VOLTAGE_CKTS";
    private static final String LT_VOLTAGE_CKTS = "LT_VOLTAGE_CKTS";
    private static final String NO_OF_LT_CKTS = "NO_OF_LT_CKTS";
    private static final String POLE_PAINTING_CODE = "POLE_PAINTING_CODE";
    private static final String REMARKS = "REMARKS";
    private static final String LAT = "LAT";
    private static final String LAN = "LAN";
    private static final String SURVEY_FLAG = "SURVEY_FLAG";
    private static final String FEEDER_ID = "FEEDER_ID";

    private static final String DTR_CODE = "DTR_CODE";
    private static final String DTR_PHASE = "DTR_PHASE";
    private static final String DTR_LOCATION = "DTR_LOCATION";
    private static final String LT_CKTCODE = "LT_CKTCODE";
    //UG
    private static final String LOCATION_TYPE = "LOCATION_TYPE";
    private static final String SERVICE_PILLERTYPE = "SERVICE_PILLERTYPE";
    private static final String FEEDERPILLET_OUTGN_CKT = "FEEDERPILLET_OUTGN_CKT";
    private static final String CABLE_SIZE = "CABLE_SIZE";
    private static final String NO_CABLES = "NO_CABLES";
    private static final String DTR_METER_AVILABLE = "DTR_METER_AVILABLE";
    private static final String DTR_NO_OF_METERS = "DTR_NO_OF_METERS";

    //DTR
    private static final String DTR_ABSWITCH = "DTR_ABSWITCH";
    private static final String DTR_HGFUSE = "DTR_HGFUSE";
    private static final String EARTH_PITS = "EARTH_PITS";
    private static final String CIPIPES = "CIPIPES";
    private static final String GIPIPES = "GIPIPES";
    private static final String DTR_METER = "DTR_METER";
    private static final String DTR_METER_NUM = "DTR_METER_NUM";
    private static final String DTR_METER_MAKE = "DTR_METER_MAKE";
    private static final String DTR_METER_CAPACITY = "DTR_METER_CAPACITY";
    private static final String DTR_METER_TYPE = "DTR_METER_TYPE";
    private static final String MODEM = "MODEM";
    private static final String MODEM_NUM = "MODEM_NUM";
    private static final String MODEM_MAKE = "MODEM_MAKE";
    private static final String BUSBAR_TYPE = "BUSBAR_TYPE";
    //private static final String CONDUCTOR_SIZE="CONDUCTOR_SIZE";
    private static final String HGFUSE_DTR = "HGFUSE_DTR";
    private static final String HGFUSE_DTR_CONDUCTOR_SIZE = "HGFUSE_DTR_CONDUCTOR_SIZE";
    private static final String DTR_POLE = "DTR_POLE";
    private static final String PIN = "PIN";
    private static final String CUT = "CUT";
    //private static final String REMARKS="REMARKS";
    private static final String LT_FUSESET = "LT_FUSESET";
    private static final String DTR_MAKE = "DTR_MAKE";
    private static final String DTR_SERIALNO = "DTR_SERIALNO";
    private static final String DTR_LOAD_TYPE = "DTR_LOAD_TYPE";
    private static final String DTR_CAPACITY = "DTR_CAPACITY";
    private static final String DTR_STRUCTURE_TYPE = "DTR_STRUCTURE_TYPE";
    private static final String IS_CROSSINGS_AV = "IS_CROSSINGS_AV";
    private static final String NOOF_6_3KV_CROSSINGS = "NOOF_6_3KV_CROSSINGS";
    private static final String NOOF_11KV_CROSSINGS = "NOOF_11KV_CROSSINGS";
    private static final String NOOF_33KV_CROSSINGS = "NOOF_33KV_CROSSINGS";
    private static final String NOOF_LT_CROSSINGS = "NOOF_LT_CROSSINGS";
    private static final String NOOF_TC_CROSSINGS = "NOOF_TC_CROSSINGS";
    private static final String NOOF_TM_CROSSINGS = "NOOF_TM_CROSSINGS";

    //DTR
    private static final String DTR_POLE_SUPPORT = "DTR_POLE_SUPPORT";
    private static final String NOOF_STURT = "NOOF_STURT";
    private static final String NOOF_STAY = "NOOF_STAY";
    private static final String DTR_ABSWTICH_TYPE = "DTR_ABSWTICH_TYPE";
    private static final String DTR_MTR2_NUM = "DTR_MTR2_NUM";
    private static final String DTR_MTR2_MAKE = "DTR_MTR2_MAKE";
    private static final String DTR_MTR2_CAPACITY = "DTR_MTR2_CAPACITY";
    private static final String DTR_MTR2_TYPE = "DTR_MTR2_TYPE";
    private static final String DTR_MTR3_NUM = "DTR_MTR3_NUM";
    private static final String DTR_MTR3_MAKE = "DTR_MTR3_MAKE";
    private static final String DTR_MTR3_CAPACITY = "DTR_MTR3_CAPACITY";
    private static final String DTR_MTR3_TYPE = "DTR_MTR3_TYPE";
    private static final String LT_BUSBAR = "LT_BUSBAR";
    private static final String LT_BUSBAR_CABLESIZE = "LT_BUSBAR_CABLESIZE";
    private static final String LT_BUSBAR_CONDUTORSIZE = "LT_BUSBAR_CONDUTORSIZE";
    private static final String NOOF_PININSULATORS = "NOOF_PININSULATORS";
    private static final String NOOF_DISCINSULATORS = "NOOF_DISCINSULATORS";
    private static final String NOOF_DTRMETERS = "NOOF_DTRMETERS";

    private static final String DTR_SNO = "DTR_SNO";
    private static final String TYPE_OF_LOAD = "TYPE_OF_LOAD";
    private static final String MODEM2 = "MODEM2";
    private static final String MODEM2_NUM = "MODEM2_NUM";
    private static final String MODEM2_MAKE = "MODEM2_MAKE";
    private static final String MODEM3 = "MODEM3";
    private static final String MODEM3_NUM = "MODEM3_NUM";
    private static final String MODEM3_MAKE = "MODEM3_MAKE";
    //private static final String DTR_LANDMARK="DTR_LANDMARK";
    private static final String NOOF_LTCIRCUITS = "NOOF_LTCIRCUITS";
    private static final String WORK_ORDERID = "WORK_ORDERID";
    private static final String WORKORDER_STATUS = "WORKORDER_STATUS";
    private static final String WO_ID = "WO_ID";
    private static final String WO_STATUS = "WO_STATUS";
    private static final String LT_CKT = "LT_CKT";
    private static final String DTR_STRCODE = "DTR_STRCODE";
    private static final String IS_SURVEY_COMPLETED = "IS_SURVEY_COMPLETED";
    private static final String GLAT = "GLAT";
    private static final String GLONG = "GLONG";


    private static final String STATUS = "STATUS";
    private static final String TAPPING_POLE = "TAPPING_POLE";


   /* private static final String DTR_STRUCTURE="DTR_STRUCTURE";
    private static final String DTR_STRUCTURE="DTR_STRUCTURE";
    private static final String DTR_STRUCTURE="DTR_STRUCTURE";
    private static final String DTR_STRUCTURE="DTR_STRUCTURE";
    private static final String DTR_STRUCTURE="DTR_STRUCTURE";
    private static final String DTR_STRUCTURE="DTR_STRUCTURE";
    private static final String DTR_STRUCTURE="DTR_STRUCTURE";
    private static final String DTR_STRUCTURE="DTR_STRUCTURE";*/

    //NEW
    private static final String DEFFECTS_AVAILABLE = "DEFFECTS_AVAILABLE";
    private static final String POLE_DEFFECTS = "POLE_DEFFECTS";
    private static final String CONDUCTOR_DEFFECTS = "CONDUCTOR_DEFFECTS";
    private static final String INSULATOR_DEFFECTS = "INSULATOR_DEFFECTS";
    private static final String CROSSARM_DEFFECTS = "CROSSARM_DEFFECTS";
    private static final String STRUT_DEFFECTS = "STRUT_DEFFECTS";
    private static final String STRAY_DEFFECTS = "STRAY_DEFFECTS";
    private static final String HTLT_CLEARANCE = "HTLT_CLEARANCE";
    private static final String INTERMEDIATE_POLE = "INTERMEDIATE_POLE";
    private static final String RESTRINGING = "RESTRINGING";
    private static final String OTHER_DEFECTS = "OTHER_DEFECTS";
    private static final String NUM_SCS = "NUM_SCS";

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

    //OTHER POLES
    private static final String NUM_OTHER_CABLES = "NUM_OTHER_CABLES";

    //JHC PHASE 1
    private static final String POLE_SURVEY_TYPE = "POLE_SURVEY_TYPE";
    private static final String LINE_SURVEY_TYPE = "LINE_SURVEY_TYPE";

    //OTHER POLES
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

    //ADDED ON 26-07-2024
    private static final String STL_METER = "STL_METER";
    private static final String STL_SWITCH = "STL_SWITCH";
    private static final String STL_SOURCE = "STL_SOURCE";
    private static final String ID = "ID";
    private static final String GPS_ACCURACY = "GPS_ACCURACY";
    private static final String RDSS_SCHEME_TYPE = "RDSS_SCHEME_TYPE";
    private static final String ADDRESS_1 = "ADDRESS_1";
    private static final String ADDRESS_2 = "ADDRESS_2";


    //NEW DTR
    private static final String STRUCTURE_DEFECTS = "STRUCTURE_DEFECTS";
    private static final String DTR_FENCING = "DTR_FENCING";
    private static final String DTR_AB_DEFECTS = "DTR_AB_DEFECTS";
    private static final String HG_FUSE_DEFECTS = "HG_FUSE_DEFECTS";
    private static final String LT_FUSE_DEFECTS = "LT_FUSE_DEFECTS";
    private static final String NUM_EARTH = "NUM_EARTH";
    private static final String NUM_LT_BUSHINGS = "NUM_LT_BUSHINGS";
    private static final String NUM_HT_BUSHINGS = "NUM_HT_BUSHINGS";
    private static final String NUM_PG_LUG = "NUM_PG_LUG";
    private static final String XLPE_DEFECTS = "XLPE_DEFECTS";
    private static final String NUM_ARRESTER = "NUM_ARRESTER";
    private static final String OIL_LEAK = "OIL_LEAK";
    private static final String DTR_OTHER_DEFECTS = "DTR_OTHER_DEFECTS";

    private static final String SURVEY_REMARKS = "SURVEY_REMARKS";

    //ADDED ON 26-07-2024
    private static final String BODY_EARTH = "BODY_EARTH";
    private static final String NEUTRAL_EARTH = "NEUTRAL_EARTH";

    //LT IMAGES


    //DTR IMAGES
    private static final String DTR_IMAGE_TABLE = "DTR_IMAGE_TABLE";
    private static final String DTR_IMAGE_1 = "DTR_IMAGE_1";
    private static final String DTR_IMAGE_2 = "DTR_IMAGE_2";
    private static final String DTR_IMAGE_3 = "DTR_IMAGE_3";

    //IMAGE TABLE & COLUMNS
    private static final String LT_IMAGE_TABLE = "LT_IMAGE_TABLE";
    private static final String LT_IMAGE_1 = "LT_IMAGE_1";
    private static final String LT_IMAGE_2 = "LT_IMAGE_2";
    private static final String LT_IMAGE_3 = "LT_IMAGE_3";

    //MULTI CIRCUIT
    private static final String MLT_CKT_TABLE = "MLT_CKT_TABLE";
    private static final String VOLTAGE_LEVEL = "VOLTAGE_LEVEL";
    private static final String MC_FEEDER_ID = "MC_FEEDER_ID";
    private static final String MC_FEEDER_NAME = "MC_FEEDER_NAME";
    private static final String MC_POLE_TYPE = "MC_POLE_TYPE";
    private static final String MC_POLE_HEIGHT = "MC_POLE_HEIGHT";
    private static final String MC_POLE_NO = "MC_POLE_NO";


    String CREATE_LT_IMAGE_TABLE = "CREATE TABLE " + LT_IMAGE_TABLE + "(" + DTR_STRUCTURE + " TEXT ," + LT_CIRCUIT + " TEXT," + POLE_NO + " INTEGER," + LT_IMAGE_1 + " TEXT ," + LT_IMAGE_2 + " TEXT ," + LT_IMAGE_3 + " TEXT " + ")";

    String CREATE_MLT_CKT_TABLE = "CREATE TABLE " + MLT_CKT_TABLE + "(" + VOLTAGE_LEVEL + " TEXT ," + MC_FEEDER_ID + " TEXT," + MC_FEEDER_NAME + " TEXT," + MC_POLE_TYPE + " TEXT ," + MC_POLE_HEIGHT + " TEXT ," + MC_POLE_NO + " INTEGER " + ")";


    String CREATE_USERMASTER_TABLE = "CREATE TABLE " + USERMASTER_TABLE +
            "(" + ROLE + " TEXT ," +
            OFFICE_ID + " TEXT ," +
            SECTION_NAME + " TEXT ," +
            SEC_CODE + " TEXT ," +
            OFFICE_TYPE + " TEXT ," +
            CIRCLE + " TEXT ," +
            USERID + " TEXT ," +
            DIV_ID + " TEXT ," +
            ACCESS_TOKEN + " TEXT ," +
            REFRESH_TOKEN + " TEXT ," +
            NAME + " TEXT ," +
            FLAG1 + " TEXT ," +
            FLAG2 + " TEXT ," +
            FLAG3 + " TEXT " + ")";
    String CREATE_SURVEY_TABLE = "CREATE TABLE " + SURVEY_TABLE + "(" + DTR_STRUCTURE + " TEXT ," + LT_CIRCUIT + " TEXT," + POLE_NO + " INTEGER," + FROM_POLE + " INTEGER ," + LINE_TYPE + " TEXT ," + IS_END_POLE + " TEXT ," + LINE_CONFIGURATION + " TEXT ," + IS_STREETLIGHT_AV + " TEXT ," + IS_SERVICES_OTHERTHAN_SL + " TEXT ," + NO_AGL_SERVICES + " TEXT ," + NO_AQUA_SERVICES + " TEXT ," + NO_INDUSTRIAL_SERVICES + " TEXT ," + PIN_CUT + " TEXT ," + IS_DOUBLE_FEEDING + " TEXT ," + POLE_TYPE + " TEXT ," + POLE_HEIGHT + " TEXT ," + STRUT_STAY + " TEXT ," + TAPPING_POINT + " TEXT ," + NO_OF_SPURLINES + " TEXT ," + TAPPING_LOCATION + " TEXT ," + CONDUCTOR_SIZE + " TEXT ," + IS_MULTI_CIRCUIT + " TEXT ," + NO_OF_CIRCUITS + " TEXT ," + MULTI_VOLTAGE + " TEXT ," + NO_OF33KV_CKTS + " TEXT ," + NO_OF11KV_CKTS + " TEXT ," + NO_OF6_3KV_VOLTAGE_CKTS + " TEXT ," + NO_OF_LT_CKTS + " TEXT ," + POLE_PAINTING_CODE + " TEXT ," + REMARKS + " TEXT ," + LAT + " TEXT ," + LAN + " TEXT ," + SURVEY_FLAG + " TEXT ," + FEEDER_ID + " TEXT ," + LOCATION_TYPE + " TEXT ," + SERVICE_PILLERTYPE + " TEXT ," + FEEDERPILLET_OUTGN_CKT + " TEXT ," + CABLE_SIZE + " TEXT ," + NO_CABLES + " TEXT ," + DTR_METER_AVILABLE + " TEXT ,"
            + DTR_NO_OF_METERS + " TEXT,"
            + IS_CROSSINGS_AV + " TEXT ,"
            + NOOF_6_3KV_CROSSINGS + " TEXT ,"
            + NOOF_11KV_CROSSINGS + " TEXT ,"
            + NOOF_33KV_CROSSINGS + " TEXT ,"
            + NOOF_LT_CROSSINGS + " TEXT ,"
            + NOOF_TC_CROSSINGS + " TEXT ,"
            + NOOF_TM_CROSSINGS + " TEXT ,"
            + DEFFECTS_AVAILABLE + " TEXT ,"
            + POLE_DEFFECTS + " TEXT ,"
            + CONDUCTOR_DEFFECTS + " TEXT ,"
            + INSULATOR_DEFFECTS + " TEXT ,"
            + CROSSARM_DEFFECTS + " TEXT ,"
            + STRUT_DEFFECTS + " TEXT ,"
            + STRAY_DEFFECTS + " TEXT ,"
            + HTLT_CLEARANCE + " TEXT ,"
            + INTERMEDIATE_POLE + " TEXT ,"
            + RESTRINGING + " TEXT ,"
            + OTHER_DEFECTS + " TEXT ,"
            + NUM_SCS + " TEXT ,"

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
            + NUM_OTHER_CABLES + " TEXT ,"

            + POLE_SURVEY_TYPE + " TEXT ,"
            + LINE_SURVEY_TYPE + " TEXT ,"
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


            + STL_METER + " TEXT ,"
            + STL_SWITCH + " TEXT ,"
            + STL_SOURCE + " TEXT ,"
            + ID + " TEXT ,"
            + GPS_ACCURACY + " TEXT ,"
            + RDSS_SCHEME_TYPE + " TEXT ,"
            + ADDRESS_1 + " TEXT ,"
            + ADDRESS_2 + " TEXT ,"

            + FLAG1 + " TEXT ,"
            + FLAG2 + " TEXT ,"
            + FLAG3 + " TEXT " + ")";

    String CREATE_SURVEY_TABLE_REPEAT = "CREATE TABLE " + SURVEY_TABLE_REPEAT + "(" + DTR_STRUCTURE + " TEXT ," + LT_CIRCUIT + " TEXT," + POLE_NO + " INTEGER," + FROM_POLE + " INTEGER ," + LINE_TYPE + " TEXT ," + IS_END_POLE + " TEXT ," + LINE_CONFIGURATION + " TEXT ," + IS_STREETLIGHT_AV + " TEXT ," + IS_SERVICES_OTHERTHAN_SL + " TEXT ," + NO_AGL_SERVICES + " TEXT ," + NO_AQUA_SERVICES + " TEXT ," + NO_INDUSTRIAL_SERVICES + " TEXT ," + PIN_CUT + " TEXT ," + IS_DOUBLE_FEEDING + " TEXT ," + POLE_TYPE + " TEXT ," + POLE_HEIGHT + " TEXT ," + STRUT_STAY + " TEXT ," + TAPPING_POINT + " TEXT ," + NO_OF_SPURLINES + " TEXT ," + TAPPING_LOCATION + " TEXT ," + CONDUCTOR_SIZE + " TEXT ," + IS_MULTI_CIRCUIT + " TEXT ," + NO_OF_CIRCUITS + " TEXT ," + MULTI_VOLTAGE + " TEXT ," + NO_OF33KV_CKTS + " TEXT ," + NO_OF11KV_CKTS + " TEXT ," + NO_OF6_3KV_VOLTAGE_CKTS + " TEXT ," + NO_OF_LT_CKTS + " TEXT ," + POLE_PAINTING_CODE + " TEXT ," + REMARKS + " TEXT ," + LAT + " TEXT ," + LAN + " TEXT ," + SURVEY_FLAG + " TEXT ," + FEEDER_ID + " TEXT ," + LOCATION_TYPE + " TEXT ," + SERVICE_PILLERTYPE + " TEXT ," + FEEDERPILLET_OUTGN_CKT + " TEXT ," + CABLE_SIZE + " TEXT ," + NO_CABLES + " TEXT ," + DTR_METER_AVILABLE + " TEXT ,"
            + DTR_NO_OF_METERS + " TEXT,"
            + IS_CROSSINGS_AV + " TEXT ,"
            + NOOF_6_3KV_CROSSINGS + " TEXT ,"
            + NOOF_11KV_CROSSINGS + " TEXT ,"
            + NOOF_33KV_CROSSINGS + " TEXT ,"
            + NOOF_LT_CROSSINGS + " TEXT ,"
            + NOOF_TC_CROSSINGS + " TEXT ,"
            + NOOF_TM_CROSSINGS + " TEXT ,"
            + NUM_SCS + " TEXT ,"
            + NUM_OTHER_CABLES + " TEXT ,"

            + POLE_SURVEY_TYPE + " TEXT ,"
            + LINE_SURVEY_TYPE + " TEXT ,"

            + POLE_CONCRETE + " TEXT ,"

            + FLAG1 + " TEXT ,"
            + FLAG2 + " TEXT ,"
            + FLAG3 + " TEXT " + ")";


    String CREATE_SURVEY_TABLE_LAT_LANG = "CREATE TABLE " + SURVEY_TABLE_LAT_LANG + " (" +
            POLE_NO + " INTEGER, " +
            FEEDER_ID + " TEXT, " +
            LAT + " TEXT, " +
            LAN + " TEXT, " +
            ID + " TEXT, " +
            "UNIQUE (" + POLE_NO + ", " + FEEDER_ID + ") ON CONFLICT REPLACE" +
            ");";

    String CREATE_SURVEY_TABLE_POLE_DETAILS_EDIT = "CREATE TABLE "
            + SURVEY_TABLE_POLE_DETAILS_EDIT + " ("
            + POLE_TYPE + " TEXT, "
            + POLE_HEIGHT + " TEXT, "
            + STRUT_STAY + " TEXT, "
            + PIN_CUT + " TEXT, "
            + POLE_CONCRETE + " TEXT, "
            + IS_DOUBLE_FEEDING + " TEXT, "
            + POLE_PAINTING_CODE + " TEXT, "
            + ID + " TEXT, "
            + FEEDER_ID + " TEXT, "
            + INSULATOR_TYPE + " TEXT, "
            + LINE_CONFIGURATION + " TEXT"
            + ");";


    String CREATE_SURVEY_TABLE_SOURCE_POLE_EDIT = "CREATE TABLE "
            + SURVEY_TABLE_SOURCE_POLE_EDIT + " ("
            + ID + " TEXT, "
            + FEEDER_ID + " TEXT, "
            + FROM_POLE + " TEXT"
            + ");";


    String CREATE_DTRMASTER_TABLE = "CREATE TABLE " + DTR_TABLE + "(" + DTR_CODE + " TEXT ," + DTR_PHASE + " TEXT ," + DTR_LOCATION + " TEXT ," + LT_CKTCODE + " TEXT ," + FEEDER_ID + " TEXT ," + FLAG2 + " TEXT ," + FLAG3 + " TEXT " + ")";

    String CREATE_CMPDTRMASTER_TABLE = "CREATE TABLE " + CMPDTR_TABLE + "(" + DTR_CODE + " TEXT ," + DTR_PHASE + " TEXT ," + DTR_LOCATION + " TEXT ," + LT_CKTCODE + " TEXT ," + FEEDER_ID + " TEXT ," + FLAG2 + " TEXT ," + FLAG3 + " TEXT " + ")";


    String CREATE_DTRSURVEY_TABLE = "CREATE TABLE " + DTRSURVEY_TABLE + "(" + DTR_STRUCTURE + " TEXT ," + DTR_ABSWITCH + " TEXT," + DTR_HGFUSE + " TEXT," + EARTH_PITS + " TEXT ," + CIPIPES + " TEXT ," + GIPIPES + " TEXT ," + DTR_METER + " TEXT ," + DTR_METER_NUM + " TEXT ," + DTR_METER_MAKE + " TEXT ," + DTR_METER_CAPACITY + " TEXT ," + DTR_METER_TYPE + " TEXT ," + MODEM + " TEXT ," + MODEM_NUM + " TEXT ," + MODEM_MAKE + " TEXT ," + BUSBAR_TYPE + " TEXT ," + CONDUCTOR_SIZE + " TEXT ," + HGFUSE_DTR + " TEXT ," + HGFUSE_DTR_CONDUCTOR_SIZE + " TEXT ," + DTR_POLE + " TEXT ," + PIN + " TEXT ," + CUT + " TEXT ," + REMARKS + " TEXT ," + LT_FUSESET + " TEXT ," + LAT + " TEXT ," + LAN + " TEXT ," + DTR_MAKE + " TEXT ," + DTR_SERIALNO + " TEXT ," + DTR_LOAD_TYPE + " TEXT ," + DTR_PHASE + " TEXT ," + DTR_CAPACITY + " TEXT ," + DTR_STRUCTURE_TYPE + " TEXT ," + DTR_POLE_SUPPORT + " TEXT ," + NOOF_STURT + " TEXT ," + NOOF_STAY + " TEXT ," + DTR_ABSWTICH_TYPE + " TEXT ," + NOOF_PININSULATORS + " TEXT ," + NOOF_DISCINSULATORS + " TEXT ," + DTR_MTR2_NUM + " TEXT ," + DTR_MTR2_MAKE + " TEXT ," + DTR_MTR2_CAPACITY + " TEXT ," + DTR_MTR2_TYPE + " TEXT ," + DTR_MTR3_NUM + " TEXT ," + DTR_MTR3_MAKE + " TEXT ," + DTR_MTR3_CAPACITY + " TEXT ," + DTR_MTR3_TYPE + " TEXT ," + LT_BUSBAR + " TEXT ,"
            + LT_BUSBAR_CABLESIZE + " TEXT ," + LT_BUSBAR_CONDUTORSIZE + " TEXT ," +
            NOOF_DTRMETERS + " TEXT ," + MODEM2 + " TEXT ," + MODEM2_NUM + " TEXT ," +
            MODEM2_MAKE + " TEXT ," + MODEM3 + " TEXT ," + MODEM3_NUM + " TEXT ," +
            MODEM3_MAKE + " TEXT ," + DTR_LOCATION + " TEXT ," + NOOF_LTCIRCUITS + " TEXT ,"
            + STRUCTURE_DEFECTS + " TEXT ," + DTR_FENCING + " TEXT ," + DTR_AB_DEFECTS + " TEXT ,"
            + HG_FUSE_DEFECTS + " TEXT ," + LT_FUSE_DEFECTS + " TEXT ," + NUM_EARTH + " TEXT ,"
            + NUM_LT_BUSHINGS + " TEXT ," + NUM_HT_BUSHINGS + " TEXT ," + NUM_PG_LUG + " TEXT ,"
            + XLPE_DEFECTS + " TEXT ," + NUM_ARRESTER + " TEXT ," + OIL_LEAK + " TEXT ,"
            + DTR_OTHER_DEFECTS + " TEXT ,"
            + DTR_IMAGE_1 + " TEXT ,"
            + DTR_IMAGE_2 + " TEXT ,"
            + DTR_IMAGE_3 + " TEXT ,"
            + SURVEY_REMARKS + " TEXT ,"

            + BODY_EARTH + " TEXT ,"
            + NEUTRAL_EARTH + " TEXT ,"

            + FLAG1 + " TEXT ," + FLAG2 + " TEXT ," + FLAG3 + " TEXT ," + FLAG4 + " TEXT " + ")";


    String CREATE_DTRDETAILS_TABLE = "CREATE TABLE " + DTRDETAILS_TABLE + "(" + DTR_CODE + " TEXT ," + DTR_PHASE + " TEXT ," + DTR_SNO + " TEXT ," + DTR_CAPACITY + " TEXT ," + DTR_MAKE + " TEXT ," + TYPE_OF_LOAD + " TEXT ," + GLAT + " TEXT ," + GLONG + " TEXT ," + FLAG1 + " TEXT " + ")";


//Incremental

    String CREATE_INCREMENTAL_DTRMASTER_TABLE = "CREATE TABLE " + INCREMENTAL_DTR_TABLE + "(" + DTR_CODE + " TEXT ," + DTR_PHASE + " TEXT ," + DTR_LOCATION + " TEXT ," + LT_CKTCODE + " TEXT ," + FEEDER_ID + " TEXT ," + WORK_ORDERID + " TEXT ," + STATUS + " TEXT ," + TAPPING_POLE + " TEXT ," + FLAG2 + " TEXT ," + FLAG3 + " TEXT " + ")";


    String CREATE_INCREMENTALDTRDETAILS_TABLE = "CREATE TABLE " + INCREMENTAL_DTRDETAILS_TABLE + "(" + DTR_CODE + " TEXT ," + DTR_PHASE + " TEXT ," + DTR_SNO + " TEXT ," + DTR_CAPACITY + " TEXT ," + DTR_MAKE + " TEXT ," + TYPE_OF_LOAD + " TEXT ," + WORK_ORDERID + " TEXT ," + FLAG1 + " TEXT ," + FLAG2 + " TEXT ," + FLAG3 + " TEXT " + ")";


    String CREATE_INCREMENTALSURVEY_TABLE = "CREATE TABLE " + INCREMENTALSURVEY_TABLE + "(" + DTR_STRUCTURE + " TEXT ," + LT_CIRCUIT + " TEXT," + POLE_NO + " INTEGER UNIQUE," + FROM_POLE + " INTEGER ," + LINE_TYPE + " TEXT ," + IS_END_POLE + " TEXT ," + LINE_CONFIGURATION + " TEXT ," + IS_STREETLIGHT_AV + " TEXT ," + IS_SERVICES_OTHERTHAN_SL + " TEXT ," + NO_AGL_SERVICES + " TEXT ," + NO_AQUA_SERVICES + " TEXT ," + NO_INDUSTRIAL_SERVICES + " TEXT ," + PIN_CUT + " TEXT ," + IS_DOUBLE_FEEDING + " TEXT ," + POLE_TYPE + " TEXT ," + POLE_HEIGHT + " TEXT ," + STRUT_STAY + " TEXT ," + TAPPING_POINT + " TEXT ," + NO_OF_SPURLINES + " TEXT ," + TAPPING_LOCATION + " TEXT ," + CONDUCTOR_SIZE + " TEXT ," + IS_MULTI_CIRCUIT + " TEXT ," + NO_OF_CIRCUITS + " TEXT ," + MULTI_VOLTAGE + " TEXT ," + NO_OF33KV_CKTS + " TEXT ," + NO_OF11KV_CKTS + " TEXT ," + NO_OF6_3KV_VOLTAGE_CKTS + " TEXT ," + NO_OF_LT_CKTS + " TEXT ," + POLE_PAINTING_CODE + " TEXT ," + REMARKS + " TEXT ," + LAT + " TEXT ," + LAN + " TEXT ," + SURVEY_FLAG + " TEXT ," + FEEDER_ID + " TEXT ," + LOCATION_TYPE + " TEXT ," + SERVICE_PILLERTYPE + " TEXT ," + FEEDERPILLET_OUTGN_CKT + " TEXT ," + CABLE_SIZE + " TEXT ," + NO_CABLES + " TEXT ," + DTR_METER_AVILABLE + " TEXT ,"
            + DTR_NO_OF_METERS + " TEXT,"
            + IS_CROSSINGS_AV + " TEXT ,"
            + NOOF_6_3KV_CROSSINGS + " TEXT ,"
            + NOOF_11KV_CROSSINGS + " TEXT ,"
            + NOOF_33KV_CROSSINGS + " TEXT ,"
            + NOOF_LT_CROSSINGS + " TEXT ,"
            + NOOF_TC_CROSSINGS + " TEXT ,"
            + NOOF_TM_CROSSINGS + " TEXT ,"
            + WORK_ORDERID + " TEXT ,"
            + WORKORDER_STATUS + " TEXT ,"
            + IS_SURVEY_COMPLETED + " TEXT ,"
            + FLAG1 + " TEXT ,"
            + FLAG2 + " TEXT ,"
            + FLAG3 + " TEXT " + ")";


    String CREATE_INCREMENTAL_DTRSURVEY_TABLE = "CREATE TABLE " + INCREMENTAL_DTRSURVEY_TABLE + "(" + DTR_STRUCTURE + " TEXT ," + DTR_ABSWITCH + " TEXT," + DTR_HGFUSE + " TEXT," + EARTH_PITS + " TEXT ," + CIPIPES + " TEXT ," + GIPIPES + " TEXT ," + DTR_METER + " TEXT ," + DTR_METER_NUM + " TEXT ," + DTR_METER_MAKE + " TEXT ," + DTR_METER_CAPACITY + " TEXT ," + DTR_METER_TYPE + " TEXT ," + MODEM + " TEXT ," + MODEM_NUM + " TEXT ," + MODEM_MAKE + " TEXT ," + BUSBAR_TYPE + " TEXT ," + CONDUCTOR_SIZE + " TEXT ," + HGFUSE_DTR + " TEXT ," + HGFUSE_DTR_CONDUCTOR_SIZE + " TEXT ," + DTR_POLE + " TEXT ," + PIN + " TEXT ," + CUT + " TEXT ," + REMARKS + " TEXT ," + LT_FUSESET + " TEXT ," + LAT + " TEXT ," + LAN + " TEXT ," + DTR_MAKE + " TEXT ," + DTR_SERIALNO + " TEXT ," + DTR_LOAD_TYPE + " TEXT ," + DTR_PHASE + " TEXT ," + DTR_CAPACITY + " TEXT ," + DTR_STRUCTURE_TYPE + " TEXT ," + DTR_POLE_SUPPORT + " TEXT ," + NOOF_STURT + " TEXT ," + NOOF_STAY + " TEXT ," + DTR_ABSWTICH_TYPE + " TEXT ," + NOOF_PININSULATORS + " TEXT ," + NOOF_DISCINSULATORS + " TEXT ," + DTR_MTR2_NUM + " TEXT ," + DTR_MTR2_MAKE + " TEXT ," + DTR_MTR2_CAPACITY + " TEXT ," + DTR_MTR2_TYPE + " TEXT ," + DTR_MTR3_NUM + " TEXT ," + DTR_MTR3_MAKE + " TEXT ," + DTR_MTR3_CAPACITY + " TEXT ," + DTR_MTR3_TYPE + " TEXT ," + LT_BUSBAR + " TEXT ,"
            + LT_BUSBAR_CABLESIZE + " TEXT ," + LT_BUSBAR_CONDUTORSIZE + " TEXT ," + NOOF_DTRMETERS + " TEXT ," + MODEM2 + " TEXT ," + MODEM2_NUM + " TEXT ," + MODEM2_MAKE + " TEXT ," + MODEM3 + " TEXT ," + MODEM3_NUM + " TEXT ," + MODEM3_MAKE + " TEXT ," + DTR_LOCATION + " TEXT ," + NOOF_LTCIRCUITS + " TEXT ," + WORK_ORDERID + " TEXT ," + WORKORDER_STATUS + " TEXT ," + FLAG1 + " TEXT ," + FLAG2 + " TEXT " + ")";


    String CREATE_WO_STATUS_MASTER_TABLE = "CREATE TABLE " + WO_STATUS_TABLE + "("
            + FEEDER_ID + " TEXT ," + WO_ID + " TEXT ," + WO_STATUS + " TEXT ," + LT_CKT + " TEXT ," + DTR_STRCODE + " TEXT ," + FLAG3 + " TEXT " + ")";


    public ArcGISDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERMASTER_TABLE);
        db.execSQL(CREATE_SURVEY_TABLE);
        db.execSQL(CREATE_DTRMASTER_TABLE);
        db.execSQL(CREATE_CMPDTRMASTER_TABLE);
        db.execSQL(CREATE_DTRSURVEY_TABLE);
        db.execSQL(CREATE_DTRDETAILS_TABLE);
        db.execSQL(CREATE_INCREMENTALSURVEY_TABLE);
        db.execSQL(CREATE_INCREMENTAL_DTRMASTER_TABLE);
        db.execSQL(CREATE_INCREMENTALDTRDETAILS_TABLE);
        db.execSQL(CREATE_INCREMENTAL_DTRSURVEY_TABLE);
        db.execSQL(CREATE_WO_STATUS_MASTER_TABLE);
        db.execSQL(CREATE_SURVEY_TABLE_REPEAT);
        db.execSQL(CREATE_LT_IMAGE_TABLE);
        db.execSQL(CREATE_MLT_CKT_TABLE);
        db.execSQL(CREATE_SURVEY_TABLE_LAT_LANG);
        db.execSQL(CREATE_SURVEY_TABLE_POLE_DETAILS_EDIT);

        db.execSQL(CREATE_SURVEY_TABLE_SOURCE_POLE_EDIT);


    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USERMASTER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SURVEY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DTR_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CMPDTR_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DTRSURVEY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DTRDETAILS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + INCREMENTALSURVEY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + INCREMENTAL_DTR_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + INCREMENTAL_DTRDETAILS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + INCREMENTAL_DTRSURVEY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + WO_STATUS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SURVEY_TABLE_REPEAT);
        db.execSQL("DROP TABLE IF EXISTS " + LT_IMAGE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MLT_CKT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SURVEY_TABLE_LAT_LANG);
        db.execSQL("DROP TABLE IF EXISTS " + SURVEY_TABLE_POLE_DETAILS_EDIT);

        db.execSQL("DROP TABLE IF EXISTS " + SURVEY_TABLE_SOURCE_POLE_EDIT);


        // Create tables again
        onCreate(db);
    }

    public void clearUserdData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + USERMASTER_TABLE);
    }

    public void clearSurveyData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + SURVEY_TABLE);
    }

    public void clearEditLatLanData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + SURVEY_TABLE_LAT_LANG);
    }

    public void clearEditPoleDetailsData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + SURVEY_TABLE_POLE_DETAILS_EDIT);
    }


    public void clearEditSourcePoleData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + SURVEY_TABLE_SOURCE_POLE_EDIT);
    }

    public void clearWOStatusMasterData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + WO_STATUS_TABLE);
    }

    public void clearIncrementalSurveyData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + INCREMENTALSURVEY_TABLE);
    }

    public void clearIncrementalDTRSurveyData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + INCREMENTAL_DTRSURVEY_TABLE);
    }


    public void clearIncrementalDTRData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + INCREMENTAL_DTR_TABLE);
    }

    public void clearDTRData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + DTR_TABLE);
    }

    public void clearCmpDTRData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + CMPDTR_TABLE);
    }

    public void clearDTRSurveyData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + DTRSURVEY_TABLE);
    }

    public void clearDTRDetailsData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + DTRDETAILS_TABLE);
    }

    public void clearIncrementalDTRDetailsData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + INCREMENTAL_DTRDETAILS_TABLE);
    }

    public void clearLTImageData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + LT_IMAGE_TABLE);
    }

    public void clearMultiCktData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + MLT_CKT_TABLE);
    }


    public void clearData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + USERMASTER_TABLE);
        db.execSQL("DELETE FROM " + SURVEY_TABLE);
        db.execSQL("DELETE FROM " + DTR_TABLE);
        db.execSQL("DELETE FROM " + CMPDTR_TABLE);
        db.execSQL("DELETE FROM " + DTRSURVEY_TABLE);
        db.execSQL("DELETE FROM " + DTRDETAILS_TABLE);
        db.execSQL("DELETE FROM " + INCREMENTALSURVEY_TABLE);
        db.execSQL("DELETE FROM " + INCREMENTAL_DTR_TABLE);
        db.execSQL("DELETE FROM " + INCREMENTAL_DTRDETAILS_TABLE);
        db.execSQL("DELETE FROM " + INCREMENTAL_DTRSURVEY_TABLE);
        db.execSQL("DELETE FROM " + WO_STATUS_TABLE);
        db.execSQL("DELETE FROM " + SURVEY_TABLE_REPEAT);
        db.execSQL("DELETE FROM " + LT_IMAGE_TABLE);
        db.execSQL("DELETE FROM " + MLT_CKT_TABLE);
        db.execSQL("DELETE FROM " + SURVEY_TABLE_LAT_LANG);
        db.execSQL("DELETE FROM " + SURVEY_TABLE_POLE_DETAILS_EDIT);
        db.execSQL("DELETE FROM " + SURVEY_TABLE_SOURCE_POLE_EDIT);


    }


    public List<String> getDTRCode() {
        List<String> stringList = new ArrayList<>();
        // Select All Query
        // String selectQuery = "SELECT DISTINCT DTR_CODE|| '-' ||DTR_LOCATION FROM DTR_TABLE WHERE DTR_CODE IS NOT NULL";
        String selectQuery = "SELECT DISTINCT DTR_CODE || '-' || IFNULL(DTR_LOCATION, 'null') FROM DTR_TABLE WHERE DTR_CODE IS NOT NULL";

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

    public List<String> getDTRCodeLT() {
        List<String> stringList = new ArrayList<>();
        // Select All Query
        // String selectQuery = "SELECT DISTINCT DTR_CODE|| '-' ||DTR_LOCATION FROM DTR_TABLE WHERE LT_CKTCODE IS NOT NULL";
        String selectQuery = "SELECT DISTINCT DTR_CODE || '-' || IFNULL(DTR_LOCATION, 'null') FROM DTR_TABLE WHERE DTR_CODE IS NOT NULL";


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


    public List<String> getIncrementalDTRCode() {
        List<String> stringList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT DISTINCT DTR_CODE|| '-' ||DTR_LOCATION FROM INCREMENTAL_DTR_TABLE WHERE DTR_CODE IS NOT NULL";


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

    public List<DistandeVo> getDistance() {
        List<DistandeVo> distandeVoArrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  POLE_NO,LAT,LAN  FROM SURVEY_TABLE  WHERE ROWID IN(SELECT MAX(ROWID) FROM SURVEY_TABLE  WHERE POLE_NO=(\n" +
                "SELECT  MAX(POLE_NO) FROM SURVEY_TABLE)) ";


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


    public List<String> getCmpDTRCode() {
        List<String> stringList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT DISTINCT DTR_CODE|| '-' ||DTR_LOCATION FROM CMPDTR_TABLE WHERE DTR_CODE IS NOT NULL";


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
                    if (string != null) {
                        stringList.add(string);
                    }

                } while (cursor.moveToNext());

               /* while (cursor.moveToNext()){
                    String string = cursor.getString(0);
                    stringList.add(string);
                }*/
            }
        } finally {
            db.close();
        }
        // return contact list
        return stringList;
    }


    public int getObjectId() {
        int stringCount = 0;
        // Select All Query
        try {
            String selectQuery = "select MAX(POLE_NO) as Result from SURVEY_TABLE group by POLE_NO order by Result desc ";
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

    public List<String> getIncrementalFeederComplaint(String condition) {
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

    public int getIncrementalObjectId() {
        int stringCount = 0;
        // Select All Query
        try {
            String selectQuery = "select MAX(POLE_NO) as Result from INCREMENTALSURVEY_TABLE group by POLE_NO order by Result desc ";
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
            String selectQuery = "select MAX(POLE_NO) as Result from INCREMENTALSURVEY_TABLE WHERE SURVEY_FLAG='N' group by POLE_NO order by Result desc ";
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

    public int getObjectIdCount() {
        int stringCount = 0;
        // Select All Query
        try {
            String selectQuery = "select MAX(POLE_NO) as Result from SURVEY_TABLE WHERE SURVEY_FLAG='N' group by POLE_NO order by Result desc ";
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

    public boolean addUserMasterData(LoginDataResponse loginDataResponse) {
        boolean status = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ROLE, loginDataResponse.getROLE());
        values.put(OFFICE_ID, loginDataResponse.getOFFICE_ID());
        values.put(SECTION_NAME, loginDataResponse.getSECTION_NAME());
        values.put(SEC_CODE, loginDataResponse.getSEC_CODE());
        values.put(OFFICE_TYPE, loginDataResponse.getOFFICE_TYPE());
        values.put(CIRCLE, loginDataResponse.getCIRCLE());
        values.put(USERID, loginDataResponse.getUSERID());
        values.put(DIV_ID, loginDataResponse.getDIV_ID());
        values.put(ACCESS_TOKEN, loginDataResponse.getACCESS_TOKEN());
        values.put(REFRESH_TOKEN, loginDataResponse.getREFRESH_TOKEN());
        values.put(NAME, loginDataResponse.getNAME());
        try {
            long a = db.insert(USERMASTER_TABLE, null, values);
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

    public List<LoginDataResponse> getUserMasterData() {
        ArrayList<LoginDataResponse> loginDataResponses = null;
        loginDataResponses = new ArrayList<LoginDataResponse>();
        String query = "SELECT * FROM USERMASTER_TABLE";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    LoginDataResponse loginVo = new LoginDataResponse();
                    loginVo.setROLE(cursor.getString(0));
                    loginVo.setOFFICE_ID(cursor.getString(1));
                    loginVo.setSECTION_NAME(cursor.getString(2));
                    loginVo.setSEC_CODE(cursor.getString(3));
                    loginVo.setOFFICE_TYPE(cursor.getString(4));
                    loginVo.setCIRCLE(cursor.getString(5));
                    loginVo.setUSERID(cursor.getString(6));
                    loginVo.setDIV_ID(cursor.getString(7));
                    loginVo.setACCESS_TOKEN(cursor.getString(8));
                    loginVo.setREFRESH_TOKEN(cursor.getString(9));
                    loginVo.setNAME(cursor.getString(10));
                    loginDataResponses.add(loginVo);
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return loginDataResponses;
    }


    public boolean addUDTRDetailsData(DTRDetailsResponseVo dtrDetailsResponseVo) {
        boolean status = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DTR_CODE, dtrDetailsResponseVo.getDTR_CODE());
        values.put(DTR_PHASE, dtrDetailsResponseVo.getDTR_PHASE());
        values.put(DTR_SNO, dtrDetailsResponseVo.getDTR_SNO());
        values.put(DTR_CAPACITY, dtrDetailsResponseVo.getDTR_CAPACITY());
        values.put(DTR_MAKE, dtrDetailsResponseVo.getDTR_MAKE());
        values.put(TYPE_OF_LOAD, dtrDetailsResponseVo.getTYPE_OF_LOAD());
        values.put(GLAT, dtrDetailsResponseVo.getGLAT());
        values.put(GLONG, dtrDetailsResponseVo.getGLONG());
        try {
            long a = db.insert(DTRDETAILS_TABLE, null, values);
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

    public List<DTRDetailsResponseVo> getDTRDetailsData() {
        ArrayList<DTRDetailsResponseVo> dtrDetailsResponseVos = null;
        dtrDetailsResponseVos = new ArrayList<DTRDetailsResponseVo>();
        String query = "SELECT * FROM DTRDETAILS_TABLE";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    DTRDetailsResponseVo dtrDetailsResponseVo = new DTRDetailsResponseVo();
                    dtrDetailsResponseVo.setDTR_CODE(cursor.getString(0));
                    dtrDetailsResponseVo.setDTR_PHASE(cursor.getString(1));
                    dtrDetailsResponseVo.setDTR_SNO(cursor.getString(2));
                    dtrDetailsResponseVo.setDTR_CAPACITY(cursor.getString(3));
                    dtrDetailsResponseVo.setDTR_MAKE(cursor.getString(4));
                    dtrDetailsResponseVo.setTYPE_OF_LOAD(cursor.getString(5));
                    dtrDetailsResponseVo.setGLAT(cursor.getString(6));
                    dtrDetailsResponseVo.setGLONG(cursor.getString(7));
                    dtrDetailsResponseVos.add(dtrDetailsResponseVo);
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return dtrDetailsResponseVos;
    }


    public boolean addDTRData(LTCktsResponseDataVo cktsResponseDataVo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put(DTR_CODE, cktsResponseDataVo.getDTR_CODE());
            values.put(DTR_PHASE, cktsResponseDataVo.getDTR_PHASE());
            values.put(DTR_LOCATION, cktsResponseDataVo.getDTR_LOCATION());
            values.put(LT_CKTCODE, cktsResponseDataVo.getLT_CKTCODE().isEmpty() ? "" : cktsResponseDataVo.getLT_CKTCODE());
            values.put(FEEDER_ID, cktsResponseDataVo.getFEEDER_ID());

        } catch (Exception e) {
            e.printStackTrace();
        }
        long a = db.insert(DTR_TABLE, null, values);
        if (a == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }


    public List<LTCktsResponseDataVo> getLT_CKT_data(String DTR_CODE) {
        List<LTCktsResponseDataVo> ltCktsResponseDataVosList = new ArrayList<>();

        String query;
        query = "SELECT * FROM DTR_TABLE WHERE DTR_CODE = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;

        try {
            if (query.contains("?")) {
                cursor = db.rawQuery(query, new String[]{DTR_CODE});
            } else {
                cursor = db.rawQuery(query, null);
            }

            if (cursor.moveToFirst()) {
                do {
                    LTCktsResponseDataVo cktsResponseDataVo = new LTCktsResponseDataVo();
                    cktsResponseDataVo.setDTR_CODE(cursor.getString(0));
                    cktsResponseDataVo.setDTR_PHASE(cursor.getString(1));
                    cktsResponseDataVo.setDTR_LOCATION(cursor.getString(2));
                    cktsResponseDataVo.setLT_CKTCODE(cursor.getString(3));
                    cktsResponseDataVo.setFEEDER_ID(cursor.getString(4));
                    ltCktsResponseDataVosList.add(cktsResponseDataVo);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return ltCktsResponseDataVosList;
    }


    public List<LTCktsResponseDataVo> getDTRMasterData(String strCon) {
        List<LTCktsResponseDataVo> ltCktsResponseDataVosList = null;
        ltCktsResponseDataVosList = new ArrayList<>();
        String query = "";
        if (strCon != null && !"".equals(strCon)) {
            query = strCon;
        } else {
            query = "SELECT * FROM DTR_TABLE";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    LTCktsResponseDataVo cktsResponseDataVo = new LTCktsResponseDataVo();
                    // loginVo.setROLE(cursor.getString(0));
                    cktsResponseDataVo.setDTR_CODE(cursor.getString(0));
                    cktsResponseDataVo.setDTR_PHASE(cursor.getString(1));
                    cktsResponseDataVo.setDTR_LOCATION(cursor.getString(2));
                    cktsResponseDataVo.setLT_CKTCODE(cursor.getString(3));
                    cktsResponseDataVo.setFEEDER_ID(cursor.getString(4));
                    ltCktsResponseDataVosList.add(cktsResponseDataVo);
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return ltCktsResponseDataVosList;
    }


    public boolean addCmpDTRData(LTCktsResponseDataVo cktsResponseDataVo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put(DTR_CODE, cktsResponseDataVo.getDTR_CODE());
            values.put(DTR_PHASE, cktsResponseDataVo.getDTR_PHASE());
            values.put(DTR_LOCATION, cktsResponseDataVo.getDTR_LOCATION());
            values.put(LT_CKTCODE, cktsResponseDataVo.getLT_CKTCODE());
            values.put(FEEDER_ID, cktsResponseDataVo.getFEEDER_ID());

        } catch (Exception e) {
            e.printStackTrace();
        }
        long a = db.insert(CMPDTR_TABLE, null, values);
        if (a == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }

    public List<LTCktsResponseDataVo> getCmpDTRMasterData() {
        List<LTCktsResponseDataVo> ltCktsResponseDataVosList = null;
        ltCktsResponseDataVosList = new ArrayList<>();
        String query = "SELECT * FROM CMPDTR_TABLE";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    LTCktsResponseDataVo cktsResponseDataVo = new LTCktsResponseDataVo();
                    // loginVo.setROLE(cursor.getString(0));
                    cktsResponseDataVo.setDTR_CODE(cursor.getString(0));
                    cktsResponseDataVo.setDTR_PHASE(cursor.getString(1));
                    cktsResponseDataVo.setDTR_LOCATION(cursor.getString(2));
                    cktsResponseDataVo.setLT_CKTCODE(cursor.getString(3));
                    cktsResponseDataVo.setFEEDER_ID(cursor.getString(4));
                    ltCktsResponseDataVosList.add(cktsResponseDataVo);
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return ltCktsResponseDataVosList;
    }

    public boolean addLTImageData(SurveyDataVo surveyDataVo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put(DTR_STRUCTURE, surveyDataVo.getDTR_STRUCTURE());
            values.put(LT_CIRCUIT, surveyDataVo.getLT_CIRCUIT());
            values.put(POLE_NO, surveyDataVo.getPOLE_NO());
            values.put(LT_IMAGE_1, surveyDataVo.getIMAGE_1());
            values.put(LT_IMAGE_2, surveyDataVo.getIMAGE_2());
            values.put(LT_IMAGE_3, surveyDataVo.getIMAGE_3());


        } catch (Exception e) {
            e.printStackTrace();
            // Log.v("SQLLLLLL==>", "" + e.getMessage());
        }
        long a = db.insert(LT_IMAGE_TABLE, null, values);
        if (a == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }

    public boolean addMltCktData(MultiCktVO multiCktVO) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put(VOLTAGE_LEVEL, multiCktVO.getVOLTAGE_LEVEL());
            values.put(MC_FEEDER_ID, multiCktVO.getMC_FEEDER_ID());
            values.put(MC_FEEDER_NAME, multiCktVO.getMC_FEEDER_NAME());
            values.put(MC_POLE_TYPE, multiCktVO.getMC_POLE_TYPE());
            values.put(MC_POLE_HEIGHT, multiCktVO.getMC_POLE_HEIGHT());
            values.put(MC_POLE_NO, multiCktVO.getMC_POLE_NO());
        } catch (Exception e) {
            e.printStackTrace();
            //   Log.v("SQLLLLLL==>", "" + e.getMessage());
        }
        long a = db.insert(MLT_CKT_TABLE, null, values);
        if (a == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }


    public List<MultiCktVO> getMltCktData() {
        String query = "SELECT * FROM MLT_CKT_TABLE ORDER BY MC_POLE_NO";
        ArrayList<MultiCktVO> surveyDataVoList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    MultiCktVO surveyDataVo = new MultiCktVO();
                    //surveyDataVo.setROLE(cursor.getString(0));
                    surveyDataVo.setVOLTAGE_LEVEL(cursor.getString(0));
                    surveyDataVo.setMC_FEEDER_ID(cursor.getString(1));
                    surveyDataVo.setMC_FEEDER_NAME(cursor.getString(2));
                    surveyDataVo.setMC_POLE_TYPE(cursor.getString(3));
                    surveyDataVo.setMC_POLE_HEIGHT(cursor.getString(4));
                    surveyDataVo.setMC_POLE_NO(cursor.getInt(5));


                    surveyDataVoList.add(surveyDataVo);
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return surveyDataVoList;
    }

    public boolean addSurveyData(SurveyDataVo surveyDataVo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put(DTR_STRUCTURE, surveyDataVo.getDTR_STRUCTURE());
            values.put(LT_CIRCUIT, surveyDataVo.getLT_CIRCUIT());
            values.put(POLE_NO, surveyDataVo.getPOLE_NO());
            values.put(FROM_POLE, surveyDataVo.getFROM_POLE());
            values.put(LINE_TYPE, surveyDataVo.getLINE_TYPE());
            values.put(IS_END_POLE, surveyDataVo.getIS_END_POLE());
            values.put(LINE_CONFIGURATION, surveyDataVo.getLINE_CONFIGURATION());
            values.put(IS_STREETLIGHT_AV, surveyDataVo.getIS_STREETLIGHT_AV());
            values.put(IS_SERVICES_OTHERTHAN_SL, surveyDataVo.getIS_SERVICES_OTHERTHAN_SL());
            values.put(NO_AGL_SERVICES, surveyDataVo.getNO_AGL_SERVICES());
            values.put(NO_AQUA_SERVICES, surveyDataVo.getNO_AQUA_SERVICES());
            values.put(NO_INDUSTRIAL_SERVICES, surveyDataVo.getNO_INDUSTRIAL_SERVICES());
            values.put(PIN_CUT, surveyDataVo.getPIN_CUT());
            values.put(IS_DOUBLE_FEEDING, surveyDataVo.getIS_DOUBLE_FEEDING());
            values.put(POLE_TYPE, surveyDataVo.getPOLE_TYPE());
            values.put(POLE_HEIGHT, surveyDataVo.getPOLE_HEIGHT());
            values.put(STRUT_STAY, surveyDataVo.getSTRUT_STAY());
            values.put(TAPPING_POINT, surveyDataVo.getTAPPING_POINT());
            values.put(NO_OF_SPURLINES, surveyDataVo.getNO_OF_SPURLINES());
            values.put(TAPPING_LOCATION, surveyDataVo.getTAPPING_LOCATION());
            values.put(CONDUCTOR_SIZE, surveyDataVo.getCONDUCTOR_SIZE());
            values.put(IS_MULTI_CIRCUIT, surveyDataVo.getIS_MULTI_CIRCUIT());
            values.put(NO_OF_CIRCUITS, surveyDataVo.getNO_OF_CIRCUITS());
            values.put(MULTI_VOLTAGE, surveyDataVo.getMULTI_VOLTAGE());
            values.put(NO_OF33KV_CKTS, surveyDataVo.getNO_OF33KV_CKTS());
            values.put(NO_OF11KV_CKTS, surveyDataVo.getNO_OF11KV_CKTS());
            values.put(NO_OF6_3KV_VOLTAGE_CKTS, surveyDataVo.getNO_OF6_3KV_VOLTAGE_CKTS());
            values.put(NO_OF_LT_CKTS, surveyDataVo.getNO_OF_LT_CKTS());
            values.put(POLE_PAINTING_CODE, surveyDataVo.getPOLE_PAINTING_CODE());
            values.put(REMARKS, surveyDataVo.getREMARKS());
            values.put(LAT, surveyDataVo.getLAT());
            values.put(LAN, surveyDataVo.getLAN());
            values.put(SURVEY_FLAG, surveyDataVo.getSURVEY_FLAG());
            values.put(FEEDER_ID, surveyDataVo.getFEEDER_ID());
            values.put(LOCATION_TYPE, surveyDataVo.getLOCATION_TYPE());
            values.put(SERVICE_PILLERTYPE, surveyDataVo.getSERVICE_PILLERTYPE());
            values.put(FEEDERPILLET_OUTGN_CKT, surveyDataVo.getFEEDERPILLET_OUTGN_CKT());
            values.put(CABLE_SIZE, surveyDataVo.getCABLE_SIZE());
            values.put(NO_CABLES, surveyDataVo.getNO_CABLES());
            values.put(DTR_METER_AVILABLE, surveyDataVo.getDTR_METER_AVILABLE());
            values.put(DTR_NO_OF_METERS, surveyDataVo.getDTR_NO_OF_METERS());
            values.put(IS_CROSSINGS_AV, surveyDataVo.getIS_CROSSINGS_AV());
            values.put(NOOF_6_3KV_CROSSINGS, surveyDataVo.getNOOF_6_3KV_CROSSINGS());
            values.put(NOOF_11KV_CROSSINGS, surveyDataVo.getNOOF_11KV_CROSSINGS());
            values.put(NOOF_33KV_CROSSINGS, surveyDataVo.getNOOF_33KV_CROSSINGS());
            values.put(NOOF_LT_CROSSINGS, surveyDataVo.getNOOF_LT_CROSSINGS());
            values.put(NOOF_TC_CROSSINGS, surveyDataVo.getNOOF_TC_CROSSINGS());
            values.put(NOOF_TM_CROSSINGS, surveyDataVo.getNOOF_TM_CROSSINGS());

            //NEW
            values.put(DEFFECTS_AVAILABLE, surveyDataVo.getDEFFECTS_AVAILABLE());
            values.put(POLE_DEFFECTS, surveyDataVo.getPOLE_DEFFECTS());
            values.put(CONDUCTOR_DEFFECTS, surveyDataVo.getCONDUCTOR_DEFFECTS());
            values.put(INSULATOR_DEFFECTS, surveyDataVo.getINSULATOR_DEFFECTS());
            values.put(CROSSARM_DEFFECTS, surveyDataVo.getCROSSARM_DEFFECTS());
            values.put(STRUT_DEFFECTS, surveyDataVo.getSTRUT_DEFFECTS());
            values.put(STRAY_DEFFECTS, surveyDataVo.getSTRAY_DEFFECTS());
            values.put(HTLT_CLEARANCE, surveyDataVo.getHTLT_CLEARANCE());
            values.put(INTERMEDIATE_POLE, surveyDataVo.getINTERMEDIATE_POLE());
            values.put(RESTRINGING, surveyDataVo.getRESTRINGING());
            values.put(OTHER_DEFECTS, surveyDataVo.getOTHER_DEFECTS());
            values.put(NUM_SCS, surveyDataVo.getNUM_SCS());

            //MULTI CKT
            values.put(MC_33KV_FEEDER_ID_1, surveyDataVo.getMC_33KV_FEEDER_ID_1());
            values.put(MC_33KV_FEEDER_ID_2, surveyDataVo.getMC_33KV_FEEDER_ID_2());
            values.put(MC_33KV_FEEDER_ID_3, surveyDataVo.getMC_33KV_FEEDER_ID_3());
            values.put(MC_33KV_POLE_1, surveyDataVo.getMC_33KV_POLE_1());
            values.put(MC_33KV_POLE_2, surveyDataVo.getMC_33KV_POLE_2());
            values.put(MC_33KV_POLE_3, surveyDataVo.getMC_33KV_POLE_3());

            values.put(MC_11KV_FEEDER_ID_1, surveyDataVo.getMC_11KV_FEEDER_ID_1());
            values.put(MC_11KV_FEEDER_ID_2, surveyDataVo.getMC_11KV_FEEDER_ID_2());
            values.put(MC_11KV_FEEDER_ID_3, surveyDataVo.getMC_11KV_FEEDER_ID_3());
            values.put(MC_11KV_POLE_1, surveyDataVo.getMC_11KV_POLE_1());
            values.put(MC_11KV_POLE_2, surveyDataVo.getMC_11KV_POLE_2());
            values.put(MC_11KV_POLE_3, surveyDataVo.getMC_11KV_POLE_3());

            values.put(MC_LT_FEEDER_ID_1, surveyDataVo.getMC_LT_FEEDER_ID_1());
            values.put(MC_LT_FEEDER_ID_2, surveyDataVo.getMC_LT_FEEDER_ID_2());
            values.put(MC_LT_FEEDER_ID_3, surveyDataVo.getMC_LT_FEEDER_ID_3());
            values.put(MC_LT_POLE_1, surveyDataVo.getMC_LT_POLE_1());
            values.put(MC_LT_POLE_2, surveyDataVo.getMC_LT_POLE_2());
            values.put(MC_LT_POLE_3, surveyDataVo.getMC_LT_POLE_3());
            values.put(NUM_OTHER_CABLES, surveyDataVo.getNUM_OTHER_CABLES());

            //JHC PHASE 1
            values.put(POLE_SURVEY_TYPE, surveyDataVo.getPOLE_SURVEY_TYPE());
            values.put(LINE_SURVEY_TYPE, surveyDataVo.getLINE_SURVEY_TYPE());

            values.put(POLE_CONCRETE, surveyDataVo.getPOLE_CONCRETE());

            values.put(CURRENT_LAT, surveyDataVo.getCURRENT_LAT());
            values.put(CURRENT_LONG, surveyDataVo.getCURRENT_LONG());

            //MULTI CKT NOT IN LIST
            values.put(MC_33KV_NOT_IN_LIST_1, surveyDataVo.getMC_33KV_NOT_IN_LIST_1());
            values.put(MC_33KV_NOT_IN_LIST_2, surveyDataVo.getMC_33KV_NOT_IN_LIST_2());
            values.put(MC_33KV_NOT_IN_LIST_3, surveyDataVo.getMC_33KV_NOT_IN_LIST_3());
            values.put(MC_11KV_NOT_IN_LIST_1, surveyDataVo.getMC_11KV_NOT_IN_LIST_1());
            values.put(MC_11KV_NOT_IN_LIST_2, surveyDataVo.getMC_11KV_NOT_IN_LIST_2());
            values.put(MC_11KV_NOT_IN_LIST_3, surveyDataVo.getMC_11KV_NOT_IN_LIST_3());
            values.put(MC_LT_NOT_IN_LIST_1, surveyDataVo.getMC_LT_NOT_IN_LIST_1());
            values.put(MC_LT_NOT_IN_LIST_2, surveyDataVo.getMC_LT_NOT_IN_LIST_2());
            values.put(MC_LT_NOT_IN_LIST_3, surveyDataVo.getMC_LT_NOT_IN_LIST_3());


            //ADDED ON 26-07-2024
            values.put(STL_METER, surveyDataVo.getSTL_METER());
            values.put(STL_SWITCH, surveyDataVo.getSTL_SWITCH());
            values.put(STL_SOURCE, surveyDataVo.getSTL_SOURCE());
            values.put(ID, surveyDataVo.getID());
            values.put(GPS_ACCURACY, surveyDataVo.getGPS_ACCURACY());
            values.put(RDSS_SCHEME_TYPE, surveyDataVo.getRDSS_SCHEME_TYPE());
            values.put(ADDRESS_1, surveyDataVo.getADDRESS_1());
            values.put(ADDRESS_2, surveyDataVo.getADDRESS_2());


        } catch (Exception e) {
            e.printStackTrace();
            // Log.v("SQLLLLLL==>", "" + e.getMessage());
        }
        long a = db.insert(SURVEY_TABLE, null, values);
        if (a == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }

    public List<SurveyDataVo> getLTImageData(String condition) {
        String query = "";
        ArrayList<SurveyDataVo> surveyDataVoList = null;
        surveyDataVoList = new ArrayList<SurveyDataVo>();
        if (condition != null && !condition.equals("")) {
            query = condition;
        } else {
            query = "SELECT * FROM LT_IMAGE_TABLE ORDER BY POLE_NO";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    SurveyDataVo surveyDataVo = new SurveyDataVo();
                    //surveyDataVo.setROLE(cursor.getString(0));
                    surveyDataVo.setDTR_STRUCTURE(cursor.getString(0));
                    surveyDataVo.setLT_CIRCUIT(cursor.getString(1));
                    surveyDataVo.setPOLE_NO(cursor.getInt(2));
                    surveyDataVo.setIMAGE_1(cursor.getString(3));
                    surveyDataVo.setIMAGE_2(cursor.getString(4));
                    surveyDataVo.setIMAGE_3(cursor.getString(5));


                    surveyDataVoList.add(surveyDataVo);
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return surveyDataVoList;
    }

    public List<SurveyDataVo> checkMultiCktForLT() {
        String query = "SELECT * FROM SURVEY_TABLE WHERE SURVEY_FLAG='N'";
        ArrayList<SurveyDataVo> surveyDataVoList = new ArrayList();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                SurveyDataVo surveyDataVo = new SurveyDataVo();
                surveyDataVo.setMC_33KV_FEEDER_ID_1(cursor.getString(60));
                surveyDataVo.setMC_33KV_FEEDER_ID_2(cursor.getString(61));
                surveyDataVo.setMC_33KV_FEEDER_ID_3(cursor.getString(62));
                surveyDataVo.setMC_33KV_POLE_1(cursor.getString(63));
                surveyDataVo.setMC_33KV_POLE_2(cursor.getString(64));
                surveyDataVo.setMC_33KV_POLE_3(cursor.getString(65));

                surveyDataVo.setMC_11KV_FEEDER_ID_1(cursor.getString(66));
                surveyDataVo.setMC_11KV_FEEDER_ID_2(cursor.getString(67));
                surveyDataVo.setMC_11KV_FEEDER_ID_3(cursor.getString(68));
                surveyDataVo.setMC_11KV_POLE_1(cursor.getString(69));
                surveyDataVo.setMC_11KV_POLE_2(cursor.getString(70));
                surveyDataVo.setMC_11KV_POLE_3(cursor.getString(71));

                surveyDataVo.setMC_LT_FEEDER_ID_1(cursor.getString(72));
                surveyDataVo.setMC_LT_FEEDER_ID_2(cursor.getString(73));
                surveyDataVo.setMC_LT_FEEDER_ID_3(cursor.getString(74));
                surveyDataVo.setMC_LT_POLE_1(cursor.getString(75));
                surveyDataVo.setMC_LT_POLE_2(cursor.getString(76));
                surveyDataVo.setMC_LT_POLE_3(cursor.getString(77));
                surveyDataVoList.add(surveyDataVo);
            }
            while (cursor.moveToNext());
        }
        db.close();
        return surveyDataVoList;
    }

    public List<SurveyDataVo> getSurveyData(String condition) {
        String query = "";
        ArrayList<SurveyDataVo> surveyDataVoList = null;
        surveyDataVoList = new ArrayList<SurveyDataVo>();
        if (condition != null && !condition.equals("")) {
            query = condition;
        } else {
            query = "SELECT * FROM SURVEY_TABLE ORDER BY POLE_NO";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    SurveyDataVo surveyDataVo = new SurveyDataVo();
                    //surveyDataVo.setROLE(cursor.getString(0));
                    surveyDataVo.setDTR_STRUCTURE(cursor.getString(0));
                    surveyDataVo.setLT_CIRCUIT(cursor.getString(1));
                    surveyDataVo.setPOLE_NO(cursor.getInt(2));
                    surveyDataVo.setFROM_POLE(cursor.getInt(3));
                    surveyDataVo.setLINE_TYPE(cursor.getString(4));
                    surveyDataVo.setIS_END_POLE(cursor.getString(5));
                    surveyDataVo.setLINE_CONFIGURATION(cursor.getString(6));
                    surveyDataVo.setIS_STREETLIGHT_AV(cursor.getString(7));
                    surveyDataVo.setIS_SERVICES_OTHERTHAN_SL(cursor.getString(8));
                    surveyDataVo.setNO_AGL_SERVICES(cursor.getString(9));
                    surveyDataVo.setNO_AQUA_SERVICES(cursor.getString(10));
                    surveyDataVo.setNO_INDUSTRIAL_SERVICES(cursor.getString(11));
                    surveyDataVo.setPIN_CUT(cursor.getString(12));
                    surveyDataVo.setIS_DOUBLE_FEEDING(cursor.getString(13));
                    surveyDataVo.setPOLE_TYPE(cursor.getString(14));
                    surveyDataVo.setPOLE_HEIGHT(cursor.getString(15));
                    surveyDataVo.setSTRUT_STAY(cursor.getString(16));
                    surveyDataVo.setTAPPING_POINT(cursor.getString(17));
                    surveyDataVo.setNO_OF_SPURLINES(cursor.getString(18));
                    surveyDataVo.setTAPPING_LOCATION(cursor.getString(19));
                    surveyDataVo.setCONDUCTOR_SIZE(cursor.getString(20));
                    surveyDataVo.setIS_MULTI_CIRCUIT(cursor.getString(21));
                    surveyDataVo.setNO_OF_CIRCUITS(cursor.getString(22));
                    surveyDataVo.setMULTI_VOLTAGE(cursor.getString(23));
                    surveyDataVo.setNO_OF33KV_CKTS(cursor.getString(24));
                    surveyDataVo.setNO_OF11KV_CKTS(cursor.getString(25));
                    surveyDataVo.setNO_OF6_3KV_VOLTAGE_CKTS(cursor.getString(26));
                    surveyDataVo.setNO_OF_LT_CKTS(cursor.getString(27));
                    surveyDataVo.setPOLE_PAINTING_CODE(cursor.getString(28));
                    surveyDataVo.setREMARKS(cursor.getString(29));
                    surveyDataVo.setLAT(cursor.getString(30));
                    surveyDataVo.setLAN(cursor.getString(31));
                    surveyDataVo.setSURVEY_FLAG(cursor.getString(32));
                    surveyDataVo.setFEEDER_ID(cursor.getString(33));
                    surveyDataVo.setLOCATION_TYPE(cursor.getString(34));
                    surveyDataVo.setSERVICE_PILLERTYPE(cursor.getString(35));
                    surveyDataVo.setFEEDERPILLET_OUTGN_CKT(cursor.getString(36));
                    surveyDataVo.setCABLE_SIZE(cursor.getString(37));
                    surveyDataVo.setNO_CABLES(cursor.getString(38));
                    surveyDataVo.setDTR_METER_AVILABLE(cursor.getString(39));
                    surveyDataVo.setDTR_NO_OF_METERS(cursor.getString(40));
                    surveyDataVo.setIS_CROSSINGS_AV(cursor.getString(41));
                    surveyDataVo.setNOOF_6_3KV_CROSSINGS(cursor.getString(42));
                    surveyDataVo.setNOOF_11KV_CROSSINGS(cursor.getString(43));
                    surveyDataVo.setNOOF_33KV_CROSSINGS(cursor.getString(44));
                    surveyDataVo.setNOOF_LT_CROSSINGS(cursor.getString(45));
                    surveyDataVo.setNOOF_TC_CROSSINGS(cursor.getString(46));
                    surveyDataVo.setNOOF_TM_CROSSINGS(cursor.getString(47));

                    //NEW
                    surveyDataVo.setDEFFECTS_AVAILABLE(cursor.getString(48));
                    surveyDataVo.setPOLE_DEFFECTS(cursor.getString(49));
                    surveyDataVo.setCONDUCTOR_DEFFECTS(cursor.getString(50));
                    surveyDataVo.setINSULATOR_DEFFECTS(cursor.getString(51));
                    surveyDataVo.setCROSSARM_DEFFECTS(cursor.getString(52));
                    surveyDataVo.setSTRUT_DEFFECTS(cursor.getString(53));
                    surveyDataVo.setSTRAY_DEFFECTS(cursor.getString(54));
                    surveyDataVo.setHTLT_CLEARANCE(cursor.getString(55));
                    surveyDataVo.setINTERMEDIATE_POLE(cursor.getString(56));
                    surveyDataVo.setRESTRINGING(cursor.getString(57));
                    surveyDataVo.setOTHER_DEFECTS(cursor.getString(58));
                    surveyDataVo.setNUM_SCS(cursor.getString(59));

                    //MULTI CKT
                    surveyDataVo.setMC_33KV_FEEDER_ID_1(cursor.getString(60));
                    surveyDataVo.setMC_33KV_FEEDER_ID_2(cursor.getString(61));
                    surveyDataVo.setMC_33KV_FEEDER_ID_3(cursor.getString(62));
                    surveyDataVo.setMC_33KV_POLE_1(cursor.getString(63));
                    surveyDataVo.setMC_33KV_POLE_2(cursor.getString(64));
                    surveyDataVo.setMC_33KV_POLE_3(cursor.getString(65));

                    surveyDataVo.setMC_11KV_FEEDER_ID_1(cursor.getString(66));
                    surveyDataVo.setMC_11KV_FEEDER_ID_2(cursor.getString(67));
                    surveyDataVo.setMC_11KV_FEEDER_ID_3(cursor.getString(68));
                    surveyDataVo.setMC_11KV_POLE_1(cursor.getString(69));
                    surveyDataVo.setMC_11KV_POLE_2(cursor.getString(70));
                    surveyDataVo.setMC_11KV_POLE_3(cursor.getString(71));

                    surveyDataVo.setMC_LT_FEEDER_ID_1(cursor.getString(72));
                    surveyDataVo.setMC_LT_FEEDER_ID_2(cursor.getString(73));
                    surveyDataVo.setMC_LT_FEEDER_ID_3(cursor.getString(74));
                    surveyDataVo.setMC_LT_POLE_1(cursor.getString(75));
                    surveyDataVo.setMC_LT_POLE_2(cursor.getString(76));
                    surveyDataVo.setMC_LT_POLE_3(cursor.getString(77));

                    surveyDataVo.setNUM_OTHER_CABLES(cursor.getString(78));
                    surveyDataVo.setPOLE_SURVEY_TYPE(cursor.getString(79));
                    surveyDataVo.setLINE_SURVEY_TYPE(cursor.getString(80));

                    surveyDataVo.setPOLE_CONCRETE(cursor.getString(81));

                    surveyDataVo.setCURRENT_LAT(cursor.getString(82));
                    surveyDataVo.setCURRENT_LONG(cursor.getString(83));

                    //MULTI CKT NOT IN LIST
                    surveyDataVo.setMC_33KV_NOT_IN_LIST_1(cursor.getString(84));
                    surveyDataVo.setMC_33KV_NOT_IN_LIST_2(cursor.getString(85));
                    surveyDataVo.setMC_33KV_NOT_IN_LIST_3(cursor.getString(86));
                    surveyDataVo.setMC_11KV_NOT_IN_LIST_1(cursor.getString(87));
                    surveyDataVo.setMC_11KV_NOT_IN_LIST_2(cursor.getString(88));
                    surveyDataVo.setMC_11KV_NOT_IN_LIST_3(cursor.getString(89));
                    surveyDataVo.setMC_LT_NOT_IN_LIST_1(cursor.getString(90));
                    surveyDataVo.setMC_LT_NOT_IN_LIST_2(cursor.getString(91));
                    surveyDataVo.setMC_LT_NOT_IN_LIST_3(cursor.getString(92));

                    //ADDED ON 26-07-2024
                    surveyDataVo.setSTL_METER(cursor.getString(93));
                    surveyDataVo.setSTL_SWITCH(cursor.getString(94));
                    surveyDataVo.setSTL_SOURCE(cursor.getString(95));
                    surveyDataVo.setID(cursor.getString(96));
                    surveyDataVo.setGPS_ACCURACY(cursor.getString(97));
                    surveyDataVo.setRDSS_SCHEME_TYPE(cursor.getString(98));
                    surveyDataVo.setADDRESS_1(cursor.getString(99));
                    surveyDataVo.setADDRESS_2(cursor.getString(100));

                    surveyDataVoList.add(surveyDataVo);
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return surveyDataVoList;
    }


    public List<SurveyDataVo> getOnlyRequiredData(String condition) {
        String query = "";
        ArrayList<SurveyDataVo> surveyDataVoList = null;
        surveyDataVoList = new ArrayList<SurveyDataVo>();
        if (condition != null && !condition.equals("")) {
            query = condition;
        } else {
            query = "SELECT * FROM SURVEY_TABLE ORDER BY POLE_NO";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    SurveyDataVo surveyDataVo = new SurveyDataVo();
                    surveyDataVo.setDTR_STRUCTURE(cursor.getString(0));
                    surveyDataVo.setLT_CIRCUIT(cursor.getString(1));
                    surveyDataVo.setPOLE_NO(cursor.getInt(2));
                    surveyDataVo.setLAT(cursor.getString(30));
                    surveyDataVo.setLAN(cursor.getString(31));
                    surveyDataVoList.add(surveyDataVo);
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return surveyDataVoList;
    }

    public ArrayList<SurveyDataVo> getSurveyDataByDistance(double currentLat, double currentLon) {
        ArrayList<SurveyDataVo> surveyDataVoList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM SURVEY_TABLE WHERE LAT IS NOT NULL AND LAN IS NOT NULL";
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    String latStr = cursor.getString(cursor.getColumnIndexOrThrow("LAT"));
                    String lonStr = cursor.getString(cursor.getColumnIndexOrThrow("LAN"));

                    if (latStr != null && lonStr != null && !latStr.isEmpty() && !lonStr.isEmpty()) {
                        double distance = getDistanceBetweenPoles(latStr, lonStr,
                                String.valueOf(currentLat), String.valueOf(currentLon));

                        // Only include poles within 500 meters
                        if (distance <= 50000000) {
                            SurveyDataVo surveyDataVo = new SurveyDataVo();

                            surveyDataVo.setDTR_STRUCTURE(cursor.getString(0));
                            surveyDataVo.setLT_CIRCUIT(cursor.getString(1));
                            surveyDataVo.setPOLE_NO(cursor.getInt(2));
                            surveyDataVo.setFROM_POLE(cursor.getInt(3));
                            surveyDataVo.setLINE_TYPE(cursor.getString(4));
                            surveyDataVo.setIS_END_POLE(cursor.getString(5));
                            surveyDataVo.setLINE_CONFIGURATION(cursor.getString(6));
                            surveyDataVo.setIS_STREETLIGHT_AV(cursor.getString(7));
                            surveyDataVo.setIS_SERVICES_OTHERTHAN_SL(cursor.getString(8));
                            surveyDataVo.setNO_AGL_SERVICES(cursor.getString(9));
                            surveyDataVo.setNO_AQUA_SERVICES(cursor.getString(10));
                            surveyDataVo.setNO_INDUSTRIAL_SERVICES(cursor.getString(11));
                            surveyDataVo.setPIN_CUT(cursor.getString(12));
                            surveyDataVo.setIS_DOUBLE_FEEDING(cursor.getString(13));
                            surveyDataVo.setPOLE_TYPE(cursor.getString(14));
                            surveyDataVo.setPOLE_HEIGHT(cursor.getString(15));
                            surveyDataVo.setSTRUT_STAY(cursor.getString(16));
                            surveyDataVo.setTAPPING_POINT(cursor.getString(17));
                            surveyDataVo.setNO_OF_SPURLINES(cursor.getString(18));
                            surveyDataVo.setTAPPING_LOCATION(cursor.getString(19));
                            surveyDataVo.setCONDUCTOR_SIZE(cursor.getString(20));
                            surveyDataVo.setIS_MULTI_CIRCUIT(cursor.getString(21));
                            surveyDataVo.setNO_OF_CIRCUITS(cursor.getString(22));
                            surveyDataVo.setMULTI_VOLTAGE(cursor.getString(23));
                            surveyDataVo.setNO_OF33KV_CKTS(cursor.getString(24));
                            surveyDataVo.setNO_OF11KV_CKTS(cursor.getString(25));
                            surveyDataVo.setNO_OF6_3KV_VOLTAGE_CKTS(cursor.getString(26));
                            surveyDataVo.setNO_OF_LT_CKTS(cursor.getString(27));
                            surveyDataVo.setPOLE_PAINTING_CODE(cursor.getString(28));
                            surveyDataVo.setREMARKS(cursor.getString(29));
                            surveyDataVo.setLAT(cursor.getString(30));
                            surveyDataVo.setLAN(cursor.getString(31));
                            surveyDataVo.setSURVEY_FLAG(cursor.getString(32));
                            surveyDataVo.setFEEDER_ID(cursor.getString(33));
                            surveyDataVo.setLOCATION_TYPE(cursor.getString(34));
                            surveyDataVo.setSERVICE_PILLERTYPE(cursor.getString(35));
                            surveyDataVo.setFEEDERPILLET_OUTGN_CKT(cursor.getString(36));
                            surveyDataVo.setCABLE_SIZE(cursor.getString(37));
                            surveyDataVo.setNO_CABLES(cursor.getString(38));
                            surveyDataVo.setDTR_METER_AVILABLE(cursor.getString(39));
                            surveyDataVo.setDTR_NO_OF_METERS(cursor.getString(40));
                            surveyDataVo.setIS_CROSSINGS_AV(cursor.getString(41));
                            surveyDataVo.setNOOF_6_3KV_CROSSINGS(cursor.getString(42));
                            surveyDataVo.setNOOF_11KV_CROSSINGS(cursor.getString(43));
                            surveyDataVo.setNOOF_33KV_CROSSINGS(cursor.getString(44));
                            surveyDataVo.setNOOF_LT_CROSSINGS(cursor.getString(45));
                            surveyDataVo.setNOOF_TC_CROSSINGS(cursor.getString(46));
                            surveyDataVo.setNOOF_TM_CROSSINGS(cursor.getString(47));
                            surveyDataVo.setNUM_SCS(cursor.getString(48));
                            surveyDataVo.setNUM_OTHER_CABLES(cursor.getString(49));
                            surveyDataVo.setPOLE_SURVEY_TYPE(cursor.getString(50));
                            surveyDataVo.setLINE_SURVEY_TYPE(cursor.getString(51));
                            surveyDataVo.setPOLE_CONCRETE(cursor.getString(81));

                            surveyDataVoList.add(surveyDataVo);
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

        return surveyDataVoList;
    }

    public ArrayList<SurveyDataVo> getSurveyDataByDistance(double currentLat, double currentLon, String feederId) {
        ArrayList<SurveyDataVo> surveyDataVoList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Updated query with feederId filter
            String query = "SELECT * FROM SURVEY_TABLE WHERE LAT IS NOT NULL AND LAN IS NOT NULL AND FEEDER_ID = ?";
            cursor = db.rawQuery(query, new String[]{feederId});

            if (cursor.moveToFirst()) {
                do {
                    String latStr = cursor.getString(cursor.getColumnIndexOrThrow("LAT"));
                    String lonStr = cursor.getString(cursor.getColumnIndexOrThrow("LAN"));

                    if (latStr != null && lonStr != null && !latStr.isEmpty() && !lonStr.isEmpty()) {
                        double distance = getDistanceBetweenPoles(
                                latStr,
                                lonStr,
                                String.valueOf(currentLat),
                                String.valueOf(currentLon)
                        );

                        // Only include poles within 500 meters
                        if (distance <= 50000000) {
                            SurveyDataVo surveyDataVo = new SurveyDataVo();
                            surveyDataVo.setDTR_STRUCTURE(cursor.getString(0));
                            surveyDataVo.setLT_CIRCUIT(cursor.getString(1));
                            surveyDataVo.setPOLE_NO(cursor.getInt(2));
                            surveyDataVo.setFROM_POLE(cursor.getInt(3));
                            surveyDataVo.setLINE_TYPE(cursor.getString(4));
                            surveyDataVo.setIS_END_POLE(cursor.getString(5));
                            surveyDataVo.setLINE_CONFIGURATION(cursor.getString(6));
                            surveyDataVo.setIS_STREETLIGHT_AV(cursor.getString(7));
                            surveyDataVo.setIS_SERVICES_OTHERTHAN_SL(cursor.getString(8));
                            surveyDataVo.setNO_AGL_SERVICES(cursor.getString(9));
                            surveyDataVo.setNO_AQUA_SERVICES(cursor.getString(10));
                            surveyDataVo.setNO_INDUSTRIAL_SERVICES(cursor.getString(11));
                            surveyDataVo.setPIN_CUT(cursor.getString(12));
                            surveyDataVo.setIS_DOUBLE_FEEDING(cursor.getString(13));
                            surveyDataVo.setPOLE_TYPE(cursor.getString(14));
                            surveyDataVo.setPOLE_HEIGHT(cursor.getString(15));
                            surveyDataVo.setSTRUT_STAY(cursor.getString(16));
                            surveyDataVo.setTAPPING_POINT(cursor.getString(17));
                            surveyDataVo.setNO_OF_SPURLINES(cursor.getString(18));
                            surveyDataVo.setTAPPING_LOCATION(cursor.getString(19));
                            surveyDataVo.setCONDUCTOR_SIZE(cursor.getString(20));
                            surveyDataVo.setIS_MULTI_CIRCUIT(cursor.getString(21));
                            surveyDataVo.setNO_OF_CIRCUITS(cursor.getString(22));
                            surveyDataVo.setMULTI_VOLTAGE(cursor.getString(23));
                            surveyDataVo.setNO_OF33KV_CKTS(cursor.getString(24));
                            surveyDataVo.setNO_OF11KV_CKTS(cursor.getString(25));
                            surveyDataVo.setNO_OF6_3KV_VOLTAGE_CKTS(cursor.getString(26));
                            surveyDataVo.setNO_OF_LT_CKTS(cursor.getString(27));
                            surveyDataVo.setPOLE_PAINTING_CODE(cursor.getString(28));
                            surveyDataVo.setREMARKS(cursor.getString(29));
                            surveyDataVo.setLAT(cursor.getString(30));
                            surveyDataVo.setLAN(cursor.getString(31));
                            surveyDataVo.setSURVEY_FLAG(cursor.getString(32));
                            surveyDataVo.setFEEDER_ID(cursor.getString(33));
                            surveyDataVo.setLOCATION_TYPE(cursor.getString(34));
                            surveyDataVo.setSERVICE_PILLERTYPE(cursor.getString(35));
                            surveyDataVo.setFEEDERPILLET_OUTGN_CKT(cursor.getString(36));
                            surveyDataVo.setCABLE_SIZE(cursor.getString(37));
                            surveyDataVo.setNO_CABLES(cursor.getString(38));
                            surveyDataVo.setDTR_METER_AVILABLE(cursor.getString(39));
                            surveyDataVo.setDTR_NO_OF_METERS(cursor.getString(40));
                            surveyDataVo.setIS_CROSSINGS_AV(cursor.getString(41));
                            surveyDataVo.setNOOF_6_3KV_CROSSINGS(cursor.getString(42));
                            surveyDataVo.setNOOF_11KV_CROSSINGS(cursor.getString(43));
                            surveyDataVo.setNOOF_33KV_CROSSINGS(cursor.getString(44));
                            surveyDataVo.setNOOF_LT_CROSSINGS(cursor.getString(45));
                            surveyDataVo.setNOOF_TC_CROSSINGS(cursor.getString(46));
                            surveyDataVo.setNOOF_TM_CROSSINGS(cursor.getString(47));
                            surveyDataVo.setNUM_SCS(cursor.getString(48));
                            surveyDataVo.setNUM_OTHER_CABLES(cursor.getString(49));
                            surveyDataVo.setPOLE_SURVEY_TYPE(cursor.getString(50));
                            surveyDataVo.setLINE_SURVEY_TYPE(cursor.getString(51));
                            surveyDataVo.setPOLE_CONCRETE(cursor.getString(81));
                            surveyDataVo.setID(cursor.getString(96));


                            surveyDataVoList.add(surveyDataVo);
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

        return surveyDataVoList;
    }


    public ArrayList<SurveyDataVo> getSurveyDataByPoleNo(int poleNo) {
        ArrayList<SurveyDataVo> surveyDataVoList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM SURVEY_TABLE WHERE POLE_NO = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(poleNo)});

            if (cursor.moveToFirst()) {
                do {
                    SurveyDataVo surveyDataVo = new SurveyDataVo();

                    surveyDataVo.setDTR_STRUCTURE(cursor.getString(0));
                    surveyDataVo.setLT_CIRCUIT(cursor.getString(1));
                    surveyDataVo.setPOLE_NO(cursor.getInt(2));
                    surveyDataVo.setFROM_POLE(cursor.getInt(3));
                    surveyDataVo.setLINE_TYPE(cursor.getString(4));
                    surveyDataVo.setIS_END_POLE(cursor.getString(5));
                    surveyDataVo.setLINE_CONFIGURATION(cursor.getString(6));
                    surveyDataVo.setIS_STREETLIGHT_AV(cursor.getString(7));
                    surveyDataVo.setIS_SERVICES_OTHERTHAN_SL(cursor.getString(8));
                    surveyDataVo.setNO_AGL_SERVICES(cursor.getString(9));
                    surveyDataVo.setNO_AQUA_SERVICES(cursor.getString(10));
                    surveyDataVo.setNO_INDUSTRIAL_SERVICES(cursor.getString(11));
                    surveyDataVo.setPIN_CUT(cursor.getString(12));
                    surveyDataVo.setIS_DOUBLE_FEEDING(cursor.getString(13));
                    surveyDataVo.setPOLE_TYPE(cursor.getString(14));
                    surveyDataVo.setPOLE_HEIGHT(cursor.getString(15));
                    surveyDataVo.setSTRUT_STAY(cursor.getString(16));
                    surveyDataVo.setTAPPING_POINT(cursor.getString(17));
                    surveyDataVo.setNO_OF_SPURLINES(cursor.getString(18));
                    surveyDataVo.setTAPPING_LOCATION(cursor.getString(19));
                    surveyDataVo.setCONDUCTOR_SIZE(cursor.getString(20));
                    surveyDataVo.setIS_MULTI_CIRCUIT(cursor.getString(21));
                    surveyDataVo.setNO_OF_CIRCUITS(cursor.getString(22));
                    surveyDataVo.setMULTI_VOLTAGE(cursor.getString(23));
                    surveyDataVo.setNO_OF33KV_CKTS(cursor.getString(24));
                    surveyDataVo.setNO_OF11KV_CKTS(cursor.getString(25));
                    surveyDataVo.setNO_OF6_3KV_VOLTAGE_CKTS(cursor.getString(26));
                    surveyDataVo.setNO_OF_LT_CKTS(cursor.getString(27));
                    surveyDataVo.setPOLE_PAINTING_CODE(cursor.getString(28));
                    surveyDataVo.setREMARKS(cursor.getString(29));
                    surveyDataVo.setLAT(cursor.getString(30));
                    surveyDataVo.setLAN(cursor.getString(31));
                    surveyDataVo.setSURVEY_FLAG(cursor.getString(32));
                    surveyDataVo.setFEEDER_ID(cursor.getString(33));
                    surveyDataVo.setLOCATION_TYPE(cursor.getString(34));
                    surveyDataVo.setSERVICE_PILLERTYPE(cursor.getString(35));
                    surveyDataVo.setFEEDERPILLET_OUTGN_CKT(cursor.getString(36));
                    surveyDataVo.setCABLE_SIZE(cursor.getString(37));
                    surveyDataVo.setNO_CABLES(cursor.getString(38));
                    surveyDataVo.setDTR_METER_AVILABLE(cursor.getString(39));
                    surveyDataVo.setDTR_NO_OF_METERS(cursor.getString(40));
                    surveyDataVo.setIS_CROSSINGS_AV(cursor.getString(41));
                    surveyDataVo.setNOOF_6_3KV_CROSSINGS(cursor.getString(42));
                    surveyDataVo.setNOOF_11KV_CROSSINGS(cursor.getString(43));
                    surveyDataVo.setNOOF_33KV_CROSSINGS(cursor.getString(44));
                    surveyDataVo.setNOOF_LT_CROSSINGS(cursor.getString(45));
                    surveyDataVo.setNOOF_TC_CROSSINGS(cursor.getString(46));
                    surveyDataVo.setNOOF_TM_CROSSINGS(cursor.getString(47));
                    surveyDataVo.setNUM_SCS(cursor.getString(48));
                    surveyDataVo.setNUM_OTHER_CABLES(cursor.getString(49));
                    surveyDataVo.setPOLE_SURVEY_TYPE(cursor.getString(50));
                    surveyDataVo.setLINE_SURVEY_TYPE(cursor.getString(51));
                    surveyDataVo.setPOLE_CONCRETE(cursor.getString(81));
                    surveyDataVo.setID(cursor.getString(96));

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


    public List<SurveyDataVo> getPoleLatLanData() {
        List<SurveyDataVo> surveyList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String[] columns = {POLE_NO, LAT, LAN};
            cursor = db.query(
                    SURVEY_TABLE,    // Table name
                    columns,         // Columns to return
                    null,            // WHERE clause (null = all rows)
                    null,            // WHERE arguments
                    null,            // GROUP BY
                    null,            // HAVING
                    null             // ORDER BY
            );

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    SurveyDataVo surveyVo = new SurveyDataVo();

                    // getInt or getString depending on your datatype
                    // Example: if pole_no is INT, use getInt
                    try {
                        surveyVo.setPOLE_NO(cursor.getInt(cursor.getColumnIndexOrThrow(POLE_NO)));
                    } catch (Exception e) {
                        surveyVo.setPOLE_NO(0); // default if conversion fails
                    }

                    surveyVo.setLAT(cursor.getString(cursor.getColumnIndexOrThrow(LAT)));
                    surveyVo.setLAN(cursor.getString(cursor.getColumnIndexOrThrow(LAN)));

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


    public List<SurveyDataVo> getSurveyDetailsByPoleNo(int poleNo) {
        ArrayList<SurveyDataVo> surveyVoArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase(); // use readable for select
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM SURVEY_TABLE WHERE POLE_NO = ? ORDER BY POLE_NO";
            cursor = db.rawQuery(query, new String[]{String.valueOf(poleNo)});

            if (cursor.moveToFirst()) {
                do {
                    SurveyDataVo vo = new SurveyDataVo();
                    vo.setPOLE_NO(cursor.getInt(2));
                    vo.setFROM_POLE(cursor.getInt(3));
                    vo.setLAT(cursor.getString(30));
                    vo.setLAN(cursor.getString(31));


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


    public List<SurveyDataVo> getSurveyDataByDtrCode(String dtr_code) {


        String query = "SELECT * FROM SURVEY_TABLE WHERE DTR_STRUCTURE = " + dtr_code;
        // System.out.println("QUERY FOR ORDER : "+query);
        ArrayList<SurveyDataVo> surveyDataVoList = null;
        surveyDataVoList = new ArrayList<SurveyDataVo>();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    SurveyDataVo surveyDataVo = new SurveyDataVo();
                    //surveyDataVo.setROLE(cursor.getString(0));
                    surveyDataVo.setDTR_STRUCTURE(cursor.getString(0));
                    surveyDataVo.setLT_CIRCUIT(cursor.getString(1));
                    surveyDataVo.setPOLE_NO(cursor.getInt(2));
                    surveyDataVo.setFROM_POLE(cursor.getInt(3));
                    surveyDataVo.setLINE_TYPE(cursor.getString(4));
                    surveyDataVo.setIS_END_POLE(cursor.getString(5));
                    surveyDataVo.setLINE_CONFIGURATION(cursor.getString(6));
                    surveyDataVo.setIS_STREETLIGHT_AV(cursor.getString(7));
                    surveyDataVo.setIS_SERVICES_OTHERTHAN_SL(cursor.getString(8));
                    surveyDataVo.setNO_AGL_SERVICES(cursor.getString(9));
                    surveyDataVo.setNO_AQUA_SERVICES(cursor.getString(10));
                    surveyDataVo.setNO_INDUSTRIAL_SERVICES(cursor.getString(11));
                    surveyDataVo.setPIN_CUT(cursor.getString(12));
                    surveyDataVo.setIS_DOUBLE_FEEDING(cursor.getString(13));
                    surveyDataVo.setPOLE_TYPE(cursor.getString(14));
                    surveyDataVo.setPOLE_HEIGHT(cursor.getString(15));
                    surveyDataVo.setSTRUT_STAY(cursor.getString(16));
                    surveyDataVo.setTAPPING_POINT(cursor.getString(17));
                    surveyDataVo.setNO_OF_SPURLINES(cursor.getString(18));
                    surveyDataVo.setTAPPING_LOCATION(cursor.getString(19));
                    surveyDataVo.setCONDUCTOR_SIZE(cursor.getString(20));
                    surveyDataVo.setIS_MULTI_CIRCUIT(cursor.getString(21));
                    surveyDataVo.setNO_OF_CIRCUITS(cursor.getString(22));
                    surveyDataVo.setMULTI_VOLTAGE(cursor.getString(23));
                    surveyDataVo.setNO_OF33KV_CKTS(cursor.getString(24));
                    surveyDataVo.setNO_OF11KV_CKTS(cursor.getString(25));
                    surveyDataVo.setNO_OF6_3KV_VOLTAGE_CKTS(cursor.getString(26));
                    surveyDataVo.setNO_OF_LT_CKTS(cursor.getString(27));
                    surveyDataVo.setPOLE_PAINTING_CODE(cursor.getString(28));
                    surveyDataVo.setREMARKS(cursor.getString(29));
                    surveyDataVo.setLAT(cursor.getString(30));
                    surveyDataVo.setLAN(cursor.getString(31));
                    surveyDataVo.setSURVEY_FLAG(cursor.getString(32));
                    surveyDataVo.setFEEDER_ID(cursor.getString(33));
                    surveyDataVo.setLOCATION_TYPE(cursor.getString(34));
                    surveyDataVo.setSERVICE_PILLERTYPE(cursor.getString(35));
                    surveyDataVo.setFEEDERPILLET_OUTGN_CKT(cursor.getString(36));
                    surveyDataVo.setCABLE_SIZE(cursor.getString(37));
                    surveyDataVo.setNO_CABLES(cursor.getString(38));
                    surveyDataVo.setDTR_METER_AVILABLE(cursor.getString(39));
                    surveyDataVo.setDTR_NO_OF_METERS(cursor.getString(40));
                    surveyDataVo.setIS_CROSSINGS_AV(cursor.getString(41));
                    surveyDataVo.setNOOF_6_3KV_CROSSINGS(cursor.getString(42));
                    surveyDataVo.setNOOF_11KV_CROSSINGS(cursor.getString(43));
                    surveyDataVo.setNOOF_33KV_CROSSINGS(cursor.getString(44));
                    surveyDataVo.setNOOF_LT_CROSSINGS(cursor.getString(45));
                    surveyDataVo.setNOOF_TC_CROSSINGS(cursor.getString(46));
                    surveyDataVo.setNOOF_TM_CROSSINGS(cursor.getString(47));

                    //NEW
                    surveyDataVo.setDEFFECTS_AVAILABLE(cursor.getString(48));
                    surveyDataVo.setPOLE_DEFFECTS(cursor.getString(49));
                    surveyDataVo.setCONDUCTOR_DEFFECTS(cursor.getString(50));
                    surveyDataVo.setINSULATOR_DEFFECTS(cursor.getString(51));
                    surveyDataVo.setCROSSARM_DEFFECTS(cursor.getString(52));
                    surveyDataVo.setSTRUT_DEFFECTS(cursor.getString(53));
                    surveyDataVo.setSTRAY_DEFFECTS(cursor.getString(54));
                    surveyDataVo.setHTLT_CLEARANCE(cursor.getString(55));
                    surveyDataVo.setINTERMEDIATE_POLE(cursor.getString(56));
                    surveyDataVo.setRESTRINGING(cursor.getString(57));
                    surveyDataVo.setOTHER_DEFECTS(cursor.getString(58));

                    surveyDataVoList.add(surveyDataVo);
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return surveyDataVoList;
    }


    public boolean addSurveyDatarepeat(SurveyDataVo surveyDataVo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put(DTR_STRUCTURE, surveyDataVo.getDTR_STRUCTURE());
            values.put(LT_CIRCUIT, surveyDataVo.getLT_CIRCUIT());
            values.put(POLE_NO, surveyDataVo.getPOLE_NO());
            values.put(FROM_POLE, surveyDataVo.getFROM_POLE());
            values.put(LINE_TYPE, surveyDataVo.getLINE_TYPE());
            values.put(IS_END_POLE, surveyDataVo.getIS_END_POLE());
            values.put(LINE_CONFIGURATION, surveyDataVo.getLINE_CONFIGURATION());
            values.put(IS_STREETLIGHT_AV, surveyDataVo.getIS_STREETLIGHT_AV());
            values.put(IS_SERVICES_OTHERTHAN_SL, surveyDataVo.getIS_SERVICES_OTHERTHAN_SL());
            values.put(NO_AGL_SERVICES, surveyDataVo.getNO_AGL_SERVICES());
            values.put(NO_AQUA_SERVICES, surveyDataVo.getNO_AQUA_SERVICES());
            values.put(NO_INDUSTRIAL_SERVICES, surveyDataVo.getNO_INDUSTRIAL_SERVICES());
            values.put(PIN_CUT, surveyDataVo.getPIN_CUT());
            values.put(IS_DOUBLE_FEEDING, surveyDataVo.getIS_DOUBLE_FEEDING());
            values.put(POLE_TYPE, surveyDataVo.getPOLE_TYPE());
            values.put(POLE_HEIGHT, surveyDataVo.getPOLE_HEIGHT());
            values.put(STRUT_STAY, surveyDataVo.getSTRUT_STAY());
            values.put(TAPPING_POINT, surveyDataVo.getTAPPING_POINT());
            values.put(NO_OF_SPURLINES, surveyDataVo.getNO_OF_SPURLINES());
            values.put(TAPPING_LOCATION, surveyDataVo.getTAPPING_LOCATION());
            values.put(CONDUCTOR_SIZE, surveyDataVo.getCONDUCTOR_SIZE());
            values.put(IS_MULTI_CIRCUIT, surveyDataVo.getIS_MULTI_CIRCUIT());
            values.put(NO_OF_CIRCUITS, surveyDataVo.getNO_OF_CIRCUITS());
            values.put(MULTI_VOLTAGE, surveyDataVo.getMULTI_VOLTAGE());
            values.put(NO_OF33KV_CKTS, surveyDataVo.getNO_OF33KV_CKTS());
            values.put(NO_OF11KV_CKTS, surveyDataVo.getNO_OF11KV_CKTS());
            values.put(NO_OF6_3KV_VOLTAGE_CKTS, surveyDataVo.getNO_OF6_3KV_VOLTAGE_CKTS());
            values.put(NO_OF_LT_CKTS, surveyDataVo.getNO_OF_LT_CKTS());
            values.put(POLE_PAINTING_CODE, surveyDataVo.getPOLE_PAINTING_CODE());
            values.put(REMARKS, surveyDataVo.getREMARKS());
            values.put(LAT, surveyDataVo.getLAT());
            values.put(LAN, surveyDataVo.getLAN());
            values.put(SURVEY_FLAG, surveyDataVo.getSURVEY_FLAG());
            values.put(FEEDER_ID, surveyDataVo.getFEEDER_ID());
            values.put(LOCATION_TYPE, surveyDataVo.getLOCATION_TYPE());
            values.put(SERVICE_PILLERTYPE, surveyDataVo.getSERVICE_PILLERTYPE());
            values.put(FEEDERPILLET_OUTGN_CKT, surveyDataVo.getFEEDERPILLET_OUTGN_CKT());
            values.put(CABLE_SIZE, surveyDataVo.getCABLE_SIZE());
            values.put(NO_CABLES, surveyDataVo.getNO_CABLES());
            values.put(DTR_METER_AVILABLE, surveyDataVo.getDTR_METER_AVILABLE());
            values.put(DTR_NO_OF_METERS, surveyDataVo.getDTR_NO_OF_METERS());
            values.put(IS_CROSSINGS_AV, surveyDataVo.getIS_CROSSINGS_AV());
            values.put(NOOF_6_3KV_CROSSINGS, surveyDataVo.getNOOF_6_3KV_CROSSINGS());
            values.put(NOOF_11KV_CROSSINGS, surveyDataVo.getNOOF_11KV_CROSSINGS());
            values.put(NOOF_33KV_CROSSINGS, surveyDataVo.getNOOF_33KV_CROSSINGS());
            values.put(NOOF_LT_CROSSINGS, surveyDataVo.getNOOF_LT_CROSSINGS());
            values.put(NOOF_TC_CROSSINGS, surveyDataVo.getNOOF_TC_CROSSINGS());
            values.put(NOOF_TM_CROSSINGS, surveyDataVo.getNOOF_TM_CROSSINGS());

            //NEW
           /* values.put(DEFFECTS_AVAILABLE,surveyDataVo.getDEFFECTS_AVAILABLE());
            values.put(POLE_DEFFECTS,surveyDataVo.getPOLE_DEFFECTS());
            values.put(CONDUCTOR_DEFFECTS,surveyDataVo.getCONDUCTOR_DEFFECTS());
            values.put(INSULATOR_DEFFECTS,surveyDataVo.getINSULATOR_DEFFECTS());
            values.put(CROSSARM_DEFFECTS,surveyDataVo.getCROSSARM_DEFFECTS());
            values.put(STRUT_DEFFECTS,surveyDataVo.getSTRUT_DEFFECTS());
            values.put(STRAY_DEFFECTS,surveyDataVo.getSTRAY_DEFFECTS());
            values.put(HTLT_CLEARANCE,surveyDataVo.getHTLT_CLEARANCE());
            values.put(INTERMEDIATE_POLE,surveyDataVo.getINTERMEDIATE_POLE());
            values.put(RESTRINGING,surveyDataVo.getRESTRINGING());
            values.put(OTHER_DEFECTS,surveyDataVo.getOTHER_DEFECTS());*/
            values.put(NUM_SCS, surveyDataVo.getNUM_SCS());
            values.put(NUM_OTHER_CABLES, surveyDataVo.getNUM_OTHER_CABLES());

            //JHC PHASE 1
            values.put(POLE_SURVEY_TYPE, surveyDataVo.getPOLE_SURVEY_TYPE());
            values.put(LINE_SURVEY_TYPE, surveyDataVo.getLINE_SURVEY_TYPE());

            values.put(POLE_CONCRETE, surveyDataVo.getPOLE_CONCRETE());


        } catch (Exception e) {
            e.printStackTrace();
            // Log.v("SQLLLLLL==>", "" + e.getMessage());
        }
        long a = db.insert(SURVEY_TABLE_REPEAT, null, values);
        if (a == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }

    public boolean addSurveyDataLatLan(SurveyDataVo surveyDataVo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {
            values.put(POLE_NO, surveyDataVo.getPOLE_NO());
            values.put(FEEDER_ID, surveyDataVo.getFEEDER_ID());
            values.put(LAT, surveyDataVo.getLAT());
            values.put(LAN, surveyDataVo.getLAN());
            values.put(ID, surveyDataVo.getID());

            // Check if record already exists
            String selection = POLE_NO + "=? AND " + FEEDER_ID + "=?";
            String[] selectionArgs = {
                    String.valueOf(surveyDataVo.getPOLE_NO()),
                    surveyDataVo.getFEEDER_ID()
            };

            Cursor cursor = db.query(SURVEY_TABLE_LAT_LANG, null, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                // Record exists → update
                int rows = db.update(SURVEY_TABLE_LAT_LANG, values, selection, selectionArgs);
                cursor.close();
                db.close();
                return rows > 0;
            } else {
                // Record doesn’t exist → insert
                long result = db.insert(SURVEY_TABLE_LAT_LANG, null, values);
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


    public boolean addEditPoleDetailsSurvey(SurveyDataVo surveyDataVo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {
            values.put(ID, surveyDataVo.getID());
            values.put(FEEDER_ID, surveyDataVo.getFEEDER_ID());
            values.put(POLE_TYPE, surveyDataVo.getPOLE_TYPE());
            values.put(POLE_HEIGHT, surveyDataVo.getPOLE_HEIGHT());
            values.put(STRUT_STAY, surveyDataVo.getSTRUT_STAY());
            values.put(PIN_CUT, surveyDataVo.getPIN_CUT());
            values.put(POLE_CONCRETE, surveyDataVo.getPOLE_CONCRETE());
            values.put(IS_DOUBLE_FEEDING, surveyDataVo.getIS_DOUBLE_FEEDING());
            values.put(POLE_PAINTING_CODE, surveyDataVo.getPOLE_PAINTING_CODE());
            values.put(LINE_CONFIGURATION, surveyDataVo.getLINE_CONFIGURATION());
            values.put(INSULATOR_TYPE, surveyDataVo.getTYPE_OF_INSULATOR());

            // Check if record already exists
            String selection = ID + "=? AND " + FEEDER_ID + "=?";
            String[] selectionArgs = {
                    String.valueOf(surveyDataVo.getPOLE_NO()),
                    surveyDataVo.getFEEDER_ID()
            };

            Cursor cursor = db.query(
                    SURVEY_TABLE_POLE_DETAILS_EDIT,
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
                int rows = db.update(SURVEY_TABLE_POLE_DETAILS_EDIT, values, selection, selectionArgs);
                db.close();
                return rows > 0;
            } else {
                // Insert
                long result = db.insert(SURVEY_TABLE_POLE_DETAILS_EDIT, null, values);
                db.close();
                return result != -1;
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return false;
        }
    }

    public List<SurveyDataVo> getEditPoleSurveyData() {

        String query = "";
        ArrayList<SurveyDataVo> surveyDataVoList = new ArrayList<>();


        query = "SELECT FEEDER_ID, POLE_TYPE, POLE_HEIGHT, STRUT_STAY, " +
                "IS_DOUBLE_FEEDING, POLE_PAINTING_CODE, POLE_CONCRETE, " +
                "PIN_CUT, LINE_CONFIGURATION,ID,INSULATOR_TYPE " +
                "FROM SURVEY_TABLE_POLE_DETAILS_EDIT";


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, null);


            if (cursor.moveToFirst()) {
                do {


                    SurveyDataVo surveyDataVo = new SurveyDataVo();
                    surveyDataVo.setFEEDER_ID(cursor.getString(0));
                    surveyDataVo.setPOLE_TYPE(cursor.getString(1));
                    surveyDataVo.setPOLE_HEIGHT(cursor.getString(2));
                    surveyDataVo.setSTRUT_STAY(cursor.getString(3));
                    surveyDataVo.setIS_DOUBLE_FEEDING(cursor.getString(4));
                    surveyDataVo.setPOLE_PAINTING_CODE(cursor.getString(5));
                    surveyDataVo.setPOLE_CONCRETE(cursor.getString(6));
                    surveyDataVo.setPIN_CUT(cursor.getString(7));
                    surveyDataVo.setLINE_CONFIGURATION(cursor.getString(8));
                    surveyDataVo.setID(cursor.getString(9));
                    surveyDataVo.setTYPE_OF_INSULATOR(cursor.getString(10));

//                    surveyDataVo.setPOLE_TYPE(cursor.getString(0));
//                    surveyDataVo.setPOLE_HEIGHT(cursor.getString(1));
//                    surveyDataVo.setSTRUT_STAY(cursor.getString(2));
//                    surveyDataVo.setPIN_CUT(cursor.getString(3));
//                    surveyDataVo.setPOLE_CONCRETE(cursor.getString(4));
//                    surveyDataVo.setIS_DOUBLE_FEEDING(cursor.getString(5));
//                    surveyDataVo.setPOLE_PAINTING_CODE(cursor.getString(6));
//                    surveyDataVo.setID(cursor.getString(7));
//                    surveyDataVo.setFEEDER_ID(cursor.getString(8));
//                    surveyDataVo.setLINE_CONFIGURATION(cursor.getString(9));


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


    public List<SurveyDataVo> getEditSourcePoleSurveyData() {

        String query = "";
        ArrayList<SurveyDataVo> surveyDataVoList = new ArrayList<>();


        query = "SELECT FEEDER_ID, FROM_POLE, ID " +
                "FROM SURVEY_TABLE_SOURCE_POLE_EDIT";


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, null);


            if (cursor.moveToFirst()) {
                do {
                    SurveyDataVo surveyDataVo = new SurveyDataVo();
                    surveyDataVo.setFEEDER_ID(cursor.getString(0));
                    surveyDataVo.setFROM_POLE(Integer.parseInt(cursor.getString(1)));
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


    public boolean addEditSourcePoleSurvey(SurveyDataVo surveyDataVo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {
            values.put(ID, surveyDataVo.getID());
            values.put(FEEDER_ID, surveyDataVo.getFEEDER_ID());
            values.put(FROM_POLE, surveyDataVo.getFROM_POLE());
            // Check if record already exists
            String selection = ID + "=? AND " + FEEDER_ID + "=?";
            String[] selectionArgs = {
                    String.valueOf(surveyDataVo.getPOLE_NO()),
                    surveyDataVo.getFEEDER_ID()
            };

            Cursor cursor = db.query(
                    SURVEY_TABLE_SOURCE_POLE_EDIT,
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
                int rows = db.update(SURVEY_TABLE_SOURCE_POLE_EDIT, values, selection, selectionArgs);
                db.close();
                return rows > 0;
            } else {
                // Insert
                long result = db.insert(SURVEY_TABLE_SOURCE_POLE_EDIT, null, values);
                db.close();
                return result != -1;
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return false;
        }
    }


    public List<SurveyDataVo> getAllSurveyDataLatLan() {
        List<SurveyDataVo> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.query(
                    SURVEY_TABLE_LAT_LANG,
                    null,      // all columns
                    null,      // no WHERE clause
                    null,
                    null,
                    null,
                    null       // no order
            );

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    SurveyDataVo surveyDataVo = new SurveyDataVo();

                    surveyDataVo.setPOLE_NO(cursor.getInt(cursor.getColumnIndexOrThrow(POLE_NO)));
                    surveyDataVo.setFEEDER_ID(cursor.getString(cursor.getColumnIndexOrThrow(FEEDER_ID)));
                    surveyDataVo.setLAT(cursor.getString(cursor.getColumnIndexOrThrow(LAT)));
                    surveyDataVo.setLAN(cursor.getString(cursor.getColumnIndexOrThrow(LAN)));
                    surveyDataVo.setID(cursor.getString(cursor.getColumnIndexOrThrow(ID)));

                    list.add(surveyDataVo);

                } while (cursor.moveToNext());

                cursor.close();
            }

            db.close();

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }

        return list;
    }


    public List<SurveyDataVo> getSurveyDataREPEAT(String condition) {
        String query = "";
        ArrayList<SurveyDataVo> surveyDataVoList = null;
        surveyDataVoList = new ArrayList<SurveyDataVo>();
        if (condition != null && !condition.equals("")) {
            query = condition;
        } else {
            query = "SELECT * FROM SURVEY_TABLE_REPEAT";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    SurveyDataVo surveyDataVo = new SurveyDataVo();
                    //surveyDataVo.setROLE(cursor.getString(0));
                    surveyDataVo.setDTR_STRUCTURE(cursor.getString(0));
                    surveyDataVo.setLT_CIRCUIT(cursor.getString(1));
                    surveyDataVo.setPOLE_NO(cursor.getInt(2));
                    surveyDataVo.setFROM_POLE(cursor.getInt(3));
                    surveyDataVo.setLINE_TYPE(cursor.getString(4));
                    surveyDataVo.setIS_END_POLE(cursor.getString(5));
                    surveyDataVo.setLINE_CONFIGURATION(cursor.getString(6));
                    surveyDataVo.setIS_STREETLIGHT_AV(cursor.getString(7));
                    surveyDataVo.setIS_SERVICES_OTHERTHAN_SL(cursor.getString(8));
                    surveyDataVo.setNO_AGL_SERVICES(cursor.getString(9));
                    surveyDataVo.setNO_AQUA_SERVICES(cursor.getString(10));
                    surveyDataVo.setNO_INDUSTRIAL_SERVICES(cursor.getString(11));
                    surveyDataVo.setPIN_CUT(cursor.getString(12));
                    surveyDataVo.setIS_DOUBLE_FEEDING(cursor.getString(13));
                    surveyDataVo.setPOLE_TYPE(cursor.getString(14));
                    surveyDataVo.setPOLE_HEIGHT(cursor.getString(15));
                    surveyDataVo.setSTRUT_STAY(cursor.getString(16));
                    surveyDataVo.setTAPPING_POINT(cursor.getString(17));
                    surveyDataVo.setNO_OF_SPURLINES(cursor.getString(18));
                    surveyDataVo.setTAPPING_LOCATION(cursor.getString(19));
                    surveyDataVo.setCONDUCTOR_SIZE(cursor.getString(20));
                    surveyDataVo.setIS_MULTI_CIRCUIT(cursor.getString(21));
                    surveyDataVo.setNO_OF_CIRCUITS(cursor.getString(22));
                    surveyDataVo.setMULTI_VOLTAGE(cursor.getString(23));
                    surveyDataVo.setNO_OF33KV_CKTS(cursor.getString(24));
                    surveyDataVo.setNO_OF11KV_CKTS(cursor.getString(25));
                    surveyDataVo.setNO_OF6_3KV_VOLTAGE_CKTS(cursor.getString(26));
                    surveyDataVo.setNO_OF_LT_CKTS(cursor.getString(27));
                    surveyDataVo.setPOLE_PAINTING_CODE(cursor.getString(28));
                    surveyDataVo.setREMARKS(cursor.getString(29));
                    surveyDataVo.setLAT(cursor.getString(30));
                    surveyDataVo.setLAN(cursor.getString(31));
                    surveyDataVo.setSURVEY_FLAG(cursor.getString(32));
                    surveyDataVo.setFEEDER_ID(cursor.getString(33));
                    surveyDataVo.setLOCATION_TYPE(cursor.getString(34));
                    surveyDataVo.setSERVICE_PILLERTYPE(cursor.getString(35));
                    surveyDataVo.setFEEDERPILLET_OUTGN_CKT(cursor.getString(36));
                    surveyDataVo.setCABLE_SIZE(cursor.getString(37));
                    surveyDataVo.setNO_CABLES(cursor.getString(38));
                    surveyDataVo.setDTR_METER_AVILABLE(cursor.getString(39));
                    surveyDataVo.setDTR_NO_OF_METERS(cursor.getString(40));
                    surveyDataVo.setIS_CROSSINGS_AV(cursor.getString(41));
                    surveyDataVo.setNOOF_6_3KV_CROSSINGS(cursor.getString(42));
                    surveyDataVo.setNOOF_11KV_CROSSINGS(cursor.getString(43));
                    surveyDataVo.setNOOF_33KV_CROSSINGS(cursor.getString(44));
                    surveyDataVo.setNOOF_LT_CROSSINGS(cursor.getString(45));
                    surveyDataVo.setNOOF_TC_CROSSINGS(cursor.getString(46));
                    surveyDataVo.setNOOF_TM_CROSSINGS(cursor.getString(47));
                    surveyDataVo.setNUM_SCS(cursor.getString(48));
                    surveyDataVo.setNUM_OTHER_CABLES(cursor.getString(49));
                    surveyDataVo.setPOLE_SURVEY_TYPE(cursor.getString(50));
                    surveyDataVo.setLINE_SURVEY_TYPE(cursor.getString(51));
                    surveyDataVo.setPOLE_CONCRETE(cursor.getString(81));
                    surveyDataVoList.add(surveyDataVo);
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return surveyDataVoList;
    }


    public boolean addDTRSurveyData(DTRSurveyVo dtrSurveyVo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DTR_STRUCTURE, dtrSurveyVo.getDTR_STRUCTURE());
        values.put(DTR_ABSWITCH, dtrSurveyVo.getDTR_ABSWITCH());
        values.put(DTR_HGFUSE, dtrSurveyVo.getDTR_HGFUSE());
        values.put(EARTH_PITS, dtrSurveyVo.getEARTH_PITS());
        values.put(CIPIPES, dtrSurveyVo.getCIPIPES());
        values.put(GIPIPES, dtrSurveyVo.getGIPIPES());
        values.put(DTR_METER, dtrSurveyVo.getDTR_METER());
        values.put(DTR_METER_NUM, dtrSurveyVo.getDTR_METER_NUM());
        values.put(DTR_METER_MAKE, dtrSurveyVo.getDTR_METER_MAKE());
        values.put(DTR_METER_CAPACITY, dtrSurveyVo.getDTR_METER_CAPACITY());
        values.put(DTR_METER_TYPE, dtrSurveyVo.getDTR_METER_TYPE());
        values.put(MODEM, dtrSurveyVo.getMODEM());
        values.put(MODEM_NUM, dtrSurveyVo.getMODEM_NUM());
        values.put(MODEM_MAKE, dtrSurveyVo.getMODEM_MAKE());
        values.put(BUSBAR_TYPE, dtrSurveyVo.getBUSBAR_TYPE());
        values.put(CONDUCTOR_SIZE, dtrSurveyVo.getCONDUCTOR_SIZE());
        values.put(HGFUSE_DTR, dtrSurveyVo.getHGFUSE_DTR());
        values.put(HGFUSE_DTR_CONDUCTOR_SIZE, dtrSurveyVo.getHGFUSE_DTR_CONDUCTOR_SIZE());
        values.put(DTR_POLE, dtrSurveyVo.getDTR_POLE());
        values.put(PIN, dtrSurveyVo.getPIN());
        values.put(CUT, dtrSurveyVo.getCUT());
        values.put(REMARKS, dtrSurveyVo.getREMARKS());
        values.put(LT_FUSESET, dtrSurveyVo.getLT_FUSESET());
        values.put(LAT, dtrSurveyVo.getLAT());
        values.put(LAN, dtrSurveyVo.getLAN());
        values.put(DTR_MAKE, dtrSurveyVo.getDTR_MAKE());
        values.put(DTR_SERIALNO, dtrSurveyVo.getDTR_SERIALNO());
        values.put(DTR_LOAD_TYPE, dtrSurveyVo.getDTR_LOAD_TYPE());
        values.put(DTR_PHASE, dtrSurveyVo.getDTR_PHASE());
        values.put(DTR_CAPACITY, dtrSurveyVo.getDTR_CAPACITY());
        values.put(DTR_STRUCTURE_TYPE, dtrSurveyVo.getDTR_STRUCTURE_TYPE());
        values.put(DTR_POLE_SUPPORT, dtrSurveyVo.getDTR_POLE_SUPPORT());
        values.put(NOOF_STURT, dtrSurveyVo.getNOOF_STURT());
        values.put(NOOF_STAY, dtrSurveyVo.getNOOF_STAY());
        values.put(DTR_ABSWTICH_TYPE, dtrSurveyVo.getDTR_ABSWTICH_TYPE());
        values.put(NOOF_PININSULATORS, dtrSurveyVo.getNOOF_PININSULATORS());
        values.put(NOOF_DISCINSULATORS, dtrSurveyVo.getNOOF_DISCINSULATORS());
        values.put(DTR_MTR2_NUM, dtrSurveyVo.getDTR_MTR2_NUM());
        values.put(DTR_MTR2_MAKE, dtrSurveyVo.getDTR_MTR2_MAKE());
        values.put(DTR_MTR2_CAPACITY, dtrSurveyVo.getDTR_MTR2_CAPACITY());
        values.put(DTR_MTR2_TYPE, dtrSurveyVo.getDTR_MTR2_TYPE());
        values.put(DTR_MTR3_NUM, dtrSurveyVo.getDTR_MTR3_NUM());
        values.put(DTR_MTR3_MAKE, dtrSurveyVo.getDTR_MTR3_MAKE());
        values.put(DTR_MTR3_CAPACITY, dtrSurveyVo.getDTR_MTR3_CAPACITY());
        values.put(DTR_MTR3_TYPE, dtrSurveyVo.getDTR_MTR3_TYPE());
        values.put(LT_BUSBAR, dtrSurveyVo.getLT_BUSBAR());
        values.put(LT_BUSBAR_CABLESIZE, dtrSurveyVo.getLT_BUSBAR_CABLESIZE());
        values.put(LT_BUSBAR_CONDUTORSIZE, dtrSurveyVo.getLT_BUSBAR_CONDUTORSIZE());
        values.put(NOOF_DTRMETERS, dtrSurveyVo.getNOOF_DTRMETERS());
        values.put(MODEM2, dtrSurveyVo.getMODEM2());
        values.put(MODEM2_NUM, dtrSurveyVo.getMODEM2_NUM());
        values.put(MODEM2_MAKE, dtrSurveyVo.getMODEM2_MAKE());
        values.put(MODEM3, dtrSurveyVo.getMODEM3());
        values.put(MODEM3_NUM, dtrSurveyVo.getMODEM3_NUM());
        values.put(MODEM3_MAKE, dtrSurveyVo.getMODEM3_MAKE());
        values.put(DTR_LOCATION, dtrSurveyVo.getDTR_LOCATION());
        values.put(NOOF_LTCIRCUITS, dtrSurveyVo.getNOOF_LTCIRCUITS());

        //NEW
        values.put(STRUCTURE_DEFECTS, dtrSurveyVo.getSTRUCTURE_DEFECTS());
        values.put(DTR_FENCING, dtrSurveyVo.getDTR_FENCING());
        values.put(DTR_AB_DEFECTS, dtrSurveyVo.getDTR_AB_DEFECTS());
        values.put(HG_FUSE_DEFECTS, dtrSurveyVo.getHG_FUSE_DEFECTS());
        values.put(LT_FUSE_DEFECTS, dtrSurveyVo.getLT_FUSE_DEFECTS());
        values.put(NUM_EARTH, dtrSurveyVo.getNUM_EARTH());
        values.put(NUM_LT_BUSHINGS, dtrSurveyVo.getNUM_LT_BUSHINGS());
        values.put(NUM_HT_BUSHINGS, dtrSurveyVo.getNUM_HT_BUSHINGS());
        values.put(NUM_PG_LUG, dtrSurveyVo.getNUM_PG_LUG());
        values.put(XLPE_DEFECTS, dtrSurveyVo.getXLPE_DEFECTS());
        values.put(NUM_ARRESTER, dtrSurveyVo.getNUM_ARRESTER());
        values.put(OIL_LEAK, dtrSurveyVo.getOIL_LEAK());
        values.put(DTR_OTHER_DEFECTS, dtrSurveyVo.getOTHER_DEFECTS());
        values.put(DTR_IMAGE_1, dtrSurveyVo.getIMAGE_1());
        values.put(DTR_IMAGE_2, dtrSurveyVo.getIMAGE_2());
        values.put(DTR_IMAGE_3, dtrSurveyVo.getIMAGE_3());
        values.put(SURVEY_REMARKS, dtrSurveyVo.getSURVEY_REMARKS());

        //     System.out.println("BODY EARTH IN INSERT : " + dtrSurveyVo.getBODY_EARTH());
        //    System.out.println("NEUTRAL EARTH IN INSERT : " + dtrSurveyVo.getNEUTRAL_EARTH());
        //ADDED ON 26-07-2024
        values.put(BODY_EARTH, dtrSurveyVo.getBODY_EARTH());
        values.put(NEUTRAL_EARTH, dtrSurveyVo.getNEUTRAL_EARTH());

        long a = db.insert(DTRSURVEY_TABLE, null, values);
        if (a == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }
    }

    public List<DTRSurveyVo> getDTRSurveyData() {
        String query = "";
        ArrayList<DTRSurveyVo> dtrSurveyVoArrayList = null;
        dtrSurveyVoArrayList = new ArrayList<DTRSurveyVo>();

        query = "SELECT * FROM DTRSURVEY_TABLE";

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    DTRSurveyVo dtrSurveyVo = new DTRSurveyVo();
                    //surveyDataVo.setROLE(cursor.getString(0));
                    dtrSurveyVo.setDTR_STRUCTURE(cursor.getString(0));
                    dtrSurveyVo.setDTR_ABSWITCH(cursor.getString(1));
                    dtrSurveyVo.setDTR_HGFUSE(cursor.getString(2));
                    dtrSurveyVo.setEARTH_PITS(cursor.getString(3));
                    dtrSurveyVo.setCIPIPES(cursor.getString(4));
                    dtrSurveyVo.setGIPIPES(cursor.getString(5));
                    dtrSurveyVo.setDTR_METER(cursor.getString(6));
                    dtrSurveyVo.setDTR_METER_NUM(cursor.getString(7));
                    dtrSurveyVo.setDTR_METER_MAKE(cursor.getString(8));
                    dtrSurveyVo.setDTR_METER_CAPACITY(cursor.getString(9));
                    dtrSurveyVo.setDTR_METER_TYPE(cursor.getString(10));
                    dtrSurveyVo.setMODEM(cursor.getString(11));
                    dtrSurveyVo.setMODEM_NUM(cursor.getString(12));
                    dtrSurveyVo.setMODEM_MAKE(cursor.getString(13));
                    dtrSurveyVo.setBUSBAR_TYPE(cursor.getString(14));
                    dtrSurveyVo.setCONDUCTOR_SIZE(cursor.getString(15));
                    dtrSurveyVo.setHGFUSE_DTR(cursor.getString(16));
                    dtrSurveyVo.setHGFUSE_DTR_CONDUCTOR_SIZE(cursor.getString(17));
                    dtrSurveyVo.setDTR_POLE(cursor.getString(18));
                    dtrSurveyVo.setPIN(cursor.getString(19));
                    dtrSurveyVo.setCUT(cursor.getString(20));
                    dtrSurveyVo.setREMARKS(cursor.getString(21));
                    dtrSurveyVo.setLT_FUSESET(cursor.getString(22));
                    dtrSurveyVo.setLAT(cursor.getString(23));
                    dtrSurveyVo.setLAN(cursor.getString(24));
                    dtrSurveyVo.setDTR_MAKE(cursor.getString(25));
                    dtrSurveyVo.setDTR_SERIALNO(cursor.getString(26));
                    dtrSurveyVo.setDTR_LOAD_TYPE(cursor.getString(27));
                    dtrSurveyVo.setDTR_PHASE(cursor.getString(28));
                    dtrSurveyVo.setDTR_CAPACITY(cursor.getString(29));
                    dtrSurveyVo.setDTR_STRUCTURE_TYPE(cursor.getString(30));
                    dtrSurveyVo.setDTR_POLE_SUPPORT(cursor.getString(31));
                    dtrSurveyVo.setNOOF_STURT(cursor.getString(32));
                    dtrSurveyVo.setNOOF_STAY(cursor.getString(33));
                    dtrSurveyVo.setDTR_ABSWTICH_TYPE(cursor.getString(34));
                    dtrSurveyVo.setNOOF_PININSULATORS(cursor.getString(35));
                    dtrSurveyVo.setNOOF_DISCINSULATORS(cursor.getString(36));
                    dtrSurveyVo.setDTR_MTR2_NUM(cursor.getString(37));
                    dtrSurveyVo.setDTR_MTR2_MAKE(cursor.getString(38));
                    dtrSurveyVo.setDTR_MTR2_CAPACITY(cursor.getString(39));
                    dtrSurveyVo.setDTR_MTR2_TYPE(cursor.getString(40));
                    dtrSurveyVo.setDTR_MTR3_NUM(cursor.getString(41));
                    dtrSurveyVo.setDTR_MTR3_MAKE(cursor.getString(42));
                    dtrSurveyVo.setDTR_MTR3_CAPACITY(cursor.getString(43));
                    dtrSurveyVo.setDTR_MTR3_TYPE(cursor.getString(44));
                    dtrSurveyVo.setLT_BUSBAR(cursor.getString(45));
                    dtrSurveyVo.setLT_BUSBAR_CABLESIZE(cursor.getString(46));
                    dtrSurveyVo.setLT_BUSBAR_CONDUTORSIZE(cursor.getString(47));
                    dtrSurveyVo.setNOOF_DTRMETERS(cursor.getString(48));
                    dtrSurveyVo.setMODEM2(cursor.getString(49));
                    dtrSurveyVo.setMODEM2_NUM(cursor.getString(50));
                    dtrSurveyVo.setMODEM2_MAKE(cursor.getString(51));
                    dtrSurveyVo.setMODEM3(cursor.getString(52));
                    dtrSurveyVo.setMODEM3_NUM(cursor.getString(53));
                    dtrSurveyVo.setMODEM3_MAKE(cursor.getString(54));
                    dtrSurveyVo.setDTR_LOCATION(cursor.getString(55));
                    dtrSurveyVo.setNOOF_LTCIRCUITS(cursor.getString(56));

                    //NEW
                    dtrSurveyVo.setSTRUCTURE_DEFECTS(cursor.getString(57));
                    dtrSurveyVo.setDTR_FENCING(cursor.getString(58));
                    dtrSurveyVo.setDTR_AB_DEFECTS(cursor.getString(59));
                    dtrSurveyVo.setHG_FUSE_DEFECTS(cursor.getString(60));
                    dtrSurveyVo.setLT_FUSE_DEFECTS(cursor.getString(61));
                    dtrSurveyVo.setNUM_EARTH(cursor.getString(62));
                    dtrSurveyVo.setNUM_LT_BUSHINGS(cursor.getString(63));
                    dtrSurveyVo.setNUM_HT_BUSHINGS(cursor.getString(64));
                    dtrSurveyVo.setNUM_PG_LUG(cursor.getString(65));
                    dtrSurveyVo.setXLPE_DEFECTS(cursor.getString(66));
                    dtrSurveyVo.setNUM_ARRESTER(cursor.getString(67));
                    dtrSurveyVo.setOIL_LEAK(cursor.getString(68));
                    dtrSurveyVo.setOTHER_DEFECTS(cursor.getString(69));
                    dtrSurveyVo.setIMAGE_1(cursor.getString(70));
                    dtrSurveyVo.setIMAGE_2(cursor.getString(71));
                    dtrSurveyVo.setIMAGE_3(cursor.getString(72));

                    dtrSurveyVo.setSURVEY_REMARKS(cursor.getString(73));

                    //  System.out.println("BODY EARTH IN READ : " + cursor.getString(74));
                    //  System.out.println("NEUTRAL EARTH IN INSERT : " + cursor.getString(75));
                    //ADDED ON 26-07-2024
                    dtrSurveyVo.setBODY_EARTH(cursor.getString(74));
                    dtrSurveyVo.setNEUTRAL_EARTH(cursor.getString(75));


                    dtrSurveyVoArrayList.add(dtrSurveyVo);
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return dtrSurveyVoArrayList;
    }


    //Incremental


    public boolean addDIncrementalDTRData(LTCktsDataVo ltCktsDataVo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put(DTR_CODE, ltCktsDataVo.getDTR_CODE());
            values.put(DTR_PHASE, ltCktsDataVo.getDTR_PHASE());
            values.put(DTR_LOCATION, ltCktsDataVo.getDTR_LOCATION());
            values.put(LT_CKTCODE, ltCktsDataVo.getLT_CKTCODE());
            values.put(FEEDER_ID, ltCktsDataVo.getFEEDER_ID());
            values.put(WORK_ORDERID, ltCktsDataVo.getWO_ID());
            values.put(STATUS, ltCktsDataVo.getSTATUS());
            values.put(TAPPING_POLE, ltCktsDataVo.getTAPPING_POLE());

        } catch (Exception e) {
            e.printStackTrace();
        }
        //long a = db.insert(DTR_TABLE, null, values);
        long a = db.insert(INCREMENTAL_DTR_TABLE, null, values);
        if (a == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }

    public List<LTCktsDataVo> getIncrementalDTRMasterData(String strCon) {
        List<LTCktsDataVo> ltCktsResponseDataVosList = null;
        ltCktsResponseDataVosList = new ArrayList<>();
        String query = "";
        if (strCon != null && !"".equals(strCon)) {
            query = strCon;
        } else {
            query = "SELECT * FROM INCREMENTAL_DTR_TABLE";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    LTCktsDataVo ltCktsDataVo = new LTCktsDataVo();
                    // loginVo.setROLE(cursor.getString(0));
                    ltCktsDataVo.setDTR_CODE(cursor.getString(0));
                    ltCktsDataVo.setDTR_PHASE(cursor.getString(1));
                    ltCktsDataVo.setDTR_LOCATION(cursor.getString(2));
                    ltCktsDataVo.setLT_CKTCODE(cursor.getString(3));
                    ltCktsDataVo.setFEEDER_ID(cursor.getString(4));
                    ltCktsDataVo.setWO_ID(cursor.getString(5));
                    ltCktsDataVo.setSTATUS(cursor.getString(6));
                    ltCktsDataVo.setTAPPING_POLE(cursor.getString(7));
                    ltCktsResponseDataVosList.add(ltCktsDataVo);
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return ltCktsResponseDataVosList;
    }

    public void upDate(String sQuery) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sQuery);

    }

    public boolean addIncrementalSurveyData(IncrementalSurveyDataVo incrementalSurveyDataVo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put(DTR_STRUCTURE, incrementalSurveyDataVo.getDTR_STRUCTURE());
            values.put(LT_CIRCUIT, incrementalSurveyDataVo.getLT_CIRCUIT());
            values.put(POLE_NO, incrementalSurveyDataVo.getPOLE_NO());
            values.put(FROM_POLE, incrementalSurveyDataVo.getFROM_POLE());
            values.put(LINE_TYPE, incrementalSurveyDataVo.getLINE_TYPE());
            values.put(IS_END_POLE, incrementalSurveyDataVo.getIS_END_POLE());
            values.put(LINE_CONFIGURATION, incrementalSurveyDataVo.getLINE_CONFIGURATION());
            values.put(IS_STREETLIGHT_AV, incrementalSurveyDataVo.getIS_STREETLIGHT_AV());
            values.put(IS_SERVICES_OTHERTHAN_SL, incrementalSurveyDataVo.getIS_SERVICES_OTHERTHAN_SL());
            values.put(NO_AGL_SERVICES, incrementalSurveyDataVo.getNO_AGL_SERVICES());
            values.put(NO_AQUA_SERVICES, incrementalSurveyDataVo.getNO_AQUA_SERVICES());
            values.put(NO_INDUSTRIAL_SERVICES, incrementalSurveyDataVo.getNO_INDUSTRIAL_SERVICES());
            values.put(PIN_CUT, incrementalSurveyDataVo.getPIN_CUT());
            values.put(IS_DOUBLE_FEEDING, incrementalSurveyDataVo.getIS_DOUBLE_FEEDING());
            values.put(POLE_TYPE, incrementalSurveyDataVo.getPOLE_TYPE());
            values.put(POLE_HEIGHT, incrementalSurveyDataVo.getPOLE_HEIGHT());
            values.put(STRUT_STAY, incrementalSurveyDataVo.getSTRUT_STAY());
            values.put(TAPPING_POINT, incrementalSurveyDataVo.getTAPPING_POINT());
            values.put(NO_OF_SPURLINES, incrementalSurveyDataVo.getNO_OF_SPURLINES());
            values.put(TAPPING_LOCATION, incrementalSurveyDataVo.getTAPPING_LOCATION());
            values.put(CONDUCTOR_SIZE, incrementalSurveyDataVo.getCONDUCTOR_SIZE());
            values.put(IS_MULTI_CIRCUIT, incrementalSurveyDataVo.getIS_MULTI_CIRCUIT());
            values.put(NO_OF_CIRCUITS, incrementalSurveyDataVo.getNO_OF_CIRCUITS());
            values.put(MULTI_VOLTAGE, incrementalSurveyDataVo.getMULTI_VOLTAGE());
            values.put(NO_OF33KV_CKTS, incrementalSurveyDataVo.getNO_OF33KV_CKTS());
            values.put(NO_OF11KV_CKTS, incrementalSurveyDataVo.getNO_OF11KV_CKTS());
            values.put(NO_OF6_3KV_VOLTAGE_CKTS, incrementalSurveyDataVo.getNO_OF6_3KV_VOLTAGE_CKTS());
            values.put(NO_OF_LT_CKTS, incrementalSurveyDataVo.getNO_OF_LT_CKTS());
            values.put(POLE_PAINTING_CODE, incrementalSurveyDataVo.getPOLE_PAINTING_CODE());
            values.put(REMARKS, incrementalSurveyDataVo.getREMARKS());
            values.put(LAT, incrementalSurveyDataVo.getLAT());
            values.put(LAN, incrementalSurveyDataVo.getLAN());
            values.put(SURVEY_FLAG, incrementalSurveyDataVo.getSURVEY_FLAG());
            values.put(FEEDER_ID, incrementalSurveyDataVo.getFEEDER_ID());
            values.put(LOCATION_TYPE, incrementalSurveyDataVo.getLOCATION_TYPE());
            values.put(SERVICE_PILLERTYPE, incrementalSurveyDataVo.getSERVICE_PILLERTYPE());
            values.put(FEEDERPILLET_OUTGN_CKT, incrementalSurveyDataVo.getFEEDERPILLET_OUTGN_CKT());
            values.put(CABLE_SIZE, incrementalSurveyDataVo.getCABLE_SIZE());
            values.put(NO_CABLES, incrementalSurveyDataVo.getNO_CABLES());
            values.put(DTR_METER_AVILABLE, incrementalSurveyDataVo.getDTR_METER_AVILABLE());
            values.put(DTR_NO_OF_METERS, incrementalSurveyDataVo.getDTR_NO_OF_METERS());
            values.put(IS_CROSSINGS_AV, incrementalSurveyDataVo.getIS_CROSSINGS_AV());
            values.put(NOOF_6_3KV_CROSSINGS, incrementalSurveyDataVo.getNOOF_6_3KV_CROSSINGS());
            values.put(NOOF_11KV_CROSSINGS, incrementalSurveyDataVo.getNOOF_11KV_CROSSINGS());
            values.put(NOOF_33KV_CROSSINGS, incrementalSurveyDataVo.getNOOF_33KV_CROSSINGS());
            values.put(NOOF_LT_CROSSINGS, incrementalSurveyDataVo.getNOOF_LT_CROSSINGS());
            values.put(NOOF_TC_CROSSINGS, incrementalSurveyDataVo.getNOOF_TC_CROSSINGS());
            values.put(NOOF_TM_CROSSINGS, incrementalSurveyDataVo.getNOOF_TM_CROSSINGS());
            values.put(WORK_ORDERID, incrementalSurveyDataVo.getWORK_ORDERID());
            values.put(WORKORDER_STATUS, incrementalSurveyDataVo.getWORKORDER_STATUS());
            values.put(IS_SURVEY_COMPLETED, incrementalSurveyDataVo.getIS_SURVEY_COMPLETED());

        } catch (Exception e) {
            e.printStackTrace();
            // Log.v("SQLLLLLL==>", "" + e.getMessage());
        }
        long a = db.insert(INCREMENTALSURVEY_TABLE, null, values);
        if (a == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }

    public List<IncrementalSurveyDataVo> getIncrementalSurveyData(String condition) {
        String query = "";
        ArrayList<IncrementalSurveyDataVo> IncrementalSurveyDataVo = null;
        IncrementalSurveyDataVo = new ArrayList<IncrementalSurveyDataVo>();
        if (condition != null && !condition.equals("")) {
            query = condition;
        } else {
            query = "SELECT * FROM INCREMENTALSURVEY_TABLE ORDER BY POLE_NO";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    IncrementalSurveyDataVo incrementalSurveyDataVo = new IncrementalSurveyDataVo();
                    //surveyDataVo.setROLE(cursor.getString(0));
                    incrementalSurveyDataVo.setDTR_STRUCTURE(cursor.getString(0));
                    incrementalSurveyDataVo.setLT_CIRCUIT(cursor.getString(1));
                    incrementalSurveyDataVo.setPOLE_NO(cursor.getInt(2));
                    incrementalSurveyDataVo.setFROM_POLE(cursor.getInt(3));
                    incrementalSurveyDataVo.setLINE_TYPE(cursor.getString(4));
                    incrementalSurveyDataVo.setIS_END_POLE(cursor.getString(5));
                    incrementalSurveyDataVo.setLINE_CONFIGURATION(cursor.getString(6));
                    incrementalSurveyDataVo.setIS_STREETLIGHT_AV(cursor.getString(7));
                    incrementalSurveyDataVo.setIS_SERVICES_OTHERTHAN_SL(cursor.getString(8));
                    incrementalSurveyDataVo.setNO_AGL_SERVICES(cursor.getString(9));
                    incrementalSurveyDataVo.setNO_AQUA_SERVICES(cursor.getString(10));
                    incrementalSurveyDataVo.setNO_INDUSTRIAL_SERVICES(cursor.getString(11));
                    incrementalSurveyDataVo.setPIN_CUT(cursor.getString(12));
                    incrementalSurveyDataVo.setIS_DOUBLE_FEEDING(cursor.getString(13));
                    incrementalSurveyDataVo.setPOLE_TYPE(cursor.getString(14));
                    incrementalSurveyDataVo.setPOLE_HEIGHT(cursor.getString(15));
                    incrementalSurveyDataVo.setSTRUT_STAY(cursor.getString(16));
                    incrementalSurveyDataVo.setTAPPING_POINT(cursor.getString(17));
                    incrementalSurveyDataVo.setNO_OF_SPURLINES(cursor.getString(18));
                    incrementalSurveyDataVo.setTAPPING_LOCATION(cursor.getString(19));
                    incrementalSurveyDataVo.setCONDUCTOR_SIZE(cursor.getString(20));
                    incrementalSurveyDataVo.setIS_MULTI_CIRCUIT(cursor.getString(21));
                    incrementalSurveyDataVo.setNO_OF_CIRCUITS(cursor.getString(22));
                    incrementalSurveyDataVo.setMULTI_VOLTAGE(cursor.getString(23));
                    incrementalSurveyDataVo.setNO_OF33KV_CKTS(cursor.getString(24));
                    incrementalSurveyDataVo.setNO_OF11KV_CKTS(cursor.getString(25));
                    incrementalSurveyDataVo.setNO_OF6_3KV_VOLTAGE_CKTS(cursor.getString(26));
                    incrementalSurveyDataVo.setNO_OF_LT_CKTS(cursor.getString(27));
                    incrementalSurveyDataVo.setPOLE_PAINTING_CODE(cursor.getString(28));
                    incrementalSurveyDataVo.setREMARKS(cursor.getString(29));
                    incrementalSurveyDataVo.setLAT(cursor.getString(30));
                    incrementalSurveyDataVo.setLAN(cursor.getString(31));
                    incrementalSurveyDataVo.setSURVEY_FLAG(cursor.getString(32));
                    incrementalSurveyDataVo.setFEEDER_ID(cursor.getString(33));
                    incrementalSurveyDataVo.setLOCATION_TYPE(cursor.getString(34));
                    incrementalSurveyDataVo.setSERVICE_PILLERTYPE(cursor.getString(35));
                    incrementalSurveyDataVo.setFEEDERPILLET_OUTGN_CKT(cursor.getString(36));
                    incrementalSurveyDataVo.setCABLE_SIZE(cursor.getString(37));
                    incrementalSurveyDataVo.setNO_CABLES(cursor.getString(38));
                    incrementalSurveyDataVo.setDTR_METER_AVILABLE(cursor.getString(39));
                    incrementalSurveyDataVo.setDTR_NO_OF_METERS(cursor.getString(40));
                    incrementalSurveyDataVo.setIS_CROSSINGS_AV(cursor.getString(41));
                    incrementalSurveyDataVo.setNOOF_6_3KV_CROSSINGS(cursor.getString(42));
                    incrementalSurveyDataVo.setNOOF_11KV_CROSSINGS(cursor.getString(43));
                    incrementalSurveyDataVo.setNOOF_33KV_CROSSINGS(cursor.getString(44));
                    incrementalSurveyDataVo.setNOOF_LT_CROSSINGS(cursor.getString(45));
                    incrementalSurveyDataVo.setNOOF_TC_CROSSINGS(cursor.getString(46));
                    incrementalSurveyDataVo.setNOOF_TM_CROSSINGS(cursor.getString(47));
                    incrementalSurveyDataVo.setWORK_ORDERID(cursor.getString(48));
                    incrementalSurveyDataVo.setWORKORDER_STATUS(cursor.getString(49));
                    incrementalSurveyDataVo.setIS_SURVEY_COMPLETED(cursor.getString(50));
                    IncrementalSurveyDataVo.add(incrementalSurveyDataVo);
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return IncrementalSurveyDataVo;
    }


    public boolean addIncrementalDTRDetailsData(DTRDetailsResponseVo dtrDetailsResponseVo) {
        boolean status = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DTR_CODE, dtrDetailsResponseVo.getDTR_CODE());
        values.put(DTR_PHASE, dtrDetailsResponseVo.getDTR_PHASE());
        values.put(DTR_SNO, dtrDetailsResponseVo.getDTR_SNO());
        values.put(DTR_CAPACITY, dtrDetailsResponseVo.getDTR_CAPACITY());
        values.put(DTR_MAKE, dtrDetailsResponseVo.getDTR_MAKE());
        values.put(TYPE_OF_LOAD, dtrDetailsResponseVo.getTYPE_OF_LOAD());
        try {
            long a = db.insert(DTRDETAILS_TABLE, null, values);
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

    public List<DTRDetailsResponseVo> getIncrementalDTRDetailsData() {
        ArrayList<DTRDetailsResponseVo> dtrDetailsResponseVos = null;
        dtrDetailsResponseVos = new ArrayList<DTRDetailsResponseVo>();
        String query = "SELECT * FROM DTRDETAILS_TABLE";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    DTRDetailsResponseVo dtrDetailsResponseVo = new DTRDetailsResponseVo();
                    dtrDetailsResponseVo.setDTR_CODE(cursor.getString(0));
                    dtrDetailsResponseVo.setDTR_PHASE(cursor.getString(1));
                    dtrDetailsResponseVo.setDTR_SNO(cursor.getString(2));
                    dtrDetailsResponseVo.setDTR_CAPACITY(cursor.getString(3));
                    dtrDetailsResponseVo.setDTR_MAKE(cursor.getString(4));
                    dtrDetailsResponseVo.setTYPE_OF_LOAD(cursor.getString(5));
                    dtrDetailsResponseVos.add(dtrDetailsResponseVo);
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return dtrDetailsResponseVos;
    }


    public boolean addIncrementalDTRSurveyData(IncrementalDTRSurveyVo incrementalDTRSurveyVo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DTR_STRUCTURE, incrementalDTRSurveyVo.getDTR_STRUCTURE());
        values.put(DTR_ABSWITCH, incrementalDTRSurveyVo.getDTR_ABSWITCH());
        values.put(DTR_HGFUSE, incrementalDTRSurveyVo.getDTR_HGFUSE());
        values.put(EARTH_PITS, incrementalDTRSurveyVo.getEARTH_PITS());
        values.put(CIPIPES, incrementalDTRSurveyVo.getCIPIPES());
        values.put(GIPIPES, incrementalDTRSurveyVo.getGIPIPES());
        values.put(DTR_METER, incrementalDTRSurveyVo.getDTR_METER());
        values.put(DTR_METER_NUM, incrementalDTRSurveyVo.getDTR_METER_NUM());
        values.put(DTR_METER_MAKE, incrementalDTRSurveyVo.getDTR_METER_MAKE());
        values.put(DTR_METER_CAPACITY, incrementalDTRSurveyVo.getDTR_METER_CAPACITY());
        values.put(DTR_METER_TYPE, incrementalDTRSurveyVo.getDTR_METER_TYPE());
        values.put(MODEM, incrementalDTRSurveyVo.getMODEM());
        values.put(MODEM_NUM, incrementalDTRSurveyVo.getMODEM_NUM());
        values.put(MODEM_MAKE, incrementalDTRSurveyVo.getMODEM_MAKE());
        values.put(BUSBAR_TYPE, incrementalDTRSurveyVo.getBUSBAR_TYPE());
        values.put(CONDUCTOR_SIZE, incrementalDTRSurveyVo.getCONDUCTOR_SIZE());
        values.put(HGFUSE_DTR, incrementalDTRSurveyVo.getHGFUSE_DTR());
        values.put(HGFUSE_DTR_CONDUCTOR_SIZE, incrementalDTRSurveyVo.getHGFUSE_DTR_CONDUCTOR_SIZE());
        values.put(DTR_POLE, incrementalDTRSurveyVo.getDTR_POLE());
        values.put(PIN, incrementalDTRSurveyVo.getPIN());
        values.put(CUT, incrementalDTRSurveyVo.getCUT());
        values.put(REMARKS, incrementalDTRSurveyVo.getREMARKS());
        values.put(LT_FUSESET, incrementalDTRSurveyVo.getLT_FUSESET());
        values.put(LAT, incrementalDTRSurveyVo.getLAT());
        values.put(LAN, incrementalDTRSurveyVo.getLAN());
        values.put(DTR_MAKE, incrementalDTRSurveyVo.getDTR_MAKE());
        values.put(DTR_SERIALNO, incrementalDTRSurveyVo.getDTR_SERIALNO());
        values.put(DTR_LOAD_TYPE, incrementalDTRSurveyVo.getDTR_LOAD_TYPE());
        values.put(DTR_PHASE, incrementalDTRSurveyVo.getDTR_PHASE());
        values.put(DTR_CAPACITY, incrementalDTRSurveyVo.getDTR_CAPACITY());
        values.put(DTR_STRUCTURE_TYPE, incrementalDTRSurveyVo.getDTR_STRUCTURE_TYPE());
        values.put(DTR_POLE_SUPPORT, incrementalDTRSurveyVo.getDTR_POLE_SUPPORT());
        values.put(NOOF_STURT, incrementalDTRSurveyVo.getNOOF_STURT());
        values.put(NOOF_STAY, incrementalDTRSurveyVo.getNOOF_STAY());
        values.put(DTR_ABSWTICH_TYPE, incrementalDTRSurveyVo.getDTR_ABSWTICH_TYPE());
        values.put(NOOF_PININSULATORS, incrementalDTRSurveyVo.getNOOF_PININSULATORS());
        values.put(NOOF_DISCINSULATORS, incrementalDTRSurveyVo.getNOOF_DISCINSULATORS());
        values.put(DTR_MTR2_NUM, incrementalDTRSurveyVo.getDTR_MTR2_NUM());
        values.put(DTR_MTR2_MAKE, incrementalDTRSurveyVo.getDTR_MTR2_MAKE());
        values.put(DTR_MTR2_CAPACITY, incrementalDTRSurveyVo.getDTR_MTR2_CAPACITY());
        values.put(DTR_MTR2_TYPE, incrementalDTRSurveyVo.getDTR_MTR2_TYPE());
        values.put(DTR_MTR3_NUM, incrementalDTRSurveyVo.getDTR_MTR3_NUM());
        values.put(DTR_MTR3_MAKE, incrementalDTRSurveyVo.getDTR_MTR3_MAKE());
        values.put(DTR_MTR3_CAPACITY, incrementalDTRSurveyVo.getDTR_MTR3_CAPACITY());
        values.put(DTR_MTR3_TYPE, incrementalDTRSurveyVo.getDTR_MTR3_TYPE());
        values.put(LT_BUSBAR, incrementalDTRSurveyVo.getLT_BUSBAR());
        values.put(LT_BUSBAR_CABLESIZE, incrementalDTRSurveyVo.getLT_BUSBAR_CABLESIZE());
        values.put(LT_BUSBAR_CONDUTORSIZE, incrementalDTRSurveyVo.getLT_BUSBAR_CONDUTORSIZE());
        values.put(NOOF_DTRMETERS, incrementalDTRSurveyVo.getNOOF_DTRMETERS());
        values.put(MODEM2, incrementalDTRSurveyVo.getMODEM2());
        values.put(MODEM2_NUM, incrementalDTRSurveyVo.getMODEM2_NUM());
        values.put(MODEM2_MAKE, incrementalDTRSurveyVo.getMODEM2_MAKE());
        values.put(MODEM3, incrementalDTRSurveyVo.getMODEM3());
        values.put(MODEM3_NUM, incrementalDTRSurveyVo.getMODEM3_NUM());
        values.put(MODEM3_MAKE, incrementalDTRSurveyVo.getMODEM3_MAKE());
        values.put(DTR_LOCATION, incrementalDTRSurveyVo.getDTR_LOCATION());
        values.put(NOOF_LTCIRCUITS, incrementalDTRSurveyVo.getNOOF_LTCIRCUITS());
        values.put(WORK_ORDERID, incrementalDTRSurveyVo.getWORK_ORDERID());
        values.put(WORKORDER_STATUS, incrementalDTRSurveyVo.getWORKORDER_STATUS());
        long a = db.insert(INCREMENTAL_DTRSURVEY_TABLE, null, values);
        if (a == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }
    }

    public List<IncrementalDTRSurveyVo> getIncrementalDTRSurveyData() {
        String query = "";
        ArrayList<IncrementalDTRSurveyVo> incrementalDTRSurveyVoArrayList = null;
        incrementalDTRSurveyVoArrayList = new ArrayList<IncrementalDTRSurveyVo>();

        query = "SELECT * FROM INCREMENTAL_DTRSURVEY_TABLE";

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    IncrementalDTRSurveyVo incrementalDTRSurveyVo = new IncrementalDTRSurveyVo();
                    //surveyDataVo.setROLE(cursor.getString(0));
                    incrementalDTRSurveyVo.setDTR_STRUCTURE(cursor.getString(0));
                    incrementalDTRSurveyVo.setDTR_ABSWITCH(cursor.getString(1));
                    incrementalDTRSurveyVo.setDTR_HGFUSE(cursor.getString(2));
                    incrementalDTRSurveyVo.setEARTH_PITS(cursor.getString(3));
                    incrementalDTRSurveyVo.setCIPIPES(cursor.getString(4));
                    incrementalDTRSurveyVo.setGIPIPES(cursor.getString(5));
                    incrementalDTRSurveyVo.setDTR_METER(cursor.getString(6));
                    incrementalDTRSurveyVo.setDTR_METER_NUM(cursor.getString(7));
                    incrementalDTRSurveyVo.setDTR_METER_MAKE(cursor.getString(8));
                    incrementalDTRSurveyVo.setDTR_METER_CAPACITY(cursor.getString(9));
                    incrementalDTRSurveyVo.setDTR_METER_TYPE(cursor.getString(10));
                    incrementalDTRSurveyVo.setMODEM(cursor.getString(11));
                    incrementalDTRSurveyVo.setMODEM_NUM(cursor.getString(12));
                    incrementalDTRSurveyVo.setMODEM_MAKE(cursor.getString(13));
                    incrementalDTRSurveyVo.setBUSBAR_TYPE(cursor.getString(14));
                    incrementalDTRSurveyVo.setCONDUCTOR_SIZE(cursor.getString(15));
                    incrementalDTRSurveyVo.setHGFUSE_DTR(cursor.getString(16));
                    incrementalDTRSurveyVo.setHGFUSE_DTR_CONDUCTOR_SIZE(cursor.getString(17));
                    incrementalDTRSurveyVo.setDTR_POLE(cursor.getString(18));
                    incrementalDTRSurveyVo.setPIN(cursor.getString(19));
                    incrementalDTRSurveyVo.setCUT(cursor.getString(20));
                    incrementalDTRSurveyVo.setREMARKS(cursor.getString(21));
                    incrementalDTRSurveyVo.setLT_FUSESET(cursor.getString(22));
                    incrementalDTRSurveyVo.setLAT(cursor.getString(23));
                    incrementalDTRSurveyVo.setLAN(cursor.getString(24));
                    incrementalDTRSurveyVo.setDTR_MAKE(cursor.getString(25));
                    incrementalDTRSurveyVo.setDTR_SERIALNO(cursor.getString(26));
                    incrementalDTRSurveyVo.setDTR_LOAD_TYPE(cursor.getString(27));
                    incrementalDTRSurveyVo.setDTR_PHASE(cursor.getString(28));
                    incrementalDTRSurveyVo.setDTR_CAPACITY(cursor.getString(29));
                    incrementalDTRSurveyVo.setDTR_STRUCTURE_TYPE(cursor.getString(30));
                    incrementalDTRSurveyVo.setDTR_POLE_SUPPORT(cursor.getString(31));
                    incrementalDTRSurveyVo.setNOOF_STURT(cursor.getString(32));
                    incrementalDTRSurveyVo.setNOOF_STAY(cursor.getString(33));
                    incrementalDTRSurveyVo.setDTR_ABSWTICH_TYPE(cursor.getString(34));
                    incrementalDTRSurveyVo.setNOOF_PININSULATORS(cursor.getString(35));
                    incrementalDTRSurveyVo.setNOOF_DISCINSULATORS(cursor.getString(36));
                    incrementalDTRSurveyVo.setDTR_MTR2_NUM(cursor.getString(37));
                    incrementalDTRSurveyVo.setDTR_MTR2_MAKE(cursor.getString(38));
                    incrementalDTRSurveyVo.setDTR_MTR2_CAPACITY(cursor.getString(39));
                    incrementalDTRSurveyVo.setDTR_MTR2_TYPE(cursor.getString(40));
                    incrementalDTRSurveyVo.setDTR_MTR3_NUM(cursor.getString(41));
                    incrementalDTRSurveyVo.setDTR_MTR3_MAKE(cursor.getString(42));
                    incrementalDTRSurveyVo.setDTR_MTR3_CAPACITY(cursor.getString(43));
                    incrementalDTRSurveyVo.setDTR_MTR3_TYPE(cursor.getString(44));
                    incrementalDTRSurveyVo.setLT_BUSBAR(cursor.getString(45));
                    incrementalDTRSurveyVo.setLT_BUSBAR_CABLESIZE(cursor.getString(46));
                    incrementalDTRSurveyVo.setLT_BUSBAR_CONDUTORSIZE(cursor.getString(47));
                    incrementalDTRSurveyVo.setNOOF_DTRMETERS(cursor.getString(48));
                    incrementalDTRSurveyVo.setMODEM2(cursor.getString(49));
                    incrementalDTRSurveyVo.setMODEM2_NUM(cursor.getString(50));
                    incrementalDTRSurveyVo.setMODEM2_MAKE(cursor.getString(51));
                    incrementalDTRSurveyVo.setMODEM3(cursor.getString(52));
                    incrementalDTRSurveyVo.setMODEM3_NUM(cursor.getString(53));
                    incrementalDTRSurveyVo.setMODEM3_MAKE(cursor.getString(54));
                    incrementalDTRSurveyVo.setDTR_LOCATION(cursor.getString(55));
                    incrementalDTRSurveyVo.setNOOF_LTCIRCUITS(cursor.getString(56));
                    incrementalDTRSurveyVoArrayList.add(incrementalDTRSurveyVo);
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return incrementalDTRSurveyVoArrayList;
    }

    public boolean addWoStatusMasterData(WoStatusMasterVo woStatusMasterVo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FEEDER_ID, woStatusMasterVo.getFEEDER_ID());
        values.put(WO_ID, woStatusMasterVo.getWO_ID());
        values.put(WO_STATUS, woStatusMasterVo.getWO_STATUS());
        values.put(LT_CKT, woStatusMasterVo.getLT_CKT());
        values.put(DTR_STRCODE, woStatusMasterVo.getDTR_STRCODE());
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
        ArrayList<WoStatusMasterVo> statusMasterVoArrayList = null;
        statusMasterVoArrayList = new ArrayList<WoStatusMasterVo>();
        String query = "SELECT * FROM WO_STATUS_TABLE";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    WoStatusMasterVo woStatusMasterVo = new WoStatusMasterVo();
                    woStatusMasterVo.setFEEDER_ID(cursor.getString(0));
                    woStatusMasterVo.setWO_ID(cursor.getString(1));
                    woStatusMasterVo.setWO_STATUS(cursor.getString(2));
                    woStatusMasterVo.setLT_CKT(cursor.getString(3));
                    woStatusMasterVo.setDTR_STRCODE(cursor.getString(4));
                    statusMasterVoArrayList.add(woStatusMasterVo);
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return statusMasterVoArrayList;
    }

}
