package com.apepdcl.gis_survey.estimates.sou;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.LoginActivity;

import java.util.ArrayList;

public class CompletedBOQAdapter extends RecyclerView.Adapter<CompletedBOQAdapter.MyViewHolder> {

    ArrayList<BoqMaterialModel> list;
    Context context;

    public CompletedBOQAdapter(ArrayList<BoqMaterialModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public CompletedBOQAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.boq_material_adapter, parent, false);;
//        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")){
//            view = LayoutInflater.from(context).inflate(R.layout.boq_material_adapter_solar, parent, false);
//        }else {
//            view = LayoutInflater.from(context).inflate(R.layout.boq_material_adapter, parent, false);
//
//        }
        return new CompletedBOQAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompletedBOQAdapter.MyViewHolder holder, int position) {
        BoqMaterialModel item = list.get(position);
//        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")){
//            holder.dtr_16.setText(item.getDTR16());
//            holder.dtr_25.setText(item.getDTR25());
//            holder.dtr_40.setText(item.getDTR40());
//            holder.dtr_63.setText(item.getDTR63());
//            holder.dtr_100.setText(item.getDTR100());
//            holder.dtr_160.setText(item.getDTR160());
//            holder.ln_63.setText(item.getLN63());
//            holder.ln_63_11.setText(item.getLC6311());
//            holder.ln_11.setText(item.getLN11());
//            holder.lc_lt_3w_5w.setText(item.getLCLT3W5W());
//            holder.ln_lt_3w.setText(item.getLNLT3W());
//            holder.ln_lt_4w.setText(item.getLNLT4W());
//            holder.ln_lt_5w.setText(item.getLNLT5W());
//
//            System.out.println("RENDERED_DATA--->"+item.getDTR16());
//
//        }else {
//            holder.qty.setText(item.getQUANTITY());
//            holder.uom.setText(item.getUNIT());
//            holder.desc.setText(item.getMATERIAL_DESCRIPTION());
//            holder.voltage.setText(item.getDATA_ID());
//            holder.qty.setText(item.getQUANTITY());
//        }

        holder.qty.setText(item.getQUANTITY());
        holder.uom.setText(item.getUNIT());
        holder.desc.setText(item.getMATERIAL_DESCRIPTION());
        holder.voltage.setText(item.getDATA_ID());
        holder.qty.setText(item.getQUANTITY());



        //item.setQTY("0");

       /* holder.qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                item.setQUANTITY(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView qty;
        TextView dtr_16, dtr_25, dtr_40, dtr_63, dtr_100, dtr_160, ln_63, ln_63_11, ln_11, lc_lt_3w_5w, ln_lt_3w, ln_lt_4w, ln_lt_5w;
        TextView uom, desc,voltage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            qty = itemView.findViewById(R.id.qty);
            uom = itemView.findViewById(R.id.uom);
            desc = itemView.findViewById(R.id.desc);
            voltage = itemView.findViewById(R.id.voltage);
            dtr_16 = itemView.findViewById(R.id.dtr_16);
            dtr_25 = itemView.findViewById(R.id.dtr_25);
            dtr_40 = itemView.findViewById(R.id.dtr_40);
            dtr_63 = itemView.findViewById(R.id.dtr_63);
            dtr_100 = itemView.findViewById(R.id.dtr_100);
            dtr_160 = itemView.findViewById(R.id.dtr_160);
            ln_63 = itemView.findViewById(R.id.ln_63);
            ln_63_11 = itemView.findViewById(R.id.ln_63_11);
            ln_11 = itemView.findViewById(R.id.ln_11);
            lc_lt_3w_5w = itemView.findViewById(R.id.lc_lt_3w_5w);
            ln_lt_3w = itemView.findViewById(R.id.ln_lt_3w);
            ln_lt_4w = itemView.findViewById(R.id.ln_lt_4w);
            ln_lt_5w = itemView.findViewById(R.id.ln_lt_5w);

        }
    }

}
