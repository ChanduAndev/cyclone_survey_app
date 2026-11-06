package com.apepdcl.gis_survey.PMI.rectification;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.PMI.models.DTR;
import com.apepdcl.gis_survey.PMI.new_screens.RVLTAdapter;

import java.util.ArrayList;

/**
 * Created by SOUBHAGYA SWAIN on 17-03-2026.
 */
public class RVLTAdapterRectify extends RecyclerView.Adapter<RVLTAdapterRectify.MyViewHolder>{

    ArrayList<DTR> dtrMaster;
    Context context;
    OnItemClickListener listener;

    public RVLTAdapterRectify(ArrayList<DTR> dtrMaster, Context context, OnItemClickListener listener) {
        this.dtrMaster = dtrMaster;
        this.context = context;
        this.listener = listener;
    }


    public interface OnItemClickListener {
        void onItemClick(DTR model);
    }

    @NonNull
    @Override
    public RVLTAdapterRectify.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_pmi_lt, parent, false);
        return new RVLTAdapterRectify.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVLTAdapterRectify.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        int dtr_no = position + 1;
        holder.dtr_heading.setText("LT Ckt " + dtr_no + " Details");
        holder.ss_name.setText(dtrMaster.get(position).getDTR_LOCATION());
        holder.feeder_name.setText(dtrMaster.get(position).getDTR_STRUCTURE_CODE());
        holder.feeder_id.setText(dtrMaster.get(position).getFEEDER_ID());

        holder.continue_survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(dtrMaster.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dtrMaster.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView dtr_heading, ss_name, feeder_name, feeder_id, continue_survey;

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
