package com.apepdcl.gis_survey.estimates;

import static com.apepdcl.gis_survey.utilits.CustomDialog.getColoredText;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.estimates.models.FdrParticularsModel;
import com.apepdcl.gis_survey.estimates.new_module.NewModel;
import com.apepdcl.gis_survey.estimates.sou.OfflineLTActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class OnlyLtSurveyAdapter extends RecyclerView.Adapter<OnlyLtSurveyAdapter.MyViewHolder> {

    String EST_NO, GIS_ID;
    List<FdrParticularsModel> list;
    Context context;
    private OnlyOnItemClickListener listener;


    public OnlyLtSurveyAdapter(String EST_NO, String GIS_ID, List<FdrParticularsModel> list, Context context, OnlyOnItemClickListener listener) {
        this.EST_NO = EST_NO;
        this.GIS_ID = GIS_ID;
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    public interface OnlyOnItemClickListener {
        void onItemClick(FdrParticularsModel model);

        void onEditItemClick(FdrParticularsModel model);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.only_lt_survey_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int dtr_no = position + 1;
       // holder.dtr_heading.setText("CIRCUIT " + dtr_no + " DETAILS");
        holder.dtr_heading.setText("" + dtr_no);
        holder.dtr_name.setText(list.get(position).getFEEDER().split("~")[1]);
        holder.dtr_structure_code.setText(list.get(position).getFEEDER().split("~")[0]);
        holder.lt_ckt_code.setText(list.get(position).getLT_CKT_CODE());
        holder.survey_type.setText(list.get(position).getSURVEY_TYPE());

        int polesCount = getPolesCount(list.get(position).getLT_CKT_CODE());
        holder.no_locations.setText("" + polesCount);



        Log.d("ONLY LT", "Poles Count " + polesCount);
        Log.d("ONLY LT", "LT_CKT_CODE " + list.get(position).getLT_CKT_CODE());

        if (polesCount == 0) {
            holder.continue_survey.setText("START SURVEY");
            holder.edit_survey.setVisibility(View.GONE);
        } else {
            holder.continue_survey.setText("CONTINUE SURVEY");
            if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")){
                holder.edit_survey.setVisibility(View.GONE);
            }else {
                holder.edit_survey.setVisibility(View.VISIBLE);
            }

        }

        if (OfflineLTActivity.IS_OFFLINE) {
            holder.edit_survey.setVisibility(View.GONE);
        }

        holder.continue_survey.setOnClickListener(v -> {
            FdrParticularsModel item = list.get(position);
            if ("LINE EXTENSION".equalsIgnoreCase(item.getSURVEY_TYPE())
                    && item.getPOLE_COUNT() == 0) {
                openAlertDialogForFirstPole("There are no existing pole survey in this circuit" +list.get(position).getLT_CKT_CODE());
                return;
            }
            if (listener != null) {
                listener.onItemClick(list.get(position));
            }

        });

        holder.edit_survey.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditItemClick(list.get(position));
            }
        });
    }

    private void openAlertDialogForFirstPole(String msg) {

        if (!(context instanceof Activity)) {
            return;
        }

        Dialog dialog = new Dialog(context);

        View view = LayoutInflater.from(context)
                .inflate(R.layout.customized_dialog, null);

        dialog.setContentView(view);

        TextView title = view.findViewById(R.id.title);
        TextView message = view.findViewById(R.id.message);
        TextView okBtn = view.findViewById(R.id.okBtn);
        TextView cancelBtn = view.findViewById(R.id.cancelBtn);
        TextView tv_est_no = view.findViewById(R.id.tv_est_no);
        TextView tv_gis_id = view.findViewById(R.id.tv_gis_id);
        ImageView iv_icon = view.findViewById(R.id.iv_icon);

        title.setText("Pay Attention!!");
        message.setText(msg);

        tv_est_no.setText(getColoredText("Est No: ", EST_NO, context));
        tv_gis_id.setText(getColoredText("GIS ID: ", GIS_ID, context));

        iv_icon.setImageResource(R.drawable.warning);

        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.GONE);

        okBtn.setOnClickListener(v -> dialog.dismiss());

        dialog.setCancelable(false);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent);
        }

        dialog.show();
    }

    private int getPolesCount(String ltCktCode) {
        //  Log.d("Debug", "Selected LT CKT " + ltCktCode);
        int totalPolesLT = 0;
        ArrayList<NewModel> allPoles = new ArcGISDatabaseEstimates(context).getNewSurveyData(EST_NO, "LT", false, GIS_ID);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(allPoles);
        Log.d("Debug", "LT POLES ADAPTER: " + json);

        for (int i = 0; i < allPoles.size(); i++) {
            // Log.d("Debug", "Selected LT CKT " + ltCktCode);
            if (allPoles.get(i).getLT_AVAILABLE().equalsIgnoreCase(ltCktCode)) {
                totalPolesLT++;
            }
        }

        return totalPolesLT;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView dtr_heading, no_locations, dtr_name, dtr_structure_code, edit_survey, continue_survey, lt_ckt_code,survey_type;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            dtr_heading = itemView.findViewById(R.id.dtr_heading);
            no_locations = itemView.findViewById(R.id.no_locations);
            dtr_name = itemView.findViewById(R.id.dtr_name);
            dtr_structure_code = itemView.findViewById(R.id.dtr_structure_code);
            lt_ckt_code = itemView.findViewById(R.id.lt_ckt_code);
            edit_survey = itemView.findViewById(R.id.edit_survey);
            continue_survey = itemView.findViewById(R.id.continue_survey);
            survey_type = itemView.findViewById(R.id.survey_type);
        }
    }
}
