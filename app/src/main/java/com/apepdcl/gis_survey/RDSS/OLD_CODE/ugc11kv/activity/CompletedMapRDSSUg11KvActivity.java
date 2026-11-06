package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.constants.MobileConstantsRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.retrofitservice.responsevo.RetrofitClientInstanceRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.retrofitservice.responsevo.RetrofitServiceRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.runtimepermessions.ConnectivityCheckingRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.sqldatabase.ArcGISDatabaseRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.utilities.CommanUtilitsRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.utilities.GpsTrakerRDSSUg11Kv;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc11kv.vo.SurveyVoRDSSUg11Kv;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
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
import com.esri.arcgisruntime.mapping.view.MapView;
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

public class CompletedMapRDSSUg11KvActivity extends AppCompatActivity {
    private MapView mv;
    ArcGISMap map;
    ArrayList<Graphic> graph_objects = new ArrayList<Graphic>();
    GraphicsOverlay graphicsOverlay;
    //GraphicsLayer graphicsLayer = new GraphicsLayer();
    private GraphicsOverlay mGraphicsOverlay;
    Graphic graphic;
    Button btndelete;
    private Callout mCallout;
    GpsTrakerRDSSUg11Kv gpsTrakerRDSSUg11Kv;
    private String ssName, feederName, feederCode, maxCount, tappingPoint, surveyType, sessionId;
    private ArcGISDatabaseRDSSUg11Kv arcGISDatabaseRDSSUg11Kv;
    private List<SurveyVoRDSSUg11Kv> surveyVoRDSSUg11KvList;
    private Point ptWgs84;
    private int i, count, maxSurveyCount, poleCountCheck;
    private SharedPreferences preferences;
    private RetrofitServiceRDSSUg11Kv retrofitServiceRDSSUg11Kv;
    private ProgressDialog progressDialog;
    PointCollection polylinePoints;
    Point point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completedmap_rdss_ug_11kv);
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("11KV UG Cable Survey");
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent playIntent = new Intent(CompletedMapRDSSUg11KvActivity.this, FeederMasterRDSSUg11KvActivity.class); //Intent.ACTION_VIEW);
                    startActivity(playIntent);
                    finish();

                }
            });
            progressDialog = CommanUtilitsRDSSUg11Kv.progressDialog(this);
            mv = (MapView) findViewById(R.id.map1);
            //btndelete=(Button)findViewById(R.id.btndelete);
            gpsTrakerRDSSUg11Kv = new GpsTrakerRDSSUg11Kv(CompletedMapRDSSUg11KvActivity.this);
            gpsTrakerRDSSUg11Kv.getLocation();
            mGraphicsOverlay = new GraphicsOverlay();
            mv.getGraphicsOverlays().add(mGraphicsOverlay);
            Intent intent = getIntent();
            if (intent != null) {
                feederCode = intent.getStringExtra("FEEDER_CODE");
            }
            // arcGISDatabase=new ArcGISDatabase(CompletedMapActivity.this);

            map = new ArcGISMap(Basemap.Type.IMAGERY_WITH_LABELS, gpsTrakerRDSSUg11Kv.getLatitude(), gpsTrakerRDSSUg11Kv.getLongitude(), 20);
            mv.setMap(map);
            preferences = PreferenceManager.getDefaultSharedPreferences(CompletedMapRDSSUg11KvActivity.this);
            sessionId = preferences.getString(MobileConstantsRDSSUg11Kv.SESSION_ID, "");

            usrLogin();


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

    public void usrLogin() {
        try {
            if (ConnectivityCheckingRDSSUg11Kv.checkConnection(CompletedMapRDSSUg11KvActivity.this)) {

                progressDialog.show();

                JsonObject postParam = new JsonObject();
                //postParam.addProperty("order",yourArray) ;
                postParam.addProperty("FEEDER_ID", feederCode);
                postParam.addProperty("IMEI_NO", CommanUtilitsRDSSUg11Kv.getIMEI_NO(CompletedMapRDSSUg11KvActivity.this));
                postParam.addProperty("SESSION_ID", sessionId);
                postParam.addProperty("VERSION", MobileConstantsRDSSUg11Kv.APP_VERSION);

                retrofitServiceRDSSUg11Kv = RetrofitClientInstanceRDSSUg11Kv.getRetrofitInstance().create(RetrofitServiceRDSSUg11Kv.class);
                Call<JsonObject> responseVoCall = retrofitServiceRDSSUg11Kv.getCompletedData(postParam);
                responseVoCall.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try {
                            if (response.body() != null) {
                                JsonObject jsonObject = response.body();
                                if ("TRUE".equalsIgnoreCase(jsonObject.get("STATUS").getAsString())) {
                                    JsonObject responseObj = jsonObject.getAsJsonObject("RESPONSE");
                                    responseObj.entrySet();
                                    polylinePoints = new PointCollection(SpatialReferences.getWgs84());
                                    List<String> keys = new ArrayList<>();
                                    Set<Map.Entry<String, JsonElement>> entries = responseObj.entrySet();//will return members of your object
                                    for (Map.Entry<String, JsonElement> entry : entries) {
                                        keys.add(entry.getKey());
                                        System.out.println(entry.getKey());
                                    }
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
                                           SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 12); //size 12, style of circle
                                            ptWgs84 = new Point(data.get("lat").getAsDouble(), data.get("long").getAsDouble(), 414, SpatialReferences.getWgs84());
                                            graphic = new Graphic(ptWgs84, symbol);
                                            graph_objects.add(graphic);
                                            graphicsOverlay.getGraphics().add(graphic);

                                          /*  SimpleMarkerSymbol symbol = null;

                                            JsonElement nullableText = data.get("woId");
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
                                                    TextSymbol.VerticalAlignment.MIDDLE);  absoluteText.setOffsetY(20);
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
                                    Toast.makeText(CompletedMapRDSSUg11KvActivity.this, jsonObject.get("ERR_MSG").getAsString(), Toast.LENGTH_SHORT).show();
                                    if ("No Records".equalsIgnoreCase(jsonObject.get("ERR_MSG").getAsString())) {
                                        Intent intent = new Intent(CompletedMapRDSSUg11KvActivity.this, FeederMasterRDSSUg11KvActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    progressDialog.dismiss();
                                }

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(CompletedMapRDSSUg11KvActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(CompletedMapRDSSUg11KvActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                CommanUtilits.internetDialog(CompletedMapRDSSUg11KvActivity.this);
               // Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
