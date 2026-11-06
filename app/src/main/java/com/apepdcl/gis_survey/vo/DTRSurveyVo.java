package com.apepdcl.gis_survey.vo;

import com.google.gson.annotations.SerializedName;

public class DTRSurveyVo {
    private String DTR_STRUCTURE;
    private String DTR_ABSWITCH;
    private String DTR_HGFUSE;
    private String EARTH_PITS;
    private String CIPIPES;
    private String GIPIPES;
    private String DTR_METER;
    private String DTR_METER_NUM;
    private String DTR_METER_MAKE;
    private String DTR_METER_CAPACITY;
    private String DTR_METER_TYPE;
    private String MODEM;
    private String MODEM_NUM;
    private String MODEM_MAKE;
    private String BUSBAR_TYPE;
    private String CONDUCTOR_SIZE;
    private String HGFUSE_DTR;
    private String HGFUSE_DTR_CONDUCTOR_SIZE;
    private String DTR_POLE;
    private String PIN;
    private String CUT;
    private String REMARKS;
    private String LT_FUSESET;
    private String DTR_MAKE;
    private String DTR_SERIALNO;
    private String DTR_LOAD_TYPE;
    private String DTR_PHASE;
    private String DTR_CAPACITY;
    private String DTR_STRUCTURE_TYPE;
    private String LAT;
    private String LAN;
    private String DTR_POLE_SUPPORT;
    private String NOOF_STURT;
    private String NOOF_STAY;
    private String DTR_ABSWTICH_TYPE;
    private String NOOF_PININSULATORS;
    private String NOOF_DISCINSULATORS;
    private String DTR_MTR2_NUM;
    private String DTR_MTR2_MAKE;
    private String DTR_MTR2_CAPACITY;
    private String DTR_MTR2_TYPE;
    private String DTR_MTR3_NUM;
    private String DTR_MTR3_MAKE;
    private String DTR_MTR3_CAPACITY;
    private String DTR_MTR3_TYPE;
    private String LT_BUSBAR;
    private String LT_BUSBAR_CABLESIZE;
    private String LT_BUSBAR_CONDUTORSIZE;
    private String NOOF_DTRMETERS;
    private String MODEM2;
    private String MODEM2_NUM;
    private String MODEM2_MAKE;
    private String MODEM3;
    private String MODEM3_NUM;
    private String MODEM3_MAKE;
    private String DTR_LOCATION;
    private String NOOF_LTCIRCUITS;

    //SURVEY REMARKS
    private String SURVEY_REMARKS;

    //ADDED ON 26-07-2024
    private String BODY_EARTH;
    private String NEUTRAL_EARTH;

    public String getBODY_EARTH() {
        return BODY_EARTH;
    }

    public void setBODY_EARTH(String BODY_EARTH) {
        this.BODY_EARTH = BODY_EARTH;
    }

    public String getNEUTRAL_EARTH() {
        return NEUTRAL_EARTH;
    }

    public void setNEUTRAL_EARTH(String NEUTRAL_EARTH) {
        this.NEUTRAL_EARTH = NEUTRAL_EARTH;
    }

    public String getSURVEY_REMARKS() {
        return SURVEY_REMARKS;
    }

    public void setSURVEY_REMARKS(String SURVEY_REMARKS) {
        this.SURVEY_REMARKS = SURVEY_REMARKS;
    }

    //NEW
    private String STRUCTURE_DEFECTS, DTR_FENCING, DTR_AB_DEFECTS, HG_FUSE_DEFECTS, LT_FUSE_DEFECTS, NUM_EARTH, NUM_LT_BUSHINGS, NUM_HT_BUSHINGS, NUM_PG_LUG, XLPE_DEFECTS, NUM_ARRESTER, OIL_LEAK, OTHER_DEFECTS;

    private String IMAGE_1;
    private String IMAGE_2;
    private String IMAGE_3;

    public String getIMAGE_1() {
        return IMAGE_1;
    }

    public void setIMAGE_1(String IMAGE_1) {
        this.IMAGE_1 = IMAGE_1;
    }

    public String getIMAGE_2() {
        return IMAGE_2;
    }

    public void setIMAGE_2(String IMAGE_2) {
        this.IMAGE_2 = IMAGE_2;
    }

    public String getIMAGE_3() {
        return IMAGE_3;
    }

    public void setIMAGE_3(String IMAGE_3) {
        this.IMAGE_3 = IMAGE_3;
    }

    public String getSTRUCTURE_DEFECTS() {
        return STRUCTURE_DEFECTS;
    }

    public void setSTRUCTURE_DEFECTS(String STRUCTURE_DEFECTS) {
        this.STRUCTURE_DEFECTS = STRUCTURE_DEFECTS;
    }

    public String getDTR_FENCING() {
        return DTR_FENCING;
    }

    public void setDTR_FENCING(String DTR_FENCING) {
        this.DTR_FENCING = DTR_FENCING;
    }

    public String getDTR_AB_DEFECTS() {
        return DTR_AB_DEFECTS;
    }

    public void setDTR_AB_DEFECTS(String DTR_AB_DEFECTS) {
        this.DTR_AB_DEFECTS = DTR_AB_DEFECTS;
    }

    public String getHG_FUSE_DEFECTS() {
        return HG_FUSE_DEFECTS;
    }

    public void setHG_FUSE_DEFECTS(String HG_FUSE_DEFECTS) {
        this.HG_FUSE_DEFECTS = HG_FUSE_DEFECTS;
    }

    public String getLT_FUSE_DEFECTS() {
        return LT_FUSE_DEFECTS;
    }

    public void setLT_FUSE_DEFECTS(String LT_FUSE_DEFECTS) {
        this.LT_FUSE_DEFECTS = LT_FUSE_DEFECTS;
    }

    public String getNUM_EARTH() {
        return NUM_EARTH;
    }

    public void setNUM_EARTH(String NUM_EARTH) {
        this.NUM_EARTH = NUM_EARTH;
    }

    public String getNUM_LT_BUSHINGS() {
        return NUM_LT_BUSHINGS;
    }

    public void setNUM_LT_BUSHINGS(String NUM_LT_BUSHINGS) {
        this.NUM_LT_BUSHINGS = NUM_LT_BUSHINGS;
    }

    public String getNUM_HT_BUSHINGS() {
        return NUM_HT_BUSHINGS;
    }

    public void setNUM_HT_BUSHINGS(String NUM_HT_BUSHINGS) {
        this.NUM_HT_BUSHINGS = NUM_HT_BUSHINGS;
    }

    public String getNUM_PG_LUG() {
        return NUM_PG_LUG;
    }

    public void setNUM_PG_LUG(String NUM_PG_LUG) {
        this.NUM_PG_LUG = NUM_PG_LUG;
    }

    public String getXLPE_DEFECTS() {
        return XLPE_DEFECTS;
    }

    public void setXLPE_DEFECTS(String XLPE_DEFECTS) {
        this.XLPE_DEFECTS = XLPE_DEFECTS;
    }

    public String getNUM_ARRESTER() {
        return NUM_ARRESTER;
    }

    public void setNUM_ARRESTER(String NUM_ARRESTER) {
        this.NUM_ARRESTER = NUM_ARRESTER;
    }

    public String getOIL_LEAK() {
        return OIL_LEAK;
    }

    public void setOIL_LEAK(String OIL_LEAK) {
        this.OIL_LEAK = OIL_LEAK;
    }

    public String getOTHER_DEFECTS() {
        return OTHER_DEFECTS;
    }

    public void setOTHER_DEFECTS(String OTHER_DEFECTS) {
        this.OTHER_DEFECTS = OTHER_DEFECTS;
    }

    public String getMODEM2() {
        return MODEM2;
    }

    public void setMODEM2(String MODEM2) {
        this.MODEM2 = MODEM2;
    }

    public String getMODEM2_NUM() {
        return MODEM2_NUM;
    }

    public void setMODEM2_NUM(String MODEM2_NUM) {
        this.MODEM2_NUM = MODEM2_NUM;
    }

    public String getMODEM2_MAKE() {
        return MODEM2_MAKE;
    }

    public void setMODEM2_MAKE(String MODEM2_MAKE) {
        this.MODEM2_MAKE = MODEM2_MAKE;
    }

    public String getMODEM3() {
        return MODEM3;
    }

    public void setMODEM3(String MODEM3) {
        this.MODEM3 = MODEM3;
    }

    public String getMODEM3_NUM() {
        return MODEM3_NUM;
    }

    public void setMODEM3_NUM(String MODEM3_NUM) {
        this.MODEM3_NUM = MODEM3_NUM;
    }

    public String getMODEM3_MAKE() {
        return MODEM3_MAKE;
    }

    public void setMODEM3_MAKE(String MODEM3_MAKE) {
        this.MODEM3_MAKE = MODEM3_MAKE;
    }

    public String getDTR_LOCATION() {
        return DTR_LOCATION;
    }

    public void setDTR_LOCATION(String DTR_LOCATION) {
        this.DTR_LOCATION = DTR_LOCATION;
    }

    public String getNOOF_LTCIRCUITS() {
        return NOOF_LTCIRCUITS;
    }

    public void setNOOF_LTCIRCUITS(String NOOF_LTCIRCUITS) {
        this.NOOF_LTCIRCUITS = NOOF_LTCIRCUITS;
    }

    public String getNOOF_DTRMETERS() {
        return NOOF_DTRMETERS;
    }

    public void setNOOF_DTRMETERS(String NOOF_DTRMETERS) {
        this.NOOF_DTRMETERS = NOOF_DTRMETERS;
    }

    public String getLT_BUSBAR_CABLESIZE() {
        return LT_BUSBAR_CABLESIZE;
    }

    public void setLT_BUSBAR_CABLESIZE(String LT_BUSBAR_CABLESIZE) {
        this.LT_BUSBAR_CABLESIZE = LT_BUSBAR_CABLESIZE;
    }

    public String getLT_BUSBAR_CONDUTORSIZE() {
        return LT_BUSBAR_CONDUTORSIZE;
    }

    public void setLT_BUSBAR_CONDUTORSIZE(String LT_BUSBAR_CONDUTORSIZE) {
        this.LT_BUSBAR_CONDUTORSIZE = LT_BUSBAR_CONDUTORSIZE;
    }

    public String getDTR_POLE_SUPPORT() {
        return DTR_POLE_SUPPORT;
    }

    public void setDTR_POLE_SUPPORT(String DTR_POLE_SUPPORT) {
        this.DTR_POLE_SUPPORT = DTR_POLE_SUPPORT;
    }

    public String getNOOF_STURT() {
        return NOOF_STURT;
    }

    public void setNOOF_STURT(String NOOF_STURT) {
        this.NOOF_STURT = NOOF_STURT;
    }

    public String getNOOF_STAY() {
        return NOOF_STAY;
    }

    public void setNOOF_STAY(String NOOF_STAY) {
        this.NOOF_STAY = NOOF_STAY;
    }

    public String getDTR_ABSWTICH_TYPE() {
        return DTR_ABSWTICH_TYPE;
    }

    public void setDTR_ABSWTICH_TYPE(String DTR_ABSWTICH_TYPE) {
        this.DTR_ABSWTICH_TYPE = DTR_ABSWTICH_TYPE;
    }

    public String getNOOF_PININSULATORS() {
        return NOOF_PININSULATORS;
    }

    public void setNOOF_PININSULATORS(String NOOF_PININSULATORS) {
        this.NOOF_PININSULATORS = NOOF_PININSULATORS;
    }

    public String getNOOF_DISCINSULATORS() {
        return NOOF_DISCINSULATORS;
    }

    public void setNOOF_DISCINSULATORS(String NOOF_DISCINSULATORS) {
        this.NOOF_DISCINSULATORS = NOOF_DISCINSULATORS;
    }

    public String getDTR_MTR2_NUM() {
        return DTR_MTR2_NUM;
    }

    public void setDTR_MTR2_NUM(String DTR_MTR2_NUM) {
        this.DTR_MTR2_NUM = DTR_MTR2_NUM;
    }

    public String getDTR_MTR2_MAKE() {
        return DTR_MTR2_MAKE;
    }

    public void setDTR_MTR2_MAKE(String DTR_MTR2_MAKE) {
        this.DTR_MTR2_MAKE = DTR_MTR2_MAKE;
    }

    public String getDTR_MTR2_CAPACITY() {
        return DTR_MTR2_CAPACITY;
    }

    public void setDTR_MTR2_CAPACITY(String DTR_MTR2_CAPACITY) {
        this.DTR_MTR2_CAPACITY = DTR_MTR2_CAPACITY;
    }

    public String getDTR_MTR2_TYPE() {
        return DTR_MTR2_TYPE;
    }

    public void setDTR_MTR2_TYPE(String DTR_MTR2_TYPE) {
        this.DTR_MTR2_TYPE = DTR_MTR2_TYPE;
    }

    public String getDTR_MTR3_NUM() {
        return DTR_MTR3_NUM;
    }

    public void setDTR_MTR3_NUM(String DTR_MTR3_NUM) {
        this.DTR_MTR3_NUM = DTR_MTR3_NUM;
    }

    public String getDTR_MTR3_MAKE() {
        return DTR_MTR3_MAKE;
    }

    public void setDTR_MTR3_MAKE(String DTR_MTR3_MAKE) {
        this.DTR_MTR3_MAKE = DTR_MTR3_MAKE;
    }

    public String getDTR_MTR3_CAPACITY() {
        return DTR_MTR3_CAPACITY;
    }

    public void setDTR_MTR3_CAPACITY(String DTR_MTR3_CAPACITY) {
        this.DTR_MTR3_CAPACITY = DTR_MTR3_CAPACITY;
    }

    public String getDTR_MTR3_TYPE() {
        return DTR_MTR3_TYPE;
    }

    public void setDTR_MTR3_TYPE(String DTR_MTR3_TYPE) {
        this.DTR_MTR3_TYPE = DTR_MTR3_TYPE;
    }

    public String getLT_BUSBAR() {
        return LT_BUSBAR;
    }

    public void setLT_BUSBAR(String LT_BUSBAR) {
        this.LT_BUSBAR = LT_BUSBAR;
    }

    public String getLAT() {
        return LAT;
    }

    public void setLAT(String LAT) {
        this.LAT = LAT;
    }

    public String getLAN() {
        return LAN;
    }

    public void setLAN(String LAN) {
        this.LAN = LAN;
    }

    public String getDTR_MAKE() {
        return DTR_MAKE;
    }

    public void setDTR_MAKE(String DTR_MAKE) {
        this.DTR_MAKE = DTR_MAKE;
    }

    public String getDTR_SERIALNO() {
        return DTR_SERIALNO;
    }

    public void setDTR_SERIALNO(String DTR_SERIALNO) {
        this.DTR_SERIALNO = DTR_SERIALNO;
    }

    public String getDTR_LOAD_TYPE() {
        return DTR_LOAD_TYPE;
    }

    public void setDTR_LOAD_TYPE(String DTR_LOAD_TYPE) {
        this.DTR_LOAD_TYPE = DTR_LOAD_TYPE;
    }

    public String getDTR_PHASE() {
        return DTR_PHASE;
    }

    public void setDTR_PHASE(String DTR_PHASE) {
        this.DTR_PHASE = DTR_PHASE;
    }

    public String getDTR_CAPACITY() {
        return DTR_CAPACITY;
    }

    public void setDTR_CAPACITY(String DTR_CAPACITY) {
        this.DTR_CAPACITY = DTR_CAPACITY;
    }

    public String getDTR_STRUCTURE_TYPE() {
        return DTR_STRUCTURE_TYPE;
    }

    public void setDTR_STRUCTURE_TYPE(String DTR_STRUCTURE_TYPE) {
        this.DTR_STRUCTURE_TYPE = DTR_STRUCTURE_TYPE;
    }

    public String getLT_FUSESET() {
        return LT_FUSESET;
    }

    public void setLT_FUSESET(String LT_FUSESET) {
        this.LT_FUSESET = LT_FUSESET;
    }

    public String getDTR_STRUCTURE() {
        return DTR_STRUCTURE;
    }

    public void setDTR_STRUCTURE(String DTR_STRUCTURE) {
        this.DTR_STRUCTURE = DTR_STRUCTURE;
    }

    public String getDTR_ABSWITCH() {
        return DTR_ABSWITCH;
    }

    public void setDTR_ABSWITCH(String DTR_ABSWITCH) {
        this.DTR_ABSWITCH = DTR_ABSWITCH;
    }

    public String getDTR_HGFUSE() {
        return DTR_HGFUSE;
    }

    public void setDTR_HGFUSE(String DTR_HGFUSE) {
        this.DTR_HGFUSE = DTR_HGFUSE;
    }

    public String getEARTH_PITS() {
        return EARTH_PITS;
    }

    public void setEARTH_PITS(String EARTH_PITS) {
        this.EARTH_PITS = EARTH_PITS;
    }

    public String getCIPIPES() {
        return CIPIPES;
    }

    public void setCIPIPES(String CIPIPES) {
        this.CIPIPES = CIPIPES;
    }

    public String getGIPIPES() {
        return GIPIPES;
    }

    public void setGIPIPES(String GIPIPES) {
        this.GIPIPES = GIPIPES;
    }

    public String getDTR_METER() {
        return DTR_METER;
    }

    public void setDTR_METER(String DTR_METER) {
        this.DTR_METER = DTR_METER;
    }

    public String getDTR_METER_NUM() {
        return DTR_METER_NUM;
    }

    public void setDTR_METER_NUM(String DTR_METER_NUM) {
        this.DTR_METER_NUM = DTR_METER_NUM;
    }

    public String getDTR_METER_MAKE() {
        return DTR_METER_MAKE;
    }

    public void setDTR_METER_MAKE(String DTR_METER_MAKE) {
        this.DTR_METER_MAKE = DTR_METER_MAKE;
    }

    public String getDTR_METER_CAPACITY() {
        return DTR_METER_CAPACITY;
    }

    public void setDTR_METER_CAPACITY(String DTR_METER_CAPACITY) {
        this.DTR_METER_CAPACITY = DTR_METER_CAPACITY;
    }

    public String getDTR_METER_TYPE() {
        return DTR_METER_TYPE;
    }

    public void setDTR_METER_TYPE(String DTR_METER_TYPE) {
        this.DTR_METER_TYPE = DTR_METER_TYPE;
    }

    public String getMODEM() {
        return MODEM;
    }

    public void setMODEM(String MODEM) {
        this.MODEM = MODEM;
    }

    public String getMODEM_NUM() {
        return MODEM_NUM;
    }

    public void setMODEM_NUM(String MODEM_NUM) {
        this.MODEM_NUM = MODEM_NUM;
    }

    public String getMODEM_MAKE() {
        return MODEM_MAKE;
    }

    public void setMODEM_MAKE(String MODEM_MAKE) {
        this.MODEM_MAKE = MODEM_MAKE;
    }

    public String getBUSBAR_TYPE() {
        return BUSBAR_TYPE;
    }

    public void setBUSBAR_TYPE(String BUSBAR_TYPE) {
        this.BUSBAR_TYPE = BUSBAR_TYPE;
    }

    public String getCONDUCTOR_SIZE() {
        return CONDUCTOR_SIZE;
    }

    public void setCONDUCTOR_SIZE(String CONDUCTOR_SIZE) {
        this.CONDUCTOR_SIZE = CONDUCTOR_SIZE;
    }

    public String getHGFUSE_DTR() {
        return HGFUSE_DTR;
    }

    public void setHGFUSE_DTR(String HGFUSE_DTR) {
        this.HGFUSE_DTR = HGFUSE_DTR;
    }

    public String getHGFUSE_DTR_CONDUCTOR_SIZE() {
        return HGFUSE_DTR_CONDUCTOR_SIZE;
    }

    public void setHGFUSE_DTR_CONDUCTOR_SIZE(String HGFUSE_DTR_CONDUCTOR_SIZE) {
        this.HGFUSE_DTR_CONDUCTOR_SIZE = HGFUSE_DTR_CONDUCTOR_SIZE;
    }

    public String getDTR_POLE() {
        return DTR_POLE;
    }

    public void setDTR_POLE(String DTR_POLE) {
        this.DTR_POLE = DTR_POLE;
    }

    public String getPIN() {
        return PIN;
    }

    public void setPIN(String PIN) {
        this.PIN = PIN;
    }

    public String getCUT() {
        return CUT;
    }

    public void setCUT(String CUT) {
        this.CUT = CUT;
    }

    public String getREMARKS() {
        return REMARKS;
    }

    public void setREMARKS(String REMARKS) {
        this.REMARKS = REMARKS;
    }
}
