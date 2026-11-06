package com.apepdcl.gis_survey.METER_SURVEY;


import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

/**
 * Created by SOUBHAGYA SWAIN on 12-06-2026.
 */
public interface ImageAPIServiceMeter {
    @Multipart
    @POST("upload")
    Call<JsonObject> uploadImage(
            @Part("access_token") RequestBody accessToken,
            @Part("account_id") RequestBody accountId,
            @Part("folder_path") RequestBody folderPath,
            @Part MultipartBody.Part uploadFile
    );
}
