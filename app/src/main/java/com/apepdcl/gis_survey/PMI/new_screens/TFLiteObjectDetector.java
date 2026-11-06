package com.apepdcl.gis_survey.PMI.new_screens;


/**
 * Created by SOUBHAGYA SWAIN on 31-07-2025.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class TFLiteObjectDetector {

 private static final String TAG = "TFLiteObjectDetector";

 private Interpreter interpreter;
 private final int INPUT_SIZE = 640;
 private final float CONFIDENCE_THRESHOLD = 0.5f;
 private final float MIN_HEIGHT_RATIO = 0.006f;
 private final float TB_MARGIN_RATIO = 0.006f;

 // Class to return both bitmap and match percentage
 public static class Result {
  public Bitmap annotatedBitmap;
  public float matchPercentage;
  public String status;

  public Result(Bitmap bitmap, float matchPercentage, String status) {
   this.annotatedBitmap = bitmap;
   this.matchPercentage = matchPercentage;
   this.status = status;
  }


 }

 public TFLiteObjectDetector(Context context) {
  try {
   ByteBuffer modelBuffer = FileUtil.loadMappedFile(context, "bet.tflite");
   interpreter = new Interpreter(modelBuffer);
  } catch (IOException e) {
   e.printStackTrace();
  }
 }

 public Result annotate(Bitmap bitmap) {
  int origHeight = bitmap.getHeight();
  int origWidth = bitmap.getWidth();

  Bitmap resized = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, true);
  ByteBuffer input = convertBitmapToByteBuffer(resized);

  float[][][] output = new float[1][5][8400];
  interpreter.run(input, output);

  Bitmap annotated = bitmap.copy(Bitmap.Config.ARGB_8888, true);
  Canvas canvas = new Canvas(annotated);

  Paint paintGreen = new Paint();
  paintGreen.setColor(Color.GREEN);
  paintGreen.setStyle(Paint.Style.STROKE);
  paintGreen.setStrokeWidth(4);

  Paint paintRed = new Paint(paintGreen);
  paintRed.setColor(Color.RED);

  Paint textPaint = new Paint();
  textPaint.setColor(Color.WHITE);
  textPaint.setTextSize(30);

  float maxMatchPercent = 0f;
  String status = "INVALID: no valid detection";

  for (int i = 0; i < 8400; i++) {
   float x = output[0][0][i];
   float y = output[0][1][i];
   float w = output[0][2][i];
   float h = output[0][3][i];
   float conf = output[0][4][i];

   if (conf < CONFIDENCE_THRESHOLD)
    continue;

   float x1 = (x - w / 2f) * origWidth;
   float y1 = (y - h / 2f) * origHeight;
   float x2 = (x + w / 2f) * origWidth;
   float y2 = (y + h / 2f) * origHeight;

   x1 = Math.max(0, Math.min(origWidth - 1, x1));
   y1 = Math.max(0, Math.min(origHeight - 1, y1));
   x2 = Math.max(0, Math.min(origWidth - 1, x2));
   y2 = Math.max(0, Math.min(origHeight - 1, y2));

   float height = y2 - y1;
   float hRatio = height / origHeight;
   float topMargin = y1 / origHeight;
   float bottomMargin = (origHeight - y2) / origHeight;

   if (hRatio < MIN_HEIGHT_RATIO)
    continue;

   float matchPercent = conf * 100f;
   if (matchPercent > maxMatchPercent) {
    maxMatchPercent = matchPercent;
   }

   boolean missingTop = topMargin < TB_MARGIN_RATIO;
   boolean missingBottom = bottomMargin < TB_MARGIN_RATIO;

   Paint paint = paintRed;

   if (!missingTop && !missingBottom) {
    paint = paintGreen;
    status = "VALID";
   } else if (missingTop && !missingBottom) {
    status = "INVALID: top not visible";
   } else if (!missingTop && missingBottom) {
    status = "INVALID: bottom not visible";
   } else {
    status = "INVALID: both top and bottom not visible";
   }

   canvas.drawRect(x1, y1, x2, y2, paint);
   canvas.drawText(status, x1, Math.max(y1 - 10, 0), textPaint);
   break; // only evaluate the first detection as per original logic
  }

  return new Result(annotated, maxMatchPercent, status);
 }

 private ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap) {
  ByteBuffer buffer = ByteBuffer.allocateDirect(1 * INPUT_SIZE * INPUT_SIZE * 3 * 4);
  buffer.order(ByteOrder.nativeOrder());

  int[] pixels = new int[INPUT_SIZE * INPUT_SIZE];
  bitmap.getPixels(pixels, 0, INPUT_SIZE, 0, 0, INPUT_SIZE, INPUT_SIZE);

  for (int pixel : pixels) {
   float r = ((pixel >> 16) & 0xFF) / 255.f;
   float g = ((pixel >> 8) & 0xFF) / 255.f;
   float b = (pixel & 0xFF) / 255.f;
   buffer.putFloat(r);
   buffer.putFloat(g);
   buffer.putFloat(b);
  }
  return buffer;
 }
}

