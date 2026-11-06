package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;

import androidx.core.content.FileProvider;

import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.constants.MobileConstantsRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.sqldatabase.ArcGISDatabaseRDSSUg11Kv;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class ApkDowanloadInstalRDSSUg11Kv extends AsyncTask<String, String, String> {
    private final String apkUrl;
    private static  Context activity = null;
    ProgressDialog progressDialog;
    private String resp;
    private String filepath;
    private static final int BUFFER_OFFSET = 0;
    private static final int BUFFER_SIZE = 1024;
    private static final int READ_FAILED = -1;
    private ArcGISDatabaseRDSSUg11Kv arcGISDatabaseRDSSUg11Kv;
    private SharedPreferences preferences;
    //private SessionManager sessionManager;

    public ApkDowanloadInstalRDSSUg11Kv(String url, Context activity) {
        this.apkUrl=url;
        this.activity=activity;

    }
    protected String doInBackground(String... params) {
        IOException e;
        publishProgress(new String[]{"Sleeping..."});
        try {
            URL url = new URL(apkUrl);
            URL url2;
            try {
                preferences = PreferenceManager.getDefaultSharedPreferences(activity);
                arcGISDatabaseRDSSUg11Kv =new ArcGISDatabaseRDSSUg11Kv(activity);
               // sessionManager=new SessionManager(activity);
                //sessionManager.clearData();
                arcGISDatabaseRDSSUg11Kv.clearData();
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(false);
                c.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
                c.setRequestProperty("Accept", "*/*");
                c.connect();
                String PATH = Environment.getExternalStorageDirectory() + "/download/";
                InputStream is = c.getInputStream();
                File outputFile = new File(activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), MobileConstantsRDSSUg11Kv.APK_NAME);
                if (outputFile.exists()) {
                    outputFile.delete();
                }
                outputFile.setWritable(true);
                filepath = outputFile.getPath();
                FileOutputStream fos1 = new FileOutputStream(outputFile, true);
                byte[] buffer = new byte[BUFFER_SIZE];
                while (true) {
                    int len1 = is.read(buffer);
                    if (len1 == READ_FAILED) {
                        break;
                    }
                    fos1.write(buffer, BUFFER_OFFSET, len1);
                }
                fos1.close();
                is.close();

                /*Intent intent = new Intent("android.intent.action.VIEW");
                intent.setDataAndType(Uri.fromFile(outputFile), "application/vnd.android.package-archive");
                activity.startActivity(intent);*/

               /* Intent install = new Intent(Intent.ACTION_VIEW);
                install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
// Old Approach
                install.setDataAndType(Uri.fromFile(outputFile), "application/vnd.android.package-archive");*/
// End Old approach
// New Approach
               /* Intent install = new Intent(Intent.ACTION_VIEW);
               // Uri apkURI = FileProvider.getUriForFile(activity, "com.epdcl.gissurvey.provider",outputFile);
                //Uri uri = FileProvider.getUriForFile(activity, "com.epdcl.gissurvey.fileprovider", outputFile);
                Uri apkUri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".fileprovider", outputFile);
                install = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                install.setData(apkUri);
                install.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);*/
               // install.setDataAndType(apkUri, "application/vnd.android.package-archive");
                //install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
// End New Approach
               // activity.startActivity(install);
                Intent install = new Intent(Intent.ACTION_VIEW);
                Uri downloaded_apk = FileProvider.getUriForFile(activity, "com.epdcl.gissurvey.provider"+ ".provider", outputFile);
                install.setDataAndType(downloaded_apk, "application/vnd.android.package-archive");
                install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                activity.startActivity(install);

                url2 = url;
            } catch (IOException e2) {
                e = e2;
                url2 = url;
                e.printStackTrace();
                return this.resp;
            }
        } catch (IOException e3) {
            e = e3;
            e.printStackTrace();
            return this.resp;
        }
        return this.resp;
    }

    protected void onPostExecute(String result) {
        this.progressDialog.dismiss();
    }

    protected void onPreExecute() {
         this.progressDialog = ProgressDialog.show(activity, "Version Update", "apk downloading please wait");
    }
}