package com.apepdcl.gis_survey.module_11KV.api_calls;

//import com.apepdcl.gis11kv.BuildConfig;

import com.apepdcl.gis_survey.DashBoard;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by srikanth.t on 27-03-2019.
 */
public class RetrofitClientInstance {
    private static Retrofit retrofit;
    public static final String web_url_help = "http://59.144.184.106:6600/11KvGis/";


    //private static final String BASEURL_TEST = "http://59.144.184.95:5517/11KvGis/rest/epdcldeptope/";
    private static final String BASEURL_LIVE = "https://11kvgis.apeasternpower.com/rest/epdcldeptope/"; ///ORIGINAL
    private static final String BASEURL_LOCAL_CLONE = "http://10.150.250.23:8080/11kvGisAssetRdss/rest/epdcldeptope/";  ///CLONE
    private static final String BASEURL_LIVE_CLONE = "https://rdssasset11kv.apeasternpower.com/rest/epdcldeptope/";  ///CLONE

    private static final String BASEURL_PRE_PRODUCTION = "http://59.144.184.92:5513/11KvGisAsset/rest/epdcldeptope/";
    private static final String BASEURL_PRE_PRODUCTION_NEW = "https://preprodgis.apeasternpower.com/asset/rest/elevenkv/";

    public static final String BASEURL_LOCAL = "http://10.150.250.23:8080/11KvGisAsset/rest/epdcldeptope/";
    public static final String BASEURL_LOCAL_NEW = "http://10.150.250.23:5551/asset/rest/elevenkv/";

    public static String FINAL_URL = "";
    public static Retrofit getRetrofitInstance() {

        if (DashBoard.IS_RDSS_USER){
            FINAL_URL = BASEURL_LIVE_CLONE;
        }else {
            FINAL_URL = BASEURL_LIVE;
        }
        //if (retrofit == null) {
        if (true) {
            // int cacheSize = 5 * 1024 * 1024; // 5 MB
            // Cache cache = new Cache(get.getCacheDir(), cacheSize);
           // Gson gson = new GsonBuilder().serializeNulls().create();
            Gson gson = new GsonBuilder().create();
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
                    //.addNetworkInterceptor(networkInterceptor)
                    //  .cache(cache)
                    .build();
            retrofit = new Retrofit.Builder().baseUrl(BASEURL_LIVE).addConverterFactory(GsonConverterFactory.create(gson)).client(okHttpClient).build();
            // retrofit = new Retrofit.Builder().baseUrl("http://59.144.184.106:6600/11KvGis/rest/epdcldeptope/").addConverterFactory(GsonConverterFactory.create(gson)).client(okHttpClient).build();
            //retrofit = new Retrofit.Builder().baseUrl(BASEURL).addConverterFactory(GsonConverterFactory.create(gson)).client(okHttpClient).build();
        }
        return retrofit;
    }

    private final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }

        public void checkClientTrusted(X509Certificate[] chain,
                                       String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain,
                                       String authType) throws CertificateException {
        }
    }};
}
