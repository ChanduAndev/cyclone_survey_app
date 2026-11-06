package com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.activitys;

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

import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.Constants.MobileConstants;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.retrofitservice.RetrofitClientInstance;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.LoginDataResponse;

import java.util.List;

public class ProfileActivityActivity extends AppCompatActivity implements View.OnClickListener {
  private TextView tv_user,tv_section,tv_role;
    private ArcGISDatabase arcGISDatabase;
    private SharedPreferences preferences;
    private List<LoginDataResponse> loginDataResponses;
    private Button btn_pfnext;
    private TextView tv_help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_11kv_oh_rdss);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("11Kv LineSurvey");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(ProfileActivityActivity.this, FeederMasterActivity.class); //Intent.ACTION_VIEW);
                startActivity(playIntent);
                finish();

            }
        });
        tv_user=(TextView)findViewById(R.id.tv_user);
        tv_section=(TextView)findViewById(R.id.tv_section);
        tv_role=(TextView)findViewById(R.id.tv_role);
        btn_pfnext=(Button)findViewById(R.id.btn_pfnext);
        btn_pfnext.setOnClickListener(this);
        arcGISDatabase=new ArcGISDatabase(ProfileActivityActivity.this);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        loginDataResponses=arcGISDatabase.getUserMasterData();
        if(loginDataResponses!=null && loginDataResponses.size()>0){
            tv_user.setText(preferences.getString(MobileConstants.USERNAME,""));
            tv_section.setText(loginDataResponses.get(0).getSECTION_NAME());
            tv_role.setText(loginDataResponses.get(0).getROLE());
        }
        tv_help=(TextView)findViewById(R.id.tv_help);
        tv_help.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_pfnext:
                Intent intent=new Intent(ProfileActivityActivity.this,FeederMasterActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.tv_help:
                openWebPage(RetrofitClientInstance.web_url_help + "34344.pdf");
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
