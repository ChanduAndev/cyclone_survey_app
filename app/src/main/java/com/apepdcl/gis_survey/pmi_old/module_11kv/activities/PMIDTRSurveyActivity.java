package com.apepdcl.gis_survey.pmi_old.module_11kv.activities;

import static com.apepdcl.gis_survey.pmi_old.module_11kv.activities.PMIDTRSelectActivity.dtrvo;
import static com.apepdcl.gis_survey.pmi_old.module_11kv.activities.PMIDTRSelectActivity.not_updated;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.pmi_old.module_11kv.PMIArcGISDatabase;
import com.apepdcl.gis_survey.pmi_old.module_11kv.PMIUpdatedDTRVO;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIDTRDetailsResponseVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIDTRSurveyVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMISurveyDataVo;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PMIDTRSurveyActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    // public Activity c;
    private TextView tv_dtrstrcuture;
    private EditText ed_pole_no, ed_frompole;

    private String lineType, oh_isendpole, oh_lineconfig, oh_isstreetlight, oh_otherthan_streetlight, oh_pin_cutpoint, oh_is_doublefeeding_pole, oh_pole_type, oh_pole_height, oh_strut_stay_details, oh_tapping_point, oh_noof_spurlines, oh_conductor_size, oh_ismulti_circuit, oh_noof_circuits, oh_multi_voltage_same_voltage,
            ug_isendpole, ug_locationtype, ug_servicepiller_type, ug_poletype, ug_poleheight, ug_polesupport, ug_feedepiller_outg_circuits, ug_tapping_point, ug_notapping_point, ug_cablesize, ug_nocable;
    private LinearLayout lyt_dtrmeter, lyt_modem, lyt_busbar_condoctorsize, lyt_dtrpolepin, lyt_dtrpolecut, lyt_nof_dtrmeters, lyt_dtrmeter_two, lyt_dtrmeter_three, lyt_lt_busbarconductor_size, lyt_busbarcable_size, lyt_noof_sturts, lyt_dtrhgfuse, lyt_noof_stay, lyt_abswt_type, lyt_gipipes, lyt_cipipes, lyt_modemtwo, lyt_modem_three, lyt_earthpitstype;
    private CheckBox ch_oh_aglservices, ch_oh_aquaservices, ch_oh_industrialservices, ch_oh_33kv_vlckts, ch_oh_11kv_vlckts, ch_oh_6_3kv_vlckts, ch_oh_lt_vlckts;
    private TextInputEditText edt_oh_aglservices, edt_oh_aquaservices, edt_oh_industrialservices, edt_oh_tappinglocation, edt_oh_33kv_vlckts, edt_oh_11kv_vlckts, edt_oh_6_3kv_vlckts, edt_oh_lt_vlckts, edt_oh_pole_paintingcode, edt_oh_remarks, edt_ug_tappinglocation, edt_ug_pole_paintingcode, edt_ug_remarks;
    private TextInputEditText edt_meternum, edt_metermake, edt_metercapacity, edt_meternum_two, edt_metermake_two, edt_metercapacity_two, edt_meternum_three, edt_metermake_three, edt_metercapacity_three, edt_modemserial_no, edt_modem_make, edt_landmark, edt_dtrserialnum, edt_noof_sturts, edt_noof_stay, edt_noof_cpin_insulators, edt_noof_disc_insulators, edt_modemtwo_serial_no, edt_modemtwo_make, edt_modemthree_serial_no, edt_modemthree_make;
    private Button btn_clear, btn_add, btn_lttopclear;
    private PMIArcGISDatabase arcGISDatabase;
    private List<PMISurveyDataVo> surveyVosList;
    private int maxSurveyCount, poleCountCheck;
    private String dtrCode,dtrlLocation;

    Callout callout;
    GraphicsOverlay graphicsOverlay;
    Graphic graphic;
    private int noof_33kvCkts, noof_11kvCkts, noof_6_3kvCkts, noof_ltCkts;
    private LinearLayout lay_oh_dtr_pole_dtr_meter_avilable, lay_oh_dtr_pole_no_of_meter, lay_ug_dtr_pole_dtr_meter_avilable, lay_ug_dtr_pole_no_of_meter;
    private Spinner sp_dtrabswt, sp_dtrabswt_type, sp_dtrgh_fuseset, sp_earthpits, sp_cipipes, sp_gipipes, sp_dtrmeter, sp_metertype, sp_modem_available, sp_busbar_type, sp_consuctorsize, sp_hgfuseto_dtr, sp_hgfuseto_dtr_consuctorsize, sp_dtrpole, sp_pin, sp_cut, sp_lt_fuseset, sp_dtrload, sp_dtrphase, sp_dtrcapacity, sp_dtrstr_type, sp_dtrs_pole_support,
            sp_noof_dtrmeters, sp_metertype_two, sp_metertype_three, sp_lt_busbar, sp_lt_busbarcable_size, sp_lt_busbarconductor_size, sp_noof_pininsulators, sp_earthpits_type, sp_modemtwo_available, sp_modemthree_available, sp_noof_ltcircuits;
    private String oh_dtr_pole_dtr_meter_avilable, oh_dtr_pole_no_of_meters, ug_dtr_pole_dtr_meter_avilable, ug_dtr_pole_no_of_meters;
    private String dtrABswt, dtrgh_fuseset, earthpits, cipipes, gipipes, dtrmeter, metertype, modem_available, busbar_type, consuctorsize, hgfuseto_dtr, hgfuseto_dtr_consuctorsize, dtrpole, pin, cut, lt_fuseset,
            dtrload, dtrphase, dtrcapacity, dtrstr_type, noof_dtrmeters, metertype_two, metertype_three, lt_busbar, lt_busbarcable_size, lt_busbarconductor_size, dtrs_pole_support, dtrabswt_type, noof_pininsulators, earthpits_type, modemtwo_available, modemthree_available, noof_ltcircuits;
    private AutoCompleteTextView aut_dtrmake;
    private String[] dtrMake;
    private String[] dtrLoad;
    private int earthPitsNo = 0, ciPipesCount = 0, giPipesCount = 0, ciGiPipesCount = 0;
    List<PMIDTRDetailsResponseVo> dtrDetailsResponseVoList = null;

    //NEW DTR DEFECTS
    //DEFECTS NEW
    LinearLayout defects_layout;
    Spinner sp_dtr_defects,sp_structure_defects,sp_dtr_ab_switch;
    CheckBox ch_structure_defects,ch_fencing_required,ch_dtr_ab_switch,ch_hg_fuse_set;
    CheckBox ch_earth_pits_damaged,ch_no_of_lt_bushings_damaged,ch_no_of_ht_bushings_damaged,ch_pg_clamp_lugs;
    Spinner sp_earth_pits_damaged,sp_no_of_lt_bushings_damaged,sp_no_of_ht_bushings_damaged;


    CheckBox ch_xlpe_cable,ch_lightening_aresters,ch_oil_leakage,ch_other_defects;
    Spinner sp_pg_clamp_lugs,sp_lightening_aresters;
    EditText ed_other_defects;
    String defect_selected;
    CheckBox distribution_board_damaged,lg_hg_fuse_set_damaged,distribution_box_damaged;
    List<String> earth_pits_list;
    String LT_FUSE_DEFECTS="";

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

    public static byte[] image_1_base64,image_2_base64,image_3_base64;
    public static boolean image_1_clicked = false;
    public static boolean image_2_clicked = false;
    public static boolean image_3_clicked = false;

    String type;

    //REMARKS
    EditText ed_remarks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pmidtrsurvey);
        type = getIntent().getStringExtra("type");
        ed_remarks = findViewById(R.id.ed_remarks);

        //IMAGES
        images_layout = findViewById(R.id.images_layout);
        image_1 = findViewById(R.id.image_1);
        image_2 = findViewById(R.id.image_2);
        image_3 = findViewById(R.id.image_3);
        upload_image_1 = findViewById(R.id.upload_image_1);
        upload_image_2 = findViewById(R.id.upload_image_2);
        upload_image_3 = findViewById(R.id.upload_image_3);


        tv_dtrstrcuture = findViewById(R.id.tv_dtrstrcuture);
        aut_dtrmake = findViewById(R.id.aut_dtrmake);
        edt_dtrserialnum = findViewById(R.id.edt_dtrserialnum);
        sp_dtrload = findViewById(R.id.sp_dtrload);
        sp_dtrphase = findViewById(R.id.sp_dtrphase);
        sp_dtrcapacity = findViewById(R.id.sp_dtrcapacity);
        sp_dtrstr_type = findViewById(R.id.sp_dtrstr_type);
        sp_dtrs_pole_support = findViewById(R.id.sp_dtrs_pole_support);
        edt_noof_sturts = findViewById(R.id.edt_noof_sturts);
        edt_noof_stay = findViewById(R.id.edt_noof_stay);
        ed_pole_no = findViewById(R.id.ed_pole_no);
        ed_pole_no.setEnabled(false);
        ed_frompole = findViewById(R.id.ed_frompole);
        ed_frompole.setEnabled(false);
        sp_dtrabswt = findViewById(R.id.sp_dtrabswt);
        sp_dtrabswt_type = findViewById(R.id.sp_dtrabswt_type);
        sp_dtrgh_fuseset = findViewById(R.id.sp_dtrgh_fuseset);
        sp_earthpits = findViewById(R.id.sp_earthpits);
        sp_cipipes = findViewById(R.id.sp_cipipes);
        sp_gipipes = findViewById(R.id.sp_gipipes);
        sp_dtrmeter = findViewById(R.id.sp_dtrmeter);
        sp_noof_dtrmeters = findViewById(R.id.sp_noof_dtrmeters);
        edt_meternum = findViewById(R.id.edt_meternum);
        edt_metermake = findViewById(R.id.edt_metermake);
        edt_metercapacity = findViewById(R.id.edt_metercapacity);
        edt_meternum_two = findViewById(R.id.edt_meternum_two);
        edt_metermake_two = findViewById(R.id.edt_metermake_two);
        edt_metercapacity_two = findViewById(R.id.edt_metercapacity_two);
        edt_meternum_three = findViewById(R.id.edt_meternum_three);
        edt_metermake_three = findViewById(R.id.edt_metermake_three);
        edt_metercapacity_three = findViewById(R.id.edt_metercapacity_three);
        sp_metertype = findViewById(R.id.sp_metertype);
        sp_metertype_two = findViewById(R.id.sp_metertype_two);
        sp_metertype_three = findViewById(R.id.sp_metertype_three);
        sp_modem_available = findViewById(R.id.sp_modem_available);
        edt_modemserial_no = findViewById(R.id.edt_modemserial_no);
        edt_modem_make = findViewById(R.id.edt_modem_make);
        sp_busbar_type = findViewById(R.id.sp_busbar_type);
        sp_consuctorsize = findViewById(R.id.sp_consuctorsize);
        sp_hgfuseto_dtr = findViewById(R.id.sp_hgfuseto_dtr);
        sp_hgfuseto_dtr_consuctorsize = findViewById(R.id.sp_hgfuseto_dtr_consuctorsize);
        sp_dtrpole = findViewById(R.id.sp_dtrpole);
        sp_pin = findViewById(R.id.sp_pin);
        sp_noof_pininsulators = findViewById(R.id.sp_noof_pininsulators);
        sp_cut = findViewById(R.id.sp_cut);
        edt_noof_cpin_insulators = findViewById(R.id.edt_noof_cpin_insulators);
        edt_noof_disc_insulators = findViewById(R.id.edt_noof_disc_insulators);
        edt_landmark = findViewById(R.id.edt_landmark);
        sp_lt_fuseset = findViewById(R.id.sp_lt_fuseset);
        sp_lt_busbar = findViewById(R.id.sp_lt_busbar);
        sp_lt_busbarcable_size = findViewById(R.id.sp_lt_busbarcable_size);
        sp_lt_busbarconductor_size = findViewById(R.id.sp_lt_busbarconductor_size);
        sp_earthpits_type = findViewById(R.id.sp_earthpits_type);

        sp_modemtwo_available = findViewById(R.id.sp_modemtwo_available);
        edt_modemtwo_serial_no = findViewById(R.id.edt_modemtwo_serial_no);
        edt_modemtwo_make = findViewById(R.id.edt_modemtwo_make);
        sp_modemthree_available = findViewById(R.id.sp_modemthree_available);
        edt_modemthree_serial_no = findViewById(R.id.edt_modemthree_serial_no);
        edt_modemthree_make = findViewById(R.id.edt_modemthree_make);

        sp_noof_ltcircuits = findViewById(R.id.sp_noof_ltcircuits);

        lyt_noof_sturts = findViewById(R.id.lyt_noof_sturts);
        lyt_noof_sturts.setVisibility(View.GONE);
        lyt_noof_stay = findViewById(R.id.lyt_noof_stay);
        lyt_noof_stay.setVisibility(View.GONE);
        lyt_dtrmeter = findViewById(R.id.lyt_dtrmeter);
        lyt_dtrmeter.setVisibility(View.GONE);
        lyt_modem = findViewById(R.id.lyt_modem);
        lyt_modem.setVisibility(View.GONE);
        lyt_busbar_condoctorsize = findViewById(R.id.lyt_busbar_condoctorsize);
        lyt_busbar_condoctorsize.setVisibility(View.GONE);
        lyt_dtrpolepin = findViewById(R.id.lyt_dtrpolepin);
        lyt_dtrpolepin.setVisibility(View.GONE);
        lyt_dtrpolecut = findViewById(R.id.lyt_dtrpolecut);
        lyt_dtrpolecut.setVisibility(View.GONE);
        lyt_nof_dtrmeters = findViewById(R.id.lyt_nof_dtrmeters);
        lyt_nof_dtrmeters.setVisibility(View.GONE);
        lyt_dtrmeter_two = findViewById(R.id.lyt_dtrmeter_two);
        lyt_dtrmeter_two.setVisibility(View.GONE);
        lyt_dtrmeter_three = findViewById(R.id.lyt_dtrmeter_three);
        lyt_dtrmeter_three.setVisibility(View.GONE);
        lyt_lt_busbarconductor_size = findViewById(R.id.lyt_lt_busbarconductor_size);
        lyt_lt_busbarconductor_size.setVisibility(View.GONE);
        lyt_busbarcable_size = findViewById(R.id.lyt_busbarcable_size);
        lyt_busbarcable_size.setVisibility(View.GONE);
        lyt_dtrhgfuse = findViewById(R.id.lyt_dtrhgfuse);
        lyt_dtrhgfuse.setVisibility(View.GONE);
        lyt_abswt_type = findViewById(R.id.lyt_abswt_type);
        lyt_abswt_type.setVisibility(View.GONE);
        lyt_gipipes = findViewById(R.id.lyt_gipipes);
        lyt_gipipes.setVisibility(View.GONE);
        lyt_cipipes = findViewById(R.id.lyt_cipipes);
        lyt_cipipes.setVisibility(View.GONE);
        lyt_modemtwo = findViewById(R.id.lyt_modemtwo);
        lyt_modemtwo.setVisibility(View.GONE);
        lyt_modem_three = findViewById(R.id.lyt_modem_three);
        lyt_modem_three.setVisibility(View.GONE);

        lyt_earthpitstype = findViewById(R.id.lyt_earthpitstype);
        lyt_earthpitstype.setVisibility(View.GONE);

        Intent intent = getIntent();

        if (intent != null) {
            dtrCode = intent.getStringExtra("DTRCODE");
            dtrlLocation= intent.getStringExtra("DTR_LOCATION");

        }


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

        btn_clear = findViewById(R.id.btn_clear);
        btn_add = findViewById(R.id.btn_add);
        btn_lttopclear = findViewById(R.id.btn_lttopclear);
        btn_clear.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        btn_lttopclear.setOnClickListener(this);

        arcGISDatabase = new PMIArcGISDatabase(PMIDTRSurveyActivity.this);
        dtrDetailsResponseVoList = new ArrayList<>();


        dtrMake = PMIDTRSurveyActivity.this.getResources().getStringArray(R.array.dtr_make);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (PMIDTRSurveyActivity.this, android.R.layout.select_dialog_item, dtrMake);

        aut_dtrmake.setThreshold(2);
        aut_dtrmake.setAdapter(adapter);

        tv_dtrstrcuture.setText(dtrCode+"-"+dtrlLocation);

        dtrDetailsResponseVoList = arcGISDatabase.getDTRDetailsData();






        if (type.equals("new")){

            aut_dtrmake.setText(not_updated.getDTR_MAKE());
            edt_dtrserialnum.setText(not_updated.getDTR_SL_NO());

            //DTR LOAD TYPE
            List<String> dtrloadList = new ArrayList<>();
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
            dtrloadList.remove(dtrDetailsResponseVoList.get(0).getTYPE_OF_LOAD());
            dtrloadList.add(0,dtrDetailsResponseVoList.get(0).getTYPE_OF_LOAD());
            ArrayAdapter<String> dtrloadListadapter = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, dtrloadList);
            sp_dtrload.setAdapter(dtrloadListadapter);

            //DTR PHASE
            List<String> dtrPhaseList = new ArrayList<>();
            if ("III".equalsIgnoreCase(dtrDetailsResponseVoList.get(0).getDTR_PHASE())) {
                dtrPhaseList.add(dtrDetailsResponseVoList.get(0).getDTR_PHASE());
                dtrPhaseList.add("I");
                ArrayAdapter<String> dtrPhaseListadapter = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, dtrPhaseList);
                sp_dtrphase.setAdapter(dtrPhaseListadapter);
            } else if ("I".equalsIgnoreCase(dtrDetailsResponseVoList.get(0).getDTR_PHASE())) {
                dtrPhaseList.add(dtrDetailsResponseVoList.get(0).getDTR_PHASE());
                dtrPhaseList.add("III");
                ArrayAdapter<String> dtrPhaseListadapter = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, dtrPhaseList);
                sp_dtrphase.setAdapter(dtrPhaseListadapter);
            }

         /*   //DTR CAPACITY
            String dtr_capacity = not_updated.getDTR_CAPACITY();
            List<String> list1_dtr_capacity = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.dtr_abswt)));
            list1_dtr_capacity.remove(dtr_capacity);
            list1_dtr_capacity.add(0,dtr_capacity);
            ArrayAdapter<String> adapter_dtr_capacity = new ArrayAdapter<String>(DTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list1_dtr_capacity);
            sp_dtrcapacity.setAdapter(adapter_dtr_capacity);
            sp_dtrcapacity.setSelection(0,true);*/


            //DTR STRUCTURE TYPE
            String s1 = not_updated.getDTR_STRUC_TYPE();
            List<String> list1 = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.dtr_str_type)));
            list1.remove(s1);
            list1.remove("Select");
            list1.add(0,s1);
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list1);
            sp_dtrstr_type.setAdapter(adapter1);
            sp_dtrstr_type.setSelection(0,true);

            //DTR POLE SUPPORT
            String s2 = not_updated.getDTR_POLE_SUPPORT();
            List<String> list2 = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.dtr_pole_support)));
            list2.remove(s2);
            list2.add(0,s2);
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list2);
            sp_dtrs_pole_support.setAdapter(adapter2);
            sp_dtrs_pole_support.setSelection(0,true);

            //DTR AB SWITCH
            String s3 = not_updated.getDTR_ABSWITCH();
            List<String> list3 = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.dtr_abswt)));
            list3.remove(s3);
            list3.add(0,s3);
            ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list3);
            sp_dtrabswt.setAdapter(adapter3);
            sp_dtrabswt.setSelection(0,true);

            //DTR POLE
            String dtr_pole = not_updated.getDTR_POLE();
            List<String> list_dtr_pole = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.dtr_pole)));
            list_dtr_pole.remove(dtr_pole);
            list_dtr_pole.add(0,dtr_pole);
            ArrayAdapter<String> adapter_dtr_pole = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list_dtr_pole);
            sp_dtrpole.setAdapter(adapter_dtr_pole);
            sp_dtrpole.setSelection(0,true);

            //NO OF LT CIRCUITS
            String no_of_lt_ckts = not_updated.getNO_OF_LTCKT();
            List<String> list_no_of_lt_ckts = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.noof_ltcircuits)));
            list_no_of_lt_ckts.remove(no_of_lt_ckts);
            list_no_of_lt_ckts.remove("Select");
            list_no_of_lt_ckts.add(0,no_of_lt_ckts);
            ArrayAdapter<String> adapter_no_of_lt_ckts = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list_no_of_lt_ckts);
            sp_noof_ltcircuits.setAdapter(adapter_no_of_lt_ckts);
            sp_noof_ltcircuits.setSelection(0,true);


        }

        if (type.equals("updated")){

            PMIUpdatedDTRVO dtrvo = PMIDTRSelectActivity.dtrvo;

            aut_dtrmake.setText(dtrvo.getDTR_MAKE());
            edt_dtrserialnum.setText(dtrvo.getDTR_SL_NO());

            //DTR LOAD TYPE
            String dtr_load_type = dtrvo.getDTR_LOAD_TYPE();
            List<String> dtrloadList = new ArrayList<>();
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
            dtrloadList.remove(dtr_load_type);
            dtrloadList.add(0,dtr_load_type);
            ArrayAdapter<String> dtrloadListadapter = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, dtrloadList);
            sp_dtrload.setAdapter(dtrloadListadapter);
            sp_dtrstr_type.setSelection(0,true);

            //DTR PHASE
            List<String> dtrPhaseList = new ArrayList<>();
            if ("III".equalsIgnoreCase(dtrvo.getDTR_PHASE())) {
                dtrPhaseList.add(dtrvo.getDTR_PHASE());
                dtrPhaseList.add("I");
                ArrayAdapter<String> dtrPhaseListadapter = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, dtrPhaseList);
                sp_dtrphase.setAdapter(dtrPhaseListadapter);
            } else if ("I".equalsIgnoreCase(dtrvo.getDTR_PHASE())) {
                dtrPhaseList.add(dtrvo.getDTR_PHASE());
                dtrPhaseList.add("III");
                ArrayAdapter<String> dtrPhaseListadapter = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, dtrPhaseList);
                sp_dtrphase.setAdapter(dtrPhaseListadapter);
            }

          /*  //DTR CAPACITY
            String dtr_capacity = dtrvo.getDTR_CAPACITY();
            List<String> list1_dtr_capacity = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.dtr_abswt)));
            list1_dtr_capacity.remove(dtr_capacity);
            list1_dtr_capacity.add(0,dtr_capacity);
            ArrayAdapter<String> adapter_dtr_capacity = new ArrayAdapter<String>(DTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list1_dtr_capacity);
            sp_dtrcapacity.setAdapter(adapter_dtr_capacity);
            sp_dtrcapacity.setSelection(0,true);*/

            //DTR STRUCTURE TYPE
            String s1 = dtrvo.getDTR_STRUC_TYPE();
            List<String> list1 = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.dtr_str_type)));
            list1.remove(s1);
            list1.remove("Select");
            list1.add(0,s1);
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list1);
            sp_dtrstr_type.setAdapter(adapter1);
            sp_dtrstr_type.setSelection(0,true);


            //DTR POLE SUPPORT
            String s2 = dtrvo.getDTR_POLE_SUPPORT();
            List<String> list2 = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.dtr_pole_support)));
            list2.remove(s2);
            list2.add(0,s2);
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list2);
            sp_dtrs_pole_support.setAdapter(adapter2);
            sp_dtrs_pole_support.setSelection(0,true);

            edt_noof_sturts.setText(dtrvo.getNOOF_STURT());
            edt_noof_stay.setText(dtrvo.getNOOF_STAY());

            //DTR AB SWITCH
            String s3 = dtrvo.getDTR_ABSWITCH();
            List<String> list3 = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.dtr_abswt)));
            list3.remove(s3);
            list3.add(0,s3);
            ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list3);
            sp_dtrabswt.setAdapter(adapter3);
            sp_dtrabswt.setSelection(0,true);

            if (s3.equalsIgnoreCase("YES")){
                //DTR AB SWITCH TYPE
                String s4 = dtrvo.getDTR_ABSWTICH_TYPE();
                List<String> list4 = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.dtr_abswt_type)));
                list4.remove(s4);
                list4.add(0,s4);
                ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list4);
                sp_dtrabswt_type.setAdapter(adapter4);
                sp_dtrabswt_type.setSelection(0,true);
            }

            //DTR HG FUSE SET
            String s5 = dtrvo.getDTR_HGFUSE();
            List<String> list5 = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.dtr_ghfuse)));
            list5.remove(s5);
            list5.add(0,s5);
            ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list5);
            sp_dtrgh_fuseset.setAdapter(adapter5);
            sp_dtrgh_fuseset.setSelection(0,true);

            if (s5.equalsIgnoreCase("YES")){
                //DTR HG FUSE SET BUS BAR TYPE
                String s6 = dtrvo.getABSWITCH_TO_HGFUSE_BUSBAR();
                List<String> list6 = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.dtr_busbar)));
                list6.remove(s6);
                list6.add(0,s6);
                ArrayAdapter<String> adapter6 = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list6);
                sp_busbar_type.setAdapter(adapter6);
                sp_busbar_type.setSelection(0,true);


                //DTR HG FUSE SET CONDUCTOR SIZE
                String s7 = dtrvo.getABS_COUNDUCTOR_SIZE();
                List<String> list7 = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.busbar_conductorsize)));
                list7.remove(s7);
                list7.add(0,s7);
                ArrayAdapter<String> adapter7 = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list7);
                sp_consuctorsize.setAdapter(adapter7);
                sp_consuctorsize.setSelection(0,true);


                //DTR HG FUSE SET TO DTR
                String s8 = dtrvo.getHGFUSE_TO_DTR_BUSBAR();
                List<String> list8 = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.hg_fuseto_dtr)));
                list8.remove(s8);
                list8.add(0,s8);
                ArrayAdapter<String> adapter8 = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list8);
                sp_hgfuseto_dtr.setAdapter(adapter8);
                sp_hgfuseto_dtr.setSelection(0,true);


                //CONDUCTOR SIZE
                String s9 = dtrvo.getHGF_COUNDUCTOR_SIZE();
                List<String> list9 = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.busbar_conductorsize)));
                list9.remove(s9);
                list9.add(0,s9);
                ArrayAdapter<String> adapter9 = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list9);
                sp_hgfuseto_dtr_consuctorsize.setAdapter(adapter9);
                sp_hgfuseto_dtr_consuctorsize.setSelection(0,true);


            }

            //EARTH PITS
            String s10 = dtrvo.getEARTH_PITS();
            List<String> list10 = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.dtr_earthpits)));
            list10.remove(s10);
            list10.remove("Select");
            list10.add(0,s10);
            ArrayAdapter<String> adapter10 = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list10);
            sp_earthpits.setAdapter(adapter10);
            sp_earthpits.setSelection(0,true);

            //EARTH PITS TYPE
            String ci_p = dtrvo.getCI_PIPES();
            String gi_p = dtrvo.getGI_PIPES();

            if (!ci_p.equals("") && !gi_p.equals("")){
                List<String> list11 = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.dtr_earthpitstype)));
                ArrayAdapter<String> adapter6 = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list11);
                sp_earthpits_type.setAdapter(adapter6);
                sp_earthpits_type.setSelection(2,true);

                List<String> list_ci_p = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.dtr_cipipes)));
                list_ci_p.remove(ci_p);
                list_ci_p.add(0,ci_p);
                ArrayAdapter<String> adapter_ci_p = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list_ci_p);
                sp_cipipes.setAdapter(adapter_ci_p);
                sp_cipipes.setSelection(0,true);

                List<String> list_gi_p = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.dtr_gipipes)));
                list_gi_p.remove(gi_p);
                list_gi_p.add(0,gi_p);
                ArrayAdapter<String> adapter_gi_p = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list_gi_p);
                sp_gipipes.setAdapter(adapter_gi_p);
                sp_gipipes.setSelection(0,true);

            } else if (!ci_p.equals("")){
                List<String> list_ci_p = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.dtr_cipipes)));
                list_ci_p.remove(ci_p);
                list_ci_p.add(0,ci_p);
                ArrayAdapter<String> adapter_ci_p = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list_ci_p);
                sp_cipipes.setAdapter(adapter_ci_p);
                sp_cipipes.setSelection(0,true);
            } else if (!gi_p.equals("")){
                List<String> list_gi_p = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.dtr_gipipes)));
                list_gi_p.remove(gi_p);
                list_gi_p.add(0,gi_p);
                ArrayAdapter<String> adapter_gi_p = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list_gi_p);
                sp_gipipes.setAdapter(adapter_gi_p);
                sp_gipipes.setSelection(0,true);
            }

          /*  if (!s10.equalsIgnoreCase("0")){
                String n_ci_pipes = dtrvo.getCI_PIPES();
                String n_gi_pipes = dtrvo.getGI_PIPES();
                if (!n_ci_pipes.equalsIgnoreCase("")){
                    List<String> list11 = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.dtr_earthpits)));
                    list11.remove(s10);
                    list11.add(0,s10);
                    ArrayAdapter<String> adapter6 = new ArrayAdapter<String>(DTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list11);
                    sp_earthpits.setAdapter(adapter6);
                    sp_earthpits.setSelection(0,true);
                }
            }*/

            //DTR METER
            String s11 = dtrvo.getNO_OF_DTR_METERS();
            if (!s11.equalsIgnoreCase("")){
                List<String> list11 = new ArrayList<>();
                list11.add("YES");
                list11.add("NO");
                ArrayAdapter<String> adapter11 = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list11);
                sp_dtrmeter.setAdapter(adapter11);
                sp_dtrmeter.setSelection(0,true);

                //NO OF DTR METERS
                List<String> list_no_of_mtr = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.noofdtr_meters)));
                list_no_of_mtr.remove(s11);
                list_no_of_mtr.add(0,s11);
                ArrayAdapter<String> adapter_no_of_mtr = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list_no_of_mtr);
                sp_noof_dtrmeters.setAdapter(adapter_no_of_mtr);
                sp_noof_dtrmeters.setSelection(0,true);

                if (s11.equalsIgnoreCase("1")){
                    edt_meternum.setText(dtrvo.getMTR1_SL_NO());
                    edt_metermake.setText(dtrvo.getMTR1_MAKE());
                    edt_metercapacity.setText(dtrvo.getMTR1_CAPACITY());

                    //METER 1 TYPE
                    String m1t = dtrvo.getMTR1_TYPE();
                    List<String> listm1t = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.meter_type)));
                    listm1t.remove(m1t);
                    listm1t.add(0,m1t);
                    ArrayAdapter<String> adapter_m1t = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, listm1t);
                    sp_metertype.setAdapter(adapter_m1t);
                    sp_metertype.setSelection(0,true);

                    //METER 1 MODEM AVAILABLE
                    String modem1_avl = dtrvo.getMODEM1_AVL();
                    List<String> list_modem1_avl = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.dtr_modem)));
                    list_modem1_avl.remove(modem1_avl);
                    list_modem1_avl.add(0,modem1_avl);
                    ArrayAdapter<String> adapter_modem1_avl = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list_modem1_avl);
                    sp_modem_available.setAdapter(adapter_modem1_avl);
                    sp_modem_available.setSelection(0,true);

                    if (modem1_avl.equalsIgnoreCase("YES")){
                        edt_modemserial_no.setText(dtrvo.getMODEM1_SLNO());
                        edt_modem_make.setText(dtrvo.getMODEM1_MAKE());
                    }
                }

                if (s11.equalsIgnoreCase("2")){
                    edt_meternum.setText(dtrvo.getMTR1_SL_NO());
                    edt_metermake.setText(dtrvo.getMTR1_MAKE());
                    edt_metercapacity.setText(dtrvo.getMTR1_CAPACITY());

                    //METER 1 TYPE
                    String m1t = dtrvo.getMTR1_TYPE();
                    List<String> listm1t = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.meter_type)));
                    listm1t.remove(m1t);
                    listm1t.add(0,m1t);
                    ArrayAdapter<String> adapter_m1t = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, listm1t);
                    sp_metertype.setAdapter(adapter_m1t);
                    sp_metertype.setSelection(0,true);

                    //METER 1 MODEM AVAILABLE
                    String modem1_avl = dtrvo.getMODEM1_AVL();
                    List<String> list_modem1_avl = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.dtr_modem)));
                    list_modem1_avl.remove(modem1_avl);
                    list_modem1_avl.add(0,modem1_avl);
                    ArrayAdapter<String> adapter_modem1_avl = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list_modem1_avl);
                    sp_modem_available.setAdapter(adapter_modem1_avl);
                    sp_modem_available.setSelection(0,true);

                    if (modem1_avl.equalsIgnoreCase("YES")){
                        edt_modemserial_no.setText(dtrvo.getMODEM1_SLNO());
                        edt_modem_make.setText(dtrvo.getMODEM1_MAKE());
                    }


                    //DTR METER 2
                    edt_meternum_two.setText(dtrvo.getMTR2_SL_NO());
                    edt_metermake_two.setText(dtrvo.getMTR2_MAKE());
                    edt_metercapacity_two.setText(dtrvo.getMTR2_CAPACITY());

                    //METER 2 TYPE
                    String m2t = dtrvo.getMTR2_TYPE();
                    List<String> listm2t = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.meter_type)));
                    listm2t.remove(m2t);
                    listm2t.add(0,m2t);
                    ArrayAdapter<String> adapter_m2t = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, listm2t);
                    sp_metertype_two.setAdapter(adapter_m2t);
                    sp_metertype_two.setSelection(0,true);

                    //METER 2 MODEM AVAILABLE
                    String modem2_avl = dtrvo.getMODEM2_AVL();
                    List<String> list_modem2_avl = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.dtr_modem)));
                    list_modem2_avl.remove(modem2_avl);
                    list_modem2_avl.add(0,modem2_avl);
                    ArrayAdapter<String> adapter_modem2_avl = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list_modem2_avl);
                    sp_modemtwo_available.setAdapter(adapter_modem2_avl);
                    sp_modemtwo_available.setSelection(0,true);

                    // System.out.println("MODEM 2 AV : "+dtrvo.getMODEM2_AVL());
                    // System.out.println("MODEM 2 MAKE : "+dtrvo.getMODEM2_MAKE());

                    if (modem2_avl.equalsIgnoreCase("YES")){
                        edt_modemtwo_serial_no.setText(dtrvo.getMODEM2_SLNO());
                        Toast.makeText(this, "MODEM 2 "+dtrvo.getMODEM2_MAKE(), Toast.LENGTH_SHORT).show();
                        edt_modemtwo_make.setText(dtrvo.getMODEM2_MAKE());
                    }
                }

                if (s11.equalsIgnoreCase("3")){
                    edt_meternum.setText(dtrvo.getMTR1_SL_NO());
                    edt_metermake.setText(dtrvo.getMTR1_MAKE());
                    edt_metercapacity.setText(dtrvo.getMTR1_CAPACITY());

                    //METER 1 TYPE
                    String m1t = dtrvo.getMTR1_TYPE();
                    List<String> listm1t = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.meter_type)));
                    listm1t.remove(m1t);
                    listm1t.add(0,m1t);
                    ArrayAdapter<String> adapter_m1t = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, listm1t);
                    sp_metertype.setAdapter(adapter_m1t);
                    sp_metertype.setSelection(0,true);

                    //METER 1 MODEM AVAILABLE
                    String modem1_avl = dtrvo.getMODEM1_AVL();
                    List<String> list_modem1_avl = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.dtr_modem)));
                    list_modem1_avl.remove(modem1_avl);
                    list_modem1_avl.add(0,modem1_avl);
                    ArrayAdapter<String> adapter_modem1_avl = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list_modem1_avl);
                    sp_modem_available.setAdapter(adapter_modem1_avl);
                    sp_modem_available.setSelection(0,true);

                    if (modem1_avl.equalsIgnoreCase("YES")){
                        edt_modemserial_no.setText(dtrvo.getMODEM1_SLNO());
                        edt_modem_make.setText(dtrvo.getMODEM1_MAKE());
                    }


                    //DTR METER 2
                    edt_meternum_two.setText(dtrvo.getMTR2_SL_NO());
                    edt_metermake_two.setText(dtrvo.getMTR2_MAKE());
                    edt_metercapacity_two.setText(dtrvo.getMTR2_CAPACITY());

                    //METER 2 TYPE
                    String m2t = dtrvo.getMTR2_TYPE();
                    List<String> listm2t = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.meter_type)));
                    listm2t.remove(m2t);
                    listm2t.add(0,m2t);
                    ArrayAdapter<String> adapter_m2t = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, listm2t);
                    sp_metertype_two.setAdapter(adapter_m2t);
                    sp_metertype_two.setSelection(0,true);

                    //METER 2 MODEM AVAILABLE
                    String modem2_avl = dtrvo.getMODEM2_AVL();
                    List<String> list_modem2_avl = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.dtr_modem)));
                    list_modem2_avl.remove(modem2_avl);
                    list_modem2_avl.add(0,modem2_avl);
                    ArrayAdapter<String> adapter_modem2_avl = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list_modem2_avl);
                    sp_modemtwo_available.setAdapter(adapter_modem2_avl);
                    sp_modemtwo_available.setSelection(0,true);

                    // System.out.println("MODEM 2 AV : "+dtrvo.getMODEM2_AVL());
                    // System.out.println("MODEM 2 MAKE : "+dtrvo.getMODEM2_MAKE());


                    if (modem2_avl.equalsIgnoreCase("YES")){
                        edt_modemtwo_serial_no.setText(dtrvo.getMODEM2_SLNO());
                        edt_modemtwo_make.setText(dtrvo.getMODEM2_MAKE());
                    }


                    //DTR METER 3
                    edt_meternum_three.setText(dtrvo.getMTR3_SL_NO());
                    edt_metermake_three.setText(dtrvo.getMTR3_MAKE());
                    edt_metercapacity_three.setText(dtrvo.getMTR3_CAPACITY());

                    //METER 3 TYPE
                    String m3t = dtrvo.getMTR3_TYPE();
                    List<String> listm3t = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.meter_type)));
                    listm3t.remove(m3t);
                    listm3t.add(0,m3t);
                    ArrayAdapter<String> adapter_m3t = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, listm3t);
                    sp_metertype_three.setAdapter(adapter_m3t);
                    sp_metertype_three.setSelection(0,true);

                    //METER 3 MODEM AVAILABLE
                    String modem3_avl = dtrvo.getMODEM3_AVL();
                    List<String> list_modem3_avl = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.dtr_modem)));
                    list_modem3_avl.remove(modem3_avl);
                    list_modem3_avl.add(0,modem3_avl);
                    ArrayAdapter<String> adapter_modem3_avl = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list_modem3_avl);
                    sp_modemthree_available.setAdapter(adapter_modem3_avl);
                    sp_modemthree_available.setSelection(0,true);

                    if (modem3_avl.equalsIgnoreCase("YES")){
                        edt_modemthree_serial_no.setText(dtrvo.getMODEM3_SLNO());
                        edt_modemthree_make.setText(dtrvo.getMODEM3_MAKE());
                    }
                }

            }

            //LT FUSE SET
            String lt_fuse_set = dtrvo.getLT_FUSE();
            List<String> list_lt_fuse_set = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.lt_fuseset)));
            list_lt_fuse_set.remove(lt_fuse_set);
            list_lt_fuse_set.add(0,lt_fuse_set);
            ArrayAdapter<String> adapter_lt_fuse_set = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list_lt_fuse_set);
            sp_lt_fuseset.setAdapter(adapter_lt_fuse_set);
            sp_lt_fuseset.setSelection(0,true);

            //LT BUS BAR
            String lt_bus_bar = dtrvo.getLT_BUS_BAR();
            List<String> list_lt_bus_bar = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.lt_busbar)));
            list_lt_bus_bar.remove(lt_bus_bar);
            list_lt_bus_bar.add(0,lt_bus_bar);
            ArrayAdapter<String> adapter_lt_bus_bar = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list_lt_bus_bar);
            sp_lt_busbar.setAdapter(adapter_lt_bus_bar);
            sp_lt_busbar.setSelection(0,true);

            if (lt_bus_bar.equalsIgnoreCase("CABLE")){
                //LT BUS BAR CABLE SIZE
                String lt_bus_bar_cable_size = dtrvo.getLT_BUSBAR_CABLESIZE();
                List<String> list_lt_bus_bar_cable_size = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.lt_busbar_cablesize)));
                list_lt_bus_bar_cable_size.remove(lt_bus_bar_cable_size);
                list_lt_bus_bar_cable_size.add(0,lt_bus_bar_cable_size);
                ArrayAdapter<String> adapter_lt_bus_bar_cable_size = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list_lt_bus_bar_cable_size);
                sp_lt_busbarcable_size.setAdapter(adapter_lt_bus_bar_cable_size);
                sp_lt_busbarcable_size.setSelection(0,true);
            }

            if (lt_bus_bar.equalsIgnoreCase("CONDUCTOR")){
                //LT BUS BAR CONDUCTOR SIZE
                String lt_bus_bar_conductor_size = dtrvo.getLT_BUSBAR_CONDUTORSIZE();
                List<String> list_lt_bus_bar_conductor_size = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.lt_busbar_conductorsize)));
                list_lt_bus_bar_conductor_size.remove(lt_bus_bar_conductor_size);
                list_lt_bus_bar_conductor_size.add(0,lt_bus_bar_conductor_size);
                ArrayAdapter<String> adapter_lt_bus_bar_conductor_size = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list_lt_bus_bar_conductor_size);
                sp_lt_busbarconductor_size.setAdapter(adapter_lt_bus_bar_conductor_size);
                sp_lt_busbarconductor_size.setSelection(0,true);
            }

            //DTR POLE
            String dtr_pole = dtrvo.getDTR_POLE();
            List<String> list_dtr_pole = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.dtr_pole)));
            list_dtr_pole.remove(dtr_pole);
            list_dtr_pole.add(0,dtr_pole);
            ArrayAdapter<String> adapter_dtr_pole = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list_dtr_pole);
            sp_dtrpole.setAdapter(adapter_dtr_pole);
            sp_dtrpole.setSelection(0,true);

            if (dtr_pole.equalsIgnoreCase("PIN")){
                //PIN
                String pin = dtrvo.getPIN();
                List<String> list_pin = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.pin)));
                list_pin.remove(pin);
                list_pin.add(0,pin);
                ArrayAdapter<String> adapter_pin = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list_pin);
                sp_pin.setAdapter(adapter_pin);
                sp_pin.setSelection(0,true);

                //NO OF PIN INSULATORS
                String no_pin = dtrvo.getNOOF_PININSULATORS();
                List<String> list_no_pin = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.no_of_pin)));
                list_no_pin.remove(no_pin);
                list_no_pin.add(0,no_pin);
                ArrayAdapter<String> adapter_no_pin = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list_no_pin);
                sp_noof_pininsulators.setAdapter(adapter_no_pin);
                sp_noof_pininsulators.setSelection(0,true);
            }

            if (dtr_pole.equalsIgnoreCase("CUT")){
                //CUT
                String pin = dtrvo.getCUT();
                List<String> list_pin = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.cut)));
                list_pin.remove(pin);
                list_pin.add(0,pin);
                ArrayAdapter<String> adapter_pin = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list_pin);
                sp_cut.setAdapter(adapter_pin);
                sp_cut.setSelection(0,true);

                //NO OF CUT INSULATORS
                edt_noof_cpin_insulators.setText(dtrvo.getNOOF_PININSULATORS());
                edt_noof_disc_insulators.setText(dtrvo.getNOOF_DISCINSULATORS());
            }

            if (!dtrvo.getDTR_LANDMARK().equals("")){
                edt_landmark.setText(dtrvo.getDTR_LANDMARK());
            }

            //NO OF LT CIRCUITS
            String no_of_lt_ckts = dtrvo.getNO_OF_LTCKT();
            List<String> list_no_of_lt_ckts = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.noof_ltcircuits)));
            list_no_of_lt_ckts.remove(no_of_lt_ckts);
            list_no_of_lt_ckts.remove("Select");
            list_no_of_lt_ckts.add(0,no_of_lt_ckts);
            ArrayAdapter<String> adapter_no_of_lt_ckts = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list_no_of_lt_ckts);
            sp_noof_ltcircuits.setAdapter(adapter_no_of_lt_ckts);
            sp_noof_ltcircuits.setSelection(0,true);

        }


        surveyVosList = arcGISDatabase.getSurveyData("");
        int count = arcGISDatabase.getObjectId();
        poleCountCheck = arcGISDatabase.getObjectIdCount();
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

        //NEW DTR DEFECTS
        sp_no_of_lt_bushings_damaged=findViewById(R.id.sp_no_of_lt_bushings_damaged);
        ch_no_of_ht_bushings_damaged=findViewById(R.id.ch_no_of_ht_bushings_damaged);
        sp_no_of_ht_bushings_damaged=findViewById(R.id.sp_no_of_ht_bushings_damaged);
        ch_pg_clamp_lugs=findViewById(R.id.ch_pg_clamp_lugs);
        ch_xlpe_cable=findViewById(R.id.ch_xlpe_cable);
        sp_pg_clamp_lugs=findViewById(R.id.sp_pg_clamp_lugs);
        ch_lightening_aresters=findViewById(R.id.ch_lightening_aresters);
        sp_lightening_aresters=findViewById(R.id.sp_lightening_aresters);
        ch_oil_leakage=findViewById(R.id.ch_oil_leakage);
        ch_other_defects=findViewById(R.id.ch_other_defects);
        ed_other_defects=findViewById(R.id.ed_other_defects);
        ch_structure_defects=findViewById(R.id.ch_structure_defects);
        sp_dtr_defects=findViewById(R.id.sp_dtr_defects);
        sp_structure_defects=findViewById(R.id.sp_structure_defects);
        ch_fencing_required=findViewById(R.id.ch_fencing_required);
        ch_dtr_ab_switch=findViewById(R.id.ch_dtr_ab_switch);
        sp_dtr_ab_switch=findViewById(R.id.sp_dtr_ab_switch);
        ch_hg_fuse_set=findViewById(R.id.ch_hg_fuse_set);
        ch_earth_pits_damaged=findViewById(R.id.ch_earth_pits_damaged);
        sp_earth_pits_damaged=findViewById(R.id.sp_earth_pits_damaged);
        ch_no_of_lt_bushings_damaged=findViewById(R.id.ch_no_of_lt_bushings_damaged);
        defects_layout=findViewById(R.id.defects_layout);
        distribution_board_damaged=findViewById(R.id.distribution_board_damaged);
        lg_hg_fuse_set_damaged=findViewById(R.id.lg_hg_fuse_set_damaged);
        distribution_box_damaged=findViewById(R.id.distribution_box_damaged);


        defect_selected = sp_dtr_defects.getSelectedItem().toString();
        //  System.out.println("SELECTED DEFECTS : "+defect_selected);


        sp_dtr_defects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                defect_selected = sp_dtr_defects.getSelectedItem().toString();
                //  System.out.println("SELECTED DEFECTS INSIDE : "+defect_selected);
                if (defect_selected.equalsIgnoreCase("Yes")){
                    defects_layout.setVisibility(View.VISIBLE);
                    // System.out.println("DTR STRUCTURE TYPE : "+dtrstr_type);
                    if (dtrstr_type.equalsIgnoreCase("PLINTH")){
                        ch_structure_defects.setVisibility(View.VISIBLE);
                    }else {
                        ch_structure_defects.setVisibility(View.VISIBLE);
                    }
                }else {
                    defects_layout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ch_structure_defects.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    sp_structure_defects.setVisibility(View.VISIBLE);

                }else {
                    sp_structure_defects.setVisibility(View.GONE);
                }
            }
        });

        ch_dtr_ab_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    sp_dtr_ab_switch.setVisibility(View.VISIBLE);
                }else {
                    sp_dtr_ab_switch.setVisibility(View.GONE);
                }
            }
        });

        ch_earth_pits_damaged.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    sp_earth_pits_damaged.setVisibility(View.VISIBLE);

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, earth_pits_list);
                    sp_earth_pits_damaged.setAdapter(adapter);
                }else {
                    sp_earth_pits_damaged.setVisibility(View.GONE);
                }
            }
        });

        sp_earth_pits_damaged.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ch_no_of_lt_bushings_damaged.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    sp_no_of_lt_bushings_damaged.setVisibility(View.VISIBLE);

                }else {
                    sp_no_of_lt_bushings_damaged.setVisibility(View.GONE);
                }
            }
        });

        ch_no_of_ht_bushings_damaged.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    sp_no_of_ht_bushings_damaged.setVisibility(View.VISIBLE);

                }else {
                    sp_no_of_ht_bushings_damaged.setVisibility(View.GONE);
                }
            }
        });

        ch_pg_clamp_lugs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    sp_pg_clamp_lugs.setVisibility(View.VISIBLE);

                }else {
                    sp_pg_clamp_lugs.setVisibility(View.GONE);
                }
            }
        });

        ch_lightening_aresters.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    sp_lightening_aresters.setVisibility(View.VISIBLE);

                }else {
                    sp_lightening_aresters.setVisibility(View.GONE);
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

        distribution_box_damaged.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    LT_FUSE_DEFECTS = "DISTRIBUTION BOX DAMAGED";
                }else {
                    LT_FUSE_DEFECTS = "";
                }
            }
        });

        distribution_board_damaged.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    LT_FUSE_DEFECTS = "DISTRIBUTION BOARD DAMAGED";
                }else {
                    LT_FUSE_DEFECTS = "";
                }
            }
        });

        lg_hg_fuse_set_damaged.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    LT_FUSE_DEFECTS = "LT FUSE SET DAMAGED";
                }else {
                    LT_FUSE_DEFECTS = "";
                }
            }
        });

        //IMAGES
        upload_image_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // openImageDialog(CAMERA_RESULT_CODE_IMAGE_1,GALLERY_RESULT_CODE_IMAGE_1);

                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, CAMERA_RESULT_CODE_IMAGE_1);
            }
        });

        upload_image_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // openImageDialog(CAMERA_RESULT_CODE_IMAGE_2,GALLERY_RESULT_CODE_IMAGE_2);
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, CAMERA_RESULT_CODE_IMAGE_2);
            }
        });

        upload_image_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openImageDialog(CAMERA_RESULT_CODE_IMAGE_3,GALLERY_RESULT_CODE_IMAGE_3);
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, CAMERA_RESULT_CODE_IMAGE_3);

            }
        });




      /*  }catch (Exception e){
            e.printStackTrace();
        }*/
    }


    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            //   System.out.println("ResultCode :" + resultCode);
            // System.out.println("RequestCode :" + requestCode);
            // System.out.println("Data  :" + data);

            if (requestCode == CAMERA_RESULT_CODE_IMAGE_1 && resultCode == RESULT_OK && data != null) {
                Bitmap image_1_bitmap = (Bitmap) data.getExtras().get("data");
                image_1.setImageBitmap(image_1_bitmap);
                encodeBitmapForLT1(image_1_bitmap);
            }else if (requestCode == CAMERA_RESULT_CODE_IMAGE_2 && resultCode == RESULT_OK && data != null) {
                Bitmap image_2_bitmap = (Bitmap) data.getExtras().get("data");
                image_2.setImageBitmap(image_2_bitmap);
                encodeBitmapForLT2(image_2_bitmap);
            } else if (requestCode == CAMERA_RESULT_CODE_IMAGE_3 && resultCode == RESULT_OK && data != null) {
                Bitmap image_3_bitmap = (Bitmap) data.getExtras().get("data");
                image_3.setImageBitmap(image_3_bitmap);
                encodeBitmapForLT3(image_3_bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void encodeBitmapForLT1(Bitmap image_1_bitmap) {
        ByteArrayOutputStream byteArrayOutputStream1 = new ByteArrayOutputStream();
        image_1_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream1);

        byte[] byteofimages1 = byteArrayOutputStream1.toByteArray();
        // CustomAllDialogs.image_1_base64 = android.util.Base64.encodeToString(byteofimages1, Base64.DEFAULT);
        image_1_base64 = byteofimages1;
        image_1_clicked = true;
        // System.out.println("IMAGE 1 BITMAP : "+CustomAllDialogs.image_1_base64);
    }

    private void encodeBitmapForLT2(Bitmap image_2_bitmap) {
        ByteArrayOutputStream byteArrayOutputStream1 = new ByteArrayOutputStream();
        image_2_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream1);

        byte[] byteofimages1 = byteArrayOutputStream1.toByteArray();
        //  CustomAllDialogs.image_2_base64 = android.util.Base64.encodeToString(byteofimages1, Base64.DEFAULT);
        image_2_base64 = byteofimages1;
        image_2_clicked = true;
        //System.out.println("IMAGE 2 BITMAP : "+CustomAllDialogs.image_2_base64);
    }

    private void encodeBitmapForLT3(Bitmap image_3_bitmap) {
        ByteArrayOutputStream byteArrayOutputStream1 = new ByteArrayOutputStream();
        image_3_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream1);

        byte[] byteofimages1 = byteArrayOutputStream1.toByteArray();
        // CustomAllDialogs.image_3_base64 = android.util.Base64.encodeToString(byteofimages1, Base64.DEFAULT);
        image_3_base64 = byteofimages1;
        image_3_clicked = true;
        //System.out.println("IMAGE 3 BITMAP : "+CustomAllDialogs.image_3_base64);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_clear:

                Intent intent = new Intent(PMIDTRSurveyActivity.this, PMIHomeActivity.class);

                startActivity(intent);
                finish();

                break;

            case R.id.btn_add:
                // System.out.println("DEFECTS IN SAVE "+defect_selected);
                if (defect_selected.equalsIgnoreCase("Yes")){
                    validateDefects();
                }else{
                    validateDTRSurveyData();
                }


                break;
            case R.id.btn_lttopclear:


                break;

        }


    }

    private void validateDefects() {
        if (ch_structure_defects.isChecked()){
            if (sp_structure_defects.getSelectedItem().toString().equalsIgnoreCase("Select")){
                Toast.makeText(this, "Select Dtr Structure Damaged", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (ch_dtr_ab_switch.isChecked()){
            if (sp_dtr_ab_switch.getSelectedItem().toString().equalsIgnoreCase("Select")){
                Toast.makeText(this, "Select Dtr AB Switch", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (ch_no_of_lt_bushings_damaged.isChecked()){
            if (sp_no_of_lt_bushings_damaged.getSelectedItem().toString().equalsIgnoreCase("Select No of. LT Bushings Damaged")){
                Toast.makeText(this, "Select No of. LT Bushings Damaged", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (ch_no_of_ht_bushings_damaged.isChecked()){
            if (sp_no_of_ht_bushings_damaged.getSelectedItem().toString().equalsIgnoreCase("Select No of. HT Bushings Damaged")){
                Toast.makeText(this, "Select No of. HT Bushings Damaged", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (ch_pg_clamp_lugs.isChecked()){
            if (sp_pg_clamp_lugs.getSelectedItem().toString().equalsIgnoreCase("Select No of. damaged PG Clamps/Lugs")){
                Toast.makeText(this, "Select No of. damaged PG Clamps/Lugs", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (ch_lightening_aresters.isChecked()){
            if (sp_lightening_aresters.getSelectedItem().toString().equalsIgnoreCase("Select No of. Lightening Arrestors")){
                Toast.makeText(this, "Select No of. Damaged Lightening Arrestors", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (ch_other_defects.isChecked()){
            if (ed_other_defects.getText().toString().trim().isEmpty()){
                Toast.makeText(this,"Please Enter Other Defects",Toast.LENGTH_SHORT).show();
                return;
            }

        }
        // Toast.makeText(this,"Validation complete",Toast.LENGTH_SHORT).show();
        validateDTRSurveyData();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.sp_dtrabswt:
                dtrABswt = sp_dtrabswt.getSelectedItem().toString();
                if ("YES".equalsIgnoreCase(dtrABswt)) {
                    lyt_abswt_type.setVisibility(View.VISIBLE);
                    ch_dtr_ab_switch.setVisibility(View.VISIBLE);
                } else {
                    lyt_abswt_type.setVisibility(View.GONE);
                    ch_dtr_ab_switch.setVisibility(View.GONE);
                }
                break;
            case R.id.sp_dtrabswt_type:
                dtrabswt_type = sp_dtrabswt_type.getSelectedItem().toString();
                break;


            case R.id.sp_dtrgh_fuseset:
                dtrgh_fuseset = sp_dtrgh_fuseset.getSelectedItem().toString();
                if ("YES".equalsIgnoreCase(dtrgh_fuseset)) {
                    lyt_dtrhgfuse.setVisibility(View.VISIBLE);
                    ch_hg_fuse_set.setVisibility(View.VISIBLE);
                } else {
                    lyt_dtrhgfuse.setVisibility(View.GONE);
                    ch_hg_fuse_set.setVisibility(View.GONE);
                }

                break;
            case R.id.sp_earthpits:
                earthpits = sp_earthpits.getSelectedItem().toString();
                if (earthpits.equalsIgnoreCase("Select")){
                    lyt_earthpitstype.setVisibility(View.GONE);
                    lyt_cipipes.setVisibility(View.GONE);
                    lyt_gipipes.setVisibility(View.GONE);
                    ch_earth_pits_damaged.setVisibility(View.GONE);
                }else {
                    earthPitsNo = Integer.parseInt(earthpits);
                    if (earthPitsNo == 0) {
                        lyt_earthpitstype.setVisibility(View.GONE);
                        lyt_cipipes.setVisibility(View.GONE);
                        lyt_gipipes.setVisibility(View.GONE);
                        ch_earth_pits_damaged.setVisibility(View.GONE);
                    } else {
                        lyt_earthpitstype.setVisibility(View.VISIBLE);
                        ch_earth_pits_damaged.setVisibility(View.VISIBLE);
                        earth_pits_list= new ArrayList<>();

                        if (earthpits.equalsIgnoreCase("1")){
                            earth_pits_list.add("1");
                        }else if (earthpits.equalsIgnoreCase("2")){
                            earth_pits_list.add("1");
                            earth_pits_list.add("2");
                        }else if (earthpits.equalsIgnoreCase("3")){
                            earth_pits_list.add("1");
                            earth_pits_list.add("2");
                            earth_pits_list.add("3");
                        }
                    }
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
                earthpits_type = sp_earthpits_type.getSelectedItem().toString();
                if ("CI Pipes".equalsIgnoreCase(earthpits_type)) {
                    lyt_cipipes.setVisibility(View.VISIBLE);
                    lyt_gipipes.setVisibility(View.GONE);
                } else if ("GI Pipes".equalsIgnoreCase(earthpits_type)) {
                    lyt_gipipes.setVisibility(View.VISIBLE);
                    lyt_cipipes.setVisibility(View.GONE);
                } else if ("CI AND GI Pipes".equalsIgnoreCase(earthpits_type)) {
                    lyt_cipipes.setVisibility(View.VISIBLE);
                    lyt_gipipes.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.sp_cipipes:
                cipipes = sp_cipipes.getSelectedItem().toString();
                ciPipesCount = Integer.parseInt(cipipes);

                if ("CI Pipes".equalsIgnoreCase(earthpits_type)) {
                    if (ciPipesCount > earthPitsNo || ciPipesCount < earthPitsNo) {
                        Toast.makeText(PMIDTRSurveyActivity.this, "CI Pipes count should be equal to Earth Pits Count", Toast.LENGTH_SHORT).show();
                    }
                }


                /*if(!"Select".equalsIgnoreCase(sp_cipipes.getSelectedItem().toString())) {
                    ciPipesCount = Integer.parseInt(cipipes);
                    if (earthPitsNo == ciPipesCount) {
                        lyt_gipipes.setVisibility(View.GONE);
                    } else if (ciPipesCount > earthPitsNo) {
                        lyt_gipipes.setVisibility(View.GONE);
                        Toast.makeText(c, "CI Pipes count should not greater than Earth Pits Count", Toast.LENGTH_SHORT).show();
                    } else {
                        lyt_gipipes.setVisibility(View.VISIBLE);
                    }
                }else if("Select".equalsIgnoreCase(sp_cipipes.getSelectedItem().toString())){
                lyt_gipipes.setVisibility(View.VISIBLE);
            }*/
                break;
            case R.id.sp_gipipes:
                gipipes = sp_gipipes.getSelectedItem().toString();
                giPipesCount = Integer.parseInt(gipipes);
                if ("GI Pipes".equalsIgnoreCase(earthpits_type)) {
                    if (giPipesCount > earthPitsNo || giPipesCount < earthPitsNo) {
                        Toast.makeText(PMIDTRSurveyActivity.this, "GI Pipes count should be equal to Earth Pits Count", Toast.LENGTH_SHORT).show();
                    }
                }
              /*  if(!"Select".equalsIgnoreCase(gipipes)) {
                    giPipesCount = Integer.parseInt(gipipes);
                }*/
                break;
            case R.id.sp_dtrmeter:
                dtrmeter = sp_dtrmeter.getSelectedItem().toString();
                if ("YES".equalsIgnoreCase(dtrmeter)) {
                    lyt_nof_dtrmeters.setVisibility(View.VISIBLE);
                    // lyt_dtrmeter.setVisibility(View.VISIBLE);
                } else {
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
                noof_dtrmeters = sp_noof_dtrmeters.getSelectedItem().toString();
                if ("1".equalsIgnoreCase(noof_dtrmeters)) {
                    lyt_dtrmeter.setVisibility(View.VISIBLE);
                    lyt_dtrmeter_three.setVisibility(View.GONE);
                    lyt_dtrmeter_two.setVisibility(View.GONE);
                    edt_meternum_two.setText("");
                    edt_metermake_two.setText("");
                    edt_metercapacity_two.setText("");
                    edt_meternum_three.setText("");
                    edt_metermake_three.setText("");
                    edt_metercapacity_three.setText("");
                } else if ("2".equalsIgnoreCase(noof_dtrmeters)) {
                    lyt_dtrmeter.setVisibility(View.VISIBLE);
                    lyt_dtrmeter_two.setVisibility(View.VISIBLE);
                    lyt_dtrmeter_three.setVisibility(View.GONE);
                    edt_meternum_three.setText("");
                    edt_metermake_three.setText("");
                    edt_metercapacity_three.setText("");
                } else if ("3".equalsIgnoreCase(noof_dtrmeters)) {
                    lyt_dtrmeter.setVisibility(View.VISIBLE);
                    lyt_dtrmeter_three.setVisibility(View.VISIBLE);
                    lyt_dtrmeter_two.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.sp_metertype:
                metertype = sp_metertype.getSelectedItem().toString();
                break;
            case R.id.sp_metertype_two:
                metertype_two = sp_metertype_two.getSelectedItem().toString();
                break;

            case R.id.sp_metertype_three:
                metertype_three = sp_metertype_three.getSelectedItem().toString();
                break;


            case R.id.sp_modem_available:
                modem_available = sp_modem_available.getSelectedItem().toString();
                if ("YES".equalsIgnoreCase(modem_available)) {
                    lyt_modem.setVisibility(View.VISIBLE);
                } else {
                    lyt_modem.setVisibility(View.GONE);
                    edt_modemserial_no.setText("");
                    edt_modemtwo_make.setText("");
                }
                break;

            case R.id.sp_modemtwo_available:
                modemtwo_available = sp_modemtwo_available.getSelectedItem().toString();
                if ("YES".equalsIgnoreCase(modemtwo_available)) {
                    lyt_modemtwo.setVisibility(View.VISIBLE);
                } else {
                    lyt_modemtwo.setVisibility(View.GONE);
                    edt_modemtwo_serial_no.setText("");
                    edt_modemtwo_make.setText("");
                }
                break;
            case R.id.sp_modemthree_available:
                modemthree_available = sp_modemthree_available.getSelectedItem().toString();
                if ("YES".equalsIgnoreCase(modemthree_available)) {
                    lyt_modem_three.setVisibility(View.VISIBLE);
                } else {
                    lyt_modem_three.setVisibility(View.GONE);
                    edt_modemthree_serial_no.setText("");
                    edt_modemthree_make.setText("");
                }
                break;

            case R.id.sp_busbar_type:
                busbar_type = sp_busbar_type.getSelectedItem().toString();
                if ("Conductor".equalsIgnoreCase(busbar_type)) {
                    lyt_busbar_condoctorsize.setVisibility(View.VISIBLE);
                } else {
                    lyt_busbar_condoctorsize.setVisibility(View.GONE);
                }
                break;
            case R.id.sp_consuctorsize:
                consuctorsize = sp_consuctorsize.getSelectedItem().toString();
                break;

            case R.id.sp_hgfuseto_dtr:
                hgfuseto_dtr = sp_hgfuseto_dtr.getSelectedItem().toString();
                break;

            case R.id.sp_dtrpole:
                dtrpole = sp_dtrpole.getSelectedItem().toString();
                if ("PIN".equalsIgnoreCase(dtrpole)) {
                    lyt_dtrpolepin.setVisibility(View.VISIBLE);
                    lyt_dtrpolecut.setVisibility(View.GONE);
                    edt_noof_cpin_insulators.setText("");
                    edt_noof_disc_insulators.setText("");
                } else if ("CUT".equalsIgnoreCase(dtrpole)) {
                    lyt_dtrpolecut.setVisibility(View.VISIBLE);
                    lyt_dtrpolepin.setVisibility(View.GONE);
                }
                break;

            case R.id.sp_hgfuseto_dtr_consuctorsize:
                hgfuseto_dtr_consuctorsize = sp_hgfuseto_dtr_consuctorsize.getSelectedItem().toString();
                break;

            case R.id.sp_pin:
                pin = sp_pin.getSelectedItem().toString();
                break;

            case R.id.sp_cut:
                cut = sp_cut.getSelectedItem().toString();
                break;

            case R.id.sp_lt_fuseset:
                lt_fuseset = sp_lt_fuseset.getSelectedItem().toString();
                if (lt_fuseset.equalsIgnoreCase("Distribution Box")){
                    distribution_box_damaged.setVisibility(View.VISIBLE);
                    distribution_board_damaged.setVisibility(View.GONE);
                    lg_hg_fuse_set_damaged.setVisibility(View.GONE);
                }
                if (lt_fuseset.equalsIgnoreCase("Distribution Board")){
                    distribution_board_damaged.setVisibility(View.VISIBLE);
                    distribution_box_damaged.setVisibility(View.GONE);
                    lg_hg_fuse_set_damaged.setVisibility(View.GONE);
                }
                if (lt_fuseset.equalsIgnoreCase("LT HG Fuse Set")){
                    lg_hg_fuse_set_damaged.setVisibility(View.VISIBLE);
                    distribution_board_damaged.setVisibility(View.GONE);
                    distribution_box_damaged.setVisibility(View.GONE);
                }
                break;

            case R.id.sp_lt_busbar:
                lt_busbar = sp_lt_busbar.getSelectedItem().toString();
                if ("Cable".equalsIgnoreCase(lt_busbar)) {
                    lyt_busbarcable_size.setVisibility(View.VISIBLE);
                    lyt_lt_busbarconductor_size.setVisibility(View.GONE);
                    ch_xlpe_cable.setVisibility(View.VISIBLE);
                } else if ("Conductor".equalsIgnoreCase(lt_busbar)) {
                    lyt_lt_busbarconductor_size.setVisibility(View.VISIBLE);
                    lyt_busbarcable_size.setVisibility(View.GONE);
                    ch_xlpe_cable.setVisibility(View.GONE);
                }
                break;

            case R.id.sp_lt_busbarcable_size:
                lt_busbarcable_size = sp_lt_busbarcable_size.getSelectedItem().toString();
                break;

            case R.id.sp_lt_busbarconductor_size:
                lt_busbarconductor_size = sp_lt_busbarconductor_size.getSelectedItem().toString();
                break;

            case R.id.sp_dtrload:
                dtrload = sp_dtrload.getSelectedItem().toString();
                break;
            case R.id.sp_dtrphase:
                dtrphase = sp_dtrphase.getSelectedItem().toString();
                if ("I".equalsIgnoreCase(dtrphase)) {
                    List<String> phase1 = new ArrayList<>();
                    /*phase1.add("Select");*/
                    // phase1.add(dtrvo.getDTR_CAPACITY());
                    phase1.add("5 KVA");
                    phase1.add("10 KVA");
                    phase1.add("15 KVA");
                    phase1.add("16 KVA");
                    phase1.add("25 KVA");
                    if (type.equals("new")){
                        phase1.remove(not_updated.getDTR_CAPACITY());
                        phase1.add(0,not_updated.getDTR_CAPACITY());
                    }else {
                        phase1.remove(dtrvo.getDTR_CAPACITY());
                        phase1.add(0,dtrvo.getDTR_CAPACITY());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, phase1);
                    sp_dtrcapacity.setAdapter(adapter);
                } else if ("III".equalsIgnoreCase(dtrphase)) {
                    List<String> phase3 = new ArrayList<>();
                    /*phase3.add("Select");*/
                    // phase3.add(dtrvo.getDTR_PHASE());
                    //  phase3.add("15 KVA");
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
                    if (type.equals("new")){
                        phase3.remove(not_updated.getDTR_CAPACITY());
                        phase3.add(0,not_updated.getDTR_CAPACITY());
                    }else {
                        phase3.remove(dtrvo.getDTR_CAPACITY());
                        phase3.add(0,dtrvo.getDTR_CAPACITY());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, phase3);
                    sp_dtrcapacity.setAdapter(adapter);
                }
                break;
            case R.id.sp_dtrcapacity:
                dtrcapacity = sp_dtrcapacity.getSelectedItem().toString();
                break;
            case R.id.sp_dtrstr_type:
                dtrstr_type = sp_dtrstr_type.getSelectedItem().toString();
                if (dtrstr_type.equalsIgnoreCase("PLINTH")){
                    List<String> list1 = new ArrayList<>();
                    list1.add("Select");
                    list1.add("PLINTH DAMAGED");
                    list1.add("PLINTH HEIGHT NOT SUFFICIENT");
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list1);
                    sp_structure_defects.setAdapter(adapter1);
                    sp_structure_defects.setSelection(0,true);
                }
                else if (dtrstr_type.equalsIgnoreCase("Pole Mounted") || dtrstr_type.equalsIgnoreCase("SINGLE POLE")){
                    List<String> list1 = new ArrayList<>();
                    list1.add("Select");
                    list1.add("DTR Mounting Damaged");
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list1);
                    sp_structure_defects.setAdapter(adapter1);
                    sp_structure_defects.setSelection(0,true);
                }
                else if (dtrstr_type.equalsIgnoreCase("DOUBLE POLE")){
                    List<String> list1 = new ArrayList<>();
                    list1.add("Select");
                    list1.add("DP Structure Damaged");
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list1);
                    sp_structure_defects.setAdapter(adapter1);
                    sp_structure_defects.setSelection(0,true);
                }
                else if (dtrstr_type.equalsIgnoreCase("FOUR POLE")){
                    List<String> list1 = new ArrayList<>();
                    list1.add("Select");
                    list1.add("Structure Damaged");
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(PMIDTRSurveyActivity.this, android.R.layout.simple_dropdown_item_1line, list1);
                    sp_structure_defects.setAdapter(adapter1);
                    sp_structure_defects.setSelection(0,true);
                }
                break;

            case R.id.sp_dtrs_pole_support:
                dtrs_pole_support = sp_dtrs_pole_support.getSelectedItem().toString();
                if ("STURT".equalsIgnoreCase(dtrs_pole_support)) {
                    lyt_noof_stay.setVisibility(View.GONE);
                    lyt_noof_sturts.setVisibility(View.VISIBLE);
                } else if ("STAY".equalsIgnoreCase(dtrs_pole_support)) {
                    lyt_noof_stay.setVisibility(View.VISIBLE);
                    lyt_noof_sturts.setVisibility(View.GONE);
                } else if ("STURT AND STAY".equalsIgnoreCase(dtrs_pole_support)) {
                    lyt_noof_stay.setVisibility(View.VISIBLE);
                    lyt_noof_sturts.setVisibility(View.VISIBLE);
                }else if ("NONE".equalsIgnoreCase(dtrs_pole_support)) {
                    lyt_noof_stay.setVisibility(View.GONE);
                    lyt_noof_sturts.setVisibility(View.GONE);
                }

                break;

            case R.id.sp_noof_pininsulators:
                noof_pininsulators = sp_noof_pininsulators.getSelectedItem().toString();
                break;

            case R.id.sp_noof_ltcircuits:
                noof_ltcircuits = sp_noof_ltcircuits.getSelectedItem().toString();
                break;


        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void validateDTRSurveyData() {
        try {

           /* if ("CI Pipes".equalsIgnoreCase(earthpits_type)) {
                if (ciPipesCount > earthPitsNo || ciPipesCount < earthPitsNo) {
                    Toast.makeText(DTRSurveyActivity.this, "CI Pipes count should be equal to Earth Pits Count", Toast.LENGTH_SHORT).show();
                }
            }*/
            ciGiPipesCount = ciPipesCount + giPipesCount;
            /*if ("CI AND GI Pipes".equalsIgnoreCase(earthpits_type)) {
                ciGiPipesCount = ciPipesCount + giPipesCount;
            }

            else*/ if (aut_dtrmake.getText().toString().isEmpty()) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "Enter DTR Make", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            } else if (edt_dtrserialnum.getText().toString().isEmpty()) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "Enter DTR SerialNo.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            } /*else if ("Select".equalsIgnoreCase(dtrcapacity)) {
                Toast.makeText(c, "Select DTR Capacity", Toast.LENGTH_SHORT).show();
            }*//*else if("POLE".equalsIgnoreCase(dtrstr_type) && edt_noof_poles.getText().toString().isEmpty()){
            Toast.makeText(c,"Enter No.Of Poles",Toast.LENGTH_SHORT).show();
        }*/ else if ("STURT".equalsIgnoreCase(dtrs_pole_support) && edt_noof_sturts.getText().toString().isEmpty()) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "Enter No.Of Sturts", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            }

            else if ("STURT".equalsIgnoreCase(dtrs_pole_support) && Integer.parseInt(edt_noof_sturts.getText().toString()) == 0) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "No.Of Sturts should be greater than 0", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            }

            else if ("STAY".equalsIgnoreCase(dtrs_pole_support) && edt_noof_stay.getText().toString().isEmpty()) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "Enter No.Of Stays", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            }

            else if ("STAY".equalsIgnoreCase(dtrs_pole_support) && Integer.parseInt(edt_noof_stay.getText().toString()) == 0) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "No.Of Stays should be greater than 0", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            }

            else if ("STURT AND STAY".equalsIgnoreCase(dtrs_pole_support) && edt_noof_sturts.getText().toString().isEmpty()) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "Enter No.Of Sturts", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            }

            else if ("STURT AND STAY".equalsIgnoreCase(dtrs_pole_support) && Integer.parseInt(edt_noof_sturts.getText().toString()) == 0) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "No.Of Sturts should be greater than 0", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            }

            else if ("STURT AND STAY".equalsIgnoreCase(dtrs_pole_support) && edt_noof_stay.getText().toString().isEmpty()) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "Enter No.Of Stays", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            }

            else if ("STURT AND STAY".equalsIgnoreCase(dtrs_pole_support) && Integer.parseInt(edt_noof_stay.getText().toString()) == 0) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "No.Of Stays should be greater than 0", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            }

            else if ("CI Pipes".equalsIgnoreCase(earthpits_type) && ciPipesCount > earthPitsNo) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "CI Pipes count should be equal to Earth Pits Count", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("CI Pipes".equalsIgnoreCase(earthpits_type) && ciPipesCount < earthPitsNo) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "CI Pipes count should be equal to Earth Pits Count", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("GI Pipes".equalsIgnoreCase(earthpits_type) && giPipesCount > earthPitsNo) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "GI Pipes count should be equal to Earth Pits Count", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("GI Pipes".equalsIgnoreCase(earthpits_type) && giPipesCount < earthPitsNo) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "GI Pipes count should be equal to Earth Pits Count", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("CI AND GI Pipes".equalsIgnoreCase(earthpits_type) && ciGiPipesCount > earthPitsNo) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "CI and GI Pipes count should be equal to Earth Pits Count", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("CI AND GI Pipes".equalsIgnoreCase(earthpits_type) && ciGiPipesCount < earthPitsNo) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "CI and GI Pipes count should be equal to Earth Pits Count", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            }

        /*else if("Select".equalsIgnoreCase(cipipes)) {
            Toast.makeText(c,"Select  No.Of CI Pipes",Toast.LENGTH_SHORT).show();
        }else if(ciPipesCount < earthPitsNo && "Select".equalsIgnoreCase(gipipes)){
            Toast.makeText(c,"Select No.Of GI Pipes",Toast.LENGTH_SHORT).show();
        }else if(ciPipesCount >earthPitsNo){
            Toast.makeText(c, "CI Pipes count should not greater than Earth Pits Count", Toast.LENGTH_SHORT).show();
        }else if(ciGiPipesCount >earthPitsNo){
            Toast.makeText(c, "CI &GI Pipes count should not greater than Earth Pits Count", Toast.LENGTH_SHORT).show();
        }else if(ciGiPipesCount <earthPitsNo){
            Toast.makeText(c, "CI &GI Pipes count should not less than Earth Pits Count", Toast.LENGTH_SHORT).show();
        }*/



        /*else if(""){

        }*/
            else if("Select".equalsIgnoreCase(earthpits)) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "Select Earth Pits No.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            }
            else if("Select".equalsIgnoreCase(dtrmeter)) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "Select DTR Meter", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            }

            else if ("1".equalsIgnoreCase(noof_dtrmeters) && edt_meternum.getText().toString().isEmpty() && "YES".equalsIgnoreCase(dtrmeter)) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "Enter Meter1 No.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("1".equalsIgnoreCase(noof_dtrmeters) && edt_metermake.getText().toString().isEmpty() && "YES".equalsIgnoreCase(dtrmeter)) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "Enter Meter1 Make.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("1".equalsIgnoreCase(noof_dtrmeters) && edt_metercapacity.getText().toString().isEmpty() && "YES".equalsIgnoreCase(dtrmeter)) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "Enter Meter1 Capacity.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("2".equalsIgnoreCase(noof_dtrmeters) && edt_meternum.getText().toString().isEmpty() && "YES".equalsIgnoreCase(dtrmeter)) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "Enter Meter1 No.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("2".equalsIgnoreCase(noof_dtrmeters) && edt_metermake.getText().toString().isEmpty() && "YES".equalsIgnoreCase(dtrmeter)) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "Enter Meter1 Make.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("2".equalsIgnoreCase(noof_dtrmeters) && edt_metercapacity.getText().toString().isEmpty() && "YES".equalsIgnoreCase(dtrmeter)) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "Enter Meter1 Capacity.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("2".equalsIgnoreCase(noof_dtrmeters) && edt_meternum_two.getText().toString().isEmpty() && "YES".equalsIgnoreCase(dtrmeter)) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "Enter Meter2 No.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("2".equalsIgnoreCase(noof_dtrmeters) && edt_metermake_two.getText().toString().isEmpty() && "YES".equalsIgnoreCase(dtrmeter)) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "Enter Meter2 Make.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("2".equalsIgnoreCase(noof_dtrmeters) && edt_metercapacity_two.getText().toString().isEmpty() && "YES".equalsIgnoreCase(dtrmeter)) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "Enter Meter2 Capacity.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("3".equalsIgnoreCase(noof_dtrmeters) && edt_meternum.getText().toString().isEmpty() && "YES".equalsIgnoreCase(dtrmeter)) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "Enter Meter1 No.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("3".equalsIgnoreCase(noof_dtrmeters) && edt_metermake.getText().toString().isEmpty() && "YES".equalsIgnoreCase(dtrmeter)) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "Enter Meter1 Make.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("3".equalsIgnoreCase(noof_dtrmeters) && edt_metercapacity.getText().toString().isEmpty() && "YES".equalsIgnoreCase(dtrmeter)) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "Enter Meter1 Capacity.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("3".equalsIgnoreCase(noof_dtrmeters) && edt_meternum_two.getText().toString().isEmpty() && "YES".equalsIgnoreCase(dtrmeter)) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "Enter Meter2 No.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("3".equalsIgnoreCase(noof_dtrmeters) && edt_metermake_two.getText().toString().isEmpty() && "YES".equalsIgnoreCase(dtrmeter)) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "Enter Meter2 Make.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("3".equalsIgnoreCase(noof_dtrmeters) && edt_metercapacity_two.getText().toString().isEmpty() && "YES".equalsIgnoreCase(dtrmeter)) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "Enter Meter3 Capacity.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("3".equalsIgnoreCase(noof_dtrmeters) && edt_meternum_three.getText().toString().isEmpty() && "YES".equalsIgnoreCase(dtrmeter)) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "Enter Meter3 No.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("3".equalsIgnoreCase(noof_dtrmeters) && edt_metermake_three.getText().toString().isEmpty() && "YES".equalsIgnoreCase(dtrmeter)) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "Enter Meter3 Make.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("3".equalsIgnoreCase(noof_dtrmeters) && edt_metercapacity_three.getText().toString().isEmpty() && "YES".equalsIgnoreCase(dtrmeter)) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "Enter Meter3 Capacity.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("YES".equalsIgnoreCase(modem_available) && edt_modemserial_no.getText().toString().isEmpty() && "YES".equalsIgnoreCase(dtrmeter)) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "Enter Modem1 SerialNo.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("YES".equalsIgnoreCase(modem_available) && edt_modem_make.getText().toString().isEmpty() && "YES".equalsIgnoreCase(dtrmeter)) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "Enter Modem1 Make", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("YES".equalsIgnoreCase(modemtwo_available) && edt_modemtwo_serial_no.getText().toString().isEmpty() && "YES".equalsIgnoreCase(dtrmeter)) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "Enter Modem2 SerialNo.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("YES".equalsIgnoreCase(modemtwo_available) && edt_modemtwo_make.getText().toString().isEmpty() && "YES".equalsIgnoreCase(dtrmeter)) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "Enter Modem2 Make.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("YES".equalsIgnoreCase(modemthree_available) && edt_modemthree_serial_no.getText().toString().isEmpty() && "YES".equalsIgnoreCase(dtrmeter)) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "Enter Modem3 SerialNo.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("YES".equalsIgnoreCase(modemthree_available) && edt_modemthree_make.getText().toString().isEmpty() && "YES".equalsIgnoreCase(dtrmeter)) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "Enter Modem3 Make.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("CUT".equalsIgnoreCase(dtrpole) && edt_noof_cpin_insulators.getText().toString().isEmpty()) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "Enter No.Of Insulators.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            } else if ("CUT".equalsIgnoreCase(dtrpole) && edt_noof_disc_insulators.getText().toString().isEmpty()) {
                Toast toast = Toast.makeText(PMIDTRSurveyActivity.this, "Enter No.Of Disc Insulators", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            } else if (!image_1_clicked && !image_2_clicked && !image_3_clicked){
                Toast.makeText(PMIDTRSurveyActivity.this, "Upload at least 1 photo", Toast.LENGTH_SHORT).show();
            } else {
                try {

                    PMIDTRSurveyVo dtrSurveyVo = new PMIDTRSurveyVo();
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
                    if ("STAY".equalsIgnoreCase(dtrs_pole_support)) {
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
                    //dtrSurveyVo.setLAT(x);
                    //dtrSurveyVo.setLAN(y);
                    dtrSurveyVo.setLAT(dtrDetailsResponseVoList.get(0).getGLAT());
                    dtrSurveyVo.setLAN(dtrDetailsResponseVoList.get(0).getGLONG());


                    dtrSurveyVo.setDTR_LOCATION(dtrlLocation);
                    dtrSurveyVo.setNOOF_LTCIRCUITS(noof_ltcircuits);

                    //NEW
                    if (defect_selected.equalsIgnoreCase("Yes")){
                        dtrSurveyVo.setSTRUCTURE_DEFECTS(ch_structure_defects.isChecked() ? sp_structure_defects.getSelectedItem().toString() : "");
                        dtrSurveyVo.setDTR_FENCING(ch_fencing_required.isChecked() ? "FENCING REQUIRED" : "");
                        dtrSurveyVo.setDTR_AB_DEFECTS(ch_dtr_ab_switch.isChecked() ? sp_dtr_ab_switch.getSelectedItem().toString() : "");
                        dtrSurveyVo.setHG_FUSE_DEFECTS(ch_fencing_required.isChecked() ? "DAMAGED" : "");
                        dtrSurveyVo.setLT_FUSE_DEFECTS(LT_FUSE_DEFECTS);
                        dtrSurveyVo.setNUM_EARTH(ch_earth_pits_damaged.isChecked() ? sp_earth_pits_damaged.getSelectedItem().toString() : "");
                        dtrSurveyVo.setNUM_LT_BUSHINGS(ch_no_of_lt_bushings_damaged.isChecked() ? sp_no_of_lt_bushings_damaged.getSelectedItem().toString() : "");
                        dtrSurveyVo.setNUM_HT_BUSHINGS(ch_no_of_ht_bushings_damaged.isChecked() ? sp_no_of_ht_bushings_damaged.getSelectedItem().toString() : "");
                        dtrSurveyVo.setNUM_PG_LUG(ch_pg_clamp_lugs.isChecked() ? sp_pg_clamp_lugs.getSelectedItem().toString() : "");
                        dtrSurveyVo.setXLPE_DEFECTS(ch_xlpe_cable.isChecked() ? "DAMAGED" : "");
                        dtrSurveyVo.setNUM_ARRESTER(ch_lightening_aresters.isChecked() ? sp_lightening_aresters.getSelectedItem().toString() : "");
                        dtrSurveyVo.setOIL_LEAK(ch_oil_leakage.isChecked() ? "YES" : "");
                        dtrSurveyVo.setOTHER_DEFECTS(ch_other_defects.isChecked() ? ed_other_defects.getText().toString() : "");
                    }else{
                        dtrSurveyVo.setSTRUCTURE_DEFECTS("");
                        dtrSurveyVo.setDTR_FENCING("");
                        dtrSurveyVo.setDTR_AB_DEFECTS("");
                        dtrSurveyVo.setHG_FUSE_DEFECTS("");
                        dtrSurveyVo.setLT_FUSE_DEFECTS("");
                        dtrSurveyVo.setNUM_EARTH("");
                        dtrSurveyVo.setNUM_LT_BUSHINGS("");
                        dtrSurveyVo.setNUM_HT_BUSHINGS("");
                        dtrSurveyVo.setNUM_PG_LUG("");
                        dtrSurveyVo.setXLPE_DEFECTS("");
                        dtrSurveyVo.setNUM_ARRESTER("");
                        dtrSurveyVo.setOIL_LEAK("");
                        dtrSurveyVo.setOTHER_DEFECTS("");
                    }

                    dtrSurveyVo.setIMAGE_1(image_1_clicked ? image_1_base64 : null);
                    dtrSurveyVo.setIMAGE_2(image_2_clicked ? image_2_base64 : null);
                    dtrSurveyVo.setIMAGE_3(image_3_clicked ? image_3_base64 : null);

                    dtrSurveyVo.setSURVEY_REMARKS(ed_remarks.getText().toString().isEmpty() ? "" : ed_remarks.getText().toString());


                    arcGISDatabase.addDTRSurveyData(dtrSurveyVo);
                    Toast.makeText(PMIDTRSurveyActivity.this, "DTR dats captured successfully..", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PMIDTRSurveyActivity.this, PMIHomeActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}