package com.apepdcl.gis_survey.pmi_old.module_11kv.vo;

import com.google.gson.annotations.SerializedName;

public class PMIMULTIVOLTAGEMaster_vo {
    @SerializedName("ID")
    private String ID;

    @SerializedName("VOLTAGE")
    private String VOLTAGE;

    @SerializedName("CKT_NO")
    private String CKT_NO;

    @SerializedName("CKT_VALUE")
    private String CKT_VALUE;

    @SerializedName("RECORD_STATUS")
    private String RECORD_STATUS;

    public String getVOLTAGE() {
        return VOLTAGE;
    }

    public void setVOLTAGE(String VOLTAGE) {
        this.VOLTAGE = VOLTAGE;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCKT_NO() {
        return CKT_NO;
    }

    public void setCKT_NO(String CKT_NO) {
        this.CKT_NO = CKT_NO;
    }

    public String getCKT_VALUE() {
        return CKT_VALUE;
    }

    public void setCKT_VALUE(String CKT_VALUE) {
        this.CKT_VALUE = CKT_VALUE;
    }

    public String getRECORD_STATUS() {
        return RECORD_STATUS;
    }

    public void setRECORD_STATUS(String RECORD_STATUS) {
        this.RECORD_STATUS = RECORD_STATUS;
    }
}
