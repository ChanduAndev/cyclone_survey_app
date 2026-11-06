package com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.activitys;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.Constants.MobileConstants;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.retrofitservice.RetrofitClientInstance;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.retrofitservice.RetrofitService;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.utilits.CommanUtilits;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.FeederDataVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.LoginDataResponse;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.SurveyVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.SurveyedRequestDataVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.SurveyedRequestVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.SuveyedResponseVo;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransmitDataActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_escount;
    private Button btn_transmit;
    private ArcGISDatabase arcGISDatabase;
    private List<SurveyVo> surveyVoList;
    private RetrofitService retrofitService;
    private ProgressDialog progressDialog;
    private String SurveyFlag = "";
    private SharedPreferences preferences;
    private String userName, sessionId;
    private List<LoginDataResponse> dataResponseList;
    private List<FeederDataVo> feederDataVos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
        setContentView(R.layout.activity_transmitdata_11kv_oh_rdss);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("11Kv LineSurvey");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(TransmitDataActivity.this, FeederMasterActivity.class); //Intent.ACTION_VIEW);
                startActivity(playIntent);
                finish();

            }
        });

            progressDialog = CommanUtilits.progressDialog(this);
            tv_escount = (TextView) findViewById(R.id.tv_escount);
            btn_transmit = (Button) findViewById(R.id.btn_transmit);
            btn_transmit.setOnClickListener(this);
            arcGISDatabase = new ArcGISDatabase(this);
            surveyVoList = arcGISDatabase.getSurveyData("SELECT * FROM ARCGIS_TABLE WHERE FLAG1='N'");
            if (surveyVoList.size() > 0) {
                tv_escount.setText("No.of Surveyed Poles" + ":(" + surveyVoList.size() + ")");
            }
            //getSupportActionBar().setTitle("11Kv LineSurvey");
            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            userName = preferences.getString(MobileConstants.USERNAME, "");
            sessionId = preferences.getString(MobileConstants.SESSION_ID, "");
            if (sessionId == null || sessionId.isEmpty()) {
                Intent intentLogin = new Intent(TransmitDataActivity.this, LoginActivity.class);
                startActivity(intentLogin);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_transmit:
                if (ConnectivityChecking.checkConnection(TransmitDataActivity.this)) {
                    if (surveyVoList.size() > 0) {
                        transmitData();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }

                break;
        }
    }

    public void transmitData() {
        try {
            progressDialog.show();
            btn_transmit.setVisibility(View.GONE);
            SurveyedRequestVo surveyedRequestVo = new SurveyedRequestVo();
            List<SurveyedRequestDataVo> surveyedRequestDataVosList = new ArrayList<>();
            dataResponseList = arcGISDatabase.getUserMasterData();
            surveyedRequestVo.setUSER_ID(userName);
            surveyedRequestVo.setIMEI_NO(CommanUtilits.getIMEI_NO(TransmitDataActivity.this));
            surveyedRequestVo.setSESSION_ID(sessionId);
            surveyedRequestVo.setOFFICE_ID(dataResponseList.get(0).getOFFICE_ID());
            surveyedRequestVo.setIS_SURVEY_COM("N");
            surveyedRequestVo.setFEEDER_ID(surveyVoList.get(0).getFeeder_id());

            for (SurveyVo surveyVo : surveyVoList) {

                SurveyedRequestDataVo requestDataVo = new SurveyedRequestDataVo();
                requestDataVo.setFEEDER_ID(CommanUtilits.isChecking(surveyVo.getFeeder_id()));
                requestDataVo.setVOLTAGE_LEVEL(CommanUtilits.isChecking(surveyVo.getVoltage_level()));
                requestDataVo.setPOLE_NO(surveyVo.getPole_no());
                requestDataVo.setFROM_POLE(surveyVo.getFrom_pole());
                requestDataVo.setPOLE_TYPE(CommanUtilits.isChecking(surveyVo.getPole_type()));
                requestDataVo.setPOLE_HEIGHT(CommanUtilits.isChecking(surveyVo.getPole_height()));
                // requestDataVo.setNO_CIRCUITS(surveyVo.getNoof_circuits());
                requestDataVo.setPOLE_SUPPORT(CommanUtilits.isChecking(surveyVo.getPole_support()));
                requestDataVo.setTAPPING_POINT(CommanUtilits.isChecking(surveyVo.getTapping_point()));
                requestDataVo.setCONDUCTOR_SIZE(CommanUtilits.isChecking(surveyVo.getConductor_size()));
                requestDataVo.setPOLE_PAINT_CODE(CommanUtilits.isChecking(surveyVo.getPole_paintingcode()));
                requestDataVo.setSEC_CODE(CommanUtilits.isChecking(dataResponseList.get(0).getOFFICE_ID()));
                requestDataVo.setCREATED_BY(CommanUtilits.isChecking(userName));
                requestDataVo.setGLAT(surveyVo.getLat());
                requestDataVo.setGLONG(surveyVo.getLan());
                requestDataVo.setNO_CKT(CommanUtilits.isChecking(surveyVo.getIsMultiCircuit()));
                requestDataVo.setCKT_TYPE(CommanUtilits.isChecking(surveyVo.getCircuitType()));
                requestDataVo.setIME_NO(CommanUtilits.isChecking(CommanUtilits.getIMEI_NO(TransmitDataActivity.this)));
                requestDataVo.setPOLE_CONFIG(CommanUtilits.isChecking(surveyVo.getPole_configuration()));
                requestDataVo.setPIN_CUT(CommanUtilits.isChecking(surveyVo.getPinpoint()));
                requestDataVo.setSS_NAME(CommanUtilits.isChecking(surveyVo.getSs_name()));
                requestDataVo.setPOLE_STRUCTURE(CommanUtilits.isChecking(surveyVo.getPoleStructure()));
                requestDataVo.setDIFF_VOLTAGES(CommanUtilits.isChecking(surveyVo.getDiffVoltage()));

                if (surveyVo.getIsEndPole() != null) {
                    requestDataVo.setEND_POLE(CommanUtilits.isChecking(surveyVo.getIsEndPole()));
                } else {
                    requestDataVo.setEND_POLE("");
                }
                // requestDataVo.setEND_POLE(surveyVo.getIsEndPole());
                requestDataVo.setLINE_TYPE(CommanUtilits.isChecking(surveyVo.getLineType()));
                requestDataVo.setCKT_NO(CommanUtilits.isChecking(surveyVo.getNoofCircuits()));
                requestDataVo.setSS_ID(CommanUtilits.isChecking(surveyVo.getSsId()));
                requestDataVo.setSTJOINT_RMU(CommanUtilits.isChecking(surveyVo.getStjoint_rmu()));
                requestDataVo.setRMU_TYPE(CommanUtilits.isChecking(surveyVo.getRmu_type()));
                requestDataVo.setTAPPING_POINTS(CommanUtilits.isChecking(surveyVo.getNoofTappings()));
                requestDataVo.setTAPPING_LOCNAME(CommanUtilits.isChecking(surveyVo.getTappingLocation()));
                requestDataVo.setREMARKS(CommanUtilits.isChecking(surveyVo.getRemarks()));

                //New fileds add
                requestDataVo.setDTR_PHASE(CommanUtilits.isChecking(surveyVo.getDtr_phase()));
                requestDataVo.setDTR_CAPACITY(CommanUtilits.isChecking(surveyVo.getDtr_capacity()));
                requestDataVo.setDTR_CODE(CommanUtilits.isChecking(surveyVo.getDtr_code()));
                requestDataVo.setDTR_STRUCTURE(CommanUtilits.isChecking(surveyVo.getDtr_structure()));
                requestDataVo.setDTR_MAKE(CommanUtilits.isChecking(surveyVo.getDtr_make()));
                requestDataVo.setDTR_AB(CommanUtilits.isChecking(surveyVo.getDtr_ab_switch()));
                requestDataVo.setDTR_LOC_NAME(CommanUtilits.isChecking(surveyVo.getDtr_location_name()));
                requestDataVo.setDTR_SNO(CommanUtilits.isChecking(surveyVo.getDtr_scno()));
                requestDataVo.setNO_LT_CKT(CommanUtilits.isChecking(surveyVo.getDtr_no_of_circuit()));
                requestDataVo.setHT_SCNO(CommanUtilits.isChecking(surveyVo.getHt_service_number()));
                requestDataVo.setCAPACITY_RATING(CommanUtilits.isChecking(surveyVo.getPc_line_capacitor()));
                requestDataVo.setRECLOSER_TYPE(CommanUtilits.isChecking(surveyVo.getPc_auto_reclosure()));

                requestDataVo.setTYPE_OF_LOAD(CommanUtilits.isChecking(surveyVo.getType_of_load()));
                requestDataVo.setDTR_LOCATION_TYPE(CommanUtilits.isChecking(surveyVo.getDtr_location_type()));
                requestDataVo.setIS_LTCKT_AVAILABLE(CommanUtilits.isChecking(surveyVo.getIs_ltckt_avalable()));
                requestDataVo.setNO_OF_AB_SWITCHES(CommanUtilits.isChecking(surveyVo.getNo_of_ab_switches()));
                requestDataVo.setWO_ID(surveyVo.getWoId());
                requestDataVo.setIS_FEEDER_TYPE(surveyVo.getIsFeederType());

                requestDataVo.setSURVEY_TYPE(surveyVo.getSurvaytype());

                requestDataVo.setDTR_PRO_EXT(surveyVo.getDTR_PRO_EXT());
              //  requestDataVo.setESTIMATE_NO(surveyVo.getESTIMATE_NO());

                requestDataVo.setPOLE_SURVEY_TYPE(surveyVo.getPOLE_SURVEY_TYPE());
                requestDataVo.setFIXURE_TYPE(surveyVo.getFIXURE_TYPE());
                surveyedRequestDataVosList.add(requestDataVo);
            }
            surveyedRequestVo.setSURVEY_DATA(surveyedRequestDataVosList);

            Gson gson = new Gson();
            String json = gson.toJson(surveyedRequestVo);

            Log.v("okDATA===>>", "" + json);
            //  writeToFile(json);
            retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
            Call<SuveyedResponseVo> responseVoCall = retrofitService.transmitData(surveyedRequestVo);
            responseVoCall.enqueue(new Callback<SuveyedResponseVo>() {
                @Override
                public void onResponse(Call<SuveyedResponseVo> call, Response<SuveyedResponseVo> response) {
                    progressDialog.dismiss();
                    if (response.body() != null) {
                        SuveyedResponseVo response_vo = response.body();
                        progressDialog.dismiss();
                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
                            arcGISDatabase.clearSurveyedData();
                            Intent intent = new Intent(TransmitDataActivity.this, FeederMasterActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(TransmitDataActivity.this, "Data Transmited Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(TransmitDataActivity.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(TransmitDataActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SuveyedResponseVo> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(TransmitDataActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
       /* Intent intent = new Intent(TransmitDataActivity.this, FeederMasterActivity.class);
        startActivity(intent);
        finish();*/
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
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
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public void writeToFile(String data) {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c);
        // Get the directory for the user's public pictures directory.
        final File path =
                Environment.getExternalStoragePublicDirectory
                        (
                                //Environment.DIRECTORY_PICTURES
                                Environment.DIRECTORY_DCIM + "/EPDCLGIS11KV/"
                        );

        // Make sure the path directory exists.
        if (!path.exists()) {
            // Make it, if it doesn't exit
            path.mkdirs();
        }
        final File file = new File(path, formattedDate + "gisdislinedata11kv.txt");
        // Save your stream, don't forget to flush() it before closing it.
        try {
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(data);
            myOutWriter.close();
            Toast.makeText(TransmitDataActivity.this, "" + file, Toast.LENGTH_SHORT).show();
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
            Toast.makeText(TransmitDataActivity.this, "File write failed: " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}



