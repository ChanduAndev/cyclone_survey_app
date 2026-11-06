package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.constants.MobileConstantsRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.incremental.IncrementalTransmitDataRDSSUg11KvActivity;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.retrofitservice.responsevo.RetrofitServiceRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.sqldatabase.ArcGISDatabaseRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.utilities.CommanUtilitsRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.FeederDataVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.IncrementalSurveyVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.LoginDataResponseRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.SurveyVoRDSSUg11Kv;

import java.util.List;

public class TransmitHomeRDSSUg11KvActivity extends AppCompatActivity implements View.OnClickListener {
    //private TextView tv_escount;
    private TextView btn_transmit, btn_incrementaltransmit;
    private ArcGISDatabaseRDSSUg11Kv arcGISDatabaseRDSSUg11Kv;
    private List<SurveyVoRDSSUg11Kv> surveyVoRDSSUg11KvList;
    private List<IncrementalSurveyVoRDSSUg11Kv> incrementalSurveyVoRDSSUg11KvList;
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
            setContentView(R.layout.activity_transmithome_rdss_ug_11kv);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("11KV UG Cable Survey");
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent playIntent = new Intent(TransmitHomeRDSSUg11KvActivity.this, FeederMasterRDSSUg11KvActivity.class); //Intent.ACTION_VIEW);
                    startActivity(playIntent);
                    finish();

                }
            });

            progressDialog = CommanUtilitsRDSSUg11Kv.progressDialog(this);

            btn_transmit = findViewById(R.id.btn_transmit);
            btn_incrementaltransmit = findViewById(R.id.btn_incrementaltransmit);
            btn_transmit.setOnClickListener(this);
            btn_incrementaltransmit.setOnClickListener(this);
            arcGISDatabaseRDSSUg11Kv = new ArcGISDatabaseRDSSUg11Kv(this);
            surveyVoRDSSUg11KvList = arcGISDatabaseRDSSUg11Kv.getSurveyData("SELECT * FROM ARCGIS_TABLE WHERE FLAG1='N'");
            incrementalSurveyVoRDSSUg11KvList = arcGISDatabaseRDSSUg11Kv.getIncrementalSurveyData("SELECT * FROM INCREMENTAL_ARCGIS_TABLE WHERE FLAG1='N'");
            //getSupportActionBar().setTitle("11Kv LineSurvey");
            preferences = PreferenceManager.getDefaultSharedPreferences(TransmitHomeRDSSUg11KvActivity.this);
            userName = preferences.getString(MobileConstantsRDSSUg11Kv.USERNAME, "");
            sessionId = preferences.getString(MobileConstantsRDSSUg11Kv.SESSION_ID, "");
            if (sessionId == null || sessionId.isEmpty()) {
                Intent intentLogin = new Intent(TransmitHomeRDSSUg11KvActivity.this, LoginActivity.class);
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

                if (surveyVoRDSSUg11KvList.size() > 0) {
                    Intent intent = new Intent(TransmitHomeRDSSUg11KvActivity.this, TransmitDataRDSSUg11KvActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(TransmitHomeRDSSUg11KvActivity.this, "No data to transmit", Toast.LENGTH_LONG).show();
                }

                break;

            case R.id.btn_incrementaltransmit:

                if (incrementalSurveyVoRDSSUg11KvList.size() > 0) {
                    Intent intent = new Intent(TransmitHomeRDSSUg11KvActivity.this, IncrementalTransmitDataRDSSUg11KvActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(TransmitHomeRDSSUg11KvActivity.this, "No data to transmit", Toast.LENGTH_LONG).show();
                }

                break;
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

}



