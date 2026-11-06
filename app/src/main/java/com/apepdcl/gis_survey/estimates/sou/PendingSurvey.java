package com.apepdcl.gis_survey.estimates.sou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.estimates.EstimatesHome;
import com.apepdcl.gis_survey.estimates.PurposeAdapter;
import com.apepdcl.gis_survey.estimates.RetrofitClientInstanceEstimates;
import com.apepdcl.gis_survey.estimates.RetrofitServiceEstimates;
import com.apepdcl.gis_survey.estimates.models.FdrParticularsModel;
import com.apepdcl.gis_survey.estimates.models.PurposeModel;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingSurvey extends AppCompatActivity implements PendingAdapter.OnItemClickListener{

    RelativeLayout back_layout;
    RelativeLayout close_layout;
    RecyclerView purpose_rv;
    GifImageView loader;
    ArrayList<PurposeModel> list;
    RetrofitServiceEstimates retrofitService;

    //LOCATION
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private double currentLat, currentLog;

    String ESTIMATION_NO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_survey);
        getSupportActionBar().hide();

        back_layout = findViewById(R.id.back_layout);
        close_layout = findViewById(R.id.close_layout);
        purpose_rv = findViewById(R.id.purpose_rv);
        loader = findViewById(R.id.loader);

        list = new ArrayList<>();

        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        close_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        checkLocationPermissions();

        getPurposeData();
    }


    private void getPurposeData() {
        loader.setVisibility(View.VISIBLE);


        //loader.setVisibility(View.VISIBLE);
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        JsonObject dtrDetailsReqestVo = new JsonObject();

       // dtrDetailsReqestVo.addProperty("SEC_ID", loginDataResponses.get(0).getSEC_CODE().equals("279") ? "589" : loginDataResponses.get(0).getSEC_CODE());
       // dtrDetailsReqestVo.addProperty("ROLE", loginDataResponses.get(0).getSEC_CODE().equals("279") ? "AEOP" : "AE");
        dtrDetailsReqestVo.addProperty("SEC_ID", loginDataResponses.get(0).getSEC_CODE());
        dtrDetailsReqestVo.addProperty("ROLE", loginDataResponses.get(0).getROLE());
        dtrDetailsReqestVo.addProperty("OFFICE_ID", loginDataResponses.get(0).getOFFICE_ID());
        dtrDetailsReqestVo.addProperty("VERSION", MobileConstants.ESTIMATES_VERSION);

        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance("").create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.getPurpose(dtrDetailsReqestVo);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loader.setVisibility(View.GONE);
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    //Log.v("FEEDER==", "" + surveyedResponseVo);

                    // Check if the STATUS field exists and is not null
                    if (surveyedResponseVo.has("STATUS") && !surveyedResponseVo.get("STATUS").isJsonNull()) {
                        String status = surveyedResponseVo.get("STATUS").getAsString();

                        if (MobileConstants.TRUE.equalsIgnoreCase(status)) {
                            JsonArray array = surveyedResponseVo.getAsJsonArray("DATA");

                            for (int i = 0; i < array.size(); i++) {
                                JsonObject object = array.get(i).getAsJsonObject();

                                if (object.get("STATUS").getAsString().equals("1")){
                                    PurposeModel model = new PurposeModel();

                                    model.setGIS_ID(object.has("GIS_ID") && !object.get("GIS_ID").isJsonNull() ? object.get("GIS_ID").getAsString() : "");
                                    model.setEST_NATURE(object.has("EST_NATURE") && !object.get("EST_NATURE").isJsonNull() ? object.get("EST_NATURE").getAsString() : "");
                                    model.setEST_NO(object.has("EST_NO") && !object.get("EST_NO").isJsonNull() ? object.get("EST_NO").getAsString() : "");
                                    model.setREG_COUNT(object.has("REG_COUNT") && !object.get("REG_COUNT").isJsonNull() ? object.get("REG_COUNT").getAsString() : "");
                                    model.setSEC_ID(object.has("SEC_ID") && !object.get("SEC_ID").isJsonNull() ? object.get("SEC_ID").getAsString() : "");
                                    model.setSTATUS(object.has("STATUS") && !object.get("STATUS").isJsonNull() ? object.get("STATUS").getAsString() : "");
                                    model.setPURPOSE(object.has("PURPOSE") && !object.get("PURPOSE").isJsonNull() ? object.get("PURPOSE").getAsString() : "");
                                    model.setLINE_33KV(object.has("LINE_33KV") && !object.get("LINE_33KV").isJsonNull() ? object.get("LINE_33KV").getAsString() : "");
                                    model.setLINE_11KV(object.has("LINE_11KV") && !object.get("LINE_11KV").isJsonNull() ? object.get("LINE_11KV").getAsString() : "");
                                    model.setLINE_LT(object.has("LINE_LT") && !object.get("LINE_LT").isJsonNull() ? object.get("LINE_LT").getAsString() : "");
                                    model.setDTR(object.has("DTR") && !object.get("DTR").isJsonNull() ? object.get("DTR").getAsString() : "");
                                    model.setSS(object.has("SS") && !object.get("SS").isJsonNull() ? object.get("SS").getAsString() : "");


                                    model.setMETERS(object.has("METERS") && !object.get("METERS").isJsonNull() ? object.get("METERS").getAsString() : "");
                                    model.setMAX_LENGTH(object.has("MAX_LENGTH") && !object.get("MAX_LENGTH").isJsonNull() ? object.get("MAX_LENGTH").getAsString() : "");
                                    model.setWORK_ORDER(object.has("WORK_ORDER") && !object.get("WORK_ORDER").isJsonNull() ? object.get("WORK_ORDER").getAsString() : "");
                                    model.setTOWER_LENGTH(object.has("TOWER_LENGTH") && !object.get("TOWER_LENGTH").isJsonNull() ? object.get("TOWER_LENGTH").getAsString() : "");


                                    if (object.has("FDR_PARTICULARS") && object.get("FDR_PARTICULARS").isJsonArray()) {
                                        JsonArray fdrArray = object.getAsJsonArray("FDR_PARTICULARS");
                                        List<FdrParticularsModel> fdrList = new ArrayList<>();

                                        for (int j = 0; j < fdrArray.size(); j++) {
                                            JsonObject fdrObject = fdrArray.get(j).getAsJsonObject();
                                            FdrParticularsModel fdrModel = new FdrParticularsModel();

                                            fdrModel.setFEEDER(fdrObject.has("FEEDER") && !fdrObject.get("FEEDER").isJsonNull() ? fdrObject.get("FEEDER").getAsString() : null);
                                            // fdrModel.setFEEDER_NAME(fdrObject.has("FEEDER_NAME") && !fdrObject.get("FEEDER_NAME").isJsonNull() ? fdrObject.get("FEEDER_NAME").getAsString() : "");
                                            fdrModel.setVOLTAGE_LEVEL(fdrObject.has("VOLTAGE_LEVEL") && !fdrObject.get("VOLTAGE_LEVEL").isJsonNull() ? fdrObject.get("VOLTAGE_LEVEL").getAsString() : "");
                                            fdrModel.setSURVEY_TYPE(fdrObject.has("SURVEY_TYPE") && !fdrObject.get("SURVEY_TYPE").isJsonNull() ? fdrObject.get("SURVEY_TYPE").getAsString() : "");
                                            fdrModel.setSS_NAME(fdrObject.has("SS_NAME") && !fdrObject.get("SS_NAME").isJsonNull() ? fdrObject.get("SS_NAME").getAsString() : "");
                                            fdrModel.setFDR_INDEX(Integer.parseInt(fdrObject.has("FDR_INDEX") && !fdrObject.get("FDR_INDEX").isJsonNull() ? fdrObject.get("FDR_INDEX").getAsString() : ""));
                                            fdrModel.setFDR_COUNT(Integer.parseInt(fdrObject.has("FDR_COUNT") && !fdrObject.get("FDR_COUNT").isJsonNull() ? fdrObject.get("FDR_COUNT").getAsString() : ""));

                                            fdrList.add(fdrModel);
                                        }
                                        model.setFDR_PARTICULARS(fdrList);
                                    }

                                    list.add(model);
                                }


                            }

                            setupAdapterData();

                        } else {
                            // Show error message if STATUS is not true
                            String errorMessage = surveyedResponseVo.has("ERR_MSG") && !surveyedResponseVo.get("ERR_MSG").isJsonNull()
                                    ? surveyedResponseVo.get("ERR_MSG").getAsString()
                                    : "Unknown error occurred";
                            Toast.makeText(PendingSurvey.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }

                    } else {

                        // Handle case where STATUS is missing or null
                        Toast.makeText(PendingSurvey.this, "Invalid response from server: STATUS is missing or null", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(PendingSurvey.this, "Server Busy. Try Again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(PendingSurvey.this, "Try Again: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setupAdapterData() {
        purpose_rv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PendingSurvey.this);
        purpose_rv.setLayoutManager(layoutManager);
        purpose_rv.setFocusable(false);
        PendingAdapter adapter = new PendingAdapter(list, PendingSurvey.this, this);
        purpose_rv.setAdapter(adapter);
    }


    private void checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {

        }
    }

    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(this, "Location permissions are required to proceed.", Toast.LENGTH_SHORT).show();
            }
        }
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

                        } else {
                            // getFeederMasterAPI(0.0, 0.0);
                            Toast.makeText(PendingSurvey.this, "Unable to retrieve location.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onItemClick(int position, PurposeModel model) {
        ESTIMATION_NO = model.getEST_NO();
        getLatLongs();
    }
}