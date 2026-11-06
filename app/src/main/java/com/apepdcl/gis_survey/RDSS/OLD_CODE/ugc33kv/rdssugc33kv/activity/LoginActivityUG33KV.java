package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.activity;

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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;


import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.ArcGISDatabaseUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.Constants.MobileConstantsUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.retrofitservice.RetrofitClientInstanceUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.retrofitservice.RetrofitServiceUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.runtimeperimissions.AppDataUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.runtimeperimissions.UtilitesUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.utilits.CommanUtilitsUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.utilits.ConnectivityCheckingUG33KV;

import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.LoginRequestVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.LoginResponseVoUG33KV;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class LoginActivityUG33KV extends Activity implements View.OnClickListener {
    private ProgressDialog progressDialog;
    private TextInputEditText login_username,login_password;
    private LinearLayout lyt_login;
    private RetrofitServiceUG33KV retrofitServiceUG33KV;
    // private ProgressDialog progressDialog;
    private Button btn_login;
    private ArcGISDatabaseUG33KV arcGISDatabase;
    private List<LoginDataResponse> loginDataResponseUG33KVList;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String sessionId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_ugc_33kv);
        try {
            // progressDialog = CommanUtilits.progressDialog(this);
            login_username = (TextInputEditText) findViewById(R.id.login_username);
            login_password = (TextInputEditText) findViewById(R.id.login_password);
            btn_login = (Button) findViewById(R.id.btn_login);
            btn_login.setOnClickListener(this);
            progressDialog = new ProgressDialog(LoginActivityUG33KV.this);
            progressDialog.setMessage("Please Wail...");
            progressDialog.setCancelable(false);
            arcGISDatabase = new ArcGISDatabaseUG33KV(LoginActivityUG33KV.this);

            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            editor = preferences.edit();
            if (Build.VERSION.SDK_INT >= 23 && !AppDataUG33KV.getInstance().isPermission()) {
                UtilitesUG33KV.isStoragePermissionGranted(LoginActivityUG33KV.this);
            }

            loginDataResponseUG33KVList = arcGISDatabase.getUserMasterData();
            sessionId = preferences.getString(MobileConstantsUG33KV.SESSION_ID_GIS, "");
            if(loginDataResponseUG33KVList.size()>0) {
                if (sessionId != null && !sessionId.isEmpty()) {
                   // Toast.makeText(LoginActivity.this, "Test===>", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivityUG33KV.this, FeederMasterActivityUG33KV.class);
                    startActivity(intent);
                    finish();
                }
            }
            //  lyt_login=(LinearLayout)findViewById(R.id.lyt_login);
            //  lyt_login.setOnClickListener(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.btn_login:
                    if (Build.VERSION.SDK_INT >= 23) {

                        if (AppDataUG33KV.getInstance().isPermission()) {
                            usrLogin();
                        } else {
                            if (Build.VERSION.SDK_INT >= 23 && !AppDataUG33KV.getInstance().isPermission()) {
                                UtilitesUG33KV.isStoragePermissionGranted(LoginActivityUG33KV.this);

                            }
                        }

                    } else {
                        usrLogin();
                    }


                    break;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void usrLogin(){
        try {
            if (ConnectivityCheckingUG33KV.checkConnection(LoginActivityUG33KV.this)) {
                if (login_username.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter UserName", Toast.LENGTH_LONG).show();
                } else if (login_password.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter Password", Toast.LENGTH_LONG).show();
                } else {
                    progressDialog.show();
                    LoginRequestVoUG33KV loginRequestVoUG33KV = new LoginRequestVoUG33KV();
                    loginRequestVoUG33KV.setUSER_ID(login_username.getText().toString().trim());
                    loginRequestVoUG33KV.setPASSWORD(login_password.getText().toString().trim());
                    loginRequestVoUG33KV.setIMEI_NO(CommanUtilitsUG33KV.getIMEI_NO(LoginActivityUG33KV.this));
                    loginRequestVoUG33KV.setVERSION(MobileConstantsUG33KV.APP_VERSION);
                    Gson gson = new Gson();
                    String json = gson.toJson(loginRequestVoUG33KV);

                     Log.v("DATA===>>", "" + json);
                    retrofitServiceUG33KV = RetrofitClientInstanceUG33KV.getRetrofitInstance().create(RetrofitServiceUG33KV.class);
                    Call<LoginResponseVoUG33KV> responseVoCall = retrofitServiceUG33KV.getLoginData(loginRequestVoUG33KV);
                    responseVoCall.enqueue(new Callback<LoginResponseVoUG33KV>() {
                        @Override
                        public void onResponse(Call<LoginResponseVoUG33KV> call, Response<LoginResponseVoUG33KV> response) {
                            progressDialog.dismiss();
                            if (response.body() != null && response.isSuccessful()) {

                                LoginResponseVoUG33KV loginDataResponse = response.body();
                                Gson gson = new Gson();
                                String json = gson.toJson(loginDataResponse);

                                Log.v("DATA===>>", "" + json);
                                if ("TRUE".equalsIgnoreCase(loginDataResponse.getSTATUS())) {
                                    editor.putString(MobileConstantsUG33KV.USERNAME, login_username.getText().toString());
                                    editor.putString(MobileConstantsUG33KV.SESSION_ID_GIS, loginDataResponse.getSESSION_ID());
                                    editor.commit();
                                    LoginDataResponse dataResponse = loginDataResponse.getRESPONSE();
                                    arcGISDatabase.addUserMasterData(dataResponse);
                                    if (dataResponse.getSEC_CODE() != null && dataResponse.getSECTION_NAME() != null) {
                                        Intent intent = new Intent(LoginActivityUG33KV.this, FeederMasterActivityUG33KV.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivityUG33KV.this, "Please check user credentials", Toast.LENGTH_SHORT).show();
                                    }
                                } else {

                                    if (loginDataResponse.getAPK() != null) {
                                        if (loginDataResponse.getAPK().contains("http")) {
                                       /* ApkDowanloadInstal apkDowanloadInstal = new ApkDowanloadInstal(loginDataResponse.getAPK(), LoginActivity.this);
                                        apkDowanloadInstal.execute();*/
                                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivityUG33KV.this);
                                            alertDialogBuilder.setTitle("A new Version is available!");
                                            alertDialogBuilder
                                                    .setMessage("Before upgrade,please transmit the data to server ")
                                                    .setCancelable(false)
                                                    .setPositiveButton("Download",
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    Uri uri = Uri.parse(MobileConstantsUG33KV.APK_DOWNLOAD_LINK);
                                                                    Intent intent= new Intent(Intent.ACTION_VIEW,uri);
                                                                    startActivity(intent);
                                                                }
                                                            })

                                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();

                                                        }
                                                    });
                                            AlertDialog alertDialog = alertDialogBuilder.create();
                                            alertDialog.show();
                                        }
                                    } else {
                                        Toast.makeText(LoginActivityUG33KV.this, loginDataResponse.getERR_MSG(), Toast.LENGTH_SHORT).show();

                                    }
                                }

                            } else {
                                Toast.makeText(LoginActivityUG33KV.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponseVoUG33KV> call, Throwable t) {
                            progressDialog.dismiss();
                            //  progressDialog.dismiss();
                            Toast.makeText(LoginActivityUG33KV.this, "Try Again", Toast.LENGTH_LONG).show();
                        }
                    });
                }

            } else {
                Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onRequestPermissionsResult ( int requestCode, String[] permissions,
                                             int[] grantResults){
        try {
            if (grantResults.length > 0
                     && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED
                    && grantResults[3] == PackageManager.PERMISSION_GRANTED
                    && grantResults[4] == PackageManager.PERMISSION_GRANTED) {
                AppDataUG33KV.getInstance().setPermission(true);
            } else {
                AppDataUG33KV.getInstance().setPermission(false);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
