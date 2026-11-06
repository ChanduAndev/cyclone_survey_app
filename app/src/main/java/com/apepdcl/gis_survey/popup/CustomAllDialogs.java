package com.apepdcl.gis_survey.popup;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
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

import androidx.core.view.WindowCompat;

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.HomeActivity;

import net.sourceforge.app.MainActivity;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.ArcGISMapActivity;
import com.apepdcl.gis_survey.activitys.DTRSelectActivity;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.module_11KV.models.SurveyVo;
import com.apepdcl.gis_survey.retrofitservice.RetrofitClientInstance;
import com.apepdcl.gis_survey.retrofitservice.RetrofitService;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.utilits.GpsTraker;
import com.apepdcl.gis_survey.vo.LTCktsResponseDataVo;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.apepdcl.gis_survey.vo.MultiCktVO;
import com.apepdcl.gis_survey.vo.SurveyDataVo;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LayerSceneProperties;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.leo.searchablespinner.SearchableSpinner;
import com.leo.searchablespinner.interfaces.OnItemSelectListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomAllDialogs extends Dialog implements AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    public Activity c;
    private TextView tv_dtrstrcuture, tv_ltcircuit, tv_distance, tv_change_from_pole;
    private EditText ed_pole_no, ed_frompole;
    private Spinner sp_lineType, sp_oh_isendpole, sp_oh_lineconfig, sp_oh_isstreetlight, sp_oh_otherthan_streetlight, sp_oh_pin_cutpoint, sp_oh_is_doublefeeding_pole, sp_oh_pole_type, sp_oh_pole_height, sp_oh_strut_stay_details, sp_oh_tapping_point, sp_oh_noof_spurlines, sp_oh_conductor_size, sp_oh_ismulti_circuit, sp_oh_noof_circuits, sp_oh_multi_voltage_same_voltage,
            sp_ug_isendpole, sp_ug_locationtype, sp_ug_servicepiller_type, sp_ug_poletype, sp_ug_poleheight, sp_ug_polesupport, sp_ug_feedepiller_outg_circuits, sp_ug_tapping_point, sp_ug_notapping_point, sp_ug_cablesize, sp_ug_nocable, sp_crossing;
    private String lineType, oh_isendpole = "", oh_lineconfig, oh_isstreetlight = "", oh_otherthan_streetlight, oh_pin_cutpoint, oh_is_doublefeeding_pole, oh_pole_type, oh_pole_height, oh_strut_stay_details, oh_tapping_point, oh_noof_spurlines, oh_conductor_size, oh_ismulti_circuit, oh_noof_circuits, oh_multi_voltage_same_voltage,
            ug_isendpole, ug_locationtype, ug_servicepiller_type, ug_poletype, ug_poleheight, ug_polesupport, ug_feedepiller_outg_circuits, ug_tapping_point, ug_notapping_point, ug_cablesize, ug_nocable, crossing;
    private LinearLayout lyt_oh_aglservice, lyt_oh_edt_aglservices, lyt_oh_chacquaservices, lyt_oh_edt_aquaservices, lyt_oh_chindustrialservices, lyt_oh_edt_industrialservices, lyt_oh_is_doublefeeding_pole, lyt_oh_noof_spurlines, lyt_oh_noof_circuits, lyt_oh_multivoltage, lyt_oh_33kv_vlckts, lyt_oh_11kv_vlckts, lyt_oh_6_3kv_vlckts, lyt_oh_lt_vlckts, lyt_ug_servicepiller, lyt_ug_tappingpoint, lyt_underground, lyt_overhead, lyt_otherthan_streetlight, lyt_spi_no_circuit, lyt_ug_poletype, lyt_ug_feederpiller, lyt_distance, lyt_oh_crossing, lyt_oh_6_3_crossing, lyt_oh_11kv_crossing, lyt_oh_33kv_crossing, lyt_oh_lt_crossing, lyt_oh_tc_crossing, lyt_oh_tm_crossing;
    private CheckBox ch_oh_aglservices, ch_oh_aquaservices, ch_oh_industrialservices, ch_oh_33kv_vlckts, ch_oh_11kv_vlckts, ch_oh_6_3kv_vlckts, ch_oh_lt_vlckts, ch_oh_6_3_crossing, ch_oh_11kv_crossing, ch_oh_33kv_crossing, ch_oh_lt_crossing, ch_oh_tc_crossing, ch_oh_tm_crossing;
    private TextInputEditText edt_oh_aglservices, edt_oh_aquaservices, edt_oh_industrialservices, edt_oh_tappinglocation, edt_oh_33kv_vlckts, edt_oh_11kv_vlckts, edt_oh_6_3kv_vlckts, edt_oh_lt_vlckts, edt_oh_pole_paintingcode, edt_oh_remarks, edt_ug_tappinglocation, edt_ug_pole_paintingcode, edt_ug_remarks, edt_oh_tc_crossing, edt_oh_tm_crossing;
    TextView edt_oh_6_3_crossing, edt_oh_11kv_crossing, edt_oh_33kv_crossing, edt_oh_lt_crossing;
    private TextView btn_clear, btn_add, btn_lttopclear;
    private ArcGISDatabase arcGISDatabase;
    private List<SurveyDataVo> surveyVosList;
    private int maxSurveyCount, poleCountCheck;
    private String dtrCode, ltCircuit;
    private final String x, y;
    Callout callout;
    GraphicsOverlay graphicsOverlay;
    Graphic graphic;
    private int noof_33kvCkts, noof_11kvCkts, noof_6_3kvCkts, noof_ltCkts;
    private LinearLayout lay_oh_dtr_pole_dtr_meter_avilable, lay_oh_dtr_pole_no_of_meter, lay_ug_dtr_pole_dtr_meter_avilable, lay_ug_dtr_pole_no_of_meter;
    private Spinner sp_oh_dtr_pole_dtr_meter_avilable, sp_oh_dtr_pole_no_of_meters, sp_ug_dtr_pole_dtr_meter_avilable, sp_ug_dtr_pole_no_of_meters;
    private String oh_dtr_pole_dtr_meter_avilable, oh_dtr_pole_no_of_meters, ug_dtr_pole_dtr_meter_avilable, ug_dtr_pole_no_of_meters;
    private double distance;
    private List<SurveyDataVo> surveyVoList;
    Button btn_repeat_pole;
    private String sp_poleheight_KEY = "N";

    //NEW
    Spinner sp_oh_defects;
    String defect_selected="No";
    LinearLayout defects_layout;
    Spinner sp_int_pole, sp_restringing, sp_ht_clearance, sp_stray_wire, sp_strut_pole, sp_cross_arm, sp_insulators, sp_conductors, sp_pole_defects;
    CheckBox ch_int_pole, ch_restringing, ch_ht_clearance, ch_stray_wire, ch_strut_pole, ch_cross_arm, ch_insulators, ch_conductors, ch_pole_defects, ch_other_defects;
    EditText ed_other_defects;

    //NEW
    TextInputEditText edt_oh_con_services;
    LinearLayout layout_oh_con_services, tv_from_pole_layout;

    //DISTANCE
    double currentDistance, lastPoleDistance;
    TextView last_pole_distance, current_distance;

    //IMAGES
    LinearLayout images_layout;
    public static ImageView image_1, image_2, image_3;
    public static TextView upload_image_1, upload_image_2, upload_image_3;
    public static final int CAMERA_RESULT_CODE_IMAGE_1 = 21;
    public static final int CAMERA_RESULT_CODE_IMAGE_2 = 22;
    public static final int CAMERA_RESULT_CODE_IMAGE_3 = 23;

    public static final int GALLERY_RESULT_CODE_IMAGE_1 = 24;
    public static final int GALLERY_RESULT_CODE_IMAGE_2 = 25;
    public static final int GALLERY_RESULT_CODE_IMAGE_3 = 26;

    public static String image_1_base64, image_2_base64, image_3_base64;
    public static boolean image_1_clicked = false;
    public static boolean image_2_clicked = false;
    public static boolean image_3_clicked = false;

    public static String currentPhotoPath_1, currentPhotoPath_2;

    LinearLayout end_pole_layout;

    //REPEAT POLE NEW
    String poleHeight;
    boolean repeatPoleClicked = false;

    //MULTI CIRCUIT
    private RetrofitService retrofitService;
    private ProgressDialog progressDialog;
    //11 KV
    Spinner sp_oh_no_of_11_feeders, sp_from_pole;
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
    Spinner sp_oh_no_of_lt_feeders;
    Spinner sp_oh_lt_feeder_1, sp_oh_lt_feeder_1_pole, sp_oh_lt_feeder_2, sp_oh_lt_feeder_2_pole, sp_oh_lt_feeder_3, sp_oh_lt_feeder_3_pole;
    TextView ed_oh_lt_feeder_1, ed_oh_lt_feeder_2, ed_oh_lt_feeder_3;
    TextView ed_oh_lt_feeder_1_ns, ed_oh_lt_feeder_2_ns, ed_oh_lt_feeder_3_ns;
    LinearLayout main_layout_feeder_1_lt, layout_oh_lt_feeder_1, main_layout_feeder_2_lt;
    LinearLayout layout_oh_lt_feeder_2, main_layout_feeder_3_lt, layout_oh_lt_feeder_3;
    TextView get_oh_lt_feeder_1, get_oh_lt_feeder_2, get_oh_lt_feeder_3;
    EditText ed_oh_lt_pole_1, ed_oh_lt_pole_2, ed_oh_lt_pole_3;
    LinearLayout feeder_1_pole_layout_lt, feeder_2_pole_layout_lt, feeder_3_pole_layout_lt;


    MapView mv_33kv;
    ArcGISMap map_33kv;
    ArrayList<Graphic> graph_objects_33kv = new ArrayList<Graphic>();
    GraphicsOverlay graphicsOverlay_33kv;
    private GraphicsOverlay mGraphicsOverlay_33kv;
    Graphic graphic_33kv;
    private Point ptWgs84_33kv;
    PointCollection polylinePoints_33kv;
    GpsTraker gpsTraker_33kv;

    //OTHER CABLES
    Spinner sp_oh_other_cables;
    EditText edt_oh_other_cables;

    //JHC PHASE 1
    Spinner sp_pole_survey_type, sp_line_survey_type;
    LinearLayout jhc_layout;

    String not;


    //POLE CONCRETE
    Spinner sp_pole_concrete;
    String pole_concrete;

    //CURRENT LOCATION
    private double CURRENT_LAT;
    private double CURRENT_LONG;

    //STREET LIGHT
    LinearLayout street_light_layout, meter_source_layout;
    Spinner sp_oh_is_streetlight_status, sp_oh_streetlight_meter_source, sp_oh_streetlight_meter_available;

    //NEW IMAGE OPEN CAMERA
    public static Bitmap capturedBitmap1 = null;
    public static boolean isImageCaptured1 = false;
    public static boolean photoClicked1 = false;

    public static Bitmap capturedBitmap2 = null;
    public static boolean isImageCaptured2 = false;
    public static boolean photoClicked2 = false;


    public CustomAllDialogs(Activity a, String x, String y, Callout callout, GraphicsOverlay graphicsOverlay,
                            Graphic graphic, String feederName, String feederCode, int maxSurveyCount,
                            String tappingPoint, String surveyType, GraphicsOverlay currentgraphicsOverlay,
                            Graphic currentGraphic, String ssName, String dtrCode, String ltCircuit, double distance,
                            double lastPoleDistance, double currentDistance,
                            double CURRENT_LAT, double CURRENT_LONG
    ) {
        super(a, R.style.FullScreenDialog);
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
        this.lastPoleDistance = lastPoleDistance;
        this.currentDistance = currentDistance;
        this.CURRENT_LAT = CURRENT_LAT;
        this.CURRENT_LONG = CURRENT_LONG;

        isImageCaptured1 = false;
        isImageCaptured2 = false;
        photoClicked1 = false;
        photoClicked2 = false;

    }
    private boolean isDialogShowing = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custome_dialog);

        if (getWindow() != null) {
            getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
        }
        if (getWindow() != null) {
            WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        }
        try {
            setCancelable(false);
            progressDialog = new ProgressDialog(c);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            end_pole_layout = findViewById(R.id.end_pole_layout);
            end_pole_layout.setVisibility(View.VISIBLE);

            //STREET LIGHT
            street_light_layout = findViewById(R.id.street_light_layout);
            sp_oh_is_streetlight_status = findViewById(R.id.sp_oh_is_streetlight_status);
            sp_oh_streetlight_meter_source = findViewById(R.id.sp_oh_streetlight_meter_source);
            sp_oh_streetlight_meter_available = findViewById(R.id.sp_oh_streetlight_meter_available);
            meter_source_layout = findViewById(R.id.meter_source_layout);

            //POLE CONCRETE
            sp_pole_concrete = findViewById(R.id.sp_pole_concrete);

            //JHC PHASE 1
            jhc_layout = findViewById(R.id.jhc_layout);
            sp_pole_survey_type = findViewById(R.id.sp_pole_survey_type);
            sp_line_survey_type = findViewById(R.id.sp_line_survey_type);
            if (LoginActivity.isJHC) {
                jhc_layout.setVisibility(View.VISIBLE);
            }

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


            //MULTI CKT MAP
            mv_33kv = findViewById(R.id.map_view_33kv);
            gpsTraker_33kv = new GpsTraker(c);
            gpsTraker_33kv.getLocation();
            mGraphicsOverlay_33kv = new GraphicsOverlay();
            mv_33kv.getGraphicsOverlays().add(mGraphicsOverlay_33kv);
            //map_33kv = new ArcGISMap(Basemap.Type.IMAGERY_WITH_LABELS, gpsTraker.getLatitude(), gpsTraker.getLongitude(), 20);
            map_33kv = new ArcGISMap(Basemap.Type.IMAGERY_WITH_LABELS, Double.parseDouble(x), Double.parseDouble(y), 50);
            mv_33kv.setMap(map_33kv);
            LocationDisplay mLocationDisplay = mv_33kv.getLocationDisplay();
            mLocationDisplay.addDataSourceStatusChangedListener(dataSourceStatusChangedEvent -> {
                if (dataSourceStatusChangedEvent.isStarted() || dataSourceStatusChangedEvent.getError() == null) {
                    return;
                }


                String message = String.format("Error in DataSourceStatusChangedListener: %s",
                        dataSourceStatusChangedEvent.getSource().getLocationDataSource().getError().getMessage());
                Toast.makeText(c, message, Toast.LENGTH_LONG).show();

            });
            mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
            mLocationDisplay.startAsync();

            //MULTI CKT 11KV
            sp_oh_no_of_11_feeders = findViewById(R.id.sp_oh_no_of_11_feeders);
            sp_from_pole = findViewById(R.id.sp_from_pole);
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

            current_distance = findViewById(R.id.current_distance);
            last_pole_distance = findViewById(R.id.last_pole_distance);
            tv_dtrstrcuture = findViewById(R.id.tv_dtrstrcuture);
            edt_oh_con_services = findViewById(R.id.edt_oh_con_services);
            layout_oh_con_services = findViewById(R.id.layout_oh_con_services);
            tv_ltcircuit = findViewById(R.id.tv_ltcircuit);
            ed_pole_no = findViewById(R.id.ed_pole_no);
            ed_pole_no.setEnabled(false);
            ed_frompole = findViewById(R.id.ed_frompole);
            tv_change_from_pole = findViewById(R.id.tv_change_from_pole);
            tv_from_pole_layout = findViewById(R.id.tv_from_pole_layout);
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

            btn_repeat_pole = findViewById(R.id.btn_repeat_pole);

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
            btn_repeat_pole.setOnClickListener(this);

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
            sp_oh_other_cables.setOnItemSelectedListener(this);
            sp_pole_concrete.setOnItemSelectedListener(this);
            sp_oh_streetlight_meter_available.setOnItemSelectedListener(this);
            sp_from_pole.setOnItemSelectedListener(this);


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

            //  surveyVosList = arcGISDatabase.getSurveyData("");
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


            tv_change_from_pole.setOnClickListener(v -> {
                tv_from_pole_layout.setVisibility(View.GONE);
                sp_from_pole.setVisibility(View.VISIBLE);

                double poleLat = Double.parseDouble(x); // new pole LAT
                double poleLan = Double.parseDouble(y); // new pole LON

                List<SurveyDataVo> list = arcGISDatabase.getPoleLatLanData();
                List<String> nearbyPoleNumbers = new ArrayList<>();
                List<String> polesWithin500 = new ArrayList<>();
                nearbyPoleNumbers.add("Select");
                polesWithin500.add("Select");

                for (SurveyDataVo vo : list) {
                    String polLatFrom = vo.getLAT();
                    String polLonFrom = vo.getLAN();


                    if (polLatFrom != null && polLonFrom != null
                            && !polLatFrom.isEmpty() && !polLonFrom.isEmpty()) {
                        double distance = getDistanceBetweenPoles(polLatFrom, polLonFrom, poleLat, poleLan);
                        if (distance <= 120) {
                            nearbyPoleNumbers.add(String.valueOf(vo.getPOLE_NO()));
                        }
                        if (distance <= 500) {
                            polesWithin500.add(String.valueOf(vo.getPOLE_NO()));
                        }


//                        if (nearbyPoleNumbers.isEmpty()) {
//                            Toast.makeText(c, "No poles found within 500 meters", Toast.LENGTH_SHORT).show();
//                        }
//
//                        sp_from_pole.setTag(list);


                    }
                }
                ArrayAdapter<String> adapter=null;
                if (!nearbyPoleNumbers.isEmpty()){
                    adapter = new ArrayAdapter<>(
                            c,
                            android.R.layout.simple_spinner_item,
                            nearbyPoleNumbers
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_from_pole.setAdapter(adapter);
                }else {
                    adapter = new ArrayAdapter<>(
                            c,
                            android.R.layout.simple_spinner_item,
                            polesWithin500
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_from_pole.setAdapter(adapter);
                }

                // If no nearby poles → show dialog
                if (nearbyPoleNumbers.size() == 1
//                    && !isDialogShowing
                ) {
                    //isDialogShowing = true;

                    polesWithin500.add("Select");

                                List<String> allPoleNumbers = new ArrayList<>();
                                allPoleNumbers.add("Select");

                                for (SurveyDataVo vo : list) {
                                    allPoleNumbers.add(String.valueOf(vo.getPOLE_NO()));
                                }

                    ArrayAdapter<String> allAdapter = new ArrayAdapter<>(
                            c,
                            android.R.layout.simple_spinner_item,
                            polesWithin500
                    );
                    allAdapter.setDropDownViewResource(
                            android.R.layout.simple_spinner_dropdown_item);
                    sp_from_pole.setAdapter(allAdapter);

//                android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(c)
//                        .setTitle("No nearby locations")
//                        .setMessage("here are no nearby locations within 500 meters from the location No :"+ed_pole_no.getText().toString())
//
//                        // OK → go back
//                        .setPositiveButton("Okay", new OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                isDialogShowing = false;
//                                tv_from_pole_layout.setVisibility(View.VISIBLE);
//                                sp_from_pole.setVisibility(View.GONE);
//                            }
//                        })
//
//                        // Load all → remove restriction
//                        .setNegativeButton("Load all locations nearby", new OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                isDialogShowing = false;
//
//                                List<String> allPoleNumbers = new ArrayList<>();
//                                allPoleNumbers.add("Select");
//
//                                for (SurveyVo vo : list) {
//                                    allPoleNumbers.add(String.valueOf(vo.getPole_no()));
//                                }
//
//                                ArrayAdapter<String> allAdapter = new ArrayAdapter<>(
//                                        c,
//                                        android.R.layout.simple_spinner_item,
//                                        allPoleNumbers
//                                );
//                                allAdapter.setDropDownViewResource(
//                                        android.R.layout.simple_spinner_dropdown_item);
//                                sp_from_pole.setAdapter(allAdapter);
//                            }
//                        })
//
//                        .setOnCancelListener(d -> isDialogShowing = false)
//                        .show();
//                dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
//                dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
                }

                sp_from_pole.setTag(list);
            });


            if (count > 0) {
                String idtance_Poles = String.valueOf(Math.round(distance));
                tv_distance.setText(idtance_Poles + "  Mtrs");

                lyt_distance.setVisibility(View.GONE);
            } else {
                lyt_distance.setVisibility(View.GONE);
            }


            surveyVoList = arcGISDatabase.getSurveyDataREPEAT("SELECT * FROM SURVEY_TABLE_REPEAT LIMIT 1 OFFSET (SELECT COUNT(*) FROM SURVEY_TABLE_REPEAT)-1");
            if (surveyVoList.size() > 0) {
                btn_repeat_pole.setVisibility(View.VISIBLE);
            } else {
                btn_repeat_pole.setVisibility(View.GONE);
            }


            //DISTANCE
            int a = (int) Math.round(lastPoleDistance);
            last_pole_distance.setText("" + a);

            int b = (int) Math.round(currentDistance);
            current_distance.setText("" + b);


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

            ch_int_pole = findViewById(R.id.ch_int_pole);
            ch_restringing = findViewById(R.id.ch_restringing);
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
                    //   System.out.println("SELECTED DEFECTS : "+defect_selected);
                    if (defect_selected.equals("Yes")) {
                        defects_layout.setVisibility(View.VISIBLE);
                        images_layout.setVisibility(View.VISIBLE);
                    } else {
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
                    if (b) {
                        sp_int_pole.setVisibility(View.VISIBLE);
                    } else {
                        sp_int_pole.setVisibility(View.GONE);
                    }
                }
            });

            ch_restringing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        sp_restringing.setVisibility(View.VISIBLE);
                    } else {
                        sp_restringing.setVisibility(View.GONE);
                    }
                }
            });

            ch_ht_clearance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        sp_ht_clearance.setVisibility(View.VISIBLE);
                    } else {
                        sp_ht_clearance.setVisibility(View.GONE);
                    }
                }
            });

            ch_stray_wire.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        sp_stray_wire.setVisibility(View.VISIBLE);
                    } else {
                        sp_stray_wire.setVisibility(View.GONE);
                    }
                }
            });

            ch_strut_pole.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        sp_strut_pole.setVisibility(View.VISIBLE);
                    } else {
                        sp_strut_pole.setVisibility(View.GONE);
                    }
                }
            });

            ch_cross_arm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        sp_cross_arm.setVisibility(View.VISIBLE);
                    } else {
                        sp_cross_arm.setVisibility(View.GONE);
                    }
                }
            });

            ch_insulators.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        sp_insulators.setVisibility(View.VISIBLE);
                    } else {
                        sp_insulators.setVisibility(View.GONE);
                    }
                }
            });

            ch_conductors.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        sp_conductors.setVisibility(View.VISIBLE);
                    } else {
                        sp_conductors.setVisibility(View.GONE);
                    }
                }
            });

            ch_pole_defects.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        sp_pole_defects.setVisibility(View.VISIBLE);
                    } else {
                        sp_pole_defects.setVisibility(View.GONE);
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
            upload_image_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // openImageDialog(CAMERA_RESULT_CODE_IMAGE_1,GALLERY_RESULT_CODE_IMAGE_1);

                    /*Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    c.startActivityForResult(intent, CAMERA_RESULT_CODE_IMAGE_1);*/

                    Intent intent = new Intent(c, MainActivity.class);
                    c.startActivity(intent);
                    photoClicked1 = true;
                    isImageCaptured1 = false;
                    capturedBitmap1 = null;


                    /*try {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File photoFile = createImageFile1();
                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(c, "com.apepdcl.gis_survey.fileprovider", photoFile);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        }
                        c.startActivityForResult(intent, CAMERA_RESULT_CODE_IMAGE_1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/


                }
            });

            upload_image_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // openImageDialog(CAMERA_RESULT_CODE_IMAGE_2,GALLERY_RESULT_CODE_IMAGE_2);
                    /*Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    c.startActivityForResult(intent, CAMERA_RESULT_CODE_IMAGE_2);*/

                    Intent intent = new Intent(c, MainActivity.class);
                    c.startActivity(intent);
                    photoClicked2 = true;
                    isImageCaptured2 = false;
                    capturedBitmap2 = null;

                    /*try {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File photoFile = createImageFile2();
                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(c, "com.apepdcl.gis_survey.fileprovider", photoFile);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        }
                        c.startActivityForResult(intent, CAMERA_RESULT_CODE_IMAGE_2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
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

            multiCircuitListeners11KV();
            multiCircuitListeners33KV();
            multiCircuitListenersLT();


        } catch (Exception e) {

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
        List<String> list_feeder_1 = new ArrayList<>();
        list_feeder_1.add("Select");
        List<String> list_feeder_2 = new ArrayList<>();
        list_feeder_2.add("Select");
        List<String> list_feeder_3 = new ArrayList<>();
        list_feeder_3.add("Select");

        //list_feeder_1.addAll(DTRSelectActivity.filter11kv);

        //NEW START

        ArrayList<MultiCktVO> filteredList = new ArrayList<>();
        //  ArrayList<String> filter33kv = new ArrayList<>();

        for (int j = 0; j < DTRSelectActivity.list11kv.size(); j++) {
            double distance = getDistanceBetweenPoles(DTRSelectActivity.list11kv.get(j).getGLAT(), DTRSelectActivity.list11kv.get(j).getGLON());
            // System.out.println("DISTANCE 33KV FEEDER IN 11KV MULTICKT OUTER : "+distance);
            if (distance <= 100) {
                filteredList.add(DTRSelectActivity.list11kv.get(j));
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

        // list_feeder_2.addAll(DTRSelectActivity.filter11kv);
        list_feeder_2.add("Other than List");
        list_feeder_2.add("Feeder Not Surveyed");
        ArrayAdapter<String> adapter_feeder_2 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_feeder_2);
        sp_oh_11_feeder_2.setAdapter(adapter_feeder_2);
        sp_oh_11_feeder_2.setSelection(0, true);

        //FEEDER 3

        //list_feeder_3.addAll(DTRSelectActivity.filter11kv);
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

                    /*ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, DTRSelectActivity.feederList11kv);
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

                   /* ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, DTRSelectActivity.feederList11kv);
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
                    //  System.out.println("SELECTED FEEDER 11KV 1 :"+selectedFeeder);
                    List<String> list = new ArrayList<>();
                    list.add("Select");
                    for (int j = 0; j < DTRSelectActivity.list11kv.size(); j++) {
                        if (selectedFeeder.equals(DTRSelectActivity.list11kv.get(j).getMC_FEEDER_ID())) {
                            double distance = getDistanceBetweenPoles(DTRSelectActivity.list11kv.get(j).getGLAT(), DTRSelectActivity.list11kv.get(j).getGLON());
                            //  System.out.println("DISTANCE 11KV FEEDER 1 : "+distance);
                            //  System.out.println("DISTANCE 11KV FEEDER 1 Pole : "+DTRSelectActivity.list11kv.get(j).getMC_POLE_NO());
                            if (distance <= 10) {
                                list.add("" + DTRSelectActivity.list11kv.get(j).getMC_POLE_NO());
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

                    /*ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, DTRSelectActivity.feederList11kv);
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

                    /*ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, DTRSelectActivity.feederList11kv);
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
                    for (int j = 0; j < DTRSelectActivity.list11kv.size(); j++) {
                        if (selectedFeeder.equals(DTRSelectActivity.list11kv.get(j).getMC_FEEDER_ID())) {
                            double distance = getDistanceBetweenPoles(DTRSelectActivity.list11kv.get(j).getGLAT(), DTRSelectActivity.list11kv.get(j).getGLON());
                            //System.out.println("DISTANCE 11KV FEEDER 2 : "+distance);
                            if (distance <= 10) {
                                list.add("" + DTRSelectActivity.list11kv.get(j).getMC_POLE_NO());
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

                    /*ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, DTRSelectActivity.feederList11kv);
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

                    /*ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, DTRSelectActivity.feederList11kv);
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
                    for (int j = 0; j < DTRSelectActivity.list11kv.size(); j++) {
                        if (selectedFeeder.equals(DTRSelectActivity.list11kv.get(j).getMC_FEEDER_ID())) {
                            double distance = getDistanceBetweenPoles(DTRSelectActivity.list11kv.get(j).getGLAT(), DTRSelectActivity.list11kv.get(j).getGLON());
                            //System.out.println("DISTANCE 11KV FEEDER 3 : "+distance);
                            if (distance <= 10) {
                                list.add("" + DTRSelectActivity.list11kv.get(j).getMC_POLE_NO());
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
        // list_feeder_1.addAll(DTRSelectActivity.filter33kv);

        //NEW START
        ArrayList<MultiCktVO> filteredList = new ArrayList<>();
        //  ArrayList<String> filter33kv = new ArrayList<>();

        for (int j = 0; j < DTRSelectActivity.list33kv.size(); j++) {
            double distance = getDistanceBetweenPoles(DTRSelectActivity.list33kv.get(j).getGLAT(), DTRSelectActivity.list33kv.get(j).getGLON());
            // System.out.println("DISTANCE 33KV FEEDER IN 11KV MULTICKT OUTER : "+distance);
            if (distance <= 100) {
                filteredList.add(DTRSelectActivity.list33kv.get(j));
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
        sp_oh_33_feeder_1.setAdapter(adapter_feeder_1);
        sp_oh_33_feeder_1.setSelection(0, true);

        //FEEDER 2

        // list_feeder_2.addAll(DTRSelectActivity.filter33kv);
        list_feeder_2.add("Other than List");
        list_feeder_2.add("Feeder Not Surveyed");
        ArrayAdapter<String> adapter_feeder_2 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_feeder_2);
        sp_oh_33_feeder_2.setAdapter(adapter_feeder_2);
        sp_oh_33_feeder_2.setSelection(0, true);

        //FEEDER 3

        // list_feeder_3.addAll(DTRSelectActivity.filter33kv);
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

                    /*ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, DTRSelectActivity.feederList33kv);
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

                   /* ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, DTRSelectActivity.feederList33kv);
                    ed_oh_33_feeder_1_ns.setAdapter(adapter_feeder);
                    ed_oh_33_feeder_1_ns.setThreshold(5);*/


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
                    for (int j = 0; j < DTRSelectActivity.list33kv.size(); j++) {
                        if (selectedFeeder.equals(DTRSelectActivity.list33kv.get(j).getMC_FEEDER_ID())) {
                            double distance = getDistanceBetweenPoles(DTRSelectActivity.list33kv.get(j).getGLAT(), DTRSelectActivity.list33kv.get(j).getGLON());
                            //System.out.println("DISTANCE 33KV FEEDER 1 : "+distance);
                            if (distance <= 10) {
                                list.add("" + DTRSelectActivity.list33kv.get(j).getMC_POLE_NO());
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

                  /*  ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, DTRSelectActivity.feederList33kv);
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

                    /*ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, DTRSelectActivity.feederList33kv);
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
                    for (int j = 0; j < DTRSelectActivity.list33kv.size(); j++) {
                        if (selectedFeeder.equals(DTRSelectActivity.list33kv.get(j).getMC_FEEDER_ID())) {
                            double distance = getDistanceBetweenPoles(DTRSelectActivity.list33kv.get(j).getGLAT(), DTRSelectActivity.list33kv.get(j).getGLON());
                            //System.out.println("DISTANCE 33KV FEEDER 2 : "+distance);
                            if (distance <= 10) {
                                list.add("" + DTRSelectActivity.list33kv.get(j).getMC_POLE_NO());
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

                   /* ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, DTRSelectActivity.feederList33kv);
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

                    /*ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, DTRSelectActivity.feederList33kv);
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
                    for (int j = 0; j < DTRSelectActivity.list33kv.size(); j++) {
                        if (selectedFeeder.equals(DTRSelectActivity.list33kv.get(j).getMC_FEEDER_ID())) {
                            double distance = getDistanceBetweenPoles(DTRSelectActivity.list33kv.get(j).getGLAT(), DTRSelectActivity.list33kv.get(j).getGLON());
                            //System.out.println("DISTANCE 33KV FEEDER 3 : "+distance);
                            if (distance <= 10) {
                                list.add("" + DTRSelectActivity.list33kv.get(j).getMC_POLE_NO());
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
        //  list_feeder_1.addAll(DTRSelectActivity.filterLT);


        //NEW START
        ArrayList<MultiCktVO> filteredList = new ArrayList<>();
        //ArrayList<String> filter33kv = new ArrayList<>();

        for (int j = 0; j < DTRSelectActivity.listLT.size(); j++) {
            double distance = getDistanceBetweenPoles(DTRSelectActivity.listLT.get(j).getGLAT(), DTRSelectActivity.listLT.get(j).getGLON());
            // System.out.println("DISTANCE 33KV FEEDER IN 11KV MULTICKT OUTER : "+distance);
            if (distance <= 100) {
                filteredList.add(DTRSelectActivity.listLT.get(j));
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

        // list_feeder_2.addAll(DTRSelectActivity.filterLT);
        list_feeder_2.add("LT CKT Not Surveyed");
        ArrayAdapter<String> adapter_feeder_2 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_feeder_2);
        sp_oh_lt_feeder_2.setAdapter(adapter_feeder_2);
        sp_oh_lt_feeder_2.setSelection(0, true);

        //FEEDER 3

        //  list_feeder_3.addAll(DTRSelectActivity.filterLT);
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

                   /* ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, DTRSelectActivity.feederListLT);
                    ed_oh_lt_feeder_1.setAdapter(adapter_feeder);
                    ed_oh_lt_feeder_1.setThreshold(5);*/

                    SearchableSpinner searchableSpinner = new SearchableSpinner(c);
                    searchableSpinner.setWindowTitle("SELECT DTR CODE");
                    searchableSpinner.setSpinnerListItems(DTRSelectActivity.feederListLT);
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

                  /*  ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, DTRSelectActivity.feederListLT);
                    ed_oh_lt_feeder_1_ns.setAdapter(adapter_feeder);
                    ed_oh_lt_feeder_1_ns.setThreshold(5);*/

                    SearchableSpinner searchableSpinner = new SearchableSpinner(c);
                    searchableSpinner.setWindowTitle("SELECT DTR CODE");
                    searchableSpinner.setSpinnerListItems(DTRSelectActivity.feederListLT);
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
                    for (int j = 0; j < DTRSelectActivity.listLT.size(); j++) {
                        if (selectedFeeder.equals(DTRSelectActivity.listLT.get(j).getMC_FEEDER_NAME())) {
                            double distance = getDistanceBetweenPoles(DTRSelectActivity.listLT.get(j).getGLAT(), DTRSelectActivity.listLT.get(j).getGLON());
                            //System.out.println("DISTANCE ltKV FEEDER 1 : "+distance);
                            if (distance <= 10) {
                                list.add("" + DTRSelectActivity.listLT.get(j).getMC_POLE_NO());
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

                   /* ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, DTRSelectActivity.feederListLT);
                    ed_oh_lt_feeder_2.setAdapter(adapter_feeder);
                    ed_oh_lt_feeder_2.setThreshold(5);*/


                    SearchableSpinner searchableSpinner = new SearchableSpinner(c);
                    searchableSpinner.setWindowTitle("SELECT DTR CODE");
                    searchableSpinner.setSpinnerListItems(DTRSelectActivity.feederListLT);
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

                    /*ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, DTRSelectActivity.feederListLT);
                    ed_oh_lt_feeder_2_ns.setAdapter(adapter_feeder);
                    ed_oh_lt_feeder_2_ns.setThreshold(5);*/

                    SearchableSpinner searchableSpinner = new SearchableSpinner(c);
                    searchableSpinner.setWindowTitle("SELECT DTR CODE");
                    searchableSpinner.setSpinnerListItems(DTRSelectActivity.feederListLT);
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
                    for (int j = 0; j < DTRSelectActivity.listLT.size(); j++) {
                        if (selectedFeeder.equals(DTRSelectActivity.listLT.get(j).getMC_FEEDER_NAME())) {
                            double distance = getDistanceBetweenPoles(DTRSelectActivity.listLT.get(j).getGLAT(), DTRSelectActivity.listLT.get(j).getGLON());
                            //System.out.println("DISTANCE ltKV FEEDER 2 : "+distance);
                            if (distance <= 10) {
                                list.add("" + DTRSelectActivity.listLT.get(j).getMC_POLE_NO());
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

                    /*ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, DTRSelectActivity.feederListLT);
                    ed_oh_lt_feeder_3.setAdapter(adapter_feeder);
                    ed_oh_lt_feeder_3.setThreshold(5);*/

                    SearchableSpinner searchableSpinner = new SearchableSpinner(c);
                    searchableSpinner.setWindowTitle("SELECT DTR CODE");
                    searchableSpinner.setSpinnerListItems(DTRSelectActivity.feederListLT);
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

                    /*ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, DTRSelectActivity.feederListLT);
                    ed_oh_lt_feeder_3_ns.setAdapter(adapter_feeder);
                    ed_oh_lt_feeder_3_ns.setThreshold(5);*/

                    SearchableSpinner searchableSpinner = new SearchableSpinner(c);
                    searchableSpinner.setWindowTitle("SELECT DTR CODE");
                    searchableSpinner.setSpinnerListItems(DTRSelectActivity.feederListLT);
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
                    for (int j = 0; j < DTRSelectActivity.listLT.size(); j++) {
                        if (selectedFeeder.equals(DTRSelectActivity.listLT.get(j).getMC_FEEDER_NAME())) {
                            double distance = getDistanceBetweenPoles(DTRSelectActivity.listLT.get(j).getGLAT(), DTRSelectActivity.listLT.get(j).getGLON());
                            //System.out.println("DISTANCE ltKV FEEDER 3 : "+distance);
                            if (distance <= 10) {
                                list.add("" + DTRSelectActivity.listLT.get(j).getMC_POLE_NO());
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
        retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        Call<JsonObject> responseVoCall = retrofitService.getMultiCKTData3(object);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                progressDialog.dismiss();
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    //  Log.v("POLE==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {

                            // arcGISDatabase.clearMultiCktData();

                            JsonArray el_kv_array = surveyedResponseVo.get("RESPONSE").getAsJsonArray();

                            if (el_kv_array.size() > 0) {
                                List<String> list = new ArrayList<>();
                                ArrayList<MultiCktVO> mapList = new ArrayList<>();
                                list.add("Select");
                                for (int i = 0; i < el_kv_array.size(); i++) {
                                    JsonObject data = el_kv_array.get(i).getAsJsonObject();
                                    double distance = getDistanceBetweenPoles(data.get("GLAT").getAsString(), data.get("GLONG").getAsString());
                                    //System.out.println("DISTANCE 33KV FEEDER 1 Static : "+distance);
                                    if (distance <= 10) {
                                        list.add(data.get("POLE_NO").getAsString());

                                        MultiCktVO multiCktVO = new MultiCktVO();
                                        multiCktVO.setMC_FEEDER_ID(data.get("FEEDER_ID").getAsString());
                                        multiCktVO.setGLAT(data.get("GLAT").getAsString());
                                        multiCktVO.setGLON(data.get("GLONG").getAsString());
                                        multiCktVO.setMC_POLE_NO(data.get("POLE_NO").getAsInt());
                                        mapList.add(multiCktVO);

                                    }
                                }
                                list.add("Not in list(Enter Pole number)");

                                if (mapList.size() > 0) {
                                    loadMultiVoltageMap("get", "33", mapList);
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
        retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        Call<JsonObject> responseVoCall = retrofitService.getMultiCKTData3(object);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                progressDialog.dismiss();
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    //   Log.v("POLE==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {

                            JsonArray el_kv_array = surveyedResponseVo.get("RESPONSE").getAsJsonArray();
                            if (el_kv_array.size() > 0) {
                                ArrayList<MultiCktVO> mapList = new ArrayList<>();
                                List<String> list = new ArrayList<>();
                                list.add("Select");
                                for (int i = 0; i < el_kv_array.size(); i++) {
                                    JsonObject data = el_kv_array.get(i).getAsJsonObject();
                                    double distance = getDistanceBetweenPoles(data.get("GLAT").getAsString(), data.get("GLONG").getAsString());
                                    //System.out.println("DISTANCE 33KV FEEDER 2 Static : "+distance);
                                    if (distance <= 10) {
                                        list.add(data.get("POLE_NO").getAsString());

                                        MultiCktVO multiCktVO = new MultiCktVO();
                                        multiCktVO.setMC_FEEDER_ID(data.get("FEEDER_ID").getAsString());
                                        multiCktVO.setGLAT(data.get("GLAT").getAsString());
                                        multiCktVO.setGLON(data.get("GLONG").getAsString());
                                        multiCktVO.setMC_POLE_NO(data.get("POLE_NO").getAsInt());
                                        mapList.add(multiCktVO);
                                    }
                                }
                                if (mapList.size() > 0) {
                                    loadMultiVoltageMap("get", "33", mapList);
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
        retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        Call<JsonObject> responseVoCall = retrofitService.getMultiCKTData3(object);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                progressDialog.dismiss();
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    //  Log.v("POLE==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {

                            JsonArray el_kv_array = surveyedResponseVo.get("RESPONSE").getAsJsonArray();
                            if (el_kv_array.size() > 0) {
                                ArrayList<MultiCktVO> mapList = new ArrayList<>();
                                List<String> list = new ArrayList<>();
                                list.add("Select");
                                for (int i = 0; i < el_kv_array.size(); i++) {
                                    JsonObject data = el_kv_array.get(i).getAsJsonObject();
                                    double distance = getDistanceBetweenPoles(data.get("GLAT").getAsString(), data.get("GLONG").getAsString());
                                    //System.out.println("DISTANCE 33KV FEEDER 3 Static : "+distance);
                                    if (distance <= 10) {
                                        list.add(data.get("POLE_NO").getAsString());

                                        MultiCktVO multiCktVO = new MultiCktVO();
                                        multiCktVO.setMC_FEEDER_ID(data.get("FEEDER_ID").getAsString());
                                        multiCktVO.setGLAT(data.get("GLAT").getAsString());
                                        multiCktVO.setGLON(data.get("GLONG").getAsString());
                                        multiCktVO.setMC_POLE_NO(data.get("POLE_NO").getAsInt());
                                        mapList.add(multiCktVO);
                                    }
                                }

                                if (mapList.size() > 0) {
                                    loadMultiVoltageMap("get", "33", mapList);
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
        retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        Call<JsonObject> responseVoCall = retrofitService.getMultiCKTData2(object);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                progressDialog.dismiss();
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    //  Log.v("POLE==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {

                            // arcGISDatabase.clearMultiCktData();

                            JsonArray el_kv_array = surveyedResponseVo.get("RESPONSE").getAsJsonArray();

                            if (el_kv_array.size() > 0) {
                                ArrayList<MultiCktVO> mapList = new ArrayList<>();
                                List<String> list = new ArrayList<>();
                                list.add("Select");
                                for (int i = 0; i < el_kv_array.size(); i++) {
                                    JsonObject data = el_kv_array.get(i).getAsJsonObject();
                                    double distance = getDistanceBetweenPoles(data.get("GLAT").getAsString(), data.get("GLOG").getAsString());
                                    //System.out.println("DISTANCE 11KV FEEDER 1 Static : "+distance);
                                    if (distance <= 10) {
                                        list.add(data.get("POLE_NO").getAsString());

                                        MultiCktVO multiCktVO = new MultiCktVO();
                                        multiCktVO.setMC_FEEDER_ID(data.get("FEEDER_ID").getAsString());
                                        multiCktVO.setGLAT(data.get("GLAT").getAsString());
                                        multiCktVO.setGLON(data.get("GLOG").getAsString());
                                        multiCktVO.setMC_POLE_NO(data.get("POLE_NO").getAsInt());
                                        mapList.add(multiCktVO);
                                    }

                                }
                                if (mapList.size() > 0) {
                                    loadMultiVoltageMap("get", "11", mapList);
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
        retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        Call<JsonObject> responseVoCall = retrofitService.getMultiCKTData2(object);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                progressDialog.dismiss();
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    // Log.v("POLE==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {

                            JsonArray el_kv_array = surveyedResponseVo.get("RESPONSE").getAsJsonArray();

                            if (el_kv_array.size() > 0) {
                                ArrayList<MultiCktVO> mapList = new ArrayList<>();
                                List<String> list = new ArrayList<>();
                                list.add("Select");
                                for (int i = 0; i < el_kv_array.size(); i++) {
                                    JsonObject data = el_kv_array.get(i).getAsJsonObject();
                                    double distance = getDistanceBetweenPoles(data.get("GLAT").getAsString(), data.get("GLOG").getAsString());
                                    //System.out.println("DISTANCE 11KV FEEDER 2 Static : "+distance);
                                    if (distance <= 10) {
                                        list.add(data.get("POLE_NO").getAsString());

                                        MultiCktVO multiCktVO = new MultiCktVO();
                                        multiCktVO.setMC_FEEDER_ID(data.get("FEEDER_ID").getAsString());
                                        multiCktVO.setGLAT(data.get("GLAT").getAsString());
                                        multiCktVO.setGLON(data.get("GLOG").getAsString());
                                        multiCktVO.setMC_POLE_NO(data.get("POLE_NO").getAsInt());
                                        mapList.add(multiCktVO);
                                    }
                                }
                                if (mapList.size() > 0) {
                                    loadMultiVoltageMap("get", "11", mapList);
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
        retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        Call<JsonObject> responseVoCall = retrofitService.getMultiCKTData2(object);
        responseVoCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                progressDialog.dismiss();
                if (response.body() != null) {

                    JsonObject surveyedResponseVo = response.body();
                    //  Log.v("POLE==", "" + surveyedResponseVo);
                    if (MobileConstants.TRUE.equalsIgnoreCase(surveyedResponseVo.get("STATUS").getAsString())) {
                        try {

                            JsonArray el_kv_array = surveyedResponseVo.get("RESPONSE").getAsJsonArray();

                            if (el_kv_array.size() > 0) {
                                ArrayList<MultiCktVO> mapList = new ArrayList<>();
                                List<String> list = new ArrayList<>();
                                list.add("Select");
                                for (int i = 0; i < el_kv_array.size(); i++) {
                                    JsonObject data = el_kv_array.get(i).getAsJsonObject();
                                    double distance = getDistanceBetweenPoles(data.get("GLAT").getAsString(), data.get("GLOG").getAsString());
                                    //System.out.println("DISTANCE 11KV FEEDER 3 Static : "+distance);
                                    if (distance <= 10) {
                                        list.add(data.get("POLE_NO").getAsString());

                                        MultiCktVO multiCktVO = new MultiCktVO();
                                        multiCktVO.setMC_FEEDER_ID(data.get("FEEDER_ID").getAsString());
                                        multiCktVO.setGLAT(data.get("GLAT").getAsString());
                                        multiCktVO.setGLON(data.get("GLOG").getAsString());
                                        multiCktVO.setMC_POLE_NO(data.get("POLE_NO").getAsInt());
                                        mapList.add(multiCktVO);
                                    }
                                }
                                if (mapList.size() > 0) {
                                    loadMultiVoltageMap("get", "11", mapList);
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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {

            case R.id.sp_pole_concrete:
//                pole_concrete = sp_pole_concrete.getSelectedItem().toString();

                break;

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
                try {
                    oh_isendpole = sp_oh_isendpole.getSelectedItem().toString();
                } catch (Exception e) {

                }


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

                try {
                    oh_isstreetlight = sp_oh_isstreetlight.getSelectedItem().toString();
                    if (oh_isstreetlight.equalsIgnoreCase("Yes")) {
                        street_light_layout.setVisibility(View.VISIBLE);
                    } else {
                        street_light_layout.setVisibility(View.GONE);
                    }
                } catch (Exception e) {

                }


                break;

            case R.id.sp_oh_streetlight_meter_available:
                if (sp_oh_streetlight_meter_available.getSelectedItem().toString().equalsIgnoreCase("Yes")) {
                    meter_source_layout.setVisibility(View.VISIBLE);
                } else {
                    meter_source_layout.setVisibility(View.GONE);
                }
                break;

            case R.id.sp_oh_otherthan_streetlight:

                try {
                    oh_otherthan_streetlight = sp_oh_otherthan_streetlight.getSelectedItem().toString();
                    if ("Yes".equalsIgnoreCase(oh_otherthan_streetlight)) {
                        lyt_oh_aglservice.setVisibility(View.VISIBLE);
                        lyt_oh_chacquaservices.setVisibility(View.VISIBLE);
                        lyt_oh_chindustrialservices.setVisibility(View.VISIBLE);
                        layout_oh_con_services.setVisibility(View.VISIBLE);
                    } else {
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
                    oh_pin_cutpoint = sp_oh_pin_cutpoint.getSelectedItem().toString();
                    if ("Cut Point".equalsIgnoreCase(oh_pin_cutpoint)) {
                        lyt_oh_is_doublefeeding_pole.setVisibility(View.VISIBLE);
                    } else {
                        lyt_oh_is_doublefeeding_pole.setVisibility(View.GONE);
                        sp_oh_is_doublefeeding_pole.setSelection(0);
                    }
                } catch (Exception e) {

                }

                break;

            case R.id.sp_oh_is_doublefeeding_pole:

                try {
                    oh_is_doublefeeding_pole = sp_oh_is_doublefeeding_pole.getSelectedItem().toString();
                } catch (Exception e) {

                }
                break;

            case R.id.sp_oh_other_cables:
                if (sp_oh_other_cables.getSelectedItem().toString().equals("Yes")) {
                    edt_oh_other_cables.setVisibility(View.VISIBLE);
                } else {
                    edt_oh_other_cables.setVisibility(View.GONE);
                }
                break;
            case R.id.sp_oh_pole_type:
                oh_pole_type = sp_oh_pole_type.getSelectedItem().toString();
                //  System.out.println("POLE TYPE "+oh_pole_type);

                if (repeatPoleClicked) {
                    try {
                        oh_pole_type = sp_oh_pole_type.getSelectedItem().toString();
                        if ("PSCC".equalsIgnoreCase(oh_pole_type)) {
                            // String test= (String) mapData.get("POLE_TYPE");
                            List<String> psccList = new ArrayList<>();
                            psccList.add("9.1");
                            psccList.add("11");
                            psccList.add("8.0");
                            psccList.remove(poleHeight);
                            psccList.add(0, poleHeight);
                            // psccList.add(test);
                            // System.out.println("POLE SIZE "+psccList.size());
                            // System.out.println("POLE SIZE DATA "+psccList);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, psccList);
                            sp_oh_pole_height.setAdapter(adapter);

                        } else if ("SPUN".equalsIgnoreCase(oh_pole_type)) {
                            List<String> spunList = new ArrayList<>();
                            spunList.add("9.5");
                            spunList.add("11");
                            spunList.add("12.5");
                            spunList.remove(poleHeight);
                            spunList.add(0, poleHeight);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_selectable_list_item, spunList);
                            sp_oh_pole_height.setAdapter(adapter);

                        } else if ("RAIL".equalsIgnoreCase(oh_pole_type)) {
                            List<String> railList = new ArrayList<>();
                            railList.add("9.5");
                            railList.add("11");
                            railList.add("12.5");
                            railList.remove(poleHeight);
                            railList.add(0, poleHeight);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_selectable_list_item, railList);
                            sp_oh_pole_height.setAdapter(adapter);

                        } else if ("RS JOIST".equalsIgnoreCase(oh_pole_type)) {
                            List<String> rssJoistList = new ArrayList<>();
                            rssJoistList.add("9.1");
                            rssJoistList.add("11");
                            rssJoistList.remove(poleHeight);
                            rssJoistList.add(0, poleHeight);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_selectable_list_item, rssJoistList);
                            sp_oh_pole_height.setAdapter(adapter);
                        } else if ("TOWER".equalsIgnoreCase(oh_pole_type)) {
                            List<String> towertList = new ArrayList<>();
                            towertList.add("M+3");
                            towertList.add("M+6");
                            towertList.add("M+9");
                            towertList.add("M+12");
                            towertList.add("R+6");
                            towertList.remove(poleHeight);
                            towertList.add(0, poleHeight);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_selectable_list_item, towertList);
                            sp_oh_pole_height.setAdapter(adapter);
                        } else if ("Tubular Pole".equalsIgnoreCase(oh_pole_type)) {
                            List<String> tubularList = new ArrayList<>();
                            tubularList.add("9.1");
                            tubularList.add("11");
                            tubularList.remove(poleHeight);
                            tubularList.add(0, poleHeight);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_selectable_list_item, tubularList);
                            sp_oh_pole_height.setAdapter(adapter);
                        }
                    } catch (Exception e) {

                    }
                } else {
                    try {

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
                }


                break;

            case R.id.sp_oh_pole_height:
                oh_pole_height = sp_oh_pole_height.getSelectedItem().toString();
                repeatPoleClicked = false;
                // System.out.println("SELECTED POLE HEIGHT : "+oh_pole_height);
               /* try {

                    if (sp_poleheight_KEY.equals("R")) {

                        surveyVoList = arcGISDatabase.getSurveyDataREPEAT("SELECT * FROM SURVEY_TABLE_REPEAT LIMIT 1 OFFSET (SELECT COUNT(*) FROM SURVEY_TABLE_REPEAT)-1");
                        if (surveyVoList.size() > 0) {


                            for (SurveyDataVo surveyVo : surveyVoList) {


                                String is_sp_poleheight = surveyVo.getPOLE_HEIGHT();
                             //   Toast.makeText(c, is_sp_poleheight, Toast.LENGTH_SHORT).show();

                                List<String> list_poleheight = new ArrayList<>();
                                list_poleheight.add(is_sp_poleheight);
                                ArrayAdapter<String> adapter_pole_height = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_poleheight);
                                sp_oh_pole_height.setAdapter(adapter_pole_height);

                                oh_pole_height=is_sp_poleheight = surveyVo.getPOLE_HEIGHT();


*//*

                                String st_oh_pole_height = surveyVo.getPOLE_HEIGHT();
                                List<String> list_oh_pole_height = new ArrayList<>();
                                list_oh_pole_height.add(st_oh_pole_height);

                                ArrayAdapter<String> adapter_oh_pole_height = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_oh_pole_height);
                                sp_ug_poleheight.setAdapter(adapter_oh_pole_height);
*//*



                              //  ug_poleheight=surveyVo.getPOLE_HEIGHT();

                              *//*  if (surveyVo.getLINE_TYPE().equals("Overhead")){

                                    oh_pole_height=surveyVo.getPOLE_HEIGHT();

                                }
                                else if (surveyVo.getLINE_TYPE().equals("UnderGround")){
                                    ug_poleheight=surveyVo.getPOLE_HEIGHT();
                                  //  sp_ug_poleheight.setAdapter(adapter_oh_pole_height);
                                }
*//*


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

                    *//*if(!"Select".equalsIgnoreCase(sp_oh_pole_height.getSelectedItem().toString())){
                        oh_pole_height=sp_oh_pole_height.getSelectedItem().toString();
                    }else {
                        oh_pole_height="";
                    }*//*
                } catch (Exception e) {

                }*/


                break;
            case R.id.sp_oh_strut_stay_details:

                try {
                    oh_strut_stay_details = sp_oh_strut_stay_details.getSelectedItem().toString();
                } catch (Exception e) {

                }


                break;

            case R.id.sp_oh_tapping_point:

                try {
                    oh_tapping_point = sp_oh_tapping_point.getSelectedItem().toString();
                    if ("Yes".equalsIgnoreCase(oh_tapping_point)) {
                        lyt_oh_noof_spurlines.setVisibility(View.VISIBLE);
                    } else {
                        lyt_oh_noof_spurlines.setVisibility(View.GONE);
                        edt_oh_tappinglocation.setText("");
                        sp_oh_noof_spurlines.setSelection(0);
                    }
                } catch (Exception e) {

                }


                break;

            case R.id.sp_oh_noof_spurlines:

                try {
                    oh_noof_spurlines = sp_oh_noof_spurlines.getSelectedItem().toString();
                } catch (Exception e) {

                }


                break;

            case R.id.sp_oh_conductor_size:

                try {
                    oh_conductor_size = sp_oh_conductor_size.getSelectedItem().toString();
                } catch (Exception e) {

                }


                break;

            case R.id.sp_oh_ismulti_circuit:
                try {
                    oh_ismulti_circuit = sp_oh_ismulti_circuit.getSelectedItem().toString();
                    if ("Yes".equalsIgnoreCase(oh_ismulti_circuit)) {
                        lyt_oh_noof_circuits.setVisibility(View.GONE);
                        lyt_spi_no_circuit.setVisibility(View.VISIBLE);
                        lyt_oh_multivoltage.setVisibility(View.VISIBLE);
                        oh_noof_circuits = "";
                        loadMultiVoltageMap("normal", "all", null);
                    } else {
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
                    oh_noof_circuits = sp_oh_noof_circuits.getSelectedItem().toString();
                } catch (Exception e) {

                }


                break;

            case R.id.sp_oh_multi_voltage_same_voltage:

                try {
                    oh_multi_voltage_same_voltage = sp_oh_multi_voltage_same_voltage.getSelectedItem().toString();
                    if ("Multi Voltage".equalsIgnoreCase(oh_multi_voltage_same_voltage)) {
                        lyt_oh_multivoltage.setVisibility(View.VISIBLE);
                        // lyt_spi_no_circuit.setVisibility(View.GONE);
                        oh_noof_circuits = "";
                        loadMultiVoltageMap("normal", "all", null);
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
                } catch (Exception e) {

                }


                break;
            //UG
            case R.id.sp_ug_isendpole:

                try {
                    ug_isendpole = sp_ug_isendpole.getSelectedItem().toString();
                } catch (Exception e) {

                }


                break;
            case R.id.sp_ug_locationtype:

                try {
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
                        ug_servicepiller_type = "";
                        ug_poletype = "";
                        ug_poleheight = "";
                        ug_polesupport = "";
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
                    ug_servicepiller_type = sp_ug_servicepiller_type.getSelectedItem().toString();
                } catch (Exception e) {

                }

                break;
            case R.id.sp_ug_poletype:

                try {
                    ug_poletype = sp_ug_poletype.getSelectedItem().toString();
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
                    ug_poleheight = sp_ug_poleheight.getSelectedItem().toString();


                } catch (Exception e) {

                }


                break;

            case R.id.sp_ug_polesupport:

                try {
                    ug_polesupport = sp_ug_polesupport.getSelectedItem().toString();
                } catch (Exception e) {

                }

                break;

            case R.id.sp_ug_feedepiller_outg_circuits:

                try {
                    ug_feedepiller_outg_circuits = sp_ug_feedepiller_outg_circuits.getSelectedItem().toString();
                } catch (Exception e) {

                }


                break;

            case R.id.sp_ug_tapping_point:
                try {
                    ug_tapping_point = sp_ug_tapping_point.getSelectedItem().toString();
                    if ("Yes".equalsIgnoreCase(ug_tapping_point)) {
                        lyt_ug_tappingpoint.setVisibility(View.VISIBLE);
                        if (repeatPoleClicked) {
                            edt_ug_tappinglocation.setText(not);
                        }
                    } else {
                        lyt_ug_tappingpoint.setVisibility(View.GONE);
                        sp_ug_notapping_point.setSelection(0);
                        edt_ug_tappinglocation.setText("");
                    }
                } catch (Exception e) {

                }

                break;
            case R.id.sp_ug_notapping_point:
                try {
                    ug_notapping_point = sp_ug_notapping_point.getSelectedItem().toString();
                } catch (Exception e) {

                }

                break;

            case R.id.sp_ug_cablesize:
                try {
                    ug_cablesize = sp_ug_cablesize.getSelectedItem().toString();
                } catch (Exception e) {

                }

                break;
            case R.id.sp_ug_nocable:
                try {
                    ug_nocable = sp_ug_nocable.getSelectedItem().toString();
                } catch (Exception e) {

                }

                break;

            case R.id.sp_crossing:
                try {

                    crossing = sp_crossing.getSelectedItem().toString();
                    if ("YES".equalsIgnoreCase(crossing)) {
                        lyt_oh_crossing.setVisibility(View.VISIBLE);
                    } else {
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

            case R.id.sp_from_pole:
                if (!sp_from_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    String selectedPoleNo = sp_from_pole.getSelectedItem().toString();
                    ed_frompole.setText(selectedPoleNo);
                    sp_from_pole.setVisibility(View.GONE);
                    tv_from_pole_layout.setVisibility(View.VISIBLE);

                    double currentLat = Double.parseDouble(x); // current/new pole LAT
                    double currentLon = Double.parseDouble(y); // current/new pole LON

                    // Retrieve list stored in spinner tag
                    List<SurveyDataVo> list = (List<SurveyDataVo>) sp_from_pole.getTag();

                    // Find the selected pole’s coordinates
                    for (SurveyDataVo vo : list) {
                        if (String.valueOf(vo.getPOLE_NO()).equals(selectedPoleNo)) {
                            String selLat = vo.getLAT();
                            String selLon = vo.getLAN();

                            if (selLat != null && selLon != null && !selLat.isEmpty() && !selLon.isEmpty()) {
                                double distance = getDistanceBetweenPoles(selLat, selLon, currentLat, currentLon);
                                lastPoleDistance = distance;
                                int a = (int) Math.round(lastPoleDistance);
                                last_pole_distance.setText("" + a);
                                System.out.println("Distance between current pole and selected pole: " + distance + " meters");
                            } else {
                                System.out.println("Selected pole lat/lon not found");
                            }
                            break;
                        }
                    }
                }
                break;


        }
    }

    private double getDistanceBetweenPoles(String polLat, String polLon, double fromLat, double fromLon) {

        System.out.println("CURRENT LOCATION LAT : " + fromLat);
        System.out.println("CURRENT LOCATION LON : " + fromLon);
        System.out.println("POLE LOCATION LAT : " + polLat);
        System.out.println("POLE LOCATION LON : " + polLon);
        Location currentStartPoint = new Location("locationP");
        currentStartPoint.setLatitude(fromLon);
        currentStartPoint.setLongitude(fromLat);

        Location currentEndPoint = new Location("locationP");
        currentEndPoint.setLatitude(Double.parseDouble(polLon));
        currentEndPoint.setLongitude(Double.parseDouble(polLat));
        double currentDistance = currentStartPoint.distanceTo(currentEndPoint);
        // System.out.println("POLE LOCATION DISTANCE : " + currentDistance);
        return currentDistance;
    }

    private void loadMultiVoltageMap(String type, String from, ArrayList<MultiCktVO> update) {
        ArrayList<MultiCktVO> old_33_list = new ArrayList<>(DTRSelectActivity.list33kv);
        ArrayList<MultiCktVO> list = new ArrayList<>();
        ArrayList<MultiCktVO> old_list_11kv = new ArrayList<>(DTRSelectActivity.list11kv);
        ArrayList<MultiCktVO> list_11kv = new ArrayList<>();

        //System.out.println("33KV SIZE BEFORE : "+old_33_list.size());
        //System.out.println("11KV SIZE BEFORE : "+old_list_11kv.size());

        for (int j = 0; j < old_33_list.size(); j++) {
            double distance = getDistanceBetweenPoles(old_33_list.get(j).getGLAT(), old_33_list.get(j).getGLON());
            //System.out.println("DISTANCE 33KV FEEDER STATIC : "+distance);
            if (distance <= 10) {
                list.add(old_33_list.get(j));
            }
        }

        for (int j = 0; j < old_list_11kv.size(); j++) {
            double distance = getDistanceBetweenPoles(old_list_11kv.get(j).getGLAT(), old_list_11kv.get(j).getGLON());
            //System.out.println("DISTANCE 11KV FEEDER STATIC : "+distance);
            if (distance <= 10) {
                list_11kv.add(old_list_11kv.get(j));
            }
        }

        //System.out.println("33KV SIZE AFTER : "+list.size());
        //System.out.println("11KV SIZE AFTER : "+list_11kv.size());

        for (int i = 0; i < list.size(); i++) {
            ptWgs84_33kv = new Point(Double.parseDouble(list.get(i).getGLAT()), Double.parseDouble(list.get(i).getGLON()), 414, SpatialReferences.getWgs84());
            Viewpoint vp = new Viewpoint(ptWgs84_33kv, 7500);
            map_33kv.setInitialViewpoint(vp);
            mv_33kv.setMap(map_33kv);
            graphicsOverlay_33kv = new GraphicsOverlay();
            mv_33kv.getGraphicsOverlays().add(graphicsOverlay_33kv);
            SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 12);
            graphic_33kv = new Graphic(ptWgs84_33kv, symbol);
            graph_objects_33kv.add(graphic_33kv);
            graphicsOverlay_33kv.getGraphics().add(graphic_33kv);
            GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
            absoluteOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties.SurfacePlacement.ABSOLUTE);
            absoluteOverlay.getGraphics().add(new Graphic(ptWgs84_33kv, symbol));
            TextSymbol absoluteText = new TextSymbol(15, list.get(i).getMC_FEEDER_ID() + "-" + list.get(i).getMC_POLE_NO(), Color.WHITE, TextSymbol.HorizontalAlignment.LEFT,
                    TextSymbol.VerticalAlignment.MIDDLE);
            absoluteText.setOffsetY(20);
            absoluteOverlay.getGraphics().add(new Graphic(ptWgs84_33kv, absoluteText));
            mv_33kv.getGraphicsOverlays().add(absoluteOverlay);
        }

        for (int i = 0; i < list_11kv.size(); i++) {
            // System.out.println("11KV LAT : "+list_11kv.get(i).getGLAT());
            // System.out.println("11KV LON : "+list_11kv.get(i).getGLON());
            ptWgs84_33kv = new Point(Double.parseDouble(list_11kv.get(i).getGLAT()), Double.parseDouble(list_11kv.get(i).getGLON()), 414, SpatialReferences.getWgs84());
            Viewpoint vp = new Viewpoint(ptWgs84_33kv, 7500);
            map_33kv.setInitialViewpoint(vp);
            mv_33kv.setMap(map_33kv);
            graphicsOverlay_33kv = new GraphicsOverlay();
            mv_33kv.getGraphicsOverlays().add(graphicsOverlay_33kv);
            SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.BLUE, 12);
            graphic_33kv = new Graphic(ptWgs84_33kv, symbol);
            graph_objects_33kv.add(graphic_33kv);
            graphicsOverlay_33kv.getGraphics().add(graphic_33kv);
            GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
            absoluteOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties.SurfacePlacement.ABSOLUTE);
            absoluteOverlay.getGraphics().add(new Graphic(ptWgs84_33kv, symbol));
            TextSymbol absoluteText = new TextSymbol(15, list_11kv.get(i).getMC_FEEDER_ID() + "-" + list_11kv.get(i).getMC_POLE_NO(), Color.WHITE, TextSymbol.HorizontalAlignment.LEFT,
                    TextSymbol.VerticalAlignment.MIDDLE);
            absoluteText.setOffsetY(20);
            absoluteOverlay.getGraphics().add(new Graphic(ptWgs84_33kv, absoluteText));
            mv_33kv.getGraphicsOverlays().add(absoluteOverlay);
        }

        if (type.equals("get") && from.equals("33")) {
            for (int i = 0; i < update.size(); i++) {
                ptWgs84_33kv = new Point(Double.parseDouble(update.get(i).getGLAT()), Double.parseDouble(update.get(i).getGLON()), 414, SpatialReferences.getWgs84());
                Viewpoint vp = new Viewpoint(ptWgs84_33kv, 7500);
                map_33kv.setInitialViewpoint(vp);
                mv_33kv.setMap(map_33kv);
                graphicsOverlay_33kv = new GraphicsOverlay();
                mv_33kv.getGraphicsOverlays().add(graphicsOverlay_33kv);
                SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 12);
                graphic_33kv = new Graphic(ptWgs84_33kv, symbol);
                graph_objects_33kv.add(graphic_33kv);
                graphicsOverlay_33kv.getGraphics().add(graphic_33kv);
                GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                absoluteOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties.SurfacePlacement.ABSOLUTE);
                absoluteOverlay.getGraphics().add(new Graphic(ptWgs84_33kv, symbol));
                TextSymbol absoluteText = new TextSymbol(15, update.get(i).getMC_FEEDER_ID() + "-" + update.get(i).getMC_POLE_NO(), Color.WHITE, TextSymbol.HorizontalAlignment.LEFT,
                        TextSymbol.VerticalAlignment.MIDDLE);
                absoluteText.setOffsetY(20);
                absoluteOverlay.getGraphics().add(new Graphic(ptWgs84_33kv, absoluteText));
                mv_33kv.getGraphicsOverlays().add(absoluteOverlay);
            }
        }

        if (type.equals("get") && from.equals("11")) {
            for (int i = 0; i < update.size(); i++) {
                // System.out.println("11KV LAT : "+list_11kv.get(i).getGLAT());
                // System.out.println("11KV LON : "+list_11kv.get(i).getGLON());
                ptWgs84_33kv = new Point(Double.parseDouble(update.get(i).getGLAT()), Double.parseDouble(update.get(i).getGLON()), 414, SpatialReferences.getWgs84());
                Viewpoint vp = new Viewpoint(ptWgs84_33kv, 7500);
                map_33kv.setInitialViewpoint(vp);
                mv_33kv.setMap(map_33kv);
                graphicsOverlay_33kv = new GraphicsOverlay();
                mv_33kv.getGraphicsOverlays().add(graphicsOverlay_33kv);
                SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.BLUE, 12);
                graphic_33kv = new Graphic(ptWgs84_33kv, symbol);
                graph_objects_33kv.add(graphic_33kv);
                graphicsOverlay_33kv.getGraphics().add(graphic_33kv);
                GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                absoluteOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties.SurfacePlacement.ABSOLUTE);
                absoluteOverlay.getGraphics().add(new Graphic(ptWgs84_33kv, symbol));
                TextSymbol absoluteText = new TextSymbol(15, update.get(i).getMC_FEEDER_ID() + "-" + update.get(i).getMC_POLE_NO(), Color.WHITE, TextSymbol.HorizontalAlignment.LEFT,
                        TextSymbol.VerticalAlignment.MIDDLE);
                absoluteText.setOffsetY(20);
                absoluteOverlay.getGraphics().add(new Graphic(ptWgs84_33kv, absoluteText));
                mv_33kv.getGraphicsOverlays().add(absoluteOverlay);
            }
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
                    /*ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, DTRSelectActivity.feederList11kv);
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
                    edt_oh_6_3_crossing.setText("");


                }
                break;

            case R.id.ch_oh_11kv_crossing:
                if (ch_oh_11kv_crossing.isChecked()) {
                    lyt_oh_11kv_crossing.setVisibility(View.VISIBLE);
                   /* ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, DTRSelectActivity.feederList11kv);
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
                    edt_oh_11kv_crossing.setText("");

                }
                break;

            case R.id.ch_oh_33kv_crossing:
                if (ch_oh_33kv_crossing.isChecked()) {
                    lyt_oh_33kv_crossing.setVisibility(View.VISIBLE);
                   /* ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, DTRSelectActivity.feederList33kv);
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
                    edt_oh_33kv_crossing.setText("");
                }
                break;

            case R.id.ch_oh_lt_crossing:
                if (ch_oh_lt_crossing.isChecked()) {
                    lyt_oh_lt_crossing.setVisibility(View.VISIBLE);
                   /* ArrayAdapter adapter_feeder = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, DTRSelectActivity.feederListLT);
                    edt_oh_lt_crossing.setAdapter(adapter_feeder);
                    edt_oh_lt_crossing.setThreshold(5);*/

                    SearchableSpinner searchableSpinner = new SearchableSpinner(c);
                    searchableSpinner.setWindowTitle("SELECT DTR CODE");
                    searchableSpinner.setSpinnerListItems(DTRSelectActivity.feederListLT);
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

    private void open11KVFeederMasterDialog(TextView edt_text) {
        Dialog payDialog = new Dialog(c);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        c.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        payDialog.setContentView(R.layout.feeder_master_dialog);
        payDialog.getWindow().setLayout(width, height / 2);
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
                if (selectedRadioButton.equals(" From Same Section")) {
                    SearchableSpinner searchableSpinner = new SearchableSpinner(c);
                    searchableSpinner.setWindowTitle("SELECT FEEDER");
                    ArrayList<String> feedersList = new ArrayList<>();
                    List<LoginDataResponse> loginDataResponses = arcGISDatabase.getUserMasterData();
                    String office_id = loginDataResponses.get(0).getOFFICE_ID();
                    // System.out.println("OFFICE ID : "+office_id);
                    for (int i = 0; i < DTRSelectActivity.feederMasterList11KV.size(); i++) {
                        if (DTRSelectActivity.feederMasterList11KV.get(i).getSEC_ID().equals(office_id)) {
                            //  System.out.println("SEC ID : "+DTRSelectActivity.feederMasterList11KV.get(i).getSEC_ID());
                            feedersList.add(DTRSelectActivity.feederMasterList11KV.get(i).getFEEDER_ID()
                                    + "~" + DTRSelectActivity.feederMasterList11KV.get(i).getFEEDER_NAME());
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

                } else {
                    SearchableSpinner searchableSpinner = new SearchableSpinner(c);
                    searchableSpinner.setWindowTitle("SELECT FEEDER");
                    searchableSpinner.setSpinnerListItems(DTRSelectActivity.feederList11kv);
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
        payDialog.getWindow().setLayout(width, height / 2);
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
                if (selectedRadioButton.equals(" From Same Section")) {
                    SearchableSpinner searchableSpinner = new SearchableSpinner(c);
                    searchableSpinner.setWindowTitle("SELECT FEEDER");
                    ArrayList<String> feedersList = new ArrayList<>();
                    for (int i = 0; i < DTRSelectActivity.feederMasterList33KV.size(); i++) {
                        if (DTRSelectActivity.feederMasterList33KV.get(i).getSEC_ID().equals("SAME")) {
                            feedersList.add(DTRSelectActivity.feederMasterList33KV.get(i).getFEEDER_ID()
                                    + "~" + DTRSelectActivity.feederMasterList33KV.get(i).getFEEDER_NAME());
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

                } else {
                    SearchableSpinner searchableSpinner = new SearchableSpinner(c);
                    searchableSpinner.setWindowTitle("SELECT FEEDER");
                    searchableSpinner.setSpinnerListItems(DTRSelectActivity.feederList33kv);
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

    @Override
    public void onClick(View v) {
        Animation animation = AnimationUtils.loadAnimation(c, R.anim.bounce);
        switch (v.getId()) {


            case R.id.btn_repeat_pole:
                btn_repeat_pole.startAnimation(animation);
                image_1_clicked = false;
                image_2_clicked = false;
                image_3_clicked = false;
                repeatPoleClicked = true;
                // re_pole();
                repeatPoleNew();
                break;


            case R.id.btn_clear:
                image_1_clicked = false;
                image_2_clicked = false;
                image_3_clicked = false;
                repeatPoleClicked = false;
                sp_poleheight_KEY = "N";
                callout.dismiss();
                graphicsOverlay.getGraphics().remove(graphic);
                dismiss();
                break;

            case R.id.btn_add:
                if (ed_pole_no.getText().toString().equals("1") && lastPoleDistance > 10) {
                    Toast.makeText(c, "Abnormal Distance:\n" +
                            "LT First Pole Must be Close to DTR", Toast.LENGTH_SHORT).show();
                } else if ("OverHead".equalsIgnoreCase(lineType)) {
                    if (sp_oh_defects.getSelectedItem().toString().equalsIgnoreCase("Yes")) {
                        validateDefects();
                    } else {
                        addOhData();
                    }

                } else if ("UnderGround".equalsIgnoreCase(lineType)) {
                    underGround();
                }

                break;
            case R.id.btn_lttopclear:
                image_1_clicked = false;
                image_2_clicked = false;
                image_3_clicked = false;
                repeatPoleClicked = false;
                callout.dismiss();
                graphicsOverlay.getGraphics().remove(graphic);
                dismiss();
                break;

        }


    }

    private void repeatPoleNew() {
        sp_poleheight_KEY = "R";
        surveyVoList = arcGISDatabase.getSurveyDataREPEAT("SELECT * FROM SURVEY_TABLE_REPEAT LIMIT 1 OFFSET (SELECT COUNT(*) FROM SURVEY_TABLE_REPEAT)-1");

        if (surveyVoList.size() > 0) {
            for (SurveyDataVo surveyVo : surveyVoList) {
                //POLE HEIGHT
                poleHeight = surveyVo.getPOLE_HEIGHT();

                if (LoginActivity.isJHC) {
                    //Pole Survey Type
                    String pst = surveyVo.getPOLE_SURVEY_TYPE();
                    //  System.out.println("POLE SURVEY TYPE : "+pst);
                    List<String> list_pst = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.pole_survey_type)));
                    list_pst.remove(pst);
                    list_pst.remove(pst.equalsIgnoreCase("JHC") ? "HC Phase 1" : pst);
                    list_pst.remove("Select");
                    list_pst.add(0, pst.equalsIgnoreCase("JHC") ? "HC Phase 1" : pst);
                    ArrayAdapter<String> adapter_pst = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_pst);
                    sp_pole_survey_type.setAdapter(adapter_pst);
                    sp_pole_survey_type.setSelection(0, true);

                    //Line Survey Type
                    String lst = surveyVo.getLINE_SURVEY_TYPE();
                    //  System.out.println("LINE SURVEY TYPE : "+lst);
                    List<String> list_lst = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.pole_survey_type)));
                    list_lst.remove(lst.equalsIgnoreCase("JHC") ? "HC Phase 1" : lst);
                    list_lst.remove("Select");
                    list_lst.add(0, lst.equalsIgnoreCase("JHC") ? "HC Phase 1" : lst);
                    ArrayAdapter<String> adapter_lst = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_lst);
                    sp_line_survey_type.setAdapter(adapter_lst);
                    sp_line_survey_type.setSelection(0, true);
                }

                //LINE TYPE
                String lineType = surveyVo.getLINE_TYPE();
                List<String> list1 = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.line_type)));
                list1.remove(lineType);
                list1.add(0, lineType);
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list1);
                sp_lineType.setAdapter(adapter1);
                sp_lineType.setSelection(0, true);

                //OVERHEAD
                if (lineType.equalsIgnoreCase("OverHead")) {
                    //LINE CONFIGURATION
                    String lineConfig = surveyVo.getLINE_CONFIGURATION();
                    List<String> list2 = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.oh_line_config)));
                    list2.remove(lineConfig);
                    list2.add(0, lineConfig);
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list2);
                    sp_oh_lineconfig.setAdapter(adapter2);
                    sp_oh_lineconfig.setSelection(0, true);

                    //POLE SUPPORT
                    String poleSupport = surveyVo.getSTRUT_STAY();
                    List<String> list3 = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.oh_strut)));
                    list3.remove(poleSupport);
                    list3.add(0, poleSupport);
                    ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list3);
                    sp_oh_strut_stay_details.setAdapter(adapter3);
                    sp_oh_strut_stay_details.setSelection(0, true);

                    //CONDUCTOR SIZE
                    String conductorSize = surveyVo.getCONDUCTOR_SIZE();
                    List<String> list4 = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.oh_conductorsize)));
                    list4.remove("Select");
                    list4.remove(conductorSize);
                    list4.add(0, conductorSize);
                    ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list4);
                    sp_oh_conductor_size.setAdapter(adapter4);
                    sp_oh_conductor_size.setSelection(0, true);

                    //Is Multi Circuit
                    String isMultiCircuit = surveyVo.getIS_MULTI_CIRCUIT();
                    List<String> list5 = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.oh_is_multi_circuit_lt)));
                    list5.remove(isMultiCircuit);
                    list5.add(0, isMultiCircuit);
                    ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list5);
                    sp_oh_ismulti_circuit.setAdapter(adapter5);
                    sp_oh_ismulti_circuit.setSelection(0, true);

                    //No Of Circuits
                    String noOfCircuits = surveyVo.getNO_OF_CIRCUITS();
                    // System.out.println("NO OF CIRCUITS : "+noOfCircuits);
                    List<String> list6 = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.oh_noof_ckts)));
                    list6.remove(noOfCircuits);
                    list6.add(0, noOfCircuits);
                    ArrayAdapter<String> adapter6 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list6);
                    sp_oh_noof_circuits.setAdapter(adapter6);
                    sp_oh_noof_circuits.setSelection(0, true);

                    //Multi Voltage & Same Voltage
                    String mvsv = surveyVo.getMULTI_VOLTAGE();
                    List<String> list7 = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.oh_same_multi)));
                    list7.remove(mvsv);
                    list7.add(0, mvsv);
                    ArrayAdapter<String> adapter7 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list7);
                    sp_oh_multi_voltage_same_voltage.setAdapter(adapter7);
                    sp_oh_multi_voltage_same_voltage.setSelection(0, true);

                    //CROSSING
                    String crossing = surveyVo.getIS_CROSSINGS_AV();
                    List<String> list8 = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.oh_crossing)));
                    list8.remove(crossing);
                    list8.add(0, crossing);
                    ArrayAdapter<String> adapter8 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list8);
                    sp_crossing.setAdapter(adapter8);
                    sp_crossing.setSelection(0, true);

                    //POLE TYPE
                    String poleType = surveyVo.getPOLE_TYPE();
                    List<String> list9 = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.oh_poletype)));
                    list9.remove("Select");
                    list9.remove(poleType);
                    list9.add(0, poleType);
                    ArrayAdapter<String> adapter9 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list9);
                    sp_oh_pole_type.setAdapter(adapter9);
                    // sp_oh_pole_type.setSelection(0,true);

                    //PIN POINT
                    String pinPoint = surveyVo.getPIN_CUT();
                    List<String> list10 = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.oh_pin_point)));
                    list10.remove(pinPoint);
                    list10.add(0, pinPoint);
                    ArrayAdapter<String> adapter10 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list10);
                    sp_oh_pin_cutpoint.setAdapter(adapter10);
                    sp_oh_pin_cutpoint.setSelection(0, true);

                    //DOUBLE FEEDING POLE
                    String df = surveyVo.getIS_DOUBLE_FEEDING();
                    List<String> list11 = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.oh_is_doublefeeding)));
                    list11.remove(df);
                    list11.add(0, df);
                    ArrayAdapter<String> adapter11 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list11);
                    sp_oh_is_doublefeeding_pole.setAdapter(adapter11);
                    sp_oh_is_doublefeeding_pole.setSelection(0, true);

                    //TAPPING POINT
                    String tp = surveyVo.getTAPPING_POINT();
                    List<String> list12 = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.oh_tappingpoint)));
                    list12.remove(tp);
                    list12.add(0, tp);
                    ArrayAdapter<String> adapter12 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list12);
                    sp_oh_tapping_point.setAdapter(adapter12);
                    sp_oh_tapping_point.setSelection(0, true);

                    //NO OF TAPINGS
                    String not = surveyVo.getNO_OF_SPURLINES();
                    List<String> list13 = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.oh_noof_spurlines)));
                    list13.remove(not);
                    list13.add(0, not);
                    ArrayAdapter<String> adapter13 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list13);
                    sp_oh_noof_spurlines.setAdapter(adapter13);
                    sp_oh_noof_spurlines.setSelection(0, true);

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
                    String isEndPole = surveyVo.getIS_END_POLE();
                    List<String> list3 = new ArrayList<>();
                    if (isEndPole.equals("No")) {
                        list3.add("No");
                        list3.add("Yes");
                    } else {
                        list3.add("Yes");
                        list3.add("No");
                    }
                    ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list3);
                    sp_ug_isendpole.setAdapter(adapter3);
                    sp_ug_isendpole.setSelection(0, true);

                    //NO OF CABLES
                    String noc = surveyVo.getNO_CABLES();
                    List<String> list2 = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.ug_no_cables)));
                    list2.remove(noc);
                    //list2.remove("Select");
                    list2.add(0, noc);
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list2);
                    sp_ug_nocable.setAdapter(adapter2);
                    sp_ug_nocable.setSelection(0, true);

                    //TAPPING POINT
                    String tp = surveyVo.getTAPPING_POINT();
                    List<String> list12 = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.ug_tapping_point)));
                    list12.remove(tp);
                    list12.add(0, tp);
                    ArrayAdapter<String> adapter12 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list12);
                    sp_ug_tapping_point.setAdapter(adapter12);
                    sp_ug_tapping_point.setSelection(0, true);


                    if (tp.equalsIgnoreCase("Yes")) {
                        //NO OF TAPPINGS
                        String no_of_t = surveyVo.getNO_OF_SPURLINES();
                        List<String> list_no_of_t = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.ug_notapping_point)));
                        list_no_of_t.remove(no_of_t);
                        list_no_of_t.add(0, no_of_t);
                        ArrayAdapter<String> adapter_no_of_t = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_no_of_t);
                        sp_ug_notapping_point.setAdapter(adapter_no_of_t);
                        sp_ug_notapping_point.setSelection(0, true);

                        //TAPING LOCATION NAME
                        not = surveyVo.getTAPPING_LOCATION();
                        edt_ug_tappinglocation.setText(not);
                    }


                    //CABLE SIZE
                    String cable_size = surveyVo.getCABLE_SIZE();
                    //NO Of Cables
                    String CABLE_TX = "CABLE";
                    //  List<String> list1_cable_size = arcGISDatabase.getComplaint("Select CKT_VALUE from MULTIVOLTAGE_MASTERS_TABLE where CKT_NO = '" + CABLE_TX + "'" + " ORDER BY CKT_VALUE");
                    List<String> list1_cable_size = new ArrayList<>(Arrays.asList(c.getResources().getStringArray(R.array.ug_cablesize)));
                    // list1_cable_size.remove("Select");
                    list1_cable_size.remove(cable_size);
                    list1_cable_size.add(0, cable_size);
                    ArrayAdapter<String> adapter_cable_size = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list1_cable_size);
                    sp_ug_cablesize.setAdapter(adapter_cable_size);
                    sp_ug_cablesize.setSelection(0, true);

                }


            }
        }

    }

    private void validateDefects() {
        // ,,,,,,,;
        //,,,,,,,;

        if (!ch_pole_defects.isChecked() && !ch_conductors.isChecked() && !ch_insulators.isChecked() &&
                !ch_cross_arm.isChecked() && !ch_strut_pole.isChecked() && !ch_stray_wire.isChecked() &&
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
                Toast.makeText(c, "Select Conductor", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (ch_insulators.isChecked()) {
            if (sp_insulators.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(c, "Select Insulator", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (ch_cross_arm.isChecked()) {
            if (sp_cross_arm.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(c, "Select Cross Arm", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (ch_strut_pole.isChecked()) {
            if (sp_strut_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(c, "Select Strut Pole", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (ch_stray_wire.isChecked()) {
            if (sp_stray_wire.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(c, "Select Stay Wire", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (ch_ht_clearance.isChecked()) {
            if (sp_ht_clearance.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(c, "Select HT/LT Clearance", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (ch_int_pole.isChecked()) {
            if (sp_int_pole.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                Toast.makeText(c, "Select Intermediate Pole", Toast.LENGTH_SHORT).show();
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

        addOhData();
    }

    public void addOhData() {

        if (LoginActivity.isJHC && sp_pole_survey_type.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(c, "Select Pole Survey Type", Toast.LENGTH_LONG).show();
        } else if (LoginActivity.isJHC && sp_line_survey_type.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(c, "Select Line Survey Type", Toast.LENGTH_LONG).show();
        } else if ("Select".equalsIgnoreCase(sp_oh_lineconfig.getSelectedItem().toString())) {
            Toast.makeText(c, "Select Line Configuration", Toast.LENGTH_SHORT).show();
        }/*else if("DTR Pole".equalsIgnoreCase(oh_lineconfig)&&"Yes".equalsIgnoreCase(oh_dtr_pole_dtr_meter_avilable)&&"Select".equalsIgnoreCase(oh_dtr_pole_no_of_meters)){
                Toast.makeText(c,"Select No Of Meters",Toast.LENGTH_SHORT).show();
        }*/ else if ("Select".equalsIgnoreCase(sp_oh_conductor_size.getSelectedItem().toString())) {
            Toast.makeText(c, "Conductor Size", Toast.LENGTH_SHORT).show();
        } else if ("Select".equalsIgnoreCase(sp_oh_pole_type.getSelectedItem().toString())) {
            Toast.makeText(c, "Select Pole Type", Toast.LENGTH_SHORT).show();
        } else if ("TOWER".equalsIgnoreCase(sp_oh_pole_type.getSelectedItem().toString()) && lastPoleDistance > 400) {
            Toast.makeText(c, "Abnormal Distance\nDistance below 400 meters\nis allowed for TOWER", Toast.LENGTH_LONG).show();
        } else if (!"TOWER".equalsIgnoreCase(sp_oh_pole_type.getSelectedItem().toString()) && lastPoleDistance > 120) {
            Toast.makeText(c, "Abnormal Distance\nDistance below 120 meters\nis allowed", Toast.LENGTH_LONG).show();
        } else if ("Select".equalsIgnoreCase(sp_oh_pole_height.getSelectedItem().toString())) {
            Toast.makeText(c, "Select Pole Height", Toast.LENGTH_SHORT).show();
        } else if ("Select".equalsIgnoreCase(sp_oh_ismulti_circuit.getSelectedItem().toString())) {
            Toast.makeText(c, "Select Is Multi Circuit", Toast.LENGTH_SHORT).show();
        } else if (sp_oh_ismulti_circuit.getSelectedItem().toString().equals("Yes") && "Multi Voltage".equalsIgnoreCase(oh_multi_voltage_same_voltage) && !ch_oh_33kv_vlckts.isChecked() && !ch_oh_11kv_vlckts.isChecked() && !ch_oh_6_3kv_vlckts.isChecked() && !ch_oh_lt_vlckts.isChecked()) {
            //"".equalsIgnoreCase(edt_oh_33kv_vlckts.getText().toString()) && "".equalsIgnoreCase(edt_oh_11kv_vlckts.getText().toString()) && "".equalsIgnoreCase(edt_oh_6_3kv_vlckts.getText().toString()) && "".equalsIgnoreCase(edt_oh_lt_vlckts.getText().toString())){
            Toast.makeText(c, "Select Any One Of Voltage Circuit", Toast.LENGTH_SHORT).show();
        } else if (sp_oh_ismulti_circuit.getSelectedItem().toString().equals("Yes") && ch_oh_33kv_vlckts.isChecked() && "Select".equalsIgnoreCase(sp_oh_no_of_33_feeders.getSelectedItem().toString())) {
            Toast.makeText(c, "Select No. of 33kv Voltage Ckts", Toast.LENGTH_SHORT).show();
        }

        //NEW MULTI CKT


        else if (validate33KVVoltageCircuit()) {

        } else if (validate11KVVoltageCircuit()) {

        } else if (validateLTVoltageCircuit()) {

        } else if (validateNoOfCircuits()) {
            Toast.makeText(c, "No of circuits and selected circuits are not matching", Toast.LENGTH_SHORT).show();
        }

        /*else if (validate33KVSamePole()){

        }*/


        else if (ch_oh_11kv_vlckts.isChecked() && "Select".equalsIgnoreCase(sp_oh_no_of_11_feeders.getSelectedItem().toString())) {
            Toast.makeText(c, "Select No. of 11kv Voltage Ckts", Toast.LENGTH_SHORT).show();
        } else if (ch_oh_lt_vlckts.isChecked() && "Select".equalsIgnoreCase(sp_oh_no_of_lt_feeders.getSelectedItem().toString())) {
            Toast.makeText(c, "Select No. of LT Voltage Ckts", Toast.LENGTH_SHORT).show();
        } else if (ch_oh_6_3kv_vlckts.isChecked() && "".equalsIgnoreCase(edt_oh_6_3kv_vlckts.getText().toString())) {
            Toast.makeText(c, "Enter No. of 6.3kv Voltage Ckts", Toast.LENGTH_SHORT).show();
        } else if ("YES".equalsIgnoreCase(crossing) && !ch_oh_6_3_crossing.isChecked() && !ch_oh_11kv_crossing.isChecked() && !ch_oh_33kv_crossing.isChecked() && !ch_oh_lt_crossing.isChecked() && !ch_oh_tc_crossing.isChecked() && !ch_oh_tm_crossing.isChecked()) {
            //"".equalsIgnoreCase(edt_oh_33kv_vlckts.getText().toString()) && "".equalsIgnoreCase(edt_oh_11kv_vlckts.getText().toString()) && "".equalsIgnoreCase(edt_oh_6_3kv_vlckts.getText().toString()) && "".equalsIgnoreCase(edt_oh_lt_vlckts.getText().toString())){
            Toast.makeText(c, "Select Any One Of Crossing", Toast.LENGTH_SHORT).show();
        } else if (ch_oh_6_3_crossing.isChecked() && "".equalsIgnoreCase(edt_oh_6_3_crossing.getText().toString())) {
            Toast.makeText(c, "Enter Feeder ID of 6.3KV Crossings", Toast.LENGTH_SHORT).show();
        } else if (ch_oh_6_3_crossing.isChecked() && "0".equalsIgnoreCase(edt_oh_6_3_crossing.getText().toString())) {
            Toast.makeText(c, "Enter Feeder ID of 6.3KV Crossings", Toast.LENGTH_SHORT).show();
        } else if (ch_oh_11kv_crossing.isChecked() && "".equalsIgnoreCase(edt_oh_11kv_crossing.getText().toString())) {
            Toast.makeText(c, "Enter Feeder ID of 11kv Crossings", Toast.LENGTH_SHORT).show();
        } else if (ch_oh_11kv_crossing.isChecked() && "0".equalsIgnoreCase(edt_oh_11kv_crossing.getText().toString())) {
            Toast.makeText(c, "Enter Feeder ID of 11kv Crossings", Toast.LENGTH_SHORT).show();
        } else if (ch_oh_33kv_crossing.isChecked() && "".equalsIgnoreCase(edt_oh_33kv_crossing.getText().toString())) {
            Toast.makeText(c, "Enter Feeder ID of 33kv Crossings", Toast.LENGTH_SHORT).show();
        } else if (ch_oh_33kv_crossing.isChecked() && "0".equalsIgnoreCase(edt_oh_33kv_crossing.getText().toString())) {
            Toast.makeText(c, "Enter Feeder ID of 33kv Crossings", Toast.LENGTH_SHORT).show();
        } else if (ch_oh_lt_crossing.isChecked() && "".equalsIgnoreCase(edt_oh_lt_crossing.getText().toString())) {
            Toast.makeText(c, "Enter DTR Code of LT Crossings", Toast.LENGTH_SHORT).show();
        } else if (ch_oh_lt_crossing.isChecked() && "0".equalsIgnoreCase(edt_oh_lt_crossing.getText().toString())) {
            Toast.makeText(c, "Enter DTR Code of LT Crossings", Toast.LENGTH_SHORT).show();
        } else if (ch_oh_tc_crossing.isChecked() && "".equalsIgnoreCase(edt_oh_tc_crossing.getText().toString())) {
            Toast.makeText(c, "Enter No. of Tele Communication Crossings", Toast.LENGTH_SHORT).show();
        } else if (ch_oh_tc_crossing.isChecked() && "0".equalsIgnoreCase(edt_oh_tc_crossing.getText().toString())) {
            Toast.makeText(c, "Enter No. of Tele Communication Crossings", Toast.LENGTH_SHORT).show();
        } else if (ch_oh_tm_crossing.isChecked() && "".equalsIgnoreCase(edt_oh_tm_crossing.getText().toString())) {
            Toast.makeText(c, "Enter No. of Transmission Crossings", Toast.LENGTH_SHORT).show();
        } else if (ch_oh_tm_crossing.isChecked() && "0".equalsIgnoreCase(edt_oh_tm_crossing.getText().toString())) {
            Toast.makeText(c, "Enter No. of Transmission Crossings", Toast.LENGTH_SHORT).show();
        } else if (oh_otherthan_streetlight.equalsIgnoreCase("Yes") && edt_oh_con_services.getText().toString().isEmpty()) {
            Toast.makeText(c, "No. of Services", Toast.LENGTH_SHORT).show();
        } else if (oh_otherthan_streetlight.equalsIgnoreCase("Yes") && edt_oh_con_services.getText().toString().equals("0")) {
            Toast.makeText(c, "No. of Services", Toast.LENGTH_SHORT).show();
        } else if (sp_oh_other_cables.getSelectedItem().toString().equalsIgnoreCase("Yes") && edt_oh_other_cables.getText().toString().isEmpty()) {
            Toast.makeText(c, "No. of Other Cables", Toast.LENGTH_SHORT).show();
        } else if (sp_oh_other_cables.getSelectedItem().toString().equalsIgnoreCase("Yes") && Integer.parseInt(edt_oh_other_cables.getText().toString()) == 0) {
            Toast.makeText(c, "No. of Other Cables should be greater than 0", Toast.LENGTH_SHORT).show();
        } else if (sp_oh_other_cables.getSelectedItem().toString().equalsIgnoreCase("Yes") && Integer.parseInt(edt_oh_other_cables.getText().toString()) > 20) {
            Toast.makeText(c, "No. of Other Cables should not be greater than 20", Toast.LENGTH_SHORT).show();
        } else if ("Yes".equalsIgnoreCase(oh_tapping_point) && "Select".equalsIgnoreCase(oh_noof_spurlines) && edt_oh_tappinglocation.getText().toString().trim().isEmpty()) {
            Toast.makeText(c, "Enter No of Tappings", Toast.LENGTH_SHORT).show();
        } else if ("Yes".equalsIgnoreCase(oh_tapping_point) && edt_oh_tappinglocation.getText().toString().trim().isEmpty()) {
            Toast.makeText(c, "Enter Location Name", Toast.LENGTH_SHORT).show();
        } else if ("Select".equalsIgnoreCase(sp_pole_concrete.getSelectedItem().toString())) {
            Toast.makeText(c, "Select Pole Concrete", Toast.LENGTH_LONG).show();
        } else if (oh_isstreetlight.equalsIgnoreCase("Yes")
                && sp_oh_streetlight_meter_available.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            Toast.makeText(c, "Select Street Meter is Available", Toast.LENGTH_LONG).show();
        } else if (oh_isstreetlight.equalsIgnoreCase("Yes")
                && sp_oh_streetlight_meter_available.getSelectedItem().toString().equalsIgnoreCase("Yes")
                && sp_oh_streetlight_meter_source.getSelectedItem().toString().equalsIgnoreCase("Select")

        ) {
            Toast.makeText(c, "Select Street Light Meter Source", Toast.LENGTH_LONG).show();
        } else {
            if (dtrCode != null & !dtrCode.isEmpty() && !ed_pole_no.getText().toString().isEmpty() && !ed_frompole.getText().toString().isEmpty() && !x.isEmpty() && !y.isEmpty()) {
                if (Integer.parseInt(ed_pole_no.getText().toString()) == Integer.parseInt(ed_frompole.getText().toString())) {
                    Toast.makeText(c, "Pole No and From Pole No Should Not Be Same Contact Admin", Toast.LENGTH_LONG).show();

                } else {
                    List<LTCktsResponseDataVo> ltCktsResponseDataVos = arcGISDatabase.getDTRMasterData(String.format("SELECT * FROM DTR_TABLE WHERE DTR_CODE='%s'", dtrCode));
                    SurveyDataVo surveyDataVo = new SurveyDataVo();
                    surveyDataVo.setDTR_STRUCTURE(dtrCode);
                    surveyDataVo.setLT_CIRCUIT(ltCircuit);
                    surveyDataVo.setPOLE_NO(Integer.parseInt(ed_pole_no.getText().toString()));
                    surveyDataVo.setFROM_POLE(Integer.parseInt(ed_frompole.getText().toString()));
                    surveyDataVo.setLINE_TYPE(lineType);
                    surveyDataVo.setIS_END_POLE(oh_isendpole);
                    surveyDataVo.setLINE_CONFIGURATION(oh_lineconfig);
                    surveyDataVo.setIS_STREETLIGHT_AV(oh_isstreetlight);
                    surveyDataVo.setGPS_ACCURACY(String.valueOf(ArcGISMapActivity.gpsAccuracy));

                    if (oh_isstreetlight.equalsIgnoreCase("Yes")) {

                        surveyDataVo.setSTL_METER(sp_oh_streetlight_meter_available.getSelectedItem().toString());
                        if (sp_oh_streetlight_meter_available.getSelectedItem().toString().equalsIgnoreCase("Yes")) {
                            surveyDataVo.setSTL_SOURCE(sp_oh_streetlight_meter_source.getSelectedItem().toString());
                        } else {
                            surveyDataVo.setSTL_SOURCE("");
                        }
                        surveyDataVo.setSTL_SWITCH(sp_oh_is_streetlight_status.getSelectedItem().toString());
                        // System.out.println("STL_SWITCH : "+sp_oh_is_streetlight_status.getSelectedItem().toString());
                    } else {
                        surveyDataVo.setSTL_SOURCE("");
                        surveyDataVo.setSTL_METER("");
                        surveyDataVo.setSTL_SWITCH("");
                    }

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
                    if (!"Select".equalsIgnoreCase(sp_oh_no_of_33_feeders.getSelectedItem().toString())) {
                        noof_33kvCkts = Integer.parseInt(sp_oh_no_of_33_feeders.getSelectedItem().toString());
                    }

                    if (!"Select".equalsIgnoreCase(sp_oh_no_of_11_feeders.getSelectedItem().toString())) {
                        noof_11kvCkts = Integer.parseInt(sp_oh_no_of_11_feeders.getSelectedItem().toString());
                    }

                    if (!"".equalsIgnoreCase(edt_oh_6_3kv_vlckts.getText().toString())) {
                        noof_6_3kvCkts = Integer.parseInt(edt_oh_6_3kv_vlckts.getText().toString());
                    }

                    if (!"Select".equalsIgnoreCase(sp_oh_no_of_lt_feeders.getSelectedItem().toString())) {
                        noof_ltCkts = Integer.parseInt(sp_oh_no_of_lt_feeders.getSelectedItem().toString());
                    }

                    if ("Yes".equalsIgnoreCase(oh_ismulti_circuit)) {
                        //  int numOfCkts = noof_33kvCkts + noof_11kvCkts + noof_6_3kvCkts + noof_ltCkts;
                        int numOfCkts = noof_33kvCkts + noof_11kvCkts + noof_ltCkts;
                        //  surveyDataVo.setNO_OF_CIRCUITS(String.valueOf(numOfCkts));
                        surveyDataVo.setNO_OF_CIRCUITS(sp_oh_noof_circuits.getSelectedItem().toString());
                        // surveyDataVo.setNO_OF33KV_CKTS(edt_oh_33kv_vlckts.getText().toString());
                        surveyDataVo.setNO_OF33KV_CKTS("" + noof_33kvCkts);
                        //surveyDataVo.setNO_OF11KV_CKTS(edt_oh_11kv_vlckts.getText().toString());
                        surveyDataVo.setNO_OF11KV_CKTS("" + noof_11kvCkts);
                        surveyDataVo.setNO_OF6_3KV_VOLTAGE_CKTS(edt_oh_6_3kv_vlckts.getText().toString());
                        surveyDataVo.setNO_OF_LT_CKTS("" + noof_ltCkts);
                        surveyDataVo.setMULTI_VOLTAGE("Multi Voltage");
                        // System.out.println("MULTI VOLTAGE STRING : "+oh_multi_voltage_same_voltage);

                        if (ch_oh_33kv_vlckts.isChecked()) {
                            if (sp_oh_no_of_33_feeders.getSelectedItem().toString().equals("1")) {

                                if (sp_oh_33_feeder_1.getSelectedItem().toString().equals("Other than List")) {
                                    surveyDataVo.setMC_33KV_FEEDER_ID_1(ed_oh_33_feeder_1.getText().toString().split("~")[0]);
                                } else if (sp_oh_33_feeder_1.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyDataVo.setMC_33KV_FEEDER_ID_1(ed_oh_33_feeder_1_ns.getText().toString().split("~")[0]);
                                } else {
                                    surveyDataVo.setMC_33KV_FEEDER_ID_1(sp_oh_33_feeder_1.getSelectedItem().toString());

                                }

                                if (sp_oh_33_feeder_1.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyDataVo.setMC_33KV_POLE_1("NOT SURVEYED");
                                } else if (sp_oh_33_feeder_1_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                    surveyDataVo.setMC_33KV_POLE_1(ed_oh_33_pole_1.getText().toString());
                                    surveyDataVo.setMC_33KV_NOT_IN_LIST_1("Y");
                                } else {
                                    surveyDataVo.setMC_33KV_POLE_1(sp_oh_33_feeder_1_pole.getSelectedItem().toString());
                                }


                                surveyDataVo.setMC_33KV_FEEDER_ID_2("N");
                                surveyDataVo.setMC_33KV_POLE_2("N");
                                surveyDataVo.setMC_33KV_FEEDER_ID_3("N");
                                surveyDataVo.setMC_33KV_POLE_3("N");

                            }

                            if (sp_oh_no_of_33_feeders.getSelectedItem().toString().equals("2")) {
                                //FEEDER 1
                                if (sp_oh_33_feeder_1.getSelectedItem().toString().equals("Other than List")) {
                                    surveyDataVo.setMC_33KV_FEEDER_ID_1(ed_oh_33_feeder_1.getText().toString().split("~")[0]);
                                } else if (sp_oh_33_feeder_1.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyDataVo.setMC_33KV_FEEDER_ID_1(ed_oh_33_feeder_1_ns.getText().toString().split("~")[0]);
                                } else {
                                    surveyDataVo.setMC_33KV_FEEDER_ID_1(sp_oh_33_feeder_1.getSelectedItem().toString());
                                }

                                if (sp_oh_33_feeder_1.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyDataVo.setMC_33KV_POLE_1("NOT SURVEYED");
                                } else if (sp_oh_33_feeder_1_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                    surveyDataVo.setMC_33KV_POLE_1(ed_oh_33_pole_1.getText().toString());
                                    surveyDataVo.setMC_33KV_NOT_IN_LIST_1("Y");
                                } else {
                                    surveyDataVo.setMC_33KV_POLE_1(sp_oh_33_feeder_1_pole.getSelectedItem().toString());

                                }

                                //FEEDER 2
                                if (sp_oh_33_feeder_2.getSelectedItem().toString().equals("Other than List")) {
                                    surveyDataVo.setMC_33KV_FEEDER_ID_2(ed_oh_33_feeder_2.getText().toString().split("~")[0]);
                                } else if (sp_oh_33_feeder_2.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyDataVo.setMC_33KV_FEEDER_ID_2(ed_oh_33_feeder_2_ns.getText().toString().split("~")[0]);
                                } else {
                                    surveyDataVo.setMC_33KV_FEEDER_ID_2(sp_oh_33_feeder_2.getSelectedItem().toString());
                                }


                                if (sp_oh_33_feeder_2.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyDataVo.setMC_33KV_POLE_2("NOT SURVEYED");
                                } else if (sp_oh_33_feeder_2_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                    surveyDataVo.setMC_33KV_POLE_2(ed_oh_33_pole_2.getText().toString());
                                    surveyDataVo.setMC_33KV_NOT_IN_LIST_2("Y");
                                } else {
                                    surveyDataVo.setMC_33KV_POLE_2(sp_oh_33_feeder_2_pole.getSelectedItem().toString());

                                }

                                surveyDataVo.setMC_33KV_FEEDER_ID_3("N");
                                surveyDataVo.setMC_33KV_POLE_3("N");

                            }


                            if (sp_oh_no_of_33_feeders.getSelectedItem().toString().equals("3")) {

                                //FEEDER 1
                                if (sp_oh_33_feeder_1.getSelectedItem().toString().equals("Other than List")) {
                                    surveyDataVo.setMC_33KV_FEEDER_ID_1(ed_oh_33_feeder_1.getText().toString().split("~")[0]);
                                } else if (sp_oh_33_feeder_1.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyDataVo.setMC_33KV_FEEDER_ID_1(ed_oh_33_feeder_1_ns.getText().toString().split("~")[0]);
                                } else {
                                    surveyDataVo.setMC_33KV_FEEDER_ID_1(sp_oh_33_feeder_1.getSelectedItem().toString());
                                }

                                if (sp_oh_33_feeder_1.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyDataVo.setMC_33KV_POLE_1("NOT SURVEYED");
                                } else if (sp_oh_33_feeder_1_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                    surveyDataVo.setMC_33KV_POLE_1(ed_oh_33_pole_1.getText().toString());
                                    surveyDataVo.setMC_33KV_NOT_IN_LIST_1("Y");
                                } else {
                                    surveyDataVo.setMC_33KV_POLE_1(sp_oh_33_feeder_1_pole.getSelectedItem().toString());
                                }

                                //FEEDER 2
                                if (sp_oh_33_feeder_2.getSelectedItem().toString().equals("Other than List")) {
                                    surveyDataVo.setMC_33KV_FEEDER_ID_2(ed_oh_33_feeder_2.getText().toString().split("~")[0]);
                                } else if (sp_oh_33_feeder_2.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyDataVo.setMC_33KV_FEEDER_ID_2(ed_oh_33_feeder_2_ns.getText().toString().split("~")[0]);
                                } else {
                                    surveyDataVo.setMC_33KV_FEEDER_ID_2(sp_oh_33_feeder_2.getSelectedItem().toString());
                                }


                                if (sp_oh_33_feeder_2.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyDataVo.setMC_33KV_POLE_2("NOT SURVEYED");
                                } else if (sp_oh_33_feeder_2_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                    surveyDataVo.setMC_33KV_POLE_2(ed_oh_33_pole_2.getText().toString());
                                    surveyDataVo.setMC_33KV_NOT_IN_LIST_2("Y");
                                } else {
                                    surveyDataVo.setMC_33KV_POLE_2(sp_oh_33_feeder_2_pole.getSelectedItem().toString());
                                    surveyDataVo.setMC_33KV_NOT_IN_LIST_2("Y");
                                }

                                //FEEDER 3
                                if (sp_oh_33_feeder_3.getSelectedItem().toString().equals("Other than List")) {
                                    surveyDataVo.setMC_33KV_FEEDER_ID_3(ed_oh_33_feeder_3.getText().toString().split("~")[0]);
                                } else if (sp_oh_33_feeder_3.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyDataVo.setMC_33KV_FEEDER_ID_3(ed_oh_33_feeder_3_ns.getText().toString().split("~")[0]);
                                } else {
                                    surveyDataVo.setMC_33KV_FEEDER_ID_3(sp_oh_33_feeder_3.getSelectedItem().toString());
                                }

                                if (sp_oh_33_feeder_3.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyDataVo.setMC_33KV_POLE_3("NOT SURVEYED");
                                } else if (sp_oh_33_feeder_3_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                    surveyDataVo.setMC_33KV_POLE_3(ed_oh_33_pole_3.getText().toString());
                                    surveyDataVo.setMC_33KV_NOT_IN_LIST_3("Y");
                                } else {
                                    surveyDataVo.setMC_33KV_POLE_3(sp_oh_33_feeder_3_pole.getSelectedItem().toString());
                                    surveyDataVo.setMC_33KV_NOT_IN_LIST_3("Y");
                                }

                            }
                        } else {
                            surveyDataVo.setMC_33KV_FEEDER_ID_1("N");
                            surveyDataVo.setMC_33KV_POLE_1("N");
                            surveyDataVo.setMC_33KV_FEEDER_ID_2("N");
                            surveyDataVo.setMC_33KV_POLE_2("N");
                            surveyDataVo.setMC_33KV_FEEDER_ID_3("N");
                            surveyDataVo.setMC_33KV_POLE_3("N");
                        }

                        //11KV FEEDER
                        if (ch_oh_11kv_vlckts.isChecked()) {
                            if (sp_oh_no_of_11_feeders.getSelectedItem().toString().equals("1")) {
                                if (sp_oh_11_feeder_1.getSelectedItem().toString().equals("Other than List")) {
                                    surveyDataVo.setMC_11KV_FEEDER_ID_1(ed_oh_11_feeder_1.getText().toString().split("~")[0]);
                                } else if (sp_oh_11_feeder_1.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyDataVo.setMC_11KV_FEEDER_ID_1(ed_oh_11_feeder_1_ns.getText().toString().split("~")[0]);
                                } else {
                                    surveyDataVo.setMC_11KV_FEEDER_ID_1(sp_oh_11_feeder_1.getSelectedItem().toString());
                                }


                                if (sp_oh_11_feeder_1.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyDataVo.setMC_11KV_POLE_1("NOT SURVEYED");
                                } else if (sp_oh_11_feeder_1_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                    surveyDataVo.setMC_11KV_POLE_1(ed_oh_11_pole_1.getText().toString());
                                    surveyDataVo.setMC_11KV_NOT_IN_LIST_1("Y");
                                } else {
                                    surveyDataVo.setMC_11KV_POLE_1(sp_oh_11_feeder_1_pole.getSelectedItem().toString());
                                }

                                surveyDataVo.setMC_11KV_FEEDER_ID_2("N");
                                surveyDataVo.setMC_11KV_POLE_2("N");
                                surveyDataVo.setMC_11KV_FEEDER_ID_3("N");
                                surveyDataVo.setMC_11KV_POLE_3("N");

                            }

                            if (sp_oh_no_of_11_feeders.getSelectedItem().toString().equals("2")) {
                                //FEEDER 1
                                if (sp_oh_11_feeder_1.getSelectedItem().toString().equals("Other than List")) {
                                    surveyDataVo.setMC_11KV_FEEDER_ID_1(ed_oh_11_feeder_1.getText().toString().split("~")[0]);
                                } else if (sp_oh_11_feeder_1.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyDataVo.setMC_11KV_FEEDER_ID_1(ed_oh_11_feeder_1_ns.getText().toString().split("~")[0]);
                                } else {
                                    surveyDataVo.setMC_11KV_FEEDER_ID_1(sp_oh_11_feeder_1.getSelectedItem().toString());
                                }

                                if (sp_oh_11_feeder_1.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyDataVo.setMC_11KV_POLE_1("NOT SURVEYED");
                                } else if (sp_oh_11_feeder_1_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                    surveyDataVo.setMC_11KV_POLE_1(ed_oh_11_pole_1.getText().toString());
                                    surveyDataVo.setMC_11KV_NOT_IN_LIST_1("Y");
                                } else {
                                    surveyDataVo.setMC_11KV_POLE_1(sp_oh_11_feeder_1_pole.getSelectedItem().toString());
                                }

                                //FEEDER 2
                                if (sp_oh_11_feeder_2.getSelectedItem().toString().equals("Other than List")) {
                                    surveyDataVo.setMC_11KV_FEEDER_ID_2(ed_oh_11_feeder_2.getText().toString().split("~")[0]);
                                } else if (sp_oh_11_feeder_2.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyDataVo.setMC_11KV_FEEDER_ID_2(ed_oh_11_feeder_2_ns.getText().toString().split("~")[0]);
                                } else {
                                    surveyDataVo.setMC_11KV_FEEDER_ID_2(sp_oh_11_feeder_2.getSelectedItem().toString());
                                }

                                if (sp_oh_11_feeder_2.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyDataVo.setMC_11KV_POLE_2("NOT SURVEYED");
                                } else if (sp_oh_11_feeder_2_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                    surveyDataVo.setMC_11KV_POLE_2(ed_oh_11_pole_2.getText().toString());
                                    surveyDataVo.setMC_11KV_NOT_IN_LIST_2("Y");
                                } else {
                                    surveyDataVo.setMC_11KV_POLE_2(sp_oh_11_feeder_2_pole.getSelectedItem().toString());
                                }

                                surveyDataVo.setMC_11KV_FEEDER_ID_3("N");
                                surveyDataVo.setMC_11KV_POLE_3("N");

                            }

                            if (sp_oh_no_of_11_feeders.getSelectedItem().toString().equals("3")) {

                                //FEEDER 1
                                if (sp_oh_11_feeder_1.getSelectedItem().toString().equals("Other than List")) {
                                    surveyDataVo.setMC_11KV_FEEDER_ID_1(ed_oh_11_feeder_1.getText().toString().split("~")[0]);
                                } else if (sp_oh_11_feeder_1.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyDataVo.setMC_11KV_FEEDER_ID_1(ed_oh_11_feeder_1_ns.getText().toString().split("~")[0]);
                                } else {
                                    surveyDataVo.setMC_11KV_FEEDER_ID_1(sp_oh_11_feeder_1.getSelectedItem().toString());
                                }

                                if (sp_oh_11_feeder_1.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyDataVo.setMC_11KV_POLE_1("NOT SURVEYED");
                                } else if (sp_oh_11_feeder_1_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                    surveyDataVo.setMC_11KV_POLE_1(ed_oh_11_pole_1.getText().toString());
                                    surveyDataVo.setMC_11KV_NOT_IN_LIST_1("Y");
                                } else {
                                    surveyDataVo.setMC_11KV_POLE_1(sp_oh_11_feeder_1_pole.getSelectedItem().toString());
                                }

                                //FEEDER 2
                                if (sp_oh_11_feeder_2.getSelectedItem().toString().equals("Other than List")) {
                                    surveyDataVo.setMC_11KV_FEEDER_ID_2(ed_oh_11_feeder_2.getText().toString().split("~")[0]);
                                } else if (sp_oh_11_feeder_2.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyDataVo.setMC_11KV_FEEDER_ID_2(ed_oh_11_feeder_2_ns.getText().toString().split("~")[0]);
                                } else {
                                    surveyDataVo.setMC_11KV_FEEDER_ID_2(sp_oh_11_feeder_2.getSelectedItem().toString());
                                }

                                if (sp_oh_11_feeder_2.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyDataVo.setMC_11KV_POLE_2("NOT SURVEYED");
                                } else if (sp_oh_11_feeder_2_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                    surveyDataVo.setMC_11KV_POLE_2(ed_oh_11_pole_2.getText().toString());
                                    surveyDataVo.setMC_11KV_NOT_IN_LIST_2("Y");
                                } else {
                                    surveyDataVo.setMC_11KV_POLE_2(sp_oh_11_feeder_2_pole.getSelectedItem().toString());
                                }

                                //FEEDER 3
                                if (sp_oh_11_feeder_3.getSelectedItem().toString().equals("Other than List")) {
                                    surveyDataVo.setMC_11KV_FEEDER_ID_3(ed_oh_11_feeder_3.getText().toString().split("~")[0]);
                                } else if (sp_oh_11_feeder_3.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyDataVo.setMC_11KV_FEEDER_ID_3(ed_oh_11_feeder_3_ns.getText().toString().split("~")[0]);
                                } else {
                                    surveyDataVo.setMC_11KV_FEEDER_ID_3(sp_oh_11_feeder_3.getSelectedItem().toString());
                                }

                                if (sp_oh_11_feeder_3.getSelectedItem().toString().equals("Feeder Not Surveyed")) {
                                    surveyDataVo.setMC_11KV_POLE_3("NOT SURVEYED");
                                } else if (sp_oh_11_feeder_3_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                    surveyDataVo.setMC_11KV_POLE_3(ed_oh_11_pole_3.getText().toString());
                                    surveyDataVo.setMC_11KV_NOT_IN_LIST_3("Y");
                                } else {
                                    surveyDataVo.setMC_11KV_POLE_3(sp_oh_11_feeder_3_pole.getSelectedItem().toString());
                                }

                            }
                        } else {
                            surveyDataVo.setMC_11KV_FEEDER_ID_1("N");
                            surveyDataVo.setMC_11KV_POLE_1("N");
                            surveyDataVo.setMC_11KV_FEEDER_ID_2("N");
                            surveyDataVo.setMC_11KV_POLE_2("N");
                            surveyDataVo.setMC_11KV_FEEDER_ID_3("N");
                            surveyDataVo.setMC_11KV_POLE_3("N");

                        }

                        //LT FEEDER
                        if (ch_oh_lt_vlckts.isChecked()) {
                            if (sp_oh_no_of_lt_feeders.getSelectedItem().toString().equals("1")) {
                                if (sp_oh_lt_feeder_1.getSelectedItem().toString().equals("Other than List")) {
                                    surveyDataVo.setMC_LT_FEEDER_ID_1(ed_oh_lt_feeder_1.getText().toString().split("~")[0]);
                                } else if (sp_oh_lt_feeder_1.getSelectedItem().toString().equals("LT CKT Not Surveyed")) {
                                    surveyDataVo.setMC_LT_FEEDER_ID_1(ed_oh_lt_feeder_1_ns.getText().toString().split("~")[0]);
                                } else {
                                    surveyDataVo.setMC_LT_FEEDER_ID_1(sp_oh_lt_feeder_1.getSelectedItem().toString());
                                }

                                if (sp_oh_lt_feeder_1.getSelectedItem().toString().equals("LT CKT Not Surveyed")) {
                                    surveyDataVo.setMC_LT_POLE_1("NOT SURVEYED");
                                } else if (sp_oh_lt_feeder_1_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                    surveyDataVo.setMC_LT_POLE_1(ed_oh_lt_pole_1.getText().toString());
                                    surveyDataVo.setMC_LT_NOT_IN_LIST_1("Y");
                                } else {
                                    surveyDataVo.setMC_LT_POLE_1(sp_oh_lt_feeder_1_pole.getSelectedItem().toString());
                                }

                                surveyDataVo.setMC_LT_FEEDER_ID_2("N");
                                surveyDataVo.setMC_LT_POLE_2("N");
                                surveyDataVo.setMC_LT_FEEDER_ID_3("N");
                                surveyDataVo.setMC_LT_POLE_3("N");

                            }

                            if (sp_oh_no_of_lt_feeders.getSelectedItem().toString().equals("2")) {
                                //FEEDER 1
                                if (sp_oh_lt_feeder_1.getSelectedItem().toString().equals("Other than List")) {
                                    surveyDataVo.setMC_LT_FEEDER_ID_1(ed_oh_lt_feeder_1.getText().toString().split("~")[0]);
                                } else if (sp_oh_lt_feeder_1.getSelectedItem().toString().equals("LT CKT Not Surveyed")) {
                                    surveyDataVo.setMC_LT_FEEDER_ID_1(ed_oh_lt_feeder_1_ns.getText().toString().split("~")[0]);
                                } else {
                                    surveyDataVo.setMC_LT_FEEDER_ID_1(sp_oh_lt_feeder_1.getSelectedItem().toString());
                                }
                                if (sp_oh_lt_feeder_1.getSelectedItem().toString().equals("LT CKT Not Surveyed")) {
                                    surveyDataVo.setMC_LT_POLE_1("NOT SURVEYED");
                                } else if (sp_oh_lt_feeder_1_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                    surveyDataVo.setMC_LT_POLE_1(ed_oh_lt_pole_1.getText().toString());
                                    surveyDataVo.setMC_LT_NOT_IN_LIST_1("Y");
                                } else {
                                    surveyDataVo.setMC_LT_POLE_1(sp_oh_lt_feeder_1_pole.getSelectedItem().toString());
                                }

                                //FEEDER 2
                                if (sp_oh_lt_feeder_2.getSelectedItem().toString().equals("Other than List")) {
                                    surveyDataVo.setMC_LT_FEEDER_ID_2(ed_oh_lt_feeder_2.getText().toString().split("~")[0]);
                                } else if (sp_oh_lt_feeder_2.getSelectedItem().toString().equals("LT CKT Not Surveyed")) {
                                    surveyDataVo.setMC_LT_FEEDER_ID_2(ed_oh_lt_feeder_2_ns.getText().toString().split("~")[0]);
                                } else {
                                    surveyDataVo.setMC_LT_FEEDER_ID_2(sp_oh_lt_feeder_2.getSelectedItem().toString());
                                }

                                if (sp_oh_lt_feeder_2.getSelectedItem().toString().equals("LT CKT Not Surveyed")) {
                                    surveyDataVo.setMC_LT_POLE_2("NOT SURVEYED");
                                } else if (sp_oh_lt_feeder_2_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                    surveyDataVo.setMC_LT_POLE_2(ed_oh_lt_pole_2.getText().toString());
                                    surveyDataVo.setMC_LT_NOT_IN_LIST_2("Y");
                                } else {
                                    surveyDataVo.setMC_LT_POLE_2(sp_oh_lt_feeder_2_pole.getSelectedItem().toString());
                                }

                                surveyDataVo.setMC_LT_FEEDER_ID_3("N");
                                surveyDataVo.setMC_LT_POLE_3("N");

                            }

                            if (sp_oh_no_of_lt_feeders.getSelectedItem().toString().equals("3")) {

                                //FEEDER 1
                                if (sp_oh_lt_feeder_1.getSelectedItem().toString().equals("Other than List")) {
                                    surveyDataVo.setMC_LT_FEEDER_ID_1(ed_oh_lt_feeder_1.getText().toString().split("~")[0]);
                                } else if (sp_oh_lt_feeder_1.getSelectedItem().toString().equals("LT CKT Not Surveyed")) {
                                    surveyDataVo.setMC_LT_FEEDER_ID_1(ed_oh_lt_feeder_1_ns.getText().toString().split("~")[0]);
                                } else {
                                    surveyDataVo.setMC_LT_FEEDER_ID_1(sp_oh_lt_feeder_1.getSelectedItem().toString());
                                }
                                if (sp_oh_lt_feeder_1.getSelectedItem().toString().equals("LT CKT Not Surveyed")) {
                                    surveyDataVo.setMC_LT_POLE_1("NOT SURVEYED");
                                } else if (sp_oh_lt_feeder_1_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                    surveyDataVo.setMC_LT_POLE_1(ed_oh_lt_pole_1.getText().toString());
                                    surveyDataVo.setMC_LT_NOT_IN_LIST_1("Y");
                                } else {
                                    surveyDataVo.setMC_LT_POLE_1(sp_oh_lt_feeder_1_pole.getSelectedItem().toString());
                                }

                                //FEEDER 2
                                if (sp_oh_lt_feeder_2.getSelectedItem().toString().equals("Other than List")) {
                                    surveyDataVo.setMC_LT_FEEDER_ID_2(ed_oh_lt_feeder_2.getText().toString().split("~")[0]);
                                } else if (sp_oh_lt_feeder_2.getSelectedItem().toString().equals("LT CKT Not Surveyed")) {
                                    surveyDataVo.setMC_LT_FEEDER_ID_2(ed_oh_lt_feeder_2_ns.getText().toString().split("~")[0]);
                                } else {
                                    surveyDataVo.setMC_LT_FEEDER_ID_2(sp_oh_lt_feeder_2.getSelectedItem().toString());
                                }


                                if (sp_oh_lt_feeder_2.getSelectedItem().toString().equals("LT CKT Not Surveyed")) {
                                    surveyDataVo.setMC_LT_POLE_2("NOT SURVEYED");
                                } else if (sp_oh_lt_feeder_2_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                    surveyDataVo.setMC_LT_POLE_2(ed_oh_lt_pole_2.getText().toString());
                                    surveyDataVo.setMC_LT_NOT_IN_LIST_2("Y");
                                } else {
                                    surveyDataVo.setMC_LT_POLE_2(sp_oh_lt_feeder_2_pole.getSelectedItem().toString());
                                }

                                //FEEDER 3
                                if (sp_oh_lt_feeder_3.getSelectedItem().toString().equals("Other than List")) {
                                    surveyDataVo.setMC_LT_FEEDER_ID_3(ed_oh_lt_feeder_3.getText().toString().split("~")[0]);
                                } else if (sp_oh_lt_feeder_3.getSelectedItem().toString().equals("LT CKT Not Surveyed")) {
                                    surveyDataVo.setMC_LT_FEEDER_ID_3(ed_oh_lt_feeder_3_ns.getText().toString().split("~")[0]);
                                } else {
                                    surveyDataVo.setMC_LT_FEEDER_ID_3(sp_oh_lt_feeder_3.getSelectedItem().toString());
                                }

                                if (sp_oh_lt_feeder_3.getSelectedItem().toString().equals("LT CKT Not Surveyed")) {
                                    surveyDataVo.setMC_LT_POLE_3("NOT SURVEYED");
                                } else if (sp_oh_lt_feeder_3_pole.getSelectedItem().toString().equalsIgnoreCase("Not in list(Enter Pole number)")) {
                                    surveyDataVo.setMC_LT_POLE_3(ed_oh_lt_pole_3.getText().toString());
                                    surveyDataVo.setMC_LT_NOT_IN_LIST_3("Y");
                                } else {
                                    surveyDataVo.setMC_LT_POLE_3(sp_oh_lt_feeder_3_pole.getSelectedItem().toString());
                                }

                            }
                        } else {
                            surveyDataVo.setMC_LT_FEEDER_ID_1("N");
                            surveyDataVo.setMC_LT_POLE_1("N");
                            surveyDataVo.setMC_LT_FEEDER_ID_2("N");
                            surveyDataVo.setMC_LT_POLE_2("N");
                            surveyDataVo.setMC_LT_FEEDER_ID_3("N");
                            surveyDataVo.setMC_LT_POLE_3("N");
                        }


                    } else if ("Yes".equalsIgnoreCase(oh_ismulti_circuit) && "Same Voltage".equalsIgnoreCase(oh_multi_voltage_same_voltage)) {
                        surveyDataVo.setNO_OF_CIRCUITS(oh_noof_circuits);
                        surveyDataVo.setMULTI_VOLTAGE("Same Voltage");

                        surveyDataVo.setNO_OF33KV_CKTS("");
                        surveyDataVo.setNO_OF11KV_CKTS("");
                        surveyDataVo.setNO_OF6_3KV_VOLTAGE_CKTS("");
                        surveyDataVo.setNO_OF_LT_CKTS("");

                        surveyDataVo.setMC_33KV_FEEDER_ID_1("N");
                        surveyDataVo.setMC_33KV_POLE_1("N");
                        surveyDataVo.setMC_33KV_FEEDER_ID_2("N");
                        surveyDataVo.setMC_33KV_POLE_2("N");
                        surveyDataVo.setMC_33KV_FEEDER_ID_3("N");
                        surveyDataVo.setMC_33KV_POLE_3("N");

                        surveyDataVo.setMC_11KV_FEEDER_ID_1("N");
                        surveyDataVo.setMC_11KV_POLE_1("N");
                        surveyDataVo.setMC_11KV_FEEDER_ID_2("N");
                        surveyDataVo.setMC_11KV_POLE_2("N");
                        surveyDataVo.setMC_11KV_FEEDER_ID_3("N");
                        surveyDataVo.setMC_11KV_POLE_3("N");

                        surveyDataVo.setMC_LT_FEEDER_ID_1("N");
                        surveyDataVo.setMC_LT_POLE_1("N");
                        surveyDataVo.setMC_LT_FEEDER_ID_2("N");
                        surveyDataVo.setMC_LT_POLE_2("N");
                        surveyDataVo.setMC_LT_FEEDER_ID_3("N");
                        surveyDataVo.setMC_LT_POLE_3("N");

                    } else {
                        surveyDataVo.setNO_OF_CIRCUITS("");
                        surveyDataVo.setMULTI_VOLTAGE("");

                        surveyDataVo.setNO_OF33KV_CKTS("");
                        surveyDataVo.setNO_OF11KV_CKTS("");
                        surveyDataVo.setNO_OF6_3KV_VOLTAGE_CKTS("");
                        surveyDataVo.setNO_OF_LT_CKTS("");

                        surveyDataVo.setMC_33KV_FEEDER_ID_1("N");
                        surveyDataVo.setMC_33KV_POLE_1("N");
                        surveyDataVo.setMC_33KV_FEEDER_ID_2("N");
                        surveyDataVo.setMC_33KV_POLE_2("N");
                        surveyDataVo.setMC_33KV_FEEDER_ID_3("N");
                        surveyDataVo.setMC_33KV_POLE_3("N");

                        surveyDataVo.setMC_11KV_FEEDER_ID_1("N");
                        surveyDataVo.setMC_11KV_POLE_1("N");
                        surveyDataVo.setMC_11KV_FEEDER_ID_2("N");
                        surveyDataVo.setMC_11KV_POLE_2("N");
                        surveyDataVo.setMC_11KV_FEEDER_ID_3("N");
                        surveyDataVo.setMC_11KV_POLE_3("N");

                        surveyDataVo.setMC_LT_FEEDER_ID_1("N");
                        surveyDataVo.setMC_LT_POLE_1("N");
                        surveyDataVo.setMC_LT_FEEDER_ID_2("N");
                        surveyDataVo.setMC_LT_POLE_2("N");
                        surveyDataVo.setMC_LT_FEEDER_ID_3("N");
                        surveyDataVo.setMC_LT_POLE_3("N");
                    }


                /*    surveyDataVo.setNO_OF33KV_CKTS(edt_oh_33kv_vlckts.getText().toString());
                    surveyDataVo.setNO_OF11KV_CKTS(edt_oh_11kv_vlckts.getText().toString());
                    surveyDataVo.setNO_OF6_3KV_VOLTAGE_CKTS(edt_oh_6_3kv_vlckts.getText().toString());
                    surveyDataVo.setNO_OF_LT_CKTS(edt_oh_lt_vlckts.getText().toString());*/
                    surveyDataVo.setPOLE_PAINTING_CODE(edt_oh_pole_paintingcode.getText().toString());
                    surveyDataVo.setREMARKS(edt_oh_remarks.getText().toString());
                    surveyDataVo.setLAT(x);
                    surveyDataVo.setLAN(y);
                    surveyDataVo.setFEEDER_ID(ltCktsResponseDataVos.get(0).getFEEDER_ID());

                    surveyDataVo.setDTR_METER_AVILABLE(oh_dtr_pole_dtr_meter_avilable);
                    surveyDataVo.setDTR_NO_OF_METERS(oh_dtr_pole_no_of_meters);


                    if ("YES".equalsIgnoreCase(crossing)) {
                        surveyDataVo.setIS_CROSSINGS_AV("YES");
                        surveyDataVo.setNOOF_6_3KV_CROSSINGS(edt_oh_6_3_crossing.getText().toString().split("~")[0]);
                        surveyDataVo.setNOOF_11KV_CROSSINGS(edt_oh_11kv_crossing.getText().toString().split("~")[0]);
                        surveyDataVo.setNOOF_33KV_CROSSINGS(edt_oh_33kv_crossing.getText().toString().split("~")[0]);
                        surveyDataVo.setNOOF_LT_CROSSINGS(edt_oh_lt_crossing.getText().toString().split("~")[0]);
                        surveyDataVo.setNOOF_TC_CROSSINGS(edt_oh_tc_crossing.getText().toString());
                        surveyDataVo.setNOOF_TM_CROSSINGS(edt_oh_tm_crossing.getText().toString());
                    } else {
                        surveyDataVo.setIS_CROSSINGS_AV("NO");
                        surveyDataVo.setNOOF_6_3KV_CROSSINGS("");
                        surveyDataVo.setNOOF_11KV_CROSSINGS("");
                        surveyDataVo.setNOOF_33KV_CROSSINGS("");
                        surveyDataVo.setNOOF_LT_CROSSINGS("");
                        surveyDataVo.setNOOF_TC_CROSSINGS("");
                        surveyDataVo.setNOOF_TM_CROSSINGS("");
                    }


                    surveyDataVo.setSURVEY_FLAG("N");
                    // System.out.println("DEFECT SELECTED : "+defect_selected);
                    //NEW
                    if (defect_selected.equalsIgnoreCase("Yes")) {
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
                    } else {
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

                    //   System.out.println("NUMBER SCS : "+sp_oh_otherthan_streetlight.getSelectedItem().toString());

                    if (sp_oh_otherthan_streetlight.getSelectedItem().toString().equalsIgnoreCase("Yes")) {
                        surveyDataVo.setNUM_SCS(edt_oh_con_services.getText().toString());

                    } else {
                        surveyDataVo.setNUM_SCS("");
                    }

                    //IMAGES START
                    if (defect_selected.equalsIgnoreCase("Yes")) {
                        surveyDataVo.setIMAGE_1(image_1_clicked ? image_1_base64 : "N");
                        surveyDataVo.setIMAGE_2(image_2_clicked ? image_2_base64 : "N");
                        surveyDataVo.setIMAGE_3(image_3_clicked ? image_3_base64 : "N");
                    } else {
                        surveyDataVo.setIMAGE_1("N");
                        surveyDataVo.setIMAGE_2("N");
                        surveyDataVo.setIMAGE_3("N");
                    }
                    //IMAGES END

                    //OTHER CABLES
                    if (sp_oh_other_cables.getSelectedItem().toString().equalsIgnoreCase("Yes")) {
                        surveyDataVo.setNUM_OTHER_CABLES(edt_oh_other_cables.getText().toString());
                    } else {
                        surveyDataVo.setNUM_OTHER_CABLES("");
                    }

                    surveyDataVo.setPOLE_CONCRETE(sp_pole_concrete.getSelectedItem().toString());

                    //JHC PHASE 1
                    if (LoginActivity.isJHC) {
                        if (sp_pole_survey_type.getSelectedItem().toString().equalsIgnoreCase("HC Phase 1")) {
                            surveyDataVo.setPOLE_SURVEY_TYPE("JHC");
                        } else {
                            surveyDataVo.setPOLE_SURVEY_TYPE(sp_pole_survey_type.getSelectedItem().toString());
                        }

                        if (sp_line_survey_type.getSelectedItem().toString().equalsIgnoreCase("HC Phase 1")) {
                            surveyDataVo.setLINE_SURVEY_TYPE("JHC");
                        } else {
                            surveyDataVo.setLINE_SURVEY_TYPE(sp_line_survey_type.getSelectedItem().toString());
                        }
                    }


                    //CURRENT LOCATION
                    surveyDataVo.setCURRENT_LAT("" + CURRENT_LAT);
                    surveyDataVo.setCURRENT_LONG("" + CURRENT_LONG);


                    //CHECK MULTI CIRCUIT DUPLICATE START

                    if ("Yes".equalsIgnoreCase(oh_ismulti_circuit)) {
                        List<SurveyDataVo> checkList = arcGISDatabase.checkMultiCktForLT();
                        if (!checkList.isEmpty()) {
                            for (int i = 0; i < checkList.size(); i++) {
                                //33KV
                                if (ch_oh_33kv_vlckts.isChecked()) {
                                    //FEEDER 1
                                    if (!surveyDataVo.getMC_33KV_FEEDER_ID_1().equals("N")) {
                                        if (!checkList.get(i).getMC_33KV_POLE_1().equals("NOT SURVEYED") && checkList.get(i).getMC_33KV_FEEDER_ID_1().equals(surveyDataVo.getMC_33KV_FEEDER_ID_1()) && checkList.get(i).getMC_33KV_POLE_1().equals(surveyDataVo.getMC_33KV_POLE_1())) {
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (!checkList.get(i).getMC_33KV_POLE_2().equals("NOT SURVEYED") && checkList.get(i).getMC_33KV_FEEDER_ID_2().equals(surveyDataVo.getMC_33KV_FEEDER_ID_1()) && checkList.get(i).getMC_33KV_POLE_2().equals(surveyDataVo.getMC_33KV_POLE_1())) {
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (!checkList.get(i).getMC_33KV_POLE_3().equals("NOT SURVEYED") && checkList.get(i).getMC_33KV_FEEDER_ID_3().equals(surveyDataVo.getMC_33KV_FEEDER_ID_1()) && checkList.get(i).getMC_33KV_POLE_3().equals(surveyDataVo.getMC_33KV_POLE_1())) {
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }

                                    //FEEDER 2
                                    if (!surveyDataVo.getMC_33KV_FEEDER_ID_2().equals("N")) {
                                        if (!checkList.get(i).getMC_33KV_POLE_1().equals("NOT SURVEYED") && checkList.get(i).getMC_33KV_FEEDER_ID_1().equals(surveyDataVo.getMC_33KV_FEEDER_ID_2()) && checkList.get(i).getMC_33KV_POLE_1().equals(surveyDataVo.getMC_33KV_POLE_2())) {
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (!checkList.get(i).getMC_33KV_POLE_2().equals("NOT SURVEYED") && checkList.get(i).getMC_33KV_FEEDER_ID_2().equals(surveyDataVo.getMC_33KV_FEEDER_ID_2()) && checkList.get(i).getMC_33KV_POLE_2().equals(surveyDataVo.getMC_33KV_POLE_2())) {
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (!checkList.get(i).getMC_33KV_POLE_3().equals("NOT SURVEYED") && checkList.get(i).getMC_33KV_FEEDER_ID_3().equals(surveyDataVo.getMC_33KV_FEEDER_ID_2()) && checkList.get(i).getMC_33KV_POLE_3().equals(surveyDataVo.getMC_33KV_POLE_2())) {
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }

                                    //FEEDER 3
                                    if (!surveyDataVo.getMC_33KV_FEEDER_ID_3().equals("N")) {
                                        if (!checkList.get(i).getMC_33KV_POLE_1().equals("NOT SURVEYED") && checkList.get(i).getMC_33KV_FEEDER_ID_1().equals(surveyDataVo.getMC_33KV_FEEDER_ID_3()) && checkList.get(i).getMC_33KV_POLE_1().equals(surveyDataVo.getMC_33KV_POLE_3())) {
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (!checkList.get(i).getMC_33KV_POLE_2().equals("NOT SURVEYED") && checkList.get(i).getMC_33KV_FEEDER_ID_2().equals(surveyDataVo.getMC_33KV_FEEDER_ID_3()) && checkList.get(i).getMC_33KV_POLE_2().equals(surveyDataVo.getMC_33KV_POLE_3())) {
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (!checkList.get(i).getMC_33KV_POLE_3().equals("NOT SURVEYED") && checkList.get(i).getMC_33KV_FEEDER_ID_3().equals(surveyDataVo.getMC_33KV_FEEDER_ID_3()) && checkList.get(i).getMC_33KV_POLE_3().equals(surveyDataVo.getMC_33KV_POLE_3())) {
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }

                                }

                                //11KV
                                if (ch_oh_11kv_vlckts.isChecked()) {
                                    //FEEDER 1
                                    if (!surveyDataVo.getMC_11KV_FEEDER_ID_1().equals("N")) {
                                        if (!checkList.get(i).getMC_11KV_POLE_1().equals("NOT SURVEYED") && checkList.get(i).getMC_11KV_FEEDER_ID_1().equals(surveyDataVo.getMC_11KV_FEEDER_ID_1()) && checkList.get(i).getMC_11KV_POLE_1().equals(surveyDataVo.getMC_11KV_POLE_1())) {
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (!checkList.get(i).getMC_11KV_POLE_2().equals("NOT SURVEYED") && checkList.get(i).getMC_11KV_FEEDER_ID_2().equals(surveyDataVo.getMC_11KV_FEEDER_ID_1()) && checkList.get(i).getMC_11KV_POLE_2().equals(surveyDataVo.getMC_11KV_POLE_1())) {
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (!checkList.get(i).getMC_11KV_POLE_3().equals("NOT SURVEYED") && checkList.get(i).getMC_11KV_FEEDER_ID_3().equals(surveyDataVo.getMC_11KV_FEEDER_ID_1()) && checkList.get(i).getMC_11KV_POLE_3().equals(surveyDataVo.getMC_11KV_POLE_1())) {
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }

                                    //FEEDER 2
                                    if (!surveyDataVo.getMC_11KV_FEEDER_ID_2().equals("N")) {
                                        if (!checkList.get(i).getMC_11KV_POLE_1().equals("NOT SURVEYED") && checkList.get(i).getMC_11KV_FEEDER_ID_1().equals(surveyDataVo.getMC_11KV_FEEDER_ID_2()) && checkList.get(i).getMC_11KV_POLE_1().equals(surveyDataVo.getMC_11KV_POLE_2())) {
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (!checkList.get(i).getMC_11KV_POLE_2().equals("NOT SURVEYED") && checkList.get(i).getMC_11KV_FEEDER_ID_2().equals(surveyDataVo.getMC_11KV_FEEDER_ID_2()) && checkList.get(i).getMC_11KV_POLE_2().equals(surveyDataVo.getMC_11KV_POLE_2())) {
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (!checkList.get(i).getMC_11KV_POLE_3().equals("NOT SURVEYED") && checkList.get(i).getMC_11KV_FEEDER_ID_3().equals(surveyDataVo.getMC_11KV_FEEDER_ID_2()) && checkList.get(i).getMC_11KV_POLE_3().equals(surveyDataVo.getMC_11KV_POLE_2())) {
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }

                                    //FEEDER 3
                                    if (!surveyDataVo.getMC_11KV_FEEDER_ID_3().equals("N")) {
                                        if (!checkList.get(i).getMC_11KV_POLE_1().equals("NOT SURVEYED") && checkList.get(i).getMC_11KV_FEEDER_ID_1().equals(surveyDataVo.getMC_11KV_FEEDER_ID_3()) && checkList.get(i).getMC_11KV_POLE_1().equals(surveyDataVo.getMC_11KV_POLE_3())) {
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (!checkList.get(i).getMC_11KV_POLE_2().equals("NOT SURVEYED") && checkList.get(i).getMC_11KV_FEEDER_ID_2().equals(surveyDataVo.getMC_11KV_FEEDER_ID_3()) && checkList.get(i).getMC_11KV_POLE_2().equals(surveyDataVo.getMC_11KV_POLE_3())) {
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (!checkList.get(i).getMC_11KV_POLE_3().equals("NOT SURVEYED") && checkList.get(i).getMC_11KV_FEEDER_ID_3().equals(surveyDataVo.getMC_11KV_FEEDER_ID_3()) && checkList.get(i).getMC_11KV_POLE_3().equals(surveyDataVo.getMC_11KV_POLE_3())) {
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }

                                }

                                //LT
                                if (ch_oh_lt_vlckts.isChecked()) {
                                    //FEEDER 1
                                    if (!surveyDataVo.getMC_LT_FEEDER_ID_1().equals("N")) {
                                        if (!checkList.get(i).getMC_LT_POLE_1().equals("NOT SURVEYED") && checkList.get(i).getMC_LT_FEEDER_ID_1().equals(surveyDataVo.getMC_LT_FEEDER_ID_1()) && checkList.get(i).getMC_LT_POLE_1().equals(surveyDataVo.getMC_LT_POLE_1())) {
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (!checkList.get(i).getMC_LT_POLE_2().equals("NOT SURVEYED") && checkList.get(i).getMC_LT_FEEDER_ID_2().equals(surveyDataVo.getMC_LT_FEEDER_ID_1()) && checkList.get(i).getMC_LT_POLE_2().equals(surveyDataVo.getMC_LT_POLE_1())) {
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (!checkList.get(i).getMC_LT_POLE_3().equals("NOT SURVEYED") && checkList.get(i).getMC_LT_FEEDER_ID_3().equals(surveyDataVo.getMC_LT_FEEDER_ID_1()) && checkList.get(i).getMC_LT_POLE_3().equals(surveyDataVo.getMC_LT_POLE_1())) {
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }

                                    //FEEDER 2
                                    if (!surveyDataVo.getMC_LT_FEEDER_ID_2().equals("N")) {
                                        if (!checkList.get(i).getMC_LT_POLE_1().equals("NOT SURVEYED") && checkList.get(i).getMC_LT_FEEDER_ID_1().equals(surveyDataVo.getMC_LT_FEEDER_ID_2()) && checkList.get(i).getMC_LT_POLE_1().equals(surveyDataVo.getMC_LT_POLE_2())) {
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (!checkList.get(i).getMC_LT_POLE_2().equals("NOT SURVEYED") && checkList.get(i).getMC_LT_FEEDER_ID_2().equals(surveyDataVo.getMC_LT_FEEDER_ID_2()) && checkList.get(i).getMC_LT_POLE_2().equals(surveyDataVo.getMC_LT_POLE_2())) {
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (!checkList.get(i).getMC_LT_POLE_3().equals("NOT SURVEYED") && checkList.get(i).getMC_LT_FEEDER_ID_3().equals(surveyDataVo.getMC_LT_FEEDER_ID_2()) && checkList.get(i).getMC_LT_POLE_3().equals(surveyDataVo.getMC_LT_POLE_2())) {
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }

                                    //FEEDER 3
                                    if (!surveyDataVo.getMC_LT_FEEDER_ID_3().equals("N")) {
                                        if (!checkList.get(i).getMC_LT_POLE_1().equals("NOT SURVEYED") && checkList.get(i).getMC_LT_FEEDER_ID_1().equals(surveyDataVo.getMC_LT_FEEDER_ID_3()) && checkList.get(i).getMC_LT_POLE_1().equals(surveyDataVo.getMC_LT_POLE_3())) {
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (!checkList.get(i).getMC_LT_POLE_2().equals("NOT SURVEYED") && checkList.get(i).getMC_LT_FEEDER_ID_2().equals(surveyDataVo.getMC_LT_FEEDER_ID_3()) && checkList.get(i).getMC_LT_POLE_2().equals(surveyDataVo.getMC_LT_POLE_3())) {
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (!checkList.get(i).getMC_LT_POLE_3().equals("NOT SURVEYED") && checkList.get(i).getMC_LT_FEEDER_ID_3().equals(surveyDataVo.getMC_LT_FEEDER_ID_3()) && checkList.get(i).getMC_LT_POLE_3().equals(surveyDataVo.getMC_LT_POLE_3())) {
                                            Toast.makeText(c, "Duplicate Multi Circuit Pole Number", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }

                                }


                            }
                        }
                    }

                    //CHECK MULTI CIRCUIT DUPLICATE END


                    boolean statusSurvey = arcGISDatabase.addSurveyData(surveyDataVo);
                    boolean statusImage = arcGISDatabase.addLTImageData(surveyDataVo);
                    boolean statusRepeat = arcGISDatabase.addSurveyDatarepeat(surveyDataVo);

                    //  System.out.println("--->statusSurvey : "+statusSurvey);
                    //  System.out.println("--->statusImage : "+statusImage);
                    //  System.out.println("--->statusRepeat : "+statusRepeat);


                    image_1_clicked = false;
                    image_2_clicked = false;
                    image_3_clicked = false;
                    repeatPoleClicked = false;

                    Toast.makeText(c, "Pole location captured successfully..", Toast.LENGTH_SHORT).show();
                    dismiss();
                    callout.dismiss();
                    graphicsOverlay.getGraphics().remove(graphic);

                    if ("Yes".equalsIgnoreCase(oh_isendpole)) {
                        Intent intent = new Intent(c, HomeActivity.class);
                        c.startActivity(intent);
                        c.finish();
                    }
                }
            }

        }
    }

    private boolean validate33KVSamePole() {
        if (sp_oh_ismulti_circuit.getSelectedItem().toString().equals("Yes")) {
            if (ch_oh_33kv_vlckts.isChecked()) {
                if (!"Select".equalsIgnoreCase(sp_oh_no_of_33_feeders.getSelectedItem().toString())) {
                    if (sp_oh_no_of_33_feeders.getSelectedItem().toString().equals("1")) {
                        String enteredFeederID_33_1 = sp_oh_33_feeder_1.getSelectedItem().toString();
                        String enteredPole_33_1 = sp_oh_33_feeder_1_pole.getSelectedItem().toString();

                    }
                }
            }
        }

        return false;

    }

    private boolean validateNoOfCircuits() {

        if (sp_oh_ismulti_circuit.getSelectedItem().toString().equalsIgnoreCase("Yes")) {
            if (sp_oh_multi_voltage_same_voltage.getSelectedItem().toString().equals("Multi Voltage")) {
                int selectedCkts = Integer.parseInt(sp_oh_noof_circuits.getSelectedItem().toString()) - 1;


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


        }


        return false;
    }

    private boolean validate33KVVoltageCircuit() {

        if (sp_oh_ismulti_circuit.getSelectedItem().toString().equals("Yes")) {
            if (ch_oh_33kv_vlckts.isChecked()) {
                if ("1".equalsIgnoreCase(sp_oh_no_of_33_feeders.getSelectedItem().toString())
                        || "2".equalsIgnoreCase(sp_oh_no_of_33_feeders.getSelectedItem().toString())
                        || "3".equalsIgnoreCase(sp_oh_no_of_33_feeders.getSelectedItem().toString())
                ) {
                    if (sp_oh_33_feeder_1.getSelectedItem().toString().equals("Select")) {
                        Toast.makeText(c, "Select Feeder 1 of 33kv Voltage Ckts", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    if (sp_oh_33_feeder_1.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed")) {
                        if (ed_oh_33_feeder_1_ns.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Not Surveyed Feeder 1 of 33kv Voltage Ckts", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }

                    if (sp_oh_33_feeder_1.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                        if (ed_oh_33_feeder_1.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Feeder 1 of 33kv Voltage Ckts", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(c, "Select Feeder 2 of 33kv Voltage Ckts", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    if (sp_oh_33_feeder_2.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed")) {
                        if (ed_oh_33_feeder_2_ns.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Not Surveyed Feeder 2 of 33kv Voltage Ckts", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }

                    if (sp_oh_33_feeder_2.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                        if (ed_oh_33_feeder_2.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Feeder 2 of 33kv Voltage Ckts", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(c, "Select Feeder 3 of 33kv Voltage Ckts", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    if (sp_oh_33_feeder_3.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed")) {
                        if (ed_oh_33_feeder_3_ns.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Not Surveyed Feeder 3 of 33kv Voltage Ckts", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }

                    if (sp_oh_33_feeder_3.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                        if (ed_oh_33_feeder_3.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Feeder 3 of 33kv Voltage Ckts", Toast.LENGTH_SHORT).show();
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

        if (sp_oh_ismulti_circuit.getSelectedItem().toString().equals("Yes")) {
            if (ch_oh_11kv_vlckts.isChecked()) {
                if ("1".equalsIgnoreCase(sp_oh_no_of_11_feeders.getSelectedItem().toString())
                        || "2".equalsIgnoreCase(sp_oh_no_of_11_feeders.getSelectedItem().toString())
                        || "3".equalsIgnoreCase(sp_oh_no_of_11_feeders.getSelectedItem().toString())
                ) {
                    if (sp_oh_11_feeder_1.getSelectedItem().toString().equals("Select")) {
                        Toast.makeText(c, "Select Feeder 1 of 11kv Voltage Ckts", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    if (sp_oh_11_feeder_1.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed")) {
                        if (ed_oh_11_feeder_1_ns.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Not Surveyed Feeder 1 of 11kv Voltage Ckts", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }

                    if (sp_oh_11_feeder_1.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                        if (ed_oh_11_feeder_1.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Feeder 1 of 11kv Voltage Ckts", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(c, "Select Feeder 2 of 11kv Voltage Ckts", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    if (sp_oh_11_feeder_2.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed")) {
                        if (ed_oh_11_feeder_2_ns.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Not Surveyed Feeder 2 of 11kv Voltage Ckts", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }

                    if (sp_oh_11_feeder_2.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                        if (ed_oh_11_feeder_2.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Feeder 2 of 11kv Voltage Ckts", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(c, "Select Feeder 3 of 11kv Voltage Ckts", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    if (sp_oh_11_feeder_3.getSelectedItem().toString().equalsIgnoreCase("Feeder Not Surveyed")) {
                        if (ed_oh_11_feeder_3_ns.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Not Surveyed Feeder 3 of 11kv Voltage Ckts", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }

                    if (sp_oh_11_feeder_3.getSelectedItem().toString().equalsIgnoreCase("Other than List")) {
                        if (ed_oh_11_feeder_3.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Enter Feeder 3 of 11kv Voltage Ckts", Toast.LENGTH_SHORT).show();
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

        if (sp_oh_ismulti_circuit.getSelectedItem().toString().equals("Yes")) {
            if (ch_oh_lt_vlckts.isChecked()) {
                if ("1".equalsIgnoreCase(sp_oh_no_of_lt_feeders.getSelectedItem().toString())
                        || "2".equalsIgnoreCase(sp_oh_no_of_lt_feeders.getSelectedItem().toString())
                        || "3".equalsIgnoreCase(sp_oh_no_of_lt_feeders.getSelectedItem().toString())
                ) {
                    if (sp_oh_lt_feeder_1.getSelectedItem().toString().equals("Select")) {
                        Toast.makeText(c, "Select Feeder 1 of LT Voltage Ckts", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(c, "Enter Feeder 1 of LT Voltage Ckts", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(c, "Select Feeder 2 of LT Voltage Ckts", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(c, "Enter Feeder 2 of LT Voltage Ckts", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(c, "Select Feeder 3 of LT Voltage Ckts", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(c, "Enter Feeder 3 of LT Voltage Ckts", Toast.LENGTH_SHORT).show();
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

    public void underGround() {
        if (dtrCode != null & !dtrCode.isEmpty() && !ed_pole_no.getText().toString().isEmpty() && !ed_frompole.getText().toString().isEmpty() && !x.isEmpty() && !y.isEmpty()) {
            if (Integer.parseInt(ed_pole_no.getText().toString()) == Integer.parseInt(ed_frompole.getText().toString())) {
                Toast.makeText(c, "Pole No and From Pole No Should Not Be Same Contact Admin", Toast.LENGTH_LONG).show();

            }/*else if("DTR Pole".equalsIgnoreCase(lineType)&&"Yes".equalsIgnoreCase(ug_dtr_pole_dtr_meter_avilable)&&"Select".equalsIgnoreCase(oh_dtr_pole_no_of_meters)){
                Toast.makeText(c,"Select No Of Meters",Toast.LENGTH_SHORT).show();
            }*/ else if (sp_ug_locationtype.getSelectedItem().toString().equals("Pole") && sp_ug_poleheight.getSelectedItem().toString().equals("Select")) {
                Toast.makeText(c, "Select Pole Height", Toast.LENGTH_LONG).show();
            } else if (sp_ug_tapping_point.getSelectedItem().toString().equals("Yes") && edt_ug_tappinglocation.getText().toString().isEmpty()) {
                Toast.makeText(c, "Enter Tapping Location Name", Toast.LENGTH_LONG).show();
            }
           /* else if ("Select".equalsIgnoreCase(sp_pole_concrete.getSelectedItem().toString())) {
                Toast.makeText(c, "Select Pole Concrete", Toast.LENGTH_LONG).show();
            }*/

            else {

                List<LTCktsResponseDataVo> ltCktsResponseDataVos = arcGISDatabase.getDTRMasterData("");
                SurveyDataVo surveyDataVos = new SurveyDataVo();
                surveyDataVos.setDTR_STRUCTURE(dtrCode);
                surveyDataVos.setLT_CIRCUIT(ltCircuit);
                surveyDataVos.setPOLE_NO(Integer.parseInt(ed_pole_no.getText().toString()));
                surveyDataVos.setFROM_POLE(Integer.parseInt(ed_frompole.getText().toString()));
                surveyDataVos.setLINE_TYPE(lineType);
                surveyDataVos.setIS_END_POLE(ug_isendpole);
                surveyDataVos.setLOCATION_TYPE(ug_locationtype);
                surveyDataVos.setLINE_CONFIGURATION(ug_locationtype);
                surveyDataVos.setGPS_ACCURACY(String.valueOf(ArcGISMapActivity.gpsAccuracy));
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
                surveyDataVos.setFEEDER_ID(ltCktsResponseDataVos.get(0).getFEEDER_ID());
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

             /*   surveyDataVos.setIMAGE_1("N");
                surveyDataVos.setIMAGE_2("N");
                surveyDataVos.setIMAGE_3("N");*/

                //IMAGES END

                surveyDataVos.setNUM_OTHER_CABLES("");
                surveyDataVos.setPOLE_CONCRETE("");

                //JHC PHASE 1
                if (LoginActivity.isJHC) {
                    // surveyDataVos.setPOLE_SURVEY_TYPE(sp_pole_survey_type.getSelectedItem().toString());
                    // surveyDataVos.setLINE_SURVEY_TYPE(sp_line_survey_type.getSelectedItem().toString());

                    if (sp_pole_survey_type.getSelectedItem().toString().equalsIgnoreCase("HC Phase 1")) {
                        surveyDataVos.setPOLE_SURVEY_TYPE("JHC");
                    } else {
                        surveyDataVos.setPOLE_SURVEY_TYPE(sp_pole_survey_type.getSelectedItem().toString());
                    }

                    if (sp_line_survey_type.getSelectedItem().toString().equalsIgnoreCase("HC Phase 1")) {
                        surveyDataVos.setLINE_SURVEY_TYPE("JHC");
                    } else {
                        surveyDataVos.setLINE_SURVEY_TYPE(sp_line_survey_type.getSelectedItem().toString());
                    }
                }

                //CURRENT LOCATION
                surveyDataVos.setCURRENT_LAT("" + CURRENT_LAT);
                surveyDataVos.setCURRENT_LONG("" + CURRENT_LONG);

                arcGISDatabase.addSurveyData(surveyDataVos);
                arcGISDatabase.addLTImageData(surveyDataVos);
                arcGISDatabase.addSurveyDatarepeat(surveyDataVos);

                image_1_clicked = false;
                image_2_clicked = false;
                image_3_clicked = false;
                repeatPoleClicked = false;
                Toast.makeText(c, "Pole location captured successfully..", Toast.LENGTH_SHORT).show();
                dismiss();
                callout.dismiss();
                graphicsOverlay.getGraphics().remove(graphic);
                if ("Yes".equalsIgnoreCase(ug_isendpole)) {
                    Intent intent = new Intent(c, HomeActivity.class);
                    c.startActivity(intent);
                    c.finish();
                }
            }
        }
    }


    public void re_pole() {

        sp_poleheight_KEY = "R";
        surveyVoList = arcGISDatabase.getSurveyDataREPEAT("SELECT * FROM SURVEY_TABLE_REPEAT LIMIT 1 OFFSET (SELECT COUNT(*) FROM SURVEY_TABLE_REPEAT)-1");


        if (surveyVoList.size() > 0) {


            for (SurveyDataVo surveyVo : surveyVoList) {


                String IS_lineType = surveyVo.getLINE_TYPE();
                List<String> key_lineType = new ArrayList<>();
                key_lineType.add(IS_lineType);
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
                ArrayAdapter<String> adapter_pole_height = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_poleheight);
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

                    if (st_oh_conductor_size.equals("Yes")) {

                        list_oh_conductor_size.add("No");

                    } else if (st_oh_conductor_size.equals("No")) {

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
                if (st_sp_oh_tapping_point.equals("Yes")) {

                    list_sp_oh_tapping_point.add("No");

                } else if (st_sp_oh_tapping_point.equals("No")) {
                    list_sp_oh_tapping_point.add("Yes");
                }
                ArrayAdapter<String> adapter_sp_oh_tapping_point = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, list_sp_oh_tapping_point);
                sp_oh_tapping_point.setAdapter(adapter_sp_oh_tapping_point);


                String st_oh_otherthan_streetlight = surveyVo.getIS_SERVICES_OTHERTHAN_SL();
                List<String> oh_otherthan_streetlight = new ArrayList<>();
                oh_otherthan_streetlight.add(st_oh_otherthan_streetlight);
                if (st_oh_otherthan_streetlight.equals("No")) {

                    oh_otherthan_streetlight.add("Yes");

                } else if (st_oh_otherthan_streetlight.equals("Yes")) {
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

