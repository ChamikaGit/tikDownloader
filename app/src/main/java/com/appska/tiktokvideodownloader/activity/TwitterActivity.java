package com.appska.tiktokvideodownloader.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.appska.tiktokvideodownloader.R;
import com.appska.tiktokvideodownloader.api.CommonClassForAPI;
import com.appska.tiktokvideodownloader.databinding.ActivityTwitterBinding;
import com.appska.tiktokvideodownloader.model.TwitterResponse;
import com.appska.tiktokvideodownloader.util.AdsUtils;
import com.appska.tiktokvideodownloader.util.SharePrefs;
import com.appska.tiktokvideodownloader.util.Utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import io.reactivex.observers.DisposableObserver;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static com.appska.tiktokvideodownloader.util.Utils.RootDirectoryTwitter;
import static com.appska.tiktokvideodownloader.util.Utils.createFileFolder;
import static com.appska.tiktokvideodownloader.util.Utils.startDownload;

public class TwitterActivity extends AppCompatActivity {
    private ActivityTwitterBinding binding;
    TwitterActivity activity;
    CommonClassForAPI commonClassForAPI;
    private String VideoUrl;
    private ClipboardManager clipBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_twitter);
        activity = this;
        commonClassForAPI = CommonClassForAPI.getInstance(activity);
        createFileFolder();
        initViews();



        AdsUtils.showGoogleBannerAd(TwitterActivity.this,binding.adView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
        assert activity != null;
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
        PasteText();
    }

    private void initViews() {
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);

        binding.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.imInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.layoutHowTo.LLHowToLayout.setVisibility(View.VISIBLE);
            }
        });

        binding.layoutHowTo.imHowto1.setImageResource(R.drawable.tw1);
        binding.layoutHowTo.imHowto2.setImageResource(R.drawable.tw2);
        binding.layoutHowTo.imHowto3.setImageResource(R.drawable.tw3);
        binding.layoutHowTo.imHowto4.setImageResource(R.drawable.tw4);
        binding.layoutHowTo.tvHowTo1.setText("1. Open Twitter");
        binding.layoutHowTo.tvHowTo3.setText("1. Open Twitter");
        if (!SharePrefs.getInstance(activity).getBoolean(SharePrefs.ISSHOWHOWTOTWITTER)) {
            SharePrefs.getInstance(activity).putBoolean(SharePrefs.ISSHOWHOWTOTWITTER,true);
            binding.layoutHowTo.LLHowToLayout.setVisibility(View.VISIBLE);
        }else {
            binding.layoutHowTo.LLHowToLayout.setVisibility(View.GONE);
        }


        binding.loginBtn1.setOnClickListener(v -> {
            String LL = binding.etText.getText().toString();
            if (LL.equals("")) {
                Utils.setToast(activity, "Enter Url");
            } else if (!Patterns.WEB_URL.matcher(LL).matches()) {
                Utils.setToast(activity, "Enter Valid Url");
            } else {
                Utils.showProgressDialog(activity);
                GetTwitterData();
            }
        });

        binding.tvPaste.setOnClickListener(v -> {
            PasteText();
        });

        binding.LLOpenTwitter.setOnClickListener(v -> {
            Utils.OpenApp(activity,"com.twitter.android");
        });
    }

    private void GetTwitterData() {
        try {
            createFileFolder();
            URL url = new URL(binding.etText.getText().toString());
            String host = url.getHost();
            if (host.contains("twitter.com")) {
                Long id = getTweetId(binding.etText.getText().toString());
                if (id != null) {
                    callGetTwitterData(String.valueOf(id));
                }
            } else {
                Utils.setToast(activity, "Enter Valid Url");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Long getTweetId(String s) {
        try {
            String[] split = s.split("\\/");
            String id = split[5].split("\\?")[0];
            return Long.parseLong(id);
        } catch (Exception e) {
            Log.d("TAG", "getTweetId: " + e.getLocalizedMessage());
            return null;
        }
    }

    private void PasteText() {
        try {
            binding.etText.setText("");
            String CopyIntent = getIntent().getStringExtra("CopyIntent");
            if (CopyIntent.equals("")) {

                if (!(clipBoard.hasPrimaryClip())) {

                } else if (!(clipBoard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {
                    if (clipBoard.getPrimaryClip().getItemAt(0).getText().toString().contains("twitter.com")) {
                        binding.etText.setText(clipBoard.getPrimaryClip().getItemAt(0).getText().toString());
                    }

                } else {
                    ClipData.Item item = clipBoard.getPrimaryClip().getItemAt(0);
                    if (item.getText().toString().contains("twitter.com")) {
                        binding.etText.setText(item.getText().toString());
                    }

                }
            } else {
                if (CopyIntent.contains("twitter.com")) {
                    binding.etText.setText(CopyIntent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callGetTwitterData(String id) {
        String URL = "https://twittervideodownloaderpro.com/twittervideodownloadv2/index.php";
        try {
            Utils utils = new Utils(activity);
            if (utils.isNetworkAvailable()) {
                if (commonClassForAPI != null) {
                    Utils.showProgressDialog(activity);
                    commonClassForAPI.callTwitterApi(observer,URL,id);
                }
            } else {
                Utils.setToast(activity, "No Internet Connection");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    private DisposableObserver<TwitterResponse> observer = new DisposableObserver<TwitterResponse>() {
        @Override
        public void onNext(TwitterResponse twitterResponse) {
            Utils.hideProgressDialog(activity);
            try {
                VideoUrl = twitterResponse.getVideos().get(0).getUrl();
                if (twitterResponse.getVideos().get(0).getType().equals("image")){
                    startDownload(VideoUrl, RootDirectoryTwitter, activity, getFilenameFromURL(VideoUrl,"image"));
                    binding.etText.setText("");
                }else {
                    VideoUrl = twitterResponse.getVideos().get(twitterResponse.getVideos().size()-1).getUrl();
                    startDownload(VideoUrl, RootDirectoryTwitter, activity, getFilenameFromURL(VideoUrl,"mp4"));
                    binding.etText.setText("");
                }

            } catch (Exception e) {
                e.printStackTrace();
                Utils.setToast(activity,"No Media on Tweet or Invalid Link");
            }
        }

        @Override
        public void onError(Throwable e) {
            Utils.hideProgressDialog(activity);
            e.printStackTrace();

        }

        @Override
        public void onComplete() {
            Utils.hideProgressDialog(activity);
        }
    };


    public String getFilenameFromURL(String url, String type) {
        if (type.equals("image")){
            try {
                return new File(new URL(url).getPath()).getName() + "";
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return System.currentTimeMillis() + ".jpg";
            }
        }else {
            try {
                return new File(new URL(url).getPath()).getName() + "";
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return System.currentTimeMillis() + ".mp4";
            }
        }
    }
}