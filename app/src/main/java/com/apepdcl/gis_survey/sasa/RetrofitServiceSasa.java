package com.apepdcl.gis_survey.sasa;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitServiceSasa {
    @GET("getQns")
    Call<JsonObject> getQuestions();

    @POST("postingDataAndImgs")
    Call<JsonObject> postingData(@Body JsonArray request);
}
