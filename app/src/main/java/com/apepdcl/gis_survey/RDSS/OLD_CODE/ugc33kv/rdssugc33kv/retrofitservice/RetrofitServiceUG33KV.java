package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.retrofitservice;

import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.retrofitservice.responsevo.CommanVOUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.FeederMasterRequestVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.FeederMasterResponseVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.GisCoordinatsVOUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.IncrementalFeederMasterResponseVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.IncrementalLastSurveyRequestVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.IncrementalSurveyedRequestVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.LastSurveyRequestVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.LastSurveyResponseSSDataVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.LastSurveyResponseVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.LoginRequestVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.LoginResponseVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.SurveyedRequestVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.SuveyedResponseVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.TruncDataRequestVoUG33KV;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**

 */
public interface RetrofitServiceUG33KV {

    //Login Service
    @POST("gisservey")
    Call<CommanVOUG33KV> getGisResponse(@Body GisCoordinatsVOUG33KV vo);

    //Login Service
  //  @POST("logincheck")
  //  Call<LoginResponseVO> getLoginResponse(@Body LoginRequestVO vo);

    @POST("gislogin")
    Call<LoginResponseVoUG33KV> getLoginData(@Body LoginRequestVoUG33KV gisfeeder);

//FeederMaster
    @POST("gisfeeder")
    Call<FeederMasterResponseVoUG33KV> getFeederMaterData(@Body FeederMasterRequestVoUG33KV gisfeeder);


    @POST("gispolefeederData")
    Call<FeederMasterResponseVoUG33KV> getCmpFeederMaterData(@Body FeederMasterRequestVoUG33KV gisfeeder);
//Past survey last 10 records
    @POST("gissurveytopten")
    Call<LastSurveyResponseSSDataVoUG33KV> getLastSurveyData(@Body LastSurveyRequestVoUG33KV gissurveytopten);



    @POST("gistrunksurvey")
    Call<LastSurveyResponseVoUG33KV> getTappingData(@Body LastSurveyRequestVoUG33KV gistrunksurvey);

    @POST("gistrunksurveypoleno")
    Call<LastSurveyResponseVoUG33KV> getTrncPoleData(@Body TruncDataRequestVoUG33KV truncDataRequestVoUG33KV);

    //Survey posting
    @POST("gissurveyposting")
    Call<SuveyedResponseVoUG33KV> transmitData(@Body SurveyedRequestVoUG33KV gissurveyposting);

    //Survey posting
    @POST("gispoledata")
    Call<JsonObject> getCompletedData(@Body JsonObject request);

    @POST("gisnewpoledata")
    Call<JsonObject> getIncrementalCompletedData(@Body JsonObject request);


    //Incremental

    @POST("gisNewfeeder")
    Call<IncrementalFeederMasterResponseVoUG33KV> getIncrementalFeederData(@Body FeederMasterRequestVoUG33KV gisfeeder);

    @POST("gisnewsurveytopten")
    Call<LastSurveyResponseSSDataVoUG33KV> getIncrementalLastSurveyData(@Body IncrementalLastSurveyRequestVoUG33KV gissurveytopten);

    @POST("gisnewsurveyposting")
    Call<SuveyedResponseVoUG33KV> incrementalTransmitData(@Body IncrementalSurveyedRequestVoUG33KV gissurveyposting);

    @POST("newgispolefeederData")
    Call<FeederMasterResponseVoUG33KV> getIncrementalCmpFeederMaterData(@Body FeederMasterRequestVoUG33KV gisfeeder);

    @POST("gisneworkorder")
    Call<JsonObject> getIncrementalWorkOrderStatusData(@Body JsonObject jsonObject);
}
