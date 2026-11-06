package com.apepdcl.gis_survey.sasa;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientSasa {
    private static Retrofit retrofit;
    public static final String BASEURL_LOCAL = "http://10.150.250.23:5556/rest/sasaservice/";
    public static final String BASEURL_PRE_PRODUCTION = "https://presasa.apeasternpower.com/rest/sasaservice/";
    public static final String BASEURL_LIVE = "https://sasa.apeasternpower.com/rest/sasaservice/";


    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            // int cacheSize = 5 * 1024 * 1024; // 5 MB
            // Cache cache = new Cache(get.getCacheDir(), cacheSize);
            Gson gson = new GsonBuilder().serializeNulls().create();
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
                    //.addNetworkInterceptor(networkInterceptor)
                    //  .cache(cache)
                    .build();
            retrofit = new Retrofit.Builder().baseUrl(BASEURL_PRE_PRODUCTION).addConverterFactory(GsonConverterFactory.create(gson)).client(okHttpClient).build();
        }
        return retrofit;
    }
}
