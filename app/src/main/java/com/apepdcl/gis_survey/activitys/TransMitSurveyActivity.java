package com.apepdcl.gis_survey.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.vo.DTRDetailsResponseVo;
import com.apepdcl.gis_survey.vo.DTRSurveyVo;
import com.apepdcl.gis_survey.vo.SurveyDataVo;

import java.util.ArrayList;
import java.util.List;

public class TransMitSurveyActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_dtrdata, btn_ltdata;
    private ArcGISDatabase arcGISDatabase;

    private List<SurveyDataVo> surveyDataVosList, imagesList, surveyVoList_LAT_LAN, surveyVoListEditPoleDetails, surveyVoList_Source_pole;

    private List<DTRDetailsResponseVo> dtrDetailsResponseVoList = null;
    private List<DTRSurveyVo> dtrSurveyVoList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transmitsurvey);
        btn_dtrdata = findViewById(R.id.btn_dtrdata);
        btn_ltdata = findViewById(R.id.btn_ltdata);
        btn_dtrdata.setOnClickListener(this);
        btn_ltdata.setOnClickListener(this);
        arcGISDatabase = new ArcGISDatabase(this);
        dtrDetailsResponseVoList = new ArrayList<>();
        surveyDataVosList = new ArrayList<>();
        imagesList = new ArrayList<>();
        surveyVoList_LAT_LAN = new ArrayList<>();
        surveyVoListEditPoleDetails = new ArrayList<>();
        surveyVoList_Source_pole = new ArrayList<>();
        surveyDataVosList = arcGISDatabase.getSurveyData("SELECT * FROM SURVEY_TABLE WHERE SURVEY_FLAG='N'");
        imagesList = arcGISDatabase.getLTImageData("");
        surveyVoList_LAT_LAN = arcGISDatabase.getAllSurveyDataLatLan();
        surveyVoListEditPoleDetails = arcGISDatabase.getEditPoleSurveyData();

        surveyVoList_Source_pole = arcGISDatabase.getEditSourcePoleSurveyData();


        System.out.println("LIST_OF_EDIT_POLE_DETAILS" + surveyVoListEditPoleDetails.size());

        // System.out.println("DATA FROM DATABASE : "+surveyDataVosList.size());

        dtrSurveyVoList = arcGISDatabase.getDTRSurveyData();
       /* if(surveyDataVosList.size()<0 ) {
            btn_ltdata.setVisibility(View.GONE);
        }
        if(dtrDetailsResponseVoList.size()<0){
            btn_dtrdata.setVisibility(View.GONE);
        }*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_ltdata:

                if (surveyDataVosList.size() > 0 || imagesList.size() > 0 || surveyVoList_LAT_LAN.size() > 0 || surveyVoListEditPoleDetails.size() > 0 || surveyVoList_Source_pole.size() > 0) {

                    Intent intent = new Intent(TransMitSurveyActivity.this, TransmitDataActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "No data to transmit", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.btn_dtrdata:

                if (dtrSurveyVoList.size() > 0) {
                    Intent intentDtr = new Intent(TransMitSurveyActivity.this, TransmitDTRDataActivity.class);
                    startActivity(intentDtr);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "No data to transmit", Toast.LENGTH_LONG).show();
                }

                break;


        }
    }
}
