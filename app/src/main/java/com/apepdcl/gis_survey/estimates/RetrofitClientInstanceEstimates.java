package com.apepdcl.gis_survey.estimates;


import com.apepdcl.gis_survey.activitys.LoginActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Soubhagya Swain on 07,June,2024
 */
public class RetrofitClientInstanceEstimates {
    private static Retrofit retrofit;
    public static final String BASEURL_LIVE = "";
    public static final String BASEURL_TEST = "http://10.150.2.191:5513/gis-estimates/rest/txservice/";

    public static final String BASEURL_LOCAL = "http://10.150.250.23:5555/rest/txservice/";
    public static final String BASEURL_LOCAL_SOLAR = "http://10.150.250.23:5555/rest/solar/";
    public static final String PRE_PRODUCTION_SOLAR = "http://59.144.184.92:5513/gis-other-services/rest/solar/";
    public static final String PRODUCTION_SOLAR = "https://prodgis.apeasternpower.com/rest/solar/";
    public static final String BASEURL_LOCAL_ASSETS = "http://10.150.250.23:5555/asset/rest/txservice/";
    public static final String PRE_PRODUCTION_ASSETS = "http://59.144.184.92:5513/gis-estimates/asset/rest/txservice/";
    public static final String PRODUCTION_ASSETS = "https://gisestimates.apeasternpower.com/asset/rest/txservice/";

    public static final String BASEURL_LOCAL_1 = "http://10.150.250.24:5555/rest/txservice/";
    public static final String PRE_PRODUCTION = "http://59.144.184.92:5513/gis-estimates/rest/txservice/";
    public static final String PRODUCTION = "https://gisestimates.apeasternpower.com/rest/txservice/";

    public static final String BASEURL_LOCAL_ASSETS_RDSS = "http://10.150.250.23:5555/asset/rest/txservice/";
    public static final String PRE_PRODUCTION_ASSETS_RDSS = "https://pregisestimates.apeasternpower.com/asset/rest/txservice/";
    public static final String PRODUCTION_ASSETS_RDSS = "https://gisestimates.apeasternpower.com/asset/rest/txservice/";

    public static final String PRE_PRODUCTION_NEW = "https://pregisestimates.apeasternpower.com/rest/txservice/";
    public static final String PRE_PRODUCTION_ASSETS_NEW = "https://pregisestimates.apeasternpower.com/asset/rest/txservice/";
    public static final String PRE_PRODUCTION_ASSETS_RDSS_NEW = "https://pregisestimates.apeasternpower.com/asset/rest/txservice/";

    public static String FINAL_URL = "";

    public static Retrofit getRetrofitInstance(String token) {

        if ("ESTIMATES".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
            FINAL_URL = PRODUCTION;
        } else if ("SOLAR_DPR".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
            FINAL_URL = PRODUCTION_SOLAR;
        } else if ("WORK_ORDER".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
            FINAL_URL = PRODUCTION_ASSETS;
        } else if ("WO_RDSS".equalsIgnoreCase(LoginActivity.SURVEY_FROM)) {
            FINAL_URL = PRODUCTION_ASSETS_RDSS;
        }

        /*if (retrofit == null) {*/
        // int cacheSize = 5 * 1024 * 1024; // 5 MB
        // Cache cache = new Cache(get.getCacheDir(), cacheSize);
        Gson gson = new GsonBuilder().serializeNulls().create();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //.addInterceptor(new AuthInterceptor(token))
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                //.addNetworkInterceptor(networkInterceptor)
                //  .cache(cache)
                .build();
        retrofit = new Retrofit.Builder().baseUrl(FINAL_URL).addConverterFactory(GsonConverterFactory.create(gson)).client(okHttpClient).build();
//        }
        return retrofit;


    }
}
