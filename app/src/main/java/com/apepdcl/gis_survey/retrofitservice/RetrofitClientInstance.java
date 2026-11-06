package com.apepdcl.gis_survey.retrofitservice;


import com.apepdcl.gis_survey.DashBoard;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by srikanth.t on 27-03-2019.
 */
public class RetrofitClientInstance {
    private static Retrofit retrofit;
    private static Retrofit localRetrofit;

    //Pdf url
    public static final String web_url_help = "http://59.144.184.106:8282/LtGis/";

    public static final String BASEURL_LIVE = "https://ltgis.apeasternpower.com/rest/ltsurvey/"; ///ORIGINAL
    public static final String BASEURL_CLONE = "http://10.150.250.23:8080/LtGisAssetRdss/rest/ltsurvey/"; /// CLONE
    public static final String BASEURL_CLONE_LIVE = "https://rdssassetlt.apeasternpower.com/rest/ltsurvey/"; /// CLONE
    public static final String BASEURL_LOCAL = "http://10.150.250.23:8080/LtGisAsset/rest/ltsurvey/";



    public static final String PRE_PRODUCTION_NEW = "http://59.144.184.92:5513/LtGisAsset/rest/ltsurvey/";
   // public static final String PRE_PRODUCTION_NEW = "http://59.144.184.92:5513/gis-other-services/asset/rest/lt/";
    public static  String FINAL_URL ="";


    public static Retrofit getRetrofitInstance() {
        if (DashBoard.IS_RDSS_USER){
            FINAL_URL = BASEURL_CLONE_LIVE;
        }else {
            FINAL_URL = BASEURL_LIVE;
        }
        if (true) {
            // int cacheSize = 5 * 1024 * 1024; // 5 MB
            // Cache cache = new Cache(get.getCacheDir(), cacheSize);
           // Gson gson = new GsonBuilder().serializeNulls().create();
            Gson gson = new GsonBuilder().create();
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
            retrofit = new Retrofit.Builder().baseUrl(BASEURL_LIVE).addConverterFactory(GsonConverterFactory.create(gson)).client(okHttpClient).build();
        }
        return retrofit;
    }

    /*public static Retrofit getLocalRetrofitInstance() {
        if (localRetrofit == null) {
            // int cacheSize = 5 * 1024 * 1024; // 5 MB
            // Cache cache = new Cache(get.getCacheDir(), cacheSize);
            Gson gson = new GsonBuilder().serializeNulls().create();
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .connectTimeout(120, TimeUnit.SECONDS)=-[
                    .addInterceptor(loggingInterceptor)
                    //.addNetworkInterceptor(networkInterceptor)
                    //  .cache(cache)
                    .build();
            localRetrofit = new Retrofit.Builder().baseUrl(BASEURL_TEST).addConverterFactory(GsonConverterFactory.create(gson)).client(okHttpClient).build();
        }
        return localRetrofit;
    }*/
}
