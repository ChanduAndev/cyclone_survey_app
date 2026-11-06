package com.apepdcl.gis_survey.pmi_old.module_33kv.activities;

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

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.pmi_old.module_11kv.activities.PMIHomeActivity;
import com.apepdcl.gis_survey.pmi_old.module_33kv.PMIRetrofitClientInstance33kv;
import com.apepdcl.gis_survey.pmi_old.module_33kv.RetrofitService33kv;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PMIAddTappingPoint33KV extends AppCompatActivity {
    TextView tv_feeder_name;
    EditText ed_tapping_number,ed_tapping_location_name;
    Button btn_submit;
    private ProgressDialog progressDialog;
    private RetrofitService33kv retrofitService;
    private SharedPreferences preferences;
    String userName,sessionId,ssName,feederName,feederCode,maxCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pmiadd_tapping_point33_kv);
        getSupportActionBar().hide();
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Add Tapping Point");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tv_feeder_name = (TextView) findViewById(R.id.tv_feeder_name);
        ed_tapping_number = (EditText) findViewById(R.id.ed_tapping_number);
        ed_tapping_location_name = (EditText) findViewById(R.id.ed_tapping_location_name);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        progressDialog = CommanUtilits.progressDialog(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userName = preferences.getString(MobileConstants.USERNAME, "");
        sessionId = preferences.getString(MobileConstants.SESSION_ID, "");
        if (sessionId == null || sessionId.isEmpty()) {
            Intent intentLogin = new Intent(PMIAddTappingPoint33KV.this, LoginActivity.class);
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
    }

    private void checkValidation() {
        if (ed_tapping_number.getText().toString().isEmpty() || ed_tapping_number.getText().toString().equals("")){
            Toast.makeText(this, "Please enter tapping pole number", Toast.LENGTH_SHORT).show();
        }else if (ed_tapping_location_name.getText().toString().isEmpty() || ed_tapping_location_name.getText().toString().equals("")){
            Toast.makeText(this, "Please enter tapping location name", Toast.LENGTH_SHORT).show();
        }
        else {
            if (ConnectivityChecking.checkConnection(PMIAddTappingPoint33KV.this)) {

                getPoleSurveyTappingLocation();
            } else {
                Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void getPoleSurveyTappingLocation() {
        try {
            progressDialog.show();

            JsonObject jsonObject = new JsonObject();
            //   jsonObject.addProperty("IMEI_NO", CommanUtilits.getIMEI_NO(PoleSurveyTappingLocationActivity.this));
            jsonObject.addProperty("SESSION_ID", sessionId);
            jsonObject.addProperty("VERSION", MobileConstants.APP_VERSION_33_KV);
            // jsonObject.addProperty("VERSION", "1");
            jsonObject.addProperty("USER_ID", preferences.getString(MobileConstants.USERNAME, ""));
            // jsonObject.addProperty("USER_ID","TEST");
            jsonObject.addProperty("FEEDER_ID", feederCode);
            jsonObject.addProperty("POLE_NO", ed_tapping_number.getText().toString());
            jsonObject.addProperty("TAPPING_LOCNAME", ed_tapping_location_name.getText().toString());

            retrofitService = PMIRetrofitClientInstance33kv.getRetrofitInstance().create(RetrofitService33kv.class);
            Call<JsonObject> responseVoCall = retrofitService.getAddTappingPoint(jsonObject);
            responseVoCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    progressDialog.dismiss();
                    if (response != null && response.isSuccessful()) {
                        JsonObject resObj = response.body();
                        // System.out.println("LC RESPONSE : "+response.body());
                        if ("TRUE".equalsIgnoreCase(resObj.get("STATUS").getAsString())) {
                            progressDialog.dismiss();
                            Toast.makeText(PMIAddTappingPoint33KV.this, resObj.get("ERR_MSG").getAsString(), Toast.LENGTH_SHORT).show();
                            Intent playIntent = new Intent(PMIAddTappingPoint33KV.this, PMIHomeActivity.class); //Intent.ACTION_VIEW);
                            startActivity(playIntent);
                            finish();
                        } else {
                            if (!resObj.get("APK").isJsonNull()) {
                                if (resObj.get("APK").getAsString().contains("http")) {

                                    androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(PMIAddTappingPoint33KV.this);
                                    alertDialogBuilder.setTitle("A new Version is available!");
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
                            }
                            progressDialog.dismiss();
                            Toast.makeText(PMIAddTappingPoint33KV.this, resObj.get("ERR_MSG").getAsString(), Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(PMIAddTappingPoint33KV.this, "Please Try Again", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(PMIAddTappingPoint33KV.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}