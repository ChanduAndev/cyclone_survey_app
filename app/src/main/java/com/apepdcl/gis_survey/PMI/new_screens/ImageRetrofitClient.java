package com.apepdcl.gis_survey.PMI.new_screens;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by SOUBHAGYA SWAIN on 03-09-2025.
 */
public class ImageRetrofitClient {
 private static Retrofit retrofit;


 public static final String BASEURL_LOCAL = "http://10.150.2.189:47194/";
 public static final String BASEURL_PRE_PRODUCTION = "http://59.144.184.92:5513/gis-other-services/pmi/";
 public static final String BASEURL_PRODUCTION = "https://lmsurvey.apepdcl.org/";
 //public static final String BASEURL_PRODUCTION_NEW = "http://10.150.2.140:47194/";
 public static final String BASEURL_PRODUCTION_NEW = "http://117.206.84.55:47194/";
 public static final String CLIENT_KEY = "fywHaBHA27LUDCzoYCN4z8AqV0eSZU9wom5pw7sKKGs=";

 public static Retrofit getRetrofitInstance() {
  if (retrofit == null) {
   // int cacheSize = 5 * 1024 * 1024; // 5 MB
   // Cache cache = new Cache(get.getCacheDir(), cacheSize);
   Gson gson = new GsonBuilder().serializeNulls().create();
   HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
   loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

   // Add interceptor for API key
   Interceptor apiKeyInterceptor = chain -> {
    Request original = chain.request();
    Request request = original.newBuilder()
            .header("X-API-Key", CLIENT_KEY)
            .method(original.method(), original.body())
            .build();
    return chain.proceed(request);
   };

   OkHttpClient okHttpClient = new OkHttpClient.Builder()
           .readTimeout(120, TimeUnit.SECONDS)
           .writeTimeout(120, TimeUnit.SECONDS)
           .connectTimeout(120, TimeUnit.SECONDS)
           .addInterceptor(loggingInterceptor)
           .addInterceptor(apiKeyInterceptor)
           //.addNetworkInterceptor(networkInterceptor)
           //  .cache(cache)
           .build();
   retrofit = new Retrofit.Builder().baseUrl(BASEURL_PRODUCTION_NEW).addConverterFactory(GsonConverterFactory.create(gson)).client(okHttpClient).build();
  }
  return retrofit;
 }
}
