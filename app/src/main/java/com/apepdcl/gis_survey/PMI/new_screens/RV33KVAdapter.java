package com.apepdcl.gis_survey.PMI.new_screens;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.PMI.models.FEEDER_MASTER_11KV;
import com.apepdcl.gis_survey.PMI.models.FEEDER_MASTER_33KV;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

/**
 * Created by SOUBHAGYA SWAIN on 04-08-2025.
 */
public class RV33KVAdapter extends RecyclerView.Adapter<RV33KVAdapter.MyViewHolder> {

    ArrayList<FEEDER_MASTER_33KV> feederMaster33;
    Context context;

    OnItemClickListener listener;

    public RV33KVAdapter(ArrayList<FEEDER_MASTER_33KV> feederMaster33, Context context, OnItemClickListener listener) {
        this.feederMaster33 = feederMaster33;
        this.context = context;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(FEEDER_MASTER_33KV model);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_pmi_11, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        int dtr_no = position + 1;
        holder.dtr_heading.setText("Feeder " + dtr_no + " Details");
        holder.ss_name.setText(feederMaster33.get(position).getEHT_SS_NAME());
        holder.feeder_name.setText(feederMaster33.get(position).getFEEDER_NAME_33KV() + " ("+feederMaster33.get(position).getFEEDER_CODE_33KV()+")");
//        holder.ss_name.setText(feederMaster33.get(position).getEHT_SS_NAME());
//        holder.feeder_name.setText(feederMaster33.get(position).getFEEDER_NAME_33KV());
        //holder.feeder_id.setText(feederMaster33.get(position).getFEEDER_CODE_33KV());

        holder.continue_survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(feederMaster33.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return feederMaster33.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dtr_heading, ss_name, feeder_name, feeder_id;
        MaterialButton continue_survey;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            dtr_heading = itemView.findViewById(R.id.dtr_heading);
            ss_name = itemView.findViewById(R.id.ss_name);
            feeder_name = itemView.findViewById(R.id.feeder_name);
            feeder_id = itemView.findViewById(R.id.feeder_id);
            continue_survey = itemView.findViewById(R.id.continue_survey);
        }
    }
}
