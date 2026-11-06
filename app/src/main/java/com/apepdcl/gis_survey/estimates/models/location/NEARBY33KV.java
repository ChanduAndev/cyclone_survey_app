package com.apepdcl.gis_survey.estimates.models.location;

import com.google.gson.annotations.SerializedName;

   
public class NEARBY33KV {

   @SerializedName("FEEDER_CODE_33KV")
   String FEEDERCODE33KV;

   @SerializedName("EHT_SS_NAME")
   String EHTSSNAME;

   @SerializedName("FEEDER_NAME_33KV")
   String FEEDERNAME33KV;

   @SerializedName("EHT_SS_CODE")
   String EHTSSCODE;


    public void setFEEDERCODE33KV(String FEEDERCODE33KV) {
        this.FEEDERCODE33KV = FEEDERCODE33KV;
    }
    public String getFEEDERCODE33KV() {
        return FEEDERCODE33KV;
    }
    
    public void setEHTSSNAME(String EHTSSNAME) {
        this.EHTSSNAME = EHTSSNAME;
    }
    public String getEHTSSNAME() {
        return EHTSSNAME;
    }
    
    public void setFEEDERNAME33KV(String FEEDERNAME33KV) {
        this.FEEDERNAME33KV = FEEDERNAME33KV;
    }
    public String getFEEDERNAME33KV() {
        return FEEDERNAME33KV;
    }
    
    public void setEHTSSCODE(String EHTSSCODE) {
        this.EHTSSCODE = EHTSSCODE;
    }
    public String getEHTSSCODE() {
        return EHTSSCODE;
    }
    
}