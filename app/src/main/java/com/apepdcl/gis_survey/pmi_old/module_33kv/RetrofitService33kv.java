package com.apepdcl.gis_survey.pmi_old.module_33kv;

import com.apepdcl.gis_survey.module_11KV.models.CommanVO;
import com.apepdcl.gis_survey.module_11KV.models.GisCoordinatsVO;
import com.apepdcl.gis_survey.pmi_old.module_11kv.PMIFeederMasterRequestVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIFeederMasterResponseVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIImageRequest;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIIncrementalFeederMasterResponseVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIIncrementalLastSurveyRequestVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIIncrementalSurveyedRequestVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMILastSurveyRequestVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMILastSurveyResponseSSDataVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMILastSurveyResponseVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMILoginRequestVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMILoginResponseVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMISurveyedRequestVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMISuveyedResponseVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMITruncDataRequestVo;
import com.apepdcl.gis_survey.pmi_old.module_33kv.vo.SurveyedRequestVo33KV;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitService33kv {
    //Login Service
    @POST("gisservey")
    Call<CommanVO> getGisResponse(@Body GisCoordinatsVO vo);

    //Login Service
    //  @POST("logincheck")
    //  Call<LoginResponseVO> getLoginResponse(@Body LoginRequestVO vo);

    @POST("gislogin")
    Call<PMILoginResponseVo> getLoginData(@Body PMILoginRequestVo gisfeeder);

    //FeederMaster
    @POST("gisfeeder")
    Call<PMIFeederMasterResponseVo> getFeederMaterData(@Body PMIFeederMasterRequestVo gisfeeder);


    @POST("gispolefeederData")
    Call<PMIFeederMasterResponseVo> getCmpFeederMaterData(@Body PMIFeederMasterRequestVo gisfeeder);
    //Past survey last 10 records
    @POST("gissurveytopten")
    Call<PMILastSurveyResponseSSDataVo> getLastSurveyData(@Body PMILastSurveyRequestVo gissurveytopten);



    @POST("gistrunksurvey")
    Call<PMILastSurveyResponseVo> getTappingData(@Body PMILastSurveyRequestVo gistrunksurvey);

    @POST("gistrunksurveypoleno")
    Call<PMILastSurveyResponseVo> getTrncPoleData(@Body PMITruncDataRequestVo truncDataRequestVo);

    //Survey posting
    @POST("gissurveyposting")
    Call<PMISuveyedResponseVo> transmitData(@Body SurveyedRequestVo33KV gissurveyposting);

    //Survey posting
    @POST("gispoledata")
    Call<JsonObject> getCompletedData(@Body JsonObject request);

    @POST("gisnewpoledata")
    Call<JsonObject> getIncrementalCompletedData(@Body JsonObject request);


    //Incremental

    @POST("gisNewfeeder")
    Call<PMIIncrementalFeederMasterResponseVo> getIncrementalFeederData(@Body PMIFeederMasterRequestVo gisfeeder);

    @POST("gisnewsurveytopten")
    Call<PMILastSurveyResponseSSDataVo> getIncrementalLastSurveyData(@Body PMIIncrementalLastSurveyRequestVo gissurveytopten);

    @POST("gisnewsurveyposting")
    Call<PMISuveyedResponseVo> incrementalTransmitData(@Body PMIIncrementalSurveyedRequestVo gissurveyposting);

    @POST("newgispolefeederData")
    Call<PMIFeederMasterResponseVo> getIncrementalCmpFeederMaterData(@Body PMIFeederMasterRequestVo gisfeeder);

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
    Call<PMISuveyedResponseVo> transmitImageData(@Body PMIImageRequest imageRequest);

    @POST("33kvaddtappingpoint")
    Call<JsonObject> getAddTappingPoint(@Body JsonObject jsonObject);

    @POST("33kvcheckinLinePole")
    Call<JsonObject> checkInlinePole(@Body JsonObject jsonObject);

    @POST("33kvinLinePole")
    Call<PMISuveyedResponseVo> inlinePole(@Body PMISurveyedRequestVo surveyVo);
}
