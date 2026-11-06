package com.apepdcl.gis_survey.estimates.new_module;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.apepdcl.gis.app.R;


public class WorkEnvironmentFragment extends Dialog {


    public WorkEnvironmentFragment(@NonNull Context context) {
        super(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.work_tools);
        setCancelable(false);

    }


}