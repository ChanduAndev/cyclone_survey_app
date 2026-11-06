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

import java.util.List;

public class OfflineSurveyAdapter extends RecyclerView.Adapter<OfflineSurveyAdapter.MyViewHolder> {
    private Context context;
    private List<OfflineDataFor11> model;
    OnItemClickListener listener;

    public OfflineSurveyAdapter(Context context, List<OfflineDataFor11> model, OnItemClickListener listener) {
        this.context = context;
        this.model = model;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public OfflineSurveyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(
                        R.layout.item_list_offline,
                        parent,
                        false
                );

        return new OfflineSurveyAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfflineSurveyAdapter.MyViewHolder holder, int position) {
        OfflineDataFor11 list = model.get(position);
        int sl_no = position + 1;
        holder.tv_location_no.setText(list.POLE_NO);
        holder.tv_feeder_id.setText(list.getFEEDER_ID());
        holder.tv_defect_survey.setText(list.getDEFECT_TYPE());
        holder.sl_no.setText("" + sl_no);
        holder.btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(position);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_feeder_id, tv_location_no, tv_defect_survey, btn_continue, sl_no;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_feeder_id = itemView.findViewById(R.id.tv_feeder_id);
            tv_location_no = itemView.findViewById(R.id.tv_location_no);
            tv_defect_survey = itemView.findViewById(R.id.tv_defect_survey);
            btn_continue = itemView.findViewById(R.id.btn_continue);
            sl_no = itemView.findViewById(R.id.sl_no);
        }
    }
}
