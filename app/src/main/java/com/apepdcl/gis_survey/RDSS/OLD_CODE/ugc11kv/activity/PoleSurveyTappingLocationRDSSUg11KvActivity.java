package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.constants.MobileConstantsRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.retrofitservice.responsevo.RetrofitClientInstanceRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.retrofitservice.responsevo.RetrofitServiceRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.runtimepermessions.ConnectivityCheckingRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.utilities.CommanUtilitsRDSSUg11Kv;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PoleSurveyTappingLocationRDSSUg11KvActivity extends AppCompatActivity  implements View.OnClickListener {
    TextView tv_feeder_name;
    EditText ed_tapping_number,ed_tapping_location_name;
    Button btn_submit;
    private ProgressDialog progressDialog;
    private RetrofitServiceRDSSUg11Kv retrofitServiceRDSSUg11Kv;
    private SharedPreferences preferences;
    String userName,sessionId,ssName,feederName,feederCode,maxCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pole_survey_tapping_location);
        try {

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("Addition of Tapping Location");
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent playIntent = new Intent(PoleSurveyTappingLocationRDSSUg11KvActivity.this, FeederMasterRDSSUg11KvActivity.class); //Intent.ACTION_VIEW);
                    startActivity(playIntent);
                    finish();

                }
            });

            tv_feeder_name = (TextView) findViewById(R.id.tv_feeder_name);
            ed_tapping_number = (EditText) findViewById(R.id.ed_tapping_number);
            ed_tapping_location_name = (EditText) findViewById(R.id.ed_tapping_location_name);
            btn_submit = (Button) findViewById(R.id.btn_submit);
            progressDialog = CommanUtilitsRDSSUg11Kv.progressDialog(this);

            preferences = PreferenceManager.getDefaultSharedPreferences(PoleSurveyTappingLocationRDSSUg11KvActivity.this);
            userName = preferences.getString(MobileConstantsRDSSUg11Kv.USERNAME, "");
            sessionId = preferences.getString(MobileConstantsRDSSUg11Kv.SESSION_ID, "");
            if (sessionId == null || sessionId.isEmpty()) {
                Intent intentLogin = new Intent(PoleSurveyTappingLocationRDSSUg11KvActivity.this, LoginActivity.class);
                startActivity(intentLogin);
                finish();
            }
            Intent intent = getIntent();
            if (intent!=null) {
                ssName = intent.getStringExtra("SSNAME");
                feederName = intent.getStringExtra("FEEDER_NAME");
                feederCode = intent.getStringExtra("FEEDER_CODE");
                maxCount = intent.getStringExtra("MAX_COUNT");
            }

            btn_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkValidation();
                }
            });

        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_submit:
                checkValidation();
                break;
        }
    }


    private void checkValidation() {
        if (ed_tapping_number.getText().toString()==null || ed_tapping_number.getText().toString().equals("")){
            Toast.makeText(this, "Please enter tapping pole number", Toast.LENGTH_SHORT).show();
        }else if (ed_tapping_location_name.getText().toString()==null || ed_tapping_location_name.getText().toString().equals("")){
            Toast.makeText(this, "Please enter tapping location name", Toast.LENGTH_SHORT).show();
        }
        else {
            if (ConnectivityCheckingRDSSUg11Kv.checkConnection(PoleSurveyTappingLocationRDSSUg11KvActivity.this)) {

                getPoleSurveyTappingLocation();
            } else {
                CommanUtilits.internetDialog(PoleSurveyTappingLocationRDSSUg11KvActivity.this);
                //Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void getPoleSurveyTappingLocation() {
        try {
            progressDialog.show();

            JsonObject jsonObject = new JsonObject();
            //   jsonObject.addProperty("IMEI_NO", CommanUtilits.getIMEI_NO(PoleSurveyTappingLocationActivity.this));
            jsonObject.addProperty("SESSION_ID", sessionId);
            jsonObject.addProperty("VERSION", MobileConstantsRDSSUg11Kv.APP_VERSION);
            // jsonObject.addProperty("VERSION", "1");
            jsonObject.addProperty("USER_ID", preferences.getString(MobileConstantsRDSSUg11Kv.USERNAME, ""));
            // jsonObject.addProperty("USER_ID","TEST");
            jsonObject.addProperty("FEEDER_ID", feederCode);
            jsonObject.addProperty("POLE_NO", ed_tapping_number.getText().toString());
            jsonObject.addProperty("TAPPING_LOCNAME", ed_tapping_location_name.getText().toString());

            retrofitServiceRDSSUg11Kv = RetrofitClientInstanceRDSSUg11Kv.getRetrofitInstance().create(RetrofitServiceRDSSUg11Kv.class);
            Call<JsonObject> responseVoCall = retrofitServiceRDSSUg11Kv.getAddTappingPoint(jsonObject);
            responseVoCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    progressDialog.dismiss();
                    if (response != null && response.isSuccessful()) {
                        JsonObject resObj = response.body();
                        System.out.println("LC RESPONSE : "+response.body());
                        if ("TRUE".equalsIgnoreCase(resObj.get("STATUS").getAsString())) {
                            progressDialog.dismiss();
                            Toast.makeText(PoleSurveyTappingLocationRDSSUg11KvActivity.this, resObj.get("ERR_MSG").getAsString(), Toast.LENGTH_SHORT).show();
                            Intent playIntent = new Intent(PoleSurveyTappingLocationRDSSUg11KvActivity.this, FeederMasterRDSSUg11KvActivity.class); //Intent.ACTION_VIEW);
                            startActivity(playIntent);
                            finish();
                        } else {
                            if (!resObj.get("APK") .isJsonNull()) {
                                if (resObj.get("APK").getAsString().contains("http")) {

                                    androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(PoleSurveyTappingLocationRDSSUg11KvActivity.this);
                                    alertDialogBuilder.setTitle("A new Version Is available!");
                                    alertDialogBuilder
                                            .setMessage("Before upgrade,please transmit the data to server ")
                                            .setCancelable(false)
                                            .setPositiveButton("Ok",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {

                                                        }
                                                    })

                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();

                                                }
                                            });
                                    androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                    progressDialog.dismiss();
                                }
                            }else {
                                Toast.makeText(PoleSurveyTappingLocationRDSSUg11KvActivity.this, resObj.get("ERR_MSG").getAsString(), Toast.LENGTH_SHORT).show();

                            }
                            progressDialog.dismiss();
                            Toast.makeText(PoleSurveyTappingLocationRDSSUg11KvActivity.this, resObj.get("ERR_MSG").getAsString(), Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(PoleSurveyTappingLocationRDSSUg11KvActivity.this, "Please Try Again", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(PoleSurveyTappingLocationRDSSUg11KvActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
       /* Intent intent = new Intent(PoleSurveyActivity.this, FeederMasterActivity.class);
        startActivity(intent);
        finish();*/
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
    }
}