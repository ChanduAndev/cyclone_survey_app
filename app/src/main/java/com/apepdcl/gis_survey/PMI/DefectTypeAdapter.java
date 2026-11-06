package com.apepdcl.gis_survey.PMI;


import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.PMI.models.DefectsModel;
import com.apepdcl.gis_survey.PMI.new_screens.PMINewHome;
import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by SOUBHAGYA SWAIN on 02-04-2026.
 */
public class DefectTypeAdapter extends RecyclerView.Adapter<DefectTypeAdapter.ViewHolder> {

    private Context context;
    private List<DefectsModel> defects;

    private OnImageActionListener listener;

    public DefectTypeAdapter(Context context, List<DefectsModel> defects) {
        this.context = context;
        this.defects = defects;
        this.listener = listener;
    }

    public interface OnImageActionListener {
        void onCaptureClick(int position, DefectsModel defect);
        void onRetakeClick(int position, DefectsModel defect);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;
        ChipGroup chipGroup;
        LinearLayout captureLayout;
        LinearLayout chipContainer;

        LinearLayout taken_image_layout,layoutChangeImage;
        ImageView imgPreview,fullImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkboxDefect);
            chipGroup = itemView.findViewById(R.id.chipGroup);
            captureLayout = itemView.findViewById(R.id.captureLayout);
            chipContainer = itemView.findViewById(R.id.chipContainer);
            taken_image_layout = itemView.findViewById(R.id.taken_image_layout);
            layoutChangeImage = itemView.findViewById(R.id.layoutChangeImage);
            imgPreview = itemView.findViewById(R.id.imgPreview);
            fullImage = itemView.findViewById(R.id.fullImage);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_defect_child, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        DefectsModel defect = defects.get(position);

        holder.checkBox.setText(defect.getDEFECT_TYPE());

        // 1. Remove listener before setting checked state to avoid false triggers
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(defect.isSelected());

        // 2. Restore visibility based on current state
        if (defect.isSelected()) {
            holder.chipContainer.setVisibility(View.VISIBLE);
            ArrayList<DefectsModel> allSubDefects = new ArrayList<>();
            for (int i = 0; i < PMINewHome.allDefectsList.size(); i++) {
                if (defect.getDEFECT_TYPE().equals(PMINewHome.allDefectsList.get(i).getDEFECT_TYPE())){
                    allSubDefects.add(PMINewHome.allDefectsList.get(i));
                }
            }

            List<DefectsModel> uniqueList = new ArrayList<>(
                    allSubDefects.stream()
                            .collect(Collectors.toMap(
                                    DefectsModel::getDEFECT_SUB_TYPE,
                                    d -> d,
                                    (existing, replacement) -> existing,
                                    LinkedHashMap::new  // preserves insertion order
                            ))
                            .values()
            );
            System.out.println("Sub defects Size isSelected : "+uniqueList.size());
            addSubDefectChipOld(holder.chipGroup, uniqueList);
        } else {
            holder.chipContainer.setVisibility(View.GONE);
            holder.chipGroup.removeAllViews();
        }

        // 3. Restore captureLayout visibility
        holder.captureLayout.setVisibility(
                "Y".equals(defect.getPHOTO()) && "N".equals(defect.getAI()) && defect.isSelected()
                        ? View.VISIBLE : View.GONE
        );

        // 4. Now set the listener
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {

            defect.setSelected(isChecked);

            if (isChecked) {
                holder.chipContainer.setVisibility(View.VISIBLE);
                ArrayList<DefectsModel> allSubDefects = new ArrayList<>();
                for (int i = 0; i < PMINewHome.allDefectsList.size(); i++) {
                    if (defect.getDEFECT_TYPE().equals(PMINewHome.allDefectsList.get(i).getDEFECT_TYPE())){
                        allSubDefects.add(PMINewHome.allDefectsList.get(i));
                    }
                }
                List<DefectsModel> uniqueList = new ArrayList<>(
                        allSubDefects.stream()
                                .collect(Collectors.toMap(
                                        DefectsModel::getDEFECT_SUB_TYPE,
                                        d -> d,
                                        (existing, replacement) -> existing,
                                        LinkedHashMap::new  // preserves insertion order
                                ))
                                .values()
                );
                System.out.println("Sub defects Size isChecked : "+uniqueList.size());
                addSubDefectChipOld(holder.chipGroup, uniqueList);
            } else {
                holder.chipContainer.setVisibility(View.GONE);
                holder.chipGroup.removeAllViews();
            }

            holder.captureLayout.setVisibility(
                    "Y".equals(defect.getPHOTO()) && "N".equals(defect.getAI()) && isChecked
                            ? View.VISIBLE : View.GONE
            );
        });

        holder.captureLayout.setOnClickListener(v -> {
            listener.onCaptureClick(position, defect);
        });

        holder.layoutChangeImage.setOnClickListener(v -> {
            listener.onRetakeClick(position, defect);
        });

        if (defect.isImageSelected()) {

            holder.captureLayout.setVisibility(View.GONE);
            holder.taken_image_layout.setVisibility(View.VISIBLE);

            Glide.with(context)
                    .load(defect.getImagePath())
                    .into(holder.imgPreview);

        } else {

            holder.captureLayout.setVisibility(View.VISIBLE);
            holder.taken_image_layout.setVisibility(View.GONE);
        }
    }

    private void addSubDefectChipOld(ChipGroup chipGroup, List<DefectsModel> defect) {

        chipGroup.removeAllViews();

        for (int i = 0; i < defect.size(); i++) {
            Chip chip = new Chip(new ContextThemeWrapper(context,
                    com.google.android.material.R.style.Widget_MaterialComponents_Chip_Choice));

            chip.setText(defect.get(i).getDEFECT_SUB_TYPE());
            chip.setCheckedIconVisible(false);

            int[][] states = new int[][]{
                    new int[]{android.R.attr.state_checked},
                    new int[]{-android.R.attr.state_checked}
            };
            int[] colors = new int[]{
                    Color.parseColor("#2F80ED"),  // checked
                    Color.parseColor("#AAAAAA")   // unchecked
            };

            chip.setChipBackgroundColor(new ColorStateList(states, colors));
            chip.setTextColor(Color.WHITE);
            chip.setCheckable(true);
            chip.setClickable(true);

            chipGroup.addView(chip);
        }


    }

    @Override
    public int getItemCount() {
        return defects.size();
    }
}
