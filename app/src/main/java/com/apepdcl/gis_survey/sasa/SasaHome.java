package com.apepdcl.gis_survey.sasa;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.DashBoard;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.estimates.RetrofitClientInstanceEstimates;
import com.apepdcl.gis_survey.estimates.RetrofitServiceEstimates;
import com.apepdcl.gis_survey.estimates.WorkInvolmentsActivity;
import com.apepdcl.gis_survey.estimates.models.BannerModel;
import com.apepdcl.gis_survey.estimates.sou.SurveyList;
import com.apepdcl.gis_survey.estimates.sou.TransmitSurvey;
import com.apepdcl.gis_survey.popup.CustomAllDialogs;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SasaHome extends AppCompatActivity implements QuestionAdapter.ImageSelectionListener {

    RelativeLayout back_layout;
    RelativeLayout profile_layout;

    RecyclerView question_rv;
    ArrayList<QuestionModel> list;
    QuestionAdapter adapter;
    TextView submit;
    RetrofitServiceSasa retrofitService;
    ProgressDialog progressDialog;
    List<LoginDataResponse> loginDataResponses;
    TextView district,office_type,office_name;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sasa_home);
        getSupportActionBar().hide();

        loginDataResponses=new ArcGISDatabase(this).getUserMasterData();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        back_layout = findViewById(R.id.back_layout);
        profile_layout = findViewById(R.id.profile_layout);
        question_rv = findViewById(R.id.question_rv);
        submit = findViewById(R.id.submit);
        district = findViewById(R.id.district);
        office_type = findViewById(R.id.office_type);
        office_name = findViewById(R.id.office_name);
        list = new ArrayList<>();

        district.setText(loginDataResponses.get(0).getCIRCLE());
        office_type.setText(loginDataResponses.get(0).getOFFICE_TYPE());
        office_name.setText(loginDataResponses.get(0).getSECTION_NAME());
       // getQuestionList();
        getQuestions();

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


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getAnswer().equalsIgnoreCase("E")){
                        Toast.makeText(SasaHome.this, "Please Answer All the questions", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (list.get(i).getAnswer().equalsIgnoreCase("YES") && list.get(i).getImage_from().equalsIgnoreCase("N")){
                        Toast.makeText(SasaHome.this, "Please Upload Image for Question No "+(i+1), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                submit.setVisibility(View.GONE);
                postSasaData();
            }
        });



    }

    private void postSasaData() {
        progressDialog.show();

        JsonArray mainArray = new JsonArray();
        for (int i = 0; i < list.size(); i++){
            JsonObject object = new JsonObject();
            object.addProperty("OFFICE_ID",loginDataResponses.get(0).getOFFICE_ID());
            object.addProperty("USER_ID",loginDataResponses.get(0).getUSERID());
            object.addProperty("USER_NAME",loginDataResponses.get(0).getROLE());
            object.addProperty("QUESTION",list.get(i).getQuestion());
            object.addProperty("ANSWER",list.get(i).getAnswer());
            if (list.get(i).getAnswer().equalsIgnoreCase("YES")){
                JsonArray imageArray = new JsonArray();
                JsonObject imageObject = new JsonObject();
                imageObject.addProperty("DATA",list.get(i).getConvertedImage());
                imageArray.add(imageObject);
                object.add("IMAGE",imageArray);
            }
            mainArray.add(object);
        }
        Gson gson = new Gson();
        String json = gson.toJson(mainArray);
        Log.v("SASA===>>", "POSTING" + json);

        retrofitService = RetrofitClientSasa.getRetrofitInstance().create(RetrofitServiceSasa.class);
        Call<JsonObject> responseVoCall = retrofitService.postingData(mainArray);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                 System.out.println("QUESTION POSTING RESPONSE : " + response.body());
                if (response.body() != null) {
                    JsonObject surveyedResponseVo = response.body();
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        submit.setVisibility(View.GONE);
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(SasaHome.this);
                        builder.setTitle("SUCCESSFULLY TRANSMITTED!!");
                        builder.setMessage("Data Transmitted Successfully!!");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OKAY", (dialog, which) -> {
                            dialog.dismiss();
                            Intent intent = new Intent(SasaHome.this, DashBoard.class);
                            startActivity(intent);
                            finish();

                        });

                        androidx.appcompat.app.AlertDialog dialog = builder.create();
                        dialog.show();

                    } else {
                        Toast.makeText(SasaHome.this, "Failed Please try again", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                submit.setVisibility(View.VISIBLE);
                progressDialog.dismiss();
                Toast.makeText(SasaHome.this, "Failed Please try again", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getQuestionList() {

        QuestionModel model = new QuestionModel();
        model.setId("1");
        model.setQuestion("Is Cleaning of office premises done?");
        model.setImage("N");
        model.setImage_from("N");
        model.setImage_path("");
        model.setAnswer("E");
        model.setSelectedRadioButtonId(-1);

        QuestionModel model1 = new QuestionModel();
        model1.setId("2");
        model1.setQuestion("Is drinking water available in office premises?");
        model1.setImage("N");
        model1.setImage_from("N");
        model1.setImage_path("");
        model1.setAnswer("E");
        model1.setSelectedRadioButtonId(-1);

        QuestionModel model2 = new QuestionModel();
        model2.setId("3");
        model2.setQuestion("Is office toilets has been cleaned?");
        model2.setImage("N");
        model2.setImage_from("N");
        model2.setImage_path("");
        model2.setAnswer("E");
        model2.setSelectedRadioButtonId(-1);

        list.add(model);
        list.add(model1);
        list.add(model2);

        question_rv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SasaHome.this);
        question_rv.setLayoutManager(layoutManager);
        question_rv.setFocusable(false);
       // question_rv.setNestedScrollingEnabled(false);
        adapter = new QuestionAdapter(list, SasaHome.this, SasaHome.this);
        question_rv.setAdapter(adapter);
    }

    private void getQuestions(){
        retrofitService = RetrofitClientSasa.getRetrofitInstance().create(RetrofitServiceSasa.class);
        Call<JsonObject> responseVoCall = retrofitService.getQuestions();
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                //loader_layout.setVisibility(View.GONE);
                //main_ui_layout.setVisibility(View.VISIBLE);
                if (response != null) {
                    JsonObject surveyedResponseVo = response.body();

                    if (surveyedResponseVo.has("STATUS") && !surveyedResponseVo.get("STATUS").isJsonNull()) {
                        String status = surveyedResponseVo.get("STATUS").getAsString();
                        if (MobileConstants.TRUE.equalsIgnoreCase(status)) {
                            JsonArray array = surveyedResponseVo.getAsJsonArray("DATA");
                            list.clear();
                            for (int i = 0; i < array.size(); i++) {
                                JsonObject object = array.get(i).getAsJsonObject();
                                if (loginDataResponses.get(0).getOFFICE_TYPE().equalsIgnoreCase("SECTION")){
                                    QuestionModel model = new QuestionModel();
                                    model.setId(object.has("serialId") && !object.get("serialId").isJsonNull() ? object.get("serialId").getAsString() : "");
                                    model.setQuestion(object.has("QUESTION") && !object.get("QUESTION").isJsonNull() ? object.get("QUESTION").getAsString() : "");
                                    model.setImage("N");
                                    model.setImage_from("N");
                                    model.setImage_path("");
                                    model.setAnswer("E");
                                    model.setSelectedRadioButtonId(-1);
                                    list.add(model);
                                }else {
                                    if (!object.get("serialId").getAsString().equalsIgnoreCase("3")){
                                        QuestionModel model = new QuestionModel();
                                        model.setId(object.has("serialId") && !object.get("serialId").isJsonNull() ? object.get("serialId").getAsString() : "");
                                        model.setQuestion(object.has("QUESTION") && !object.get("QUESTION").isJsonNull() ? object.get("QUESTION").getAsString() : "");
                                        model.setImage("N");
                                        model.setImage_from("N");
                                        model.setImage_path("");
                                        model.setAnswer("E");
                                        model.setSelectedRadioButtonId(-1);
                                        list.add(model);
                                    }
                                }
                            }

                            question_rv.setHasFixedSize(true);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SasaHome.this);
                            question_rv.setLayoutManager(layoutManager);
                            question_rv.setFocusable(false);
                            // question_rv.setNestedScrollingEnabled(false);
                            adapter = new QuestionAdapter(list, SasaHome.this, SasaHome.this);
                            question_rv.setAdapter(adapter);

                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            int position = requestCode % 100; // Get position

            if (requestCode >= 100 && requestCode < 200) { // Camera
               // Bitmap photo = (Bitmap) data.getExtras().get("data");
                adapter.imageSelectionListener.onImageSelected(position);
            } else if (requestCode >= 200) { // Gallery
                Uri imageUri = data.getData();
                adapter.imageSelectionListener.onImageSelected(imageUri, position);
            }
        }
    }



    @Override
    public void onImageSelected( int position) {
        Bitmap bitmap = BitmapFactory.decodeFile(list.get(position).getImage_path());
        list.get(position).setImage(bitmap.toString()); // Save image
        list.get(position).setImage_from("C");
        adapter.notifyItemChanged(position);
    }

    @Override
    public void onImageSelected(Uri uri, int position) {
        list.get(position).setImage(uri.toString());
        list.get(position).setImage_from("G");// Save image
        adapter.notifyItemChanged(position);
    }
}
