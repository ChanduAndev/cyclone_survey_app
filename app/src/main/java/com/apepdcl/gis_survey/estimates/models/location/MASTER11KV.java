package com.apepdcl.gis_survey.estimates.models.location;
import java.util.Date;

import com.google.gson.annotations.SerializedName;

   
public class MASTER11KV {

   @SerializedName("SUB_STATION_ID")
   String SUBSTATIONID;

   @SerializedName("SEC_ID")
   String SECID;

   @SerializedName("SEC_NAME")
   String SECNAME;

   @SerializedName("SUB_STATION_NAME")
   String SUBSTATIONNAME;

   @SerializedName("FEEDER_ID")
   String FEEDERID;

   @SerializedName("FEEDER_NAME")
   Date FEEDERNAME;


    public void setSUBSTATIONID(String SUBSTATIONID) {
        this.SUBSTATIONID = SUBSTATIONID;
    }
    public String getSUBSTATIONID() {
        return SUBSTATIONID;
    }
    
    public void setSECID(String SECID) {
        this.SECID = SECID;
    }
    public String getSECID() {
        return SECID;
    }
    
    public void setSECNAME(String SECNAME) {
        this.SECNAME = SECNAME;
    }
    public String getSECNAME() {
        return SECNAME;
    }
    
    public void setSUBSTATIONNAME(String SUBSTATIONNAME) {
        this.SUBSTATIONNAME = SUBSTATIONNAME;
    }
    public String getSUBSTATIONNAME() {
        return SUBSTATIONNAME;
    }
    
    public void setFEEDERID(String FEEDERID) {
        this.FEEDERID = FEEDERID;
    }
    public String getFEEDERID() {
        return FEEDERID;
    }
    
    public void setFEEDERNAME(Date FEEDERNAME) {
        this.FEEDERNAME = FEEDERNAME;
    }
    public Date getFEEDERNAME() {
        return FEEDERNAME;
    }
    
}