package com.apepdcl.gis_survey.module_33KV;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;


import com.apepdcl.gis_survey.estimates.models.PoleHeightModel;
import com.apepdcl.gis_survey.module_33KV.vo.DistandeVo;
import com.apepdcl.gis_survey.module_33KV.vo.FeederDataVo;
import com.apepdcl.gis_survey.module_33KV.vo.HtServiceListModel;
import com.apepdcl.gis_survey.module_33KV.vo.IncrementalFeederDataVo;
import com.apepdcl.gis_survey.module_33KV.vo.IncrementalSurveyVo;
import com.apepdcl.gis_survey.module_33KV.vo.LoginDataResponse;
import com.apepdcl.gis_survey.module_33KV.vo.SSMasterVo;
import com.apepdcl.gis_survey.module_33KV.vo.SurveyVo;
import com.apepdcl.gis_survey.module_33KV.vo.WoStatusMasterVo;

import java.util.ArrayList;
import java.util.List;

public class ArcGISDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 10;

    // Database Name
    private static final String DATABASE_NAME = "GIS_JHC_33KV";
    private static final String ARCGIS_TABLE = "ARCGIS_TABLE";
    private static final String HT_SERVICE_LIST_TABLE = "HT_SERVICE_LIST_TABLE";
    private static final String FEEDERMASTER_TABLE = "FEEDERMASTER_TABLE";
    private static final String EHTMASTER_TABLE = "EHTMASTER_TABLE";
    private static final String USERMASTER_TABLE = "USERMASTER_TABLE";
    private static final String SSMASTER_TABLE = "SSMASTER_TABLE";
    private static final String CMPEHTMASTER_TABLE = "CMPEHTMASTER_TABLE";

    private static final String INCRFEMENTAL_EHTMASTER_TABLE = "INCRFEMENTAL_EHTMASTER_TABLE";
    private static final String INCREMENTAL_ARCGIS_TABLE = "INCREMENTAL_ARCGIS_TABLE";
    private static final String WO_STATUS_TABLE = "WO_STATUS_TABLE";
    private static final String INCREMENTAL_SSMASTER_TABLE = "INCREMENTAL_SSMASTER_TABLE";
    private static final String ARCGIS_TABLE_LAT_LANG_EDIT = "ARCGIS_TABLE_LAT_LANG_EDIT";
    private static final String ARCGIS_TABLE_SOURCE_POLE_EDIT = "ARCGIS_TABLE_SOURCE_POLE_EDIT";
    private static final String ARCGIS_TABLE_POLE_DETAILS_EDIT = "ARCGIS_TABLE_POLE_DETAILS_EDIT";


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

    private static final String WORKORDER_ID = "WORKORDER_ID";
    private static final String STATUS = "STATUS";
    private static final String WO_ID = "WO_ID";
    private static final String WO_STATUS = "WO_STATUS";
    private static final String SURVEY_STATUS = "SURVEY_STATUS";
    private static final String IS_FEEDER_TYPE = "IS_FEEDER_TYPE";
    private static final String HT_SCNO = "HT_SCNO";
    private static final String HTSC_NAME = "HTSC_NAME";
    private static final String FLAG = "FLAG";
    private static final String SURVEY_TYPE = "SURVEY_TYPE";

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

    //DEFECTS
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

    //CROSSING NEW
    private static final String CROSS_LT_CKT = "CROSS_LT_CKT";
    private static final String CROSS_6_3KV_ID = "CROSS_6_3KV_ID";
    private static final String CROSS_11KV_ID = "CROSS_11KV_ID";
    private static final String CROSS_33KV_ID = "CROSS_33KV_ID";
    private static final String CROSS_EHT = "CROSS_EHT";
    private static final String CROSS_COMMUN = "CROSS_COMMUN";
    private static final String CROSS_REMARKS = "CROSS_REMARKS";
    private static final String CROSSING_TYPE = "CROSSING_TYPE";

    //OTHER CABLES
    private static final String NUM_OTHER_CABLES = "NUM_OTHER_CABLES";

    //POLE CONCRETE
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
    private static final String ID = "ID";
    private static final String GPS_ACCURACY = "GPS_ACCURACY";
    private static final String DEVICE_ID = "DEVICE_ID";

    //IMAGES
    private static final String IMAGE_11KV_TABLE = "IMAGE_33KV_TABLE";
    private static final String IMAGE_1 = "IMAGE_1";
    private static final String IMAGE_2 = "IMAGE_2";
    private static final String IMAGE_3 = "IMAGE_3";

    String CREATE_IMAGE_11KV_TABLE = "CREATE TABLE " + IMAGE_11KV_TABLE + "(" + FEEDER_ID + " TEXT ," + POLE_NO + " INTEGER," + IMAGE_1 + " TEXT ," + IMAGE_2 + " TEXT ," + IMAGE_3 + " TEXT " + ")";


    String CREATE_SSMASTER_TABLE = "CREATE TABLE " + SSMASTER_TABLE + "("
            + EHT_SS_CODE + " TEXT ,"
            + EHT_SS_NAME + " TEXT ,"
            + FEEDER_CODE_33KV + " TEXT ,"
            + LAT + " TEXT ,"
            + LAN + " TEXT ,"
            + MAX_LENGTH + " TEXT ,"
            + FLAG1 + " TEXT ,"
            + FLAG2 + " TEXT ," + FLAG3 + " TEXT " + ")";

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
            + FEEDER_ID + " TEXT ," + POLE_NO + " INTEGER UNIQUE," + FROM_POLE + " INTEGER ," + POLE_TYPE + " TEXT," + POLE_HEIGHT + " TEXT ," + POLE_SUPPORT + " TEXT ," + TAPPING_POINT + " TEXT ," + CONDUCTOR_SIZE + " TEXT ," + POLE_CONFIGURATION + " TEXT ," + PINPOINT + " TEXT ," + POLE_PAINTINGCODE + " TEXT ," + LAT + " TEXT ," + LAN + " TEXT ," + FLAG1 + " TEXT ," + NO_CKT + " TEXT ," + CKT_TYPE + " TEXT ," + IS_ENDPOLE + " TEXT ," + LINE_TYPE + " TEXT ," + NOOF_CIRCUITS + " TEXT ," + SS_NAME + " TEXT ," + POLE_STRUCTURE + " TEXT ," + DIFF_VOLTAGE + " TEXT ," + SS_ID + " TEXT ," + STJOINT_RMU + " TEXT ," + RMU_TYPE + " TEXT ," + REMARKS + " TEXT ," + TAPPING_LOCNAME + " TEXT ," + TAPPING_POINTS + " TEXT ," + WO_ID + " TEXT ," + IS_FEEDER_TYPE + " TEXT ," + HT_SCNO + " TEXT ," +
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
            + ID + " TEXT ,"
            + GPS_ACCURACY + " TEXT ,"
            + DEVICE_ID + " TEXT ,"

            + FLAG3 + " TEXT ," + FLAG4 + " TEXT " + ")";


    String CREATE_INCREMENTALARCGIS_TABLE = "CREATE TABLE " + INCREMENTAL_ARCGIS_TABLE + "("
            + FEEDER_ID + " TEXT ," + POLE_NO + " INTEGER UNIQUE," + FROM_POLE + " INTEGER ," + POLE_TYPE + " TEXT," + POLE_HEIGHT + " TEXT ," + POLE_SUPPORT + " TEXT ," + TAPPING_POINT + " TEXT ," + CONDUCTOR_SIZE + " TEXT ," + POLE_CONFIGURATION + " TEXT ," + PINPOINT + " TEXT ," + POLE_PAINTINGCODE + " TEXT ," + LAT + " TEXT ," + LAN + " TEXT ," + FLAG1 + " TEXT ," + NO_CKT + " TEXT ," + CKT_TYPE + " TEXT ," + IS_ENDPOLE + " TEXT ," + LINE_TYPE + " TEXT ," + NOOF_CIRCUITS + " TEXT ," + SS_NAME + " TEXT ," + POLE_STRUCTURE + " TEXT ," + DIFF_VOLTAGE + " TEXT ," + SS_ID + " TEXT ," + STJOINT_RMU + " TEXT ," + RMU_TYPE + " TEXT ," + REMARKS + " TEXT ," + TAPPING_LOCNAME + " TEXT ," + TAPPING_POINTS + " TEXT ," + WORKORDER_ID + " TEXT ," + WO_STATUS + " TEXT ," + IS_FEEDER_TYPE + " TEXT ," + HT_SCNO + " TEXT ," + FLAG4 + " TEXT " + ")";


    String CREATE_CMPEHTMASTER_TABLE = "CREATE TABLE " + CMPEHTMASTER_TABLE + "("
            + EHT_SS_CODE + " TEXT ," + EHT_SS_NAME + " TEXT ," + FEEDER_NAME_33KV + " TEXT ," + FEEDER_CODE_33KV + " TEXT ," + FLAG1 + " TEXT ," + FLAG2 + " TEXT ," + FLAG3 + " TEXT " + ")";


    String CREATE_WO_STATUS_MASTER_TABLE = "CREATE TABLE " + WO_STATUS_TABLE + "("
            + FEEDER_ID + " TEXT ," + WO_ID + " TEXT ," + WO_STATUS + " TEXT ," + FLAG1 + " TEXT ," + FLAG2 + " TEXT ," + FLAG3 + " TEXT " + ")";


    String CREATE_HT_SERVICE_LIST_TABLE = "CREATE TABLE " + HT_SERVICE_LIST_TABLE + "("
            + HT_SCNO + " TEXT ,"
            + HTSC_NAME + " TEXT ,"
            + FEEDER_ID + " TEXT ,"
            + FLAG + " TEXT " + ")";


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
        db.execSQL(CREATE_CMPEHTMASTER_TABLE);
        db.execSQL(CREATE_INCRFEMENTAL_EHTMASTER_TABLE);
        db.execSQL(CREATE_INCREMENTALARCGIS_TABLE);
        db.execSQL(CREATE_WO_STATUS_MASTER_TABLE);
        db.execSQL(CREATE_INCREMENTAL_SSMASTER_TABLE);
        db.execSQL(CREATE_IMAGE_11KV_TABLE);
        db.execSQL(CREATE_HT_SERVICE_LIST_TABLE);
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
        db.execSQL("DROP TABLE IF EXISTS " + CMPEHTMASTER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + INCRFEMENTAL_EHTMASTER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + INCREMENTAL_SSMASTER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + IMAGE_11KV_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + HT_SERVICE_LIST_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + WO_STATUS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + INCREMENTAL_ARCGIS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ARCGIS_TABLE_LAT_LANG_EDIT);
        db.execSQL("DROP TABLE IF EXISTS " + ARCGIS_TABLE_SOURCE_POLE_EDIT);
        db.execSQL("DROP TABLE IF EXISTS " + ARCGIS_TABLE_POLE_DETAILS_EDIT);


        // Create tables again
        onCreate(db);
    }

    public boolean addHtServiceList(HtServiceListModel data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HT_SCNO, data.getHT_SCNO());
        values.put(HTSC_NAME, data.getHTSC_NAME());
        values.put(FEEDER_ID, data.getFEEDER_ID());
        values.put(FLAG, "N");

        long a = db.insert(HT_SERVICE_LIST_TABLE, null, values);
        db.close();
        if (a == -1) {
            return false;
        } else {
            return true;
        }

    }

    public List<HtServiceListModel> getHtServiceList(String feeder_id) {
        ArrayList<HtServiceListModel> data = new ArrayList<>();
        String query = "SELECT * FROM HT_SERVICE_LIST_TABLE WHERE FEEDER_ID = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, new String[]{feeder_id});
            if (cursor.moveToFirst()) {
                do {
                    HtServiceListModel model = new HtServiceListModel();
                    model.setHT_SCNO(cursor.getString(0));
                    model.setHTSC_NAME(cursor.getString(1));
                    model.setFEEDER_ID(cursor.getString(2));
                    data.add(model);
                } while (cursor.moveToNext());
            }
            cursor.close();  // Close cursor after use
        } catch (Exception e) {
            e.printStackTrace();  // Handle the exception
        } finally {
            db.close();  // Ensure database is closed in all cases
        }
        return data;
    }


    public boolean clearHTServiceListTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            db.delete(HT_SERVICE_LIST_TABLE, null, null);
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

    public void clearIncrementalFeederData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + INCRFEMENTAL_EHTMASTER_TABLE);
    }

    public void clearIncrementalSurveyedData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + INCREMENTAL_ARCGIS_TABLE);
    }


    public void clearssMasterData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + SSMASTER_TABLE);
    }

    public void clearIncrementalssMasterData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + INCREMENTAL_SSMASTER_TABLE);
    }

    public void clearEditLatLanData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + ARCGIS_TABLE_LAT_LANG_EDIT);
    }

    public void clearEditPoleDetails() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + ARCGIS_TABLE_POLE_DETAILS_EDIT);
    }

    public void clearEditSourcePoleData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + ARCGIS_TABLE_SOURCE_POLE_EDIT);
    }

    public void clearWOStatusMasterData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + WO_STATUS_TABLE);
    }


    public void cmpClearFeederData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + CMPEHTMASTER_TABLE);
    }

    public void clearData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + ARCGIS_TABLE);
        db.execSQL("DELETE FROM " + USERMASTER_TABLE);
        db.execSQL("DELETE FROM " + EHTMASTER_TABLE);
        db.execSQL("DELETE FROM " + SSMASTER_TABLE);
        db.execSQL("DELETE FROM " + CMPEHTMASTER_TABLE);
        db.execSQL("DELETE FROM " + INCRFEMENTAL_EHTMASTER_TABLE);
        db.execSQL("DELETE FROM " + INCREMENTAL_ARCGIS_TABLE);
        db.execSQL("DELETE FROM " + WO_STATUS_TABLE);
        db.execSQL("DELETE FROM " + INCREMENTAL_SSMASTER_TABLE);
        db.execSQL("DELETE FROM " + IMAGE_11KV_TABLE);
        db.execSQL("DELETE FROM " + ARCGIS_TABLE_LAT_LANG_EDIT);
        db.execSQL("DELETE FROM " + ARCGIS_TABLE_SOURCE_POLE_EDIT);
        db.execSQL("DELETE FROM " + ARCGIS_TABLE_POLE_DETAILS_EDIT);

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
        String selectQuery = "SELECT DISTINCT  EHT_SS_CODE|| '-' ||EHT_SS_NAME FROM CMPEHTMASTER_TABLE WHERE FEEDER_CODE_33KV IS NOT NULL";


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
        String selectQuery = "SELECT DISTINCT  EHT_SS_CODE|| '-' ||EHT_SS_NAME FROM INCRFEMENTAL_EHTMASTER_TABLE WHERE FEEDER_CODE_33KV IS NOT NULL";


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

    public List<String> getIncrementalFeederComplaintTest(String condition) {
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

    private static final String MAX_LENGTH = "MAX_LENGTH";


    public boolean addSSMasterData(SSMasterVo ssMasterVo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EHT_SS_CODE, ssMasterVo.getFEEDING_SS_CODE());
        values.put(EHT_SS_NAME, ssMasterVo.getFEEDING_SS_NAME());
        values.put(FEEDER_CODE_33KV, ssMasterVo.getFEEDER_CODE_33KV());
        values.put(LAT, ssMasterVo.getLAT());
        values.put(LAN, ssMasterVo.getLAN());
        values.put(MAX_LENGTH, ssMasterVo.getMAX_DISTANCE());

        long a = db.insert(SSMASTER_TABLE, null, values);
        if (a == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }

    public boolean addIncrementalSSMasterData(SSMasterVo ssMasterVo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EHT_SS_CODE, ssMasterVo.getFEEDING_SS_CODE());
        values.put(EHT_SS_NAME, ssMasterVo.getFEEDING_SS_NAME());
        values.put(FEEDER_CODE_33KV, ssMasterVo.getFEEDER_CODE_33KV());

        long a = db.insert(INCREMENTAL_SSMASTER_TABLE, null, values);
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
                ssMasterVo.setLAT(cursor.getString(3));
                ssMasterVo.setLAN(cursor.getString(4));
                ssMasterVo.setMAX_DISTANCE(cursor.getString(5));


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
        values.put(WO_ID, feederDataVo.getWOID());
        values.put(IS_FEEDER_TYPE, feederDataVo.getIS_FEEDER_TYPE());


        long a = db.insert(EHTMASTER_TABLE, null, values);
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

        String query = "SELECT * FROM EHTMASTER_TABLE";
        SQLiteDatabase db = this.getWritableDatabase();
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
        return feederDataVos;
    }


    public boolean addIncrementalFeederData(IncrementalFeederDataVo incrementalFeederDataVo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EHT_SS_CODE, incrementalFeederDataVo.getEHT_SS_CODE());
        values.put(EHT_SS_NAME, incrementalFeederDataVo.getEHT_SS_NAME());
        values.put(FEEDER_NAME_33KV, incrementalFeederDataVo.getFEEDER_NAME_33KV());
        values.put(FEEDER_CODE_33KV, incrementalFeederDataVo.getFEEDER_CODE_33KV());
        values.put(WORKORDER_ID, incrementalFeederDataVo.getWORKORDER_ID());
        values.put(STATUS, incrementalFeederDataVo.getSTATUS());
        values.put(FROM_POLE, incrementalFeederDataVo.getFROM_POLE());

        long a = db.insert(INCRFEMENTAL_EHTMASTER_TABLE, null, values);
        if (a == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }

    public List<IncrementalFeederDataVo> getIncrementalFeerData() {
        ArrayList<IncrementalFeederDataVo> incrementalFeederDataList = new ArrayList<IncrementalFeederDataVo>();

        String query = "SELECT * FROM INCRFEMENTAL_EHTMASTER_TABLE";
        SQLiteDatabase db = this.getWritableDatabase();
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
                //  vo.setSECTION_CODE(cursor.getString(8));
                incrementalFeederDataList.add(incrementalFeederDataVo);
            } while (cursor.moveToNext());
        }

        db.close();
        return incrementalFeederDataList;
    }

    public boolean addCmpFeederData(FeederDataVo feederDataVo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EHT_SS_CODE, feederDataVo.getEHT_SS_CODE());
        values.put(EHT_SS_NAME, feederDataVo.getEHT_SS_NAME());
        values.put(FEEDER_NAME_33KV, feederDataVo.getFEEDER_NAME_33KV());
        values.put(FEEDER_CODE_33KV, feederDataVo.getFEEDER_CODE_33KV());

        long a = db.insert(CMPEHTMASTER_TABLE, null, values);
        if (a == -1) {
            db.close();
            return false;
        } else {
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
    public boolean addSurveyData(SurveyVo surveyVo) {
        SQLiteDatabase db = this.getWritableDatabase();
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
        values.put(WO_ID, surveyVo.getWoId());
        values.put(IS_FEEDER_TYPE, surveyVo.getIsFeederType());
        values.put(HT_SCNO, surveyVo.getHtServiceNumber());
        values.put(SURVEY_TYPE, surveyVo.getSurvaytype());

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
        values.put(CROSS_LT_CKT, surveyVo.getCROSS_LT_CKT());
        values.put(CROSS_6_3KV_ID, surveyVo.getCROSS_6_3KV_ID());
        values.put(CROSS_11KV_ID, surveyVo.getCROSS_11KV_ID());
        values.put(CROSS_33KV_ID, surveyVo.getCROSS_33KV_ID());
        values.put(CROSS_EHT, surveyVo.getCROSS_EHT());
        values.put(CROSS_COMMUN, surveyVo.getCROSS_COMMUN());
        values.put(CROSS_REMARKS, surveyVo.getCROSS_REMARKS());
        values.put(CROSSING_TYPE, surveyVo.getCROSSING_TYPE());

        //OTHER CABLES
        values.put(NUM_OTHER_CABLES, surveyVo.getNUM_OTHER_CABLES());
        values.put(POLE_CONCRETE, surveyVo.getPOLE_CONCRETE());

        // System.out.println("CURRENT_LAT IN DB INSERT : "+surveyVo.getCURRENT_LAT());
        values.put(CURRENT_LAT, surveyVo.getCURRENT_LAT());
        values.put(CURRENT_LONG, surveyVo.getCURRENT_LONG());

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
        values.put(ID, surveyVo.getID());
        values.put(GPS_ACCURACY, surveyVo.getGPS_ACCURACY());
        values.put(DEVICE_ID, surveyVo.getDEVICE_ID());


        long a = db.insert(ARCGIS_TABLE, null, values);
        if (a == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }

    public List<SurveyVo> checkMultiCktForLT() {
        String query = "SELECT * FROM ARCGIS_TABLE WHERE FLAG1='N'";
        ArrayList<SurveyVo> surveyVoArrayList = new ArrayList<SurveyVo>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                SurveyVo vo = new SurveyVo();
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

            } while (cursor.moveToNext());
        }
        db.close();
        return surveyVoArrayList;
    }


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
                vo.setID(cursor.getString(88));
                vo.setGPS_ACCURACY(cursor.getString(89));


                //  vo.setSECTION_CODE(cursor.getString(8));
                surveyVoArrayList.add(vo);
            } while (cursor.moveToNext());
        }

        db.close();
        return surveyVoArrayList;
    }


    public List<SurveyVo> getData() {
        ArrayList<SurveyVo> surveyVoArrayList = new ArrayList<SurveyVo>();

        String query = "SELECT * FROM ARCGIS_TABLE ORDER BY POLE_NO";
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
                //  vo.setSECTION_CODE(cursor.getString(8));
                surveyVoArrayList.add(vo);
            } while (cursor.moveToNext());
        }

        db.close();
        return surveyVoArrayList;
    }

    public List<SurveyVo> getPoleLatLonData() {
        ArrayList<SurveyVo> surveyVoArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();  // readable is enough for SELECT
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM ARCGIS_TABLE ORDER BY POLE_NO";
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    SurveyVo vo = new SurveyVo();

                    // Use column indices as per query order
                    vo.setPole_no(cursor.getInt(1));
                    vo.setFrom_pole(cursor.getInt(2));
                    vo.setLat(cursor.getString(11));
                    vo.setLan(cursor.getString(12));

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


    public ArrayList<SurveyVo> getSurveyDataByDistance(double currentLat, double currentLon) {
        ArrayList<SurveyVo> surveyVoArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Build query based on condition
            String query = query = "SELECT * FROM ARCGIS_TABLE WHERE LAT IS NOT NULL AND LAN IS NOT NULL";
            ;


            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    String latStr = cursor.getString(cursor.getColumnIndexOrThrow("LAT"));
                    String lonStr = cursor.getString(cursor.getColumnIndexOrThrow("LAN"));

                    if (latStr != null && lonStr != null && !latStr.isEmpty() && !lonStr.isEmpty()) {

                        double distance = getDistanceBetweenPoles(
                                latStr, lonStr,
                                String.valueOf(currentLat), String.valueOf(currentLon)
                        );

                        // Log for debugging
                        System.out.println("DISTANCE_FROM_CURRENT: " + distance);

                        // Add only if within 500 meters
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
                            vo.setWoId(cursor.getString(28));
                            vo.setIsFeederType(cursor.getString(29));
                            vo.setHtServiceNumber(cursor.getString(30));
                            vo.setSurvaytype(cursor.getString(31));

                            // MULTI CKT
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

                            // DEFECTS
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

                            // CROSSINGS
                            vo.setCROSS_LT_CKT(cursor.getString(67));
                            vo.setCROSS_6_3KV_ID(cursor.getString(68));
                            vo.setCROSS_11KV_ID(cursor.getString(69));
                            vo.setCROSS_33KV_ID(cursor.getString(70));
                            vo.setCROSS_EHT(cursor.getString(71));
                            vo.setCROSS_COMMUN(cursor.getString(72));
                            vo.setCROSS_REMARKS(cursor.getString(73));
                            vo.setCROSSING_TYPE(cursor.getString(74));

                            // OTHER CABLES
                            vo.setNUM_OTHER_CABLES(cursor.getString(75));
                            vo.setPOLE_CONCRETE(cursor.getString(76));
                            vo.setCURRENT_LAT(cursor.getString(77));
                            vo.setCURRENT_LONG(cursor.getString(78));

                            // MULTI CKT NOT IN LIST
                            vo.setMC_33KV_NOT_IN_LIST_1(cursor.getString(79));
                            vo.setMC_33KV_NOT_IN_LIST_2(cursor.getString(80));
                            vo.setMC_33KV_NOT_IN_LIST_3(cursor.getString(81));
                            vo.setMC_11KV_NOT_IN_LIST_1(cursor.getString(82));
                            vo.setMC_11KV_NOT_IN_LIST_2(cursor.getString(83));
                            vo.setMC_11KV_NOT_IN_LIST_3(cursor.getString(84));
                            vo.setMC_LT_NOT_IN_LIST_1(cursor.getString(85));
                            vo.setMC_LT_NOT_IN_LIST_2(cursor.getString(86));
                            vo.setMC_LT_NOT_IN_LIST_3(cursor.getString(87));
                            vo.setID(cursor.getString(88));

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
            String query = "SELECT * FROM ARCGIS_TABLE WHERE LAT IS NOT NULL AND LAN IS NOT NULL AND FEEDER_ID = ?";
            cursor = db.rawQuery(query, new String[]{feeder_id});

            if (cursor.moveToFirst()) {
                do {
                    String latStr = cursor.getString(cursor.getColumnIndexOrThrow("LAT"));
                    String lonStr = cursor.getString(cursor.getColumnIndexOrThrow("LAN"));

                    if (latStr != null && lonStr != null && !latStr.isEmpty() && !lonStr.isEmpty()) {

                        double distance = getDistanceBetweenPoles(
                                latStr, lonStr,
                                String.valueOf(currentLat), String.valueOf(currentLon)
                        );

                        // Log for debugging
                        System.out.println("DISTANCE_FROM_CURRENT: " + distance);

                        // Add only if within 500 meters
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
                            vo.setWoId(cursor.getString(28));
                            vo.setIsFeederType(cursor.getString(29));
                            vo.setHtServiceNumber(cursor.getString(30));
                            vo.setSurvaytype(cursor.getString(31));

                            // MULTI CKT
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

                            // DEFECTS
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

                            // CROSSINGS
                            vo.setCROSS_LT_CKT(cursor.getString(67));
                            vo.setCROSS_6_3KV_ID(cursor.getString(68));
                            vo.setCROSS_11KV_ID(cursor.getString(69));
                            vo.setCROSS_33KV_ID(cursor.getString(70));
                            vo.setCROSS_EHT(cursor.getString(71));
                            vo.setCROSS_COMMUN(cursor.getString(72));
                            vo.setCROSS_REMARKS(cursor.getString(73));
                            vo.setCROSSING_TYPE(cursor.getString(74));

                            // OTHER CABLES
                            vo.setNUM_OTHER_CABLES(cursor.getString(75));
                            vo.setPOLE_CONCRETE(cursor.getString(76));
                            vo.setCURRENT_LAT(cursor.getString(77));
                            vo.setCURRENT_LONG(cursor.getString(78));

                            // MULTI CKT NOT IN LIST
                            vo.setMC_33KV_NOT_IN_LIST_1(cursor.getString(79));
                            vo.setMC_33KV_NOT_IN_LIST_2(cursor.getString(80));
                            vo.setMC_33KV_NOT_IN_LIST_3(cursor.getString(81));
                            vo.setMC_11KV_NOT_IN_LIST_1(cursor.getString(82));
                            vo.setMC_11KV_NOT_IN_LIST_2(cursor.getString(83));
                            vo.setMC_11KV_NOT_IN_LIST_3(cursor.getString(84));
                            vo.setMC_LT_NOT_IN_LIST_1(cursor.getString(85));
                            vo.setMC_LT_NOT_IN_LIST_2(cursor.getString(86));
                            vo.setMC_LT_NOT_IN_LIST_3(cursor.getString(87));
                            vo.setID(cursor.getString(88));

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
                    vo.setWoId(cursor.getString(28));
                    vo.setIsFeederType(cursor.getString(29));
                    vo.setHtServiceNumber(cursor.getString(30));
                    vo.setSurvaytype(cursor.getString(31));

                    // MULTI CKT
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

                    // DEFECTS
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

                    // CROSSINGS
                    vo.setCROSS_LT_CKT(cursor.getString(67));
                    vo.setCROSS_6_3KV_ID(cursor.getString(68));
                    vo.setCROSS_11KV_ID(cursor.getString(69));
                    vo.setCROSS_33KV_ID(cursor.getString(70));
                    vo.setCROSS_EHT(cursor.getString(71));
                    vo.setCROSS_COMMUN(cursor.getString(72));
                    vo.setCROSS_REMARKS(cursor.getString(73));
                    vo.setCROSSING_TYPE(cursor.getString(74));

                    // OTHER CABLES
                    vo.setNUM_OTHER_CABLES(cursor.getString(75));
                    vo.setPOLE_CONCRETE(cursor.getString(76));

                    vo.setCURRENT_LAT(cursor.getString(77));
                    vo.setCURRENT_LONG(cursor.getString(78));

                    // MULTI CKT NOT IN LIST
                    vo.setMC_33KV_NOT_IN_LIST_1(cursor.getString(79));
                    vo.setMC_33KV_NOT_IN_LIST_2(cursor.getString(80));
                    vo.setMC_33KV_NOT_IN_LIST_3(cursor.getString(81));
                    vo.setMC_11KV_NOT_IN_LIST_1(cursor.getString(82));
                    vo.setMC_11KV_NOT_IN_LIST_2(cursor.getString(83));
                    vo.setMC_11KV_NOT_IN_LIST_3(cursor.getString(84));
                    vo.setMC_LT_NOT_IN_LIST_1(cursor.getString(85));
                    vo.setMC_LT_NOT_IN_LIST_2(cursor.getString(86));
                    vo.setMC_LT_NOT_IN_LIST_3(cursor.getString(87));
                    vo.setID(cursor.getString(88));

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


    public boolean addIncrementalSurveyData(IncrementalSurveyVo incrementalSurveyVo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FEEDER_ID, incrementalSurveyVo.getFeeder_id());
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
        if (a == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }


    public List<IncrementalSurveyVo> getIncrementalSurveyData(String strCondition) {
        ArrayList<IncrementalSurveyVo> incrementalSurveyVosList = new ArrayList<IncrementalSurveyVo>();

        String query = "";
        ArrayList<SurveyVo> surveyVoArrayList = new ArrayList<SurveyVo>();

        if (strCondition.equals("")) {
            query = "SELECT * FROM INCREMENTAL_ARCGIS_TABLE ORDER BY POLE_NO";
        } else {

            query = strCondition;

        }

        /// String  query = "SELECT * FROM INCREMENTAL_ARCGIS_TABLE ORDER BY POLE_NO";
        SQLiteDatabase db = this.getWritableDatabase();
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
            //   Log.v("SQLLLLLL==>",""+e.getMessage());
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
                query = "SELECT * FROM  IMAGE_33KV_TABLE";
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

    public void clearImageData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + IMAGE_11KV_TABLE);
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

    public ArrayList<SurveyVo> getAllEditSurveyLatLan() {
        ArrayList<SurveyVo> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM " + ARCGIS_TABLE_LAT_LANG_EDIT;
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    SurveyVo vo = new SurveyVo();

                    vo.setPole_no(cursor.getInt(cursor.getColumnIndexOrThrow(POLE_NO)));
                    vo.setFeeder_id(cursor.getString(cursor.getColumnIndexOrThrow(FEEDER_ID)));
                    vo.setLat(cursor.getString(cursor.getColumnIndexOrThrow(LAT)));
                    vo.setLan(cursor.getString(cursor.getColumnIndexOrThrow(LAN)));
                    vo.setID(cursor.getString(cursor.getColumnIndexOrThrow(ID)));

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


    public List<SurveyVo> getEditPoleDetailsSurveyData() {

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


}
