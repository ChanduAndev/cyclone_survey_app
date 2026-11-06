package com.apepdcl.gis_survey.METER_SURVEY;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;

import java.util.ArrayList;

public class AreaCodeAdapter extends RecyclerView.Adapter<AreaCodeAdapter.MyHolder> {

    private ArrayList<AreaCodeModel> originalList;
    private ArrayList<AreaCodeModel> displayList;

    public interface OnSelectionChangedListener {
        void onSelectionChanged(int count);
    }

    private OnSelectionChangedListener listener;

    public void setOnSelectionChangedListener(OnSelectionChangedListener listener) {
        this.listener = listener;
    }

    public AreaCodeAdapter(ArrayList<AreaCodeModel> list) {
        this.originalList = list;
        this.displayList = new ArrayList<>(list);
    }

    class MyHolder extends RecyclerView.ViewHolder {

        CheckBox checkArea;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            checkArea = itemView.findViewById(R.id.checkArea);
        }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_area_code, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        AreaCodeModel model = displayList.get(position);

        holder.checkArea.setOnCheckedChangeListener(null);

        holder.checkArea.setText(
                model.getAreaName() + " (" + model.getAreaCode() + ")"
        );

        holder.checkArea.setChecked(model.isSelected());

        holder.checkArea.setOnCheckedChangeListener((buttonView, isChecked) -> {

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

    public void filter(String text) {

        displayList.clear();

        if (text == null || text.trim().isEmpty()) {

            displayList.addAll(originalList);

        } else {

            text = text.toLowerCase();

            for (AreaCodeModel model : originalList) {

                if (model.getAreaCode().toLowerCase().contains(text)
                        || model.getAreaName().toLowerCase().contains(text)) {

                    displayList.add(model);
                }
            }
        }

        notifyDataSetChanged();
    }

    public int getSelectedCount() {

        int count = 0;

        for (AreaCodeModel model : originalList) {

            if (model.isSelected()) {
                count++;
            }
        }

        return count;
    }

    public ArrayList<String> getSelectedCodes() {

        ArrayList<String> selectedCodes = new ArrayList<>();

        for (AreaCodeModel model : originalList) {

            if (model.isSelected()) {
                selectedCodes.add(model.getAreaCode());
            }
        }

        return selectedCodes;
    }
}
