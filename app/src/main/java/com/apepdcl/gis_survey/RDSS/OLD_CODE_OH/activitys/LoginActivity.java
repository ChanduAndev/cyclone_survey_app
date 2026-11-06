package com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.activitys;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.Constants.MobileConstants;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.retrofitservice.RetrofitClientInstance;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.retrofitservice.RetrofitService;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.runtimeperimissions.AppData;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.runtimeperimissions.Utilites;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.utilits.CommanUtilits;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.LoginDataResponse;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.LoginRequestVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.LoginResponseVo;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity implements View.OnClickListener {
    private ProgressDialog progressDialog;
    private TextInputEditText login_username, login_password;
    private RetrofitService retrofitService;
    private Button btn_login;
    private ArcGISDatabase arcGISDatabase;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String sessionId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity1_11kv_oh_rdss);
        try {
            progressDialog = CommanUtilits.progressDialog(this);
            login_username = (TextInputEditText) findViewById(R.id.login_username);
            login_password = (TextInputEditText) findViewById(R.id.login_password);
            btn_login = (Button) findViewById(R.id.btn_login);
            btn_login.setOnClickListener(this);
            arcGISDatabase = new ArcGISDatabase(LoginActivity.this);

            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            if (Build.VERSION.SDK_INT >= 23 && !AppData.getInstance().isPermission()) {
                Utilites.isStoragePermissionGranted(LoginActivity.this);
            }
            sessionId = preferences.getString(MobileConstants.SESSION_ID, "");
            if (sessionId != null && !sessionId.isEmpty()) {
                Intent intent = new Intent(LoginActivity.this, FeederMasterActivity.class);
                startActivity(intent);
                finish();
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
                        if (AppData.getInstance().isPermission()) {
                            usrLogin();
                        } else {
                            if (Build.VERSION.SDK_INT >= 23 && !AppData.getInstance().isPermission()) {
                                Utilites.isStoragePermissionGranted(LoginActivity.this);
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
            if (ConnectivityChecking.checkConnection(LoginActivity.this)) {
                if (login_username.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter UserName", Toast.LENGTH_LONG).show();
                } else if (login_password.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter Password", Toast.LENGTH_LONG).show();
                } else {
                    progressDialog.show();
                    LoginRequestVo loginRequestVo = new LoginRequestVo();
                    loginRequestVo.setUSER_ID(login_username.getText().toString().trim());
                    loginRequestVo.setPASSWORD(login_password.getText().toString().trim());
                    loginRequestVo.setIMEI_NO(CommanUtilits.getIMEI_NO(LoginActivity.this));
                    loginRequestVo.setVERSION(MobileConstants.APP_VERSION);
                    Gson gson = new Gson();
                    String json = gson.toJson(loginRequestVo);
                    Log.v("DATA===>>", "" + json);
                    retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
                    Call<LoginResponseVo> responseVoCall = retrofitService.getLoginData(loginRequestVo);
                    responseVoCall.enqueue(new Callback<LoginResponseVo>() {
                        @Override
                        public void onResponse(Call<LoginResponseVo> call, Response<LoginResponseVo> response) {
                            progressDialog.dismiss();
                            if (response.body() != null) {
                                LoginResponseVo loginDataResponse = response.body();
                                /*Gson gson = new Gson();
                                String json = gson.toJson(loginDataResponse);
                                Log.v("DATA===>>", "" + json);*/
                                if ("TRUE".equalsIgnoreCase(loginDataResponse.getSTATUS())) {
                                    editor = preferences.edit();
                                    editor.putString(MobileConstants.USERNAME, login_username.getText().toString());
                                    editor.putString(MobileConstants.SESSION_ID, loginDataResponse.getSESSION_ID());
                                    editor.commit();
                                    LoginDataResponse dataResponse = loginDataResponse.getRESPONSE();
                                   // arcGISDatabase.addUserMasterData(dataResponse);
                                    if (dataResponse.getSEC_CODE() != null && dataResponse.getSECTION_NAME() != null) {
                                        Intent intent = new Intent(LoginActivity.this, FeederMasterActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Please check user credentials", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    if (loginDataResponse.getAPK() != null) {
                                        if (loginDataResponse.getAPK().contains("http")) {
                               /* ApkDowanloadInstal apkDowanloadInstal = new ApkDowanloadInstal(response_vo.getAPK(), FeederMasterActivity.this);
                                apkDowanloadInstal.execute();*/
                                            String link1 = "Html.fromHtml(\"your message,<a href=\"http://www.google.com\">link</a>";
                                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);

                                            alertDialogBuilder.setTitle("A new Version is available!");
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
                                                                    String url = MobileConstants.APK_URL;
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
                                        Toast.makeText(LoginActivity.this, loginDataResponse.getERR_MSG(), Toast.LENGTH_SHORT).show();

                                    }
                                }

                            } else {
                                Toast.makeText(LoginActivity.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponseVo> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

            } else {
                Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
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
                AppData.getInstance().setPermission(true);
            } else {
                AppData.getInstance().setPermission(false);

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
