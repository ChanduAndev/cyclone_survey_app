package com.apepdcl.gis_survey.RDSS;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis_survey.DashBoard;
import com.apepdcl.gis_survey.utilits.GpsTraker;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LayerSceneProperties;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;

import java.util.ArrayList;

public class ViewMapRdssActivity extends AppCompatActivity {
    private MapView mv;
    GpsTraker gpsTraker;
    private GraphicsOverlay mGraphicsOverlay;
    ArcGISMap map;

    PointCollection polylinePoints;
    LocationDisplay mLocationDisplay;
    private ArrayList<UGPostingModel> list = new ArrayList<>();

    RdssUGDatabase database;
    ArrayList<UGPostingModel> polesList33, polesList11;
    ArrayList<UGPostingModel> filteredPolesList33, filteredPolesList11;
    private String ID = "", USER_ID = "", VOLTAGE_LEVEL = "";
    GraphicsOverlay graphicsOverlay;
    private Point ptWgs84;
    Graphic graphic;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_map_rdss_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mv = findViewById(R.id.map1);
        polesList33 = new ArrayList<>();
        polesList11 = new ArrayList<>();
        filteredPolesList33 = new ArrayList<>();
        filteredPolesList11 = new ArrayList<>();
        list = (ArrayList<UGPostingModel>) getIntent().getSerializableExtra("DATA_MODEL");
        ID = getIntent().getStringExtra("ID");
        VOLTAGE_LEVEL = getIntent().getStringExtra("VOLTAGE");
        if (VOLTAGE_LEVEL.equals("33KV")) {
            toolbar.setTitle("33KV Survey");
        } else {
            toolbar.setTitle("11KV Survey");
        }
        database = new RdssUGDatabase(this);
        gpsTraker = new GpsTraker(ViewMapRdssActivity.this);
        gpsTraker.getLocation();
        mGraphicsOverlay = new GraphicsOverlay();
        mv.getGraphicsOverlays().add(mGraphicsOverlay);
        preferences = PreferenceManager.getDefaultSharedPreferences(ViewMapRdssActivity.this);
        USER_ID = preferences.getString(MobileConstants.USERNAME, "");

        map = new ArcGISMap(Basemap.Type.IMAGERY_WITH_LABELS, gpsTraker.getLatitude(), gpsTraker.getLongitude(), 20);
        mv.setMap(map);

        mLocationDisplay = mv.getLocationDisplay();

        mLocationDisplay.addDataSourceStatusChangedListener(dataSourceStatusChangedEvent -> {
            if (dataSourceStatusChangedEvent.isStarted() || dataSourceStatusChangedEvent.getError() == null) {
                return;
            }


            String message = String.format("Error in DataSourceStatusChangedListener: %s",
                    dataSourceStatusChangedEvent.getSource().getLocationDataSource().getError().getMessage());
            Toast.makeText(ViewMapRdssActivity.this, message, Toast.LENGTH_LONG).show();

        });
        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
        mLocationDisplay.startAsync();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (VOLTAGE_LEVEL.equals("33KV")) {
            ArrayList<UGPostingModel> allPolesList33 = database.getUGPostingDataWithId(ID, USER_ID);
            polesList33 = new ArrayList<>();
            polesList33.clear();

            // Filter poles belonging to current Substation ID and voltage = 33KV
            for (int i = 0; i < allPolesList33.size(); i++) {
                UGPostingModel pole = allPolesList33.get(i);
                if (pole.getVOLTAGE().equalsIgnoreCase("33KV") && pole.getSS_ID().equalsIgnoreCase(ID)) {
                    polesList33.add(pole);
                }
            }

            // If there are any 33KV poles found
            if (!polesList33.isEmpty()) {
                polylinePoints = new PointCollection(SpatialReferences.getWgs84());
                filteredPolesList33.clear();

                // ✅ Load ALL poles without any distance restriction
                filteredPolesList33.addAll(polesList33);

                // Show info dialog (optional)


                // --- Plot each pole on the map ---
                for (int i = 0; i < filteredPolesList33.size(); i++) {
                    UGPostingModel pole = filteredPolesList33.get(i);

                    double lat = Double.parseDouble(pole.getLAT());
                    double lon = Double.parseDouble(pole.getLON());

                    // Add point to collection
                    polylinePoints.add(new Point(lat, lon));

                    // Create graphics overlay and symbol for marker
                    graphicsOverlay = new GraphicsOverlay();
                    mv.getGraphicsOverlays().add(graphicsOverlay);

                    SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(
                            SimpleMarkerSymbol.Style.CIRCLE,
                            Color.RED,
                            12
                    );

                    // Create and add the pole point
                    ptWgs84 = new Point(lat, lon, 414, SpatialReferences.getWgs84());
                    graphic = new Graphic(ptWgs84, symbol);
                    graphicsOverlay.getGraphics().add(graphic);

                    // Create overlay for text labels (absolute placement)
                    GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                    absoluteOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties.SurfacePlacement.ABSOLUTE);
                    absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, symbol));

                    // Label showing LOCATION_NO (pole ID)
                    TextSymbol absoluteText = new TextSymbol(
                            15,
                            pole.getLOCATION_NO(),
                            Color.WHITE,
                            TextSymbol.HorizontalAlignment.LEFT,
                            TextSymbol.VerticalAlignment.MIDDLE
                    );
                    absoluteText.setOffsetY(20);
                    absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, absoluteText));

                    mv.getGraphicsOverlays().add(absoluteOverlay);
                }

                // --- Draw connecting lines between poles ---
                for (UGPostingModel surveyVo : filteredPolesList33) {
                    PointCollection polylinePoints = new PointCollection(SpatialReferences.getWgs84());

                    // Find matching source location and connect it
                    for (int i = 0; i < filteredPolesList33.size(); i++) {
                        UGPostingModel targetPole = filteredPolesList33.get(i);
                        if (surveyVo.getSOURCE_LOCATION().equalsIgnoreCase(targetPole.getLOCATION_NO())) {
                            polylinePoints.add(new Point(
                                    Double.parseDouble(targetPole.getLAT()),
                                    Double.parseDouble(targetPole.getLON())
                            ));
                        }
                    }

                    // Add the destination pole
                    polylinePoints.add(new Point(
                            Double.parseDouble(surveyVo.getLAT()),
                            Double.parseDouble(surveyVo.getLON())
                    ));

                    // Create polyline between poles
                    Polyline polyline = new Polyline(polylinePoints);

                    // Blue line between connected poles
                    SimpleLineSymbol polylineSymbol = new SimpleLineSymbol(
                            SimpleLineSymbol.Style.SOLID,
                            Color.BLUE,
                            2.0f
                    );

                    Graphic polylineGraphic = new Graphic(polyline, polylineSymbol);
                    mGraphicsOverlay.getGraphics().add(polylineGraphic);
                }

                // Reset for next operation
                polylinePoints = new PointCollection(SpatialReferences.getWgs84());
            }
        } else {


            ArrayList<UGPostingModel> allPolesList11 = database.getUGPostingDataWithId(ID, USER_ID);
            polesList11 = new ArrayList<>();
            polesList11.clear();

            // Filter poles belonging to current Substation ID and voltage = 33KV
            for (int i = 0; i < allPolesList11.size(); i++) {
                UGPostingModel pole = allPolesList11.get(i);
                if (pole.getVOLTAGE().equalsIgnoreCase("11KV") && pole.getSS_ID().equalsIgnoreCase(ID)) {
                    polesList11.add(pole);
                }
            }

            // If there are any 11KV poles found
            if (!polesList11.isEmpty()) {
                polylinePoints = new PointCollection(SpatialReferences.getWgs84());
                filteredPolesList11.clear();

                // ✅ Load ALL poles without any distance restriction
                filteredPolesList11.addAll(polesList11);

                // Show info dialog (optional)


                // --- Plot each pole on the map ---
                for (int i = 0; i < filteredPolesList11.size(); i++) {
                    UGPostingModel pole = filteredPolesList11.get(i);

                    double lat = Double.parseDouble(pole.getLAT());
                    double lon = Double.parseDouble(pole.getLON());

                    // Add point to collection
                    polylinePoints.add(new Point(lat, lon));

                    // Create graphics overlay and symbol for marker
                    graphicsOverlay = new GraphicsOverlay();
                    mv.getGraphicsOverlays().add(graphicsOverlay);

                    SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(
                            SimpleMarkerSymbol.Style.CIRCLE,
                            Color.RED,
                            12
                    );

                    // Create and add the pole point
                    ptWgs84 = new Point(lat, lon, 414, SpatialReferences.getWgs84());
                    graphic = new Graphic(ptWgs84, symbol);
                    graphicsOverlay.getGraphics().add(graphic);

                    // Create overlay for text labels (absolute placement)
                    GraphicsOverlay absoluteOverlay = new GraphicsOverlay();
                    absoluteOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties.SurfacePlacement.ABSOLUTE);
                    absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, symbol));

                    // Label showing LOCATION_NO (pole ID)
                    TextSymbol absoluteText = new TextSymbol(
                            15,
                            pole.getLOCATION_NO(),
                            Color.WHITE,
                            TextSymbol.HorizontalAlignment.LEFT,
                            TextSymbol.VerticalAlignment.MIDDLE
                    );
                    absoluteText.setOffsetY(20);
                    absoluteOverlay.getGraphics().add(new Graphic(ptWgs84, absoluteText));

                    mv.getGraphicsOverlays().add(absoluteOverlay);
                }

                // --- Draw connecting lines between poles ---
                for (UGPostingModel surveyVo : filteredPolesList11) {
                    PointCollection polylinePoints = new PointCollection(SpatialReferences.getWgs84());

                    // Find matching source location and connect it
                    for (int i = 0; i < filteredPolesList11.size(); i++) {
                        UGPostingModel targetPole = filteredPolesList11.get(i);
                        if (surveyVo.getSOURCE_LOCATION().equalsIgnoreCase(targetPole.getLOCATION_NO())) {
                            polylinePoints.add(new Point(
                                    Double.parseDouble(targetPole.getLAT()),
                                    Double.parseDouble(targetPole.getLON())
                            ));
                        }
                    }

                    // Add the destination pole
                    polylinePoints.add(new Point(
                            Double.parseDouble(surveyVo.getLAT()),
                            Double.parseDouble(surveyVo.getLON())
                    ));

                    // Create polyline between poles
                    Polyline polyline = new Polyline(polylinePoints);

                    // Blue line between connected poles
                    SimpleLineSymbol polylineSymbol = new SimpleLineSymbol(
                            SimpleLineSymbol.Style.SOLID,
                            Color.BLUE,
                            2.0f
                    );

                    Graphic polylineGraphic = new Graphic(polyline, polylineSymbol);
                    mGraphicsOverlay.getGraphics().add(polylineGraphic);
                }

                // Reset for next operation
                polylinePoints = new PointCollection(SpatialReferences.getWgs84());
            }
        }


    }
}
