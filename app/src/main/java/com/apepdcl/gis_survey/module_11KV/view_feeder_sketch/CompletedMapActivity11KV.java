//package com.apepdcl.gis_survey.module_11KV.view_feeder_sketch;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.graphics.Color;
//import android.location.LocationManager;
//import android.os.Bundle;
//import android.preference.PreferenceManager;
//import android.provider.Settings;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.Toast;
//
//import com.apepdcl.gis_survey.Constants.MobileConstants;
//import com.apepdcl.gis_survey.HomeActivity;
//import com.apepdcl.gis.app.R;
//import com.apepdcl.gis_survey.module_11KV.ArcGISDatabase;
//import com.apepdcl.gis_survey.module_11KV.api_calls.RetrofitClientInstance;
//import com.apepdcl.gis_survey.module_11KV.api_calls.RetrofitService;
//import com.apepdcl.gis_survey.module_11KV.models.SurveyVo;
//import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
//import com.apepdcl.gis_survey.utilits.CommanUtilits;
//import com.apepdcl.gis_survey.utilits.GpsTraker;
//import com.esri.arcgisruntime.geometry.Point;
//import com.esri.arcgisruntime.geometry.PointCollection;
//import com.esri.arcgisruntime.geometry.Polyline;
//import com.esri.arcgisruntime.geometry.SpatialReferences;
//import com.esri.arcgisruntime.mapping.ArcGISMap;
//import com.esri.arcgisruntime.mapping.Basemap;
//import com.esri.arcgisruntime.mapping.view.Callout;
//import com.esri.arcgisruntime.mapping.view.Graphic;
//import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
//import com.esri.arcgisruntime.mapping.view.LayerSceneProperties;
//import com.esri.arcgisruntime.mapping.view.LocationDisplay;
//import com.esri.arcgisruntime.mapping.view.MapView;
//import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
//import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
//import com.esri.arcgisruntime.symbology.TextSymbol;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class CompletedMapActivity11KV extends AppCompatActivity {
//    private ImageButton img_logout;
//    private MapView mv;
//    ArcGISMap map;
//    ArrayList<Graphic> graph_objects = new ArrayList<Graphic>();
//    GraphicsOverlay graphicsOverlay;
//    //GraphicsLayer graphicsLayer = new GraphicsLayer();
//    private GraphicsOverlay mGraphicsOverlay;
//    Graphic graphic;
//    Button btndelete;
//    private Callout mCallout;
//    GpsTraker gpsTraker;
//    private String ssName, feederName, feederCode, maxCount, tappingPoint, surveyType, sessionId;
//    private ArcGISDatabase arcGISDatabase;
//    private List<SurveyVo> surveyVoList;
//    private Point ptWgs84;
//    private int i, count, maxSurveyCount, poleCountCheck;
//    private SharedPreferences preferences;
//    private RetrofitService retrofitService;
//    private ProgressDialog progressDialog;
//    PointCollection polylinePoints;
//    LocationDisplay mLocationDisplay;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_completed_map_activity11_kv);
//        getSupportActionBar().hide();
//        try {
//            img_logout = (ImageButton) findViewById(R.id.img_logout);
//            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//            toolbar.setTitle("11kV Survey ");
//            toolbar.setTitleTextColor(Color.WHITE);
//            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
//            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent playIntent = new Intent(CompletedMapActivity11KV.this, HomeActivity.class); //Intent.ACTION_VIEW);
//                    startActivity(playIntent);
//                    finish();
//
//                }
//            });
//            progressDialog = CommanUtilits.progressDialog(this);
//            mv = (MapView) findViewById(R.id.map1);
//            //btndelete=(Button)findViewById(R.id.btndelete);
//            gpsTraker = new GpsTraker(CompletedMapActivity11KV.this);
//            gpsTraker.getLocation();
//            mGraphicsOverlay = new GraphicsOverlay();
//            mv.getGraphicsOverlays().add(mGraphicsOverlay);
//            Intent intent = getIntent();
//            if (intent != null) {
//                feederCode = intent.getStringExtra("FEEDER_CODE");
//            }
//            // arcGISDatabase=new ArcGISDatabase(CompletedMapActivity.this);
//
//            map = new ArcGISMap(Basemap.Type.IMAGERY_WITH_LABELS, gpsTraker.getLatitude(), gpsTraker.getLongitude(), 20);
//            mv.setMap(map);
//
//            mLocationDisplay = mv.getLocationDisplay();
//
//            mLocationDisplay.addDataSourceStatusChangedListener(dataSourceStatusChangedEvent -> {
//                if (dataSourceStatusChangedEvent.isStarted() || dataSourceStatusChangedEvent.getError() == null) {
//                    return;
//                }
//
//
//                String message = String.format("Error in DataSourceStatusChangedListener: %s",
//                        dataSourceStatusChangedEvent.getSource().getLocationDataSource().getError().getMessage());
//                Toast.makeText(CompletedMapActivity11KV.this, message, Toast.LENGTH_LONG).show();
//
//            });
//            mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
//            mLocationDisplay.startAsync();
//
//            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//            sessionId = preferences.getString(MobileConstants.SESSION_ID, "");
//
//            usrLogin();
//
//            img_logout.setVisibility(View.VISIBLE);
//
//            img_logout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    setCurrentLocation();
//                }
//            });
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mv.pause();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mv.resume();
//    }
//
//    @Override
//    public void onBackPressed() {
//       /* Intent intent=new Intent(CompletedMapActivity.this, FeederMasterActivity.class);
//        startActivity(intent);
//        finish();*/
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//        alertDialogBuilder.setTitle("Exit");
//        alertDialogBuilder
//                .setMessage("Are you sure you want to Exit ?")
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        finish();
//                        System.exit(0);
//
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
//    }
//
//    private void setCurrentLocation() {
//        try {
//            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//            boolean gps_enabled = false;
//            boolean network_enabled = false;
//
//            try {
//                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
//            } catch (Exception ex) {
//            }
//
//            try {
//                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//            } catch (Exception ex) {
//            }
//
//            if (!gps_enabled && !network_enabled) {
//                new androidx.appcompat.app.AlertDialog.Builder(CompletedMapActivity11KV.this)
//                        .setTitle("GPS Alert")
//                        .setMessage("Please Enable GPS")
//
//                        // Specifying a listener allows you to take an action before dismissing the dialog.
//                        // The dialog is automatically dismissed when a dialog button is clicked.
//                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // Continue with delete operation
//                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                            }
//                        })
//
//                        // A null listener allows the button to dismiss the dialog and take no further action.
//                        .setNegativeButton(android.R.string.no, null)
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .show();
//
//            } else {
//
//                /* ** ADD ** */
//                mLocationDisplay.addDataSourceStatusChangedListener(dataSourceStatusChangedEvent -> {
//                    if (dataSourceStatusChangedEvent.isStarted() || dataSourceStatusChangedEvent.getError() == null) {
//                        return;
//                    }
//
//
//                    String message = String.format("Error in DataSourceStatusChangedListener: %s",
//                            dataSourceStatusChangedEvent.getSource().getLocationDataSource().getError().getMessage());
//                    Toast.makeText(CompletedMapActivity11KV.this, message, Toast.LENGTH_LONG).show();
//
//                });
//                // mLocationDisplay.stop();
//                mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
//                mLocationDisplay.startAsync();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void usrLogin() {
//        try {
//            if (ConnectivityChecking.checkConnection(CompletedMapActivity11KV.this)) {
//
//                progressDialog.show();
//
//                JsonObject postParam = new JsonObject();
//                //postParam.addProperty("order",yourArray) ;
//                postParam.addProperty("FEEDER_ID", feederCode);
//                postParam.addProperty("IMEI_NO", CommanUtilits.getIMEI_NO(CompletedMapActivity11KV.this));
//                postParam.addProperty("SESSION_ID", sessionId);
//                postParam.addProperty("VERSION", MobileConstants.APP_VERSION_11_KV);
//               // System.out.println("FEEDER SKETCH BODY : "+postParam);
//                retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
//                Call<JsonObject> responseVoCall = retrofitService.getCompletedData(postParam);
//                responseVoCall.enqueue(new Callback<JsonObject>() {
//                    @Override
//                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                        try {
//                            if (response.body() != null) {
//                              //  System.out.println("FEEDER SKETCH RESPONSE : "+response.body());
//                                JsonObject jsonObject = response.body();
//                                if ("TRUE".equalsIgnoreCase(jsonObject.get("STATUS").getAsString())) {
//                                    JsonObject responseObj = jsonObject.getAsJsonObject("RESPONSE");
//                                    responseObj.entrySet();
//                                    polylinePoints = new PointCollection(SpatialReferences.getWgs84());
//                                    List<String> keys = new ArrayList<>();
//                                    Set<Map.Entry<String, JsonElement>> entries = responseObj.entrySet();//will return members of your object
//                                    for (Map.Entry<String, JsonElement> entry : entries) {
//                                        keys.add(entry.getKey());
//                                      //  System.out.println(entry.getKey());
//                                    }
//                                    for (String s : keys) {
//                                        JsonArray elements = responseObj.getAsJsonArray(s);
//
//                                        for (int i = 0; elements.size() > i; i++) {
//                                            JsonObject data = elements.get(i).getAsJsonObject();
//                                            //Double  lat=data.get("lat").getAsDouble();
//                                            //  Double  lon=data.get("long").getAsDouble();
//                                           // Log.v("DATA==", "" + data);
//
//                                            polylinePoints.add(new Point(data.get("lat").getAsDouble(), data.get("long").getAsDouble()));
//                                            graphicsOverlay = new GraphicsOverlay();
//                                            mv.getGraphicsOverlays().add(graphicsOverlay);
//                                            SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 12); //size 12, style of circle
//                                            ptWgs84 = new Point(data.get("lat").getAsDouble(), data.get("long").getAsDouble(), 414, SpatialReferences.getWgs84());
//                                            graphic = new Graphic(ptWgs84, symbol);
//                                            graph_objects.add(graphic);
//                                            graphicsOverlay.getGraphics().add(graphic);
//
//
//
//
//                                            //Display Text
//                                            //add a new graphic with a new point geometry
//                                            // Point graphicPoint = new Point(myWGS84Point.getX(), myWGS84Point.getY(), SpatialReferences.getWebMercator());
//                                            ptWgs84 = new Point(data.get("lat").getAsDouble(), data.get("long").getAsDouble(), 414, SpatialReferences.getWgs84());
//                                            graphic = new Graphic(ptWgs84, symbol);
//                                            graph_objects.add(graphic);
//                                            graphicsOverlay.getGraphics().add(graphic);
//                                            GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
//                                            absoluteOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties.SurfacePlacement.ABSOLUTE);
//                                            absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, symbol));
//                                            // create a text symbol for elevation mode
//                                            TextSymbol absoluteText = new TextSymbol(15, data.get("poleno").getAsString(), Color.WHITE, TextSymbol.HorizontalAlignment.LEFT,
//                                                    TextSymbol.VerticalAlignment.MIDDLE);  absoluteText.setOffsetY(20);
//                                            absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, absoluteText));
//                                            mv.getGraphicsOverlays().add(absoluteOverlay);
//                                        }
//
//                                        Polyline polyline = new Polyline(polylinePoints);
//                                        SimpleLineSymbol polylineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 3.0f);
//                                        Graphic polylineGraphic = new Graphic(polyline, polylineSymbol);
//                                        mGraphicsOverlay.getGraphics().add(polylineGraphic);
//                                        polylinePoints = new PointCollection(SpatialReferences.getWgs84());
//                                        progressDialog.dismiss();
//
//                                    }
//
//                                } else {
//                                    Toast.makeText(CompletedMapActivity11KV.this, jsonObject.get("ERR_MSG").getAsString(), Toast.LENGTH_SHORT).show();
//                                    if ("No Records".equalsIgnoreCase(jsonObject.get("ERR_MSG").getAsString())) {
//                                        Intent intent = new Intent(CompletedMapActivity11KV.this, HomeActivity.class);
//                                        startActivity(intent);
//                                        finish();
//                                    }
//                                    progressDialog.dismiss();
//                                }
//
//                            } else {
//                                progressDialog.dismiss();
//                                Toast.makeText(CompletedMapActivity11KV.this, "Server Busy", Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<JsonObject> call, Throwable t) {
//                        progressDialog.dismiss();
//                        Toast.makeText(CompletedMapActivity11KV.this, "Try Again", Toast.LENGTH_LONG).show();
//                    }
//                });
//            } else {
//                Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}


//NEW CODE BY SOUBHAGYA

package com.apepdcl.gis_survey.module_11KV.view_feeder_sketch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
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

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.HomeActivity;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.module_11KV.ArcGISDatabase;
import com.apepdcl.gis_survey.module_11KV.api_calls.RetrofitClientInstance;
import com.apepdcl.gis_survey.module_11KV.api_calls.RetrofitService;
import com.apepdcl.gis_survey.module_11KV.models.SurveyVo;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.utilits.GpsTraker;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LayerSceneProperties;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompletedMapActivity11KV extends AppCompatActivity {
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
    private ArcGISDatabase arcGISDatabase;
    private List<SurveyVo> surveyVoList;
    private Point ptWgs84;
    private int i, count, maxSurveyCount, poleCountCheck;
    private SharedPreferences preferences;
    private RetrofitService retrofitService;
    private ProgressDialog progressDialog;
    PointCollection polylinePoints;
    LocationDisplay mLocationDisplay;

    String apiResponse = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_map_activity11_kv);
        getSupportActionBar().hide();
        try {
            img_logout = (ImageButton) findViewById(R.id.img_logout);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("11kV Survey ");
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent playIntent = new Intent(CompletedMapActivity11KV.this, HomeActivity.class); //Intent.ACTION_VIEW);
                    startActivity(playIntent);
                    finish();

                }
            });
            progressDialog = CommanUtilits.progressDialog(this);
            mv = (MapView) findViewById(R.id.map1);
            //btndelete=(Button)findViewById(R.id.btndelete);
            gpsTraker = new GpsTraker(CompletedMapActivity11KV.this);
            gpsTraker.getLocation();
            mGraphicsOverlay = new GraphicsOverlay();
            mv.getGraphicsOverlays().add(mGraphicsOverlay);
            Intent intent = getIntent();
            if (intent != null) {
                feederCode = intent.getStringExtra("FEEDER_CODE");
            }
            // arcGISDatabase=new ArcGISDatabase(CompletedMapActivity.this);

            map = new ArcGISMap(Basemap.Type.IMAGERY_WITH_LABELS, gpsTraker.getLatitude(), gpsTraker.getLongitude(), 20);
            mv.setMap(map);

            mLocationDisplay = mv.getLocationDisplay();

            mLocationDisplay.addDataSourceStatusChangedListener(dataSourceStatusChangedEvent -> {
                if (dataSourceStatusChangedEvent.isStarted() || dataSourceStatusChangedEvent.getError() == null) {
                    return;
                }


                String message = String.format("Error in DataSourceStatusChangedListener: %s",
                        dataSourceStatusChangedEvent.getSource().getLocationDataSource().getError().getMessage());
                Toast.makeText(CompletedMapActivity11KV.this, message, Toast.LENGTH_LONG).show();

            });
            mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
            mLocationDisplay.startAsync();

            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            sessionId = preferences.getString(MobileConstants.SESSION_ID, "");

            //  usrLogin();
            progressDialog.show();
            callApiWithThread();

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
       /* Intent intent=new Intent(CompletedMapActivity.this, FeederMasterActivity.class);
        startActivity(intent);
        finish();*/
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit");
        alertDialogBuilder
                .setMessage("Are you sure you want to Exit ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        System.exit(0);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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
                new androidx.appcompat.app.AlertDialog.Builder(CompletedMapActivity11KV.this)
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
                    Toast.makeText(CompletedMapActivity11KV.this, message, Toast.LENGTH_LONG).show();

                });
                // mLocationDisplay.stop();
                mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
                mLocationDisplay.startAsync();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void runOnUiThreads(Runnable action) {
        // Simulate running on the UI thread (use Activity.runOnUiThread() in real apps)
        action.run();
    }

    public void callApiWithThread() {
        new Thread(() -> {
            try {
                String response = fetchApiData();
                // Update UI on the main thread
            } catch (Exception e) {
                runOnUiThreads(() -> showError(e)); // Handle error on the main thread
            }
        }).start();
    }

    private String fetchApiData() throws Exception {
        // Simulate API call
        // Simulating network delay


        try {
            if (ConnectivityChecking.checkConnection(CompletedMapActivity11KV.this)) {


                JsonObject postParam = new JsonObject();
                //postParam.addProperty("order",yourArray) ;
                postParam.addProperty("FEEDER_ID", feederCode);
                postParam.addProperty("IMEI_NO", CommanUtilits.getIMEI_NO(CompletedMapActivity11KV.this));
                postParam.addProperty("SESSION_ID", sessionId);
                postParam.addProperty("VERSION", MobileConstants.APP_VERSION_11_KV);
                // System.out.println("FEEDER SKETCH BODY : "+postParam);
                retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
                Call<JsonObject> responseVoCall = retrofitService.getCompletedData(postParam);
                responseVoCall.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try {
                            if (response.body() != null) {
                              //  System.out.println("FEEDER SKETCH RESPONSE : " + response.body());
                                JsonObject jsonObject = response.body();
                                if ("TRUE".equalsIgnoreCase(jsonObject.get("STATUS").getAsString())) {
                                    apiResponse = response.body().toString();
                                    //  Thread.sleep(2000); // Simulate API call
                                    runOnUiThreads(() -> updateUI(apiResponse));

                                } else {
                                    Toast.makeText(CompletedMapActivity11KV.this, jsonObject.get("ERR_MSG").getAsString(), Toast.LENGTH_SHORT).show();
                                    if ("No Records".equalsIgnoreCase(jsonObject.get("ERR_MSG").getAsString())) {
                                        Intent intent = new Intent(CompletedMapActivity11KV.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    progressDialog.dismiss();
                                }

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(CompletedMapActivity11KV.this, "Server Busy", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(CompletedMapActivity11KV.this, "Try Again", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return apiResponse;
    }

    private void updateUI(String response) {
        // Update your UI with the response
       // System.out.println("API Response: " + response);
        //  progressDialog.show();
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(response).getAsJsonObject();
        if ("TRUE".equalsIgnoreCase(jsonObject.get("STATUS").getAsString())) {
            progressDialog.dismiss();
            JsonObject responseObj = jsonObject.getAsJsonObject("RESPONSE");
            responseObj.entrySet();
            polylinePoints = new PointCollection(SpatialReferences.getWgs84());
            List<String> keys = new ArrayList<>();
            Set<Map.Entry<String, JsonElement>> entries = responseObj.entrySet();//will return members of your object
          //  System.out.println("API Response DATA SIZE: " + entries.size());
            for (Map.Entry<String, JsonElement> entry : entries) {
                keys.add(entry.getKey());
                //  System.out.println(entry.getKey());
            }
            for (String s : keys) {
                JsonArray elements = responseObj.getAsJsonArray(s);

                for (int i = 0; elements.size() > i; i++) {
                    JsonObject data = elements.get(i).getAsJsonObject();
                    //Double  lat=data.get("lat").getAsDouble();
                    //  Double  lon=data.get("long").getAsDouble();
                    // Log.v("DATA==", "" + data);

                    polylinePoints.add(new Point(data.get("lat").getAsDouble(), data.get("long").getAsDouble()));
                    graphicsOverlay = new GraphicsOverlay();
                    mv.getGraphicsOverlays().add(graphicsOverlay);
                    SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 12); //size 12, style of circle
                    ptWgs84 = new Point(data.get("lat").getAsDouble(), data.get("long").getAsDouble(), 414, SpatialReferences.getWgs84());
                    graphic = new Graphic(ptWgs84, symbol);
                    graph_objects.add(graphic);
                    graphicsOverlay.getGraphics().add(graphic);


                    //Display Text
                    //add a new graphic with a new point geometry
                    // Point graphicPoint = new Point(myWGS84Point.getX(), myWGS84Point.getY(), SpatialReferences.getWebMercator());
                    ptWgs84 = new Point(data.get("lat").getAsDouble(), data.get("long").getAsDouble(), 414, SpatialReferences.getWgs84());
                    graphic = new Graphic(ptWgs84, symbol);
                    graph_objects.add(graphic);
                    graphicsOverlay.getGraphics().add(graphic);
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

                Polyline polyline = new Polyline(polylinePoints);
                SimpleLineSymbol polylineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 3.0f);
                Graphic polylineGraphic = new Graphic(polyline, polylineSymbol);
                mGraphicsOverlay.getGraphics().add(polylineGraphic);
                polylinePoints = new PointCollection(SpatialReferences.getWgs84());
                progressDialog.dismiss();

            }

        } else {
            Toast.makeText(CompletedMapActivity11KV.this, jsonObject.get("ERR_MSG").getAsString(), Toast.LENGTH_SHORT).show();
            if ("No Records".equalsIgnoreCase(jsonObject.get("ERR_MSG").getAsString())) {
                Intent intent = new Intent(CompletedMapActivity11KV.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
            progressDialog.dismiss();
        }
    }

    private void showError(Exception e) {
        // Show error message
        System.err.println("Error: " + e.getMessage());
    }

    public void usrLogin() {
        try {
            if (ConnectivityChecking.checkConnection(CompletedMapActivity11KV.this)) {

                progressDialog.show();

                JsonObject postParam = new JsonObject();
                //postParam.addProperty("order",yourArray) ;
                postParam.addProperty("FEEDER_ID", feederCode);
                postParam.addProperty("IMEI_NO", CommanUtilits.getIMEI_NO(CompletedMapActivity11KV.this));
                postParam.addProperty("SESSION_ID", sessionId);
                postParam.addProperty("VERSION", MobileConstants.APP_VERSION_11_KV);
                // System.out.println("FEEDER SKETCH BODY : "+postParam);
                retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
                Call<JsonObject> responseVoCall = retrofitService.getCompletedData(postParam);
                responseVoCall.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try {
                            if (response.body() != null) {
                                //  System.out.println("FEEDER SKETCH RESPONSE : "+response.body());
                                JsonObject jsonObject = response.body();
                                if ("TRUE".equalsIgnoreCase(jsonObject.get("STATUS").getAsString())) {
                                    JsonObject responseObj = jsonObject.getAsJsonObject("RESPONSE");
                                    responseObj.entrySet();
                                    polylinePoints = new PointCollection(SpatialReferences.getWgs84());
                                    List<String> keys = new ArrayList<>();
                                    Set<Map.Entry<String, JsonElement>> entries = responseObj.entrySet();//will return members of your object
                                    for (Map.Entry<String, JsonElement> entry : entries) {
                                        keys.add(entry.getKey());
                                        //  System.out.println(entry.getKey());
                                    }
                                    for (String s : keys) {
                                        JsonArray elements = responseObj.getAsJsonArray(s);

                                        for (int i = 0; elements.size() > i; i++) {
                                            JsonObject data = elements.get(i).getAsJsonObject();
                                            //Double  lat=data.get("lat").getAsDouble();
                                            //  Double  lon=data.get("long").getAsDouble();
                                            // Log.v("DATA==", "" + data);

                                            polylinePoints.add(new Point(data.get("lat").getAsDouble(), data.get("long").getAsDouble()));
                                            graphicsOverlay = new GraphicsOverlay();
                                            mv.getGraphicsOverlays().add(graphicsOverlay);
                                            SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 12); //size 12, style of circle
                                            ptWgs84 = new Point(data.get("lat").getAsDouble(), data.get("long").getAsDouble(), 414, SpatialReferences.getWgs84());
                                            graphic = new Graphic(ptWgs84, symbol);
                                            graph_objects.add(graphic);
                                            graphicsOverlay.getGraphics().add(graphic);


                                            //Display Text
                                            //add a new graphic with a new point geometry
                                            // Point graphicPoint = new Point(myWGS84Point.getX(), myWGS84Point.getY(), SpatialReferences.getWebMercator());
                                            ptWgs84 = new Point(data.get("lat").getAsDouble(), data.get("long").getAsDouble(), 414, SpatialReferences.getWgs84());
                                            graphic = new Graphic(ptWgs84, symbol);
                                            graph_objects.add(graphic);
                                            graphicsOverlay.getGraphics().add(graphic);
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

                                        Polyline polyline = new Polyline(polylinePoints);
                                        SimpleLineSymbol polylineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 3.0f);
                                        Graphic polylineGraphic = new Graphic(polyline, polylineSymbol);
                                        mGraphicsOverlay.getGraphics().add(polylineGraphic);
                                        polylinePoints = new PointCollection(SpatialReferences.getWgs84());
                                        progressDialog.dismiss();

                                    }

                                } else {
                                    Toast.makeText(CompletedMapActivity11KV.this, jsonObject.get("ERR_MSG").getAsString(), Toast.LENGTH_SHORT).show();
                                    if ("No Records".equalsIgnoreCase(jsonObject.get("ERR_MSG").getAsString())) {
                                        Intent intent = new Intent(CompletedMapActivity11KV.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    progressDialog.dismiss();
                                }

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(CompletedMapActivity11KV.this, "Server Busy", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(CompletedMapActivity11KV.this, "Try Again", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}