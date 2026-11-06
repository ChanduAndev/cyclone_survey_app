package com.apepdcl.gis_survey.METER_SURVEY;


import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.estimates.EstimatesHome;
import com.apepdcl.gis_survey.estimates.PrepareEstimate;
import com.bumptech.glide.Glide;
import com.leo.searchablespinner.SearchableSpinner;
import com.leo.searchablespinner.interfaces.OnItemSelectListener;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by SOUBHAGYA SWAIN on 09-06-2026.
 */
public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.MyViewHolder> {

    ArrayList<ServiceData> serviceList;
    AdapterListener listener;

    Context context;

    public ServicesAdapter(ArrayList<ServiceData> serviceList, AdapterListener listener, Context context) {
        this.serviceList = serviceList;
        this.listener = listener;
        this.context = context;
    }

    public interface AdapterListener{
        void onCaptureImage(int position);
        void onPickLocation(int position);

        void onSelectServiceNumber(int position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.service_list_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int sl_no = position + 1;
        holder.sl_no.setText("" + sl_no);

        ServiceData data = serviceList.get(position);

        holder.select_service_number.setText(data.getServiceNumber());

        holder.select_service_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSelectServiceNumber(position);
            }
        });

        holder.btn_capture_image.setOnClickListener(v -> {
            listener.onCaptureImage(position);
        });

        holder.btn_capture_location.setOnClickListener(v -> {
            listener.onPickLocation(position);
        });

        if (!TextUtils.isEmpty(data.getImageUri())) {

            Glide.with(holder.itemView.getContext())
                    .load(Uri.parse(data.getImageUri()))
                    .into(holder.captured_image);

        } else {

            holder.captured_image.setImageResource(
                    R.drawable.outline_add_a_photo_24);
        }

        if(data.isLocationCaptured()){

            holder.before_location.setVisibility(View.GONE);
            holder.after_location.setVisibility(View.VISIBLE);

        }else{

            holder.before_location.setVisibility(View.VISIBLE);
            holder.after_location.setVisibility(View.GONE);
        }



    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView sl_no,select_service_number,btn_capture_image,btn_capture_location;
        ImageView captured_image,before_location,after_location;


        public MyViewHolder(View itemView) {
            super(itemView);
            sl_no = itemView.findViewById(R.id.sl_no);
            select_service_number = itemView.findViewById(R.id.select_service_number);
            btn_capture_image = itemView.findViewById(R.id.btn_capture_image);
            captured_image = itemView.findViewById(R.id.captured_image);
            btn_capture_location = itemView.findViewById(R.id.btn_capture_location);
            before_location = itemView.findViewById(R.id.before_location);
            after_location = itemView.findViewById(R.id.after_location);

        }
    }

}
