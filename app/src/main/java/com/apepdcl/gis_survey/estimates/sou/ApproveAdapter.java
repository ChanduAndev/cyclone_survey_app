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
import com.apepdcl.gis_survey.estimates.models.PurposeModel;

import java.util.ArrayList;

/**
 * Created by Soubhagya Swain on 20,November,2024
 */
public class ApproveAdapter extends RecyclerView.Adapter<ApproveAdapter.MyViewHolder> {


    ArrayList<PurposeModel> list;
    Context context;
    OnItemClickListener listener;

    public ApproveAdapter(ArrayList<PurposeModel> list, Context context, OnItemClickListener listener) {
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

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.poles_33kv.setText("" + list.get(position).getNUM_33KV());
        holder.poles_11kv.setText("" + list.get(position).getNUM_11KV());
        holder.poles_lt.setText("" + list.get(position).getNUM_LT());
        holder.transmit.setText("Approve");
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
            holder.tv_action_remarks.setVisibility(View.VISIBLE);
            holder.tv_action_remarks.setText(EstimatesHome.ACTION_REMARKS);
            holder.layout_est_data.setVisibility(View.VISIBLE);
            holder.est_no.setText("DTR Code : " + list.get(position).getEST_NO());
        } else {
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
        TextView poles_33kv, est_no, poles_11kv, poles_lt, transmit, gis_id, work_order, description, est_nature, tv_action_remarks;
        private LinearLayout layout_est_data;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            est_no = itemView.findViewById(R.id.est_no);
            poles_33kv = itemView.findViewById(R.id.poles_33kv);
            poles_11kv = itemView.findViewById(R.id.poles_11kv);
            poles_lt = itemView.findViewById(R.id.poles_lt);
            transmit = itemView.findViewById(R.id.transmit);
            work_order = itemView.findViewById(R.id.work_order);
            gis_id = itemView.findViewById(R.id.gis_id);
            description = itemView.findViewById(R.id.description);
            est_nature = itemView.findViewById(R.id.est_nature);
            tv_action_remarks = itemView.findViewById(R.id.tv_action_remarks);
            layout_est_data = itemView.findViewById(R.id.layout_est_data);
        }
    }
}
