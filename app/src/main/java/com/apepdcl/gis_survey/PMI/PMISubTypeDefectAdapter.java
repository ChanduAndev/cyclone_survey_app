package com.apepdcl.gis_survey.PMI;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis_survey.PMI.models.DefectsModel;
import com.apepdcl.gis.app.R;
import com.apepdcl.gis_survey.module_11KV.ConvertImage;
import com.apepdcl.gis_survey.sasa.QuestionAdapter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by SOUBHAGYA SWAIN on 22-03-2025.
 */
public class PMISubTypeDefectAdapter extends RecyclerView.Adapter<PMISubTypeDefectAdapter.MyViewHolder> {

    ArrayList<DefectsModel> list;
    Context context;

    ImageSelectionListener imageSelectionListener;

    public PMISubTypeDefectAdapter(ArrayList<DefectsModel> list, Context context, ImageSelectionListener imageSelectionListener) {
        this.list = list;
        this.context = context;
        this.imageSelectionListener = imageSelectionListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_sub_defect, parent, false);
        return new MyViewHolder(view);
    }

    public interface ImageSelectionListener {
        void onImageSelected(int position, DefectsModel model);;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        DefectsModel model = new DefectsModel();


        holder.checkBoxType.setText(model.getDEFECT_SUB_TYPE());
        holder.checkBoxType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    holder.image_layout.setVisibility(View.VISIBLE);
                } else {
                    holder.image_layout.setVisibility(View.GONE);
                    list.get(position).setImageSelected(false);
                    list.get(position).setImagePath("");
                }
            }
        });


        if (model.isImageSelected()) {
            holder.image.setVisibility(View.VISIBLE);

            if (model.getImagePath() != null && !model.getImagePath().isEmpty()) {
                Bitmap bitmap = BitmapFactory.decodeFile(model.getImagePath());
                holder.image.setImageBitmap(bitmap);
                // holder.image.setRotation(90);

            }

        } else {
            holder.image.setVisibility(View.GONE);
        }

        holder.upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void openCamera(int position) {
        /*Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(context.getPackageManager()) != null) {
            Activity activity = (Activity) context;
            activity.startActivityForResult(cameraIntent, 100 + position);
        }*/

        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Activity activity = (Activity) context;
            File photoFile = createImageFile1(position);
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(activity, "com.apepdcl.gis_survey.fileprovider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            }
            activity.startActivityForResult(intent, 100 + position);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File createImageFile1(int position) throws IOException {
        // Create a unique file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        // Create a file for the image
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a path to the file (optional)
        String currentPhotoPath_1 = image.getAbsolutePath();
        list.get(position).setImagePath(currentPhotoPath_1);
        list.get(position).setImageSelected(true);
        return image;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout image_layout;
        CheckBox checkBoxType;
        ImageView image;
        TextView upload_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image_layout = itemView.findViewById(R.id.image_layout);
            checkBoxType = itemView.findViewById(R.id.checkBoxType);
            image = itemView.findViewById(R.id.image);
            upload_image = itemView.findViewById(R.id.upload_image);
        }
    }
}
