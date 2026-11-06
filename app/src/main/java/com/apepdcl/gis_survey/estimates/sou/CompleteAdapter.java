package com.apepdcl.gis_survey.estimates.sou;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.estimates.EstimatesHome;
import com.apepdcl.gis_survey.estimates.PurposeAdapter;
import com.apepdcl.gis_survey.estimates.models.PurposeModel;
import com.apepdcl.gis_survey.estimates.new_module.NewModel;

import java.util.ArrayList;

/**
 * Created by Soubhagya Swain on 18,November,2024
 */
class CompleteAdapter extends RecyclerView.Adapter<CompleteAdapter.MyViewHolder> {

    ArrayList<PurposeModel> list;
    Context context;
    OnItemClickListener listener;


    public CompleteAdapter(ArrayList<PurposeModel> list, Context context, OnItemClickListener listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.transmit_adapter, parent, false);
        return new MyViewHolder(view);
    }

    public interface OnItemClickListener {
        void onItemClick(int position, PurposeModel model);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        /*if (list.get(position).getLINE_33KV().equals("Y")){
            holder.layout_33.setVisibility(View.VISIBLE);
        }else {
            holder.layout_33.setVisibility(View.GONE);
        }

        if (list.get(position).getLINE_11KV().equals("Y")){
            holder.layout_11.setVisibility(View.VISIBLE);
        }else {
            holder.layout_11.setVisibility(View.GONE);
        }

        if (list.get(position).getLINE_LT().equals("Y")){
            holder.layout_lt.setVisibility(View.VISIBLE);
        }else {
            holder.layout_lt.setVisibility(View.GONE);
        }*/

        holder.poles_33kv.setText("" + list.get(position).getNUM_33KV());
        holder.poles_11kv.setText("" + list.get(position).getNUM_11KV());
        holder.poles_lt.setText("" + list.get(position).getNUM_LT());


        holder.work_order.setText("WO No: " + list.get(position).getWORK_ORDER());
        holder.gis_id.setText("GIS ID: " + list.get(position).getGIS_ID());
        holder.est_no.setText("Est No: " + list.get(position).getEST_NO());
        holder.description.setText("" + list.get(position).getPURPOSE());
        holder.est_nature.setText("Est Nature: " + list.get(position).getEST_NATURE());

        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("ESTIMATES")) {
            holder.work_order.setVisibility(View.GONE);
            holder.layout_est_data.setVisibility(View.VISIBLE);
        } else if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
            holder.est_nature.setVisibility(View.GONE);
            holder.work_order.setVisibility(View.GONE);
            holder.est_no.setVisibility(View.VISIBLE);
            holder.layout_est_data.setVisibility(View.VISIBLE);
            holder.tv_action_remarks.setVisibility(View.VISIBLE);
            holder.tv_action_remarks.setText(EstimatesHome.ACTION_REMARKS);
            holder.est_no.setText("DTR Code : " + list.get(position).getEST_NO());
        } else if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")){
            holder.layout_est_data_rdss_asset.setVisibility(View.VISIBLE);
            holder.layout_est_data.setVisibility(View.GONE);
            holder.feeders_wo_rdss.setText("FEEDERS: "+list.get(position).getFEEDERS());
            holder.gis_id_wo_rdss.setText("GIS ID: "+list.get(position).getGIS_ID());
        }else {
            holder.work_order.setVisibility(View.VISIBLE);
            holder.layout_est_data.setVisibility(View.VISIBLE);
        }

        holder.transmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView poles_33kv, est_no, poles_11kv, poles_lt, transmit, gis_id, work_order, description, est_nature, tv_action_remarks,feeders_wo_rdss,gis_id_wo_rdss;
        LinearLayout layout_33;
        LinearLayout layout_11;
        LinearLayout layout_lt;
        LinearLayout layout_est_data_rdss_asset,layout_est_data;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            est_no = itemView.findViewById(R.id.est_no);
            poles_33kv = itemView.findViewById(R.id.poles_33kv);
            poles_11kv = itemView.findViewById(R.id.poles_11kv);
            poles_lt = itemView.findViewById(R.id.poles_lt);
            transmit = itemView.findViewById(R.id.transmit);
            layout_33 = itemView.findViewById(R.id.layout_33);
            layout_11 = itemView.findViewById(R.id.layout_11);
            layout_lt = itemView.findViewById(R.id.layout_lt);
            work_order = itemView.findViewById(R.id.work_order);
            gis_id = itemView.findViewById(R.id.gis_id);
            description = itemView.findViewById(R.id.description);
            est_nature = itemView.findViewById(R.id.est_nature);
            tv_action_remarks = itemView.findViewById(R.id.tv_action_remarks);
            gis_id_wo_rdss = itemView.findViewById(R.id.gis_id_wo_rdss);
            feeders_wo_rdss = itemView.findViewById(R.id.feeders_wo_rdss);
            layout_est_data = itemView.findViewById(R.id.layout_est_data);
            layout_est_data_rdss_asset = itemView.findViewById(R.id.layout_est_data_rdss_asset);
        }
    }
}
