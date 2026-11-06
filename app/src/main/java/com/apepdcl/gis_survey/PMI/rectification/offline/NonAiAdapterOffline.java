package com.apepdcl.gis_survey.PMI.rectification.offline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.PMI.models.OfflineDataFor11;

import java.util.ArrayList;

public class NonAiAdapterOffline extends RecyclerView.Adapter<NonAiAdapterOffline.MyViewHolder> {
    Context context;
    ArrayList<OfflineDataFor11> polesList;
    OnNonAiItemClickListener listener;

    public NonAiAdapterOffline(Context context, ArrayList<OfflineDataFor11> polesList, OnNonAiItemClickListener listener) {
        this.context = context;
        this.polesList = polesList;
        this.listener = listener;
    }

    public interface OnNonAiItemClickListener {
        void onNonAiItemClick(OfflineDataFor11 model);
    }

    @NonNull
    @Override
    public NonAiAdapterOffline.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_pmi_lt_rectify_poles, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.pole_no.setText(polesList.get(position).getPOLE_NO());
        holder.pole_config.setText(polesList.get(position).getPOLE_CONFIG());
        holder.pole_type.setText(polesList.get(position).getPOLE_TYPE());
        holder.pole_height.setText(polesList.get(position).getPOLE_HEIGHT());
        holder.pole_support.setText(polesList.get(position).getPOLE_SUPPORT());
        holder.pole_defects.setText(polesList.get(position).getDEFECT_TYPE());
        holder.pole_sub_defects.setText(polesList.get(position).getDEFECT_SUB_TYPE());
        holder.distance.setText(polesList.get(position).getDISTANCE() + "meters");

        holder.capture_image.setOnClickListener(v ->
                listener.onNonAiItemClick(polesList.get(position))
        );
    }

    @Override
    public int getItemCount() {
        return polesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView pole_no;
        TextView pole_config;
        TextView pole_type;
        TextView pole_height;
        TextView pole_support;
        TextView capture_image;
        TextView distance;
        TextView pole_defects;
        TextView pole_sub_defects;

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
            pole_sub_defects = itemView.findViewById(R.id.pole_sub_defects);
        }
    }
}
