package com.apepdcl.gis_survey.estimates.new_module;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.estimates.ArcGISDatabaseEstimates;
import com.apepdcl.gis_survey.estimates.models.MaterialMasterModel;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MeterSurveyDialog extends Dialog implements AdapterView.OnItemSelectedListener {

    Activity context;
    String poleLat;
    String poleLon;
    String REG_COUNT;
    String EST_NO;
    String GIS_NO;
    int selectedCount=0;
    ArcGISDatabaseEstimates databaseEstimates;
    LinearLayout layout_sp_pole_desc_meter, layout_no_meters;
    String meterCode = "";
    Spinner sp_meter_type_meter, sp_meter_survey_type, sp_meter_location, sp_no_of_meters_meter;
    Button btn_add_meters, btn_clear_meters;
    private long randomNumber = 0;
    Callout callout;
    GraphicsOverlay graphicsOverlay;
    Graphic graphic;
    TextView location_no;
    RelativeLayout close_layout_meters;
    int poleNumber;


    public MeterSurveyDialog(Activity context, String poleLat, String poleLon, String REG_COUNT, String EST_NO, String GIS_NO, Callout callout, GraphicsOverlay graphicsOverlay, Graphic graphic) {
        super(context);
        this.context = context;
        this.poleLat = poleLat;
        this.poleLon = poleLon;
        this.REG_COUNT = REG_COUNT;
        this.EST_NO = EST_NO;
        this.GIS_NO = GIS_NO;
        this.callout = callout;
        this.graphicsOverlay = graphicsOverlay;
        this.graphic = graphic;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_meters);
        setCancelable(false);
        databaseEstimates = new ArcGISDatabaseEstimates(context);
        btn_add_meters = findViewById(R.id.btn_add_meters);
        btn_clear_meters = findViewById(R.id.btn_clear_meters);
        location_no = findViewById(R.id.location_no_meters);
        close_layout_meters = findViewById(R.id.close_layout_meters);
        layout_no_meters = findViewById(R.id.layout_no_meters);

        layout_sp_pole_desc_meter = findViewById(R.id.layout_sp_pole_desc_meter);
        sp_meter_type_meter = findViewById(R.id.sp_meter_type_meter);
        sp_meter_survey_type = findViewById(R.id.sp_meter_survey_type);
        sp_meter_location = findViewById(R.id.sp_meter_location);
        sp_no_of_meters_meter = findViewById(R.id.sp_no_of_meters_meter);
        sp_meter_survey_type.setOnItemSelectedListener(this);
        sp_meter_location.setOnItemSelectedListener(this);
        sp_no_of_meters_meter.setOnItemSelectedListener(this);
        randomNumber = createRandomUID();
        ArrayList<NewModel> editList = databaseEstimates.getNewSurveyData(EST_NO, "METERS", true,"");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(editList);
       // Log.d("METERS_DEBUGGGG", "onCreate: " + json);
       // Log.d("REG_COUNT======>", "onCreate: " + REG_COUNT);


        if (editList.isEmpty()) {
            poleNumber = 1;
        } else {
            int lastPoleNumber = Integer.parseInt(editList.get(editList.size() - 1).getMT_LOC_NO());

            poleNumber = lastPoleNumber;

            for (int i = 0; i < editList.size(); i++) {
                int currentPoleNumber = Integer.parseInt(editList.get(i).getMT_LOC_NO());
                if (currentPoleNumber > poleNumber) {
                    poleNumber = currentPoleNumber;
                }
            }

            poleNumber++;

        }
        location_no.setText(String.valueOf(poleNumber));


//        if (editList.isEmpty()) {
//            location_no.setText("M1");
//        }


      //  Log.d("randomNumber__Meter", "onItemSelected: " + randomNumber);
       // Log.d("REG_COUNT=====>", "onCreate: " + REG_COUNT);


        loadMeters();

        btn_add_meters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToMeterToDataBase();
            }
        });
        btn_clear_meters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callout.dismiss();
                graphicsOverlay.getGraphics().remove(graphic);
                dismiss();
            }
        });

        close_layout_meters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callout.dismiss();
                graphicsOverlay.getGraphics().remove(graphic);
                dismiss();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.sp_meter_location:
                if (sp_meter_location.getSelectedItem().toString().equalsIgnoreCase("METERS AT DIFFERENT PREMISES")) {
                    layout_no_meters.setVisibility(View.VISIBLE);
                    List<String> meterCountList = new ArrayList<>();
                    int temp_reg_count = Integer.parseInt(REG_COUNT);

                 List<NewModel>  listMeters = databaseEstimates.getNewSurveyData(EST_NO, "METERS", true,"");
                 if (listMeters.size()>0){
                     for (int i = 0; i <listMeters.size() ; i++) {
                         selectedCount = selectedCount + listMeters.get(i).getCOUNT_MT();
                     }
                 }

                 int total_count =  temp_reg_count-selectedCount;



                    for (int i = 1; i <= total_count; i++) {
                        meterCountList.add("" + i);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, meterCountList);
                    sp_no_of_meters_meter.setAdapter(adapter);
                } else {
                    layout_no_meters.setVisibility(View.GONE);
                }
                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void loadMeters() {
        List<MaterialMasterModel> model = databaseEstimates.getMeters();
        List<String> abList = new ArrayList<>();
        HashMap<String, String> materialCodeMap = new HashMap<>();


      //  System.out.println("FABRIC SIZE TOTAL: " + model.size());

        for (MaterialMasterModel item : model) {
            String itemPoleType = item.getPOLE_TYPE();
            String material = item.getMATERIAL();
            String materialPart1 = item.getMATERIALPART1();
            String voltageLevel = item.getHT_33KV();
            String itemPoleHeight = item.getPOLE_HEIGHT();
            // String groupDesc=item.getGROUPDESC1()

            abList.add(materialPart1);
            materialCodeMap.put(materialPart1, item.getMATERIALCODE());
            //Log.d("LOAD_METERS--->>>", "loadMeters: " + item.getMATERIALCODE());


        }

        if (!abList.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, abList);
            sp_meter_type_meter.setAdapter(adapter);
            sp_meter_type_meter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedMaterialPart1 = abList.get(position);
                    meterCode = materialCodeMap.get(selectedMaterialPart1);
                   // Log.d("loadMeters--->>>", "onItemSelected: " + meterCode);
                    //clamp33kvCode = model.get(position).getMATERIALCODE();
                    // Log.d("BACK_CLAMP_CODE_33KV", "onItemSelected: " + clamp33kvCode);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Handle case when no selection is made
                }
            });
            layout_sp_pole_desc_meter.setVisibility(View.VISIBLE);
        } else {
            layout_sp_pole_desc_meter.setVisibility(View.GONE);
        }

      //  System.out.println("AB SWITCH SIZE FILTER: " + abList.size());

    }

    private void addToMeterToDataBase() {
        NewModel model = new NewModel();
        model.setEST_NO(EST_NO);
        model.setGIS_ID(GIS_NO);
        model.setLATITUDE(poleLat);
        model.setLONGITUDE(poleLon);
        model.setMT_DESC(sp_meter_location.getSelectedItem().toString());
        model.setMT_CODE(meterCode);
        model.setLOCATION_ID(Long.toString(randomNumber));
        if (sp_meter_location.getSelectedItem().toString().equalsIgnoreCase("METERS AT DIFFERENT PREMISES")) {

            model.setCOUNT_MT(Integer.parseInt(sp_no_of_meters_meter.getSelectedItem().toString()));
        } else {
            model.setCOUNT_MT(Integer.parseInt(REG_COUNT));
        }

        model.setVOLTAGE_LEVEL("METERS");
        model.setMT_SURVEY_TYPE(sp_meter_survey_type.getSelectedItem().toString());
        model.setMT_LOC_NO(location_no.getText().toString());
        boolean status = databaseEstimates.addNewSurveyData(model);

        if (status) {
            Toast.makeText(context, "Meter location captured successfully..", Toast.LENGTH_SHORT).show();
            dismiss();
            callout.dismiss();
            graphicsOverlay.getGraphics().remove(graphic);
        } else {
            Toast.makeText(context, "Failed to capture Pole location.", Toast.LENGTH_SHORT).show();
        }

    }

    private long createRandomUID() {
        UUID uuid = UUID.randomUUID();
       // System.out.println("UUID: " + uuid);
        long randomNumber = uuid.getLeastSignificantBits();
        randomNumber = Math.abs(randomNumber);
       // System.out.println("Random Number: " + randomNumber);
        return randomNumber;
    }

}
