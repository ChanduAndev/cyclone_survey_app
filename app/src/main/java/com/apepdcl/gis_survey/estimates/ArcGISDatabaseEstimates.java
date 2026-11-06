package com.apepdcl.gis_survey.estimates;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

import androidx.annotation.Nullable;

import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.estimates.models.ConductorTypeModel;
import com.apepdcl.gis_survey.estimates.models.DTRCapacities;
import com.apepdcl.gis_survey.estimates.models.DTRFeederMaster;
import com.apepdcl.gis_survey.estimates.models.DTRMakeMaster;
import com.apepdcl.gis_survey.estimates.models.DTRModel;
import com.apepdcl.gis_survey.estimates.models.DTRTypesModel;
import com.apepdcl.gis_survey.estimates.models.DTR_HT_MaterialModel;
import com.apepdcl.gis_survey.estimates.models.Estimates11KVModel;
import com.apepdcl.gis_survey.estimates.models.Estimates33KVModel;
import com.apepdcl.gis_survey.estimates.models.EstimatesLTModel;
import com.apepdcl.gis_survey.estimates.models.LTLineConfigModel;
import com.apepdcl.gis_survey.estimates.models.LT_CKT_Model;
import com.apepdcl.gis_survey.estimates.models.MaterialMasterModel;
import com.apepdcl.gis_survey.estimates.models.PoleDistanceModel;
import com.apepdcl.gis_survey.estimates.models.PoleHeightModel;
import com.apepdcl.gis_survey.estimates.models.StatusModel;
import com.apepdcl.gis_survey.estimates.models.WorkInvolvementModel;
import com.apepdcl.gis_survey.estimates.new_module.NewModel;
import com.apepdcl.gis_survey.estimates.sou.EditModel;
import com.apepdcl.gis_survey.estimates.sou.MaterialPostingModel;
import com.apepdcl.gis_survey.module_33KV.vo.SurveyVo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Soubhagya Swain on 11,June,2024
 */
public class ArcGISDatabaseEstimates extends SQLiteOpenHelper {




    private static final int DATABASE_VERSION = 41; //Modified 28-08-2025
    private static final String DATABASE_NAME = "ESTIMATES_DB";

    private static final String WORK_TABLE = "WORK_TABLE";
    private static final String EST_FEEDERS = "EST_FEEDERS";

    private static final String EST_NO = "EST_NO";
    private static final String REF_ID = "REF_ID";
    private static final String WORK_TYPE = "WORK_TYPE";
    private static final String SURVEY_TYPE = "SURVEY_TYPE";
    private static final String FEEDER_NAME = "FEEDER_NAME";
    private static final String SS_DTR_MTR = "SS_DTR_MTR";
    private static final String TAPPING_LOCATION = "TAPPING_LOCATION";
    private static final String SS_DTR_MTR_QTY = "SS_DTR_MTR_QTY";
    private static final String IS_SURVEY_COMPLETED = "IS_SURVEY_COMPLETED";
    private static final String IS_33KV_SURVEY_COMPLETED = "IS_33KV_SURVEY_COMPLETED";
    private static final String IS_11KV_SURVEY_COMPLETED = "IS_11KV_SURVEY_COMPLETED";
    private static final String IS_LT_SURVEY_COMPLETED = "IS_LT_SURVEY_COMPLETED";
    private static final String IS_METERS_SURVEY_COMPLETED = "IS_METERS_SURVEY_COMPLETED";
    private static final String IS_33KV_SELECTED = "IS_33KV_SELECTED";
    private static final String IS_11KV_SELECTED = "IS_11KV_SELECTED";
    private static final String IS_LT_SELECTED = "IS_LT_SELECTED";
    private static final String FEEDER_ID = "FEEDER_ID";
    private static final String SS_ID = "SS_ID";
    private static final String SS_NAME = "SS_NAME";
    private static final String SS_TYPE = "SS_TYPE";
    private static final String CHECKED = "CHECKED";
    private static final String LT_CKT_CODE = "LT_CKT_CODE";
    private static final String FLAG = "FLAG";
    private static final String GIS_ID = "GIS_ID";
    private static final String FDR_COUNT = "FDR_COUNT";
    private static final String FDR_INDEX = "FDR_INDEX";
    private static final String HT_SCNO = "HT_SCNO";
    private static final String HTSC_NAME = "HTSC_NAME";
    private static final String PURPOSE = "PURPOSE";

    private static final String IS_11KV_FEEDER_1_SELECTED = "IS_11KV_FEEDER_1_SELECTED";
    private static final String IS_11KV_FEEDER_2_SELECTED = "IS_11KV_FEEDER_2_SELECTED";
    private static final String IS_11KV_FEEDER_3_SELECTED = "IS_11KV_FEEDER_3_SELECTED";
    private static final String IS_11KV_FEEDER_4_SELECTED = "IS_11KV_FEEDER_4_SELECTED";
    private static final String IS_11KV_FEEDER_5_SELECTED = "IS_11KV_FEEDER_5_SELECTED";

    private static final String IS_11KV_FEEDER_1_SURVEY_COMPLETED = "IS_11KV_FEEDER_1_SURVEY_COMPLETED";
    private static final String IS_11KV_FEEDER_2_SURVEY_COMPLETED = "IS_11KV_FEEDER_2_SURVEY_COMPLETED";
    private static final String IS_11KV_FEEDER_3_SURVEY_COMPLETED = "IS_11KV_FEEDER_3_SURVEY_COMPLETED";
    private static final String IS_11KV_FEEDER_4_SURVEY_COMPLETED = "IS_11KV_FEEDER_4_SURVEY_COMPLETED";
    private static final String IS_11KV_FEEDER_5_SURVEY_COMPLETED = "IS_11KV_FEEDER_5_SURVEY_COMPLETED";


    // Add these to your existing column name constants
    public static final String IS_11KV_FEEDER_6_SURVEY_COMPLETED = "IS_11KV_FEEDER_6_SURVEY_COMPLETED";
    public static final String IS_11KV_FEEDER_7_SURVEY_COMPLETED = "IS_11KV_FEEDER_7_SURVEY_COMPLETED";
    public static final String IS_11KV_FEEDER_8_SURVEY_COMPLETED = "IS_11KV_FEEDER_8_SURVEY_COMPLETED";
    public static final String IS_11KV_FEEDER_9_SURVEY_COMPLETED = "IS_11KV_FEEDER_9_SURVEY_COMPLETED";
    public static final String IS_11KV_FEEDER_10_SURVEY_COMPLETED = "IS_11KV_FEEDER_10_SURVEY_COMPLETED";

    public static final String IS_11KV_FEEDER_6_SELECTED = "IS_11KV_FEEDER_6_SELECTED";
    public static final String IS_11KV_FEEDER_7_SELECTED = "IS_11KV_FEEDER_7_SELECTED";
    public static final String IS_11KV_FEEDER_8_SELECTED = "IS_11KV_FEEDER_8_SELECTED";
    public static final String IS_11KV_FEEDER_9_SELECTED = "IS_11KV_FEEDER_9_SELECTED";
    public static final String IS_11KV_FEEDER_10_SELECTED = "IS_11KV_FEEDER_10_SELECTED";


    //NEW CODE FOR 33KV LINE EXTENSION START

    private static final String IS_33KV_FEEDER_1_SELECTED = "IS_33KV_FEEDER_1_SELECTED";
    private static final String IS_33KV_FEEDER_2_SELECTED = "IS_33KV_FEEDER_2_SELECTED";
    private static final String IS_33KV_FEEDER_3_SELECTED = "IS_33KV_FEEDER_3_SELECTED";
    private static final String IS_33KV_FEEDER_4_SELECTED = "IS_33KV_FEEDER_4_SELECTED";
    private static final String IS_33KV_FEEDER_5_SELECTED = "IS_33KV_FEEDER_5_SELECTED";

    private static final String IS_33KV_FEEDER_1_SURVEY_COMPLETED = "IS_33KV_FEEDER_1_SURVEY_COMPLETED";
    private static final String IS_33KV_FEEDER_2_SURVEY_COMPLETED = "IS_33KV_FEEDER_2_SURVEY_COMPLETED";
    private static final String IS_33KV_FEEDER_3_SURVEY_COMPLETED = "IS_33KV_FEEDER_3_SURVEY_COMPLETED";
    private static final String IS_33KV_FEEDER_4_SURVEY_COMPLETED = "IS_33KV_FEEDER_4_SURVEY_COMPLETED";
    private static final String IS_33KV_FEEDER_5_SURVEY_COMPLETED = "IS_33KV_FEEDER_5_SURVEY_COMPLETED";


    String CREATE_EST_FEEDERS = "CREATE TABLE " + EST_FEEDERS + "("
            + EST_NO + " TEXT ,"
            + REF_ID + " TEXT ,"
            + VOLTAGE_LEVEL + " TEXT ,"
            + FEEDER_ID + " TEXT ,"
            + FEEDER_NAME + " TEXT ,"
            + SURVEY_TYPE + " TEXT ,"
            + FDR_COUNT + " TEXT ,"
            + SS_ID + " TEXT ,"
            + SS_NAME + " TEXT ,"
            + SS_TYPE + " TEXT ,"
            + FDR_INDEX + " TEXT ,"


            + FLAG + " TEXT " + ")";


    String CREATE_WORK_TABLE = "CREATE TABLE " + WORK_TABLE + "("
            + EST_NO + " TEXT ,"
            + REF_ID + " TEXT ,"
            + WORK_TYPE + " TEXT ,"
            + SURVEY_TYPE + " TEXT ,"
            + FEEDER_NAME + " TEXT ,"
            + SS_DTR_MTR + " TEXT ,"
            + TAPPING_LOCATION + " TEXT ,"
            + SS_DTR_MTR_QTY + " TEXT ,"
            + IS_SURVEY_COMPLETED + " TEXT ,"
            + IS_33KV_SURVEY_COMPLETED + " TEXT ,"
            + IS_11KV_SURVEY_COMPLETED + " TEXT ,"
            + IS_LT_SURVEY_COMPLETED + " TEXT ,"
            + IS_11KV_SELECTED + " TEXT ,"
            + IS_33KV_SELECTED + " TEXT ,"
            + IS_LT_SELECTED + " TEXT ,"
            + FEEDER_ID + " TEXT ,"
            + SS_ID + " TEXT ,"
            + SS_NAME + " TEXT ,"
            + SS_TYPE + " TEXT ,"
            + CHECKED + " TEXT ,"
            + GIS_ID + " TEXT ,"
            + LT_CKT_CODE + " TEXT ,"

            + FLAG + " TEXT " + ")";


    //33KV
    private static final String SURVEY_TABLE_33KV = "SURVEY_TABLE_33KV";
    private static final String EXISTING_POLES_TABLE_33KV = "EXISTING_POLES_TABLE_33KV";
    private static final String EST_NO_33KV = "EST_NO_33KV";
    private static final String FROM_POLE_33KV = "FROM_POLE_33KV";
    private static final String POLE_NO_33KV = "POLE_NO_33KV";
    private static final String LINE_TYPE_33KV = "LINE_TYPE_33KV";
    private static final String No_OF_STRUT_33KV = "No_OF_STRUT_33KV";
    private static final String No_OF_STAY_33KV = "No_OF_STAY_33KV";
    private static final String EXI_POLE_NO_33KV = "EXI_POLE_NO_33KV";
    private static final String POLE_LOCATION_TYPE_33KV = "POLE_LOCATION_TYPE_33KV";
    private static final String POLE_HEIGHT_33KV = "POLE_HEIGHT_33KV";
    private static final String CONDUTCOR_SIZE_33KV = "CONDUTCOR_SIZE_33KV";
    private static final String LINE_SURVEY_TYPE_33KV = "LINE_SURVEY_TYPE_33KV";
    private static final String DTR_PHASE_33KV = "DTR_PHASE_33KV";
    private static final String DTR_CAPACITY_33KV = "DTR_CAPACITY_33KV";
    private static final String DTR_MOUNTING_33KV = "DTR_MOUNTING_33KV";
    private static final String DTR_SURVEY_TYPE_33KV = "DTR_SURVEY_TYPE_33KV";
    private static final String LOCATION_ID_33KV = "LOCATION_ID_33KV";
    private static final String NUMB_9M_STRUTS_33KV = "NUMB_9M_STRUTS_33KV";
    private static final String NUMB_8M_STRUTS_33KV = "NUMB_8M_STRUTS_33KV";
    private static final String NUMB_STAYS_33KV = "NUMB_STAYS_33KV";
    private static final String NUMBERS_33KV_FEEDERS = "NUMBERS_33KV_FEEDERS";
    private static final String DTR_STRUCTURE_CODE_33KV = "DTR_STRUCTURE_CODE_33KV";
    private static final String POLE_DESC_33KV = "POLE_DESC_33KV";
    private static final String POLE_SAP_CODE_33KV = "POLE_SAP_CODE_33KV";
    private static final String CONDUCTOR_DESC_33KV = "CONDUCTOR_DESC_33KV";
    private static final String CONDUCTOR_SAP_CODE_33KV = "CONDUCTOR_SAP_CODE_33KV";
    private static final String AB_SWITCH_33KV = "AB_SWITCH_33KV";
    private static final String AB_DESC_33KV = "AB_DESC_33KV";
    private static final String SS_ID_33KV = "SS_ID_33KV";
    private static final String SS_NAME_33KV = "SS_NAME_33KV";
    private static final String SS_TYPE_33KV = "SS_TYPE_33KV";
    private static final String FEEDER_ID_33KV = "FEEDER_ID_33KV";
    private static final String FEEDER_NAME_33KV = "FEEDER_NAME_33KV";
    private static final String COUNT_TAPPING_XARM_33KV = "COUNT_TAPPING_XARM_33KV";
    private static final String COUNT_V_XARM_33KV = "COUNT_V_XARM_33KV";
    private static final String COUNT_TOP_CLEAT_33KV = "COUNT_TOP_CLEAT_33KV";
    private static final String COUNT_PIN_INS_33KV = "COUNT_PIN_INS_33KV";
    private static final String COUNT_DISC_INS_33KV = "COUNT_DISC_INS_33KV";
    private static final String COUNT_SIDE_ARM_33KV = "COUNT_SIDE_ARM_33KV";
    private static final String COUNT_METALS_PARTS_33KV = "COUNT_METALS_PARTS_33KV";

    private static final String TAPPINGXARM_CODE_33KV = "TAPPINGXARM_CODE_33KV";
    private static final String TAPPINGXARM_DESC_33KV = "TAPPINGXARM_DESC_33KV";
    private static final String VXARM_CODE_33KV = "VXARM_CODE_33KV";
    private static final String VXARM_DESC_33KV = "VXARM_DESC_33KV";
    private static final String SIDEARM_CODE_33KV = "SIDEARM_CODE_33KV";
    private static final String SIDEARM_DESC_33KV = "SIDEARM_DESC_33KV";
    private static final String CHANNEL_CODE_33KV = "CHANNEL_CODE_33KV";
    private static final String CHANNEL_DESC_33KV = "CHANNEL_DESC_33KV";
    private static final String COUNT_CHANNEL_33KV = "COUNT_CHANNEL_33KV";
    private static final String TOPCLEAT_CODE_33KV = "TOPCLEAT_CODE_33KV";
    private static final String TOPCLEAT_DESC_33KV = "TOPCLEAT_DESC_33KV";

    private static final String PIN_INS_CODE_33KV = "PIN_INS_CODE_33KV";
    private static final String PIN_INS_DESC_33KV = "PIN_INS_DESC_33KV";
    private static final String COUNT_PIN_33KV = "COUNT_PIN_33KV";
    private static final String DISC_INS_CODE_33KV = "DISC_INS_CODE_33KV";
    private static final String DISC_INS_DESC_33KV = "DISC_INS_DESC_33KV";
    private static final String COUNT_DISC_33KV = "COUNT_DISC_33KV";
    private static final String GIPIN_CODE_33KV = "GIPIN_CODE_33KV";
    private static final String GIPIN_DESC_33KV = "GIPIN_DESC_33KV";
    private static final String COUNT_GIPIN_33KV = "COUNT_GIPIN_33KV";
    private static final String METAL_PARTS_CODE_33KV = "METAL_PARTS_CODE_33KV";
    private static final String METAL_PARTS_DESC_33KV = "METAL_PARTS_DESC_33KV";
    private static final String COUNT_METAL_PARTS_33KV = "COUNT_METAL_PARTS_33KV";
    private static final String BACK_CLAMPS_CODE_33KV = "BACK_CLAMPS_CODE_33KV";
    private static final String BACK_CLAMPS_DESC_33KV = "BACK_CLAMPS_DESC_33KV";
    private static final String COUNT_BACK_CLAMPS_33KV = "COUNT_BACK_CLAMPS_33KV";
    private static final String STAY_CLAMPS_CODE_33KV = "STAY_CLAMPS_CODE_33KV";
    private static final String STAY_CLAMPS_DESC_33KV = "STAY_CLAMPS_DESC_33KV";
    private static final String COUNT_STAY_CLAMPS_33KV = "COUNT_STAY_CLAMPS_33KV";
    private static final String STAY_SETS_CODE_33KV = "STAY_SETS_CODE_33KV";
    private static final String STAY_SETS_DESC_33KV = "STAY_SETS_DESC_33KV";
    private static final String COUNT_STAY_SETS_33KV = "COUNT_STAY_SETS_33KV";
    private static final String STAYCLAMP_11M_DESC_33KV = "STAYCLAMP_11M_DESC_33KV";
    private static final String STAYCLAMP_11M_CODE_33KV = "STAYCLAMP_11M_CODE_33KV";
    private static final String COUNT__STAYCLAMP_11M_33KV = "COUNT__STAYCLAMP_11M_33KV";
    private static final String STAYCLAMP_9M_DESC_33KV = "STAYCLAMP_9M_DESC_33KV";
    private static final String STAYCLAMP_9M_CODE_33KV = "STAYCLAMP_9M_CODE_33KV";
    private static final String COUNT__STAYCLAMP_9M_33KV = "COUNT__STAYCLAMP_9M_33KV";
    private static final String STAYCLAMP_8M_DESC_33KV = "STAYCLAMP_8M_DESC_33KV";
    private static final String STAYCLAMP_8M_CODE_33KV = "STAYCLAMP_8M_CODE_33KV";
    private static final String COUNT__STAYCLAMP_8M_33KV = "COUNT__STAYCLAMP_8M_33KV";
    private static final String STAYCLAMP_SPUN_DESC_33KV = "STAYCLAMP_SPUN_DESC_33KV";
    private static final String STAYCLAMP_SPUN_CODE_33KV = "STAYCLAMP_SPUN_CODE_33KV";
    private static final String COUNT__STAYCLAMP_SPUN_33KV = "COUNT__STAYCLAMP_SPUN_33KV";
    private static final String PIN_CUT_33KV = "PIN_CUT_33KV";
    private static final String END_POLE_33KV = "END_POLE_33KV";
    private static final String INTERMEDIATE_POLE_33KV = "INTERMEDIATE_POLE_33KV";
    private static final String OTHER_FEEDER_33KV = "OTHER_FEEDER_33KV";
    private static final String CROSS_6_3KV_ID_33KV = "CROSS_6_3KV_ID_33KV";
    private static final String CROSSING_TYPE_33KV = "CROSSING_TYPE_33KV";
    private static final String CROSS_33KV_ID_33KV = "CROSS_33KV_ID_33KV";
    private static final String CROSS_LT_CKT_33KV = "CROSS_LT_CKT_33KV";
    private static final String CROSS_COMMUN_33KV = "CROSS_COMMUN_33KV";
    private static final String CROSS_EHT_33KV = "CROSS_EHT_33KV";
    private static final String CROSS_REMARKS_33KV = "CROSS_REMARKS_33KV";
    private static final String CROSS_11KV_ID_33kv = "CROSS_11KV_ID33KV";
    private static final String REMARKS_33KV = "REMARKS_33KV";
    private static final String POLE_PAINT_CODE_33KV = "POLE_PAINT_CODE_33KV";


    private static final String POLE_SURVEY_TYPE_33KV = "POLE_SURVEY_TYPE_33KV";
    private static final String IS_MULTI_CKT_33KV = "IS_MULTI_CKT_33KV";
    private static final String MULTI_CKT_33_33KV = "MULTI_CKT_33_33KV";
    private static final String MULTI_CKT_11_33KV = "MULTI_CKT_11_33KV";
    private static final String MULTI_CKT_LT_33KV = "MULTI_CKT_LT_33KV";
    private static final String POLE_TYPE_33KV = "POLE_TYPE_33KV";
    private static final String POLE_SUPPORT_33KV = "POLE_SUPPORT_33KV";
    private static final String POLE_CONFIG_33KV = "POLE_CONFIG_33KV";
    private static final String REF_ID_33KV = "REF_ID_33KV";
    private static final String LAT_33KV = "LAT_33KV";
    private static final String LON_33KV = "LON_33KV";
    private static final String FLAG_33KV = "FLAG_33KV";
    private static final String EXT_LOCATION_33KV = "EXT_LOCATION_33KV";
    private static final String REG_NUM_33KV = "REG_NUM_33KV";
    private static final String CTPT_33KV = "CTPT_33KV";
    private static final String NEW_MAPPED_33KV = "NEW_MAPPED_33KV";
    private static final String E_POLE_33KV = "E_POLE_33KV";
    private static final String FDR_LOC_NO_33KV = "FDR_LOC_NO_33KV";
    private static final String FDR_SOURCE_NO_33KV = "FDR_SOURCE_NO_33KV";


    String CREATE_SURVEY_TABLE_33KV = "CREATE TABLE " + SURVEY_TABLE_33KV + "("
            + EST_NO_33KV + " TEXT ,"
            + FROM_POLE_33KV + " TEXT ,"
            + POLE_NO_33KV + " TEXT ,"
            + LINE_TYPE_33KV + " TEXT ,"
            + No_OF_STRUT_33KV + " TEXT ,"
            + No_OF_STAY_33KV + " TEXT ,"
            + EXI_POLE_NO_33KV + " TEXT ,"
            + POLE_LOCATION_TYPE_33KV + " TEXT ,"
            + POLE_HEIGHT_33KV + " TEXT ,"
            + CONDUTCOR_SIZE_33KV + " TEXT ,"
            + LINE_SURVEY_TYPE_33KV + " TEXT ,"
            + DTR_PHASE_33KV + " TEXT ,"
            + DTR_CAPACITY_33KV + " TEXT ,"
            + DTR_MOUNTING_33KV + " TEXT ,"
            + DTR_SURVEY_TYPE_33KV + " TEXT ,"
            + LOCATION_ID_33KV + " TEXT ,"
            + POLE_SURVEY_TYPE_33KV + " TEXT ,"
            + IS_MULTI_CKT_33KV + " TEXT ,"
            + MULTI_CKT_33_33KV + " TEXT ,"
            + MULTI_CKT_11_33KV + " TEXT ,"
            + MULTI_CKT_LT_33KV + " TEXT ,"
            + POLE_TYPE_33KV + " TEXT ,"
            + POLE_SUPPORT_33KV + " TEXT ,"
            + POLE_CONFIG_33KV + " TEXT ,"
            + REF_ID_33KV + " TEXT ,"
            + LAT_33KV + " TEXT ,"
            + LON_33KV + " TEXT ,"
            + NUMB_9M_STRUTS_33KV + " TEXT ,"
            + NUMB_8M_STRUTS_33KV + " TEXT ,"
            + NUMB_STAYS_33KV + " TEXT ,"
            + NUMBERS_33KV_FEEDERS + " TEXT ,"
            + DTR_STRUCTURE_CODE_33KV + " TEXT ,"
            + POLE_DESC_33KV + " TEXT ,"
            + POLE_SAP_CODE_33KV + " TEXT ,"
            + CONDUCTOR_DESC_33KV + " TEXT ,"
            + CONDUCTOR_SAP_CODE_33KV + " TEXT ,"
            + AB_SWITCH_33KV + " TEXT ,"
            + AB_DESC_33KV + " TEXT ,"
            + SS_ID_33KV + " TEXT ,"
            + SS_NAME_33KV + " TEXT ,"
            + SS_TYPE_33KV + " TEXT ,"
            + FEEDER_ID_33KV + " TEXT ,"
            + FEEDER_NAME_33KV + " TEXT ,"
            + COUNT_TAPPING_XARM_33KV + " TEXT ,"
            + COUNT_V_XARM_33KV + " TEXT ,"
            + COUNT_TOP_CLEAT_33KV + " TEXT ,"
            + COUNT_PIN_INS_33KV + " TEXT ,"
            + COUNT_DISC_INS_33KV + " TEXT ,"
            + COUNT_SIDE_ARM_33KV + " TEXT ,"
            + COUNT_METALS_PARTS_33KV + " TEXT ,"
            + TAPPINGXARM_CODE_33KV + " TEXT ,"
            + TAPPINGXARM_DESC_33KV + " TEXT ,"
            + VXARM_CODE_33KV + " TEXT ,"
            + VXARM_DESC_33KV + " TEXT ,"
            + SIDEARM_CODE_33KV + " TEXT ,"
            + SIDEARM_DESC_33KV + " TEXT ,"
            + CHANNEL_CODE_33KV + " TEXT ,"
            + CHANNEL_DESC_33KV + " TEXT ,"
            + TOPCLEAT_CODE_33KV + " TEXT ,"
            + TOPCLEAT_DESC_33KV + " TEXT ,"
            + COUNT_CHANNEL_33KV + " TEXT ,"
            + PIN_INS_CODE_33KV + " TEXT ,"
            + PIN_INS_DESC_33KV + " TEXT ,"
            + COUNT_PIN_33KV + " TEXT ,"
            + DISC_INS_CODE_33KV + " TEXT ,"
            + DISC_INS_DESC_33KV + " TEXT ,"
            + COUNT_DISC_33KV + " TEXT ,"
            + GIPIN_CODE_33KV + " TEXT ,"
            + GIPIN_DESC_33KV + " TEXT ,"
            + COUNT_GIPIN_33KV + " TEXT ,"
            + METAL_PARTS_CODE_33KV + " TEXT ,"
            + METAL_PARTS_DESC_33KV + " TEXT ,"
            + COUNT_METAL_PARTS_33KV + " TEXT ,"
            + BACK_CLAMPS_CODE_33KV + " TEXT ,"
            + BACK_CLAMPS_DESC_33KV + " TEXT ,"
            + COUNT_BACK_CLAMPS_33KV + " TEXT ,"
            + STAY_CLAMPS_CODE_33KV + " TEXT ,"
            + STAY_CLAMPS_DESC_33KV + " TEXT ,"
            + COUNT_STAY_CLAMPS_33KV + " TEXT ,"
            + STAY_SETS_CODE_33KV + " TEXT ,"
            + STAY_SETS_DESC_33KV + " TEXT ,"
            + COUNT_STAY_SETS_33KV + " TEXT ,"
            + STAYCLAMP_11M_DESC_33KV + " TEXT ,"
            + STAYCLAMP_11M_CODE_33KV + " TEXT ,"
            + COUNT__STAYCLAMP_11M_33KV + " TEXT ,"
            + STAYCLAMP_9M_DESC_33KV + " TEXT ,"
            + STAYCLAMP_9M_CODE_33KV + " TEXT ,"
            + COUNT__STAYCLAMP_9M_33KV + " TEXT ,"
            + STAYCLAMP_8M_DESC_33KV + " TEXT ,"
            + STAYCLAMP_8M_CODE_33KV + " TEXT ,"
            + COUNT__STAYCLAMP_8M_33KV + " TEXT ,"
            + STAYCLAMP_SPUN_DESC_33KV + " TEXT ,"
            + STAYCLAMP_SPUN_CODE_33KV + " TEXT ,"
            + COUNT__STAYCLAMP_SPUN_33KV + " TEXT ,"
            + PIN_CUT_33KV + " TEXT ,"
            + END_POLE_33KV + " TEXT ,"
            + EXT_LOCATION_33KV + " TEXT ,"
            + INTERMEDIATE_POLE_33KV + " TEXT ,"
            + OTHER_FEEDER_33KV + " TEXT ,"
            + CROSS_6_3KV_ID_33KV + " TEXT ,"
            + CROSSING_TYPE_33KV + " TEXT ,"
            + CROSS_33KV_ID_33KV + " TEXT ,"
            + CROSS_LT_CKT_33KV + " TEXT ,"
            + CROSS_COMMUN_33KV + " TEXT ,"
            + CROSS_EHT_33KV + " TEXT ,"
            + CROSS_REMARKS_33KV + " TEXT ,"
            + CROSS_11KV_ID_33kv + " TEXT ,"
            + REMARKS_33KV + " TEXT ,"
            + POLE_PAINT_CODE_33KV + " TEXT ,"
            + REG_NUM_33KV + " TEXT ,"
            + CTPT_33KV + " TEXT ,"
            + NEW_MAPPED_33KV + " TEXT ,"
            + E_POLE_33KV + " TEXT ,"
            + FDR_LOC_NO_33KV + " TEXT ,"
            + FDR_SOURCE_NO_33KV + " TEXT ,"


            + FLAG_33KV + " TEXT " + ")";


    //11KV
    private static final String SURVEY_TABLE_11KV = "SURVEY_TABLE_11KV";
    private static final String EXISTING_POLES_TABLE_11KV = "EXISTING_POLES_TABLE_11KV";
    private static final String EXT_LOCATION_11KV = "EXT_LOCATION_11KV";
    private static final String EST_NO_11KV = "EST_NO_11KV";
    private static final String FROM_POLE_11KV = "FROM_POLE_11KV";
    private static final String POLE_NO_11KV = "POLE_NO_11KV";
    private static final String LINE_TYPE_11KV = "LINE_TYPE_11KV";
    private static final String No_OF_STRUT_11KV = "No_OF_STRUT_11KV";
    private static final String No_OF_STAY_11KV = "No_OF_STAY_11KV";
    private static final String EXI_POLE_NO_11KV = "EXI_POLE_NO_11KV";
    private static final String POLE_LOCATION_TYPE_11KV = "POLE_LOCATION_TYPE_11KV";
    private static final String POLE_HEIGHT_11KV = "POLE_HEIGHT_11KV";
    private static final String CONDUTCOR_SIZE_11KV = "CONDUTCOR_SIZE_11KV";
    private static final String LINE_SURVEY_TYPE_11KV = "LINE_SURVEY_TYPE_11KV";
    private static final String DTR_PHASE_11KV = "DTR_PHASE_11KV";
    private static final String DTR_CAPACITY_11KV = "DTR_CAPACITY_11KV";
    private static final String DTR_MOUNTING_11KV = "DTR_MOUNTING_11KV";
    private static final String DTR_SURVEY_TYPE_11KV = "DTR_SURVEY_TYPE_11KV";
    private static final String LOCATION_ID_11KV = "LOCATION_ID_11KV";
    private static final String POLE_SURVEY_TYPE_11KV = "POLE_SURVEY_TYPE_11KV";
    private static final String IS_MULTI_CKT_11KV = "IS_MULTI_CKT_11KV";
    private static final String MULTI_CKT_33_11KV = "MULTI_CKT_33_11KV";
    private static final String MULTI_CKT_11_11KV = "MULTI_CKT_11_11KV";
    private static final String MULTI_CKT_LT_11KV = "MULTI_CKT_LT_11KV";
    private static final String POLE_TYPE_11KV = "POLE_TYPE_11KV";
    private static final String POLE_SUPPORT_11KV = "POLE_SUPPORT_11KV";
    private static final String POLE_CONFIG_11KV = "POLE_CONFIG_11KV";
    private static final String REF_ID_11KV = "REF_ID_11KV";
    private static final String LAT_11KV = "LAT_11KV";
    private static final String LON_11KV = "LON_11KV";
    private static final String IS_LT_CKT_AVAILABLE = "IS_LT_CKT_AVAILABLE";
    private static final String NO_OF_LT_CKT = "NO_OF_LT_CKT";
    private static final String FLAG_11KV = "FLAG_11KV";
    private static final String NUMB_9M_STRUTS_11KV = "NUMB_9M_STRUTS_11KV";
    private static final String NUMB_8M_STRUTS_11KV = "NUMB_8M_STRUTS_11KV";
    private static final String NUMB_STAYS_11KV = "NUMB_STAYS_11KV";
    private static final String NUMBERS_11KV_FEEDERS = "NUMBERS_11KV_FEEDERS";
    private static final String DTR_STRUCTURE_CODE_11KV = "DTR_STRUCTURE_CODE_11KV";
    private static final String POLE_DESC_11KV = "POLE_DESC_11KV";
    private static final String POLE_SAP_CODE_11KV = "POLE_SAP_CODE_11KV";
    private static final String CONDUCTOR_DESC_11KV = "CONDUCTOR_DESC_11KV";
    private static final String CONDUCTOR_SAP_CODE_11KV = "CONDUCTOR_SAP_CODE_11KV";
    private static final String DTR_DESC_11KV = "DTR_DESC_11KV";
    private static final String DTR_SAP_CODE_11KV = "DTR_SAP_CODE_11KV";
    private static final String AB_SWITCH_11KV = "AB_SWITCH_11KV";
    private static final String AB_DESC_11KV = "AB_DESC_11KV";
    private static final String SS_ID_11KV = "SS_ID_11KV";
    private static final String SS_NAME_11KV = "SS_NAME_11KV";
    private static final String SS_TYPE_11KV = "SS_TYPE_11KV";
    private static final String FEEDER_ID_11KV = "FEEDER_ID_11KV";
    private static final String FEEDER_NAME_11KV = "FEEDER_NAME_11KV";

    private static final String DTR_AB_DESC_11KV = "DTR_AB_DESC_11KV";
    private static final String DTR_AB_CODE_11KV = "DTR_AB_CODE_11KV";
    private static final String HG_FUSE_DESC_11KV = "HG_FUSE_DESC_11KV";
    private static final String HG_FUSE_CODE_11KV = "HG_FUSE_CODE_11KV";
    private static final String LT_FUSE_DESC_11KV = "LT_FUSE_DESC_11KV";
    private static final String LT_FUSE_CODE_11KV = "LT_FUSE_CODE_11KV";
    private static final String CI_SAP_CODE_11KV = "CI_SAP_CODE_11KV";
    private static final String CI_DESC_11KV = "CI_DESC_11KV";
    private static final String GI_SAP_CODE_11KV = "GI_SAP_CODE_11KV";
    private static final String GI_DESC_11KV = "GI_DESC_11KV";
    private static final String COUNT_CI_11KV = "COUNT_CI_11KV";
    private static final String COUNT_GI_11KV = "COUNT_GI_11KV";
    private static final String COUNT_TAPPING_XARM_11KV = "COUNT_TAPPING_XARM_11KV";
    private static final String COUNT_V_XARM_11KV = "COUNT_V_XARM_11KV";
    private static final String COUNT_TOP_CLEAT_11KV = "COUNT_TOP_CLEAT_11KV";
    private static final String COUNT_PIN_INS_11KV = "COUNT_PIN_INS_11KV";
    private static final String COUNT_DISC_INS_11KV = "COUNT_DISC_INS_11KV";
    private static final String COUNT_SIDE_ARM_11KV = "COUNT_SIDE_ARM_11KV";
    private static final String COUNT_METALS_PARTS_11KV = "COUNT_METALS_PARTS_11KV";
    private static final String TAPPINGXARM_CODE_11KV = "TAPPINGXARM_CODE_11KV";
    private static final String TAPPINGXARM_DESC_11KV = "TAPPINGXARM_DESC_11KV";
    private static final String VXARM_CODE_11KV = "VXARM_CODE_11KV";
    private static final String VXARM_DESC_11KV = "VXARM_DESC_11KV";
    private static final String SIDEARM_CODE_11KV = "SIDEARM_CODE_11KV";
    private static final String SIDEARM_DESC_11KV = "SIDEARM_DESC_11KV";
    private static final String CHANNEL_CODE_11KV = "CHANNEL_CODE_11KV";
    private static final String CHANNEL_DESC_11KV = "CHANNEL_DESC_11KV";
    private static final String TOPCLEAT_CODE_11KV = "TOPCLEAT_CODE_11KV";
    private static final String TOPCLEAT_DESC_11KV = "TOPCLEAT_DESC_11KV";
    private static final String COUNT_CHANNEL_11KV = "COUNT_CHANNEL_11KV";

    private static final String PIN_INS_CODE_11KV = "PIN_INS_CODE_11KV";
    private static final String PIN_INS_DESC_11KV = "PIN_INS_DESC_11KV";
    private static final String COUNT_PIN_11KV = "COUNT_PIN_11KV";
    private static final String DISC_INS_CODE_11KV = "DISC_INS_CODE_11KV";
    private static final String DISC_INS_DESC_11KV = "DISC_INS_DESC_11KV";
    private static final String COUNT_DISC_11KV = "COUNT_DISC_11KV";
    private static final String GIPIN_CODE_11KV = "GIPIN_CODE_11KV";
    private static final String GIPIN_DESC_11KV = "GIPIN_DESC_11KV";
    private static final String COUNT_GIPIN_11KV = "COUNT_GIPIN_11KV";
    private static final String METAL_PARTS_CODE_11KV = "METAL_PARTS_CODE_11KV";
    private static final String METAL_PARTS_DESC_11KV = "METAL_PARTS_DESC_11KV";
    private static final String COUNT_METAL_PARTS_11KV = "COUNT_METAL_PARTS_11KV";
    private static final String BACK_CLAMPS_CODE_11KV = "BACK_CLAMPS_CODE_11KV";
    private static final String BACK_CLAMPS_DESC_11KV = "BACK_CLAMPS_DESC_11KV";
    private static final String COUNT_BACK_CLAMPS_11KV = "COUNT_BACK_CLAMPS_11KV";
    private static final String STAY_CLAMPS_CODE_11KV = "STAY_CLAMPS_CODE_11KV";
    private static final String STAY_CLAMPS_DESC_11KV = "STAY_CLAMPS_DESC_11KV";
    private static final String COUNT_STAY_CLAMPS_11KV = "COUNT_STAY_CLAMPS_11KV";
    private static final String STAY_SETS_CODE_11KV = "STAY_SETS_CODE_11KV";
    private static final String STAY_SETS_DESC_11KV = "STAY_SETS_DESC_11KV";
    private static final String COUNT_STAY_SETS_11KV = "COUNT_STAY_SETS_11KV";

    private static final String STAYCLAMP_11M_DESC_11KV = "STAYCLAMP_11M_DESC_11KV";
    private static final String STAYCLAMP_11M_CODE_11KV = "STAYCLAMP_11M_CODE_11KV";
    private static final String COUNT__STAYCLAMP_11M_11KV = "COUNT__STAYCLAMP_11M_11KV";
    private static final String STAYCLAMP_9M_DESC_11KV = "STAYCLAMP_9M_DESC_11KV";
    private static final String STAYCLAMP_9M_CODE_11KV = "STAYCLAMP_9M_CODE_11KV";
    private static final String COUNT__STAYCLAMP_9M_11KV = "COUNT__STAYCLAMP_9M_11KV";
    private static final String STAYCLAMP_8M_DESC_11KV = "STAYCLAMP_8M_DESC_11KV";
    private static final String STAYCLAMP_8M_CODE_11KV = "STAYCLAMP_8M_CODE_11KV";
    private static final String COUNT__STAYCLAMP_8M_11KV = "COUNT__STAYCLAMP_8M_11KV";
    private static final String STAYCLAMP_SPUN_DESC_11KV = "STAYCLAMP_SPUN_DESC_11KV";
    private static final String STAYCLAMP_SPUN_CODE_11KV = "STAYCLAMP_SPUN_CODE_11KV";
    private static final String COUNT__STAYCLAMP_SPUN_11KV = "COUNT__STAYCLAMP_SPUN_11KV";

    private static final String DTR_MOUNTING_DESC_11KV = "DTR_MOUNTING_DESC_11KV";
    private static final String DTR_MOUNTING_CODE_11KV = "DTR_MOUNTING_CODE_11KV";
    private static final String AB_MOUNTING_DESC_11KV = "AB_MOUNTING_DESC_11KV";
    private static final String AB_MOUNTING_CODE_11KV = "AB_MOUNTING_CODE_11KV";
    private static final String PIN_CUT_11KV = "PIN_CUT_11KV";
    private static final String END_POLE_11KV = "END_POLE_11KV";
    private static final String LT_AVAILABLE = "LT_AVAILABLE";
    private static final String COUNT_CKT = "COUNT_CKT";
    private static final String LOAD_TYPE = "LOAD_TYPE";
    private static final String INTERMEDIATE_POLE_11KV = "INTERMEDIATE_POLE_11KV";
    private static final String OTHER_FEEDER_11KV = "OTHER_FEEDER_11KV";
    private static final String CROSS_6_3KV_ID_11KV = "CROSS_6_3KV_ID_11KV";
    private static final String CROSSING_TYPE_11KV = "CROSSING_TYPE_11KV";
    private static final String CROSS_33KV_ID_11KV = "CROSS_33KV_ID_11KV";
    private static final String CROSS_LT_CKT_11KV = "CROSS_LT_CKT_11KV";
    private static final String CROSS_COMMUN_11KV = "CROSS_COMMUN_11KV";
    private static final String CROSS_EHT_11KV = "CROSS_EHT_11KV";
    private static final String CROSS_REMARKS_11KV = "CROSS_REMARKS_11KV";
    private static final String CROSS_11KV_ID_11kv = "CROSS_11KV_ID_11kv";
    private static final String REMARKS_11KV = "REMARKS_11KV";
    private static final String POLE_PAINT_CODE_11KV = "POLE_PAINT_CODE_11KV";
    private static final String DTR_SNO_11KV = "DTR_SNO_11KV";
    private static final String DTR_MAKE_11KV = "DTR_MAKE_11KV";
    private static final String DTR_LOCATION_11KV = "DTR_LOCATION_11KV";
    private static final String EDTR_PHASE_11KV = "EDTR_PHASE_11KV";
    private static final String EDTR_CAPACITY_11KV = "EDTR_CAPACITY_11KV";
    private static final String EDTR_EDITED_11KV = "EDTR_EDITED_11KV";
    private static final String SERVICE_TYPE_11KV = "SERVICE_TYPE_11KV";
    private static final String DTR_SHIFTED_11KV = "DTR_SHIFTED_11KV";
    private static final String REG_NUM_11KV = "REG_NUM_11KV";
    private static final String DTR_ID_11KV = "DTR_ID_11KV";

    private static final String DT_SHIFT_REMARKS_11KV = "DT_SHIFT_REMARKS_11KV";
    private static final String CTPT_11KV = "CTPT_11KV";
    private static final String NEW_MAPPED_11KV = "NEW_MAPPED_11KV";
    private static final String E_POLE_11KV = "E_POLE_11KV";
    private static final String FDR_LOC_NO_11KV = "FDR_LOC_NO_11KV";
    private static final String FDR_SOURCE_NO_11KV = "FDR_SOURCE_NO_11KV";
    private static final String REPEAT_POLE = "REPEAT_POLE";

    String CREATE_SURVEY_TABLE_11KV = "CREATE TABLE " + SURVEY_TABLE_11KV + "("
            + EST_NO_11KV + " TEXT ,"
            + FROM_POLE_11KV + " TEXT ,"
            + POLE_NO_11KV + " TEXT ,"
            + LINE_TYPE_11KV + " TEXT ,"
            + No_OF_STRUT_11KV + " TEXT ,"
            + No_OF_STAY_11KV + " TEXT ,"
            + EXI_POLE_NO_11KV + " TEXT ,"
            + POLE_LOCATION_TYPE_11KV + " TEXT ,"
            + POLE_HEIGHT_11KV + " TEXT ,"
            + CONDUTCOR_SIZE_11KV + " TEXT ,"
            + LINE_SURVEY_TYPE_11KV + " TEXT ,"
            + DTR_PHASE_11KV + " TEXT ,"
            + DTR_CAPACITY_11KV + " TEXT ,"
            + DTR_MOUNTING_11KV + " TEXT ,"
            + DTR_SURVEY_TYPE_11KV + " TEXT ,"
            + LOCATION_ID_11KV + " TEXT ,"
            + POLE_SURVEY_TYPE_11KV + " TEXT ,"
            + IS_MULTI_CKT_11KV + " TEXT ,"
            + MULTI_CKT_33_11KV + " TEXT ,"
            + MULTI_CKT_11_11KV + " TEXT ,"
            + MULTI_CKT_LT_11KV + " TEXT ,"
            + POLE_TYPE_11KV + " TEXT ,"
            + POLE_SUPPORT_11KV + " TEXT ,"
            + POLE_CONFIG_11KV + " TEXT ,"
            + REF_ID_11KV + " TEXT ,"
            + LAT_11KV + " TEXT ,"
            + LON_11KV + " TEXT ,"
            + IS_LT_CKT_AVAILABLE + " TEXT ,"
            + NO_OF_LT_CKT + " TEXT ,"
            + NUMB_9M_STRUTS_11KV + " TEXT ,"
            + NUMB_8M_STRUTS_11KV + " TEXT ,"
            + NUMB_STAYS_11KV + " TEXT ,"
            + NUMBERS_11KV_FEEDERS + " TEXT ,"
            + DTR_STRUCTURE_CODE_11KV + " TEXT ,"
            + POLE_DESC_11KV + " TEXT ,"
            + POLE_SAP_CODE_11KV + " TEXT ,"
            + CONDUCTOR_DESC_11KV + " TEXT ,"
            + DTR_DESC_11KV + " TEXT ,"
            + DTR_SAP_CODE_11KV + " TEXT ,"
            + CONDUCTOR_SAP_CODE_11KV + " TEXT ,"
            + AB_SWITCH_11KV + " TEXT ,"
            + AB_DESC_11KV + " TEXT ,"
            + SS_ID_11KV + " TEXT ,"
            + SS_NAME_11KV + " TEXT ,"
            + SS_TYPE_11KV + " TEXT ,"
            + FEEDER_ID_11KV + " TEXT ,"
            + FEEDER_NAME_11KV + " TEXT ,"
            + DTR_AB_DESC_11KV + " TEXT ,"
            + DTR_AB_CODE_11KV + " TEXT ,"
            + HG_FUSE_DESC_11KV + " TEXT ,"
            + HG_FUSE_CODE_11KV + " TEXT ,"
            + LT_FUSE_DESC_11KV + " TEXT ,"
            + LT_FUSE_CODE_11KV + " TEXT ,"
            + CI_SAP_CODE_11KV + " TEXT ,"
            + CI_DESC_11KV + " TEXT ,"
            + GI_SAP_CODE_11KV + " TEXT ,"
            + GI_DESC_11KV + " TEXT ,"
            + COUNT_CI_11KV + " TEXT ,"
            + COUNT_GI_11KV + " TEXT ,"
            + COUNT_TAPPING_XARM_11KV + " TEXT ,"
            + COUNT_V_XARM_11KV + " TEXT ,"
            + COUNT_TOP_CLEAT_11KV + " TEXT ,"
            + COUNT_PIN_INS_11KV + " TEXT ,"
            + COUNT_DISC_INS_11KV + " TEXT ,"
            + COUNT_SIDE_ARM_11KV + " TEXT ,"
            + COUNT_METALS_PARTS_11KV + " TEXT ,"

            + TAPPINGXARM_CODE_11KV + " TEXT ,"
            + TAPPINGXARM_DESC_11KV + " TEXT ,"
            + VXARM_CODE_11KV + " TEXT ,"
            + VXARM_DESC_11KV + " TEXT ,"
            + SIDEARM_CODE_11KV + " TEXT ,"
            + SIDEARM_DESC_11KV + " TEXT ,"
            + CHANNEL_CODE_11KV + " TEXT ,"
            + CHANNEL_DESC_11KV + " TEXT ,"
            + TOPCLEAT_CODE_11KV + " TEXT ,"
            + TOPCLEAT_DESC_11KV + " TEXT ,"
            + PIN_INS_CODE_11KV + " TEXT ,"
            + PIN_INS_DESC_11KV + " TEXT ,"
            + COUNT_PIN_11KV + " TEXT ,"
            + DISC_INS_CODE_11KV + " TEXT ,"
            + DISC_INS_DESC_11KV + " TEXT ,"
            + COUNT_DISC_11KV + " TEXT ,"
            + GIPIN_CODE_11KV + " TEXT ,"
            + GIPIN_DESC_11KV + " TEXT ,"
            + COUNT_GIPIN_11KV + " TEXT ,"
            + METAL_PARTS_CODE_11KV + " TEXT ,"
            + METAL_PARTS_DESC_11KV + " TEXT ,"
            + COUNT_METAL_PARTS_11KV + " TEXT ,"
            + COUNT_CHANNEL_11KV + " TEXT ,"

            + BACK_CLAMPS_CODE_11KV + " TEXT ,"
            + BACK_CLAMPS_DESC_11KV + " TEXT ,"
            + COUNT_BACK_CLAMPS_11KV + " TEXT ,"
            + STAY_CLAMPS_CODE_11KV + " TEXT ,"
            + STAY_CLAMPS_DESC_11KV + " TEXT ,"
            + COUNT_STAY_CLAMPS_11KV + " TEXT ,"
            + STAY_SETS_CODE_11KV + " TEXT ,"
            + STAY_SETS_DESC_11KV + " TEXT ,"
            + COUNT_STAY_SETS_11KV + " TEXT ,"

            + STAYCLAMP_11M_DESC_11KV + " TEXT ,"
            + STAYCLAMP_11M_CODE_11KV + " TEXT ,"
            + COUNT__STAYCLAMP_11M_11KV + " TEXT ,"
            + STAYCLAMP_9M_DESC_11KV + " TEXT ,"
            + STAYCLAMP_9M_CODE_11KV + " TEXT ,"
            + COUNT__STAYCLAMP_9M_11KV + " TEXT ,"
            + STAYCLAMP_8M_DESC_11KV + " TEXT ,"
            + STAYCLAMP_8M_CODE_11KV + " TEXT ,"
            + COUNT__STAYCLAMP_8M_11KV + " TEXT ,"
            + STAYCLAMP_SPUN_DESC_11KV + " TEXT ,"
            + STAYCLAMP_SPUN_CODE_11KV + " TEXT ,"
            + COUNT__STAYCLAMP_SPUN_11KV + " TEXT ,"

            + DTR_MOUNTING_DESC_11KV + " TEXT ,"
            + DTR_MOUNTING_CODE_11KV + " TEXT ,"
            + AB_MOUNTING_DESC_11KV + " TEXT ,"
            + AB_MOUNTING_CODE_11KV + " TEXT ,"
            + PIN_CUT_11KV + " TEXT ,"
            + END_POLE_11KV + " TEXT ,"
            + EXT_LOCATION_11KV + " TEXT ,"
            + LT_AVAILABLE + " TEXT ,"
            + COUNT_CKT + " TEXT ,"
            + LOAD_TYPE + " TEXT ,"
            + INTERMEDIATE_POLE_11KV + " TEXT ,"
            + OTHER_FEEDER_11KV + " TEXT ,"
            + CROSS_6_3KV_ID_11KV + " TEXT ,"
            + CROSSING_TYPE_11KV + " TEXT ,"
            + CROSS_33KV_ID_11KV + " TEXT ,"
            + CROSS_LT_CKT_11KV + " TEXT ,"
            + CROSS_COMMUN_11KV + " TEXT ,"
            + CROSS_EHT_11KV + " TEXT ,"
            + CROSS_REMARKS_11KV + " TEXT ,"
            + CROSS_11KV_ID_11kv + " TEXT ,"
            + REMARKS_11KV + " TEXT ,"
            + POLE_PAINT_CODE_11KV + " TEXT ,"
            + DTR_SNO_11KV + " TEXT ,"
            + DTR_MAKE_11KV + " TEXT ,"
            + DTR_LOCATION_11KV + " TEXT ,"
            + EDTR_PHASE_11KV + " TEXT ,"
            + EDTR_CAPACITY_11KV + " TEXT ,"
            + EDTR_EDITED_11KV + " TEXT ,"
            + SERVICE_TYPE_11KV + " TEXT ,"
            + DTR_SHIFTED_11KV + " TEXT ,"
            + REG_NUM_11KV + " TEXT ,"
            + DTR_ID_11KV + " TEXT ,"
            + DT_SHIFT_REMARKS_11KV + " TEXT ,"
            + CTPT_11KV + " TEXT ,"
            + NEW_MAPPED_11KV + " TEXT ,"
            + E_POLE_11KV + " TEXT ,"
            + FDR_LOC_NO_11KV + " TEXT ,"
            + FDR_SOURCE_NO_11KV + " TEXT ,"


            + FLAG_11KV + " TEXT " + ")";

    private static final String DTR_TABLE = "DTR_TABLE";
    String CREATE_DTR_TABLE = "CREATE TABLE " + DTR_TABLE + "("
            + EST_NO_11KV + " TEXT ,"
            + POLE_NO_11KV + " TEXT ,"
            + LAT_11KV + " TEXT ,"
            + LON_11KV + " TEXT ,"
            + IS_LT_CKT_AVAILABLE + " TEXT ,"
            + NO_OF_LT_CKT + " TEXT ,"

            + FLAG_11KV + " TEXT " + ")";

    //LOCATIONS EDIT
    private static final String EDIT_TABLE = "EDIT_TABLE";
    private static final String EDIT_EST_NO = "EDIT_EST_NO";
    private static final String EDIT_POLE_NO = "EDIT_POLE_NO";
    private static final String EDIT_POLE_LAT = "EDIT_POLE_LAT";
    private static final String EDIT_POLE_LON = "EDIT_POLE_LON";
    private static final String EDIT_VOLTAGE_LEVEL = "EDIT_VOLTAGE_LEVEL";
    private static final String EDIT_LOCATION_ID = "EDIT_LOCATION_ID";
    private static final String EDIT_GIS_ID = "EDIT_GIS_ID";
    private static final String EDIT_TYPE = "EDIT_TYPE";

    String CREATE_EDIT_TABLE = "CREATE TABLE " + EDIT_TABLE + "("
            + EDIT_EST_NO + " TEXT ,"
            + EDIT_POLE_NO + " TEXT ,"
            + EDIT_POLE_LAT + " TEXT ,"
            + EDIT_POLE_LON + " TEXT ,"
            + EDIT_VOLTAGE_LEVEL + " TEXT ,"
            + EDIT_LOCATION_ID + " TEXT ,"
            + EDIT_GIS_ID + " TEXT ,"
            + EDIT_TYPE + " TEXT ,"

            + FLAG_11KV + " TEXT " + ")";


    //POLE HEIGHT DETAILS
    private static final String POLE_HEIGHT_DETAILS_TABLE = "POLE_HEIGHT_DETAILS_TABLE";
    private static final String HEIGHT = "HEIGHT";
    private static final String MAX_DISTANCE = "MAX_DISTANCE";
    private static final String VOLTAGE = "VOLTAGE";
    private static final String CROSSING = "CROSSING";
    private static final String LINE_TYPE = "LINE_TYPE";


    String CREATE_POLE_HEIGHT_DETAILS_TABLE = "CREATE TABLE " + POLE_HEIGHT_DETAILS_TABLE + "("
            + POLE_TYPE + " TEXT ,"
            + HEIGHT + " TEXT ,"
            + MAX_DISTANCE + " TEXT ,"
            + VOLTAGE + " TEXT ,"
            + CROSSING + " TEXT ,"
            + LINE_TYPE + " TEXT ,"
            + FLAG + " TEXT " + ")";

    public boolean addPoleDistanceDetails(PoleDistanceModel data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HEIGHT, data.getHEIGHT());
        values.put(POLE_TYPE, data.getPOLE_TYPE());
        values.put(MAX_DISTANCE, data.getMAX_DISTANCE());
        values.put(VOLTAGE, data.getVOLTAGE());
        values.put(CROSSING, data.getCROSSING());
        values.put(LINE_TYPE, data.getLINE_TYPE());


        values.put(FLAG, "N");

        long a = db.insert(POLE_HEIGHT_DETAILS_TABLE, null, values);
        db.close();
        if (a == -1) {
            return false;
        } else {
            return true;
        }

    }

    public ArrayList<PoleDistanceModel> getPoleDistanceDetails() {
        ArrayList<PoleDistanceModel> data = new ArrayList<>();
        String query = "SELECT * FROM POLE_HEIGHT_DETAILS_TABLE";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    PoleDistanceModel model = new PoleDistanceModel();
                    model.setPOLE_TYPE(cursor.getString(0));
                    model.setHEIGHT(cursor.getString(1));
                    model.setMAX_DISTANCE(cursor.getInt(2));
                    model.setVOLTAGE(cursor.getString(3));
                    model.setCROSSING(cursor.getString(4));
                    model.setLINE_TYPE(cursor.getString(5));

                    data.add(model);
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        } finally {
            db.close();
        }
        return data;
    }

    public ArrayList<PoleDistanceModel> getPoleDistanceDetails(String lineType, String poleType, String poleHeight, String voltageLevel, String crossing) {
        System.out.println("QUARY lineType" + lineType.toUpperCase());
        System.out.println("QUARY poleType" + poleType);
        System.out.println("QUARY poleHeight" + poleHeight);
        System.out.println("QUARY voltageLevel" + voltageLevel);
        System.out.println("QUARY crossing" + crossing);
        ArrayList<PoleDistanceModel> data = new ArrayList<>();
        String query = "SELECT * FROM POLE_HEIGHT_DETAILS_TABLE WHERE VOLTAGE = ? AND LINE_TYPE = ? AND POLE_TYPE = ? AND HEIGHT = ? AND CROSSING = ?";
        System.out.println("QUARY" + query);

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, new String[]{voltageLevel, lineType.toUpperCase(), poleType, poleHeight, crossing});
            if (cursor.moveToFirst()) {
                do {
                    PoleDistanceModel model = new PoleDistanceModel();
                    model.setPOLE_TYPE(cursor.getString(0));
                    model.setHEIGHT(cursor.getString(1));
                    model.setMAX_DISTANCE(cursor.getInt(2));
                    model.setVOLTAGE(cursor.getString(3));
                    model.setCROSSING(cursor.getString(4));
                    model.setLINE_TYPE(cursor.getString(5));

                    data.add(model);
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        } finally {
            db.close();
        }
        return data;
    }

    public ArrayList<PoleDistanceModel> getPoleDistanceDetailsUnderground(String lineType, String voltageLevel) {
        System.out.println("QUARY lineType" + lineType.toUpperCase());
        System.out.println("QUARY voltageLevel" + voltageLevel);
        ArrayList<PoleDistanceModel> data = new ArrayList<>();
        String query = "SELECT * FROM POLE_HEIGHT_DETAILS_TABLE WHERE VOLTAGE = ? AND LINE_TYPE = ?";
        System.out.println("QUARY" + query);

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, new String[]{voltageLevel, lineType.toUpperCase()});
            if (cursor.moveToFirst()) {
                do {
                    PoleDistanceModel model = new PoleDistanceModel();
                    model.setPOLE_TYPE(cursor.getString(0));
                    model.setHEIGHT(cursor.getString(1));
                    model.setMAX_DISTANCE(cursor.getInt(2));
                    model.setVOLTAGE(cursor.getString(3));
                    model.setCROSSING(cursor.getString(4));
                    model.setLINE_TYPE(cursor.getString(5));

                    data.add(model);
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        } finally {
            db.close();
        }
        return data;
    }

    public ArrayList<PoleDistanceModel> getPoleDistanceDetailsTowerPole(String lineType, String voltageLevel, String poleType) {
        System.out.println("QUERY FOR TOWER lineType: " + lineType.toUpperCase());
        System.out.println("QUERY FOR TOWER voltageLevel: " + voltageLevel);
        System.out.println("QUERY FOR TOWER pole_type: " + poleType);

        ArrayList<PoleDistanceModel> data = new ArrayList<>();
        String query = "SELECT * FROM POLE_HEIGHT_DETAILS_TABLE WHERE VOLTAGE = ? AND LINE_TYPE = ? AND POLE_TYPE = ?";
        System.out.println("QUERY FOR TOWER: " + query);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, new String[]{voltageLevel, lineType.toUpperCase(), poleType});
            if (cursor.moveToFirst()) {
                do {
                    PoleDistanceModel model = new PoleDistanceModel();
                    model.setPOLE_TYPE(cursor.getString(0));
                    model.setHEIGHT(cursor.getString(1));
                    model.setMAX_DISTANCE(cursor.getInt(2));
                    model.setVOLTAGE(cursor.getString(3));
                    model.setCROSSING(cursor.getString(4));
                    model.setLINE_TYPE(cursor.getString(5));

                    data.add(model);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }

        return data;
    }


    public boolean clearPoleDetails() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            db.delete(POLE_HEIGHT_DETAILS_TABLE, null, null);
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
    }


    //LT LINE CONFIG
    private static final String LT_LINE_CONFIG_TABLE = "LT_LINE_CONFIG_TABLE";
    private static final String LINE_CONFIG = "LINE_CONFIG";
    private static final String TYPE = "TYPE";
    private static final String FACTOR = "FACTOR";

    //DTR MAKE MASTER
    private static final String DTR_MAKE_TABLE = "DTR_MAKE_TABLE";
    private static final String DTR_NAME = "DTR_NAME";
    private static final String ID = "ID";

    String CREATE_DTR_MAKE_TABLE = "CREATE TABLE " + DTR_MAKE_TABLE + "("
            + DTR_MAKE + " TEXT ,"
            + DTR_NAME + " TEXT ,"
            + ID + " TEXT ,"
            + FLAG + " TEXT " + ")";


    String CREATE_LT_LINE_CONFIG_TABLE = "CREATE TABLE " + LT_LINE_CONFIG_TABLE + "("
            + PHASE + " TEXT ,"
            + LINE_CONFIG + " TEXT ,"
            + TYPE + " TEXT ,"
            + FACTOR + " TEXT ,"
            + FLAG + " TEXT " + ")";

    public boolean addDTRMakeMaster(DTRMakeMaster data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DTR_MAKE, data.getDTR_MAKE());
        values.put(DTR_NAME, data.getDTR_MAKE());
        values.put(ID, data.getID());


        values.put(FLAG, "N");

        long a = db.insert(DTR_MAKE_TABLE, null, values);
        db.close();
        if (a == -1) {
            return false;
        } else {
            return true;
        }

    }


    public ArrayList<DTRMakeMaster> getDTRMakeMaster() {
        ArrayList<DTRMakeMaster> data = new ArrayList<>();
        String query = "SELECT * FROM DTR_MAKE_TABLE";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    DTRMakeMaster model = new DTRMakeMaster();
                    model.setDTR_MAKE(cursor.getString(0));
                    model.setDTR_NAME(cursor.getString(1));
                    model.setID(cursor.getString(2));


                    data.add(model);
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        } finally {
            db.close();
        }
        return data;
    }

    public boolean addLTLineConfigDetails(LTLineConfigModel data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PHASE, data.getPHASE());
        values.put(LINE_CONFIG, data.getLINE_CONFIG());
        values.put(TYPE, data.getTYPE());
        values.put(FACTOR, data.getFACTOR());


        values.put(FLAG, "N");

        long a = db.insert(LT_LINE_CONFIG_TABLE, null, values);
        db.close();
        if (a == -1) {
            return false;
        } else {
            return true;
        }

    }

    public ArrayList<LTLineConfigModel> getLTLineConfigSpinnerData() {
        ArrayList<LTLineConfigModel> data = new ArrayList<>();
        String query = "SELECT * FROM LT_LINE_CONFIG_TABLE";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    LTLineConfigModel model = new LTLineConfigModel();
                    model.setPHASE(cursor.getString(0));
                    model.setLINE_CONFIG(cursor.getString(1));
                    model.setTYPE(cursor.getString(2));
                    model.setFACTOR(cursor.getString(3));

                    data.add(model);
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        } finally {
            db.close();
        }
        return data;
    }

    public boolean clearLTLineConfig() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            db.delete(LT_LINE_CONFIG_TABLE, null, null);
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
    }


    private static final String LT_CKT_TABLE = "LT_CKT_TABLE";
    private static final String LT_CKT_NAME = "LT_CKT_NAME";
    private static final String IS_LT_CKT_SURVEY_COMPLETED = "IS_LT_CKT_SURVEY_COMPLETED";
    String CREATE_LT_CKT_TABLE = "CREATE TABLE " + LT_CKT_TABLE + "("
            + EST_NO_11KV + " TEXT ,"
            + POLE_NO_11KV + " TEXT ,"
            + LT_CKT_NAME + " TEXT ,"
            + IS_LT_CKT_SURVEY_COMPLETED + " TEXT ,"

            + FLAG_11KV + " TEXT " + ")";


    //LT
    private static final String SURVEY_TABLE_LT = "SURVEY_TABLE_LT";
    private static final String EST_NO_LT = "EST_NO_LT";
    private static final String FROM_POLE_LT = "FROM_POLE_LT";
    private static final String POLE_NO_LT = "POLE_NO_LT";
    private static final String LINE_TYPE_LT = "LINE_TYPE_LT";
    private static final String No_OF_STRUT_LT = "No_OF_STRUT_11KV";
    private static final String No_OF_STAY_LT = "No_OF_STAY_LT";
    private static final String EXI_POLE_NO_LT = "EXI_POLE_NO_LT";
    private static final String POLE_LOCATION_TYPE_LT = "POLE_LOCATION_TYPE_LT";
    private static final String POLE_HEIGHT_LT = "POLE_HEIGHT_LT";
    private static final String CONDUTCOR_SIZE_LT = "CONDUTCOR_SIZE_LT";
    private static final String LINE_SURVEY_TYPE_LT = "LINE_SURVEY_TYPE_LT";
    private static final String DTR_PHASE_LT = "DTR_PHASE_LT";
    private static final String DTR_CAPACITY_LT = "DTR_CAPACITY_LT";
    private static final String DTR_MOUNTING_LT = "DTR_MOUNTING_LT";
    private static final String DTR_SURVEY_TYPE_LT = "DTR_SURVEY_TYPE_LT";
    private static final String LOCATION_ID_LT = "LOCATION_ID_LT";
    private static final String NUMB_9M_STRUTS_LT = "NUMB_9M_STRUTS_LT";
    private static final String NUMB_8M_STRUTS_LT = "NUMB_8M_STRUTS_LT";
    private static final String NUMB_STAYS_LT = "NUMB_STAYS_LT";
    private static final String NUMBERS_LT_FEEDERS = "NUMBERS_LT_FEEDERS";
    private static final String DTR_STRUCTURE_CODE_LT = "DTR_STRUCTURE_CODE_LT";
    private static final String POLE_DESC_LT = "POLE_DESC_LT";
    private static final String POLE_SAP_CODE_LT = "POLE_SAP_CODE_LT";
    private static final String CONDUCTOR_DESC_LT = "CONDUCTOR_DESC_LT";
    private static final String CONDUCTOR_SAP_CODE_LT = "CONDUCTOR_SAP_CODE_LT";
    private static final String AB_SWITCH_LT = "AB_SWITCH_LT";
    private static final String AB_DESC_LT = "AB_DESC_LT";
    private static final String SS_ID_LT = "SS_ID_LT";
    private static final String SS_NAME_LT = "SS_NAME_LT";
    private static final String SS_TYPE_LT = "SS_TYPE_LT";
    private static final String FEEDER_ID_LT = "FEEDER_ID_LT";
    private static final String FEEDER_NAME_LT = "FEEDER_NAME_LT";

    private static final String TAPPINGXARM_CODE_LT = "TAPPINGXARM_CODE_LT";
    private static final String TAPPINGXARM_DESC_LT = "TAPPINGXARM_DESC_LT";

    private static final String SIDEARM_CODE_LT = "SIDEARM_CODE_LT";
    private static final String SIDEARM_DESC_LT = "SIDEARM_DESC_LT";

    private static final String PIN_INS_CODE_LT = "PIN_INS_CODE_LT";
    private static final String PIN_INS_DESC_LT = "PIN_INS_DESC_LT";

    private static final String DISC_INS_CODE_LT = "DISC_INS_CODE_LT";
    private static final String DISC_INS_DESC_LT = "DISC_INS_DESC_LT";

    private static final String METAL_PARTS_CODE_LT = "METAL_PARTS_CODE_LT";
    private static final String METAL_PARTS_DESC_LT = "METAL_PARTS_DESC_LT";
    private static final String COUNT_PIN_INS_LT = "COUNT_PIN_INS_LT";
    private static final String COUNT_DISC_INS_LT = "COUNT_DISC_INS_LT";
    private static final String COUNT_SIDE_ARM_LT = "COUNT_SIDE_ARM_LT";
    private static final String COUNT_METALS_PARTS_LT = "COUNT_METALS_PARTS_LT";
    private static final String COUNT_TAPPING_XARM_LT = "COUNT_TAPPING_XARM_LT";


    private static final String STAYCLAMP_11M_DESC_LT = "STAYCLAMP_11M_DESC_LT";
    private static final String STAYCLAMP_11M_CODE_LT = "STAYCLAMP_11M_CODE_LT";
    private static final String COUNT__STAYCLAMP_11M_LT = "COUNT__STAYCLAMP_11M_LT";
    private static final String STAYCLAMP_9M_DESC_LT = "STAYCLAMP_9M_DESC_LT";
    private static final String STAYCLAMP_9M_CODE_LT = "STAYCLAMP_9M_CODE_LT";
    private static final String COUNT__STAYCLAMP_9M_LT = "COUNT__STAYCLAMP_9M_LT";
    private static final String STAYCLAMP_8M_DESC_LT = "STAYCLAMP_8M_DESC_LT";
    private static final String STAYCLAMP_8M_CODE_LT = "STAYCLAMP_8M_CODE_LT";
    private static final String COUNT__STAYCLAMP_8M_LT = "COUNT__STAYCLAMP_8M_LT";
    private static final String STAYCLAMP_SPUN_DESC_LT = "STAYCLAMP_SPUN_DESC_LT";
    private static final String STAYCLAMP_SPUN_CODE_LT = "STAYCLAMP_SPUN_CODE_LT";
    private static final String COUNT__STAYCLAMP_SPUN_LT = "COUNT__STAYCLAMP_SPUN_LT";
    private static final String GI_PIN_CODE_LT = "GI_PIN_CODE_LT";
    private static final String GI_PIN_DESC_LT = "GI_PIN_DESC_LT";
    private static final String COUNT_GI_PIN_LT = "COUNT_GI_PIN_LT";
    private static final String PIN_CUT_LT = "PIN_CUT_LT";
    private static final String END_POLE_LT = "END_POLE_LT";
    private static final String NEW_CKT_LT = "NEW_CKT_LT";


    private static final String POLE_SURVEY_TYPE_LT = "POLE_SURVEY_TYPE_LT";
    private static final String IS_MULTI_CKT_LT = "IS_MULTI_CKT_LT";
    private static final String MULTI_CKT_33_LT = "MULTI_CKT_33_LT";
    private static final String MULTI_CKT_11_LT = "MULTI_CKT_11_LT";
    private static final String MULTI_CKT_LT_LT = "MULTI_CKT_LT_LT";
    private static final String POLE_TYPE_LT = "POLE_TYPE_LT";
    private static final String POLE_SUPPORT_LT = "POLE_SUPPORT_LT";
    private static final String POLE_CONFIG_LT = "POLE_CONFIG_LT";
    private static final String REF_ID_LT = "REF_ID_LT";
    private static final String LAT_LT = "LAT_LT";
    private static final String LON_LT = "LON_LT";
    private static final String FLAG_LT = "FLAG_LT";

    String CREATE_SURVEY_TABLE_LT = "CREATE TABLE " + SURVEY_TABLE_LT + "("
            + EST_NO_LT + " TEXT ,"
            + FROM_POLE_LT + " TEXT ,"
            + POLE_NO_LT + " TEXT ,"
            + LINE_TYPE_LT + " TEXT ,"
            + No_OF_STRUT_LT + " TEXT ,"
            + No_OF_STAY_LT + " TEXT ,"
            + EXI_POLE_NO_LT + " TEXT ,"
            + POLE_LOCATION_TYPE_LT + " TEXT ,"
            + POLE_HEIGHT_LT + " TEXT ,"
            + CONDUTCOR_SIZE_LT + " TEXT ,"
            + LINE_SURVEY_TYPE_LT + " TEXT ,"
            + DTR_PHASE_LT + " TEXT ,"
            + DTR_CAPACITY_LT + " TEXT ,"
            + DTR_MOUNTING_LT + " TEXT ,"
            + DTR_SURVEY_TYPE_LT + " TEXT ,"
            + LOCATION_ID_LT + " TEXT ,"
            + NUMB_9M_STRUTS_LT + " TEXT ,"
            + NUMB_8M_STRUTS_LT + " TEXT ,"
            + NUMB_STAYS_LT + " TEXT ,"
            + NUMBERS_LT_FEEDERS + " TEXT ,"
            + DTR_STRUCTURE_CODE_LT + " TEXT ,"
            + POLE_DESC_LT + " TEXT ,"
            + POLE_SAP_CODE_LT + " TEXT ,"
            + CONDUCTOR_DESC_LT + " TEXT ,"
            + CONDUCTOR_SAP_CODE_LT + " TEXT ,"
            + AB_SWITCH_LT + " TEXT ,"
            + AB_DESC_LT + " TEXT ,"
            + SS_ID_LT + " TEXT ,"
            + SS_NAME_LT + " TEXT ,"
            + SS_TYPE_LT + " TEXT ,"
            + FEEDER_ID_LT + " TEXT ,"
            + FEEDER_NAME_LT + " TEXT ,"

            + POLE_SURVEY_TYPE_LT + " TEXT ,"
            + IS_MULTI_CKT_LT + " TEXT ,"
            + MULTI_CKT_33_LT + " TEXT ,"
            + MULTI_CKT_11_LT + " TEXT ,"
            + MULTI_CKT_LT_LT + " TEXT ,"
            + POLE_TYPE_LT + " TEXT ,"
            + POLE_SUPPORT_LT + " TEXT ,"
            + POLE_CONFIG_LT + " TEXT ,"
            + REF_ID_LT + " TEXT ,"
            + LAT_LT + " TEXT ,"
            + LON_LT + " TEXT ,"

            + TAPPINGXARM_CODE_LT + " TEXT ,"
            + TAPPINGXARM_DESC_LT + " TEXT ,"
            + SIDEARM_CODE_LT + " TEXT ,"
            + SIDEARM_DESC_LT + " TEXT ,"
            + PIN_INS_CODE_LT + " TEXT ,"
            + PIN_INS_DESC_LT + " TEXT ,"
            + DISC_INS_CODE_LT + " TEXT ,"
            + DISC_INS_DESC_LT + " TEXT ,"
            + METAL_PARTS_CODE_LT + " TEXT ,"
            + METAL_PARTS_DESC_LT + " TEXT ,"

            + COUNT_PIN_INS_LT + " TEXT ,"
            + COUNT_DISC_INS_LT + " TEXT ,"
            + COUNT_SIDE_ARM_LT + " TEXT ,"
            + COUNT_METALS_PARTS_LT + " TEXT ,"
            + COUNT_TAPPING_XARM_LT + " TEXT ,"


            + STAYCLAMP_11M_DESC_LT + " TEXT ,"
            + STAYCLAMP_11M_CODE_LT + " TEXT ,"
            + COUNT__STAYCLAMP_11M_LT + " TEXT ,"
            + STAYCLAMP_9M_DESC_LT + " TEXT ,"
            + STAYCLAMP_9M_CODE_LT + " TEXT ,"
            + COUNT__STAYCLAMP_9M_LT + " TEXT ,"
            + STAYCLAMP_8M_DESC_LT + " TEXT ,"
            + STAYCLAMP_8M_CODE_LT + " TEXT ,"
            + COUNT__STAYCLAMP_8M_LT + " TEXT ,"
            + STAYCLAMP_SPUN_DESC_LT + " TEXT ,"
            + STAYCLAMP_SPUN_CODE_LT + " TEXT ,"
            + COUNT__STAYCLAMP_SPUN_LT + " TEXT ,"

            + GI_PIN_CODE_LT + " TEXT ,"
            + GI_PIN_DESC_LT + " TEXT ,"
            + COUNT_GI_PIN_LT + " TEXT ,"
            + PIN_CUT_LT + " TEXT ,"
            + END_POLE_LT + " TEXT ,"
            + NEW_CKT_LT + " TEXT ,"


            + FLAG_LT + " TEXT " + ")";


    //NEW TABLE
    private static final String SURVEY_TABLE_NEW = "SURVEY_TABLE_NEW";


    private static final String EXT_LOCATION_NEW = "EXT_LOCATION_NEW";
    private static final String EXISTING_POLES_TABLE_NEW = "EXISTING_POLES_TABLE_NEW";
    private static final String EST_NO_NEW = "EST_NO_NEW";
    private static final String FROM_POLE_NEW = "FROM_POLE_NEW";
    private static final String POLE_NO_NEW = "POLE_NO_NEW";
    private static final String LINE_TYPE_NEW = "LINE_TYPE_NEW";
    private static final String POLE_SURVEY_TYPE_NEW = "POLE_SURVEY_TYPE_NEW";
    private static final String IS_MULTI_CKT_NEW = "IS_MULTI_CKT_NEW";
    private static final String MULTI_CKT_33_NEW = "MULTI_CKT_33_NEW";
    private static final String MULTI_CKT_11_NEW = "MULTI_CKT_11_NEW";
    private static final String MULTI_CKT_LT_NEW = "MULTI_CKT_LT_NEW";
    private static final String POLE_TYPE_NEW = "POLE_TYPE_NEW";
    private static final String POLE_SUPPORT_NEW = "POLE_SUPPORT_NEW";
    private static final String No_OF_STRUT_NEW = "No_OF_STRUT_NEW";
    private static final String No_OF_STAY_NEW = "No_OF_STAY_NEW";
    private static final String EXI_POLE_NO_NEW = "EXI_POLE_NO_NEW";
    private static final String POLE_LOCATION_TYPE_NEW = "POLE_LOCATION_TYPE_NEW";
    private static final String POLE_HEIGHT_NEW = "POLE_HEIGHT_NEW";
    private static final String CONDUTCOR_SIZE_NEW = "CONDUTCOR_SIZE_NEW";
    private static final String LINE_SURVEY_TYPE_NEW = "LINE_SURVEY_TYPE_NEW";
    private static final String DTR_PHASE_NEW = "DTR_PHASE_NEW";
    private static final String DTR_CAPACITY_NEW = "DTR_CAPACITY_NEW";
    private static final String DTR_MOUNTING_NEW = "DTR_MOUNTING_NEW";
    private static final String DTR_SURVEY_TYPE_NEW = "DTR_SURVEY_TYPE_NEW";
    private static final String LOCATION_ID_NEW = "LOCATION_ID_NEW";
    private static final String NUMB_9M_STRUTS_NEW = "NUMB_9M_STRUTS_NEW";
    private static final String NUMB_8M_STRUTS_NEW = "NUMB_8M_STRUTS_NEW";
    private static final String NUMB_STAYS_NEW = "NUMB_STAYS_NEW";
    private static final String NUMBERS_OF_FEEDERS_NEW = "NUMBERS_OF_FEEDERS_NEW";
    private static final String DTR_STRUCTURE_CODE_NEW = "DTR_STRUCTURE_CODE_NEW";
    private static final String POLE_DESC_NEW = "POLE_DESC_NEW";
    private static final String POLE_SAP_CODE_NEW = "POLE_SAP_CODE_NEW";
    private static final String CONDUCTOR_DESC_NEW = "CONDUCTOR_DESC_NEW";
    private static final String CONDUCTOR_SAP_CODE_NEW = "CONDUCTOR_SAP_CODE_NEW";
    private static final String DTR_DESC_NEW = "DTR_DESC_NEW";
    private static final String DTR_SAP_CODE_NEW = "DTR_SAP_CODE_NEW";
    private static final String AB_SWITCH_NEW = "AB_SWITCH_NEW";
    private static final String AB_DESC_NEW = "AB_DESC_NEW";
    private static final String SS_ID_NEW = "SS_ID_NEW";
    private static final String SS_NAME_NEW = "SS_NAME_NEW";
    private static final String SS_TYPE_NEW = "SS_TYPE_NEW";
    private static final String FEEDER_ID_NEW = "FEEDER_ID_NEW";
    private static final String FEEDER_NAME_NEW = "FEEDER_NAME_NEW";
    private static final String DTR_AB_DESC_NEW = "DTR_AB_DESC_NEW";
    private static final String DTR_AB_CODE_NEW = "DTR_AB_CODE_NEW";
    private static final String HG_FUSE_DESC_NEW = "HG_FUSE_DESC_NEW";
    private static final String HG_FUSE_CODE_NEW = "HG_FUSE_CODE_NEW";
    private static final String LT_FUSE_DESC_NEW = "LT_FUSE_DESC_NEW";
    private static final String LT_FUSE_CODE_NEW = "LT_FUSE_CODE_NEW";
    private static final String CI_SAP_CODE_NEW = "CI_SAP_CODE_NEW";
    private static final String CI_DESC_NEW = "CI_DESC_NEW";
    private static final String GI_SAP_CODE_NEW = "GI_SAP_CODE_NEW";
    private static final String GI_DESC_NEW = "GI_DESC_NEW";
    private static final String COUNT_CI_NEW = "COUNT_CI_NEW";
    private static final String COUNT_GI_NEW = "COUNT_GI_NEW";
    private static final String COUNT_TAPPING_XARM_NEW = "COUNT_TAPPING_XARM_NEW";
    private static final String COUNT_V_XARM_NEW = "COUNT_V_XARM_NEW";
    private static final String COUNT_TOP_CLEAT_NEW = "COUNT_TOP_CLEAT_NEW";
    private static final String COUNT_PIN_INS_NEW = "COUNT_PIN_INS_NEW";
    private static final String COUNT_DISC_INS_NEW = "COUNT_DISC_INS_NEW";
    private static final String COUNT_SIDE_ARM_NEW = "COUNT_SIDE_ARM_NEW";
    private static final String COUNT_METALS_PARTS_NEW = "COUNT_METALS_PARTS_NEW";
    private static final String TAPPINGXARM_CODE_NEW = "TAPPINGXARM_CODE_NEW";
    private static final String TAPPINGXARM_DESC_NEW = "TAPPINGXARM_DESC_NEW";
    private static final String VXARM_CODE_NEW = "VXARM_CODE_NEW";
    private static final String VXARM_DESC_NEW = "VXARM_DESC_NEW";
    private static final String SIDEARM_CODE_NEW = "SIDEARM_CODE_NEW";
    private static final String SIDEARM_DESC_NEW = "SIDEARM_DESC_NEW";
    private static final String CHANNEL_CODE_NEW = "CHANNEL_CODE_NEW";
    private static final String CHANNEL_DESC_NEW = "CHANNEL_DESC_NEW";
    private static final String TOPCLEAT_CODE_NEW = "TOPCLEAT_CODE_NEW";
    private static final String TOPCLEAT_DESC_NEW = "TOPCLEAT_DESC_NEW";
    private static final String PIN_INS_CODE_NEW = "PIN_INS_CODE_NEW";
    private static final String PIN_INS_DESC_NEW = "PIN_INS_DESC_NEW";
    private static final String COUNT_PIN_NEW = "COUNT_PIN_NEW";
    private static final String DISC_INS_CODE_NEW = "DISC_INS_CODE_NEW";
    private static final String DISC_INS_DESC_NEW = "DISC_INS_DESC_NEW";
    private static final String COUNT_DISC_NEW = "COUNT_DISC_NEW";
    private static final String GIPIN_CODE_NEW = "GIPIN_CODE_NEW";
    private static final String GIPIN_DESC_NEW = "GIPIN_DESC_NEW";
    private static final String COUNT_GIPIN_NEW = "COUNT_GIPIN_NEW";
    private static final String METAL_PARTS_CODE_NEW = "METAL_PARTS_CODE_NEW";
    private static final String METAL_PARTS_DESC_NEW = "METAL_PARTS_DESC_NEW";
    private static final String COUNT_METAL_PARTS_NEW = "COUNT_METAL_PARTS_NEW";
    private static final String COUNT_CHANNEL_NEW = "COUNT_CHANNEL_NEW";
    private static final String BACK_CLAMPS_CODE_NEW = "BACK_CLAMPS_CODE_NEW";
    private static final String BACK_CLAMPS_DESC_NEW = "BACK_CLAMPS_DESC_NEW";
    private static final String COUNT_BACK_CLAMPS_NEW = "COUNT_BACK_CLAMPS_NEW";
    private static final String STAYCLAMP_11M_DESC_NEW = "STAYCLAMP_11M_DESC_NEW";
    private static final String STAYCLAMP_11M_CODE_NEW = "STAYCLAMP_11M_CODE_NEW";
    private static final String COUNT__STAYCLAMP_11M_NEW = "COUNT__STAYCLAMP_11M_NEW";
    private static final String STAYCLAMP_9M_DESC_NEW = "STAYCLAMP_9M_DESC_NEW";
    private static final String STAYCLAMP_9M_CODE_NEW = "STAYCLAMP_9M_CODE_NEW";
    private static final String COUNT__STAYCLAMP_9M_NEW = "COUNT__STAYCLAMP_9M_NEW";
    private static final String STAYCLAMP_8M_DESC_NEW = "STAYCLAMP_8M_DESC_NEW";
    private static final String STAYCLAMP_8M_CODE_NEW = "STAYCLAMP_8M_CODE_NEW";
    private static final String COUNT__STAYCLAMP_8M_NEW = "COUNT__STAYCLAMP_8M_NEW";
    private static final String STAYCLAMP_SPUN_DESC_NEW = "STAYCLAMP_SPUN_DESC_NEW";
    private static final String STAYCLAMP_SPUN_CODE_NEW = "STAYCLAMP_SPUN_CODE_NEW";
    private static final String COUNT__STAYCLAMP_SPUN_NEW = "COUNT__STAYCLAMP_SPUN_NEW";
    private static final String GI_PIN_CODE_NEW = "GI_PIN_CODE_NEW";
    private static final String GI_PIN_DESC_NEW = "GI_PIN_DESC_NEW";
    private static final String COUNT_GI_PIN_NEW = "COUNT_GI_PIN_NEW";
    private static final String NEW_DATA_ADDED_NEW = "NEW_DATA_ADDED_NEW";

    private static final String DTR_MOUNTING_DESC_NEW = "DTR_MOUNTING_DESC_NEW";
    private static final String DTR_MOUNTING_CODE_NEW = "DTR_MOUNTING_CODE_NEW";
    private static final String AB_MOUNTING_DESC_NEW = "AB_MOUNTING_DESC_NEW ";
    private static final String AB_MOUNTING_CODE_NEW = "AB_MOUNTING_CODE_NEW ";

    private static final String MT_CODE_NEW = "MT_CODE_NEW ";
    private static final String MT_DESC_NEW = "MT_DESC_NEW ";
    private static final String COUNT_MT_NEW = "COUNT_MT_NEW ";
    private static final String MT_LOC_NO_NEW = "MT_LOC_NO_NEW ";
    private static final String MT_SURVEY_TYPE_NEW = "MT_SURVEY_TYPE_NEW ";
    private static final String PIN_CUT_NEW = "PIN_CUT_NEW ";
    private static final String END_POLE_NEW = "END_POLE_NEW ";
    private static final String IS_NEW_SURVEY = "IS_NEW_SURVEY ";
    private static final String SERVICE_POLE = "SERVICE_POLE ";
    private static final String POLE_CONCRETE = "POLE_CONCRETE";
    private static final String POLE_STRUCTURE = "POLE_STRUCTURE";
    private static final String SURVEY_FROM = "SURVEY_FROM";
    private static final String WORK_ORDER = "WORK_ORDER";
    private static final String GIS_ID_NEW = "GIS_ID_NEW";
    private static final String HTSC_NO_NEW = "HTSC_NO_NEW";

    //LOCATIONS EDIT
    private static final String IS_TRANSMITTED_DATA_UPDATED = "IS_TRANSMITTED_DATA_UPDATED";
    private static final String IS_TRANSMITTED_LOCATION = "IS_TRANSMITTED_LOCATION";
    private static final String REF_VOLTAGE = "REF_VOLTAGE";
    private static final String EXT_ID = "EXT_ID";
    private static final String LT_AVAILABLE_NEW = "LT_AVAILABLE_NEW";
    private static final String COUNT_CKT_NEW = "COUNT_CKT_NEW";
    private static final String LOAD_TYPE_NEW = "LOAD_TYPE_NEW";
    private static final String INTERMEDIATE_POLE = "INTERMEDIATE_POLE";
    private static final String OTHER_FEEDER_NEW = "OTHER_FEEDER_NEW";
    private static final String CROSS_6_3KV_ID_NEW = "CROSS_6_3KV_ID_NEW";
    private static final String CROSSING_TYPE_NEW = "CROSSING_TYPE_NEW";
    private static final String CROSS_33KV_ID_NEW = "CROSS_33KV_ID_NEW";
    private static final String CROSS_LT_CKT_NEW = "CROSS_LT_CKT_NEW";
    private static final String CROSS_COMMUN_NEW = "CROSS_COMMUN_NEW";
    private static final String CROSS_EHT_NEW = "CROSS_EHT_NEW";
    private static final String CROSS_REMARKS_NEW = "CROSS_REMARKS_NEW";
    private static final String CROSS_11KV_ID_NEW = "CROSS_11KV_ID_NEW";
    private static final String REMARKS_NEW = "REMARKS_NEW";
    private static final String POLE_PAINT_CODE_NEW = "POLE_PAINT_CODE_NEW";
    private static final String CURRENT_LAT = "CURRENT_LAT";
    private static final String CURRENT_LONG = "CURRENT_LONG";
    private static final String APP_VERSION = "APP_VERSION";
    private static final String APP_VERSION2 = "APP_VERSION2";
    private static final String INTID1 = "INTID1";
    private static final String INTID2 = "INTID2";
    private static final String DTR_SNO_NEW = "DTR_SNO_NEW";
    private static final String DTR_MAKE_NEW = "DTR_MAKE_NEW";
    private static final String DTR_LOCATION_NEW = "DTR_LOCATION_NEW";
    private static final String EDTR_PHASE_NEW = "EDTR_PHASE_NEW";
    private static final String EDTR_CAPACITY_NEW = "EDTR_CAPACITY_NEW";
    private static final String EDTR_EDITED_NEW = "EDTR_EDITED_NEW";
    private static final String SERVICE_TYPE_NEW = "SERVICE_TYPE_NEW";
    private static final String DTR_SHIFTED_NEW = "DTR_SHIFTED_NEW";

    private static final String REG_NUM_NEW = "REG_NUM_NEW";
    private static final String DTR_ID_NEW = "DTR_ID_NEW";
    private static final String DT_SHIFT_REMARKS_NEW = "DT_SHIFT_REMARKS_NEW";

    private static final String SURVEY_MODE_NEW = "SURVEY_MODE_NEW";
    private static final String CTPT_NEW = "CTPT_NEW";
    private static final String NEW_MAPPED_NEW = "NEW_MAPPED_NEW";
    private static final String E_POLE_NEW = "E_POLE_NEW";
    private static final String FDR_LOC_NO_NEW = "FDR_LOC_NO_NEW";
    private static final String FDR_SOURCE_NO_NEW = "FDR_SOURCE_NO_NEW";
    private static final String LTCKT_LOC_NO_NEW = "LTCKT_LOC_NO_NEW";
    private static final String LTCKT_SOURCE_NO_NEW = "LTCKT_SOURCE_NO_NEW";
    private static final String GPS_ACCURACY_NEW = "GPS_ACCURACY_NEW";
    private static final String DEVICE_ID_NEW = "DEVICE_ID_NEW";

    private static final String RDSS_SCHEME_TYPE_NEW = "RDSS_SCHEME_TYPE_NEW";
    private static final String PACKAGE_NO_NEW = "PACKAGE_NO_NEW";
    private static final String DT_VOLATAGE_RATIO_NEW = "DT_VOLATAGE_RATIO_NEW";
    private static final String DT_INSTL_DT_NEW = "DT_INSTL_DT_NEW";
    private static final String DT_MANF_DT_NEW = "DT_MANF_DT_NEW";

    public static final String IMAGE_1 = "IMAGE_1";
    public static final String IMAGE_2 = "IMAGE_2";
    private static final String IMAGE_3 = "IMAGE_3";
    private static final String IS_IMAGE_TRANSMITTED = "IS_IMAGE_TRANSMITTED";
    private static final String ADDRESS_1 = "ADDRESS_1";
    private static final String ADDRESS_2 = "ADDRESS_2";

    private static final String ASSET_TYPE = "ASSET_TYPE";
    private static final String BLOCK_PREMISES = "BLOCK_PREMISES";
    private static final String VILLAGE = "VILLAGE";
    private static final String TOWN_CITY = "TOWN_CITY";
    private static final String STATE = "STATE";
    private static final String BRANCH_POINT = "BRANCH_POINT";
    private static final String ACTUAL_LENGTH = "ACTUAL_LENGTH";
    private static final String TARGET_FEEDER_ID = "TARGET_FEEDER_ID";
    private static final String TARGET_FEEDER_NAME = "TARGET_FEEDER_NAME";


    private static final String NEW_CKT_NEW = "NEW_CKT_NEW";


    private static final String POLE_CONFIG_NEW = "POLE_CONFIG_NEW";
    private static final String REF_ID_NEW = "REF_ID_NEW";
    private static final String LAT_NEW = "LAT_NEW";
    private static final String LON_NEW = "LON_NEW";
    private static final String SURVEY_TYPE_NEW = "SURVEY_TYPE_NEW";
    private static final String IS_EXISTING_LOCATION_NEW = "IS_EXISTING_LOCATION_NEW";
    private static final String EXISTING_LOCATION_33_NEW = "EXISTING_LOCATION_33_NEW";
    private static final String EXISTING_LOCATION_11_NEW = "EXISTING_LOCATION_11_NEW";
    private static final String EXISTING_LOCATION_LT_NEW = "EXISTING_LOCATION_LT_NEW";
    private static final String TAPPING_LOCATION_NEW = "TAPPING_LOCATION_NEW";
    private static final String FROM_POLE_LATITUDE_NEW = "FROM_POLE_LATITUDE_NEW";
    private static final String FROM_POLE_LONGITUDE_NEW = "FROM_POLE_LONGITUDE_NEW";
    private static final String MULTI_CKT_33_POLE_NO_NEW = "MULTI_CKT_33_POLE_NO_NEW";
    private static final String MULTI_CKT_11_POLE_NO_NEW = "MULTI_CKT_11_POLE_NO_NEW";
    private static final String MULTI_CKT_LT_POLE_NO_NEW = "MULTI_CKT_LT_POLE_NO_NEW";
    private static final String FLAG_NEW = "FLAG_NEW";
    String CREATE_SURVEY_TABLE_NEW = "CREATE TABLE " + SURVEY_TABLE_NEW + "("
            + EST_NO_NEW + " TEXT ,"
            + FROM_POLE_NEW + " TEXT ,"
            + POLE_NO_NEW + " TEXT ,"
            + LINE_TYPE_NEW + " TEXT ,"
            + POLE_SURVEY_TYPE_NEW + " TEXT ,"
            + IS_MULTI_CKT_NEW + " TEXT ,"
            + MULTI_CKT_33_NEW + " TEXT ,"
            + MULTI_CKT_11_NEW + " TEXT ,"
            + MULTI_CKT_LT_NEW + " TEXT ,"
            + POLE_TYPE_NEW + " TEXT ,"
            + POLE_SUPPORT_NEW + " TEXT ,"
            + POLE_CONFIG_NEW + " TEXT ,"
            + REF_ID_NEW + " TEXT ,"
            + LAT_NEW + " TEXT ,"
            + LON_NEW + " TEXT ,"
            + SURVEY_TYPE_NEW + " TEXT ,"
            + IS_EXISTING_LOCATION_NEW + " TEXT ,"
            + EXISTING_LOCATION_33_NEW + " TEXT ,"
            + EXISTING_LOCATION_11_NEW + " TEXT ,"
            + EXISTING_LOCATION_LT_NEW + " TEXT ,"
            + TAPPING_LOCATION_NEW + " TEXT ,"
            + FROM_POLE_LATITUDE_NEW + " TEXT ,"
            + FROM_POLE_LONGITUDE_NEW + " TEXT ,"
            + MULTI_CKT_33_POLE_NO_NEW + " TEXT ,"
            + MULTI_CKT_11_POLE_NO_NEW + " TEXT ,"
            + MULTI_CKT_LT_POLE_NO_NEW + " TEXT ,"
            + No_OF_STRUT_NEW + " TEXT ,"
            + No_OF_STAY_NEW + " TEXT ,"
            + EXI_POLE_NO_NEW + " TEXT ,"
            + POLE_LOCATION_TYPE_NEW + " TEXT ,"
            + POLE_HEIGHT_NEW + " TEXT ,"
            + CONDUTCOR_SIZE_NEW + " TEXT ,"
            + LINE_SURVEY_TYPE_NEW + " TEXT ,"
            + DTR_PHASE_NEW + " TEXT ,"
            + DTR_CAPACITY_NEW + " TEXT ,"
            + DTR_MOUNTING_NEW + " TEXT ,"
            + DTR_SURVEY_TYPE_NEW + " TEXT ,"
            + LOCATION_ID_NEW + " TEXT ,"
            + NUMB_9M_STRUTS_NEW + " TEXT ,"
            + NUMB_8M_STRUTS_NEW + " TEXT ,"
            + NUMB_STAYS_NEW + " TEXT ,"
            + NUMBERS_OF_FEEDERS_NEW + " TEXT ,"
            + DTR_STRUCTURE_CODE_NEW + " TEXT ,"
            + POLE_DESC_NEW + " TEXT ,"
            + POLE_SAP_CODE_NEW + " TEXT ,"
            + CONDUCTOR_DESC_NEW + " TEXT ,"
            + CONDUCTOR_SAP_CODE_NEW + " TEXT ,"
            + DTR_DESC_NEW + " TEXT ,"
            + DTR_SAP_CODE_NEW + " TEXT ,"
            + AB_SWITCH_NEW + " TEXT ,"
            + AB_DESC_NEW + " TEXT ,"
            + SS_ID_NEW + " TEXT ,"
            + SS_NAME_NEW + " TEXT ,"
            + SS_TYPE_NEW + " TEXT ,"
            + FEEDER_ID_NEW + " TEXT ,"
            + FEEDER_NAME_NEW + " TEXT ,"
            + DTR_AB_DESC_NEW + " TEXT ,"
            + DTR_AB_CODE_NEW + " TEXT ,"
            + HG_FUSE_DESC_NEW + " TEXT ,"
            + HG_FUSE_CODE_NEW + " TEXT ,"
            + LT_FUSE_DESC_NEW + " TEXT ,"
            + LT_FUSE_CODE_NEW + " TEXT ,"
            + CI_SAP_CODE_NEW + " TEXT ,"
            + CI_DESC_NEW + " TEXT ,"
            + GI_SAP_CODE_NEW + " TEXT ,"
            + GI_DESC_NEW + " TEXT ,"
            + COUNT_CI_NEW + " TEXT ,"
            + COUNT_GI_NEW + " TEXT ,"
            + COUNT_TAPPING_XARM_NEW + " TEXT ,"
            + COUNT_V_XARM_NEW + " TEXT ,"
            + COUNT_TOP_CLEAT_NEW + " TEXT ,"
            + COUNT_PIN_INS_NEW + " TEXT ,"
            + COUNT_DISC_INS_NEW + " TEXT ,"
            + COUNT_SIDE_ARM_NEW + " TEXT ,"
            + COUNT_METALS_PARTS_NEW + " TEXT ,"
            + TAPPINGXARM_CODE_NEW + " TEXT ,"
            + TAPPINGXARM_DESC_NEW + " TEXT ,"
            + VXARM_CODE_NEW + " TEXT ,"
            + VXARM_DESC_NEW + " TEXT ,"
            + SIDEARM_CODE_NEW + " TEXT ,"
            + SIDEARM_DESC_NEW + " TEXT ,"
            + CHANNEL_CODE_NEW + " TEXT ,"
            + CHANNEL_DESC_NEW + " TEXT ,"
            + TOPCLEAT_CODE_NEW + " TEXT ,"
            + TOPCLEAT_DESC_NEW + " TEXT ,"
            + PIN_INS_CODE_NEW + " TEXT ,"
            + PIN_INS_DESC_NEW + " TEXT ,"
            + COUNT_PIN_NEW + " TEXT ,"
            + DISC_INS_CODE_NEW + " TEXT ,"
            + DISC_INS_DESC_NEW + " TEXT ,"
            + COUNT_DISC_NEW + " TEXT ,"
            + GIPIN_CODE_NEW + " TEXT ,"
            + GIPIN_DESC_NEW + " TEXT ,"
            + COUNT_GIPIN_NEW + " TEXT ,"
            + METAL_PARTS_CODE_NEW + " TEXT ,"
            + METAL_PARTS_DESC_NEW + " TEXT ,"
            + COUNT_METAL_PARTS_NEW + " TEXT ,"
            + COUNT_CHANNEL_NEW + " TEXT ,"
            + BACK_CLAMPS_CODE_NEW + " TEXT ,"
            + BACK_CLAMPS_DESC_NEW + " TEXT ,"
            + COUNT_BACK_CLAMPS_NEW + " TEXT ,"
            + STAYCLAMP_11M_DESC_NEW + " TEXT ,"
            + STAYCLAMP_11M_CODE_NEW + " TEXT ,"
            + COUNT__STAYCLAMP_11M_NEW + " TEXT ,"
            + STAYCLAMP_9M_DESC_NEW + " TEXT ,"
            + STAYCLAMP_9M_CODE_NEW + " TEXT ,"
            + COUNT__STAYCLAMP_9M_NEW + " TEXT ,"
            + STAYCLAMP_8M_DESC_NEW + " TEXT ,"
            + STAYCLAMP_8M_CODE_NEW + " TEXT ,"
            + COUNT__STAYCLAMP_8M_NEW + " TEXT ,"
            + STAYCLAMP_SPUN_DESC_NEW + " TEXT ,"
            + STAYCLAMP_SPUN_CODE_NEW + " TEXT ,"
            + COUNT__STAYCLAMP_SPUN_NEW + " TEXT ,"
            + GI_PIN_CODE_NEW + " TEXT ,"
            + GI_PIN_DESC_NEW + " TEXT ,"
            + COUNT_GI_PIN_NEW + " TEXT ,"
            + NEW_DATA_ADDED_NEW + " TEXT ,"
            + DTR_MOUNTING_DESC_NEW + " TEXT ,"
            + DTR_MOUNTING_CODE_NEW + " TEXT ,"
            + AB_MOUNTING_DESC_NEW + " TEXT ,"
            + AB_MOUNTING_CODE_NEW + " TEXT ,"
            + MT_CODE_NEW + " TEXT ,"
            + MT_DESC_NEW + " TEXT ,"
            + COUNT_MT_NEW + " TEXT ,"
            + MT_LOC_NO_NEW + " TEXT ,"
            + MT_SURVEY_TYPE_NEW + " TEXT ,"
            + PIN_CUT_NEW + " TEXT ,"
            + END_POLE_NEW + " TEXT ,"
            + IS_NEW_SURVEY + " TEXT ,"
            + SERVICE_POLE + " TEXT ,"
            + EXT_LOCATION_NEW + " TEXT ,"
            + POLE_CONCRETE + " TEXT ,"
            + POLE_STRUCTURE + " TEXT ,"
            + SURVEY_FROM + " TEXT ,"
            + WORK_ORDER + " TEXT ,"
            + GIS_ID_NEW + " TEXT ,"
            + HTSC_NO_NEW + " TEXT ,"
            + IS_TRANSMITTED_DATA_UPDATED + " TEXT ,"
            + IS_TRANSMITTED_LOCATION + " TEXT ,"
            + REF_VOLTAGE + " TEXT ,"
            + EXT_ID + " TEXT ,"
            + PURPOSE + " TEXT ,"
            + LT_AVAILABLE_NEW + " TEXT ,"
            + COUNT_CKT_NEW + " TEXT ,"
            + LOAD_TYPE_NEW + " TEXT ,"
            + INTERMEDIATE_POLE + " TEXT ,"
            + OTHER_FEEDER_NEW + " TEXT ,"
            + CROSS_6_3KV_ID_NEW + " TEXT ,"
            + CROSSING_TYPE_NEW + " TEXT ,"
            + CROSS_33KV_ID_NEW + " TEXT ,"
            + CROSS_LT_CKT_NEW + " TEXT ,"
            + CROSS_COMMUN_NEW + " TEXT ,"
            + CROSS_EHT_NEW + " TEXT ,"
            + CROSS_REMARKS_NEW + " TEXT ,"
            + CROSS_11KV_ID_NEW + " TEXT ,"
            + REMARKS_NEW + " TEXT ,"
            + POLE_PAINT_CODE_NEW + " TEXT ,"
            + CURRENT_LAT + " TEXT ,"
            + CURRENT_LONG + " TEXT ,"
            + APP_VERSION + " TEXT ,"
            + APP_VERSION2 + " TEXT ,"
            + INTID1 + " TEXT ,"
            + INTID2 + " TEXT ,"
            + DTR_SNO_NEW + " TEXT ,"
            + DTR_MAKE_NEW + " TEXT ,"
            + DTR_LOCATION_NEW + " TEXT ,"
            + EDTR_PHASE_NEW + " TEXT ,"
            + EDTR_CAPACITY_NEW + " TEXT ,"
            + EDTR_EDITED_NEW + " TEXT ,"
            + SERVICE_TYPE_NEW + " TEXT ,"
            + DTR_SHIFTED_NEW + " TEXT ,"
            + NEW_CKT_NEW + " TEXT ,"
            + REG_NUM_NEW + " TEXT ,"
            + DTR_ID_NEW + " TEXT ,"
            + DT_SHIFT_REMARKS_NEW + " TEXT ,"
            + SURVEY_MODE_NEW + " TEXT ,"
            + CTPT_NEW + " TEXT ,"
            + NEW_MAPPED_NEW + " TEXT ,"
            + E_POLE_NEW + " TEXT ,"
            + FDR_LOC_NO_NEW + " TEXT ,"
            + FDR_SOURCE_NO_NEW + " TEXT ,"
            + LTCKT_LOC_NO_NEW + " TEXT ,"
            + LTCKT_SOURCE_NO_NEW + " TEXT ,"
            + GPS_ACCURACY_NEW + " TEXT ,"
            + DEVICE_ID_NEW + " TEXT ,"

            + RDSS_SCHEME_TYPE_NEW + " TEXT ,"
            + PACKAGE_NO_NEW + " TEXT ,"
            + DT_VOLATAGE_RATIO_NEW + " TEXT ,"
            + DT_MANF_DT_NEW + " TEXT ,"
            + DT_INSTL_DT_NEW + " TEXT ,"
            + IMAGE_1 + " TEXT ,"
            + IMAGE_2 + " TEXT ,"
            + IMAGE_3 + " TEXT ,"
            + IS_IMAGE_TRANSMITTED + " TEXT ,"
            + ADDRESS_1 + " TEXT ,"
            + ADDRESS_2 + " TEXT ,"
            + ASSET_TYPE + " TEXT ,"
            + BLOCK_PREMISES + " TEXT ,"
            + VILLAGE + " TEXT ,"
            + TOWN_CITY + " TEXT ,"
            + STATE + " TEXT ,"
            + BRANCH_POINT + " TEXT ,"
            + ACTUAL_LENGTH + " TEXT ,"
            + TARGET_FEEDER_ID + " TEXT ,"
            + TARGET_FEEDER_NAME + " TEXT ,"
            + REPEAT_POLE + " TEXT ,"
            + FLAG_NEW + " TEXT " + ")";

    //For Pole Types
    private static final String POLE_TYPES_TABLE = "POLE_TYPES_TABLE";
    private static final String MATERIALPART1 = "MATERIALPART1";
    private static final String MATERIALCODE = "MATERIALCODE";
    private static final String GROUPDESC1 = "GROUPDESC1";
    private static final String MATERIAL_TYPE = "MATERIAL_TYPE";
    private static final String GROUPCODE = "GROUPCODE";
    private static final String UOM = "UOM";
    private static final String POLE_TYPE = "POLE_TYPE";
    private static final String POLE_HEIGHT = "POLE_HEIGHT";

    private static final String CABLE_TYPE = "CABLE_TYPE";
    private static final String CORE = "CORE";
    private static final String CABLE_SIZE = "CABLE_SIZE";

    private static final String VOLTAGE_LEVEL = "VOLTAGE_LEVEL";
    private static final String HT_11KV = "HT_11KV";
    private static final String HT_33KV = "HT_33KV";
    private static final String LT = "LT";
    private static final String MATERIAL = "MATERIAL";

    private static final String CAPACITY = "CAPACITY";
    private static final String PHASE = "PHASE";
    private static final String MT_FLAG = "MT_FLAG";

    private static final String VOLTAGE_LEVEL_33KV = "VOLTAGE_LEVEL_33KV";
    private static final String VOLTAGE_LEVEL_11KV = "VOLTAGE_LEVEL_11KV";
    private static final String VOLTAGE_LEVEL_NEW = "VOLTAGE_LEVEL_NEW";


    String CREATE_EXISTING_POLES_TABLE_33KV = "CREATE TABLE " + EXISTING_POLES_TABLE_33KV + "("


            + FEEDER_ID_33KV + " TEXT ,"
            + VOLTAGE_LEVEL_33KV + " TEXT ,"
            + POLE_NO_33KV + " TEXT ,"
            + FROM_POLE_33KV + " TEXT ,"
            + LINE_TYPE_33KV + " TEXT ,"
            + POLE_CONFIG_33KV + " TEXT ,"
            + POLE_TYPE_33KV + " TEXT ,"
            + POLE_HEIGHT_33KV + " TEXT ,"
            + POLE_SUPPORT_33KV + " TEXT ,"
            + PIN_CUT_33KV + " TEXT ,"
            + LAT_33KV + " TEXT ,"
            + LON_33KV + " TEXT ,"
            + FLAG_33KV + " TEXT " + ")";

    String CREATE_EXISTING_POLES_TABLE_11KV = "CREATE TABLE " + EXISTING_POLES_TABLE_11KV + "("


            + FEEDER_ID_11KV + " TEXT ,"
            + VOLTAGE_LEVEL_11KV + " TEXT ,"
            + POLE_NO_11KV + " TEXT ,"
            + FROM_POLE_11KV + " TEXT ,"
            + LINE_TYPE_11KV + " TEXT ,"
            + POLE_CONFIG_11KV + " TEXT ,"
            + POLE_TYPE_11KV + " TEXT ,"
            + POLE_HEIGHT_11KV + " TEXT ,"
            + POLE_SUPPORT_11KV + " TEXT ,"
            + PIN_CUT_11KV + " TEXT ,"
            + LAT_11KV + " TEXT ,"
            + LON_11KV + " TEXT ,"
            + FLAG_11KV + " TEXT " + ")";


    String CREATE_EXISTING_POLES_TABLE_NEW = "CREATE TABLE " + EXISTING_POLES_TABLE_NEW + "("
            + FEEDER_ID_NEW + " TEXT ,"
            + VOLTAGE_LEVEL_NEW + " TEXT ,"
            + POLE_NO_NEW + " TEXT ,"
            + FROM_POLE_NEW + " TEXT ,"
            + LINE_TYPE_NEW + " TEXT ,"
            + POLE_CONFIG_NEW + " TEXT ,"
            + POLE_TYPE_NEW + " TEXT ,"
            + POLE_HEIGHT_NEW + " TEXT ,"
            + POLE_SUPPORT_NEW + " TEXT ,"
            + PIN_CUT_NEW + " TEXT ,"
            + LAT_NEW + " TEXT ,"
            + LON_NEW + " TEXT ,"
            + IS_EXISTING_LOCATION_NEW + " TEXT ,"
            + EST_NO_NEW + " TEXT ,"
            + FLAG_NEW + " TEXT " + ")";


    String CREATE_POLE_TYPES_TABLE = "CREATE TABLE " + POLE_TYPES_TABLE + "("
            + MATERIALPART1 + " TEXT ,"
            + MATERIALCODE + " TEXT ,"
            + GROUPDESC1 + " TEXT ,"
            + MATERIAL_TYPE + " TEXT ,"
            + GROUPCODE + " TEXT ,"
            + UOM + " TEXT ,"
            + POLE_TYPE + " TEXT ,"
            + POLE_HEIGHT + " TEXT ,"

            + FLAG + " TEXT " + ")";

    private static final String CONDUCTOR_TYPE_TABLE = "CONDUCTOR_TYPE_TABLE";

    String CREATE_CONDUCTOR_TYPE_TABLE = "CREATE TABLE " + CONDUCTOR_TYPE_TABLE + "("
            + MATERIALPART1 + " TEXT ,"
            + MATERIALCODE + " TEXT ,"
            + GROUPDESC1 + " TEXT ,"
            + MATERIAL_TYPE + " TEXT ,"
            + GROUPCODE + " TEXT ,"
            + UOM + " TEXT ,"
            + CABLE_TYPE + " TEXT ,"
            + CORE + " TEXT ,"
            + CABLE_SIZE + " TEXT ,"
            + VOLTAGE_LEVEL + " TEXT ,"
            + HT_11KV + " TEXT ,"
            + HT_33KV + " TEXT ,"
            + LT + " TEXT ,"

            + FLAG + " TEXT " + ")";

    private static final String DTR_TYPE_TABLE = "DTR_TYPE_TABLE";
    String CREATE_DTR_TYPE_TABLE = "CREATE TABLE " + DTR_TYPE_TABLE + "("
            + MATERIALPART1 + " TEXT ,"
            + MATERIALCODE + " TEXT ,"
            + GROUPDESC1 + " TEXT ,"
            + MATERIAL_TYPE + " TEXT ,"
            + GROUPCODE + " TEXT ,"
            + UOM + " TEXT ,"
            + CAPACITY + " TEXT ,"
            + PHASE + " TEXT ,"


            + FLAG + " TEXT " + ")";

    private static final String MATERIAL_MASTER_TABLE = "MATRRIAL_MASTER_TABLE";
    String CREATE_MATERIAL_MASTER_TABLE = "CREATE TABLE " + MATERIAL_MASTER_TABLE + "("
            + MATERIALPART1 + " TEXT ,"
            + MATERIALCODE + " TEXT ,"
            + GROUPDESC1 + " TEXT ,"
            + MATERIAL_TYPE + " TEXT ,"
            + GROUPCODE + " TEXT ,"
            + UOM + " TEXT ,"
            + VOLTAGE_LEVEL + " TEXT ,"
            + HT_33KV + " TEXT ,"
            + HT_11KV + " TEXT ,"
            + LT + " TEXT ,"
            + MATERIAL + " TEXT ,"
            + POLE_TYPE + " TEXT ,"
            + POLE_HEIGHT + " TEXT ,"
            + MT_FLAG + " TEXT ,"


            + FLAG + " TEXT " + ")";


    private static final String DTR_HT_TABLE = "DTR_HT_TABLE";
    private static final String DTR_PHASE_CAPACITIES_TABLE = "DTR_PHASE_CAPACITIES_TABLE";

    String CREATE_DTR_PHASE_CAPACITIES_TABLE = "CREATE TABLE " + DTR_PHASE_CAPACITIES_TABLE + "("
            + PHASE + " TEXT ,"
            + CAPACITY + " TEXT ,"
            + FLAG + " TEXT " + ")";

    private static final String SURVEY_CODE = "SURVEY_CODE";
    String CREATE_DTR_HT_TABLE = "CREATE TABLE " + DTR_HT_TABLE + "("
            + MATERIALCODE + " TEXT ,"
            + MATERAIL_DESC + " TEXT ,"
            + GROUPCODE + " TEXT ,"
            + PHASE + " TEXT ,"
            + CAPACITY + " TEXT ,"
            + GROUPDESC1 + " TEXT ,"
            + MATERIAL_TYPE + " TEXT ,"
            + UOM + " TEXT ,"
            + SURVEY_CODE + " TEXT ,"
            + FLAG + " TEXT " + ")";


    private static final String SECTION_NAME = "SECTION_NAME";
    private static final String DTR_STRUCTURE_CODE = "DTR_STRUCTURE_CODE";
    private static final String DTR_LOCATION = "DTR_LOCATION";
    private static final String DTR_MAKE = "DTR_MAKE";
    private static final String DTR_SLNO = "DTR_SLNO";
    private static final String SECTION_ID = "SECTION_ID";
    private static final String IS_MAPPED = "IS_MAPPED";
    private static final String NO_OF_LT_CKTS = "NO_OF_LT_CKTS";
    private static final String MAPPED_LOCATION_NO = "MAPPED_LOCATION_NO";
    private static final String GLAT = "GLAT";
    private static final String GLONG = "GLONG";

    private static final String FEEDER_MASTER_DTRS = "FEEDER_MASTER_DTRS";
    String CREATE_FEEDER_MASTER_DTRS = "CREATE TABLE " + FEEDER_MASTER_DTRS + "("
            + SECTION_NAME + " TEXT ,"
            + DTR_STRUCTURE_CODE + " TEXT ,"
            + CAPACITY + " TEXT ,"
            + PHASE + " TEXT ,"
            + DTR_LOCATION + " TEXT ,"
            + DTR_MAKE + " TEXT ,"
            + DTR_SLNO + " TEXT ,"
            + SECTION_ID + " TEXT ,"
            + FEEDER_ID + " TEXT ,"
            + FEEDER_NAME + " TEXT ,"
            + IS_MAPPED + " TEXT ,"
            + NO_OF_LT_CKTS + " TEXT ,"
            + MAPPED_LOCATION_NO + " TEXT ,"
            + GLAT + " TEXT ,"
            + GLONG + " TEXT ,"

            + FLAG + " TEXT " + ")";


    private static final String STATUS_TABLE = "STATUS_TABLE";
    String CREATE_STATUS_TABLE = "CREATE TABLE " + STATUS_TABLE + "("
            + EST_NO + " TEXT PRIMARY KEY,"
            + IS_SURVEY_COMPLETED + " TEXT ,"
            + IS_33KV_SURVEY_COMPLETED + " TEXT ,"
            + IS_11KV_SURVEY_COMPLETED + " TEXT ,"
            + IS_LT_SURVEY_COMPLETED + " TEXT ,"
            + IS_11KV_SELECTED + " TEXT ,"
            + IS_33KV_SELECTED + " TEXT ,"
            + IS_LT_SELECTED + " TEXT ,"
            + IS_METERS_SURVEY_COMPLETED + " TEXT ,"

            + IS_11KV_FEEDER_1_SURVEY_COMPLETED + " TEXT ,"
            + IS_11KV_FEEDER_2_SURVEY_COMPLETED + " TEXT ,"
            + IS_11KV_FEEDER_3_SURVEY_COMPLETED + " TEXT ,"
            + IS_11KV_FEEDER_4_SURVEY_COMPLETED + " TEXT ,"
            + IS_11KV_FEEDER_5_SURVEY_COMPLETED + " TEXT ,"
            + IS_11KV_FEEDER_6_SURVEY_COMPLETED + " TEXT ,"
            + IS_11KV_FEEDER_7_SURVEY_COMPLETED + " TEXT ,"
            + IS_11KV_FEEDER_8_SURVEY_COMPLETED + " TEXT ,"
            + IS_11KV_FEEDER_9_SURVEY_COMPLETED + " TEXT ,"
            + IS_11KV_FEEDER_10_SURVEY_COMPLETED + " TEXT ,"

            + IS_11KV_FEEDER_1_SELECTED + " TEXT ,"
            + IS_11KV_FEEDER_2_SELECTED + " TEXT ,"
            + IS_11KV_FEEDER_3_SELECTED + " TEXT ,"
            + IS_11KV_FEEDER_4_SELECTED + " TEXT ,"
            + IS_11KV_FEEDER_5_SELECTED + " TEXT ,"
            + IS_11KV_FEEDER_6_SELECTED + " TEXT ,"
            + IS_11KV_FEEDER_7_SELECTED + " TEXT ,"
            + IS_11KV_FEEDER_8_SELECTED + " TEXT ,"
            + IS_11KV_FEEDER_9_SELECTED + " TEXT ,"
            + IS_11KV_FEEDER_10_SELECTED + " TEXT ,"

            + IS_33KV_FEEDER_1_SURVEY_COMPLETED + " TEXT ,"
            + IS_33KV_FEEDER_2_SURVEY_COMPLETED + " TEXT ,"
            + IS_33KV_FEEDER_3_SURVEY_COMPLETED + " TEXT ,"
            + IS_33KV_FEEDER_4_SURVEY_COMPLETED + " TEXT ,"
            + IS_33KV_FEEDER_5_SURVEY_COMPLETED + " TEXT ,"

            + IS_33KV_FEEDER_1_SELECTED + " TEXT ,"
            + IS_33KV_FEEDER_2_SELECTED + " TEXT ,"
            + IS_33KV_FEEDER_3_SELECTED + " TEXT ,"
            + IS_33KV_FEEDER_4_SELECTED + " TEXT ,"
            + IS_33KV_FEEDER_5_SELECTED + " TEXT ,"

            + GIS_ID + " TEXT ,"

            + FLAG + " TEXT " + ")";


//    private static final String STATUS_TABLE = "STATUS_TABLE";
//    String CREATE_STATUS_TABLE = "CREATE TABLE " + STATUS_TABLE + "("
//            + EST_NO + " TEXT PRIMARY KEY,"
//            + IS_SURVEY_COMPLETED + " TEXT ,"
//            + IS_33KV_SURVEY_COMPLETED + " TEXT ,"
//            + IS_11KV_SURVEY_COMPLETED + " TEXT ,"
//            + IS_LT_SURVEY_COMPLETED + " TEXT ,"
//            + IS_11KV_SELECTED + " TEXT ,"
//            + IS_33KV_SELECTED + " TEXT ,"
//            + IS_LT_SELECTED + " TEXT ,"
//            + IS_METERS_SURVEY_COMPLETED + " TEXT ,"
//
//            + IS_11KV_FEEDER_1_SURVEY_COMPLETED + " TEXT ,"
//            + IS_11KV_FEEDER_2_SURVEY_COMPLETED + " TEXT ,"
//            + IS_11KV_FEEDER_3_SURVEY_COMPLETED + " TEXT ,"
//            + IS_11KV_FEEDER_4_SURVEY_COMPLETED + " TEXT ,"
//            + IS_11KV_FEEDER_5_SURVEY_COMPLETED + " TEXT ,"
//
//            + IS_11KV_FEEDER_1_SELECTED + " TEXT ,"
//            + IS_11KV_FEEDER_2_SELECTED + " TEXT ,"
//            + IS_11KV_FEEDER_3_SELECTED + " TEXT ,"
//            + IS_11KV_FEEDER_4_SELECTED + " TEXT ,"
//            + IS_11KV_FEEDER_5_SELECTED + " TEXT ,"
//
//            + IS_33KV_FEEDER_1_SURVEY_COMPLETED + " TEXT ,"
//            + IS_33KV_FEEDER_2_SURVEY_COMPLETED + " TEXT ,"
//            + IS_33KV_FEEDER_3_SURVEY_COMPLETED + " TEXT ,"
//            + IS_33KV_FEEDER_4_SURVEY_COMPLETED + " TEXT ,"
//            + IS_33KV_FEEDER_5_SURVEY_COMPLETED + " TEXT ,"
//
//            + IS_33KV_FEEDER_1_SELECTED + " TEXT ,"
//            + IS_33KV_FEEDER_2_SELECTED + " TEXT ,"
//            + IS_33KV_FEEDER_3_SELECTED + " TEXT ,"
//            + IS_33KV_FEEDER_4_SELECTED + " TEXT ,"
//            + IS_33KV_FEEDER_5_SELECTED + " TEXT ,"
//
//            + GIS_ID + " TEXT ,"
//
//            + FLAG + " TEXT " + ")";

    //MATERIALS TABLE
    private static final String MATERIAL_TABLE = "MATERIAL_TABLE";
    private static final String MT_ESTIMATE_NO = "MT_ESTIMATE_NO";
    private static final String MT_VOLTAGE_LEVEL = "MT_VOLTAGE_LEVEL";
    private static final String MT_LOCATION_ID = "MT_LOCATION_ID";
    private static final String MT_LOCATION_NO = "MT_LOCATION_NO";
    private static final String MT_MATERIAL = "MT_MATERIAL";
    private static final String MT_SAP_CODE = "MT_SAP_CODE";
    private static final String MT_MATERIAL_DESC = "MT_MATERIAL_DESC";
    private static final String MT_COUNT = "MT_COUNT";
    private static final String MT_SURVEY_CODE = "MT_SURVEY_CODE";
    private static final String MT_DATA_ID = "MT_DATA_ID";
    private static final String MT_TYPE = "MT_TYPE";
    private static final String MOD_TYPE = "MOD_TYPE";


    String CREATE_MATERIAL_TABLE = "CREATE TABLE " + MATERIAL_TABLE + "("
            + MT_ESTIMATE_NO + " TEXT ,"
            + MT_VOLTAGE_LEVEL + " TEXT ,"
            + MT_LOCATION_ID + " TEXT ,"
            + MT_LOCATION_NO + " TEXT ,"
            + MT_MATERIAL + " TEXT ,"
            + MT_SAP_CODE + " TEXT ,"
            + MT_MATERIAL_DESC + " TEXT ,"
            + MT_COUNT + " TEXT ,"
            + MT_SURVEY_CODE + " TEXT ,"
            + MT_DATA_ID + " TEXT ,"
            + MT_TYPE + " TEXT ,"
            + GIS_ID + " TEXT ,"
            + APP_VERSION + " TEXT ,"
            + APP_VERSION2 + " TEXT ,"
            + MOD_TYPE + " TEXT ,"
            + REF_ID + " TEXT ,"
            + UOM + " TEXT ,"


            + FLAG + " TEXT " + ")";


    public ArcGISDatabaseEstimates(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_WORK_TABLE);
        db.execSQL(CREATE_SURVEY_TABLE_33KV);
        db.execSQL(CREATE_SURVEY_TABLE_11KV);
        db.execSQL(CREATE_SURVEY_TABLE_LT);
        db.execSQL(CREATE_DTR_TABLE);
        db.execSQL(CREATE_LT_CKT_TABLE);
        db.execSQL(CREATE_SURVEY_TABLE_NEW);
        db.execSQL(CREATE_STATUS_TABLE);
        db.execSQL(CREATE_POLE_TYPES_TABLE);
        db.execSQL(CREATE_CONDUCTOR_TYPE_TABLE);
        db.execSQL(CREATE_DTR_TYPE_TABLE);
        db.execSQL(CREATE_MATERIAL_MASTER_TABLE);
        db.execSQL(CREATE_EST_FEEDERS);
        db.execSQL(CREATE_MATERIAL_TABLE);
        db.execSQL(CREATE_FEEDER_MASTER_DTRS);
        db.execSQL(CREATE_EXISTING_POLES_TABLE_33KV);
        db.execSQL(CREATE_EXISTING_POLES_TABLE_11KV);
        db.execSQL(CREATE_EXISTING_POLES_TABLE_NEW);
        db.execSQL(CREATE_EDIT_TABLE);
        db.execSQL(CREATE_POLE_HEIGHT_DETAILS_TABLE);
        db.execSQL(CREATE_LT_LINE_CONFIG_TABLE);
        db.execSQL(CREATE_DTR_MAKE_TABLE);
        db.execSQL(CREATE_DTR_HT_TABLE);
        //db.execSQL(CREATE_SURVEY_TABLE_CABLE_OPERATOR);
        db.execSQL(CREATE_DTR_PHASE_CAPACITIES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + WORK_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SURVEY_TABLE_33KV);
        db.execSQL("DROP TABLE IF EXISTS " + SURVEY_TABLE_11KV);
        db.execSQL("DROP TABLE IF EXISTS " + SURVEY_TABLE_LT);
        db.execSQL("DROP TABLE IF EXISTS " + DTR_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + LT_CKT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SURVEY_TABLE_NEW);
        db.execSQL("DROP TABLE IF EXISTS " + STATUS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + POLE_TYPES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CONDUCTOR_TYPE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DTR_TYPE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MATERIAL_MASTER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + EST_FEEDERS);
        db.execSQL("DROP TABLE IF EXISTS " + MATERIAL_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + FEEDER_MASTER_DTRS);
        db.execSQL("DROP TABLE IF EXISTS " + EXISTING_POLES_TABLE_33KV);
        db.execSQL("DROP TABLE IF EXISTS " + EXISTING_POLES_TABLE_11KV);
        db.execSQL("DROP TABLE IF EXISTS " + EXISTING_POLES_TABLE_NEW);
        db.execSQL("DROP TABLE IF EXISTS " + EDIT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + POLE_HEIGHT_DETAILS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + LT_LINE_CONFIG_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DTR_MAKE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DTR_HT_TABLE);
        //db.execSQL("DROP TABLE IF EXISTS " + SURVEY_TABLE_CABLE_OPERATOR);
        db.execSQL("DROP TABLE IF EXISTS " + DTR_PHASE_CAPACITIES_TABLE);

        onCreate(db);
    }


    public boolean addPoleHeightData(PoleHeightModel data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MATERIALPART1, data.getMATERIALPART1());
        values.put(MATERIALCODE, data.getMATERIALCODE());
        values.put(GROUPDESC1, data.getGROUPDESC1());
        values.put(MATERIAL_TYPE, data.getMATERIAL_TYPE());
        values.put(GROUPCODE, data.getGROUPCODE());
        values.put(UOM, data.getUOM());
        values.put(POLE_TYPE, data.getPOLE_TYPE());
        values.put(POLE_HEIGHT, data.getPOLE_HEIGHT());
        values.put(FLAG, "N");

        long a = db.insert(POLE_TYPES_TABLE, null, values);
        db.close();
        if (a == -1) {
            return false;
        } else {
            return true;
        }

    }


    public boolean addConductorTypeData(ConductorTypeModel data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MATERIALPART1, data.getMATERIALPART1());
        values.put(MATERIALCODE, data.getMATERIALCODE());
        values.put(GROUPDESC1, data.getGROUPDESC1());
        values.put(MATERIAL_TYPE, data.getMATERIAL_TYPE());
        values.put(GROUPCODE, data.getGROUPCODE());
        values.put(UOM, data.getUOM());
        values.put(CABLE_TYPE, data.getCABLE_TYPE());
        values.put(CORE, data.getCORE());
        values.put(CABLE_SIZE, data.getCABLE_SIZE());
        values.put(VOLTAGE_LEVEL, data.getVOLTAGE_LEVEL());
        values.put(HT_11KV, data.getHT_11KV());
        values.put(HT_33KV, data.getHT_33KV());
        values.put(LT, data.getLT());
        values.put(FLAG, "N");

        long a = db.insert(CONDUCTOR_TYPE_TABLE, null, values);
        db.close();
        if (a == -1) {
            return false;
        } else {
            return true;
        }

    }


    private static final String MATERAIL_DESC = "MATERAIL_DESC";


    public List<String> getCTPTCapacities(String groupCode) {
        List<String> capacities = new ArrayList<>();
        String query = "SELECT CAPACITY FROM DTR_HT_TABLE WHERE GROUPCODE = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, new String[]{groupCode});

            System.out.println("QUERY FOR DTR_HT: " + query + " [GROUPCODE=" + groupCode + "]");

            if (cursor.moveToFirst()) {
                do {
                    capacities.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            db.close();
        }
        return capacities;
    }

    public List<String> getDTRPhase() {
        List<String> capacities = new ArrayList<>();
        String query = "SELECT PHASE FROM DTR_PHASE_CAPACITIES_TABLE";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, new String[]{});
            if (cursor.moveToFirst()) {
                do {
                    capacities.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            db.close();
        }
        return capacities;
    }

    public List<String> getDTRCapacities(String phase) {
        List<String> capacities = new ArrayList<>();

        String query = "SELECT CAPACITY " +
                "FROM DTR_PHASE_CAPACITIES_TABLE " +
                "WHERE PHASE = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, new String[]{phase});

            if (cursor.moveToFirst()) {
                do {
                    capacities.add(cursor.getString(
                            cursor.getColumnIndexOrThrow(CAPACITY)
                    ));
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            db.close();
        }

        return capacities;
    }


    public List<DTR_HT_MaterialModel> getDTR_HT(String groupCode, String capacity) {
        List<DTR_HT_MaterialModel> data = new ArrayList<>();
        String query = "SELECT * FROM DTR_HT_TABLE WHERE GROUPCODE = ? AND CAPACITY = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, new String[]{groupCode, capacity});

            System.out.println("QUARY FOR DTR_HT" + query + " " + groupCode + " " + capacity);

            if (cursor.moveToFirst()) {
                do {
                    DTR_HT_MaterialModel model = new DTR_HT_MaterialModel();
                    model.setMATERIALCODE(cursor.getString(0));
                    model.setMATERAIL_DESC(cursor.getString(1));
                    model.setGROUPCODE(cursor.getString(2));
                    model.setPHASE(cursor.getString(3));
                    model.setCAPACITY(cursor.getString(4));
                    model.setMATERIAL_TYPE(cursor.getString(5));
                    model.setGROUPDESC1(cursor.getString(6));
                    model.setUOM(cursor.getString(7));
                    model.setSURVEY_CODE(cursor.getString(8));

                    data.add(model);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            db.close();
        }
        return data;
    }


    public boolean addDTR_HTMaterialData(DTR_HT_MaterialModel data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MATERIALCODE, data.getMATERIALCODE());
        values.put(MATERAIL_DESC, data.getMATERAIL_DESC());
        values.put(GROUPCODE, data.getGROUPCODE());
        values.put(PHASE, data.getPHASE());
        values.put(CAPACITY, data.getCAPACITY());
        values.put(MATERIAL_TYPE, data.getMATERIAL_TYPE());
        values.put(GROUPDESC1, data.getGROUPDESC1());
        values.put(UOM, data.getUOM());
        values.put(SURVEY_CODE, data.getSURVEY_CODE());
        values.put(FLAG, "N");

        long a = db.insert(DTR_HT_TABLE, null, values);
        db.close();
        if (a == -1) {
            return false;
        } else {
            return true;
        }

    }


    public boolean addDtrPhaseCapacities(DTRCapacities data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PHASE, data.getPhase());
        values.put(CAPACITY, data.getCapacity());

        values.put(FLAG, "N");

        long a = db.insert(DTR_PHASE_CAPACITIES_TABLE, null, values);
        db.close();
        if (a == -1) {
            return false;
        } else {
            return true;
        }

    }


    public boolean clearDTR_HT() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            db.delete(DTR_HT_TABLE, null, null);
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public boolean clearDTR_Capacities() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            db.delete(DTR_PHASE_CAPACITIES_TABLE, null, null);
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public boolean addDTRTypeData(DTRTypesModel data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MATERIALPART1, data.getMATERIALPART1());
        values.put(MATERIALCODE, data.getMATERIALCODE());
        values.put(GROUPDESC1, data.getGROUPDESC1());
        values.put(MATERIAL_TYPE, data.getMATERIAL_TYPE());
        values.put(GROUPCODE, data.getGROUPCODE());
        values.put(UOM, data.getUOM());
        values.put(CAPACITY, data.getCAPACITY());
        values.put(PHASE, data.getPHASE());

        values.put(FLAG, "N");

        long a = db.insert(DTR_TYPE_TABLE, null, values);
        db.close();
        if (a == -1) {
            return false;
        } else {
            return true;
        }

    }

    public boolean addMaterialMaster(MaterialMasterModel data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MATERIALPART1, data.getMATERIALPART1());
        values.put(MATERIALCODE, data.getMATERIALCODE());
        values.put(GROUPDESC1, data.getGROUPDESC1());
        values.put(MATERIAL_TYPE, data.getMATERIAL_TYPE());
        values.put(GROUPCODE, data.getGROUPCODE());
        values.put(UOM, data.getUOM());
        values.put(VOLTAGE_LEVEL, data.getVOLTAGE_LEVEL());
        values.put(HT_11KV, data.getHT_11KV());
        values.put(HT_33KV, data.getHT_33KV());
        values.put(LT, data.getLT());
        values.put(MATERIAL, data.getMATERIAL());
        values.put(POLE_TYPE, data.getPOLE_TYPE());
        values.put(POLE_HEIGHT, data.getPOLE_HEIGHT());
        values.put(MT_FLAG, data.getMT_FLAG());


        values.put(FLAG, "N");

        long a = db.insert(MATERIAL_MASTER_TABLE, null, values);
        db.close();
        if (a == -1) {
            return false;
        } else {
            return true;
        }

    }

    public boolean clearPoleHeightTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            db.delete(POLE_TYPES_TABLE, null, null);
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public boolean clearConductorTypeTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            db.delete(CONDUCTOR_TYPE_TABLE, null, null);
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public boolean clearEstimatesTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            db.delete(SURVEY_TABLE_NEW, null, null);
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public boolean clearDTRTypeTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            db.delete(DTR_TYPE_TABLE, null, null);
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public boolean clearMaterialMasterTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            db.delete(MATERIAL_MASTER_TABLE, null, null);
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public ArrayList<NewModel> getExistingData(String survey_type, boolean type) {
        ArrayList<NewModel> data = new ArrayList<>();
        String query = "";

        if (type) {
            query = "SELECT * FROM EXISTING_POLES_TABLE_NEW";
        } else {
            query = "SELECT * FROM EXISTING_POLES_TABLE_NEW WHERE VOLTAGE_LEVEL_NEW = '" + survey_type + "'";
        }


        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    NewModel model = new NewModel();
                    model.setFEEDER_ID(cursor.getString(0));
                    model.setVOLTAGE_LEVEL(cursor.getString(1));
                    model.setPOLE_NO(cursor.getString(2));
                    model.setFROM_POLE(cursor.getString(3));
                    model.setLINE_TYPE(cursor.getString(4));
                    model.setPOLE_CONFIG(cursor.getString(5));
                    model.setPOLE_TYPE(cursor.getString(6));
                    model.setPOLE_HEIGHT(cursor.getString(7));
                    model.setPOLE_SUPPORT(cursor.getString(8));
                    model.setPIN_CUT(cursor.getString(9));
                    model.setLATITUDE(cursor.getString(10));
                    model.setLONGITUDE(cursor.getString(11));
                    model.setIS_EXISTING_LOCATION_NEW(cursor.getString(12));
                    model.setEST_NO(cursor.getString(13));


                    data.add(model);
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return data;
    }

    public boolean addExistingData(NewModel data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FEEDER_ID_NEW, data.getFEEDER_ID());
        values.put(VOLTAGE_LEVEL_NEW, data.getVOLTAGE_LEVEL());
        values.put(POLE_NO_NEW, data.getPOLE_NO());
        values.put(LINE_TYPE_NEW, data.getLINE_TYPE());
        values.put(FROM_POLE_NEW, data.getFROM_POLE());
        values.put(POLE_CONFIG_NEW, data.getPOLE_CONFIG());
        values.put(POLE_TYPE_NEW, data.getPOLE_TYPE());
        values.put(POLE_HEIGHT_NEW, data.getPOLE_HEIGHT());
        values.put(POLE_SUPPORT_NEW, data.getPOLE_SUPPORT());
        values.put(PIN_CUT_NEW, data.getPIN_CUT());
        values.put(LAT_NEW, data.getLATITUDE());
        values.put(LON_NEW, data.getLONGITUDE());
        values.put(IS_EXISTING_LOCATION_NEW, data.getIS_EXISTING_LOCATION_NEW());
        values.put(EST_NO_NEW, data.getEST_NO());


        values.put(FLAG_NEW, "N");

       /* long a = db.insert(EXISTING_POLES_TABLE_NEW, null, values);

        db.close();
        if (a == -1) {
            return false;
        } else {
            return true;
        }*/
        return true;
    }

    public List<PoleHeightModel> getPoleHeightData(String height, String poleType) {
        ArrayList<PoleHeightModel> data = new ArrayList<>();
        String query = "SELECT * FROM POLE_TYPES_TABLE WHERE POLE_HEIGHT = ? AND POLE_TYPE = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, new String[]{height, poleType});
            if (cursor.moveToFirst()) {
                do {
                    PoleHeightModel model = new PoleHeightModel();
                    model.setMATERIALPART1(cursor.getString(0));
                    model.setMATERIALCODE(cursor.getString(1));
                    model.setGROUPDESC1(cursor.getString(2));
                    model.setMATERIAL_TYPE(cursor.getString(3));
                    model.setGROUPCODE(cursor.getString(4));
                    model.setUOM(cursor.getString(5));
                    model.setPOLE_TYPE(cursor.getString(6));
                    model.setPOLE_HEIGHT(cursor.getString(7));
                    data.add(model);
                } while (cursor.moveToNext());
            }
            cursor.close();  // Close cursor after use
        } catch (Exception e) {
            // Handle the exception
        } finally {
            db.close();  // Ensure database is closed in all cases
        }
        return data;
    }

    public List<PoleHeightModel> getPoleHeightData(String height) {
        ArrayList<PoleHeightModel> data = new ArrayList<>();
        String query = "SELECT * FROM POLE_TYPES_TABLE WHERE POLE_HEIGHT = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, new String[]{height});
            if (cursor.moveToFirst()) {
                do {
                    PoleHeightModel model = new PoleHeightModel();
                    model.setMATERIALPART1(cursor.getString(0));
                    model.setMATERIALCODE(cursor.getString(1));
                    model.setGROUPDESC1(cursor.getString(2));
                    model.setMATERIAL_TYPE(cursor.getString(3));
                    model.setGROUPCODE(cursor.getString(4));
                    model.setUOM(cursor.getString(5));
                    model.setPOLE_TYPE(cursor.getString(6));
                    model.setPOLE_HEIGHT(cursor.getString(7));
                    data.add(model);
                } while (cursor.moveToNext());
            }
            cursor.close();  // Close cursor after use
        } catch (Exception e) {
            // Handle the exception
        } finally {
            db.close();  // Ensure database is closed in all cases
        }
        return data;
    }

    public List<ConductorTypeModel> getConductorTypeData(String conductorType) {
        ArrayList<ConductorTypeModel> data = new ArrayList<>();
        String query = "SELECT * FROM CONDUCTOR_TYPE_TABLE WHERE CABLE_TYPE = '" + conductorType + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    ConductorTypeModel model = new ConductorTypeModel();
                    model.setMATERIALPART1(cursor.getString(0));
                    model.setMATERIALCODE(cursor.getString(1));
                    model.setGROUPDESC1(cursor.getString(2));
                    model.setMATERIAL_TYPE(cursor.getString(3));
                    model.setGROUPCODE(cursor.getString(4));
                    model.setUOM(cursor.getString(5));
                    model.setCABLE_TYPE(cursor.getString(6));
                    model.setCORE(cursor.getString(7));
                    model.setCABLE_SIZE(cursor.getString(8));
                    model.setVOLTAGE_LEVEL(cursor.getString(9));
                    model.setHT_11KV(cursor.getString(10));
                    model.setHT_33KV(cursor.getString(11));
                    model.setLT(cursor.getString(12));
                    data.add(model);
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return data;
    }


    public List<DTRTypesModel> getDTRTypeData(String phase, String capacity) {
        ArrayList<DTRTypesModel> data = new ArrayList<>();
        String query = "SELECT * FROM DTR_TYPE_TABLE WHERE PHASE = ? AND CAPACITY = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, new String[]{phase, capacity});
            if (cursor.moveToFirst()) {
                do {
                    DTRTypesModel model = new DTRTypesModel();
                    model.setMATERIALPART1(cursor.getString(0));
                    model.setMATERIALCODE(cursor.getString(1));
                    model.setGROUPDESC1(cursor.getString(2));
                    model.setMATERIAL_TYPE(cursor.getString(3));
                    model.setGROUPCODE(cursor.getString(4));
                    model.setUOM(cursor.getString(5));
                    model.setCAPACITY(cursor.getString(6));
                    model.setPHASE(cursor.getString(7));
                    data.add(model);
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return data;
    }

    public List<MaterialMasterModel> getMaterialMasterDataWithAbSwitch() {
        ArrayList<MaterialMasterModel> data = new ArrayList<>();
        String query = "SELECT * FROM MATRRIAL_MASTER_TABLE WHERE MATERIALPART1 LIKE '%AB SWITCH%' AND GROUPDESC1 = 'AB Switches'";
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            // Log.d("AB SWITCH_TYPE_DATA", "Executing query: " + query);
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    MaterialMasterModel model = new MaterialMasterModel();
                    model.setMATERIALPART1(cursor.getString(0));
                    model.setMATERIALCODE(cursor.getString(1));
                    model.setGROUPDESC1(cursor.getString(2));
                    model.setMATERIAL_TYPE(cursor.getString(3));
                    model.setGROUPCODE(cursor.getString(4));
                    model.setUOM(cursor.getString(5));
                    data.add(model);

                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String json = gson.toJson(model);
                    // Log.d("AB SWITCH_TYPE_DATA", "Fetched Data: " + json);

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            // Log.e("AB SWITCH_TYPE_DATA", "Error fetching data: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close();
        }

        return data;
    }

    public List<MaterialMasterModel> getMaterialMasterDataForFabricationMaterial() {
        ArrayList<MaterialMasterModel> data = new ArrayList<>();
        String query = "SELECT * FROM MATRRIAL_MASTER_TABLE WHERE groupdesc1 = 'FABRICATION MATERIAL' AND MATERIAL IS NOT NULL";

        //String query = "SELECT * FROM MATRRIAL_MASTER_TABLE WHERE GROUPDESC1 = 'FABRICATION MATERIAL'";
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            // Log.d("FABRICATION_MATERIAL", "Executing query: " + query);
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    MaterialMasterModel model = new MaterialMasterModel();
                    model.setMATERIALPART1(cursor.getString(0));
                    model.setMATERIALCODE(cursor.getString(1));
                    model.setGROUPDESC1(cursor.getString(2));
                    model.setMATERIAL_TYPE(cursor.getString(3));
                    model.setGROUPCODE(cursor.getString(4));
                    model.setUOM(cursor.getString(5));
                    model.setVOLTAGE_LEVEL(cursor.getString(6));
                    model.setHT_11KV(cursor.getString(7));
                    model.setHT_33KV(cursor.getString(8));
                    model.setLT(cursor.getString(9));
                    model.setMATERIAL(cursor.getString(10));
//                    model.setPOLE_HEIGHT(cursor.getString(11));
//                    model.setPOLE_TYPE(cursor.getString(12));
                    data.add(model);

                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String json = gson.toJson(model);
                    //  Log.d("FABRICATION_MATERIAL", "Fetched Data: " + json);

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            // Log.e("FABRICATION_MATERIAL", "Error fetching data: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close();
        }

        return data;
    }


    public List<MaterialMasterModel> getDiscInsulators() {
        ArrayList<MaterialMasterModel> data = new ArrayList<>();
        String query = "SELECT * FROM MATRRIAL_MASTER_TABLE WHERE MATERIAL = 'DISC INSULATORS'";

        //String query = "SELECT * FROM MATRRIAL_MASTER_TABLE WHERE GROUPDESC1 = 'FABRICATION MATERIAL'";
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            // Log.d("FABRICATION_MATERIAL", "Executing query: " + query);
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    MaterialMasterModel model = new MaterialMasterModel();
                    model.setMATERIALPART1(cursor.getString(0));
                    model.setMATERIALCODE(cursor.getString(1));
                    model.setGROUPDESC1(cursor.getString(2));
                    model.setMATERIAL_TYPE(cursor.getString(3));
                    model.setGROUPCODE(cursor.getString(4));
                    model.setUOM(cursor.getString(5));
                    model.setVOLTAGE_LEVEL(cursor.getString(6));
                    model.setHT_11KV(cursor.getString(7));
                    model.setHT_33KV(cursor.getString(8));
                    model.setLT(cursor.getString(9));
                    model.setMATERIAL(cursor.getString(10));
//                    model.setPOLE_HEIGHT(cursor.getString(11));
//                    model.setPOLE_TYPE(cursor.getString(12));
                    data.add(model);

                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String json = gson.toJson(model);
                    //Log.d("FABRICATION_MATERIAL", "Fetched Data: " + json);

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            //Log.e("FABRICATION_MATERIAL", "Error fetching data: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close();
        }

        return data;
    }


    public List<MaterialMasterModel> getShackles() {
        ArrayList<MaterialMasterModel> data = new ArrayList<>();
        String query = "SELECT * FROM MATRRIAL_MASTER_TABLE WHERE MATERIAL LIKE '%SHAC%'";

        //String query = "SELECT * FROM MATRRIAL_MASTER_TABLE WHERE GROUPDESC1 = 'FABRICATION MATERIAL'";
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            //Log.d("SHACKLES_FOR_LT", "Executing query: " + query);
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    MaterialMasterModel model = new MaterialMasterModel();
                    model.setMATERIALPART1(cursor.getString(0));
                    model.setMATERIALCODE(cursor.getString(1));
                    model.setGROUPDESC1(cursor.getString(2));
                    model.setMATERIAL_TYPE(cursor.getString(3));
                    model.setGROUPCODE(cursor.getString(4));
                    model.setUOM(cursor.getString(5));
                    model.setVOLTAGE_LEVEL(cursor.getString(6));
                    model.setHT_11KV(cursor.getString(7));
                    model.setHT_33KV(cursor.getString(8));
                    model.setLT(cursor.getString(9));
                    model.setMATERIAL(cursor.getString(10));
//                    model.setPOLE_HEIGHT(cursor.getString(11));
//                    model.setPOLE_TYPE(cursor.getString(12));
                    data.add(model);

                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String json = gson.toJson(model);
                    // Log.d("SHACKLES_FOR_LT", "Fetched Data: " + json);

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            // Log.e("SHACKLES_FOR_LT", "Error fetching data: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close();
        }

        return data;
    }

    public List<MaterialMasterModel> getMetalParts() {
        ArrayList<MaterialMasterModel> data = new ArrayList<>();
        String query = "SELECT * FROM MATRRIAL_MASTER_TABLE WHERE MATERIAL LIKE '%METAL%'";

        //String query = "SELECT * FROM MATRRIAL_MASTER_TABLE WHERE GROUPDESC1 = 'FABRICATION MATERIAL'";
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            // Log.d("FABRICATION_MATERIAL", "Executing query: " + query);
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    MaterialMasterModel model = new MaterialMasterModel();
                    model.setMATERIALPART1(cursor.getString(0));
                    model.setMATERIALCODE(cursor.getString(1));
                    model.setGROUPDESC1(cursor.getString(2));
                    model.setMATERIAL_TYPE(cursor.getString(3));
                    model.setGROUPCODE(cursor.getString(4));
                    model.setUOM(cursor.getString(5));
                    model.setVOLTAGE_LEVEL(cursor.getString(6));
                    model.setHT_11KV(cursor.getString(7));
                    model.setHT_33KV(cursor.getString(8));
                    model.setLT(cursor.getString(9));
                    model.setMATERIAL(cursor.getString(10));
//                    model.setPOLE_HEIGHT(cursor.getString(11));
//                    model.setPOLE_TYPE(cursor.getString(12));
                    data.add(model);

                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String json = gson.toJson(model);
                    // Log.d("FABRICATION_MATERIAL", "Fetched Data: " + json);

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            // Log.e("FABRICATION_MATERIAL", "Error fetching data: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close();
        }

        return data;
    }

    public List<MaterialMasterModel> getClamps() {
        ArrayList<MaterialMasterModel> data = new ArrayList<>();
        String query = "SELECT * FROM MATRRIAL_MASTER_TABLE WHERE MATERIAL LIKE '%BACK CLAMP%'";

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            //  Log.d("CLAMPS---->", "Executing query: " + query);
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    MaterialMasterModel model = new MaterialMasterModel();
                    model.setMATERIALPART1(cursor.getString(0));
                    model.setMATERIALCODE(cursor.getString(1));
                    model.setGROUPDESC1(cursor.getString(2));
                    model.setMATERIAL_TYPE(cursor.getString(3));
                    model.setGROUPCODE(cursor.getString(4));
                    model.setUOM(cursor.getString(5));
                    model.setVOLTAGE_LEVEL(cursor.getString(6));
                    model.setHT_11KV(cursor.getString(7));
                    model.setHT_33KV(cursor.getString(8));
                    model.setLT(cursor.getString(9));
                    model.setMATERIAL(cursor.getString(10));
                    model.setPOLE_TYPE(cursor.getString(11));
                    model.setPOLE_HEIGHT(cursor.getString(12));
                    data.add(model);

                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String json = gson.toJson(model);
                    //  Log.d("CLAMPS---->", "Fetched Data: " + json);

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            // Log.e("CLAMPS---->", "Error fetching data: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close();
        }

        return data;
    }

    public boolean addFeederMasterDtrs(DTRFeederMaster data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SECTION_NAME, data.getSECTION_NAME());
        values.put(DTR_STRUCTURE_CODE, data.getDTR_STRUCTURE_CODE());
        values.put(CAPACITY, data.getCAPACITY());
        values.put(PHASE, data.getPHASE());
        values.put(DTR_LOCATION, data.getDTR_LOCATION());
        values.put(DTR_MAKE, data.getDTR_MAKE());
        values.put(DTR_SLNO, data.getDTR_SLNO());
        values.put(SECTION_ID, data.getSECTION_ID());
        values.put(FEEDER_ID, data.getFEEDER_ID());
        values.put(FEEDER_NAME, data.getFEEDER_NAME());
        values.put(IS_MAPPED, data.getIS_MAPPED());
        values.put(NO_OF_LT_CKTS, data.getNO_OF_LT_CKTS());
        values.put(MAPPED_LOCATION_NO, data.getMAPPED_LOCATION_NO());
        values.put(GLAT, data.getGLAT());
        values.put(GLONG, data.getGLONG());
        values.put(FLAG, "N");


        values.put(FLAG, "N");

        long a = db.insert(FEEDER_MASTER_DTRS, null, values);
        db.close();
        if (a == -1) {
            return false;
        } else {
            return true;
        }

    }

    public List<DTRFeederMaster> getPhaseANDCapacityData(String dtrCode) {
        ArrayList<DTRFeederMaster> data = new ArrayList<>();
        String query = "SELECT * FROM FEEDER_MASTER_DTRS WHERE DTR_STRUCTURE_CODE = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            //   Log.d("CLAMPS---->", "Executing query with DTR_STRUCTURE_CODE: " + dtrCode);
            Cursor cursor = db.rawQuery(query, new String[]{dtrCode});

            if (cursor.moveToFirst()) {
                do {
                    DTRFeederMaster model = new DTRFeederMaster();
                    model.setDTR_STRUCTURE_CODE(cursor.getString(1));
                    model.setCAPACITY(cursor.getString(2));
                    model.setPHASE(cursor.getString(3));
                    model.setDTR_LOCATION(cursor.getString(4));
                    model.setDTR_MAKE(cursor.getString(5));
                    model.setDTR_SLNO(cursor.getString(6));
                    model.setFEEDER_ID(cursor.getString(8));
                    model.setFEEDER_NAME(cursor.getString(9));
                    model.setIS_MAPPED(cursor.getString(10));
                    model.setNO_OF_LT_CKTS(cursor.getString(11));
                    model.setMAPPED_LOCATION_NO(cursor.getString(12));
                    model.setGLAT(cursor.getString(13));
                    model.setGLONG(cursor.getString(14));


                    data.add(model);

                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String json = gson.toJson(model);
                    //   Log.d("CLAMPS---->", "Fetched Data: " + json);

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            // Log.e("CLAMPS---->", "Error fetching data: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close();
        }

        return data;
    }


    public List<MaterialMasterModel> getMeters() {
        ArrayList<MaterialMasterModel> data = new ArrayList<>();
        String query = "SELECT * FROM MATRRIAL_MASTER_TABLE  WHERE GROUPDESC1 LIKE  '%METERS%' AND MT_FLAG = 'Y'";

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            //  Log.d("CLAMPS---->", "Executing query: " + query);
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    MaterialMasterModel model = new MaterialMasterModel();
                    model.setMATERIALPART1(cursor.getString(0));
                    model.setMATERIALCODE(cursor.getString(1));
                    model.setGROUPDESC1(cursor.getString(2));
                    model.setMATERIAL_TYPE(cursor.getString(3));
                    model.setGROUPCODE(cursor.getString(4));
                    model.setUOM(cursor.getString(5));
                    model.setVOLTAGE_LEVEL(cursor.getString(6));
                    model.setHT_11KV(cursor.getString(7));
                    model.setHT_33KV(cursor.getString(8));
                    model.setLT(cursor.getString(9));
                    model.setMATERIAL(cursor.getString(10));
                    model.setPOLE_TYPE(cursor.getString(11));
                    model.setPOLE_HEIGHT(cursor.getString(12));
                    model.setMT_FLAG(cursor.getString(13));
                    data.add(model);

                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String json = gson.toJson(model);
                    //   Log.d("CLAMPS---->", "Fetched Data: " + json);

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            // Log.e("CLAMPS---->", "Error fetching data: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close();
        }

        return data;
    }

    public List<MaterialMasterModel> getSpecificClamps(String type) {
        ArrayList<MaterialMasterModel> data = new ArrayList<>();
        String query = "SELECT * FROM MATRRIAL_MASTER_TABLE WHERE MATERIAL LIKE '%BACK CLAMP%'AND POLE_HEIGHT = '" + type + "'";

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            //   Log.d("CLAMPS---->", "Executing query: " + query);
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    MaterialMasterModel model = new MaterialMasterModel();
                    model.setMATERIALPART1(cursor.getString(0));
                    model.setMATERIALCODE(cursor.getString(1));
                    model.setGROUPDESC1(cursor.getString(2));
                    model.setMATERIAL_TYPE(cursor.getString(3));
                    model.setGROUPCODE(cursor.getString(4));
                    model.setUOM(cursor.getString(5));
                    model.setVOLTAGE_LEVEL(cursor.getString(6));
                    model.setHT_11KV(cursor.getString(7));
                    model.setHT_33KV(cursor.getString(8));
                    model.setLT(cursor.getString(9));
                    model.setMATERIAL(cursor.getString(10));
                    model.setPOLE_TYPE(cursor.getString(11));
                    model.setPOLE_HEIGHT(cursor.getString(12));
                    data.add(model);

                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String json = gson.toJson(model);
                    //  Log.d("CLAMPS---->", "Fetched Data: " + json);

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            // Log.e("CLAMPS---->", "Error fetching data: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close();
        }

        return data;
    }

    public List<MaterialMasterModel> getSturtClamps() {
        ArrayList<MaterialMasterModel> data = new ArrayList<>();
        //String query = "SELECT * FROM MATRRIAL_MASTER_TABLE WHERE MATERIAL LIKE '%STAY CLAMP%' AND  POLE_TYPE  IS NOT NULL AND POLE_HEIGHT IS NOT NULL";

        String query = "SELECT * FROM MATRRIAL_MASTER_TABLE WHERE MATERIAL LIKE '%STAY CLAMP%'";


        SQLiteDatabase db = this.getWritableDatabase();

        try {
            //   Log.d("CLAMPS---->", "Executing query: " + query);
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    MaterialMasterModel model = new MaterialMasterModel();
                    model.setMATERIALPART1(cursor.getString(0));
                    model.setMATERIALCODE(cursor.getString(1));
                    model.setGROUPDESC1(cursor.getString(2));
                    model.setMATERIAL_TYPE(cursor.getString(3));
                    model.setGROUPCODE(cursor.getString(4));
                    model.setUOM(cursor.getString(5));
                    model.setVOLTAGE_LEVEL(cursor.getString(6));
                    model.setHT_11KV(cursor.getString(7));
                    model.setHT_33KV(cursor.getString(8));
                    model.setLT(cursor.getString(9));
                    model.setMATERIAL(cursor.getString(10));
                    model.setPOLE_TYPE(cursor.getString(11));
                    model.setPOLE_HEIGHT(cursor.getString(12));
                    data.add(model);

                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String json = gson.toJson(model);
                    //     Log.d("CLAMPS---->", "Fetched Data: " + json);

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            //  Log.e("CLAMPS---->", "Error fetching data: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close();
        }

        return data;
    }

    public List<MaterialMasterModel> getSpecificSturtClamps(String type) {
        ArrayList<MaterialMasterModel> data = new ArrayList<>();
        //String query = "SELECT * FROM MATRRIAL_MASTER_TABLE WHERE MATERIAL LIKE '%STAY CLAMP%' AND  POLE_TYPE  IS NOT NULL AND POLE_HEIGHT IS NOT NULL";

        String query = "SELECT * FROM MATRRIAL_MASTER_TABLE WHERE MATERIAL LIKE '%STAY CLAMP%' AND POLE_HEIGHT = '" + type + "'";


        SQLiteDatabase db = this.getWritableDatabase();

        try {
            //  Log.d("CLAMPS---->", "Executing query: " + query);
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    MaterialMasterModel model = new MaterialMasterModel();
                    model.setMATERIALPART1(cursor.getString(0));
                    model.setMATERIALCODE(cursor.getString(1));
                    model.setGROUPDESC1(cursor.getString(2));
                    model.setMATERIAL_TYPE(cursor.getString(3));
                    model.setGROUPCODE(cursor.getString(4));
                    model.setUOM(cursor.getString(5));
                    model.setVOLTAGE_LEVEL(cursor.getString(6));
                    model.setHT_11KV(cursor.getString(7));
                    model.setHT_33KV(cursor.getString(8));
                    model.setLT(cursor.getString(9));
                    model.setMATERIAL(cursor.getString(10));
                    model.setPOLE_TYPE(cursor.getString(11));
                    model.setPOLE_HEIGHT(cursor.getString(12));
                    data.add(model);

                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String json = gson.toJson(model);
                    //   Log.d("CLAMPS---->", "Fetched Data: " + json);

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            //  Log.e("CLAMPS---->", "Error fetching data: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close();
        }

        return data;
    }


    public List<MaterialMasterModel> getGIPinsForLT() {
        ArrayList<MaterialMasterModel> data = new ArrayList<>();
        //String query = "SELECT * FROM MATRRIAL_MASTER_TABLE WHERE MATERIAL LIKE '%STAY CLAMP%' AND  POLE_TYPE  IS NOT NULL AND POLE_HEIGHT IS NOT NULL";

        String query = "SELECT * FROM MATRRIAL_MASTER_TABLE WHERE MATERIAL LIKE '%GI%'";


        SQLiteDatabase db = this.getWritableDatabase();

        try {
            //    Log.d("CLAMPS---->", "Executing query: " + query);
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    MaterialMasterModel model = new MaterialMasterModel();
                    model.setMATERIALPART1(cursor.getString(0));
                    model.setMATERIALCODE(cursor.getString(1));
                    model.setGROUPDESC1(cursor.getString(2));
                    model.setMATERIAL_TYPE(cursor.getString(3));
                    model.setGROUPCODE(cursor.getString(4));
                    model.setUOM(cursor.getString(5));
                    model.setVOLTAGE_LEVEL(cursor.getString(6));
                    model.setHT_11KV(cursor.getString(7));
                    model.setHT_33KV(cursor.getString(8));
                    model.setLT(cursor.getString(9));
                    model.setMATERIAL(cursor.getString(10));
                    model.setPOLE_TYPE(cursor.getString(11));
                    model.setPOLE_HEIGHT(cursor.getString(12));
                    data.add(model);

                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String json = gson.toJson(model);
                    //   Log.d("CLAMPS---->", "Fetched Data: " + json);

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            // Log.e("CLAMPS---->", "Error fetching data: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close();
        }

        return data;
    }

    public List<MaterialMasterModel> getPinInsulatorsFromMaterialData() {
        ArrayList<MaterialMasterModel> data = new ArrayList<>();
        String query = "SELECT * FROM MATRRIAL_MASTER_TABLE WHERE MATERIAL = 'PIN INSULATORS'";

        //String query = "SELECT * FROM MATRRIAL_MASTER_TABLE WHERE MATERIAL = 'PIN INSULATORS";

        //String query = "SELECT * FROM MATRRIAL_MASTER_TABLE WHERE GROUPDESC1 = 'FABRICATION MATERIAL'";
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            //    Log.d("FABRICATION_MATERIAL", "Executing query: " + query);
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    MaterialMasterModel model = new MaterialMasterModel();
                    model.setMATERIALPART1(cursor.getString(0));
                    model.setMATERIALCODE(cursor.getString(1));
                    model.setGROUPDESC1(cursor.getString(2));
                    model.setMATERIAL_TYPE(cursor.getString(3));
                    model.setGROUPCODE(cursor.getString(4));
                    model.setUOM(cursor.getString(5));
                    model.setVOLTAGE_LEVEL(cursor.getString(6));
                    model.setHT_11KV(cursor.getString(7));
                    model.setHT_33KV(cursor.getString(8));
                    model.setLT(cursor.getString(9));
                    model.setMATERIAL(cursor.getString(10));
//                    model.setPOLE_HEIGHT(cursor.getString(11));
//                    model.setPOLE_TYPE(cursor.getString(12));
                    data.add(model);

                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String json = gson.toJson(model);
                    //     Log.d("FABRICATION_MATERIAL", "Fetched Data: " + json);

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            // Log.e("FABRICATION_MATERIAL", "Error fetching data: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close();
        }

        return data;
    }


    public List<MaterialMasterModel> getMaterialMasterDataWithDistributionBox() {
        ArrayList<MaterialMasterModel> data = new ArrayList<>();
        String query = "SELECT * FROM MATRRIAL_MASTER_TABLE WHERE GROUPDESC1 = 'BOXES' AND MATERIALPART1 LIKE '%DIST%'";

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            //    Log.d("AB SWITCH_TYPE_DATA", "Executing query: " + query);
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    MaterialMasterModel model = new MaterialMasterModel();
                    model.setMATERIALPART1(cursor.getString(0));
                    model.setMATERIALCODE(cursor.getString(1));
                    model.setGROUPDESC1(cursor.getString(2));
                    model.setMATERIAL_TYPE(cursor.getString(3));
                    model.setGROUPCODE(cursor.getString(4));
                    model.setUOM(cursor.getString(5));
                    data.add(model);

                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String json = gson.toJson(model);
                    //   Log.d("AB SWITCH_TYPE_DATA", "Fetched Data: " + json);

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            //  Log.e("AB SWITCH_TYPE_DATA", "Error fetching data: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close();
        }

        return data;
    }

    public boolean addWorkInvolvementData(WorkInvolvementModel data) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + WORK_TABLE + " WHERE " + GIS_ID + " = ? AND " + WORK_TYPE + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{data.getEST_NO(), data.getWORK_TYPE()});

        ContentValues values = new ContentValues();
        values.put(GIS_ID, data.getGIS_ID());
        values.put(WORK_TYPE, data.getWORK_TYPE());

        if (data.getREF_ID() != null) values.put(REF_ID, data.getREF_ID());
        if (data.getSURVEY_TYPE() != null) values.put(SURVEY_TYPE, data.getSURVEY_TYPE());
        if (data.getFEEDER_NAME() != null) values.put(FEEDER_NAME, data.getFEEDER_NAME());
        if (data.getSS_DTR_MTR() != null) values.put(SS_DTR_MTR, data.getSS_DTR_MTR());
        if (data.getTAPPING_LOCATION() != null)
            values.put(TAPPING_LOCATION, data.getTAPPING_LOCATION());
        if (data.getSS_DTR_MTR_QTY() != null) values.put(SS_DTR_MTR_QTY, data.getSS_DTR_MTR_QTY());
        if (data.getIS_SURVEY_COMPLETED() != null)
            values.put(IS_SURVEY_COMPLETED, data.getIS_SURVEY_COMPLETED());
        if (data.getIS_33KV_SURVEY_COMPLETED() != null)
            values.put(IS_33KV_SURVEY_COMPLETED, data.getIS_33KV_SURVEY_COMPLETED());
        if (data.getIS_11KV_SURVEY_COMPLETED() != null)
            values.put(IS_11KV_SURVEY_COMPLETED, data.getIS_11KV_SURVEY_COMPLETED());
        if (data.getIS_LT_SURVEY_COMPLETED() != null)
            values.put(IS_LT_SURVEY_COMPLETED, data.getIS_LT_SURVEY_COMPLETED());
        if (data.getFLAG() != null) values.put(FLAG, data.getFLAG());
        if (data.getIS_11KV_SELECTED() != null)
            values.put(IS_11KV_SELECTED, data.getIS_11KV_SELECTED());
        if (data.getIS_33KV_SELECTED() != null)
            values.put(IS_33KV_SELECTED, data.getIS_33KV_SELECTED());
        if (data.getIS_LT_SELECTED() != null) values.put(IS_LT_SELECTED, data.getIS_LT_SELECTED());
        if (data.getFEEDER_ID() != null) values.put(FEEDER_ID, data.getFEEDER_ID());
        if (data.getSS_ID() != null) values.put(SS_ID, data.getSS_ID());
        if (data.getSS_NAME() != null) values.put(SS_NAME, data.getSS_NAME());
        if (data.getSS_TYPE() != null) values.put(SS_TYPE, data.getSS_TYPE());
        if (data.getCHECKED() != null) values.put(CHECKED, data.getCHECKED());
        if (data.getLT_CKT_CODE() != null) values.put(LT_CKT_CODE, data.getLT_CKT_CODE());

        if (cursor.getCount() > 0) {
            int rowsAffected = db.update(WORK_TABLE, values, GIS_ID + " = ? AND " + WORK_TYPE + " = ?",
                    new String[]{data.getGIS_ID(), data.getWORK_TYPE()});
            cursor.close();
            db.close();
            return rowsAffected > 0;
        } else {
            long result = db.insert(WORK_TABLE, null, values);
            cursor.close();
            db.close();
            return result != -1;
        }
    }


    public boolean addEstFeedersData(WorkInvolvementModel data) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + EST_FEEDERS + " WHERE " + EST_NO + " = ? AND " + WORK_TYPE + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{data.getEST_NO(), data.getWORK_TYPE()});

        ContentValues values = new ContentValues();
        values.put(EST_NO, data.getEST_NO());
        values.put(WORK_TYPE, data.getWORK_TYPE());
        if (data.getEST_NO() != null) values.put(EST_NO, data.getEST_NO());
        if (data.getSURVEY_TYPE() != null) values.put(SURVEY_TYPE, data.getSURVEY_TYPE());
        if (data.getFEEDER_NAME() != null) values.put(FEEDER_NAME, data.getFEEDER_NAME());
        if (data.getFEEDER_ID() != null) values.put(FEEDER_ID, data.getSS_DTR_MTR());
        if (data.getFEEDER_ID() != null) values.put(FEEDER_ID, data.getFEEDER_ID());
        if (data.getSS_ID() != null) values.put(SS_ID, data.getSS_ID());
        values.put(FDR_INDEX, data.getFDR_INDEX());


        if (cursor.getCount() > 0) {
            int rowsAffected = db.update(EST_FEEDERS, values, EST_NO + " = ? AND " + WORK_TYPE + " = ?",
                    new String[]{data.getEST_NO(), data.getWORK_TYPE()});
            cursor.close();
            db.close();
            return rowsAffected > 0;
        } else {
            long result = db.insert(EST_FEEDERS, null, values);
            cursor.close();
            db.close();
            return result != -1;
        }
    }

    public List<WorkInvolvementModel> getWorkInvolvementData(String est_no, String gis_id) {
        ArrayList<WorkInvolvementModel> data = new ArrayList<>();
        String query = "SELECT * FROM WORK_TABLE WHERE GIS_ID = '" + gis_id + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    WorkInvolvementModel model = new WorkInvolvementModel();
                    model.setEST_NO(cursor.getString(0));
                    model.setREF_ID(cursor.getString(1));
                    model.setWORK_TYPE(cursor.getString(2));
                    model.setSURVEY_TYPE(cursor.getString(3));
                    model.setFEEDER_NAME(cursor.getString(4));
                    model.setSS_DTR_MTR(cursor.getString(5));
                    model.setTAPPING_LOCATION(cursor.getString(6));
                    model.setSS_DTR_MTR_QTY(cursor.getString(7));
                    model.setIS_SURVEY_COMPLETED(cursor.getString(8));
                    model.setFLAG(cursor.getString(9));
                    model.setIS_33KV_SURVEY_COMPLETED(cursor.getString(10));
                    model.setIS_11KV_SURVEY_COMPLETED(cursor.getString(11));
                    model.setIS_LT_SURVEY_COMPLETED(cursor.getString(12));
                    model.setIS_11KV_SELECTED(cursor.getString(13));
                    model.setIS_33KV_SELECTED(cursor.getString(14));
                    model.setIS_LT_SELECTED(cursor.getString(15));
                    model.setFEEDER_ID(cursor.getString(16));
                    model.setSS_ID(cursor.getString(19));
                    model.setSS_NAME(cursor.getString(20));
                    model.setSS_TYPE(cursor.getString(21));
                    model.setCHECKED(cursor.getString(22));
                    model.setLT_CKT_CODE(cursor.getString(24));
                    data.add(model);
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return data;
    }


//    public boolean addWorkInvolvementData(WorkInvolvementModel data) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(EST_NO, data.getEST_NO());
//        values.put(REF_ID, data.getREF_ID());
//        values.put(WORK_TYPE, data.getWORK_TYPE());
//        values.put(SURVEY_TYPE, data.getSURVEY_TYPE());
//        values.put(FEEDER_NAME, data.getFEEDER_NAME());
//        values.put(SS_DTR_MTR, data.getSS_DTR_MTR());
//        values.put(TAPPING_LOCATION, data.getTAPPING_LOCATION());
//        values.put(SS_DTR_MTR_QTY, data.getSS_DTR_MTR_QTY());
//        values.put(IS_SURVEY_COMPLETED, data.getIS_SURVEY_COMPLETED());
//        values.put(IS_33KV_SURVEY_COMPLETED, data.getIS_33KV_SURVEY_COMPLETED());
//        values.put(IS_11KV_SURVEY_COMPLETED, data.getIS_11KV_SURVEY_COMPLETED());
//        values.put(IS_LT_SURVEY_COMPLETED, data.getIS_LT_SURVEY_COMPLETED());
//        values.put(FLAG, data.getFLAG());
//        values.put(IS_11KV_SELECTED, data.getIS_11KV_SELECTED());
//        values.put(IS_33KV_SELECTED, data.getIS_33KV_SELECTED());
//        values.put(IS_LT_SELECTED, data.getIS_33KV_SELECTED());
//        values.put(FEEDER_ID, data.getFEEDER_ID());
//        values.put(SS_ID, data.getSS_ID());
//        values.put(SS_NAME, data.getSS_NAME());
//        values.put(SS_TYPE, data.getSS_TYPE());
//
//        long a = db.insert(WORK_TABLE, null, values);
//        db.close();
//        if (a == -1) {
//            return false;
//        } else {
//            return true;
//        }
//
//    }


    public List<WorkInvolvementModel> getEstFeedersdata(String est_no) {
        ArrayList<WorkInvolvementModel> data = new ArrayList<>();
        String query = "SELECT * FROM EST_FEEDERS WHERE EST_NO = '" + est_no + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    WorkInvolvementModel model = new WorkInvolvementModel();
                    model.setEST_NO(cursor.getString(0));
                    model.setREF_ID(cursor.getString(1));
                    model.setWORK_TYPE(cursor.getString(2));
                    model.setFEEDER_ID(cursor.getString(3));
                    model.setFEEDER_NAME(cursor.getString(4));
                    model.setFEEDER_NAME(cursor.getString(5));
                    model.setSURVEY_TYPE(cursor.getString(6));
                    model.setFDR_COUNT(cursor.getInt(7));
                    model.setSS_ID(cursor.getString(8));
                    model.setSS_NAME(cursor.getString(9));
                    model.setFDR_INDEX(cursor.getInt(10));

                    data.add(model);
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return data;
    }


    public String getFeederDetails33KV(String est_no, String work_type, String gis_id) {
        String feederDetails = "";
        String query = "SELECT FEEDER_ID, FEEDER_NAME FROM WORK_TABLE WHERE GIS_ID = ? AND WORK_TYPE = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, new String[]{gis_id, work_type});

            if (cursor.moveToFirst()) {
                String FEEDER_ID = cursor.getString(0);
                String FEEDER_NAME = cursor.getString(1);
                feederDetails = "FEEDER_ID: " + FEEDER_ID + ", FEEDER_NAME: " + FEEDER_NAME;
            } else {
                //   Log.d("Database", "No feeder details found for EST_NO: " + est_no + " and WORK_TYPE: " + work_type);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return feederDetails;
    }


    public String getFeederDetails11KV(String est_no, String work_type, String gis_id) {
        String feederDetails = "";
        String query = "SELECT FEEDER_ID, FEEDER_NAME FROM WORK_TABLE WHERE GIS_ID = ? AND WORK_TYPE = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, new String[]{gis_id, work_type});

            if (cursor.moveToFirst()) {
                String FEEDER_ID = cursor.getString(0);
                String FEEDER_NAME = cursor.getString(1);
                feederDetails = "FEEDER_ID: " + FEEDER_ID + ", FEEDER_NAME: " + FEEDER_NAME;
            } else {
                //   Log.d("Database", "No feeder details found for EST_NO: " + est_no + " and WORK_TYPE: " + work_type);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return feederDetails;
    }


    public String getReferenceID(String est_no, String work_type) {
        String REF_ID = "";
        String query = "SELECT REF_ID FROM WORK_TABLE WHERE EST_NO = '" + est_no + "' AND WORK_TYPE = '" + work_type + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    REF_ID = cursor.getString(0);
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return REF_ID;
    }

    public boolean getSurveyStatus(String est_no, String work_type) {
        boolean status = false;
        String surveyStatus;
        String query = "SELECT IS_SURVEY_COMPLETED FROM WORK_TABLE WHERE EST_NO = '" + est_no + "' AND WORK_TYPE = '" + work_type + "'";
        // System.out.println("GET SURVEY STATUS query : " + query);
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    surveyStatus = cursor.getString(0);
                    if (surveyStatus.equalsIgnoreCase("Y")) {
                        status = true;
                    } else {
                        status = false;
                    }
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return status;
    }

    public boolean get33kVSurveyStatus(String est_no, String work_type) {
        boolean status = false;
        String surveyStatus;
        String query = "SELECT IS_33KV_SURVEY_COMPLETED FROM WORK_TABLE WHERE EST_NO = '" + est_no + "' AND WORK_TYPE = '" + work_type + "'";
        //  System.out.println("GET SURVEY STATUS query : " + query);
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    surveyStatus = cursor.getString(0);
                    if (surveyStatus.equalsIgnoreCase("Y")) {
                        status = true;
                    } else {
                        status = false;
                    }
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return status;
    }

    public boolean get11kVSurveyStatus(String est_no, String work_type) {
        boolean status = false;
        String surveyStatus;
        String query = "SELECT IS_11KV_SURVEY_COMPLETED FROM WORK_TABLE WHERE EST_NO = '" + est_no + "' AND WORK_TYPE = '" + work_type + "'";
        //   System.out.println("GET SURVEY STATUS query : " + query);
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    surveyStatus = cursor.getString(0);
                    if (surveyStatus.equalsIgnoreCase("Y")) {
                        status = true;
                    } else {
                        status = false;
                    }
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return status;
    }


    public boolean getLTSurveyStatus(String est_no, String work_type) {
        boolean status = false;
        String surveyStatus;
        String query = "SELECT IS_LT_SURVEY_COMPLETED FROM WORK_TABLE WHERE EST_NO = '" + est_no + "' AND WORK_TYPE = '" + work_type + "'";
        //  System.out.println("GET SURVEY STATUS query : " + query);
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    surveyStatus = cursor.getString(0);
                    if (surveyStatus.equalsIgnoreCase("Y")) {
                        status = true;
                    } else {
                        status = false;
                    }
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return status;
    }

    public void updateSurveyStatus(String est_no, String work_type) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(IS_SURVEY_COMPLETED, "Y");
        String whereClause = "EST_NO = ? AND WORK_TYPE = ?";
        String[] whereArgs = {est_no, String.valueOf(work_type)};
        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.update(WORK_TABLE, contentValues, whereClause, whereArgs);

    }

    public boolean addSurveyStatusNew(String est_no, String gis_id) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EST_NO, est_no);
        values.put(IS_SURVEY_COMPLETED, "N");
        values.put(GIS_ID, gis_id);
        values.put(FLAG, "N");

        long a = db.insert(STATUS_TABLE, null, values);
        db.close();
        if (a == -1) {
            return false;
        } else {
            return true;
        }

    }

    public boolean add33kVSurveyStatusNew(String est_no) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EST_NO, est_no);
        values.put(IS_33KV_SURVEY_COMPLETED, "N");
        values.put(FLAG, "N");

        long a = db.insert(STATUS_TABLE, null, values);
        db.close();
        if (a == -1) {
            return false;
        } else {
            return true;
        }

    }

    public boolean add11kVSurveyStatusNew(String est_no) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EST_NO, est_no);
        values.put(IS_11KV_SURVEY_COMPLETED, "N");
        values.put(FLAG, "N");

        long a = db.insert(STATUS_TABLE, null, values);
        db.close();
        if (a == -1) {
            return false;
        } else {
            return true;
        }

    }

    public boolean addLTSurveyStatusNew(String est_no) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EST_NO, est_no);
        values.put(IS_LT_SURVEY_COMPLETED, "N");
        values.put(FLAG, "N");

        long a = db.insert(STATUS_TABLE, null, values);
        db.close();
        if (a == -1) {
            return false;
        } else {
            return true;
        }

    }

    public boolean getSurveyStatusNEW(String est_no, String gis_id) {
        boolean status = false;
        String surveyStatus;
        String query = "SELECT IS_SURVEY_COMPLETED FROM STATUS_TABLE WHERE GIS_ID = '" + gis_id + "'";
        //  System.out.println("GET SURVEY STATUS NEW QUERY : " + query);
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    surveyStatus = cursor.getString(0);
                    //  System.out.println("SURVEY STATUS : " + surveyStatus);
                    if (surveyStatus.equalsIgnoreCase("Y")) {
                        status = true;
                    } else {
                        status = false;
                    }
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return status;
    }

    public boolean get33kvSurveyStatusNEW(String est_no, String gis_id) {
        boolean status = false;
        String surveyStatus;
        String query = "SELECT IS_33KV_SURVEY_COMPLETED FROM STATUS_TABLE WHERE GIS_ID = '" + gis_id + "'";
        //  System.out.println("GET SURVEY STATUS NEW QUERY : " + query);
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    surveyStatus = cursor.getString(0);
                    //  System.out.println("SURVEY STATUS : " + surveyStatus);
                    if (surveyStatus.equalsIgnoreCase("Y")) {
                        status = true;
                    } else {
                        status = false;
                    }
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return status;
    }

    public boolean getFeeder1SurveyStatusNEW(String est_no, String gis_id) {
        boolean status = false;
        String surveyStatus;

        String query = "SELECT IS_11KV_FEEDER_1_SURVEY_COMPLETED FROM STATUS_TABLE WHERE GIS_ID = '" + gis_id + "'";
        // System.out.println("GET SURVEY STATUS NEW QUERY : " + query);
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    surveyStatus = cursor.getString(0);
                    //  System.out.println("SURVEY STATUS : " + surveyStatus);
                    if (surveyStatus.equalsIgnoreCase("Y")) {
                        status = true;
                    } else {
                        status = false;
                    }
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return status;
    }

    public boolean getFeeder2SurveyStatusNEW(String est_no, String gis_id) {
        boolean status = false;
        String surveyStatus;

        String query = "SELECT IS_11KV_FEEDER_2_SURVEY_COMPLETED FROM STATUS_TABLE WHERE GIS_ID = '" + gis_id + "'";
        // System.out.println("GET SURVEY STATUS NEW QUERY : " + query);
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    surveyStatus = cursor.getString(0);
                    //  System.out.println("SURVEY STATUS : " + surveyStatus);
                    if (surveyStatus.equalsIgnoreCase("Y")) {
                        status = true;
                    } else {
                        status = false;
                    }
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return status;
    }

    public boolean getFeeder3SurveyStatusNEW(String est_no, String gis_id) {
        boolean status = false;
        String surveyStatus;

        String query = "SELECT IS_11KV_FEEDER_3_SURVEY_COMPLETED FROM STATUS_TABLE WHERE GIS_ID = '" + gis_id + "'";
        //  System.out.println("GET SURVEY STATUS NEW QUERY : " + query);
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    surveyStatus = cursor.getString(0);
                    //  System.out.println("SURVEY STATUS : " + surveyStatus);
                    if (surveyStatus.equalsIgnoreCase("Y")) {
                        status = true;
                    } else {
                        status = false;
                    }
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return status;
    }

    public boolean getFeeder4SurveyStatusNEW(String est_no, String gis_id) {
        boolean status = false;
        String surveyStatus;

        String query = "SELECT IS_11KV_FEEDER_4_SURVEY_COMPLETED FROM STATUS_TABLE WHERE GIS_ID = '" + gis_id + "'";
        // System.out.println("GET SURVEY STATUS NEW QUERY : " + query);
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    surveyStatus = cursor.getString(0);
                    // System.out.println("SURVEY STATUS : " + surveyStatus);
                    if (surveyStatus.equalsIgnoreCase("Y")) {
                        status = true;
                    } else {
                        status = false;
                    }
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return status;
    }

    public boolean getFeeder5SurveyStatusNEW(String est_no, String gis_id) {
        boolean status = false;
        String surveyStatus;

        String query = "SELECT IS_11KV_FEEDER_5_SURVEY_COMPLETED FROM STATUS_TABLE WHERE GIS_ID = '" + gis_id + "'";
        // System.out.println("GET SURVEY STATUS NEW QUERY : " + query);
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    surveyStatus = cursor.getString(0);
                    //  System.out.println("SURVEY STATUS : " + surveyStatus);
                    if (surveyStatus.equalsIgnoreCase("Y")) {
                        status = true;
                    } else {
                        status = false;
                    }
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return status;
    }

    public boolean getFeeder6SurveyStatusNEW(String est_no, String gis_id) {
        boolean status = false;
        String surveyStatus;

        String query = "SELECT IS_11KV_FEEDER_6_SURVEY_COMPLETED FROM STATUS_TABLE WHERE GIS_ID = '" + gis_id + "'";
        // System.out.println("GET SURVEY STATUS NEW QUERY : " + query);
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    surveyStatus = cursor.getString(0);
                    //  System.out.println("SURVEY STATUS : " + surveyStatus);
                    if (surveyStatus.equalsIgnoreCase("Y")) {
                        status = true;
                    } else {
                        status = false;
                    }
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return status;
    }

    public boolean getFeeder7SurveyStatusNEW(String est_no, String gis_id) {
        boolean status = false;
        String surveyStatus;

        String query = "SELECT IS_11KV_FEEDER_7_SURVEY_COMPLETED FROM STATUS_TABLE WHERE GIS_ID = '" + gis_id + "'";
        // System.out.println("GET SURVEY STATUS NEW QUERY : " + query);
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    surveyStatus = cursor.getString(0);
                    //  System.out.println("SURVEY STATUS : " + surveyStatus);
                    if (surveyStatus.equalsIgnoreCase("Y")) {
                        status = true;
                    } else {
                        status = false;
                    }
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return status;
    }

    public boolean getFeeder8SurveyStatusNEW(String est_no, String gis_id) {
        boolean status = false;
        String surveyStatus;

        String query = "SELECT IS_11KV_FEEDER_8_SURVEY_COMPLETED FROM STATUS_TABLE WHERE GIS_ID = '" + gis_id + "'";
        // System.out.println("GET SURVEY STATUS NEW QUERY : " + query);
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    surveyStatus = cursor.getString(0);
                    //  System.out.println("SURVEY STATUS : " + surveyStatus);
                    if (surveyStatus.equalsIgnoreCase("Y")) {
                        status = true;
                    } else {
                        status = false;
                    }
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return status;
    }

    public boolean getFeeder9SurveyStatusNEW(String est_no, String gis_id) {
        boolean status = false;
        String surveyStatus;

        String query = "SELECT IS_11KV_FEEDER_9_SURVEY_COMPLETED FROM STATUS_TABLE WHERE GIS_ID = '" + gis_id + "'";
        // System.out.println("GET SURVEY STATUS NEW QUERY : " + query);
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    surveyStatus = cursor.getString(0);
                    //  System.out.println("SURVEY STATUS : " + surveyStatus);
                    if (surveyStatus.equalsIgnoreCase("Y")) {
                        status = true;
                    } else {
                        status = false;
                    }
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return status;
    }

    public boolean getFeeder10SurveyStatusNEW(String est_no, String gis_id) {
        boolean status = false;
        String surveyStatus;

        String query = "SELECT IS_11KV_FEEDER_10_SURVEY_COMPLETED FROM STATUS_TABLE WHERE GIS_ID = '" + gis_id + "'";
        // System.out.println("GET SURVEY STATUS NEW QUERY : " + query);
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    surveyStatus = cursor.getString(0);
                    //  System.out.println("SURVEY STATUS : " + surveyStatus);
                    if (surveyStatus.equalsIgnoreCase("Y")) {
                        status = true;
                    } else {
                        status = false;
                    }
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return status;
    }


    public boolean getMetersSurveyStatusNEW(String est_no, String gis_id) {
        boolean status = false;
        String surveyStatus;
        String query = "SELECT IS_METERS_SURVEY_COMPLETED FROM STATUS_TABLE WHERE GIS_ID = '" + gis_id + "'";
        //  System.out.println("GET SURVEY STATUS NEW QUERY : " + query);
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    surveyStatus = cursor.getString(0);
                    //   System.out.println("SURVEY STATUS : " + surveyStatus);
                    if (surveyStatus.equalsIgnoreCase("Y")) {
                        status = true;
                    } else {
                        status = false;
                    }
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return status;
    }

    public boolean get11kvSurveyStatusNEW(String est_no, String gis_id) {
        boolean status = false;
        String surveyStatus;
        String query = "SELECT IS_11KV_SURVEY_COMPLETED FROM STATUS_TABLE WHERE GIS_ID = '" + gis_id + "'";
        //  System.out.println("GET SURVEY STATUS NEW QUERY : " + query);
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    surveyStatus = cursor.getString(0);
                    //   System.out.println("SURVEY STATUS : " + surveyStatus);
                    if (surveyStatus.equalsIgnoreCase("Y")) {
                        status = true;
                    } else {
                        status = false;
                    }
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return status;
    }


    public boolean getLTSurveyStatusNEW(String est_no, String gis_id) {
        boolean status = false;
        String surveyStatus;
        String query = "SELECT IS_LT_SURVEY_COMPLETED FROM STATUS_TABLE WHERE GIS_ID = '" + gis_id + "'";
        //  System.out.println("GET SURVEY STATUS NEW QUERY : " + query);
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    surveyStatus = cursor.getString(0);
                    //  System.out.println("SURVEY STATUS : " + surveyStatus);
                    if (surveyStatus.equalsIgnoreCase("Y")) {
                        status = true;
                    } else {
                        status = false;
                    }
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return status;
    }

    public boolean isSurveyExistsNEW(String est_no, String gis_id) {
        boolean status = false;
        String surveyStatus;
        String query = "SELECT GIS_ID FROM STATUS_TABLE WHERE GIS_ID = '" + gis_id + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    surveyStatus = cursor.getString(0);
                    if (surveyStatus.equalsIgnoreCase(gis_id)) {
                        status = true;
                    } else {
                        status = false;
                    }
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return status;
    }

    public boolean is33kvSurveyExistsNEW(String est_no) {
        boolean status = false;
        String surveyStatus;
        String query = "SELECT EST_NO FROM STATUS_TABLE WHERE EST_NO = '" + est_no + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    surveyStatus = cursor.getString(0);
                    if (surveyStatus.equalsIgnoreCase(est_no)) {
                        status = true;
                    } else {
                        status = false;
                    }
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return status;
    }

    public boolean updateSurveyStatusNEW(String est_no, String gis_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(IS_SURVEY_COMPLETED, "Y");
        String whereClause = "GIS_ID = ?";
        String[] whereArgs = {gis_id};
        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.update(STATUS_TABLE, contentValues, whereClause, whereArgs);
        if (result > 0) {
            return true;
        } else {
            return false;
        }

    }

    public boolean update33kvSurveyStatusNEW(String est_no, String gis_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(IS_33KV_SURVEY_COMPLETED, "Y");
        String whereClause = "GIS_ID = ?";
        String[] whereArgs = {gis_id};
        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.update(STATUS_TABLE, contentValues, whereClause, whereArgs);
        if (result > 0) {
            return true;
        } else {
            return false;
        }

    }

    public boolean update33kvSurveyforReSurveyStatusNEW(String est_no, boolean isSurveyCompleted, String gis_id) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(IS_33KV_SURVEY_COMPLETED, isSurveyCompleted ? "Y" : "N");

        String whereClause = "GIS_ID = ?";
        String[] whereArgs = {gis_id};

        SQLiteDatabase database = this.getWritableDatabase();

        int result = database.update(STATUS_TABLE, contentValues, whereClause, whereArgs);

        //   Log.d("DEBUG_UPDATE", "Update Result: " + result);
        // Check if update was successful
        return result > 0;
    }


    public boolean update11kvSelectSurveyUpdateStatusNEW(String est_no, String gis_id, boolean isSurveySelected) {
        //   Log.d("update11kv", "Updating EST_NO: " + est_no + ", Is Survey Selected: " + isSurveySelected);

       /* ContentValues contentValues = new ContentValues();
        contentValues.put(IS_11KV_SELECTED, isSurveySelected ? "Y" : "N");

        String whereClause = "EST_NO = ?";
        String[] whereArgs = {est_no};

        SQLiteDatabase database = this.getWritableDatabase();

        int result = database.update(STATUS_TABLE, contentValues, whereClause, whereArgs);

        Log.d("update11kv", "Rows Affected: " + result);
        return result > 0;*/


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EST_NO, est_no);
        values.put(IS_11KV_SELECTED, isSurveySelected ? "Y" : "N");
        values.put(IS_33KV_SELECTED, isSurveySelected ? "Y" : "N");
        values.put(IS_LT_SELECTED, isSurveySelected ? "Y" : "N");

        long a = db.insert(STATUS_TABLE, null, values);
        db.close();
        if (a == -1) {
            return false;
        } else {
            return true;
        }

    }

    public boolean updateSelectStatusALL(String est_no, boolean is33, boolean is11, boolean isLT, int FEEDER_COUNT, int FEEDER_COUNT_33, String gis_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EST_NO, est_no);
        values.put(GIS_ID, gis_id);
        values.put(IS_33KV_SELECTED, is33 ? "Y" : "N");
        values.put(IS_11KV_SELECTED, is11 ? "Y" : "N");
        values.put(IS_LT_SELECTED, isLT ? "Y" : "N");
        if (FEEDER_COUNT == 0) {
            values.put(IS_11KV_FEEDER_1_SELECTED, "N");
            values.put(IS_11KV_FEEDER_2_SELECTED, "N");
            values.put(IS_11KV_FEEDER_3_SELECTED, "N");
            values.put(IS_11KV_FEEDER_4_SELECTED, "N");
            values.put(IS_11KV_FEEDER_5_SELECTED, "N");
        }
        if (FEEDER_COUNT == 1) {
            values.put(IS_11KV_FEEDER_1_SELECTED, "Y");
            values.put(IS_11KV_FEEDER_2_SELECTED, "N");
            values.put(IS_11KV_FEEDER_3_SELECTED, "N");
            values.put(IS_11KV_FEEDER_4_SELECTED, "N");
            values.put(IS_11KV_FEEDER_5_SELECTED, "N");
        }
        if (FEEDER_COUNT == 2) {
            values.put(IS_11KV_FEEDER_1_SELECTED, "Y");
            values.put(IS_11KV_FEEDER_2_SELECTED, "Y");
            values.put(IS_11KV_FEEDER_3_SELECTED, "N");
            values.put(IS_11KV_FEEDER_4_SELECTED, "N");
            values.put(IS_11KV_FEEDER_5_SELECTED, "N");
        }
        if (FEEDER_COUNT == 3) {
            values.put(IS_11KV_FEEDER_1_SELECTED, "Y");
            values.put(IS_11KV_FEEDER_2_SELECTED, "Y");
            values.put(IS_11KV_FEEDER_3_SELECTED, "Y");
            values.put(IS_11KV_FEEDER_4_SELECTED, "N");
            values.put(IS_11KV_FEEDER_5_SELECTED, "N");
        }
        if (FEEDER_COUNT == 4) {
            values.put(IS_11KV_FEEDER_1_SELECTED, "Y");
            values.put(IS_11KV_FEEDER_2_SELECTED, "Y");
            values.put(IS_11KV_FEEDER_3_SELECTED, "Y");
            values.put(IS_11KV_FEEDER_4_SELECTED, "Y");
            values.put(IS_11KV_FEEDER_5_SELECTED, "N");
        }
        if (FEEDER_COUNT == 5) {
            values.put(IS_11KV_FEEDER_1_SELECTED, "Y");
            values.put(IS_11KV_FEEDER_2_SELECTED, "Y");
            values.put(IS_11KV_FEEDER_3_SELECTED, "Y");
            values.put(IS_11KV_FEEDER_4_SELECTED, "Y");
            values.put(IS_11KV_FEEDER_5_SELECTED, "Y");
        }

        //NEW CODE FOR 33KV LINE EXTENSION START
        if (FEEDER_COUNT_33 == 0) {
            values.put(IS_33KV_FEEDER_1_SELECTED, "N");
            values.put(IS_33KV_FEEDER_2_SELECTED, "N");
            values.put(IS_33KV_FEEDER_3_SELECTED, "N");
            values.put(IS_33KV_FEEDER_4_SELECTED, "N");
            values.put(IS_33KV_FEEDER_5_SELECTED, "N");
        }
        if (FEEDER_COUNT_33 == 1) {
            values.put(IS_33KV_FEEDER_1_SELECTED, "Y");
            values.put(IS_33KV_FEEDER_2_SELECTED, "N");
            values.put(IS_33KV_FEEDER_3_SELECTED, "N");
            values.put(IS_33KV_FEEDER_4_SELECTED, "N");
            values.put(IS_33KV_FEEDER_5_SELECTED, "N");
        }
        if (FEEDER_COUNT_33 == 2) {
            values.put(IS_33KV_FEEDER_1_SELECTED, "Y");
            values.put(IS_33KV_FEEDER_2_SELECTED, "Y");
            values.put(IS_33KV_FEEDER_3_SELECTED, "N");
            values.put(IS_33KV_FEEDER_4_SELECTED, "N");
            values.put(IS_33KV_FEEDER_5_SELECTED, "N");
        }
        if (FEEDER_COUNT_33 == 3) {
            values.put(IS_33KV_FEEDER_1_SELECTED, "Y");
            values.put(IS_33KV_FEEDER_2_SELECTED, "Y");
            values.put(IS_33KV_FEEDER_3_SELECTED, "Y");
            values.put(IS_33KV_FEEDER_4_SELECTED, "N");
            values.put(IS_33KV_FEEDER_5_SELECTED, "N");
        }
        if (FEEDER_COUNT_33 == 4) {
            values.put(IS_33KV_FEEDER_1_SELECTED, "Y");
            values.put(IS_33KV_FEEDER_2_SELECTED, "Y");
            values.put(IS_33KV_FEEDER_3_SELECTED, "Y");
            values.put(IS_33KV_FEEDER_4_SELECTED, "Y");
            values.put(IS_33KV_FEEDER_5_SELECTED, "N");
        }
        if (FEEDER_COUNT_33 == 5) {
            values.put(IS_33KV_FEEDER_1_SELECTED, "Y");
            values.put(IS_33KV_FEEDER_2_SELECTED, "Y");
            values.put(IS_33KV_FEEDER_3_SELECTED, "Y");
            values.put(IS_33KV_FEEDER_4_SELECTED, "Y");
            values.put(IS_33KV_FEEDER_5_SELECTED, "Y");
        }


        long a = db.insert(STATUS_TABLE, null, values);
        db.close();
        if (a == -1) {
            return false;
        } else {
            return true;
        }
    }

    //05-10-24
    public boolean updateNewSelectStatusALL(String est_no, boolean is33, boolean is11, boolean isLT) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EST_NO, est_no);
        values.put(IS_33KV_SELECTED, is33 ? "Y" : "N");
        values.put(IS_11KV_SELECTED, is11 ? "Y" : "N");
        values.put(IS_LT_SELECTED, isLT ? "Y" : "N");

        // Use the update() method to modify existing rows
        int rowsAffected = db.update(STATUS_TABLE, values, EST_NO + " = ?", new String[]{est_no});
        db.close();

        // Check if the update affected any rows
        if (rowsAffected == 0) {
            // No rows were updated, meaning the est_no doesn't exist, so insert a new row
            long a = db.insert(STATUS_TABLE, null, values);
            return a != -1;
        } else {
            return true;
        }
    }


    public boolean updateTheSelectStatusALL(String est_no, boolean is33, boolean is11, boolean isLT) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IS_33KV_SELECTED, is33 ? "Y" : "N");
        values.put(IS_11KV_SELECTED, is11 ? "Y" : "N");
        values.put(IS_LT_SELECTED, isLT ? "Y" : "N");

        // Update row(s) based on est_no
        int rowsAffected = db.update(STATUS_TABLE, values, EST_NO + " = ?", new String[]{est_no});
        db.close();

        return rowsAffected > 0;
    }


    public boolean updateLTSelectSurveyUpdateStatusNEW(String est_no, boolean isSurveySelected, String gis_id) {
        //   Log.d("update11kv", "Updating EST_NO: " + est_no + ", Is Survey Selected: " + isSurveySelected);

       /* ContentValues contentValues = new ContentValues();
        contentValues.put(IS_LT_SELECTED, isSurveySelected ? "Y" : "N");

        String whereClause = "EST_NO = ?";
        String[] whereArgs = {est_no};

        SQLiteDatabase database = this.getWritableDatabase();

        int result = database.update(STATUS_TABLE, contentValues, whereClause, whereArgs);

        Log.d("update11kv", "Rows Affected: " + result);
        return result > 0;*/

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EST_NO, est_no);
        values.put(GIS_ID, gis_id);
        values.put(IS_LT_SELECTED, isSurveySelected ? "Y" : "N");

        long a = db.insert(STATUS_TABLE, null, values);
        db.close();
        if (a == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean update33kvSelectSurveyUpdateStatusNEW(String est_no, boolean isSurveySelected) {
       /* ContentValues contentValues = new ContentValues();
        contentValues.put(IS_33KV_SELECTED, isSurveySelected ? "Y" : "N");
        String whereClause = "EST_NO = ?";
        String[] whereArgs = {est_no};

        SQLiteDatabase database = this.getWritableDatabase();

        int result = database.update(STATUS_TABLE, contentValues, whereClause, whereArgs);

        return result > 0;*/

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EST_NO, est_no);
        values.put(IS_33KV_SELECTED, isSurveySelected ? "Y" : "N");

        long a = db.insert(STATUS_TABLE, null, values);
        db.close();
        if (a == -1) {
            return false;
        } else {
            return true;
        }
    }

    public List<StatusModel> getSelectStatus(String est_no, String gis_id) {
        ArrayList<StatusModel> data = new ArrayList<>();
        String query = "SELECT * FROM STATUS_TABLE WHERE GIS_ID = '" + gis_id + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    StatusModel model = new StatusModel();
                    model.setIs11(cursor.getString(5));
                    model.setIs33(cursor.getString(6));
                    model.setIsLT(cursor.getString(7));
                    data.add(model);
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return data;
    }

    public boolean getltSurveySelectStatusNEW(String est_no) {
        boolean status = false;
        String surveyStatus;
        String query = "SELECT IS_LT_SELECTED FROM STATUS_TABLE WHERE EST_NO = '" + est_no + "'";
        //  System.out.println("GET SURVEY STATUS NEW QUERY : " + query);
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    surveyStatus = cursor.getString(0);
                    //   System.out.println("SURVEY STATUS : " + surveyStatus);
                    if (surveyStatus.equalsIgnoreCase("Y")) {
                        status = true;
                    } else {
                        status = false;
                    }
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return status;
    }

    public boolean get11kvSurveySelectStatusNEW(String est_no) {
        boolean status = false;
        String surveyStatus;
        String query = "SELECT IS_11KV_SELECTED FROM STATUS_TABLE WHERE EST_NO = '" + est_no + "'";
        // System.out.println("GET SURVEY STATUS NEW QUERY : " + query);
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    surveyStatus = cursor.getString(0);
                    //  System.out.println("SURVEY STATUS : " + surveyStatus);
                    if (surveyStatus.equalsIgnoreCase("Y")) {
                        status = true;
                    } else {
                        status = false;
                    }
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return status;
    }

    public boolean clearExistingPolesTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            db.delete(EXISTING_POLES_TABLE_NEW, null, null);
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public boolean get33kvSurveySelectStatusNEW(String est_no) {
        boolean status = false;
        String surveyStatus;
        String query = "SELECT IS_33KV_SELECTED FROM STATUS_TABLE WHERE EST_NO = '" + est_no + "'";
        //   System.out.println("GET SURVEY STATUS NEW QUERY : " + query);
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    surveyStatus = cursor.getString(0);
                    //  System.out.println("SURVEY STATUS : " + surveyStatus);
                    if (surveyStatus.equalsIgnoreCase("Y")) {
                        status = true;
                    } else {
                        status = false;
                    }
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return status;
    }


    public boolean update11kvSurveyforReSurveyStatusNEW(String est_no, boolean isSurveyCompleted, String gis_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(IS_11KV_SURVEY_COMPLETED, isSurveyCompleted ? "Y" : "N");
        String whereClause = "GIS_ID = ?";
        String[] whereArgs = {gis_id};
        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.update(STATUS_TABLE, contentValues, whereClause, whereArgs);
        return result > 0;
    }

    public boolean updateLTSurveyforReSurveyStatusNEW(String est_no, boolean isSurveyCompleted, String gis_id) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(IS_LT_SURVEY_COMPLETED, isSurveyCompleted ? "Y" : "N");

        String whereClause = "GIS_ID = ?";
        String[] whereArgs = {gis_id};

        SQLiteDatabase database = this.getWritableDatabase();

        int result = database.update(STATUS_TABLE, contentValues, whereClause, whereArgs);

        // Check if update was successful
        return result > 0;
    }

    public boolean updateMetersSurveyforReSurveyStatusNEW(String est_no, boolean isSurveyCompleted, String gis_id) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(IS_METERS_SURVEY_COMPLETED, isSurveyCompleted ? "Y" : "N");

        String whereClause = "GIS_ID = ?";
        String[] whereArgs = {gis_id};

        SQLiteDatabase database = this.getWritableDatabase();

        int result = database.update(STATUS_TABLE, contentValues, whereClause, whereArgs);

        // Check if update was successful
        return result > 0;
    }

    public boolean update11kvSurveyStatusNEW(String est_no, String gis_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(IS_11KV_SURVEY_COMPLETED, "Y");
        String whereClause = "GIS_ID = ?";
        String[] whereArgs = {gis_id};
        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.update(STATUS_TABLE, contentValues, whereClause, whereArgs);
        if (result > 0) {
            return true;
        } else {
            return false;
        }

    }


    public boolean updateLTSurveyStatusNEW(String est_no, String gis_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(IS_LT_SURVEY_COMPLETED, "Y");
        String whereClause = "GIS_ID = ?";
        String[] whereArgs = {gis_id};
        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.update(STATUS_TABLE, contentValues, whereClause, whereArgs);
        if (result > 0) {
            return true;
        } else {
            return false;
        }

    }


    public boolean updateMeterSurveyStatusNEW(String est_no, String gis_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(IS_METERS_SURVEY_COMPLETED, "Y");
        String whereClause = "GIS_ID = ?";
        String[] whereArgs = {gis_id};
        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.update(STATUS_TABLE, contentValues, whereClause, whereArgs);
        if (result > 0) {
            return true;
        } else {
            return false;
        }

    }

    public boolean reSurvey(String est_no, String gis_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(IS_SURVEY_COMPLETED, "N");
        String whereClause = "GIS_ID = ?";
        String[] whereArgs = {gis_id};
        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.update(STATUS_TABLE, contentValues, whereClause, whereArgs);
        if (result > 0) {
            return true;
        } else {
            return false;
        }

    }


    public boolean deleteSurveyStatusNEW(String est_no, String gis_id) {
        String whereClause = "GIS_ID = ?";
        String[] whereArgs = {gis_id};
        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.delete(STATUS_TABLE, whereClause, whereArgs);
        if (result > 0) {
            return true;
        } else {
            return false;
        }

    }


    public boolean add33KVSurveyData(Estimates33KVModel data) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EST_NO_33KV, data.getEST_NO());
        values.put(FROM_POLE_33KV, data.getFROM_POLE());
        values.put(POLE_NO_33KV, data.getPOLE_NO());
        values.put(LINE_TYPE_33KV, data.getLINE_TYPE());
        values.put(POLE_SURVEY_TYPE_33KV, data.getPOLE_SURVEY_TYPE());
        values.put(IS_MULTI_CKT_33KV, data.getIS_MULTI_CKT());
        values.put(MULTI_CKT_33_33KV, data.getMULTI_CKT_33());
        values.put(MULTI_CKT_11_33KV, data.getMULTI_CKT_11());
        values.put(MULTI_CKT_LT_33KV, data.getMULTI_CKT_LT());
        values.put(POLE_TYPE_33KV, data.getPOLE_TYPE());
        values.put(POLE_SUPPORT_33KV, data.getPOLE_SUPPORT());
        values.put(No_OF_STAY_33KV, data.getNo_OF_STAY());
        values.put(No_OF_STRUT_33KV, data.getNo_OF_STRUT());
        values.put(EXI_POLE_NO_33KV, data.getEXI_POLE_NO());
        values.put(POLE_LOCATION_TYPE_33KV, data.getPOLE_LOCATION_TYPE());
        values.put(POLE_HEIGHT_33KV, data.getPOLE_HEIGHT());
        values.put(CONDUTCOR_SIZE_33KV, data.getCONDUTCOR_SIZE());
        values.put(LINE_SURVEY_TYPE_33KV, data.getLINE_SURVEY_TYPE());
        values.put(DTR_PHASE_33KV, data.getDTR_PHASE());
        values.put(DTR_CAPACITY_33KV, data.getDTR_CAPACITY());
        values.put(DTR_MOUNTING_33KV, data.getDTR_MOUNTING());
        values.put(DTR_SURVEY_TYPE_33KV, data.getDTR_SURVEY_TYPE());
        values.put(LOCATION_ID_33KV, data.getLOCATION_ID());
        values.put(NUMB_9M_STRUTS_33KV, data.getNUMB_9M_STRUTS());
        values.put(NUMB_8M_STRUTS_33KV, data.getNUMB_8M_STRUTS());
        values.put(NUMB_STAYS_33KV, data.getNUMB_STAYS());
        values.put(NUMBERS_33KV_FEEDERS, data.getNUMBERS_OF_FEEDERS());
        values.put(DTR_STRUCTURE_CODE_33KV, data.getDTR_STRUCTURE_CODE());
        values.put(POLE_DESC_33KV, data.getMATERIALPART1());
        values.put(POLE_SAP_CODE_33KV, data.getPOLE_SAP_CODE());
        values.put(CONDUCTOR_DESC_33KV, data.getCONDUCTOR_DESC());
        values.put(CONDUCTOR_SAP_CODE_33KV, data.getCONDUCTOR_SAP_CODE());
        values.put(AB_SWITCH_33KV, data.getAB_SWITCH());
        values.put(AB_DESC_33KV, data.getAB_DESC());
        values.put(SS_ID_33KV, data.getSS_ID());
        values.put(SS_NAME_33KV, data.getSS_NAME());
        values.put(SS_TYPE_33KV, data.getSS_TYPE());
        values.put(FEEDER_ID_33KV, data.getFEEDER_ID());
        values.put(FEEDER_NAME_33KV, data.getFEEDER_NAME());
        values.put(COUNT_TAPPING_XARM_33KV, data.getCOUNT_TAPPING_XARM());
        values.put(COUNT_V_XARM_33KV, data.getCOUNT_V_XARM());
        values.put(COUNT_TOP_CLEAT_33KV, data.getCOUNT_TOP_CLEAT());
        values.put(COUNT_PIN_INS_33KV, data.getCOUNT_PIN_INS());
        values.put(COUNT_DISC_INS_33KV, data.getCOUNT_DISC_INS());
        values.put(COUNT_SIDE_ARM_33KV, data.getCOUNT_SIDE_ARM());
        values.put(COUNT_METALS_PARTS_33KV, data.getCOUNT_METALS_PARTS());
        values.put(TAPPINGXARM_CODE_33KV, data.getTAPPINGXARM_CODE());
        values.put(TAPPINGXARM_DESC_33KV, data.getTAPPINGXARM_DESC());
        values.put(VXARM_CODE_33KV, data.getVXARM_CODE());
        values.put(VXARM_DESC_33KV, data.getVXARM_DESC());
        values.put(SIDEARM_CODE_33KV, data.getSIDEARM_CODE());
        values.put(SIDEARM_DESC_33KV, data.getSIDEARM_DESC());
        values.put(CHANNEL_CODE_33KV, data.getCHANNEL_CODE());
        values.put(CHANNEL_DESC_33KV, data.getCHANNEL_DESC());
        values.put(COUNT_CHANNEL_33KV, data.getCOUNT_CHANNEL());
        values.put(TOPCLEAT_CODE_33KV, data.getTOPCLEAT_CODE());
        values.put(TOPCLEAT_DESC_33KV, data.getTOPCLEAT_DESC());
        values.put(PIN_INS_CODE_33KV, data.getPIN_INS_CODE());
        values.put(PIN_INS_DESC_33KV, data.getPIN_INS_DESC());
        values.put(COUNT_PIN_33KV, data.getCOUNT_PIN());
        values.put(DISC_INS_CODE_33KV, data.getDISC_INS_CODE());
        values.put(DISC_INS_DESC_33KV, data.getDISC_INS_DESC());
        values.put(COUNT_DISC_33KV, data.getCOUNT_DISC());
        values.put(GIPIN_CODE_33KV, data.getGIPIN_CODE());
        values.put(GIPIN_DESC_33KV, data.getGIPIN_DESC());
        values.put(COUNT_GIPIN_33KV, data.getCOUNT_GIPIN());
        values.put(METAL_PARTS_CODE_33KV, data.getMETAL_PARTS_CODE());
        values.put(METAL_PARTS_DESC_33KV, data.getMETAL_PARTS_DESC());
        values.put(COUNT_METAL_PARTS_33KV, data.getCOUNT_METALS_PARTS());
        values.put(BACK_CLAMPS_CODE_33KV, data.getBACK_CLAMPS_CODE());
        values.put(BACK_CLAMPS_DESC_33KV, data.getBACK_CLAMPS_DESC());
        values.put(COUNT_BACK_CLAMPS_33KV, data.getCOUNT_BACK_CLAMPS());
        values.put(STAYCLAMP_11M_DESC_33KV, data.getSTAYCLAMP_11M_DESC());
        values.put(STAYCLAMP_11M_CODE_33KV, data.getSTAYCLAMP_11M_CODE());
        values.put(COUNT__STAYCLAMP_11M_33KV, data.getCOUNT__STAYCLAMP_11M());
        values.put(STAYCLAMP_9M_DESC_33KV, data.getSTAYCLAMP_9M_DESC());
        values.put(STAYCLAMP_9M_CODE_33KV, data.getSTAYCLAMP_9M_CODE());
        values.put(COUNT__STAYCLAMP_9M_33KV, data.getCOUNT__STAYCLAMP_9M());
        values.put(STAYCLAMP_8M_DESC_33KV, data.getSTAYCLAMP_8M_DESC());
        values.put(STAYCLAMP_8M_CODE_33KV, data.getSTAYCLAMP_8M_CODE());
        values.put(COUNT__STAYCLAMP_8M_33KV, data.getCOUNT__STAYCLAMP_8M());
        values.put(STAYCLAMP_SPUN_DESC_33KV, data.getSTAYCLAMP_SPUN_DESC());
        values.put(STAYCLAMP_SPUN_CODE_33KV, data.getSTAYCLAMP_SPUN_CODE());
        values.put(COUNT__STAYCLAMP_SPUN_33KV, data.getCOUNT__STAYCLAMP_SPUN());
        values.put(PIN_CUT_33KV, data.getPIN_CUT());
        values.put(END_POLE_33KV, data.getEND_POLE());
        values.put(EXT_LOCATION_33KV, data.getEXT_LOCATION());

        values.put(INTERMEDIATE_POLE_33KV, data.getINTERMEDIATE_POLE());
        values.put(OTHER_FEEDER_33KV, data.getOTHER_FEEDER());
        values.put(CROSS_6_3KV_ID_33KV, data.getCROSS_6_3KV_ID());
        values.put(CROSSING_TYPE_33KV, data.getCROSSING_TYPE());
        values.put(CROSS_33KV_ID_33KV, data.getCROSS_33KV_ID());
        values.put(CROSS_LT_CKT_33KV, data.getCROSS_LT_CKT());
        values.put(CROSS_COMMUN_33KV, data.getCROSS_COMMUN());
        values.put(CROSS_EHT_33KV, data.getCROSS_EHT());
        values.put(CROSS_REMARKS_33KV, data.getCROSS_REMARKS());
        values.put(CROSS_11KV_ID_33kv, data.getCROSS_11KV_ID());
        values.put(REMARKS_33KV, data.getREMARKS());
        values.put(POLE_PAINT_CODE_33KV, data.getPOLE_PAINT());
        values.put(REG_NUM_33KV, data.getREG_NUM());
        values.put(CTPT_33KV, data.getCTPT());
        values.put(NEW_MAPPED_33KV, data.getNEW_MAPPED());
        values.put(E_POLE_33KV, data.getE_POLE());
        values.put(FDR_LOC_NO_33KV, data.getFDR_LOC_NO_NUMBER());
        values.put(FDR_SOURCE_NO_33KV, data.getFDR_SOURCE_NO_NUMBER());


        values.put(POLE_CONFIG_33KV, data.getPOLE_CONFIG());
        values.put(REF_ID_33KV, data.getREF_ID());
        values.put(LAT_33KV, data.getLATITUDE());
        values.put(LON_33KV, data.getLONGITUDE());
        values.put(FLAG_33KV, "N");

        long a = db.insert(SURVEY_TABLE_33KV, null, values);
        db.close();
        if (a == -1) {
            return false;
        } else {
            return true;
        }

    }

    public ArrayList<Estimates33KVModel> get33KVSurveyData(String est_no) {
        ArrayList<Estimates33KVModel> data = new ArrayList<>();
        String query = "SELECT * FROM SURVEY_TABLE_33KV WHERE EST_NO_33KV = '" + est_no + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    Estimates33KVModel model = new Estimates33KVModel();
                    model.setEST_NO(cursor.getString(0));
                    model.setFROM_POLE(cursor.getString(1));
                    model.setPOLE_NO(cursor.getString(2));
                    model.setLINE_TYPE(cursor.getString(3));
                    model.setPOLE_SURVEY_TYPE(cursor.getString(4));
                    model.setIS_MULTI_CKT(cursor.getString(5));
                    model.setMULTI_CKT_33(cursor.getString(6));
                    model.setMULTI_CKT_11(cursor.getString(7));
                    model.setMULTI_CKT_LT(cursor.getString(8));
                    model.setPOLE_TYPE(cursor.getString(9));
                    model.setPOLE_SUPPORT(cursor.getString(10));
                    model.setPOLE_CONFIG(cursor.getString(11));
                    model.setREF_ID(cursor.getString(12));
                    model.setLATITUDE(cursor.getString(13));
                    model.setLONGITUDE(cursor.getString(14));
                    model.setNo_OF_STRUT(cursor.getString(15));
                    model.setNo_OF_STAY(cursor.getString(16));
                    model.setEXI_POLE_NO(cursor.getString(17));
                    model.setPOLE_LOCATION_TYPE(cursor.getString(18));
                    model.setPOLE_HEIGHT(cursor.getString(19));
                    model.setCONDUTCOR_SIZE(cursor.getString(20));
                    model.setLINE_SURVEY_TYPE(cursor.getString(21));
                    model.setDTR_PHASE(cursor.getString(22));
                    model.setDTR_CAPACITY(cursor.getString(23));
                    model.setDTR_MOUNTING(cursor.getString(24));
                    model.setDTR_SURVEY_TYPE(cursor.getString(25));
                    model.setLOCATION_ID(cursor.getString(26));
                    model.setNUMB_9M_STRUTS(cursor.getInt(27));
                    model.setNUMB_8M_STRUTS(cursor.getInt(28));
                    model.setNUMB_STAYS(cursor.getInt(29));
                    model.setNUMBERS_OF_FEEDERS(cursor.getString(30));
                    model.setDTR_STRUCTURE_CODE(cursor.getString(31));
                    model.setMATERIALPART1(cursor.getString(32));
                    model.setPOLE_SAP_CODE(cursor.getString(33));
                    model.setCONDUCTOR_DESC(cursor.getString(34));
                    model.setCONDUCTOR_SAP_CODE(cursor.getString(35));
                    model.setAB_SWITCH(cursor.getString(36));
                    model.setAB_DESC(cursor.getString(37));
                    model.setSS_ID(cursor.getString(38));
                    model.setSS_NAME(cursor.getString(39));
                    model.setSS_TYPE(cursor.getString(40));
                    model.setFEEDER_ID(cursor.getString(41));
                    model.setCOUNT_TAPPING_XARM(cursor.getInt(42));
                    model.setCOUNT_V_XARM(cursor.getInt(43));
                    model.setCOUNT_TOP_CLEAT(cursor.getInt(44));
                    model.setCOUNT_PIN_INS(cursor.getInt(45));
                    model.setCOUNT_DISC_INS(cursor.getInt(46));
                    model.setCOUNT_SIDE_ARM(cursor.getInt(47));
                    model.setCOUNT_METALS_PARTS(cursor.getInt(48));
                    model.setTAPPINGXARM_CODE(cursor.getString(49));
                    model.setTAPPINGXARM_DESC(cursor.getString(50));
                    model.setVXARM_CODE(cursor.getString(51));
                    model.setVXARM_DESC(cursor.getString(52));
                    model.setSIDEARM_CODE(cursor.getString(53));
                    model.setSIDEARM_DESC(cursor.getString(54));
                    model.setCHANNEL_CODE(cursor.getString(55));
                    model.setCHANNEL_DESC(cursor.getString(56));
                    model.setTOPCLEAT_CODE(cursor.getString(57));
                    model.setTOPCLEAT_DESC(cursor.getString(58));
                    model.setPIN_INS_CODE(cursor.getString(59));
                    model.setPIN_INS_DESC(cursor.getString(60));
                    model.setCOUNT_PIN(cursor.getInt(61));
                    model.setDISC_INS_CODE(cursor.getString(62));
                    model.setDISC_INS_DESC(cursor.getString(63));
                    model.setCOUNT_DISC(cursor.getInt(64));
                    model.setGIPIN_CODE(cursor.getString(65));
                    model.setGIPIN_DESC(cursor.getString(66));
                    model.setCOUNT_GIPIN(cursor.getInt(67));
                    model.setMETAL_PARTS_CODE(cursor.getString(68));
                    model.setMETAL_PARTS_DESC(cursor.getString(69));
                    model.setCOUNT_METAL_PARTS(cursor.getInt(70));
                    model.setBACK_CLAMPS_CODE(cursor.getString(71));
                    model.setBACK_CLAMPS_DESC(cursor.getString(72));
                    model.setCOUNT_BACK_CLAMPS(cursor.getInt(73));
                    model.setCOUNT_CHANNEL(cursor.getInt(74));
                    model.setSTAYCLAMP_9M_DESC(cursor.getString(75));
                    model.setSTAYCLAMP_9M_CODE(cursor.getString(76));
                    model.setCOUNT__STAYCLAMP_9M(cursor.getInt(77));
                    model.setSTAYCLAMP_11M_CODE(cursor.getString(78));
                    model.setSTAYCLAMP_11M_DESC(cursor.getString(79));
                    model.setSTAYCLAMP_SPUN_DESC(cursor.getString(80));
                    model.setSTAYCLAMP_SPUN_CODE(cursor.getString(81));
                    model.setCOUNT__STAYCLAMP_SPUN(cursor.getInt(82));
                    model.setPIN_CUT(cursor.getString(83));
                    model.setEND_POLE(cursor.getString(84));
                    model.setEXT_LOCATION(cursor.getString(85));
                    model.setINTERMEDIATE_POLE(cursor.getString(86));
                    model.setOTHER_FEEDER(cursor.getString(87));
                    model.setCROSS_6_3KV_ID(cursor.getString(88));
                    model.setCROSSING_TYPE(cursor.getString(89));
                    model.setCROSS_33KV_ID(cursor.getString(90));
                    model.setCROSS_LT_CKT(cursor.getString(91));
                    model.setCROSS_COMMUN(cursor.getString(92));
                    model.setCROSS_EHT(cursor.getString(93));
                    model.setCROSS_REMARKS(cursor.getString(94));
                    model.setCROSS_11KV_ID(cursor.getString(95));
                    model.setREMARKS(cursor.getString(96));
                    model.setPOLE_PAINT(cursor.getString(97));
                    model.setREG_NUM(cursor.getString(98));
                    model.setCTPT(cursor.getString(99));
                    model.setNEW_MAPPED(cursor.getString(100));
                    model.setE_POLE(cursor.getString(101));
                    model.setFDR_LOC_NO_NUMBER(cursor.getString(102));
                    model.setFDR_SOURCE_NO_NUMBER(cursor.getString(103));


                    data.add(model);
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return data;
    }

    public boolean add11KVSurveyData(Estimates11KVModel data) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EST_NO_11KV, data.getEST_NO());
        values.put(FROM_POLE_11KV, data.getFROM_POLE());
        values.put(POLE_NO_11KV, data.getPOLE_NO());
        values.put(LINE_TYPE_11KV, data.getLINE_TYPE());
        values.put(POLE_SURVEY_TYPE_11KV, data.getPOLE_SURVEY_TYPE());
        values.put(IS_MULTI_CKT_11KV, data.getIS_MULTI_CKT());
        values.put(MULTI_CKT_33_11KV, data.getMULTI_CKT_33());
        values.put(MULTI_CKT_11_11KV, data.getMULTI_CKT_11());
        values.put(MULTI_CKT_LT_11KV, data.getMULTI_CKT_LT());
        values.put(POLE_TYPE_11KV, data.getPOLE_TYPE());
        values.put(POLE_SUPPORT_11KV, data.getPOLE_SUPPORT());
        values.put(No_OF_STAY_11KV, data.getNo_OF_STAY());
        values.put(No_OF_STRUT_11KV, data.getNo_OF_STRUT());
        values.put(EXI_POLE_NO_11KV, data.getEXI_POLE_NO());
        values.put(POLE_LOCATION_TYPE_11KV, data.getPOLE_LOCATION_TYPE());
        values.put(POLE_HEIGHT_11KV, data.getPOLE_HEIGHT());
        values.put(CONDUTCOR_SIZE_11KV, data.getCONDUTCOR_SIZE());
        values.put(LINE_SURVEY_TYPE_11KV, data.getLINE_SURVEY_TYPE());
        values.put(DTR_PHASE_11KV, data.getDTR_PHASE());
        values.put(DTR_CAPACITY_11KV, data.getDTR_CAPACITY());
        values.put(DTR_MOUNTING_11KV, data.getDTR_MOUNTING());
        values.put(DTR_SURVEY_TYPE_11KV, data.getDTR_SURVEY_TYPE());
        values.put(LOCATION_ID_11KV, data.getLOCATION_ID());
        values.put(POLE_CONFIG_11KV, data.getPOLE_CONFIG());
        values.put(REF_ID_11KV, data.getREF_ID());
        values.put(LAT_11KV, data.getLATITUDE());
        values.put(LON_11KV, data.getLONGITUDE());
        values.put(IS_LT_CKT_AVAILABLE, data.getIS_LT_CKT_AVAILABLE());
        values.put(NO_OF_LT_CKT, data.getNO_OF_LT_CKT());


        values.put(NUMB_9M_STRUTS_11KV, data.getNUMB_9M_STRUTS());
        values.put(NUMB_8M_STRUTS_11KV, data.getNUMB_8M_STRUTS());
        values.put(NUMB_STAYS_11KV, data.getNUMB_STAYS());
        values.put(NUMBERS_11KV_FEEDERS, data.getNUMBERS_OF_FEEDERS());
        values.put(DTR_STRUCTURE_CODE_11KV, data.getDTR_STRUCTURE_CODE());
        values.put(POLE_DESC_11KV, data.getMATERIALPART1());
        values.put(POLE_SAP_CODE_11KV, data.getPOLE_SAP_CODE());
        values.put(CONDUCTOR_DESC_11KV, data.getCONDUCTOR_DESC());
        values.put(CONDUCTOR_SAP_CODE_11KV, data.getCONDUCTOR_SAP_CODE());
        values.put(DTR_DESC_11KV, data.getDTR_DESC());
        values.put(DTR_SAP_CODE_11KV, data.getDTR_SAP_CODE());
        values.put(AB_SWITCH_11KV, data.getAB_SWITCH());
        values.put(AB_DESC_11KV, data.getAB_DESC());
        values.put(SS_ID_11KV, data.getSS_ID());
        values.put(SS_NAME_11KV, data.getSS_NAME());
        values.put(SS_TYPE_11KV, data.getSS_TYPE());
        values.put(FEEDER_ID_11KV, data.getFEEDER_ID());
        values.put(FEEDER_NAME_11KV, data.getFEEDER_NAME());
        values.put(DTR_AB_DESC_11KV, data.getDTR_AB_DESC());
        values.put(DTR_AB_CODE_11KV, data.getDTR_AB_CODE());
        values.put(HG_FUSE_DESC_11KV, data.getHG_FUSE_DESC());
        values.put(HG_FUSE_CODE_11KV, data.getHG_FUSE_CODE());
        values.put(LT_FUSE_DESC_11KV, data.getLT_FUSE_DESC());
        values.put(LT_FUSE_CODE_11KV, data.getLT_FUSE_CODE());
        values.put(CI_SAP_CODE_11KV, data.getCI_SAP_CODE());
        values.put(CI_DESC_11KV, data.getCI_DESC());
        values.put(GI_SAP_CODE_11KV, data.getGI_SAP_CODE());
        values.put(GI_DESC_11KV, data.getGI_DESC());
        values.put(COUNT_CI_11KV, data.getCOUNT_CI());
        values.put(COUNT_GI_11KV, data.getCOUNT_GI());
        values.put(COUNT_TAPPING_XARM_11KV, data.getCOUNT_TAPPING_XARM());
        values.put(COUNT_V_XARM_11KV, data.getCOUNT_V_XARM());
        values.put(COUNT_TOP_CLEAT_11KV, data.getCOUNT_TOP_CLEAT());
        values.put(COUNT_PIN_INS_11KV, data.getCOUNT_PIN_INS());
        values.put(COUNT_DISC_INS_11KV, data.getCOUNT_DISC_INS());
        values.put(COUNT_SIDE_ARM_11KV, data.getCOUNT_SIDE_ARM());
        values.put(COUNT_METALS_PARTS_11KV, data.getCOUNT_METALS_PARTS());

        values.put(TAPPINGXARM_CODE_11KV, data.getTAPPINGXARM_CODE());
        values.put(TAPPINGXARM_DESC_11KV, data.getTAPPINGXARM_DESC());
        values.put(VXARM_CODE_11KV, data.getVXARM_CODE());
        values.put(VXARM_DESC_11KV, data.getVXARM_DESC());
        values.put(SIDEARM_CODE_11KV, data.getSIDEARM_CODE());
        values.put(SIDEARM_DESC_11KV, data.getSIDEARM_DESC());
        values.put(CHANNEL_CODE_11KV, data.getCHANNEL_CODE());
        values.put(CHANNEL_DESC_11KV, data.getCHANNEL_DESC());
        values.put(TOPCLEAT_CODE_11KV, data.getTOPCLEAT_CODE());
        values.put(TOPCLEAT_DESC_11KV, data.getTOPCLEAT_DESC());
        values.put(PIN_INS_CODE_11KV, data.getPIN_INS_CODE());
        values.put(PIN_INS_DESC_11KV, data.getPIN_INS_DESC());
        values.put(PIN_INS_DESC_11KV, data.getPIN_INS_DESC());
        values.put(COUNT_PIN_11KV, data.getCOUNT_PIN());
        values.put(DISC_INS_DESC_11KV, data.getDISC_INS_DESC());
        values.put(COUNT_DISC_11KV, data.getCOUNT_DISC());
        values.put(GIPIN_CODE_11KV, data.getGIPIN_CODE());
        values.put(GIPIN_DESC_11KV, data.getGIPIN_DESC());
        values.put(COUNT_GIPIN_11KV, data.getCOUNT_GIPIN());
        values.put(METAL_PARTS_CODE_11KV, data.getMETAL_PARTS_CODE());
        values.put(METAL_PARTS_DESC_11KV, data.getMETAL_PARTS_DESC());
        values.put(COUNT_METAL_PARTS_11KV, data.getCOUNT_METALS_PARTS());
        values.put(BACK_CLAMPS_CODE_11KV, data.getBACK_CLAMPS_CODE());
        values.put(BACK_CLAMPS_DESC_11KV, data.getBACK_CLAMPS_DESC());
        values.put(COUNT_BACK_CLAMPS_11KV, data.getCOUNT_BACK_CLAMPS());
        values.put(STAY_CLAMPS_CODE_11KV, data.getSTAY_CLAMPS_CODE());
        values.put(STAY_CLAMPS_DESC_11KV, data.getSTAY_CLAMPS_CODE());
        values.put(COUNT_STAY_CLAMPS_11KV, data.getCOUNT_STAY_CLAMPS());
        values.put(STAY_SETS_CODE_11KV, data.getSTAY_SETS_CODE());
        values.put(STAY_SETS_DESC_11KV, data.getSTAY_SETS_DESC());
        values.put(COUNT_STAY_SETS_11KV, data.getCOUNT_STAY_SETS());
        values.put(COUNT_CHANNEL_11KV, data.getCOUNT_CHANNEL());

        values.put(STAYCLAMP_11M_DESC_11KV, data.getSTAYCLAMP_11M_DESC());
        values.put(STAYCLAMP_11M_CODE_11KV, data.getSTAYCLAMP_11M_CODE());
        values.put(COUNT__STAYCLAMP_11M_11KV, data.getCOUNT__STAYCLAMP_11M());
        values.put(STAYCLAMP_9M_DESC_11KV, data.getSTAYCLAMP_9M_DESC());
        values.put(STAYCLAMP_9M_CODE_11KV, data.getSTAYCLAMP_9M_CODE());
        values.put(COUNT__STAYCLAMP_9M_11KV, data.getCOUNT__STAYCLAMP_11M());
        values.put(STAYCLAMP_8M_DESC_11KV, data.getSTAYCLAMP_8M_DESC());
        values.put(STAYCLAMP_8M_CODE_11KV, data.getSTAYCLAMP_8M_CODE());
        values.put(COUNT__STAYCLAMP_8M_11KV, data.getCOUNT__STAYCLAMP_8M());
        values.put(STAYCLAMP_SPUN_DESC_11KV, data.getSTAYCLAMP_SPUN_DESC());
        values.put(STAYCLAMP_SPUN_CODE_11KV, data.getSTAYCLAMP_SPUN_CODE());
        values.put(COUNT__STAYCLAMP_SPUN_11KV, data.getCOUNT__STAYCLAMP_SPUN());
        values.put(DTR_MOUNTING_DESC_11KV, data.getDTR_MOUNTING_DESC());
        values.put(DTR_MOUNTING_CODE_11KV, data.getDTR_MOUNTING_CODE());
        values.put(AB_MOUNTING_DESC_11KV, data.getAB_MOUNTING_DESC());
        values.put(AB_MOUNTING_CODE_11KV, data.getAB_MOUNTING_CODE());
        values.put(PIN_CUT_11KV, data.getPIN_CUT());
        values.put(END_POLE_11KV, data.getEND_POLE());
        values.put(EXT_LOCATION_11KV, data.getEXT_LOCATION());
        values.put(LT_AVAILABLE, data.getLT_AVAILABLE());
        values.put(COUNT_CKT, data.getCOUNT_CKT());
        values.put(LOAD_TYPE, data.getLOAD_TYPE());
        values.put(INTERMEDIATE_POLE_11KV, data.getINTERMEDIATE_POLE());
        values.put(OTHER_FEEDER_11KV, data.getOTHER_FEEDER());
        values.put(CROSS_6_3KV_ID_11KV, data.getCROSS_6_3KV_ID());
        values.put(CROSSING_TYPE_11KV, data.getCROSSING_TYPE());
        values.put(CROSS_33KV_ID_11KV, data.getCROSS_33KV_ID());
        values.put(CROSS_LT_CKT_11KV, data.getCROSS_LT_CKT());
        values.put(CROSS_COMMUN_11KV, data.getCROSS_COMMUN());
        values.put(CROSS_EHT_11KV, data.getCROSS_EHT());
        values.put(CROSS_REMARKS_11KV, data.getCROSS_REMARKS());
        values.put(CROSS_11KV_ID_11kv, data.getCROSS_11KV_ID());
        values.put(REMARKS_11KV, data.getREMARKS());
        values.put(POLE_PAINT_CODE_11KV, data.getPOLE_PAINT());
        values.put(DTR_SNO_11KV, data.getDTR_SNO());
        values.put(DTR_MAKE_11KV, data.getDTR_MAKE());
        values.put(DTR_LOCATION_11KV, data.getDTR_LOCATION());
        values.put(EDTR_PHASE_11KV, data.getEDTR_PHASE());
        values.put(EDTR_CAPACITY_11KV, data.getEDTR_CAPACITY());
        values.put(EDTR_EDITED_11KV, data.getEDTR_EDITED());
        values.put(SERVICE_TYPE_11KV, data.getSERVICE_TYPE());
        values.put(DTR_SHIFTED_11KV, data.getDTR_SHIFTED());
        values.put(REG_NUM_11KV, data.getREG_NUM());
        values.put(DTR_ID_11KV, data.getDTR_ID());
        values.put(DT_SHIFT_REMARKS_11KV, data.getDT_SHIFT_REMARKS());
        values.put(CTPT_11KV, data.getCTPT_11KV());
        values.put(NEW_MAPPED_11KV, data.getNEW_MAPPED());
        values.put(E_POLE_11KV, data.getE_POLE());
        values.put(FDR_LOC_NO_11KV, data.getFDR_LOC_NO_NUMBER());
        values.put(FDR_SOURCE_NO_11KV, data.getFDR_SOURCE_NO_NUMBER());
        values.put(FLAG_11KV, "N");

        long a = db.insert(SURVEY_TABLE_11KV, null, values);
        db.close();
        if (a == -1) {
            return false;
        } else {
            return true;
        }

    }

    public ArrayList<Estimates11KVModel> get11KVSurveyData(String est_no) {
        ArrayList<Estimates11KVModel> data = new ArrayList<>();
        String query = "SELECT * FROM SURVEY_TABLE_11KV WHERE EST_NO_11KV = '" + est_no + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    Estimates11KVModel model = new Estimates11KVModel();
                    model.setEST_NO(cursor.getString(0));
                    model.setFROM_POLE(cursor.getString(1));
                    model.setPOLE_NO(cursor.getString(2));
                    model.setLINE_TYPE(cursor.getString(3));
                    model.setPOLE_SURVEY_TYPE(cursor.getString(4));
                    model.setIS_MULTI_CKT(cursor.getString(5));
                    model.setMULTI_CKT_33(cursor.getString(6));
                    model.setMULTI_CKT_11(cursor.getString(7));
                    model.setMULTI_CKT_LT(cursor.getString(8));
                    model.setPOLE_TYPE(cursor.getString(9));
                    model.setPOLE_SUPPORT(cursor.getString(10));
                    model.setPOLE_CONFIG(cursor.getString(11));
                    model.setREF_ID(cursor.getString(12));
                    model.setLATITUDE(cursor.getString(13));
                    model.setLONGITUDE(cursor.getString(14));
                    model.setIS_LT_CKT_AVAILABLE(cursor.getString(15));
                    model.setNO_OF_LT_CKT(cursor.getString(16));
                    model.setNo_OF_STRUT(cursor.getString(17));
                    model.setNo_OF_STAY(cursor.getString(18));
                    model.setEXI_POLE_NO(cursor.getString(19));
                    model.setPOLE_LOCATION_TYPE(cursor.getString(20));
                    model.setPOLE_HEIGHT(cursor.getString(21));
                    model.setCONDUTCOR_SIZE(cursor.getString(22));
                    model.setLINE_SURVEY_TYPE(cursor.getString(23));
                    model.setDTR_PHASE(cursor.getString(24));
                    model.setDTR_CAPACITY(cursor.getString(25));
                    model.setDTR_MOUNTING(cursor.getString(26));
                    model.setDTR_SURVEY_TYPE(cursor.getString(27));
                    model.setLOCATION_ID(cursor.getString(28));
                    model.setNUMB_9M_STRUTS(cursor.getInt(29));
                    model.setNUMB_8M_STRUTS(cursor.getInt(30));
                    model.setNUMB_STAYS(cursor.getInt(31));
                    model.setNUMBERS_OF_FEEDERS(cursor.getString(32));
                    model.setDTR_STRUCTURE_CODE(cursor.getString(33));
                    model.setMATERIALPART1(cursor.getString(34));
                    model.setPOLE_SAP_CODE(cursor.getString(35));
                    model.setCONDUCTOR_DESC(cursor.getString(35));
                    model.setCONDUCTOR_SAP_CODE(cursor.getString(36));
                    model.setDTR_DESC(cursor.getString(37));
                    model.setDTR_SAP_CODE(cursor.getString(38));
                    model.setAB_SWITCH(cursor.getString(39));
                    model.setAB_DESC(cursor.getString(40));
                    model.setSS_ID(cursor.getString(41));
                    model.setSS_NAME(cursor.getString(42));
                    model.setSS_TYPE(cursor.getString(43));
                    model.setFEEDER_ID(cursor.getString(44));
                    model.setFEEDER_NAME(cursor.getString(45));
                    model.setDTR_AB_DESC(cursor.getString(46));
                    model.setDTR_AB_CODE(cursor.getString(47));
                    model.setHG_FUSE_DESC(cursor.getString(48));
                    model.setHG_FUSE_CODE(cursor.getString(49));
                    model.setLT_FUSE_DESC(cursor.getString(50));
                    model.setLT_FUSE_CODE(cursor.getString(51));
                    model.setCI_SAP_CODE(cursor.getString(52));
                    model.setCI_DESC(cursor.getString(53));
                    model.setGI_SAP_CODE(cursor.getString(54));
                    model.setGI_DESC(cursor.getString(55));
                    model.setCOUNT_CI(cursor.getInt(56));
                    model.setCOUNT_GI(cursor.getInt(57));
                    model.setCOUNT_TAPPING_XARM(cursor.getInt(58));
                    model.setCOUNT_V_XARM(cursor.getInt(59));
                    model.setCOUNT_TOP_CLEAT(cursor.getInt(60));
                    model.setCOUNT_PIN_INS(cursor.getInt(61));
                    model.setCOUNT_DISC_INS(cursor.getInt(62));
                    model.setCOUNT_SIDE_ARM(cursor.getInt(63));
                    model.setCOUNT_METALS_PARTS(cursor.getInt(64));
                    model.setTAPPINGXARM_CODE(cursor.getString(65));
                    model.setTAPPINGXARM_DESC(cursor.getString(66));
                    model.setVXARM_CODE(cursor.getString(67));
                    model.setVXARM_DESC(cursor.getString(68));
                    model.setSIDEARM_CODE(cursor.getString(69));
                    model.setSIDEARM_DESC(cursor.getString(70));
                    model.setCHANNEL_CODE(cursor.getString(71));
                    model.setCHANNEL_DESC(cursor.getString(72));
                    model.setTOPCLEAT_CODE(cursor.getString(73));
                    model.setTOPCLEAT_DESC(cursor.getString(74));
                    model.setPIN_INS_CODE(cursor.getString(75));
                    model.setPIN_INS_DESC(cursor.getString(76));
                    model.setCOUNT_PIN(cursor.getInt(77));
                    model.setDISC_INS_CODE(cursor.getString(78));
                    model.setDISC_INS_DESC(cursor.getString(79));
                    model.setCOUNT_DISC(cursor.getInt(80));
                    model.setGIPIN_CODE(cursor.getString(81));
                    model.setGIPIN_DESC(cursor.getString(82));
                    model.setCOUNT_GIPIN(cursor.getInt(83));
                    model.setMETAL_PARTS_CODE(cursor.getString(84));
                    model.setMETAL_PARTS_DESC(cursor.getString(85));
                    model.setCOUNT_METAL_PARTS(cursor.getInt(86));
                    model.setBACK_CLAMPS_CODE(cursor.getString(87));
                    model.setBACK_CLAMPS_DESC(cursor.getString(88));
                    model.setCOUNT_BACK_CLAMPS(cursor.getInt(89));
                    model.setSTAY_CLAMPS_CODE(cursor.getString(90));
                    model.setSTAY_CLAMPS_DESC(cursor.getString(91));
                    model.setCOUNT_STAY_CLAMPS(cursor.getInt(92));
                    model.setSTAY_SETS_CODE(cursor.getString(93));
                    model.setSTAY_SETS_DESC(cursor.getString(94));
                    model.setCOUNT_STAY_SETS(cursor.getInt(95));
                    model.setCOUNT_CHANNEL(cursor.getInt(96));
                    model.setSTAYCLAMP_11M_DESC(cursor.getString(97));
                    model.setSTAYCLAMP_11M_CODE(cursor.getString(98));
                    model.setSTAYCLAMP_9M_DESC(cursor.getString(99));
                    model.setSTAYCLAMP_9M_CODE(cursor.getString(100));
                    model.setCOUNT__STAYCLAMP_9M(cursor.getInt(101));
                    model.setSTAYCLAMP_8M_DESC(cursor.getString(102));
                    model.setSTAYCLAMP_8M_CODE(cursor.getString(103));
                    model.setCOUNT__STAYCLAMP_8M(cursor.getInt(104));
                    model.setSTAYCLAMP_SPUN_DESC(cursor.getString(105));
                    model.setSTAYCLAMP_SPUN_CODE(cursor.getString(106));
                    model.setCOUNT__STAYCLAMP_SPUN(cursor.getInt(107));
                    model.setCOUNT__STAYCLAMP_11M(cursor.getInt(108));
                    model.setDTR_MOUNTING_DESC(cursor.getString(109));
                    model.setDTR_MOUNTING_CODE(cursor.getString(110));
                    model.setAB_MOUNTING_DESC(cursor.getString(111));
                    model.setAB_MOUNTING_CODE(cursor.getString(112));
                    model.setPIN_CUT(cursor.getString(113));
                    model.setEND_POLE(cursor.getString(114));
                    model.setEXT_LOCATION(cursor.getString(115));
                    model.setLT_AVAILABLE(cursor.getString(116));
                    model.setCOUNT_CKT(cursor.getInt(117));
                    model.setLOAD_TYPE(cursor.getString(118));
                    model.setINTERMEDIATE_POLE(cursor.getString(119));
                    model.setOTHER_FEEDER(cursor.getString(120));
                    model.setCROSS_6_3KV_ID(cursor.getString(121));
                    model.setCROSSING_TYPE(cursor.getString(122));
                    model.setCROSS_33KV_ID(cursor.getString(123));
                    model.setCROSS_LT_CKT(cursor.getString(124));
                    model.setCROSS_COMMUN(cursor.getString(125));
                    model.setCROSS_EHT(cursor.getString(126));
                    model.setCROSS_REMARKS(cursor.getString(127));
                    model.setCROSS_11KV_ID(cursor.getString(128));
                    model.setREMARKS(cursor.getString(130));
                    model.setPOLE_PAINT(cursor.getString(131));
                    model.setDTR_SNO(cursor.getString(132));
                    model.setDTR_MAKE(cursor.getString(133));
                    model.setDTR_LOCATION(cursor.getString(134));
                    model.setEDTR_PHASE(cursor.getString(135));
                    model.setEDTR_CAPACITY(cursor.getString(136));
                    model.setEDTR_EDITED(cursor.getString(137));
                    model.setSERVICE_TYPE(cursor.getString(138));
                    model.setDTR_SHIFTED(cursor.getString(139));
                    model.setREG_NUM(cursor.getString(140));
                    model.setDTR_ID(cursor.getString(141));
                    model.setDT_SHIFT_REMARKS(cursor.getString(142));
                    model.setCTPT_11KV(cursor.getString(143));
                    model.setNEW_MAPPED(cursor.getString(144));
                    model.setE_POLE(cursor.getString(145));
                    model.setFDR_LOC_NO_NUMBER(cursor.getString(146));
                    model.setFDR_SOURCE_NO_NUMBER(cursor.getString(147));


                    data.add(model);
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return data;
    }

    public boolean addDTRSurveyData(DTRModel data) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EST_NO_11KV, data.getEST_NO());
        values.put(POLE_NO_11KV, data.getPOLE_NO());
        values.put(LAT_11KV, data.getLATITUDE());
        values.put(LON_11KV, data.getLONGITUDE());
        values.put(IS_LT_CKT_AVAILABLE, data.getIS_LT_CKT_AVAILABLE());
        values.put(NO_OF_LT_CKT, data.getNO_OF_LT_CKT());
        values.put(FLAG_11KV, data.getSURVEY_STATUS());

        long a = db.insert(DTR_TABLE, null, values);
        db.close();
        if (a == -1) {
            return false;
        } else {
            return true;
        }

    }

    public ArrayList<DTRModel> getDTRSurveyData(String est_no) {
        ArrayList<DTRModel> data = new ArrayList<>();
        String query = "SELECT * FROM DTR_TABLE WHERE EST_NO_11KV = '" + est_no + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    DTRModel model = new DTRModel();
                    model.setEST_NO(cursor.getString(0));
                    model.setPOLE_NO(cursor.getString(1));
                    model.setLATITUDE(cursor.getString(2));
                    model.setLONGITUDE(cursor.getString(3));
                    model.setIS_LT_CKT_AVAILABLE(cursor.getString(4));
                    model.setNO_OF_LT_CKT(cursor.getString(5));
                    model.setSURVEY_STATUS(cursor.getString(6));
                    data.add(model);
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return data;
    }

    public void updateDTRSurveyStatus(String est_no, String dtr_no) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FLAG_11KV, "Y");
        String whereClause = "EST_NO_11KV = ? AND POLE_NO_11KV = ?";
        String[] whereArgs = {est_no, String.valueOf(dtr_no)};
        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.update(DTR_TABLE, contentValues, whereClause, whereArgs);
        // System.out.println("UPDATE RESULT DTR Table : " + result);

    }

    public boolean addLTCKTData(LT_CKT_Model model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EST_NO_11KV, model.getEst());
        values.put(POLE_NO_11KV, model.getPole_umber());
        values.put(LT_CKT_NAME, model.getCkt_name());
        values.put(IS_LT_CKT_SURVEY_COMPLETED, model.getStatus());
        values.put(FLAG_11KV, "N");

        long a = db.insert(LT_CKT_TABLE, null, values);
        db.close();
        if (a == -1) {
            return false;
        } else {
            return true;
        }

    }

    public ArrayList<LT_CKT_Model> getLT_CKT_Data(String est_no, String pole) {
        ArrayList<LT_CKT_Model> data = new ArrayList<>();
        //  String query = "SELECT * FROM LT_CKT_TABLE WHERE EST_NO_11KV = '"+est_no+"'";
        String query = "SELECT * FROM LT_CKT_TABLE WHERE EST_NO_11KV = '" + est_no + "' AND POLE_NO_11KV = '" + pole + "' AND IS_LT_CKT_SURVEY_COMPLETED = 'N'";
        //  System.out.println("LT CKT GET CODE QUERY : " + query);
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    LT_CKT_Model model = new LT_CKT_Model();
                    model.setEst(cursor.getString(0));
                    model.setPole_umber(cursor.getString(1));
                    model.setCkt_name(cursor.getString(2));
                    model.setStatus(cursor.getString(3));
                    data.add(model);
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return data;
    }

    public ArrayList<LT_CKT_Model> getCompletedLT_CKT_Data(String est_no, String pole) {
        ArrayList<LT_CKT_Model> data = new ArrayList<>();
        //  String query = "SELECT * FROM LT_CKT_TABLE WHERE EST_NO_11KV = '"+est_no+"'";
        String query = "SELECT * FROM LT_CKT_TABLE WHERE EST_NO_11KV = '" + est_no + "' AND POLE_NO_11KV = '" + pole + "' AND IS_LT_CKT_SURVEY_COMPLETED = 'Y'";
        //  System.out.println("LT CKT GET CODE Completed QUERY : " + query);
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    LT_CKT_Model model = new LT_CKT_Model();
                    model.setEst(cursor.getString(0));
                    model.setPole_umber(cursor.getString(1));
                    model.setCkt_name(cursor.getString(2));
                    model.setStatus(cursor.getString(3));
                    data.add(model);
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return data;
    }

    public void updateLTCKTSurveyStatus(String est_no, String pole, String lt_ckt) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(IS_LT_CKT_SURVEY_COMPLETED, "Y");
        String whereClause = "EST_NO_11KV = ? AND POLE_NO_11KV = ? AND LT_CKT_NAME = ?";
        String[] whereArgs = {est_no, pole, lt_ckt};
        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.update(LT_CKT_TABLE, contentValues, whereClause, whereArgs);
        // System.out.println("UPDATE RESULT LT_CKT_TABLE Table : " + result);

    }

    public int deletePoleNumber(String poleNumber, String estNo, String surveyType, String gis_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = POLE_NO_NEW + " = ? AND " + GIS_ID_NEW + " = ? AND " + SURVEY_TYPE_NEW + " = ?";
        String[] whereArgs = new String[]{poleNumber, gis_id, surveyType};
        int rowsAffected = db.delete(SURVEY_TABLE_NEW, whereClause, whereArgs);
        db.close();
        return rowsAffected;
    }


    public boolean addLTSurveyData(EstimatesLTModel data) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EST_NO_LT, data.getEST_NO());
        values.put(FROM_POLE_LT, data.getFROM_POLE());
        values.put(POLE_NO_LT, data.getPOLE_NO());
        values.put(LINE_TYPE_LT, data.getLINE_TYPE());
        values.put(POLE_SURVEY_TYPE_LT, data.getPOLE_SURVEY_TYPE());
        values.put(IS_MULTI_CKT_LT, data.getIS_MULTI_CKT());
        values.put(MULTI_CKT_33_LT, data.getMULTI_CKT_33());
        values.put(MULTI_CKT_11_LT, data.getMULTI_CKT_11());
        values.put(MULTI_CKT_LT_LT, data.getMULTI_CKT_LT());
        values.put(POLE_TYPE_LT, data.getPOLE_TYPE());
        values.put(POLE_SUPPORT_LT, data.getPOLE_SUPPORT());
        values.put(No_OF_STRUT_LT, data.getNo_OF_STRUT());
        values.put(No_OF_STAY_LT, data.getNo_OF_STAY());
        values.put(EXI_POLE_NO_LT, data.getEXI_POLE_NO());
        values.put(POLE_LOCATION_TYPE_LT, data.getPOLE_LOCATION_TYPE());
        values.put(POLE_HEIGHT_LT, data.getPOLE_HEIGHT());
        values.put(CONDUTCOR_SIZE_LT, data.getCONDUTCOR_SIZE());
        values.put(LINE_SURVEY_TYPE_LT, data.getLINE_SURVEY_TYPE());
        values.put(DTR_PHASE_LT, data.getDTR_PHASE());
        values.put(DTR_CAPACITY_LT, data.getDTR_CAPACITY());
        values.put(DTR_MOUNTING_LT, data.getDTR_MOUNTING());
        values.put(DTR_SURVEY_TYPE_LT, data.getDTR_SURVEY_TYPE());
        values.put(LOCATION_ID_LT, data.getLOCATION_ID());
        values.put(NUMB_9M_STRUTS_LT, data.getNUMB_9M_STRUTS());
        values.put(NUMB_8M_STRUTS_LT, data.getNUMB_8M_STRUTS());
        values.put(NUMB_STAYS_LT, data.getNUMB_STAYS());
        values.put(NUMBERS_LT_FEEDERS, data.getNUMBERS_OF_FEEDERS());
        values.put(DTR_STRUCTURE_CODE_LT, data.getDTR_STRUCTURE_CODE());
        values.put(POLE_DESC_LT, data.getMATERIALPART1());
        values.put(POLE_SAP_CODE_LT, data.getPOLE_SAP_CODE());
        values.put(CONDUCTOR_DESC_LT, data.getCONDUCTOR_DESC());
        values.put(CONDUCTOR_SAP_CODE_LT, data.getCONDUCTOR_SAP_CODE());
        values.put(AB_SWITCH_LT, data.getAB_SWITCH());
        values.put(AB_DESC_LT, data.getAB_DESC());
        values.put(SS_ID_LT, data.getSS_ID());
        values.put(SS_NAME_LT, data.getSS_NAME());
        values.put(SS_TYPE_LT, data.getSS_TYPE());
        values.put(POLE_CONFIG_LT, data.getPOLE_CONFIG());
        values.put(REF_ID_LT, data.getREF_ID());
        values.put(LAT_LT, data.getLATITUDE());
        values.put(LON_LT, data.getLONGITUDE());
        values.put(TAPPINGXARM_CODE_LT, data.getTAPPINGXARM_CODE());
        values.put(TAPPINGXARM_DESC_LT, data.getTAPPINGXARM_DESC());
        values.put(SIDEARM_CODE_LT, data.getSIDEARM_CODE());
        values.put(SIDEARM_DESC_LT, data.getSIDEARM_DESC());
        values.put(PIN_INS_CODE_LT, data.getPIN_INS_CODE());
        values.put(PIN_INS_DESC_LT, data.getPIN_INS_DESC());
        values.put(DISC_INS_CODE_LT, data.getDISC_INS_CODE());
        values.put(DISC_INS_DESC_LT, data.getDISC_INS_DESC());
        values.put(METAL_PARTS_CODE_LT, data.getMETAL_PARTS_CODE());
        values.put(METAL_PARTS_DESC_LT, data.getMETAL_PARTS_DESC());
        values.put(COUNT_PIN_INS_LT, data.getCOUNT_PIN_INS());
        values.put(COUNT_DISC_INS_LT, data.getCOUNT_DISC_INS());
        values.put(COUNT_SIDE_ARM_LT, data.getCOUNT_SIDE_ARM());
        values.put(COUNT_METALS_PARTS_LT, data.getCOUNT_METAL_PARTS());
        values.put(COUNT_TAPPING_XARM_LT, data.getCOUNT_TAPPING_XARM());
        values.put(STAYCLAMP_11M_DESC_LT, data.getSTAYCLAMP_11M_DESC());
        values.put(STAYCLAMP_11M_CODE_LT, data.getSTAYCLAMP_11M_CODE());
        values.put(COUNT__STAYCLAMP_11M_LT, data.getCOUNT__STAYCLAMP_11M());
        values.put(STAYCLAMP_9M_DESC_LT, data.getSTAYCLAMP_9M_DESC());
        values.put(STAYCLAMP_9M_CODE_LT, data.getSTAYCLAMP_9M_CODE());
        values.put(COUNT__STAYCLAMP_9M_LT, data.getCOUNT__STAYCLAMP_9M());
        values.put(STAYCLAMP_8M_DESC_LT, data.getSTAYCLAMP_8M_DESC());
        values.put(STAYCLAMP_8M_CODE_LT, data.getSTAYCLAMP_8M_CODE());
        values.put(COUNT__STAYCLAMP_8M_LT, data.getCOUNT__STAYCLAMP_8M());
        values.put(STAYCLAMP_SPUN_DESC_LT, data.getSTAYCLAMP_SPUN_DESC());
        values.put(STAYCLAMP_SPUN_CODE_LT, data.getSTAYCLAMP_SPUN_CODE());
        values.put(COUNT__STAYCLAMP_SPUN_LT, data.getCOUNT__STAYCLAMP_SPUN());
        values.put(GI_PIN_CODE_LT, data.getGI_PIN_CODE());
        values.put(GI_PIN_DESC_LT, data.getGI_PIN_DESC());
        values.put(COUNT_GI_PIN_LT, data.getCOUNT_GI_PIN());
        values.put(PIN_CUT_LT, data.getPIN_CUT());
        values.put(END_POLE_LT, data.getEND_POLE());
        values.put(NEW_CKT_LT, data.getNEW_CKT());
        values.put(FLAG_LT, "N");

        long a = db.insert(SURVEY_TABLE_LT, null, values);
        db.close();
        if (a == -1) {
            return false;
        } else {
            return true;
        }

    }

    public ArrayList<EstimatesLTModel> getLTSurveyData(String est_no) {
        ArrayList<EstimatesLTModel> data = new ArrayList<>();
        String query = "SELECT * FROM SURVEY_TABLE_LT WHERE EST_NO_LT = '" + est_no + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    EstimatesLTModel model = new EstimatesLTModel();
                    model.setEST_NO(cursor.getString(0));
                    model.setFROM_POLE(cursor.getString(1));
                    model.setPOLE_NO(cursor.getString(2));
                    model.setLINE_TYPE(cursor.getString(3));
                    model.setPOLE_SURVEY_TYPE(cursor.getString(4));
                    model.setIS_MULTI_CKT(cursor.getString(5));
                    model.setMULTI_CKT_33(cursor.getString(6));
                    model.setMULTI_CKT_11(cursor.getString(7));
                    model.setMULTI_CKT_LT(cursor.getString(8));
                    model.setPOLE_TYPE(cursor.getString(9));
                    model.setPOLE_SUPPORT(cursor.getString(10));
                    model.setPOLE_CONFIG(cursor.getString(11));
                    model.setREF_ID(cursor.getString(12));
                    model.setLATITUDE(cursor.getString(13));
                    model.setLONGITUDE(cursor.getString(14));
                    model.setNo_OF_STRUT(cursor.getString(15));
                    model.setNo_OF_STAY(cursor.getString(16));
                    model.setEXI_POLE_NO(cursor.getString(17));
                    model.setPOLE_LOCATION_TYPE(cursor.getString(18));
                    model.setPOLE_HEIGHT(cursor.getString(19));
                    model.setCONDUTCOR_SIZE(cursor.getString(20));
                    model.setLINE_SURVEY_TYPE(cursor.getString(21));
                    model.setDTR_PHASE(cursor.getString(22));
                    model.setDTR_CAPACITY(cursor.getString(23));
                    model.setDTR_MOUNTING(cursor.getString(24));
                    model.setDTR_SURVEY_TYPE(cursor.getString(25));
                    model.setLOCATION_ID(cursor.getString(26));
                    model.setNUMB_9M_STRUTS(cursor.getInt(27));
                    model.setNUMB_8M_STRUTS(cursor.getInt(28));
                    model.setNUMB_STAYS(cursor.getInt(29));
                    model.setNUMBERS_OF_FEEDERS(cursor.getString(30));
                    model.setDTR_STRUCTURE_CODE(cursor.getString(31));
                    model.setMATERIALPART1(cursor.getString(32));
                    model.setPOLE_SAP_CODE(cursor.getString(33));
                    model.setCONDUCTOR_DESC(cursor.getString(34));
                    model.setCONDUCTOR_SAP_CODE(cursor.getString(35));
                    model.setAB_SWITCH(cursor.getString(36));
                    model.setAB_DESC(cursor.getString(37));
                    model.setSS_ID(cursor.getString(38));
                    model.setSS_NAME(cursor.getString(39));
                    model.setSS_TYPE(cursor.getString(40));
                    model.setTAPPINGXARM_CODE(cursor.getString(41));
                    model.setTAPPINGXARM_DESC(cursor.getString(42));
                    model.setSIDEARM_CODE(cursor.getString(43));
                    model.setSIDEARM_DESC(cursor.getString(44));
                    model.setPIN_INS_CODE(cursor.getString(45));
                    model.setPIN_INS_DESC(cursor.getString(46));
                    model.setDISC_INS_CODE(cursor.getString(47));
                    model.setDISC_INS_DESC(cursor.getString(48));
                    model.setMETAL_PARTS_CODE(cursor.getString(49));
                    model.setMETAL_PARTS_DESC(cursor.getString(50));
                    model.setCOUNT_METAL_PARTS(cursor.getInt(51));
                    model.setBACK_CLAMPS_CODE(cursor.getString(52));
                    model.setBACK_CLAMPS_DESC(cursor.getString(53));
                    model.setCOUNT_PIN_INS(cursor.getInt(54));
                    model.setCOUNT_DISC_INS(cursor.getInt(55));
                    model.setCOUNT_SIDE_ARM(cursor.getInt(56));
                    model.setCOUNT_METALS_PARTS(cursor.getInt(57));
                    model.setCOUNT_TAPPING_XARM(cursor.getInt(58));
                    model.setSTAYCLAMP_11M_DESC(cursor.getString(59));
                    model.setSTAYCLAMP_11M_CODE(cursor.getString(60));
                    model.setSTAYCLAMP_9M_DESC(cursor.getString(61));
                    model.setSTAYCLAMP_9M_CODE(cursor.getString(62));
                    model.setCOUNT__STAYCLAMP_9M(cursor.getInt(63));
                    model.setSTAYCLAMP_8M_DESC(cursor.getString(64));
                    model.setSTAYCLAMP_8M_CODE(cursor.getString(65));
                    model.setCOUNT__STAYCLAMP_8M(cursor.getInt(66));
                    model.setSTAYCLAMP_SPUN_DESC(cursor.getString(67));
                    model.setSTAYCLAMP_SPUN_CODE(cursor.getString(68));
                    model.setCOUNT__STAYCLAMP_SPUN(cursor.getInt(69));
                    model.setCOUNT__STAYCLAMP_11M(cursor.getInt(70));
                    model.setGI_PIN_CODE(cursor.getString(71));
                    model.setGI_PIN_DESC(cursor.getString(72));
                    model.setCOUNT_GI_PIN(cursor.getInt(73));
                    model.setPIN_CUT(cursor.getString(74));
                    model.setEND_POLE(cursor.getString(75));
                    model.setEND_POLE(cursor.getString(76));


                    data.add(model);
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return data;
    }

    public boolean addNewSurveyData(NewModel data) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EST_NO_NEW, data.getEST_NO());
        values.put(FROM_POLE_NEW, data.getFROM_POLE());
        values.put(POLE_NO_NEW, data.getPOLE_NO());
        values.put(LINE_TYPE_NEW, data.getLINE_TYPE());
        values.put(POLE_SURVEY_TYPE_NEW, data.getPOLE_SURVEY_TYPE());
        values.put(IS_MULTI_CKT_NEW, data.getIS_MULTI_CKT());
        values.put(MULTI_CKT_33_NEW, data.getMULTI_CKT_33());
        values.put(MULTI_CKT_11_NEW, data.getMULTI_CKT_11());
        values.put(MULTI_CKT_LT_NEW, data.getMULTI_CKT_LT());
        values.put(POLE_TYPE_NEW, data.getPOLE_TYPE());
        values.put(POLE_SUPPORT_NEW, data.getPOLE_SUPPORT());
        values.put(No_OF_STRUT_NEW, data.getNo_OF_STRUT());
        values.put(No_OF_STAY_NEW, data.getNo_OF_STAY());
        values.put(POLE_CONFIG_NEW, data.getPOLE_CONFIG());
        values.put(REF_ID_NEW, data.getREF_ID());
        values.put(LAT_NEW, data.getLATITUDE());
        values.put(LON_NEW, data.getLONGITUDE());
        values.put(SURVEY_TYPE_NEW, data.getVOLTAGE_LEVEL());
        values.put(IS_EXISTING_LOCATION_NEW, data.getIS_EXISTING_LOCATION_NEW());
        values.put(EXISTING_LOCATION_33_NEW, data.getEXISTING_LOCATION_33_NEW());
        values.put(EXISTING_LOCATION_11_NEW, data.getEXISTING_LOCATION_11_NEW());
        values.put(EXISTING_LOCATION_LT_NEW, data.getEXISTING_LOCATION_LT_NEW());
        values.put(TAPPING_LOCATION_NEW, data.getTAPPING_LOCATION());
        values.put(FROM_POLE_LATITUDE_NEW, data.getFROM_POLE_LATITUDE());
        values.put(FROM_POLE_LONGITUDE_NEW, data.getFROM_POLE_LONGITUDE());
        values.put(MULTI_CKT_33_POLE_NO_NEW, data.getMULTI_CKT_33_POLE_NO());
        values.put(MULTI_CKT_11_POLE_NO_NEW, data.getMULTI_CKT_11_POLE_NO());
        values.put(MULTI_CKT_LT_POLE_NO_NEW, data.getMULTI_CKT_LT_POLE_NO());
        values.put(EXI_POLE_NO_NEW, data.getEXI_POLE_NO());
        values.put(POLE_LOCATION_TYPE_NEW, data.getPOLE_LOCATION_TYPE());
        values.put(POLE_HEIGHT_NEW, data.getPOLE_HEIGHT());
        values.put(CONDUTCOR_SIZE_NEW, data.getCONDUTCOR_SIZE());
        values.put(LINE_SURVEY_TYPE_NEW, data.getLINE_SURVEY_TYPE());
        values.put(DTR_PHASE_NEW, data.getDTR_PHASE());
        values.put(DTR_CAPACITY_NEW, data.getDTR_CAPACITY());
        values.put(DTR_MOUNTING_NEW, data.getDTR_MOUNTING());
        values.put(DTR_SURVEY_TYPE_NEW, data.getDTR_SURVEY_TYPE());
        values.put(LOCATION_ID_NEW, data.getLOCATION_ID());
        values.put(NUMB_9M_STRUTS_NEW, data.getNUMB_9M_STRUTS());
        values.put(NUMB_8M_STRUTS_NEW, data.getNUMB_8M_STRUTS());
        values.put(NUMB_STAYS_NEW, data.getNUMB_STAYS());
        values.put(NUMBERS_OF_FEEDERS_NEW, data.getNUMBERS_OF_FEEDERS());
        values.put(DTR_STRUCTURE_CODE_NEW, data.getDTR_STRUCTURE_CODE());
        values.put(POLE_DESC_NEW, data.getPOLE_DESC());
        values.put(POLE_SAP_CODE_NEW, data.getPOLE_SAP_CODE());
        values.put(CONDUCTOR_DESC_NEW, data.getCONDUCTOR_DESC());
        values.put(CONDUCTOR_SAP_CODE_NEW, data.getCONDUCTOR_SAP_CODE());
        values.put(DTR_DESC_NEW, data.getDTR_DESC());
        values.put(DTR_SAP_CODE_NEW, data.getDTR_SAP_CODE());
        values.put(AB_SWITCH_NEW, data.getAB_SWITCH());
        values.put(AB_DESC_NEW, data.getAB_DESC());
        values.put(SS_ID_NEW, data.getSS_ID());
        values.put(SS_NAME_NEW, data.getSS_NAME());
        values.put(SS_TYPE_NEW, data.getSS_TYPE());
        values.put(FEEDER_ID_NEW, data.getFEEDER_ID());
        values.put(FEEDER_NAME_NEW, data.getFEEDER_NAME());
        values.put(DTR_AB_DESC_NEW, data.getDTR_AB_DESC());
        values.put(DTR_AB_CODE_NEW, data.getDTR_AB_CODE());
        values.put(HG_FUSE_DESC_NEW, data.getHG_FUSE_DESC());
        values.put(HG_FUSE_CODE_NEW, data.getHG_FUSE_CODE());
        values.put(LT_FUSE_DESC_NEW, data.getLT_FUSE_DESC());
        values.put(LT_FUSE_CODE_NEW, data.getLT_FUSE_CODE());
        values.put(CI_SAP_CODE_NEW, data.getCI_SAP_CODE());
        values.put(CI_DESC_NEW, data.getCI_DESC());
        values.put(GI_SAP_CODE_NEW, data.getGI_SAP_CODE());
        values.put(GI_DESC_NEW, data.getGI_DESC());
        values.put(COUNT_CI_NEW, data.getCOUNT_CI());
        values.put(COUNT_GI_NEW, data.getCOUNT_GI());
        values.put(COUNT_TAPPING_XARM_NEW, data.getCOUNT_TAPPING_XARM());
        values.put(COUNT_V_XARM_NEW, data.getCOUNT_V_XARM());
        values.put(COUNT_TOP_CLEAT_NEW, data.getCOUNT_TOP_CLEAT());
        values.put(COUNT_PIN_INS_NEW, data.getCOUNT_PIN_INS());
        values.put(COUNT_DISC_INS_NEW, data.getCOUNT_DISC_INS());
        values.put(COUNT_SIDE_ARM_NEW, data.getCOUNT_SIDE_ARM());
        values.put(COUNT_METALS_PARTS_NEW, data.getCOUNT_METALS_PARTS());

        values.put(TAPPINGXARM_CODE_NEW, data.getTAPPINGXARM_CODE());
        values.put(TAPPINGXARM_DESC_NEW, data.getTAPPINGXARM_DESC());
        values.put(VXARM_CODE_NEW, data.getVXARM_CODE());
        values.put(VXARM_DESC_NEW, data.getVXARM_DESC());
        values.put(SIDEARM_CODE_NEW, data.getSIDEARM_CODE());
        values.put(SIDEARM_DESC_NEW, data.getSIDEARM_DESC());
        values.put(CHANNEL_CODE_NEW, data.getCHANNEL_CODE());
        values.put(CHANNEL_DESC_NEW, data.getCHANNEL_DESC());
        values.put(TOPCLEAT_CODE_NEW, data.getTOPCLEAT_CODE());
        values.put(TOPCLEAT_DESC_NEW, data.getTOPCLEAT_DESC());
        values.put(PIN_INS_CODE_NEW, data.getPIN_INS_CODE());
        values.put(PIN_INS_DESC_NEW, data.getPIN_INS_DESC());
        values.put(COUNT_PIN_NEW, data.getCOUNT_PIN());
        values.put(DISC_INS_CODE_NEW, data.getDISC_INS_CODE());
        values.put(DISC_INS_DESC_NEW, data.getDISC_INS_DESC());
        values.put(COUNT_DISC_NEW, data.getCOUNT_DISC());
        values.put(GIPIN_CODE_NEW, data.getGIPIN_CODE());
        values.put(GIPIN_DESC_NEW, data.getGIPIN_DESC());
        values.put(COUNT_GIPIN_NEW, data.getCOUNT_GIPIN());
        values.put(METAL_PARTS_CODE_NEW, data.getMETAL_PARTS_CODE());
        values.put(METAL_PARTS_DESC_NEW, data.getMETAL_PARTS_DESC());
        values.put(COUNT_METAL_PARTS_NEW, data.getCOUNT_METALS_PARTS());
        values.put(COUNT_CHANNEL_NEW, data.getCOUNT_CHANNEL());
        values.put(BACK_CLAMPS_CODE_NEW, data.getBACK_CLAMPS_CODE());
        values.put(BACK_CLAMPS_DESC_NEW, data.getBACK_CLAMPS_DESC());
        values.put(COUNT_BACK_CLAMPS_NEW, data.getCOUNT_BACK_CLAMPS());
        values.put(STAYCLAMP_9M_DESC_NEW, data.getSTAYCLAMP_9M_DESC());
        values.put(STAYCLAMP_9M_CODE_NEW, data.getSTAYCLAMP_9M_CODE());
        values.put(COUNT__STAYCLAMP_9M_NEW, data.getCOUNT__STAYCLAMP_9M());
        values.put(STAYCLAMP_11M_DESC_NEW, data.getSTAYCLAMP_11M_DESC());
        values.put(STAYCLAMP_11M_CODE_NEW, data.getSTAYCLAMP_11M_CODE());
        values.put(COUNT__STAYCLAMP_11M_NEW, data.getCOUNT__STAYCLAMP_11M());
        values.put(STAYCLAMP_8M_DESC_NEW, data.getSTAYCLAMP_8M_DESC());
        values.put(STAYCLAMP_8M_CODE_NEW, data.getSTAYCLAMP_8M_CODE());
        values.put(COUNT__STAYCLAMP_8M_NEW, data.getCOUNT__STAYCLAMP_8M());
        values.put(STAYCLAMP_SPUN_DESC_NEW, data.getSTAYCLAMP_SPUN_DESC());
        values.put(STAYCLAMP_SPUN_CODE_NEW, data.getSTAYCLAMP_SPUN_CODE());
        values.put(COUNT__STAYCLAMP_SPUN_NEW, data.getCOUNT__STAYCLAMP_SPUN());
        values.put(GI_PIN_CODE_NEW, data.getGI_PIN_CODE());
        values.put(GI_PIN_DESC_NEW, data.getGI_PIN_DESC());
        values.put(COUNT_GI_PIN_NEW, data.getCOUNT_GI_PIN());
        values.put(NEW_DATA_ADDED_NEW, data.isNEW_DATA_ADDED());
        values.put(DTR_MOUNTING_DESC_NEW, data.getDTR_MOUNTING_DESC());
        values.put(DTR_MOUNTING_CODE_NEW, data.getDTR_MOUNTING_CODE());
        values.put(AB_MOUNTING_DESC_NEW, data.getAB_MOUNTING_DESC());
        values.put(AB_MOUNTING_CODE_NEW, data.getAB_MOUNTING_CODE());
        values.put(MT_CODE_NEW, data.getMT_CODE());
        values.put(MT_DESC_NEW, data.getMT_DESC());
        values.put(COUNT_MT_NEW, data.getCOUNT_MT());
        values.put(MT_LOC_NO_NEW, data.getMT_LOC_NO());
        values.put(MT_SURVEY_TYPE_NEW, data.getMT_SURVEY_TYPE());
        values.put(PIN_CUT_NEW, data.getPIN_CUT());
        values.put(END_POLE_NEW, data.getEND_POLE());
        values.put(IS_NEW_SURVEY, data.getIS_NEW_SURVEY());
        values.put(SERVICE_POLE, data.getSERVICE_POLE());
        values.put(EXT_LOCATION_NEW, data.getEXT_LOCATION());
        values.put(POLE_CONCRETE, data.getPOLE_CONCRETE());
        values.put(POLE_STRUCTURE, data.getPOLE_STRUCTURE());
        // System.out.println("SURVEY FROM INSERT: " + data.getSURVEY_FROM());
        values.put(SURVEY_FROM, data.getSURVEY_FROM());
        values.put(WORK_ORDER, data.getWORK_ORDER());
        values.put(GIS_ID_NEW, data.getGIS_ID());
        values.put(HTSC_NO_NEW, data.getHTSC_NO());
        values.put(IS_TRANSMITTED_DATA_UPDATED, data.getIS_TRANSMITTED_DATA_UPDATED());
        values.put(IS_TRANSMITTED_LOCATION, data.getIS_TRANSMITTED_LOCATION());
        values.put(REF_VOLTAGE, data.getREF_VOLTAGE());
        values.put(EXT_ID, data.getEXT_ID());
        values.put(PURPOSE, data.getPURPOSE());
        values.put(LT_AVAILABLE_NEW, data.getLT_AVAILABLE());
        values.put(COUNT_CKT_NEW, data.getCOUNT_CKT());
        values.put(LOAD_TYPE_NEW, data.getLOAD_TYPE());
        values.put(INTERMEDIATE_POLE, data.getINTERMEDIATE_POLE());
        values.put(OTHER_FEEDER_NEW, data.getOTHER_FEEDER());
        values.put(CROSS_6_3KV_ID_NEW, data.getCROSS_6_3KV_ID());
        values.put(CROSSING_TYPE_NEW, data.getCROSSING_TYPE());
        values.put(CROSS_33KV_ID_NEW, data.getCROSS_33KV_ID());
        values.put(CROSS_LT_CKT_NEW, data.getCROSS_LT_CKT());
        values.put(CROSS_COMMUN_NEW, data.getCROSS_COMMUN());
        values.put(CROSS_EHT_NEW, data.getCROSS_EHT());
        values.put(CROSS_REMARKS_NEW, data.getCROSS_REMARKS());
        values.put(CROSS_11KV_ID_NEW, data.getCROSS_11KV_ID());
        values.put(REMARKS_NEW, data.getREMARKS());
        values.put(POLE_PAINT_CODE_NEW, data.getPOLE_PAINT());
        values.put(CURRENT_LAT, data.getCURRENT_LAT());
        values.put(CURRENT_LONG, data.getCURRENT_LONG());
        values.put(APP_VERSION, data.getAPP_VERSION());
        values.put(APP_VERSION2, data.getAPP_VERSION2());
        values.put(INTID1, data.getINTID1());
        values.put(INTID2, data.getINTID2());
        values.put(DTR_SNO_NEW, data.getDTR_SNO());
        values.put(DTR_MAKE_NEW, data.getDTR_MAKE());
        values.put(DTR_LOCATION_NEW, data.getDTR_LOCATION());
        values.put(EDTR_PHASE_NEW, data.getEDTR_PHASE());
        values.put(EDTR_CAPACITY_NEW, data.getEDTR_CAPACITY());
        values.put(EDTR_EDITED_NEW, data.getEDTR_EDITED());
        values.put(SERVICE_TYPE_NEW, data.getSERVICE_TYPE());
        values.put(DTR_SHIFTED_NEW, data.getDTR_SHIFTED());
        values.put(NEW_CKT_NEW, data.getNEW_CKT());
        values.put(REG_NUM_NEW, data.getREG_NUM());
        values.put(DTR_ID_NEW, data.getDTR_ID());
        values.put(DT_SHIFT_REMARKS_NEW, data.getDT_SHIFT_REMARKS());
        values.put(SURVEY_MODE_NEW, data.getSURVEY_MODE());
        values.put(CTPT_NEW, data.getCTPT());
        values.put(NEW_MAPPED_NEW, data.getNEW_MAPPED());
        values.put(E_POLE_NEW, data.getE_POLE());
        if ("33KV".equalsIgnoreCase(data.getVOLTAGE_LEVEL())) {
            if (data.getFDR_LOC_NO_NUMBER() != null && !data.getFDR_LOC_NO_NUMBER().isEmpty()) {
                values.put(FDR_LOC_NO_NEW, data.getFDR_LOC_NO_NUMBER());
                int valueA = Integer.parseInt(data.getFDR_LOC_NO_NUMBER());
                int valueB = valueA - 1;
                values.put(FDR_SOURCE_NO_NEW, String.valueOf(valueB));
            }

        }
        if ("11KV".equalsIgnoreCase(data.getVOLTAGE_LEVEL())) {
            if (data.getFDR_LOC_NO_NUMBER() != null && !data.getFDR_LOC_NO_NUMBER().isEmpty()) {
                values.put(FDR_LOC_NO_NEW, data.getFDR_LOC_NO_NUMBER());
                int valueA = Integer.parseInt(data.getFDR_LOC_NO_NUMBER());
                int valueB = valueA - 1;
                values.put(FDR_SOURCE_NO_NEW, String.valueOf(valueB));
            }

        }


        if ("LT".equalsIgnoreCase(data.getVOLTAGE_LEVEL())) {
            if (data.getLTCKT_LOC_NO_NUMBER() != null && !data.getLTCKT_LOC_NO_NUMBER().isEmpty()) {
                values.put(LTCKT_LOC_NO_NEW, data.getLTCKT_LOC_NO_NUMBER());
                int valueC = Integer.parseInt(data.getLTCKT_LOC_NO_NUMBER());
                int valueD = valueC - 1;
                values.put(LTCKT_SOURCE_NO_NEW, String.valueOf(valueD));
            }

        }
        values.put(GPS_ACCURACY_NEW, data.getGPS_ACCURACY());
        values.put(DEVICE_ID_NEW, data.getDEVICE_ID());

        values.put(RDSS_SCHEME_TYPE_NEW, data.getRDSS_SCHEME_TYPE());
        values.put(PACKAGE_NO_NEW, data.getPACKAGE_NO());
        values.put(DT_VOLATAGE_RATIO_NEW, data.getDT_VOLATAGE_RATIO());
        values.put(DT_MANF_DT_NEW, data.getDT_MANF_DT());
        values.put(DT_INSTL_DT_NEW, data.getDT_INSTL_DT());

        values.put(IMAGE_1, data.getIMAGE_1());
        values.put(IMAGE_2, data.getIMAGE_2());
        values.put(IMAGE_3, data.getIMAGE_3());
        values.put(IS_IMAGE_TRANSMITTED, data.getIS_IMAGE_TRANSMITTED());
        values.put(ADDRESS_1, data.getADDRESS_1());
        values.put(ADDRESS_2, data.getADDRESS_2());

        values.put(ASSET_TYPE, data.getASSET_TYPE());
        values.put(BLOCK_PREMISES, data.getBLOCK_PREMISES());
        values.put(VILLAGE, data.getVILLAGE());
        values.put(TOWN_CITY, data.getTOWN_CITY());
        values.put(STATE, data.getSTATE());
        values.put(BRANCH_POINT, data.getBRANCH_POINT());
        values.put(ACTUAL_LENGTH, data.getACTUAL_LENGTH());
        values.put(TARGET_FEEDER_ID, data.getTARGET_FEEDER_ID());
        values.put(TARGET_FEEDER_NAME, data.getTARGET_FEEDER_NAME());
        values.put(REPEAT_POLE, data.getREPEAT_POLE());


        values.put(FLAG_NEW, "N");

        long a = db.insert(SURVEY_TABLE_NEW, null, values);
        db.close();
        if (a == -1) {
            return false;
        } else {
            return true;
        }

    }


    public boolean updateSurveyData(NewModel data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Strings
        if (data.getPOLE_TYPE() != null) values.put(POLE_TYPE_NEW, data.getPOLE_TYPE());
        if (data.getPOLE_HEIGHT() != null) values.put(POLE_HEIGHT_NEW, data.getPOLE_HEIGHT());
        if (data.getPOLE_SUPPORT() != null) values.put(POLE_SUPPORT_NEW, data.getPOLE_SUPPORT());
        if (data.getPIN_CUT() != null) values.put(PIN_CUT_NEW, data.getPIN_CUT());
        if (data.getPOLE_CONCRETE() != null) values.put(POLE_CONCRETE, data.getPOLE_CONCRETE());
        if (data.getPOLE_STRUCTURE() != null) values.put(POLE_STRUCTURE, data.getPOLE_STRUCTURE());
        if (data.getPOLE_DESC() != null) values.put(POLE_DESC_NEW, data.getPOLE_DESC());
        if (data.getVOLTAGE_LEVEL() != null) values.put(SURVEY_TYPE_NEW, data.getVOLTAGE_LEVEL());
        if (data.getFROM_POLE() != null)
            values.put(FROM_POLE_NEW, data.getFROM_POLE()); // optional update

        // Integers (nullable wrappers)
        if (data.getNUMB_STAYS() > 0) values.put(NUMB_STAYS_NEW, data.getNUMB_STAYS());
        if (data.getNUMB_9M_STRUTS() > 0) values.put(NUMB_9M_STRUTS_NEW, data.getNUMB_9M_STRUTS());
        if (data.getNUMB_8M_STRUTS() > 0) values.put(NUMB_8M_STRUTS_NEW, data.getNUMB_8M_STRUTS());

        // 🔹 DTR-related updates (new)
        if (data.getDTR_PHASE() != null) values.put(DTR_PHASE_NEW, data.getDTR_PHASE());
        if (data.getDTR_CAPACITY() != null) values.put(DTR_CAPACITY_NEW, data.getDTR_CAPACITY());
        if (data.getDTR_LOCATION() != null) values.put(DTR_LOCATION_NEW, data.getDTR_LOCATION());
        if (data.getDTR_SNO() != null) values.put(DTR_SNO_NEW, data.getDTR_SNO());
        if (data.getDTR_MAKE() != null) values.put(DTR_MAKE_NEW, data.getDTR_MAKE());
        if (data.getGIS_ID() != null) values.put(GIS_ID_NEW, data.getGIS_ID());
        if (data.getLOCATION_ID() != null) values.put(LOCATION_ID_NEW, data.getLOCATION_ID());
        if (data.getDTR_STRUCTURE_CODE() != null)
            values.put(DTR_STRUCTURE_CODE_NEW, data.getDTR_STRUCTURE_CODE());

        if (data.getSERVICE_TYPE() != null) values.put(SERVICE_TYPE_NEW, data.getSERVICE_TYPE());
        if (data.getCTPT() != null) values.put(CTPT_NEW, data.getCTPT());


        // WHERE clause: identify row
        String whereClause = EST_NO_NEW + "=? AND " + POLE_NO_NEW + "=?";
        String[] whereArgs = new String[]{
                data.getEST_NO(),
                data.getPOLE_NO()
        };

        int rows = db.update(SURVEY_TABLE_NEW, values, whereClause, whereArgs);
        db.close();

        return rows > 0;
    }

    public boolean updateMaterialData(MaterialPostingModel data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Strings
        if (data.getESTIMATE_NO() != null) values.put(MT_ESTIMATE_NO, data.getESTIMATE_NO());
        if (data.getVOLTAGE_LEVEL() != null) values.put(MT_VOLTAGE_LEVEL, data.getVOLTAGE_LEVEL());
        if (data.getLOCATION_ID() != null) values.put(MT_LOCATION_ID, data.getLOCATION_ID());
        if (data.getLOCATION_NO() != null) values.put(MT_LOCATION_NO, data.getLOCATION_NO());
        if (data.getMATERIAL() != null) values.put(MT_MATERIAL, data.getMATERIAL());
        if (data.getSAP_CODE() != null) values.put(MT_SAP_CODE, data.getSAP_CODE());
        if (data.getMATERIAL_DESC() != null) values.put(MT_MATERIAL_DESC, data.getMATERIAL_DESC());
        if (data.getSURVEY_CODE() != null) values.put(MT_SURVEY_CODE, data.getSURVEY_CODE());
        if (data.getDATA_ID() != null) values.put(MT_DATA_ID, data.getDATA_ID());
        if (data.getTYPE() != null) values.put(MT_TYPE, data.getTYPE());
        if (data.getGIS_ID() != null) values.put(GIS_ID, data.getGIS_ID());
        if (data.getAPP_VERSION() != null) values.put(APP_VERSION, data.getAPP_VERSION());
        if (data.getAPP_VERSION2() != null) values.put(APP_VERSION2, data.getAPP_VERSION2());

        if (data.getCOUNT() != null && !data.getCOUNT().trim().isEmpty()) {
            values.put(MT_COUNT, data.getCOUNT());
        }

        // Always mark as updated
        values.put(FLAG, "U");

        int rows = 0;

        // 🔹 Only try update if DATA_ID is non-null and not empty
        if (data.getDATA_ID() != null && !data.getDATA_ID().trim().isEmpty()) {
            rows = db.update(MATERIAL_TABLE, values, MT_DATA_ID + "=?", new String[]{data.getDATA_ID()});
        }

        // 🔹 If update failed (rows==0), insert a new row
        boolean result;
        if (rows == 0) {
            long id = db.insert(MATERIAL_TABLE, null, values);
            if (id != -1) {
                System.out.println("✅ Inserted new material record with DATA_ID=" + data.getDATA_ID());
                result = true;
            } else {
                System.out.println("❌ Failed to insert new material record for DATA_ID=" + data.getDATA_ID());
                result = false;
            }
        } else {
            System.out.println("✅ Updated material record with DATA_ID=" + data.getDATA_ID() + " (rows affected=" + rows + ")");
            result = true;
        }

        db.close();
        return result;
    }


//    public boolean updateMaterialData(MaterialPostingModel data) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//
//        // Strings
//        if (data.getESTIMATE_NO() != null) values.put(MT_ESTIMATE_NO, data.getESTIMATE_NO());
//        if (data.getVOLTAGE_LEVEL() != null) values.put(MT_VOLTAGE_LEVEL, data.getVOLTAGE_LEVEL());
//        if (data.getLOCATION_ID() != null) values.put(MT_LOCATION_ID, data.getLOCATION_ID());
//        if (data.getLOCATION_NO() != null) values.put(MT_LOCATION_NO, data.getLOCATION_NO());
//        if (data.getMATERIAL() != null) values.put(MT_MATERIAL, data.getMATERIAL());
//        if (data.getSAP_CODE() != null) values.put(MT_SAP_CODE, data.getSAP_CODE());
//        if (data.getMATERIAL_DESC() != null) values.put(MT_MATERIAL_DESC, data.getMATERIAL_DESC());
//        if (data.getSURVEY_CODE() != null) values.put(MT_SURVEY_CODE, data.getSURVEY_CODE());
//        if (data.getDATA_ID() != null) values.put(MT_DATA_ID, data.getDATA_ID());
//        if (data.getTYPE() != null) values.put(MT_TYPE, data.getTYPE());
//        if (data.getGIS_ID() != null) values.put(GIS_ID, data.getGIS_ID());
//        if (data.getAPP_VERSION() != null) values.put(APP_VERSION, data.getAPP_VERSION());
//        if (data.getAPP_VERSION2() != null) values.put(APP_VERSION2, data.getAPP_VERSION2());
//
//        // Integers (if you store as int)
//        // Strings
//        if (data.getCOUNT() != null && !data.getCOUNT().trim().isEmpty()) {
//            values.put(MT_COUNT, data.getCOUNT());
//        }
//
//
//        // Always mark as updated
//        values.put(FLAG, "U");
//
//        // 🔹 WHERE clause: identify row by DATA_ID (assuming unique key)
//        String whereClause = MT_DATA_ID + "=?";
//        String[] whereArgs = new String[]{data.getDATA_ID()};
//
//        int rows = db.update(MATERIAL_TABLE, values, whereClause, whereArgs);
//        db.close();
//
//        return rows > 0;
//    }


    public ArrayList<SurveyVo> getSurveyData(String strCondition) {
        String query = "";
        ArrayList<SurveyVo> surveyVoArrayList = new ArrayList<SurveyVo>();

        if (strCondition.equals("")) {
            query = "SELECT * FROM  ARCGIS_TABLE";
        } else {

            query = strCondition;

        }

        SQLiteDatabase db = this.getWritableDatabase();
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
                vo.setWoId(cursor.getString(28));
                vo.setIsFeederType(cursor.getString(29));
                vo.setHtServiceNumber(cursor.getString(30));
                vo.setSurvaytype(cursor.getString(31));

                //MULTI CKT
                vo.setMC_33KV_FEEDER_ID_1(cursor.getString(32));
                vo.setMC_33KV_FEEDER_ID_2(cursor.getString(33));
                vo.setMC_33KV_FEEDER_ID_3(cursor.getString(34));
                vo.setMC_33KV_POLE_1(cursor.getString(35));
                vo.setMC_33KV_POLE_2(cursor.getString(36));
                vo.setMC_33KV_POLE_3(cursor.getString(37));

                vo.setMC_11KV_FEEDER_ID_1(cursor.getString(38));
                vo.setMC_11KV_FEEDER_ID_2(cursor.getString(39));
                vo.setMC_11KV_FEEDER_ID_3(cursor.getString(40));
                vo.setMC_11KV_POLE_1(cursor.getString(41));
                vo.setMC_11KV_POLE_2(cursor.getString(42));
                vo.setMC_11KV_POLE_3(cursor.getString(43));

                vo.setMC_LT_FEEDER_ID_1(cursor.getString(44));
                vo.setMC_LT_FEEDER_ID_2(cursor.getString(45));
                vo.setMC_LT_FEEDER_ID_3(cursor.getString(46));
                vo.setMC_LT_POLE_1(cursor.getString(47));
                vo.setMC_LT_POLE_2(cursor.getString(48));
                vo.setMC_LT_POLE_3(cursor.getString(49));

                vo.setNO_OF33KV_CKTS(cursor.getString(50));
                vo.setNO_OF11KV_CKTS(cursor.getString(51));
                vo.setNO_OF_LT_CKTS(cursor.getString(52));

                //DEFECTS
                vo.setDEFFECTS_AVAILABLE(cursor.getString(53));
                vo.setPOLE_DEFECTS(cursor.getString(54));
                vo.setCONDUCTOR_DEFECTS(cursor.getString(55));
                vo.setAB_SWITCH_DEFECTS(cursor.getString(56));
                vo.setINSULATOR_DEFECTS(cursor.getString(57));
                vo.setCROSSARM_DEFECTS(cursor.getString(58));
                vo.setTOPCLEAT_DEFECTS(cursor.getString(59));
                vo.setSTRUT_DEFECTS(cursor.getString(60));
                vo.setSTRAY_DEFECTS(cursor.getString(61));
                vo.setHTLT_CLEARANCE(cursor.getString(62));
                vo.setINTERMEDIATE_POLE(cursor.getString(63));
                vo.setRESTRINGING(cursor.getString(64));
                vo.setTREE_CLEARANCE(cursor.getString(65));
                vo.setOTHER_DEFFECTS(cursor.getString(66));

                //CROSSINGS
                vo.setCROSS_LT_CKT(cursor.getString(67));
                vo.setCROSS_6_3KV_ID(cursor.getString(68));
                vo.setCROSS_11KV_ID(cursor.getString(69));
                vo.setCROSS_33KV_ID(cursor.getString(70));
                vo.setCROSS_EHT(cursor.getString(71));
                vo.setCROSS_COMMUN(cursor.getString(72));
                vo.setCROSS_REMARKS(cursor.getString(73));
                vo.setCROSSING_TYPE(cursor.getString(74));

                //OTHER CABLES
                vo.setNUM_OTHER_CABLES(cursor.getString(75));
                vo.setPOLE_CONCRETE(cursor.getString(76));

                vo.setCURRENT_LAT(cursor.getString(77));
                vo.setCURRENT_LONG(cursor.getString(78));

                //MULTI CKT NOT IN LIST
                vo.setMC_33KV_NOT_IN_LIST_1(cursor.getString(79));
                vo.setMC_33KV_NOT_IN_LIST_2(cursor.getString(80));
                vo.setMC_33KV_NOT_IN_LIST_3(cursor.getString(81));
                vo.setMC_11KV_NOT_IN_LIST_1(cursor.getString(82));
                vo.setMC_11KV_NOT_IN_LIST_2(cursor.getString(83));
                vo.setMC_11KV_NOT_IN_LIST_3(cursor.getString(84));
                vo.setMC_LT_NOT_IN_LIST_1(cursor.getString(85));
                vo.setMC_LT_NOT_IN_LIST_2(cursor.getString(86));
                vo.setMC_LT_NOT_IN_LIST_3(cursor.getString(87));


                //  vo.setSECTION_CODE(cursor.getString(8));
                surveyVoArrayList.add(vo);
            } while (cursor.moveToNext());
        }

        db.close();
        return surveyVoArrayList;
    }

    public ArrayList<NewModel> getSurveyDataByDistance(
            double currentLat,
            double currentLon,
            String feederId,
            String surveyType,
            double maxDistanceMeters
    ) {

        ArrayList<NewModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {

            String query =
                    "SELECT * FROM SURVEY_TABLE_NEW " +
                            "WHERE LAT_NEW IS NOT NULL " +
                            "AND LON_NEW IS NOT NULL " +
                            "AND FEEDER_ID_NEW = ? " +
                            "AND SURVEY_TYPE_NEW = ?";

            cursor = db.rawQuery(query, new String[]{
                    feederId,
                    surveyType
            });

            if (cursor.moveToFirst()) {
                do {

                    // ✅ ALWAYS read by COLUMN NAME when using SELECT *
                    String latStr = cursor.getString(
                            cursor.getColumnIndexOrThrow("LAT_NEW")
                    );
                    String lonStr = cursor.getString(
                            cursor.getColumnIndexOrThrow("LON_NEW")
                    );

                    if (!latStr.isEmpty() && !lonStr.isEmpty()) {

                        double distance = getDistanceBetweenPoles(
                                latStr,
                                lonStr,
                                String.valueOf(currentLon),
                                String.valueOf(currentLat)
                        );

                        if (distance <= maxDistanceMeters) {

                            NewModel model = new NewModel();
                            model.setLATITUDE(latStr);
                            model.setLONGITUDE(lonStr);
                            model.setVOLTAGE_LEVEL(
                                    cursor.getString(cursor.getColumnIndexOrThrow("SURVEY_TYPE_NEW"))
                            );
                            model.setPOLE_NO(
                                    cursor.getString(cursor.getColumnIndexOrThrow("POLE_NO_NEW"))
                            );
                            model.setFROM_POLE(
                                    cursor.getString(cursor.getColumnIndexOrThrow("FROM_POLE_NEW"))
                            );

                            list.add(model);
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

        return list;
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


    public ArrayList<NewModel> getNewSurveyData(String est_no, String survey_type, boolean type, String gis_id) {
        ArrayList<NewModel> data = new ArrayList<>();
        String query = "";
       /* if (type) {
            query = "SELECT * FROM SURVEY_TABLE_NEW WHERE GIS_ID_NEW = '" + gis_id + "'";
        } else {
            query = "SELECT * FROM SURVEY_TABLE_NEW WHERE GIS_ID_NEW = '" + gis_id + "' AND SURVEY_TYPE_NEW = '" + survey_type + "'";
        }*/

        if (type) {
            query = "SELECT * FROM SURVEY_TABLE_NEW WHERE GIS_ID_NEW = '" + gis_id + "'";
        } else {
            if (survey_type.equalsIgnoreCase("11KV")) {
                query = "SELECT * FROM SURVEY_TABLE_NEW WHERE GIS_ID_NEW = '" + gis_id + "' AND (SURVEY_TYPE_NEW = '11KV' OR SURVEY_TYPE_NEW = '6.3KV')";
            } else {
                query = "SELECT * FROM SURVEY_TABLE_NEW WHERE GIS_ID_NEW = '" + gis_id + "' AND SURVEY_TYPE_NEW = '" + survey_type + "'";
            }

        }

        //   System.out.println("GET SURVEY LIST QUERY : " + query);

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    NewModel model = new NewModel();
                    model.setEST_NO(cursor.getString(0));
                    model.setFROM_POLE(cursor.getString(1));
                    model.setPOLE_NO(cursor.getString(2));
                    model.setLINE_TYPE(cursor.getString(3));
                    model.setPOLE_SURVEY_TYPE(cursor.getString(4));
                    model.setIS_MULTI_CKT(cursor.getString(5));
                    model.setMULTI_CKT_33(cursor.getString(6));
                    model.setMULTI_CKT_11(cursor.getString(7));
                    model.setMULTI_CKT_LT(cursor.getString(8));
                    model.setPOLE_TYPE(cursor.getString(9));
                    model.setPOLE_SUPPORT(cursor.getString(10));
                    model.setPOLE_CONFIG(cursor.getString(11));
                    model.setREF_ID(cursor.getString(12));
                    model.setLATITUDE(cursor.getString(13));
                    model.setLONGITUDE(cursor.getString(14));
                    model.setVOLTAGE_LEVEL(cursor.getString(15));
                    model.setIS_EXISTING_LOCATION_NEW(cursor.getString(16));
                    model.setEXISTING_LOCATION_33_NEW(cursor.getString(17));
                    model.setEXISTING_LOCATION_11_NEW(cursor.getString(18));
                    model.setEXISTING_LOCATION_LT_NEW(cursor.getString(19));
                    model.setTAPPING_LOCATION(cursor.getString(20));
                    model.setFROM_POLE_LATITUDE(cursor.getString(21));
                    model.setFROM_POLE_LONGITUDE(cursor.getString(22));
                    model.setMULTI_CKT_33_POLE_NO(cursor.getString(23));
                    model.setMULTI_CKT_11_POLE_NO(cursor.getString(24));
                    model.setMULTI_CKT_LT_POLE_NO(cursor.getString(25));
                    model.setNo_OF_STRUT(cursor.getString(26));
                    model.setNo_OF_STAY(cursor.getString(27));
                    model.setEXI_POLE_NO(cursor.getString(28));
                    model.setPOLE_LOCATION_TYPE(cursor.getString(29));
                    model.setPOLE_HEIGHT(cursor.getString(30));
                    model.setCONDUCTOR_SIZE(cursor.getString(31));
                    model.setLINE_SURVEY_TYPE(cursor.getString(32));
                    model.setDTR_PHASE(cursor.getString(33));
                    model.setDTR_CAPACITY(cursor.getString(34));
                    model.setDTR_MOUNTING(cursor.getString(35));
                    model.setDTR_SURVEY_TYPE(cursor.getString(36));
                    model.setLOCATION_ID(cursor.getString(37));
                    model.setNUMB_9M_STRUTS(cursor.getInt(38));
                    model.setNUMB_8M_STRUTS(cursor.getInt(39));
                    model.setNUMB_STAYS(cursor.getInt(40));
                    model.setNUMBERS_OF_FEEDERS(cursor.getString(41));
                    model.setDTR_STRUCTURE_CODE(cursor.getString(42));
                    model.setPOLE_DESC(cursor.getString(43));
                    model.setPOLE_SAP_CODE(cursor.getString(44));
                    model.setCONDUCTOR_DESC(cursor.getString(45));
                    model.setCONDUCTOR_SAP_CODE(cursor.getString(46));
                    model.setDTR_DESC(cursor.getString(47));
                    model.setDTR_SAP_CODE(cursor.getString(48));
                    model.setAB_SWITCH(cursor.getString(49));
                    model.setAB_DESC(cursor.getString(50));
                    model.setSS_ID(cursor.getString(51));
                    model.setSS_NAME(cursor.getString(52));
                    model.setSS_TYPE(cursor.getString(53));
                    model.setFEEDER_ID(cursor.getString(54));
                    model.setFEEDER_NAME(cursor.getString(55));
                    model.setDTR_AB_DESC(cursor.getString(56));
                    model.setDTR_AB_CODE(cursor.getString(57));
                    model.setHG_FUSE_DESC(cursor.getString(58));
                    model.setHG_FUSE_CODE(cursor.getString(59));
                    model.setLT_FUSE_DESC(cursor.getString(60));
                    model.setLT_FUSE_CODE(cursor.getString(61));
                    model.setCI_SAP_CODE(cursor.getString(62));
                    model.setCI_DESC(cursor.getString(63));
                    model.setGI_SAP_CODE(cursor.getString(64));
                    model.setGI_DESC(cursor.getString(65));
                    model.setCOUNT_CI(cursor.getInt(66));
                    model.setCOUNT_GI(cursor.getInt(67));
                    model.setCOUNT_TAPPING_XARM(cursor.getInt(68));
                    model.setCOUNT_V_XARM(cursor.getInt(69));
                    model.setCOUNT_TOP_CLEAT(cursor.getInt(70));
                    model.setCOUNT_PIN_INS(cursor.getInt(71));
                    model.setCOUNT_DISC_INS(cursor.getInt(72));
                    model.setCOUNT_SIDE_ARM(cursor.getInt(73));
                    model.setCOUNT_METALS_PARTS(cursor.getInt(74));
                    model.setTAPPINGXARM_CODE(cursor.getString(75));
                    model.setTAPPINGXARM_DESC(cursor.getString(76));
                    model.setVXARM_CODE(cursor.getString(77));
                    model.setVXARM_DESC(cursor.getString(78));
                    model.setSIDEARM_CODE(cursor.getString(79));
                    model.setSIDEARM_DESC(cursor.getString(80));
                    model.setCHANNEL_CODE(cursor.getString(81));
                    model.setCHANNEL_DESC(cursor.getString(82));
                    model.setTOPCLEAT_CODE(cursor.getString(83));
                    model.setTOPCLEAT_DESC(cursor.getString(84));
                    model.setPIN_INS_CODE(cursor.getString(85));
                    model.setPIN_INS_DESC(cursor.getString(86));
                    model.setCOUNT_PIN(cursor.getString(87));
                    model.setDISC_INS_CODE(cursor.getString(88));
                    model.setDISC_INS_DESC(cursor.getString(89));
                    model.setCOUNT_DISC(cursor.getString(90));
                    model.setGIPIN_CODE(cursor.getString(91));
                    model.setGIPIN_DESC(cursor.getString(92));
                    model.setCOUNT_GIPIN(cursor.getString(93));
                    model.setMETAL_PARTS_CODE(cursor.getString(94));
                    model.setMETAL_PARTS_DESC(cursor.getString(95));
                    model.setCOUNT_METAL_PARTS(cursor.getInt(96));
                    model.setCOUNT_CHANNEL(cursor.getInt(97));
                    model.setBACK_CLAMPS_CODE(cursor.getString(98));
                    model.setBACK_CLAMPS_DESC(cursor.getString(99));
                    model.setCOUNT_BACK_CLAMPS(cursor.getInt(100));
                    model.setSTAYCLAMP_11M_DESC(cursor.getString(101));
                    model.setSTAYCLAMP_11M_CODE(cursor.getString(102));
                    model.setCOUNT__STAYCLAMP_11M(cursor.getInt(103));
                    model.setSTAYCLAMP_9M_DESC(cursor.getString(104));
                    model.setSTAYCLAMP_9M_CODE(cursor.getString(105));
                    model.setCOUNT__STAYCLAMP_9M(cursor.getInt(106));
                    model.setSTAYCLAMP_8M_DESC(cursor.getString(107));
                    model.setSTAYCLAMP_8M_CODE(cursor.getString(108));
                    model.setCOUNT__STAYCLAMP_8M(cursor.getInt(109));
                    model.setSTAYCLAMP_SPUN_DESC(cursor.getString(110));
                    model.setSTAYCLAMP_SPUN_CODE(cursor.getString(111));
                    model.setCOUNT__STAYCLAMP_SPUN(cursor.getInt(112));
                    model.setGI_PIN_CODE(cursor.getString(113));
                    model.setGI_PIN_DESC(cursor.getString(114));
                    model.setCOUNT_GI_PIN(cursor.getInt(115));
                    model.setNEW_DATA_ADDED(cursor.getString(116));
                    model.setDTR_MOUNTING_DESC(cursor.getString(117));
                    model.setDTR_MOUNTING_CODE(cursor.getString(118));
                    model.setAB_MOUNTING_DESC(cursor.getString(119));
                    model.setAB_MOUNTING_CODE(cursor.getString(120));
                    model.setMT_CODE(cursor.getString(121));
                    model.setMT_DESC(cursor.getString(122));
                    model.setCOUNT_MT(cursor.getInt(123));
                    model.setMT_LOC_NO(cursor.getString(124));
                    model.setMT_SURVEY_TYPE(cursor.getString(125));
                    model.setPIN_CUT(cursor.getString(126));
                    model.setEND_POLE(cursor.getString(127));
                    model.setIS_NEW_SURVEY(cursor.getString(128));
                    model.setSERVICE_POLE(cursor.getString(129));
                    model.setEXT_LOCATION(cursor.getString(130));
                    model.setPOLE_CONCRETE(cursor.getString(131));
                    model.setPOLE_STRUCTURE(cursor.getString(132));
                    model.setSURVEY_FROM(cursor.getString(133));
                    model.setWORK_ORDER(cursor.getString(134));
                    model.setGIS_ID(cursor.getString(135));
                    model.setHTSC_NO(cursor.getString(136));
                    model.setIS_TRANSMITTED_DATA_UPDATED(cursor.getString(137));
                    model.setIS_TRANSMITTED_LOCATION(cursor.getString(138));
                    model.setREF_VOLTAGE(cursor.getString(139));
                    model.setEXT_ID(cursor.getString(140));
                    model.setPURPOSE(cursor.getString(141));
                    model.setLT_AVAILABLE(cursor.getString(142));
                    model.setCOUNT_CKT(cursor.getInt(143));
                    model.setLOAD_TYPE(cursor.getString(144));
                    model.setINTERMEDIATE_POLE(cursor.getString(145));
                    model.setOTHER_FEEDER(cursor.getString(146));
                    model.setCROSS_6_3KV_ID(cursor.getString(147));
                    model.setCROSSING_TYPE(cursor.getString(148));
                    model.setCROSS_33KV_ID(cursor.getString(149));
                    model.setCROSS_LT_CKT(cursor.getString(150));
                    model.setCROSS_COMMUN(cursor.getString(151));
                    model.setCROSS_EHT(cursor.getString(152));
                    model.setCROSS_REMARKS(cursor.getString(153));
                    model.setCROSS_11KV_ID(cursor.getString(154));
                    model.setREMARKS(cursor.getString(155));
                    model.setPOLE_PAINT(cursor.getString(156));
                    model.setCURRENT_LAT(cursor.getString(157));
                    model.setCURRENT_LONG(cursor.getString(158));
                    model.setAPP_VERSION(cursor.getString(159));
                    model.setAPP_VERSION2(cursor.getString(160));
                    model.setINTID1(cursor.getString(161));
                    model.setINTID2(cursor.getString(162));
                    model.setDTR_SNO(cursor.getString(163));
                    model.setDTR_MAKE(cursor.getString(164));
                    model.setDTR_LOCATION(cursor.getString(165));
                    model.setEDTR_PHASE(cursor.getString(166));
                    model.setEDTR_CAPACITY(cursor.getString(167));
                    model.setEDTR_EDITED(cursor.getString(168));
                    model.setSERVICE_TYPE(cursor.getString(169));
                    model.setDTR_SHIFTED(cursor.getString(170));
                    model.setNEW_CKT(cursor.getString(171));
                    model.setREG_NUM(cursor.getString(172));
                    model.setDTR_ID(cursor.getString(173));
                    model.setDT_SHIFT_REMARKS(cursor.getString(174));
                    model.setSURVEY_MODE(cursor.getString(175));
                    model.setCTPT(cursor.getString(176));
                    model.setNEW_MAPPED(cursor.getString(177));
                    model.setE_POLE(cursor.getString(178));
                    model.setFDR_LOC_NO_NUMBER(cursor.getString(179));
                    model.setFDR_SOURCE_NO_NUMBER(cursor.getString(180));
                    model.setLTCKT_LOC_NO_NUMBER(cursor.getString(181));
                    model.setLTCKT_SOURCE_NO_NUMBER(cursor.getString(182));
                    model.setGPS_ACCURACY(cursor.getString(183));
                    model.setDEVICE_ID(cursor.getString(184));
                    model.setRDSS_SCHEME_TYPE(cursor.getString(185));
                    model.setPACKAGE_NO(cursor.getString(186));
                    model.setDT_VOLATAGE_RATIO(cursor.getString(187));
                    model.setDT_MANF_DT(cursor.getString(188));
                    model.setDT_INSTL_DT(cursor.getString(189));
                    model.setIMAGE_1(cursor.getString(190));
                    model.setIMAGE_2(cursor.getString(191));
                    model.setIMAGE_3(cursor.getString(192));
                    model.setIS_IMAGE_TRANSMITTED(cursor.getString(193));
                    model.setADDRESS_1(cursor.getString(194));
                    model.setADDRESS_2(cursor.getString(195));
                    model.setASSET_TYPE(cursor.getString(196));
                    model.setBLOCK_PREMISES(cursor.getString(197));
                    model.setVILLAGE(cursor.getString(198));
                    model.setTOWN_CITY(cursor.getString(199));
                    model.setSTATE(cursor.getString(200));
                    model.setBRANCH_POINT(cursor.getString(201));
                    model.setACTUAL_LENGTH(cursor.getString(202));
                    model.setTARGET_FEEDER_ID(cursor.getString(203));
                    model.setTARGET_FEEDER_NAME(cursor.getString(204));
                    model.setREPEAT_POLE(cursor.getString(205));


                    data.add(model);
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return data;
    }

    public void updateImageShortCode(String ref_id, String shortCode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IMAGE_1, shortCode);
        values.put(IS_IMAGE_TRANSMITTED, "Y");

        int rowsAffected = db.update(
                SURVEY_TABLE_NEW,           // Table name
                values,            // New values
                LOCATION_ID_NEW + " = ?",          // WHERE clause
                new String[]{ref_id}  // WHERE arguments
        );

        db.close();

    }

    public void updateImageShortCode(String ref_id,
                                     String imageColumn,
                                     String shortCode) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(imageColumn, shortCode);

        // Mark image transmitted
        values.put(IS_IMAGE_TRANSMITTED, "Y");

        int rowsAffected = db.update(
                SURVEY_TABLE_NEW,
                values,
                LOCATION_ID_NEW + "=?",
                new String[]{ref_id}
        );

        Log.d("DB_UPDATE---->",
                "Rows Updated : " + rowsAffected +
                        " Column : " + imageColumn +
                        " ShortCode : " + shortCode);

        db.close();
    }


    public ArrayList<NewModel> getAllSurveyData() {
        ArrayList<NewModel> data = new ArrayList<>();
        String query = "";
        query = "SELECT * FROM SURVEY_TABLE_NEW";

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    NewModel model = new NewModel();
                    model.setEST_NO(cursor.getString(0));
                    model.setFROM_POLE(cursor.getString(1));
                    model.setPOLE_NO(cursor.getString(2));
                    model.setLINE_TYPE(cursor.getString(3));
                    model.setPOLE_SURVEY_TYPE(cursor.getString(4));
                    model.setIS_MULTI_CKT(cursor.getString(5));
                    model.setMULTI_CKT_33(cursor.getString(6));
                    model.setMULTI_CKT_11(cursor.getString(7));
                    model.setMULTI_CKT_LT(cursor.getString(8));
                    model.setPOLE_TYPE(cursor.getString(9));
                    model.setPOLE_SUPPORT(cursor.getString(10));
                    model.setPOLE_CONFIG(cursor.getString(11));
                    model.setREF_ID(cursor.getString(12));
                    model.setLATITUDE(cursor.getString(13));
                    model.setLONGITUDE(cursor.getString(14));
                    model.setVOLTAGE_LEVEL(cursor.getString(15));
                    model.setIS_EXISTING_LOCATION_NEW(cursor.getString(16));
                    model.setEXISTING_LOCATION_33_NEW(cursor.getString(17));
                    model.setEXISTING_LOCATION_11_NEW(cursor.getString(18));
                    model.setEXISTING_LOCATION_LT_NEW(cursor.getString(19));
                    model.setTAPPING_LOCATION(cursor.getString(20));
                    model.setFROM_POLE_LATITUDE(cursor.getString(21));
                    model.setFROM_POLE_LONGITUDE(cursor.getString(22));
                    model.setMULTI_CKT_33_POLE_NO(cursor.getString(23));
                    model.setMULTI_CKT_11_POLE_NO(cursor.getString(24));
                    model.setMULTI_CKT_LT_POLE_NO(cursor.getString(25));
                    model.setNo_OF_STRUT(cursor.getString(26));
                    model.setNo_OF_STAY(cursor.getString(27));
                    model.setEXI_POLE_NO(cursor.getString(28));
                    model.setPOLE_LOCATION_TYPE(cursor.getString(29));
                    model.setPOLE_HEIGHT(cursor.getString(30));
                    model.setCONDUCTOR_SIZE(cursor.getString(31));
                    model.setLINE_SURVEY_TYPE(cursor.getString(32));
                    model.setDTR_PHASE(cursor.getString(33));
                    model.setDTR_CAPACITY(cursor.getString(34));
                    model.setDTR_MOUNTING(cursor.getString(35));
                    model.setDTR_SURVEY_TYPE(cursor.getString(36));
                    model.setLOCATION_ID(cursor.getString(37));
                    model.setNUMB_9M_STRUTS(cursor.getInt(38));
                    model.setNUMB_8M_STRUTS(cursor.getInt(39));
                    model.setNUMB_STAYS(cursor.getInt(40));
                    model.setNUMBERS_OF_FEEDERS(cursor.getString(41));
                    model.setDTR_STRUCTURE_CODE(cursor.getString(42));
                    model.setPOLE_DESC(cursor.getString(43));
                    model.setPOLE_SAP_CODE(cursor.getString(44));
                    model.setCONDUCTOR_DESC(cursor.getString(45));
                    model.setCONDUCTOR_SAP_CODE(cursor.getString(46));
                    model.setDTR_DESC(cursor.getString(47));
                    model.setDTR_SAP_CODE(cursor.getString(48));
                    model.setAB_SWITCH(cursor.getString(49));
                    model.setAB_DESC(cursor.getString(50));
                    model.setSS_ID(cursor.getString(51));
                    model.setSS_NAME(cursor.getString(52));
                    model.setSS_TYPE(cursor.getString(53));
                    model.setFEEDER_ID(cursor.getString(54));
                    model.setFEEDER_NAME(cursor.getString(55));
                    model.setDTR_AB_DESC(cursor.getString(56));
                    model.setDTR_AB_CODE(cursor.getString(57));
                    model.setHG_FUSE_DESC(cursor.getString(58));
                    model.setHG_FUSE_CODE(cursor.getString(59));
                    model.setLT_FUSE_DESC(cursor.getString(60));
                    model.setLT_FUSE_CODE(cursor.getString(61));
                    model.setCI_SAP_CODE(cursor.getString(62));
                    model.setCI_DESC(cursor.getString(63));
                    model.setGI_SAP_CODE(cursor.getString(64));
                    model.setGI_DESC(cursor.getString(65));
                    model.setCOUNT_CI(cursor.getInt(66));
                    model.setCOUNT_GI(cursor.getInt(67));
                    model.setCOUNT_TAPPING_XARM(cursor.getInt(68));
                    model.setCOUNT_V_XARM(cursor.getInt(69));
                    model.setCOUNT_TOP_CLEAT(cursor.getInt(70));
                    model.setCOUNT_PIN_INS(cursor.getInt(71));
                    model.setCOUNT_DISC_INS(cursor.getInt(72));
                    model.setCOUNT_SIDE_ARM(cursor.getInt(73));
                    model.setCOUNT_METALS_PARTS(cursor.getInt(74));
                    model.setTAPPINGXARM_CODE(cursor.getString(75));
                    model.setTAPPINGXARM_DESC(cursor.getString(76));
                    model.setVXARM_CODE(cursor.getString(77));
                    model.setVXARM_DESC(cursor.getString(78));
                    model.setSIDEARM_CODE(cursor.getString(79));
                    model.setSIDEARM_DESC(cursor.getString(80));
                    model.setCHANNEL_CODE(cursor.getString(81));
                    model.setCHANNEL_DESC(cursor.getString(82));
                    model.setTOPCLEAT_CODE(cursor.getString(83));
                    model.setTOPCLEAT_DESC(cursor.getString(84));
                    model.setPIN_INS_CODE(cursor.getString(85));
                    model.setPIN_INS_DESC(cursor.getString(86));
                    model.setCOUNT_PIN(cursor.getString(87));
                    model.setDISC_INS_CODE(cursor.getString(88));
                    model.setDISC_INS_DESC(cursor.getString(89));
                    model.setCOUNT_DISC(cursor.getString(90));
                    model.setGIPIN_CODE(cursor.getString(91));
                    model.setGIPIN_DESC(cursor.getString(92));
                    model.setCOUNT_GIPIN(cursor.getString(93));
                    model.setMETAL_PARTS_CODE(cursor.getString(94));
                    model.setMETAL_PARTS_DESC(cursor.getString(95));
                    model.setCOUNT_METAL_PARTS(cursor.getInt(96));
                    model.setCOUNT_CHANNEL(cursor.getInt(97));
                    model.setBACK_CLAMPS_CODE(cursor.getString(98));
                    model.setBACK_CLAMPS_DESC(cursor.getString(99));
                    model.setCOUNT_BACK_CLAMPS(cursor.getInt(100));
                    model.setSTAYCLAMP_11M_DESC(cursor.getString(101));
                    model.setSTAYCLAMP_11M_CODE(cursor.getString(102));
                    model.setCOUNT__STAYCLAMP_11M(cursor.getInt(103));
                    model.setSTAYCLAMP_9M_DESC(cursor.getString(104));
                    model.setSTAYCLAMP_9M_CODE(cursor.getString(105));
                    model.setCOUNT__STAYCLAMP_9M(cursor.getInt(106));
                    model.setSTAYCLAMP_8M_DESC(cursor.getString(107));
                    model.setSTAYCLAMP_8M_CODE(cursor.getString(108));
                    model.setCOUNT__STAYCLAMP_8M(cursor.getInt(109));
                    model.setSTAYCLAMP_SPUN_DESC(cursor.getString(110));
                    model.setSTAYCLAMP_SPUN_CODE(cursor.getString(111));
                    model.setCOUNT__STAYCLAMP_SPUN(cursor.getInt(112));
                    model.setGI_PIN_CODE(cursor.getString(113));
                    model.setGI_PIN_DESC(cursor.getString(114));
                    model.setCOUNT_GI_PIN(cursor.getInt(115));
                    model.setNEW_DATA_ADDED(cursor.getString(116));
                    model.setDTR_MOUNTING_DESC(cursor.getString(117));
                    model.setDTR_MOUNTING_CODE(cursor.getString(118));
                    model.setAB_MOUNTING_DESC(cursor.getString(119));
                    model.setAB_MOUNTING_CODE(cursor.getString(120));
                    model.setMT_CODE(cursor.getString(121));
                    model.setMT_DESC(cursor.getString(122));
                    model.setCOUNT_MT(cursor.getInt(123));
                    model.setMT_LOC_NO(cursor.getString(124));
                    model.setMT_SURVEY_TYPE(cursor.getString(125));
                    model.setPIN_CUT(cursor.getString(126));
                    model.setEND_POLE(cursor.getString(127));
                    model.setIS_NEW_SURVEY(cursor.getString(128));
                    model.setSERVICE_POLE(cursor.getString(129));
                    model.setEXT_LOCATION(cursor.getString(130));
                    model.setPOLE_CONCRETE(cursor.getString(131));
                    model.setPOLE_STRUCTURE(cursor.getString(132));
                    model.setSURVEY_FROM(cursor.getString(133));
                    model.setWORK_ORDER(cursor.getString(134));
                    model.setGIS_ID(cursor.getString(135));
                    model.setHTSC_NO(cursor.getString(136));
                    model.setIS_TRANSMITTED_DATA_UPDATED(cursor.getString(137));
                    model.setIS_TRANSMITTED_LOCATION(cursor.getString(138));
                    model.setREF_VOLTAGE(cursor.getString(139));
                    model.setEXT_ID(cursor.getString(140));
                    model.setPURPOSE(cursor.getString(141));
                    model.setLT_AVAILABLE(cursor.getString(142));
                    model.setCOUNT_CKT(cursor.getInt(143));
                    model.setLOAD_TYPE(cursor.getString(144));
                    model.setINTERMEDIATE_POLE(cursor.getString(145));
                    model.setOTHER_FEEDER(cursor.getString(146));
                    model.setCROSS_6_3KV_ID(cursor.getString(147));
                    model.setCROSSING_TYPE(cursor.getString(148));
                    model.setCROSS_33KV_ID(cursor.getString(149));
                    model.setCROSS_LT_CKT(cursor.getString(150));
                    model.setCROSS_COMMUN(cursor.getString(151));
                    model.setCROSS_EHT(cursor.getString(152));
                    model.setCROSS_REMARKS(cursor.getString(153));
                    model.setCROSS_11KV_ID(cursor.getString(154));
                    model.setREMARKS(cursor.getString(155));
                    model.setPOLE_PAINT(cursor.getString(156));
                    model.setCURRENT_LAT(cursor.getString(157));
                    model.setCURRENT_LONG(cursor.getString(158));
                    model.setAPP_VERSION(cursor.getString(159));
                    model.setAPP_VERSION2(cursor.getString(160));
                    model.setINTID1(cursor.getString(161));
                    model.setINTID2(cursor.getString(162));
                    model.setDTR_SNO(cursor.getString(163));
                    model.setDTR_MAKE(cursor.getString(164));
                    model.setDTR_LOCATION(cursor.getString(165));
                    model.setEDTR_PHASE(cursor.getString(166));
                    model.setEDTR_CAPACITY(cursor.getString(167));
                    model.setEDTR_EDITED(cursor.getString(168));
                    model.setSERVICE_TYPE(cursor.getString(169));
                    model.setDTR_SHIFTED(cursor.getString(170));
                    model.setNEW_CKT(cursor.getString(171));
                    model.setREG_NUM(cursor.getString(172));
                    model.setDTR_ID(cursor.getString(173));
                    model.setDT_SHIFT_REMARKS(cursor.getString(174));
                    model.setSURVEY_MODE(cursor.getString(175));
                    model.setCTPT(cursor.getString(176));
                    model.setNEW_MAPPED(cursor.getString(177));
                    model.setE_POLE(cursor.getString(178));
                    model.setFDR_LOC_NO_NUMBER(cursor.getString(179));
                    model.setFDR_SOURCE_NO_NUMBER(cursor.getString(180));
                    model.setLTCKT_LOC_NO_NUMBER(cursor.getString(181));
                    model.setLTCKT_SOURCE_NO_NUMBER(cursor.getString(182));
                    model.setGPS_ACCURACY(cursor.getString(183));
                    model.setDEVICE_ID(cursor.getString(184));
                    model.setRDSS_SCHEME_TYPE(cursor.getString(185));
                    model.setPACKAGE_NO(cursor.getString(186));
                    model.setDT_VOLATAGE_RATIO(cursor.getString(187));
                    model.setDT_MANF_DT(cursor.getString(188));
                    model.setDT_INSTL_DT(cursor.getString(189));
                    model.setIMAGE_1(cursor.getString(190));
                    model.setIMAGE_2(cursor.getString(191));
                    model.setIMAGE_3(cursor.getString(192));
                    model.setIS_IMAGE_TRANSMITTED(cursor.getString(193));
                    model.setADDRESS_1(cursor.getString(194));
                    model.setADDRESS_2(cursor.getString(195));
                    model.setASSET_TYPE(cursor.getString(196));
                    model.setBLOCK_PREMISES(cursor.getString(197));
                    model.setVILLAGE(cursor.getString(198));
                    model.setTOWN_CITY(cursor.getString(199));
                    model.setSTATE(cursor.getString(200));
                    model.setBRANCH_POINT(cursor.getString(201));
                    model.setACTUAL_LENGTH(cursor.getString(202));
                    model.setTARGET_FEEDER_ID(cursor.getString(203));
                    model.setTARGET_FEEDER_NAME(cursor.getString(204));
                    model.setREPEAT_POLE(cursor.getString(205));


                    data.add(model);
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return data;
    }

    public ArrayList<NewModel> getCurrentSurveyData() {
        ArrayList<NewModel> data = new ArrayList<>();
        String query = "";
        //  query = "SELECT * FROM SURVEY_TABLE_NEW WHERE ";
        query = "SELECT * FROM SURVEY_TABLE_NEW WHERE IS_NEW_SURVEY = 'Y'";

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    NewModel model = new NewModel();
                    model.setEST_NO(cursor.getString(0));
                    model.setFROM_POLE(cursor.getString(1));
                    model.setPOLE_NO(cursor.getString(2));
                    model.setLINE_TYPE(cursor.getString(3));
                    model.setPOLE_SURVEY_TYPE(cursor.getString(4));
                    model.setIS_MULTI_CKT(cursor.getString(5));
                    model.setMULTI_CKT_33(cursor.getString(6));
                    model.setMULTI_CKT_11(cursor.getString(7));
                    model.setMULTI_CKT_LT(cursor.getString(8));
                    model.setPOLE_TYPE(cursor.getString(9));
                    model.setPOLE_SUPPORT(cursor.getString(10));
                    model.setPOLE_CONFIG(cursor.getString(11));
                    model.setREF_ID(cursor.getString(12));
                    model.setLATITUDE(cursor.getString(13));
                    model.setLONGITUDE(cursor.getString(14));
                    model.setVOLTAGE_LEVEL(cursor.getString(15));
                    model.setIS_EXISTING_LOCATION_NEW(cursor.getString(16));
                    model.setEXISTING_LOCATION_33_NEW(cursor.getString(17));
                    model.setEXISTING_LOCATION_11_NEW(cursor.getString(18));
                    model.setEXISTING_LOCATION_LT_NEW(cursor.getString(19));
                    model.setTAPPING_LOCATION(cursor.getString(20));
                    model.setFROM_POLE_LATITUDE(cursor.getString(21));
                    model.setFROM_POLE_LONGITUDE(cursor.getString(22));
                    model.setMULTI_CKT_33_POLE_NO(cursor.getString(23));
                    model.setMULTI_CKT_11_POLE_NO(cursor.getString(24));
                    model.setMULTI_CKT_LT_POLE_NO(cursor.getString(25));
                    model.setNo_OF_STRUT(cursor.getString(26));
                    model.setNo_OF_STAY(cursor.getString(27));
                    model.setEXI_POLE_NO(cursor.getString(28));
                    model.setPOLE_LOCATION_TYPE(cursor.getString(29));
                    model.setPOLE_HEIGHT(cursor.getString(30));
                    model.setCONDUCTOR_SIZE(cursor.getString(31));
                    model.setLINE_SURVEY_TYPE(cursor.getString(32));
                    model.setDTR_PHASE(cursor.getString(33));
                    model.setDTR_CAPACITY(cursor.getString(34));
                    model.setDTR_MOUNTING(cursor.getString(35));
                    model.setDTR_SURVEY_TYPE(cursor.getString(36));
                    model.setLOCATION_ID(cursor.getString(37));
                    model.setNUMB_9M_STRUTS(cursor.getInt(38));
                    model.setNUMB_8M_STRUTS(cursor.getInt(39));
                    model.setNUMB_STAYS(cursor.getInt(40));
                    model.setNUMBERS_OF_FEEDERS(cursor.getString(41));
                    model.setDTR_STRUCTURE_CODE(cursor.getString(42));
                    model.setPOLE_DESC(cursor.getString(43));
                    model.setPOLE_SAP_CODE(cursor.getString(44));
                    model.setCONDUCTOR_DESC(cursor.getString(45));
                    model.setCONDUCTOR_SAP_CODE(cursor.getString(46));
                    model.setDTR_DESC(cursor.getString(47));
                    model.setDTR_SAP_CODE(cursor.getString(48));
                    model.setAB_SWITCH(cursor.getString(49));
                    model.setAB_DESC(cursor.getString(50));
                    model.setSS_ID(cursor.getString(51));
                    model.setSS_NAME(cursor.getString(52));
                    model.setSS_TYPE(cursor.getString(53));
                    model.setFEEDER_ID(cursor.getString(54));
                    model.setFEEDER_NAME(cursor.getString(55));
                    model.setDTR_AB_DESC(cursor.getString(56));
                    model.setDTR_AB_CODE(cursor.getString(57));
                    model.setHG_FUSE_DESC(cursor.getString(58));
                    model.setHG_FUSE_CODE(cursor.getString(59));
                    model.setLT_FUSE_DESC(cursor.getString(60));
                    model.setLT_FUSE_CODE(cursor.getString(61));
                    model.setCI_SAP_CODE(cursor.getString(62));
                    model.setCI_DESC(cursor.getString(63));
                    model.setGI_SAP_CODE(cursor.getString(64));
                    model.setGI_DESC(cursor.getString(65));
                    model.setCOUNT_CI(cursor.getInt(66));
                    model.setCOUNT_GI(cursor.getInt(67));
                    model.setCOUNT_TAPPING_XARM(cursor.getInt(68));
                    model.setCOUNT_V_XARM(cursor.getInt(69));
                    model.setCOUNT_TOP_CLEAT(cursor.getInt(70));
                    model.setCOUNT_PIN_INS(cursor.getInt(71));
                    model.setCOUNT_DISC_INS(cursor.getInt(72));
                    model.setCOUNT_SIDE_ARM(cursor.getInt(73));
                    model.setCOUNT_METALS_PARTS(cursor.getInt(74));
                    model.setTAPPINGXARM_CODE(cursor.getString(75));
                    model.setTAPPINGXARM_DESC(cursor.getString(76));
                    model.setVXARM_CODE(cursor.getString(77));
                    model.setVXARM_DESC(cursor.getString(78));
                    model.setSIDEARM_CODE(cursor.getString(79));
                    model.setSIDEARM_DESC(cursor.getString(80));
                    model.setCHANNEL_CODE(cursor.getString(81));
                    model.setCHANNEL_DESC(cursor.getString(82));
                    model.setTOPCLEAT_CODE(cursor.getString(83));
                    model.setTOPCLEAT_DESC(cursor.getString(84));
                    model.setPIN_INS_CODE(cursor.getString(85));
                    model.setPIN_INS_DESC(cursor.getString(86));
                    model.setCOUNT_PIN(cursor.getString(87));
                    model.setDISC_INS_CODE(cursor.getString(88));
                    model.setDISC_INS_DESC(cursor.getString(89));
                    model.setCOUNT_DISC(cursor.getString(90));
                    model.setGIPIN_CODE(cursor.getString(91));
                    model.setGIPIN_DESC(cursor.getString(92));
                    model.setCOUNT_GIPIN(cursor.getString(93));
                    model.setMETAL_PARTS_CODE(cursor.getString(94));
                    model.setMETAL_PARTS_DESC(cursor.getString(95));
                    model.setCOUNT_METAL_PARTS(cursor.getInt(96));
                    model.setCOUNT_CHANNEL(cursor.getInt(97));
                    model.setBACK_CLAMPS_CODE(cursor.getString(98));
                    model.setBACK_CLAMPS_DESC(cursor.getString(99));
                    model.setCOUNT_BACK_CLAMPS(cursor.getInt(100));
                    model.setSTAYCLAMP_11M_DESC(cursor.getString(101));
                    model.setSTAYCLAMP_11M_CODE(cursor.getString(102));
                    model.setCOUNT__STAYCLAMP_11M(cursor.getInt(103));
                    model.setSTAYCLAMP_9M_DESC(cursor.getString(104));
                    model.setSTAYCLAMP_9M_CODE(cursor.getString(105));
                    model.setCOUNT__STAYCLAMP_9M(cursor.getInt(106));
                    model.setSTAYCLAMP_8M_DESC(cursor.getString(107));
                    model.setSTAYCLAMP_8M_CODE(cursor.getString(108));
                    model.setCOUNT__STAYCLAMP_8M(cursor.getInt(109));
                    model.setSTAYCLAMP_SPUN_DESC(cursor.getString(110));
                    model.setSTAYCLAMP_SPUN_CODE(cursor.getString(111));
                    model.setCOUNT__STAYCLAMP_SPUN(cursor.getInt(112));
                    model.setGI_PIN_CODE(cursor.getString(113));
                    model.setGI_PIN_DESC(cursor.getString(114));
                    model.setCOUNT_GI_PIN(cursor.getInt(115));
                    model.setNEW_DATA_ADDED(cursor.getString(116));
                    model.setDTR_MOUNTING_DESC(cursor.getString(117));
                    model.setDTR_MOUNTING_CODE(cursor.getString(118));
                    model.setAB_MOUNTING_DESC(cursor.getString(119));
                    model.setAB_MOUNTING_CODE(cursor.getString(120));
                    model.setMT_CODE(cursor.getString(121));
                    model.setMT_DESC(cursor.getString(122));
                    model.setCOUNT_MT(cursor.getInt(123));
                    model.setMT_LOC_NO(cursor.getString(124));
                    model.setMT_SURVEY_TYPE(cursor.getString(125));
                    model.setPIN_CUT(cursor.getString(126));
                    model.setEND_POLE(cursor.getString(127));
                    model.setIS_NEW_SURVEY(cursor.getString(128));
                    model.setSERVICE_POLE(cursor.getString(129));
                    model.setEXT_LOCATION(cursor.getString(130));
                    model.setPOLE_CONCRETE(cursor.getString(131));
                    model.setPOLE_STRUCTURE(cursor.getString(132));
                    //  System.out.println("SURVEY FROM READ: " + cursor.getString(133));
                    model.setSURVEY_FROM(cursor.getString(133));
                    model.setWORK_ORDER(cursor.getString(134));
                    model.setGIS_ID(cursor.getString(135));
                    model.setHTSC_NO(cursor.getString(136));
                    model.setIS_TRANSMITTED_DATA_UPDATED(cursor.getString(137));
                    model.setIS_TRANSMITTED_LOCATION(cursor.getString(138));
                    model.setREF_VOLTAGE(cursor.getString(139));
                    model.setEXT_ID(cursor.getString(140));
                    model.setPURPOSE(cursor.getString(141));
                    model.setLT_AVAILABLE(cursor.getString(142));
                    model.setCOUNT_CKT(cursor.getInt(143));
                    model.setLOAD_TYPE(cursor.getString(144));
                    model.setINTERMEDIATE_POLE(cursor.getString(145));
                    model.setOTHER_FEEDER(cursor.getString(146));
                    model.setCROSS_6_3KV_ID(cursor.getString(147));
                    model.setCROSSING_TYPE(cursor.getString(148));
                    model.setCROSS_33KV_ID(cursor.getString(149));
                    model.setCROSS_LT_CKT(cursor.getString(150));
                    model.setCROSS_COMMUN(cursor.getString(151));
                    model.setCROSS_EHT(cursor.getString(152));
                    model.setCROSS_REMARKS(cursor.getString(153));
                    model.setCROSS_11KV_ID(cursor.getString(154));
                    model.setREMARKS(cursor.getString(155));
                    model.setPOLE_PAINT(cursor.getString(156));
                    model.setCURRENT_LAT(cursor.getString(157));
                    model.setCURRENT_LONG(cursor.getString(158));
                    model.setAPP_VERSION(cursor.getString(159));
                    model.setAPP_VERSION2(cursor.getString(160));
                    model.setINTID1(cursor.getString(161));
                    model.setINTID2(cursor.getString(162));
                    model.setDTR_SNO(cursor.getString(163));
                    model.setDTR_MAKE(cursor.getString(164));
                    model.setDTR_LOCATION(cursor.getString(165));
                    model.setEDTR_PHASE(cursor.getString(166));
                    model.setEDTR_CAPACITY(cursor.getString(167));
                    model.setEDTR_EDITED(cursor.getString(168));
                    model.setSERVICE_TYPE(cursor.getString(169));
                    model.setDTR_SHIFTED(cursor.getString(170));
                    model.setNEW_CKT(cursor.getString(171));
                    model.setREG_NUM(cursor.getString(172));
                    model.setDTR_ID(cursor.getString(173));
                    model.setDT_SHIFT_REMARKS(cursor.getString(174));
                    model.setSURVEY_MODE(cursor.getString(175));
                    model.setCTPT(cursor.getString(176));
                    model.setNEW_MAPPED(cursor.getString(177));
                    model.setE_POLE(cursor.getString(178));
                    model.setFDR_LOC_NO_NUMBER(cursor.getString(179));
                    model.setFDR_SOURCE_NO_NUMBER(cursor.getString(180));
                    model.setLTCKT_LOC_NO_NUMBER(cursor.getString(181));
                    model.setLTCKT_SOURCE_NO_NUMBER(cursor.getString(182));
                    model.setGPS_ACCURACY(cursor.getString(183));
                    model.setDEVICE_ID(cursor.getString(184));
                    model.setRDSS_SCHEME_TYPE(cursor.getString(185));
                    model.setPACKAGE_NO(cursor.getString(186));
                    model.setDT_VOLATAGE_RATIO(cursor.getString(187));
                    model.setDT_MANF_DT(cursor.getString(188));
                    model.setDT_INSTL_DT(cursor.getString(189));
                    model.setIMAGE_1(cursor.getString(190));
                    model.setIMAGE_2(cursor.getString(191));
                    model.setIMAGE_3(cursor.getString(192));
                    model.setIS_IMAGE_TRANSMITTED(cursor.getString(193));
                    model.setADDRESS_1(cursor.getString(194));
                    model.setADDRESS_2(cursor.getString(195));
                    model.setASSET_TYPE(cursor.getString(196));
                    model.setBLOCK_PREMISES(cursor.getString(197));
                    model.setVILLAGE(cursor.getString(198));
                    model.setTOWN_CITY(cursor.getString(199));
                    model.setSTATE(cursor.getString(200));
                    model.setBRANCH_POINT(cursor.getString(201));
                    model.setACTUAL_LENGTH(cursor.getString(202));
                    model.setTARGET_FEEDER_ID(cursor.getString(203));
                    model.setTARGET_FEEDER_NAME(cursor.getString(204));
                    model.setREPEAT_POLE(cursor.getString(205));


                    data.add(model);
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return data;
    }

    public boolean deleteALLSurveyStatusNEW() {

        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.delete(SURVEY_TABLE_NEW, null, null);
        if (result > 0) {
            return true;
        } else {
            return false;
        }

    }

    public boolean deleteAllNewSurveyData() {
        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.delete(SURVEY_TABLE_NEW, null, null);
        // System.out.println("DELETE RESULT : " + result);
        if (result > 0) {
            return true;
        } else {
            return false;
        }

    }


    public boolean updateMultiCircuit33(String est_no, String pole, String m_pole, String gis_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MULTI_CKT_33_POLE_NO_NEW, pole);
        String whereClause = "GIS_ID_NEW = ? AND POLE_NO_NEW = ?";
        String[] whereArgs = {gis_id, m_pole};
        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.update(SURVEY_TABLE_NEW, contentValues, whereClause, whereArgs);
        //  System.out.println("UPDATE 33 MULTI CKT Table : " + result);
        if (result > 0) {
            return true;
        } else {
            return false;
        }

    }

    public boolean updateMultiCircuit11(String est_no, String pole, String m_pole, String gis_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MULTI_CKT_11_POLE_NO_NEW, pole);
        String whereClause = "GIS_ID_NEW = ? AND POLE_NO_NEW = ?";
        String[] whereArgs = {gis_id, m_pole};
        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.update(SURVEY_TABLE_NEW, contentValues, whereClause, whereArgs);
        // System.out.println("UPDATE 11 MULTI CKT Table : " + result);
        if (result > 0) {
            return true;
        } else {
            return false;
        }

    }

    public boolean updateMultiCircuitLT(String est_no, String pole, String m_pole, String gis_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MULTI_CKT_LT_POLE_NO_NEW, pole);
        String whereClause = "GIS_ID_NEW = ? AND POLE_NO_NEW = ?";
        String[] whereArgs = {gis_id, m_pole};
        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.update(SURVEY_TABLE_NEW, contentValues, whereClause, whereArgs);
        //System.out.println("UPDATE LT MULTI CKT Table : " + result);
        if (result > 0) {
            return true;
        } else {
            return false;
        }

    }


    public boolean deleteNewSurveyData(String est_no, String gis_id) {
        String whereClause = "GIS_ID_NEW = ?";
        String[] whereArgs = {gis_id};
        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.delete(SURVEY_TABLE_NEW, whereClause, whereArgs);
        //System.out.println("DELETE RESULT : " + result);
        if (result > 0) {
            return true;
        } else {
            return false;
        }

    }

    public ArrayList<NewModel> getLatAndLon(String est_no, String pole, String gis_id) {
        ArrayList<NewModel> data = new ArrayList<>();
        String query = "SELECT * FROM SURVEY_TABLE_NEW WHERE GIS_ID_NEW = '" + gis_id + "' AND POLE_NO_NEW = '" + pole + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    NewModel model = new NewModel();
                    model.setLATITUDE(cursor.getString(13));
                    model.setLONGITUDE(cursor.getString(14));
                    model.setLOCATION_ID(cursor.getString(37));
                    data.add(model);
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return data;
    }

    public ArrayList<NewModel> getPoleDetails(String est_no, String pole, String gis_id) {
        ArrayList<NewModel> data = new ArrayList<>();
        String query = "SELECT * FROM SURVEY_TABLE_NEW WHERE GIS_ID_NEW = '" + gis_id + "' AND POLE_NO_NEW = '" + pole + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    NewModel model = new NewModel();
                    // Assuming LINE_TYPE is at index 15, IS_MULTI_CKT_NEW at 16, and POLE_TYPE at 17
                    model.setLINE_TYPE(cursor.getString(3));        // Update index as per the actual table schema
                    model.setIS_MULTI_CKT(cursor.getString(5)); // Update index as per the actual table schema
                    model.setPOLE_TYPE(cursor.getString(9));        // Update index as per the actual table schema
                    model.setPOLE_HEIGHT(cursor.getString(30));        // Update index as per the actual table schema
                    model.setPOLE_SUPPORT(cursor.getString(10));        // Update index as per the actual table schema
                    model.setNo_OF_STRUT(cursor.getString(26));        // Update index as per the actual table schema
                    model.setNo_OF_STAY(cursor.getString(27));        // Update index as per the actual table schema
                    data.add(model);
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        } finally {
            db.close();
        }
        return data;
    }

    public ArrayList<NewModel> getPoleDataDetails(String est_no, String pole_no, String gis_id) {
        ArrayList<NewModel> data = new ArrayList<>();
        String query = "SELECT * FROM SURVEY_TABLE_NEW WHERE GIS_ID_NEW = '" + gis_id + "' AND POLE_NO_NEW = '" + pole_no + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    NewModel model = new NewModel();
                    model.setPOLE_TYPE(cursor.getString(9));
                    model.setPOLE_SUPPORT(cursor.getString(10));
                    model.setPOLE_CONFIG(cursor.getString(11));
                    model.setPOLE_HEIGHT(cursor.getString(30));
                    model.setPOLE_CONCRETE(cursor.getString(131));
                    model.setPOLE_STRUCTURE(cursor.getString(132));
                    model.setPIN_CUT(cursor.getString(126));
                    model.setIS_EXISTING_LOCATION_NEW(cursor.getString(16));
                    model.setDTR_SURVEY_TYPE(cursor.getString(36));
                    model.setDTR_STRUCTURE_CODE(cursor.getString(42));
                    model.setLINE_TYPE(cursor.getString(3));
                    //System.out.println("PIN_CUT_POINT");
                    data.add(model);
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return data;
    }


    public boolean addMaterialData(MaterialPostingModel data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MT_ESTIMATE_NO, data.getESTIMATE_NO());
        values.put(MT_VOLTAGE_LEVEL, data.getVOLTAGE_LEVEL());
        values.put(MT_LOCATION_ID, data.getLOCATION_ID());
        values.put(MT_LOCATION_NO, data.getLOCATION_NO());
        values.put(MT_MATERIAL, data.getMATERIAL());
        values.put(MT_SAP_CODE, data.getSAP_CODE());
        values.put(MT_MATERIAL_DESC, data.getMATERIAL_DESC());
        values.put(MT_COUNT, data.getCOUNT());
        values.put(MT_SURVEY_CODE, data.getSURVEY_CODE());
        values.put(MT_DATA_ID, data.getDATA_ID());
        values.put(MT_TYPE, data.getTYPE());
        values.put(GIS_ID, data.getGIS_ID());
        values.put(APP_VERSION, data.getAPP_VERSION());
        values.put(APP_VERSION2, data.getAPP_VERSION2());
        values.put(MOD_TYPE, data.getMOD_TYPE());
        values.put(REF_ID, data.getREF_ID());
        values.put(UOM, data.getUOM());
        values.put(FLAG, "N");

        try {
            long a = db.insertOrThrow(MATERIAL_TABLE, null, values);
            //Log.d("DB_SUCCESS", "Insert success rowId=" + a);
            return true;
        } catch (Exception e) {
            //Log.e("DB_ERROR", "Insert failed for MATERIAL_TABLE. Values=" + values.toString(), e);
            return false;
        } finally {
            db.close();
        }
    }

    public ArrayList<MaterialPostingModel> getMaterialDataForPole(String gis_id, String pole_no) {
        ArrayList<MaterialPostingModel> data = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        // Use parameterized query to avoid SQL injection
        String query = "SELECT * FROM MATERIAL_TABLE WHERE GIS_ID = ? AND MT_LOCATION_NO = ?";
        try {
            Cursor cursor = db.rawQuery(query, new String[]{gis_id, pole_no});
            if (cursor.moveToFirst()) {
                do {
                    MaterialPostingModel model = new MaterialPostingModel();
                    model.setESTIMATE_NO(cursor.getString(0));   // Update indexes as per schema
                    model.setVOLTAGE_LEVEL(cursor.getString(1));
                    model.setLOCATION_ID(cursor.getString(2));
                    model.setLOCATION_NO(cursor.getString(3));
                    model.setMATERIAL(cursor.getString(4));
                    model.setSAP_CODE(cursor.getString(5));
                    model.setMATERIAL_DESC(cursor.getString(6));
                    model.setCOUNT(cursor.getString(7));
                    model.setSURVEY_CODE(cursor.getString(8));
                    model.setDATA_ID(cursor.getString(9));
                    model.setTYPE(cursor.getString(10));
                    model.setGIS_ID(cursor.getString(11));
                    model.setAPP_VERSION(cursor.getString(12));
                    model.setAPP_VERSION2(cursor.getString(13));
                    model.setUOM(cursor.getString(14));
                    // If POLE_NO column exists, fetch it as well:
                    // model.setPOLE_NO(cursor.getString(<index_of_POLE_NO>));

                    data.add(model);
                } while (cursor.moveToNext());
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return data;
    }


    public ArrayList<MaterialPostingModel> getMaterialData(String est_no, String gis_id) {
        ArrayList<MaterialPostingModel> data = new ArrayList<>();
        String query = "SELECT * FROM MATERIAL_TABLE WHERE GIS_ID = '" + gis_id + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    MaterialPostingModel model = new MaterialPostingModel();
                    model.setESTIMATE_NO(cursor.getString(0));        // Update index as per the actual table schema
                    model.setVOLTAGE_LEVEL(cursor.getString(1)); // Update index as per the actual table schema
                    model.setLOCATION_ID(cursor.getString(2));        // Update index as per the actual table schema
                    model.setLOCATION_NO(cursor.getString(3));        // Update index as per the actual table schema
                    model.setMATERIAL(cursor.getString(4));        // Update index as per the actual table schema
                    model.setSAP_CODE(cursor.getString(5));        // Update index as per the actual table schema
                    model.setMATERIAL_DESC(cursor.getString(6));        // Update index as per the actual table schema
                    model.setCOUNT(cursor.getString(7));        // Update index as per the actual table schema
                    model.setSURVEY_CODE(cursor.getString(8));        // Update index as per the actual table schema
                    model.setDATA_ID(cursor.getString(9));        // Update index as per the actual table schema
                    model.setTYPE(cursor.getString(10));        // Update index as per the actual table schema
                    model.setGIS_ID(cursor.getString(11));        // Update index as per the actual table schema
                    model.setAPP_VERSION(cursor.getString(12));        // Update index as per the actual table schema
                    model.setAPP_VERSION2(cursor.getString(13));        // Update index as per the actual table schema
                    data.add(model);
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        } finally {
            db.close();
        }
        return data;
    }


    public boolean clearMaterialTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            db.delete(MATERIAL_TABLE, null, null);
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
    }


    public boolean update11KVFeeder1Status(String est_no, boolean isSurveyCompleted, String gis_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(IS_11KV_FEEDER_1_SURVEY_COMPLETED, isSurveyCompleted ? "Y" : "N");
        String whereClause = "GIS_ID = ?";
        String[] whereArgs = {gis_id};
        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.update(STATUS_TABLE, contentValues, whereClause, whereArgs);
        return result > 0;
    }

    public boolean update11KVFeeder2Status(String est_no, boolean isSurveyCompleted, String gis_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(IS_11KV_FEEDER_2_SURVEY_COMPLETED, isSurveyCompleted ? "Y" : "N");
        String whereClause = "GIS_ID = ?";
        String[] whereArgs = {gis_id};
        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.update(STATUS_TABLE, contentValues, whereClause, whereArgs);
        return result > 0;
    }

    public boolean update11KVFeeder3Status(String est_no, boolean isSurveyCompleted, String gis_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(IS_11KV_FEEDER_3_SURVEY_COMPLETED, isSurveyCompleted ? "Y" : "N");
        String whereClause = "GIS_ID = ?";
        String[] whereArgs = {gis_id};
        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.update(STATUS_TABLE, contentValues, whereClause, whereArgs);
        return result > 0;
    }

    public boolean update11KVFeeder4Status(String est_no, boolean isSurveyCompleted, String gis_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(IS_11KV_FEEDER_4_SURVEY_COMPLETED, isSurveyCompleted ? "Y" : "N");
        String whereClause = "GIS_ID = ?";
        String[] whereArgs = {gis_id};
        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.update(STATUS_TABLE, contentValues, whereClause, whereArgs);
        return result > 0;
    }

    public boolean update11KVFeeder5Status(String est_no, boolean isSurveyCompleted, String gis_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(IS_11KV_FEEDER_5_SURVEY_COMPLETED, isSurveyCompleted ? "Y" : "N");
        String whereClause = "GIS_ID = ?";
        String[] whereArgs = {gis_id};
        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.update(STATUS_TABLE, contentValues, whereClause, whereArgs);
        return result > 0;
    }

    public boolean update11KVFeeder6Status(String est_no, boolean isSurveyCompleted, String gis_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(IS_11KV_FEEDER_6_SURVEY_COMPLETED, isSurveyCompleted ? "Y" : "N");

        String whereClause = "GIS_ID = ?";
        String[] whereArgs = {gis_id};

        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.update(STATUS_TABLE, contentValues, whereClause, whereArgs);
        return result > 0;
    }

    public boolean update11KVFeeder7Status(String est_no, boolean isSurveyCompleted, String gis_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(IS_11KV_FEEDER_7_SURVEY_COMPLETED, isSurveyCompleted ? "Y" : "N");

        String whereClause = "GIS_ID = ?";
        String[] whereArgs = {gis_id};

        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.update(STATUS_TABLE, contentValues, whereClause, whereArgs);
        return result > 0;
    }

    public boolean update11KVFeeder8Status(String est_no, boolean isSurveyCompleted, String gis_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(IS_11KV_FEEDER_8_SURVEY_COMPLETED, isSurveyCompleted ? "Y" : "N");

        String whereClause = "GIS_ID = ?";
        String[] whereArgs = {gis_id};

        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.update(STATUS_TABLE, contentValues, whereClause, whereArgs);
        return result > 0;
    }

    public boolean update11KVFeeder9Status(String est_no, boolean isSurveyCompleted, String gis_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(IS_11KV_FEEDER_9_SURVEY_COMPLETED, isSurveyCompleted ? "Y" : "N");

        String whereClause = "GIS_ID = ?";
        String[] whereArgs = {gis_id};

        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.update(STATUS_TABLE, contentValues, whereClause, whereArgs);
        return result > 0;
    }

    public boolean update11KVFeeder10Status(String est_no, boolean isSurveyCompleted, String gis_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(IS_11KV_FEEDER_10_SURVEY_COMPLETED, isSurveyCompleted ? "Y" : "N");

        String whereClause = "GIS_ID = ?";
        String[] whereArgs = {gis_id};

        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.update(STATUS_TABLE, contentValues, whereClause, whereArgs);
        return result > 0;
    }


    public void deleteAllDataFromDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(WORK_TABLE, null, null);
        db.delete(SURVEY_TABLE_33KV, null, null);
        db.delete(SURVEY_TABLE_11KV, null, null);
        db.delete(SURVEY_TABLE_LT, null, null);
        db.delete(DTR_TABLE, null, null);
        db.delete(LT_CKT_TABLE, null, null);
        db.delete(SURVEY_TABLE_NEW, null, null);
        db.delete(STATUS_TABLE, null, null);
        db.delete(POLE_TYPES_TABLE, null, null);
        db.delete(CONDUCTOR_TYPE_TABLE, null, null);
        db.delete(DTR_TYPE_TABLE, null, null);
        db.delete(MATERIAL_MASTER_TABLE, null, null);
        db.delete(EST_FEEDERS, null, null);
        db.delete(MATERIAL_TABLE, null, null);
        db.delete(FEEDER_MASTER_DTRS, null, null);
        db.delete(EXISTING_POLES_TABLE_33KV, null, null);
        db.delete(EXISTING_POLES_TABLE_11KV, null, null);
        db.delete(EXISTING_POLES_TABLE_NEW, null, null);
        db.delete(EDIT_TABLE, null, null);
        db.delete(POLE_HEIGHT_DETAILS_TABLE, null, null);
        db.delete(LT_LINE_CONFIG_TABLE, null, null);
        db.delete(DTR_HT_TABLE, null, null);
        db.delete(DTR_PHASE_CAPACITIES_TABLE, null, null);
    }


    //LOCATIONS EDIT
    public void insertOrUpdateEditLocationsData(EditModel editModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if the data with the given EST_NO and POLE_NO exists
      /*  String query = "SELECT * FROM " + EDIT_TABLE + " WHERE " +
                EDIT_EST_NO + " = ? AND " +
                EDIT_VOLTAGE_LEVEL + " = ? AND " +
                EDIT_LOCATION_ID + " = ? AND " +
                EDIT_POLE_NO + " = ?";*/


        String query = "SELECT * FROM EDIT_TABLE WHERE EDIT_GIS_ID = '" + editModel.getEDIT_GIS_ID() + "' AND EDIT_VOLTAGE_LEVEL = '" + editModel.getEDIT_VOLTAGE_LEVEL() + "' AND EDIT_LOCATION_ID = '" + editModel.getEDIT_LOCATION_ID() + "' AND EDIT_POLE_NO = '" + editModel.getEDIT_POLE_NO() + "'";

        //System.out.println("insertOrUpdateEditLocationsData query : " + query);

        // Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(editModel.getEDIT_EST_NO()), String.valueOf(editModel.getEDIT_VOLTAGE_LEVEL()), String.valueOf(editModel.getEDIT_LOCATION_ID()), String.valueOf(editModel.getEDIT_POLE_NO())});

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            // Record exists, perform update
            ContentValues contentValues = new ContentValues();
            contentValues.put(EDIT_POLE_LAT, editModel.getEDIT_POLE_LAT());
            contentValues.put(EDIT_POLE_LON, editModel.getEDIT_POLE_LON());


            // Update the existing record
            db.update(EDIT_TABLE, contentValues,
                    EDIT_GIS_ID + " = ? AND " + EDIT_POLE_NO + " = ? AND " + EDIT_VOLTAGE_LEVEL + " = ? AND " + EDIT_LOCATION_ID + " = ?",
                    new String[]{String.valueOf(editModel.getEDIT_GIS_ID()), String.valueOf(editModel.getEDIT_POLE_NO()), String.valueOf(editModel.getEDIT_VOLTAGE_LEVEL()), String.valueOf(editModel.getEDIT_LOCATION_ID())});
        } else {
            // Record does not exist, insert new
            ContentValues contentValues = new ContentValues();
            contentValues.put(EDIT_EST_NO, editModel.getEDIT_EST_NO());
            contentValues.put(EDIT_POLE_NO, editModel.getEDIT_POLE_NO());
            contentValues.put(EDIT_POLE_LAT, editModel.getEDIT_POLE_LAT());
            contentValues.put(EDIT_POLE_LON, editModel.getEDIT_POLE_LON());
            contentValues.put(EDIT_VOLTAGE_LEVEL, editModel.getEDIT_VOLTAGE_LEVEL());
            contentValues.put(EDIT_LOCATION_ID, editModel.getEDIT_LOCATION_ID());
            contentValues.put(EDIT_GIS_ID, editModel.getEDIT_GIS_ID());
            contentValues.put(EDIT_TYPE, editModel.getEDIT_TYPE());

            // Insert the new record
            db.insert(EDIT_TABLE, null, contentValues);
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
    }

    public ArrayList<EditModel> getEditLocationsData() {
        ArrayList<EditModel> data = new ArrayList<>();
        String query = "SELECT * FROM EDIT_TABLE";
        // String query = "SELECT * FROM EDIT_TABLE WHERE EDIT_TYPE = '" + LoginActivity.SURVEY_FROM + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    EditModel model = new EditModel();
                    model.setEDIT_EST_NO(cursor.getString(0));        // Update index as per the actual table schema
                    model.setEDIT_POLE_NO(cursor.getString(1)); // Update index as per the actual table schema
                    model.setEDIT_POLE_LAT(cursor.getString(2));        // Update index as per the actual table schema
                    model.setEDIT_POLE_LON(cursor.getString(3));        // Update index as per the actual table schema
                    model.setEDIT_VOLTAGE_LEVEL(cursor.getString(4));        // Update index as per the actual table schema
                    model.setEDIT_LOCATION_ID(cursor.getString(5));        // Update index as per the actual table schema
                    model.setEDIT_GIS_ID(cursor.getString(6));        // Update index as per the actual table schema
                    model.setEDIT_TYPE(cursor.getString(7));        // Update index as per the actual table schema
                    data.add(model);
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        } finally {
            db.close();
        }
        return data;
    }

    public boolean deleteEditLocationsData() {
        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.delete(EDIT_TABLE, null, null);
        //System.out.println("DELETE RESULT : " + result);
        if (result > 0) {
            return true;
        } else {
            return false;
        }

    }

    public boolean deleteSingleTransmittedLocation(String location_id) {
        String whereClause = "EDIT_LOCATION_ID = ?";
        String[] whereArgs = {location_id};
        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.delete(EDIT_TABLE, whereClause, whereArgs);
        if (result > 0) {
            return true;
        } else {
            return false;
        }

    }

    public ArrayList<EditModel> getSingleLocationData(String location_id) {
        ArrayList<EditModel> data = new ArrayList<>();
        // String query = "SELECT * FROM EDIT_TABLE";
        String query = "SELECT * FROM EDIT_TABLE WHERE EDIT_LOCATION_ID = '" + location_id + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    EditModel model = new EditModel();
                    model.setEDIT_EST_NO(cursor.getString(0));        // Update index as per the actual table schema
                    model.setEDIT_POLE_NO(cursor.getString(1)); // Update index as per the actual table schema
                    model.setEDIT_POLE_LAT(cursor.getString(2));        // Update index as per the actual table schema
                    model.setEDIT_POLE_LON(cursor.getString(3));        // Update index as per the actual table schema
                    model.setEDIT_VOLTAGE_LEVEL(cursor.getString(4));        // Update index as per the actual table schema
                    model.setEDIT_LOCATION_ID(cursor.getString(5));        // Update index as per the actual table schema
                    model.setEDIT_GIS_ID(cursor.getString(6));        // Update index as per the actual table schema
                    model.setEDIT_TYPE(cursor.getString(7));        // Update index as per the actual table schema
                    data.add(model);
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        } finally {
            db.close();
        }
        return data;
    }


    public boolean updateOnlyLocationBeforeTransmit(String est_no, String pole_no, String lat, String lon, String gis_id) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(LAT_NEW, lat);
        contentValues.put(LON_NEW, lon);
        String whereClause = "GIS_ID_NEW = ? AND POLE_NO_NEW = ?";
        String[] whereArgs = {gis_id, pole_no};
        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.update(SURVEY_TABLE_NEW, contentValues, whereClause, whereArgs);
        return result > 0;
    }

    public boolean deleteOnlySurveyedData() {

        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = IS_NEW_SURVEY + " = ? AND " + IS_TRANSMITTED_LOCATION + " = ?";
        String[] whereArgs = {"Y", "N"};
        int result = db.delete(SURVEY_TABLE_NEW, whereClause, whereArgs);

        if (result > 0) {
            //       Log.d("SURVEYD TABLE", result + " rows deleted.");
            return true;
        } else {
            //   Log.d("SURVEYD TABLE", "No rows matched the conditions.");
            return false;
        }

    }

    public boolean deleteOnlyTransmittedData() {

        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = IS_NEW_SURVEY + " = ? AND " + IS_TRANSMITTED_LOCATION + " = ?";
        String[] whereArgs = {"Y", "Y"};
        int result = db.delete(SURVEY_TABLE_NEW, whereClause, whereArgs);

        if (result > 0) {
            // Log.d("SURVEYD TABLE", result + " rows deleted.");
            return true;
        } else {
            // Log.d("SURVEYD TABLE", "No rows matched the conditions.");
            return false;
        }

    }

    public ArrayList<NewModel> getSpecificCurrentSurveyData() {
        ArrayList<NewModel> data = new ArrayList<>();
        // String query = "";
        //  query = "SELECT * FROM SURVEY_TABLE_NEW WHERE ";
        // String query = "SELECT * FROM SURVEY_TABLE_NEW WHERE IS_NEW_SURVEY = 'Y'";
        String query = "SELECT * FROM SURVEY_TABLE_NEW WHERE IS_NEW_SURVEY = 'Y' AND SURVEY_FROM = '" + LoginActivity.SURVEY_FROM + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    NewModel model = new NewModel();
                    model.setEST_NO(cursor.getString(0));
                    model.setFROM_POLE(cursor.getString(1));
                    model.setPOLE_NO(cursor.getString(2));
                    model.setLINE_TYPE(cursor.getString(3));
                    model.setPOLE_SURVEY_TYPE(cursor.getString(4));
                    model.setIS_MULTI_CKT(cursor.getString(5));
                    model.setMULTI_CKT_33(cursor.getString(6));
                    model.setMULTI_CKT_11(cursor.getString(7));
                    model.setMULTI_CKT_LT(cursor.getString(8));
                    model.setPOLE_TYPE(cursor.getString(9));
                    model.setPOLE_SUPPORT(cursor.getString(10));
                    model.setPOLE_CONFIG(cursor.getString(11));
                    model.setREF_ID(cursor.getString(12));
                    model.setLATITUDE(cursor.getString(13));
                    model.setLONGITUDE(cursor.getString(14));
                    model.setVOLTAGE_LEVEL(cursor.getString(15));
                    model.setIS_EXISTING_LOCATION_NEW(cursor.getString(16));
                    model.setEXISTING_LOCATION_33_NEW(cursor.getString(17));
                    model.setEXISTING_LOCATION_11_NEW(cursor.getString(18));
                    model.setEXISTING_LOCATION_LT_NEW(cursor.getString(19));
                    model.setTAPPING_LOCATION(cursor.getString(20));
                    model.setFROM_POLE_LATITUDE(cursor.getString(21));
                    model.setFROM_POLE_LONGITUDE(cursor.getString(22));
                    model.setMULTI_CKT_33_POLE_NO(cursor.getString(23));
                    model.setMULTI_CKT_11_POLE_NO(cursor.getString(24));
                    model.setMULTI_CKT_LT_POLE_NO(cursor.getString(25));
                    model.setNo_OF_STRUT(cursor.getString(26));
                    model.setNo_OF_STAY(cursor.getString(27));
                    model.setEXI_POLE_NO(cursor.getString(28));
                    model.setPOLE_LOCATION_TYPE(cursor.getString(29));
                    model.setPOLE_HEIGHT(cursor.getString(30));
                    model.setCONDUCTOR_SIZE(cursor.getString(31));
                    model.setLINE_SURVEY_TYPE(cursor.getString(32));
                    model.setDTR_PHASE(cursor.getString(33));
                    model.setDTR_CAPACITY(cursor.getString(34));
                    model.setDTR_MOUNTING(cursor.getString(35));
                    model.setDTR_SURVEY_TYPE(cursor.getString(36));
                    model.setLOCATION_ID(cursor.getString(37));
                    model.setNUMB_9M_STRUTS(cursor.getInt(38));
                    model.setNUMB_8M_STRUTS(cursor.getInt(39));
                    model.setNUMB_STAYS(cursor.getInt(40));
                    model.setNUMBERS_OF_FEEDERS(cursor.getString(41));
                    model.setDTR_STRUCTURE_CODE(cursor.getString(42));
                    model.setPOLE_DESC(cursor.getString(43));
                    model.setPOLE_SAP_CODE(cursor.getString(44));
                    model.setCONDUCTOR_DESC(cursor.getString(45));
                    model.setCONDUCTOR_SAP_CODE(cursor.getString(46));
                    model.setDTR_DESC(cursor.getString(47));
                    model.setDTR_SAP_CODE(cursor.getString(48));
                    model.setAB_SWITCH(cursor.getString(49));
                    model.setAB_DESC(cursor.getString(50));
                    model.setSS_ID(cursor.getString(51));
                    model.setSS_NAME(cursor.getString(52));
                    model.setSS_TYPE(cursor.getString(53));
                    model.setFEEDER_ID(cursor.getString(54));
                    model.setFEEDER_NAME(cursor.getString(55));
                    model.setDTR_AB_DESC(cursor.getString(56));
                    model.setDTR_AB_CODE(cursor.getString(57));
                    model.setHG_FUSE_DESC(cursor.getString(58));
                    model.setHG_FUSE_CODE(cursor.getString(59));
                    model.setLT_FUSE_DESC(cursor.getString(60));
                    model.setLT_FUSE_CODE(cursor.getString(61));
                    model.setCI_SAP_CODE(cursor.getString(62));
                    model.setCI_DESC(cursor.getString(63));
                    model.setGI_SAP_CODE(cursor.getString(64));
                    model.setGI_DESC(cursor.getString(65));
                    model.setCOUNT_CI(cursor.getInt(66));
                    model.setCOUNT_GI(cursor.getInt(67));
                    model.setCOUNT_TAPPING_XARM(cursor.getInt(68));
                    model.setCOUNT_V_XARM(cursor.getInt(69));
                    model.setCOUNT_TOP_CLEAT(cursor.getInt(70));
                    model.setCOUNT_PIN_INS(cursor.getInt(71));
                    model.setCOUNT_DISC_INS(cursor.getInt(72));
                    model.setCOUNT_SIDE_ARM(cursor.getInt(73));
                    model.setCOUNT_METALS_PARTS(cursor.getInt(74));
                    model.setTAPPINGXARM_CODE(cursor.getString(75));
                    model.setTAPPINGXARM_DESC(cursor.getString(76));
                    model.setVXARM_CODE(cursor.getString(77));
                    model.setVXARM_DESC(cursor.getString(78));
                    model.setSIDEARM_CODE(cursor.getString(79));
                    model.setSIDEARM_DESC(cursor.getString(80));
                    model.setCHANNEL_CODE(cursor.getString(81));
                    model.setCHANNEL_DESC(cursor.getString(82));
                    model.setTOPCLEAT_CODE(cursor.getString(83));
                    model.setTOPCLEAT_DESC(cursor.getString(84));
                    model.setPIN_INS_CODE(cursor.getString(85));
                    model.setPIN_INS_DESC(cursor.getString(86));
                    model.setCOUNT_PIN(cursor.getString(87));
                    model.setDISC_INS_CODE(cursor.getString(88));
                    model.setDISC_INS_DESC(cursor.getString(89));
                    model.setCOUNT_DISC(cursor.getString(90));
                    model.setGIPIN_CODE(cursor.getString(91));
                    model.setGIPIN_DESC(cursor.getString(92));
                    model.setCOUNT_GIPIN(cursor.getString(93));
                    model.setMETAL_PARTS_CODE(cursor.getString(94));
                    model.setMETAL_PARTS_DESC(cursor.getString(95));
                    model.setCOUNT_METAL_PARTS(cursor.getInt(96));
                    model.setCOUNT_CHANNEL(cursor.getInt(97));
                    model.setBACK_CLAMPS_CODE(cursor.getString(98));
                    model.setBACK_CLAMPS_DESC(cursor.getString(99));
                    model.setCOUNT_BACK_CLAMPS(cursor.getInt(100));
                    model.setSTAYCLAMP_11M_DESC(cursor.getString(101));
                    model.setSTAYCLAMP_11M_CODE(cursor.getString(102));
                    model.setCOUNT__STAYCLAMP_11M(cursor.getInt(103));
                    model.setSTAYCLAMP_9M_DESC(cursor.getString(104));
                    model.setSTAYCLAMP_9M_CODE(cursor.getString(105));
                    model.setCOUNT__STAYCLAMP_9M(cursor.getInt(106));
                    model.setSTAYCLAMP_8M_DESC(cursor.getString(107));
                    model.setSTAYCLAMP_8M_CODE(cursor.getString(108));
                    model.setCOUNT__STAYCLAMP_8M(cursor.getInt(109));
                    model.setSTAYCLAMP_SPUN_DESC(cursor.getString(110));
                    model.setSTAYCLAMP_SPUN_CODE(cursor.getString(111));
                    model.setCOUNT__STAYCLAMP_SPUN(cursor.getInt(112));
                    model.setGI_PIN_CODE(cursor.getString(113));
                    model.setGI_PIN_DESC(cursor.getString(114));
                    model.setCOUNT_GI_PIN(cursor.getInt(115));
                    model.setNEW_DATA_ADDED(cursor.getString(116));
                    model.setDTR_MOUNTING_DESC(cursor.getString(117));
                    model.setDTR_MOUNTING_CODE(cursor.getString(118));
                    model.setAB_MOUNTING_DESC(cursor.getString(119));
                    model.setAB_MOUNTING_CODE(cursor.getString(120));
                    model.setMT_CODE(cursor.getString(121));
                    model.setMT_DESC(cursor.getString(122));
                    model.setCOUNT_MT(cursor.getInt(123));
                    model.setMT_LOC_NO(cursor.getString(124));
                    model.setMT_SURVEY_TYPE(cursor.getString(125));
                    model.setPIN_CUT(cursor.getString(126));
                    model.setEND_POLE(cursor.getString(127));
                    model.setIS_NEW_SURVEY(cursor.getString(128));
                    model.setSERVICE_POLE(cursor.getString(129));
                    model.setEXT_LOCATION(cursor.getString(130));
                    model.setPOLE_CONCRETE(cursor.getString(131));
                    model.setPOLE_STRUCTURE(cursor.getString(132));
                    model.setSURVEY_FROM(cursor.getString(133));
                    model.setWORK_ORDER(cursor.getString(134));
                    model.setGIS_ID(cursor.getString(135));
                    model.setHTSC_NO(cursor.getString(136));
                    //locations edit
                    model.setIS_TRANSMITTED_DATA_UPDATED(cursor.getString(137));
                    model.setIS_TRANSMITTED_LOCATION(cursor.getString(138));
                    model.setREF_VOLTAGE(cursor.getString(139));
                    model.setEXT_ID(cursor.getString(140));
                    model.setPURPOSE(cursor.getString(141));
                    model.setLT_AVAILABLE(cursor.getString(142));
                    model.setCOUNT_CKT(cursor.getInt(143));
                    model.setLOAD_TYPE(cursor.getString(144));
                    model.setINTERMEDIATE_POLE(cursor.getString(145));
                    model.setOTHER_FEEDER(cursor.getString(146));
                    model.setCROSS_6_3KV_ID(cursor.getString(147));
                    model.setCROSSING_TYPE(cursor.getString(148));
                    model.setCROSS_33KV_ID(cursor.getString(149));
                    model.setCROSS_LT_CKT(cursor.getString(150));
                    model.setCROSS_COMMUN(cursor.getString(151));
                    model.setCROSS_EHT(cursor.getString(152));
                    model.setCROSS_REMARKS(cursor.getString(153));
                    model.setCROSS_11KV_ID(cursor.getString(154));
                    model.setREMARKS(cursor.getString(155));
                    model.setPOLE_PAINT(cursor.getString(156));
                    model.setCURRENT_LAT(cursor.getString(157));
                    model.setCURRENT_LONG(cursor.getString(158));
                    model.setAPP_VERSION(cursor.getString(159));
                    model.setAPP_VERSION2(cursor.getString(160));
                    model.setINTID1(cursor.getString(161));
                    model.setINTID2(cursor.getString(162));
                    model.setDTR_SNO(cursor.getString(163));
                    model.setDTR_MAKE(cursor.getString(164));
                    model.setDTR_LOCATION(cursor.getString(165));
                    model.setEDTR_PHASE(cursor.getString(166));
                    model.setEDTR_CAPACITY(cursor.getString(167));
                    model.setEDTR_EDITED(cursor.getString(168));
                    model.setSERVICE_TYPE(cursor.getString(169));
                    model.setDTR_SHIFTED(cursor.getString(170));
                    model.setNEW_CKT(cursor.getString(171));
                    model.setREG_NUM(cursor.getString(172));
                    model.setDTR_ID(cursor.getString(173));
                    model.setDT_SHIFT_REMARKS(cursor.getString(174));
                    model.setSURVEY_MODE(cursor.getString(175));
                    model.setCTPT(cursor.getString(176));
                    model.setNEW_MAPPED(cursor.getString(177));
                    model.setE_POLE(cursor.getString(178));
                    model.setFDR_LOC_NO_NUMBER(cursor.getString(179));
                    model.setFDR_SOURCE_NO_NUMBER(cursor.getString(180));
                    model.setLTCKT_LOC_NO_NUMBER(cursor.getString(181));
                    model.setLTCKT_SOURCE_NO_NUMBER(cursor.getString(182));
                    model.setGPS_ACCURACY(cursor.getString(183));
                    model.setDEVICE_ID(cursor.getString(184));
                    model.setRDSS_SCHEME_TYPE(cursor.getString(185));
                    model.setPACKAGE_NO(cursor.getString(186));
                    model.setDT_VOLATAGE_RATIO(cursor.getString(187));
                    model.setDT_MANF_DT(cursor.getString(188));
                    model.setDT_INSTL_DT(cursor.getString(189));
                    model.setIMAGE_1(cursor.getString(190));
                    model.setIMAGE_2(cursor.getString(191));
                    model.setIMAGE_3(cursor.getString(192));
                    model.setIS_IMAGE_TRANSMITTED(cursor.getString(193));
                    model.setADDRESS_1(cursor.getString(194));
                    model.setADDRESS_2(cursor.getString(195));
                    model.setASSET_TYPE(cursor.getString(196));
                    model.setBLOCK_PREMISES(cursor.getString(197));
                    model.setVILLAGE(cursor.getString(198));
                    model.setTOWN_CITY(cursor.getString(199));
                    model.setSTATE(cursor.getString(200));
                    model.setBRANCH_POINT(cursor.getString(201));
                    model.setACTUAL_LENGTH(cursor.getString(202));
                    model.setTARGET_FEEDER_ID(cursor.getString(203));
                    model.setTARGET_FEEDER_NAME(cursor.getString(204));
                    model.setREPEAT_POLE(cursor.getString(205));


                    data.add(model);
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return data;
    }


    public ArrayList<NewModel> getLocalSurveyedData() {
        ArrayList<NewModel> data = new ArrayList<>();
        // String query = "";
        //  query = "SELECT * FROM SURVEY_TABLE_NEW WHERE ";
        // String query = "SELECT * FROM SURVEY_TABLE_NEW WHERE IS_NEW_SURVEY = 'Y'";
        String query = "SELECT * FROM SURVEY_TABLE_NEW WHERE IS_NEW_SURVEY = 'Y' AND IS_TRANSMITTED_LOCATION = 'N'";

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    NewModel model = new NewModel();
                    model.setEST_NO(cursor.getString(0));
                    model.setFROM_POLE(cursor.getString(1));
                    model.setPOLE_NO(cursor.getString(2));
                    model.setLINE_TYPE(cursor.getString(3));
                    model.setPOLE_SURVEY_TYPE(cursor.getString(4));
                    model.setIS_MULTI_CKT(cursor.getString(5));
                    model.setMULTI_CKT_33(cursor.getString(6));
                    model.setMULTI_CKT_11(cursor.getString(7));
                    model.setMULTI_CKT_LT(cursor.getString(8));
                    model.setPOLE_TYPE(cursor.getString(9));
                    model.setPOLE_SUPPORT(cursor.getString(10));
                    model.setPOLE_CONFIG(cursor.getString(11));
                    model.setREF_ID(cursor.getString(12));
                    model.setLATITUDE(cursor.getString(13));
                    model.setLONGITUDE(cursor.getString(14));
                    model.setVOLTAGE_LEVEL(cursor.getString(15));
                    model.setIS_EXISTING_LOCATION_NEW(cursor.getString(16));
                    model.setEXISTING_LOCATION_33_NEW(cursor.getString(17));
                    model.setEXISTING_LOCATION_11_NEW(cursor.getString(18));
                    model.setEXISTING_LOCATION_LT_NEW(cursor.getString(19));
                    model.setTAPPING_LOCATION(cursor.getString(20));
                    model.setFROM_POLE_LATITUDE(cursor.getString(21));
                    model.setFROM_POLE_LONGITUDE(cursor.getString(22));
                    model.setMULTI_CKT_33_POLE_NO(cursor.getString(23));
                    model.setMULTI_CKT_11_POLE_NO(cursor.getString(24));
                    model.setMULTI_CKT_LT_POLE_NO(cursor.getString(25));
                    model.setNo_OF_STRUT(cursor.getString(26));
                    model.setNo_OF_STAY(cursor.getString(27));
                    model.setEXI_POLE_NO(cursor.getString(28));
                    model.setPOLE_LOCATION_TYPE(cursor.getString(29));
                    model.setPOLE_HEIGHT(cursor.getString(30));
                    model.setCONDUCTOR_SIZE(cursor.getString(31));
                    model.setLINE_SURVEY_TYPE(cursor.getString(32));
                    model.setDTR_PHASE(cursor.getString(33));
                    model.setDTR_CAPACITY(cursor.getString(34));
                    model.setDTR_MOUNTING(cursor.getString(35));
                    model.setDTR_SURVEY_TYPE(cursor.getString(36));
                    model.setLOCATION_ID(cursor.getString(37));
                    model.setNUMB_9M_STRUTS(cursor.getInt(38));
                    model.setNUMB_8M_STRUTS(cursor.getInt(39));
                    model.setNUMB_STAYS(cursor.getInt(40));
                    model.setNUMBERS_OF_FEEDERS(cursor.getString(41));
                    model.setDTR_STRUCTURE_CODE(cursor.getString(42));
                    model.setPOLE_DESC(cursor.getString(43));
                    model.setPOLE_SAP_CODE(cursor.getString(44));
                    model.setCONDUCTOR_DESC(cursor.getString(45));
                    model.setCONDUCTOR_SAP_CODE(cursor.getString(46));
                    model.setDTR_DESC(cursor.getString(47));
                    model.setDTR_SAP_CODE(cursor.getString(48));
                    model.setAB_SWITCH(cursor.getString(49));
                    model.setAB_DESC(cursor.getString(50));
                    model.setSS_ID(cursor.getString(51));
                    model.setSS_NAME(cursor.getString(52));
                    model.setSS_TYPE(cursor.getString(53));
                    model.setFEEDER_ID(cursor.getString(54));
                    model.setFEEDER_NAME(cursor.getString(55));
                    model.setDTR_AB_DESC(cursor.getString(56));
                    model.setDTR_AB_CODE(cursor.getString(57));
                    model.setHG_FUSE_DESC(cursor.getString(58));
                    model.setHG_FUSE_CODE(cursor.getString(59));
                    model.setLT_FUSE_DESC(cursor.getString(60));
                    model.setLT_FUSE_CODE(cursor.getString(61));
                    model.setCI_SAP_CODE(cursor.getString(62));
                    model.setCI_DESC(cursor.getString(63));
                    model.setGI_SAP_CODE(cursor.getString(64));
                    model.setGI_DESC(cursor.getString(65));
                    model.setCOUNT_CI(cursor.getInt(66));
                    model.setCOUNT_GI(cursor.getInt(67));
                    model.setCOUNT_TAPPING_XARM(cursor.getInt(68));
                    model.setCOUNT_V_XARM(cursor.getInt(69));
                    model.setCOUNT_TOP_CLEAT(cursor.getInt(70));
                    model.setCOUNT_PIN_INS(cursor.getInt(71));
                    model.setCOUNT_DISC_INS(cursor.getInt(72));
                    model.setCOUNT_SIDE_ARM(cursor.getInt(73));
                    model.setCOUNT_METALS_PARTS(cursor.getInt(74));
                    model.setTAPPINGXARM_CODE(cursor.getString(75));
                    model.setTAPPINGXARM_DESC(cursor.getString(76));
                    model.setVXARM_CODE(cursor.getString(77));
                    model.setVXARM_DESC(cursor.getString(78));
                    model.setSIDEARM_CODE(cursor.getString(79));
                    model.setSIDEARM_DESC(cursor.getString(80));
                    model.setCHANNEL_CODE(cursor.getString(81));
                    model.setCHANNEL_DESC(cursor.getString(82));
                    model.setTOPCLEAT_CODE(cursor.getString(83));
                    model.setTOPCLEAT_DESC(cursor.getString(84));
                    model.setPIN_INS_CODE(cursor.getString(85));
                    model.setPIN_INS_DESC(cursor.getString(86));
                    model.setCOUNT_PIN(cursor.getString(87));
                    model.setDISC_INS_CODE(cursor.getString(88));
                    model.setDISC_INS_DESC(cursor.getString(89));
                    model.setCOUNT_DISC(cursor.getString(90));
                    model.setGIPIN_CODE(cursor.getString(91));
                    model.setGIPIN_DESC(cursor.getString(92));
                    model.setCOUNT_GIPIN(cursor.getString(93));
                    model.setMETAL_PARTS_CODE(cursor.getString(94));
                    model.setMETAL_PARTS_DESC(cursor.getString(95));
                    model.setCOUNT_METAL_PARTS(cursor.getInt(96));
                    model.setCOUNT_CHANNEL(cursor.getInt(97));
                    model.setBACK_CLAMPS_CODE(cursor.getString(98));
                    model.setBACK_CLAMPS_DESC(cursor.getString(99));
                    model.setCOUNT_BACK_CLAMPS(cursor.getInt(100));
                    model.setSTAYCLAMP_11M_DESC(cursor.getString(101));
                    model.setSTAYCLAMP_11M_CODE(cursor.getString(102));
                    model.setCOUNT__STAYCLAMP_11M(cursor.getInt(103));
                    model.setSTAYCLAMP_9M_DESC(cursor.getString(104));
                    model.setSTAYCLAMP_9M_CODE(cursor.getString(105));
                    model.setCOUNT__STAYCLAMP_9M(cursor.getInt(106));
                    model.setSTAYCLAMP_8M_DESC(cursor.getString(107));
                    model.setSTAYCLAMP_8M_CODE(cursor.getString(108));
                    model.setCOUNT__STAYCLAMP_8M(cursor.getInt(109));
                    model.setSTAYCLAMP_SPUN_DESC(cursor.getString(110));
                    model.setSTAYCLAMP_SPUN_CODE(cursor.getString(111));
                    model.setCOUNT__STAYCLAMP_SPUN(cursor.getInt(112));
                    model.setGI_PIN_CODE(cursor.getString(113));
                    model.setGI_PIN_DESC(cursor.getString(114));
                    model.setCOUNT_GI_PIN(cursor.getInt(115));
                    model.setNEW_DATA_ADDED(cursor.getString(116));
                    model.setDTR_MOUNTING_DESC(cursor.getString(117));
                    model.setDTR_MOUNTING_CODE(cursor.getString(118));
                    model.setAB_MOUNTING_DESC(cursor.getString(119));
                    model.setAB_MOUNTING_CODE(cursor.getString(120));
                    model.setMT_CODE(cursor.getString(121));
                    model.setMT_DESC(cursor.getString(122));
                    model.setCOUNT_MT(cursor.getInt(123));
                    model.setMT_LOC_NO(cursor.getString(124));
                    model.setMT_SURVEY_TYPE(cursor.getString(125));
                    model.setPIN_CUT(cursor.getString(126));
                    model.setEND_POLE(cursor.getString(127));
                    model.setIS_NEW_SURVEY(cursor.getString(128));
                    model.setSERVICE_POLE(cursor.getString(129));
                    model.setEXT_LOCATION(cursor.getString(130));
                    model.setPOLE_CONCRETE(cursor.getString(131));
                    model.setPOLE_STRUCTURE(cursor.getString(132));
                    model.setSURVEY_FROM(cursor.getString(133));
                    model.setWORK_ORDER(cursor.getString(134));
                    model.setGIS_ID(cursor.getString(135));
                    model.setHTSC_NO(cursor.getString(136));
                    //locations edit
                    model.setIS_TRANSMITTED_DATA_UPDATED(cursor.getString(137));
                    model.setIS_TRANSMITTED_LOCATION(cursor.getString(138));
                    model.setREF_VOLTAGE(cursor.getString(139));
                    model.setEXT_ID(cursor.getString(140));
                    model.setPURPOSE(cursor.getString(141));
                    model.setLT_AVAILABLE(cursor.getString(142));
                    model.setCOUNT_CKT(cursor.getInt(143));
                    model.setLOAD_TYPE(cursor.getString(144));
                    model.setINTERMEDIATE_POLE(cursor.getString(145));
                    model.setOTHER_FEEDER(cursor.getString(146));
                    model.setCROSS_6_3KV_ID(cursor.getString(147));
                    model.setCROSSING_TYPE(cursor.getString(148));
                    model.setCROSS_33KV_ID(cursor.getString(149));
                    model.setCROSS_LT_CKT(cursor.getString(150));
                    model.setCROSS_COMMUN(cursor.getString(151));
                    model.setCROSS_EHT(cursor.getString(152));
                    model.setCROSS_REMARKS(cursor.getString(153));
                    model.setCROSS_11KV_ID(cursor.getString(154));
                    model.setREMARKS(cursor.getString(155));
                    model.setPOLE_PAINT(cursor.getString(156));
                    model.setCURRENT_LAT(cursor.getString(157));
                    model.setCURRENT_LONG(cursor.getString(158));
                    model.setAPP_VERSION(cursor.getString(159));
                    model.setAPP_VERSION2(cursor.getString(160));
                    model.setINTID1(cursor.getString(161));
                    model.setINTID2(cursor.getString(162));
                    model.setDTR_SNO(cursor.getString(163));
                    model.setDTR_MAKE(cursor.getString(164));
                    model.setDTR_LOCATION(cursor.getString(165));
                    model.setEDTR_PHASE(cursor.getString(166));
                    model.setEDTR_CAPACITY(cursor.getString(167));
                    model.setEDTR_EDITED(cursor.getString(168));
                    model.setSERVICE_TYPE(cursor.getString(169));
                    model.setDTR_SHIFTED(cursor.getString(170));
                    model.setNEW_CKT(cursor.getString(171));
                    model.setREG_NUM(cursor.getString(172));
                    model.setDTR_ID(cursor.getString(173));
                    model.setDT_SHIFT_REMARKS(cursor.getString(174));
                    model.setSURVEY_MODE(cursor.getString(175));
                    model.setCTPT(cursor.getString(176));
                    model.setNEW_MAPPED(cursor.getString(177));
                    model.setE_POLE(cursor.getString(178));
                    model.setFDR_LOC_NO_NUMBER(cursor.getString(179));
                    model.setFDR_SOURCE_NO_NUMBER(cursor.getString(180));
                    model.setLTCKT_LOC_NO_NUMBER(cursor.getString(181));
                    model.setLTCKT_SOURCE_NO_NUMBER(cursor.getString(182));
                    model.setGPS_ACCURACY(cursor.getString(183));
                    model.setDEVICE_ID(cursor.getString(184));
                    model.setRDSS_SCHEME_TYPE(cursor.getString(185));
                    model.setPACKAGE_NO(cursor.getString(186));
                    model.setDT_VOLATAGE_RATIO(cursor.getString(187));
                    model.setDT_MANF_DT(cursor.getString(188));
                    model.setDT_INSTL_DT(cursor.getString(189));
                    model.setIMAGE_1(cursor.getString(190));
                    model.setIMAGE_2(cursor.getString(191));
                    model.setIMAGE_3(cursor.getString(192));
                    model.setIS_IMAGE_TRANSMITTED(cursor.getString(193));
                    model.setADDRESS_1(cursor.getString(194));
                    model.setADDRESS_2(cursor.getString(195));
                    model.setASSET_TYPE(cursor.getString(196));
                    model.setBLOCK_PREMISES(cursor.getString(197));
                    model.setVILLAGE(cursor.getString(198));
                    model.setTOWN_CITY(cursor.getString(199));
                    model.setSTATE(cursor.getString(200));
                    model.setBRANCH_POINT(cursor.getString(201));
                    model.setACTUAL_LENGTH(cursor.getString(202));
                    model.setTARGET_FEEDER_ID(cursor.getString(203));
                    model.setTARGET_FEEDER_NAME(cursor.getString(204));
                    model.setREPEAT_POLE(cursor.getString(205));
                    data.add(model);
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return data;
    }


    public ArrayList<NewModel> getTransmittedSurveyedData() {
        ArrayList<NewModel> data = new ArrayList<>();
        // String query = "";
        //  query = "SELECT * FROM SURVEY_TABLE_NEW WHERE ";
        // String query = "SELECT * FROM SURVEY_TABLE_NEW WHERE IS_NEW_SURVEY = 'Y'";
        String query = "SELECT * FROM SURVEY_TABLE_NEW WHERE IS_NEW_SURVEY = 'Y' AND IS_TRANSMITTED_LOCATION = 'Y'";

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    NewModel model = new NewModel();
                    model.setEST_NO(cursor.getString(0));
                    model.setFROM_POLE(cursor.getString(1));
                    model.setPOLE_NO(cursor.getString(2));
                    model.setLINE_TYPE(cursor.getString(3));
                    model.setPOLE_SURVEY_TYPE(cursor.getString(4));
                    model.setIS_MULTI_CKT(cursor.getString(5));
                    model.setMULTI_CKT_33(cursor.getString(6));
                    model.setMULTI_CKT_11(cursor.getString(7));
                    model.setMULTI_CKT_LT(cursor.getString(8));
                    model.setPOLE_TYPE(cursor.getString(9));
                    model.setPOLE_SUPPORT(cursor.getString(10));
                    model.setPOLE_CONFIG(cursor.getString(11));
                    model.setREF_ID(cursor.getString(12));
                    model.setLATITUDE(cursor.getString(13));
                    model.setLONGITUDE(cursor.getString(14));
                    model.setVOLTAGE_LEVEL(cursor.getString(15));
                    model.setIS_EXISTING_LOCATION_NEW(cursor.getString(16));
                    model.setEXISTING_LOCATION_33_NEW(cursor.getString(17));
                    model.setEXISTING_LOCATION_11_NEW(cursor.getString(18));
                    model.setEXISTING_LOCATION_LT_NEW(cursor.getString(19));
                    model.setTAPPING_LOCATION(cursor.getString(20));
                    model.setFROM_POLE_LATITUDE(cursor.getString(21));
                    model.setFROM_POLE_LONGITUDE(cursor.getString(22));
                    model.setMULTI_CKT_33_POLE_NO(cursor.getString(23));
                    model.setMULTI_CKT_11_POLE_NO(cursor.getString(24));
                    model.setMULTI_CKT_LT_POLE_NO(cursor.getString(25));
                    model.setNo_OF_STRUT(cursor.getString(26));
                    model.setNo_OF_STAY(cursor.getString(27));
                    model.setEXI_POLE_NO(cursor.getString(28));
                    model.setPOLE_LOCATION_TYPE(cursor.getString(29));
                    model.setPOLE_HEIGHT(cursor.getString(30));
                    model.setCONDUCTOR_SIZE(cursor.getString(31));
                    model.setLINE_SURVEY_TYPE(cursor.getString(32));
                    model.setDTR_PHASE(cursor.getString(33));
                    model.setDTR_CAPACITY(cursor.getString(34));
                    model.setDTR_MOUNTING(cursor.getString(35));
                    model.setDTR_SURVEY_TYPE(cursor.getString(36));
                    model.setLOCATION_ID(cursor.getString(37));
                    model.setNUMB_9M_STRUTS(cursor.getInt(38));
                    model.setNUMB_8M_STRUTS(cursor.getInt(39));
                    model.setNUMB_STAYS(cursor.getInt(40));
                    model.setNUMBERS_OF_FEEDERS(cursor.getString(41));
                    model.setDTR_STRUCTURE_CODE(cursor.getString(42));
                    model.setPOLE_DESC(cursor.getString(43));
                    model.setPOLE_SAP_CODE(cursor.getString(44));
                    model.setCONDUCTOR_DESC(cursor.getString(45));
                    model.setCONDUCTOR_SAP_CODE(cursor.getString(46));
                    model.setDTR_DESC(cursor.getString(47));
                    model.setDTR_SAP_CODE(cursor.getString(48));
                    model.setAB_SWITCH(cursor.getString(49));
                    model.setAB_DESC(cursor.getString(50));
                    model.setSS_ID(cursor.getString(51));
                    model.setSS_NAME(cursor.getString(52));
                    model.setSS_TYPE(cursor.getString(53));
                    model.setFEEDER_ID(cursor.getString(54));
                    model.setFEEDER_NAME(cursor.getString(55));
                    model.setDTR_AB_DESC(cursor.getString(56));
                    model.setDTR_AB_CODE(cursor.getString(57));
                    model.setHG_FUSE_DESC(cursor.getString(58));
                    model.setHG_FUSE_CODE(cursor.getString(59));
                    model.setLT_FUSE_DESC(cursor.getString(60));
                    model.setLT_FUSE_CODE(cursor.getString(61));
                    model.setCI_SAP_CODE(cursor.getString(62));
                    model.setCI_DESC(cursor.getString(63));
                    model.setGI_SAP_CODE(cursor.getString(64));
                    model.setGI_DESC(cursor.getString(65));
                    model.setCOUNT_CI(cursor.getInt(66));
                    model.setCOUNT_GI(cursor.getInt(67));
                    model.setCOUNT_TAPPING_XARM(cursor.getInt(68));
                    model.setCOUNT_V_XARM(cursor.getInt(69));
                    model.setCOUNT_TOP_CLEAT(cursor.getInt(70));
                    model.setCOUNT_PIN_INS(cursor.getInt(71));
                    model.setCOUNT_DISC_INS(cursor.getInt(72));
                    model.setCOUNT_SIDE_ARM(cursor.getInt(73));
                    model.setCOUNT_METALS_PARTS(cursor.getInt(74));
                    model.setTAPPINGXARM_CODE(cursor.getString(75));
                    model.setTAPPINGXARM_DESC(cursor.getString(76));
                    model.setVXARM_CODE(cursor.getString(77));
                    model.setVXARM_DESC(cursor.getString(78));
                    model.setSIDEARM_CODE(cursor.getString(79));
                    model.setSIDEARM_DESC(cursor.getString(80));
                    model.setCHANNEL_CODE(cursor.getString(81));
                    model.setCHANNEL_DESC(cursor.getString(82));
                    model.setTOPCLEAT_CODE(cursor.getString(83));
                    model.setTOPCLEAT_DESC(cursor.getString(84));
                    model.setPIN_INS_CODE(cursor.getString(85));
                    model.setPIN_INS_DESC(cursor.getString(86));
                    model.setCOUNT_PIN(cursor.getString(87));
                    model.setDISC_INS_CODE(cursor.getString(88));
                    model.setDISC_INS_DESC(cursor.getString(89));
                    model.setCOUNT_DISC(cursor.getString(90));
                    model.setGIPIN_CODE(cursor.getString(91));
                    model.setGIPIN_DESC(cursor.getString(92));
                    model.setCOUNT_GIPIN(cursor.getString(93));
                    model.setMETAL_PARTS_CODE(cursor.getString(94));
                    model.setMETAL_PARTS_DESC(cursor.getString(95));
                    model.setCOUNT_METAL_PARTS(cursor.getInt(96));
                    model.setCOUNT_CHANNEL(cursor.getInt(97));
                    model.setBACK_CLAMPS_CODE(cursor.getString(98));
                    model.setBACK_CLAMPS_DESC(cursor.getString(99));
                    model.setCOUNT_BACK_CLAMPS(cursor.getInt(100));
                    model.setSTAYCLAMP_11M_DESC(cursor.getString(101));
                    model.setSTAYCLAMP_11M_CODE(cursor.getString(102));
                    model.setCOUNT__STAYCLAMP_11M(cursor.getInt(103));
                    model.setSTAYCLAMP_9M_DESC(cursor.getString(104));
                    model.setSTAYCLAMP_9M_CODE(cursor.getString(105));
                    model.setCOUNT__STAYCLAMP_9M(cursor.getInt(106));
                    model.setSTAYCLAMP_8M_DESC(cursor.getString(107));
                    model.setSTAYCLAMP_8M_CODE(cursor.getString(108));
                    model.setCOUNT__STAYCLAMP_8M(cursor.getInt(109));
                    model.setSTAYCLAMP_SPUN_DESC(cursor.getString(110));
                    model.setSTAYCLAMP_SPUN_CODE(cursor.getString(111));
                    model.setCOUNT__STAYCLAMP_SPUN(cursor.getInt(112));
                    model.setGI_PIN_CODE(cursor.getString(113));
                    model.setGI_PIN_DESC(cursor.getString(114));
                    model.setCOUNT_GI_PIN(cursor.getInt(115));
                    model.setNEW_DATA_ADDED(cursor.getString(116));
                    model.setDTR_MOUNTING_DESC(cursor.getString(117));
                    model.setDTR_MOUNTING_CODE(cursor.getString(118));
                    model.setAB_MOUNTING_DESC(cursor.getString(119));
                    model.setAB_MOUNTING_CODE(cursor.getString(120));
                    model.setMT_CODE(cursor.getString(121));
                    model.setMT_DESC(cursor.getString(122));
                    model.setCOUNT_MT(cursor.getInt(123));
                    model.setMT_LOC_NO(cursor.getString(124));
                    model.setMT_SURVEY_TYPE(cursor.getString(125));
                    model.setPIN_CUT(cursor.getString(126));
                    model.setEND_POLE(cursor.getString(127));
                    model.setIS_NEW_SURVEY(cursor.getString(128));
                    model.setSERVICE_POLE(cursor.getString(129));
                    model.setEXT_LOCATION(cursor.getString(130));
                    model.setPOLE_CONCRETE(cursor.getString(131));
                    model.setPOLE_STRUCTURE(cursor.getString(132));
                    model.setSURVEY_FROM(cursor.getString(133));
                    model.setWORK_ORDER(cursor.getString(134));
                    model.setGIS_ID(cursor.getString(135));
                    model.setHTSC_NO(cursor.getString(136));
                    //locations edit
                    model.setIS_TRANSMITTED_DATA_UPDATED(cursor.getString(137));
                    model.setIS_TRANSMITTED_LOCATION(cursor.getString(138));
                    model.setREF_VOLTAGE(cursor.getString(139));
                    model.setEXT_ID(cursor.getString(140));
                    model.setPURPOSE(cursor.getString(141));
                    model.setLT_AVAILABLE(cursor.getString(142));
                    model.setCOUNT_CKT(cursor.getInt(143));
                    model.setLOAD_TYPE(cursor.getString(144));
                    model.setINTERMEDIATE_POLE(cursor.getString(145));
                    model.setOTHER_FEEDER(cursor.getString(146));
                    model.setCROSS_6_3KV_ID(cursor.getString(147));
                    model.setCROSSING_TYPE(cursor.getString(148));
                    model.setCROSS_33KV_ID(cursor.getString(149));
                    model.setCROSS_LT_CKT(cursor.getString(150));
                    model.setCROSS_COMMUN(cursor.getString(151));
                    model.setCROSS_EHT(cursor.getString(152));
                    model.setCROSS_REMARKS(cursor.getString(153));
                    model.setCROSS_11KV_ID(cursor.getString(154));
                    model.setREMARKS(cursor.getString(155));
                    model.setPOLE_PAINT(cursor.getString(156));
                    model.setCURRENT_LAT(cursor.getString(157));
                    model.setCURRENT_LONG(cursor.getString(158));
                    model.setAPP_VERSION(cursor.getString(159));
                    model.setAPP_VERSION2(cursor.getString(160));
                    model.setINTID1(cursor.getString(161));
                    model.setINTID2(cursor.getString(162));
                    model.setDTR_SNO(cursor.getString(163));
                    model.setDTR_MAKE(cursor.getString(164));
                    model.setDTR_LOCATION(cursor.getString(165));
                    model.setEDTR_PHASE(cursor.getString(166));
                    model.setEDTR_CAPACITY(cursor.getString(167));
                    model.setEDTR_EDITED(cursor.getString(168));
                    model.setSERVICE_TYPE(cursor.getString(169));
                    model.setDTR_SHIFTED(cursor.getString(170));
                    model.setNEW_CKT(cursor.getString(171));
                    model.setREG_NUM(cursor.getString(172));
                    model.setDTR_ID(cursor.getString(173));
                    model.setDT_SHIFT_REMARKS(cursor.getString(174));
                    model.setSURVEY_MODE(cursor.getString(175));
                    model.setCTPT(cursor.getString(176));
                    model.setNEW_MAPPED(cursor.getString(177));
                    model.setE_POLE(cursor.getString(178));
                    model.setFDR_LOC_NO_NUMBER(cursor.getString(179));
                    model.setFDR_SOURCE_NO_NUMBER(cursor.getString(180));
                    model.setLTCKT_LOC_NO_NUMBER(cursor.getString(181));
                    model.setLTCKT_SOURCE_NO_NUMBER(cursor.getString(182));
                    model.setGPS_ACCURACY(cursor.getString(183));
                    model.setDEVICE_ID(cursor.getString(184));
                    model.setRDSS_SCHEME_TYPE(cursor.getString(185));
                    model.setPACKAGE_NO(cursor.getString(186));
                    model.setDT_VOLATAGE_RATIO(cursor.getString(187));
                    model.setDT_MANF_DT(cursor.getString(188));
                    model.setDT_INSTL_DT(cursor.getString(189));
                    model.setIMAGE_1(cursor.getString(190));
                    model.setIMAGE_2(cursor.getString(191));
                    model.setIMAGE_3(cursor.getString(192));
                    model.setIS_IMAGE_TRANSMITTED(cursor.getString(193));
                    model.setADDRESS_1(cursor.getString(194));
                    model.setADDRESS_2(cursor.getString(195));
                    model.setASSET_TYPE(cursor.getString(196));
                    model.setBLOCK_PREMISES(cursor.getString(197));
                    model.setVILLAGE(cursor.getString(198));
                    model.setTOWN_CITY(cursor.getString(199));
                    model.setSTATE(cursor.getString(200));
                    model.setBRANCH_POINT(cursor.getString(201));
                    model.setACTUAL_LENGTH(cursor.getString(202));
                    model.setTARGET_FEEDER_ID(cursor.getString(203));
                    model.setTARGET_FEEDER_NAME(cursor.getString(204));
                    model.setREPEAT_POLE(cursor.getString(205));


                    data.add(model);
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return data;
    }

    public ArrayList<MaterialPostingModel> getOngoingMaterialData(String est_no, String gis_id) {
        ArrayList<MaterialPostingModel> data = new ArrayList<>();

        String query = "SELECT * FROM MATERIAL_TABLE WHERE GIS_ID = '" + gis_id + "' AND MT_TYPE = 'ONGOING'";
        // String query = "SELECT * FROM MATERIAL_TABLE WHERE GIS_ID = '" + gis_id + "' AND MT_TYPE = 'TRANSMITTED' DISTICT SAP_CODE,LOCATION_NO";


        //String query = "SELECT * FROM MATERIAL_TABLE WHERE GIS_ID = '" + gis_id + "' AND MT_TYPE = 'ONGOING'";

        // String query = "SELECT * FROM SURVEY_TABLE_NEW WHERE EST_NO_NEW = '" + est_no + "' AND POLE_NO_NEW = '" + pole_no + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    MaterialPostingModel model = new MaterialPostingModel();
                    model.setESTIMATE_NO(cursor.getString(0));        // Update index as per the actual table schema
                    model.setVOLTAGE_LEVEL(cursor.getString(1)); // Update index as per the actual table schema
                    model.setLOCATION_ID(cursor.getString(2));        // Update index as per the actual table schema
                    model.setLOCATION_NO(cursor.getString(3));        // Update index as per the actual table schema
                    model.setMATERIAL(cursor.getString(4));        // Update index as per the actual table schema
                    model.setSAP_CODE(cursor.getString(5));        // Update index as per the actual table schema
                    model.setMATERIAL_DESC(cursor.getString(6));        // Update index as per the actual table schema
                    model.setCOUNT(cursor.getString(7));        // Update index as per the actual table schema
                    model.setSURVEY_CODE(cursor.getString(8));        // Update index as per the actual table schema
                    model.setDATA_ID(cursor.getString(9));        // Update index as per the actual table schema
                    model.setTYPE(cursor.getString(10));        // Update index as per the actual table schema
                    model.setGIS_ID(cursor.getString(11));
                    model.setAPP_VERSION(cursor.getString(12));        // Update index as per the actual table schema
                    model.setAPP_VERSION2(cursor.getString(13));        // Update index as per the actual table schema// Update index as per the actual table schema
                    model.setMOD_TYPE(cursor.getString(14));        // Update index as per the actual table schema// Update index as per the actual table schema
                    model.setREF_ID(cursor.getString(15));        // Update index as per the actual table schema// Update index as per the actual table schema
                    data.add(model);
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        } finally {
            db.close();
        }
        return data;
    }

    public ArrayList<MaterialPostingModel> getTransmittedMaterialData(String est_no, String gis_id) {
        ArrayList<MaterialPostingModel> data = new ArrayList<>();


        String query = "SELECT * FROM MATERIAL_TABLE WHERE GIS_ID = '" + gis_id + "' AND MT_TYPE = 'TRANSMITTED'";
        //System.out.println("TRANSMITTED MATERIAL QUERY : " + query);
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    MaterialPostingModel model = new MaterialPostingModel();
                    model.setESTIMATE_NO(cursor.getString(0));        // Update index as per the actual table schema
                    model.setVOLTAGE_LEVEL(cursor.getString(1)); // Update index as per the actual table schema
                    model.setLOCATION_ID(cursor.getString(2));        // Update index as per the actual table schema
                    model.setLOCATION_NO(cursor.getString(3));        // Update index as per the actual table schema
                    model.setMATERIAL(cursor.getString(4));        // Update index as per the actual table schema
                    model.setSAP_CODE(cursor.getString(5));        // Update index as per the actual table schema
                    model.setMATERIAL_DESC(cursor.getString(6));        // Update index as per the actual table schema
                    model.setCOUNT(cursor.getString(7));        // Update index as per the actual table schema
                    model.setSURVEY_CODE(cursor.getString(8));        // Update index as per the actual table schema
                    model.setDATA_ID(cursor.getString(9));        // Update index as per the actual table schema
                    model.setTYPE(cursor.getString(10));        // Update index as per the actual table schema
                    model.setGIS_ID(cursor.getString(11));
                    model.setAPP_VERSION(cursor.getString(12));        // Update index as per the actual table schema
                    model.setAPP_VERSION2(cursor.getString(13));        // Update index as per the actual table schema// Update index as per the actual table schema
                    model.setMOD_TYPE(cursor.getString(14));        // Update index as per the actual table schema// Update index as per the actual table schema
                    model.setREF_ID(cursor.getString(15));        // Update index as per the actual table schema// Update index as per the actual table schema
                    data.add(model);
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        } finally {
            db.close();
        }
        return data;
    }

    public boolean deleteTransmittedMaterialData(String EST_NO, String GIS_ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = GIS_ID + " = ? AND " + MT_TYPE + " = ?";
        String[] whereArgs = {GIS_ID, "TRANSMITTED"};
        int result = db.delete(MATERIAL_TABLE, whereClause, whereArgs);

        if (result > 0) {
            //    Log.d("TRANSMITTED TABLE", result + " rows deleted.");
            return true;
        } else {
            //    Log.d("TRANSMITTED TABLE", "No rows matched the conditions.");
            return false;
        }
    }

    public boolean deleteOngoingMaterialData(String EST_NO, String GIS_ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = GIS_ID + " = ? AND " + MT_TYPE + " = ?";
        String[] whereArgs = {GIS_ID, "ONGOING"};
        int result = db.delete(MATERIAL_TABLE, whereClause, whereArgs);

        if (result > 0) {
            //   Log.d("ONGOING TABLE", result + " rows deleted.");
            return true;
        } else {
            //    Log.d("ONGOING TABLE", "No rows matched the conditions.");
            return false;
        }
    }

    public boolean deleteSinglePoleMaterialData(String est_no, String location_no, String gis_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = GIS_ID + " = ? AND " + MT_LOCATION_NO + " = ?";
        String[] whereArgs = {gis_id, location_no};
        int result = db.delete(MATERIAL_TABLE, whereClause, whereArgs);

        if (result > 0) {
            //    Log.d("MATERIAL TABLE", result + " rows deleted.");
            return true;
        } else {
            //     Log.d("MATERIAL TABLE", "No rows matched the conditions.");
            return false;
        }
    }


    public boolean getFeeder1SurveyStatusNEW33(String est_no, String gis_id) {
        boolean status = false;
        String surveyStatus;

        String query = "SELECT IS_33KV_FEEDER_1_SURVEY_COMPLETED FROM STATUS_TABLE WHERE GIS_ID = '" + gis_id + "'";
        //System.out.println("GET SURVEY STATUS NEW QUERY : " + query);
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    surveyStatus = cursor.getString(0);
                    //System.out.println("SURVEY STATUS : " + surveyStatus);
                    if (surveyStatus.equalsIgnoreCase("Y")) {
                        status = true;
                    } else {
                        status = false;
                    }
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return status;
    }

    public boolean getFeeder2SurveyStatusNEW33(String est_no, String gis_id) {
        boolean status = false;
        String surveyStatus;

        String query = "SELECT IS_33KV_FEEDER_2_SURVEY_COMPLETED FROM STATUS_TABLE WHERE GIS_ID = '" + gis_id + "'";
        //System.out.println("GET SURVEY STATUS NEW QUERY : " + query);
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    surveyStatus = cursor.getString(0);
                    //System.out.println("SURVEY STATUS : " + surveyStatus);
                    if (surveyStatus.equalsIgnoreCase("Y")) {
                        status = true;
                    } else {
                        status = false;
                    }
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return status;
    }

    public boolean getFeeder3SurveyStatusNEW33(String est_no, String gis_id) {
        boolean status = false;
        String surveyStatus;

        String query = "SELECT IS_33KV_FEEDER_3_SURVEY_COMPLETED FROM STATUS_TABLE WHERE GIS_ID = '" + gis_id + "'";
        //System.out.println("GET SURVEY STATUS NEW QUERY : " + query);
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    surveyStatus = cursor.getString(0);
                    //System.out.println("SURVEY STATUS : " + surveyStatus);
                    if (surveyStatus.equalsIgnoreCase("Y")) {
                        status = true;
                    } else {
                        status = false;
                    }
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return status;
    }

    public boolean getFeeder4SurveyStatusNEW33(String est_no, String gis_id) {
        boolean status = false;
        String surveyStatus;

        String query = "SELECT IS_33KV_FEEDER_4_SURVEY_COMPLETED FROM STATUS_TABLE WHERE GIS_ID = '" + gis_id + "'";
        //System.out.println("GET SURVEY STATUS NEW QUERY : " + query);
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    surveyStatus = cursor.getString(0);
                    //System.out.println("SURVEY STATUS : " + surveyStatus);
                    if (surveyStatus.equalsIgnoreCase("Y")) {
                        status = true;
                    } else {
                        status = false;
                    }
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return status;
    }

    public boolean getFeeder5SurveyStatusNEW33(String est_no, String gis_id) {
        boolean status = false;
        String surveyStatus;

        String query = "SELECT IS_33KV_FEEDER_5_SURVEY_COMPLETED FROM STATUS_TABLE WHERE GIS_ID = '" + gis_id + "'";
        //System.out.println("GET SURVEY STATUS NEW QUERY : " + query);
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    surveyStatus = cursor.getString(0);
                    //System.out.println("SURVEY STATUS : " + surveyStatus);
                    if (surveyStatus.equalsIgnoreCase("Y")) {
                        status = true;
                    } else {
                        status = false;
                    }
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return status;
    }


    public boolean update33KVFeeder1Status(String est_no, boolean isSurveyCompleted, String gis_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(IS_33KV_FEEDER_1_SURVEY_COMPLETED, isSurveyCompleted ? "Y" : "N");
        String whereClause = "GIS_ID = ?";
        String[] whereArgs = {gis_id};
        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.update(STATUS_TABLE, contentValues, whereClause, whereArgs);
        return result > 0;
    }

    public boolean update33KVFeeder2Status(String est_no, boolean isSurveyCompleted, String gis_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(IS_33KV_FEEDER_2_SURVEY_COMPLETED, isSurveyCompleted ? "Y" : "N");
        String whereClause = "GIS_ID = ?";
        String[] whereArgs = {gis_id};
        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.update(STATUS_TABLE, contentValues, whereClause, whereArgs);
        return result > 0;
    }

    public boolean update33KVFeeder3Status(String est_no, boolean isSurveyCompleted, String gis_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(IS_33KV_FEEDER_3_SURVEY_COMPLETED, isSurveyCompleted ? "Y" : "N");
        String whereClause = "GIS_ID = ?";
        String[] whereArgs = {gis_id};
        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.update(STATUS_TABLE, contentValues, whereClause, whereArgs);
        return result > 0;
    }

    public boolean update33KVFeeder4Status(String est_no, boolean isSurveyCompleted, String gis_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(IS_33KV_FEEDER_4_SURVEY_COMPLETED, isSurveyCompleted ? "Y" : "N");
        String whereClause = "GIS_ID = ?";
        String[] whereArgs = {gis_id};
        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.update(STATUS_TABLE, contentValues, whereClause, whereArgs);
        return result > 0;
    }

    public boolean update33KVFeeder5Status(String est_no, boolean isSurveyCompleted, String gis_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(IS_33KV_FEEDER_5_SURVEY_COMPLETED, isSurveyCompleted ? "Y" : "N");
        String whereClause = "GIS_ID = ?";
        String[] whereArgs = {gis_id};
        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.update(STATUS_TABLE, contentValues, whereClause, whereArgs);
        return result > 0;
    }

    public String getDtrStructureCode11kv() {

        String STRUCTURE_CODE = "";

        String query =
                "SELECT MAX(CAST(SUBSTR(DTR_STRUCTURE_CODE_NEW, -5) AS INTEGER)) " +
                        "FROM SURVEY_TABLE_NEW " +
                        "WHERE DTR_SURVEY_TYPE_NEW = 'Proposed'";

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {

                int maxCode = cursor.getInt(0);

                if (maxCode == 0) {
                    STRUCTURE_CODE = "90000";
                } else {
                    STRUCTURE_CODE = String.valueOf(maxCode);
                }
            }

            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return STRUCTURE_CODE;
    }

//    public String getDtrStructureCode11kv() {
//        String STRUCTURE_CODE = "";
//        //String query = "SELECT REF_ID FROM WORK_TABLE WHERE EST_NO = '" + est_no + "' AND WORK_TYPE = '" + work_type + "'";
//        String query = " SELECT DTR_STRUCTURE_CODE_NEW FROM SURVEY_TABLE_NEW WHERE DTR_SURVEY_TYPE_NEW = 'Proposed' ORDER BY rowid DESC LIMIT 1 ";
//        System.out.println("DTR STRUCTURE_CODE QUERY : " + query);
//        SQLiteDatabase db = this.getWritableDatabase();
//        try {
//            Cursor cursor = db.rawQuery(query, null);
//            if (cursor.moveToFirst()) {
//                do {
//                    STRUCTURE_CODE = cursor.getString(0);
//                } while (cursor.moveToNext());
//            }
//
//            db.close();
//        } catch (Exception e) {
//            db.close();
//        } finally {
//            db.close();
//        }
//        return STRUCTURE_CODE;
//    }

}
