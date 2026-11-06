package com.apepdcl.gis_survey.estimates.sou;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.estimates.models.SectionsModel;

import java.util.ArrayList;

public class SectionsAdapter extends RecyclerView.Adapter<SectionsAdapter.MyViewHolder> {
    private ArrayList<SectionsModel> originalList;
    private ArrayList<SectionsModel> displayList;

    public interface OnSelectionChangedListener {
        void onSelectionChanged(int count);
    }

    private SectionsAdapter.OnSelectionChangedListener listener;

    public void setOnSelectionChangedListener(SectionsAdapter.OnSelectionChangedListener listener) {
        this.listener = listener;
    }

    public SectionsAdapter(ArrayList<SectionsModel> list) {
        this.originalList = list;
        this.displayList = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public SectionsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_area_code, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionsAdapter.MyViewHolder holder, int position) {
        SectionsModel model = displayList.get(position);
        holder.sectionCheck.setOnCheckedChangeListener(null);

//        holder.sectionCheck.setText(
//                model.getSECTION_ID() + " (" + model.getSECTION_NAME() + ")"
//        );

        holder.sectionCheck.setText(
                model.getSECTION_NAME()
        );

        holder.sectionCheck.setChecked(model.isSelected());

        holder.sectionCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {

            model.setSelected(isChecked);
            if (listener != null) {
                listener.onSelectionChanged(getSelectedCount());
            }
        });
    }

    @Override
    public int getItemCount() {
        return displayList.size();
    }

    public int getSelectedCount() {

        int count = 0;

        for (SectionsModel model : originalList) {

            if (model.isSelected()) {
                count++;
            }
        }

        return count;
    }

    public ArrayList<String> getSelectedCodes() {

        ArrayList<String> selectedCodes = new ArrayList<>();

        for (SectionsModel model : originalList) {

            if (model.isSelected()) {
                selectedCodes.add(model.getSECTION_ID());
            }
        }

        return selectedCodes;
    }

    public void filter(String text) {

        displayList.clear();

        if (text == null || text.trim().isEmpty()) {

            displayList.addAll(originalList);

        } else {

            text = text.toLowerCase();

            for (SectionsModel model : originalList) {

                if (model.getSECTION_ID().toLowerCase().contains(text)
                        || model.getSECTION_NAME().toLowerCase().contains(text)) {

                    displayList.add(model);
                }
            }
        }

        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox sectionCheck;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionCheck = itemView.findViewById(R.id.checkArea);
        }
    }
}
