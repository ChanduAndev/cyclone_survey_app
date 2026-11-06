package com.apepdcl.gis_survey.RDSS;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by SOUBHAGYA SWAIN on 19-05-2025.
 */
public class RetrofitClientInstanceRDSS {

    private static Retrofit retrofit;


    public static final String BASEURL_LOCAL = "http://10.150.250.23:5555/ugcable-assets/rest/txservice/ugcabletrench/";
    public static final String BASEURL_LOCAL_1 = "http://10.150.250.23:5551/ugc/trench/";

    public static final String BASEURL_PRE_PRODUCTION = "http://59.144.184.92:5513/gis-other-services/ugc/trench/";
    public static final String BASEURL_PRODUCTION = "https://prodgis.apeasternpower.com/ugc/trench/";


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
            retrofit = new Retrofit.Builder().baseUrl(BASEURL_PRODUCTION).addConverterFactory(GsonConverterFactory.create(gson)).client(okHttpClient).build();
        }
        return retrofit;
    }
}
