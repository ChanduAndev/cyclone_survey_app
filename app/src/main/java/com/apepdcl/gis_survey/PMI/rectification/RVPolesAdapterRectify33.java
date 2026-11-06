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
import com.apepdcl.gis_survey.PMI.models.PoleModel11KV;
import com.apepdcl.gis_survey.PMI.models.PoleModel33KV;

import java.util.ArrayList;

/**
 * Created by SOUBHAGYA SWAIN on 23-03-2026.
 */
public class RVPolesAdapterRectify33 extends RecyclerView.Adapter<RVPolesAdapterRectify33.MyViewHolder> {

    Context context;
    ArrayList<PoleModel33KV> polesList;
    OnItemClickListener listener;

    public RVPolesAdapterRectify33(Context context, ArrayList<PoleModel33KV> polesListLT, OnItemClickListener listener) {
        this.context = context;
        this.polesList = polesListLT;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(PoleModel33KV model);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_pmi_lt_rectify_poles, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.pole_no.setText(polesList.get(position).getPOLE_NO());
        holder.pole_config.setText(polesList.get(position).getPOLE_CONFIG());
        holder.pole_type.setText(polesList.get(position).getPOLE_TYPE());
        holder.pole_height.setText(polesList.get(position).getPOLE_HEIGHT());
        holder.pole_support.setText(polesList.get(position).getPOLE_SUPPORT());
        holder.pole_defects.setText(polesList.get(position).getDEFECT_TYPE());
        holder.distance.setText(polesList.get(position).getDISTANCE()+" meters");

        holder.capture_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(polesList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return polesList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView pole_no ;
        TextView pole_config ;
        TextView pole_type;
        TextView pole_height;
        TextView pole_support;
        TextView capture_image;
        TextView distance;
        TextView pole_defects;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            pole_no = itemView.findViewById(R.id.pole_no);
            pole_config = itemView.findViewById(R.id.pole_config);
            pole_type = itemView.findViewById(R.id.pole_type);
            pole_height = itemView.findViewById(R.id.pole_height);
            pole_support = itemView.findViewById(R.id.pole_support);
            capture_image = itemView.findViewById(R.id.capture_image);
            distance = itemView.findViewById(R.id.distance);
            pole_defects = itemView.findViewById(R.id.pole_defects);
        }
    }
}
