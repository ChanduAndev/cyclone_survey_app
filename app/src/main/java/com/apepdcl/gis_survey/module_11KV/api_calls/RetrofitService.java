package com.apepdcl.gis_survey.module_11KV.api_calls;

import com.apepdcl.gis_survey.module_11KV.ImageRequest;
import com.apepdcl.gis_survey.module_11KV.models.CommanVO;
import com.apepdcl.gis_survey.module_11KV.models.DTRMasterRequestVo;
import com.apepdcl.gis_survey.module_11KV.models.DTRMasterResponseMainVo;
import com.apepdcl.gis_survey.module_11KV.models.FeederMasterRequestVo;
import com.apepdcl.gis_survey.module_11KV.models.FeederMasterResponseVo;
import com.apepdcl.gis_survey.module_11KV.models.FeederServiceMasterResponseMainVo;
import com.apepdcl.gis_survey.module_11KV.models.GisCoordinatsVO;
import com.apepdcl.gis_survey.module_11KV.models.IncrementalFeederMasterResponseVo;
import com.apepdcl.gis_survey.module_11KV.models.IncrementalLastSurveyRequestVo;
import com.apepdcl.gis_survey.module_11KV.models.IncrementalSurveyedRequestVo;
import com.apepdcl.gis_survey.module_11KV.models.LastSurveyRequestVo;
import com.apepdcl.gis_survey.module_11KV.models.LastSurveyResponseSSDataVo;
import com.apepdcl.gis_survey.module_11KV.models.LastSurveyResponseVo;
import com.apepdcl.gis_survey.module_11KV.models.LoginRequestVo;
import com.apepdcl.gis_survey.module_11KV.models.LoginResponseVo;
import com.apepdcl.gis_survey.module_11KV.models.MakeMasterResponseMainVo;
import com.apepdcl.gis_survey.module_11KV.models.MultiVoltageMasterResponseMainVo;
import com.apepdcl.gis_survey.module_11KV.models.SurveyedRequestVo;
import com.apepdcl.gis_survey.module_11KV.models.SuveyedResponseVo;
import com.apepdcl.gis_survey.module_11KV.models.TruncDataRequestVo;
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

   /* //Login Service
    @POST("logincheck")
    Call<LoginResponseVO> getLoginResponse(@Body LoginRequestVO vo);*/

    @POST("gislogin")
    Call<LoginResponseVo> getLoginData(@Body LoginRequestVo gisfeeder);

    //FeederMaster
    @POST("gisfeeder")
    Call<FeederMasterResponseVo> getFeederMaterData(@Body FeederMasterRequestVo gisfeeder);

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



    //Survey posting
    @POST("gisdtrdata")
    Call<DTRMasterResponseMainVo> getDTRMasterData(@Body DTRMasterRequestVo request);

    //Survey posting
    @POST("dtrmaster")
    Call<MakeMasterResponseMainVo> getMakeMasterData(@Body DTRMasterRequestVo request);

    //Survey posting  HT Masetr Download
    @POST("gisfeederservice")
    Call<FeederServiceMasterResponseMainVo> getFeederServiceMasterData(@Body DTRMasterRequestVo request);


    //Multi Voltage
    @POST("getmultivoltagemaster")
    Call<MultiVoltageMasterResponseMainVo> getMultiVoltageMasterData(@Body DTRMasterRequestVo request);

    @POST("getdtrmakemaster")
    Call<MakeMasterResponseMainVo> getMakeListMasterData(@Body DTRMasterRequestVo request);




    //Incremental

    @POST("gisnewfeeder")
    Call<IncrementalFeederMasterResponseVo> getIncrementalFeederMaterData(@Body FeederMasterRequestVo gisfeeder);

    @POST("gisnewsurveytopten")
    Call<LastSurveyResponseSSDataVo> getIncrementalLastSurveyData(@Body IncrementalLastSurveyRequestVo gissurveytopten);


    @POST("gisneworkorder")
    Call<JsonObject> getWorkOrderStatus(@Body JsonObject objectReq);


    @POST("gissurveynewposting")
    Call<SuveyedResponseVo> transmitIncrementalSurveyData(@Body IncrementalSurveyedRequestVo gissurveyposting);


    @POST("giscmpfeeder")
    Call<FeederMasterResponseVo> getCmpFeederMaterData(@Body FeederMasterRequestVo gisfeeder);

    // NEW Adding Pole Tapping Number
    @POST("addtappingpoint")
    Call<JsonObject> getAddTappingPoint(@Body JsonObject jsonObject);

    //ADDED BY SOUBHAGYA
    @POST("checkinLinePole")
    Call<JsonObject> checkInlinePole(@Body JsonObject request);

    @POST("inLinePole")
    Call<SuveyedResponseVo> inlinePole(@Body SurveyedRequestVo surveyVo);

    @POST("11kvimageposting")
    Call<SuveyedResponseVo> transmitImageData(@Body ImageRequest imageRequest);

    //MULTI CKT
    @POST("11kvmulticktdata")
    Call<JsonObject> getMultiCKTData(@Body JsonObject dtrDetailsReqestVo);

    @POST("11kvmulticktdata2")
    Call<JsonObject> getMultiCKTData2(@Body JsonObject object);

    @POST("11kvmulticktdata3")
    Call<JsonObject> getMultiCKTData3(@Body JsonObject object);

    @POST("11kvfeedermaster")
    Call<JsonObject> getFeederMaster(@Body JsonObject object);

    @POST("getPoleData")
    Call<JsonObject> getPoleData(@Body JsonObject object);

    @POST("updatePoleDetailsByMode")
    Call<JsonObject> updatePoleDetailsByMode(@Body JsonObject object);

    @POST("updateMultiplePoleDetailsByMode")
    Call<SuveyedResponseVo> updateLatLan(@Body JsonArray object);

}
