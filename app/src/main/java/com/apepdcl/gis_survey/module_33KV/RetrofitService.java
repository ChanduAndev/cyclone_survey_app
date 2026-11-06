package com.apepdcl.gis_survey.module_33KV;

import com.apepdcl.gis_survey.module_11KV.ImageRequest;
import com.apepdcl.gis_survey.module_11KV.models.CommanVO;
import com.apepdcl.gis_survey.module_33KV.vo.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**

 */
public interface RetrofitService {

    //Login Service
    @POST("gisservey")
    Call<CommanVO> getGisResponse(@Body GisCoordinatsVO vo);

    //Login Service
  //  @POST("logincheck")
  //  Call<LoginResponseVO> getLoginResponse(@Body LoginRequestVO vo);

    @POST("gislogin")
    Call<LoginResponseVo> getLoginData(@Body LoginRequestVo gisfeeder);

//FeederMaster
    @POST("gisfeeder")
    Call<FeederMasterResponseVo> getFeederMaterData(@Body FeederMasterRequestVo gisfeeder);


    @POST("gispolefeederData")
    Call<FeederMasterResponseVo> getCmpFeederMaterData(@Body FeederMasterRequestVo gisfeeder);
//Past survey last 10 records
    @POST("gissurveytopten")
    Call<LastSurveyResponseSSDataVo> getLastSurveyData(@Body LastSurveyRequestVo gissurveytopten);



    @POST("gistrunksurvey")
    Call<LastSurveyResponseVo> getTappingData(@Body LastSurveyRequestVo gistrunksurvey);

    @POST("gistrunksurveypoleno")
    Call<LastSurveyResponseVo> getTrncPoleData(@Body TruncDataRequestVo truncDataRequestVo);

    //Survey posting
    @POST("gissurveyposting")
    Call<SuveyedResponseVo> transmitData(@Body SurveyedRequestVo gissurveyposting);

    //Survey posting
    @POST("gispoledata")
    Call<JsonObject> getCompletedData(@Body JsonObject request);

    @POST("gisnewpoledata")
    Call<JsonObject> getIncrementalCompletedData(@Body JsonObject request);


    //Incremental

    @POST("gisNewfeeder")
    Call<IncrementalFeederMasterResponseVo> getIncrementalFeederData(@Body FeederMasterRequestVo gisfeeder);

    @POST("gisnewsurveytopten")
    Call<LastSurveyResponseSSDataVo> getIncrementalLastSurveyData(@Body IncrementalLastSurveyRequestVo gissurveytopten);

    @POST("gisnewsurveyposting")
    Call<SuveyedResponseVo> incrementalTransmitData(@Body IncrementalSurveyedRequestVo gissurveyposting);

    @POST("newgispolefeederData")
    Call<FeederMasterResponseVo> getIncrementalCmpFeederMaterData(@Body FeederMasterRequestVo gisfeeder);

    @POST("gisneworkorder")
    Call<JsonObject> getIncrementalWorkOrderStatusData(@Body JsonObject jsonObject);

    //NEW
    //MULTI CKT
    @POST("33kvmulticktdata")
    Call<JsonObject> getMultiCKTData(@Body JsonObject dtrDetailsReqestVo);

    @POST("33kvmulticktdata2")
    Call<JsonObject> getMultiCKTData2(@Body JsonObject object);

    @POST("33kvmulticktdata3")
    Call<JsonObject> getMultiCKTData3(@Body JsonObject object);

    @POST("33kvfeedermaster")
    Call<JsonObject> getFeederMaster(@Body JsonObject object);

    @POST("33kvimageposting")
    Call<SuveyedResponseVo> transmitImageData(@Body ImageRequest imageRequest);

    @POST("33kvaddtappingpoint")
    Call<JsonObject> getAddTappingPoint(@Body JsonObject jsonObject);

    @POST("33kvcheckinLinePole")
    Call<JsonObject> checkInlinePole(@Body JsonObject jsonObject);

    @POST("33kvinLinePole")
    Call<SuveyedResponseVo> inlinePole(@Body SurveyedRequestVo surveyVo);

    @POST("htScnoList")
    Call<JsonObject> htScnoList(@Body JsonObject surveyVo);

    @POST("gisPoleData")
    Call<JsonObject> getPoleData(@Body JsonObject surveyVo);

    @POST("updatePoleDetailsByMode")
    Call<JsonObject> updatePoleDetailsByMode(@Body JsonObject object);

    @POST("updateMultiplePoleDetailsByMode")
    Call<SuveyedResponseVo> updateMultipleDataByMode(@Body JsonArray jsonArray);

}
