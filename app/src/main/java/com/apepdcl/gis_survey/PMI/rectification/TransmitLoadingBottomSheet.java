package com.apepdcl.gis_survey.PMI.rectification;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.apepdcl.gis.app.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

/**
 * Created by SOUBHAGYA SWAIN on 01-04-2026.
 */
public class TransmitLoadingBottomSheet {
    private static BottomSheetDialog dialog;

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing()) return;

        if (dialog != null && dialog.isShowing()) return;

        dialog = new BottomSheetDialog(activity, R.style.BottomSheetDialogTheme);

        View view = LayoutInflater.from(activity)
                .inflate(R.layout.bottomsheet_api_loader, null);

        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        Button btnCancel = view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> dismiss());

        dialog.setOnShowListener(dialogInterface -> {
            View bottomSheet = dialog.findViewById(
                    R.id.design_bottom_sheet
            );

            if (bottomSheet != null) {
                bottomSheet.setBackgroundResource(R.drawable.bg_bottom_sheet_rounded);

                BottomSheetBehavior<View> behavior =
                        BottomSheetBehavior.from(bottomSheet);

                behavior.setDraggable(false);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        dialog.show();
    }

    public static void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
