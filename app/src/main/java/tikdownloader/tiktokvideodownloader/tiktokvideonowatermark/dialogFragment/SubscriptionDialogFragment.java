package tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.dialogFragment;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.R;

public class SubscriptionDialogFragment extends DialogFragment implements View.OnClickListener {

    public OnItemClickListener onItemClickListener;
    public TextView tvTickWeek,tvTickMonth;
    public RelativeLayout relWeek,relMonth;
    public String state ="";

    public interface OnItemClickListener {
        void subscriptionClick(String state);
    }

    public SubscriptionDialogFragment(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private TextView tvYes, tvNo;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);
        View view = inflater.inflate(R.layout.dialogfragment_subscription, container, false);
        tvYes = view.findViewById(R.id.tvYes);
        tvNo = view.findViewById(R.id.tvNo);
        tvTickWeek = view.findViewById(R.id.tvTickWeek);
        tvTickMonth = view.findViewById(R.id.tvTickMonth);
        relWeek = view.findViewById(R.id.relWeek);
        relMonth = view.findViewById(R.id.relMonth);
        relWeek.setOnClickListener(this);
        relMonth.setOnClickListener(this);
        tvYes.setOnClickListener(this);
        tvNo.setOnClickListener(this);
        tvTickWeek.setVisibility(View.VISIBLE);
        state ="monthly";
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setGravity(Gravity.CENTER);
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
                Dialog dialog2 = getDialog();
                if (dialog2 != null) {
                    dialog2.dismiss();
                }
                onItemClickListener.subscriptionClick(state);
                break;

            case R.id.relWeek:
                tvTickWeek.setVisibility(View.VISIBLE);
                tvTickMonth.setVisibility(View.GONE);
                state ="weekly";
                break;
            case R.id.relMonth:
                tvTickWeek.setVisibility(View.GONE);
                tvTickMonth.setVisibility(View.VISIBLE);
                state ="monthly";
                break;

        }
    }
}
