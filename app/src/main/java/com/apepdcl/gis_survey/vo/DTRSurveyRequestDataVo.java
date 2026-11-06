package com.apepdcl.gis_survey.vo;

import com.google.gson.annotations.SerializedName;

public class DTRSurveyRequestDataVo {
    @SerializedName("SEC_CODE")
    private String SEC_CODE;
    @SerializedName("CREATED_BY")
    private String CREATED_BY;
    @SerializedName("IME_NO")
    private String IME_NO;
    @SerializedName("FEEDER_ID")
    private String FEEDER_ID;
    @SerializedName("DTR_MAKE")
    private String DTR_MAKE;
    @SerializedName("DTR_SL_NO")
    private String DTR_SL_NO;
    @SerializedName("DTR_STRUC_CODE")
    private String DTR_STRUC_CODE;
    @SerializedName("DTR_LOAD_TYPE")
    private String DTR_LOAD_TYPE;
    @SerializedName("DTR_PHASE")
    private String DTR_PHASE;
    @SerializedName("DTR_CAPACITY")
    private String DTR_CAPACITY;
    @SerializedName("DTR_LOCATION")
    private String DTR_LOCATION;
    @SerializedName("DTR_STRUC_TYPE")
    private String DTR_STRUC_TYPE;

    //	@Column(name="NO_OF_POLES")
//	  @SerializedName("NO_OF_POLES")
//	private String NO_OF_POLES;
    @SerializedName("NO_OF_LTCKT")
    private String NO_OF_LTCKT;
    @SerializedName("DTR_ABSWITCH")
    private String DTR_ABSWITCH;
    @SerializedName("DTR_HGFUSE")
    private String DTR_HGFUSE;
    @SerializedName("ABSWITCH_TO_HGFUSE_BUSBAR")
    private String ABSWITCH_TO_HGFUSE_BUSBAR;
    @SerializedName("ABS_COUNDUCTOR_SIZE")
    private String ABS_COUNDUCTOR_SIZE;
    @SerializedName("HGFUSE_TO_DTR_BUSBAR")
    private String HGFUSE_TO_DTR_BUSBAR;
    @SerializedName("HGF_COUNDUCTOR_SIZE")
    private String HGF_COUNDUCTOR_SIZE;
    @SerializedName("EARTH_PITS")
    private String EARTH_PITS;
    @SerializedName("CI_PIPES")
    private String CI_PIPES;
    @SerializedName("GI_PIPES")
    private String GI_PIPES;
    @SerializedName("NO_OF_DTR_METERS")
    private String NO_OF_DTR_METERS;
    @SerializedName("MTR1_SL_NO")
    private String MTR1_SL_NO;
    @SerializedName("MTR1_MAKE")
    private String MTR1_MAKE;
    @SerializedName("MTR1_CAPACITY")
    private String MTR1_CAPACITY;
    @SerializedName("MTR1_TYPE")
    private String MTR1_TYPE;
    @SerializedName("MTR2_SL_NO")
    private String MTR2_SL_NO;
    @SerializedName("MTR2_MAKE")
    private String MTR2_MAKE;
    @SerializedName("MTR2_CAPACITY")
    private String MTR2_CAPACITY;
    @SerializedName("MTR2_TYPE")
    private String MTR2_TYPE;
    @SerializedName("MTR3_SL_NO")
    private String MTR3_SL_NO;
    @SerializedName("MTR3_MAKE")
    private String MTR3_MAKE;
    @SerializedName("MTR3_CAPACITY")
    private String MTR3_CAPACITY;
    @SerializedName("MTR3_TYPE")
    private String MTR3_TYPE;
    @SerializedName("MODEM1_AVL")
    private String MODEM1_AVL;
    @SerializedName("MODEM1_SLNO")
    private String MODEM1_SLNO;
    @SerializedName("MODEM1_MAKE")
    private String MODEM1_MAKE;
    @SerializedName("DTR_POLE")
    private String DTR_POLE;
    @SerializedName("PIN")
    private String PIN;
    @SerializedName("CUT")
    private String CUT;
    @SerializedName("LT_FUSE")
    private String LT_FUSE;
    @SerializedName("LT_BUS_BAR")
    private String LT_BUS_BAR;
    @SerializedName("LT_BUSBAR_CONDUTORSIZE")
    private String LTBUS_CONDUCTOR_SIZE;
    @SerializedName("LT_BUSBAR_CABLESIZE")
    private String LTBUS_CABLE_SIZE;
    @SerializedName("GLAT")
    private String GLAT;
    @SerializedName("GLONG")
    private String GLONG;
    @SerializedName("DTR_POLE_SUPPORT")
    private String DTR_POLE_SUPPORT;
    @SerializedName("NOOF_STURT")
    private String NOOF_STURT;
    @SerializedName("NOOF_STAY")
    private String NOOF_STAY;
    @SerializedName("DTR_ABSWTICH_TYPE")
    private String DTR_ABSWTICH_TYPE;
    @SerializedName("NOOF_PININSULATORS")
    private String NOOF_PININSULATORS;
    @SerializedName("NOOF_DISCINSULATORS")
    private String NOOF_DISCINSULATORS;
    @SerializedName("DTR_LANDMARK")
    private String DTR_LANDMARK;
    @SerializedName("MODEM2_AVL")
    private String MODEM2_AVL;
    @SerializedName("MODEM2_SLNO")
    private String MODEM2_SLNO;
    @SerializedName("MODEM2_MAKE")
    private String MODEM2_MAKE;
    @SerializedName("MODEM3_AVL")
    private String MODEM3_AVL;
    @SerializedName("MODEM3_SLNO")
    private String MODEM3_SLNO;
    @SerializedName("MODEM3_MAKE")
    private String MODEM3_MAKE;

    //NEW
    @SerializedName("STRUCTURE_DEFECTS")
    private String STRUCTURE_DEFECTS;
    @SerializedName("DTR_FENCING")
    private String DTR_FENCING;
    @SerializedName("DTR_AB_DEFECTS")
    private String DTR_AB_DEFECTS;
    @SerializedName("HG_FUSE_DEFECTS")
    private String HG_FUSE_DEFECTS;
    @SerializedName("LT_FUSE_DEFECTS")
    private String LT_FUSE_DEFECTS;
    @SerializedName("NUM_EARTH")
    private String NUM_EARTH;
    @SerializedName("NUM_LT_BUSHINGS")
    private String NUM_LT_BUSHINGS;
    @SerializedName("NUM_HT_BUSHINGS")
    private String NUM_HT_BUSHINGS;
    @SerializedName("NUM_PG_LUG")
    private String NUM_PG_LUG;
    @SerializedName("XLPE_DEFECTS")
    private String XLPE_DEFECTS;
    @SerializedName("NUM_ARRESTER")
    private String NUM_ARRESTER;
    @SerializedName("OIL_LEAK")
    private String OIL_LEAK;
    @SerializedName("OTHER_DEFECTS")
    private String OTHER_DEFECTS;

    @SerializedName("IMAGE_1")
    private String IMAGE_1;
    @SerializedName("IMAGE_2")
    private String IMAGE_2;
    @SerializedName("IMAGE_3")
    private String IMAGE_3;

    @SerializedName("REMARKS")
    private String REMARKS;


    //ADDED ON 26-07-2024
    @SerializedName("BODY_EARTH")
    private String BODY_EARTH;
    @SerializedName("NEUTRAL_EARTH")
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

    public String getREMARKS() {
        return REMARKS;
    }

    public void setREMARKS(String REMARKS) {
        this.REMARKS = REMARKS;
    }

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

    public String getMODEM1_AVL() {
        return MODEM1_AVL;
    }

    public void setMODEM1_AVL(String MODEM1_AVL) {
        this.MODEM1_AVL = MODEM1_AVL;
    }

    public String getMODEM1_SLNO() {
        return MODEM1_SLNO;
    }

    public void setMODEM1_SLNO(String MODEM1_SLNO) {
        this.MODEM1_SLNO = MODEM1_SLNO;
    }

    public String getMODEM1_MAKE() {
        return MODEM1_MAKE;
    }

    public void setMODEM1_MAKE(String MODEM1_MAKE) {
        this.MODEM1_MAKE = MODEM1_MAKE;
    }

    public String getDTR_LANDMARK() {
        return DTR_LANDMARK;
    }

    public void setDTR_LANDMARK(String DTR_LANDMARK) {
        this.DTR_LANDMARK = DTR_LANDMARK;
    }

    public String getMODEM2_AVL() {
        return MODEM2_AVL;
    }

    public void setMODEM2_AVL(String MODEM2_AVL) {
        this.MODEM2_AVL = MODEM2_AVL;
    }

    public String getMODEM2_SLNO() {
        return MODEM2_SLNO;
    }

    public void setMODEM2_SLNO(String MODEM2_SLNO) {
        this.MODEM2_SLNO = MODEM2_SLNO;
    }

    public String getMODEM2_MAKE() {
        return MODEM2_MAKE;
    }

    public void setMODEM2_MAKE(String MODEM2_MAKE) {
        this.MODEM2_MAKE = MODEM2_MAKE;
    }

    public String getMODEM3_AVL() {
        return MODEM3_AVL;
    }

    public void setMODEM3_AVL(String MODEM3_AVL) {
        this.MODEM3_AVL = MODEM3_AVL;
    }

    public String getMODEM3_SLNO() {
        return MODEM3_SLNO;
    }

    public void setMODEM3_SLNO(String MODEM3_SLNO) {
        this.MODEM3_SLNO = MODEM3_SLNO;
    }

    public String getMODEM3_MAKE() {
        return MODEM3_MAKE;
    }

    public void setMODEM3_MAKE(String MODEM3_MAKE) {
        this.MODEM3_MAKE = MODEM3_MAKE;
    }

    public String getSEC_CODE() {
        return SEC_CODE;
    }

    public void setSEC_CODE(String SEC_CODE) {
        this.SEC_CODE = SEC_CODE;
    }

    public String getCREATED_BY() {
        return CREATED_BY;
    }

    public void setCREATED_BY(String CREATED_BY) {
        this.CREATED_BY = CREATED_BY;
    }

    public String getIME_NO() {
        return IME_NO;
    }

    public void setIME_NO(String IME_NO) {
        this.IME_NO = IME_NO;
    }

    public String getFEEDER_ID() {
        return FEEDER_ID;
    }

    public void setFEEDER_ID(String FEEDER_ID) {
        this.FEEDER_ID = FEEDER_ID;
    }

    public String getDTR_MAKE() {
        return DTR_MAKE;
    }

    public void setDTR_MAKE(String DTR_MAKE) {
        this.DTR_MAKE = DTR_MAKE;
    }

    public String getDTR_SL_NO() {
        return DTR_SL_NO;
    }

    public void setDTR_SL_NO(String DTR_SL_NO) {
        this.DTR_SL_NO = DTR_SL_NO;
    }

    public String getDTR_STRUC_CODE() {
        return DTR_STRUC_CODE;
    }

    public void setDTR_STRUC_CODE(String DTR_STRUC_CODE) {
        this.DTR_STRUC_CODE = DTR_STRUC_CODE;
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

    public String getDTR_LOCATION() {
        return DTR_LOCATION;
    }

    public void setDTR_LOCATION(String DTR_LOCATION) {
        this.DTR_LOCATION = DTR_LOCATION;
    }

    public String getDTR_STRUC_TYPE() {
        return DTR_STRUC_TYPE;
    }

    public void setDTR_STRUC_TYPE(String DTR_STRUC_TYPE) {
        this.DTR_STRUC_TYPE = DTR_STRUC_TYPE;
    }

    public String getNO_OF_LTCKT() {
        return NO_OF_LTCKT;
    }

    public void setNO_OF_LTCKT(String NO_OF_LTCKT) {
        this.NO_OF_LTCKT = NO_OF_LTCKT;
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

    public String getABSWITCH_TO_HGFUSE_BUSBAR() {
        return ABSWITCH_TO_HGFUSE_BUSBAR;
    }

    public void setABSWITCH_TO_HGFUSE_BUSBAR(String ABSWITCH_TO_HGFUSE_BUSBAR) {
        this.ABSWITCH_TO_HGFUSE_BUSBAR = ABSWITCH_TO_HGFUSE_BUSBAR;
    }

    public String getABS_COUNDUCTOR_SIZE() {
        return ABS_COUNDUCTOR_SIZE;
    }

    public void setABS_COUNDUCTOR_SIZE(String ABS_COUNDUCTOR_SIZE) {
        this.ABS_COUNDUCTOR_SIZE = ABS_COUNDUCTOR_SIZE;
    }

    public String getHGFUSE_TO_DTR_BUSBAR() {
        return HGFUSE_TO_DTR_BUSBAR;
    }

    public void setHGFUSE_TO_DTR_BUSBAR(String HGFUSE_TO_DTR_BUSBAR) {
        this.HGFUSE_TO_DTR_BUSBAR = HGFUSE_TO_DTR_BUSBAR;
    }

    public String getHGF_COUNDUCTOR_SIZE() {
        return HGF_COUNDUCTOR_SIZE;
    }

    public void setHGF_COUNDUCTOR_SIZE(String HGF_COUNDUCTOR_SIZE) {
        this.HGF_COUNDUCTOR_SIZE = HGF_COUNDUCTOR_SIZE;
    }

    public String getEARTH_PITS() {
        return EARTH_PITS;
    }

    public void setEARTH_PITS(String EARTH_PITS) {
        this.EARTH_PITS = EARTH_PITS;
    }

    public String getCI_PIPES() {
        return CI_PIPES;
    }

    public void setCI_PIPES(String CI_PIPES) {
        this.CI_PIPES = CI_PIPES;
    }

    public String getGI_PIPES() {
        return GI_PIPES;
    }

    public void setGI_PIPES(String GI_PIPES) {
        this.GI_PIPES = GI_PIPES;
    }

    public String getNO_OF_DTR_METERS() {
        return NO_OF_DTR_METERS;
    }

    public void setNO_OF_DTR_METERS(String NO_OF_DTR_METERS) {
        this.NO_OF_DTR_METERS = NO_OF_DTR_METERS;
    }

    public String getMTR1_SL_NO() {
        return MTR1_SL_NO;
    }

    public void setMTR1_SL_NO(String MTR1_SL_NO) {
        this.MTR1_SL_NO = MTR1_SL_NO;
    }

    public String getMTR1_MAKE() {
        return MTR1_MAKE;
    }

    public void setMTR1_MAKE(String MTR1_MAKE) {
        this.MTR1_MAKE = MTR1_MAKE;
    }

    public String getMTR1_CAPACITY() {
        return MTR1_CAPACITY;
    }

    public void setMTR1_CAPACITY(String MTR1_CAPACITY) {
        this.MTR1_CAPACITY = MTR1_CAPACITY;
    }

    public String getMTR1_TYPE() {
        return MTR1_TYPE;
    }

    public void setMTR1_TYPE(String MTR1_TYPE) {
        this.MTR1_TYPE = MTR1_TYPE;
    }

    public String getMTR2_SL_NO() {
        return MTR2_SL_NO;
    }

    public void setMTR2_SL_NO(String MTR2_SL_NO) {
        this.MTR2_SL_NO = MTR2_SL_NO;
    }

    public String getMTR2_MAKE() {
        return MTR2_MAKE;
    }

    public void setMTR2_MAKE(String MTR2_MAKE) {
        this.MTR2_MAKE = MTR2_MAKE;
    }

    public String getMTR2_CAPACITY() {
        return MTR2_CAPACITY;
    }

    public void setMTR2_CAPACITY(String MTR2_CAPACITY) {
        this.MTR2_CAPACITY = MTR2_CAPACITY;
    }

    public String getMTR2_TYPE() {
        return MTR2_TYPE;
    }

    public void setMTR2_TYPE(String MTR2_TYPE) {
        this.MTR2_TYPE = MTR2_TYPE;
    }

    public String getMTR3_SL_NO() {
        return MTR3_SL_NO;
    }

    public void setMTR3_SL_NO(String MTR3_SL_NO) {
        this.MTR3_SL_NO = MTR3_SL_NO;
    }

    public String getMTR3_MAKE() {
        return MTR3_MAKE;
    }

    public void setMTR3_MAKE(String MTR3_MAKE) {
        this.MTR3_MAKE = MTR3_MAKE;
    }

    public String getMTR3_CAPACITY() {
        return MTR3_CAPACITY;
    }

    public void setMTR3_CAPACITY(String MTR3_CAPACITY) {
        this.MTR3_CAPACITY = MTR3_CAPACITY;
    }

    public String getMTR3_TYPE() {
        return MTR3_TYPE;
    }

    public void setMTR3_TYPE(String MTR3_TYPE) {
        this.MTR3_TYPE = MTR3_TYPE;
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

    public String getLT_FUSE() {
        return LT_FUSE;
    }

    public void setLT_FUSE(String LT_FUSE) {
        this.LT_FUSE = LT_FUSE;
    }

    public String getLT_BUS_BAR() {
        return LT_BUS_BAR;
    }

    public void setLT_BUS_BAR(String LT_BUS_BAR) {
        this.LT_BUS_BAR = LT_BUS_BAR;
    }

    public String getLTBUS_CONDUCTOR_SIZE() {
        return LTBUS_CONDUCTOR_SIZE;
    }

    public void setLTBUS_CONDUCTOR_SIZE(String LTBUS_CONDUCTOR_SIZE) {
        this.LTBUS_CONDUCTOR_SIZE = LTBUS_CONDUCTOR_SIZE;
    }

    public String getLTBUS_CABLE_SIZE() {
        return LTBUS_CABLE_SIZE;
    }

    public void setLTBUS_CABLE_SIZE(String LTBUS_CABLE_SIZE) {
        this.LTBUS_CABLE_SIZE = LTBUS_CABLE_SIZE;
    }

    public String getGLAT() {
        return GLAT;
    }

    public void setGLAT(String GLAT) {
        this.GLAT = GLAT;
    }

    public String getGLONG() {
        return GLONG;
    }

    public void setGLONG(String GLONG) {
        this.GLONG = GLONG;
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
}
