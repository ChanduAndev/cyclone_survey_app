package com.apepdcl.gis_survey.sasa;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.apepdcl.gis.app.R;

import com.apepdcl.gis_survey.module_11KV.ConvertImage;
import com.apepdcl.gis_survey.module_11KV.IntermediateMapActivity;
import com.apepdcl.gis_survey.popup.CustomAllDialogs;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.MyViewHolder> {

    ArrayList<QuestionModel> list;
    Context context;
    ImageSelectionListener imageSelectionListener;

    public QuestionAdapter(ArrayList<QuestionModel> list, Context context, ImageSelectionListener imageSelectionListener) {
        this.list = list;
        this.context = context;
        this.imageSelectionListener = imageSelectionListener;
    }

    public interface ImageSelectionListener {
        void onImageSelected(int position);

        void onImageSelected(Uri uri, int position);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.question_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        QuestionModel questionModel = list.get(position);
        String q = ""+(position+1);
        holder.question.setText(q+") "+questionModel.getQuestion());

        holder.radioGroup.setOnCheckedChangeListener(null); // Prevent unwanted triggers

        if (questionModel.getSelectedRadioButtonId() != -1) {
            holder.radioGroup.check(questionModel.getSelectedRadioButtonId());
            holder.image_layout.setVisibility(questionModel.getSelectedRadioButtonId() == R.id.yes ? View.VISIBLE : View.GONE);
        } else {
            holder.radioGroup.clearCheck();
            holder.image_layout.setVisibility(View.GONE);
        }

        holder.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            questionModel.setSelectedRadioButtonId(checkedId);
            if (checkedId == R.id.yes) {
                holder.image_layout.setVisibility(View.VISIBLE);
                questionModel.setAnswer("YES");

            } else {
                holder.image_layout.setVisibility(View.GONE);
                questionModel.setAnswer("NO");
                questionModel.setImage("N");
            }
        });

        System.out.println("IMAGE PATH : "+questionModel.getImage_path());

        if (questionModel.getAnswer().equalsIgnoreCase("YES")) {
            holder.image.setVisibility(View.VISIBLE);

            if (questionModel.getImage_from().equalsIgnoreCase("C")){
                if (questionModel.getImage_path() != null && !questionModel.getImage_path().isEmpty()) {
                    questionModel.setConvertedImage(ConvertImage.convertBase64(questionModel.getImage_path()));
                    Bitmap bitmap = BitmapFactory.decodeFile(questionModel.getImage_path());
                    holder.image.setImageBitmap(bitmap);
                   // holder.image.setRotation(90);

                }

            }
            if (questionModel.getImage_from().equalsIgnoreCase("G")){
                holder.image.setImageURI(Uri.parse(questionModel.getImage()));
                Uri uri =  Uri.parse(questionModel.getImage());
                String path = ConvertImage.getPathFromURI(uri, context);
                questionModel.setConvertedImage(ConvertImage.convertBase64(path));
            }

        } else {
            holder.image.setVisibility(View.GONE);
        }

        holder.upload_image.setOnClickListener(v -> {
            showImagePickerDialog(holder, position);
        });

    }


    private void showImagePickerDialog(MyViewHolder holder, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select Image From")
                .setItems(new CharSequence[]{"Camera", "Gallery"}, (dialog, which) -> {
                    if (which == 0) {
                        openCamera(holder, position);
                    } else {
                        openGallery(holder, position);
                    }
                })
                .show();
    }

    private void openCamera(MyViewHolder holder, int position) {
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
        list.get(position).setImage_path(currentPhotoPath_1);
        return image;
    }

    private void openGallery(MyViewHolder holder, int position) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Activity activity = (Activity) context;
        activity.startActivityForResult(galleryIntent, 200 + position);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView question;
        RadioGroup radioGroup;
        LinearLayout image_layout;
        ImageView image;
        TextView upload_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.question);
            radioGroup = itemView.findViewById(R.id.radioGroup);
            image_layout = itemView.findViewById(R.id.image_layout);
            image = itemView.findViewById(R.id.image);
            upload_image = itemView.findViewById(R.id.upload_image);
        }
    }


}
