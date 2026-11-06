package com.apepdcl.gis_survey.estimates.sou;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.estimates.models.InfoModel;

import java.util.ArrayList;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.MyViewHolder> {

    ArrayList<InfoModel> list;
    Context context;

    public InfoAdapter(ArrayList<InfoModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.info_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.header.setText(list.get(position).getFEATURE_SUB_TYPE());
        holder.desc.setText(list.get(position).getDESCRIPTION());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView header,desc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.header);
            desc = itemView.findViewById(R.id.desc);
        }
    }
}
