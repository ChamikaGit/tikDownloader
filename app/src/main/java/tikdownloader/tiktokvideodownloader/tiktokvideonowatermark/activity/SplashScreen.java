package tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.BuildConfig;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.R;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util.Settings;

import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;

public class SplashScreen extends AppCompatActivity {
    SplashScreen activity;
    Context context;
    AppUpdateManager appUpdateManager;
    TextView tvVersionNO;
    private static final String TOKEN = "token_of_the_app";
    private static final String BASE_URL = "base_url_of_app";
    private Settings settings;
    FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        settings = new Settings(this);
        tvVersionNO = findViewById(R.id.tvVersionNO);
        context = activity = this;
        appUpdateManager = AppUpdateManagerFactory.create(context);
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings.Builder configBuilder = new FirebaseRemoteConfigSettings.Builder();
        if (BuildConfig.DEBUG) {
            long cacheInterval = 0;
            configBuilder.setMinimumFetchIntervalInSeconds(cacheInterval);
        }
        // finally build config settings and sets to Remote Config
        mFirebaseRemoteConfig.setConfigSettingsAsync(configBuilder.build());
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
        UpdateApp();
        tvVersionNO.setText("v" + BuildConfig.VERSION_NAME);
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;

        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo, IMMEDIATE, activity, 101);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void HomeScreen() {

        Log.e("firebaseRemoteConfig", "firebaseRemoteConfig :" + mFirebaseRemoteConfig.getString(TOKEN));
        Log.e("firebaseRemoteConfig", "firebaseRemoteConfig :" + mFirebaseRemoteConfig.getString(BASE_URL));
        settings.setAccessToken(mFirebaseRemoteConfig.getString(TOKEN));
        settings.setBaseUrl(mFirebaseRemoteConfig.getString(BASE_URL));
        mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Boolean> task) {
                if (task.isSuccessful()) {
                    boolean updated = task.getResult();
                    Log.d("firebaseRemoteConfig", "Config params updated: " + updated);
//                    Toast.makeText(SplashScreen.this, "Fetch and activate succeeded",
//                            Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(SplashScreen.this, MainActivity.class);
                            startActivity(i);
                        }
                    }, 2000);

                } else {
                    Toast.makeText(SplashScreen.this, "Fetch failed",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void UpdateApp() {
        try {
            Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
            appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo, IMMEDIATE, activity, 101);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                } else {
                    HomeScreen();
                }
            }).addOnFailureListener(e -> {
                e.printStackTrace();
                HomeScreen();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode != RESULT_OK) {
                HomeScreen();
            } else {
                HomeScreen();
            }
        }
    }

}
