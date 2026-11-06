package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.activity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.constants.MobileConstantsRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.retrofitservice.responsevo.RetrofitClientInstanceRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.sqldatabase.ArcGISDatabaseRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.LoginDataResponseRDSSUg11Kv;

import java.util.List;

public class ProfileActivityRDSSUg11KvActivity extends AppCompatActivity implements View.OnClickListener {
  private TextView tv_user,tv_section,tv_role;
    private ArcGISDatabaseRDSSUg11Kv arcGISDatabaseRDSSUg11Kv;
    private SharedPreferences preferences;
    private List<LoginDataResponseRDSSUg11Kv> loginDataResponsRDSSUg11Kvs;
    private Button btn_pfnext;
    private TextView tv_help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("11KV UG Cable Survey");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(ProfileActivityRDSSUg11KvActivity.this, FeederMasterRDSSUg11KvActivity.class); //Intent.ACTION_VIEW);
                startActivity(playIntent);
                finish();

            }
        });
        tv_user=(TextView)findViewById(R.id.tv_user);
        tv_section=(TextView)findViewById(R.id.tv_section);
        tv_role=(TextView)findViewById(R.id.tv_role);
        btn_pfnext=(Button)findViewById(R.id.btn_pfnext);
        btn_pfnext.setOnClickListener(this);
        arcGISDatabaseRDSSUg11Kv =new ArcGISDatabaseRDSSUg11Kv(ProfileActivityRDSSUg11KvActivity.this);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        loginDataResponsRDSSUg11Kvs = arcGISDatabaseRDSSUg11Kv.getUserMasterData();
        if(loginDataResponsRDSSUg11Kvs !=null && loginDataResponsRDSSUg11Kvs.size()>0){
            tv_user.setText(preferences.getString(MobileConstantsRDSSUg11Kv.USERNAME,""));
            tv_section.setText(loginDataResponsRDSSUg11Kvs.get(0).getSECTION_NAME());
            tv_role.setText(loginDataResponsRDSSUg11Kvs.get(0).getROLE());
        }
        tv_help=(TextView)findViewById(R.id.tv_help);
        tv_help.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_pfnext:
                Intent intent=new Intent(ProfileActivityRDSSUg11KvActivity.this, FeederMasterRDSSUg11KvActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.tv_help:
                openWebPage(RetrofitClientInstanceRDSSUg11Kv.web_url_help + "34344.pdf");
                break;
        }

    }
    public void openWebPage(String url) {
        try {
          /*  String format = "https://drive.google.com/viewerng/viewer?embedded=true&url=%s";
            String fullPath = String.format(Locale.ENGLISH, format, url);
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fullPath));
            startActivity(browserIntent);*/
            Uri webpage = Uri.parse(url);
            Intent myIntent = new Intent(Intent.ACTION_VIEW, webpage);
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No application can handle this request. Please install a web browser or check your URL.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
       /* Intent intent=new Intent(ProfileActivityActivity.this,FeederMasterActivity.class);
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
