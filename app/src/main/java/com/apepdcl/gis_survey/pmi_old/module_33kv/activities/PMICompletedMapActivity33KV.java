package com.apepdcl.gis_survey.pmi_old.module_33kv.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.pmi_old.module_11kv.activities.PMIHomeActivity;
import com.apepdcl.gis_survey.pmi_old.module_11kv.vo.PMISurveyVo;
import com.apepdcl.gis_survey.pmi_old.module_33kv.ArcGISDatabase33KV;
import com.apepdcl.gis_survey.pmi_old.module_33kv.PMIRetrofitClientInstance33kv;
import com.apepdcl.gis_survey.pmi_old.module_33kv.RetrofitService33kv;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.utilits.GpsTraker;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LayerSceneProperties;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PMICompletedMapActivity33KV extends AppCompatActivity {
    private ImageButton img_logout;

    private MapView mv;
    ArcGISMap map;
    ArrayList<Graphic> graph_objects = new ArrayList<Graphic>();
    GraphicsOverlay graphicsOverlay;
    //GraphicsLayer graphicsLayer = new GraphicsLayer();
    private GraphicsOverlay mGraphicsOverlay;
    Graphic graphic;
    Button btndelete;
    private Callout mCallout;
    GpsTraker gpsTraker;
    private String ssName, feederName, feederCode, maxCount, tappingPoint, surveyType, sessionId;
    private ArcGISDatabase33KV arcGISDatabase;
    private List<PMISurveyVo> surveyVoList;
    private Point ptWgs84;
    private int i, count, maxSurveyCount, poleCountCheck;
    private SharedPreferences preferences;
    private RetrofitService33kv retrofitService;
    private ProgressDialog progressDialog;
    PointCollection polylinePoints;
    LocationDisplay mLocationDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pmicompleted_map_activity33_kv);
        getSupportActionBar().hide();
        img_logout = (ImageButton) findViewById(R.id.img_logout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("33KV Survey");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(PMICompletedMapActivity33KV.this, PMIHomeActivity.class); //Intent.ACTION_VIEW);
                startActivity(playIntent);
                finish();

            }
        });


        try {
            mv = (MapView) findViewById(R.id.map1);
            //btndelete=(Button)findViewById(R.id.btndelete);
            gpsTraker = new GpsTraker(PMICompletedMapActivity33KV.this);
            gpsTraker.getLocation();
            mGraphicsOverlay = new GraphicsOverlay();
            mv.getGraphicsOverlays().add(mGraphicsOverlay);
            Intent intent = getIntent();
            if (intent != null) {
                feederCode = intent.getStringExtra("FEEDER_CODE");
            }
            progressDialog = new ProgressDialog(PMICompletedMapActivity33KV.this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            // arcGISDatabase=new ArcGISDatabase(CompletedMapActivity.this);

           /* GraphicsOverlay currentLocmGraphicsOverla = new GraphicsOverlay();
            mv.getGraphicsOverlays().add(currentLocmGraphicsOverla);*/

            map = new ArcGISMap(Basemap.Type.IMAGERY_WITH_LABELS, gpsTraker.getLatitude(), gpsTraker.getLongitude(), 20);
            mv.setMap(map);

            mLocationDisplay = mv.getLocationDisplay();

            mLocationDisplay.addDataSourceStatusChangedListener(dataSourceStatusChangedEvent -> {
                if (dataSourceStatusChangedEvent.isStarted() || dataSourceStatusChangedEvent.getError() == null) {
                    return;
                }


                String message = String.format("Error in DataSourceStatusChangedListener: %s",
                        dataSourceStatusChangedEvent.getSource().getLocationDataSource().getError().getMessage());
                Toast.makeText(PMICompletedMapActivity33KV.this, message, Toast.LENGTH_LONG).show();

            });
            mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
            mLocationDisplay.startAsync();

            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            sessionId = preferences.getString(MobileConstants.SESSION_ID, "");

            usrLogin();

            createPolygonGraphics();

            img_logout.setVisibility(View.VISIBLE);

            img_logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setCurrentLocation();
                }
            });

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
                new AlertDialog.Builder(PMICompletedMapActivity33KV.this)
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
                    Toast.makeText(PMICompletedMapActivity33KV.this, message, Toast.LENGTH_LONG).show();

                });
                // mLocationDisplay.stop();
                mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
                mLocationDisplay.startAsync();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void usrLogin() {
        try {
            if (ConnectivityChecking.checkConnection(PMICompletedMapActivity33KV.this)) {

                progressDialog.show();

                JsonObject postParam = new JsonObject();
                //postParam.addProperty("order",yourArray) ;
                postParam.addProperty("FEEDER_ID", feederCode);
                // postParam.addProperty("FEEDER_ID", "032111106");
                postParam.addProperty("IMEI_NO", CommanUtilits.getIMEI_NO(PMICompletedMapActivity33KV.this));
                postParam.addProperty("SESSION_ID", sessionId);
                postParam.addProperty("VERSION", MobileConstants.APP_VERSION_33_KV);

                retrofitService = PMIRetrofitClientInstance33kv.getRetrofitInstance().create(RetrofitService33kv.class);
                Call<JsonObject> responseVoCall = retrofitService.getCompletedData(postParam);
                responseVoCall.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        progressDialog.dismiss();
                        if (response.body() != null) {
                            JsonObject jsonObject = response.body();
                            if ("TRUE".equalsIgnoreCase(jsonObject.get("STATUS").getAsString())) {
                                JsonObject responseObj = jsonObject.getAsJsonObject("RESPONSE");
                                Log.v("DATACOM==", "" + responseObj);
                                responseObj.entrySet();
                                polylinePoints = new PointCollection(SpatialReferences.getWgs84());
                                List<String> keys = new ArrayList<>();
                                Set<Map.Entry<String, JsonElement>> entries = responseObj.entrySet();//will return members of your object
                                for (Map.Entry<String, JsonElement> entry : entries) {
                                    keys.add(entry.getKey());
                                    //  System.out.println(entry.getKey());
                                }
                                Log.v("KEYS", "" + keys.size());
                                for (String s : keys) {
                                    JsonArray elements = responseObj.getAsJsonArray(s);

                                    for (int i = 0; elements.size() > i; i++) {
                                        JsonObject data = elements.get(i).getAsJsonObject();
                                        //Double  lat=data.get("lat").getAsDouble();
                                        //  Double  lon=data.get("long").getAsDouble();
                                        Log.v("DATA==", "" + data);

                                        polylinePoints.add(new Point(data.get("lat").getAsDouble(), data.get("long").getAsDouble()));
                                        graphicsOverlay = new GraphicsOverlay();
                                        mv.getGraphicsOverlays().add(graphicsOverlay);
                                        // SimpleMarkerSymbol symbol = null;
                                        SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 12); //size 12, style of circle


                                        ptWgs84 = new Point(data.get("lat").getAsDouble(), data.get("long").getAsDouble(), 414, SpatialReferences.getWgs84());
                                        graphic = new Graphic(ptWgs84, symbol);
                                        graph_objects.add(graphic);
                                        graphicsOverlay.getGraphics().add(graphic);


                                        //Object  wo=data.get("woId");
                                        //  String  wo1=data.get("woId").toString();
                                        //  String sr=""+""+"";
                                        //String value = (wo == null ||  wo.equals("")) ? "": wo.toString();


                                       /* JsonElement nullableText = data.get("woId");
                                        String text = (nullableText instanceof JsonNull) ? "" : nullableText.getAsString();
                                       // if(value !=null && !"".equalsIgnoreCase(value) && !"null".equalsIgnoreCase(value) && !sr.equalsIgnoreCase(wo.toString())){
                                        if(text !=null && !"".equalsIgnoreCase(text)){
                                            symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.GREEN,
                                                        12); //size 12, style of circle
                                                ptWgs84 = new Point(data.get("lat").getAsDouble(), data.get("long").getAsDouble(), 414, SpatialReferences.getWgs84());
                                                graphic = new Graphic(ptWgs84, symbol);
                                                graph_objects.add(graphic);
                                                graphicsOverlay.getGraphics().add(graphic);

                                        }else {
                                            symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED,12); //size 12, style of circle
                                            ptWgs84 = new Point(data.get("lat").getAsDouble(), data.get("long").getAsDouble(), 414, SpatialReferences.getWgs84());
                                            graphic = new Graphic(ptWgs84, symbol);
                                            graph_objects.add(graphic);
                                            graphicsOverlay.getGraphics().add(graphic);
                                        }*/

                                            /* SimpleMarkerSymbol symbol = null;

                                            if(i==0) {
                                                     symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.GREEN,
                                                        12); //size 12, style of circle
                                            }else if(i==2){
                                                //MultiCircuit
                                                 symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.TRIANGLE, Color.RED,
                                                        20); //size 12, style of circle
                                            }else {
                                                symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.YELLOW,
                                                        12); //size 12, style of circle
                                            }*/

                                        //add a new graphic with a new point geometry
                                        // Point graphicPoint = new Point(myWGS84Point.getX(), myWGS84Point.getY(), SpatialReferences.getWebMercator());


                                        //DisplayText

                                        GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                                        absoluteOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties.SurfacePlacement.ABSOLUTE);
                                        absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, symbol));
                                        // create a text symbol for elevation mode
                                        TextSymbol absoluteText = new TextSymbol(15, data.get("poleno").getAsString(), Color.WHITE, TextSymbol.HorizontalAlignment.LEFT,
                                                TextSymbol.VerticalAlignment.MIDDLE);
                                        absoluteText.setOffsetY(20);
                                        absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, absoluteText));
                                        mv.getGraphicsOverlays().add(absoluteOverlay);
                                    }

                                   /* Polyline polyline = new Polyline(polylinePoints);
                                    SimpleLineSymbol polylineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 3.0f);
                                    Graphic polylineGraphic = new Graphic(polyline, polylineSymbol);
                                    mGraphicsOverlay.getGraphics().add(polylineGraphic);*/
                                    polylinePoints = new PointCollection(SpatialReferences.getWgs84());

                                      /*if(s.equalsIgnoreCase("1")){
                                          Polyline polyline = new Polyline(polylinePoints);
                                          SimpleLineSymbol polylineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 3.0f);
                                          Graphic polylineGraphic = new Graphic(polyline, polylineSymbol);
                                          mGraphicsOverlay.getGraphics().add(polylineGraphic);
                                          polylinePoints = new PointCollection(SpatialReferences.getWgs84());
                                      }else if(s.equalsIgnoreCase("2")){
                                          Polyline polyline = new Polyline(polylinePoints);
                                          SimpleLineSymbol polylineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.GREEN, 3.0f);
                                          Graphic polylineGraphic = new Graphic(polyline, polylineSymbol);
                                          mGraphicsOverlay.getGraphics().add(polylineGraphic);
                                          polylinePoints = new PointCollection(SpatialReferences.getWgs84());
                                      }else if(s.equalsIgnoreCase("3")){
                                          Polyline polyline = new Polyline(polylinePoints);
                                          SimpleLineSymbol polylineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.YELLOW, 3.0f);
                                          Graphic polylineGraphic = new Graphic(polyline, polylineSymbol);
                                          mGraphicsOverlay.getGraphics().add(polylineGraphic);
                                          polylinePoints = new PointCollection(SpatialReferences.getWgs84());
                                      }*/

                                }

                            } else {
                                Toast.makeText(PMICompletedMapActivity33KV.this, jsonObject.get("ERR_MSG").getAsString(), Toast.LENGTH_SHORT).show();
                                if ("No Records".equalsIgnoreCase(jsonObject.get("ERR_MSG").getAsString())) {
                                    Intent intent = new Intent(PMICompletedMapActivity33KV.this, PMIHomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                        } else {
                            Toast.makeText(PMICompletedMapActivity33KV.this, "Server Busy", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        progressDialog.dismiss();
                        //  progressDialog.dismiss();
                        Toast.makeText(PMICompletedMapActivity33KV.this, "Try Again", Toast.LENGTH_LONG).show();
                    }
                });


            } else {
                Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void createPolygonGraphics() {
        PointCollection polygonPoints = new PointCollection(SpatialReferences.getWgs84());
        polygonPoints.add(new Point(-118.70372100524446, 34.03519536420519));
        polygonPoints.add(new Point(-118.71766916267414, 34.03505116445459));
        polygonPoints.add(new Point(-118.71923322580597, 34.04919407570509));
        polygonPoints.add(new Point(-118.71631129436038, 34.04915962906471));
        polygonPoints.add(new Point(-118.71526020370266, 34.059921300916244));
        polygonPoints.add(new Point(-118.71153226844807, 34.06035488360282));
        polygonPoints.add(new Point(-118.70803735010169, 34.05014385296186));
        polygonPoints.add(new Point(-118.69877903513455, 34.045182336992816));
        polygonPoints.add(new Point(-118.6979656552508, 34.040267760924316));
        polygonPoints.add(new Point(-118.70259112469694, 34.038800278306674));
        polygonPoints.add(new Point(-118.70372100524446, 34.03519536420519));
        Polygon polygon = new Polygon(polygonPoints);
        SimpleFillSymbol polygonSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.rgb(226, 119, 40),
                new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.GREEN, 2.0f));
        Graphic polygonGraphic = new Graphic(polygon, polygonSymbol);
        mGraphicsOverlay.getGraphics().add(polygonGraphic);
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


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PMICompletedMapActivity33KV.this, PMIHomeActivity.class);
        startActivity(intent);
        finish();
    }
}