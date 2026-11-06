package com.apepdcl.gis_survey.estimates;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.estimates.models.PurposeModel;
import com.apepdcl.gis_survey.estimates.new_module.NewModel;
import com.apepdcl.gis_survey.estimates.sou.InfoAdapter;
import com.apepdcl.gis_survey.estimates.sou.OfflineLTActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LtSurveyAdapter extends RecyclerView.Adapter<LtSurveyAdapter.MyViewHolder> {

    ArrayList<NewModel> list;
    Context context;
    private OnItemClickListener listener;

    public LtSurveyAdapter(ArrayList<NewModel> list, Context context, OnItemClickListener listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(NewModel model, String selectedCKT);

        void onEditItemClick(NewModel model, String selectedCKT);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.lt_survey_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        System.out.println("DTR_LOCATION_IS" + list.get(position).getDTR_LOCATION());
        int dtr_no = position + 1;
       // holder.dtr_heading.setText("DTR " + dtr_no + " DETAILS");
        holder.dtr_heading.setText("" + dtr_no);
        holder.dtr_name.setText(list.get(position).getDTR_LOCATION());
        holder.dtr_structure_code.setText(list.get(position).getDTR_STRUCTURE_CODE());
        int polesCount = getPolesCount(list.get(position).getDTR_STRUCTURE_CODE());
        holder.no_locations.setText("" + polesCount);



        //NEW CODE AFTER SOUBHAGYA
        if (polesCount == 0) {
            holder.continue_survey.setText("START SURVEY");
        } else {
            holder.continue_survey.setText("CONTINUE SURVEY");
            if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")){
                holder.edit_survey.setVisibility(View.VISIBLE);
            }else {
                holder.edit_survey.setVisibility(View.VISIBLE);
            }
        }

        if (OfflineLTActivity.IS_OFFLINE) {
            holder.edit_survey.setVisibility(View.GONE);

        }

        List<String> list_ckt = new ArrayList<>();
        list_ckt.add("Select");

        int countCkt = list.get(position).getCOUNT_CKT();
        String dtrCode = list.get(position).getDTR_STRUCTURE_CODE();

        for (int i = 0; i < countCkt; i++) {
            list_ckt.add(dtrCode + "_LTCKT_" + (char) ('A' + i));
        }


        ArrayAdapter<String> adapter_ckt = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, list_ckt);
        holder.sp_circuit.setAdapter(adapter_ckt);
        holder.sp_circuit.setSelection(0, true);


        holder.continue_survey.setOnClickListener(v -> {
            String selectedCircuit = holder.sp_circuit.getSelectedItem().toString();
            String selectedStructureCode = holder.dtr_structure_code.getText().toString();


            if (selectedCircuit.equalsIgnoreCase("Select")) {
                Toast.makeText(context, "Please Select LT Circuit Code", Toast.LENGTH_SHORT).show();
                return;
            }

            System.out.println("DTR_SURVEY_TYPE" + list.get(position).getDTR_SURVEY_TYPE());

            if (list.get(position).getDTR_SURVEY_TYPE().equalsIgnoreCase("Existing")
                    || list.get(position).getDTR_SURVEY_TYPE().equalsIgnoreCase("Enhancement") ||
                    list.get(position).getDTR_SURVEY_TYPE().equalsIgnoreCase("ENHANCED")) {
            } else {
                if (polesCount == 0 && !selectedCircuit.endsWith("_LTCKT_A")) {
                    Toast.makeText(context, "Please continue with First LT Circuit (LTCKT_A)", Toast.LENGTH_SHORT).show();
                    return;
                }



                if (!selectedCircuit.endsWith("_LTCKT_A")) {
                    char selectedChar = selectedCircuit.charAt(selectedCircuit.length() - 1);
                    char previousChar = (char) (selectedChar - 1);
                    String previousCircuitCode = selectedCircuit.substring(0, selectedCircuit.length() - 1) + previousChar;
                    ArrayList<NewModel> allPoles = new ArcGISDatabaseEstimates(context).getNewSurveyData(list.get(0).getEST_NO(), "LT", false, list.get(0).getGIS_ID());
                    int polesCount_Lt = 0;
                    for (int i = 0; i < allPoles.size(); i++) {
                        if (allPoles.get(i).getLT_AVAILABLE().equalsIgnoreCase(previousCircuitCode)) {
                            polesCount_Lt++;
                        }
                    }

                    int previousPolesCount = getPolesCount(previousCircuitCode);

                    if (polesCount_Lt == 0) {
                        Toast.makeText(context, "Please complete survey LT Circuit: " + previousCircuitCode, Toast.LENGTH_SHORT).show();
                        System.out.println("DATA_POLES" + polesCount_Lt);
                        System.out.println("TOAST" + previousCircuitCode);
                        return;
                    }
                }
            }


            // All checks passed
            if (listener != null) {
                listener.onItemClick(list.get(position), selectedCircuit);
            }
        });


        //NEW CODE AFTER SOUBHAGYA
        holder.edit_survey.setOnClickListener(v -> {
            if (listener != null) {
                String selectedLTCKT = getSelectedLTCKT(holder);
                System.out.println("SELECTED_CKT :" + selectedLTCKT);

                listener.onEditItemClick(list.get(position), selectedLTCKT);
            }
        });


    }


    private int getPolesCount(String dtrStructureCode) {
        int totalPolesLT = 0;
        ArrayList<NewModel> allPoles = new ArcGISDatabaseEstimates(context).getNewSurveyData(list.get(0).getEST_NO(), "LT", false, list.get(0).getGIS_ID());
        for (int i = 0; i < allPoles.size(); i++) {
            if (allPoles.get(i).getDTR_STRUCTURE_CODE().equalsIgnoreCase(dtrStructureCode)) {
                totalPolesLT++;
            }
        }

        return totalPolesLT;

    }

    private String getSelectedLTCKT(MyViewHolder holder) {
        if (list.isEmpty() || list.get(0) == null) {
            return "";
        }

        Object selectedItem = holder.sp_circuit.getSelectedItem();
        if (selectedItem == null) {
            return "";
        }

        String selectedCircuit = selectedItem.toString();
        String selectedCKT = "";

        ArrayList<NewModel> allPoles = new ArcGISDatabaseEstimates(context)
                .getNewSurveyData(list.get(0).getEST_NO(), "LT", false, list.get(0).getGIS_ID());

        for (int i = 0; i < allPoles.size(); i++) {
            if (allPoles.get(i).getLT_AVAILABLE().contains(selectedCircuit)) {
                selectedCKT = selectedCircuit;
            }
        }
        return selectedCKT;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView dtr_heading, no_locations, dtr_name, dtr_structure_code, edit_survey, continue_survey;
        Spinner sp_circuit;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            dtr_heading = itemView.findViewById(R.id.dtr_heading);
            no_locations = itemView.findViewById(R.id.no_locations);
            dtr_name = itemView.findViewById(R.id.dtr_name);
            dtr_structure_code = itemView.findViewById(R.id.dtr_structure_code);
            sp_circuit = itemView.findViewById(R.id.sp_circuit);
            edit_survey = itemView.findViewById(R.id.edit_survey);
            continue_survey = itemView.findViewById(R.id.continue_survey);
        }
    }
}
