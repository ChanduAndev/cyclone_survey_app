package com.apepdcl.gis_survey.activitys;

import android.app.ProgressDialog;
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

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.HomeActivity;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.retrofitservice.RetrofitClientInstance;
import com.apepdcl.gis_survey.retrofitservice.RetrofitService;
import com.apepdcl.gis_survey.runtimeperimissions.ConnectivityChecking;
import com.apepdcl.gis_survey.sqldatabase.ArcGISDatabase;
import com.apepdcl.gis_survey.utilits.CommanUtilits;
import com.apepdcl.gis_survey.utilits.GpsTraker;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.apepdcl.gis_survey.vo.SurveyDataVo;
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
import com.google.gson.Gson;
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

public class LTCompletedMapActivity extends AppCompatActivity{
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
    private String dtrCode,ltcktCode,sessionId;
    private ArcGISDatabase arcGISDatabase;
    private List<SurveyDataVo> surveyVoList;
    private Point ptWgs84;
    private int i,count,maxSurveyCount,poleCountCheck;
    private SharedPreferences preferences;
    private RetrofitService retrofitService;
    private ProgressDialog progressDialog;
    PointCollection polylinePoints;
    Point point;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completedmap);
        try {
            mv = (MapView) findViewById(R.id.map1);
            //btndelete=(Button)findViewById(R.id.btndelete);
            gpsTraker = new GpsTraker(LTCompletedMapActivity.this);
            gpsTraker.getLocation();
            mGraphicsOverlay = new GraphicsOverlay();
            mv.getGraphicsOverlays().add(mGraphicsOverlay);
            Intent intent = getIntent();
            if(intent!=null) {
                dtrCode= intent.getStringExtra("DTRCODE");
                ltcktCode= intent.getStringExtra("CIRCUIT_CODE");
            }
            progressDialog=new ProgressDialog(LTCompletedMapActivity.this);
            progressDialog.setMessage("Please Wail...");
            progressDialog.setCancelable(false);
             arcGISDatabase=new ArcGISDatabase(LTCompletedMapActivity.this);

            map = new ArcGISMap(Basemap.Type.IMAGERY_WITH_LABELS, gpsTraker.getLatitude(), gpsTraker.getLongitude(), 20);
            mv.setMap(map);
            preferences = PreferenceManager.getDefaultSharedPreferences(LTCompletedMapActivity.this);
            sessionId=preferences.getString(MobileConstants.SESSION_ID,"");

            usrLogin();

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("GISLT Survey");
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent playIntent = new Intent(LTCompletedMapActivity.this, HomeActivity.class); //Intent.ACTION_VIEW);
                    startActivity(playIntent);
                    finish();

                }
            });
         //   setSupportActionBar(toolbar);
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
        Intent intent=new Intent(LTCompletedMapActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
    public void usrLogin(){
        try {
            if (ConnectivityChecking.checkConnection(LTCompletedMapActivity.this)) {

                progressDialog.show();
                List<LoginDataResponse> loginDataResponses=arcGISDatabase.getUserMasterData();
                JsonObject postParam = new JsonObject();
                //postParam.addProperty("order",yourArray) ;
                postParam.addProperty("DTR_STRUCTURE", dtrCode);
                postParam.addProperty("LT_CKTCODE", ltcktCode);
                postParam.addProperty("IMEI_NO", CommanUtilits.getIMEI_NO(LTCompletedMapActivity.this));
                postParam.addProperty("SESSION_ID", sessionId);
                postParam.addProperty("VERSION", "1.2");
                postParam.addProperty("SEC_CODE", loginDataResponses.get(0).getSEC_CODE());
                Gson gson = new Gson();
                String json = gson.toJson(postParam);
               // Log.v("DATA===>>", "" + json);

                retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
                Call<JsonObject> responseVoCall = retrofitService.getCompletedData(postParam);
                responseVoCall.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        progressDialog.dismiss();
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
                                       // System.out.println(entry.getKey());
                                    }
                                  //  Log.v("KEYS", "" + keys.size());
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
                                            SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED,
                                                    12); //size 12, style of circle

                                            //add a new graphic with a new point geometry
                                            // Point graphicPoint = new Point(myWGS84Point.getX(), myWGS84Point.getY(), SpatialReferences.getWebMercator());
                                            ptWgs84 = new Point(data.get("lat").getAsDouble(), data.get("long").getAsDouble(), 414, SpatialReferences.getWgs84());
                                            graphic = new Graphic(ptWgs84, symbol);
                                            graph_objects.add(graphic);
                                            graphicsOverlay.getGraphics().add(graphic);

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

                                    }

                                } else {
                                    Toast.makeText(LTCompletedMapActivity.this, jsonObject.get("ERR_MSG").getAsString(), Toast.LENGTH_SHORT).show();
                                   if("No Records".equalsIgnoreCase(jsonObject.get("ERR_MSG").getAsString())){
                                        Intent intent=new Intent(LTCompletedMapActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }

                        } else {
                            Toast.makeText(LTCompletedMapActivity.this, "Server Busy", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        progressDialog.dismiss();
                        //  progressDialog.dismiss();
                        Toast.makeText(LTCompletedMapActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                    }
                });


            } else {
                Toast.makeText(getApplicationContext(), "For Accessing this feature internet connection is required", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
