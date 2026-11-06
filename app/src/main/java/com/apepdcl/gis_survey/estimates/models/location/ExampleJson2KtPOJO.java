package com.apepdcl.gis_survey.estimates.models.location;

import com.google.gson.annotations.SerializedName;

   
public class ExampleJson2KtPOJO {

   @SerializedName("STATUS")
   boolean STATUS;

   @SerializedName("DATA")
   DATA DATA;

   @SerializedName("MESSAGE")
   String MESSAGE;


    public void setSTATUS(boolean STATUS) {
        this.STATUS = STATUS;
    }
    public boolean getSTATUS() {
        return STATUS;
    }
    
    public void setDATA(DATA DATA) {
        this.DATA = DATA;
    }
    public DATA getDATA() {
        return DATA;
    }
    
    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }
    public String getMESSAGE() {
        return MESSAGE;
    }
    
}