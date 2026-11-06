package com.apepdcl.gis_survey.module_11KV.transmit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import android.widget.Toast;

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.HomeActivity;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.module_11KV.ArcGISDatabase;
import com.apepdcl.gis_survey.module_11KV.api_calls.RetrofitService;
import com.apepdcl.gis_survey.module_11KV.models.FeederDataVo;
import com.apepdcl.gis_survey.module_11KV.models.IncrementalSurveyVo;
import com.apepdcl.gis_survey.module_11KV.models.SurveyVo;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.vo.LoginDataResponse;

import java.util.List;

public class TransmitHomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_transmit, btn_incrementaltransmit;
    private ArcGISDatabase arcGISDatabase;
    private List<SurveyVo> surveyVoList, imageList, surveyVoEditLatLan, surveyVoListSourcePole,surveyVoListEditPoleAttributes;
    private List<IncrementalSurveyVo> incrementalSurveyVoList;
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
        setContentView(R.layout.activity_transmit_home);
        getSupportActionBar().hide();
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("11Kv Survey");
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent playIntent = new Intent(TransmitHomeActivity.this, HomeActivity.class); //Intent.ACTION_VIEW);
                    startActivity(playIntent);
                    finish();

                }
            });

            progressDialog = CommanUtilits.progressDialog(this);

            btn_transmit = (Button) findViewById(R.id.btn_transmit);
            btn_incrementaltransmit = (Button) findViewById(R.id.btn_incrementaltransmit);
            btn_transmit.setOnClickListener(this);
            btn_incrementaltransmit.setOnClickListener(this);
            arcGISDatabase = new ArcGISDatabase(this);
            surveyVoList = arcGISDatabase.getSurveyData("SELECT * FROM ARCGIS_TABLE WHERE FLAG1='N'");
            imageList = arcGISDatabase.getImageData("");
            surveyVoEditLatLan = arcGISDatabase.getEditLatLanData();
            surveyVoListSourcePole = arcGISDatabase.getEditSourcePoleSurveyData();
            surveyVoListEditPoleAttributes = arcGISDatabase.getEditPoleSurveyData();
            incrementalSurveyVoList = arcGISDatabase.getIncrementalSurveyData("SELECT * FROM INCREMENTAL_ARCGIS_TABLE WHERE FLAG1='N'");
            //getSupportActionBar().setTitle("11Kv LineSurvey Test");
            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            userName = preferences.getString(MobileConstants.USERNAME, "");
            sessionId = preferences.getString(MobileConstants.SESSION_ID, "");
            if (sessionId == null || sessionId.isEmpty()) {
                Intent intentLogin = new Intent(TransmitHomeActivity.this, LoginActivity.class);
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

                if (surveyVoList.size() > 0 || imageList.size() > 0 || surveyVoEditLatLan.size() > 0 || surveyVoListSourcePole.size() > 0 || surveyVoListEditPoleAttributes.size() >0) {
                    Intent intent = new Intent(TransmitHomeActivity.this, TransmitDataActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "No data to transmit", Toast.LENGTH_LONG).show();
                }

                break;

            case R.id.btn_incrementaltransmit:

                if (incrementalSurveyVoList.size() > 0) {
                   /* Intent intent = new Intent(TransmitHomeActivity.this, IncrementalTransmitDataActivity.class);
                    startActivity(intent);
                    finish();*/

                } else {
                    Toast.makeText(getApplicationContext(), "No data to transmit", Toast.LENGTH_LONG).show();
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