package com.apepdcl.gis_survey.pmi_old.module_11kv;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.pmi_old.module_11kv.activities.PMIArcGISMapActivity11kv;
import com.apepdcl.gis_survey.pmi_old.module_11kv.activities.PMIHomeActivity;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMISurveyDataVo;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class PMILTDialog extends Dialog implements AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener {


    public Activity c;
    private TextView tv_dtrstrcuture,tv_ltcircuit,tv_distance;
    private EditText ed_pole_no,ed_frompole;
    private Spinner sp_lineType,sp_oh_isendpole,sp_oh_lineconfig,sp_oh_isstreetlight,sp_oh_otherthan_streetlight,sp_oh_pin_cutpoint,sp_oh_is_doublefeeding_pole,sp_oh_pole_type,sp_oh_pole_height,sp_oh_strut_stay_details,sp_oh_tapping_point,sp_oh_noof_spurlines,sp_oh_conductor_size,sp_oh_ismulti_circuit,sp_oh_noof_circuits,sp_oh_multi_voltage_same_voltage,
            sp_ug_isendpole,sp_ug_locationtype,sp_ug_servicepiller_type,sp_ug_poletype,sp_ug_poleheight,sp_ug_polesupport,sp_ug_feedepiller_outg_circuits,sp_ug_tapping_point,sp_ug_notapping_point,sp_ug_cablesize,sp_ug_nocable,sp_crossing;
    private String lineType,oh_isendpole="",oh_lineconfig,oh_isstreetlight="",oh_otherthan_streetlight,oh_pin_cutpoint,oh_is_doublefeeding_pole,oh_pole_type,oh_pole_height,oh_strut_stay_details,oh_tapping_point,oh_noof_spurlines,oh_conductor_size,oh_ismulti_circuit,oh_noof_circuits,oh_multi_voltage_same_voltage,
            ug_isendpole,ug_locationtype,ug_servicepiller_type,ug_poletype,ug_poleheight,ug_polesupport,ug_feedepiller_outg_circuits,ug_tapping_point,ug_notapping_point,ug_cablesize,ug_nocable,crossing;
    private LinearLayout lyt_oh_aglservice,lyt_oh_edt_aglservices,lyt_oh_chacquaservices,lyt_oh_edt_aquaservices,lyt_oh_chindustrialservices,lyt_oh_edt_industrialservices,lyt_oh_is_doublefeeding_pole,lyt_oh_noof_spurlines,lyt_oh_noof_circuits,lyt_oh_multivoltage,lyt_oh_33kv_vlckts,lyt_oh_11kv_vlckts,lyt_oh_6_3kv_vlckts,lyt_oh_lt_vlckts,lyt_ug_servicepiller,lyt_ug_tappingpoint,lyt_underground,lyt_overhead,lyt_otherthan_streetlight,lyt_spi_no_circuit,lyt_ug_poletype,lyt_ug_feederpiller,lyt_distance,lyt_oh_crossing,lyt_oh_6_3_crossing,lyt_oh_11kv_crossing,lyt_oh_33kv_crossing,lyt_oh_lt_crossing,lyt_oh_tc_crossing,lyt_oh_tm_crossing;
    private CheckBox ch_oh_aglservices,ch_oh_aquaservices,ch_oh_industrialservices,ch_oh_33kv_vlckts,ch_oh_11kv_vlckts,ch_oh_6_3kv_vlckts,ch_oh_lt_vlckts,ch_oh_6_3_crossing,ch_oh_11kv_crossing,ch_oh_33kv_crossing,ch_oh_lt_crossing,ch_oh_tc_crossing,ch_oh_tm_crossing;
    private TextInputEditText edt_oh_aglservices,edt_oh_aquaservices,edt_oh_industrialservices,edt_oh_tappinglocation,edt_oh_33kv_vlckts,edt_oh_11kv_vlckts,edt_oh_6_3kv_vlckts,edt_oh_lt_vlckts,edt_oh_pole_paintingcode,edt_oh_remarks,edt_ug_tappinglocation,edt_ug_pole_paintingcode,edt_ug_remarks,edt_oh_6_3_crossing,edt_oh_11kv_crossing,edt_oh_33kv_crossing,edt_oh_lt_crossing,edt_oh_tc_crossing,edt_oh_tm_crossing;
    private Button btn_clear,btn_add,btn_lttopclear;
    private PMIArcGISDatabase arcGISDatabase;
    private List<PMISurveyDataVo> surveyVosList;
    private int maxSurveyCount,poleCountCheck;
    private String dtrCode,ltCircuit;
    private final String x, y;
    Callout callout;
    GraphicsOverlay graphicsOverlay;
    Graphic graphic;
    private int noof_33kvCkts,noof_11kvCkts,noof_6_3kvCkts,noof_ltCkts;
    private LinearLayout lay_oh_dtr_pole_dtr_meter_avilable,lay_oh_dtr_pole_no_of_meter,lay_ug_dtr_pole_dtr_meter_avilable,lay_ug_dtr_pole_no_of_meter;
    private Spinner sp_oh_dtr_pole_dtr_meter_avilable,sp_oh_dtr_pole_no_of_meters,sp_ug_dtr_pole_dtr_meter_avilable,sp_ug_dtr_pole_no_of_meters;
    private String oh_dtr_pole_dtr_meter_avilable,oh_dtr_pole_no_of_meters,ug_dtr_pole_dtr_meter_avilable,ug_dtr_pole_no_of_meters;
    private double distance;
    private List<PMISurveyDataVo> surveyVoList;
    Button btn_repeat_pole;
    private  String sp_poleheight_KEY = "N";

    //NEW
    Spinner sp_oh_defects;
    String defect_selected;
    LinearLayout defects_layout;
    Spinner sp_int_pole,sp_restringing,sp_ht_clearance,sp_stray_wire,sp_strut_pole,sp_cross_arm,sp_insulators,sp_conductors,sp_pole_defects;
    CheckBox ch_int_pole,ch_restringing,ch_ht_clearance,ch_stray_wire,ch_strut_pole,ch_cross_arm,ch_insulators,ch_conductors,ch_pole_defects,ch_other_defects;
    EditText ed_other_defects;
    String feeder_id;
    LinearLayout ckt_type_layout;
    Spinner sp_ckt_type;
    TextInputEditText edt_oh_con_services;
    LinearLayout layout_oh_con_services;

    //DISTANCE
    double currentDistance, lastPoleDistance;
    TextView last_pole_distance, current_distance;

    //IMAGES
    LinearLayout images_layout;
    public static ImageView image_1,image_2,image_3;
    public static TextView upload_image_1,upload_image_2,upload_image_3;
    public static final int CAMERA_RESULT_CODE_IMAGE_1 = 11;
    public static final int CAMERA_RESULT_CODE_IMAGE_2 = 12;
    public static final int CAMERA_RESULT_CODE_IMAGE_3 = 13;

    public static final int GALLERY_RESULT_CODE_IMAGE_1 = 14;
    public static final int GALLERY_RESULT_CODE_IMAGE_2 = 15;
    public static final int GALLERY_RESULT_CODE_IMAGE_3 = 16;

    public static String image_1_base64,image_2_base64,image_3_base64;
    public static boolean image_1_clicked = false;
    public static boolean image_2_clicked = false;
    public static boolean image_3_clicked = false;

    TextView tv_edit_pole;
    //OTHER CABLES
    Spinner sp_oh_other_cables;
    EditText edt_oh_other_cables;

    public PMILTDialog(Activity a, String x, String y, Callout callout, GraphicsOverlay graphicsOverlay, Graphic graphic, int maxSurveyCount,
                    String dtrCode,String ltCircuit, String feeder_id, double lastPoleDistance, double currentDistance) {
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
        this.feeder_id=feeder_id;
        this.lastPoleDistance=lastPoleDistance;
        this.currentDistance=currentDistance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pmiltdialog);
        try {
            setCancelable(false);
            tv_edit_pole = findViewById(R.id.tv_edit_pole);
            tv_edit_pole.setVisibility(View.VISIBLE);

            //OTHER CABLES
            sp_oh_other_cables = findViewById(R.id.sp_oh_other_cables);
            edt_oh_other_cables = findViewById(R.id.edt_oh_other_cables);

            //IMAGES
            images_layout = findViewById(R.id.images_layout);
            image_1 = findViewById(R.id.image_1);
            image_2 = findViewById(R.id.image_2);
            image_3 = findViewById(R.id.image_3);
            upload_image_1 = findViewById(R.id.upload_image_1);
            upload_image_2 = findViewById(R.id.upload_image_2);
            upload_image_3 = findViewById(R.id.upload_image_3);

            current_distance = findViewById(R.id.current_distance);
            last_pole_distance = findViewById(R.id.last_pole_distance);
            tv_dtrstrcuture = findViewById(R.id.tv_dtrstrcuture);
            ckt_type_layout = findViewById(R.id.ckt_type_layout);
            sp_ckt_type = findViewById(R.id.sp_ckt_type);
            edt_oh_con_services = findViewById(R.id.edt_oh_con_services);
            layout_oh_con_services = findViewById(R.id.layout_oh_con_services);
            ckt_type_layout.setVisibility(View.VISIBLE);

            tv_ltcircuit=findViewById(R.id.tv_ltcircuit);
            ed_pole_no=findViewById(R.id.ed_pole_no);
            ed_pole_no.setEnabled(false);
            ed_frompole=findViewById(R.id.ed_frompole);
            //  ed_frompole.setEnabled(false);
            sp_lineType=findViewById(R.id.sp_lineType);
            sp_oh_isendpole=findViewById(R.id.sp_oh_isendpole);
            sp_oh_lineconfig=findViewById(R.id.sp_oh_lineconfig);
            sp_oh_isstreetlight=findViewById(R.id.sp_oh_isstreetlight);
            sp_oh_otherthan_streetlight=findViewById(R.id.sp_oh_otherthan_streetlight);
            ch_oh_aglservices=findViewById(R.id.ch_oh_aglservices);
            edt_oh_aglservices=findViewById(R.id.edt_oh_aglservices);
            ch_oh_aquaservices=findViewById(R.id.ch_oh_aquaservices);
            edt_oh_aquaservices=findViewById(R.id.edt_oh_aquaservices);
            ch_oh_industrialservices=findViewById(R.id.ch_oh_industrialservices);
            edt_oh_industrialservices=findViewById(R.id.edt_oh_industrialservices);
            sp_oh_pin_cutpoint=findViewById(R.id.sp_oh_pin_cutpoint);
            sp_oh_is_doublefeeding_pole=findViewById(R.id.sp_oh_is_doublefeeding_pole);
            sp_oh_pole_type=findViewById(R.id.sp_oh_pole_type);
            sp_oh_pole_height=findViewById(R.id.sp_oh_pole_height);
            sp_oh_strut_stay_details=findViewById(R.id.sp_oh_strut_stay_details);
            sp_oh_tapping_point=findViewById(R.id.sp_oh_tapping_point);
            sp_oh_noof_spurlines=findViewById(R.id.sp_oh_noof_spurlines);
            edt_oh_tappinglocation=findViewById(R.id.edt_oh_tappinglocation);
            sp_oh_conductor_size=findViewById(R.id.sp_oh_conductor_size);
            sp_oh_ismulti_circuit=findViewById(R.id.sp_oh_ismulti_circuit);
            sp_oh_noof_circuits=findViewById(R.id.sp_oh_noof_circuits);
            sp_oh_multi_voltage_same_voltage=findViewById(R.id.sp_oh_multi_voltage_same_voltage);
            ch_oh_33kv_vlckts=findViewById(R.id.ch_oh_33kv_vlckts);
            edt_oh_33kv_vlckts=findViewById(R.id.edt_oh_33kv_vlckts);
            ch_oh_11kv_vlckts=findViewById(R.id.ch_oh_11kv_vlckts);
            edt_oh_11kv_vlckts=findViewById(R.id.edt_oh_11kv_vlckts);
            ch_oh_6_3kv_vlckts=findViewById(R.id.ch_oh_6_3kv_vlckts);
            edt_oh_6_3kv_vlckts=findViewById(R.id.edt_oh_6_3kv_vlckts);
            ch_oh_lt_vlckts=findViewById(R.id.ch_oh_lt_vlckts);
            edt_oh_lt_vlckts=findViewById(R.id.edt_oh_lt_vlckts);
            edt_oh_pole_paintingcode=findViewById(R.id.edt_oh_pole_paintingcode);
            edt_oh_remarks=findViewById(R.id.edt_oh_remarks);
            lyt_spi_no_circuit=findViewById(R.id.lyt_spi_no_circuit);
            lay_oh_dtr_pole_dtr_meter_avilable=(LinearLayout)findViewById(R.id.lay_oh_dtr_pole_dtr_meter_avilable);
            lay_oh_dtr_pole_dtr_meter_avilable.setVisibility(View.GONE);
            lay_oh_dtr_pole_no_of_meter=(LinearLayout)findViewById(R.id.lay_oh_dtr_pole_no_of_meter);
            lay_oh_dtr_pole_no_of_meter.setVisibility(View.GONE);
            sp_oh_dtr_pole_dtr_meter_avilable=(Spinner)findViewById(R.id.sp_oh_dtr_pole_dtr_meter_avilable);
            sp_oh_dtr_pole_no_of_meters=(Spinner)findViewById(R.id.sp_oh_dtr_pole_no_of_meters);
            lyt_spi_no_circuit.setVisibility(View.GONE);
//UG
            sp_ug_isendpole=findViewById(R.id.sp_ug_isendpole);
            sp_ug_locationtype=findViewById(R.id.sp_ug_locationtype);

            sp_ug_servicepiller_type=findViewById(R.id.sp_ug_servicepiller_type);
            sp_ug_poletype=findViewById(R.id.sp_ug_poletype);
            sp_ug_poleheight=findViewById(R.id.sp_ug_poleheight);
            sp_ug_polesupport=findViewById(R.id.sp_ug_polesupport);
            sp_ug_feedepiller_outg_circuits=findViewById(R.id.sp_ug_feedepiller_outg_circuits);
            sp_ug_tapping_point=findViewById(R.id.sp_ug_tapping_point);
            sp_ug_notapping_point=findViewById(R.id.sp_ug_notapping_point);
            edt_ug_tappinglocation=findViewById(R.id.edt_ug_tappinglocation);
            sp_ug_cablesize=findViewById(R.id.sp_ug_cablesize);
            sp_ug_nocable=findViewById(R.id.sp_ug_nocable);
            edt_ug_pole_paintingcode=findViewById(R.id.edt_ug_pole_paintingcode);
            edt_ug_remarks=findViewById(R.id.edt_ug_remarks);
            tv_distance=findViewById(R.id.tv_distance);
            sp_crossing=findViewById(R.id.sp_crossing);

            btn_repeat_pole=findViewById(R.id.btn_repeat_pole);

            lay_ug_dtr_pole_dtr_meter_avilable=(LinearLayout)findViewById(R.id.lay_ug_dtr_pole_dtr_meter_avilable);
            lay_ug_dtr_pole_dtr_meter_avilable.setVisibility(View.GONE);
            lay_ug_dtr_pole_no_of_meter=(LinearLayout)findViewById(R.id.lay_ug_dtr_pole_no_of_meter);
            lay_ug_dtr_pole_no_of_meter.setVisibility(View.GONE);
            sp_ug_dtr_pole_dtr_meter_avilable=(Spinner)findViewById(R.id.sp_ug_dtr_pole_dtr_meter_avilable);
            sp_ug_dtr_pole_no_of_meters=(Spinner)findViewById(R.id.sp_ug_dtr_pole_no_of_meters);
            ch_oh_33kv_vlckts=findViewById(R.id.ch_oh_33kv_vlckts);


            edt_oh_6_3_crossing=findViewById(R.id.edt_oh_6_3_crossing);
            ch_oh_6_3_crossing=findViewById(R.id.ch_oh_6_3_crossing);
            ch_oh_11kv_crossing=findViewById(R.id.ch_oh_11kv_crossing);
            edt_oh_11kv_crossing=findViewById(R.id.edt_oh_11kv_crossing);
            ch_oh_33kv_crossing=findViewById(R.id.ch_oh_33kv_crossing);
            edt_oh_33kv_crossing=findViewById(R.id.edt_oh_33kv_crossing);
            ch_oh_lt_crossing=findViewById(R.id.ch_oh_lt_crossing);
            edt_oh_lt_crossing=findViewById(R.id.edt_oh_lt_crossing);
            ch_oh_tc_crossing=findViewById(R.id.ch_oh_tc_crossing);
            edt_oh_tc_crossing=findViewById(R.id.edt_oh_tc_crossing);
            ch_oh_tm_crossing=findViewById(R.id.ch_oh_tm_crossing);
            edt_oh_tm_crossing=findViewById(R.id.edt_oh_tm_crossing);


            lyt_spi_no_circuit.setVisibility(View.GONE);


            btn_clear=findViewById(R.id.btn_clear);
            btn_add=findViewById(R.id.btn_add);
            btn_lttopclear=findViewById(R.id.btn_lttopclear);
            btn_clear.setOnClickListener(this);
            btn_add.setOnClickListener(this);
            btn_lttopclear.setOnClickListener(this);
            btn_repeat_pole.setOnClickListener(this);

//LinearLayout
            lyt_oh_aglservice=findViewById(R.id.lyt_oh_aglservice);
            lyt_oh_edt_aglservices=findViewById(R.id.lyt_oh_edt_aglservices);
            lyt_oh_chacquaservices=findViewById(R.id.lyt_oh_chacquaservices);
            lyt_oh_edt_aquaservices=findViewById(R.id.lyt_oh_edt_aquaservices);
            lyt_oh_chindustrialservices=findViewById(R.id.lyt_oh_chindustrialservices);
            lyt_oh_edt_industrialservices=findViewById(R.id.lyt_oh_edt_industrialservices);
            lyt_oh_is_doublefeeding_pole=findViewById(R.id.lyt_oh_is_doublefeeding_pole);
            lyt_oh_noof_spurlines=findViewById(R.id.lyt_oh_noof_spurlines);
            lyt_oh_noof_circuits=findViewById(R.id.lyt_oh_noof_circuits);
            lyt_oh_multivoltage=findViewById(R.id.lyt_oh_multivoltage);
            lyt_oh_33kv_vlckts=findViewById(R.id.lyt_oh_33kv_vlckts);
            lyt_oh_11kv_vlckts=findViewById(R.id.lyt_oh_11kv_vlckts);
            lyt_oh_6_3kv_vlckts=findViewById(R.id.lyt_oh_6_3kv_vlckts);
            lyt_oh_lt_vlckts=findViewById(R.id.lyt_oh_lt_vlckts);
            lyt_ug_servicepiller=findViewById(R.id.lyt_ug_servicepiller);
            lyt_ug_tappingpoint=findViewById(R.id.lyt_ug_tappingpoint);
            lyt_underground=findViewById(R.id.lyt_underground);
            lyt_overhead=findViewById(R.id.lyt_overhead);
            lyt_ug_poletype=findViewById(R.id.lyt_ug_poletype);
            lyt_ug_feederpiller=findViewById(R.id.lyt_ug_feederpiller);
            lyt_ug_poletype.setVisibility(View.GONE);
            lyt_ug_feederpiller.setVisibility(View.GONE);
            lyt_distance=findViewById(R.id.lyt_distance);
            lyt_distance.setVisibility(View.GONE);
            lyt_oh_crossing=findViewById(R.id.lyt_oh_crossing);
            lyt_oh_crossing.setVisibility(View.GONE);
            lyt_oh_6_3_crossing=findViewById(R.id.lyt_oh_6_3_crossing);
            lyt_oh_6_3_crossing.setVisibility(View.GONE);
            lyt_oh_11kv_crossing=findViewById(R.id.lyt_oh_11kv_crossing);
            lyt_oh_11kv_crossing.setVisibility(View.GONE);
            lyt_oh_33kv_crossing=findViewById(R.id.lyt_oh_33kv_crossing);
            lyt_oh_33kv_crossing.setVisibility(View.GONE);
            lyt_oh_lt_crossing=findViewById(R.id.lyt_oh_lt_crossing);
            lyt_oh_lt_crossing.setVisibility(View.GONE);
            lyt_oh_tc_crossing=findViewById(R.id.lyt_oh_tc_crossing);
            lyt_oh_tc_crossing.setVisibility(View.GONE);
            lyt_oh_tm_crossing=findViewById(R.id.lyt_oh_tm_crossing);
            lyt_oh_tm_crossing.setVisibility(View.GONE);

//Always hide
            lyt_otherthan_streetlight=findViewById(R.id.lyt_otherthan_streetlight);
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
            sp_oh_other_cables.setOnItemSelectedListener(this);



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
            // System.out.println("CKT TYPE IN DIALOG : "+ltCircuit);
            tv_ltcircuit.setText(ltCircuit);

            arcGISDatabase=new PMIArcGISDatabase(c);

            // surveyVosList = arcGISDatabase.getSurveyData("");
            //  int count = arcGISDatabase.getObjectId();
            //  poleCountCheck = arcGISDatabase.getObjectIdCount();
           /* if (poleCountCheck == 0) {
                //  if(""surveyType)
                int polenum = maxSurveyCount+ 1;
                ed_frompole.setText(String.valueOf(count));
                ed_pole_no.setText(String.valueOf(polenum));
               *//* ed_frompole.setText(String.valueOf("0"));
                ed_pole_no.setText(String.valueOf(polenum));*//*
            } else {
                ed_frompole.setText(String.valueOf(count));
                ed_pole_no.setText(String.valueOf(count+1));
            }*/

            int polenum = maxSurveyCount+ 1;
            ed_frompole.setText(String.valueOf(maxSurveyCount));
            ed_pole_no.setText(String.valueOf(polenum));








            if(maxSurveyCount>0){
                String idtance_Poles=String.valueOf(Math.round(distance));
                tv_distance.setText(idtance_Poles+"  Mtrs");

                lyt_distance.setVisibility(View.GONE);
            }else {
                lyt_distance.setVisibility(View.GONE);
            }



            surveyVoList = arcGISDatabase.getSurveyDataREPEAT("SELECT * FROM SURVEY_TABLE_REPEAT LIMIT 1 OFFSET (SELECT COUNT(*) FROM SURVEY_TABLE_REPEAT)-1");
            if (surveyVoList.size() > 0) {
                btn_repeat_pole.setVisibility(View.VISIBLE);
            }
            else {
                btn_repeat_pole.setVisibility(View.GONE);
            }




            // ,,,,,;
            // CheckBox ,,,,,,,;
            //NEW
            sp_oh_defects = findViewById(R.id.sp_oh_defects);
            defects_layout = findViewById(R.id.defects_layout);
            sp_int_pole = findViewById(R.id.sp_int_pole);
            sp_restringing = findViewById(R.id.sp_restringing);
            sp_ht_clearance = findViewById(R.id.sp_ht_clearance);
            sp_stray_wire = findViewById(R.id.sp_stray_wire);
            sp_strut_pole = findViewById(R.id.sp_strut_pole);
            sp_cross_arm = findViewById(R.id.sp_cross_arm);
            sp_insulators = findViewById(R.id.sp_insulators);
            sp_conductors = findViewById(R.id.sp_conductors);
            sp_pole_defects = findViewById(R.id.sp_pole_defects);

            ch_int_pole= findViewById(R.id.ch_int_pole);
            ch_restringing= findViewById(R.id.ch_restringing);
            ch_ht_clearance = findViewById(R.id.ch_ht_clearance);
            ch_stray_wire = findViewById(R.id.ch_stray_wire);
            ch_strut_pole = findViewById(R.id.ch_strut_pole);
            ch_cross_arm = findViewById(R.id.ch_cross_arm);
            ch_insulators = findViewById(R.id.ch_insulators);
            ch_conductors = findViewById(R.id.ch_conductors);
            ch_pole_defects = findViewById(R.id.ch_pole_defects);
            ch_other_defects = findViewById(R.id.ch_other_defects);
            ed_other_defects = findViewById(R.id.ed_other_defects);

            sp_oh_defects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    defect_selected = sp_oh_defects.getSelectedItem().toString();
                    //System.out.println("SELECTED DEFECTS : "+defect_selected);
                    if (defect_selected.equals("Yes")){
                        defects_layout.setVisibility(View.VISIBLE);
                        images_layout.setVisibility(View.VISIBLE);
                    }else {
                        defects_layout.setVisibility(View.GONE);
                        images_layout.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            ch_int_pole.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b){
                        sp_int_pole.setVisibility(View.VISIBLE);
                    }else {
                        sp_int_pole.setVisibility(View.GONE);
                    }
                }
            });

            ch_restringing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b){
                        sp_restringing.setVisibility(View.VISIBLE);
                    }else {
                        sp_restringing.setVisibility(View.GONE);
                    }
                }
            });

            ch_ht_clearance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b){
                        sp_ht_clearance.setVisibility(View.VISIBLE);
                    }else {
                        sp_ht_clearance.setVisibility(View.GONE);
                    }
                }
            });

            ch_stray_wire.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b){
                        sp_stray_wire.setVisibility(View.VISIBLE);
                    }else {
                        sp_stray_wire.setVisibility(View.GONE);
                    }
                }
            });

            ch_strut_pole.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b){
                        sp_strut_pole.setVisibility(View.VISIBLE);
                    }else {
                        sp_strut_pole.setVisibility(View.GONE);
                    }
                }
            });

            ch_cross_arm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b){
                        sp_cross_arm.setVisibility(View.VISIBLE);
                    }else {
                        sp_cross_arm.setVisibility(View.GONE);
                    }
                }
            });

            ch_insulators.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b){
                        sp_insulators.setVisibility(View.VISIBLE);
                    }else {
                        sp_insulators.setVisibility(View.GONE);
                    }
                }
            });

            ch_conductors.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b){
                        sp_conductors.setVisibility(View.VISIBLE);
                    }else {
                        sp_conductors.setVisibility(View.GONE);
                    }
                }
            });

            ch_pole_defects.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b){
                        sp_pole_defects.setVisibility(View.VISIBLE);
                    }else {
                        sp_pole_defects.setVisibility(View.GONE);
                    }
                }
            });

            ch_other_defects.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        ed_other_defects.setVisibility(View.VISIBLE);
                    }else {
                        ed_other_defects.setVisibility(View.GONE);
                    }
                }
            });

            int a = (int) Math.round(lastPoleDistance);
            last_pole_distance.setText("" + a);

            int b = (int) Math.round(currentDistance);
            current_distance.setText("" + b);


            //IMAGES
            upload_image_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // openImageDialog(CAMERA_RESULT_CODE_IMAGE_1,GALLERY_RESULT_CODE_IMAGE_1);

                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    c.startActivityForResult(intent, CAMERA_RESULT_CODE_IMAGE_1);
                }
            });

            upload_image_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // openImageDialog(CAMERA_RESULT_CODE_IMAGE_2,GALLERY_RESULT_CODE_IMAGE_2);
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    c.startActivityForResult(intent, CAMERA_RESULT_CODE_IMAGE_2);
                }
            });

            upload_image_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //openImageDialog(CAMERA_RESULT_CODE_IMAGE_3,GALLERY_RESULT_CODE_IMAGE_3);
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    c.startActivityForResult(intent, CAMERA_RESULT_CODE_IMAGE_3);

                }
            });


        }catch (Exception e){

        }
    }

    private void openImageDialog(int CAMERA, int GALLERY) {
        android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(c, R.style.MyDialogTheme);
        builder1.setTitle("Upload Photo");
        builder1.setMessage("Please choose an option");
        builder1.setCancelable(true);
        builder1.setPositiveButton(Html.fromHtml("<font color='#14B457'>Camera</font>"), new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();

                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
               /* intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 0);
                intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", false);*/
                c.startActivityForResult(intent, CAMERA);

                /*int permission = ActivityCompat.checkSelfPermission(c, Manifest.permission.CAMERA);

                if (permission != PackageManager.PERMISSION_GRANTED) {
                    // We don't have permission so prompt the user
                    verifyStoragePermissions(c, CAMERA_REQUEST_CODE_IMAGE_1);

                } else {
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 0);
                    intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", false);
                    c.startActivityForResult(intent, CAMERA_REQUEST_CODE_IMAGE_1);
                }*/

            }
        });
        builder1.setNegativeButton(Html.fromHtml("<font color='#14B457'>Gallery</font>"), new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();



                if (ActivityCompat.checkSelfPermission(c, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(c, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY);
//                    return;
                } else {
                    launchGalleryIntent(GALLERY);
                }
            }
        });

        android.app.AlertDialog alert11 = builder1.create();
        alert11.show();

    }

    private void launchGalleryIntent(int RequestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        c.startActivityForResult(Intent.createChooser(intent, "Select Picture"), RequestCode);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){


            case R.id.btn_repeat_pole:
                re_pole();
                image_1_clicked = false;
                image_2_clicked = false;
                image_3_clicked = false;
                break;


            case R.id.btn_clear:
                image_1_clicked = false;
                image_2_clicked = false;
                image_3_clicked = false;
                sp_poleheight_KEY = "N";
                PMIArcGISMapActivity11kv.isPoleAdded = false;
                callout.dismiss();
                graphicsOverlay.getGraphics().remove(graphic);
                dismiss();
                break;

            case R.id.btn_add:
                if (Integer.parseInt(ed_frompole.getText().toString()) > Integer.parseInt(ed_pole_no.getText().toString())){
                    Toast.makeText(c, "Wrong from Pole Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if("OverHead".equalsIgnoreCase(lineType)){
                    if (sp_oh_defects.getSelectedItem().toString().equalsIgnoreCase("Yes")){
                        validateDefects();
                    }else {
                        addOhData();
                    }

                }else if("UnderGround".equalsIgnoreCase(lineType)){
                    underGround();
                }

                break;
            case R.id.btn_lttopclear:
                image_1_clicked = false;
                image_2_clicked = false;
                image_3_clicked = false;
                PMIArcGISMapActivity11kv.isPoleAdded = false;
                callout.dismiss();
                graphicsOverlay.getGraphics().remove(graphic);
                dismiss();
                break;

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){

            case R.id.sp_ug_dtr_pole_dtr_meter_avilable:
                if("Yes".equalsIgnoreCase(sp_ug_dtr_pole_dtr_meter_avilable.getSelectedItem().toString())){
                    lay_ug_dtr_pole_no_of_meter.setVisibility(View.VISIBLE);
                    ug_dtr_pole_dtr_meter_avilable=sp_ug_dtr_pole_dtr_meter_avilable.getSelectedItem().toString();
                }else {
                    ug_dtr_pole_dtr_meter_avilable="";
                    ug_dtr_pole_no_of_meters="";
                    sp_ug_dtr_pole_no_of_meters.setSelection(0);
                    lay_ug_dtr_pole_no_of_meter.setVisibility(View.GONE);
                }
                break;
            case R.id.sp_ug_dtr_pole_no_of_meters:
                if(!"Select".equalsIgnoreCase(sp_ug_dtr_pole_no_of_meters.getSelectedItem().toString())){
                    ug_dtr_pole_no_of_meters=sp_ug_dtr_pole_no_of_meters.getSelectedItem().toString();
                }else {
                    ug_dtr_pole_no_of_meters="";
                }
                break;

            case R.id.sp_oh_dtr_pole_dtr_meter_avilable:
                if("Yes".equalsIgnoreCase(sp_oh_dtr_pole_dtr_meter_avilable.getSelectedItem().toString())){
                    lay_oh_dtr_pole_no_of_meter.setVisibility(View.VISIBLE);
                    oh_dtr_pole_dtr_meter_avilable=sp_oh_dtr_pole_dtr_meter_avilable.getSelectedItem().toString();
                }else {
                    oh_dtr_pole_dtr_meter_avilable="";
                    oh_dtr_pole_no_of_meters="";
                    sp_oh_dtr_pole_no_of_meters.setSelection(0);
                    lay_oh_dtr_pole_no_of_meter.setVisibility(View.GONE);
                }
                break;
            case R.id.sp_oh_dtr_pole_no_of_meters:
                if(!"Select".equalsIgnoreCase(sp_oh_dtr_pole_no_of_meters.getSelectedItem().toString())){
                    oh_dtr_pole_no_of_meters=sp_oh_dtr_pole_no_of_meters.getSelectedItem().toString();
                }else {
                    oh_dtr_pole_no_of_meters="";
                }
                break;

            case R.id.sp_lineType:

                lineType=sp_lineType.getSelectedItem().toString();
                if("OverHead".equalsIgnoreCase(lineType)){
                    lyt_overhead.setVisibility(View.VISIBLE);
                    lyt_underground.setVisibility(View.GONE);
                }else if("UnderGround".equalsIgnoreCase(lineType)){
                    lyt_underground.setVisibility(View.VISIBLE);
                    lyt_overhead.setVisibility(View.GONE);
                }

                break;


            case R.id.sp_oh_isendpole:
                try {
                    oh_isendpole=sp_oh_isendpole.getSelectedItem().toString();
                } catch (Exception e) {

                }


                break;
            case R.id.sp_oh_lineconfig:
                if("Select".equalsIgnoreCase(sp_oh_lineconfig.getSelectedItem().toString())){
                    oh_lineconfig="";
                    /*lay_oh_dtr_pole_dtr_meter_avilable.setVisibility(View.GONE);
                    lay_oh_dtr_pole_no_of_meter.setVisibility(View.GONE);*/
                }/*else if("DTR Pole".equalsIgnoreCase(sp_oh_lineconfig.getSelectedItem().toString())){
                    lay_oh_dtr_pole_dtr_meter_avilable.setVisibility(View.VISIBLE);
                    oh_lineconfig=sp_oh_lineconfig.getSelectedItem().toString();
                }*/else {
                   /* lay_oh_dtr_pole_dtr_meter_avilable.setVisibility(View.GONE);
                    lay_oh_dtr_pole_no_of_meter.setVisibility(View.GONE);*/
                    oh_lineconfig=sp_oh_lineconfig.getSelectedItem().toString();
                }
                break;

            case R.id.sp_oh_isstreetlight:

                try {
                    oh_isstreetlight=sp_oh_isstreetlight.getSelectedItem().toString();
                } catch (Exception e) {

                }


                break;

            case R.id.sp_oh_other_cables:
                if (sp_oh_other_cables.getSelectedItem().toString().equals("Yes")){
                    edt_oh_other_cables.setVisibility(View.VISIBLE);
                }else {
                    edt_oh_other_cables.setVisibility(View.GONE);
                }
                break;

            case R.id.sp_oh_otherthan_streetlight:

                try {
                    oh_otherthan_streetlight=sp_oh_otherthan_streetlight.getSelectedItem().toString();
                    if("Yes".equalsIgnoreCase(oh_otherthan_streetlight)){
                        lyt_oh_aglservice.setVisibility(View.VISIBLE);
                        lyt_oh_chacquaservices.setVisibility(View.VISIBLE);
                        lyt_oh_chindustrialservices.setVisibility(View.VISIBLE);
                        layout_oh_con_services.setVisibility(View.VISIBLE);
                    }else {
                        lyt_oh_aglservice.setVisibility(View.GONE);
                        lyt_oh_chacquaservices.setVisibility(View.GONE);
                        lyt_oh_chindustrialservices.setVisibility(View.GONE);
                        layout_oh_con_services.setVisibility(View.GONE);
                        ch_oh_aglservices.setChecked(false);
                        ch_oh_aquaservices.setChecked(false);
                        ch_oh_industrialservices.setChecked(false);
                        edt_oh_aglservices.setText("");
                        edt_oh_aquaservices.setText("");
                        edt_oh_industrialservices.setText("");
                    }
                } catch (Exception e) {

                }


                break;
            case R.id.sp_oh_pin_cutpoint:

                try {
                    oh_pin_cutpoint=sp_oh_pin_cutpoint.getSelectedItem().toString();
                    if("Cut Point".equalsIgnoreCase(oh_pin_cutpoint)){
                        lyt_oh_is_doublefeeding_pole.setVisibility(View.VISIBLE);
                    }else {
                        lyt_oh_is_doublefeeding_pole.setVisibility(View.GONE);
                        sp_oh_is_doublefeeding_pole.setSelection(0);
                    }
                } catch (Exception e) {

                }

                break;

            case R.id.sp_oh_is_doublefeeding_pole:

                try {
                    oh_is_doublefeeding_pole=sp_oh_is_doublefeeding_pole.getSelectedItem().toString();
                } catch (Exception e) {

                }


                break;
            case R.id.sp_oh_pole_type:

                try {
                    oh_pole_type=sp_oh_pole_type.getSelectedItem().toString();
                    if ("PSCC".equalsIgnoreCase(oh_pole_type)) {
                        // String test= (String) mapData.get("POLE_TYPE");
                        List<String> psccList = new ArrayList<>();
                        psccList.add("Select");
                        psccList.add("9.1");
                        psccList.add("11");
                        psccList.add("8.0");
                        // psccList.add(test);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_selectable_list_item, psccList);
                        sp_oh_pole_height.setAdapter(adapter);

                    } else if ("SPUN".equalsIgnoreCase(oh_pole_type)) {
                        List<String> spunList = new ArrayList<>();
                        spunList.add("Select");
                        spunList.add("9.5");
                        spunList.add("11");
                        spunList.add("12.5");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_selectable_list_item, spunList);
                        sp_oh_pole_height.setAdapter(adapter);

                    } else if ("RAIL".equalsIgnoreCase(oh_pole_type)) {
                        List<String> railList = new ArrayList<>();
                        railList.add("Select");
                        railList.add("9.5");
                        railList.add("11");
                        railList.add("12.5");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_selectable_list_item, railList);
                        sp_oh_pole_height.setAdapter(adapter);

                    } else if ("RS JOIST".equalsIgnoreCase(oh_pole_type)) {
                        List<String> rssJoistList = new ArrayList<>();
                        rssJoistList.add("Select");
                        /*rssJoistList.add("8");*/
                        rssJoistList.add("9.1");
                        rssJoistList.add("11");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_selectable_list_item, rssJoistList);
                        sp_oh_pole_height.setAdapter(adapter);
                    } else if ("TOWER".equalsIgnoreCase(oh_pole_type)) {
                        List<String> towertList = new ArrayList<>();
                        towertList.add("Select");
                        towertList.add("M+3");
                        towertList.add("M+6");
                        towertList.add("M+9");
                        towertList.add("M+12");
                        towertList.add("R+6");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_selectable_list_item, towertList);
                        sp_oh_pole_height.setAdapter(adapter);
                    } else if ("Tubular Pole".equalsIgnoreCase(oh_pole_type)) {
                        List<String> tubularList = new ArrayList<>();
                        tubularList.add("Select");
                        tubularList.add("9.1");
                        tubularList.add("11");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_selectable_list_item, tubularList);
                        sp_oh_pole_height.setAdapter(adapter);
                    }
                } catch (Exception e) {

                }

                break;

            case R.id.sp_oh_pole_height:
                try {

                    if (sp_poleheight_KEY.equals("R")) {

                        surveyVoList = arcGISDatabase.getSurveyDataREPEAT("SELECT * FROM SURVEY_TABLE_REPEAT LIMIT 1 OFFSET (SELECT COUNT(*) FROM SURVEY_TABLE_REPEAT)-1");
                        if (surveyVoList.size() > 0) {


                            for (PMISurveyDataVo surveyVo : surveyVoList) {


                                String is_sp_poleheight = surveyVo.getPOLE_HEIGHT();
                                //   Toast.makeText(c, is_sp_poleheight, Toast.LENGTH_SHORT).show();

                                List<String> list_poleheight = new ArrayList<>();
                                list_poleheight.add(is_sp_poleheight);
                                ArrayAdapter<String> adapter_pole_height = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_poleheight);
                                sp_oh_pole_height.setAdapter(adapter_pole_height);

                                oh_pole_height=is_sp_poleheight = surveyVo.getPOLE_HEIGHT();


/*

                                String st_oh_pole_height = surveyVo.getPOLE_HEIGHT();
                                List<String> list_oh_pole_height = new ArrayList<>();
                                list_oh_pole_height.add(st_oh_pole_height);

                                ArrayAdapter<String> adapter_oh_pole_height = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_oh_pole_height);
                                sp_ug_poleheight.setAdapter(adapter_oh_pole_height);
*/



                                //  ug_poleheight=surveyVo.getPOLE_HEIGHT();

                              /*  if (surveyVo.getLINE_TYPE().equals("Overhead")){

                                    oh_pole_height=surveyVo.getPOLE_HEIGHT();

                                }
                                else if (surveyVo.getLINE_TYPE().equals("UnderGround")){
                                    ug_poleheight=surveyVo.getPOLE_HEIGHT();
                                  //  sp_ug_poleheight.setAdapter(adapter_oh_pole_height);
                                }
*/


                            }
                        }
                    }
                    else {
                        if(!"Select".equalsIgnoreCase(sp_oh_pole_height.getSelectedItem().toString())){
                            oh_pole_height=sp_oh_pole_height.getSelectedItem().toString();
                        }else {
                            oh_pole_height="";
                        }
                    }

                    /*if(!"Select".equalsIgnoreCase(sp_oh_pole_height.getSelectedItem().toString())){
                        oh_pole_height=sp_oh_pole_height.getSelectedItem().toString();
                    }else {
                        oh_pole_height="";
                    }*/
                } catch (Exception e) {

                }



                break;
            case R.id.sp_oh_strut_stay_details:

                try {
                    oh_strut_stay_details=sp_oh_strut_stay_details.getSelectedItem().toString();
                } catch (Exception e) {

                }


                break;

            case R.id.sp_oh_tapping_point:

                try {
                    oh_tapping_point=sp_oh_tapping_point.getSelectedItem().toString();
                    if("Yes".equalsIgnoreCase(oh_tapping_point)) {
                        lyt_oh_noof_spurlines.setVisibility(View.VISIBLE);
                    }else {
                        lyt_oh_noof_spurlines.setVisibility(View.GONE);
                        edt_oh_tappinglocation.setText("");
                        sp_oh_noof_spurlines.setSelection(0);
                    }
                } catch (Exception e) {

                }



                break;

            case R.id.sp_oh_noof_spurlines:

                try {
                    oh_noof_spurlines=sp_oh_noof_spurlines.getSelectedItem().toString();
                } catch (Exception e) {

                }


                break;

            case R.id.sp_oh_conductor_size:

                try {
                    oh_conductor_size=sp_oh_conductor_size.getSelectedItem().toString();
                } catch (Exception e) {

                }


                break;

            case R.id.sp_oh_ismulti_circuit:
                try {
                    oh_ismulti_circuit=sp_oh_ismulti_circuit.getSelectedItem().toString();
                    if("Yes".equalsIgnoreCase(oh_ismulti_circuit)){
                        lyt_oh_noof_circuits.setVisibility(View.VISIBLE);
                        lyt_spi_no_circuit.setVisibility(View.VISIBLE);
                    }else {
                        lyt_oh_noof_circuits.setVisibility(View.GONE);
                        lyt_spi_no_circuit.setVisibility(View.GONE);
                        lyt_oh_multivoltage.setVisibility(View.GONE);
                        sp_oh_noof_circuits.setSelection(0);

                    }
                } catch (Exception e) {

                }



                break;

            case R.id.sp_oh_noof_circuits:
                try {
                    oh_noof_circuits=sp_oh_noof_circuits.getSelectedItem().toString();
                } catch (Exception e) {

                }



                break;

            case R.id.sp_oh_multi_voltage_same_voltage:

                try {
                    oh_multi_voltage_same_voltage=sp_oh_multi_voltage_same_voltage.getSelectedItem().toString();
                    if("Multi Voltage".equalsIgnoreCase(oh_multi_voltage_same_voltage)){
                        lyt_oh_multivoltage.setVisibility(View.VISIBLE);
                        // lyt_spi_no_circuit.setVisibility(View.GONE);
                        oh_noof_circuits="";
                    }else {
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
                } catch (Exception e) {

                }


                break;
            //UG
            case R.id.sp_ug_isendpole:

                try {
                    ug_isendpole=sp_ug_isendpole.getSelectedItem().toString();
                } catch (Exception e) {

                }


                break;
            case R.id.sp_ug_locationtype:

                try {
                    ug_locationtype=sp_ug_locationtype.getSelectedItem().toString();
                    if("Panel Board".equalsIgnoreCase(ug_locationtype) || "Service Pillar".equalsIgnoreCase(ug_locationtype)){
                        lyt_ug_servicepiller.setVisibility(View.VISIBLE);
                        lyt_ug_poletype.setVisibility(View.GONE);
                        lyt_ug_feederpiller.setVisibility(View.GONE);
                        lay_ug_dtr_pole_dtr_meter_avilable.setVisibility(View.GONE);
                        lay_ug_dtr_pole_no_of_meter.setVisibility(View.GONE);
                        ug_poletype="";
                        ug_poleheight="";
                        ug_polesupport="";
                        sp_ug_poletype.setSelection(0);
                        sp_ug_poleheight.setSelection(0);
                        sp_ug_polesupport.setSelection(0);
                        sp_ug_feedepiller_outg_circuits.setSelection(0);

                    }else if("Pole".equalsIgnoreCase(ug_locationtype) || "Rail way Crsossing".equalsIgnoreCase(ug_locationtype) ||"Road Crossing".equalsIgnoreCase(ug_locationtype)){
                        lyt_ug_poletype.setVisibility(View.VISIBLE);
                        lyt_ug_feederpiller.setVisibility(View.GONE);
                        lyt_ug_servicepiller.setVisibility(View.GONE);
                        lay_ug_dtr_pole_dtr_meter_avilable.setVisibility(View.GONE);
                        lay_ug_dtr_pole_no_of_meter.setVisibility(View.GONE);
                        sp_ug_servicepiller_type.setSelection(0);
                        sp_ug_feedepiller_outg_circuits.setSelection(0);
                    }else if("Feeder Pillar".equalsIgnoreCase(ug_locationtype)){
                        lyt_ug_feederpiller.setVisibility(View.VISIBLE);
                        lyt_ug_servicepiller.setVisibility(View.GONE);
                        lyt_ug_poletype.setVisibility(View.GONE);
                        lay_ug_dtr_pole_dtr_meter_avilable.setVisibility(View.GONE);
                        lay_ug_dtr_pole_no_of_meter.setVisibility(View.GONE);
                        sp_ug_servicepiller_type.setSelection(0);
                        sp_ug_poletype.setSelection(0);
                        sp_ug_poleheight.setSelection(0);
                        sp_ug_polesupport.setSelection(0);
                    }
                    /*else if("DTR Pole".equalsIgnoreCase(ug_locationtype)){
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
                }*/
                    else {
                        lay_ug_dtr_pole_dtr_meter_avilable.setVisibility(View.GONE);
                        lay_ug_dtr_pole_no_of_meter.setVisibility(View.GONE);
                        lyt_ug_poletype.setVisibility(View.GONE);
                        lyt_ug_servicepiller.setVisibility(View.GONE);
                        lyt_ug_feederpiller.setVisibility(View.GONE);
                        sp_ug_servicepiller_type.setSelection(0);
                        ug_servicepiller_type="";
                        ug_poletype="";
                        ug_poleheight="";
                        ug_polesupport="";
                        sp_ug_poletype.setSelection(0);
                        sp_ug_poleheight.setSelection(0);
                        sp_ug_polesupport.setSelection(0);
                        sp_ug_feedepiller_outg_circuits.setSelection(0);
                    }
                } catch (Exception e) {

                }

                break;
            case R.id.sp_ug_servicepiller_type:

                try {
                    ug_servicepiller_type=sp_ug_servicepiller_type.getSelectedItem().toString();
                } catch (Exception e) {

                }

                break;
            case R.id.sp_ug_poletype:

                try {
                    ug_poletype=sp_ug_poletype.getSelectedItem().toString();
                    if ("PSCC".equalsIgnoreCase(ug_poletype)) {
                        // String test= (String) mapData.get("POLE_TYPE");
                        List<String> psccList = new ArrayList<>();
                        psccList.add("Select");
                        psccList.add("9.1");
                        psccList.add("8.0");
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

                } catch (Exception e) {

                }

                break;
            case R.id.sp_ug_poleheight:

                try {
                    ug_poleheight=sp_ug_poleheight.getSelectedItem().toString();


                } catch (Exception e) {

                }


                break;

            case R.id.sp_ug_polesupport:

                try {
                    ug_polesupport=sp_ug_polesupport.getSelectedItem().toString();
                } catch (Exception e) {

                }

                break;

            case R.id.sp_ug_feedepiller_outg_circuits:

                try {
                    ug_feedepiller_outg_circuits=sp_ug_feedepiller_outg_circuits.getSelectedItem().toString();
                } catch (Exception e) {

                }


                break;

            case R.id.sp_ug_tapping_point:
                try {
                    ug_tapping_point=sp_ug_tapping_point.getSelectedItem().toString();
                    if("Yes".equalsIgnoreCase(ug_tapping_point)){
                        lyt_ug_tappingpoint.setVisibility(View.VISIBLE);
                    }else {
                        lyt_ug_tappingpoint.setVisibility(View.GONE);
                        sp_ug_notapping_point.setSelection(0);
                        edt_ug_tappinglocation.setText("");
                    }
                } catch (Exception e) {

                }

                break;
            case R.id.sp_ug_notapping_point:
                try {
                    ug_notapping_point=sp_ug_notapping_point.getSelectedItem().toString();
                } catch (Exception e) {

                }

                break;

            case R.id.sp_ug_cablesize:
                try {
                    ug_cablesize=sp_ug_cablesize.getSelectedItem().toString();
                } catch (Exception e) {

                }

                break;
            case R.id.sp_ug_nocable:
                try {
                    ug_nocable=sp_ug_nocable.getSelectedItem().toString();
                } catch (Exception e) {

                }

                break;

            case R.id.sp_crossing:
                try {

                    crossing=sp_crossing.getSelectedItem().toString();
                    if("YES".equalsIgnoreCase(crossing)){
                        lyt_oh_crossing.setVisibility(View.VISIBLE);
                    }else {
                        lyt_oh_crossing.setVisibility(View.GONE);
                    }


            /*        surveyVoList = arcGISDatabase.getSurveyDataREPEAT("SELECT * FROM SURVEY_TABLE_REPEAT LIMIT 1 OFFSET (SELECT COUNT(*) FROM SURVEY_TABLE_REPEAT)-1");
                    if (surveyVoList.size() > 0) {


                        for (SurveyDataVo surveyVo : surveyVoList) {


                            if (surveyVo.getIS_CROSSINGS_AV().equals("YES")) {
                                lyt_oh_crossing.setVisibility(View.VISIBLE);
                                sp_crossing.setSelection(0);

                            } else if (surveyVo.getIS_CROSSINGS_AV().equals("No")) {
                                sp_crossing.setSelection(1);
                                lyt_oh_crossing.setVisibility(View.GONE);
                            }


                        }
                    }*/

                } catch (Exception e) {

                }


                break;


        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.ch_oh_aglservices:
                if(ch_oh_aglservices.isChecked()){
                    lyt_oh_edt_aglservices.setVisibility(View.VISIBLE);
                }else {
                    lyt_oh_edt_aglservices.setVisibility(View.GONE);
                    edt_oh_aglservices.setText("");

                }
                break;
            case R.id.ch_oh_aquaservices:
                if(ch_oh_aquaservices.isChecked()){
                    lyt_oh_edt_aquaservices.setVisibility(View.VISIBLE);
                }else {
                    lyt_oh_edt_aquaservices.setVisibility(View.GONE);
                    edt_oh_aquaservices.setText("");

                }
                break;

            case R.id.ch_oh_industrialservices:
                if(ch_oh_industrialservices.isChecked()){
                    lyt_oh_edt_industrialservices.setVisibility(View.VISIBLE);
                }else {
                    lyt_oh_edt_industrialservices.setVisibility(View.GONE);
                    edt_oh_industrialservices.setText("");
                }
                break;

            case R.id.ch_oh_33kv_vlckts:
                if(ch_oh_33kv_vlckts.isChecked()){
                    lyt_oh_33kv_vlckts.setVisibility(View.VISIBLE);

                }else {
                    lyt_oh_33kv_vlckts.setVisibility(View.GONE);
                    edt_oh_33kv_vlckts.setText("");

                }
                break;

            case R.id.ch_oh_11kv_vlckts:
                if(ch_oh_11kv_vlckts.isChecked()){
                    lyt_oh_11kv_vlckts.setVisibility(View.VISIBLE);
                }else {
                    lyt_oh_11kv_vlckts.setVisibility(View.GONE);
                    edt_oh_11kv_vlckts.setText("");
                    noof_11kvCkts=0;

                }
                break;

            case R.id.ch_oh_6_3kv_vlckts:
                if(ch_oh_6_3kv_vlckts.isChecked()){
                    lyt_oh_6_3kv_vlckts.setVisibility(View.VISIBLE);
                }else {
                    lyt_oh_6_3kv_vlckts.setVisibility(View.GONE);
                    edt_oh_6_3kv_vlckts.setText("");
                    noof_6_3kvCkts=0;

                }
                break;

            case R.id.ch_oh_lt_vlckts:
                if(ch_oh_lt_vlckts.isChecked()){
                    lyt_oh_lt_vlckts.setVisibility(View.VISIBLE);
                }else {
                    lyt_oh_lt_vlckts.setVisibility(View.GONE);
                    edt_oh_lt_vlckts.setText("");
                    noof_ltCkts=0;
                }
                break;






            case R.id.ch_oh_6_3_crossing:
                if(ch_oh_6_3_crossing.isChecked()){
                    lyt_oh_6_3_crossing.setVisibility(View.VISIBLE);

                }else {
                    lyt_oh_6_3_crossing.setVisibility(View.GONE);
                    edt_oh_6_3_crossing.setText("");


                }
                break;

            case R.id.ch_oh_11kv_crossing:
                if(ch_oh_11kv_crossing.isChecked()){
                    lyt_oh_11kv_crossing.setVisibility(View.VISIBLE);
                }else {
                    lyt_oh_11kv_crossing.setVisibility(View.GONE);
                    edt_oh_11kv_crossing.setText("");

                }
                break;

            case R.id.ch_oh_33kv_crossing:
                if(ch_oh_33kv_crossing.isChecked()){
                    lyt_oh_33kv_crossing.setVisibility(View.VISIBLE);
                }else {
                    lyt_oh_33kv_crossing.setVisibility(View.GONE);
                    edt_oh_33kv_crossing.setText("");
                }
                break;

            case R.id.ch_oh_lt_crossing:
                if(ch_oh_lt_crossing.isChecked()){
                    lyt_oh_lt_crossing.setVisibility(View.VISIBLE);
                }else {
                    lyt_oh_lt_crossing.setVisibility(View.GONE);
                    edt_oh_lt_crossing.setText("");
                }
                break;

            case R.id.ch_oh_tc_crossing:
                if(ch_oh_tc_crossing.isChecked()){
                    lyt_oh_tc_crossing.setVisibility(View.VISIBLE);
                }else {
                    lyt_oh_tc_crossing.setVisibility(View.GONE);
                    edt_oh_tc_crossing.setText("");
                }
                break;

            case R.id.ch_oh_tm_crossing:
                if(ch_oh_tm_crossing.isChecked()){
                    lyt_oh_tm_crossing.setVisibility(View.VISIBLE);
                }else {
                    lyt_oh_tm_crossing.setVisibility(View.GONE);
                    edt_oh_tm_crossing.setText("");
                }
                break;



        }
    }

    private void validateDefects() {
        // ,,,,,,,;
        //,,,,,,,;

        if (!ch_pole_defects.isChecked() && !ch_conductors.isChecked() && !ch_insulators.isChecked() &&
                !ch_cross_arm.isChecked() && !ch_strut_pole.isChecked() && !ch_stray_wire.isChecked() &&
                !ch_ht_clearance.isChecked() &&  !ch_int_pole.isChecked() && !ch_other_defects.isChecked()){
            Toast.makeText(c, "Please Select at least 1 Defect", Toast.LENGTH_SHORT).show();
            return;

        }



        if (ch_pole_defects.isChecked()){
            if (sp_pole_defects.getSelectedItem().toString().equalsIgnoreCase("Select")){
                Toast.makeText(c,"Select Pole Defects",Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (ch_conductors.isChecked()){
            if (sp_conductors.getSelectedItem().toString().equalsIgnoreCase("Select")){
                Toast.makeText(c,"Select Conductor",Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (ch_insulators.isChecked()){
            if (sp_insulators.getSelectedItem().toString().equalsIgnoreCase("Select")){
                Toast.makeText(c,"Select Insulator",Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (ch_cross_arm.isChecked()){
            if (sp_cross_arm.getSelectedItem().toString().equalsIgnoreCase("Select")){
                Toast.makeText(c,"Select Cross Arm",Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (ch_strut_pole.isChecked()){
            if (sp_strut_pole.getSelectedItem().toString().equalsIgnoreCase("Select")){
                Toast.makeText(c,"Select Strut Pole",Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (ch_stray_wire.isChecked()){
            if (sp_stray_wire.getSelectedItem().toString().equalsIgnoreCase("Select")){
                Toast.makeText(c,"Select Stay Wire",Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (ch_ht_clearance.isChecked()){
            if (sp_ht_clearance.getSelectedItem().toString().equalsIgnoreCase("Select")){
                Toast.makeText(c,"Select HT/LT Clearance",Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (ch_int_pole.isChecked()){
            if (sp_int_pole.getSelectedItem().toString().equalsIgnoreCase("Select")){
                Toast.makeText(c,"Select Intermediate Pole",Toast.LENGTH_SHORT).show();
                return;
            }

        }

        if (ch_other_defects.isChecked()){
            if (ed_other_defects.getText().toString().trim().isEmpty()){
                Toast.makeText(c,"Please Enter Other Defects",Toast.LENGTH_SHORT).show();
                return;
            }

        }

        if (!image_1_clicked && !image_2_clicked && !image_3_clicked){
            Toast.makeText(c, "Upload at least 1 photo", Toast.LENGTH_SHORT).show();
            return;
        }

        addOhData();
    }

    public void addOhData(){
        if("Select".equalsIgnoreCase(sp_oh_lineconfig.getSelectedItem().toString())){
            Toast.makeText(c,"Select Line Configuration",Toast.LENGTH_SHORT).show();
        }/*else if("DTR Pole".equalsIgnoreCase(oh_lineconfig)&&"Yes".equalsIgnoreCase(oh_dtr_pole_dtr_meter_avilable)&&"Select".equalsIgnoreCase(oh_dtr_pole_no_of_meters)){
                Toast.makeText(c,"Select No Of Meters",Toast.LENGTH_SHORT).show();
        }*/



        else if("Select".equalsIgnoreCase(sp_oh_conductor_size.getSelectedItem().toString())){
            Toast.makeText(c,"Conductor Size",Toast.LENGTH_SHORT).show();
        }
        else if("Select".equalsIgnoreCase(sp_oh_pole_type.getSelectedItem().toString())){
            Toast.makeText(c,"Select Pole Type",Toast.LENGTH_SHORT).show();
        }

        else if("Select".equalsIgnoreCase(sp_oh_pole_height.getSelectedItem().toString())){
            Toast.makeText(c,"Select Pole Height",Toast.LENGTH_SHORT).show();
        }else if("Multi Voltage".equalsIgnoreCase(oh_multi_voltage_same_voltage) && !ch_oh_33kv_vlckts.isChecked() && !ch_oh_11kv_vlckts.isChecked() && !ch_oh_6_3kv_vlckts.isChecked() && !ch_oh_lt_vlckts.isChecked()){
            //"".equalsIgnoreCase(edt_oh_33kv_vlckts.getText().toString()) && "".equalsIgnoreCase(edt_oh_11kv_vlckts.getText().toString()) && "".equalsIgnoreCase(edt_oh_6_3kv_vlckts.getText().toString()) && "".equalsIgnoreCase(edt_oh_lt_vlckts.getText().toString())){
            Toast.makeText(c,"Select Any One Of Voltage Circuit",Toast.LENGTH_SHORT).show();
        }else if(ch_oh_33kv_vlckts.isChecked() && "".equalsIgnoreCase(edt_oh_33kv_vlckts.getText().toString())){
            Toast.makeText(c,"Enter No. of 33kv Voltage Ckts",Toast.LENGTH_SHORT).show();
        }else if(ch_oh_11kv_vlckts.isChecked() && "".equalsIgnoreCase(edt_oh_11kv_vlckts.getText().toString())){
            Toast.makeText(c,"Enter No. of 11kv Voltage Ckts",Toast.LENGTH_SHORT).show();
        }else if(ch_oh_6_3kv_vlckts.isChecked() && "".equalsIgnoreCase(edt_oh_6_3kv_vlckts.getText().toString())){
            Toast.makeText(c,"Enter No. of 6.3kv Voltage Ckts",Toast.LENGTH_SHORT).show();
        }else if(ch_oh_lt_vlckts.isChecked() && "".equalsIgnoreCase(edt_oh_lt_vlckts.getText().toString())){
            Toast.makeText(c,"Enter No. of LT Voltage Ckts",Toast.LENGTH_SHORT).show();
        }else if("YES".equalsIgnoreCase(crossing) && !ch_oh_6_3_crossing.isChecked() && !ch_oh_11kv_crossing.isChecked() && !ch_oh_33kv_crossing.isChecked() && !ch_oh_lt_crossing.isChecked() && !ch_oh_tc_crossing.isChecked() && !ch_oh_tm_crossing.isChecked()){
            //"".equalsIgnoreCase(edt_oh_33kv_vlckts.getText().toString()) && "".equalsIgnoreCase(edt_oh_11kv_vlckts.getText().toString()) && "".equalsIgnoreCase(edt_oh_6_3kv_vlckts.getText().toString()) && "".equalsIgnoreCase(edt_oh_lt_vlckts.getText().toString())){
            Toast.makeText(c,"Select Any One Of Crossing",Toast.LENGTH_SHORT).show();
        }
        else if(ch_oh_6_3_crossing.isChecked() && "".equalsIgnoreCase(edt_oh_6_3_crossing.getText().toString())){
            Toast.makeText(c,"Enter No. of 6.3 Crossings",Toast.LENGTH_SHORT).show();
        }else if(ch_oh_11kv_crossing.isChecked() && "".equalsIgnoreCase(edt_oh_11kv_crossing.getText().toString())){
            Toast.makeText(c,"Enter No. of 11kv Crossings",Toast.LENGTH_SHORT).show();
        }else if(ch_oh_33kv_crossing.isChecked() && "".equalsIgnoreCase(edt_oh_33kv_crossing.getText().toString())){
            Toast.makeText(c,"Enter No. of 33kv Crossings",Toast.LENGTH_SHORT).show();
        }else if(ch_oh_lt_crossing.isChecked() && "".equalsIgnoreCase(edt_oh_lt_crossing.getText().toString())){
            Toast.makeText(c,"Enter No. of LT Crossings",Toast.LENGTH_SHORT).show();
        }else if(ch_oh_tc_crossing.isChecked() && "".equalsIgnoreCase(edt_oh_tc_crossing.getText().toString())){
            Toast.makeText(c,"Enter No. of Tele Communication Crossings",Toast.LENGTH_SHORT).show();
        }else if(ch_oh_tm_crossing.isChecked() && "".equalsIgnoreCase(edt_oh_tm_crossing.getText().toString())){
            Toast.makeText(c,"Enter No. of Transmission Crossings",Toast.LENGTH_SHORT).show();
        } else if (oh_otherthan_streetlight.equalsIgnoreCase("Yes") && edt_oh_con_services.getText().toString().isEmpty()){
            Toast.makeText(c,"No. of Services",Toast.LENGTH_SHORT).show();
        }
        else if (sp_oh_other_cables.getSelectedItem().toString().equalsIgnoreCase("Yes") && edt_oh_other_cables.getText().toString().isEmpty()){
            Toast.makeText(c,"No. of Other Cables",Toast.LENGTH_SHORT).show();
        }
        else if (sp_oh_other_cables.getSelectedItem().toString().equalsIgnoreCase("Yes") && edt_oh_other_cables.getText().toString().equals("0")){
            Toast.makeText(c,"No. of Other Cables",Toast.LENGTH_SHORT).show();
        }

        else if("Yes".equalsIgnoreCase(oh_tapping_point) && "Select".equalsIgnoreCase(oh_noof_spurlines) && edt_oh_tappinglocation.getText().toString().trim().isEmpty()) {
            Toast.makeText(c,"Enter No of Tappings",Toast.LENGTH_SHORT).show();
        }else if("Yes".equalsIgnoreCase(oh_tapping_point) && edt_oh_tappinglocation.getText().toString().trim().isEmpty()) {
            Toast.makeText(c,"Enter Location Name",Toast.LENGTH_SHORT).show();
        }
        else {
            if (dtrCode != null & !dtrCode.isEmpty() && !ed_pole_no.getText().toString().isEmpty() && !ed_frompole.getText().toString().isEmpty() && !x.isEmpty() && !y.isEmpty()) {
                if (Integer.parseInt(ed_pole_no.getText().toString()) == Integer.parseInt(ed_frompole.getText().toString())) {
                    Toast.makeText(c, "Pole No and From Pole No Should Not Be Same Contact Admin", Toast.LENGTH_LONG).show();

                } else {
                    //  List<LTCktsResponseDataVo> ltCktsResponseDataVos = arcGISDatabase.getDTRMasterData(String.format("SELECT * FROM DTR_TABLE WHERE DTR_CODE='%s'",dtrCode));
                    PMISurveyDataVo surveyDataVo = new PMISurveyDataVo();
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
                    if ("Yes".equalsIgnoreCase(oh_tapping_point)) {
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
                        surveyDataVo.setNO_OF33KV_CKTS(edt_oh_33kv_vlckts.getText().toString());
                        surveyDataVo.setNO_OF11KV_CKTS(edt_oh_11kv_vlckts.getText().toString());
                        surveyDataVo.setNO_OF6_3KV_VOLTAGE_CKTS(edt_oh_6_3kv_vlckts.getText().toString());
                        surveyDataVo.setNO_OF_LT_CKTS(edt_oh_lt_vlckts.getText().toString());

                        surveyDataVo.setMULTI_VOLTAGE(oh_multi_voltage_same_voltage);
                    } else if ("Yes".equalsIgnoreCase(oh_ismulti_circuit) && "Same Voltage".equalsIgnoreCase(oh_multi_voltage_same_voltage)) {
                        surveyDataVo.setNO_OF_CIRCUITS(oh_noof_circuits);
                        surveyDataVo.setMULTI_VOLTAGE(oh_multi_voltage_same_voltage);

                        surveyDataVo.setNO_OF33KV_CKTS("");
                        surveyDataVo.setNO_OF11KV_CKTS("");
                        surveyDataVo.setNO_OF6_3KV_VOLTAGE_CKTS("");
                        surveyDataVo.setNO_OF_LT_CKTS("");
                    } else {
                        surveyDataVo.setNO_OF_CIRCUITS("");
                        surveyDataVo.setMULTI_VOLTAGE("");

                        surveyDataVo.setNO_OF33KV_CKTS("");
                        surveyDataVo.setNO_OF11KV_CKTS("");
                        surveyDataVo.setNO_OF6_3KV_VOLTAGE_CKTS("");
                        surveyDataVo.setNO_OF_LT_CKTS("");
                    }


                /*    surveyDataVo.setNO_OF33KV_CKTS(edt_oh_33kv_vlckts.getText().toString());
                    surveyDataVo.setNO_OF11KV_CKTS(edt_oh_11kv_vlckts.getText().toString());
                    surveyDataVo.setNO_OF6_3KV_VOLTAGE_CKTS(edt_oh_6_3kv_vlckts.getText().toString());
                    surveyDataVo.setNO_OF_LT_CKTS(edt_oh_lt_vlckts.getText().toString());*/
                    surveyDataVo.setPOLE_PAINTING_CODE(edt_oh_pole_paintingcode.getText().toString());
                    surveyDataVo.setREMARKS(edt_oh_remarks.getText().toString());
                    surveyDataVo.setLAT(x);
                    surveyDataVo.setLAN(y);
                    //  surveyDataVo.setFEEDER_ID(ltCktsResponseDataVos.get(0).getFEEDER_ID());
                    surveyDataVo.setFEEDER_ID(feeder_id);

                    surveyDataVo.setDTR_METER_AVILABLE(oh_dtr_pole_dtr_meter_avilable);
                    surveyDataVo.setDTR_NO_OF_METERS(oh_dtr_pole_no_of_meters);


                    if("YES".equalsIgnoreCase(crossing)) {
                        surveyDataVo.setIS_CROSSINGS_AV("YES");
                    }else {
                        surveyDataVo.setIS_CROSSINGS_AV("NO");
                    }
                    surveyDataVo.setNOOF_6_3KV_CROSSINGS(edt_oh_6_3_crossing.getText().toString());
                    surveyDataVo.setNOOF_11KV_CROSSINGS(edt_oh_11kv_crossing.getText().toString());
                    surveyDataVo.setNOOF_33KV_CROSSINGS(edt_oh_33kv_crossing.getText().toString());
                    surveyDataVo.setNOOF_LT_CROSSINGS(edt_oh_lt_crossing.getText().toString());
                    surveyDataVo.setNOOF_TC_CROSSINGS(edt_oh_tc_crossing.getText().toString());
                    surveyDataVo.setNOOF_TM_CROSSINGS(edt_oh_tm_crossing.getText().toString());




                    surveyDataVo.setSURVEY_FLAG("N");
                    // System.out.println("DEFECT SELECTED : "+defect_selected);
                    //NEW
                    if (defect_selected.equalsIgnoreCase("Yes")){
                        surveyDataVo.setDEFFECTS_AVAILABLE("Yes");
                        surveyDataVo.setPOLE_DEFFECTS(ch_pole_defects.isChecked() ? sp_pole_defects.getSelectedItem().toString() : "");
                        surveyDataVo.setCONDUCTOR_DEFFECTS(ch_conductors.isChecked() ? sp_conductors.getSelectedItem().toString() : "");
                        surveyDataVo.setINSULATOR_DEFFECTS(ch_insulators.isChecked() ? sp_insulators.getSelectedItem().toString() : "");
                        surveyDataVo.setCROSSARM_DEFFECTS(ch_cross_arm.isChecked() ? sp_cross_arm.getSelectedItem().toString() : "");
                        surveyDataVo.setSTRUT_DEFFECTS(ch_strut_pole.isChecked() ? sp_strut_pole.getSelectedItem().toString() : "");
                        surveyDataVo.setSTRAY_DEFFECTS(ch_stray_wire.isChecked() ? sp_stray_wire.getSelectedItem().toString() : "");
                        surveyDataVo.setHTLT_CLEARANCE(ch_ht_clearance.isChecked() ? sp_ht_clearance.getSelectedItem().toString() : "");
                        surveyDataVo.setINTERMEDIATE_POLE(ch_int_pole.isChecked() ? sp_int_pole.getSelectedItem().toString() : "");
                        surveyDataVo.setOTHER_DEFECTS(ch_other_defects.isChecked() ? ed_other_defects.getText().toString().trim() : "");
                        surveyDataVo.setRESTRINGING("");
                    }else {
                        surveyDataVo.setDEFFECTS_AVAILABLE("No");
                        surveyDataVo.setPOLE_DEFFECTS("");
                        surveyDataVo.setCONDUCTOR_DEFFECTS("");
                        surveyDataVo.setINSULATOR_DEFFECTS("");
                        surveyDataVo.setCROSSARM_DEFFECTS("");
                        surveyDataVo.setSTRUT_DEFFECTS("");
                        surveyDataVo.setSTRAY_DEFFECTS("");
                        surveyDataVo.setHTLT_CLEARANCE("");
                        surveyDataVo.setINTERMEDIATE_POLE("");
                        surveyDataVo.setOTHER_DEFECTS("");
                        surveyDataVo.setRESTRINGING("");
                    }
                    if (oh_otherthan_streetlight.equalsIgnoreCase("Yes")){
                        surveyDataVo.setNUM_SCS(edt_oh_con_services.getText().toString());

                    }else {
                        surveyDataVo.setNUM_SCS("");
                    }

                    //IMAGES START

                    surveyDataVo.setIMAGE_1(image_1_clicked ? image_1_base64 : "N");
                    surveyDataVo.setIMAGE_2(image_2_clicked ? image_2_base64 : "N");
                    surveyDataVo.setIMAGE_3(image_3_clicked ? image_3_base64 : "N");


                    //IMAGES END

                    //   System.out.println("DATA TO DATABASE : "+surveyDataVo);

                    //OTHER CABLES
                    if (sp_oh_other_cables.getSelectedItem().toString().equalsIgnoreCase("Yes")){
                        surveyDataVo.setNUM_OTHER_CABLES(edt_oh_other_cables.getText().toString());
                    }else {
                        surveyDataVo.setNUM_OTHER_CABLES("");
                    }

                    boolean normalSurveyDone = arcGISDatabase.addSurveyData(surveyDataVo);
                    boolean repeatSurveyDone = arcGISDatabase.addSurveyDatarepeat(surveyDataVo);
                    boolean imageSurveyDone = arcGISDatabase.addLTImageData(surveyDataVo);

                    //  System.out.println("normalSurveyDone : "+normalSurveyDone);
                    //  System.out.println("repeatSurveyDone : "+repeatSurveyDone);
                    //  System.out.println("imageSurveyDone : "+imageSurveyDone);

                    image_1_clicked = false;
                    image_2_clicked = false;
                    image_3_clicked = false;

                    Toast.makeText(c, "Pole location captured successfully..", Toast.LENGTH_SHORT).show();

                    PMIArcGISMapActivity11kv.isLTSurveyStarted = true;
                    PMIArcGISMapActivity11kv.isPoleAdded = true;
                    dismiss();
                    callout.dismiss();
                    graphicsOverlay.getGraphics().remove(graphic);

                    if ("Yes".equalsIgnoreCase(oh_isendpole)) {
                        Intent intent = new Intent(c, PMIHomeActivity.class);
                        c.startActivity(intent);
                        c.finish();
                    }

                    if (sp_ckt_type.getSelectedItem().toString().equalsIgnoreCase("Yes")){
                        if (PMIArcGISMapActivity11kv.ckt1Available){
                            if (PMIArcGISMapActivity11kv.ckt1Completed){
                                if (PMIArcGISMapActivity11kv.ckt2Available){
                                    if (PMIArcGISMapActivity11kv.ckt2Completed){
                                        if (PMIArcGISMapActivity11kv.ckt3Available){
                                            if (PMIArcGISMapActivity11kv.ckt3Completed){
                                                Intent intent = new Intent(c, PMIHomeActivity.class);
                                                c.startActivity(intent);
                                                c.finish();
                                            }else {
                                                PMIArcGISMapActivity11kv.ckt3Completed=true;
                                                PMIArcGISMapActivity11kv.isCKTChanged = true;

                                            }
                                        }
                                    }else {
                                        PMIArcGISMapActivity11kv.ckt2Completed=true;
                                        PMIArcGISMapActivity11kv.isCKTChanged = true;
                                        PMIArcGISMapActivity11kv.current_ckt = PMIArcGISMapActivity11kv.ckt3;
                                        /*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
                                        alertDialogBuilder.setTitle("Survey Completed for "+ArcGISMapActivity.ckt2);
                                        alertDialogBuilder
                                                .setMessage("Start Survey for "+ArcGISMapActivity.ckt3)
                                                .setCancelable(false)
                                                .setPositiveButton("Okay",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                dialog.cancel();

                                                            }
                                                        });
                                        AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.show();*/

                                    }
                                }
                            }else {
                                PMIArcGISMapActivity11kv.ckt1Completed = true;
                                PMIArcGISMapActivity11kv.isCKTChanged = true;
                                PMIArcGISMapActivity11kv.current_ckt = PMIArcGISMapActivity11kv.ckt2;
                               /* AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
                                alertDialogBuilder.setTitle("Survey Completed for "+ArcGISMapActivity.ckt1);
                                alertDialogBuilder
                                        .setMessage("Start Survey for "+ArcGISMapActivity.ckt2)
                                        .setCancelable(false)
                                        .setPositiveButton("Okay",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();

                                                    }
                                                });
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();*/

                            }

                        }
                    }else {
                        PMIArcGISMapActivity11kv.isCKTChanged = false;
                    }


                    if (PMIArcGISMapActivity11kv.ckt1Completed){
                        if (!PMIArcGISMapActivity11kv.ckt2Available){
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
                            alertDialogBuilder.setTitle("LT Survey Completed!!");
                            alertDialogBuilder
                                    .setMessage("Do You Want to start 11KV Survey again?")
                                    .setCancelable(false)
                                    .setPositiveButton("YES",
                                            new OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                    PMIArcGISMapActivity11kv.typeOfSurvey = "11kv";
                                                    PMIArcGISMapActivity11kv.isLTSurveyStarted = false;
                                                    PMIArcGISMapActivity11kv.isDTRSurvey = false;
                                                }
                                            })

                                    .setNegativeButton("NO", new OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            Intent intent = new Intent(c, PMIHomeActivity.class);
                                            c.startActivity(intent);
                                            c.finish();
                                        }
                                    });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }

                    }
                    if (PMIArcGISMapActivity11kv.ckt2Completed){
                        if (!PMIArcGISMapActivity11kv.ckt3Available){
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
                            alertDialogBuilder.setTitle("LT Survey Completed!!");
                            alertDialogBuilder
                                    .setMessage("Do You Want to start 11KV Survey again?")
                                    .setCancelable(false)
                                    .setPositiveButton("YES",
                                            new OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                    PMIArcGISMapActivity11kv.typeOfSurvey = "11kv";
                                                    PMIArcGISMapActivity11kv.isLTSurveyStarted = false;
                                                    PMIArcGISMapActivity11kv.isDTRSurvey = false;
                                                }
                                            })

                                    .setNegativeButton("NO", new OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            Intent intent = new Intent(c, PMIHomeActivity.class);
                                            c.startActivity(intent);
                                            c.finish();
                                        }
                                    });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }
                    }
                    if (PMIArcGISMapActivity11kv.ckt3Completed){
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
                        alertDialogBuilder.setTitle("LT Survey Completed!!");
                        alertDialogBuilder
                                .setMessage("Do You Want to start 11KV Survey again?")
                                .setCancelable(false)
                                .setPositiveButton("YES",
                                        new OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                                PMIArcGISMapActivity11kv.typeOfSurvey = "11kv";
                                                PMIArcGISMapActivity11kv.isLTSurveyStarted = false;
                                                PMIArcGISMapActivity11kv.isDTRSurvey = false;
                                            }
                                        })

                                .setNegativeButton("NO", new OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        Intent intent = new Intent(c, PMIHomeActivity.class);
                                        c.startActivity(intent);
                                        c.finish();
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                }
            }

        }
    }

    public void underGround(){
        if (dtrCode != null & !dtrCode.isEmpty() && !ed_pole_no.getText().toString().isEmpty() && !ed_frompole.getText().toString().isEmpty() && !x.isEmpty() && !y.isEmpty()) {
            if (Integer.parseInt(ed_pole_no.getText().toString()) == Integer.parseInt(ed_frompole.getText().toString())) {
                Toast.makeText(c, "Pole No and From Pole No Should Not Be Same Contact Admin", Toast.LENGTH_LONG).show();

            }/*else if("DTR Pole".equalsIgnoreCase(lineType)&&"Yes".equalsIgnoreCase(ug_dtr_pole_dtr_meter_avilable)&&"Select".equalsIgnoreCase(oh_dtr_pole_no_of_meters)){
                Toast.makeText(c,"Select No Of Meters",Toast.LENGTH_SHORT).show();
            }*/

            else if (sp_ug_locationtype.getSelectedItem().toString().equals("Pole") && sp_ug_poleheight.getSelectedItem().toString().equals("Select")){
                Toast.makeText(c, "Select Pole Height", Toast.LENGTH_LONG).show();
            }
            else if (sp_ug_tapping_point.getSelectedItem().toString().equals("Yes") && edt_ug_tappinglocation.getText().toString().isEmpty()){
                Toast.makeText(c, "Enter Tapping Location Name", Toast.LENGTH_LONG).show();
            }

            else {

                // List<LTCktsResponseDataVo> ltCktsResponseDataVos = arcGISDatabase.getDTRMasterData("");
                PMISurveyDataVo surveyDataVos = new PMISurveyDataVo();
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

              /*  if ("Pole".equalsIgnoreCase(ug_locationtype) || "Rail way Crsossing".equalsIgnoreCase(ug_locationtype) || "Road Crossing".equalsIgnoreCase(ug_locationtype)) {
                    surveyDataVos.setPOLE_TYPE(ug_poletype);
                    surveyDataVos.setPOLE_HEIGHT(ug_poleheight);
                    surveyDataVos.setSTRUT_STAY(ug_polesupport);
                } else {
                    surveyDataVos.setPOLE_TYPE("");
                    surveyDataVos.setPOLE_HEIGHT("");
                    surveyDataVos.setSTRUT_STAY("");
                }*/
                surveyDataVos.setPOLE_TYPE("");
                surveyDataVos.setPOLE_HEIGHT(ug_poleheight);
                surveyDataVos.setSTRUT_STAY("");
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
                surveyDataVos.setFEEDER_ID(feeder_id);
                surveyDataVos.setLAT(x);
                surveyDataVos.setLAN(y);
                surveyDataVos.setSURVEY_FLAG("N");
                surveyDataVos.setDTR_METER_AVILABLE(ug_dtr_pole_dtr_meter_avilable);
                surveyDataVos.setDTR_NO_OF_METERS(ug_dtr_pole_no_of_meters);
                surveyDataVos.setNUM_SCS("");

                //IMAGES START

                surveyDataVos.setIMAGE_1(image_1_clicked ? image_1_base64 : "N");
                surveyDataVos.setIMAGE_2(image_2_clicked ? image_2_base64 : "N");
                surveyDataVos.setIMAGE_3(image_3_clicked ? image_3_base64 : "N");

                //IMAGES END

                boolean normalSurveyDone = arcGISDatabase.addSurveyData(surveyDataVos);
                boolean repeatSurveyDone = arcGISDatabase.addSurveyDatarepeat(surveyDataVos);
                boolean imageSurveyDone =  arcGISDatabase.addLTImageData(surveyDataVos);
                //  System.out.println("normalSurveyDone : "+normalSurveyDone);
                // System.out.println("repeatSurveyDone : "+repeatSurveyDone);
                //  System.out.println("imageSurveyDone : "+imageSurveyDone);

                image_1_clicked = false;
                image_2_clicked = false;
                image_3_clicked = false;

                Toast.makeText(c, "Pole location captured successfully..", Toast.LENGTH_SHORT).show();
                PMIArcGISMapActivity11kv.isLTSurveyStarted = true;
                PMIArcGISMapActivity11kv.isPoleAdded = true;
                dismiss();
                callout.dismiss();
                graphicsOverlay.getGraphics().remove(graphic);
                if ("Yes".equalsIgnoreCase(ug_isendpole)) {
                    Intent intent = new Intent(c, PMIHomeActivity.class);
                    c.startActivity(intent);
                    c.finish();
                }

                if ("Yes".equalsIgnoreCase(oh_isendpole)) {
                    Intent intent = new Intent(c, PMIHomeActivity.class);
                    c.startActivity(intent);
                    c.finish();
                }

                if (sp_ckt_type.getSelectedItem().toString().equalsIgnoreCase("Yes")){
                    if (PMIArcGISMapActivity11kv.ckt1Available){
                        if (PMIArcGISMapActivity11kv.ckt1Completed){
                            if (PMIArcGISMapActivity11kv.ckt2Available){
                                if (PMIArcGISMapActivity11kv.ckt2Completed){
                                    if (PMIArcGISMapActivity11kv.ckt3Available){
                                        if (PMIArcGISMapActivity11kv.ckt3Completed){
                                            Intent intent = new Intent(c, PMIHomeActivity.class);
                                            c.startActivity(intent);
                                            c.finish();
                                        }else {
                                            PMIArcGISMapActivity11kv.ckt3Completed=true;
                                            PMIArcGISMapActivity11kv.isCKTChanged = true;

                                        }
                                    }
                                }else {
                                    PMIArcGISMapActivity11kv.ckt2Completed=true;
                                    PMIArcGISMapActivity11kv.isCKTChanged = true;
                                    PMIArcGISMapActivity11kv.current_ckt = PMIArcGISMapActivity11kv.ckt3;
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
                                    alertDialogBuilder.setTitle("Survey Completed for "+PMIArcGISMapActivity11kv.ckt2);
                                    alertDialogBuilder
                                            .setMessage("Start Survey for "+PMIArcGISMapActivity11kv.ckt3)
                                            .setCancelable(false)
                                            .setPositiveButton("Okay",
                                                    new OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();

                                                        }
                                                    });
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();

                                }
                            }
                        }else {
                            PMIArcGISMapActivity11kv.ckt1Completed = true;
                            PMIArcGISMapActivity11kv.isCKTChanged = true;
                            PMIArcGISMapActivity11kv.current_ckt = PMIArcGISMapActivity11kv.ckt2;
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
                            alertDialogBuilder.setTitle("Survey Completed for "+PMIArcGISMapActivity11kv.ckt1);
                            alertDialogBuilder
                                    .setMessage("Start Survey for "+PMIArcGISMapActivity11kv.ckt2)
                                    .setCancelable(false)
                                    .setPositiveButton("Okay",
                                            new OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();

                                                }
                                            });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();

                        }

                    }
                }else {
                    PMIArcGISMapActivity11kv.isCKTChanged = false;
                }


                if (PMIArcGISMapActivity11kv.ckt1Completed){
                    if (!PMIArcGISMapActivity11kv.ckt2Available){
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
                        alertDialogBuilder.setTitle("LT Survey Completed!!");
                        alertDialogBuilder
                                .setMessage("Do You Want to start 11KV Survey again?")
                                .setCancelable(false)
                                .setPositiveButton("YES",
                                        new OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                                PMIArcGISMapActivity11kv.typeOfSurvey = "11kv";
                                                PMIArcGISMapActivity11kv.isLTSurveyStarted = false;
                                                PMIArcGISMapActivity11kv.isDTRSurvey = false;
                                            }
                                        })

                                .setNegativeButton("NO", new OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        Intent intent = new Intent(c, PMIHomeActivity.class);
                                        c.startActivity(intent);
                                        c.finish();
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }

                }
                if (PMIArcGISMapActivity11kv.ckt2Completed){
                    if (!PMIArcGISMapActivity11kv.ckt3Available){
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
                        alertDialogBuilder.setTitle("LT Survey Completed!!");
                        alertDialogBuilder
                                .setMessage("Do You Want to start 11KV Survey again?")
                                .setCancelable(false)
                                .setPositiveButton("YES",
                                        new OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                                PMIArcGISMapActivity11kv.typeOfSurvey = "11kv";
                                                PMIArcGISMapActivity11kv.isLTSurveyStarted = false;
                                                PMIArcGISMapActivity11kv.isDTRSurvey = false;
                                            }
                                        })

                                .setNegativeButton("NO", new OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        Intent intent = new Intent(c, PMIHomeActivity.class);
                                        c.startActivity(intent);
                                        c.finish();
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                }
                if (PMIArcGISMapActivity11kv.ckt3Completed){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
                    alertDialogBuilder.setTitle("LT Survey Completed!!");
                    alertDialogBuilder
                            .setMessage("Do You Want to start 11KV Survey again?")
                            .setCancelable(false)
                            .setPositiveButton("YES",
                                    new OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            PMIArcGISMapActivity11kv.typeOfSurvey = "11kv";
                                            PMIArcGISMapActivity11kv.isLTSurveyStarted = false;
                                            PMIArcGISMapActivity11kv.isDTRSurvey = false;
                                        }
                                    })

                            .setNegativeButton("NO", new OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    Intent intent = new Intent(c, PMIHomeActivity.class);
                                    c.startActivity(intent);
                                    c.finish();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        }
    }



    public void re_pole(){

        sp_poleheight_KEY = "R";

        surveyVoList = arcGISDatabase.getSurveyDataREPEAT("SELECT * FROM SURVEY_TABLE_REPEAT LIMIT 1 OFFSET (SELECT COUNT(*) FROM SURVEY_TABLE_REPEAT)-1");
        if (surveyVoList.size() > 0) {


            for (PMISurveyDataVo surveyVo : surveyVoList) {



                String IS_lineType = surveyVo.getLINE_TYPE();
                List<String> key_lineType = new ArrayList<>();
                key_lineType.add(IS_lineType);
       /*         key_lineType.add("Overhead");
                key_lineType.add("UnderGround");*/
                ArrayAdapter<String> adapter_lineType = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, key_lineType);
                sp_lineType.setAdapter(adapter_lineType);

                //   if (surveyVo.getLINE_TYPE().equals("Overhead")){



/*
                String IS_lineTypee = surveyVo.getLINE_TYPE();
                List<String> key_lineTypee = new ArrayList<>();
                key_lineTypee.add(IS_lineTypee);
       */
/*         key_lineType.add("Overhead");
                key_lineType.add("UnderGround");*//*

                ArrayAdapter<String> adapter_lineTypee = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, key_lineTypee);
                sp_lineType.setAdapter(adapter_lineType);
*/





                String IS_END_POLE = surveyVo.getIS_END_POLE();
                List<String> key_IS_END_POLE = new ArrayList<>();
                key_IS_END_POLE.add(IS_END_POLE);
                ArrayAdapter<String> adapter_isstreetlight = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, key_IS_END_POLE);
                sp_oh_isendpole.setAdapter(adapter_isstreetlight);



                String s_poleheight = surveyVo.getLINE_CONFIGURATION();
                List<String> lineconfig = new ArrayList<>();
                lineconfig.add(s_poleheight);
              /*  lineconfig.add("1-Ph, 2- Wire");
                lineconfig.add("1-Ph, 3- Wire");
                lineconfig.add("1-Ph, 4- Wire");
                lineconfig.add("3-Ph, 5- Wire");
                lineconfig.add("1*16+25sqmm XLPE Cable");
                lineconfig.add("2*16+25sqmm XLPE Cable");
                lineconfig.add("3*16+25sqmmXLPE Cable");
                lineconfig.add("3-ph,3-wire");*/
                ArrayAdapter<String> adapter_h = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, lineconfig);
                sp_oh_lineconfig.setAdapter(adapter_h);

/*
                String isstreetlight = surveyVo.getIS_STREETLIGHT_AV();
                List<String> oh_isstreetlight = new ArrayList<>();
                oh_isstreetlight.add(isstreetlight);
              *//*  oh_isstreetlight.add("No");
                oh_isstreetlight.add("Yes");*//*
                ArrayAdapter<String> adapter_oh_lineconfig = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, oh_isstreetlight);
                sp_oh_isstreetlight.setAdapter(adapter_oh_lineconfig);*/




          /*      String st_oh_otherthan_streetlight = surveyVo.getIS_STREETLIGHT_AV();
                List<String> oh_otherthan_streetlight = new ArrayList<>();
                oh_otherthan_streetlight.add(st_oh_otherthan_streetlight);

                ArrayAdapter<String> adapter_oh_otherthan_streetlight = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, oh_otherthan_streetlight);
                sp_oh_otherthan_streetlight.setAdapter(adapter_oh_otherthan_streetlight);

*/
                String oh_dtr_pole_dtr_meter_avilable = surveyVo.getIS_STREETLIGHT_AV();
                List<String> oh_oh_dtr_pole_dtr_meter_avilable = new ArrayList<>();
                oh_oh_dtr_pole_dtr_meter_avilable.add(oh_dtr_pole_dtr_meter_avilable);
          /*      oh_oh_dtr_pole_dtr_meter_avilable.add("No");
                oh_oh_dtr_pole_dtr_meter_avilable.add("Yes");*/
                ArrayAdapter<String> adapter_oh_dtr_pole_dtr_meter_avilable = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, oh_oh_dtr_pole_dtr_meter_avilable);
                sp_oh_dtr_pole_dtr_meter_avilable.setAdapter(adapter_oh_dtr_pole_dtr_meter_avilable);






                String oh_oh_pin_cutpoint = surveyVo.getPIN_CUT();
                List<String> oh_pin_cutpoint = new ArrayList<>();
                oh_pin_cutpoint.add(oh_oh_pin_cutpoint);
                ArrayAdapter<String> adapter_pin_cutpoint = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, oh_pin_cutpoint);
                sp_oh_pin_cutpoint.setAdapter(adapter_pin_cutpoint);




                String st_oh_pole_type = surveyVo.getPOLE_TYPE();
                List<String> list_oh_pole_type = new ArrayList<>();
                list_oh_pole_type.add(st_oh_pole_type);

                ArrayAdapter<String> adapter_oh_pole_type = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_oh_pole_type);
                sp_oh_pole_type.setAdapter(adapter_oh_pole_type);



                String is_sp_poleheight = surveyVo.getPOLE_HEIGHT();

                List<String> list_poleheight = new ArrayList<>();
                list_poleheight.add(is_sp_poleheight);
                ArrayAdapter<String> adapter_pole_height = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line,list_poleheight);
                sp_oh_pole_height.setAdapter(adapter_pole_height);



          /*      String st_oh_pole_height = surveyVo.getPOLE_HEIGHT();
                List<String> list_oh_pole_height = new ArrayList<>();
                list_oh_pole_height.add(st_oh_pole_height);

                ArrayAdapter<String> adapter_oh_pole_height = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_oh_pole_height);
                sp_ug_poleheight.setAdapter(adapter_oh_pole_height);*/



/*

                String st_oh_strut_stay_details = surveyVo.getSTRUT_STAY();
                List<String> list_oh_strut_stay_details = new ArrayList<>();
                list_oh_strut_stay_details.add(st_oh_strut_stay_details);

                ArrayAdapter<String> adapter_oh_strut_stay_details = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_oh_strut_stay_details);
                sp_oh_strut_stay_details.setAdapter(adapter_oh_strut_stay_details);


                String st_sp_oh_tapping_point = surveyVo.getTAPPING_POINT();
                List<String> list_sp_oh_tapping_point = new ArrayList<>();
                list_sp_oh_tapping_point.add(st_sp_oh_tapping_point);

                ArrayAdapter<String> adapter_sp_oh_tapping_point = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_sp_oh_tapping_point);
                sp_oh_tapping_point.setAdapter(adapter_sp_oh_tapping_point);




                String st_oh_conductor_size = surveyVo.getCONDUCTOR_SIZE();
                List<String> list_oh_conductor_size = new ArrayList<>();
                list_oh_conductor_size.add(st_oh_conductor_size);

                ArrayAdapter<String> adapter_oh_conductor_size = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_oh_conductor_size);
                sp_oh_conductor_size.setAdapter(adapter_oh_conductor_size);




                String st_sp_oh_ismulti_circuit = surveyVo.getIS_MULTI_CIRCUIT();
                List<String> list_sp_oh_ismulti_circuit = new ArrayList<>();
                list_sp_oh_ismulti_circuit.add(st_sp_oh_ismulti_circuit);

                ArrayAdapter<String> adapter_sp_oh_ismulti_circuit = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_sp_oh_ismulti_circuit);
                sp_oh_ismulti_circuit.setAdapter(adapter_sp_oh_ismulti_circuit);
*/


               /* String st_sp_crossing = surveyVo.getIS_MULTI_CIRCUIT();
                List<String> list_st_sp_crossing = new ArrayList<>();
                list_st_sp_crossing.add(st_sp_crossing);

                ArrayAdapter<String> adapter_list_st_sp_crossing = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_st_sp_crossing);
                sp_crossing.setAdapter(adapter_list_st_sp_crossing);
*/


                String st_sp_crossing = surveyVo.getIS_CROSSINGS_AV();
                List<String> list_st_sp_crossing = new ArrayList<>();
                list_st_sp_crossing.add(st_sp_crossing);

                list_st_sp_crossing.add(st_sp_crossing);
              /*  if (st_sp_crossing.equals("YES")){

                    list_st_sp_crossing.add("NO");


                }
                else if (st_sp_crossing.equals("NO")){
                    list_st_sp_crossing.add("YES");
                }
*/

                ArrayAdapter<String> adapter_list_st_sp_crossing = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_st_sp_crossing);
                sp_crossing.setAdapter(adapter_list_st_sp_crossing);



                try {
                    String st_oh_conductor_size = surveyVo.getIS_STREETLIGHT_AV();
                    List<String> list_oh_conductor_size = new ArrayList<>();
                    list_oh_conductor_size.add(st_oh_conductor_size);

                    if (st_oh_conductor_size.equals("Yes")){

                        list_oh_conductor_size.add("No");

                    }else if (st_oh_conductor_size.equals("No")){

                        list_oh_conductor_size.add("Yes");
                    }
                    ArrayAdapter<String> adapter_oh_conductor_size = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_oh_conductor_size);
                    sp_oh_isstreetlight.setAdapter(adapter_oh_conductor_size);

                } catch (Exception e) {
                    e.printStackTrace();
                }




                String st_sp_oh_tapping_point = surveyVo.getTAPPING_POINT();
                List<String> list_sp_oh_tapping_point = new ArrayList<>();
                list_sp_oh_tapping_point.add(st_sp_oh_tapping_point);
                if (st_sp_oh_tapping_point.equals("Yes")){

                    list_sp_oh_tapping_point.add("No");

                }else if (st_sp_oh_tapping_point.equals("No")){
                    list_sp_oh_tapping_point.add("Yes");
                }
                ArrayAdapter<String> adapter_sp_oh_tapping_point = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_sp_oh_tapping_point);
                sp_oh_tapping_point.setAdapter(adapter_sp_oh_tapping_point);




                String st_oh_otherthan_streetlight = surveyVo.getIS_SERVICES_OTHERTHAN_SL();
                List<String> oh_otherthan_streetlight = new ArrayList<>();
                oh_otherthan_streetlight.add(st_oh_otherthan_streetlight);
                if (st_oh_otherthan_streetlight.equals("No")){

                    oh_otherthan_streetlight.add("Yes");

                }else if (st_oh_otherthan_streetlight.equals("Yes")){
                    oh_otherthan_streetlight.add("No");
                }
                ArrayAdapter<String> adapter_oh_otherthan_streetlight = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, oh_otherthan_streetlight);
                sp_oh_otherthan_streetlight.setAdapter(adapter_oh_otherthan_streetlight);




                String st_oh_strut_stay_details = surveyVo.getSTRUT_STAY();
                List<String> list_oh_strut_stay_details = new ArrayList<>();
                list_oh_strut_stay_details.add(st_oh_strut_stay_details);

                ArrayAdapter<String> adapter_oh_strut_stay_details = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_oh_strut_stay_details);
                sp_oh_strut_stay_details.setAdapter(adapter_oh_strut_stay_details);




                String st_noof_spurlines = surveyVo.getNO_OF_SPURLINES();
                List<String> list_oh_noof_spurlines = new ArrayList<>();
                list_oh_noof_spurlines.add(st_noof_spurlines);

                ArrayAdapter<String> adapter_oh_noof_spurlines = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_oh_noof_spurlines);
                sp_oh_noof_spurlines.setAdapter(adapter_oh_noof_spurlines);


                String st_sp_oh_ismulti_circuit = surveyVo.getIS_MULTI_CIRCUIT();
                List<String> list_sp_oh_ismulti_circuit = new ArrayList<>();
                list_sp_oh_ismulti_circuit.add(st_sp_oh_ismulti_circuit);

                ArrayAdapter<String> adapter_sp_oh_ismulti_circuit = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_sp_oh_ismulti_circuit);
                sp_oh_ismulti_circuit.setAdapter(adapter_sp_oh_ismulti_circuit);




                String st_sp_oh_conductor_size = surveyVo.getCONDUCTOR_SIZE();
                List<String> list_sp_oh_conductor_size = new ArrayList<>();
                list_sp_oh_conductor_size.add(st_sp_oh_conductor_size);

                ArrayAdapter<String> adapter_sp_oh_conductor_size = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_sp_oh_conductor_size);
                sp_oh_conductor_size.setAdapter(adapter_sp_oh_conductor_size);


                /* Under Ground */
                //  }

                //   else if (surveyVo.getLINE_TYPE().equals("UnderGround")){


             /*       String IS_lineTypeu = surveyVo.getLINE_TYPE();
                    List<String> key_lineTypeu = new ArrayList<>();
                    key_lineTypeu.add(IS_lineTypeu);
                    ArrayAdapter<String> adapter_lineTypee = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, key_lineTypeu);
                    sp_lineType.setAdapter(adapter_lineType);



                    String st_ug_isendpole = surveyVo.getIS_END_POLE();
                    List<String> list_sp_ug_isendpole = new ArrayList<>();
                    list_sp_ug_isendpole.add(st_ug_isendpole);

                    ArrayAdapter<String> adapter_sp_ug_isendpole = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_sp_ug_isendpole);
                    sp_ug_isendpole.setAdapter(adapter_sp_ug_isendpole);




                    String st_oh_pole_height = surveyVo.getPOLE_HEIGHT();
                    List<String> list_oh_pole_height = new ArrayList<>();
                    list_oh_pole_height.add(st_oh_pole_height);

                    ArrayAdapter<String> adapter_oh_pole_height = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_oh_pole_height);
                    sp_ug_poleheight.setAdapter(adapter_oh_pole_height);





                    String st_sp_ug_locationtype = surveyVo.getLOCATION_TYPE();
                    List<String> list_ug_locationtype = new ArrayList<>();
                    list_ug_locationtype.add(st_sp_ug_locationtype);

                    ArrayAdapter<String> adapter_sp_ug_locationtype = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_ug_locationtype);
                    sp_ug_locationtype.setAdapter(adapter_sp_ug_locationtype);







                    String st_ug_tapping_point = surveyVo.getTAPPING_POINT();
                    List<String> list_sp_ug_tapping_point = new ArrayList<>();
                    list_sp_ug_tapping_point.add(st_ug_tapping_point);

                    ArrayAdapter<String> adapter_sp_ug_tapping_point = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_sp_ug_tapping_point);
                    sp_ug_tapping_point.setAdapter(adapter_sp_ug_tapping_point);








                    String st_ug_ug_cablesize = surveyVo.getCABLE_SIZE();
                    List<String> list_sp_ug_cablesize = new ArrayList<>();
                    list_sp_ug_cablesize.add(st_ug_ug_cablesize);

                    ArrayAdapter<String> adapter_list_sp_ug_cablesize = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_sp_ug_cablesize);
                    sp_ug_cablesize.setAdapter(adapter_list_sp_ug_cablesize);
*/

                //   }





                //   surveyDataVo.setPIN_CUT(oh_pin_cutpoint);

            }

        }


    }
}