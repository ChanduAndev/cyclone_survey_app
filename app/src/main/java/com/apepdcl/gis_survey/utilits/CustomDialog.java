

package com.apepdcl.gis_survey.utilits;

import static com.apepdcl.gis_survey.DashBoard.dialogDataModels;
import static com.apepdcl.gis_survey.estimates.new_module.EstimatesMap.IS_LINE_EXTENSION_11_DATA_AVAILABLE;
import static com.apepdcl.gis_survey.estimates.new_module.EstimatesMap.IS_LINE_EXTENSION_33_DATA_AVAILABLE;
import static com.apepdcl.gis_survey.estimates.new_module.EstimatesMap.IS_LINE_EXTENSION_LT_DATA_AVAILABLE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.estimates.models.DialogDataModel;

import java.util.ArrayList;
import java.util.Objects;

public class CustomDialog {

    public static void showCustomDialog(Context context, ArrayList<DialogDataModel> dialogDataModels) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dialog_new);

        RecyclerView recyclerView = dialog.findViewById(R.id.rv_dialog_custom);
        ImageView imageView = dialog.findViewById(R.id.iv_cancel);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new CustomAdapter(dialogDataModels, context));

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    @SuppressLint("SetTextI18n")
    public static void showMessageDialog(Context context, String titleText, String messageText,
                                         String est_no, String gis_id_no, int buttonCount, boolean isUpdate,
                                         String buttonText1, String buttonText2, CheckBox checkBox) {

        Dialog dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.customized_dialog, null);
        dialog.setContentView(view);

        TextView title = view.findViewById(R.id.title);
        TextView message = view.findViewById(R.id.message);
        TextView tv_est_no = view.findViewById(R.id.tv_est_no);
        TextView tv_gis_id = view.findViewById(R.id.tv_gis_id);


        TextView okBtn = view.findViewById(R.id.okBtn);
        TextView cancelBtn = view.findViewById(R.id.cancelBtn);

        title.setText(titleText);
        message.setText(messageText);

        dialog.setCancelable(false);

        // Transparent background
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        dialog.show();

        if (buttonCount == 2) {
            okBtn.setVisibility(View.VISIBLE);
            cancelBtn.setVisibility(View.VISIBLE);
        } else if (buttonCount == 1) {
            okBtn.setVisibility(View.VISIBLE);
            cancelBtn.setVisibility(View.GONE);
        }

        okBtn.setText(buttonText1);
        cancelBtn.setText(buttonText2);


        tv_est_no.setText(getColoredText("Est No: ", est_no, context));
        tv_gis_id.setText(getColoredText("GIS ID: ", gis_id_no, context));

        okBtn.setOnClickListener(v -> {
            if (checkBox != null) {
                checkBox.setChecked(true);
                dialog.dismiss();
            } else {
                dialog.dismiss();
            }
        });
        cancelBtn.setOnClickListener(v -> {
            if (checkBox != null) {
                checkBox.setChecked(false);
                dialog.dismiss();
            } else {
                dialog.dismiss();
            }

        });


    }

    @SuppressLint("SetTextI18n")
    public static void showMultipleMappedMessageDialog(Context context, String titleText,
                                                       String messageText, String est_no,
                                                       String gis_id_no, int buttonCount, String voltage_level) {

        Dialog dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.customized_dialog, null);
        dialog.setContentView(view);

        TextView title = view.findViewById(R.id.title);
        TextView message = view.findViewById(R.id.message);
        TextView tv_est_no = view.findViewById(R.id.tv_est_no);
        TextView tv_gis_id = view.findViewById(R.id.tv_gis_id);


        TextView okBtn = view.findViewById(R.id.okBtn);
        TextView cancelBtn = view.findViewById(R.id.cancelBtn);

        title.setText(titleText);
        message.setText(messageText);

        dialog.setCancelable(false);

        // Transparent background
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        dialog.show();

        if (buttonCount == 2) {
            okBtn.setVisibility(View.VISIBLE);
            cancelBtn.setVisibility(View.VISIBLE);
        } else if (buttonCount == 1) {
            okBtn.setVisibility(View.VISIBLE);
            cancelBtn.setVisibility(View.GONE);
        }

        tv_est_no.setText(getColoredText("Est No: ", est_no, context));
        tv_gis_id.setText(getColoredText("GIS ID: ", gis_id_no, context));

        okBtn.setOnClickListener(v -> {
            if ("LT".equalsIgnoreCase(voltage_level)) {
                IS_LINE_EXTENSION_LT_DATA_AVAILABLE = false;
            } else if ("33KV".equalsIgnoreCase(voltage_level)) {
                IS_LINE_EXTENSION_33_DATA_AVAILABLE = false;
            } else if ("11KV".equalsIgnoreCase(voltage_level)) {
                IS_LINE_EXTENSION_11_DATA_AVAILABLE = false;
            }
            if (context instanceof Activity) {
                ((Activity) context).onBackPressed();
            }
            dialog.dismiss();
        });


    }

    @SuppressLint("SetTextI18n")
    public static void showDTRMessageDialog(Context context, String titleText,
                                            String messageText, String est_no,
                                            String gis_id_no, int buttonCount, TextView textView) {

        Dialog dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.customized_dialog, null);
        dialog.setContentView(view);

        TextView title = view.findViewById(R.id.title);
        TextView message = view.findViewById(R.id.message);
        TextView tv_est_no = view.findViewById(R.id.tv_est_no);
        TextView tv_gis_id = view.findViewById(R.id.tv_gis_id);


        TextView okBtn = view.findViewById(R.id.okBtn);
        TextView cancelBtn = view.findViewById(R.id.cancelBtn);

        title.setText(titleText);
        message.setText(messageText);

        dialog.setCancelable(false);

        // Transparent background
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        dialog.show();

        if (buttonCount == 2) {
            okBtn.setVisibility(View.VISIBLE);
            cancelBtn.setVisibility(View.VISIBLE);
        } else if (buttonCount == 1) {
            okBtn.setVisibility(View.VISIBLE);
            cancelBtn.setVisibility(View.GONE);
        }

        tv_est_no.setText(getColoredText("Est No: ", est_no, context));
        tv_gis_id.setText(getColoredText("GIS ID: ", gis_id_no, context));

        okBtn.setOnClickListener(v -> {
            textView.setText("");
            dialog.dismiss();
        });


    }


    @SuppressLint("SetTextI18n")
    public static void showDTRShiftedMessageDialog(Context context, String titleText,
                                                   String messageText, String est_no,
                                                   String gis_id_no, int buttonCount, TextView textView) {

        Dialog dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.customized_dialog, null);
        dialog.setContentView(view);

        TextView title = view.findViewById(R.id.title);
        TextView message = view.findViewById(R.id.message);
        TextView tv_est_no = view.findViewById(R.id.tv_est_no);
        TextView tv_gis_id = view.findViewById(R.id.tv_gis_id);


        TextView okBtn = view.findViewById(R.id.okBtn);
        TextView cancelBtn = view.findViewById(R.id.cancelBtn);

        title.setText(titleText);
        message.setText(messageText);

        dialog.setCancelable(false);

        // Transparent background
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        dialog.show();

        if (buttonCount == 2) {
            okBtn.setVisibility(View.VISIBLE);
            cancelBtn.setVisibility(View.VISIBLE);
        } else if (buttonCount == 1) {
            okBtn.setVisibility(View.VISIBLE);
            cancelBtn.setVisibility(View.GONE);
        }
        okBtn.setText("Yes,DTR Shifted");
        cancelBtn.setText("Cancel");

        tv_est_no.setText(getColoredText("Est No: ", est_no, context));
        tv_gis_id.setText(getColoredText("GIS ID: ", gis_id_no, context));

        okBtn.setOnClickListener(v -> {
            textView.setText("");
            dialog.dismiss();
        });
        cancelBtn.setOnClickListener(v -> dialog.dismiss());


    }

    public static SpannableString getColoredText(String label, String value, Context context) {

        SpannableString spannable = new SpannableString(label + value);

        spannable.setSpan(
                new ForegroundColorSpan(ContextCompat.getColor(context, R.color.ui_gray_color)),
                0,
                label.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        spannable.setSpan(
                new ForegroundColorSpan(ContextCompat.getColor(context, R.color.ui_black_color)),
                label.length(),
                label.length() + value.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        // Value bold
        spannable.setSpan(
                new StyleSpan(Typeface.BOLD),
                label.length(),
                label.length() + value.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        return spannable;
    }


}

