package com.apepdcl.gis_survey.RDSS.OLD_CODE.ugc33kv.rdssugc33kv.retrofitservice;






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
public class RetrofitClientInstanceUG33KV {
    private static Retrofit retrofit;

   // private static final String BASEURL="http://59.144.184.106:7766/epdcl_dept_ope_activities/rest/epdcldeptope/";


 //private static final String BASEURL="http://59.144.184.106:6600/epdcl_dept_ope_activities/rest/epdcldeptope/";
 private static final String LIVE_URL="https://33kvrdss.apeasternpower.com/rest/epdcldeptope/";
 private static final String LOCAL_URL="http://10.150.250.24:8080/33kvGis_RDSS/rest/epdcldeptope/";
    public static final String BASEURL_PRE_PRODUCTION = "http://59.144.184.92:5513/gis-other-services/ugc/trench/";


    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder().serializeNulls().create();

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .addNetworkInterceptor(loggingInterceptor) // ✅ Use network interceptor
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(LIVE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }



//    //https://mcccapps.apeasternpower.com
//
//    public static Retrofit getRetrofitInstance(){
//        if(retrofit==null){
//            // int cacheSize = 5 * 1024 * 1024; // 5 MB
//            // Cache cache = new Cache(get.getCacheDir(), cacheSize);
//            Gson gson = new GsonBuilder().serializeNulls().create();
//            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//            OkHttpClient.Builder builder = new OkHttpClient.Builder();
//            builder.addNetworkInterceptor(loggingInterceptor);
//
//            OkHttpClient okHttpClient = new OkHttpClient.Builder()
//
//                    .readTimeout(120, TimeUnit.SECONDS)
//                    .writeTimeout(120, TimeUnit.SECONDS)
//                    .connectTimeout(120, TimeUnit.SECONDS)
//                    .addInterceptor(loggingInterceptor)
//
//                    //.addNetworkInterceptor(networkInterceptor)
//                    //  .cache(cache)
//                    .build();
//            retrofit=new Retrofit.Builder().baseUrl(LOCAL_URL).addConverterFactory(GsonConverterFactory.create(gson)).client(okHttpClient).build();
//        }
//        return  retrofit;
//    }

    /*private static String BASEURL_33KV="https://mcccapps.apeasternpower.com";
    private static Retrofit retrofit;
    public static Retrofit getRetrofitInstance() {

        if (retrofit==null){
            Gson gson = new GsonBuilder().serializeNulls().create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASEURL_33KV)
                    .client(getUnsafeOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }
                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[0];
                }
            } };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts,
                    new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext
                    .getSocketFactory();


            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);


            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient = okHttpClient.newBuilder()
                    .sslSocketFactory(sslSocketFactory)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
                    .hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER).build();

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }*/
}
