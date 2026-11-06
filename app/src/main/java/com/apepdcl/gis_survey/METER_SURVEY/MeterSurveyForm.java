package com.apepdcl.gis_survey.METER_SURVEY;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.leo.searchablespinner.SearchableSpinner;
import com.leo.searchablespinner.interfaces.OnItemSelectListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by SOUBHAGYA SWAIN on 08-06-2026.
 */
public class MeterSurveyForm extends AppCompatActivity implements ServicesAdapter.AdapterListener {

    ImageView back_layout;
    TextView pole_no, pole_height, pole_type;

    PolesModel selectedPole;

    Spinner sp_no_of_services;

    RecyclerView services_rv;

    ServicesAdapter adapter;

    private int selectedPosition = -1;
    private Uri imageUri;
    public ArrayList<ServiceData> serviceList;

    TextView submitButton;

    ActivityResultLauncher<Uri> cameraLauncher;

    public static boolean IS_LOCATION_CHANGED;
    public static double selectedLat, selectedLong;
    public static double currentLat, currentLong, gpsAccuracy;
    boolean isNoSelected = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meter_survey_form);
        getSupportActionBar().hide();

        back_layout = findViewById(R.id.back_layout);
        pole_no = findViewById(R.id.pole_no);
        pole_height = findViewById(R.id.pole_height);
        pole_type = findViewById(R.id.pole_type);
        sp_no_of_services = findViewById(R.id.sp_no_of_services);
        services_rv = findViewById(R.id.services_rv);
        submitButton = findViewById(R.id.submitButton);
        selectedPole = (PolesModel) getIntent().getSerializableExtra("POLE_DATA");
        pole_no.setText("Pole No: " + selectedPole.getPole_no());
        pole_height.setText(selectedPole.getPole_height());
        pole_type.setText(selectedPole.getPole_type());
        serviceList = new ArrayList<>();

        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        sp_no_of_services.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!sp_no_of_services.getSelectedItem().toString().equals("Select")) {
                    int selectedServices = Integer.parseInt(sp_no_of_services.getSelectedItem().toString());

                    if (isNoSelected){
                        int currentSize = selectedServices - serviceList.size();

                        if (currentSize > 0) {

                            for (int j = 0; j < currentSize; j++) {
                                serviceList.add(new ServiceData());
                            }

                        } else if (currentSize < 0) {

                            int removeCount = Math.abs(currentSize);

                            for (int k = 0; k < removeCount; k++) {
                                serviceList.remove(serviceList.size() - 1);
                            }
                        }
                    }else {
                        for (int j = 0; j < selectedServices; j++) {
                            serviceList.add(new ServiceData());
                        }
                    }






                    services_rv.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MeterSurveyForm.this);
                    services_rv.setLayoutManager(layoutManager);
                    services_rv.setFocusable(false);
                    adapter = new ServicesAdapter(serviceList, MeterSurveyForm.this, MeterSurveyForm.this);
                    services_rv.setAdapter(adapter);
                    isNoSelected = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateAllServices()) {
                    Gson gson = new Gson();
                    String json = gson.toJson(serviceList);
                    Log.e("SERVICE_DATA", json);
                    // Send to API

                    MeterSurveyDatabase database = new MeterSurveyDatabase(MeterSurveyForm.this);
                    for (int i = 0; i < serviceList.size(); i++) {
                        MeterPostingModel data = new MeterPostingModel();
                        data.setREF_ID(System.currentTimeMillis() + "_" + i);
                        data.setPOLE_ID(selectedPole.getPole_id());
                        data.setPOLE_NO(selectedPole.getPole_no());
                        data.setSERVICE_NUMBER(serviceList.get(i).getServiceNumber());
                        data.setDTR_CODE(selectedPole.getDtr_code());
                        data.setDTR_NAME(selectedPole.getDtr_name());
                        data.setLT_CKT_CODE(selectedPole.getLt_ckt_code());
                        data.setLATITUDE(String.valueOf(serviceList.get(i).getLatitude()));
                        data.setLONGITUDE(String.valueOf(serviceList.get(i).getLongitude()));
                        data.setCURRENT_LAT(String.valueOf(serviceList.get(i).getCurrent_lat()));
                        data.setCURRENT_LON(String.valueOf(serviceList.get(i).getCurrent_lon()));
                        data.setGPS_ACCURACY(String.valueOf(serviceList.get(i).getGps_accuracy()));
                        //data.setIMAGE(serviceList.get(i).getImageUri());
                        data.setIMAGE("");
                        data.setIS_IMAGE_TRANSMITTED("Y");
                        data.setUKSC_NO(serviceList.get(i).getUkscNumber());
                        data.setSC_NAME(serviceList.get(i).getServiceName());
                        data.setREMARKS("");
                        if (ConnectivityChecking.checkConnection(MeterSurveyForm.this)) {
                            data.setSURVEY_MODE("ONLINE");
                        }else {
                            data.setSURVEY_MODE("OFFLINE");
                        }
                        database.addMeterSurveyData(data);
                    }

                    PoleCountModel model = new PoleCountModel();
                    model.setCOUNT_POLE_ID(selectedPole.getPole_id());
                    model.setCOUNT_POLE_NO(selectedPole.getPole_no());
                    model.setREC_ID(getIntent().getStringExtra("REC_ID"));
                    model.setCOUNT_TOTAL(""+serviceList.size());
                    database.insertOrUpdateCountData(model);

                    Toast.makeText(MeterSurveyForm.this, "Data Added Successfully", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }
        });

        cameraLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.TakePicture(),
                        result -> {
                            if (result) {
                                serviceList.get(selectedPosition).setImageUri(imageUri.toString());
                                adapter.notifyItemChanged(selectedPosition);
                            }
                        });


    }

    @Override
    public void onCaptureImage(int position) {
        selectedPosition = position;
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "MeterImage_" + System.currentTimeMillis());
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        cameraLauncher.launch(imageUri);
    }

    @Override
    public void onPickLocation(int position) {
        selectedPosition = position;
        IS_LOCATION_CHANGED = false;
        Intent intent = new Intent(this, PickServiceLocation.class);
        intent.putExtra("SERVICE_POSITION", position);
        intent.putExtra("POLE_DATA", selectedPole);
        intent.putExtra("SERVICE_NUMBER", serviceList.get(position).getServiceNumber());
        startActivity(intent);
    }

    @Override
    public void onSelectServiceNumber(int position) {
        ArrayList<ServiceNumbersModel> serviceNumbersModelArrayList = new ArrayList<>(MeterConstants.serviceNumbersList);
        showServiceSearchBottomSheet(position, false, serviceNumbersModelArrayList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (IS_LOCATION_CHANGED) {
            IS_LOCATION_CHANGED = false;
            serviceList.get(selectedPosition).setLongitude(selectedLong);
            serviceList.get(selectedPosition).setLatitude(selectedLat);
            serviceList.get(selectedPosition).setCurrent_lat(currentLat);
            serviceList.get(selectedPosition).setCurrent_lon(currentLong);
            serviceList.get(selectedPosition).setGps_accuracy(gpsAccuracy);
            serviceList.get(selectedPosition).setLocationCaptured(true);
            adapter.notifyItemChanged(selectedPosition);
        }
    }


    private boolean validateAllServices() {

        for (int i = 0; i < serviceList.size(); i++) {
            ServiceData item = serviceList.get(i);
            if (item.getServiceNumber().trim().isEmpty()) {
                Toast.makeText(this, "Enter Service Number for item " + (i + 1), Toast.LENGTH_SHORT).show();
                return false;
            }

           /* if (item.getImageUri().trim().isEmpty()) {
                Toast.makeText(this, "Capture image for item " + (i + 1), Toast.LENGTH_SHORT).show();
                return false;
            }*/

            if (!item.isLocationCaptured()) {
                Toast.makeText(this, "Pick location for item " + (i + 1), Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }
    @SuppressLint("MissingInflatedId")
    private void showServiceSearchBottomSheet(int adapterPosition, boolean isAreaFilter, ArrayList<ServiceNumbersModel> serviceNumbersModelArrayList) {

        BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        View view = getLayoutInflater().inflate(R.layout.bottomsheet_service_search, null);
        dialog.setContentView(view);
        RadioGroup radioGroup = view.findViewById(R.id.radioGroup);


        EditText search_service_number = view.findViewById(R.id.search_service_number);
        EditText search_consumer_name = view.findViewById(R.id.search_consumer_name);
        EditText search_meter_number = view.findViewById(R.id.search_meter_number);
        TextView label_search_by = view.findViewById(R.id.label_search_by);
        TextView or_label_search_by = view.findViewById(R.id.or_label_search_by);
        ImageView back_layout_search = view.findViewById(R.id.back_layout_search);

        RecyclerView recyclerView = view.findViewById(R.id.rvResults);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MeterSurveyForm.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setFocusable(false);

        SearchServicesAdapter searchServicesAdapter = new SearchServicesAdapter(serviceNumbersModelArrayList, adapterPosition, dialog);
        recyclerView.setAdapter(searchServicesAdapter);

        if (isAreaFilter){
            radioGroup.setVisibility(View.GONE);
            label_search_by.setVisibility(View.GONE);
            or_label_search_by.setText("Area Name: "+serviceNumbersModelArrayList.get(0).getAREA_NAME()+"\nArea Code: "+serviceNumbersModelArrayList.get(0).getAREA_CODE());
        }

        back_layout_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        search_service_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchText = search_service_number.getText().toString().trim();
                if (!searchText.isEmpty()) {
                    ArrayList<ServiceNumbersModel> tempList = new ArrayList<>();
                    tempList.clear();
                    for (int j = 0; j < serviceNumbersModelArrayList.size(); j++) {
                        if (serviceNumbersModelArrayList.get(j).getSERVICE_NUMBER().toLowerCase().contains(searchText.toLowerCase())){
                            tempList.add(serviceNumbersModelArrayList.get(j));
                        }
                    }

                    searchServicesAdapter.updateList(tempList);
                }else {
                    searchServicesAdapter.updateList(serviceNumbersModelArrayList);
                }
            }
        });

        search_consumer_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchText = search_consumer_name.getText().toString().trim();
                if (!searchText.isEmpty()) {
                    ArrayList<ServiceNumbersModel> tempList = new ArrayList<>();
                    tempList.clear();
                    for (int j = 0; j < serviceNumbersModelArrayList.size(); j++) {
                        if (serviceNumbersModelArrayList.get(j).getCONSUMER_NAME().toLowerCase().contains(searchText.toLowerCase())){
                            tempList.add(serviceNumbersModelArrayList.get(j));
                        }
                    }

                    searchServicesAdapter.updateList(tempList);
                }else {
                    searchServicesAdapter.updateList(serviceNumbersModelArrayList);
                }
            }
        });

        search_meter_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchText = search_meter_number.getText().toString().trim();
                if (!searchText.isEmpty()) {
                    ArrayList<ServiceNumbersModel> tempList = new ArrayList<>();
                    tempList.clear();
                    for (int j = 0; j < serviceNumbersModelArrayList.size(); j++) {
                        if (serviceNumbersModelArrayList.get(j).getMETER_NO().toLowerCase().contains(searchText.toLowerCase())){
                            tempList.add(serviceNumbersModelArrayList.get(j));
                        }
                    }

                    searchServicesAdapter.updateList(tempList);
                }
                else {
                    searchServicesAdapter.updateList(serviceNumbersModelArrayList);
                }
            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String selected = "";
                switch (checkedId) {
                    case R.id.radioName:
                        selected = "Name";
                        break;
                    case R.id.radioMeterNo:
                        selected = "Meter No.";
                        break;
                    case R.id.radioAreaCode:
                        selected = "Area Code";
                        dialog.dismiss();
                        Set<String> uniqueAreaCodes = new HashSet<>();
                        for(ServiceNumbersModel item : serviceNumbersModelArrayList){
                            uniqueAreaCodes.add(item.getAREA_CODE());
                        }
                        ArrayList<String> arList = new ArrayList<>();
                        arList.addAll(uniqueAreaCodes);

                        SearchableSpinner searchableSpinner = new SearchableSpinner(MeterSurveyForm.this);
                        searchableSpinner.setWindowTitle("Select Area Code");
                        searchableSpinner.setSpinnerListItems(arList);
                        searchableSpinner.show();
                        searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                            @Override
                            public void setOnItemSelectListener(int position, String selectedString) {
                                String selectedAreaCode = selectedString;
                                ArrayList<ServiceNumbersModel> serList = new ArrayList<>();
                                for (int i = 0; i < serviceNumbersModelArrayList.size(); i++) {
                                    if (serviceNumbersModelArrayList.get(i).getAREA_CODE().equals(selectedAreaCode)){
                                        serList.add(serviceNumbersModelArrayList.get(i));
                                    }
                                }

                                showServiceSearchBottomSheet(adapterPosition,true, serList);
                            }
                        });
                        break;
                    case R.id.radioAreaName:
                        selected = "Area Name";
                        dialog.dismiss();
                        Set<String> uniqueAreaNames = new HashSet<>();
                        for(ServiceNumbersModel item : serviceNumbersModelArrayList){
                            uniqueAreaNames.add(item.getAREA_NAME());
                        }
                        ArrayList<String> arNameList = new ArrayList<>();
                        arNameList.addAll(uniqueAreaNames);

                        SearchableSpinner searchableSpinnerName = new SearchableSpinner(MeterSurveyForm.this);
                        searchableSpinnerName.setWindowTitle("Select Area Name");
                        searchableSpinnerName.setSpinnerListItems(arNameList);
                        searchableSpinnerName.show();
                        searchableSpinnerName.setOnItemSelectListener(new OnItemSelectListener() {
                            @Override
                            public void setOnItemSelectListener(int position, String selectedString) {
                                String selectedAreaName = selectedString;
                                ArrayList<ServiceNumbersModel> serListName = new ArrayList<>();
                                for (int i = 0; i < serviceNumbersModelArrayList.size(); i++) {
                                    if (serviceNumbersModelArrayList.get(i).getAREA_NAME().equals(selectedAreaName)){
                                        serListName.add(serviceNumbersModelArrayList.get(i));
                                    }
                                }

                                showServiceSearchBottomSheet(adapterPosition,true, serListName);
                            }
                        });
                        break;
                }
                //Toast.makeText(MeterSurveyForm.this, "Selected: " + selected, Toast.LENGTH_SHORT).show();
            }
        });


        dialog.setOnShowListener(dialogInterface -> {
            View bottomSheet = dialog.findViewById(
                    R.id.design_bottom_sheet
            );

            if (bottomSheet != null) {
                bottomSheet.setBackgroundResource(R.drawable.bg_bottom_sheet_rounded);

                BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);
                bottomSheet.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                behavior.setDraggable(false);
                behavior.setSkipCollapsed(true);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);


            }
        });
        dialog.show();
    }



    public class SearchServicesAdapter extends RecyclerView.Adapter<SearchServicesAdapter.MyViewHolder> {

        ArrayList<ServiceNumbersModel> list;
        int adapterPosition;
        BottomSheetDialog dialog;

        public void updateList(ArrayList<ServiceNumbersModel> newList){
            this.list = new ArrayList<>(newList);
            notifyDataSetChanged();
        }

        public SearchServicesAdapter(ArrayList<ServiceNumbersModel> list, int adapterPosition, BottomSheetDialog dialog) {
            this.list = list;
            this.adapterPosition = adapterPosition;
            this.dialog = dialog;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MeterSurveyForm.this).inflate(R.layout.service_search_adapter, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.search_tv_service_number.setText(list.get(position).getSERVICE_NUMBER());
            holder.search_tv_consumer_name.setText(list.get(position).getCONSUMER_NAME());
            holder.search_tv_meter_number.setText("Meter No: "+list.get(position).getMETER_NO());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ///CURRENT LIST CHECK
                    for (int i = 0; i < serviceList.size(); i++) {
                        if (serviceList.get(i).getServiceNumber().equalsIgnoreCase(list.get(position).getSERVICE_NUMBER())){
                            Toast.makeText(MeterSurveyForm.this, "Service Number Already added", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    ///DB CHECK
                    MeterSurveyDatabase database = new MeterSurveyDatabase(MeterSurveyForm.this);
                    List<MeterPostingModel> allData = database.getAllMeterSurveyData();
                    for (int i = 0; i < allData.size(); i++) {
                        if (allData.get(i).getSERVICE_NUMBER().equalsIgnoreCase(list.get(position).getSERVICE_NUMBER())){
                            Toast.makeText(MeterSurveyForm.this, "Service Number Already added", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    serviceList.get(adapterPosition).setServiceNumber(list.get(position).getSERVICE_NUMBER());
                    serviceList.get(adapterPosition).setServiceName(list.get(position).getCONSUMER_NAME());
                    serviceList.get(adapterPosition).setUkscNumber(list.get(position).getUKSC_NUMBER());
                    adapter.notifyItemChanged(adapterPosition);
                    dialog.dismiss();
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView search_tv_service_number,search_tv_consumer_name,search_tv_meter_number;
            ImageView search_forward_icon;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                search_tv_service_number = itemView.findViewById(R.id.search_tv_service_number);
                search_tv_consumer_name = itemView.findViewById(R.id.search_tv_consumer_name);
                search_forward_icon = itemView.findViewById(R.id.search_forward_icon);
                search_tv_meter_number = itemView.findViewById(R.id.search_tv_meter_number);
            }
        }
    }



}
