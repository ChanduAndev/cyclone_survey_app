package com.apepdcl.gis_survey.RDSS;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.os.Build;

import com.apepdcl.gis_survey.PMI.models.FeedersData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SOUBHAGYA SWAIN on 19-05-2025.
 */
public class RdssUGDatabase extends SQLiteOpenHelper {

    private Gson gson = new Gson();

    private static final int DATABASE_VERSION = 6;


    private static final String DATABASE_NAME = "RDSS_UG_DB";

    private static final String RDSS_UG_TABLE_POSTING = "RDSS_UG_TABLE_POSTING";

    private static final String LOCATION_ID = "LOCATION_ID";
    private static final String LOCATION_NO = "LOCATION_NO";
    private static final String SOURCE_LOCATION = "SOURCE_LOCATION";
    private static final String SS_NAME = "SS_NAME";
    private static final String SS_ID = "SS_ID";
    private static final String SS_TYPE = "SS_TYPE";
    private static final String CREATED_BY = "CREATED_BY";
    private static final String WIDTH = "WIDTH";
    private static final String DEPTH = "DEPTH";
    private static final String UTILITY = "UTILITY";
    private static final String COUNT_CABLES = "COUNT_CABLES";
    private static final String CABLE_33KV = "CABLE_33KV";
    private static final String CABLE_11KV = "CABLE_11KV";
    private static final String CABLE_LT = "CABLE_LT";
    private static final String LAT = "LAT";
    private static final String LOCATION_TYPE = "LOCATION_TYPE";
    private static final String RMU_TYPE = "RMU_TYPE";

    private static final String DTR_CAPACITY = "DTR_CAPACITY";
    private static final String HT_SCNO = "HT_SCNO";
    private static final String UTILITY_TYPE = "UTILITY_TYPE";
    private static final String LOCATION_REMARKS = "LOCATION_REMARKS";
    private static final String FEEDERS_DATA = "FEEDERS_DATA";
    private static final String CABLE_SIZE = "CABLE_SIZE";
    private static final String PROTECTION_PIPE = "PROTECTION_PIPE";
    private static final String DTR_CODE = "DTR_CODE";
    private static final String DTR_LOCATION = "DTR_LOCATION";
    private static final String REMARKS = "REMARKS";
    private static final String GPS_ACCURACY = "GPS_ACCURACY";


    ;
    private static final String LON = "LON";
    private static final String VOLTAGE = "VOLTAGE";
    private static final String IS_FRESH_SURVEY = "IS_FRESH_SURVEY";
    private static final String FROM_LAT = "FROM_LAT";
    private static final String FROM_LON = "FROM_LON";
    private static final String FLAG = "FLAG";

    String CREATE_RDSS_UG_TABLE_POSTING = "CREATE TABLE " + RDSS_UG_TABLE_POSTING + "("
            + LOCATION_ID + " TEXT ,"
            + LOCATION_NO + " TEXT ,"
            + SOURCE_LOCATION + " TEXT ,"
            + SS_NAME + " TEXT ,"
            + SS_ID + " TEXT ,"
            + SS_TYPE + " TEXT ,"
            + CREATED_BY + " TEXT ,"
            + WIDTH + " TEXT ,"
            + DEPTH + " TEXT ,"
            + UTILITY + " TEXT ,"
            + COUNT_CABLES + " TEXT ,"
            + CABLE_33KV + " TEXT ,"
            + CABLE_11KV + " TEXT ,"
            + CABLE_LT + " TEXT ,"
            + LAT + " TEXT ,"
            + LON + " TEXT ,"
            + VOLTAGE + " TEXT ,"
            + IS_FRESH_SURVEY + " TEXT ,"
            + FROM_LAT + " TEXT ,"
            + FROM_LON + " TEXT ,"
            + LOCATION_TYPE + " TEXT ,"
            + RMU_TYPE + " TEXT ,"
            + DTR_CAPACITY + " TEXT ,"
            + HT_SCNO + " TEXT ,"
            + UTILITY_TYPE + " TEXT ,"
            + LOCATION_REMARKS + " TEXT ,"
            + FEEDERS_DATA + " TEXT ,"
            + CABLE_SIZE + " TEXT ,"
            + PROTECTION_PIPE + " TEXT ,"
            + DTR_CODE + " TEXT ,"
            + DTR_LOCATION + " TEXT ,"
            + REMARKS + " TEXT ,"
            + GPS_ACCURACY + " TEXT ,"


            + FLAG + " TEXT " + ")";

    public RdssUGDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_RDSS_UG_TABLE_POSTING);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RDSS_UG_TABLE_POSTING);
        onCreate(sqLiteDatabase);
    }


    public void insertRdssPosting(UGPostingModel postingModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LOCATION_ID, postingModel.getLOCATION_ID());
        contentValues.put(LOCATION_NO, postingModel.getLOCATION_NO());
        contentValues.put(SOURCE_LOCATION, postingModel.getSOURCE_LOCATION());
        contentValues.put(SS_NAME, postingModel.getSS_NAME());
        contentValues.put(SS_ID, postingModel.getSS_ID());
        contentValues.put(SS_TYPE, postingModel.getSS_TYPE());
        contentValues.put(CREATED_BY, postingModel.getCREATED_BY());
        contentValues.put(WIDTH, postingModel.getWIDTH());
        contentValues.put(DEPTH, postingModel.getDEPTH());
        contentValues.put(UTILITY, postingModel.getUTILITY());
        contentValues.put(COUNT_CABLES, postingModel.getCOUNT_CABLES());
        contentValues.put(CABLE_33KV, postingModel.getCABLE_33KV());
        contentValues.put(CABLE_11KV, postingModel.getCABLE_11KV());
        contentValues.put(CABLE_LT, postingModel.getCABLE_LT());
        contentValues.put(LAT, postingModel.getLAT());
        contentValues.put(LON, postingModel.getLON());
        contentValues.put(VOLTAGE, postingModel.getVOLTAGE());
        contentValues.put(IS_FRESH_SURVEY, postingModel.getIS_FRESH_SURVEY());
        contentValues.put(FROM_LAT, postingModel.getFROM_LAT());
        contentValues.put(FROM_LON, postingModel.getFROM_LON());
        contentValues.put(LOCATION_TYPE, postingModel.getLOCATION_TYPE());
        contentValues.put(RMU_TYPE, postingModel.getRMU_TYPE());
        contentValues.put(DTR_CAPACITY, postingModel.getDTR_CAPACITY());
        contentValues.put(HT_SCNO, postingModel.getHT_SCNO());
        contentValues.put(UTILITY_TYPE, postingModel.getUTILITY_TYPE());
        contentValues.put(LOCATION_REMARKS, postingModel.getLOCATION_REMARKS());
        contentValues.put(CABLE_SIZE, postingModel.getCABLE_SIZE());
        contentValues.put(PROTECTION_PIPE, postingModel.getPROTECTION_PIPE());
        contentValues.put(DTR_CODE, postingModel.getDTR_CODE());
        contentValues.put(DTR_LOCATION, postingModel.getDTR_LOCATION());
        contentValues.put(REMARKS, postingModel.getREMARKS());
        contentValues.put(GPS_ACCURACY,postingModel.getGPS_ACCURACY());
        // Convert feedersData to JSON string
        if (postingModel.getFeedersData() != null && !postingModel.getFeedersData().isEmpty()) {
            String feedersDataJson = gson.toJson(postingModel.getFeedersData());
            contentValues.put(FEEDERS_DATA, feedersDataJson);
            System.out.println("Feeders Data JSON: " + feedersDataJson); // For debugging
        } else {
            contentValues.put(FEEDERS_DATA, "");
            System.out.println("No feeders data to save");
        }




        contentValues.put(FLAG, "N");

        // Insert the new record
        db.insert(RDSS_UG_TABLE_POSTING, null, contentValues);
        db.close();
    }

    public void updateRdssPostingDynamic(UGPostingModel postingModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // ✅ Only put non-null / non-empty values
        if (postingModel.getLOCATION_NO() != null)
            contentValues.put(LOCATION_NO, postingModel.getLOCATION_NO());
        if (postingModel.getSOURCE_LOCATION() != null)
            contentValues.put(SOURCE_LOCATION, postingModel.getSOURCE_LOCATION());
        if (postingModel.getSS_NAME() != null)
            contentValues.put(SS_NAME, postingModel.getSS_NAME());
        if (postingModel.getSS_ID() != null) contentValues.put(SS_ID, postingModel.getSS_ID());
        if (postingModel.getSS_TYPE() != null)
            contentValues.put(SS_TYPE, postingModel.getSS_TYPE());
        if (postingModel.getCREATED_BY() != null)
            contentValues.put(CREATED_BY, postingModel.getCREATED_BY());
        if (postingModel.getWIDTH() != null) contentValues.put(WIDTH, postingModel.getWIDTH());
        if (postingModel.getDEPTH() != null) contentValues.put(DEPTH, postingModel.getDEPTH());
        if (postingModel.getUTILITY() != null)
            contentValues.put(UTILITY, postingModel.getUTILITY());
        if (postingModel.getCOUNT_CABLES() != null)
            contentValues.put(COUNT_CABLES, postingModel.getCOUNT_CABLES());
        if (postingModel.getCABLE_33KV() != null)
            contentValues.put(CABLE_33KV, postingModel.getCABLE_33KV());
        if (postingModel.getCABLE_11KV() != null)
            contentValues.put(CABLE_11KV, postingModel.getCABLE_11KV());
        if (postingModel.getCABLE_LT() != null)
            contentValues.put(CABLE_LT, postingModel.getCABLE_LT());
        if (postingModel.getLAT() != null) contentValues.put(LAT, postingModel.getLAT());
        if (postingModel.getLON() != null) contentValues.put(LON, postingModel.getLON());
        if (postingModel.getVOLTAGE() != null)
            contentValues.put(VOLTAGE, postingModel.getVOLTAGE());
        if (postingModel.getIS_FRESH_SURVEY() != null)
            contentValues.put(IS_FRESH_SURVEY, postingModel.getIS_FRESH_SURVEY());
        if (postingModel.getFROM_LAT() != null)
            contentValues.put(FROM_LAT, postingModel.getFROM_LAT());
        if (postingModel.getFROM_LON() != null)
            contentValues.put(FROM_LON, postingModel.getFROM_LON());
        if (postingModel.getLOCATION_TYPE() != null)
            contentValues.put(LOCATION_TYPE, postingModel.getLOCATION_TYPE());
        if (postingModel.getRMU_TYPE() != null)
            contentValues.put(RMU_TYPE, postingModel.getRMU_TYPE());
        if (postingModel.getDTR_CAPACITY() != null)
            contentValues.put(DTR_CAPACITY, postingModel.getDTR_CAPACITY());
        if (postingModel.getHT_SCNO() != null)
            contentValues.put(HT_SCNO, postingModel.getHT_SCNO());
        if (postingModel.getUTILITY_TYPE() != null)
            contentValues.put(UTILITY_TYPE, postingModel.getUTILITY_TYPE());
        if (postingModel.getLOCATION_REMARKS() != null)
            contentValues.put(LOCATION_REMARKS, postingModel.getLOCATION_REMARKS());

        // ✅ Feeders data — only if not null/empty
        if (postingModel.getFeedersData() != null && !postingModel.getFeedersData().isEmpty()) {
            String feedersDataJson = gson.toJson(postingModel.getFeedersData());
            contentValues.put(FEEDERS_DATA, feedersDataJson);
            System.out.println("Feeders Data JSON (Update): " + feedersDataJson);
        }
        if (postingModel.getCABLE_SIZE() != null)
            contentValues.put(CABLE_SIZE, postingModel.getCABLE_SIZE());


        // Optionally track modified record
        contentValues.put(FLAG, "U");

        // ✅ Only proceed if there’s something to update
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!contentValues.isEmpty()) {
                int rowsAffected = db.update(
                        RDSS_UG_TABLE_POSTING,
                        contentValues,
                        LOCATION_ID + " = ?",
                        new String[]{postingModel.getLOCATION_ID()}
                );

                if (rowsAffected > 0) {
                    System.out.println("✅ Updated fields for LOCATION_ID: " + postingModel.getLOCATION_ID());
                } else {
                    System.out.println("⚠️ No record found for LOCATION_ID: " + postingModel.getLOCATION_ID());
                }
            } else {
                System.out.println("⚠️ No fields provided to update for LOCATION_ID: " + postingModel.getLOCATION_ID());
            }
        }

        db.close();
    }


    public UGPostingModel getLastPosting() {
        SQLiteDatabase db = this.getReadableDatabase();
        UGPostingModel postingModel = null;

        Cursor cursor = db.query(
                RDSS_UG_TABLE_POSTING,
                null, // all columns
                null,
                null,
                null,
                null,
                "rowid DESC", // sort by latest entry
                "1" // limit 1
        );

        if (cursor != null && cursor.moveToFirst()) {
            postingModel = new UGPostingModel();
            postingModel.setLOCATION_ID(cursor.getString(cursor.getColumnIndexOrThrow(LOCATION_ID)));
            postingModel.setLOCATION_NO(cursor.getString(cursor.getColumnIndexOrThrow(LOCATION_NO)));
            postingModel.setSOURCE_LOCATION(cursor.getString(cursor.getColumnIndexOrThrow(SOURCE_LOCATION)));
            postingModel.setSS_NAME(cursor.getString(cursor.getColumnIndexOrThrow(SS_NAME)));
            postingModel.setSS_ID(cursor.getString(cursor.getColumnIndexOrThrow(SS_ID)));
            postingModel.setSS_TYPE(cursor.getString(cursor.getColumnIndexOrThrow(SS_TYPE)));
            postingModel.setCREATED_BY(cursor.getString(cursor.getColumnIndexOrThrow(CREATED_BY)));
            postingModel.setWIDTH(cursor.getString(cursor.getColumnIndexOrThrow(WIDTH)));
            postingModel.setDEPTH(cursor.getString(cursor.getColumnIndexOrThrow(DEPTH)));
            postingModel.setUTILITY(cursor.getString(cursor.getColumnIndexOrThrow(UTILITY)));
            postingModel.setCOUNT_CABLES(cursor.getString(cursor.getColumnIndexOrThrow(COUNT_CABLES)));
            postingModel.setCABLE_33KV(cursor.getString(cursor.getColumnIndexOrThrow(CABLE_33KV)));
            postingModel.setCABLE_11KV(cursor.getString(cursor.getColumnIndexOrThrow(CABLE_11KV)));
            postingModel.setCABLE_LT(cursor.getString(cursor.getColumnIndexOrThrow(CABLE_LT)));
            postingModel.setLAT(cursor.getString(cursor.getColumnIndexOrThrow(LAT)));
            postingModel.setLON(cursor.getString(cursor.getColumnIndexOrThrow(LON)));
            postingModel.setVOLTAGE(cursor.getString(cursor.getColumnIndexOrThrow(VOLTAGE)));
            postingModel.setIS_FRESH_SURVEY(cursor.getString(cursor.getColumnIndexOrThrow(IS_FRESH_SURVEY)));
            postingModel.setFROM_LAT(cursor.getString(cursor.getColumnIndexOrThrow(FROM_LAT)));
            postingModel.setFROM_LON(cursor.getString(cursor.getColumnIndexOrThrow(FROM_LON)));
            postingModel.setLOCATION_TYPE(cursor.getString(cursor.getColumnIndexOrThrow(LOCATION_TYPE)));
            postingModel.setRMU_TYPE(cursor.getString(cursor.getColumnIndexOrThrow(RMU_TYPE)));
            postingModel.setDTR_CAPACITY(cursor.getString(cursor.getColumnIndexOrThrow(DTR_CAPACITY)));
            postingModel.setHT_SCNO(cursor.getString(cursor.getColumnIndexOrThrow(HT_SCNO)));
            postingModel.setUTILITY_TYPE(cursor.getString(cursor.getColumnIndexOrThrow(UTILITY_TYPE)));
            postingModel.setLOCATION_REMARKS(cursor.getString(cursor.getColumnIndexOrThrow(LOCATION_REMARKS)));

            String feedersDataJson = cursor.getString(cursor.getColumnIndexOrThrow(FEEDERS_DATA));
            if (feedersDataJson != null && !feedersDataJson.isEmpty()) {
                Type listType = new TypeToken<List<FeedersData>>() {
                }.getType();
                ArrayList<FeedersData> feedersData = gson.fromJson(feedersDataJson, listType);
                postingModel.setFeedersData(feedersData);
            }
            postingModel.setCABLE_SIZE(cursor.getString(cursor.getColumnIndexOrThrow(CABLE_SIZE)));
            postingModel.setPROTECTION_PIPE(cursor.getString(cursor.getColumnIndexOrThrow(PROTECTION_PIPE)));
            cursor.close();
        }

        db.close();
        return postingModel;
    }


    //NEW CODE
    public ArrayList<UGPostingModel> getUGPostingData() {
        ArrayList<UGPostingModel> data = new ArrayList<>();
        String query = "SELECT * FROM RDSS_UG_TABLE_POSTING";
        SQLiteDatabase db = this.getWritableDatabase();
        Gson gson = new Gson();

        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    UGPostingModel model = new UGPostingModel();

                    // Use column names instead of hardcoded indices
                    model.setLOCATION_ID(getStringFromCursor(cursor, LOCATION_ID));
                    model.setLOCATION_NO(getStringFromCursor(cursor, LOCATION_NO));
                    model.setSOURCE_LOCATION(getStringFromCursor(cursor, SOURCE_LOCATION));
                    model.setSS_NAME(getStringFromCursor(cursor, SS_NAME));
                    model.setSS_ID(getStringFromCursor(cursor, SS_ID));
                    model.setSS_TYPE(getStringFromCursor(cursor, SS_TYPE));
                    model.setCREATED_BY(getStringFromCursor(cursor, CREATED_BY));
                    model.setWIDTH(getStringFromCursor(cursor, WIDTH));
                    model.setDEPTH(getStringFromCursor(cursor, DEPTH));
                    model.setUTILITY(getStringFromCursor(cursor, UTILITY));
                    model.setCOUNT_CABLES(getStringFromCursor(cursor, COUNT_CABLES));
                    model.setCABLE_33KV(getStringFromCursor(cursor, CABLE_33KV));
                    model.setCABLE_11KV(getStringFromCursor(cursor, CABLE_11KV));
                    model.setCABLE_LT(getStringFromCursor(cursor, CABLE_LT));
                    model.setLAT(getStringFromCursor(cursor, LAT));
                    model.setLON(getStringFromCursor(cursor, LON));
                    model.setVOLTAGE(getStringFromCursor(cursor, VOLTAGE));
                    model.setIS_FRESH_SURVEY(getStringFromCursor(cursor, IS_FRESH_SURVEY));
                    model.setFROM_LAT(getStringFromCursor(cursor, FROM_LAT));
                    model.setFROM_LON(getStringFromCursor(cursor, FROM_LON));
                    model.setLOCATION_TYPE(getStringFromCursor(cursor, LOCATION_TYPE));
                    model.setRMU_TYPE(getStringFromCursor(cursor, RMU_TYPE));
                    model.setDTR_CAPACITY(getStringFromCursor(cursor, DTR_CAPACITY));
                    model.setHT_SCNO(getStringFromCursor(cursor, HT_SCNO));
                    model.setUTILITY_TYPE(getStringFromCursor(cursor, UTILITY_TYPE));
                    model.setLOCATION_REMARKS(getStringFromCursor(cursor, LOCATION_REMARKS));

                    // Handle FEEDERS_DATA
                    String feedersDataJson = getStringFromCursor(cursor, FEEDERS_DATA);
                    if (feedersDataJson != null && !feedersDataJson.trim().isEmpty()) {
                        try {
                            Type listType = new TypeToken<ArrayList<FeedersData>>() {
                            }.getType();
                            ArrayList<FeedersData> feedersData = gson.fromJson(feedersDataJson, listType);
                            model.setFeedersData(feedersData);
                        } catch (Exception e) {
                            e.printStackTrace();
                            model.setFeedersData(new ArrayList<FeedersData>());
                        }
                    } else {
                        model.setFeedersData(new ArrayList<FeedersData>());
                    }
                    model.setCABLE_SIZE(getStringFromCursor(cursor, CABLE_SIZE));
                    model.setPROTECTION_PIPE(getStringFromCursor(cursor, PROTECTION_PIPE));
                    model.setGPS_ACCURACY(getStringFromCursor(cursor, GPS_ACCURACY));

                    data.add(model);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return data;
    }

    // Helper method to safely get string from cursor
    private String getStringFromCursor(Cursor cursor, String columnName) {
        try {
            int columnIndex = cursor.getColumnIndex(columnName);
            if (columnIndex != -1) {
                return cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public boolean deletePmiPostingData() {
        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.delete(RDSS_UG_TABLE_POSTING, null, null);
        //System.out.println("DELETE RESULT : " + result);
        return result > 0;

    }

    public void deleteSSData(String ssId) {
       /* String whereClause = POLE_NO_NEW + " = ? AND " + GIS_ID_NEW + " = ? AND " + SURVEY_TYPE_NEW + " = ?";
        String[] whereArgs = new String[]{poleNumber, gis_id, surveyType};*/

        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = SS_ID + " = ? AND " + IS_FRESH_SURVEY + " = ?";
        String[] whereArgs = new String[]{ssId, "N"};
        int rowsAffected = db.delete(RDSS_UG_TABLE_POSTING, whereClause, whereArgs);
        System.out.println("ROWS DELETED " + rowsAffected);
        db.close();


       /* SQLiteDatabase database = this.getWritableDatabase();
        int result = database.delete(RDSS_UG_TABLE_POSTING, null, null);
        //System.out.println("DELETE RESULT : " + result);
        return result > 0;*/

    }

    public ArrayList<UGPostingModel> getUGPostingDataByDistance(double currentLat, double currentLon, String ssId) {
        ArrayList<UGPostingModel> nearbyList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM " + RDSS_UG_TABLE_POSTING +
                    " WHERE LAT IS NOT NULL AND LON IS NOT NULL AND " + SS_ID + " = ?";
            cursor = db.rawQuery(query, new String[]{ssId});

            if (cursor.moveToFirst()) {
                do {
                    String latStr = cursor.getString(cursor.getColumnIndexOrThrow(LAT));
                    String lonStr = cursor.getString(cursor.getColumnIndexOrThrow(LON));

                    if (latStr != null && lonStr != null && !latStr.isEmpty() && !lonStr.isEmpty()) {
                        double distance = getDistanceBetweenPoles(
                                latStr,
                                lonStr,
                                String.valueOf(currentLat),
                                String.valueOf(currentLon)
                        );

                        // Filter: within 500 meters
                        if (distance <= 500) {
                            UGPostingModel model = new UGPostingModel();

                            model.setLOCATION_ID(getStringFromCursor(cursor, LOCATION_ID));
                            model.setLOCATION_NO(getStringFromCursor(cursor, LOCATION_NO));
                            model.setSOURCE_LOCATION(getStringFromCursor(cursor, SOURCE_LOCATION));
                            model.setSS_NAME(getStringFromCursor(cursor, SS_NAME));
                            model.setSS_ID(getStringFromCursor(cursor, SS_ID));
                            model.setSS_TYPE(getStringFromCursor(cursor, SS_TYPE));
                            model.setCREATED_BY(getStringFromCursor(cursor, CREATED_BY));
                            model.setWIDTH(getStringFromCursor(cursor, WIDTH));
                            model.setDEPTH(getStringFromCursor(cursor, DEPTH));
                            model.setUTILITY(getStringFromCursor(cursor, UTILITY));
                            model.setCOUNT_CABLES(getStringFromCursor(cursor, COUNT_CABLES));
                            model.setCABLE_33KV(getStringFromCursor(cursor, CABLE_33KV));
                            model.setCABLE_11KV(getStringFromCursor(cursor, CABLE_11KV));
                            model.setCABLE_LT(getStringFromCursor(cursor, CABLE_LT));
                            model.setLAT(latStr);
                            model.setLON(lonStr);
                            model.setVOLTAGE(getStringFromCursor(cursor, VOLTAGE));
                            model.setIS_FRESH_SURVEY(getStringFromCursor(cursor, IS_FRESH_SURVEY));
                            model.setFROM_LAT(getStringFromCursor(cursor, FROM_LAT));
                            model.setFROM_LON(getStringFromCursor(cursor, FROM_LON));
                            model.setLOCATION_TYPE(getStringFromCursor(cursor, LOCATION_TYPE));
                            model.setRMU_TYPE(getStringFromCursor(cursor, RMU_TYPE));
                            model.setDTR_CAPACITY(getStringFromCursor(cursor, DTR_CAPACITY));
                            model.setHT_SCNO(getStringFromCursor(cursor, HT_SCNO));
                            model.setUTILITY_TYPE(getStringFromCursor(cursor, UTILITY_TYPE));
                            model.setLOCATION_REMARKS(getStringFromCursor(cursor, LOCATION_REMARKS));


                            String feedersDataJson = getStringFromCursor(cursor, FEEDERS_DATA);
                            if (feedersDataJson != null && !feedersDataJson.trim().isEmpty()) {
                                try {
                                    Type listType = new TypeToken<ArrayList<FeedersData>>() {
                                    }.getType();
                                    ArrayList<FeedersData> feedersData = gson.fromJson(feedersDataJson, listType);
                                    model.setFeedersData(feedersData);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    model.setFeedersData(new ArrayList<>());
                                }
                            } else {
                                model.setFeedersData(new ArrayList<>());
                            }
                            model.setCABLE_SIZE(getStringFromCursor(cursor,CABLE_SIZE));
                            model.setPROTECTION_PIPE(getStringFromCursor(cursor, PROTECTION_PIPE));

                            nearbyList.add(model);
                        }
                    }
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
            if (db != null && db.isOpen()) db.close();
        }

        return nearbyList;
    }


    public ArrayList<UGPostingModel> getUGPostingDataByLocationNoAndSsId(String locationNo, String ssId) {
        ArrayList<UGPostingModel> postingList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM " + RDSS_UG_TABLE_POSTING +
                    " WHERE " + LOCATION_NO + " = ? AND " + SS_ID + " = ?";
            cursor = db.rawQuery(query, new String[]{locationNo, ssId});

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    UGPostingModel model = new UGPostingModel();

                    model.setLOCATION_ID(getStringFromCursor(cursor, LOCATION_ID));
                    model.setLOCATION_NO(getStringFromCursor(cursor, LOCATION_NO));
                    model.setSOURCE_LOCATION(getStringFromCursor(cursor, SOURCE_LOCATION));
                    model.setSS_NAME(getStringFromCursor(cursor, SS_NAME));
                    model.setSS_ID(getStringFromCursor(cursor, SS_ID));
                    model.setSS_TYPE(getStringFromCursor(cursor, SS_TYPE));
                    model.setCREATED_BY(getStringFromCursor(cursor, CREATED_BY));
                    model.setWIDTH(getStringFromCursor(cursor, WIDTH));
                    model.setDEPTH(getStringFromCursor(cursor, DEPTH));
                    model.setUTILITY(getStringFromCursor(cursor, UTILITY));
                    model.setCOUNT_CABLES(getStringFromCursor(cursor, COUNT_CABLES));
                    model.setCABLE_33KV(getStringFromCursor(cursor, CABLE_33KV));
                    model.setCABLE_11KV(getStringFromCursor(cursor, CABLE_11KV));
                    model.setCABLE_LT(getStringFromCursor(cursor, CABLE_LT));
                    model.setLAT(getStringFromCursor(cursor, LAT));
                    model.setLON(getStringFromCursor(cursor, LON));
                    model.setVOLTAGE(getStringFromCursor(cursor, VOLTAGE));
                    model.setIS_FRESH_SURVEY(getStringFromCursor(cursor, IS_FRESH_SURVEY));
                    model.setFROM_LAT(getStringFromCursor(cursor, FROM_LAT));
                    model.setFROM_LON(getStringFromCursor(cursor, FROM_LON));
                    model.setLOCATION_TYPE(getStringFromCursor(cursor, LOCATION_TYPE));
                    model.setRMU_TYPE(getStringFromCursor(cursor, RMU_TYPE));
                    model.setDTR_CAPACITY(getStringFromCursor(cursor, DTR_CAPACITY));
                    model.setHT_SCNO(getStringFromCursor(cursor, HT_SCNO));
                    model.setUTILITY_TYPE(getStringFromCursor(cursor, UTILITY_TYPE));
                    model.setLOCATION_REMARKS(getStringFromCursor(cursor, LOCATION_REMARKS));

                    // Parse FEEDERS_DATA JSON
                    String feedersDataJson = getStringFromCursor(cursor, FEEDERS_DATA);
                    if (feedersDataJson != null && !feedersDataJson.trim().isEmpty()) {
                        try {
                            Type listType = new TypeToken<ArrayList<FeedersData>>() {
                            }.getType();
                            ArrayList<FeedersData> feedersData = gson.fromJson(feedersDataJson, listType);
                            model.setFeedersData(feedersData);
                        } catch (Exception e) {
                            e.printStackTrace();
                            model.setFeedersData(new ArrayList<>());
                        }
                    } else {
                        model.setFeedersData(new ArrayList<>());
                    }
                    model.setCABLE_SIZE(getStringFromCursor(cursor,CABLE_SIZE));
                    model.setPROTECTION_PIPE(getStringFromCursor(cursor, PROTECTION_PIPE));

                    postingList.add(model);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
            if (db != null && db.isOpen()) db.close();
        }

        return postingList;
    }


    public ArrayList<UGPostingModel> getUGPostingDataWithId(String ssId, String createdBy) {
        ArrayList<UGPostingModel> data = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Gson gson = new Gson();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM " + RDSS_UG_TABLE_POSTING + " WHERE " + SS_ID + " = ? AND " + CREATED_BY + " = ?";
            cursor = db.rawQuery(query, new String[]{ssId, createdBy});

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    UGPostingModel model = new UGPostingModel();

                    // Use column names instead of hardcoded indices
                    model.setLOCATION_ID(getStringFromCursor(cursor, LOCATION_ID));
                    model.setLOCATION_NO(getStringFromCursor(cursor, LOCATION_NO));
                    model.setSOURCE_LOCATION(getStringFromCursor(cursor, SOURCE_LOCATION));
                    model.setSS_NAME(getStringFromCursor(cursor, SS_NAME));
                    model.setSS_ID(getStringFromCursor(cursor, SS_ID));
                    model.setSS_TYPE(getStringFromCursor(cursor, SS_TYPE));
                    model.setCREATED_BY(getStringFromCursor(cursor, CREATED_BY));
                    model.setWIDTH(getStringFromCursor(cursor, WIDTH));
                    model.setDEPTH(getStringFromCursor(cursor, DEPTH));
                    model.setUTILITY(getStringFromCursor(cursor, UTILITY));
                    model.setCOUNT_CABLES(getStringFromCursor(cursor, COUNT_CABLES));
                    model.setCABLE_33KV(getStringFromCursor(cursor, CABLE_33KV));
                    model.setCABLE_11KV(getStringFromCursor(cursor, CABLE_11KV));
                    model.setCABLE_LT(getStringFromCursor(cursor, CABLE_LT));
                    model.setLAT(getStringFromCursor(cursor, LAT));
                    model.setLON(getStringFromCursor(cursor, LON));
                    model.setVOLTAGE(getStringFromCursor(cursor, VOLTAGE));
                    model.setIS_FRESH_SURVEY(getStringFromCursor(cursor, IS_FRESH_SURVEY));
                    model.setFROM_LAT(getStringFromCursor(cursor, FROM_LAT));
                    model.setFROM_LON(getStringFromCursor(cursor, FROM_LON));
                    model.setLOCATION_TYPE(getStringFromCursor(cursor, LOCATION_TYPE));
                    model.setRMU_TYPE(getStringFromCursor(cursor, RMU_TYPE));
                    model.setDTR_CAPACITY(getStringFromCursor(cursor, DTR_CAPACITY));
                    model.setHT_SCNO(getStringFromCursor(cursor, HT_SCNO));
                    model.setUTILITY_TYPE(getStringFromCursor(cursor, UTILITY_TYPE));
                    model.setLOCATION_REMARKS(getStringFromCursor(cursor, LOCATION_REMARKS));

                    // Handle FEEDERS_DATA
                    String feedersDataJson = getStringFromCursor(cursor, FEEDERS_DATA);
                    if (feedersDataJson != null && !feedersDataJson.trim().isEmpty()) {
                        try {
                            Type listType = new TypeToken<ArrayList<FeedersData>>() {
                            }.getType();
                            ArrayList<FeedersData> feedersData = gson.fromJson(feedersDataJson, listType);
                            model.setFeedersData(feedersData);
                        } catch (Exception e) {
                            e.printStackTrace();
                            model.setFeedersData(new ArrayList<>());
                        }
                    } else {
                        model.setFeedersData(new ArrayList<>());
                    }
                    model.setCABLE_SIZE(getStringFromCursor(cursor,CABLE_SIZE));
                    model.setPROTECTION_PIPE(getStringFromCursor(cursor, PROTECTION_PIPE));

                    data.add(model);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
            if (db != null && db.isOpen()) db.close();
        }

        return data;
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

    public void clearData() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // Clear only RDSS_UG_TABLE_POSTING (specific to this DB)
            db.execSQL("DELETE FROM " + RDSS_UG_TABLE_POSTING);

            // If you also want to reset AUTOINCREMENT (if you had INTEGER PRIMARY KEY AUTOINCREMENT)
            // db.execSQL("DELETE FROM sqlite_sequence WHERE name='" + RDSS_UG_TABLE_POSTING + "'");

            System.out.println("All data cleared from RDSS_UG_TABLE_POSTING");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }


}
