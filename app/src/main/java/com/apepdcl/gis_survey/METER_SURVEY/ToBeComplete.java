package com.apepdcl.gis_survey.METER_SURVEY;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.PMI.new_screens.PMINewHome;
import com.apepdcl.gis_survey.PMI.new_screens.Transmit11KV;
import com.apepdcl.gis_survey.estimates.models.DTRFeederMaster;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by SOUBHAGYA SWAIN on 12-06-2026.
 */
public class ToBeComplete extends AppCompatActivity {

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
        setContentView(R.layout.to_be_complete);
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
        if (ConnectivityChecking.checkConnection(ToBeComplete.this)) {
            getPendingDTRList();
        } else {
            CommanUtilits.internetDialog(ToBeComplete.this);
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
                    Toast.makeText(ToBeComplete.this, "Server Busy. Try Again.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 🔹 CLEAR LISTS
                list.clear();

                JsonObject surveyedResponseVo = response.body();

                if (!surveyedResponseVo.has("STATUS") || surveyedResponseVo.get("STATUS").isJsonNull()) {
                    Toast.makeText(ToBeComplete.this,
                            "Invalid response from server: STATUS missing",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                    String errorMessage = surveyedResponseVo.has("ERR_MSG")
                            ? surveyedResponseVo.get("ERR_MSG").getAsString()
                            : "Unknown error occurred";
                    Toast.makeText(ToBeComplete.this, errorMessage, Toast.LENGTH_SHORT).show();
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
                        continue; // Skip this DTR if survey is completed
                    }

                    // 🔹 BUILD MASTER OBJECT ONCE
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
                    dtr_rv.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ToBeComplete.this);
                    dtr_rv.setLayoutManager(layoutManager);
                    dtr_rv.setFocusable(false);
                    adapter = new PendingDtrAdapter(list);
                    dtr_rv.setAdapter(adapter);

                }


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
            View view = LayoutInflater.from(ToBeComplete.this).inflate(R.layout.dtr_list_adapter, parent, false);
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
            holder.btn_start.setText("Complete Survey");

            holder.btn_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openAlertDialog(list.get(position));

                }
            });
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

    private void openAlertDialog(AssignedDtrModel compDTR) {
        Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.customized_dialog, null);
        dialog.setContentView(view);

        // Views
        TextView title = view.findViewById(R.id.title);
        TextView msg = view.findViewById(R.id.message);
        TextView okBtn = view.findViewById(R.id.okBtn);
        TextView cancelBtn = view.findViewById(R.id.cancelBtn);
        TextView tv_est_no = view.findViewById(R.id.tv_est_no);
        TextView tv_gis_id = view.findViewById(R.id.tv_gis_id);
        ImageView iv_icon = view.findViewById(R.id.iv_icon);
        LinearLayout estimate_details = view.findViewById(R.id.estimate_details);
        estimate_details.setVisibility(View.GONE);
        iv_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.warning));
        // Set Data
        title.setText("Complete Survey");
        msg.setText("Are there any poles not mapped for this DTR ? ");

        // Show only OK button
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.VISIBLE);
        okBtn.setText("Okay");
        cancelBtn.setText("No");

        // Click action
        okBtn.setOnClickListener(v -> {
            showUnMappedDialog(compDTR);
            dialog.dismiss();
        });

        cancelBtn.setOnClickListener(v -> {
            callCompleteAPI(compDTR,"","",new JsonArray());
            dialog.dismiss();
        });

        dialog.setCancelable(false);

        // Transparent background
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        dialog.show();
    }

    private void openPendingSurveyDialog(AssignedDtrModel compDTR) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Complete Survey");
        builder.setMessage("Are there any poles not mapped for this DTR ? ");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", (dialog, which) -> {
            dialog.dismiss();
            showUnMappedDialog(compDTR);
        });

        builder.setNegativeButton("No", (dialog, which) -> {
            dialog.dismiss();
            callCompleteAPI(compDTR,"","",new JsonArray());
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void showUnMappedDialog(AssignedDtrModel compDTR) {
        Dialog payDialog = new Dialog(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        payDialog.setContentView(R.layout.unmapped_dialog);
        payDialog.getWindow().setLayout(width, height / 2);
        payDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        payDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        // Views
        TextView submit = payDialog.findViewById(R.id.submit);
        TextView cancel = payDialog.findViewById(R.id.cancel);
        EditText unmapped_services = payDialog.findViewById(R.id.unmapped_services);
        EditText unmapped_poles = payDialog.findViewById(R.id.unmapped_poles);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (unmapped_poles.getText().toString().isEmpty()){
                    Toast.makeText(ToBeComplete.this, "Please Enter un-mapped poles", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (unmapped_services.getText().toString().isEmpty()){
                    Toast.makeText(ToBeComplete.this, "Please Enter un-mapped services", Toast.LENGTH_SHORT).show();
                    return;
                }

                payDialog.dismiss();
                callCompleteAPI(compDTR, unmapped_poles.getText().toString(), unmapped_services.getText().toString(), new JsonArray());
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payDialog.dismiss();
            }
        });


        payDialog.show();
    }


    private void callCompleteAPI(AssignedDtrModel compDTR,String unPoles, String unServices, JsonArray unServicesList) {
        progressDialog.show();
        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("APP_VERSION", MobileConstants.CONSUMERS);
        requestObject.addProperty("DTR_CODE", compDTR.getDTR_CODE());
        requestObject.addProperty("UNMAPPED_POLES", unPoles);
        requestObject.addProperty("UNMAPPED_SERVICES", unServices);
        requestObject.add("UNMAPPED_SERVICES_LIST", unServicesList);
        requestObject.addProperty("SURVEY_COMPLETED", "Y");


        retrofitServiceMeter = RetrofitClientInstanceMeter.getRetrofitInstance().create(RetrofitServiceMeter.class);

        Call<JsonObject> responseVoCall = retrofitServiceMeter.completeSurvey(requestObject);

        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {



                if (response.body() == null) {
                    progressDialog.dismiss();
                    Toast.makeText(ToBeComplete.this, "Server Busy. Try Again.", Toast.LENGTH_SHORT).show();
                    return;
                }



                JsonObject surveyedResponseVo = response.body();

                if (!surveyedResponseVo.has("STATUS") || surveyedResponseVo.get("STATUS").isJsonNull()) {
                    Toast.makeText(ToBeComplete.this,
                            "Invalid response from server: STATUS missing",
                            Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }

                if (!MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                    String errorMessage = surveyedResponseVo.has("ERR_MSG")
                            ? surveyedResponseVo.get("ERR_MSG").getAsString()
                            : "Unknown error occurred";
                    Toast.makeText(ToBeComplete.this, errorMessage, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }

                progressDialog.dismiss();
                Toast.makeText(ToBeComplete.this, "Survey Completed Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ToBeComplete.this, MeterSurveyHome.class);
                startActivity(intent);
                finish();


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }
}
