package com.apepdcl.gis_survey.utilits;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.estimates.models.DialogDataModel;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    ArrayList<DialogDataModel> list;
    Context context;

    public CustomAdapter(ArrayList<DialogDataModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_dialog_new_recyclerview, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, int position) {
        String html = list.get(position).getDESCRIPTION();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.tv_custom_text.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.tv_custom_text.setText(Html.fromHtml(html));
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_custom_text;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_custom_text = itemView.findViewById(R.id.tv_custom_text);
        }
    }
}
