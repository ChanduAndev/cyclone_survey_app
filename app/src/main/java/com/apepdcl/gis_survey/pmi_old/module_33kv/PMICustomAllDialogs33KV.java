package com.apepdcl.gis_survey.pmi_old.module_33kv;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apepdcl.gis_survey.Constants.MobileConstants;

import net.sourceforge.app.MainActivity;
import com.apepdcl.gis.app.R;

import com.apepdcl.gis_survey.module_11KV.ConvertImage;
import com.apepdcl.gis_survey.module_11KV.ImpData;
import com.apepdcl.gis_survey.pmi_old.module_11kv.PMIArcGISDatabase;
import com.apepdcl.gis_survey.pmi_old.module_11kv.activities.PMIHomeActivity;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIImageList;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIImageRequest;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIMultiCktVO;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIMultiRequestVO;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMISurveyVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMISurveyedRequestDataVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMISurveyedRequestVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMISuveyedResponseVo;
import com.apepdcl.gis_survey.pmi_old.module_33kv.activities.PMIArcGISMapActivity33KV;
import com.apepdcl.gis_survey.pmi_old.module_33kv.activities.PMIPoleSurveyActivity33KV;
import com.apepdcl.gis_survey.pmi_old.module_33kv.activities.PMISurveyHome33KV;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.leo.searchablespinner.SearchableSpinner;
import com.leo.searchablespinner.interfaces.OnItemSelectListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PMICustomAllDialogs33KV extends Dialog implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private final String x, y;
    public Activity c;
    private Button btn_add, btn_clear, btn_topclear;
    private EditText ed_pole_no, ed_frompole;
    private TextInputEditText edt_oh_htscnum_overhead, edt_polepaintingcode, edt_remarks_overhead, edt_remarks_underground, edt_tappinglocation, edt_ugtappinglocation, edt_ugpolepaintingcode;
    public ImpData deligate;
    private RetrofitService33kv retrofitService;
    private ProgressDialog progressDialog;
    Callout callout;
    GraphicsOverlay graphicsOverlay;
    GraphicsOverlay currentgraphicsOverlay;
    Graphic graphic;
    Graphic currentGraphic;
    LocationDisplay locationDisplay;

    private Spinner sp_ugssname, sp_ugnooftappings, sp_ugtapingpoint, sp_ugpolesupport, sp_ugpoleheight, sp_ugpoletype, sp__underpolestructure, sp_undergisendpole, sp_nooftappings, sp_rmu, sp_straight_rmu, sp_ssname, sp_diffvoltage, sp__polestructure, sp_noofcircuit_underg, sp_cablesize, sp_lineType, sp_poletype, sp_poleheight, sp_noofcircuit, sp_polesupport, sp_tapingpoint, sp_conductorsize, sp_pinpoint, sp_poleconfiguration, sp_ismulticircute, sp_circutetype, sp_isendpole;
    private String ugssname = "", ugnooftappings = "", ugtapingpoint = "", ugpolesupport = "", ugpoleheight = "", ugpoletype = "", underpolestructure = "", undergisendpole = "", ssName = "", nooftappings, rmu, straight_rmu, ssId = "", ssname, diffvoltage, polestructure, noofcircuit_underg, cablesize, noofcircuit, lineType, endPole, isendpole, surveyType, poleType, poleHeight, noofPoles, feederName, poleSupport, tapingPoint, taping_Point, conductorsize, pinpoint, poleconfiguration, feederCode, tappingPoint, ismulticircute, circutetype;
    private ArcGISDatabase33KV arcGISDatabase;
    private List<PMISurveyVo> surveyVosList;
    private int maxSurveyCount, poleCountCheck;
    private LinearLayout lyt_circuit, lyt_overhead, lyt_underground, lyt_ssname, lyt_polestructure, lyt_diffvoltage, lyt_rmu, lyt_tapping, lyt_underpolestructure, lyt_ugpole, lyt_ugtapping, lyt_ugssname;
    private TextView tv_sunname, tv_feedename;
    private Map mapData;
    private int copyFlag = 0;
    private LinearLayout lyt_oh_htscnum;


    Spinner sp_Survey_type;
    private String Survey_type_string = "";

    //MULTI CKT

    LinearLayout lyt_oh_multivoltage;
    private int noof_33kvCkts, noof_11kvCkts, noof_ltCkts;

    //11 KV
    LinearLayout lyt_oh_11kv_vlckts;
    CheckBox ch_oh_11kv_vlckts;
    Spinner sp_oh_no_of_11_feeders;
    String str_no_of_feeders;
    Spinner sp_oh_11_feeder_1, sp_oh_11_feeder_1_pole, sp_oh_11_feeder_2, sp_oh_11_feeder_2_pole, sp_oh_11_feeder_3, sp_oh_11_feeder_3_pole;
    TextView ed_oh_11_feeder_1, ed_oh_11_feeder_2, ed_oh_11_feeder_3;
    TextView ed_oh_11_feeder_1_ns, ed_oh_11_feeder_2_ns, ed_oh_11_feeder_3_ns;
    LinearLayout main_layout_feeder_1, layout_oh_11_feeder_1, main_layout_feeder_2;
    LinearLayout layout_oh_11_feeder_2, main_layout_feeder_3, layout_oh_11_feeder_3;
    TextView get_oh_11_feeder_1, get_oh_11_feeder_2, get_oh_11_feeder_3;
    EditText ed_oh_11_pole_1, ed_oh_11_pole_2, ed_oh_11_pole_3;
    LinearLayout feeder_1_pole_layout_11, feeder_2_pole_layout_11, feeder_3_pole_layout_11;


    //33 KV
    LinearLayout lyt_oh_33kv_vlckts;
    CheckBox ch_oh_33kv_vlckts;
    Spinner sp_oh_no_of_33_feeders;
    Spinner sp_oh_33_feeder_1, sp_oh_33_feeder_1_pole, sp_oh_33_feeder_2, sp_oh_33_feeder_2_pole, sp_oh_33_feeder_3, sp_oh_33_feeder_3_pole;
    TextView ed_oh_33_feeder_1, ed_oh_33_feeder_2, ed_oh_33_feeder_3;
    TextView ed_oh_33_feeder_1_ns, ed_oh_33_feeder_2_ns, ed_oh_33_feeder_3_ns;
    LinearLayout main_layout_feeder_1_33, layout_oh_33_feeder_1, main_layout_feeder_2_33;
    LinearLayout layout_oh_33_feeder_2, main_layout_feeder_3_33, layout_oh_33_feeder_3;
    TextView get_oh_33_feeder_1, get_oh_33_feeder_2, get_oh_33_feeder_3;
    EditText ed_oh_33_pole_1, ed_oh_33_pole_2, ed_oh_33_pole_3;
    LinearLayout feeder_1_pole_layout_33, feeder_2_pole_layout_33, feeder_3_pole_layout_33;

    //LT
    LinearLayout lyt_oh_lt_vlckts;
    CheckBox ch_oh_lt_vlckts;
    Spinner sp_oh_no_of_lt_feeders;
    Spinner sp_oh_lt_feeder_1, sp_oh_lt_feeder_1_pole, sp_oh_lt_feeder_2, sp_oh_lt_feeder_2_pole, sp_oh_lt_feeder_3, sp_oh_lt_feeder_3_pole;
    TextView ed_oh_lt_feeder_1, ed_oh_lt_feeder_2, ed_oh_lt_feeder_3;
    TextView ed_oh_lt_feeder_1_ns, ed_oh_lt_feeder_2_ns, ed_oh_lt_feeder_3_ns;
    LinearLayout main_layout_feeder_1_lt, layout_oh_lt_feeder_1, main_layout_feeder_2_lt;
    LinearLayout layout_oh_lt_feeder_2, main_layout_feeder_3_lt, layout_oh_lt_feeder_3;
    TextView get_oh_lt_feeder_1, get_oh_lt_feeder_2, get_oh_lt_feeder_3;
    EditText ed_oh_lt_pole_1, ed_oh_lt_pole_2, ed_oh_lt_pole_3;
    LinearLayout feeder_1_pole_layout_lt, feeder_2_pole_layout_lt, feeder_3_pole_layout_lt;

    //OTHER CABLES
    Spinner sp_oh_other_cables;
    EditText edt_oh_other_cables;

    //DEFECTS
    Spinner sp_oh_defects, sp_pole_defects, sp_conductors, sp_ab_switch_defects, sp_insulators, sp_cross_arm, sp_top_cleat, sp_strut_pole, sp_stray_wire,
            sp_ht_clearance, sp_int_pole, sp_restringing, sp_tree_clearance;
    CheckBox ch_pole_defects, ch_conductors, ch_ab_switch_defects, ch_insulators, ch_cross_arm, ch_top_cleat, ch_strut_pole, ch_stray_wire, ch_ht_clearance, ch_int_pole,
            ch_restringing, ch_tree_clearance, ch_other_defects;
    LinearLayout defects_layout;
    EditText ed_other_defects;
    String defect_selected = "No";
    //IMAGES
    LinearLayout images_layout;
    public static ImageView image_1, image_2, image_3;
    public static TextView upload_image_1, upload_image_2, upload_image_3;
    public static final int CAMERA_RESULT_CODE_IMAGE_1 = 1000;
    public static final int CAMERA_RESULT_CODE_IMAGE_2 = 2000;
    public static final int CAMERA_RESULT_CODE_IMAGE_3 = 3000;

    public static String currentPhotoPath_1, currentPhotoPath_2;


    public static String image_1_base64, image_2_base64, image_3_base64;
    public static boolean image_1_clicked = false;
    public static boolean image_2_clicked = false;
    public static boolean image_3_clicked = false;

    //CROSSING
    CheckBox ch_crossing;
    LinearLayout lyt_oh_crossing;
    CheckBox ch_oh_6_3_crossing, ch_oh_11kv_crossing, ch_oh_33kv_crossing, ch_oh_lt_crossing, ch_oh_road_crossing;
    CheckBox ch_oh_tc_crossing, ch_oh_tm_crossing;
    LinearLayout lyt_oh_6_3_crossing, lyt_oh_11kv_crossing, lyt_oh_33kv_crossing, lyt_oh_lt_crossing;
    LinearLayout lyt_oh_tc_crossing, lyt_oh_tm_crossing;
    TextView edt_oh_6_3_crossing, edt_oh_11kv_crossing, edt_oh_33kv_crossing, edt_oh_lt_crossing;
    Spinner sp_crossing;
    TextInputEditText edt_oh_tc_crossing, edt_oh_tm_crossing;
    EditText ed_crossing_remarks;

    //INLINE
    private SharedPreferences preferences;
    private String userName, sessionId;

    //DISTANCE
    double lastPoleDistance, currentDistance;
    TextView last_pole_distance, current_distance;

    //REPEAT POLE
    Button btn_repeat_pole;
    boolean repeatPoleClicked = false;
    private List<PMISurveyVo> repeatPoleList;
    String poleHeightRepeat;
    String not;

    //POLE CONCRETE
    Spinner sp_pole_concrete;
    String pole_concrete;

    double CURRENT_LAT;
    double CURRENT_LONG;

    //NEW IMAGE OPEN CAMERA
    public static Bitmap capturedBitmap1 = null;
    public static boolean isImageCaptured1 = false;
    public static boolean photoClicked1 = false;

    public static Bitmap capturedBitmap2 = null;
    public static boolean isImageCaptured2 = false;
    public static boolean photoClicked2 = false;


    public PMICustomAllDialogs33KV(Activity a, String x, String y, Callout callout, GraphicsOverlay graphicsOverlay,
                                Graphic graphic, String feederName, String feederCode, int maxSurveyCount, String tappingPoint,
                                String surveyType, GraphicsOverlay currentgraphicsOverlay, Graphic currentGraphic, String ssName,
                                double lastPoleDistance, double currentDistance, double CURRENT_LAT, double CURRENT_LONG) {
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
        this.lastPoleDistance = lastPoleDistance;
        this.currentDistance = currentDistance;
        this.CURRENT_LAT = CURRENT_LAT;
        this.CURRENT_LONG = CURRENT_LONG;

        //System.out.println("CURRENT LAT : "+CURRENT_LAT);

        preferences = PreferenceManager.getDefaultSharedPreferences(c);
        userName = preferences.getString(MobileConstants.USERNAME, "");
        sessionId = preferences.getString(MobileConstants.SESSION_ID, "");

        isImageCaptured1 = false;
        isImageCaptured2 = false;
        photoClicked1 = false;
        photoClicked2 = false;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pmicustom_all_dialogs33_kv);
        try {
            setCancelable(false);
            progressDialog = CommanUtilits.progressDialog(c);

            //REPEAT POLE
            btn_repeat_pole = findViewById(R.id.btn_repeat_pole);
            //POLE CONCRETE
            sp_pole_concrete = findViewById(R.id.sp_pole_concrete);
            //DISTANCE
            current_distance = findViewById(R.id.current_distance);
            last_pole_distance = findViewById(R.id.last_pole_distance);
            int a = (int) Math.round(lastPoleDistance);
            last_pole_distance.setText("" + a);

            int b = (int) Math.round(currentDistance);
            current_distance.setText("" + b);

            //CROSSING
            ch_crossing = findViewById(R.id.ch_crossing);
            lyt_oh_crossing = findViewById(R.id.lyt_oh_crossing);
            ch_oh_6_3_crossing = findViewById(R.id.ch_oh_6_3_crossing);
            ch_oh_11kv_crossing = findViewById(R.id.ch_oh_11kv_crossing);
            ch_oh_33kv_crossing = findViewById(R.id.ch_oh_33kv_crossing);
            ch_oh_lt_crossing = findViewById(R.id.ch_oh_lt_crossing);
            ch_oh_road_crossing = findViewById(R.id.ch_oh_road_crossing);
            ch_oh_tc_crossing = findViewById(R.id.ch_oh_tc_crossing);
            ch_oh_tm_crossing = findViewById(R.id.ch_oh_tm_crossing);
            lyt_oh_6_3_crossing = findViewById(R.id.lyt_oh_6_3_crossing);
            lyt_oh_11kv_crossing = findViewById(R.id.lyt_oh_11kv_crossing);
            lyt_oh_33kv_crossing = findViewById(R.id.lyt_oh_33kv_crossing);
            lyt_oh_lt_crossing = findViewById(R.id.lyt_oh_lt_crossing);
            lyt_oh_tc_crossing = findViewById(R.id.lyt_oh_tc_crossing);
            lyt_oh_tm_crossing = findViewById(R.id.lyt_oh_tm_crossing);
            edt_oh_6_3_crossing = findViewById(R.id.edt_oh_6_3_crossing);
            edt_oh_11kv_crossing = findViewById(R.id.edt_oh_11kv_crossing);
            edt_oh_33kv_crossing = findViewById(R.id.edt_oh_33kv_crossing);
            edt_oh_lt_crossing = findViewById(R.id.edt_oh_lt_crossing);
            sp_crossing = findViewById(R.id.sp_crossing);
            edt_oh_tc_crossing = findViewById(R.id.edt_oh_tc_crossing);
            edt_oh_tm_crossing = findViewById(R.id.edt_oh_tm_crossing);
            ed_crossing_remarks = findViewById(R.id.ed_crossing_remarks);

            //DEFECTS
            images_layout = findViewById(R.id.images_layout);
            image_1 = findViewById(R.id.image_1);
            image_2 = findViewById(R.id.image_2);
            image_3 = findViewById(R.id.image_3);
            upload_image_1 = findViewById(R.id.upload_image_1);
            upload_image_2 = findViewById(R.id.upload_image_2);
            upload_image_3 = findViewById(R.id.upload_image_3);

            sp_oh_defects = findViewById(R.id.sp_oh_defects);
            defects_layout = findViewById(R.id.defects_layout);
            ch_pole_defects = findViewById(R.id.ch_pole_defects);
            sp_pole_defects = findViewById(R.id.sp_pole_defects);
            ch_conductors = findViewById(R.id.ch_conductors);
            sp_conductors = findViewById(R.id.sp_conductors);
            ch_ab_switch_defects = findViewById(R.id.ch_ab_switch_defects);
            sp_ab_switch_defects = findViewById(R.id.sp_ab_switch_defects);
            ch_insulators = findViewById(R.id.ch_insulators);
            sp_insulators = findViewById(R.id.sp_insulators);
            ch_cross_arm = findViewById(R.id.ch_cross_arm);
            sp_cross_arm = findViewById(R.id.sp_cross_arm);
            ch_top_cleat = findViewById(R.id.ch_top_cleat);
            sp_top_cleat = findViewById(R.id.sp_top_cleat);
            ch_strut_pole = findViewById(R.id.ch_strut_pole);
            sp_strut_pole = findViewById(R.id.sp_strut_pole);
            ch_stray_wire = findViewById(R.id.ch_stray_wire);
            sp_stray_wire = findViewById(R.id.sp_stray_wire);
            ch_ht_clearance = findViewById(R.id.ch_ht_clearance);
            sp_ht_clearance = findViewById(R.id.sp_ht_clearance);
            ch_int_pole = findViewById(R.id.ch_int_pole);
            sp_int_pole = findViewById(R.id.sp_int_pole);
            ch_restringing = findViewById(R.id.ch_restringing);
            sp_restringing = findViewById(R.id.sp_restringing);
            ch_tree_clearance = findViewById(R.id.ch_tree_clearance);
            sp_tree_clearance = findViewById(R.id.sp_tree_clearance);
            ch_other_defects = findViewById(R.id.ch_other_defects);
            ed_other_defects = findViewById(R.id.ed_other_defects);

            //OTHER CABLES
            sp_oh_other_cables = findViewById(R.id.sp_oh_other_cables);
            edt_oh_other_cables = findViewById(R.id.edt_oh_other_cables);


            //MULTI CKT
            lyt_oh_multivoltage = findViewById(R.id.lyt_oh_multivoltage);
            //MULTI CKT 11KV
            ch_oh_11kv_vlckts = findViewById(R.id.ch_oh_11kv_vlckts);
            lyt_oh_11kv_vlckts = findViewById(R.id.lyt_oh_11kv_vlckts);
            sp_oh_no_of_11_feeders = findViewById(R.id.sp_oh_no_of_11_feeders);
            sp_oh_11_feeder_1 = findViewById(R.id.sp_oh_11_feeder_1);
            sp_oh_11_feeder_1_pole = findViewById(R.id.sp_oh_11_feeder_1_pole);
            sp_oh_11_feeder_2 = findViewById(R.id.sp_oh_11_feeder_2);
            sp_oh_11_feeder_2_pole = findViewById(R.id.sp_oh_11_feeder_2_pole);
            sp_oh_11_feeder_3 = findViewById(R.id.sp_oh_11_feeder_3);
            sp_oh_11_feeder_3_pole = findViewById(R.id.sp_oh_11_feeder_3_pole);
            ed_oh_11_feeder_1 = findViewById(R.id.ed_oh_11_feeder_1);
            ed_oh_11_feeder_2 = findViewById(R.id.ed_oh_11_feeder_2);
            ed_oh_11_feeder_3 = findViewById(R.id.ed_oh_11_feeder_3);
            ed_oh_11_feeder_1_ns = findViewById(R.id.ed_oh_11_feeder_1_ns);
            ed_oh_11_feeder_2_ns = findViewById(R.id.ed_oh_11_feeder_2_ns);
            ed_oh_11_feeder_3_ns = findViewById(R.id.ed_oh_11_feeder_3_ns);
            main_layout_feeder_1 = findViewById(R.id.main_layout_feeder_1);
            layout_oh_11_feeder_1 = findViewById(R.id.layout_oh_11_feeder_1);
            main_layout_feeder_2 = findViewById(R.id.main_layout_feeder_2);
            layout_oh_11_feeder_2 = findViewById(R.id.layout_oh_11_feeder_2);
            main_layout_feeder_3 = findViewById(R.id.main_layout_feeder_3);
            layout_oh_11_feeder_3 = findViewById(R.id.layout_oh_11_feeder_3);
            get_oh_11_feeder_1 = findViewById(R.id.get_oh_11_feeder_1);
            get_oh_11_feeder_2 = findViewById(R.id.get_oh_11_feeder_2);
            get_oh_11_feeder_3 = findViewById(R.id.get_oh_11_feeder_3);
            ed_oh_11_pole_1 = findViewById(R.id.ed_oh_11_pole_1);
            ed_oh_11_pole_2 = findViewById(R.id.ed_oh_11_pole_2);
            ed_oh_11_pole_3 = findViewById(R.id.ed_oh_11_pole_3);
            feeder_1_pole_layout_11 = findViewById(R.id.feeder_1_pole_layout_11);
            feeder_2_pole_layout_11 = findViewById(R.id.feeder_2_pole_layout_11);
            feeder_3_pole_layout_11 = findViewById(R.id.feeder_3_pole_layout_11);

            //33 KV
            ch_oh_33kv_vlckts = findViewById(R.id.ch_oh_33kv_vlckts);
            lyt_oh_33kv_vlckts = findViewById(R.id.lyt_oh_33kv_vlckts);
            sp_oh_no_of_33_feeders = findViewById(R.id.sp_oh_no_of_33_feeders);
            sp_oh_33_feeder_1 = findViewById(R.id.sp_oh_33_feeder_1);
            sp_oh_33_feeder_1_pole = findViewById(R.id.sp_oh_33_feeder_1_pole);
            sp_oh_33_feeder_2 = findViewById(R.id.sp_oh_33_feeder_2);
            sp_oh_33_feeder_2_pole = findViewById(R.id.sp_oh_33_feeder_2_pole);
            sp_oh_33_feeder_3 = findViewById(R.id.sp_oh_33_feeder_3);
            sp_oh_33_feeder_3_pole = findViewById(R.id.sp_oh_33_feeder_3_pole);
            ed_oh_33_feeder_1 = findViewById(R.id.ed_oh_33_feeder_1);
            ed_oh_33_feeder_2 = findViewById(R.id.ed_oh_33_feeder_2);
            ed_oh_33_feeder_3 = findViewById(R.id.ed_oh_33_feeder_3);
            ed_oh_33_feeder_1_ns = findViewById(R.id.ed_oh_33_feeder_1_ns);
            ed_oh_33_feeder_2_ns = findViewById(R.id.ed_oh_33_feeder_2_ns);
            ed_oh_33_feeder_3_ns = findViewById(R.id.ed_oh_33_feeder_3_ns);
            main_layout_feeder_1_33 = findViewById(R.id.main_layout_feeder_1_33);
            layout_oh_33_feeder_1 = findViewById(R.id.layout_oh_33_feeder_1);
            main_layout_feeder_2_33 = findViewById(R.id.main_layout_feeder_2_33);
            layout_oh_33_feeder_2 = findViewById(R.id.layout_oh_33_feeder_2);
            main_layout_feeder_3_33 = findViewById(R.id.main_layout_feeder_3_33);
            layout_oh_33_feeder_3 = findViewById(R.id.layout_oh_33_feeder_3);
            get_oh_33_feeder_1 = findViewById(R.id.get_oh_33_feeder_1);
            get_oh_33_feeder_2 = findViewById(R.id.get_oh_33_feeder_2);
            get_oh_33_feeder_3 = findViewById(R.id.get_oh_33_feeder_3);
            ed_oh_33_pole_1 = findViewById(R.id.ed_oh_33_pole_1);
            ed_oh_33_pole_2 = findViewById(R.id.ed_oh_33_pole_2);
            ed_oh_33_pole_3 = findViewById(R.id.ed_oh_33_pole_3);
            feeder_1_pole_layout_33 = findViewById(R.id.feeder_1_pole_layout_33);
            feeder_2_pole_layout_33 = findViewById(R.id.feeder_2_pole_layout_33);
            feeder_3_pole_layout_33 = findViewById(R.id.feeder_3_pole_layout_33);

            //LT
            ch_oh_lt_vlckts = findViewById(R.id.ch_oh_lt_vlckts);
            lyt_oh_lt_vlckts = findViewById(R.id.lyt_oh_lt_vlckts);
            sp_oh_no_of_lt_feeders = findViewById(R.id.sp_oh_no_of_lt_feeders);
            sp_oh_lt_feeder_1 = findViewById(R.id.sp_oh_lt_feeder_1);
            sp_oh_lt_feeder_1_pole = findViewById(R.id.sp_oh_lt_feeder_1_pole);
            sp_oh_lt_feeder_2 = findViewById(R.id.sp_oh_lt_feeder_2);
            sp_oh_lt_feeder_2_pole = findViewById(R.id.sp_oh_lt_feeder_2_pole);
            sp_oh_lt_feeder_3 = findViewById(R.id.sp_oh_lt_feeder_3);
            sp_oh_lt_feeder_3_pole = findViewById(R.id.sp_oh_lt_feeder_3_pole);
            ed_oh_lt_feeder_1 = findViewById(R.id.ed_oh_lt_feeder_1);
            ed_oh_lt_feeder_2 = findViewById(R.id.ed_oh_lt_feeder_2);
            ed_oh_lt_feeder_3 = findViewById(R.id.ed_oh_lt_feeder_3);
            ed_oh_lt_feeder_1_ns = findViewById(R.id.ed_oh_lt_feeder_1_ns);
            ed_oh_lt_feeder_2_ns = findViewById(R.id.ed_oh_lt_feeder_2_ns);
            ed_oh_lt_feeder_3_ns = findViewById(R.id.ed_oh_lt_feeder_3_ns);
            main_layout_feeder_1_lt = findViewById(R.id.main_layout_feeder_1_lt);
            layout_oh_lt_feeder_1 = findViewById(R.id.layout_oh_lt_feeder_1);
            main_layout_feeder_2_lt = findViewById(R.id.main_layout_feeder_2_lt);
            layout_oh_lt_feeder_2 = findViewById(R.id.layout_oh_lt_feeder_2);
            main_layout_feeder_3_lt = findViewById(R.id.main_layout_feeder_3_lt);
            layout_oh_lt_feeder_3 = findViewById(R.id.layout_oh_lt_feeder_3);
            get_oh_lt_feeder_1 = findViewById(R.id.get_oh_lt_feeder_1);
            get_oh_lt_feeder_2 = findViewById(R.id.get_oh_lt_feeder_2);
            get_oh_lt_feeder_3 = findViewById(R.id.get_oh_lt_feeder_3);
            ed_oh_lt_pole_1 = findViewById(R.id.ed_oh_lt_pole_1);
            ed_oh_lt_pole_2 = findViewById(R.id.ed_oh_lt_pole_2);
            ed_oh_lt_pole_3 = findViewById(R.id.ed_oh_lt_pole_3);
            feeder_1_pole_layout_lt = findViewById(R.id.feeder_1_pole_layout_lt);
            feeder_2_pole_layout_lt = findViewById(R.id.feeder_2_pole_layout_lt);
            feeder_3_pole_layout_lt = findViewById(R.id.feeder_3_pole_layout_lt);


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
            lyt_ssname = (LinearLayout) findViewById(R.id.lyt_ssname);
            lyt_polestructure = (LinearLayout) findViewById(R.id.lyt_polestructure);
            lyt_diffvoltage = (LinearLayout) findViewById(R.id.lyt_diffvoltage);
            lyt_rmu = (LinearLayout) findViewById(R.id.lyt_rmu);
            lyt_tapping = (LinearLayout) findViewById(R.id.lyt_tapping);
            lyt_underpolestructure = (LinearLayout) findViewById(R.id.lyt_underpolestructure);
            lyt_ugpole = (LinearLayout) findViewById(R.id.lyt_ugpole);
            lyt_ugtapping = (LinearLayout) findViewById(R.id.lyt_ugtapping);
            lyt_ugssname = (LinearLayout) findViewById(R.id.lyt_ugssname);
            sp_lineType = (Spinner) findViewById(R.id.sp_lineType);
            sp_noofcircuit = (Spinner) findViewById(R.id.sp_noofcircuit);
            sp_cablesize = (Spinner) findViewById(R.id.sp_cablesize);
            sp_noofcircuit_underg = (Spinner) findViewById(R.id.sp_noofcircuit_underg);
            //edt_ssname=(TextInputEditText)findViewById(R.id.edt_ssname);
            sp__polestructure = (Spinner) findViewById(R.id.sp__polestructure);
            sp_diffvoltage = (Spinner) findViewById(R.id.sp_diffvoltage);
            sp_ssname = (Spinner) findViewById(R.id.sp_ssname);
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
            sp_ugssname = (Spinner) findViewById(R.id.sp_ugssname);
            tv_sunname = (TextView) findViewById(R.id.tv_sunname);
            tv_feedename = (TextView) findViewById(R.id.tv_feedename);
            lyt_oh_htscnum = findViewById(R.id.lyt_oh_htscnum);
            edt_oh_htscnum_overhead = findViewById(R.id.edt_oh_htscnum_overhead);

            sp_Survey_type = (Spinner) findViewById(R.id.sp_Survey_type);


            ed_pole_no.setEnabled(false);
            ed_frompole.setEnabled(false);

            btn_clear.setOnClickListener(this);
            btn_add.setOnClickListener(this);
            btn_topclear.setOnClickListener(this);
            btn_repeat_pole.setOnClickListener(this);

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
            sp_Survey_type.setOnItemSelectedListener(this);
            sp_oh_other_cables.setOnItemSelectedListener(this);
            sp_pole_concrete.setOnItemSelectedListener(this);
            arcGISDatabase = new ArcGISDatabase33KV(c);
            surveyVosList = arcGISDatabase.getSurveyData("");
            int count = arcGISDatabase.getObjectId();
            surveyVosList = arcGISDatabase.getSurveyData(String.format("SELECT * FROM  ARCGIS_TABLE WHERE POLE_NO=%s", count));
            if (PMIArcGISMapActivity33KV.IS_SURVEY_STARTED) {
                btn_repeat_pole.setVisibility(View.GONE);
                repeatPoleList = arcGISDatabase.getSurveyData("");
            } else {
                btn_repeat_pole.setVisibility(View.GONE);
            }
            poleCountCheck = arcGISDatabase.getObjectIdCount();
            if (poleCountCheck == 0) {
                //  if(""surveyType)
                int polenum = maxSurveyCount + 1;
                ed_frompole.setText(String.valueOf(count));
                ed_pole_no.setText(String.valueOf(polenum));
            } else {
                ed_frompole.setText(String.valueOf(count));
                ed_pole_no.setText(String.valueOf(count + 1));
            }

            if (surveyType.equalsIgnoreCase("Intermediate/Inline Pole")) {
                ed_frompole.setText(PMIPoleSurveyActivity33KV.result_pole);
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
            lyt_oh_htscnum.setVisibility(View.GONE);
            tv_sunname.setText(ssName);
            tv_feedename.setText(feederName);
            mapData = new HashMap();
            mapData.put("POLE_TYPE", "TESTING");


           /* if(copyFlag==1){
                List<String> str=new ArrayList<>();
                String strtype=(String)mapData.get("POLE_TYPE");
                str.add(strtype);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, str);
                sp_poletype.setAdapter(adapter);
            }*/


            //MULTI CKT
            ch_oh_33kv_vlckts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        lyt_oh_33kv_vlckts.setVisibility(View.VISIBLE);
                    } else {
                        lyt_oh_33kv_vlckts.setVisibility(View.GONE);
                        noof_33kvCkts = 0;

                    }
                }
            });

            ch_oh_11kv_vlckts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        lyt_oh_11kv_vlckts.setVisibility(View.VISIBLE);
                    } else {
                        lyt_oh_11kv_vlckts.setVisibility(View.GONE);
                        noof_11kvCkts = 0;

                    }
                }
            });

            ch_oh_lt_vlckts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        lyt_oh_lt_vlckts.setVisibility(View.VISIBLE);
                    } else {
                        lyt_oh_lt_vlckts.setVisibility(View.GONE);
                        noof_ltCkts = 0;
                    }
                }
            });


            multiCircuitListeners33KV();

            multiCircuitListeners11KV();

            multiCircuitListenersLT();

            //DEFECTS
            defect_selected = sp_oh_defects.getSelectedItem().toString();
            sp_oh_defects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    defect_selected = sp_oh_defects.getSelectedItem().toString();
                    //  System.out.println("SELECTED DEFECTS INSIDE : "+defect_selected);
                    /*if (defect_selected.equalsIgnoreCase("Yes")) {
                        defects_layout.setVisibility(View.VISIBLE);
                        images_layout.setVisibility(View.VISIBLE);
                    } else {
                        defects_layout.setVisibility(View.GONE);
                        if (poleconfiguration.equals("Line-Pole") || poleconfiguration.equals("Select")) {
                            images_layout.setVisibility(View.GONE);
                        } else {
                            images_layout.setVisibility(View.VISIBLE);
                        }

                    }*/
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            ch_pole_defects.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        sp_pole_defects.setVisibility(View.VISIBLE);
                    } else {
                        sp_pole_defects.setVisibility(View.GONE);
                    }
                }
            });

            ch_conductors.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        sp_conductors.setVisibility(View.VISIBLE);
                    } else {
                        sp_conductors.setVisibility(View.GONE);
                    }
                }
            });

            ch_ab_switch_defects.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        sp_ab_switch_defects.setVisibility(View.VISIBLE);
                    } else {
                        sp_ab_switch_defects.setVisibility(View.GONE);
                    }
                }
            });

            ch_insulators.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        sp_insulators.setVisibility(View.VISIBLE);
                    } else {
                        sp_insulators.setVisibility(View.GONE);
                    }
                }
            });

            ch_cross_arm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        sp_cross_arm.setVisibility(View.VISIBLE);
                    } else {
                        sp_cross_arm.setVisibility(View.GONE);
                    }
                }
            });

            ch_top_cleat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        sp_top_cleat.setVisibility(View.VISIBLE);
                    } else {
                        sp_top_cleat.setVisibility(View.GONE);
                    }
                }
            });

            ch_strut_pole.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        sp_strut_pole.setVisibility(View.VISIBLE);
                    } else {
                        sp_strut_pole.setVisibility(View.GONE);
                    }
                }
            });

            ch_stray_wire.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        sp_stray_wire.setVisibility(View.VISIBLE);
                    } else {
                        sp_stray_wire.setVisibility(View.GONE);
                    }
                }
            });

            ch_ht_clearance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        sp_ht_clearance.setVisibility(View.VISIBLE);
                    } else {
                        sp_ht_clearance.setVisibility(View.GONE);
                    }
                }
            });

            ch_int_pole.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        sp_int_pole.setVisibility(View.VISIBLE);
                    } else {
                        sp_int_pole.setVisibility(View.GONE);
                    }
                }
            });

            ch_restringing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        sp_restringing.setVisibility(View.VISIBLE);
                    } else {
                        sp_restringing.setVisibility(View.GONE);
                    }
                }
            });

            ch_tree_clearance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        sp_tree_clearance.setVisibility(View.VISIBLE);
                    } else {
                        sp_tree_clearance.setVisibility(View.GONE);
                    }
                }
            });

            ch_other_defects.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        ed_other_defects.setVisibility(View.VISIBLE);
                    } else {
                        ed_other_defects.setVisibility(View.GONE);
                    }
                }
            });

            //IMAGES

           /* image_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(c, FullImage.class);

                    intent.putExtra("data",currentPhotoPath_2);
                    c.startActivity(intent);
                }
            });*/

            upload_image_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // openImageDialog(CAMERA_RESULT_CODE_IMAGE_1,GALLERY_RESULT_CODE_IMAGE_1);
                   /* Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    c.startActivityForResult(intent, CAMERA_RESULT_CODE_IMAGE_1);*/

                    Intent intent = new Intent(c, MainActivity.class);
                    c.startActivity(intent);
                    photoClicked1 = true;
                    isImageCaptured1 = false;
                    capturedBitmap1 = null;


                   /* try {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.setPackage("com.apepdcl.gis.app");
                        Activity activity = (Activity) c;
                        File photoFile = createImageFile1();
                        Uri photoURI = FileProvider.getUriForFile(activity, "com.apepdcl.gis_survey.fileprovider", photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        activity.startActivityForResult(intent,  CAMERA_RESULT_CODE_IMAGE_1);

                    } catch (Exception e) {
                        e.printStackTrace();
                        showInstallDialog();
                    }*/




//                    try {
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        File photoFile = createImageFile1();
//                        if (photoFile != null) {
//
//                            Uri photoURI = FileProvider.getUriForFile(c, "com.apepdcl.gis_survey.fileprovider", photoFile);
//                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                        }
//                        c.startActivityForResult(intent, CAMERA_RESULT_CODE_IMAGE_1);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }


                }
            });

            upload_image_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(c, MainActivity.class);
                    c.startActivity(intent);
                    photoClicked2 = true;
                    isImageCaptured2 = false;
                    capturedBitmap2 = null;

                    /*try {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.setPackage("com.apepdcl.gis.app");
                        Activity activity = (Activity) c;
                        File photoFile = createImageFile2();
                        Uri photoURI = FileProvider.getUriForFile(activity, "com.apepdcl.gis_survey.fileprovider", photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        activity.startActivityForResult(intent,  CAMERA_RESULT_CODE_IMAGE_2);

                    } catch (Exception e) {
                        e.printStackTrace();
                        showInstallDialog();
                    }*/




//                    try {
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        File photoFile = createImageFile2();
//                        if (photoFile != null) {
//                            Uri photoURI = FileProvider.getUriForFile(c, "com.apepdcl.gis_survey.fileprovider", photoFile);
//                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                        }
//                        c.startActivityForResult(intent, CAMERA_RESULT_CODE_IMAGE_2);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                    /*Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    c.startActivityForResult(intent, CAMERA_RESULT_CODE_IMAGE_2);*/
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


            //CROSSING
            ch_crossing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        lyt_oh_crossing.setVisibility(View.VISIBLE);
                    } else {
                        lyt_oh_crossing.setVisibility(View.GONE);
                    }
                }
            });

            ch_oh_6_3_crossing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        lyt_oh_6_3_crossing.setVisibility(View.VISIBLE);
                       /* ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMISurveyHome33KV.feederList11kv);
                        edt_oh_6_3_crossing.setAdapter(adapter_feeder);
                        edt_oh_6_3_crossing.setThreshold(5);*/



                        edt_oh_6_3_crossing.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                open11KVFeederMasterDialog(edt_oh_6_3_crossing);
                            }
                        });

                    } else {
                        lyt_oh_6_3_crossing.setVisibility(View.GONE);
                    }
                }
            });

            ch_oh_11kv_crossing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        lyt_oh_11kv_crossing.setVisibility(View.VISIBLE);
                        /*ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMISurveyHome33KV.feederList11kv);
                        edt_oh_11kv_crossing.setAdapter(adapter_feeder);
                        edt_oh_11kv_crossing.setThreshold(5);*/



                        edt_oh_11kv_crossing.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                open11KVFeederMasterDialog(edt_oh_11kv_crossing);
                            }
                        });

                    } else {
                        lyt_oh_11kv_crossing.setVisibility(View.GONE);
                    }
                }
            });

            ch_oh_33kv_crossing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        lyt_oh_33kv_crossing.setVisibility(View.VISIBLE);
                        /*ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMISurveyHome33KV.feederList33kv);
                        edt_oh_33kv_crossing.setAdapter(adapter_feeder);
                        edt_oh_33kv_crossing.setThreshold(5);*/


                        edt_oh_33kv_crossing.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                open33KVFeederMasterDialog(edt_oh_33kv_crossing);
                            }
                        });

                    } else {
                        lyt_oh_33kv_crossing.setVisibility(View.GONE);
                    }
                }
            });

            ch_oh_lt_crossing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        lyt_oh_lt_crossing.setVisibility(View.VISIBLE);
                        /*ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMISurveyHome33KV.feederListLT);
                        edt_oh_lt_crossing.setAdapter(adapter_feeder);
                        edt_oh_lt_crossing.setThreshold(5);*/

                        SearchableSpinner searchableSpinner = new SearchableSpinner(c);
                        searchableSpinner.setWindowTitle("SELECT DTR CODE");
                        searchableSpinner.setSpinnerListItems(PMISurveyHome33KV.feederListLT);
                        searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                            @Override
                            public void setOnItemSelectListener(int position, String selectedString) {
                                edt_oh_lt_crossing.setText(selectedString);
                            }
                        });

                        edt_oh_lt_crossing.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                searchableSpinner.show();
                            }
                        });

                    } else {
                        lyt_oh_lt_crossing.setVisibility(View.GONE);
                    }
                }
            });

            ch_oh_road_crossing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        sp_crossing.setVisibility(View.VISIBLE);
                    } else {
                        sp_crossing.setVisibility(View.GONE);
                    }
                }
            });

            ch_oh_tc_crossing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        lyt_oh_tc_crossing.setVisibility(View.VISIBLE);
                    } else {
                        lyt_oh_tc_crossing.setVisibility(View.GONE);
                    }
                }
            });

            ch_oh_tm_crossing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        lyt_oh_tm_crossing.setVisibility(View.VISIBLE);
                    } else {
                        lyt_oh_tm_crossing.setVisibility(View.GONE);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showInstallDialog() {
        new AlertDialog.Builder(c)
                .setTitle("Open Camera Not Installed")
                .setMessage("To continue, please install the Open Camera app.")
                .setPositiveButton("Install", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Activity activity = (Activity) c;
                        try {
                            // Try opening Play Store directly
                            activity.startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=com.apepdcl.gis.app")));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            // Fallback to browser
                            activity.startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("https://play.google.com/store/apps/details?id=com.apepdcl.gis.app")));
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.btn_clear:
                    image_1_clicked = false;
                    image_2_clicked = false;
                    image_3_clicked = false;
                    repeatPoleClicked = false;
                    deligate.IsSetData("add Record Not Success", "1");
                    callout.dismiss();
                    graphicsOverlay.getGraphics().remove(graphic);
                    dismiss();
                    break;
                case R.id.btn_topclear:
                    image_1_clicked = false;
                    image_2_clicked = false;
                    image_3_clicked = false;
                    repeatPoleClicked = false;
                    callout.dismiss();
                    graphicsOverlay.getGraphics().remove(graphic);
                    dismiss();
                    break;
                case R.id.btn_repeat_pole:
                    Animation animation = AnimationUtils.loadAnimation(c, R.anim.bounce);
                    btn_repeat_pole.startAnimation(animation);
                    image_1_clicked = false;
                    image_2_clicked = false;
                    image_3_clicked = false;
                    repeatPoleClicked = true;
                    repeatPole();
                    break;
                case R.id.btn_add:

                    currentgraphicsOverlay.getGraphics().remove(currentGraphic);

                    if (ed_pole_no.getText().toString().isEmpty()) {
                        Toast.makeText(c, "Enter Pole No", Toast.LENGTH_LONG).show();
                    } else if (ed_frompole.getText().toString().isEmpty()) {
                        Toast.makeText(c, "Enter From Pole ", Toast.LENGTH_LONG).show();
                    } else {
                        if ("Overhead".equalsIgnoreCase(lineType)) {
                            if (defect_selected.equalsIgnoreCase("Yes")) {
                                validateDefects();
                            } else {
                                overHeadData();
                            }
                        } else if ("UnderGround".equalsIgnoreCase(lineType)) {
                            underGroundData();
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

    private void repeatPole() {
        PMISurveyVo surveyVo = repeatPoleList.get(repeatPoleList.size() - 1);

        //LINE TYPE
        String lineType = surveyVo.getLineType();
        List<String> list1 = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.line_type)));
        list1.remove(lineType);
        list1.add(0, lineType);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list1);
        sp_lineType.setAdapter(adapter1);
        sp_lineType.setSelection(0, true);

        //OVERHEAD
        if (lineType.equalsIgnoreCase("OverHead")) {
            //POLE HEIGHT
            poleHeightRepeat = surveyVo.getPole_height();

            //IS END POLE
            String isEndPole = surveyVo.getIsEndPole();
            List<String> list3 = new ArrayList<>();
            if (isEndPole.equals("N")) {
                list3.add("No");
                list3.add("Yes");
            } else {
                list3.add("Yes");
                list3.add("No");
            }

            ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list3);
            sp_isendpole.setAdapter(adapter3);
            sp_isendpole.setSelection(0, true);

            //POLE TYPE
            String poleType = surveyVo.getPole_type();
            List<String> list9 = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.pole_type)));
            list9.remove("Select");
            list9.remove(poleType);
            list9.add(0, poleType);
            ArrayAdapter<String> adapter9 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list9);
            sp_poletype.setAdapter(adapter9);

            //POLE SUPPORT
            String poleSupport = surveyVo.getPole_support();
            List<String> list_poleSupport = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.pole_support)));
            list_poleSupport.remove(poleSupport);
            list_poleSupport.add(0, poleSupport);
            ArrayAdapter<String> adapter_poleSupport = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_poleSupport);
            sp_polesupport.setAdapter(adapter_poleSupport);
            sp_polesupport.setSelection(0, true);

            //TAPPING POINT
            String tp = surveyVo.getTapping_point();
            List<String> list12 = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.taping_point)));
            list12.remove("Select");
            list12.remove(tp);
            list12.add(0, tp);
            ArrayAdapter<String> adapter12 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list12);
            sp_tapingpoint.setAdapter(adapter12);
            sp_tapingpoint.setSelection(0, true);


            if (tp.equalsIgnoreCase("Yes")) {

                //NO OF TAPPINGS
                String no_of_t = surveyVo.getNoofTappings();
                List<String> list_no_of_t = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.nofof_tappings)));
                list_no_of_t.remove("Select");
                list_no_of_t.remove(no_of_t);
                list_no_of_t.add(0, no_of_t);
                ArrayAdapter<String> adapter_no_of_t = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_no_of_t);
                sp_nooftappings.setAdapter(adapter_no_of_t);
                sp_nooftappings.setSelection(0, true);

                //TAPING LOCATION NAME

                not = surveyVo.getTappingLocation();
                edt_tappinglocation.setText(not);
            }

            //CONDUCTOR SIZE
            String conductorSize = surveyVo.getConductor_size();
            List<String> list4 = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.conductor_size)));
            list4.remove("Select");
            list4.remove(conductorSize);
            list4.add(0, conductorSize);
            ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list4);
            sp_conductorsize.setAdapter(adapter4);
            sp_conductorsize.setSelection(0, true);

            //PIN POINT
            String pinPoint_r = surveyVo.getPinpoint();
            List<String> list10 = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.pinpoint)));
            list10.remove(pinPoint_r);
            list10.add(0, pinPoint_r);
            ArrayAdapter<String> adapter10 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list10);
            sp_pinpoint.setAdapter(adapter10);
            sp_pinpoint.setSelection(0, true);

            //CUT POINT
            if (pinPoint_r.equals("Cutpoint")) {
                //POLE STRUCTURE
                String poleStructure = surveyVo.getPoleStructure();
                List<String> listpoleStructure = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.pole_structure)));
                listpoleStructure.remove(poleStructure);
                listpoleStructure.add(0, poleStructure);
                ArrayAdapter<String> adapterpoleStructure = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, listpoleStructure);
                sp__polestructure.setAdapter(adapterpoleStructure);
                sp__polestructure.setSelection(0, true);
            }

            //Is Multi Circuit
            String isMultiCircuit = surveyVo.getIsMultiCircuit();
            //  System.out.println("isMultiCircuit : " + isMultiCircuit);

            List<String> list5 = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.is_multicircuit_33kv)));
            list5.remove("Select");
            list5.remove(isMultiCircuit);
            list5.add(0, isMultiCircuit);
            ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list5);
            sp_ismulticircute.setAdapter(adapter5);
            sp_ismulticircute.setSelection(0, true);

            //No Of Circuits
            if (isMultiCircuit.equalsIgnoreCase("Yes")) {
                String noOfCircuits = surveyVo.getNoofCircuits();
                // System.out.println("NO OF CIRCUITS : "+noOfCircuits);
                List<String> list6 = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.noof_circuit_overhead)));
                list6.remove(noOfCircuits);
                list6.add(0, noOfCircuits);
                ArrayAdapter<String> adapter6 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list6);
                sp_noofcircuit.setAdapter(adapter6);
                sp_noofcircuit.setSelection(0, true);
            }

            //POLE CONCRETE
            String pole_concrete = surveyVo.getPOLE_CONCRETE();
            List<String> list_pole_concrete = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.pole_concrete)));
            list_pole_concrete.remove(pole_concrete);
            list_pole_concrete.remove("Select");
            list_pole_concrete.add(0, pole_concrete);
            ArrayAdapter<String> adapter_pole_concrete = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_pole_concrete);
            sp_pole_concrete.setAdapter(adapter_pole_concrete);
            sp_pole_concrete.setSelection(0, true);

        }

        //UNDER GROUND
        if (lineType.equalsIgnoreCase("UnderGround")) {
            //IS END POINT
            String isEndPole = surveyVo.getIsEndPole();
            List<String> list3 = new ArrayList<>();
            if (isEndPole.equals("N")) {
                list3.add("No");
                list3.add("Yes");
            } else {
                list3.add("Yes");
                list3.add("No");
            }
            ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list3);
            sp_undergisendpole.setAdapter(adapter3);
            sp_undergisendpole.setSelection(0, true);

            //NO OF CABLES
            String noc = surveyVo.getNoofCircuits();
            List<String> list2 = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.noof_circuit)));
            list2.remove(noc);
            list2.remove("Select");
            list2.add(0, noc);
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list2);
            sp_noofcircuit_underg.setAdapter(adapter2);
            sp_noofcircuit_underg.setSelection(0, true);


            //TAPPING POINT
            String tp = surveyVo.getTapping_point();
            List<String> list12 = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.taping_point)));
            list12.remove("Select");
            list12.remove(tp);
            list12.add(0, tp);
            ArrayAdapter<String> adapter12 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list12);
            sp_ugtapingpoint.setAdapter(adapter12);
            sp_ugtapingpoint.setSelection(0, true);


            if (tp.equalsIgnoreCase("Yes")) {
                //NO OF TAPPINGS
                String no_of_t = surveyVo.getNoofTappings();
                List<String> list_no_of_t = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.nofof_tappings)));
                list_no_of_t.remove("Select");
                list_no_of_t.remove(no_of_t);
                list_no_of_t.add(0, no_of_t);
                ArrayAdapter<String> adapter_no_of_t = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_no_of_t);
                sp_ugnooftappings.setAdapter(adapter_no_of_t);
                sp_ugnooftappings.setSelection(0, true);

                //TAPING LOCATION NAME
                not = surveyVo.getTappingLocation();
                edt_ugtappinglocation.setText(not);
            }


            //CABLE SIZE
            String cable_size = surveyVo.getConductor_size();
            // String CABLE_TX = "CABLE";
            // List<String> list1_cable_size = arcGISDatabase.getComplaint("Select CKT_VALUE from MULTIVOLTAGE_MASTERS_TABLE where CKT_NO = '" + CABLE_TX + "'" + " ORDER BY CKT_VALUE");
            List<String> list1_cable_size = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.cable_size)));
            list1_cable_size.remove("Select");
            list1_cable_size.remove(cable_size);
            list1_cable_size.add(0, cable_size);
            ArrayAdapter<String> adapter_cable_size = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list1_cable_size);
            sp_cablesize.setAdapter(adapter_cable_size);
            sp_cablesize.setSelection(0, true);
        }

    }

    private void validateDefects() {

        if (!ch_pole_defects.isChecked() && !ch_conductors.isChecked() && !ch_ab_switch_defects.isChecked()
                && !ch_insulators.isChecked() && !ch_top_cleat.isChecked() && !ch_restringing.isChecked() &&
                !ch_cross_arm.isChecked() && !ch_strut_pole.isChecked() && !ch_stray_wire.isChecked() &&
                !ch_tree_clearance.isChecked() &&
                !ch_ht_clearance.isChecked() && !ch_int_pole.isChecked() && !ch_other_defects.isChecked()) {
            Toast.makeText(c, "Please Select at least 1 Defect", Toast.LENGTH_SHORT).show();
            return;

        }


        if (ch_pole_defects.isChecked()) {
            if (sp_pole_defects.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(c, "Select Pole Defects", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (ch_conductors.isChecked()) {
            if (sp_conductors.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(c, "Select Conductor Defects", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (ch_ab_switch_defects.isChecked()) {
            if (sp_ab_switch_defects.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(c, "Select AB Switch Defects", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (ch_insulators.isChecked()) {
            if (sp_insulators.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(c, "Select Insulators Defects", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (ch_cross_arm.isChecked()) {
            if (sp_cross_arm.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(c, "Select Cross Arm Defects", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (ch_top_cleat.isChecked()) {
            if (sp_top_cleat.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(c, "Select Top Cleat Defects", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (ch_ab_switch_defects.isChecked()) {
            if (sp_ab_switch_defects.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(c, "Select  Defects", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (ch_strut_pole.isChecked()) {
            if (sp_strut_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(c, "Select Strut Pole Defects", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (ch_stray_wire.isChecked()) {
            if (sp_stray_wire.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(c, "Select Stay Wire Defects", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (ch_ht_clearance.isChecked()) {
            if (sp_ht_clearance.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(c, "Select HT/LT Clearance Defects", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (ch_int_pole.isChecked()) {
            if (sp_int_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(c, "Select Intermediate Pole Defects", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        //  ,,ch_other_defects;
        if (ch_restringing.isChecked()) {
            if (sp_restringing.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(c, "Select Restringing Defects", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (ch_tree_clearance.isChecked()) {
            if (sp_tree_clearance.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(c, "Select Tree Clearance Defects", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (ch_other_defects.isChecked()) {
            if (ed_other_defects.getText().toString().trim().isEmpty()) {
                Toast.makeText(c, "Please Enter Other Defects", Toast.LENGTH_SHORT).show();
                return;
            }

        }

        if (!image_1_clicked && !image_2_clicked && !image_3_clicked) {
            Toast.makeText(c, "Upload at least 1 photo", Toast.LENGTH_SHORT).show();
            return;
        }

        overHeadData();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        try {
            switch (adapterView.getId()) {
                case R.id.sp_poletype:
                    poleType = sp_poletype.getSelectedItem().toString();

                    if (repeatPoleClicked) {
                        if ("PSCC".equalsIgnoreCase(poleType)) {
                            List<String> psccList = new ArrayList<>();
                            // psccList.add("Select");
                            psccList.add("9.1");
                            psccList.add("11");
                            psccList.remove(poleHeightRepeat);
                            psccList.add(0, poleHeightRepeat);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, psccList);
                            sp_poleheight.setAdapter(adapter);
                        } else if ("SPUN".equalsIgnoreCase(poleType)) {
                            List<String> spunList = new ArrayList<>();
                            // spunList.add("Select");
                            spunList.add("9.5");
                            spunList.add("11");
                            spunList.add("12.5");
                            spunList.remove(poleHeightRepeat);
                            spunList.add(0, poleHeightRepeat);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, spunList);
                            sp_poleheight.setAdapter(adapter);

                        } else if ("RAIL".equalsIgnoreCase(poleType)) {
                            List<String> railList = new ArrayList<>();
                            // railList.add("Select");
                            railList.add("9.5");
                            railList.add("11");
                            railList.add("12.5");
                            railList.remove(poleHeightRepeat);
                            railList.add(0, poleHeightRepeat);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, railList);
                            sp_poleheight.setAdapter(adapter);

                        } else if ("RS JOIST".equalsIgnoreCase(poleType)) {
                            List<String> rssJoistList = new ArrayList<>();
                            // rssJoistList.add("Select");
                            /*rssJoistList.add("8");*/
                            rssJoistList.add("9.1");
                            rssJoistList.add("11");
                            rssJoistList.remove(poleHeightRepeat);
                            rssJoistList.add(0, poleHeightRepeat);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, rssJoistList);
                            sp_poleheight.setAdapter(adapter);
                        } else if ("TOWER".equalsIgnoreCase(poleType)) {
                            List<String> towertList = new ArrayList<>();
                            //  towertList.add("Select");
                            towertList.add("M+3");
                            towertList.add("M+6");
                            towertList.add("M+9");
                            towertList.add("M+12");
                            towertList.add("R+6");
                            towertList.remove(poleHeightRepeat);
                            towertList.add(0, poleHeightRepeat);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, towertList);
                            sp_poleheight.setAdapter(adapter);
                        } else if ("Tubular Pole".equalsIgnoreCase(poleType)) {

                            List<String> tubularList = new ArrayList<>();
                            // tubularList.add("Select");
                            tubularList.add("9.1");
                            tubularList.add("11");
                            tubularList.remove(poleHeightRepeat);
                            tubularList.add(0, poleHeightRepeat);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, tubularList);
                            sp_poleheight.setAdapter(adapter);
                        }
                    } else {
                        if ("PSCC".equalsIgnoreCase(poleType)) {
                            // String test= (String) mapData.get("POLE_TYPE");
                            List<String> psccList = new ArrayList<>();
                            psccList.add("Select");
                            psccList.add("9.1");
                            psccList.add("11");
                            // psccList.add(test);
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



                   /* if ("PSCC".equalsIgnoreCase(poleType)) {
                        // String test= (String) mapData.get("POLE_TYPE");
                        List<String> psccList = new ArrayList<>();
                        psccList.add("Select");
                        psccList.add("9.1");
                        psccList.add("11");
                        // psccList.add(test);
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
                        *//*rssJoistList.add("8");*//*
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
*/
                    break;
                case R.id.sp_poleheight:
                    poleHeight = sp_poleheight.getSelectedItem().toString();
                    repeatPoleClicked = false;
                    break;
                case R.id.sp_polesupport:
                    poleSupport = sp_polesupport.getSelectedItem().toString();
                    break;
                case R.id.sp_tapingpoint:
                    taping_Point = sp_tapingpoint.getSelectedItem().toString();
                    if ("Yes".equalsIgnoreCase(taping_Point)) {
                        lyt_tapping.setVisibility(View.VISIBLE);
                        if (repeatPoleClicked) {
                            edt_tappinglocation.setText(not);
                        }
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
                    /*if ("Line-Pole".equalsIgnoreCase(poleconfiguration)) {
                        images_layout.setVisibility(View.GONE);
                    } else {
                        images_layout.setVisibility(View.VISIBLE);
                    }*/

                    if ("33/11kv SS-Bay".equalsIgnoreCase(poleconfiguration)) {
                        lyt_oh_htscnum.setVisibility(View.GONE);
                        lyt_ssname.setVisibility(View.VISIBLE);
                        //List<String> sunStationName=arcGISDatabase.getFeederId(String.format("SELECT DISTINCT EHT_SS_NAME FROM SSMASTER_TABLE WHERE FEEDER_CODE_33KV='%s'",feederCode));
                        List<String> sunStationName = arcGISDatabase.getSSNAMEList();
                        sunStationName.add("Select");
                        Collections.reverse(sunStationName);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, sunStationName);
                        sp_ssname.setAdapter(adapter);

                    } else if ("HT Service-Pole".equalsIgnoreCase(poleconfiguration)) {
                        lyt_oh_htscnum.setVisibility(View.VISIBLE);
                        lyt_ssname.setVisibility(View.GONE);

                    } else {
                        lyt_ssname.setVisibility(View.GONE);
                        lyt_oh_htscnum.setVisibility(View.GONE);
                        ssname = "";
                    }

                    break;
                case R.id.sp_ssname:
                    ssname = sp_ssname.getSelectedItem().toString();
                    if (!"Select".equalsIgnoreCase(ssname) && !ssname.isEmpty()) {
                        ssId = arcGISDatabase.getFeederId(String.format("SELECT EHT_SS_CODE FROM SSMASTER_TABLE WHERE EHT_SS_NAME='%s'", ssname));
                    }
                    break;

                case R.id.sp__polestructure:
                    polestructure = sp__polestructure.getSelectedItem().toString();
                    break;
                case R.id.sp_ismulticircute:
                    ismulticircute = sp_ismulticircute.getSelectedItem().toString();
                    if ("Yes".equalsIgnoreCase(ismulticircute)) {
                        lyt_circuit.setVisibility(View.VISIBLE);
                        lyt_diffvoltage.setVisibility(View.GONE);
                        sp_circutetype.setSelection(0);
                        sp_noofcircuit.setSelection(0);

                    } else {
                        sp_circutetype.setSelection(0);
                        lyt_oh_multivoltage.setVisibility(View.GONE);
                        lyt_circuit.setVisibility(View.GONE);
                        lyt_diffvoltage.setVisibility(View.GONE);
                        sp_noofcircuit.setSelection(0);
                    }
                    break;
                case R.id.sp_noofcircuit:
                    noofcircuit = sp_noofcircuit.getSelectedItem().toString();

                    if ("Select".equalsIgnoreCase(noofcircuit)) {
                        lyt_oh_multivoltage.setVisibility(View.GONE);
                    } else {
                        lyt_oh_multivoltage.setVisibility(View.VISIBLE);
                    }

                    if ("2".equalsIgnoreCase(noofcircuit)) {
                        List<String> twoCircuits = new ArrayList<>();
                        // twoCircuits.add("Select");
                        twoCircuits.add("11kv");
                        twoCircuits.add("LT");
                        twoCircuits.add("6.3kv");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, twoCircuits);
                        sp_diffvoltage.setAdapter(adapter);
                    } else if ("3".equalsIgnoreCase(noofcircuit)) {
                        List<String> threeCircuits = new ArrayList<>();
                        // threeCircuits.add("Select");
                        threeCircuits.add("(33 kv-1)+(11 kv-1)");
                        threeCircuits.add("11 kv-2");
                        threeCircuits.add("(11 kv-1)+(LT-1)");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, threeCircuits);
                        sp_diffvoltage.setAdapter(adapter);
                    } else if ("4".equalsIgnoreCase(noofcircuit)) {
                        List<String> fourCircuits = new ArrayList<>();
                        // fourCircuits.add("Select");
                        fourCircuits.add("(33 kv-2) +( 11kv-1)");
                        fourCircuits.add("(33 kV- 1)+(11kv-2)");
                        fourCircuits.add("(33 kv-1)+(11 kv-1)+(LT-1)");
                        fourCircuits.add("11 kv-3");
                        fourCircuits.add("(11 kv-2)+(LT-1)");
                        fourCircuits.add("(33 kv-2)+(LT-1)");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, fourCircuits);
                        sp_diffvoltage.setAdapter(adapter);
                    } else if ("5".equalsIgnoreCase(noofcircuit)) {
                        List<String> fiveCircuits = new ArrayList<>();
                        // fiveCircuits.add("Select");
                        fiveCircuits.add("(33 kv-2)+(11 kv-1)+(LT-1)");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, fiveCircuits);
                        sp_diffvoltage.setAdapter(adapter);
                    }

                    break;
                case R.id.sp_circutetype:
                    circutetype = sp_circutetype.getSelectedItem().toString();
                    if ("Multi Voltage".equalsIgnoreCase(circutetype)) {
                        lyt_diffvoltage.setVisibility(View.GONE);
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
                    } else if ("UnderGround".equalsIgnoreCase(lineType)) {
                        lyt_underground.setVisibility(View.VISIBLE);
                        lyt_overhead.setVisibility(View.GONE);
                    }
                    break;

                case R.id.sp_oh_other_cables:
                    if (sp_oh_other_cables.getSelectedItem().toString().equals("Yes")) {
                        edt_oh_other_cables.setVisibility(View.VISIBLE);
                    } else {
                        edt_oh_other_cables.setVisibility(View.GONE);
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
                    } else if ("Structure".equalsIgnoreCase(straight_rmu)) {
                        lyt_underpolestructure.setVisibility(View.VISIBLE);
                        lyt_ugpole.setVisibility(View.GONE);
                        lyt_rmu.setVisibility(View.GONE);
                        sp_rmu.setSelection(0);
                        sp__underpolestructure.setSelection(0);
                    } else if ("Pole".equalsIgnoreCase(straight_rmu)) {
                        sp_rmu.setSelection(0);
                        sp__underpolestructure.setSelection(0);
                        lyt_ugpole.setVisibility(View.VISIBLE);
                        lyt_rmu.setVisibility(View.GONE);
                        lyt_underpolestructure.setVisibility(View.GONE);
                    } else {
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
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, ugpsccList);
                        sp_ugpoleheight.setAdapter(adapter);

                    } else if ("SPUN".equalsIgnoreCase(ugpoletype)) {
                        lyt_ugssname.setVisibility(View.GONE);
                        List<String> ugspunList = new ArrayList<>();
                        ugspunList.add("Select");
                        ugspunList.add("9.5");
                        ugspunList.add("11");
                        ugspunList.add("12.5");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, ugspunList);
                        sp_ugpoleheight.setAdapter(adapter);

                    } else if ("RAIL".equalsIgnoreCase(ugpoletype)) {
                        lyt_ugssname.setVisibility(View.GONE);
                        List<String> ugrailList = new ArrayList<>();
                        ugrailList.add("Select");
                        ugrailList.add("9.5");
                        ugrailList.add("11");
                        ugrailList.add("12.5");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, ugrailList);
                        sp_ugpoleheight.setAdapter(adapter);

                    } else if ("RS JOIST".equalsIgnoreCase(ugpoletype)) {
                        lyt_ugssname.setVisibility(View.GONE);
                        List<String> ugrssJoistList = new ArrayList<>();
                        ugrssJoistList.add("Select");
                        /*rssJoistList.add("8");*/
                        ugrssJoistList.add("9.1");
                        ugrssJoistList.add("11");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, ugrssJoistList);
                        sp_ugpoleheight.setAdapter(adapter);
                    } else if ("TOWER".equalsIgnoreCase(ugpoletype)) {
                        lyt_ugssname.setVisibility(View.GONE);
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
                        lyt_ugssname.setVisibility(View.GONE);
                        List<String> ugtubularList = new ArrayList<>();
                        ugtubularList.add("Select");
                        ugtubularList.add("9.1");
                        ugtubularList.add("11");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, ugtubularList);
                        sp_ugpoleheight.setAdapter(adapter);
                    } else if ("SS Bay".equalsIgnoreCase(ugpoletype)) {
                        lyt_ugssname.setVisibility(View.VISIBLE);
                        List<String> ugSSName = arcGISDatabase.getSSNAMEList();
                        ugSSName.add("Select");
                        Collections.reverse(ugSSName);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, ugSSName);
                        sp_ugssname.setAdapter(adapter);
                    }

                    break;

                case R.id.sp_ugpoleheight:
                    ugpoleheight = sp_ugpoleheight.getSelectedItem().toString();

                    break;

                case R.id.sp_ugpolesupport:
                    ugpolesupport = sp_ugpolesupport.getSelectedItem().toString();

                    break;
                case R.id.sp_ugssname:
                    ugssname = sp_ugssname.getSelectedItem().toString();

                    break;

                case R.id.sp_ugtapingpoint:
                    ugtapingpoint = sp_ugtapingpoint.getSelectedItem().toString();
                    if ("Yes".equalsIgnoreCase(ugtapingpoint)) {
                        lyt_ugtapping.setVisibility(View.VISIBLE);
                        if (repeatPoleClicked) {
                            edt_ugtappinglocation.setText(not);
                        }
                    } else {
                        lyt_ugtapping.setVisibility(View.GONE);
                        ugnooftappings = "";
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

                case R.id.sp_Survey_type:
                    Survey_type_string = sp_Survey_type.getSelectedItem().toString();

                    break;

                case R.id.sp_pole_concrete:
                    pole_concrete = sp_pole_concrete.getSelectedItem().toString();

                    break;


                default:

                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void overHeadData() {
        try {

            if ("Select".equalsIgnoreCase(Survey_type_string)) {
                Toast.makeText(c, "Select Survey Type", Toast.LENGTH_LONG).show();
            }else if ("Select".equalsIgnoreCase(sp_ismulticircute.getSelectedItem().toString())) {
                Toast.makeText(c, "Select Is Multi Circuit", Toast.LENGTH_LONG).show();
            }

            else if ("Select".equalsIgnoreCase(poleType)) {
                Toast.makeText(c, "Select Pole Type", Toast.LENGTH_LONG).show();
            }

           /* else if ("TOWER".equalsIgnoreCase(poleType) && lastPoleDistance >200) {
                Toast.makeText(c, "Abnormal Distance\n Distance below 200 meters\nis allowed for TOWER", Toast.LENGTH_LONG).show();
            }

            else if (!"TOWER".equalsIgnoreCase(poleType) && lastPoleDistance >150) {
                Toast.makeText(c, "Abnormal Distance\nDistance below 150 meters\nis allowed", Toast.LENGTH_LONG).show();
            }*/

            else if ("Select".equalsIgnoreCase(poleHeight)) {
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
            }

           /* else if (!"33/11kv SS-Bay".equalsIgnoreCase(poleconfiguration) && !"EHT SS-Bay".equalsIgnoreCase(poleconfiguration)
                    && PMIArcGISMapActivity33KV.isFirstPole) {
                Toast.makeText(c, "First Pole Should be SS Bay", Toast.LENGTH_LONG).show();
            }
*/
          /*  else if ("33/11kv SS-Bay".equalsIgnoreCase(poleconfiguration) && ! PMIArcGISMapActivity33KV.isFirstPole) {
                Toast.makeText(c, "Only First Pole can be SS Bay", Toast.LENGTH_LONG).show();
            }
*/
            else if ("33/11kv SS-Bay".equalsIgnoreCase(poleconfiguration) && "Select".equalsIgnoreCase(sp_ssname.getSelectedItem().toString())) {
                Toast.makeText(c, "Select SS Name", Toast.LENGTH_LONG).show();
            } else if ("HT Service-Pole".equalsIgnoreCase(poleconfiguration) && edt_oh_htscnum_overhead.getText().toString().isEmpty()) {
                Toast.makeText(c, "Enter HT ServiceNumber", Toast.LENGTH_LONG).show();
            } else if ("Yes".equalsIgnoreCase(ismulticircute) && "Select".equalsIgnoreCase(noofcircuit)) {
                Toast.makeText(c, "Select No.Of Circuits", Toast.LENGTH_LONG).show();
            } else if ("Yes".equalsIgnoreCase(ismulticircute) && "Select".equalsIgnoreCase(sp_circutetype.getSelectedItem().toString())) {
                Toast.makeText(c, "Select Circuit Type", Toast.LENGTH_LONG).show();
            } else if ("Yes".equalsIgnoreCase(ismulticircute) && "Multi Voltage".equalsIgnoreCase(circutetype) && "Select".equalsIgnoreCase(sp_diffvoltage.getSelectedItem().toString())) {
                Toast.makeText(c, "Select Diff Voltage", Toast.LENGTH_LONG).show();
            }


            //CROSSING

            else if (ch_crossing.isChecked() && !ch_oh_6_3_crossing.isChecked() && !ch_oh_11kv_crossing.isChecked() && !ch_oh_road_crossing.isChecked() &&
                    !ch_oh_33kv_crossing.isChecked() && !ch_oh_lt_crossing.isChecked() && !ch_oh_tc_crossing.isChecked() &&
                    !ch_oh_tm_crossing.isChecked()) {
                //"".equalsIgnoreCase(edt_oh_33kv_vlckts.getText().toString()) && "".equalsIgnoreCase(edt_oh_11kv_vlckts.getText().toString()) && "".equalsIgnoreCase(edt_oh_6_3kv_vlckts.getText().toString()) && "".equalsIgnoreCase(edt_oh_lt_vlckts.getText().toString())){
                Toast.makeText(c, "Select Any One Of Crossing", Toast.LENGTH_SHORT).show();
            } else if (ch_crossing.isChecked() && ch_oh_6_3_crossing.isChecked() && "".equalsIgnoreCase(edt_oh_6_3_crossing.getText().toString())) {
                Toast.makeText(c, "Enter Feeder ID of 6.3KV Crossings", Toast.LENGTH_SHORT).show();
            } else if (ch_crossing.isChecked() && ch_oh_6_3_crossing.isChecked() && "0".equalsIgnoreCase(edt_oh_6_3_crossing.getText().toString())) {
                Toast.makeText(c, "Enter Feeder ID of 6.3KV Crossings", Toast.LENGTH_SHORT).show();
            } else if (ch_crossing.isChecked() && ch_oh_11kv_crossing.isChecked() && "".equalsIgnoreCase(edt_oh_11kv_crossing.getText().toString())) {
                Toast.makeText(c, "Enter Feeder ID of 11kv Crossings", Toast.LENGTH_SHORT).show();
            } else if (ch_crossing.isChecked() && ch_oh_11kv_crossing.isChecked() && "0".equalsIgnoreCase(edt_oh_11kv_crossing.getText().toString())) {
                Toast.makeText(c, "Enter Feeder ID of 11kv Crossings", Toast.LENGTH_SHORT).show();
            } else if (ch_crossing.isChecked() && ch_oh_33kv_crossing.isChecked() && "".equalsIgnoreCase(edt_oh_33kv_crossing.getText().toString())) {
                Toast.makeText(c, "Enter Feeder ID of 33kv Crossings", Toast.LENGTH_SHORT).show();
            } else if (ch_crossing.isChecked() && ch_oh_33kv_crossing.isChecked() && "0".equalsIgnoreCase(edt_oh_33kv_crossing.getText().toString())) {
                Toast.makeText(c, "Enter Feeder ID of 33kv Crossings", Toast.LENGTH_SHORT).show();
            } else if (ch_crossing.isChecked() && ch_oh_lt_crossing.isChecked() && "".equalsIgnoreCase(edt_oh_lt_crossing.getText().toString())) {
                Toast.makeText(c, "Enter DTR Code of LT Crossings", Toast.LENGTH_SHORT).show();
            } else if (ch_crossing.isChecked() && ch_oh_lt_crossing.isChecked() && "0".equalsIgnoreCase(edt_oh_lt_crossing.getText().toString())) {
                Toast.makeText(c, "Enter DTR Code of LT Crossings", Toast.LENGTH_SHORT).show();
            } else if (ch_crossing.isChecked() && ch_oh_tc_crossing.isChecked() && "".equalsIgnoreCase(edt_oh_tc_crossing.getText().toString())) {
                Toast.makeText(c, "Enter No. of Tele Communication Crossings", Toast.LENGTH_SHORT).show();
            } else if (ch_crossing.isChecked() && ch_oh_tc_crossing.isChecked() && "0".equalsIgnoreCase(edt_oh_tc_crossing.getText().toString())) {
                Toast.makeText(c, "Enter No. of Tele Communication Crossings", Toast.LENGTH_SHORT).show();
            } else if (ch_crossing.isChecked() && ch_oh_tm_crossing.isChecked() && "".equalsIgnoreCase(edt_oh_tm_crossing.getText().toString())) {
                Toast.makeText(c, "Enter No. of Transmission Crossings", Toast.LENGTH_SHORT).show();
            } else if (ch_crossing.isChecked() && ch_oh_tm_crossing.isChecked() && "0".equalsIgnoreCase(edt_oh_tm_crossing.getText().toString())) {
                Toast.makeText(c, "Enter No. of Transmission Crossings", Toast.LENGTH_SHORT).show();
            }


            //NEW MULTI CKT


            else if (validate33KVVoltageCircuit()) {

            } else if (validate11KVVoltageCircuit()) {

            } else if (validateLTVoltageCircuit()) {

            } else if (validateNoOfCircuits()) {
                Toast.makeText(c, "No of circuits and selected circuits are not matching", Toast.LENGTH_SHORT).show();
            } else if (sp_oh_other_cables.getSelectedItem().toString().equalsIgnoreCase("Yes") && edt_oh_other_cables.getText().toString().isEmpty()) {
                Toast.makeText(c, "No. of Other Cables", Toast.LENGTH_SHORT).show();
            } else if (sp_oh_other_cables.getSelectedItem().toString().equalsIgnoreCase("Yes") && Integer.parseInt(edt_oh_other_cables.getText().toString()) == 0) {
                Toast.makeText(c, "No. of Other Cables should be greater than 0", Toast.LENGTH_SHORT).show();
            } else if (sp_oh_other_cables.getSelectedItem().toString().equalsIgnoreCase("Yes") && Integer.parseInt(edt_oh_other_cables.getText().toString()) > 20) {
                Toast.makeText(c, "No. of Other Cables should not be greater than 20", Toast.LENGTH_SHORT).show();
            } else if (!"Line-Pole".equalsIgnoreCase(poleconfiguration) && !image_1_clicked && !image_2_clicked && !image_3_clicked) {
                Toast.makeText(c, "Please Upload an Image", Toast.LENGTH_LONG).show();
            }else if ("Select".equalsIgnoreCase(sp_oh_defects.getSelectedItem().toString())) {
                Toast.makeText(c, "Select Defects", Toast.LENGTH_LONG).show();
            }
            else if (edt_polepaintingcode.getText().toString().isEmpty()){
                Toast.makeText(c, "Please Enter Pole Painting Code", Toast.LENGTH_SHORT).show();
            }
            else if ("Select".equalsIgnoreCase(sp_pole_concrete.getSelectedItem().toString())) {
                Toast.makeText(c, "Select Pole Concrete", Toast.LENGTH_LONG).show();
            } else {
                PMISurveyVo surveyVo = new PMISurveyVo();
                if (feederCode != null & !feederCode.isEmpty() && !ed_pole_no.getText().toString().isEmpty() && !ed_frompole.getText().toString().isEmpty() && !x.isEmpty() && !y.isEmpty()) {

                    if (Integer.parseInt(ed_pole_no.getText().toString()) == Integer.parseInt(ed_frompole.getText().toString())) {
                        Toast.makeText(c, "Pole No and From Pole No Should Not Be Same Contact Admin", Toast.LENGTH_LONG).show();

                    } else {
                        List<String> woID = arcGISDatabase.getIncrementalFeederComplaint(String.format("SELECT DISTINCT WO_ID FROM EHTMASTER_TABLE WHERE FEEDER_CODE_33KV='%s'", feederCode));

                        List<String> isFeederType = arcGISDatabase.getIncrementalFeederComplaint(String.format("SELECT DISTINCT IS_FEEDER_TYPE FROM EHTMASTER_TABLE WHERE FEEDER_CODE_33KV='%s'", feederCode));
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
                        if (!edt_polepaintingcode.getText().toString().isEmpty()) {
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
                       /* if (!"Select".equalsIgnoreCase(sp_circutetype.getSelectedItem().toString())) {
                            surveyVo.setCircuitType(circutetype);
                        } else {
                            surveyVo.setCircuitType("");
                        }*/
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

                        if (!polestructure.isEmpty()) {
                            surveyVo.setPoleStructure(polestructure);
                        }
                        //surveyVo.setDiffVoltage(diffvoltage);
                        surveyVo.setSsId(ssId);
                        surveyVo.setNoofTappings(nooftappings);
                        if (!edt_tappinglocation.getText().toString().isEmpty()) {
                            surveyVo.setTappingLocation(edt_tappinglocation.getText().toString());
                        }
                        if (!edt_remarks_overhead.getText().toString().isEmpty()) {
                            surveyVo.setRemarks(edt_remarks_overhead.getText().toString());
                        }
                        surveyVo.setWoId(woID.get(0));
                        surveyVo.setIsFeederType(isFeederType.get(0));
                        if ("HT Service-Pole".equalsIgnoreCase(poleconfiguration)) {
                            surveyVo.setHt_service_number(edt_oh_htscnum_overhead.getText().toString());
                        }
                        // surveyVo.setSurvaytype(Survey_type_string);
                        surveyVo.setSurvaytype("");


                        //MULTI CKT START

                        if ("Yes".equalsIgnoreCase(ismulticircute)) {

                            surveyVo.setIsMultiCircuit(ismulticircute);
                            surveyVo.setNO_OF33KV_CKTS("" + noof_33kvCkts);
                            surveyVo.setNO_OF11KV_CKTS("" + noof_11kvCkts);
                            surveyVo.setNO_OF_LT_CKTS("" + noof_ltCkts);
                            surveyVo.setCircuitType("Multi Voltage");

                            if (ch_oh_33kv_vlckts.isChecked()) {
                                if (sp_oh_no_of_33_feeders.getSelectedItem().toString().equals("1")) {
                                    if (sp_oh_33_feeder_1.getSelectedItem().toString().equals("Other than List")) {
                                        surveyVo.setMC_33KV_FEEDER_ID_1(ed_oh_33_feeder_1.getText().toString().split("~")[0]);
                                    } else if (sp_oh_33_feeder_1.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                        surveyVo.setMC_33KV_FEEDER_ID_1(ed_oh_33_feeder_1_ns.getText().toString().split("~")[0]);
                                    } else {
                                        surveyVo.setMC_33KV_FEEDER_ID_1(sp_oh_33_feeder_1.getSelectedItem().toString());
                                    }

                                    if (sp_oh_33_feeder_1.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                        surveyVo.setMC_33KV_POLE_1("NOT SURVEYED");
                                    } else if (sp_oh_33_feeder_1_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                        surveyVo.setMC_33KV_POLE_1(ed_oh_33_pole_1.getText().toString());
                                        surveyVo.setMC_33KV_NOT_IN_LIST_1("Y");
                                    } else {
                                        surveyVo.setMC_33KV_POLE_1(sp_oh_33_feeder_1_pole.getSelectedItem().toString());
                                    }

                                    surveyVo.setMC_33KV_FEEDER_ID_2("N");
                                    surveyVo.setMC_33KV_POLE_2("N");
                                    surveyVo.setMC_33KV_FEEDER_ID_3("N");
                                    surveyVo.setMC_33KV_POLE_3("N");

                                }

                                if (sp_oh_no_of_33_feeders.getSelectedItem().toString().equals("2")) {
                                    //FEEDER 1
                                    if (sp_oh_33_feeder_1.getSelectedItem().toString().equals("Other than List")) {
                                        surveyVo.setMC_33KV_FEEDER_ID_1(ed_oh_33_feeder_1.getText().toString().split("~")[0]);
                                    } else if (sp_oh_33_feeder_1.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                        surveyVo.setMC_33KV_FEEDER_ID_1(ed_oh_33_feeder_1_ns.getText().toString().split("~")[0]);
                                    } else {
                                        surveyVo.setMC_33KV_FEEDER_ID_1(sp_oh_33_feeder_1.getSelectedItem().toString());
                                    }

                                    if (sp_oh_33_feeder_1.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                        surveyVo.setMC_33KV_POLE_1("NOT SURVEYED");
                                    } else if (sp_oh_33_feeder_1_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                        surveyVo.setMC_33KV_POLE_1(ed_oh_33_pole_1.getText().toString());
                                        surveyVo.setMC_33KV_NOT_IN_LIST_1("Y");
                                    } else {
                                        surveyVo.setMC_33KV_POLE_1(sp_oh_33_feeder_1_pole.getSelectedItem().toString());
                                    }

                                    //FEEDER 2
                                    if (sp_oh_33_feeder_2.getSelectedItem().toString().equals("Other than List")) {
                                        surveyVo.setMC_33KV_FEEDER_ID_2(ed_oh_33_feeder_2.getText().toString().split("~")[0]);
                                    } else if (sp_oh_33_feeder_2.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                        surveyVo.setMC_33KV_FEEDER_ID_2(ed_oh_33_feeder_2_ns.getText().toString().split("~")[0]);
                                    } else {
                                        surveyVo.setMC_33KV_FEEDER_ID_2(sp_oh_33_feeder_2.getSelectedItem().toString());
                                    }


                                    if (sp_oh_33_feeder_2.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                        surveyVo.setMC_33KV_POLE_2("NOT SURVEYED");
                                    } else if (sp_oh_33_feeder_2_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                        surveyVo.setMC_33KV_POLE_2(ed_oh_33_pole_2.getText().toString());
                                        surveyVo.setMC_33KV_NOT_IN_LIST_2("Y");
                                    } else {
                                        surveyVo.setMC_33KV_POLE_2(sp_oh_33_feeder_2_pole.getSelectedItem().toString());
                                    }

                                    surveyVo.setMC_33KV_FEEDER_ID_3("N");
                                    surveyVo.setMC_33KV_POLE_3("N");

                                }


                                if (sp_oh_no_of_33_feeders.getSelectedItem().toString().equals("3")) {

                                    //FEEDER 1
                                    if (sp_oh_33_feeder_1.getSelectedItem().toString().equals("Other than List")) {
                                        surveyVo.setMC_33KV_FEEDER_ID_1(ed_oh_33_feeder_1.getText().toString().split("~")[0]);
                                    } else if (sp_oh_33_feeder_1.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                        surveyVo.setMC_33KV_FEEDER_ID_1(ed_oh_33_feeder_1_ns.getText().toString().split("~")[0]);
                                    } else {
                                        surveyVo.setMC_33KV_FEEDER_ID_1(sp_oh_33_feeder_1.getSelectedItem().toString());
                                    }

                                    if (sp_oh_33_feeder_1.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                        surveyVo.setMC_33KV_POLE_1("NOT SURVEYED");
                                    } else if (sp_oh_33_feeder_1_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                        surveyVo.setMC_33KV_POLE_1(ed_oh_33_pole_1.getText().toString());
                                        surveyVo.setMC_33KV_NOT_IN_LIST_1("Y");
                                    } else {
                                        surveyVo.setMC_33KV_POLE_1(sp_oh_33_feeder_1_pole.getSelectedItem().toString());
                                    }

                                    //FEEDER 2
                                    if (sp_oh_33_feeder_2.getSelectedItem().toString().equals("Other than List")) {
                                        surveyVo.setMC_33KV_FEEDER_ID_2(ed_oh_33_feeder_2.getText().toString().split("~")[0]);
                                    } else if (sp_oh_33_feeder_2.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                        surveyVo.setMC_33KV_FEEDER_ID_2(ed_oh_33_feeder_2_ns.getText().toString().split("~")[0]);
                                    } else {
                                        surveyVo.setMC_33KV_FEEDER_ID_2(sp_oh_33_feeder_2.getSelectedItem().toString());
                                    }


                                    if (sp_oh_33_feeder_2.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                        surveyVo.setMC_33KV_POLE_2("NOT SURVEYED");
                                    } else if (sp_oh_33_feeder_2_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                        surveyVo.setMC_33KV_POLE_2(ed_oh_33_pole_2.getText().toString());
                                        surveyVo.setMC_33KV_NOT_IN_LIST_2("Y");
                                    } else {
                                        surveyVo.setMC_33KV_POLE_2(sp_oh_33_feeder_2_pole.getSelectedItem().toString());
                                    }

                                    //FEEDER 3
                                    if (sp_oh_33_feeder_3.getSelectedItem().toString().equals("Other than List")) {
                                        surveyVo.setMC_33KV_FEEDER_ID_3(ed_oh_33_feeder_3.getText().toString().split("~")[0]);
                                    } else if (sp_oh_33_feeder_3.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                        surveyVo.setMC_33KV_FEEDER_ID_3(ed_oh_33_feeder_3_ns.getText().toString().split("~")[0]);
                                    } else {
                                        surveyVo.setMC_33KV_FEEDER_ID_3(sp_oh_33_feeder_3.getSelectedItem().toString());
                                    }

                                    if (sp_oh_33_feeder_3.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                        surveyVo.setMC_33KV_POLE_3("NOT SURVEYED");
                                    } else if (sp_oh_33_feeder_3_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                        surveyVo.setMC_33KV_POLE_3(ed_oh_33_pole_3.getText().toString());
                                        surveyVo.setMC_33KV_NOT_IN_LIST_3("Y");
                                    } else {
                                        surveyVo.setMC_33KV_POLE_3(sp_oh_33_feeder_3_pole.getSelectedItem().toString());
                                    }

                                }
                            } else {
                                surveyVo.setMC_33KV_FEEDER_ID_1("N");
                                surveyVo.setMC_33KV_POLE_1("N");
                                surveyVo.setMC_33KV_FEEDER_ID_2("N");
                                surveyVo.setMC_33KV_POLE_2("N");
                                surveyVo.setMC_33KV_FEEDER_ID_3("N");
                                surveyVo.setMC_33KV_POLE_3("N");
                            }

                            //11KV FEEDER
                            if (ch_oh_11kv_vlckts.isChecked()) {
                                if (sp_oh_no_of_11_feeders.getSelectedItem().toString().equals("1")) {
                                    if (sp_oh_11_feeder_1.getSelectedItem().toString().equals("Other than List")) {
                                        surveyVo.setMC_11KV_FEEDER_ID_1(ed_oh_11_feeder_1.getText().toString().split("~")[0]);
                                    } else if (sp_oh_11_feeder_1.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                        surveyVo.setMC_11KV_FEEDER_ID_1(ed_oh_11_feeder_1_ns.getText().toString().split("~")[0]);
                                    } else {
                                        surveyVo.setMC_11KV_FEEDER_ID_1(sp_oh_11_feeder_1.getSelectedItem().toString());
                                    }


                                    if (sp_oh_11_feeder_1.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                        surveyVo.setMC_11KV_POLE_1("NOT SURVEYED");
                                    } else if (sp_oh_11_feeder_1_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                        surveyVo.setMC_11KV_POLE_1(ed_oh_11_pole_1.getText().toString());
                                        surveyVo.setMC_11KV_NOT_IN_LIST_1("Y");
                                    } else {
                                        surveyVo.setMC_11KV_POLE_1(sp_oh_11_feeder_1_pole.getSelectedItem().toString());
                                    }

                                    surveyVo.setMC_11KV_FEEDER_ID_2("N");
                                    surveyVo.setMC_11KV_POLE_2("N");
                                    surveyVo.setMC_11KV_FEEDER_ID_3("N");
                                    surveyVo.setMC_11KV_POLE_3("N");

                                }

                                if (sp_oh_no_of_11_feeders.getSelectedItem().toString().equals("2")) {
                                    //FEEDER 1
                                    if (sp_oh_11_feeder_1.getSelectedItem().toString().equals("Other than List")) {
                                        surveyVo.setMC_11KV_FEEDER_ID_1(ed_oh_11_feeder_1.getText().toString().split("~")[0]);
                                    } else if (sp_oh_11_feeder_1.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                        surveyVo.setMC_11KV_FEEDER_ID_1(ed_oh_11_feeder_1_ns.getText().toString().split("~")[0]);
                                    } else {
                                        surveyVo.setMC_11KV_FEEDER_ID_1(sp_oh_11_feeder_1.getSelectedItem().toString());
                                    }

                                    if (sp_oh_11_feeder_1.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                        surveyVo.setMC_11KV_POLE_1("NOT SURVEYED");
                                    } else if (sp_oh_11_feeder_1_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                        surveyVo.setMC_11KV_POLE_1(ed_oh_11_pole_1.getText().toString());
                                        surveyVo.setMC_11KV_NOT_IN_LIST_1("Y");
                                    } else {
                                        surveyVo.setMC_11KV_POLE_1(sp_oh_11_feeder_1_pole.getSelectedItem().toString());
                                    }

                                    //FEEDER 2
                                    if (sp_oh_11_feeder_2.getSelectedItem().toString().equals("Other than List")) {
                                        surveyVo.setMC_11KV_FEEDER_ID_2(ed_oh_11_feeder_2.getText().toString().split("~")[0]);
                                    } else if (sp_oh_11_feeder_2.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                        surveyVo.setMC_11KV_FEEDER_ID_2(ed_oh_11_feeder_2_ns.getText().toString().split("~")[0]);
                                    } else {
                                        surveyVo.setMC_11KV_FEEDER_ID_2(sp_oh_11_feeder_2.getSelectedItem().toString());
                                    }

                                    if (sp_oh_11_feeder_2.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                        surveyVo.setMC_11KV_POLE_2("NOT SURVEYED");
                                    } else if (sp_oh_11_feeder_2_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                        surveyVo.setMC_11KV_POLE_2(ed_oh_11_pole_2.getText().toString());
                                        surveyVo.setMC_11KV_NOT_IN_LIST_2("Y");
                                    } else {
                                        surveyVo.setMC_11KV_POLE_2(sp_oh_11_feeder_2_pole.getSelectedItem().toString());
                                    }

                                    surveyVo.setMC_11KV_FEEDER_ID_3("N");
                                    surveyVo.setMC_11KV_POLE_3("N");

                                }

                                if (sp_oh_no_of_11_feeders.getSelectedItem().toString().equals("3")) {

                                    //FEEDER 1
                                    if (sp_oh_11_feeder_1.getSelectedItem().toString().equals("Other than List")) {
                                        surveyVo.setMC_11KV_FEEDER_ID_1(ed_oh_11_feeder_1.getText().toString().split("~")[0]);
                                    } else if (sp_oh_11_feeder_1.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                        surveyVo.setMC_11KV_FEEDER_ID_1(ed_oh_11_feeder_1_ns.getText().toString().split("~")[0]);
                                    } else {
                                        surveyVo.setMC_11KV_FEEDER_ID_1(sp_oh_11_feeder_1.getSelectedItem().toString());
                                    }

                                    if (sp_oh_11_feeder_1.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                        surveyVo.setMC_11KV_POLE_1("NOT SURVEYED");
                                    } else if (sp_oh_11_feeder_1_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                        surveyVo.setMC_11KV_POLE_1(ed_oh_11_pole_1.getText().toString());
                                        surveyVo.setMC_11KV_NOT_IN_LIST_1("Y");
                                    } else {
                                        surveyVo.setMC_11KV_POLE_1(sp_oh_11_feeder_1_pole.getSelectedItem().toString());
                                    }

                                    //FEEDER 2
                                    if (sp_oh_11_feeder_2.getSelectedItem().toString().equals("Other than List")) {
                                        surveyVo.setMC_11KV_FEEDER_ID_2(ed_oh_11_feeder_2.getText().toString().split("~")[0]);
                                    } else if (sp_oh_11_feeder_2.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                        surveyVo.setMC_11KV_FEEDER_ID_2(ed_oh_11_feeder_2_ns.getText().toString().split("~")[0]);
                                    } else {
                                        surveyVo.setMC_11KV_FEEDER_ID_2(sp_oh_11_feeder_2.getSelectedItem().toString());
                                    }

                                    if (sp_oh_11_feeder_2.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                        surveyVo.setMC_11KV_POLE_2("NOT SURVEYED");
                                    } else if (sp_oh_11_feeder_2_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                        surveyVo.setMC_11KV_POLE_2(ed_oh_11_pole_2.getText().toString());
                                        surveyVo.setMC_11KV_NOT_IN_LIST_2("Y");
                                    } else {
                                        surveyVo.setMC_11KV_POLE_2(sp_oh_11_feeder_2_pole.getSelectedItem().toString());
                                    }

                                    //FEEDER 3
                                    if (sp_oh_11_feeder_3.getSelectedItem().toString().equals("Other than List")) {
                                        surveyVo.setMC_11KV_FEEDER_ID_3(ed_oh_11_feeder_3.getText().toString().split("~")[0]);
                                    } else if (sp_oh_11_feeder_3.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                        surveyVo.setMC_11KV_FEEDER_ID_3(ed_oh_11_feeder_3_ns.getText().toString().split("~")[0]);
                                    } else {
                                        surveyVo.setMC_11KV_FEEDER_ID_3(sp_oh_11_feeder_3.getSelectedItem().toString());
                                    }

                                    if (sp_oh_11_feeder_3.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                        surveyVo.setMC_11KV_POLE_3("NOT SURVEYED");
                                    } else if (sp_oh_11_feeder_3_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                        surveyVo.setMC_11KV_POLE_3(ed_oh_11_pole_3.getText().toString());
                                        surveyVo.setMC_11KV_NOT_IN_LIST_3("Y");
                                    } else {
                                        surveyVo.setMC_11KV_POLE_3(sp_oh_11_feeder_3_pole.getSelectedItem().toString());
                                    }

                                }
                            } else {
                                surveyVo.setMC_11KV_FEEDER_ID_1("N");
                                surveyVo.setMC_11KV_POLE_1("N");
                                surveyVo.setMC_11KV_FEEDER_ID_2("N");
                                surveyVo.setMC_11KV_POLE_2("N");
                                surveyVo.setMC_11KV_FEEDER_ID_3("N");
                                surveyVo.setMC_11KV_POLE_3("N");

                            }

                            //LT FEEDER
                            if (ch_oh_lt_vlckts.isChecked()) {
                                if (sp_oh_no_of_lt_feeders.getSelectedItem().toString().equals("1")) {
                                    if (sp_oh_lt_feeder_1.getSelectedItem().toString().equals("Other than List")) {
                                        surveyVo.setMC_LT_FEEDER_ID_1(ed_oh_lt_feeder_1.getText().toString().split("~")[0]);
                                    } else if (sp_oh_lt_feeder_1.getSelectedItem().toString().equals("LT CKT Not Surveyed")) {
                                        surveyVo.setMC_LT_FEEDER_ID_1(ed_oh_lt_feeder_1_ns.getText().toString().split("~")[0]);
                                    } else {
                                        surveyVo.setMC_LT_FEEDER_ID_1(sp_oh_lt_feeder_1.getSelectedItem().toString());
                                    }

                                    if (sp_oh_lt_feeder_1.getSelectedItem().toString().equals("LT CKT Not Surveyed")) {
                                        surveyVo.setMC_LT_POLE_1("NOT SURVEYED");
                                    } else if (sp_oh_lt_feeder_1_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                        surveyVo.setMC_LT_POLE_1(ed_oh_lt_pole_1.getText().toString());
                                        surveyVo.setMC_LT_NOT_IN_LIST_1("Y");
                                    } else {
                                        surveyVo.setMC_LT_POLE_1(sp_oh_lt_feeder_1_pole.getSelectedItem().toString());
                                    }

                                    surveyVo.setMC_LT_FEEDER_ID_2("N");
                                    surveyVo.setMC_LT_POLE_2("N");
                                    surveyVo.setMC_LT_FEEDER_ID_3("N");
                                    surveyVo.setMC_LT_POLE_3("N");

                                }

                                if (sp_oh_no_of_lt_feeders.getSelectedItem().toString().equals("2")) {
                                    //FEEDER 1
                                    if (sp_oh_lt_feeder_1.getSelectedItem().toString().equals("Other than List")) {
                                        surveyVo.setMC_LT_FEEDER_ID_1(ed_oh_lt_feeder_1.getText().toString().split("~")[0]);
                                    } else if (sp_oh_lt_feeder_1.getSelectedItem().toString().equals("LT CKT Not Surveyed")) {
                                        surveyVo.setMC_LT_FEEDER_ID_1(ed_oh_lt_feeder_1_ns.getText().toString().split("~")[0]);
                                    } else {
                                        surveyVo.setMC_LT_FEEDER_ID_1(sp_oh_lt_feeder_1.getSelectedItem().toString());
                                    }
                                    if (sp_oh_lt_feeder_1.getSelectedItem().toString().equals("LT CKT Not Surveyed")) {
                                        surveyVo.setMC_LT_POLE_1("NOT SURVEYED");
                                    } else if (sp_oh_lt_feeder_1_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                        surveyVo.setMC_LT_POLE_1(ed_oh_lt_pole_1.getText().toString());
                                        surveyVo.setMC_LT_NOT_IN_LIST_1("Y");
                                    } else {
                                        surveyVo.setMC_LT_POLE_1(sp_oh_lt_feeder_1_pole.getSelectedItem().toString());
                                    }

                                    //FEEDER 2
                                    if (sp_oh_lt_feeder_2.getSelectedItem().toString().equals("Other than List")) {
                                        surveyVo.setMC_LT_FEEDER_ID_2(ed_oh_lt_feeder_2.getText().toString().split("~")[0]);
                                    } else if (sp_oh_lt_feeder_2.getSelectedItem().toString().equals("LT CKT Not Surveyed")) {
                                        surveyVo.setMC_LT_FEEDER_ID_2(ed_oh_lt_feeder_2_ns.getText().toString().split("~")[0]);
                                    } else {
                                        surveyVo.setMC_LT_FEEDER_ID_2(sp_oh_lt_feeder_2.getSelectedItem().toString());
                                    }

                                    if (sp_oh_lt_feeder_2.getSelectedItem().toString().equals("LT CKT Not Surveyed")) {
                                        surveyVo.setMC_LT_POLE_2("NOT SURVEYED");
                                    } else if (sp_oh_lt_feeder_2_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                        surveyVo.setMC_LT_POLE_2(ed_oh_lt_pole_2.getText().toString());
                                        surveyVo.setMC_LT_NOT_IN_LIST_2("Y");
                                    } else {
                                        surveyVo.setMC_LT_POLE_2(sp_oh_lt_feeder_2_pole.getSelectedItem().toString());
                                    }

                                    surveyVo.setMC_LT_FEEDER_ID_3("N");
                                    surveyVo.setMC_LT_POLE_3("N");

                                }

                                if (sp_oh_no_of_lt_feeders.getSelectedItem().toString().equals("3")) {

                                    //FEEDER 1
                                    if (sp_oh_lt_feeder_1.getSelectedItem().toString().equals("Other than List")) {
                                        surveyVo.setMC_LT_FEEDER_ID_1(ed_oh_lt_feeder_1.getText().toString().split("~")[0]);
                                    } else if (sp_oh_lt_feeder_1.getSelectedItem().toString().equals("LT CKT Not Surveyed")) {
                                        surveyVo.setMC_LT_FEEDER_ID_1(ed_oh_lt_feeder_1_ns.getText().toString().split("~")[0]);
                                    } else {
                                        surveyVo.setMC_LT_FEEDER_ID_1(sp_oh_lt_feeder_1.getSelectedItem().toString());
                                    }
                                    if (sp_oh_lt_feeder_1.getSelectedItem().toString().equals("LT CKT Not Surveyed")) {
                                        surveyVo.setMC_LT_POLE_1("NOT SURVEYED");
                                    } else if (sp_oh_lt_feeder_1_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                        surveyVo.setMC_LT_POLE_1(ed_oh_lt_pole_1.getText().toString());
                                        surveyVo.setMC_LT_NOT_IN_LIST_1("Y");
                                    } else {
                                        surveyVo.setMC_LT_POLE_1(sp_oh_lt_feeder_1_pole.getSelectedItem().toString());
                                    }

                                    //FEEDER 2
                                    if (sp_oh_lt_feeder_2.getSelectedItem().toString().equals("Other than List")) {
                                        surveyVo.setMC_LT_FEEDER_ID_2(ed_oh_lt_feeder_2.getText().toString().split("~")[0]);
                                    } else if (sp_oh_lt_feeder_2.getSelectedItem().toString().equals("LT CKT Not Surveyed")) {
                                        surveyVo.setMC_LT_FEEDER_ID_2(ed_oh_lt_feeder_2_ns.getText().toString().split("~")[0]);
                                    } else {
                                        surveyVo.setMC_LT_FEEDER_ID_2(sp_oh_lt_feeder_2.getSelectedItem().toString());
                                    }


                                    if (sp_oh_lt_feeder_2.getSelectedItem().toString().equals("LT CKT Not Surveyed")) {
                                        surveyVo.setMC_LT_POLE_2("NOT SURVEYED");
                                    } else if (sp_oh_lt_feeder_2_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                        surveyVo.setMC_LT_POLE_2(ed_oh_lt_pole_2.getText().toString());
                                        surveyVo.setMC_LT_NOT_IN_LIST_2("Y");
                                    } else {
                                        surveyVo.setMC_LT_POLE_2(sp_oh_lt_feeder_2_pole.getSelectedItem().toString());
                                    }

                                    //FEEDER 3
                                    if (sp_oh_lt_feeder_3.getSelectedItem().toString().equals("Other than List")) {
                                        surveyVo.setMC_LT_FEEDER_ID_3(ed_oh_lt_feeder_3.getText().toString().split("~")[0]);
                                    } else if (sp_oh_lt_feeder_3.getSelectedItem().toString().equals("LT CKT Not Surveyed")) {
                                        surveyVo.setMC_LT_FEEDER_ID_3(ed_oh_lt_feeder_3_ns.getText().toString().split("~")[0]);
                                    } else {
                                        surveyVo.setMC_LT_FEEDER_ID_3(sp_oh_lt_feeder_3.getSelectedItem().toString());
                                    }

                                    if (sp_oh_lt_feeder_3.getSelectedItem().toString().equals("LT CKT Not Surveyed")) {
                                        surveyVo.setMC_LT_POLE_3("NOT SURVEYED");
                                    } else if (sp_oh_lt_feeder_3_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                        surveyVo.setMC_LT_POLE_3(ed_oh_lt_pole_3.getText().toString());
                                        surveyVo.setMC_LT_NOT_IN_LIST_3("Y");
                                    } else {
                                        surveyVo.setMC_LT_POLE_3(sp_oh_lt_feeder_3_pole.getSelectedItem().toString());
                                    }

                                }
                            } else {
                                surveyVo.setMC_LT_FEEDER_ID_1("N");
                                surveyVo.setMC_LT_POLE_1("N");
                                surveyVo.setMC_LT_FEEDER_ID_2("N");
                                surveyVo.setMC_LT_POLE_2("N");
                                surveyVo.setMC_LT_FEEDER_ID_3("N");
                                surveyVo.setMC_LT_POLE_3("N");
                            }


                        } else if ("No".equalsIgnoreCase(ismulticircute)) {
                            surveyVo.setIsMultiCircuit(ismulticircute);
                            surveyVo.setNoofCircuits("1");
                            surveyVo.setCircuitType("");

                            surveyVo.setNO_OF33KV_CKTS("");
                            surveyVo.setNO_OF11KV_CKTS("");
                            surveyVo.setNO_OF_LT_CKTS("");

                            surveyVo.setMC_33KV_FEEDER_ID_1("N");
                            surveyVo.setMC_33KV_POLE_1("N");
                            surveyVo.setMC_33KV_FEEDER_ID_2("N");
                            surveyVo.setMC_33KV_POLE_2("N");
                            surveyVo.setMC_33KV_FEEDER_ID_3("N");
                            surveyVo.setMC_33KV_POLE_3("N");

                            surveyVo.setMC_11KV_FEEDER_ID_1("N");
                            surveyVo.setMC_11KV_POLE_1("N");
                            surveyVo.setMC_11KV_FEEDER_ID_2("N");
                            surveyVo.setMC_11KV_POLE_2("N");
                            surveyVo.setMC_11KV_FEEDER_ID_3("N");
                            surveyVo.setMC_11KV_POLE_3("N");

                            surveyVo.setMC_LT_FEEDER_ID_1("N");
                            surveyVo.setMC_LT_POLE_1("N");
                            surveyVo.setMC_LT_FEEDER_ID_2("N");
                            surveyVo.setMC_LT_POLE_2("N");
                            surveyVo.setMC_LT_FEEDER_ID_3("N");
                            surveyVo.setMC_LT_POLE_3("N");
                        }


                        /*if (!"Select".equalsIgnoreCase(sp_circutetype.getSelectedItem().toString())) {
                            surveyVo.setCircuitType(circutetype);
                        } else {
                            surveyVo.setCircuitType("");
                        }*/

                        //MULTI CKT END

                        //OTHER CABLES
                        if (sp_oh_other_cables.getSelectedItem().toString().equalsIgnoreCase("Yes")) {
                            surveyVo.setNUM_OTHER_CABLES(edt_oh_other_cables.getText().toString());
                        } else {
                            surveyVo.setNUM_OTHER_CABLES("");
                        }

                        //NEW DEFECTS
                        if (defect_selected.equalsIgnoreCase("Yes")) {
                            surveyVo.setDEFFECTS_AVAILABLE("YES");
                            surveyVo.setCROSSARM_DEFECTS(ch_cross_arm.isChecked() ? sp_cross_arm.getSelectedItem().toString() : "");
                            surveyVo.setTOPCLEAT_DEFECTS(ch_top_cleat.isChecked() ? sp_top_cleat.getSelectedItem().toString() : "");
                            surveyVo.setSTRUT_DEFECTS(ch_strut_pole.isChecked() ? sp_strut_pole.getSelectedItem().toString() : "");
                            surveyVo.setSTRAY_DEFECTS(ch_stray_wire.isChecked() ? sp_stray_wire.getSelectedItem().toString() : "");
                            surveyVo.setHTLT_CLEARANCE(ch_ht_clearance.isChecked() ? sp_ht_clearance.getSelectedItem().toString() : "");
                            surveyVo.setINTERMEDIATE_POLE(ch_int_pole.isChecked() ? sp_int_pole.getSelectedItem().toString() : "");
                            surveyVo.setRESTRINGING(ch_restringing.isChecked() ? sp_restringing.getSelectedItem().toString() : "");
                            surveyVo.setTREE_CLEARANCE(ch_tree_clearance.isChecked() ? sp_tree_clearance.getSelectedItem().toString() : "");
                            surveyVo.setOTHER_DEFFECTS(ch_other_defects.isChecked() ? ed_other_defects.getText().toString().trim() : "");
                            surveyVo.setPOLE_DEFECTS(ch_pole_defects.isChecked() ? sp_pole_defects.getSelectedItem().toString() : "");
                            surveyVo.setCONDUCTOR_DEFECTS(ch_conductors.isChecked() ? sp_conductors.getSelectedItem().toString() : "");
                            surveyVo.setAB_SWITCH_DEFECTS(ch_ab_switch_defects.isChecked() ? sp_ab_switch_defects.getSelectedItem().toString() : "");
                            surveyVo.setINSULATOR_DEFECTS(ch_insulators.isChecked() ? sp_insulators.getSelectedItem().toString() : "");

                        } else {
                            surveyVo.setDEFFECTS_AVAILABLE("NO");
                            surveyVo.setCROSSARM_DEFECTS("");
                            surveyVo.setTOPCLEAT_DEFECTS("");
                            surveyVo.setSTRUT_DEFECTS("");
                            surveyVo.setSTRAY_DEFECTS("");
                            surveyVo.setHTLT_CLEARANCE("");
                            surveyVo.setINTERMEDIATE_POLE("");
                            surveyVo.setRESTRINGING("");
                            surveyVo.setTREE_CLEARANCE("");
                            surveyVo.setOTHER_DEFFECTS("");
                            surveyVo.setPOLE_DEFECTS("");
                            surveyVo.setCONDUCTOR_DEFECTS("");
                            surveyVo.setAB_SWITCH_DEFECTS("");
                            surveyVo.setINSULATOR_DEFECTS("");
                        }


                        //IMAGES START

                        surveyVo.setIMAGE_1(image_1_clicked ? image_1_base64 : "N");
                        surveyVo.setIMAGE_2(image_2_clicked ? image_2_base64 : "N");
                        surveyVo.setIMAGE_3(image_3_clicked ? image_3_base64 : "N");

                        //OPEN CAMERA IMAGE
                        /*surveyVo.setIMAGE_1(image_1_clicked ?  ConvertImage.convertBase64OpenCamera(capturedBitmap1): "N");
                        surveyVo.setIMAGE_2(image_2_clicked ? ConvertImage.convertBase64OpenCamera(capturedBitmap2) : "N");
                        surveyVo.setIMAGE_3(image_3_clicked ? image_3_base64 : "N");*/

                        //IMAGES END

                        //CROSSING START
                    /*if (ch_crossing.isChecked()){
                        surveyVo.setCROSSING_TYPE(sp_crossing.getSelectedItem().toString());
                    }else {
                        surveyVo.setCROSSING_TYPE("");
                    }*/

                        if (ch_crossing.isChecked()) {
                            surveyVo.setCROSSING_TYPE(ch_oh_road_crossing.isChecked() ? sp_crossing.getSelectedItem().toString() : "");
                            surveyVo.setCROSS_6_3KV_ID(ch_oh_6_3_crossing.isChecked() ? edt_oh_6_3_crossing.getText().toString().split("~")[0] : "");
                            surveyVo.setCROSS_11KV_ID(ch_oh_11kv_crossing.isChecked() ? edt_oh_11kv_crossing.getText().toString().split("~")[0] : "");
                            surveyVo.setCROSS_33KV_ID(ch_oh_33kv_crossing.isChecked() ? edt_oh_33kv_crossing.getText().toString().split("~")[0] : "");
                            surveyVo.setCROSS_LT_CKT(ch_oh_lt_crossing.isChecked() ? edt_oh_lt_crossing.getText().toString().split("~")[0] : "");
                            surveyVo.setCROSS_COMMUN(ch_oh_tc_crossing.isChecked() ? edt_oh_tc_crossing.getText().toString() : "");
                            surveyVo.setCROSS_EHT(ch_oh_tm_crossing.isChecked() ? edt_oh_tm_crossing.getText().toString() : "");
                            surveyVo.setCROSS_REMARKS(ed_crossing_remarks.getText().toString().isEmpty() ? "" : ed_crossing_remarks.getText().toString());
                        } else {
                            surveyVo.setCROSSING_TYPE("");
                            surveyVo.setCROSS_6_3KV_ID("");
                            surveyVo.setCROSS_11KV_ID("");
                            surveyVo.setCROSS_33KV_ID("");
                            surveyVo.setCROSS_LT_CKT("");
                            surveyVo.setCROSS_COMMUN("");
                            surveyVo.setCROSS_EHT("");
                            surveyVo.setCROSS_REMARKS("");
                        }


                        surveyVo.setPOLE_CONCRETE(sp_pole_concrete.getSelectedItem().toString());
                        surveyVo.setCURRENT_LAT(""+CURRENT_LAT);
                        surveyVo.setCURRENT_LONG(""+CURRENT_LONG);


                        //CHECK MULTI CIRCUIT DUPLICATE START

                        System.out.println("IS MULTI CKT "+ismulticircute);
                        if ("Yes".equalsIgnoreCase(ismulticircute)){
                            List<PMISurveyVo> checkList = arcGISDatabase.checkMultiCktForLT();
                            if (!checkList.isEmpty()){
                                for (int i = 0; i < checkList.size(); i++) {
                                    //33KV
                                    if (ch_oh_33kv_vlckts.isChecked()){
                                        //FEEDER 1
                                        if (!surveyVo.getMC_33KV_FEEDER_ID_1().equals("N")){
                                            if (!checkList.get(i).getMC_33KV_POLE_1().equals("NOT SURVEYED") && checkList.get(i).getMC_33KV_FEEDER_ID_1().equals(surveyVo.getMC_33KV_FEEDER_ID_1()) && checkList.get(i).getMC_33KV_POLE_1().equals(surveyVo.getMC_33KV_POLE_1())){
                                                Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            if (!checkList.get(i).getMC_33KV_POLE_2().equals("NOT SURVEYED") && checkList.get(i).getMC_33KV_FEEDER_ID_2().equals(surveyVo.getMC_33KV_FEEDER_ID_1()) && checkList.get(i).getMC_33KV_POLE_2().equals(surveyVo.getMC_33KV_POLE_1())){
                                                Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            if (!checkList.get(i).getMC_33KV_POLE_3().equals("NOT SURVEYED") && checkList.get(i).getMC_33KV_FEEDER_ID_3().equals(surveyVo.getMC_33KV_FEEDER_ID_1()) && checkList.get(i).getMC_33KV_POLE_3().equals(surveyVo.getMC_33KV_POLE_1())){
                                                Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                        }

                                        //FEEDER 2
                                        if (!surveyVo.getMC_33KV_FEEDER_ID_2().equals("N")){
                                            if (!checkList.get(i).getMC_33KV_POLE_1().equals("NOT SURVEYED") && checkList.get(i).getMC_33KV_FEEDER_ID_1().equals(surveyVo.getMC_33KV_FEEDER_ID_2()) && checkList.get(i).getMC_33KV_POLE_1().equals(surveyVo.getMC_33KV_POLE_2())){
                                                Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            if (!checkList.get(i).getMC_33KV_POLE_2().equals("NOT SURVEYED") && checkList.get(i).getMC_33KV_FEEDER_ID_2().equals(surveyVo.getMC_33KV_FEEDER_ID_2()) && checkList.get(i).getMC_33KV_POLE_2().equals(surveyVo.getMC_33KV_POLE_2())){
                                                Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            if (!checkList.get(i).getMC_33KV_POLE_3().equals("NOT SURVEYED") && checkList.get(i).getMC_33KV_FEEDER_ID_3().equals(surveyVo.getMC_33KV_FEEDER_ID_2()) && checkList.get(i).getMC_33KV_POLE_3().equals(surveyVo.getMC_33KV_POLE_2())){
                                                Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                        }

                                        //FEEDER 3
                                        if (!surveyVo.getMC_33KV_FEEDER_ID_3().equals("N")){
                                            if (!checkList.get(i).getMC_33KV_POLE_1().equals("NOT SURVEYED") && checkList.get(i).getMC_33KV_FEEDER_ID_1().equals(surveyVo.getMC_33KV_FEEDER_ID_3()) && checkList.get(i).getMC_33KV_POLE_1().equals(surveyVo.getMC_33KV_POLE_3())){
                                                Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            if (!checkList.get(i).getMC_33KV_POLE_2().equals("NOT SURVEYED") && checkList.get(i).getMC_33KV_FEEDER_ID_2().equals(surveyVo.getMC_33KV_FEEDER_ID_3()) && checkList.get(i).getMC_33KV_POLE_2().equals(surveyVo.getMC_33KV_POLE_3())){
                                                Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            if (!checkList.get(i).getMC_33KV_POLE_3().equals("NOT SURVEYED") && checkList.get(i).getMC_33KV_FEEDER_ID_3().equals(surveyVo.getMC_33KV_FEEDER_ID_3()) && checkList.get(i).getMC_33KV_POLE_3().equals(surveyVo.getMC_33KV_POLE_3())){
                                                Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                        }

                                    }

                                    //11KV
                                    if (ch_oh_11kv_vlckts.isChecked()){
                                        //FEEDER 1
                                        if (!surveyVo.getMC_11KV_FEEDER_ID_1().equals("N")){
                                            if (!checkList.get(i).getMC_11KV_POLE_1().equals("NOT SURVEYED") && checkList.get(i).getMC_11KV_FEEDER_ID_1().equals(surveyVo.getMC_11KV_FEEDER_ID_1()) && checkList.get(i).getMC_11KV_POLE_1().equals(surveyVo.getMC_11KV_POLE_1())){
                                                Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            if (!checkList.get(i).getMC_11KV_POLE_2().equals("NOT SURVEYED") && checkList.get(i).getMC_11KV_FEEDER_ID_2().equals(surveyVo.getMC_11KV_FEEDER_ID_1()) && checkList.get(i).getMC_11KV_POLE_2().equals(surveyVo.getMC_11KV_POLE_1())){
                                                Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            if (!checkList.get(i).getMC_11KV_POLE_3().equals("NOT SURVEYED") && checkList.get(i).getMC_11KV_FEEDER_ID_3().equals(surveyVo.getMC_11KV_FEEDER_ID_1()) && checkList.get(i).getMC_11KV_POLE_3().equals(surveyVo.getMC_11KV_POLE_1())){
                                                Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                        }

                                        //FEEDER 2
                                        if (!surveyVo.getMC_11KV_FEEDER_ID_2().equals("N")){
                                            if (!checkList.get(i).getMC_11KV_POLE_1().equals("NOT SURVEYED") && checkList.get(i).getMC_11KV_FEEDER_ID_1().equals(surveyVo.getMC_11KV_FEEDER_ID_2()) && checkList.get(i).getMC_11KV_POLE_1().equals(surveyVo.getMC_11KV_POLE_2())){
                                                Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            if (!checkList.get(i).getMC_11KV_POLE_2().equals("NOT SURVEYED") && checkList.get(i).getMC_11KV_FEEDER_ID_2().equals(surveyVo.getMC_11KV_FEEDER_ID_2()) && checkList.get(i).getMC_11KV_POLE_2().equals(surveyVo.getMC_11KV_POLE_2())){
                                                Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            if (!checkList.get(i).getMC_11KV_POLE_3().equals("NOT SURVEYED") && checkList.get(i).getMC_11KV_FEEDER_ID_3().equals(surveyVo.getMC_11KV_FEEDER_ID_2()) && checkList.get(i).getMC_11KV_POLE_3().equals(surveyVo.getMC_11KV_POLE_2())){
                                                Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                        }

                                        //FEEDER 3
                                        if (!surveyVo.getMC_11KV_FEEDER_ID_3().equals("N")){
                                            if (!checkList.get(i).getMC_11KV_POLE_1().equals("NOT SURVEYED") && checkList.get(i).getMC_11KV_FEEDER_ID_1().equals(surveyVo.getMC_11KV_FEEDER_ID_3()) && checkList.get(i).getMC_11KV_POLE_1().equals(surveyVo.getMC_11KV_POLE_3())){
                                                Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            if (!checkList.get(i).getMC_11KV_POLE_2().equals("NOT SURVEYED") && checkList.get(i).getMC_11KV_FEEDER_ID_2().equals(surveyVo.getMC_11KV_FEEDER_ID_3()) && checkList.get(i).getMC_11KV_POLE_2().equals(surveyVo.getMC_11KV_POLE_3())){
                                                Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            if (!checkList.get(i).getMC_11KV_POLE_3().equals("NOT SURVEYED") && checkList.get(i).getMC_11KV_FEEDER_ID_3().equals(surveyVo.getMC_11KV_FEEDER_ID_3()) && checkList.get(i).getMC_11KV_POLE_3().equals(surveyVo.getMC_11KV_POLE_3())){
                                                Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                        }

                                    }

                                    //LT
                                    if (ch_oh_lt_vlckts.isChecked()){
                                        //FEEDER 1
                                        if (!surveyVo.getMC_LT_FEEDER_ID_1().equals("N")){
                                            if (!checkList.get(i).getMC_LT_POLE_1().equals("NOT SURVEYED") && checkList.get(i).getMC_LT_FEEDER_ID_1().equals(surveyVo.getMC_LT_FEEDER_ID_1()) && checkList.get(i).getMC_LT_POLE_1().equals(surveyVo.getMC_LT_POLE_1())){
                                                Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            if (!checkList.get(i).getMC_LT_POLE_2().equals("NOT SURVEYED") && checkList.get(i).getMC_LT_FEEDER_ID_2().equals(surveyVo.getMC_LT_FEEDER_ID_1()) && checkList.get(i).getMC_LT_POLE_2().equals(surveyVo.getMC_LT_POLE_1())){
                                                Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            if (!checkList.get(i).getMC_LT_POLE_3().equals("NOT SURVEYED") && checkList.get(i).getMC_LT_FEEDER_ID_3().equals(surveyVo.getMC_LT_FEEDER_ID_1()) && checkList.get(i).getMC_LT_POLE_3().equals(surveyVo.getMC_LT_POLE_1())){
                                                Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                        }

                                        //FEEDER 2
                                        if (!surveyVo.getMC_LT_FEEDER_ID_2().equals("N")){
                                            if (!checkList.get(i).getMC_LT_POLE_1().equals("NOT SURVEYED") && checkList.get(i).getMC_LT_FEEDER_ID_1().equals(surveyVo.getMC_LT_FEEDER_ID_2()) && checkList.get(i).getMC_LT_POLE_1().equals(surveyVo.getMC_LT_POLE_2())){
                                                Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            if (!checkList.get(i).getMC_LT_POLE_2().equals("NOT SURVEYED") && checkList.get(i).getMC_LT_FEEDER_ID_2().equals(surveyVo.getMC_LT_FEEDER_ID_2()) && checkList.get(i).getMC_LT_POLE_2().equals(surveyVo.getMC_LT_POLE_2())){
                                                Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            if (!checkList.get(i).getMC_LT_POLE_3().equals("NOT SURVEYED") && checkList.get(i).getMC_LT_FEEDER_ID_3().equals(surveyVo.getMC_LT_FEEDER_ID_2()) && checkList.get(i).getMC_LT_POLE_3().equals(surveyVo.getMC_LT_POLE_2())){
                                                Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                        }

                                        //FEEDER 3
                                        if (!surveyVo.getMC_LT_FEEDER_ID_3().equals("N")){
                                            if (!checkList.get(i).getMC_LT_POLE_1().equals("NOT SURVEYED") && checkList.get(i).getMC_LT_FEEDER_ID_1().equals(surveyVo.getMC_LT_FEEDER_ID_3()) && checkList.get(i).getMC_LT_POLE_1().equals(surveyVo.getMC_LT_POLE_3())){
                                                Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            if (!checkList.get(i).getMC_LT_POLE_2().equals("NOT SURVEYED") && checkList.get(i).getMC_LT_FEEDER_ID_2().equals(surveyVo.getMC_LT_FEEDER_ID_3()) && checkList.get(i).getMC_LT_POLE_2().equals(surveyVo.getMC_LT_POLE_3())){
                                                Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            if (!checkList.get(i).getMC_LT_POLE_3().equals("NOT SURVEYED") && checkList.get(i).getMC_LT_FEEDER_ID_3().equals(surveyVo.getMC_LT_FEEDER_ID_3()) && checkList.get(i).getMC_LT_POLE_3().equals(surveyVo.getMC_LT_POLE_3())){
                                                Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                        }

                                    }



                                }
                            }
                        }

                        //CHECK MULTI CIRCUIT DUPLICATE END


                        if (surveyType.equalsIgnoreCase("Intermediate/Inline Pole")) {
                            callInlinePoleAPI(surveyVo);
                        } else {
                            PMIArcGISMapActivity33KV.IS_SURVEY_STARTED = true;
                            image_1_clicked = false;
                            image_2_clicked = false;
                            image_3_clicked = false;
                            repeatPoleClicked = false;
                            arcGISDatabase.addSurveyData(surveyVo);
                            arcGISDatabase.addImageData(surveyVo);
                            Toast.makeText(c, "Pole location captured successfully..", Toast.LENGTH_SHORT).show();
                            dismiss();
                            callout.dismiss();
                            graphicsOverlay.getGraphics().remove(graphic);
                            if ("YES".equalsIgnoreCase(isendpole)) {
                                PMIArcGISMapActivity33KV.IS_SURVEY_STARTED = false;
                                Intent intent = new Intent(c, PMIHomeActivity.class);
                                c.startActivity(intent);
                                c.finish();
                            }

                        }

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void callInlinePoleAPI(PMISurveyVo surveyVo) {
        try {
            progressDialog.show();
            PMISurveyedRequestVo surveyedRequestVo = new PMISurveyedRequestVo();
            List<PMISurveyedRequestDataVo> surveyedRequestDataVosList = new ArrayList<>();
            List<LoginDataResponse> dataResponseList = new PMIArcGISDatabase(c).getUserMasterData();
            surveyedRequestVo.setUSER_ID(userName);
            surveyedRequestVo.setIMEI_NO(CommanUtilits.getIMEI_NO(c));
            surveyedRequestVo.setSESSION_ID(sessionId);
            surveyedRequestVo.setOFFICE_ID(dataResponseList.get(0).getOFFICE_ID());
            surveyedRequestVo.setIS_SURVEY_COM("N");
            surveyedRequestVo.setFEEDER_ID(feederCode);
            PMISurveyedRequestDataVo requestDataVo = new PMISurveyedRequestDataVo();
            requestDataVo.setFEEDER_ID(surveyVo.getFeeder_id());
            requestDataVo.setVOLTAGE_LEVEL("33kv");
            requestDataVo.setPOLE_NO(Integer.parseInt(PMIPoleSurveyActivity33KV.pole_no));
            requestDataVo.setFROM_POLE(surveyVo.getFrom_pole());
            requestDataVo.setPOLE_TYPE(CommanUtilits.isChecking(surveyVo.getPole_type()));
            requestDataVo.setPOLE_HEIGHT(surveyVo.getPole_height());
            // requestDataVo.setNO_CIRCUITS(surveyVo.getNoof_circuits());
            requestDataVo.setPOLE_SUPPORT(CommanUtilits.isChecking(surveyVo.getPole_support()));
            requestDataVo.setTAPPING_POINT(CommanUtilits.isChecking(surveyVo.getTapping_point()));
            requestDataVo.setCONDUCTOR_SIZE(surveyVo.getConductor_size());
            requestDataVo.setPOLE_PAINT_CODE(CommanUtilits.isChecking(surveyVo.getPole_paintingcode()));
            requestDataVo.setSEC_CODE(dataResponseList.get(0).getOFFICE_ID());
            requestDataVo.setCREATED_BY(userName);
            requestDataVo.setGLAT(surveyVo.getLat());
            requestDataVo.setGLONG(surveyVo.getLan());
            requestDataVo.setNO_CKT(CommanUtilits.isChecking(surveyVo.getIsMultiCircuit()));
            requestDataVo.setCKT_TYPE(CommanUtilits.isChecking(surveyVo.getCircuitType()));
            requestDataVo.setIME_NO(CommanUtilits.getIMEI_NO(c));
            requestDataVo.setPOLE_CONFIG(CommanUtilits.isChecking(surveyVo.getPole_configuration()));
            requestDataVo.setPIN_CUT(CommanUtilits.isChecking(surveyVo.getPinpoint()));
            requestDataVo.setSS_NAME(CommanUtilits.isChecking(surveyVo.getSs_name()));
            requestDataVo.setPOLE_STRUCTURE(CommanUtilits.isChecking(surveyVo.getPoleStructure()));
            requestDataVo.setDIFF_VOLTAGES(CommanUtilits.isChecking(surveyVo.getDiffVoltage()));

            if (surveyVo.getIsEndPole() != null) {
                requestDataVo.setEND_POLE(CommanUtilits.isChecking(surveyVo.getIsEndPole()));
            } else {
                requestDataVo.setEND_POLE("");
            }
            // requestDataVo.setEND_POLE(surveyVo.getIsEndPole());
            requestDataVo.setLINE_TYPE(CommanUtilits.isChecking(surveyVo.getLineType()));
            requestDataVo.setCKT_NO(surveyVo.getNoofCircuits());
            requestDataVo.setSS_ID(surveyVo.getSsId());
            requestDataVo.setSTJOINT_RMU(CommanUtilits.isChecking(surveyVo.getStjoint_rmu()));
            requestDataVo.setRMU_TYPE(CommanUtilits.isChecking(surveyVo.getRmu_type()));
            requestDataVo.setTAPPING_POINTS(surveyVo.getNoofTappings());
            requestDataVo.setTAPPING_LOCNAME(CommanUtilits.isChecking(surveyVo.getTappingLocation()));
            requestDataVo.setREMARKS(CommanUtilits.isChecking(surveyVo.getRemarks()));
            requestDataVo.setWO_ID(surveyVo.getWoId());
            requestDataVo.setIS_FEEDER_TYPE(surveyVo.getIsFeederType());
            requestDataVo.setHT_SCNO(surveyVo.getHt_service_number());
            requestDataVo.setSURVEY_TYPE(surveyVo.getSurvaytype());

            //MULTI CKT and OTHER CABLES
            requestDataVo.setNO_33KV(surveyVo.getNO_OF33KV_CKTS());
            requestDataVo.setNO_11KV(surveyVo.getNO_OF11KV_CKTS());
            requestDataVo.setNO_LT(surveyVo.getNO_OF_LT_CKTS());

            if (surveyVo.getLineType().equalsIgnoreCase("OverHead")) {

                //CROSSINGS
                requestDataVo.setCROSSING_TYPE(surveyVo.getCROSSING_TYPE());
                requestDataVo.setCROSS_6_3KV_ID(surveyVo.getCROSS_6_3KV_ID());
                requestDataVo.setCROSS_11KV_ID(surveyVo.getCROSS_11KV_ID());
                requestDataVo.setCROSS_33KV_ID(surveyVo.getCROSS_33KV_ID());
                requestDataVo.setCROSS_LT_CKT(surveyVo.getCROSS_LT_CKT());
                requestDataVo.setCROSS_COMMUN(surveyVo.getCROSS_COMMUN());
                requestDataVo.setCROSS_EHT(surveyVo.getCROSS_EHT());
                requestDataVo.setCROSS_REMARKS(surveyVo.getCROSS_REMARKS());

                //DEFECTS
                requestDataVo.setDEFFECTS_AVAILABLE(surveyVo.getDEFFECTS_AVAILABLE());
                requestDataVo.setPOLE_DEFECTS(surveyVo.getPOLE_DEFECTS());
                requestDataVo.setCONDUCTOR_DEFECTS(surveyVo.getCONDUCTOR_DEFECTS());
                requestDataVo.setAB_SWITCH_DEFECTS(surveyVo.getAB_SWITCH_DEFECTS());
                requestDataVo.setINSULATOR_DEFECTS(surveyVo.getINSULATOR_DEFECTS());
                requestDataVo.setCROSSARM_DEFECTS(surveyVo.getCROSSARM_DEFECTS());
                requestDataVo.setTOPCLEAT_DEFECTS(surveyVo.getTOPCLEAT_DEFECTS());
                requestDataVo.setSTRUT_DEFECTS(surveyVo.getSTRUT_DEFECTS());
                requestDataVo.setSTRAY_DEFECTS(surveyVo.getSTRAY_DEFECTS());
                requestDataVo.setHTLT_CLEARANCE(surveyVo.getHTLT_CLEARANCE());
                requestDataVo.setINTERMEDIATE_POLE(surveyVo.getINTERMEDIATE_POLE());
                requestDataVo.setRESTRINGING(surveyVo.getRESTRINGING());
                requestDataVo.setTREE_CLEARANCE(surveyVo.getTREE_CLEARANCE());
                requestDataVo.setOTHER_DEFFECTS(surveyVo.getOTHER_DEFFECTS());


                //OTHER CABLES
                requestDataVo.setNUM_OTHER_CABLES(surveyVo.getNUM_OTHER_CABLES());

                if (surveyVo.getIsMultiCircuit().equalsIgnoreCase("Yes")) {


                    ArrayList<PMIMultiRequestVO> mc_list = new ArrayList<>();
                    if (!surveyVo.getMC_33KV_FEEDER_ID_1().equals("N")) {
                        PMIMultiRequestVO vo = new PMIMultiRequestVO();
                        vo.setVOLTAGE_LEVEL("33KV");
                        String selectedFeeder = surveyVo.getMC_33KV_FEEDER_ID_1();
                        String[] splitFeederId = selectedFeeder.split("~");
                        selectedFeeder = splitFeederId[0];
                        vo.setFEEDER_ID(selectedFeeder);
                        vo.setLOCATION_NO(surveyVo.getMC_33KV_POLE_1());
                        vo.setMANUAL_ENTRY(surveyVo.getMC_33KV_NOT_IN_LIST_1());
                        mc_list.add(vo);
                    }
                    if (!surveyVo.getMC_33KV_FEEDER_ID_2().equals("N")) {
                        PMIMultiRequestVO vo = new PMIMultiRequestVO();
                        vo.setVOLTAGE_LEVEL("33KV");
                        String selectedFeeder = surveyVo.getMC_33KV_FEEDER_ID_2();
                        String[] splitFeederId = selectedFeeder.split("~");
                        selectedFeeder = splitFeederId[0];
                        vo.setFEEDER_ID(selectedFeeder);
                        vo.setLOCATION_NO(surveyVo.getMC_33KV_POLE_2());
                        vo.setMANUAL_ENTRY(surveyVo.getMC_33KV_NOT_IN_LIST_2());
                        mc_list.add(vo);
                    }
                    if (!surveyVo.getMC_33KV_FEEDER_ID_3().equals("N")) {
                        PMIMultiRequestVO vo = new PMIMultiRequestVO();
                        vo.setVOLTAGE_LEVEL("33KV");
                        String selectedFeeder = surveyVo.getMC_33KV_FEEDER_ID_3();
                        String[] splitFeederId = selectedFeeder.split("~");
                        selectedFeeder = splitFeederId[0];
                        vo.setFEEDER_ID(selectedFeeder);
                        vo.setLOCATION_NO(surveyVo.getMC_33KV_POLE_3());
                        vo.setMANUAL_ENTRY(surveyVo.getMC_33KV_NOT_IN_LIST_3());
                        mc_list.add(vo);
                    }

                    if (!surveyVo.getMC_11KV_FEEDER_ID_1().equals("N")) {
                        PMIMultiRequestVO vo = new PMIMultiRequestVO();
                        vo.setVOLTAGE_LEVEL("11KV");
                        String selectedFeeder = surveyVo.getMC_11KV_FEEDER_ID_1();
                        String[] splitFeederId = selectedFeeder.split("~");
                        selectedFeeder = splitFeederId[0];
                        vo.setFEEDER_ID(selectedFeeder);
                        vo.setLOCATION_NO(surveyVo.getMC_11KV_POLE_1());
                        vo.setMANUAL_ENTRY(surveyVo.getMC_11KV_NOT_IN_LIST_1());
                        mc_list.add(vo);
                    }
                    if (!surveyVo.getMC_11KV_FEEDER_ID_2().equals("N")) {
                        PMIMultiRequestVO vo = new PMIMultiRequestVO();
                        vo.setVOLTAGE_LEVEL("11KV");
                        String selectedFeeder = surveyVo.getMC_11KV_FEEDER_ID_2();
                        String[] splitFeederId = selectedFeeder.split("~");
                        selectedFeeder = splitFeederId[0];
                        vo.setFEEDER_ID(selectedFeeder);
                        vo.setLOCATION_NO(surveyVo.getMC_11KV_POLE_2());
                        vo.setMANUAL_ENTRY(surveyVo.getMC_11KV_NOT_IN_LIST_2());
                        mc_list.add(vo);
                    }
                    if (!surveyVo.getMC_11KV_FEEDER_ID_3().equals("N")) {
                        PMIMultiRequestVO vo = new PMIMultiRequestVO();
                        vo.setVOLTAGE_LEVEL("11KV");
                        String selectedFeeder = surveyVo.getMC_11KV_FEEDER_ID_2();
                        String[] splitFeederId = selectedFeeder.split("~");
                        selectedFeeder = splitFeederId[0];
                        vo.setFEEDER_ID(selectedFeeder);
                        vo.setLOCATION_NO(surveyVo.getMC_11KV_POLE_3());
                        vo.setMANUAL_ENTRY(surveyVo.getMC_11KV_NOT_IN_LIST_3());
                        mc_list.add(vo);
                    }

                    if (!surveyVo.getMC_LT_FEEDER_ID_1().equals("N")) {
                        PMIMultiRequestVO vo = new PMIMultiRequestVO();
                        vo.setVOLTAGE_LEVEL("LT");
                        String selectedFeeder = surveyVo.getMC_LT_FEEDER_ID_1();
                       /* String [] splitFeederId = selectedFeeder.split("~");
                        selectedFeeder = splitFeederId[0];*/
                        vo.setFEEDER_ID(selectedFeeder);
                        vo.setLOCATION_NO(surveyVo.getMC_LT_POLE_1());
                        vo.setMANUAL_ENTRY(surveyVo.getMC_LT_NOT_IN_LIST_1());
                        mc_list.add(vo);
                    }
                    if (!surveyVo.getMC_LT_FEEDER_ID_2().equals("N")) {
                        PMIMultiRequestVO vo = new PMIMultiRequestVO();
                        vo.setVOLTAGE_LEVEL("LT");
                        String selectedFeeder = surveyVo.getMC_LT_FEEDER_ID_2();
                        /*String [] splitFeederId = selectedFeeder.split("~");
                        selectedFeeder = splitFeederId[0];*/
                        vo.setFEEDER_ID(selectedFeeder);
                        vo.setLOCATION_NO(surveyVo.getMC_LT_POLE_2());
                        vo.setMANUAL_ENTRY(surveyVo.getMC_LT_NOT_IN_LIST_2());
                        mc_list.add(vo);
                    }
                    if (!surveyVo.getMC_LT_FEEDER_ID_3().equals("N")) {
                        PMIMultiRequestVO vo = new PMIMultiRequestVO();
                        vo.setVOLTAGE_LEVEL("LT");
                        String selectedFeeder = surveyVo.getMC_LT_FEEDER_ID_2();
                       /* String [] splitFeederId = selectedFeeder.split("~");
                        selectedFeeder = splitFeederId[0];*/
                        vo.setFEEDER_ID(selectedFeeder);
                        vo.setLOCATION_NO(surveyVo.getMC_LT_POLE_3());
                        vo.setMANUAL_ENTRY(surveyVo.getMC_LT_NOT_IN_LIST_3());
                        mc_list.add(vo);
                    }

                    requestDataVo.setMC(mc_list);

                }
            }

            requestDataVo.setPOLE_CONCRETE(surveyVo.getPOLE_CONCRETE());
            requestDataVo.setCURRENT_LAT(surveyVo.getCURRENT_LAT());
            requestDataVo.setCURRENT_LONG(surveyVo.getCURRENT_LONG());

            surveyedRequestDataVosList.add(requestDataVo);

            surveyedRequestVo.setSURVEY_DATA(surveyedRequestDataVosList);
            Gson gson = new Gson();
            String json = gson.toJson(surveyedRequestVo);

            Log.v("DATA===>>", "" + json);


            retrofitService = PMIRetrofitClientInstance33kv.getRetrofitInstance().create(RetrofitService33kv.class);
            Call<PMISuveyedResponseVo> responseVoCall = retrofitService.inlinePole(surveyedRequestVo);
            responseVoCall.enqueue(new Callback<PMISuveyedResponseVo>() {
                @Override
                public void onResponse(Call<PMISuveyedResponseVo> call, Response<PMISuveyedResponseVo> response) {
                    progressDialog.dismiss();
                    if (response.body() != null) {
                        PMISuveyedResponseVo response_vo = response.body();
                        // progressDialog.dismiss();
                        if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {

                            //  arcGISDatabase.clearSurveyedData();
                            if (image_1_clicked || image_2_clicked || image_3_clicked) {
                                transmitImageData(surveyVo);
                            } else {
                                Toast.makeText(c, "Data Transmitted Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(c, PMIHomeActivity.class);
                                c.startActivity(intent);
                                c.finish();

                            }


                        } else {
                            Toast.makeText(c, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(c, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<PMISuveyedResponseVo> call, Throwable t) {
                    progressDialog.dismiss();
                    //  progressDialog.dismiss();
                    Toast.makeText(c, "Try Again", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void transmitImageData(PMISurveyVo surveyVo) {
        progressDialog.show();
        //  btn_transmit_images.setVisibility(View.GONE);
        PMIImageRequest imageRequest = new PMIImageRequest();
        List<PMIImageList> list = new ArrayList<>();
        imageRequest.setFEEDER_ID(feederCode);
        PMIImageList requestDataVo = new PMIImageList();
        requestDataVo.setFEEDER_ID(CommanUtilits.isChecking(surveyVo.getFeeder_id()));
        requestDataVo.setPOLE_NO("" + surveyVo.getPole_no());

        //OLD
        requestDataVo.setIMAGE_1(surveyVo.getIMAGE_1().equals("N") ? "N" : ConvertImage.convertBase64(surveyVo.getIMAGE_1()));
        requestDataVo.setIMAGE_2(surveyVo.getIMAGE_2().equals("N") ? "N" : ConvertImage.convertBase64(surveyVo.getIMAGE_2()));
        requestDataVo.setIMAGE_3(surveyVo.getIMAGE_3().equals("N") ? "N" : ConvertImage.convertBase64(surveyVo.getIMAGE_3()));

        //NEW OPEN CAMERA
        /*requestDataVo.setIMAGE_1(surveyVo.getIMAGE_1().equals("N") ? "N" : surveyVo.getIMAGE_1());
        requestDataVo.setIMAGE_2(surveyVo.getIMAGE_2().equals("N") ? "N" : surveyVo.getIMAGE_2());
        requestDataVo.setIMAGE_3(surveyVo.getIMAGE_3().equals("N") ? "N" : surveyVo.getIMAGE_3());*/



        list.add(requestDataVo);
        imageRequest.setImageList(list);
        Gson gson = new Gson();
        String json = gson.toJson(imageRequest);

        Log.v("DATA===>>", "" + json);
        //  writeToFile(json);
        retrofitService = PMIRetrofitClientInstance33kv.getRetrofitInstance().create(RetrofitService33kv.class);
        Call<PMISuveyedResponseVo> responseVoCall = retrofitService.transmitImageData(imageRequest);
        responseVoCall.enqueue(new Callback<PMISuveyedResponseVo>() {
            @Override
            public void onResponse(Call<PMISuveyedResponseVo> call, Response<PMISuveyedResponseVo> response) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    PMISuveyedResponseVo response_vo = response.body();
                    progressDialog.dismiss();
                    if ("TRUE".equalsIgnoreCase(response_vo.getSTATUS())) {
                        //  arcGISDatabase.clearImageData();
                        image_1_clicked = false;
                        image_2_clicked = false;
                        image_3_clicked = false;
                        repeatPoleClicked = false;
                        Toast.makeText(c, "Data Transmitted Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(c, PMIHomeActivity.class);
                        c.startActivity(intent);
                        c.finish();

                    } else {
                        Toast.makeText(c, response_vo.getERR_MSG(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(c, "Server Busy", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PMISuveyedResponseVo> call, Throwable t) {
                progressDialog.dismiss();
                //  progressDialog.dismiss();
                Toast.makeText(c, "Try Again", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean validate33KVVoltageCircuit() {

        if (sp_ismulticircute.getSelectedItem().toString().equals("Yes")) {
            if (ch_oh_33kv_vlckts.isChecked()) {
                if ("1".equalsIgnoreCase(sp_oh_no_of_33_feeders.getSelectedItem().toString())
                        || "2".equalsIgnoreCase(sp_oh_no_of_33_feeders.getSelectedItem().toString())
                        || "3".equalsIgnoreCase(sp_oh_no_of_33_feeders.getSelectedItem().toString())
                ) {
                    if (sp_oh_33_feeder_1.getSelectedItem().toString().equals("Select")) {
                        Toast.makeText(c, "Select Feeder 1 of 33kv Voltage CKT", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    if (sp_oh_33_feeder_1.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed")) {
                        if (ed_oh_33_feeder_1_ns.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Not Surveyed Feeder 1 of 33kv Voltage CKT", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }

                    if (sp_oh_33_feeder_1.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                        if (ed_oh_33_feeder_1.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Feeder 1 of 33kv Voltage CKT", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }


                    if (!(sp_oh_33_feeder_1.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed"))) {
                        try {
                            if (sp_oh_33_feeder_1_pole.getSelectedItem().toString().equals("Select")) {
                                Toast.makeText(c, "Select Feeder 1 pole", Toast.LENGTH_SHORT).show();
                                return true;
                            }

                            if (sp_oh_33_feeder_1_pole.getSelectedItem().toString().equals("Not in list(Enter Pole number)")) {
                                if (ed_oh_33_pole_1.getText().toString().isEmpty() || ed_oh_33_pole_1.getText().toString().equals("0")) {
                                    Toast.makeText(c, "Enter Feeder 1 pole", Toast.LENGTH_SHORT).show();
                                    return true;
                                }
                            }

                        } catch (Exception e) {
                            Toast.makeText(c, "Select Feeder 1 pole", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }

                }

                if ("2".equalsIgnoreCase(sp_oh_no_of_33_feeders.getSelectedItem().toString())
                        || "3".equalsIgnoreCase(sp_oh_no_of_33_feeders.getSelectedItem().toString())
                ) {
                    if (sp_oh_33_feeder_2.getSelectedItem().toString().equals("Select")) {
                        Toast.makeText(c, "Select Feeder 2 of 33kv Voltage CKT", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    if (sp_oh_33_feeder_2.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed")) {
                        if (ed_oh_33_feeder_2_ns.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Not Surveyed Feeder 2 of 33kv Voltage CKT", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }

                    if (sp_oh_33_feeder_2.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                        if (ed_oh_33_feeder_2.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Feeder 2 of 33kv Voltage CKT", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }

                    if (!(sp_oh_33_feeder_2.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed"))) {
                        try {
                            if (sp_oh_33_feeder_2_pole.getSelectedItem().toString().equals("Select")) {
                                Toast.makeText(c, "Select Feeder 2 pole", Toast.LENGTH_SHORT).show();
                                return true;
                            }

                            if (sp_oh_33_feeder_2_pole.getSelectedItem().toString().equals("Not in list(Enter Pole number)")) {
                                if (ed_oh_33_pole_2.getText().toString().isEmpty() || ed_oh_33_pole_2.getText().toString().equals("0")) {
                                    Toast.makeText(c, "Enter Feeder 2 pole", Toast.LENGTH_SHORT).show();
                                    return true;
                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(c, "Select Feeder 2 pole", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }


                }

                if ("3".equalsIgnoreCase(sp_oh_no_of_33_feeders.getSelectedItem().toString())
                ) {
                    if (sp_oh_33_feeder_3.getSelectedItem().toString().equals("Select")) {
                        Toast.makeText(c, "Select Feeder 3 of 33kv Voltage CKT", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    if (sp_oh_33_feeder_3.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed")) {
                        if (ed_oh_33_feeder_3_ns.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Not Surveyed Feeder 3 of 33kv Voltage CKT", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }

                    if (sp_oh_33_feeder_3.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                        if (ed_oh_33_feeder_3.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Feeder 3 of 33kv Voltage CKT", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }

                    if (!(sp_oh_33_feeder_3.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed"))) {
                        try {
                            if (sp_oh_33_feeder_3_pole.getSelectedItem().toString().equals("Select")) {
                                Toast.makeText(c, "Select Feeder 3 pole", Toast.LENGTH_SHORT).show();
                                return true;
                            }

                            if (sp_oh_33_feeder_3_pole.getSelectedItem().toString().equals("Not in list(Enter Pole number)")) {
                                if (ed_oh_33_pole_3.getText().toString().isEmpty() || ed_oh_33_pole_3.getText().toString().equals("0")) {
                                    Toast.makeText(c, "Enter Feeder 3 pole", Toast.LENGTH_SHORT).show();
                                    return true;
                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(c, "Select Feeder 3 pole", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }


                }

            }
        }


        return false;
    }

    private boolean validate11KVVoltageCircuit() {

        if (sp_ismulticircute.getSelectedItem().toString().equals("Yes")) {
            if (ch_oh_11kv_vlckts.isChecked()) {
                if ("1".equalsIgnoreCase(sp_oh_no_of_11_feeders.getSelectedItem().toString())
                        || "2".equalsIgnoreCase(sp_oh_no_of_11_feeders.getSelectedItem().toString())
                        || "3".equalsIgnoreCase(sp_oh_no_of_11_feeders.getSelectedItem().toString())
                ) {
                    if (sp_oh_11_feeder_1.getSelectedItem().toString().equals("Select")) {
                        Toast.makeText(c, "Select Feeder 1 of 11kv Voltage CKT", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    if (sp_oh_11_feeder_1.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed")) {
                        if (ed_oh_11_feeder_1_ns.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Not Surveyed Feeder 1 of 11kv Voltage CKT", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }

                    if (sp_oh_11_feeder_1.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                        if (ed_oh_11_feeder_1.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Feeder 1 of 11kv Voltage CKT", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }

                    if (!(sp_oh_11_feeder_1.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed"))) {
                        try {
                            if (sp_oh_11_feeder_1_pole.getSelectedItem().toString().equals("Select")) {
                                Toast.makeText(c, "Select Feeder 1 pole", Toast.LENGTH_SHORT).show();
                                return true;
                            }

                            if (sp_oh_11_feeder_1_pole.getSelectedItem().toString().equals("Not in list(Enter Pole number)")) {
                                if (ed_oh_11_pole_1.getText().toString().isEmpty() || ed_oh_11_pole_1.getText().toString().equals("0")) {
                                    Toast.makeText(c, "Enter Feeder 1 pole", Toast.LENGTH_SHORT).show();
                                    return true;
                                }
                            }

                        } catch (Exception e) {
                            Toast.makeText(c, "Select Feeder 1 pole", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }


                }

                if ("2".equalsIgnoreCase(sp_oh_no_of_11_feeders.getSelectedItem().toString())
                        || "3".equalsIgnoreCase(sp_oh_no_of_11_feeders.getSelectedItem().toString())
                ) {
                    if (sp_oh_11_feeder_2.getSelectedItem().toString().equals("Select")) {
                        Toast.makeText(c, "Select Feeder 2 of 11kv Voltage CKT", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    if (sp_oh_11_feeder_2.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed")) {
                        if (ed_oh_11_feeder_2_ns.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Not Surveyed Feeder 2 of 11kv Voltage CKT", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }

                    if (sp_oh_11_feeder_2.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                        if (ed_oh_11_feeder_2.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Feeder 2 of 11kv Voltage CKT", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }

                    if (!(sp_oh_11_feeder_2.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed"))) {
                        try {
                            if (sp_oh_11_feeder_2_pole.getSelectedItem().toString().equals("Select")) {
                                Toast.makeText(c, "Select Feeder 2 pole", Toast.LENGTH_SHORT).show();
                                return true;
                            }
                            if (sp_oh_11_feeder_2_pole.getSelectedItem().toString().equals("Not in list(Enter Pole number)")) {
                                if (ed_oh_11_pole_2.getText().toString().isEmpty() || ed_oh_11_pole_2.getText().toString().equals("0")) {
                                    Toast.makeText(c, "Enter Feeder 2 pole", Toast.LENGTH_SHORT).show();
                                    return true;
                                }
                            }

                        } catch (Exception e) {
                            Toast.makeText(c, "Select Feeder 2 pole", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }


                }

                if ("3".equalsIgnoreCase(sp_oh_no_of_11_feeders.getSelectedItem().toString())
                ) {
                    if (sp_oh_11_feeder_3.getSelectedItem().toString().equals("Select")) {
                        Toast.makeText(c, "Select Feeder 3 of 11kv Voltage CKT", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    if (sp_oh_11_feeder_3.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed")) {
                        if (ed_oh_11_feeder_3_ns.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Not Surveyed Feeder 3 of 11kv Voltage CKT", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }

                    if (sp_oh_11_feeder_3.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                        if (ed_oh_11_feeder_3.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Feeder 3 of 11kv Voltage CKT", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }

                    if (!(sp_oh_11_feeder_3.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed"))) {
                        try {
                            if (sp_oh_11_feeder_3_pole.getSelectedItem().toString().equals("Select")) {
                                Toast.makeText(c, "Select Feeder 3 pole", Toast.LENGTH_SHORT).show();
                                return true;
                            }
                            if (sp_oh_11_feeder_3_pole.getSelectedItem().toString().equals("Not in list(Enter Pole number)")) {
                                if (ed_oh_11_pole_3.getText().toString().isEmpty() || ed_oh_11_pole_3.getText().toString().equals("0")) {
                                    Toast.makeText(c, "Enter Feeder 3 pole", Toast.LENGTH_SHORT).show();
                                    return true;
                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(c, "Select Feeder 3 pole", Toast.LENGTH_SHORT).show();
                            return true;
                        }

                    }


                }

            }
        }


        return false;
    }

    private boolean validateLTVoltageCircuit() {

        if (sp_ismulticircute.getSelectedItem().toString().equals("Yes")) {
            if (ch_oh_lt_vlckts.isChecked()) {
                if ("1".equalsIgnoreCase(sp_oh_no_of_lt_feeders.getSelectedItem().toString())
                        || "2".equalsIgnoreCase(sp_oh_no_of_lt_feeders.getSelectedItem().toString())
                        || "3".equalsIgnoreCase(sp_oh_no_of_lt_feeders.getSelectedItem().toString())
                ) {

                    if (sp_oh_lt_feeder_1.getSelectedItem().toString().equals("Select")) {
                        Toast.makeText(c, "Select Feeder 1 of LT Voltage CKT", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    if (sp_oh_lt_feeder_1.getSelectedItem().toString().equalsIgnoreCase("LT CKT Not Surveyed")) {
                        if (ed_oh_lt_feeder_1_ns.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Not Surveyed LT CKT 1", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }

                    if (sp_oh_lt_feeder_1.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                        if (ed_oh_lt_feeder_1.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Feeder 1 of LT Voltage CKT", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }
                    if (!(sp_oh_lt_feeder_1.getSelectedItem().toString().equalsIgnoreCase("LT CKT Not Surveyed"))) {
                        try {
                            if (sp_oh_lt_feeder_1_pole.getSelectedItem().toString().equals("Select")) {
                                Toast.makeText(c, "Select Feeder 1 pole", Toast.LENGTH_SHORT).show();
                                return true;
                            }
                            if (sp_oh_lt_feeder_1_pole.getSelectedItem().toString().equals("Not in list(Enter Pole number)")) {
                                if (ed_oh_lt_pole_1.getText().toString().isEmpty() || ed_oh_lt_pole_1.getText().toString().equals("0")) {
                                    Toast.makeText(c, "Enter Feeder 1 pole", Toast.LENGTH_SHORT).show();
                                    return true;
                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(c, "Select Feeder 1 pole", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }


                }

                if ("2".equalsIgnoreCase(sp_oh_no_of_lt_feeders.getSelectedItem().toString())
                        || "3".equalsIgnoreCase(sp_oh_no_of_lt_feeders.getSelectedItem().toString())
                ) {
                    if (sp_oh_lt_feeder_2.getSelectedItem().toString().equals("Select")) {
                        Toast.makeText(c, "Select Feeder 2 of LT Voltage CKT", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    if (sp_oh_lt_feeder_2.getSelectedItem().toString().equalsIgnoreCase("LT CKT Not Surveyed")) {
                        if (ed_oh_lt_feeder_2_ns.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Not Surveyed LT CKT 2", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }

                    if (sp_oh_lt_feeder_2.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                        if (ed_oh_lt_feeder_2.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Feeder 2 of LT Voltage CKT", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }

                    if (!(sp_oh_lt_feeder_2.getSelectedItem().toString().equalsIgnoreCase("LT CKT Not Surveyed"))) {
                        try {
                            if (sp_oh_lt_feeder_2_pole.getSelectedItem().toString().equals("Select")) {
                                Toast.makeText(c, "Select Feeder 2 pole", Toast.LENGTH_SHORT).show();
                                return true;
                            }

                            if (sp_oh_lt_feeder_2_pole.getSelectedItem().toString().equals("Not in list(Enter Pole number)")) {
                                if (ed_oh_lt_pole_2.getText().toString().isEmpty() || ed_oh_lt_pole_2.getText().toString().equals("0")) {
                                    Toast.makeText(c, "Enter Feeder 2 pole", Toast.LENGTH_SHORT).show();
                                    return true;
                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(c, "Select Feeder 2 pole", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }


                }

                if ("3".equalsIgnoreCase(sp_oh_no_of_lt_feeders.getSelectedItem().toString())
                ) {
                    if (sp_oh_lt_feeder_3.getSelectedItem().toString().equals("Select")) {
                        Toast.makeText(c, "Select Feeder 3 of LT Voltage CKT", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    if (sp_oh_lt_feeder_3.getSelectedItem().toString().equalsIgnoreCase("LT CKT Not Surveyed")) {
                        if (ed_oh_lt_feeder_3_ns.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Not Surveyed LT CKT 3", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }

                    if (sp_oh_lt_feeder_3.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                        if (ed_oh_lt_feeder_3.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Feeder 3 of LT Voltage CKT", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }

                    if (!(sp_oh_lt_feeder_3.getSelectedItem().toString().equalsIgnoreCase("LT CKT Not Surveyed"))) {
                        try {
                            if (sp_oh_lt_feeder_3_pole.getSelectedItem().toString().equals("Select")) {
                                Toast.makeText(c, "Select Feeder 3 pole", Toast.LENGTH_SHORT).show();
                                return true;
                            }

                            if (sp_oh_lt_feeder_3_pole.getSelectedItem().toString().equals("Not in list(Enter Pole number)")) {
                                if (ed_oh_lt_pole_3.getText().toString().isEmpty() || ed_oh_lt_pole_3.getText().toString().equals("0")) {
                                    Toast.makeText(c, "Enter Feeder 3 pole", Toast.LENGTH_SHORT).show();
                                    return true;
                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(c, "Select Feeder 3 pole", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }


                }

            }
        }


        return false;
    }

    private boolean validateNoOfCircuits() {

        if (sp_ismulticircute.getSelectedItem().toString().equalsIgnoreCase("Yes")) {
            int selectedCkts = Integer.parseInt(sp_noofcircuit.getSelectedItem().toString()) - 1;


            if (!"Select".equalsIgnoreCase(sp_oh_no_of_33_feeders.getSelectedItem().toString()) && ch_oh_33kv_vlckts.isChecked()) {
                noof_33kvCkts = Integer.parseInt(sp_oh_no_of_33_feeders.getSelectedItem().toString());
            }

            if (!"Select".equalsIgnoreCase(sp_oh_no_of_11_feeders.getSelectedItem().toString()) && ch_oh_11kv_vlckts.isChecked()) {
                noof_11kvCkts = Integer.parseInt(sp_oh_no_of_11_feeders.getSelectedItem().toString());
            }

            if (!"Select".equalsIgnoreCase(sp_oh_no_of_lt_feeders.getSelectedItem().toString()) && ch_oh_lt_vlckts.isChecked()) {
                noof_ltCkts = Integer.parseInt(sp_oh_no_of_lt_feeders.getSelectedItem().toString());
            }

            int totalCKTS = noof_33kvCkts + noof_11kvCkts + noof_ltCkts;

            if (selectedCkts != totalCKTS) {
                return true;
            }


        }


        return false;
    }

    public void underGroundData() {
        try {

            if ("Select".equalsIgnoreCase(Survey_type_string)) {
                Toast.makeText(c, "Select Survey Type", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(sp_noofcircuit_underg.getSelectedItem().toString())) {
                Toast.makeText(c, "Select No.Of Cables", Toast.LENGTH_LONG).show();
            } /*else if ("Select".equalsIgnoreCase(straight_rmu)) {
                Toast.makeText(c, "Straight Through Joint/RMU", Toast.LENGTH_LONG).show();
            }*/ else if ("RMU".equalsIgnoreCase(straight_rmu) && "Select".equalsIgnoreCase(sp_rmu.getSelectedItem().toString())) {
                Toast.makeText(c, "Select RMU ", Toast.LENGTH_LONG).show();
            } else if ("Pole".equalsIgnoreCase(straight_rmu) && "Select".equalsIgnoreCase(sp_ugpoletype.getSelectedItem().toString())) {
                Toast.makeText(c, "Select Pole Type ", Toast.LENGTH_LONG).show();
            } else if ("Pole".equalsIgnoreCase(straight_rmu) && "Select".equalsIgnoreCase(sp_ugpoleheight.getSelectedItem().toString())) {
                Toast.makeText(c, "Select Pole Height ", Toast.LENGTH_LONG).show();
            } else if ("SS Bay".equalsIgnoreCase(ugpoletype) && "Select".equalsIgnoreCase(sp_ugssname.getSelectedItem().toString())) {
                Toast.makeText(c, "Select SS Name ", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(sp_ugtapingpoint.getSelectedItem().toString())) {
                Toast.makeText(c, "Select Tapping Point ", Toast.LENGTH_LONG).show();
            } else if ("Yes".equalsIgnoreCase(ugtapingpoint) && "Select".equalsIgnoreCase(sp_ugnooftappings.getSelectedItem().toString())) {
                Toast.makeText(c, "Select No.Of Tappings", Toast.LENGTH_LONG).show();
            } else if ("Yes".equalsIgnoreCase(ugtapingpoint) && edt_ugtappinglocation.getText().toString().isEmpty()) {
                Toast.makeText(c, "Enter Tapping Location Name", Toast.LENGTH_LONG).show();
            } else if ("Select".equalsIgnoreCase(cablesize)) {
                Toast.makeText(c, "Select Cable Size ", Toast.LENGTH_LONG).show();
            } /*else if ("Select".equalsIgnoreCase(sp_pole_concrete.getSelectedItem().toString())) {
                Toast.makeText(c, "Select Pole Concrete", Toast.LENGTH_LONG).show();
            }*/ else {
                if (feederCode != null & !feederCode.isEmpty() && !ed_pole_no.getText().toString().isEmpty() && !ed_frompole.getText().toString().isEmpty() && !x.isEmpty() && !y.isEmpty()) {

                    if (Integer.parseInt(ed_pole_no.getText().toString()) == Integer.parseInt(ed_frompole.getText().toString())) {
                        Toast.makeText(c, "Pole No and From Pole No Should Not Be Same Contact Admin", Toast.LENGTH_LONG).show();

                    } else {
                        List<String> woID = arcGISDatabase.getIncrementalFeederComplaint(String.format("SELECT DISTINCT WO_ID FROM EHTMASTER_TABLE WHERE FEEDER_CODE_33KV='%s'", feederCode));
                        List<String> isFeederType = arcGISDatabase.getIncrementalFeederComplaint(String.format("SELECT DISTINCT IS_FEEDER_TYPE FROM EHTMASTER_TABLE WHERE FEEDER_CODE_33KV='%s'", feederCode));
                        PMISurveyVo underGroundsurveyVo = new PMISurveyVo();
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
                        if (!edt_remarks_underground.getText().toString().isEmpty()) {
                            underGroundsurveyVo.setRemarks(edt_remarks_underground.getText().toString());
                        }
                        underGroundsurveyVo.setPoleStructure(underpolestructure);
                        if ("Select".equalsIgnoreCase(ugpoletype)) {
                            underGroundsurveyVo.setPole_type("");
                        } else {
                            underGroundsurveyVo.setPole_type(ugpoletype);
                        }
                        if ("Select".equalsIgnoreCase(ugpoleheight)) {
                            underGroundsurveyVo.setPole_height("");
                        } else {
                            underGroundsurveyVo.setPole_height(ugpoleheight);
                        }
                        underGroundsurveyVo.setPole_support(ugpolesupport);
                        underGroundsurveyVo.setSs_name(ugssname);
                        underGroundsurveyVo.setTapping_point(ugtapingpoint);
                        underGroundsurveyVo.setNoofTappings(ugnooftappings);
                        if (!edt_ugtappinglocation.getText().toString().isEmpty()) {
                            underGroundsurveyVo.setTappingLocation(edt_ugtappinglocation.getText().toString());
                        }
                        if (!edt_ugpolepaintingcode.getText().toString().isEmpty()) {
                            underGroundsurveyVo.setPole_paintingcode(edt_ugpolepaintingcode.getText().toString());
                        }
                        underGroundsurveyVo.setWoId(woID.get(0));
                        underGroundsurveyVo.setIsFeederType(isFeederType.get(0));
                        //underGroundsurveyVo.setSurvaytype(Survey_type_string);
                        underGroundsurveyVo.setSurvaytype("");
                        underGroundsurveyVo.setPOLE_CONCRETE("");

                        underGroundsurveyVo.setCURRENT_LAT(""+CURRENT_LAT);
                        underGroundsurveyVo.setCURRENT_LONG(""+CURRENT_LONG);


                        image_1_clicked = false;
                        image_2_clicked = false;
                        image_3_clicked = false;
                        repeatPoleClicked = false;
                        arcGISDatabase.addSurveyData(underGroundsurveyVo);
                        PMIArcGISMapActivity33KV.IS_SURVEY_STARTED = true;
                        Toast.makeText(c, "Pole location captured successfully..", Toast.LENGTH_SHORT).show();
                        dismiss();
                        callout.dismiss();
                        graphicsOverlay.getGraphics().remove(graphic);

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void multiCircuitListeners11KV() {

        sp_oh_no_of_11_feeders.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_no_of_feeders = sp_oh_no_of_11_feeders.getSelectedItem().toString();
                if (str_no_of_feeders.equalsIgnoreCase("Select")) {
                    main_layout_feeder_1.setVisibility(View.GONE);
                    main_layout_feeder_2.setVisibility(View.GONE);
                    main_layout_feeder_3.setVisibility(View.GONE);
                }
                if (str_no_of_feeders.equalsIgnoreCase("1")) {
                    main_layout_feeder_1.setVisibility(View.VISIBLE);
                    main_layout_feeder_2.setVisibility(View.GONE);
                    main_layout_feeder_3.setVisibility(View.GONE);
                }
                if (str_no_of_feeders.equalsIgnoreCase("2")) {
                    main_layout_feeder_1.setVisibility(View.VISIBLE);
                    main_layout_feeder_2.setVisibility(View.VISIBLE);
                    main_layout_feeder_3.setVisibility(View.GONE);
                }
                if (str_no_of_feeders.equalsIgnoreCase("3")) {
                    main_layout_feeder_1.setVisibility(View.VISIBLE);
                    main_layout_feeder_2.setVisibility(View.VISIBLE);
                    main_layout_feeder_3.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //FEEDER 1
        List<String> list_feeder_2 = new ArrayList<>();
        list_feeder_2.add("Select");
        List<String> list_feeder_1 = new ArrayList<>();
        list_feeder_1.add("Select");
        List<String> list_feeder_3 = new ArrayList<>();
        list_feeder_3.add("Select");
        //  list_feeder_1.addAll(PMISurveyHome33KV.filter11kv);

        //NEW START

        ArrayList<PMIMultiCktVO> filteredList = new ArrayList<>();
        //  ArrayList<String> filter33kv = new ArrayList<>();

        for (int j = 0; j < PMISurveyHome33KV.list11kv.size(); j++) {
           /* if (!feederCode.equals(PMISurveyHome33KV.list11kv.get(j).getMC_FEEDER_ID())){
                double distance = getDistanceBetweenPoles(PMISurveyHome33KV.list11kv.get(j).getGLAT(),PMISurveyHome33KV.list11kv.get(j).getGLON());
                // System.out.println("DISTANCE 33KV FEEDER IN 11KV MULTICKT OUTER : "+distance);
                if (distance<=100){
                    filteredList.add(PMISurveyHome33KV.list11kv.get(j));
                }
            }*/

            double distance = getDistanceBetweenPoles(PMISurveyHome33KV.list11kv.get(j).getGLAT(), PMISurveyHome33KV.list11kv.get(j).getGLON());
            // System.out.println("DISTANCE 33KV FEEDER IN 11KV MULTICKT OUTER : "+distance);
            if (distance <= 100) {
                filteredList.add(PMISurveyHome33KV.list11kv.get(j));
            }


        }

        List<String> combinedList = new ArrayList<>();
        for (int i = 0; i < filteredList.size(); i++) {
            combinedList.add(filteredList.get(i).getSHOW_NAME());
        }
        Set<String> set_feeder_id_1 = new LinkedHashSet<>();
        set_feeder_id_1.addAll(combinedList);
        combinedList.clear();
        combinedList.addAll(set_feeder_id_1);

        if (!combinedList.isEmpty()) {
            for (int i = 0; i < combinedList.size(); i++) {
                list_feeder_1.add(combinedList.get(i));
                list_feeder_2.add(combinedList.get(i));
                list_feeder_3.add(combinedList.get(i));
            }
        }


        //NEW END


        list_feeder_1.add("Other than List");
        list_feeder_1.add("Feeder Not Surveyed");
        ArrayAdapter<String> adapter_feeder_1 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_feeder_1);
        sp_oh_11_feeder_1.setAdapter(adapter_feeder_1);
        sp_oh_11_feeder_1.setSelection(0, true);

        //FEEDER 2

        //  list_feeder_2.addAll(PMISurveyHome33KV.filter11kv);


        list_feeder_2.add("Other than List");
        list_feeder_2.add("Feeder Not Surveyed");
        ArrayAdapter<String> adapter_feeder_2 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_feeder_2);
        sp_oh_11_feeder_2.setAdapter(adapter_feeder_2);
        sp_oh_11_feeder_2.setSelection(0, true);

        //FEEDER 3

        // list_feeder_3.addAll(PMISurveyHome33KV.filter11kv);
        list_feeder_3.add("Other than List");
        list_feeder_3.add("Feeder Not Surveyed");
        ArrayAdapter<String> adapter_feeder_3 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_feeder_3);
        sp_oh_11_feeder_3.setAdapter(adapter_feeder_3);
        sp_oh_11_feeder_3.setSelection(0, true);

        sp_oh_11_feeder_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (sp_oh_11_feeder_1.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                    layout_oh_11_feeder_1.setVisibility(View.VISIBLE);
                    feeder_1_pole_layout_11.setVisibility(View.VISIBLE);
                    ed_oh_11_feeder_1_ns.setVisibility(View.GONE);

                    /*ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMISurveyHome33KV.feederList11kv);
                    ed_oh_11_feeder_1.setAdapter(adapter_feeder);
                    ed_oh_11_feeder_1.setThreshold(5);*/



                    ed_oh_11_feeder_1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            open11KVFeederMasterDialog(ed_oh_11_feeder_1);
                        }
                    });

                    List<String> list = new ArrayList<>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_11_feeder_1_pole.setAdapter(adapter);
                } else if (sp_oh_11_feeder_1.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    List<String> list = new ArrayList<>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_11_feeder_1_pole.setAdapter(adapter);
                    layout_oh_11_feeder_1.setVisibility(View.GONE);
                    ed_oh_11_feeder_1_ns.setVisibility(View.GONE);
                } else if (sp_oh_11_feeder_1.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed")) {
                    layout_oh_11_feeder_1.setVisibility(View.GONE);
                    feeder_1_pole_layout_11.setVisibility(View.GONE);
                    ed_oh_11_feeder_1_ns.setVisibility(View.VISIBLE);

                    /*ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMISurveyHome33KV.feederList11kv);
                    ed_oh_11_feeder_1_ns.setAdapter(adapter_feeder);
                    ed_oh_11_feeder_1_ns.setThreshold(5);*/



                    ed_oh_11_feeder_1_ns.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            open11KVFeederMasterDialog(ed_oh_11_feeder_1_ns);
                        }
                    });

                } else {
                    layout_oh_11_feeder_1.setVisibility(View.GONE);
                    feeder_1_pole_layout_11.setVisibility(View.VISIBLE);
                    ed_oh_11_feeder_1_ns.setVisibility(View.GONE);
                    String selectedFeeder = sp_oh_11_feeder_1.getSelectedItem().toString();
                    String[] splitFeederId = selectedFeeder.split("~");
                    selectedFeeder = splitFeederId[0];
                    // System.out.println("SELECTED FEEDER 11KV 1 :"+selectedFeeder);
                    List<String> list = new ArrayList<>();
                    list.add("Select");
                    for (int j = 0; j < PMISurveyHome33KV.list11kv.size(); j++) {
                        if (selectedFeeder.equals(PMISurveyHome33KV.list11kv.get(j).getMC_FEEDER_ID())) {
                            double distance = getDistanceBetweenPoles(PMISurveyHome33KV.list11kv.get(j).getGLAT(), PMISurveyHome33KV.list11kv.get(j).getGLON());
                            // System.out.println("DISTANCE 11KV FEEDER 1 : "+distance);
                            // System.out.println("DISTANCE 11KV FEEDER 1 Pole : "+PMISurveyHome33KV.list11kv.get(j).getMC_POLE_NO());
                            if (distance <= 10) {
                                list.add("" + PMISurveyHome33KV.list11kv.get(j).getMC_POLE_NO());
                            }
                        }

                    }
                    /*if (list.size()==1){
                        list.remove("Select");
                        Toast.makeText(c, "No Poles Found", Toast.LENGTH_SHORT).show();
                    }*/
                    list.add("Not in list(Enter Pole number)");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_11_feeder_1_pole.setAdapter(adapter);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        sp_oh_11_feeder_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (sp_oh_11_feeder_2.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                    layout_oh_11_feeder_2.setVisibility(View.VISIBLE);
                    feeder_2_pole_layout_11.setVisibility(View.VISIBLE);
                    ed_oh_11_feeder_2_ns.setVisibility(View.GONE);

                    /*ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMISurveyHome33KV.feederList11kv);
                    ed_oh_11_feeder_2.setAdapter(adapter_feeder);
                    ed_oh_11_feeder_2.setThreshold(5);*/



                    ed_oh_11_feeder_2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            open11KVFeederMasterDialog(ed_oh_11_feeder_2);
                        }
                    });

                    List<String> list = new ArrayList<>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_11_feeder_2_pole.setAdapter(adapter);
                } else if (sp_oh_11_feeder_2.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed")) {
                    layout_oh_11_feeder_2.setVisibility(View.GONE);
                    feeder_2_pole_layout_11.setVisibility(View.GONE);
                    ed_oh_11_feeder_2_ns.setVisibility(View.VISIBLE);

                    /*ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMISurveyHome33KV.feederList11kv);
                    ed_oh_11_feeder_2_ns.setAdapter(adapter_feeder);
                    ed_oh_11_feeder_2_ns.setThreshold(5);*/



                    ed_oh_11_feeder_2_ns.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            open11KVFeederMasterDialog(ed_oh_11_feeder_2_ns);
                        }
                    });

                } else if (sp_oh_11_feeder_2.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    layout_oh_11_feeder_2.setVisibility(View.GONE);
                    ed_oh_11_feeder_2_ns.setVisibility(View.GONE);
                    List<String> list = new ArrayList<>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_11_feeder_2_pole.setAdapter(adapter);
                } else {
                    layout_oh_11_feeder_2.setVisibility(View.GONE);
                    feeder_2_pole_layout_11.setVisibility(View.VISIBLE);
                    ed_oh_11_feeder_2_ns.setVisibility(View.GONE);
                    String selectedFeeder = sp_oh_11_feeder_2.getSelectedItem().toString();
                    String[] splitFeederId = selectedFeeder.split("~");
                    selectedFeeder = splitFeederId[0];
                    List<String> list = new ArrayList<>();
                    list.add("Select");
                    for (int j = 0; j < PMISurveyHome33KV.list11kv.size(); j++) {
                        if (selectedFeeder.equals(PMISurveyHome33KV.list11kv.get(j).getMC_FEEDER_ID())) {
                            double distance = getDistanceBetweenPoles(PMISurveyHome33KV.list11kv.get(j).getGLAT(), PMISurveyHome33KV.list11kv.get(j).getGLON());
                            //System.out.println("DISTANCE 11KV FEEDER 2 : "+distance);
                            if (distance <= 10) {
                                list.add("" + PMISurveyHome33KV.list11kv.get(j).getMC_POLE_NO());
                            }
                        }

                    }
                   /* if (list.size()==1){
                        list.remove("Select");
                        Toast.makeText(c, "No Poles Found", Toast.LENGTH_SHORT).show();
                    }*/
                    list.add("Not in list(Enter Pole number)");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_11_feeder_2_pole.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_oh_11_feeder_3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (sp_oh_11_feeder_3.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                    layout_oh_11_feeder_3.setVisibility(View.VISIBLE);
                    feeder_3_pole_layout_11.setVisibility(View.VISIBLE);
                    ed_oh_11_feeder_3_ns.setVisibility(View.GONE);

                    /*ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMISurveyHome33KV.feederList11kv);
                    ed_oh_11_feeder_3.setAdapter(adapter_feeder);
                    ed_oh_11_feeder_3.setThreshold(5);*/



                    ed_oh_11_feeder_3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            open11KVFeederMasterDialog(ed_oh_11_feeder_3);
                        }
                    });

                    List<String> list = new ArrayList<>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_11_feeder_3_pole.setAdapter(adapter);
                } else if (sp_oh_11_feeder_3.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed")) {
                    layout_oh_11_feeder_3.setVisibility(View.GONE);
                    feeder_3_pole_layout_11.setVisibility(View.GONE);
                    ed_oh_11_feeder_3_ns.setVisibility(View.VISIBLE);

                   /* ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMISurveyHome33KV.feederList11kv);
                    ed_oh_11_feeder_3_ns.setAdapter(adapter_feeder);
                    ed_oh_11_feeder_3_ns.setThreshold(5);*/



                    ed_oh_11_feeder_3_ns.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            open11KVFeederMasterDialog(ed_oh_11_feeder_3_ns);
                        }
                    });

                } else if (sp_oh_11_feeder_3.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    layout_oh_11_feeder_3.setVisibility(View.GONE);
                    ed_oh_11_feeder_3_ns.setVisibility(View.GONE);
                    List<String> list = new ArrayList<>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_11_feeder_3_pole.setAdapter(adapter);
                } else {
                    layout_oh_11_feeder_3.setVisibility(View.GONE);
                    feeder_3_pole_layout_11.setVisibility(View.VISIBLE);
                    ed_oh_11_feeder_3_ns.setVisibility(View.GONE);
                    String selectedFeeder = sp_oh_11_feeder_2.getSelectedItem().toString();
                    String[] splitFeederId = selectedFeeder.split("~");
                    selectedFeeder = splitFeederId[0];
                    List<String> list = new ArrayList<>();
                    list.add("Select");
                    for (int j = 0; j < PMISurveyHome33KV.list11kv.size(); j++) {
                        if (selectedFeeder.equals(PMISurveyHome33KV.list11kv.get(j).getMC_FEEDER_ID())) {
                            double distance = getDistanceBetweenPoles(PMISurveyHome33KV.list11kv.get(j).getGLAT(), PMISurveyHome33KV.list11kv.get(j).getGLON());
                            //System.out.println("DISTANCE 11KV FEEDER 3 : "+distance);
                            if (distance <= 10) {
                                list.add("" + PMISurveyHome33KV.list11kv.get(j).getMC_POLE_NO());
                            }
                        }

                    }
                    /*if (list.size()==1){
                        list.remove("Select");
                        Toast.makeText(c, "No Poles Found", Toast.LENGTH_SHORT).show();
                    }*/
                    list.add("Not in list(Enter Pole number)");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_11_feeder_3_pole.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        get_oh_11_feeder_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_oh_11_feeder_1.getText().toString().isEmpty()) {
                    Toast.makeText(c, "Please Enter Feeder 1 ID", Toast.LENGTH_SHORT).show();
                } else if (ed_oh_11_feeder_1.getText().toString().equals(ed_oh_11_feeder_2.getText().toString())) {
                    Toast.makeText(c, "Feeder 1 & 2 IDs should not be same", Toast.LENGTH_SHORT).show();
                } else if (ed_oh_11_feeder_1.getText().toString().equals(ed_oh_11_feeder_3.getText().toString())) {
                    Toast.makeText(c, "Feeder 1 & 3 IDs should not be same", Toast.LENGTH_SHORT).show();
                } else {
                    get11KVFeeder1Pole();
                }
            }
        });

        get_oh_11_feeder_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_oh_11_feeder_2.getText().toString().isEmpty()) {
                    Toast.makeText(c, "Please Enter Feeder 2 ID", Toast.LENGTH_SHORT).show();
                } else if (ed_oh_11_feeder_2.getText().toString().equals(ed_oh_11_feeder_1.getText().toString())) {
                    Toast.makeText(c, "Feeder 2 & 1 IDs should not be same", Toast.LENGTH_SHORT).show();
                } else if (ed_oh_11_feeder_2.getText().toString().equals(ed_oh_11_feeder_3.getText().toString())) {
                    Toast.makeText(c, "Feeder 2 & 3 IDs should not be same", Toast.LENGTH_SHORT).show();
                } else {
                    get11KVFeeder2Pole();
                }
            }
        });

        get_oh_11_feeder_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_oh_11_feeder_3.getText().toString().isEmpty()) {
                    Toast.makeText(c, "Please Enter Feeder 3 ID", Toast.LENGTH_SHORT).show();
                } else if (ed_oh_11_feeder_3.getText().toString().equals(ed_oh_11_feeder_1.getText().toString())) {
                    Toast.makeText(c, "Feeder 3 & 1 IDs should not be same", Toast.LENGTH_SHORT).show();
                } else if (ed_oh_11_feeder_3.getText().toString().equals(ed_oh_11_feeder_2.getText().toString())) {
                    Toast.makeText(c, "Feeder 3 & 2 IDs should not be same", Toast.LENGTH_SHORT).show();
                } else {
                    get11KVFeeder3Pole();
                }
            }
        });

        sp_oh_11_feeder_1_pole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_oh_11_feeder_1_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                    ed_oh_11_pole_1.setVisibility(View.VISIBLE);
                } else {
                    ed_oh_11_pole_1.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_oh_11_feeder_2_pole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_oh_11_feeder_2_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                    ed_oh_11_pole_2.setVisibility(View.VISIBLE);
                } else {
                    ed_oh_11_pole_2.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_oh_11_feeder_3_pole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_oh_11_feeder_3_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                    ed_oh_11_pole_3.setVisibility(View.VISIBLE);
                } else {
                    ed_oh_11_pole_3.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void open11KVFeederMasterDialog(TextView edt_text) {
        Dialog payDialog = new Dialog(c);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        c.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        payDialog.setContentView(R.layout.feeder_master_dialog);
        payDialog.getWindow().setLayout(width,height/2);
        payDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        payDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        TextView cancel_button = payDialog.findViewById(R.id.dialog_cancel);
        TextView back_to_home = payDialog.findViewById(R.id.back_to_home);
        TextView title = payDialog.findViewById(R.id.title);
        title.setText("SELECT 11KV FEEDER !!");

        RadioGroup radioGroup = payDialog.findViewById(R.id.radioGroup);


        payDialog.show();

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = payDialog.findViewById(selectedId);
                String selectedRadioButton = radioButton.getText().toString();
                payDialog.dismiss();
                if (selectedRadioButton.equals(" From Same Section")){
                    SearchableSpinner searchableSpinner = new SearchableSpinner(c);
                    searchableSpinner.setWindowTitle("SELECT FEEDER");
                    ArrayList<String> feedersList = new ArrayList<>();
                    List<LoginDataResponse> loginDataResponses = new PMIArcGISDatabase(c).getUserMasterData();
                    String office_id = loginDataResponses.get(0).getOFFICE_ID();
                    // System.out.println("OFFICE ID : "+office_id);
                    for (int i = 0; i < PMISurveyHome33KV.feederMasterList11KV.size(); i++) {
                        if (PMISurveyHome33KV.feederMasterList11KV.get(i).getSEC_ID().equals(office_id)){
                            //  System.out.println("SEC ID : "+DTRSelectActivity.feederMasterList11KV.get(i).getSEC_ID());
                            feedersList.add(PMISurveyHome33KV.feederMasterList11KV.get(i).getFEEDER_ID()
                                    +"~"+PMISurveyHome33KV.feederMasterList11KV.get(i).getFEEDER_NAME());
                        }
                    }
                    searchableSpinner.setSpinnerListItems(feedersList);
                    searchableSpinner.show();
                    searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                        @Override
                        public void setOnItemSelectListener(int position, String selectedString) {
                            edt_text.setText(selectedString);
                        }
                    });

                }else {
                    SearchableSpinner searchableSpinner = new SearchableSpinner(c);
                    searchableSpinner.setWindowTitle("SELECT FEEDER");
                    ArrayList<String> searchList = new ArrayList<>(PMISurveyHome33KV.feederList11kv);
                    if (searchList.contains(feederCode + "~" + feederName)) {
                        searchList.remove(feederCode + "~" + feederName);
                    }

                    searchableSpinner.setSpinnerListItems(searchList);
                    searchableSpinner.show();
                    searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                        @Override
                        public void setOnItemSelectListener(int position, String selectedString) {
                            edt_text.setText(selectedString);
                        }
                    });
                }
            }
        });
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payDialog.dismiss();
            }
        });


    }

    private void open33KVFeederMasterDialog(TextView edt_text) {
        Dialog payDialog = new Dialog(c);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        c.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        payDialog.setContentView(R.layout.feeder_master_dialog);
        payDialog.getWindow().setLayout(width,height/2);
        payDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        payDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        TextView cancel_button = payDialog.findViewById(R.id.dialog_cancel);
        TextView back_to_home = payDialog.findViewById(R.id.back_to_home);
        TextView title = payDialog.findViewById(R.id.title);
        title.setText("SELECT 33KV FEEDER !!");

        RadioGroup radioGroup = payDialog.findViewById(R.id.radioGroup);


        payDialog.show();

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = payDialog.findViewById(selectedId);
                String selectedRadioButton = radioButton.getText().toString();
                payDialog.dismiss();
                if (selectedRadioButton.equals(" From Same Section")){
                    SearchableSpinner searchableSpinner = new SearchableSpinner(c);
                    searchableSpinner.setWindowTitle("SELECT FEEDER");
                    ArrayList<String> feedersList = new ArrayList<>();
                    for (int i = 0; i < PMISurveyHome33KV.feederMasterList33KV.size(); i++) {
                        if (PMISurveyHome33KV.feederMasterList33KV.get(i).getSEC_ID().equals("SAME")){
                            feedersList.add(PMISurveyHome33KV.feederMasterList33KV.get(i).getFEEDER_ID()
                                    +"~"+PMISurveyHome33KV.feederMasterList33KV.get(i).getFEEDER_NAME());
                        }
                    }
                    searchableSpinner.setSpinnerListItems(feedersList);
                    searchableSpinner.show();
                    searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                        @Override
                        public void setOnItemSelectListener(int position, String selectedString) {
                            edt_text.setText(selectedString);
                        }
                    });

                }else {
                    SearchableSpinner searchableSpinner = new SearchableSpinner(c);
                    searchableSpinner.setWindowTitle("SELECT FEEDER");
                    searchableSpinner.setSpinnerListItems(PMISurveyHome33KV.feederList33kv);
                    searchableSpinner.show();
                    searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                        @Override
                        public void setOnItemSelectListener(int position, String selectedString) {
                            edt_text.setText(selectedString);
                        }
                    });
                }
            }
        });
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payDialog.dismiss();
            }
        });


    }

    private void multiCircuitListeners33KV() {

        sp_oh_no_of_33_feeders.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_no_of_feeders = sp_oh_no_of_33_feeders.getSelectedItem().toString();
                if (str_no_of_feeders.equalsIgnoreCase("Select")) {
                    main_layout_feeder_1_33.setVisibility(View.GONE);
                    main_layout_feeder_2_33.setVisibility(View.GONE);
                    main_layout_feeder_3_33.setVisibility(View.GONE);
                }
                if (str_no_of_feeders.equalsIgnoreCase("1")) {
                    main_layout_feeder_1_33.setVisibility(View.VISIBLE);
                    main_layout_feeder_2_33.setVisibility(View.GONE);
                    main_layout_feeder_3_33.setVisibility(View.GONE);
                }
                if (str_no_of_feeders.equalsIgnoreCase("2")) {
                    main_layout_feeder_1_33.setVisibility(View.VISIBLE);
                    main_layout_feeder_2_33.setVisibility(View.VISIBLE);
                    main_layout_feeder_3_33.setVisibility(View.GONE);
                }
                if (str_no_of_feeders.equalsIgnoreCase("3")) {
                    main_layout_feeder_1_33.setVisibility(View.VISIBLE);
                    main_layout_feeder_2_33.setVisibility(View.VISIBLE);
                    main_layout_feeder_3_33.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //FEEDER 1
        List<String> list_feeder_1 = new ArrayList<>();
        list_feeder_1.add("Select");
        List<String> list_feeder_2 = new ArrayList<>();
        list_feeder_2.add("Select");
        List<String> list_feeder_3 = new ArrayList<>();
        list_feeder_3.add("Select");

        //NEW START
        ArrayList<PMIMultiCktVO> filteredList = new ArrayList<>();
        //  ArrayList<String> filter33kv = new ArrayList<>();

        for (int j = 0; j < PMISurveyHome33KV.list33kv.size(); j++) {
            if (!feederCode.equals(PMISurveyHome33KV.list33kv.get(j).getMC_FEEDER_ID())) {
                double distance = getDistanceBetweenPoles(PMISurveyHome33KV.list33kv.get(j).getGLAT(), PMISurveyHome33KV.list33kv.get(j).getGLON());
                // System.out.println("DISTANCE 33KV FEEDER IN 11KV MULTICKT OUTER : "+distance);
                if (distance <= 100) {
                    filteredList.add(PMISurveyHome33KV.list33kv.get(j));
                }
            }

        }

        List<String> combinedList = new ArrayList<>();
        for (int i = 0; i < filteredList.size(); i++) {
            combinedList.add(filteredList.get(i).getSHOW_NAME());
        }
        Set<String> set_feeder_id_1 = new LinkedHashSet<>();
        set_feeder_id_1.addAll(combinedList);
        combinedList.clear();
        combinedList.addAll(set_feeder_id_1);

        if (!combinedList.isEmpty()) {
            for (int i = 0; i < combinedList.size(); i++) {
                list_feeder_1.add(combinedList.get(i));
                list_feeder_2.add(combinedList.get(i));
                list_feeder_3.add(combinedList.get(i));
            }
        }

        //NEW END

        //list_feeder_1.addAll(PMISurveyHome33KV.filter33kv);
        list_feeder_1.add("Other than List");
        list_feeder_1.add("Feeder Not Surveyed");
        ArrayAdapter<String> adapter_feeder_1 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_feeder_1);
        sp_oh_33_feeder_1.setAdapter(adapter_feeder_1);
        sp_oh_33_feeder_1.setSelection(0, true);

        //FEEDER 2

        //list_feeder_2.addAll(PMISurveyHome33KV.filter33kv);
        list_feeder_2.add("Other than List");
        list_feeder_2.add("Feeder Not Surveyed");
        ArrayAdapter<String> adapter_feeder_2 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_feeder_2);
        sp_oh_33_feeder_2.setAdapter(adapter_feeder_2);
        sp_oh_33_feeder_2.setSelection(0, true);

        //FEEDER 3

        //list_feeder_3.addAll(PMISurveyHome33KV.filter33kv);
        list_feeder_3.add("Other than List");
        list_feeder_3.add("Feeder Not Surveyed");
        ArrayAdapter<String> adapter_feeder_3 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_feeder_3);
        sp_oh_33_feeder_3.setAdapter(adapter_feeder_3);
        sp_oh_33_feeder_3.setSelection(0, true);

        sp_oh_33_feeder_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (sp_oh_33_feeder_1.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                    layout_oh_33_feeder_1.setVisibility(View.VISIBLE);
                    feeder_1_pole_layout_33.setVisibility(View.VISIBLE);
                    ed_oh_33_feeder_1_ns.setVisibility(View.GONE);

                    /*ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMISurveyHome33KV.feederList33kv);
                    ed_oh_33_feeder_1.setAdapter(adapter_feeder);
                    ed_oh_33_feeder_1.setThreshold(5);*/



                    ed_oh_33_feeder_1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            open33KVFeederMasterDialog(ed_oh_33_feeder_1);
                        }
                    });


                    List<String> list = new ArrayList<>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_33_feeder_1_pole.setAdapter(adapter);
                } else if (sp_oh_33_feeder_1.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed")) {
                    layout_oh_33_feeder_1.setVisibility(View.GONE);
                    feeder_1_pole_layout_33.setVisibility(View.GONE);
                    ed_oh_33_feeder_1_ns.setVisibility(View.VISIBLE);

                    /*ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMISurveyHome33KV.feederList33kv);
                    ed_oh_33_feeder_1_ns.setAdapter(adapter_feeder);
                    ed_oh_33_feeder_1_ns.setThreshold(5);*/

                    /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMISurveyHome33KV.feederList33kv);
                    adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    ed_oh_33_feeder_1_ns.setAdapter(adapter);*/

                    //Optional Parameters


                    ed_oh_33_feeder_1_ns.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            open33KVFeederMasterDialog(ed_oh_33_feeder_1_ns);
                        }
                    });


                } else if (sp_oh_33_feeder_1.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    List<String> list = new ArrayList<>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_33_feeder_1_pole.setAdapter(adapter);
                    layout_oh_33_feeder_1.setVisibility(View.GONE);
                    ed_oh_33_feeder_1_ns.setVisibility(View.GONE);
                } else {
                    layout_oh_33_feeder_1.setVisibility(View.GONE);
                    feeder_1_pole_layout_33.setVisibility(View.VISIBLE);
                    ed_oh_33_feeder_1_ns.setVisibility(View.GONE);
                    String selectedFeeder = sp_oh_33_feeder_1.getSelectedItem().toString();
                    String[] splitFeederId = selectedFeeder.split("~");
                    selectedFeeder = splitFeederId[0];
                    List<String> list = new ArrayList<>();
                    list.add("Select");
                    for (int j = 0; j < PMISurveyHome33KV.list33kv.size(); j++) {
                        if (selectedFeeder.equals(PMISurveyHome33KV.list33kv.get(j).getMC_FEEDER_ID())) {
                            double distance = getDistanceBetweenPoles(PMISurveyHome33KV.list33kv.get(j).getGLAT(), PMISurveyHome33KV.list33kv.get(j).getGLON());
                            //System.out.println("DISTANCE 33KV FEEDER 1 : "+distance);
                            if (distance <= 10) {
                                list.add("" + PMISurveyHome33KV.list33kv.get(j).getMC_POLE_NO());
                            }
                        }
                    }
                    list.add("Not in list(Enter Pole number)");
                   /* if (list.size()==1){
                        list.remove("Select");
                        Toast.makeText(c, "No Poles Found", Toast.LENGTH_SHORT).show();
                    }*/
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_33_feeder_1_pole.setAdapter(adapter);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_oh_33_feeder_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (sp_oh_33_feeder_2.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                    layout_oh_33_feeder_2.setVisibility(View.VISIBLE);
                    feeder_2_pole_layout_33.setVisibility(View.VISIBLE);
                    ed_oh_33_feeder_2_ns.setVisibility(View.GONE);

                   /* ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMISurveyHome33KV.feederList33kv);
                    ed_oh_33_feeder_2.setAdapter(adapter_feeder);
                    ed_oh_33_feeder_2.setThreshold(5);*/



                    ed_oh_33_feeder_2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            open33KVFeederMasterDialog(ed_oh_33_feeder_2);
                        }
                    });

                    List<String> list = new ArrayList<>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_33_feeder_2_pole.setAdapter(adapter);
                } else if (sp_oh_33_feeder_2.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed")) {
                    layout_oh_33_feeder_2.setVisibility(View.GONE);
                    feeder_2_pole_layout_33.setVisibility(View.GONE);
                    ed_oh_33_feeder_2_ns.setVisibility(View.VISIBLE);

                   /* ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMISurveyHome33KV.feederList33kv);
                    ed_oh_33_feeder_2_ns.setAdapter(adapter_feeder);
                    ed_oh_33_feeder_2_ns.setThreshold(5);*/



                    ed_oh_33_feeder_2_ns.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            open33KVFeederMasterDialog(ed_oh_33_feeder_2_ns);
                        }
                    });

                } else if (sp_oh_33_feeder_2.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    layout_oh_33_feeder_2.setVisibility(View.GONE);
                    ed_oh_33_feeder_2_ns.setVisibility(View.GONE);
                    List<String> list = new ArrayList<>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_33_feeder_2_pole.setAdapter(adapter);
                } else {
                    layout_oh_33_feeder_2.setVisibility(View.GONE);
                    feeder_2_pole_layout_33.setVisibility(View.VISIBLE);
                    ed_oh_33_feeder_2_ns.setVisibility(View.GONE);
                    String selectedFeeder = sp_oh_33_feeder_2.getSelectedItem().toString();
                    String[] splitFeederId = selectedFeeder.split("~");
                    selectedFeeder = splitFeederId[0];
                    List<String> list = new ArrayList<>();
                    list.add("Select");
                    for (int j = 0; j < PMISurveyHome33KV.list33kv.size(); j++) {
                        if (selectedFeeder.equals(PMISurveyHome33KV.list33kv.get(j).getMC_FEEDER_ID())) {
                            double distance = getDistanceBetweenPoles(PMISurveyHome33KV.list33kv.get(j).getGLAT(), PMISurveyHome33KV.list33kv.get(j).getGLON());
                            //System.out.println("DISTANCE 33KV FEEDER 2 : "+distance);
                            if (distance <= 10) {
                                list.add("" + PMISurveyHome33KV.list33kv.get(j).getMC_POLE_NO());
                            }
                        }
                    }
                    list.add("Not in list(Enter Pole number)");
                    /*if (list.size()==1){
                        list.remove("Select");
                        Toast.makeText(c, "No Poles Found", Toast.LENGTH_SHORT).show();
                    }*/
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_33_feeder_2_pole.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_oh_33_feeder_3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (sp_oh_33_feeder_3.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                    layout_oh_33_feeder_3.setVisibility(View.VISIBLE);
                    feeder_3_pole_layout_33.setVisibility(View.VISIBLE);
                    ed_oh_33_feeder_3_ns.setVisibility(View.GONE);

                   /* ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMISurveyHome33KV.feederList33kv);
                    ed_oh_33_feeder_3.setAdapter(adapter_feeder);
                    ed_oh_33_feeder_3.setThreshold(5);*/



                    ed_oh_33_feeder_3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            open33KVFeederMasterDialog(ed_oh_33_feeder_3);
                        }
                    });

                    List<String> list = new ArrayList<>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_33_feeder_3_pole.setAdapter(adapter);
                } else if (sp_oh_33_feeder_3.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed")) {
                    layout_oh_33_feeder_3.setVisibility(View.GONE);
                    feeder_3_pole_layout_33.setVisibility(View.GONE);
                    ed_oh_33_feeder_3_ns.setVisibility(View.VISIBLE);

                    /*ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMISurveyHome33KV.feederList33kv);
                    ed_oh_33_feeder_3_ns.setAdapter(adapter_feeder);
                    ed_oh_33_feeder_3_ns.setThreshold(5);*/



                    ed_oh_33_feeder_3_ns.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            open33KVFeederMasterDialog(ed_oh_33_feeder_3_ns);
                        }
                    });

                } else if (sp_oh_33_feeder_3.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    layout_oh_33_feeder_3.setVisibility(View.GONE);
                    ed_oh_33_feeder_3_ns.setVisibility(View.GONE);
                    List<String> list = new ArrayList<>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_33_feeder_3_pole.setAdapter(adapter);
                } else {
                    layout_oh_33_feeder_3.setVisibility(View.GONE);
                    feeder_3_pole_layout_33.setVisibility(View.VISIBLE);
                    ed_oh_33_feeder_3_ns.setVisibility(View.GONE);
                    String selectedFeeder = sp_oh_33_feeder_3.getSelectedItem().toString();
                    String[] splitFeederId = selectedFeeder.split("~");
                    selectedFeeder = splitFeederId[0];
                    List<String> list = new ArrayList<>();
                    list.add("Select");
                    for (int j = 0; j < PMISurveyHome33KV.list33kv.size(); j++) {
                        if (selectedFeeder.equals(PMISurveyHome33KV.list33kv.get(j).getMC_FEEDER_ID())) {
                            double distance = getDistanceBetweenPoles(PMISurveyHome33KV.list33kv.get(j).getGLAT(), PMISurveyHome33KV.list33kv.get(j).getGLON());
                            //System.out.println("DISTANCE 33KV FEEDER 3 : "+distance);
                            if (distance <= 10) {
                                list.add("" + PMISurveyHome33KV.list33kv.get(j).getMC_POLE_NO());
                            }
                        }
                    }
                    list.add("Not in list(Enter Pole number)");
                    /*if (list.size()==1){
                        list.remove("Select");
                        Toast.makeText(c, "No Poles Found", Toast.LENGTH_SHORT).show();
                    }*/
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_33_feeder_3_pole.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        get_oh_33_feeder_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_oh_33_feeder_1.getText().toString().isEmpty()) {
                    Toast.makeText(c, "Please Enter Feeder 1 ID", Toast.LENGTH_SHORT).show();
                } else if (ed_oh_33_feeder_1.getText().toString().equals(ed_oh_33_feeder_2.getText().toString())) {
                    Toast.makeText(c, "Feeder 1 & 2 IDs should not be same", Toast.LENGTH_SHORT).show();
                } else if (ed_oh_33_feeder_1.getText().toString().equals(ed_oh_33_feeder_3.getText().toString())) {
                    Toast.makeText(c, "Feeder 1 & 3 IDs should not be same", Toast.LENGTH_SHORT).show();
                } else {
                    get33KVFeeder1Pole();
                }
            }
        });

        get_oh_33_feeder_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_oh_33_feeder_2.getText().toString().isEmpty()) {
                    Toast.makeText(c, "Please Enter Feeder 2 ID", Toast.LENGTH_SHORT).show();
                } else if (ed_oh_33_feeder_2.getText().toString().equals(ed_oh_33_feeder_1.getText().toString())) {
                    Toast.makeText(c, "Feeder 2 & 1 IDs should not be same", Toast.LENGTH_SHORT).show();
                } else if (ed_oh_33_feeder_2.getText().toString().equals(ed_oh_33_feeder_3.getText().toString())) {
                    Toast.makeText(c, "Feeder 2 & 3 IDs should not be same", Toast.LENGTH_SHORT).show();
                } else {
                    get33KVFeeder2Pole();
                }
            }
        });

        get_oh_33_feeder_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_oh_33_feeder_3.getText().toString().isEmpty()) {
                    Toast.makeText(c, "Please Enter Feeder 3 ID", Toast.LENGTH_SHORT).show();
                } else if (ed_oh_33_feeder_3.getText().toString().equals(ed_oh_33_feeder_1.getText().toString())) {
                    Toast.makeText(c, "Feeder 3 & 1 IDs should not be same", Toast.LENGTH_SHORT).show();
                } else if (ed_oh_33_feeder_3.getText().toString().equals(ed_oh_33_feeder_2.getText().toString())) {
                    Toast.makeText(c, "Feeder 3 & 2 IDs should not be same", Toast.LENGTH_SHORT).show();
                } else {
                    get33KVFeeder3Pole();
                }
            }
        });

        sp_oh_33_feeder_1_pole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_oh_33_feeder_1_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                    ed_oh_33_pole_1.setVisibility(View.VISIBLE);
                } else {
                    ed_oh_33_pole_1.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_oh_33_feeder_2_pole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_oh_33_feeder_2_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                    ed_oh_33_pole_2.setVisibility(View.VISIBLE);
                } else {
                    ed_oh_33_pole_2.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_oh_33_feeder_3_pole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_oh_33_feeder_3_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                    ed_oh_33_pole_3.setVisibility(View.VISIBLE);
                } else {
                    ed_oh_33_pole_3.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void multiCircuitListenersLT() {

        sp_oh_no_of_lt_feeders.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_no_of_feeders = sp_oh_no_of_lt_feeders.getSelectedItem().toString();
                if (str_no_of_feeders.equalsIgnoreCase("Select")) {
                    main_layout_feeder_1_lt.setVisibility(View.GONE);
                    main_layout_feeder_2_lt.setVisibility(View.GONE);
                    main_layout_feeder_3_lt.setVisibility(View.GONE);
                }
                if (str_no_of_feeders.equalsIgnoreCase("1")) {
                    main_layout_feeder_1_lt.setVisibility(View.VISIBLE);
                    main_layout_feeder_2_lt.setVisibility(View.GONE);
                    main_layout_feeder_3_lt.setVisibility(View.GONE);
                }
                if (str_no_of_feeders.equalsIgnoreCase("2")) {
                    main_layout_feeder_1_lt.setVisibility(View.VISIBLE);
                    main_layout_feeder_2_lt.setVisibility(View.VISIBLE);
                    main_layout_feeder_3_lt.setVisibility(View.GONE);
                }
                if (str_no_of_feeders.equalsIgnoreCase("3")) {
                    main_layout_feeder_1_lt.setVisibility(View.VISIBLE);
                    main_layout_feeder_2_lt.setVisibility(View.VISIBLE);
                    main_layout_feeder_3_lt.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //FEEDER 1
        List<String> list_feeder_1 = new ArrayList<>();
        list_feeder_1.add("Select");
        List<String> list_feeder_2 = new ArrayList<>();
        list_feeder_2.add("Select");
        List<String> list_feeder_3 = new ArrayList<>();
        list_feeder_3.add("Select");

        // list_feeder_1.addAll(PMISurveyHome33KV.filterLT);

        //NEW START
        ArrayList<PMIMultiCktVO> filteredList = new ArrayList<>();
        //ArrayList<String> filter33kv = new ArrayList<>();

        for (int j = 0; j < PMISurveyHome33KV.listLT.size(); j++) {
            double distance = getDistanceBetweenPoles(PMISurveyHome33KV.listLT.get(j).getGLAT(), PMISurveyHome33KV.listLT.get(j).getGLON());
            // System.out.println("DISTANCE 33KV FEEDER IN 11KV MULTICKT OUTER : "+distance);
            if (distance <= 100) {
                filteredList.add(PMISurveyHome33KV.listLT.get(j));
            }
        }

        List<String> combinedList = new ArrayList<>();
        for (int i = 0; i < filteredList.size(); i++) {
            combinedList.add(filteredList.get(i).getMC_FEEDER_NAME());
        }
        Set<String> set_feeder_id_1 = new LinkedHashSet<>();
        set_feeder_id_1.addAll(combinedList);
        combinedList.clear();
        combinedList.addAll(set_feeder_id_1);

        if (!combinedList.isEmpty()) {
            for (int i = 0; i < combinedList.size(); i++) {
                list_feeder_1.add(combinedList.get(i));
                list_feeder_2.add(combinedList.get(i));
                list_feeder_3.add(combinedList.get(i));
            }
        }

        //NEW END

        list_feeder_1.add("LT CKT Not Surveyed");
        ArrayAdapter<String> adapter_feeder_1 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_feeder_1);
        sp_oh_lt_feeder_1.setAdapter(adapter_feeder_1);
        sp_oh_lt_feeder_1.setSelection(0, true);

        //FEEDER 2

        // list_feeder_2.addAll(PMISurveyHome33KV.filterLT);
        list_feeder_2.add("LT CKT Not Surveyed");
        ArrayAdapter<String> adapter_feeder_2 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_feeder_2);
        sp_oh_lt_feeder_2.setAdapter(adapter_feeder_2);
        sp_oh_lt_feeder_2.setSelection(0, true);

        //FEEDER 3

        // list_feeder_3.addAll(PMISurveyHome33KV.filterLT);
        list_feeder_3.add("LT CKT Not Surveyed");
        ArrayAdapter<String> adapter_feeder_3 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_feeder_3);
        sp_oh_lt_feeder_3.setAdapter(adapter_feeder_3);
        sp_oh_lt_feeder_3.setSelection(0, true);

        sp_oh_lt_feeder_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (sp_oh_lt_feeder_1.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                    layout_oh_lt_feeder_1.setVisibility(View.VISIBLE);
                    feeder_1_pole_layout_lt.setVisibility(View.VISIBLE);
                    ed_oh_lt_feeder_1_ns.setVisibility(View.GONE);

                   /* ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMISurveyHome33KV.feederListLT);
                    ed_oh_lt_feeder_1.setAdapter(adapter_feeder);
                    ed_oh_lt_feeder_1.setThreshold(5);*/

                    SearchableSpinner searchableSpinner = new SearchableSpinner(c);
                    searchableSpinner.setWindowTitle("SELECT DTR CODE");
                    searchableSpinner.setSpinnerListItems(PMISurveyHome33KV.feederListLT);
                    searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                        @Override
                        public void setOnItemSelectListener(int position, String selectedString) {
                            ed_oh_lt_feeder_1.setText(selectedString);
                        }
                    });

                    ed_oh_lt_feeder_1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            searchableSpinner.show();
                        }
                    });

                    List<String> list = new ArrayList<>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_lt_feeder_1_pole.setAdapter(adapter);
                } else if (sp_oh_lt_feeder_1.getSelectedItem().toString().equalsIgnoreCase("LT CKT Not Surveyed")) {
                    layout_oh_lt_feeder_1.setVisibility(View.GONE);
                    feeder_1_pole_layout_lt.setVisibility(View.GONE);
                    ed_oh_lt_feeder_1_ns.setVisibility(View.VISIBLE);

                    /*ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMISurveyHome33KV.feederListLT);
                    ed_oh_lt_feeder_1_ns.setAdapter(adapter_feeder);
                    ed_oh_lt_feeder_1_ns.setThreshold(5);*/

                    SearchableSpinner searchableSpinner = new SearchableSpinner(c);
                    searchableSpinner.setWindowTitle("SELECT DTR CODE");
                    searchableSpinner.setSpinnerListItems(PMISurveyHome33KV.feederListLT);
                    searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                        @Override
                        public void setOnItemSelectListener(int position, String selectedString) {
                            ed_oh_lt_feeder_1_ns.setText(selectedString);
                        }
                    });

                    ed_oh_lt_feeder_1_ns.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            searchableSpinner.show();
                        }
                    });

                } else if (sp_oh_lt_feeder_1.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    List<String> list = new ArrayList<>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_lt_feeder_1_pole.setAdapter(adapter);
                    layout_oh_lt_feeder_1.setVisibility(View.GONE);
                    ed_oh_lt_feeder_1_ns.setVisibility(View.GONE);
                } else {
                    layout_oh_lt_feeder_1.setVisibility(View.GONE);
                    feeder_1_pole_layout_lt.setVisibility(View.VISIBLE);
                    ed_oh_lt_feeder_1_ns.setVisibility(View.GONE);
                    String selectedFeeder = sp_oh_lt_feeder_1.getSelectedItem().toString();
                    /*String [] splitFeederId = selectedFeeder.split("~");
                    selectedFeeder = splitFeederId[1];*/
                    //System.out.println("selectedFeeder 1 : "+selectedFeeder);
                    List<String> list = new ArrayList<>();
                    list.add("Select");
                    for (int j = 0; j < PMISurveyHome33KV.listLT.size(); j++) {
                        if (selectedFeeder.equals(PMISurveyHome33KV.listLT.get(j).getMC_FEEDER_NAME())) {
                            double distance = getDistanceBetweenPoles(PMISurveyHome33KV.listLT.get(j).getGLAT(), PMISurveyHome33KV.listLT.get(j).getGLON());
                            //System.out.println("DISTANCE ltKV FEEDER 1 : "+distance);
                            if (distance <= 10) {
                                list.add("" + PMISurveyHome33KV.listLT.get(j).getMC_POLE_NO());
                            }
                        }
                    }
                    list.add("Not in list(Enter Pole number)");
                   /* if (list.size()==1){
                        list.remove("Select");
                        Toast.makeText(c, "No Poles Found", Toast.LENGTH_SHORT).show();
                    }*/
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_lt_feeder_1_pole.setAdapter(adapter);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_oh_lt_feeder_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (sp_oh_lt_feeder_2.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                    layout_oh_lt_feeder_2.setVisibility(View.VISIBLE);
                    feeder_2_pole_layout_lt.setVisibility(View.VISIBLE);
                    ed_oh_lt_feeder_2_ns.setVisibility(View.GONE);

                   /* ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMISurveyHome33KV.feederListLT);
                    ed_oh_lt_feeder_2.setAdapter(adapter_feeder);
                    ed_oh_lt_feeder_2.setThreshold(5);*/

                    SearchableSpinner searchableSpinner = new SearchableSpinner(c);
                    searchableSpinner.setWindowTitle("SELECT DTR CODE");
                    searchableSpinner.setSpinnerListItems(PMISurveyHome33KV.feederListLT);
                    searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                        @Override
                        public void setOnItemSelectListener(int position, String selectedString) {
                            ed_oh_lt_feeder_2.setText(selectedString);
                        }
                    });

                    ed_oh_lt_feeder_2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            searchableSpinner.show();
                        }
                    });

                    List<String> list = new ArrayList<>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_lt_feeder_2_pole.setAdapter(adapter);
                } else if (sp_oh_lt_feeder_2.getSelectedItem().toString().equalsIgnoreCase("LT CKT Not Surveyed")) {
                    layout_oh_lt_feeder_2.setVisibility(View.GONE);
                    feeder_2_pole_layout_lt.setVisibility(View.GONE);
                    ed_oh_lt_feeder_2_ns.setVisibility(View.VISIBLE);

                   /* ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMISurveyHome33KV.feederListLT);
                    ed_oh_lt_feeder_2_ns.setAdapter(adapter_feeder);
                    ed_oh_lt_feeder_2_ns.setThreshold(5);*/

                    SearchableSpinner searchableSpinner = new SearchableSpinner(c);
                    searchableSpinner.setWindowTitle("SELECT DTR CODE");
                    searchableSpinner.setSpinnerListItems(PMISurveyHome33KV.feederListLT);
                    searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                        @Override
                        public void setOnItemSelectListener(int position, String selectedString) {
                            ed_oh_lt_feeder_2_ns.setText(selectedString);
                        }
                    });

                    ed_oh_lt_feeder_2_ns.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            searchableSpinner.show();
                        }
                    });


                } else if (sp_oh_lt_feeder_2.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    layout_oh_lt_feeder_2.setVisibility(View.GONE);
                    ed_oh_lt_feeder_2_ns.setVisibility(View.GONE);
                    List<String> list = new ArrayList<>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_lt_feeder_2_pole.setAdapter(adapter);
                } else {
                    layout_oh_lt_feeder_2.setVisibility(View.GONE);
                    feeder_2_pole_layout_lt.setVisibility(View.VISIBLE);
                    ed_oh_lt_feeder_2_ns.setVisibility(View.GONE);
                    String selectedFeeder = sp_oh_lt_feeder_2.getSelectedItem().toString();
                    /*String [] splitFeederId = selectedFeeder.split("~");
                    selectedFeeder = splitFeederId[1];*/
                    //System.out.println("selectedFeeder 2 : "+selectedFeeder);
                    List<String> list = new ArrayList<>();
                    list.add("Select");
                    for (int j = 0; j < PMISurveyHome33KV.listLT.size(); j++) {
                        if (selectedFeeder.equals(PMISurveyHome33KV.listLT.get(j).getMC_FEEDER_NAME())) {
                            double distance = getDistanceBetweenPoles(PMISurveyHome33KV.listLT.get(j).getGLAT(), PMISurveyHome33KV.listLT.get(j).getGLON());
                            //System.out.println("DISTANCE ltKV FEEDER 2 : "+distance);
                            if (distance <= 10) {
                                list.add("" + PMISurveyHome33KV.listLT.get(j).getMC_POLE_NO());
                            }
                        }
                    }
                    list.add("Not in list(Enter Pole number)");
                    /*if (list.size()==1){
                        list.remove("Select");
                        Toast.makeText(c, "No Poles Found", Toast.LENGTH_SHORT).show();
                    }*/
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_lt_feeder_2_pole.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_oh_lt_feeder_3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (sp_oh_lt_feeder_3.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                    layout_oh_lt_feeder_3.setVisibility(View.VISIBLE);
                    feeder_3_pole_layout_lt.setVisibility(View.VISIBLE);
                    ed_oh_lt_feeder_3_ns.setVisibility(View.GONE);

                   /* ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMISurveyHome33KV.feederListLT);
                    ed_oh_lt_feeder_3.setAdapter(adapter_feeder);
                    ed_oh_lt_feeder_3.setThreshold(5);*/

                    SearchableSpinner searchableSpinner = new SearchableSpinner(c);
                    searchableSpinner.setWindowTitle("SELECT DTR CODE");
                    searchableSpinner.setSpinnerListItems(PMISurveyHome33KV.feederListLT);
                    searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                        @Override
                        public void setOnItemSelectListener(int position, String selectedString) {
                            ed_oh_lt_feeder_3.setText(selectedString);
                        }
                    });

                    ed_oh_lt_feeder_3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            searchableSpinner.show();
                        }
                    });

                    List<String> list = new ArrayList<>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_lt_feeder_3_pole.setAdapter(adapter);
                } else if (sp_oh_lt_feeder_3.getSelectedItem().toString().equalsIgnoreCase("LT CKT Not Surveyed")) {
                    layout_oh_lt_feeder_3.setVisibility(View.GONE);
                    feeder_3_pole_layout_lt.setVisibility(View.GONE);
                    ed_oh_lt_feeder_3_ns.setVisibility(View.VISIBLE);

                    /*ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, PMISurveyHome33KV.feederListLT);
                    ed_oh_lt_feeder_3_ns.setAdapter(adapter_feeder);
                    ed_oh_lt_feeder_3_ns.setThreshold(5);*/

                    SearchableSpinner searchableSpinner = new SearchableSpinner(c);
                    searchableSpinner.setWindowTitle("SELECT DTR CODE");
                    searchableSpinner.setSpinnerListItems(PMISurveyHome33KV.feederListLT);
                    searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                        @Override
                        public void setOnItemSelectListener(int position, String selectedString) {
                            ed_oh_lt_feeder_3_ns.setText(selectedString);
                        }
                    });

                    ed_oh_lt_feeder_3_ns.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            searchableSpinner.show();
                        }
                    });

                } else if (sp_oh_lt_feeder_3.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    layout_oh_lt_feeder_3.setVisibility(View.GONE);
                    ed_oh_lt_feeder_3_ns.setVisibility(View.GONE);
                    List<String> list = new ArrayList<>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_lt_feeder_3_pole.setAdapter(adapter);
                } else {
                    layout_oh_lt_feeder_3.setVisibility(View.GONE);
                    feeder_3_pole_layout_lt.setVisibility(View.VISIBLE);
                    ed_oh_lt_feeder_3_ns.setVisibility(View.GONE);
                    String selectedFeeder = sp_oh_lt_feeder_3.getSelectedItem().toString();
                    /*String [] splitFeederId = selectedFeeder.split("~");
                    selectedFeeder = splitFeederId[1];*/
                    //System.out.println("selectedFeeder 3 : "+selectedFeeder);
                    List<String> list = new ArrayList<>();
                    list.add("Select");
                    for (int j = 0; j < PMISurveyHome33KV.listLT.size(); j++) {
                        if (selectedFeeder.equals(PMISurveyHome33KV.listLT.get(j).getMC_FEEDER_NAME())) {
                            double distance = getDistanceBetweenPoles(PMISurveyHome33KV.listLT.get(j).getGLAT(), PMISurveyHome33KV.listLT.get(j).getGLON());
                            //System.out.println("DISTANCE ltKV FEEDER 3 : "+distance);
                            if (distance <= 10) {
                                list.add("" + PMISurveyHome33KV.listLT.get(j).getMC_POLE_NO());
                            }
                        }
                    }
                    list.add("Not in list(Enter Pole number)");
                    /*if (list.size()==1){
                        list.remove("Select");
                        Toast.makeText(c, "No Poles Found", Toast.LENGTH_SHORT).show();
                    }*/
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                    sp_oh_lt_feeder_3_pole.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        get_oh_lt_feeder_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_oh_lt_feeder_1.getText().toString().isEmpty()) {
                    Toast.makeText(c, "Please Enter Feeder 1 ID", Toast.LENGTH_SHORT).show();
                } else if (ed_oh_lt_feeder_1.getText().toString().equals(ed_oh_lt_feeder_2.getText().toString())) {
                    Toast.makeText(c, "Feeder 1 & 2 IDs should not be same", Toast.LENGTH_SHORT).show();
                } else if (ed_oh_lt_feeder_1.getText().toString().equals(ed_oh_lt_feeder_3.getText().toString())) {
                    Toast.makeText(c, "Feeder 1 & 3 IDs should not be same", Toast.LENGTH_SHORT).show();
                } else {
                    //  getltKVFeeder1Pole();
                }
            }
        });

        get_oh_lt_feeder_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_oh_lt_feeder_2.getText().toString().isEmpty()) {
                    Toast.makeText(c, "Please Enter Feeder 2 ID", Toast.LENGTH_SHORT).show();
                } else if (ed_oh_lt_feeder_2.getText().toString().equals(ed_oh_lt_feeder_1.getText().toString())) {
                    Toast.makeText(c, "Feeder 2 & 1 IDs should not be same", Toast.LENGTH_SHORT).show();
                } else if (ed_oh_lt_feeder_2.getText().toString().equals(ed_oh_lt_feeder_3.getText().toString())) {
                    Toast.makeText(c, "Feeder 2 & 3 IDs should not be same", Toast.LENGTH_SHORT).show();
                } else {
                    //   getltKVFeeder2Pole();
                }
            }
        });

        get_oh_lt_feeder_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_oh_lt_feeder_3.getText().toString().isEmpty()) {
                    Toast.makeText(c, "Please Enter Feeder 3 ID", Toast.LENGTH_SHORT).show();
                } else if (ed_oh_lt_feeder_3.getText().toString().equals(ed_oh_lt_feeder_1.getText().toString())) {
                    Toast.makeText(c, "Feeder 3 & 1 IDs should not be same", Toast.LENGTH_SHORT).show();
                } else if (ed_oh_lt_feeder_3.getText().toString().equals(ed_oh_lt_feeder_2.getText().toString())) {
                    Toast.makeText(c, "Feeder 3 & 2 IDs should not be same", Toast.LENGTH_SHORT).show();
                } else {
                    //  getltKVFeeder3Pole();
                }
            }
        });

        sp_oh_lt_feeder_1_pole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_oh_lt_feeder_1_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                    ed_oh_lt_pole_1.setVisibility(View.VISIBLE);
                } else {
                    ed_oh_lt_pole_1.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_oh_lt_feeder_2_pole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_oh_lt_feeder_2_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                    ed_oh_lt_pole_2.setVisibility(View.VISIBLE);
                } else {
                    ed_oh_lt_pole_2.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_oh_lt_feeder_3_pole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_oh_lt_feeder_3_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                    ed_oh_lt_pole_3.setVisibility(View.VISIBLE);
                } else {
                    ed_oh_lt_pole_3.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void get33KVFeeder1Pole() {
        JsonObject object = new JsonObject();
        object.addProperty("FEEDER_ID", ed_oh_33_feeder_1.getText().toString().split("~")[0]);
        object.addProperty("DISTANCE", 1000000);
        object.addProperty("GLAT", y);
        object.addProperty("GLOG", x);


        progressDialog.show();
        retrofitService = PMIRetrofitClientInstance33kv.getRetrofitInstance().create(RetrofitService33kv.class);
        Call<JsonObject> responseVoCall = retrofitService.getMultiCKTData3(object);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                progressDialog.dismiss();
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    Log.v("POLE==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {

                            // arcGISDatabase.clearMultiCktData();
                            JsonObject obj = surveyedResponseVo.get("RESPONSE").getAsJsonObject();
                            JsonArray el_kv_array = obj.get("33KV").getAsJsonArray();

                            if (el_kv_array.size() > 0) {
                                List<String> list = new ArrayList<>();
                                ArrayList<PMIMultiCktVO> mapList = new ArrayList<>();
                                list.add("Select");
                                for (int i = 0; i < el_kv_array.size(); i++) {
                                    JsonObject data = el_kv_array.get(i).getAsJsonObject();
                                    double distance = getDistanceBetweenPoles(data.get("GLAT").getAsString(), data.get("GLONG").getAsString());
                                    //System.out.println("DISTANCE 33KV FEEDER 1 Static : "+distance);
                                    if (distance <= 10) {
                                        list.add(data.get("POLE_NO").getAsString());

                                        PMIMultiCktVO PMIMultiCktVO = new PMIMultiCktVO();
                                        PMIMultiCktVO.setMC_FEEDER_ID(data.get("FEEDER_ID").getAsString());
                                        PMIMultiCktVO.setGLAT(data.get("GLAT").getAsString());
                                        PMIMultiCktVO.setGLON(data.get("GLONG").getAsString());
                                        PMIMultiCktVO.setMC_POLE_NO(data.get("POLE_NO").getAsInt());
                                        mapList.add(PMIMultiCktVO);

                                    }
                                }
                                list.add("Not in list(Enter Pole number)");

                                if (mapList.size() > 0) {
                                    // loadMultiVoltageMap("get","33",mapList);
                                }

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                                sp_oh_33_feeder_1_pole.setAdapter(adapter);
                            } else {

                                Toast.makeText(c, "No Poles Found", Toast.LENGTH_SHORT).show();
                                List<String> list = new ArrayList<>();
                                list.add("Select");
                                list.add("Not in list(Enter Pole number)");
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                                sp_oh_33_feeder_1_pole.setAdapter(adapter);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(c, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(c, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(c, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void get33KVFeeder2Pole() {
        JsonObject object = new JsonObject();
        object.addProperty("FEEDER_ID", ed_oh_33_feeder_2.getText().toString().split("~")[0]);
        object.addProperty("DISTANCE", 1000000);
        object.addProperty("GLAT", y);
        object.addProperty("GLOG", x);


        progressDialog.show();
        retrofitService = PMIRetrofitClientInstance33kv.getRetrofitInstance().create(RetrofitService33kv.class);
        Call<JsonObject> responseVoCall = retrofitService.getMultiCKTData3(object);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                progressDialog.dismiss();
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    Log.v("POLE==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {

                            JsonObject obj = surveyedResponseVo.get("RESPONSE").getAsJsonObject();
                            JsonArray el_kv_array = obj.get("33KV").getAsJsonArray();
                            if (el_kv_array.size() > 0) {
                                ArrayList<PMIMultiCktVO> mapList = new ArrayList<>();
                                List<String> list = new ArrayList<>();
                                list.add("Select");
                                for (int i = 0; i < el_kv_array.size(); i++) {
                                    JsonObject data = el_kv_array.get(i).getAsJsonObject();
                                    double distance = getDistanceBetweenPoles(data.get("GLAT").getAsString(), data.get("GLONG").getAsString());
                                    //System.out.println("DISTANCE 33KV FEEDER 2 Static : "+distance);
                                    if (distance <= 10) {
                                        list.add(data.get("POLE_NO").getAsString());

                                        PMIMultiCktVO PMIMultiCktVO = new PMIMultiCktVO();
                                        PMIMultiCktVO.setMC_FEEDER_ID(data.get("FEEDER_ID").getAsString());
                                        PMIMultiCktVO.setGLAT(data.get("GLAT").getAsString());
                                        PMIMultiCktVO.setGLON(data.get("GLONG").getAsString());
                                        PMIMultiCktVO.setMC_POLE_NO(data.get("POLE_NO").getAsInt());
                                        mapList.add(PMIMultiCktVO);
                                    }
                                }
                                if (mapList.size() > 0) {
                                    //  loadMultiVoltageMap("get","33",mapList);
                                }
                                list.add("Not in list(Enter Pole number)");

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                                sp_oh_33_feeder_2_pole.setAdapter(adapter);
                            } else {
                                Toast.makeText(c, "No Poles Found", Toast.LENGTH_SHORT).show();
                                List<String> list = new ArrayList<>();
                                list.add("Select");
                                list.add("Not in list(Enter Pole number)");
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                                sp_oh_33_feeder_2_pole.setAdapter(adapter);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(c, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(c, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(c, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void get33KVFeeder3Pole() {
        JsonObject object = new JsonObject();
        object.addProperty("FEEDER_ID", ed_oh_33_feeder_3.getText().toString().split("~")[0]);
        object.addProperty("DISTANCE", 1000000);
        object.addProperty("GLAT", y);
        object.addProperty("GLOG", x);


        progressDialog.show();
        retrofitService = PMIRetrofitClientInstance33kv.getRetrofitInstance().create(RetrofitService33kv.class);
        Call<JsonObject> responseVoCall = retrofitService.getMultiCKTData3(object);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                progressDialog.dismiss();
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    Log.v("POLE==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {

                            JsonObject obj = surveyedResponseVo.get("RESPONSE").getAsJsonObject();
                            JsonArray el_kv_array = obj.get("33KV").getAsJsonArray();
                            if (el_kv_array.size() > 0) {
                                ArrayList<PMIMultiCktVO> mapList = new ArrayList<>();
                                List<String> list = new ArrayList<>();
                                list.add("Select");
                                for (int i = 0; i < el_kv_array.size(); i++) {
                                    JsonObject data = el_kv_array.get(i).getAsJsonObject();
                                    double distance = getDistanceBetweenPoles(data.get("GLAT").getAsString(), data.get("GLONG").getAsString());
                                    //System.out.println("DISTANCE 33KV FEEDER 3 Static : "+distance);
                                    if (distance <= 10) {
                                        list.add(data.get("POLE_NO").getAsString());

                                        PMIMultiCktVO PMIMultiCktVO = new PMIMultiCktVO();
                                        PMIMultiCktVO.setMC_FEEDER_ID(data.get("FEEDER_ID").getAsString());
                                        PMIMultiCktVO.setGLAT(data.get("GLAT").getAsString());
                                        PMIMultiCktVO.setGLON(data.get("GLONG").getAsString());
                                        PMIMultiCktVO.setMC_POLE_NO(data.get("POLE_NO").getAsInt());
                                        mapList.add(PMIMultiCktVO);
                                    }
                                }

                                if (mapList.size() > 0) {
                                    //  loadMultiVoltageMap("get","33",mapList);
                                }
                                list.add("Not in list(Enter Pole number)");

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                                sp_oh_33_feeder_3_pole.setAdapter(adapter);
                            } else {
                                Toast.makeText(c, "No Poles Found", Toast.LENGTH_SHORT).show();
                                List<String> list = new ArrayList<>();
                                list.add("Select");
                                list.add("Not in list(Enter Pole number)");
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                                sp_oh_33_feeder_3_pole.setAdapter(adapter);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(c, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(c, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(c, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void get11KVFeeder1Pole() {
        JsonObject object = new JsonObject();
        object.addProperty("FEEDER_ID", ed_oh_11_feeder_1.getText().toString().split("~")[0]);
        object.addProperty("DISTANCE", 1000000);
        object.addProperty("GLAT", y);
        object.addProperty("GLOG", x);


        progressDialog.show();
        retrofitService = PMIRetrofitClientInstance33kv.getRetrofitInstance().create(RetrofitService33kv.class);
        Call<JsonObject> responseVoCall = retrofitService.getMultiCKTData2(object);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                progressDialog.dismiss();
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    Log.v("POLE==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {

                            // arcGISDatabase.clearMultiCktData();

                            JsonObject obj = surveyedResponseVo.get("RESPONSE").getAsJsonObject();
                            JsonArray el_kv_array = obj.get("11KV").getAsJsonArray();

                            if (el_kv_array.size() > 0) {
                                ArrayList<PMIMultiCktVO> mapList = new ArrayList<>();
                                List<String> list = new ArrayList<>();
                                list.add("Select");
                                for (int i = 0; i < el_kv_array.size(); i++) {
                                    JsonObject data = el_kv_array.get(i).getAsJsonObject();
                                    double distance = getDistanceBetweenPoles(data.get("GLAT").getAsString(), data.get("GLONG").getAsString());
                                    //System.out.println("DISTANCE 11KV FEEDER 1 Static : "+distance);
                                    if (distance <= 10) {
                                        list.add(data.get("POLE_NO").getAsString());

                                        PMIMultiCktVO PMIMultiCktVO = new PMIMultiCktVO();
                                        PMIMultiCktVO.setMC_FEEDER_ID(data.get("FEEDER_ID").getAsString());
                                        PMIMultiCktVO.setGLAT(data.get("GLAT").getAsString());
                                        PMIMultiCktVO.setGLON(data.get("GLOG").getAsString());
                                        PMIMultiCktVO.setMC_POLE_NO(data.get("POLE_NO").getAsInt());
                                        mapList.add(PMIMultiCktVO);
                                    }

                                }
                                if (mapList.size() > 0) {
                                    // loadMultiVoltageMap("get","11",mapList);
                                }
                                list.add("Not in list(Enter Pole number)");
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                                sp_oh_11_feeder_1_pole.setAdapter(adapter);
                            } else {
                                Toast.makeText(c, "No Poles Found", Toast.LENGTH_SHORT).show();
                                List<String> list = new ArrayList<>();
                                list.add("Select");
                                list.add("Not in list(Enter Pole number)");
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                                sp_oh_11_feeder_1_pole.setAdapter(adapter);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(c, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(c, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(c, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void get11KVFeeder2Pole() {
        JsonObject object = new JsonObject();
        object.addProperty("FEEDER_ID", ed_oh_11_feeder_2.getText().toString().split("~")[0]);
        object.addProperty("DISTANCE", 1000000);
        object.addProperty("GLAT", y);
        object.addProperty("GLOG", x);


        progressDialog.show();
        retrofitService = PMIRetrofitClientInstance33kv.getRetrofitInstance().create(RetrofitService33kv.class);
        Call<JsonObject> responseVoCall = retrofitService.getMultiCKTData2(object);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                progressDialog.dismiss();
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    Log.v("POLE==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {

                            JsonObject obj = surveyedResponseVo.get("RESPONSE").getAsJsonObject();
                            JsonArray el_kv_array = obj.get("11KV").getAsJsonArray();

                            if (el_kv_array.size() > 0) {
                                ArrayList<PMIMultiCktVO> mapList = new ArrayList<>();
                                List<String> list = new ArrayList<>();
                                list.add("Select");
                                for (int i = 0; i < el_kv_array.size(); i++) {
                                    JsonObject data = el_kv_array.get(i).getAsJsonObject();
                                    double distance = getDistanceBetweenPoles(data.get("GLAT").getAsString(), data.get("GLONG").getAsString());
                                    //System.out.println("DISTANCE 11KV FEEDER 2 Static : "+distance);
                                    if (distance <= 10) {
                                        list.add(data.get("POLE_NO").getAsString());

                                        PMIMultiCktVO PMIMultiCktVO = new PMIMultiCktVO();
                                        PMIMultiCktVO.setMC_FEEDER_ID(data.get("FEEDER_ID").getAsString());
                                        PMIMultiCktVO.setGLAT(data.get("GLAT").getAsString());
                                        PMIMultiCktVO.setGLON(data.get("GLOG").getAsString());
                                        PMIMultiCktVO.setMC_POLE_NO(data.get("POLE_NO").getAsInt());
                                        mapList.add(PMIMultiCktVO);
                                    }
                                }
                                if (mapList.size() > 0) {
                                    // loadMultiVoltageMap("get","11",mapList);
                                }
                                list.add("Not in list(Enter Pole number)");
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                                sp_oh_11_feeder_2_pole.setAdapter(adapter);
                            } else {
                                Toast.makeText(c, "No Poles Found", Toast.LENGTH_SHORT).show();
                                List<String> list = new ArrayList<>();
                                list.add("Select");
                                list.add("Not in list(Enter Pole number)");
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                                sp_oh_11_feeder_2_pole.setAdapter(adapter);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(c, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(c, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(c, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void get11KVFeeder3Pole() {
        JsonObject object = new JsonObject();
        object.addProperty("FEEDER_ID", ed_oh_11_feeder_3.getText().toString().split("~")[0]);
        object.addProperty("DISTANCE", 1000000);
        object.addProperty("GLAT", y);
        object.addProperty("GLOG", x);


        progressDialog.show();
        retrofitService = PMIRetrofitClientInstance33kv.getRetrofitInstance().create(RetrofitService33kv.class);
        Call<JsonObject> responseVoCall = retrofitService.getMultiCKTData2(object);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                progressDialog.dismiss();
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    Log.v("POLE==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {

                            JsonObject obj = surveyedResponseVo.get("RESPONSE").getAsJsonObject();
                            JsonArray el_kv_array = obj.get("11KV").getAsJsonArray();

                            if (el_kv_array.size() > 0) {
                                ArrayList<PMIMultiCktVO> mapList = new ArrayList<>();
                                List<String> list = new ArrayList<>();
                                list.add("Select");
                                for (int i = 0; i < el_kv_array.size(); i++) {
                                    JsonObject data = el_kv_array.get(i).getAsJsonObject();
                                    double distance = getDistanceBetweenPoles(data.get("GLAT").getAsString(), data.get("GLONG").getAsString());
                                    //System.out.println("DISTANCE 11KV FEEDER 3 Static : "+distance);
                                    if (distance <= 10) {
                                        list.add(data.get("POLE_NO").getAsString());

                                        PMIMultiCktVO PMIMultiCktVO = new PMIMultiCktVO();
                                        PMIMultiCktVO.setMC_FEEDER_ID(data.get("FEEDER_ID").getAsString());
                                        PMIMultiCktVO.setGLAT(data.get("GLAT").getAsString());
                                        PMIMultiCktVO.setGLON(data.get("GLOG").getAsString());
                                        PMIMultiCktVO.setMC_POLE_NO(data.get("POLE_NO").getAsInt());
                                        mapList.add(PMIMultiCktVO);
                                    }
                                }
                                if (mapList.size() > 0) {
                                    // loadMultiVoltageMap("get","11",mapList);
                                }
                                list.add("Not in list(Enter Pole number)");
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                                sp_oh_11_feeder_3_pole.setAdapter(adapter);
                            } else {
                                Toast.makeText(c, "No Poles Found", Toast.LENGTH_SHORT).show();
                                List<String> list = new ArrayList<>();
                                list.add("Select");
                                list.add("Not in list(Enter Pole number)");
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list);
                                sp_oh_11_feeder_3_pole.setAdapter(adapter);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(c, surveyedResponseVo.get("ERR_MSG").toString(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(c, "Server Busy Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(c, "Try Again" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private double getDistanceBetweenPoles(String polLat, String polLon) {
        //  System.out.println("INSIDE CURRENT LOCATION LAT : "+x);
        //  System.out.println("INSIDE CURRENT LOCATION LON : "+y);
        // System.out.println("INSIDE POLE LOCATION LAT : "+polLat);
        // System.out.println("INSIDE POLE LOCATION LON : "+polLon);

        Location currentStartPoint = new Location("locationA");
        currentStartPoint.setLatitude(Double.parseDouble(x));
        currentStartPoint.setLongitude(Double.parseDouble(y));

        Location currentEndPoint = new Location("locationA");
        currentEndPoint.setLatitude(Double.parseDouble(polLat));
        currentEndPoint.setLongitude(Double.parseDouble(polLon));
        double currentDistance = currentStartPoint.distanceTo(currentEndPoint);
        return currentDistance;
    }

    private File createImageFile1() throws IOException {
        // Create a unique file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = c.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        // Create a file for the image
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a path to the file (optional)
        currentPhotoPath_1 = image.getAbsolutePath();
        return image;
    }

    private File createImageFile2() throws IOException {
        // Create a unique file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = c.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        // Create a file for the image
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a path to the file (optional)
        currentPhotoPath_2 = image.getAbsolutePath();
        return image;
    }
}