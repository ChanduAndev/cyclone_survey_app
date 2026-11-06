package com.apepdcl.gis_survey.RDSS.OLD_CODE_OH.retrofitservice;

//import com.apepdcl.gis11kv.BuildConfig;


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
    public static final String web_url_help = "http://59.144.184.106:6611/11KvGis/";
    /*Test server*/
    //  public static final String BASEURL="http://59.144.184.77:8085/11KvGis/rest/epdcldeptope/";
    //public static final String BASEURL1="http://10.150.250.166:8077/epdcl_dept_ope_activities/rest/epdcldeptope/";


    //MyServer
    // public static final String BASEURL="http://10.150.250.182:8080/11kvsurvey/rest/epdcldeptope/";

    //Naidu
    //  public static final String BASEURL="http://10.150.250.92:8080/11KvGis/rest/epdcldeptope/";
    //Mouli
    //public static final String BASEURL="http://10.150.250.17:8081/11KvGis/rest/epdcldeptope/";


    //Live
    private static final String BASEURL_PRE_PRODUCTION = "http://59.144.184.106:8899/11KvGis/rest/epdcldeptope/";

    // private static final String BASEURL = "http://10.150.250.166:8080/11KvGis/rest/epdcldeptope/";

    private static final String LIVE_URL = "https://11kvrdss.apeasternpower.com/rest/epdcldeptope/";
    private static final String LOCAL_URL = "http://10.150.250.24:8090/11KvGisRDSS/rest/epdcldeptope/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            // int cacheSize = 5 * 1024 * 1024; // 5 MB
            // Cache cache = new Cache(get.getCacheDir(), cacheSize);
            Gson gson = new GsonBuilder().serializeNulls().create();
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // loggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
                    //.addNetworkInterceptor(networkInterceptor)
                    //  .cache(cache)
                    .build();
            retrofit = new Retrofit.Builder().baseUrl(LIVE_URL).addConverterFactory(GsonConverterFactory.create(gson)).client(okHttpClient).build();
            // retrofit = new Retrofit.Builder().baseUrl(BuildConfig.API_URL).addConverterFactory(GsonConverterFactory.create(gson)).client(okHttpClient).build();
            //retrofit = new Retrofit.Builder().baseUrl(BASEURL).addConverterFactory(GsonConverterFactory.create(gson)).client(okHttpClient).build();
        }
        return retrofit;
    }
}
