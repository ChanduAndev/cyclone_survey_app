package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.incremental;

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
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.activity.FeederMasterRDSSUg11KvActivity;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.constants.MobileConstantsRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.retrofitservice.responsevo.RetrofitClientInstanceRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.retrofitservice.responsevo.RetrofitServiceRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.runtimepermessions.ConnectivityCheckingRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.sqldatabase.ArcGISDatabaseRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.utilities.CommanUtilitsRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.FeederDataVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.IncrementalFeederDataVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.IncrementalSurveyVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.IncrementalSurveyedRequestDataVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.IncrementalSurveyedRequestVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.LoginDataResponseRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.SuveyedResponseVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.WoStatusMasterVoRDSSUg11Kv;
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

public class IncrementalTransmitDataRDSSUg11KvActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_escount;
    private Button btn_transmit;
    private ArcGISDatabaseRDSSUg11Kv arcGISDatabaseRDSSUg11Kv;
    private List<IncrementalSurveyVoRDSSUg11Kv> surveyVoList;
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
        setContentView(R.layout.activity_incremental_transmitdata_rdss_ug_11kv);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("11KV UG Cable Survey");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(IncrementalTransmitDataRDSSUg11KvActivity.this, FeederMasterRDSSUg11KvActivity.class); //Intent.ACTION_VIEW);
                startActivity(playIntent);
                finish();

            }
        });

            progressDialog = CommanUtilitsRDSSUg11Kv.progressDialog(this);
            tv_escount = (TextView) findViewById(R.id.tv_escount);
            btn_transmit = (Button) findViewById(R.id.btn_transmit);
            btn_transmit.setOnClickListener(this);
            arcGISDatabaseRDSSUg11Kv = new ArcGISDatabaseRDSSUg11Kv(this);
            surveyVoList = arcGISDatabaseRDSSUg11Kv.getIncrementalSurveyData("SELECT * FROM INCREMENTAL_ARCGIS_TABLE WHERE FLAG1='N'");
            if (surveyVoList.size() > 0) {
                tv_escount.setText("No.of Surveyed Poles" + ":(" + surveyVoList.size() + ")");
            }
            //getSupportActionBar().setTitle("11Kv LineSurvey");
            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            userName = preferences.getString(MobileConstantsRDSSUg11Kv.USERNAME, "");
            sessionId = preferences.getString(MobileConstantsRDSSUg11Kv.SESSION_ID, "");
            if (sessionId == null || sessionId.isEmpty()) {
                Intent intentLogin = new Intent(IncrementalTransmitDataRDSSUg11KvActivity.this, LoginActivity.class);
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
                if (ConnectivityCheckingRDSSUg11Kv.checkConnection(IncrementalTransmitDataRDSSUg11KvActivity.this)) {
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
            List<IncrementalFeederDataVoRDSSUg11Kv> incrementalFeederDataVoRDSSUg11KvList =new ArrayList<>();
            incrementalFeederDataVoRDSSUg11KvList = arcGISDatabaseRDSSUg11Kv.getIncrementalFeerData();
            IncrementalSurveyedRequestVoRDSSUg11Kv surveyedRequestVo = new IncrementalSurveyedRequestVoRDSSUg11Kv();
            List<IncrementalSurveyedRequestDataVoRDSSUg11Kv> surveyedRequestDataVosList = new ArrayList<>();

            List<WoStatusMasterVoRDSSUg11Kv> woStatusMasterVosListRDSSUg11Kv =new ArrayList<>();
            woStatusMasterVosListRDSSUg11Kv = arcGISDatabaseRDSSUg11Kv.getWoStatusMasterData();

            dataResponseList = arcGISDatabaseRDSSUg11Kv.getUserMasterData();
            surveyedRequestVo.setUSER_ID(userName);
            surveyedRequestVo.setIMEI_NO(CommanUtilitsRDSSUg11Kv.getIMEI_NO(IncrementalTransmitDataRDSSUg11KvActivity.this));
            surveyedRequestVo.setSESSION_ID(sessionId);
            surveyedRequestVo.setOFFICE_ID(dataResponseList.get(0).getOFFICE_ID());
            surveyedRequestVo.setIS_SURVEY_COM("N");
            surveyedRequestVo.setFEEDER_ID(surveyVoList.get(0).getFeeder_id());

            WoStatusMasterVoRDSSUg11Kv woStatusMasterVoRDSSUg11Kv =new WoStatusMasterVoRDSSUg11Kv();
            woStatusMasterVoRDSSUg11Kv.setFEEDER_ID(woStatusMasterVosListRDSSUg11Kv.get(0).getFEEDER_ID());
            woStatusMasterVoRDSSUg11Kv.setWO_ID(woStatusMasterVosListRDSSUg11Kv.get(0).getWO_ID());
            woStatusMasterVoRDSSUg11Kv.setWO_STATUS(woStatusMasterVosListRDSSUg11Kv.get(0).getWO_STATUS());
            List<WoStatusMasterVoRDSSUg11Kv> masterVosList=new ArrayList<>();
            masterVosList.add(woStatusMasterVoRDSSUg11Kv);

            for (IncrementalSurveyVoRDSSUg11Kv surveyVo : surveyVoList) {
                IncrementalSurveyedRequestDataVoRDSSUg11Kv requestDataVo = new IncrementalSurveyedRequestDataVoRDSSUg11Kv();
                requestDataVo.setFEEDER_ID(CommanUtilitsRDSSUg11Kv.isChecking(surveyVo.getFeeder_id()));
                requestDataVo.setVOLTAGE_LEVEL(CommanUtilitsRDSSUg11Kv.isChecking(surveyVo.getVoltage_level()));
                requestDataVo.setPOLE_NO(surveyVo.getPole_no());
                requestDataVo.setFROM_POLE(surveyVo.getFrom_pole());
                requestDataVo.setPOLE_TYPE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVo.getPole_type()));
                requestDataVo.setPOLE_HEIGHT(CommanUtilitsRDSSUg11Kv.isChecking(surveyVo.getPole_height()));
                // requestDataVo.setNO_CIRCUITS(surveyVo.getNoof_circuits());
                requestDataVo.setPOLE_SUPPORT(CommanUtilitsRDSSUg11Kv.isChecking(surveyVo.getPole_support()));
                requestDataVo.setTAPPING_POINT(CommanUtilitsRDSSUg11Kv.isChecking(surveyVo.getTapping_point()));
                requestDataVo.setCONDUCTOR_SIZE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVo.getConductor_size()));
                requestDataVo.setPOLE_PAINT_CODE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVo.getPole_paintingcode()));
                requestDataVo.setSEC_CODE(CommanUtilitsRDSSUg11Kv.isChecking(dataResponseList.get(0).getOFFICE_ID()));
                requestDataVo.setCREATED_BY(CommanUtilitsRDSSUg11Kv.isChecking(userName));
                requestDataVo.setGLAT(surveyVo.getLat());
                requestDataVo.setGLONG(surveyVo.getLan());
                requestDataVo.setNO_CKT(CommanUtilitsRDSSUg11Kv.isChecking(surveyVo.getIsMultiCircuit()));
                requestDataVo.setCKT_TYPE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVo.getCircuitType()));
                requestDataVo.setIME_NO(CommanUtilitsRDSSUg11Kv.isChecking(CommanUtilitsRDSSUg11Kv.getIMEI_NO(IncrementalTransmitDataRDSSUg11KvActivity.this)));
                requestDataVo.setPOLE_CONFIG(CommanUtilitsRDSSUg11Kv.isChecking(surveyVo.getPole_configuration()));
                requestDataVo.setPIN_CUT(CommanUtilitsRDSSUg11Kv.isChecking(surveyVo.getPinpoint()));
                requestDataVo.setSS_NAME(CommanUtilitsRDSSUg11Kv.isChecking(surveyVo.getSs_name()));
                requestDataVo.setPOLE_STRUCTURE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVo.getPoleStructure()));
                requestDataVo.setDIFF_VOLTAGES(CommanUtilitsRDSSUg11Kv.isChecking(surveyVo.getDiffVoltage()));

                if (surveyVo.getIsEndPole() != null) {
                    requestDataVo.setEND_POLE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVo.getIsEndPole()));
                } else {
                    requestDataVo.setEND_POLE("");
                }
                // requestDataVo.setEND_POLE(surveyVo.getIsEndPole());
                requestDataVo.setLINE_TYPE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVo.getLineType()));
                requestDataVo.setCKT_NO(CommanUtilitsRDSSUg11Kv.isChecking(surveyVo.getNoofCircuits()));
                requestDataVo.setSS_ID(CommanUtilitsRDSSUg11Kv.isChecking(surveyVo.getSsId()));
                requestDataVo.setSTJOINT_RMU(CommanUtilitsRDSSUg11Kv.isChecking(surveyVo.getStjoint_rmu()));
                requestDataVo.setRMU_TYPE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVo.getRmu_type()));
                requestDataVo.setTAPPING_POINTS(CommanUtilitsRDSSUg11Kv.isChecking(surveyVo.getNoofTappings()));
                requestDataVo.setTAPPING_LOCNAME(CommanUtilitsRDSSUg11Kv.isChecking(surveyVo.getTappingLocation()));
                requestDataVo.setREMARKS(CommanUtilitsRDSSUg11Kv.isChecking(surveyVo.getRemarks()));

                //New fileds add
                requestDataVo.setDTR_PHASE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVo.getDtr_phase()));
                requestDataVo.setDTR_CAPACITY(CommanUtilitsRDSSUg11Kv.isChecking(surveyVo.getDtr_capacity()));
                requestDataVo.setDTR_CODE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVo.getDtr_code()));
                requestDataVo.setDTR_STRUCTURE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVo.getDtr_structure()));
                requestDataVo.setDTR_MAKE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVo.getDtr_make()));
                requestDataVo.setDTR_AB(CommanUtilitsRDSSUg11Kv.isChecking(surveyVo.getDtr_ab_switch()));
                requestDataVo.setDTR_LOC_NAME(CommanUtilitsRDSSUg11Kv.isChecking(surveyVo.getDtr_location_name()));
                requestDataVo.setDTR_SNO(CommanUtilitsRDSSUg11Kv.isChecking(surveyVo.getDtr_scno()));
                requestDataVo.setNO_LT_CKT(CommanUtilitsRDSSUg11Kv.isChecking(surveyVo.getDtr_no_of_circuit()));
                requestDataVo.setHT_SCNO(CommanUtilitsRDSSUg11Kv.isChecking(surveyVo.getHt_service_number()));
                requestDataVo.setCAPACITY_RATING(CommanUtilitsRDSSUg11Kv.isChecking(surveyVo.getPc_line_capacitor()));
                requestDataVo.setRECLOSER_TYPE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVo.getPc_auto_reclosure()));

                requestDataVo.setTYPE_OF_LOAD(CommanUtilitsRDSSUg11Kv.isChecking(surveyVo.getType_of_load()));
                requestDataVo.setDTR_LOCATION_TYPE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVo.getDtr_location_type()));
                requestDataVo.setIS_LTCKT_AVAILABLE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVo.getIs_ltckt_avalable()));
                //requestDataVo.setNO_OF_AB_SWITCHES(CommanUtilits.isChecking(surveyVo.getNo_of_ab_switches()));
                requestDataVo.setWO_ID(surveyVo.getWorkOrder());
                //requestDataVo.setWO_STATUS(incrementalFeederDataVoList.get(0).getSTATUS());
                requestDataVo.setWO_STATUS(surveyVo.getWoStatus());
                requestDataVo.setIS_FEEDER_TYPE(surveyVo.getIsFeederType());
                surveyedRequestDataVosList.add(requestDataVo);
            }
            surveyedRequestVo.setSURVEY_DATA(surveyedRequestDataVosList);
            surveyedRequestVo.setWORK_ORDER(masterVosList);

            Gson gson = new Gson();
            String json = gson.toJson(surveyedRequestVo);

            Log.v("POSTDATA===>>", "" + json);
            //  writeToFile(json);
            retrofitServiceRDSSUg11Kv = RetrofitClientInstanceRDSSUg11Kv.getRetrofitInstance().create(RetrofitServiceRDSSUg11Kv.class);
            Call<SuveyedResponseVoRDSSUg11Kv> responseVoCall = retrofitServiceRDSSUg11Kv.transmitIncrementalSurveyData(surveyedRequestVo);
            responseVoCall.enqueue(new Callback<SuveyedResponseVoRDSSUg11Kv>() {
                @Override
                public void onResponse(Call<SuveyedResponseVoRDSSUg11Kv> call, Response<SuveyedResponseVoRDSSUg11Kv> response) {
                    progressDialog.dismiss();
                    if (response.body() != null) {
                        SuveyedResponseVoRDSSUg11Kv response_vo = response.body();
                        progressDialog.dismiss();
                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
                            arcGISDatabaseRDSSUg11Kv.clearIncrementalSurveyedData();
                            Intent intent = new Intent(IncrementalTransmitDataRDSSUg11KvActivity.this, FeederMasterRDSSUg11KvActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(IncrementalTransmitDataRDSSUg11KvActivity.this, "Data Transmited Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(IncrementalTransmitDataRDSSUg11KvActivity.this, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(IncrementalTransmitDataRDSSUg11KvActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SuveyedResponseVoRDSSUg11Kv> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(IncrementalTransmitDataRDSSUg11KvActivity.this, "Try Again", Toast.LENGTH_LONG).show();
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
            Toast.makeText(IncrementalTransmitDataRDSSUg11KvActivity.this, "" + file, Toast.LENGTH_SHORT).show();
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
            Toast.makeText(IncrementalTransmitDataRDSSUg11KvActivity.this, "File write failed: " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}



