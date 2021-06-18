package tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.R;

import static tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.activity.SplashScreen.nativeAdObjSplash;

public class NativeAdsActivity extends AppCompatActivity implements View.OnClickListener {

    private View shine;
    private Intent intent;
    private NativeAd nativeAdObj;
    private RelativeLayout relSkipContainer;
    private TextView tvSkipTime;
    private CountDownTimer countDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_native_ads);
        relSkipContainer = findViewById(R.id.relSkipContainer);
        tvSkipTime = findViewById(R.id.tvSkipTime);
        relSkipContainer.setOnClickListener(this::onClick);
        intent = getIntent();
        if (intent != null) {
            nativeAdObj = nativeAdObjSplash;
            if (nativeAdObj != null) {
                FrameLayout nativeContainer = findViewById(R.id.native_container);
                NativeAdView adView = (NativeAdView) getLayoutInflater().inflate(R.layout.native_ad_after_splash, null);
                populateNativeAdView(nativeAdObj, adView);
                nativeContainer.removeAllViews();
                nativeContainer.addView(adView);
            }
        }

        countDownTimer = new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
                tvSkipTime.setText("SKIP (" + millisUntilFinished / 1000+")");
                //here you can have your logic to set text to edittext
            }
            public void onFinish() {
                openMainActivitiy();
            }
        }.start();

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                openMainActivitiy();
//            }
//        },4000);
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
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
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

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.GONE);
        } else {
            adView.getPriceView().setVisibility(View.GONE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.GONE);
        } else {
            adView.getStoreView().setVisibility(View.GONE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null || nativeAd.getStarRating() < 3) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

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

    private void openMainActivitiy() {
        Intent i = new Intent(NativeAdsActivity.this, MainActivity.class);
        startActivity(i);
        finishAffinity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (nativeAdObj != null) {
            nativeAdObj.destroy();
        }
        if (countDownTimer!=null){
            countDownTimer.cancel();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.relSkipContainer) {
//            openMainActivitiy();
        }
    }
}