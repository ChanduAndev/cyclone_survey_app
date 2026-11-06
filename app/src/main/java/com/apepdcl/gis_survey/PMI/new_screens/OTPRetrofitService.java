package com.apepdcl.gis_survey.PMI.new_screens;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OTPRetrofitService {
    @POST("v1.0/otpInit")
    Call<JsonObject> sendOTP(@Body JsonObject jsonObject);

    @POST("v1.0/otpVerification")
    Call<JsonObject> verifyOTP(@Body JsonObject jsonObject);
}
