package com.apepdcl.gis_survey.PMI;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by SOUBHAGYA SWAIN on 20-03-2025.
 */
public class RetrofitClientInstancePMI {
    private static Retrofit retrofit;

    //NEED TO UPDATE LIVE
    public static final String BASEURL_LOCAL = "http://10.150.250.23:5551/pmi/cyclone/";
    //public static final String BASEURL_PRE_PRODUCTION = "http://59.144.184.92:5513/gis-other-services/pmi/";
    public static final String BASEURL_PRODUCTION = "https://prodgis.apeasternpower.com/pmi/cyclone/";
    public static final String BASEURL_PRE_PRODUCTION = "https://preprodgis.apeasternpower.com/pmi/cyclone/";
    //public static final String BASEURL_PRODUCTION = "https://prodgis.apeasternpower.com/pmi/";


    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            // int cacheSize = 5 * 1024 * 1024; // 5 MB
            // Cache cache = new Cache(get.getCacheDir(), cacheSize);
            Gson gson = new GsonBuilder().serializeNulls().create();
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
                    //.addNetworkInterceptor(networkInterceptor)
                    //  .cache(cache)
                    .build();
            retrofit = new Retrofit.Builder().baseUrl(BASEURL_LOCAL).addConverterFactory(GsonConverterFactory.create(gson)).client(okHttpClient).build();
        }
        return retrofit;
    }
}
