package com.apepdcl.gis_survey.module_11KV.edit_screens;

import static com.apepdcl.gis_survey.estimates.new_module.EstimatesMap.IS_LINE_EXTENSION_11_DATA_AVAILABLE;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.lt_edit_screens.EditPoleAttributesLtAsset;
import com.apepdcl.gis_survey.module_11KV.ArcGISDatabase;
import com.apepdcl.gis_survey.module_11KV.api_calls.RetrofitClientInstance;
import com.apepdcl.gis_survey.module_11KV.api_calls.RetrofitService;
import com.apepdcl.gis_survey.module_11KV.models.SurveyVo;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.vo.SurveyDataVo;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPoleAttributesAsset extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String FEEDER_ID = "", POLE_NO = "", POLE_TYPE = "", ID = "", FROM = "", POLE_HEIGHT = "",
            POLE_SUPPORT = "", POLE_CONCRETE = "", PIN_CUT = "", POLE_STRUCTURE = "", POLE_PAINT_CODE = "";
    SurveyVo DATA_MODEL;
    private TextView pole_no, from_pole, last_pole_distance_prv;
    private EditText et_pole_painting;

    private ArcGISDatabase arcGISDatabase;
    private ProgressDialog progressDialog;
    private Spinner sp_connecting_point, sp_pole_type_33, sp_pole_height_33, sp_pole_support_33, sp_pole_concrete_33kv, sp_pole_pin_cut_point_33kv, sp_pole_structure_33kv;
    private TextView btn_update;
    private String poleType33 = "";
    private RelativeLayout back_layout, close_layout;
    private LinearLayout layout_source_pole, layout_distance_prv, layout_connecting_point, layout_pole_details, layout_sp_pole_height_33, layout_pole_structure_33kv;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_pole_details_asset);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        FEEDER_ID = getIntent().getStringExtra("FEEDER_ID");
        POLE_NO = getIntent().getStringExtra("POLE_NO");
        FROM = getIntent().getStringExtra("FROM");
        DATA_MODEL = (SurveyVo) getIntent().getSerializableExtra("DATA_MODEL");
        arcGISDatabase = new ArcGISDatabase(this);
        // System.out.println("RECEIVING DATA" + " " + DATA_MODEL.getPole_no() + " " + DATA_MODEL.getFeeder_id());
        progressDialog = CommanUtilits.progressDialog(this);
        pole_no = findViewById(R.id.pole_no);
        from_pole = findViewById(R.id.from_pole);
        last_pole_distance_prv = findViewById(R.id.last_pole_distance_prv);
        sp_connecting_point = findViewById(R.id.sp_connecting_point);
        back_layout = findViewById(R.id.back_layout);
        close_layout = findViewById(R.id.close_layout);
        sp_pole_type_33 = findViewById(R.id.sp_pole_type_33);
        sp_pole_height_33 = findViewById(R.id.sp_pole_height_33);
        sp_pole_support_33 = findViewById(R.id.sp_pole_support_33);
        sp_pole_pin_cut_point_33kv = findViewById(R.id.sp_pole_pin_cut_point_33kv);
        sp_pole_concrete_33kv = findViewById(R.id.sp_pole_concrete_33kv);
        sp_pole_structure_33kv = findViewById(R.id.sp_pole_structure_33kv);
        btn_update = findViewById(R.id.btn_add);
        layout_source_pole = findViewById(R.id.layout_source_pole);
        layout_distance_prv = findViewById(R.id.layout_distance_prv);
        layout_connecting_point = findViewById(R.id.layout_connecting_point);
        layout_pole_details = findViewById(R.id.layout_pole_details);
        layout_sp_pole_height_33 = findViewById(R.id.layout_sp_pole_height_33);
        layout_pole_structure_33kv = findViewById(R.id.layout_pole_structure_33kv);
        et_pole_painting = findViewById(R.id.et_pole_painting);

        if (FROM.equalsIgnoreCase("SOURCE")) {
            layout_source_pole.setVisibility(View.VISIBLE);
            layout_distance_prv.setVisibility(View.VISIBLE);
            layout_connecting_point.setVisibility(View.VISIBLE);
            if (ConnectivityChecking.checkConnection(this)) {
                getPoleData(DATA_MODEL.getFeeder_id(), String.valueOf(DATA_MODEL.getPole_no()));
            } else {
                ArrayList<SurveyVo> data = arcGISDatabase.getSurveyDataByPoleNo(DATA_MODEL.getPole_no());
                for (int i = 0; i < data.size(); i++) {
                    ID = data.get(i).getID();
                    pole_no.setText(String.valueOf(data.get(i).getPole_no()));
                    from_pole.setText(String.valueOf(data.get(i).getFrom_pole()));

                    System.out.println("FROM_POLE_NUMBER_CHECK" + data.get(i).getFrom_pole());
                    String glon = String.valueOf(data.get(i).getLan());
                    String glat = String.valueOf(data.get(i).getLat());
                    System.out.println("POLE LAT---->" + glon);
                    System.out.println("POLE LONG--->" + glat);


                    ArrayList<SurveyVo> surveyVosList = arcGISDatabase.getSurveyDataByDistance(
                            Double.parseDouble(glon),
                            Double.parseDouble(glat)
                    );

                    if (surveyVosList != null && !surveyVosList.isEmpty()) {
                        List<Integer> poleNumbersInt = new ArrayList<>();

                        for (SurveyVo vo : surveyVosList) {
                            try {
                                int poleNumber = vo.getPole_no();
                                if (String.valueOf(poleNumber).equals(String.valueOf(POLE_NO)))
                                    continue;
                                poleNumbersInt.add(poleNumber);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        Collections.sort(poleNumbersInt);

                        List<String> poleNumbers = new ArrayList<>();
                        for (int num : poleNumbersInt) {
                            poleNumbers.add(String.valueOf(num));
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                EditPoleAttributesAsset.this,
                                android.R.layout.simple_spinner_item,
                                poleNumbers
                        );
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sp_connecting_point.setAdapter(adapter);

                        sp_connecting_point.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                from_pole.setText(sp_connecting_point.getSelectedItem().toString());
                                List<SurveyVo> data = arcGISDatabase.getSurveyDetailsByPoleNo(
                                        Integer.parseInt(sp_connecting_point.getSelectedItem().toString())
                                );

                                if (data != null && !data.isEmpty()) {
                                    double distance = getDistanceBetweenPolesNew(glon, glat, data.get(0).getLan(), data.get(0).getLat());
                                    double roundedDistance = Math.round(distance * 100.0) / 100.0;
                                    last_pole_distance_prv.setText(String.valueOf(roundedDistance));
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });

                        // Set previously selected from_pole value if available
                        int position = adapter.getPosition(String.valueOf(data.get(i).getFrom_pole()));
                        if (position >= 0) {
                            sp_connecting_point.setSelection(position);
                        }
                    }
                }
            }

        } else if (FROM.equalsIgnoreCase("POLE")) {
            layout_pole_details.setVisibility(View.VISIBLE);

            if (ConnectivityChecking.checkConnection(EditPoleAttributesAsset.this)) {
                getPoleData(DATA_MODEL.getFeeder_id(), String.valueOf(DATA_MODEL.getPole_no()));
            } else {
                ArrayList<SurveyVo> data = arcGISDatabase.getSurveyDataByPoleNo(DATA_MODEL.getPole_no());

                for (int i = 0; i < data.size(); i++) {
                    setOfflinePoleType(data.get(i).getPole_type());
                    setOfflinePoleStructure(data.get(i).getPoleStructure());
                    setOfflinePinCut(data.get(i).getPinpoint());
                    setOfflinePoleConcreteSpinner(data.get(i).getPOLE_CONCRETE());
                    setOfflinePoleSupportSpinner(data.get(i).getPole_support());
                    et_pole_painting.setText(data.get(i).getPole_paintingcode());
                    poleType33 = data.get(i).getPole_type();
                    POLE_HEIGHT = data.get(i).getPole_height();
                    ID = data.get(i).getID();
                }

            }
            //getPoleData(DATA_MODEL.getFeeder_id(), String.valueOf(DATA_MODEL.getPole_no()));


        } else if (FROM.equalsIgnoreCase("POLE_33KV")) {
            layout_pole_details.setVisibility(View.VISIBLE);
        }
        sp_pole_type_33.setOnItemSelectedListener(this);
        sp_pole_height_33.setOnItemSelectedListener(this);
        sp_pole_pin_cut_point_33kv.setOnItemSelectedListener(this);

        close_layout.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
        back_layout.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FROM.equalsIgnoreCase("SOURCE")) {
                    double distance = Double.parseDouble(last_pole_distance_prv.getText().toString());
                    if (POLE_TYPE.equalsIgnoreCase("TOWER")) {
                        if (distance > 400) {
                            Toast.makeText(EditPoleAttributesAsset.this, "Abnormal Distance\nDistance below 400 meters\nis allowed for TOWER", Toast.LENGTH_LONG).show();

                        } else {
                            //updatePoleLocationAsset();
                            if (ConnectivityChecking.checkConnection(EditPoleAttributesAsset.this)) {
                                updatePoleLocationAsset();
                            } else {
                                setOfflineSourcePole();
                            }
                        }
                    } else {
                        if (distance > 120) {
                            Toast.makeText(EditPoleAttributesAsset.this, "Abnormal Distance\nDistance below 120 meters\nis allowed", Toast.LENGTH_LONG).show();

                        } else {
                            if (ConnectivityChecking.checkConnection(EditPoleAttributesAsset.this)) {
                                updatePoleLocationAsset();
                            } else {
                                setOfflineSourcePole();
                            }
                            // updatePoleLocationAsset();
                        }
                    }
                } else if (FROM.equalsIgnoreCase("POLE")) {
                    boolean validationPassed = validatePoleAttributes();
                    if (validationPassed) {
                        if (ConnectivityChecking.checkConnection(EditPoleAttributesAsset.this)) {
                            updatePoleDetailsAsset();
                        } else {
                            addOfflineData();
                        }


                        //updatePoleDetailsAsset();
                    }
                }
            }
        });
    }

    private void addOfflineData() {
        SurveyVo data = new SurveyVo();
        data.setID(ID);
        data.setFeeder_id(FEEDER_ID);
        data.setPole_type(sp_pole_type_33.getSelectedItem().toString().trim());
        data.setPole_height(sp_pole_height_33.getSelectedItem().toString().trim());
        data.setPole_support(sp_pole_support_33.getSelectedItem().toString().trim());
        data.setPinpoint(sp_pole_pin_cut_point_33kv.getSelectedItem().toString().trim());
        data.setPOLE_CONCRETE(sp_pole_concrete_33kv.getSelectedItem().toString().trim());
        data.setPole_paintingcode(et_pole_painting.getText().toString().trim());
        if (sp_pole_pin_cut_point_33kv.getSelectedItem().toString().equalsIgnoreCase("CUT Point")) {
            data.setPoleStructure(sp_pole_structure_33kv.getSelectedItem().toString().trim());
        } else {
            data.setPoleStructure("");
        }

        boolean status = arcGISDatabase.addEditPoleDetailsSurvey(data);
        if (status) {
            Toast.makeText(this, "Data added successfully", Toast.LENGTH_SHORT).show();
            getOnBackPressedDispatcher().onBackPressed();
        }
    }

    private void setOfflineSourcePole() {
        SurveyVo data = new SurveyVo();
        data.setID(ID);
        data.setFeeder_id(FEEDER_ID);
        data.setFrom_pole(Integer.parseInt(from_pole.getText().toString().trim()));
        boolean status = arcGISDatabase.addEditSourcePoleSurvey(data);
        if (status) {
            Toast.makeText(this, "Data added successfully", Toast.LENGTH_SHORT).show();
            getOnBackPressedDispatcher().onBackPressed();
        }
    }

    private boolean validatePoleAttributes() {
        if (sp_pole_type_33.getSelectedItem() == null ||
                sp_pole_type_33.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(this, "Please select pole type", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (sp_pole_height_33.getSelectedItem() == null ||
                sp_pole_height_33.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(this, "Please select pole height", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (sp_pole_support_33.getSelectedItem() == null ||
                sp_pole_support_33.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(this, "Please select pole support", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (sp_pole_concrete_33kv.getSelectedItem() == null ||
                sp_pole_concrete_33kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(this, "Please select pole concrete", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (layout_pole_structure_33kv.getVisibility() == View.VISIBLE) {
            if (sp_pole_structure_33kv.getSelectedItem() == null ||
                    sp_pole_structure_33kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(this, "Please select pole structure", Toast.LENGTH_SHORT).show();
                return false;
            }
        }


        if (sp_pole_pin_cut_point_33kv.getSelectedItem() == null ||
                sp_pole_pin_cut_point_33kv.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(this, "Please select pin/cut", Toast.LENGTH_SHORT).show();
            return false;
        }

//        if (et_pole_painting.getText().toString().trim().isEmpty()) {
//            Toast.makeText(this, "Please enter pole painting code", Toast.LENGTH_SHORT).show();
//            return false;
//        }

        // ✅ Passed all validations
        return true;
    }

    private void updatePoleDetailsAsset() {
        if (!ConnectivityChecking.checkConnection(EditPoleAttributesAsset.this)) {
            Toast.makeText(getApplicationContext(),
                    "For accessing this feature, internet connection is required",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // Show progress dialog (if you already have a reusable one, use that)
        progressDialog = new ProgressDialog(EditPoleAttributesAsset.this);
        progressDialog.setMessage("Updating pole details");
        progressDialog.setCancelable(false);
        progressDialog.show();


        JsonObject postParam = new JsonObject();
        postParam.addProperty("FEEDER_ID", FEEDER_ID);
        postParam.addProperty("POLE_TYPE", sp_pole_type_33.getSelectedItem().toString().trim());
        postParam.addProperty("POLE_HEIGHT", sp_pole_height_33.getSelectedItem().toString().trim());
        postParam.addProperty("POLE_SUPPORT", sp_pole_support_33.getSelectedItem().toString().trim());
        if (sp_pole_pin_cut_point_33kv.getSelectedItem().toString().equalsIgnoreCase("CUTPOINT")) {
            postParam.addProperty("POLE_STRUCTURE", sp_pole_structure_33kv.getSelectedItem().toString().trim());
        } else {
            postParam.addProperty("POLE_STRUCTURE", "");
        }
        postParam.addProperty("POLE_PAINT_CODE", et_pole_painting.getText().toString().trim());
        postParam.addProperty("POLE_CONCRETE", sp_pole_concrete_33kv.getSelectedItem().toString().trim());
        postParam.addProperty("PIN_CUT", sp_pole_pin_cut_point_33kv.getSelectedItem().toString().trim());
        postParam.addProperty("MODE", "POLE");
        postParam.addProperty("ID", ID);

        RetrofitService retrofitService =
                RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        Call<JsonObject> call = retrofitService.updatePoleDetailsByMode(postParam);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    if (response.body() == null) {
                        Toast.makeText(EditPoleAttributesAsset.this, "Server busy, please try again", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        return;
                    }

                    JsonObject jsonObject = response.body();
                    String status = jsonObject.has("STATUS") ? jsonObject.get("STATUS").getAsString() : "FALSE";

                    if ("TRUE".equalsIgnoreCase(status)) {
                        String errorMsg = jsonObject.has("ERR_MSG") && !jsonObject.get("ERR_MSG").isJsonNull()
                                ? jsonObject.get("ERR_MSG").getAsString()
                                : "Unknown error";

                        Toast.makeText(EditPoleAttributesAsset.this, errorMsg, Toast.LENGTH_SHORT).show();
                        onBackPressed();
                        //JsonArray responseArray = jsonObject.getAsJsonArray("RESPONSE");


                    } else {
                        String errorMsg = jsonObject.has("ERR_MSG") && !jsonObject.get("ERR_MSG").isJsonNull()
                                ? jsonObject.get("ERR_MSG").getAsString()
                                : "Unknown error";

                        Toast.makeText(EditPoleAttributesAsset.this, errorMsg, Toast.LENGTH_SHORT).show();

                        if ("No Records".equalsIgnoreCase(errorMsg)) {
                            onBackPressed();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(EditPoleAttributesAsset.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                } finally {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditPoleAttributesAsset.this, "Network error, please try again", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void setPoleType() {
        List<String> ugpsccList = new ArrayList<>();
        ugpsccList.add("PSCC");
        ugpsccList.add("SPUN");
        ugpsccList.add("TOWER");
        ugpsccList.add("RAIL");
        ugpsccList.add("RS JOIST");
        ugpsccList.add("TUBULAR POLE");

        String currentType = POLE_TYPE;

        // Remove currentType if it's already in the list
        ugpsccList.remove(currentType);

        // Add currentType at the first position
        ugpsccList.add(0, currentType);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line,
                ugpsccList);
        sp_pole_type_33.setAdapter(adapter);

        // Set the spinner selection to the first item (currentType)
        sp_pole_type_33.setSelection(0);
    }

    private void setPoleSupportSpinner() {
        List<String> ugspunList = new ArrayList<>();
        if (POLE_SUPPORT.equalsIgnoreCase("None")) {
            ugspunList.add("NONE");
            ugspunList.add("STRUT");
            ugspunList.add("STAY");
            ugspunList.add("STRUT AND STAY");
        } else if (POLE_SUPPORT.equalsIgnoreCase("STRUT")) {
            ugspunList.add("STRUT");
            ugspunList.add("STAY");
            ugspunList.add("STRUT AND STAY");
            ugspunList.add("NONE");


        } else if (POLE_SUPPORT.equalsIgnoreCase("STAY")) {
            ugspunList.add("STAY");
            ugspunList.add("STRUT");
            ugspunList.add("STRUT AND STAY");
            ugspunList.add("NONE");
        } else if (POLE_SUPPORT.equalsIgnoreCase("STRUT AND STAY")) {
            ugspunList.add("STRUT AND STAY");
            ugspunList.add("STAY");
            ugspunList.add("STRUT");
            ugspunList.add("NONE");
        } else {
            ugspunList.add("Select");
            ugspunList.add("NONE");
            ugspunList.add("STRUT");
            ugspunList.add("STAY");
            ugspunList.add("STRUT AND STAY");
        }
        ArrayAdapter<String> adapterPoleConfig11kv = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, ugspunList);
        sp_pole_support_33.setAdapter(adapterPoleConfig11kv);


    }

    private void setPinCut() {
        List<String> polePin = new ArrayList<>();
        if (PIN_CUT.equalsIgnoreCase("PINPOINT")) {
            polePin.clear();
            polePin.add("PINPOINT");
            polePin.add("CUTPOINT");
        } else if (PIN_CUT.equalsIgnoreCase("CUTPOINT")) {
            polePin.add("CUTPOINT");
            polePin.add("PINPOINT");
        } else {
            polePin.add("Select");
            polePin.add("PINPOINT");
            polePin.add("CUTPOINT");
        }

        ArrayAdapter<String> adapterPoleCon = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, polePin);
        sp_pole_pin_cut_point_33kv.setAdapter(adapterPoleCon);
    }

    private void setPoleConcreteSpinner() {
        List<String> ugspunList = new ArrayList<>();
        if (POLE_CONCRETE.equalsIgnoreCase("Yes")) {
            ugspunList.add("Yes");
            ugspunList.add("No");
        } else if (POLE_CONCRETE.equalsIgnoreCase("No")) {
            ugspunList.add("No");
            ugspunList.add("Yes");
        } else {
            ugspunList.add("Select");
            ugspunList.add("No");
            ugspunList.add("Yes");
        }
        ArrayAdapter<String> adapterPoleConfig11kv = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, ugspunList);
        sp_pole_concrete_33kv.setAdapter(adapterPoleConfig11kv);
    }

    private void setPoleStructure() {
        List<String> ugspunList = new ArrayList<>();
        if (POLE_STRUCTURE.equalsIgnoreCase("SINGLE-POLE")) {
            ugspunList.add("SINGLE-POLE");
            ugspunList.add("DOUBLE-POLE");
            ugspunList.add("TRIPLE-POLE");
            ugspunList.add("FOUR-POLE");
        } else if (POLE_STRUCTURE.equalsIgnoreCase("DOUBLE-POLE")) {
            ugspunList.add("DOUBLE-POLE");
            ugspunList.add("SINGLE-POLE");
            ugspunList.add("TRIPLE-POLE");
            ugspunList.add("FOUR-POLE");
        } else if (POLE_STRUCTURE.equalsIgnoreCase("TRIPLE-POLE")) {
            ugspunList.add("TRIPLE-POLE");
            ugspunList.add("DOUBLE-POLE");
            ugspunList.add("SINGLE-POLE");
            ugspunList.add("FOUR-POLE");
        } else if (POLE_STRUCTURE.equalsIgnoreCase("FOUR-POLE")) {
            ugspunList.add("FOUR-POLE");
            ugspunList.add("SINGLE-POLE");
            ugspunList.add("DOUBLE-POLE");
            ugspunList.add("TRIPLE-POLE");
        } else {
            ugspunList.add("Select");
            ugspunList.add("SINGLE-POLE");
            ugspunList.add("DOUBLE-POLE");
            ugspunList.add("TRIPLE-POLE");
            ugspunList.add("FOUR-POLE");
        }
        ArrayAdapter<String> adapterPoleConfig11kv = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, ugspunList);
        sp_pole_structure_33kv.setAdapter(adapterPoleConfig11kv);
    }


    private void setOfflinePoleType(String POLE_TYPE) {
        List<String> ugpsccList = new ArrayList<>();
        ugpsccList.add("PSCC");
        ugpsccList.add("SPUN");
        ugpsccList.add("TOWER");
        ugpsccList.add("RAIL");
        ugpsccList.add("RS JOIST");
        ugpsccList.add("TUBULAR POLE");

        String currentType = POLE_TYPE;

        // Remove currentType if it's already in the list
        ugpsccList.remove(currentType);

        // Add currentType at the first position
        ugpsccList.add(0, currentType);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line,
                ugpsccList);
        sp_pole_type_33.setAdapter(adapter);

        // Set the spinner selection to the first item (currentType)
        sp_pole_type_33.setSelection(0);
    }

    private void setOfflinePoleSupportSpinner(String POLE_SUPPORT) {
        List<String> ugspunList = new ArrayList<>();
        if (POLE_SUPPORT.equalsIgnoreCase("None")) {
            ugspunList.add("NONE");
            ugspunList.add("STRUT");
            ugspunList.add("STAY");
            ugspunList.add("STRUT AND STAY");
        } else if (POLE_SUPPORT.equalsIgnoreCase("STRUT")) {
            ugspunList.add("STRUT");
            ugspunList.add("STAY");
            ugspunList.add("STRUT AND STAY");
            ugspunList.add("NONE");


        } else if (POLE_SUPPORT.equalsIgnoreCase("STAY")) {
            ugspunList.add("STAY");
            ugspunList.add("STRUT");
            ugspunList.add("STRUT AND STAY");
            ugspunList.add("NONE");
        } else if (POLE_SUPPORT.equalsIgnoreCase("STRUT AND STAY")) {
            ugspunList.add("STRUT AND STAY");
            ugspunList.add("STAY");
            ugspunList.add("STRUT");
            ugspunList.add("NONE");
        } else {
            ugspunList.add("Select");
            ugspunList.add("NONE");
            ugspunList.add("STRUT");
            ugspunList.add("STAY");
            ugspunList.add("STRUT AND STAY");
        }
        ArrayAdapter<String> adapterPoleConfig11kv = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, ugspunList);
        sp_pole_support_33.setAdapter(adapterPoleConfig11kv);


    }

    private void setOfflinePinCut(String PIN_CUT) {
        List<String> polePin = new ArrayList<>();
        if (PIN_CUT.equalsIgnoreCase("PINPOINT")) {
            polePin.clear();
            polePin.add("PINPOINT");
            polePin.add("CUTPOINT");
        } else if (PIN_CUT.equalsIgnoreCase("CUTPOINT")) {
            polePin.add("CUTPOINT");
            polePin.add("PINPOINT");
        } else {
            polePin.add("Select");
            polePin.add("PINPOINT");
            polePin.add("CUTPOINT");
        }

        ArrayAdapter<String> adapterPoleCon = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, polePin);
        sp_pole_pin_cut_point_33kv.setAdapter(adapterPoleCon);
    }

    private void setOfflinePoleConcreteSpinner(String POLE_CONCRETE) {
        List<String> ugspunList = new ArrayList<>();
        if (POLE_CONCRETE.equalsIgnoreCase("Yes")) {
            ugspunList.add("Yes");
            ugspunList.add("No");
        } else if (POLE_CONCRETE.equalsIgnoreCase("No")) {
            ugspunList.add("No");
            ugspunList.add("Yes");
        } else {
            ugspunList.add("Select");
            ugspunList.add("No");
            ugspunList.add("Yes");
        }
        ArrayAdapter<String> adapterPoleConfig11kv = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, ugspunList);
        sp_pole_concrete_33kv.setAdapter(adapterPoleConfig11kv);
    }

    private void setOfflinePoleStructure(String POLE_STRUCTURE) {
        List<String> ugspunList = new ArrayList<>();
        if (POLE_STRUCTURE.equalsIgnoreCase("SINGLE-POLE")) {
            ugspunList.add("SINGLE-POLE");
            ugspunList.add("DOUBLE-POLE");
            ugspunList.add("TRIPLE-POLE");
            ugspunList.add("FOUR-POLE");
        } else if (POLE_STRUCTURE.equalsIgnoreCase("DOUBLE-POLE")) {
            ugspunList.add("DOUBLE-POLE");
            ugspunList.add("SINGLE-POLE");
            ugspunList.add("TRIPLE-POLE");
            ugspunList.add("FOUR-POLE");
        } else if (POLE_STRUCTURE.equalsIgnoreCase("TRIPLE-POLE")) {
            ugspunList.add("TRIPLE-POLE");
            ugspunList.add("DOUBLE-POLE");
            ugspunList.add("SINGLE-POLE");
            ugspunList.add("FOUR-POLE");
        } else if (POLE_STRUCTURE.equalsIgnoreCase("FOUR-POLE")) {
            ugspunList.add("FOUR-POLE");
            ugspunList.add("SINGLE-POLE");
            ugspunList.add("DOUBLE-POLE");
            ugspunList.add("TRIPLE-POLE");
        } else {
            ugspunList.add("Select");
            ugspunList.add("SINGLE-POLE");
            ugspunList.add("DOUBLE-POLE");
            ugspunList.add("TRIPLE-POLE");
            ugspunList.add("FOUR-POLE");
        }
        ArrayAdapter<String> adapterPoleConfig11kv = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, ugspunList);
        sp_pole_structure_33kv.setAdapter(adapterPoleConfig11kv);
    }


    private void getPoleData(String feederId, String poleNo) {
        if (!ConnectivityChecking.checkConnection(EditPoleAttributesAsset.this)) {
            Toast.makeText(getApplicationContext(),
                    "For accessing this feature, internet connection is required",
                    Toast.LENGTH_LONG).show();
            return;
        }


        progressDialog = new ProgressDialog(EditPoleAttributesAsset.this);
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
                        Toast.makeText(EditPoleAttributesAsset.this, "Server busy, please try again", Toast.LENGTH_SHORT).show();
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
                            String glat = poleObj.has("GLAT") ? poleObj.get("GLAT").getAsString() : "";
                            String glon = poleObj.has("GLONG") ? poleObj.get("GLONG").getAsString() : "";
                            String feederIdRes = poleObj.has("FEEDER_ID") ? poleObj.get("FEEDER_ID").getAsString() : "";
                            String poleConfig = poleObj.has("POLE_CONFIG") ? poleObj.get("POLE_CONFIG").getAsString() : "";
                            POLE_TYPE = (poleObj.has("POLE_TYPE") && !poleObj.get("POLE_TYPE").isJsonNull())
                                    ? poleObj.get("POLE_TYPE").getAsString() : "";

                            POLE_HEIGHT = (poleObj.has("POLE_HEIGHT") && !poleObj.get("POLE_HEIGHT").isJsonNull())
                                    ? poleObj.get("POLE_HEIGHT").getAsString() : "";

                            POLE_SUPPORT = (poleObj.has("POLE_SUPPORT") && !poleObj.get("POLE_SUPPORT").isJsonNull())
                                    ? poleObj.get("POLE_SUPPORT").getAsString() : "";

                            PIN_CUT = (poleObj.has("PIN_CUT") && !poleObj.get("PIN_CUT").isJsonNull())
                                    ? poleObj.get("PIN_CUT").getAsString() : "";

                            POLE_CONCRETE = (poleObj.has("POLE_CONCRETE") && !poleObj.get("POLE_CONCRETE").isJsonNull())
                                    ? poleObj.get("POLE_CONCRETE").getAsString() : "";

                            POLE_STRUCTURE = (poleObj.has("POLE_STRUCTURE") && !poleObj.get("POLE_STRUCTURE").isJsonNull())
                                    ? poleObj.get("POLE_STRUCTURE").getAsString() : "";

                            POLE_PAINT_CODE = (poleObj.has("POLE_PAINT_CODE") && !poleObj.get("POLE_PAINT_CODE").isJsonNull())
                                    ? poleObj.get("POLE_PAINT_CODE").getAsString() : "";

                            ID = (poleObj.has("ID") && !poleObj.get("ID").isJsonNull())
                                    ? poleObj.get("ID").getAsString() : "";


                            if (FROM.equalsIgnoreCase("SOURCE")) {
                                pole_no.setText(poleNoRes);
                                from_pole.setText(fromPole);
                                List<String> list_pst = new ArrayList<>();

                                List<String> nearPoles = new ArrayList<>();

                                String currentLat = DATA_MODEL.getLat();
                                String currentLon = DATA_MODEL.getLan();


                                System.out.println("HAHAHAHAHAHAHHAAHHAHAHAHAHAHHAHA"+currentLat);
                                ArrayList<SurveyVo> surveyVosList = arcGISDatabase.getSurveyDataByDistance(
                                        Double.parseDouble(glon),
                                        Double.parseDouble(glat)
                                );


                                for (int i = 0; i <surveyVosList.size() ; i++) {
                                    String poleLat = surveyVosList.get(i).getLat();
                                    String poleLon = surveyVosList.get(i).getLan();

                                    String poleNo = String.valueOf(surveyVosList.get(i).getPole_no());

                                    // skip same pole
                                    if (poleNo != null && poleNo.equalsIgnoreCase(String.valueOf(DATA_MODEL.getPole_no()))) {
                                        continue;
                                    }

                                    if (poleLat != null && poleLon != null) {

                                        double distance = getDistanceBetweenPoles33(
                                                currentLat,
                                                currentLon,
                                                Double.parseDouble(poleLat),
                                                Double.parseDouble(poleLon)
                                        );

                                        long roundedDistance = Math.round(distance);

                                        System.out.println("ROUNDED_DISTANCE----->" + roundedDistance);

                                        // collect poles within 120 meters
                                        if (roundedDistance <= 120) {
                                            nearPoles.add(String.valueOf(surveyVosList.get(i).getPole_no()));
                                        }
                                    }
                                    list_pst.add(String.valueOf(surveyVosList.get(i).getPole_no()));

                                }
                                ArrayAdapter<String> adapter=null;
                                if (nearPoles.isEmpty()){
                                    adapter = new ArrayAdapter<>(
                                            EditPoleAttributesAsset.this,
                                            android.R.layout.simple_spinner_item,
                                            list_pst
                                    );
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    sp_connecting_point.setAdapter(adapter);
                                }else {
                                    adapter = new ArrayAdapter<>(
                                            EditPoleAttributesAsset.this,
                                            android.R.layout.simple_spinner_item,
                                            nearPoles
                                    );
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    sp_connecting_point.setAdapter(adapter);
                                }
                                sp_connecting_point.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        from_pole.setText(sp_connecting_point.getSelectedItem().toString());
                                        List<SurveyVo> data = arcGISDatabase.getSurveyDetailsByPoleNo(
                                                Integer.parseInt(sp_connecting_point.getSelectedItem().toString())
                                        );

                                        if (data != null && !data.isEmpty()) {
                                            double distance = getDistanceBetweenPolesNew(glon, glat, data.get(0).getLan(), data.get(0).getLat());
                                            double roundedDistance = Math.round(distance * 100.0) / 100.0;
                                            last_pole_distance_prv.setText(String.valueOf(roundedDistance));
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                    }
                                });
//                                if (surveyVosList != null && !surveyVosList.isEmpty()) {
//                                    List<Integer> poleNumbersInt = new ArrayList<>();
//
//                                    for (SurveyVo vo : surveyVosList) {
//                                        try {
//                                            int poleNumber = vo.getPole_no();
//                                            if (String.valueOf(poleNumber).equals(poleNoRes))
//                                                continue;
//                                            poleNumbersInt.add(poleNumber);
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//
//                                    Collections.sort(poleNumbersInt);
//
//                                    List<String> poleNumbers = new ArrayList<>();
//                                    for (int num : poleNumbersInt) {
//                                        poleNumbers.add(String.valueOf(num));
//                                    }
//
//                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
//                                            EditPoleAttributesAsset.this,
//                                            android.R.layout.simple_spinner_item,
//                                            poleNumbers
//                                    );
//                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                                    sp_connecting_point.setAdapter(adapter);
//
//                                    sp_connecting_point.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                                        @Override
//                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                            from_pole.setText(sp_connecting_point.getSelectedItem().toString());
//                                            List<SurveyVo> data = arcGISDatabase.getSurveyDetailsByPoleNo(
//                                                    Integer.parseInt(sp_connecting_point.getSelectedItem().toString())
//                                            );
//
//                                            if (data != null && !data.isEmpty()) {
//                                                double distance = getDistanceBetweenPolesNew(glon, glat, data.get(0).getLan(), data.get(0).getLat());
//                                                double roundedDistance = Math.round(distance * 100.0) / 100.0;
//                                                last_pole_distance_prv.setText(String.valueOf(roundedDistance));
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onNothingSelected(AdapterView<?> parent) {
//                                        }
//                                    });
//
//                                    // Set previously selected from_pole value if available
//                                    int position = adapter.getPosition(fromPole);
//                                    if (position >= 0) {
//                                        sp_connecting_point.setSelection(position);
//                                    }
//                                }
                            } else {
                                setPoleType();
                                setPoleSupportSpinner();
                                setPinCut();
                                setPoleConcreteSpinner();
                                setPoleStructure();
                                et_pole_painting.setText(POLE_PAINT_CODE);
                            }


                        }

                    } else {
                        String errorMsg = jsonObject.has("ERR_MSG") && !jsonObject.get("ERR_MSG").isJsonNull()
                                ? jsonObject.get("ERR_MSG").getAsString()
                                : "Unknown error";

                        Toast.makeText(EditPoleAttributesAsset.this, errorMsg, Toast.LENGTH_SHORT).show();

                        if ("No Records".equalsIgnoreCase(errorMsg)) {
                            onBackPressed();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(EditPoleAttributesAsset.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                } finally {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditPoleAttributesAsset.this, "Network error, please try again", Toast.LENGTH_LONG).show();
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

    private void updatePoleLocationAsset() {
        if (!ConnectivityChecking.checkConnection(EditPoleAttributesAsset.this)) {
            Toast.makeText(getApplicationContext(),
                    "For accessing this feature, internet connection is required",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // Show progress dialog (if you already have a reusable one, use that)
        progressDialog = new ProgressDialog(EditPoleAttributesAsset.this);
        progressDialog.setMessage("Fetching pole data...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        JsonObject postParam = new JsonObject();
        postParam.addProperty("FEEDER_ID", FEEDER_ID);
        postParam.addProperty("FROM_POLE", from_pole.getText().toString());
        postParam.addProperty("MODE", "SOURCE");
        postParam.addProperty("ID", ID);

        RetrofitService retrofitService =
                RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        Call<JsonObject> call = retrofitService.updatePoleDetailsByMode(postParam);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    if (response.body() == null) {
                        Toast.makeText(EditPoleAttributesAsset.this, "Server busy, please try again", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        return;
                    }

                    JsonObject jsonObject = response.body();
                    String status = jsonObject.has("STATUS") ? jsonObject.get("STATUS").getAsString() : "FALSE";

                    if ("TRUE".equalsIgnoreCase(status)) {
                        String errorMsg = jsonObject.has("ERR_MSG") && !jsonObject.get("ERR_MSG").isJsonNull()
                                ? jsonObject.get("ERR_MSG").getAsString()
                                : "Unknown error";

                        Toast.makeText(EditPoleAttributesAsset.this, errorMsg, Toast.LENGTH_SHORT).show();
                        onBackPressed();
                        //JsonArray responseArray = jsonObject.getAsJsonArray("RESPONSE");


                    } else {
                        String errorMsg = jsonObject.has("ERR_MSG") && !jsonObject.get("ERR_MSG").isJsonNull()
                                ? jsonObject.get("ERR_MSG").getAsString()
                                : "Unknown error";

                        Toast.makeText(EditPoleAttributesAsset.this, errorMsg, Toast.LENGTH_SHORT).show();

                        if ("No Records".equalsIgnoreCase(errorMsg)) {
                            onBackPressed();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(EditPoleAttributesAsset.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                } finally {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditPoleAttributesAsset.this, "Network error, please try again", Toast.LENGTH_LONG).show();
            }
        });
    }

    private double getDistanceBetweenPolesNew(String polLat, String polLon, String cLat, String cLon) {
//        System.out.println("CURRENT LOCATION LAT NEW : " + cLat);
//        System.out.println("CURRENT LOCATION LON NEW: " + cLon);
//        System.out.println("POLE LOCATION LAT NEW:  " + polLat);
//        System.out.println("POLE LOCATION LON NEW:  " + polLon);
        Location currentStartPoint = new Location("locationC");
        currentStartPoint.setLatitude(Double.parseDouble(cLat));
        currentStartPoint.setLongitude(Double.parseDouble(cLon));

        Location currentEndPoint = new Location("locationC");
        currentEndPoint.setLatitude(Double.parseDouble(polLat));
        currentEndPoint.setLongitude(Double.parseDouble(polLon));
        double currentDistance = currentStartPoint.distanceTo(currentEndPoint);
        return currentDistance;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.sp_pole_type_33:
                poleType33 = sp_pole_type_33.getSelectedItem().toString();
                sp_pole_height_33.setSelection(0);

                if (!sp_pole_type_33.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    layout_sp_pole_height_33.setVisibility(View.VISIBLE);
                    if ("PSCC".equalsIgnoreCase(poleType33)) {
                        List<String> ugpsccList = new ArrayList<>();

                        if (poleType33.equalsIgnoreCase("PSCC")) {
                            if (POLE_HEIGHT.equalsIgnoreCase("8.0") ||
                                    POLE_HEIGHT.equalsIgnoreCase("9.1") ||
                                    POLE_HEIGHT.equalsIgnoreCase("11")) {
                                ugpsccList.add("8.0");
                                ugpsccList.add("9.1");
                                ugpsccList.add("11");
                                ugpsccList.remove(POLE_HEIGHT);
                                ugpsccList.add(POLE_HEIGHT);
                                Collections.reverse(ugpsccList);
                            } else {
                                ugpsccList.add("Select");
                                ugpsccList.add("8.0");
                                ugpsccList.add("9.1");
                                ugpsccList.add("11");
                            }
                        } else {
                            ugpsccList.add("Select");
                            ugpsccList.add("8.0");
                            ugpsccList.add("9.1");
                            ugpsccList.add("11");
                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ugpsccList);
                        sp_pole_height_33.setAdapter(adapter);
                    } else if ("SPUN".equalsIgnoreCase(poleType33)) {
                        List<String> ugspunList = new ArrayList<>();

                        if (poleType33.equalsIgnoreCase("SPUN")) {
                            if (POLE_HEIGHT.equalsIgnoreCase("9.5") ||
                                    POLE_HEIGHT.equalsIgnoreCase("11") ||
                                    POLE_HEIGHT.equalsIgnoreCase("12.5")
                            ) {
                                ugspunList.add("9.5");
                                ugspunList.add("11");
                                ugspunList.add("12.5");
                                ugspunList.remove(POLE_HEIGHT);
                                ugspunList.add(POLE_HEIGHT);
                                Collections.reverse(ugspunList);
                            } else {
                                ugspunList.add("Select");
                                ugspunList.add("9.5");
                                ugspunList.add("11");
                                ugspunList.add("12.5");
                            }

                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ugspunList);
                        sp_pole_height_33.setAdapter(adapter);

                    } else if ("TOWER".equalsIgnoreCase(poleType33)) {
                        List<String> ugspunList = new ArrayList<>();

                        if (poleType33.equalsIgnoreCase("TOWER")) {
                            if (POLE_HEIGHT.equalsIgnoreCase("M+3") ||
                                    POLE_HEIGHT.equalsIgnoreCase("M+6") ||
                                    POLE_HEIGHT.equalsIgnoreCase("M+9") ||
                                    POLE_HEIGHT.equalsIgnoreCase("M+12") ||
                                    POLE_HEIGHT.equalsIgnoreCase("R+6")

                            ) {
                                ugspunList.add("M+3");
                                ugspunList.add("M+6");
                                ugspunList.add("M+9");
                                ugspunList.add("M+12");
                                ugspunList.add("R+6");
                                ugspunList.remove(POLE_HEIGHT);
                                ugspunList.add(POLE_HEIGHT);
                                Collections.reverse(ugspunList);
                            } else {
                                ugspunList.add("Select");
                                ugspunList.add("M+3");
                                ugspunList.add("M+6");
                                ugspunList.add("M+9");
                                ugspunList.add("M+12");
                                ugspunList.add("R+6");
                            }
                        } else {
                            ugspunList.add("Select");
                            ugspunList.add("M+3");
                            ugspunList.add("M+6");
                            ugspunList.add("M+9");
                            ugspunList.add("M+12");
                            ugspunList.add("R+6");
                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ugspunList);
                        sp_pole_height_33.setAdapter(adapter);

                    } else if ("33/11KV SS-11KV BAY".equalsIgnoreCase(poleType33)) {
                        List<String> ugspunList = new ArrayList<>();

                        if (IS_LINE_EXTENSION_11_DATA_AVAILABLE) {
                            if (POLE_TYPE.equalsIgnoreCase("33/11KV SS-11KV BAY")) {
                                if (POLE_HEIGHT.equalsIgnoreCase("9.5") ||
                                        POLE_HEIGHT.equalsIgnoreCase("11") ||
                                        POLE_HEIGHT.equalsIgnoreCase("12.5")

                                ) {
                                    ugspunList.add("9.5");
                                    ugspunList.add("11");
                                    ugspunList.add("12.5");
                                    ugspunList.remove(POLE_HEIGHT);
                                    ugspunList.add(POLE_HEIGHT);
                                    Collections.reverse(ugspunList);
                                } else {
                                    ugspunList.add("Select");
                                    ugspunList.add("9.5");
                                    ugspunList.add("11");
                                    ugspunList.add("12.5");
                                }
                            } else {
                                ugspunList.add("Select");
                                ugspunList.add("9.5");
                                ugspunList.add("11");
                                ugspunList.add("12.5");
                            }
                        } else {

                            ugspunList.add("9.5");
                            ugspunList.add("11");
                            ugspunList.add("12.5");
                            ugspunList.add(DATA_MODEL.getPole_height());
                            ugspunList.remove(DATA_MODEL.getPole_height());
                            Collections.reverse(ugspunList);
                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ugspunList);
                        sp_pole_height_33.setAdapter(adapter);

                    } else if ("RAIL".equalsIgnoreCase(poleType33)) {
                        List<String> ugspunList = new ArrayList<>();

                        if (poleType33.equalsIgnoreCase("RAIL")) {
                            if (POLE_HEIGHT.equalsIgnoreCase("9.5") ||
                                    POLE_HEIGHT.equalsIgnoreCase("11") ||
                                    POLE_HEIGHT.equalsIgnoreCase("12.5")) {
                                ugspunList.add("9.5");
                                ugspunList.add("11");
                                ugspunList.add("12.5");
                                ugspunList.remove(POLE_HEIGHT);
                                ugspunList.add(POLE_HEIGHT);
                                Collections.reverse(ugspunList);
                            } else {
                                ugspunList.add("Select");
                                ugspunList.add("9.5");
                                ugspunList.add("11");
                                ugspunList.add("12.5");
                            }
                        } else {
                            ugspunList.add("Select");
                            ugspunList.add("9.5");
                            ugspunList.add("11");
                            ugspunList.add("12.5");
                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ugspunList);
                        sp_pole_height_33.setAdapter(adapter);

                    } else if ("RS JOIST".equalsIgnoreCase(poleType33)) {
                        List<String> ugspunList = new ArrayList<>();

                        if (poleType33.equalsIgnoreCase("RS JOIST")) {
                            if (POLE_HEIGHT.equalsIgnoreCase("9.1") ||
                                    POLE_HEIGHT.equalsIgnoreCase("11")
                            ) {
                                ugspunList.add("9.1");
                                ugspunList.add("11");
                                ugspunList.remove(POLE_HEIGHT);
                                ugspunList.add(POLE_HEIGHT);
                                Collections.reverse(ugspunList);
                            } else {
                                ugspunList.add("Select");
                                ugspunList.add("9.1");
                                ugspunList.add("11");
                            }
                        } else {
                            ugspunList.add("Select");
                            ugspunList.add("9.1");
                            ugspunList.add("11");
                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ugspunList);
                        sp_pole_height_33.setAdapter(adapter);

                    } else if ("TUBULAR POLE".equalsIgnoreCase(poleType33)) {
                        List<String> ugspunList = new ArrayList<>();

                        if (poleType33.equalsIgnoreCase("TUBULAR POLE")) {
                            if (POLE_HEIGHT.equalsIgnoreCase("9.1") ||
                                    POLE_HEIGHT.equalsIgnoreCase("11")) {
                                ugspunList.add("9.1");
                                ugspunList.add("11");
                                ugspunList.remove(POLE_HEIGHT);
                                ugspunList.add(POLE_HEIGHT);
                                Collections.reverse(ugspunList);
                            } else {
                                ugspunList.add("Select");
                                ugspunList.add("9.1");
                                ugspunList.add("11");
                            }
                        } else {
                            ugspunList.add("Select");
                            ugspunList.add("9.1");
                            ugspunList.add("11");
                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ugspunList);
                        sp_pole_height_33.setAdapter(adapter);

                    }
                }

                break;

            case R.id.sp_pole_pin_cut_point_33kv:
                if (sp_pole_pin_cut_point_33kv.getSelectedItem().toString().equalsIgnoreCase("CUTPOINT")) {
                    layout_pole_structure_33kv.setVisibility(View.VISIBLE);
                } else {
                    layout_pole_structure_33kv.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
