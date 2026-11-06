package com.apepdcl.gis_survey.utilits;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.apepdcl.gis.app.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.WeakHashMap;

public class CommanUtilits {
    public static ProgressDialog progressDialog(Context context) {
        ProgressDialog prgDialog = new ProgressDialog(context);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        return prgDialog;
    }

    public static String getIMEI_NO(Context context) {
        String uid = null;
        try {
            TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= 23) {
                //   if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                //  return false;
                uid = tManager.getDeviceId();
              /*  }else {
                    uid="" ;
                }*/
            } else if (Build.VERSION.SDK_INT >= 26) {
                uid = tManager.getImei();
            } else {
                uid = tManager.getDeviceId();
            }
            //  uid = tManager.getDeviceId();
        } catch (SecurityException e) {
            e.printStackTrace();
            uid = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return uid;
    }

    public static String isChecking(String tempcond) {

        String tempcond1 = null;
        try {
            tempcond1 = tempcond == null || tempcond.equals("null") || tempcond.equals("") ? "" : tempcond.toUpperCase();

            return tempcond1;
        } catch (Exception e) {
            tempcond1 = "";
            return tempcond1;
        }
    }

    private static final Map<Context, Boolean> dialogVisibilityMap = new WeakHashMap<>();

    public static void internetDialog(Context context) {
        if (dialogVisibilityMap.containsKey(context) && Boolean.TRUE.equals(dialogVisibilityMap.get(context))) {
            // Dialog is already showing for this context
            return;
        }

        dialogVisibilityMap.put(context, true);

        Dialog dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.no_internet_dialog, null);
        dialog.setContentView(view);

        TextView title = view.findViewById(R.id.title);
        TextView message = view.findViewById(R.id.message);
        TextView okBtn = view.findViewById(R.id.okBtn);

        title.setText("No Internet Connection");
        message.setText("Please check your internet connection and try again.");
        okBtn.setText("Okay");
        dialog.setCancelable(false);
        okBtn.setOnClickListener(v -> dialog.dismiss());


        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        dialog.show();

//        new AlertDialog.Builder(context)
//                .setTitle("No Internet Connection")
//                .setMessage("Please check your internet connection and try again.")
//                .setCancelable(false)
//                .setPositiveButton("OK", (dialog, which) -> {
//                    dialog.dismiss();
//                    dialogVisibilityMap.put(context, false);
//                })
//                .setOnDismissListener(dialog -> {
//                    dialogVisibilityMap.put(context, false);
//                })
//                .show();
    }


    public static void netWorkError(Context context) {
        Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show();
    }

    public static Uri compressImageOld(Context context, Uri imageUri) throws IOException {

        final long MAX_SIZE = 1 * 1024 * 1024; // 2 MB

        Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                context.getContentResolver(), imageUri);



        bitmap = rotateBitmapIfRequired(context, bitmap, imageUri);

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        File imageFile = getRealPathFromUri(context, imageUri);
        long fileSizeKB = imageFile.length() / 1024;
        Log.d("Image", "Actual Size: " + fileSizeKB +" KB");

        while (imageFile.length() > MAX_SIZE && width > 1000 && height > 1000) {

            width = (int) (width * 0.9);
            height = (int) (height * 0.9);

            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            int quality = 90;
            do {
                baos.reset();
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                quality -= 5;
            } while (baos.size() > MAX_SIZE && quality > 20);

            FileOutputStream fos = new FileOutputStream(imageFile);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        }

        bitmap.recycle();
        File imageFileR = getRealPathFromUri(context, Uri.fromFile(imageFile));
        long fileSizeKBR = imageFileR.length() / 1024;
        Log.d("Image", "Reduce Size: " + fileSizeKBR +" KB");
        return Uri.fromFile(imageFile);
    }
    public static Uri compressImage(Context context, Uri imageUri) throws IOException {

        final long MAX_SIZE = 150 * 1024; // 150 KB

        Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                context.getContentResolver(), imageUri
        );

        bitmap = rotateBitmapIfRequired(context, bitmap, imageUri);

        Log.d("Image", "Original Width: " + bitmap.getWidth());
        Log.d("Image", "Original Height: " + bitmap.getHeight());
        File imageFileA = getRealPathFromUri(context, imageUri);
        long fileSizeKBA = imageFileA.length() / 1024;
        Log.d("Image", "Actual Size: " + fileSizeKBA +" KB");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int quality = 90;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        while (true) {

            baos.reset();

            bitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    quality,
                    baos
            );

            long size = baos.size();



            // Image is within 150 KB
            if (size <= MAX_SIZE) {
                break;
            }

            // First reduce JPEG quality
            if (quality > 30) {
                quality -= 5;
            } else {
                // Quality is already low, so reduce dimensions
                width = (int) (width * 0.85);
                height = (int) (height * 0.85);

                if (width < 300 || height < 300) {
                    break;
                }

                bitmap = Bitmap.createScaledBitmap(
                        bitmap,
                        width,
                        height,
                        true
                );

                quality = 90;
            }
        }

        // Write compressed image
        File imageFile = getRealPathFromUri(context, imageUri);

        FileOutputStream fos = new FileOutputStream(imageFile);
        fos.write(baos.toByteArray());
        fos.flush();
        fos.close();

        long finalSizeKB = imageFile.length() / 1024;

        Log.d("Image", "Final Size: " + finalSizeKB + " KB");

        bitmap.recycle();

        return Uri.fromFile(imageFile);
    }


    public static File getRealPathFromUri(Context context, Uri uri) throws IOException {

        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        File file = new File(context.getCacheDir(), "MeterImage_" + System.currentTimeMillis() + ".jpg");
        FileOutputStream outputStream = new FileOutputStream(file);

        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        outputStream.flush();
        outputStream.close();
        inputStream.close();

        return file;
    }

    public static Bitmap rotateBitmapIfRequired(Context context, Bitmap bitmap, Uri uri) throws IOException {

        InputStream input = context.getContentResolver().openInputStream(uri);
        ExifInterface exif = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            exif = new ExifInterface(input);
        }

        int orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
        );

        Matrix matrix = new Matrix();

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.postRotate(90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.postRotate(180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.postRotate(270);
                break;

            default:
                return bitmap;
        }

        Bitmap rotated = Bitmap.createBitmap(
                bitmap,
                0,
                0,
                bitmap.getWidth(),
                bitmap.getHeight(),
                matrix,
                true
        );

        bitmap.recycle();

        return rotated;
    }


//    public static void internetDialog(Activity activity) {
//        if (activity == null || activity.isFinishing() || activity.isDestroyed()) return;
//
//        new AlertDialog.Builder(activity)
//                .setTitle("No Internet Connection")
//                .setMessage("Please check your internet connection and try again.")
//                .setCancelable(false)
//                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
//                .show();
//    }



}
