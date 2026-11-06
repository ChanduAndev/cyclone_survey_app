package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.DistandeVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.FeederDataVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.IncrementalFeederDataVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.IncrementalSurveyVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.LoginDataResponseUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.SSMasterVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.SurveyVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.WoStatusMasterVoUG33KV;
import com.apepdcl.gis_survey.vo.LoginDataResponse;


import java.util.ArrayList;
import java.util.List;

public class ArcGISDatabaseUG33KV extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 7;
    // Database Name
    private static final String DATABASE_NAME = "GIS_RDSS";
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
    private static final String GPS_ACCURACY="GPS_ACCURACY";
    //NEW
    private static final String POLE_SURVEY_TYPE="POLE_SURVEY_TYPE";
    private static final String RMU_SURVEY_TYPE="RMU_SURVEY_TYPE";





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
            + FEEDER_ID + " TEXT ," + POLE_NO + " INTEGER UNIQUE," + FROM_POLE + " INTEGER ," + POLE_TYPE + " TEXT," + POLE_HEIGHT + " TEXT ," + POLE_SUPPORT + " TEXT ,"+ TAPPING_POINT + " TEXT ,"+ CONDUCTOR_SIZE + " TEXT ," + POLE_CONFIGURATION + " TEXT ," + PINPOINT + " TEXT ,"+ POLE_PAINTINGCODE + " TEXT ," + LAT + " TEXT ," + LAN + " TEXT ," + FLAG1 + " TEXT ," + NO_CKT + " TEXT ," + CKT_TYPE + " TEXT ,"+ IS_ENDPOLE + " TEXT ," + LINE_TYPE + " TEXT ," + NOOF_CIRCUITS + " TEXT ," + SS_NAME + " TEXT ," + POLE_STRUCTURE + " TEXT ," + DIFF_VOLTAGE + " TEXT ," + SS_ID + " TEXT ," + STJOINT_RMU + " TEXT ," + RMU_TYPE + " TEXT ," + REMARKS + " TEXT ,"+ TAPPING_LOCNAME + " TEXT ," + TAPPING_POINTS + " TEXT ," + WO_ID + " TEXT ," + IS_FEEDER_TYPE + " TEXT ,"  + HT_SCNO + " TEXT ,"  +SURVEY_TYPE + " TEXT ,"  +POLE_SURVEY_TYPE + " TEXT ," +RMU_SURVEY_TYPE + " TEXT ," + FLAG3 + " TEXT ,"  + FLAG4 + " TEXT " + ")";




    String CREATE_INCREMENTALARCGIS_TABLE = "CREATE TABLE " + INCREMENTAL_ARCGIS_TABLE + "("
            + FEEDER_ID + " TEXT ," + POLE_NO + " INTEGER UNIQUE," + FROM_POLE + " INTEGER ," + POLE_TYPE + " TEXT," + POLE_HEIGHT + " TEXT ," + POLE_SUPPORT + " TEXT ,"+ TAPPING_POINT + " TEXT ,"+ CONDUCTOR_SIZE + " TEXT ," + POLE_CONFIGURATION + " TEXT ," + PINPOINT + " TEXT ,"+ POLE_PAINTINGCODE + " TEXT ," + LAT + " TEXT ," + LAN + " TEXT ," + FLAG1 + " TEXT ," + NO_CKT + " TEXT ," + CKT_TYPE + " TEXT ,"+ IS_ENDPOLE + " TEXT ," + LINE_TYPE + " TEXT ," + NOOF_CIRCUITS + " TEXT ," + SS_NAME + " TEXT ," + POLE_STRUCTURE + " TEXT ," + DIFF_VOLTAGE + " TEXT ," + SS_ID + " TEXT ," + STJOINT_RMU + " TEXT ," + RMU_TYPE + " TEXT ," + REMARKS + " TEXT ,"+ TAPPING_LOCNAME + " TEXT ," + TAPPING_POINTS + " TEXT ," + WORKORDER_ID + " TEXT ," + WO_STATUS + " TEXT ," + IS_FEEDER_TYPE + " TEXT ,"  +HT_SCNO + " TEXT ,"  + FLAG4 + " TEXT " + ")";




    String CREATE_CMPEHTMASTER_TABLE = "CREATE TABLE " + CMPEHTMASTER_TABLE + "("
            + EHT_SS_CODE + " TEXT ," + EHT_SS_NAME + " TEXT ," + FEEDER_NAME_33KV + " TEXT ," + FEEDER_CODE_33KV + " TEXT ," + FLAG1 + " TEXT ," + FLAG2 + " TEXT ," + FLAG3 + " TEXT " + ")";




    String CREATE_WO_STATUS_MASTER_TABLE = "CREATE TABLE " + WO_STATUS_TABLE + "("
            + FEEDER_ID + " TEXT ," + WO_ID + " TEXT ," + WO_STATUS + " TEXT ," + FLAG1 + " TEXT ," + FLAG2 + " TEXT ," + FLAG3 + " TEXT " + ")";


    public ArcGISDatabaseUG33KV(Context context) {
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


    public boolean addSSMasterData(SSMasterVoUG33KV ssMasterVoUG33KV) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EHT_SS_CODE, ssMasterVoUG33KV.getFEEDING_SS_CODE());
        values.put(EHT_SS_NAME, ssMasterVoUG33KV.getFEEDING_SS_NAME());
        values.put(FEEDER_CODE_33KV, ssMasterVoUG33KV.getFEEDER_CODE_33KV());

        long a = db.insert(SSMASTER_TABLE, null, values);
        if(a==-1){
            db.close();
            return false;
        }else {
            db.close();
            return true;
        }

    }

    public boolean addIncrementalSSMasterData(SSMasterVoUG33KV ssMasterVoUG33KV) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EHT_SS_CODE, ssMasterVoUG33KV.getFEEDING_SS_CODE());
        values.put(EHT_SS_NAME, ssMasterVoUG33KV.getFEEDING_SS_NAME());
        values.put(FEEDER_CODE_33KV, ssMasterVoUG33KV.getFEEDER_CODE_33KV());

        long a = db.insert(INCREMENTAL_SSMASTER_TABLE, null, values);
        if(a==-1){
            db.close();
            return false;
        }else {
            db.close();
            return true;
        }

    }



    public List<SSMasterVoUG33KV> getSSMasterData(){
        ArrayList<SSMasterVoUG33KV> ssResponseList= new ArrayList<SSMasterVoUG33KV>();

        String  query = "SELECT * FROM SSMASTER_TABLE";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {

                SSMasterVoUG33KV ssMasterVoUG33KV = new SSMasterVoUG33KV();
                //vo.setLat(cursor.getString(0));
                ssMasterVoUG33KV.setFEEDING_SS_CODE(cursor.getString(0));
                ssMasterVoUG33KV.setFEEDING_SS_NAME(cursor.getString(1));
                ssMasterVoUG33KV.setFEEDER_CODE_33KV(cursor.getString(2));

                //  vo.setSECTION_CODE(cursor.getString(8));
                ssResponseList.add(ssMasterVoUG33KV);
            } while (cursor.moveToNext());
        }

        db.close();
        return ssResponseList;
    }

    public boolean addUserMasterData(LoginDataResponse loginDataResponseUG33KV) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ROLE, loginDataResponseUG33KV.getROLE());
        values.put(OFFICE_ID, loginDataResponseUG33KV.getOFFICE_ID());
        values.put(SECTION_NAME, loginDataResponseUG33KV.getSECTION_NAME());
        values.put(SEC_CODE, loginDataResponseUG33KV.getSEC_CODE());

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
        ArrayList<LoginDataResponse> loginDataResponsUG33KVS = new ArrayList<LoginDataResponse>();

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
                loginDataResponsUG33KVS.add(loginVo);
            } while (cursor.moveToNext());
        }

        db.close();
        return loginDataResponsUG33KVS;
    }

//+ EHT_SS_CODE + " TEXT ," + EHT_SS_NAME + " TEXT ," + FEEDER_NAME_33KV + " TEXT ," + FEEDER_CODE_33KV + " TEXT ," + FLAG1 +

    public boolean addFeederData(FeederDataVoUG33KV feederDataVoUG33KV) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EHT_SS_CODE, feederDataVoUG33KV.getEHT_SS_CODE());
        values.put(EHT_SS_NAME, feederDataVoUG33KV.getEHT_SS_NAME());
        values.put(FEEDER_NAME_33KV, feederDataVoUG33KV.getFEEDER_NAME_33KV());
        values.put(FEEDER_CODE_33KV, feederDataVoUG33KV.getFEEDER_CODE_33KV());
        values.put(WO_ID, feederDataVoUG33KV.getWOID());
        values.put(IS_FEEDER_TYPE, feederDataVoUG33KV.getIS_FEEDER_TYPE());


        long a = db.insert(EHTMASTER_TABLE, null, values);
        if(a==-1){
            db.close();
            return false;
        }else {
            db.close();
            return true;
        }

    }

    public List<FeederDataVoUG33KV> getFeerData(){
        ArrayList<FeederDataVoUG33KV> feederDataVoUG33KVS = new ArrayList<FeederDataVoUG33KV>();

        String  query = "SELECT * FROM EHTMASTER_TABLE";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {

                FeederDataVoUG33KV feederDataVoUG33KV = new FeederDataVoUG33KV();
                //vo.setLat(cursor.getString(0));
                feederDataVoUG33KV.setEHT_SS_CODE(cursor.getString(0));
                feederDataVoUG33KV.setEHT_SS_NAME(cursor.getString(1));
                feederDataVoUG33KV.setFEEDER_NAME_33KV(cursor.getString(2));
                feederDataVoUG33KV.setFEEDER_CODE_33KV(cursor.getString(3));
                //  vo.setSECTION_CODE(cursor.getString(8));
                feederDataVoUG33KVS.add(feederDataVoUG33KV);
            } while (cursor.moveToNext());
        }

        db.close();
        return feederDataVoUG33KVS;
    }


    public boolean addIncrementalFeederData(IncrementalFeederDataVoUG33KV incrementalFeederDataVoUG33KV) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EHT_SS_CODE, incrementalFeederDataVoUG33KV.getEHT_SS_CODE());
        values.put(EHT_SS_NAME, incrementalFeederDataVoUG33KV.getEHT_SS_NAME());
        values.put(FEEDER_NAME_33KV, incrementalFeederDataVoUG33KV.getFEEDER_NAME_33KV());
        values.put(FEEDER_CODE_33KV, incrementalFeederDataVoUG33KV.getFEEDER_CODE_33KV());
        values.put(WORKORDER_ID, incrementalFeederDataVoUG33KV.getWORKORDER_ID());
        values.put(STATUS, incrementalFeederDataVoUG33KV.getSTATUS());
        values.put(FROM_POLE, incrementalFeederDataVoUG33KV.getFROM_POLE());

        long a = db.insert(INCRFEMENTAL_EHTMASTER_TABLE, null, values);
        if(a==-1){
            db.close();
            return false;
        }else {
            db.close();
            return true;
        }

    }
    public List<IncrementalFeederDataVoUG33KV> getIncrementalFeerData(){
        ArrayList<IncrementalFeederDataVoUG33KV>incrementalFeederDataList= new ArrayList<IncrementalFeederDataVoUG33KV>();

        String  query = "SELECT * FROM INCRFEMENTAL_EHTMASTER_TABLE";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {

                IncrementalFeederDataVoUG33KV incrementalFeederDataVoUG33KV = new IncrementalFeederDataVoUG33KV();
                //vo.setLat(cursor.getString(0));
                incrementalFeederDataVoUG33KV.setEHT_SS_CODE(cursor.getString(0));
                incrementalFeederDataVoUG33KV.setEHT_SS_NAME(cursor.getString(1));
                incrementalFeederDataVoUG33KV.setFEEDER_NAME_33KV(cursor.getString(2));
                incrementalFeederDataVoUG33KV.setFEEDER_CODE_33KV(cursor.getString(3));
                incrementalFeederDataVoUG33KV.setWORKORDER_ID(cursor.getString(4));
                incrementalFeederDataVoUG33KV.setSTATUS(cursor.getString(5));
                incrementalFeederDataVoUG33KV.setFROM_POLE(cursor.getString(6));
                //  vo.setSECTION_CODE(cursor.getString(8));
                incrementalFeederDataList.add(incrementalFeederDataVoUG33KV);
            } while (cursor.moveToNext());
        }

        db.close();
        return incrementalFeederDataList;
    }

    public boolean addCmpFeederData(FeederDataVoUG33KV feederDataVoUG33KV) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EHT_SS_CODE, feederDataVoUG33KV.getEHT_SS_CODE());
        values.put(EHT_SS_NAME, feederDataVoUG33KV.getEHT_SS_NAME());
        values.put(FEEDER_NAME_33KV, feederDataVoUG33KV.getFEEDER_NAME_33KV());
        values.put(FEEDER_CODE_33KV, feederDataVoUG33KV.getFEEDER_CODE_33KV());

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
    public boolean addSurveyData(SurveyVoUG33KV surveyVoUG33KV) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FEEDER_ID, surveyVoUG33KV.getFeeder_id());
        values.put(POLE_NO, surveyVoUG33KV.getPole_no());
        values.put(FROM_POLE, surveyVoUG33KV.getFrom_pole());
        values.put(POLE_TYPE, surveyVoUG33KV.getPole_type());
        values.put(POLE_HEIGHT, surveyVoUG33KV.getPole_height());
        // values.put(NOOF_CIRCUITS, surveyVo.getNoof_circuits());
        values.put(POLE_SUPPORT, surveyVoUG33KV.getPole_support());
        values.put(TAPPING_POINT, surveyVoUG33KV.getTapping_point());
        values.put(CONDUCTOR_SIZE, surveyVoUG33KV.getConductor_size());
        values.put(POLE_CONFIGURATION, surveyVoUG33KV.getPole_configuration());
        values.put(PINPOINT, surveyVoUG33KV.getPinpoint());
        values.put(POLE_PAINTINGCODE, surveyVoUG33KV.getPole_paintingcode());
        values.put(LAT, surveyVoUG33KV.getLat());
        values.put(LAN, surveyVoUG33KV.getLan());
        values.put(FLAG1, surveyVoUG33KV.getPastSurveyFlag());
        values.put(NO_CKT, surveyVoUG33KV.getIsMultiCircuit());
        values.put(CKT_TYPE, surveyVoUG33KV.getCircuitType());
        values.put(IS_ENDPOLE, surveyVoUG33KV.getIsEndPole());
        values.put(LINE_TYPE, surveyVoUG33KV.getLineType());
        values.put(NOOF_CIRCUITS, surveyVoUG33KV.getNoofCircuits());
        values.put(SS_NAME, surveyVoUG33KV.getSs_name());
        values.put(POLE_STRUCTURE, surveyVoUG33KV.getPoleStructure());
        values.put(DIFF_VOLTAGE, surveyVoUG33KV.getDiffVoltage());
        values.put(SS_ID, surveyVoUG33KV.getSsId());
        values.put(STJOINT_RMU, surveyVoUG33KV.getStjoint_rmu());
        values.put(RMU_TYPE, surveyVoUG33KV.getRmu_type());
        values.put(REMARKS, surveyVoUG33KV.getRemarks());
        values.put(TAPPING_LOCNAME, surveyVoUG33KV.getTappingLocation());
        values.put(TAPPING_POINTS, surveyVoUG33KV.getNoofTappings());
        values.put(WO_ID, surveyVoUG33KV.getWoId());
        values.put(IS_FEEDER_TYPE, surveyVoUG33KV.getIsFeederType());
        values.put(HT_SCNO, surveyVoUG33KV.getHtServiceNumber());
        values.put(SURVEY_TYPE, surveyVoUG33KV.getSurvaytype());
        values.put(GPS_ACCURACY, surveyVoUG33KV.getGPS_ACCURACY());

        //NEW
        System.out.println("POLE SURVEY TYPE IN DB: "+ surveyVoUG33KV.getPoleSurveyType());
        System.out.println("RMU SURVEY TYPE IN DB: "+ surveyVoUG33KV.getRmuSurveyType());

        values.put(POLE_SURVEY_TYPE, surveyVoUG33KV.getPoleSurveyType());
        values.put(RMU_SURVEY_TYPE, surveyVoUG33KV.getRmuSurveyType());


        long a = db.insert(ARCGIS_TABLE, null, values);
        if(a==-1){
            db.close();
            return false;
        }else {
            db.close();
            return true;
        }

    }

    public ArrayList<SurveyVoUG33KV> getSurveyData(String strCondition) {
        String query = "";
        ArrayList<SurveyVoUG33KV> surveyVoUG33KVArrayList = new ArrayList<SurveyVoUG33KV>();

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

                SurveyVoUG33KV vo = new SurveyVoUG33KV();
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

                //NEW
                vo.setPoleSurveyType(cursor.getString(32));
                vo.setRmuSurveyType(cursor.getString(33));


                //  vo.setSECTION_CODE(cursor.getString(8));
                surveyVoUG33KVArrayList.add(vo);
            } while (cursor.moveToNext());
        }

        db.close();
        return surveyVoUG33KVArrayList;
    }
    public List<SurveyVoUG33KV> getData(){
        ArrayList<SurveyVoUG33KV> surveyVoUG33KVArrayList = new ArrayList<SurveyVoUG33KV>();

        String  query = "SELECT * FROM ARCGIS_TABLE ORDER BY POLE_NO";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {

                SurveyVoUG33KV vo = new SurveyVoUG33KV();
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
                surveyVoUG33KVArrayList.add(vo);
            } while (cursor.moveToNext());
        }

        db.close();
        return surveyVoUG33KVArrayList;
    }



    public boolean addIncrementalSurveyData(IncrementalSurveyVoUG33KV incrementalSurveyVoUG33KV) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FEEDER_ID, incrementalSurveyVoUG33KV.getFeeder_id());
        values.put(POLE_NO, incrementalSurveyVoUG33KV.getPole_no());
        values.put(FROM_POLE, incrementalSurveyVoUG33KV.getFrom_pole());
        values.put(POLE_TYPE, incrementalSurveyVoUG33KV.getPole_type());
        values.put(POLE_HEIGHT, incrementalSurveyVoUG33KV.getPole_height());
        // values.put(NOOF_CIRCUITS, surveyVo.getNoof_circuits());
        values.put(POLE_SUPPORT, incrementalSurveyVoUG33KV.getPole_support());
        values.put(TAPPING_POINT, incrementalSurveyVoUG33KV.getTapping_point());
        values.put(CONDUCTOR_SIZE, incrementalSurveyVoUG33KV.getConductor_size());
        values.put(POLE_CONFIGURATION, incrementalSurveyVoUG33KV.getPole_configuration());
        values.put(PINPOINT, incrementalSurveyVoUG33KV.getPinpoint());
        values.put(POLE_PAINTINGCODE, incrementalSurveyVoUG33KV.getPole_paintingcode());
        values.put(LAT, incrementalSurveyVoUG33KV.getLat());
        values.put(LAN, incrementalSurveyVoUG33KV.getLan());
        values.put(FLAG1, incrementalSurveyVoUG33KV.getPastSurveyFlag());
        values.put(NO_CKT, incrementalSurveyVoUG33KV.getIsMultiCircuit());
        values.put(CKT_TYPE, incrementalSurveyVoUG33KV.getCircuitType());
        values.put(IS_ENDPOLE, incrementalSurveyVoUG33KV.getIsEndPole());
        values.put(LINE_TYPE, incrementalSurveyVoUG33KV.getLineType());
        values.put(NOOF_CIRCUITS, incrementalSurveyVoUG33KV.getNoofCircuits());
        values.put(SS_NAME, incrementalSurveyVoUG33KV.getSs_name());
        values.put(POLE_STRUCTURE, incrementalSurveyVoUG33KV.getPoleStructure());
        values.put(DIFF_VOLTAGE, incrementalSurveyVoUG33KV.getDiffVoltage());
        values.put(SS_ID, incrementalSurveyVoUG33KV.getSsId());
        values.put(STJOINT_RMU, incrementalSurveyVoUG33KV.getStjoint_rmu());
        values.put(RMU_TYPE, incrementalSurveyVoUG33KV.getRmu_type());
        values.put(REMARKS, incrementalSurveyVoUG33KV.getRemarks());
        values.put(TAPPING_LOCNAME, incrementalSurveyVoUG33KV.getTappingLocation());
        values.put(TAPPING_POINTS, incrementalSurveyVoUG33KV.getNoofTappings());
        values.put(WORKORDER_ID, incrementalSurveyVoUG33KV.getWorkOrder());
        values.put(WO_STATUS, incrementalSurveyVoUG33KV.getWorkOrderStatus());
        values.put(IS_FEEDER_TYPE, incrementalSurveyVoUG33KV.getIsFeederType());
        values.put(HT_SCNO, incrementalSurveyVoUG33KV.getHtServiceNumber());

        long a = db.insert(INCREMENTAL_ARCGIS_TABLE, null, values);
        if(a==-1){
            db.close();
            return false;
        }else {
            db.close();
            return true;
        }

    }


    public List<IncrementalSurveyVoUG33KV> getIncrementalSurveyData(String strCondition){
        ArrayList<IncrementalSurveyVoUG33KV> incrementalSurveyVosListUG33KV = new ArrayList<IncrementalSurveyVoUG33KV>();

        String query = "";
        ArrayList<SurveyVoUG33KV> surveyVoUG33KVArrayList = new ArrayList<SurveyVoUG33KV>();

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

                IncrementalSurveyVoUG33KV incrementalSurveyVoUG33KV = new IncrementalSurveyVoUG33KV();
                //vo.setLat(cursor.getString(0));
                incrementalSurveyVoUG33KV.setFeeder_id(cursor.getString(0));
                incrementalSurveyVoUG33KV.setPole_no(cursor.getInt(1));
                incrementalSurveyVoUG33KV.setFrom_pole(cursor.getInt(2));
                incrementalSurveyVoUG33KV.setPole_type(cursor.getString(3));
                incrementalSurveyVoUG33KV.setPole_height(cursor.getString(4));
                // vo.setNoof_circuits(cursor.getString(5));
                incrementalSurveyVoUG33KV.setPole_support(cursor.getString(5));
                incrementalSurveyVoUG33KV.setTapping_point(cursor.getString(6));
                incrementalSurveyVoUG33KV.setConductor_size(cursor.getString(7));
                incrementalSurveyVoUG33KV.setPole_configuration(cursor.getString(8));
                incrementalSurveyVoUG33KV.setPinpoint(cursor.getString(9));
                incrementalSurveyVoUG33KV.setPole_paintingcode(cursor.getString(10));
                incrementalSurveyVoUG33KV.setLat(cursor.getString(11));
                incrementalSurveyVoUG33KV.setLan(cursor.getString(12));
                incrementalSurveyVoUG33KV.setPastSurveyFlag(cursor.getString(13));
                incrementalSurveyVoUG33KV.setIsMultiCircuit(cursor.getString(14));
                incrementalSurveyVoUG33KV.setCircuitType(cursor.getString(15));
                incrementalSurveyVoUG33KV.setIsEndPole(cursor.getString(16));
                incrementalSurveyVoUG33KV.setLineType(cursor.getString(17));
                incrementalSurveyVoUG33KV.setNoofCircuits(cursor.getString(18));
                incrementalSurveyVoUG33KV.setSs_name(cursor.getString(19));
                incrementalSurveyVoUG33KV.setPoleStructure(cursor.getString(20));
                incrementalSurveyVoUG33KV.setDiffVoltage(cursor.getString(21));
                incrementalSurveyVoUG33KV.setSsId(cursor.getString(22));
                incrementalSurveyVoUG33KV.setStjoint_rmu(cursor.getString(23));
                incrementalSurveyVoUG33KV.setRmu_type(cursor.getString(24));
                incrementalSurveyVoUG33KV.setRemarks(cursor.getString(25));
                incrementalSurveyVoUG33KV.setTappingLocation(cursor.getString(26));
                incrementalSurveyVoUG33KV.setNoofTappings(cursor.getString(27));
                incrementalSurveyVoUG33KV.setWorkOrder(cursor.getString(28));
                incrementalSurveyVoUG33KV.setWorkOrderStatus(cursor.getString(29));
                incrementalSurveyVoUG33KV.setIsFeederType(cursor.getString(30));
                incrementalSurveyVoUG33KV.setHtServiceNumber(cursor.getString(31));
                //  vo.setSECTION_CODE(cursor.getString(8));
                incrementalSurveyVosListUG33KV.add(incrementalSurveyVoUG33KV);
            } while (cursor.moveToNext());
        }

        db.close();
        return incrementalSurveyVosListUG33KV;
    }

    public boolean addWoStatusMasterData(WoStatusMasterVoUG33KV woStatusMasterVoUG33KV) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FEEDER_ID, woStatusMasterVoUG33KV.getFEEDER_ID());
        values.put(WO_ID, woStatusMasterVoUG33KV.getWO_ID());
        values.put(WO_STATUS, woStatusMasterVoUG33KV.getWO_STATUS());
        long a = db.insert(WO_STATUS_TABLE, null, values);
        if(a==-1){
            db.close();
            return false;
        }else {
            db.close();
            return true;
        }

    }
    public List<WoStatusMasterVoUG33KV> getWoStatusMasterData(){
        ArrayList<WoStatusMasterVoUG33KV> woStatusMasterVosListUG33KV = new ArrayList<WoStatusMasterVoUG33KV>();

        String  query = "SELECT * FROM WO_STATUS_TABLE";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                WoStatusMasterVoUG33KV woStatusMasterVoUG33KV = new WoStatusMasterVoUG33KV();
                //vo.setLat(cursor.getString(0));
                woStatusMasterVoUG33KV.setFEEDER_ID(cursor.getString(0));
                woStatusMasterVoUG33KV.setWO_ID(cursor.getString(1));
                woStatusMasterVoUG33KV.setWO_STATUS(cursor.getString(2));

                //  vo.setSECTION_CODE(cursor.getString(8));
                woStatusMasterVosListUG33KV.add(woStatusMasterVoUG33KV);
            } while (cursor.moveToNext());
        }

        db.close();
        return woStatusMasterVosListUG33KV;
    }


    public List<DistandeVoUG33KV> getDistance() {
        List<DistandeVoUG33KV> distandeVoUG33KVArrayList = new ArrayList<>();
        // Select All Query
        String selectQuery ="SELECT  POLE_NO,LAT,LAN  FROM INCREMENTAL_ARCGIS_TABLE  WHERE ROWID IN(SELECT MAX(ROWID) FROM INCREMENTAL_ARCGIS_TABLE  WHERE POLE_NO=(\n" +
                "SELECT  MAX(POLE_NO) FROM INCREMENTAL_ARCGIS_TABLE)) ";


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    DistandeVoUG33KV distandeVoUG33KV = new DistandeVoUG33KV();
                    distandeVoUG33KV.setPOLE_NO(cursor.getString(0));
                    distandeVoUG33KV.setLAT(cursor.getString(1));
                    distandeVoUG33KV.setLAN(cursor.getString(2));
                    distandeVoUG33KVArrayList.add(distandeVoUG33KV);
                } while (cursor.moveToNext());
            }
        } finally {
            db.close();
        }
        // return contact list
        return distandeVoUG33KVArrayList;
    }

}
