package com.apepdcl.gis_survey.estimates;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apepdcl.gis_survey.DashBoard;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.estimates.models.Estimates11KVModel;
import com.apepdcl.gis_survey.estimates.models.Estimates33KVModel;
import com.apepdcl.gis_survey.estimates.models.EstimatesLTModel;
import com.apepdcl.gis_survey.module_11KV.ImpData;
import com.apepdcl.gis_survey.utilits.GpsTraker;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polyline;
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

import java.util.ArrayList;

public class ArcGISMapActivityEstimates extends AppCompatActivity implements ImpData {

    RelativeLayout back_layout, current_location_layout;
    MapView mv;
    GpsTraker gpsTraker;
    GraphicsOverlay mGraphicsOverlay, graphicsOverlay;
    ArcGISMap map;
    private Point ptWgs84;
    PointCollection polylinePoints;
    LocationDisplay mLocationDisplay;
    Graphic graphic;
    TextView calloutContent;
    Callout mCallout;
    int poleNumber33 = 100;
    int poleNumber11 = 100;
    int poleNumberLT = 100;
    String EST_NO, TYPE;
    public static boolean IS_33, IS_11, IS_LT;
    String REF_ID_33, REF_ID_11, REF_ID_LT;
    ArcGISDatabaseEstimates databaseEstimates;
    ArrayList<Estimates33KVModel> list33KV;
    ArrayList<Estimates11KVModel> list11KV;
    ArrayList<EstimatesLTModel> listLT;

    public static boolean IS_33_COMPLETED = false;
    public static boolean IS_11_COMPLETED = false;
    public static boolean IS_LT_COMPLETED = false;
    public static boolean IS_LT_CKT_COMPLETED = false;
    TextView present_survey;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arc_gismap_estimates);
        getSupportActionBar().hide();


        EST_NO = getIntent().getStringExtra("EST_NO");
        TYPE = getIntent().getStringExtra("TYPE");
        IS_33 = getIntent().getBooleanExtra("IS_33", false);
        IS_11 = getIntent().getBooleanExtra("IS_11", false);
        IS_LT = getIntent().getBooleanExtra("IS_LT", false);
        databaseEstimates = new ArcGISDatabaseEstimates(this);
        if (IS_33) {
            IS_33_COMPLETED = databaseEstimates.getSurveyStatus(EST_NO, "33KV");
           // System.out.println("IS_33_COMPLETED : " + IS_33_COMPLETED);
            REF_ID_33 = databaseEstimates.getReferenceID(EST_NO, "33KV");
            if (TYPE.equalsIgnoreCase("NEW")) {
                poleNumber33 = 0;
            }
        }
        if (IS_11) {
            IS_11_COMPLETED = databaseEstimates.getSurveyStatus(EST_NO, "11KV");
            REF_ID_11 = databaseEstimates.getReferenceID(EST_NO, "11KV");
            if (TYPE.equalsIgnoreCase("NEW")) {
                poleNumber11 = 0;
            }
        }
        if (IS_LT) {
            IS_LT_COMPLETED = databaseEstimates.getSurveyStatus(EST_NO, "LT");
            REF_ID_LT = databaseEstimates.getReferenceID(EST_NO, "LT");
            if (TYPE.equalsIgnoreCase("NEW")) {
                poleNumberLT = 0;
            }

        }


        back_layout = findViewById(R.id.back_layout);
        current_location_layout = findViewById(R.id.close_layout);
        mv = findViewById(R.id.map1);
        present_survey = findViewById(R.id.present_survey);

        gpsTraker = new GpsTraker(this);
        mGraphicsOverlay = new GraphicsOverlay();
        mv.getGraphicsOverlays().add(mGraphicsOverlay);
        map = new ArcGISMap(Basemap.Type.IMAGERY_WITH_LABELS, gpsTraker.getLatitude(), gpsTraker.getLongitude(), 20);
        mv.setMap(map);
        mLocationDisplay = mv.getLocationDisplay();
        mLocationDisplay.addDataSourceStatusChangedListener(dataSourceStatusChangedEvent -> {
            if (dataSourceStatusChangedEvent.isStarted() || dataSourceStatusChangedEvent.getError() == null) {
                return;
            }


            String message = String.format("Error in DataSourceStatusChangedListener: %s",
                    dataSourceStatusChangedEvent.getSource().getLocationDataSource().getError().getMessage());
            Toast.makeText(ArcGISMapActivityEstimates.this, message, Toast.LENGTH_LONG).show();

        });
        // mLocationDisplay.stop();
        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
        mLocationDisplay.startAsync();

        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        current_location_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentLocation();
            }
        });


        mv.setOnTouchListener(new DefaultMapViewOnTouchListener(this, mv) {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {

                int x = (int) e.getX();
                int y = (int) e.getY();
                android.graphics.Point clickedPoint = new android.graphics.Point(x, y);
                Point myWebMercatorPoint = mv.screenToLocation(clickedPoint);
                Point myWGS84Point = (Point) GeometryEngine.project(myWebMercatorPoint, SpatialReferences.getWgs84());
                Viewpoint vp = new Viewpoint(myWGS84Point, 7500);
                map.setInitialViewpoint(vp);
                mv.setMap(map);
                graphicsOverlay = new GraphicsOverlay();
                mv.getGraphicsOverlays().add(graphicsOverlay);

                SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CROSS, Color.RED, 12); //size 12, style of circle
                graphic = new Graphic(myWGS84Point, symbol);
                graphicsOverlay.getGraphics().add(graphic);

                // create a textview for the callout
                TextView calloutContent = new TextView(getApplicationContext());
                calloutContent.setTextColor(Color.BLACK);
                calloutContent.setSingleLine();

                // format coordinates to 4 decimal places

                if (IS_33 && !IS_33_COMPLETED) {
                    if (poleNumber33 == 0) {
                        calloutContent.setText(String.valueOf(1));
                        poleNumber33++;
                    } else {
                        ArrayList<Estimates33KVModel> list = databaseEstimates.get33KVSurveyData(EST_NO);
                        if (list.size() == 0) {
                            poleNumber33 = 0;
                            calloutContent.setText(String.valueOf(1));
                            poleNumber33++;
                        } else {
                            poleNumber33 = Integer.parseInt(list.get(list.size() - 1).getPOLE_NO());
                            poleNumber33++;
                            calloutContent.setText(String.valueOf(poleNumber33));
                        }
                    }

                    mCallout = mMapView.getCallout();
                    mCallout.setLocation(myWGS84Point);
                    mCallout.setContent(calloutContent);
                    mCallout.show();

                    SurveyDialog33KV greeting = new SurveyDialog33KV(ArcGISMapActivityEstimates.this, String.valueOf(myWGS84Point.getX()),
                            String.valueOf(myWGS84Point.getY()), mCallout, graphicsOverlay, graphic, gpsTraker.getLatitude(), gpsTraker.getLongitude(),
                            EST_NO, REF_ID_33, poleNumber33);
                    greeting.show();

                }

                if (IS_11) {
                    if (IS_33) {
                        if (IS_33_COMPLETED && !IS_11_COMPLETED) {
                            if (poleNumber11 == 0) {
                                calloutContent.setText(String.valueOf(1));
                                poleNumber11++;
                            } else {
                                ArrayList<Estimates11KVModel> list = databaseEstimates.get11KVSurveyData(EST_NO);
                                if (list.size() == 0) {
                                    poleNumber11 = 0;
                                    calloutContent.setText(String.valueOf(1));
                                    poleNumber11++;
                                } else {
                                    poleNumber11 = Integer.parseInt(list.get(list.size() - 1).getPOLE_NO());
                                    poleNumber11++;
                                    calloutContent.setText(String.valueOf(poleNumber11));
                                }

                            }

                            mCallout = mMapView.getCallout();
                            mCallout.setLocation(myWGS84Point);
                            mCallout.setContent(calloutContent);
                            mCallout.show();

                            SurveyDialog11KV greeting = new SurveyDialog11KV(ArcGISMapActivityEstimates.this, String.valueOf(myWGS84Point.getX()),
                                    String.valueOf(myWGS84Point.getY()), mCallout, graphicsOverlay, graphic, gpsTraker.getLatitude(), gpsTraker.getLongitude(),
                                    EST_NO, REF_ID_11, poleNumber11);
                            greeting.show();

                        }
                    } else {
                        if (!IS_11_COMPLETED) {
                            if (poleNumber11 == 0) {
                                calloutContent.setText(String.valueOf(1));
                                poleNumber11++;
                            } else {
                                ArrayList<Estimates11KVModel> list = databaseEstimates.get11KVSurveyData(EST_NO);
                                if (list.size() == 0) {
                                    poleNumber11 = 0;
                                    calloutContent.setText(String.valueOf(1));
                                    poleNumber11++;
                                } else {
                                    poleNumber11 = Integer.parseInt(list.get(list.size() - 1).getPOLE_NO());
                                    poleNumber11++;
                                    calloutContent.setText(String.valueOf(poleNumber11));
                                }

                            }

                            mCallout = mMapView.getCallout();
                            mCallout.setLocation(myWGS84Point);
                            mCallout.setContent(calloutContent);
                            mCallout.show();

                            SurveyDialog11KV greeting = new SurveyDialog11KV(ArcGISMapActivityEstimates.this, String.valueOf(myWGS84Point.getX()),
                                    String.valueOf(myWGS84Point.getY()), mCallout, graphicsOverlay, graphic, gpsTraker.getLatitude(), gpsTraker.getLongitude(),
                                    EST_NO, REF_ID_11, poleNumber11);
                            greeting.show();

                        }
                    }

                }

                if (IS_LT) {
                    if (IS_33) {
                        if (IS_33_COMPLETED) {
                            if (IS_11 && IS_11_COMPLETED && !IS_LT_COMPLETED) {
                                if (poleNumberLT == 0) {
                                    calloutContent.setText(String.valueOf(1));
                                    poleNumberLT++;
                                } else {
                                    if (IS_LT_CKT_COMPLETED) {
                                        poleNumberLT = 0;
                                        calloutContent.setText(String.valueOf(1));
                                        poleNumberLT++;
                                    } else {
                                        ArrayList<EstimatesLTModel> list = databaseEstimates.getLTSurveyData(EST_NO);
                                        if (list.size() == 0) {
                                            poleNumberLT = 0;
                                            calloutContent.setText(String.valueOf(1));
                                            poleNumberLT++;
                                        } else {
                                            poleNumberLT = Integer.parseInt(list.get(list.size() - 1).getPOLE_NO());
                                            poleNumberLT++;
                                            calloutContent.setText(String.valueOf(poleNumberLT));
                                        }
                                    }


                                }

                                mCallout = mMapView.getCallout();
                                mCallout.setLocation(myWGS84Point);
                                mCallout.setContent(calloutContent);
                                mCallout.show();

                                SurveyDialogLT greeting = new SurveyDialogLT(ArcGISMapActivityEstimates.this, String.valueOf(myWGS84Point.getX()),
                                        String.valueOf(myWGS84Point.getY()), mCallout, graphicsOverlay, graphic, gpsTraker.getLatitude(), gpsTraker.getLongitude(),
                                        EST_NO, REF_ID_LT, poleNumberLT);
                                greeting.show();
                            }
                        }

                    } else {
                        if (IS_11) {
                            if (IS_11_COMPLETED && !IS_LT_COMPLETED) {
                                if (poleNumberLT == 0) {
                                    calloutContent.setText(String.valueOf(1));
                                    poleNumberLT++;
                                } else {

                                    if (IS_LT_CKT_COMPLETED) {
                                        poleNumberLT = 0;
                                        calloutContent.setText(String.valueOf(1));
                                        poleNumberLT++;
                                    } else {
                                        ArrayList<EstimatesLTModel> list = databaseEstimates.getLTSurveyData(EST_NO);
                                        if (list.size() == 0) {
                                            poleNumberLT = 0;
                                            calloutContent.setText(String.valueOf(1));
                                            poleNumberLT++;
                                        } else {
                                            poleNumberLT = Integer.parseInt(list.get(list.size() - 1).getPOLE_NO());
                                            poleNumberLT++;
                                            calloutContent.setText(String.valueOf(poleNumberLT));
                                        }
                                    }


                                }

                                mCallout = mMapView.getCallout();
                                mCallout.setLocation(myWGS84Point);
                                mCallout.setContent(calloutContent);
                                mCallout.show();

                                SurveyDialogLT greeting = new SurveyDialogLT(ArcGISMapActivityEstimates.this, String.valueOf(myWGS84Point.getX()),
                                        String.valueOf(myWGS84Point.getY()), mCallout, graphicsOverlay, graphic, gpsTraker.getLatitude(), gpsTraker.getLongitude(),
                                        EST_NO, REF_ID_LT, poleNumberLT);
                                greeting.show();
                            }
                        } else {
                            if (!IS_LT_COMPLETED) {
                                if (poleNumberLT == 0) {
                                    calloutContent.setText(String.valueOf(1));
                                    poleNumberLT++;
                                } else {

                                    if (IS_LT_CKT_COMPLETED) {
                                        poleNumberLT = 0;
                                        calloutContent.setText(String.valueOf(1));
                                        poleNumberLT++;
                                    } else {
                                        ArrayList<EstimatesLTModel> list = databaseEstimates.getLTSurveyData(EST_NO);
                                        if (list.size() == 0) {
                                            poleNumberLT = 0;
                                            calloutContent.setText(String.valueOf(1));
                                            poleNumberLT++;
                                        } else {
                                            poleNumberLT = Integer.parseInt(list.get(list.size() - 1).getPOLE_NO());
                                            poleNumberLT++;
                                            calloutContent.setText(String.valueOf(poleNumberLT));
                                        }
                                    }


                                }

                                mCallout = mMapView.getCallout();
                                mCallout.setLocation(myWGS84Point);
                                mCallout.setContent(calloutContent);
                                mCallout.show();

                                SurveyDialogLT greeting = new SurveyDialogLT(ArcGISMapActivityEstimates.this, String.valueOf(myWGS84Point.getX()),
                                        String.valueOf(myWGS84Point.getY()), mCallout, graphicsOverlay, graphic, gpsTraker.getLatitude(), gpsTraker.getLongitude(),
                                        EST_NO, REF_ID_LT, poleNumberLT);
                                greeting.show();
                            }
                        }
                    }
                }


                return true;
            }
        });

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        try {
            list33KV = databaseEstimates.get33KVSurveyData(EST_NO);
            if (list33KV.size() > 0) {
                polylinePoints = new PointCollection(SpatialReferences.getWgs84());

                for (Estimates33KVModel surveyVo : list33KV) {
                    polylinePoints.add(new Point(Double.parseDouble(surveyVo.getLATITUDE()), Double.parseDouble(surveyVo.getLONGITUDE())));
                    ptWgs84 = new Point(Double.parseDouble(surveyVo.getLATITUDE()), Double.parseDouble(surveyVo.getLONGITUDE()), 414, SpatialReferences.getWgs84());
                    Viewpoint vp = new Viewpoint(ptWgs84, 7500);
                    map.setInitialViewpoint(vp);
                    mv.setMap(map);
                    graphicsOverlay = new GraphicsOverlay();
                    mv.getGraphicsOverlays().add(graphicsOverlay);
                    SimpleMarkerSymbol symbol = null;
                    if (surveyVo.getPOLE_SURVEY_TYPE().equalsIgnoreCase("EXISTING")) {
                        symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 17);
                    } else {
                        symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CROSS, Color.RED, 25);
                        if (surveyVo.getIS_MULTI_CKT().equalsIgnoreCase("Yes")) {
                            symbol.setOutline(new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.WHITE, 2));
                        }
                    }

                    //size 12, style of circle

                    graphic = new Graphic(ptWgs84, symbol);
                    graphicsOverlay.getGraphics().add(graphic);
                    graphicsOverlay.setLabelsEnabled(true);

                    calloutContent = new TextView(getApplicationContext());
                    calloutContent.setTextColor(Color.BLACK);
                    calloutContent.setSingleLine();

                    calloutContent.setText(String.valueOf(surveyVo.getPOLE_NO()));
                    mCallout = mv.getCallout();
                    mCallout.setLocation(ptWgs84);
                    mCallout.setContent(calloutContent);
                    mCallout.show();

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

                Polyline polyline = new Polyline(polylinePoints);
                SimpleLineSymbol polylineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.RED, 4.0f);
                Graphic polylineGraphic = new Graphic(polyline, polylineSymbol);
                mGraphicsOverlay.getGraphics().add(polylineGraphic);
                polylinePoints = new PointCollection(SpatialReferences.getWgs84());


            }
            list11KV = databaseEstimates.get11KVSurveyData(EST_NO);
            if (list11KV.size() > 0) {
                polylinePoints = new PointCollection(SpatialReferences.getWgs84());


                for (Estimates11KVModel surveyVo : list11KV) {


                    polylinePoints.add(new Point(Double.parseDouble(surveyVo.getLATITUDE()), Double.parseDouble(surveyVo.getLONGITUDE())));
                    ptWgs84 = new Point(Double.parseDouble(surveyVo.getLATITUDE()), Double.parseDouble(surveyVo.getLONGITUDE()), 414, SpatialReferences.getWgs84());
                    Viewpoint vp = new Viewpoint(ptWgs84, 7500);
                    map.setInitialViewpoint(vp);

                    // set the map to be displayed in the mapview
                    mv.setMap(map);

                    //mv.removeView(mv);

                    if (!surveyVo.getPOLE_CONFIG().equalsIgnoreCase("From 33")) {
                        graphicsOverlay = new GraphicsOverlay();
                        mv.getGraphicsOverlays().add(graphicsOverlay);
                        SimpleMarkerSymbol symbol = null;
                        if (surveyVo.getPOLE_SURVEY_TYPE().equalsIgnoreCase("EXISTING")) {
                            symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.GREEN, 17);
                        } else {
                            symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CROSS, Color.GREEN, 25);
                            if (surveyVo.getIS_MULTI_CKT().equalsIgnoreCase("Yes")) {
                                symbol.setOutline(new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.WHITE, 2));
                            }
                        }
                        graphic = new Graphic(ptWgs84, symbol);
                        graphicsOverlay.getGraphics().add(graphic);
                        graphicsOverlay.setLabelsEnabled(true);

                        calloutContent = new TextView(getApplicationContext());
                        calloutContent.setTextColor(Color.BLACK);
                        calloutContent.setSingleLine();


                        if (surveyVo.getPOLE_CONFIG().equalsIgnoreCase("DTR Pole")) {
                            calloutContent.setText(String.valueOf("DTR-" + surveyVo.getPOLE_NO()));
                        } else {
                            calloutContent.setText(String.valueOf(surveyVo.getPOLE_NO()));
                        }

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
                        TextSymbol absoluteText = new TextSymbol(15,
                                surveyVo.getPOLE_CONFIG().equalsIgnoreCase("DTR Pole") ? String.valueOf("DTR-" + surveyVo.getPOLE_NO()) : String.valueOf(surveyVo.getPOLE_NO()),
                                Color.WHITE, TextSymbol.HorizontalAlignment.LEFT,
                                TextSymbol.VerticalAlignment.MIDDLE);
                        absoluteText.setOffsetY(20);
                        absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, absoluteText));
                        mv.getGraphicsOverlays().add(absoluteOverlay);


                    }


                }

                Polyline polyline = new Polyline(polylinePoints);
                SimpleLineSymbol polylineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.GREEN, 4.0f);
                Graphic polylineGraphic = new Graphic(polyline, polylineSymbol);
                mGraphicsOverlay.getGraphics().add(polylineGraphic);
                polylinePoints = new PointCollection(SpatialReferences.getWgs84());


            }
            listLT = databaseEstimates.getLTSurveyData(EST_NO);
            if (listLT.size() > 0) {

                polylinePoints = new PointCollection(SpatialReferences.getWgs84());
                for (EstimatesLTModel surveyVo : listLT) {

                    polylinePoints.add(new Point(Double.parseDouble(surveyVo.getLATITUDE()), Double.parseDouble(surveyVo.getLONGITUDE())));
                    ptWgs84 = new Point(Double.parseDouble(surveyVo.getLATITUDE()), Double.parseDouble(surveyVo.getLONGITUDE()), 414, SpatialReferences.getWgs84());
                    Viewpoint vp = new Viewpoint(ptWgs84, 7500);
                    map.setInitialViewpoint(vp);
                  //  System.out.println("POLE CONFIG LT : " + surveyVo.getPOLE_CONFIG());
                    // set the map to be displayed in the mapview
                    mv.setMap(map);

                    if (!surveyVo.getPOLE_CONFIG().equalsIgnoreCase("Fake")) {
                        //mv.removeView(mv);


                        graphicsOverlay = new GraphicsOverlay();
                        mv.getGraphicsOverlays().add(graphicsOverlay);
                      //  System.out.println("POLE CONFIG LT INSIDE : " + surveyVo.getPOLE_CONFIG());
                        //[DocRef: Name=Point graphic with symbol, Category=Fundamentals, Topic=Symbols and Renderers]
                        //create a simple marker symbol
                        SimpleMarkerSymbol symbol;
                        if (surveyVo.getPOLE_SURVEY_TYPE().equalsIgnoreCase("EXISTING")) {
                            symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE,
                                    surveyVo.getPOLE_CONFIG().equalsIgnoreCase("From DTR") ? Color.GREEN : Color.CYAN,
                                    17);
                        } else {
                            symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CROSS,
                                    surveyVo.getPOLE_CONFIG().equalsIgnoreCase("From DTR") ? Color.GREEN : Color.CYAN,
                                    25);
                            if (surveyVo.getIS_MULTI_CKT().equalsIgnoreCase("Yes")) {
                                symbol.setOutline(new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.WHITE, 2));
                            }
                        }
                        //add a new graphic with a new point geometry
                        // Point graphicPoint = new Point(myWGS84Point.getX(), myWGS84Point.getY(), SpatialReferences.getWebMercator());
                        graphic = new Graphic(ptWgs84, symbol);
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


                        Polyline polyline = new Polyline(polylinePoints);
                        SimpleLineSymbol polylineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.CYAN, 4.0f);
                        Graphic polylineGraphic = new Graphic(polyline, polylineSymbol);
                        mGraphicsOverlay.getGraphics().add(polylineGraphic);

                        if (surveyVo.getPOLE_CONFIG().equalsIgnoreCase("From DTR")) {
                            mGraphicsOverlay.getGraphics().remove(polylineGraphic);
                            polylinePoints = new PointCollection(SpatialReferences.getWgs84());
                        }


                    }


                }


                // polylinePoints = new PointCollection(SpatialReferences.getWgs84());


            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setCurrentLocation() {
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
                new AlertDialog.Builder(ArcGISMapActivityEstimates.this)
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
                    Toast.makeText(ArcGISMapActivityEstimates.this, message, Toast.LENGTH_LONG).show();

                });
                // mLocationDisplay.stop();
                mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
                mLocationDisplay.startAsync();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent playIntent = new Intent(ArcGISMapActivityEstimates.this, DashBoard.class); //Intent.ACTION_VIEW);
        startActivity(playIntent);
        finish();
    }

    @Override
    public void IsSetData(String data, String postion) {

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
    }
}