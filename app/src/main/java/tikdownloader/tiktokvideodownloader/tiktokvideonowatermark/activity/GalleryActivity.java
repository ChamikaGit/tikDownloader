package tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.ads.mediation.facebook.FacebookAdapter;
import com.google.ads.mediation.facebook.FacebookExtras;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;

import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.R;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.databinding.ActivityGalleryBinding;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.fragment.TikTokDownloadedFragment;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util.AdsUtils;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util.Settings;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util.TemplateView;

import java.util.ArrayList;
import java.util.List;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;
import static tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util.Utils.createFileFolder;

public class GalleryActivity extends AppCompatActivity {
    GalleryActivity activity;
    ActivityGalleryBinding binding;
    private InterstitialAd mInterstitialAdBackPress, mInterstitialAdOpen;
    private ProgressDialog progressDialog;
    private int newCount = 1;
    private TemplateView templateView;
    private NativeAd unifiedNativeAdObj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gallery);
        activity = this;
        templateView = findViewById(R.id.my_template);
        initViews();

        int getCount = new Settings(this).getAdShowCountGallery();
        newCount = getCount + 1;
        new Settings(this).setAdShowCountGallery(newCount);

        if (newCount % 3 == 0) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setTitle("Ad Loading");
            progressDialog.setMessage("Please wait..!");
            progressDialog.show();
        }

//        AdsUtils.showGoogleBannerAd(GalleryActivity.this, binding.adView);
        loadNativeAd();
        loadAdOpen();
        loadAdBack();

//        mInterstitialAdBackPress = new InterstitialAd(this);
//        mInterstitialAdBackPress.setAdUnitId(getResources().getString(R.string.admob_interstitial_ad));
//        mInterstitialAdBackPress.loadAd(new AdRequest.Builder().build());
//
//        mInterstitialAdOpen = new InterstitialAd(this);
//        mInterstitialAdOpen.setAdUnitId(getResources().getString(R.string.admob_interstitial_ad));
//        mInterstitialAdOpen.loadAd(new AdRequest.Builder().build());
//
//        mInterstitialAdBackPress.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                // Code to be executed when an ad finishes loading.
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                // Code to be executed when an ad request fails.
//            }
//
//            @Override
//            public void onAdOpened() {
//                // Code to be executed when the ad is displayed.
//            }
//
//            @Override
//            public void onAdClicked() {
//
//                // Code to be executed when the user clicks on an ad.
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//                // Code to be executed when the user has left the app.
//            }
//
//            @Override
//            public void onAdClosed() {
//
//                onBackPressed();
//            }
//        });
//
//        mInterstitialAdOpen.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                // Code to be executed when an ad finishes loading.
//                if (progressDialog.isShowing()) {
//                    progressDialog.dismiss();
//                }
//                if (newCount % 3 == 0) {
//                    mInterstitialAdOpen.show();
//                }
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                // Code to be executed when an ad request fails.
//                if (progressDialog.isShowing()) {
//                    progressDialog.dismiss();
//                }
//            }
//
//            @Override
//            public void onAdOpened() {
//                // Code to be executed when the ad is displayed.
//            }
//
//            @Override
//            public void onAdClicked() {
//
//                // Code to be executed when the user clicks on an ad.
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//                // Code to be executed when the user has left the app.
//                if (progressDialog.isShowing()) {
//                    progressDialog.dismiss();
//                }
//            }
//
//            @Override
//            public void onAdClosed() {
//            }
//        });

    }

    private void loadNativeAd() {

        Bundle extras = new FacebookExtras().setNativeBanner(true).build();

        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(false)
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        AdLoader adLoader = new AdLoader.Builder(GalleryActivity.this, getString(R.string.admob_native_ad))
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
                        showNativeAd(unifiedNativeAdObj);
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
                .withNativeAdOptions(adOptions)
                .build();

        adLoader.loadAd(new AdRequest.Builder()
                .addNetworkExtrasBundle(FacebookAdapter.class, extras)
                .build());
    }

    private void showNativeAd(NativeAd unifiedNativeAdObj) {
        if (unifiedNativeAdObj == null){
            templateView.setVisibility(View.GONE);
        }else {
            templateView.setVisibility(View.VISIBLE);
            templateView.setNativeAd(unifiedNativeAdObj);
        }
    }

    private void loadAdBack() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, getResources().getString(R.string.admob_interstitial_ad), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                GalleryActivity.this.mInterstitialAdBackPress = interstitialAd;
//                        Log.i(TAG, "onAdLoaded");
//                        Toast.makeText(MyActivity.this, "onAdLoaded()", Toast.LENGTH_SHORT).show();
                interstitialAd.setFullScreenContentCallback(
                        new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                GalleryActivity.this.mInterstitialAdBackPress = null;
                                Log.d("TAG", "The ad was dismissed.");
                                onBackPressed();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                GalleryActivity.this.mInterstitialAdBackPress = null;
                                Log.d("TAG", "The ad failed to show.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                Log.d("TAG", "The ad was shown.");
                            }
                        });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
//                        Log.i(TAG, loadAdError.getMessage());
                mInterstitialAdBackPress = null;

//                        String error =
//                                String.format(
//                                        "domain: %s, code: %d, message: %s",
//                                        loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());
//                        Toast.makeText(
//                                MyActivity.this, "onAdFailedToLoad() with error: " + error, Toast.LENGTH_SHORT)
//                                .show();
            }
        });

    }

    private void loadAdOpen() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, getResources().getString(R.string.admob_interstitial_ad), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                GalleryActivity.this.mInterstitialAdOpen = interstitialAd;
//                        Log.i(TAG, "onAdLoaded");
//                        Toast.makeText(MyActivity.this, "onAdLoaded()", Toast.LENGTH_SHORT).show();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (newCount % 3 == 0) {
                    mInterstitialAdOpen.show(GalleryActivity.this);
                }
                interstitialAd.setFullScreenContentCallback(
                        new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                GalleryActivity.this.mInterstitialAdOpen = null;
                                Log.d("TAG", "The ad was dismissed.");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                GalleryActivity.this.mInterstitialAdOpen = null;
                                Log.d("TAG", "The ad failed to show.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
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

    public void initViews() {
        setupViewPager(binding.viewpager);
//        binding.tabs.setupWithViewPager(binding.viewpager);
        binding.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

//        for (int i = 0; i < binding.tabs.getTabCount(); i++) {
//            TextView tv=(TextView) LayoutInflater.from(activity).inflate(R.layout.custom_tab,null);
//            binding.tabs.getTabAt(i).setCustomView(tv);
//        }

        binding.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        createFileFolder();
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(activity.getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
//        adapter.addFragment(new InstaDownloadedFragment(), "Instagram");
//        adapter.addFragment(new WhatsAppDowndlededFragment(), "Whatsapp");
        adapter.addFragment(new TikTokDownloadedFragment(), "TikTok");
//        adapter.addFragment(new FBDownloadedFragment(), "Facebook");
//        adapter.addFragment(new TwitterDownloadedFragment(), "Twitter");

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        if (mInterstitialAdBackPress != null) {
            mInterstitialAdBackPress.show(GalleryActivity.this);
        } else {
            setResult(Activity.RESULT_OK);
            finish();
        }
    }
}
