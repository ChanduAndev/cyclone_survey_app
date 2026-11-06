package com.apepdcl.gis_survey.module_11KV.edit_screens;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.estimates.new_module.NewModel;
import com.apepdcl.gis_survey.estimates.sou.EditLocationAdapter;
import com.apepdcl.gis_survey.estimates.sou.EditLocationsMap;
import com.apepdcl.gis_survey.module_11KV.models.SurveyVo;

import java.util.ArrayList;

public class Edit11kvAdapterAssetMapping extends RecyclerView.Adapter<Edit11kvAdapterAssetMapping.MyViewHolder> {
    ArrayList<SurveyVo> list;
    Context context;

    Edit11kvAdapterAssetMapping.OnItemClickListener listener;

    public Edit11kvAdapterAssetMapping(ArrayList<SurveyVo> list, Context context, Edit11kvAdapterAssetMapping.OnItemClickListener listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, SurveyVo model);
        void onUpdateLocationClick(int pos,SurveyVo model);
    }

    @NonNull
    @Override
    public Edit11kvAdapterAssetMapping.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(context).inflate(R.layout.edit_adapter_asset, parent, false);
        View view = LayoutInflater.from(context).inflate(R.layout.edit_adapter, parent, false);
        return new Edit11kvAdapterAssetMapping.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Edit11kvAdapterAssetMapping.MyViewHolder holder, int position) {
        holder.location_no.setText(String.valueOf(list.get(position).getPole_no()));

        // holder.location_id.setText(list.get(position).getLOCATION_ID());
        holder.pole_config.setText(list.get(position).getPole_configuration());





       holder.update_locations.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (listener != null) {
                   listener.onUpdateLocationClick(position, list.get(position));
               }
//

           }
       });


        holder.update_attributes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(position, list.get(position));
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView update_locations, update_attributes;
        TextView location_no;
        TextView pole_config;
        TextView mapped_pole;
        TextView tv_loc_number;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            update_locations = itemView.findViewById(R.id.update_locations);
            update_attributes = itemView.findViewById(R.id.update_attributes);
            location_no = itemView.findViewById(R.id.location_no);
            pole_config = itemView.findViewById(R.id.pole_config);
            mapped_pole = itemView.findViewById(R.id.mapped_pole);
            tv_loc_number = itemView.findViewById(R.id.tv_loc_number);

        }
    }
}

