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
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.nativead.NativeAd;

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
    private NativeAd unifiedNativeAd;
    private TemplateView templateView;

    public VideoReadyDialogFragment(Context context,OnItemClickListener onItemClickListener,String thumb,NativeAd unifiedNativeAd) {
        this.onItemClickListener = onItemClickListener;
        this.thumb = thumb;
        this.context =context;
        this.unifiedNativeAd = unifiedNativeAd;
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
        if (unifiedNativeAd == null){
            templateView.setVisibility(View.GONE);
        }else {
            templateView.setVisibility(View.VISIBLE);
            templateView.setNativeAd(this.unifiedNativeAd);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setGravity(Gravity.BOTTOM);
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
