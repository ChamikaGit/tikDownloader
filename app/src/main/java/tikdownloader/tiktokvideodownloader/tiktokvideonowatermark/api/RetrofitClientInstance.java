package tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.api;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.MyApplication;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util.Settings;

/**
 * Created by Kasun on 6/23/2019.
 */

//public class RetrofitClientInstance {
//    private static Retrofit retrofit;
//    private static String BASE_URL = "";
//    private static Settings settings;
//
//    public static Retrofit getRetrofitInstance() {
//
//        try {
//            settings = new Settings(MyApplication.getContext());
//            BASE_URL = settings.getBaseUrl();
//            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//            interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
//            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//            httpClient.connectTimeout(100, TimeUnit.SECONDS);
//            httpClient.readTimeout(100, TimeUnit.SECONDS).build();
//            httpClient.writeTimeout(100, TimeUnit.SECONDS).build();
//            httpClient.addInterceptor(interceptor);
//            httpClient.addInterceptor(new BasicAuthInterceptor());
//
//
////        httpClient.addInterceptor(new Interceptor() {
////            @Override
////            public Response intercept(Chain chain) throws IOException {
////                Request original = chain.request();
////                Response response = chain.proceed(original);
////
////                if (response.code() == 401){
////
////
////                }
////
////                // Request customization: add request headers
////                Request.Builder requestBuilder = original.newBuilder()
////                        .header("Content-Type", "application/json")
////                        .header("app-token", "DX9343ZXS9JPK5c8ws5ct9G4u3720jTQ5lHwbGJH777GflSQX1VMDFELKS");
//////                        .header("session-token",settings.getAccessToken()); // <-- this is the important line
////
////                Request request = requestBuilder.build();
////                return chain.proceed(request);
////            }
////        });
//
//
//            OkHttpClient client = httpClient.build();
//
//            Gson gson = new GsonBuilder()
//                    .setLenient()
//                    .create();
//
//            if (retrofit == null) {
//                retrofit = new Retrofit.Builder()
//                        .baseUrl(BASE_URL)
//                        .client(client)
//                        .addConverterFactory(ScalarsConverterFactory.create())
//                        .addConverterFactory(GsonConverterFactory.create(gson))
//                        .build();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e("Error", e.getMessage());
//        }
//        return retrofit;
//    }
//
//
//    public static class BasicAuthInterceptor implements Interceptor {
//
//        @Override
//        public Response intercept(Chain chain) throws IOException {
//            Request request = chain.request();
//            Request.Builder requestBuilder = request.newBuilder()
//                    .header("token", settings.getAccessToken())
////                    .header("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10.16; rv:84.0) Gecko/20100101 Firefox/84.0")
//                    .header("Accept","application/json, text/plain, */*");
////                    .header("Accept-Language","en-US,en;q=0.5")
////                    .header("Content-Type","multipart/form-data; boundary=---------------------------78106230932511381533150274355")
////                    .header("Origin","https://tiktok.codespikex.com")
////                    .header("Connection","keep-alive")
////                    .header("Referer","https://tiktok.codespikex.com/")
////                    .header("Cookie","_ga_LGGKNVMLCT=GS1.1.1609308870.2.0.1609308870.0; _ga=GA1.1.1753111199.1609305519; __gads=ID=220eb91e9f84b274-22a1693061c50052:T=1609305521:RT=1609305521:S=ALNI_MbZoi1m0M5SkYjnBeJh7VUj9AltLw")
////                    .header("TE","Trailers");
////                        .header("session-token",settings.getAccessToken()); // <-- this is the important line
//            Request request1 = requestBuilder.build();
//            Response response = chain.proceed(request1);
//
//            if (response.code() == 401) {
//
//            }
//            return response;
//        }
//    }
//}


public class RetrofitClientInstance {
    private static Retrofit retrofit;
    //    private static final String BASE_URL = BuildConfig.API_HOST;
    private static Settings settings;
    private static Context context;
    private static Context activityContext;
    private static String BASE_URL = "";


    public static Retrofit getRetrofitInstance(Context mContext) {
        activityContext = mContext;
        settings = new Settings(MyApplication.getContext());
        BASE_URL = settings.getBaseUrl();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(100, TimeUnit.SECONDS);
        httpClient.readTimeout(100, TimeUnit.SECONDS).build();
        httpClient.writeTimeout(100, TimeUnit.SECONDS).build();
        httpClient.addInterceptor(interceptor);
        httpClient.addInterceptor(new BasicAuthInterceptor());
        Log.e("erorrrrrrrr:", "getAccessToken :getAccessToken "+settings.getAccessToken());
//        CertificatePinner certificatePinner = new CertificatePinner.Builder()
//                .add(BuildConfig.cert_domain,BuildConfig.cert_key).build();
//        httpClient.certificatePinner(certificatePinner);

//        httpClient.addInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request original = chain.request();
//                Response response = chain.proceed(original);
//
//                if (response.code() == 401){
//
//
//                }
//
//                // Request customization: add request headers
//                Request.Builder requestBuilder = original.newBuilder()
//                        .header("Content-Type", "application/json")
//                        .header("app-token", "DX9343ZXS9JPK5c8ws5ct9G4u3720jTQ5lHwbGJH777GflSQX1VMDFELKS");
////                        .header("session-token",settings.getAccessToken()); // <-- this is the important line
//
//                Request request = requestBuilder.build();
//                return chain.proceed(request);
//            }
//        });


        OkHttpClient client = httpClient.build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }


    public static class BasicAuthInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request.Builder requestBuilder = request.newBuilder()
//                    .header("Content-Type", "application/json")
                    .header("Host", "tiktok.codespikex.com")
                    .header("token", settings.getAccessToken())
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36")
                    .header("Postman-Token", "8e140168-25f6-4ebc-b894-8ee619689197");
//                    .header("Accept-Language","en-US,en;q=0.5")
//                    .header("Content-Type","multipart/form-data; boundary=---------------------------78106230932511381533150274355")
//                    .header("Origin","https://tiktok.codespikex.com")
//                    .header("Connection","keep-alive")
//                    .header("Referer","https://tiktok.codespikex.com/")
//                    .header("Cookie","_ga_LGGKNVMLCT=GS1.1.1609308870.2.0.1609308870.0; _ga=GA1.1.1753111199.1609305519; __gads=ID=220eb91e9f84b274-22a1693061c50052:T=1609305521:RT=1609305521:S=ALNI_MbZoi1m0M5SkYjnBeJh7VUj9AltLw")
//                    .header("TE","Trailers");
//                        .header("session-token",settings.getAccessToken()); // <-- this is the important line
            Request request1 = requestBuilder.build();
            Response response = chain.proceed(request1);

            if (response.code() == 401) {

            }
            return response;
        }
    }

//    public static void showDialog(Context activity) {
//        ((Activity) activity).runOnUiThread(new Runnable() {
//            public void run() {
//                final Dialog dialog = new Dialog(activity);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setCancelable(false);
//                dialog.setContentView(R.layout.dialog_message);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
//                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//
//                TextView textViewMessage = dialog.findViewById(R.id.textViewMessage);
//                textViewMessage.setText(activityContext.getString(R.string.account_suspended));
//                TextView btnOk = dialog.findViewById(R.id.tvOK);
//
//                btnOk.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        settings.setAccessToken("");
//                        settings.setReceiveReward("");
//                        settings.setLocationState(false);
//                        Intent intent = new Intent(activityContext, LoginActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        activityContext.startActivity(intent);
//                    }
//                });
//                Runnable runnable = new Runnable() {
//                    @Override
//                    public void run() {
//                        dialog.show();
//                    }
//                };
//
//                new Handler(Looper.getMainLooper()).post(runnable);
//
//            }
//        });
//
//    }

}
