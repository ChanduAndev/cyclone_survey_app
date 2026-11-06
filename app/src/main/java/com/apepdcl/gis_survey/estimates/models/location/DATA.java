package com.apepdcl.gis_survey.estimates.models.location;
import java.util.List;

import com.google.gson.annotations.SerializedName;

   
public class DATA {

   @SerializedName("NEAR_BY_11KV")
   List<NEARBY11KV> NEARBY11KV;

   @SerializedName("MASTER_11KV")
   List<MASTER11KV> MASTER11KV;

   @SerializedName("NEAR_BY_33KV")
   List<NEARBY33KV> NEARBY33KV;

   @SerializedName("MASTER_33KV")
   List<MASTER33KV> MASTER33KV;

   @SerializedName("DTR")
   List<DTR> DTR;


    public void setNEARBY11KV(List<NEARBY11KV> NEARBY11KV) {
        this.NEARBY11KV = NEARBY11KV;
    }
    public List<NEARBY11KV> getNEARBY11KV() {
        return NEARBY11KV;
    }
    
    public void setMASTER11KV(List<MASTER11KV> MASTER11KV) {
        this.MASTER11KV = MASTER11KV;
    }
    public List<MASTER11KV> getMASTER11KV() {
        return MASTER11KV;
    }
    
    public void setNEARBY33KV(List<NEARBY33KV> NEARBY33KV) {
        this.NEARBY33KV = NEARBY33KV;
    }
    public List<NEARBY33KV> getNEARBY33KV() {
        return NEARBY33KV;
    }
    
    public void setMASTER33KV(List<MASTER33KV> MASTER33KV) {
        this.MASTER33KV = MASTER33KV;
    }
    public List<MASTER33KV> getMASTER33KV() {
        return MASTER33KV;
    }
    
    public void setDTR(List<DTR> DTR) {
        this.DTR = DTR;
    }
    public List<DTR> getDTR() {
        return DTR;
    }
    
}