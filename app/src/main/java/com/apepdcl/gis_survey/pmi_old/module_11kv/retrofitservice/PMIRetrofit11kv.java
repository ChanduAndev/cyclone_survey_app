package com.apepdcl.gis_survey.pmi_old.module_11kv.retrofitservice;

import com.apepdcl.gis_survey.module_11KV.models.CommanVO;
import com.apepdcl.gis_survey.module_11KV.models.GisCoordinatsVO;
import com.apepdcl.gis_survey.pmi_old.module_11kv.PMIFeederMasterRequestVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIDTRMasterRequestVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIDTRMasterResponseMainVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIFeederMasterResponseVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIFeederServiceMasterResponseMainVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIImageRequest;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIIncrementalFeederMasterResponseVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIIncrementalLastSurveyRequestVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIIncrementalSurveyedRequestVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMILastSurveyRequestVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMILastSurveyResponseSSDataVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMILastSurveyResponseVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMILoginRequestVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMILoginResponseVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIMakeMasterResponseMainVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIMultiVoltageMasterResponseMainVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMISurveyedRequestVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMISuveyedResponseVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMITruncDataRequestVo;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PMIRetrofit11kv {
    //Login Service
    @POST("gisservey")
    Call<CommanVO> getGisResponse(@Body GisCoordinatsVO vo);

   /* //Login Service
    @POST("logincheck")
    Call<LoginResponseVO> getLoginResponse(@Body LoginRequestVO vo);*/

    @POST("gislogin")
    Call<PMILoginResponseVo> getLoginData(@Body PMILoginRequestVo gisfeeder);

    //FeederMaster
    @POST("gisfeeder")
    Call<PMIFeederMasterResponseVo> getFeederMaterData(@Body PMIFeederMasterRequestVo gisfeeder);

    //Past survey last 10 records
    @POST("gissurveytopten")
    Call<PMILastSurveyResponseSSDataVo> getLastSurveyData(@Body PMILastSurveyRequestVo gissurveytopten);

    @POST("gistrunksurvey")
    Call<PMILastSurveyResponseVo> getTappingData(@Body PMILastSurveyRequestVo gistrunksurvey);

    @POST("gistrunksurveypoleno")
    Call<PMILastSurveyResponseVo> getTrncPoleData(@Body PMITruncDataRequestVo truncDataRequestVo);

    //Survey posting
    @POST("gissurveyposting")
    Call<PMISuveyedResponseVo> transmitData(@Body PMISurveyedRequestVo gissurveyposting);

    //Survey posting
    @POST("gispoledata")
    Call<JsonObject> getCompletedData(@Body JsonObject request);

    @POST("gisnewpoledata")
    Call<JsonObject> getIncrementalCompletedData(@Body JsonObject request);



    //Survey posting
    @POST("gisdtrdata")
    Call<PMIDTRMasterResponseMainVo> getDTRMasterData(@Body PMIDTRMasterRequestVo request);

    //Survey posting
    @POST("dtrmaster")
    Call<PMIMakeMasterResponseMainVo> getMakeMasterData(@Body PMIDTRMasterRequestVo request);

    //Survey posting  HT Masetr Download
    @POST("gisfeederservice")
    Call<PMIFeederServiceMasterResponseMainVo> getFeederServiceMasterData(@Body PMIDTRMasterRequestVo request);


    //Multi Voltage
    @POST("getmultivoltagemaster")
    Call<PMIMultiVoltageMasterResponseMainVo> getMultiVoltageMasterData(@Body PMIDTRMasterRequestVo request);

    @POST("getdtrmakemaster")
    Call<PMIMakeMasterResponseMainVo> getMakeListMasterData(@Body PMIDTRMasterRequestVo request);




    //Incremental

    @POST("gisnewfeeder")
    Call<PMIIncrementalFeederMasterResponseVo> getIncrementalFeederMaterData(@Body PMIFeederMasterRequestVo gisfeeder);

    @POST("gisnewsurveytopten")
    Call<PMILastSurveyResponseSSDataVo> getIncrementalLastSurveyData(@Body PMIIncrementalLastSurveyRequestVo gissurveytopten);


    @POST("gisneworkorder")
    Call<JsonObject> getWorkOrderStatus(@Body JsonObject objectReq);


    @POST("gissurveynewposting")
    Call<PMISuveyedResponseVo> transmitIncrementalSurveyData(@Body PMIIncrementalSurveyedRequestVo gissurveyposting);


    @POST("giscmpfeeder")
    Call<PMIFeederMasterResponseVo> getCmpFeederMaterData(@Body PMIFeederMasterRequestVo gisfeeder);

    // NEW Adding Pole Tapping Number
    @POST("addtappingpoint")
    Call<JsonObject> getAddTappingPoint(@Body JsonObject jsonObject);

    //ADDED BY SOUBHAGYA
    @POST("checkinLinePole")
    Call<JsonObject> checkInlinePole(@Body JsonObject request);

    @POST("inLinePole")
    Call<PMISuveyedResponseVo> inlinePole(@Body PMISurveyedRequestVo surveyVo);

    @POST("11kvimageposting")
    Call<PMISuveyedResponseVo> transmitImageData(@Body PMIImageRequest imageRequest);

    //MULTI CKT
    @POST("11kvmulticktdata")
    Call<JsonObject> getMultiCKTData(@Body JsonObject dtrDetailsReqestVo);

    @POST("11kvmulticktdata2")
    Call<JsonObject> getMultiCKTData2(@Body JsonObject object);

    @POST("11kvmulticktdata3")
    Call<JsonObject> getMultiCKTData3(@Body JsonObject object);

    @POST("11kvfeedermaster")
    Call<JsonObject> getFeederMaster(@Body JsonObject object);

    @POST("11kvDefectsRectfied")
    Call<JsonObject> getDefectedPolesList(@Body JsonObject object);

    @POST("11kvDefectsRectfiedposting")
    Call<JsonObject> updateDefectPole(@Body JsonObject object);

    @POST("11kvDefectFeeders")
    Call<PMIFeederMasterResponseVo> getDefectFeederMasterData(@Body PMIFeederMasterRequestVo gisfeeder);
}
