package com.apepdcl.gis_survey.module_11KV;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.module_11KV.api_calls.RetrofitClientInstance;
import com.apepdcl.gis_survey.module_11KV.api_calls.RetrofitService;
import com.apepdcl.gis_survey.module_11KV.models.FeederDataVo;
import com.apepdcl.gis_survey.module_11KV.models.LastSurveyRequestVo;
import com.apepdcl.gis_survey.module_11KV.models.LastSurveyResponseDataVo;
import com.apepdcl.gis_survey.module_11KV.models.LastSurveyResponseSSDataVo;
import com.apepdcl.gis_survey.module_11KV.models.SSMasterVo;
import com.apepdcl.gis_survey.module_11KV.models.SurveyVo;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectFeeder extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button btn_next;
    private Spinner sp_ssname1, sp_feedername1;
    private String ssName, feederName, feederId, maxCount, sessionId, feederCode, ssCode, subName, feedeNameList;
    private ArcGISDatabase arcGISDatabase;
    private List<String> feederNameList;
    private List<String> ehtName;
    private RetrofitService retrofitService;
    private ProgressDialog progressDialog;
    private SharedPreferences preferences;

    //MULTI CKT REQUIRED
    LinearLayout lyt_is_multi_circuit;
    Spinner sp_is_multi_circuit;
    String ALLOW_SURVEY = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_feeder);
        getSupportActionBar().hide();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("11kV Survey");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        progressDialog = CommanUtilits.progressDialog(this);
        sp_ssname1 = findViewById(R.id.sp_ssname1);
        sp_feedername1 = findViewById(R.id.sp_feedername1);
        lyt_is_multi_circuit = findViewById(R.id.lyt_is_multi_circuit);
        sp_is_multi_circuit = findViewById(R.id.sp_is_multi_circuit);
//        getSupportActionBar().setTitle("11Kv LineSurvey Test");
        sp_ssname1.setOnItemSelectedListener(this);
        sp_feedername1.setOnItemSelectedListener(this);
        arcGISDatabase = new ArcGISDatabase(this);

        ehtName = new ArrayList<>();
        //ehtName.add("Select");
        ehtName = arcGISDatabase.getEHTSSNAME();
        ehtName.add("Select");
        Collections.reverse(ehtName);

        ArrayAdapter<String> ehtSSadapter = new ArrayAdapter<String>(SelectFeeder.this,
                android.R.layout.simple_spinner_dropdown_item, ehtName);
        sp_ssname1.setAdapter(ehtSSadapter);


        btn_next = (Button) findViewById(R.id.btn_next);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sessionId = preferences.getString(MobileConstants.SESSION_ID, "");
        if (sessionId == null || sessionId.isEmpty()) {
            Intent intent = new Intent(SelectFeeder.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("Select".equals(ssName)) {
                    Toast.makeText(SelectFeeder.this, "Select SS Name", Toast.LENGTH_LONG).show();
                } else if ("Select".equalsIgnoreCase(feederName)) {
                    Toast.makeText(SelectFeeder.this, "Select Feeder Name", Toast.LENGTH_LONG).show();
                } else if ("Select".equalsIgnoreCase(sp_is_multi_circuit.getSelectedItem().toString())) {
                    Toast.makeText(SelectFeeder.this, "Select Multi Circuit required", Toast.LENGTH_SHORT).show();
                } else {

                    if (ConnectivityChecking.checkConnection(SelectFeeder.this)) {
                        getSurveyData();
                    } else {
                        Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                    }


                    /*Intent intent = new Intent(MainActivity.this, PoleSurveyActivity.class);
                    intent.putExtra("SSNAME", ssName);
                    intent.putExtra("FEEDER_NAME", feederName);
                    startActivity(intent);
                    finish();*/
                }
            }
        });

    }

    private void openAlertDialogNew(Spinner spinner) {
        spinner.setSelection(0, true);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_success, null);
        dialog.setContentView(dialogView);
        dialog.show();
        TextView cancelButton = dialogView.findViewById(R.id.cancelButton);
        TextView okayButton = dialogView.findViewById(R.id.okayButton);
        ImageView sheet_image = dialogView.findViewById(R.id.sheet_image);
        sheet_image.setImageDrawable(getResources().getDrawable(R.drawable.report));
        TextView title = dialogView.findViewById(R.id.title);
        TextView message = dialogView.findViewById(R.id.message);
        title.setText("Please contact the GIS team");
        String detailsMessage =
                "1. The lines/assets executed pertains to work orders released after 01.01.2025 should be integrated with Work Order Module only.\n\n" +
                        "2. For using the Asset Mapping (Old) module AE-Operation is requested to raise the support complaint in EPCCB in GIS Module.\n\n" +
                        "3. Asset Mapping (Old) module can be used if there is requirement of the Re-survey due to Bifurcation or Segregation works or re-survey required for Incorrect Survey.";


        message.setText("The selected 11kV Feeder is not allowed for GIS Survey. " +
                "Use Work Order Module for surveying new  works after 01.01.2025.\n\nDETAILS\n"+detailsMessage);

        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

//    private void openAlertDialog(Spinner spinner) {
//        // Inflate custom view
//        LayoutInflater inflater = LayoutInflater.from(this);
//        View dialogView = inflater.inflate(R.layout.dialog_custom_alert, null);
//
//        TextView messageText = dialogView.findViewById(R.id.text_message);
//        TextView detailsText = dialogView.findViewById(R.id.text_details);
//
//        // Set main message
//        messageText.setText("The selected 11kV Feeder is not allowed for GIS Survey. " +
//                "Use Work Order Module for surveying new  works after 01.01.2025.");
//
//        // Create AlertDialog
//        AlertDialog alertDialog = new AlertDialog.Builder(this)
//                .setTitle("Please contact the GIS team")
//                .setView(dialogView)
//                .setCancelable(false)
//                .setPositiveButton("Okay", (dialog, which) -> {
//                    spinner.setSelection(0, true);
//                    dialog.dismiss();
//                })
//                .create();
//
//        // Handle Details click
//        detailsText.setOnClickListener(v -> showDetailsDialog());
//
//        alertDialog.show();
//    }

    // Show another dialog for detailed information
    private void showDetailsDialog() {
        String detailsMessage =
                "1. The lines/assets executed pertains to work orders released after 01.01.2025 should be integrated with Work Order Module only.\n\n" +
                        "2. For using the Asset Mapping (Old) module AE-Operation is requested to raise the support complaint in EPCCB in GIS Module.\n\n" +
                        "3. Asset Mapping (Old) module can be used if there is requirement of the Re-survey due to Bifurcation or Segregation works or re-survey required for Incorrect Survey.";

        new AlertDialog.Builder(this)
                .setTitle("Details")
                .setMessage(detailsMessage)
                .setCancelable(false)
                .setPositiveButton("Close", (dialog, which) -> dialog.dismiss())
                .show();
    }


//    private void openAlertDialog(Spinner spinner) {
//        new android.app.AlertDialog.Builder(this)
//                .setTitle("Pay Attention")
//                .setCancelable(false)
//                .setMessage("The selected feeder is not allowed for survey. Please contact the GIS team")
//                .setPositiveButton("Okay", (dialog, which) -> {
//                    spinner.setSelection(0, true);
//                    dialog.cancel();
//                })
//                .show();
//
//
//    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        try {
            switch (adapterView.getId()) {

                case R.id.sp_ssname1:
                    feederNameList = new ArrayList<>();
                    ssName = sp_ssname1.getSelectedItem().toString();
                    if (!"Select".equalsIgnoreCase(ssName)) {


                        String[] ss = ssName.split("-");
                        ssCode = ss[0]; // 004
                        subName = ss[1];
                        //Collections.reverse(feederNameList);
                        feederNameList = arcGISDatabase.getComplaint(String.format("SELECT DISTINCT FEEDER_CODE_33KV || '-' || FEEDER_NAME_33KV FROM EHTMASTER_TABLE WHERE EHT_SS_CODE='%s'", ssCode));
                        feederNameList.add("Select");
                        Collections.reverse(feederNameList);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SelectFeeder.this,
                                android.R.layout.simple_spinner_dropdown_item, feederNameList);
                        sp_feedername1.setAdapter(adapter);


                    } else {
                        feederNameList.add("Select");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SelectFeeder.this,
                                android.R.layout.simple_spinner_dropdown_item, feederNameList);
                        sp_feedername1.setAdapter(adapter);
                    }
                    break;
                case R.id.sp_feedername1:
                    feederName = sp_feedername1.getSelectedItem().toString();
                    if (!feederName.equalsIgnoreCase("Select")) {
                        String ehtSsName = feederName.split("-")[1].trim(); // safer split
                        List<FeederDataVo> feeders = arcGISDatabase.getFeerDataByName(ehtSsName);

                        if (!feeders.isEmpty()) {
                            ALLOW_SURVEY = feeders.get(0).getALLOW_SURVEY();
                            if (ALLOW_SURVEY.equalsIgnoreCase("N")) {
                                openAlertDialogNew(sp_feedername1);
                            }
                            //System.out.println("ALLOW_SURVEY --> " + ALLOW_SURVEY);
                        }
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void getSurveyData() {
        // FEEDER_NAME_33KV + " TEXT ," + FEEDER_CODE_33KV
        // feederId=arcGISDatabase.getFeederId(String.format("SELECT FEEDER_CODE_33KV FROM EHTMASTER_TABLE WHERE FEEDER_NAME_33KV='%s'",feederName ));
        try {
            progressDialog.show();
            String[] feederDta = feederName.split("-");
            feederCode = feederDta[0];
            feedeNameList = feederDta[1];
            LastSurveyRequestVo requestVo = new LastSurveyRequestVo();
            requestVo.setFEEDER_ID(feederCode);
            requestVo.setIMEI_NO(CommanUtilits.getIMEI_NO(SelectFeeder.this));
            requestVo.setSESSION_ID(sessionId);
            requestVo.setVERSION(MobileConstants.APP_VERSION_11_KV);
            Gson gson = new Gson();
            String json = gson.toJson(requestVo);

            // Log.v("DATA===>>", "" + json);
            retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
            Call<LastSurveyResponseSSDataVo> responseVoCall = retrofitService.getLastSurveyData(requestVo);
            responseVoCall.enqueue(new Callback<LastSurveyResponseSSDataVo>() {
                @Override
                public void onResponse(Call<LastSurveyResponseSSDataVo> call, Response<LastSurveyResponseSSDataVo> response) {

                    if (response.body() != null) {
                        Log.v("DATA===>>", "" + json);
                        LastSurveyResponseSSDataVo response_vo = response.body();
                        Gson gson = new Gson();
                        String json = gson.toJson(response_vo);
                        // progressDialog.dismiss();
                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
                            arcGISDatabase.clearSurveyedData();
                            arcGISDatabase.clearssMasterData();
                            List<SSMasterVo> ssList = response_vo.getSS_MASTER();
                            if (ssList != null && ssList.size() > 0) {
                                for (SSMasterVo ssMasterVo : ssList) {
                                    arcGISDatabase.addSSMasterData(ssMasterVo);
                                }
                            }

                            if ("No Records".equalsIgnoreCase(response_vo.getERR_MSG())) {
                                Intent intent = new Intent(SelectFeeder.this, DTRMasterDownloadActivity.class);
                                intent.putExtra("SSNAME", subName);
                                intent.putExtra("SSID", ssCode);
                                intent.putExtra("FEEDER_NAME", feedeNameList);
                                intent.putExtra("FEEDER_CODE", feederCode);
                                intent.putExtra("MAX_COUNT", response_vo.getSESSION_ID());
                                intent.putExtra("MULTI_CKT", sp_is_multi_circuit.getSelectedItem().toString());
                                startActivity(intent);
                                finish();
                            } else {
                                arcGISDatabase.clearSurveyedData();
                                List<LastSurveyResponseDataVo> masterResponseDataVos = response_vo.getRESPONSE();
                                for (LastSurveyResponseDataVo vo : masterResponseDataVos) {
                                    SurveyVo surveyVo = new SurveyVo();
                                    surveyVo.setPole_no(safeInt(vo.getPOLE_NO()));
                                    surveyVo.setFeeder_id(safeString(vo.getFEEDER_ID()));
                                    surveyVo.setLat(safeString(vo.getGLAT()));
                                    surveyVo.setLan(safeString(vo.getGLONG()));
                                    surveyVo.setPastSurveyFlag("Y");
                                    surveyVo.setPole_configuration(safeString(vo.getPOLE_CONFIG()));
                                    surveyVo.setVoltage_level(safeString(vo.getVOLTAGE_LEVEL()));
                                    surveyVo.setID(safeString(vo.getId()));
                                    surveyVo.setFrom_pole(safeInt(vo.getFROM_POLE()));
                                    surveyVo.setPole_type(safeString(vo.getPOLE_TYPE()));
                                    surveyVo.setPole_height(safeString(vo.getPOLE_HEIGHT()));
                                    surveyVo.setPole_support(safeString(vo.getPOLE_SUPPORT()));
                                    surveyVo.setPinpoint(safeString(vo.getPIN_CUT()));
                                    surveyVo.setPole_paintingcode(safeString(vo.getPOLE_PAINT_CODE()));
                                    surveyVo.setPOLE_CONCRETE(safeString(vo.getPOLE_CONCRETE()));
                                    surveyVo.setPoleStructure(safeString(vo.getPOLE_STRUCTURE()));
                                    arcGISDatabase.addSurveyData(surveyVo);
                                }
                                Intent intent = new Intent(SelectFeeder.this, DTRMasterDownloadActivity.class);
                                intent.putExtra("SSNAME", subName);
                                intent.putExtra("SSID", ssCode);
                                intent.putExtra("FEEDER_NAME", feedeNameList);
                                intent.putExtra("FEEDER_CODE", feederCode);
                                intent.putExtra("MAX_COUNT", response_vo.getSESSION_ID());
                                intent.putExtra("MULTI_CKT", sp_is_multi_circuit.getSelectedItem().toString());
                                startActivity(intent);
                                finish();
                            }
                            progressDialog.dismiss();
                        } else {
                            // Toast.makeText(MainActivity.this,response_vo.getERR_MSG(),Toast.LENGTH_SHORT).show();


                            if (response_vo.getAPK() != null) {
                                if (response_vo.getAPK().contains("http")) {
                               /* ApkDowanloadInstal apkDowanloadInstal = new ApkDowanloadInstal(response_vo.getAPK(), MainActivity.this);
                                apkDowanloadInstal.execute();*/
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SelectFeeder.this);
                                    alertDialogBuilder.setTitle("A new Version is available!");
                                    alertDialogBuilder
                                            .setMessage("Before upgrade, please transmit the data to server ")
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
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                }
                            } else {
                                Toast.makeText(SelectFeeder.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();

                            }
                            progressDialog.dismiss();
                        }
                    } else {
                        Toast.makeText(SelectFeeder.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LastSurveyResponseSSDataVo> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(SelectFeeder.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String safeString(String value) {
        return value == null ? "" : value;
    }

    private int safeInt(String value) {
        try {
            return value == null || value.isEmpty() ? 0 : Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }

}