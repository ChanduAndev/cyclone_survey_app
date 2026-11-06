package com.apepdcl.gis_survey.RDSS.edit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.PMI.models.FeedersData;
import com.apepdcl.gis_survey.RDSS.AssetsHomeRdssUgCable;
import com.apepdcl.gis_survey.RDSS.RetrofitClientInstanceRDSS;
import com.apepdcl.gis_survey.RDSS.RetrofitServiceRDSS;
import com.apepdcl.gis_survey.RDSS.UGPostingModel;
import com.apepdcl.gis_survey.estimates.sou.EditLocationsMap;
import com.apepdcl.gis_survey.module_11KV.edit_screens.EditPoleAttributesAsset;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditOptionsRdss extends AppCompatActivity implements EditSurveyAdapterRdss.OnItemClickListener {
    private RecyclerView edit_rv;
    private RelativeLayout back_layout, close_layout;
    private ProgressDialog progressDialog;
    EditSurveyAdapterRdss adapter;
    ArrayList<UGPostingModel> list = new ArrayList<>();
    private ArrayList<UGPostingModel> getList = new ArrayList<>();
    ;
    private RetrofitServiceRDSS retrofitServiceRDSS;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_screen_rdss);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        progressDialog = CommanUtilits.progressDialog(this);
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
        //list = new ArrayList<>();
        list = (ArrayList<UGPostingModel>) getIntent().getSerializableExtra("UG_MODEL");
        setRecyclerView();
    }

    private void setRecyclerView() {
        edit_rv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        edit_rv.setLayoutManager(layoutManager);
        edit_rv.setFocusable(false);
        adapter = new EditSurveyAdapterRdss(list, this, this);
        edit_rv.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position, UGPostingModel model) {
        showCustomEditDialog(model);
    }

    @Override
    public void onUpdateLocationClick(int pos, UGPostingModel model) {
        if (ConnectivityChecking.checkConnection(EditOptionsRdss.this)) {
            get33KVPolesData(model, "Source");
        } else {
            CommanUtilits.internetDialog(EditOptionsRdss.this);
        }
    }

    private void get33KVPolesData(UGPostingModel model, String type) {
        progressDialog.show();
        JsonObject object = new JsonObject();
        object.addProperty("LOCATION_ID", model.getLOCATION_ID());
        retrofitServiceRDSS = RetrofitClientInstanceRDSS.getRetrofitInstance().create(RetrofitServiceRDSS.class);
        Call<JsonObject> responseVoCall = retrofitServiceRDSS.getTrenchSurvey(object);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    Log.v("FEEDER==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {
                            progressDialog.dismiss();
                            JsonArray array33 = surveyedResponseVo.get("DATA").getAsJsonArray();

                            for (int i = 0; i < array33.size(); i++) {


                                UGPostingModel model = new UGPostingModel();
                                model.setLOCATION_ID(array33.get(i).getAsJsonObject().get("LOCATION_ID").getAsString());
                                model.setLOCATION_NO(array33.get(i).getAsJsonObject().get("LOCATION_NO").getAsString()); // Update index as per the actual table schema
                                model.setSOURCE_LOCATION(array33.get(i).getAsJsonObject().get("SOURCE_LOCATION").getAsString());        // Update index as per the actual table schema
                                model.setSS_NAME(array33.get(i).getAsJsonObject().get("SS_NAME").getAsString());        // Update index as per the actual table schema
                                model.setSS_ID(array33.get(i).getAsJsonObject().get("SS_ID").getAsString());        // Update index as per the actual table schema
                                model.setSS_TYPE(array33.get(i).getAsJsonObject().get("SS_TYPE").getAsString());        // Update index as per the actual table schema
                                model.setCREATED_BY(array33.get(i).getAsJsonObject().get("CREATED_BY").getAsString());        // Update index as per the actual table schema
                                model.setWIDTH(array33.get(i).getAsJsonObject().get("WIDTH").getAsString());        // Update index as per the actual table schema
                                model.setDEPTH(array33.get(i).getAsJsonObject().get("DEPTH").getAsString());
                                JsonObject jsonObject = array33.get(i).getAsJsonObject();
                                if (jsonObject.has("UTILITY") && !jsonObject.get("UTILITY").isJsonNull()) {
                                    model.setUTILITY(jsonObject.get("UTILITY").getAsString());
                                } else {
                                    model.setUTILITY(""); // or null, depending on your model
                                }
                                // Update index as per the actual table schema
                                model.setCOUNT_CABLES(array33.get(i).getAsJsonObject().get("COUNT_CABLES").getAsString());        // Update index as per the actual table schema
                                model.setCABLE_33KV(array33.get(i).getAsJsonObject().get("CABLE_33KV").getAsString());        // Update index as per the actual table schema
                                model.setCABLE_11KV(array33.get(i).getAsJsonObject().get("CABLE_11KV").getAsString());        // Update index as per the actual table schema
                                model.setCABLE_LT(array33.get(i).getAsJsonObject().get("CABLE_LT").getAsString());        // Update index as per the actual table schema
                                model.setLAT(array33.get(i).getAsJsonObject().get("LAT").getAsString());        // Update index as per the actual table schema
                                model.setLON(array33.get(i).getAsJsonObject().get("LON").getAsString());
                                model.setID(array33.get(i).getAsJsonObject().get("ID").getAsString());


                                if (jsonObject.has("LOCATION_TYPE") && !jsonObject.get("LOCATION_TYPE").isJsonNull()) {
                                    model.setLOCATION_TYPE(jsonObject.get("LOCATION_TYPE").getAsString());
                                } else {
                                    model.setLOCATION_TYPE("");
                                }

                                if (jsonObject.has("LOCATION_REMARKS") && !jsonObject.get("LOCATION_REMARKS").isJsonNull()) {
                                    model.setLOCATION_REMARKS(jsonObject.get("LOCATION_REMARKS").getAsString());
                                } else {
                                    model.setLOCATION_REMARKS("");
                                }

                                if (jsonObject.has("RMU_TYPE") && !jsonObject.get("RMU_TYPE").isJsonNull()) {
                                    model.setRMU_TYPE(jsonObject.get("RMU_TYPE").getAsString());
                                } else {
                                    model.setRMU_TYPE("");
                                }

                                if (jsonObject.has("DTR_CAPACITY") && !jsonObject.get("DTR_CAPACITY").isJsonNull()) {
                                    model.setDTR_CAPACITY(jsonObject.get("DTR_CAPACITY").getAsString());
                                } else {
                                    model.setDTR_CAPACITY("");
                                }
                                if (jsonObject.has("HT_SCNO") && !jsonObject.get("HT_SCNO").isJsonNull()) {
                                    model.setHT_SCNO(jsonObject.get("HT_SCNO").getAsString());
                                } else {
                                    model.setHT_SCNO("");
                                }

                                if (jsonObject.has("UTILITY_TYPE") && !jsonObject.get("UTILITY_TYPE").isJsonNull()) {
                                    model.setUTILITY_TYPE(jsonObject.get("UTILITY_TYPE").getAsString());
                                } else {
                                    model.setUTILITY_TYPE("");
                                }

                                if (jsonObject.has("PROTECTION_PIPE") && !jsonObject.get("PROTECTION_PIPE").isJsonNull()) {
                                    model.setPROTECTION_PIPE(jsonObject.get("PROTECTION_PIPE").getAsString());
                                } else {
                                    model.setPROTECTION_PIPE("");
                                }

                                if (jsonObject.has("CABLE_SIZE") && !jsonObject.get("CABLE_SIZE").isJsonNull()) {
                                    model.setCABLE_SIZE(jsonObject.get("CABLE_SIZE").getAsString());
                                } else {
                                    model.setCABLE_SIZE("");
                                }

                                if (jsonObject.has("FEEDERS") && jsonObject.get("FEEDERS").isJsonArray()) {
                                    JsonArray feedersArray = jsonObject.getAsJsonArray("FEEDERS");

                                    ArrayList<FeedersData> feedersData = new ArrayList<>();
                                    for (int j = 0; j < feedersArray.size(); j++) {
                                        JsonObject feederObj = feedersArray.get(j).getAsJsonObject();

                                        FeedersData feeder = new FeedersData();
                                        feeder.setVoltage_level(feederObj.has("VOLTAGE_LEVEL") && !feederObj.get("VOLTAGE_LEVEL").isJsonNull() ? feederObj.get("VOLTAGE_LEVEL").getAsString() : "");
                                        feeder.setFeeder_id(feederObj.has("FEEDER_ID") && !feederObj.get("FEEDER_ID").isJsonNull() ? feederObj.get("FEEDER_ID").getAsString() : "");
                                        feeder.setFeeder_name(feederObj.has("FEEDER_NAME") && !feederObj.get("FEEDER_NAME").isJsonNull() ? feederObj.get("FEEDER_NAME").getAsString() : "");
                                        feeder.setSS_NAME(feederObj.has("SS_NAME") && !feederObj.get("SS_NAME").isJsonNull() ? feederObj.get("SS_NAME").getAsString() : "");
                                        feeder.setSS_ID(feederObj.has("SS_ID") && !feederObj.get("SS_ID").isJsonNull() ? feederObj.get("SS_ID").getAsString() : "");
                                        feeder.setFDR_INDEX(feederObj.has("FDR_INDEX") && !feederObj.get("FDR_INDEX").isJsonNull() ? feederObj.get("FDR_INDEX").getAsInt() : 0);
                                        feeder.setDTR_LOCATION(feederObj.has("DTR_LOCATION") && !feederObj.get("DTR_LOCATION").isJsonNull() ? feederObj.get("DTR_LOCATION").getAsString() : "");
                                        feeder.setDTR_CODE(feederObj.has("DTR_CODE") && !feederObj.get("DTR_CODE").isJsonNull() ? feederObj.get("DTR_CODE").getAsString() : "");
                                        feeder.setDTR_PHASE(feederObj.has("DTR_PHASE") && !feederObj.get("DTR_PHASE").isJsonNull() ? feederObj.get("DTR_PHASE").getAsString() : "");
                                        feeder.setDTR_CAPACITY(feederObj.has("DTR_CAPACITY") && !feederObj.get("DTR_CAPACITY").isJsonNull() ? feederObj.get("DTR_CAPACITY").getAsString() : "");
                                        feeder.setREMARKS(feederObj.has("REMARKS") && !feederObj.get("REMARKS").isJsonNull() ? feederObj.get("REMARKS").getAsString() : "");
                                        feeder.setCABLE_SIZE(feederObj.has("CABLE_SIZE") && !feederObj.get("CABLE_SIZE").isJsonNull() ? feederObj.get("CABLE_SIZE").getAsString() : "");
                                        System.out.println(
                                                "PARSE_LT -> CODE="
                                                        + feeder.getDTR_CODE()
                                                        + ", LOCATION="
                                                        + feeder.getDTR_LOCATION()
                                                        + ", CAPACITY="
                                                        + feeder.getDTR_CAPACITY()
                                                        + ", PHASE="
                                                        + feeder.getDTR_PHASE()
                                        );

                                        Log.d("JSON_KEYS", feederObj.toString());

                                        Log.d("JSON_CHECK",
                                                "has DTR_CODE = " + feederObj.has("DTR_CODE") +
                                                        ", value = " + feederObj.get("DTR_CODE"));

                                        Log.d("JSON_CHECK",
                                                "has DTR_LOCATION = " + feederObj.has("DTR_LOCATION") +
                                                        ", value = " + feederObj.get("DTR_LOCATION"));

                                        feedersData.add(feeder);
                                    }

                                    // ✅ Set feeders list to model
                                    model.setFeedersData(feedersData);

                                    //getList.add(model);

                                } else {
                                    // No feeders
                                    model.setFeedersData(new ArrayList<>());
                                }
                                getList.add(model);
                                if (type.equalsIgnoreCase("Source")) {
                                    Intent intent = new Intent(EditOptionsRdss.this, EditLocationsMap.class);
                                    intent.putExtra("UG_MODEL", getList);
                                    intent.putExtra("FROM", "RDSS");
                                    intent.putExtra("ID", jsonObject.get("ID").getAsString());
                                    startActivity(intent);
                                } else if (type.equalsIgnoreCase("SOURCE_POLE")) {
                                    Intent intent = new Intent(EditOptionsRdss.this, EditPoleAttributesRdss.class);
                                    intent.putExtra("DATA_MODEL", model);
                                    intent.putExtra("FROM", "SOURCE_POLE");
                                    intent.putExtra("ID", model.getID());
                                    intent.putExtra("LOCATION_ID", model.getLOCATION_ID());

                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(EditOptionsRdss.this, EditPoleAttributesRdss.class);
                                    intent.putExtra("DATA_MODEL", model);
                                    intent.putExtra("FROM", "POLE_RDSS");
                                    intent.putExtra("ID", model.getID());
                                    intent.putExtra("LOCATION_ID", model.getLOCATION_ID());
                                    intent.putExtra("VOLTAGE_LEVEL", AssetsHomeRdssUgCable.SELECTED_VOLTAGE);
                                    startActivity(intent);
                                }


                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        progressDialog.dismiss();

                    }


                } else {
                    progressDialog.dismiss();

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                progressDialog.dismiss();
                CommanUtilits.netWorkError(EditOptionsRdss.this);
            }
        });

    }


    private void showCustomEditDialog(UGPostingModel model) {
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

    private void editSourcePole(UGPostingModel model) {
        get33KVPolesData(model, "SOURCE_POLE");
//        Intent intent = new Intent(this, EditPoleAttributesRdss.class);
//        //intent.putExtra("FEEDER_ID", model.getFeeder_id());
//        //intent.putExtra("POLE_NO", model.getPole_no());
//        intent.putExtra("DATA_MODEL", model);
//        intent.putExtra("FROM", "SOURCE");
//        startActivity(intent);

    }

    private void editPoleAttributes(UGPostingModel model) {
        get33KVPolesData(model, "POLE");


//        Intent intent = new Intent(this, EditPoleAttributesRdss.class);
//        intent.putExtra("DATA_MODEL", model);
//        intent.putExtra("FROM", "POLE_RDSS");
//        intent.putExtra("ID", model.getID());
//        intent.putExtra("LOCATION_ID", model.getLOCATION_ID());
//        startActivity(intent);

    }
}


