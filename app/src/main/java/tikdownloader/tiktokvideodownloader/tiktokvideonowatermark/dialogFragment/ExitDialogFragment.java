package tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.dialogFragment;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


import com.google.android.gms.ads.AdValue;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.nativead.NativeAd;

import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.R;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.activity.MainActivity;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util.AdsUtils;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util.TemplateView;

public class ExitDialogFragment extends DialogFragment implements View.OnClickListener {

    private TextView tvYes, tvNo;
    private AdView adView;
    private OnItemClickListener onItemClickListener;
    private NativeAd unifiedNativeAd;
    private TemplateView templateView;


    public ExitDialogFragment() {
    }

    public ExitDialogFragment(OnItemClickListener onItemClickListener, NativeAd unifiedNativeAd) {
        this.onItemClickListener = onItemClickListener;
        this.unifiedNativeAd = unifiedNativeAd;
    }

    public interface OnItemClickListener {
        void onYesClick(Dialog dialog);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);
        View view = inflater.inflate(R.layout.dialogfragment_exit, container, false);
        tvYes = view.findViewById(R.id.tvYes);
        tvNo = view.findViewById(R.id.tvNo);
        templateView = view.findViewById(R.id.my_template);
//        adView = view.findViewById(R.id.adView);
        tvYes.setOnClickListener(this);
        tvNo.setOnClickListener(this);
//        AdsUtils.showGoogleBannerAd(getActivity(), adView);
        showNativeAd();
        return view;
    }

    private void showNativeAd() {
        if (unifiedNativeAd == null){
            templateView.setVisibility(View.GONE);
        }else {
            templateView.setVisibility(View.VISIBLE);
            templateView.setNativeAd(unifiedNativeAd);
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
                break;

            case R.id.tvYes:
                onItemClickListener.onYesClick(getDialog());
                break;

        }
    }
}
