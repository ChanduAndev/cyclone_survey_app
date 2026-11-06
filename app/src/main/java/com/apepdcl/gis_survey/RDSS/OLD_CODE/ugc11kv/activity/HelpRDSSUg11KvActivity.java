package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.activity;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.retrofitservice.responsevo.RetrofitClientInstanceRDSSUg11Kv;


public class HelpRDSSUg11KvActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_profile, tv_help, tv_contacts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view_layout_rdss_ug_11kv);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Help");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(HelpRDSSUg11KvActivity.this, FeederMasterRDSSUg11KvActivity.class); //Intent.ACTION_VIEW);
                startActivity(playIntent);
                finish();

            }
        });
        tv_profile = (TextView) findViewById(R.id.tv_profile);
        tv_help = (TextView) findViewById(R.id.tv_help);
        tv_contacts = (TextView) findViewById(R.id.tv_contacts);
        tv_profile.setOnClickListener(this);
        tv_help.setOnClickListener(this);
        tv_contacts.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_profile:
                Intent intent = new Intent(HelpRDSSUg11KvActivity.this, ProfileActivityRDSSUg11KvActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.tv_help:
 /*       Intent intent1=new Intent(HelpActivity.this,WebHelpActivity.class);
        startActivity(intent1);
        finish();*/
                openWebPage(RetrofitClientInstanceRDSSUg11Kv.web_url_help + "34344.pdf");
                break;
            case R.id.tv_contacts:
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
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
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
        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


}
