package com.apepdcl.gis_survey.activitys;

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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.HomeActivity;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.module_11KV.ConvertImage;
import com.apepdcl.gis_survey.module_11KV.models.SurveyVo;
import com.apepdcl.gis_survey.pmi_old.module_11kv.PMICustomAllDialogs11KV;
import com.apepdcl.gis_survey.popup.CustomAllDialogs;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.utilits.GpsTraker;
import com.apepdcl.gis_survey.vo.DistandeVo;
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

public class ArcGISMapActivity extends AppCompatActivity implements View.OnClickListener {
    private MapView mv;
    ArcGISMap map;
    ArrayList<Graphic> graph_objects = new ArrayList<Graphic>();
    GraphicsOverlay graphicsOverlay, graphicsOverlay_33, graphicsOverlay_11, graphicsOverlay_lt;
    //GraphicsLayer graphicsLayer = new GraphicsLayer();
    private GraphicsOverlay mGraphicsOverlay;
    private GraphicsOverlay currentLocmGraphicsOverla;
    Graphic graphic, graphic_33, graphic_11, graphic_lt;
    Graphic currentLocGraphic;
    Button btndelete;
    private Callout mCallout;
    GpsTraker gpsTraker;
    public static float gpsAccuracy=0;
    private String ssName, feederName, feederCode, maxCount, tappingPoint, surveyType, sessionId, dtrCode, ltCircuit;
    private ArcGISDatabase arcGISDatabase;
    private List<SurveyDataVo> surveyVoList;
    private Point ptWgs84;
    private Point dtrPoint;
    private int i, count, maxSurveyCount, poleCountCheck;
    private SharedPreferences preferences;
    PointCollection polylinePoints;
    private ImageButton img_logout;
    private LocationDisplay mLocationDisplay;
    TextView calloutContent;
    private double distance;
    private double gLat = 0.0, gLong = 0.0;
    double lastPoleDistance;
    LinearLayout multi_circuit;
    int count_33 = 0;
    int count_11 = 0;
    int count_lt = 0;
    ArrayList<GraphicsOverlay> GO_33, GO_11, GO_LT;
    ArrayList<Graphic> GH_33, GH_11, GH_LT;
    boolean isFirstPole = false;
    private RelativeLayout reload_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gismap_activity);
        mv = (MapView) findViewById(R.id.map1);
        img_logout = (ImageButton) findViewById(R.id.img_logout);
        multi_circuit = findViewById(R.id.multi_circuit);
        reload_layout= findViewById(R.id.reload_layout);
        try {
            gpsTraker = new GpsTraker(ArcGISMapActivity.this);
            Intent intent = getIntent();
            dtrCode = intent.getStringExtra("DTRCODE");
            ltCircuit = intent.getStringExtra("CIRCUIT_CODE");
            maxCount = intent.getStringExtra("MAX_COUNT");
            gLat = Double.parseDouble(intent.getStringExtra("GLAT"));
            gLong = Double.parseDouble(intent.getStringExtra("GLONG"));

            // System.out.println("FROM PREVIOUS dtrCode: "+dtrCode);
            // System.out.println("FROM PREVIOUS ltCircuit: "+ltCircuit);
            // System.out.println("FROM PREVIOUS maxCount: "+maxCount);
            // System.out.println("FROM PREVIOUS gLat: "+gLat);
            // System.out.println("FROM PREVIOUS gLat: "+gLong);


            /*intent.putExtra("GLAT",gLat);
            intent.putExtra("GLONG",gLong);*/
            gpsTraker.getLocation();

            arcGISDatabase = new ArcGISDatabase(ArcGISMapActivity.this);


            surveyVoList = arcGISDatabase.getOnlyRequiredData("");
            count = arcGISDatabase.getObjectId();
            poleCountCheck = arcGISDatabase.getObjectIdCount();
            mGraphicsOverlay = new GraphicsOverlay();
            mv.getGraphicsOverlays().add(mGraphicsOverlay);
            currentLocmGraphicsOverla = new GraphicsOverlay();
            mv.getGraphicsOverlays().add(currentLocmGraphicsOverla);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("LT");
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent playIntent = new Intent(ArcGISMapActivity.this, HomeActivity.class); //Intent.ACTION_VIEW);
                    startActivity(playIntent);
                    finish();

                }
            });
            //  setSupportActionBar(toolbar);
            img_logout.setVisibility(View.VISIBLE);
            multi_circuit.setVisibility(View.VISIBLE);
            img_logout.setOnClickListener(this);
            if ("NO_DATA".equalsIgnoreCase(maxCount)) {
                maxSurveyCount = 0;
            } else {
                maxSurveyCount = Integer.parseInt(maxCount);
            }

            map = new ArcGISMap(Basemap.Type.IMAGERY_WITH_LABELS, gLat, gLong, 20);
            // map = new ArcGISMap(Basemap.Type.IMAGERY_WITH_LABELS, gpsTraker.getLatitude(), gpsTraker.getLongitude(), 20);
            mv.setMap(map);
            mLocationDisplay = mv.getLocationDisplay();
            SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.SQUARE, Color.CYAN, 16);
            symbol.setOutline(new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.RED, 1));
            graphicsOverlay = new GraphicsOverlay();
            mv.getGraphicsOverlays().add(graphicsOverlay);
            dtrPoint = new Point(gLat, gLong, 414, SpatialReferences.getWgs84());
            graphic = new Graphic(dtrPoint, symbol);
            graph_objects.add(graphic);
            graphicsOverlay.getGraphics().add(graphic);

//To display text
            GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
            absoluteOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties.SurfacePlacement.ABSOLUTE);
            absoluteOverlay.getGraphics().add(new Graphic(dtrPoint, symbol));
            // create a text symbol for elevation mode
            TextSymbol absoluteText = new TextSymbol(15, "DTR-" + dtrCode, Color.WHITE, TextSymbol.HorizontalAlignment.LEFT,
                    TextSymbol.VerticalAlignment.MIDDLE);
            absoluteText.setOffsetY(20);
            absoluteOverlay.getGraphics().add(new Graphic(dtrPoint, absoluteText));
            mv.getGraphicsOverlays().add(absoluteOverlay);


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

                    int x = (int) e.getX();
                    int y = (int) e.getY();
                    gpsAccuracy=gpsTraker.getLocation().getAccuracy();
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

                    List<DistandeVo> distandeVos = arcGISDatabase.getDistance();

                    surveyVoList = arcGISDatabase.getOnlyRequiredData("");
                    if (surveyVoList.size() > 0) {
                        //Toast.makeText(ArcGISMapActivity.this,distandeVos.get(0).getPOLE_NO(),Toast.LENGTH_SHORT).show();
                        //Distance
                        Location startPoint = new Location("locationA");
                        startPoint.setLatitude(Double.parseDouble(distandeVos.get(0).getLAT()));
                        startPoint.setLongitude(Double.parseDouble(distandeVos.get(0).getLAN()));

                        Location endPoint = new Location("locationB");
                        endPoint.setLatitude(myWGS84Point.getX());
                        endPoint.setLongitude(myWGS84Point.getY());

                        distance = startPoint.distanceTo(endPoint);
                        // Toast.makeText(ArcGISMapActivity.this,String.valueOf(distance),Toast.LENGTH_SHORT).show();

                        //    Log.v("Distance--->", String.valueOf(distance));



                      /*  //Esri calculate Distance
                        // Define geographic map points for the cities of Edinburgh (Scotland) and Dar es Salaam (Tanzania).
                        Point edinburghGeographic = new Point(Double.parseDouble(distandeVos.get(0).getLAT()), Double.parseDouble(distandeVos.get(0).getLAN()), SpatialReferences.getWgs84());
                        Point darEsSalaamGeographic = new Point(myWGS84Point.getX(), myWGS84Point.getX(), SpatialReferences.getWgs84());

// Create a world equidistant cylindrical spatial reference for measuring planar distance.
                        SpatialReference equidistantSpatialRef = SpatialReference.create(54002);

// Project the points from geographic to the projected coordinate system.
                        Point edinburghProjected = (Point) GeometryEngine.project(edinburghGeographic, equidistantSpatialRef);
                        Point darEsSalaamProjected = (Point) GeometryEngine.project(darEsSalaamGeographic, equidistantSpatialRef);

// Get the planar distance between the points in the spatial reference unit (meters).
                        double planarDistanceMeters = GeometryEngine.distanceBetween(edinburghProjected, darEsSalaamProjected);

                        Toast.makeText(ArcGISMapActivity.this,String.valueOf(planarDistanceMeters),Toast.LENGTH_SHORT).show();

                        Log.v("ESri Distance--->", String.valueOf(planarDistanceMeters));
// Result = 7,372,671.29511302 (around 7,372.67 kilometers)*/


                    }


                    // get callout, set content and show
                    mCallout = mMapView.getCallout();
                    mCallout.setLocation(myWGS84Point);
                    mCallout.setContent(calloutContent);
                    mCallout.show();

                    //DISTANCE COMMON
                    Location currentStartPoint = new Location("locationA");
                    currentStartPoint.setLatitude(gpsTraker.getLatitude());
                    currentStartPoint.setLongitude(gpsTraker.getLongitude());

                    Location currentEndPoint = new Location("locationA");
                    currentEndPoint.setLatitude(myWGS84Point.getY());
                    currentEndPoint.setLongitude(myWGS84Point.getX());
                    double currentDistance = currentStartPoint.distanceTo(currentEndPoint);

                    if (count == 0) {

                        isFirstPole = true;
                        Location startPoint = new Location("locationD");
                        startPoint.setLatitude(myWGS84Point.getX());
                        startPoint.setLongitude(myWGS84Point.getY());

                        Location endPoint = new Location("locationD");
                        endPoint.setLatitude(gLat);
                        endPoint.setLongitude(gLong);

                        lastPoleDistance = startPoint.distanceTo(endPoint);
                    } else {
                        Location lastPoleStartPoint = new Location("locationB");
                        lastPoleStartPoint.setLatitude(Double.parseDouble(surveyVoList.get(surveyVoList.size() - 1).getLAN()));
                        lastPoleStartPoint.setLongitude(Double.parseDouble(surveyVoList.get(surveyVoList.size() - 1).getLAT()));
                        Location lastPoleEndPoint = new Location("locationB");
                        lastPoleEndPoint.setLatitude(myWGS84Point.getY());
                        lastPoleEndPoint.setLongitude(myWGS84Point.getX());
                        lastPoleDistance = lastPoleStartPoint.distanceTo(lastPoleEndPoint);
                    }


                    CustomAllDialogs greeting = new CustomAllDialogs(ArcGISMapActivity.this, String.valueOf(myWGS84Point.getX()),
                            String.valueOf(myWGS84Point.getY()), mCallout, graphicsOverlay, graphic, feederName,
                            feederCode, maxSurveyCount, tappingPoint, surveyType, currentLocmGraphicsOverla,
                            currentLocGraphic, ssName, dtrCode, ltCircuit, distance,
                            lastPoleDistance, currentDistance, gpsTraker.getLatitude(), gpsTraker.getLongitude());
                    greeting.show();

                    return true;
                }
            });


            //REMOVE POINTS
            GO_33 = new ArrayList<>();
            GO_11 = new ArrayList<>();
            GO_LT = new ArrayList<>();
            GH_33 = new ArrayList<>();
            GH_11 = new ArrayList<>();
            GH_LT = new ArrayList<>();

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

    @Override
    protected void onPause() {
        super.onPause();
        mv.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mv.resume();

        if (CustomAllDialogs.isImageCaptured1) {


            Uri imageUri = getLastImageUri(this);
            if (imageUri != null) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    CustomAllDialogs.image_1.setImageBitmap(bitmap);
                    CustomAllDialogs.image_1_clicked = true;
                    CustomAllDialogs.isImageCaptured1 = false;
                    CustomAllDialogs.image_1_base64 = getLastImagePath(this);
                    if (inputStream != null) inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (CustomAllDialogs.isImageCaptured2) {


            Uri imageUri = getLastImageUri(this);
            if (imageUri != null) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    CustomAllDialogs.image_2.setImageBitmap(bitmap);
                    CustomAllDialogs.image_2_clicked = true;
                    CustomAllDialogs.isImageCaptured2 = false;
                    CustomAllDialogs.image_2_base64 = getLastImagePath(this);
                    if (inputStream != null) inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    private void showNearbyPoles() {
        try {
            // Clear previous overlays
            mv.getGraphicsOverlays().clear();

            // Get all poles from database
            List<SurveyDataVo> allPoles = arcGISDatabase.getOnlyRequiredData("");

            if (allPoles == null || allPoles.isEmpty()) {
                Toast.makeText(this, "No poles found in database", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create overlay for nearby poles
            GraphicsOverlay graphicsOverlay = new GraphicsOverlay();
            mv.getGraphicsOverlays().add(graphicsOverlay);

            int nearbyCount = 0;

            for (SurveyDataVo pole : allPoles) {
                double poleLat = Double.parseDouble(pole.getLAT());
                double poleLon = Double.parseDouble(pole.getLAN());

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
                            String.valueOf(pole.getPOLE_NO()),
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
   /* @Override
    public void IsSetData(String data, String postion) {

        if (postion.equals("0")){

        }else{

        }

    }*/

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ArcGISMapActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // System.out.println("ResultCode :" + resultCode);
            // System.out.println("RequestCode :" + requestCode);
            // System.out.println("Data  :" + data);

            if (requestCode == CustomAllDialogs.CAMERA_RESULT_CODE_IMAGE_1 && resultCode == RESULT_OK /*&& data != null*/) {
               /* Bitmap image_1_bitmap = (Bitmap) data.getExtras().get("data");
                CustomAllDialogs.image_1.setImageBitmap(image_1_bitmap);

                Uri uri =  ConvertImage.getImageUri(ArcGISMapActivity.this, image_1_bitmap);
                String path = ConvertImage.getPathFromURI(uri,ArcGISMapActivity.this);
                CustomAllDialogs.image_1_base64 = path;
                CustomAllDialogs.image_1_clicked = true;*/

                Bitmap bitmap = BitmapFactory.decodeFile(CustomAllDialogs.currentPhotoPath_1);
                CustomAllDialogs.image_1.setImageBitmap(bitmap);
                CustomAllDialogs.image_1.setRotation(90);
                CustomAllDialogs.image_1_base64 = CustomAllDialogs.currentPhotoPath_1;
                CustomAllDialogs.image_1_clicked = true;

            } else if (requestCode == CustomAllDialogs.CAMERA_RESULT_CODE_IMAGE_2 && resultCode == RESULT_OK /*&& data != null*/) {
                /*Bitmap image_2_bitmap = (Bitmap) data.getExtras().get("data");
                CustomAllDialogs.image_2.setImageBitmap(image_2_bitmap);

                Uri uri =  ConvertImage.getImageUri(ArcGISMapActivity.this, image_2_bitmap);
                String path = ConvertImage.getPathFromURI(uri,ArcGISMapActivity.this);
                CustomAllDialogs.image_2_base64 = path;
                CustomAllDialogs.image_2_clicked = true;*/

                Bitmap bitmap = BitmapFactory.decodeFile(CustomAllDialogs.currentPhotoPath_2);
                CustomAllDialogs.image_2.setImageBitmap(bitmap);
                CustomAllDialogs.image_2.setRotation(90);
                CustomAllDialogs.image_2_base64 = CustomAllDialogs.currentPhotoPath_2;
                CustomAllDialogs.image_2_clicked = true;

            } else if (requestCode == CustomAllDialogs.CAMERA_RESULT_CODE_IMAGE_3 && resultCode == RESULT_OK && data != null) {
                Bitmap image_3_bitmap = (Bitmap) data.getExtras().get("data");
                CustomAllDialogs.image_3.setImageBitmap(image_3_bitmap);

                Uri uri = ConvertImage.getImageUri(ArcGISMapActivity.this, image_3_bitmap);
                String path = ConvertImage.getPathFromURI(uri, ArcGISMapActivity.this);
                CustomAllDialogs.image_3_base64 = path;
                CustomAllDialogs.image_3_clicked = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        try {
            surveyVoList = arcGISDatabase.getOnlyRequiredData("");
            count = arcGISDatabase.getObjectId();

            if (surveyVoList.size() > 0) {
                polylinePoints = new PointCollection(SpatialReferences.getWgs84());

                double userLat = gpsTraker.getLatitude();
                double userLon = gpsTraker.getLongitude();

                // ✅ Initialize map and overlays once
                mv.setMap(map);
                graphicsOverlay = new GraphicsOverlay();
                mv.getGraphicsOverlays().add(graphicsOverlay);

                // optional: zoom map to user location
                Point userPoint = new Point(userLon, userLat, SpatialReferences.getWgs84());
                Viewpoint startViewpoint = new Viewpoint(userPoint, 5000);
                map.setInitialViewpoint(startViewpoint);

                for (SurveyDataVo surveyVo : surveyVoList) {
                    double poleLat = Double.parseDouble(surveyVo.getLAN()); // true latitude
                    double poleLon = Double.parseDouble(surveyVo.getLAT()); // true longitude

                    double distance = getDistanceBetweenPolesNew(
                            String.valueOf(poleLon),
                            String.valueOf(poleLat),
                            String.valueOf(userLon),
                            String.valueOf(userLat)
                    );

                    Log.d("MAP_FILTER", "Pole " + surveyVo.getPOLE_NO() + " Distance = " + distance);

                    if (distance > 5000000) {
                        continue;
                    }

                    // --- Add visible point ---
                    ptWgs84 = new Point(poleLon, poleLat, 414, SpatialReferences.getWgs84());
                    SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 12);
                    Graphic graphic = new Graphic(ptWgs84, symbol);
                    graphicsOverlay.getGraphics().add(graphic);

                    // --- Add text label ---
                    TextSymbol textSymbol = new TextSymbol(
                            15,
                            String.valueOf(surveyVo.getPOLE_NO()),
                            Color.WHITE,
                            TextSymbol.HorizontalAlignment.CENTER,
                            TextSymbol.VerticalAlignment.MIDDLE
                    );
                    textSymbol.setOffsetY(20);
                    Graphic textGraphic = new Graphic(ptWgs84, textSymbol);
                    graphicsOverlay.getGraphics().add(textGraphic);
                }

                // ✅ Optionally center map on user
                mv.setViewpointCenterAsync(userPoint, 2000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//OLD
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//
//        try {
//            surveyVoList = arcGISDatabase.getOnlyRequiredData("");
//            count = arcGISDatabase.getObjectId();
//            if (surveyVoList.size() > 0) {
//                polylinePoints = new PointCollection(SpatialReferences.getWgs84());
//
//                for (SurveyDataVo surveyVo : surveyVoList) {
//                    ptWgs84 = new Point(Double.parseDouble(surveyVo.getLAT()), Double.parseDouble(surveyVo.getLAN()), 414, SpatialReferences.getWgs84());
//                    Viewpoint vp = new Viewpoint(ptWgs84, 7500);
//                    // String polenum=String.valueOf(surveyVo.getPole_no());
//                    // set initial map extent
//                    map.setInitialViewpoint(vp);
//
//                    // set the map to be displayed in the mapview
//                    mv.setMap(map);
//
//                    //mv.removeView(mv);
//                    graphicsOverlay = new GraphicsOverlay();
//                    mv.getGraphicsOverlays().add(graphicsOverlay);
//
//                    //[DocRef: Name=Point graphic with symbol, Category=Fundamentals, Topic=Symbols and Renderers]
//                    //create a simple marker symbol
//                    SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED,
//                            12); //size 12, style of circle
//
//                    //add a new graphic with a new point geometry
//                    // Point graphicPoint = new Point(myWGS84Point.getX(), myWGS84Point.getY(), SpatialReferences.getWebMercator());
//                    graphic = new Graphic(ptWgs84, symbol);
//                    graph_objects.add(graphic);
//                    graphicsOverlay.getGraphics().add(graphic);
//                    graphicsOverlay.setLabelsEnabled(true);
//                    //graphicsOverlay.toString(surveyVo.getPole_no());
//
//
//                    // create a textview for the callout
//
//                    calloutContent = new TextView(getApplicationContext());
//                    calloutContent.setTextColor(Color.BLACK);
//                    calloutContent.setSingleLine();
//
//                    // format coordinates to 4 decimal places
//                    calloutContent.setText(String.valueOf(surveyVo.getPOLE_NO()));
//                    // calloutContent.setText(surveyVo.getPole_no());
//
//                    //calloutContent.setText("");
//
//                    // get callout, set content and show
//                    mCallout = mv.getCallout();
//                    mCallout.setLocation(ptWgs84);
//                    mCallout.setContent(calloutContent);
//                    mCallout.show();
//
//                    // mv.setViewpointCenterAsync(ptWgs84);
//
//                    List<DistandeVo> distandeVos = arcGISDatabase.getDistance();
//
//
//                   /* //Distance
//                    Location startPoint=new Location("locationA");
//                    startPoint.setLatitude(Double.parseDouble(distandeVos.get(0).getLAT()));
//                    startPoint.setLongitude(Double.parseDouble(distandeVos.get(0).getLAN()));
//
//                    Location endPoint=new Location("locationB");
//                    endPoint.setLatitude(17.7366);
//                    endPoint.setLongitude(83.3085);
//
//                    double distance=startPoint.distanceTo(endPoint);
//
//                    Log.v("Distance--->", String.valueOf(distance));
//*/
//
//                    //Display Text
//
//                    GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
//                    absoluteOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties.SurfacePlacement.ABSOLUTE);
//                    absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, symbol));
//                    // create a text symbol for elevation mode
//                    TextSymbol absoluteText = new TextSymbol(15, String.valueOf(surveyVo.getPOLE_NO()), Color.WHITE, TextSymbol.HorizontalAlignment.LEFT,
//                            TextSymbol.VerticalAlignment.MIDDLE);
//                    absoluteText.setOffsetY(20);
//                    absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, absoluteText));
//                    mv.getGraphicsOverlays().add(absoluteOverlay);
//
//
//                }
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

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
                    /*List<String> list_feeder_1 = new ArrayList<>();
                    list_feeder_1.addAll(DTRSelectActivity.filter33kv);
                    ArrayAdapter<String> adapter_feeder_1 = new ArrayAdapter<String>(ArcGISMapActivity.this, android.R.layout.simple_dropdown_item_1line, list_feeder_1);
                    sp_feeder_33.setAdapter(adapter_feeder_1);
                    sp_feeder_33.setSelection(0,true);*/

                    ArrayList<MultiCktVO> filteredList = new ArrayList<>();
                    ArrayList<String> filter33kv = new ArrayList<>();

                    for (int j = 0; j < DTRSelectActivity.list33kv.size(); j++) {
                        double distance = getDistanceBetweenPoles(DTRSelectActivity.list33kv.get(j).getGLAT(), DTRSelectActivity.list33kv.get(j).getGLON());
                        // System.out.println("DISTANCE 33KV FEEDER IN 11KV MULTICKT OUTER : "+distance);
                        if (distance <= 500) {
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
                    /*List<String> list_feeder_1 = new ArrayList<>();
                    list_feeder_1.addAll(DTRSelectActivity.filter11kv);
                    ArrayAdapter<String> adapter_feeder_1 = new ArrayAdapter<String>(ArcGISMapActivity.this, android.R.layout.simple_dropdown_item_1line, list_feeder_1);
                    sp_feeder_11.setAdapter(adapter_feeder_1);
                    sp_feeder_11.setSelection(0,true);*/

                    ArrayList<MultiCktVO> filteredList = new ArrayList<>();
                    ArrayList<String> filter33kv = new ArrayList<>();

                    for (int j = 0; j < DTRSelectActivity.list11kv.size(); j++) {
                        double distance = getDistanceBetweenPoles(DTRSelectActivity.list11kv.get(j).getGLAT(), DTRSelectActivity.list11kv.get(j).getGLON());
                        // System.out.println("DISTANCE 33KV FEEDER IN 11KV MULTICKT OUTER : "+distance);
                        if (distance <= 500) {
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
                    list_feeder_1.addAll(DTRSelectActivity.filterLT);
                    ArrayAdapter<String> adapter_feeder_1 = new ArrayAdapter<String>(ArcGISMapActivity.this, android.R.layout.simple_dropdown_item_1line, list_feeder_1);
                    sp_feeder_lt.setAdapter(adapter_feeder_1);
                    sp_feeder_lt.setSelection(0,true);*/


                    ArrayList<MultiCktVO> filteredList = new ArrayList<>();
                    ArrayList<String> filter33kv = new ArrayList<>();

                    for (int j = 0; j < DTRSelectActivity.listLT.size(); j++) {
                        double distance = getDistanceBetweenPoles(DTRSelectActivity.listLT.get(j).getGLAT(), DTRSelectActivity.listLT.get(j).getGLON());
                        // System.out.println("DISTANCE 33KV FEEDER IN 11KV MULTICKT OUTER : "+distance);
                        if (distance <= 500) {
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

                   /* if (ch_multi_33kv.isChecked()){
                        loadMultiVoltageMap("33",sp_feeder_33.getSelectedItem().toString());
                    }
                    if (ch_multi_11kv.isChecked()){
                        loadMultiVoltageMap("11",sp_feeder_11.getSelectedItem().toString());
                    }
                    if (ch_multi_lt.isChecked()){
                        loadMultiVoltageMap("lt",sp_feeder_lt.getSelectedItem().toString());
                    }*/


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

        if (count_33 > 0) {
            for (int j = 0; j < GH_33.size(); j++) {
                // System.out.println("33KV ID "+GH_33.get(j).getAttributes().get("ID"));
                // GO_33.get(j).getGraphics().remove(GH_33.get(j));
                boolean isRemoved = mv.getGraphicsOverlays().remove(GO_33.get(j));
                // System.out.println("33KV REMOVED : "+isRemoved);

            }


        }
        if (count_11 > 0) {
            for (int j = 0; j < GH_11.size(); j++) {
                // GO_11.get(j).getGraphics().remove(GH_11.get(j));
                mv.getGraphicsOverlays().remove(GO_11.get(j));
                //  mv.invalidate();
            }

        }
        if (count_lt > 0) {
            for (int j = 0; j < GH_LT.size(); j++) {
                GO_LT.get(j).getGraphics().remove(GH_LT.get(j));
            }
        }

        ArrayList<MultiCktVO> old_33_list = new ArrayList<>(DTRSelectActivity.list33kv);
        ArrayList<MultiCktVO> list = new ArrayList<>();
        ArrayList<MultiCktVO> old_list_11kv = new ArrayList<>(DTRSelectActivity.list11kv);
        ArrayList<MultiCktVO> list_11kv = new ArrayList<>();
        ArrayList<MultiCktVO> old_list_lt = new ArrayList<>(DTRSelectActivity.listLT);
        ArrayList<MultiCktVO> list_lt = new ArrayList<>();

        GO_33.clear();
        GO_11.clear();
        GO_LT.clear();
        GH_33.clear();
        GH_11.clear();
        GH_LT.clear();

        if (from.equals("33")) {
            String[] splitFeederId = selectedFeeder.split("~");
            selectedFeeder = splitFeederId[0];
            for (int j = 0; j < old_33_list.size(); j++) {
                if (selectedFeeder.equals(old_33_list.get(j).getMC_FEEDER_ID())) {
                    double distance = getDistanceBetweenPoles(old_33_list.get(j).getGLAT(), old_33_list.get(j).getGLON());
                    //System.out.println("DISTANCE 33KV FEEDER STATIC : "+distance);
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

                GO_33.add(graphicsOverlay_33);
                GH_33.add(graphic_33);
            }
            count_33++;
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

                GO_11.add(graphicsOverlay_11);
                GH_11.add(graphic_11);
            }
            count_11++;
        }

        if (from.equals("lt")) {

            for (int j = 0; j < old_list_lt.size(); j++) {
                if (selectedFeeder.equals(old_list_lt.get(j).getMC_FEEDER_NAME())) {
                    double distance = getDistanceBetweenPoles(old_list_lt.get(j).getGLAT(), old_list_lt.get(j).getGLON());
                    // System.out.println("DISTANCE LT FEEDER STATIC : "+distance);
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

                GO_LT.add(graphicsOverlay_lt);
                GH_LT.add(graphic_lt);
            }

            count_lt++;
        }

    }

    private double getDistanceBetweenPoles(String polLat, String polLon) {
        //System.out.println("CURRENT LOCATION LAT : "+gpsTraker.getLatitude());
        //System.out.println("CURRENT LOCATION LON : "+gpsTraker.getLongitude());
        //System.out.println("POLE LOCATION LAT : "+polLat);
        //System.out.println("POLE LOCATION LON : "+polLon);
        Location currentStartPoint = new Location("locationC");
        currentStartPoint.setLatitude(gpsTraker.getLatitude());
        currentStartPoint.setLongitude(gpsTraker.getLongitude());

        Location currentEndPoint = new Location("locationC");
        currentEndPoint.setLatitude(Double.parseDouble(polLon));
        currentEndPoint.setLongitude(Double.parseDouble(polLat));
        double currentDistance = currentStartPoint.distanceTo(currentEndPoint);
        return currentDistance;
    }

}
