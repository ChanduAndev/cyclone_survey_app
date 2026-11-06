package com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.retrofitservice;

import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.retrofitservice.responsevo.CommanVO;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.DTRMasterRequestVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.DTRMasterResponseMainVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.FeederMasterRequestVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.FeederMasterResponseVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.FeederServiceMasterResponseMainVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.GisCoordinatsVO;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.IncrementalFeederMasterResponseVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.IncrementalLastSurveyRequestVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.IncrementalSurveyedRequestVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.LastSurveyRequestVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.LastSurveyResponseSSDataVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.LastSurveyResponseVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.LoginRequestVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.LoginResponseVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.MakeMasterResponseMainVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.MultiVoltageMasterResponseMainVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.SurveyedRequestVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.SuveyedResponseVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.TruncDataRequestVo;
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
}
