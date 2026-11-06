package com.apepdcl.gis_survey.pmi_old.module_11kv.retrofitservice;

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

public class PMIRetrofitClientInstance11KV {
    private static Retrofit retrofit;
    public static final String web_url_help = "http://59.144.184.106:6600/11KvGis/";
    //private static final String BASEURL_LOCAL = "http://10.150.250.186:8080/11KvGis/rest/epdcldeptope/";
    private static final String BASEURL_LOCAL_SURENDER = "http://10.150.250.24:8080/11KvGisPmi/rest/epdcldeptope/";
    private static final String BASEURL_TEST = "http://59.144.184.95:5517/11KvGis/rest/epdcldeptope/";
    private static final String BASEURL_LIVE = "https://pmi11kv.apeasternpower.com/rest/epdcldeptope/";
    private static final String BASEURL_PRE_PRODUCTION = "https://gispmi11kv.apeasternpower.com/rest/epdcldeptope/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            // int cacheSize = 5 * 1024 * 1024; // 5 MB
            // Cache cache = new Cache(get.getCacheDir(), cacheSize);
            Gson gson = new GsonBuilder().serializeNulls().create();
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

    private final TrustManager[] trustAllCerts= new TrustManager[] { new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }

        public void checkClientTrusted(X509Certificate[] chain,
                                       String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain,
                                       String authType) throws CertificateException {
        }
    }
    };
}
