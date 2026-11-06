package com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.sqldatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.DTRMaster_vo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.DistandeVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.FeederDataVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.FeederServiceMasterResponseVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.IncrementalFeederDataVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.IncrementalSurveyVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.LoginDataResponse;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.MULTIVOLTAGEMaster_vo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.MakeMaster_vo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.SSMasterVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.SurveyVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.WoStatusMasterVo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ArcGISDatabase extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 13;
    // Database Name
 //   private static final String DATABASE_NAME = "GIS_11KV_SURVEYDB";

    private static final String DATABASE_NAME = "RDSS11KV_SURVEYDB";

    //Tables Name
    private static final String ARCGIS_TABLE = "ARCGIS_TABLE";
    private static final String ARCGIS_TABLE_REPEAT = "ARCGIS_TABLE_REPEAT";
    private static final String FEEDERMASTER_TABLE = "FEEDERMASTER_TABLE";
    private static final String EHTMASTER_TABLE = "EHTMASTER_TABLE";
    private static final String USERMASTER_TABLE = "USERMASTER_TABLE";
    private static final String SSMASTER_TABLE = "SSMASTER_TABLE";
    private static final String DTR_MASTER_TABLE = "DTR_MASTER_TABLE";
    private static final String MAKE_MASTER_TABLE="MAKE_MASTER_TABLE";
    private static final String FEEDER_SERVICE_MASTER_TABLE="FEEDER_SERVICE_MASTER_TABLE";
    private static final String MULTIVOLTAGE_MASTERS_TABLE="MULTIVOLTAGE_MASTERS_TABLE";

    private static final String CMP_EHTMASTER_TABLE = "CMP_EHTMASTER_TABLE";

    private static final String INCREMENTAL_EHTMASTER_TABLE="INCREMENTAL_EHTMASTER_TABLE";
    private static final String INCREMENTAL_ARCGIS_TABLE="INCREMENTAL_ARCGIS_TABLE";
    private static final String INCREMENTALSSMASTER_TABLE="INCREMENTALSSMASTER_TABLE";
    private static final String INCREMENTAL_DTR_MASTER_TABLE="INCREMENTAL_DTR_MASTER_TABLE";
    private static final String INCREMENTAL_FEEDER_SERVICE_MASTER_TABLE="INCREMENTAL_FEEDER_SERVICE_MASTER_TABLE";
    private static final String INCREMENTAL_MAKE_MASTER_TABLE="INCREMENTAL_MAKE_MASTER_TABLE";
    private static final String INCREMENTAL_MULTIVOLTAGE_MASTERS_TABLE="INCREMENTAL_MULTIVOLTAGE_MASTERS_TABLE";
    private static final String WO_STATUS_TABLE="WO_STATUS_TABLE";






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
    private static final String DTR_STRUCTURE_CODE="DTR_STRUCTURE_CODE";
    private static final String DTR_FEEDER_NAME="DTR_FEEDER_NAME";
    private static final String DTR_SECTION_ID="DTR_SECTION_ID";
    private static final String DTR_LOCATION="DTR_LOCATION";

    //Make Master
    private static final String ID="ID";
    private static final String DTR_MAKE_M="DTR_MAKE_M";
    private static final String DTR_RECORD_STATUS="DTR_RECORD_STATUS";


    //Feeder Service Master
    private static final String FS_FEEDER_ID="FS_FEEDER_ID";
    private static final String FS_CONSUMER_NUMBER="FS_CONSUMER_NUMBER";
    private static final String FS_CONSUMER_NAME="FS_CONSUMER_NAME";

//Multivoltage
    private static final String DTR_ID="DTR_ID";
    private static final String VOLTAGE="VOLTAGE";
    private static final String CKT_NO="CKT_NO";
    private static final String CKT_VALUE="CKT_VALUE";
    private static final String RECORD_STATUS="RECORD_STATUS";

    //INCREMENTAL
    private static final String WORKORDER_ID="WORKORDER_ID";
    private static final String STATUS="STATUS";
    private static final String WO_STATUS="WO_STATUS";

    private static final String WO_ID="WO_ID";
    private static final String SURVEY_STATUS="SURVEY_STATUS";
    private static final String IS_FEEDER_TYPE="IS_FEEDER_TYPE";
    private static final String SURVEY_TYPE="SURVEY_TYPE";

    private static final String DTR_PRO_EXT ="DTR_PRO_EXT";
    private static final String ESTIMATE_NO="ESTIMATE_NO";
    private static final String POLE_SURVEY_TYPE="POLE_SURVEY_TYPE";
    private static final String FIXURE_TYPE="FIXURE_TYPE";
    private static final String GPS_ACCURACY="GPS_ACCURACY";



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
            + DTR_PRO_EXT + " TEXT ,"
            + ESTIMATE_NO + " TEXT ,"
            + POLE_SURVEY_TYPE + " TEXT ,"
            + FIXURE_TYPE + " TEXT ,"
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
            + DTR_PRO_EXT + " TEXT ,"
            + ESTIMATE_NO + " TEXT ,"
            + POLE_SURVEY_TYPE + " TEXT ,"
            + FIXURE_TYPE + " TEXT ,"
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
            + WORKORDER_ID + " TEXT ," + WO_STATUS + " TEXT ," + IS_FEEDER_TYPE + " TEXT ,"+ SURVEY_TYPE + " TEXT ," + FLAG3 + " TEXT " + ")";



    String CREATE_WO_STATUS_MASTER_TABLE = "CREATE TABLE " + WO_STATUS_TABLE + "("
            + FEEDER_ID + " TEXT ," + WO_ID + " TEXT ," + WO_STATUS + " TEXT ," + FLAG1 + " TEXT ," + FLAG2 + " TEXT ," + FLAG3 + " TEXT " + ")";



    public ArcGISDatabase(Context context) {
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
        String selectQuery =condition ;


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

    public boolean addUserMasterData(com.apepdcl.gis_survey.vo.LoginDataResponse loginDataResponse) {
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
        values.put(IS_FEEDER_TYPE,feederDataVo.getIS_FEEDER_TYPE());



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
            values.put(SURVEY_TYPE, surveyVo.getSurvaytype());
            values.put(DTR_PRO_EXT, surveyVo.getDTR_PRO_EXT());
            values.put(ESTIMATE_NO, surveyVo.getESTIMATE_NO());
            values.put(POLE_SURVEY_TYPE, surveyVo.getPOLE_SURVEY_TYPE());
            values.put(FIXURE_TYPE, surveyVo.getFIXURE_TYPE());
            values.put(GPS_ACCURACY, surveyVo.getGPS_ACCURACY());
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
                    vo.setDTR_PRO_EXT(cursor.getString(49));
                    vo.setESTIMATE_NO(cursor.getString(50));

                    vo.setPOLE_SURVEY_TYPE(cursor.getString(51));
                    vo.setFIXURE_TYPE(cursor.getString(52));
                    vo.setGPS_ACCURACY(cursor.getString(53));
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
                    vo.setDTR_PRO_EXT(cursor.getString(49));
                    vo.setESTIMATE_NO(cursor.getString(50));
                    vo.setPOLE_SURVEY_TYPE(cursor.getString(51));
                    vo.setFIXURE_TYPE(cursor.getString(52));
                    vo.setGPS_ACCURACY(cursor.getString(53));
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
            values.put(DTR_PRO_EXT, surveyVo.getDTR_PRO_EXT());
            values.put(ESTIMATE_NO, surveyVo.getESTIMATE_NO());
            values.put(POLE_SURVEY_TYPE, surveyVo.getPOLE_SURVEY_TYPE());
            values.put(FIXURE_TYPE, surveyVo.getFIXURE_TYPE());
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
                    vo.setDTR_PRO_EXT(cursor.getString(49));
                    vo.setESTIMATE_NO(cursor.getString(50));

                    vo.setPOLE_SURVEY_TYPE(cursor.getString(51));
                    vo.setFIXURE_TYPE(cursor.getString(52));
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
        boolean status=false;
        SQLiteDatabase sqldb = this.getWritableDatabase();
        try {
            String Query = "Select * from " + FEEDER_SERVICE_MASTER_TABLE + " where " + FS_CONSUMER_NUMBER + " = '" + ht_number+"'";
            Cursor cursor = sqldb.rawQuery(Query, null);
            if (cursor.getCount() <= 0) {
                cursor.close();
                return false;
            }else{
                status=true;
            }
            cursor.close();
            sqldb.close();
        }catch (Exception e){
         sqldb.close();
        }finally {
            sqldb.close();
        }
        return status;
    }

    public boolean checkValueDTRCodeMethod(String s) {
        boolean status=false;
        SQLiteDatabase sqldb = this.getWritableDatabase();
        try {
            String Query = "Select * from " + DTR_MASTER_TABLE + " where " + DTR_STRUCTURE_CODE + " = '" + s+"'";
            Cursor cursor = sqldb.rawQuery(Query, null);
            if (cursor.getCount() <= 0) {
                cursor.close();
                return false;
            }else{
                status=true;
            }
            cursor.close();
            sqldb.close();
        }catch (Exception e){
            sqldb.close();
        }finally {
            sqldb.close();
        }
        return status;

    }

    public boolean checkValueDTRCodeLoacalSave(String s) {
        boolean status=false;
        SQLiteDatabase sqldb = this.getWritableDatabase();
        try {
            String Query = "Select * from " + ARCGIS_TABLE + " where " + DTR_CODE1 + " = '" + s+"'";
            Cursor cursor = sqldb.rawQuery(Query, null);
            if (cursor.getCount() <= 0) {
                cursor.close();
                return false;
            }else{
                status=true;
            }
            cursor.close();
            sqldb.close();
        }catch (Exception e){
            sqldb.close();
        }finally {
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

                    IncrementalFeederDataVo incrementalFeederDataVo= new IncrementalFeederDataVo();
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
        boolean status=false;
        SQLiteDatabase sqldb = this.getWritableDatabase();
        try {
            String Query = "Select * from " + INCREMENTAL_DTR_MASTER_TABLE + " where " + DTR_STRUCTURE_CODE + " = '" + s+"'";
            Cursor cursor = sqldb.rawQuery(Query, null);
            if (cursor.getCount() <= 0) {
                cursor.close();
                return false;
            }else{
                status=true;
            }
            cursor.close();
            sqldb.close();
        }catch (Exception e){
            sqldb.close();
        }finally {
            sqldb.close();
        }
        return status;

    }



    public boolean checkIncrementalValueDTRCodeLoacalSave(String s) {
        boolean status=false;
        SQLiteDatabase sqldb = this.getWritableDatabase();
        try {
            String Query = "Select * from " + INCREMENTAL_ARCGIS_TABLE + " where " + DTR_CODE1 + " = '" + s+"'";
            Cursor cursor = sqldb.rawQuery(Query, null);
            if (cursor.getCount() <= 0) {
                cursor.close();
                return false;
            }else{
                status=true;
            }
            cursor.close();
            sqldb.close();
        }catch (Exception e){
            sqldb.close();
        }finally {
            sqldb.close();
        }
        return status;

    }


    public boolean checkIncrementalValueMethod(String ht_number) {
        boolean status=false;
        SQLiteDatabase sqldb = this.getWritableDatabase();
        try {
            String Query = "Select * from " + INCREMENTAL_FEEDER_SERVICE_MASTER_TABLE + " where " + FS_CONSUMER_NUMBER + " = '" + ht_number+"'";
            Cursor cursor = sqldb.rawQuery(Query, null);
            if (cursor.getCount() <= 0) {
                cursor.close();
                return false;
            }else{
                status=true;
            }
            cursor.close();
            sqldb.close();
        }catch (Exception e){
            sqldb.close();
        }finally {
            sqldb.close();
        }
        return status;
    }


    public boolean addWoStatusMasterData(WoStatusMasterVo woStatusMasterVo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FEEDER_ID,woStatusMasterVo.getFEEDER_ID());
        values.put(WO_ID, woStatusMasterVo.getWO_ID());
        values.put(WO_STATUS, woStatusMasterVo.getWO_STATUS());
        long a = db.insert(WO_STATUS_TABLE, null, values);
        if(a==-1){
            db.close();
            return false;
        }else {
            db.close();
            return true;
        }

    }
    public List<WoStatusMasterVo> getWoStatusMasterData(){
        ArrayList<WoStatusMasterVo> woStatusMasterVosList= new ArrayList<WoStatusMasterVo>();

        String  query = "SELECT * FROM WO_STATUS_TABLE";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                WoStatusMasterVo woStatusMasterVo= new WoStatusMasterVo();
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
    public List<DistandeVo> getDistance() {
        List<DistandeVo> distandeVoArrayList = new ArrayList<>();
        // Select All Query
        String selectQuery ="SELECT  POLE_NO,LAT,LAN  FROM INCREMENTAL_ARCGIS_TABLE  WHERE ROWID IN(SELECT MAX(ROWID) FROM INCREMENTAL_ARCGIS_TABLE  WHERE POLE_NO=(\n" +
                "SELECT  MAX(POLE_NO) FROM INCREMENTAL_ARCGIS_TABLE)) ";


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    DistandeVo distandeVo= new DistandeVo();
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
