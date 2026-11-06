package com.apepdcl.gis_survey.PMI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.DashBoard;
import com.apepdcl.gis_survey.PMI.models.DTR;
import com.apepdcl.gis_survey.PMI.models.DefectRVModel;
import com.apepdcl.gis_survey.PMI.models.DefectsModel;
import com.apepdcl.gis_survey.PMI.models.EHT_SS_MASTER;
import com.apepdcl.gis_survey.PMI.models.FEEDER_MASTER_11KV;
import com.apepdcl.gis_survey.PMI.models.FEEDER_MASTER_33KV;
import com.apepdcl.gis_survey.PMI.models.PoleModel11KV;
import com.apepdcl.gis_survey.PMI.models.PoleModel33KV;
import com.apepdcl.gis_survey.PMI.models.PoleModelLT;
import com.apepdcl.gis_survey.PMI.models.PostingModel;
import com.apepdcl.gis_survey.PMI.models.SS_MASTER_11KV;

import com.apepdcl.gis.app.R;

import com.apepdcl.gis_survey.PMI.new_screens.RV11KVAdapter;
import com.apepdcl.gis_survey.module_11KV.ConvertImage;
import com.apepdcl.gis_survey.retrofitservice.RetrofitService;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.leo.searchablespinner.SearchableSpinner;
import com.leo.searchablespinner.interfaces.OnItemSelectListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PmiHome extends AppCompatActivity implements RV11KVAdapter.OnItemClickListener {
    RelativeLayout back_layout;
    RelativeLayout profile_layout;

    LinearLayout main_layout;
    Spinner sp_voltage_level;

    LinearLayout loader;

    //33KV
    LinearLayout main_layout_33;
    TextView sub_station_name_33, feeder_33kv;
    Button btn_next_33;
    ProgressBar loader_33;

    //11KV
    LinearLayout main_layout_11;
    TextView sub_station_name_11, feeder_11kv;
    Button btn_next_11;
    ProgressBar loader_11;

    //LT
    LinearLayout main_layout_lt;
    TextView dtr_code;
    Spinner sp_circuit;
    Button btn_next_lt;
    ProgressBar loader_lt;

    private ArcGISDatabase arcGISDatabase;
    private RetrofitService retrofitService;
    private RetrofitServicePMI retrofitServicePmi;
    ArrayList<EHT_SS_MASTER> ssMasters33;
    ArrayList<FEEDER_MASTER_33KV> feederMaster33;
    ArrayList<SS_MASTER_11KV> ssMaster11;
    ArrayList<FEEDER_MASTER_11KV> feederMaster11;
    ArrayList<DTR> dtrMaster;

    String selectedSSCode33, selectedFeederId33, selectedSSCode11, selectedFeederId11, selectedDtrCode;
    String selectedFeederName33, selectedFeederName11;

    private FusedLocationProviderClient fusedLocationClient;
    private double currentLat, currentLog;

    public static String SELECTED_VOLTAGE;
    public static ArrayList<PoleModel33KV> polesList33;
    public static ArrayList<PoleModel11KV> polesList11;
    public static ArrayList<PoleModelLT> polesListLT;

    LinearLayout transmit_layout;
    TextView poles_33kv, poles_11kv, poles_lt;
    Button btn_transmit;
    ProgressBar loader_transmit;
    ArrayList<PostingModel> postingList;
    PmiDatabase pmiDatabase;
    public static ArrayList<String> defectsTypeList;
    public static ArrayList<DefectsModel> allDefectsList;
    public static ArrayList<DefectRVModel> defectRVList;

    //NEW 33KV and 11KV
    Spinner sp_feedername, sp_ssname;
    Spinner sp_feeder_name_11, sp_ss_name_11;

    RecyclerView rv_11;
    LinearLayout new_main_layout_11;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pmi_home);
        getSupportActionBar().hide();

        arcGISDatabase = new ArcGISDatabase(this);
        pmiDatabase = new PmiDatabase(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLatLongs();
        postingList = pmiDatabase.getPmiPostingData();

        //NEW 33KV
        sp_feedername = findViewById(R.id.sp_feedername);
        sp_ssname = findViewById(R.id.sp_ssname);
        sp_feeder_name_11 = findViewById(R.id.sp_feeder_name_11);
        sp_ss_name_11 = findViewById(R.id.sp_ss_name_11);
        rv_11 = findViewById(R.id.rv_11);
        new_main_layout_11 = findViewById(R.id.new_main_layout_11);


        //TRANSMIT
        transmit_layout = findViewById(R.id.transmit_layout);
        poles_33kv = findViewById(R.id.poles_33kv);
        poles_11kv = findViewById(R.id.poles_11kv);
        poles_lt = findViewById(R.id.poles_lt);
        btn_transmit = findViewById(R.id.btn_transmit);
        loader_transmit = findViewById(R.id.loader_transmit);

        ssMasters33 = new ArrayList<>();
        feederMaster33 = new ArrayList<>();
        ssMaster11 = new ArrayList<>();
        feederMaster11 = new ArrayList<>();
        dtrMaster = new ArrayList<>();

        back_layout = findViewById(R.id.back_layout);
        profile_layout = findViewById(R.id.profile_layout);
        main_layout = findViewById(R.id.main_layout);
        sp_voltage_level = findViewById(R.id.sp_voltage_level);
        loader = findViewById(R.id.loader);

        main_layout_33 = findViewById(R.id.main_layout_33);
        sub_station_name_33 = findViewById(R.id.sub_station_name_33);
        feeder_33kv = findViewById(R.id.feeder_33kv);
        btn_next_33 = findViewById(R.id.btn_next_33);
        loader_33 = findViewById(R.id.loader_33);

        main_layout_11 = findViewById(R.id.main_layout_11);
        sub_station_name_11 = findViewById(R.id.sub_station_name_11);
        feeder_11kv = findViewById(R.id.feeder_11kv);
        btn_next_11 = findViewById(R.id.btn_next_11);
        loader_11 = findViewById(R.id.loader_11);

        main_layout_lt = findViewById(R.id.main_layout_lt);
        dtr_code = findViewById(R.id.dtr_code);
        sp_circuit = findViewById(R.id.sp_circuit);
        btn_next_lt = findViewById(R.id.btn_next_lt);
        loader_lt = findViewById(R.id.loader_lt);

        polesList33 = new ArrayList<>();
        polesList11 = new ArrayList<>();
        polesListLT = new ArrayList<>();

        defectsTypeList = new ArrayList<>();
        allDefectsList = new ArrayList<>();
        defectRVList = new ArrayList<>();

        //loadFeederMasterData();
        loadFeederMasterDataForSingleUser();
        loadDefectsMaster();
        setTransmitData();


        sp_voltage_level.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SELECTED_VOLTAGE = sp_voltage_level.getSelectedItem().toString();
                if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("33KV")) {
                    main_layout_33.setVisibility(View.VISIBLE);
                    main_layout_11.setVisibility(View.GONE);
                    main_layout_lt.setVisibility(View.GONE);
                } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("11KV")) {
                    main_layout_33.setVisibility(View.GONE);
                    main_layout_11.setVisibility(View.VISIBLE);
                    main_layout_lt.setVisibility(View.GONE);

                } else if (sp_voltage_level.getSelectedItem().toString().equalsIgnoreCase("LT")) {
                    main_layout_33.setVisibility(View.GONE);
                    main_layout_11.setVisibility(View.GONE);
                    main_layout_lt.setVisibility(View.VISIBLE);
                } else {
                    main_layout_33.setVisibility(View.GONE);
                    main_layout_11.setVisibility(View.GONE);
                    main_layout_lt.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sub_station_name_33.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open33KVSSNearByDialog(sub_station_name_33);

                ArrayList<String> allList = new ArrayList<>();
                for (int i = 0; i < ssMasters33.size(); i++) {
                    allList.add(ssMasters33.get(i).getEHT_SS_NAME() + "~" + ssMasters33.get(i).getEHT_SS_CODE());
                }
                SearchableSpinner searchableSpinner = new SearchableSpinner(PmiHome.this);
                searchableSpinner.setWindowTitle("SELECT EHT SS");
                searchableSpinner.setSpinnerListItems(allList);
                searchableSpinner.show();
                searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                    @Override
                    public void setOnItemSelectListener(int position, String selectedString) {
                        sub_station_name_33.setText(selectedString);
                        selectedSSCode33 = selectedString.split("~")[1];
                    }
                });
            }
        });

        feeder_33kv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sub_station_name_33.getText().toString().isEmpty()) {
                    Toast.makeText(PmiHome.this, "Please Select EHT SS", Toast.LENGTH_SHORT).show();
                    return;
                }
                ArrayList<String> feederList = new ArrayList<>();
                for (int i = 0; i < feederMaster33.size(); i++) {
                    if (selectedSSCode33.equalsIgnoreCase(feederMaster33.get(i).getEHT_SS_CODE())) {
                        feederList.add(feederMaster33.get(i).getFEEDER_NAME_33KV() + "~" + feederMaster33.get(i).getFEEDER_CODE_33KV());
                    }
                }

                SearchableSpinner searchableSpinner = new SearchableSpinner(PmiHome.this);
                searchableSpinner.setWindowTitle("SELECT FEEDER");
                searchableSpinner.setSpinnerListItems(feederList);
                searchableSpinner.show();
                searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                    @Override
                    public void setOnItemSelectListener(int position, String selectedString) {
                        feeder_33kv.setText(selectedString);
                        selectedFeederName33 = selectedString.split("~")[0];
                        selectedFeederId33 = selectedString.split("~")[1];
                    }
                });
            }
        });

        btn_next_33.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sp_ssname.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    Toast.makeText(PmiHome.this, "Please Select EHT SS", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (sp_feedername.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    Toast.makeText(PmiHome.this, "Please Select 33KV Feeder", Toast.LENGTH_SHORT).show();
                    return;
                }

                get33KVPolesData();
            }
        });

        sub_station_name_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open11KVSSNearByDialog(sub_station_name_11);

                ArrayList<String> allList = new ArrayList<>();
                for (int i = 0; i < ssMaster11.size(); i++) {
                    allList.add(ssMaster11.get(i).getSUB_STATION_NAME() + "~" + ssMaster11.get(i).getSUB_STATION_ID());
                }
                SearchableSpinner searchableSpinner = new SearchableSpinner(PmiHome.this);
                searchableSpinner.setWindowTitle("SELECT 33/11KV SS");
                searchableSpinner.setSpinnerListItems(allList);
                searchableSpinner.show();
                searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                    @Override
                    public void setOnItemSelectListener(int position, String selectedString) {
                        sub_station_name_11.setText(selectedString);
                        selectedSSCode11 = selectedString.split("~")[1];
                    }
                });

            }
        });

        feeder_11kv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sub_station_name_11.getText().toString().isEmpty()) {
                    Toast.makeText(PmiHome.this, "Please Select 33/11KV SS", Toast.LENGTH_SHORT).show();
                    return;
                }
                ArrayList<String> feederList = new ArrayList<>();
                for (int i = 0; i < feederMaster11.size(); i++) {
                    if (selectedSSCode11.equalsIgnoreCase(feederMaster11.get(i).getSUB_STATION_ID())) {
                        feederList.add(feederMaster11.get(i).getFEEDER_NAME() + "~" + feederMaster11.get(i).getFEEDER_ID());
                    }
                }

                SearchableSpinner searchableSpinner = new SearchableSpinner(PmiHome.this);
                searchableSpinner.setWindowTitle("SELECT FEEDER");
                searchableSpinner.setSpinnerListItems(feederList);
                searchableSpinner.show();
                searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                    @Override
                    public void setOnItemSelectListener(int position, String selectedString) {
                        feeder_11kv.setText(selectedString);
                        selectedFeederName11 = selectedString.split("~")[0];
                        selectedFeederId11 = selectedString.split("~")[1];
                    }
                });
            }
        });

        btn_next_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sp_ss_name_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    Toast.makeText(PmiHome.this, "Please Select 33/11KV SS", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (sp_feeder_name_11.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    Toast.makeText(PmiHome.this, "Please Select 11KV Feeder", Toast.LENGTH_SHORT).show();
                    return;
                }

                get11KVPolesData();
            }
        });

        dtr_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<String> dtrList = new ArrayList<>();
                for (int i = 0; i < dtrMaster.size(); i++) {
                    dtrList.add(dtrMaster.get(i).getDTR_LOCATION() + "~" + dtrMaster.get(i).getDTR_STRUCTURE_CODE());
                }

                SearchableSpinner searchableSpinner = new SearchableSpinner(PmiHome.this);
                searchableSpinner.setWindowTitle("SELECT DTR");
                searchableSpinner.setSpinnerListItems(dtrList);
                searchableSpinner.show();
                searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                    @Override
                    public void setOnItemSelectListener(int position, String selectedString) {

                        int no_of_ckt = 0;
                        boolean isMapped = false;
                        List<String> cktList = new ArrayList<>();
                        for (int i = 0; i < dtrMaster.size(); i++) {
                            if (selectedString.split("~")[1].equalsIgnoreCase(dtrMaster.get(i).getDTR_STRUCTURE_CODE())) {

                                isMapped = dtrMaster.get(i).getMAPPED().equalsIgnoreCase("YES");


                                no_of_ckt = Integer.parseInt(dtrMaster.get(i).getNO_OF_LT_CKTS());
                            }
                        }

                        cktList.clear();
                        cktList.add("Select");
                        if (!isMapped) {
                            ArrayAdapter<String> adapter_pst = new ArrayAdapter<String>(PmiHome.this, android.R.layout.simple_dropdown_item_1line, cktList);
                            sp_circuit.setAdapter(adapter_pst);
                            sp_circuit.setSelection(0, true);
                            Toast.makeText(PmiHome.this, "This DTR is Not Mapped yet. Please complete DTR Survey first..", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        dtr_code.setText(selectedString);
                        selectedDtrCode = selectedString.split("~")[1];
                        if (no_of_ckt == 1) {
                            cktList.add(selectedDtrCode + "_LTCKT_A");
                        }
                        if (no_of_ckt == 2) {
                            cktList.add(selectedDtrCode + "_LTCKT_A");
                            cktList.add(selectedDtrCode + "_LTCKT_B");
                        }
                        if (no_of_ckt == 3) {
                            cktList.add(selectedDtrCode + "_LTCKT_A");
                            cktList.add(selectedDtrCode + "_LTCKT_B");
                            cktList.add(selectedDtrCode + "_LTCKT_C");
                        }
                        if (no_of_ckt == 4) {
                            cktList.add(selectedDtrCode + "_LTCKT_A");
                            cktList.add(selectedDtrCode + "_LTCKT_B");
                            cktList.add(selectedDtrCode + "_LTCKT_C");
                            cktList.add(selectedDtrCode + "_LTCKT_D");
                        }

                        ArrayAdapter<String> adapter_pst = new ArrayAdapter<String>(PmiHome.this, android.R.layout.simple_dropdown_item_1line, cktList);
                        sp_circuit.setAdapter(adapter_pst);
                        sp_circuit.setSelection(0, true);
                    }
                });
            }
        });

        btn_next_lt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dtr_code.getText().toString().isEmpty()) {
                    Toast.makeText(PmiHome.this, "Please Select DTR Structure Code", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (sp_circuit.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    Toast.makeText(PmiHome.this, "Please Select LT Circuit Code", Toast.LENGTH_SHORT).show();
                    return;
                }

                getLTPolesData();
            }
        });

        btn_transmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (postingList.isEmpty()) {
                    Toast.makeText(PmiHome.this, "There are No Poles for transmit", Toast.LENGTH_SHORT).show();
                    return;
                }
                transmitPoles();
            }
        });


        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        profile_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }


    private void loadDefectsMaster() {
        allDefectsList.clear();
        defectsTypeList.clear();
        defectRVList.clear();
        retrofitServicePmi = RetrofitClientInstancePMI.getRetrofitInstance().create(RetrofitServicePMI.class);
        Map<String, String> map = new HashMap<>();
        map.put("VERSION", getResources().getString(R.string.displayVersion));

        Gson gson = new Gson();
        JsonObject jsonObject = gson.toJsonTree(map).getAsJsonObject();
        Call<JsonObject> responseVoCall = retrofitServicePmi.getDefectMaster(jsonObject);


        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    // Log.v("FEEDER==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {
                            JsonArray array33 = surveyedResponseVo.getAsJsonArray("DATA");
                            List<String> list = new ArrayList<>();
                            for (int i = 0; i < array33.size(); i++) {
                                DefectsModel model = new DefectsModel();
                                model.setDEFECT_ID(array33.get(i).getAsJsonObject().get("DEFECT_ID").isJsonNull() ? "" : "" + array33.get(i).getAsJsonObject().get("DEFECT_ID").getAsInt());
                                model.setDEFECT_TYPE(array33.get(i).getAsJsonObject().get("DEFECT_TYPE").isJsonNull() ? "" : array33.get(i).getAsJsonObject().get("DEFECT_TYPE").getAsString());
                                model.setDEFECT_SUB_TYPE(array33.get(i).getAsJsonObject().get("DEFECT_SUB_TYPE").isJsonNull() ? "" : array33.get(i).getAsJsonObject().get("DEFECT_SUB_TYPE").getAsString());
                                model.setDEFECT_SUB_TYPE_ID(array33.get(i).getAsJsonObject().get("DEFECT_SUB_TYPE_ID").isJsonNull() ? "" : array33.get(i).getAsJsonObject().get("DEFECT_SUB_TYPE_ID").getAsString());
                                model.setVOLTAGE_LEVEL(array33.get(i).getAsJsonObject().get("VOLTAGE_LEVEL").isJsonNull() ? "" : array33.get(i).getAsJsonObject().get("VOLTAGE_LEVEL").getAsString());
                                list.add(array33.get(i).getAsJsonObject().get("DEFECT_TYPE").isJsonNull() ? "" : array33.get(i).getAsJsonObject().get("DEFECT_TYPE").getAsString());
                                allDefectsList.add(model);
                            }


                            defectsTypeList = new ArrayList<>(new LinkedHashSet<>(list));
                            System.out.println("DEFECT TYPES : " + defectsTypeList);
                            for (int i = 0; i < defectsTypeList.size(); i++) {
                                DefectRVModel rvModel = new DefectRVModel();
                                rvModel.setDefect(defectsTypeList.get(i));
                                rvModel.setDefectSelected(false);
                                rvModel.setSelectedRadioButtonId(-1);
                                ArrayList<DefectsModel> listDefectsModels = new ArrayList<>();
                                for (int j = 0; j < allDefectsList.size(); j++) {
                                    if (defectsTypeList.get(i).equalsIgnoreCase(allDefectsList.get(j).getDEFECT_TYPE())) {
                                        DefectsModel defectsModel = new DefectsModel();
                                        defectsModel.setDEFECT_ID(allDefectsList.get(j).getDEFECT_ID());
                                        defectsModel.setDEFECT_TYPE(allDefectsList.get(j).getDEFECT_TYPE());
                                        defectsModel.setDEFECT_SUB_TYPE(allDefectsList.get(j).getDEFECT_SUB_TYPE());
                                        defectsModel.setDEFECT_SUB_TYPE_ID(allDefectsList.get(j).getDEFECT_SUB_TYPE_ID());
                                        defectsModel.setVOLTAGE_LEVEL(allDefectsList.get(j).getVOLTAGE_LEVEL());
                                        defectsModel.setImagePath("");
                                        defectsModel.setImage("N");
                                        defectsModel.setImageSelected(false);
                                        defectsModel.setSelected(false);
                                        listDefectsModels.add(defectsModel);
                                    }
                                }
                                rvModel.setDefectsModelArrayList(listDefectsModels);
                                defectRVList.add(rvModel);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {

                        Toast.makeText(PmiHome.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {

                    Toast.makeText(PmiHome.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                Toast.makeText(PmiHome.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void transmitPoles() {

        btn_transmit.setVisibility(View.GONE);
        loader_transmit.setVisibility(View.VISIBLE);
        List<LoginDataResponse> loginDataResponses = arcGISDatabase.getUserMasterData();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String userId = preferences.getString(MobileConstants.USERNAME, "");
        JsonArray array = new JsonArray();
        for (int i = 0; i < postingList.size(); i++) {
            JsonObject requestObject = new JsonObject();
            requestObject.addProperty("APP_VERSION", MobileConstants.PMI_VERSION);
            requestObject.addProperty("ID", postingList.get(i).getID());
            requestObject.addProperty("DEFECT_ID", postingList.get(i).getDEFECT_ID());
            requestObject.addProperty("POLE_ID", postingList.get(i).getPOLE_ID());
            requestObject.addProperty("POLE_NO", postingList.get(i).getPOLE_NO());
            requestObject.addProperty("VOLTAGE_LEVEL", postingList.get(i).getVOLTAGE_LEVEL());
            requestObject.addProperty("DTR_CODE", postingList.get(i).getDTR_CODE());
            requestObject.addProperty("LT_CKT_CODE", postingList.get(i).getLT_CKT_CODE());
            requestObject.addProperty("FEEDER_ID", postingList.get(i).getFEEDER_ID());
            requestObject.addProperty("DEFECT_TYPE", postingList.get(i).getDEFECT_TYPE());
            requestObject.addProperty("DEFECT_SUB_TYPE", postingList.get(i).getDEFECT_SUB_TYPE());
            requestObject.addProperty("DEFECT_SUB_TYPE_ID", postingList.get(i).getDEFECT_SUB_TYPE_ID());
            //  requestObject.addProperty("IMAGE", postingList.get(i).getIMAGE());
            // Log.v("PMI DATA IMAGE", postingList.get(i).getIMAGE());
            requestObject.addProperty("IMAGE", ConvertImage.convertBase64(postingList.get(i).getIMAGE()));
            requestObject.addProperty("RECT_REMARKS", postingList.get(i).getRECT_REMARKS());
            requestObject.addProperty("USERID", userId);
            requestObject.addProperty("USERNAME", loginDataResponses.get(0).getROLE());
            array.add(requestObject);
        }

        Gson gson = new Gson();
        String json = gson.toJson(array);
        Log.v("PMI DATA===>>", "" + json);


        retrofitServicePmi = RetrofitClientInstancePMI.getRetrofitInstance().create(RetrofitServicePMI.class);
        Call<JsonObject> responseVoCall = retrofitServicePmi.transmitData(array);


        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    // Log.v("FEEDER==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {
                            loader_transmit.setVisibility(View.GONE);
                            postingList.clear();
                            pmiDatabase.deletePmiPostingData();
                            setTransmitData();

                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(PmiHome.this);
                            builder.setTitle("SUCCESSFULLY TRANSMITTED!!");
                            builder.setMessage("Data Transmitted Successfully!!");
                            builder.setCancelable(false);
                            builder.setPositiveButton("OKAY", (dialog, which) -> {
                                dialog.dismiss();
                                Intent intent = new Intent(PmiHome.this, DashBoard.class);
                                startActivity(intent);
                                finish();

                            });

                            androidx.appcompat.app.AlertDialog dialog = builder.create();
                            dialog.show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        btn_transmit.setVisibility(View.VISIBLE);
                        loader_transmit.setVisibility(View.GONE);
                        Toast.makeText(PmiHome.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    btn_transmit.setVisibility(View.VISIBLE);
                    loader_transmit.setVisibility(View.GONE);
                    Toast.makeText(PmiHome.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                btn_transmit.setVisibility(View.VISIBLE);
                loader_transmit.setVisibility(View.GONE);
                Toast.makeText(PmiHome.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setTransmitData() {
        if (!postingList.isEmpty()) {
            int poles_count_33 = 0;
            int poles_count_11 = 0;
            int poles_count_lt = 0;

            for (int i = 0; i < postingList.size(); i++) {
                if (postingList.get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("33KV")) {
                    poles_count_33++;
                }
                if (postingList.get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("11KV")) {
                    poles_count_11++;
                }
                if (postingList.get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("LT")) {
                    poles_count_lt++;
                }
            }

            poles_33kv.setText("" + poles_count_33);
            poles_11kv.setText("" + poles_count_11);
            poles_lt.setText("" + poles_count_lt);
        }
    }

    private void get33KVPolesData() {
        polesList33.clear();
        btn_next_33.setVisibility(View.GONE);
        loader_33.setVisibility(View.VISIBLE);
        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("FEEDER_ID", selectedFeederId33);
        requestObject.addProperty("VERSION", getResources().getString(R.string.displayVersion));
        retrofitServicePmi = RetrofitClientInstancePMI.getRetrofitInstance().create(RetrofitServicePMI.class);
        Call<JsonObject> responseVoCall = retrofitServicePmi.getPoles33(requestObject);


        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    // Log.v("FEEDER==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {
                            JsonArray array33 = surveyedResponseVo.getAsJsonArray("RESPONSE");
                            for (int i = 0; i < array33.size(); i++) {
                                PoleModel33KV model = new PoleModel33KV();
                                model.setFEEDER_ID(array33.get(i).getAsJsonObject().get("FEEDER_ID").isJsonNull() ? "" : array33.get(i).getAsJsonObject().get("FEEDER_ID").getAsString());
                                model.setGLAT(array33.get(i).getAsJsonObject().get("GLAT").isJsonNull() ? "" : array33.get(i).getAsJsonObject().get("GLAT").getAsString());
                                model.setPOLE_ID(array33.get(i).getAsJsonObject().get("REF_ID").isJsonNull() ? "" : array33.get(i).getAsJsonObject().get("REF_ID").getAsString());
                                model.setPOLE_NO(array33.get(i).getAsJsonObject().get("POLE_NO").isJsonNull() ? "" : array33.get(i).getAsJsonObject().get("POLE_NO").getAsString());
                                model.setGLONG(array33.get(i).getAsJsonObject().get("GLONG").isJsonNull() ? "" : array33.get(i).getAsJsonObject().get("GLONG").getAsString());
                                model.setPOLE_CONFIG(array33.get(i).getAsJsonObject().get("POLE_CONFIG").isJsonNull() ? "" : array33.get(i).getAsJsonObject().get("POLE_CONFIG").getAsString());
                                model.setPOLE_HEIGHT(array33.get(i).getAsJsonObject().get("POLE_HEIGHT").isJsonNull() ? "" : array33.get(i).getAsJsonObject().get("POLE_HEIGHT").getAsString());
                                model.setPOLE_SUPPORT(array33.get(i).getAsJsonObject().get("POLE_SUPPORT").isJsonNull() ? "" : array33.get(i).getAsJsonObject().get("POLE_SUPPORT").getAsString());
                                model.setPOLE_TYPE(array33.get(i).getAsJsonObject().get("POLE_TYPE").isJsonNull() ? "" : array33.get(i).getAsJsonObject().get("POLE_TYPE").getAsString());
                                model.setPOLE_STRUCTURE(array33.get(i).getAsJsonObject().get("POLE_STRUCTURE").isJsonNull() ? "" : array33.get(i).getAsJsonObject().get("POLE_STRUCTURE").getAsString());
                                polesList33.add(model);
                            }
                            loader_33.setVisibility(View.GONE);
                            SELECTED_VOLTAGE = "33KV";
                            Intent intent = new Intent(PmiHome.this, PmiMap.class);
                            intent.putExtra("ID", selectedFeederId33);
                            intent.putExtra("NAME", selectedFeederName33);
                            startActivity(intent);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        btn_next_33.setVisibility(View.VISIBLE);
                        loader_33.setVisibility(View.GONE);
                        Toast.makeText(PmiHome.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    btn_next_33.setVisibility(View.VISIBLE);
                    loader_33.setVisibility(View.GONE);
                    Toast.makeText(PmiHome.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                btn_next_33.setVisibility(View.VISIBLE);
                loader_33.setVisibility(View.GONE);
                Toast.makeText(PmiHome.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void get11KVPolesData() {
        polesList11.clear();
        btn_next_11.setVisibility(View.GONE);
        loader_11.setVisibility(View.VISIBLE);
        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("FEEDER_ID", selectedFeederId11);
        requestObject.addProperty("VERSION", getResources().getString(R.string.displayVersion));
        retrofitServicePmi = RetrofitClientInstancePMI.getRetrofitInstance().create(RetrofitServicePMI.class);
        Call<JsonObject> responseVoCall = retrofitServicePmi.getPoles11(requestObject);


        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    // Log.v("FEEDER==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {
                            JsonArray array = surveyedResponseVo.getAsJsonArray("RESPONSE");
                            for (int i = 0; i < array.size(); i++) {
                                PoleModel11KV model = new PoleModel11KV();
                                model.setFEEDER_ID(array.get(i).getAsJsonObject().get("FEEDER_ID").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("FEEDER_ID").getAsString());
                                model.setGLAT(array.get(i).getAsJsonObject().get("GLAT").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("GLAT").getAsString());
                                model.setPOLE_ID(array.get(i).getAsJsonObject().get("REF_ID").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("REF_ID").getAsString());
                                model.setPOLE_NO(array.get(i).getAsJsonObject().get("POLE_NO").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("POLE_NO").getAsString());
                                model.setGLONG(array.get(i).getAsJsonObject().get("GLONG").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("GLONG").getAsString());
                                model.setPOLE_CONFIG(array.get(i).getAsJsonObject().get("POLE_CONFIG").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("POLE_CONFIG").getAsString());
                                model.setPOLE_HEIGHT(array.get(i).getAsJsonObject().get("POLE_HEIGHT").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("POLE_HEIGHT").getAsString());
                                model.setPOLE_SUPPORT(array.get(i).getAsJsonObject().get("POLE_SUPPORT").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("POLE_SUPPORT").getAsString());
                                model.setPOLE_TYPE(array.get(i).getAsJsonObject().get("POLE_TYPE").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("POLE_TYPE").getAsString());
                                model.setPOLE_STRUCTURE(array.get(i).getAsJsonObject().get("POLE_STRUCTURE").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("POLE_STRUCTURE").getAsString());
                                polesList11.add(model);
                            }

                            SELECTED_VOLTAGE = "11KV";
                            Intent intent = new Intent(PmiHome.this, PmiMap.class);
                            intent.putExtra("ID", selectedFeederId11);
                            intent.putExtra("NAME", selectedFeederName11);
                            startActivity(intent);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        btn_next_11.setVisibility(View.VISIBLE);
                        loader_11.setVisibility(View.GONE);
                        Toast.makeText(PmiHome.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    btn_next_11.setVisibility(View.VISIBLE);
                    loader_11.setVisibility(View.GONE);
                    Toast.makeText(PmiHome.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                btn_next_11.setVisibility(View.VISIBLE);
                loader_11.setVisibility(View.GONE);
                Toast.makeText(PmiHome.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


    private void getLTPolesData() {
        polesListLT.clear();
        btn_next_lt.setVisibility(View.GONE);
        loader_lt.setVisibility(View.VISIBLE);
        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("DTR_CODE", selectedDtrCode);
        requestObject.addProperty("LT_CKT_CODE", sp_circuit.getSelectedItem().toString());
        requestObject.addProperty("VERSION", getResources().getString(R.string.displayVersion));
        retrofitServicePmi = RetrofitClientInstancePMI.getRetrofitInstance().create(RetrofitServicePMI.class);
        Call<JsonObject> responseVoCall = retrofitServicePmi.getPolesLT(requestObject);


        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    // Log.v("FEEDER==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {
                            JsonArray array = surveyedResponseVo.getAsJsonArray("RESPONSE");
                            for (int i = 0; i < array.size(); i++) {
                                PoleModelLT model = new PoleModelLT();
                                model.setDTR_CODE(selectedDtrCode);
                                model.setLT_CKT_CODE(array.get(i).getAsJsonObject().get("LT_CKT_CODE").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("LT_CKT_CODE").getAsString());
                                model.setGLAT(array.get(i).getAsJsonObject().get("GLAT").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("GLAT").getAsString());
                                model.setPOLE_ID(array.get(i).getAsJsonObject().get("REF_ID").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("REF_ID").getAsString());
                                model.setPOLE_NO(array.get(i).getAsJsonObject().get("POLE_NO").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("POLE_NO").getAsString());
                                model.setGLONG(array.get(i).getAsJsonObject().get("GLONG").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("GLONG").getAsString());
                                model.setPOLE_CONFIG(array.get(i).getAsJsonObject().get("LINE_CONFIG").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("LINE_CONFIG").getAsString());
                                model.setPOLE_HEIGHT(array.get(i).getAsJsonObject().get("POLE_HEIGHT").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("POLE_HEIGHT").getAsString());
                                model.setPOLE_SUPPORT(array.get(i).getAsJsonObject().get("POLE_SUPPORT").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("POLE_SUPPORT").getAsString());
                                model.setPOLE_TYPE(array.get(i).getAsJsonObject().get("POLE_TYPE").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("POLE_TYPE").getAsString());
                                //model.setPOLE_STRUCTURE(array.get(i).getAsJsonObject().get("POLE_STRUCTURE").isJsonNull() ? "" : array.get(i).getAsJsonObject().get("POLE_STRUCTURE").getAsString());
                                polesListLT.add(model);
                            }

                            Intent intent = new Intent(PmiHome.this, PmiMap.class);
                            intent.putExtra("ID", selectedDtrCode);
                            intent.putExtra("NAME", sp_circuit.getSelectedItem().toString());
                            startActivity(intent);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        btn_next_lt.setVisibility(View.VISIBLE);
                        loader_lt.setVisibility(View.GONE);
                        Toast.makeText(PmiHome.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    btn_next_lt.setVisibility(View.VISIBLE);
                    loader_lt.setVisibility(View.GONE);
                    Toast.makeText(PmiHome.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                btn_next_lt.setVisibility(View.VISIBLE);
                loader_lt.setVisibility(View.GONE);
                Toast.makeText(PmiHome.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private double getDistanceBetweenPoles33(String polLat, String polLon, double fromLat, double fromLon) {
        System.out.println("CURRENT LOCATION LAT : " + fromLat);
        System.out.println("CURRENT LOCATION LON : " + fromLon);
        System.out.println("POLE LOCATION LAT : " + polLat);
        System.out.println("POLE LOCATION LON : " + polLon);
        Location currentStartPoint = new Location("locationM");
        currentStartPoint.setLatitude(fromLon);
        currentStartPoint.setLongitude(fromLat);

        Location currentEndPoint = new Location("locationM");
        currentEndPoint.setLatitude(Double.parseDouble(polLon));
        currentEndPoint.setLongitude(Double.parseDouble(polLat));
        double currentDistance = currentStartPoint.distanceTo(currentEndPoint);
        System.out.println("POLE LOCATION DISTANCE : " + currentDistance);
        return currentDistance;
    }

    private double getDistanceBetweenPoles11(String polLat, String polLon, double fromLat, double fromLon) {
        System.out.println("CURRENT LOCATION LAT : " + fromLat);
        System.out.println("CURRENT LOCATION LON : " + fromLon);
        System.out.println("POLE LOCATION LAT : " + polLat);
        System.out.println("POLE LOCATION LON : " + polLon);
        Location currentStartPoint = new Location("locationN");
        currentStartPoint.setLatitude(fromLon);
        currentStartPoint.setLongitude(fromLat);

        Location currentEndPoint = new Location("locationN");
        currentEndPoint.setLatitude(Double.parseDouble(polLon));
        currentEndPoint.setLongitude(Double.parseDouble(polLat));
        double currentDistance = currentStartPoint.distanceTo(currentEndPoint);
        System.out.println("POLE LOCATION DISTANCE : " + currentDistance);
        return currentDistance;
    }

    private void getLatLongs() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            currentLat = location.getLatitude();
                            currentLog = location.getLongitude();
                            //  getFeederMasterAPI(currentLat, currentLog);
                            // Log.d("GETLOCATION------>", "onSuccess: " + "LAT" + currentLat + "LNG" + currentLog);

                            // Proceed with your logic, e.g., navigate to the next screen with lat/long
                            //navigateNextScreen(model, latitude, longitude);
                        } else {
                            // getFeederMasterAPI(0.0, 0.0);
                            Toast.makeText(PmiHome.this, "Unable to retrieve location.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void open33KVSSNearByDialog(TextView edt_text) {
        Dialog payDialog = new Dialog(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);


        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        payDialog.setContentView(R.layout.feeder_master_dialog_estimates);
        payDialog.getWindow().setLayout(width, height / 2);
        payDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        payDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        TextView cancel_button = payDialog.findViewById(R.id.dialog_cancel);
        TextView back_to_home = payDialog.findViewById(R.id.back_to_home);
        TextView title = payDialog.findViewById(R.id.title);
        title.setText("SELECT EHT SS !!");

        RadioGroup radioGroup = payDialog.findViewById(R.id.radioGroup);

        ArrayList<String> nearList = new ArrayList<>();
        ArrayList<String> allList = new ArrayList<>();

        for (int i = 0; i < ssMasters33.size(); i++) {
            allList.add(ssMasters33.get(i).getEHT_SS_NAME() + "~" + ssMasters33.get(i).getEHT_SS_CODE());
        }

        for (int i = 0; i < ssMasters33.size(); i++) {
            double distance = getDistanceBetweenPoles33(ssMasters33.get(i).getLATITUDE(), ssMasters33.get(i).getLONGITUDE(), currentLat, currentLog);
            if (distance <= 20000) {
                nearList.add(ssMasters33.get(i).getEHT_SS_NAME() + "~" + ssMasters33.get(i).getEHT_SS_CODE());
            }
        }


        payDialog.show();

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = payDialog.findViewById(selectedId);
                String selectedRadioButton = radioButton.getText().toString();
                payDialog.dismiss();
                if (selectedRadioButton.equals(" Nearby EHT SS")) {
                    SearchableSpinner searchableSpinner = new SearchableSpinner(PmiHome.this);
                    searchableSpinner.setWindowTitle("SELECT EHT SS");
                    searchableSpinner.setSpinnerListItems(nearList);
                    searchableSpinner.show();
                    searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                        @Override
                        public void setOnItemSelectListener(int position, String selectedString) {
                            edt_text.setText(selectedString);
                            selectedSSCode33 = selectedString.split("~")[1];
                        }
                    });

                } else {
                    SearchableSpinner searchableSpinner = new SearchableSpinner(PmiHome.this);
                    searchableSpinner.setWindowTitle("SELECT EHT SS");
                    searchableSpinner.setSpinnerListItems(allList);
                    searchableSpinner.show();
                    searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                        @Override
                        public void setOnItemSelectListener(int position, String selectedString) {
                            edt_text.setText(selectedString);
                            selectedSSCode33 = selectedString.split("~")[1];
                        }
                    });
                }
            }
        });
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payDialog.dismiss();
            }
        });


    }

    private void open11KVSSNearByDialog(TextView edt_text) {
        Dialog payDialog = new Dialog(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);


        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        payDialog.setContentView(R.layout.feeder_master_dialog_estimates_11kv);
        payDialog.getWindow().setLayout(width, height / 2);
        payDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        payDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        TextView cancel_button = payDialog.findViewById(R.id.dialog_cancel);
        TextView back_to_home = payDialog.findViewById(R.id.back_to_home);
        TextView title = payDialog.findViewById(R.id.title);
        title.setText("SELECT 33/11KV SS !!");

        RadioGroup radioGroup = payDialog.findViewById(R.id.radioGroup);

        ArrayList<String> nearList = new ArrayList<>();
        ArrayList<String> allList = new ArrayList<>();

        for (int i = 0; i < ssMaster11.size(); i++) {
            allList.add(ssMaster11.get(i).getSUB_STATION_NAME() + "~" + ssMaster11.get(i).getSUB_STATION_ID());
        }

        for (int i = 0; i < ssMaster11.size(); i++) {
            double distance = getDistanceBetweenPoles11(ssMaster11.get(i).getLATITUDE(), ssMaster11.get(i).getLONGITUDE(), currentLat, currentLog);
            if (distance <= 20000) {
                nearList.add(ssMaster11.get(i).getSUB_STATION_NAME() + "~" + ssMaster11.get(i).getSUB_STATION_ID());
            }
        }


        payDialog.show();

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = payDialog.findViewById(selectedId);
                String selectedRadioButton = radioButton.getText().toString();
                payDialog.dismiss();
                if (selectedRadioButton.equals(" Nearby 33/11KV SS")) {
                    SearchableSpinner searchableSpinner = new SearchableSpinner(PmiHome.this);
                    searchableSpinner.setWindowTitle("SELECT 33/11KV SS");
                    searchableSpinner.setSpinnerListItems(nearList);
                    searchableSpinner.show();
                    searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                        @Override
                        public void setOnItemSelectListener(int position, String selectedString) {
                            edt_text.setText(selectedString);
                            selectedSSCode11 = selectedString.split("~")[1];
                        }
                    });

                } else {
                    SearchableSpinner searchableSpinner = new SearchableSpinner(PmiHome.this);
                    searchableSpinner.setWindowTitle("SELECT 33/11KV SS");
                    searchableSpinner.setSpinnerListItems(allList);
                    searchableSpinner.show();
                    searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                        @Override
                        public void setOnItemSelectListener(int position, String selectedString) {
                            edt_text.setText(selectedString);
                            selectedSSCode11 = selectedString.split("~")[1];
                        }
                    });
                }
            }
        });
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payDialog.dismiss();
            }
        });


    }

    private void loadFeederMasterData() {
        loader.setVisibility(View.VISIBLE);
        List<LoginDataResponse> loginDataResponses = arcGISDatabase.getUserMasterData();
        JsonObject dtrDetailsReqestVo = new JsonObject();
        dtrDetailsReqestVo.addProperty("OFFICE_ID", loginDataResponses.get(0).getSEC_CODE());

        /*retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        Call<JsonObject> responseVoCall = retrofitService.getFeederMasterNew(dtrDetailsReqestVo);*/


        retrofitServicePmi = RetrofitClientInstancePMI.getRetrofitInstance().create(RetrofitServicePMI.class);
        Call<JsonObject> responseVoCall = retrofitServicePmi.getFeederMasterNew(dtrDetailsReqestVo);

        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    Log.v("FEEDER==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {
                            loader.setVisibility(View.GONE);
                            main_layout.setVisibility(View.VISIBLE);
                            transmit_layout.setVisibility(View.VISIBLE);
                            JsonObject dataObject = surveyedResponseVo.get("DATA").getAsJsonObject();

                            JsonArray array33 = dataObject.getAsJsonArray("EHT_SS_MASTER");
                            for (int i = 0; i < array33.size(); i++) {
                                EHT_SS_MASTER model = new EHT_SS_MASTER();
                                model.setEHT_SS_CODE(array33.get(i).getAsJsonObject().get("EHT_SS_CODE").getAsString());
                                model.setEHT_SS_NAME(array33.get(i).getAsJsonObject().get("EHT_SS_NAME").getAsString());
                                model.setLONGITUDE(array33.get(i).getAsJsonObject().get("LONGITUDE").getAsString());
                                model.setLATITUDE(array33.get(i).getAsJsonObject().get("LATITUDE").getAsString());
                                ssMasters33.add(model);
                            }

                            JsonArray arrayFeeder33 = dataObject.getAsJsonArray("33KV_FEEDER_MASTER");
                            for (int i = 0; i < arrayFeeder33.size(); i++) {
                                FEEDER_MASTER_33KV model = new FEEDER_MASTER_33KV();
                                model.setEHT_SS_CODE(arrayFeeder33.get(i).getAsJsonObject().get("EHT_SS_CODE").getAsString());
                                model.setEHT_SS_NAME(arrayFeeder33.get(i).getAsJsonObject().get("EHT_SS_NAME").getAsString());
                                model.setFEEDER_CODE_33KV(arrayFeeder33.get(i).getAsJsonObject().get("FEEDER_CODE_33KV").getAsString());
                                model.setFEEDER_NAME_33KV(arrayFeeder33.get(i).getAsJsonObject().get("FEEDER_NAME_33KV").getAsString());
                                model.setDIVISION_ID(arrayFeeder33.get(i).getAsJsonObject().get("DIVISION_ID").getAsString());
                                model.setPOLES(arrayFeeder33.get(i).getAsJsonObject().get("POLES").getAsString());
                                feederMaster33.add(model);
                            }

                            JsonArray array11 = dataObject.getAsJsonArray("SS_MATSER");
                            for (int i = 0; i < array11.size(); i++) {
                                SS_MASTER_11KV model = new SS_MASTER_11KV();
                                model.setSEC_ID(array11.get(i).getAsJsonObject().get("SEC_ID").getAsString());
                                model.setSEC_NAME(array11.get(i).getAsJsonObject().get("SEC_NAME").getAsString());
                                model.setSUB_STATION_NAME(array11.get(i).getAsJsonObject().get("SUB_STATION_NAME").getAsString());
                                model.setSUB_STATION_ID(array11.get(i).getAsJsonObject().get("SUB_STATION_ID").getAsString());
                                model.setLONGITUDE(array11.get(i).getAsJsonObject().get("LONGITUDE").getAsString());
                                model.setLATITUDE(array11.get(i).getAsJsonObject().get("LATITUDE").getAsString());
                                ssMaster11.add(model);
                            }

                            JsonArray arrayFeeder11 = dataObject.getAsJsonArray("11KV_FEEDER_MASTER");
                            for (int i = 0; i < arrayFeeder11.size(); i++) {
                                FEEDER_MASTER_11KV model = new FEEDER_MASTER_11KV();
                                model.setSEC_ID(arrayFeeder11.get(i).getAsJsonObject().get("SEC_ID").getAsString());
                                model.setSEC_NAME(arrayFeeder11.get(i).getAsJsonObject().get("SEC_NAME").getAsString());
                                model.setSUB_STATION_NAME(arrayFeeder11.get(i).getAsJsonObject().get("SUB_STATION_NAME").getAsString());
                                model.setSUB_STATION_ID(arrayFeeder11.get(i).getAsJsonObject().get("SUB_STATION_ID").getAsString());
                                model.setFEEDER_NAME(arrayFeeder11.get(i).getAsJsonObject().get("FEEDER_NAME").getAsString());
                                model.setFEEDER_ID(arrayFeeder11.get(i).getAsJsonObject().get("FEEDER_ID").getAsString());
                                model.setPOLES(arrayFeeder11.get(i).getAsJsonObject().get("POLES").getAsString());
                                feederMaster11.add(model);
                            }

                            JsonArray arrayLT = dataObject.getAsJsonArray("DTR");
                            for (int i = 0; i < arrayLT.size(); i++) {
                                DTR model = new DTR();
                                model.setSECTION_NAME(arrayLT.get(i).getAsJsonObject().get("SECTION_NAME").getAsString());
                                model.setDTR_STRUCTURE_CODE(arrayLT.get(i).getAsJsonObject().get("DTR_STRUCTURE_CODE").getAsString());
                                model.setNO_OF_LT_CKTS(arrayLT.get(i).getAsJsonObject().get("NO_OF_LT_CKTS").getAsString());
                                model.setDTR_LOCATION(arrayLT.get(i).getAsJsonObject().get("DTR_LOCATION").getAsString());
                                model.setFEEDER_NAME(arrayLT.get(i).getAsJsonObject().get("FEEDER_NAME").getAsString());
                                model.setFEEDER_ID(arrayLT.get(i).getAsJsonObject().get("FEEDER_ID").getAsString());
                                model.setSECTION_ID(arrayLT.get(i).getAsJsonObject().get("SECTION_ID").getAsString());
                                model.setMAPPED(arrayLT.get(i).getAsJsonObject().get("MAPPED").getAsString());
                                dtrMaster.add(model);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        loader.setVisibility(View.GONE);
                        transmit_layout.setVisibility(View.VISIBLE);
                        Toast.makeText(PmiHome.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    loader.setVisibility(View.GONE);
                    transmit_layout.setVisibility(View.VISIBLE);
                    Toast.makeText(PmiHome.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loader.setVisibility(View.GONE);
                transmit_layout.setVisibility(View.VISIBLE);
                Toast.makeText(PmiHome.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void loadFeederMasterDataForSingleUser() {
        loader.setVisibility(View.VISIBLE);
        List<LoginDataResponse> loginDataResponses = arcGISDatabase.getUserMasterData();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String userId = preferences.getString(MobileConstants.USERNAME, "");
        JsonObject dtrDetailsReqestVo = new JsonObject();
        dtrDetailsReqestVo.addProperty("OFFICE_ID", loginDataResponses.get(0).getSEC_CODE());
        dtrDetailsReqestVo.addProperty("USERID", userId);

        /*retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        Call<JsonObject> responseVoCall = retrofitService.getFeederMasterNew(dtrDetailsReqestVo);*/


        retrofitServicePmi = RetrofitClientInstancePMI.getRetrofitInstance().create(RetrofitServicePMI.class);
        Call<JsonObject> responseVoCall = retrofitServicePmi.userWiseDefectList(dtrDetailsReqestVo);

        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    Log.v("FEEDER==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {
                            loader.setVisibility(View.GONE);
                            main_layout.setVisibility(View.VISIBLE);
                            transmit_layout.setVisibility(View.VISIBLE);
                            JsonObject dataObject = surveyedResponseVo.get("DATA").getAsJsonObject();

                            JsonArray array33 = dataObject.getAsJsonArray("33KV_FEEDER_MASTER");
                            for (int i = 0; i < array33.size(); i++) {
                                EHT_SS_MASTER model = new EHT_SS_MASTER();
                                model.setEHT_SS_CODE(array33.get(i).getAsJsonObject().get("EHT_SS_CODE").getAsString());
                                model.setEHT_SS_NAME(array33.get(i).getAsJsonObject().get("EHT_SS_NAME").getAsString());
                                //  model.setLONGITUDE(array33.get(i).getAsJsonObject().get("LONGITUDE").getAsString());
                                // model.setLATITUDE(array33.get(i).getAsJsonObject().get("LATITUDE").getAsString());
                                ssMasters33.add(model);
                            }

                            JsonArray arrayFeeder33 = dataObject.getAsJsonArray("33KV_FEEDER_MASTER");
                            for (int i = 0; i < arrayFeeder33.size(); i++) {
                                FEEDER_MASTER_33KV model = new FEEDER_MASTER_33KV();
                                model.setEHT_SS_CODE(arrayFeeder33.get(i).getAsJsonObject().get("EHT_SS_CODE").getAsString());
                                model.setEHT_SS_NAME(arrayFeeder33.get(i).getAsJsonObject().get("EHT_SS_NAME").getAsString());
                                model.setFEEDER_CODE_33KV(arrayFeeder33.get(i).getAsJsonObject().get("FEEDER_CODE_33KV").getAsString());
                                model.setFEEDER_NAME_33KV(arrayFeeder33.get(i).getAsJsonObject().get("FEEDER_NAME_33KV").getAsString());
                                model.setDIVISION_ID(arrayFeeder33.get(i).getAsJsonObject().get("DIVISION_ID").getAsString());
                                model.setPOLES(arrayFeeder33.get(i).getAsJsonObject().get("POLES").getAsString());
                                feederMaster33.add(model);
                            }

                            JsonArray array11 = dataObject.getAsJsonArray("11KV_FEEDER_MASTER");
                            for (int i = 0; i < array11.size(); i++) {
                                SS_MASTER_11KV model = new SS_MASTER_11KV();
                                model.setSEC_ID(array11.get(i).getAsJsonObject().get("SEC_ID").getAsString());
                                model.setSEC_NAME(array11.get(i).getAsJsonObject().get("SEC_NAME").getAsString());
                                model.setSUB_STATION_NAME(array11.get(i).getAsJsonObject().get("SUB_STATION_NAME").getAsString());
                                model.setSUB_STATION_ID(array11.get(i).getAsJsonObject().get("SUB_STATION_ID").getAsString());
                                // model.setLONGITUDE(array11.get(i).getAsJsonObject().get("LONGITUDE").getAsString());
                                // model.setLATITUDE(array11.get(i).getAsJsonObject().get("LATITUDE").getAsString());
                                ssMaster11.add(model);
                            }

                            JsonArray arrayFeeder11 = dataObject.getAsJsonArray("11KV_FEEDER_MASTER");
                            for (int i = 0; i < arrayFeeder11.size(); i++) {
                                FEEDER_MASTER_11KV model = new FEEDER_MASTER_11KV();
                                model.setSEC_ID(arrayFeeder11.get(i).getAsJsonObject().get("SEC_ID").getAsString());
                                model.setSEC_NAME(arrayFeeder11.get(i).getAsJsonObject().get("SEC_NAME").getAsString());
                                model.setSUB_STATION_NAME(arrayFeeder11.get(i).getAsJsonObject().get("SUB_STATION_NAME").getAsString());
                                model.setSUB_STATION_ID(arrayFeeder11.get(i).getAsJsonObject().get("SUB_STATION_ID").getAsString());
                                model.setFEEDER_NAME(arrayFeeder11.get(i).getAsJsonObject().get("FEEDER_NAME").getAsString());
                                model.setFEEDER_ID(arrayFeeder11.get(i).getAsJsonObject().get("FEEDER_ID").getAsString());
                                model.setPOLES(arrayFeeder11.get(i).getAsJsonObject().get("POLES").getAsString());
                                feederMaster11.add(model);
                            }

                            if (ssMasters33.isEmpty()) {
                                main_layout_33.setVisibility(View.GONE);
                            } else {
                                main_layout_33.setVisibility(View.VISIBLE);
                                set33KVData();
                            }

                            if (ssMaster11.isEmpty()) {
                                main_layout_11.setVisibility(View.GONE);
                            } else {
                                // main_layout_11.setVisibility(View.VISIBLE);
                                // set11KVData();
                                set11KVRecyclerView();
                            }

                           /* JsonArray arrayLT = dataObject.getAsJsonArray("DTR");
                            for (int i = 0; i < arrayLT.size(); i++) {
                                DTR model = new DTR();
                                model.setSECTION_NAME(arrayLT.get(i).getAsJsonObject().get("SECTION_NAME").getAsString());
                                model.setDTR_STRUCTURE_CODE(arrayLT.get(i).getAsJsonObject().get("DTR_STRUCTURE_CODE").getAsString());
                                model.setNO_OF_LT_CKTS(arrayLT.get(i).getAsJsonObject().get("NO_OF_LT_CKTS").getAsString());
                                model.setDTR_LOCATION(arrayLT.get(i).getAsJsonObject().get("DTR_LOCATION").getAsString());
                                model.setFEEDER_NAME(arrayLT.get(i).getAsJsonObject().get("FEEDER_NAME").getAsString());
                                model.setFEEDER_ID(arrayLT.get(i).getAsJsonObject().get("FEEDER_ID").getAsString());
                                model.setSECTION_ID(arrayLT.get(i).getAsJsonObject().get("SECTION_ID").getAsString());
                                model.setMAPPED(arrayLT.get(i).getAsJsonObject().get("MAPPED").getAsString());
                                dtrMaster.add(model);
                            }*/

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        loader.setVisibility(View.GONE);
                        transmit_layout.setVisibility(View.VISIBLE);
                        Toast.makeText(PmiHome.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    loader.setVisibility(View.GONE);
                    transmit_layout.setVisibility(View.VISIBLE);
                    Toast.makeText(PmiHome.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loader.setVisibility(View.GONE);
                transmit_layout.setVisibility(View.VISIBLE);
                Toast.makeText(PmiHome.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void set11KVRecyclerView() {
        new_main_layout_11.setVisibility(View.VISIBLE);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(PmiHome.this);
        rv_11.setLayoutManager(layoutManager1);
        rv_11.setFocusable(false);
        RV11KVAdapter adapter1 = new RV11KVAdapter(feederMaster11, PmiHome.this, PmiHome.this);
        rv_11.setAdapter(adapter1);
        rv_11.setNestedScrollingEnabled(false);
    }

    private void set11KVData() {
        ArrayList<String> allList = new ArrayList<>();
        allList.add("Select");
        for (int i = 0; i < ssMaster11.size(); i++) {
            allList.add(ssMaster11.get(i).getSUB_STATION_NAME() + "~" + ssMaster11.get(i).getSUB_STATION_ID());
        }

        Set<String> set = new LinkedHashSet<>(allList);
        allList.clear();
        allList.addAll(set);

        ArrayAdapter<String> ehtSSadapter = new ArrayAdapter<String>(PmiHome.this,
                android.R.layout.simple_spinner_dropdown_item, allList);
        sp_ss_name_11.setAdapter(ehtSSadapter);

        sp_ss_name_11.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if (!"Select".equalsIgnoreCase(sp_ss_name_11.getSelectedItem().toString())) {
                    sub_station_name_11.setText(sp_ss_name_11.getSelectedItem().toString());
                    selectedSSCode11 = sp_ss_name_11.getSelectedItem().toString().split("~")[1];

                    ArrayList<String> feederList = new ArrayList<>();
                    feederList.add("Select");
                    for (int i = 0; i < feederMaster11.size(); i++) {
                        if (selectedSSCode11.equalsIgnoreCase(feederMaster11.get(i).getSUB_STATION_ID())) {
                            feederList.add(feederMaster11.get(i).getFEEDER_NAME() + "~" + feederMaster11.get(i).getFEEDER_ID());
                        }
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(PmiHome.this,
                            android.R.layout.simple_spinner_dropdown_item, feederList);
                    sp_feeder_name_11.setAdapter(adapter);


                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_feeder_name_11.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!"Select".equalsIgnoreCase(sp_feeder_name_11.getSelectedItem().toString())) {
                    selectedFeederName11 = sp_feeder_name_11.getSelectedItem().toString().split("~")[0];
                    selectedFeederId11 = sp_feeder_name_11.getSelectedItem().toString().split("~")[1];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void set33KVData() {
        ArrayList<String> allList = new ArrayList<>();
        allList.add("Select");
        for (int i = 0; i < ssMasters33.size(); i++) {
            allList.add(ssMasters33.get(i).getEHT_SS_NAME() + "~" + ssMasters33.get(i).getEHT_SS_CODE());
        }

        Set<String> set = new LinkedHashSet<>(allList);
        allList.clear();
        allList.addAll(set);

        ArrayAdapter<String> ehtSSadapter = new ArrayAdapter<String>(PmiHome.this,
                android.R.layout.simple_spinner_dropdown_item, allList);
        sp_ssname.setAdapter(ehtSSadapter);

        sp_ssname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (!"Select".equalsIgnoreCase(sp_ssname.getSelectedItem().toString())) {
                    sub_station_name_33.setText(sp_ssname.getSelectedItem().toString());
                    selectedSSCode33 = sp_ssname.getSelectedItem().toString().split("~")[1];

                    ArrayList<String> feederList = new ArrayList<>();
                    feederList.add("Select");
                    for (int i = 0; i < feederMaster33.size(); i++) {
                        if (selectedSSCode33.equalsIgnoreCase(feederMaster33.get(i).getEHT_SS_CODE())) {
                            feederList.add(feederMaster33.get(i).getFEEDER_NAME_33KV() + "~" + feederMaster33.get(i).getFEEDER_CODE_33KV());
                        }
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(PmiHome.this,
                            android.R.layout.simple_spinner_dropdown_item, feederList);
                    sp_feedername.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_feedername.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!"Select".equalsIgnoreCase(sp_feedername.getSelectedItem().toString())) {
                    selectedFeederName33 = sp_feedername.getSelectedItem().toString().split("~")[0];
                    selectedFeederId33 = sp_feedername.getSelectedItem().toString().split("~")[1];
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    @Override
    public void onItemClick(FEEDER_MASTER_11KV model) {
        selectedSSCode11 = model.getSUB_STATION_ID();
        selectedFeederId11 = model.getFEEDER_ID();
        selectedFeederName11 = model.getFEEDER_NAME();
        get11KVPolesData();
    }
}
