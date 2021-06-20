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
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util.TemplateView;

public class LockVideoDialogFragment extends DialogFragment implements View.OnClickListener {

    private TextView tvYes, tvNo;
    private AdView adView;
    private OnItemClickListener onItemClickListener;
    private TemplateView templateView;


    public LockVideoDialogFragment() {
    }

    public LockVideoDialogFragment(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onWatchVideoClick(Dialog dialog);
        void onDismissVideoSubscribeClick(Dialog dialog);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);
        View view = inflater.inflate(R.layout.dialogfragment_lock_video, container, false);
        tvYes = view.findViewById(R.id.tvYes);
        tvNo = view.findViewById(R.id.tvNo);
        templateView = view.findViewById(R.id.my_template);
//        adView = view.findViewById(R.id.adView);
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
            dialog.getWindow().setGravity(Gravity.CENTER_VERTICAL);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvYes:
                Dialog dialog = getDialog();
                if (dialog != null) {
                    dialog.dismiss();
                }
                onItemClickListener.onWatchVideoClick(getDialog());
                break;

            case R.id.tvNo:
                Dialog dialog2 = getDialog();
                if (dialog2 != null) {
                    dialog2.dismiss();
                }
                onItemClickListener.onDismissVideoSubscribeClick(getDialog());
                break;

        }
    }
}
