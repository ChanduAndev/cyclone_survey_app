package com.apepdcl.gis_survey.estimates;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.activitys.DTRSelectActivity;
import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.apepdcl.gis_survey.estimates.models.PurposeModel;
import com.apepdcl.gis_survey.estimates.models.StatusModel;
import com.apepdcl.gis_survey.estimates.models.WorkInvolvementModel;
import com.apepdcl.gis_survey.estimates.new_module.NewModel;
import com.apepdcl.gis_survey.estimates.sou.SurveyList;
import com.apepdcl.gis_survey.vo.LoginDataResponse;
import com.leo.searchablespinner.SearchableSpinner;
import com.leo.searchablespinner.interfaces.OnItemSelectListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Soubhagya Swain on 10,June,2024
 */
public class PurposeAdapter extends RecyclerView.Adapter<PurposeAdapter.MyViewHolder> {
    ArrayList<PurposeModel> list;
    Context context;
    private OnItemClickListener listener;
    private ArcGISDatabaseEstimates arcGISDatabaseEstimates;

    String no_of_selected_33kv_feeders = "Select";
    String feeder_1_33kv_type = "Select";


    public PurposeAdapter(ArrayList<PurposeModel> list, Context context, OnItemClickListener listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;

    }

    public interface OnItemClickListener {
        void onItemClick(int position, PurposeModel model);

        void onStartClick(int position, PurposeModel model);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")){
            view = LayoutInflater.from(context).inflate(R.layout.purpose_adapter_wo_rdss, parent, false);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.purpose_adapter, parent, false);
        }
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        arcGISDatabaseEstimates = new ArcGISDatabaseEstimates(context);
        int sl_no = position + 1;
       // holder.sl_no.setText("" + sl_no);


//        holder.est_no.setText("EST No: " + list.get(position).getEST_NO());
//        holder.purpose.setText(list.get(position).getPURPOSE());
//        holder.gis_no.setText("GIS ID: " + list.get(position).getGIS_ID());
//        holder.work_order.setText("Work Order: " + list.get(position).getWORK_ORDER());


        if (SurveyList.IS_EDIT) {

            ArrayList<NewModel> editData = arcGISDatabaseEstimates.getSpecificCurrentSurveyData();
            if (editData.isEmpty()) {
                holder.poles_33kv.setText("" + list.get(position).getNUM_33KV());
                holder.poles_11kv.setText("" + list.get(position).getNUM_11KV());
                holder.poles_lt.setText("" + list.get(position).getNUM_LT());
            } else {
                if (editData.get(0).getEST_NO().equalsIgnoreCase(list.get(position).getEST_NO())) {
                    int poles33 = 0;
                    int poles11 = 0;
                    int polesLT = 0;
                    for (int i = 0; i < editData.size(); i++) {
                        if (editData.get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("33KV") && editData.get(i).getIS_TRANSMITTED_LOCATION().equalsIgnoreCase("N")) {
                            poles33++;
                        }
                        if (editData.get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("11KV") && editData.get(i).getIS_TRANSMITTED_LOCATION().equalsIgnoreCase("N")) {
                            poles11++;
                        }
                        if (editData.get(i).getVOLTAGE_LEVEL().equalsIgnoreCase("LT") && editData.get(i).getIS_TRANSMITTED_LOCATION().equalsIgnoreCase("N")) {
                            polesLT++;
                        }
                    }

                    poles33 = list.get(position).getNUM_33KV() + poles33;
                    poles11 = list.get(position).getNUM_11KV() + poles11;
                    polesLT = list.get(position).getNUM_LT() + polesLT;

                    holder.poles_33kv.setText("" + poles33);
                    holder.poles_11kv.setText("" + poles11);
                    holder.poles_lt.setText("" + polesLT);

                }
            }


        } else {
            holder.poles_33kv.setText("" + list.get(position).getNUM_33KV());
            holder.poles_11kv.setText("" + list.get(position).getNUM_11KV());
            holder.poles_lt.setText("" + list.get(position).getNUM_LT());
        }


        if (list.get(position).getCOUNT_33KV() == 0 && list.get(position).getCOUNT_11KV() == 0 && list.get(position).getCOUNT_LT() == 0) {
            holder.btn_continue.setText("Start Survey");
        } else {
            holder.btn_continue.setText("Continue Survey");
        }
        holder.btn_continue.setAllCaps(false);

        if (list.get(0).getSTATUS().equalsIgnoreCase("1")) {
            holder.layout_pole_count.setVisibility(View.GONE);
            holder.btn_continue.setText("Work Involvements");
        } else {
            holder.layout_pole_count.setVisibility(View.VISIBLE);
        }

        holder.tv_est_no.setText(list.get(position).getEST_NO());
        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("WO_RDSS")){
            holder.tv_description.setText(list.get(position).getFEEDERS());
            holder.tv_description_rdss.setText(list.get(position).getPURPOSE());
            String rdssType = "";
            rdssType=list.get(position).getRDSS_TYPE();
            if (rdssType.equalsIgnoreCase("BIF")){
                holder.tv_scheme_type.setText("BIFURCATION");
            }else if (rdssType.equalsIgnoreCase("AGL")){
                holder.tv_scheme_type.setText("SEGREGATION");
            }else if (rdssType.equalsIgnoreCase("AGL_BIF"))
                holder.tv_scheme_type.setText("BIFURCATION, SEGREGATION");
        }else {
            if (!list.get(position).getPURPOSE().isEmpty() || list.get(position).getPURPOSE()!=null){
                holder.layout_description.setVisibility(View.VISIBLE);
                holder.tv_description.setText(list.get(position).getPURPOSE());
            }else {
                holder.layout_description.setVisibility(View.GONE);
            }

        }

        holder.tv_gis_no.setText(list.get(position).getGIS_ID());
        holder.tv_work_order.setText(list.get(position).getWORK_ORDER());



        if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("ESTIMATES")) {
            holder.layout_work_order.setVisibility(View.GONE);
        } else if (LoginActivity.SURVEY_FROM.equalsIgnoreCase("SOLAR_DPR")) {
            holder.layout_work_order.setVisibility(View.GONE);
            holder.layout_estimates.setVisibility(View.VISIBLE);
            holder.tv_work_order.setVisibility(View.GONE);
            holder.tv_estimate_key.setText("DTR Code          ");
            holder.tv_est_no.setVisibility(View.VISIBLE);
            holder.tv_est_nature.setVisibility(View.GONE);
            holder.layout_est_nature.setVisibility(View.GONE);


        } else {
            holder.layout_work_order.setVisibility(View.VISIBLE);

        }

        holder.btn_continue.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position, list.get(position));
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView est_no, purpose, gis_no, work_order;
        TextView tv_description, tv_work_order, tv_est_no, tv_gis_no, poles_33kv, poles_11kv, poles_lt, tv_est_nature, estimate,tv_description_rdss,tv_scheme_type,tv_estimate_key;
        LinearLayout layout_work_order, layout_estimates, layout_est_nature,layout_schema,layout_description;
        LinearLayout layout_pole_count;
        TextView btn_continue;
        boolean is33kvSelected = false;
        boolean is11kvSelected = false;
        boolean isLtSelected = false;
        boolean isDTRSelected = false;
        String msg = "";
        private boolean isComplted33kVSurvey = false;
        private boolean isComplted11kVSurvey = false;
        private boolean isCompltedLTSurvey = false;
        ArcGISDatabaseEstimates databaseEstimates;
        TextView sl_no;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
//            est_no = itemView.findViewById(R.id.est_no);
//            purpose = itemView.findViewById(R.id.purpose);
//            gis_no = itemView.findViewById(R.id.gis_no);
//            work_order = itemView.findViewById(R.id.work_order);

            sl_no = itemView.findViewById(R.id.sl_no);
            tv_description = itemView.findViewById(R.id.tv_description);
            tv_work_order = itemView.findViewById(R.id.tv_work_order);
            tv_est_no = itemView.findViewById(R.id.tv_est_no);
            tv_gis_no = itemView.findViewById(R.id.tv_gis_no);
            poles_33kv = itemView.findViewById(R.id.poles_33kv);
            poles_11kv = itemView.findViewById(R.id.poles_11kv);
            poles_lt = itemView.findViewById(R.id.poles_lt);
            btn_continue = itemView.findViewById(R.id.btn_continue);
            layout_work_order = itemView.findViewById(R.id.layout_work_order);
            layout_estimates = itemView.findViewById(R.id.layout_estimates);
            layout_est_nature = itemView.findViewById(R.id.layout_est_nature);
            layout_pole_count = itemView.findViewById(R.id.layout_pole_count);
            tv_est_nature = itemView.findViewById(R.id.tv_est_nature);
            estimate = itemView.findViewById(R.id.estimate);
            tv_description_rdss = itemView.findViewById(R.id.tv_description_rdss);
            layout_schema = itemView.findViewById(R.id.layout_schema);
            tv_scheme_type = itemView.findViewById(R.id.tv_scheme_type);
            tv_estimate_key = itemView.findViewById(R.id.tv_estimate_key);
            layout_description = itemView.findViewById(R.id.layout_description);


        }

        private String btnText = "";

        private void setButtonForAlertDialog(String msg) {
            if (msg != null) {
                if (msg.equalsIgnoreCase("Complete Survey")) {
                    btnText = "Complete";
                } else {
                    btnText = "Start";
                }
            }
        }


        private void showAlertDialog(String msg, int position, boolean is33kvSelected, boolean is11kvSelected, boolean isLtSelected) {
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
            builder.setTitle("Alert");
            setButtonForAlertDialog(msg);
            builder.setMessage(msg);
            builder.setPositiveButton(btnText, (dialog, which) -> {
                navigateNextScreen(position, is33kvSelected, is11kvSelected, isLtSelected);
                dialog.dismiss();
            });

            builder.setNegativeButton("Close", (dialog, which) -> {
                dialog.cancel();


            });
            androidx.appcompat.app.AlertDialog dialog = builder.create();
            dialog.show();
        }

        private void navigateNextScreen(int position, boolean is33kvSelected, boolean is11kvSelected, boolean isLtSelected) {
            Intent intent = new Intent(context, WorkInvolmentsActivity.class);
            intent.putExtra("EST_NO", list.get(position).getEST_NO());
            intent.putExtra("EST_NATURE", list.get(position).getEST_NATURE());
            intent.putExtra("PURPOSE", list.get(position).getPURPOSE());
            intent.putExtra("GIS_ID", list.get(position).getGIS_ID());
            intent.putExtra("33KV", is33kvSelected);
            intent.putExtra("11KV", is11kvSelected);
            intent.putExtra("DTR", isDTRSelected);
            intent.putExtra("LT", isLtSelected);
            context.startActivity(intent);
        }

        private void open33KVFeederMasterDialog(TextView edt_text) {
            Dialog payDialog = new Dialog(context);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            Activity activity = (Activity) context;
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);


            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            payDialog.setContentView(R.layout.feeder_master_dialog_estimates);
            payDialog.getWindow().setLayout(width, height / 2);
            payDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            payDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

            TextView cancel_button = payDialog.findViewById(R.id.dialog_cancel);
            TextView back_to_home = payDialog.findViewById(R.id.back_to_home);
            TextView title = payDialog.findViewById(R.id.title);
            title.setText("SELECT 33KV FEEDER !!");

            RadioGroup radioGroup = payDialog.findViewById(R.id.radioGroup);


            payDialog.show();

            back_to_home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    RadioButton radioButton = payDialog.findViewById(selectedId);
                    String selectedRadioButton = radioButton.getText().toString();
                    payDialog.dismiss();
                    if (selectedRadioButton.equals(" Nearby Feeders")) {
                        SearchableSpinner searchableSpinner = new SearchableSpinner(context);
                        searchableSpinner.setWindowTitle("SELECT FEEDER");
                        searchableSpinner.setSpinnerListItems(EstimatesHome.same_list_33kv);
                        searchableSpinner.show();
                        searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                            @Override
                            public void setOnItemSelectListener(int position, String selectedString) {
                                edt_text.setText(selectedString);
                            }
                        });

                    } else {
                        SearchableSpinner searchableSpinner = new SearchableSpinner(context);
                        searchableSpinner.setWindowTitle("SELECT FEEDER");
                        searchableSpinner.setSpinnerListItems(EstimatesHome.other_list_33kv);
                        searchableSpinner.show();
                        searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                            @Override
                            public void setOnItemSelectListener(int position, String selectedString) {
                                edt_text.setText(selectedString);
                            }
                        });
                    }
                }
            });
            cancel_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    payDialog.dismiss();
                }
            });


        }

        private void open11KVFeederMasterDialog(TextView edt_text) {
            Dialog payDialog = new Dialog(context);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            Activity activity = (Activity) context;
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);


            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            payDialog.setContentView(R.layout.feeder_master_dialog_estimates);
            payDialog.getWindow().setLayout(width, height / 2);
            payDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            payDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

            TextView cancel_button = payDialog.findViewById(R.id.dialog_cancel);
            TextView back_to_home = payDialog.findViewById(R.id.back_to_home);
            TextView title = payDialog.findViewById(R.id.title);
            title.setText("SELECT 11KV FEEDER !!");

            RadioGroup radioGroup = payDialog.findViewById(R.id.radioGroup);


            payDialog.show();

            back_to_home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    RadioButton radioButton = payDialog.findViewById(selectedId);
                    String selectedRadioButton = radioButton.getText().toString();
                    payDialog.dismiss();
                    if (selectedRadioButton.equals(" Nearby Feeders")) {
                        SearchableSpinner searchableSpinner = new SearchableSpinner(context);
                        searchableSpinner.setWindowTitle("SELECT FEEDER");
                        searchableSpinner.setSpinnerListItems(EstimatesHome.same_list_11kv);
                        searchableSpinner.show();
                        searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                            @Override
                            public void setOnItemSelectListener(int position, String selectedString) {
                                edt_text.setText(selectedString);
                            }
                        });

                    } else {
                        SearchableSpinner searchableSpinner = new SearchableSpinner(context);
                        searchableSpinner.setWindowTitle("SELECT FEEDER");
                        searchableSpinner.setSpinnerListItems(EstimatesHome.other_list_11kv);
                        searchableSpinner.show();
                        searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                            @Override
                            public void setOnItemSelectListener(int position, String selectedString) {
                                edt_text.setText(selectedString);
                            }
                        });
                    }
                }
            });
            cancel_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    payDialog.dismiss();
                }
            });


        }


        private void showWorkInvolvementsSurveyDialog(int position) {

            LayoutInflater inflater = LayoutInflater.from(context);
            View dialogView = inflater.inflate(R.layout.work_tools, null);
            Spinner sp_survey_type_33_estimates = dialogView.findViewById(R.id.sp_survey_type_33_estimates);
            Spinner sp_survey_type_11_estimates = dialogView.findViewById(R.id.sp_survey_type_11_estimates);
            TextView ed_oh_33_feeder_1 = dialogView.findViewById(R.id.ed_oh_33_feeder_1);
            TextView ed_oh_11_feeder_1 = dialogView.findViewById(R.id.ed_oh_11_feeder_1);
            ImageView iv_close = dialogView.findViewById(R.id.iv_close);
            CheckBox checkBox33kv = dialogView.findViewById(R.id.checkbox_33kv);
            CheckBox checkBox11kv = dialogView.findViewById(R.id.checkbox_11kv);
            CheckBox checkBoxLt = dialogView.findViewById(R.id.checkbox_lt);
            CheckBox checkboxDTR = dialogView.findViewById(R.id.checkbox_DTR);
            LinearLayout main_33kv_layout_estimates = dialogView.findViewById(R.id.main_33kv_layout_estimates);
            LinearLayout main_11kv_layout_estimates = dialogView.findViewById(R.id.main_11kv_layout_estimate);
            LinearLayout main_lt_layout_estimates = dialogView.findViewById(R.id.main_lt_layout_estimates);
            LinearLayout layout_oh_33_feeder_1_estimate = dialogView.findViewById(R.id.layout_oh_33_feeder_1_estimate);
            LinearLayout layout_oh_11_feeder_1_estimate = dialogView.findViewById(R.id.layout_oh_11_feeder_1_estimate);
            LinearLayout layout_feeders_list_11kv = dialogView.findViewById(R.id.layout_feeders_list_11kv);
            LinearLayout layout_feeder_11kv = dialogView.findViewById(R.id.layout_feeder_11kv);
            Button startSurveyButton = dialogView.findViewById(R.id.start_survey_button);
            EditText dtr_structure_code = dialogView.findViewById(R.id.dtr_structure_code);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            databaseEstimates = new ArcGISDatabaseEstimates(context);

            isComplted33kVSurvey = databaseEstimates.get33kvSurveyStatusNEW(list.get(position).getEST_NO(), list.get(position).getGIS_ID());
            isComplted11kVSurvey = databaseEstimates.get11kvSurveyStatusNEW(list.get(position).getEST_NO(), list.get(position).getGIS_ID());
            isCompltedLTSurvey = databaseEstimates.getLTSurveyStatusNEW(list.get(position).getEST_NO(), list.get(position).getGIS_ID());

            boolean finalIsCompleted33kVServey = isComplted33kVSurvey;
            boolean finalIsCompleted11kvServey = isComplted11kVSurvey;
            boolean finalIsLTCompletedSurvey = isCompltedLTSurvey;


            sp_survey_type_11_estimates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String spSurveySelected11 = sp_survey_type_11_estimates.getSelectedItem().toString();
                    if (spSurveySelected11.equalsIgnoreCase("Select")) {
                        layout_feeders_list_11kv.setVisibility(View.GONE);
                    } else if (spSurveySelected11.equalsIgnoreCase("Proposed")) {
                        layout_feeders_list_11kv.setVisibility(View.GONE);
                    } else {
                        layout_feeders_list_11kv.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            dtr_structure_code.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SearchableSpinner searchableSpinner = new SearchableSpinner(context);
                    searchableSpinner.setWindowTitle("SELECT FEEDER");
                    searchableSpinner.setSpinnerListItems(EstimatesHome.dtr);
                    searchableSpinner.show();
                    searchableSpinner.setOnItemSelectListener(new OnItemSelectListener() {
                        @Override
                        public void setOnItemSelectListener(int position, String selectedString) {
                            dtr_structure_code.setText(selectedString);
                        }
                    });
                }
            });

            sp_survey_type_33_estimates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String spSurveySelected11 = sp_survey_type_33_estimates.getSelectedItem().toString();
                    if (spSurveySelected11.equalsIgnoreCase("Select")) {
                        layout_oh_33_feeder_1_estimate.setVisibility(View.GONE);
                    } else if (spSurveySelected11.equalsIgnoreCase("Proposed")) {
                        layout_oh_33_feeder_1_estimate.setVisibility(View.GONE);

                    } else {
                        layout_oh_33_feeder_1_estimate.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            layout_oh_33_feeder_1_estimate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    open33KVFeederMasterDialog(ed_oh_33_feeder_1);


                }
            });
            ed_oh_11_feeder_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    open11KVFeederMasterDialog(ed_oh_11_feeder_1);

                }
            });


            String line33KV = list.get(position).getLINE_33KV();
            if (line33KV == null || line33KV.equalsIgnoreCase("Y")) {
                checkBox33kv.setEnabled(true);
                is33kvSelected = true;
                checkBox33kv.setChecked(true);
                databaseEstimates.update33kvSurveyforReSurveyStatusNEW(list.get(position).getEST_NO(), true, list.get(position).getGIS_ID());
            } else if (line33KV.equalsIgnoreCase("N")) {
                checkBox33kv.setEnabled(false);
            }

            String line11KV = list.get(position).getLINE_11KV();
            if (line11KV == null || line11KV.equalsIgnoreCase("Y")) {
                checkBox11kv.setEnabled(true);
                checkBox11kv.setChecked(true);
                is11kvSelected = true;
                isLtSelected = true;
                databaseEstimates.update11kvSurveyforReSurveyStatusNEW(list.get(position).getEST_NO(), true, list.get(position).getGIS_ID());
            } else if (line11KV.equalsIgnoreCase("N")) {
                checkBox11kv.setEnabled(false);
            }
            String lineLT = list.get(position).getLINE_LT();
            if (lineLT == null || lineLT.equalsIgnoreCase("Y")) {
                isLtSelected = true;
                checkBoxLt.setEnabled(true);
                checkBoxLt.setChecked(true);
                databaseEstimates.updateLTSelectSurveyUpdateStatusNEW(list.get(position).getEST_NO(), true, list.get(position).getGIS_ID());
                isLtSelected = true;
            } else if (lineLT.equalsIgnoreCase("N")) {
                checkBoxLt.setEnabled(false);
            }

            checkBox33kv.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    is33kvSelected = true;
                    main_33kv_layout_estimates.setVisibility(View.VISIBLE);
                    if (isLtSelected) {
                        main_lt_layout_estimates.setVisibility(View.GONE);
                    }

                    // databaseEstimates.update33kvSelectSurveyUpdateStatusNEW(list.get(position).getEST_NO(), true);
                    //layout_feeder_33kv.setVisibility(View.VISIBLE);

                } else {
                    is33kvSelected = false;
                    if (isLtSelected) {
                        main_lt_layout_estimates.setVisibility(View.GONE);
                    } else {
                        main_lt_layout_estimates.setVisibility(View.VISIBLE);
                    }
                    layout_oh_33_feeder_1_estimate.setVisibility(View.GONE);
                    main_33kv_layout_estimates.setVisibility(View.GONE);
                }

            });
            checkBox11kv.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    is11kvSelected = true;
                    main_11kv_layout_estimates.setVisibility(View.VISIBLE);
                    if (isLtSelected) {
                        main_lt_layout_estimates.setVisibility(View.GONE);
                    }

                    // databaseEstimates.update11kvSelectSurveyUpdateStatusNEW(list.get(position).getEST_NO(), true);
                    //checkboxDTR.setVisibility(View.GONE);
                    // layout_feeder_11kv.setVisibility(View.VISIBLE);
                } else {
                    is11kvSelected = false;
                    if (isLtSelected) {
                        main_lt_layout_estimates.setVisibility(View.GONE);
                    } else {
                        main_lt_layout_estimates.setVisibility(View.VISIBLE);
                    }
                    layout_oh_11_feeder_1_estimate.setVisibility(View.GONE);
                    main_11kv_layout_estimates.setVisibility(View.GONE);
                    //  layout_feeder_11kv.setVisibility(View.GONE);
                }

            });
            checkBoxLt.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    isLtSelected = true;
                    if (isLtSelected && is11kvSelected) {
                        main_lt_layout_estimates.setVisibility(View.GONE);
                    } else if (isLtSelected) {
                        main_lt_layout_estimates.setVisibility(View.VISIBLE);
                    }
                } else {
                    main_lt_layout_estimates.setVisibility(View.GONE);
                }


            });

            builder.setView(dialogView);
            builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();
            iv_close.setOnClickListener(v -> {
                dialog.cancel();
            });

            startSurveyButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onStartClick(position, list.get(position));
                }
                //  databaseEstimates.updateSelectStatusALL(list.get(position).getEST_NO(), is33kvSelected, is11kvSelected, isLtSelected);
                if (is33kvSelected || is11kvSelected || isLtSelected) {
                    if (is33kvSelected) {
                        if (!finalIsCompleted33kVServey) {
                            msg = "Start 33kV Survey";
                        } else if (is11kvSelected && !finalIsCompleted11kvServey) {
                            msg = "Start 11kV Survey";
                        }
                    }
                    if (is11kvSelected && !is33kvSelected) {
                        if (!finalIsCompleted11kvServey) {
                            msg = "Start 11kV Survey";
                        }
                    }

                    if (isLtSelected) {
                        if (is33kvSelected && !finalIsCompleted33kVServey) {
                            msg = "Start 33kV Survey";
                        } else if (is11kvSelected && !finalIsCompleted11kvServey) {
                            msg = "Start 11kV Survey";
                        } else if (finalIsCompleted33kVServey && finalIsCompleted11kvServey) {
                            msg = "Start LT Survey";
                        }
                    }
                    if (is33kvSelected && is11kvSelected && isLtSelected) {
                        if (finalIsCompleted33kVServey && finalIsCompleted11kvServey && finalIsLTCompletedSurvey) {
                            msg = "Complete Survey";
                        }
                    }
//                    WorkInvolvementModel model=new WorkInvolvementModel();
//                    model.setFEEDER_NAME();
                    navigateNextScreen(position, is33kvSelected, is11kvSelected, isLtSelected);

                    //showAlertDialog(msg, position, is33kvSelected, is11kvSelected, isLtSelected);

                } else {
                    Toast.makeText(context, "Select at least one option.", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }


}
