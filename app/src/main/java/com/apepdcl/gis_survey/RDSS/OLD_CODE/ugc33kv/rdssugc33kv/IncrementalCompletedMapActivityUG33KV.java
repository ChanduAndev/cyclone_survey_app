package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.Constants.MobileConstantsUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.activity.FeederMasterActivityUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.retrofitservice.RetrofitClientInstanceUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.retrofitservice.RetrofitServiceUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.utilits.CommanUtilitsUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.utilits.ConnectivityCheckingUG33KV;
import com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.vo.SurveyVoUG33KV;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LayerSceneProperties;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IncrementalCompletedMapActivityUG33KV extends AppCompatActivity{
    private MapView mv;
    ArcGISMap map;
    ArrayList<Graphic> graph_objects = new ArrayList<Graphic>();
    GraphicsOverlay graphicsOverlay;
    //GraphicsLayer graphicsLayer = new GraphicsLayer();
    private GraphicsOverlay mGraphicsOverlay;
    Graphic graphic;
    Button btndelete;
    private Callout mCallout;
    GpsTrakerUG33KV gpsTraker;
    private String ssName,feederName,feederCode,maxCount,tappingPoint,surveyType,sessionId;
    private ArcGISDatabaseUG33KV arcGISDatabase;
    private List<SurveyVoUG33KV> surveyVoUG33KVList;
    private Point ptWgs84;
    private int i,count,maxSurveyCount,poleCountCheck;
    private SharedPreferences preferences;
    private RetrofitServiceUG33KV retrofitServiceUG33KV;
    private ProgressDialog progressDialog;
    PointCollection polylinePoints;
    Point point;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incrementalcompletedmap_ugc_33kv);
        try {
            mv = (MapView) findViewById(R.id.map1);
            //btndelete=(Button)findViewById(R.id.btndelete);
            gpsTraker = new GpsTrakerUG33KV(IncrementalCompletedMapActivityUG33KV.this);
            gpsTraker.getLocation();
            mGraphicsOverlay = new GraphicsOverlay();
            mv.getGraphicsOverlays().add(mGraphicsOverlay);
            Intent intent = getIntent();
            if(intent!=null) {
                feederCode = intent.getStringExtra("FEEDER_CODE");
            }
            progressDialog=new ProgressDialog(IncrementalCompletedMapActivityUG33KV.this);
            progressDialog.setMessage("Please Wail...");
            progressDialog.setCancelable(false);
            // arcGISDatabase=new ArcGISDatabase(CompletedMapActivity.this);

            map = new ArcGISMap(Basemap.Type.IMAGERY_WITH_LABELS, gpsTraker.getLatitude(), gpsTraker.getLongitude(), 20);
            mv.setMap(map);
            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            sessionId=preferences.getString(MobileConstantsUG33KV.SESSION_ID_GIS,"");

            usrLogin();

            createPolygonGraphics();
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

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(IncrementalCompletedMapActivityUG33KV.this, FeederMasterActivityUG33KV.class);
        startActivity(intent);
        finish();
    }
    public void usrLogin(){
        try {
            if (ConnectivityCheckingUG33KV.checkConnection(IncrementalCompletedMapActivityUG33KV.this)) {

                progressDialog.show();

                JsonObject postParam = new JsonObject();
                //postParam.addProperty("order",yourArray) ;
                 postParam.addProperty("FEEDER_ID", feederCode);
               // postParam.addProperty("FEEDER_ID", "032111106");
                postParam.addProperty("IMEI_NO", CommanUtilitsUG33KV.getIMEI_NO(IncrementalCompletedMapActivityUG33KV.this));
                postParam.addProperty("SESSION_ID", sessionId);
                postParam.addProperty("VERSION", MobileConstantsUG33KV.APP_VERSION);

                retrofitServiceUG33KV = RetrofitClientInstanceUG33KV.getRetrofitInstance().create(RetrofitServiceUG33KV.class);
                Call<JsonObject> responseVoCall = retrofitServiceUG33KV.getIncrementalCompletedData(postParam);
                responseVoCall.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        progressDialog.dismiss();
                        if (response.body() != null) {
                            JsonObject jsonObject=response.body();
                            if ("TRUE".equalsIgnoreCase(jsonObject.get("STATUS").getAsString())) {
                                JsonObject responseObj = jsonObject.getAsJsonObject("RESPONSE");
                                Log.v("DATACOM==", "" + responseObj);
                                responseObj.entrySet();
                                polylinePoints = new PointCollection(SpatialReferences.getWgs84());
                                List<String> keys = new ArrayList<>();
                                Set<Map.Entry<String, JsonElement>> entries = responseObj.entrySet();//will return members of your object
                                for (Map.Entry<String, JsonElement> entry : entries) {
                                    keys.add(entry.getKey());
                                    System.out.println(entry.getKey());
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
                                        SimpleMarkerSymbol symbol = null;
                                        //SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED,12); //size 12, style of circle

                                       //Object  wo=data.get("woId");
                                      //  String  wo1=data.get("woId").toString();
                                      //  String sr=""+""+"";
                                        //String value = (wo == null ||  wo.equals("")) ? "": wo.toString();


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
                                        }

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

                                    Polyline polyline = new Polyline(polylinePoints);
                                    SimpleLineSymbol polylineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 3.0f);
                                    Graphic polylineGraphic = new Graphic(polyline, polylineSymbol);
                                    mGraphicsOverlay.getGraphics().add(polylineGraphic);
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
                                Toast.makeText(IncrementalCompletedMapActivityUG33KV.this, jsonObject.get("ERR_MSG").getAsString(), Toast.LENGTH_SHORT).show();
                                if("No Records".equalsIgnoreCase(jsonObject.get("ERR_MSG").getAsString())){
                                    Intent intent=new Intent(IncrementalCompletedMapActivityUG33KV.this, FeederMasterActivityUG33KV.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                        } else {
                            Toast.makeText(IncrementalCompletedMapActivityUG33KV.this, "Server Busy", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        progressDialog.dismiss();
                        //  progressDialog.dismiss();
                        Toast.makeText(IncrementalCompletedMapActivityUG33KV.this, "Try Again", Toast.LENGTH_LONG).show();
                    }
                });


            } else {
                Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
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

}
