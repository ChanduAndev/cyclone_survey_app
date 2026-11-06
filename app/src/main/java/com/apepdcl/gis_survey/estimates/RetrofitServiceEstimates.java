package com.apepdcl.gis_survey.estimates;

import com.apepdcl.gis_survey.vo.LoginRequestVo;
import com.apepdcl.gis_survey.vo.LoginResponseVo;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Soubhagya Swain on 07,June,2024
 */
public interface RetrofitServiceEstimates {

    @POST("gislogin")
    Call<LoginResponseVo> getLoginData(@Body LoginRequestVo loginRequestVo);

    @POST("estsaps")
    Call<JsonObject> getPurpose(@Body JsonObject request);

    @POST("estPosting")
    Call<JsonObject> postingData(@Body JsonArray request);

    @POST("feederMaster")
    Call<JsonObject> getFeederMaster(@Body JsonObject request);

    //NEW DTR CHANGES BY CHANDU


    @POST("estFeederPosting")
    Call<JsonObject> postingFeederMaster(@Body JsonArray request);

    @GET("poleTypes")
    Call<JsonObject> getPoleTypes();

    @GET("conductorTypes")
    Call<JsonObject> getCableTypes();

    @GET("dtrTypes")
    Call<JsonObject> getDTRTypes();

    @GET("materialMaster")
    Call<JsonObject> getMaterialMaster();


    @POST("estData")
    Call<JsonObject> getEstData(@Body JsonObject request);

    // @POST("updateEstStatus")
    @POST("updateEstStatusTwo")
    Call<JsonObject> approveSurvey(@Body JsonArray request);

    @POST("updateEstProgress")
    Call<JsonObject> completeSurvey(@Body JsonArray request);

    //NEW
    @POST("sapMaterialsMasterNew")
    Call<JsonObject> getMaterials(@Body JsonObject request);

    //OLD
//    @POST("sapMaterialsMaster")
//    Call<JsonObject> getMaterials(@Body JsonObject request);

    @POST("estPostingNew")
    Call<JsonObject> postingDataNew(@Body JsonArray request);

    @POST("estMeterialsPosting")
    Call<JsonObject> postingMaterialNew(@Body JsonArray request);

    @POST("existingPoleData")
    Call<JsonObject> getExistingPoleData(@Body JsonObject request);

    @POST("addWorkOrderDataToAsset")
    Call<JsonObject> workOrderStatusToAssets(@Body JsonObject request);

    @POST("estboqDetails")
    Call<JsonObject> estBoqData(@Body JsonObject request);

    @POST("estLengthDetails")
    Call<JsonObject> estLengthDetails(@Body JsonObject request);

    @POST("trtythreehstclist")
    Call<JsonObject> htServiceList33KV(@Body JsonObject request);

    @POST("elevKvhstclist")
    Call<JsonObject> htServiceList11KV(@Body JsonObject request);

//OLD
//    @POST("updatedEstDtlsPosting")
//    Call<JsonObject> postingTransmittedData(@Body JsonArray request);

    //NEW
    @POST("updatedEstDtlsPostingNew")
    Call<JsonObject> postingTransmittedData(@Body JsonArray request);
//OLD
//    @POST("updatedMaterialDtlsPosing")
//    Call<JsonObject> postingMaterialTransmitted(@Body JsonArray request);

    //NEW
    @POST("updatedMaterialDtlsPosingNew")
    Call<JsonObject> postingMaterialTransmitted(@Body JsonArray request);


    @POST("updateLatLong")
    Call<JsonObject> updateLocations(@Body JsonArray request);

    @GET("distanceValidationdtls")
    Call<JsonObject> getPoleDistanceValidation();

    @GET("estFeaturesInfo")
    Call<JsonObject> getInfo();

    @GET("ltLineConfig")
    Call<JsonObject> getLTLineConfigData();

    @GET("gisBanner")
    Call<JsonObject> getBannerImages();

    @POST("exstpoleDataForDtr")
    Call<JsonObject> getExistingPoleDataForLT(@Body JsonObject request);

    @POST("existingLTSecWiseData")
    Call<JsonObject> getexistingLTSecWiseData(@Body JsonObject request);

    @POST("dbCounts")
    Call<JsonObject> getEstimateCount(@Body JsonObject request);


    @POST("estimateswithpolecnts")
    Call<JsonObject> estimateswithpolecnts(@Body JsonObject request);

    @POST("estimateswithpolecntsforIntg")
    Call<JsonObject> estimateswithpolecntsforIntg(@Body JsonObject request);

    @POST("singleEstimateWithFdrs")
    Call<JsonObject> singleEstimateWithFdrs(@Body JsonObject request);

    @GET("elevenkvfeedermaster")
    Call<JsonObject> getElevenkvfeedermaster();

    @GET("proposedFeedersMaster")
    Call<JsonObject> getProposedFeederMaster();

    @GET("thirtythreekvfeedermaster")
    Call<JsonObject> getThirtythreekvfeedermaster();


    @POST("dtrmaster")
    Call<JsonObject> getdtrmaster(@Body JsonObject request);

    @POST("workorderIntegarteAndApprovaldata")
    Call<JsonObject> workorderIntegarteAndApprovaldata(@Body JsonObject request);


    @GET("dtrMakeMasters")
    Call<JsonObject> dtrMakeMasters();

    @POST("cktWisePoleCounts")
    Call<JsonObject> cktWisePoleCounts(@Body JsonObject request);

//OLD
//    @POST("updatedDtrDetails")
//    Call<JsonObject> updatedDtrDetails(@Body JsonArray request);

    //NEW
    @POST("updatedDtrDetailsNew")
    Call<JsonObject> updatedDtrDetails(@Body JsonArray request);

    @GET("helpPoints")
    Call<JsonObject> getHelpPoints();

    @GET("feederloadpoles")
    Call<JsonObject> getFeederLoadsPoles();


    @GET("dtrHtWiseMaterials")
    Call<JsonObject> getHTWiseMaterials();

    @POST("dtrPhaseWiseCapacities")
    Call<JsonObject> getCapacities(@Body JsonObject request);


    @POST("getExistingMaterials")
    Call<JsonObject> getExistingMaterials(@Body JsonObject request);

    @POST("validateProposedInvolvments")
    Call<JsonObject> validateProposedInvolvments(@Body JsonObject request);


    @POST("htpoleDetails")
    Call<JsonObject> htpoleDet(@Body JsonObject request);

    @POST("cktwiseExistingPoleCounts")
    Call<JsonObject> cktwiseExistingPoleCounts(@Body JsonObject request);

    @POST("updateLTWorkInvolvement")
    Call<JsonObject> updateLTWorkInvolvement(@Body JsonObject request);

    @POST("updateStatusToResurveyRejected")
    Call<JsonObject> updateStatusToResurveyRejected(@Body JsonArray request);

    @POST("gislogout")
    Call<LoginResponseVo> logout(@Body JsonObject request);

    @GET("getDmsToken")
    Call<JsonObject> getImageAccessToken();

    @GET("secDetails")
    Call<JsonObject> getSectionDetails();

    @POST("dtrmaster/rdss")
    Call<JsonObject> getDtrMasterBySections(@Body JsonObject object);

    @POST("dtrmaster/rdss")
    Call<JsonObject> dtrmasterRdss(@Body JsonObject object);

    @POST("saveSectionsForRdss")
    Call<JsonObject> saveSectionsForRdss(@Body JsonObject object);

}
