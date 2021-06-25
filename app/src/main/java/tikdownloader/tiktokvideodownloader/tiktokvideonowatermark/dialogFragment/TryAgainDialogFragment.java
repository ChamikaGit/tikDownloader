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

import com.google.android.gms.ads.AdView;

import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.R;

public class TryAgainDialogFragment extends DialogFragment implements View.OnClickListener {

    private TextView tvYes, tvNo,tvDescription;
    private AdView adView;
    private OnItemClickListener onItemClickListener;
    private String msgDescription = "";


    public TryAgainDialogFragment() {
    }

    public TryAgainDialogFragment(OnItemClickListener onItemClickListener,String msgDescription) {
        this.onItemClickListener = onItemClickListener;
        this.msgDescription =msgDescription;
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
        View view = inflater.inflate(R.layout.dialogfragment_try_again, container, false);
        tvYes = view.findViewById(R.id.tvYes);
        tvNo = view.findViewById(R.id.tvNo);
        tvDescription = view.findViewById(R.id.tvDescription);
        tvDescription.setText(msgDescription);
//        adView = view.findViewById(R.id.adView);
        tvYes.setOnClickListener(this);
        tvNo.setOnClickListener(this);
//        AdsUtils.showGoogleBannerAd(getActivity(), adView);
        return view;
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
