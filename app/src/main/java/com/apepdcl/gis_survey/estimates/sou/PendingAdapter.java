package com.apepdcl.gis_survey.estimates.sou;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.estimates.PurposeAdapter;
import com.apepdcl.gis_survey.estimates.models.PurposeModel;

import java.util.ArrayList;

/**
 * Created by Soubhagya Swain on 19,November,2024
 */
class PendingAdapter extends RecyclerView.Adapter<PendingAdapter.MyViewHolder> {

    ArrayList<PurposeModel> list;
    Context context;
    OnItemClickListener listener;

    public PendingAdapter(ArrayList<PurposeModel> list, Context context, OnItemClickListener listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, PurposeModel model);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ongoing_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.est_no.setText("EST No.: " + list.get(position).getEST_NO());
        holder.purpose.setText(list.get(position).getPURPOSE());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(position, list.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView est_no, purpose;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            est_no = itemView.findViewById(R.id.est_no);
            purpose = itemView.findViewById(R.id.purpose);
        }
    }
}
