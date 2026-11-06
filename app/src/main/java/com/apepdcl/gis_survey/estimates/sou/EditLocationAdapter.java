package com.apepdcl.gis_survey.estimates.sou;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.estimates.PurposeAdapter;
import com.apepdcl.gis_survey.estimates.models.PurposeModel;
import com.apepdcl.gis_survey.estimates.new_module.EstimatesMap;
import com.apepdcl.gis_survey.estimates.new_module.NewModel;

import java.util.ArrayList;

public class EditLocationAdapter extends RecyclerView.Adapter<EditLocationAdapter.MyViewHolder> {

    ArrayList<NewModel> list;
    Context context;
    String  GIS_ID;
    OnItemClickListener listener;

    public EditLocationAdapter(ArrayList<NewModel> list, Context context, String  GIS_ID, OnItemClickListener listener) {
        this.list = list;
        this.context = context;
        this.GIS_ID = GIS_ID;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, NewModel model);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")){
            view = LayoutInflater.from(context).inflate(R.layout.edit_adapter_solar, parent, false);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.edit_adapter, parent, false);
        }
         //view = LayoutInflater.from(context).inflate(R.layout.edit_adapter, parent, false);
        return new MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.location_no.setText(list.get(position).getPOLE_NO());
        // holder.location_id.setText(list.get(position).getLOCATION_ID());
        holder.pole_config.setText(list.get(position).getPOLE_CONFIG());

        //NEW DTR CHANGES BY CHANDU
        //------------------------START------------------------
        if (list.get(position).getEXT_ID() != null && !list.get(position).getEXT_ID().isEmpty()) {
            holder.update_locations.setClickable(false);
            holder.update_locations.setFocusable(false);
            holder.update_locations.setEnabled(false);
            holder.update_locations.setBackground(context.getResources().getDrawable(R.drawable.disable_border));
            holder.update_locations.setTextColor(context.getResources().getColor(R.color.lite_black));
        }
        //------------------------END------------------------


        if (list.get(position).getIS_TRANSMITTED_LOCATION().equalsIgnoreCase("Y")){
            if (!list.get(position).getVOLTAGE_LEVEL().equalsIgnoreCase(list.get(position).getREF_VOLTAGE())){
                holder.mapped_pole.setVisibility(View.VISIBLE);
                holder.tv_loc_number.setVisibility(View.GONE);
                holder.update_locations.setBackground(context.getResources().getDrawable(R.drawable.disable_border));
                holder.update_locations.setTextColor(context.getResources().getColor(R.color.lite_black));
                //  holder.location_no.setText(list.get(position).getPOLE_NO()+"(Mapped Location)");
            }
            else {
                holder.mapped_pole.setVisibility(View.GONE);
                holder.tv_loc_number.setVisibility(View.VISIBLE);
            }
        }else {
            if (list.get(position).getPOLE_LOCATION_TYPE().equalsIgnoreCase("Mapped Location")){
                holder.mapped_pole.setVisibility(View.VISIBLE);
                holder.tv_loc_number.setVisibility(View.GONE);

                if (!list.get(position).getVOLTAGE_LEVEL().equalsIgnoreCase("33KV")){
                    holder.update_locations.setBackground(context.getResources().getDrawable(R.drawable.disable_border));
                    holder.update_locations.setTextColor(context.getResources().getColor(R.color.lite_black));
                }

                // holder.location_no.setText(list.get(position).getPOLE_NO()+"(Mapped Location)");
            }else {
                holder.mapped_pole.setVisibility(View.GONE);
                holder.tv_loc_number.setVisibility(View.VISIBLE);
            }
        }


        holder.update_locations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // System.out.println("IS TRANSMITTED LOCATION : "+list.get(position).getIS_TRANSMITTED_LOCATION());

                if (list.get(position).getIS_TRANSMITTED_LOCATION().equalsIgnoreCase("Y")){
                   // System.out.println("VOLTAGE LEVEL : "+list.get(position).getVOLTAGE_LEVEL());
                   // System.out.println("REF VOLTAGE : "+list.get(position).getREF_VOLTAGE());
                    if (!list.get(position).getVOLTAGE_LEVEL().equalsIgnoreCase(list.get(position).getREF_VOLTAGE())){
                        new AlertDialog.Builder(context)
                                .setTitle("MAPPED LOCATION!!")
                                .setMessage("This is a Multi Circuit Location. If you want to update this Location then Update in "+list.get(position).getREF_VOLTAGE()+" Survey")

                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton("OKAY", (dialog, which) -> {
                                    dialog.dismiss();
                                    /*Intent intent = new Intent(context, EditLocationsMap.class);
                                    intent.putExtra("data", list.get(position));
                                    intent.putExtra("GIS_ID", GIS_ID);
                                    context.startActivity(intent);*/
                                })

                                // A null listener allows the button to dismiss the dialog and take no further action.
                                .setNegativeButton(android.R.string.no, null)
                                //.setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }else {
                        Intent intent = new Intent(context, EditLocationsMap.class);
                        intent.putExtra("data", list.get(position));
                        intent.putExtra("GIS_ID", GIS_ID);
                        intent.putExtra("FROM","GIS");
                        intent.putExtra("POLE_NO",list.get(position).getPOLE_NO());

                        context.startActivity(intent);
                    }
                }else {
                    if (list.get(position).getPOLE_LOCATION_TYPE().equalsIgnoreCase("Mapped Location") && !list.get(position).getVOLTAGE_LEVEL().equalsIgnoreCase("33KV")){
                        String mV = list.get(position).getVOLTAGE_LEVEL().equalsIgnoreCase("11KV") ? "33KV" : "11KV" ;
                        new AlertDialog.Builder(context)
                                .setTitle("MAPPED LOCATION!!")
                                //  .setMessage("This location is already Mapped in "+list.get(position).getREF_VOLTAGE()+" Survey. If you want to update this Location then Update in "+list.get(position).getREF_VOLTAGE()+" Survey")
                                .setMessage("This is a Multi Circuit Location. If you want to update this Location then Update in the survey where you marked it as mapped pole")

                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton("OKAY", (dialog, which) -> {
                                    dialog.dismiss();
                                    /*Intent intent = new Intent(context, EditLocationsMap.class);
                                    intent.putExtra("data", list.get(position));
                                    intent.putExtra("GIS_ID", GIS_ID);
                                    context.startActivity(intent);*/
                                })

                                // A null listener allows the button to dismiss the dialog and take no further action.
                                .setNegativeButton(android.R.string.no, null)
                                //
                                // .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                    else {
                        Intent intent = new Intent(context, EditLocationsMap.class);
                        intent.putExtra("data", list.get(position));
                        intent.putExtra("GIS_ID", GIS_ID);
                        intent.putExtra("FROM","GIS");
                        context.startActivity(intent);
                    }
                }



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

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView update_locations,update_attributes;
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
