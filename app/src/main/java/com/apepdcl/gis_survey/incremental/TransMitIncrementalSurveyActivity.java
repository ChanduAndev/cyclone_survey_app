package com.apepdcl.gis_survey.incremental;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.TransmitDTRDataActivity;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.vo.DTRDetailsResponseVo;
import com.apepdcl.gis_survey.vo.DTRSurveyVo;
import com.apepdcl.gis_survey.vo.IncrementalSurveyDataVo;

import java.util.ArrayList;
import java.util.List;
import com.apepdcl.gis_survey.HomeActivity;

public class TransMitIncrementalSurveyActivity extends AppCompatActivity implements View.OnClickListener {
private Button btn_incr_dtrdata,btn_incr_ltdata;
private ArcGISDatabase arcGISDatabase;
    private List<IncrementalSurveyDataVo> surveyDataVosList;
    private List<DTRDetailsResponseVo> dtrDetailsResponseVoList=null;
    private List<DTRSurveyVo> dtrSurveyVoList=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incr_transmitsurvey);
        btn_incr_dtrdata=findViewById(R.id.btn_incr_dtrdata);
        btn_incr_ltdata=findViewById(R.id.btn_incr_ltdata);
        btn_incr_dtrdata.setOnClickListener(this);
        btn_incr_ltdata.setOnClickListener(this);
        arcGISDatabase=new ArcGISDatabase(this);
        dtrDetailsResponseVoList=new ArrayList<>();
        surveyDataVosList=new ArrayList<>();
        surveyDataVosList=arcGISDatabase.getIncrementalSurveyData("SELECT * FROM INCREMENTALSURVEY_TABLE WHERE SURVEY_FLAG='N'");

        dtrSurveyVoList=arcGISDatabase.getDTRSurveyData();
       /* if(surveyDataVosList.size()<0 ) {
            btn_ltdata.setVisibility(View.GONE);
        }
        if(dtrDetailsResponseVoList.size()<0){
            btn_dtrdata.setVisibility(View.GONE);
        }*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_incr_ltdata:
                if(surveyDataVosList.size()>0) {
                    Intent intent = new Intent(TransMitIncrementalSurveyActivity.this, IncrementalTransmitDataActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(), "No data to transmit", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.btn_incr_dtrdata:

                if(dtrSurveyVoList.size()>0) {
                    Intent intentDtr = new Intent(TransMitIncrementalSurveyActivity.this, TransmitDTRDataActivity.class);
                    startActivity(intentDtr);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(), "No data to transmit", Toast.LENGTH_LONG).show();
                }

                break;


        }
    }

    @Override
    public void onBackPressed() {
        Intent intentDtr = new Intent(TransMitIncrementalSurveyActivity.this, HomeActivity.class);
        startActivity(intentDtr);
        finish();
    }
}
