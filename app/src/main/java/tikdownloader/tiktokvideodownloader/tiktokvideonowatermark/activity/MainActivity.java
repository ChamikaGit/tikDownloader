package tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

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
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.ads.mediation.facebook.FacebookAdapter;
import com.google.ads.mediation.facebook.FacebookExtras;
import com.google.android.ads.mediationtestsuite.MediationTestSuite;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnFailureListener;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;

import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.BuildConfig;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.MyApplication;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.R;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.databinding.ActivityMainBinding;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.dialogFragment.ExitDialogFragment;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.dialogFragment.HowToUseDialogFragment;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.dialogFragment.SubscriptionDialogFragment;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util.AdsUtils;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util.ClipboardListener;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util.Security;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util.Settings;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static androidx.browser.customtabs.CustomTabsIntent.COLOR_SCHEME_SYSTEM;
import static com.android.billingclient.api.BillingClient.SkuType.SUBS;
import static tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util.Utils.createFileFolder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ExitDialogFragment.OnItemClickListener, SubscriptionDialogFragment.OnItemClickListener, PurchasesUpdatedListener {
    private MyApplication myApplication;
    MainActivity activity;
    ActivityMainBinding binding;
    boolean doubleBackToExitPressedOnce = false;
    private ClipboardManager clipBoard;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    String CopyKey = "";
    String CopyValue = "";
    private ExitDialogFragment exitDialogFragment;
    private Settings settings;
    private int reviewCount = 0;
    private AppUpdateManager appUpdateManager;
    private int RequestUpdate = 1;
    private ReviewInfo reviewInfo;
    private ReviewManager manager;
    private final int REQUEST_CODE_TIKTOK = 52;
    private NativeAd unifiedNativeAdObj;
    private NativeAd nativeAdObjMainScreen;
    private View shine;
    private AdLoader adLoaderMainScreen;
    private ShimmerFrameLayout shimmerFrameLayout;
    private FrameLayout nativeContainer;


    //in-app-subscription
    public static final String ITEM_SKU_SUBSCRIBE_WEEKLY = "ads_free_weekly";
    public static final String ITEM_SKU_SUBSCRIBE_MONTHLY = "ads_free_monthly";
    public static String ITEM_SKU_SUBSCRIBE = "";
    private BillingClient billingClient;
    private String subscriptionBundleState = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        myApplication = (MyApplication) getApplication();
        activity = this;
        initViews();
        checkUpdate();
        settings = new Settings(this);
//        if (!settings.getSubscriptionState()) {
//            //check if user enable the in-app subscribed
//            AdsUtils.showGoogleBannerAd(MainActivity.this, binding.adView);
//        }
        //MediationTestSuite.launch(MainActivity.this);
        reviewCount = settings.getReviewCount();
        reviewCount++;
        settings.setReviewCount(reviewCount);
        if (settings.getReviewCount() > 2) {
            intiReview();
        }
//        openSubscriptionDialog();
        // Establish connection to billing client
        //check subscription status from google play store cache
        //to check if item is already Subscribed or subscription is not renewed and cancelled
        billingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener(this).build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    Purchase.PurchasesResult queryPurchase = billingClient.queryPurchases(SUBS);
                    List<Purchase> queryPurchases = queryPurchase.getPurchasesList();
                    if (queryPurchases != null && queryPurchases.size() > 0) {
                        handlePurchases(queryPurchases);
                    } else {
                        settings.setSubscriptionState(false);
                    }
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                Toast.makeText(getApplicationContext(), "Service Disconnected", Toast.LENGTH_SHORT).show();
            }
        });


        if (settings.getSubscriptionState()) {
            binding.tvPro.setText("PRO");
            binding.tvPro.setTextColor(getResources().getColor(R.color.pink));
        } else {
            binding.tvPro.setText("BE A PRO");
            int currentCount = settings.getSubscriptionPopUpCount();
            settings.setSubscriptionPopUpCount(currentCount + 1);
            if (settings.getSubscriptionPopUpCount() % 5 == 0) {
                openSubscriptionDialog();
            }
        }
        //item subscribed
//        if (getSubscribeValueFromPref()) {
//            subscribe.setVisibility(View.GONE);
//            premiumContent.setVisibility(View.VISIBLE);
//            subscriptionStatus.setText("Subscription Status : Subscribed");
//        }
//        //item not subscribed
//        else {
//            premiumContent.setVisibility(View.GONE);
//            subscribe.setVisibility(View.VISIBLE);
//            subscriptionStatus.setText("Subscription Status : Not Subscribed");
//        }
    }


//
//    private SharedPreferences getPreferenceObject() {
//        return getApplicationContext().getSharedPreferences(PREF_FILE, 0);
//    }
//    private SharedPreferences.Editor getPreferenceEditObject() {
//        SharedPreferences pref = getApplicationContext().getSharedPreferences(PREF_FILE, 0);
//        return pref.edit();
//    }
//    private boolean getSubscribeValueFromPref(){
//        return getPreferenceObject().getBoolean( SUBSCRIBE_KEY,false);
//    }
//    private void saveSubscribeValueToPref(boolean value){
//        getPreferenceEditObject().putBoolean(SUBSCRIBE_KEY,value).commit();
//    }

    //initiate purchase on button click
    public void subscribe(String productId) {
        //check if service is already connected
        if (billingClient.isReady()) {
            initiatePurchase(productId);
        }
        //else reconnect service
        else {
            billingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener(this).build();
            billingClient.startConnection(new BillingClientStateListener() {
                @Override
                public void onBillingSetupFinished(BillingResult billingResult) {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        initiatePurchase(productId);
                    } else {
                        Toast.makeText(getApplicationContext(), "Error " + billingResult.getDebugMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onBillingServiceDisconnected() {
                    Toast.makeText(getApplicationContext(), "Service Disconnected ", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void initiatePurchase(String productId) {
        List<String> skuList = new ArrayList<>();
        skuList.add(productId);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(SUBS);
        BillingResult billingResult = billingClient.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS);
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
            billingClient.querySkuDetailsAsync(params.build(),
                    new SkuDetailsResponseListener() {
                        @Override
                        public void onSkuDetailsResponse(BillingResult billingResult,
                                                         List<SkuDetails> skuDetailsList) {
                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                                if (skuDetailsList != null && skuDetailsList.size() > 0) {
                                    BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                            .setSkuDetails(skuDetailsList.get(0))
                                            .build();
                                    billingClient.launchBillingFlow(MainActivity.this, flowParams);
                                } else {
                                    //try to add subscription item "sub_example" in google play console
                                    Toast.makeText(getApplicationContext(), "Item not Found", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        " Error " + billingResult.getDebugMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry Subscription not Supported. Please Update Play Store", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
        //if item subscribed
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            handlePurchases(purchases);
        }
        //if item already subscribed then check and reflect changes
        else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            Purchase.PurchasesResult queryAlreadyPurchasesResult = billingClient.queryPurchases(SUBS);
            List<Purchase> alreadyPurchases = queryAlreadyPurchasesResult.getPurchasesList();
            if (alreadyPurchases != null) {
                handlePurchases(alreadyPurchases);
            }
        }
        //if Purchase canceled
        else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            Toast.makeText(getApplicationContext(), "Purchase Canceled", Toast.LENGTH_SHORT).show();
        }
        // Handle any other error msgs
        else {
            Toast.makeText(getApplicationContext(), "Error " + billingResult.getDebugMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    void handlePurchases(List<Purchase> purchases) {
        for (Purchase purchase : purchases) {
            //if item is purchased
            if (ITEM_SKU_SUBSCRIBE.equals(purchase.getSku()) && purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                if (!verifyValidSignature(purchase.getOriginalJson(), purchase.getSignature())) {
                    // Invalid purchase
                    // show error to user
                    Toast.makeText(getApplicationContext(), "Error : invalid Purchase", Toast.LENGTH_SHORT).show();
                    return;
                }
                // else purchase is valid
                //if item is purchased and not acknowledged
                if (!purchase.isAcknowledged()) {
                    AcknowledgePurchaseParams acknowledgePurchaseParams =
                            AcknowledgePurchaseParams.newBuilder()
                                    .setPurchaseToken(purchase.getPurchaseToken())
                                    .build();
                    billingClient.acknowledgePurchase(acknowledgePurchaseParams, ackPurchase);
                }
                //else item is purchased and also acknowledged
                else {
                    // Grant entitlement to the user on item purchase
                    // restart activity
                    if (!settings.getSubscriptionState()) {
                        settings.setSubscriptionState(true);
                        Log.e("TAG : Item Purchased", "Item Purchased");
                        Toast.makeText(getApplicationContext(), "Item Purchased", Toast.LENGTH_SHORT).show();
//                        this.recreate();
                        reOpenTheApp();
                    }
                }
            }
            //if purchase is pending
            else if (ITEM_SKU_SUBSCRIBE.equals(purchase.getSku()) && purchase.getPurchaseState() == Purchase.PurchaseState.PENDING) {
                Toast.makeText(getApplicationContext(), "Purchase is Pending. Please complete Transaction", Toast.LENGTH_SHORT).show();
            }
            //if purchase is unknown mark false
            else if (ITEM_SKU_SUBSCRIBE.equals(purchase.getSku()) && purchase.getPurchaseState() == Purchase.PurchaseState.UNSPECIFIED_STATE) {
                settings.setSubscriptionState(false);
                Toast.makeText(getApplicationContext(), "Purchase Status Unknown", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void reOpenTheApp() {
        Intent intent = new Intent(MainActivity.this, SplashScreen.class);
        startActivity(intent);
        finishAffinity();
    }

    AcknowledgePurchaseResponseListener ackPurchase = new AcknowledgePurchaseResponseListener() {
        @Override
        public void onAcknowledgePurchaseResponse(BillingResult billingResult) {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                //if purchase is acknowledged
                // Grant entitlement to the user. and restart activity
//                saveSubscribeValueToPref(true);
                settings.setSubscriptionState(true);
//                MainActivity.this.recreate();
                reOpenTheApp();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (billingClient != null) {
            billingClient.endConnection();
        }
    }

    private void loadNativeAd() {

        Bundle extras = new FacebookExtras().setNativeBanner(true).build();

//        VideoOptions videoOptions = new VideoOptions.Builder()
//                .setStartMuted(false)
//                .build();
//
//        NativeAdOptions adOptions = new NativeAdOptions.Builder()
//                .setVideoOptions(videoOptions)
//                .build();

        AdLoader adLoader = new AdLoader.Builder(MainActivity.this, getString(R.string.admob_native_ad))
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                        if (isDestroyed()) {
                            nativeAd.destroy();
                            Log.d("TAG", "Native Ad Destroyed");
                            return;
                        }
                        if (nativeAd.getMediaContent().hasVideoContent()) {
                            float mediaAspectRatio = nativeAd.getMediaContent().getAspectRatio();
                            float duration = nativeAd.getMediaContent().getDuration();

                            nativeAd.getMediaContent().getVideoController().setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                                @Override
                                public void onVideoStart() {
                                    super.onVideoStart();
                                }

                                @Override
                                public void onVideoPlay() {
                                    super.onVideoPlay();
                                }

                                @Override
                                public void onVideoPause() {
                                    super.onVideoPause();
                                }

                                @Override
                                public void onVideoEnd() {
                                    super.onVideoEnd();
                                }

                                @Override
                                public void onVideoMute(boolean b) {
                                    super.onVideoMute(b);
                                }
                            });
                        }

                        unifiedNativeAdObj = nativeAd;
                    }
                })

                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);

                        new CountDownTimer(10000, 1000) {

                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                                loadNativeAd();
                            }
                        }.start();
                    }
                })
//                .withNativeAdOptions(new NativeAdOptions.Builder().build())
//                .withNativeAdOptions(adOptions)
                .build();

        adLoader.loadAd(new AdRequest.Builder()
                .addNetworkExtrasBundle(FacebookAdapter.class, extras)
                .build());
    }

    private void loadNativeAdMainScreen() {
        adLoaderMainScreen = new AdLoader.Builder(getApplicationContext(), getString(R.string.admob_native_ad_media))
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                        nativeAdObjMainScreen = nativeAd;
                        if (nativeAdObjMainScreen != null) {
                            binding.adView.setVisibility(View.GONE);
                            nativeContainer.setVisibility(View.VISIBLE);
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            NativeAdView adView = (NativeAdView) getLayoutInflater().inflate(R.layout.native_ad_banner_rectangle, null);
                            populateNativeAdView(nativeAdObjMainScreen, adView);
                            nativeContainer.removeAllViews();
                            nativeContainer.addView(adView);
                        } else {
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            if (!settings.getSubscriptionState()) {
                                //check if user enable the in-app subscribed
                                AdsUtils.showGoogleBannerAd(MainActivity.this, binding.adView);
                            }
                        }
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError error) {
                        nativeAdObjMainScreen = null;
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        if (!settings.getSubscriptionState()) {
                            //check if user enable the in-app subscribed
                            AdsUtils.showGoogleBannerAd(MainActivity.this, binding.adView);
                        }
//                        Toast.makeText(SplashScreen.this, "Failed to load native ad: " + error, Toast.LENGTH_SHORT).show();
                    }
                }).build();
        adLoaderMainScreen.loadAd(new AdRequest.Builder().build());
    }

    private void populateNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        // Set the media view. Media content will be automatically populated in the media view once
        // adView.setNativeAd() is called.
        MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
//        adView.setPriceView(adView.findViewById(R.id.ad_price));
//        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
//        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline is guaranteed to be in every NativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

        // These assets aren't guaranteed to be in every NativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.GONE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
//      ((RelativeLayout) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
            ((TextView) adView.findViewById(R.id.tvCallActionText)).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

//        if (nativeAd.getPrice() == null) {
//            adView.getPriceView().setVisibility(View.GONE);
//        } else {
//            adView.getPriceView().setVisibility(View.GONE);
//            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
//        }

//        if (nativeAd.getStore() == null) {
//            adView.getStoreView().setVisibility(View.GONE);
//        } else {
//            adView.getStoreView().setVisibility(View.GONE);
//            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
//        }

//        if (nativeAd.getStarRating() == null || nativeAd.getStarRating() < 3) {
//            adView.getStarRatingView().setVisibility(View.INVISIBLE);
//        } else {
//            ((RatingBar) adView.getStarRatingView())
//                    .setRating(nativeAd.getStarRating().floatValue());
//            adView.getStarRatingView().setVisibility(View.VISIBLE);
//        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad. The SDK will populate the adView's MediaView
        // with the media content from this native ad.
        adView.setNativeAd(nativeAd);

        shine = adView.findViewById(R.id.shine);
        shineAnimation();
    }

    private void shineAnimation() {

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.left_right);
        shine.startAnimation(anim);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                shine.startAnimation(animation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void checkUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(this);
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {
                if ((result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE)
                        && result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(result, AppUpdateType.IMMEDIATE, MainActivity.this, RequestUpdate);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void intiReview() {
        manager = ReviewManagerFactory.create(MainActivity.this);
        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                reviewInfo = task.getResult();
            } else {
                // There was some problem, continue regardless of the result.
                reviewInfo = null;
            }
        });
    }

    protected synchronized MyApplication getMainApp() {
        if (myApplication == null) {
            myApplication = MyApplication.getInstance();
        }
        return myApplication;
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
        assert activity != null;
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {
                if (result.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(result, AppUpdateType.IMMEDIATE, MainActivity.this, RequestUpdate);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        exitDialogFragment = new ExitDialogFragment(this, unifiedNativeAdObj);
        shimmerFrameLayout = (ShimmerFrameLayout) findViewById(R.id.native_ad_banner_shimmer);
        if (!settings.getSubscriptionState()) {
            nativeContainer = findViewById(R.id.native_container);
            nativeContainer.setVisibility(View.GONE);
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.startShimmer();
            loadNativeAdMainScreen();
        } else {
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!settings.getSubscriptionState())
            loadNativeAd();
    }

    public void initViews() {
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
        if (activity.getIntent().getExtras() != null) {
            for (String key : activity.getIntent().getExtras().keySet()) {
                CopyKey = key;
                String value = activity.getIntent().getExtras().getString(CopyKey);
                if (CopyKey.equals("android.intent.extra.TEXT")) {
                    CopyValue = activity.getIntent().getExtras().getString(CopyKey);
                    callText(value);
                } else {
                    CopyValue = "";
                    callText(value);
                }
            }
        }
        if (clipBoard != null) {
            clipBoard.addPrimaryClipChangedListener(new ClipboardListener() {
                @Override
                public void onPrimaryClipChanged() {
                    try {
                        showNotification(Objects.requireNonNull(clipBoard.getPrimaryClip().getItemAt(0).getText()).toString());
                    } catch (
                            Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        if (Build.VERSION.SDK_INT >= 23) {
            checkPermissions(0);
        }

        binding.rvTikTok.setOnClickListener(this);
        binding.rvGallery.setOnClickListener(this);
        binding.rvAbout.setOnClickListener(this);
        binding.rvShareApp.setOnClickListener(this);
        binding.rvRateApp.setOnClickListener(this);
        binding.rvMoreApp.setOnClickListener(this);
        binding.rvProSubscription.setOnClickListener(this);
        binding.rvTikTokTrendings.setOnClickListener(this);
        binding.rvTikTokDiscover.setOnClickListener(this);

        createFileFolder();

    }

    private void callText(String CopiedText) {
        try {
            if (CopiedText.contains("tiktok.com")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(103);
                } else {
                    callTikTokActivity();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        Intent i = null;

        switch (v.getId()) {

            case R.id.rvTikTok:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(103);
                } else {
                    callTikTokActivity();
                }
                getMainApp().trackFireBaseEvent("TIKTOK_BUTTON", "CLICK", "TRUE");
                break;
            case R.id.rvGallery:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(105);
                } else {
                    callGalleryActivity();
                }
                getMainApp().trackFireBaseEvent("GALLERY_BUTTON", "CLICK", "TRUE");
                break;

            case R.id.relHowToDownloadContainer:
//                i = new Intent(activity, AboutUsActivity.class);
//                startActivity(i);
                HowToUseDialogFragment howToUseDialogFragment = new HowToUseDialogFragment();
                howToUseDialogFragment.show(getSupportFragmentManager(), "HowToUseDialogFragment");
                getMainApp().trackFireBaseEvent("ABOUT_BUTTON", "CLICK", "TRUE");

                break;
            case R.id.relShareContainer:
                Utils.ShareApp(activity);
                getMainApp().trackFireBaseEvent("SHARE_BUTTON", "CLICK", "TRUE");
                break;
            case R.id.rvRateApp:
                Utils.RateApp(activity);
                getMainApp().trackFireBaseEvent("RATE_BUTTON", "CLICK", "TRUE");
                break;
            case R.id.rvMoreApp:
                Utils.MoreApp(activity);
                getMainApp().trackFireBaseEvent("MORE_BUTTON", "CLICK", "TRUE");
                break;
            case R.id.rvProSubscription:
                openSubscriptionDialog();
                break;
            case R.id.rvTikTokTrendings:
                openTikTokTrendings();
                break;
            case R.id.rvTikTokDiscover:
                openTikTokDiscover();
                break;
            case R.id.relGamesContainer:
                try {
                    checkChromAppInstalled();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.relRateContainer:
//                if (reviewInfo != null) {
//                    Utils.setToast(MainActivity.this, "Give a best rate to us!");
//                    Task<Void> flow = manager.launchReviewFlow(activity, reviewInfo);
//                    flow.addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void result) {
//
//                        }
//                    });
//                    flow.addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(Exception e) {
//                        }
//                    });
//                }
                Utils.RateApp(activity);
                break;

        }
    }

    private void checkChromAppInstalled() {
        boolean isAppInstalled = appInstalledOrNot("com.android.chrome");
        if (isAppInstalled) {
            openGameWebView();
        } else {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.game_url)));
            startActivity(browserIntent);
        }
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

    private void openGameWebView() {

        try {
            String url = getString(R.string.game_url);
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setShowTitle(false);
            builder.setColorScheme(COLOR_SCHEME_SYSTEM);
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.intent.setPackage("com.android.chrome");
            customTabsIntent.launchUrl(this, Uri.parse(url));
        } catch (Exception e) {
            e.printStackTrace();
            String url = getString(R.string.game_url);
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setShowTitle(false);
            builder.setToolbarColor(Color.parseColor("#021B2A"));
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.intent.setPackage("com.android.chrome");
            customTabsIntent.launchUrl(this, Uri.parse(url));

        }

    }

    private void openTikTokDiscover() {
        Intent intent = new Intent(this, WebviewAcitivity.class);
        intent.putExtra("URL", "https://www.tiktok.com/discover?lang=" + Locale.getDefault().getLanguage());
        intent.putExtra("Title", "Tiktok Discover");
        startActivity(intent);
    }

    private void openTikTokTrendings() {
        Intent intent = new Intent(this, WebviewAcitivity.class);
        intent.putExtra("URL", "https://www.tiktok.com/?lang=" + Locale.getDefault().getLanguage() + "&is_copy_url=1&is_from_webapp=v1");
        intent.putExtra("Title", "Tiktok Trendings");
        startActivity(intent);
    }

    private void openSubscriptionDialog() {
        SubscriptionDialogFragment subscriptionDialogFragment = new SubscriptionDialogFragment(this, settings.getSubscribedItemMonthlyPrice(), settings.getSubscribedItemWeeklyPrice());
        subscriptionDialogFragment.show(getSupportFragmentManager(), "SubscriptionDialogFragment");
    }


    public void callTikTokActivity() {

        Intent i = new Intent(activity, TikTokNewActivity.class);
        i.putExtra("CopyIntent", CopyValue);
        startActivityForResult(i, REQUEST_CODE_TIKTOK);


    }

    public void callGalleryActivity() {


        Intent i = new Intent(activity, GalleryActivity.class);
        startActivity(i);
    }

    public void showNotification(String Text) {
//        if (Text.contains("instagram.com") || Text.contains("facebook.com") || Text.contains("tiktok.com") || Text.contains("twitter.com")) {
        if (Text.contains("tiktok.com")) {
            Intent intent = new Intent(activity, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("Notification", Text);
            PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(getResources().getString(R.string.app_name),
                        getResources().getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
                mChannel.enableLights(true);
                mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                notificationManager.createNotificationChannel(mChannel);
            }
            NotificationCompat.Builder notificationBuilder;
            notificationBuilder = new NotificationCompat.Builder(activity, getResources().getString(R.string.app_name))
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setColor(getResources().getColor(R.color.white))
                    .setLargeIcon(BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_launcher_round))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentTitle("Copied text")
                    .setContentText(Text)
                    .setChannelId(getResources().getString(R.string.app_name))
                    .setFullScreenIntent(pendingIntent, true);
            notificationManager.notify(1, notificationBuilder.build());
        }
    }

    private boolean checkPermissions(int type) {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(activity, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) (activity),
                    listPermissionsNeeded.toArray(new
                            String[listPermissionsNeeded.size()]), type);
            return false;
        } else {
            if (type == 103) {
                callTikTokActivity();
            } else if (type == 105) {
                callGalleryActivity();
            }

        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 103) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callTikTokActivity();
            } else {
            }
            return;
        } else if (requestCode == 105) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callGalleryActivity();
            } else {
            }
            return;
        }

    }

    @Override
    public void onBackPressed() {
        this.doubleBackToExitPressedOnce = true;
        if (doubleBackToExitPressedOnce) {
            exitDialogFragment.show(getSupportFragmentManager(), "ExitDialogFragment");
        }
        //Utils.setToast(activity, "Please click BACK again to exit");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }

    @Override
    public void onYesClick(Dialog dialog) {
        dialog.dismiss();
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_HOME);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_CODE_TIKTOK) {
            if (resultCode == Activity.RESULT_OK) {
                if (reviewInfo != null) {
                    Utils.setToast(MainActivity.this, "Give a best rate to us!");
                    Task<Void> flow = manager.launchReviewFlow(activity, reviewInfo);
                    flow.addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void result) {

                        }
                    });
                    flow.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                        }
                    });
                }
            }
        }
    }

    @Override
    public void subscriptionClick(String state) {

        if (!state.equals("")) {
            subscriptionBundleState = state;
            if (subscriptionBundleState.equals("weekly")) {
                ITEM_SKU_SUBSCRIBE = ITEM_SKU_SUBSCRIBE_WEEKLY;
                subscribe(ITEM_SKU_SUBSCRIBE_WEEKLY);
            } else if (subscriptionBundleState.equals("monthly")) {
                ITEM_SKU_SUBSCRIBE = ITEM_SKU_SUBSCRIBE_MONTHLY;
                subscribe(ITEM_SKU_SUBSCRIBE_MONTHLY);
            }
        } else {
            Toast.makeText(this, "Service Not Available", Toast.LENGTH_SHORT).show();
        }
    }
}
