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
import com.google.android.gms.ads.nativead.NativeAd;

import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.R;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util.TemplateView;

public class NoInternetDialogFragment extends DialogFragment implements View.OnClickListener {

    private TextView tvYes, tvNo;
    private OnItemClickListener onItemClickListener;

    public NoInternetDialogFragment() {
    }

    public NoInternetDialogFragment(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onYesClick(Dialog dialog);
        void onNoClick(Dialog dialog);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        View view = inflater.inflate(R.layout.dialogfragment_no_internet, container, false);
        tvYes = view.findViewById(R.id.tvYes);
        tvNo = view.findViewById(R.id.tvNo);
        tvYes.setOnClickListener(this);
        tvNo.setOnClickListener(this);
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
                onItemClickListener.onNoClick(getDialog());
                break;

            case R.id.tvYes:
                Dialog dialog2 = getDialog();
                if (dialog2 != null) {
                    dialog2.dismiss();
                }
                onItemClickListener.onYesClick(getDialog());
                break;

        }
    }
}
