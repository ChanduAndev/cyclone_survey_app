package com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.popup;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.activitys.FeederMasterActivity;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.interfacess.ImpData;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.retrofitservice.RetrofitService;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.utilits.CommanUtilits;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.DTRMaster_vo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.IncrementalFeederDataVo;
import com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.vo.IncrementalSurveyVo;
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

public class CustomAllIncremntalDialogs extends Dialog implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private final String x, y;
    public Activity c;
    private Button btn_add, btn_clear, btn_topclear;
    private EditText ed_pole_no, ed_frompole;
    private TextInputEditText edt_polepaintingcode, edt_remarks_overhead, edt_remarks_underground, edt_tappinglocation, edt_ugtappinglocation, edt_ugpolepaintingcode;
    public ImpData deligate;
    private RetrofitService retrofitService;
    private ProgressDialog progressDialog;
    Callout callout;
    GraphicsOverlay graphicsOverlay;
    GraphicsOverlay currentgraphicsOverlay;
    Graphic graphic;
    Graphic currentGraphic;
    LocationDisplay locationDisplay;

    private Spinner sp_oh_ab_switch_dtr_pole, sp_oh_capacity_dtr_pole, sp_oh_phase_dtr_pole, sp_oh_dtr_structure_type_dtr_pole, sp_oh_section_name_dtr_pole, sp_ug_ab_switch_dtr_pole, sp_ug_capacity_dtr_pole, sp_ug_phase_dtr_pole, sp_ug_dtr_structure_type_dtr_pole, sp_ug_section_name_dtr_pole, sp_ugnooftappings, sp_ugtapingpoint, sp_ugpolesupport, sp_ugpoleheight, sp_ugpoletype, sp__underpolestructure, sp_undergisendpole, sp_nooftappings, sp_rmu, sp_straight_rmu, sp_ssname, sp_diffvoltage, sp__polestructure, sp_noofcircuit_underg, sp_cablesize, sp_lineType, sp_poletype, sp_poleheight, sp_noofcircuit, sp_polesupport, sp_tapingpoint, sp_conductorsize, sp_pinpoint, sp_poleconfiguration, sp_ismulticircute, sp_circutetype, sp_isendpole;
    private String ugssname = "", ugnooftappings = "", ugtapingpoint = "", ugpolesupport = "", ugpoleheight = "", ugpoletype = "", underpolestructure = "", undergisendpole = "", ssName = "", nooftappings = "", rmu = "", straight_rmu = "", ssId = "", ssname = "", diffvoltage = "", polestructure = "", noofcircuit_underg = "", cablesize = "", noofcircuit = "", lineType = "", endPole = "", isendpole = "", surveyType, poleType = "", poleHeight = "", noofPoles, feederName = "", poleSupport = "", tapingPoint, taping_Point = "", conductorsize = "", pinpoint = "", poleconfiguration = "", feederCode = "", tappingPoint, ismulticircute = "", circutetype = "";
    private ArcGISDatabase arcGISDatabase;
    private List<IncrementalSurveyVo> surveyVosList;
    private int maxSurveyCount, poleCountCheck;
    private LinearLayout lay_oh_dtr_pole_main, lay__ug_dtr_pole_main, lyt_circuit, lyt_overhead, lyt_underground, lyt_polestructure, lyt_diffvoltage, lyt_rmu, lyt_tapping, lyt_underpolestructure, lyt_ugpole, lyt_ugtapping;
    private TextView tv_sunname, tv_feedename,tv_distance;
    private Map mapData;
    private int copyFlag = 0;
    private AutoCompleteTextView auto_oh_dtr_structure_dtr_pole, auto_oh_make_dtr_pole, auto_ug_dtr_structure_dtr_pole, auto_ug_make_dtr_pole;
    private EditText ed_oh_location_name_dtr_pole, ed_ug_location_name_dtr_pole;
    private LinearLayout lyt_distance,lay_ug_dtr_pole_main;
    private List<String> dtrsectionList = null;
    private String dtr_pole_ug_section_name;
    private List<String> dtr_ug_structureList = null;
    private List<DTRMaster_vo> lsss;
    private ArrayList<String> dtrsectionList_oh = null;
    private String dtr_pole_oh_section_name;
    private List<String> dtr_oh_structureList = null;
    private Spinner sp_ug_voltage_level;
    private Spinner sp_oh_voltage_level;
    private LinearLayout lay_ab_cable_main;
    private Spinner sp_abc_derpolestructure;
    private Spinner sp_abc_poletype;
    private Spinner sp_abc_poleheight;
    private Spinner sp_abc_tapingpoint;
    private Spinner sp_abc_nooftappings;
    private EditText ed_abc_tapping_location_name;
    private Spinner sp_abc_ab_cable_size;
    private Spinner sp_abc_no_of_cable;
    private String sp_abc_pole_type_str;
    private String sp_oh_phase_dtr_pole_str;
    private String sp_ug_phase_dtr_pole_str;
    private EditText ed_oh_serial_number_dtr_pole;
    private EditText ed_ug_serial_number_dtr_pole;
    private Spinner sp_ug_no_of_lt_circuits;
    private Spinner sp_oh_no_of_lt_circuits;
    private LinearLayout lay_oh_ht_service_number;
    private AutoCompleteTextView auto_oh_ht_service_number, auto_ug_ht_service_number;
    private List<String> oh_ht_servicenoList = null;
    private Spinner sp_abc_dtr_capacity;
    private ArrayList<String> dtr_ug_sectionList = null;
    private LinearLayout lay_oh_line_capacitor_pc, lay_oh_auto_reclosure_pc;
    private Spinner sp_oh_auto_reclosure, sp_oh_line_capacitor;
    private String sp_oh_voltage_level_str;
    private Spinner sp_ug_line_capacitor, sp_ug_auto_reclosure;
    private LinearLayout lay_ug_ht_service_number, lay_ug_line_capacitor, lay_ug_auto_reclosure;
    private LinearLayout lyt_abc_tapping;
    private String abc_tapingpoint;
    private String abc_nooftappings;
    private LinearLayout lay_abc_line_capacitor, lay_abc_auto_reclosure;
    private LinearLayout lay_abc_ht_service_number;
    private Spinner sp_abc_line_capacitor, sp_abc_auto_reclosure;
    private AutoCompleteTextView auto_abc_ht_service_number;
    private List<String> abc_ht_servicenoList = null;
    private String sp_abc_location_typeStr;
    private Spinner sp_abc_dergisendpole;
    private Spinner sp_abc_noofcircuit;
    private Spinner sp_abc_straight_rmu;
    private LinearLayout lyt_abc_rmu;
    private LinearLayout lyt_abc_derpolestructure;
    private Spinner sp_abc_polesupport;
    private LinearLayout lay_abc_dtr_pole_main;
    private Spinner sp_abc_section_name_dtr_pole;
    private AutoCompleteTextView auto_abc_dtr_structure_dtr_pole;
    private Spinner sp_abc_dtr_structure_type_dtr_pole;
    private Spinner sp_abc_phase_dtr_pole;
    private Spinner sp_abc_capacity_dtr_pole;
    private AutoCompleteTextView auto_abc_make_dtr_pole;
    private EditText ed_abc_serial_number_dtr_pole;
    private EditText ed_abc_location_name_dtr_pole;
    private Spinner sp_abc_no_of_lt_circuits;
    private Spinner sp_abc_ab_switch_dtr_pole;
    private Spinner sp_abc_rmu;
    private TextInputEditText edt_abc_tappinglocation;
    private Spinner sp_abc_cablesize;
    private EditText edt_abc_remarks;
    private EditText edt_abc_polepaintingcode;
    private Spinner sp_abc_voltage_level;
    private String abc_isendpole = "";
    private String abc_noofcircuitStr;
    private String abc_straight_rmuStr;
    private LinearLayout lyt_abc_pole;
    private String sp_abc_rmuStr;
    private String sp_abc_derpolestructureStr;
    private String sp_abc_polesupportStr;
    private String dtr_pole_abc_section_name;
    private String sp_abc_phase_dtr_pole_str;
    private String sp_abc_nooftappingsStr;
    private String abc_cablesizeStr;
    private String abc_poleheightStr;
    private List<String> makeArrayList;
    private Spinner sp_oh_is_lt_ckts_available;
    private String oh_is_lt_ckts_availableStr;
    private LinearLayout lay_oh_no_of_lt_circuits;
    private Spinner sp_oh_dtr_location_type, sp_oh_type_of_load;
    private LinearLayout lay_oh_no_of_ab_switches;
    private Spinner sp_oh_no_of_ab_switches;
    private Spinner sp_ug_is_lt_ckts_available;
    private LinearLayout lay_ug_no_of_lt_circuits;
    private Spinner sp_ug_dtr_location_type;
    private Spinner sp_ug_type_of_load;
    private LinearLayout lay_ug_no_of_ab_switches;
    private Spinner sp_ug_no_of_ab_switches;
    private String ug_is_lt_ckts_availableStr;
    private Spinner sp_abc_is_lt_ckts_available, sp_abc_dtr_location_type;
    private Spinner sp_abc_type_of_load, sp_abc_no_of_ab_switches;
    private LinearLayout lay_abc_no_of_lt_circuits, lay_abc_no_of_ab_switches;
    private String abc_is_lt_ckts_availableStr;
    private List<String> makeListDatabase = null;
    private String sp_oh_type_of_loadStr;
    private String sp_ug_type_of_loadStr,work_order;
    private String sp_abc_type_of_loadStr;
    private Spinner sp_Survey_type;
    private Spinner sp_oh_is_dtr_avalabile;
    private String oh_is_dtr_avalabileStr;
    private LinearLayout lay_oh_is_dtr_avalable;
    private Spinner sp_ug_is_dtr_avalabile;
    private LinearLayout lay_ug_is_dtr_avalable;
    private String ug_is_dtr_avalabileStr;
    private List<IncrementalFeederDataVo> incrementalFeederDataVoList=null;
    private  double distance;
    private Spinner sp_ug_wosurveycompleted,sp_oh_wosurveycompleted,sp_abc_wosurveycompleted;
    private String ug_wosurveycompleted,oh_wosurveycompleted,abc_wosurveycompleted;

    private  String sp_Survey_type_string = "";

    public CustomAllIncremntalDialogs(Activity a, String x, String y, Callout callout, GraphicsOverlay graphicsOverlay, Graphic graphic, String feederName, String feederCode, int maxSurveyCount, String tappingPoint, String surveyType, GraphicsOverlay currentgraphicsOverlay, Graphic currentGraphic, String ssName,String work_order,double distance) {
        super(a);
        this.c = a;
        this.x = x;
        this.y = y;
        this.callout = callout;
        this.graphicsOverlay = graphicsOverlay;
        this.currentgraphicsOverlay = currentgraphicsOverlay;
        this.graphic = graphic;
        this.currentGraphic = currentGraphic;
        this.feederName = feederName;
        this.feederCode = feederCode;
        this.maxSurveyCount = maxSurveyCount;
        this.tappingPoint = tappingPoint;
        this.surveyType = surveyType;
        this.ssName = ssName;
        this.work_order=work_order;
        this.distance=distance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custome_dialog_incremntal_11kv_oh_rdss);
        try {
            setCancelable(false);
            progressDialog = CommanUtilits.progressDialog(c);
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
            edt_ugtappinglocation = (TextInputEditText) findViewById(R.id.edt_ugtappinglocation);
            edt_ugpolepaintingcode = (TextInputEditText) findViewById(R.id.edt_ugpolepaintingcode);
            sp_isendpole = (Spinner) findViewById(R.id.sp_isendpole);
            lyt_circuit = (LinearLayout) findViewById(R.id.lyt_circuit);
            lyt_overhead = (LinearLayout) findViewById(R.id.lyt_overhead);
            lyt_underground = (LinearLayout) findViewById(R.id.lyt_underground);
            lyt_polestructure = (LinearLayout) findViewById(R.id.lyt_polestructure);
            lyt_diffvoltage = (LinearLayout) findViewById(R.id.lyt_diffvoltage);
            lyt_rmu = (LinearLayout) findViewById(R.id.lyt_rmu);
            lyt_tapping = (LinearLayout) findViewById(R.id.lyt_tapping);
            lyt_underpolestructure = (LinearLayout) findViewById(R.id.lyt_underpolestructure);
            lyt_ugpole = (LinearLayout) findViewById(R.id.lyt_ugpole);
            lyt_ugtapping = (LinearLayout) findViewById(R.id.lyt_ugtapping);
            lyt_distance=findViewById(R.id.lyt_distance);
            lyt_distance.setVisibility(View.GONE);
            tv_distance=findViewById(R.id.tv_distance);

            sp_lineType = (Spinner) findViewById(R.id.sp_lineType);
            sp_noofcircuit = (Spinner) findViewById(R.id.sp_noofcircuit);
            sp_cablesize = (Spinner) findViewById(R.id.sp_cablesize);
            sp_noofcircuit_underg = (Spinner) findViewById(R.id.sp_noofcircuit_underg);
            //edt_ssname=(TextInputEditText)findViewById(R.id.edt_ssname);
            sp__polestructure = (Spinner) findViewById(R.id.sp__polestructure);
            sp_diffvoltage = (Spinner) findViewById(R.id.sp_diffvoltage);

            sp_straight_rmu = (Spinner) findViewById(R.id.sp_straight_rmu);
            sp_rmu = (Spinner) findViewById(R.id.sp_rmu);
            sp_nooftappings = (Spinner) findViewById(R.id.sp_nooftappings);
            sp_undergisendpole = (Spinner) findViewById(R.id.sp_undergisendpole);
            sp__underpolestructure = (Spinner) findViewById(R.id.sp__underpolestructure);
            sp_ugpoletype = (Spinner) findViewById(R.id.sp_ugpoletype);
            sp_ugpoleheight = (Spinner) findViewById(R.id.sp_ugpoleheight);
            sp_ugpolesupport = (Spinner) findViewById(R.id.sp_ugpolesupport);
            sp_ugtapingpoint = (Spinner) findViewById(R.id.sp_ugtapingpoint);
            sp_ugnooftappings = (Spinner) findViewById(R.id.sp_ugnooftappings);

            tv_sunname = (TextView) findViewById(R.id.tv_sunname);
            tv_feedename = (TextView) findViewById(R.id.tv_feedename);
//dtr pole ug
            sp_ug_section_name_dtr_pole = (Spinner) findViewById(R.id.sp_ug_section_name_dtr_pole);
            auto_ug_dtr_structure_dtr_pole = (AutoCompleteTextView) findViewById(R.id.auto_ug_dtr_structure_dtr_pole);
            sp_ug_dtr_structure_type_dtr_pole = (Spinner) findViewById(R.id.sp_ug_dtr_structure_type_dtr_pole);
            sp_ug_phase_dtr_pole = (Spinner) findViewById(R.id.sp_ug_phase_dtr_pole);
            sp_ug_capacity_dtr_pole = (Spinner) findViewById(R.id.sp_ug_capacity_dtr_pole);
            auto_ug_make_dtr_pole = (AutoCompleteTextView) findViewById(R.id.auto_ug_make_dtr_pole);
            ed_ug_location_name_dtr_pole = (EditText) findViewById(R.id.ed_ug_location_name_dtr_pole);
            sp_ug_ab_switch_dtr_pole = (Spinner) findViewById(R.id.sp_ug_ab_switch_dtr_pole);
            lay_ug_dtr_pole_main = (LinearLayout) findViewById(R.id.lay_ug_dtr_pole_main);
            sp_ug_voltage_level = (Spinner) findViewById(R.id.sp_ug_voltage_level);
            ed_ug_serial_number_dtr_pole = (EditText) findViewById(R.id.ed_ug_serial_number_dtr_pole);
            sp_ug_no_of_lt_circuits = (Spinner) findViewById(R.id.sp_ug_no_of_lt_circuits);
            // sp_ug_struct_stay_detailes = (Spinner) findViewById(R.id.sp_ug_struct_stay_detailes);

            lay_ug_ht_service_number = (LinearLayout) findViewById(R.id.lay_ug_ht_service_number);
            lay_ug_line_capacitor = (LinearLayout) findViewById(R.id.lay_ug_line_capacitor);
            lay_ug_auto_reclosure = (LinearLayout) findViewById(R.id.lay_ug_auto_reclosure);
            sp_ug_auto_reclosure = (Spinner) findViewById(R.id.sp_ug_auto_reclosure);
            sp_ug_line_capacitor = (Spinner) findViewById(R.id.sp_ug_line_capacitor);
            auto_ug_ht_service_number = (AutoCompleteTextView) findViewById(R.id.auto_ug_ht_service_number);


            sp_ug_is_lt_ckts_available = (Spinner) findViewById(R.id.sp_ug_is_lt_ckts_available);
            lay_ug_no_of_lt_circuits = (LinearLayout) findViewById(R.id.lay_ug_no_of_lt_circuits);
            sp_ug_dtr_location_type = (Spinner) findViewById(R.id.sp_ug_dtr_location_type);
            sp_ug_type_of_load = (Spinner) findViewById(R.id.sp_ug_type_of_load);
            lay_ug_no_of_ab_switches = (LinearLayout) findViewById(R.id.lay_ug_no_of_ab_switches);
            sp_ug_no_of_ab_switches = (Spinner) findViewById(R.id.sp_ug_no_of_ab_switches);

            //dtr pole oh
            sp_oh_section_name_dtr_pole = (Spinner) findViewById(R.id.sp_oh_section_name_dtr_pole);
            auto_oh_dtr_structure_dtr_pole = (AutoCompleteTextView) findViewById(R.id.auto_oh_dtr_structure_dtr_pole);
            sp_oh_dtr_structure_type_dtr_pole = (Spinner) findViewById(R.id.sp_oh_dtr_structure_type_dtr_pole);
            sp_oh_phase_dtr_pole = (Spinner) findViewById(R.id.sp_oh_phase_dtr_pole);
            sp_oh_capacity_dtr_pole = (Spinner) findViewById(R.id.sp_oh_capacity_dtr_pole);
            auto_oh_make_dtr_pole = (AutoCompleteTextView) findViewById(R.id.auto_oh_make_dtr_pole);
            ed_oh_location_name_dtr_pole = (EditText) findViewById(R.id.ed_oh_location_name_dtr_pole);
            sp_oh_ab_switch_dtr_pole = (Spinner) findViewById(R.id.sp_oh_ab_switch_dtr_pole);
            lay_oh_dtr_pole_main = (LinearLayout) findViewById(R.id.lay_oh_dtr_pole_main);
            sp_oh_voltage_level = (Spinner) findViewById(R.id.sp_oh_voltage_level);
            ed_oh_serial_number_dtr_pole = (EditText) findViewById(R.id.ed_oh_serial_number_dtr_pole);
            sp_oh_no_of_lt_circuits = (Spinner) findViewById(R.id.sp_oh_no_of_lt_circuits);
            lay_oh_ht_service_number = (LinearLayout) findViewById(R.id.lay_oh_ht_service_number);
            auto_oh_ht_service_number = (AutoCompleteTextView) findViewById(R.id.auto_oh_ht_service_number);
            lay_oh_line_capacitor_pc = (LinearLayout) findViewById(R.id.lay_oh_line_capacitor_pc);
            lay_oh_auto_reclosure_pc = (LinearLayout) findViewById(R.id.lay_oh_auto_reclosure_pc);
            sp_oh_auto_reclosure = (Spinner) findViewById(R.id.sp_oh_auto_reclosure);
            sp_oh_line_capacitor = (Spinner) findViewById(R.id.sp_oh_line_capacitor);
            sp_oh_is_lt_ckts_available = (Spinner) findViewById(R.id.sp_oh_is_lt_ckts_available);
            lay_oh_no_of_lt_circuits = (LinearLayout) findViewById(R.id.lay_oh_no_of_lt_circuits);
            sp_oh_dtr_location_type = (Spinner) findViewById(R.id.sp_oh_dtr_location_type);
            sp_oh_type_of_load = (Spinner) findViewById(R.id.sp_oh_type_of_load);
            lay_oh_no_of_ab_switches = (LinearLayout) findViewById(R.id.lay_oh_no_of_ab_switches);
            sp_oh_no_of_ab_switches = (Spinner) findViewById(R.id.sp_oh_no_of_ab_switches);
            sp_oh_is_dtr_avalabile=(Spinner)findViewById(R.id.sp_oh_is_dtr_avalabile);
            sp_Survey_type=(Spinner)findViewById(R.id.sp_Survey_type);
            lay_oh_is_dtr_avalable=(LinearLayout)findViewById(R.id.lay_oh_is_dtr_avalable);

            sp_ug_is_dtr_avalabile=(Spinner)findViewById(R.id.sp_ug_is_dtr_avalabile);
            lay_ug_is_dtr_avalable=(LinearLayout)findViewById(R.id.lay_ug_is_dtr_avalable);

            //AB Cable

            lay_ab_cable_main = (LinearLayout) findViewById(R.id.lay_ab_cable_main);
            sp_abc_voltage_level = (Spinner) findViewById(R.id.sp_abc_voltage_level);
            sp_abc_dergisendpole = (Spinner) findViewById(R.id.sp_abc_dergisendpole);
            sp_abc_noofcircuit = (Spinner) findViewById(R.id.sp_abc_noofcircuit);
            sp_abc_straight_rmu = (Spinner) findViewById(R.id.sp_abc_straight_rmu);
            sp_abc_line_capacitor = (Spinner) findViewById(R.id.sp_abc_line_capacitor);
            lay_abc_line_capacitor = (LinearLayout) findViewById(R.id.lay_abc_line_capacitor);
            lay_abc_auto_reclosure = (LinearLayout) findViewById(R.id.lay_abc_auto_reclosure);
            lay_abc_ht_service_number = (LinearLayout) findViewById(R.id.lay_abc_ht_service_number);
            sp_abc_line_capacitor = (Spinner) findViewById(R.id.sp_abc_line_capacitor);
            sp_abc_auto_reclosure = (Spinner) findViewById(R.id.sp_abc_auto_reclosure);
            auto_abc_ht_service_number = (AutoCompleteTextView) findViewById(R.id.auto_abc_ht_service_number);
            lyt_abc_rmu = (LinearLayout) findViewById(R.id.lyt_abc_rmu);
            sp_abc_rmu = (Spinner) findViewById(R.id.sp_abc_rmu);
            lyt_abc_derpolestructure = (LinearLayout) findViewById(R.id.lyt_abc_derpolestructure);
            sp_abc_derpolestructure = (Spinner) findViewById(R.id.sp_abc_derpolestructure);
            sp_abc_poletype = (Spinner) findViewById(R.id.sp_abc_poletype);
            sp_abc_poleheight = (Spinner) findViewById(R.id.sp_abc_poleheight);
            // sp_abc_struct_stay_detailes=(Spinner)findViewById(R.id.sp_abc_struct_stay_detailes);
            sp_abc_polesupport = (Spinner) findViewById(R.id.sp_abc_polesupport);
            lay_abc_dtr_pole_main = (LinearLayout) findViewById(R.id.lay_abc_dtr_pole_main);
            sp_abc_section_name_dtr_pole = (Spinner) findViewById(R.id.sp_abc_section_name_dtr_pole);
            auto_abc_dtr_structure_dtr_pole = (AutoCompleteTextView) findViewById(R.id.auto_abc_dtr_structure_dtr_pole);
            sp_abc_dtr_structure_type_dtr_pole = (Spinner) findViewById(R.id.sp_abc_dtr_structure_type_dtr_pole);
            sp_abc_phase_dtr_pole = (Spinner) findViewById(R.id.sp_abc_phase_dtr_pole);
            sp_abc_capacity_dtr_pole = (Spinner) findViewById(R.id.sp_abc_capacity_dtr_pole);
            auto_abc_make_dtr_pole = (AutoCompleteTextView) findViewById(R.id.auto_abc_make_dtr_pole);
            ed_abc_serial_number_dtr_pole = (EditText) findViewById(R.id.ed_abc_serial_number_dtr_pole);
            ed_abc_location_name_dtr_pole = (EditText) findViewById(R.id.ed_abc_location_name_dtr_pole);
            sp_abc_no_of_lt_circuits = (Spinner) findViewById(R.id.sp_abc_no_of_lt_circuits);
            sp_abc_ab_switch_dtr_pole = (Spinner) findViewById(R.id.sp_abc_ab_switch_dtr_pole);
            sp_abc_tapingpoint = (Spinner) findViewById(R.id.sp_abc_tapingpoint);
            sp_abc_nooftappings = (Spinner) findViewById(R.id.sp_abc_nooftappings);
            edt_abc_tappinglocation = (TextInputEditText) findViewById(R.id.edt_abc_tappinglocation);
            sp_abc_cablesize = (Spinner) findViewById(R.id.sp_abc_cablesize);
            edt_abc_remarks = (EditText) findViewById(R.id.edt_abc_remarks);
            edt_abc_polepaintingcode = (EditText) findViewById(R.id.edt_abc_polepaintingcode);
            lyt_abc_tapping = (LinearLayout) findViewById(R.id.lyt_abc_tapping);
            lyt_abc_pole = (LinearLayout) findViewById(R.id.lyt_abc_pole);

            sp_abc_is_lt_ckts_available = (Spinner) findViewById(R.id.sp_abc_is_lt_ckts_available);
            lay_abc_no_of_lt_circuits = (LinearLayout) findViewById(R.id.lay_abc_no_of_lt_circuits);
            sp_abc_dtr_location_type = (Spinner) findViewById(R.id.sp_abc_dtr_location_type);
            sp_abc_type_of_load = (Spinner) findViewById(R.id.sp_abc_type_of_load);
            lay_abc_no_of_ab_switches = (LinearLayout) findViewById(R.id.lay_abc_no_of_ab_switches);
            sp_abc_no_of_ab_switches = (Spinner) findViewById(R.id.sp_abc_no_of_ab_switches);

            sp_ug_wosurveycompleted=(Spinner) findViewById(R.id.sp_ug_wosurveycompleted);
            sp_oh_wosurveycompleted=(Spinner) findViewById(R.id.sp_oh_wosurveycompleted);
            sp_abc_wosurveycompleted=(Spinner) findViewById(R.id.sp_abc_wosurveycompleted);

            sp_ug_wosurveycompleted.setOnItemSelectedListener(this);
            sp_oh_wosurveycompleted.setOnItemSelectedListener(this);
            sp_abc_wosurveycompleted.setOnItemSelectedListener(this);

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
            sp_oh_type_of_load.setOnItemSelectedListener(this);
            sp_oh_is_dtr_avalabile.setOnItemSelectedListener(this);
            sp_Survey_type.setOnItemSelectedListener(this);

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
            sp_abc_tapingpoint.setOnItemSelectedListener(this);

            sp_oh_section_name_dtr_pole.setOnItemSelectedListener(this);
            sp_oh_dtr_structure_type_dtr_pole.setOnItemSelectedListener(this);
            sp_oh_phase_dtr_pole.setOnItemSelectedListener(this);
            sp_oh_capacity_dtr_pole.setOnItemSelectedListener(this);
            sp_oh_ab_switch_dtr_pole.setOnItemSelectedListener(this);
            sp_oh_voltage_level.setOnItemSelectedListener(this);
            sp_oh_is_lt_ckts_available.setOnItemSelectedListener(this);

            sp_ug_section_name_dtr_pole.setOnItemSelectedListener(this);
            sp_ug_dtr_structure_type_dtr_pole.setOnItemSelectedListener(this);
            sp_ug_phase_dtr_pole.setOnItemSelectedListener(this);
            sp_ug_capacity_dtr_pole.setOnItemSelectedListener(this);
            sp_ug_ab_switch_dtr_pole.setOnItemSelectedListener(this);
            sp_ug_is_dtr_avalabile.setOnItemSelectedListener(this);
            sp_abc_dergisendpole.setOnItemSelectedListener(this);
            sp_abc_noofcircuit.setOnItemSelectedListener(this);
            sp_abc_straight_rmu.setOnItemSelectedListener(this);
            sp_abc_derpolestructure.setOnItemSelectedListener(this);
            sp_abc_polesupport.setOnItemSelectedListener(this);
            sp_abc_poleheight.setOnItemSelectedListener(this);
            sp_abc_section_name_dtr_pole.setOnItemSelectedListener(this);
            sp_abc_dtr_structure_type_dtr_pole.setOnItemSelectedListener(this);
            sp_abc_phase_dtr_pole.setOnItemSelectedListener(this);
            sp_abc_capacity_dtr_pole.setOnItemSelectedListener(this);
            sp_abc_ab_switch_dtr_pole.setOnItemSelectedListener(this);
            sp_abc_tapingpoint.setOnItemSelectedListener(this);
            sp_abc_nooftappings.setOnItemSelectedListener(this);
            sp_abc_cablesize.setOnItemSelectedListener(this);
            sp_abc_is_lt_ckts_available.setOnItemSelectedListener(this);
            sp_ug_is_lt_ckts_available.setOnItemSelectedListener(this);
            sp_oh_is_lt_ckts_available.setOnItemSelectedListener(this);
            sp_ug_type_of_load.setOnItemSelectedListener(this);
            sp_abc_type_of_load.setOnItemSelectedListener(this);
            incrementalFeederDataVoList=new ArrayList<>();
            arcGISDatabase = new ArcGISDatabase(c);
            surveyVosList = arcGISDatabase.getIncrementalSurveyData("");
            int count = arcGISDatabase.getIncrementalObjectId();
            poleCountCheck = arcGISDatabase.getIncrementalObjectIdCount();

            incrementalFeederDataVoList=arcGISDatabase.getIncrementalFeerData();
            if (poleCountCheck == 0) {
                //  if(""surveyType)
                int polenum = maxSurveyCount + 1;
                // ed_frompole.setText(String.valueOf(count));
                //ed_pole_no.setText(String.valueOf(polenum));
                if(surveyVosList.size()==1){
                    List<String>   poleNumber = arcGISDatabase.getComplaint(String.format("SELECT DISTINCT FROM_POLE FROM INCREMENTAL_EHTMASTER_TABLE WHERE WORKORDER_ID='%s'", work_order));
                    ed_frompole.setText(poleNumber.get(0));
                }else if(surveyVosList.size()>1){
                    ed_frompole.setText(String.valueOf(count));
                }
                ed_pole_no.setText(String.valueOf(polenum));
            } else {
                ed_frompole.setText(String.valueOf(count));
                ed_pole_no.setText(String.valueOf(count + 1));
            }


            if(count>0){
                String idtance_Poles=String.valueOf(Math.round(distance));
                tv_distance.setText(idtance_Poles+"  Mtrs");

                lyt_distance.setVisibility(View.GONE);
            }else {
                lyt_distance.setVisibility(View.GONE);
            }





            lyt_underground.setVisibility(View.GONE);

            lyt_polestructure.setVisibility(View.GONE);
            lyt_diffvoltage.setVisibility(View.GONE);
            lyt_rmu.setVisibility(View.GONE);
            lyt_tapping.setVisibility(View.GONE);
            lyt_underpolestructure.setVisibility(View.GONE);
            lyt_ugpole.setVisibility(View.GONE);
            lyt_ugtapping.setVisibility(View.GONE);
            lay_ug_dtr_pole_main.setVisibility(View.GONE);
            lay_oh_dtr_pole_main.setVisibility(View.GONE);
            lay_oh_no_of_lt_circuits.setVisibility(View.GONE);
            lay_oh_no_of_ab_switches.setVisibility(View.GONE);
            lay_oh_is_dtr_avalable.setVisibility(View.GONE);
            lay_ug_is_dtr_avalable.setVisibility(View.GONE);

            lay_ab_cable_main.setVisibility(View.GONE);
            lay_oh_ht_service_number.setVisibility(View.GONE);
            lay_oh_line_capacitor_pc.setVisibility(View.GONE);
            lay_oh_line_capacitor_pc.setVisibility(View.GONE);
            lay_ug_ht_service_number.setVisibility(View.GONE);
            lyt_abc_tapping.setVisibility(View.GONE);
            lay_abc_ht_service_number.setVisibility(View.GONE);
            lay_abc_line_capacitor.setVisibility(View.GONE);
            lay_abc_auto_reclosure.setVisibility(View.GONE);
            lay_abc_no_of_ab_switches.setVisibility(View.GONE);
            lay_abc_no_of_lt_circuits.setVisibility(View.GONE);
            lay_ug_no_of_ab_switches.setVisibility(View.GONE);
            lay_ug_no_of_lt_circuits.setVisibility(View.GONE);


            tv_sunname.setText(ssName);
            Log.v("SSNAME-->", ssName);
            tv_feedename.setText(feederName);
            mapData = new HashMap();

            makeListDatabase = arcGISDatabase.getComplaint("SELECT DTR_MAKE_M FROM INCREMENTAL_MAKE_MASTER_TABLE");
            ArrayAdapter adapterMake_oh = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, makeListDatabase);//c.getResources().getStringArray(R.array.dtr_make)
            auto_oh_make_dtr_pole.setAdapter(adapterMake_oh);
            auto_oh_make_dtr_pole.setThreshold(1);
            ArrayAdapter adapterMake_ug = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, makeListDatabase);
            auto_ug_make_dtr_pole.setAdapter(adapterMake_ug);
            auto_ug_make_dtr_pole.setThreshold(1);
            ArrayAdapter adapterMake_abc = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, makeListDatabase);
            auto_abc_make_dtr_pole.setAdapter(adapterMake_abc);
            auto_abc_make_dtr_pole.setThreshold(1);
            makeArrayList = makeListDatabase;
            String CONDUCTOR_TX = "CONDUCTOR";

            List<String> conductorsizeListDatabe = arcGISDatabase.getComplaint("Select CKT_VALUE from INCREMENTAL_MULTIVOLTAGE_MASTERS_TABLE where CKT_NO = '" + CONDUCTOR_TX + "'" + " ORDER BY CKT_VALUE");
            conductorsizeListDatabe.add("Select");
            Collections.reverse(conductorsizeListDatabe);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, conductorsizeListDatabe);
            sp_conductorsize.setAdapter(adapter);

            //NO Of Cables
            String CABLE_TX = "CABLE";
            List<String> cableListDatabe = arcGISDatabase.getComplaint("Select CKT_VALUE from INCREMENTAL_MULTIVOLTAGE_MASTERS_TABLE where CKT_NO = '" + CABLE_TX + "'" + " ORDER BY CKT_VALUE");
            cableListDatabe.add("Select");
            Collections.reverse(cableListDatabe);
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, cableListDatabe);
            sp_cablesize.setAdapter(adapter1);

            //NO Of Cables
            String CABLE_TX1 = "CABLE";
            List<String> abc_cableListDatabe = arcGISDatabase.getComplaint("Select CKT_VALUE from INCREMENTAL_MULTIVOLTAGE_MASTERS_TABLE where CKT_NO = '" + CABLE_TX1 + "'" + " ORDER BY CKT_VALUE");
            abc_cableListDatabe.add("Select");
            Collections.reverse(abc_cableListDatabe);
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, abc_cableListDatabe);
            sp_abc_cablesize.setAdapter(adapter2);
            edt_tappinglocation.setFilters(new InputFilter[]{new EmojiExcludeFilter()});
            edt_ugtappinglocation.setFilters(new InputFilter[]{new EmojiExcludeFilter()});
            edt_abc_tappinglocation.setFilters(new InputFilter[]{new EmojiExcludeFilter()});

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class EmojiExcludeFilter implements InputFilter {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                int type = Character.getType(source.charAt(i));
                if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
                    return "";
                }
            }
            return null;
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
                        } else if ("AB Cable/Covered Conductor".equalsIgnoreCase(lineType)) {
                            abCableData();
                        }
                    }

                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        try {
            switch (adapterView.getId()) {
                case R.id.sp_ug_is_dtr_avalabile:
                    ug_is_dtr_avalabileStr = sp_ug_is_dtr_avalabile.getSelectedItem().toString();
                    if ("Yes".equalsIgnoreCase(ug_is_dtr_avalabileStr)){
                        lay_ug_is_dtr_avalable.setVisibility(View.VISIBLE);
                    }else{
                        lay_ug_is_dtr_avalable.setVisibility(View.GONE);
                        sp_ug_phase_dtr_pole.setSelection(0);
                        sp_ug_capacity_dtr_pole.setSelection(0);
                        sp_ug_type_of_load.setSelection(0);
                        sp_ug_dtr_location_type.setSelection(0);
                        sp_ug_is_lt_ckts_available.setSelection(0);
                        sp_ug_no_of_lt_circuits.setSelection(0);
                        sp_ug_ab_switch_dtr_pole.setSelection(0);
                        auto_ug_make_dtr_pole.setText("");
                        ed_ug_serial_number_dtr_pole.setText("");
                        ed_ug_location_name_dtr_pole.setText("");
                    }
                    break;
                case R.id.sp_oh_is_dtr_avalabile:
                    oh_is_dtr_avalabileStr = sp_oh_is_dtr_avalabile.getSelectedItem().toString();
                    if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr)){
                        lay_oh_is_dtr_avalable.setVisibility(View.VISIBLE);
                    }else{
                        lay_oh_is_dtr_avalable.setVisibility(View.GONE);
                        sp_oh_phase_dtr_pole.setSelection(0);
                        sp_oh_capacity_dtr_pole.setSelection(0);
                        sp_oh_type_of_load.setSelection(0);
                        sp_oh_dtr_location_type.setSelection(0);
                        sp_oh_is_lt_ckts_available.setSelection(0);
                        sp_oh_no_of_lt_circuits.setSelection(0);
                        sp_oh_ab_switch_dtr_pole.setSelection(0);
                        auto_oh_make_dtr_pole.setText("");
                        ed_oh_serial_number_dtr_pole.setText("");
                        ed_oh_location_name_dtr_pole.setText("");
                    }
                    break;
                case R.id.sp_oh_type_of_load:
                    sp_oh_type_of_loadStr = sp_oh_type_of_load.getSelectedItem().toString();
                    sp_oh_dtr_location_type.setSelection(0);
                    if ("Agl".equalsIgnoreCase(sp_oh_type_of_loadStr)) {
                        List<String> aglcList = new ArrayList<>();
                        aglcList.add("Select");
                        aglcList.add("Aqua");
                        aglcList.add("Non-Aqua");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, aglcList);
                        sp_oh_dtr_location_type.setAdapter(adapter);
                    } else if ("Industrial".equalsIgnoreCase(sp_oh_type_of_loadStr)) {
                        List<String> inducList = new ArrayList<>();
                        inducList.add("Select");
                        inducList.add("Dedicated");
                        inducList.add("Non dedicated");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, inducList);
                        sp_oh_dtr_location_type.setAdapter(adapter);

                    } else if ("Commercial".equalsIgnoreCase(sp_oh_type_of_loadStr) || "Domestic".equalsIgnoreCase(sp_oh_type_of_loadStr) || "Mixed".equalsIgnoreCase(sp_oh_type_of_loadStr)) {
                        List<String> comercialList = new ArrayList<>();
                        comercialList.add("Select");
                        comercialList.add("Field DTR");
                        comercialList.add("Apartment");
                        comercialList.add("Group House");
                        comercialList.add("Commercial Complex");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, comercialList);
                        sp_oh_dtr_location_type.setAdapter(adapter);
                    } else {
                        List<String> selectcList = new ArrayList<>();
                        selectcList.add("Select");

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, selectcList);
                        sp_oh_dtr_location_type.setAdapter(adapter);
                    }
                    break;

                case R.id.sp_ug_type_of_load:
                    sp_ug_type_of_loadStr = sp_ug_type_of_load.getSelectedItem().toString();
                    sp_ug_dtr_location_type.setSelection(0);
                    if ("Agl".equalsIgnoreCase(sp_ug_type_of_loadStr)) {
                        List<String> aglcList = new ArrayList<>();
                        aglcList.add("Select");
                        aglcList.add("Aqua");
                        aglcList.add("Non-Aqua");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, aglcList);
                        sp_ug_dtr_location_type.setAdapter(adapter);
                    } else if ("Industrial".equalsIgnoreCase(sp_ug_type_of_loadStr)) {
                        List<String> inducList = new ArrayList<>();
                        inducList.add("Select");
                        inducList.add("Dedicated");
                        inducList.add("Non dedicated");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, inducList);
                        sp_ug_dtr_location_type.setAdapter(adapter);

                    } else if ("Commercial".equalsIgnoreCase(sp_ug_type_of_loadStr) || "Domestic".equalsIgnoreCase(sp_ug_type_of_loadStr) || "Mixed".equalsIgnoreCase(sp_ug_type_of_loadStr)) {
                        List<String> comercialList = new ArrayList<>();
                        comercialList.add("Select");
                        comercialList.add("Field DTR");
                        comercialList.add("Apartment");
                        comercialList.add("Group House");
                        comercialList.add("Commercial Complex");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, comercialList);
                        sp_ug_dtr_location_type.setAdapter(adapter);
                    } else {
                        List<String> selectcList = new ArrayList<>();
                        selectcList.add("Select");

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, selectcList);
                        sp_ug_dtr_location_type.setAdapter(adapter);
                    }
                    break;
                case R.id.sp_abc_type_of_load:
                    sp_abc_type_of_loadStr = sp_abc_type_of_load.getSelectedItem().toString();
                    sp_abc_dtr_location_type.setSelection(0);
                    if ("Agl".equalsIgnoreCase(sp_abc_type_of_loadStr)) {
                        List<String> aglcList = new ArrayList<>();
                        aglcList.add("Select");
                        aglcList.add("Aqua");
                        aglcList.add("Non-Aqua");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, aglcList);
                        sp_abc_dtr_location_type.setAdapter(adapter);
                    } else if ("Industrial".equalsIgnoreCase(sp_abc_type_of_loadStr)) {
                        List<String> inducList = new ArrayList<>();
                        inducList.add("Select");
                        inducList.add("Dedicated");
                        inducList.add("Non dedicated");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, inducList);
                        sp_abc_dtr_location_type.setAdapter(adapter);

                    } else if ("Commercial".equalsIgnoreCase(sp_abc_type_of_loadStr) || "Domestic".equalsIgnoreCase(sp_abc_type_of_loadStr) || "Mixed".equalsIgnoreCase(sp_abc_type_of_loadStr)) {
                        List<String> comercialList = new ArrayList<>();
                        comercialList.add("Select");
                        comercialList.add("Field DTR");
                        comercialList.add("Apartment");
                        comercialList.add("Group House");
                        comercialList.add("Commercial Complex");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, comercialList);
                        sp_abc_dtr_location_type.setAdapter(adapter);
                    } else {
                        List<String> selectcList = new ArrayList<>();
                        selectcList.add("Select");

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, selectcList);
                        sp_abc_dtr_location_type.setAdapter(adapter);
                    }
                    break;
                case R.id.sp_oh_is_lt_ckts_available:
                    oh_is_lt_ckts_availableStr = sp_oh_is_lt_ckts_available.getSelectedItem().toString();
                    if ("Yes".equalsIgnoreCase(oh_is_lt_ckts_availableStr)) {
                        lay_oh_no_of_lt_circuits.setVisibility(View.VISIBLE);
                    } else {
                        lay_oh_no_of_lt_circuits.setVisibility(View.GONE);
                        sp_oh_no_of_lt_circuits.setSelection(0);
                    }
                    break;
                case R.id.sp_ug_is_lt_ckts_available:
                    ug_is_lt_ckts_availableStr = sp_ug_is_lt_ckts_available.getSelectedItem().toString();
                    if ("Yes".equalsIgnoreCase(ug_is_lt_ckts_availableStr)) {
                        lay_ug_no_of_lt_circuits.setVisibility(View.VISIBLE);
                    } else {
                        lay_ug_no_of_lt_circuits.setVisibility(View.GONE);
                        sp_ug_no_of_lt_circuits.setSelection(0);
                    }
                    break;
                case R.id.sp_abc_is_lt_ckts_available:
                    abc_is_lt_ckts_availableStr = sp_abc_is_lt_ckts_available.getSelectedItem().toString();
                    if ("Yes".equalsIgnoreCase(abc_is_lt_ckts_availableStr)) {
                        lay_abc_no_of_lt_circuits.setVisibility(View.VISIBLE);
                    } else {
                        lay_abc_no_of_lt_circuits.setVisibility(View.GONE);
                        sp_abc_no_of_lt_circuits.setSelection(0);
                    }
                    break;
                case R.id.sp_poletype:
                    poleType = sp_poletype.getSelectedItem().toString();

                    if ("PSCC".equalsIgnoreCase(poleType)) {
                        List<String> psccList = new ArrayList<>();
                        psccList.add("Select");
                        psccList.add("8.0");
                        psccList.add("9.1");
                        psccList.add("11");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, psccList);
                        sp_poleheight.setAdapter(adapter);
                    } else if ("SPUN".equalsIgnoreCase(poleType)) {
                        List<String> spunList = new ArrayList<>();
                        spunList.add("Select");
                        spunList.add("9.5");
                        spunList.add("11");
                        spunList.add("12.5");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, spunList);
                        sp_poleheight.setAdapter(adapter);

                    } else if ("RAIL".equalsIgnoreCase(poleType)) {
                        List<String> railList = new ArrayList<>();
                        railList.add("Select");
                        railList.add("9.5");
                        railList.add("11");
                        railList.add("12.5");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, railList);
                        sp_poleheight.setAdapter(adapter);

                    } else if ("RS JOIST".equalsIgnoreCase(poleType)) {
                        List<String> rssJoistList = new ArrayList<>();
                        rssJoistList.add("Select");
                        /*rssJoistList.add("8");*/
                        rssJoistList.add("9.1");
                        rssJoistList.add("11");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, rssJoistList);
                        sp_poleheight.setAdapter(adapter);
                    } else if ("TOWER".equalsIgnoreCase(poleType)) {
                        List<String> towertList = new ArrayList<>();
                        towertList.add("Select");
                        towertList.add("M+3");
                        towertList.add("M+6");
                        towertList.add("M+9");
                        towertList.add("M+12");
                        towertList.add("R+6");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, towertList);
                        sp_poleheight.setAdapter(adapter);
                    } else if ("Tubular Pole".equalsIgnoreCase(poleType)) {

                        List<String> tubularList = new ArrayList<>();
                        tubularList.add("Select");
                        tubularList.add("9.1");
                        tubularList.add("11");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, tubularList);
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
                        sp_nooftappings.setSelection(0);
                        edt_tappinglocation.setText("");
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
                    /*if ("SS-Bay".equalsIgnoreCase(poleconfiguration)) {
                        lyt_ssname.setVisibility(View.VISIBLE);
                        lay_dtr_pole_main.setVisibility(View.GONE);
                        //List<String> sunStationName=arcGISDatabase.getFeederId(String.format("SELECT DISTINCT EHT_SS_NAME FROM SSMASTER_TABLE WHERE FEEDER_CODE_33KV='%s'",feederCode));
                        List<String> sunStationName = arcGISDatabase.getSSNAMEList();
                        sunStationName.add("Select");
                        Collections.reverse(sunStationName);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, sunStationName);
                        sp_ssname.setAdapter(adapter);

                    } else */
                    ssname = "";
                    sp_oh_line_capacitor.setSelection(0);
                    sp_oh_auto_reclosure.setSelection(0);
                    sp_oh_no_of_ab_switches.setSelection(0);
                    clearTextAfterSelectedOtherFiled_OH();
                    if ("DTR Pole".equalsIgnoreCase(poleconfiguration)) {
                        lay_oh_dtr_pole_main.setVisibility(View.VISIBLE);
                        lay_oh_ht_service_number.setVisibility(View.GONE);
                        lay_oh_auto_reclosure_pc.setVisibility(View.GONE);
                        lay_oh_line_capacitor_pc.setVisibility(View.GONE);
                        lay_oh_no_of_ab_switches.setVisibility(View.GONE);
                        auto_oh_ht_service_number.setText("");
                        //dtrsectionList_oh = arcGISDatabase.getAll_DtrStructureSubStationMaster();
                        dtrsectionList_oh = arcGISDatabase.getIncrementalAll_DtrStructureSubStationMaster();
                        dtrsectionList_oh.add("Select");
                        Collections.reverse(dtrsectionList_oh);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c,
                                android.R.layout.simple_spinner_dropdown_item, dtrsectionList_oh);
                        sp_oh_section_name_dtr_pole.setAdapter(adapter);
                    } else if ("HT Service-Pole".equalsIgnoreCase(poleconfiguration)) {
                        lay_oh_dtr_pole_main.setVisibility(View.GONE);
                        lay_oh_ht_service_number.setVisibility(View.VISIBLE);
                        lay_oh_auto_reclosure_pc.setVisibility(View.GONE);
                        lay_oh_line_capacitor_pc.setVisibility(View.GONE);
                        lay_oh_no_of_ab_switches.setVisibility(View.GONE);
                        auto_oh_ht_service_number.setText("");
                        //List<String> oh_ht_servicenoList1 = arcGISDatabase.getComplaint("SELECT DISTINCT FS_CONSUMER_NUMBER || '~' || FS_CONSUMER_NAME FROM FEEDER_SERVICE_MASTER_TABLE");
                        List<String> oh_ht_servicenoList1 = arcGISDatabase.getComplaint("SELECT DISTINCT FS_CONSUMER_NUMBER || '~' || FS_CONSUMER_NAME FROM INCREMENTAL_FEEDER_SERVICE_MASTER_TABLE");
                        oh_ht_servicenoList1.add("Select");
                        Collections.reverse(oh_ht_servicenoList1);
                        ArrayAdapter adapterMake_ug = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, oh_ht_servicenoList1);
                        auto_oh_ht_service_number.setAdapter(adapterMake_ug);
                        auto_oh_ht_service_number.setThreshold(1);

                        // Toast.makeText(c,"Size"+oh_ht_servicenoList.size(),Toast.LENGTH_LONG).show();
                    } else if ("Line Capacitor".equalsIgnoreCase(poleconfiguration)) {
                        lay_oh_dtr_pole_main.setVisibility(View.GONE);
                        lay_oh_ht_service_number.setVisibility(View.GONE);
                        lay_oh_auto_reclosure_pc.setVisibility(View.GONE);
                        lay_oh_line_capacitor_pc.setVisibility(View.VISIBLE);
                        lay_oh_no_of_ab_switches.setVisibility(View.GONE);
                        auto_oh_ht_service_number.setText("");
                    } else if ("Auto Reclosure".equalsIgnoreCase(poleconfiguration)) {
                        lay_oh_dtr_pole_main.setVisibility(View.GONE);
                        lay_oh_ht_service_number.setVisibility(View.GONE);
                        lay_oh_auto_reclosure_pc.setVisibility(View.VISIBLE);
                        lay_oh_line_capacitor_pc.setVisibility(View.GONE);
                        lay_oh_no_of_ab_switches.setVisibility(View.GONE);
                        auto_oh_ht_service_number.setText("");
                    } else if ("AB-Pole".equalsIgnoreCase(poleconfiguration)) {
                        lay_oh_no_of_ab_switches.setVisibility(View.VISIBLE);
                        lay_oh_dtr_pole_main.setVisibility(View.GONE);
                        lay_oh_ht_service_number.setVisibility(View.GONE);
                        lay_oh_auto_reclosure_pc.setVisibility(View.GONE);
                        lay_oh_line_capacitor_pc.setVisibility(View.GONE);
                        auto_oh_ht_service_number.setText("");
                    } else {
                        lay_oh_dtr_pole_main.setVisibility(View.GONE);
                        lay_oh_ht_service_number.setVisibility(View.GONE);
                        lay_oh_auto_reclosure_pc.setVisibility(View.GONE);
                        lay_oh_line_capacitor_pc.setVisibility(View.GONE);
                        lay_oh_no_of_ab_switches.setVisibility(View.GONE);
                        ssname = "";
                        auto_oh_ht_service_number.setText("");
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
                        sp_diffvoltage.setSelection(0);

                    } else {
                        sp_circutetype.setSelection(0);
                        lyt_circuit.setVisibility(View.GONE);
                        lyt_diffvoltage.setVisibility(View.GONE);
                        sp_noofcircuit.setSelection(0);
                        sp_diffvoltage.setSelection(0);
                        noofcircuit = "";
                        circutetype = "";
                    }
                    break;
                case R.id.sp_noofcircuit:
                    noofcircuit = sp_noofcircuit.getSelectedItem().toString();
                    if (!"Select".equalsIgnoreCase(noofcircuit)) {
                        try {
                            // List<String> deffvoltageListDatabe = arcGISDatabase.getComplaint("Select CKT_VALUE from MULTIVOLTAGE_MASTERS_TABLE where CKT_NO = '" + noofcircuit + "'" + " ORDER BY CKT_VALUE");
                            List<String> deffvoltageListDatabe = arcGISDatabase.getComplaint("Select CKT_VALUE from INCREMENTAL_MULTIVOLTAGE_MASTERS_TABLE where CKT_NO = '" + noofcircuit + "'" + " ORDER BY CKT_VALUE");
                            deffvoltageListDatabe.add("Select");
                            Collections.reverse(deffvoltageListDatabe);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, deffvoltageListDatabe);
                            sp_diffvoltage.setAdapter(adapter);
                        } catch (Exception e) {
                        }
                    }

                    /*if ("2".equalsIgnoreCase(noofcircuit)) {
                        List<String> twoCircuits = new ArrayList<>();
                        twoCircuits.add("Select");
                        twoCircuits.add("33kv");
                        twoCircuits.add("6.3kv");
                        twoCircuits.add("LT");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, twoCircuits);
                        sp_diffvoltage.setAdapter(adapter);
                    } else if ("3".equalsIgnoreCase(noofcircuit)) {
                        List<String> threeCircuits = new ArrayList<>();
                        threeCircuits.add("Select");
                        threeCircuits.add("(33 kv-1)+(11 kv-1)");
                        threeCircuits.add("33 kv-2");
                        threeCircuits.add("(33 kv-1)+(LT-1)");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, threeCircuits);
                        sp_diffvoltage.setAdapter(adapter);
                    } else if ("4".equalsIgnoreCase(noofcircuit)) {
                        List<String> fourCircuits = new ArrayList<>();
                        fourCircuits.add("Select");
                        fourCircuits.add("11 kv-2/33kv-1");
                        fourCircuits.add("11 kv- 1/33kv-2");
                        fourCircuits.add("33 kv-1/11 kv-1+LT-1");
                        fourCircuits.add("33 kv-3");
                        fourCircuits.add("33 kv-2+LT-1");
                        fourCircuits.add("11 kv-2+LT-1");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, fourCircuits);
                        sp_diffvoltage.setAdapter(adapter);
                    } else if ("5".equalsIgnoreCase(noofcircuit)) {
                        List<String> fiveCircuits = new ArrayList<>();
                        fiveCircuits.add("Select");
                        fiveCircuits.add("(11 kv-2)+(33 kv-1)+(LT-1)");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, fiveCircuits);
                        sp_diffvoltage.setAdapter(adapter);
                    }*/

                    break;
                case R.id.sp_circutetype:
                    circutetype = sp_circutetype.getSelectedItem().toString();
                    if ("Multi Voltage".equalsIgnoreCase(circutetype)) {
                        lyt_diffvoltage.setVisibility(View.VISIBLE);
                        sp_diffvoltage.setSelection(0);
                    } else {
                        lyt_diffvoltage.setVisibility(View.GONE);
                        sp_diffvoltage.setSelection(0);
                        diffvoltage = "";
                    }
                    break;
                case R.id.sp_diffvoltage:
                    diffvoltage = sp_diffvoltage.getSelectedItem().toString();

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
                        lay_ab_cable_main.setVisibility(View.GONE);
                        clearAllDataFileds();
                    } else if ("UnderGround".equalsIgnoreCase(lineType)) {
                        lyt_underground.setVisibility(View.VISIBLE);
                        lyt_overhead.setVisibility(View.GONE);
                        lay_ab_cable_main.setVisibility(View.GONE);
                        clearAllDataFileds();
                    } else if ("AB Cable/Covered Conductor".equalsIgnoreCase(lineType)) {
                        lyt_underground.setVisibility(View.GONE);
                        lyt_overhead.setVisibility(View.GONE);
                        lay_ab_cable_main.setVisibility(View.VISIBLE);
                        clearAllDataFileds();
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
                    sp_rmu.setSelection(0);
                    if ("RMU".equalsIgnoreCase(straight_rmu)) {
                        lyt_rmu.setVisibility(View.VISIBLE);
                        lyt_underpolestructure.setVisibility(View.GONE);
                        lyt_ugpole.setVisibility(View.GONE);
                        lay_ug_ht_service_number.setVisibility(View.GONE);
                        lay_ug_auto_reclosure.setVisibility(View.GONE);
                        lay_ug_line_capacitor.setVisibility(View.GONE);
                        lay_ug_dtr_pole_main.setVisibility(View.GONE);
                        lay_ug_no_of_ab_switches.setVisibility(View.GONE);
                        clearSelecteAfterChangeTextFiledsDTR();
                    } else if ("Structure".equalsIgnoreCase(straight_rmu)) {
                        lyt_underpolestructure.setVisibility(View.VISIBLE);
                        lyt_ugpole.setVisibility(View.GONE);
                        lyt_rmu.setVisibility(View.GONE);
                        lay_ug_ht_service_number.setVisibility(View.GONE);
                        lay_ug_auto_reclosure.setVisibility(View.GONE);
                        lay_ug_line_capacitor.setVisibility(View.GONE);
                        lay_ug_dtr_pole_main.setVisibility(View.GONE);
                        lay_ug_no_of_ab_switches.setVisibility(View.GONE);
                        clearSelecteAfterChangeTextFiledsDTR();
                    } else if ("Pole".equalsIgnoreCase(straight_rmu)) {
                        lyt_ugpole.setVisibility(View.VISIBLE);
                        lyt_rmu.setVisibility(View.GONE);
                        lay_ug_ht_service_number.setVisibility(View.GONE);
                        lay_ug_auto_reclosure.setVisibility(View.GONE);
                        lay_ug_line_capacitor.setVisibility(View.GONE);
                        lyt_underpolestructure.setVisibility(View.GONE);
                        lay_ug_dtr_pole_main.setVisibility(View.GONE);
                        lay_ug_no_of_ab_switches.setVisibility(View.GONE);
                        clearSelecteAfterChangeTextFiledsDTR();
                    } else if ("HT Service".equalsIgnoreCase(straight_rmu)) {
                        lyt_rmu.setVisibility(View.GONE);
                        lyt_underpolestructure.setVisibility(View.GONE);
                        lyt_ugpole.setVisibility(View.GONE);
                        lay_ug_ht_service_number.setVisibility(View.VISIBLE);
                        lay_ug_dtr_pole_main.setVisibility(View.GONE);
                        lay_ug_auto_reclosure.setVisibility(View.GONE);
                        lay_ug_line_capacitor.setVisibility(View.GONE);
                        lay_ug_no_of_ab_switches.setVisibility(View.GONE);
                        //List<String> ug_ht_servicenoList = arcGISDatabase.getComplaint("SELECT DISTINCT FS_CONSUMER_NUMBER || '~' || FS_CONSUMER_NAME FROM FEEDER_SERVICE_MASTER_TABLE");
                        List<String> ug_ht_servicenoList = arcGISDatabase.getComplaint("SELECT DISTINCT FS_CONSUMER_NUMBER || '~' || FS_CONSUMER_NAME FROM INCREMENTAL_FEEDER_SERVICE_MASTER_TABLE");
                        ug_ht_servicenoList.add("Select");
                        Collections.reverse(ug_ht_servicenoList);
                        ArrayAdapter adapterMake_ug = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, ug_ht_servicenoList);
                        auto_ug_ht_service_number.setAdapter(adapterMake_ug);
                        auto_ug_ht_service_number.setThreshold(1);
                        clearSelecteAfterChangeTextFiledsDTR();
                    } else if ("Line Capacitor".equalsIgnoreCase(straight_rmu)) {
                        lyt_rmu.setVisibility(View.GONE);
                        lyt_underpolestructure.setVisibility(View.GONE);
                        lyt_ugpole.setVisibility(View.GONE);
                        lay_ug_ht_service_number.setVisibility(View.GONE);
                        lay_ug_auto_reclosure.setVisibility(View.GONE);
                        lay_ug_line_capacitor.setVisibility(View.VISIBLE);
                        lay_ug_dtr_pole_main.setVisibility(View.GONE);
                        lay_ug_no_of_ab_switches.setVisibility(View.GONE);
                        clearSelecteAfterChangeTextFiledsDTR();
                    } else if ("Auto Reclosure".equalsIgnoreCase(straight_rmu)) {
                        lyt_rmu.setVisibility(View.GONE);
                        lyt_underpolestructure.setVisibility(View.GONE);
                        lyt_ugpole.setVisibility(View.GONE);
                        lay_ug_ht_service_number.setVisibility(View.GONE);
                        lay_ug_auto_reclosure.setVisibility(View.VISIBLE);
                        lay_ug_line_capacitor.setVisibility(View.GONE);
                        lay_ug_dtr_pole_main.setVisibility(View.GONE);
                        lay_ug_no_of_ab_switches.setVisibility(View.GONE);
                        clearSelecteAfterChangeTextFiledsDTR();
                    } else if ("DTR".equalsIgnoreCase(straight_rmu)) {
                        clearSelecteAfterChangeTextFiledsDTR();
                        lay_ug_dtr_pole_main.setVisibility(View.VISIBLE);
                        lay_ug_ht_service_number.setVisibility(View.GONE);
                        lay_ug_auto_reclosure.setVisibility(View.GONE);

                        lay_ug_line_capacitor.setVisibility(View.GONE);
                        lyt_rmu.setVisibility(View.GONE);
                        lyt_underpolestructure.setVisibility(View.GONE);
                        lyt_ugpole.setVisibility(View.GONE);
                        lay_ug_no_of_ab_switches.setVisibility(View.GONE);
                        //dtr_ug_sectionList = arcGISDatabase.getAll_DtrStructureSubStationMaster();
                        dtr_ug_sectionList = arcGISDatabase.getIncrementalAll_DtrStructureSubStationMaster();
                        dtr_ug_sectionList.add("Select");
                        Collections.reverse(dtr_ug_sectionList);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c,
                                android.R.layout.simple_spinner_dropdown_item, dtr_ug_sectionList);
                        sp_ug_section_name_dtr_pole.setAdapter(adapter);

                    } else if ("AB-Pole".equalsIgnoreCase(straight_rmu)) {
                        lyt_rmu.setVisibility(View.GONE);
                        lyt_underpolestructure.setVisibility(View.GONE);
                        lyt_ugpole.setVisibility(View.GONE);
                        lay_ug_ht_service_number.setVisibility(View.GONE);
                        lay_ug_auto_reclosure.setVisibility(View.GONE);
                        lay_ug_line_capacitor.setVisibility(View.GONE);
                        lay_ug_dtr_pole_main.setVisibility(View.GONE);
                        lay_ug_no_of_ab_switches.setVisibility(View.VISIBLE);
                        clearSelecteAfterChangeTextFiledsDTR();
                    } else {
                        clearSelecteAfterChangeTextFiledsDTR();
                        sp_rmu.setSelection(0);
                        sp__underpolestructure.setSelection(0);
                        lyt_rmu.setVisibility(View.GONE);
                        lyt_underpolestructure.setVisibility(View.GONE);
                        lyt_ugpole.setVisibility(View.GONE);
                        lay_ug_ht_service_number.setVisibility(View.GONE);
                        lay_ug_auto_reclosure.setVisibility(View.GONE);
                        lay_ug_line_capacitor.setVisibility(View.GONE);
                        lay_ug_dtr_pole_main.setVisibility(View.GONE);
                        lay_ug_no_of_ab_switches.setVisibility(View.GONE);
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
                    sp_ugpoleheight.setSelection(0);
                    if ("PSCC".equalsIgnoreCase(ugpoletype)) {
                        List<String> ugpsccList = new ArrayList<>();
                        ugpsccList.add("Select");
                        ugpsccList.add("8.0");
                        ugpsccList.add("9.1");
                        ugpsccList.add("11");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, ugpsccList);
                        sp_ugpoleheight.setAdapter(adapter);
                    } else if ("SPUN".equalsIgnoreCase(ugpoletype)) {
                        List<String> ugspunList = new ArrayList<>();
                        ugspunList.add("Select");
                        ugspunList.add("9.5");
                        ugspunList.add("11");
                        ugspunList.add("12.5");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, ugspunList);
                        sp_ugpoleheight.setAdapter(adapter);

                    } else if ("RAIL".equalsIgnoreCase(ugpoletype)) {
                        List<String> ugrailList = new ArrayList<>();
                        ugrailList.add("Select");
                        ugrailList.add("9.5");
                        ugrailList.add("11");
                        ugrailList.add("12.5");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, ugrailList);
                        sp_ugpoleheight.setAdapter(adapter);

                    } else if ("RS JOIST".equalsIgnoreCase(ugpoletype)) {
                        List<String> ugrssJoistList = new ArrayList<>();
                        ugrssJoistList.add("Select");
                        /*rssJoistList.add("8");*/
                        ugrssJoistList.add("9.1");
                        ugrssJoistList.add("11");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, ugrssJoistList);
                        sp_ugpoleheight.setAdapter(adapter);
                    } else if ("TOWER".equalsIgnoreCase(ugpoletype)) {
                        List<String> ugtowertList = new ArrayList<>();
                        ugtowertList.add("Select");
                        ugtowertList.add("M+3");
                        ugtowertList.add("M+6");
                        ugtowertList.add("M+9");
                        ugtowertList.add("M+12");
                        ugtowertList.add("R+6");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, ugtowertList);
                        sp_ugpoleheight.setAdapter(adapter);
                    } else if ("Tubular Pole".equalsIgnoreCase(ugpoletype)) {
                        List<String> ugtubularList = new ArrayList<>();
                        ugtubularList.add("Select");
                        ugtubularList.add("9.1");
                        ugtubularList.add("11");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, ugtubularList);
                        sp_ugpoleheight.setAdapter(adapter);
                    }

                    break;

                case R.id.sp_ugpoleheight:
                    ugpoleheight = sp_ugpoleheight.getSelectedItem().toString();

                    break;

                case R.id.sp_ugpolesupport:
                    ugpolesupport = sp_ugpolesupport.getSelectedItem().toString();

                    break;


                case R.id.sp_ugtapingpoint:
                    ugtapingpoint = sp_ugtapingpoint.getSelectedItem().toString();
                    if ("Yes".equalsIgnoreCase(ugtapingpoint)) {
                        lyt_ugtapping.setVisibility(View.VISIBLE);
                    } else {
                        lyt_ugtapping.setVisibility(View.GONE);
                        ugnooftappings = "";
                        sp_ugnooftappings.setSelection(0);
                        edt_ugtappinglocation.setText("");
                    }

                    break;
                case R.id.sp_ugnooftappings:
                    ugnooftappings = sp_ugnooftappings.getSelectedItem().toString();

                    break;


                case R.id.sp_nooftappings:
                    nooftappings = sp_nooftappings.getSelectedItem().toString();
                    break;

                case R.id.sp_undergisendpole:
                    if ("No".equalsIgnoreCase(sp_undergisendpole.getSelectedItem().toString())) {
                        undergisendpole = "N";
                    } else {
                        undergisendpole = "Y";
                    }
                    break;

                case R.id.sp_ug_section_name_dtr_pole:
                    dtr_pole_ug_section_name = sp_ug_section_name_dtr_pole.getSelectedItem().toString();
                    if ("Select".equalsIgnoreCase(dtr_pole_ug_section_name)) {

                    } else {
                        //dtr_ug_structureList = arcGISDatabase.getComplaint(String.format("SELECT DISTINCT DTR_STRUCTURE_CODE || '~' || DTR_LOCATION FROM DTR_MASTER_TABLE WHERE DTR_SECTION_NAME='%s'", dtr_pole_ug_section_name));
                        dtr_ug_structureList = arcGISDatabase.getComplaint(String.format("SELECT DISTINCT DTR_STRUCTURE_CODE || '~' || DTR_LOCATION FROM INCREMENTAL_DTR_MASTER_TABLE WHERE DTR_SECTION_NAME='%s'", dtr_pole_ug_section_name));
                        dtr_ug_structureList.add("Select");
                        Collections.reverse(dtr_ug_structureList);
                        ArrayAdapter adapterMake_ug = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, dtr_ug_structureList);
                        auto_ug_dtr_structure_dtr_pole.setAdapter(adapterMake_ug);
                        auto_ug_dtr_structure_dtr_pole.setThreshold(1);
                    }
                    break;

                case R.id.sp_oh_section_name_dtr_pole:
                    dtr_pole_oh_section_name = sp_oh_section_name_dtr_pole.getSelectedItem().toString();
                    if ("Select".equalsIgnoreCase(dtr_pole_ug_section_name)) {

                    } else {
                        // dtr_ug_structureList=arcGISDatabase.getAll_DtrStructureCodeMaster(ug_section_id_spilt[0]);

                        // dtr_oh_structureList = arcGISDatabase.getComplaint(String.format("SELECT DISTINCT DTR_STRUCTURE_CODE || '~' || DTR_LOCATION FROM DTR_MASTER_TABLE WHERE DTR_SECTION_NAME='%s'", dtr_pole_oh_section_name));
                        dtr_oh_structureList = arcGISDatabase.getComplaint(String.format("SELECT DISTINCT DTR_STRUCTURE_CODE || '~' || DTR_LOCATION FROM INCREMENTAL_DTR_MASTER_TABLE WHERE DTR_SECTION_NAME='%s'", dtr_pole_oh_section_name));
                        dtr_oh_structureList.add("Select");
                        Collections.reverse(dtr_oh_structureList);
                        ArrayAdapter adapterMake_ug = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, dtr_oh_structureList);
                        auto_oh_dtr_structure_dtr_pole.setAdapter(adapterMake_ug);
                        auto_oh_dtr_structure_dtr_pole.setThreshold(1);
                    }
                    break;
                case R.id.sp_ug_phase_dtr_pole:
                    sp_ug_phase_dtr_pole_str = sp_ug_phase_dtr_pole.getSelectedItem().toString();

                    if ("I".equalsIgnoreCase(sp_ug_phase_dtr_pole_str)) {
                        List<String> ug_phase1 = new ArrayList<>();
                        ug_phase1.add("Select");
                        ug_phase1.add("5 kva");
                        ug_phase1.add("10 kva");
                        ug_phase1.add("15 kva");
                        ug_phase1.add("16 kva");
                        ug_phase1.add("25 kva");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, ug_phase1);
                        sp_ug_capacity_dtr_pole.setAdapter(adapter);
                    } else if ("II".equalsIgnoreCase(sp_ug_phase_dtr_pole_str)) {
                        List<String> ug_phase2 = new ArrayList<>();
                        ug_phase2.add("Select");
                        ug_phase2.add("10 kva");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, ug_phase2);
                        sp_ug_capacity_dtr_pole.setAdapter(adapter);
                    } else if ("III".equalsIgnoreCase(sp_ug_phase_dtr_pole_str)) {
                        List<String> ug_phase3 = new ArrayList<>();
                        ug_phase3.add("Select");
                        ug_phase3.add("16 kva");
                        ug_phase3.add("25 kva");
                        ug_phase3.add("40 kva");
                        ug_phase3.add("50 kva");
                        ug_phase3.add("63 kva");
                        ug_phase3.add("75 kva");
                        ug_phase3.add("100 kva");
                        ug_phase3.add("160 kva");
                        ug_phase3.add("200 kva");
                        ug_phase3.add("250 kva");
                        ug_phase3.add("315 kva");
                        ug_phase3.add("500 kva");
                        ug_phase3.add("1000 kva");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, ug_phase3);
                        sp_ug_capacity_dtr_pole.setAdapter(adapter);
                    }
                    break;
                case R.id.sp_oh_voltage_level:
                    sp_oh_voltage_level_str = sp_oh_voltage_level.getSelectedItem().toString();
                    if ("6.3 KV".equalsIgnoreCase(sp_oh_voltage_level_str)) {
                        List<String> oh_phase_I = new ArrayList<>();
                        oh_phase_I.add("Select");
                        oh_phase_I.add("I");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, oh_phase_I);
                        sp_oh_phase_dtr_pole.setAdapter(adapter);
                    }
                    break;

                case R.id.sp_oh_phase_dtr_pole:
                    sp_oh_phase_dtr_pole_str = sp_oh_phase_dtr_pole.getSelectedItem().toString();

                    if ("I".equalsIgnoreCase(sp_oh_phase_dtr_pole_str)) {
                        List<String> oh_phase1 = new ArrayList<>();
                        oh_phase1.add("Select");
                        oh_phase1.add("5 kva");
                        oh_phase1.add("10 kva");
                        oh_phase1.add("15 kva");
                        oh_phase1.add("16 kva");
                        oh_phase1.add("25 kva");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, oh_phase1);
                        sp_oh_capacity_dtr_pole.setAdapter(adapter);
                    } else if ("II".equalsIgnoreCase(sp_oh_phase_dtr_pole_str)) {
                        List<String> oh_phase2 = new ArrayList<>();
                        oh_phase2.add("Select");
                        oh_phase2.add("10 kva");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, oh_phase2);
                        sp_oh_capacity_dtr_pole.setAdapter(adapter);
                    } else if ("III".equalsIgnoreCase(sp_oh_phase_dtr_pole_str)) {
                        List<String> oh_phase3 = new ArrayList<>();
                        oh_phase3.add("Select");
                        oh_phase3.add("16 kva");
                        oh_phase3.add("25 kva");
                        oh_phase3.add("40 kva");
                        oh_phase3.add("50 kva");
                        oh_phase3.add("63 kva");
                        oh_phase3.add("75 kva");
                        oh_phase3.add("100 kva");
                        oh_phase3.add("160 kva");
                        oh_phase3.add("200 kva");
                        oh_phase3.add("250 kva");
                        oh_phase3.add("315 kva");
                        oh_phase3.add("500 kva");
                        oh_phase3.add("1000 kva");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, oh_phase3);
                        sp_oh_capacity_dtr_pole.setAdapter(adapter);
                    }
                    break;

                //ab cable spinner start
                case R.id.sp_abc_poletype:
                    sp_abc_pole_type_str = sp_abc_poletype.getSelectedItem().toString();
                    sp_abc_poleheight.setSelection(0);
                    if ("PSCC".equalsIgnoreCase(sp_abc_pole_type_str)) {
                        List<String> psccList_abc = new ArrayList<>();
                        psccList_abc.add("Select");
                        psccList_abc.add("8.0");
                        psccList_abc.add("9.1");
                        psccList_abc.add("11");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, psccList_abc);
                        sp_abc_poleheight.setAdapter(adapter);
                    } else if ("SPUN".equalsIgnoreCase(sp_abc_pole_type_str)) {
                        List<String> spunList_abc = new ArrayList<>();
                        spunList_abc.add("Select");
                        spunList_abc.add("9.5");
                        spunList_abc.add("11");
                        spunList_abc.add("12.5");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, spunList_abc);
                        sp_abc_poleheight.setAdapter(adapter);
                    } else if ("RAIL".equalsIgnoreCase(sp_abc_pole_type_str)) {
                        List<String> rail_abc = new ArrayList<>();
                        rail_abc.add("Select");
                        rail_abc.add("9.5");
                        rail_abc.add("11");
                        rail_abc.add("12.5");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, rail_abc);
                        sp_abc_poleheight.setAdapter(adapter);
                    } else if ("RS JOIST".equalsIgnoreCase(sp_abc_pole_type_str)) {
                        List<String> rs_joist_abc = new ArrayList<>();
                        rs_joist_abc.add("Select");
                        rs_joist_abc.add("9.1");
                        rs_joist_abc.add("11");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, rs_joist_abc);
                        sp_abc_poleheight.setAdapter(adapter);
                    } else if ("TOWER".equalsIgnoreCase(sp_abc_pole_type_str)) {
                        List<String> tower_abc = new ArrayList<>();
                        tower_abc.add("Select");
                        tower_abc.add("M+3");
                        tower_abc.add("M+6");
                        tower_abc.add("M+9");
                        tower_abc.add("M+12");
                        tower_abc.add("R+6");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, tower_abc);
                        sp_abc_poleheight.setAdapter(adapter);
                    } else if ("Tubular Pole".equalsIgnoreCase(sp_abc_pole_type_str)) {
                        List<String> truble_pole_abc = new ArrayList<>();
                        truble_pole_abc.add("Select");
                        truble_pole_abc.add("9.1");
                        truble_pole_abc.add("11");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, truble_pole_abc);
                        sp_abc_poleheight.setAdapter(adapter);
                    }
                case R.id.sp_abc_tapingpoint:
                    abc_tapingpoint = sp_abc_tapingpoint.getSelectedItem().toString();
                    if ("Yes".equalsIgnoreCase(abc_tapingpoint)) {
                        lyt_abc_tapping.setVisibility(View.VISIBLE);
                    } else {
                        lyt_abc_tapping.setVisibility(View.GONE);
                        abc_nooftappings = "";
                        sp_abc_nooftappings.setSelection(0);
                        edt_abc_tappinglocation.setText("");
                    }

                    break;
                case R.id.sp_abc_dergisendpole:
                    if ("No".equalsIgnoreCase(sp_abc_dergisendpole.getSelectedItem().toString())) {
                        abc_isendpole = "N";
                    } else {
                        abc_isendpole = "Y";
                    }
                    break;
                case R.id.sp_abc_noofcircuit:
                    abc_noofcircuitStr = sp_abc_noofcircuit.getSelectedItem().toString();

                    break;
                case R.id.sp_abc_straight_rmu:
                    abc_straight_rmuStr = sp_abc_straight_rmu.getSelectedItem().toString();
                    sp_abc_rmu.setSelection(0);
                    if ("RMU".equalsIgnoreCase(abc_straight_rmuStr)) {
                        lyt_abc_rmu.setVisibility(View.VISIBLE);
                        lyt_abc_derpolestructure.setVisibility(View.GONE);
                        lyt_abc_pole.setVisibility(View.GONE);
                        lay_abc_ht_service_number.setVisibility(View.GONE);
                        lay_abc_auto_reclosure.setVisibility(View.GONE);
                        lay_abc_line_capacitor.setVisibility(View.GONE);
                        lay_abc_dtr_pole_main.setVisibility(View.GONE);
                        lay_abc_no_of_ab_switches.setVisibility(View.GONE);
                        clearSelecteAfterChangeTextFiledsDTR();
                    } else if ("Structure".equalsIgnoreCase(abc_straight_rmuStr)) {
                        lyt_abc_derpolestructure.setVisibility(View.VISIBLE);
                        lyt_abc_pole.setVisibility(View.GONE);
                        lyt_abc_rmu.setVisibility(View.GONE);
                        lay_abc_ht_service_number.setVisibility(View.GONE);
                        lay_abc_auto_reclosure.setVisibility(View.GONE);
                        lay_abc_line_capacitor.setVisibility(View.GONE);
                        lay_abc_dtr_pole_main.setVisibility(View.GONE);
                        lay_abc_no_of_ab_switches.setVisibility(View.GONE);
                        clearSelecteAfterChangeTextFiledsDTR();
                    } else if ("Pole".equalsIgnoreCase(abc_straight_rmuStr)) {
                        lyt_abc_pole.setVisibility(View.VISIBLE);
                        lyt_abc_rmu.setVisibility(View.GONE);
                        lay_abc_ht_service_number.setVisibility(View.GONE);
                        lay_abc_auto_reclosure.setVisibility(View.GONE);
                        lay_abc_line_capacitor.setVisibility(View.GONE);
                        lyt_abc_derpolestructure.setVisibility(View.GONE);
                        lay_abc_dtr_pole_main.setVisibility(View.GONE);
                        lay_abc_no_of_ab_switches.setVisibility(View.GONE);
                        clearSelecteAfterChangeTextFiledsDTR();
                    } else if ("HT Service".equalsIgnoreCase(abc_straight_rmuStr)) {
                        lyt_abc_rmu.setVisibility(View.GONE);
                        lyt_abc_derpolestructure.setVisibility(View.GONE);
                        lyt_abc_pole.setVisibility(View.GONE);
                        lay_abc_ht_service_number.setVisibility(View.VISIBLE);
                        lay_abc_dtr_pole_main.setVisibility(View.GONE);
                        lay_abc_auto_reclosure.setVisibility(View.GONE);
                        lay_abc_line_capacitor.setVisibility(View.GONE);
                        lay_abc_no_of_ab_switches.setVisibility(View.GONE);
                        //List<String> abc_ht_servicenoList = arcGISDatabase.getComplaint("SELECT DISTINCT FS_CONSUMER_NUMBER || '~' || FS_CONSUMER_NAME FROM FEEDER_SERVICE_MASTER_TABLE");
                        List<String> abc_ht_servicenoList = arcGISDatabase.getComplaint("SELECT DISTINCT FS_CONSUMER_NUMBER || '~' || FS_CONSUMER_NAME FROM INCREMENTAL_FEEDER_SERVICE_MASTER_TABLE");
                        abc_ht_servicenoList.add("Select");
                        Collections.reverse(abc_ht_servicenoList);
                        ArrayAdapter adapterMake_ug = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, abc_ht_servicenoList);
                        auto_abc_ht_service_number.setAdapter(adapterMake_ug);
                        auto_abc_ht_service_number.setThreshold(1);
                        clearSelecteAfterChangeTextFiledsDTR();
                    } else if ("Line Capacitor".equalsIgnoreCase(abc_straight_rmuStr)) {
                        lyt_abc_rmu.setVisibility(View.GONE);
                        lyt_abc_derpolestructure.setVisibility(View.GONE);
                        lyt_abc_pole.setVisibility(View.GONE);
                        lay_abc_ht_service_number.setVisibility(View.GONE);
                        lay_abc_auto_reclosure.setVisibility(View.GONE);
                        lay_abc_line_capacitor.setVisibility(View.VISIBLE);
                        lay_abc_dtr_pole_main.setVisibility(View.GONE);
                        lay_abc_no_of_ab_switches.setVisibility(View.GONE);
                        clearSelecteAfterChangeTextFiledsDTR();
                    } else if ("Auto Reclosure".equalsIgnoreCase(abc_straight_rmuStr)) {
                        lyt_abc_rmu.setVisibility(View.GONE);
                        lyt_abc_derpolestructure.setVisibility(View.GONE);
                        lyt_abc_pole.setVisibility(View.GONE);
                        lay_abc_ht_service_number.setVisibility(View.GONE);
                        lay_abc_auto_reclosure.setVisibility(View.VISIBLE);
                        lay_abc_line_capacitor.setVisibility(View.GONE);
                        lay_abc_dtr_pole_main.setVisibility(View.GONE);
                        lay_abc_no_of_ab_switches.setVisibility(View.GONE);
                        clearSelecteAfterChangeTextFiledsDTR();
                    } else if ("DTR".equalsIgnoreCase(abc_straight_rmuStr)) {
                        clearSelecteAfterChangeTextFiledsDTR();
                        lay_abc_dtr_pole_main.setVisibility(View.VISIBLE);
                        lyt_abc_rmu.setVisibility(View.GONE);
                        lyt_abc_derpolestructure.setVisibility(View.GONE);
                        lyt_abc_pole.setVisibility(View.GONE);
                        lay_abc_ht_service_number.setVisibility(View.GONE);
                        lay_abc_auto_reclosure.setVisibility(View.GONE);
                        lay_abc_line_capacitor.setVisibility(View.GONE);
                        lay_abc_no_of_ab_switches.setVisibility(View.GONE);
                        ArrayList<String> dtr_abc_sectionList = arcGISDatabase.getIncrementalAll_DtrStructureSubStationMaster();
                        dtr_abc_sectionList.add("Select");
                        Collections.reverse(dtr_abc_sectionList);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c,
                                android.R.layout.simple_spinner_dropdown_item, dtr_abc_sectionList);
                        sp_abc_section_name_dtr_pole.setAdapter(adapter);

                    } else if ("AB-Pole".equalsIgnoreCase(abc_straight_rmuStr)) {
                        lyt_abc_rmu.setVisibility(View.GONE);
                        lyt_abc_derpolestructure.setVisibility(View.GONE);
                        lyt_abc_pole.setVisibility(View.GONE);
                        lay_abc_ht_service_number.setVisibility(View.GONE);
                        lay_abc_auto_reclosure.setVisibility(View.GONE);
                        lay_abc_line_capacitor.setVisibility(View.GONE);
                        lay_abc_dtr_pole_main.setVisibility(View.GONE);
                        lay_abc_no_of_ab_switches.setVisibility(View.VISIBLE);
                        clearSelecteAfterChangeTextFiledsDTR();
                    } else {
                        sp_abc_rmu.setSelection(0);
                        sp_abc_derpolestructure.setSelection(0);
                        lyt_abc_rmu.setVisibility(View.GONE);
                        lyt_abc_derpolestructure.setVisibility(View.GONE);
                        lyt_abc_pole.setVisibility(View.GONE);
                        lay_abc_ht_service_number.setVisibility(View.GONE);
                        lay_abc_auto_reclosure.setVisibility(View.GONE);
                        lay_abc_line_capacitor.setVisibility(View.GONE);
                        lay_abc_dtr_pole_main.setVisibility(View.GONE);
                        lay_abc_no_of_ab_switches.setVisibility(View.GONE);
                        rmu = "";
                        //nooftappings="";
                    }
                    break;
                case R.id.sp_abc_rmu:
                    sp_abc_rmuStr = sp_abc_rmu.getSelectedItem().toString();
                    break;
                case R.id.sp_abc_derpolestructure:
                    sp_abc_derpolestructureStr = sp_abc_derpolestructure.getSelectedItem().toString();
                    break;
                case R.id.sp_abc_polesupport:
                    sp_abc_polesupportStr = sp_abc_polesupport.getSelectedItem().toString();
                    break;

                case R.id.sp_abc_section_name_dtr_pole:
                    dtr_pole_abc_section_name = sp_abc_section_name_dtr_pole.getSelectedItem().toString();
                    if ("Select".equalsIgnoreCase(dtr_pole_abc_section_name)) {

                    } else {
                        // List<String> dtr_abc_structureList = arcGISDatabase.getComplaint(String.format("SELECT DISTINCT DTR_STRUCTURE_CODE || '~' || DTR_LOCATION FROM DTR_MASTER_TABLE WHERE DTR_SECTION_NAME='%s'", dtr_pole_abc_section_name));
                        List<String> dtr_abc_structureList = arcGISDatabase.getComplaint(String.format("SELECT DISTINCT DTR_STRUCTURE_CODE || '~' || DTR_LOCATION FROM INCREMENTAL_DTR_MASTER_TABLE WHERE DTR_SECTION_NAME='%s'", dtr_pole_abc_section_name));
                        dtr_abc_structureList.add("Select");
                        Collections.reverse(dtr_abc_structureList);
                        ArrayAdapter adapterMake_ug = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, dtr_abc_structureList);
                        auto_abc_dtr_structure_dtr_pole.setAdapter(adapterMake_ug);
                        auto_abc_dtr_structure_dtr_pole.setThreshold(1);
                    }
                    break;
                case R.id.sp_abc_phase_dtr_pole:
                    sp_abc_phase_dtr_pole_str = sp_abc_phase_dtr_pole.getSelectedItem().toString();
                    if ("I".equalsIgnoreCase(sp_abc_phase_dtr_pole_str)) {
                        List<String> abc_phase1 = new ArrayList<>();
                        abc_phase1.add("Select");
                        abc_phase1.add("5 kva");
                        abc_phase1.add("10 kva");
                        abc_phase1.add("15 kva");
                        abc_phase1.add("16 kva");
                        abc_phase1.add("25 kva");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, abc_phase1);
                        sp_abc_capacity_dtr_pole.setAdapter(adapter);
                    } else if ("II".equalsIgnoreCase(sp_abc_phase_dtr_pole_str)) {
                        List<String> abc_phase2 = new ArrayList<>();
                        abc_phase2.add("Select");
                        abc_phase2.add("10 kva");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, abc_phase2);
                        sp_abc_capacity_dtr_pole.setAdapter(adapter);
                    } else if ("III".equalsIgnoreCase(sp_abc_phase_dtr_pole_str)) {
                        List<String> abc_phase3 = new ArrayList<>();
                        abc_phase3.add("Select");
                        abc_phase3.add("16 kva");
                        abc_phase3.add("25 kva");
                        abc_phase3.add("40 kva");
                        abc_phase3.add("50 kva");
                        abc_phase3.add("63 kva");
                        abc_phase3.add("75 kva");
                        abc_phase3.add("100 kva");
                        abc_phase3.add("160 kva");
                        abc_phase3.add("200 kva");
                        abc_phase3.add("250 kva");
                        abc_phase3.add("315 kva");
                        abc_phase3.add("500 kva");
                        abc_phase3.add("1000 kva");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, abc_phase3);
                        sp_abc_capacity_dtr_pole.setAdapter(adapter);
                    }
                    break;
                case R.id.sp_abc_nooftappings:
                    abc_nooftappings = sp_abc_nooftappings.getSelectedItem().toString();

                    break;
                case R.id.sp_abc_cablesize:
                    abc_cablesizeStr = sp_abc_cablesize.getSelectedItem().toString();

                    break;
                case R.id.sp_abc_poleheight:
                    abc_poleheightStr = sp_abc_poleheight.getSelectedItem().toString();

                    break;

                case R.id.sp_ug_wosurveycompleted:
                    ug_wosurveycompleted = sp_ug_wosurveycompleted.getSelectedItem().toString();
                    break;

                case R.id.sp_oh_wosurveycompleted:
                    oh_wosurveycompleted = sp_oh_wosurveycompleted.getSelectedItem().toString();
                    break;


                case R.id.sp_abc_wosurveycompleted:
                    abc_wosurveycompleted = sp_abc_wosurveycompleted.getSelectedItem().toString();
                    break;

                case R.id.sp_Survey_type:
                    sp_Survey_type_string = sp_Survey_type.getSelectedItem().toString();
                    break;

                default:

                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearAllDataFileds() {
        straight_rmu = "";
        sp_straight_rmu.setSelection(0);
        //Over Head
        sp_conductorsize.setSelection(0);
        sp_poletype.setSelection(0);
        sp_polesupport.setSelection(0);
        sp_poleconfiguration.setSelection(0);
        sp_poleheight.setSelection(0);
        sp_oh_section_name_dtr_pole.setSelection(0);
        sp_oh_ab_switch_dtr_pole.setSelection(0);
        sp_oh_phase_dtr_pole.setSelection(0);
        sp_oh_phase_dtr_pole_str = "";
        sp_oh_dtr_structure_type_dtr_pole.setSelection(0);
        sp_oh_line_capacitor.setSelection(0);
        sp_oh_capacity_dtr_pole.setSelection(0);
        auto_oh_dtr_structure_dtr_pole.setText("");
        auto_oh_dtr_structure_dtr_pole.setText("");
        ed_oh_location_name_dtr_pole.setText("");
        ed_oh_serial_number_dtr_pole.setText("");
        sp_oh_no_of_lt_circuits.setSelection(0);
        edt_polepaintingcode.setText("");
        edt_tappinglocation.setText("");
        edt_polepaintingcode.setText("");
        edt_remarks_overhead.setText("");
        edt_ugtappinglocation.setText("");
        edt_remarks_underground.setText("");
        edt_ugpolepaintingcode.setText("");

        //Under Ground
        sp_ug_section_name_dtr_pole.setSelection(0);
        sp_ug_ab_switch_dtr_pole.setSelection(0);
        sp_ug_phase_dtr_pole.setSelection(0);
        sp_ug_phase_dtr_pole_str = "";
        sp_ug_dtr_structure_type_dtr_pole.setSelection(0);
        sp_ug_line_capacitor.setSelection(0);
        sp_ug_capacity_dtr_pole.setSelection(0);
        auto_ug_dtr_structure_dtr_pole.setText("");
        auto_ug_dtr_structure_dtr_pole.setText("");
        sp_rmu.setSelection(0);
        sp__underpolestructure.setSelection(0);
        ed_ug_location_name_dtr_pole.setText("");
        sp_noofcircuit_underg.setSelection(0);
        ed_ug_serial_number_dtr_pole.setText("");
        sp_ug_no_of_lt_circuits.setSelection(0);

        sp_ugpoleheight.setSelection(0);
        sp_oh_no_of_lt_circuits.setSelection(0);
        sp_oh_is_lt_ckts_available.setSelection(0);
        sp_oh_dtr_location_type.setSelection(0);
        sp_oh_type_of_load.setSelection(0);
        sp_noofcircuit.setSelection(0);
        sp_diffvoltage.setSelection(0);
        sp_ismulticircute.setSelection(0);
        sp_Survey_type.setSelection(0);
    }

    private void clearTextAfterSelectedOtherFiled_OH() {
        sp_oh_section_name_dtr_pole.setSelection(0);
        sp_oh_ab_switch_dtr_pole.setSelection(0);
        sp_oh_phase_dtr_pole.setSelection(0);
        sp_oh_phase_dtr_pole_str = "";
        sp_oh_dtr_structure_type_dtr_pole.setSelection(0);
        sp_oh_line_capacitor.setSelection(0);
        sp_oh_capacity_dtr_pole.setSelection(0);
        auto_oh_dtr_structure_dtr_pole.setText("");
        auto_oh_dtr_structure_dtr_pole.setText("");
        auto_oh_make_dtr_pole.setText("");
        ed_oh_location_name_dtr_pole.setText("");
        ed_oh_serial_number_dtr_pole.setText("");
        sp_oh_no_of_lt_circuits.setSelection(0);
        sp_oh_is_lt_ckts_available.setSelection(0);
        sp_oh_dtr_location_type.setSelection(0);
        sp_oh_type_of_load.setSelection(0);
        auto_oh_ht_service_number.setText("");
        sp_oh_auto_reclosure.setSelection(0);
        sp_Survey_type.setSelection(0);
    }

    private void clearSelecteAfterChangeTextFiledsDTR() {
        sp_ug_section_name_dtr_pole.setSelection(0);
        sp_ug_ab_switch_dtr_pole.setSelection(0);
        sp_ug_phase_dtr_pole.setSelection(0);
        sp_ug_phase_dtr_pole_str = "";
        sp_ug_dtr_structure_type_dtr_pole.setSelection(0);
        sp_ug_line_capacitor.setSelection(0);
        sp_ug_capacity_dtr_pole.setSelection(0);
        auto_ug_dtr_structure_dtr_pole.setText("");
        auto_ug_dtr_structure_dtr_pole.setText("");
        sp_rmu.setSelection(0);
        sp__underpolestructure.setSelection(0);
        auto_ug_make_dtr_pole.setText("");
        ed_ug_location_name_dtr_pole.setText("");
        ed_ug_serial_number_dtr_pole.setText("");
        sp_ug_no_of_lt_circuits.setSelection(0);
        sp_ug_no_of_ab_switches.setSelection(0);
        sp_ug_is_lt_ckts_available.setSelection(0);
        sp_ug_dtr_location_type.setSelection(0);
        sp_ug_type_of_load.setSelection(0);
        auto_ug_ht_service_number.setText("");
        sp_ug_auto_reclosure.setSelection(0);


        //AB CABLE
        sp_abc_section_name_dtr_pole.setSelection(0);
        sp_abc_ab_switch_dtr_pole.setSelection(0);
        sp_abc_phase_dtr_pole.setSelection(0);
        sp_abc_phase_dtr_pole_str = "";
        sp_abc_dtr_structure_type_dtr_pole.setSelection(0);
        sp_abc_line_capacitor.setSelection(0);
        sp_abc_capacity_dtr_pole.setSelection(0);
        auto_abc_dtr_structure_dtr_pole.setText("");
        auto_abc_dtr_structure_dtr_pole.setText("");
        sp_abc_rmu.setSelection(0);
        sp_abc_derpolestructure.setSelection(0);
        auto_abc_make_dtr_pole.setText("");
        ed_abc_location_name_dtr_pole.setText("");
        ed_abc_serial_number_dtr_pole.setText("");
        sp_abc_no_of_lt_circuits.setSelection(0);
        sp_abc_no_of_ab_switches.setSelection(0);
        sp_abc_is_lt_ckts_available.setSelection(0);
        sp_abc_dtr_location_type.setSelection(0);
        sp_abc_type_of_load.setSelection(0);
        auto_abc_ht_service_number.setText("");
        sp_abc_auto_reclosure.setSelection(0);
        sp_Survey_type.setSelection(0);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void overHeadData() {
        try {

            //sp_Survey_type_string
            if ("Select".equalsIgnoreCase(sp_Survey_type_string)){
                Toast.makeText(c, "Select Survey Type", Toast.LENGTH_LONG).show();
            }
            else if ("Select".equalsIgnoreCase(poleType)) {
                Toast.makeText(c, "Select Pole Type", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(poleHeight)) {
                Toast.makeText(c, "Select Pole Height", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(taping_Point)) {
                Toast.makeText(c, "Select Taping_Point ", Toast.LENGTH_LONG).show();
            } else if ("Yes".equalsIgnoreCase(taping_Point) && "Select".equalsIgnoreCase(sp_nooftappings.getSelectedItem().toString())) {
                Toast.makeText(c, "Select No.Of Tappings", Toast.LENGTH_LONG).show();
            } else if ("Yes".equalsIgnoreCase(taping_Point) && edt_tappinglocation.getText().toString().isEmpty()) {
                Toast.makeText(c, "Enter Tapping Location Name", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(conductorsize)) {
                Toast.makeText(c, "Select ConductorSize", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(poleconfiguration)) {
                Toast.makeText(c, "Select PoleConfiguration", Toast.LENGTH_LONG).show();
            } else if ("Yes".equalsIgnoreCase(ismulticircute) && "Select".equalsIgnoreCase(noofcircuit)) {
                Toast.makeText(c, "Select No.Of Circuits", Toast.LENGTH_LONG).show();
            } else if ("Yes".equalsIgnoreCase(ismulticircute) && "Select".equalsIgnoreCase(sp_circutetype.getSelectedItem().toString())) {
                Toast.makeText(c, "Select Circuit Type", Toast.LENGTH_LONG).show();
            } else if ("Yes".equalsIgnoreCase(ismulticircute) && "Multi Voltage".equalsIgnoreCase(circutetype) && "Select".equalsIgnoreCase(sp_diffvoltage.getSelectedItem().toString())) {
                Toast.makeText(c, "Select Diff Voltage", Toast.LENGTH_LONG).show();
            } else {
                if ("DTR Pole".equalsIgnoreCase(poleconfiguration)) {
                    if (sp_oh_section_name_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select Section Name ", Toast.LENGTH_LONG).show();
                    } else if (auto_oh_dtr_structure_dtr_pole.getText().toString().isEmpty()) {
                        Toast.makeText(c, "Select DTR Structure Code ", Toast.LENGTH_LONG).show();
                    } else if (!arcGISDatabase.checkValueIncrementalDTRCodeMethod(auto_oh_dtr_structure_dtr_pole.getText().toString().split("~")[0])) {
                        Toast.makeText(c, "Select  DTR Structure Code Not Match..!! ", Toast.LENGTH_LONG).show();
                    } else if (arcGISDatabase.checkIncrementalValueDTRCodeLoacalSave(auto_oh_dtr_structure_dtr_pole.getText().toString().split("~")[0])) {
                        Toast.makeText(c, "DTR already mapped ..!! ", Toast.LENGTH_LONG).show();
                    } else if (sp_oh_dtr_structure_type_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select DTR Structure type ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr)&&sp_oh_phase_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select Phase ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr)&&sp_oh_capacity_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select Capacity ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr)&&auto_oh_make_dtr_pole.getText().toString().isEmpty()) {
                        Toast.makeText(c, "Enter Make ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr)&&!makeArrayList.contains(auto_oh_make_dtr_pole.getText().toString())) {
                        Toast.makeText(c, "Enter Make Not Match ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr)&&ed_oh_serial_number_dtr_pole.getText().toString().isEmpty()) {
                        Toast.makeText(c, "Enter Serial Number ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr)&&ed_oh_location_name_dtr_pole.getText().toString().isEmpty()) {
                        Toast.makeText(c, "Enter Location Name ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr)&&sp_oh_type_of_load.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select Type Of Load ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr)&&sp_oh_dtr_location_type.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select DTR Location Type ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr)&&sp_oh_is_lt_ckts_available.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select Is OH LT ckts available  ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr)&&"Yes".equalsIgnoreCase(oh_is_lt_ckts_availableStr) && sp_oh_no_of_lt_circuits.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select No Of LT Circuit ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr)&&sp_oh_ab_switch_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select AB Switch ", Toast.LENGTH_LONG).show();
                    } else {
                        overHeadDataValidation();
                    }
                } else if ("HT Service-Pole".equalsIgnoreCase(poleconfiguration)) {
                    try {
                        if (auto_oh_ht_service_number.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Select HT Service Number ", Toast.LENGTH_LONG).show();
                        } else if (!arcGISDatabase.checkIncrementalValueMethod(auto_oh_ht_service_number.getText().toString().split("~")[0])) {
                            Toast.makeText(c, "Select HT Service Number Not Match..!! ", Toast.LENGTH_LONG).show();
                        } else {
                            overHeadDataValidation();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(c, "Select HT Service Number Not Match..!! ", Toast.LENGTH_LONG).show();
                    }
                } else if ("Line Capacitor".equalsIgnoreCase(poleconfiguration)) {
                    if ("Select".equalsIgnoreCase(sp_oh_line_capacitor.getSelectedItem().toString())) {
                        Toast.makeText(c, "Select Line Capacitor ", Toast.LENGTH_LONG).show();
                    } else {
                        overHeadDataValidation();
                    }
                } else if ("Auto Reclosure".equalsIgnoreCase(poleconfiguration)) {
                    if ("Select".equalsIgnoreCase(sp_oh_auto_reclosure.getSelectedItem().toString())) {
                        Toast.makeText(c, "Select Auto Reclosure ", Toast.LENGTH_LONG).show();
                    } else {
                        overHeadDataValidation();
                    }
                } else if ("AB-Pole".equalsIgnoreCase(poleconfiguration)) {
                    if ("Select".equalsIgnoreCase(sp_oh_no_of_ab_switches.getSelectedItem().toString())) {
                        Toast.makeText(c, "Select No Of AB Switches ", Toast.LENGTH_LONG).show();
                    } else {
                        overHeadDataValidation();
                    }
                } else {
                    overHeadDataValidation();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void overHeadDataValidation() {

        IncrementalSurveyVo surveyVo = new IncrementalSurveyVo();
        if (feederCode != null & !feederCode.isEmpty() && !ed_pole_no.getText().toString().isEmpty() && !ed_frompole.getText().toString().isEmpty() && !x.isEmpty() && !y.isEmpty()) {

            if (Integer.parseInt(ed_pole_no.getText().toString()) == Integer.parseInt(ed_frompole.getText().toString())) {
                Toast.makeText(c, "Pole No and From Pole No Should Not Be Same Contact Admin", Toast.LENGTH_LONG).show();
            } else {
                if (x.equalsIgnoreCase("0.0") && y.equalsIgnoreCase("0.0")) {
                    Toast.makeText(c, "Gps Loading .....!!", Toast.LENGTH_LONG).show();
                } else {

                    List<String>  workOrderStatus = arcGISDatabase.getIncrementalWorkOrderStatus(String.format("SELECT DISTINCT STATUS FROM INCREMENTAL_EHTMASTER_TABLE WHERE WORKORDER_ID='%s'",work_order));
                    List<String>  isFeederType = arcGISDatabase.getIncrementalWorkOrderStatus(String.format("SELECT DISTINCT IS_FEEDER_TYPE FROM INCREMENTAL_EHTMASTER_TABLE WHERE FEEDER_CODE_33KV='%s'",feederCode));
                    mapData.put("POLE_TYPE", poleType);
                    surveyVo.setFeeder_id(feederCode);
                    surveyVo.setPole_no(Integer.parseInt(ed_pole_no.getText().toString()));
                    surveyVo.setFrom_pole(Integer.parseInt(ed_frompole.getText().toString()));
                    surveyVo.setLineType(lineType);
                    if (!"Select".equalsIgnoreCase(noofcircuit)) {
                        surveyVo.setNoofCircuits(noofcircuit);
                    } else {
                        surveyVo.setNoofCircuits("");
                    }

                    if (!"Select".equalsIgnoreCase(poleType)) {
                        surveyVo.setPole_type(poleType);
                    } else {
                        surveyVo.setPole_type("");
                    }
                    if (!"Select".equalsIgnoreCase(poleHeight)) {
                        surveyVo.setPole_height(poleHeight);
                    } else {
                        surveyVo.setPole_height("");
                    }
                    //surveyVo.setNoof_circuits(noofCircuits);
                    surveyVo.setPole_support(poleSupport);

                    surveyVo.setTapping_point(taping_Point);
                    if (!"Select".equalsIgnoreCase(conductorsize)) {
                        surveyVo.setConductor_size(conductorsize);
                    } else {
                        surveyVo.setConductor_size("");
                    }
                    surveyVo.setPinpoint(pinpoint);
                    if ("Cutpoint".equalsIgnoreCase(pinpoint)) {
                        surveyVo.setPoleStructure(polestructure);
                    } else {
                        surveyVo.setPoleStructure("");
                    }


                    if (!"Select".equalsIgnoreCase(poleconfiguration)) {
                        surveyVo.setPole_configuration(poleconfiguration);
                    } else {
                        surveyVo.setPole_configuration("");
                    }
                    surveyVo.setPole_paintingcode(edt_polepaintingcode.getText().toString());
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
                    if (!"Select".equalsIgnoreCase(ssname)) {
                        surveyVo.setSs_name(ssname);
                    } else {
                        surveyVo.setSs_name("");
                    }
                    if ("DTR Pole".equalsIgnoreCase(poleconfiguration)) {
                        surveyVo.setDtr_phase(sp_oh_phase_dtr_pole.getSelectedItem().toString());
                        surveyVo.setDtr_capacity(sp_oh_capacity_dtr_pole.getSelectedItem().toString());
                        surveyVo.setDtr_ab_switch(sp_oh_ab_switch_dtr_pole.getSelectedItem().toString());
                        surveyVo.setDtr_make(auto_oh_make_dtr_pole.getText().toString());
                        surveyVo.setDtr_structure(sp_oh_dtr_structure_type_dtr_pole.getSelectedItem().toString());
                        surveyVo.setDtr_scno(ed_oh_serial_number_dtr_pole.getText().toString());
                        try {
                            surveyVo.setDtr_code(auto_oh_dtr_structure_dtr_pole.getText().toString().split("~")[0]);
                        } catch (Exception e) {
                            surveyVo.setDtr_code("");
                            Toast.makeText(c, "DTR Structure code Should not be null", Toast.LENGTH_LONG).show();

                        }
                        surveyVo.setDtr_location_name(ed_oh_location_name_dtr_pole.getText().toString());
                        if ("Yes".equalsIgnoreCase(sp_oh_is_lt_ckts_available.getSelectedItem().toString())) {
                            surveyVo.setDtr_no_of_circuit(sp_oh_no_of_lt_circuits.getSelectedItem().toString());
                        } else {
                            surveyVo.setDtr_no_of_circuit("");
                        }
                        surveyVo.setDtr_location_type(sp_oh_dtr_location_type.getSelectedItem().toString());
                        surveyVo.setIs_ltckt_avalable(sp_oh_is_lt_ckts_available.getSelectedItem().toString());
                        surveyVo.setType_of_load(sp_oh_type_of_load.getSelectedItem().toString());
                        surveyVo.setHt_service_number("");
                        surveyVo.setPc_line_capacitor("");
                        surveyVo.setPc_auto_reclosure("");
                    } else {
                        surveyVo.setDtr_phase("");
                        surveyVo.setDtr_capacity("");
                        surveyVo.setDtr_ab_switch("");
                        surveyVo.setDtr_make("");
                        surveyVo.setDtr_structure("");
                        surveyVo.setDtr_scno("");
                        surveyVo.setDtr_code("");
                        surveyVo.setDtr_location_name("");
                        surveyVo.setDtr_no_of_circuit("");
                        surveyVo.setDtr_location_type("");
                        surveyVo.setIs_ltckt_avalable("");
                        surveyVo.setType_of_load("");
                    }

                    if ("HT Service-Pole".equalsIgnoreCase(poleconfiguration)) {
                        try {
                            String[] auto_oh_ht_service_numberStrArry = auto_oh_ht_service_number.getText().toString().split("~");
                            surveyVo.setHt_service_number(auto_oh_ht_service_numberStrArry[0]);
                            surveyVo.setPc_line_capacitor("");
                            surveyVo.setPc_auto_reclosure("");
                            surveyVo.setNo_of_ab_switches("");
                        } catch (Exception e) {
                            surveyVo.setHt_service_number("");
                            surveyVo.setPc_line_capacitor("");
                            surveyVo.setPc_auto_reclosure("");
                            surveyVo.setNo_of_ab_switches("");
                        }
                    } else if ("Line Capacitor".equalsIgnoreCase(poleconfiguration)) {
                        surveyVo.setPc_line_capacitor(sp_oh_line_capacitor.getSelectedItem().toString());
                        surveyVo.setHt_service_number("");
                        surveyVo.setPc_auto_reclosure("");
                        surveyVo.setNo_of_ab_switches("");
                    } else if ("Auto Reclosure".equalsIgnoreCase(poleconfiguration)) {
                        surveyVo.setPc_auto_reclosure(sp_oh_auto_reclosure.getSelectedItem().toString());
                        surveyVo.setHt_service_number("");
                        surveyVo.setPc_line_capacitor("");
                        surveyVo.setNo_of_ab_switches("");
                    } else if ("AB-Pole".equalsIgnoreCase(poleconfiguration)) {
                        surveyVo.setPc_auto_reclosure("");
                        surveyVo.setHt_service_number("");
                        surveyVo.setPc_line_capacitor("");
                        surveyVo.setNo_of_ab_switches(sp_oh_no_of_ab_switches.getSelectedItem().toString());
                    } else {
                        surveyVo.setHt_service_number("");
                        surveyVo.setPc_line_capacitor("");
                        surveyVo.setPc_auto_reclosure("");
                        surveyVo.setNo_of_ab_switches("");
                    }


                    surveyVo.setVoltage_level(sp_oh_voltage_level.getSelectedItem().toString());
                    //surveyVo.setDiffVoltage(diffvoltage);
                    surveyVo.setSsId(ssId);
                    surveyVo.setNoofTappings(nooftappings);
                    surveyVo.setTappingLocation(edt_tappinglocation.getText().toString());
                    surveyVo.setRemarks(edt_remarks_overhead.getText().toString());
                    surveyVo.setWorkOrder(work_order);
                    surveyVo.setWoStatus(workOrderStatus.get(0));
                    surveyVo.setIsFeederType(isFeederType.get(0));
                    surveyVo.setSURVEY_TYPE(sp_Survey_type_string);
                    arcGISDatabase.addIncrementalSurveyData(surveyVo);
                    Toast.makeText(c, "Pole location captured successfully..", Toast.LENGTH_SHORT).show();
                    dismiss();
                    callout.dismiss();
                    graphicsOverlay.getGraphics().remove(graphic);

                    if ("YES".equalsIgnoreCase(oh_wosurveycompleted)) {
                        arcGISDatabase.upDate(String.format("UPDATE WO_STATUS_TABLE SET WO_STATUS='Y' WHERE WO_ID='%s'",work_order));

                    }

                    if ("YES".equalsIgnoreCase(isendpole)) {
                        //arcGISDatabase.upDate(String.format("UPDATE WO_STATUS_TABLE SET WO_STATUS='Y' WHERE WO_ID='%s'",work_order));
                        Intent intent = new Intent(c, FeederMasterActivity.class);
                        c.startActivity(intent);
                        c.finish();
                    }


                }
            }
        }
    }

    public void underGroundData() {
        try {
          /*  if ("Select".equalsIgnoreCase(sp_ug_voltage_level.getSelectedItem().toString())) {
                Toast.makeText(c, "Select Voltage Level", Toast.LENGTH_LONG).show();
            }else*/

            //sp_Survey_type_string
            if ("Select".equalsIgnoreCase(sp_Survey_type_string)){
                Toast.makeText(c, "Select Survey Type", Toast.LENGTH_LONG).show();
            }
           else if ("Select".equalsIgnoreCase(sp_noofcircuit_underg.getSelectedItem().toString())) {
                Toast.makeText(c, "Select No.Of Cables", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(straight_rmu)) {
                Toast.makeText(c, "Select Location Type", Toast.LENGTH_LONG).show();
            } else if ("RMU".equalsIgnoreCase(straight_rmu) && "Select".equalsIgnoreCase(sp_rmu.getSelectedItem().toString())) {
                Toast.makeText(c, "Select RMU ", Toast.LENGTH_LONG).show();
            } else if ("Pole".equalsIgnoreCase(straight_rmu) && "Select".equalsIgnoreCase(sp_ugpoletype.getSelectedItem().toString())) {
                Toast.makeText(c, "Select Pole Type ", Toast.LENGTH_LONG).show();
            } else if ("Pole".equalsIgnoreCase(straight_rmu) && "Select".equalsIgnoreCase(sp_ugpoleheight.getSelectedItem().toString())) {
                Toast.makeText(c, "Select Pole Height ", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(sp_ugtapingpoint.getSelectedItem().toString())) {
                Toast.makeText(c, "Select Tapping Point ", Toast.LENGTH_LONG).show();
            } else if ("Yes".equalsIgnoreCase(ugtapingpoint) && "Select".equalsIgnoreCase(sp_ugnooftappings.getSelectedItem().toString())) {
                Toast.makeText(c, "Select No.Of Tappings", Toast.LENGTH_LONG).show();
            } else if ("Yes".equalsIgnoreCase(ugtapingpoint) && edt_ugtappinglocation.getText().toString().isEmpty()) {
                Toast.makeText(c, "Enter Tapping Location Name", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(cablesize)) {
                Toast.makeText(c, "Select Cable Size ", Toast.LENGTH_LONG).show();
            } else {
                if ("DTR".equalsIgnoreCase(straight_rmu)) {
                    if (sp_ug_section_name_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select Section Name ", Toast.LENGTH_LONG).show();
                    } else if (auto_ug_dtr_structure_dtr_pole.getText().toString().isEmpty()) {
                        Toast.makeText(c, "Select DTR Structure Code ", Toast.LENGTH_LONG).show();
                    } else if (!arcGISDatabase.checkValueIncrementalDTRCodeMethod(auto_ug_dtr_structure_dtr_pole.getText().toString().split("~")[0])) {
                        Toast.makeText(c, "Select  DTR Structure Code Not Match..!! ", Toast.LENGTH_LONG).show();
                    } else if (arcGISDatabase.checkIncrementalValueDTRCodeLoacalSave(auto_ug_dtr_structure_dtr_pole.getText().toString().split("~")[0])) {
                        Toast.makeText(c, "DTR already mapped ..!! ", Toast.LENGTH_LONG).show();
                    } else if (sp_ug_dtr_structure_type_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select DTR Structure type ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(ug_is_dtr_avalabileStr)&&sp_ug_phase_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select Phase ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(ug_is_dtr_avalabileStr)&&sp_ug_capacity_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select Capacity ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(ug_is_dtr_avalabileStr)&&auto_ug_make_dtr_pole.getText().toString().isEmpty()) {
                        Toast.makeText(c, "Enter Make ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(ug_is_dtr_avalabileStr)&&!makeArrayList.contains(auto_ug_make_dtr_pole.getText().toString())) {
                        Toast.makeText(c, "Enter Make Not Match ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(ug_is_dtr_avalabileStr)&&ed_ug_serial_number_dtr_pole.getText().toString().isEmpty()) {
                        Toast.makeText(c, "Enter Serial Number ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(ug_is_dtr_avalabileStr)&&ed_ug_location_name_dtr_pole.getText().toString().isEmpty()) {
                        Toast.makeText(c, "Enter Location Name ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(ug_is_dtr_avalabileStr)&&sp_ug_type_of_load.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select Type Of Load ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(ug_is_dtr_avalabileStr)&&sp_ug_dtr_location_type.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select DTR Location Type ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(ug_is_dtr_avalabileStr)&&sp_ug_is_lt_ckts_available.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select Is OH LT ckts available  ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(ug_is_dtr_avalabileStr)&&"Yes".equalsIgnoreCase(ug_is_lt_ckts_availableStr) && sp_ug_no_of_lt_circuits.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select No Of LT Circuit ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(ug_is_dtr_avalabileStr)&&sp_ug_ab_switch_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select AB Switch ", Toast.LENGTH_LONG).show();
                    } else {
                        underGroundDataValidation();
                    }
                } else if ("HT Service".equalsIgnoreCase(straight_rmu)) {
                    try {
                        if (auto_ug_ht_service_number.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Select HT Service Number ", Toast.LENGTH_LONG).show();
                        } else if (!arcGISDatabase.checkIncrementalValueMethod(auto_ug_ht_service_number.getText().toString().split("~")[0])) {
                            Toast.makeText(c, "Select HT Service Number Not Match..!! ", Toast.LENGTH_LONG).show();
                        } else {
                            underGroundDataValidation();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(c, "Select HT Service Number Not Match..!! ", Toast.LENGTH_LONG).show();
                    }
                } else if ("Line Capacitor".equalsIgnoreCase(straight_rmu)) {
                    if ("Select".equalsIgnoreCase(sp_ug_line_capacitor.getSelectedItem().toString())) {
                        Toast.makeText(c, "Select Line Capacitor ", Toast.LENGTH_LONG).show();
                    } else {
                        underGroundDataValidation();
                    }
                } else if ("Auto Reclosure".equalsIgnoreCase(straight_rmu)) {
                    if ("Select".equalsIgnoreCase(sp_ug_auto_reclosure.getSelectedItem().toString())) {
                        Toast.makeText(c, "Select Auto Reclosure ", Toast.LENGTH_LONG).show();
                    } else {
                        underGroundDataValidation();
                    }
                } else if ("AB-Pole".equalsIgnoreCase(straight_rmu)) {
                    if ("Select".equalsIgnoreCase(sp_ug_no_of_ab_switches.getSelectedItem().toString())) {
                        Toast.makeText(c, "Select No Of AB Switches ", Toast.LENGTH_LONG).show();
                    } else {
                        underGroundDataValidation();
                    }
                } else {
                    underGroundDataValidation();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void underGroundDataValidation() {
        if (feederCode != null & !feederCode.isEmpty() && !ed_pole_no.getText().toString().isEmpty() && !ed_frompole.getText().toString().isEmpty() && !x.isEmpty() && !y.isEmpty()) {
            if (Integer.parseInt(ed_pole_no.getText().toString()) == Integer.parseInt(ed_frompole.getText().toString())) {
                Toast.makeText(c, "Pole No and From Pole No Should Not Be Same Contact Admin", Toast.LENGTH_LONG).show();
            } else {
                if (x.equalsIgnoreCase("0.0") && y.equalsIgnoreCase("0.0")) {
                    Toast.makeText(c, "Gps Loading .....!!", Toast.LENGTH_LONG).show();
                } else {
                    List<String>  workOrderStatus = arcGISDatabase.getIncrementalWorkOrderStatus(String.format("SELECT DISTINCT STATUS FROM INCREMENTAL_EHTMASTER_TABLE WHERE WORKORDER_ID='%s'",work_order));
                    List<String>  isFeederType = arcGISDatabase.getIncrementalWorkOrderStatus(String.format("SELECT DISTINCT IS_FEEDER_TYPE FROM INCREMENTAL_EHTMASTER_TABLE WHERE FEEDER_CODE_33KV='%s'",feederCode));
                    IncrementalSurveyVo underGroundsurveyVo = new IncrementalSurveyVo();
                    underGroundsurveyVo.setFeeder_id(feederCode);
                    underGroundsurveyVo.setPole_no(Integer.parseInt(ed_pole_no.getText().toString()));
                    underGroundsurveyVo.setFrom_pole(Integer.parseInt(ed_frompole.getText().toString()));
                    underGroundsurveyVo.setLineType(lineType);

                    underGroundsurveyVo.setLat(x);
                    underGroundsurveyVo.setLan(y);
                    underGroundsurveyVo.setPastSurveyFlag("N");
                    underGroundsurveyVo.setStjoint_rmu(straight_rmu);//c

                    underGroundsurveyVo.setIsEndPole(undergisendpole);//c
                    underGroundsurveyVo.setRemarks(edt_remarks_underground.getText().toString());
                    if (!"Select".equalsIgnoreCase(rmu)) {
                        underGroundsurveyVo.setRmu_type(rmu);//c
                    } else {
                        underGroundsurveyVo.setRmu_type("");//c
                    }


                    if ("Structure".equalsIgnoreCase(straight_rmu)) {
                        underGroundsurveyVo.setPoleStructure(underpolestructure);
                    } else {
                        underGroundsurveyVo.setPoleStructure("");
                    }
                    if (!"Select".equalsIgnoreCase(noofcircuit_underg)) {
                        underGroundsurveyVo.setNoofCircuits(noofcircuit_underg);//c
                    } else {
                        underGroundsurveyVo.setNoofCircuits("");//c
                    }

                    if (!"Select".equalsIgnoreCase(cablesize)) {
                        underGroundsurveyVo.setConductor_size(cablesize);//c
                    } else {
                        underGroundsurveyVo.setConductor_size("");//c
                    }
                    if (!"Select".equalsIgnoreCase(ugpoletype)) {
                        underGroundsurveyVo.setPole_type(ugpoletype);//C
                    } else {
                        underGroundsurveyVo.setPole_type("");//C
                    }
                    if (!"Select".equalsIgnoreCase(ugpoleheight)) {
                        underGroundsurveyVo.setPole_height(ugpoleheight);//C
                    } else {
                        underGroundsurveyVo.setPole_height("");//C
                    }
                    if (!"Select".equalsIgnoreCase(ugpolesupport)) {
                        underGroundsurveyVo.setPole_support(ugpolesupport);//C
                    } else {
                        underGroundsurveyVo.setPole_support("");//C
                    }
                    underGroundsurveyVo.setSs_name(ugssname);
                    if (!"Select".equalsIgnoreCase(ugtapingpoint)) {
                        underGroundsurveyVo.setTapping_point(ugtapingpoint);//c
                    } else {
                        underGroundsurveyVo.setTapping_point("");//c
                    }
                    if (!"Select".equalsIgnoreCase(ugnooftappings)) {
                        underGroundsurveyVo.setNoofTappings(ugnooftappings);//c
                    } else {
                        underGroundsurveyVo.setNoofTappings("");//c
                    }
                    underGroundsurveyVo.setTappingLocation(edt_ugtappinglocation.getText().toString());//c
                    underGroundsurveyVo.setPole_paintingcode(edt_ugpolepaintingcode.getText().toString());//c
                    underGroundsurveyVo.setVoltage_level(sp_ug_voltage_level.getSelectedItem().toString());//c
                    if ("DTR".equalsIgnoreCase(straight_rmu)) {
                        underGroundsurveyVo.setDtr_phase(sp_ug_phase_dtr_pole.getSelectedItem().toString());//C
                        underGroundsurveyVo.setDtr_capacity(sp_ug_capacity_dtr_pole.getSelectedItem().toString());//C
                        underGroundsurveyVo.setDtr_ab_switch(sp_ug_ab_switch_dtr_pole.getSelectedItem().toString());//c
                        underGroundsurveyVo.setDtr_make(auto_ug_make_dtr_pole.getText().toString());///c
                        underGroundsurveyVo.setDtr_structure(sp_ug_dtr_structure_type_dtr_pole.getSelectedItem().toString());//C
                        underGroundsurveyVo.setDtr_scno(ed_ug_serial_number_dtr_pole.getText().toString());//c
                        try {
                            underGroundsurveyVo.setDtr_code(auto_ug_dtr_structure_dtr_pole.getText().toString().split("~")[0]);//C
                        } catch (Exception e) {
                            underGroundsurveyVo.setDtr_code("");//C
                            Toast.makeText(c, "DTR Structure code Should not be null", Toast.LENGTH_LONG).show();
                        }
                        underGroundsurveyVo.setDtr_location_name(ed_ug_location_name_dtr_pole.getText().toString());///c
                        if ("Yes".equalsIgnoreCase(sp_ug_is_lt_ckts_available.getSelectedItem().toString())) {
                            underGroundsurveyVo.setDtr_no_of_circuit(sp_ug_no_of_lt_circuits.getSelectedItem().toString());//c
                        } else {
                            underGroundsurveyVo.setDtr_no_of_circuit("");//c
                        }
                        underGroundsurveyVo.setDtr_location_type(sp_ug_dtr_location_type.getSelectedItem().toString());
                        underGroundsurveyVo.setIs_ltckt_avalable(sp_ug_is_lt_ckts_available.getSelectedItem().toString());
                        underGroundsurveyVo.setType_of_load(sp_ug_type_of_load.getSelectedItem().toString());
                    } else {
                        underGroundsurveyVo.setDtr_phase("");
                        underGroundsurveyVo.setDtr_capacity("");
                        underGroundsurveyVo.setDtr_ab_switch("");
                        underGroundsurveyVo.setDtr_make("");
                        underGroundsurveyVo.setDtr_structure("");
                        underGroundsurveyVo.setDtr_scno("");
                        underGroundsurveyVo.setDtr_code("");
                        underGroundsurveyVo.setDtr_location_name("");
                        underGroundsurveyVo.setDtr_no_of_circuit("");
                        underGroundsurveyVo.setDtr_location_type("");
                        underGroundsurveyVo.setIs_ltckt_avalable("");
                        underGroundsurveyVo.setType_of_load("");

                    }


                    //sp_ug_struct_stay_detailes       -PENDING FILED
                    if ("Select".equalsIgnoreCase(sp_ug_line_capacitor.getSelectedItem().toString())) {
                        underGroundsurveyVo.setPc_line_capacitor("");
                    } else {
                        underGroundsurveyVo.setPc_line_capacitor(sp_ug_line_capacitor.getSelectedItem().toString());//c
                    }
                    if ("Select".equalsIgnoreCase(sp_ug_auto_reclosure.getSelectedItem().toString())) {
                        underGroundsurveyVo.setPc_auto_reclosure("");
                    } else {
                        underGroundsurveyVo.setPc_auto_reclosure(sp_ug_auto_reclosure.getSelectedItem().toString());//c
                    }
                    if ("AB-Pole".equalsIgnoreCase(straight_rmu)) {
                        underGroundsurveyVo.setNo_of_ab_switches(sp_ug_no_of_ab_switches.getSelectedItem().toString());
                    } else {
                        underGroundsurveyVo.setNo_of_ab_switches("");
                    }
                    try {
                        underGroundsurveyVo.setHt_service_number(auto_ug_ht_service_number.getText().toString().split("~")[0]);//c
                    } catch (Exception e) {
                        underGroundsurveyVo.setHt_service_number("");//c
                    }
                    underGroundsurveyVo.setWorkOrder(work_order);

                    underGroundsurveyVo.setWoStatus(workOrderStatus.get(0));
                    underGroundsurveyVo.setIsFeederType(isFeederType.get(0));
                    underGroundsurveyVo.setSURVEY_TYPE(sp_Survey_type_string);
                    arcGISDatabase.addIncrementalSurveyData(underGroundsurveyVo);

                    Toast.makeText(c, "Pole location captured successfully..", Toast.LENGTH_SHORT).show();
                    dismiss();
                    callout.dismiss();
                    graphicsOverlay.getGraphics().remove(graphic);


                    if ("YES".equalsIgnoreCase(ug_wosurveycompleted)) {
                        arcGISDatabase.upDate(String.format("UPDATE WO_STATUS_TABLE SET WO_STATUS='Y' WHERE WO_ID='%s'",work_order));

                    }
                    if ("Y".equalsIgnoreCase(undergisendpole)) {
                        //arcGISDatabase.upDate(String.format("UPDATE WO_STATUS_TABLE SET WO_STATUS='Y' WHERE WO_ID='%s'",work_order));
                        Intent intent = new Intent(c, FeederMasterActivity.class);
                        c.startActivity(intent);
                        c.finish();
                    }




                }
            }
        }
    }
    public void abCableData() {
        try {

            //sp_Survey_type_string
            if ("Select".equalsIgnoreCase(sp_Survey_type_string)){
                Toast.makeText(c, "Select Survey Type", Toast.LENGTH_LONG).show();
            }

           else if ("Select".equalsIgnoreCase(sp_abc_noofcircuit.getSelectedItem().toString())) {
                Toast.makeText(c, "Select No.Of Cables", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(abc_straight_rmuStr)) {
                Toast.makeText(c, "Select Location Type", Toast.LENGTH_LONG).show();
            } else if ("RMU".equalsIgnoreCase(abc_straight_rmuStr) && "Select".equalsIgnoreCase(sp_abc_rmu.getSelectedItem().toString())) {
                Toast.makeText(c, "Select RMU ", Toast.LENGTH_LONG).show();
            } else if ("Pole".equalsIgnoreCase(abc_straight_rmuStr) && "Select".equalsIgnoreCase(sp_abc_poletype.getSelectedItem().toString())) {
                Toast.makeText(c, "Select Pole Type ", Toast.LENGTH_LONG).show();
            } else if ("Pole".equalsIgnoreCase(abc_straight_rmuStr) && "Select".equalsIgnoreCase(sp_abc_poleheight.getSelectedItem().toString())) {
                Toast.makeText(c, "Select Pole Height ", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(sp_abc_tapingpoint.getSelectedItem().toString())) {
                Toast.makeText(c, "Select Tapping Point ", Toast.LENGTH_LONG).show();
            } else if ("Yes".equalsIgnoreCase(abc_tapingpoint) && "Select".equalsIgnoreCase(sp_abc_nooftappings.getSelectedItem().toString())) {
                Toast.makeText(c, "Select No.Of Tappings", Toast.LENGTH_LONG).show();
            } else if ("Yes".equalsIgnoreCase(abc_tapingpoint) && edt_abc_tappinglocation.getText().toString().isEmpty()) {
                Toast.makeText(c, "Enter Tapping Location Name", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(abc_cablesizeStr)) {
                Toast.makeText(c, "Select Cable Size ", Toast.LENGTH_LONG).show();
            } else {
                if ("DTR".equalsIgnoreCase(abc_straight_rmuStr)) {
                    if (sp_abc_section_name_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select Section Name ", Toast.LENGTH_LONG).show();
                    } else if (auto_abc_dtr_structure_dtr_pole.getText().toString().isEmpty()) {
                        Toast.makeText(c, "Select DTR Structure Code ", Toast.LENGTH_LONG).show();
                    } else if (!arcGISDatabase.checkValueIncrementalDTRCodeMethod(auto_abc_dtr_structure_dtr_pole.getText().toString().split("~")[0])) {
                        Toast.makeText(c, "Select  DTR Structure Code Not Match..!! ", Toast.LENGTH_LONG).show();
                    } else if (arcGISDatabase.checkIncrementalValueDTRCodeLoacalSave(auto_abc_dtr_structure_dtr_pole.getText().toString().split("~")[0])) {
                        Toast.makeText(c, "DTR already mapped ..!! ", Toast.LENGTH_LONG).show();
                    } else if (sp_abc_dtr_structure_type_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select DTR Structure type ", Toast.LENGTH_LONG).show();
                    } else if (sp_abc_phase_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select Phase ", Toast.LENGTH_LONG).show();
                    } else if (sp_abc_capacity_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select Capacity ", Toast.LENGTH_LONG).show();
                    } else if (auto_abc_make_dtr_pole.getText().toString().isEmpty()) {
                        Toast.makeText(c, "Enter Make ", Toast.LENGTH_LONG).show();
                    } else if (!makeArrayList.contains(auto_abc_make_dtr_pole.getText().toString())) {
                        Toast.makeText(c, "Enter Make Not Match ", Toast.LENGTH_LONG).show();
                    } else if (ed_abc_serial_number_dtr_pole.getText().toString().isEmpty()) {
                        Toast.makeText(c, "Enter Serial Number ", Toast.LENGTH_LONG).show();
                    } else if (ed_abc_location_name_dtr_pole.getText().toString().isEmpty()) {
                        Toast.makeText(c, "Enter Location Name ", Toast.LENGTH_LONG).show();
                    } else if (sp_abc_type_of_load.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select Type Of Load ", Toast.LENGTH_LONG).show();
                    } else if (sp_abc_dtr_location_type.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select DTR Location Type ", Toast.LENGTH_LONG).show();
                    } else if (sp_abc_is_lt_ckts_available.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select Is OH LT ckts available  ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(abc_is_lt_ckts_availableStr) && sp_abc_no_of_lt_circuits.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select No Of LT Circuit ", Toast.LENGTH_LONG).show();
                    } else if (sp_abc_ab_switch_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select AB Switch ", Toast.LENGTH_LONG).show();
                    } else {
                        abCableCoverdConductorValidation();
                    }
                } else if ("HT Service".equalsIgnoreCase(abc_straight_rmuStr)) {
                    try {
                        if (auto_abc_ht_service_number.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Select HT Service Number ", Toast.LENGTH_LONG).show();
                        } else if (!arcGISDatabase.checkIncrementalValueMethod(auto_abc_ht_service_number.getText().toString().split("~")[0])) {
                            Toast.makeText(c, "Select HT Service Number Not Match..!! ", Toast.LENGTH_LONG).show();
                        } else {
                            abCableCoverdConductorValidation();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(c, "Select HT Service Number Not Match..!! ", Toast.LENGTH_LONG).show();
                    }
                } else if ("Line Capacitor".equalsIgnoreCase(abc_straight_rmuStr)) {
                    if ("Select".equalsIgnoreCase(sp_abc_line_capacitor.getSelectedItem().toString())) {
                        Toast.makeText(c, "Select Line Capacitor ", Toast.LENGTH_LONG).show();
                    } else {
                        abCableCoverdConductorValidation();
                    }
                } else if ("Auto Reclosure".equalsIgnoreCase(abc_straight_rmuStr)) {
                    if ("Select".equalsIgnoreCase(sp_abc_auto_reclosure.getSelectedItem().toString())) {
                        Toast.makeText(c, "Select Auto Reclosure ", Toast.LENGTH_LONG).show();
                    } else {
                        abCableCoverdConductorValidation();
                    }
                } else if ("AB-Pole".equalsIgnoreCase(abc_straight_rmuStr)) {
                    if ("Select".equalsIgnoreCase(sp_abc_no_of_ab_switches.getSelectedItem().toString())) {
                        Toast.makeText(c, "Select No Of AB Switches ", Toast.LENGTH_LONG).show();
                    } else {
                        abCableCoverdConductorValidation();
                    }
                } else {
                    abCableCoverdConductorValidation();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void abCableCoverdConductorValidation() {
        if (feederCode != null & !feederCode.isEmpty() && !ed_pole_no.getText().toString().isEmpty() && !ed_frompole.getText().toString().isEmpty() && !x.isEmpty() && !y.isEmpty()) {
            if (Integer.parseInt(ed_pole_no.getText().toString()) == Integer.parseInt(ed_frompole.getText().toString())) {
                Toast.makeText(c, "Pole No and From Pole No Should Not Be Same Contact Admin", Toast.LENGTH_LONG).show();
            } else {
                if (x.equalsIgnoreCase("0.0") && y.equalsIgnoreCase("0.0")) {
                    Toast.makeText(c, "Gps Loading .....!!", Toast.LENGTH_LONG).show();
                } else {
                    List<String>  workOrderStatus = arcGISDatabase.getIncrementalWorkOrderStatus(String.format("SELECT DISTINCT STATUS FROM INCREMENTAL_EHTMASTER_TABLE WHERE WORKORDER_ID='%s'",work_order));
                    List<String>  isFeederType = arcGISDatabase.getIncrementalWorkOrderStatus(String.format("SELECT DISTINCT IS_FEEDER_TYPE FROM INCREMENTAL_EHTMASTER_TABLE WHERE FEEDER_CODE_33KV='%s'",feederCode));
                    IncrementalSurveyVo abc_vo = new IncrementalSurveyVo();
                    abc_vo.setFeeder_id(feederCode);
                    abc_vo.setPole_no(Integer.parseInt(ed_pole_no.getText().toString()));
                    abc_vo.setFrom_pole(Integer.parseInt(ed_frompole.getText().toString()));
                    abc_vo.setLineType(lineType);

                    abc_vo.setLat(x);
                    abc_vo.setLan(y);
                    abc_vo.setPastSurveyFlag("N");
                    abc_vo.setStjoint_rmu(abc_straight_rmuStr);//c

                    abc_vo.setIsEndPole(abc_isendpole);//cabc_isendpole
                    abc_vo.setRemarks(edt_abc_remarks.getText().toString());
                    if (!"Select".equalsIgnoreCase(sp_abc_rmuStr)) {
                        abc_vo.setRmu_type(sp_abc_rmuStr);//c
                    } else {
                        abc_vo.setRmu_type("");//c
                    }

                    if ("Structure".equalsIgnoreCase(abc_straight_rmuStr)) {
                        abc_vo.setPoleStructure(sp_abc_derpolestructureStr);
                    } else {
                        abc_vo.setPoleStructure("");
                    }
                    if (!"Select".equalsIgnoreCase(abc_noofcircuitStr)) {
                        abc_vo.setNoofCircuits(abc_noofcircuitStr);//c
                    } else {
                        abc_vo.setNoofCircuits("");//c
                    }

                    if (!"Select".equalsIgnoreCase(abc_cablesizeStr)) {
                        abc_vo.setConductor_size(abc_cablesizeStr);//c
                    } else {
                        abc_vo.setConductor_size("");//c
                    }
                    if (!"Select".equalsIgnoreCase(sp_abc_pole_type_str)) {
                        abc_vo.setPole_type(sp_abc_pole_type_str);//C
                    } else {
                        abc_vo.setPole_type("");//C
                    }
                    if (!"Select".equalsIgnoreCase(abc_poleheightStr)) {
                        abc_vo.setPole_height(abc_poleheightStr);//C
                    } else {
                        abc_vo.setPole_height("");//C
                    }
                    if (!"Select".equalsIgnoreCase(sp_abc_polesupportStr)) {
                        abc_vo.setPole_support(sp_abc_polesupportStr);//C
                    } else {
                        abc_vo.setPole_support("");//C
                    }

                    if (!"Select".equalsIgnoreCase(abc_tapingpoint)) {
                        abc_vo.setTapping_point(abc_tapingpoint);//c
                    } else {
                        abc_vo.setTapping_point("");//c
                    }
                    if (!"Select".equalsIgnoreCase(abc_nooftappings)) {
                        abc_vo.setNoofTappings(abc_nooftappings);//c
                    } else {
                        abc_vo.setNoofTappings("");//c
                    }

                    if ("DTR".equalsIgnoreCase(abc_straight_rmuStr)) {
                        abc_vo.setDtr_phase(sp_abc_phase_dtr_pole.getSelectedItem().toString());//C
                        abc_vo.setDtr_capacity(sp_abc_capacity_dtr_pole.getSelectedItem().toString());//C
                        abc_vo.setDtr_ab_switch(sp_abc_ab_switch_dtr_pole.getSelectedItem().toString());//c
                        abc_vo.setDtr_make(auto_abc_make_dtr_pole.getText().toString());///c
                        abc_vo.setDtr_structure(sp_abc_dtr_structure_type_dtr_pole.getSelectedItem().toString());//C
                        abc_vo.setDtr_scno(ed_abc_serial_number_dtr_pole.getText().toString());//c
                        try {
                            abc_vo.setDtr_code(auto_abc_dtr_structure_dtr_pole.getText().toString().split("~")[0]);//C
                        } catch (Exception e) {
                            abc_vo.setDtr_code("");//C
                            Toast.makeText(c, "DTR Structure code Should not be null", Toast.LENGTH_LONG).show();
                        }

                        abc_vo.setDtr_location_name(ed_abc_location_name_dtr_pole.getText().toString());///c
                        if ("Yes".equalsIgnoreCase(sp_abc_is_lt_ckts_available.getSelectedItem().toString())) {
                            abc_vo.setDtr_no_of_circuit(sp_abc_no_of_lt_circuits.getSelectedItem().toString());//c
                        } else {
                            abc_vo.setDtr_no_of_circuit("");//c
                        }

                        abc_vo.setDtr_location_type(sp_abc_dtr_location_type.getSelectedItem().toString());
                        abc_vo.setIs_ltckt_avalable(sp_abc_is_lt_ckts_available.getSelectedItem().toString());
                        abc_vo.setType_of_load(sp_abc_type_of_load.getSelectedItem().toString());

                    } else {
                        abc_vo.setDtr_phase("");
                        abc_vo.setDtr_capacity("");
                        abc_vo.setDtr_ab_switch("");
                        abc_vo.setDtr_make("");
                        abc_vo.setDtr_structure("");
                        abc_vo.setDtr_scno("");
                        abc_vo.setDtr_code("");
                        abc_vo.setDtr_location_name("");
                        abc_vo.setDtr_no_of_circuit("");
                        abc_vo.setDtr_location_type("");
                        abc_vo.setIs_ltckt_avalable("");
                        abc_vo.setType_of_load("");
                    }


                    //sp_ug_struct_stay_detailes       -PENDING FILED
                    if ("Select".equalsIgnoreCase(sp_abc_line_capacitor.getSelectedItem().toString())) {
                        abc_vo.setPc_line_capacitor("");
                    } else {
                        abc_vo.setPc_line_capacitor(sp_abc_line_capacitor.getSelectedItem().toString());//c
                    }
                    if ("Select".equalsIgnoreCase(sp_abc_auto_reclosure.getSelectedItem().toString())) {
                        abc_vo.setPc_auto_reclosure("");
                    } else {
                        abc_vo.setPc_auto_reclosure(sp_abc_auto_reclosure.getSelectedItem().toString());//c
                    }
                    if ("AB-Pole".equalsIgnoreCase(abc_straight_rmuStr)) {
                        abc_vo.setNo_of_ab_switches(sp_abc_no_of_ab_switches.getSelectedItem().toString());
                    } else {
                        abc_vo.setNo_of_ab_switches("");
                    }
                    abc_vo.setTappingLocation(edt_abc_tappinglocation.getText().toString());//c
                    abc_vo.setPole_paintingcode(edt_abc_polepaintingcode.getText().toString());//c
                    abc_vo.setVoltage_level(sp_abc_voltage_level.getSelectedItem().toString());//c
                    try {
                        abc_vo.setHt_service_number(auto_abc_ht_service_number.getText().toString().split("~")[0]);//c
                    } catch (Exception e) {
                        abc_vo.setHt_service_number("");//c
                    }
                    abc_vo.setSs_name("");//ugssname is no filed
                    abc_vo.setWorkOrder(work_order);
                    abc_vo.setWoStatus(workOrderStatus.get(0));
                    abc_vo.setIsFeederType(isFeederType.get(0));

                    abc_vo.setSURVEY_TYPE(sp_Survey_type_string);

                    arcGISDatabase.addIncrementalSurveyData(abc_vo);

                    Toast.makeText(c, "Pole location captured successfully..", Toast.LENGTH_SHORT).show();
                    dismiss();
                    callout.dismiss();
                    graphicsOverlay.getGraphics().remove(graphic);

                    if ("YES".equalsIgnoreCase(abc_wosurveycompleted)) {
                        arcGISDatabase.upDate(String.format("UPDATE WO_STATUS_TABLE SET WO_STATUS='Y' WHERE WO_ID='%s'",work_order));

                    }

                    if ("Y".equalsIgnoreCase(abc_isendpole)) {
                        //arcGISDatabase.upDate(String.format("UPDATE WO_STATUS_TABLE SET WO_STATUS='Y' WHERE WO_ID='%s'",work_order));
                        Intent intent = new Intent(c, FeederMasterActivity.class);
                        c.startActivity(intent);
                        c.finish();
                    }

                }
            }
        }
    }
}
