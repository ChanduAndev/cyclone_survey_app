package com.apepdcl.gis_survey.PMI.rectification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.PMI.models.PoleModel11KV;

import java.util.List;

public class PoleFeederAdapter
        extends RecyclerView.Adapter<PoleFeederAdapter.ViewHolder> {

    private final Context context;
    private final List<PoleNosModel11> list;
    OnItemClickListener listener;

    public PoleFeederAdapter(Context context, List<PoleNosModel11> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(PoleNosModel11 model);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(
                        R.layout.item_pole_feeder,
                        parent,
                        false
                );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        PoleNosModel11 model = list.get(position);

        holder.tvPoleNo.setText(
                model.getPOLE_NO()
        );
        holder.layout_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(list.get(position));
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvPoleNo;
        TextView tvFeederId;
        LinearLayout layout_main;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPoleNo = itemView.findViewById(
                    R.id.tvPoleNo
            );
            layout_main=itemView.findViewById(R.id.layout_main);

//            tvFeederId = itemView.findViewById(
//                    R.id.tvFeederId
//            );
        }
    }
}
