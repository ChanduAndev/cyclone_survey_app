package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.popup;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.ArcGISDatabaseUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.activity.FeederMasterActivityUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.interfacess.ImpDataUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.retrofitservice.RetrofitServiceUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.utilits.CommanUtilitsUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.IncrementalFeederDataVoUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.IncrementalSurveyVoUG33KV;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InCrementalCustomAllDialogsUG33KV extends Dialog implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private final String x, y;
    public Activity c;
    private Button btn_add, btn_clear,btn_topclear;
    private EditText ed_pole_no,ed_frompole;
    private TextInputEditText edt_incroh_htscnum,edt_polepaintingcode,edt_remarks_overhead,edt_remarks_underground,edt_tappinglocation,edt_ugtappinglocation,edt_ugpolepaintingcode;
    public ImpDataUG33KV deligate;
    private RetrofitServiceUG33KV retrofitServiceUG33KV;
    private ProgressDialog progressDialog;
    Callout callout;
    GraphicsOverlay graphicsOverlay;
    GraphicsOverlay currentgraphicsOverlay;
    Graphic graphic;
    Graphic currentGraphic;
    LocationDisplay locationDisplay;
    private Spinner sp_oh_wosurveycompleted,sp_ug_wosurveycompleted,sp_ugssname,sp_ugnooftappings,sp_ugtapingpoint,sp_ugpolesupport,sp_ugpoleheight,sp_ugpoletype,sp__underpolestructure,sp_undergisendpole,sp_nooftappings,sp_rmu,sp_straight_rmu,sp_ssname,sp_diffvoltage,sp__polestructure,sp_noofcircuit_underg,sp_cablesize,sp_lineType,sp_poletype,sp_poleheight,sp_noofcircuit,sp_polesupport,sp_tapingpoint,sp_conductorsize,sp_pinpoint,sp_poleconfiguration,sp_ismulticircute,sp_circutetype,sp_isendpole;
    private String oh_wosurveycompleted,ug_wosurveycompleted,ugssname="",ugnooftappings="",ugtapingpoint="",ugpolesupport="",ugpoleheight="",ugpoletype="",underpolestructure="",undergisendpole="",ssName="",nooftappings,rmu,straight_rmu,ssId="",ssname,diffvoltage,polestructure,noofcircuit_underg,cablesize,noofcircuit,lineType,endPole,isendpole,surveyType,poleType,poleHeight,noofPoles,feederName,poleSupport,tapingPoint,taping_Point,conductorsize,pinpoint,poleconfiguration,feederCode,tappingPoint,ismulticircute,circutetype,workorder;
    private ArcGISDatabaseUG33KV arcGISDatabase;
    private List<IncrementalSurveyVoUG33KV> surveyVosList;
    private int maxSurveyCount,poleCountCheck;
    private LinearLayout lyt_distance,lyt_circuit,lyt_overhead,lyt_underground,lyt_ssname,lyt_polestructure,lyt_diffvoltage,lyt_rmu,lyt_tapping,lyt_underpolestructure,lyt_ugpole,lyt_ugtapping,lyt_ugssname;
    private TextView tv_sunname,tv_feedename,tv_distance;
    private Map mapData;
    private int copyFlag=0;
    private List<IncrementalFeederDataVoUG33KV> incrementalFeederDataVoUG33KVList =null;
    private double distance;
    private LinearLayout lyt_incroh_htscnum;
    public InCrementalCustomAllDialogsUG33KV(Activity a, String x, String y, Callout callout, GraphicsOverlay graphicsOverlay, Graphic graphic, String feederName, String feederCode, int maxSurveyCount, String tappingPoint, String surveyType, GraphicsOverlay currentgraphicsOverlay, Graphic currentGraphic, String ssName, String workorder, double distance) {
        super(a);
        this.c = a;
        this.x = x;
        this.y = y;
        this.callout=callout;
        this.graphicsOverlay=graphicsOverlay;
        this.currentgraphicsOverlay=currentgraphicsOverlay;
        this.graphic=graphic;
        this.currentGraphic=currentGraphic;
        this.feederName=feederName;
        this.feederCode=feederCode;
        this.maxSurveyCount=maxSurveyCount;
        this.tappingPoint=tappingPoint;
        this.surveyType=surveyType;
        this.ssName=ssName;
        this.workorder=workorder;
        this.distance=distance;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.incrementalcustome_dialog_ugc_33kv);
        try {
            setCancelable(false);
            progressDialog = CommanUtilitsUG33KV.progressDialog(c);
            btn_clear = (Button) findViewById(R.id.btn_clear);
            btn_add = (Button) findViewById(R.id.btn_add);
            btn_topclear = (Button) findViewById(R.id.btn_topclear);
            ed_pole_no = (EditText) findViewById(R.id.ed_pole_no);
            ed_frompole = (EditText) findViewById(R.id.ed_frompole);
            sp_poletype = (Spinner) findViewById(R.id.sp_poletype);
            sp_poleheight = (Spinner) findViewById(R.id.sp_poleheight);
            //sp_noof_circuits=(Spinner)findViewById(R.id.sp_noof_circuits);
            sp_polesupport = (Spinner) findViewById(R.id.sp_polesupport);
            sp_tapingpoint = (Spinner) findViewById(R.id.sp_tapingpoint);
            sp_conductorsize = (Spinner) findViewById(R.id.sp_conductorsize);
            sp_pinpoint = (Spinner) findViewById(R.id.sp_pinpoint);
            sp_ismulticircute = (Spinner) findViewById(R.id.sp_ismulticircute);
            sp_circutetype = (Spinner) findViewById(R.id.sp_circutetype);
            sp_poleconfiguration = (Spinner) findViewById(R.id.sp_poleconfiguration);
            edt_polepaintingcode = (TextInputEditText) findViewById(R.id.edt_polepaintingcode);
            edt_remarks_overhead = (TextInputEditText) findViewById(R.id.edt_remarks_overhead);
            edt_remarks_underground = (TextInputEditText) findViewById(R.id.edt_remarks_underground);
            edt_tappinglocation = (TextInputEditText) findViewById(R.id.edt_tappinglocation);
            edt_ugtappinglocation=(TextInputEditText) findViewById(R.id.edt_ugtappinglocation);
            edt_ugpolepaintingcode=(TextInputEditText) findViewById(R.id.edt_ugpolepaintingcode);
            sp_isendpole = (Spinner) findViewById(R.id.sp_isendpole);
            lyt_circuit = (LinearLayout) findViewById(R.id.lyt_circuit);
            lyt_overhead = (LinearLayout) findViewById(R.id.lyt_overhead);
            lyt_underground = (LinearLayout) findViewById(R.id.lyt_underground);
            lyt_ssname = (LinearLayout) findViewById(R.id.lyt_ssname);
            lyt_polestructure = (LinearLayout)findViewById(R.id.lyt_polestructure);
            lyt_diffvoltage = (LinearLayout)findViewById(R.id.lyt_diffvoltage);
            lyt_rmu = (LinearLayout)findViewById(R.id.lyt_rmu);
            lyt_tapping = (LinearLayout)findViewById(R.id.lyt_tapping);
            lyt_underpolestructure=(LinearLayout)findViewById(R.id.lyt_underpolestructure);
            lyt_ugpole=(LinearLayout)findViewById(R.id.lyt_ugpole);
            lyt_ugtapping=(LinearLayout)findViewById(R.id.lyt_ugtapping);
            lyt_ugssname=(LinearLayout)findViewById(R.id.lyt_ugssname);
            lyt_distance=findViewById(R.id.lyt_distance);
            lyt_distance.setVisibility(View.GONE);
            sp_lineType = (Spinner)findViewById(R.id.sp_lineType);
            sp_noofcircuit = (Spinner)findViewById(R.id.sp_noofcircuit);
            sp_cablesize = (Spinner)findViewById(R.id.sp_cablesize);
            sp_noofcircuit_underg =(Spinner)findViewById(R.id.sp_noofcircuit_underg);
            //edt_ssname=(TextInputEditText)findViewById(R.id.edt_ssname);
            sp__polestructure = (Spinner)findViewById(R.id.sp__polestructure);
            sp_diffvoltage = (Spinner)findViewById(R.id.sp_diffvoltage);
            sp_ssname = (Spinner)findViewById(R.id.sp_ssname);
            sp_straight_rmu =(Spinner)findViewById(R.id.sp_straight_rmu);
            sp_rmu = (Spinner)findViewById(R.id.sp_rmu);
            sp_nooftappings=(Spinner)findViewById(R.id.sp_nooftappings);
            sp_undergisendpole=(Spinner)findViewById(R.id.sp_undergisendpole);
            sp__underpolestructure=(Spinner)findViewById(R.id.sp__underpolestructure);
            sp_ugpoletype=(Spinner)findViewById(R.id.sp_ugpoletype);
            sp_ugpoleheight=(Spinner)findViewById(R.id.sp_ugpoleheight);
            sp_ugpolesupport=(Spinner)findViewById(R.id.sp_ugpolesupport);
            sp_ugtapingpoint=(Spinner)findViewById(R.id.sp_ugtapingpoint);
            sp_ugnooftappings=(Spinner)findViewById(R.id.sp_ugnooftappings);
            sp_ug_wosurveycompleted=(Spinner)findViewById(R.id.sp_ugnooftappings);
            sp_oh_wosurveycompleted=(Spinner)findViewById(R.id.sp_oh_wosurveycompleted);
            sp_ugssname=(Spinner)findViewById(R.id.sp_ugssname);
            tv_sunname=(TextView)findViewById(R.id.tv_sunname);
            tv_feedename=(TextView)findViewById(R.id.tv_feedename);
            tv_distance=findViewById(R.id.tv_distance);
            lyt_incroh_htscnum=findViewById(R.id.lyt_incroh_htscnum);
            edt_incroh_htscnum=findViewById(R.id.edt_incroh_htscnum);
            ed_pole_no.setEnabled(false);
            ed_frompole.setEnabled(false);

            btn_clear.setOnClickListener(this);
            btn_add.setOnClickListener(this);
            btn_topclear.setOnClickListener(this);

            sp_poletype.setOnItemSelectedListener(this);
            sp_poleheight.setOnItemSelectedListener(this);
//        sp_noof_circuits.setOnItemSelectedListener(this);
            sp_polesupport.setOnItemSelectedListener(this);
            sp_tapingpoint.setOnItemSelectedListener(this);
            sp_conductorsize.setOnItemSelectedListener(this);
            sp_pinpoint.setOnItemSelectedListener(this);
            sp_poleconfiguration.setOnItemSelectedListener(this);
            sp_ismulticircute.setOnItemSelectedListener(this);
            sp_circutetype.setOnItemSelectedListener(this);
            sp_isendpole.setOnItemSelectedListener(this);
            sp_lineType.setOnItemSelectedListener(this);
            sp_noofcircuit.setOnItemSelectedListener(this);
            sp_cablesize.setOnItemSelectedListener(this);
            sp_noofcircuit_underg.setOnItemSelectedListener(this);
            sp__polestructure.setOnItemSelectedListener(this);
            sp_diffvoltage.setOnItemSelectedListener(this);
            sp_ssname.setOnItemSelectedListener(this);
            sp_straight_rmu.setOnItemSelectedListener(this);
            sp_rmu.setOnItemSelectedListener(this);
            sp_nooftappings.setOnItemSelectedListener(this);
            sp_undergisendpole.setOnItemSelectedListener(this);
            sp__underpolestructure.setOnItemSelectedListener(this);
            sp_ugpoletype.setOnItemSelectedListener(this);
            sp_ugpoleheight.setOnItemSelectedListener(this);
            sp_ugpolesupport.setOnItemSelectedListener(this);
            sp_ugtapingpoint.setOnItemSelectedListener(this);
            sp_ugnooftappings.setOnItemSelectedListener(this);
            sp_ugssname.setOnItemSelectedListener(this);
            sp_ug_wosurveycompleted.setOnItemSelectedListener(this);
            sp_oh_wosurveycompleted.setOnItemSelectedListener(this);

            incrementalFeederDataVoUG33KVList =new ArrayList<>();
            arcGISDatabase = new ArcGISDatabaseUG33KV(c);
            surveyVosList = arcGISDatabase.getIncrementalSurveyData("");
            int count = arcGISDatabase.getIncrementalObjectId();
            List<IncrementalSurveyVoUG33KV> surveyVosListData = arcGISDatabase.getIncrementalSurveyData(String.format("SELECT * FROM  INCREMENTAL_ARCGIS_TABLE WHERE POLE_NO=%s",count));

            poleCountCheck = arcGISDatabase.getIncrementalObjectIdCount();
            incrementalFeederDataVoUG33KVList =arcGISDatabase.getIncrementalFeerData();
            if (poleCountCheck == 0) {
                //  if(""surveyType)
                int polenum = maxSurveyCount+1;

                if(surveyVosList.size()==1){
                    List<String> poleNumber = arcGISDatabase.getIncrementalFeederComplaint(String.format("SELECT DISTINCT FROM_POLE FROM INCRFEMENTAL_EHTMASTER_TABLE WHERE WORKORDER_ID='%s'", workorder));
                    ed_frompole.setText(poleNumber.get(0));
                }else if(surveyVosList.size()>1){
                    ed_frompole.setText(String.valueOf(count));
                }
                ed_pole_no.setText(String.valueOf(polenum));
            } else {
                ed_frompole.setText(String.valueOf(count));
                ed_pole_no.setText(String.valueOf(count+1));
            }
            lyt_underground.setVisibility(View.GONE);
            lyt_ssname.setVisibility(View.GONE);
            lyt_polestructure.setVisibility(View.GONE);
            lyt_diffvoltage.setVisibility(View.GONE);
            lyt_rmu.setVisibility(View.GONE);
            lyt_tapping.setVisibility(View.GONE);
            lyt_underpolestructure.setVisibility(View.GONE);
            lyt_ugpole.setVisibility(View.GONE);
            lyt_ugtapping.setVisibility(View.GONE);
            lyt_ugssname.setVisibility(View.GONE);
            lyt_incroh_htscnum.setVisibility(View.GONE);
            tv_sunname.setText(ssName);
            tv_feedename.setText(feederName);
            mapData=new HashMap();
            mapData.put("POLE_TYPE","TESTING");
           /* if(copyFlag==1){
                List<String> str=new ArrayList<>();
                String strtype=(String)mapData.get("POLE_TYPE");
                str.add(strtype);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, str);
                sp_poletype.setAdapter(adapter);
            }*/

            if(count>0){
                String idtance_Poles=String.valueOf(Math.round(distance));
                tv_distance.setText(idtance_Poles+"  Mtrs");

                lyt_distance.setVisibility(View.GONE);
            }else {
                lyt_distance.setVisibility(View.GONE);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.btn_clear:
                    deligate.IsSetData("add Record Not Sucess", "1");
                    callout.dismiss();
                    graphicsOverlay.getGraphics().remove(graphic);
                    dismiss();
                    break;
                case R.id.btn_topclear:
                    callout.dismiss();
                    graphicsOverlay.getGraphics().remove(graphic);
                    dismiss();
                    break;
                case R.id.btn_add:
                    currentgraphicsOverlay.getGraphics().remove(currentGraphic);

                    if (ed_pole_no.getText().toString().isEmpty()) {
                        Toast.makeText(c, "Enter Pole No", Toast.LENGTH_LONG).show();
                    } else if (ed_frompole.getText().toString().isEmpty()) {
                        Toast.makeText(c, "Enter From Pole ", Toast.LENGTH_LONG).show();
                    } else {
                        if ("Overhead".equalsIgnoreCase(lineType)) {
                            overHeadData();
                        } else if ("UnderGround".equalsIgnoreCase(lineType)) {
                            underGroundData();
                        }
                    }

                    break;
                default:
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        try {
            switch (adapterView.getId()) {
                case R.id.sp_poletype:
                    poleType = sp_poletype.getSelectedItem().toString();

                    if ("PSCC".equalsIgnoreCase(poleType)) {
                        // String test= (String) mapData.get("POLE_TYPE");
                        List<String> psccList = new ArrayList<>();
                        psccList.add("Select");
                        psccList.add("9.1");
                        psccList.add("11");
                        // psccList.add(test);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, psccList);
                        sp_poleheight.setAdapter(adapter);

                    } else if ("SPUN".equalsIgnoreCase(poleType)) {
                        List<String> spunList = new ArrayList<>();
                        spunList.add("Select");
                        spunList.add("9.5");
                        spunList.add("11");
                        spunList.add("12.5");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, spunList);
                        sp_poleheight.setAdapter(adapter);

                    } else if ("RAIL".equalsIgnoreCase(poleType)) {
                        List<String> railList = new ArrayList<>();
                        railList.add("Select");
                        railList.add("9.5");
                        railList.add("11");
                        railList.add("12.5");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, railList);
                        sp_poleheight.setAdapter(adapter);

                    } else if ("RS JOIST".equalsIgnoreCase(poleType)) {
                        List<String> rssJoistList = new ArrayList<>();
                        rssJoistList.add("Select");
                        /*rssJoistList.add("8");*/
                        rssJoistList.add("9.1");
                        rssJoistList.add("11");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, rssJoistList);
                        sp_poleheight.setAdapter(adapter);
                    } else if ("TOWER".equalsIgnoreCase(poleType)) {
                        List<String> towertList = new ArrayList<>();
                        towertList.add("Select");
                        towertList.add("M+3");
                        towertList.add("M+6");
                        towertList.add("M+9");
                        towertList.add("M+12");
                        towertList.add("R+6");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, towertList);
                        sp_poleheight.setAdapter(adapter);
                    } else if ("Tubular Pole".equalsIgnoreCase(poleType)) {

                        List<String> tubularList = new ArrayList<>();
                        tubularList.add("Select");
                        tubularList.add("9.1");
                        tubularList.add("11");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, tubularList);
                        sp_poleheight.setAdapter(adapter);
                    }

                    break;
                case R.id.sp_poleheight:
                    poleHeight = sp_poleheight.getSelectedItem().toString();
                    break;
                case R.id.sp_polesupport:
                    poleSupport = sp_polesupport.getSelectedItem().toString();
                    break;
                case R.id.sp_tapingpoint:
                    taping_Point = sp_tapingpoint.getSelectedItem().toString();
                    if ("Yes".equalsIgnoreCase(taping_Point)) {
                        lyt_tapping.setVisibility(View.VISIBLE);
                    } else {
                        lyt_tapping.setVisibility(View.GONE);
                        nooftappings = "";
                    }
                    break;
                case R.id.sp_conductorsize:
                    conductorsize = sp_conductorsize.getSelectedItem().toString();
                    break;
                case R.id.sp_pinpoint:
                    pinpoint = sp_pinpoint.getSelectedItem().toString();
                    if ("Cutpoint".equalsIgnoreCase(pinpoint)) {
                        lyt_polestructure.setVisibility(View.VISIBLE);
                    } else {
                        lyt_polestructure.setVisibility(View.GONE);
                        polestructure = "";
                        sp__polestructure.setSelection(0);
                    }
                    break;

                case R.id.sp_poleconfiguration:
                    poleconfiguration = sp_poleconfiguration.getSelectedItem().toString();
                    if ("SS-Bay".equalsIgnoreCase(poleconfiguration)) {
                        lyt_ssname.setVisibility(View.VISIBLE);
                        lyt_incroh_htscnum.setVisibility(View.GONE);
                        //List<String> sunStationName=arcGISDatabase.getFeederId(String.format("SELECT DISTINCT EHT_SS_NAME FROM SSMASTER_TABLE WHERE FEEDER_CODE_33KV='%s'",feederCode));
                        //List<String> sunStationName = arcGISDatabase.getSSNAMEList();
                        List<String> sunStationName = arcGISDatabase.getIncrementalSSNAMEList();
                        sunStationName.add("Select");
                        Collections.reverse(sunStationName);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, sunStationName);
                        sp_ssname.setAdapter(adapter);
                        edt_incroh_htscnum.setText("");

                    }else if("HT Service-Pole".equalsIgnoreCase(poleconfiguration)) {
                        lyt_ssname.setVisibility(View.GONE);
                        lyt_incroh_htscnum.setVisibility(View.VISIBLE);
                        ssname = "";
                    }

                    else {
                        lyt_ssname.setVisibility(View.GONE);
                        lyt_incroh_htscnum.setVisibility(View.GONE);

                        ssname = "";
                        edt_incroh_htscnum.setText("");
                    }

                    break;
                case R.id.sp_ssname:
                    ssname = sp_ssname.getSelectedItem().toString();
                    if (!"Select".equalsIgnoreCase(ssname) && !ssname.isEmpty()) {
                        //ssId = arcGISDatabase.getFeederId(String.format("SELECT EHT_SS_CODE FROM SSMASTER_TABLE WHERE EHT_SS_NAME='%s'", ssname));
                        ssId = arcGISDatabase.getFeederId(String.format("SELECT EHT_SS_CODE FROM INCREMENTAL_SSMASTER_TABLE WHERE EHT_SS_NAME='%s'", ssname));

                    }
                    break;

                case R.id.sp__polestructure:
                    polestructure = sp__polestructure.getSelectedItem().toString();
                    break;
                case R.id.sp_ismulticircute:
                    ismulticircute = sp_ismulticircute.getSelectedItem().toString();
                    if ("Yes".equalsIgnoreCase(ismulticircute)) {
                        lyt_circuit.setVisibility(View.VISIBLE);
                        lyt_diffvoltage.setVisibility(View.VISIBLE);
                        sp_circutetype.setSelection(0);
                        sp_noofcircuit.setSelection(0);

                    } else {
                        sp_circutetype.setSelection(0);
                        lyt_circuit.setVisibility(View.GONE);
                        lyt_diffvoltage.setVisibility(View.GONE);
                        sp_noofcircuit.setSelection(0);
                    }
                    break;
                case R.id.sp_noofcircuit:
                    noofcircuit = sp_noofcircuit.getSelectedItem().toString();
                    if ("2".equalsIgnoreCase(noofcircuit)) {
                        List<String> twoCircuits = new ArrayList<>();
                        twoCircuits.add("Select");
                        twoCircuits.add("11kv");
                        twoCircuits.add("LT");
                        twoCircuits.add("6.3kv");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, twoCircuits);
                        sp_diffvoltage.setAdapter(adapter);
                    } else if ("3".equalsIgnoreCase(noofcircuit)) {
                        List<String> threeCircuits = new ArrayList<>();
                        threeCircuits.add("Select");
                        threeCircuits.add("(33 kv-1)+(11 kv-1)");
                        threeCircuits.add("11 kv-2");
                        threeCircuits.add("(11 kv-1)+(LT-1)");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, threeCircuits);
                        sp_diffvoltage.setAdapter(adapter);
                    } else if ("4".equalsIgnoreCase(noofcircuit)) {
                        List<String> fourCircuits = new ArrayList<>();
                        fourCircuits.add("Select");
                        fourCircuits.add("(33 kv-2) +( 11kv-1)");
                        fourCircuits.add("(33 kV- 1)+(11kv-2)");
                        fourCircuits.add("(33 kv-1)+(11 kv-1)+(LT-1)");
                        fourCircuits.add("11 kv-3");
                        fourCircuits.add("(11 kv-2)+(LT-1)");
                        fourCircuits.add("(33 kv-2)+(LT-1)");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, fourCircuits);
                        sp_diffvoltage.setAdapter(adapter);
                    }else if ("5".equalsIgnoreCase(noofcircuit)) {
                        List<String> fiveCircuits= new ArrayList<>();
                        fiveCircuits.add("Select");
                        fiveCircuits.add("(33 kv-2)+(11 kv-1)+(LT-1)");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, fiveCircuits);
                        sp_diffvoltage.setAdapter(adapter);
                    }

                    break;
                case R.id.sp_circutetype:
                    circutetype = sp_circutetype.getSelectedItem().toString();
                    if ("Multi Voltage".equalsIgnoreCase(circutetype)) {
                        lyt_diffvoltage.setVisibility(View.VISIBLE);
                        sp_diffvoltage.setSelection(0);
                    } else {
                        lyt_diffvoltage.setVisibility(View.GONE);
                        sp_diffvoltage.setSelection(0);
                        diffvoltage="";
                    }
                    break;
                case R.id.sp_diffvoltage:
                    diffvoltage=sp_diffvoltage.getSelectedItem().toString();

                    break;
                case R.id.sp_isendpole:
                    isendpole = sp_isendpole.getSelectedItem().toString();
                    if ("No".equalsIgnoreCase(isendpole)) {
                        endPole = "N";
                    } else {
                        endPole = "Y";
                    }
                    break;

                case R.id.sp_lineType:
                    lineType = sp_lineType.getSelectedItem().toString();

                    if ("Overhead".equalsIgnoreCase(lineType)) {
                        lyt_overhead.setVisibility(View.VISIBLE);
                        lyt_underground.setVisibility(View.GONE);
                    } else if ("UnderGround".equalsIgnoreCase(lineType)) {
                        lyt_underground.setVisibility(View.VISIBLE);
                        lyt_overhead.setVisibility(View.GONE);
                    }
                    break;

                case R.id.sp_cablesize:
                    cablesize = sp_cablesize.getSelectedItem().toString();

                    break;
                case R.id.sp_noofcircuit_underg:
                    noofcircuit_underg = sp_noofcircuit_underg.getSelectedItem().toString();

                    break;
                case R.id.sp_straight_rmu:
                    straight_rmu = sp_straight_rmu.getSelectedItem().toString();
                    if ("RMU".equalsIgnoreCase(straight_rmu)) {
                        lyt_rmu.setVisibility(View.VISIBLE);
                        lyt_underpolestructure.setVisibility(View.GONE);
                        lyt_ugpole.setVisibility(View.GONE);
                        sp_rmu.setSelection(0);
                        sp__underpolestructure.setSelection(0);
                    }else if("Structure".equalsIgnoreCase(straight_rmu)){
                        lyt_underpolestructure.setVisibility(View.VISIBLE);
                        lyt_ugpole.setVisibility(View.GONE);
                        lyt_rmu.setVisibility(View.GONE);
                        sp_rmu.setSelection(0);
                        sp__underpolestructure.setSelection(0);
                    }else if("Pole".equalsIgnoreCase(straight_rmu)) {
                        sp_rmu.setSelection(0);
                        sp__underpolestructure.setSelection(0);
                        lyt_ugpole.setVisibility(View.VISIBLE);
                        lyt_rmu.setVisibility(View.GONE);
                        lyt_underpolestructure.setVisibility(View.GONE);
                    }
                    else {
                        sp_rmu.setSelection(0);
                        sp__underpolestructure.setSelection(0);
                        lyt_rmu.setVisibility(View.GONE);
                        lyt_underpolestructure.setVisibility(View.GONE);
                        lyt_ugpole.setVisibility(View.GONE);
                        rmu = "";
                        //nooftappings="";
                    }
                    break;

                case R.id.sp_rmu:
                    rmu = sp_rmu.getSelectedItem().toString();

                    break;
                case R.id.sp__underpolestructure:
                    underpolestructure = sp__underpolestructure.getSelectedItem().toString();

                    break;
                case R.id.sp_ugpoletype:
                    ugpoletype = sp_ugpoletype.getSelectedItem().toString();
                    if ("PSCC".equalsIgnoreCase(ugpoletype)) {
                        lyt_ugssname.setVisibility(View.GONE);
                        List<String> ugpsccList = new ArrayList<>();
                        ugpsccList.add("Select");
                        // psccList.add("8");
                        ugpsccList.add("9.1");
                        ugpsccList.add("11");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, ugpsccList);
                        sp_ugpoleheight.setAdapter(adapter);

                    } else if ("SPUN".equalsIgnoreCase(ugpoletype)) {
                        lyt_ugssname.setVisibility(View.GONE);
                        List<String> ugspunList = new ArrayList<>();
                        ugspunList.add("Select");
                        ugspunList.add("9.5");
                        ugspunList.add("11");
                        ugspunList.add("12.5");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, ugspunList);
                        sp_ugpoleheight.setAdapter(adapter);

                    } else if ("RAIL".equalsIgnoreCase(ugpoletype)) {
                        lyt_ugssname.setVisibility(View.GONE);
                        List<String> ugrailList = new ArrayList<>();
                        ugrailList.add("Select");
                        ugrailList.add("9.5");
                        ugrailList.add("11");
                        ugrailList.add("12.5");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, ugrailList);
                        sp_ugpoleheight.setAdapter(adapter);

                    } else if ("RS JOIST".equalsIgnoreCase(ugpoletype)) {
                        lyt_ugssname.setVisibility(View.GONE);
                        List<String> ugrssJoistList = new ArrayList<>();
                        ugrssJoistList.add("Select");
                        /*rssJoistList.add("8");*/
                        ugrssJoistList.add("9.1");
                        ugrssJoistList.add("11");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, ugrssJoistList);
                        sp_ugpoleheight.setAdapter(adapter);
                    } else if("TOWER".equalsIgnoreCase(ugpoletype)) {
                        lyt_ugssname.setVisibility(View.GONE);
                        List<String> ugtowertList = new ArrayList<>();
                        ugtowertList.add("Select");
                        ugtowertList.add("M+3");
                        ugtowertList.add("M+6");
                        ugtowertList.add("M+9");
                        ugtowertList.add("M+12");
                        ugtowertList.add("R+6");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, ugtowertList);
                        sp_ugpoleheight.setAdapter(adapter);
                    } else if ("Tubular Pole".equalsIgnoreCase(ugpoletype)) {
                        lyt_ugssname.setVisibility(View.GONE);
                        List<String> ugtubularList = new ArrayList<>();
                        ugtubularList.add("Select");
                        ugtubularList.add("9.1");
                        ugtubularList.add("11");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item,ugtubularList);
                        sp_ugpoleheight.setAdapter(adapter);
                    }else if("SS Bay".equalsIgnoreCase(ugpoletype)){
                        lyt_ugssname.setVisibility(View.VISIBLE);
                        List<String> ugSSName = arcGISDatabase.getSSNAMEList();
                        ugSSName.add("Select");
                        Collections.reverse(ugSSName);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, ugSSName);
                        sp_ugssname.setAdapter(adapter);
                    }

                    break;

                case R.id.sp_ugpoleheight:
                    ugpoleheight = sp_ugpoleheight.getSelectedItem().toString();

                    break;

                case R.id.sp_ugpolesupport:
                    ugpolesupport=sp_ugpolesupport.getSelectedItem().toString();

                    break;
                case R.id.sp_ugssname:
                    ugssname=sp_ugssname.getSelectedItem().toString();

                    break;

                case R.id.sp_ugtapingpoint:
                    ugtapingpoint=sp_ugtapingpoint.getSelectedItem().toString();
                    if ("Yes".equalsIgnoreCase(ugtapingpoint)) {
                        lyt_ugtapping.setVisibility(View.VISIBLE);
                    } else {
                        lyt_ugtapping.setVisibility(View.GONE);
                        ugnooftappings = "";
                    }

                    break;
                case R.id.sp_ugnooftappings:
                    ugnooftappings=sp_ugnooftappings.getSelectedItem().toString();

                    break;


                case R.id.sp_nooftappings:
                    nooftappings =sp_nooftappings.getSelectedItem().toString();
                    break;

                case R.id.sp_undergisendpole:
                    if ("No".equalsIgnoreCase(sp_undergisendpole.getSelectedItem().toString())) {
                        undergisendpole = "N";
                    } else {
                        undergisendpole = "Y";
                    }
                    break;

                case R.id.sp_ug_wosurveycompleted:
                    ug_wosurveycompleted =sp_ug_wosurveycompleted.getSelectedItem().toString();
                    break;

                case R.id.sp_oh_wosurveycompleted:
                    oh_wosurveycompleted =sp_oh_wosurveycompleted.getSelectedItem().toString();
                    break;





                default:

                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void overHeadData(){
        try {
            if ("Select".equalsIgnoreCase(poleType)) {
                Toast.makeText(c, "Select Pole Type", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(poleHeight)) {
                Toast.makeText(c, "Select Pole Height", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(taping_Point)) {
                Toast.makeText(c, "Select Taping Point", Toast.LENGTH_LONG).show();
            } else if ("Yes".equalsIgnoreCase(taping_Point) && "Select".equalsIgnoreCase(sp_nooftappings.getSelectedItem().toString())) {
                Toast.makeText(c, "Select No.Of Tappings", Toast.LENGTH_LONG).show();
            } else if ("Yes".equalsIgnoreCase(taping_Point) && edt_tappinglocation.getText().toString().isEmpty()) {
                Toast.makeText(c, "Enter Tapping Location Name", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(conductorsize)) {
                Toast.makeText(c, "Select ConductorSize", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(poleconfiguration)) {
                Toast.makeText(c, "Select PoleConfiguration", Toast.LENGTH_LONG).show();
            } else if ("SS-Bay".equalsIgnoreCase(poleconfiguration) && "Select".equalsIgnoreCase(sp_ssname.getSelectedItem().toString())) {
                Toast.makeText(c, "Select SS Name", Toast.LENGTH_LONG).show();
            }else if ("HT Service-Pole".equalsIgnoreCase(poleconfiguration) && edt_incroh_htscnum.getText().toString().isEmpty()) {
                Toast.makeText(c, "Enter HT ServiceNumber", Toast.LENGTH_LONG).show();
            } else if ("Yes".equalsIgnoreCase(ismulticircute) && "Select".equalsIgnoreCase(noofcircuit)) {
                Toast.makeText(c, "Select No.Of Circuits", Toast.LENGTH_LONG).show();
            } else if ("Yes".equalsIgnoreCase(ismulticircute) && "Select".equalsIgnoreCase(sp_circutetype.getSelectedItem().toString())) {
                Toast.makeText(c, "Select Circuit Type", Toast.LENGTH_LONG).show();
            } else if ("Yes".equalsIgnoreCase(ismulticircute) && "Multi Voltage".equalsIgnoreCase(circutetype) && "Select".equalsIgnoreCase(sp_diffvoltage.getSelectedItem().toString())) {
                Toast.makeText(c, "Select Diff Voltage", Toast.LENGTH_LONG).show();
            } else {
                IncrementalSurveyVoUG33KV surveyVo = new IncrementalSurveyVoUG33KV();
                if (feederCode != null & !feederCode.isEmpty() && !ed_pole_no.getText().toString().isEmpty() && !ed_frompole.getText().toString().isEmpty() && !x.isEmpty() && !y.isEmpty()) {

                    if (Integer.parseInt(ed_pole_no.getText().toString()) == Integer.parseInt(ed_frompole.getText().toString())) {
                        Toast.makeText(c, "Pole No and From Pole No Should Not Be Same Contact Admin", Toast.LENGTH_LONG).show();

                    } else {
                        List<String>  workOrderStatus = arcGISDatabase.getIncrementalFeederComplaint(String.format("SELECT DISTINCT STATUS FROM INCRFEMENTAL_EHTMASTER_TABLE WHERE WORKORDER_ID='%s'", workorder));

                        List<String>  isFeederType = arcGISDatabase.getIncrementalFeederComplaint(String.format("SELECT DISTINCT IS_FEEDER_TYPE FROM INCRFEMENTAL_EHTMASTER_TABLE WHERE FEEDER_CODE_33KV='%s'", feederCode));
                        // copyFlag=1;
                        surveyVo.setFeeder_id(feederCode);
                        surveyVo.setPole_no(Integer.parseInt(ed_pole_no.getText().toString()));
                        surveyVo.setFrom_pole(Integer.parseInt(ed_frompole.getText().toString()));
                        surveyVo.setLineType(lineType);
                        surveyVo.setNoofCircuits(noofcircuit);
                        surveyVo.setPole_type(poleType);
                        surveyVo.setPole_height(poleHeight);
                        //surveyVo.setNoof_circuits(noofCircuits);
                        surveyVo.setPole_support(poleSupport);
                        surveyVo.setTapping_point(taping_Point);
                        surveyVo.setConductor_size(conductorsize);
                        surveyVo.setPinpoint(pinpoint);
                        surveyVo.setPole_configuration(poleconfiguration);
                        if(!edt_polepaintingcode.getText().toString().isEmpty()) {
                            surveyVo.setPole_paintingcode(edt_polepaintingcode.getText().toString());
                        }
                        surveyVo.setLat(x);
                        surveyVo.setLan(y);
                        surveyVo.setPastSurveyFlag("N");
                        if ("Yes".equalsIgnoreCase(ismulticircute)) {
                            surveyVo.setIsMultiCircuit(ismulticircute);
                        } else if ("No".equalsIgnoreCase(ismulticircute)) {
                            surveyVo.setNoofCircuits("1");
                        }
                        if (!"Select".equalsIgnoreCase(sp_circutetype.getSelectedItem().toString())) {
                            surveyVo.setCircuitType(circutetype);
                        } else {
                            surveyVo.setCircuitType("");
                        }
                        if (!"Select".equalsIgnoreCase(diffvoltage)) {
                            surveyVo.setDiffVoltage(diffvoltage);
                        } else {
                            surveyVo.setDiffVoltage("");
                        }
                        surveyVo.setIsEndPole(endPole);
                        if(!"Select".equalsIgnoreCase(ssname)) {
                            surveyVo.setSs_name(ssname);
                        }else {
                            surveyVo.setSs_name("");
                        }

                        if(!polestructure.isEmpty()) {
                            surveyVo.setPoleStructure(polestructure);
                        }
                        //surveyVo.setDiffVoltage(diffvoltage);
                        surveyVo.setSsId(ssId);
                        surveyVo.setNoofTappings(nooftappings);
                        if(!edt_tappinglocation.getText().toString().isEmpty()) {
                            surveyVo.setTappingLocation(edt_tappinglocation.getText().toString());
                        }
                        if(!edt_remarks_overhead.getText().toString().isEmpty()) {
                            surveyVo.setRemarks(edt_remarks_overhead.getText().toString());
                        }
                        surveyVo.setWorkOrder(workorder);
                        surveyVo.setWorkOrderStatus(workOrderStatus.get(0));
                        surveyVo.setIsFeederType(isFeederType.get(0));
                         if ("HT Service-Pole".equalsIgnoreCase(poleconfiguration)) {
                             surveyVo.setHtServiceNumber(edt_incroh_htscnum.getText().toString());
                        }
                        arcGISDatabase.addIncrementalSurveyData(surveyVo);

                        Toast.makeText(c, "Pole location captured successfully..", Toast.LENGTH_SHORT).show();
                        dismiss();
                        callout.dismiss();
                        graphicsOverlay.getGraphics().remove(graphic);

                        if ("YES".equalsIgnoreCase(oh_wosurveycompleted)) {
                            arcGISDatabase.upDate(String.format("UPDATE WO_STATUS_TABLE SET WO_STATUS='Y' WHERE WO_ID='%s'",workorder));
                        }
                        if ("YES".equalsIgnoreCase(isendpole)) {
                           // arcGISDatabase.upDate(String.format("UPDATE WO_STATUS_TABLE SET WO_STATUS='Y' WHERE WO_ID='%s'",workorder));
                            //UPDATE WO_STATUS_TABLE SET WO_STATUS='Y' WHERE WO_STATUS='N'
                            Intent intent = new Intent(c, FeederMasterActivityUG33KV.class);
                            c.startActivity(intent);
                            c.finish();
                        }



                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void underGroundData() {
        try {
            if ("Select".equalsIgnoreCase(sp_noofcircuit_underg.getSelectedItem().toString())) {
                Toast.makeText(c, "Select No.Of Cables", Toast.LENGTH_LONG).show();
            } /*else if ("Select".equalsIgnoreCase(straight_rmu)) {
                Toast.makeText(c, "Straight Through Joint/RMU", Toast.LENGTH_LONG).show();
            }*/ else if ("RMU".equalsIgnoreCase(straight_rmu) && "Select".equalsIgnoreCase(sp_rmu.getSelectedItem().toString())) {
                Toast.makeText(c, "Select RMU ", Toast.LENGTH_LONG).show();
            }else if ("Pole".equalsIgnoreCase(straight_rmu) && "Select".equalsIgnoreCase(sp_ugpoletype.getSelectedItem().toString())) {
                Toast.makeText(c, "Select Pole Type ", Toast.LENGTH_LONG).show();
            }else if ("Pole".equalsIgnoreCase(straight_rmu) && "Select".equalsIgnoreCase(sp_ugpoleheight.getSelectedItem().toString())) {
                Toast.makeText(c, "Select Pole Height ", Toast.LENGTH_LONG).show();
            }else if ("SS Bay".equalsIgnoreCase(ugpoletype) && "Select".equalsIgnoreCase(sp_ugssname.getSelectedItem().toString())) {
                Toast.makeText(c, "Select SS Name ", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(sp_ugtapingpoint.getSelectedItem().toString())) {
                Toast.makeText(c, "Select Tapping Point ", Toast.LENGTH_LONG).show();
            }else if ("Yes".equalsIgnoreCase(ugtapingpoint) && "Select".equalsIgnoreCase(sp_ugnooftappings.getSelectedItem().toString())) {
                Toast.makeText(c, "Select No.Of Tappings", Toast.LENGTH_LONG).show();
            }else if ("Yes".equalsIgnoreCase(ugtapingpoint) && edt_ugtappinglocation.getText().toString().isEmpty()) {
                Toast.makeText(c, "Enter Tapping Location Name", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(cablesize)) {
                Toast.makeText(c, "Select Cable Size ", Toast.LENGTH_LONG).show();
            } else {
                if (feederCode != null & !feederCode.isEmpty() && !ed_pole_no.getText().toString().isEmpty() && !ed_frompole.getText().toString().isEmpty() && !x.isEmpty() && !y.isEmpty()) {

                    if (Integer.parseInt(ed_pole_no.getText().toString()) == Integer.parseInt(ed_frompole.getText().toString())) {
                        Toast.makeText(c, "Pole No and From Pole No Should Not Be Same Contact Admin", Toast.LENGTH_LONG).show();

                    } else {
                        List<String>  workOrderStatus = arcGISDatabase.getIncrementalFeederComplaint(String.format("SELECT DISTINCT STATUS FROM INCRFEMENTAL_EHTMASTER_TABLE WHERE WORKORDER_ID='%s'",workorder));
                        List<String>  isFeederType = arcGISDatabase.getIncrementalFeederComplaint(String.format("SELECT DISTINCT IS_FEEDER_TYPE FROM INCRFEMENTAL_EHTMASTER_TABLE WHERE FEEDER_CODE_33KV='%s'", feederCode));
                        IncrementalSurveyVoUG33KV underGroundsurveyVo = new IncrementalSurveyVoUG33KV();
                        underGroundsurveyVo.setFeeder_id(feederCode);
                        underGroundsurveyVo.setPole_no(Integer.parseInt(ed_pole_no.getText().toString()));
                        underGroundsurveyVo.setFrom_pole(Integer.parseInt(ed_frompole.getText().toString()));
                        underGroundsurveyVo.setLineType(lineType);
                        underGroundsurveyVo.setNoofCircuits(noofcircuit_underg);
                        underGroundsurveyVo.setConductor_size(cablesize);
                        underGroundsurveyVo.setLat(x);
                        underGroundsurveyVo.setLan(y);
                        underGroundsurveyVo.setPastSurveyFlag("N");
                        underGroundsurveyVo.setStjoint_rmu(straight_rmu);
                        underGroundsurveyVo.setRmu_type(rmu);
                        underGroundsurveyVo.setIsEndPole(undergisendpole);
                        if(!edt_remarks_underground.getText().toString().isEmpty()) {
                            underGroundsurveyVo.setRemarks(edt_remarks_underground.getText().toString());
                        }
                        underGroundsurveyVo.setPoleStructure(underpolestructure);
                        if("Select".equalsIgnoreCase(ugpoletype)){
                            underGroundsurveyVo.setPole_type("");
                        }else {
                            underGroundsurveyVo.setPole_type(ugpoletype);
                        }
                        if("Select".equalsIgnoreCase(ugpoleheight)){
                            underGroundsurveyVo.setPole_height("");
                        }else {
                            underGroundsurveyVo.setPole_height(ugpoleheight);
                        }
                        underGroundsurveyVo.setPole_support(ugpolesupport);
                        underGroundsurveyVo.setSs_name(ugssname);
                        underGroundsurveyVo.setTapping_point(ugtapingpoint);
                        underGroundsurveyVo.setNoofTappings(ugnooftappings);
                        if(!edt_ugtappinglocation.getText().toString().isEmpty()) {
                            underGroundsurveyVo.setTappingLocation(edt_ugtappinglocation.getText().toString());
                        }
                        if(!edt_ugpolepaintingcode.getText().toString().isEmpty()){
                            underGroundsurveyVo.setPole_paintingcode(edt_ugpolepaintingcode.getText().toString());
                        }
                        underGroundsurveyVo.setWorkOrder(workorder);
                        underGroundsurveyVo.setWorkOrderStatus(workOrderStatus.get(0));
                        underGroundsurveyVo.setIsFeederType(isFeederType.get(0));
                        arcGISDatabase.addIncrementalSurveyData(underGroundsurveyVo);
                        Toast.makeText(c, "Pole location captured successfully..", Toast.LENGTH_SHORT).show();
                        dismiss();
                        callout.dismiss();
                        graphicsOverlay.getGraphics().remove(graphic);


                        if ("YES".equalsIgnoreCase(ug_wosurveycompleted)) {
                            arcGISDatabase.upDate(String.format("UPDATE WO_STATUS_TABLE SET WO_STATUS='Y' WHERE WO_ID='%s'",workorder));
                        }
                        if ("YES".equalsIgnoreCase(undergisendpole)) {
                            //arcGISDatabase.upDate(String.format("UPDATE WO_STATUS_TABLE SET WO_STATUS='Y' WHERE WO_ID='%s'",workorder));
                            //UPDATE WO_STATUS_TABLE SET WO_STATUS='Y' WHERE WO_STATUS='N'
                            Intent intent = new Intent(c, FeederMasterActivityUG33KV.class);
                            c.startActivity(intent);
                            c.finish();
                        }



                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
