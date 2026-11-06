package com.apepdcl.gis_survey.activitys;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.InputType;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.apepdcl.gis_survey.CommonNames;
import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.DashBoard;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.METER_SURVEY.MeterConstants;
import com.apepdcl.gis_survey.METER_SURVEY.MeterSurveyHome;
import com.apepdcl.gis_survey.METER_SURVEY.PasswordUtil;
import com.apepdcl.gis_survey.METER_SURVEY.RetrofitClientInstanceMeter;
import com.apepdcl.gis_survey.METER_SURVEY.RetrofitServiceMeter;
import com.apepdcl.gis_survey.PMI.RetrofitClientInstancePMI;
import com.apepdcl.gis_survey.PMI.RetrofitServicePMI;
import com.apepdcl.gis_survey.PMI.new_screens.OTPRetrofitClient;
import com.apepdcl.gis_survey.PMI.new_screens.OTPRetrofitService;
import com.apepdcl.gis_survey.PMI.new_screens.PMINewHome;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.ArcGISDatabaseUG33KV;

import com.apepdcl.gis_survey.estimates.RetrofitClientInstanceEstimates;
import com.apepdcl.gis_survey.estimates.RetrofitServiceEstimates;
import com.apepdcl.gis_survey.estimates.new_module.DashboardRdss;
import com.apepdcl.gis_survey.estimates.sou.SurveyList;
import com.apepdcl.gis_survey.runtimeperimissions.AppData;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.runtimeperimissions.Utilites;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.apepdcl.gis_survey.vo.LoginRequestVo;
import com.apepdcl.gis_survey.vo.LoginResponseVo;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import okhttp3.internal.platform.Platform;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;

import java.util.HashMap;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText ed_user_id, ed_password;
    Spinner spinner_user_type;
    private TextView btn_login;
    RetrofitServiceEstimates retrofitService;
    RetrofitServicePMI retrofitClientInstancePMI;
    OTPRetrofitService otpRetrofitService;
    // private ProgressDialog progressDialog;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private ArcGISDatabase arcGISDatabase;
    private String sessionId, office_id;
    ProgressBar loader;

    public static boolean isJHC = false;
    public static boolean IS_33KV_USER = false;
    public static String SURVEY_FROM = "";

    private static final int PERMISSION_REQUEST_CODE = 100;

    TextView btn_login_bill_reader;
    ProgressBar loader_bill_reader;

    RetrofitServiceMeter retrofitServiceMeter;
    ProgressDialog progressDialog;

    //CheckBox checkBox_bill_reader;

    private static final String[] REQUIRED_PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE
    };

    private ImageView imgTogglePassword;
    private boolean isPasswordVisible = false;

    private TextView tabEmployees, tabOthers;

    private LinearLayout employeeLoginContainer, othersLoginContainer;
    private LinearLayout layoutOtp, layoutOtherDetails;

    private EditText edMobileNumber, edOtp;
    private EditText edOtherName, edOtherDesignation, edOtherOffice;

    private TextView btnSendOtp, btnVerifyOtp, btnSaveOther;

    private boolean isOtpVerified = false;
    private String verifiedMobileNumber = "";
    private String otpID = "";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            //setContentView(R.layout.activity_sign_in);
            setContentView(R.layout.new_login_screen);
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);


            tabEmployees = findViewById(R.id.tab_employees);
            tabOthers = findViewById(R.id.tab_others);

            employeeLoginContainer = findViewById(R.id.employeeLoginContainer);
            othersLoginContainer = findViewById(R.id.othersLoginContainer);

            layoutOtp = findViewById(R.id.layout_otp);
            layoutOtherDetails = findViewById(R.id.layout_other_details);

            edMobileNumber = findViewById(R.id.ed_mobile_number);
            edOtp = findViewById(R.id.ed_otp);

            edOtherName = findViewById(R.id.ed_other_name);
            edOtherDesignation = findViewById(R.id.ed_other_designation);
            edOtherOffice = findViewById(R.id.ed_other_office);

            btnSendOtp = findViewById(R.id.btn_send_otp);
            btnVerifyOtp = findViewById(R.id.btn_verify_otp);
            btnSaveOther = findViewById(R.id.btn_save_other);
            spinner_user_type = findViewById(R.id.spinner_user_type);

            tabEmployees.setOnClickListener(v -> selectLoginTab(true));

            tabOthers.setOnClickListener(v -> selectLoginTab(false));

            SURVEY_FROM = "ESTIMATES";
            imgTogglePassword = findViewById(R.id.imgTogglePassword);
            imgTogglePassword.setOnClickListener(v -> togglePassword());


           /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("11KV/LT GIS Survey ");
            toolbar.setTitleTextColor(Color.WHITE);*/
            arcGISDatabase = new ArcGISDatabase(this);
           /* progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please Wail...");
            progressDialog.setCancelable(false);*/
            ed_user_id = (EditText) findViewById(R.id.ed_user_id);
            ed_password = (EditText) findViewById(R.id.ed_password);
            btn_login = findViewById(R.id.btn_login);
            // btn_login_bill_reader = findViewById(R.id.btn_login_bill_reader);
            //  checkBox_bill_reader = findViewById(R.id.checkBox_bill_reader);
            loader = findViewById(R.id.loader);
            //loader_bill_reader = findViewById(R.id.loader_bill_reader);
            btn_login.setOnClickListener(this);
            // btn_login_bill_reader.setOnClickListener(this);
            // checkBox_bill_reader.setOnClickListener(this);
            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            sessionId = preferences.getString(MobileConstants.SESSION_ID, "");
            office_id = preferences.getString(MobileConstants.OFFICE_ID, "");

            if (preferences.getBoolean(MeterConstants.METER_IS_LOGGED_IN, false)) {
                Intent intent = new Intent(LoginActivity.this, MeterSurveyHome.class);
                startActivity(intent);
                finish();
            }

            if (sessionId != null && !sessionId.isEmpty()) {
                System.out.println("SESSION_ID111 : " + sessionId + " OFFICE_ID" + office_id);
                isJHC = preferences.getBoolean(CommonNames.IS_JHC, false);
                IS_33KV_USER = preferences.getBoolean(CommonNames.IS_33KV_USER, false);
                List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
                Intent intent;
                if (loginDataResponses.get(0).getROLE().equalsIgnoreCase("RDSS Vendor")) {
                    LoginActivity.SURVEY_FROM = "WO_RDSS";
                    intent = new Intent(LoginActivity.this, PMINewHome.class);
                } else {
                    intent = new Intent(LoginActivity.this, PMINewHome.class);
                }
                startActivity(intent);
                finish();
            } else {
                System.out.println("SESSION_ID2222 : " + sessionId + " OFFICE_ID" + office_id);
            }
            if (Build.VERSION.SDK_INT >= 23 && !AppData.getInstance().isPermission()) {
                Utilites.isStoragePermissionGranted(LoginActivity.this);
            }


            btnSendOtp.setOnClickListener(v -> {

                String mobile = edMobileNumber.getText().toString().trim();

                if (mobile.length() != 10) {
                    edMobileNumber.setError("Enter valid mobile number");
                    return;
                }

                // Call Send OTP API here
                sendOtpApi(mobile);
            });

            btnVerifyOtp.setOnClickListener(v -> {

                String mobile = edMobileNumber.getText().toString().trim();
                String otp = edOtp.getText().toString().trim();

                if (otp.isEmpty()) {
                    edOtp.setError("Enter OTP");
                    return;
                }

                // Call Verify OTP API
                verifyOtpApi(mobile, otp);
            });

            btnSaveOther.setOnClickListener(v -> {

                if (!isOtpVerified) {
                    Toast.makeText(this, "Please verify your mobile number",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                String name = edOtherName.getText().toString().trim();
                String designation = edOtherDesignation.getText().toString().trim();
                // String office = edOtherOffice.getText().toString().trim();
                String office = spinner_user_type.getSelectedItem().toString().trim();

                if (name.isEmpty()) {
                    edOtherName.setError("Enter name");
                    return;
                }

                if (designation.isEmpty()) {
                    edOtherDesignation.setError("Enter designation");
                    return;
                }

                if (office.equalsIgnoreCase("Select")) {
                    Toast.makeText(this, "Please select user type",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // Call Save User API
                saveOtherUserApi(
                        verifiedMobileNumber,
                        name,
                        designation,
                        office
                );
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void verifyOtpApi(String mobile, String otp) {
        progressDialog.show();
        JsonObject request = new JsonObject();
        request.addProperty("OTP", otp);
        request.addProperty("OTPID", otpID);
        request.addProperty("DeviceType", "Android");
        request.addProperty("IMEINO", CommanUtilits.getIMEI_NO(LoginActivity.this));


        otpRetrofitService = OTPRetrofitClient.getRetrofitInstance().create(OTPRetrofitService.class);
        Call<JsonObject> responseVoCall = otpRetrofitService.verifyOTP(request);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                String status = response.body().get("message").getAsString();
                if (status.equalsIgnoreCase("valid OTP")) {

                    checkUserAvailability(mobile);
                } else {
                    Toast.makeText(LoginActivity.this, status, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void checkUserAvailability(String mobile) {
        progressDialog.show();
        JsonObject request = new JsonObject();
        request.addProperty("MOBILE_NO", mobile);
        request.addProperty("VERSION", getResources().getString(R.string.displayVersion));
        retrofitClientInstancePMI = RetrofitClientInstancePMI.getRetrofitInstance().create(RetrofitServicePMI.class);
        Call<LoginResponseVo> responseVoCall = retrofitClientInstancePMI.checkUser(request);
        responseVoCall.enqueue(new Callback<LoginResponseVo>() {
            @Override
            public void onResponse(Call<LoginResponseVo> call, Response<LoginResponseVo> response) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    LoginResponseVo loginDataResponse = response.body();

                    System.out.println("LOGIN RESPONSE : " + response.body().getRESPONSE());
                    if (loginDataResponse != null) {
                                /*Gson gson = new Gson();
                                String json = gson.toJson(loginDataResponse);
                                Log.v("DATA===>>", "" + json);*/
                        if ("TRUE".equalsIgnoreCase(loginDataResponse.getSTATUS())) {
                            editor = preferences.edit();
                            editor.putString(MobileConstants.USERNAME, ed_user_id.getText().toString().trim());
                            editor.putString(MobileConstants.SESSION_ID, loginDataResponse.getRESPONSE().getUSERID());
                            editor.putString(MobileConstants.MOBILE_NUMBER, loginDataResponse.getRESPONSE().getUSERID());


                            if (loginDataResponse.getRESPONSE().getROLE().equalsIgnoreCase("GIS VENDOR")) {
                                editor.putBoolean(CommonNames.IS_JHC, true);
                                isJHC = true;
                            } else {
                                editor.putBoolean(CommonNames.IS_JHC, false);
                                isJHC = false;
                            }


                            if (loginDataResponse.getRESPONSE().getROLE().equalsIgnoreCase("AEOP")) {
                                editor.putBoolean(CommonNames.IS_33KV_USER, true);
                                IS_33KV_USER = true;
                            } else {
                                editor.putBoolean(CommonNames.IS_33KV_USER, false);
                                IS_33KV_USER = false;
                            }

                            if (loginDataResponse.getRESPONSE().getROLE().equalsIgnoreCase("RDSS Vendor")) {
                                LoginActivity.SURVEY_FROM = "WO_RDSS";
                            }


                            editor.commit();
                            LoginDataResponse dataResponse = loginDataResponse.getRESPONSE();
                            if (dataResponse != null) {
                                arcGISDatabase.addUserMasterData(dataResponse);
                                new com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.sqldatabase.ArcGISDatabase(LoginActivity.this).addUserMasterData(dataResponse);
                                new com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.sqldatabase.ArcGISDatabaseRDSSUg11Kv(LoginActivity.this).addUserMasterData(dataResponse);
                                new ArcGISDatabaseUG33KV(LoginActivity.this).addUserMasterData(dataResponse);
                                if (dataResponse.getSEC_CODE() != null && dataResponse.getSECTION_NAME() != null) {
                                    Intent intent;
                                    if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")) {
                                        intent = new Intent(LoginActivity.this, PMINewHome.class);
                                    } else {
                                        intent = new Intent(LoginActivity.this, PMINewHome.class);
                                    }
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Please check user credentials", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                if (loginDataResponse.getAPK() != null) {
                                    if (loginDataResponse.getAPK().contains("http")) {
                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);

                                        alertDialogBuilder.setTitle("A new Version is available!");
                                        //  alertDialogBuilder.setTitle("A new Version is available!  \nPlease Click to Download latest app \n<a href=\\\"http://www.google.com\\\">Check this link out</a>"c);
                                        alertDialogBuilder
                                                .setMessage(Html.fromHtml("Please Download latest app\n " +
                                                        ""))
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
                                        ((TextView) alertDialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
                                    }
                                } else {
                                    Toast.makeText(LoginActivity.this, loginDataResponse.getERR_MSG(), Toast.LENGTH_SHORT).show();

                                }
                            }

                                /*else{
                                    Toast.makeText(LoginActivity.this, "No Data", Toast.LENGTH_SHORT).show();
                                }*/
                        } else {
                            onOtpVerificationSuccess(false);
                            // btn_login.setVisibility(View.VISIBLE);
                            Toast.makeText(LoginActivity.this, loginDataResponse.getERR_MSG(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        btn_login.setVisibility(View.VISIBLE);
                        Toast.makeText(LoginActivity.this, "No Data", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    // btn_login.setVisibility(View.VISIBLE);
                    Toast.makeText(LoginActivity.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponseVo> call, Throwable t) {
                progressDialog.dismiss();
            }
        });

    }

    private void saveOtherUserApi(String verifiedMobileNumber, String name, String designation, String office) {
        JsonObject request = new JsonObject();
        request.addProperty("MOBILE_NO", verifiedMobileNumber);
        request.addProperty("DESIGNATION", designation);
        request.addProperty("USER_TYPE", office);
        request.addProperty("NAME", name);
        retrofitClientInstancePMI = RetrofitClientInstancePMI.getRetrofitInstance().create(RetrofitServicePMI.class);
        Call<LoginResponseVo> responseVoCall = retrofitClientInstancePMI.saveUser(request);
        responseVoCall.enqueue(new Callback<LoginResponseVo>() {
            @Override
            public void onResponse(Call<LoginResponseVo> call, Response<LoginResponseVo> response) {
                if (response.body() != null) {
                    LoginResponseVo loginDataResponse = response.body();

                    System.out.println("LOGIN RESPONSE : " + response.body().getRESPONSE());
                    if (loginDataResponse != null) {
                                /*Gson gson = new Gson();
                                String json = gson.toJson(loginDataResponse);
                                Log.v("DATA===>>", "" + json);*/
                        if ("TRUE".equalsIgnoreCase(loginDataResponse.getSTATUS())) {
                            editor = preferences.edit();
                            editor.putString(MobileConstants.USERNAME, ed_user_id.getText().toString().trim());
                            editor.putString(MobileConstants.SESSION_ID, loginDataResponse.getRESPONSE().getUSERID());


                            if (loginDataResponse.getRESPONSE().getROLE().equalsIgnoreCase("GIS VENDOR")) {
                                editor.putBoolean(CommonNames.IS_JHC, true);
                                isJHC = true;
                            } else {
                                editor.putBoolean(CommonNames.IS_JHC, false);
                                isJHC = false;
                            }


                            if (loginDataResponse.getRESPONSE().getROLE().equalsIgnoreCase("AEOP")) {
                                editor.putBoolean(CommonNames.IS_33KV_USER, true);
                                IS_33KV_USER = true;
                            } else {
                                editor.putBoolean(CommonNames.IS_33KV_USER, false);
                                IS_33KV_USER = false;
                            }

                            if (loginDataResponse.getRESPONSE().getROLE().equalsIgnoreCase("RDSS Vendor")) {
                                LoginActivity.SURVEY_FROM = "WO_RDSS";
                            }


                            editor.commit();
                            LoginDataResponse dataResponse = loginDataResponse.getRESPONSE();
                            if (dataResponse != null) {
                                arcGISDatabase.addUserMasterData(dataResponse);
                                new com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.sqldatabase.ArcGISDatabase(LoginActivity.this).addUserMasterData(dataResponse);
                                new com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.sqldatabase.ArcGISDatabaseRDSSUg11Kv(LoginActivity.this).addUserMasterData(dataResponse);
                                new ArcGISDatabaseUG33KV(LoginActivity.this).addUserMasterData(dataResponse);
                                if (dataResponse.getSEC_CODE() != null && dataResponse.getSECTION_NAME() != null) {
                                    Intent intent;
                                    if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")) {
                                        intent = new Intent(LoginActivity.this, PMINewHome.class);
                                    } else {
                                        intent = new Intent(LoginActivity.this, PMINewHome.class);
                                    }
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Please check user credentials", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                if (loginDataResponse.getAPK() != null) {
                                    if (loginDataResponse.getAPK().contains("http")) {
                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);

                                        alertDialogBuilder.setTitle("A new Version is available!");
                                        //  alertDialogBuilder.setTitle("A new Version is available!  \nPlease Click to Download latest app \n<a href=\\\"http://www.google.com\\\">Check this link out</a>"c);
                                        alertDialogBuilder
                                                .setMessage(Html.fromHtml("Please Download latest app\n " +
                                                        ""))
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
                                        ((TextView) alertDialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
                                    }
                                } else {
                                    Toast.makeText(LoginActivity.this, loginDataResponse.getERR_MSG(), Toast.LENGTH_SHORT).show();

                                }
                            }

                                /*else{
                                    Toast.makeText(LoginActivity.this, "No Data", Toast.LENGTH_SHORT).show();
                                }*/
                        } else {
                            // onOtpVerificationSuccess(false);
                            // btn_login.setVisibility(View.VISIBLE);
                            Toast.makeText(LoginActivity.this, loginDataResponse.getERR_MSG(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        btn_login.setVisibility(View.VISIBLE);
                        Toast.makeText(LoginActivity.this, "No Data", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    // btn_login.setVisibility(View.VISIBLE);
                    Toast.makeText(LoginActivity.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponseVo> call, Throwable t) {

            }
        });
    }

    private void sendOtpApi(String mobile) {
        progressDialog.show();
        JsonObject request = new JsonObject();
        request.addProperty("MOBILE_NO", mobile);
        request.addProperty("CHANEL", "EPCCB");
        request.addProperty("DeviceType", "Android");
        request.addProperty("IMEINO", CommanUtilits.getIMEI_NO(LoginActivity.this));


        otpRetrofitService = OTPRetrofitClient.getRetrofitInstance().create(OTPRetrofitService.class);
        Call<JsonObject> responseVoCall = otpRetrofitService.sendOTP(request);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                otpID = response.body().get("message").getAsString();
                onOtpSent();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }


    private void onOtherUserSaved() {

        Toast.makeText(this,
                "Registration successful",
                Toast.LENGTH_SHORT).show();

        proceedToHome();
    }

    private void proceedToHome() {
    }

    private void onOtpVerificationSuccess(boolean userExists) {

        isOtpVerified = true;

        verifiedMobileNumber = edMobileNumber.getText().toString().trim();

        if (userExists) {

            // Existing user
            // Store user/session details returned by API
            // Navigate to Home Screen

            proceedToHome();

        } else {

            // New user
            layoutOtherDetails.setVisibility(View.VISIBLE);

            layoutOtp.setVisibility(View.GONE);

            btnSendOtp.setVisibility(View.GONE);

            edMobileNumber.setEnabled(false);
        }
    }

    private void onOtpSent() {

        layoutOtp.setVisibility(View.VISIBLE);

        btnSendOtp.setEnabled(false);
        btnSendOtp.setText("OTP Sent");
    }


    private void selectLoginTab(boolean isEmployee) {

        if (isEmployee) {

            employeeLoginContainer.setVisibility(View.VISIBLE);
            othersLoginContainer.setVisibility(View.GONE);

            tabEmployees.setBackgroundResource(R.drawable.button_background);
            tabEmployees.setTextColor(Color.WHITE);

            tabOthers.setBackgroundColor(Color.TRANSPARENT);
            tabOthers.setTextColor(getResources().getColor(R.color.colorPrimary));

        } else {

            employeeLoginContainer.setVisibility(View.GONE);
            othersLoginContainer.setVisibility(View.VISIBLE);

            tabOthers.setBackgroundResource(R.drawable.button_background);
            tabOthers.setTextColor(Color.WHITE);

            tabEmployees.setBackgroundColor(Color.TRANSPARENT);
            tabEmployees.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    private void togglePassword() {
        if (isPasswordVisible) {
            // Hide password
            ed_password.setInputType(
                    InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD
            );
            imgTogglePassword.setImageResource(R.drawable.ic_eye_closed);
        } else {
            // Show password
            ed_password.setInputType(
                    InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            );
            imgTogglePassword.setImageResource(R.drawable.ic_eye_open);
        }

        ed_password.setSelection(ed_password.getText().length());
        isPasswordVisible = !isPasswordVisible;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_login) {
            if (ConnectivityChecking.checkConnection(LoginActivity.this)) {

                if (false) {
                    //if (checkBox_bill_reader.isChecked()){
                    loginRequestRetofitMeterSurvey();
                } else {
                    if (hasAllPermissions()) {

                        loginRequestRetofit();
                    } else {
                        requestAllPermissions();
                    }
                }
                /*if (Build.VERSION.SDK_INT >= 23) {
                    if (AppData.getInstance().isPermission()) {
                        loginRequestRetofit();
                    } else {
                        if (Build.VERSION.SDK_INT >= 23 && !AppData.getInstance().isPermission()) {
                            Utilites.isStoragePermissionGranted(LoginActivity.this);
                        } else {
                            loginRequestRetofit();
                        }
                    }

                } else {
                    loginRequestRetofit();
                }*/

//                if (hasAllPermissions()) {
//
//                    loginRequestRetofit();
//                } else {
//                    requestAllPermissions();
//                }

            } else {
                CommanUtilits.internetDialog(LoginActivity.this);
//                Toast.makeText(getApplicationContext(), MobileConstants.INTERNET, Toast.LENGTH_LONG).show();
            }
        }

    }


    private boolean hasAllPermissions() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestAllPermissions() {
        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSION_REQUEST_CODE);
    }


    private void loginRequestRetofitMeterSurvey() {
        if (ed_user_id.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Enter UserName", Toast.LENGTH_LONG).show();
        } else if (ed_password.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Enter Password", Toast.LENGTH_LONG).show();
        } else {

            // String uname = PasswordUtil.encrypt(ed_user_id.getText().toString().trim());
            // System.out.println("DEC :"+PasswordUtil.decrypt("0da8b815a602266070dee10cb51ca723"));
            String pwd = PasswordUtil.encryptNew(ed_password.getText().toString().trim());
            //   loader_bill_reader.setVisibility(View.VISIBLE);
            // btn_login_bill_reader.setVisibility(View.GONE);
            btn_login.setVisibility(View.GONE);
            LoginRequestVo loginRequestVo = new LoginRequestVo();
            loginRequestVo.setUSER_ID(ed_user_id.getText().toString().trim());
            loginRequestVo.setPASSWORD(pwd);
            loginRequestVo.setIMEI_NO(CommanUtilits.getIMEI_NO(LoginActivity.this));
            loginRequestVo.setVERSION(MeterConstants.METER_APP_VERSION);
            if (ConnectivityChecking.checkConnection(LoginActivity.this)) {
               /* editor = preferences.edit();
                editor.putString(MeterConstants.METER_USER_ID, "AGN0385");
                editor.putString(MeterConstants.METER_SECTION_NAME, "BARUKU ANANDARAO");
                editor.putString(MeterConstants.METER_OFFICE_ID, "BILL READER");
                editor.putBoolean(MeterConstants.METER_IS_LOGGED_IN,true);
                editor.commit();
                Intent intent = new Intent(LoginActivity.this, MeterSurveyHome.class);
                startActivity(intent);
                finish();*/

                billReaderLogin(loginRequestVo);

            } else {
                CommanUtilits.internetDialog(LoginActivity.this);
            }

            Gson gson = new Gson();
            String json = gson.toJson(loginRequestVo);
            //  Log.v("DATA===>>", "" + json);
        }
    }

    private void loginRequestRetofit() {
        if (ed_user_id.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Enter UserName", Toast.LENGTH_LONG).show();
        } else if (ed_password.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Enter Password", Toast.LENGTH_LONG).show();
        } else {
            loader.setVisibility(View.VISIBLE);
            btn_login.setVisibility(View.GONE);
            LoginRequestVo loginRequestVo = new LoginRequestVo();
            loginRequestVo.setUSER_ID(ed_user_id.getText().toString().trim());
            loginRequestVo.setPASSWORD(ed_password.getText().toString().trim());
            loginRequestVo.setIMEI_NO(CommanUtilits.getIMEI_NO(LoginActivity.this));
            loginRequestVo.setVERSION(MobileConstants.APP_VERSION);
            if (ConnectivityChecking.checkConnection(LoginActivity.this)) {
                usrLogin(loginRequestVo);
            } else {
                CommanUtilits.internetDialog(LoginActivity.this);
            }

            Gson gson = new Gson();
            String json = gson.toJson(loginRequestVo);
            //  Log.v("DATA===>>", "" + json);
        }
    }

    public void usrLogin(LoginRequestVo loginRequestVo) {
        try {
            retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance("").create(RetrofitServiceEstimates.class);
            Call<LoginResponseVo> responseVoCall = retrofitService.getLoginData(loginRequestVo);
            responseVoCall.enqueue(new Callback<LoginResponseVo>() {
                @Override
                public void onResponse(Call<LoginResponseVo> call, Response<LoginResponseVo> response) {
                    loader.setVisibility(View.GONE);
                    if (response.body() != null) {
                        LoginResponseVo loginDataResponse = response.body();

                        System.out.println("LOGIN RESPONSE : " + response.body().getRESPONSE());
                        if (loginDataResponse != null) {
                                /*Gson gson = new Gson();
                                String json = gson.toJson(loginDataResponse);
                                Log.v("DATA===>>", "" + json);*/
                            if ("TRUE".equalsIgnoreCase(loginDataResponse.getSTATUS())) {
                                editor = preferences.edit();
                                editor.putString(MobileConstants.USERNAME, ed_user_id.getText().toString().trim());
                                editor.putString(MobileConstants.SESSION_ID, loginDataResponse.getSESSION_ID());


                                if (loginDataResponse.getRESPONSE().getROLE().equalsIgnoreCase("GIS VENDOR")) {
                                    editor.putBoolean(CommonNames.IS_JHC, true);
                                    isJHC = true;
                                } else {
                                    editor.putBoolean(CommonNames.IS_JHC, false);
                                    isJHC = false;
                                }


                                if (loginDataResponse.getRESPONSE().getROLE().equalsIgnoreCase("AEOP")) {
                                    editor.putBoolean(CommonNames.IS_33KV_USER, true);
                                    IS_33KV_USER = true;
                                } else {
                                    editor.putBoolean(CommonNames.IS_33KV_USER, false);
                                    IS_33KV_USER = false;
                                }

                                if (loginDataResponse.getRESPONSE().getROLE().equalsIgnoreCase("RDSS Vendor")) {
                                    LoginActivity.SURVEY_FROM = "WO_RDSS";
                                }


                                editor.commit();
                                LoginDataResponse dataResponse = loginDataResponse.getRESPONSE();
                                if (dataResponse != null) {
                                    arcGISDatabase.addUserMasterData(dataResponse);
                                    new com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.sqldatabase.ArcGISDatabase(LoginActivity.this).addUserMasterData(dataResponse);
                                    new com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.sqldatabase.ArcGISDatabaseRDSSUg11Kv(LoginActivity.this).addUserMasterData(dataResponse);
                                    new ArcGISDatabaseUG33KV(LoginActivity.this).addUserMasterData(dataResponse);
                                    if (dataResponse.getSEC_CODE() != null && dataResponse.getSECTION_NAME() != null) {
                                        Intent intent;
                                        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")) {
                                            intent = new Intent(LoginActivity.this, PMINewHome.class);
                                        } else {
                                            intent = new Intent(LoginActivity.this, PMINewHome.class);
                                        }
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Please check user credentials", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    if (loginDataResponse.getAPK() != null) {
                                        if (loginDataResponse.getAPK().contains("http")) {
                                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);

                                            alertDialogBuilder.setTitle("A new Version is available!");
                                            //  alertDialogBuilder.setTitle("A new Version is available!  \nPlease Click to Download latest app \n<a href=\\\"http://www.google.com\\\">Check this link out</a>"c);
                                            alertDialogBuilder
                                                    .setMessage(Html.fromHtml("Please Download latest app\n " +
                                                            ""))
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
                                            ((TextView) alertDialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
                                        }
                                    } else {
                                        Toast.makeText(LoginActivity.this, loginDataResponse.getERR_MSG(), Toast.LENGTH_SHORT).show();

                                    }
                                }

                                /*else{
                                    Toast.makeText(LoginActivity.this, "No Data", Toast.LENGTH_SHORT).show();
                                }*/
                            } else {
                                btn_login.setVisibility(View.VISIBLE);
                                Toast.makeText(LoginActivity.this, loginDataResponse.getERR_MSG(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            btn_login.setVisibility(View.VISIBLE);
                            Toast.makeText(LoginActivity.this, "No Data", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        btn_login.setVisibility(View.VISIBLE);
                        Toast.makeText(LoginActivity.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponseVo> call, Throwable t) {
                    loader.setVisibility(View.GONE);
                    btn_login.setVisibility(View.VISIBLE);
                    CommanUtilits.netWorkError(LoginActivity.this);
                    //Toast.makeText(LoginActivity.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            loader.setVisibility(View.GONE);
            btn_login.setVisibility(View.VISIBLE);
            e.printStackTrace();
        }
    }

    public void billReaderLogin(LoginRequestVo loginRequestVo) {
        loader.setVisibility(View.VISIBLE);
        try {
            HashMap<String, Object> dataMap = new HashMap<>();
            dataMap.put("MOBILE_NO", loginRequestVo.getUSER_ID());
            dataMap.put("PASSWORD", loginRequestVo.getPASSWORD());
            dataMap.put("VERSION", MobileConstants.ESTIMATES_VERSION);
            Gson gson = new Gson();
            JsonObject jsonObject = gson.toJsonTree(dataMap).getAsJsonObject();
            retrofitServiceMeter = RetrofitClientInstanceMeter.getRetrofitInstance().create(RetrofitServiceMeter.class);

            Call<JsonObject> responseVoCall = retrofitServiceMeter.billReaderLogin(jsonObject);

            responseVoCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    loader.setVisibility(View.GONE);

                    if (response.body() == null) {
                        Toast.makeText(LoginActivity.this, "Server Busy. Try Again.", Toast.LENGTH_SHORT).show();
                        // loader_bill_reader.setVisibility(View.GONE);
                        btn_login.setVisibility(View.VISIBLE);
                        //  btn_login_bill_reader.setVisibility(View.VISIBLE);
                        return;
                    }


                    JsonObject surveyedResponseVo = response.body();

                    if (!surveyedResponseVo.has("STATUS") || surveyedResponseVo.get("STATUS").isJsonNull()) {
                        Toast.makeText(LoginActivity.this, "Invalid response from server: STATUS missing", Toast.LENGTH_SHORT).show();
                        //   loader_bill_reader.setVisibility(View.GONE);
                        btn_login.setVisibility(View.VISIBLE);
                        // btn_login_bill_reader.setVisibility(View.VISIBLE);
                        return;
                    }

                    if (!MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        String errorMessage = surveyedResponseVo.has("ERR_MSG")
                                ? surveyedResponseVo.get("ERR_MSG").getAsString()
                                : "Unknown error occurred";

                        if (errorMessage.contains("update to Version")) {
                            openDialogForVersion();
                        }
                        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        //  loader_bill_reader.setVisibility(View.GONE);
                        btn_login.setVisibility(View.VISIBLE);
                        // btn_login_bill_reader.setVisibility(View.VISIBLE);
                        return;
                    }

                    JsonObject object = surveyedResponseVo.getAsJsonObject("RESPONSE");

                    editor = preferences.edit();
                    editor.putString(MeterConstants.METER_USER_ID, object.get("USERID").getAsString());
                    editor.putString(MeterConstants.METER_OFFICE_ID, object.get("OFFICE_ID").getAsString());
                    editor.putString(MeterConstants.METER_SECTION_NAME, object.get("SECTION_NAME").getAsString());
                    editor.putString(MeterConstants.METER_MOBILE_NO, object.get("MOBILE_NO").getAsString());
                    editor.putBoolean(MeterConstants.METER_IS_LOGGED_IN, true);
                    editor.commit();
                    Intent intent = new Intent(LoginActivity.this, MeterSurveyHome.class);
                    startActivity(intent);
                    finish();


                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "ERROR: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    //loader_bill_reader.setVisibility(View.GONE);
                    btn_login.setVisibility(View.VISIBLE);
                    // btn_login_bill_reader.setVisibility(View.VISIBLE);
                }
            });
        } catch (Exception e) {
            loader.setVisibility(View.VISIBLE);
            //loader_bill_reader.setVisibility(View.GONE);
            btn_login.setVisibility(View.VISIBLE);
            // btn_login_bill_reader.setVisibility(View.VISIBLE);
            e.printStackTrace();
        }
    }


    private void openDialogForVersion() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);

        alertDialogBuilder.setTitle("A new Version is available!");
        //  alertDialogBuilder.setTitle("A new Version is available!  \nPlease Click to Download latest app \n<a href=\\\"http://www.google.com\\\">Check this link out</a>"c);
        alertDialogBuilder.setMessage(Html.fromHtml("Before upgrade,please transmit the data to server \n " +
                        "Please Download latest MOBILE APP APKs"))
                //.setMessage("Before upgrade,please transmit the data to server ")
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

                .setNegativeButton("Transmit Data", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allGranted = true;

            for (int i = 0; i < permissions.length; i++) {
                Log.d("PermissionCheck", permissions[i] + ": " + grantResults[i]);
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                }
            }

            if (allGranted) {
                loginRequestRetofit();
            } else {
                Toast.makeText(this, "Please grant all permissions to continue.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
