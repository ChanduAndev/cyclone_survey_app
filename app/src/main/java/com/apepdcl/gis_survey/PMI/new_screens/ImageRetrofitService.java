package com.apepdcl.gis_survey.PMI.new_screens;


import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by SOUBHAGYA SWAIN on 03-09-2025.
 */
public interface ImageRetrofitService {
    @Multipart
    @POST("process")
        // Replace with your actual upload endpoint
    Call<JsonObject> uploadImage(
            @Part MultipartBody.Part image,
            @Query("simplified") boolean simplified
    );

    @Multipart
    @POST("process-dtr-plinth-clearance")
    Call<JsonObject> uploadImageDTRPlinthClearance(
            @Part MultipartBody.Part file,
            @Query("pole_height_meters") String poleHeightMeters
    );


}
