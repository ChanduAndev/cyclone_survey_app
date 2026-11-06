package com.apepdcl.gis_survey.popup;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apepdcl.gis_survey.HomeActivity;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.vo.IncrementalSurveyDataVo;
import com.apepdcl.gis_survey.vo.LTCktsDataVo;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class IncrementalCustomAllDialogs extends Dialog implements AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    public Activity c;
    private TextView tv_dtrstrcuture, tv_ltcircuit, tv_distance;
    private EditText ed_pole_no, ed_frompole;
    private Spinner sp_lineType, sp_oh_isendpole, sp_oh_lineconfig, sp_oh_isstreetlight, sp_oh_otherthan_streetlight, sp_oh_pin_cutpoint, sp_oh_is_doublefeeding_pole, sp_oh_pole_type, sp_oh_pole_height, sp_oh_strut_stay_details, sp_oh_tapping_point, sp_oh_noof_spurlines, sp_oh_conductor_size, sp_oh_ismulti_circuit, sp_oh_noof_circuits, sp_oh_multi_voltage_same_voltage,
            sp_ug_isendpole, sp_ug_locationtype, sp_ug_servicepiller_type, sp_ug_poletype, sp_ug_poleheight, sp_ug_polesupport, sp_ug_feedepiller_outg_circuits, sp_ug_tapping_point, sp_ug_notapping_point, sp_ug_cablesize, sp_ug_nocable, sp_crossing;
    private String woId, lineType, oh_isendpole, oh_lineconfig, oh_isstreetlight, oh_otherthan_streetlight, oh_pin_cutpoint, oh_is_doublefeeding_pole, oh_pole_type, oh_pole_height, oh_strut_stay_details, oh_tapping_point, oh_noof_spurlines, oh_conductor_size, oh_ismulti_circuit, oh_noof_circuits, oh_multi_voltage_same_voltage,
            ug_isendpole, ug_locationtype, ug_servicepiller_type, ug_poletype, ug_poleheight, ug_polesupport, ug_feedepiller_outg_circuits, ug_tapping_point, ug_notapping_point, ug_cablesize, ug_nocable, crossing;
    private LinearLayout lyt_oh_aglservice, lyt_oh_edt_aglservices, lyt_oh_chacquaservices, lyt_oh_edt_aquaservices, lyt_oh_chindustrialservices, lyt_oh_edt_industrialservices, lyt_oh_is_doublefeeding_pole, lyt_oh_noof_spurlines, lyt_oh_noof_circuits, lyt_oh_multivoltage, lyt_oh_33kv_vlckts, lyt_oh_11kv_vlckts, lyt_oh_6_3kv_vlckts, lyt_oh_lt_vlckts, lyt_ug_servicepiller, lyt_ug_tappingpoint, lyt_underground, lyt_overhead, lyt_otherthan_streetlight, lyt_spi_no_circuit, lyt_ug_poletype, lyt_ug_feederpiller, lyt_distance, lyt_oh_crossing, lyt_oh_6_3_crossing, lyt_oh_11kv_crossing, lyt_oh_33kv_crossing, lyt_oh_lt_crossing, lyt_oh_tc_crossing, lyt_oh_tm_crossing;
    private CheckBox ch_oh_aglservices, ch_oh_aquaservices, ch_oh_industrialservices, ch_oh_33kv_vlckts, ch_oh_11kv_vlckts, ch_oh_6_3kv_vlckts, ch_oh_lt_vlckts, ch_oh_6_3_crossing, ch_oh_11kv_crossing, ch_oh_33kv_crossing, ch_oh_lt_crossing, ch_oh_tc_crossing, ch_oh_tm_crossing;
    private TextInputEditText edt_oh_aglservices, edt_oh_aquaservices, edt_oh_industrialservices, edt_oh_tappinglocation, edt_oh_33kv_vlckts, edt_oh_11kv_vlckts, edt_oh_6_3kv_vlckts, edt_oh_lt_vlckts, edt_oh_pole_paintingcode, edt_oh_remarks, edt_ug_tappinglocation, edt_ug_pole_paintingcode, edt_ug_remarks, edt_oh_6_3_crossing, edt_oh_11kv_crossing, edt_oh_33kv_crossing, edt_oh_lt_crossing, edt_oh_tc_crossing, edt_oh_tm_crossing;
    private Button btn_clear, btn_add, btn_lttopclear;
    private ArcGISDatabase arcGISDatabase;
    private List<IncrementalSurveyDataVo> incrementalSurveyDataVoList;
    private int maxSurveyCount, poleCountCheck;
    private String dtrCode, ltCircuit, feederId;
    private final String x, y;
    Callout callout;
    GraphicsOverlay graphicsOverlay;
    Graphic graphic;
    private int noof_33kvCkts, noof_11kvCkts, noof_6_3kvCkts, noof_ltCkts;
    private LinearLayout lay_oh_dtr_pole_dtr_meter_avilable, lay_oh_dtr_pole_no_of_meter, lay_ug_dtr_pole_dtr_meter_avilable, lay_ug_dtr_pole_no_of_meter;
    private Spinner sp_oh_dtr_pole_dtr_meter_avilable, sp_oh_dtr_pole_no_of_meters, sp_ug_dtr_pole_dtr_meter_avilable, sp_ug_dtr_pole_no_of_meters;
    private String oh_dtr_pole_dtr_meter_avilable, oh_dtr_pole_no_of_meters, ug_dtr_pole_dtr_meter_avilable, ug_dtr_pole_no_of_meters;
    private double distance;
    private Spinner sp_ltug_wosurveycompleted, sp_ltoh_wosurveycompleted;
    private String ltug_wosurveycompleted, ltoh_wosurveycompleted;

    public IncrementalCustomAllDialogs(Activity a, String x, String y, Callout callout, GraphicsOverlay graphicsOverlay, Graphic graphic, String feederName, String feederId, int maxSurveyCount, String tappingPoint, String surveyType, GraphicsOverlay currentgraphicsOverlay, Graphic currentGraphic, String ssName, String dtrCode, String ltCircuit, double distance, String woId) {
        super(a);
        this.c = a;
        this.x = x;
        this.y = y;
        this.dtrCode = dtrCode;
        this.ltCircuit = ltCircuit;
        this.callout = callout;
        this.graphicsOverlay = graphicsOverlay;
        this.graphic = graphic;
        this.maxSurveyCount = maxSurveyCount;
        this.distance = distance;
        this.woId = woId;
        this.feederId = feederId;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.incremental_custom_dialog);
        try {
            setCancelable(false);
            tv_dtrstrcuture = findViewById(R.id.tv_dtrstrcuture);
            tv_ltcircuit = findViewById(R.id.tv_ltcircuit);
            ed_pole_no = findViewById(R.id.ed_pole_no);
            ed_pole_no.setEnabled(false);
            ed_frompole = findViewById(R.id.ed_frompole);
            ed_frompole.setEnabled(false);
            sp_lineType = findViewById(R.id.sp_lineType);
            sp_oh_isendpole = findViewById(R.id.sp_oh_isendpole);
            sp_oh_lineconfig = findViewById(R.id.sp_oh_lineconfig);
            sp_oh_isstreetlight = findViewById(R.id.sp_oh_isstreetlight);
            sp_oh_otherthan_streetlight = findViewById(R.id.sp_oh_otherthan_streetlight);
            ch_oh_aglservices = findViewById(R.id.ch_oh_aglservices);
            edt_oh_aglservices = findViewById(R.id.edt_oh_aglservices);
            ch_oh_aquaservices = findViewById(R.id.ch_oh_aquaservices);
            edt_oh_aquaservices = findViewById(R.id.edt_oh_aquaservices);
            ch_oh_industrialservices = findViewById(R.id.ch_oh_industrialservices);
            edt_oh_industrialservices = findViewById(R.id.edt_oh_industrialservices);
            sp_oh_pin_cutpoint = findViewById(R.id.sp_oh_pin_cutpoint);
            sp_oh_is_doublefeeding_pole = findViewById(R.id.sp_oh_is_doublefeeding_pole);
            sp_oh_pole_type = findViewById(R.id.sp_oh_pole_type);
            sp_oh_pole_height = findViewById(R.id.sp_oh_pole_height);
            sp_oh_strut_stay_details = findViewById(R.id.sp_oh_strut_stay_details);
            sp_oh_tapping_point = findViewById(R.id.sp_oh_tapping_point);
            sp_oh_noof_spurlines = findViewById(R.id.sp_oh_noof_spurlines);
            edt_oh_tappinglocation = findViewById(R.id.edt_oh_tappinglocation);
            sp_oh_conductor_size = findViewById(R.id.sp_oh_conductor_size);
            sp_oh_ismulti_circuit = findViewById(R.id.sp_oh_ismulti_circuit);
            sp_oh_noof_circuits = findViewById(R.id.sp_oh_noof_circuits);
            sp_oh_multi_voltage_same_voltage = findViewById(R.id.sp_oh_multi_voltage_same_voltage);
            ch_oh_33kv_vlckts = findViewById(R.id.ch_oh_33kv_vlckts);
            edt_oh_33kv_vlckts = findViewById(R.id.edt_oh_33kv_vlckts);
            ch_oh_11kv_vlckts = findViewById(R.id.ch_oh_11kv_vlckts);
            edt_oh_11kv_vlckts = findViewById(R.id.edt_oh_11kv_vlckts);
            ch_oh_6_3kv_vlckts = findViewById(R.id.ch_oh_6_3kv_vlckts);
            edt_oh_6_3kv_vlckts = findViewById(R.id.edt_oh_6_3kv_vlckts);
            ch_oh_lt_vlckts = findViewById(R.id.ch_oh_lt_vlckts);
            edt_oh_lt_vlckts = findViewById(R.id.edt_oh_lt_vlckts);
            edt_oh_pole_paintingcode = findViewById(R.id.edt_oh_pole_paintingcode);
            edt_oh_remarks = findViewById(R.id.edt_oh_remarks);
            lyt_spi_no_circuit = findViewById(R.id.lyt_spi_no_circuit);
            lay_oh_dtr_pole_dtr_meter_avilable = (LinearLayout) findViewById(R.id.lay_oh_dtr_pole_dtr_meter_avilable);
            lay_oh_dtr_pole_dtr_meter_avilable.setVisibility(View.GONE);
            lay_oh_dtr_pole_no_of_meter = (LinearLayout) findViewById(R.id.lay_oh_dtr_pole_no_of_meter);
            lay_oh_dtr_pole_no_of_meter.setVisibility(View.GONE);
            sp_oh_dtr_pole_dtr_meter_avilable = (Spinner) findViewById(R.id.sp_oh_dtr_pole_dtr_meter_avilable);
            sp_oh_dtr_pole_no_of_meters = (Spinner) findViewById(R.id.sp_oh_dtr_pole_no_of_meters);
            lyt_spi_no_circuit.setVisibility(View.GONE);
//UG
            sp_ug_isendpole = findViewById(R.id.sp_ug_isendpole);
            sp_ug_locationtype = findViewById(R.id.sp_ug_locationtype);

            sp_ug_servicepiller_type = findViewById(R.id.sp_ug_servicepiller_type);
            sp_ug_poletype = findViewById(R.id.sp_ug_poletype);
            sp_ug_poleheight = findViewById(R.id.sp_ug_poleheight);
            sp_ug_polesupport = findViewById(R.id.sp_ug_polesupport);
            sp_ug_feedepiller_outg_circuits = findViewById(R.id.sp_ug_feedepiller_outg_circuits);
            sp_ug_tapping_point = findViewById(R.id.sp_ug_tapping_point);
            sp_ug_notapping_point = findViewById(R.id.sp_ug_notapping_point);
            edt_ug_tappinglocation = findViewById(R.id.edt_ug_tappinglocation);
            sp_ug_cablesize = findViewById(R.id.sp_ug_cablesize);
            sp_ug_nocable = findViewById(R.id.sp_ug_nocable);
            edt_ug_pole_paintingcode = findViewById(R.id.edt_ug_pole_paintingcode);
            edt_ug_remarks = findViewById(R.id.edt_ug_remarks);
            tv_distance = findViewById(R.id.tv_distance);
            sp_crossing = findViewById(R.id.sp_crossing);
            sp_ltug_wosurveycompleted = findViewById(R.id.sp_ltug_wosurveycompleted);
            sp_ltoh_wosurveycompleted = findViewById(R.id.sp_ltoh_wosurveycompleted);

            lay_ug_dtr_pole_dtr_meter_avilable = (LinearLayout) findViewById(R.id.lay_ug_dtr_pole_dtr_meter_avilable);
            lay_ug_dtr_pole_dtr_meter_avilable.setVisibility(View.GONE);
            lay_ug_dtr_pole_no_of_meter = (LinearLayout) findViewById(R.id.lay_ug_dtr_pole_no_of_meter);
            lay_ug_dtr_pole_no_of_meter.setVisibility(View.GONE);
            sp_ug_dtr_pole_dtr_meter_avilable = (Spinner) findViewById(R.id.sp_ug_dtr_pole_dtr_meter_avilable);
            sp_ug_dtr_pole_no_of_meters = (Spinner) findViewById(R.id.sp_ug_dtr_pole_no_of_meters);
            ch_oh_33kv_vlckts = findViewById(R.id.ch_oh_33kv_vlckts);


            edt_oh_6_3_crossing = findViewById(R.id.edt_oh_6_3_crossing);
            ch_oh_6_3_crossing = findViewById(R.id.ch_oh_6_3_crossing);
            ch_oh_11kv_crossing = findViewById(R.id.ch_oh_11kv_crossing);
            edt_oh_11kv_crossing = findViewById(R.id.edt_oh_11kv_crossing);
            ch_oh_33kv_crossing = findViewById(R.id.ch_oh_33kv_crossing);
            edt_oh_33kv_crossing = findViewById(R.id.edt_oh_33kv_crossing);
            ch_oh_lt_crossing = findViewById(R.id.ch_oh_lt_crossing);
            edt_oh_lt_crossing = findViewById(R.id.edt_oh_lt_crossing);
            ch_oh_tc_crossing = findViewById(R.id.ch_oh_tc_crossing);
            edt_oh_tc_crossing = findViewById(R.id.edt_oh_tc_crossing);
            ch_oh_tm_crossing = findViewById(R.id.ch_oh_tm_crossing);
            edt_oh_tm_crossing = findViewById(R.id.edt_oh_tm_crossing);


            lyt_spi_no_circuit.setVisibility(View.GONE);


            btn_clear = findViewById(R.id.btn_clear);
            btn_add = findViewById(R.id.btn_add);
            btn_lttopclear = findViewById(R.id.btn_lttopclear);
            btn_clear.setOnClickListener(this);
            btn_add.setOnClickListener(this);
            btn_lttopclear.setOnClickListener(this);

//LinearLayout
            lyt_oh_aglservice = findViewById(R.id.lyt_oh_aglservice);
            lyt_oh_edt_aglservices = findViewById(R.id.lyt_oh_edt_aglservices);
            lyt_oh_chacquaservices = findViewById(R.id.lyt_oh_chacquaservices);
            lyt_oh_edt_aquaservices = findViewById(R.id.lyt_oh_edt_aquaservices);
            lyt_oh_chindustrialservices = findViewById(R.id.lyt_oh_chindustrialservices);
            lyt_oh_edt_industrialservices = findViewById(R.id.lyt_oh_edt_industrialservices);
            lyt_oh_is_doublefeeding_pole = findViewById(R.id.lyt_oh_is_doublefeeding_pole);
            lyt_oh_noof_spurlines = findViewById(R.id.lyt_oh_noof_spurlines);
            lyt_oh_noof_circuits = findViewById(R.id.lyt_oh_noof_circuits);
            lyt_oh_multivoltage = findViewById(R.id.lyt_oh_multivoltage);
            lyt_oh_33kv_vlckts = findViewById(R.id.lyt_oh_33kv_vlckts);
            lyt_oh_11kv_vlckts = findViewById(R.id.lyt_oh_11kv_vlckts);
            lyt_oh_6_3kv_vlckts = findViewById(R.id.lyt_oh_6_3kv_vlckts);
            lyt_oh_lt_vlckts = findViewById(R.id.lyt_oh_lt_vlckts);
            lyt_ug_servicepiller = findViewById(R.id.lyt_ug_servicepiller);
            lyt_ug_tappingpoint = findViewById(R.id.lyt_ug_tappingpoint);
            lyt_underground = findViewById(R.id.lyt_underground);
            lyt_overhead = findViewById(R.id.lyt_overhead);
            lyt_ug_poletype = findViewById(R.id.lyt_ug_poletype);
            lyt_ug_feederpiller = findViewById(R.id.lyt_ug_feederpiller);
            lyt_ug_poletype.setVisibility(View.GONE);
            lyt_ug_feederpiller.setVisibility(View.GONE);
            lyt_distance = findViewById(R.id.lyt_distance);
            lyt_distance.setVisibility(View.GONE);
            lyt_oh_crossing = findViewById(R.id.lyt_oh_crossing);
            lyt_oh_crossing.setVisibility(View.GONE);
            lyt_oh_6_3_crossing = findViewById(R.id.lyt_oh_6_3_crossing);
            lyt_oh_6_3_crossing.setVisibility(View.GONE);
            lyt_oh_11kv_crossing = findViewById(R.id.lyt_oh_11kv_crossing);
            lyt_oh_11kv_crossing.setVisibility(View.GONE);
            lyt_oh_33kv_crossing = findViewById(R.id.lyt_oh_33kv_crossing);
            lyt_oh_33kv_crossing.setVisibility(View.GONE);
            lyt_oh_lt_crossing = findViewById(R.id.lyt_oh_lt_crossing);
            lyt_oh_lt_crossing.setVisibility(View.GONE);
            lyt_oh_tc_crossing = findViewById(R.id.lyt_oh_tc_crossing);
            lyt_oh_tc_crossing.setVisibility(View.GONE);
            lyt_oh_tm_crossing = findViewById(R.id.lyt_oh_tm_crossing);
            lyt_oh_tm_crossing.setVisibility(View.GONE);

//Always hide
            lyt_otherthan_streetlight = findViewById(R.id.lyt_otherthan_streetlight);
            lyt_otherthan_streetlight.setVisibility(View.GONE);

//Visibility
            lyt_oh_aglservice.setVisibility(View.GONE);
            lyt_oh_edt_aglservices.setVisibility(View.GONE);
            lyt_oh_chacquaservices.setVisibility(View.GONE);
            lyt_oh_edt_aquaservices.setVisibility(View.GONE);
            lyt_oh_chindustrialservices.setVisibility(View.GONE);
            lyt_oh_edt_industrialservices.setVisibility(View.GONE);
            lyt_oh_is_doublefeeding_pole.setVisibility(View.GONE);
            lyt_oh_noof_spurlines.setVisibility(View.GONE);
            lyt_oh_noof_circuits.setVisibility(View.GONE);
            lyt_oh_multivoltage.setVisibility(View.GONE);
            lyt_oh_33kv_vlckts.setVisibility(View.GONE);
            lyt_oh_11kv_vlckts.setVisibility(View.GONE);
            lyt_oh_6_3kv_vlckts.setVisibility(View.GONE);
            lyt_oh_lt_vlckts.setVisibility(View.GONE);

            lyt_ug_servicepiller.setVisibility(View.GONE);
            lyt_ug_tappingpoint.setVisibility(View.GONE);
            lyt_underground.setVisibility(View.GONE);
//SpinnerListner
            sp_ug_dtr_pole_no_of_meters.setOnItemSelectedListener(this);
            sp_ug_dtr_pole_dtr_meter_avilable.setOnItemSelectedListener(this);
            sp_oh_dtr_pole_no_of_meters.setOnItemSelectedListener(this);
            sp_oh_dtr_pole_dtr_meter_avilable.setOnItemSelectedListener(this);
            sp_lineType.setOnItemSelectedListener(this);
            sp_oh_isendpole.setOnItemSelectedListener(this);
            sp_oh_lineconfig.setOnItemSelectedListener(this);
            sp_oh_isstreetlight.setOnItemSelectedListener(this);
            sp_oh_otherthan_streetlight.setOnItemSelectedListener(this);
            sp_oh_pin_cutpoint.setOnItemSelectedListener(this);
            sp_oh_is_doublefeeding_pole.setOnItemSelectedListener(this);
            sp_oh_pole_type.setOnItemSelectedListener(this);
            sp_oh_pole_height.setOnItemSelectedListener(this);
            sp_oh_strut_stay_details.setOnItemSelectedListener(this);
            sp_oh_tapping_point.setOnItemSelectedListener(this);
            sp_oh_noof_spurlines.setOnItemSelectedListener(this);
            sp_oh_conductor_size.setOnItemSelectedListener(this);
            sp_oh_ismulti_circuit.setOnItemSelectedListener(this);
            sp_oh_noof_circuits.setOnItemSelectedListener(this);
            sp_oh_multi_voltage_same_voltage.setOnItemSelectedListener(this);
            sp_ug_isendpole.setOnItemSelectedListener(this);
            sp_ug_locationtype.setOnItemSelectedListener(this);
            sp_ug_servicepiller_type.setOnItemSelectedListener(this);
            sp_ug_poletype.setOnItemSelectedListener(this);
            sp_ug_poletype.setOnItemSelectedListener(this);
            sp_ug_poleheight.setOnItemSelectedListener(this);
            sp_ug_polesupport.setOnItemSelectedListener(this);
            sp_ug_feedepiller_outg_circuits.setOnItemSelectedListener(this);
            sp_ug_tapping_point.setOnItemSelectedListener(this);
            sp_ug_notapping_point.setOnItemSelectedListener(this);
            sp_ug_cablesize.setOnItemSelectedListener(this);
            sp_ug_nocable.setOnItemSelectedListener(this);
            sp_crossing.setOnItemSelectedListener(this);
            sp_ltug_wosurveycompleted.setOnItemSelectedListener(this);
            sp_ltoh_wosurveycompleted.setOnItemSelectedListener(this);

//CheckBox Listner
            ch_oh_aglservices.setOnCheckedChangeListener(this);
            ch_oh_aquaservices.setOnCheckedChangeListener(this);
            ch_oh_industrialservices.setOnCheckedChangeListener(this);
            ch_oh_33kv_vlckts.setOnCheckedChangeListener(this);
            ch_oh_11kv_vlckts.setOnCheckedChangeListener(this);
            ch_oh_6_3kv_vlckts.setOnCheckedChangeListener(this);
            ch_oh_lt_vlckts.setOnCheckedChangeListener(this);
            ch_oh_6_3_crossing.setOnCheckedChangeListener(this);
            ch_oh_11kv_crossing.setOnCheckedChangeListener(this);
            ch_oh_33kv_crossing.setOnCheckedChangeListener(this);
            ch_oh_lt_crossing.setOnCheckedChangeListener(this);
            ch_oh_tc_crossing.setOnCheckedChangeListener(this);
            ch_oh_tm_crossing.setOnCheckedChangeListener(this);


            tv_dtrstrcuture.setText(dtrCode);
            tv_ltcircuit.setText(ltCircuit);

            arcGISDatabase = new ArcGISDatabase(c);

            incrementalSurveyDataVoList = arcGISDatabase.getIncrementalSurveyData("");
            int count = arcGISDatabase.getIncrementalObjectId();
            poleCountCheck = arcGISDatabase.getIncrementalObjectIdCount();
            if (poleCountCheck == 0) {
                //  if(""surveyType)
                int polenum = maxSurveyCount + 1;
                ed_frompole.setText(String.valueOf(count));
                ed_pole_no.setText(String.valueOf(polenum));
               /* ed_frompole.setText(String.valueOf("0"));
                ed_pole_no.setText(String.valueOf(polenum));*/
            } else {
                ed_frompole.setText(String.valueOf(count));
                ed_pole_no.setText(String.valueOf(count + 1));
            }


            if (count > 0) {
                String idtance_Poles = String.valueOf(Math.round(distance));
                tv_distance.setText(idtance_Poles + "  Mtrs");

                lyt_distance.setVisibility(View.GONE);
            } else {
                lyt_distance.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {

            case R.id.sp_ug_dtr_pole_dtr_meter_avilable:
                if ("Yes".equalsIgnoreCase(sp_ug_dtr_pole_dtr_meter_avilable.getSelectedItem().toString())) {
                    lay_ug_dtr_pole_no_of_meter.setVisibility(View.VISIBLE);
                    ug_dtr_pole_dtr_meter_avilable = sp_ug_dtr_pole_dtr_meter_avilable.getSelectedItem().toString();
                } else {
                    ug_dtr_pole_dtr_meter_avilable = "";
                    ug_dtr_pole_no_of_meters = "";
                    sp_ug_dtr_pole_no_of_meters.setSelection(0);
                    lay_ug_dtr_pole_no_of_meter.setVisibility(View.GONE);
                }
                break;
            case R.id.sp_ug_dtr_pole_no_of_meters:
                if (!"Select".equalsIgnoreCase(sp_ug_dtr_pole_no_of_meters.getSelectedItem().toString())) {
                    ug_dtr_pole_no_of_meters = sp_ug_dtr_pole_no_of_meters.getSelectedItem().toString();
                } else {
                    ug_dtr_pole_no_of_meters = "";
                }
                break;

            case R.id.sp_oh_dtr_pole_dtr_meter_avilable:
                if ("Yes".equalsIgnoreCase(sp_oh_dtr_pole_dtr_meter_avilable.getSelectedItem().toString())) {
                    lay_oh_dtr_pole_no_of_meter.setVisibility(View.VISIBLE);
                    oh_dtr_pole_dtr_meter_avilable = sp_oh_dtr_pole_dtr_meter_avilable.getSelectedItem().toString();
                } else {
                    oh_dtr_pole_dtr_meter_avilable = "";
                    oh_dtr_pole_no_of_meters = "";
                    sp_oh_dtr_pole_no_of_meters.setSelection(0);
                    lay_oh_dtr_pole_no_of_meter.setVisibility(View.GONE);
                }
                break;
            case R.id.sp_oh_dtr_pole_no_of_meters:
                if (!"Select".equalsIgnoreCase(sp_oh_dtr_pole_no_of_meters.getSelectedItem().toString())) {
                    oh_dtr_pole_no_of_meters = sp_oh_dtr_pole_no_of_meters.getSelectedItem().toString();
                } else {
                    oh_dtr_pole_no_of_meters = "";
                }
                break;

            case R.id.sp_lineType:

                lineType = sp_lineType.getSelectedItem().toString();
                if ("OverHead".equalsIgnoreCase(lineType)) {
                    lyt_overhead.setVisibility(View.VISIBLE);
                    lyt_underground.setVisibility(View.GONE);
                } else if ("UnderGround".equalsIgnoreCase(lineType)) {
                    lyt_underground.setVisibility(View.VISIBLE);
                    lyt_overhead.setVisibility(View.GONE);
                }

                break;


            case R.id.sp_oh_isendpole:

                oh_isendpole = sp_oh_isendpole.getSelectedItem().toString();

                break;
            case R.id.sp_oh_lineconfig:
                if ("Select".equalsIgnoreCase(sp_oh_lineconfig.getSelectedItem().toString())) {
                    oh_lineconfig = "";
                    /*lay_oh_dtr_pole_dtr_meter_avilable.setVisibility(View.GONE);
                    lay_oh_dtr_pole_no_of_meter.setVisibility(View.GONE);*/
                }/*else if("DTR Pole".equalsIgnoreCase(sp_oh_lineconfig.getSelectedItem().toString())){
                    lay_oh_dtr_pole_dtr_meter_avilable.setVisibility(View.VISIBLE);
                    oh_lineconfig=sp_oh_lineconfig.getSelectedItem().toString();
                }*/ else {
                   /* lay_oh_dtr_pole_dtr_meter_avilable.setVisibility(View.GONE);
                    lay_oh_dtr_pole_no_of_meter.setVisibility(View.GONE);*/
                    oh_lineconfig = sp_oh_lineconfig.getSelectedItem().toString();
                }
                break;

            case R.id.sp_oh_isstreetlight:

                oh_isstreetlight = sp_oh_isstreetlight.getSelectedItem().toString();

                break;

            case R.id.sp_oh_otherthan_streetlight:

                oh_otherthan_streetlight = sp_oh_otherthan_streetlight.getSelectedItem().toString();
                if ("Yes".equalsIgnoreCase(oh_otherthan_streetlight)) {
                    lyt_oh_aglservice.setVisibility(View.VISIBLE);
                    lyt_oh_chacquaservices.setVisibility(View.VISIBLE);
                    lyt_oh_chindustrialservices.setVisibility(View.VISIBLE);
                } else {
                    lyt_oh_aglservice.setVisibility(View.GONE);
                    lyt_oh_chacquaservices.setVisibility(View.GONE);
                    lyt_oh_chindustrialservices.setVisibility(View.GONE);
                    ch_oh_aglservices.setChecked(false);
                    ch_oh_aquaservices.setChecked(false);
                    ch_oh_industrialservices.setChecked(false);
                    edt_oh_aglservices.setText("");
                    edt_oh_aquaservices.setText("");
                    edt_oh_industrialservices.setText("");
                }

                break;
            case R.id.sp_oh_pin_cutpoint:

                oh_pin_cutpoint = sp_oh_pin_cutpoint.getSelectedItem().toString();
                if ("Cut Point".equalsIgnoreCase(oh_pin_cutpoint)) {
                    lyt_oh_is_doublefeeding_pole.setVisibility(View.VISIBLE);
                } else {
                    lyt_oh_is_doublefeeding_pole.setVisibility(View.GONE);
                    sp_oh_is_doublefeeding_pole.setSelection(0);
                }
                break;

            case R.id.sp_oh_is_doublefeeding_pole:

                oh_is_doublefeeding_pole = sp_oh_is_doublefeeding_pole.getSelectedItem().toString();

                break;
            case R.id.sp_oh_pole_type:

                oh_pole_type = sp_oh_pole_type.getSelectedItem().toString();
                if ("PSCC".equalsIgnoreCase(oh_pole_type)) {
                    // String test= (String) mapData.get("POLE_TYPE");
                    List<String> psccList = new ArrayList<>();
                    psccList.add("Select");
                    psccList.add("8.0");
                    psccList.add("9.1");
                    psccList.add("11");
                    // psccList.add(test);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, psccList);
                    sp_oh_pole_height.setAdapter(adapter);

                } else if ("SPUN".equalsIgnoreCase(oh_pole_type)) {
                    List<String> spunList = new ArrayList<>();
                    spunList.add("Select");
                    spunList.add("9.5");
                    spunList.add("11");
                    spunList.add("12.5");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, spunList);
                    sp_oh_pole_height.setAdapter(adapter);

                } else if ("RAIL".equalsIgnoreCase(oh_pole_type)) {
                    List<String> railList = new ArrayList<>();
                    railList.add("Select");
                    railList.add("9.5");
                    railList.add("11");
                    railList.add("12.5");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, railList);
                    sp_oh_pole_height.setAdapter(adapter);

                } else if ("RS JOIST".equalsIgnoreCase(oh_pole_type)) {
                    List<String> rssJoistList = new ArrayList<>();
                    rssJoistList.add("Select");
                    /*rssJoistList.add("8");*/
                    rssJoistList.add("9.1");
                    rssJoistList.add("11");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, rssJoistList);
                    sp_oh_pole_height.setAdapter(adapter);
                } else if ("TOWER".equalsIgnoreCase(oh_pole_type)) {
                    List<String> towertList = new ArrayList<>();
                    towertList.add("Select");
                    towertList.add("M+3");
                    towertList.add("M+6");
                    towertList.add("M+9");
                    towertList.add("M+12");
                    towertList.add("R+6");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, towertList);
                    sp_oh_pole_height.setAdapter(adapter);
                } else if ("Tubular Pole".equalsIgnoreCase(oh_pole_type)) {
                    List<String> tubularList = new ArrayList<>();
                    tubularList.add("Select");
                    tubularList.add("9.1");
                    tubularList.add("11");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, tubularList);
                    sp_oh_pole_height.setAdapter(adapter);
                }
                break;

            case R.id.sp_oh_pole_height:
                if (!"Select".equalsIgnoreCase(sp_oh_pole_height.getSelectedItem().toString())) {
                    oh_pole_height = sp_oh_pole_height.getSelectedItem().toString();
                } else {
                    oh_pole_height = "";
                }


                break;
            case R.id.sp_oh_strut_stay_details:

                oh_strut_stay_details = sp_oh_strut_stay_details.getSelectedItem().toString();

                break;

            case R.id.sp_oh_tapping_point:

                oh_tapping_point = sp_oh_tapping_point.getSelectedItem().toString();
                if ("Yes".equalsIgnoreCase(oh_tapping_point)) {
                    lyt_oh_noof_spurlines.setVisibility(View.VISIBLE);
                } else {
                    lyt_oh_noof_spurlines.setVisibility(View.GONE);
                    edt_oh_tappinglocation.setText("");
                    sp_oh_noof_spurlines.setSelection(0);
                }

                break;

            case R.id.sp_oh_noof_spurlines:

                oh_noof_spurlines = sp_oh_noof_spurlines.getSelectedItem().toString();

                break;

            case R.id.sp_oh_conductor_size:

                oh_conductor_size = sp_oh_conductor_size.getSelectedItem().toString();

                break;

            case R.id.sp_oh_ismulti_circuit:

                oh_ismulti_circuit = sp_oh_ismulti_circuit.getSelectedItem().toString();
                if ("Yes".equalsIgnoreCase(oh_ismulti_circuit)) {
                    lyt_oh_noof_circuits.setVisibility(View.VISIBLE);
                    lyt_spi_no_circuit.setVisibility(View.VISIBLE);
                } else {
                    lyt_oh_noof_circuits.setVisibility(View.GONE);
                    lyt_spi_no_circuit.setVisibility(View.GONE);
                    lyt_oh_multivoltage.setVisibility(View.GONE);
                    sp_oh_noof_circuits.setSelection(0);

                }

                break;

            case R.id.sp_oh_noof_circuits:

                oh_noof_circuits = sp_oh_noof_circuits.getSelectedItem().toString();

                break;

            case R.id.sp_oh_multi_voltage_same_voltage:

                oh_multi_voltage_same_voltage = sp_oh_multi_voltage_same_voltage.getSelectedItem().toString();
                if ("Multi Voltage".equalsIgnoreCase(oh_multi_voltage_same_voltage)) {
                    lyt_oh_multivoltage.setVisibility(View.VISIBLE);
                    // lyt_spi_no_circuit.setVisibility(View.GONE);
                    oh_noof_circuits = "";
                } else {
                    // lyt_spi_no_circuit.setVisibility(View.VISIBLE);
                    lyt_oh_multivoltage.setVisibility(View.GONE);
                    ch_oh_33kv_vlckts.setChecked(false);
                    ch_oh_11kv_vlckts.setChecked(false);
                    ch_oh_6_3kv_vlckts.setChecked(false);
                    ch_oh_lt_vlckts.setChecked(false);
                    edt_oh_33kv_vlckts.setText("");
                    edt_oh_11kv_vlckts.setText("");
                    edt_oh_6_3kv_vlckts.setText("");
                    edt_oh_lt_vlckts.setText("");
                }
                break;
            //UG
            case R.id.sp_ug_isendpole:

                ug_isendpole = sp_ug_isendpole.getSelectedItem().toString();
                break;
            case R.id.sp_ug_locationtype:

                ug_locationtype = sp_ug_locationtype.getSelectedItem().toString();
                if ("Panel Board".equalsIgnoreCase(ug_locationtype) || "Service Pillar".equalsIgnoreCase(ug_locationtype)) {
                    lyt_ug_servicepiller.setVisibility(View.VISIBLE);
                    lyt_ug_poletype.setVisibility(View.GONE);
                    lyt_ug_feederpiller.setVisibility(View.GONE);
                    lay_ug_dtr_pole_dtr_meter_avilable.setVisibility(View.GONE);
                    lay_ug_dtr_pole_no_of_meter.setVisibility(View.GONE);
                    ug_poletype = "";
                    ug_poleheight = "";
                    ug_polesupport = "";
                    sp_ug_poletype.setSelection(0);
                    sp_ug_poleheight.setSelection(0);
                    sp_ug_polesupport.setSelection(0);
                    sp_ug_feedepiller_outg_circuits.setSelection(0);

                } else if ("Pole".equalsIgnoreCase(ug_locationtype) || "Rail way Crsossing".equalsIgnoreCase(ug_locationtype) || "Road Crossing".equalsIgnoreCase(ug_locationtype)) {
                    lyt_ug_poletype.setVisibility(View.VISIBLE);
                    lyt_ug_feederpiller.setVisibility(View.GONE);
                    lyt_ug_servicepiller.setVisibility(View.GONE);
                    lay_ug_dtr_pole_dtr_meter_avilable.setVisibility(View.GONE);
                    lay_ug_dtr_pole_no_of_meter.setVisibility(View.GONE);
                    sp_ug_servicepiller_type.setSelection(0);
                    sp_ug_feedepiller_outg_circuits.setSelection(0);
                } else if ("Feeder Pillar".equalsIgnoreCase(ug_locationtype)) {
                    lyt_ug_feederpiller.setVisibility(View.VISIBLE);
                    lyt_ug_servicepiller.setVisibility(View.GONE);
                    lyt_ug_poletype.setVisibility(View.GONE);
                    lay_ug_dtr_pole_dtr_meter_avilable.setVisibility(View.GONE);
                    lay_ug_dtr_pole_no_of_meter.setVisibility(View.GONE);
                    sp_ug_servicepiller_type.setSelection(0);
                    sp_ug_poletype.setSelection(0);
                    sp_ug_poleheight.setSelection(0);
                    sp_ug_polesupport.setSelection(0);
                }/*else if("DTR Pole".equalsIgnoreCase(ug_locationtype)){
                    lay_ug_dtr_pole_dtr_meter_avilable.setVisibility(View.VISIBLE);
                    lyt_ug_feederpiller.setVisibility(View.GONE);
                    lyt_ug_servicepiller.setVisibility(View.GONE);
                    lyt_ug_poletype.setVisibility(View.GONE);
                    sp_ug_servicepiller_type.setSelection(0);
                    sp_ug_poletype.setSelection(0);
                    sp_ug_poleheight.setSelection(0);
                    sp_ug_polesupport.setSelection(0);
                    sp_ug_feedepiller_outg_circuits.setSelection(0);
                    ug_servicepiller_type="";
                    ug_poletype="";
                    ug_poleheight="";
                    ug_polesupport="";
                }*/ else {
                    lay_ug_dtr_pole_dtr_meter_avilable.setVisibility(View.GONE);
                    lay_ug_dtr_pole_no_of_meter.setVisibility(View.GONE);
                    lyt_ug_poletype.setVisibility(View.GONE);
                    lyt_ug_servicepiller.setVisibility(View.GONE);
                    lyt_ug_feederpiller.setVisibility(View.GONE);
                    sp_ug_servicepiller_type.setSelection(0);
                    ug_servicepiller_type = "";
                    ug_poletype = "";
                    ug_poleheight = "";
                    ug_polesupport = "";
                    sp_ug_poletype.setSelection(0);
                    sp_ug_poleheight.setSelection(0);
                    sp_ug_polesupport.setSelection(0);
                    sp_ug_feedepiller_outg_circuits.setSelection(0);
                }
                break;
            case R.id.sp_ug_servicepiller_type:

                ug_servicepiller_type = sp_ug_servicepiller_type.getSelectedItem().toString();
                break;
            case R.id.sp_ug_poletype:

                ug_poletype = sp_ug_poletype.getSelectedItem().toString();
                if ("PSCC".equalsIgnoreCase(ug_poletype)) {
                    // String test= (String) mapData.get("POLE_TYPE");
                    List<String> psccList = new ArrayList<>();
                    psccList.add("Select");
                    psccList.add("8.0");
                    psccList.add("9.1");
                    psccList.add("11");
                    // psccList.add(test);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, psccList);
                    sp_ug_poleheight.setAdapter(adapter);

                } else if ("SPUN".equalsIgnoreCase(ug_poletype)) {
                    List<String> spunList = new ArrayList<>();
                    spunList.add("Select");
                    spunList.add("9.5");
                    spunList.add("11");
                    spunList.add("12.5");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, spunList);
                    sp_ug_poleheight.setAdapter(adapter);

                } else if ("RAIL".equalsIgnoreCase(ug_poletype)) {
                    List<String> railList = new ArrayList<>();
                    railList.add("Select");
                    railList.add("9.5");
                    railList.add("11");
                    railList.add("12.5");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, railList);
                    sp_ug_poleheight.setAdapter(adapter);

                } else if ("RS JOIST".equalsIgnoreCase(ug_poletype)) {
                    List<String> rssJoistList = new ArrayList<>();
                    rssJoistList.add("Select");
                    /*rssJoistList.add("8");*/
                    rssJoistList.add("9.1");
                    rssJoistList.add("11");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, rssJoistList);
                    sp_ug_poleheight.setAdapter(adapter);
                } else if ("TOWER".equalsIgnoreCase(ug_poletype)) {
                    List<String> towertList = new ArrayList<>();
                    towertList.add("Select");
                    towertList.add("M+3");
                    towertList.add("M+6");
                    towertList.add("M+9");
                    towertList.add("M+12");
                    towertList.add("R+6");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, towertList);
                    sp_ug_poleheight.setAdapter(adapter);
                } else if ("Tubular Pole".equalsIgnoreCase(ug_poletype)) {
                    List<String> tubularList = new ArrayList<>();
                    tubularList.add("Select");
                    tubularList.add("9.1");
                    tubularList.add("11");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, tubularList);
                    sp_ug_poleheight.setAdapter(adapter);
                }
                break;
            case R.id.sp_ug_poleheight:

                ug_poleheight = sp_ug_poleheight.getSelectedItem().toString();
                break;

            case R.id.sp_ug_polesupport:

                ug_polesupport = sp_ug_polesupport.getSelectedItem().toString();
                break;

            case R.id.sp_ug_feedepiller_outg_circuits:

                ug_feedepiller_outg_circuits = sp_ug_feedepiller_outg_circuits.getSelectedItem().toString();
                break;

            case R.id.sp_ug_tapping_point:

                ug_tapping_point = sp_ug_tapping_point.getSelectedItem().toString();
                if ("Yes".equalsIgnoreCase(ug_tapping_point)) {
                    lyt_ug_tappingpoint.setVisibility(View.VISIBLE);
                } else {
                    lyt_ug_tappingpoint.setVisibility(View.GONE);
                    sp_ug_notapping_point.setSelection(0);
                    edt_ug_tappinglocation.setText("");
                }
                break;
            case R.id.sp_ug_notapping_point:

                ug_notapping_point = sp_ug_notapping_point.getSelectedItem().toString();
                break;

            case R.id.sp_ug_cablesize:

                ug_cablesize = sp_ug_cablesize.getSelectedItem().toString();
                break;
            case R.id.sp_ug_nocable:

                ug_nocable = sp_ug_nocable.getSelectedItem().toString();
                break;

            case R.id.sp_crossing:

                crossing = sp_crossing.getSelectedItem().toString();
                if ("YES".equalsIgnoreCase(crossing)) {
                    lyt_oh_crossing.setVisibility(View.VISIBLE);
                } else {
                    lyt_oh_crossing.setVisibility(View.GONE);
                }

                break;

            case R.id.sp_ltug_wosurveycompleted:
                ltug_wosurveycompleted = sp_ltug_wosurveycompleted.getSelectedItem().toString();
                break;

            case R.id.sp_ltoh_wosurveycompleted:
                ltoh_wosurveycompleted = sp_ltoh_wosurveycompleted.getSelectedItem().toString();
                break;


        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.ch_oh_aglservices:
                if (ch_oh_aglservices.isChecked()) {
                    lyt_oh_edt_aglservices.setVisibility(View.VISIBLE);
                } else {
                    lyt_oh_edt_aglservices.setVisibility(View.GONE);
                    edt_oh_aglservices.setText("");

                }
                break;
            case R.id.ch_oh_aquaservices:
                if (ch_oh_aquaservices.isChecked()) {
                    lyt_oh_edt_aquaservices.setVisibility(View.VISIBLE);
                } else {
                    lyt_oh_edt_aquaservices.setVisibility(View.GONE);
                    edt_oh_aquaservices.setText("");

                }
                break;

            case R.id.ch_oh_industrialservices:
                if (ch_oh_industrialservices.isChecked()) {
                    lyt_oh_edt_industrialservices.setVisibility(View.VISIBLE);
                } else {
                    lyt_oh_edt_industrialservices.setVisibility(View.GONE);
                    edt_oh_industrialservices.setText("");
                }
                break;

            case R.id.ch_oh_33kv_vlckts:
                if (ch_oh_33kv_vlckts.isChecked()) {
                    lyt_oh_33kv_vlckts.setVisibility(View.VISIBLE);

                } else {
                    lyt_oh_33kv_vlckts.setVisibility(View.GONE);
                    edt_oh_33kv_vlckts.setText("");

                }
                break;

            case R.id.ch_oh_11kv_vlckts:
                if (ch_oh_11kv_vlckts.isChecked()) {
                    lyt_oh_11kv_vlckts.setVisibility(View.VISIBLE);
                } else {
                    lyt_oh_11kv_vlckts.setVisibility(View.GONE);
                    edt_oh_11kv_vlckts.setText("");
                    noof_11kvCkts = 0;

                }
                break;

            case R.id.ch_oh_6_3kv_vlckts:
                if (ch_oh_6_3kv_vlckts.isChecked()) {
                    lyt_oh_6_3kv_vlckts.setVisibility(View.VISIBLE);
                } else {
                    lyt_oh_6_3kv_vlckts.setVisibility(View.GONE);
                    edt_oh_6_3kv_vlckts.setText("");
                    noof_6_3kvCkts = 0;

                }
                break;

            case R.id.ch_oh_lt_vlckts:
                if (ch_oh_lt_vlckts.isChecked()) {
                    lyt_oh_lt_vlckts.setVisibility(View.VISIBLE);
                } else {
                    lyt_oh_lt_vlckts.setVisibility(View.GONE);
                    edt_oh_lt_vlckts.setText("");
                    noof_ltCkts = 0;
                }
                break;


            case R.id.ch_oh_6_3_crossing:
                if (ch_oh_6_3_crossing.isChecked()) {
                    lyt_oh_6_3_crossing.setVisibility(View.VISIBLE);

                } else {
                    lyt_oh_6_3_crossing.setVisibility(View.GONE);
                    edt_oh_6_3_crossing.setText("");


                }
                break;

            case R.id.ch_oh_11kv_crossing:
                if (ch_oh_11kv_crossing.isChecked()) {
                    lyt_oh_11kv_crossing.setVisibility(View.VISIBLE);
                } else {
                    lyt_oh_11kv_vlckts.setVisibility(View.GONE);
                    edt_oh_11kv_crossing.setText("");

                }
                break;

            case R.id.ch_oh_33kv_crossing:
                if (ch_oh_33kv_crossing.isChecked()) {
                    lyt_oh_33kv_crossing.setVisibility(View.VISIBLE);
                } else {
                    lyt_oh_33kv_crossing.setVisibility(View.GONE);
                    edt_oh_33kv_crossing.setText("");
                }
                break;

            case R.id.ch_oh_lt_crossing:
                if (ch_oh_lt_crossing.isChecked()) {
                    lyt_oh_lt_crossing.setVisibility(View.VISIBLE);
                } else {
                    lyt_oh_lt_crossing.setVisibility(View.GONE);
                    edt_oh_lt_crossing.setText("");
                }
                break;

            case R.id.ch_oh_tc_crossing:
                if (ch_oh_tc_crossing.isChecked()) {
                    lyt_oh_tc_crossing.setVisibility(View.VISIBLE);
                } else {
                    lyt_oh_tc_crossing.setVisibility(View.GONE);
                    edt_oh_tc_crossing.setText("");
                }
                break;

            case R.id.ch_oh_tm_crossing:
                if (ch_oh_tm_crossing.isChecked()) {
                    lyt_oh_tm_crossing.setVisibility(View.VISIBLE);
                } else {
                    lyt_oh_tm_crossing.setVisibility(View.GONE);
                    edt_oh_tm_crossing.setText("");
                }
                break;


        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_clear:

                callout.dismiss();
                graphicsOverlay.getGraphics().remove(graphic);
                dismiss();
                break;

            case R.id.btn_add:
                if ("OverHead".equalsIgnoreCase(lineType)) {
                    addOhData();
                } else if ("UnderGround".equalsIgnoreCase(lineType)) {
                    underGround();
                }

                break;
            case R.id.btn_lttopclear:
                callout.dismiss();
                graphicsOverlay.getGraphics().remove(graphic);
                dismiss();
                break;

        }


    }

    public void addOhData() {
        if ("Select".equalsIgnoreCase(sp_oh_lineconfig.getSelectedItem().toString())) {
            Toast.makeText(c, "Select Line Configuration", Toast.LENGTH_SHORT).show();
        }/*else if("DTR Pole".equalsIgnoreCase(oh_lineconfig)&&"Yes".equalsIgnoreCase(oh_dtr_pole_dtr_meter_avilable)&&"Select".equalsIgnoreCase(oh_dtr_pole_no_of_meters)){
                Toast.makeText(c,"Select No Of Meters",Toast.LENGTH_SHORT).show();
        }*/ else if ("Select".equalsIgnoreCase(sp_oh_pole_height.getSelectedItem().toString())) {
            Toast.makeText(c, "Select Pole Height", Toast.LENGTH_SHORT).show();
        } else if ("Multi Voltage".equalsIgnoreCase(oh_multi_voltage_same_voltage) && !ch_oh_33kv_vlckts.isChecked() && !ch_oh_11kv_vlckts.isChecked() && !ch_oh_6_3kv_vlckts.isChecked() && !ch_oh_lt_vlckts.isChecked()) {
            //"".equalsIgnoreCase(edt_oh_33kv_vlckts.getText().toString()) && "".equalsIgnoreCase(edt_oh_11kv_vlckts.getText().toString()) && "".equalsIgnoreCase(edt_oh_6_3kv_vlckts.getText().toString()) && "".equalsIgnoreCase(edt_oh_lt_vlckts.getText().toString())){
            Toast.makeText(c, "Select Any One Of Voltage Circuit", Toast.LENGTH_SHORT).show();
        } else if (ch_oh_33kv_vlckts.isChecked() && "".equalsIgnoreCase(edt_oh_33kv_vlckts.getText().toString())) {
            Toast.makeText(c, "Enter No. of 33kv Voltage Ckts", Toast.LENGTH_SHORT).show();
        } else if (ch_oh_11kv_vlckts.isChecked() && "".equalsIgnoreCase(edt_oh_11kv_vlckts.getText().toString())) {
            Toast.makeText(c, "Enter No. of 11kv Voltage Ckts", Toast.LENGTH_SHORT).show();
        } else if (ch_oh_6_3kv_vlckts.isChecked() && "".equalsIgnoreCase(edt_oh_6_3kv_vlckts.getText().toString())) {
            Toast.makeText(c, "Enter No. of 6.3kv Voltage Ckts", Toast.LENGTH_SHORT).show();
        } else if (ch_oh_lt_vlckts.isChecked() && "".equalsIgnoreCase(edt_oh_lt_vlckts.getText().toString())) {
            Toast.makeText(c, "Enter No. of LT Voltage Ckts", Toast.LENGTH_SHORT).show();
        } else if ("YES".equalsIgnoreCase(crossing) && !ch_oh_6_3_crossing.isChecked() && !ch_oh_11kv_crossing.isChecked() && !ch_oh_33kv_crossing.isChecked() && !ch_oh_lt_crossing.isChecked() && !ch_oh_tc_crossing.isChecked() && !ch_oh_tm_crossing.isChecked()) {
            //"".equalsIgnoreCase(edt_oh_33kv_vlckts.getText().toString()) && "".equalsIgnoreCase(edt_oh_11kv_vlckts.getText().toString()) && "".equalsIgnoreCase(edt_oh_6_3kv_vlckts.getText().toString()) && "".equalsIgnoreCase(edt_oh_lt_vlckts.getText().toString())){
            Toast.makeText(c, "Select Any One Of Crossing", Toast.LENGTH_SHORT).show();
        } else if (ch_oh_6_3_crossing.isChecked() && "".equalsIgnoreCase(edt_oh_6_3_crossing.getText().toString())) {
            Toast.makeText(c, "Enter No. of 6.3 Crossings", Toast.LENGTH_SHORT).show();
        } else if (ch_oh_11kv_crossing.isChecked() && "".equalsIgnoreCase(edt_oh_11kv_crossing.getText().toString())) {
            Toast.makeText(c, "Enter No. of 11kv Crossings", Toast.LENGTH_SHORT).show();
        } else if (ch_oh_33kv_crossing.isChecked() && "".equalsIgnoreCase(edt_oh_33kv_crossing.getText().toString())) {
            Toast.makeText(c, "Enter No. of 33kv Crossings", Toast.LENGTH_SHORT).show();
        } else if (ch_oh_lt_crossing.isChecked() && "".equalsIgnoreCase(edt_oh_lt_crossing.getText().toString())) {
            Toast.makeText(c, "Enter No. of LT Crossings", Toast.LENGTH_SHORT).show();
        } else if (ch_oh_tc_crossing.isChecked() && "".equalsIgnoreCase(edt_oh_tc_crossing.getText().toString())) {
            Toast.makeText(c, "Enter No. of Tele Communication Crossings", Toast.LENGTH_SHORT).show();
        } else if (ch_oh_tm_crossing.isChecked() && "".equalsIgnoreCase(edt_oh_tm_crossing.getText().toString())) {
            Toast.makeText(c, "Enter No. of Transmission Crossings", Toast.LENGTH_SHORT).show();
        } else {
            if (dtrCode != null & !dtrCode.isEmpty() && !ed_pole_no.getText().toString().isEmpty() && !ed_frompole.getText().toString().isEmpty() && !x.isEmpty() && !y.isEmpty()) {
                if (Integer.parseInt(ed_pole_no.getText().toString()) == Integer.parseInt(ed_frompole.getText().toString())) {
                    Toast.makeText(c, "Pole No and From Pole No Should Not Be Same Contact Admin", Toast.LENGTH_LONG).show();

                } else {
                    //List<LTCktsResponseDataVo> ltCktsResponseDataVos = arcGISDatabase.getDTRMasterData("");
                    List<String> workOrderStatus = arcGISDatabase.getIncrementalFeederComplaint(String.format("SELECT DISTINCT STATUS FROM INCREMENTAL_DTR_TABLE WHERE WORK_ORDERID='%s'", woId));
                    List<LTCktsDataVo> ltCktsResponseDataVos = arcGISDatabase.getIncrementalDTRMasterData("");
                    // SurveyDataVo surveyDataVo = new SurveyDataVo();
                    IncrementalSurveyDataVo surveyDataVo = new IncrementalSurveyDataVo();
                    surveyDataVo.setDTR_STRUCTURE(dtrCode);
                    surveyDataVo.setLT_CIRCUIT(ltCircuit);
                    surveyDataVo.setPOLE_NO(Integer.parseInt(ed_pole_no.getText().toString()));
                    surveyDataVo.setFROM_POLE(Integer.parseInt(ed_frompole.getText().toString()));
                    surveyDataVo.setLINE_TYPE(lineType);
                    surveyDataVo.setIS_END_POLE(oh_isendpole);
                    surveyDataVo.setLINE_CONFIGURATION(oh_lineconfig);
                    surveyDataVo.setIS_STREETLIGHT_AV(oh_isstreetlight);
                    surveyDataVo.setIS_SERVICES_OTHERTHAN_SL(oh_otherthan_streetlight);
            /*surveyDataVo.setIS_SERVICES_OTHERTHAN_SL(oh_otherthan_streetlight);
            surveyDataVo.setNO_AGL_SERVICES(edt_oh_aglservices.getText().toString());
            surveyDataVo.setNO_AQUA_SERVICES(edt_oh_aquaservices.getText().toString());
            surveyDataVo.setNO_INDUSTRIAL_SERVICES(edt_oh_industrialservices.getText().toString());*/
                    //Unused

                    surveyDataVo.setNO_AGL_SERVICES("");
                    surveyDataVo.setNO_AQUA_SERVICES("");
                    surveyDataVo.setNO_INDUSTRIAL_SERVICES("");
                    surveyDataVo.setPIN_CUT(oh_pin_cutpoint);
                    if ("Cut Point".equalsIgnoreCase(oh_pin_cutpoint)) {
                        surveyDataVo.setIS_DOUBLE_FEEDING(oh_is_doublefeeding_pole);
                    } else {
                        surveyDataVo.setIS_DOUBLE_FEEDING("");
                    }

                    surveyDataVo.setPOLE_TYPE(oh_pole_type);
                    surveyDataVo.setPOLE_HEIGHT(oh_pole_height);
                    surveyDataVo.setSTRUT_STAY(oh_strut_stay_details);
                    surveyDataVo.setTAPPING_POINT(oh_tapping_point);
                    if (!"Select".equalsIgnoreCase(sp_oh_noof_spurlines.getSelectedItem().toString()) && "Yes".equalsIgnoreCase(oh_tapping_point)) {
                        surveyDataVo.setNO_OF_SPURLINES(oh_noof_spurlines);
                    } else {
                        surveyDataVo.setNO_OF_SPURLINES("");

                    }

                    surveyDataVo.setTAPPING_LOCATION(edt_oh_tappinglocation.getText().toString());
                    surveyDataVo.setCONDUCTOR_SIZE(oh_conductor_size);
                    surveyDataVo.setIS_MULTI_CIRCUIT(oh_ismulti_circuit);
            /*if("Yes".equalsIgnoreCase(oh_ismulti_circuit)){
                surveyDataVo.setNO_OF_CIRCUITS(oh_noof_circuits);
                surveyDataVo.setMULTI_VOLTAGE(oh_multi_voltage_same_voltage);
            }else {
                surveyDataVo.setNO_OF_CIRCUITS("");
                surveyDataVo.setMULTI_VOLTAGE("");
            }*/
                    if (!"".equalsIgnoreCase(edt_oh_33kv_vlckts.getText().toString())) {
                        noof_33kvCkts = Integer.parseInt(edt_oh_33kv_vlckts.getText().toString());
                    }

                    if (!"".equalsIgnoreCase(edt_oh_11kv_vlckts.getText().toString())) {
                        noof_11kvCkts = Integer.parseInt(edt_oh_11kv_vlckts.getText().toString());
                    }

                    if (!"".equalsIgnoreCase(edt_oh_6_3kv_vlckts.getText().toString())) {
                        noof_6_3kvCkts = Integer.parseInt(edt_oh_6_3kv_vlckts.getText().toString());
                    }

                    if (!"".equalsIgnoreCase(edt_oh_lt_vlckts.getText().toString())) {
                        noof_ltCkts = Integer.parseInt(edt_oh_lt_vlckts.getText().toString());
                    }

                    if ("Yes".equalsIgnoreCase(oh_ismulti_circuit) && "Multi Voltage".equalsIgnoreCase(oh_multi_voltage_same_voltage)) {
                        int numOfCkts = noof_33kvCkts + noof_11kvCkts + noof_6_3kvCkts + noof_ltCkts;
                        surveyDataVo.setNO_OF_CIRCUITS(String.valueOf(numOfCkts));
                        surveyDataVo.setMULTI_VOLTAGE(oh_multi_voltage_same_voltage);
                    } else if ("Yes".equalsIgnoreCase(oh_ismulti_circuit) && "Same Voltage".equalsIgnoreCase(oh_multi_voltage_same_voltage)) {
                        surveyDataVo.setNO_OF_CIRCUITS(oh_noof_circuits);
                        surveyDataVo.setMULTI_VOLTAGE(oh_multi_voltage_same_voltage);
                    } else {
                        surveyDataVo.setNO_OF_CIRCUITS("");
                        surveyDataVo.setMULTI_VOLTAGE("");
                    }
                    surveyDataVo.setNO_OF33KV_CKTS(edt_oh_33kv_vlckts.getText().toString());
                    surveyDataVo.setNO_OF11KV_CKTS(edt_oh_11kv_vlckts.getText().toString());
                    surveyDataVo.setNO_OF6_3KV_VOLTAGE_CKTS(edt_oh_6_3kv_vlckts.getText().toString());
                    surveyDataVo.setNO_OF_LT_CKTS(edt_oh_lt_vlckts.getText().toString());
                    surveyDataVo.setPOLE_PAINTING_CODE(edt_oh_pole_paintingcode.getText().toString());
                    surveyDataVo.setREMARKS(edt_oh_remarks.getText().toString());
                    surveyDataVo.setLAT(x);
                    surveyDataVo.setLAN(y);
                    //surveyDataVo.setFEEDER_ID(ltCktsResponseDataVos.get(0).getFEEDER_ID());
                    surveyDataVo.setFEEDER_ID(feederId);
                    surveyDataVo.setDTR_METER_AVILABLE(oh_dtr_pole_dtr_meter_avilable);
                    surveyDataVo.setDTR_NO_OF_METERS(oh_dtr_pole_no_of_meters);
                    if ("YES".equalsIgnoreCase(crossing)) {
                        surveyDataVo.setIS_CROSSINGS_AV("YES");
                    } else {
                        surveyDataVo.setIS_CROSSINGS_AV("NO");
                    }
                    surveyDataVo.setNOOF_6_3KV_CROSSINGS(edt_oh_6_3_crossing.getText().toString());
                    surveyDataVo.setNOOF_11KV_CROSSINGS(edt_oh_11kv_crossing.getText().toString());
                    surveyDataVo.setNOOF_33KV_CROSSINGS(edt_oh_33kv_crossing.getText().toString());
                    surveyDataVo.setNOOF_LT_CROSSINGS(edt_oh_lt_crossing.getText().toString());
                    surveyDataVo.setNOOF_TC_CROSSINGS(edt_oh_tc_crossing.getText().toString());
                    surveyDataVo.setNOOF_TM_CROSSINGS(edt_oh_tm_crossing.getText().toString());
                    surveyDataVo.setSURVEY_FLAG("N");
                    surveyDataVo.setWORK_ORDERID(woId);
                    surveyDataVo.setWORKORDER_STATUS(workOrderStatus.get(0));
                    surveyDataVo.setIS_SURVEY_COMPLETED(ltoh_wosurveycompleted);
                    arcGISDatabase.addIncrementalSurveyData(surveyDataVo);


                    dismiss();
                    callout.dismiss();
                    graphicsOverlay.getGraphics().remove(graphic);
                    if ("YES".equalsIgnoreCase(ltoh_wosurveycompleted)) {
                        arcGISDatabase.upDate(String.format("UPDATE WO_STATUS_TABLE SET WO_STATUS='Y' WHERE WO_ID='%s'", woId));
                    }
                    if ("Yes".equalsIgnoreCase(oh_isendpole)) {
                        Intent intent = new Intent(c, HomeActivity.class);
                        c.startActivity(intent);
                        c.finish();
                    }
                    Toast.makeText(c, "Pole location captured successfully..", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    public void underGround() {
        if (dtrCode != null & !dtrCode.isEmpty() && !ed_pole_no.getText().toString().isEmpty() && !ed_frompole.getText().toString().isEmpty() && !x.isEmpty() && !y.isEmpty()) {
            if (Integer.parseInt(ed_pole_no.getText().toString()) == Integer.parseInt(ed_frompole.getText().toString())) {
                Toast.makeText(c, "Pole No and From Pole No Should Not Be Same Contact Admin", Toast.LENGTH_LONG).show();

            }/*else if("DTR Pole".equalsIgnoreCase(lineType)&&"Yes".equalsIgnoreCase(ug_dtr_pole_dtr_meter_avilable)&&"Select".equalsIgnoreCase(oh_dtr_pole_no_of_meters)){
                Toast.makeText(c,"Select No Of Meters",Toast.LENGTH_SHORT).show();
            }*/ else {
                List<String> workOrderStatus = arcGISDatabase.getIncrementalFeederComplaint(String.format("SELECT DISTINCT STATUS FROM INCREMENTAL_DTR_TABLE WHERE WORK_ORDERID='%s'", woId));
                // List<LTCktsResponseDataVo> ltCktsResponseDataVos = arcGISDatabase.getDTRMasterData("");
                List<LTCktsDataVo> ltCktsResponseDataVos = arcGISDatabase.getIncrementalDTRMasterData("");
                //SurveyDataVo surveyDataVos = new SurveyDataVo();
                IncrementalSurveyDataVo surveyDataVos = new IncrementalSurveyDataVo();
                surveyDataVos.setDTR_STRUCTURE(dtrCode);
                surveyDataVos.setLT_CIRCUIT(ltCircuit);
                surveyDataVos.setPOLE_NO(Integer.parseInt(ed_pole_no.getText().toString()));
                surveyDataVos.setFROM_POLE(Integer.parseInt(ed_frompole.getText().toString()));
                surveyDataVos.setLINE_TYPE(lineType);
                surveyDataVos.setIS_END_POLE(ug_isendpole);
                surveyDataVos.setLOCATION_TYPE(ug_locationtype);
                surveyDataVos.setLINE_CONFIGURATION(ug_locationtype);
                if ("Panel Board".equalsIgnoreCase(ug_locationtype) || "Service Pillar".equalsIgnoreCase(ug_locationtype)) {
                    surveyDataVos.setSERVICE_PILLERTYPE(ug_servicepiller_type);

                } else {
                    surveyDataVos.setSERVICE_PILLERTYPE("");
                }
                if ("Service Pillar".equalsIgnoreCase(ug_locationtype)) {
                    surveyDataVos.setIS_SERVICES_OTHERTHAN_SL("YES");
                } else {
                    surveyDataVos.setIS_SERVICES_OTHERTHAN_SL("NO");
                }

                if ("Pole".equalsIgnoreCase(ug_locationtype) || "Rail way Crsossing".equalsIgnoreCase(ug_locationtype) || "Road Crossing".equalsIgnoreCase(ug_locationtype)) {
                    surveyDataVos.setPOLE_TYPE(ug_poletype);
                    surveyDataVos.setPOLE_HEIGHT(ug_poleheight);
                    surveyDataVos.setSTRUT_STAY(ug_polesupport);
                } else {
                    surveyDataVos.setPOLE_TYPE("");
                    surveyDataVos.setPOLE_HEIGHT("");
                    surveyDataVos.setSTRUT_STAY("");
                }
                if ("Feeder Pillar".equalsIgnoreCase(ug_locationtype)) {
                    surveyDataVos.setFEEDERPILLET_OUTGN_CKT(ug_feedepiller_outg_circuits);
                } else {
                    surveyDataVos.setFEEDERPILLET_OUTGN_CKT("");
                }
                surveyDataVos.setTAPPING_POINT(ug_tapping_point);
                if ("Yes".equalsIgnoreCase(ug_tapping_point)) {
                    surveyDataVos.setNO_OF_SPURLINES(ug_notapping_point);
                    surveyDataVos.setTAPPING_LOCATION(edt_ug_tappinglocation.getText().toString());
                } else {
                    surveyDataVos.setNO_OF_SPURLINES("");
                    surveyDataVos.setTAPPING_LOCATION("");
                }
                surveyDataVos.setCABLE_SIZE(ug_cablesize);
                surveyDataVos.setNO_CABLES(ug_nocable);
                surveyDataVos.setPOLE_PAINTING_CODE(edt_ug_pole_paintingcode.getText().toString());
                surveyDataVos.setREMARKS(edt_ug_remarks.getText().toString());
                // surveyDataVos.setFEEDER_ID(ltCktsResponseDataVos.get(0).getFEEDER_ID());
                surveyDataVos.setFEEDER_ID(feederId);
                surveyDataVos.setLAT(x);
                surveyDataVos.setLAN(y);
                surveyDataVos.setSURVEY_FLAG("N");
                surveyDataVos.setDTR_METER_AVILABLE(ug_dtr_pole_dtr_meter_avilable);
                surveyDataVos.setDTR_NO_OF_METERS(ug_dtr_pole_no_of_meters);
                surveyDataVos.setWORK_ORDERID(woId);
                surveyDataVos.setWORKORDER_STATUS(workOrderStatus.get(0));
                surveyDataVos.setIS_SURVEY_COMPLETED(ltug_wosurveycompleted);
                arcGISDatabase.addIncrementalSurveyData(surveyDataVos);
                dismiss();
                callout.dismiss();
                graphicsOverlay.getGraphics().remove(graphic);


                if ("YES".equalsIgnoreCase(ltug_wosurveycompleted)) {
                    arcGISDatabase.upDate(String.format("UPDATE WO_STATUS_TABLE SET WO_STATUS='Y' WHERE WO_ID='%s'", woId));
                }

                if ("Yes".equalsIgnoreCase(ug_isendpole)) {
                    Intent intent = new Intent(c, HomeActivity.class);
                    c.startActivity(intent);
                    c.finish();
                }
                Toast.makeText(c, "Pole location captured successfully..", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

