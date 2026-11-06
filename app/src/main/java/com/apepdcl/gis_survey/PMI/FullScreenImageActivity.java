package com.apepdcl.gis_survey.PMI;


/**
 * Created by SOUBHAGYA SWAIN on 07-04-2026.
 */



import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class FullScreenImageActivity extends Activity {

    public static final String EXTRA_BITMAP_KEY = "bitmap_key";

    private ImageView imageView;
    private Matrix matrix = new Matrix();
    private float scaleFactor = 1.0f;
    private float minScale = 1.0f;
    private float maxScale = 5.0f;

    // For drag/pan
    private float lastTouchX, lastTouchY;
    private float translateX = 0f, translateY = 0f;
    private boolean isDragging = false;
    private static final int POINTER_COUNT_SINGLE = 1;

    private ScaleGestureDetector scaleGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Full screen + immersive
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );

        imageView = new ImageView(this);
        imageView.setLayoutParams(new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
        ));
        imageView.setScaleType(ImageView.ScaleType.MATRIX);
        imageView.setBackgroundColor(0xFF000000);

        Bitmap bitmap = BitmapCache.get(EXTRA_BITMAP_KEY);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            // Center the image once layout is ready
            imageView.post(() -> centerImage(bitmap));
        }

        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        imageView.setOnTouchListener((v, event) -> {
            scaleGestureDetector.onTouchEvent(event);

            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    lastTouchX = event.getX();
                    lastTouchY = event.getY();
                    isDragging = true;
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (!scaleGestureDetector.isInProgress()
                            && event.getPointerCount() == POINTER_COUNT_SINGLE
                            && isDragging) {
                        float dx = event.getX() - lastTouchX;
                        float dy = event.getY() - lastTouchY;
                        translateX += dx;
                        translateY += dy;
                        lastTouchX = event.getX();
                        lastTouchY = event.getY();
                        clampTranslation();
                        applyMatrix();
                    }
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    isDragging = false;
                    // Snap back if zoomed out below minScale
                    if (scaleFactor <= minScale) {
                        resetToCenter();
                    }
                    break;
            }
            return true;
        });

        android.widget.FrameLayout root = new android.widget.FrameLayout(this);

// Add imageView fresh — don't call setContentView(imageView) separately
        root.addView(imageView, new android.widget.FrameLayout.LayoutParams(
                android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
                android.widget.FrameLayout.LayoutParams.MATCH_PARENT
        ));

        android.widget.ImageButton backBtn = new android.widget.ImageButton(this);
        backBtn.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
        backBtn.setBackgroundColor(0x88000000);
        android.widget.FrameLayout.LayoutParams params =
                new android.widget.FrameLayout.LayoutParams(120, 120);
        params.gravity = android.view.Gravity.TOP | Gravity.END;
        params.setMargins(0, 98, 65, 0);
        backBtn.setLayoutParams(params);
        backBtn.setOnClickListener(v -> finish());

        root.addView(backBtn);
        setContentView(root);  // only called once, on root
    }

    private void centerImage(Bitmap bitmap) {
        float viewW = imageView.getWidth();
        float viewH = imageView.getHeight();
        float bmpW = bitmap.getWidth();
        float bmpH = bitmap.getHeight();

        // Scale to fit screen while maintaining aspect ratio
        float scale = Math.min(viewW / bmpW, viewH / bmpH);
        scaleFactor = scale;
        minScale = scale;

        // Center offset
        translateX = (viewW - bmpW * scale) / 2f;
        translateY = (viewH - bmpH * scale) / 2f;

        applyMatrix();
    }

    private void applyMatrix() {
        matrix.reset();
        matrix.postScale(scaleFactor, scaleFactor);
        matrix.postTranslate(translateX, translateY);
        imageView.setImageMatrix(matrix);
    }

    private void clampTranslation() {
        if (imageView.getDrawable() == null) return;

        float viewW = imageView.getWidth();
        float viewH = imageView.getHeight();
        float bmpW = imageView.getDrawable().getIntrinsicWidth() * scaleFactor;
        float bmpH = imageView.getDrawable().getIntrinsicHeight() * scaleFactor;

        // Horizontal clamp
        if (bmpW <= viewW) {
            // Image narrower than screen: keep centered
            translateX = (viewW - bmpW) / 2f;
        } else {
            translateX = Math.min(0, Math.max(translateX, viewW - bmpW));
        }

        // Vertical clamp
        if (bmpH <= viewH) {
            // Image shorter than screen: keep centered
            translateY = (viewH - bmpH) / 2f;
        } else {
            translateY = Math.min(0, Math.max(translateY, viewH - bmpH));
        }
    }

    private void resetToCenter() {
        if (imageView.getDrawable() == null) return;
        scaleFactor = minScale;
        float viewW = imageView.getWidth();
        float viewH = imageView.getHeight();
        float bmpW = imageView.getDrawable().getIntrinsicWidth();
        float bmpH = imageView.getDrawable().getIntrinsicHeight();
        translateX = (viewW - bmpW * scaleFactor) / 2f;
        translateY = (viewH - bmpH * scaleFactor) / 2f;
        applyMatrix();
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float prevScale = scaleFactor;
            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max(minScale, Math.min(scaleFactor, maxScale));

            // Zoom toward the pinch focal point
            float focusX = detector.getFocusX();
            float focusY = detector.getFocusY();
            float scaleChange = scaleFactor / prevScale;
            translateX = focusX - scaleChange * (focusX - translateX);
            translateY = focusY - scaleChange * (focusY - translateY);

            clampTranslation();
            applyMatrix();
            return true;
        }
    }
}
