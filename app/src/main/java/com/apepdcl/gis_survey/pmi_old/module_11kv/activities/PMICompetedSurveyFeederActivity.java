package com.apepdcl.gis_survey.pmi_old.module_11kv.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.pmi_old.module_11kv.database.PMI11kvArcGISDatabase;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.utilits.CommanUtilits;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PMICompetedSurveyFeederActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private Button btn_next;
    private Spinner sp_cmpssname, sp_cmpfeedername;
    private String ssName, feederName, sessionId;
    private PMI11kvArcGISDatabase arcGISDatabase;
    private List<String> feederNameList;
    private List<String> ehtName;
    private ProgressDialog progressDialog;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pmicompeted_survey_feeder);
        getSupportActionBar().hide();
        try {

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("11Kv Survey ");
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent playIntent = new Intent(PMICompetedSurveyFeederActivity.this, PMIHomeActivity.class); //Intent.ACTION_VIEW);
                    startActivity(playIntent);
                    finish();

                }
            });
            progressDialog = CommanUtilits.progressDialog(this);
            sp_cmpssname = findViewById(R.id.sp_cmpssname);
            sp_cmpfeedername = findViewById(R.id.sp_cmpfeedername);

            sp_cmpssname.setOnItemSelectedListener(this);
            sp_cmpfeedername.setOnItemSelectedListener(this);
            arcGISDatabase = new PMI11kvArcGISDatabase(this);


            ehtName = new ArrayList<>();
            //ehtName.add("Select");
            ehtName = arcGISDatabase.getEHTSSNAME();
            ehtName.add("Select");
            Collections.reverse(ehtName);

            ArrayAdapter<String> ehtSSadapter = new ArrayAdapter<String>(PMICompetedSurveyFeederActivity.this,
                    android.R.layout.simple_dropdown_item_1line, ehtName);
            sp_cmpssname.setAdapter(ehtSSadapter);


            btn_next = (Button) findViewById(R.id.btn_next);
            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            sessionId = preferences.getString(MobileConstants.SESSION_ID, "");
            if (sessionId == null || sessionId.isEmpty()) {
                Intent intent = new Intent(PMICompetedSurveyFeederActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
            btn_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if ("Select".equals(ssName)) {
                        Toast.makeText(PMICompetedSurveyFeederActivity.this, "Select SS Name", Toast.LENGTH_LONG).show();
                    } else if ("Select".equalsIgnoreCase(feederName)) {
                        Toast.makeText(PMICompetedSurveyFeederActivity.this, "Select Feeder Name", Toast.LENGTH_LONG).show();
                    } else {
                        if (ConnectivityChecking.checkConnection(PMICompetedSurveyFeederActivity.this)) {
                            // getSurveyData();

                            // feederId=arcGISDatabase.getFeederId(String.format("SELECT FEEDER_CODE_33KV FROM EHTMASTER_TABLE WHERE FEEDER_NAME_33KV='%s'",feederName));

                            String[] feederDta = feederName.split("-");
                            String feederCode = feederDta[0];
                            Intent intent = new Intent(PMICompetedSurveyFeederActivity.this, PMICompletedMapActivity11KV.class);
                            intent.putExtra("FEEDER_CODE", feederCode);
                            startActivity(intent);
                            finish();

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
        }catch (Exception e){}
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.sp_cmpssname:
                feederNameList=new ArrayList<>();
                ssName=sp_cmpssname.getSelectedItem().toString();
                if(!"Select".equalsIgnoreCase(ssName)) {


                    String[] ss = ssName.split("-");
                    String ssCode = ss[0]; // 004
                    //String ssName = ss[1];
                    //Collections.reverse(feederNameList);
                    feederNameList = arcGISDatabase.getComplaint(String.format("SELECT DISTINCT FEEDER_CODE_33KV || '-' || FEEDER_NAME_33KV FROM EHTMASTER_TABLE WHERE EHT_SS_CODE='%s'", ssCode));
                    feederNameList.add("Select");
                    Collections.reverse(feederNameList);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(PMICompetedSurveyFeederActivity.this,
                            android.R.layout.simple_dropdown_item_1line, feederNameList);
                    sp_cmpfeedername.setAdapter(adapter);
                    //Collections.reverse(feederNameList);
                    /*feederNameList = arcGISDatabase.getComplaint(String.format("SELECT FEEDER_NAME_33KV FROM EHTMASTER_TABLE WHERE EHT_SS_NAME='%s'",ssName));
                    feederNameList.add("Select");
                    Collections.reverse(feederNameList);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(CompetedSurveyFeederActivity.this,
                            android.R.layout.simple_spinner_item,feederNameList);
                    sp_cmpfeedername.setAdapter(adapter);*/
                }else {
                    feederNameList.add("Select");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(PMICompetedSurveyFeederActivity.this,
                            android.R.layout.simple_dropdown_item_1line,feederNameList);
                    sp_cmpfeedername.setAdapter(adapter);
                }
                break;
            case R.id.sp_cmpfeedername:
                feederName=sp_cmpfeedername.getSelectedItem().toString();

                break;

            default:

                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
       /* Intent intent = new Intent(CompetedSurveyFeederActivity.this, FeederMasterActivity.class);
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