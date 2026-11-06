package com.apepdcl.gis_survey.estimates.sou;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;

import java.util.ArrayList;

public class MaterialAdapterForUpdatePole extends RecyclerView.Adapter<MaterialAdapterForUpdatePole.MyViewHolder>{
    ArrayList<MaterialModelPole> list;
    Context context;

    public MaterialAdapterForUpdatePole(ArrayList<MaterialModelPole> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MaterialAdapterForUpdatePole.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.material_adapter, parent, false);
        return new MaterialAdapterForUpdatePole.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MaterialAdapterForUpdatePole.MyViewHolder holder, int position) {
        MaterialModelPole item = list.get(position);
        holder.uom.setText(item.getUOM());
        holder.desc.setText(item.getMATERIAL_DESC());
        holder.qty.setText(item.getQTY());



        holder.qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                item.setQTY(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        EditText qty;
        TextView uom, desc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            qty = itemView.findViewById(R.id.qty);
            uom = itemView.findViewById(R.id.uom);
            desc = itemView.findViewById(R.id.desc);
        }
    }
}
