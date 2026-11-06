package com.apepdcl.gis_survey.estimates.sou;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
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
import com.apepdcl.gis_survey.estimates.sou.MaterialModel;

import java.util.ArrayList;

/**
 * Created by Soubhagya Swain on 21,November,2024
 */
public class MaterialAdapterABSwitch extends RecyclerView.Adapter<MaterialAdapterABSwitch.MyViewHolder> {

    ArrayList<MaterialModel> list;
    Context context;
    boolean IS_EDIT;
    boolean isDialogShowing = false;


    public MaterialAdapterABSwitch(ArrayList<MaterialModel> list, Context context, boolean IS_EDIT) {
        this.list = list;
        this.context = context;
        this.IS_EDIT = IS_EDIT;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.material_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MaterialModel item = list.get(position);

        if (IS_EDIT) {
            holder.uom.setText(item.getUOM());
            holder.desc.setText(item.getMATERIAL_DESC());
            holder.qty.setText(item.getQTY());
        } else {
            holder.qty.setText("0");
            holder.uom.setText(item.getUOM());
            holder.desc.setText(item.getMATERIAL_DESC());
            item.setQTY("0");
        }

//        holder.qty.setText("0");
//        holder.uom.setText(item.getUOM());
//        holder.desc.setText(item.getMATERIAL_DESC());
//        item.setQTY("0");


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

//        holder.qty.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
//                    if (s == null || s.isEmpty()) {
//                        item.setQTY("0");
//                        return;
//                    }
//                }
//
//                int number;
//                int max;
//
//                try {
//                    number = Integer.parseInt(s.toString());
//                    max = Integer.parseInt(item.getMAX());
//                } catch (NumberFormatException e) {
//                    item.setQTY("0");
//                    return;
//                }
//
//                if (number > max && !isDialogShowing) {
//                    isDialogShowing = true;
//                    new AlertDialog.Builder(context)
//                            .setTitle("Invalid Quantity")
//                            .setMessage("Quantity exceeds max limit "+item.getMAX())
//                            .setCancelable(false)
//                            .setPositiveButton("OK", (dialog, which) -> {
//                                dialog.dismiss();
//                                isDialogShowing = false;
//                                item.setQTY("0");
//                                // Reset value safely
//                                holder.qty.removeTextChangedListener(this);
//                                holder.qty.setSelection(holder.qty.getText().length());
//                                holder.qty.addTextChangedListener(this);
//
//
//                            })
//                            .show();
//
//                    // Prevent recursive call
//                    holder.qty.removeTextChangedListener(this);
//                    holder.qty.setText("0");
//                    holder.qty.setSelection(holder.qty.getText().length());
//                    holder.qty.addTextChangedListener(this);
//
//                    item.setQTY("0");
//                } else {
//                    item.setQTY(String.valueOf(number));
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });

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

