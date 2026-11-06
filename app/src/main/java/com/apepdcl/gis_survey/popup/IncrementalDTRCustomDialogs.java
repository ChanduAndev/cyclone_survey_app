package com.apepdcl.gis_survey.popup;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.vo.DTRDetailsResponseVo;
import com.apepdcl.gis_survey.vo.DTRSurveyVo;
import com.apepdcl.gis_survey.vo.IncrementalSurveyDataVo;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class IncrementalDTRCustomDialogs extends Dialog implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    public Activity c;
    private TextView tv_dtrstrcuture;
    private EditText ed_pole_no,ed_frompole;

    private String lineType,oh_isendpole,oh_lineconfig,oh_isstreetlight,oh_otherthan_streetlight,oh_pin_cutpoint,oh_is_doublefeeding_pole,oh_pole_type,oh_pole_height,oh_strut_stay_details,oh_tapping_point,oh_noof_spurlines,oh_conductor_size,oh_ismulti_circuit,oh_noof_circuits,oh_multi_voltage_same_voltage,
            ug_isendpole,ug_locationtype,ug_servicepiller_type,ug_poletype,ug_poleheight,ug_polesupport,ug_feedepiller_outg_circuits,ug_tapping_point,ug_notapping_point,ug_cablesize,ug_nocable;
    private LinearLayout lyt_dtrmeter,lyt_modem,lyt_busbar_condoctorsize,lyt_dtrpolepin,lyt_dtrpolecut,lyt_nof_dtrmeters,lyt_dtrmeter_two,lyt_dtrmeter_three,lyt_lt_busbarconductor_size,lyt_busbarcable_size,lyt_noof_sturts,lyt_dtrhgfuse,lyt_noof_stay,lyt_abswt_type,lyt_gipipes,lyt_cipipes,lyt_modemtwo,lyt_modem_three,lyt_earthpitstype;
    private CheckBox ch_oh_aglservices,ch_oh_aquaservices,ch_oh_industrialservices,ch_oh_33kv_vlckts,ch_oh_11kv_vlckts,ch_oh_6_3kv_vlckts,ch_oh_lt_vlckts;
    private TextInputEditText edt_oh_aglservices,edt_oh_aquaservices,edt_oh_industrialservices,edt_oh_tappinglocation,edt_oh_33kv_vlckts,edt_oh_11kv_vlckts,edt_oh_6_3kv_vlckts,edt_oh_lt_vlckts,edt_oh_pole_paintingcode,edt_oh_remarks,edt_ug_tappinglocation,edt_ug_pole_paintingcode,edt_ug_remarks;
    private TextInputEditText edt_meternum,edt_metermake,edt_metercapacity,edt_meternum_two,edt_metermake_two,edt_metercapacity_two,edt_meternum_three,edt_metermake_three,edt_metercapacity_three,edt_modemserial_no,edt_modem_make,edt_landmark,edt_dtrserialnum,edt_noof_sturts,edt_noof_stay,edt_noof_cpin_insulators,edt_noof_disc_insulators,edt_modemtwo_serial_no,edt_modemtwo_make,edt_modemthree_serial_no,edt_modemthree_make;
    private Button btn_clear,btn_add,btn_lttopclear;
    private ArcGISDatabase arcGISDatabase;
    private List<IncrementalSurveyDataVo> incrementalSurveyDataVoList;
    private int maxSurveyCount,poleCountCheck;
    private String dtrCode,ltCircuit;
    private final String x, y;
    Callout callout;
    GraphicsOverlay graphicsOverlay;
    Graphic graphic;
    private int noof_33kvCkts,noof_11kvCkts,noof_6_3kvCkts,noof_ltCkts;
    private LinearLayout lay_oh_dtr_pole_dtr_meter_avilable,lay_oh_dtr_pole_no_of_meter,lay_ug_dtr_pole_dtr_meter_avilable,lay_ug_dtr_pole_no_of_meter;
    private Spinner sp_dtrabswt,sp_dtrabswt_type,sp_dtrgh_fuseset,sp_earthpits,sp_cipipes,sp_gipipes,sp_dtrmeter,sp_metertype,sp_modem_available,sp_busbar_type,sp_consuctorsize,sp_hgfuseto_dtr,sp_hgfuseto_dtr_consuctorsize,sp_dtrpole,sp_pin,sp_cut,sp_lt_fuseset,sp_dtrload,sp_dtrphase,sp_dtrcapacity,sp_dtrstr_type,sp_dtrs_pole_support,
            sp_noof_dtrmeters,sp_metertype_two,sp_metertype_three,sp_lt_busbar,sp_lt_busbarcable_size,sp_lt_busbarconductor_size,sp_noof_pininsulators,sp_earthpits_type,sp_modemtwo_available,sp_modemthree_available,sp_noof_ltcircuits;
    private String oh_dtr_pole_dtr_meter_avilable,oh_dtr_pole_no_of_meters,ug_dtr_pole_dtr_meter_avilable,ug_dtr_pole_no_of_meters;
    private String dtrABswt,dtrgh_fuseset,earthpits,cipipes,gipipes,dtrmeter,metertype,modem_available,busbar_type,consuctorsize,hgfuseto_dtr,hgfuseto_dtr_consuctorsize,dtrpole,pin,cut,lt_fuseset,
            dtrload,dtrphase,dtrcapacity,dtrstr_type,noof_dtrmeters,metertype_two,metertype_three,lt_busbar,lt_busbarcable_size,lt_busbarconductor_size,dtrs_pole_support,dtrabswt_type,noof_pininsulators,earthpits_type,modemtwo_available,modemthree_available,noof_ltcircuits;
    private AutoCompleteTextView aut_dtrmake;
    private String[] dtrMake;
    private String[] dtrLoad;
    private int earthPitsNo=0,ciPipesCount=0,giPipesCount=0,ciGiPipesCount=0;
    List<DTRDetailsResponseVo> dtrDetailsResponseVoList=null;
    public IncrementalDTRCustomDialogs(Activity a, String x, String y, Callout callout, GraphicsOverlay graphicsOverlay, Graphic graphic, String feederName, String feederCode, int maxSurveyCount, String tappingPoint, String surveyType, GraphicsOverlay currentgraphicsOverlay, Graphic currentGraphic, String ssName, String dtrCode, String ltCircuit) {
        super(a);
        this.c = a;
        this.x = x;
        this.y = y;
        this.dtrCode=dtrCode;
        this.ltCircuit=ltCircuit;
        this.callout=callout;
        this.graphicsOverlay=graphicsOverlay;
        this.graphic=graphic;
        this.maxSurveyCount=maxSurveyCount;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.incremental_dtrcustome_dialog);
        // try {
        setCancelable(false);
        tv_dtrstrcuture=findViewById(R.id.tv_dtrstrcuture);
        aut_dtrmake=findViewById(R.id.aut_dtrmake);
        edt_dtrserialnum=findViewById(R.id.edt_dtrserialnum);
        sp_dtrload=findViewById(R.id.sp_dtrload);
        sp_dtrphase=findViewById(R.id.sp_dtrphase);
        sp_dtrcapacity=findViewById(R.id.sp_dtrcapacity);
        sp_dtrstr_type=findViewById(R.id.sp_dtrstr_type);
        sp_dtrs_pole_support=findViewById(R.id.sp_dtrs_pole_support);
        edt_noof_sturts=findViewById(R.id.edt_noof_sturts);
        edt_noof_stay=findViewById(R.id.edt_noof_stay);
        ed_pole_no=findViewById(R.id.ed_pole_no);
        ed_pole_no.setEnabled(false);
        ed_frompole=findViewById(R.id.ed_frompole);
        ed_frompole.setEnabled(false);
        sp_dtrabswt=findViewById(R.id.sp_dtrabswt);
        sp_dtrabswt_type=findViewById(R.id.sp_dtrabswt_type);
        sp_dtrgh_fuseset=findViewById(R.id.sp_dtrgh_fuseset);
        sp_earthpits=findViewById(R.id.sp_earthpits);
        sp_cipipes=findViewById(R.id.sp_cipipes);
        sp_gipipes=findViewById(R.id.sp_gipipes);
        sp_dtrmeter=findViewById(R.id.sp_dtrmeter);
        sp_noof_dtrmeters=findViewById(R.id.sp_noof_dtrmeters);
        edt_meternum=findViewById(R.id.edt_meternum);
        edt_metermake=findViewById(R.id.edt_metermake);
        edt_metercapacity=findViewById(R.id.edt_metercapacity);
        edt_meternum_two=findViewById(R.id.edt_meternum_two);
        edt_metermake_two=findViewById(R.id.edt_metermake_two);
        edt_metercapacity_two=findViewById(R.id.edt_metercapacity_two);
        edt_meternum_three=findViewById(R.id.edt_meternum_three);
        edt_metermake_three=findViewById(R.id.edt_metermake_three);
        edt_metercapacity_three=findViewById(R.id.edt_metercapacity_three);
        sp_metertype=findViewById(R.id.sp_metertype);
        sp_metertype_two=findViewById(R.id.sp_metertype_two);
        sp_metertype_three=findViewById(R.id.sp_metertype_three);
        sp_modem_available=findViewById(R.id.sp_modem_available);
        edt_modemserial_no=findViewById(R.id.edt_modemserial_no);
        edt_modem_make=findViewById(R.id.edt_modem_make);
        sp_busbar_type=findViewById(R.id.sp_busbar_type);
        sp_consuctorsize=findViewById(R.id.sp_consuctorsize);
        sp_hgfuseto_dtr=findViewById(R.id.sp_hgfuseto_dtr);
        sp_hgfuseto_dtr_consuctorsize=findViewById(R.id.sp_hgfuseto_dtr_consuctorsize);
        sp_dtrpole=findViewById(R.id.sp_dtrpole);
        sp_pin=findViewById(R.id.sp_pin);
        sp_noof_pininsulators=findViewById(R.id.sp_noof_pininsulators);
        sp_cut=findViewById(R.id.sp_cut);
        edt_noof_cpin_insulators=findViewById(R.id.edt_noof_cpin_insulators);
        edt_noof_disc_insulators=findViewById(R.id.edt_noof_disc_insulators);
        edt_landmark=findViewById(R.id.edt_landmark);
        sp_lt_fuseset=findViewById(R.id.sp_lt_fuseset);
        sp_lt_busbar=findViewById(R.id.sp_lt_busbar);
        sp_lt_busbarcable_size=findViewById(R.id.sp_lt_busbarcable_size);
        sp_lt_busbarconductor_size=findViewById(R.id.sp_lt_busbarconductor_size);
        sp_earthpits_type=findViewById(R.id.sp_earthpits_type);

        sp_modemtwo_available=findViewById(R.id.sp_modemtwo_available);
        edt_modemtwo_serial_no=findViewById(R.id.edt_modemtwo_serial_no);
        edt_modemtwo_make=findViewById(R.id.edt_modemtwo_make);
        sp_modemthree_available=findViewById(R.id.sp_modemthree_available);
        edt_modemthree_serial_no=findViewById(R.id.edt_modemthree_serial_no);
        edt_modemthree_make=findViewById(R.id.edt_modemthree_make);

        sp_noof_ltcircuits=findViewById(R.id.sp_noof_ltcircuits);

        lyt_noof_sturts=findViewById(R.id.lyt_noof_sturts);
        lyt_noof_sturts.setVisibility(View.GONE);
        lyt_noof_stay=findViewById(R.id.lyt_noof_stay);
        lyt_noof_stay.setVisibility(View.GONE);
        lyt_dtrmeter=findViewById(R.id.lyt_dtrmeter);
        lyt_dtrmeter.setVisibility(View.GONE);
        lyt_modem=findViewById(R.id.lyt_modem);
        lyt_modem.setVisibility(View.GONE);
        lyt_busbar_condoctorsize=findViewById(R.id.lyt_busbar_condoctorsize);
        lyt_busbar_condoctorsize.setVisibility(View.GONE);
        lyt_dtrpolepin=findViewById(R.id.lyt_dtrpolepin);
        lyt_dtrpolepin.setVisibility(View.GONE);
        lyt_dtrpolecut=findViewById(R.id.lyt_dtrpolecut);
        lyt_dtrpolecut.setVisibility(View.GONE);
        lyt_nof_dtrmeters=findViewById(R.id.lyt_nof_dtrmeters);
        lyt_nof_dtrmeters.setVisibility(View.GONE);
        lyt_dtrmeter_two=findViewById(R.id.lyt_dtrmeter_two);
        lyt_dtrmeter_two.setVisibility(View.GONE);
        lyt_dtrmeter_three=findViewById(R.id.lyt_dtrmeter_three);
        lyt_dtrmeter_three.setVisibility(View.GONE);
        lyt_lt_busbarconductor_size=findViewById(R.id.lyt_lt_busbarconductor_size);
        lyt_lt_busbarconductor_size.setVisibility(View.GONE);
        lyt_busbarcable_size=findViewById(R.id.lyt_busbarcable_size);
        lyt_busbarcable_size.setVisibility(View.GONE);
        lyt_dtrhgfuse=findViewById(R.id.lyt_dtrhgfuse);
        lyt_dtrhgfuse.setVisibility(View.GONE);
        lyt_abswt_type=findViewById(R.id.lyt_abswt_type);
        lyt_abswt_type.setVisibility(View.GONE);
        lyt_gipipes=findViewById(R.id.lyt_gipipes);
        lyt_gipipes.setVisibility(View.GONE);
        lyt_cipipes=findViewById(R.id.lyt_cipipes);
        lyt_cipipes.setVisibility(View.GONE);
        lyt_modemtwo=findViewById(R.id.lyt_modemtwo);
        lyt_modemtwo.setVisibility(View.GONE);
        lyt_modem_three=findViewById(R.id.lyt_modem_three);
        lyt_modem_three.setVisibility(View.GONE);

        lyt_earthpitstype=findViewById(R.id.lyt_earthpitstype);
        lyt_earthpitstype.setVisibility(View.GONE);







        sp_dtrabswt.setOnItemSelectedListener(this);
        sp_dtrabswt_type.setOnItemSelectedListener(this);
        sp_dtrgh_fuseset.setOnItemSelectedListener(this);
        sp_earthpits.setOnItemSelectedListener(this);
        sp_cipipes.setOnItemSelectedListener(this);
        sp_gipipes.setOnItemSelectedListener(this);
        sp_dtrmeter.setOnItemSelectedListener(this);
        sp_noof_dtrmeters.setOnItemSelectedListener(this);
        sp_metertype.setOnItemSelectedListener(this);
        sp_metertype_two.setOnItemSelectedListener(this);
        sp_metertype_three.setOnItemSelectedListener(this);
        sp_modem_available.setOnItemSelectedListener(this);
        sp_busbar_type.setOnItemSelectedListener(this);
        sp_consuctorsize.setOnItemSelectedListener(this);
        sp_hgfuseto_dtr.setOnItemSelectedListener(this);
        sp_hgfuseto_dtr_consuctorsize.setOnItemSelectedListener(this);
        sp_dtrpole.setOnItemSelectedListener(this);
        sp_pin.setOnItemSelectedListener(this);
        sp_noof_pininsulators.setOnItemSelectedListener(this);
        sp_cut.setOnItemSelectedListener(this);
        sp_lt_fuseset.setOnItemSelectedListener(this);
        sp_lt_busbar.setOnItemSelectedListener(this);
        sp_lt_busbarcable_size.setOnItemSelectedListener(this);
        sp_dtrload.setOnItemSelectedListener(this);
        sp_dtrphase.setOnItemSelectedListener(this);
        sp_dtrcapacity.setOnItemSelectedListener(this);
        sp_dtrstr_type.setOnItemSelectedListener(this);
        sp_dtrs_pole_support.setOnItemSelectedListener(this);
        sp_lt_busbarconductor_size.setOnItemSelectedListener(this);
        sp_earthpits_type.setOnItemSelectedListener(this);
        sp_modemtwo_available.setOnItemSelectedListener(this);
        sp_modemthree_available.setOnItemSelectedListener(this);
        sp_noof_ltcircuits.setOnItemSelectedListener(this);

        btn_clear=findViewById(R.id.btn_clear);
        btn_add=findViewById(R.id.btn_add);
        btn_lttopclear=findViewById(R.id.btn_lttopclear);
        btn_clear.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        btn_lttopclear.setOnClickListener(this);

        arcGISDatabase=new ArcGISDatabase(c);
       dtrDetailsResponseVoList=new ArrayList<>();



        dtrMake= c.getResources().getStringArray(R.array.dtr_make);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (c,android.R.layout.select_dialog_item, dtrMake);

        aut_dtrmake.setThreshold(2);
        aut_dtrmake.setAdapter(adapter);


        tv_dtrstrcuture.setText(dtrCode);

         //dtrDetailsResponseVoList=arcGISDatabase.getDTRDetailsData();
        dtrDetailsResponseVoList=arcGISDatabase.getIncrementalDTRDetailsData();
        aut_dtrmake.setText(dtrDetailsResponseVoList.get(0).getDTR_MAKE());
        edt_dtrserialnum.setText(dtrDetailsResponseVoList.get(0).getDTR_SNO());
        List<String>  dtrloadList= new ArrayList<>();
        dtrloadList.add(dtrDetailsResponseVoList.get(0).getTYPE_OF_LOAD());
        dtrloadList.add("24 HRS SUPPLY");
        dtrloadList.add("DHQ<");
        dtrloadList.add("DOMESTIC");
        dtrloadList.add("INDUSTRIAL");
        dtrloadList.add("INDUSTRIAL-DEDICATED");
        dtrloadList.add("INDUSTRIAL-EXPRESS");
        dtrloadList.add("LIFT IRRIGATION");
        dtrloadList.add("MHQ");
        dtrloadList.add("MIXED");
        dtrloadList.add("MIXED INDL");
        dtrloadList.add("PURE AGL");
        dtrloadList.add("REC");
        dtrloadList.add("RURAL");
        dtrloadList.add("RWS");
        dtrloadList.add("SOLAR");
        dtrloadList.add("TOWN");
        dtrloadList.add("WATER WORKS");
        ArrayAdapter<String> dtrloadListadapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, dtrloadList);
        sp_dtrload.setAdapter(dtrloadListadapter);


        List<String>  dtrPhaseList= new ArrayList<>();
        if("III".equalsIgnoreCase(dtrDetailsResponseVoList.get(0).getDTR_PHASE())){
            dtrPhaseList.add(dtrDetailsResponseVoList.get(0).getDTR_PHASE());
            dtrPhaseList.add("I");
            ArrayAdapter<String> dtrPhaseListadapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, dtrPhaseList);
            sp_dtrphase.setAdapter(dtrPhaseListadapter);
        }else if("I".equalsIgnoreCase(dtrDetailsResponseVoList.get(0).getDTR_PHASE())){
            dtrPhaseList.add(dtrDetailsResponseVoList.get(0).getDTR_PHASE());
            dtrPhaseList.add("III");
            ArrayAdapter<String> dtrPhaseListadapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, dtrPhaseList);
            sp_dtrphase.setAdapter(dtrPhaseListadapter);
        }




        incrementalSurveyDataVoList = arcGISDatabase.getIncrementalSurveyData("");
        int count = arcGISDatabase.getObjectId();
        poleCountCheck = arcGISDatabase.getObjectIdCount();
        if (poleCountCheck == 0) {
            //  if(""surveyType)
            int polenum = maxSurveyCount+ 1;
            ed_frompole.setText(String.valueOf(count));
            ed_pole_no.setText(String.valueOf(polenum));
               /* ed_frompole.setText(String.valueOf("0"));
                ed_pole_no.setText(String.valueOf(polenum));*/
        } else {
            ed_frompole.setText(String.valueOf(count));
            ed_pole_no.setText(String.valueOf(count+1));
        }




      /*  }catch (Exception e){
            e.printStackTrace();
        }*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_clear:

                callout.dismiss();
                graphicsOverlay.getGraphics().remove(graphic);
                dismiss();
                break;

            case R.id.btn_add:
                validateDTRSurveyData();

                break;
            case R.id.btn_lttopclear:
                callout.dismiss();
                graphicsOverlay.getGraphics().remove(graphic);
                dismiss();
                break;

        }


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.sp_dtrabswt:
                dtrABswt=sp_dtrabswt.getSelectedItem().toString();
                if("YES".equalsIgnoreCase(dtrABswt)){
                    lyt_abswt_type.setVisibility(View.VISIBLE);
                }else {
                    lyt_abswt_type.setVisibility(View.GONE);
                }
                break;
            case R.id.sp_dtrabswt_type:
                dtrabswt_type=sp_dtrabswt_type.getSelectedItem().toString();
                break;


            case R.id.sp_dtrgh_fuseset:
                dtrgh_fuseset=sp_dtrgh_fuseset.getSelectedItem().toString();
                if("YES".equalsIgnoreCase(dtrgh_fuseset)){
                    lyt_dtrhgfuse.setVisibility(View.VISIBLE);
                }else {
                    lyt_dtrhgfuse.setVisibility(View.GONE);
                }

                break;
            case R.id.sp_earthpits:
                earthpits=sp_earthpits.getSelectedItem().toString();

                earthPitsNo=Integer.parseInt(earthpits);
                if(earthPitsNo==0){
                    lyt_earthpitstype.setVisibility(View.GONE);
                    lyt_cipipes.setVisibility(View.GONE);
                    lyt_gipipes.setVisibility(View.GONE);
                }else {
                    lyt_earthpitstype.setVisibility(View.VISIBLE);
                }
                /*if(!"Select".equalsIgnoreCase(sp_cipipes.getSelectedItem().toString())){
                    if(earthPitsNo==ciPipesCount || ciPipesCount>earthPitsNo) {
                        lyt_gipipes.setVisibility(View.GONE);
                    }else{
                        lyt_gipipes.setVisibility(View.VISIBLE);
                    }
                }else if("Select".equalsIgnoreCase(sp_cipipes.getSelectedItem().toString())){
                lyt_gipipes.setVisibility(View.VISIBLE);
            }*/
                break;
            case R.id.sp_earthpits_type:
                earthpits_type=sp_earthpits_type.getSelectedItem().toString();
                if("CI Pipes".equalsIgnoreCase(earthpits_type)){
                    lyt_cipipes.setVisibility(View.VISIBLE);
                    lyt_gipipes.setVisibility(View.GONE);
                }else if("GI Pipes".equalsIgnoreCase(earthpits_type)){
                    lyt_gipipes.setVisibility(View.VISIBLE);
                    lyt_cipipes.setVisibility(View.GONE);
                }else if("CI AND GI Pipes".equalsIgnoreCase(earthpits_type)){
                    lyt_cipipes.setVisibility(View.VISIBLE);
                    lyt_gipipes.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.sp_cipipes:
                cipipes=sp_cipipes.getSelectedItem().toString();
                ciPipesCount = Integer.parseInt(cipipes);

                if("CI Pipes".equalsIgnoreCase(earthpits_type)){
                    if(ciPipesCount >earthPitsNo || ciPipesCount <earthPitsNo){
                        Toast.makeText(c, "CI Pipes count should be equal to Earth Pits Count", Toast.LENGTH_LONG).show();
                    }
                }


                /*if(!"Select".equalsIgnoreCase(sp_cipipes.getSelectedItem().toString())) {
                    ciPipesCount = Integer.parseInt(cipipes);
                    if (earthPitsNo == ciPipesCount) {
                        lyt_gipipes.setVisibility(View.GONE);
                    } else if (ciPipesCount > earthPitsNo) {
                        lyt_gipipes.setVisibility(View.GONE);
                        Toast.makeText(c, "CI Pipes count should not greater than Earth Pits Count", Toast.LENGTH_LONG).show();
                    } else {
                        lyt_gipipes.setVisibility(View.VISIBLE);
                    }
                }else if("Select".equalsIgnoreCase(sp_cipipes.getSelectedItem().toString())){
                lyt_gipipes.setVisibility(View.VISIBLE);
            }*/
                break;
            case R.id.sp_gipipes:
                gipipes=sp_gipipes.getSelectedItem().toString();
                giPipesCount = Integer.parseInt(gipipes);
                if("GI Pipes".equalsIgnoreCase(earthpits_type)){
                    if(giPipesCount >earthPitsNo || giPipesCount <earthPitsNo){
                        Toast.makeText(c, "GI Pipes count should be equal to Earth Pits Count", Toast.LENGTH_LONG).show();
                    }
                }
              /*  if(!"Select".equalsIgnoreCase(gipipes)) {
                    giPipesCount = Integer.parseInt(gipipes);
                }*/
                break;
            case R.id.sp_dtrmeter:
                dtrmeter=sp_dtrmeter.getSelectedItem().toString();
                if("YES".equalsIgnoreCase(dtrmeter)) {
                    lyt_nof_dtrmeters.setVisibility(View.VISIBLE);
                    // lyt_dtrmeter.setVisibility(View.VISIBLE);
                }else {
                    lyt_nof_dtrmeters.setVisibility(View.GONE);
                    lyt_dtrmeter.setVisibility(View.GONE);
                    lyt_dtrmeter_three.setVisibility(View.GONE);
                    lyt_dtrmeter_two.setVisibility(View.GONE);
                    edt_meternum.setText("");
                    edt_metermake.setText("");
                    edt_metercapacity.setText("");
                    edt_meternum_two.setText("");
                    edt_metermake_two.setText("");
                    edt_metercapacity_two.setText("");
                    edt_meternum_three.setText("");
                    edt_metermake_three.setText("");
                    edt_metercapacity_three.setText("");
                    //lyt_dtrmeter.setVisibility(View.GONE);
                   /* edt_meternum.setText("");
                    edt_metermake.setText("");
                    edt_metercapacity.setText("");*/
                }
                break;
            case R.id.sp_noof_dtrmeters:
                noof_dtrmeters=sp_noof_dtrmeters.getSelectedItem().toString();
                if("1".equalsIgnoreCase(noof_dtrmeters)){
                    lyt_dtrmeter.setVisibility(View.VISIBLE);
                    lyt_dtrmeter_three.setVisibility(View.GONE);
                    lyt_dtrmeter_two.setVisibility(View.GONE);
                    edt_meternum_two.setText("");
                    edt_metermake_two.setText("");
                    edt_metercapacity_two.setText("");
                    edt_meternum_three.setText("");
                    edt_metermake_three.setText("");
                    edt_metercapacity_three.setText("");
                }else if("2".equalsIgnoreCase(noof_dtrmeters)){
                    lyt_dtrmeter_two.setVisibility(View.VISIBLE);
                    lyt_dtrmeter_three.setVisibility(View.GONE);
                    edt_meternum_three.setText("");
                    edt_metermake_three.setText("");
                    edt_metercapacity_three.setText("");
                }else if("3".equalsIgnoreCase(noof_dtrmeters)){
                    lyt_dtrmeter.setVisibility(View.VISIBLE);
                    lyt_dtrmeter_three.setVisibility(View.VISIBLE);
                    lyt_dtrmeter_two.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.sp_metertype:
                metertype=sp_metertype.getSelectedItem().toString();
                break;
            case R.id.sp_metertype_two:
                metertype_two=sp_metertype_two.getSelectedItem().toString();
                break;

            case R.id.sp_metertype_three:
                metertype_three=sp_metertype_three.getSelectedItem().toString();
                break;


            case R.id.sp_modem_available:
                modem_available=sp_modem_available.getSelectedItem().toString();
                if("YES".equalsIgnoreCase(modem_available)){
                    lyt_modem.setVisibility(View.VISIBLE);
                }else {
                    lyt_modem.setVisibility(View.GONE);
                    edt_modemserial_no.setText("");
                    edt_modemtwo_make.setText("");
                }
                break;

            case R.id.sp_modemtwo_available:
                modemtwo_available=sp_modemtwo_available.getSelectedItem().toString();
                if("YES".equalsIgnoreCase(modemtwo_available)){
                    lyt_modemtwo.setVisibility(View.VISIBLE);
                }else {
                    lyt_modemtwo.setVisibility(View.GONE);
                    edt_modemtwo_serial_no.setText("");
                    edt_modemtwo_make.setText("");
                }
                break;
            case R.id.sp_modemthree_available:
                modemthree_available=sp_modemthree_available.getSelectedItem().toString();
                if("YES".equalsIgnoreCase(modemthree_available)){
                    lyt_modem_three.setVisibility(View.VISIBLE);
                }else {
                    lyt_modem_three.setVisibility(View.GONE);
                    edt_modemthree_serial_no.setText("");
                    edt_modemthree_make.setText("");
                }
                break;

            case R.id.sp_busbar_type:
                busbar_type=sp_busbar_type.getSelectedItem().toString();
                if("Conductor".equalsIgnoreCase(busbar_type)) {
                    lyt_busbar_condoctorsize.setVisibility(View.VISIBLE);
                }else {
                    lyt_busbar_condoctorsize.setVisibility(View.GONE);
                }
                break;
            case R.id.sp_consuctorsize:
                consuctorsize=sp_consuctorsize.getSelectedItem().toString();
                break;

            case R.id.sp_hgfuseto_dtr:
                hgfuseto_dtr=sp_hgfuseto_dtr.getSelectedItem().toString();
                break;

            case R.id.sp_dtrpole:
                dtrpole=sp_dtrpole.getSelectedItem().toString();
                if("PIN".equalsIgnoreCase(dtrpole)) {
                    lyt_dtrpolepin.setVisibility(View.VISIBLE);
                    lyt_dtrpolecut.setVisibility(View.GONE);
                    edt_noof_cpin_insulators.setText("");
                    edt_noof_disc_insulators.setText("");
                }else if("CUT".equalsIgnoreCase(dtrpole)) {
                    lyt_dtrpolecut.setVisibility(View.VISIBLE);
                    lyt_dtrpolepin.setVisibility(View.GONE);
                }
                break;

            case R.id.sp_hgfuseto_dtr_consuctorsize:
                hgfuseto_dtr_consuctorsize=sp_hgfuseto_dtr_consuctorsize.getSelectedItem().toString();
                break;

            case R.id.sp_pin:
                pin=sp_pin.getSelectedItem().toString();
                break;

            case R.id.sp_cut:
                cut=sp_cut.getSelectedItem().toString();
                break;

            case R.id.sp_lt_fuseset:
                lt_fuseset=sp_lt_fuseset.getSelectedItem().toString();
                break;

            case R.id.sp_lt_busbar:
                lt_busbar=sp_lt_busbar.getSelectedItem().toString();
                if("Cable".equalsIgnoreCase(lt_busbar)){
                    lyt_busbarcable_size.setVisibility(View.VISIBLE);
                    lyt_lt_busbarconductor_size.setVisibility(View.GONE);
                }else if("Conductor".equalsIgnoreCase(lt_busbar)){
                    lyt_lt_busbarconductor_size.setVisibility(View.VISIBLE);
                    lyt_busbarcable_size.setVisibility(View.GONE);
                }
                break;

            case R.id.sp_lt_busbarcable_size:
                lt_busbarcable_size=sp_lt_busbarcable_size.getSelectedItem().toString();
                break;

            case R.id.sp_lt_busbarconductor_size:
                lt_busbarconductor_size=sp_lt_busbarconductor_size.getSelectedItem().toString();
                break;

            case R.id.sp_dtrload:
                dtrload=sp_dtrload.getSelectedItem().toString();
                break;
            case R.id.sp_dtrphase:
                dtrphase=sp_dtrphase.getSelectedItem().toString();
                if("I".equalsIgnoreCase(dtrphase)) {
                    List<String>  phase1= new ArrayList<>();
                    /*phase1.add("Select");*/
                    phase1.add(dtrDetailsResponseVoList.get(0).getDTR_CAPACITY());
                    phase1.add("5 KVA");
                    phase1.add("10 KVA");
                    phase1.add("15 KVA");
                    phase1.add("16 KVA");
                    phase1.add("25 KVA");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, phase1);
                    sp_dtrcapacity.setAdapter(adapter);
                }else if("III".equalsIgnoreCase(dtrphase)){
                    List<String>  phase3= new ArrayList<>();
                    /*phase3.add("Select");*/
                    phase3.add(dtrDetailsResponseVoList.get(0).getDTR_CAPACITY());
                    phase3.add("15 KVA");
                    phase3.add("16 KVA");
                    phase3.add("25 KVA");
                    phase3.add("40 KVA");
                    phase3.add("50 KVA");
                    phase3.add("63 KVA");
                    phase3.add("75 KVA");
                    phase3.add("100 KVA");
                    phase3.add("160 KVA");
                    phase3.add("200 KVA");
                    phase3.add("250 KVA");
                    phase3.add("300 KVA");
                    phase3.add("315 KVA");
                    phase3.add("400 KVA");
                    phase3.add("500 KVA");
                    phase3.add("750 KVA");
                    phase3.add("1000 KVA");
                    phase3.add("1500 KVA");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, phase3);
                    sp_dtrcapacity.setAdapter(adapter);
                }
                break;
            case R.id.sp_dtrcapacity:
                dtrcapacity=sp_dtrcapacity.getSelectedItem().toString();
                break;
            case R.id.sp_dtrstr_type:
                dtrstr_type=sp_dtrstr_type.getSelectedItem().toString();
               /* if("POLE".equalsIgnoreCase(dtrstr_type)){
                    lyt_noof_poles.setVisibility(View.VISIBLE);
                }else {
                    lyt_noof_poles.setVisibility(View.GONE);
                }*/
                break;

            case R.id.sp_dtrs_pole_support:
                dtrs_pole_support=sp_dtrs_pole_support.getSelectedItem().toString();
                if("STURT".equalsIgnoreCase(dtrs_pole_support)) {
                    lyt_noof_stay.setVisibility(View.GONE);
                    lyt_noof_sturts.setVisibility(View.VISIBLE);
                }else if("STAY".equalsIgnoreCase(dtrs_pole_support)){
                    lyt_noof_stay.setVisibility(View.VISIBLE);
                    lyt_noof_sturts.setVisibility(View.GONE);
                }else if("STURT AND STAY".equalsIgnoreCase(dtrs_pole_support)){
                    lyt_noof_stay.setVisibility(View.VISIBLE);
                    lyt_noof_sturts.setVisibility(View.VISIBLE);
                }

                break;

            case R.id.sp_noof_pininsulators:
                noof_pininsulators=sp_noof_pininsulators.getSelectedItem().toString();
                break;

            case R.id.sp_noof_ltcircuits:
                noof_ltcircuits=sp_noof_ltcircuits.getSelectedItem().toString();
                break;


        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void validateDTRSurveyData(){
        try {

            if ("CI AND GI Pipes".equalsIgnoreCase(earthpits_type)) {
                ciGiPipesCount = ciPipesCount + giPipesCount;
            }

            if (aut_dtrmake.getText().toString().isEmpty()) {
                Toast toast=Toast.makeText(c, "Enter DTR Make", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                toast.show();
            } else if (edt_dtrserialnum.getText().toString().isEmpty()) {
                Toast toast=Toast.makeText(c, "Enter DTR SerialNo.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                toast.show();
            } /*else if ("Select".equalsIgnoreCase(dtrcapacity)) {
                Toast.makeText(c, "Select DTR Capacity", Toast.LENGTH_SHORT).show();
            }*//*else if("POLE".equalsIgnoreCase(dtrstr_type) && edt_noof_poles.getText().toString().isEmpty()){
            Toast.makeText(c,"Enter No.Of Poles",Toast.LENGTH_SHORT).show();
        }*/ else if ("STURT".equalsIgnoreCase(dtrs_pole_support) && edt_noof_sturts.getText().toString().isEmpty()) {
                Toast toast=Toast.makeText(c, "Enter No.Of Sturts", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("STAY".equalsIgnoreCase(dtrs_pole_support) && edt_noof_stay.getText().toString().isEmpty()) {
                Toast toast=Toast.makeText(c, "Enter No.Of Stays", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("STURT AND STAY".equalsIgnoreCase(dtrs_pole_support) && edt_noof_sturts.getText().toString().isEmpty() && edt_noof_stay.getText().toString().isEmpty()) {
                Toast toast=Toast.makeText(c, "Enter No.Of Sturts and Stays", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("CI Pipes".equalsIgnoreCase(earthpits_type) && ciPipesCount > earthPitsNo ) {
                Toast toast=Toast.makeText(c, "CI Pipes count should be equal to Earth Pits Count", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("CI Pipes".equalsIgnoreCase(earthpits_type) && ciPipesCount < earthPitsNo) {
                Toast toast=Toast.makeText(c, "CI Pipes count should be equal to Earth Pits Count", Toast.LENGTH_LONG);
            } else if ("GI Pipes".equalsIgnoreCase(earthpits_type) && giPipesCount > earthPitsNo) {
                Toast toast=Toast.makeText(c, "GI Pipes count should be equal to Earth Pits Count", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                toast.show();
            }else if ("GI Pipes".equalsIgnoreCase(earthpits_type) && giPipesCount < earthPitsNo) {
                Toast toast=Toast.makeText(c, "GI Pipes count should be equal to Earth Pits Count", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("CI AND GI Pipes".equalsIgnoreCase(earthpits_type) && ciGiPipesCount > earthPitsNo) {
                Toast toast=Toast.makeText(c, "CI and GI Pipes count should be equal to Earth Pits Count", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                toast.show();
            }else if ("CI AND GI Pipes".equalsIgnoreCase(earthpits_type) && ciGiPipesCount < earthPitsNo) {
                Toast toast=Toast.makeText(c, "CI and GI Pipes count should be equal to Earth Pits Count", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                toast.show();
            }

        /*else if("Select".equalsIgnoreCase(cipipes)) {
            Toast.makeText(c,"Select  No.Of CI Pipes",Toast.LENGTH_SHORT).show();
        }else if(ciPipesCount < earthPitsNo && "Select".equalsIgnoreCase(gipipes)){
            Toast.makeText(c,"Select No.Of GI Pipes",Toast.LENGTH_SHORT).show();
        }else if(ciPipesCount >earthPitsNo){
            Toast.makeText(c, "CI Pipes count should not greater than Earth Pits Count", Toast.LENGTH_LONG).show();
        }else if(ciGiPipesCount >earthPitsNo){
            Toast.makeText(c, "CI &GI Pipes count should not greater than Earth Pits Count", Toast.LENGTH_LONG).show();
        }else if(ciGiPipesCount <earthPitsNo){
            Toast.makeText(c, "CI &GI Pipes count should not less than Earth Pits Count", Toast.LENGTH_LONG).show();
        }*/



        /*else if(""){

        }*/


            else if ("1".equalsIgnoreCase(noof_dtrmeters) && edt_meternum.getText().toString().isEmpty()) {
                Toast toast=Toast.makeText(c, "Enter Meter1 No.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("1".equalsIgnoreCase(noof_dtrmeters) && edt_metermake.getText().toString().isEmpty()) {
                Toast toast=Toast.makeText(c, "Enter Meter1 Make.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("1".equalsIgnoreCase(noof_dtrmeters) && edt_metercapacity.getText().toString().isEmpty()) {
                Toast toast=Toast.makeText(c, "Enter Meter1 Capacity.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("2".equalsIgnoreCase(noof_dtrmeters) && edt_meternum.getText().toString().isEmpty()) {
                Toast toast=Toast.makeText(c, "Enter Meter1 No.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("2".equalsIgnoreCase(noof_dtrmeters) && edt_metermake.getText().toString().isEmpty()) {
                Toast toast=Toast.makeText(c, "Enter Meter1 Make.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("2".equalsIgnoreCase(noof_dtrmeters) && edt_metercapacity.getText().toString().isEmpty()) {
                Toast toast=Toast.makeText(c, "Enter Meter1 Capacity.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("2".equalsIgnoreCase(noof_dtrmeters) && edt_meternum_two.getText().toString().isEmpty()) {
                Toast toast=Toast.makeText(c, "Enter Meter2 No.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("2".equalsIgnoreCase(noof_dtrmeters) && edt_metermake_two.getText().toString().isEmpty()) {
                Toast toast=Toast.makeText(c, "Enter Meter2 Make.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("2".equalsIgnoreCase(noof_dtrmeters) && edt_metercapacity_two.getText().toString().isEmpty()) {
                Toast toast=Toast.makeText(c, "Enter Meter2 Capacity.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("3".equalsIgnoreCase(noof_dtrmeters) && edt_meternum.getText().toString().isEmpty()) {
                Toast toast=Toast.makeText(c, "Enter Meter1 No.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("3".equalsIgnoreCase(noof_dtrmeters) && edt_metermake.getText().toString().isEmpty()) {
                Toast toast=Toast.makeText(c, "Enter Meter1 Make.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("3".equalsIgnoreCase(noof_dtrmeters) && edt_metercapacity.getText().toString().isEmpty()) {
                Toast toast=Toast.makeText(c, "Enter Meter1 Capacity.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("3".equalsIgnoreCase(noof_dtrmeters) && edt_meternum_two.getText().toString().isEmpty()) {
                Toast toast=Toast.makeText(c, "Enter Meter2 No.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("3".equalsIgnoreCase(noof_dtrmeters) && edt_metermake_two.getText().toString().isEmpty()) {
                Toast toast=Toast.makeText(c, "Enter Meter2 Make.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("3".equalsIgnoreCase(noof_dtrmeters) && edt_metercapacity_two.getText().toString().isEmpty()) {
                Toast toast=Toast.makeText(c, "Enter Meter3 Capacity.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("3".equalsIgnoreCase(noof_dtrmeters) && edt_meternum_three.getText().toString().isEmpty()) {
                Toast toast=Toast.makeText(c, "Enter Meter3 No.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("3".equalsIgnoreCase(noof_dtrmeters) && edt_metermake_three.getText().toString().isEmpty()) {
                Toast toast=Toast.makeText(c, "Enter Meter3 Make.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("3".equalsIgnoreCase(noof_dtrmeters) && edt_metercapacity_three.getText().toString().isEmpty()) {
                Toast toast=Toast.makeText(c, "Enter Meter3 Capacity.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                toast.show();
            }else if ("YES".equalsIgnoreCase(modem_available) && edt_modemserial_no.getText().toString().isEmpty()) {
                Toast toast=Toast.makeText(c, "Enter Modem1 SerialNo.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("YES".equalsIgnoreCase(modem_available) && edt_modem_make.getText().toString().isEmpty()) {
                Toast toast=Toast.makeText(c, "Enter Modem1 Make", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                toast.show();
            }else if ("YES".equalsIgnoreCase(modemtwo_available) && edt_modemtwo_serial_no.getText().toString().isEmpty()) {
                Toast toast=Toast.makeText(c, "Enter Modem2 SerialNo.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("YES".equalsIgnoreCase(modemtwo_available) && edt_modemtwo_make.getText().toString().isEmpty()) {
                Toast toast=Toast.makeText(c, "Enter Modem2 Make.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                toast.show();
            }else if ("YES".equalsIgnoreCase(modemthree_available) && edt_modemthree_serial_no.getText().toString().isEmpty()) {
                Toast toast=Toast.makeText(c, "Enter Modem3 SerialNo.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("YES".equalsIgnoreCase(modemthree_available) && edt_modemthree_make.getText().toString().isEmpty()) {
                Toast toast=Toast.makeText(c, "Enter Modem3 Make.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("CUT".equalsIgnoreCase(dtrpole) && edt_noof_cpin_insulators.getText().toString().isEmpty()) {
                Toast toast=Toast.makeText(c, "Enter No.Of Insulators.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("CUT".equalsIgnoreCase(dtrpole) && edt_noof_disc_insulators.getText().toString().isEmpty()) {
                Toast toast=Toast.makeText(c, "Enter No.Of Disc Insulators", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                toast.show();
            } else {
                try {
                    DTRSurveyVo dtrSurveyVo = new DTRSurveyVo();
                    dtrSurveyVo.setDTR_STRUCTURE(dtrCode);
                    dtrSurveyVo.setDTR_MAKE(aut_dtrmake.getText().toString().trim());
                    dtrSurveyVo.setDTR_SERIALNO(edt_dtrserialnum.getText().toString());
                    dtrSurveyVo.setDTR_LOAD_TYPE(dtrload);
                    dtrSurveyVo.setDTR_PHASE(dtrphase);
                    dtrSurveyVo.setDTR_CAPACITY(dtrcapacity);
                    dtrSurveyVo.setDTR_STRUCTURE_TYPE(dtrstr_type);
                    dtrSurveyVo.setDTR_POLE_SUPPORT(dtrs_pole_support);

                    if ("STURT".equalsIgnoreCase(dtrs_pole_support)) {
                        dtrSurveyVo.setNOOF_STURT(edt_noof_sturts.getText().toString());
                    }
                    if ("STAY".equalsIgnoreCase(dtrs_pole_support) && edt_noof_stay.getText().toString().isEmpty()) {
                        dtrSurveyVo.setNOOF_STAY(edt_noof_stay.getText().toString());
                    }

                    if ("STURT AND STAY".equalsIgnoreCase(dtrs_pole_support)) {
                        dtrSurveyVo.setNOOF_STURT(edt_noof_sturts.getText().toString());
                        dtrSurveyVo.setNOOF_STAY(edt_noof_stay.getText().toString());
                    }
                    dtrSurveyVo.setDTR_ABSWITCH(dtrABswt);
                    if ("YES".equalsIgnoreCase(dtrABswt)) {
                        dtrSurveyVo.setDTR_ABSWTICH_TYPE(dtrabswt_type);
                    }

                    dtrSurveyVo.setEARTH_PITS(earthpits);
                    dtrSurveyVo.setCIPIPES(cipipes);
                    dtrSurveyVo.setGIPIPES(gipipes);
                    dtrSurveyVo.setDTR_METER(dtrmeter);
                    if ("YES".equalsIgnoreCase(dtrmeter)) {

                        dtrSurveyVo.setNOOF_DTRMETERS(noof_dtrmeters);
                        if ("1".equalsIgnoreCase(noof_dtrmeters)) {
                            dtrSurveyVo.setDTR_METER_NUM(edt_meternum.getText().toString().trim());
                            dtrSurveyVo.setDTR_METER_MAKE(edt_metermake.getText().toString().trim());
                            dtrSurveyVo.setDTR_METER_CAPACITY(edt_metercapacity.getText().toString().trim());
                            dtrSurveyVo.setDTR_METER_TYPE(metertype);
                        }
                        if ("2".equalsIgnoreCase(noof_dtrmeters)) {
                            dtrSurveyVo.setDTR_METER_NUM(edt_meternum.getText().toString().trim());
                            dtrSurveyVo.setDTR_METER_MAKE(edt_metermake.getText().toString().trim());
                            dtrSurveyVo.setDTR_METER_CAPACITY(edt_metercapacity.getText().toString().trim());
                            dtrSurveyVo.setDTR_METER_TYPE(metertype);
                            dtrSurveyVo.setDTR_MTR2_NUM(edt_meternum_two.getText().toString().trim());
                            dtrSurveyVo.setDTR_MTR2_MAKE(edt_metermake_two.getText().toString().trim());
                            dtrSurveyVo.setDTR_MTR2_CAPACITY(edt_metercapacity_two.getText().toString().trim());
                            dtrSurveyVo.setDTR_MTR2_TYPE(metertype_two);
                        }
                        if ("3".equalsIgnoreCase(noof_dtrmeters)) {
                            dtrSurveyVo.setDTR_METER_NUM(edt_meternum.getText().toString().trim());
                            dtrSurveyVo.setDTR_METER_MAKE(edt_metermake.getText().toString().trim());
                            dtrSurveyVo.setDTR_METER_CAPACITY(edt_metercapacity.getText().toString().trim());
                            dtrSurveyVo.setDTR_METER_TYPE(metertype);
                            dtrSurveyVo.setDTR_MTR2_NUM(edt_meternum_two.getText().toString().trim());
                            dtrSurveyVo.setDTR_MTR2_MAKE(edt_metermake_two.getText().toString().trim());
                            dtrSurveyVo.setDTR_MTR2_CAPACITY(edt_metercapacity_two.getText().toString().trim());
                            dtrSurveyVo.setDTR_MTR2_TYPE(metertype_two);
                            dtrSurveyVo.setDTR_MTR3_NUM(edt_meternum_three.getText().toString().trim());
                            dtrSurveyVo.setDTR_MTR3_MAKE(edt_metermake_three.getText().toString().trim());
                            dtrSurveyVo.setDTR_MTR3_CAPACITY(edt_metercapacity_three.getText().toString().trim());
                            dtrSurveyVo.setDTR_MTR3_TYPE(metertype_three);
                        }

                    }
                    dtrSurveyVo.setMODEM(modem_available);
                    if ("YES".equalsIgnoreCase(modem_available)) {
                        dtrSurveyVo.setMODEM_NUM(edt_modemserial_no.getText().toString().trim());
                        dtrSurveyVo.setMODEM_MAKE(edt_modem_make.getText().toString().trim());
                    }




                    dtrSurveyVo.setMODEM2(modemtwo_available);
                     if ("YES".equalsIgnoreCase(modemtwo_available)) {
                        dtrSurveyVo.setMODEM2_NUM(edt_modemtwo_serial_no.getText().toString());
                         dtrSurveyVo.setMODEM2_MAKE(edt_modemtwo_make.getText().toString());
                    }
                    dtrSurveyVo.setMODEM3(modemthree_available);
                    if ("YES".equalsIgnoreCase(modemthree_available)) {
                        dtrSurveyVo.setMODEM3_NUM(edt_modemthree_serial_no.getText().toString());
                        dtrSurveyVo.setMODEM3_MAKE(edt_modemthree_make.getText().toString());
                    }
                    dtrSurveyVo.setDTR_HGFUSE(dtrgh_fuseset);
                    if ("YES".equalsIgnoreCase(dtrgh_fuseset)) {
                        dtrSurveyVo.setBUSBAR_TYPE(busbar_type);
                        if ("Conductor".equalsIgnoreCase(busbar_type)) {
                            dtrSurveyVo.setCONDUCTOR_SIZE(consuctorsize);
                        }
                        dtrSurveyVo.setHGFUSE_DTR(hgfuseto_dtr);
                        dtrSurveyVo.setHGFUSE_DTR_CONDUCTOR_SIZE(hgfuseto_dtr_consuctorsize);
                    }



                    dtrSurveyVo.setLT_BUSBAR(lt_busbar);
                    if ("Cable".equalsIgnoreCase(lt_busbar)) {
                        dtrSurveyVo.setLT_BUSBAR_CABLESIZE(lt_busbarcable_size);
                    } else if ("Conductor".equalsIgnoreCase(lt_busbar)) {
                        dtrSurveyVo.setLT_BUSBAR_CONDUTORSIZE(lt_busbarconductor_size);
                    }

                    dtrSurveyVo.setDTR_POLE(dtrpole);
                    if ("PIN".equalsIgnoreCase(dtrpole)) {
                        dtrSurveyVo.setPIN(pin);
                        dtrSurveyVo.setNOOF_PININSULATORS(noof_pininsulators);
                    }
                    if ("CUT".equalsIgnoreCase(dtrpole)) {
                        dtrSurveyVo.setCUT(cut);
                        dtrSurveyVo.setNOOF_PININSULATORS(edt_noof_cpin_insulators.getText().toString().trim());
                        dtrSurveyVo.setNOOF_DISCINSULATORS(edt_noof_disc_insulators.getText().toString().trim());
                    }

                    dtrSurveyVo.setREMARKS(edt_landmark.getText().toString());
                    dtrSurveyVo.setLT_FUSESET(lt_fuseset);
                    dtrSurveyVo.setLAT(x);
                    dtrSurveyVo.setLAN(y);
                    dtrSurveyVo.setDTR_LOCATION("");
                    dtrSurveyVo.setNOOF_LTCIRCUITS(noof_ltcircuits);
                    arcGISDatabase.addDTRSurveyData(dtrSurveyVo);
                    Toast.makeText(c, "Pole location captured successfully..", Toast.LENGTH_SHORT).show();
                    dismiss();
                    callout.dismiss();
                    graphicsOverlay.getGraphics().remove(graphic);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

