package com.apepdcl.gis_survey.PMI.new_screens;

import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONObject;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class DtrPlinthValidator {

    // Class IDs (same as Python)
    public static final int PLINTH_CLASS_ID = 0;
    public static final int POLE_CLASS_ID = 1;

    // Defaults
    public static final float DEFAULT_MIN_HEIGHT_RATIO = 0.006f;
    public static final float DEFAULT_TOP_BOTTOM_MARGIN_RATIO = 0.006f;
    public static final float DEFAULT_CONF_THRESHOLD = 0.5f;

    /** Represent one detection from YOLO */
    public static class Detection {
        public float x1, y1, x2, y2;
        public float conf;
        public int cls;

        public Detection(float x1, float y1, float x2, float y2, float conf, int cls) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.conf = conf;
            this.cls = cls;
        }
    }

    /** Result of visibility check */
    public static class VisibilityResult {
        public String status;
        public boolean valid;
        public boolean detected;
        public boolean topVisible;
        public boolean baseVisible;
        public String reason;
        public Detection validDetection;
    }


    // -------------------------------------------------------------------------
    // ✔ Equivalent of validate_visibility() from Python
    // -------------------------------------------------------------------------

    public static VisibilityResult validateVisibility(
            ArrayList<Detection> detections,
            int imgH,
            int imgW,
            String type,
            float minHeightRatio,
            float marginRatio,
            float confThresh) {

        VisibilityResult result = new VisibilityResult();
        result.detected = detections.size() > 0;

        if (!result.detected) {
            result.status = "INVALID: no " + type + " detected";
            result.reason = "no_" + type + "_detected";
            result.valid = false;
            return result;
        }

        for (Detection det : detections) {
            if (det.conf < confThresh) {
                result.status = "INVALID: low confidence";
                result.reason = "low_confidence";
                continue;
            }

            float height = det.y2 - det.y1;
            float hRatio = height / imgH;

            if (hRatio < minHeightRatio) {
                result.status = "INVALID: " + type + " height too small";
                result.reason = "height_small";
                continue;
            }

            // Top / base visibility
            float topMargin = det.y1 / imgH;
            float bottomMargin = (imgH - det.y2) / imgH;

            boolean missTop = topMargin < marginRatio;
            boolean missBase = bottomMargin < marginRatio;

            result.topVisible = !missTop;
            result.baseVisible = !missBase;

            if (!missTop && !missBase) {
                result.status = "VALID";
                result.valid = true;
                result.validDetection = det;
                return result;
            } else if (missTop && !missBase) {
                result.status = "INVALID: " + type + " top not visible";
                result.reason = "top_missing";
                result.valid = false;
                return result;
            } else if (!missTop && missBase) {
                result.status = "INVALID: " + type + " base not visible";
                result.reason = "base_missing";
                result.valid = false;
                return result;
            } else {
                result.status = "INVALID: " + type + " top and base not visible";
                result.reason = "top_base_missing";
                result.valid = false;
                return result;
            }
        }

        result.status = "INVALID: no valid " + type + " detection";
        result.valid = false;
        return result;
    }


    // -------------------------------------------------------------------------
    // ✔ Equivalent of validate_dtr_plinth_quality() from Python
    // -------------------------------------------------------------------------

    //OLD-----------------------------------------------------------

    public static JSONObject validateImage(
            Bitmap bitmap,
            ArrayList<Detection> detections,
            float confThreshold,
            float minHeightRatio,
            float marginRatio) {

        try {
            int imgW = bitmap.getWidth();
            int imgH = bitmap.getHeight();

            JSONObject result = new JSONObject();
            result.put("status", "unknown");
            result.put("width", imgW);
            result.put("height", imgH);

            // Split by class
            ArrayList<Detection> poles = new ArrayList<>();
            ArrayList<Detection> plinths = new ArrayList<>();

            for (Detection d : detections) {
                if (d.cls == POLE_CLASS_ID) poles.add(d);
                if (d.cls == PLINTH_CLASS_ID) plinths.add(d);
            }

            // POLE VALIDATION
            VisibilityResult poleVal = validateVisibility(
                    poles, imgH, imgW, "pole",
                    minHeightRatio, marginRatio, confThreshold
            );
            result.put("pole_status", poleVal.status);

            // PLINTH VALIDATION
            VisibilityResult plinthVal = validateVisibility(
                    plinths, imgH, imgW, "plinth",
                    minHeightRatio, marginRatio, confThreshold
            );
            result.put("plinth_status", plinthVal.status);

            // FINAL STATUS
            if (poleVal.valid && plinthVal.valid) {
                result.put("status", "passed");
            } else {
                result.put("status", "failed");
            }

            return result;

        } catch (Exception e) {
            Log.e("DtrValidator", "Error", e);
            JSONObject err = new JSONObject();
            try {
                err.put("status", "error");
                err.put("message", e.getMessage());
            } catch (Exception ignore) { }
            return err;
        }
    }




}

