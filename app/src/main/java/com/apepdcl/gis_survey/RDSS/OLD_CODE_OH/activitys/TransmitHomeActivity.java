package com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.activitys;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.Constants.MobileConstants;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.incremetal.IncrementalTransmitDataActivity;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.retrofitservice.RetrofitService;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.utilits.CommanUtilits;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.FeederDataVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.IncrementalSurveyVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.LoginDataResponse;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.SurveyVo;

import java.util.List;

public class TransmitHomeActivity extends AppCompatActivity implements View.OnClickListener {
    //private TextView tv_escount;
    private Button btn_transmit,btn_incrementaltransmit;
    private ArcGISDatabase arcGISDatabase;
    private List<SurveyVo> surveyVoList;
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
        try {
            setContentView(R.layout.activity_transmithome_11kv_oh_rdss);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("11Kv LineSurvey");
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent playIntent = new Intent(TransmitHomeActivity.this, FeederMasterActivity.class); //Intent.ACTION_VIEW);
                    startActivity(playIntent);
                    finish();

                }
            });

            progressDialog = CommanUtilits.progressDialog(this);

            btn_transmit = (Button) findViewById(R.id.btn_transmit);
            btn_incrementaltransmit= (Button) findViewById(R.id.btn_incrementaltransmit);
            btn_transmit.setOnClickListener(this);
            btn_incrementaltransmit.setOnClickListener(this);
            arcGISDatabase = new ArcGISDatabase(this);
            surveyVoList = arcGISDatabase.getSurveyData("SELECT * FROM ARCGIS_TABLE WHERE FLAG1='N'");
            incrementalSurveyVoList=arcGISDatabase.getIncrementalSurveyData("SELECT * FROM INCREMENTAL_ARCGIS_TABLE WHERE FLAG1='N'");
            //getSupportActionBar().setTitle("11Kv LineSurvey");
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

                if (surveyVoList.size() > 0) {
                    Intent intent = new Intent(TransmitHomeActivity.this, TransmitDataActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "No data to transmit", Toast.LENGTH_LONG).show();
                }

                break;

            case R.id.btn_incrementaltransmit:

                if (incrementalSurveyVoList.size() > 0) {
                    Intent intent = new Intent(TransmitHomeActivity.this, IncrementalTransmitDataActivity.class);
                    startActivity(intent);
                    finish();

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



