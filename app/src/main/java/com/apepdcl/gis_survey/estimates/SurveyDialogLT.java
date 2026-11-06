package com.apepdcl.gis_survey.estimates;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import com.apepdcl.gis_survey.Constants.MobileConstants;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.estimates.models.DTRModel;
import com.apepdcl.gis_survey.estimates.models.EstimatesLTModel;
import com.apepdcl.gis_survey.estimates.models.LT_CKT_Model;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Soubhagya Swain on 13,June,2024
 */
public class SurveyDialogLT extends Dialog {

    SharedPreferences preferences;

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
    Spinner sp_change_from_pole,sp_select_from_pole;
    LinearLayout layout_change_from_pole,layout_is_change_from_pole;

    ArcGISDatabaseEstimates databaseEstimates;
    ArrayList<EstimatesLTModel> listLT;

    TextView tv_current_survey;
    String current_DTR, current_CKT;
    LinearLayout dtr_layout,layout_lt_ckt_survey_completed;
    Spinner sp_dtr,sp_lt_ckt,sp_ckt_survey_completed;


    public SurveyDialogLT(@NonNull Activity context, String poleLat, String poleLon,
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
        setContentView(R.layout.survey_dialog_lt);
        setCancelable(false);

        preferences= PreferenceManager.getDefaultSharedPreferences(context);

        dtr_layout = findViewById(R.id.dtr_layout);
        layout_lt_ckt_survey_completed = findViewById(R.id.layout_lt_ckt_survey_completed);
        sp_dtr = findViewById(R.id.sp_dtr);
        sp_lt_ckt = findViewById(R.id.sp_lt_ckt);
        sp_ckt_survey_completed = findViewById(R.id.sp_ckt_survey_completed);

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
        sp_change_from_pole = findViewById(R.id.sp_change_from_pole);
        sp_select_from_pole = findViewById(R.id.sp_select_from_pole);
        layout_change_from_pole = findViewById(R.id.layout_change_from_pole);
        layout_is_change_from_pole = findViewById(R.id.layout_is_change_from_pole);
        tv_current_survey = findViewById(R.id.tv_current_survey);

        pole_no.setText(""+poleNUmber);
        int fromPoleNumber = poleNUmber-1;
        from_pole.setText(""+fromPoleNumber);

        databaseEstimates = new ArcGISDatabaseEstimates(context);
        listLT = databaseEstimates.getLTSurveyData(EST_NO);

        if (listLT.size()>0){
         //   layout_is_change_from_pole.setVisibility(View.VISIBLE);
        }else {
          //  layout_is_change_from_pole.setVisibility(View.GONE);
        }

        if (ArcGISMapActivityEstimates.IS_33 || ArcGISMapActivityEstimates.IS_11){
           if (poleNUmber == 1){
               dtr_layout.setVisibility(View.VISIBLE);
               layout_lt_ckt_survey_completed.setVisibility(View.GONE);
               tv_current_survey.setVisibility(View.GONE);
               ArrayList<DTRModel> dtrSurveyData = databaseEstimates.getDTRSurveyData(EST_NO);
               List<String> list_pst = new ArrayList<>();
               list_pst.add("Select");
               for (int i = 0; i < dtrSurveyData.size(); i++) {
                   if (dtrSurveyData.get(i).getSURVEY_STATUS().equalsIgnoreCase("N")){
                       list_pst.add("DTR-"+dtrSurveyData.get(i).getPOLE_NO());
                   }
               }
               ArrayAdapter<String> adapter_pst = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, list_pst);
               sp_dtr.setAdapter(adapter_pst);
               sp_dtr.setSelection(0, true);


               sp_dtr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                   @Override
                   public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                       String selected_DTR = list_pst.get(position);
                       if (!selected_DTR.equalsIgnoreCase("Select")){
                        /*   String no_of_ckts = "0";
                           String[] poleNum = selected_DTR.split("-");
                           String dtr_code = poleNum[1];
                           for (int i = 0; i < dtrSurveyData.size(); i++) {
                               if (dtrSurveyData.get(i).getSURVEY_STATUS().equalsIgnoreCase("N")
                                       && dtrSurveyData.get(i).getPOLE_NO().equalsIgnoreCase(dtr_code)){
                                   no_of_ckts = dtrSurveyData.get(i).getNO_OF_LT_CKT();
                               }
                           }*/
                           String[] poleNum = selected_DTR.split("-");
                           String dtr_code = poleNum[1];
                           List<String> list_ckt = new ArrayList<>();
                           list_ckt.add("Select");
                           ArrayList<LT_CKT_Model> cktData = databaseEstimates.getLT_CKT_Data(EST_NO, dtr_code);
                           for (int i = 0; i < cktData.size(); i++) {
                               list_ckt.add("LT_CKT_"+cktData.get(i).getCkt_name());
                           }
                           ArrayAdapter<String> adapter_pst = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, list_ckt);
                           sp_lt_ckt.setAdapter(adapter_pst);
                           sp_lt_ckt.setSelection(0, true);

                          /* if (no_of_ckts.equalsIgnoreCase("1")){
                               list_ckt.add(selected_DTR+"-LT_CKT_A");
                           }

                           if (no_of_ckts.equalsIgnoreCase("2")){
                               list_ckt.add(selected_DTR+"-LT_CKT_A");
                               list_ckt.add(selected_DTR+"-LT_CKT_B");
                           }

                           if (no_of_ckts.equalsIgnoreCase("3")){
                               list_ckt.add(selected_DTR+"-LT_CKT_A");
                               list_ckt.add(selected_DTR+"-LT_CKT_B");
                               list_ckt.add(selected_DTR+"-LT_CKT_C");
                           }

                           if (no_of_ckts.equalsIgnoreCase("4")){
                               list_ckt.add(selected_DTR+"-LT_CKT_A");
                               list_ckt.add(selected_DTR+"-LT_CKT_B");
                               list_ckt.add(selected_DTR+"-LT_CKT_C");
                               list_ckt.add(selected_DTR+"-LT_CKT_D");
                           }*/



                       }
                   }

                   @Override
                   public void onNothingSelected(AdapterView<?> parent) {

                   }
               });

           }else {
               dtr_layout.setVisibility(View.GONE);
               layout_lt_ckt_survey_completed.setVisibility(View.VISIBLE);
               tv_current_survey.setVisibility(View.VISIBLE);
               tv_current_survey.setText(
                       "LT_CKT_"+preferences.getString(MobileConstants.CURRENT_LT_CKT,"")+" of DTR-"+preferences.getString(MobileConstants.CURRENT_DTR,"")
               );
           }
        }

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

        sp_change_from_pole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_change_from_pole.getSelectedItem().toString().equalsIgnoreCase("Yes")){
                    layout_change_from_pole.setVisibility(View.VISIBLE);
                    List<String> list_pst = new ArrayList<>();
                    list_pst.add("Select");

                    if (listLT.size()>0){
                        for (int i = 0; i < listLT.size(); i++) {
                            list_pst.add(listLT.get(i).getPOLE_NO());
                        }
                    }

                    ArrayAdapter<String> adapter_pst = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, list_pst);
                    sp_select_from_pole.setAdapter(adapter_pst);
                    sp_select_from_pole.setSelection(0, true);
                }else {
                    layout_change_from_pole.setVisibility(View.GONE);
                    from_pole.setText(""+fromPoleNumber);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_select_from_pole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_select_from_pole.getSelectedItem().toString().equalsIgnoreCase("Select")){
                    from_pole.setText(""+fromPoleNumber);
                }else {
                    from_pole.setText(""+sp_select_from_pole.getSelectedItem().toString());
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

        if (sp_change_from_pole.getSelectedItem().toString().equalsIgnoreCase("Yes") &&
                sp_select_from_pole.getSelectedItem().toString().equalsIgnoreCase("Select")
        ){
            Toast.makeText(context, "Please Select From Pole", Toast.LENGTH_SHORT).show();
            return;
        }

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
            Toast.makeText(context, "Please Select Line Configuration", Toast.LENGTH_SHORT).show();
            return;
        }


        addPoleToLocalDatabase();



    }

    private void addPoleToLocalDatabase() {
        ArcGISDatabaseEstimates databaseEstimates = new ArcGISDatabaseEstimates(context);
        ArrayList<EstimatesLTModel> ltSurveyData = databaseEstimates.getLTSurveyData(EST_NO);
        if (ArcGISMapActivityEstimates.IS_33 || ArcGISMapActivityEstimates.IS_11){
            if (poleNUmber==1){
                ArrayList<DTRModel> dtrSurveyData = databaseEstimates.getDTRSurveyData(EST_NO);
                String[] poleNum = sp_dtr.getSelectedItem().toString().split("-");
                String dtr_code = poleNum[1];

                String[] ltCktNum = sp_lt_ckt.getSelectedItem().toString().split("_");
                String lt_ckt = ltCktNum[2];

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(MobileConstants.CURRENT_DTR,dtr_code);
                editor.putString(MobileConstants.CURRENT_LT_CKT,lt_ckt);
                editor.commit();


                for (int i = 0; i < dtrSurveyData.size(); i++) {
                    if (dtr_code.equalsIgnoreCase(dtrSurveyData.get(i).getPOLE_NO())){
                        EstimatesLTModel model = new EstimatesLTModel();
                        model.setEST_NO(EST_NO);
                        model.setFROM_POLE(from_pole.getText().toString());
                        model.setPOLE_NO("");
                        model.setLINE_TYPE(sp_lineType.getSelectedItem().toString());
                        model.setPOLE_SURVEY_TYPE(sp_pole_survey_type.getSelectedItem().toString());
                        model.setIS_MULTI_CKT(sp_is_multi_circuit.getSelectedItem().toString());
                        model.setMULTI_CKT_33(ch_multi_33kv.isChecked() ? "YES" : "NO");
                        model.setPOLE_TYPE(sp_pole_type.getSelectedItem().toString());
                        model.setPOLE_SUPPORT(sp_pole_support.getSelectedItem().toString());
                        model.setPOLE_CONFIG(ltSurveyData.isEmpty() ? "Fake" : "From DTR");
                        model.setREF_ID(REF_ID_33);
                        model.setLATITUDE(dtrSurveyData.get(i).getLATITUDE());
                        model.setLONGITUDE(dtrSurveyData.get(i).getLONGITUDE());
                       // model.setLATITUDE(ltSurveyData.isEmpty() ? dtrSurveyData.get(i).getLATITUDE(): ltSurveyData.get(ltSurveyData.size()-1).getLATITUDE());
                      //  model.setLONGITUDE(ltSurveyData.isEmpty() ? dtrSurveyData.get(i).getLONGITUDE(): ltSurveyData.get(ltSurveyData.size()-1).getLONGITUDE());
                        boolean s_f = databaseEstimates.addLTSurveyData(model);
                        if (!ltSurveyData.isEmpty()){
                            model.setPOLE_CONFIG("Fake");
                            databaseEstimates.addLTSurveyData(model);
                        }
                      //  System.out.println("FALSE POLE ADDED : "+s_f);
                    }
                }


            }
        }


        EstimatesLTModel model = new EstimatesLTModel();
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
        boolean status = databaseEstimates.addLTSurveyData(model);
        ArcGISMapActivityEstimates.IS_LT_CKT_COMPLETED = false;
        if (status){
            if (sp_ckt_survey_completed.getSelectedItem().toString().equalsIgnoreCase("Yes")){
                databaseEstimates.updateLTCKTSurveyStatus(EST_NO,preferences.getString(MobileConstants.CURRENT_DTR,""),preferences.getString(MobileConstants.CURRENT_LT_CKT,""));
                ArcGISMapActivityEstimates.IS_LT_CKT_COMPLETED = true;

                ArrayList<LT_CKT_Model> cktModels = databaseEstimates.getLT_CKT_Data(EST_NO, preferences.getString(MobileConstants.CURRENT_DTR, ""));
                if (cktModels.isEmpty()){
                    databaseEstimates.updateDTRSurveyStatus(EST_NO, preferences.getString(MobileConstants.CURRENT_DTR, ""));
                }

                Toast.makeText(context, "Pole location captured Successfully..", Toast.LENGTH_SHORT).show();
                dismiss();
                callout.dismiss();
                graphicsOverlay.getGraphics().remove(graphic);


            }
            if (sp_survey_completed.getSelectedItem().toString().equalsIgnoreCase("Yes")){
                databaseEstimates.updateSurveyStatus(EST_NO, "LT");
                ArcGISMapActivityEstimates.IS_LT_COMPLETED = true;
                Toast.makeText(context, "Pole location captured Successfully..", Toast.LENGTH_SHORT).show();
                dismiss();
                callout.dismiss();
                graphicsOverlay.getGraphics().remove(graphic);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("LT Survey Completed");
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
            else {
                Toast.makeText(context, "Pole location captured Successfully..", Toast.LENGTH_SHORT).show();
                dismiss();
                callout.dismiss();
                graphicsOverlay.getGraphics().remove(graphic);
            }


        }else {
            Toast.makeText(context, "Failed to capture Pole location.", Toast.LENGTH_SHORT).show();
        }
    }
}
