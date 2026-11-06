package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.popup;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.ImpDataRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.activity.ArcGISMapRDSSUg11KvActivity;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.activity.FeederMasterRDSSUg11KvActivity;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.activity.PoleSurveyRDSSUg11KvActivity;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.constants.MobileConstantsRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.retrofitservice.responsevo.RetrofitClientInstanceRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.retrofitservice.responsevo.RetrofitServiceRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.sqldatabase.ArcGISDatabaseRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.utilities.CommanUtilitsRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.DTRMaster_voRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.LoginDataResponseRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.SurveyVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.SurveyedRequestDataVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.SurveyedRequestVoRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.SuveyedResponseVoRDSSUg11Kv;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomAllDialogsRDSSUg11Kv extends Dialog implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    private final String x, y;
    public Activity c;
    private Button btn_add, btn_clear, btn_topclear;
    private EditText ed_pole_no, ed_frompole, ed_Estimate_number;
    private TextInputEditText edt_polepaintingcode, edt_remarks_overhead, edt_remarks_underground, edt_tappinglocation, edt_ugtappinglocation, edt_ugpolepaintingcode;
    public ImpDataRDSSUg11Kv deligate;
    private RetrofitServiceRDSSUg11Kv retrofitServiceRDSSUg11Kv;
    private ProgressDialog progressDialog;
    Callout callout;
    GraphicsOverlay graphicsOverlay;
    GraphicsOverlay currentgraphicsOverlay;
    Graphic graphic;
    Graphic currentGraphic;
    LocationDisplay locationDisplay;

    private Spinner sp_oh_ab_switch_dtr_pole, sp_oh_capacity_dtr_pole, sp_oh_phase_dtr_pole, sp_oh_dtr_structure_type_dtr_pole, sp_oh_section_name_dtr_pole, sp_ug_ab_switch_dtr_pole, sp_ug_capacity_dtr_pole, sp_ug_phase_dtr_pole, sp_ug_dtr_structure_type_dtr_pole, sp_ug_section_name_dtr_pole, sp_ugnooftappings, sp_ugtapingpoint, sp_ugpolesupport, sp_ugpoleheight, sp_ugpoletype, sp__underpolestructure, sp_undergisendpole, sp_nooftappings, sp_rmu, sp_straight_rmu, sp_ssname, sp_diffvoltage, sp__polestructure, sp_noofcircuit_underg, sp_cablesize, sp_lineType, sp_poletype, sp_poleheight, sp_noofcircuit, sp_polesupport, sp_tapingpoint, sp_conductorsize, sp_pinpoint, sp_poleconfiguration, sp_ismulticircute, sp_circutetype, sp_isendpole;
    private String ugssname = "", ugnooftappings = "", ugtapingpoint = "", ugpolesupport = "", ugpoleheight = "", ugpoletype = "", underpolestructure = "", undergisendpole = "", ssName = "", nooftappings = "", rmu = "", straight_rmu = "", ssId = "", ssname = "", diffvoltage = "", polestructure = "", noofcircuit_underg = "", cablesize = "", noofcircuit = "", lineType = "", endPole = "", isendpole = "", surveyType, poleType = "", poleHeight = "", noofPoles, feederName = "", poleSupport = "", tapingPoint, taping_Point = "", conductorsize = "", pinpoint = "", poleconfiguration = "", feederCode = "", tappingPoint, ismulticircute = "", circutetype = "";
    private ArcGISDatabaseRDSSUg11Kv arcGISDatabaseRDSSUg11Kv;
    private List<SurveyVoRDSSUg11Kv> surveyVosListRDSSUg11Kv;
    private int maxSurveyCount, poleCountCheck;
    private LinearLayout lay_oh_dtr_pole_main, lay__ug_dtr_pole_main, lyt_circuit, lyt_overhead, lyt_underground, lyt_polestructure, lyt_diffvoltage, lyt_rmu, lyt_tapping, lyt_underpolestructure, lyt_ugpole, lyt_ugtapping;
    private TextView tv_sunname, tv_feedename;
    private Map mapData;
    private int copyFlag = 0;
    private AutoCompleteTextView auto_oh_dtr_structure_dtr_pole, auto_oh_make_dtr_pole, auto_ug_dtr_structure_dtr_pole, auto_ug_make_dtr_pole;
    private EditText ed_oh_location_name_dtr_pole, ed_ug_location_name_dtr_pole;
    private LinearLayout lay_ug_dtr_pole_main;
    private List<String> dtrsectionList = null;
    private String dtr_pole_ug_section_name;
    private List<String> dtr_ug_structureList = null;
    private List<DTRMaster_voRDSSUg11Kv> lsss;
    private ArrayList<String> dtrsectionList_oh = null;
    private String dtr_pole_oh_section_name;
    private List<String> dtr_oh_structureList = null;
    private List<String> dtr_oh_structureList_new = null;
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
    private String sp_ug_type_of_loadStr;
    private String sp_abc_type_of_loadStr;
    private Spinner sp_oh_is_dtr_avalabile;
    private LinearLayout ll_sp_oh_is_dtr_avalabile;
    private LinearLayout ll_auto_oh_make_dtr_pole;
    private LinearLayout ll_ed_oh_serial_number_dtr_pole;
    private LinearLayout ll_sp_oh_is_lt_ckts_available;
    private LinearLayout ll_sp_oh_ab_switch_dtr_pole;
    private LinearLayout ll_sp_ismulticircute;
    private String oh_is_dtr_avalabileStr;
    private LinearLayout lay_oh_is_dtr_avalable;
    private Spinner sp_ug_is_dtr_avalabile;
    private Spinner sp_lay_out;
    private LinearLayout lay_ug_is_dtr_avalable;
    private String ug_is_dtr_avalabileStr = "Yes";
    private String String_sp_lay_out;
    private String String_LAYOUT_NAME_KEY;
    private String String_LAYOUT_ID_KEY;

    Spinner sp_Survey_type;
    Spinner sp_dtr_survey_type;
    Spinner sp_line_Survey_type;
    Spinner sp_fixure_type;
    private String Survey_type_string = "";
    private String dtr_survey_type_string = "";
    private String line_Survey_type_string = "";
    private String fixure_type_string = "";

    private List<SurveyVoRDSSUg11Kv> surveyVoRDSSUg11KvList;
    private String sp_poleheight_KEY = "N";
    Button btn_repeat_pole;

    //New Column RMU and DTR
    private Spinner sp_rmu_survey_type, sp_DTR_survey_type;
    private String rmu_survey_type = "", DTR_survey_type = "";
    private LinearLayout lyt_rmu_survey_type, lyt_dtr_survey_type;

    String from;

    public CustomAllDialogsRDSSUg11Kv(String from, Activity a, String x, String y, Callout callout, GraphicsOverlay graphicsOverlay, Graphic graphic, String feederName, String feederCode, int maxSurveyCount, String tappingPoint, String surveyType, GraphicsOverlay currentgraphicsOverlay, Graphic currentGraphic, String ssName) {
        super(a);
        this.from = from;
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

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custome_dialog_rdss_ug_11kv);
        try {
            setCancelable(false);
            progressDialog = CommanUtilitsRDSSUg11Kv.progressDialog(c);
            btn_clear = (Button) findViewById(R.id.btn_clear);
            btn_add = (Button) findViewById(R.id.btn_add);
            btn_topclear = (Button) findViewById(R.id.btn_topclear);
            ed_pole_no = (EditText) findViewById(R.id.ed_pole_no);
            ed_frompole = (EditText) findViewById(R.id.ed_frompole);
            ed_Estimate_number = (EditText) findViewById(R.id.ed_Estimate_number);
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
            lyt_rmu_survey_type = (LinearLayout) findViewById(R.id.lyt_rmu_survey_type);
            lyt_tapping = (LinearLayout) findViewById(R.id.lyt_tapping);
            lyt_underpolestructure = (LinearLayout) findViewById(R.id.lyt_underpolestructure);
            lyt_ugpole = (LinearLayout) findViewById(R.id.lyt_ugpole);
            lyt_ugtapping = (LinearLayout) findViewById(R.id.lyt_ugtapping);

            btn_repeat_pole = (Button) findViewById(R.id.btn_repeat_pole);
            sp_lineType = (Spinner) findViewById(R.id.sp_lineType);
            sp_noofcircuit = (Spinner) findViewById(R.id.sp_noofcircuit);
            sp_cablesize = (Spinner) findViewById(R.id.sp_cablesize);
            sp_noofcircuit_underg = (Spinner) findViewById(R.id.sp_noofcircuit_underg);
            //edt_ssname=(TextInputEditText)findViewById(R.id.edt_ssname);
            sp__polestructure = (Spinner) findViewById(R.id.sp__polestructure);
            sp_diffvoltage = (Spinner) findViewById(R.id.sp_diffvoltage);

            sp_straight_rmu = (Spinner) findViewById(R.id.sp_straight_rmu);
            sp_rmu = (Spinner) findViewById(R.id.sp_rmu);
            sp_rmu_survey_type = (Spinner) findViewById(R.id.sp_rmu_survey_type);
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
            sp_DTR_survey_type = (Spinner) findViewById(R.id.sp_DTR_survey_type);
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
            sp_oh_is_dtr_avalabile = (Spinner) findViewById(R.id.sp_oh_is_dtr_avalabile);
            ll_sp_oh_is_dtr_avalabile = (LinearLayout) findViewById(R.id.ll_sp_oh_is_dtr_avalabile);
            ll_auto_oh_make_dtr_pole = (LinearLayout) findViewById(R.id.ll_auto_oh_make_dtr_pole);
            ll_ed_oh_serial_number_dtr_pole = (LinearLayout) findViewById(R.id.ll_ed_oh_serial_number_dtr_pole);
            ll_sp_oh_is_lt_ckts_available = (LinearLayout) findViewById(R.id.ll_sp_oh_is_lt_ckts_available);
            ll_sp_oh_ab_switch_dtr_pole = (LinearLayout) findViewById(R.id.ll_sp_oh_ab_switch_dtr_pole);
            ll_sp_ismulticircute = (LinearLayout) findViewById(R.id.ll_sp_ismulticircute);
            lay_oh_is_dtr_avalable = (LinearLayout) findViewById(R.id.lay_oh_is_dtr_avalable);

            sp_ug_is_dtr_avalabile = (Spinner) findViewById(R.id.sp_ug_is_dtr_avalabile);
            sp_lay_out = (Spinner) findViewById(R.id.sp_lay_out);
            lay_ug_is_dtr_avalable = (LinearLayout) findViewById(R.id.lay_ug_is_dtr_avalable);
            lyt_dtr_survey_type = (LinearLayout) findViewById(R.id.lyt_dtr_survey_type);

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
            /*SURVEY_TYPE*/
            sp_Survey_type = (Spinner) findViewById(R.id.sp_Survey_type);
            sp_dtr_survey_type = (Spinner) findViewById(R.id.sp_dtr_survey_type);

            sp_line_Survey_type = (Spinner) findViewById(R.id.sp_line_Survey_type);
            sp_fixure_type = (Spinner) findViewById(R.id.sp_fixure_type);


            ed_pole_no.setEnabled(false);
            ed_frompole.setEnabled(false);

            btn_repeat_pole.setOnClickListener(this);
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
            sp_Survey_type.setOnItemSelectedListener(this);
            sp_dtr_survey_type.setOnItemSelectedListener(this);


            sp_line_Survey_type.setOnItemSelectedListener(this);
            sp_fixure_type.setOnItemSelectedListener(this);


            sp_noofcircuit_underg.setOnItemSelectedListener(this);
            sp__polestructure.setOnItemSelectedListener(this);
            sp_diffvoltage.setOnItemSelectedListener(this);
            sp_oh_type_of_load.setOnItemSelectedListener(this);
            sp_oh_is_dtr_avalabile.setOnItemSelectedListener(this);

            sp_straight_rmu.setOnItemSelectedListener(this);
            sp_rmu.setOnItemSelectedListener(this);
            sp_rmu_survey_type.setOnItemSelectedListener(this);
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
            sp_DTR_survey_type.setOnItemSelectedListener(this);
            sp_ug_capacity_dtr_pole.setOnItemSelectedListener(this);
            sp_ug_ab_switch_dtr_pole.setOnItemSelectedListener(this);
            sp_ug_is_dtr_avalabile.setOnItemSelectedListener(this);
            sp_lay_out.setOnItemSelectedListener(this);
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

            arcGISDatabaseRDSSUg11Kv = new ArcGISDatabaseRDSSUg11Kv(c);
            surveyVosListRDSSUg11Kv = arcGISDatabaseRDSSUg11Kv.getSurveyData("");
            int count = arcGISDatabaseRDSSUg11Kv.getObjectId();
            poleCountCheck = arcGISDatabaseRDSSUg11Kv.getObjectIdCount();
            if (poleCountCheck == 0) {
                //  if(""surveyType)

                if (from.equals("int")) {
                    ed_frompole.setText(String.valueOf(PoleSurveyRDSSUg11KvActivity.other_pole));
                    ed_pole_no.setText(String.valueOf(count + 1));
                } else {
                    int polenum = maxSurveyCount + 1;
                    ed_frompole.setText(String.valueOf(count));
                    ed_pole_no.setText(String.valueOf(polenum));
                }


            } else {
                if (from.equals("int")) {
                    ed_frompole.setText(String.valueOf(PoleSurveyRDSSUg11KvActivity.other_pole));
                    ed_pole_no.setText(String.valueOf(count + 1));
                } else {
                    ed_frompole.setText(String.valueOf(count));
                    ed_pole_no.setText(String.valueOf(count + 1));
                }

            }
            lyt_underground.setVisibility(View.GONE);

            lyt_polestructure.setVisibility(View.GONE);
            lyt_diffvoltage.setVisibility(View.GONE);
            lyt_rmu.setVisibility(View.GONE);
            lyt_rmu_survey_type.setVisibility(View.GONE);
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
            lyt_dtr_survey_type.setVisibility(View.GONE);

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



      /*      surveyVoList = arcGISDatabase.getSurveyDataREPEAT("SELECT * FROM ARCGIS_TABLE_REPEAT LIMIT 1 OFFSET (SELECT COUNT(*) FROM ARCGIS_TABLE_REPEAT)-1");
            if (surveyVoList.size() > 0) {
                btn_repeat_pole.setVisibility(View.VISIBLE);
            }
            else {
                btn_repeat_pole.setVisibility(View.GONE);
            }

*/


            tv_sunname.setText(ssName);
            Log.v("SSNAME-->", ssName);
            tv_feedename.setText(feederName);
            mapData = new HashMap();

            makeListDatabase = arcGISDatabaseRDSSUg11Kv.getComplaint("SELECT DTR_MAKE_M FROM MAKE_MASTER_TABLE");
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

            List<String> conductorsizeListDatabe = arcGISDatabaseRDSSUg11Kv.getComplaint("Select CKT_VALUE from MULTIVOLTAGE_MASTERS_TABLE where CKT_NO = '" + CONDUCTOR_TX + "'" + " ORDER BY CKT_VALUE");
            conductorsizeListDatabe.add("Select");
            Collections.reverse(conductorsizeListDatabe);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, conductorsizeListDatabe);
            sp_conductorsize.setAdapter(adapter);

            //NO Of Cables
          /*  String CABLE_TX = "CABLE";
            List<String> cableListDatabe = arcGISDatabase.getComplaint("Select CKT_VALUE from MULTIVOLTAGE_MASTERS_TABLE where CKT_NO = '" + CABLE_TX + "'" + " ORDER BY CKT_VALUE");
            cableListDatabe.add("Select");
            Collections.reverse(cableListDatabe);
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, cableListDatabe);
            sp_cablesize.setAdapter(adapter1);*/

            //NO Of Cables
            String CABLE_TX1 = "CABLE";
            List<String> abc_cableListDatabe = arcGISDatabaseRDSSUg11Kv.getComplaint("Select CKT_VALUE from MULTIVOLTAGE_MASTERS_TABLE where CKT_NO = '" + CABLE_TX1 + "'" + " ORDER BY CKT_VALUE");
            abc_cableListDatabe.add("Select");
            Collections.reverse(abc_cableListDatabe);
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, abc_cableListDatabe);
            sp_abc_cablesize.setAdapter(adapter2);
            edt_tappinglocation.setFilters(new InputFilter[]{new EmojiExcludeFilter()});
            edt_ugtappinglocation.setFilters(new InputFilter[]{new EmojiExcludeFilter()});
            edt_abc_tappinglocation.setFilters(new InputFilter[]{new EmojiExcludeFilter()});


//SELECT *
            dtr_oh_structureList_new = arcGISDatabaseRDSSUg11Kv.getComplaint(String.format("SELECT DISTINCT LAYOUT_ID || '~' || LAYOUT_NAME FROM COM_TYPE_SUBTYPE_VER"));
            dtr_oh_structureList_new.add("Select");
            Collections.reverse(dtr_oh_structureList_new);
            ArrayAdapter sp_lay_out_ug = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, dtr_oh_structureList_new);
            sp_lay_out.setAdapter(sp_lay_out_ug);


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
                case R.id.btn_repeat_pole:
                    re_pole();
                    break;

                case R.id.btn_clear:
                    deligate.IsSetData("add Record Not Sucess", "1");
                    callout.dismiss();
                    graphicsOverlay.getGraphics().remove(graphic);
                    dismiss();
                    break;
                case R.id.btn_topclear:
                    sp_poleheight_KEY = "N";
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
                    }
                    /*else if (ed_Estimate_number.getText().toString().isEmpty()) {
                        Toast.makeText(c, "Enter Estimate Number ", Toast.LENGTH_LONG).show();
                    }*/


                    else {
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

                case R.id.sp_lay_out:

                    String_sp_lay_out = sp_lay_out.getSelectedItem().toString();

                    String str = String_sp_lay_out;
                    String[] splitStr = str.split("~");

                    String_LAYOUT_ID_KEY = splitStr[0]; //
                    String_LAYOUT_NAME_KEY = splitStr[1]; //


                    break;
                case R.id.sp_ug_is_dtr_avalabile:
                    ug_is_dtr_avalabileStr = sp_ug_is_dtr_avalabile.getSelectedItem().toString();
                    if ("Yes".equalsIgnoreCase(ug_is_dtr_avalabileStr)) {
                        lay_ug_is_dtr_avalable.setVisibility(View.VISIBLE);
                        lyt_dtr_survey_type.setVisibility(View.VISIBLE);
                    } else {
                        lay_ug_is_dtr_avalable.setVisibility(View.GONE);
                        lyt_dtr_survey_type.setVisibility(View.GONE);
                        sp_ug_phase_dtr_pole.setSelection(0);
                        sp_DTR_survey_type.setSelection(0);
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
                    if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr)) {
                        lay_oh_is_dtr_avalable.setVisibility(View.VISIBLE);
                    } else {
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
                    if (sp_poleheight_KEY.equals("N")) {
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

                    } else if (sp_poleheight_KEY.equals("R")) {

                        surveyVoRDSSUg11KvList = arcGISDatabaseRDSSUg11Kv.getSurveyDataREPEAT("SELECT * FROM ARCGIS_TABLE_REPEAT LIMIT 1 OFFSET (SELECT COUNT(*) FROM ARCGIS_TABLE_REPEAT)-1");
                        if (surveyVoRDSSUg11KvList.size() > 0) {

                            for (SurveyVoRDSSUg11Kv surveyVoRDSSUg11Kv : surveyVoRDSSUg11KvList) {

                                String str_key = surveyVoRDSSUg11Kv.getPole_height();

                                List<String> psccList = new ArrayList<>();

                                List<String> rssJoistList = new ArrayList<>();
                                rssJoistList.add(str_key);
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, rssJoistList);
                                sp_poleheight.setAdapter(adapter);


                            }


                        }
                    } else {
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
                        dtrsectionList_oh = arcGISDatabaseRDSSUg11Kv.getAll_DtrStructureSubStationMaster();
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
                        List<String> oh_ht_servicenoList1 = arcGISDatabaseRDSSUg11Kv.getComplaint("SELECT DISTINCT FS_CONSUMER_NUMBER || '~' || FS_CONSUMER_NAME FROM FEEDER_SERVICE_MASTER_TABLE");
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
                            List<String> deffvoltageListDatabe = arcGISDatabaseRDSSUg11Kv.getComplaint("Select CKT_VALUE from MULTIVOLTAGE_MASTERS_TABLE where CKT_NO = '" + noofcircuit + "'" + " ORDER BY CKT_VALUE");
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
                    sp_rmu_survey_type.setSelection(0);
                    if ("RMU".equalsIgnoreCase(straight_rmu)) {
                        lyt_rmu.setVisibility(View.VISIBLE);
                        lyt_rmu_survey_type.setVisibility(View.VISIBLE);
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
                        lyt_rmu_survey_type.setVisibility(View.GONE);
                        lay_ug_ht_service_number.setVisibility(View.GONE);
                        lay_ug_auto_reclosure.setVisibility(View.GONE);
                        lay_ug_line_capacitor.setVisibility(View.GONE);
                        lay_ug_dtr_pole_main.setVisibility(View.GONE);
                        lay_ug_no_of_ab_switches.setVisibility(View.GONE);
                        clearSelecteAfterChangeTextFiledsDTR();
                    } else if ("Pole".equalsIgnoreCase(straight_rmu)) {
                        lyt_ugpole.setVisibility(View.VISIBLE);
                        lyt_rmu.setVisibility(View.GONE);
                        lyt_rmu_survey_type.setVisibility(View.GONE);
                        lay_ug_ht_service_number.setVisibility(View.GONE);
                        lay_ug_auto_reclosure.setVisibility(View.GONE);
                        lay_ug_line_capacitor.setVisibility(View.GONE);
                        lyt_underpolestructure.setVisibility(View.GONE);
                        lay_ug_dtr_pole_main.setVisibility(View.GONE);
                        lay_ug_no_of_ab_switches.setVisibility(View.GONE);
                        clearSelecteAfterChangeTextFiledsDTR();
                    } else if ("HT Service".equalsIgnoreCase(straight_rmu)) {
                        lyt_rmu.setVisibility(View.GONE);
                        lyt_rmu_survey_type.setVisibility(View.GONE);
                        lyt_underpolestructure.setVisibility(View.GONE);
                        lyt_ugpole.setVisibility(View.GONE);
                        lay_ug_ht_service_number.setVisibility(View.VISIBLE);
                        lay_ug_dtr_pole_main.setVisibility(View.GONE);
                        lay_ug_auto_reclosure.setVisibility(View.GONE);
                        lay_ug_line_capacitor.setVisibility(View.GONE);
                        lay_ug_no_of_ab_switches.setVisibility(View.GONE);
                        List<String> ug_ht_servicenoList = arcGISDatabaseRDSSUg11Kv.getComplaint("SELECT DISTINCT FS_CONSUMER_NUMBER || '~' || FS_CONSUMER_NAME FROM FEEDER_SERVICE_MASTER_TABLE");
                        ug_ht_servicenoList.add("Select");
                        Collections.reverse(ug_ht_servicenoList);
                        ArrayAdapter adapterMake_ug = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, ug_ht_servicenoList);
                        auto_ug_ht_service_number.setAdapter(adapterMake_ug);
                        auto_ug_ht_service_number.setThreshold(1);
                        clearSelecteAfterChangeTextFiledsDTR();
                    } else if ("Line Capacitor".equalsIgnoreCase(straight_rmu)) {
                        lyt_rmu.setVisibility(View.GONE);
                        lyt_rmu_survey_type.setVisibility(View.GONE);
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
                        lyt_rmu_survey_type.setVisibility(View.GONE);
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
                        //  lay_ug_is_dtr_avalable.setVisibility(View.VISIBLE);
                        lay_ug_dtr_pole_main.setVisibility(View.VISIBLE);
                        lay_ug_is_dtr_avalable.setVisibility(View.VISIBLE);
                        lyt_dtr_survey_type.setVisibility(View.VISIBLE);
                        lay_ug_ht_service_number.setVisibility(View.GONE);
                        lay_ug_auto_reclosure.setVisibility(View.GONE);
                        lay_ug_line_capacitor.setVisibility(View.GONE);
                        lyt_rmu.setVisibility(View.GONE);
                        lyt_rmu_survey_type.setVisibility(View.GONE);
                        lyt_underpolestructure.setVisibility(View.GONE);
                        lyt_ugpole.setVisibility(View.GONE);
                        lay_ug_no_of_ab_switches.setVisibility(View.GONE);
                        auto_ug_dtr_structure_dtr_pole.setText("");
                      /*  dtr_ug_sectionList = arcGISDatabase.getAll_DtrStructureSubStationMaster();
                        dtr_ug_sectionList.add("Select");
                        Collections.reverse(dtr_ug_sectionList);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c,
                                android.R.layout.simple_spinner_dropdown_item, dtr_ug_sectionList);
                        sp_ug_section_name_dtr_pole.setAdapter(adapter);*/

                    } else if ("AB-Pole".equalsIgnoreCase(straight_rmu)) {
                        lyt_rmu.setVisibility(View.GONE);
                        lyt_rmu_survey_type.setVisibility(View.GONE);
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
                        sp_rmu_survey_type.setSelection(0);
                        sp__underpolestructure.setSelection(0);
                        lyt_rmu.setVisibility(View.GONE);
                        lyt_rmu_survey_type.setVisibility(View.GONE);
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

                case R.id.sp_rmu_survey_type:
                    rmu_survey_type = sp_rmu_survey_type.getSelectedItem().toString();

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
                        dtr_ug_structureList = arcGISDatabaseRDSSUg11Kv.getComplaint(String.format("SELECT DISTINCT DTR_STRUCTURE_CODE || '~' || DTR_LOCATION FROM DTR_MASTER_TABLE WHERE DTR_SECTION_NAME='%s'", dtr_pole_ug_section_name));
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
                        dtr_oh_structureList = arcGISDatabaseRDSSUg11Kv.getComplaint(String.format("SELECT DISTINCT DTR_STRUCTURE_CODE || '~' || DTR_LOCATION FROM DTR_MASTER_TABLE WHERE DTR_SECTION_NAME='%s'", dtr_pole_oh_section_name));
                        dtr_oh_structureList.add("Select");
                        Collections.reverse(dtr_oh_structureList);
                        ArrayAdapter adapterMake_ug = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, dtr_oh_structureList);
                        auto_oh_dtr_structure_dtr_pole.setAdapter(adapterMake_ug);
                        auto_oh_dtr_structure_dtr_pole.setThreshold(1);
                    }
                    break;
                case R.id.sp_DTR_survey_type:
                    DTR_survey_type = sp_DTR_survey_type.getSelectedItem().toString();
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
                        List<String> abc_ht_servicenoList = arcGISDatabaseRDSSUg11Kv.getComplaint("SELECT DISTINCT FS_CONSUMER_NUMBER || '~' || FS_CONSUMER_NAME FROM FEEDER_SERVICE_MASTER_TABLE");
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
                        ArrayList<String> dtr_abc_sectionList = arcGISDatabaseRDSSUg11Kv.getAll_DtrStructureSubStationMaster();
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
                        List<String> dtr_abc_structureList = arcGISDatabaseRDSSUg11Kv.getComplaint(String.format("SELECT DISTINCT DTR_STRUCTURE_CODE || '~' || DTR_LOCATION FROM DTR_MASTER_TABLE WHERE DTR_SECTION_NAME='%s'", dtr_pole_abc_section_name));
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
                case R.id.sp_Survey_type:
                    Survey_type_string = sp_Survey_type.getSelectedItem().toString();

                    break;
                case R.id.sp_dtr_survey_type:
                    dtr_survey_type_string = sp_dtr_survey_type.getSelectedItem().toString();

                    if (dtr_survey_type_string.equals("Proposed")) {
                        ll_sp_oh_is_dtr_avalabile.setVisibility(View.GONE);
                        ll_auto_oh_make_dtr_pole.setVisibility(View.GONE);

                        ll_ed_oh_serial_number_dtr_pole.setVisibility(View.GONE);
                        ll_sp_oh_is_lt_ckts_available.setVisibility(View.GONE);
                        ll_sp_oh_ab_switch_dtr_pole.setVisibility(View.GONE);
                        ll_sp_ismulticircute.setVisibility(View.GONE);

                        auto_oh_make_dtr_pole.setText("");
                    } else {
                        ll_sp_oh_is_dtr_avalabile.setVisibility(View.VISIBLE);
                        ll_auto_oh_make_dtr_pole.setVisibility(View.VISIBLE);
                        ll_ed_oh_serial_number_dtr_pole.setVisibility(View.VISIBLE);
                        ll_sp_oh_is_lt_ckts_available.setVisibility(View.VISIBLE);
                        ll_sp_oh_ab_switch_dtr_pole.setVisibility(View.VISIBLE);
                        ll_sp_ismulticircute.setVisibility(View.VISIBLE);
                    }
                    break;

                case R.id.sp_line_Survey_type:
                    line_Survey_type_string = sp_line_Survey_type.getSelectedItem().toString();

                    break;


                case R.id.sp_fixure_type:
                    fixure_type_string = sp_fixure_type.getSelectedItem().toString();

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
        sp_DTR_survey_type.setSelection(0);
        sp_ug_phase_dtr_pole_str = "";
        DTR_survey_type = "";
        sp_ug_dtr_structure_type_dtr_pole.setSelection(0);
        sp_ug_line_capacitor.setSelection(0);
        sp_ug_capacity_dtr_pole.setSelection(0);
        auto_ug_dtr_structure_dtr_pole.setText("");
        auto_ug_dtr_structure_dtr_pole.setText("");
        sp_rmu.setSelection(0);
        sp_rmu_survey_type.setSelection(0);
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
        // sp_Survey_type.setSelection(0);
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
        //  sp_Survey_type.setSelection(0);
    }

    private void clearSelecteAfterChangeTextFiledsDTR() {
        sp_ug_section_name_dtr_pole.setSelection(0);
        sp_ug_ab_switch_dtr_pole.setSelection(0);
        sp_ug_phase_dtr_pole.setSelection(0);
        sp_ug_phase_dtr_pole_str = "";
        DTR_survey_type = "";
        sp_ug_dtr_structure_type_dtr_pole.setSelection(0);
        sp_ug_line_capacitor.setSelection(0);
        sp_ug_capacity_dtr_pole.setSelection(0);
        auto_ug_dtr_structure_dtr_pole.setText("");
        auto_ug_dtr_structure_dtr_pole.setText("");
        sp_rmu.setSelection(0);
        sp_rmu_survey_type.setSelection(0);
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
        //   sp_Survey_type.setSelection(0);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void overHeadData() {
        try {

            if ("Select".equalsIgnoreCase(Survey_type_string)) {
                Toast.makeText(c, "Select  Pole Survey Type", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(line_Survey_type_string)) {
                Toast.makeText(c, "Select Line Survey Type", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(fixure_type_string)) {
                Toast.makeText(c, "Select fixure Type", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(poleType)) {
                Toast.makeText(c, "Select Pole Type", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(poleHeight)) {
                Toast.makeText(c, "Select Pole Height", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(poleSupport)) {
                Toast.makeText(c, "Select Pole Support", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(taping_Point)) {
                Toast.makeText(c, "Select Taping_Point ", Toast.LENGTH_LONG).show();
            } /*else if ("Yes".equalsIgnoreCase(taping_Point) && "Select".equalsIgnoreCase(sp_nooftappings.getSelectedItem().toString())) {
                Toast.makeText(c, "Select No.Of Tappings", Toast.LENGTH_LONG).show();
            } else if ("Yes".equalsIgnoreCase(taping_Point) && edt_tappinglocation.getText().toString().isEmpty()) {
                Toast.makeText(c, "Enter Tapping Location Name", Toast.LENGTH_LONG).show();
            }*/ else if ("Yes".equalsIgnoreCase(taping_Point) && edt_tappinglocation.getText().toString().isEmpty()) {
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
                    } else if ("Select".equalsIgnoreCase(dtr_survey_type_string)) {
                        Toast.makeText(c, "Select DTR Survey Type ", Toast.LENGTH_LONG).show();
                    } else if (auto_oh_dtr_structure_dtr_pole.getText().toString().isEmpty()) {
                        Toast.makeText(c, "Select DTR Structure Code ", Toast.LENGTH_LONG).show();
                    }
                    //dtr_survey_type_string


                    //Existing // Proposed
                    else if (dtr_survey_type_string.equals("Existing")) {



                       /* if (!arcGISDatabase.checkValueDTRCodeMethod(auto_oh_dtr_structure_dtr_pole.getText().toString().split("~")[0])) {
                            Toast.makeText(c, "Select  DTR Structure Code Not Match..!! ", Toast.LENGTH_LONG).show();
                        } */ /*if (arcGISDatabase.checkValueDTRCodeLoacalSave(auto_oh_dtr_structure_dtr_pole.getText().toString().split("~")[0])) {
                            Toast.makeText(c, "DTR already mapped ..!! ", Toast.LENGTH_LONG).show();
                        } else*/
                        if (sp_oh_dtr_structure_type_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                            Toast.makeText(c, "Select DTR Structure type ", Toast.LENGTH_LONG).show();
                        } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr) && sp_oh_phase_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                            Toast.makeText(c, "Select Phase ", Toast.LENGTH_LONG).show();
                        } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr) && sp_oh_capacity_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                            Toast.makeText(c, "Select Capacity ", Toast.LENGTH_LONG).show();
                        } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr) && auto_oh_make_dtr_pole.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Make ", Toast.LENGTH_LONG).show();
                        } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr) && !makeArrayList.contains(auto_oh_make_dtr_pole.getText().toString())) {
                            Toast.makeText(c, "Enter Make Not Match ", Toast.LENGTH_LONG).show();
                        } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr) && ed_oh_serial_number_dtr_pole.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Serial Number ", Toast.LENGTH_LONG).show();
                        } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr) && ed_oh_location_name_dtr_pole.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Location Name ", Toast.LENGTH_LONG).show();
                        } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr) && sp_oh_type_of_load.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                            Toast.makeText(c, "Select Type Of Load ", Toast.LENGTH_LONG).show();
                        } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr) && sp_oh_dtr_location_type.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                            Toast.makeText(c, "Select DTR Location Type ", Toast.LENGTH_LONG).show();
                        } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr) && sp_oh_is_lt_ckts_available.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                            Toast.makeText(c, "Select Is OH LT ckts available  ", Toast.LENGTH_LONG).show();
                        } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr) && "Yes".equalsIgnoreCase(oh_is_lt_ckts_availableStr) && sp_oh_no_of_lt_circuits.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                            Toast.makeText(c, "Select No Of LT Circuit ", Toast.LENGTH_LONG).show();
                        } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr) && sp_oh_ab_switch_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                            Toast.makeText(c, "Select AB Switch ", Toast.LENGTH_LONG).show();
                        } else {
                            overHeadDataValidation();
                        }
                    }

                    //Existing // Proposed
                    else if (dtr_survey_type_string.equals("Proposed")) {

                        /*if (!arcGISDatabase.checkValueDTRCodeMethod(auto_oh_dtr_structure_dtr_pole.getText().toString().split("~")[0])) {
                            Toast.makeText(c, "Select  DTR Structure Code Not Match..!! ", Toast.LENGTH_LONG).show();
                        } */
                         /*if (arcGISDatabase.checkValueDTRCodeLoacalSave(auto_oh_dtr_structure_dtr_pole.getText().toString().split("~")[0])) {
                            Toast.makeText(c, "DTR already mapped ..!! ", Toast.LENGTH_LONG).show();
                        } else*/
                        if (sp_oh_dtr_structure_type_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                            Toast.makeText(c, "Select DTR Structure type ", Toast.LENGTH_LONG).show();
                        } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr) && sp_oh_phase_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                            Toast.makeText(c, "Select Phase ", Toast.LENGTH_LONG).show();
                        } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr) && sp_oh_capacity_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                            Toast.makeText(c, "Select Capacity ", Toast.LENGTH_LONG).show();
                        } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr) && ed_oh_location_name_dtr_pole.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Location Name ", Toast.LENGTH_LONG).show();
                        } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr) && sp_oh_type_of_load.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                            Toast.makeText(c, "Select Type Of Load ", Toast.LENGTH_LONG).show();
                        } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr) && sp_oh_dtr_location_type.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                            Toast.makeText(c, "Select DTR Location Type ", Toast.LENGTH_LONG).show();
                        }

                      /*

                        if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr)&&auto_oh_make_dtr_pole.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Make ", Toast.LENGTH_LONG).show();
                        } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr)&&!makeArrayList.contains(auto_oh_make_dtr_pole.getText().toString())) {
                            Toast.makeText(c, "Enter Make Not Match ", Toast.LENGTH_LONG).show();
                        }
                          else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr)&&ed_oh_serial_number_dtr_pole.getText().toString().isEmpty()) {
                              Toast.makeText(c, "Enter Serial Number ", Toast.LENGTH_LONG).show();
                          }

                          else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr)&&sp_oh_is_lt_ckts_available.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                              Toast.makeText(c, "Select Is OH LT ckts available  ", Toast.LENGTH_LONG).show();
                          } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr)&&"Yes".equalsIgnoreCase(oh_is_lt_ckts_availableStr) && sp_oh_no_of_lt_circuits.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                              Toast.makeText(c, "Select No Of LT Circuit ", Toast.LENGTH_LONG).show();
                          } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr)&&sp_oh_ab_switch_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                              Toast.makeText(c, "Select AB Switch ", Toast.LENGTH_LONG).show();
                          }
*/
                        else {
                            overHeadDataValidation();
                        }
                    } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr) && ed_oh_location_name_dtr_pole.getText().toString().isEmpty()) {
                        Toast.makeText(c, "Enter Location Name ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr) && sp_oh_type_of_load.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select Type Of Load ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(oh_is_dtr_avalabileStr) && sp_oh_dtr_location_type.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select DTR Location Type ", Toast.LENGTH_LONG).show();
                    } else {
                        overHeadDataValidation();
                    }
                } else if ("HT Service-Pole".equalsIgnoreCase(poleconfiguration)) {
                    try {
                      /*  if (auto_oh_ht_service_number.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Select HT Service Number ", Toast.LENGTH_LONG).show();
                        } else if (!arcGISDatabase.checkValueMethod(auto_oh_ht_service_number.getText().toString().split("~")[0])) {
                            Toast.makeText(c, "Select HT Service Number Not Match..!! ", Toast.LENGTH_LONG).show();
                        } else {

                        }*/
                        overHeadDataValidation();
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

        SurveyVoRDSSUg11Kv surveyVoRDSSUg11Kv = new SurveyVoRDSSUg11Kv();
        if (feederCode != null & !feederCode.isEmpty() && !ed_pole_no.getText().toString().isEmpty() && !ed_frompole.getText().toString().isEmpty() && !x.isEmpty() && !y.isEmpty()) {

            if (Integer.parseInt(ed_pole_no.getText().toString()) == Integer.parseInt(ed_frompole.getText().toString())) {
                Toast.makeText(c, "Pole No and From Pole No Should Not Be Same Contact Admin", Toast.LENGTH_LONG).show();
            } else {
                if (x.equalsIgnoreCase("0.0") && y.equalsIgnoreCase("0.0")) {
                    Toast.makeText(c, "Gps Loading .....!!", Toast.LENGTH_LONG).show();
                } else {

                    List<String> woId = arcGISDatabaseRDSSUg11Kv.getIncrementalWorkOrderStatus(String.format("SELECT DISTINCT WO_ID FROM EHTMASTER_TABLE WHERE FEEDER_CODE_33KV='%s'", feederCode));
                    List<String> isFeederType = arcGISDatabaseRDSSUg11Kv.getIncrementalWorkOrderStatus(String.format("SELECT DISTINCT IS_FEEDER_TYPE FROM EHTMASTER_TABLE WHERE FEEDER_CODE_33KV='%s'", feederCode));


                    mapData.put("POLE_TYPE", poleType);
                    surveyVoRDSSUg11Kv.setFeeder_id(feederCode);
                    surveyVoRDSSUg11Kv.setPole_no(Integer.parseInt(ed_pole_no.getText().toString()));
                    surveyVoRDSSUg11Kv.setFrom_pole(Integer.parseInt(ed_frompole.getText().toString()));
                    surveyVoRDSSUg11Kv.setLineType(lineType);
                    surveyVoRDSSUg11Kv.setGPS_ACCURACY(String.valueOf(ArcGISMapRDSSUg11KvActivity.gpsAccurcy));
                    if (!"Select".equalsIgnoreCase(noofcircuit)) {
                        surveyVoRDSSUg11Kv.setNoofCircuits(noofcircuit);
                    } else {
                        surveyVoRDSSUg11Kv.setNoofCircuits("");
                    }

                    if (!"Select".equalsIgnoreCase(poleType)) {
                        surveyVoRDSSUg11Kv.setPole_type(poleType);
                    } else {
                        surveyVoRDSSUg11Kv.setPole_type("");
                    }
                    if (!"Select".equalsIgnoreCase(poleHeight)) {
                        surveyVoRDSSUg11Kv.setPole_height(poleHeight);
                    } else {
                        surveyVoRDSSUg11Kv.setPole_height("");
                    }
                    //surveyVo.setNoof_circuits(noofCircuits);
                    surveyVoRDSSUg11Kv.setPole_support(poleSupport);

                    surveyVoRDSSUg11Kv.setTapping_point(taping_Point);
                    if (!"Select".equalsIgnoreCase(conductorsize)) {
                        surveyVoRDSSUg11Kv.setConductor_size(conductorsize);
                    } else {
                        surveyVoRDSSUg11Kv.setConductor_size("");
                    }
                    surveyVoRDSSUg11Kv.setPinpoint(pinpoint);
                    if ("Cutpoint".equalsIgnoreCase(pinpoint)) {
                        surveyVoRDSSUg11Kv.setPoleStructure(polestructure);
                    } else {
                        surveyVoRDSSUg11Kv.setPoleStructure("");
                    }


                    if (!"Select".equalsIgnoreCase(poleconfiguration)) {
                        surveyVoRDSSUg11Kv.setPole_configuration(poleconfiguration);
                    } else {
                        surveyVoRDSSUg11Kv.setPole_configuration("");
                    }
                    surveyVoRDSSUg11Kv.setPole_paintingcode(edt_polepaintingcode.getText().toString());
                    surveyVoRDSSUg11Kv.setLat(x);
                    surveyVoRDSSUg11Kv.setLan(y);
                    surveyVoRDSSUg11Kv.setPastSurveyFlag("N");
                    if ("Yes".equalsIgnoreCase(ismulticircute)) {
                        surveyVoRDSSUg11Kv.setIsMultiCircuit(ismulticircute);
                    } else if ("No".equalsIgnoreCase(ismulticircute)) {
                        surveyVoRDSSUg11Kv.setNoofCircuits("1");
                    }
                    if (!"Select".equalsIgnoreCase(sp_circutetype.getSelectedItem().toString())) {
                        surveyVoRDSSUg11Kv.setCircuitType(circutetype);
                    } else {
                        surveyVoRDSSUg11Kv.setCircuitType("");
                    }
                    if (!"Select".equalsIgnoreCase(diffvoltage)) {
                        surveyVoRDSSUg11Kv.setDiffVoltage(diffvoltage);
                    } else {
                        surveyVoRDSSUg11Kv.setDiffVoltage("");
                    }
                    surveyVoRDSSUg11Kv.setIsEndPole(endPole);
                    if (!"Select".equalsIgnoreCase(ssname)) {
                        surveyVoRDSSUg11Kv.setSs_name(ssname);
                    } else {
                        surveyVoRDSSUg11Kv.setSs_name("");
                    }
                    if ("DTR Pole".equalsIgnoreCase(poleconfiguration)) {
                        surveyVoRDSSUg11Kv.setDtr_phase(sp_oh_phase_dtr_pole.getSelectedItem().toString());
                        try {
                            surveyVoRDSSUg11Kv.setDtr_capacity(sp_oh_capacity_dtr_pole.getSelectedItem().toString());
                        } catch (Exception e) {
                            surveyVoRDSSUg11Kv.setDtr_capacity("");
                        }

                        surveyVoRDSSUg11Kv.setDTR_PRO_EXT(sp_dtr_survey_type.getSelectedItem().toString());
                        surveyVoRDSSUg11Kv.setDtr_ab_switch(sp_oh_ab_switch_dtr_pole.getSelectedItem().toString());
                        surveyVoRDSSUg11Kv.setDtr_make(auto_oh_make_dtr_pole.getText().toString());
                        surveyVoRDSSUg11Kv.setDtr_structure(sp_oh_dtr_structure_type_dtr_pole.getSelectedItem().toString());
                        surveyVoRDSSUg11Kv.setDtr_scno(ed_oh_serial_number_dtr_pole.getText().toString());
                        try {
                            surveyVoRDSSUg11Kv.setDtr_code(auto_oh_dtr_structure_dtr_pole.getText().toString().split("~")[0]);
                        } catch (Exception e) {
                            surveyVoRDSSUg11Kv.setDtr_code("");
                            Toast.makeText(c, "DTR Structure code Should not be null", Toast.LENGTH_LONG).show();

                        }
                        surveyVoRDSSUg11Kv.setDtr_location_name(ed_oh_location_name_dtr_pole.getText().toString());
                        if ("Yes".equalsIgnoreCase(sp_oh_is_lt_ckts_available.getSelectedItem().toString())) {
                            surveyVoRDSSUg11Kv.setDtr_no_of_circuit(sp_oh_no_of_lt_circuits.getSelectedItem().toString());
                        } else {
                            surveyVoRDSSUg11Kv.setDtr_no_of_circuit("");
                        }
                        surveyVoRDSSUg11Kv.setDtr_location_type(sp_oh_dtr_location_type.getSelectedItem().toString());

                        if (sp_oh_is_lt_ckts_available.getSelectedItem().toString().equals("Select")) {
                            surveyVoRDSSUg11Kv.setIs_ltckt_avalable("");
                        } else {
                            surveyVoRDSSUg11Kv.setIs_ltckt_avalable(sp_oh_is_lt_ckts_available.getSelectedItem().toString());
                        }


                        surveyVoRDSSUg11Kv.setType_of_load(sp_oh_type_of_load.getSelectedItem().toString());
                        surveyVoRDSSUg11Kv.setHt_service_number("");
                        surveyVoRDSSUg11Kv.setPc_line_capacitor("");
                        surveyVoRDSSUg11Kv.setPc_auto_reclosure("");
                    } else {
                        surveyVoRDSSUg11Kv.setDtr_phase("");
                        surveyVoRDSSUg11Kv.setDtr_capacity("");
                        surveyVoRDSSUg11Kv.setDtr_ab_switch("");
                        surveyVoRDSSUg11Kv.setDtr_make("");
                        surveyVoRDSSUg11Kv.setDtr_structure("");
                        surveyVoRDSSUg11Kv.setDtr_scno("");
                        surveyVoRDSSUg11Kv.setDtr_code("");
                        surveyVoRDSSUg11Kv.setDtr_location_name("");
                        surveyVoRDSSUg11Kv.setDtr_no_of_circuit("");
                        surveyVoRDSSUg11Kv.setDtr_location_type("");
                        surveyVoRDSSUg11Kv.setIs_ltckt_avalable("");
                        surveyVoRDSSUg11Kv.setType_of_load("");
                    }

                    if ("HT Service-Pole".equalsIgnoreCase(poleconfiguration)) {
                        try {
                            String[] auto_oh_ht_service_numberStrArry = auto_oh_ht_service_number.getText().toString().split("~");
                            surveyVoRDSSUg11Kv.setHt_service_number(auto_oh_ht_service_numberStrArry[0]);
                            surveyVoRDSSUg11Kv.setPc_line_capacitor("");
                            surveyVoRDSSUg11Kv.setPc_auto_reclosure("");
                            surveyVoRDSSUg11Kv.setNo_of_ab_switches("");
                        } catch (Exception e) {
                            surveyVoRDSSUg11Kv.setHt_service_number("");
                            surveyVoRDSSUg11Kv.setPc_line_capacitor("");
                            surveyVoRDSSUg11Kv.setPc_auto_reclosure("");
                            surveyVoRDSSUg11Kv.setNo_of_ab_switches("");
                        }
                    } else if ("Line Capacitor".equalsIgnoreCase(poleconfiguration)) {
                        surveyVoRDSSUg11Kv.setPc_line_capacitor(sp_oh_line_capacitor.getSelectedItem().toString());
                        surveyVoRDSSUg11Kv.setHt_service_number("");
                        surveyVoRDSSUg11Kv.setPc_auto_reclosure("");
                        surveyVoRDSSUg11Kv.setNo_of_ab_switches("");
                    } else if ("Auto Reclosure".equalsIgnoreCase(poleconfiguration)) {
                        surveyVoRDSSUg11Kv.setPc_auto_reclosure(sp_oh_auto_reclosure.getSelectedItem().toString());
                        surveyVoRDSSUg11Kv.setHt_service_number("");
                        surveyVoRDSSUg11Kv.setPc_line_capacitor("");
                        surveyVoRDSSUg11Kv.setNo_of_ab_switches("");
                    } else if ("AB-Pole".equalsIgnoreCase(poleconfiguration)) {
                        surveyVoRDSSUg11Kv.setPc_auto_reclosure("");
                        surveyVoRDSSUg11Kv.setHt_service_number("");
                        surveyVoRDSSUg11Kv.setPc_line_capacitor("");
                        surveyVoRDSSUg11Kv.setNo_of_ab_switches(sp_oh_no_of_ab_switches.getSelectedItem().toString());
                    } else {
                        surveyVoRDSSUg11Kv.setHt_service_number("");
                        surveyVoRDSSUg11Kv.setPc_line_capacitor("");
                        surveyVoRDSSUg11Kv.setPc_auto_reclosure("");
                        surveyVoRDSSUg11Kv.setNo_of_ab_switches("");
                    }


                    surveyVoRDSSUg11Kv.setVoltage_level(sp_oh_voltage_level.getSelectedItem().toString());
                    //surveyVo.setDiffVoltage(diffvoltage);
                    surveyVoRDSSUg11Kv.setSsId(ssId);
                    surveyVoRDSSUg11Kv.setNoofTappings(nooftappings);
                    surveyVoRDSSUg11Kv.setTappingLocation(edt_tappinglocation.getText().toString());
                    surveyVoRDSSUg11Kv.setRemarks(edt_remarks_overhead.getText().toString());
                    surveyVoRDSSUg11Kv.setWoId(woId.get(0));
                    surveyVoRDSSUg11Kv.setIsFeederType(isFeederType.get(0));
                    /*SURVEY_TYPE*/
                    surveyVoRDSSUg11Kv.setSurvaytype(line_Survey_type_string);//Survey_type_string

                    surveyVoRDSSUg11Kv.setESTIMATE_NO("");
                    surveyVoRDSSUg11Kv.setDTR_PRO_EXT(dtr_survey_type_string);
                    surveyVoRDSSUg11Kv.setPOLE_SURVEY_TYPE(Survey_type_string);//line_Survey_type_string
                    surveyVoRDSSUg11Kv.setFIXURE_TYPE(fixure_type_string);

                    surveyVoRDSSUg11Kv.setLAYOUT_NAME(String_LAYOUT_NAME_KEY);
                    surveyVoRDSSUg11Kv.setLAYOUT_ID(String_LAYOUT_ID_KEY);

                    if (from.equals("int")) {
                        //Toast.makeText(c, "FROM INT", Toast.LENGTH_SHORT).show();
                        callAddApi(surveyVoRDSSUg11Kv);
                    } else {
                        arcGISDatabaseRDSSUg11Kv.addSurveyData(surveyVoRDSSUg11Kv);
                        arcGISDatabaseRDSSUg11Kv.addSurveyDatarepeat(surveyVoRDSSUg11Kv);
                        if ("YES".equalsIgnoreCase(isendpole)) {
                            Intent intent = new Intent(c, FeederMasterRDSSUg11KvActivity.class);
                            c.startActivity(intent);
                            c.finish();
                        }

                        Toast.makeText(c, "Pole location captured successfully..", Toast.LENGTH_SHORT).show();
                        dismiss();
                        callout.dismiss();
                        graphicsOverlay.getGraphics().remove(graphic);
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


            if ("Select".equalsIgnoreCase(Survey_type_string)) {
                Toast.makeText(c, "Select  Pole Survey Type", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(line_Survey_type_string)) {
                Toast.makeText(c, "Select Line Survey Type", Toast.LENGTH_LONG).show();
            }
           /* else if ("Select".equalsIgnoreCase(fixure_type_string)){
                Toast.makeText(c, "Select fixure Type", Toast.LENGTH_LONG).show();
            }*/
            else if ("Select".equalsIgnoreCase(sp_noofcircuit_underg.getSelectedItem().toString())) {
                Toast.makeText(c, "Select No.Of Cables", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(straight_rmu)) {
                Toast.makeText(c, "Select Location Type", Toast.LENGTH_LONG).show();
            } else if ("RMU".equalsIgnoreCase(straight_rmu) && "Select".equalsIgnoreCase(sp_rmu_survey_type.getSelectedItem().toString())) {
                Toast.makeText(c, "Select RMU Survey Type ", Toast.LENGTH_LONG).show();
            } else if ("RMU".equalsIgnoreCase(straight_rmu) && "Select".equalsIgnoreCase(sp_rmu.getSelectedItem().toString())) {
                Toast.makeText(c, "Select RMU Type ", Toast.LENGTH_LONG).show();
            } else if ("Pole".equalsIgnoreCase(straight_rmu) && "Select".equalsIgnoreCase(sp_ugpoletype.getSelectedItem().toString())) {
                Toast.makeText(c, "Select Pole Type ", Toast.LENGTH_LONG).show();
            } else if ("Pole".equalsIgnoreCase(straight_rmu) && "Select".equalsIgnoreCase(sp_ugpoleheight.getSelectedItem().toString())) {
                Toast.makeText(c, "Select Pole Height ", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(sp_ugtapingpoint.getSelectedItem().toString())) {
                Toast.makeText(c, "Select Tapping Point ", Toast.LENGTH_LONG).show();
            } /*else if ("Yes".equalsIgnoreCase(ugtapingpoint) && "Select".equalsIgnoreCase(sp_ugnooftappings.getSelectedItem().toString())) {
                Toast.makeText(c, "Select No.Of Tappings", Toast.LENGTH_LONG).show();
            } else if ("Yes".equalsIgnoreCase(ugtapingpoint) && edt_ugtappinglocation.getText().toString().isEmpty()) {
                Toast.makeText(c, "Enter Tapping Location Name", Toast.LENGTH_LONG).show();
            } */ else if ("Yes".equalsIgnoreCase(ugtapingpoint) && edt_ugtappinglocation.getText().toString().isEmpty()) {
                Toast.makeText(c, "Enter Tapping Location Name", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(cablesize)) {
                Toast.makeText(c, "Select Cable Size ", Toast.LENGTH_LONG).show();
            } else {

                if ("DTR".equalsIgnoreCase(straight_rmu)) {
                    if (sp_ug_section_name_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select Section Name ", Toast.LENGTH_LONG).show();
                    }
                   /* else if ("Select".equalsIgnoreCase(dtr_survey_type_string)) {
                        Toast.makeText(c, "Select DTR Survey Type ", Toast.LENGTH_LONG).show();
                    }*/
                   /* else if (sp_DTR_survey_type.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select DTR Survey Type", Toast.LENGTH_LONG).show();
                    }

                    else if (auto_ug_dtr_structure_dtr_pole.getText().toString().isEmpty()) {
                        Toast.makeText(c, "Select DTR Structure Code ", Toast.LENGTH_LONG).show();
                    }*/

                  /*  else if (!arcGISDatabase.checkValueDTRCodeMethod(auto_ug_dtr_structure_dtr_pole.getText().toString().split("~")[0])) {
                        Toast.makeText(c, "Select  DTR Structure Code Not Match..!! ", Toast.LENGTH_LONG).show();
                    } *//*else if (arcGISDatabase.checkValueDTRCodeLoacalSave(auto_ug_dtr_structure_dtr_pole.getText().toString().split("~")[0])) {
                        Toast.makeText(c, "DTR already mapped ..!! ", Toast.LENGTH_LONG).show();
                    } else *//*if (sp_ug_dtr_structure_type_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select DTR Structure type ", Toast.LENGTH_LONG).show();
                    }  else */
                    if ("Yes".equalsIgnoreCase(ug_is_dtr_avalabileStr) && sp_DTR_survey_type.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select DTR Survey Type", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(ug_is_dtr_avalabileStr) && sp_ug_phase_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select Phase ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(ug_is_dtr_avalabileStr) && sp_ug_capacity_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select Capacity ", Toast.LENGTH_LONG).show();
                    } else if ("Yes".equalsIgnoreCase(ug_is_dtr_avalabileStr) && ed_ug_location_name_dtr_pole.getText().toString().isEmpty()) {
                        Toast.makeText(c, "Enter Location Name ", Toast.LENGTH_LONG).show();
                    }

                  /*  else if ("Yes".equalsIgnoreCase(ug_is_dtr_avalabileStr)&&auto_ug_make_dtr_pole.getText().toString().isEmpty()) {
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
                    }*/
                    else {
                        underGroundDataValidation();
                    }
                } else if ("HT Service".equalsIgnoreCase(straight_rmu)) {
                    try {
                      /*  if (auto_ug_ht_service_number.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Select HT Service Number ", Toast.LENGTH_LONG).show();
                        } else if (!arcGISDatabase.checkValueMethod(auto_ug_ht_service_number.getText().toString().split("~")[0])) {
                            Toast.makeText(c, "Select HT Service Number Not Match..!! ", Toast.LENGTH_LONG).show();
                        } else {
                            underGroundDataValidation();
                        }*/
                        underGroundDataValidation();
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
                    List<String> woId = arcGISDatabaseRDSSUg11Kv.getIncrementalWorkOrderStatus(String.format("SELECT DISTINCT WO_ID FROM EHTMASTER_TABLE WHERE FEEDER_CODE_33KV='%s'", feederCode));
                    List<String> isFeederType = arcGISDatabaseRDSSUg11Kv.getIncrementalWorkOrderStatus(String.format("SELECT DISTINCT IS_FEEDER_TYPE FROM EHTMASTER_TABLE WHERE FEEDER_CODE_33KV='%s'", feederCode));
                    SurveyVoRDSSUg11Kv underGroundsurveyVoRDSSUg11Kv = new SurveyVoRDSSUg11Kv();
                    underGroundsurveyVoRDSSUg11Kv.setFeeder_id(feederCode);
                    underGroundsurveyVoRDSSUg11Kv.setPole_no(Integer.parseInt(ed_pole_no.getText().toString()));
                    underGroundsurveyVoRDSSUg11Kv.setFrom_pole(Integer.parseInt(ed_frompole.getText().toString()));
                    underGroundsurveyVoRDSSUg11Kv.setLineType(lineType);

                    underGroundsurveyVoRDSSUg11Kv.setLat(x);
                    underGroundsurveyVoRDSSUg11Kv.setLan(y);
                    underGroundsurveyVoRDSSUg11Kv.setPastSurveyFlag("N");
                    underGroundsurveyVoRDSSUg11Kv.setStjoint_rmu(straight_rmu);//c
                    underGroundsurveyVoRDSSUg11Kv.setGPS_ACCURACY(String.valueOf(ArcGISMapRDSSUg11KvActivity.gpsAccurcy));
                    underGroundsurveyVoRDSSUg11Kv.setIsEndPole(undergisendpole);//c
                    underGroundsurveyVoRDSSUg11Kv.setRemarks(edt_remarks_underground.getText().toString());

                    if (!"Select".equalsIgnoreCase(rmu)) {
                        underGroundsurveyVoRDSSUg11Kv.setRmu_type(rmu);//c
                    } else {
                        underGroundsurveyVoRDSSUg11Kv.setRmu_type("");//c
                    }

                    if (!"Select".equalsIgnoreCase(rmu_survey_type)) {
                        underGroundsurveyVoRDSSUg11Kv.setRMU_SURVEY_TYPE(rmu_survey_type);//c
                    } else {
                        underGroundsurveyVoRDSSUg11Kv.setRMU_SURVEY_TYPE("");//c
                    }


                    if ("Structure".equalsIgnoreCase(straight_rmu)) {
                        underGroundsurveyVoRDSSUg11Kv.setPoleStructure(underpolestructure);
                    } else {
                        underGroundsurveyVoRDSSUg11Kv.setPoleStructure("");
                    }
                    if (!"Select".equalsIgnoreCase(noofcircuit_underg)) {
                        underGroundsurveyVoRDSSUg11Kv.setNoofCircuits(noofcircuit_underg);//c
                    } else {
                        underGroundsurveyVoRDSSUg11Kv.setNoofCircuits("");//c
                    }

                    if (!"Select".equalsIgnoreCase(cablesize)) {
                        underGroundsurveyVoRDSSUg11Kv.setConductor_size(cablesize);//c
                    } else {
                        underGroundsurveyVoRDSSUg11Kv.setConductor_size("");//c
                    }
                    if (!"Select".equalsIgnoreCase(ugpoletype)) {
                        underGroundsurveyVoRDSSUg11Kv.setPole_type(ugpoletype);//C
                    } else {
                        underGroundsurveyVoRDSSUg11Kv.setPole_type("");//C
                    }
                    if (!"Select".equalsIgnoreCase(ugpoleheight)) {
                        underGroundsurveyVoRDSSUg11Kv.setPole_height(ugpoleheight);//C
                    } else {
                        underGroundsurveyVoRDSSUg11Kv.setPole_height("");//C
                    }
                    if (!"Select".equalsIgnoreCase(ugpolesupport)) {
                        underGroundsurveyVoRDSSUg11Kv.setPole_support(ugpolesupport);//C
                    } else {
                        underGroundsurveyVoRDSSUg11Kv.setPole_support("");//C
                    }
                    underGroundsurveyVoRDSSUg11Kv.setSs_name(ugssname);
                    if (!"Select".equalsIgnoreCase(ugtapingpoint)) {
                        underGroundsurveyVoRDSSUg11Kv.setTapping_point(ugtapingpoint);//c
                    } else {
                        underGroundsurveyVoRDSSUg11Kv.setTapping_point("");//c
                    }
                    if (!"Select".equalsIgnoreCase(ugnooftappings)) {
                        underGroundsurveyVoRDSSUg11Kv.setNoofTappings(ugnooftappings);//c
                    } else {
                        underGroundsurveyVoRDSSUg11Kv.setNoofTappings("");//c
                    }



                   /* if (!"Select".equalsIgnoreCase(DTR_survey_type)) {
                        underGroundsurveyVo.setDTR_PRO_EXT(DTR_survey_type);//c
                        System.out.println("DTR Survey y:"+DTR_survey_type);
                        Toast.makeText(c, "DTR Survey y:"+DTR_survey_type, Toast.LENGTH_SHORT).show();
                    } else {
                        underGroundsurveyVo.setDTR_PRO_EXT("");//c
                        System.out.println("DTR Survey N:"+DTR_survey_type);
                        Toast.makeText(c, "DTR Survey N:"+DTR_survey_type, Toast.LENGTH_SHORT).show();
                    }*/
                    underGroundsurveyVoRDSSUg11Kv.setTappingLocation(edt_ugtappinglocation.getText().toString());//c
                    underGroundsurveyVoRDSSUg11Kv.setPole_paintingcode(edt_ugpolepaintingcode.getText().toString());//c
                    underGroundsurveyVoRDSSUg11Kv.setVoltage_level(sp_ug_voltage_level.getSelectedItem().toString());//c
                    if ("DTR".equalsIgnoreCase(sp_straight_rmu.getSelectedItem().toString())) {
                        Toast.makeText(c, sp_straight_rmu.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(c, sp_DTR_survey_type.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                        underGroundsurveyVoRDSSUg11Kv.setDTR_PRO_EXT(DTR_survey_type);//C
                        underGroundsurveyVoRDSSUg11Kv.setDtr_phase(sp_ug_phase_dtr_pole.getSelectedItem().toString());//C
                        underGroundsurveyVoRDSSUg11Kv.setDtr_capacity(sp_ug_capacity_dtr_pole.getSelectedItem().toString());//C
                        underGroundsurveyVoRDSSUg11Kv.setDtr_ab_switch(sp_ug_ab_switch_dtr_pole.getSelectedItem().toString());//c
                        underGroundsurveyVoRDSSUg11Kv.setDtr_make(auto_ug_make_dtr_pole.getText().toString());///c
                        underGroundsurveyVoRDSSUg11Kv.setDtr_structure(sp_ug_dtr_structure_type_dtr_pole.getSelectedItem().toString());//C
                        underGroundsurveyVoRDSSUg11Kv.setDtr_scno(ed_ug_serial_number_dtr_pole.getText().toString());//c
                        try {
                            underGroundsurveyVoRDSSUg11Kv.setDtr_code(auto_ug_dtr_structure_dtr_pole.getText().toString().split("~")[0]);//C
                        } catch (Exception e) {
                            underGroundsurveyVoRDSSUg11Kv.setDtr_code("");//C
                            Toast.makeText(c, "DTR Structure code Should not be null", Toast.LENGTH_LONG).show();
                        }
                        underGroundsurveyVoRDSSUg11Kv.setDtr_location_name(ed_ug_location_name_dtr_pole.getText().toString());///c
                        if ("Yes".equalsIgnoreCase(sp_ug_is_lt_ckts_available.getSelectedItem().toString())) {
                            underGroundsurveyVoRDSSUg11Kv.setDtr_no_of_circuit(sp_ug_no_of_lt_circuits.getSelectedItem().toString());//c
                        } else {
                            underGroundsurveyVoRDSSUg11Kv.setDtr_no_of_circuit("");//c
                        }
                        underGroundsurveyVoRDSSUg11Kv.setDtr_location_type(sp_ug_dtr_location_type.getSelectedItem().toString());


                        if (sp_ug_is_lt_ckts_available.getSelectedItem().toString().equals("Select")) {
                            underGroundsurveyVoRDSSUg11Kv.setIs_ltckt_avalable("");//c
                        } else {
                            underGroundsurveyVoRDSSUg11Kv.setIs_ltckt_avalable(sp_ug_is_lt_ckts_available.getSelectedItem().toString());//c
                        }


                        underGroundsurveyVoRDSSUg11Kv.setType_of_load(sp_ug_type_of_load.getSelectedItem().toString());
                    } else {
                        underGroundsurveyVoRDSSUg11Kv.setDtr_phase("");
                        underGroundsurveyVoRDSSUg11Kv.setDTR_PRO_EXT("");
                        underGroundsurveyVoRDSSUg11Kv.setDtr_capacity("");
                        underGroundsurveyVoRDSSUg11Kv.setDtr_ab_switch("");
                        underGroundsurveyVoRDSSUg11Kv.setDtr_make("");
                        underGroundsurveyVoRDSSUg11Kv.setDtr_structure("");
                        underGroundsurveyVoRDSSUg11Kv.setDtr_scno("");
                        underGroundsurveyVoRDSSUg11Kv.setDtr_code("");
                        underGroundsurveyVoRDSSUg11Kv.setDtr_location_name("");
                        underGroundsurveyVoRDSSUg11Kv.setDtr_no_of_circuit("");
                        underGroundsurveyVoRDSSUg11Kv.setDtr_location_type("");
                        underGroundsurveyVoRDSSUg11Kv.setIs_ltckt_avalable("");
                        underGroundsurveyVoRDSSUg11Kv.setType_of_load("");

                    }


                    //sp_ug_struct_stay_detailes       -PENDING FILED
                    if ("Select".equalsIgnoreCase(sp_ug_line_capacitor.getSelectedItem().toString())) {
                        underGroundsurveyVoRDSSUg11Kv.setPc_line_capacitor("");
                    } else {
                        underGroundsurveyVoRDSSUg11Kv.setPc_line_capacitor(sp_ug_line_capacitor.getSelectedItem().toString());//c
                    }
                    if ("Select".equalsIgnoreCase(sp_ug_auto_reclosure.getSelectedItem().toString())) {
                        underGroundsurveyVoRDSSUg11Kv.setPc_auto_reclosure("");
                    } else {
                        underGroundsurveyVoRDSSUg11Kv.setPc_auto_reclosure(sp_ug_auto_reclosure.getSelectedItem().toString());//c
                    }
                    if ("AB-Pole".equalsIgnoreCase(straight_rmu)) {
                        underGroundsurveyVoRDSSUg11Kv.setNo_of_ab_switches(sp_ug_no_of_ab_switches.getSelectedItem().toString());
                    } else {
                        underGroundsurveyVoRDSSUg11Kv.setNo_of_ab_switches("");
                    }
                    try {
                        //underGroundsurveyVo.setHt_service_number("");//c

                        underGroundsurveyVoRDSSUg11Kv.setHt_service_number(auto_ug_ht_service_number.getText().toString().split("~")[0]);//c
                    } catch (Exception e) {
                        underGroundsurveyVoRDSSUg11Kv.setHt_service_number("");//c
                    }
                    underGroundsurveyVoRDSSUg11Kv.setWoId(woId.get(0));
                    underGroundsurveyVoRDSSUg11Kv.setIsFeederType(isFeederType.get(0));

                    underGroundsurveyVoRDSSUg11Kv.setSurvaytype(line_Survey_type_string);//line_Survey_type_string
                    /*1111*/
                    //  underGroundsurveyVo.setDTR_PRO_EXT(DTR_survey_type);
                    underGroundsurveyVoRDSSUg11Kv.setESTIMATE_NO("");

                    underGroundsurveyVoRDSSUg11Kv.setPOLE_SURVEY_TYPE(Survey_type_string);//Survey_type_string

                    if (fixure_type_string.equals("Select")) {
                        underGroundsurveyVoRDSSUg11Kv.setFIXURE_TYPE("");
                    } else {
                        underGroundsurveyVoRDSSUg11Kv.setFIXURE_TYPE(fixure_type_string);
                    }

                    underGroundsurveyVoRDSSUg11Kv.setLAYOUT_NAME(String_LAYOUT_NAME_KEY);
                    underGroundsurveyVoRDSSUg11Kv.setLAYOUT_ID(String_LAYOUT_ID_KEY);

                    if (from.equals("int")) {
                        //Toast.makeText(c, "FROM INT", Toast.LENGTH_SHORT).show();
                        callAddApi(underGroundsurveyVoRDSSUg11Kv);
                    } else {
                        arcGISDatabaseRDSSUg11Kv.addSurveyData(underGroundsurveyVoRDSSUg11Kv);
                        arcGISDatabaseRDSSUg11Kv.addSurveyDatarepeat(underGroundsurveyVoRDSSUg11Kv);
                        if ("Y".equalsIgnoreCase(undergisendpole)) {
                            Intent intent = new Intent(c, FeederMasterRDSSUg11KvActivity.class);
                            c.startActivity(intent);
                            c.finish();
                        }
                        Toast.makeText(c, "Pole location captured successfully..", Toast.LENGTH_SHORT).show();
                        dismiss();
                        callout.dismiss();
                        graphicsOverlay.getGraphics().remove(graphic);
                    }


                }
            }
        }
    }


    public void abCableData() {
        try {


            if ("Select".equalsIgnoreCase(Survey_type_string)) {
                Toast.makeText(c, "Select  Pole Survey Type", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(line_Survey_type_string)) {
                Toast.makeText(c, "Select Line Survey Type", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(fixure_type_string)) {
                Toast.makeText(c, "Select fixure Type", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(sp_abc_noofcircuit.getSelectedItem().toString())) {
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
            } /*else if ("Yes".equalsIgnoreCase(abc_tapingpoint) && "Select".equalsIgnoreCase(sp_abc_nooftappings.getSelectedItem().toString())) {
                Toast.makeText(c, "Select No.Of Tappings", Toast.LENGTH_LONG).show();
            } else if ("Yes".equalsIgnoreCase(abc_tapingpoint) && edt_abc_tappinglocation.getText().toString().isEmpty()) {
                Toast.makeText(c, "Enter Tapping Location Name", Toast.LENGTH_LONG).show();
            } */ else if ("Yes".equalsIgnoreCase(abc_tapingpoint) && edt_abc_tappinglocation.getText().toString().isEmpty()) {
                Toast.makeText(c, "Enter Tapping Location Name", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(abc_cablesizeStr)) {
                Toast.makeText(c, "Select Cable Size ", Toast.LENGTH_LONG).show();
            } else {
                if ("DTR".equalsIgnoreCase(abc_straight_rmuStr)) {
                    if (sp_abc_section_name_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select Section Name ", Toast.LENGTH_LONG).show();
                    } else if ("Select".equalsIgnoreCase(dtr_survey_type_string)) {
                        Toast.makeText(c, "Select DTR Survey Type ", Toast.LENGTH_LONG).show();
                    } else if (auto_abc_dtr_structure_dtr_pole.getText().toString().isEmpty()) {
                        Toast.makeText(c, "Select DTR Structure Code ", Toast.LENGTH_LONG).show();
                    } else if (sp_dtr_survey_type.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                        Toast.makeText(c, "Select DTR Survey Type ", Toast.LENGTH_LONG).show();
                    }
                   /* else if (!arcGISDatabase.checkValueDTRCodeMethod(auto_abc_dtr_structure_dtr_pole.getText().toString().split("~")[0])) {
                        Toast.makeText(c, "Select  DTR Structure Code Not Match..!! ", Toast.LENGTH_LONG).show();
                    }*/
                    /*else if (arcGISDatabase.checkValueDTRCodeLoacalSave(auto_abc_dtr_structure_dtr_pole.getText().toString().split("~")[0])) {
                        Toast.makeText(c, "DTR already mapped ..!! ", Toast.LENGTH_LONG).show();
                    } else*/
                    if (sp_abc_dtr_structure_type_dtr_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
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
                  /*      if (auto_abc_ht_service_number.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Select HT Service Number ", Toast.LENGTH_LONG).show();
                        } else if (!arcGISDatabase.checkValueMethod(auto_abc_ht_service_number.getText().toString().split("~")[0])) {
                            Toast.makeText(c, "Select HT Service Number Not Match..!! ", Toast.LENGTH_LONG).show();
                        } else {
                            abCableCoverdConductorValidation();
                        }*/
                        abCableCoverdConductorValidation();
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
                    List<String> woId = arcGISDatabaseRDSSUg11Kv.getIncrementalWorkOrderStatus(String.format("SELECT DISTINCT WO_ID FROM EHTMASTER_TABLE WHERE FEEDER_CODE_33KV='%s'", feederCode));
                    List<String> isFeederType = arcGISDatabaseRDSSUg11Kv.getIncrementalWorkOrderStatus(String.format("SELECT DISTINCT IS_FEEDER_TYPE FROM EHTMASTER_TABLE WHERE FEEDER_CODE_33KV='%s'", feederCode));
                    SurveyVoRDSSUg11Kv abc_vo = new SurveyVoRDSSUg11Kv();
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

                        if (sp_ug_is_lt_ckts_available.getSelectedItem().toString().equals("Select")) {
                            abc_vo.setIs_ltckt_avalable("");//c
                        } else {
                            abc_vo.setIs_ltckt_avalable(sp_ug_is_lt_ckts_available.getSelectedItem().toString());//c
                        }


                        //   abc_vo.setIs_ltckt_avalable(sp_abc_is_lt_ckts_available.getSelectedItem().toString());
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

                    abc_vo.setWoId(woId.get(0));
                    abc_vo.setIsFeederType(isFeederType.get(0));
                    abc_vo.setSurvaytype(line_Survey_type_string);//line_Survey_type_string

                    abc_vo.setDTR_PRO_EXT(dtr_survey_type_string);
                    abc_vo.setESTIMATE_NO("");


                    abc_vo.setPOLE_SURVEY_TYPE(Survey_type_string);//Survey_type_string


                    if (fixure_type_string.equals("Select")) {

                    } else {
                        abc_vo.setFIXURE_TYPE(fixure_type_string);
                    }

                    abc_vo.setLAYOUT_NAME(String_LAYOUT_NAME_KEY);
                    abc_vo.setLAYOUT_ID(String_LAYOUT_ID_KEY);

                    if (from.equals("int")) {
                        //Toast.makeText(c, "FROM INT", Toast.LENGTH_SHORT).show();
                        callAddApi(abc_vo);
                    } else {
                        arcGISDatabaseRDSSUg11Kv.addSurveyData(abc_vo);
                        arcGISDatabaseRDSSUg11Kv.addSurveyDatarepeat(abc_vo);
                        if ("Y".equalsIgnoreCase(abc_isendpole)) {
                            Intent intent = new Intent(c, FeederMasterRDSSUg11KvActivity.class);
                            c.startActivity(intent);
                            c.finish();
                        }
                        Toast.makeText(c, "Pole location captured successfully..", Toast.LENGTH_SHORT).show();
                        dismiss();
                        callout.dismiss();
                        graphicsOverlay.getGraphics().remove(graphic);
                    }


                }
            }
        }
    }


    public void re_pole() {

        sp_poleheight_KEY = "R";

        surveyVoRDSSUg11KvList = arcGISDatabaseRDSSUg11Kv.getSurveyDataREPEAT("SELECT * FROM ARCGIS_TABLE_REPEAT LIMIT 1 OFFSET (SELECT COUNT(*) FROM ARCGIS_TABLE_REPEAT)-1");
        if (surveyVoRDSSUg11KvList.size() > 0) {

            Toast.makeText(c, "No.of Surveyed Poles" + ":(" + surveyVoRDSSUg11KvList.size() + ")", Toast.LENGTH_SHORT).show();

            for (SurveyVoRDSSUg11Kv surveyVoRDSSUg11Kv : surveyVoRDSSUg11KvList) {


                if (surveyVoRDSSUg11Kv.getPOLE_SURVEY_TYPE().equals("Existing")) {

//sp_Survey_type.setSelection(Integer.parseInt(surveyVo.getPOLE_SURVEY_TYPE()));
                    sp_Survey_type.setSelection(1);

                } else if (surveyVoRDSSUg11Kv.getPOLE_SURVEY_TYPE().equals("Proposed")) {
                    sp_Survey_type.setSelection(2);
                } else {
                    sp_Survey_type.setSelection(0);
                }


                if (surveyVoRDSSUg11Kv.getSurvaytype().equals("Existing")) {
                    sp_line_Survey_type.setSelection(1);
                } else if (surveyVoRDSSUg11Kv.getSurvaytype().equals("Proposed")) {
                    sp_line_Survey_type.setSelection(2);
                } else if (surveyVoRDSSUg11Kv.getSurvaytype().equals("6.3KV to 11KV Convertion")) {
                    sp_line_Survey_type.setSelection(3);
                } else {
                    sp_line_Survey_type.setSelection(0);
                }


                if (surveyVoRDSSUg11Kv.getLineType().equals("Overhead")) {
                    sp_lineType.setSelection(0);
                } else if (surveyVoRDSSUg11Kv.getLineType().equals("UnderGround")) {
                    sp_lineType.setSelection(1);
                }


                if (surveyVoRDSSUg11Kv.getVoltage_level().equals("11KV")) {

                    sp_oh_voltage_level.setSelection(0);
                } else if (surveyVoRDSSUg11Kv.getVoltage_level().equals("6.3 KV")) {

                    sp_oh_voltage_level.setSelection(1);

                }


                if (surveyVoRDSSUg11Kv.getIsEndPole().equals("N")) {

                    sp_isendpole.setSelection(0);
                } else {

                    sp_isendpole.setSelection(1);
                }

                String s = surveyVoRDSSUg11Kv.getPole_type();
                List<String> psccList = new ArrayList<>();

                psccList.add(s);
                psccList.add("Select");
                psccList.add("SS BAY");
                psccList.add("PSCC");
                psccList.add("SPUN");
                psccList.add("RAIL");
                psccList.add("RS JOIST");
                psccList.add("TOWER");
                psccList.add("TUBULAR POLE");
                List<String> newList = psccList.stream().distinct().collect(Collectors.toList());
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, newList);
                sp_poletype.setAdapter(arrayAdapter);

                // sp_poletype.setSelection(0);
/*

                    if (surveyVo.getPole_type().equals("SS BAY")){

                        sp_poletype.setSelection(1);
                    }
                    else  if (surveyVo.getPole_type().equals("PSCC")) {
                        sp_poletype.setSelection(2);
                    }
                    else  if (surveyVo.getPole_type().equals("SPUN")) {
                        sp_poletype.setSelection(3);
                    }
                    else  if (surveyVo.getPole_type().equals("RAIL")) {
                        sp_poletype.setSelection(4);
                    }

                    else  if (surveyVo.getPole_type().equals("RS JOIST")) {
                        sp_poletype.setSelection(5);
                    }
                    else  if (surveyVo.getPole_type().equals("TOWER")) {
                        sp_poletype.setSelection(6);
                    }
                    else  if (surveyVo.getPole_type().equals("TUBULAR POLE")) {
                        sp_poletype.setSelection(7);
                    }
                    else {
                        //Select
                        sp_poletype.setSelection(0);
                    }
*/


                String s_poleheight = surveyVoRDSSUg11Kv.getPole_support();
                List<String> getPole_height = new ArrayList<>();
                getPole_height.add(s_poleheight);
                ArrayAdapter<String> adapter_h = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, getPole_height);
                sp_polesupport.setAdapter(adapter_h);


                String sp_fixure_poleheight = surveyVoRDSSUg11Kv.getFIXURE_TYPE();
                List<String> sp_fixure = new ArrayList<>();
                sp_fixure.add(sp_fixure_poleheight);
                ArrayAdapter<String> adapter_sp_fixure = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, sp_fixure);
                sp_fixure_type.setAdapter(adapter_sp_fixure);


                String sp_conductorsizee = surveyVoRDSSUg11Kv.getConductor_size();
                List<String> sp_sp_conductorsize_ll = new ArrayList<>();
                sp_sp_conductorsize_ll.add(sp_conductorsizee);
                ArrayAdapter<String> adapter_conductorsize = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, sp_sp_conductorsize_ll);
                sp_conductorsize.setAdapter(adapter_conductorsize);


                String taping_point = surveyVoRDSSUg11Kv.getTapping_point();
                List<String> sp_taping_point_ll = new ArrayList<>();
                sp_taping_point_ll.add(taping_point);
                ArrayAdapter<String> adapter_sp_taping_point_ll = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, sp_taping_point_ll);
                sp_tapingpoint.setAdapter(adapter_sp_taping_point_ll);


                //  sp_lay_out.setAdapter(sp_lay_out_ug);


                List<String> ismulticircute_array = new ArrayList<>();
                String LAYOUT_NAME = surveyVoRDSSUg11Kv.getLAYOUT_NAME();
                String LAYOUT_ID = surveyVoRDSSUg11Kv.getLAYOUT_ID();
                ismulticircute_array.add(LAYOUT_ID + "~" + LAYOUT_NAME);
                ArrayAdapter<String> adapter_ismulticircute_point = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, ismulticircute_array);
                sp_lay_out.setAdapter(adapter_ismulticircute_point);


                //sp_poleheight.setSelection(0);

          /*
                List<String> rssJoistList = new ArrayList<>();
                rssJoistList.add("Select");
                *//*rssJoistList.add("8");*//*
                rssJoistList.add("9.1");
                rssJoistList.add("11");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, rssJoistList);
                sp_poleheight.setAdapter(adapter);*/
/*


                if (surveyVo.getPole_type().equals("None")){

                    sp_polesupport.setSelection(0);
                }
                else  if (surveyVo.getPole_type().equals("STRUT")) {
                    sp_polesupport.setSelection(1);
                }
                else  if (surveyVo.getPole_type().equals("STAY")) {
                    sp_polesupport.setSelection(2);
                }
                else  if (surveyVo.getPole_type().equals("STRUT and STAY")) {
                    sp_polesupport.setSelection(3);
                }
*/



                  /*  if (surveyVo.getPole_height().equals("No")){

                        sp_tapingpoint.setSelection(0);
                    }
                    else  if (surveyVo.getPole_type().equals("Yes")) {
                        sp_tapingpoint.setSelection(1);
                    }*/

               /*     if (surveyVo.getConductor_size().equals("No")){

                        sp_conductorsize.setSelection(0);
                    }
                    else  if (surveyVo.getPole_type().equals("Yes")) {
                        sp_conductorsize.setSelection(1);
                    }
*/

            }

        }


    }

    public void callAddApi(SurveyVoRDSSUg11Kv surveyVoRDSSUg11Kv) {
        try {

            callout.dismiss();
            progressDialog.show();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
            String userName = preferences.getString(MobileConstantsRDSSUg11Kv.USERNAME, "");
            String sessionId = preferences.getString(MobileConstantsRDSSUg11Kv.SESSION_ID, "");
            SurveyedRequestVoRDSSUg11Kv surveyedRequestVoRDSSUg11Kv = new SurveyedRequestVoRDSSUg11Kv();
            List<SurveyedRequestDataVoRDSSUg11Kv> surveyedRequestDataVosListRDSSUg11Kv = new ArrayList<>();
            List<LoginDataResponseRDSSUg11Kv> dataResponseList = arcGISDatabaseRDSSUg11Kv.getUserMasterData();
            surveyedRequestVoRDSSUg11Kv.setUSER_ID(userName);
            surveyedRequestVoRDSSUg11Kv.setIMEI_NO(CommanUtilitsRDSSUg11Kv.getIMEI_NO(c));
            surveyedRequestVoRDSSUg11Kv.setSESSION_ID(sessionId);
            surveyedRequestVoRDSSUg11Kv.setOFFICE_ID(dataResponseList.get(0).getOFFICE_ID());
            surveyedRequestVoRDSSUg11Kv.setIS_SURVEY_COM("N");
            surveyedRequestVoRDSSUg11Kv.setFEEDER_ID(feederCode);


            SurveyedRequestDataVoRDSSUg11Kv requestDataVo = new SurveyedRequestDataVoRDSSUg11Kv();
            requestDataVo.setFEEDER_ID(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getFeeder_id()));
            requestDataVo.setVOLTAGE_LEVEL(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getVoltage_level()));
               /* requestDataVo.setPOLE_NO(surveyVo.getPole_no());
                requestDataVo.setFROM_POLE(surveyVo.getFrom_pole());*/

            requestDataVo.setFROM_POLE(Integer.parseInt(PoleSurveyRDSSUg11KvActivity.other_pole));
            requestDataVo.setPOLE_NO(Integer.parseInt(PoleSurveyRDSSUg11KvActivity.pole_no));

            requestDataVo.setPOLE_TYPE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getPole_type()));
            requestDataVo.setPOLE_HEIGHT(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getPole_height()));
            // requestDataVo.setNO_CIRCUITS(surveyVo.getNoof_circuits());
            requestDataVo.setPOLE_SUPPORT(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getPole_support()));
            requestDataVo.setTAPPING_POINT(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getTapping_point()));
            requestDataVo.setCONDUCTOR_SIZE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getConductor_size()));
            requestDataVo.setPOLE_PAINT_CODE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getPole_paintingcode()));
            requestDataVo.setSEC_CODE(CommanUtilitsRDSSUg11Kv.isChecking(dataResponseList.get(0).getOFFICE_ID()));
            requestDataVo.setCREATED_BY(CommanUtilitsRDSSUg11Kv.isChecking(userName));
            requestDataVo.setGLAT(surveyVoRDSSUg11Kv.getLat());
            requestDataVo.setGLONG(surveyVoRDSSUg11Kv.getLan());
            requestDataVo.setNO_CKT(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getIsMultiCircuit()));
            requestDataVo.setCKT_TYPE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getCircuitType()));
            requestDataVo.setIME_NO(CommanUtilitsRDSSUg11Kv.isChecking(CommanUtilitsRDSSUg11Kv.getIMEI_NO(c)));
            requestDataVo.setPOLE_CONFIG(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getPole_configuration()));
            requestDataVo.setPIN_CUT(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getPinpoint()));
            requestDataVo.setSS_NAME(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getSs_name()));
            requestDataVo.setPOLE_STRUCTURE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getPoleStructure()));
            requestDataVo.setDIFF_VOLTAGES(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getDiffVoltage()));

            if (surveyVoRDSSUg11Kv.getIsEndPole() != null) {
                requestDataVo.setEND_POLE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getIsEndPole()));
            } else {
                requestDataVo.setEND_POLE("");
            }
            // requestDataVo.setEND_POLE(surveyVo.getIsEndPole());
            requestDataVo.setLINE_TYPE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getLineType()));
            requestDataVo.setCKT_NO(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getNoofCircuits()));
            requestDataVo.setSS_ID(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getSsId()));
            requestDataVo.setSTJOINT_RMU(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getStjoint_rmu()));
            requestDataVo.setRMU_TYPE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getRmu_type()));
            requestDataVo.setRMU_SURVEY_TYPE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getRMU_SURVEY_TYPE()));
            requestDataVo.setTAPPING_POINTS(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getNoofTappings()));
            requestDataVo.setTAPPING_LOCNAME(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getTappingLocation()));
            requestDataVo.setREMARKS(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getRemarks()));

            //New fileds add
            requestDataVo.setDTR_PHASE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getDtr_phase()));
            requestDataVo.setDTR_CAPACITY(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getDtr_capacity()));
            requestDataVo.setDTR_CODE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getDtr_code()));
            requestDataVo.setDTR_STRUCTURE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getDtr_structure()));
            requestDataVo.setDTR_MAKE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getDtr_make()));
            requestDataVo.setDTR_AB(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getDtr_ab_switch()));
            requestDataVo.setDTR_LOC_NAME(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getDtr_location_name()));
            requestDataVo.setDTR_SNO(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getDtr_scno()));
            requestDataVo.setNO_LT_CKT(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getDtr_no_of_circuit()));
            requestDataVo.setHT_SCNO(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getHt_service_number()));
            requestDataVo.setCAPACITY_RATING(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getPc_line_capacitor()));
            requestDataVo.setRECLOSER_TYPE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getPc_auto_reclosure()));

            requestDataVo.setTYPE_OF_LOAD(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getType_of_load()));
            requestDataVo.setDTR_LOCATION_TYPE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getDtr_location_type()));
            requestDataVo.setIS_LTCKT_AVAILABLE(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getIs_ltckt_avalable()));
            requestDataVo.setNO_OF_AB_SWITCHES(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getNo_of_ab_switches()));
            requestDataVo.setWO_ID(surveyVoRDSSUg11Kv.getWoId());
            requestDataVo.setIS_FEEDER_TYPE(surveyVoRDSSUg11Kv.getIsFeederType());

            requestDataVo.setSURVEY_TYPE(surveyVoRDSSUg11Kv.getSurvaytype());

            requestDataVo.setDTR_PRO_EXT(CommanUtilitsRDSSUg11Kv.isChecking(surveyVoRDSSUg11Kv.getDTR_PRO_EXT()));
            //  requestDataVo.setESTIMATE_NO(surveyVo.getESTIMATE_NO());

            requestDataVo.setPOLE_SURVEY_TYPE(surveyVoRDSSUg11Kv.getPOLE_SURVEY_TYPE());
            requestDataVo.setFIXURE_TYPE(surveyVoRDSSUg11Kv.getFIXURE_TYPE());

            requestDataVo.setLAYOUT_ID(surveyVoRDSSUg11Kv.getLAYOUT_ID());
            requestDataVo.setLAYOUT_NAME(surveyVoRDSSUg11Kv.getLAYOUT_NAME());

            surveyedRequestDataVosListRDSSUg11Kv.add(requestDataVo);
            surveyedRequestVoRDSSUg11Kv.setSURVEY_DATA(surveyedRequestDataVosListRDSSUg11Kv);

            Gson gson = new Gson();
            String json = gson.toJson(surveyedRequestVoRDSSUg11Kv);

            Log.v("okDATA===>>", "" + json);
            //  writeToFile(json);
            retrofitServiceRDSSUg11Kv = RetrofitClientInstanceRDSSUg11Kv.getRetrofitInstance().create(RetrofitServiceRDSSUg11Kv.class);
            Call<SuveyedResponseVoRDSSUg11Kv> responseVoCall = retrofitServiceRDSSUg11Kv.inLinePole(surveyedRequestVoRDSSUg11Kv);
            responseVoCall.enqueue(new Callback<SuveyedResponseVoRDSSUg11Kv>() {
                @Override
                public void onResponse(Call<SuveyedResponseVoRDSSUg11Kv> call, Response<SuveyedResponseVoRDSSUg11Kv> response) {
                    progressDialog.dismiss();
                    if (response.body() != null) {
                        SuveyedResponseVoRDSSUg11Kv response_vo = response.body();
                        progressDialog.dismiss();
                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
                            arcGISDatabaseRDSSUg11Kv.clearSurveyedData();

                            Intent intent = new Intent(c, FeederMasterRDSSUg11KvActivity.class);
                            c.startActivity(intent);
                            c.finish();
                            Toast.makeText(c, "Data Transmitted Successfully", Toast.LENGTH_SHORT).show();

                            Toast.makeText(c, "Data Transmited Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(c, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(c, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SuveyedResponseVoRDSSUg11Kv> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(c, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
