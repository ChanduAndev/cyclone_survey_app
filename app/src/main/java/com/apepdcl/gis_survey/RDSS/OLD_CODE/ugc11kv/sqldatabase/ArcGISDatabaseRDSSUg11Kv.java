package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.sqldatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.COM_TYPE_SUBTYPE_VER_vo_RDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.DTRMaster_voRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.DistandeVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.FeederDataVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.FeederServiceMasterResponseVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.IncrementalFeederDataVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.IncrementalSurveyVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.LoginDataResponseRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.MULTIVOLTAGEMaster_voRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.MakeMaster_voRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.SSMasterVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.SurveyVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.WoStatusMasterVoRDSSUg11Kv;
import com.apepdcl.gis_survey.vo.LoginDataResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ArcGISDatabaseRDSSUg11Kv extends SQLiteOpenHelper {

    // Database Version

    public static final int DATABASE_VERSION = 15;  // 06-01-2024
    // Database Name
    //   private static final String DATABASE_NAME = "GIS_11KV_SURVEYDB";

    private static final String DATABASE_NAME = "JHC_PHASE_2_11KV_SURVEYDB";

    //Tables Name
    private static final String ARCGIS_TABLE = "ARCGIS_TABLE";
    private static final String ARCGIS_TABLE_REPEAT = "ARCGIS_TABLE_REPEAT";
    private static final String FEEDERMASTER_TABLE = "FEEDERMASTER_TABLE";
    private static final String EHTMASTER_TABLE = "EHTMASTER_TABLE";
    private static final String USERMASTER_TABLE = "USERMASTER_TABLE";
    private static final String SSMASTER_TABLE = "SSMASTER_TABLE";
    private static final String DTR_MASTER_TABLE = "DTR_MASTER_TABLE";
    private static final String MAKE_MASTER_TABLE = "MAKE_MASTER_TABLE";
    private static final String FEEDER_SERVICE_MASTER_TABLE = "FEEDER_SERVICE_MASTER_TABLE";
    private static final String MULTIVOLTAGE_MASTERS_TABLE = "MULTIVOLTAGE_MASTERS_TABLE";
    private static final String COM_TYPE_SUBTYPE_VER = "COM_TYPE_SUBTYPE_VER";

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
    private static final String RMU_SURVEY_TYPE = "RMU_SURVEY_TYPE";
    private static final String REMARKS = "REMARKS";
    private static final String TAPPING_LOCNAME = "TAPPING_LOCNAME";
    private static final String TAPPING_POINTS = "TAPPING_POINTS";
    private static final String DTR_CODE1 = "DTR_CODE1";
    private static final String DTR_PHASE1 = "DTR_PHASE1";
    private static final String DTR_PRO_EXT1 = "DTR_PRO_EXT1";
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

    //Make Master
    private static final String ID = "ID";
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

    private static final String DTR_PRO_EXT = "DTR_PRO_EXT";
    private static final String ESTIMATE_NO = "ESTIMATE_NO";
    private static final String POLE_SURVEY_TYPE = "POLE_SURVEY_TYPE";
    private static final String FIXURE_TYPE = "FIXURE_TYPE";
    private static final String LAYOUT_ID = "LAYOUT_ID";
    private static final String LAYOUT_NAME = "LAYOUT_NAME";
    private static final String GPS_ACCURACY = "GPS_ACCURACY";


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
            + DTR_LOCATION + " TEXT " + ")";


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
            + DTR_LOCATION + " TEXT " + ")";


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
            + EHT_SS_CODE + " TEXT ," + EHT_SS_NAME + " TEXT ," + FEEDER_NAME_33KV + " TEXT ," + FEEDER_CODE_33KV + " TEXT ," + WO_ID + " TEXT ," + IS_FEEDER_TYPE + " TEXT ," + FLAG3 + " TEXT " + ")";


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
            + RMU_SURVEY_TYPE + " TEXT ,"
            + REMARKS + " TEXT ,"
            + TAPPING_LOCNAME + " TEXT ,"
            + TAPPING_POINTS + " TEXT ,"
            + DTR_CODE1 + " TEXT ,"
            + DTR_PHASE1 + " TEXT ,"
            + DTR_PRO_EXT1 + " TEXT ,"
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
            + DTR_PRO_EXT + " TEXT ,"
            + ESTIMATE_NO + " TEXT ,"
            + POLE_SURVEY_TYPE + " TEXT ,"
            + FIXURE_TYPE + " TEXT ,"
            + LAYOUT_ID + " TEXT ,"
            + LAYOUT_NAME + " TEXT ,"
            + GPS_ACCURACY + " TEXT ,"
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
            + RMU_SURVEY_TYPE + " TEXT ,"
            + REMARKS + " TEXT ,"
            + TAPPING_LOCNAME + " TEXT ,"
            + TAPPING_POINTS + " TEXT ,"
            + DTR_CODE1 + " TEXT ,"
            + DTR_PHASE1 + " TEXT ,"
            + DTR_PRO_EXT1 + " TEXT ,"
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
            + DTR_PRO_EXT + " TEXT ,"
            + ESTIMATE_NO + " TEXT ,"
            + POLE_SURVEY_TYPE + " TEXT ,"
            + FIXURE_TYPE + " TEXT ,"
            + LAYOUT_ID + " TEXT ,"
            + LAYOUT_NAME + " TEXT ,"
            + FLAG4 + " TEXT " + ")";


    String CREATE_INCREMENTAL_ARCGIS_TABLE = "CREATE TABLE " + INCREMENTAL_ARCGIS_TABLE + "("
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


    String CREATE_COM_TYPE_SUBTYPE_VER = "CREATE TABLE " + COM_TYPE_SUBTYPE_VER + "("
            + LAYOUT_NAME + " TEXT ," + FEEDER_ID + " TEXT ," + LAYOUT_ID + " TEXT ," + FLAG1 + " TEXT ," + FLAG2 + " TEXT ," + FLAG3 + " TEXT " + ")";


    public ArcGISDatabaseRDSSUg11Kv(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ARCGIS_TABLE);

        db.execSQL(CREATE_TABLE_REPEAT);
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
        db.execSQL(CREATE_COM_TYPE_SUBTYPE_VER);


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
        db.execSQL("DROP TABLE IF EXISTS " + COM_TYPE_SUBTYPE_VER);


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

    public void clearCOM_TYPE_SUBTYPE_VER() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + COM_TYPE_SUBTYPE_VER);
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
        db.execSQL("DELETE FROM " + COM_TYPE_SUBTYPE_VER);

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


    public boolean addSSMasterData(SSMasterVoRDSSUg11Kv ssMasterVoRDSSUg11Kv) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EHT_SS_CODE, ssMasterVoRDSSUg11Kv.getFEEDING_SS_CODE());
        values.put(EHT_SS_NAME, ssMasterVoRDSSUg11Kv.getFEEDING_SS_NAME());
        values.put(FEEDER_CODE_33KV, ssMasterVoRDSSUg11Kv.getFEEDER_CODE_33KV());

        long a = db.insert(SSMASTER_TABLE, null, values);
        if (a == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }


    public List<SSMasterVoRDSSUg11Kv> getSSMasterData() {
        ArrayList<SSMasterVoRDSSUg11Kv> ssResponseList = new ArrayList<SSMasterVoRDSSUg11Kv>();

        String query = "SELECT * FROM SSMASTER_TABLE";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {

                SSMasterVoRDSSUg11Kv ssMasterVoRDSSUg11Kv = new SSMasterVoRDSSUg11Kv();
                //vo.setLat(cursor.getString(0));
                ssMasterVoRDSSUg11Kv.setFEEDING_SS_CODE(cursor.getString(0));
                ssMasterVoRDSSUg11Kv.setFEEDING_SS_NAME(cursor.getString(1));
                ssMasterVoRDSSUg11Kv.setFEEDER_CODE_33KV(cursor.getString(2));

                //  vo.setSECTION_CODE(cursor.getString(8));
                ssResponseList.add(ssMasterVoRDSSUg11Kv);
            } while (cursor.moveToNext());
        }

        db.close();
        return ssResponseList;
    }

    public boolean addUserMasterData(LoginDataResponse loginDataResponseRDSSUg11Kv) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ROLE, loginDataResponseRDSSUg11Kv.getROLE());
        values.put(OFFICE_ID, loginDataResponseRDSSUg11Kv.getOFFICE_ID());
        values.put(SECTION_NAME, loginDataResponseRDSSUg11Kv.getSECTION_NAME());
        values.put(SEC_CODE, loginDataResponseRDSSUg11Kv.getSEC_CODE());

        long a = db.insert(USERMASTER_TABLE, null, values);
        if (a == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }

    public List<LoginDataResponseRDSSUg11Kv> getUserMasterData() {
        ArrayList<LoginDataResponseRDSSUg11Kv> loginDataResponsRDSSUg11Kvs = new ArrayList<LoginDataResponseRDSSUg11Kv>();

        String query = "SELECT * FROM USERMASTER_TABLE";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {

                LoginDataResponseRDSSUg11Kv loginVo = new LoginDataResponseRDSSUg11Kv();
                //vo.setLat(cursor.getString(0));
                loginVo.setROLE(cursor.getString(0));
                loginVo.setOFFICE_ID(cursor.getString(1));
                loginVo.setSECTION_NAME(cursor.getString(2));
                loginVo.setSEC_CODE(cursor.getString(3));
                //  vo.setSECTION_CODE(cursor.getString(8));
                loginDataResponsRDSSUg11Kvs.add(loginVo);
            } while (cursor.moveToNext());
        }

        db.close();
        return loginDataResponsRDSSUg11Kvs;
    }

//+ EHT_SS_CODE + " TEXT ," + EHT_SS_NAME + " TEXT ," + FEEDER_NAME_33KV + " TEXT ," + FEEDER_CODE_33KV + " TEXT ," + FLAG1 +

    public boolean addCOM_TYPE_SUBTYPE_VER(COM_TYPE_SUBTYPE_VER_vo_RDSSUg11Kv feederDataVo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LAYOUT_NAME, feederDataVo.getLAYOUT_NAME());
        values.put(FEEDER_ID, feederDataVo.getFEEDER_ID());
        values.put(LAYOUT_ID, feederDataVo.getLAYOUT_ID());


        long a = db.insert(COM_TYPE_SUBTYPE_VER, null, values);
        if (a == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }


    public List<COM_TYPE_SUBTYPE_VER_vo_RDSSUg11Kv> getCOM_TYPE_SUBTYPE_VER() {
        ArrayList<COM_TYPE_SUBTYPE_VER_vo_RDSSUg11Kv> feederDataVos = new ArrayList<COM_TYPE_SUBTYPE_VER_vo_RDSSUg11Kv>();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String query = "SELECT * FROM COM_TYPE_SUBTYPE_VER";

            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {

                    COM_TYPE_SUBTYPE_VER_vo_RDSSUg11Kv feederDataVo = new COM_TYPE_SUBTYPE_VER_vo_RDSSUg11Kv();
                    //vo.setLat(cursor.getString(0));
                    feederDataVo.setLAYOUT_NAME(cursor.getString(0));
                    feederDataVo.setFEEDER_ID(cursor.getString(1));
                    feederDataVo.setLAYOUT_ID(cursor.getString(2));
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

    public boolean addFeederData(FeederDataVoRDSSUg11Kv feederDataVoRDSSUg11Kv) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EHT_SS_CODE, feederDataVoRDSSUg11Kv.getEHT_SS_CODE());
        values.put(EHT_SS_NAME, feederDataVoRDSSUg11Kv.getEHT_SS_NAME());
        values.put(FEEDER_NAME_33KV, feederDataVoRDSSUg11Kv.getFEEDER_NAME_33KV());
        values.put(FEEDER_CODE_33KV, feederDataVoRDSSUg11Kv.getFEEDER_CODE_33KV());
        // values.put(FEEDER_CODE_33KV, feederDataVo.getFEEDER_CODE_33KV());
        values.put(WO_ID, feederDataVoRDSSUg11Kv.getWO_ID());
        values.put(IS_FEEDER_TYPE, feederDataVoRDSSUg11Kv.getIS_FEEDER_TYPE());


        long a = db.insert(EHTMASTER_TABLE, null, values);
        if (a == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }

    public boolean addCmpFeederData(FeederDataVoRDSSUg11Kv feederDataVoRDSSUg11Kv) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EHT_SS_CODE, feederDataVoRDSSUg11Kv.getEHT_SS_CODE());
        values.put(EHT_SS_NAME, feederDataVoRDSSUg11Kv.getEHT_SS_NAME());
        values.put(FEEDER_NAME_33KV, feederDataVoRDSSUg11Kv.getFEEDER_NAME_33KV());
        values.put(FEEDER_CODE_33KV, feederDataVoRDSSUg11Kv.getFEEDER_CODE_33KV());

        long a = db.insert(CMP_EHTMASTER_TABLE, null, values);
        if (a == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }

    public List<FeederDataVoRDSSUg11Kv> getFeerData() {
        ArrayList<FeederDataVoRDSSUg11Kv> feederDataVoRDSSUg11Kvs = new ArrayList<FeederDataVoRDSSUg11Kv>();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String query = "SELECT * FROM EHTMASTER_TABLE";

            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {

                    FeederDataVoRDSSUg11Kv feederDataVoRDSSUg11Kv = new FeederDataVoRDSSUg11Kv();
                    //vo.setLat(cursor.getString(0));
                    feederDataVoRDSSUg11Kv.setEHT_SS_CODE(cursor.getString(0));
                    feederDataVoRDSSUg11Kv.setEHT_SS_NAME(cursor.getString(1));
                    feederDataVoRDSSUg11Kv.setFEEDER_NAME_33KV(cursor.getString(2));
                    feederDataVoRDSSUg11Kv.setFEEDER_CODE_33KV(cursor.getString(3));
                    //  vo.setSECTION_CODE(cursor.getString(8));
                    feederDataVoRDSSUg11Kvs.add(feederDataVoRDSSUg11Kv);
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }
        return feederDataVoRDSSUg11Kvs;
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
    public boolean addSurveyData(SurveyVoRDSSUg11Kv surveyVoRDSSUg11Kv) {
        boolean status = false;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(FEEDER_ID, surveyVoRDSSUg11Kv.getFeeder_id());
            values.put(POLE_NO, surveyVoRDSSUg11Kv.getPole_no());
            values.put(FROM_POLE, surveyVoRDSSUg11Kv.getFrom_pole());
            values.put(POLE_TYPE, surveyVoRDSSUg11Kv.getPole_type());
            values.put(POLE_HEIGHT, surveyVoRDSSUg11Kv.getPole_height());
            // values.put(NOOF_CIRCUITS, surveyVo.getNoof_circuits());
            values.put(POLE_SUPPORT, surveyVoRDSSUg11Kv.getPole_support());
            values.put(TAPPING_POINT, surveyVoRDSSUg11Kv.getTapping_point());
            values.put(CONDUCTOR_SIZE, surveyVoRDSSUg11Kv.getConductor_size());
            values.put(POLE_CONFIGURATION, surveyVoRDSSUg11Kv.getPole_configuration());
            values.put(PINPOINT, surveyVoRDSSUg11Kv.getPinpoint());
            values.put(POLE_PAINTINGCODE, surveyVoRDSSUg11Kv.getPole_paintingcode());
            values.put(LAT, surveyVoRDSSUg11Kv.getLat());
            values.put(LAN, surveyVoRDSSUg11Kv.getLan());
            values.put(FLAG1, surveyVoRDSSUg11Kv.getPastSurveyFlag());
            values.put(NO_CKT, surveyVoRDSSUg11Kv.getIsMultiCircuit());
            values.put(CKT_TYPE, surveyVoRDSSUg11Kv.getCircuitType());
            values.put(IS_ENDPOLE, surveyVoRDSSUg11Kv.getIsEndPole());
            values.put(LINE_TYPE, surveyVoRDSSUg11Kv.getLineType());
            values.put(NOOF_CIRCUITS, surveyVoRDSSUg11Kv.getNoofCircuits());
            values.put(SS_NAME, surveyVoRDSSUg11Kv.getSs_name());
            values.put(POLE_STRUCTURE, surveyVoRDSSUg11Kv.getPoleStructure());
            values.put(DIFF_VOLTAGE, surveyVoRDSSUg11Kv.getDiffVoltage());
            values.put(SS_ID, surveyVoRDSSUg11Kv.getSsId());
            values.put(STJOINT_RMU, surveyVoRDSSUg11Kv.getStjoint_rmu());
            values.put(RMU_TYPE, surveyVoRDSSUg11Kv.getRmu_type());
            values.put(RMU_SURVEY_TYPE, surveyVoRDSSUg11Kv.getRMU_SURVEY_TYPE());
            values.put(REMARKS, surveyVoRDSSUg11Kv.getRemarks());
            values.put(TAPPING_LOCNAME, surveyVoRDSSUg11Kv.getTappingLocation());
            values.put(TAPPING_POINTS, surveyVoRDSSUg11Kv.getNoofTappings());

            values.put(DTR_CODE1, surveyVoRDSSUg11Kv.getDtr_code());
            values.put(DTR_PHASE1, surveyVoRDSSUg11Kv.getDtr_phase());
            values.put(DTR_PRO_EXT1, surveyVoRDSSUg11Kv.getDTR_PRO_EXT());
            values.put(DTR_SNO1, surveyVoRDSSUg11Kv.getDtr_scno());
            values.put(DTR_CAPACITY1, surveyVoRDSSUg11Kv.getDtr_capacity());
            values.put(DTR_MAKE1, surveyVoRDSSUg11Kv.getDtr_make());
            values.put(DTR_STRUCTURE1, surveyVoRDSSUg11Kv.getDtr_structure());
            values.put(NO_LI_CKT1, surveyVoRDSSUg11Kv.getNo_lt_ckt());
            values.put(DTR_AB1, surveyVoRDSSUg11Kv.getDtr_ab_switch());
            values.put(VOLTAGE_LEVEL, surveyVoRDSSUg11Kv.getVoltage_level());
            values.put(DTR_LOCATION_NAME, surveyVoRDSSUg11Kv.getDtr_location_name());
            values.put(DTR_NO_OF_LT_CIRCUIT, surveyVoRDSSUg11Kv.getDtr_no_of_circuit());
            values.put(HT_SERVICE_NO, surveyVoRDSSUg11Kv.getHt_service_number());
            values.put(PC_LINE_CAPACITOR, surveyVoRDSSUg11Kv.getPc_line_capacitor());
            values.put(PC_AUTO_RECLOSURE, surveyVoRDSSUg11Kv.getPc_auto_reclosure());

            values.put(TYPE_OF_LOAD, surveyVoRDSSUg11Kv.getType_of_load());
            values.put(DTR_LOCATION_TYPE, surveyVoRDSSUg11Kv.getDtr_location_type());
            values.put(IS_LTCKT_AVAILABLE, surveyVoRDSSUg11Kv.getIs_ltckt_avalable());
            values.put(NO_OF_AB_SWITCHES, surveyVoRDSSUg11Kv.getNo_of_ab_switches());
            values.put(WO_ID, surveyVoRDSSUg11Kv.getWoId());
            values.put(IS_FEEDER_TYPE, surveyVoRDSSUg11Kv.getIsFeederType());
            values.put(SURVEY_TYPE, surveyVoRDSSUg11Kv.getSurvaytype());
            values.put(DTR_PRO_EXT, surveyVoRDSSUg11Kv.getDTR_PRO_EXT());
            values.put(ESTIMATE_NO, surveyVoRDSSUg11Kv.getESTIMATE_NO());
            values.put(POLE_SURVEY_TYPE, surveyVoRDSSUg11Kv.getPOLE_SURVEY_TYPE());
            values.put(FIXURE_TYPE, surveyVoRDSSUg11Kv.getFIXURE_TYPE());
            values.put(LAYOUT_ID, surveyVoRDSSUg11Kv.getLAYOUT_ID());
            values.put(LAYOUT_NAME, surveyVoRDSSUg11Kv.getLAYOUT_NAME());
            values.put(GPS_ACCURACY, surveyVoRDSSUg11Kv.getGPS_ACCURACY());
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

    public ArrayList<SurveyVoRDSSUg11Kv> getSurveyData(String strCondition) {
        String query = "";
        ArrayList<SurveyVoRDSSUg11Kv> surveyVoRDSSUg11KvArrayList = new ArrayList<SurveyVoRDSSUg11Kv>();
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

                    SurveyVoRDSSUg11Kv vo = new SurveyVoRDSSUg11Kv();
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
                    vo.setRMU_SURVEY_TYPE(cursor.getString(25));
                    vo.setRemarks(cursor.getString(26));
                    vo.setTappingLocation(cursor.getString(27));
                    vo.setNoofTappings(cursor.getString(28));

                    vo.setDtr_code(cursor.getString(29));
                    vo.setDtr_phase(cursor.getString(30));
                    vo.setDTR_PRO_EXT(cursor.getString(31));
                    vo.setDtr_scno(cursor.getString(32));
                    vo.setDtr_capacity(cursor.getString(33));
                    vo.setDtr_make(cursor.getString(34));
                    vo.setDtr_structure(cursor.getString(35));
                    vo.setNo_lt_ckt(cursor.getString(36));
                    vo.setDtr_ab_switch(cursor.getString(37));
                    vo.setVoltage_level(cursor.getString(38));
                    vo.setDtr_location_name(cursor.getString(39));
                    vo.setDtr_no_of_circuit(cursor.getString(40));
                    vo.setHt_service_number(cursor.getString(41));
                    vo.setPc_line_capacitor(cursor.getString(42));
                    vo.setPc_auto_reclosure(cursor.getString(43));
                    vo.setType_of_load(cursor.getString(44));
                    vo.setDtr_location_type(cursor.getString(45));
                    vo.setIs_ltckt_avalable(cursor.getString(46));
                    vo.setNo_of_ab_switches(cursor.getString(47));
                    vo.setWoId(cursor.getString(48));
                    vo.setIsFeederType(cursor.getString(49));
                    vo.setSurvaytype(cursor.getString(50));
                    vo.setDTR_PRO_EXT(cursor.getString(51));
                    vo.setESTIMATE_NO(cursor.getString(52));

                    vo.setPOLE_SURVEY_TYPE(cursor.getString(53));
                    vo.setFIXURE_TYPE(cursor.getString(54));

                    vo.setLAYOUT_ID(cursor.getString(55));
                    vo.setLAYOUT_NAME(cursor.getString(56));
                    vo.setGPS_ACCURACY(cursor.getString(57));

                    surveyVoRDSSUg11KvArrayList.add(vo);
                } while (cursor.moveToNext());
            }
            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }

        return surveyVoRDSSUg11KvArrayList;
    }

    public List<SurveyVoRDSSUg11Kv> getData() {
        ArrayList<SurveyVoRDSSUg11Kv> surveyVoRDSSUg11KvArrayList = new ArrayList<SurveyVoRDSSUg11Kv>();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String query = "SELECT * FROM ARCGIS_TABLE ORDER BY POLE_NO";

            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {

                    SurveyVoRDSSUg11Kv vo = new SurveyVoRDSSUg11Kv();
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
                    vo.setRMU_SURVEY_TYPE(cursor.getString(25));
                    vo.setRemarks(cursor.getString(26));
                    vo.setTappingLocation(cursor.getString(27));
                    vo.setNoofTappings(cursor.getString(28));
                    vo.setDtr_code(cursor.getString(29));
                    vo.setDtr_phase(cursor.getString(30));
                    vo.setDTR_PRO_EXT(cursor.getString(31));
                    vo.setDtr_scno(cursor.getString(32));
                    vo.setDtr_capacity(cursor.getString(33));
                    vo.setDtr_make(cursor.getString(34));
                    vo.setDtr_structure(cursor.getString(35));
                    vo.setNo_lt_ckt(cursor.getString(36));
                    vo.setDtr_ab_switch(cursor.getString(37));
                    vo.setVoltage_level(cursor.getString(38));
                    vo.setDtr_location_name(cursor.getString(39));
                    vo.setDtr_no_of_circuit(cursor.getString(40));
                    vo.setHt_service_number(cursor.getString(41));
                    vo.setPc_line_capacitor(cursor.getString(42));
                    vo.setPc_auto_reclosure(cursor.getString(43));
                    vo.setType_of_load(cursor.getString(44));
                    vo.setDtr_location_type(cursor.getString(45));
                    vo.setIs_ltckt_avalable(cursor.getString(46));
                    vo.setNo_of_ab_switches(cursor.getString(47));
                    vo.setWoId(cursor.getString(48));
                    vo.setIsFeederType(cursor.getString(49));
                    vo.setSurvaytype(cursor.getString(50));
                    vo.setDTR_PRO_EXT(cursor.getString(51));
                    vo.setESTIMATE_NO(cursor.getString(52));
                    vo.setPOLE_SURVEY_TYPE(cursor.getString(53));
                    vo.setFIXURE_TYPE(cursor.getString(54));
                    vo.setLAYOUT_ID(cursor.getString(55));
                    vo.setLAYOUT_NAME(cursor.getString(56));
                    vo.setGPS_ACCURACY(cursor.getString(57));
                    surveyVoRDSSUg11KvArrayList.add(vo);
                } while (cursor.moveToNext());
            }
            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }

        return surveyVoRDSSUg11KvArrayList;
    }

    public boolean addDTRMasterData(DTRMaster_voRDSSUg11Kv dtrMaster_voRDSSUg11Kv) {
        boolean status = false;
        SQLiteDatabase db = this.getWritableDatabase();
        try {

            ContentValues values = new ContentValues();
            values.put(DTR_FEEDER_ID, dtrMaster_voRDSSUg11Kv.getDTR_FEEDER_ID());
            values.put(DTR_OFFICE_ID, dtrMaster_voRDSSUg11Kv.getDTR_OFFICE_ID());
            values.put(DTR_SECTION_NAME, dtrMaster_voRDSSUg11Kv.getDTR_SECTION_NAME());
            values.put(DTR_PHASE, dtrMaster_voRDSSUg11Kv.getDTR_PHASE());
            values.put(DTR_CAPACITY, dtrMaster_voRDSSUg11Kv.getDTR_CAPACITY());
            values.put(DTR_MAKE, dtrMaster_voRDSSUg11Kv.getDTR_MAKE());
            values.put(DTR_STRUCTURE_CODE, dtrMaster_voRDSSUg11Kv.getDTR_DTR_STRUCTURE_CODE());
            values.put(DTR_FEEDER_NAME, dtrMaster_voRDSSUg11Kv.getDTR_FEEDER_NAME());
            values.put(DTR_SECTION_ID, dtrMaster_voRDSSUg11Kv.getDTR_SECTION_ID());
            values.put(DTR_LOCATION, dtrMaster_voRDSSUg11Kv.getDTR_LOCATION());

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

    public List<DTRMaster_voRDSSUg11Kv> getDTRMasterData() {
        ArrayList<DTRMaster_voRDSSUg11Kv> dtrmaster_list = new ArrayList<DTRMaster_voRDSSUg11Kv>();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String query = "SELECT * FROM DTR_MASTER_TABLE";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    DTRMaster_voRDSSUg11Kv Vo = new DTRMaster_voRDSSUg11Kv();
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

    public boolean addMakeMasterData(MakeMaster_voRDSSUg11Kv dtrMaster_vo) {
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

    public List<MakeMaster_voRDSSUg11Kv> getMakeMasterData() {
        ArrayList<MakeMaster_voRDSSUg11Kv> dtrmaster_list = new ArrayList<MakeMaster_voRDSSUg11Kv>();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String query = "SELECT * FROM DTR_MASTER_TABLE";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    MakeMaster_voRDSSUg11Kv Vo = new MakeMaster_voRDSSUg11Kv();
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


    public boolean addSurveyDatarepeat(SurveyVoRDSSUg11Kv surveyVoRDSSUg11Kv) {
        boolean status = false;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(FEEDER_ID, surveyVoRDSSUg11Kv.getFeeder_id());
            values.put(POLE_NO, surveyVoRDSSUg11Kv.getPole_no());
            values.put(FROM_POLE, surveyVoRDSSUg11Kv.getFrom_pole());
            values.put(POLE_TYPE, surveyVoRDSSUg11Kv.getPole_type());
            values.put(POLE_HEIGHT, surveyVoRDSSUg11Kv.getPole_height());
            // values.put(NOOF_CIRCUITS, surveyVo.getNoof_circuits());
            values.put(POLE_SUPPORT, surveyVoRDSSUg11Kv.getPole_support());
            values.put(TAPPING_POINT, surveyVoRDSSUg11Kv.getTapping_point());
            values.put(CONDUCTOR_SIZE, surveyVoRDSSUg11Kv.getConductor_size());
            values.put(POLE_CONFIGURATION, surveyVoRDSSUg11Kv.getPole_configuration());
            values.put(PINPOINT, surveyVoRDSSUg11Kv.getPinpoint());
            values.put(POLE_PAINTINGCODE, surveyVoRDSSUg11Kv.getPole_paintingcode());
            values.put(LAT, surveyVoRDSSUg11Kv.getLat());
            values.put(LAN, surveyVoRDSSUg11Kv.getLan());
            values.put(FLAG1, surveyVoRDSSUg11Kv.getPastSurveyFlag());
            values.put(NO_CKT, surveyVoRDSSUg11Kv.getIsMultiCircuit());
            values.put(CKT_TYPE, surveyVoRDSSUg11Kv.getCircuitType());
            values.put(IS_ENDPOLE, surveyVoRDSSUg11Kv.getIsEndPole());
            values.put(LINE_TYPE, surveyVoRDSSUg11Kv.getLineType());
            values.put(NOOF_CIRCUITS, surveyVoRDSSUg11Kv.getNoofCircuits());
            values.put(SS_NAME, surveyVoRDSSUg11Kv.getSs_name());
            values.put(POLE_STRUCTURE, surveyVoRDSSUg11Kv.getPoleStructure());
            values.put(DIFF_VOLTAGE, surveyVoRDSSUg11Kv.getDiffVoltage());
            values.put(SS_ID, surveyVoRDSSUg11Kv.getSsId());
            values.put(STJOINT_RMU, surveyVoRDSSUg11Kv.getStjoint_rmu());
            values.put(RMU_TYPE, surveyVoRDSSUg11Kv.getRmu_type());
            values.put(RMU_SURVEY_TYPE, surveyVoRDSSUg11Kv.getRMU_SURVEY_TYPE());
            values.put(REMARKS, surveyVoRDSSUg11Kv.getRemarks());
            values.put(TAPPING_LOCNAME, surveyVoRDSSUg11Kv.getTappingLocation());
            values.put(TAPPING_POINTS, surveyVoRDSSUg11Kv.getNoofTappings());

            values.put(DTR_CODE1, surveyVoRDSSUg11Kv.getDtr_code());
            values.put(DTR_PHASE1, surveyVoRDSSUg11Kv.getDtr_phase());
            values.put(DTR_PRO_EXT1, surveyVoRDSSUg11Kv.getDTR_PRO_EXT());
            values.put(DTR_SNO1, surveyVoRDSSUg11Kv.getDtr_scno());
            values.put(DTR_CAPACITY1, surveyVoRDSSUg11Kv.getDtr_capacity());
            values.put(DTR_MAKE1, surveyVoRDSSUg11Kv.getDtr_make());
            values.put(DTR_STRUCTURE1, surveyVoRDSSUg11Kv.getDtr_structure());
            values.put(NO_LI_CKT1, surveyVoRDSSUg11Kv.getNo_lt_ckt());
            values.put(DTR_AB1, surveyVoRDSSUg11Kv.getDtr_ab_switch());
            values.put(VOLTAGE_LEVEL, surveyVoRDSSUg11Kv.getVoltage_level());
            values.put(DTR_LOCATION_NAME, surveyVoRDSSUg11Kv.getDtr_location_name());
            values.put(DTR_NO_OF_LT_CIRCUIT, surveyVoRDSSUg11Kv.getDtr_no_of_circuit());
            values.put(HT_SERVICE_NO, surveyVoRDSSUg11Kv.getHt_service_number());
            values.put(PC_LINE_CAPACITOR, surveyVoRDSSUg11Kv.getPc_line_capacitor());
            values.put(PC_AUTO_RECLOSURE, surveyVoRDSSUg11Kv.getPc_auto_reclosure());

            values.put(TYPE_OF_LOAD, surveyVoRDSSUg11Kv.getType_of_load());
            values.put(DTR_LOCATION_TYPE, surveyVoRDSSUg11Kv.getDtr_location_type());
            values.put(IS_LTCKT_AVAILABLE, surveyVoRDSSUg11Kv.getIs_ltckt_avalable());
            values.put(NO_OF_AB_SWITCHES, surveyVoRDSSUg11Kv.getNo_of_ab_switches());
            values.put(WO_ID, surveyVoRDSSUg11Kv.getWoId());
            values.put(IS_FEEDER_TYPE, surveyVoRDSSUg11Kv.getIsFeederType());
            values.put(SURVEY_TYPE, surveyVoRDSSUg11Kv.getSurvaytype());
            values.put(DTR_PRO_EXT, surveyVoRDSSUg11Kv.getDTR_PRO_EXT());
            values.put(ESTIMATE_NO, surveyVoRDSSUg11Kv.getESTIMATE_NO());
            values.put(POLE_SURVEY_TYPE, surveyVoRDSSUg11Kv.getPOLE_SURVEY_TYPE());
            values.put(FIXURE_TYPE, surveyVoRDSSUg11Kv.getFIXURE_TYPE());

            values.put(LAYOUT_ID, surveyVoRDSSUg11Kv.getLAYOUT_ID());
            values.put(LAYOUT_NAME, surveyVoRDSSUg11Kv.getLAYOUT_NAME());
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

    public ArrayList<SurveyVoRDSSUg11Kv> getSurveyDataREPEAT(String strCondition) {
        String query = "";
        ArrayList<SurveyVoRDSSUg11Kv> surveyVoRDSSUg11KvArrayList = new ArrayList<SurveyVoRDSSUg11Kv>();
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

                    SurveyVoRDSSUg11Kv vo = new SurveyVoRDSSUg11Kv();
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
                    vo.setRMU_SURVEY_TYPE(cursor.getString(25));
                    vo.setRemarks(cursor.getString(26));
                    vo.setTappingLocation(cursor.getString(27));
                    vo.setNoofTappings(cursor.getString(28));

                    vo.setDtr_code(cursor.getString(29));
                    vo.setDtr_phase(cursor.getString(30));
                    vo.setDTR_PRO_EXT(cursor.getString(31));
                    vo.setDtr_scno(cursor.getString(32));
                    vo.setDtr_capacity(cursor.getString(33));
                    vo.setDtr_make(cursor.getString(34));
                    vo.setDtr_structure(cursor.getString(35));
                    vo.setNo_lt_ckt(cursor.getString(36));
                    vo.setDtr_ab_switch(cursor.getString(37));
                    vo.setVoltage_level(cursor.getString(38));
                    vo.setDtr_location_name(cursor.getString(39));
                    vo.setDtr_no_of_circuit(cursor.getString(40));
                    vo.setHt_service_number(cursor.getString(41));
                    vo.setPc_line_capacitor(cursor.getString(42));
                    vo.setPc_auto_reclosure(cursor.getString(43));
                    vo.setType_of_load(cursor.getString(44));
                    vo.setDtr_location_type(cursor.getString(45));
                    vo.setIs_ltckt_avalable(cursor.getString(46));
                    vo.setNo_of_ab_switches(cursor.getString(47));
                    vo.setWoId(cursor.getString(48));
                    vo.setIsFeederType(cursor.getString(49));
                    vo.setSurvaytype(cursor.getString(50));
                    vo.setDTR_PRO_EXT(cursor.getString(51));
                    vo.setESTIMATE_NO(cursor.getString(52));

                    vo.setPOLE_SURVEY_TYPE(cursor.getString(53));
                    vo.setFIXURE_TYPE(cursor.getString(54));
                    vo.setLAYOUT_ID(cursor.getString(55));
                    vo.setLAYOUT_NAME(cursor.getString(56));
                    surveyVoRDSSUg11KvArrayList.add(vo);
                } while (cursor.moveToNext());
            }
            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }

        return surveyVoRDSSUg11KvArrayList;
    }


    public boolean addFeederServiceMasterData(FeederServiceMasterResponseVoRDSSUg11Kv vo) {
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

    public List<FeederServiceMasterResponseVoRDSSUg11Kv> getFeederServiceMasterData() {
        ArrayList<FeederServiceMasterResponseVoRDSSUg11Kv> dtrmaster_list = new ArrayList<FeederServiceMasterResponseVoRDSSUg11Kv>();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String query = "SELECT * FROM DTR_MASTER_TABLE";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    FeederServiceMasterResponseVoRDSSUg11Kv Vo = new FeederServiceMasterResponseVoRDSSUg11Kv();
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


    public boolean addMultiVoltageMasterData(MULTIVOLTAGEMaster_voRDSSUg11Kv vo) {
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

    public List<MULTIVOLTAGEMaster_voRDSSUg11Kv> getMultiVoltageMasterData() {
        ArrayList<MULTIVOLTAGEMaster_voRDSSUg11Kv> dtrmaster_list = new ArrayList<MULTIVOLTAGEMaster_voRDSSUg11Kv>();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String query = "SELECT * FROM MULTIVOLTAGE_MASTERS_TABLE";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    MULTIVOLTAGEMaster_voRDSSUg11Kv Vo = new MULTIVOLTAGEMaster_voRDSSUg11Kv();
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


    public boolean addIncrementFeederData(IncrementalFeederDataVoRDSSUg11Kv incrementalFeederDataVoRDSSUg11Kv) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EHT_SS_CODE, incrementalFeederDataVoRDSSUg11Kv.getEHT_SS_CODE());
        values.put(EHT_SS_NAME, incrementalFeederDataVoRDSSUg11Kv.getEHT_SS_NAME());
        values.put(FEEDER_NAME_33KV, incrementalFeederDataVoRDSSUg11Kv.getFEEDER_NAME_33KV());
        values.put(FEEDER_CODE_33KV, incrementalFeederDataVoRDSSUg11Kv.getFEEDER_CODE_33KV());
        values.put(WORKORDER_ID, incrementalFeederDataVoRDSSUg11Kv.getWORKORDER_ID());
        values.put(STATUS, incrementalFeederDataVoRDSSUg11Kv.getSTATUS());
        values.put(FROM_POLE, incrementalFeederDataVoRDSSUg11Kv.getFROM_POLE());
        values.put(IS_FEEDER_TYPE, incrementalFeederDataVoRDSSUg11Kv.getIS_FEEDER_TYPE());
        long a = db.insert(INCREMENTAL_EHTMASTER_TABLE, null, values);
        if (a == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }

    public List<IncrementalFeederDataVoRDSSUg11Kv> getIncrementalFeerData() {
        ArrayList<IncrementalFeederDataVoRDSSUg11Kv> feederDataVosList = new ArrayList<IncrementalFeederDataVoRDSSUg11Kv>();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String query = "SELECT * FROM INCREMENTAL_EHTMASTER_TABLE";

            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {

                    IncrementalFeederDataVoRDSSUg11Kv incrementalFeederDataVoRDSSUg11Kv = new IncrementalFeederDataVoRDSSUg11Kv();
                    //vo.setLat(cursor.getString(0));
                    incrementalFeederDataVoRDSSUg11Kv.setEHT_SS_CODE(cursor.getString(0));
                    incrementalFeederDataVoRDSSUg11Kv.setEHT_SS_NAME(cursor.getString(1));
                    incrementalFeederDataVoRDSSUg11Kv.setFEEDER_NAME_33KV(cursor.getString(2));
                    incrementalFeederDataVoRDSSUg11Kv.setFEEDER_CODE_33KV(cursor.getString(3));
                    incrementalFeederDataVoRDSSUg11Kv.setWORKORDER_ID(cursor.getString(4));
                    incrementalFeederDataVoRDSSUg11Kv.setSTATUS(cursor.getString(5));
                    incrementalFeederDataVoRDSSUg11Kv.setFROM_POLE(cursor.getString(6));
                    incrementalFeederDataVoRDSSUg11Kv.setIS_FEEDER_TYPE(cursor.getString(7));
                    //  vo.setSECTION_CODE(cursor.getString(8));
                    feederDataVosList.add(incrementalFeederDataVoRDSSUg11Kv);
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


    public boolean addIncrementalSSMasterData(SSMasterVoRDSSUg11Kv ssMasterVoRDSSUg11Kv) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EHT_SS_CODE, ssMasterVoRDSSUg11Kv.getFEEDING_SS_CODE());
        values.put(EHT_SS_NAME, ssMasterVoRDSSUg11Kv.getFEEDING_SS_NAME());
        values.put(FEEDER_CODE_33KV, ssMasterVoRDSSUg11Kv.getFEEDER_CODE_33KV());

        long a = db.insert(INCREMENTALSSMASTER_TABLE, null, values);
        if (a == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }


    public List<SSMasterVoRDSSUg11Kv> getIncrementalSSMasterData() {
        ArrayList<SSMasterVoRDSSUg11Kv> ssResponseList = new ArrayList<SSMasterVoRDSSUg11Kv>();

        String query = "SELECT * FROM INCREMENTALSSMASTER_TABLE";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {

                SSMasterVoRDSSUg11Kv ssMasterVoRDSSUg11Kv = new SSMasterVoRDSSUg11Kv();
                //vo.setLat(cursor.getString(0));
                ssMasterVoRDSSUg11Kv.setFEEDING_SS_CODE(cursor.getString(0));
                ssMasterVoRDSSUg11Kv.setFEEDING_SS_NAME(cursor.getString(1));
                ssMasterVoRDSSUg11Kv.setFEEDER_CODE_33KV(cursor.getString(2));

                //  vo.setSECTION_CODE(cursor.getString(8));
                ssResponseList.add(ssMasterVoRDSSUg11Kv);
            } while (cursor.moveToNext());
        }

        db.close();
        return ssResponseList;
    }


    public boolean addIncrementalSurveyDTRMasterData(DTRMaster_voRDSSUg11Kv dtrMaster_voRDSSUg11Kv) {
        boolean status = false;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(DTR_FEEDER_ID, dtrMaster_voRDSSUg11Kv.getDTR_FEEDER_ID());
            values.put(DTR_OFFICE_ID, dtrMaster_voRDSSUg11Kv.getDTR_OFFICE_ID());
            values.put(DTR_SECTION_NAME, dtrMaster_voRDSSUg11Kv.getDTR_SECTION_NAME());
            values.put(DTR_PHASE, dtrMaster_voRDSSUg11Kv.getDTR_PHASE());
            values.put(DTR_CAPACITY, dtrMaster_voRDSSUg11Kv.getDTR_CAPACITY());
            values.put(DTR_MAKE, dtrMaster_voRDSSUg11Kv.getDTR_MAKE());
            values.put(DTR_STRUCTURE_CODE, dtrMaster_voRDSSUg11Kv.getDTR_DTR_STRUCTURE_CODE());
            values.put(DTR_FEEDER_NAME, dtrMaster_voRDSSUg11Kv.getDTR_FEEDER_NAME());
            values.put(DTR_SECTION_ID, dtrMaster_voRDSSUg11Kv.getDTR_SECTION_ID());
            values.put(DTR_LOCATION, dtrMaster_voRDSSUg11Kv.getDTR_LOCATION());

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


    public boolean addIncrementalFeederServiceMasterData(FeederServiceMasterResponseVoRDSSUg11Kv vo) {
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


    public boolean addIncrementalMakeMasterData(MakeMaster_voRDSSUg11Kv dtrMaster_vo) {
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

    public boolean addIncrementalMultiVoltageMasterData(MULTIVOLTAGEMaster_voRDSSUg11Kv vo) {
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


    public boolean addIncrementalSurveyData(IncrementalSurveyVoRDSSUg11Kv surveyVo) {
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

           /* values.put(DTR_PRO_EXT, surveyVo.getDTR_PRO_EXT());
            values.put(ESTIMATE_NO, surveyVo.getESTIMATE_NO());
*/


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


    public ArrayList<IncrementalSurveyVoRDSSUg11Kv> getIncrementalSurveyData(String strCondition) {
        String query = "";
        ArrayList<IncrementalSurveyVoRDSSUg11Kv> incrementalSurveyVosListRDSSUg11Kv = new ArrayList<IncrementalSurveyVoRDSSUg11Kv>();
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

                    IncrementalSurveyVoRDSSUg11Kv incrementalSurveyVoRDSSUg11Kv = new IncrementalSurveyVoRDSSUg11Kv();
                    //vo.setLat(cursor.getString(0));
                    incrementalSurveyVoRDSSUg11Kv.setFeeder_id(cursor.getString(0));
                    incrementalSurveyVoRDSSUg11Kv.setPole_no(cursor.getInt(1));
                    incrementalSurveyVoRDSSUg11Kv.setFrom_pole(cursor.getInt(2));
                    incrementalSurveyVoRDSSUg11Kv.setPole_type(cursor.getString(3));
                    incrementalSurveyVoRDSSUg11Kv.setPole_height(cursor.getString(4));
                    // vo.setNoof_circuits(cursor.getString(5));
                    incrementalSurveyVoRDSSUg11Kv.setPole_support(cursor.getString(5));
                    incrementalSurveyVoRDSSUg11Kv.setTapping_point(cursor.getString(6));
                    incrementalSurveyVoRDSSUg11Kv.setConductor_size(cursor.getString(7));
                    incrementalSurveyVoRDSSUg11Kv.setPole_configuration(cursor.getString(8));
                    incrementalSurveyVoRDSSUg11Kv.setPinpoint(cursor.getString(9));
                    incrementalSurveyVoRDSSUg11Kv.setPole_paintingcode(cursor.getString(10));
                    incrementalSurveyVoRDSSUg11Kv.setLat(cursor.getString(11));
                    incrementalSurveyVoRDSSUg11Kv.setLan(cursor.getString(12));
                    incrementalSurveyVoRDSSUg11Kv.setPastSurveyFlag(cursor.getString(13));
                    incrementalSurveyVoRDSSUg11Kv.setIsMultiCircuit(cursor.getString(14));
                    incrementalSurveyVoRDSSUg11Kv.setCircuitType(cursor.getString(15));
                    incrementalSurveyVoRDSSUg11Kv.setIsEndPole(cursor.getString(16));
                    incrementalSurveyVoRDSSUg11Kv.setLineType(cursor.getString(17));
                    incrementalSurveyVoRDSSUg11Kv.setNoofCircuits(cursor.getString(18));
                    incrementalSurveyVoRDSSUg11Kv.setSs_name(cursor.getString(19));
                    incrementalSurveyVoRDSSUg11Kv.setPoleStructure(cursor.getString(20));
                    incrementalSurveyVoRDSSUg11Kv.setDiffVoltage(cursor.getString(21));
                    incrementalSurveyVoRDSSUg11Kv.setSsId(cursor.getString(22));
                    incrementalSurveyVoRDSSUg11Kv.setStjoint_rmu(cursor.getString(23));
                    incrementalSurveyVoRDSSUg11Kv.setRmu_type(cursor.getString(24));
                    incrementalSurveyVoRDSSUg11Kv.setRemarks(cursor.getString(25));
                    incrementalSurveyVoRDSSUg11Kv.setTappingLocation(cursor.getString(26));
                    incrementalSurveyVoRDSSUg11Kv.setNoofTappings(cursor.getString(27));

                    incrementalSurveyVoRDSSUg11Kv.setDtr_code(cursor.getString(28));
                    incrementalSurveyVoRDSSUg11Kv.setDtr_phase(cursor.getString(29));
                    incrementalSurveyVoRDSSUg11Kv.setDtr_scno(cursor.getString(30));
                    incrementalSurveyVoRDSSUg11Kv.setDtr_capacity(cursor.getString(31));
                    incrementalSurveyVoRDSSUg11Kv.setDtr_make(cursor.getString(32));
                    incrementalSurveyVoRDSSUg11Kv.setDtr_structure(cursor.getString(33));
                    incrementalSurveyVoRDSSUg11Kv.setNo_lt_ckt(cursor.getString(34));
                    incrementalSurveyVoRDSSUg11Kv.setDtr_ab_switch(cursor.getString(35));
                    incrementalSurveyVoRDSSUg11Kv.setVoltage_level(cursor.getString(36));
                    incrementalSurveyVoRDSSUg11Kv.setDtr_location_name(cursor.getString(37));
                    incrementalSurveyVoRDSSUg11Kv.setDtr_no_of_circuit(cursor.getString(38));
                    incrementalSurveyVoRDSSUg11Kv.setHt_service_number(cursor.getString(39));
                    incrementalSurveyVoRDSSUg11Kv.setPc_line_capacitor(cursor.getString(40));
                    incrementalSurveyVoRDSSUg11Kv.setPc_auto_reclosure(cursor.getString(41));
                    incrementalSurveyVoRDSSUg11Kv.setType_of_load(cursor.getString(42));
                    incrementalSurveyVoRDSSUg11Kv.setDtr_location_type(cursor.getString(43));
                    incrementalSurveyVoRDSSUg11Kv.setIs_ltckt_avalable(cursor.getString(44));
                    incrementalSurveyVoRDSSUg11Kv.setNo_of_ab_switches(cursor.getString(45));
                    incrementalSurveyVoRDSSUg11Kv.setWorkOrder(cursor.getString(46));
                    incrementalSurveyVoRDSSUg11Kv.setWoStatus(cursor.getString(47));
                    incrementalSurveyVoRDSSUg11Kv.setIsFeederType(cursor.getString(48));
                    incrementalSurveyVoRDSSUg11Kv.setSURVEY_TYPE(cursor.getString(49));
                    incrementalSurveyVosListRDSSUg11Kv.add(incrementalSurveyVoRDSSUg11Kv);
                } while (cursor.moveToNext());
            }
            db.close();
        } catch (Exception e) {
            db.close();
        } finally {
            db.close();
        }

        return incrementalSurveyVosListRDSSUg11Kv;
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


    public boolean addWoStatusMasterData(WoStatusMasterVoRDSSUg11Kv woStatusMasterVoRDSSUg11Kv) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FEEDER_ID, woStatusMasterVoRDSSUg11Kv.getFEEDER_ID());
        values.put(WO_ID, woStatusMasterVoRDSSUg11Kv.getWO_ID());
        values.put(WO_STATUS, woStatusMasterVoRDSSUg11Kv.getWO_STATUS());
        long a = db.insert(WO_STATUS_TABLE, null, values);
        if (a == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }

    public List<WoStatusMasterVoRDSSUg11Kv> getWoStatusMasterData() {
        ArrayList<WoStatusMasterVoRDSSUg11Kv> woStatusMasterVosListRDSSUg11Kv = new ArrayList<WoStatusMasterVoRDSSUg11Kv>();

        String query = "SELECT * FROM WO_STATUS_TABLE";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                WoStatusMasterVoRDSSUg11Kv woStatusMasterVoRDSSUg11Kv = new WoStatusMasterVoRDSSUg11Kv();
                //vo.setLat(cursor.getString(0));
                woStatusMasterVoRDSSUg11Kv.setFEEDER_ID(cursor.getString(0));
                woStatusMasterVoRDSSUg11Kv.setWO_ID(cursor.getString(1));
                woStatusMasterVoRDSSUg11Kv.setWO_STATUS(cursor.getString(2));

                //  vo.setSECTION_CODE(cursor.getString(8));
                woStatusMasterVosListRDSSUg11Kv.add(woStatusMasterVoRDSSUg11Kv);
            } while (cursor.moveToNext());
        }

        db.close();
        return woStatusMasterVosListRDSSUg11Kv;
    }

    public List<DistandeVoRDSSUg11Kv> getDistance() {
        List<DistandeVoRDSSUg11Kv> distandeVoRDSSUg11KvArrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  POLE_NO,LAT,LAN  FROM INCREMENTAL_ARCGIS_TABLE  WHERE ROWID IN(SELECT MAX(ROWID) FROM INCREMENTAL_ARCGIS_TABLE  WHERE POLE_NO=(\n" +
                "SELECT  MAX(POLE_NO) FROM INCREMENTAL_ARCGIS_TABLE)) ";


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    DistandeVoRDSSUg11Kv distandeVoRDSSUg11Kv = new DistandeVoRDSSUg11Kv();
                    distandeVoRDSSUg11Kv.setPOLE_NO(cursor.getString(0));
                    distandeVoRDSSUg11Kv.setLAT(cursor.getString(1));
                    distandeVoRDSSUg11Kv.setLAN(cursor.getString(2));
                    distandeVoRDSSUg11KvArrayList.add(distandeVoRDSSUg11Kv);
                } while (cursor.moveToNext());
            }
        } finally {
            db.close();
        }
        // return contact list
        return distandeVoRDSSUg11KvArrayList;
    }
}
