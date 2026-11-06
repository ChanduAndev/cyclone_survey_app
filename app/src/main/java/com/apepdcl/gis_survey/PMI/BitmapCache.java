package com.apepdcl.gis_survey.PMI;


/**
 * Created by SOUBHAGYA SWAIN on 07-04-2026.
 */

import android.graphics.Bitmap;
import java.util.HashMap;
import java.util.Map;

public class BitmapCache {

    private static final Map<String, Bitmap> cache = new HashMap<>();

    public static void put(String key, Bitmap bitmap) {
        cache.put(key, bitmap);
    }

    public static Bitmap get(String key) {
        return cache.get(key);
    }

    public static void remove(String key) {
        cache.remove(key);
    }
}
