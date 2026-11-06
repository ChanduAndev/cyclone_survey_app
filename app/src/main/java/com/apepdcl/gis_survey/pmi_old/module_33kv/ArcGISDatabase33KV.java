package com.apepdcl.gis_survey.pmi_old.module_33kv;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIDistandeVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIIncrementalSurveyVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMISurveyVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIWoStatusMasterVo;
import com.apepdcl.gis_survey.pmi_old.module_33kv.vo.PMIFeederDataVo;
import com.apepdcl.gis_survey.pmi_old.module_33kv.vo.PMIIncrementalFeederDataVo;
import com.apepdcl.gis_survey.pmi_old.module_33kv.vo.PMISSMasterVo;
import com.apepdcl.gis_survey.vo.LoginDataResponse;

import java.util.ArrayList;
import java.util.List;

public class ArcGISDatabase33KV extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "GIS_PMI_JHC_33KV";
    private static final String ARCGIS_TABLE ="ARCGIS_TABLE";
    private static final String FEEDERMASTER_TABLE="FEEDERMASTER_TABLE";
    private static final String EHTMASTER_TABLE="EHTMASTER_TABLE";
    private static final String USERMASTER_TABLE="USERMASTER_TABLE";
    private static final String SSMASTER_TABLE="SSMASTER_TABLE";
    private static final String CMPEHTMASTER_TABLE="CMPEHTMASTER_TABLE";

    private static final String INCRFEMENTAL_EHTMASTER_TABLE="INCRFEMENTAL_EHTMASTER_TABLE";
    private static final String INCREMENTAL_ARCGIS_TABLE="INCREMENTAL_ARCGIS_TABLE";
    private static final String WO_STATUS_TABLE="WO_STATUS_TABLE";
    private static final String INCREMENTAL_SSMASTER_TABLE="INCREMENTAL_SSMASTER_TABLE";



    private static final String FEEDER_ID="FEEDER_ID";
    private static final String POLE_NO="POLE_NO";
    private static final String FROM_POLE="FROM_POLE";
    private static final String POLE_TYPE="POLE_TYPE";
    private static final String POLE_HEIGHT="POLE_HEIGHT";
    private static final String POLE_SUPPORT="POLE_SUPPORT";
    private static final String TAPPING_POINT="TAPPING_POINT";
    private static final String CONDUCTOR_SIZE="CONDUCTOR_SIZE";
    private static final String POLE_CONFIGURATION="POLE_CONFIGURATION";
    private static final String PINPOINT="PINPOINT";
    private static final String POLE_PAINTINGCODE="POLE_PAINTINGCODE";
    private static final String NO_CKT="NO_CKT";
    private static final String CKT_TYPE="CKT_TYPE";
    private static final String LAT="LAT";
    private static final String LAN="LAN";
    private static final String FLAG1="FLAG1";
    private static final String FLAG2="FLAG2";
    private static final String FLAG3="FLAG3";
    private static final String FLAG4="FLAG4";
    //FeederMaster
    private static final String SECTION_ID="SECTION_ID";
    private static final String EHT_SS_CODE="EHT_SS_CODE";
    private static final String EHT_SS_NAME="EHT_SS_NAME";
    private static final String VOLTAGE_LEVEL="VOLTAGE_LEVEL";
    private static final String FEEDER_NAME_33KV="FEEDER_NAME_33KV";
    private static final String FEEDER_CODE_33KV="FEEDER_CODE_33KV";
    private static final String ROLE="ROLE";
    private static final String OFFICE_ID="OFFICE_ID";
    private static final String SECTION_NAME="SECTION_NAME";
    private static final String SEC_CODE="SEC_CODE";
    private static final String IS_ENDPOLE="IS_ENDPOLE";
    private static final String LINE_TYPE="LINE_TYPE";
    private static final String NOOF_CIRCUITS="NOOF_CIRCUITS";
    private static final String POLE_STRUCTURE="POLE_STRUCTURE";
    private static final String SS_NAME="SS_NAME";
    private static final String DIFF_VOLTAGE="DIFF_VOLTAGE";
    private static final String SS_ID="SS_ID";
    private static final String STJOINT_RMU="STJOINT_RMU";
    private static final String RMU_TYPE="RMU_TYPE";
    private static final String REMARKS="REMARKS";
    private static final String TAPPING_LOCNAME="TAPPING_LOCNAME";
    private static final String TAPPING_POINTS="TAPPING_POINTS";

    private static final String WORKORDER_ID="WORKORDER_ID";
    private static final String STATUS="STATUS";
    private static final String WO_ID="WO_ID";
    private static final String WO_STATUS="WO_STATUS";
    private static final String SURVEY_STATUS="SURVEY_STATUS";
    private static final String IS_FEEDER_TYPE="IS_FEEDER_TYPE";
    private static final String HT_SCNO="HT_SCNO";
    private static final String SURVEY_TYPE="SURVEY_TYPE";

    //MULTI CKT
    private static final String MC_33KV_FEEDER_ID_1="MC_33KV_FEEDER_ID_1";
    private static final String MC_33KV_FEEDER_ID_2 ="MC_33KV_FEEDER_ID_2";
    private static final String MC_33KV_FEEDER_ID_3="MC_33KV_FEEDER_ID_3";
    private static final String MC_33KV_POLE_1="MC_33KV_POLE_1";
    private static final String MC_33KV_POLE_2="MC_33KV_POLE_2";
    private static final String MC_33KV_POLE_3="MC_33KV_POLE_3";
    private static final String MC_11KV_FEEDER_ID_1="MC_11KV_FEEDER_ID_1";
    private static final String MC_11KV_FEEDER_ID_2="MC_11KV_FEEDER_ID_2";
    private static final String MC_11KV_FEEDER_ID_3="MC_11KV_FEEDER_ID_3";
    private static final String MC_11KV_POLE_1="MC_11KV_POLE_1";
    private static final String MC_11KV_POLE_2="MC_11KV_POLE_2";
    private static final String MC_11KV_POLE_3="MC_11KV_POLE_3";
    private static final String MC_LT_FEEDER_ID_1="MC_LT_FEEDER_ID_1";
    private static final String MC_LT_FEEDER_ID_2="MC_LT_FEEDER_ID_2";
    private static final String MC_LT_FEEDER_ID_3="MC_LT_FEEDER_ID_3";
    private static final String MC_LT_POLE_1="MC_LT_POLE_1";
    private static final String MC_LT_POLE_2="MC_LT_POLE_2";
    private static final String MC_LT_POLE_3="MC_LT_POLE_3";

    private static final String NO_33KV="NO_33KV";
    private static final String NO_11KV="NO_11KV";
    private static final String NO_LT="NO_LT";

    //DEFECTS
    private static final String DEFFECTS_AVAILABLE = "DEFFECTS_AVAILABLE";
    private static final String POLE_DEFECTS = "POLE_DEFECTS";
    private static final String CONDUCTOR_DEFECTS = "CONDUCTOR_DEFECTS";
    private static final String AB_SWITCH_DEFECTS = "AB_SWITCH_DEFECTS";
    private static final String INSULATOR_DEFECTS = "INSULATOR_DEFECTS";
    private static final String CROSSARM_DEFECTS = "CROSSARM_DEFECTS"	;
    private static final String TOPCLEAT_DEFECTS = "TOPCLEAT_DEFECTS";
    private static final String STRUT_DEFECTS = "STRUT_DEFECTS";
    private static final String STRAY_DEFECTS = "STRAY_DEFECTS";
    private static final String HTLT_CLEARANCE = "HTLT_CLEARANCE";
    private static final String INTERMEDIATE_POLE = "INTERMEDIATE_POLE";
    private static final String RESTRINGING = "RESTRINGING";
    private static final String TREE_CLEARANCE = "TREE_CLEARANCE";
    private static final String OTHER_DEFFECTS = "OTHER_DEFFECTS";

    //CROSSING NEW
    private static final String CROSS_LT_CKT="CROSS_LT_CKT";
    private static final String CROSS_6_3KV_ID="CROSS_6_3KV_ID";
    private static final String CROSS_11KV_ID="CROSS_11KV_ID";
    private static final String CROSS_33KV_ID="CROSS_33KV_ID";
    private static final String CROSS_EHT="CROSS_EHT";
    private static final String CROSS_COMMUN="CROSS_COMMUN";
    private static final String CROSS_REMARKS="CROSS_REMARKS";
    private static final String CROSSING_TYPE = "CROSSING_TYPE";

    //OTHER CABLES
    private static final String NUM_OTHER_CABLES="NUM_OTHER_CABLES";

    //POLE CONCRETE
    private static final String POLE_CONCRETE="POLE_CONCRETE";

    //CURRENT LOCATION
    private static final String CURRENT_LAT="CURRENT_LAT";
    private static final String CURRENT_LONG="CURRENT_LONG";

    //MULTI CKT NOT IN LIST
    private static final String MC_33KV_NOT_IN_LIST_1="MC_33KV_NOT_IN_LIST_1";
    private static final String MC_33KV_NOT_IN_LIST_2="MC_33KV_NOT_IN_LIST_2";
    private static final String MC_33KV_NOT_IN_LIST_3="MC_33KV_NOT_IN_LIST_3";
    private static final String MC_11KV_NOT_IN_LIST_1="MC_11KV_NOT_IN_LIST_1";
    private static final String MC_11KV_NOT_IN_LIST_2="MC_11KV_NOT_IN_LIST_2";
    private static final String MC_11KV_NOT_IN_LIST_3="MC_11KV_NOT_IN_LIST_3";
    private static final String MC_LT_NOT_IN_LIST_1="MC_LT_NOT_IN_LIST_1";
    private static final String MC_LT_NOT_IN_LIST_2="MC_LT_NOT_IN_LIST_2";
    private static final String MC_LT_NOT_IN_LIST_3="MC_LT_NOT_IN_LIST_3";

    //IMAGES
    private static final String IMAGE_11KV_TABLE="IMAGE_33KV_TABLE";
    private static final String IMAGE_1 = "IMAGE_1";
    private static final String IMAGE_2 = "IMAGE_2";
    private static final String IMAGE_3 = "IMAGE_3";

    String CREATE_IMAGE_11KV_TABLE = "CREATE TABLE " + IMAGE_11KV_TABLE + "(" + FEEDER_ID + " TEXT ," + POLE_NO + " INTEGER," + IMAGE_1 + " TEXT ," + IMAGE_2 + " TEXT ,"+ IMAGE_3 + " TEXT " + ")";






    String CREATE_SSMASTER_TABLE = "CREATE TABLE " + SSMASTER_TABLE + "("
            + EHT_SS_CODE + " TEXT ," + EHT_SS_NAME + " TEXT ," + FEEDER_CODE_33KV + " TEXT ," + FLAG1 + " TEXT ," + FLAG2 + " TEXT ," + FLAG3 + " TEXT " + ")";



    String CREATE_INCREMENTAL_SSMASTER_TABLE = "CREATE TABLE " + INCREMENTAL_SSMASTER_TABLE + "("
            + EHT_SS_CODE + " TEXT ," + EHT_SS_NAME + " TEXT ," + FEEDER_CODE_33KV + " TEXT ," + FLAG1 + " TEXT ," + FLAG2 + " TEXT ," + FLAG3 + " TEXT " + ")";


    String CREATE_USERMASTER_TABLE = "CREATE TABLE " + USERMASTER_TABLE + "("
            + ROLE + " TEXT ," + OFFICE_ID + " TEXT ," + SECTION_NAME + " TEXT ," + SEC_CODE + " TEXT ," + FLAG1 + " TEXT ," + FLAG2 + " TEXT ," + FLAG3 + " TEXT " + ")";


    String CREATE_EHTMASTER_TABLE = "CREATE TABLE " + EHTMASTER_TABLE + "("
            + EHT_SS_CODE + " TEXT ," + EHT_SS_NAME + " TEXT ," + FEEDER_NAME_33KV + " TEXT ," + FEEDER_CODE_33KV + " TEXT ," + WO_ID + " TEXT ," + IS_FEEDER_TYPE + " TEXT ," + FLAG3 + " TEXT " + ")";



    String CREATE_INCRFEMENTAL_EHTMASTER_TABLE = "CREATE TABLE " + INCRFEMENTAL_EHTMASTER_TABLE + "("
            + EHT_SS_CODE + " TEXT ," + EHT_SS_NAME + " TEXT ," + FEEDER_NAME_33KV + " TEXT ," + FEEDER_CODE_33KV + " TEXT ," + WORKORDER_ID + " TEXT ," + STATUS + " TEXT ," + FROM_POLE + " TEXT ," + FLAG2 + " TEXT " + ")";


    /* String CREATE_FEEDERMASTER_TABLE = "CREATE TABLE " + FEEDERMASTER_TABLE + "("
             + SECTION_ID + " TEXT ," + EHT_SS_CODE + " TEXT ," + EHT_SS_NAME + " TEXT ," + VOLTAGE_LEVEL + " TEXT," + FEEDER_NAME_33KV + " TEXT ," + FEEDER_CODE_33KV + " TEXT ," + FLAG1 + " TEXT ," + FLAG2 + " TEXT ," + FLAG3 + " TEXT " + ")";*/
    String CREATE_ARCGIS_TABLE = "CREATE TABLE " + ARCGIS_TABLE + "("
            + FEEDER_ID + " TEXT ," + POLE_NO + " INTEGER UNIQUE," + FROM_POLE + " INTEGER ," + POLE_TYPE + " TEXT," + POLE_HEIGHT + " TEXT ," + POLE_SUPPORT + " TEXT ,"+ TAPPING_POINT + " TEXT ,"+ CONDUCTOR_SIZE + " TEXT ," + POLE_CONFIGURATION + " TEXT ," + PINPOINT + " TEXT ,"+ POLE_PAINTINGCODE + " TEXT ," + LAT + " TEXT ," + LAN + " TEXT ," + FLAG1 + " TEXT ," + NO_CKT + " TEXT ," + CKT_TYPE + " TEXT ,"+ IS_ENDPOLE + " TEXT ," + LINE_TYPE + " TEXT ," + NOOF_CIRCUITS + " TEXT ," + SS_NAME + " TEXT ," + POLE_STRUCTURE + " TEXT ," + DIFF_VOLTAGE + " TEXT ," + SS_ID + " TEXT ," + STJOINT_RMU + " TEXT ," + RMU_TYPE + " TEXT ," + REMARKS + " TEXT ,"+ TAPPING_LOCNAME + " TEXT ," + TAPPING_POINTS + " TEXT ," + WO_ID + " TEXT ," + IS_FEEDER_TYPE + " TEXT ,"  + HT_SCNO + " TEXT ,"  +
            SURVEY_TYPE + " TEXT ,"

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

            + CROSS_LT_CKT + " TEXT ,"
            + CROSS_6_3KV_ID + " TEXT ,"
            + CROSS_11KV_ID + " TEXT ,"
            + CROSS_33KV_ID + " TEXT ,"
            + CROSS_EHT + " TEXT ,"
            + CROSS_COMMUN + " TEXT ,"
            + CROSS_REMARKS + " TEXT ,"
            + CROSSING_TYPE + " TEXT ,"

            + NUM_OTHER_CABLES + " TEXT ,"
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

            + FLAG3 + " TEXT ,"  + FLAG4 + " TEXT " + ")";




    String CREATE_INCREMENTALARCGIS_TABLE = "CREATE TABLE " + INCREMENTAL_ARCGIS_TABLE + "("
            + FEEDER_ID + " TEXT ," + POLE_NO + " INTEGER UNIQUE," + FROM_POLE + " INTEGER ," + POLE_TYPE + " TEXT," + POLE_HEIGHT + " TEXT ," + POLE_SUPPORT + " TEXT ,"+ TAPPING_POINT + " TEXT ,"+ CONDUCTOR_SIZE + " TEXT ," + POLE_CONFIGURATION + " TEXT ," + PINPOINT + " TEXT ,"+ POLE_PAINTINGCODE + " TEXT ," + LAT + " TEXT ," + LAN + " TEXT ," + FLAG1 + " TEXT ," + NO_CKT + " TEXT ," + CKT_TYPE + " TEXT ,"+ IS_ENDPOLE + " TEXT ," + LINE_TYPE + " TEXT ," + NOOF_CIRCUITS + " TEXT ," + SS_NAME + " TEXT ," + POLE_STRUCTURE + " TEXT ," + DIFF_VOLTAGE + " TEXT ," + SS_ID + " TEXT ," + STJOINT_RMU + " TEXT ," + RMU_TYPE + " TEXT ," + REMARKS + " TEXT ,"+ TAPPING_LOCNAME + " TEXT ," + TAPPING_POINTS + " TEXT ," + WORKORDER_ID + " TEXT ," + WO_STATUS + " TEXT ," + IS_FEEDER_TYPE + " TEXT ,"  +HT_SCNO + " TEXT ,"  + FLAG4 + " TEXT " + ")";




    String CREATE_CMPEHTMASTER_TABLE = "CREATE TABLE " + CMPEHTMASTER_TABLE + "("
            + EHT_SS_CODE + " TEXT ," + EHT_SS_NAME + " TEXT ," + FEEDER_NAME_33KV + " TEXT ," + FEEDER_CODE_33KV + " TEXT ," + FLAG1 + " TEXT ," + FLAG2 + " TEXT ," + FLAG3 + " TEXT " + ")";




    String CREATE_WO_STATUS_MASTER_TABLE = "CREATE TABLE " + WO_STATUS_TABLE + "("
            + FEEDER_ID + " TEXT ," + WO_ID + " TEXT ," + WO_STATUS + " TEXT ," + FLAG1 + " TEXT ," + FLAG2 + " TEXT ," + FLAG3 + " TEXT " + ")";


    public ArcGISDatabase33KV(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ARCGIS_TABLE);
        // db.execSQL(CREATE_FEEDERMASTER_TABLE);
        db.execSQL(CREATE_USERMASTER_TABLE);
        db.execSQL(CREATE_EHTMASTER_TABLE);
        db.execSQL(CREATE_SSMASTER_TABLE);
        db.execSQL(CREATE_CMPEHTMASTER_TABLE);
        db.execSQL(CREATE_INCRFEMENTAL_EHTMASTER_TABLE);
        db.execSQL(CREATE_INCREMENTALARCGIS_TABLE);
        db.execSQL(CREATE_WO_STATUS_MASTER_TABLE);
        db.execSQL(CREATE_INCREMENTAL_SSMASTER_TABLE);
        db.execSQL(CREATE_IMAGE_11KV_TABLE);


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
        db.execSQL("DROP TABLE IF EXISTS " + CMPEHTMASTER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + INCRFEMENTAL_EHTMASTER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + INCREMENTAL_SSMASTER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + IMAGE_11KV_TABLE);



        // Create tables again
        onCreate(db);
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
        db.execSQL("DELETE FROM " +USERMASTER_TABLE);
    }
    public void clearFeederData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " +EHTMASTER_TABLE);
    }

    public void clearIncrementalFeederData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " +INCRFEMENTAL_EHTMASTER_TABLE);
    }
    public void clearIncrementalSurveyedData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + INCREMENTAL_ARCGIS_TABLE);
    }


    public void clearssMasterData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " +SSMASTER_TABLE);
    }
    public void clearIncrementalssMasterData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " +INCREMENTAL_SSMASTER_TABLE);
    }

    public void clearWOStatusMasterData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " +WO_STATUS_TABLE);
    }


    public void cmpClearFeederData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " +CMPEHTMASTER_TABLE);
    }

    public void clearData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " +ARCGIS_TABLE);
        db.execSQL("DELETE FROM " +USERMASTER_TABLE);
        db.execSQL("DELETE FROM " +EHTMASTER_TABLE);
        db.execSQL("DELETE FROM " +SSMASTER_TABLE);
        db.execSQL("DELETE FROM " +CMPEHTMASTER_TABLE);
        db.execSQL("DELETE FROM " +INCRFEMENTAL_EHTMASTER_TABLE);
        db.execSQL("DELETE FROM " +INCREMENTAL_ARCGIS_TABLE);
        db.execSQL("DELETE FROM " +WO_STATUS_TABLE);
        db.execSQL("DELETE FROM " +INCREMENTAL_SSMASTER_TABLE);
        db.execSQL("DELETE FROM " +IMAGE_11KV_TABLE);

    }



    public void upDate(String sQuery) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sQuery);

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
        List<String> ssList=new ArrayList<>();
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
            }while (cursor.moveToNext());

            db.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
        // return contact list
        return ssList;
    }
    public List<String> getFeederName() {
        List<String> feederList =new ArrayList<>();
        String feederName="";
        // Select All Query
        try {
            String selectQuery = "SELECT FEEDER_NAME_33KV FROM FEEDERMASTER_TABLE";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                feederName = cursor.getString(0);
                feederList.add(feederName);
            }while (cursor.moveToNext());


        } catch (Exception e) {
            e.printStackTrace();

        }
        // return contact list
        return feederList;
    }

    public List<String> getEHTSSNAME() {
        List<String> stringList = new ArrayList<>();
        // Select All Query
        String selectQuery ="SELECT DISTINCT  EHT_SS_CODE|| '-' ||EHT_SS_NAME FROM EHTMASTER_TABLE WHERE FEEDER_CODE_33KV IS NOT NULL";


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
        String selectQuery ="SELECT DISTINCT  EHT_SS_CODE|| '-' ||EHT_SS_NAME FROM CMPEHTMASTER_TABLE WHERE FEEDER_CODE_33KV IS NOT NULL";


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
    public List<String> getIncrementalSSNAMEList() {
        List<String> stringList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT EHT_SS_NAME FROM INCREMENTAL_SSMASTER_TABLE";


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
        String selectQuery ="SELECT DISTINCT  EHT_SS_CODE|| '-' ||EHT_SS_NAME FROM INCRFEMENTAL_EHTMASTER_TABLE WHERE FEEDER_CODE_33KV IS NOT NULL";


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


    public List<String> getIncrementalFeederComplaint(String condition) {
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

    public List<String> getIncrementalFeederComplaintTest(String condition) {
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





    public List<String> getComplaint(String condition) {
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


    public boolean addSSMasterData(PMISSMasterVo ssMasterVo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EHT_SS_CODE,ssMasterVo.getFEEDING_SS_CODE());
        values.put(EHT_SS_NAME, ssMasterVo.getFEEDING_SS_NAME());
        values.put(FEEDER_CODE_33KV, ssMasterVo.getFEEDER_CODE_33KV());

        long a = db.insert(SSMASTER_TABLE, null, values);
        if(a==-1){
            db.close();
            return false;
        }else {
            db.close();
            return true;
        }

    }

    public boolean addIncrementalSSMasterData(PMISSMasterVo ssMasterVo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EHT_SS_CODE,ssMasterVo.getFEEDING_SS_CODE());
        values.put(EHT_SS_NAME, ssMasterVo.getFEEDING_SS_NAME());
        values.put(FEEDER_CODE_33KV, ssMasterVo.getFEEDER_CODE_33KV());

        long a = db.insert(INCREMENTAL_SSMASTER_TABLE, null, values);
        if(a==-1){
            db.close();
            return false;
        }else {
            db.close();
            return true;
        }

    }



    public List<PMISSMasterVo> getSSMasterData(){
        ArrayList<PMISSMasterVo> ssResponseList= new ArrayList<PMISSMasterVo>();

        String  query = "SELECT * FROM SSMASTER_TABLE";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {

                PMISSMasterVo ssMasterVo= new PMISSMasterVo();
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
        values.put(ROLE,loginDataResponse.getROLE());
        values.put(OFFICE_ID, loginDataResponse.getOFFICE_ID());
        values.put(SECTION_NAME, loginDataResponse.getSECTION_NAME());
        values.put(SEC_CODE, loginDataResponse.getSEC_CODE());

        long a = db.insert(USERMASTER_TABLE, null, values);
        if(a==-1){
            db.close();
            return false;
        }else {
            db.close();
            return true;
        }

    }
    public List<LoginDataResponse> getUserMasterData(){
        ArrayList<LoginDataResponse> loginDataResponses= new ArrayList<LoginDataResponse>();

        String  query = "SELECT * FROM USERMASTER_TABLE";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {

                LoginDataResponse loginVo= new LoginDataResponse();
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

    public boolean addFeederData(PMIFeederDataVo feederDataVo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EHT_SS_CODE,feederDataVo.getEHT_SS_CODE());
        values.put(EHT_SS_NAME, feederDataVo.getEHT_SS_NAME());
        values.put(FEEDER_NAME_33KV, feederDataVo.getFEEDER_NAME_33KV());
        values.put(FEEDER_CODE_33KV, feederDataVo.getFEEDER_CODE_33KV());
        values.put(WO_ID, feederDataVo.getWOID());
        values.put(IS_FEEDER_TYPE, feederDataVo.getIS_FEEDER_TYPE());


        long a = db.insert(EHTMASTER_TABLE, null, values);
        if(a==-1){
            db.close();
            return false;
        }else {
            db.close();
            return true;
        }

    }

    public List<PMIFeederDataVo> getFeerData(){
        ArrayList<PMIFeederDataVo> feederDataVos= new ArrayList<PMIFeederDataVo>();

        String  query = "SELECT * FROM EHTMASTER_TABLE";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {

                PMIFeederDataVo feederDataVo= new PMIFeederDataVo();
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
        return feederDataVos;
    }


    public boolean addIncrementalFeederData(PMIIncrementalFeederDataVo incrementalFeederDataVo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EHT_SS_CODE,incrementalFeederDataVo.getEHT_SS_CODE());
        values.put(EHT_SS_NAME, incrementalFeederDataVo.getEHT_SS_NAME());
        values.put(FEEDER_NAME_33KV, incrementalFeederDataVo.getFEEDER_NAME_33KV());
        values.put(FEEDER_CODE_33KV, incrementalFeederDataVo.getFEEDER_CODE_33KV());
        values.put(WORKORDER_ID, incrementalFeederDataVo.getWORKORDER_ID());
        values.put(STATUS, incrementalFeederDataVo.getSTATUS());
        values.put(FROM_POLE, incrementalFeederDataVo.getFROM_POLE());

        long a = db.insert(INCRFEMENTAL_EHTMASTER_TABLE, null, values);
        if(a==-1){
            db.close();
            return false;
        }else {
            db.close();
            return true;
        }

    }
    public List<PMIIncrementalFeederDataVo> getIncrementalFeerData(){
        ArrayList<PMIIncrementalFeederDataVo>incrementalFeederDataList= new ArrayList<PMIIncrementalFeederDataVo>();

        String  query = "SELECT * FROM INCRFEMENTAL_EHTMASTER_TABLE";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {

                PMIIncrementalFeederDataVo incrementalFeederDataVo= new PMIIncrementalFeederDataVo();
                //vo.setLat(cursor.getString(0));
                incrementalFeederDataVo.setEHT_SS_CODE(cursor.getString(0));
                incrementalFeederDataVo.setEHT_SS_NAME(cursor.getString(1));
                incrementalFeederDataVo.setFEEDER_NAME_33KV(cursor.getString(2));
                incrementalFeederDataVo.setFEEDER_CODE_33KV(cursor.getString(3));
                incrementalFeederDataVo.setWORKORDER_ID(cursor.getString(4));
                incrementalFeederDataVo.setSTATUS(cursor.getString(5));
                incrementalFeederDataVo.setFROM_POLE(cursor.getString(6));
                //  vo.setSECTION_CODE(cursor.getString(8));
                incrementalFeederDataList.add(incrementalFeederDataVo);
            } while (cursor.moveToNext());
        }

        db.close();
        return incrementalFeederDataList;
    }

    public boolean addCmpFeederData(PMIFeederDataVo feederDataVo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EHT_SS_CODE,feederDataVo.getEHT_SS_CODE());
        values.put(EHT_SS_NAME, feederDataVo.getEHT_SS_NAME());
        values.put(FEEDER_NAME_33KV, feederDataVo.getFEEDER_NAME_33KV());
        values.put(FEEDER_CODE_33KV, feederDataVo.getFEEDER_CODE_33KV());

        long a = db.insert(CMPEHTMASTER_TABLE, null, values);
        if(a==-1){
            db.close();
            return false;
        }else {
            db.close();
            return true;
        }

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
    public boolean addSurveyData(PMISurveyVo surveyVo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FEEDER_ID,surveyVo.getFeeder_id());
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
        values.put(WO_ID, surveyVo.getWoId());
        values.put(IS_FEEDER_TYPE, surveyVo.getIsFeederType());
        values.put(HT_SCNO, surveyVo.getHt_service_number());
        values.put(SURVEY_TYPE, surveyVo.getSurvaytype());

        //MULTI CKT
        values.put(MC_33KV_FEEDER_ID_1,surveyVo.getMC_33KV_FEEDER_ID_1());
        values.put(MC_33KV_FEEDER_ID_2,surveyVo.getMC_33KV_FEEDER_ID_2());
        values.put(MC_33KV_FEEDER_ID_3,surveyVo.getMC_33KV_FEEDER_ID_3());
        values.put(MC_33KV_POLE_1,surveyVo.getMC_33KV_POLE_1());
        values.put(MC_33KV_POLE_2,surveyVo.getMC_33KV_POLE_2());
        values.put(MC_33KV_POLE_3,surveyVo.getMC_33KV_POLE_3());

        values.put(MC_11KV_FEEDER_ID_1,surveyVo.getMC_11KV_FEEDER_ID_1());
        values.put(MC_11KV_FEEDER_ID_2,surveyVo.getMC_11KV_FEEDER_ID_2());
        values.put(MC_11KV_FEEDER_ID_3,surveyVo.getMC_11KV_FEEDER_ID_3());
        values.put(MC_11KV_POLE_1,surveyVo.getMC_11KV_POLE_1());
        values.put(MC_11KV_POLE_2,surveyVo.getMC_11KV_POLE_2());
        values.put(MC_11KV_POLE_3,surveyVo.getMC_11KV_POLE_3());

        values.put(MC_LT_FEEDER_ID_1,surveyVo.getMC_LT_FEEDER_ID_1());
        values.put(MC_LT_FEEDER_ID_2,surveyVo.getMC_LT_FEEDER_ID_2());
        values.put(MC_LT_FEEDER_ID_3,surveyVo.getMC_LT_FEEDER_ID_3());
        values.put(MC_LT_POLE_1,surveyVo.getMC_LT_POLE_1());
        values.put(MC_LT_POLE_2,surveyVo.getMC_LT_POLE_2());
        values.put(MC_LT_POLE_3,surveyVo.getMC_LT_POLE_3());

        values.put(NO_33KV,surveyVo.getNO_OF33KV_CKTS());
        values.put(NO_11KV,surveyVo.getNO_OF11KV_CKTS());
        values.put(NO_LT,surveyVo.getNO_OF_LT_CKTS());

        //DEFECTS
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

        //CROSSING NEW
        values.put(CROSS_LT_CKT,surveyVo.getCROSS_LT_CKT());
        values.put(CROSS_6_3KV_ID,surveyVo.getCROSS_6_3KV_ID());
        values.put(CROSS_11KV_ID,surveyVo.getCROSS_11KV_ID());
        values.put(CROSS_33KV_ID,surveyVo.getCROSS_33KV_ID());
        values.put(CROSS_EHT,surveyVo.getCROSS_EHT());
        values.put(CROSS_COMMUN,surveyVo.getCROSS_COMMUN());
        values.put(CROSS_REMARKS,surveyVo.getCROSS_REMARKS());
        values.put(CROSSING_TYPE, surveyVo.getCROSSING_TYPE());

        //OTHER CABLES
        values.put(NUM_OTHER_CABLES,surveyVo.getNUM_OTHER_CABLES());
        values.put(POLE_CONCRETE,surveyVo.getPOLE_CONCRETE());

        // System.out.println("CURRENT_LAT IN DB INSERT : "+surveyVo.getCURRENT_LAT());
        values.put(CURRENT_LAT,surveyVo.getCURRENT_LAT());
        values.put(CURRENT_LONG,surveyVo.getCURRENT_LONG());

        //MULTI CKT NOT IN LIST
        values.put(MC_33KV_NOT_IN_LIST_1,surveyVo.getMC_33KV_NOT_IN_LIST_1());
        values.put(MC_33KV_NOT_IN_LIST_2,surveyVo.getMC_33KV_NOT_IN_LIST_2());
        values.put(MC_33KV_NOT_IN_LIST_3,surveyVo.getMC_33KV_NOT_IN_LIST_3());
        values.put(MC_11KV_NOT_IN_LIST_1,surveyVo.getMC_11KV_NOT_IN_LIST_1());
        values.put(MC_11KV_NOT_IN_LIST_2,surveyVo.getMC_11KV_NOT_IN_LIST_2());
        values.put(MC_11KV_NOT_IN_LIST_3,surveyVo.getMC_11KV_NOT_IN_LIST_3());
        values.put(MC_LT_NOT_IN_LIST_1,surveyVo.getMC_LT_NOT_IN_LIST_1());
        values.put(MC_LT_NOT_IN_LIST_2,surveyVo.getMC_LT_NOT_IN_LIST_2());
        values.put(MC_LT_NOT_IN_LIST_3,surveyVo.getMC_LT_NOT_IN_LIST_3());


        long a = db.insert(ARCGIS_TABLE, null, values);
        if(a==-1){
            db.close();
            return false;
        }else {
            db.close();
            return true;
        }

    }

    public List<PMISurveyVo> checkMultiCktForLT() {
        String query = "SELECT * FROM ARCGIS_TABLE WHERE FLAG1='N'";
        ArrayList<PMISurveyVo> surveyVoArrayList= new ArrayList<PMISurveyVo>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do {
                PMISurveyVo vo = new PMISurveyVo();
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

                surveyVoArrayList.add(vo);

            }while (cursor.moveToNext());
        }
        db.close();
        return surveyVoArrayList;
    }


    public ArrayList<PMISurveyVo> getSurveyData(String strCondition) {
        String query = "";
        ArrayList<PMISurveyVo> surveyVoArrayList= new ArrayList<PMISurveyVo>();

        if(strCondition.equals("")) {
            query = "SELECT * FROM  ARCGIS_TABLE";
        }
        else {

            query =strCondition;

        }

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {

                PMISurveyVo vo = new PMISurveyVo();
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
                vo.setHt_service_number(cursor.getString(30));
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



    public List<PMISurveyVo> getData(){
        ArrayList<PMISurveyVo> surveyVoArrayList= new ArrayList<PMISurveyVo>();

        String  query = "SELECT * FROM ARCGIS_TABLE ORDER BY POLE_NO";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {

                PMISurveyVo vo = new PMISurveyVo();
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
                vo.setHt_service_number(cursor.getString(30));
                //  vo.setSECTION_CODE(cursor.getString(8));
                surveyVoArrayList.add(vo);
            } while (cursor.moveToNext());
        }

        db.close();
        return surveyVoArrayList;
    }



    public boolean addIncrementalSurveyData(PMIIncrementalSurveyVo incrementalSurveyVo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FEEDER_ID,incrementalSurveyVo.getFeeder_id());
        values.put(POLE_NO, incrementalSurveyVo.getPole_no());
        values.put(FROM_POLE, incrementalSurveyVo.getFrom_pole());
        values.put(POLE_TYPE, incrementalSurveyVo.getPole_type());
        values.put(POLE_HEIGHT, incrementalSurveyVo.getPole_height());
        // values.put(NOOF_CIRCUITS, surveyVo.getNoof_circuits());
        values.put(POLE_SUPPORT, incrementalSurveyVo.getPole_support());
        values.put(TAPPING_POINT, incrementalSurveyVo.getTapping_point());
        values.put(CONDUCTOR_SIZE, incrementalSurveyVo.getConductor_size());
        values.put(POLE_CONFIGURATION, incrementalSurveyVo.getPole_configuration());
        values.put(PINPOINT, incrementalSurveyVo.getPinpoint());
        values.put(POLE_PAINTINGCODE, incrementalSurveyVo.getPole_paintingcode());
        values.put(LAT, incrementalSurveyVo.getLat());
        values.put(LAN, incrementalSurveyVo.getLan());
        values.put(FLAG1, incrementalSurveyVo.getPastSurveyFlag());
        values.put(NO_CKT, incrementalSurveyVo.getIsMultiCircuit());
        values.put(CKT_TYPE, incrementalSurveyVo.getCircuitType());
        values.put(IS_ENDPOLE, incrementalSurveyVo.getIsEndPole());
        values.put(LINE_TYPE, incrementalSurveyVo.getLineType());
        values.put(NOOF_CIRCUITS, incrementalSurveyVo.getNoofCircuits());
        values.put(SS_NAME, incrementalSurveyVo.getSs_name());
        values.put(POLE_STRUCTURE, incrementalSurveyVo.getPoleStructure());
        values.put(DIFF_VOLTAGE, incrementalSurveyVo.getDiffVoltage());
        values.put(SS_ID, incrementalSurveyVo.getSsId());
        values.put(STJOINT_RMU, incrementalSurveyVo.getStjoint_rmu());
        values.put(RMU_TYPE, incrementalSurveyVo.getRmu_type());
        values.put(REMARKS, incrementalSurveyVo.getRemarks());
        values.put(TAPPING_LOCNAME, incrementalSurveyVo.getTappingLocation());
        values.put(TAPPING_POINTS, incrementalSurveyVo.getNoofTappings());
        values.put(WORKORDER_ID, incrementalSurveyVo.getWorkOrder());
        values.put(WO_STATUS, incrementalSurveyVo.getWorkOrderStatus());
        values.put(IS_FEEDER_TYPE, incrementalSurveyVo.getIsFeederType());
        values.put(HT_SCNO, incrementalSurveyVo.getHtServiceNumber());

        long a = db.insert(INCREMENTAL_ARCGIS_TABLE, null, values);
        if(a==-1){
            db.close();
            return false;
        }else {
            db.close();
            return true;
        }

    }


    public List<PMIIncrementalSurveyVo> getIncrementalSurveyData(String strCondition){
        ArrayList<PMIIncrementalSurveyVo> incrementalSurveyVosList= new ArrayList<PMIIncrementalSurveyVo>();

        String query = "";
        ArrayList<PMISurveyVo> surveyVoArrayList= new ArrayList<PMISurveyVo>();

        if(strCondition.equals("")) {
            query = "SELECT * FROM INCREMENTAL_ARCGIS_TABLE ORDER BY POLE_NO";
        }
        else {

            query =strCondition;

        }

        /// String  query = "SELECT * FROM INCREMENTAL_ARCGIS_TABLE ORDER BY POLE_NO";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {

                PMIIncrementalSurveyVo  incrementalSurveyVo= new PMIIncrementalSurveyVo();
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
                incrementalSurveyVo.setWorkOrder(cursor.getString(28));
                incrementalSurveyVo.setWorkOrderStatus(cursor.getString(29));
                incrementalSurveyVo.setIsFeederType(cursor.getString(30));
                incrementalSurveyVo.setHtServiceNumber(cursor.getString(31));
                //  vo.setSECTION_CODE(cursor.getString(8));
                incrementalSurveyVosList.add(incrementalSurveyVo);
            } while (cursor.moveToNext());
        }

        db.close();
        return incrementalSurveyVosList;
    }

    public boolean addWoStatusMasterData( PMIWoStatusMasterVo woStatusMasterVo) {
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
    public List<PMIWoStatusMasterVo> getWoStatusMasterData(){
        ArrayList<PMIWoStatusMasterVo> woStatusMasterVosList= new ArrayList<PMIWoStatusMasterVo>();

        String  query = "SELECT * FROM WO_STATUS_TABLE";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                PMIWoStatusMasterVo woStatusMasterVo= new PMIWoStatusMasterVo();
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


    public List<PMIDistandeVo> getDistance() {
        List<PMIDistandeVo> distandeVoArrayList = new ArrayList<>();
        // Select All Query
        String selectQuery ="SELECT  POLE_NO,LAT,LAN  FROM INCREMENTAL_ARCGIS_TABLE  WHERE ROWID IN(SELECT MAX(ROWID) FROM INCREMENTAL_ARCGIS_TABLE  WHERE POLE_NO=(\n" +
                "SELECT  MAX(POLE_NO) FROM INCREMENTAL_ARCGIS_TABLE)) ";


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    PMIDistandeVo distandeVo= new PMIDistandeVo();
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

    public boolean addImageData(PMISurveyVo surveyDataVo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put(FEEDER_ID, surveyDataVo.getFeeder_id());
            values.put(POLE_NO, surveyDataVo.getPole_no());
            values.put(IMAGE_1, surveyDataVo.getIMAGE_1());
            values.put(IMAGE_2, surveyDataVo.getIMAGE_2());
            values.put(IMAGE_3, surveyDataVo.getIMAGE_3());


        }catch (Exception e){
            e.printStackTrace();
            Log.v("SQLLLLLL==>",""+e.getMessage());
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


    public ArrayList<PMISurveyVo> getImageData(String strCondition) {
        String query = "";
        ArrayList<PMISurveyVo> surveyVoArrayList = new ArrayList<PMISurveyVo>();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (strCondition.equals("")) {
                query = "SELECT * FROM  IMAGE_33KV_TABLE";
            } else {

                query = strCondition;

            }


            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {

                    PMISurveyVo vo = new PMISurveyVo();
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

    public void clearImageData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + IMAGE_11KV_TABLE);
    }
}
