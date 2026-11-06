package com.apepdcl.gis_survey.pmi_old.module_11kv.retrofitservice;

import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIDTRMasterRequestVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIDTRMasterResponseMainVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIDTRSurveyRequestVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIFeederServiceMasterResponseMainVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIIncrementalLTSurveyRequestVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIIncrementalSurveyedDataReqVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMILTCktsReqVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMILTCktsResponseVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMILTImageRequest;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMILTSurveyRequestVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMILastSurveyRequestVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMILastSurveyResponseSSDataVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMILastSurveyResponseVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMILoginRequestVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMILoginResponseVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIMakeMasterResponseMainVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIMultiVoltageMasterResponseMainVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMISurveyedDataReqVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMISurveyedResponseVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMISuveyedResponseVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMITappingPointResponseVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMITappingPointktsReqVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMITappingPointsDataReqVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMITruncDataRequestVo;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PMIRetrofitService {
    @POST("gislogin")
    Call<PMILoginResponseVo> getLoginData(@Body PMILoginRequestVo loginRequestVo);

    @POST("gisltposting1")
    Call<PMISuveyedResponseVo> postSurveyData(@Body PMILTSurveyRequestVo loginRequestVo);

    @POST("gisltckt")
    Call<PMILTCktsResponseVo> getGisltckt(@Body PMILTCktsReqVo ltCktsReqVo);

    @POST("gislttoptenpole")
    Call<PMISurveyedResponseVo> getsurveyedData(@Body PMISurveyedDataReqVo surveyedDataReqVo);


    @POST("gisltthroughtline")
    Call<PMITappingPointResponseVo> getTappingPointData(@Body PMITappingPointktsReqVo surveyedDataReqVo);



    @POST("gisltthroughtlinepoleno")
    Call<PMISurveyedResponseVo> getTappingData(@Body PMITappingPointsDataReqVo surveyedDataReqVo);



    @POST("gispoledata")
    Call<JsonObject> getCompletedData(@Body JsonObject jsonObject);


    //TRANSMIT DTR SURVEY
    @POST("gisdtr")
    Call<PMISuveyedResponseVo> postDTRSurveyData(@Body PMIDTRSurveyRequestVo dtrSurveyRequestVo);

    @POST("dtrdetails")
    Call<JsonObject> getDTRData(@Body JsonObject dtrDetailsReqestVo);

    //Incremental
    @POST("incremental_gisltckt")
    Call<JsonObject> getIncrementalGisltckt(@Body PMILTCktsReqVo ltCktsReqVo);

    @POST("incremental_gislttopten")
    Call<PMISurveyedResponseVo> getIncrementalsurveyedData(@Body PMIIncrementalSurveyedDataReqVo incrementalSurveyedDataReqVo);


    @POST("ltwostatus")
    Call<JsonObject> getIncrementalWorkOrderStatusData(@Body JsonObject jsonObject);


    @POST("incrementalsurveyposting")
    Call<PMISuveyedResponseVo> posIncrementalLtSurveyData(@Body PMIIncrementalLTSurveyRequestVo loginRequestVo);

    @POST("incremental_mapltckt")
    Call<JsonObject> getIncrementalMapGisltckt(@Body PMILTCktsReqVo ltCktsReqVo);

    @POST("incrementalgispoledata")
    Call<JsonObject> getCompletedLtMap(@Body JsonObject jsonObject);

    //ADDED BY SOUBHAGYA
    @POST("imageposting")
    Call<PMISuveyedResponseVo> postLTImageData(@Body PMILTImageRequest loginRequestVo);

    @POST("checkgisdtr")
    Call<JsonObject> checkDTRSurvey(@Body JsonObject dtrDetailsReqestVo);

    @POST("ltmulticktdata")
    Call<JsonObject> getMultiCKTData(@Body JsonObject dtrDetailsReqestVo);

    @POST("ltmulticktdata2")
    Call<JsonObject> getMultiCKTData2(@Body JsonObject object);

    @POST("ltmulticktdata3")
    Call<JsonObject> getMultiCKTData3(@Body JsonObject object);

    @POST("feedermaster")
    Call<JsonObject> getFeederMaster(@Body JsonObject object);

    @POST("dtrupdateddetails")
    Call<JsonObject> updatedDTRDetails(@Body JsonObject object);

    @POST("updateGisLTSurveyComp")
    Call<JsonObject> updateSurveyStatus(@Body JsonObject dtrDetailsReqestVo);

    //Past survey last 10 records
    @POST("gissurveytopten")
    Call<PMILastSurveyResponseSSDataVo> getLastSurveyData(@Body PMILastSurveyRequestVo gissurveytopten);

    //Survey posting
    @POST("gisdtrdata")
    Call<PMIDTRMasterResponseMainVo> getDTRMasterData(@Body PMIDTRMasterRequestVo request);

    //Survey posting  HT Masetr Download
    @POST("gisfeederservice")
    Call<PMIFeederServiceMasterResponseMainVo> getFeederServiceMasterData(@Body PMIDTRMasterRequestVo request);

    //Multi Voltage
    @POST("getmultivoltagemaster")
    Call<PMIMultiVoltageMasterResponseMainVo> getMultiVoltageMasterData(@Body PMIDTRMasterRequestVo request);

    @POST("getdtrmakemaster")
    Call<PMIMakeMasterResponseMainVo> getMakeListMasterData(@Body PMIDTRMasterRequestVo request);

    @POST("gistrunksurveypoleno")
    Call<PMILastSurveyResponseVo> getTrncPoleData(@Body PMITruncDataRequestVo truncDataRequestVo);

    //ADDED BY SOUBHAGYA
    @POST("checkinLinePole")
    Call<JsonObject> checkInlinePole(@Body JsonObject request);

}
