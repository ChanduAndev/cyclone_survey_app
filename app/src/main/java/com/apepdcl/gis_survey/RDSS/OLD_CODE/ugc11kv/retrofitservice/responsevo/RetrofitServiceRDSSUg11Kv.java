package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.retrofitservice.responsevo;

import com.apepdcl.gis_survey.module_11KV.models.CommanVO;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.DTRMasterRequestVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.DTRMasterResponseMainVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.FeederMasterRequestVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.FeederMasterResponseVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.FeederServiceMasterResponseMainVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.GisCoordinatsVORDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.IncrementalFeederMasterResponseVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.IncrementalLastSurveyRequestVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.IncrementalSurveyedRequestVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.LastSurveyRequestVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.LastSurveyResponseSSDataVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.LastSurveyResponseVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.LoginRequestVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.LoginResponseVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.MakeMasterResponseMainVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.MultiVoltageMasterResponseMainVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.SurveyedRequestVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.SuveyedResponseVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.TruncDataRequestVoRDSSUg11Kv;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**

 */
public interface RetrofitServiceRDSSUg11Kv {

    //Login Service
    @POST("gisservey")
    Call<CommanVO> getGisResponse(@Body GisCoordinatsVORDSSUg11Kv vo);

   /* //Login Service
    @POST("logincheck")
    Call<LoginResponseVO> getLoginResponse(@Body LoginRequestVO vo);*/

    @POST("gislogin")
    Call<LoginResponseVoRDSSUg11Kv> getLoginData(@Body LoginRequestVoRDSSUg11Kv gisfeeder);

    //FeederMaster
    @POST("gisfeeder")
    Call<FeederMasterResponseVoRDSSUg11Kv> getFeederMaterData(@Body FeederMasterRequestVoRDSSUg11Kv gisfeeder);

    //Past survey last 10 records
    @POST("gissurveytopten")
    Call<LastSurveyResponseSSDataVoRDSSUg11Kv> getLastSurveyData(@Body LastSurveyRequestVoRDSSUg11Kv gissurveytopten);

    @POST("gistrunksurvey")
    Call<LastSurveyResponseVoRDSSUg11Kv> getTappingData(@Body LastSurveyRequestVoRDSSUg11Kv gistrunksurvey);

    @POST("gistrunksurveypoleno")
    Call<LastSurveyResponseVoRDSSUg11Kv> getTrncPoleData(@Body TruncDataRequestVoRDSSUg11Kv truncDataRequestVoRDSSUg11Kv);

    //Survey posting
    @POST("gissurveyposting")
    Call<SuveyedResponseVoRDSSUg11Kv> transmitData(@Body SurveyedRequestVoRDSSUg11Kv gissurveyposting);

    //Survey posting
    @POST("gispoledata")
    Call<JsonObject> getCompletedData(@Body JsonObject request);

    @POST("gisnewpoledata")
    Call<JsonObject> getIncrementalCompletedData(@Body JsonObject request);



    //Survey posting
    @POST("gisdtrdata")
    Call<DTRMasterResponseMainVoRDSSUg11Kv> getDTRMasterData(@Body DTRMasterRequestVoRDSSUg11Kv request);

    //Survey posting
    @POST("dtrmaster")
    Call<MakeMasterResponseMainVoRDSSUg11Kv> getMakeMasterData(@Body DTRMasterRequestVoRDSSUg11Kv request);

    //Survey posting  HT Masetr Download
    @POST("gisfeederservice")
    Call<FeederServiceMasterResponseMainVoRDSSUg11Kv> getFeederServiceMasterData(@Body DTRMasterRequestVoRDSSUg11Kv request);


    //Multi Voltage
    @POST("getmultivoltagemaster")
    Call<MultiVoltageMasterResponseMainVoRDSSUg11Kv> getMultiVoltageMasterData(@Body DTRMasterRequestVoRDSSUg11Kv request);

    @POST("getdtrmakemaster")
    Call<MakeMasterResponseMainVoRDSSUg11Kv> getMakeListMasterData(@Body DTRMasterRequestVoRDSSUg11Kv request);




    //Incremental

    @POST("gisnewfeeder")
    Call<IncrementalFeederMasterResponseVoRDSSUg11Kv> getIncrementalFeederMaterData(@Body FeederMasterRequestVoRDSSUg11Kv gisfeeder);

    @POST("gisnewsurveytopten")
    Call<LastSurveyResponseSSDataVoRDSSUg11Kv> getIncrementalLastSurveyData(@Body IncrementalLastSurveyRequestVoRDSSUg11Kv gissurveytopten);


    @POST("gisneworkorder")
    Call<JsonObject> getWorkOrderStatus(@Body JsonObject objectReq);


    @POST("gissurveynewposting")
    Call<SuveyedResponseVoRDSSUg11Kv> transmitIncrementalSurveyData(@Body IncrementalSurveyedRequestVoRDSSUg11Kv gissurveyposting);


    @POST("giscmpfeeder")
    Call<FeederMasterResponseVoRDSSUg11Kv> getCmpFeederMaterData(@Body FeederMasterRequestVoRDSSUg11Kv gisfeeder);

    @POST("gisjhcLayOutFeedrs")
    Call<JsonObject> gisjhcLayOutFeedrs(@Body JsonObject gisfeeder);


    // NEW Adding Pole Tapping Number
    @POST("addtappingpoint")
    Call<JsonObject> getAddTappingPoint(@Body JsonObject jsonObject);

    @POST("checkinLinePole")
    Call<JsonObject> checkInlinePole(@Body JsonObject request);

    @POST("inLinePole")
    Call<SuveyedResponseVoRDSSUg11Kv> inLinePole(@Body SurveyedRequestVoRDSSUg11Kv gissurveyposting);
}
