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
import com.apepdcl.gis_survey.estimates.models.PurposeModel;

import java.util.ArrayList;

public class ToBeIntegratedAdapter extends RecyclerView.Adapter<ToBeIntegratedAdapter.MyViewHolder> {
    ArrayList<PurposeModel> list;
    Context context;
    OnItemClickListener listener;
    String INTG_STATUS = "";

    public ToBeIntegratedAdapter(ArrayList<PurposeModel> list, Context context, ToBeIntegratedAdapter.OnItemClickListener listener, String INTG_STATUS) {
        this.list = list;
        this.context = context;
        this.listener = listener;
        this.INTG_STATUS = INTG_STATUS;
    }

    @NonNull
    @Override
    public ToBeIntegratedAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.transmit_adapter, parent, false);
        return new ToBeIntegratedAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.poles_33kv.setText("" + list.get(position).getNUM_33KV());
        holder.poles_11kv.setText("" + list.get(position).getNUM_11KV());
        holder.poles_lt.setText("" + list.get(position).getNUM_LT());
        //holder.est_no.setText(list.get(position).getEST_NO());




        holder.work_order.setText("WO No: " + list.get(position).getWORK_ORDER());
        holder.gis_id.setText("GIS ID: " + list.get(position).getGIS_ID());
        holder.est_no.setText("Est No: " + list.get(position).getEST_NO());
        holder.description.setText("" + list.get(position).getPURPOSE());
        holder.est_nature.setText("Est Nature: " + list.get(position).getEST_NATURE());

        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("ESTIMATES")) {
            holder.work_order.setVisibility(View.GONE);
            holder.layout_est_data.setVisibility(View.VISIBLE);
        } else if(LoginActivity.SURVEY_FROM.equalsIgnoreCase("WORK_ORDER")){
            String status = list.get(position).getINTG_STATUS();
            String remarks = list.get(position).getINTG_REMARKS();
            holder.layout_est_data.setVisibility(View.VISIBLE);
            holder.work_order.setVisibility(View.VISIBLE);

// Reset views (VERY IMPORTANT in RecyclerView)
            holder.intg_remarks.setVisibility(View.GONE);
            holder.transmit.setVisibility(View.GONE);

            if ("R".equalsIgnoreCase(status)) {

                if (remarks != null && !remarks.isEmpty()) {
                    holder.intg_remarks.setVisibility(View.VISIBLE);
                    holder.intg_remarks.setText("Rejected Remarks : " + remarks);
                }

                holder.transmit.setText("Resurvey");
                holder.transmit.setVisibility(View.VISIBLE);

            } else if ("A".equalsIgnoreCase(status)) {

                holder.transmit.setText("Integrate data");
                holder.transmit.setVisibility(View.VISIBLE);

            }
        }else if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")){
            String status = list.get(position).getINTG_STATUS();
            String remarks = list.get(position).getINTG_REMARKS();
            if ("R".equalsIgnoreCase(status)) {

                if (remarks != null && !remarks.isEmpty()) {
                    holder.intg_remarks.setVisibility(View.VISIBLE);
                    holder.intg_remarks.setText("Rejected Remarks : " + remarks);
                }

                holder.transmit.setText("Resurvey");
                holder.transmit.setVisibility(View.VISIBLE);

            } else if ("A".equalsIgnoreCase(status)) {

                holder.transmit.setText("Integrate data");
                holder.transmit.setVisibility(View.VISIBLE);

            }

            holder.layout_est_data_rdss_asset.setVisibility(View.VISIBLE);
            holder.layout_est_data.setVisibility(View.GONE);
            holder.gis_id_wo_rdss.setText("GIS ID: "+list.get(position).getGIS_ID());
            holder.feeders_wo_rdss.setText("Feeders : "+list.get(position).getFEEDERS());
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

    public interface OnItemClickListener {
        void onItemClick(int position, PurposeModel model);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView poles_33kv, est_no, poles_11kv, poles_lt, transmit, gis_id, work_order, description, est_nature,intg_remarks,gis_id_wo_rdss,feeders_wo_rdss;
        LinearLayout layout_33;
        LinearLayout layout_11;
        LinearLayout layout_lt,layout_est_data,layout_est_data_rdss_asset;

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
            intg_remarks = itemView.findViewById(R.id.intg_remarks);
            layout_est_data = itemView.findViewById(R.id.layout_est_data);
            layout_est_data_rdss_asset = itemView.findViewById(R.id.layout_est_data_rdss_asset);
            gis_id_wo_rdss = itemView.findViewById(R.id.gis_id_wo_rdss);
            feeders_wo_rdss = itemView.findViewById(R.id.feeders_wo_rdss);
        }
    }

}
