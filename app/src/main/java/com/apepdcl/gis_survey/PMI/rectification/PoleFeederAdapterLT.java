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

import java.util.List;

public class PoleFeederAdapterLT extends RecyclerView.Adapter<PoleFeederAdapterLT.MyViewHolder> {
    private Context context;
    private List<PoleNosModelLT> list;
    OnItemClickListener listener;

    public PoleFeederAdapterLT(Context context, List<PoleNosModelLT> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(PoleNosModelLT model);
    }

    @NonNull
    @Override
    public PoleFeederAdapterLT.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(
                        R.layout.item_pole_feeder,
                        parent,
                        false
                );

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PoleFeederAdapterLT.MyViewHolder holder, int position) {
        PoleNosModelLT model = list.get(position);

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

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvPoleNo;
        TextView tvFeederId;
        LinearLayout layout_main;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPoleNo = itemView.findViewById(
                    R.id.tvPoleNo
            );
            layout_main = itemView.findViewById(R.id.layout_main);

//            tvFeederId = itemView.findViewById(
//                    R.id.tvFeederId
//            );
        }
    }
}
