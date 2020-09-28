package tikdownloader.tiktokvideodownloader.tiktokvideonowatermark;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;


import com.facebook.ads.AudienceNetworkAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;

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
    }

    public void trackFireBaseEvent(String eventName, String action, String value) {
        Bundle bundle = new Bundle();
        bundle.putString(action, value);
        mFirebaseAnalytics.logEvent(eventName, bundle);
    }

    public static Context getContext() {
        return appContext;
    }
}
