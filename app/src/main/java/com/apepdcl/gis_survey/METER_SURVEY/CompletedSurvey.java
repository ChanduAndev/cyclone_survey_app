package com.apepdcl.gis_survey.METER_SURVEY;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.estimates.models.DTRFeederMaster;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by SOUBHAGYA SWAIN on 12-06-2026.
 */
public class CompletedSurvey extends AppCompatActivity {

    ImageView back_layout;

    ArrayList<AssignedDtrModel> list;
    PendingDtrAdapter adapter;
    RecyclerView dtr_rv;
    LinearLayout main_ui_layout;
    LinearLayout loader_layout;

    ProgressDialog progressDialog;

    private RetrofitServiceMeter retrofitServiceMeter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.completed_survey);
        getSupportActionBar().hide();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setButton("Cancel", (dialog, which) -> {
            progressDialog.dismiss();
        });
        progressDialog.setCancelable(false);

        back_layout = findViewById(R.id.back_layout);
        dtr_rv = findViewById(R.id.dtr_rv);
        main_ui_layout = findViewById(R.id.main_ui_layout);
        loader_layout = findViewById(R.id.loader_layout);
        back_layout.setOnClickListener(view -> onBackPressed());

        list = new ArrayList<>();
        if (ConnectivityChecking.checkConnection(CompletedSurvey.this)) {
            getPendingDTRList();
        } else {
            CommanUtilits.internetDialog(CompletedSurvey.this);
        }

    }

    private void getPendingDTRList() {

        loader_layout.setVisibility(View.VISIBLE);
        main_ui_layout.setVisibility(View.GONE);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("MOBILE_NO", sharedPreferences.getString(MeterConstants.METER_MOBILE_NO,""));
        dataMap.put("VERSION", MobileConstants.CONSUMERS);

        Gson gson = new Gson();
        JsonObject jsonObject = gson.toJsonTree(dataMap).getAsJsonObject();

        retrofitServiceMeter = RetrofitClientInstanceMeter
                .getRetrofitInstance()
                .create(RetrofitServiceMeter.class);

        Call<JsonObject> responseVoCall = retrofitServiceMeter.getAssignedDTrs(jsonObject);

        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                loader_layout.setVisibility(View.GONE);
                main_ui_layout.setVisibility(View.VISIBLE);

                if (response.body() == null) {
                    Toast.makeText(CompletedSurvey.this, "Server Busy. Try Again.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 🔹 CLEAR LISTS
                list.clear();

                JsonObject surveyedResponseVo = response.body();

                if (!surveyedResponseVo.has("STATUS") || surveyedResponseVo.get("STATUS").isJsonNull()) {
                    Toast.makeText(CompletedSurvey.this,
                            "Invalid response from server: STATUS missing",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                    String errorMessage = surveyedResponseVo.has("ERR_MSG")
                            ? surveyedResponseVo.get("ERR_MSG").getAsString()
                            : "Unknown error occurred";
                    Toast.makeText(CompletedSurvey.this, errorMessage, Toast.LENGTH_SHORT).show();
                    return;
                }

                //JsonObject object = surveyedResponseVo.getAsJsonObject("DATA");
                JsonArray dtr_array = surveyedResponseVo.getAsJsonArray("DATA");

                for (int i = 0; i < dtr_array.size(); i++) {

                    JsonObject jsonObject1 = dtr_array.get(i).getAsJsonObject();

                    String isMapped = jsonObject1.has("IS_LTCKT_AVAILABLE") && !jsonObject1.get("IS_LTCKT_AVAILABLE").isJsonNull()
                            ? jsonObject1.get("IS_LTCKT_AVAILABLE").getAsString()
                            : "";

                    String isCompleted = jsonObject1.has("SURVEY_COMPLETED") && !jsonObject1.get("SURVEY_COMPLETED").isJsonNull()
                            ? jsonObject1.get("SURVEY_COMPLETED").getAsString()
                            : "";

                    if (isCompleted.equals("Y")){
                        AssignedDtrModel master = new AssignedDtrModel();

                        master.setREC_ID(jsonObject1.get("REC_ID").isJsonNull() ? "" : jsonObject1.get("REC_ID").getAsString());
                        master.setUSERID(jsonObject1.get("USERID").isJsonNull() ? "" : jsonObject1.get("USERID").getAsString());
                        master.setUSER_NAME(jsonObject1.get("USER_NAME").isJsonNull() ? "" : jsonObject1.get("USER_NAME").getAsString());
                        master.setROLE(jsonObject1.get("ROLE").isJsonNull() ? "" : jsonObject1.get("ROLE").getAsString());
                        master.setDTR_CODE(jsonObject1.get("DTR_CODE").isJsonNull() ? "" : jsonObject1.get("DTR_CODE").getAsString());
                        master.setDTR_LOCATION(jsonObject1.get("DTR_LOCATION").isJsonNull() ? "" : jsonObject1.get("DTR_LOCATION").getAsString());
                        master.setDTR_GLAT(jsonObject1.get("DTR_GLAT").isJsonNull() ? "" : jsonObject1.get("DTR_GLAT").getAsString());
                        master.setDTR_GLONG(jsonObject1.get("DTR_GLONG").isJsonNull() ? "" : jsonObject1.get("DTR_GLONG").getAsString());
                        master.setIS_LTCKT_AVAILABLE(jsonObject1.get("IS_LTCKT_AVAILABLE").isJsonNull() ? "" : jsonObject1.get("IS_LTCKT_AVAILABLE").getAsString());
                        master.setCREATED_DT(jsonObject1.get("CREATED_DT").isJsonNull() ? "" : jsonObject1.get("CREATED_DT").getAsString());
                        master.setSURVEY_COMPLETED(jsonObject1.get("SURVEY_COMPLETED").isJsonNull() ? "" : jsonObject1.get("SURVEY_COMPLETED").getAsString());
                        master.setCOMPLETION_DT(jsonObject1.get("COMPLETION_DT").isJsonNull() ? "" : jsonObject1.get("COMPLETION_DT").getAsString());
                        master.setNO_OF_SCS(jsonObject1.get("NO_OF_SCS").isJsonNull() ? "" : jsonObject1.get("NO_OF_SCS").getAsString());
                        master.setSECTION_NAME(jsonObject1.get("SECTION_NAME").isJsonNull() ? "" : jsonObject1.get("SECTION_NAME").getAsString());
                        master.setUNMAPPED_POLES(jsonObject1.get("UNMAPPED_POLES").isJsonNull() ? "" : jsonObject1.get("UNMAPPED_POLES").getAsString());
                        master.setUNMAPPED_SERVICES(jsonObject1.get("UNMAPPED_SERVICES").isJsonNull() ? "" : jsonObject1.get("UNMAPPED_SERVICES").getAsString());
                        master.setLOAD_TYPE_ASGNED(jsonObject1.get("LOAD_TYPE_ASGNED").isJsonNull() ? "" : jsonObject1.get("LOAD_TYPE_ASGNED").getAsString());
                        master.setLOAD_TYPE_SURVEYED(jsonObject1.get("LOAD_TYPE_SURVEYED").isJsonNull() ? "" : jsonObject1.get("LOAD_TYPE_SURVEYED").getAsString());
                        master.setSECTION_ID(jsonObject1.get("SECTION_ID").isJsonNull() ? "" : jsonObject1.get("SECTION_ID").getAsString());
                        master.setMOBILE_NO(jsonObject1.get("MOBILE_NO").isJsonNull() ? "" : jsonObject1.get("MOBILE_NO").getAsString());



                        list.add(master);
                    }




                }

                dtr_rv.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CompletedSurvey.this);
                dtr_rv.setLayoutManager(layoutManager);
                dtr_rv.setFocusable(false);
                adapter = new PendingDtrAdapter(list);
                dtr_rv.setAdapter(adapter);


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loader_layout.setVisibility(View.GONE);
            }
        });
    }

    public class PendingDtrAdapter extends RecyclerView.Adapter<PendingDtrAdapter.MyViewHolder> {

        ArrayList<AssignedDtrModel> list;

        public PendingDtrAdapter(ArrayList<AssignedDtrModel> list) {
            this.list = list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(CompletedSurvey.this).inflate(R.layout.dtr_list_adapter, parent, false);
            return new PendingDtrAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            int sl_no = position + 1;
            holder.sl_no.setText("" + sl_no);
            holder.tv_dtr_location_name.setText(list.get(position).getDTR_LOCATION());
            holder.tv_dtr_code.setText(list.get(position).getDTR_CODE());
            holder.tv_no_of_services.setText(list.get(position).getNO_OF_SCS());
            holder.tv_section.setText(list.get(position).getSECTION_NAME());
            holder.btn_start.setVisibility(View.GONE);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView sl_no,tv_dtr_location_name,tv_dtr_code, tv_no_of_services, tv_section;
            TextView btn_start;

            public MyViewHolder(View itemView) {
                super(itemView);
                sl_no = itemView.findViewById(R.id.sl_no);
                tv_dtr_location_name = itemView.findViewById(R.id.tv_dtr_location_name);
                tv_dtr_code = itemView.findViewById(R.id.tv_dtr_code);
                tv_no_of_services = itemView.findViewById(R.id.tv_no_of_services);
                tv_section = itemView.findViewById(R.id.tv_section);
                btn_start = itemView.findViewById(R.id.btn_start);
                // Initialize your item views here
            }
        }

    }
}
