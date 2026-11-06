package com.apepdcl.gis_survey.lt_edit_screens;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.vo.SurveyDataVo;

import java.util.ArrayList;

public class EditLTAdapterAssetMapping extends RecyclerView.Adapter<EditLTAdapterAssetMapping.MyViewHolder> {
    ArrayList<SurveyDataVo> list;
    Context context;
    EditLTAdapterAssetMapping.OnItemClickListener listener;
    String type;

    public EditLTAdapterAssetMapping(ArrayList<SurveyDataVo> list, Context context, OnItemClickListener listener, String type) {
        this.list = list;
        this.context = context;
        this.listener = listener;
        this.type = type;
    }

    @NonNull
    @Override
    public EditLTAdapterAssetMapping.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.edit_lt_adapter, parent, false);
//        if (type.equalsIgnoreCase("OFFLINE_LT")) {
//            view = LayoutInflater.from(context).inflate(R.layout.offline_lt_lat, parent, false);
//        } else {
//            view = LayoutInflater.from(context).inflate(R.layout.edit_lt_adapter, parent, false);
//        }

        return new EditLTAdapterAssetMapping.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EditLTAdapterAssetMapping.MyViewHolder holder, int position) {
        holder.location_no.setText(String.valueOf(list.get(position).getPOLE_NO()));
        if (!type.equalsIgnoreCase("OFFLINE_LT")) {
            holder.pole_config.setText(list.get(position).getLINE_CONFIGURATION());
        }
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

    public interface OnItemClickListener {
        void onItemClick(int position, SurveyDataVo model);

        void onUpdateLocationClick(int pos, SurveyDataVo model);
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
