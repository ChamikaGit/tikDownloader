package tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.dialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.R;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.activity.TikTokNewActivity;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util.TemplateView;

public class VideoReadyDialogFragment extends DialogFragment implements View.OnClickListener {

    private TextView tvYes, tvNo;
    private AdView adView;
    private OnItemClickListener onItemClickListener;
    private String thumb;
    private Context context;
    private ImageView imgThumb;
    private ProgressBar progressBar;
    private NativeAd nativeAdObjVideoReadyScreen;
    private TemplateView templateView;
    private FrameLayout nativeContainer;
    private View shine;

    public VideoReadyDialogFragment(Context context,OnItemClickListener onItemClickListener,String thumb,NativeAd nativeAdObjVideoReadyScreen) {
        this.onItemClickListener = onItemClickListener;
        this.thumb = thumb;
        this.context =context;
        this.nativeAdObjVideoReadyScreen = nativeAdObjVideoReadyScreen;
    }

    public interface OnItemClickListener {
        void onDownloadClick(Dialog dialog);
        void onDownloadCancelClick(Dialog dialog);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        View view = inflater.inflate(R.layout.dialogfragment_video_ready, container, false);
        tvYes = view.findViewById(R.id.tvYes);
        tvNo = view.findViewById(R.id.tvNo);
        imgThumb = view.findViewById(R.id.imgThumb);
        progressBar = view.findViewById(R.id.progressBar);
//        adView = view.findViewById(R.id.adView);
        tvYes.setOnClickListener(this);
        tvNo.setOnClickListener(this);
//        AdsUtils.showGoogleBannerAd(getActivity(), adView);
        templateView = view.findViewById(R.id.my_template);
        nativeContainer = view.findViewById(R.id.native_container);
        showNativeAd();
        Glide.with(context).load(thumb).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(imgThumb);

        return view;
    }

    private void showNativeAd() {
//        if (unifiedNativeAd == null){
//            templateView.setVisibility(View.GONE);
//        }else {
//            templateView.setVisibility(View.VISIBLE);
//            templateView.setNativeAd(this.unifiedNativeAd);
//        }

        if (nativeAdObjVideoReadyScreen != null) {
            nativeContainer.setVisibility(View.VISIBLE);
            NativeAdView adView = (NativeAdView) getLayoutInflater().inflate(R.layout.native_ad_banner_rectangle_medium, null);
            populateNativeAdView(nativeAdObjVideoReadyScreen, adView);
            nativeContainer.removeAllViews();
            nativeContainer.addView(adView);
        } else {
            nativeContainer.setVisibility(View.GONE);
        }
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
        //adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

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

//        if (nativeAd.getAdvertiser() == null) {
//            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
//        } else {
//            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
//            adView.getAdvertiserView().setVisibility(View.VISIBLE);
//        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad. The SDK will populate the adView's MediaView
        // with the media content from this native ad.
        adView.setNativeAd(nativeAd);

        shine = adView.findViewById(R.id.shine);
        shineAnimation();
    }

    private void shineAnimation() {

        Animation anim = AnimationUtils.loadAnimation(context, R.anim.left_right);
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

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setGravity(Gravity.CENTER_VERTICAL);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvNo:
                Dialog dialog = getDialog();
                if (dialog != null) {
                    dialog.dismiss();
                }
                onItemClickListener.onDownloadCancelClick(getDialog());
                break;

            case R.id.tvYes:
                onItemClickListener.onDownloadClick(getDialog());
                break;

        }
    }
}
