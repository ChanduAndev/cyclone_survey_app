package com.apepdcl.gis_survey.estimates.models.location;

import com.google.gson.annotations.SerializedName;

   
public class DTR {

   @SerializedName("SECTION_NAME")
   String SECTIONNAME;

   @SerializedName("DTR_STRUCTURE_CODE")
   String DTRSTRUCTURECODE;

   @SerializedName("CAPACITY")
   int CAPACITY;

   @SerializedName("PHASE")
   String PHASE;

   @SerializedName("DTR_LOCATION")
   String DTRLOCATION;

   @SerializedName("DTR_MAKE")
   String DTRMAKE;

   @SerializedName("DTR_SLNO")
   String DTRSLNO;

   @SerializedName("SECTION_ID")
   String SECTIONID;

   @SerializedName("FEEDER_ID")
   String FEEDERID;

   @SerializedName("FEEDER_NAME")
   String FEEDERNAME;


    public void setSECTIONNAME(String SECTIONNAME) {
        this.SECTIONNAME = SECTIONNAME;
    }
    public String getSECTIONNAME() {
        return SECTIONNAME;
    }
    
    public void setDTRSTRUCTURECODE(String DTRSTRUCTURECODE) {
        this.DTRSTRUCTURECODE = DTRSTRUCTURECODE;
    }
    public String getDTRSTRUCTURECODE() {
        return DTRSTRUCTURECODE;
    }
    
    public void setCAPACITY(int CAPACITY) {
        this.CAPACITY = CAPACITY;
    }
    public int getCAPACITY() {
        return CAPACITY;
    }
    
    public void setPHASE(String PHASE) {
        this.PHASE = PHASE;
    }
    public String getPHASE() {
        return PHASE;
    }
    
    public void setDTRLOCATION(String DTRLOCATION) {
        this.DTRLOCATION = DTRLOCATION;
    }
    public String getDTRLOCATION() {
        return DTRLOCATION;
    }
    
    public void setDTRMAKE(String DTRMAKE) {
        this.DTRMAKE = DTRMAKE;
    }
    public String getDTRMAKE() {
        return DTRMAKE;
    }
    
    public void setDTRSLNO(String DTRSLNO) {
        this.DTRSLNO = DTRSLNO;
    }
    public String getDTRSLNO() {
        return DTRSLNO;
    }
    
    public void setSECTIONID(String SECTIONID) {
        this.SECTIONID = SECTIONID;
    }
    public String getSECTIONID() {
        return SECTIONID;
    }
    
    public void setFEEDERID(String FEEDERID) {
        this.FEEDERID = FEEDERID;
    }
    public String getFEEDERID() {
        return FEEDERID;
    }
    
    public void setFEEDERNAME(String FEEDERNAME) {
        this.FEEDERNAME = FEEDERNAME;
    }
    public String getFEEDERNAME() {
        return FEEDERNAME;
    }
    
}