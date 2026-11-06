package com.apepdcl.gis_survey.PMI;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.apepdcl.gis_survey.PMI.models.OfflineDataFor11;
import com.apepdcl.gis_survey.PMI.models.PostingModel;
import com.apepdcl.gis_survey.PMI.rectification.offline.OfflineSurveyLists;

import java.util.ArrayList;
import java.util.HashSet;

public class PmiDatabase extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 12; //19-11-2025 Added
    private static final String DATABASE_NAME = "PMI_DB";

    private static final String PMI_TABLE_POSTING = "PMI_TABLE_POSTING";
    private static final String PMI_DISTANCE_CHECK = "PMI_DISTANCE_CHECK";


    private static final String PMI_OFFLINE_RECTIFICATION_TABLE = "PMI_OFFLINE_RECTIFICATION_TABLE";
    private static final String DEFECT_ID = "DEFECT_ID";
    private static final String ONLINE_DISTANCE = "ONLINE_DISTANCE";
    private static final String OFFLINE_DISTANCE = "OFFLINE_DISTANCE";
    private static final String POLE_ID = "POLE_ID";
    private static final String VOLTAGE_LEVEL = "VOLTAGE_LEVEL";
    private static final String DTR_CODE = "DTR_CODE";
    private static final String LT_CKT_CODE = "LT_CKT_CODE";
    private static final String FEEDER_ID = "FEEDER_ID";
    private static final String DEFECT_TYPE = "DEFECT_TYPE";
    private static final String DEFECT_SUB_TYPE = "DEFECT_SUB_TYPE";
    public static final String IMAGE = "IMAGE";
    public static final String DTR_IMAGE = "DTR_IMAGE";
    private static final String USERID = "USERID";
    private static final String USERNAME = "USERNAME";
    private static final String RECT_REMARKS = "RECT_REMARKS";
    private static final String DEFECT_SUB_TYPE_ID = "DEFECT_SUB_TYPE_ID";
    private static final String POLE_NO = "POLE_NO";
    private static final String ID = "ID";
    private static final String IMAGE_TYPE = "IMAGE_TYPE";//, , , ;
    private static final String VOLTAGE = "VOLTAGE";
    private static final String POSITION = "POSITION";
    private static final String AI_RESPONSE = "AI_RESPONSE";
    private static final String LEAN_ANGLE = "LEAN_ANGLE";
    private static final String POSTITION_XARM = "POSTITION_XARM";
    private static final String CYCLONE = "CYCLONE";
    private static final String POLE_RATIO = "POLE_RATIO";
    private static final String PLINTH_RATIO = "PLINTH_RATIO";
    private static final String CURRENT_LAT = "CURRENT_LAT";
    private static final String CURRENT_LONG = "CURRENT_LONG";
    private static final String POLE_BBOX = "POLE_BBOX";
    private static final String CROP_BBOX = "CROP_BBOX";

    private static final String SURVEY_TYPE = "SURVEY_TYPE";
    private static final String DEFECT_GROUP = "DEFECT_GROUP";


    private static final String FLAG = "FLAG";
    private static final String GRP_ID = "GRP_ID";
    private static final String SHORT_PATH = "SHORT_PATH";
    private static final String POLE_TYPE = "POLE_TYPE";
    private static final String IMG_URL = "IMG_URL";
    private static final String AI_FLAG = "AI_FLAG";
    private static final String POLE_CONFIG = "POLE_CONFIG";
    private static final String POLE_HEIGHT = "POLE_HEIGHT";
    private static final String COMPLETE_SURVEY = "COMPLETE_SURVEY";
    private static final String RECTIFIED_DEFECT_SUB_TYPE  = "RECTIFIED_DEFECT_SUB_TYPE ";
    private static final String POLE_LAT  = "POLE_LAT";
    private static final String POLE_LNG  = "POLE_LNG";
    private static final String AI_IMAGE  = "AI_IMAGE";
    private static final String DEFECT_BY_USER  = "DEFECT_BY_USER";



    String CREATE_PMI_OFFLINE_DATA_RECTIFICATION =
            "CREATE TABLE IF NOT EXISTS " + PMI_OFFLINE_RECTIFICATION_TABLE + " ("
                    + POLE_NO + " TEXT, "
                    + GRP_ID + " TEXT, "
                    + SHORT_PATH + " TEXT, "
                    + POLE_TYPE + " TEXT, "
                    + FEEDER_ID + " TEXT, "
                    + IMG_URL + " TEXT, "
                    + DEFECT_TYPE + " TEXT, "
                    + DEFECT_SUB_TYPE + " TEXT, "
                    + AI_FLAG + " TEXT, "
                    + POLE_CONFIG + " TEXT, "
                    + POLE_HEIGHT + " TEXT, "
                    + POLE_ID + " TEXT, "
                    + COMPLETE_SURVEY + " TEXT, "
                    + RECTIFIED_DEFECT_SUB_TYPE + " TEXT, "
                    + POLE_LAT + " TEXT, "
                    + POLE_LNG + " TEXT, "
                    + FLAG + " TEXT"
                    + ")";

    String CREATE_PMI_DISTANCE_CHECK =
            "CREATE TABLE IF NOT EXISTS " + PMI_DISTANCE_CHECK + " ("
                    + OFFLINE_DISTANCE + " TEXT, "
                    + ONLINE_DISTANCE + " TEXT, "
                    + FLAG + " TEXT"
                    + ")";


    String CREATE_PMI_TABLE_POSTING = "CREATE TABLE " + PMI_TABLE_POSTING + "("
            + DEFECT_ID + " TEXT ,"
            + POLE_ID + " TEXT ,"
            + VOLTAGE_LEVEL + " TEXT ,"
            + DTR_CODE + " TEXT ,"
            + LT_CKT_CODE + " TEXT ,"
            + FEEDER_ID + " TEXT ,"
            + DEFECT_TYPE + " TEXT ,"
            + DEFECT_SUB_TYPE + " TEXT ,"
            + IMAGE + " TEXT ,"
            + USERID + " TEXT ,"
            + USERNAME + " TEXT ,"
            + RECT_REMARKS + " TEXT ,"
            + DEFECT_SUB_TYPE_ID + " TEXT ,"
            + POLE_NO + " TEXT ,"
            + ID + " TEXT ,"
            + IMAGE_TYPE + " TEXT ,"
            + VOLTAGE + " TEXT ,"
            + POSITION + " TEXT ,"
            + AI_RESPONSE + " TEXT ,"
            + LEAN_ANGLE + " TEXT ,"
            + POSTITION_XARM + " TEXT ,"
            + CYCLONE + " TEXT ,"
            + POLE_RATIO + " TEXT ,"
            + PLINTH_RATIO + " TEXT ,"
            + CURRENT_LAT + " TEXT ,"
            + CURRENT_LONG + " TEXT ,"
            + POLE_BBOX + " TEXT ,"
            + CROP_BBOX + " TEXT ,"
            + SURVEY_TYPE + " TEXT ,"
            + DEFECT_GROUP + " TEXT ,"
            + DTR_IMAGE + " TEXT ,"
            + AI_FLAG + " TEXT ,"
            + GRP_ID + " TEXT ,"
            + AI_IMAGE + " TEXT ,"
            + DEFECT_BY_USER + " TEXT ,"
            + POLE_LAT + " TEXT ,"
            + POLE_LNG + " TEXT ,"


            + FLAG + " TEXT " + ")";

    public PmiDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PMI_TABLE_POSTING);
        db.execSQL(CREATE_PMI_OFFLINE_DATA_RECTIFICATION);
        db.execSQL(CREATE_PMI_DISTANCE_CHECK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PMI_TABLE_POSTING);
        if (oldVersion < 11) {
            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS "
                            + PMI_OFFLINE_RECTIFICATION_TABLE + " ("
                            + POLE_NO + " TEXT, "
                            + GRP_ID + " TEXT, "
                            + SHORT_PATH + " TEXT, "
                            + POLE_TYPE + " TEXT, "
                            + FEEDER_ID + " TEXT, "
                            + IMG_URL + " TEXT, "
                            + DEFECT_TYPE + " TEXT, "
                            + DEFECT_SUB_TYPE + " TEXT, "
                            + AI_FLAG + " TEXT, "
                            + POLE_CONFIG + " TEXT, "
                            + POLE_HEIGHT + " TEXT, "
                            + POLE_ID + " TEXT, "
                            + COMPLETE_SURVEY + " TEXT, "
                            + RECTIFIED_DEFECT_SUB_TYPE + " TEXT, "
                            + POLE_LAT + " TEXT, "
                            + POLE_LNG + " TEXT, "
                            + AI_IMAGE + " TEXT, "
                            + DEFECT_BY_USER + " TEXT, "
                            + FLAG + " TEXT"
                            + ")"
            );
        }
        onCreate(db);
    }

    public void insertOrUpdatePmiPosting(PostingModel editModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM PMI_TABLE_POSTING WHERE POLE_ID = '" + editModel.getPOLE_ID() + "' AND VOLTAGE_LEVEL = '" + editModel.getVOLTAGE_LEVEL() + "' AND AI_RESPONSE = '" + editModel.getAI_RESPONSE() + "' AND DEFECT_SUB_TYPE_ID = '" + editModel.getDEFECT_SUB_TYPE_ID() + "'";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            // Record exists, perform update
            ContentValues contentValues = new ContentValues();
            contentValues.put(DTR_CODE, editModel.getDTR_CODE());
            contentValues.put(LT_CKT_CODE, editModel.getLT_CKT_CODE());
            contentValues.put(FEEDER_ID, editModel.getFEEDER_ID());
            contentValues.put(DEFECT_TYPE, editModel.getDEFECT_TYPE());
            contentValues.put(DEFECT_SUB_TYPE, editModel.getDEFECT_SUB_TYPE());
            contentValues.put(IMAGE, editModel.getIMAGE());
            contentValues.put(AI_RESPONSE, editModel.getAI_RESPONSE());
            contentValues.put(IMAGE_TYPE, editModel.getIMAGE_TYPE());
            contentValues.put(POSITION, editModel.getPOSITION());
            contentValues.put(RECT_REMARKS, editModel.getRECT_REMARKS());
            contentValues.put(VOLTAGE, editModel.getVOLTAGE());
            contentValues.put(LEAN_ANGLE, editModel.getLEAN_ANGLE());
            contentValues.put(POSTITION_XARM, editModel.getPOSTITION_XARM());
            contentValues.put(CYCLONE, editModel.getCYCLONE());
            contentValues.put(POLE_RATIO, editModel.getPOLE_RATIO());
            contentValues.put(PLINTH_RATIO, editModel.getPLINTH_RATIO());
            contentValues.put(CURRENT_LAT, editModel.getCURRENT_LAT());
            contentValues.put(CURRENT_LONG, editModel.getCURRENT_LONG());
            contentValues.put(POLE_BBOX, editModel.getPOLE_BBOX());
            contentValues.put(CROP_BBOX, editModel.getCROP_BBOX());
            contentValues.put(DTR_IMAGE, editModel.getDTR_IMAGE());
            contentValues.put(GRP_ID, editModel.getGRP_ID());
            contentValues.put(AI_FLAG, editModel.getAI_FLAG());
            contentValues.put(AI_IMAGE, editModel.getAI_IMAGE());
            contentValues.put(DEFECT_BY_USER, editModel.getDEFECT_BY_USER());
            contentValues.put(POLE_LAT, editModel.getPOLE_LAT());
            contentValues.put(POLE_LNG, editModel.getPOLE_LONG());


            // Update the existing record
            db.update(PMI_TABLE_POSTING, contentValues,
                    POLE_ID + " = ? AND " + VOLTAGE_LEVEL + " = ? AND " + DEFECT_SUB_TYPE_ID + " = ? AND " + AI_RESPONSE + " = ? ",
                    new String[]{String.valueOf(editModel.getPOLE_ID()), String.valueOf(editModel.getVOLTAGE_LEVEL()), String.valueOf(editModel.getDEFECT_SUB_TYPE_ID())
                            , String.valueOf(editModel.getAI_RESPONSE())});
        } else {
            // Record does not exist, insert new
            ContentValues contentValues = new ContentValues();
            contentValues.put(SURVEY_TYPE, "PMI");
            contentValues.put(DEFECT_GROUP, editModel.getDEFECT_GROUP());
            contentValues.put(DEFECT_ID, editModel.getDEFECT_ID());
            contentValues.put(POLE_ID, editModel.getPOLE_ID());
            contentValues.put(VOLTAGE_LEVEL, editModel.getVOLTAGE_LEVEL());
            contentValues.put(DTR_CODE, editModel.getDTR_CODE());
            contentValues.put(LT_CKT_CODE, editModel.getLT_CKT_CODE());
            contentValues.put(FEEDER_ID, editModel.getFEEDER_ID());
            contentValues.put(DEFECT_TYPE, editModel.getDEFECT_TYPE());
            contentValues.put(DEFECT_SUB_TYPE, editModel.getDEFECT_SUB_TYPE());
            contentValues.put(DEFECT_SUB_TYPE_ID, editModel.getDEFECT_SUB_TYPE_ID());
            contentValues.put(IMAGE, editModel.getIMAGE());
            contentValues.put(USERID, editModel.getUSERID());
            contentValues.put(USERNAME, editModel.getUSERNAME());
            contentValues.put(RECT_REMARKS, editModel.getRECT_REMARKS());
            contentValues.put(POLE_NO, editModel.getPOLE_NO());
            contentValues.put(ID, editModel.getID());
            contentValues.put(IMAGE_TYPE, editModel.getIMAGE_TYPE());
            contentValues.put(VOLTAGE, editModel.getVOLTAGE());
            contentValues.put(POSITION, editModel.getPOSITION());
            contentValues.put(AI_RESPONSE, editModel.getAI_RESPONSE());
            contentValues.put(LEAN_ANGLE, editModel.getLEAN_ANGLE());
            contentValues.put(POSTITION_XARM, editModel.getPOSTITION_XARM());
            contentValues.put(CYCLONE, editModel.getCYCLONE());
            contentValues.put(POLE_RATIO, editModel.getPOLE_RATIO());
            contentValues.put(PLINTH_RATIO, editModel.getPLINTH_RATIO());
            contentValues.put(CURRENT_LAT, editModel.getCURRENT_LAT());
            contentValues.put(CURRENT_LONG, editModel.getCURRENT_LONG());
            contentValues.put(DTR_IMAGE, editModel.getDTR_IMAGE());
            contentValues.put(GRP_ID, editModel.getGRP_ID());
            contentValues.put(AI_FLAG, editModel.getAI_FLAG());
            contentValues.put(AI_IMAGE, editModel.getAI_IMAGE());
            contentValues.put(DEFECT_BY_USER, editModel.getDEFECT_BY_USER());
            contentValues.put(POLE_LAT, editModel.getPOLE_LAT());
            contentValues.put(POLE_LNG, editModel.getPOLE_LONG());
            contentValues.put(FLAG, "N");

            // Insert the new record
            db.insert(PMI_TABLE_POSTING, null, contentValues);
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
    }

    public void insertDistanceCheck(String offlineDistance, String onlineDistance) {

        try (SQLiteDatabase db = this.getWritableDatabase()) {

            db.beginTransaction();

            try {
                // Delete existing records
                db.delete(PMI_DISTANCE_CHECK, null, null);

                ContentValues contentValues = new ContentValues();
                contentValues.put(OFFLINE_DISTANCE, offlineDistance);
                contentValues.put(ONLINE_DISTANCE, onlineDistance);
                contentValues.put(FLAG, "N");

                long result = db.insert(PMI_DISTANCE_CHECK, null, contentValues);

                if (result != -1) {
                    db.setTransactionSuccessful();
                }

            } finally {
                db.endTransaction();
            }
        }
    }

    public int[] getPmiDistanceCheckData() {

        int[] data = new int[3];

        String query = "SELECT * FROM " + PMI_DISTANCE_CHECK
                + " ORDER BY rowid DESC LIMIT 1";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                data[0] = cursor.getInt(0); // OFFLINE_DISTANCE
                data[1] = cursor.getInt(1); // ONLINE_DISTANCE
                data[2] = cursor.getInt(2); // FLAG
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return data;
    }

    public void insertOrUpdatePmiPostingForRectify(PostingModel editModel) {
        System.out.println("QUERY p1: " + editModel.getPOLE_ID());
        System.out.println("QUERY p2: " + editModel.getDEFECT_GROUP());
        String TAG = "PMI_DB";
        SQLiteDatabase db = this.getWritableDatabase();
        /*String query = "SELECT * FROM PMI_TABLE_POSTING WHERE POLE_ID = '" + editModel.getPOLE_ID()  + "'";
        System.out.println("QUERY : " + query);
        Cursor cursor = db.rawQuery(query, null);*/

//        String query = "SELECT * FROM PMI_TABLE_POSTING WHERE POLE_ID = ? AND DEFECT_GROUP = ? ";
        String query =
                "SELECT * FROM "
                        + PMI_TABLE_POSTING
                        + " WHERE "
                        + POLE_ID + " = ?"
                        + " AND "
                        + DEFECT_GROUP + " = ?"
                        + " AND "
                        + AI_FLAG + " = ?";
//        String[] selectionArgs = new String[]{editModel.getPOLE_ID(), editModel.getDEFECT_GROUP()};
        String[] selectionArgs = new String[]{
                editModel.getPOLE_ID(),
                editModel.getDEFECT_GROUP(),
                editModel.getAI_FLAG()
        };

        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor.moveToFirst()) {
            Log.d(TAG, "Record FOUND → Updating | POLE_ID: " + editModel.getPOLE_ID());
            // Record exists, perform update
            ContentValues contentValues = new ContentValues();
            contentValues.put(DTR_CODE, editModel.getDTR_CODE());
            contentValues.put(LT_CKT_CODE, editModel.getLT_CKT_CODE());
            contentValues.put(FEEDER_ID, editModel.getFEEDER_ID());
            contentValues.put(DEFECT_TYPE, editModel.getDEFECT_TYPE());
            contentValues.put(DEFECT_GROUP, editModel.getDEFECT_GROUP());
            contentValues.put(DEFECT_SUB_TYPE, editModel.getDEFECT_SUB_TYPE());
            contentValues.put(IMAGE, editModel.getIMAGE());
            contentValues.put(AI_RESPONSE, editModel.getAI_RESPONSE());
            contentValues.put(IMAGE_TYPE, editModel.getIMAGE_TYPE());
            contentValues.put(POSITION, editModel.getPOSITION());
            contentValues.put(RECT_REMARKS, editModel.getRECT_REMARKS());
            contentValues.put(VOLTAGE, editModel.getVOLTAGE());
            contentValues.put(LEAN_ANGLE, editModel.getLEAN_ANGLE());
            contentValues.put(POSTITION_XARM, editModel.getPOSTITION_XARM());
            contentValues.put(CYCLONE, editModel.getCYCLONE());
            contentValues.put(POLE_RATIO, editModel.getPOLE_RATIO());
            contentValues.put(PLINTH_RATIO, editModel.getPLINTH_RATIO());
            contentValues.put(CURRENT_LAT, editModel.getCURRENT_LAT());
            contentValues.put(CURRENT_LONG, editModel.getCURRENT_LONG());
            contentValues.put(POLE_BBOX, editModel.getPOLE_BBOX());
            contentValues.put(CROP_BBOX, editModel.getCROP_BBOX());
            contentValues.put(DTR_IMAGE, editModel.getDTR_IMAGE());
            contentValues.put(AI_FLAG, editModel.getAI_FLAG());
            contentValues.put(GRP_ID, editModel.getGRP_ID());
            contentValues.put(SURVEY_TYPE, editModel.getFROM());
            contentValues.put(AI_IMAGE, editModel.getAI_IMAGE());



            // Update the existing record
//            int rowsAffected = db.update(PMI_TABLE_POSTING, contentValues,
//                    POLE_ID + " = ? AND " + VOLTAGE_LEVEL + " = ? AND " + DEFECT_GROUP + " = ? AND " + AI_RESPONSE + " = ? ",
//                    new String[]{String.valueOf(editModel.getPOLE_ID()), String.valueOf(editModel.getVOLTAGE_LEVEL()), String.valueOf(editModel.getDEFECT_GROUP())
//                            , String.valueOf(editModel.getAI_RESPONSE())});
            int rowsAffected = db.update(
                    PMI_TABLE_POSTING,
                    contentValues,
                    POLE_ID + " = ? AND "
                            + DEFECT_GROUP + " = ? AND "
                            + AI_FLAG + " = ?",
                    new String[]{
                            editModel.getPOLE_ID(),
                            editModel.getDEFECT_GROUP(),
                            editModel.getAI_FLAG()
                    }
            );

            Log.d(TAG, "UPDATE DONE → Rows affected: " + rowsAffected);
        } else {
            Log.d(TAG, "Record NOT FOUND → Inserting | POLE_ID: " + editModel.getPOLE_ID());
            // Record does not exist, insert new
            ContentValues contentValues = new ContentValues();
            contentValues.put(DEFECT_ID, editModel.getDEFECT_ID());
            contentValues.put(POLE_ID, editModel.getPOLE_ID());
            contentValues.put(VOLTAGE_LEVEL, editModel.getVOLTAGE_LEVEL());
            contentValues.put(DTR_CODE, editModel.getDTR_CODE());
            contentValues.put(LT_CKT_CODE, editModel.getLT_CKT_CODE());
            contentValues.put(FEEDER_ID, editModel.getFEEDER_ID());
            contentValues.put(DEFECT_TYPE, editModel.getDEFECT_TYPE());
            contentValues.put(DEFECT_SUB_TYPE, editModel.getDEFECT_SUB_TYPE());
            contentValues.put(DEFECT_SUB_TYPE_ID, editModel.getDEFECT_SUB_TYPE_ID());
            contentValues.put(IMAGE, editModel.getIMAGE());
            contentValues.put(USERID, editModel.getUSERID());
            contentValues.put(USERNAME, editModel.getUSERNAME());
            contentValues.put(RECT_REMARKS, editModel.getRECT_REMARKS());
            contentValues.put(POLE_NO, editModel.getPOLE_NO());
            contentValues.put(ID, editModel.getID());
            contentValues.put(IMAGE_TYPE, editModel.getIMAGE_TYPE());
            contentValues.put(VOLTAGE, editModel.getVOLTAGE());
            contentValues.put(POSITION, editModel.getPOSITION());
            contentValues.put(AI_RESPONSE, editModel.getAI_RESPONSE());
            contentValues.put(LEAN_ANGLE, editModel.getLEAN_ANGLE());
            contentValues.put(POSTITION_XARM, editModel.getPOSTITION_XARM());
            contentValues.put(CYCLONE, editModel.getCYCLONE());
            contentValues.put(POLE_RATIO, editModel.getPOLE_RATIO());
            contentValues.put(PLINTH_RATIO, editModel.getPLINTH_RATIO());
            contentValues.put(CURRENT_LAT, editModel.getCURRENT_LAT());
            contentValues.put(CURRENT_LONG, editModel.getCURRENT_LONG());
            contentValues.put(SURVEY_TYPE, editModel.getFROM());
            contentValues.put(DEFECT_GROUP, editModel.getDEFECT_GROUP());
            contentValues.put(AI_FLAG, editModel.getAI_FLAG());
            contentValues.put(GRP_ID, editModel.getGRP_ID());
            contentValues.put(SURVEY_TYPE, editModel.getFROM());
            contentValues.put(AI_IMAGE, editModel.getAI_IMAGE());
            contentValues.put(DTR_IMAGE, editModel.getDTR_IMAGE());
            contentValues.put(FLAG, "N");

            // Insert the new record
            long rowId = db.insert(PMI_TABLE_POSTING, null, contentValues);
            Log.d(TAG, "INSERT DONE → Row ID: " + rowId);
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
    }

    public ArrayList<PostingModel> getPmiPostingData() {
        ArrayList<PostingModel> data = new ArrayList<>();
        String query = "SELECT * FROM PMI_TABLE_POSTING";
        // String query = "SELECT * FROM EDIT_TABLE WHERE EDIT_LOCATION_ID = '" + location_id + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    PostingModel model = new PostingModel();
                    model.setDEFECT_ID(cursor.getString(0));        // Update index as per the actual table schema
                    model.setPOLE_ID(cursor.getString(1)); // Update index as per the actual table schema
                    model.setVOLTAGE_LEVEL(cursor.getString(2));        // Update index as per the actual table schema
                    model.setDTR_CODE(cursor.getString(3));        // Update index as per the actual table schema
                    model.setLT_CKT_CODE(cursor.getString(4));        // Update index as per the actual table schema
                    model.setFEEDER_ID(cursor.getString(5));        // Update index as per the actual table schema
                    model.setDEFECT_TYPE(cursor.getString(6));        // Update index as per the actual table schema
                    model.setDEFECT_SUB_TYPE(cursor.getString(7));        // Update index as per the actual table schema
                    model.setIMAGE(cursor.getString(8));        // Update index as per the actual table schema
                    model.setUSERID(cursor.getString(9));        // Update index as per the actual table schema
                    model.setUSERNAME(cursor.getString(10));        // Update index as per the actual table schema
                    model.setRECT_REMARKS(cursor.getString(11));        // Update index as per the actual table schema
                    model.setDEFECT_SUB_TYPE_ID(cursor.getString(12));        // Update index as per the actual table schema
                    model.setPOLE_NO(cursor.getString(13));        // Update index as per the actual table schema
                    model.setID(cursor.getString(14));        // Update index as per the actual table schema
                    model.setIMAGE_TYPE(cursor.getString(15));        // Update index as per the actual table schema
                    model.setVOLTAGE(cursor.getString(16));        // Update index as per the actual table schema
                    model.setPOSITION(cursor.getString(17));        // Update index as per the actual table schema
                    model.setAI_RESPONSE(cursor.getString(18));        // Update index as per the actual table schema
                    model.setLEAN_ANGLE(cursor.getString(19));        // Update index as per the actual table schema
                    model.setPOSTITION_XARM(cursor.getString(20));        // Update index as per the actual table schema
                    model.setCYCLONE(cursor.getString(21));        // Update index as per the actual table schema
                    model.setPOLE_RATIO(cursor.getString(22));        // Update index as per the actual table schema
                    model.setPLINTH_RATIO(cursor.getString(23));        // Update index as per the actual table schema
                    model.setCURRENT_LAT(cursor.getString(24));        // Update index as per the actual table schema
                    model.setCURRENT_LONG(cursor.getString(25));        // Update index as per the actual table schema
                    model.setPOLE_BBOX(cursor.getString(26));        // Update index as per the actual table schema
                    model.setCROP_BBOX(cursor.getString(27));
                    model.setFROM(cursor.getString(28));        // Update index as per the actual table schema
                    model.setDEFECT_GROUP(cursor.getString(29)); // Update index as per the actual table schema
                    model.setDTR_IMAGE(cursor.getString(30)); // Update index as per the actual table schema
                    model.setAI_FLAG(cursor.getString(31)); // Update index as per the actual table schema
                    model.setGRP_ID(cursor.getString(32)); // Update index as per the actual table schema
                    model.setAI_IMAGE(cursor.getString(33)); // Update index as per the actual table schema
                    model.setDEFECT_BY_USER(cursor.getString(34)); // Update index as per the actual table schema
                    model.setPOLE_LAT(cursor.getString(35)); // Update index as per the actual table schema
                    model.setPOLE_LONG(cursor.getString(36)); // Update index as per the actual table schema
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

    public boolean deletePmiPostingData() {
        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.delete(PMI_TABLE_POSTING, null, null);
        //System.out.println("DELETE RESULT : " + result);
        if (result > 0) {
            return true;
        } else {
            return false;
        }

    }

    public boolean deleteSingleData(String voltage) {
        /*SQLiteDatabase database = this.getWritableDatabase();
        int result = database.delete(PMI_TABLE_POSTING, null, null);
        //System.out.println("DELETE RESULT : " + result);
        if (result > 0) {
            return true;
        } else {
            return false;
        }*/

        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = VOLTAGE_LEVEL + " = ? ";
        String[] whereArgs = new String[]{voltage};
        int rowsAffected = db.delete(PMI_TABLE_POSTING, whereClause, whereArgs);
        System.out.println("ROWS DELETED " + rowsAffected);
        db.close();
        if (rowsAffected > 0) {
            return true;
        } else {
            return false;
        }


    }

    public boolean deleteOfflineImage(String ID) {
        /*SQLiteDatabase database = this.getWritableDatabase();
        int result = database.delete(PMI_TABLE_POSTING, null, null);
        //System.out.println("DELETE RESULT : " + result);
        if (result > 0) {
            return true;
        } else {
            return false;
        }*/

        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = "ID = ?";
        String[] whereArgs = new String[]{ID};
        int rowsAffected = db.delete(PMI_TABLE_POSTING, whereClause, whereArgs);
        System.out.println("ROWS DELETED " + rowsAffected);
        db.close();
        if (rowsAffected > 0) {
            return true;
        } else {
            return false;
        }


    }

    public void deleteAllPmiData() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL("DELETE FROM " + PMI_TABLE_POSTING); // clears all rows
            db.execSQL("VACUUM"); // optional, reclaims space
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }





    public ArrayList<PostingModel> getCurrentPmiPostingData() {
        ArrayList<PostingModel> data = new ArrayList<>();
        String query = "SELECT * FROM " + PMI_TABLE_POSTING; // or add WHERE FLAG = 'N'

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    PostingModel model = new PostingModel();
                    model.setDEFECT_ID(cursor.getString(0));
                    model.setPOLE_ID(cursor.getString(1));
                    model.setVOLTAGE_LEVEL(cursor.getString(2));
                    model.setDTR_CODE(cursor.getString(3));
                    model.setLT_CKT_CODE(cursor.getString(4));
                    model.setFEEDER_ID(cursor.getString(5));
                    model.setDEFECT_TYPE(cursor.getString(6));
                    model.setDEFECT_SUB_TYPE(cursor.getString(7));
                    model.setIMAGE(cursor.getString(8));
                    model.setUSERID(cursor.getString(9));
                    model.setUSERNAME(cursor.getString(10));
                    model.setRECT_REMARKS(cursor.getString(11));
                    model.setDEFECT_SUB_TYPE_ID(cursor.getString(12));
                    model.setPOLE_NO(cursor.getString(13));
                    model.setID(cursor.getString(14));
                    model.setIMAGE_TYPE(cursor.getString(15));
                    model.setVOLTAGE(cursor.getString(16));
                    model.setPOSITION(cursor.getString(17));
                    model.setAI_RESPONSE(cursor.getString(18));
                    model.setLEAN_ANGLE(cursor.getString(19));
                    model.setPOSTITION_XARM(cursor.getString(20));
                    model.setCYCLONE(cursor.getString(21));
                    model.setPOLE_RATIO(cursor.getString(22));
                    model.setPLINTH_RATIO(cursor.getString(23));
                    model.setCURRENT_LAT(cursor.getString(24));
                    model.setCURRENT_LONG(cursor.getString(25));
                    model.setCURRENT_LAT(cursor.getString(24));        // Update index as per the actual table schema
                    model.setCURRENT_LONG(cursor.getString(25));
                    model.setPOLE_BBOX(cursor.getString(26));
                    model.setCROP_BBOX(cursor.getString(27));
                    model.setFROM(cursor.getString(28));
                    model.setDEFECT_GROUP(cursor.getString(29));
                    model.setDTR_IMAGE(cursor.getString(30));
                    model.setAI_FLAG(cursor.getString(31));
                    // model.setFLAG(cursor.getString(21));
                    data.add(model);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return data;
    }


    public ArrayList<OfflineDataFor11> getTotalOfflineDataToSurvey(
            String feederId) {

        ArrayList<OfflineDataFor11> data = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {

            String query =
                    "SELECT "
                            + "MAX(" + POLE_NO + ") AS " + POLE_NO + ", "
                            + "MAX(" + GRP_ID + ") AS " + GRP_ID + ", "

                            // AI + NON AI image paths
                            + "GROUP_CONCAT(DISTINCT " + SHORT_PATH + ") AS " + SHORT_PATH + ", "

                            + "MAX(" + POLE_TYPE + ") AS " + POLE_TYPE + ", "
                            + "MAX(" + FEEDER_ID + ") AS " + FEEDER_ID + ", "

                            + "GROUP_CONCAT(DISTINCT " + IMG_URL + ") AS " + IMG_URL + ", "

                            // Defect Types
                            + "GROUP_CONCAT(DISTINCT " + DEFECT_TYPE + ") AS " + DEFECT_TYPE + ", "

                            // Defect Sub Types
                            + "GROUP_CONCAT(DISTINCT " + DEFECT_SUB_TYPE + ") AS " + DEFECT_SUB_TYPE + ", "

                            // AI + NON AI
                            + "GROUP_CONCAT(DISTINCT " + AI_FLAG + ") AS " + AI_FLAG + ", "

                            + "MAX(" + POLE_CONFIG + ") AS " + POLE_CONFIG + ", "
                            + "MAX(" + POLE_HEIGHT + ") AS " + POLE_HEIGHT + ", "
                            + "MAX(" + POLE_ID + ") AS " + POLE_ID + ", "
                            + "MAX(" + COMPLETE_SURVEY + ") AS " + COMPLETE_SURVEY + ", "

                            // Rectified sub types
                            + "GROUP_CONCAT(DISTINCT "
                            + RECTIFIED_DEFECT_SUB_TYPE
                            + ") AS "
                            + RECTIFIED_DEFECT_SUB_TYPE + ", "

                            + "MAX(" + POLE_LAT + ") AS " + POLE_LAT + ", "
                            + "MAX(" + POLE_LNG + ") AS " + POLE_LNG + ", "
                            + "MAX(" + FLAG + ") AS " + FLAG

                            + " FROM "
                            + PMI_OFFLINE_RECTIFICATION_TABLE

                            + " WHERE "
                            + FEEDER_ID + " = ?"

                            + " AND "
                            + COMPLETE_SURVEY + " = ?"

                            + " GROUP BY "
                            + POLE_ID + ", "
                            + FEEDER_ID;

            Log.d("PMI_DB_QUERY", query);

            cursor = db.rawQuery(
                    query,
                    new String[]{
                            feederId,
                            "N"
                    }
            );

            if (cursor != null && cursor.moveToFirst()) {

                do {

                    OfflineDataFor11 model = new OfflineDataFor11();

                    model.setPOLE_NO(
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(POLE_NO)
                            )
                    );

                    model.setGRP_ID(
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(GRP_ID)
                            )
                    );

                    model.setSHORT_PATH(
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(SHORT_PATH)
                            )
                    );

                    model.setPOLE_TYPE(
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(POLE_TYPE)
                            )
                    );

                    model.setFEEDER_ID(
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(FEEDER_ID)
                            )
                    );

                    model.setIMG_URL(
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(IMG_URL)
                            )
                    );

                    model.setDEFECT_TYPE(
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(DEFECT_TYPE)
                            )
                    );

                    model.setDEFECT_SUB_TYPE(
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(DEFECT_SUB_TYPE)
                            )
                    );

                    model.setAI_FLAG(
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(AI_FLAG)
                            )
                    );

                    model.setPOLE_CONFIG(
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(POLE_CONFIG)
                            )
                    );

                    model.setPOLE_HEIGHT(
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(POLE_HEIGHT)
                            )
                    );

                    model.setPOLE_ID(
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(POLE_ID)
                            )
                    );

                    model.setGLAT(
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(POLE_LAT)
                            )
                    );

                    model.setGLONG(
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(POLE_LNG)
                            )
                    );

                    data.add(model);

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {

            Log.e(
                    "PMI_DB",
                    "getTotalOfflineDataToSurvey error",
                    e
            );

        } finally {

            if (cursor != null) {
                cursor.close();
            }

            db.close();
        }

        return data;
    }

    public ArrayList<OfflineDataFor11> getOfflineDataPoleNoWise(
            String feederId,
            String poleNo,
            String ai_flag) {

        ArrayList<OfflineDataFor11> data = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {

            String query =
                    "SELECT "
                            + POLE_NO + ", "
                            + GRP_ID + ", "
                            + SHORT_PATH + ", "
                            + POLE_TYPE + ", "
                            + FEEDER_ID + ", "
                            + IMG_URL + ", "
                            + "GROUP_CONCAT(" + DEFECT_TYPE + ", ',') AS " + DEFECT_TYPE + ", "
                            + "GROUP_CONCAT(" + DEFECT_SUB_TYPE + ", ',') AS " + DEFECT_SUB_TYPE + ", "
                            + AI_FLAG + ", "
                            + POLE_CONFIG + ", "
                            + POLE_HEIGHT + ", "
                            + POLE_ID + ", "
                            + COMPLETE_SURVEY + ", "
                            + RECTIFIED_DEFECT_SUB_TYPE + ", "
                            + POLE_LAT + ", "
                            + POLE_LNG + ", "
                            + FLAG
                            + " FROM " + PMI_OFFLINE_RECTIFICATION_TABLE
                            + " WHERE " + FEEDER_ID + " = ?"
                            + " AND " + POLE_NO + " = ?"
                            + " AND " + AI_FLAG + " = ?"
                            + " AND " + COMPLETE_SURVEY + " = ?"
                            + " GROUP BY "
                            + POLE_NO + ", "
                            + GRP_ID + ", "
                            + SHORT_PATH + ", "
                            + POLE_TYPE + ", "
                            + FEEDER_ID + ", "
                            + IMG_URL + ", "
                            + AI_FLAG + ", "
                            + POLE_CONFIG + ", "
                            + POLE_HEIGHT + ", "
                            + POLE_ID + ", "
                            + COMPLETE_SURVEY + ", "
                            + RECTIFIED_DEFECT_SUB_TYPE + ", "
                            + POLE_LAT + ", "
                            + POLE_LNG + ", "
                            + FLAG;

            cursor = db.rawQuery(
                    query,
                    new String[]{
                            feederId,
                            poleNo,
                            ai_flag,
                            "N"
                    }
            );

            if (cursor != null && cursor.moveToFirst()) {

                do {

                    OfflineDataFor11 model = new OfflineDataFor11();

                    model.setPOLE_NO(cursor.getString(
                            cursor.getColumnIndexOrThrow(POLE_NO)));

                    model.setGRP_ID(cursor.getString(
                            cursor.getColumnIndexOrThrow(GRP_ID)));

                    model.setSHORT_PATH(cursor.getString(
                            cursor.getColumnIndexOrThrow(SHORT_PATH)));

                    model.setPOLE_TYPE(cursor.getString(
                            cursor.getColumnIndexOrThrow(POLE_TYPE)));

                    model.setFEEDER_ID(cursor.getString(
                            cursor.getColumnIndexOrThrow(FEEDER_ID)));

                    model.setIMG_URL(cursor.getString(
                            cursor.getColumnIndexOrThrow(IMG_URL)));

                    // Multiple defect types combined with comma
                    model.setDEFECT_TYPE(cursor.getString(
                            cursor.getColumnIndexOrThrow(DEFECT_TYPE)));

                    // Multiple defect sub types combined with comma
                    model.setDEFECT_SUB_TYPE(cursor.getString(
                            cursor.getColumnIndexOrThrow(DEFECT_SUB_TYPE)));

                    model.setAI_FLAG(cursor.getString(
                            cursor.getColumnIndexOrThrow(AI_FLAG)));

                    model.setPOLE_CONFIG(cursor.getString(
                            cursor.getColumnIndexOrThrow(POLE_CONFIG)));

                    model.setPOLE_HEIGHT(cursor.getString(
                            cursor.getColumnIndexOrThrow(POLE_HEIGHT)));

                    model.setPOLE_ID(cursor.getString(
                            cursor.getColumnIndexOrThrow(POLE_ID)));

                    model.setGLAT(cursor.getString(
                            cursor.getColumnIndexOrThrow(POLE_LAT)));

                    model.setGLONG(cursor.getString(
                            cursor.getColumnIndexOrThrow(POLE_LNG)));

                    data.add(model);

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {

            Log.e("PMI_DB", "getOfflineDataPoleNoWise error", e);

        } finally {

            if (cursor != null) {
                cursor.close();
            }

            db.close();
        }

        return data;
    }

    public boolean insertOfflineRectification(OfflineDataFor11 model) {

        if (model == null) {
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();

        try {

            String defectType = model.getDEFECT_TYPE();
            String defectSubType = model.getDEFECT_SUB_TYPE();

            String aiFlag = model.getAI_FLAG() == null
                    ? ""
                    : model.getAI_FLAG().trim();



            if (aiFlag.isEmpty()) {
                Log.e("PMI_DB", "AI_FLAG is empty");
                return false;
            }

            if (defectSubType == null ||
                    defectSubType.trim().isEmpty()) {

                Log.e("PMI_DB", "DEFECT_SUB_TYPE is empty");
                return false;
            }


            String[] defectTypes = defectType == null
                    ? new String[0]
                    : defectType.split(",");

            String[] defectSubTypes =
                    defectSubType.split(",");

            if (defectTypes.length == 0) {
                Log.e("PMI_DB", "DEFECT_TYPE is empty");
                return false;
            }

            db.beginTransaction();

            // Prevent duplicate inside current model
            HashSet<String> insertedDefects =
                    new HashSet<>();



            for (int i = 0; i < defectSubTypes.length; i++) {

                String currentSubType =
                        defectSubTypes[i] == null
                                ? ""
                                : defectSubTypes[i].trim();

                if (currentSubType.isEmpty()) {
                    continue;
                }

                String currentType = "";



                if (defectTypes.length == defectSubTypes.length) {

                    currentType =
                            defectTypes[i].trim();

                }



                else {

                    if (defectTypes.length == 2) {

                        if (i == 0) {



                            currentType =
                                    defectTypes[0].trim();

                        } else {


                            currentType =
                                    defectTypes[1].trim();
                        }

                    } else {



                        if (i < defectTypes.length) {

                            currentType =
                                    defectTypes[i].trim();

                        } else {

                            currentType =
                                    defectTypes[
                                            defectTypes.length - 1
                                            ].trim();
                        }
                    }
                }

                if (currentType.isEmpty()) {
                    continue;
                }


                String defectKey =
                        aiFlag.toUpperCase()
                                + "||"
                                + currentType.toUpperCase()
                                + "||"
                                + currentSubType.toUpperCase();



                if (!insertedDefects.add(defectKey)) {

                    Log.d(
                            "PMI_DB",
                            "Duplicate in current model: "
                                    + defectKey
                    );

                    continue;
                }



                String checkQuery =
                        "SELECT COUNT(*) FROM "
                                + PMI_OFFLINE_RECTIFICATION_TABLE
                                + " WHERE "
                                + POLE_ID + " = ?"
                                + " AND "
                                + FEEDER_ID + " = ?"
                                + " AND "
                                + AI_FLAG + " = ?"
                                + " AND "
                                + DEFECT_TYPE + " = ?"
                                + " AND "
                                + DEFECT_SUB_TYPE + " = ?";

                Cursor cursor = db.rawQuery(
                        checkQuery,
                        new String[]{
                                safe(model.getPOLE_ID()),
                                safe(model.getFEEDER_ID()),
                                aiFlag,
                                currentType,
                                currentSubType
                        }
                );

                boolean alreadyExists = false;

                if (cursor.moveToFirst()) {

                    alreadyExists =
                            cursor.getInt(0) > 0;
                }

                cursor.close();

                if (alreadyExists) {

                    Log.d(
                            "PMI_DB",
                            "Already exists: "
                                    + defectKey
                    );

                    continue;
                }

                ContentValues values =
                        new ContentValues();

                values.put(
                        POLE_NO,
                        safe(model.getPOLE_NO())
                );

                values.put(
                        GRP_ID,
                        safe(model.getGRP_ID())
                );

                values.put(
                        SHORT_PATH,
                        safe(model.getSHORT_PATH())
                );

                values.put(
                        POLE_TYPE,
                        safe(model.getPOLE_TYPE())
                );

                values.put(
                        FEEDER_ID,
                        safe(model.getFEEDER_ID())
                );

                values.put(
                        IMG_URL,
                        safe(model.getIMG_URL())
                );

                // Current defect type
                values.put(
                        DEFECT_TYPE,
                        currentType
                );

                // Current subtype
                values.put(
                        DEFECT_SUB_TYPE,
                        currentSubType
                );

                // IMPORTANT:
                // AI and NON AI stored separately
                values.put(
                        AI_FLAG,
                        aiFlag
                );

                values.put(
                        POLE_CONFIG,
                        safe(model.getPOLE_CONFIG())
                );

                values.put(
                        POLE_HEIGHT,
                        safe(model.getPOLE_HEIGHT())
                );

                values.put(
                        POLE_ID,
                        safe(model.getPOLE_ID())
                );

                values.put(
                        COMPLETE_SURVEY,
                        "N"
                );

                values.put(
                        RECTIFIED_DEFECT_SUB_TYPE,
                        ""
                );

                values.put(
                        POLE_LAT,
                        safe(model.getGLAT())
                );

                values.put(
                        POLE_LNG,
                        safe(model.getGLONG())
                );

                values.put(
                        FLAG,
                        "N"
                );

                long rowId =
                        db.insert(
                                PMI_OFFLINE_RECTIFICATION_TABLE,
                                null,
                                values
                        );

                if (rowId == -1) {

                    throw new Exception(
                            "Insert failed: "
                                    + defectKey
                    );
                }

                Log.d(
                        "PMI_DB",
                        "INSERT SUCCESS"
                                + " | POLE="
                                + model.getPOLE_NO()
                                + " | AI_FLAG="
                                + aiFlag
                                + " | TYPE="
                                + currentType
                                + " | SUBTYPE="
                                + currentSubType
                                + " | ROW_ID="
                                + rowId
                );
            }

            db.setTransactionSuccessful();

            return true;

        } catch (Exception e) {

            Log.e(
                    "PMI_DB",
                    "insertOfflineRectification error",
                    e
            );

            return false;

        } finally {

            if (db.inTransaction()) {
                db.endTransaction();
            }

            db.close();
        }
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }

    public boolean deleteRectificationData(String feederId) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            int rowsDeleted = db.delete(
                    "PMI_OFFLINE_RECTIFICATION_TABLE",
                    "FEEDER_ID = ?",
                    new String[]{feederId}
            );

            return rowsDeleted > 0;

        } catch (Exception e) {
            Log.e("PMI_DB", "Error deleting rectification data", e);
            return false;

        } finally {
            db.close();
        }
    }

    public boolean isRectificationDataExists(String feederId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        try {
            cursor = db.rawQuery(
                    "SELECT 1 FROM PMI_OFFLINE_RECTIFICATION_TABLE " +
                            "WHERE FEEDER_ID = ? LIMIT 1",
                    new String[]{feederId}
            );

            return cursor.moveToFirst();

        } catch (Exception e) {
            Log.e("PMI_DB", "Error checking rectification data", e);
            return false;

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }

    public boolean updateCompleteSurveyToY(
            String feederId,
            String poleId,
            String defectSubType,
            String aiFlag) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {

            ContentValues values = new ContentValues();
            values.put(COMPLETE_SURVEY, "Y");

            int rowsUpdated = db.update(
                    PMI_OFFLINE_RECTIFICATION_TABLE,
                    values,
                    FEEDER_ID + " = ? AND "
                            + POLE_ID + " = ? AND "
                            + AI_FLAG + " = ? AND "
                            + DEFECT_SUB_TYPE + " = ?",
                    new String[]{
                            feederId,
                            poleId,
                            aiFlag == null ? "" : aiFlag.trim(),
                            defectSubType == null ? "" : defectSubType.trim()
                    }
            );

            Log.d(
                    "PMI_DB",
                    "Updated rows = " + rowsUpdated
                            + " | POLE_ID = " + poleId
                            + " | AI_FLAG = " + aiFlag
                            + " | DEFECT_SUB_TYPE = " + defectSubType
                            + " | COMPLETE_SURVEY = Y"
            );

            return rowsUpdated > 0;

        } catch (Exception e) {

            Log.e(
                    "PMI_DB",
                    "updateCompleteSurveyToY error",
                    e
            );

            return false;

        } finally {

            db.close();
        }
    }

    public void checkPostingTable() {

        SQLiteDatabase db = getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery(
                    "SELECT ID, POLE_ID, POLE_NO, AI_FLAG, GRP_ID " +
                            "FROM PMI_TABLE_POSTING",
                    null
            );

            Log.d("POSTING_CHECK===",
                    "DATABASE = " + db.getPath());

            Log.d("POSTING_CHECK===",
                    "TOTAL ROWS = " + cursor.getCount());

            while (cursor.moveToNext()) {

                Log.d(
                        "POSTING_CHECK===",
                        "ID=[" + cursor.getString(
                                cursor.getColumnIndexOrThrow("ID")
                        ) + "]"
                                + " | POLE_ID=[" + cursor.getString(
                                cursor.getColumnIndexOrThrow("POLE_ID")
                        ) + "]"
                                + " | POLE_NO=[" + cursor.getString(
                                cursor.getColumnIndexOrThrow("POLE_NO")
                        ) + "]"
                                + " | AI_FLAG=[" + cursor.getString(
                                cursor.getColumnIndexOrThrow("AI_FLAG")
                        ) + "]"
                                + " | GRP_ID=[" + cursor.getString(
                                cursor.getColumnIndexOrThrow("GRP_ID")
                        ) + "]"
                );
            }

            cursor.close();

        } finally {

            db.close();
        }
    }

}
