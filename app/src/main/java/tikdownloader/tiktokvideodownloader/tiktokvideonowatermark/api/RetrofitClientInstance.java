package tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.api;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.MyApplication;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util.Settings;

/**
 * Created by Kasun on 6/23/2019.
 */

public class RetrofitClientInstance {
    private static Retrofit retrofit;
    private static String BASE_URL = "";
    private static Settings settings;

    public static Retrofit getRetrofitInstance() {

        try {
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

            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error", e.getMessage());
        }
        return retrofit;
    }


    public static class BasicAuthInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request.Builder requestBuilder = request.newBuilder()
                    .header("TOKEN", settings.getAccessToken());
//                        .header("session-token",settings.getAccessToken()); // <-- this is the important line
            Request request1 = requestBuilder.build();
            Response response = chain.proceed(request1);

            if (response.code() == 401) {

            }
            return response;
        }
    }
}
