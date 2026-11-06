package com.apepdcl.gis_survey.module_11KV;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.HomeActivity;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.module_11KV.models.SurveyVo;
import com.apepdcl.gis_survey.utilits.GpsTraker;
import com.apepdcl.gis_survey.vo.MultiCktVO;
import com.apepdcl.gis_survey.vo.SurveyDataVo;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LayerSceneProperties;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ArcGISMapActivity extends AppCompatActivity implements ImpData, View.OnClickListener {

    private MapView mv;
    ArcGISMap map;
    ArrayList<Graphic> graph_objects = new ArrayList<Graphic>();
    GraphicsOverlay graphicsOverlay, graphicsOverlay_33, graphicsOverlay_11, graphicsOverlay_lt;
    private GraphicsOverlay mGraphicsOverlay;
    private GraphicsOverlay currentLocmGraphicsOverla;
    Graphic graphic, graphic_33, graphic_11, graphic_lt;
    Graphic currentLocGraphic;
    private Callout mCallout;
    GpsTraker gpsTraker;
    public static float gpsAccuracy=0;
    private String ssName, feederName, feederCode, maxCount, tappingPoint, surveyType, sessionId;
    private ArcGISDatabase arcGISDatabase;
    private List<SurveyVo> surveyVoList;
    private Point ptWgs84;
    private int i, count, maxSurveyCount, poleCountCheck;
    private SharedPreferences preferences;
    PointCollection polylinePoints;
    private ImageButton img_logout;
    private LocationDisplay mLocationDisplay;
    TextView calloutContent;
    double CURRENT_LATTITUDE = 0.0, CURRENT_LONGITUDE = 0.0;

    //COMBINED NEW
    public static String typeOfSurvey = "11kv";
    public static int lt_pole_count;
    public static boolean isLTSurveyStarted = false;
    public static boolean isDTRSurvey = false;
    public static String noOfSwitch;
    String DTR_NO;
    public static String ckt1, ckt2, ckt3;
    public static boolean ckt1Available, ckt2Available, ckt3Available;
    public static boolean ckt1Completed = false, ckt2Completed = false, ckt3Completed = false;
    public static String current_ckt;
    public static boolean isCKTChanged;
    public static boolean isPoleAdded = false;
    public static boolean isDTREndPole = false;


    //DISTANCE
    com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase ltDB;
    List<SurveyDataVo> ltSurveyData;
    double lastPoleDistance, currentDistance;

    //MULTI CKT
    LinearLayout multi_circuit;

    boolean isFirstPole = false;

    public static String TAPPING_POINT;
    public static boolean SPUR_FIRST_POLE = true;

    private RelativeLayout reload_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arc_gismap);
        getSupportActionBar().hide();
        SPUR_FIRST_POLE = true;

        mv = (MapView) findViewById(R.id.map1);
        img_logout = (ImageButton) findViewById(R.id.img_logout);
        reload_layout = findViewById(R.id.reload_layout);
        multi_circuit = findViewById(R.id.multi_circuit);
        // System.out.println("IS LT SURVEY STARTED : "+isLTSurveyStarted);
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("11kV");
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isDTRSurvey = false;
                    isLTSurveyStarted = false;
                    isPoleAdded = false;
                    Intent playIntent = new Intent(ArcGISMapActivity.this, HomeActivity.class); //Intent.ACTION_VIEW);
                    startActivity(playIntent);
                    finish();

                }
            });


            gpsTraker = new GpsTraker(ArcGISMapActivity.this);
            Intent intent = getIntent();
            ssName = intent.getStringExtra("SSNAME");
            feederName = intent.getStringExtra("FEEDER_NAME");
            feederCode = intent.getStringExtra("FEEDER_CODE");
            maxCount = intent.getStringExtra("MAX_COUNT");
            surveyType = intent.getStringExtra("SURVEY_TYPE");
            //System.out.println("SURVEY TYPE : "+surveyType);
            // System.out.println("SPUR_FIRST_POLE : "+SPUR_FIRST_POLE);
            if (surveyType.equalsIgnoreCase("Spur Line")) {
                TAPPING_POINT = intent.getStringExtra("TAPPING_POINT");
            }
            gpsTraker.getLocation();

            arcGISDatabase = new ArcGISDatabase(ArcGISMapActivity.this);
            ltDB = new com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase(ArcGISMapActivity.this);

            // surveyVoList = arcGISDatabase.getData();
            surveyVoList = arcGISDatabase.getOnlyLatAndLon();
            count = arcGISDatabase.getObjectId();
            poleCountCheck = arcGISDatabase.getObjectIdCount();
            mGraphicsOverlay = new GraphicsOverlay();
            mv.getGraphicsOverlays().add(mGraphicsOverlay);

            currentLocmGraphicsOverla = new GraphicsOverlay();
            mv.getGraphicsOverlays().add(currentLocmGraphicsOverla);

            img_logout.setVisibility(View.VISIBLE);
            multi_circuit.setVisibility(View.VISIBLE);
            img_logout.setOnClickListener(this);
            maxSurveyCount = Integer.parseInt(maxCount);


            map = new ArcGISMap(Basemap.Type.IMAGERY_WITH_LABELS, gpsTraker.getLatitude(), gpsTraker.getLongitude(), 20);
            mv.setMap(map);

            mLocationDisplay = mv.getLocationDisplay();

            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            sessionId = preferences.getString(MobileConstants.SESSION_ID, "");
            if (sessionId == null || sessionId.isEmpty()) {
                Intent intentLogin = new Intent(ArcGISMapActivity.this, LoginActivity.class);
                startActivity(intentLogin);
                finish();
            }


            mLocationDisplay.addDataSourceStatusChangedListener(dataSourceStatusChangedEvent -> {
                if (dataSourceStatusChangedEvent.isStarted() || dataSourceStatusChangedEvent.getError() == null) {
                    return;
                }


                String message = String.format("Error in DataSourceStatusChangedListener: %s",
                        dataSourceStatusChangedEvent.getSource().getLocationDataSource().getError().getMessage());
                Toast.makeText(ArcGISMapActivity.this, message, Toast.LENGTH_LONG).show();

            });
            // mLocationDisplay.stop();
            mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
            mLocationDisplay.startAsync();


            mv.setOnTouchListener(new DefaultMapViewOnTouchListener(this, mv) {
                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    try {
                        int x = (int) e.getX();
                        int y = (int) e.getY();
                        gpsAccuracy=gpsTraker.getLocation().getAccuracy();
                        System.out.println("GPS_ACCURACY-------->"+gpsAccuracy);
                        android.graphics.Point clickedPoint = new android.graphics.Point(x, y);
                        Point myWebMercatorPoint = mv.screenToLocation(clickedPoint);
                        Point myWGS84Point = (Point) GeometryEngine.project(myWebMercatorPoint,
                                SpatialReferences.getWgs84());


                        // Point point = new Point(myWGS84Point.getX(), myWGS84Point.getY(), SpatialReferences.getWebMercator());
                        Viewpoint vp = new Viewpoint(myWGS84Point, 7500);

                        // set initial map extent
                        map.setInitialViewpoint(vp);

                        // set the map to be displayed in the mapview
                        mv.setMap(map);

                        //mv.removeView(mv);
                        graphicsOverlay = new GraphicsOverlay();
                        mv.getGraphicsOverlays().add(graphicsOverlay);

                        //[DocRef: Name=Point graphic with symbol, Category=Fundamentals, Topic=Symbols and Renderers]
                        //create a simple marker symbol
                        SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CROSS, Color.RED,
                                16); //size 12, style of circle

                        //add a new graphic with a new point geometry
                        // Point graphicPoint = new Point(myWGS84Point.getX(), myWGS84Point.getY(), SpatialReferences.getWebMercator());
                        graphic = new Graphic(myWGS84Point, symbol);
                        graph_objects.add(graphic);
                        graphicsOverlay.getGraphics().add(graphic);
                        //graphicsOverlay.getGraphics().remove(graphic);

                        // create a textview for the callout
                        TextView calloutContent = new TextView(getApplicationContext());
                        calloutContent.setTextColor(Color.BLACK);
                        calloutContent.setSingleLine();

                        //DISTANCE COMMON
                        Location currentStartPoint = new Location("locationA");
                        currentStartPoint.setLatitude(gpsTraker.getLatitude());
                        currentStartPoint.setLongitude(gpsTraker.getLongitude());

                        Location currentEndPoint = new Location("locationA");
                        currentEndPoint.setLatitude(myWGS84Point.getY());
                        currentEndPoint.setLongitude(myWGS84Point.getX());
                        currentDistance = currentStartPoint.distanceTo(currentEndPoint);
                        // format coordinates to 4 decimal places

                        if (typeOfSurvey.equalsIgnoreCase("11kv")) {


                            poleCountCheck = arcGISDatabase.getObjectIdCount();
                            count = arcGISDatabase.getObjectId();
                            // System.out.println("POLE COUNT 11KV: "+count);
                            if (poleCountCheck == 0) {
                                int polenum = maxSurveyCount + 1;
                                calloutContent.setText(String.valueOf(polenum));
                            } else {
                                calloutContent.setText(String.valueOf(count + 1));
                            }
                            if (count == 0) {
                                lastPoleDistance = 0;
                                isFirstPole = true;
                            } else {
                                //DISTANCE LAST POLE
                                isFirstPole = false;
                                Location lastPoleStartPoint = new Location("locationB");
                                if (surveyType.equalsIgnoreCase("Spur Line") && SPUR_FIRST_POLE) {
                                    for (int j = 0; j < surveyVoList.size(); j++) {
                                        if (surveyVoList.get(j).getPole_no() == Integer.parseInt(TAPPING_POINT)) {
                                            lastPoleStartPoint.setLatitude(Double.parseDouble(surveyVoList.get(j).getLan()));
                                            lastPoleStartPoint.setLongitude(Double.parseDouble(surveyVoList.get(j).getLat()));
                                        }
                                    }
                                } else {
                                    lastPoleStartPoint.setLatitude(Double.parseDouble(surveyVoList.get(surveyVoList.size() - 1).getLan()));
                                    lastPoleStartPoint.setLongitude(Double.parseDouble(surveyVoList.get(surveyVoList.size() - 1).getLat()));
                                }


                                Location lastPoleEndPoint = new Location("locationB");
                                lastPoleEndPoint.setLatitude(myWGS84Point.getY());
                                lastPoleEndPoint.setLongitude(myWGS84Point.getX());
                                lastPoleDistance = lastPoleStartPoint.distanceTo(lastPoleEndPoint);
                            }


                        } else {
                            // System.out.println("IS POLE ADDED : "+isPoleAdded);
                            //  System.out.println("POLE COUNT LT: "+lt_pole_count);
                            if (isLTSurveyStarted) {
                                if (isCKTChanged) {
                                    lt_pole_count = 0;
                                    Location lastPoleStartPoint = new Location("locationB");
                                    lastPoleStartPoint.setLatitude(Double.parseDouble(surveyVoList.get(surveyVoList.size() - 1).getLan()));
                                    lastPoleStartPoint.setLongitude(Double.parseDouble(surveyVoList.get(surveyVoList.size() - 1).getLat()));

                                    Location lastPoleEndPoint = new Location("locationB");
                                    lastPoleEndPoint.setLatitude(myWGS84Point.getY());
                                    lastPoleEndPoint.setLongitude(myWGS84Point.getX());
                                    lastPoleDistance = lastPoleStartPoint.distanceTo(lastPoleEndPoint);
                                } else {
                                    if (isPoleAdded) {
                                        lt_pole_count = lt_pole_count + 1;
                                        //DISTANCE LAST POLE
                                        ltSurveyData = ltDB.getOnlyRequiredData("");
                                        Location lastPoleStartPoint = new Location("locationB");
                                        lastPoleStartPoint.setLatitude(Double.parseDouble(ltSurveyData.get(ltSurveyData.size() - 1).getLAN()));
                                        lastPoleStartPoint.setLongitude(Double.parseDouble(ltSurveyData.get(ltSurveyData.size() - 1).getLAT()));

                                        Location lastPoleEndPoint = new Location("locationB");
                                        lastPoleEndPoint.setLatitude(myWGS84Point.getY());
                                        lastPoleEndPoint.setLongitude(myWGS84Point.getX());
                                        lastPoleDistance = lastPoleStartPoint.distanceTo(lastPoleEndPoint);

                                    }

                                }

                            } else {
                                lt_pole_count = 0;
                                Location lastPoleStartPoint = new Location("locationB");
                                lastPoleStartPoint.setLatitude(Double.parseDouble(surveyVoList.get(surveyVoList.size() - 1).getLan()));
                                lastPoleStartPoint.setLongitude(Double.parseDouble(surveyVoList.get(surveyVoList.size() - 1).getLat()));

                                Location lastPoleEndPoint = new Location("locationB");
                                lastPoleEndPoint.setLatitude(myWGS84Point.getY());
                                lastPoleEndPoint.setLongitude(myWGS84Point.getX());
                                lastPoleDistance = lastPoleStartPoint.distanceTo(lastPoleEndPoint);
                            }

                            // System.out.println("POLE NUMBER IN LT : "+lt_pole_count);
                            calloutContent.setText(String.valueOf(lt_pole_count + 1));
                        }

                        // get callout, set content and show
                        mCallout = mMapView.getCallout();
                        mCallout.setLocation(myWGS84Point);
                        mCallout.setContent(calloutContent);
                        mCallout.show();

                        if (typeOfSurvey.equalsIgnoreCase("11kv")) {
                            CustomAllDialogs greeting = new CustomAllDialogs(ArcGISMapActivity.this, String.valueOf(myWGS84Point.getX()),
                                    String.valueOf(myWGS84Point.getY()), mCallout, graphicsOverlay, graphic, feederName,
                                    feederCode, maxSurveyCount, tappingPoint, surveyType, currentLocmGraphicsOverla,
                                    currentLocGraphic, ssName, lastPoleDistance,
                                    currentDistance, "11kv", gpsTraker.getLatitude(), gpsTraker.getLongitude(), isFirstPole);
                            greeting.deligate = ArcGISMapActivity.this;
                            greeting.show();
                        } else {
                            //Activity a, String x, String y, Callout callout, GraphicsOverlay graphicsOverlay, Graphic graphic, int maxSurveyCount, String dtrCode,String ltCircuit
                            LTDialog ltDialog = new LTDialog(ArcGISMapActivity.this, String.valueOf(myWGS84Point.getX()), String.valueOf(myWGS84Point.getY()), mCallout, graphicsOverlay, graphic, lt_pole_count, DTR_NO, current_ckt, feederCode, lastPoleDistance, currentDistance);
                            ltDialog.show();
                        }


                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return true;
                }
            });


            //REMOVE POINTS


            multi_circuit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openMultiCircuitDialog();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

        reload_layout.setOnClickListener(v -> {
            showNearbyPoles();
        });

    }

    private void openMultiCircuitDialog() {
        Dialog payDialog = new Dialog(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        payDialog.setContentView(R.layout.multi_circuit_dialog);
        payDialog.getWindow().setLayout(width, height / 2);
        payDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        payDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        TextView cancel_button = payDialog.findViewById(R.id.dialog_cancel);
        TextView back_to_home = payDialog.findViewById(R.id.back_to_home);
        CheckBox ch_multi_33kv = payDialog.findViewById(R.id.ch_multi_33kv);
        CheckBox ch_multi_11kv = payDialog.findViewById(R.id.ch_multi_11kv);
        CheckBox ch_multi_lt = payDialog.findViewById(R.id.ch_multi_lt);
        LinearLayout feeder_33 = payDialog.findViewById(R.id.feeder_33);
        LinearLayout feeder_11 = payDialog.findViewById(R.id.feeder_11);
        LinearLayout feeder_lt = payDialog.findViewById(R.id.feeder_lt);
        Spinner sp_feeder_33 = payDialog.findViewById(R.id.sp_feeder_33);
        Spinner sp_feeder_11 = payDialog.findViewById(R.id.sp_feeder_11);
        Spinner sp_feeder_lt = payDialog.findViewById(R.id.sp_feeder_lt);
        payDialog.show();

        ch_multi_33kv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    feeder_33.setVisibility(View.VISIBLE);
                   /* List<String> list_feeder_1 = new ArrayList<>();
                    list_feeder_1.addAll(DTRMasterDownloadActivity.filter33kv);
                    ArrayAdapter<String> adapter_feeder_1 = new ArrayAdapter<String>(ArcGISMapActivity.this, android.R.layout.simple_dropdown_item_1line, list_feeder_1);
                    sp_feeder_33.setAdapter(adapter_feeder_1);
                    sp_feeder_33.setSelection(0,true);*/

                    ArrayList<MultiCktVO> filteredList = new ArrayList<>();
                    ArrayList<String> filter33kv = new ArrayList<>();

                    for (int j = 0; j < DTRMasterDownloadActivity.list33kv.size(); j++) {
                        double distance = getDistanceBetweenPoles(DTRMasterDownloadActivity.list33kv.get(j).getGLAT(), DTRMasterDownloadActivity.list33kv.get(j).getGLON());
                        // System.out.println("DISTANCE 33KV FEEDER IN 11KV MULTICKT OUTER : "+distance);
                        if (distance <= 500) {
                            filteredList.add(DTRMasterDownloadActivity.list33kv.get(j));
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
                            filter33kv.add(combinedList.get(i));
                        }
                    }

                    if (filter33kv.isEmpty()) {
                        filter33kv.add("NO DATA");
                    }

                    ArrayAdapter<String> adapter_feeder_1 = new ArrayAdapter<String>(ArcGISMapActivity.this, android.R.layout.simple_dropdown_item_1line, filter33kv);
                    sp_feeder_33.setAdapter(adapter_feeder_1);
                    sp_feeder_33.setSelection(0, true);


                } else {
                    feeder_33.setVisibility(View.GONE);
                }
            }
        });

        ch_multi_11kv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    feeder_11.setVisibility(View.VISIBLE);
                   /* List<String> list_feeder_1 = new ArrayList<>();
                    list_feeder_1.addAll(DTRMasterDownloadActivity.filter11kv);
                    ArrayAdapter<String> adapter_feeder_1 = new ArrayAdapter<String>(ArcGISMapActivity.this, android.R.layout.simple_dropdown_item_1line, list_feeder_1);
                    sp_feeder_11.setAdapter(adapter_feeder_1);
                    sp_feeder_11.setSelection(0,true);*/


                    ArrayList<MultiCktVO> filteredList = new ArrayList<>();
                    ArrayList<String> filter33kv = new ArrayList<>();


                    for (int j = 0; j < DTRMasterDownloadActivity.list11kv.size(); j++) {

                        if (!DTRMasterDownloadActivity.list11kv.get(j).getMC_FEEDER_ID().equals(feederCode)) {
                            double distance = getDistanceBetweenPoles(DTRMasterDownloadActivity.list11kv.get(j).getGLAT(), DTRMasterDownloadActivity.list11kv.get(j).getGLON());
                            // System.out.println("DISTANCE 33KV FEEDER IN 11KV MULTICKT OUTER : "+distance);
                            if (distance <= 500) {
                                filteredList.add(DTRMasterDownloadActivity.list11kv.get(j));
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
                            filter33kv.add(combinedList.get(i));
                        }
                    }

                    if (filter33kv.isEmpty()) {
                        filter33kv.add("NO DATA");
                    }

                    ArrayAdapter<String> adapter_feeder_1 = new ArrayAdapter<String>(ArcGISMapActivity.this, android.R.layout.simple_dropdown_item_1line, filter33kv);
                    sp_feeder_11.setAdapter(adapter_feeder_1);
                    sp_feeder_11.setSelection(0, true);


                } else {
                    feeder_11.setVisibility(View.GONE);
                }
            }
        });

        ch_multi_lt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    feeder_lt.setVisibility(View.VISIBLE);
                   /* List<String> list_feeder_1 = new ArrayList<>();
                    list_feeder_1.addAll(DTRMasterDownloadActivity.filterLT);
                    ArrayAdapter<String> adapter_feeder_1 = new ArrayAdapter<String>(ArcGISMapActivity.this, android.R.layout.simple_dropdown_item_1line, list_feeder_1);
                    sp_feeder_lt.setAdapter(adapter_feeder_1);
                    sp_feeder_lt.setSelection(0,true);*/


                    ArrayList<MultiCktVO> filteredList = new ArrayList<>();
                    ArrayList<String> filter33kv = new ArrayList<>();

                    for (int j = 0; j < DTRMasterDownloadActivity.listLT.size(); j++) {
                        double distance = getDistanceBetweenPoles(DTRMasterDownloadActivity.listLT.get(j).getGLAT(), DTRMasterDownloadActivity.listLT.get(j).getGLON());
                        // System.out.println("DISTANCE 33KV FEEDER IN 11KV MULTICKT OUTER : "+distance);
                        if (distance <= 500) {
                            filteredList.add(DTRMasterDownloadActivity.listLT.get(j));
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
                            filter33kv.add(combinedList.get(i));
                        }
                    }

                    if (filter33kv.isEmpty()) {
                        filter33kv.add("NO DATA");
                    }

                    ArrayAdapter<String> adapter_feeder_1 = new ArrayAdapter<String>(ArcGISMapActivity.this, android.R.layout.simple_dropdown_item_1line, filter33kv);
                    sp_feeder_lt.setAdapter(adapter_feeder_1);
                    sp_feeder_lt.setSelection(0, true);

                } else {
                    feeder_lt.setVisibility(View.GONE);
                }
            }
        });


        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payDialog.dismiss();
            }
        });

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ch_multi_33kv.isChecked() || ch_multi_11kv.isChecked() || ch_multi_lt.isChecked()) {
                    payDialog.dismiss();

                    if (ch_multi_33kv.isChecked()) {

                        if (sp_feeder_33.getSelectedItem().toString().equals("NO DATA")) {
                            Toast.makeText(ArcGISMapActivity.this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
                        } else {
                            loadMultiVoltageMap("33", sp_feeder_33.getSelectedItem().toString());
                        }


                    }
                    if (ch_multi_11kv.isChecked()) {
                        if (sp_feeder_11.getSelectedItem().toString().equals("NO DATA")) {
                            Toast.makeText(ArcGISMapActivity.this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
                        } else {
                            loadMultiVoltageMap("11", sp_feeder_11.getSelectedItem().toString());
                        }

                    }
                    if (ch_multi_lt.isChecked()) {
                        if (sp_feeder_lt.getSelectedItem().toString().equals("NO DATA")) {
                            Toast.makeText(ArcGISMapActivity.this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
                        } else {
                            loadMultiVoltageMap("lt", sp_feeder_lt.getSelectedItem().toString());
                            ;
                        }

                    }

                    // loadMultiCircuitData(ch_multi_33kv.isChecked(),ch_multi_11kv.isChecked(),ch_multi_lt.isChecked());
                } else {
                    Toast.makeText(ArcGISMapActivity.this, "Please Choose at least 1 multi circuit", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void loadMultiVoltageMap(String from, String selectedFeeder) {
        // graphicsOverlay.getGraphics().remove(graphic);


        ArrayList<MultiCktVO> old_33_list = new ArrayList<>(DTRMasterDownloadActivity.list33kv);
        ArrayList<MultiCktVO> list = new ArrayList<>();
        ArrayList<MultiCktVO> old_list_11kv = new ArrayList<>(DTRMasterDownloadActivity.list11kv);
        ArrayList<MultiCktVO> list_11kv = new ArrayList<>();
        ArrayList<MultiCktVO> old_list_lt = new ArrayList<>(DTRMasterDownloadActivity.listLT);
        ArrayList<MultiCktVO> list_lt = new ArrayList<>();


        if (from.equals("33")) {
            String[] splitFeederId = selectedFeeder.split("~");
            selectedFeeder = splitFeederId[0];
            for (int j = 0; j < old_33_list.size(); j++) {
                if (selectedFeeder.equals(old_33_list.get(j).getMC_FEEDER_ID())) {
                    double distance = getDistanceBetweenPoles(old_33_list.get(j).getGLAT(), old_33_list.get(j).getGLON());
                    //  System.out.println("DISTANCE 33KV FEEDER STATIC : "+distance);
                    if (distance <= 200) {
                        list.add(old_33_list.get(j));
                    }
                }

            }

            for (int i = 0; i < list.size(); i++) {

                ptWgs84 = new Point(Double.parseDouble(list.get(i).getGLAT()), Double.parseDouble(list.get(i).getGLON()), 414, SpatialReferences.getWgs84());
                Viewpoint vp = new Viewpoint(ptWgs84, 7500);
                map.setInitialViewpoint(vp);
                mv.setMap(map);
                graphicsOverlay_33 = new GraphicsOverlay();
                mv.getGraphicsOverlays().add(graphicsOverlay_33);
                SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.BLUE, 12);
                graphic_33 = new Graphic(ptWgs84, symbol);
                graph_objects.add(graphic_33);
                graphicsOverlay_33.getGraphics().add(graphic_33);
                GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                absoluteOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties.SurfacePlacement.ABSOLUTE);
                absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, symbol));
                TextSymbol absoluteText = new TextSymbol(15, list.get(i).getMC_FEEDER_ID() + "-" + list.get(i).getMC_POLE_NO(), Color.WHITE, TextSymbol.HorizontalAlignment.LEFT,
                        TextSymbol.VerticalAlignment.MIDDLE);
                absoluteText.setOffsetY(20);
                absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, absoluteText));
                mv.getGraphicsOverlays().add(absoluteOverlay);

            }

        }

        if (from.equals("11")) {
            String[] splitFeederId = selectedFeeder.split("~");
            selectedFeeder = splitFeederId[0];

            for (int j = 0; j < old_list_11kv.size(); j++) {
                if (selectedFeeder.equals(old_list_11kv.get(j).getMC_FEEDER_ID())) {
                    double distance = getDistanceBetweenPoles(old_list_11kv.get(j).getGLAT(), old_list_11kv.get(j).getGLON());
                    //System.out.println("DISTANCE 11KV FEEDER STATIC : "+distance);
                    if (distance <= 200) {
                        list_11kv.add(old_list_11kv.get(j));
                    }
                }

            }

            for (int i = 0; i < list_11kv.size(); i++) {
                // //System.out.println("11KV LAT : "+list_11kv.get(i).getGLAT());
                // //System.out.println("11KV LON : "+list_11kv.get(i).getGLON());
                ptWgs84 = new Point(Double.parseDouble(list_11kv.get(i).getGLAT()), Double.parseDouble(list_11kv.get(i).getGLON()), 414, SpatialReferences.getWgs84());
                Viewpoint vp = new Viewpoint(ptWgs84, 7500);
                map.setInitialViewpoint(vp);
                mv.setMap(map);
                graphicsOverlay_11 = new GraphicsOverlay();
                mv.getGraphicsOverlays().add(graphicsOverlay_11);
                SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.YELLOW, 12);
                graphic_11 = new Graphic(ptWgs84, symbol);
                graph_objects.add(graphic_11);
                graphicsOverlay_11.getGraphics().add(graphic_11);
                GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                absoluteOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties.SurfacePlacement.ABSOLUTE);
                absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, symbol));
                TextSymbol absoluteText = new TextSymbol(15, list_11kv.get(i).getMC_FEEDER_ID() + "-" + list_11kv.get(i).getMC_POLE_NO(), Color.WHITE, TextSymbol.HorizontalAlignment.LEFT,
                        TextSymbol.VerticalAlignment.MIDDLE);
                absoluteText.setOffsetY(20);
                absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, absoluteText));
                mv.getGraphicsOverlays().add(absoluteOverlay);


            }

        }

        if (from.equals("lt")) {

            for (int j = 0; j < old_list_lt.size(); j++) {
                if (selectedFeeder.equals(old_list_lt.get(j).getMC_FEEDER_NAME())) {
                    double distance = getDistanceBetweenPoles(old_list_lt.get(j).getGLAT(), old_list_lt.get(j).getGLON());
                    //  System.out.println("DISTANCE LT FEEDER STATIC : "+distance);
                    if (distance <= 200) {
                        list_lt.add(old_list_lt.get(j));
                    }
                }


            }

            for (int i = 0; i < list_lt.size(); i++) {
                // //System.out.println("11KV LAT : "+list_11kv.get(i).getGLAT());
                // //System.out.println("11KV LON : "+list_11kv.get(i).getGLON());
                ptWgs84 = new Point(Double.parseDouble(list_lt.get(i).getGLAT()), Double.parseDouble(list_lt.get(i).getGLON()), 414, SpatialReferences.getWgs84());
                Viewpoint vp = new Viewpoint(ptWgs84, 7500);
                map.setInitialViewpoint(vp);
                mv.setMap(map);
                graphicsOverlay_lt = new GraphicsOverlay();
                mv.getGraphicsOverlays().add(graphicsOverlay_lt);
                SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.rgb(3, 251, 13), 12);
                graphic_lt = new Graphic(ptWgs84, symbol);
                graph_objects.add(graphic_lt);
                graphicsOverlay_lt.getGraphics().add(graphic_lt);
                GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                absoluteOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties.SurfacePlacement.ABSOLUTE);
                absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, symbol));
                TextSymbol absoluteText = new TextSymbol(15, list_lt.get(i).getMC_FEEDER_NAME() + "-" + list_lt.get(i).getMC_POLE_NO(), Color.WHITE, TextSymbol.HorizontalAlignment.LEFT,
                        TextSymbol.VerticalAlignment.MIDDLE);
                absoluteText.setOffsetY(20);
                absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, absoluteText));
                mv.getGraphicsOverlays().add(absoluteOverlay);


            }


        }

    }

    private double getDistanceBetweenPoles(String polLat, String polLon) {
//        System.out.println("CURRENT LOCATION LAT : "+gpsTraker.getLongitude());
//        System.out.println("CURRENT LOCATION LON : "+gpsTraker.getLatitude());
//        System.out.println("POLE LOCATION LAT : "+polLat);
//        System.out.println("POLE LOCATION LON : "+polLon);
        Location currentStartPoint = new Location("locationC");
        currentStartPoint.setLatitude(gpsTraker.getLatitude());
        currentStartPoint.setLongitude(gpsTraker.getLongitude());

        Location currentEndPoint = new Location("locationC");
        currentEndPoint.setLatitude(Double.parseDouble(polLon));
        currentEndPoint.setLongitude(Double.parseDouble(polLat));
        double currentDistance = currentStartPoint.distanceTo(currentEndPoint);
        return currentDistance;
    }


    private double getDistanceBetweenPolesNew(String polLat, String polLon, String cLat, String cLon) {
        System.out.println("CURRENT LOCATION LAT NEW : " + cLat);
        System.out.println("CURRENT LOCATION LON NEW: " + cLon);
        System.out.println("POLE LOCATION LAT NEW:  " + polLat);
        System.out.println("POLE LOCATION LON NEW:  " + polLon);
        Location currentStartPoint = new Location("locationC");
        currentStartPoint.setLatitude(Double.parseDouble(cLat));
        currentStartPoint.setLongitude(Double.parseDouble(cLon));

        Location currentEndPoint = new Location("locationC");
        currentEndPoint.setLatitude(Double.parseDouble(polLat));
        currentEndPoint.setLongitude(Double.parseDouble(polLon));
        double currentDistance = currentStartPoint.distanceTo(currentEndPoint);
        return currentDistance;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mv.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mv.resume();

        if (com.apepdcl.gis_survey.module_11KV.CustomAllDialogs.isImageCaptured1_11KV) {


            Uri imageUri = getLastImageUri(this);
            if (imageUri != null) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    com.apepdcl.gis_survey.module_11KV.CustomAllDialogs.image_1.setImageBitmap(bitmap);
                    com.apepdcl.gis_survey.module_11KV.CustomAllDialogs.image_1_clicked = true;
                    com.apepdcl.gis_survey.module_11KV.CustomAllDialogs.isImageCaptured1_11KV = false;
                    com.apepdcl.gis_survey.module_11KV.CustomAllDialogs.image_1_base64 = getLastImagePath(this);
                    if (inputStream != null) inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (com.apepdcl.gis_survey.module_11KV.CustomAllDialogs.isImageCaptured2_11KV) {


            Uri imageUri = getLastImageUri(this);
            if (imageUri != null) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    com.apepdcl.gis_survey.module_11KV.CustomAllDialogs.image_2.setImageBitmap(bitmap);
                    com.apepdcl.gis_survey.module_11KV.CustomAllDialogs.image_2_clicked = true;
                    com.apepdcl.gis_survey.module_11KV.CustomAllDialogs.isImageCaptured2_11KV = false;
                    com.apepdcl.gis_survey.module_11KV.CustomAllDialogs.image_2_base64 = getLastImagePath(this);
                    if (inputStream != null) inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private String getLastImagePath(Context context) {
        Uri imageUri = null;
        String imageFilePath = null;

        String[] projection = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME
        };

        String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";

        Cursor cursor = null;
        try {
            Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver cr = context.getContentResolver();
            cursor = cr.query(images, projection, null, null, sortOrder);

            if (cursor != null && cursor.moveToFirst()) {
                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);

                long id = cursor.getLong(idColumn);
                String name = cursor.getString(nameColumn);
                imageUri = ContentUris.withAppendedId(images, id);

                // Use this stream to read the image
                InputStream inputStream = cr.openInputStream(imageUri);

                // Create a temporary file in the app's cache directory
                File tempFile = new File(context.getCacheDir(), name);
                FileOutputStream outputStream = new FileOutputStream(tempFile);

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.close();
                inputStream.close();

                imageFilePath = tempFile.getAbsolutePath(); // Here's your usable file path
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }

// Use imageFilePath as needed (e.g., upload, native processing)
        return imageFilePath;
    }

    private Uri getLastImageUri(Context context) {
        Uri imageUri = null;
        String[] projection = {MediaStore.Images.Media._ID};
        String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";

        Cursor cursor = null;
        try {
            Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver cr = context.getContentResolver();
            cursor = cr.query(images, projection, null, null, sortOrder);

            if (cursor != null && cursor.moveToFirst()) {
                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                long id = cursor.getLong(idColumn);
                imageUri = Uri.withAppendedPath(images, String.valueOf(id));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return imageUri;
    }

    @Override
    public void onBackPressed() {
        isLTSurveyStarted = false;
        isDTRSurvey = false;
        isPoleAdded = false;
        Intent intent = new Intent(ArcGISMapActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_logout:
                try {
                    LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    boolean gps_enabled = false;
                    boolean network_enabled = false;

                    try {
                        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    } catch (Exception ex) {
                    }

                    try {
                        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                    } catch (Exception ex) {
                    }

                    if (!gps_enabled && !network_enabled) {
                        new AlertDialog.Builder(ArcGISMapActivity.this)
                                .setTitle("GPS Alert")
                                .setMessage("Please Enable GPS")

                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Continue with delete operation
                                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                    }
                                })

                                // A null listener allows the button to dismiss the dialog and take no further action.
                                .setNegativeButton(android.R.string.no, null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                    } else {

                        /* ** ADD ** */
                        mLocationDisplay.addDataSourceStatusChangedListener(dataSourceStatusChangedEvent -> {
                            if (dataSourceStatusChangedEvent.isStarted() || dataSourceStatusChangedEvent.getError() == null) {
                                return;
                            }


                            String message = String.format("Error in DataSourceStatusChangedListener: %s",
                                    dataSourceStatusChangedEvent.getSource().getLocationDataSource().getError().getMessage());
                            Toast.makeText(ArcGISMapActivity.this, message, Toast.LENGTH_LONG).show();

                        });
                        // mLocationDisplay.stop();
                        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
                        mLocationDisplay.startAsync();
                    }

                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    @Override
    public void IsSetData(String data, String postion) {

        if (postion.equals("0")) {

        } else {

        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        try {
            surveyVoList = arcGISDatabase.getOnlyLatAndLon();
            count = arcGISDatabase.getObjectId();
            // System.out.println("LT SURVEY STARTED "+isLTSurveyStarted);

            LocationDisplay locationDisplay = mv.getLocationDisplay();
            locationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
            locationDisplay.startAsync();


            locationDisplay.addDataSourceStatusChangedListener(statusChangedEvent -> {
                if (statusChangedEvent.isStarted()) {
                    System.out.println("Location display started");
                } else if (statusChangedEvent.getError() != null) {
                    System.out.println("Location error: " + statusChangedEvent.getError().getMessage());
                }
            });


            locationDisplay.addLocationChangedListener(locationChangedEvent -> {
                Point currentPoint = locationChangedEvent.getLocation().getPosition();
                CURRENT_LATTITUDE = currentPoint.getY();
                CURRENT_LONGITUDE = currentPoint.getX();
                // System.out.println("CURRENT_DEVICE_LOCATION---> Lat: " + CURRENT_LATTITUDE + " Lon: " + CURRENT_LONGITUDE);
            });


            if (isLTSurveyStarted) {
                polylinePoints = new PointCollection(SpatialReferences.getWgs84());
                com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase ltDatabase = new com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase(ArcGISMapActivity.this);
                // List<SurveyDataVo> list = ltDatabase.getSurveyDataByDtrCode(DTR_NO);
                List<SurveyDataVo> list = ltDatabase.getOnlyRequiredData("");
                // System.out.println("LT SURVEY SIZE : "+list.size());

                for (SurveyDataVo surveyVo : list) {
                    // System.out.println("LT LAT : "+surveyVo.getLAT());
                    //  System.out.println("LT LON : "+surveyVo.getLAN());
                    //  System.out.println("CKT NO : "+surveyVo.getLT_CIRCUIT());
                    ptWgs84 = new Point(Double.parseDouble(surveyVo.getLAT()), Double.parseDouble(surveyVo.getLAN()), 414, SpatialReferences.getWgs84());
                    Viewpoint vp = new Viewpoint(ptWgs84, 7500);
                    String polenum = "";
                    if (surveyVo.getLT_CIRCUIT().contains("A")) {
                        polenum = "A-" + surveyVo.getPOLE_NO();
                    } else if (surveyVo.getLT_CIRCUIT().contains("B")) {
                        polenum = "B-" + surveyVo.getPOLE_NO();
                    } else {
                        polenum = "C-" + surveyVo.getPOLE_NO();
                    }

                    //  String polenum = String.valueOf(surveyVo.getPOLE_NO());
                    // set initial map extent
                    map.setInitialViewpoint(vp);

                    // set the map to be displayed in the mapview
                    mv.setMap(map);

                    //mv.removeView(mv);
                    graphicsOverlay = new GraphicsOverlay();
                    mv.getGraphicsOverlays().add(graphicsOverlay);

                    //[DocRef: Name=Point graphic with symbol, Category=Fundamentals, Topic=Symbols and Renderers]
                    //create a simple marker symbol
                    // System.out.println("POLE TYPE : "+surveyVo.getPole_configuration());
                    SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.CYAN, 12);


                    //add a new graphic with a new point geometry
                    // Point graphicPoint = new Point(myWGS84Point.getX(), myWGS84Point.getY(), SpatialReferences.getWebMercator());
                    graphic = new Graphic(ptWgs84, symbol);
                    graph_objects.add(graphic);
                    graphicsOverlay.getGraphics().add(graphic);

                    GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                    absoluteOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties.SurfacePlacement.ABSOLUTE);
                    absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, symbol));
                    // create a text symbol for elevation mode
                    TextSymbol absoluteText = new TextSymbol(15, polenum, Color.WHITE, TextSymbol.HorizontalAlignment.CENTER,
                            TextSymbol.VerticalAlignment.MIDDLE);
                    absoluteText.setOffsetY(20);
                    absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, absoluteText));
                    mv.getGraphicsOverlays().add(absoluteOverlay);


                    // create a textview for the callout


                    calloutContent = new TextView(getApplicationContext());
                    calloutContent.setTextColor(Color.BLACK);
                    calloutContent.setSingleLine();
                    calloutContent.setText(polenum);
                    mCallout = mv.getCallout();
                    mCallout.setLocation(ptWgs84);
                    mCallout.setContent(calloutContent);
                    mCallout.show();


                }


            } else {
                if (surveyVoList.size() > 0) {
                    polylinePoints = new PointCollection(SpatialReferences.getWgs84());

                    for (SurveyVo surveyVo : surveyVoList) {

                        ptWgs84 = new Point(
                                Double.parseDouble(surveyVo.getLat()),
                                Double.parseDouble(surveyVo.getLan()),
                                414,
                                SpatialReferences.getWgs84()
                        );

                        Viewpoint vp = new Viewpoint(ptWgs84, 7500);
                        map.setInitialViewpoint(vp);
                        mv.setMap(map);

                        double distance = getDistanceBetweenPolesNew(
                                String.valueOf(surveyVo.getLat()),
                                String.valueOf(surveyVo.getLan()), String.valueOf(gpsTraker.getLongitude()), String.valueOf(gpsTraker.getLatitude()));


//                        double distance = getDistanceBetweenPoles(
//                                surveyVo.getLat(),
//                                surveyVo.getLan()
//                        );


                        System.out.println("Current Distance->  " + distance);
                        if (distance >= 1000) {
                            System.out.println("SKIPPED POLE (Distance > 1000): " + surveyVo.getPole_no() + " Distance->  " + distance);
                            continue;
                        }

                        // Create symbol only if within 500 meters
                        SimpleMarkerSymbol symbol;
                        if (surveyVo.getPole_configuration() != null) {
                            if (surveyVo.getPole_configuration().equalsIgnoreCase("DTR Pole")) {
                                symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.SQUARE, Color.CYAN, 16);
                                symbol.setOutline(new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.RED, 1));
                            } else if (surveyVo.getPole_configuration().equalsIgnoreCase("HT Service-Pole")) {
                                symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.DIAMOND, Color.RED, 16);
                            } else {
                                symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 12);
                            }
                        } else {
                            symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 12);
                        }

                        // Add marker
                        GraphicsOverlay graphicsOverlay = new GraphicsOverlay();
                        mv.getGraphicsOverlays().add(graphicsOverlay);

                        Graphic graphic = new Graphic(ptWgs84, symbol);
                        graphicsOverlay.getGraphics().add(graphic);

                        // Add text overlay
                        GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                        absoluteOverlay.getSceneProperties()
                                .setSurfacePlacement(LayerSceneProperties.SurfacePlacement.ABSOLUTE);

                        TextSymbol absoluteText = new TextSymbol(
                                15,
                                "" + surveyVo.getPole_no(),
                                Color.WHITE,
                                TextSymbol.HorizontalAlignment.CENTER,
                                TextSymbol.VerticalAlignment.MIDDLE
                        );
                        absoluteText.setOffsetY(20);
                        absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, absoluteText));
                        mv.getGraphicsOverlays().add(absoluteOverlay);

                        // Show callout
                        if (surveyType.equalsIgnoreCase("Spur Line") && SPUR_FIRST_POLE) {
                            for (SurveyVo vo : surveyVoList) {
                                if (vo.getPole_no() == Integer.parseInt(TAPPING_POINT)) {
                                    Point tapPt = new Point(
                                            Double.parseDouble(vo.getLat()),
                                            Double.parseDouble(vo.getLan()),
                                            414,
                                            SpatialReferences.getWgs84()
                                    );
                                    calloutContent = new TextView(getApplicationContext());
                                    calloutContent.setTextColor(Color.BLACK);
                                    calloutContent.setSingleLine();
                                    calloutContent.setText(TAPPING_POINT);
                                    mCallout = mv.getCallout();
                                    mCallout.setLocation(tapPt);
                                    mCallout.setContent(calloutContent);
                                    mCallout.show();
                                }
                            }
                        } else {
                            Point ptCallout = new Point(
                                    Double.parseDouble(surveyVo.getLat()),
                                    Double.parseDouble(surveyVo.getLan()),
                                    414,
                                    SpatialReferences.getWgs84()
                            );
                            calloutContent = new TextView(getApplicationContext());
                            calloutContent.setTextColor(Color.BLACK);
                            calloutContent.setSingleLine();
                            calloutContent.setText(String.valueOf(surveyVo.getPole_no()));
                            mCallout = mv.getCallout();
                            mCallout.setLocation(ptCallout);
                            mCallout.setContent(calloutContent);
                            mCallout.show();
                        }
                    }
                }
            }

            if (isDTREndPole) {
                isDTREndPole = false;
               /* AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ArcGISMapActivity.this);
                alertDialogBuilder.setTitle("LT Survey!!");
                alertDialogBuilder
                        .setMessage("Do You Want to start LT Survey?")
                        .setCancelable(false)
                        .setPositiveButton("YES",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        DTR_NO = surveyVoList.get(surveyVoList.size()-1).getDtr_code();
                                        setUpLTGISSurvey();
                                    }
                                })

                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Intent intent = new Intent(ArcGISMapActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();

                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();*/
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showNearbyPoles() {
        try {
            // Clear previous overlays
            mv.getGraphicsOverlays().clear();

            // Get all poles from database
            List<SurveyVo> allPoles = arcGISDatabase.getOnlyLatAndLon();

            if (allPoles == null || allPoles.isEmpty()) {
                Toast.makeText(this, "No poles found in database", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create overlay for nearby poles
            GraphicsOverlay graphicsOverlay = new GraphicsOverlay();
            mv.getGraphicsOverlays().add(graphicsOverlay);

            int nearbyCount = 0;

            for (SurveyVo pole : allPoles) {
                double poleLat = Double.parseDouble(pole.getLat());
                double poleLon = Double.parseDouble(pole.getLan());

                // Calculate distance from current location
                double distance = getDistanceBetweenPolesNew(
                        String.valueOf(poleLat),
                        String.valueOf(poleLon),
                        String.valueOf(gpsTraker.getLongitude()),
                        String.valueOf(gpsTraker.getLatitude())

                );
                System.out.println("NEWWWW_DISTANCE---->:::::" + distance);

                if (distance <= 500) { // within 500 meters
                    nearbyCount++;

                    // Marker symbol for nearby pole
                    SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(
                            SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 14
                    );

                    // Create point
                    Point point = new Point(poleLat, poleLon, SpatialReferences.getWgs84());
                    Graphic graphic = new Graphic(point, symbol);
                    graphicsOverlay.getGraphics().add(graphic);

                    // Label
                    TextSymbol label = new TextSymbol(
                            15,
                            String.valueOf(pole.getPole_no()),
                            Color.WHITE,
                            TextSymbol.HorizontalAlignment.CENTER,
                            TextSymbol.VerticalAlignment.MIDDLE
                    );
                    label.setOffsetY(20);

                    GraphicsOverlay textOverlay = new GraphicsOverlay();
                    textOverlay.getGraphics().add(new Graphic(point, label));
                    mv.getGraphicsOverlays().add(textOverlay);
                }
            }

            if (nearbyCount == 0) {
                Toast.makeText(this, "No poles found within 500 meters", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, nearbyCount + " poles found within 500 meters", Toast.LENGTH_SHORT).show();

                // Center map on current location

                Point myPoint = new Point(gpsTraker.getLongitude(), gpsTraker.getLatitude(), SpatialReferences.getWgs84());
                SimpleMarkerSymbol myLocationSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.BLUE, 14);
                graphicsOverlay.getGraphics().add(new Graphic(myPoint, myLocationSymbol));

                // Zoom to current location
                Viewpoint viewpoint = new Viewpoint(myPoint, 2000);
                mv.setViewpointAsync(viewpoint);

//                Point userPoint = new Point(gpsTraker.getLatitude(), gpsTraker.getLongitude(), SpatialReferences.getWgs84());
//                Viewpoint viewpoint = new Viewpoint(userPoint, 2000);
//                mv.setViewpointAsync(viewpoint);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading nearby poles", Toast.LENGTH_SHORT).show();
        }
    }


    private void setUpLTGISSurvey() {
        typeOfSurvey = "LT";
        if (noOfSwitch.equalsIgnoreCase("3")) {
            ckt1Available = true;
            ckt2Available = true;
            ckt3Available = true;
            ckt1 = DTR_NO + "_LTCKT_A";
            ckt2 = DTR_NO + "_LTCKT_B";
            ckt3 = DTR_NO + "_LTCKT_C";
        }
        if (noOfSwitch.equalsIgnoreCase("2")) {
            ckt1Available = true;
            ckt2Available = true;
            ckt3Available = false;
            ckt1 = DTR_NO + "_LTCKT_A";
            ckt2 = DTR_NO + "_LTCKT_B";
        }
        if (noOfSwitch.equalsIgnoreCase("1")) {
            ckt1Available = true;
            ckt2Available = false;
            ckt3Available = false;
            ckt1 = DTR_NO + "_LTCKT_A";
        }
        current_ckt = ckt1;
        Toast.makeText(this, "Start LT Survey...", Toast.LENGTH_SHORT).show();

    }

    private void startLTGISSurvey() {

    }


    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // System.out.println("ResultCode :" + resultCode);
            // System.out.println("RequestCode :" + requestCode);
            // System.out.println("Data  :" + data);

            if (requestCode == CustomAllDialogs.CAMERA_RESULT_CODE_IMAGE_1 && resultCode == RESULT_OK && data != null) {
                Bitmap image_1_bitmap = (Bitmap) data.getExtras().get("data");
                CustomAllDialogs.image_1.setImageBitmap(image_1_bitmap);

                Uri uri = ConvertImage.getImageUri(ArcGISMapActivity.this, image_1_bitmap);
                String path = ConvertImage.getPathFromURI(uri, ArcGISMapActivity.this);
                CustomAllDialogs.image_1_base64 = path;
                CustomAllDialogs.image_1_clicked = true;

            } else if (requestCode == CustomAllDialogs.CAMERA_RESULT_CODE_IMAGE_2 && resultCode == RESULT_OK && data != null) {
                Bitmap image_2_bitmap = (Bitmap) data.getExtras().get("data");
                CustomAllDialogs.image_2.setImageBitmap(image_2_bitmap);

                Uri uri = ConvertImage.getImageUri(ArcGISMapActivity.this, image_2_bitmap);
                String path = ConvertImage.getPathFromURI(uri, ArcGISMapActivity.this);
                CustomAllDialogs.image_2_base64 = path;
                CustomAllDialogs.image_2_clicked = true;


            } else if (requestCode == CustomAllDialogs.CAMERA_RESULT_CODE_IMAGE_3 && resultCode == RESULT_OK && data != null) {
                Bitmap image_3_bitmap = (Bitmap) data.getExtras().get("data");
                CustomAllDialogs.image_3.setImageBitmap(image_3_bitmap);

                Uri uri = ConvertImage.getImageUri(ArcGISMapActivity.this, image_3_bitmap);
                String path = ConvertImage.getPathFromURI(uri, ArcGISMapActivity.this);
                CustomAllDialogs.image_3_base64 = path;
                CustomAllDialogs.image_3_clicked = true;


            } else if (requestCode == LTDialog.CAMERA_RESULT_CODE_IMAGE_1 && resultCode == RESULT_OK && data != null) {
                Bitmap image_1_bitmap = (Bitmap) data.getExtras().get("data");
                LTDialog.image_1.setImageBitmap(image_1_bitmap);

                Uri uri = ConvertImage.getImageUri(ArcGISMapActivity.this, image_1_bitmap);
                String path = ConvertImage.getPathFromURI(uri, ArcGISMapActivity.this);
                LTDialog.image_1_base64 = path;
                LTDialog.image_1_clicked = true;

            } else if (requestCode == LTDialog.CAMERA_RESULT_CODE_IMAGE_2 && resultCode == RESULT_OK && data != null) {
                Bitmap image_2_bitmap = (Bitmap) data.getExtras().get("data");
                LTDialog.image_2.setImageBitmap(image_2_bitmap);

                Uri uri = ConvertImage.getImageUri(ArcGISMapActivity.this, image_2_bitmap);
                String path = ConvertImage.getPathFromURI(uri, ArcGISMapActivity.this);
                LTDialog.image_2_base64 = path;
                LTDialog.image_2_clicked = true;
            } else if (requestCode == LTDialog.CAMERA_RESULT_CODE_IMAGE_3 && resultCode == RESULT_OK && data != null) {
                Bitmap image_3_bitmap = (Bitmap) data.getExtras().get("data");
                LTDialog.image_3.setImageBitmap(image_3_bitmap);

                Uri uri = ConvertImage.getImageUri(ArcGISMapActivity.this, image_3_bitmap);
                String path = ConvertImage.getPathFromURI(uri, ArcGISMapActivity.this);
                LTDialog.image_3_base64 = path;
                LTDialog.image_3_clicked = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}