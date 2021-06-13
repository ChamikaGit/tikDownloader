package tikdownloader.tiktokvideodownloader.tiktokvideonowatermark;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;


import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.SSLContext;

public class MyApplication extends Application {
    private FirebaseAnalytics mFirebaseAnalytics;
    private static MyApplication mainApplication;
    private static Context appContext;

    public static synchronized MyApplication getInstance() {
        return mainApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mainApplication = this;
        appContext = this;
        AudienceNetworkAds.initialize(this);
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        try {
            // Google Play will install latest OpenSSL
            ProviderInstaller.installIfNeeded(appContext);
            SSLContext sslContext;
            sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, null, null);
            sslContext.createSSLEngine();
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException
                | NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }

        //admob initilization
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        if(BuildConfig.DEBUG){
            List<String> testDeviceIds = Arrays.asList("2CBF563B58BA400B60874AFF2690AEED","BC95797D2B25EDCE1862C0646F339445");
            RequestConfiguration configuration = new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
            MobileAds.setRequestConfiguration(configuration);
        }
    }

    public void trackFireBaseEvent(String eventName, String action, String value) {
        Bundle bundle = new Bundle();
        bundle.putString(action, value);
        mFirebaseAnalytics.logEvent(eventName, bundle);
    }

    public static Context getContext() {
        return appContext;
    }

//    EAADjM5zIiSoBAFACsyzOW9EltVua9j9bjGZBnHzkvXTiZCYO6X6zzJoVTf6O5gZBsTxNvltsh1i9JUkxg3CFuUlkOXZB1zZAJYBunKGUorZC6ZATAQPwuvv73zbIhNes2LJ1VEVK4eq5K3PG66WVkfLkWCpIjNr9ldze7HZCIHjUvL4nRyspQWgk
    //241681297401399 property id
    //249810813225258_249825376557135 placement id
    //249810813225258_249825376557135

    //mediation sysytem user
    //EAADjM5zIiSoBAMPKlLRnFqjnLuFR6fsxlhZB0TWsk6IQsVRRqF3NwZCZAJROqiGmuaH62TUHZA3pn8MNE0ZApaPy51inz2K7QFNf7fHAw3dofPFJIKSJeWDiICH44OREbgbn9ImLjozc6qbBA3k695n9MxuqekZCNuhuycHan1zAJzlsV45OG5

}
