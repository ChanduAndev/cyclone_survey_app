package com.apepdcl.gis_survey.METER_SURVEY;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by SOUBHAGYA SWAIN on 12-06-2026.
 */
public class ApiClient {

    private static Retrofit retrofit;

    public static Retrofit getClient() {

        if (retrofit == null) {
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
//            retrofit = new Retrofit.Builder()
//                    .baseUrl("https://ggdmsdc.apeasternpower.com/api/v2/file/")
//
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl("https://ggdmsdc.apeasternpower.com/api/v2/file/")
                    .addConverterFactory(GsonConverterFactory.create(gson)).client(okHttpClient).build();
        }

        return retrofit;
    }
}
