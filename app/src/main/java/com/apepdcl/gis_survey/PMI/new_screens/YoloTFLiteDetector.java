package com.apepdcl.gis_survey.PMI.new_screens;



import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.util.Log;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class YoloTFLiteDetector {

    private static final String TAG = "YoloTFLiteDetector";
    private Interpreter interpreter;
    private int inputSize = 640;  // your model is most likely 640x640

    public static class Detection {
        public float x1, y1, x2, y2;
        public float confidence;
        public int cls;

        Detection(float x1, float y1, float x2, float y2, float confidence, int cls) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.confidence = confidence;
            this.cls = cls;
        }
    }

    // Load model from assets
    public YoloTFLiteDetector(AssetManager assetManager, String modelPath) throws IOException {
        Interpreter.Options options = new Interpreter.Options();
        options.setNumThreads(4);

        interpreter = new Interpreter(loadModelFile(assetManager, modelPath), options);
    }

    private ByteBuffer loadModelFile(AssetManager assets, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = assets.openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();

        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    // Convert Bitmap → ByteBuffer
    private ByteBuffer bitmapToFloat32Buffer(Bitmap bitmap) {
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, true);

        ByteBuffer input = ByteBuffer.allocateDirect(1 * inputSize * inputSize * 3 * 4);
        input.order(ByteOrder.nativeOrder());
        input.rewind();

        int[] pixels = new int[inputSize * inputSize];
        resized.getPixels(pixels, 0, inputSize, 0, 0, inputSize, inputSize);

        int idx = 0;
        for (int i = 0; i < pixels.length; i++) {
            int px = pixels[i];
            float r = ((px >> 16) & 0xFF) / 255f;
            float g = ((px >> 8) & 0xFF) / 255f;
            float b = (px & 0xFF) / 255f;

            input.putFloat(r);
            input.putFloat(g);
            input.putFloat(b);
        }

        return input;
    }

    public ArrayList<Detection> run(Bitmap bitmap) {

        ByteBuffer input = bitmapToFloat32Buffer(bitmap);

        // Correct output shape based on your model: [1, 6, 8400]
        float[][][] output = new float[1][6][8400];

        interpreter.run(input, output);

        ArrayList<Detection> detections = new ArrayList<>();

        for (int i = 0; i < 8400; i++) {

            float x = output[0][0][i];
            float y = output[0][1][i];
            float w = output[0][2][i];
            float h = output[0][3][i];
            float conf = output[0][4][i];
            int cls = (int) output[0][5][i];

            if (conf < 0.30f) continue;

            // cxcywh -> x1y1x2y2
            float x1 = x - w / 2;
            float y1 = y - h / 2;
            float x2 = x + w / 2;
            float y2 = y + h / 2;

            // scale
            float scaleX = bitmap.getWidth() / (float) inputSize;
            float scaleY = bitmap.getHeight() / (float) inputSize;

            x1 *= scaleX;
            y1 *= scaleY;
            x2 *= scaleX;
            y2 *= scaleY;

            detections.add(new Detection(x1, y1, x2, y2, conf, cls));
        }

        return detections;
    }


    // Perform inference
//    public ArrayList<Detection> run(Bitmap bitmap) {
//
//        ByteBuffer input = bitmapToFloat32Buffer(bitmap);
//
//        // OUTPUT FORMAT (YOLOv8-TFLite)
//        // array shape: [1, 8400, 6]
//        // each row: [x, y, w, h, conf, class]
//
//        //float[][][] output = new float[1][8400][6];
//
//        float[][][] output = new float[1][6][8400];
//
//
//        interpreter.run(input, output);
//
//        ArrayList<Detection> detections = new ArrayList<>();
//
//        for (int i = 0; i < 8400; i++) {
//            float x = output[0][i][0];
//            float y = output[0][i][1];
//            float w = output[0][i][2];
//            float h = output[0][i][3];
//            float conf = output[0][i][4];
//            int cls = (int) output[0][i][5];
//
//            if (conf < 0.30f) continue;  // keep threshold low, validator will re-check
//
//            // Convert cxcywh → x1,y1,x2,y2
//            float x1 = x - w / 2;
//            float y1 = y - h / 2;
//            float x2 = x + w / 2;
//            float y2 = y + h / 2;
//
//            // Scale to original bitmap size
//            float scaleX = bitmap.getWidth() / (float) inputSize;
//            float scaleY = bitmap.getHeight() / (float) inputSize;
//
//            x1 *= scaleX;
//            y1 *= scaleY;
//            x2 *= scaleX;
//            y2 *= scaleY;
//
//            detections.add(new Detection(x1, y1, x2, y2, conf, cls));
//        }
//
//        return detections;
//    }
}

