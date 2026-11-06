package com.apepdcl.gis_survey.estimates;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;

public class OnlyLtEdit extends AppCompatActivity {

    RelativeLayout back_layout, close_layout;
    RecyclerView dialog_rv;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_lt_edit);
        getSupportActionBar().hide();

        back_layout = findViewById(R.id.back_layout);
        close_layout = findViewById(R.id.close_layout);
        dialog_rv = findViewById(R.id.dialog_rv);

        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        close_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
}
