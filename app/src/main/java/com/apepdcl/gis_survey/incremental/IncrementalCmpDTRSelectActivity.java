package com.apepdcl.gis_survey.incremental;

import android.app.ProgressDialog;
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
import com.apepdcl.gis_survey.retrofitservice.RetrofitService;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.vo.LTCktsResponseDataVo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.apepdcl.gis_survey.HomeActivity;
public class IncrementalCmpDTRSelectActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private Spinner sp_incr_cmpdtrcode,sp_incr_cmpcircuit;
    private Button btn_incr_next;
    private  List<LTCktsResponseDataVo> ltCktsResponseDataVos;
    private ArcGISDatabase arcGISDatabase;
    private List<String> dtrCodeList;
    private List<String> ltCircuitList;
    private String dtrCode,ltCircuit,sessionId;
    private SharedPreferences sharedPreferences;
    private RetrofitService retrofitService;
    private ProgressDialog progressDialog;
    private String dtr_Code,dtr_Code1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_incremental_cmpdtrselection);
            sp_incr_cmpdtrcode = findViewById(R.id.sp_incr_cmpdtrcode);
            sp_incr_cmpcircuit = findViewById(R.id.sp_incr_cmpcircuit);
            btn_incr_next = findViewById(R.id.btn_incr_next);

            sp_incr_cmpdtrcode.setOnItemSelectedListener(this);
            sp_incr_cmpcircuit.setOnItemSelectedListener(this);
            btn_incr_next.setOnClickListener(this);
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please Wail...");
            progressDialog.setCancelable(false);


            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("DTR Selection");
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent playIntent = new Intent(IncrementalCmpDTRSelectActivity.this, HomeActivity.class); //Intent.ACTION_VIEW);
                    startActivity(playIntent);
                    finish();

                }
            });
            //  setSupportActionBar(toolbar);
            arcGISDatabase = new ArcGISDatabase(this);
            ltCktsResponseDataVos = arcGISDatabase.getDTRMasterData("");
            dtrCodeList = arcGISDatabase.getCmpDTRCode();
            dtrCodeList.add("Select");
            Collections.reverse(dtrCodeList);

            ArrayAdapter<String> ehtSSadapter = new ArrayAdapter<String>(IncrementalCmpDTRSelectActivity.this,
                    android.R.layout.simple_spinner_dropdown_item, dtrCodeList);
            sp_incr_cmpdtrcode.setAdapter(ehtSSadapter);
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(IncrementalCmpDTRSelectActivity.this);
            sessionId = sharedPreferences.getString(MobileConstants.SESSION_ID, "");
        }catch (Exception e){}
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.sp_incr_cmpdtrcode:
                dtrCode=sp_incr_cmpdtrcode.getSelectedItem().toString();
                ltCircuitList=new ArrayList<>();
                if (!"Select".equalsIgnoreCase(dtrCode)) {

                    //Collections.reverse(feederNameList);
                    if (dtrCode.contains("-")) {
                        String[] dtrcode = dtrCode.split("-");
                         dtr_Code1 = dtrcode[0]; // 004
                        // String ssName = poleNum[1];
                        ltCircuitList = arcGISDatabase.getComplaint(String.format("SELECT DISTINCT LT_CKTCODE FROM CMPDTR_TABLE WHERE DTR_CODE='%s'", dtr_Code1));
                        ltCircuitList.add("Select");
                        Collections.reverse(ltCircuitList);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(IncrementalCmpDTRSelectActivity.this,
                                android.R.layout.simple_spinner_dropdown_item, ltCircuitList);
                        sp_incr_cmpcircuit.setAdapter(adapter);

                    } else {
                        ltCircuitList = arcGISDatabase.getComplaint(String.format("SELECT DISTINCT LT_CKTCODE FROM CMPDTR_TABLE WHERE DTR_CODE='%s'", dtrCode));
                        ltCircuitList.add("Select");
                        Collections.reverse(ltCircuitList);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(IncrementalCmpDTRSelectActivity.this,
                                android.R.layout.simple_spinner_dropdown_item, ltCircuitList);
                        sp_incr_cmpcircuit.setAdapter(adapter);
                    }

                } else {
                    ltCircuitList.add("Select");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(IncrementalCmpDTRSelectActivity.this,
                            android.R.layout.simple_spinner_dropdown_item, ltCircuitList);
                    sp_incr_cmpcircuit.setAdapter(adapter);
                }
                break;

            case R.id.sp_incr_cmpcircuit:
                ltCircuit=sp_incr_cmpcircuit.getSelectedItem().toString();
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_incr_next:
                if("Select".equalsIgnoreCase(dtrCode)){
                    Toast.makeText(IncrementalCmpDTRSelectActivity.this,"Select DTR Code",Toast.LENGTH_SHORT).show();
                }else if("Select".equalsIgnoreCase(ltCircuit)){
                    Toast.makeText(IncrementalCmpDTRSelectActivity.this,"Select LT Circuit",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(IncrementalCmpDTRSelectActivity.this, IncrementalLTCompletedMapActivity.class);
                    intent.putExtra("DTRCODE",dtr_Code1);
                    intent.putExtra("CIRCUIT_CODE",ltCircuit);
                    startActivity(intent);
                    finish();
                }

                break;

        }
    }


    @Override
    public void onBackPressed() {
        Intent intent=new Intent(IncrementalCmpDTRSelectActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
