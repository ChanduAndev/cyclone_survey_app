package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.constants.MobileConstantsRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.retrofitservice.responsevo.RetrofitClientInstanceRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.retrofitservice.responsevo.RetrofitServiceRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.runtimepermessions.AppDataRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.runtimepermessions.ConnectivityCheckingRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.runtimepermessions.UtilitesRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.sqldatabase.ArcGISDatabaseRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.utilities.CommanUtilitsRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.LoginDataResponseRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.LoginRequestVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.LoginResponseVoRDSSUg11Kv;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRDSSUg11KvActivity extends Activity implements View.OnClickListener {
    private ProgressDialog progressDialog;
    private EditText login_username, login_password;
    private RetrofitServiceRDSSUg11Kv retrofitServiceRDSSUg11Kv;
    private Button btn_login;
    private ArcGISDatabaseRDSSUg11Kv arcGISDatabaseRDSSUg11Kv;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String sessionId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        try {
            progressDialog = CommanUtilitsRDSSUg11Kv.progressDialog(this);
            login_username =  findViewById(R.id.ed_user_id);
            login_password =  findViewById(R.id.ed_password);
            btn_login = (Button) findViewById(R.id.btn_login);
            btn_login.setOnClickListener(this);
            arcGISDatabaseRDSSUg11Kv = new ArcGISDatabaseRDSSUg11Kv(LoginRDSSUg11KvActivity.this);

            preferences = PreferenceManager.getDefaultSharedPreferences(LoginRDSSUg11KvActivity.this);

            if (Build.VERSION.SDK_INT >= 23 && !AppDataRDSSUg11Kv.getInstance().isPermission()) {
                UtilitesRDSSUg11Kv.isStoragePermissionGranted(LoginRDSSUg11KvActivity.this);
            }
            sessionId = preferences.getString(MobileConstantsRDSSUg11Kv.SESSION_ID, "");
            if (sessionId != null && !sessionId.isEmpty()) {
                if (preferences.getInt("DATABASE_VERSION",0) == ArcGISDatabaseRDSSUg11Kv.DATABASE_VERSION){
                    Intent intent = new Intent(LoginRDSSUg11KvActivity.this, FeederMasterRDSSUg11KvActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.btn_login:
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (AppDataRDSSUg11Kv.getInstance().isPermission()) {
                            usrLogin();
                        } else {
                            if (Build.VERSION.SDK_INT >= 23 && !AppDataRDSSUg11Kv.getInstance().isPermission()) {
                                UtilitesRDSSUg11Kv.isStoragePermissionGranted(LoginRDSSUg11KvActivity.this);
                            }
                        }
                    } else {
                        usrLogin();
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void usrLogin() {
        try {
            if (ConnectivityCheckingRDSSUg11Kv.checkConnection(LoginRDSSUg11KvActivity.this)) {
                if (login_username.getText().toString().isEmpty()) {
                    Toast.makeText(LoginRDSSUg11KvActivity.this, "Please Enter UserName", Toast.LENGTH_LONG).show();
                } else if (login_password.getText().toString().isEmpty()) {
                    Toast.makeText(LoginRDSSUg11KvActivity.this, "Please Enter Password", Toast.LENGTH_LONG).show();
                } else {
                    progressDialog.show();
                    LoginRequestVoRDSSUg11Kv loginRequestVoRDSSUg11Kv = new LoginRequestVoRDSSUg11Kv();
                    loginRequestVoRDSSUg11Kv.setUSER_ID(login_username.getText().toString().trim());
                    loginRequestVoRDSSUg11Kv.setPASSWORD(login_password.getText().toString().trim());
                    loginRequestVoRDSSUg11Kv.setIMEI_NO(CommanUtilitsRDSSUg11Kv.getIMEI_NO(LoginRDSSUg11KvActivity.this));
                    loginRequestVoRDSSUg11Kv.setVERSION(MobileConstantsRDSSUg11Kv.APP_VERSION);
                    Gson gson = new Gson();
                    String json = gson.toJson(loginRequestVoRDSSUg11Kv);
                    Log.v("DATA===>>", "" + json);
                    retrofitServiceRDSSUg11Kv = RetrofitClientInstanceRDSSUg11Kv.getRetrofitInstance().create(RetrofitServiceRDSSUg11Kv.class);
                    Call<LoginResponseVoRDSSUg11Kv> responseVoCall = retrofitServiceRDSSUg11Kv.getLoginData(loginRequestVoRDSSUg11Kv);
                    responseVoCall.enqueue(new Callback<LoginResponseVoRDSSUg11Kv>() {
                        @Override
                        public void onResponse(Call<LoginResponseVoRDSSUg11Kv> call, Response<LoginResponseVoRDSSUg11Kv> response) {
                            progressDialog.dismiss();
                            if (response.body() != null) {
                                LoginResponseVoRDSSUg11Kv loginDataResponse = response.body();
                                /*Gson gson = new Gson();
                                String json = gson.toJson(loginDataResponse);
                                Log.v("DATA===>>", "" + json);*/
                                if ("TRUE".equalsIgnoreCase(loginDataResponse.getSTATUS())) {
                                    editor = preferences.edit();
                                    editor.putString(MobileConstantsRDSSUg11Kv.USERNAME, login_username.getText().toString());
                                    editor.putString(MobileConstantsRDSSUg11Kv.SESSION_ID, loginDataResponse.getSESSION_ID());
                                    editor.putInt("DATABASE_VERSION", ArcGISDatabaseRDSSUg11Kv.DATABASE_VERSION);
                                    editor.apply();
                                    editor.commit();
                                    LoginDataResponseRDSSUg11Kv dataResponse = loginDataResponse.getRESPONSE();
                                   // arcGISDatabaseRDSSUg11Kv.addUserMasterData(dataResponse);
                                    if (dataResponse.getSEC_CODE() != null && dataResponse.getSECTION_NAME() != null) {
                                       // editor.putString(MobileConstants.SEC_CODE, dataResponse.getSEC_CODE());
                                        editor.commit();
                                        Intent intent = new Intent(LoginRDSSUg11KvActivity.this, FeederMasterRDSSUg11KvActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(LoginRDSSUg11KvActivity.this, "Please check user credentials", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    if (loginDataResponse.getAPK() != null) {
                                        if (loginDataResponse.getAPK().contains("http")) {
                               /* ApkDowanloadInstal apkDowanloadInstal = new ApkDowanloadInstal(response_vo.getAPK(), FeederMasterActivity.this);
                                apkDowanloadInstal.execute();*/
                                            String link1 = "Html.fromHtml(\"your message,<a href=\"http://www.google.com\">link</a>";
                                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginRDSSUg11KvActivity.this);

                                            alertDialogBuilder.setTitle("A New Version Is Available!");
                                            //  alertDialogBuilder.setTitle("A new Version is available!  \nPlease Click to Download latest app \n<a href=\\\"http://www.google.com\\\">Check this link out</a>"c);
                                            alertDialogBuilder

                                                    .setMessage(Html.fromHtml("Please Download latest app\n " +
                                                            ""))
                                                    //.setMessage("Before upgrade,please trasmit the data to server ")
                                                    /*  .setMessage("Before upgrade,please transmit the data to server \n " +
                                                              "Please Click to Download latest app \n "+link1+" Check this link out</a>")*/
                                                    .setCancelable(false)
                                                    .setPositiveButton("Download",
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    String url = MobileConstantsRDSSUg11Kv.APK_URL;
                                                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                                                    i.setData(Uri.parse(url));
                                                                    startActivity(i);

                                                                }
                                                            })

                                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();

                                                        }
                                                    });
                                            AlertDialog alertDialog = alertDialogBuilder.create();
                                            alertDialog.show();
                                            ((TextView)alertDialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
                                        }
                                    } else {
                                        Toast.makeText(LoginRDSSUg11KvActivity.this, loginDataResponse.getERR_MSG(), Toast.LENGTH_SHORT).show();

                                    }
                                }

                            } else {
                                Toast.makeText(LoginRDSSUg11KvActivity.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponseVoRDSSUg11Kv> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(LoginRDSSUg11KvActivity.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

            } else {
                CommanUtilits.internetDialog(LoginRDSSUg11KvActivity.this);
                //Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        try {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED
                    && grantResults[3] == PackageManager.PERMISSION_GRANTED
                    && grantResults[4] == PackageManager.PERMISSION_GRANTED) {
                AppDataRDSSUg11Kv.getInstance().setPermission(true);
            } else {
                AppDataRDSSUg11Kv.getInstance().setPermission(false);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* @Override
    public void onBackPressed() {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit");
        alertDialogBuilder
                .setMessage("Are you sure you want to Exit ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        System.exit(0);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }*/
}
