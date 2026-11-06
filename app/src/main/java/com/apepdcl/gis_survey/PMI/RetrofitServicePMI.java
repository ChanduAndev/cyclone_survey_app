package com.apepdcl.gis_survey.PMI;

import com.apepdcl.gis_survey.module_33KV.vo.LastSurveyRequestVo;
import com.apepdcl.gis_survey.module_33KV.vo.LastSurveyResponseSSDataVo;
import com.apepdcl.gis_survey.vo.LoginResponseVo;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetrofitServicePMI {

    @POST("feederMasterNew")
    Call<JsonObject> getFeederMasterNew(@Body JsonObject object);

    @POST("gissurveytopten33")
    Call<JsonObject> getPoles33(@Body JsonObject jsonObject);

    @POST("gissurveytopten11")
    Call<JsonObject> getPoles11(@Body JsonObject jsonObject);

    @POST("gissurveytoptenLt")
    Call<JsonObject> getPolesLT(@Body JsonObject jsonObject);

    @POST("defectsDetails")
    Call<JsonObject> transmitData(@Body JsonArray jsonArray);

//    @POST("saveDefectsDetailsByGropWise")
//    Call<JsonObject> transmitDataNew(@Body JsonArray jsonArray);

    //@POST("saveDefectsDetailsByGropWiseLatest")
    @POST("saveDefectsSurvyedAfterCyclone")
    Call<JsonObject> transmitDataNew(@Body JsonArray jsonArray);

//    @GET("defectsmaster")
//    Call<JsonObject> getDefectMaster();

    @POST("defectsmaster")
    Call<JsonObject> getDefectMaster(@Body JsonObject jsonObject);

    @POST("userwisefeederslist")
    Call<JsonObject> userWiseDefectList(@Body JsonObject object);

    @POST("userwisefeederslistRect")
    Call<JsonObject> userWiseDefectListRectify(@Body JsonObject object);

    @POST("defectsSurveyLt")
    Call<JsonObject> getPolesLTRectify(@Body JsonObject jsonObject);

    @POST("defectsSurvey11kV")
    Call<JsonObject> getPoles11Rectify(@Body JsonObject jsonObject);

    @POST("defectsSurvey33kV")
    Call<JsonObject> getPoles33Rectify(@Body JsonObject jsonObject);

    @POST("new/defectsSurveyForPole11kV")
    Call<JsonObject> getPoles11RectifyNew(@Body JsonObject jsonObject);

    @POST("new/defectsSurveyForPole33kV")
    Call<JsonObject> getPoles33RectifyNew(@Body JsonObject jsonObject);

    @POST("new/defectsSurveyForPoleLt")
    Call<JsonObject> getPolesLTRectifyNew(@Body JsonObject jsonObject);



    @POST("new/updateRectifiedDtls")
    Call<JsonObject> transmitDataNewRectify(@Body JsonArray jsonArray);

    @POST("defectsmasterNew")
    Call<JsonObject> getDefectMasterNew(@Body JsonObject jsonObject);

    @POST("new/feederWiseSurvyedPoles")
    Call<JsonObject> getFeederWisePoleNo(@Body JsonObject jsonObject);

    @POST("new/defectsSurveyedDetailsFeederWise")
    Call<JsonObject> getDataForOffline(@Body JsonObject jsonObject);

    @GET("getDmsToken")
    Call<JsonObject> getDmsToken();


    @POST("feederMasters")
    Call<JsonObject> getArnabFeederMasterData(@Body JsonObject jsonObject);

    @POST("checkUserForOtherOpern")
    Call<LoginResponseVo> checkUser(@Body JsonObject jsonObject);


    @POST("saveUserInUserForOtherOpern")
    Call<LoginResponseVo> saveUser(@Body JsonObject jsonObject);


}
