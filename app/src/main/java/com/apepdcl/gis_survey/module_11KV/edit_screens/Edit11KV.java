package com.apepdcl.gis_survey.module_11KV.edit_screens;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.estimates.WorkInvolmentsActivity;
import com.apepdcl.gis_survey.estimates.new_module.EstimatesMap;
import com.apepdcl.gis_survey.estimates.new_module.NewModel;
import com.apepdcl.gis_survey.estimates.sou.EditLocationsMap;
import com.apepdcl.gis_survey.module_11KV.ArcGISDatabase;
import com.apepdcl.gis_survey.module_11KV.api_calls.RetrofitClientInstance;
import com.apepdcl.gis_survey.module_11KV.api_calls.RetrofitService;
import com.apepdcl.gis_survey.module_11KV.models.SurveyVo;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Edit11KV extends AppCompatActivity implements Edit11kvAdapterAssetMapping.OnItemClickListener {
    private ArcGISDatabase arcGISDatabase;
    private RecyclerView edit_rv;
    private RelativeLayout back_layout, close_layout;
    Edit11kvAdapterAssetMapping adapter;
    ArrayList<SurveyVo> list;
    ArrayList<com.apepdcl.gis_survey.module_33KV.vo.SurveyVo> list33KV;
    private RetrofitService retrofitService;


    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_11kv_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        progressDialog = CommanUtilits.progressDialog(this);
        list = new ArrayList<>();
        list33KV = new ArrayList<>();
        arcGISDatabase = new ArcGISDatabase(this);
        edit_rv = findViewById(R.id.edit_rv);
        back_layout = findViewById(R.id.back_layout);
        close_layout = findViewById(R.id.close_layout);
        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

        close_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
        list = (ArrayList<SurveyVo>) getIntent().getSerializableExtra("data");
        setRecyclerView();


        //list = arcGISDatabase.getSurveyData("");

    }

    private void setRecyclerView() {
        edit_rv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        edit_rv.setLayoutManager(layoutManager);
        edit_rv.setFocusable(false);
        adapter = new Edit11kvAdapterAssetMapping(list, this, this);
        edit_rv.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position, SurveyVo model) {
        showCustomEditDialog(model);

    }

    private void showCustomEditDialog(SurveyVo model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_options_asset, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();


        // Find views
        LinearLayout optionAll = dialogView.findViewById(R.id.option_all);
        LinearLayout optionPole = dialogView.findViewById(R.id.option_pole);
        LinearLayout optionDTR = dialogView.findViewById(R.id.option_source);
        LinearLayout optionSourcePole = dialogView.findViewById(R.id.option_source_pole);
        LinearLayout option_boq = dialogView.findViewById(R.id.option_boq);
        LinearLayout option_httvr = dialogView.findViewById(R.id.option_httvr);

        optionPole.setOnClickListener(v -> {
            editPoleAttributes(model);
            dialog.dismiss();
        });

        optionSourcePole.setOnClickListener(v -> {
            editSourcePole(model);
            dialog.dismiss();
        });


        dialog.show();
    }

    private void editSourcePole(SurveyVo model) {
        Intent intent = new Intent(this, EditPoleAttributesAsset.class);
        intent.putExtra("FEEDER_ID", model.getFeeder_id());
        intent.putExtra("POLE_NO", model.getPole_no());
        intent.putExtra("DATA_MODEL", model);
        intent.putExtra("FROM", "SOURCE");
        startActivity(intent);

    }

    private void editPoleAttributes(SurveyVo model) {
        Intent intent = new Intent(this, EditPoleAttributesAsset.class);
        intent.putExtra("FEEDER_ID", model.getFeeder_id());
        intent.putExtra("POLE_NO", model.getPole_no());
        intent.putExtra("DATA_MODEL", model);
        intent.putExtra("FROM", "POLE");
        startActivity(intent);

    }

    @Override
    public void onUpdateLocationClick(int pos, SurveyVo model) {
        if (ConnectivityChecking.checkConnection(Edit11KV.this)) {
            getPoleData(model.getFeeder_id(), String.valueOf(model.getPole_no()), pos);
        } else {
            CommanUtilits.internetDialog(Edit11KV.this);
        }

    }

    private void getPoleData(String feederId, String poleNo, int pos) {
        if (!ConnectivityChecking.checkConnection(Edit11KV.this)) {
            Toast.makeText(getApplicationContext(),
                    "For accessing this feature, internet connection is required",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // Show progress dialog (if you already have a reusable one, use that)
        progressDialog = new ProgressDialog(Edit11KV.this);
        progressDialog.setMessage("Fetching pole data...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        JsonObject postParam = new JsonObject();
        postParam.addProperty("FEEDER_ID", feederId);
        postParam.addProperty("POLE_NO", poleNo);

        RetrofitService retrofitService =
                RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        Call<JsonObject> call = retrofitService.getPoleData(postParam);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    if (response.body() == null) {
                        Toast.makeText(Edit11KV.this, "Server busy, please try again", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        return;
                    }

                    JsonObject jsonObject = response.body();
                    String status = jsonObject.has("STATUS") ? jsonObject.get("STATUS").getAsString() : "FALSE";

                    if ("TRUE".equalsIgnoreCase(status)) {
                        JsonArray responseArray = jsonObject.getAsJsonArray("RESPONSE");

                        if (responseArray != null && responseArray.size() > 0) {
                            JsonObject poleObj = responseArray.get(0).getAsJsonObject();

                            String poleNoRes = poleObj.has("POLE_NO") ? poleObj.get("POLE_NO").getAsString() : "";
                            String fromPole = poleObj.has("FROM_POLE") ? poleObj.get("FROM_POLE").getAsString() : "";
                            String lat = poleObj.has("GLAT") ? poleObj.get("GLAT").getAsString() : "";
                            String lon = poleObj.has("GLONG") ? poleObj.get("GLONG").getAsString() : "";
                            String feederIdRes = poleObj.has("FEEDER_ID") ? poleObj.get("FEEDER_ID").getAsString() : "";
                            String poleConfig = poleObj.has("POLE_CONFIG") ? poleObj.get("POLE_CONFIG").getAsString() : "";
                            String poleType = poleObj.has("POLE_TYPE") ? poleObj.get("POLE_TYPE").getAsString() : "";
                            String id = poleObj.has("ID") ? poleObj.get("ID").getAsString() : "";

                            Intent intent = new Intent(Edit11KV.this, EditLocationsMap.class);
                            intent.putExtra("FROM", "ASSETMAPPING");
                            intent.putExtra("data", list.get(pos));
                            intent.putExtra("POLE_NO", poleNoRes);
                            intent.putExtra("FEEDER_ID", feederIdRes);
                            intent.putExtra("ID", id);
                            //intent.putExtra("GIS_ID", "GIS_ID");
                            startActivity(intent);

                        }

                    } else {
                        String errorMsg = jsonObject.has("ERR_MSG") && !jsonObject.get("ERR_MSG").isJsonNull()
                                ? jsonObject.get("ERR_MSG").getAsString()
                                : "Unknown error";

                        Toast.makeText(Edit11KV.this, errorMsg, Toast.LENGTH_SHORT).show();

                        if ("No Records".equalsIgnoreCase(errorMsg)) {
                            onBackPressed();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Edit11KV.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                } finally {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(Edit11KV.this, "Network error, please try again", Toast.LENGTH_LONG).show();
            }
        });
    }


}

