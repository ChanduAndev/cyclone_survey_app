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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.constants.MobileConstantsRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.runtimepermessions.ConnectivityCheckingRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.sqldatabase.ArcGISDatabaseRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.utilities.CommanUtilitsRDSSUg11Kv;
import com.apepdcl.gis_survey.utilits.CommanUtilits;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IncrementalCompetedFeederRDSSUg11KvActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button btn_next;
    private Spinner sp_cmpssname,sp_cmpfeedername;
    private String ssName,feederName,sessionId;
    private ArcGISDatabaseRDSSUg11Kv arcGISDatabaseRDSSUg11Kv;
    private List<String> feederNameList;
    private List<String> ehtName;
    private ProgressDialog progressDialog;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_incrementalcompletedsuervey_feeder_rdss_ug_11kv);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("11KV UG Cable Survey");
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent playIntent = new Intent(IncrementalCompetedFeederRDSSUg11KvActivity.this, FeederMasterRDSSUg11KvActivity.class); //Intent.ACTION_VIEW);
                    startActivity(playIntent);
                    finish();

                }
            });
            progressDialog = CommanUtilitsRDSSUg11Kv.progressDialog(this);
            sp_cmpssname = findViewById(R.id.sp_cmpssname);
            sp_cmpfeedername = findViewById(R.id.sp_cmpfeedername);

            sp_cmpssname.setOnItemSelectedListener(this);
            sp_cmpfeedername.setOnItemSelectedListener(this);
            arcGISDatabaseRDSSUg11Kv = new ArcGISDatabaseRDSSUg11Kv(this);


            ehtName = new ArrayList<>();
            //ehtName.add("Select");
            ehtName = arcGISDatabaseRDSSUg11Kv.getCmpEHTSSNAME();
            ehtName.add("Select");
            Collections.reverse(ehtName);

            ArrayAdapter<String> ehtSSadapter = new ArrayAdapter<String>(IncrementalCompetedFeederRDSSUg11KvActivity.this,
                    android.R.layout.simple_dropdown_item_1line, ehtName);
            sp_cmpssname.setAdapter(ehtSSadapter);


            btn_next = (Button) findViewById(R.id.btn_next);
            preferences = PreferenceManager.getDefaultSharedPreferences(IncrementalCompetedFeederRDSSUg11KvActivity.this);
            sessionId = preferences.getString(MobileConstantsRDSSUg11Kv.SESSION_ID, "");
            if (sessionId == null || sessionId.isEmpty()) {
                Intent intent = new Intent(IncrementalCompetedFeederRDSSUg11KvActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
            btn_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if ("Select".equals(ssName)) {
                        Toast.makeText(IncrementalCompetedFeederRDSSUg11KvActivity.this, "Select SS Name", Toast.LENGTH_LONG).show();
                    } else if ("Select".equalsIgnoreCase(feederName)) {
                        Toast.makeText(IncrementalCompetedFeederRDSSUg11KvActivity.this, "Select Feeder Name", Toast.LENGTH_LONG).show();
                    } else {
                        if (ConnectivityCheckingRDSSUg11Kv.checkConnection(IncrementalCompetedFeederRDSSUg11KvActivity.this)) {
                            // getSurveyData();

                            // feederId=arcGISDatabase.getFeederId(String.format("SELECT FEEDER_CODE_33KV FROM EHTMASTER_TABLE WHERE FEEDER_NAME_33KV='%s'",feederName));

                            String[] feederDta = feederName.split("-");
                            String feederCode = feederDta[0];
                            Intent intent = new Intent(IncrementalCompetedFeederRDSSUg11KvActivity.this, IncrementalCompletedMapRDSSUg11KvActivity.class);
                            intent.putExtra("FEEDER_CODE", feederCode);
                            startActivity(intent);
                            finish();

                        } else {
                            CommanUtilits.internetDialog(IncrementalCompetedFeederRDSSUg11KvActivity.this);
                           // Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
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
                    feederNameList = arcGISDatabaseRDSSUg11Kv.getComplaint(String.format("SELECT DISTINCT FEEDER_CODE_33KV || '-' || FEEDER_NAME_33KV FROM CMP_EHTMASTER_TABLE WHERE EHT_SS_CODE='%s'", ssCode));
                    feederNameList.add("Select");
                    Collections.reverse(feederNameList);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(IncrementalCompetedFeederRDSSUg11KvActivity.this,
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(IncrementalCompetedFeederRDSSUg11KvActivity.this,
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
    public void onNothingSelected(AdapterView<?> adapterView) {

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
