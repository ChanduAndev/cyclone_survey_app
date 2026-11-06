package com.apepdcl.gis_survey.estimates.sou;

import static com.apepdcl.gis_survey.utilits.CustomDialog.getColoredText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.DashBoard;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.estimates.EstimatesDashboardActivity;
import com.apepdcl.gis_survey.estimates.EstimatesHome;
import com.apepdcl.gis_survey.estimates.RetrofitClientInstanceEstimates;
import com.apepdcl.gis_survey.estimates.RetrofitServiceEstimates;
import com.apepdcl.gis_survey.estimates.WorkInvolmentsActivity;
import com.apepdcl.gis_survey.estimates.models.CheckListDataModel;
import com.apepdcl.gis_survey.estimates.models.DTRMakeMaster;
import com.apepdcl.gis_survey.estimates.models.FdrParticularsModel;
import com.apepdcl.gis_survey.estimates.models.PurposeModel;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompleteSurvey extends AppCompatActivity implements CompleteAdapter.OnItemClickListener {

    RelativeLayout back_layout, close_layout;
    GifImageView loader;
    RecyclerView purpose_rv;
    ScrollView main_layout;
    private RetrofitServiceEstimates retrofitService;
    ArrayList<PurposeModel> list;

    ArrayList<CheckListDataModel> checkListDataModels;
    List<LoginDataResponse> loginDataResponses;
    private ArcGISDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_survey);
        getSupportActionBar().hide();
        back_layout = findViewById(R.id.back_layout);
        close_layout = findViewById(R.id.close_layout);
        loader = findViewById(R.id.loader);
        main_layout = findViewById(R.id.main_layout);
        purpose_rv = findViewById(R.id.purpose_rv);
        list = new ArrayList<>();
        checkListDataModels = new ArrayList<>();

        if (ConnectivityChecking.checkConnection(CompleteSurvey.this)) {
            getEstimateswithpolecnts();
        } else {
            CommanUtilits.internetDialog(CompleteSurvey.this);
        }
        // getEstimateswithpolecnts();

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
        database = new ArcGISDatabase(this);
        loginDataResponses = database.getUserMasterData();

    }


    private void getEstimateswithpolecnts() {
        loader.setVisibility(View.VISIBLE);
        list.clear();
        //loader.setVisibility(View.VISIBLE);
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        JsonObject dtrDetailsReqestVo = new JsonObject();

        //dtrDetailsReqestVo.addProperty("SEC_ID", loginDataResponses.get(0).getSEC_CODE().equals("279") ? "589" : loginDataResponses.get(0).getSEC_CODE());
        dtrDetailsReqestVo.addProperty("SEC_ID", loginDataResponses.get(0).getSEC_CODE());
        // dtrDetailsReqestVo.addProperty("ROLE", loginDataResponses.get(0).getSEC_CODE().equals("279") ? "AEOP" : "AE");
        dtrDetailsReqestVo.addProperty("ROLE", loginDataResponses.get(0).getROLE());
        dtrDetailsReqestVo.addProperty("OFFICE_ID", loginDataResponses.get(0).getOFFICE_ID());
        dtrDetailsReqestVo.addProperty("STATUS", 2);
        dtrDetailsReqestVo.addProperty("USER_ID", loginDataResponses.get(0).getUSERID());
        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")){
            dtrDetailsReqestVo.addProperty("MODULE", LoginActivity.SURVEY_FROM);
            dtrDetailsReqestVo.addProperty("VERSION", MobileConstants.WO_RDSS_VERSION);
        }else {
            dtrDetailsReqestVo.addProperty("VERSION", MobileConstants.ESTIMATES_VERSION);
        }
        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.estimateswithpolecnts(dtrDetailsReqestVo);
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

                                if (object.get("STATUS").getAsString().equals("2")) {


                                    boolean IS_33KV = !object.get("LINE_33KV").isJsonNull() && object.get("LINE_33KV").getAsString().equals("Y");
                                    boolean IS_11KV = !object.get("LINE_11KV").isJsonNull() && object.get("LINE_11KV").getAsString().equals("Y");
                                    boolean IS_LT = !object.get("LINE_LT").isJsonNull() && object.get("LINE_LT").getAsString().equals("Y");

                                    int COUNT_33KV = object.get("NUM_33KV").getAsInt();
                                    int COUNT_11KV = object.get("NUM_11KV").getAsInt();
                                    int COUNT_LT = object.get("NUM_LT").getAsInt();

                                    if (IS_33KV && COUNT_33KV == 0) {
                                        continue;
                                    }

                                    if (IS_11KV && COUNT_11KV == 0) {
                                        continue;
                                    }

                                    if (IS_LT && COUNT_LT == 0) {
                                        continue;
                                    }


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
                                    model.setFEEDERS(object.has("FEEDERS") && !object.get("FEEDERS").isJsonNull() ? object.get("FEEDERS").getAsString() : "");


                                    model.setMETERS(object.has("METERS") && !object.get("METERS").isJsonNull() ? object.get("METERS").getAsString() : "");
                                    model.setMAX_LENGTH(object.has("MAX_LENGTH") && !object.get("MAX_LENGTH").isJsonNull() ? object.get("MAX_LENGTH").getAsString() : "");
                                    model.setWORK_ORDER(object.has("WORK_ORDER") && !object.get("WORK_ORDER").isJsonNull() ? object.get("WORK_ORDER").getAsString() : "");
                                    model.setTOWER_LENGTH(object.has("TOWER_LENGTH") && !object.get("TOWER_LENGTH").isJsonNull() ? object.get("TOWER_LENGTH").getAsString() : "");
                                    model.setWO_OLD(object.has("WO_OLD") && !object.get("WO_OLD").isJsonNull() ? object.get("WO_OLD").getAsString() : "");

                                    model.setCOUNT_33KV(object.get("COUNT_33KV").getAsInt());
                                    model.setCOUNT_11KV(object.get("COUNT_11KV").getAsInt());
                                    model.setCOUNT_LT(object.get("COUNT_LT").getAsInt());

                                    model.setNUM_33KV(object.get("NUM_33KV").getAsInt());
                                    model.setNUM_11KV(object.get("NUM_11KV").getAsInt());
                                    model.setNUM_LT(object.get("NUM_LT").getAsInt());

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

                            setupCompleteSurvey();

                        } else {
                            // Show error message if STATUS is not true
                            String errorMessage = surveyedResponseVo.has("ERR_MSG") && !surveyedResponseVo.get("ERR_MSG").isJsonNull()
                                    ? surveyedResponseVo.get("ERR_MSG").getAsString()
                                    : "Unknown error occurred";
                            Toast.makeText(CompleteSurvey.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }

                    } else {

                        // Handle case where STATUS is missing or null
                        Toast.makeText(CompleteSurvey.this, "Invalid response from server: STATUS is missing or null", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(CompleteSurvey.this, "Server Busy. Try Again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(CompleteSurvey.this, "Try Again: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setupCompleteSurvey() {
        purpose_rv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CompleteSurvey.this);
        purpose_rv.setLayoutManager(layoutManager);
        purpose_rv.setFocusable(false);
        CompleteAdapter adapter = new CompleteAdapter(list, CompleteSurvey.this, this);
        purpose_rv.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        Intent intentPf = new Intent(CompleteSurvey.this, SurveyList.class);
        startActivity(intentPf);
        finish();
        overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
    }

    @Override
    public void onItemClick(int position, PurposeModel model) {
        if (ConnectivityChecking.checkConnection(CompleteSurvey.this)) {
            validateProposedInvolvments(model.getGIS_ID(), model, model.getEST_NO(), LoginActivity.SURVEY_FROM.equals("WORK_ORDER") ? model.getWORK_ORDER() : "");
        } else {
            CommanUtilits.internetDialog(CompleteSurvey.this);
        }

        //cktWisePoleCounts(model.getGIS_ID(), model);


        //openCompleteDialog(model.getGIS_ID());
    }

    private void openCompleteDialog(String GIS_ID) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Complete Survey!!");
        builder.setMessage("Are you sure you want to complete this survey?");
        builder.setPositiveButton("YES", (dialog, which) -> {
            dialog.dismiss();
            callSurveyCompleteAPI(GIS_ID);

        });

        builder.setNegativeButton("NO", (dialog, which) -> {
            dialog.cancel();

        });
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void callSurveyCompleteAPI(String GIS_ID) {
        loader.setVisibility(View.VISIBLE);

        JsonObject entry = new JsonObject();
        entry.addProperty("GIS_ID", GIS_ID);
        entry.addProperty("STATUS", 5);

        JsonArray jsonArray = new JsonArray();
        jsonArray.add(entry);

        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        // Log.v("DATA===>>", "" + jsonArray);
        //  writeToFile(json);
        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.completeSurvey(jsonArray);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loader.setVisibility(View.GONE);
                //  Log.v("RESPONSE===>>", "" + response.body());
                // System.out.println("COMPLETE ESTIMATES RESPONSE : " + response.body());
                if (response.body() != null) {
                    JsonObject surveyedResponseVo = response.body();
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        openDialogForCompleteSuccess(surveyedResponseVo.get("ERR_MSG").getAsString(), GIS_ID, "", "");
                        //Toast.makeText(CompleteSurvey.this, surveyedResponseVo.get("ERR_MSG").getAsString(), Toast.LENGTH_LONG).show();
                        if (ConnectivityChecking.checkConnection(CompleteSurvey.this)) {
                            getEstimateswithpolecnts();
                        } else {
                            CommanUtilits.internetDialog(CompleteSurvey.this);
                        }
                        // getEstimateswithpolecnts();
                    } else {
                        openDialogForCompleteSuccess(surveyedResponseVo.get("ERR_MSG").getAsString(), GIS_ID, "", "");
                        // Toast.makeText(CompleteSurvey.this, surveyedResponseVo.get("ERR_MSG").getAsString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }

    private void cktWisePoleCounts(String GIS_ID, PurposeModel model, String Est_no, String work_order) {
        loader.setVisibility(View.VISIBLE);
        main_layout.setVisibility(View.GONE);
        JsonObject entry = new JsonObject();
        entry.addProperty("GIS_ID", GIS_ID);


        // Log.v("DATA===>>", "" + jsonArray);
        //  writeToFile(json);
        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.cktWisePoleCounts(entry);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loader.setVisibility(View.GONE);
                main_layout.setVisibility(View.VISIBLE);
                //  Log.v("RESPONSE===>>", "" + response.body());
                // System.out.println("COMPLETE ESTIMATES RESPONSE : " + response.body());
                JsonObject surveyedResponseVo = response.body();

                boolean allCktPoleCntGreaterThanZero = true;
                boolean allDtrsValid = true;
                String invalidDtrCode = "";

                if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                    JsonObject jsonObject = surveyedResponseVo.getAsJsonObject("DATA");
                    String type = jsonObject.has("TYPE") && !jsonObject.get("TYPE").isJsonNull() ? jsonObject.get("TYPE").getAsString() : "";

                    if ("ONLY_LT".equalsIgnoreCase(type)) {
                        if (jsonObject.has("CNT_DETAILS") && jsonObject.get("CNT_DETAILS").isJsonArray()) {
                            JsonArray array = jsonObject.getAsJsonArray("CNT_DETAILS");
                            for (int i = 0; i < array.size(); i++) {
                                JsonObject object = array.get(i).getAsJsonObject();

                                int cktPoleCnt = object.has("CKT_POLE_CNT") && !object.get("CKT_POLE_CNT").isJsonNull()
                                        ? object.get("CKT_POLE_CNT").getAsInt()
                                        : 0;

                                if (cktPoleCnt <= 0) {
                                    allCktPoleCntGreaterThanZero = false;
                                    invalidDtrCode = object.has("LT_CKT_CODE") && !object.get("LT_CKT_CODE").isJsonNull()
                                            ? object.get("LT_CKT_CODE").getAsString()
                                            : "";
                                    break;
                                }

                                CheckListDataModel model = new CheckListDataModel();
                                model.setDTR_CODE(object.has("DTR_CODE") && !object.get("DTR_CODE").isJsonNull() ? object.get("DTR_CODE").getAsString() : "");
                                model.setCOUNT_CKT(object.has("COUNT_CKT") && !object.get("COUNT_CKT").isJsonNull() ? object.get("COUNT_CKT").getAsString() : "");
                                model.setLT_CKT_CODE(object.has("LT_CKT_CODE") && !object.get("LT_CKT_CODE").isJsonNull() ? object.get("LT_CKT_CODE").getAsString() : "");
                                model.setCKT_POLE_CNT(String.valueOf(cktPoleCnt));
                                checkListDataModels.add(model);
                            }
                        }

                        if (allCktPoleCntGreaterThanZero) {
                            navigateToCompleteBoqScreen(model);
                        } else {
                            openDialogForCompleteSuccess("The LT Circuit code " + " " + invalidDtrCode + " " + " is not surveyed", GIS_ID, Est_no, work_order);
                        }
                    } else if ("OTHERS".equalsIgnoreCase(type)) {
                        navigateToCompleteBoqScreen(model);
                    } else {
                        if (jsonObject.has("CNT_DETAILS") && jsonObject.get("CNT_DETAILS").isJsonArray()) {
                            JsonArray array = jsonObject.getAsJsonArray("CNT_DETAILS");

                            Map<String, List<JsonObject>> dtrMap = new HashMap<>();

                            for (JsonElement element : array) {
                                JsonObject obj = element.getAsJsonObject();
                                String dtrCode = obj.has("DTR_CODE") && !obj.get("DTR_CODE").isJsonNull() ? obj.get("DTR_CODE").getAsString() : "";
                                if (!dtrCode.isEmpty()) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        dtrMap.computeIfAbsent(dtrCode, k -> new ArrayList<>()).add(obj);
                                    }
                                }
                            }

                            for (Map.Entry<String, List<JsonObject>> entry : dtrMap.entrySet()) {
                                String dtrCode = entry.getKey();
                                List<JsonObject> dtrList = entry.getValue();

                                int expectedCount = 0;
                                JsonObject firstObj = dtrList.get(0);
                                if (firstObj.has("COUNT_CKT") && !firstObj.get("COUNT_CKT").isJsonNull()) {
                                    try {
                                        expectedCount = Integer.parseInt(firstObj.get("COUNT_CKT").getAsString());
                                    } catch (NumberFormatException e) {
                                        expectedCount = 0;
                                    }
                                }

                                boolean allCktValid = false;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    allCktValid = dtrList.size() == expectedCount && dtrList.stream().allMatch(o -> o.has("CKT_POLE_CNT") && !o.get("CKT_POLE_CNT").isJsonNull() && o.get("CKT_POLE_CNT").getAsInt() > 0
                                    );
                                }

                                if (!allCktValid) {
                                    // Find the first invalid DTR_CODE from dtrList
                                    for (JsonObject obj : dtrList) {
                                        if (obj.has("CKT_POLE_CNT") && !obj.get("CKT_POLE_CNT").isJsonNull() && obj.get("CKT_POLE_CNT").getAsInt() == 0) {
                                            invalidDtrCode = obj.has("DTR_CODE") ? obj.get("DTR_CODE").getAsString() : "";
                                            break;
                                        }
                                    }

                                    allDtrsValid = false;
                                    break;
                                }
                            }
                            if (allDtrsValid) {
                                navigateToCompleteBoqScreen(model);

                            } else {
                                if (invalidDtrCode.isBlank()) {
                                    openDialogForCompleteSuccess("The LT Circuit code" + " " + "is not surveyed", GIS_ID, Est_no, work_order);
                                } else {
                                    openDialogForCompleteSuccess("The LT Circuit code" + " " + invalidDtrCode + " " + "is not surveyed", GIS_ID, Est_no, work_order);

                                }
                            }
                        }

                    }
                } else {
                    if (surveyedResponseVo.get("ERR_MSG").isJsonNull()) {
                        openDialogForCompleteSuccess("Server error", GIS_ID, Est_no, work_order);
                    } else {
                        openDialogForCompleteSuccess(surveyedResponseVo.get("ERR_MSG").getAsString(), GIS_ID, Est_no, work_order);
                    }
                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                main_layout.setVisibility(View.VISIBLE);
                loader.setVisibility(View.VISIBLE);
            }
        });

    }

    private void validateProposedInvolvments(String GIS_ID, PurposeModel model, String Est_no, String work_order) {
        main_layout.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);
        JsonObject entry = new JsonObject();
        entry.addProperty("GIS_ID", GIS_ID);
        List<LoginDataResponse> loginDataResponses = new ArcGISDatabase(this).getUserMasterData();
        retrofitService = RetrofitClientInstanceEstimates.getRetrofitInstance(loginDataResponses.get(0).getACCESS_TOKEN()).create(RetrofitServiceEstimates.class);
        Call<JsonObject> responseVoCall = retrofitService.validateProposedInvolvments(entry);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loader.setVisibility(View.GONE);
                main_layout.setVisibility(View.VISIBLE);
                JsonObject surveyedResponseVo = response.body();
                if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                    cktWisePoleCounts(model.getGIS_ID(), model, model.getEST_NO(), work_order);
                } else {
                    openDialogForCompleteSuccess(surveyedResponseVo.get("ERR_MSG").getAsString(), GIS_ID, Est_no, work_order);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loader.setVisibility(View.GONE);
                main_layout.setVisibility(View.GONE);

            }
        });


    }


    private void navigateToCompleteBoqScreen(PurposeModel model) {
        Intent intent = new Intent(CompleteSurvey.this, BoQActivity.class);
        intent.putExtra("GIS_ID", model.getGIS_ID());
        intent.putExtra("EST_NO", model.getEST_NO());
        intent.putExtra("DESCRIPTION", model.getPURPOSE());
        intent.putExtra("WORK_ORDER", model.getWORK_ORDER());
        intent.putExtra("EST_NATURE", model.getEST_NATURE());
        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")){
            intent.putExtra("FEEDERS", model.getFEEDERS());
        }
        startActivity(intent);
        overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
    }

    private void openDialogForCompleteSuccess(String msg, String GIS_ID, String Est_no, String work_order) {

        Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.customized_dialog, null);
        dialog.setContentView(view);

        // Views
        TextView title = view.findViewById(R.id.title);
        TextView message = view.findViewById(R.id.message);
        TextView okBtn = view.findViewById(R.id.okBtn);
        TextView cancelBtn = view.findViewById(R.id.cancelBtn);

        TextView tv_est_no = view.findViewById(R.id.tv_est_no);
        TextView tv_gis_id = view.findViewById(R.id.tv_gis_id);
        ImageView iv_icon = view.findViewById(R.id.iv_icon);
        tv_est_no.setText(getColoredText("Est No: ", Est_no, this));
        tv_gis_id.setText(getColoredText("GIS ID: ", GIS_ID, this));
        iv_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.warning));
        // 🔹 Title
        title.setText("Pay Attention");

        // 🔹 Build Header + Message
        StringBuilder builder = new StringBuilder();

        if (GIS_ID != null && !GIS_ID.isEmpty()) {
            builder.append("<b>GIS ID:</b> ").append(GIS_ID).append("<br>");
        }

        if (Est_no != null && !Est_no.isEmpty()) {
            builder.append("<b>Est No:</b> ").append(Est_no).append("<br>");
        }

        if (work_order != null && !work_order.isEmpty()) {
            builder.append("<b>Work Order:</b> ").append(work_order).append("<br><br>");
        }

        // 🔴 Message Styling
        if (msg != null && msg.toLowerCase().contains("newly  proposed")) {
            builder.append("<font color='#D32F2F'><b>⚠ ")
                    .append(msg)
                    .append("</b></font>");
        } else {
            builder.append(msg);
        }

        message.setText(HtmlCompat.fromHtml(builder.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY));

        // 🔹 Buttons
        okBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.GONE);

        okBtn.setText("Okay");

        // Click
        okBtn.setOnClickListener(v -> dialog.dismiss());

        dialog.setCancelable(false);

        // Transparent background + full width
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        }

        dialog.show();
    }




}