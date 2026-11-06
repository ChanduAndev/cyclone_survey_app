package com.apepdcl.gis_survey.PMI;


import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.PMI.models.DefectGroupModel;
import com.apepdcl.gis_survey.PMI.models.DefectsModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by SOUBHAGYA SWAIN on 02-04-2026.
 */
public class DefectGroupAdapter extends RecyclerView.Adapter<DefectGroupAdapter.ViewHolder> {

    private Context context;
    private ArrayList<DefectGroupModel> groupList;

    public DefectGroupAdapter(Context context, ArrayList<DefectGroupModel> groupList) {
        this.context = context;
        this.groupList = groupList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvGroup;
        ImageView ivArrow;
        RecyclerView rvChild;
        LinearLayout expandableLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvGroup = itemView.findViewById(R.id.tvGroup);
            ivArrow = itemView.findViewById(R.id.ivArrow);
            rvChild = itemView.findViewById(R.id.rvChild);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_defect_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        DefectGroupModel group = groupList.get(position);

        holder.tvGroup.setText(group.getDefectGroup());

        holder.expandableLayout.setVisibility(
                group.isExpanded() ? View.VISIBLE : View.GONE
        );

        holder.ivArrow.setRotation(group.isExpanded() ? 180 : 0);

        holder.itemView.setOnClickListener(v -> {
            group.setExpanded(!group.isExpanded());
            notifyItemChanged(position);
        });

        List<DefectsModel> defectsList = group.getDefects();

        List<DefectsModel> uniqueList = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uniqueList = new ArrayList<>(
                    defectsList.stream()
                            .collect(Collectors.toMap(
                                    DefectsModel::getDEFECT_TYPE,
                                    d -> d,
                                    (existing, replacement) -> existing,
                                    LinkedHashMap::new  // preserves insertion order
                            ))
                            .values()
            );
        }

        holder.rvChild.setLayoutManager(new LinearLayoutManager(context));
        holder.rvChild.setAdapter(
                new DefectTypeAdapter(context, uniqueList)
        );
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

}
