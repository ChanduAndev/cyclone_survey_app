package com.apepdcl.gis_survey.incremental;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.popup.IncrementalDTRCustomDialogs;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.utilits.GpsTraker;
import com.apepdcl.gis_survey.vo.IncrementalSurveyDataVo;
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

import java.util.ArrayList;
import java.util.List;
import com.apepdcl.gis_survey.HomeActivity;

public class IncrementalDTRGISMapActivity extends AppCompatActivity implements  View.OnClickListener {
    private MapView mv;
    ArcGISMap map;
    ArrayList<Graphic> graph_objects = new ArrayList<Graphic>();
    GraphicsOverlay graphicsOverlay;
    //GraphicsLayer graphicsLayer = new GraphicsLayer();
    private GraphicsOverlay mGraphicsOverlay;
    private GraphicsOverlay currentLocmGraphicsOverla;
    Graphic graphic;
    Graphic currentLocGraphic;
    Button btndelete;
    private Callout mCallout;
    GpsTraker gpsTraker;
    private String ssName,feederName,feederCode,maxCount,tappingPoint,surveyType,sessionId,dtrCode,ltCircuit;
    private ArcGISDatabase arcGISDatabase;
    private List<IncrementalSurveyDataVo> incrementalSurveyDataVoList;
    private Point ptWgs84;
    private int i,count,maxSurveyCount=0,poleCountCheck;
    private SharedPreferences preferences;
    PointCollection polylinePoints;
    private ImageButton img_logout;
    private LocationDisplay mLocationDisplay;
    TextView calloutContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incrementaldtrgismap);
        mv = (MapView) findViewById(R.id.map1);
        img_logout=(ImageButton)findViewById(R.id.img_logout);
        try {
            gpsTraker = new GpsTraker(IncrementalDTRGISMapActivity.this);
            Intent intent = getIntent();
            dtrCode=intent.getStringExtra("DTRCODE");
           // ltCircuit=intent.getStringExtra("CIRCUIT_CODE");
           // maxCount=intent.getStringExtra("MAX_COUNT");
            gpsTraker.getLocation();

            arcGISDatabase = new ArcGISDatabase(IncrementalDTRGISMapActivity.this);
            incrementalSurveyDataVoList = arcGISDatabase.getIncrementalSurveyData("");
            count = arcGISDatabase.getObjectId();
            poleCountCheck = arcGISDatabase.getObjectIdCount();
            mGraphicsOverlay = new GraphicsOverlay();
            mv.getGraphicsOverlays().add(mGraphicsOverlay);
            currentLocmGraphicsOverla = new GraphicsOverlay();
            mv.getGraphicsOverlays().add(currentLocmGraphicsOverla);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("GISLT Survey");
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent playIntent = new Intent(IncrementalDTRGISMapActivity.this, HomeActivity.class); //Intent.ACTION_VIEW);
                    startActivity(playIntent);
                    finish();

                }
            });
          //  setSupportActionBar(toolbar);
            img_logout.setVisibility(View.VISIBLE);
            img_logout.setOnClickListener(this);

         /*   if("NO_DATA".equalsIgnoreCase(maxCount) || "".equalsIgnoreCase(maxCount)) {
                maxSurveyCount =0;
            }else {
                maxSurveyCount=Integer.parseInt(maxCount);
            }*/


            map = new ArcGISMap(Basemap.Type.IMAGERY_WITH_LABELS, gpsTraker.getLatitude(), gpsTraker.getLongitude(), 20);
            mv.setMap(map);
            mLocationDisplay = mv.getLocationDisplay();

            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            sessionId=preferences.getString(MobileConstants.SESSION_ID,"");
            if(sessionId ==null || sessionId.isEmpty()){
                Intent intentLogin=new Intent(IncrementalDTRGISMapActivity.this, LoginActivity.class);
                startActivity(intentLogin);
                finish();
            }
            mLocationDisplay.addDataSourceStatusChangedListener(dataSourceStatusChangedEvent -> {
                if (dataSourceStatusChangedEvent.isStarted() || dataSourceStatusChangedEvent.getError() == null) {
                    return;
                }


                String message = String.format("Error in DataSourceStatusChangedListener: %s",
                        dataSourceStatusChangedEvent.getSource().getLocationDataSource().getError().getMessage());
                Toast.makeText(IncrementalDTRGISMapActivity.this, message, Toast.LENGTH_LONG).show();

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



                    IncrementalDTRCustomDialogs dtrCustomDialogs = new IncrementalDTRCustomDialogs(IncrementalDTRGISMapActivity.this, String.valueOf(myWGS84Point.getX()), String.valueOf(myWGS84Point.getY()), mCallout, graphicsOverlay, graphic, feederName, feederCode, maxSurveyCount, tappingPoint, surveyType,currentLocmGraphicsOverla,currentLocGraphic,ssName,dtrCode,ltCircuit);
                    dtrCustomDialogs.show();

                    return true;
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
    }
   /* @Override
    public void IsSetData(String data, String postion) {

        if (postion.equals("0")){

        }else{

        }

    }*/

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(IncrementalDTRGISMapActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        try {
            incrementalSurveyDataVoList = arcGISDatabase.getIncrementalSurveyData("");
            count = arcGISDatabase.getObjectId();
            if (incrementalSurveyDataVoList.size() > 0) {
                polylinePoints = new PointCollection(SpatialReferences.getWgs84());

                for (IncrementalSurveyDataVo surveyVo : incrementalSurveyDataVoList) {
                    ptWgs84 = new Point(Double.parseDouble(surveyVo.getLAT()), Double.parseDouble(surveyVo.getLAN()), 414, SpatialReferences.getWgs84());
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
                        calloutContent.setText(String.valueOf(surveyVo.getPOLE_NO()));
                       // calloutContent.setText(surveyVo.getPole_no());

                        //calloutContent.setText("");

                        // get callout, set content and show
                        mCallout = mv.getCallout();
                        mCallout.setLocation(ptWgs84);
                        mCallout.setContent(calloutContent);
                        mCallout.show();

                   // mv.setViewpointCenterAsync(ptWgs84);


                    //Distance
                    Location startPoint=new Location("locationA");
                    startPoint.setLatitude(17.7404);
                    startPoint.setLongitude(83.308);

                    Location endPoint=new Location("locationB");
                    endPoint.setLatitude(17.7366);
                    endPoint.setLongitude(83.3085);

                    double distance=startPoint.distanceTo(endPoint);

                   // Log.v("Distance--->", String.valueOf(distance));


                    //Display Text

                    GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                    absoluteOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties.SurfacePlacement.ABSOLUTE);
                    absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, symbol));
                    // create a text symbol for elevation mode
                    TextSymbol absoluteText = new TextSymbol(15, String.valueOf(surveyVo.getPOLE_NO()), Color.WHITE, TextSymbol.HorizontalAlignment.LEFT,
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
                        new AlertDialog.Builder(IncrementalDTRGISMapActivity.this)
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
                            Toast.makeText(IncrementalDTRGISMapActivity.this, message, Toast.LENGTH_LONG).show();

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

}
