package tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.ads.mediation.facebook.FacebookAdapter;
import com.google.ads.mediation.facebook.FacebookExtras;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.BuildConfig;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.MyApplication;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.R;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.api.APIServices;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.api.CommonClassForAPI;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.api.RestClient;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.api.RetrofitClientInstance;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.databinding.ActivityTikTokBinding;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.dialogFragment.TryAgainDialogFragment;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.dialogFragment.VideoReadyDialogFragment;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.jni.TikTokFullCryptor;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.model.TiktokModelNew;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util.AdsUtils;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util.Settings;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util.SharePrefs;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util.Utils;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util.Utils.RootDirectoryTikTok;
import static tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util.Utils.createFileFolder;
import static tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util.Utils.setToast;

public class TikTokNewActivity extends AppCompatActivity implements TryAgainDialogFragment.OnItemClickListener, VideoReadyDialogFragment.OnItemClickListener {
    private ActivityTikTokBinding binding;
    TikTokNewActivity activity;
    CommonClassForAPI commonClassForAPI;
    private String VideoUrl;
    private ClipboardManager clipBoard;
    boolean IsWithWaternark = true;
    private MyApplication myApplication;

    private InterstitialAd mInterstitialAdDownload, mInterstitialAdBackPress, mInterstitialAdOpen;
    private ImageView img, imgPicture;
    private TextView tvName, tvDescription, tvKeywords, tvCommentCount, tvDownloadNow, tvPast;
    private ProgressBar progressBar;
    private LinearLayout relHowToDownloadContainerNew;
    private ProgressDialog progressDialog;
    private int tryCount = 0;
    private int newCount = 1;
    private String notMarked = "", marked = "";
    private NativeAd unifiedNativeAdObj;
    public static NativeAd nativeAdObjDownloadScreen;
    private AdLoader adLoaderDownloadScreen;
    private View shine;
    private boolean isHowtoDownloadVisible = false;
    private ShimmerFrameLayout shimmerFrameLayout;
    private Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tik_tok);
        hideKeyboard();
        myApplication = (MyApplication) getApplication();
        settings = new Settings(this);
//        int getCount = new Settings(this).getAdShowCount();
//        newCount = getCount + 1;
//        new Settings(this).setAdShowCount(newCount);
//        if (newCount % 5 == 0) {
//            progressDialog = new ProgressDialog(this);
//            progressDialog.setCancelable(false);
//            progressDialog.setTitle("Ad Loading");
//            progressDialog.setMessage("Please wait..!");
//            progressDialog.show();
//        }

        relHowToDownloadContainerNew = findViewById(R.id.relHowToDownloadContainerNew);
        progressBar = findViewById(R.id.progressBar);
        shimmerFrameLayout = (ShimmerFrameLayout) findViewById(R.id.native_ad_banner_shimmer);
        activity = this;
        commonClassForAPI = CommonClassForAPI.getInstance(activity);
        createFileFolder();
        initViews();
//        AdsUtils.showGoogleBannerAd(TikTokNewActivity.this, binding.adView);


        if (!settings.getSubscriptionState()) {
            shimmerFrameLayout.startShimmer();
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            loadNativeAdDownloadScreen();
            //loadAdOpen();
            loadAdDownload();
//            loadNativeAd();
        } else {
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
        }


    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = getCurrentFocus();
        if (v != null)
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void loadAdOpen() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, getResources().getString(R.string.admob_interstitial_ad), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                TikTokNewActivity.this.mInterstitialAdOpen = interstitialAd;
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (newCount % 5 == 0) {
                    mInterstitialAdOpen.show(TikTokNewActivity.this);
                }
                interstitialAd.setFullScreenContentCallback(
                        new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                TikTokNewActivity.this.mInterstitialAdOpen = null;
                                Log.d("TAG", "The ad was dismissed.");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                TikTokNewActivity.this.mInterstitialAdOpen = null;
                                Log.d("TAG", "The ad failed to show.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                Log.d("TAG", "The ad was shown.");
                            }
                        });

            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mInterstitialAdOpen = null;
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

            }
        });

    }


    private void loadAdDownload() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, getResources().getString(R.string.admob_interstitial_ad), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                TikTokNewActivity.this.mInterstitialAdDownload = interstitialAd;

                interstitialAd.setFullScreenContentCallback(
                        new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                TikTokNewActivity.this.mInterstitialAdDownload = null;
                                Log.d("TAG", "The ad was dismissed.");
                                //after dismissed the ad activity will be closed
                                TikTokNewActivity.this.finish();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                TikTokNewActivity.this.mInterstitialAdDownload = null;
                                Log.d("TAG", "The ad failed to show.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                Log.d("TAG", "The ad was shown.");
                            }
                        });

            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mInterstitialAdDownload = null;

            }
        });

    }

//    private void loadNativeAd() {
//
//        AdLoader.Builder builder = new AdLoader.Builder(TikTokNewActivity.this, getString(R.string.admob_native_ad));
//        //for facebook
//        Bundle extras = new FacebookExtras().setNativeBanner(true).build();
//
//        builder.withAdListener(new AdListener() {
//
//            @Override
//            public void onAdFailedToLoad(LoadAdError loadAdError) {
//                super.onAdFailedToLoad(loadAdError);
//            }
//        });
//
//        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
//            @Override
//            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
//
//                unifiedNativeAdObj = unifiedNativeAd;
//            }
//        });
//
//        AdLoader adLoader = builder.build();
//        adLoader.loadAd(new AdRequest.Builder()
//                .addNetworkExtrasBundle(FacebookAdapter.class, extras)
//                .build());
//    }

    private void loadNativeAdDownloadScreen() {
        adLoaderDownloadScreen = new AdLoader.Builder(getApplicationContext(), getString(R.string.admob_native_ad_media2))
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                        if (isDestroyed()) {
                            nativeAd.destroy();
                            Log.e("isDestroyed","isDestroyed"+isDestroyed());
                            return;
                        }
                        if (nativeAdObjDownloadScreen!=null){
                            nativeAdObjDownloadScreen.destroy();
                        }
                        nativeAdObjDownloadScreen = nativeAd;
                        if (nativeAdObjDownloadScreen != null) {
                            binding.adView.setVisibility(View.GONE);
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            FrameLayout nativeContainer = findViewById(R.id.native_container);
                            NativeAdView adView = (NativeAdView) getLayoutInflater().inflate(R.layout.native_ad_banner_rectangle, null);
                            populateNativeAdView(nativeAdObjDownloadScreen, adView);
                            nativeContainer.removeAllViews();
                            nativeContainer.addView(adView);
                        } else {
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            if (!settings.getSubscriptionState()) {
                                //check if user enable the in-app subscribed
                                AdsUtils.showGoogleBannerAd(TikTokNewActivity.this, binding.adView);
                            }
                        }
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError error) {
                        nativeAdObjDownloadScreen = null;
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        if (!settings.getSubscriptionState()) {
                            //check if user enable the in-app subscribed
                            AdsUtils.showGoogleBannerAd(TikTokNewActivity.this, binding.adView);
                        }
//                        Toast.makeText(SplashScreen.this, "Failed to load native ad: " + error, Toast.LENGTH_SHORT).show();
                    }
                }).build();
        adLoaderDownloadScreen.loadAd(new AdRequest.Builder().build());
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

    private void loadNativeAd() {

//        AdLoader.Builder builder =  new AdLoader.Builder(MainActivity.this,getString(R.string.admob_native_ad));
//
//        //for facebook
//        Bundle extras = new FacebookExtras().setNativeBanner(true).build();
//
//        builder.withAdListener(new AdListener(){
//
//            @Override
//            public void onAdFailedToLoad(LoadAdError loadAdError) {
//                super.onAdFailedToLoad(loadAdError);
//            }
//        });
//
//        builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
//            @Override
//            public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
//                if (isDestroyed()) {
//                    nativeAd.destroy();
//                    Log.d("TAG", "Native Ad Destroyed");
//                    return;
//                }
//                unifiedNativeAdObj = nativeAd;
//            }
//        });
//
////        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
////            @Override
////            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
////
////                unifiedNativeAdObj = unifiedNativeAd;
////            }
////        });
//        builder.withNativeAdOptions(new NativeAdOptions.Builder().build());
//
//        AdLoader adLoader = builder.build();
//        adLoader.loadAd(new AdRequest.Builder()
//                .addNetworkExtrasBundle(FacebookAdapter.class, extras)
//                .build());

        Bundle extras = new FacebookExtras().setNativeBanner(true).build();

        AdLoader adLoader = new AdLoader.Builder(TikTokNewActivity.this, getString(R.string.admob_native_ad_without_media))
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                        if (isDestroyed()) {
                            nativeAd.destroy();
                            Log.d("TAG", "Native Ad Destroyed");
                            return;
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
                .withNativeAdOptions(new NativeAdOptions.Builder().build())
                .build();

        adLoader.loadAd(new AdRequest.Builder()
                .addNetworkExtrasBundle(FacebookAdapter.class, extras)
                .build());
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
        PasteText();

//        try {
//            URL url = new URL(binding.etText.getText().toString());
//            String host = url.getHost();
//            if (host.contains("tiktok.com")) {
//                //Utils.showProgressDialog(activity);
//                progressBar.setVisibility(View.VISIBLE);
//                img.setVisibility(View.VISIBLE);
//                new callGetTikTokDefaultData().execute(binding.etText.getText().toString());
//            } else {
////                Utils.setToast(activity, "Enter Valid Url");
//                img.setVisibility(View.GONE);
//                progressBar.setVisibility(View.GONE);
//            }
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }

    }

    private void initViews() {
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
//        binding.layoutHowTo.LLHowToLayout.setVisibility(View.VISIBLE);
        binding.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.imInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SharePrefs.getInstance(activity).getBoolean(SharePrefs.ISSHOWHOWTOTT)) {
                    SharePrefs.getInstance(activity).putBoolean(SharePrefs.ISSHOWHOWTOTT, true);
//                    binding.layoutHowTo.LLHowToLayout.setVisibility(View.VISIBLE);
                } else {
//                    binding.layoutHowTo.LLHowToLayout.setVisibility(View.GONE);
                }
            }
        });

//        binding.layoutHowTo.imHowto1.setImageResource(R.drawable.tt1);
//        binding.layoutHowTo.imHowto2.setImageResource(R.drawable.tt_2);
//        binding.layoutHowTo.imHowto3.setImageResource(R.drawable.tt3);
//        binding.layoutHowTo.imHowto4.setImageResource(R.drawable.tt4);
//        binding.layoutHowTo.tvHowTo1.setText("1. Open TikTok");
//        binding.layoutHowTo.tvHowTo3.setText("1. Open TikTok");


        binding.tvWithMark.setOnClickListener(v -> {
            IsWithWaternark = true;
            //version_14
            String LL = binding.etText.getText().toString();
            if (LL.equals("")) {
                Utils.setToast(activity, "Enter Url");
            } else if (!Patterns.WEB_URL.matcher(LL).matches()) {
                Utils.setToast(activity, "Enter Valid Url");
            } else {
                if (!settings.getSubscriptionState()) {
                    loadNativeAd();
//                    loadAdDownload();
                }
                GetTikTokData(IsWithWaternark);
            }
            getMainApp().trackFireBaseEvent("WITH_WATERMARK", "CLICK", "TRUE");
        });

        binding.tvWithoutMark.setOnClickListener(v -> {
            watchToDownloadWithOutWaterMark();
        });

        binding.LLOpenTikTok.setOnClickListener(v -> {
            Intent launchIntent = activity.getPackageManager().getLaunchIntentForPackage("com.zhiliaoapp.musically.go");
            Intent launchIntent1 = activity.getPackageManager().getLaunchIntentForPackage("com.zhiliaoapp.musically");
            if (launchIntent != null) {
                activity.startActivity(launchIntent);
            } else if (launchIntent1 != null) {
                activity.startActivity(launchIntent1);
            } else {
                Utils.setToast(activity, "App Not Available.");
            }

        });

        binding.tvPast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
                PasteText();
                if (!binding.etText.getText().toString().equals("")) {
                    try {
                        URL url = new URL(binding.etText.getText().toString());
                        String host = url.getHost();
                        if (host.contains("tiktok.com")) {
                            //Utils.showProgressDialog(activity);
//                            progressBar.setVisibility(View.VISIBLE);
//                            img.setVisibility(View.VISIBLE);
                            //new callGetTikTokDefaultData().execute(binding.etText.getText().toString());
                        } else {
//                Utils.setToast(activity, "Enter Valid Url");
//                            img.setVisibility(View.GONE);
//                            progressBar.setVisibility(View.GONE);
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        binding.relHowToDownloadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHowtoDownloadVisible) {
                    isHowtoDownloadVisible = false;
                    relHowToDownloadContainerNew.setVisibility(View.GONE);
                } else {
                    isHowtoDownloadVisible = true;
                    relHowToDownloadContainerNew.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    private void watchToDownloadWithOutWaterMark() {
        IsWithWaternark = false;
        //version_14
        String LL = binding.etText.getText().toString();
        if (LL.equals("")) {
            Utils.setToast(activity, "Enter Url");
        } else if (!Patterns.WEB_URL.matcher(LL).matches()) {
            Utils.setToast(activity, "Enter Valid Url");
        } else {
            if (!settings.getSubscriptionState()) {
                loadNativeAd();
//                loadAdDownload();
            }
            GetTikTokData(IsWithWaternark);
        }

        getMainApp().trackFireBaseEvent("WITHOUT_WATERMARK", "CLICK", "TRUE");
    }

    private void GetTikTokData(boolean isWithWaternark) {
        //version_8
        try {
            createFileFolder();
            String host = binding.etText.getText().toString();
            if (host.contains("tiktok")) {
                Utils.showProgressDialog(activity);
                callVideoDownload(binding.etText.getText().toString(), isWithWaternark);
            } else {
                Utils.setToast(activity, "Enter Valid Url");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callVideoDownload(String Url, boolean isWithWaternark) {
        try {
            Utils utils = new Utils(activity);
            if (utils.isNetworkAvailable()) {
                if (isWithWaternark) {
                    getTikTokAPIData(Url);
                } else {
                    getTikTokAPIDataPartner(Url);
                }
            } else {
                Utils.setToast(activity, "No Internet Connection");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    private void getTikTokAPIData(String LL) {
        try {
            Utils.showProgressDialog(activity);
//            RestClient.getInstance(mActivity).getService().getTiktokData
            APIServices service = RestClient.getInstance(TikTokNewActivity.this).getService();
            Call<JsonObject> call = service.getTiktokDataNew(Utils.TikTokUrl, LL);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Utils.hideProgressDialog(activity);
                    if (response.code() == 200) {
//                        TiktokModel model = response.body();
                        JsonObject responseBody = response.body();
                        JsonElement isSuccess = responseBody.get("success");
                        try {
                            if (IsWithWaternark) {
//                    marked = tiktokModel.getNotMarked().replace("http://", "https://");
                                marked = responseBody.get("marked").getAsString();
//                                marked = model.getNotMarked();
                                if (BuildConfig.DEBUG) {
                                    Log.e("marked1 ", "marked " + marked);
                                }
                                VideoReadyDialogFragment videoReadyDialogFragment = new VideoReadyDialogFragment(TikTokNewActivity.this, TikTokNewActivity.this, responseBody.get("thumb").getAsString(), unifiedNativeAdObj);
                                videoReadyDialogFragment.show(getSupportFragmentManager(), "VideoReadyDialogFragment");
//                    startDownload(tiktokModel.getMarked().replace("http://", "https://"),
//                            RootDirectoryTikTok, activity, "tiktok_" + System.currentTimeMillis() + ".mp4");
//                    binding.etText.setText("");
//                    showInterstitial();
//                    loadIndustrisialAd();
                            } else {
//                    notMarked = tiktokModel.getNotMarked().replace("http://", "https://");
                                notMarked = responseBody.get("not_marked").getAsString();
//                                notMarked = model.getNotMarked();
                                if (BuildConfig.DEBUG) {
                                    Log.e("notMarked ", "notMarked " + notMarked);
                                }
                                VideoReadyDialogFragment videoReadyDialogFragment = new VideoReadyDialogFragment(TikTokNewActivity.this, TikTokNewActivity.this, responseBody.get("thumb").getAsString(), unifiedNativeAdObj);
                                videoReadyDialogFragment.show(getSupportFragmentManager(), "VideoReadyDialogFragment");
//                    startDownload(tiktokModel.getNotMarked().replace("http://", "https://"),
//                            RootDirectoryTikTok, activity, "tiktok_" + System.currentTimeMillis() + ".mp4");
//                    binding.etText.setText("");
//                    showInterstitial();
//                    loadIndustrisialAd();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Utils.hideProgressDialog(activity);
                        TryAgainDialogFragment tryAgainDialogFragment = new TryAgainDialogFragment(TikTokNewActivity.this);
                        tryAgainDialogFragment.show(getSupportFragmentManager(), "TryAgainDialogFragment");
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Utils.hideProgressDialog(activity);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error", e.getMessage());
        }

    }

    private void getTikTokAPIDataPartner(String LL) {
        try {
            Utils.showProgressDialog(activity);
//            RestClient.getInstance(mActivity).getService().getTiktokData
            APIServices service = RestClient.getInstance(TikTokNewActivity.this).getService();
            Call<JsonObject> call = service.getTikTokDataTikTokAllPartner(Utils.TikTokUrlNew, LL, BuildConfig.TOKEN);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Utils.hideProgressDialog(activity);
                    if (response.code() == 200) {
//                        TiktokModel model = response.body();
                        JsonObject responseBody = response.body();
                        JsonElement data = responseBody.get("data");
                        if (BuildConfig.DEBUG) {
                            Log.e("Tiktok data", data.getAsString());
                        }
                        if (data.getAsString() != null || !data.getAsString().equals("")) {
                            try {
                                try {
                                    byte[] dataCrypt = TikTokFullCryptor.hexStr2Bytes(data.getAsString());
                                    dataCrypt = TikTokFullCryptor.crypt(dataCrypt, System.currentTimeMillis(), 1);
                                    String decrypt = "" + new String(dataCrypt, "UTF-8");
                                    if (BuildConfig.DEBUG) {
                                        Log.e("Tiktok data", new String(dataCrypt, "UTF-8"));
                                    }
                                    JSONObject jsonObject = new JSONObject(decrypt);
                                    if (jsonObject != null) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("links");
                                        if (jsonArray.length() > 0) {
                                            JSONObject linksObject = jsonArray.getJSONObject(0);
                                            notMarked = linksObject.getString("url");
                                            if (BuildConfig.DEBUG) {
                                                Log.e("Tiktok data url ", notMarked);
                                            }
                                            VideoReadyDialogFragment videoReadyDialogFragment = new VideoReadyDialogFragment(TikTokNewActivity.this, TikTokNewActivity.this, jsonObject.getString("thumbnail"), unifiedNativeAdObj);
                                            videoReadyDialogFragment.show(getSupportFragmentManager(), "VideoReadyDialogFragment");
                                        } else {
                                            Toast.makeText(TikTokNewActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                } catch (UnsupportedEncodingException | JSONException e) {
                                    e.printStackTrace();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(TikTokNewActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                                Log.e("TAG", e.getMessage());
                            }

                        } else {
                            Toast.makeText(TikTokNewActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Utils.hideProgressDialog(activity);
                        TryAgainDialogFragment tryAgainDialogFragment = new TryAgainDialogFragment(TikTokNewActivity.this);
                        tryAgainDialogFragment.show(getSupportFragmentManager(), "TryAgainDialogFragment");
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Utils.hideProgressDialog(activity);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error", e.getMessage());
        }

    }

    private void PasteText() {
        try {
            binding.etText.setText("");
            String CopyIntent = getIntent().getStringExtra("CopyIntent");
            if (CopyIntent.equals("")) {

                if (!(clipBoard.hasPrimaryClip())) {

                } else if (!(clipBoard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {
                    if (clipBoard.getPrimaryClip().getItemAt(0).getText().toString().contains("tiktok.com")) {
                        binding.etText.setText(clipBoard.getPrimaryClip().getItemAt(0).getText().toString());
                    }

                } else {
                    ClipData.Item item = clipBoard.getPrimaryClip().getItemAt(0);
                    if (item.getText().toString().contains("tiktok.com")) {
                        binding.etText.setText(item.getText().toString());
                    }

                }
            } else {
                if (CopyIntent.contains("tiktok.com")) {
                    binding.etText.setText(CopyIntent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onYesClick(Dialog dialog) {
        if (dialog.isShowing())
            dialog.dismiss();
        Utils.showProgressDialog(activity);
        callVideoDownload(binding.etText.getText().toString(), IsWithWaternark);
    }

    @Override
    public void onDownloadClick(Dialog dialog) {

        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        if (IsWithWaternark) {
            if (BuildConfig.DEBUG) {
                Log.e("startDownload ", "marked " + marked);
            }
            startDownload(marked, RootDirectoryTikTok, TikTokNewActivity.this, "tiktok_" + System.currentTimeMillis() + ".mp4", true);
            binding.etText.setText("");
            if (!settings.getSubscriptionState()) {
                showInterstitialAdDownload();
                loadAdDownload();
            }
//            loadNativeAd();
        } else {
            if (BuildConfig.DEBUG) {
                Log.e("startDownload ", "marked " + notMarked);
            }
            startDownload(notMarked, RootDirectoryTikTok, TikTokNewActivity.this, "tiktok_" + System.currentTimeMillis() + ".mp4", false);
            binding.etText.setText("");
            if (!settings.getSubscriptionState()) {
                showInterstitialAdDownload();
                loadAdDownload();
            }
//            loadNativeAd();
        }

    }

    @Override
    public void onDownloadCancelClick(Dialog dialog) {

    }


    public void startDownload(String downloadPath, String destinationPath, Context context, String FileName, boolean isWatermark) {
        if (BuildConfig.DEBUG) {
            Log.e("marked2 ", "marked " + downloadPath);
        }
        setToast(context, "Download Started");
        Uri uri = null;
        if (isWatermark) {
            uri = Uri.parse(downloadPath.trim().replace("https://", "http://")); // Path where you want to download file.
        } else {
            uri = Uri.parse(downloadPath.trim()); // Path where you want to download file.
        }
        if (BuildConfig.DEBUG) {
            Log.e("marked3 ", "uri " + uri.toString().replace("https://", "http://"));
        }
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.allowScanningByMediaScanner();
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);  // Tell on which network you want to download file.
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);  // This will show notification on top when downloading the file.
        request.setTitle(FileName + ""); // Title for notification.
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS, destinationPath + FileName);  // Storage directory path
        ((DownloadManager) context.getSystemService(DOWNLOAD_SERVICE)).enqueue(request); // This will start downloading

        try {
            if (Build.VERSION.SDK_INT >= 19) {
                MediaScannerConnection.scanFile(context, new String[]{new File(DIRECTORY_DOWNLOADS + "/" + destinationPath + FileName).getAbsolutePath()},
                        null, new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                            }
                        });
            } else {
                context.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.fromFile(new File(DIRECTORY_DOWNLOADS + "/" + destinationPath + FileName))));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("printStackTrace ", "printStackTrace " + e.getMessage().toString());
        }
    }

    //version_14
    private void showInterstitialAdDownload() {
        if (mInterstitialAdDownload != null) {
            mInterstitialAdDownload.show(TikTokNewActivity.this);
        }
    }

    private void getAllBanners(String LL) {
        try {
            Utils.showProgressDialog(activity);
            APIServices service = RetrofitClientInstance.getRetrofitInstance(TikTokNewActivity.this).create(APIServices.class);
            Call<TiktokModelNew> call = service.getTiktokVideo(LL);
            call.enqueue(new Callback<TiktokModelNew>() {
                @Override
                public void onResponse(Call<TiktokModelNew> call, Response<TiktokModelNew> response) {
                    if (response.code() == 200) {
                        TiktokModelNew bannerResponse = response.body();
                        String videoId = bannerResponse.getVideoId();
//                        https://tiktok.codespikex.com/download?id=6877028169791130885&type=video&nwm=true
                        String videoUrl = BuildConfig.URL + videoId + "&type=video&nwm=true";
//                        Log.e("videoUrl",videoUrl);
                        if (bannerResponse.getUrlNwm().equals("")) {
//                            Toast.makeText(TikTokActivity.this, "Something went wrong in video url!", Toast.LENGTH_SHORT).show();
                            fetchTheVideo(videoId);
                        } else {
                            Utils.hideProgressDialog(activity);
//                            startDownload(videoUrl, RootDirectoryTikTok, activity, "tiktok_" + System.currentTimeMillis() + ".mp4");
                        }
                    } else {
                        Utils.hideProgressDialog(activity);
                    }
                }

                @Override
                public void onFailure(Call<TiktokModelNew> call, Throwable t) {
                    Utils.hideProgressDialog(activity);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error", e.getMessage());
        }

    }

    private void fetchTheVideo(String videoId) {
        try {
            Utils.showProgressDialog(activity);
            APIServices service = RetrofitClientInstance.getRetrofitInstance(TikTokNewActivity.this).create(APIServices.class);
            Call<ResponseBody> call = service.getTiktokFetchVideo(videoId);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        Utils.hideProgressDialog(activity);
                        ResponseBody bannerResponse = response.body();
                        String videoUrl = BuildConfig.URL + videoId + "&type=video&nwm=true";
//                        startDownload(videoUrl, RootDirectoryTikTok, activity, "tiktok_" + System.currentTimeMillis() + ".mp4");
                    } else {
                        Utils.hideProgressDialog(activity);
                        Utils.setToast(TikTokNewActivity.this, "Something went wrong!");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Utils.hideProgressDialog(activity);
                }
            });
        } catch (Exception e) {
            Utils.hideProgressDialog(activity);
            e.printStackTrace();
            Log.e("Error", e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
//        if (mInterstitialAdBackPress != null && mInterstitialAdBackPress.isLoaded()) {
//            mInterstitialAdBackPress.show();
//        } else {
        setResult(Activity.RESULT_OK);
        finish();
//        }
    }
}