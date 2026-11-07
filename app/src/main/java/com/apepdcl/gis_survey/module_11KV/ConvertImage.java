package com.apepdcl.gis_survey.module_11KV;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;

/**
 * Created by Soubhagya Swain on 27,March,2024
 */
public class ConvertImage {

    public static String convertBase64(String filePath){
       // System.out.println("FILE PATH IN TRANSMIT : "+filePath);
        File imgFile = new File(filePath);
        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        ByteArrayOutputStream byteArrayOutputStream1 = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream1);
        byte[] byteofimages1 = byteArrayOutputStream1.toByteArray();
        return android.util.Base64.encodeToString(byteofimages1, Base64.DEFAULT);
    }

    public static String convertBase64New(String filePath) {

        if (filePath == null || filePath.trim().isEmpty()) {
            return "";
        }

        Bitmap bitmap = null;

        try {
            File imgFile = new File(filePath);

            if (!imgFile.exists()) {
                return "";
            }

            bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            if (bitmap == null) {
                return "";
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            bitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    80,
                    outputStream
            );

            byte[] imageBytes = outputStream.toByteArray();

            return Base64.encodeToString(
                    imageBytes,
                    Base64.NO_WRAP
            );

        } catch (Exception e) {
            e.printStackTrace();
            return "";

        } finally {
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }




    public static String convertBase64OpenCamera(Bitmap myBitmap){
        // System.out.println("FILE PATH IN TRANSMIT : "+filePath);
        ByteArrayOutputStream byteArrayOutputStream1 = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream1);
        byte[] byteofimages1 = byteArrayOutputStream1.toByteArray();
        return android.util.Base64.encodeToString(byteofimages1, Base64.DEFAULT);
    }



    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        Calendar date = Calendar.getInstance();
        long millisecondsDate = date.getTimeInMillis();
        String imageName = ""+millisecondsDate;
        Bitmap OutImage = Bitmap.createScaledBitmap(inImage, 1000, 1000,true);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), OutImage, imageName, null);
        return Uri.parse(path);
    }

    public static String getPathFromURI(Uri uri,Context context) {
        String path = "";
        if (context.getContentResolver() != null) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    public static String convertByteToBase64(byte[] byteOfImages){
        return android.util.Base64.encodeToString(byteOfImages, Base64.DEFAULT);
    }
    public static Bitmap getBitmapFromPath(String filePath){
        // System.out.println("FILE PATH IN TRANSMIT : "+filePath);
        File imgFile = new File(filePath);
        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        return myBitmap;
    }
}
