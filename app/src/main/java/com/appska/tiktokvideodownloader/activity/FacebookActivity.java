package com.cd.statussaver.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.cd.statussaver.util.AdsUtils;
import com.cd.statussaver.R;
import com.cd.statussaver.api.CommonClassForAPI;
import com.cd.statussaver.databinding.ActivityFacebookBinding;
import com.cd.statussaver.util.SharePrefs;
import com.cd.statussaver.util.Utils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static android.content.ContentValues.TAG;
import static com.cd.statussaver.util.Utils.RootDirectoryFacebook;
import static com.cd.statussaver.util.Utils.createFileFolder;
import static com.cd.statussaver.util.Utils.startDownload;

public class FacebookActivity extends AppCompatActivity {
    ActivityFacebookBinding binding;
    FacebookActivity activity;
    CommonClassForAPI commonClassForAPI;
    private String VideoUrl;
    private ClipboardManager clipBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_facebook);
        activity = this;
        commonClassForAPI = CommonClassForAPI.getInstance(activity);
        createFileFolder();
        initViews();

        AdsUtils.showGoogleBannerAd(activity,binding.adView);
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

        binding.layoutHowTo.imHowto1.setImageResource(R.drawable.fb1);
        binding.layoutHowTo.imHowto2.setImageResource(R.drawable.fb2);
        binding.layoutHowTo.imHowto3.setImageResource(R.drawable.fb3);
        binding.layoutHowTo.imHowto4.setImageResource(R.drawable.fb4);
        binding.layoutHowTo.tvHowTo1.setText("1. Open Facebook");
        binding.layoutHowTo.tvHowTo3.setText("1. Copy Video Link from Facebook");

        if (!SharePrefs.getInstance(activity).getBoolean(SharePrefs.ISSHOWHOWTOFB)) {
            SharePrefs.getInstance(activity).putBoolean(SharePrefs.ISSHOWHOWTOFB,true);
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
                GetFacebookData();
            }
        });

        binding.tvPaste.setOnClickListener(v -> {
            PasteText();
        });
        binding.LLOpenFacebbook.setOnClickListener(v -> {
            Utils.OpenApp(activity,"com.facebook.katana");
        });


    }

    private void GetFacebookData() {
        try {
            createFileFolder();
            URL url = new URL(binding.etText.getText().toString());
            String host = url.getHost();
            Log.e("initViews: ", host);

            if (host.contains("facebook.com")) {
                Utils.showProgressDialog(activity);
                new callGetFacebookData().execute(binding.etText.getText().toString());
            } else {
                Utils.setToast(activity, "Enter Valid Url");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void PasteText() {
        try {
            binding.etText.setText("");
            String CopyIntent = getIntent().getStringExtra("CopyIntent");
            if (CopyIntent.equals("")) {
                if (!(clipBoard.hasPrimaryClip())) {

                } else if (!(clipBoard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {
                    if (clipBoard.getPrimaryClip().getItemAt(0).getText().toString().contains("facebook.com")) {
                        binding.etText.setText(clipBoard.getPrimaryClip().getItemAt(0).getText().toString());
                    }

                } else {
                    ClipData.Item item = clipBoard.getPrimaryClip().getItemAt(0);
                    if (item.getText().toString().contains("facebook.com")) {
                        binding.etText.setText(item.getText().toString());
                    }

                }
            }else {
                if (CopyIntent.contains("facebook.com")) {
                    binding.etText.setText(CopyIntent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class callGetFacebookData extends AsyncTask<String, Void, Document> {
        Document facebookDoc;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Document doInBackground(String... urls) {
            try {
                facebookDoc = Jsoup.connect(urls[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "doInBackground: Error");
            }
            return facebookDoc;
        }

        protected void onPostExecute(Document result) {
            Utils.hideProgressDialog(activity);
            try {

                VideoUrl = result.select("meta[property=\"og:video\"]").last().attr("content");
                Log.e("onPostExecute: ", VideoUrl);
                if (!VideoUrl.equals("")) {
                    try {
                        startDownload(VideoUrl, RootDirectoryFacebook, activity, getFilenameFromURL(VideoUrl));
                        VideoUrl = "";
                        binding.etText.setText("");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    public String getFilenameFromURL(String url) {
        try {
            return new File(new URL(url).getPath()).getName()+".mp4";
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return System.currentTimeMillis() + ".mp4";
        }
    }
}