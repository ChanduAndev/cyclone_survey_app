package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.ArcGISDatabaseUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.Constants.MobileConstantsUG33KV;
import com.apepdcl.gis_survey.vo.LoginDataResponse;


import java.util.List;

public class ProfileActivityActivityUG33KV extends AppCompatActivity implements View.OnClickListener {
  private TextView tv_user,tv_section,tv_role;
    private ArcGISDatabaseUG33KV arcGISDatabase;
    private SharedPreferences preferences;
    private List<LoginDataResponse> loginDataResponsUG33KVS;
    private Button btn_pfnext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_ugc_33kv);
        tv_user=(TextView)findViewById(R.id.tv_user);
        tv_section=(TextView)findViewById(R.id.tv_section);
        tv_role=(TextView)findViewById(R.id.tv_role);
        btn_pfnext=(Button)findViewById(R.id.btn_pfnext);
        btn_pfnext.setOnClickListener(this);
        arcGISDatabase=new ArcGISDatabaseUG33KV(ProfileActivityActivityUG33KV.this);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        loginDataResponsUG33KVS =arcGISDatabase.getUserMasterData();
        if(loginDataResponsUG33KVS !=null && loginDataResponsUG33KVS.size()>0){
            tv_user.setText(preferences.getString(MobileConstantsUG33KV.USERNAME,""));
            tv_section.setText(loginDataResponsUG33KVS.get(0).getSECTION_NAME());
            tv_role.setText(loginDataResponsUG33KVS.get(0).getROLE());
        }


    }


    @Override
    public void onClick(View v) {
        Intent intent=new Intent(ProfileActivityActivityUG33KV.this, FeederMasterActivityUG33KV.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(ProfileActivityActivityUG33KV.this, FeederMasterActivityUG33KV.class);
        startActivity(intent);
        finish();
    }
}
