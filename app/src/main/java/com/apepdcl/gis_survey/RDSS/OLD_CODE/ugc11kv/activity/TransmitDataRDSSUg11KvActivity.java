package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.activity;

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

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.constants.MobileConstantsRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.retrofitservice.responsevo.RetrofitClientInstanceRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.retrofitservice.responsevo.RetrofitServiceRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.runtimepermessions.ConnectivityCheckingRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.sqldatabase.ArcGISDatabaseRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.utilities.CommanUtilitsRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.FeederDataVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.LoginDataResponseRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.SurveyVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.SurveyedRequestDataVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.SurveyedRequestVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.SuveyedResponseVoRDSSUg11Kv;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
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

public class TransmitDataRDSSUg11KvActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_escount;
    private Button btn_transmit;
    private ArcGISDatabaseRDSSUg11Kv arcGISDatabaseRDSSUg11Kv;
    private List<SurveyVoRDSSUg11Kv> surveyVoRDSSUg11KvList;
    private RetrofitServiceRDSSUg11Kv retrofitServiceRDSSUg11Kv;
    private ProgressDialog progressDialog;
    private String SurveyFlag = "";
    private SharedPreferences preferences;
    private String userName, sessionId;
    private List<LoginDataResponseRDSSUg11Kv> dataResponseList;
    private List<FeederDataVoRDSSUg11Kv> feederDataVoRDSSUg11Kvs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
        setContentView(R.layout.activity_transmitdata_rdss_ug_11kv);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("11KV UG Cable Survey");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(TransmitDataRDSSUg11KvActivity.this, FeederMasterRDSSUg11KvActivity.class); //Intent.ACTION_VIEW);
                startActivity(playIntent);
                finish();

            }
        });

            progressDialog = CommanUtilitsRDSSUg11Kv.progressDialog(this);
            tv_escount = (TextView) findViewById(R.id.tv_escount);
            btn_transmit = (Button) findViewById(R.id.btn_transmit);
            btn_transmit.setOnClickListener(this);
            arcGISDatabaseRDSSUg11Kv = new ArcGISDatabaseRDSSUg11Kv(this);
            surveyVoRDSSUg11KvList = arcGISDatabaseRDSSUg11Kv.getSurveyData("SELECT * FROM ARCGIS_TABLE WHERE FLAG1='N'");
            if (surveyVoRDSSUg11KvList.size() > 0) {
                tv_escount.setText("No.of Surveyed Poles" + ":(" + surveyVoRDSSUg11KvList.size() + ")");
            }
            //getSupportActionBar().setTitle("11Kv LineSurvey");
            preferences = PreferenceManager.getDefaultSharedPreferences(TransmitDataRDSSUg11KvActivity.this);
            userName = preferences.getString(MobileConstantsRDSSUg11Kv.USERNAME, "");
            sessionId = preferences.getString(MobileConstantsRDSSUg11Kv.SESSION_ID, "");
            if (sessionId == null || sessionId.isEmpty()) {
                Intent intentLogin = new Intent(TransmitDataRDSSUg11KvActivity.this, LoginActivity.class);
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
                if (ConnectivityCheckingRDSSUg11Kv.checkConnection(TransmitDataRDSSUg11KvActivity.this)) {
                    if (surveyVoRDSSUg11KvList.size() > 0) {
                        transmitData();
                    }
                } else {
                    CommanUtilits.internetDialog(TransmitDataRDSSUg11KvActivity.this);
                   // Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
                }

                break;
        }
    }

    public void transmitData() {
        try {
            progressDialog.show();
            btn_transmit.setVisibility(View.GONE);
            SurveyedRequestVoRDSSUg11Kv surveyedRequestVoRDSSUg11Kv = new SurveyedRequestVoRDSSUg11Kv();
            List<SurveyedRequestDataVoRDSSUg11Kv> surveyedRequestDataVosListRDSSUg11Kv = new ArrayList<>();
            dataResponseList = arcGISDatabaseRDSSUg11Kv.getUserMasterData();
            surveyedRequestVoRDSSUg11Kv.setUSER_ID(userName);
            surveyedRequestVoRDSSUg11Kv.setIMEI_NO(CommanUtilitsRDSSUg11Kv.getIMEI_NO(TransmitDataRDSSUg11KvActivity.this));
            surveyedRequestVoRDSSUg11Kv.setSESSION_ID(sessionId);
            surveyedRequestVoRDSSUg11Kv.setOFFICE_ID(dataResponseList.get(0).getOFFICE_ID());
            surveyedRequestVoRDSSUg11Kv.setIS_SURVEY_COM("N");
            surveyedRequestVoRDSSUg11Kv.setFEEDER_ID(surveyVoRDSSUg11KvList.get(0).getFeeder_id());

            for (SurveyVoRDSSUg11Kv surveyVoRDSSUg11Kv : surveyVoRDSSUg11KvList) {

                SurveyedRequestDataVoRDSSUg11Kv requestDataVo = new SurveyedRequestDataVoRDSSUg11Kv();
                requestDataVo.setFEEDER_ID(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getFeeder_id()));
                requestDataVo.setVOLTAGE_LEVEL(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getVoltage_level()));
                requestDataVo.setPOLE_NO(surveyVoRDSSUg11Kv.getPole_no());
                requestDataVo.setFROM_POLE(surveyVoRDSSUg11Kv.getFrom_pole());
                requestDataVo.setPOLE_TYPE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getPole_type()));
                requestDataVo.setPOLE_HEIGHT(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getPole_height()));
                // requestDataVo.setNO_CIRCUITS(surveyVo.getNoof_circuits());
                requestDataVo.setPOLE_SUPPORT(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getPole_support()));
                requestDataVo.setTAPPING_POINT(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getTapping_point()));
                requestDataVo.setCONDUCTOR_SIZE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getConductor_size()));
                requestDataVo.setPOLE_PAINT_CODE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getPole_paintingcode()));
                requestDataVo.setSEC_CODE(CommanUtilitsRDSSUg11Kv.isChecking(dataResponseList.get(0).getOFFICE_ID()));
                requestDataVo.setCREATED_BY(CommanUtilitsRDSSUg11Kv.isChecking(userName));
                requestDataVo.setGLAT(surveyVoRDSSUg11Kv.getLat());
                requestDataVo.setGLONG(surveyVoRDSSUg11Kv.getLan());
                requestDataVo.setNO_CKT(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getIsMultiCircuit()));
                requestDataVo.setCKT_TYPE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getCircuitType()));
                requestDataVo.setIME_NO(CommanUtilitsRDSSUg11Kv.isChecking(CommanUtilitsRDSSUg11Kv.getIMEI_NO(TransmitDataRDSSUg11KvActivity.this)));
                requestDataVo.setPOLE_CONFIG(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getPole_configuration()));
                requestDataVo.setPIN_CUT(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getPinpoint()));
                requestDataVo.setSS_NAME(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getSs_name()));
                requestDataVo.setPOLE_STRUCTURE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getPoleStructure()));
                requestDataVo.setDIFF_VOLTAGES(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getDiffVoltage()));

                if (surveyVoRDSSUg11Kv.getIsEndPole() != null) {
                    requestDataVo.setEND_POLE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getIsEndPole()));
                } else {
                    requestDataVo.setEND_POLE("");
                }
                // requestDataVo.setEND_POLE(surveyVo.getIsEndPole());
                requestDataVo.setLINE_TYPE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getLineType()));
                requestDataVo.setCKT_NO(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getNoofCircuits()));
                requestDataVo.setSS_ID(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getSsId()));
                requestDataVo.setSTJOINT_RMU(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getStjoint_rmu()));
                requestDataVo.setRMU_TYPE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getRmu_type()));
                requestDataVo.setRMU_SURVEY_TYPE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getRMU_SURVEY_TYPE()));
                requestDataVo.setTAPPING_POINTS(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getNoofTappings()));
                requestDataVo.setTAPPING_LOCNAME(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getTappingLocation()));
                requestDataVo.setREMARKS(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getRemarks()));

                //New fileds add
                requestDataVo.setDTR_PHASE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getDtr_phase()));
                requestDataVo.setDTR_CAPACITY(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getDtr_capacity()));
                requestDataVo.setDTR_CODE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getDtr_code()));
                requestDataVo.setDTR_STRUCTURE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getDtr_structure()));
                requestDataVo.setDTR_MAKE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getDtr_make()));
                requestDataVo.setDTR_AB(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getDtr_ab_switch()));
                requestDataVo.setDTR_LOC_NAME(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getDtr_location_name()));
                requestDataVo.setDTR_SNO(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getDtr_scno()));
                requestDataVo.setNO_LT_CKT(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getDtr_no_of_circuit()));
                requestDataVo.setHT_SCNO(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getHt_service_number()));
                requestDataVo.setCAPACITY_RATING(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getPc_line_capacitor()));
                requestDataVo.setRECLOSER_TYPE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getPc_auto_reclosure()));

                requestDataVo.setTYPE_OF_LOAD(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getType_of_load()));
                requestDataVo.setDTR_LOCATION_TYPE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getDtr_location_type()));
                requestDataVo.setIS_LTCKT_AVAILABLE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getIs_ltckt_avalable()));
                requestDataVo.setNO_OF_AB_SWITCHES(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getNo_of_ab_switches()));
                requestDataVo.setWO_ID(surveyVoRDSSUg11Kv.getWoId());
                requestDataVo.setIS_FEEDER_TYPE(surveyVoRDSSUg11Kv.getIsFeederType());

                requestDataVo.setSURVEY_TYPE(surveyVoRDSSUg11Kv.getSurvaytype());

                requestDataVo.setDTR_PRO_EXT(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getDTR_PRO_EXT()));
              //  requestDataVo.setESTIMATE_NO(surveyVo.getESTIMATE_NO());

                requestDataVo.setPOLE_SURVEY_TYPE(surveyVoRDSSUg11Kv.getPOLE_SURVEY_TYPE());
                requestDataVo.setFIXURE_TYPE(surveyVoRDSSUg11Kv.getFIXURE_TYPE());

                requestDataVo.setLAYOUT_ID(surveyVoRDSSUg11Kv.getLAYOUT_ID());
                requestDataVo.setLAYOUT_NAME(surveyVoRDSSUg11Kv.getLAYOUT_NAME());

                surveyedRequestDataVosListRDSSUg11Kv.add(requestDataVo);
            }
            surveyedRequestVoRDSSUg11Kv.setSURVEY_DATA(surveyedRequestDataVosListRDSSUg11Kv);

            Gson gson = new Gson();
            String json = gson.toJson(surveyedRequestVoRDSSUg11Kv);

            Log.v("okDATA===>>", "" + json);
            //  writeToFile(json);
            retrofitServiceRDSSUg11Kv = RetrofitClientInstanceRDSSUg11Kv.getRetrofitInstance().create(RetrofitServiceRDSSUg11Kv.class);
            Call<SuveyedResponseVoRDSSUg11Kv> responseVoCall = retrofitServiceRDSSUg11Kv.transmitData(surveyedRequestVoRDSSUg11Kv);
            responseVoCall.enqueue(new Callback<SuveyedResponseVoRDSSUg11Kv>() {
                @Override
                public void onResponse(Call<SuveyedResponseVoRDSSUg11Kv> call, Response<SuveyedResponseVoRDSSUg11Kv> response) {
                    progressDialog.dismiss();
                    if (response.body() != null) {
                        SuveyedResponseVoRDSSUg11Kv response_vo = response.body();
                        progressDialog.dismiss();
                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
                            arcGISDatabaseRDSSUg11Kv.clearSurveyedData();
                            Intent intent = new Intent(TransmitDataRDSSUg11KvActivity.this, FeederMasterRDSSUg11KvActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(TransmitDataRDSSUg11KvActivity.this, "Data Transmited Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(TransmitDataRDSSUg11KvActivity.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(TransmitDataRDSSUg11KvActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SuveyedResponseVoRDSSUg11Kv> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(TransmitDataRDSSUg11KvActivity.this, "Try Again", Toast.LENGTH_LONG).show();
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
            Toast.makeText(TransmitDataRDSSUg11KvActivity.this, "" + file, Toast.LENGTH_SHORT).show();
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
            Toast.makeText(TransmitDataRDSSUg11KvActivity.this, "File write failed: " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}



