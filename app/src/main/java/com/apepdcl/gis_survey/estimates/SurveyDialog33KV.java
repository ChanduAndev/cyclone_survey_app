package com.apepdcl.gis_survey.estimates;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.estimates.models.Estimates33KVModel;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Soubhagya Swain on 13,June,2024
 */
public class SurveyDialog33KV extends Dialog {

    RelativeLayout back_layout,close_layout;
    String poleLat, poleLon;
    double currentLat, currentLon;
    Activity context;
    Callout callout;
    GraphicsOverlay graphicsOverlay;
    Graphic graphic;
    Button btn_clear,btn_add;
    String EST_NO;
    String REF_ID_33;
    TextView from_pole,pole_no;
    Spinner sp_lineType,sp_pole_survey_type,sp_is_multi_circuit,sp_pole_type,sp_pole_support,sp_pole_configuration,sp_survey_completed;
    CheckBox ch_multi_33kv,ch_multi_11kv,ch_multi_lt;
    LinearLayout multi_ckt_layout;
    int poleNUmber;

    public SurveyDialog33KV(@NonNull Activity context, String poleLat, String poleLon,
                            Callout callout, GraphicsOverlay graphicsOverlay, Graphic graphic,
                            double currentLat, double currentLon,String EST_NO,String REF_ID_33, int poleNUmber) {
        super(context);
        this.context = context;
        this.poleLat = poleLat;
        this.poleLon = poleLon;
        this.currentLat = currentLat;
        this.currentLon = currentLon;
        this.callout = callout;
        this.graphicsOverlay = graphicsOverlay;
        this.graphic = graphic;
        this.EST_NO = EST_NO;
        this.REF_ID_33 = REF_ID_33;
        this.poleNUmber = poleNUmber;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.survey_dialog_33);
        setCancelable(false);
      //  System.out.println("REF ID : "+REF_ID_33);

        close_layout = findViewById(R.id.close_layout);
        back_layout = findViewById(R.id.back_layout);
        btn_clear = findViewById(R.id.btn_clear);
        btn_add = findViewById(R.id.btn_add);
        from_pole = findViewById(R.id.from_pole);
        pole_no = findViewById(R.id.pole_no);
        sp_lineType = findViewById(R.id.sp_lineType);
        sp_pole_survey_type = findViewById(R.id.sp_pole_survey_type);
        sp_is_multi_circuit = findViewById(R.id.sp_is_multi_circuit);
        ch_multi_33kv = findViewById(R.id.ch_multi_33kv);
        ch_multi_11kv = findViewById(R.id.ch_multi_11kv);
        ch_multi_lt = findViewById(R.id.ch_multi_lt);
        sp_pole_type = findViewById(R.id.sp_pole_type);
        sp_pole_support = findViewById(R.id.sp_pole_support);
        sp_pole_configuration = findViewById(R.id.sp_pole_configuration);
        sp_survey_completed = findViewById(R.id.sp_survey_completed);
        multi_ckt_layout = findViewById(R.id.multi_ckt_layout);

        pole_no.setText(""+poleNUmber);
        int fromPoleNumber = poleNUmber-1;
        from_pole.setText(""+fromPoleNumber);

        sp_pole_survey_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_pole_survey_type.getSelectedItem().toString().equalsIgnoreCase("EXISTING")){
                    List<String> list_pst = new ArrayList<>();
                    list_pst.add("Yes");
                    ArrayAdapter<String> adapter_pst = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, list_pst);
                    sp_is_multi_circuit.setAdapter(adapter_pst);
                    sp_is_multi_circuit.setSelection(0, true);
                }else {
                    List<String> list_pst = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.is_multicircuit_33kv)));
                    ArrayAdapter<String> adapter_pst = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, list_pst);
                    sp_is_multi_circuit.setAdapter(adapter_pst);
                    sp_is_multi_circuit.setSelection(0, true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_is_multi_circuit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_is_multi_circuit.getSelectedItem().toString().equalsIgnoreCase("Yes")){
                    multi_ckt_layout.setVisibility(View.VISIBLE);
                }else {
                    multi_ckt_layout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateSurvey();
            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callout.dismiss();
                graphicsOverlay.getGraphics().remove(graphic);
                dismiss();
            }
        });

        close_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callout.dismiss();
                graphicsOverlay.getGraphics().remove(graphic);
                dismiss();
            }
        });

        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callout.dismiss();
                graphicsOverlay.getGraphics().remove(graphic);
                dismiss();
            }
        });
    }

    private void validateSurvey() {

        if (sp_pole_survey_type.getSelectedItem().toString().equalsIgnoreCase("Select")){
            Toast.makeText(context, "Please Select Pole Survey Type", Toast.LENGTH_SHORT).show();
            return;
        }

        if (sp_is_multi_circuit.getSelectedItem().toString().equalsIgnoreCase("Select")){
            Toast.makeText(context, "Please Select is multi circuit", Toast.LENGTH_SHORT).show();
            return;
        }

        if (sp_is_multi_circuit.getSelectedItem().toString().equalsIgnoreCase("Yes")
                && !ch_multi_33kv.isChecked() && !ch_multi_11kv.isChecked() && !ch_multi_lt.isChecked() ){
            Toast.makeText(context, "Please Select other multi circuits", Toast.LENGTH_SHORT).show();
            return;
        }

        if (sp_pole_type.getSelectedItem().toString().equalsIgnoreCase("Select")){
            Toast.makeText(context, "Please Select Pole type", Toast.LENGTH_SHORT).show();
            return;
        }

        if (sp_pole_configuration.getSelectedItem().toString().equalsIgnoreCase("Select")){
            Toast.makeText(context, "Please Select Pole Configuration", Toast.LENGTH_SHORT).show();
            return;
        }

        if (sp_survey_completed.getSelectedItem().toString().equalsIgnoreCase("Yes") &&
                !sp_pole_configuration.getSelectedItem().toString().equalsIgnoreCase("33/11kv SS-Bay")
        ){
            ArcGISDatabaseEstimates databaseEstimates = new ArcGISDatabaseEstimates(context);
            ArrayList<Estimates33KVModel> list = databaseEstimates.get33KVSurveyData(EST_NO);
            boolean ssNotSelected=true;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getPOLE_CONFIG().equalsIgnoreCase("33/11kv SS-Bay")){
                    ssNotSelected = false;
                }
            }

            if (ssNotSelected){
                Toast.makeText(context, "Please Select At least One 33/11kv SS-Bay", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        addPoleToLocalDatabase();



    }

    private void addPoleToLocalDatabase() {
        Estimates33KVModel model = new Estimates33KVModel();
        model.setEST_NO(EST_NO);
        model.setFROM_POLE(from_pole.getText().toString());
        model.setPOLE_NO(pole_no.getText().toString());
        model.setLINE_TYPE(sp_lineType.getSelectedItem().toString());
        model.setPOLE_SURVEY_TYPE(sp_pole_survey_type.getSelectedItem().toString());
        model.setIS_MULTI_CKT(sp_is_multi_circuit.getSelectedItem().toString());
        if (sp_is_multi_circuit.getSelectedItem().toString().equalsIgnoreCase("Yes")){
            model.setMULTI_CKT_33(ch_multi_33kv.isChecked() ? "YES" : "NO");
            model.setMULTI_CKT_11(ch_multi_11kv.isChecked() ? "YES" : "NO");
            model.setMULTI_CKT_LT(ch_multi_lt.isChecked() ? "YES" : "NO");
        }
        model.setPOLE_TYPE(sp_pole_type.getSelectedItem().toString());
        model.setPOLE_SUPPORT(sp_pole_support.getSelectedItem().toString());
        model.setPOLE_CONFIG(sp_pole_configuration.getSelectedItem().toString());
        model.setREF_ID(REF_ID_33);
        model.setLATITUDE(poleLat);
        model.setLONGITUDE(poleLon);
        ArcGISDatabaseEstimates databaseEstimates = new ArcGISDatabaseEstimates(context);
        boolean status = databaseEstimates.add33KVSurveyData(model);
        if (status){
            if (sp_survey_completed.getSelectedItem().toString().equalsIgnoreCase("Yes")){
                databaseEstimates.updateSurveyStatus(EST_NO, "33KV");
                ArcGISMapActivityEstimates.IS_33_COMPLETED = true;
                Toast.makeText(context, "Pole location captured successfully..", Toast.LENGTH_SHORT).show();
                dismiss();
                callout.dismiss();
                graphicsOverlay.getGraphics().remove(graphic);

                if (ArcGISMapActivityEstimates.IS_11){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("Please Start 11KV Survey");
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OKAY",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    })

                            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("33KV Survey Completed");
                    alertDialogBuilder.setMessage("Please transmit the data to server.");
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OKAY",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            Intent intent = new Intent(context, EstimatesHome.class);
                                            context.startActivity(intent);
                                            context.finish();
                                        }
                                    })

                            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    Intent intent = new Intent(context, EstimatesHome.class);
                                    context.startActivity(intent);
                                    context.finish();

                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
            else {
                Toast.makeText(context, "Pole location captured successfully..", Toast.LENGTH_SHORT).show();
                dismiss();
                callout.dismiss();
                graphicsOverlay.getGraphics().remove(graphic);
            }


        }else {
            Toast.makeText(context, "Failed to capture Pole location.", Toast.LENGTH_SHORT).show();
        }
    }
}
