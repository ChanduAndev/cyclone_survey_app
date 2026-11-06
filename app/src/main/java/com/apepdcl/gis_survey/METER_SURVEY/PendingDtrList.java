package com.apepdcl.gis_survey.METER_SURVEY;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.Constants.MobileConstants;

import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by SOUBHAGYA SWAIN on 08-06-2026.
 */
public class PendingDtrList extends AppCompatActivity {

    ImageView back_layout;

    ArrayList<AssignedDtrModel> list;
    PendingDtrAdapter adapter;
    RecyclerView dtr_rv;
    LinearLayout main_ui_layout;
    LinearLayout loader_layout;

    ProgressDialog progressDialog;

    private RetrofitServiceMeter retrofitServiceMeter;

    private Uri imageUri;
    ActivityResultLauncher<Uri> cameraLauncher;
    ImageView captured_image;

    boolean isImageCaptured;
    List<String> typeList;
    private boolean isDeicatedDTR = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_dtr_list);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setButton("Cancel", (dialog, which) -> {
            progressDialog.dismiss();
        });
        progressDialog.setCancelable(false);

        typeList = new ArrayList<>();
        typeList.add("Select");

        back_layout = findViewById(R.id.back_layout);
        dtr_rv = findViewById(R.id.dtr_rv);
        main_ui_layout = findViewById(R.id.main_ui_layout);
        loader_layout = findViewById(R.id.loader_layout);
        back_layout.setOnClickListener(view -> onBackPressed());

        list = new ArrayList<>();
        if (ConnectivityChecking.checkConnection(PendingDtrList.this)) {
            getPendingDTRList();
            //getStaticPendingDTRList("103");
        } else {
            CommanUtilits.internetDialog(PendingDtrList.this);
        }

        callDTROptionsAPI();

        cameraLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.TakePicture(),
                        result -> {
                            if (result) {
                                isImageCaptured = true;
                                captured_image.setImageURI(imageUri);
                            }
                        });
    }


    private void getPendingDTRList() {

        loader_layout.setVisibility(View.VISIBLE);
        main_ui_layout.setVisibility(View.GONE);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("MOBILE_NO", sharedPreferences.getString(MeterConstants.METER_MOBILE_NO, ""));
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
                    Toast.makeText(PendingDtrList.this, "Server Busy. Try Again", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 🔹 CLEAR LISTS
                list.clear();

                JsonObject surveyedResponseVo = response.body();

                if (!surveyedResponseVo.has("STATUS") || surveyedResponseVo.get("STATUS").isJsonNull()) {
                    Toast.makeText(PendingDtrList.this,
                            "Invalid response from server: STATUS missing",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                    String errorMessage = surveyedResponseVo.has("ERR_MSG")
                            ? surveyedResponseVo.get("ERR_MSG").getAsString()
                            : "Unknown error occurred";
                    Toast.makeText(PendingDtrList.this, errorMessage, Toast.LENGTH_SHORT).show();
                    return;
                }

                //JsonObject object = surveyedResponseVo.getAsJsonObject("DATA");
                JsonArray dtr_array = surveyedResponseVo.getAsJsonArray("DATA");

                for (int i = 0; i < dtr_array.size(); i++) {

                    JsonObject jsonObject1 = dtr_array.get(i).getAsJsonObject();
                    String isCompleted = jsonObject1.has("SURVEY_COMPLETED") && !jsonObject1.get("SURVEY_COMPLETED").isJsonNull()
                            ? jsonObject1.get("SURVEY_COMPLETED").getAsString()
                            : "";

                    if (isCompleted.equals("Y")) {
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
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PendingDtrList.this);
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
            View view = LayoutInflater.from(PendingDtrList.this).inflate(R.layout.dtr_list_adapter, parent, false);
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

            int total_scs = Integer.parseInt(list.get(position).getNO_OF_SCS());
            if (total_scs > 0) {
                holder.btn_start.setText("Continue Survey");
            } else {
                holder.btn_start.setText("Start Survey");
            }


            holder.btn_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (list.get(position).getIS_LTCKT_AVAILABLE().equalsIgnoreCase("NO") && list.get(position).getLOAD_TYPE_SURVEYED().isEmpty()) {
                       // openWithoutPolesDialog(list.get(position), position);
                        isDeicatedDTR = true;
                    } else if (list.get(position).getIS_LTCKT_AVAILABLE().equalsIgnoreCase("NO") && !list.get(position).getLOAD_TYPE_SURVEYED().isEmpty()) {
                        //getAreaCodeBySecId(list.get(position),true);
                       // openAlertDialogForLoadServices(position);
                        isDeicatedDTR = true;
                        //getServiceNumbersForApartmentDTR(list.get(position), true);
                    } else {
                       // getPolesForDTR(list.get(position), position);
                        isDeicatedDTR = false;
                    }

                    if (list.get(position).getLOAD_TYPE_SURVEYED().isEmpty()){
                        /// CHANGED HERE
                        //openWithoutPolesDialog(list.get(position), position);
                        openAlertDialogForLoadServices(position);
                       // getPolesForDTR(list.get(position), position);
                    }else {
                        getPolesForDTR(list.get(position), position);
                    }



                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView sl_no, tv_dtr_location_name, tv_dtr_code, tv_no_of_services, tv_section;
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

    private void openWithoutPolesDialog(AssignedDtrModel selectedDTR, int position) {
        Dialog payDialog = new Dialog(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        payDialog.setContentView(R.layout.dtr_details_dialog);
        payDialog.getWindow().setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        payDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        payDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        payDialog.setCancelable(false);
        TextView cancel_button = payDialog.findViewById(R.id.cancel_button);
        TextView proceed_button = payDialog.findViewById(R.id.proceed_button);
        TextView btn_capture_image = payDialog.findViewById(R.id.btn_capture_image);
        captured_image = payDialog.findViewById(R.id.captured_image);
        Spinner sp_dtr_type = payDialog.findViewById(R.id.sp_dtr_type);
        EditText enter_dtr_type = payDialog.findViewById(R.id.enter_dtr_type);
        LinearLayout layout_dtr_type_options=payDialog.findViewById(R.id.layout_dtr_type_options);

        if (selectedDTR.getIS_LTCKT_AVAILABLE().equalsIgnoreCase("NO")){
            layout_dtr_type_options.setVisibility(View.VISIBLE);
        }else {
            layout_dtr_type_options.setVisibility(View.GONE);
        }


        ArrayAdapter<String> adapter_pst = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, typeList);
        sp_dtr_type.setAdapter(adapter_pst);

        sp_dtr_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (sp_dtr_type.getSelectedItem().toString().equalsIgnoreCase("Others")) {
                    enter_dtr_type.setVisibility(View.VISIBLE);
                } else {
                    enter_dtr_type.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_capture_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "DtrImage_" + System.currentTimeMillis());
                imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                cameraLauncher.launch(imageUri);
            }
        });

        proceed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isImageCaptured) {
                    Toast.makeText(PendingDtrList.this, "Please Capture DTR Image", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (layout_dtr_type_options.getVisibility()==View.VISIBLE){
                    if (sp_dtr_type.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(PendingDtrList.this, "Please Select DTR type", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (sp_dtr_type.getSelectedItem().toString().equalsIgnoreCase("Others") && enter_dtr_type.getText().toString().isEmpty()) {
                    Toast.makeText(PendingDtrList.this, "Please Enter DTR type", Toast.LENGTH_SHORT).show();
                    return;
                }

                payDialog.dismiss();
                getDbmsAccessToken(selectedDTR, sp_dtr_type.getSelectedItem().toString(), position);

            }
        });


        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payDialog.dismiss();
            }
        });

        payDialog.show();
    }

    private void getDbmsAccessToken(AssignedDtrModel selectedDTR, String dtrType, int position) {
        progressDialog.show();
        retrofitServiceMeter = RetrofitClientInstanceMeter.getRetrofitInstance().create(RetrofitServiceMeter.class);

        Call<JsonObject> responseVoCall = retrofitServiceMeter.getImageAccessToken();

        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                if (response.body() == null) {
                    progressDialog.dismiss();
                    Toast.makeText(PendingDtrList.this, "Server Busy. Try Again.", Toast.LENGTH_SHORT).show();
                    return;
                }


                JsonObject surveyedResponseVo = response.body();

                if (!surveyedResponseVo.has("STATUS") || surveyedResponseVo.get("STATUS").isJsonNull()) {
                    Toast.makeText(PendingDtrList.this, "Invalid response from server: STATUS missing", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }

                if (!MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                    String errorMessage = surveyedResponseVo.has("ERR_MSG")
                            ? surveyedResponseVo.get("ERR_MSG").getAsString()
                            : "Unknown error occurred";
                    Toast.makeText(PendingDtrList.this, errorMessage, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }
                if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                    //Toast.makeText(PendingDtrList.this, surveyedResponseVo.get(), Toast.LENGTH_SHORT).show();
                    //JsonObject object = surveyedResponseVo.getAsJsonObject("DATA");
                    String accessToken = surveyedResponseVo.get("DATA").getAsString();
                    callSaveDtrImageAPI(selectedDTR, dtrType, accessToken, position);
                }

//                //JsonObject object = surveyedResponseVo.getAsJsonObject("DATA");
//                String accessToken = surveyedResponseVo.get("DATA").getAsString();
//                callSaveDtrImageAPI(selectedDTR, dtrType, accessToken,position);

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }


    public void callSaveDtrImageAPI(AssignedDtrModel selectedDTR, String dtrType, String accessToken, int position) {

        try {
            File file = getFileFromUri(PendingDtrList.this, imageUri);

            RequestBody fileRequestBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("upload_file", file.getName(), fileRequestBody);
            RequestBody tokenBody = RequestBody.create(MultipartBody.FORM, accessToken);
            RequestBody accountBody = RequestBody.create(MultipartBody.FORM, "2");
            RequestBody folderBody = RequestBody.create(MultipartBody.FORM, "okm:root/GIS_SrvCns_Images");

            ImageAPIServiceMeter apiService = ApiClient.getClient().create(ImageAPIServiceMeter.class);

            apiService.uploadImage(tokenBody, accountBody, folderBody, imagePart).enqueue(new Callback<JsonObject>() {

                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.d("Image Response", response.body().toString());
                    if (response.isSuccessful()) {

                        JsonObject resObject = response.body();
                        JsonArray jsonArray = resObject.getAsJsonArray("data");
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonObject object = jsonArray.get(i).getAsJsonObject();
                            String short_code = object.get("short_url").getAsString();
                            callSaveDtrOptionAPI(selectedDTR, dtrType, short_code, position);
                        }

                    } else {
                        progressDialog.dismiss();
                        Log.e("UPLOAD", "Error Code : " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.e("UPLOAD", "Failed : " + t.getMessage());
                }
            });

        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    private void callSaveDtrOptionAPI(AssignedDtrModel selectedDTR, String dtrType, String shortCode, int position) {
        JsonObject dtrDetailsReqestVo = new JsonObject();
        dtrDetailsReqestVo.addProperty("DTR_CODE", selectedDTR.getDTR_CODE());
        dtrDetailsReqestVo.addProperty("REC_ID", selectedDTR.getREC_ID());
        dtrDetailsReqestVo.addProperty("LOAD_TYPE_SURVEYED", dtrType);
        dtrDetailsReqestVo.addProperty("IMAGE", shortCode);

        retrofitServiceMeter = RetrofitClientInstanceMeter.getRetrofitInstance().create(RetrofitServiceMeter.class);
        Call<JsonObject> responseVoCall = retrofitServiceMeter.saveDTROption(dtrDetailsReqestVo);

        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    //Log.v("FEEDER==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        //getServiceNumbersForApartmentDTR(selectedDTR, false);
                        openAlertDialogForLoadServices(position);
                        // getAreaCodeBySecId(selectedDTR, false);
                    } else {
                        progressDialog.dismiss();
                        openAlertDialog(surveyedResponseVo.get("ERR_MSG").toString());
                        // Toast.makeText(PendingDtrList.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    progressDialog.dismiss();
                    Toast.makeText(PendingDtrList.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PendingDtrList.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void openAlertDialog(String message) {
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
        title.setText("Pay Attention!!");
        msg.setText(message);

        // Show only OK button
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.GONE);
        okBtn.setText("Okay");

        // Click action
        okBtn.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.setCancelable(false);

        // Transparent background
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        dialog.show();
    }

    private void openAlertDialogForLoadServices(int position) {
        Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.customized_dialog_new, null);
        dialog.setContentView(view);

        ImageView iv_icon = view.findViewById(R.id.iv_icon);
        RadioGroup rgServiceType = view.findViewById(R.id.rgServiceType);
        TextView btnContinue = view.findViewById(R.id.btnContinue);
        TextView btnCancel = view.findViewById(R.id.btnCancel);
        iv_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.warning));
        dialog.setCancelable(false);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        btnContinue.setOnClickListener(v -> {
            int selectedId = rgServiceType.getCheckedRadioButtonId();

            if (selectedId == -1) {
                Toast.makeText(this, "Please select an option", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedId == R.id.rbLoadAllServices) {
                getServiceNumbersForApartmentDTR(list.get(position), true);
            } else if (selectedId == R.id.rbLoadAreaWiseServices) {
                getAreaCodeBySecId(list.get(position), true);
            }

            dialog.dismiss();
        });

        dialog.show();
    }


    private void getPolesForDTR(AssignedDtrModel dtrFeederMaster, int position) {
        progressDialog.show();
        MeterConstants.selectedPolesList.clear();

        JsonObject dtrDetailsReqestVo = new JsonObject();
        dtrDetailsReqestVo.addProperty("DTR_CODE", dtrFeederMaster.getDTR_CODE());

        /*retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        Call<JsonObject> responseVoCall = retrofitService.getFeederMasterNew(dtrDetailsReqestVo);*/


        retrofitServiceMeter = RetrofitClientInstanceMeter.getRetrofitInstance().create(RetrofitServiceMeter.class);
        Call<JsonObject> responseVoCall = retrofitServiceMeter.getDTrPoleData(dtrDetailsReqestVo);

        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                 progressDialog.dismiss();
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    //Log.v("FEEDER==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {
                            JsonArray dataArray = surveyedResponseVo.get("DATA").getAsJsonArray();
                            ArrayList<PolesModel> polesList = new ArrayList<>();
                            for (int i = 0; i < dataArray.size(); i++) {
                                PolesModel pole1 = new PolesModel();
                                JsonObject obj = dataArray.get(i).getAsJsonObject();

                                pole1.setPole_id(
                                        obj.has("EXT_ID") && !obj.get("EXT_ID").isJsonNull()
                                                ? obj.get("EXT_ID").getAsString()
                                                : "");

                                pole1.setPole_no(
                                        obj.has("POLE_NO") && !obj.get("POLE_NO").isJsonNull()
                                                ? obj.get("POLE_NO").getAsString()
                                                : "");

                                pole1.setPole_type(
                                        obj.has("POLE_TYPE") && !obj.get("POLE_TYPE").isJsonNull()
                                                ? obj.get("POLE_TYPE").getAsString()
                                                : "");

                                pole1.setPole_height(
                                        obj.has("POLE_HEIGHT") && !obj.get("POLE_HEIGHT").isJsonNull()
                                                ? obj.get("POLE_HEIGHT").getAsString()
                                                : "");

                                pole1.setPole_latitude(
                                        obj.has("GLAT") && !obj.get("GLAT").isJsonNull()
                                                ? obj.get("GLAT").getAsString()
                                                : "");

                                pole1.setPole_longitude(
                                        obj.has("GLONG") && !obj.get("GLONG").isJsonNull()
                                                ? obj.get("GLONG").getAsString()
                                                : "");

                                pole1.setLt_ckt_code(
                                        obj.has("LT_CKT_CODE") && !obj.get("LT_CKT_CODE").isJsonNull()
                                                ? obj.get("LT_CKT_CODE").getAsString()
                                                : "");
                                pole1.setPOLE_IMG_AVAILABLE(obj.has("POLE_IMG_AVAILABLE") && !obj.get("POLE_IMG_AVAILABLE").isJsonNull()
                                        ? obj.get("POLE_IMG_AVAILABLE").getAsString()
                                        : "");
                                pole1.setDtr_code(dtrFeederMaster.getDTR_CODE());
                                pole1.setDtr_name(dtrFeederMaster.getDTR_LOCATION());
                                polesList.add(pole1);
                            }
                            MeterConstants.selectedPolesList.addAll(polesList);
                            openAlertDialogForLoadServices(position);
                            //getAreaCodeBySecId(dtrFeederMaster,true);
                            // getServiceNumbersForDTR(dtrFeederMaster);

                        } catch (Exception e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(PendingDtrList.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    progressDialog.dismiss();
                    Toast.makeText(PendingDtrList.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PendingDtrList.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getServiceNumbersForDTR(AssignedDtrModel dtrFeederMaster) {
        // progressDialog.show();
        MeterConstants.allServiceNumbers.clear();
        MeterConstants.serviceNumbersList.clear();

        JsonObject dtrDetailsReqestVo = new JsonObject();
        dtrDetailsReqestVo.addProperty("SECTION_ID", dtrFeederMaster.getSECTION_ID());

        retrofitServiceMeter = RetrofitClientInstanceMeter.getRetrofitInstance().create(RetrofitServiceMeter.class);
        Call<JsonObject> responseVoCall = retrofitServiceMeter.getServiceConsumersData(dtrDetailsReqestVo);

        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    //Log.v("FEEDER==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {
                            JsonArray dataArray = surveyedResponseVo.get("DATA").getAsJsonArray();
                            ArrayList<ServiceNumbersModel> serviceNumbersList = new ArrayList<>();
                            for (int i = 0; i < dataArray.size(); i++) {
                                MeterConstants.allServiceNumbers.add(dataArray.get(i).getAsJsonObject().get("CONS_NO").getAsString());
                                ServiceNumbersModel model = new ServiceNumbersModel();
//                                model.setSERVICE_NUMBER(dataArray.get(i).getAsJsonObject().get("CONS_NO").getAsString());
//                                model.setAREA_CODE(dataArray.get(i).getAsJsonObject().get("AREA_CODE").getAsString());
//                                model.setAREA_NAME(dataArray.get(i).getAsJsonObject().get("AREA_NAME").getAsString());
//                                model.setCONSUMER_NAME(dataArray.get(i).getAsJsonObject().get("CONS_NAME").getAsString());
//                                model.setUKSC_NUMBER(dataArray.get(i).getAsJsonObject().get("UKSCNO").getAsString());
//                                model.setMETER_NO(dataArray.get(i).getAsJsonObject().get("METER_NO").getAsString());

                                JsonObject obj = dataArray.get(i).getAsJsonObject();

                                model.setSERVICE_NUMBER(
                                        obj.has("CONS_NO") && !obj.get("CONS_NO").isJsonNull()
                                                ? obj.get("CONS_NO").getAsString()
                                                : "");

                                model.setAREA_CODE(
                                        obj.has("AREA_CODE") && !obj.get("AREA_CODE").isJsonNull()
                                                ? obj.get("AREA_CODE").getAsString()
                                                : "");

                                model.setAREA_NAME(
                                        obj.has("AREA_NAME") && !obj.get("AREA_NAME").isJsonNull()
                                                ? obj.get("AREA_NAME").getAsString()
                                                : "");

                                model.setCONSUMER_NAME(
                                        obj.has("CONS_NAME") && !obj.get("CONS_NAME").isJsonNull()
                                                ? obj.get("CONS_NAME").getAsString()
                                                : "");

                                model.setUKSC_NUMBER(
                                        obj.has("UKSCNO") && !obj.get("UKSCNO").isJsonNull()
                                                ? obj.get("UKSCNO").getAsString()
                                                : "");

                                model.setMETER_NO(
                                        obj.has("METER_NO") && !obj.get("METER_NO").isJsonNull()
                                                ? obj.get("METER_NO").getAsString()
                                                : "");
                                serviceNumbersList.add(model);
                            }
                            MeterConstants.serviceNumbersList.addAll(serviceNumbersList);
                            Intent intent = new Intent(PendingDtrList.this, MeterSurveyMap.class);
                            intent.putExtra("DTR_DATA", dtrFeederMaster);
                            startActivity(intent);
                            finish();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {

                        Toast.makeText(PendingDtrList.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(PendingDtrList.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PendingDtrList.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void getSurveyPoleData(AssignedDtrModel dtrFeederMaster) {
        JsonObject dtrDetailsReqestVo = new JsonObject();
        dtrDetailsReqestVo.addProperty("DTR_CODE", dtrFeederMaster.getDTR_CODE());
        retrofitServiceMeter = RetrofitClientInstanceMeter.getRetrofitInstance().create(RetrofitServiceMeter.class);
        Call<JsonObject> responseVoCall = retrofitServiceMeter.getSurveyData(dtrDetailsReqestVo);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    MeterConstants.alreadyMappedList.clear();

                    JsonObject surveyedResponseVo = response.body();
                    //Log.v("FEEDER==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {
                            JsonArray dataArray = surveyedResponseVo.get("DATA").getAsJsonArray();
                            ArrayList<MeterPostingModel> surveyedPoleData = new ArrayList<>();
                            for (int i = 0; i < dataArray.size(); i++) {

                                MeterPostingModel model = new MeterPostingModel();
                                JsonObject obj = dataArray.get(i).getAsJsonObject();

                                model.setPOLE_ID(
                                        obj.has("POLE_ID") && !obj.get("POLE_ID").isJsonNull()
                                                ? obj.get("POLE_ID").getAsString()
                                                : "");


                                model.setUKSC_NO(
                                        obj.has("UKSCNO") && !obj.get("UKSCNO").isJsonNull()
                                                ? obj.get("UKSCNO").getAsString()
                                                : "");
                                model.setSC_NAME(
                                        obj.has("SC_NAME") && !obj.get("SC_NAME").isJsonNull()
                                                ? obj.get("SC_NAME").getAsString()
                                                : "");
                                model.setSERVICE_NUMBER(
                                        obj.has("SCNO") && !obj.get("SCNO").isJsonNull()
                                                ? obj.get("SCNO").getAsString()
                                                : "");
                                model.setSURVEY_MODE(
                                        obj.has("SURVEY_MODE") && !obj.get("SURVEY_MODE").isJsonNull()
                                                ? obj.get("SURVEY_MODE").getAsString()
                                                : "");


                                model.setLONGITUDE(
                                        obj.has("SC_GLONG") && !obj.get("SC_GLONG").isJsonNull()
                                                ? obj.get("SC_GLONG").getAsString()
                                                : "");

                                model.setLATITUDE(
                                        obj.has("SC_GLAT") && !obj.get("SC_GLAT").isJsonNull()
                                                ? obj.get("SC_GLAT").getAsString()
                                                : "");

                                model.setDTR_CODE(
                                        obj.has("DTR_CODE") && !obj.get("DTR_CODE").isJsonNull()
                                                ? obj.get("DTR_CODE").getAsString()
                                                : "");

                                model.setLT_CKT_CODE(
                                        obj.has("LT_CKT_CODE") && !obj.get("LT_CKT_CODE").isJsonNull()
                                                ? obj.get("LT_CKT_CODE").getAsString()
                                                : "");

                                model.setCURRENT_LAT(
                                        obj.has("CURRENT_LAT") && !obj.get("CURRENT_LAT").isJsonNull()
                                                ? obj.get("CURRENT_LAT").getAsString()
                                                : "");

                                model.setCURRENT_LON(
                                        obj.has("CURRENT_LONG") && !obj.get("CURRENT_LONG").isJsonNull()
                                                ? obj.get("CURRENT_LONG").getAsString()
                                                : "");

                                model.setDEVICE_ID(
                                        obj.has("DEVICE_ID") && !obj.get("DEVICE_ID").isJsonNull()
                                                ? obj.get("DEVICE_ID").getAsString()
                                                : "");
                                surveyedPoleData.add(model);
                            }
                            MeterConstants.alreadyMappedList.addAll(surveyedPoleData);
                            if (isDeicatedDTR){
                                Intent intent = new Intent(PendingDtrList.this, OnlyMeterSurveyMap.class);
                                intent.putExtra("DTR_DATA", dtrFeederMaster);
                                startActivity(intent);
                                finish();
                            }else {
                                Intent intent = new Intent(PendingDtrList.this, MeterSurveyMap.class);
                                intent.putExtra("DTR_DATA", dtrFeederMaster);
                                startActivity(intent);
                                finish();
                            }



                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {

                        Toast.makeText(PendingDtrList.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(PendingDtrList.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PendingDtrList.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void getServiceNumbersForApartmentDTR(AssignedDtrModel dtrFeederMaster, boolean showProgress) {

        if (showProgress) {
            progressDialog.show();
        }

        MeterConstants.allServiceNumbers.clear();
        MeterConstants.serviceNumbersList.clear();

        JsonObject dtrDetailsReqestVo = new JsonObject();
        dtrDetailsReqestVo.addProperty("SECTION_ID", dtrFeederMaster.getSECTION_ID());

        retrofitServiceMeter = RetrofitClientInstanceMeter.getRetrofitInstance().create(RetrofitServiceMeter.class);
        Call<JsonObject> responseVoCall = retrofitServiceMeter.getServiceConsumersData(dtrDetailsReqestVo);

        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    //Log.v("FEEDER==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {
                            JsonArray dataArray = surveyedResponseVo.get("DATA").getAsJsonArray();
                            ArrayList<ServiceNumbersModel> serviceNumbersList = new ArrayList<>();
                            for (int i = 0; i < dataArray.size(); i++) {
                                MeterConstants.allServiceNumbers.add(dataArray.get(i).getAsJsonObject().get("CONS_NO").getAsString());
                                ServiceNumbersModel model = new ServiceNumbersModel();
//                                model.setSERVICE_NUMBER(dataArray.get(i).getAsJsonObject().get("CONS_NO").getAsString());
//                                model.setAREA_CODE(dataArray.get(i).getAsJsonObject().get("AREA_CODE").getAsString());
//                                model.setAREA_NAME(dataArray.get(i).getAsJsonObject().get("AREA_NAME").getAsString());
//                                model.setCONSUMER_NAME(dataArray.get(i).getAsJsonObject().get("CONS_NAME").getAsString());
//                                model.setUKSC_NUMBER(dataArray.get(i).getAsJsonObject().get("UKSCNO").getAsString());
//                                model.setMETER_NO(dataArray.get(i).getAsJsonObject().get("METER_NO").getAsString());
                                JsonObject obj = dataArray.get(i).getAsJsonObject();

                                model.setSERVICE_NUMBER(
                                        obj.has("CONS_NO") && !obj.get("CONS_NO").isJsonNull()
                                                ? obj.get("CONS_NO").getAsString()
                                                : "");

                                model.setAREA_CODE(
                                        obj.has("AREA_CODE") && !obj.get("AREA_CODE").isJsonNull()
                                                ? obj.get("AREA_CODE").getAsString()
                                                : "");

                                model.setAREA_NAME(
                                        obj.has("AREA_NAME") && !obj.get("AREA_NAME").isJsonNull()
                                                ? obj.get("AREA_NAME").getAsString()
                                                : "");

                                model.setCONSUMER_NAME(
                                        obj.has("CONS_NAME") && !obj.get("CONS_NAME").isJsonNull()
                                                ? obj.get("CONS_NAME").getAsString()
                                                : "");

                                model.setUKSC_NUMBER(
                                        obj.has("UKSCNO") && !obj.get("UKSCNO").isJsonNull()
                                                ? obj.get("UKSCNO").getAsString()
                                                : "");

                                model.setMETER_NO(
                                        obj.has("METER_NO") && !obj.get("METER_NO").isJsonNull()
                                                ? obj.get("METER_NO").getAsString()
                                                : "");
                                serviceNumbersList.add(model);
                            }

                            MeterConstants.serviceNumbersList.addAll(serviceNumbersList);
                            if (isDeicatedDTR) {
                                getSurveyPoleData(dtrFeederMaster);


                            } else {
                                getSurveyPoleData(dtrFeederMaster);

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {

                        Toast.makeText(PendingDtrList.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(PendingDtrList.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PendingDtrList.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void getAreaCodeBySecId(AssignedDtrModel dtrFeederMaster, boolean showProgress) {

        if (showProgress) {
            progressDialog.show();
        }

        MeterConstants.allServiceNumbers.clear();
        MeterConstants.serviceNumbersList.clear();

        JsonObject dtrDetailsReqestVo = new JsonObject();
        dtrDetailsReqestVo.addProperty("SECTION_ID", dtrFeederMaster.getSECTION_ID());

        retrofitServiceMeter = RetrofitClientInstanceMeter.getRetrofitInstance().create(RetrofitServiceMeter.class);
        Call<JsonObject> responseVoCall = retrofitServiceMeter.getAreaCodesBySectionId(dtrDetailsReqestVo);

        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    //Log.v("FEEDER==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {
                            JsonArray dataArray = surveyedResponseVo.get("DATA").getAsJsonArray();

                            ArrayList<AreaCodeModel> areaWiseCodesList = new ArrayList<>();

                            for (int i = 0; i < dataArray.size(); i++) {

                                JsonObject obj = dataArray.get(i).getAsJsonObject();

                                areaWiseCodesList.add(
                                        new AreaCodeModel(
                                                obj.get("AREA_CODE").getAsString(),
                                                obj.get("AREA_NAME").getAsString()
                                        )
                                );
                            }

                            // Open BottomSheet here
                            showAreaBottomSheet(dtrFeederMaster, areaWiseCodesList);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {

                        Toast.makeText(PendingDtrList.this, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(PendingDtrList.this, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PendingDtrList.this, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void getServiceConsumersDataByAreaCode(AssignedDtrModel dtrFeederMaster, JsonObject request, boolean showProgress) {

        if (showProgress) {
            progressDialog.show();
        }

        MeterConstants.allServiceNumbers.clear();
        MeterConstants.serviceNumbersList.clear();

        retrofitServiceMeter = RetrofitClientInstanceMeter
                .getRetrofitInstance()
                .create(RetrofitServiceMeter.class);

        Call<JsonObject> responseVoCall =
                retrofitServiceMeter.getServiceConsumersDataByAreaCode(request);

        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                progressDialog.dismiss();

                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();

                    if (MobileConstants.TRUE.equalsIgnoreCase(
                            surveyedResponseVo.get("STATUS").getAsString())) {

                        try {
                            JsonArray dataArray = surveyedResponseVo.get("DATA").getAsJsonArray();
                            ArrayList<ServiceNumbersModel> serviceNumbersList = new ArrayList<>();
                            for (int i = 0; i < dataArray.size(); i++) {
                                MeterConstants.allServiceNumbers.add(dataArray.get(i).getAsJsonObject().get("CONS_NO").getAsString());
                                ServiceNumbersModel model = new ServiceNumbersModel();
                                JsonObject obj = dataArray.get(i).getAsJsonObject();

                                model.setSERVICE_NUMBER(
                                        obj.has("CONS_NO") && !obj.get("CONS_NO").isJsonNull()
                                                ? obj.get("CONS_NO").getAsString()
                                                : "");

                                model.setAREA_CODE(
                                        obj.has("AREA_CODE") && !obj.get("AREA_CODE").isJsonNull()
                                                ? obj.get("AREA_CODE").getAsString()
                                                : "");

                                model.setAREA_NAME(
                                        obj.has("AREA_NAME") && !obj.get("AREA_NAME").isJsonNull()
                                                ? obj.get("AREA_NAME").getAsString()
                                                : "");

                                model.setCONSUMER_NAME(
                                        obj.has("CONS_NAME") && !obj.get("CONS_NAME").isJsonNull()
                                                ? obj.get("CONS_NAME").getAsString()
                                                : "");

                                model.setUKSC_NUMBER(
                                        obj.has("UKSCNO") && !obj.get("UKSCNO").isJsonNull()
                                                ? obj.get("UKSCNO").getAsString()
                                                : "");

                                model.setMETER_NO(
                                        obj.has("METER_NO") && !obj.get("METER_NO").isJsonNull()
                                                ? obj.get("METER_NO").getAsString()
                                                : "");
                                serviceNumbersList.add(model);
                            }
                            MeterConstants.serviceNumbersList.addAll(serviceNumbersList);
                            if (isDeicatedDTR) {
                                getSurveyPoleData(dtrFeederMaster);
//                                MeterConstants.serviceNumbersList.addAll(serviceNumbersList);
//                                Intent intent = new Intent(PendingDtrList.this, OnlyMeterSurveyMap.class);
//                                intent.putExtra("DTR_DATA", dtrFeederMaster);
//                                startActivity(intent);
//                                finish();

                            } else {
                                getSurveyPoleData(dtrFeederMaster);

                            }


//                            Intent intent = new Intent(PendingDtrList.this, MeterSurveyMap.class);
//                            intent.putExtra("DTR_DATA", dtrFeederMaster);
//                            startActivity(intent);
//                            finish();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {

                        Toast.makeText(PendingDtrList.this,
                                surveyedResponseVo.get("ERR_MSG").getAsString(),
                                Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(PendingDtrList.this,
                            "Server Busy Try Again",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                progressDialog.dismiss();

                Toast.makeText(PendingDtrList.this,
                        "Try Again " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showAreaBottomSheet(AssignedDtrModel dtrFeederMaster, ArrayList<AreaCodeModel> areaList) {

        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = getLayoutInflater()
                .inflate(R.layout.bottom_sheet_area_codes, null);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerAreas);
        TextView btnApply = view.findViewById(R.id.btnApply);
        SearchView searchView = view.findViewById(R.id.searchView);
        TextView tvSelectedCount = view.findViewById(R.id.tvSelectedCount);
        searchView.setQueryHint("Search your area code here");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AreaCodeAdapter adapter = new AreaCodeAdapter(areaList);

        adapter.setOnSelectionChangedListener(count -> {

            if (count == 0) {
                tvSelectedCount.setVisibility(View.GONE);
                //tvSelectedCount.setText("No Area Selected");
            } else {
                tvSelectedCount.setVisibility(View.VISIBLE);
                tvSelectedCount.setText(count + " Area Selected");
            }

        });
        dialog.setOnShowListener(dialogInterface -> {

            BottomSheetDialog sheetDialog = (BottomSheetDialog) dialogInterface;

            FrameLayout bottomSheet =
                    sheetDialog.findViewById(
                            com.google.android.material.R.id.design_bottom_sheet);

            if (bottomSheet != null) {

                ViewGroup.LayoutParams params = bottomSheet.getLayoutParams();
                params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                bottomSheet.setLayoutParams(params);

                BottomSheetBehavior<View> behavior =
                        BottomSheetBehavior.from(bottomSheet);

                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setSkipCollapsed(true);
            }
        });

        recyclerView.setAdapter(adapter);

        tvSelectedCount.setText("No Area Selected");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                adapter.filter(query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.filter(newText);

                return true;
            }
        });

        btnApply.setOnClickListener(v -> {

            JsonArray array = new JsonArray();

            for (String code : adapter.getSelectedCodes()) {

                array.add(code);

            }

            JsonObject jsonObject = new JsonObject();
            jsonObject.add("AREA_CODES", array);


            getServiceConsumersDataByAreaCode(dtrFeederMaster, jsonObject, true);
            Log.e("AREA_JSON", jsonObject.toString());

            dialog.dismiss();

        });

        dialog.setContentView(view);
        dialog.show();
    }


    private void callDTROptionsAPI() {

        retrofitServiceMeter = RetrofitClientInstanceMeter.getRetrofitInstance().create(RetrofitServiceMeter.class);

        Call<JsonObject> responseVoCall = retrofitServiceMeter.getDTROptions();

        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                if (response.body() == null) {
                    Toast.makeText(PendingDtrList.this, "Server Busy. Try Again.", Toast.LENGTH_SHORT).show();
                    return;
                }


                JsonObject surveyedResponseVo = response.body();

                if (!surveyedResponseVo.has("STATUS") || surveyedResponseVo.get("STATUS").isJsonNull()) {
                    Toast.makeText(PendingDtrList.this, "Invalid response from server: STATUS missing", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                    String errorMessage = surveyedResponseVo.has("ERR_MSG")
                            ? surveyedResponseVo.get("ERR_MSG").getAsString()
                            : "Unknown error occurred";
                    Toast.makeText(PendingDtrList.this, errorMessage, Toast.LENGTH_SHORT).show();
                    return;
                }

                JsonArray array = surveyedResponseVo.getAsJsonArray("DATA");
                for (int i = 0; i < array.size(); i++) {
                    typeList.add(array.get(i).getAsString());
                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }


    public File getFileFromUri(Context context, Uri uri) throws IOException {

        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        File file = new File(context.getCacheDir(), "upload_" + System.currentTimeMillis() + ".jpg");
        FileOutputStream outputStream = new FileOutputStream(file);

        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        outputStream.flush();
        outputStream.close();
        inputStream.close();

        return file;
    }
}


