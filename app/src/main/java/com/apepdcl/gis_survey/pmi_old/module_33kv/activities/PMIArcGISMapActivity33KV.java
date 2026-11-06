package com.apepdcl.gis_survey.pmi_old.module_33kv.activities;

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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.module_11KV.ConvertImage;
import com.apepdcl.gis_survey.module_11KV.ImpData;
import com.apepdcl.gis_survey.pmi_old.module_11kv.activities.PMIHomeActivity;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMIMultiCktVO;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMISurveyVo;
import com.apepdcl.gis_survey.pmi_old.module_33kv.ArcGISDatabase33KV;
import com.apepdcl.gis_survey.pmi_old.module_33kv.PMICustomAllDialogs33KV;
import com.apepdcl.gis_survey.utilits.GpsTraker;
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

public class PMIArcGISMapActivity33KV extends AppCompatActivity implements ImpData, View.OnClickListener{

    private MapView mv;
    ArcGISMap map;
    ArrayList<Graphic> graph_objects = new ArrayList<Graphic>();
    GraphicsOverlay graphicsOverlay,graphicsOverlay_33,graphicsOverlay_11,graphicsOverlay_lt;
    //GraphicsLayer graphicsLayer = new GraphicsLayer();
    private GraphicsOverlay mGraphicsOverlay;
    private GraphicsOverlay currentLocmGraphicsOverla;
    Graphic graphic,graphic_33,graphic_11,graphic_lt;
    Graphic currentLocGraphic;
    Button btndelete;
    private Callout mCallout;
    GpsTraker gpsTraker;
    private String ssName,feederName,feederCode,maxCount,tappingPoint,surveyType,sessionId,work_order;
    private ArcGISDatabase33KV arcGISDatabase;
    private List<PMISurveyVo> surveyVoList;
    private Point ptWgs84;
    private int i,count,maxSurveyCount,poleCountCheck;
    private SharedPreferences preferences;
    PointCollection polylinePoints;
    private ImageButton img_logout;
    private LocationDisplay mLocationDisplay;
    TextView calloutContent;

    //MULTI CKT
    LinearLayout multi_circuit;

    //DISTANCE
    double lastPoleDistance,currentDistance;

    //REPEAT POLE
    public static boolean IS_SURVEY_STARTED = false;

    public static boolean isFirstPole = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pmiarc_gismap_activity33_kv);
        getSupportActionBar().hide();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Defects noticed under PMI - 33KV Line ");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(PMIArcGISMapActivity33KV.this, PMIHomeActivity.class); //Intent.ACTION_VIEW);
                startActivity(playIntent);
                finish();

            }
        });

        mv = (MapView) findViewById(R.id.map1);
        img_logout=(ImageButton)findViewById(R.id.img_logout);
        multi_circuit = findViewById(R.id.multi_circuit);

        try {
            gpsTraker = new GpsTraker(PMIArcGISMapActivity33KV.this);
            Intent intent = getIntent();
            ssName = intent.getStringExtra("SSNAME");
            feederName = intent.getStringExtra("FEEDER_NAME");
            feederCode = intent.getStringExtra("FEEDER_CODE");
            maxCount = intent.getStringExtra("MAX_COUNT");
            surveyType = intent.getStringExtra("SURVEY_TYPE");
            work_order = intent.getStringExtra("WORKORDER");
            gpsTraker.getLocation();

            arcGISDatabase = new ArcGISDatabase33KV(PMIArcGISMapActivity33KV.this);
            surveyVoList = arcGISDatabase.getData();
            count = arcGISDatabase.getObjectId();
            poleCountCheck = arcGISDatabase.getObjectIdCount();
            mGraphicsOverlay = new GraphicsOverlay();
            mv.getGraphicsOverlays().add(mGraphicsOverlay);

            currentLocmGraphicsOverla = new GraphicsOverlay();
            mv.getGraphicsOverlays().add(currentLocmGraphicsOverla);

            img_logout.setVisibility(View.VISIBLE);
            multi_circuit.setVisibility(View.GONE);
            img_logout.setOnClickListener(this);
            maxSurveyCount = Integer.parseInt(maxCount);


            map = new ArcGISMap(Basemap.Type.IMAGERY_WITH_LABELS, gpsTraker.getLatitude(), gpsTraker.getLongitude(), 20);
            mv.setMap(map);

            mLocationDisplay = mv.getLocationDisplay();

            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            sessionId=preferences.getString(MobileConstants.SESSION_ID,"");
            if(sessionId ==null || sessionId.isEmpty()){
                Intent intentLogin=new Intent(PMIArcGISMapActivity33KV.this, LoginActivity.class);
                startActivity(intentLogin);
                finish();
            }


            mLocationDisplay.addDataSourceStatusChangedListener(dataSourceStatusChangedEvent -> {
                if (dataSourceStatusChangedEvent.isStarted() || dataSourceStatusChangedEvent.getError() == null) {
                    return;
                }


                String message = String.format("Error in DataSourceStatusChangedListener: %s",
                        dataSourceStatusChangedEvent.getSource().getLocationDataSource().getError().getMessage());
                Toast.makeText(PMIArcGISMapActivity33KV.this, message, Toast.LENGTH_LONG).show();

            });
            // mLocationDisplay.stop();
            mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
            mLocationDisplay.startAsync();



            mv.setOnTouchListener(new DefaultMapViewOnTouchListener(this, mv) {
                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {

                    int x = (int) e.getX();
                    int y = (int) e.getY();
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
                    SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED,
                            12); //size 12, style of circle

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

                    // format coordinates to 4 decimal places


                    poleCountCheck = arcGISDatabase.getObjectIdCount();
                    count = arcGISDatabase.getObjectId();
                    if (poleCountCheck == 0) {
                        int polenum = maxSurveyCount + 1;
                        calloutContent.setText(String.valueOf(polenum));
                    } else {
                        calloutContent.setText(String.valueOf(count + 1));
                    }
                    // get callout, set content and show
                    mCallout = mMapView.getCallout();
                    mCallout.setLocation(myWGS84Point);
                    mCallout.setContent(calloutContent);
                    mCallout.show();

                    //DISTANCE COMMON
                    Location currentStartPoint=new Location("locationA");
                    currentStartPoint.setLatitude(gpsTraker.getLatitude());
                    currentStartPoint.setLongitude(gpsTraker.getLongitude());

                    Location currentEndPoint=new Location("locationA");
                    currentEndPoint.setLatitude(myWGS84Point.getY());
                    currentEndPoint.setLongitude(myWGS84Point.getX());
                    currentDistance = currentStartPoint.distanceTo(currentEndPoint);

                    if (count==0){
                        lastPoleDistance = 0;
                        isFirstPole = true;
                    }else {
                        isFirstPole = false;
                        //DISTANCE LAST POLE

                        if (surveyType.equalsIgnoreCase("Intermediate/Inline Pole")) {
                            Location lastPoleStartPoint=new Location("locationD");
                            lastPoleStartPoint.setLatitude(Double.parseDouble(PMIPoleSurveyActivity33KV.from_pole_lon));
                            lastPoleStartPoint.setLongitude(Double.parseDouble(PMIPoleSurveyActivity33KV.from_pole_lat));

                            Location lastPoleEndPoint=new Location("locationD");
                            lastPoleEndPoint.setLatitude(myWGS84Point.getY());
                            lastPoleEndPoint.setLongitude(myWGS84Point.getX());
                            lastPoleDistance = lastPoleStartPoint.distanceTo(lastPoleEndPoint);
                        }else {
                            Location lastPoleStartPoint=new Location("locationB");
                            lastPoleStartPoint.setLatitude(Double.parseDouble(surveyVoList.get(surveyVoList.size()-1).getLan()));
                            lastPoleStartPoint.setLongitude(Double.parseDouble(surveyVoList.get(surveyVoList.size()-1).getLat()));

                            Location lastPoleEndPoint=new Location("locationB");
                            lastPoleEndPoint.setLatitude(myWGS84Point.getY());
                            lastPoleEndPoint.setLongitude(myWGS84Point.getX());
                            lastPoleDistance = lastPoleStartPoint.distanceTo(lastPoleEndPoint);
                        }

                    }



                    PMICustomAllDialogs33KV greeting = new PMICustomAllDialogs33KV(PMIArcGISMapActivity33KV.this, String.valueOf(myWGS84Point.getX()),
                            String.valueOf(myWGS84Point.getY()), mCallout, graphicsOverlay, graphic, feederName,
                            feederCode, maxSurveyCount, tappingPoint, surveyType,currentLocmGraphicsOverla,currentLocGraphic,
                            ssName,lastPoleDistance,currentDistance,gpsTraker.getLatitude(),gpsTraker.getLongitude());
                    greeting.deligate = PMIArcGISMapActivity33KV.this;
                    greeting.show();

                    return true;
                }
            });

            multi_circuit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openMultiCircuitDialog();
                }
            });



        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        mv.pause();
    }
    @Override
    protected void onResume(){
        super.onResume();
        mv.resume();

        if (PMICustomAllDialogs33KV.isImageCaptured1){

            Uri imageUri = getLastImageUri(this);
            if (imageUri != null) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    PMICustomAllDialogs33KV.image_1.setImageBitmap(bitmap);
                    PMICustomAllDialogs33KV.image_1_clicked = true;
                    PMICustomAllDialogs33KV.isImageCaptured1 = false;
                    PMICustomAllDialogs33KV.image_1_base64 = getLastImagePath(this);
                    if (inputStream != null) inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (PMICustomAllDialogs33KV.isImageCaptured2){


            Uri imageUri = getLastImageUri(this);
            if (imageUri != null) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    PMICustomAllDialogs33KV.image_2.setImageBitmap(bitmap);
                    PMICustomAllDialogs33KV.image_2_clicked = true;
                    PMICustomAllDialogs33KV.isImageCaptured2 = false;
                    PMICustomAllDialogs33KV.image_2_base64 = getLastImagePath(this);
                    if (inputStream != null) inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    private String getLastImagePath(Context context){
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
        String[] projection = { MediaStore.Images.Media._ID };
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
    public void IsSetData(String data, String postion) {

        if (postion.equals("0")){

        }else{

        }

    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(PMIArcGISMapActivity33KV.this, PMIHomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        try {
            surveyVoList = arcGISDatabase.getData();
            count = arcGISDatabase.getObjectId();
            if (surveyVoList.size() > 0) {
                polylinePoints = new PointCollection(SpatialReferences.getWgs84());

                for (PMISurveyVo surveyVo : surveyVoList) {
                    ptWgs84 = new Point(Double.parseDouble(surveyVo.getLat()), Double.parseDouble(surveyVo.getLan()), 414, SpatialReferences.getWgs84());
                    Viewpoint vp = new Viewpoint(ptWgs84, 7500);
                    // String polenum=String.valueOf(surveyVo.getPole_no());
                    // set initial map extent
                    map.setInitialViewpoint(vp);

                    // set the map to be displayed in the mapview
                    mv.setMap(map);

                    //mv.removeView(mv);
                    graphicsOverlay = new GraphicsOverlay();
                    mv.getGraphicsOverlays().add(graphicsOverlay);

                    //[DocRef: Name=Point graphic with symbol, Category=Fundamentals, Topic=Symbols and Renderers]
                    //create a simple marker symbol
                    SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED,
                            12); //size 12, style of circle

                    //add a new graphic with a new point geometry
                    // Point graphicPoint = new Point(myWGS84Point.getX(), myWGS84Point.getY(), SpatialReferences.getWebMercator());
                    graphic = new Graphic(ptWgs84, symbol);
                    graph_objects.add(graphic);
                    graphicsOverlay.getGraphics().add(graphic);
                    graphicsOverlay.setLabelsEnabled(true);
                    //graphicsOverlay.toString(surveyVo.getPole_no());


                    // create a textview for the callout

                    calloutContent = new TextView(getApplicationContext());
                    calloutContent.setTextColor(Color.BLACK);
                    calloutContent.setSingleLine();

                    // format coordinates to 4 decimal places
                    calloutContent.setText(String.valueOf(surveyVo.getPole_no()));
                    // calloutContent.setText(surveyVo.getPole_no());

                    //calloutContent.setText("");

                    // get callout, set content and show
                    mCallout = mv.getCallout();
                    mCallout.setLocation(ptWgs84);
                    mCallout.setContent(calloutContent);
                    mCallout.show();

                    // mv.setViewpointCenterAsync(ptWgs84);

                    //Display Text

                    GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                    absoluteOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties.SurfacePlacement.ABSOLUTE);
                    absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, symbol));
                    // create a text symbol for elevation mode
                    TextSymbol absoluteText = new TextSymbol(15, String.valueOf(surveyVo.getPole_no()), Color.WHITE, TextSymbol.HorizontalAlignment.LEFT,
                            TextSymbol.VerticalAlignment.MIDDLE);
                    absoluteText.setOffsetY(20);
                    absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, absoluteText));
                    mv.getGraphicsOverlays().add(absoluteOverlay);



                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
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
                        new AlertDialog.Builder(PMIArcGISMapActivity33KV.this)
                                .setTitle("GPS Alert")
                                .setMessage("Please Enabl GPS")

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
                            Toast.makeText(PMIArcGISMapActivity33KV.this, message, Toast.LENGTH_LONG).show();

                        });
                        // mLocationDisplay.stop();
                        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
                        mLocationDisplay.startAsync();
                    }

                    break;
                }catch (Exception e){
                    e.printStackTrace();
                }
        }
    }


    private void openMultiCircuitDialog() {
        Dialog payDialog = new Dialog(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        payDialog.setContentView(R.layout.multi_circuit_dialog);
        payDialog.getWindow().setLayout(width,height/2);
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
                if (isChecked){
                    feeder_33.setVisibility(View.VISIBLE);
                   /* List<String> list_feeder_1 = new ArrayList<>();
                    list_feeder_1.addAll(SurveyHome33KV.filter33kv);
                    ArrayAdapter<String> adapter_feeder_1 = new ArrayAdapter<String>(ArcGISMapActivity.this, android.R.layout.simple_dropdown_item_1line, list_feeder_1);
                    sp_feeder_33.setAdapter(adapter_feeder_1);
                    sp_feeder_33.setSelection(0,true);*/

                    ArrayList<PMIMultiCktVO> filteredList = new ArrayList<>();
                    ArrayList<String> filter33kv = new ArrayList<>();

                    for (int j = 0; j < PMISurveyHome33KV.list33kv.size(); j++) {
                        if (!PMISurveyHome33KV.list33kv.get(j).getMC_FEEDER_ID().equals(feederCode)){
                            double distance = getDistanceBetweenPoles(PMISurveyHome33KV.list33kv.get(j).getGLAT(),PMISurveyHome33KV.list33kv.get(j).getGLON());
                            // System.out.println("DISTANCE 33KV FEEDER IN 33KV MULTICKT OUTER : "+distance);
                            if (distance<=500){
                                filteredList.add(PMISurveyHome33KV.list33kv.get(j));
                            }
                        }

                    }

                    List<String> combinedList = new ArrayList<>();
                    for(int i =0; i < filteredList.size(); i++) {
                        combinedList.add(filteredList.get(i).getSHOW_NAME());
                    }
                    Set<String> set_feeder_id_1 = new LinkedHashSet<>();
                    set_feeder_id_1.addAll(combinedList);
                    combinedList.clear();
                    combinedList.addAll(set_feeder_id_1);

                    if (!combinedList.isEmpty()){
                        for (int i = 0; i < combinedList.size(); i++) {
                            filter33kv.add(combinedList.get(i));
                        }
                    }

                    if (filter33kv.isEmpty()){
                        filter33kv.add("NO DATA");
                    }

                    ArrayAdapter<String> adapter_feeder_1 = new ArrayAdapter<String>(PMIArcGISMapActivity33KV.this, android.R.layout.simple_dropdown_item_1line, filter33kv);
                    sp_feeder_33.setAdapter(adapter_feeder_1);
                    sp_feeder_33.setSelection(0,true);



                }else {
                    feeder_33.setVisibility(View.GONE);
                }
            }
        });

        ch_multi_11kv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    feeder_11.setVisibility(View.VISIBLE);
                   /* List<String> list_feeder_1 = new ArrayList<>();
                    list_feeder_1.addAll(SurveyHome33KV.filter11kv);
                    ArrayAdapter<String> adapter_feeder_1 = new ArrayAdapter<String>(ArcGISMapActivity.this, android.R.layout.simple_dropdown_item_1line, list_feeder_1);
                    sp_feeder_11.setAdapter(adapter_feeder_1);
                    sp_feeder_11.setSelection(0,true);*/


                    ArrayList<PMIMultiCktVO> filteredList = new ArrayList<>();
                    ArrayList<String> filter33kv = new ArrayList<>();




                    for (int j = 0; j < PMISurveyHome33KV.list11kv.size(); j++) {

                        double distance = getDistanceBetweenPoles(PMISurveyHome33KV.list11kv.get(j).getGLAT(),PMISurveyHome33KV.list11kv.get(j).getGLON());
                        // System.out.println("DISTANCE 33KV FEEDER IN 11KV MULTICKT OUTER : "+distance);
                        if (distance<=500){
                            filteredList.add(PMISurveyHome33KV.list11kv.get(j));
                        }

                        /*if (!SurveyHome33KV.list11kv.get(j).getMC_FEEDER_ID().equals(feederCode)){
                            double distance = getDistanceBetweenPoles(SurveyHome33KV.list11kv.get(j).getGLAT(),SurveyHome33KV.list11kv.get(j).getGLON());
                            // System.out.println("DISTANCE 33KV FEEDER IN 11KV MULTICKT OUTER : "+distance);
                            if (distance<=500){
                                filteredList.add(SurveyHome33KV.list11kv.get(j));
                            }
                        }*/


                    }

                    List<String> combinedList = new ArrayList<>();
                    for(int i =0; i < filteredList.size(); i++) {
                        combinedList.add(filteredList.get(i).getSHOW_NAME());
                    }
                    Set<String> set_feeder_id_1 = new LinkedHashSet<>();
                    set_feeder_id_1.addAll(combinedList);
                    combinedList.clear();
                    combinedList.addAll(set_feeder_id_1);

                    if (!combinedList.isEmpty()){
                        for (int i = 0; i < combinedList.size(); i++) {
                            filter33kv.add(combinedList.get(i));
                        }
                    }

                    if (filter33kv.isEmpty()){
                        filter33kv.add("NO DATA");
                    }

                    ArrayAdapter<String> adapter_feeder_1 = new ArrayAdapter<String>(PMIArcGISMapActivity33KV.this, android.R.layout.simple_dropdown_item_1line, filter33kv);
                    sp_feeder_11.setAdapter(adapter_feeder_1);
                    sp_feeder_11.setSelection(0,true);


                }else {
                    feeder_11.setVisibility(View.GONE);
                }
            }
        });

        ch_multi_lt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    feeder_lt.setVisibility(View.VISIBLE);
                   /* List<String> list_feeder_1 = new ArrayList<>();
                    list_feeder_1.addAll(SurveyHome33KV.filterLT);
                    ArrayAdapter<String> adapter_feeder_1 = new ArrayAdapter<String>(ArcGISMapActivity.this, android.R.layout.simple_dropdown_item_1line, list_feeder_1);
                    sp_feeder_lt.setAdapter(adapter_feeder_1);
                    sp_feeder_lt.setSelection(0,true);*/


                    ArrayList<PMIMultiCktVO> filteredList = new ArrayList<>();
                    ArrayList<String> filter33kv = new ArrayList<>();

                    for (int j = 0; j < PMISurveyHome33KV.listLT.size(); j++) {
                        double distance = getDistanceBetweenPoles(PMISurveyHome33KV.listLT.get(j).getGLAT(),PMISurveyHome33KV.listLT.get(j).getGLON());
                        // System.out.println("DISTANCE 33KV FEEDER IN 11KV MULTICKT OUTER : "+distance);
                        if (distance<=500){
                            filteredList.add(PMISurveyHome33KV.listLT.get(j));
                        }
                    }

                    List<String> combinedList = new ArrayList<>();
                    for(int i =0; i < filteredList.size(); i++) {
                        combinedList.add(filteredList.get(i).getMC_FEEDER_NAME());
                    }
                    Set<String> set_feeder_id_1 = new LinkedHashSet<>();
                    set_feeder_id_1.addAll(combinedList);
                    combinedList.clear();
                    combinedList.addAll(set_feeder_id_1);

                    if (!combinedList.isEmpty()){
                        for (int i = 0; i < combinedList.size(); i++) {
                            filter33kv.add(combinedList.get(i));
                        }
                    }

                    if (filter33kv.isEmpty()){
                        filter33kv.add("NO DATA");
                    }

                    ArrayAdapter<String> adapter_feeder_1 = new ArrayAdapter<String>(PMIArcGISMapActivity33KV.this, android.R.layout.simple_dropdown_item_1line, filter33kv);
                    sp_feeder_lt.setAdapter(adapter_feeder_1);
                    sp_feeder_lt.setSelection(0,true);

                }else {
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
                if (ch_multi_33kv.isChecked() || ch_multi_11kv.isChecked() || ch_multi_lt.isChecked()){
                    payDialog.dismiss();

                    if (ch_multi_33kv.isChecked()){

                        if (sp_feeder_33.getSelectedItem().toString().equals("NO DATA")){
                            Toast.makeText(PMIArcGISMapActivity33KV.this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
                        }else {
                            loadMultiVoltageMap("33",sp_feeder_33.getSelectedItem().toString());
                        }


                    }
                    if (ch_multi_11kv.isChecked()){
                        if (sp_feeder_11.getSelectedItem().toString().equals("NO DATA")){
                            Toast.makeText(PMIArcGISMapActivity33KV.this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
                        }else {
                            loadMultiVoltageMap("11",sp_feeder_11.getSelectedItem().toString());
                        }

                    }
                    if (ch_multi_lt.isChecked()){
                        if (sp_feeder_lt.getSelectedItem().toString().equals("NO DATA")){
                            Toast.makeText(PMIArcGISMapActivity33KV.this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
                        }else {
                            loadMultiVoltageMap("lt",sp_feeder_lt.getSelectedItem().toString());;
                        }

                    }

                    // loadMultiCircuitData(ch_multi_33kv.isChecked(),ch_multi_11kv.isChecked(),ch_multi_lt.isChecked());
                }else {
                    Toast.makeText(PMIArcGISMapActivity33KV.this, "Please Choose at least 1 multi circuit", Toast.LENGTH_SHORT).show();
                }



            }
        });
    }

    private void loadMultiVoltageMap(String from, String selectedFeeder) {
        // graphicsOverlay.getGraphics().remove(graphic);



        ArrayList<PMIMultiCktVO> old_33_list = new ArrayList<>(PMISurveyHome33KV.list33kv);
        ArrayList<PMIMultiCktVO> list = new ArrayList<>();
        ArrayList<PMIMultiCktVO> old_list_11kv = new ArrayList<>(PMISurveyHome33KV.list11kv);
        ArrayList<PMIMultiCktVO> list_11kv = new ArrayList<>();
        ArrayList<PMIMultiCktVO> old_list_lt = new ArrayList<>(PMISurveyHome33KV.listLT);
        ArrayList<PMIMultiCktVO> list_lt = new ArrayList<>();





        if (from.equals("33")){
            String [] splitFeederId = selectedFeeder.split("~");
            selectedFeeder = splitFeederId[0];
            for (int j = 0; j < old_33_list.size(); j++) {
                if (selectedFeeder.equals(old_33_list.get(j).getMC_FEEDER_ID())){
                    double distance = getDistanceBetweenPoles(old_33_list.get(j).getGLAT(),old_33_list.get(j).getGLON());
                    //  System.out.println("DISTANCE 33KV FEEDER STATIC : "+distance);
                    if (distance<=200){
                        list.add(old_33_list.get(j));
                    }
                }

            }

            for (int i = 0; i<list.size() ; i++) {

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
                TextSymbol absoluteText = new TextSymbol(15, list.get(i).getMC_FEEDER_ID()+"-"+list.get(i).getMC_POLE_NO(), Color.WHITE, TextSymbol.HorizontalAlignment.LEFT,
                        TextSymbol.VerticalAlignment.MIDDLE);
                absoluteText.setOffsetY(20);
                absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, absoluteText));
                mv.getGraphicsOverlays().add(absoluteOverlay);

            }

        }

        if (from.equals("11")){
            String [] splitFeederId = selectedFeeder.split("~");
            selectedFeeder = splitFeederId[0];

            for (int j = 0; j < old_list_11kv.size(); j++) {
                if (selectedFeeder.equals(old_list_11kv.get(j).getMC_FEEDER_ID())){
                    double distance = getDistanceBetweenPoles(old_list_11kv.get(j).getGLAT(),old_list_11kv.get(j).getGLON());
                    //System.out.println("DISTANCE 11KV FEEDER STATIC : "+distance);
                    if (distance<=200){
                        list_11kv.add(old_list_11kv.get(j));
                    }
                }

            }

            for (int i = 0; i<list_11kv.size() ; i++) {
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
                TextSymbol absoluteText = new TextSymbol(15, list_11kv.get(i).getMC_FEEDER_ID()+"-"+list_11kv.get(i).getMC_POLE_NO(), Color.WHITE, TextSymbol.HorizontalAlignment.LEFT,
                        TextSymbol.VerticalAlignment.MIDDLE);
                absoluteText.setOffsetY(20);
                absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, absoluteText));
                mv.getGraphicsOverlays().add(absoluteOverlay);


            }

        }

        if (from.equals("lt")){

            for (int j = 0; j < old_list_lt.size(); j++) {
                if (selectedFeeder.equals(old_list_lt.get(j).getMC_FEEDER_NAME())){
                    double distance = getDistanceBetweenPoles(old_list_lt.get(j).getGLAT(),old_list_lt.get(j).getGLON());
                    //  System.out.println("DISTANCE LT FEEDER STATIC : "+distance);
                    if (distance<=200){
                        list_lt.add(old_list_lt.get(j));
                    }
                }


            }

            for (int i = 0; i<list_lt.size() ; i++) {
                // //System.out.println("11KV LAT : "+list_11kv.get(i).getGLAT());
                // //System.out.println("11KV LON : "+list_11kv.get(i).getGLON());
                ptWgs84 = new Point(Double.parseDouble(list_lt.get(i).getGLAT()), Double.parseDouble(list_lt.get(i).getGLON()), 414, SpatialReferences.getWgs84());
                Viewpoint vp = new Viewpoint(ptWgs84, 7500);
                map.setInitialViewpoint(vp);
                mv.setMap(map);
                graphicsOverlay_lt = new GraphicsOverlay();
                mv.getGraphicsOverlays().add(graphicsOverlay_lt);
                SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.rgb(3,251,13), 12);
                graphic_lt = new Graphic(ptWgs84, symbol);
                graph_objects.add(graphic_lt);
                graphicsOverlay_lt.getGraphics().add(graphic_lt);
                GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                absoluteOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties.SurfacePlacement.ABSOLUTE);
                absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, symbol));
                TextSymbol absoluteText = new TextSymbol(15, list_lt.get(i).getMC_FEEDER_NAME()+"-"+list_lt.get(i).getMC_POLE_NO(), Color.WHITE, TextSymbol.HorizontalAlignment.LEFT,
                        TextSymbol.VerticalAlignment.MIDDLE);
                absoluteText.setOffsetY(20);
                absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, absoluteText));
                mv.getGraphicsOverlays().add(absoluteOverlay);


            }


        }

    }

    private double getDistanceBetweenPoles(String polLat,String polLon){
        //System.out.println("CURRENT LOCATION LAT : "+gpsTraker.getLatitude());
        //System.out.println("CURRENT LOCATION LON : "+gpsTraker.getLongitude());
        //System.out.println("POLE LOCATION LAT : "+polLat);
        //System.out.println("POLE LOCATION LON : "+polLon);
        Location currentStartPoint = new Location("locationE");
        currentStartPoint.setLatitude(gpsTraker.getLatitude());
        currentStartPoint.setLongitude(gpsTraker.getLongitude());

        Location currentEndPoint = new Location("locationE");
        currentEndPoint.setLatitude(Double.parseDouble(polLon));
        currentEndPoint.setLongitude(Double.parseDouble(polLat));
        double currentDistance = currentStartPoint.distanceTo(currentEndPoint);
        return currentDistance;
    }



    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
             /*System.out.println("ResultCode :" + resultCode);
             System.out.println("RequestCode :" + requestCode);
             System.out.println("Data  :" + data);*/

            if (requestCode ==  PMICustomAllDialogs33KV.CAMERA_RESULT_CODE_IMAGE_1 && resultCode == RESULT_OK ) {

                /*int targetW = CustomAllDialogs33KV.image_1.getWidth();
                int targetH = CustomAllDialogs33KV.image_1.getHeight();

                // Get the dimensions of the bitmap
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(CustomAllDialogs33KV.currentPhotoPath_1, bmOptions);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;

                // Determine how much to scale down the image
                int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

                // Decode the image file into a Bitmap sized to fill the view
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                bmOptions.inPurgeable = true;*/

                Bitmap bitmap = BitmapFactory.decodeFile(PMICustomAllDialogs33KV.currentPhotoPath_1);
                PMICustomAllDialogs33KV.image_1.setImageBitmap(bitmap);
                //PMICustomAllDialogs33KV.image_1.setRotation(90);




              /*
              Bitmap image_1_bitmap = (Bitmap) data.getExtras().get("data");
                CustomAllDialogs33KV.image_1.setImageBitmap(image_1_bitmap);
              Uri uri =  ConvertImage.getImageUri(ArcGISMapActivity33KV.this, image_1_bitmap);
                String path = ConvertImage.getPathFromURI(uri, ArcGISMapActivity33KV.this);
                CustomAllDialogs33KV.image_1_base64 = path;*/
                PMICustomAllDialogs33KV.image_1_base64 = PMICustomAllDialogs33KV.currentPhotoPath_1;
                PMICustomAllDialogs33KV.image_1_clicked = true;

            }  else if (requestCode == PMICustomAllDialogs33KV.CAMERA_RESULT_CODE_IMAGE_2 && resultCode == RESULT_OK /*&& data != null*/) {

                Bitmap bitmap = BitmapFactory.decodeFile(PMICustomAllDialogs33KV.currentPhotoPath_2);
                PMICustomAllDialogs33KV.image_2.setImageBitmap(bitmap);
               // PMICustomAllDialogs33KV.image_2.setRotation(90);
                PMICustomAllDialogs33KV.image_2_base64 = PMICustomAllDialogs33KV.currentPhotoPath_2;
                PMICustomAllDialogs33KV.image_2_clicked = true;

                /*Bitmap image_2_bitmap = (Bitmap) data.getExtras().get("data");
                CustomAllDialogs33KV.image_2.setImageBitmap(image_2_bitmap);

                Uri uri =  ConvertImage.getImageUri(ArcGISMapActivity33KV.this, image_2_bitmap);
                String path = ConvertImage.getPathFromURI(uri, ArcGISMapActivity33KV.this);
                CustomAllDialogs33KV.image_2_base64 = path;
                CustomAllDialogs33KV.image_2_clicked = true;*/



            } else if (requestCode == PMICustomAllDialogs33KV.CAMERA_RESULT_CODE_IMAGE_3 && resultCode == RESULT_OK && data != null) {
                Bitmap image_3_bitmap = (Bitmap) data.getExtras().get("data");
                PMICustomAllDialogs33KV.image_3.setImageBitmap(image_3_bitmap);

                Uri uri =  ConvertImage.getImageUri(PMIArcGISMapActivity33KV.this, image_3_bitmap);
                String path = ConvertImage.getPathFromURI(uri, PMIArcGISMapActivity33KV.this);
                PMICustomAllDialogs33KV.image_3_base64 = path;
                PMICustomAllDialogs33KV.image_3_clicked = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}