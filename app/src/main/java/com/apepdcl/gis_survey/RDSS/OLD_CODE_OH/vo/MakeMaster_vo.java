package com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo;

import com.google.gson.annotations.SerializedName;

public class MakeMaster_vo {

    @SerializedName("ID")
    private String ID;

    @SerializedName("RECORD_STATUS")
    private String DTR_RECORD_STATUS;

    @SerializedName("DTR_MAKE")
    private String DTR_MAKE;



    public String getDTR_MAKE() {
        return DTR_MAKE;
    }

    public void setDTR_MAKE(String DTR_MAKE) {
        this.DTR_MAKE = DTR_MAKE;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDTR_RECORD_STATUS() {
        return DTR_RECORD_STATUS;
    }

    public void setDTR_RECORD_STATUS(String DTR_RECORD_STATUS) {
        this.DTR_RECORD_STATUS = DTR_RECORD_STATUS;
    }
}
