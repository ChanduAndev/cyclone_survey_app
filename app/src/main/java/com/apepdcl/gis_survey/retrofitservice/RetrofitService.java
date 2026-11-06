package com.apepdcl.gis_survey.retrofitservice;

import com.apepdcl.gis_survey.LTImageRequest;
import com.apepdcl.gis_survey.vo.DTRSurveyRequestVo;
import com.apepdcl.gis_survey.vo.IncrementalLTSurveyRequestVo;
import com.apepdcl.gis_survey.vo.IncrementalSurveyedDataReqVo;
import com.apepdcl.gis_survey.vo.LTCktsReqVo;
import com.apepdcl.gis_survey.vo.LTCktsResponseVo;
import com.apepdcl.gis_survey.vo.LTSurveyRequestVo;
import com.apepdcl.gis_survey.vo.LoginRequestVo;
import com.apepdcl.gis_survey.vo.LoginResponseVo;
import com.apepdcl.gis_survey.vo.SurveyedDataReqVo;
import com.apepdcl.gis_survey.vo.SurveyedResponseVo;
import com.apepdcl.gis_survey.vo.SuveyedResponseVo;
import com.apepdcl.gis_survey.vo.TappingPointResponseVo;
import com.apepdcl.gis_survey.vo.TappingPointktsReqVo;
import com.apepdcl.gis_survey.vo.TappingPointsDataReqVo;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**

 */
public interface RetrofitService {

    @POST("gislogin")
    Call<LoginResponseVo> getLoginData(@Body LoginRequestVo loginRequestVo);

    @POST("gisltposting1")
    Call<SuveyedResponseVo> postSurveyData(@Body LTSurveyRequestVo loginRequestVo);

    @POST("gisltckt")
    Call<LTCktsResponseVo> getGisltckt(@Body LTCktsReqVo ltCktsReqVo);

    @POST("gislttoptenpole")
    Call<SurveyedResponseVo> getsurveyedData(@Body SurveyedDataReqVo surveyedDataReqVo);


    @POST("gisltthroughtline")
    Call<TappingPointResponseVo> getTappingPointData(@Body TappingPointktsReqVo surveyedDataReqVo);

    @POST("gisltthroughtlinepoleno")
    Call<SurveyedResponseVo> getTappingData(@Body TappingPointsDataReqVo surveyedDataReqVo);

    @POST("gispoledata")
    Call<JsonObject> getCompletedData(@Body JsonObject jsonObject);


    //TRANSMIT DTR SURVEY
    @POST("gisdtr")
    Call<SuveyedResponseVo> postDTRSurveyData(@Body DTRSurveyRequestVo dtrSurveyRequestVo);

    @POST("dtrdetails")
    Call<JsonObject> getDTRData(@Body JsonObject dtrDetailsReqestVo);

    //Incremental
    @POST("incremental_gisltckt")
    Call<JsonObject> getIncrementalGisltckt(@Body LTCktsReqVo ltCktsReqVo);

    @POST("incremental_gislttopten")
    Call<SurveyedResponseVo> getIncrementalsurveyedData(@Body IncrementalSurveyedDataReqVo incrementalSurveyedDataReqVo);


    @POST("ltwostatus")
    Call<JsonObject> getIncrementalWorkOrderStatusData(@Body JsonObject jsonObject);


    @POST("incrementalsurveyposting")
    Call<SuveyedResponseVo> posIncrementalLtSurveyData(@Body IncrementalLTSurveyRequestVo loginRequestVo);

    @POST("incremental_mapltckt")
    Call<JsonObject> getIncrementalMapGisltckt(@Body LTCktsReqVo ltCktsReqVo);

    @POST("incrementalgispoledata")
    Call<JsonObject> getCompletedLtMap(@Body JsonObject jsonObject);

    //ADDED BY SOUBHAGYA
    @POST("imageposting")
    Call<SuveyedResponseVo> postLTImageData(@Body LTImageRequest loginRequestVo);

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

    @POST("feederMasterNew")
    Call<JsonObject> getFeederMasterNew(@Body JsonObject object);

    @POST("getPoleData")
    Call<JsonObject> getPoleData(@Body JsonObject object);

    @POST("updatePoleDetailsByMode")
    Call<JsonObject> updatePoleDetailsByMode(@Body JsonObject object);

    @POST("updateMultiplePoleDetailsByMode")
    Call<SuveyedResponseVo> updateLatLan(@Body JsonArray object);


}
