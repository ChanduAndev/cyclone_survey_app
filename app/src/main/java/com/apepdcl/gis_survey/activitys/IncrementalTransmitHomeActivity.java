package com.apepdcl.gis_survey.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.HomeActivity;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.incremental.TransMitIncrementalSurveyActivity;
import com.apepdcl.gis_survey.module_11KV.transmit.TransmitHomeActivity;
import com.apepdcl.gis_survey.retrofitservice.RetrofitService;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.vo.DTRDetailsResponseVo;
import com.apepdcl.gis_survey.vo.DTRSurveyVo;
import com.apepdcl.gis_survey.vo.IncrementalSurveyDataVo;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.apepdcl.gis_survey.vo.SurveyDataVo;

import java.util.ArrayList;
import java.util.List;

public class IncrementalTransmitHomeActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_goto_ltsurvey,btn_goto_incremental_ltsurvey,btn_goto_11_kv_survey;
     private List<SurveyDataVo> surveyDataVosList;
    private List<IncrementalSurveyDataVo> incrementalSurveyDataVoList;
    private List<DTRDetailsResponseVo> dtrDetailsResponseVoList=null;
    private List<DTRSurveyVo> dtrSurveyVoList=null;
    private List<LoginDataResponse> loginDataResponses;
    private ArcGISDatabase arcGISDatabase;
    private RetrofitService retrofitService;
    private ProgressDialog progressDialog;
    private String userName;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incr_transmit_home);
        btn_goto_ltsurvey=findViewById(R.id.btn_goto_ltsurvey);
        btn_goto_incremental_ltsurvey=findViewById(R.id.btn_goto_incremental_ltsurvey);
        btn_goto_11_kv_survey=findViewById(R.id.btn_goto_11_kv_survey);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wail...");
        progressDialog.setCancelable(false);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("11KV/LT GIS Survey");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(IncrementalTransmitHomeActivity.this, HomeActivity.class); //Intent.ACTION_VIEW);
                startActivity(playIntent);
                finish();

            }
        });
        preferences= PreferenceManager.getDefaultSharedPreferences(IncrementalTransmitHomeActivity.this);
        userName=preferences.getString(MobileConstants.USERNAME,"");

        btn_goto_ltsurvey.setOnClickListener(this);
        btn_goto_incremental_ltsurvey.setOnClickListener(this);
        btn_goto_11_kv_survey.setOnClickListener(this);
        arcGISDatabase=new ArcGISDatabase(this);
        loginDataResponses=arcGISDatabase.getUserMasterData();
        surveyDataVosList=arcGISDatabase.getSurveyData("SELECT * FROM SURVEY_TABLE WHERE SURVEY_FLAG='N'");
        incrementalSurveyDataVoList=arcGISDatabase.getIncrementalSurveyData("SELECT * FROM INCREMENTALSURVEY_TABLE WHERE SURVEY_FLAG='N'");
        dtrDetailsResponseVoList=new ArrayList<>();
        dtrSurveyVoList=new ArrayList<>();
        dtrDetailsResponseVoList=arcGISDatabase.getDTRDetailsData();
        dtrSurveyVoList=arcGISDatabase.getDTRSurveyData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_goto_ltsurvey:

                  /*  if(surveyDataVosList.size()>0 ||dtrSurveyVoList.size()>0) {

                    }else {
                        Toast.makeText(getApplicationContext(), "No data found" , Toast.LENGTH_LONG).show();
                    }*/
                Intent intentTrans=new Intent(IncrementalTransmitHomeActivity.this,TransMitSurveyActivity.class);
                startActivity(intentTrans);
                finish();

                break;

            case R.id.btn_goto_11_kv_survey:
                Intent intent = new Intent(IncrementalTransmitHomeActivity.this, TransmitHomeActivity.class);
                startActivity(intent);
                finish();

                break;

            case R.id.btn_goto_incremental_ltsurvey:

                Intent intentTransIncr=new Intent(IncrementalTransmitHomeActivity.this, TransMitIncrementalSurveyActivity.class);
                startActivity(intentTransIncr);
                finish();

              /*  if(incrementalSurveyDataVoList.size()>0) {


                }else {
                    Toast.makeText(getApplicationContext(), "No data found" , Toast.LENGTH_LONG).show();
                }*/
                break;

            default:

                break;
        }

    }






}
