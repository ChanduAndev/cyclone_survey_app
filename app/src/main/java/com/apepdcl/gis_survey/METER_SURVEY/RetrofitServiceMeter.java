package com.apepdcl.gis_survey.METER_SURVEY;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by SOUBHAGYA SWAIN on 10-06-2026.
 */
public interface RetrofitServiceMeter {


    @POST("gisBillReaderLogin")
    Call<JsonObject> billReaderLogin(@Body JsonObject object);
    @POST("getUserWiseCounts")
    Call<JsonObject> getUserWiseCounts(@Body JsonObject object);

    @POST("getAssignedDTrs")
    Call<JsonObject> getAssignedDTrs(@Body JsonObject object);

    @POST("getDTrPoleData")
    Call<JsonObject> getDTrPoleData(@Body JsonObject object);

    @POST("getServiceConsumersData")
    Call<JsonObject> getServiceConsumersData(@Body JsonObject object);

    @GET("getDmsToken")
    Call<JsonObject> getImageAccessToken();

    @POST("saveConsumersMappingSurveyData")
    Call<JsonObject> transmitData(@Body JsonArray object);

    @POST("completeSurvey")
    Call<JsonObject> completeSurvey(@Body JsonObject object);

    @GET("getDTrOptions")
    Call<JsonObject> getDTROptions();

    @POST("saveDTrOption")
    Call<JsonObject> saveDTROption(@Body JsonObject object);

    @POST("saveServiceConsumersCount")
    Call<JsonObject> transmitPoleCountData(@Body JsonArray object);

    @POST("getAreaCodesBySectionId")
    Call<JsonObject> getAreaCodesBySectionId(@Body JsonObject object);

    @POST("getServiceConsumersDataByAreaCode")
    Call<JsonObject> getServiceConsumersDataByAreaCode(
            @Body JsonObject request);

    @POST("getSurvyedCSData")
    Call<JsonObject> getSurveyData(@Body JsonObject object);
}
