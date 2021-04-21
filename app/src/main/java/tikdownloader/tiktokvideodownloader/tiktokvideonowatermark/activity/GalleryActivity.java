package tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.R;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.databinding.ActivityGalleryBinding;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.fragment.TikTokDownloadedFragment;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util.AdsUtils;

import java.util.ArrayList;
import java.util.List;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;
import static tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util.Utils.createFileFolder;

public class GalleryActivity extends AppCompatActivity {
    GalleryActivity activity;
    ActivityGalleryBinding binding;
    private InterstitialAd mInterstitialAdBackPress, mInterstitialAdOpen;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gallery);
        activity = this;
        initViews();

        AdsUtils.showGoogleBannerAd(GalleryActivity.this, binding.adView);
        mInterstitialAdBackPress = new InterstitialAd(this);
        mInterstitialAdBackPress.setAdUnitId(getResources().getString(R.string.admob_interstitial_ad));
        mInterstitialAdBackPress.loadAd(new AdRequest.Builder().build());

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Ad Loading");
        progressDialog.setMessage("Please wait..!");
        progressDialog.show();

        mInterstitialAdOpen = new InterstitialAd(this);
        mInterstitialAdOpen.setAdUnitId(getResources().getString(R.string.admob_interstitial_ad));
        mInterstitialAdOpen.loadAd(new AdRequest.Builder().build());

        mInterstitialAdBackPress.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {

                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {

                onBackPressed();
            }
        });

        mInterstitialAdOpen.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                progressDialog.dismiss();
                mInterstitialAdOpen.show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                progressDialog.dismiss();
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {

                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                progressDialog.dismiss();
            }

            @Override
            public void onAdClosed() {
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
//        if (mInterstitialAdBackPress != null && mInterstitialAdBackPress.isLoaded()) {
//            mInterstitialAdBackPress.show();
//        }else {
        setResult(Activity.RESULT_OK);
        finish();
//        }

    }
}
