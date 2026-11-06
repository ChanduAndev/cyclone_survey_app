package com.apepdcl.gis_survey.estimates.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Soubhagya Swain on 10,June,2024
 */
public class PurposeModel implements Serializable {
    private String GIS_ID,EST_NATURE,EST_NO,REG_COUNT,SEC_ID,STATUS,PURPOSE,LINE_33KV,LINE_11KV,LINE_LT,DTR,EST_TYPE,MAX_LENGTH,SS,METERS,WORK_ORDER,
            TOWER_LENGTH,WO_OLD,MODIFY_WI,RE_SURVEY,REVISED,INTG_REMARKS,INTG_STATUS,FEEDERS,RDSS_TYPE;
    private List<FdrParticularsModel> FDR_PARTICULARS;
    private boolean isDialogShown;
    private int COUNT_33KV,COUNT_11KV,COUNT_LT,NUM_33KV,NUM_11KV,NUM_LT;

    public String getFEEDERS() {
        return FEEDERS;
    }

    public void setFEEDERS(String FEEDERS) {
        this.FEEDERS = FEEDERS;
    }

    public String getRDSS_TYPE() {
        return RDSS_TYPE;
    }

    public void setRDSS_TYPE(String RDSS_TYPE) {
        this.RDSS_TYPE = RDSS_TYPE;
    }

    public String getRE_SURVEY() {
        return RE_SURVEY;
    }

    public void setRE_SURVEY(String RE_SURVEY) {
        this.RE_SURVEY = RE_SURVEY;
    }

    public String getREVISED() {
        return REVISED;
    }

    public void setREVISED(String REVISED) {
        this.REVISED = REVISED;
    }

    public int getCOUNT_33KV() {
        return COUNT_33KV;
    }

    public void setCOUNT_33KV(int COUNT_33KV) {
        this.COUNT_33KV = COUNT_33KV;
    }

    public int getCOUNT_11KV() {
        return COUNT_11KV;
    }

    public void setCOUNT_11KV(int COUNT_11KV) {
        this.COUNT_11KV = COUNT_11KV;
    }

    public int getCOUNT_LT() {
        return COUNT_LT;
    }

    public void setCOUNT_LT(int COUNT_LT) {
        this.COUNT_LT = COUNT_LT;
    }

    public String getGIS_ID() {
        return GIS_ID;
    }

    public void setGIS_ID(String GIS_ID) {
        this.GIS_ID = GIS_ID;
    }

    public String getEST_NATURE() {
        return EST_NATURE;
    }

    public void setEST_NATURE(String EST_NATURE) {
        this.EST_NATURE = EST_NATURE;
    }

    public String getEST_NO() {
        return EST_NO;
    }

    public void setEST_NO(String EST_NO) {
        this.EST_NO = EST_NO;
    }

    public String getREG_COUNT() {
        return REG_COUNT;
    }

    public void setREG_COUNT(String REG_COUNT) {
        this.REG_COUNT = REG_COUNT;
    }

    public String getSEC_ID() {
        return SEC_ID;
    }

    public void setSEC_ID(String SEC_ID) {
        this.SEC_ID = SEC_ID;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public String getINTG_REMARKS() {
        return INTG_REMARKS;
    }

    public String getINTG_STATUS() {
        return INTG_STATUS;
    }

    public void setINTG_STATUS(String INTG_STATUS) {
        this.INTG_STATUS = INTG_STATUS;
    }

    public void setINTG_REMARKS(String INTG_REMARKS) {
        this.INTG_REMARKS = INTG_REMARKS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getPURPOSE() {
        return PURPOSE;
    }

    public void setPURPOSE(String PURPOSE) {
        this.PURPOSE = PURPOSE;
    }

    public String getLINE_33KV() {
        return LINE_33KV;
    }

    public void setLINE_33KV(String LINE_33KV) {
        this.LINE_33KV = LINE_33KV;
    }

    public String getLINE_11KV() {
        return LINE_11KV;
    }

    public void setLINE_11KV(String LINE_11KV) {
        this.LINE_11KV = LINE_11KV;
    }

    public String getLINE_LT() {
        return LINE_LT;
    }

    public void setLINE_LT(String LINE_LT) {
        this.LINE_LT = LINE_LT;
    }

    public String getDTR() {
        return DTR;
    }

    public void setDTR(String DTR) {
        this.DTR = DTR;
    }

    public String getEST_TYPE() {
        return EST_TYPE;
    }

    public void setEST_TYPE(String EST_TYPE) {
        this.EST_TYPE = EST_TYPE;
    }

    public String getMAX_LENGTH() {
        return MAX_LENGTH;
    }

    public void setMAX_LENGTH(String MAX_LENGTH) {
        this.MAX_LENGTH = MAX_LENGTH;
    }

    public boolean isDialogShown() {
        return isDialogShown;
    }

    public void setDialogShown(boolean dialogShown) {
        isDialogShown = dialogShown;
    }

    public List<FdrParticularsModel> getFDR_PARTICULARS() {
        return FDR_PARTICULARS;
    }

    public void setFDR_PARTICULARS(List<FdrParticularsModel> FDR_PARTICULARS) {
        this.FDR_PARTICULARS = FDR_PARTICULARS;
    }

    public String getSS() {
        return SS;
    }

    public void setSS(String SS) {
        this.SS = SS;
    }

    public String getTOWER_LENGTH() {
        return TOWER_LENGTH;
    }

    public void setTOWER_LENGTH(String TOWER_LENGTH) {
        this.TOWER_LENGTH = TOWER_LENGTH;
    }

    public String getMETERS() {
        return METERS;
    }

    public void setMETERS(String METERS) {
        this.METERS = METERS;
    }

    public String getWORK_ORDER() {
        return WORK_ORDER;
    }

    public void setWORK_ORDER(String WORK_ORDER) {
        this.WORK_ORDER = WORK_ORDER;
    }

    public int getNUM_33KV() {
        return NUM_33KV;
    }

    public void setNUM_33KV(int NUM_33KV) {
        this.NUM_33KV = NUM_33KV;
    }

    public int getNUM_11KV() {
        return NUM_11KV;
    }

    public void setNUM_11KV(int NUM_11KV) {
        this.NUM_11KV = NUM_11KV;
    }

    public int getNUM_LT() {
        return NUM_LT;
    }

    public void setNUM_LT(int NUM_LT) {
        this.NUM_LT = NUM_LT;
    }

    public String getWO_OLD() {
        return WO_OLD;
    }

    public void setWO_OLD(String WO_OLD) {
        this.WO_OLD = WO_OLD;
    }

    public String getMODIFY_WI() {
        return MODIFY_WI;
    }

    public void setMODIFY_WI(String MODIFY_WI) {
        this.MODIFY_WI = MODIFY_WI;
    }
}
