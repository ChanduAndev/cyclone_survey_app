package com.apepdcl.gis_survey.RDSS;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by SOUBHAGYA SWAIN on 19-05-2025.
 */
public interface RetrofitServiceRDSS {
    @POST("feederMasterNew")
    Call<JsonObject> getFeederMaster(@Body JsonObject jsonObject);
    @POST("savesurvey")
    Call<JsonObject> getUGPostingData(@Body JsonArray jsonArray);

    @POST("getTrenchSurvey")
    Call<JsonObject> getTrenchSurvey(@Body JsonObject jsonObject);


    @POST("editsurveyLocation")
    Call<JsonObject> getEditSurveyLocation(@Body JsonObject jsonObject);


}
