package com.apepdcl.gis_survey.METER_SURVEY;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.apepdcl.gis_survey.estimates.models.PoleHeightModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SOUBHAGYA SWAIN on 09-06-2026.
 */
public class MeterSurveyDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "METER_SURVEY_DB";

    private static final String POSTING_TABLE = "POSTING_TABLE";
    private static final String REF_ID = "REF_ID";
    private static final String POLE_ID = "POLE_ID";
    private static final String POLE_NO = "POLE_NO";
    private static final String SERVICE_NUMBER = "SERVICE_NUMBER";
    private static final String DTR_CODE = "DTR_CODE";
    private static final String LATITUDE = "LATITUDE";
    private static final String LONGITUDE = "LONGITUDE";
    private static final String IMAGE = "IMAGE";
    private static final String REMARKS = "REMARKS";
    private static final String DTR_NAME = "DTR_NAME";
    private static final String UKSC_NO = "UKSC_NO";
    private static final String GPS_ACCURACY = "GPS_ACCURACY";
    private static final String CURRENT_LAT = "CURRENT_LAT";
    private static final String CURRENT_LON = "CURRENT_LON";
    private static final String IS_IMAGE_TRANSMITTED = "IS_IMAGE_TRANSMITTED";
    private static final String LT_CKT_CODE = "LT_CKT_CODE";
    private static final String SC_NAME = "SC_NAME";
    private static final String SURVEY_MODE = "SURVEY_MODE";
    private static final String FLAG = "FLAG";


    private static final String COUNT_TABLE = "COUNT_TABLE";
    private static final String COUNT_POLE_ID = "COUNT_POLE_ID";
    private static final String COUNT_POLE_NO = "COUNT_POLE_NO";
    private static final String COUNT_TOTAL = "COUNT_TOTAL";
    private static final String COUNT_REC_ID = "COUNT_REC_ID";
    private static final String POLE_IMAGE = "POLE_IMAGE";
    private static final String IS_IMAGE_TRANSMITTED_PC = "IS_IMAGE_TRANSMITTED_PC";
    private static final String IS_IMAGE_TRANSMITTED_PC_LOC = "IS_IMAGE_TRANSMITTED_PC_LOC";

    String CREATE_COUNT_TABLE = "CREATE TABLE " + COUNT_TABLE + "("
            + COUNT_POLE_ID + " TEXT ,"
            + COUNT_POLE_NO + " TEXT ,"
            + COUNT_TOTAL + " TEXT ,"
            + COUNT_REC_ID + " TEXT ,"
            + POLE_IMAGE + " TEXT ,"
            + IS_IMAGE_TRANSMITTED_PC + " TEXT ,"
            + IS_IMAGE_TRANSMITTED_PC_LOC + " TEXT ,"


            + FLAG + " TEXT " + ")";

    String CREATE_POSTING_TABLE = "CREATE TABLE " + POSTING_TABLE + "("
            + REF_ID + " TEXT ,"
            + POLE_ID + " TEXT ,"
            + POLE_NO + " TEXT ,"
            + SERVICE_NUMBER + " TEXT ,"
            + DTR_CODE + " TEXT ,"
            + LATITUDE + " TEXT ,"
            + LONGITUDE + " TEXT ,"
            + IMAGE + " TEXT ,"
            + REMARKS + " TEXT ,"
            + DTR_NAME + " TEXT ,"
            + UKSC_NO + " TEXT ,"
            + GPS_ACCURACY + " TEXT ,"
            + CURRENT_LAT + " TEXT ,"
            + CURRENT_LON + " TEXT ,"
            + IS_IMAGE_TRANSMITTED + " TEXT ,"
            + LT_CKT_CODE + " TEXT ,"
            + SC_NAME + " TEXT ,"
            + SURVEY_MODE + " TEXT ,"


            + FLAG + " TEXT " + ")";

    public MeterSurveyDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_POSTING_TABLE);
        db.execSQL(CREATE_COUNT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + POSTING_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + COUNT_TABLE);
        onCreate(db);
    }

    public boolean addMeterSurveyData(MeterPostingModel data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(REF_ID, data.getREF_ID());
        values.put(POLE_ID, data.getPOLE_ID());
        values.put(POLE_NO, data.getPOLE_NO());
        values.put(SERVICE_NUMBER, data.getSERVICE_NUMBER());
        values.put(DTR_CODE, data.getDTR_CODE());
        values.put(LATITUDE, data.getLATITUDE());
        values.put(LONGITUDE, data.getLONGITUDE());
        values.put(IMAGE, data.getIMAGE());
        values.put(REMARKS, data.getREMARKS());
        values.put(DTR_NAME, data.getDTR_NAME());
        values.put(UKSC_NO, data.getUKSC_NO());

        values.put(GPS_ACCURACY, data.getGPS_ACCURACY());
        values.put(CURRENT_LAT, data.getCURRENT_LAT());
        values.put(CURRENT_LON, data.getCURRENT_LON());
        values.put(IS_IMAGE_TRANSMITTED, data.getIS_IMAGE_TRANSMITTED());
        values.put(LT_CKT_CODE, data.getLT_CKT_CODE());
        values.put(SC_NAME, data.getSC_NAME());
        values.put(SURVEY_MODE, data.getSURVEY_MODE());

        values.put(FLAG, "N");

        long a = db.insert(POSTING_TABLE, null, values);
        db.close();
        if (a == -1) {
            return false;
        } else {
            return true;
        }

    }
    public List<MeterPostingModel> getAllMeterSurveyData() {
        ArrayList<MeterPostingModel> data = new ArrayList<>();
        String query = "SELECT * FROM POSTING_TABLE";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    MeterPostingModel model = new MeterPostingModel();
                    model.setREF_ID(cursor.getString(0));
                    model.setPOLE_ID(cursor.getString(1));
                    model.setPOLE_NO(cursor.getString(2));
                    model.setSERVICE_NUMBER(cursor.getString(3));
                    model.setDTR_CODE(cursor.getString(4));
                    model.setLATITUDE(cursor.getString(5));
                    model.setLONGITUDE(cursor.getString(6));
                    model.setIMAGE(cursor.getString(7));
                    model.setREMARKS(cursor.getString(8));
                    model.setDTR_NAME(cursor.getString(9));
                    model.setUKSC_NO(cursor.getString(10));
                    model.setGPS_ACCURACY(cursor.getString(11));
                    model.setCURRENT_LAT(cursor.getString(12));
                    model.setCURRENT_LON(cursor.getString(13));
                    model.setIS_IMAGE_TRANSMITTED(cursor.getString(14));
                    model.setLT_CKT_CODE(cursor.getString(15));
                    model.setSC_NAME(cursor.getString(16));
                    model.setSURVEY_MODE(cursor.getString(17));
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

    public List<MeterPostingModel> getOnlyLatLonData(String dtrCode) {
        ArrayList<MeterPostingModel> data = new ArrayList<>();
        String query = "SELECT * FROM POSTING_TABLE WHERE DTR_CODE = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, new String[]{dtrCode});
            if (cursor.moveToFirst()) {
                do {
                    MeterPostingModel model = new MeterPostingModel();
                 /*   model.setREF_ID(cursor.getString(0));
                    model.setPOLE_ID(cursor.getString(1));*/
                    model.setPOLE_NO(cursor.getString(2));
                    model.setSERVICE_NUMBER(cursor.getString(3));
                    //model.setDTR_CODE(cursor.getString(4));
                    model.setLATITUDE(cursor.getString(5));
                    model.setLONGITUDE(cursor.getString(6));
                    /*model.setIMAGE(cursor.getString(7));
                    model.setREMARKS(cursor.getString(8));*/
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


    public List<MeterPostingModel> getSinglePoleMeterSurveyData(String poleId) {
        ArrayList<MeterPostingModel> data = new ArrayList<>();
        String query = "SELECT * FROM POSTING_TABLE WHERE POLE_ID = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, new String[]{poleId});
            if (cursor.moveToFirst()) {
                do {
                    MeterPostingModel model = new MeterPostingModel();
                 /*   model.setREF_ID(cursor.getString(0));
                    model.setPOLE_ID(cursor.getString(1));
                    model.setPOLE_NO(cursor.getString(2));*/
                    model.setSERVICE_NUMBER(cursor.getString(3));
                    //model.setDTR_CODE(cursor.getString(4));
                    model.setLATITUDE(cursor.getString(5));
                    model.setLONGITUDE(cursor.getString(6));
                    /*model.setIMAGE(cursor.getString(7));
                    model.setREMARKS(cursor.getString(8));*/
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

    public void deleteAllPostingData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(POSTING_TABLE, null, null);
        db.delete(COUNT_TABLE, null, null);
        db.close();

    }

    public void updateImageShortCode(String ref_id, String shortCode){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IMAGE, shortCode);
        values.put(IS_IMAGE_TRANSMITTED, "Y");

        int rowsAffected = db.update(
                POSTING_TABLE,           // Table name
                values,            // New values
                REF_ID+" = ?",          // WHERE clause
                new String[]{ref_id}  // WHERE arguments
        );

        db.close();

    }


    public void updateImageShortCodePoleCount(String ref_id, String shortCode){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(POLE_IMAGE, shortCode);
        values.put(IS_IMAGE_TRANSMITTED_PC, "Y");

        int rowsAffected = db.update(
                COUNT_TABLE,           // Table name
                values,            // New values
                COUNT_POLE_ID+" = ?",          // WHERE clause
                new String[]{ref_id}  // WHERE arguments
        );

        db.close();

    }



    public boolean insertOrUpdateCountData(PoleCountModel data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COUNT_POLE_ID, data.getCOUNT_POLE_ID());
        values.put(COUNT_POLE_NO, data.getCOUNT_POLE_NO());
        values.put(COUNT_TOTAL, data.getCOUNT_TOTAL());
        values.put(COUNT_REC_ID, data.getREC_ID());
        values.put(POLE_IMAGE, data.getIMAGE());
        values.put(IS_IMAGE_TRANSMITTED_PC, data.getIS_IMAGE_TRANSMITTED());
        values.put(IS_IMAGE_TRANSMITTED_PC_LOC, data.getIS_IMAGE_TRANSMITTED());
        values.put(FLAG, "N");

        // Try to update first
        int rowsAffected = db.update(
                COUNT_TABLE,
                values,
                COUNT_POLE_ID + " = ?",
                new String[]{String.valueOf(data.getCOUNT_POLE_ID())}
        );

        if (rowsAffected == 0) {
            // If no row updated, insert new
            long a = db.insert(COUNT_TABLE, null, values);
            db.close();
            return a != -1;
        } else {
            db.close();
            return true;
        }
    }

    public List<PoleCountModel> getPoleCountData() {
        ArrayList<PoleCountModel> data = new ArrayList<>();
        String query = "SELECT * FROM COUNT_TABLE";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    PoleCountModel model = new PoleCountModel();
                    model.setCOUNT_POLE_ID(cursor.getString(0));
                    model.setCOUNT_POLE_NO(cursor.getString(1));
                    model.setCOUNT_TOTAL(cursor.getString(2));
                    model.setREC_ID(cursor.getString(3));
                    model.setIMAGE(cursor.getString(4));
                    model.setIS_IMAGE_TRANSMITTED(cursor.getString(5));
                    model.setIS_IMAGE_TRANSMITTED_PC_LOC(cursor.getString(6));
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

    public void deleteCountData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(COUNT_TABLE, null, null);
        db.close();

    }


}
