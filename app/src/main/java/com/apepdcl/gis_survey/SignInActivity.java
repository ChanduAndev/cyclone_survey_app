package com.apepdcl.gis_survey;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.apepdcl.gis.app.R;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().hide();
    }
}