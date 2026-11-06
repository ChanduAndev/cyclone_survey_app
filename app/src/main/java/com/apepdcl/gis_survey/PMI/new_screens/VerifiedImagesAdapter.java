package com.apepdcl.gis_survey.PMI.new_screens;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.PMI.models.PostingModel;
import com.apepdcl.gis_survey.module_11KV.ConvertImage;

import java.util.ArrayList;

/**
 * Created by SOUBHAGYA SWAIN on 08-09-2025.
 */
public class VerifiedImagesAdapter extends RecyclerView.Adapter<VerifiedImagesAdapter.MyViewHolder> {

    ArrayList<PostingModel> verifyOfflineImagesList;
    Context context;

    public VerifiedImagesAdapter(ArrayList<PostingModel> verifyOfflineImagesList, Context context) {
        this.verifyOfflineImagesList = verifyOfflineImagesList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
     View view = LayoutInflater.from(context).inflate(R.layout.adapter_pmi_verified_images, parent, false);
     return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        StringBuilder stringBuilder = new StringBuilder("");
        holder.location_no.setText("Location No: "+verifyOfflineImagesList.get(position).getPOLE_NO());
        holder.defect_type.setText("Defect Type: "+verifyOfflineImagesList.get(position).getDEFECT_TYPE());
        holder.defect_sub_type.setText("Defect Sub-Type: "+verifyOfflineImagesList.get(position).getDEFECT_SUB_TYPE());
        if (verifyOfflineImagesList.get(position).getIMAGE_TYPE().equalsIgnoreCase("OFFLINE")){
            holder.status.setText("Status : Rejected");
            holder.reason.setVisibility(View.VISIBLE);
//            holder.reason.setText("Reason : " + verifyOfflineImagesList.get(position).getAiResponseModel().get(0).getAI_RESPONSE());
        }else {
            holder.status.setText("Status : Approved");
            for (int i = 0; i < verifyOfflineImagesList.get(position).getAiResponseModel().size(); i++) {
                stringBuilder.append(verifyOfflineImagesList.get(position).getAiResponseModel().get(i).getAI_RESPONSE() + "\n");
            }
            holder.reason.setText("Defects : " +stringBuilder);
            holder.reason.setVisibility(View.VISIBLE);
        }
        String imagePath = verifyOfflineImagesList.get(position).getIMAGE();
        String dtrImage = verifyOfflineImagesList.get(position).getDTR_IMAGE();

        String selectedImagePath = null;

        if (imagePath != null && !imagePath.trim().isEmpty()) {
            selectedImagePath = imagePath;
        } else if (dtrImage != null && !dtrImage.trim().isEmpty()) {
            selectedImagePath = dtrImage;
        }
        Bitmap imageBitmap;
        if (selectedImagePath != null && !selectedImagePath.trim().isEmpty()) {
            imageBitmap = ConvertImage.getBitmapFromPath(selectedImagePath);
        } else {
            imageBitmap = null;
        }
//        Bitmap imageBitmap = ConvertImage.getBitmapFromPath(verifyOfflineImagesList.get(position).getIMAGE());
        holder.image.setImageBitmap(imageBitmap);
        
        // Set click listener for the image
        holder.image.setOnClickListener(v -> showFullImageDialog(context, imageBitmap));

    }

    @Override
    public int getItemCount() {
        return verifyOfflineImagesList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView location_no, status, reason,defect_type,defect_sub_type;
        ImageView image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            location_no = itemView.findViewById(R.id.location_no);
            status = itemView.findViewById(R.id.status);
            reason = itemView.findViewById(R.id.reason);
            image = itemView.findViewById(R.id.image);
            defect_type = itemView.findViewById(R.id.defect_type);
            defect_sub_type = itemView.findViewById(R.id.defect_sub_type);
        }
    }
    
    private void showFullImageDialog(Context context, Bitmap imageBitmap) {
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_fullscreen_image);
        
        ImageView fullScreenImage = dialog.findViewById(R.id.fullscreen_image);
        ImageView closeButton = dialog.findViewById(R.id.close_button);
        
        fullScreenImage.setImageBitmap(imageBitmap);
        
        closeButton.setOnClickListener(v -> dialog.dismiss());
        fullScreenImage.setOnClickListener(v -> dialog.dismiss());
        
        dialog.show();
    }
}
