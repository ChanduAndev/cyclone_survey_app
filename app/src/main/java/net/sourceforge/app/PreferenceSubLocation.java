package net.sourceforge.app;

import android.os.Bundle;
import android.util.Log;

import com.apepdcl.gis.app.R;

public class PreferenceSubLocation extends PreferenceSubScreen {
    private static final String TAG = "PreferenceSubLocation";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if( MyDebug.LOG )
            Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_sub_location);

        if( MyDebug.LOG )
            Log.d(TAG, "onCreate done");
    }
}
