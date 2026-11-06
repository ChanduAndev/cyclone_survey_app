package com.apepdcl.gis_survey.estimates.sou;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.RDSS.RdssHome;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.estimates.RetrofitClientInstanceEstimates;
import com.apepdcl.gis_survey.estimates.RetrofitServiceEstimates;
import com.apepdcl.gis_survey.estimates.new_module.EstimatesMap;
import com.apepdcl.gis_survey.estimates.new_module.NewModel;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditBOQDetailsActivity extends AppCompatActivity {
    private RecyclerView material_rv_33, material_rv_33_pole, material_rv_33_AB,material_rv_33_dtr_meter;
    ArrayList<MaterialModel> materialList33, materialList33AB;
    ArrayList<MaterialModelPole> materialList33_pole;
    ArrayList<MaterialModel> tempList, tempAB;
    ArrayList<MaterialModelPole> tempListPole;
    ArrayList<DtrMeterModel> tempDTRMeter;
    ArrayList<DtrMeterModel> dtrMeterModel;
    private LinearLayout loader_layout, sap_33_material_layout, sap_33_material_layout_pole, sap_33_material_layout_AB,sap_33_material_layout_dtr_meter;

    boolean IS_EDIT;
    NewModel DATA_MODEL;

    String EST_NO, GIS_ID, BTN_VALUE, wo_no, EST_NATURE = "", poleType33 = "", PURPOSE = "", REG_COUNT = "", from = "", btnClick = "",
            TYPE = "", SELECTED_SOURCE_POLE_LOCATION = "", MAX_LENGTH = "", user_id;

    private RetrofitServiceEstimates retrofitService;
    private TextView btn_update;
    private SharedPreferences preferences;

    private RelativeLayout back_layout, close_layout;

    private TextView tv_materials, tv_error_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_boqdetails);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        preferences = PreferenceManager.getDefaultSharedPreferences(EditBOQDetailsActivity.this);
        materialList33 = new ArrayList<>();
        materialList33_pole = new ArrayList<>();
        materialList33AB = new ArrayList<>();
        tempList = new ArrayList<>();
        tempAB = new ArrayList<>();
        tempListPole = new ArrayList<>();
        tempDTRMeter = new ArrayList<>();
        dtrMeterModel = new ArrayList<>();
        IS_EDIT = getIntent().getBooleanExtra("IS_EDIT", false);
        if (IS_EDIT) {
            DATA_MODEL = (NewModel) getIntent().getSerializableExtra("DATA_MODEL");
        }

        EST_NO = getIntent().getStringExtra("EST_NO");
        GIS_ID = getIntent().getStringExtra("GIS_ID");
        EST_NATURE = getIntent().getStringExtra("EST_NATURE");
        PURPOSE = getIntent().getStringExtra("PURPOSE");
        wo_no = getIntent().getStringExtra("WORK_ORDER");
        REG_COUNT = getIntent().getStringExtra("REG_COUNT");
        BTN_VALUE = getIntent().getStringExtra("BTN_VALUE");
        from = getIntent().getStringExtra("FROM");
        btnClick = getIntent().getStringExtra("BTN_VALUE");
        TYPE = getIntent().getStringExtra("TYPE");
        MAX_LENGTH = getIntent().getStringExtra("MAX_LENGTH");
        // databaseEstimates = new ArcGISDatabaseEstimates(this);

        user_id = preferences.getString(MobileConstants.USERNAME, "");

        setViewCasting();
        close_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

        if (DATA_MODEL.getREF_ID() != null && !DATA_MODEL.getREF_ID().isEmpty()) {
            updateDTRDetails();

        } else {
            tv_error_msg.setVisibility(View.VISIBLE);
            sap_33_material_layout.setVisibility(View.GONE);
            sap_33_material_layout_pole.setVisibility(View.GONE);

        }


        btn_update.setOnClickListener(v -> {
            ArrayList<MaterialPostingModel> changedList = new ArrayList<>();

            // ================= NORMAL MATERIALS =================
            for (int i = 0; i < materialList33.size(); i++) {
                MaterialModel current = materialList33.get(i);  // edited via adapter
                MaterialModel original = tempList.get(i);       // snapshot

                int oldQty = 0;
                int newQty = 0;
                try {
                    oldQty = Integer.parseInt(original.getQTY());
                    newQty = Integer.parseInt(current.getQTY());
                } catch (NumberFormatException e) {
                    continue;
                }

                if (oldQty != newQty) {
                    MaterialPostingModel postingModel = new MaterialPostingModel();
                    postingModel.setAPP_VERSION(MobileConstants.APP_VERSION);
                    postingModel.setAPP_VERSION2(getResources().getString(R.string.displayVersion));
                    postingModel.setCOUNT(String.valueOf(newQty));
                    postingModel.setDATA_ID(current.getDATA_ID());
                    postingModel.setESTIMATE_NO(DATA_MODEL.getEST_NO());
                    postingModel.setGIS_ID(DATA_MODEL.getGIS_ID());
                    postingModel.setLOCATION_ID(DATA_MODEL.getLOCATION_ID());
                    postingModel.setLOCATION_NO(DATA_MODEL.getPOLE_NO());
                    postingModel.setMATERIAL_DESC(current.getMATERIAL_DESC());
                    postingModel.setSAP_CODE(current.getMATERIAL_CODE());
                    postingModel.setTYPE("TRANSMITTED");
                    postingModel.setVOLTAGE_LEVEL(BTN_VALUE);
                    postingModel.setSURVEY_CODE(current.getSURVEY_CODE());

                    if (oldQty == 0 && newQty > 0) {
                        postingModel.setMOD_TYPE("NEW");
                    } else {
                        postingModel.setMOD_TYPE("EDIT");
                        postingModel.setREF_ID(current.getId());
                        postingModel.setUSER_ID(user_id);
                    }

                    changedList.add(postingModel);
                }
            }

            // ================= POLE MATERIALS =================
            for (int i = 0; i < materialList33_pole.size(); i++) {
                MaterialModelPole current = materialList33_pole.get(i);  // edited via adapter
                MaterialModelPole original = tempListPole.get(i);       // snapshot

                int oldQty = 0;
                int newQty = 0;
                try {
                    oldQty = Integer.parseInt(original.getQTY());
                    newQty = Integer.parseInt(current.getQTY());
                } catch (NumberFormatException e) {
                    continue;
                }

                if (oldQty != newQty) {
                    MaterialPostingModel postingModel = new MaterialPostingModel();
                    postingModel.setAPP_VERSION(MobileConstants.APP_VERSION);
                    postingModel.setAPP_VERSION2(getResources().getString(R.string.displayVersion));
                    postingModel.setCOUNT(String.valueOf(newQty));
                    postingModel.setDATA_ID(current.getDATA_ID());
                    postingModel.setESTIMATE_NO(DATA_MODEL.getEST_NO());
                    postingModel.setGIS_ID(DATA_MODEL.getGIS_ID());
                    postingModel.setLOCATION_ID(DATA_MODEL.getLOCATION_ID());
                    postingModel.setLOCATION_NO(DATA_MODEL.getPOLE_NO());
                    postingModel.setMATERIAL_DESC(current.getMATERIAL_DESC());
                    postingModel.setSAP_CODE(current.getMATERIAL_CODE());
                    postingModel.setTYPE("TRANSMITTED");
                    postingModel.setVOLTAGE_LEVEL(BTN_VALUE);
                    postingModel.setSURVEY_CODE(current.getSURVEY_CODE());

                    if (oldQty == 0 && newQty > 0) {
                        postingModel.setMOD_TYPE("NEW");
                    } else {
                        postingModel.setMOD_TYPE("EDIT");
                        postingModel.setREF_ID(current.getId());
                        postingModel.setUSER_ID(user_id);
                    }

                    changedList.add(postingModel);
                }
            }

            for (int i = 0; i < materialList33AB.size(); i++) {
                MaterialModel current = materialList33AB.get(i);  // edited via adapter
                MaterialModel original = tempAB.get(i);       // snapshot

                int oldQty = 0;
                int newQty = 0;
                try {
                    oldQty = Integer.parseInt(original.getQTY());
                    newQty = Integer.parseInt(current.getQTY());
                } catch (NumberFormatException e) {
                    continue;
                }

                if (oldQty != newQty) {
                    MaterialPostingModel postingModel = new MaterialPostingModel();
                    postingModel.setAPP_VERSION(MobileConstants.APP_VERSION);
                    postingModel.setAPP_VERSION2(getResources().getString(R.string.displayVersion));
                    postingModel.setCOUNT(String.valueOf(newQty));
                    postingModel.setDATA_ID(current.getDATA_ID());
                    postingModel.setESTIMATE_NO(DATA_MODEL.getEST_NO());
                    postingModel.setGIS_ID(DATA_MODEL.getGIS_ID());
                    postingModel.setLOCATION_ID(DATA_MODEL.getLOCATION_ID());
                    postingModel.setLOCATION_NO(DATA_MODEL.getPOLE_NO());
                    postingModel.setMATERIAL_DESC(current.getMATERIAL_DESC());
                    postingModel.setSAP_CODE(current.getMATERIAL_CODE());
                    postingModel.setTYPE("TRANSMITTED");
                    postingModel.setVOLTAGE_LEVEL(BTN_VALUE);
                    postingModel.setSURVEY_CODE(current.getSURVEY_CODE());

                    if (oldQty == 0 && newQty > 0) {
                        postingModel.setMOD_TYPE("NEW");
                    } else {
                        postingModel.setMOD_TYPE("EDIT");
                        postingModel.setREF_ID(current.getId());
                        postingModel.setUSER_ID(user_id);
                    }

                    changedList.add(postingModel);
                }
            }

            for (int i = 0; i < dtrMeterModel.size(); i++) {
                DtrMeterModel current = dtrMeterModel.get(i);  // edited via adapter
                DtrMeterModel original = tempDTRMeter.get(i);       // snapshot

                int oldQty = 0;
                int newQty = 0;
                try {
                    oldQty = Integer.parseInt(original.getQTY());
                    newQty = Integer.parseInt(current.getQTY());
                } catch (NumberFormatException e) {
                    continue;
                }

                if (oldQty != newQty) {
                    MaterialPostingModel postingModel = new MaterialPostingModel();
                    postingModel.setAPP_VERSION(MobileConstants.APP_VERSION);
                    postingModel.setAPP_VERSION2(getResources().getString(R.string.displayVersion));
                    postingModel.setCOUNT(String.valueOf(newQty));
                    postingModel.setDATA_ID(current.getDATA_ID());
                    postingModel.setESTIMATE_NO(DATA_MODEL.getEST_NO());
                    postingModel.setGIS_ID(DATA_MODEL.getGIS_ID());
                    postingModel.setLOCATION_ID(DATA_MODEL.getLOCATION_ID());
                    postingModel.setLOCATION_NO(DATA_MODEL.getPOLE_NO());
                    postingModel.setMATERIAL_DESC(current.getMATERIAL_DESC());
                    postingModel.setSAP_CODE(current.getMATERIAL_CODE());
                    postingModel.setTYPE("TRANSMITTED");
                    postingModel.setVOLTAGE_LEVEL(BTN_VALUE);
                    postingModel.setSURVEY_CODE(current.getSURVEY_CODE());

                    if (oldQty == 0 && newQty > 0) {
                        postingModel.setMOD_TYPE("NEW");
                    } else {
                        postingModel.setMOD_TYPE("EDIT");
                        postingModel.setREF_ID(current.getId());
                        postingModel.setUSER_ID(user_id);
                    }

                    changedList.add(postingModel);
                }
            }

            // ================= API CALL =================
            if (!changedList.isEmpty()) {
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(changedList);
                JsonArray jsonArray = jsonElement.getAsJsonArray();
                loader_layout.setVisibility(View.VISIBLE);
                List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
                retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
                Call<JsonObject> responseVoCall = retrofitService.postingMaterialTransmitted(jsonArray);
                responseVoCall.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        loader_layout.setVisibility(View.GONE);
                        if (response.body() != null) {
                            JsonObject surveyedResponseVo = response.body();
                            if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                                openDialogForCompleteSuccess(surveyedResponseVo.get("ERR_MSG").getAsString());
                            } else {
                                openDialogForCompleteSuccess(surveyedResponseVo.get("ERR_MSG").getAsString());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        loader_layout.setVisibility(View.GONE);
                        Toast.makeText(EditBOQDetailsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(EditBOQDetailsActivity.this, "You are not selected anything to update", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void openDialogForCompleteSuccess(String msg) {
        new AlertDialog.Builder(EditBOQDetailsActivity.this)
                .setTitle("Pay Attention!!")
                .setIcon(R.drawable.app_logo)
                .setMessage(msg)
                .setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getOnBackPressedDispatcher().onBackPressed();
                        dialog.dismiss();

                    }
                })
                .show();
    }

    private MaterialPostingModel buildPostingModel(MaterialModel current, int newQty) {
        MaterialPostingModel postingModel = new MaterialPostingModel();
        postingModel.setAPP_VERSION(MobileConstants.APP_VERSION);
        postingModel.setAPP_VERSION2(getResources().getString(R.string.displayVersion));
        postingModel.setCOUNT(String.valueOf(newQty));
        postingModel.setDATA_ID(current.getDATA_ID());
        postingModel.setESTIMATE_NO(DATA_MODEL.getEST_NO());
        postingModel.setGIS_ID(DATA_MODEL.getGIS_ID());
        postingModel.setLOCATION_ID(DATA_MODEL.getLOCATION_ID());
        postingModel.setLOCATION_NO(DATA_MODEL.getPOLE_NO());
        postingModel.setMATERIAL_DESC(current.getMATERIAL_DESC());
        postingModel.setSAP_CODE(current.getMATERIAL_CODE());
        postingModel.setTYPE("TRANSMITTED");
        postingModel.setVOLTAGE_LEVEL(BTN_VALUE);
        return postingModel;
    }

    @SuppressLint("SetTextI18n")
    private void setBoQAdapter(ArrayList<MaterialModel> model) {
        System.out.println("POLE_CONFIG_ADAPTER---->" + DATA_MODEL.getPOLE_CONFIG());
        //System.out.println("COUNT_OF_MODEL_DTR" + model.size());
        Set<String> uniquePoleConfigs = new LinkedHashSet<>();

        for (int i = 0; i < model.size(); i++) {
            String poleConfig = model.get(i).getPOLE_CONFIG();
            if (poleConfig != null && !poleConfig.isEmpty()) {
                uniquePoleConfigs.add(poleConfig); // automatically skips duplicates
            }
        }

        String selectType = DATA_MODEL.getPOLE_CONFIG();
        String poleConfig = "";
        if (selectType.equalsIgnoreCase("DTR Pole")) {
            poleConfig = "DTR";
        } else if (selectType.equalsIgnoreCase("AB-POLE")) {
            poleConfig = "AB Switch";
        } else if (selectType.equalsIgnoreCase("HT SERVICE-POLE")) {
            poleConfig = "HTTVR";
        } else if (selectType.equalsIgnoreCase("33/11KV SS-BAY")) {
            poleConfig = "33/11KV SS- 33VK BAY";
        } else if ("33/11KV SS".equalsIgnoreCase(selectType)) {
            poleConfig = "33/11KV SS";
        } else if ("RMU".equalsIgnoreCase(selectType)) {
            poleConfig = "RMU";
        } else if ("EHT SS-BAY".equalsIgnoreCase(selectType)) {
            poleConfig = "EHT SS-33KV BAY";
        } else if ("SS BAY".equalsIgnoreCase(selectType)) {
            poleConfig = "33/11KV SS-11KV BAY";
        }


        tv_materials.setText(poleConfig + " Material");

        // material_rv_33.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(EditBOQDetailsActivity.this);
        material_rv_33.setLayoutManager(layoutManager);
        material_rv_33.setFocusable(false);
        MaterialAdapterForUpdate adapter = new MaterialAdapterForUpdate(model, EditBOQDetailsActivity.this);
        material_rv_33.setAdapter(adapter);
        material_rv_33.setNestedScrollingEnabled(false);
    }


    private void setBoQAdapterPole(ArrayList<MaterialModelPole> model) {
        System.out.println("COUNT_OF_MODEL" + model.size());
        // material_rv_33.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(EditBOQDetailsActivity.this);
        material_rv_33_pole.setLayoutManager(layoutManager);
        material_rv_33_pole.setFocusable(false);
        MaterialAdapterForUpdatePole adapter = new MaterialAdapterForUpdatePole(model, EditBOQDetailsActivity.this);
        material_rv_33_pole.setAdapter(adapter);
        material_rv_33_pole.setNestedScrollingEnabled(false);
    }


    private void setDTRMeterBoQAdapter(ArrayList<DtrMeterModel> model) {
        System.out.println("COUNT_OF_MODEL" + model.size());
        // material_rv_33.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(EditBOQDetailsActivity.this);
        material_rv_33_dtr_meter.setLayoutManager(layoutManager);
        material_rv_33_dtr_meter.setFocusable(false);
        DTRMeterMaterialAdapter adapter = new DTRMeterMaterialAdapter(model, EditBOQDetailsActivity.this,true);
        material_rv_33_dtr_meter.setAdapter(adapter);
        material_rv_33_dtr_meter.setNestedScrollingEnabled(false);
    }

    private void setBoQABAdapterPole(ArrayList<MaterialModel> model) {
        System.out.println("COUNT_OF_MODEL" + model.size());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(EditBOQDetailsActivity.this);
        material_rv_33_AB.setLayoutManager(layoutManager);
        material_rv_33_AB.setFocusable(false);
        MaterialAdapterABSwitch adapter = new MaterialAdapterABSwitch(model, EditBOQDetailsActivity.this, IS_EDIT);
        material_rv_33_AB.setAdapter(adapter);
        material_rv_33_AB.setNestedScrollingEnabled(false);
    }

    private void setViewCasting() {
        material_rv_33 = findViewById(R.id.material_rv_33);
        loader_layout = findViewById(R.id.loader_layout);
        sap_33_material_layout = findViewById(R.id.sap_33_material_layout);
        sap_33_material_layout_pole = findViewById(R.id.sap_33_material_layout_pole);
        sap_33_material_layout_AB = findViewById(R.id.sap_33_material_layout_AB);
        btn_update = findViewById(R.id.btn_update);
        material_rv_33_pole = findViewById(R.id.material_rv_33_pole);
        material_rv_33_dtr_meter = findViewById(R.id.material_rv_33_dtr_meter);
        material_rv_33_AB = findViewById(R.id.material_rv_33_AB);
        sap_33_material_layout_dtr_meter = findViewById(R.id.sap_33_material_layout_dtr_meter);
        back_layout = findViewById(R.id.back_layout);
        close_layout = findViewById(R.id.close_layout);
        tv_materials = findViewById(R.id.tv_materials);
        tv_error_msg = findViewById(R.id.tv_error_msg);
    }

private boolean isDTRMeterAvailble=false;
    //NEW CHANGES FOR VERSION 4.3
    private void updateDTRDetails() {
        loader_layout.setVisibility(View.VISIBLE);
        Map<String, String> data = new HashMap<>();
        data.put("LOCATION_ID", DATA_MODEL.getLOCATION_ID());
        String poleConfig = DATA_MODEL.getPOLE_CONFIG();
        System.out.println("COUNT_OF_MODEL_DTR" + BTN_VALUE);
        System.out.println("POLE_CONFIG---->" + DATA_MODEL.getPOLE_CONFIG());

        if (BTN_VALUE.equalsIgnoreCase("33KV")) {
            data.put("SURVEY_TYPE", "33");


        } else if (BTN_VALUE.equalsIgnoreCase("11KV")) {
            data.put("SURVEY_TYPE", "11");


        } else if (BTN_VALUE.equalsIgnoreCase("LT")) {
            data.put("SURVEY_TYPE", "LT");
            //data.put("POLE_CONFIG", "LT LINE");
        }

        System.out.println("POLE_CONFIG-->" + DATA_MODEL.getPOLE_CONFIG());
        System.out.println("DTR_SURVEY_TYPE-->" + DATA_MODEL.getDTR_SURVEY_TYPE());

        JsonObject jsonObject = new JsonObject();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            jsonObject.addProperty(entry.getKey(), entry.getValue());
        }
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();

        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.getExistingMaterials(jsonObject);

        responseVoCall.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loader_layout.setVisibility(View.GONE);
                if (response.body() != null) {
                    JsonObject surveyedResponseVo = response.body();
                    if (surveyedResponseVo.has("STATUS") && !surveyedResponseVo.get("STATUS").isJsonNull()) {
                        String status = surveyedResponseVo.get("STATUS").getAsString();
                        if (MobileConstants.TRUE.equalsIgnoreCase(status)) {
                            JsonArray array = surveyedResponseVo.getAsJsonArray("DATA");

                            materialList33.clear();
                            materialList33_pole.clear();
                            materialList33AB.clear();
                            dtrMeterModel.clear();

                            for (int i = 0; i < array.size(); i++) {
                                JsonObject object = array.get(i).getAsJsonObject();
                                String surveyCode = object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                        ? object.get("SURVEY_CODE").getAsString()
                                        : "";

                                boolean hasId = object.has("id") && !object.get("id").isJsonNull()
                                        && !object.get("id").getAsString().trim().isEmpty();

                                // Skip if SURVEY_CODE is null or empty
                                if (surveyCode.isEmpty()) {
                                    continue;
                                }

                                if (BTN_VALUE.equalsIgnoreCase("11KV")) {
                                    if ("202".equalsIgnoreCase(surveyCode)) {
                                        // ==== MATERIALMODELPOLE ====
                                        MaterialModelPole modelPole = new MaterialModelPole();

                                        if (hasId) {
                                            String desc = object.has("MATERIAL_DESC") && !object.get("MATERIAL_DESC").isJsonNull()
                                                    ? object.get("MATERIAL_DESC").getAsString() : "";
                                            modelPole.setMATERIAL_DESC(desc);
                                            modelPole.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                    ? object.get("MATERIALCODE").getAsString() : "");
                                            modelPole.setQTY(object.has("COUNT") && !object.get("COUNT").isJsonNull()
                                                    ? object.get("COUNT").getAsString() : "0");
                                            modelPole.setId(object.has("id") && !object.get("id").isJsonNull()
                                                    ? object.get("id").getAsString() : "");
                                            modelPole.setSURVEY_CODE(surveyCode);
                                            modelPole.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                    ? object.get("UOM").getAsString() : "");
                                            modelPole.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                    ? object.get("SAP_CODE").getAsString() : "");

                                            modelPole.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                    ? object.get("SURVEY_CODE").getAsString() : "");
                                            modelPole.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull()
                                                    ? object.get("POLE_CONFIG").getAsString() : "");

                                        } else {
                                            String desc = object.has("MATERAIL_DESC") && !object.get("MATERAIL_DESC").isJsonNull()
                                                    ? object.get("MATERAIL_DESC").getAsString() : "";
                                            modelPole.setMATERIAL_DESC(desc);
                                            modelPole.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                    ? object.get("SAP_CODE").getAsString() : "");
                                            modelPole.setSURVEY_CODE(surveyCode);
                                            modelPole.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                    ? object.get("UOM").getAsString() : "");
                                            modelPole.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                    ? object.get("MATERIALCODE").getAsString() : "");
                                            modelPole.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                    ? object.get("SURVEY_CODE").getAsString() : "");
                                            modelPole.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull()
                                                    ? object.get("POLE_CONFIG").getAsString() : "");
                                            modelPole.setQTY("0");
                                        }

                                        materialList33_pole.add(modelPole);

                                    }

//                                    else if (poleConfig.equalsIgnoreCase("DTR Pole") &&
//                                            DATA_MODEL.getDTR_SURVEY_TYPE().equalsIgnoreCase("Proposed") && "205".equalsIgnoreCase(surveyCode)) {
//
//                                        Toast.makeText(EditBOQDetailsActivity.this, "GAGAGAGAGAGAGAGAGAGA", Toast.LENGTH_SHORT).show();
//                                        MaterialModel model = new MaterialModel();
//                                        DtrMeterModel dtrModel= new DtrMeterModel();
//                                        // ==== MATERIALMODEL ====
//                                        if("302".equalsIgnoreCase(surveyCode)){
//
//                                            isDTRMeterAvailble=true;
//                                            if (hasId) {
//                                                String desc = object.has("MATERIAL_DESC") && !object.get("MATERIAL_DESC").isJsonNull()
//                                                        ? object.get("MATERIAL_DESC").getAsString() : "";
//                                                dtrModel.setMATERIAL_DESC(desc);
//                                                dtrModel.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
//                                                        ? object.get("MATERIALCODE").getAsString() : "");
//                                                dtrModel.setQTY(object.has("COUNT") && !object.get("COUNT").isJsonNull()
//                                                        ? object.get("COUNT").getAsString() : "0");
//                                                dtrModel.setId(object.has("id") && !object.get("id").isJsonNull()
//                                                        ? object.get("id").getAsString() : "");
//                                                dtrModel.setSURVEY_CODE(surveyCode);
//                                                dtrModel.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
//                                                        ? object.get("UOM").getAsString() : "");
//                                                dtrModel.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
//                                                        ? object.get("SAP_CODE").getAsString() : "");
//                                                dtrModel.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
//                                                        ? object.get("SURVEY_CODE").getAsString() : "");
//                                                dtrModel.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull()
//                                                        ? object.get("POLE_CONFIG").getAsString() : "");
//
//                                            } else {
//                                                String desc = object.has("MATERAIL_DESC") && !object.get("MATERAIL_DESC").isJsonNull()
//                                                        ? object.get("MATERAIL_DESC").getAsString() : "";
//                                                dtrModel.setMATERIAL_DESC(desc);
//                                                dtrModel.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
//                                                        ? object.get("SAP_CODE").getAsString() : "");
//                                                dtrModel.setSURVEY_CODE(surveyCode);
//                                                dtrModel.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
//                                                        ? object.get("UOM").getAsString() : "");
//                                                dtrModel.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
//                                                        ? object.get("MATERIALCODE").getAsString() : "");
//                                                dtrModel.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
//                                                        ? object.get("SURVEY_CODE").getAsString() : "");
//                                                dtrModel.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull()
//                                                        ? object.get("POLE_CONFIG").getAsString() : "");
//                                                dtrModel.setQTY("0");
//                                            }
//                                            dtrMeterModel.add(dtrModel);
//                                        }else {
//                                            if (hasId) {
//                                                String desc = object.has("MATERIAL_DESC") && !object.get("MATERIAL_DESC").isJsonNull()
//                                                        ? object.get("MATERIAL_DESC").getAsString() : "";
//                                                model.setMATERIAL_DESC(desc);
//                                                model.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
//                                                        ? object.get("MATERIALCODE").getAsString() : "");
//                                                model.setQTY(object.has("COUNT") && !object.get("COUNT").isJsonNull()
//                                                        ? object.get("COUNT").getAsString() : "0");
//                                                model.setId(object.has("id") && !object.get("id").isJsonNull()
//                                                        ? object.get("id").getAsString() : "");
//                                                model.setSURVEY_CODE(surveyCode);
//                                                model.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
//                                                        ? object.get("UOM").getAsString() : "");
//                                                model.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
//                                                        ? object.get("SAP_CODE").getAsString() : "");
//                                                model.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
//                                                        ? object.get("SURVEY_CODE").getAsString() : "");
//                                                model.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull()
//                                                        ? object.get("POLE_CONFIG").getAsString() : "");
//
//
//                                            } else {
//                                                String desc = object.has("MATERAIL_DESC") && !object.get("MATERAIL_DESC").isJsonNull()
//                                                        ? object.get("MATERAIL_DESC").getAsString() : "";
//                                                model.setMATERIAL_DESC(desc);
//                                                model.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
//                                                        ? object.get("SAP_CODE").getAsString() : "");
//                                                model.setSURVEY_CODE(surveyCode);
//                                                model.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
//                                                        ? object.get("UOM").getAsString() : "");
//                                                model.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
//                                                        ? object.get("MATERIALCODE").getAsString() : "");
//                                                model.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
//                                                        ? object.get("SURVEY_CODE").getAsString() : "");
//                                                model.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull()
//                                                        ? object.get("POLE_CONFIG").getAsString() : "");
//                                                model.setQTY("0");
//                                            }
//                                        }
//
//                                        materialList33.add(model);
//                                    }
                                    else if (poleConfig.equalsIgnoreCase("DTR Pole")
                                            && DATA_MODEL.getDTR_SURVEY_TYPE().equalsIgnoreCase("Proposed")) {

                                        // ================= SURVEY CODE 302 → DTR METER =================
                                        if ("302".equalsIgnoreCase(surveyCode)) {

                                            isDTRMeterAvailble = true;
                                            DtrMeterModel dtrModel = new DtrMeterModel();

                                            if (hasId) {
                                                dtrModel.setMATERIAL_DESC(
                                                        object.has("MATERIAL_DESC") && !object.get("MATERIAL_DESC").isJsonNull()
                                                                ? object.get("MATERIAL_DESC").getAsString() : ""
                                                );
                                                dtrModel.setMATERIAL_CODE(
                                                        object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                                ? object.get("SAP_CODE").getAsString() : ""
                                                );
                                                dtrModel.setQTY(
                                                        object.has("COUNT") && !object.get("COUNT").isJsonNull()
                                                                ? object.get("COUNT").getAsString() : "0"
                                                );
                                                dtrModel.setId(
                                                        object.has("id") && !object.get("id").isJsonNull()
                                                                ? object.get("id").getAsString() : ""
                                                );
                                            } else {
                                                dtrModel.setMATERIAL_DESC(
                                                        object.has("MATERAIL_DESC") && !object.get("MATERAIL_DESC").isJsonNull()
                                                                ? object.get("MATERAIL_DESC").getAsString() : ""
                                                );
                                                dtrModel.setMATERIAL_CODE(
                                                        object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                                ? object.get("MATERIALCODE").getAsString() : ""
                                                );
                                                dtrModel.setQTY("0");
                                            }

                                            dtrModel.setSURVEY_CODE(surveyCode);
                                            dtrModel.setUOM(
                                                    object.has("UOM") && !object.get("UOM").isJsonNull()
                                                            ? object.get("UOM").getAsString() : ""
                                            );
                                            dtrModel.setPOLE_CONFIG(
                                                    object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull()
                                                            ? object.get("POLE_CONFIG").getAsString() : ""
                                            );

                                            dtrMeterModel.add(dtrModel);
                                        }

                                        // ================= SURVEY CODE 205 → MATERIAL =================
                                        else if ("205".equalsIgnoreCase(surveyCode)) {

                                            MaterialModel model = new MaterialModel();

                                            if (hasId) {
                                                model.setMATERIAL_DESC(
                                                        object.has("MATERIAL_DESC") && !object.get("MATERIAL_DESC").isJsonNull()
                                                                ? object.get("MATERIAL_DESC").getAsString() : ""
                                                );
                                                model.setMATERIAL_CODE(
                                                        object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                                ? object.get("SAP_CODE").getAsString() : ""
                                                );
                                                model.setQTY(
                                                        object.has("COUNT") && !object.get("COUNT").isJsonNull()
                                                                ? object.get("COUNT").getAsString() : "0"
                                                );
                                                model.setId(
                                                        object.has("id") && !object.get("id").isJsonNull()
                                                                ? object.get("id").getAsString() : ""
                                                );
                                            } else {
                                                model.setMATERIAL_DESC(
                                                        object.has("MATERAIL_DESC") && !object.get("MATERAIL_DESC").isJsonNull()
                                                                ? object.get("MATERAIL_DESC").getAsString() : ""
                                                );
                                                model.setMATERIAL_CODE(
                                                        object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                                ? object.get("MATERIALCODE").getAsString() : ""
                                                );
                                                model.setQTY("0");
                                            }

                                            model.setSURVEY_CODE(surveyCode);
                                            model.setUOM(
                                                    object.has("UOM") && !object.get("UOM").isJsonNull()
                                                            ? object.get("UOM").getAsString() : ""
                                            );
                                            model.setPOLE_CONFIG(
                                                    object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull()
                                                            ? object.get("POLE_CONFIG").getAsString() : ""
                                            );

                                            materialList33.add(model);
                                        }
                                    }

                                    else if (poleConfig.equalsIgnoreCase("DTR Pole") && !DATA_MODEL.getDTR_SURVEY_TYPE().equalsIgnoreCase("Proposed") && "206".equalsIgnoreCase(surveyCode)) {
                                        MaterialModel model = new MaterialModel();
                                        if (hasId) {
                                            String desc = object.has("MATERIAL_DESC") && !object.get("MATERIAL_DESC").isJsonNull()
                                                    ? object.get("MATERIAL_DESC").getAsString() : "";
                                            model.setMATERIAL_DESC(desc);
                                            model.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                    ? object.get("MATERIALCODE").getAsString() : "");
                                            model.setQTY(object.has("COUNT") && !object.get("COUNT").isJsonNull()
                                                    ? object.get("COUNT").getAsString() : "0");
                                            model.setId(object.has("id") && !object.get("id").isJsonNull()
                                                    ? object.get("id").getAsString() : "");
                                            model.setSURVEY_CODE(surveyCode);
                                            model.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                    ? object.get("UOM").getAsString() : "");
                                            model.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                    ? object.get("SAP_CODE").getAsString() : "");
                                            model.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                    ? object.get("SURVEY_CODE").getAsString() : "");
                                            model.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull()
                                                    ? object.get("POLE_CONFIG").getAsString() : "");

                                        }
                                        else {
                                            String desc = object.has("MATERAIL_DESC") && !object.get("MATERAIL_DESC").isJsonNull()
                                                    ? object.get("MATERAIL_DESC").getAsString() : "";
                                            model.setMATERIAL_DESC(desc);
                                            model.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                    ? object.get("SAP_CODE").getAsString() : "");
                                            model.setSURVEY_CODE(surveyCode);
                                            model.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                    ? object.get("UOM").getAsString() : "");
                                            model.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                    ? object.get("MATERIALCODE").getAsString() : "");
                                            model.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                    ? object.get("SURVEY_CODE").getAsString() : "");
                                            model.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull()
                                                    ? object.get("POLE_CONFIG").getAsString() : "");
                                            model.setQTY("0");
                                        }

                                        materialList33.add(model);

                                    }
                                    else if (poleConfig.equalsIgnoreCase("DTR Pole") && !DATA_MODEL.getDTR_SURVEY_TYPE().equalsIgnoreCase("ENHANCED") && "302".equalsIgnoreCase(surveyCode)) {
                                        MaterialModel model = new MaterialModel();
                                        DtrMeterModel dtrModel = new DtrMeterModel();

                                        if("302".equalsIgnoreCase(surveyCode)){
                                            isDTRMeterAvailble=true;
                                            if (hasId) {
                                                String desc = object.has("MATERIAL_DESC") && !object.get("MATERIAL_DESC").isJsonNull()
                                                        ? object.get("MATERIAL_DESC").getAsString() : "";
                                                dtrModel.setMATERIAL_DESC(desc);
                                                dtrModel.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                        ? object.get("MATERIALCODE").getAsString() : "");
                                                dtrModel.setQTY(object.has("COUNT") && !object.get("COUNT").isJsonNull()
                                                        ? object.get("COUNT").getAsString() : "0");
                                                dtrModel.setId(object.has("id") && !object.get("id").isJsonNull()
                                                        ? object.get("id").getAsString() : "");
                                                dtrModel.setSURVEY_CODE(surveyCode);
                                                dtrModel.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                        ? object.get("UOM").getAsString() : "");
                                                dtrModel.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                        ? object.get("SAP_CODE").getAsString() : "");
                                                dtrModel.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                        ? object.get("SURVEY_CODE").getAsString() : "");
                                                dtrModel.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull()
                                                        ? object.get("POLE_CONFIG").getAsString() : "");

                                            } else {
                                                String desc = object.has("MATERAIL_DESC") && !object.get("MATERAIL_DESC").isJsonNull()
                                                        ? object.get("MATERAIL_DESC").getAsString() : "";
                                                dtrModel.setMATERIAL_DESC(desc);
                                                dtrModel.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                        ? object.get("SAP_CODE").getAsString() : "");
                                                dtrModel.setSURVEY_CODE(surveyCode);
                                                dtrModel.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                        ? object.get("UOM").getAsString() : "");
                                                dtrModel.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                        ? object.get("MATERIALCODE").getAsString() : "");
                                                dtrModel.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                        ? object.get("SURVEY_CODE").getAsString() : "");
                                                dtrModel.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull()
                                                        ? object.get("POLE_CONFIG").getAsString() : "");
                                                dtrModel.setQTY("0");
                                            }
                                            dtrMeterModel.add(dtrModel);
                                        }

                                        if (hasId) {
                                            String desc = object.has("MATERIAL_DESC") && !object.get("MATERIAL_DESC").isJsonNull()
                                                    ? object.get("MATERIAL_DESC").getAsString() : "";
                                            model.setMATERIAL_DESC(desc);
                                            model.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                    ? object.get("MATERIALCODE").getAsString() : "");
                                            model.setQTY(object.has("COUNT") && !object.get("COUNT").isJsonNull()
                                                    ? object.get("COUNT").getAsString() : "0");
                                            model.setId(object.has("id") && !object.get("id").isJsonNull()
                                                    ? object.get("id").getAsString() : "");
                                            model.setSURVEY_CODE(surveyCode);
                                            model.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                    ? object.get("UOM").getAsString() : "");
                                            model.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                    ? object.get("SAP_CODE").getAsString() : "");
                                            model.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                    ? object.get("SURVEY_CODE").getAsString() : "");
                                            model.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull()
                                                    ? object.get("POLE_CONFIG").getAsString() : "");

                                        } else {
                                            String desc = object.has("MATERAIL_DESC") && !object.get("MATERAIL_DESC").isJsonNull()
                                                    ? object.get("MATERAIL_DESC").getAsString() : "";
                                            model.setMATERIAL_DESC(desc);
                                            model.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                    ? object.get("SAP_CODE").getAsString() : "");
                                            model.setSURVEY_CODE(surveyCode);
                                            model.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                    ? object.get("UOM").getAsString() : "");
                                            model.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                    ? object.get("MATERIALCODE").getAsString() : "");
                                            model.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                    ? object.get("SURVEY_CODE").getAsString() : "");
                                            model.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull()
                                                    ? object.get("POLE_CONFIG").getAsString() : "");
                                            model.setQTY("0");
                                        }

                                        materialList33.add(model);

                                    }
                                    else if (poleConfig.equalsIgnoreCase("DTR Pole") && DATA_MODEL.getDTR_SURVEY_TYPE().equalsIgnoreCase("Proposed")|| poleConfig.equalsIgnoreCase("DTR Pole") && DATA_MODEL.getDTR_SURVEY_TYPE().equalsIgnoreCase("ENHANCED")){
                                        DtrMeterModel dtrModel=new DtrMeterModel();
                                        if("302".equalsIgnoreCase(surveyCode)){
                                            isDTRMeterAvailble=true;
                                            if (hasId) {
                                                String desc = object.has("MATERIAL_DESC") && !object.get("MATERIAL_DESC").isJsonNull()
                                                        ? object.get("MATERIAL_DESC").getAsString() : "";
                                                dtrModel.setMATERIAL_DESC(desc);
                                                dtrModel.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                        ? object.get("MATERIALCODE").getAsString() : "");
                                                dtrModel.setQTY(object.has("COUNT") && !object.get("COUNT").isJsonNull()
                                                        ? object.get("COUNT").getAsString() : "0");
                                                dtrModel.setId(object.has("id") && !object.get("id").isJsonNull()
                                                        ? object.get("id").getAsString() : "");
                                                dtrModel.setSURVEY_CODE(surveyCode);
                                                dtrModel.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                        ? object.get("UOM").getAsString() : "");
                                                dtrModel.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                        ? object.get("SAP_CODE").getAsString() : "");
                                                dtrModel.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                        ? object.get("SURVEY_CODE").getAsString() : "");
                                                dtrModel.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull()
                                                        ? object.get("POLE_CONFIG").getAsString() : "");

                                            } else {
                                                String desc = object.has("MATERAIL_DESC") && !object.get("MATERAIL_DESC").isJsonNull()
                                                        ? object.get("MATERAIL_DESC").getAsString() : "";
                                                dtrModel.setMATERIAL_DESC(desc);
                                                dtrModel.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                        ? object.get("SAP_CODE").getAsString() : "");
                                                dtrModel.setSURVEY_CODE(surveyCode);
                                                dtrModel.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                        ? object.get("UOM").getAsString() : "");
                                                dtrModel.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                        ? object.get("MATERIALCODE").getAsString() : "");
                                                dtrModel.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                        ? object.get("SURVEY_CODE").getAsString() : "");
                                                dtrModel.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull()
                                                        ? object.get("POLE_CONFIG").getAsString() : "");
                                                dtrModel.setQTY("0");
                                            }
                                            dtrMeterModel.add(dtrModel);
                                        }
                                    }
//


                                    else if (poleConfig.equalsIgnoreCase("AB-POLE")) {
                                        if ("203".equalsIgnoreCase(surveyCode)) {
                                            // ==== MATERIALMODEL ====
                                            MaterialModel model = new MaterialModel();

                                            if (hasId) {
                                                String desc = object.has("MATERIAL_DESC") && !object.get("MATERIAL_DESC").isJsonNull()
                                                        ? object.get("MATERIAL_DESC").getAsString() : "";
                                                model.setMATERIAL_DESC(desc);
                                                model.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                        ? object.get("MATERIALCODE").getAsString() : "");
                                                model.setQTY(object.has("COUNT") && !object.get("COUNT").isJsonNull()
                                                        ? object.get("COUNT").getAsString() : "0");
                                                model.setId(object.has("id") && !object.get("id").isJsonNull()
                                                        ? object.get("id").getAsString() : "");
                                                model.setSURVEY_CODE(surveyCode);
                                                model.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                        ? object.get("UOM").getAsString() : "");
                                                model.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                        ? object.get("SAP_CODE").getAsString() : "");
                                                model.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                        ? object.get("SURVEY_CODE").getAsString() : "");
                                                model.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull()
                                                        ? object.get("POLE_CONFIG").getAsString() : "");
                                            } else {
                                                String desc = object.has("MATERAIL_DESC") && !object.get("MATERAIL_DESC").isJsonNull()
                                                        ? object.get("MATERAIL_DESC").getAsString() : "";
                                                model.setMATERIAL_DESC(desc);
                                                model.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                        ? object.get("SAP_CODE").getAsString() : "");
                                                model.setSURVEY_CODE(surveyCode);
                                                model.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                        ? object.get("UOM").getAsString() : "");
                                                model.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                        ? object.get("MATERIALCODE").getAsString() : "");
                                                model.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                        ? object.get("SURVEY_CODE").getAsString() : "");
                                                model.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull()
                                                        ? object.get("POLE_CONFIG").getAsString() : "");
                                                model.setQTY("0");
                                            }

                                            materialList33AB.add(model); // ✅ Only add to AB list
                                        }
                                    }
                                    else if (poleConfig.equalsIgnoreCase("HT SERVICE-POLE") && ("204".equalsIgnoreCase(surveyCode) || "203".equalsIgnoreCase(surveyCode) || "202".equalsIgnoreCase(surveyCode))) {
                                        if ("203".equalsIgnoreCase(surveyCode)) {
                                            // ==== MATERIALMODEL ====
                                            MaterialModel model = new MaterialModel();

                                            if (hasId) {
                                                String desc = object.has("MATERIAL_DESC") && !object.get("MATERIAL_DESC").isJsonNull()
                                                        ? object.get("MATERIAL_DESC").getAsString() : "";
                                                model.setMATERIAL_DESC(desc);
                                                model.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                        ? object.get("MATERIALCODE").getAsString() : "");
                                                model.setQTY(object.has("COUNT") && !object.get("COUNT").isJsonNull()
                                                        ? object.get("COUNT").getAsString() : "0");
                                                model.setId(object.has("id") && !object.get("id").isJsonNull()
                                                        ? object.get("id").getAsString() : "");
                                                model.setSURVEY_CODE(surveyCode);
                                                model.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                        ? object.get("UOM").getAsString() : "");
                                                model.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                        ? object.get("SAP_CODE").getAsString() : "");
                                                model.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                        ? object.get("SURVEY_CODE").getAsString() : "");
                                                model.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull()
                                                        ? object.get("POLE_CONFIG").getAsString() : "");
                                            } else {
                                                String desc = object.has("MATERAIL_DESC") && !object.get("MATERAIL_DESC").isJsonNull()
                                                        ? object.get("MATERAIL_DESC").getAsString() : "";
                                                model.setMATERIAL_DESC(desc);
                                                model.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                        ? object.get("SAP_CODE").getAsString() : "");
                                                model.setSURVEY_CODE(surveyCode);
                                                model.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                        ? object.get("UOM").getAsString() : "");
                                                model.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                        ? object.get("MATERIALCODE").getAsString() : "");
                                                model.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                        ? object.get("SURVEY_CODE").getAsString() : "");
                                                model.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull()
                                                        ? object.get("POLE_CONFIG").getAsString() : "");
                                                model.setQTY("0");
                                            }

                                            materialList33AB.add(model); // ✅ Only add to AB list
                                        }
                                        if ("204".equalsIgnoreCase(surveyCode)) {
                                            // ==== MATERIALMODEL ====
                                            MaterialModel model = new MaterialModel();

                                            if (hasId) {
                                                String desc = object.has("MATERIAL_DESC") && !object.get("MATERIAL_DESC").isJsonNull()
                                                        ? object.get("MATERIAL_DESC").getAsString() : "";
                                                model.setMATERIAL_DESC(desc);
                                                model.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                        ? object.get("MATERIALCODE").getAsString() : "");
                                                model.setQTY(object.has("COUNT") && !object.get("COUNT").isJsonNull()
                                                        ? object.get("COUNT").getAsString() : "0");
                                                model.setId(object.has("id") && !object.get("id").isJsonNull()
                                                        ? object.get("id").getAsString() : "");
                                                model.setSURVEY_CODE(surveyCode);
                                                model.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                        ? object.get("UOM").getAsString() : "");
                                                model.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                        ? object.get("SAP_CODE").getAsString() : "");
                                                model.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                        ? object.get("SURVEY_CODE").getAsString() : "");
                                                model.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull()
                                                        ? object.get("POLE_CONFIG").getAsString() : "");
                                            } else {
                                                String desc = object.has("MATERAIL_DESC") && !object.get("MATERAIL_DESC").isJsonNull()
                                                        ? object.get("MATERAIL_DESC").getAsString() : "";
                                                model.setMATERIAL_DESC(desc);
                                                model.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                        ? object.get("SAP_CODE").getAsString() : "");
                                                model.setSURVEY_CODE(surveyCode);
                                                model.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                        ? object.get("UOM").getAsString() : "");
                                                model.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                        ? object.get("MATERIALCODE").getAsString() : "");
                                                model.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                        ? object.get("SURVEY_CODE").getAsString() : "");
                                                model.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull()
                                                        ? object.get("POLE_CONFIG").getAsString() : "");
                                                model.setQTY("0");
                                            }

                                            materialList33.add(model); // ✅ Only add to AB list
                                        }

                                    }
                                    else if (poleConfig.equalsIgnoreCase("SS BAY") && "201".equalsIgnoreCase(surveyCode)) {
                                        MaterialModel model = new MaterialModel();

                                        if (hasId) {
                                            String desc = object.has("MATERIAL_DESC") && !object.get("MATERIAL_DESC").isJsonNull()
                                                    ? object.get("MATERIAL_DESC").getAsString() : "";
                                            model.setMATERIAL_DESC(desc);
                                            model.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                    ? object.get("MATERIALCODE").getAsString() : "");
                                            model.setQTY(object.has("COUNT") && !object.get("COUNT").isJsonNull()
                                                    ? object.get("COUNT").getAsString() : "0");
                                            model.setId(object.has("id") && !object.get("id").isJsonNull()
                                                    ? object.get("id").getAsString() : "");
                                            model.setSURVEY_CODE(surveyCode);
                                            model.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                    ? object.get("UOM").getAsString() : "");
                                            model.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                    ? object.get("SAP_CODE").getAsString() : "");
                                            model.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                    ? object.get("SURVEY_CODE").getAsString() : "");
                                            model.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull()
                                                    ? object.get("POLE_CONFIG").getAsString() : "");

                                        } else {
                                            String desc = object.has("MATERAIL_DESC") && !object.get("MATERAIL_DESC").isJsonNull()
                                                    ? object.get("MATERAIL_DESC").getAsString() : "";
                                            model.setMATERIAL_DESC(desc);
                                            model.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                    ? object.get("SAP_CODE").getAsString() : "");
                                            model.setSURVEY_CODE(surveyCode);
                                            model.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                    ? object.get("UOM").getAsString() : "");
                                            model.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                    ? object.get("MATERIALCODE").getAsString() : "");
                                            model.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                    ? object.get("SURVEY_CODE").getAsString() : "");
                                            model.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull()
                                                    ? object.get("POLE_CONFIG").getAsString() : "");
                                            model.setQTY("0");
                                        }
                                        materialList33.add(model);
                                    }
                                    else if (poleConfig.equalsIgnoreCase("RMU") && "207".equalsIgnoreCase(surveyCode)) {
                                        MaterialModel model = new MaterialModel();
                                        if (hasId) {
                                            String desc = object.has("MATERIAL_DESC") && !object.get("MATERIAL_DESC").isJsonNull()
                                                    ? object.get("MATERIAL_DESC").getAsString() : "";
                                            model.setMATERIAL_DESC(desc);
                                            model.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                    ? object.get("MATERIALCODE").getAsString() : "");
                                            model.setQTY(object.has("COUNT") && !object.get("COUNT").isJsonNull()
                                                    ? object.get("COUNT").getAsString() : "0");
                                            model.setId(object.has("id") && !object.get("id").isJsonNull()
                                                    ? object.get("id").getAsString() : "");
                                            model.setSURVEY_CODE(surveyCode);
                                            model.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                    ? object.get("UOM").getAsString() : "");
                                            model.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                    ? object.get("SAP_CODE").getAsString() : "");
                                            model.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                    ? object.get("SURVEY_CODE").getAsString() : "");
                                            model.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull()
                                                    ? object.get("POLE_CONFIG").getAsString() : "");

                                        } else {
                                            String desc = object.has("MATERAIL_DESC") && !object.get("MATERAIL_DESC").isJsonNull()
                                                    ? object.get("MATERAIL_DESC").getAsString() : "";
                                            model.setMATERIAL_DESC(desc);
                                            model.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                    ? object.get("SAP_CODE").getAsString() : "");
                                            model.setSURVEY_CODE(surveyCode);
                                            model.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                    ? object.get("UOM").getAsString() : "");
                                            model.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                    ? object.get("MATERIALCODE").getAsString() : "");
                                            model.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                    ? object.get("SURVEY_CODE").getAsString() : "");
                                            model.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull()
                                                    ? object.get("POLE_CONFIG").getAsString() : "");
                                            model.setQTY("0");
                                        }
                                        materialList33.add(model);
                                    }
                                    else if (poleConfig.equalsIgnoreCase("UG Cable Route Marker")) {
                                        materialList33.clear();
                                        materialList33_pole.clear();
                                    }


                                } else if (BTN_VALUE.equalsIgnoreCase("33KV")) {
                                    if ("104".equalsIgnoreCase(surveyCode)) {
                                        // ==== MATERIALMODELPOLE ====
                                        MaterialModelPole modelPole = new MaterialModelPole();

                                        if (hasId) {
                                            String desc = object.has("MATERIAL_DESC") && !object.get("MATERIAL_DESC").isJsonNull()
                                                    ? object.get("MATERIAL_DESC").getAsString() : "";
                                            modelPole.setMATERIAL_DESC(desc);
                                            modelPole.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                    ? object.get("MATERIALCODE").getAsString() : "");
                                            modelPole.setQTY(object.has("COUNT") && !object.get("COUNT").isJsonNull()
                                                    ? object.get("COUNT").getAsString() : "0");
                                            modelPole.setId(object.has("id") && !object.get("id").isJsonNull()
                                                    ? object.get("id").getAsString() : "");
                                            modelPole.setSURVEY_CODE(surveyCode);
                                            modelPole.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                    ? object.get("UOM").getAsString() : "");
                                            modelPole.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                    ? object.get("SAP_CODE").getAsString() : "");

                                            modelPole.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                    ? object.get("SURVEY_CODE").getAsString() : "");

                                        } else {
                                            String desc = object.has("MATERAIL_DESC") && !object.get("MATERAIL_DESC").isJsonNull()
                                                    ? object.get("MATERAIL_DESC").getAsString() : "";
                                            modelPole.setMATERIAL_DESC(desc);
                                            modelPole.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                    ? object.get("SAP_CODE").getAsString() : "");
                                            modelPole.setSURVEY_CODE(surveyCode);
                                            modelPole.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                    ? object.get("UOM").getAsString() : "");
                                            modelPole.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                    ? object.get("MATERIALCODE").getAsString() : "");
                                            modelPole.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                    ? object.get("SURVEY_CODE").getAsString() : "");
                                            modelPole.setQTY("0");
                                        }

                                        materialList33_pole.add(modelPole);

                                    } else if (poleConfig.equalsIgnoreCase("AB-POLE") ||
                                            poleConfig.equalsIgnoreCase("Line AB-Pole")) {
                                        if ("105".equalsIgnoreCase(surveyCode)) {
                                            // ==== MATERIALMODEL ====
                                            MaterialModel model = new MaterialModel();

                                            if (hasId) {
                                                String desc = object.has("MATERIAL_DESC") && !object.get("MATERIAL_DESC").isJsonNull()
                                                        ? object.get("MATERIAL_DESC").getAsString() : "";
                                                model.setMATERIAL_DESC(desc);
                                                model.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                        ? object.get("MATERIALCODE").getAsString() : "");
                                                model.setQTY(object.has("COUNT") && !object.get("COUNT").isJsonNull()
                                                        ? object.get("COUNT").getAsString() : "0");
                                                model.setId(object.has("id") && !object.get("id").isJsonNull()
                                                        ? object.get("id").getAsString() : "");
                                                model.setSURVEY_CODE(surveyCode);
                                                model.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                        ? object.get("UOM").getAsString() : "");
                                                model.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                        ? object.get("SAP_CODE").getAsString() : "");
                                                model.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                        ? object.get("SURVEY_CODE").getAsString() : "");
                                                model.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull()
                                                        ? object.get("POLE_CONFIG").getAsString() : "");
                                            } else {
                                                String desc = object.has("MATERAIL_DESC") && !object.get("MATERAIL_DESC").isJsonNull()
                                                        ? object.get("MATERAIL_DESC").getAsString() : "";
                                                model.setMATERIAL_DESC(desc);
                                                model.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                        ? object.get("SAP_CODE").getAsString() : "");
                                                model.setSURVEY_CODE(surveyCode);
                                                model.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                        ? object.get("UOM").getAsString() : "");
                                                model.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                        ? object.get("MATERIALCODE").getAsString() : "");
                                                model.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                        ? object.get("SURVEY_CODE").getAsString() : "");
                                                model.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull()
                                                        ? object.get("POLE_CONFIG").getAsString() : "");
                                                model.setQTY("0");
                                            }

                                            materialList33AB.add(model); // ✅ Only add to AB list


                                        }
                                    }


                                    else if (poleConfig.equalsIgnoreCase("HT SERVICE-POLE")) {

                                        if ("105".equalsIgnoreCase(surveyCode)) {
                                            // ==== MATERIALMODEL ====
                                            MaterialModel model = new MaterialModel();

                                            if (hasId) {
                                                String desc = object.has("MATERIAL_DESC") && !object.get("MATERIAL_DESC").isJsonNull()
                                                        ? object.get("MATERIAL_DESC").getAsString() : "";
                                                model.setMATERIAL_DESC(desc);
                                                model.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                        ? object.get("MATERIALCODE").getAsString() : "");
                                                model.setQTY(object.has("COUNT") && !object.get("COUNT").isJsonNull()
                                                        ? object.get("COUNT").getAsString() : "0");
                                                model.setId(object.has("id") && !object.get("id").isJsonNull()
                                                        ? object.get("id").getAsString() : "");
                                                model.setSURVEY_CODE(surveyCode);
                                                model.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                        ? object.get("UOM").getAsString() : "");
                                                model.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                        ? object.get("SAP_CODE").getAsString() : "");
                                                model.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                        ? object.get("SURVEY_CODE").getAsString() : "");
                                                model.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull()
                                                        ? object.get("POLE_CONFIG").getAsString() : "");
                                            } else {
                                                String desc = object.has("MATERAIL_DESC") && !object.get("MATERAIL_DESC").isJsonNull()
                                                        ? object.get("MATERAIL_DESC").getAsString() : "";
                                                model.setMATERIAL_DESC(desc);
                                                model.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                        ? object.get("SAP_CODE").getAsString() : "");
                                                model.setSURVEY_CODE(surveyCode);
                                                model.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                        ? object.get("UOM").getAsString() : "");
                                                model.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                        ? object.get("MATERIALCODE").getAsString() : "");
                                                model.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                        ? object.get("SURVEY_CODE").getAsString() : "");
                                                model.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull()
                                                        ? object.get("POLE_CONFIG").getAsString() : "");
                                                model.setQTY("0");
                                            }

                                            materialList33AB.add(model); // ✅ Only add to AB list


                                        } else if ("106".equalsIgnoreCase(surveyCode)) {
                                            // ==== MATERIALMODEL ====
                                            MaterialModel model = new MaterialModel();

                                            if (hasId) {
                                                String desc = object.has("MATERIAL_DESC") && !object.get("MATERIAL_DESC").isJsonNull()
                                                        ? object.get("MATERIAL_DESC").getAsString() : "";
                                                model.setMATERIAL_DESC(desc);
                                                model.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                        ? object.get("MATERIALCODE").getAsString() : "");
                                                model.setQTY(object.has("COUNT") && !object.get("COUNT").isJsonNull()
                                                        ? object.get("COUNT").getAsString() : "0");
                                                model.setId(object.has("id") && !object.get("id").isJsonNull()
                                                        ? object.get("id").getAsString() : "");
                                                model.setSURVEY_CODE(surveyCode);
                                                model.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                        ? object.get("UOM").getAsString() : "");
                                                model.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                        ? object.get("SAP_CODE").getAsString() : "");
                                                model.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                        ? object.get("SURVEY_CODE").getAsString() : "");
                                                model.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull()
                                                        ? object.get("POLE_CONFIG").getAsString() : "");
                                            } else {
                                                String desc = object.has("MATERAIL_DESC") && !object.get("MATERAIL_DESC").isJsonNull()
                                                        ? object.get("MATERAIL_DESC").getAsString() : "";
                                                model.setMATERIAL_DESC(desc);
                                                model.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                        ? object.get("SAP_CODE").getAsString() : "");
                                                model.setSURVEY_CODE(surveyCode);
                                                model.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                        ? object.get("UOM").getAsString() : "");
                                                model.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                        ? object.get("MATERIALCODE").getAsString() : "");
                                                model.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                        ? object.get("SURVEY_CODE").getAsString() : "");
                                                model.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull()
                                                        ? object.get("POLE_CONFIG").getAsString() : "");
                                                model.setQTY("0");
                                            }
                                            materialList33.add(model);
                                        }
                                    } else if (poleConfig.equalsIgnoreCase("33/11KV SS-BAY") && "103".equalsIgnoreCase(surveyCode)) {
                                        MaterialModel model = new MaterialModel();

                                        if (hasId) {
                                            String desc = object.has("MATERIAL_DESC") && !object.get("MATERIAL_DESC").isJsonNull()
                                                    ? object.get("MATERIAL_DESC").getAsString() : "";
                                            model.setMATERIAL_DESC(desc);
                                            model.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                    ? object.get("MATERIALCODE").getAsString() : "");
                                            model.setQTY(object.has("COUNT") && !object.get("COUNT").isJsonNull()
                                                    ? object.get("COUNT").getAsString() : "0");
                                            model.setId(object.has("id") && !object.get("id").isJsonNull()
                                                    ? object.get("id").getAsString() : "");
                                            model.setSURVEY_CODE(surveyCode);
                                            model.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                    ? object.get("UOM").getAsString() : "");
                                            model.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                    ? object.get("SAP_CODE").getAsString() : "");
                                            model.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                    ? object.get("SURVEY_CODE").getAsString() : "");

                                        } else {
                                            String desc = object.has("MATERAIL_DESC") && !object.get("MATERAIL_DESC").isJsonNull()
                                                    ? object.get("MATERAIL_DESC").getAsString() : "";
                                            model.setMATERIAL_DESC(desc);
                                            model.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                    ? object.get("SAP_CODE").getAsString() : "");
                                            model.setSURVEY_CODE(surveyCode);
                                            model.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                    ? object.get("UOM").getAsString() : "");
                                            model.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                    ? object.get("MATERIALCODE").getAsString() : "");
                                            model.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                    ? object.get("SURVEY_CODE").getAsString() : "");
                                            model.setQTY("0");
                                        }
                                        materialList33.add(model);
                                    } else if (poleConfig.equalsIgnoreCase("33/11KV SS") && "102".equalsIgnoreCase(surveyCode)) {
                                        MaterialModel model = new MaterialModel();

                                        if (hasId) {
                                            String desc = object.has("MATERIAL_DESC") && !object.get("MATERIAL_DESC").isJsonNull()
                                                    ? object.get("MATERIAL_DESC").getAsString() : "";
                                            model.setMATERIAL_DESC(desc);
                                            model.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                    ? object.get("MATERIALCODE").getAsString() : "");
                                            model.setQTY(object.has("COUNT") && !object.get("COUNT").isJsonNull()
                                                    ? object.get("COUNT").getAsString() : "0");
                                            model.setId(object.has("id") && !object.get("id").isJsonNull()
                                                    ? object.get("id").getAsString() : "");
                                            model.setSURVEY_CODE(surveyCode);
                                            model.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                    ? object.get("UOM").getAsString() : "");
                                            model.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                    ? object.get("SAP_CODE").getAsString() : "");
                                            model.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                    ? object.get("SURVEY_CODE").getAsString() : "");

                                        } else {
                                            String desc = object.has("MATERAIL_DESC") && !object.get("MATERAIL_DESC").isJsonNull()
                                                    ? object.get("MATERAIL_DESC").getAsString() : "";
                                            model.setMATERIAL_DESC(desc);
                                            model.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                    ? object.get("SAP_CODE").getAsString() : "");
                                            model.setSURVEY_CODE(surveyCode);
                                            model.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                    ? object.get("UOM").getAsString() : "");
                                            model.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                    ? object.get("MATERIALCODE").getAsString() : "");
                                            model.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                    ? object.get("SURVEY_CODE").getAsString() : "");
                                            model.setQTY("0");
                                        }
                                        materialList33.add(model);
                                    } else if (poleConfig.equalsIgnoreCase("EHT SS-BAY") && "101".equalsIgnoreCase(surveyCode)) {
                                        MaterialModel model = new MaterialModel();
                                        if (hasId) {
                                            String desc = object.has("MATERIAL_DESC") && !object.get("MATERIAL_DESC").isJsonNull()
                                                    ? object.get("MATERIAL_DESC").getAsString() : "";
                                            model.setMATERIAL_DESC(desc);
                                            model.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                    ? object.get("MATERIALCODE").getAsString() : "");
                                            model.setQTY(object.has("COUNT") && !object.get("COUNT").isJsonNull()
                                                    ? object.get("COUNT").getAsString() : "0");
                                            model.setId(object.has("id") && !object.get("id").isJsonNull()
                                                    ? object.get("id").getAsString() : "");
                                            model.setSURVEY_CODE(surveyCode);
                                            model.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                    ? object.get("UOM").getAsString() : "");
                                            model.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                    ? object.get("SAP_CODE").getAsString() : "");
                                            model.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                    ? object.get("SURVEY_CODE").getAsString() : "");
                                            model.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull()
                                                    ? object.get("POLE_CONFIG").getAsString() : "");

                                        } else {
                                            String desc = object.has("MATERAIL_DESC") && !object.get("MATERAIL_DESC").isJsonNull()
                                                    ? object.get("MATERAIL_DESC").getAsString() : "";
                                            model.setMATERIAL_DESC(desc);
                                            model.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                    ? object.get("SAP_CODE").getAsString() : "");
                                            model.setSURVEY_CODE(surveyCode);
                                            model.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                    ? object.get("UOM").getAsString() : "");
                                            model.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                    ? object.get("MATERIALCODE").getAsString() : "");
                                            model.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                    ? object.get("SURVEY_CODE").getAsString() : "");
                                            model.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull()
                                                    ? object.get("POLE_CONFIG").getAsString() : "");
                                            model.setQTY("0");
                                        }
                                        materialList33.add(model);
                                    } else if (poleConfig.equalsIgnoreCase("RMU") && "107".equalsIgnoreCase(surveyCode)) {
                                        MaterialModel model = new MaterialModel();

                                        if (hasId) {
                                            String desc = object.has("MATERIAL_DESC") && !object.get("MATERIAL_DESC").isJsonNull()
                                                    ? object.get("MATERIAL_DESC").getAsString() : "";
                                            model.setMATERIAL_DESC(desc);
                                            model.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                    ? object.get("MATERIALCODE").getAsString() : "");
                                            model.setQTY(object.has("COUNT") && !object.get("COUNT").isJsonNull()
                                                    ? object.get("COUNT").getAsString() : "0");
                                            model.setId(object.has("id") && !object.get("id").isJsonNull()
                                                    ? object.get("id").getAsString() : "");
                                            model.setSURVEY_CODE(surveyCode);
                                            model.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                    ? object.get("UOM").getAsString() : "");
                                            model.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                    ? object.get("SAP_CODE").getAsString() : "");
                                            model.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                    ? object.get("SURVEY_CODE").getAsString() : "");
                                            model.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull()
                                                    ? object.get("POLE_CONFIG").getAsString() : "");

                                        } else {
                                            String desc = object.has("MATERAIL_DESC") && !object.get("MATERAIL_DESC").isJsonNull()
                                                    ? object.get("MATERAIL_DESC").getAsString() : "";
                                            model.setMATERIAL_DESC(desc);
                                            model.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                    ? object.get("SAP_CODE").getAsString() : "");
                                            model.setSURVEY_CODE(surveyCode);
                                            model.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                    ? object.get("UOM").getAsString() : "");
                                            model.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                    ? object.get("MATERIALCODE").getAsString() : "");
                                            model.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                    ? object.get("SURVEY_CODE").getAsString() : "");
                                            model.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull()
                                                    ? object.get("POLE_CONFIG").getAsString() : "");
                                            model.setQTY("0");
                                        }
                                        materialList33.add(model);
                                    }


                                } else {
                                    if (poleConfig.isEmpty() && DATA_MODEL.getLINE_TYPE().equalsIgnoreCase("UNDERGROUND")) {
                                        if ("302".equalsIgnoreCase(surveyCode)) {
                                            MaterialModel model = new MaterialModel();

                                            if (hasId) {
                                                String desc = object.has("MATERIAL_DESC") && !object.get("MATERIAL_DESC").isJsonNull()
                                                        ? object.get("MATERIAL_DESC").getAsString() : "";
                                                model.setMATERIAL_DESC(desc);
                                                model.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                        ? object.get("MATERIALCODE").getAsString() : "");
                                                model.setQTY(object.has("COUNT") && !object.get("COUNT").isJsonNull()
                                                        ? object.get("COUNT").getAsString() : "0");
                                                model.setId(object.has("id") && !object.get("id").isJsonNull()
                                                        ? object.get("id").getAsString() : "");
                                                model.setSURVEY_CODE(surveyCode);
                                                model.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                        ? object.get("UOM").getAsString() : "");
                                                model.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                        ? object.get("SAP_CODE").getAsString() : "");
                                                model.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                        ? object.get("SURVEY_CODE").getAsString() : "");
                                                model.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull()
                                                        ? object.get("POLE_CONFIG").getAsString() : "");


                                            } else {
                                                String desc = object.has("MATERAIL_DESC") && !object.get("MATERAIL_DESC").isJsonNull()
                                                        ? object.get("MATERAIL_DESC").getAsString() : "";
                                                model.setMATERIAL_DESC(desc);
                                                model.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                        ? object.get("SAP_CODE").getAsString() : "");
                                                model.setSURVEY_CODE(surveyCode);
                                                model.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                        ? object.get("UOM").getAsString() : "");
                                                model.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                        ? object.get("MATERIALCODE").getAsString() : "");
                                                model.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                        ? object.get("SURVEY_CODE").getAsString() : "");
                                                model.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull()
                                                        ? object.get("POLE_CONFIG").getAsString() : "");
                                                model.setQTY("0");
                                            }

                                            materialList33.add(model);
                                        }
                                    } else {
                                        if ("301".equalsIgnoreCase(surveyCode)) {
                                            // ==== MATERIALMODELPOLE ====
                                            MaterialModel modelPole = new MaterialModel();

                                            if (hasId) {
                                                String desc = object.has("MATERIAL_DESC") && !object.get("MATERIAL_DESC").isJsonNull()
                                                        ? object.get("MATERIAL_DESC").getAsString() : "";
                                                modelPole.setMATERIAL_DESC(desc);
                                                modelPole.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                        ? object.get("MATERIALCODE").getAsString() : "");
                                                modelPole.setQTY(object.has("COUNT") && !object.get("COUNT").isJsonNull()
                                                        ? object.get("COUNT").getAsString() : "0");
                                                modelPole.setId(object.has("id") && !object.get("id").isJsonNull()
                                                        ? object.get("id").getAsString() : "");
                                                modelPole.setSURVEY_CODE(surveyCode);
                                                modelPole.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                        ? object.get("UOM").getAsString() : "");
                                                modelPole.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                        ? object.get("SAP_CODE").getAsString() : "");

                                                modelPole.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                        ? object.get("SURVEY_CODE").getAsString() : "");

                                            } else {
                                                String desc = object.has("MATERAIL_DESC") && !object.get("MATERAIL_DESC").isJsonNull()
                                                        ? object.get("MATERAIL_DESC").getAsString() : "";
                                                modelPole.setMATERIAL_DESC(desc);
                                                modelPole.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                        ? object.get("SAP_CODE").getAsString() : "");
                                                modelPole.setSURVEY_CODE(surveyCode);
                                                modelPole.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                        ? object.get("UOM").getAsString() : "");
                                                modelPole.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                        ? object.get("MATERIALCODE").getAsString() : "");
                                                modelPole.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                        ? object.get("SURVEY_CODE").getAsString() : "");
                                                modelPole.setQTY("0");
                                            }

                                            materialList33.add(modelPole);

                                        }
                                        if ("302".equalsIgnoreCase(surveyCode)) {
                                            MaterialModel model = new MaterialModel();

                                            if (hasId) {
                                                String desc = object.has("MATERIAL_DESC") && !object.get("MATERIAL_DESC").isJsonNull()
                                                        ? object.get("MATERIAL_DESC").getAsString() : "";
                                                model.setMATERIAL_DESC(desc);
                                                model.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                        ? object.get("MATERIALCODE").getAsString() : "");
                                                model.setQTY(object.has("COUNT") && !object.get("COUNT").isJsonNull()
                                                        ? object.get("COUNT").getAsString() : "0");
                                                model.setId(object.has("id") && !object.get("id").isJsonNull()
                                                        ? object.get("id").getAsString() : "");
                                                model.setSURVEY_CODE(surveyCode);
                                                model.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                        ? object.get("UOM").getAsString() : "");
                                                model.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                        ? object.get("SAP_CODE").getAsString() : "");
                                                model.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                        ? object.get("SURVEY_CODE").getAsString() : "");
                                                model.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull()
                                                        ? object.get("POLE_CONFIG").getAsString() : "");


                                            } else {
                                                String desc = object.has("MATERAIL_DESC") && !object.get("MATERAIL_DESC").isJsonNull()
                                                        ? object.get("MATERAIL_DESC").getAsString() : "";
                                                model.setMATERIAL_DESC(desc);
                                                model.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull()
                                                        ? object.get("SAP_CODE").getAsString() : "");
                                                model.setSURVEY_CODE(surveyCode);
                                                model.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull()
                                                        ? object.get("UOM").getAsString() : "");
                                                model.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull()
                                                        ? object.get("MATERIALCODE").getAsString() : "");
                                                model.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull()
                                                        ? object.get("SURVEY_CODE").getAsString() : "");
                                                model.setPOLE_CONFIG(object.has("POLE_CONFIG") && !object.get("POLE_CONFIG").isJsonNull()
                                                        ? object.get("POLE_CONFIG").getAsString() : "");
                                                model.setQTY("0");
                                            }

                                            materialList33.add(model);
                                        }
                                    }

                                }


                            }

                            // ==== TEMP LISTS ====
                            tempList.clear();
                            for (MaterialModel m : materialList33) {
                                MaterialModel copy = new MaterialModel();
                                copy.setMATERIAL_DESC(m.getMATERIAL_DESC());
                                copy.setMATERIAL_CODE(m.getMATERIAL_CODE());
                                copy.setQTY(m.getQTY());
                                copy.setDATA_ID(m.getDATA_ID());
                                tempList.add(copy);
                            }

                            tempListPole.clear();
                            for (MaterialModelPole m : materialList33_pole) {
                                MaterialModelPole copy = new MaterialModelPole();
                                copy.setMATERIAL_DESC(m.getMATERIAL_DESC());
                                copy.setMATERIAL_CODE(m.getMATERIAL_CODE());
                                copy.setQTY(m.getQTY());
                                copy.setDATA_ID(m.getDATA_ID());
                                copy.setSURVEY_CODE(m.getSURVEY_CODE());
                                copy.setUOM(m.getUOM());
                                tempListPole.add(copy);
                            }

                            tempAB.clear();
                            for (MaterialModel m : materialList33AB) {
                                MaterialModel copy = new MaterialModel();
                                copy.setMATERIAL_DESC(m.getMATERIAL_DESC());
                                copy.setMATERIAL_CODE(m.getMATERIAL_CODE());
                                copy.setQTY(m.getQTY());
                                copy.setDATA_ID(m.getDATA_ID());
                                copy.setSURVEY_CODE(m.getSURVEY_CODE());
                                copy.setUOM(m.getUOM());
                                tempAB.add(copy);
                            }

                            tempDTRMeter.clear();
                            for (DtrMeterModel m : dtrMeterModel) {
                                DtrMeterModel copy = new DtrMeterModel();
                                copy.setMATERIAL_DESC(m.getMATERIAL_DESC());
                                copy.setMATERIAL_CODE(m.getMATERIAL_CODE());
                                copy.setQTY(m.getQTY());
                                copy.setDATA_ID(m.getDATA_ID());
                                copy.setSURVEY_CODE(m.getSURVEY_CODE());
                                copy.setUOM(m.getUOM());
                                tempDTRMeter.add(copy);
                            }

                            // ==== SET ADAPTERS ====
                            setBoQAdapter(materialList33);
                            setBoQAdapterPole(materialList33_pole);
                            setBoQABAdapterPole(materialList33AB);
                            //setDTRMeterBoQAdapterPole(dtrMeterModel);





                            if (BTN_VALUE.equalsIgnoreCase("11KV")) {
                                if (poleConfig.equalsIgnoreCase("RMU")
                                        || poleConfig.equalsIgnoreCase("UG CABLE ROUTE MARKER")
                                        || poleConfig.equalsIgnoreCase("SS BAY")) {
                                    sap_33_material_layout_pole.setVisibility(View.GONE);
                                }
                                else if (poleConfig.equalsIgnoreCase("DTR Pole") && isDTRMeterAvailble){
                                    sap_33_material_layout_dtr_meter.setVisibility(View.VISIBLE);
                                    sap_33_material_layout_pole.setVisibility(View.VISIBLE);
                                    sap_33_material_layout.setVisibility(View.VISIBLE);
                                    sap_33_material_layout_AB.setVisibility(View.GONE);
                                    setBoQAdapter(materialList33);
                                    setBoQAdapterPole(materialList33_pole);
                                    setDTRMeterBoQAdapter(dtrMeterModel);
                                    //setBoQABAdapterPole(materialList33AB);


                                }


                            } else if (BTN_VALUE.equalsIgnoreCase("33KV")) {
                                if (poleConfig.equalsIgnoreCase("RMU")
                                        || poleConfig.equalsIgnoreCase("UG CABLE ROUTE MARKER")
                                        || poleConfig.equalsIgnoreCase("33/11KV SS-BAY")
                                        || poleConfig.equalsIgnoreCase("EHT SS-BAY")
                                        || poleConfig.equalsIgnoreCase("33/11KV SS")

                                ) {
                                    sap_33_material_layout_pole.setVisibility(View.GONE);
                                }
                            }

                            if (BTN_VALUE.equalsIgnoreCase("LT")) {
                                sap_33_material_layout_pole.setVisibility(View.GONE);
                            }

                            // ==== SHOW / HIDE POLE RECYCLERVIEW ====
                            if (materialList33_pole.isEmpty()) {
                                sap_33_material_layout_pole.setVisibility(View.GONE);
                            } else {
                                material_rv_33_pole.setVisibility(View.VISIBLE);
                            }

                            if (materialList33.isEmpty()) {
                                sap_33_material_layout.setVisibility(View.GONE);
                            } else {
                                material_rv_33.setVisibility(View.VISIBLE);
                            }

                            if (materialList33AB.isEmpty()) {
                                sap_33_material_layout_AB.setVisibility(View.GONE);
                            } else {
                                material_rv_33_AB.setVisibility(View.VISIBLE);
                            }

                            if (dtrMeterModel.isEmpty()){
                                sap_33_material_layout_dtr_meter.setVisibility(View.GONE);
                            }else {
                                material_rv_33_dtr_meter.setVisibility(View.VISIBLE);
                            }

                            if (sap_33_material_layout_pole.getVisibility() == View.VISIBLE || sap_33_material_layout.getVisibility() == View.VISIBLE || sap_33_material_layout_AB.getVisibility() == View.VISIBLE) {
                                tv_error_msg.setVisibility(View.GONE);
                            } else {
                                System.out.println("TRIGGERED_THIS-->");
                                tv_error_msg.setVisibility(View.VISIBLE);
                            }


                        }
                    }

                } else {
                    //Log.e("Retrofit", "Error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }


        });
    }


//    private void updateDTRDetails() {
//        loader_layout.setVisibility(View.VISIBLE);
//        Map<String, String> data = new HashMap<>();
//        data.put("LOCATION_ID", DATA_MODEL.getLOCATION_ID());
//        String poleConfig = DATA_MODEL.getPOLE_CONFIG();
//        if (BTN_VALUE.equalsIgnoreCase("33KV")) {
//            data.put("SURVEY_TYPE", "33");
//            switch (poleConfig.toUpperCase()) {
//                case "LINE-POLE":
//                    data.put("POLE_CONFIG", "33KV LINE");
//                    break;
//
//                case "HT SERVICE-POLE":
//                    data.put("POLE_CONFIG", "33KV METERING");
//                    break;
//
//                case "RMU":
//                    data.put("POLE_CONFIG", "33KV RMU");
//                    break;
//
//                case "AB-POLE":
//                    data.put("POLE_CONFIG", "33KV LINE AB SWITCH");
//                    break;
//            }
//
//        } else if (BTN_VALUE.equalsIgnoreCase("11KV")) {
//            data.put("SURVEY_TYPE", "11");
//
//
//            switch (poleConfig.toUpperCase()) {
//                case "LINE-POLE":
//                    data.put("POLE_CONFIG", "11KV LINE");
//                    break;
//
//                case "DTR POLE":
//                    if ("Proposed".equalsIgnoreCase(DATA_MODEL.getDTR_SURVEY_TYPE())) {
//                        data.put("POLE_CONFIG", "DTR NEW");
//                    } else {
//                        data.put("POLE_CONFIG", "DTR ENHANCEMENT");
//                    }
//                    break;
//
//                case "HT SERVICE-POLE":
//                    data.put("POLE_CONFIG", "11KV METERING");
//                    break;
//
//                case "RMU":
//                    data.put("POLE_CONFIG", "11KV RMU");
//                    break;
//
//                case "AB-POLE":
//                    data.put("POLE_CONFIG", "11KV LINE AB SWITCH");
//                    break;
//            }
//        } else if (BTN_VALUE.equalsIgnoreCase("LT")) {
//            data.put("SURVEY_TYPE", "LT");
//            data.put("POLE_CONFIG", "LT LINE");
//
//        }
//
//
//        System.out.println("POLE_CONFIG-->" + DATA_MODEL.getPOLE_CONFIG());
//
//        //if (DATA_MODEL.getPOLE_CONFIG().equalsIgnoreCase(""))
//
//        JsonObject jsonObject = new JsonObject();
//        for (Map.Entry<String, String> entry : data.entrySet()) {
//            jsonObject.addProperty(entry.getKey(), entry.getValue());
//        }
//        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance().create(RetrofitServiceEstimates.class);
//        Call<JsonObject> responseVoCall = retrofitService.getExistingMaterials(jsonObject);
//
//        responseVoCall.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                loader_layout.setVisibility(View.GONE);
//                if (response.body() != null) {
//                    JsonObject surveyedResponseVo = response.body();
//                    if (surveyedResponseVo.has("STATUS") && !surveyedResponseVo.get("STATUS").isJsonNull()) {
//                        String status = surveyedResponseVo.get("STATUS").getAsString();
//                        if (MobileConstants.TRUE.equalsIgnoreCase(status)) {
//                            JsonArray array = surveyedResponseVo.getAsJsonArray("DATA");
//
//                            Map<String, MaterialModel> materialMap = new HashMap<>();
//
//                            for (int i = 0; i < array.size(); i++) {
//                                JsonObject object = array.get(i).getAsJsonObject();
//                                MaterialModel model = new MaterialModel();
//                                boolean hasId = object.has("id") && !object.get("id").isJsonNull() && !object.get("id").getAsString().trim().isEmpty();
//                                if (hasId) {
//                                    String desc = object.has("MATERIAL_DESC") && !object.get("MATERIAL_DESC").isJsonNull() ? object.get("MATERIAL_DESC").getAsString() : "";
//                                    model.setMATERIAL_DESC(desc);
//                                    model.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull() ? object.get("MATERIALCODE").getAsString() : "");
//                                    model.setQTY(object.has("COUNT") && !object.get("COUNT").isJsonNull() && !object.get("COUNT").getAsString().trim().isEmpty() ? object.get("COUNT").getAsString() : "0");
//                                    model.setId(object.has("id") && !object.get("id").isJsonNull() ? object.get("id").getAsString() : "");
//                                    model.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull() ? object.get("SURVEY_CODE").getAsString() : "");
//                                    model.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull() ? object.get("UOM").getAsString() : "");
//                                    model.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull() ? object.get("SAP_CODE").getAsString() : "");
//                                    materialMap.put(desc, model);
//
//
//                                } else {
//                                    // Data without ID
//                                    String desc = object.has("MATERAIL_DESC") && !object.get("MATERAIL_DESC").isJsonNull() ? object.get("MATERAIL_DESC").getAsString() : "";
//                                    model.setMATERIAL_DESC(desc);
//                                    model.setMATERIAL_CODE(object.has("SAP_CODE") && !object.get("SAP_CODE").isJsonNull() ? object.get("SAP_CODE").getAsString() : "");
//                                    model.setSURVEY_CODE(object.has("SURVEY_CODE") && !object.get("SURVEY_CODE").isJsonNull() ? object.get("SURVEY_CODE").getAsString() : "");
//                                    model.setUOM(object.has("UOM") && !object.get("UOM").isJsonNull() ? object.get("UOM").getAsString() : "");
//                                    model.setMATERIAL_CODE(object.has("MATERIALCODE") && !object.get("MATERIALCODE").isJsonNull() ? object.get("MATERIALCODE").getAsString() : "");
//                                    model.setQTY("0");
//
//                                    if (!materialMap.containsKey(desc)) {
//                                        materialMap.put(desc, model);
//                                    }
//                                }
//                            }
//
//
//                            materialList33.clear();
//                            materialList33.addAll(materialMap.values());
//
//
//
//                            tempList.clear();
//                            for (MaterialModel m : materialMap.values()) {
//                                MaterialModel copy = new MaterialModel();
//                                copy.setMATERIAL_DESC(m.getMATERIAL_DESC());
//                                copy.setMATERIAL_CODE(m.getMATERIAL_CODE());
//                                copy.setQTY(m.getQTY());
//                                copy.setDATA_ID(m.getDATA_ID());
//                                tempList.add(copy);
//                            }
//
//
//
//                            setBoQAdapter(materialList33);
//
//                            setBoQAdapterPole(materialList33_pole);
//
//
//                        }
//                    }
//
//                } else {
//                    Log.e("Retrofit", "Error: " + response.errorBody());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                loader_layout.setVisibility(View.GONE);
//                Log.e("Retrofit", "Failure: " + t.getMessage());
//            }
//        });
//
//
//    }
}