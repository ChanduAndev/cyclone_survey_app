package com.apepdcl.gis_survey.lt_edit_screens;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
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
import com.apepdcl.gis_survey.estimates.sou.EditLocationsMap;

import com.apepdcl.gis_survey.retrofitservice.RetrofitClientInstance;
import com.apepdcl.gis_survey.retrofitservice.RetrofitService;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.vo.SurveyDataVo;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditLT extends AppCompatActivity implements EditLTAdapterAssetMapping.OnItemClickListener {
    private ProgressDialog progressDialog;
    EditLTAdapterAssetMapping adapter;
    ArrayList<SurveyDataVo> list;
    private RecyclerView edit_rv;
    private ImageView back_layout, close_layout;
    private RetrofitService retrofitService;
    private String CIRCUIT_CODE = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_lt_asset);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        edit_rv = findViewById(R.id.edit_rv);
        back_layout = findViewById(R.id.back_layout);
        //close_layout = findViewById(R.id.close_layout);
        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });


        list = (ArrayList<SurveyDataVo>) getIntent().getSerializableExtra("data");
        CIRCUIT_CODE = getIntent().getStringExtra("CIRCUIT_CODE");

        setRecyclerView();
    }

    private void setRecyclerView() {
        edit_rv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        edit_rv.setLayoutManager(layoutManager);
        edit_rv.setFocusable(false);
        adapter = new EditLTAdapterAssetMapping(list, this, this, "Online");
        edit_rv.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position, SurveyDataVo model) {
        showCustomEditDialog(model);
    }

    @Override
    public void onUpdateLocationClick(int pos, SurveyDataVo model) {
        if (ConnectivityChecking.checkConnection(EditLT.this)) {
            getPoleData(model.getFEEDER_ID(), String.valueOf(model.getPOLE_NO()), pos);
        } else {
            CommanUtilits.internetDialog(EditLT.this);
        }
    }

    private void getPoleData(String feederId, String poleNo, int pos) {
        if (!ConnectivityChecking.checkConnection(EditLT.this)) {
            Toast.makeText(getApplicationContext(),
                    "For accessing this feature, internet connection is required",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // Show progress dialog (if you already have a reusable one, use that)
        progressDialog = new ProgressDialog(EditLT.this);
        progressDialog.setMessage("Fetching pole data...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        JsonObject postParam = new JsonObject();
        postParam.addProperty("FEEDER_ID", feederId);
        postParam.addProperty("POLE_NO", poleNo);
        postParam.addProperty("LT_CKTCODE", CIRCUIT_CODE);

        retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        Call<JsonObject> responseVoCall = retrofitService.getPoleData(postParam);


        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    if (response.body() == null) {
                        Toast.makeText(EditLT.this, "Server busy, please try again", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        return;
                    }

                    JsonObject jsonObject = response.body();
                    String status = jsonObject.has("STATUS") ? jsonObject.get("STATUS").getAsString() : "FALSE";

                    if ("TRUE".equalsIgnoreCase(status)) {
                        JsonArray responseArray = jsonObject.getAsJsonArray("RESPONSE");

                        if (responseArray != null && responseArray.size() > 0) {
                            JsonObject poleObj = responseArray.get(0).getAsJsonObject();

                            String poleNoRes = (poleObj.has("POLE_NO") && !poleObj.get("POLE_NO").isJsonNull())
                                    ? poleObj.get("POLE_NO").getAsString() : "";

                            String fromPole = (poleObj.has("FROM_POLE") && !poleObj.get("FROM_POLE").isJsonNull())
                                    ? poleObj.get("FROM_POLE").getAsString() : "";

                            String lat = (poleObj.has("GLAT") && !poleObj.get("GLAT").isJsonNull())
                                    ? poleObj.get("GLAT").getAsString() : "";

                            String lon = (poleObj.has("GLONG") && !poleObj.get("GLONG").isJsonNull())
                                    ? poleObj.get("GLONG").getAsString() : "";

                            String feederIdRes = (poleObj.has("FEEDER_ID") && !poleObj.get("FEEDER_ID").isJsonNull())
                                    ? poleObj.get("FEEDER_ID").getAsString() : "";

                            String poleConfig = (poleObj.has("POLE_CONFIG") && !poleObj.get("POLE_CONFIG").isJsonNull())
                                    ? poleObj.get("POLE_CONFIG").getAsString() : "";

                            String poleType = (poleObj.has("POLE_TYPE") && !poleObj.get("POLE_TYPE").isJsonNull())
                                    ? poleObj.get("POLE_TYPE").getAsString() : "";

                            String id = (poleObj.has("ID") && !poleObj.get("ID").isJsonNull())
                                    ? poleObj.get("ID").getAsString() : "";

                            System.out.println("ID Check :::::::" + id);


                            Intent intent = new Intent(EditLT.this, EditLocationsMap.class);
                            intent.putExtra("FROM", "ASSETMAPPING_LT");
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

                        Toast.makeText(EditLT.this, errorMsg, Toast.LENGTH_SHORT).show();

                        if ("No Records".equalsIgnoreCase(errorMsg)) {
                            onBackPressed();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(EditLT.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                } finally {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditLT.this, "Network error, please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void showCustomEditDialog(SurveyDataVo model) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.dialog_edit_options_asset, null);
//        builder.setView(dialogView);
//
//        AlertDialog dialog = builder.create();
//
//
//        // Find views
//        LinearLayout optionAll = dialogView.findViewById(R.id.option_all);
//        LinearLayout optionPole = dialogView.findViewById(R.id.option_pole);
//        LinearLayout optionDTR = dialogView.findViewById(R.id.option_source);
//        LinearLayout optionSourcePole = dialogView.findViewById(R.id.option_source_pole);
//        LinearLayout option_boq = dialogView.findViewById(R.id.option_boq);
//        LinearLayout option_httvr = dialogView.findViewById(R.id.option_httvr);
//
//        optionPole.setOnClickListener(v -> {
//            editPoleAttributes(model);
//            dialog.dismiss();
//        });
//
//        optionSourcePole.setOnClickListener(v -> {
//            editSourcePole(model);
//            dialog.dismiss();
//        });
//
//
//        dialog.show();
//    }

    private void showCustomEditDialog(SurveyDataVo model) {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View dialogView = getLayoutInflater()
                .inflate(R.layout.dialog_edit_options_asset, null);

        bottomSheetDialog.setContentView(dialogView);

        // Find views
        LinearLayout optionAll = dialogView.findViewById(R.id.option_all);
        LinearLayout optionPole = dialogView.findViewById(R.id.option_pole);
        LinearLayout optionDTR = dialogView.findViewById(R.id.option_source);
        LinearLayout optionSourcePole = dialogView.findViewById(R.id.option_source_pole);
        LinearLayout option_boq = dialogView.findViewById(R.id.option_boq);
        LinearLayout option_httvr = dialogView.findViewById(R.id.option_httvr);

        bottomSheetDialog.show();

        optionPole.setOnClickListener(v -> {
            editPoleAttributes(model);
            bottomSheetDialog.dismiss();
        });

        optionSourcePole.setOnClickListener(v -> {
            editSourcePole(model);
            bottomSheetDialog.dismiss();
        });

        // Add remaining options if needed
    }
    private void editSourcePole(SurveyDataVo model) {
        Intent intent = new Intent(this, EditPoleAttributesLtAsset.class);
        intent.putExtra("FEEDER_ID", model.getFEEDER_ID());
        intent.putExtra("POLE_NO", model.getPOLE_NO());
        intent.putExtra("DATA_MODEL", model);
        intent.putExtra("FROM", "SOURCE");
        intent.putExtra("CIRCUIT_CODE", CIRCUIT_CODE);
        startActivity(intent);


    }

    private void editPoleAttributes(SurveyDataVo model) {
        Intent intent = new Intent(this, EditPoleAttributesLtAsset.class);
        intent.putExtra("FEEDER_ID", model.getFEEDER_ID());
        intent.putExtra("POLE_NO", model.getPOLE_NO());
        intent.putExtra("DATA_MODEL", model);
        intent.putExtra("FROM", "POLE");
        intent.putExtra("CIRCUIT_CODE", CIRCUIT_CODE);
        startActivity(intent);

    }
}
