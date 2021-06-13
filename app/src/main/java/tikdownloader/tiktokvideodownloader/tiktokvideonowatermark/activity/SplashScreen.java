package tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.BuildConfig;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.R;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.dialogFragment.NoInternetDialogFragment;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util.Security;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util.Settings;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util.Utils;

import static com.android.billingclient.api.BillingClient.SkuType.SUBS;
import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;
import static tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.activity.MainActivity.ITEM_SKU_SUBSCRIBE_MONTHLY;
import static tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.activity.MainActivity.ITEM_SKU_SUBSCRIBE_WEEKLY;

public class SplashScreen extends AppCompatActivity implements NoInternetDialogFragment.OnItemClickListener, PurchasesUpdatedListener {
    SplashScreen activity;
    Context context;
    AppUpdateManager appUpdateManager;
    TextView tvVersionNO;
    private static final String TOKEN = "token_of_the_app";
    private static final String BASE_URL = "base_url_of_app";
    private Settings settings;
    FirebaseRemoteConfig mFirebaseRemoteConfig;

    //in-app purchase
    private BillingClient billingClient;

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
        Utils utils = new Utils(activity);
        if (utils.isNetworkAvailable()) {
            checkInAppSubscription();
        } else {
            NoInternetDialogFragment noInternetDialogFragment = new NoInternetDialogFragment(this);
            noInternetDialogFragment.show(getSupportFragmentManager(), "NoInternetDialogFragment");
        }

        tvVersionNO.setText("v" + BuildConfig.VERSION_NAME);
    }

    private void checkInAppSubscription() {

        try {
            billingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener(this).build();
            billingClient.startConnection(new BillingClientStateListener() {
                @Override
                public void onBillingSetupFinished(BillingResult billingResult) {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        Purchase.PurchasesResult queryPurchase = billingClient.queryPurchases(SUBS);
                        List<Purchase> queryPurchases = queryPurchase.getPurchasesList();
                        if (queryPurchases != null && queryPurchases.size() > 0) {
                            handlePurchases(queryPurchases);
                        }
                        //if no item in purchase list means subscription is not subscribed
                        //Or subscription is cancelled and not renewed for next month
                        // so update pref in both cases
                        // so next time on app launch our premium content will be locked again
                        else {
                            settings.setSubscriptionState(false);
                            initiatePurchase();
                        }
                    }
                }

                @Override
                public void onBillingServiceDisconnected() {
                    Toast.makeText(getApplicationContext(), "Service Disconnected", Toast.LENGTH_SHORT).show();
                }
            });

        }catch (Exception e){
            e.printStackTrace();
            Log.e("error","error :"+e.getMessage().toString());
        }


    }

    private void handlePurchases(List<Purchase> queryPurchases) {
        for (Purchase purchase : queryPurchases) {
            //if item is purchased
            if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                if (!verifyValidSignature(purchase.getOriginalJson(), purchase.getSignature())) {
                    // Invalid purchase
                    // show error to user
                    Toast.makeText(getApplicationContext(), "Error : invalid Purchase", Toast.LENGTH_SHORT).show();
                    return;
                }
                // else purchase is valid
                //if item is purchased and not acknowledged
                if (!purchase.isAcknowledged()) {
                    AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.getPurchaseToken()).build();
                    billingClient.acknowledgePurchase(acknowledgePurchaseParams, ackPurchase);
                }
                //else item is purchased and also acknowledged
                else {
                    // Grant entitlement to the user on item purchase
                    if (!settings.getSubscriptionState()) {
                        settings.setSubscriptionState(true);
                    }
                }
            }
            //if purchase is pending
            else if (purchase.getPurchaseState() == Purchase.PurchaseState.PENDING) {
                Toast.makeText(getApplicationContext(), "Purchase is Pending. Please complete Transaction", Toast.LENGTH_SHORT).show();
                settings.setSubscriptionState(false);
            }
            //if purchase is unknown mark false
            else if (purchase.getPurchaseState() == Purchase.PurchaseState.UNSPECIFIED_STATE) {
                settings.setSubscriptionState(false);
                Toast.makeText(getApplicationContext(), "Purchase Status Unknown", Toast.LENGTH_SHORT).show();
            }
        }
        initiatePurchase();
    }

    AcknowledgePurchaseResponseListener ackPurchase = new AcknowledgePurchaseResponseListener() {
        @Override
        public void onAcknowledgePurchaseResponse(BillingResult billingResult) {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                //if purchase is acknowledged
                // Grant entitlement to the user. and restart activity
                settings.setSubscriptionState(true);
            }
        }
    };

    /**
     * Verifies that the purchase was signed correctly for this developer's public key.
     * <p>Note: It's strongly recommended to perform such check on your backend since hackers can
     * replace this method with "constant true" if they decompile/rebuild your app.
     * </p>
     */
    private boolean verifyValidSignature(String signedData, String signature) {
        try {
            // To get key go to Developer Console > Select your app > Development Tools > Services & APIs.
            String base64Key = BuildConfig.LicencensKey;
            return Security.verifyPurchase(base64Key, signedData, signature);
        } catch (IOException e) {
            return false;
        }
    }

    private void initiatePurchase() {
        List<String> skuList = new ArrayList<>();
        skuList.add(ITEM_SKU_SUBSCRIBE_WEEKLY);
        skuList.add(ITEM_SKU_SUBSCRIBE_MONTHLY);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(SUBS);
        BillingResult billingResult = billingClient.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS);
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
            billingClient.querySkuDetailsAsync(params.build(),
                    new SkuDetailsResponseListener() {
                        @Override
                        public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                                if (skuDetailsList != null && skuDetailsList.size() > 0) {
                                    settings.setSubscribedItemMonthlyPrice(skuDetailsList.get(0).getOriginalPrice() + " ");
                                    settings.setSubscribedItemWeeklyPrice(skuDetailsList.get(1).getOriginalPrice() + " ");
                                    UpdateApp();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), " Error " + billingResult.getDebugMessage(), Toast.LENGTH_SHORT).show();
                                UpdateApp();
                            }
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Sorry Subscription not Supported. Please Update Play Store", Toast.LENGTH_SHORT).show();
            UpdateApp();
        }
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
        new Settings(this).setAccessToken(mFirebaseRemoteConfig.getString(TOKEN));
        new Settings(this).setBaseUrl(mFirebaseRemoteConfig.getString(BASE_URL));
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
                            finish();
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

    @Override
    public void onYesClick(Dialog dialog) {
        try {
            Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNoClick(Dialog dialog) {
        finish();
    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (billingClient != null) {
            billingClient.endConnection();
        }
    }
}
