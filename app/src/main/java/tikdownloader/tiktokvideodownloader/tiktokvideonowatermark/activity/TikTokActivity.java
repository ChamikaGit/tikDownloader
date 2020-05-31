package tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.MyApplication;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.R;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.databinding.ActivityTikTokBinding;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util.AdsUtils;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.api.CommonClassForAPI;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util.SharePrefs;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util.Utils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static android.content.ContentValues.TAG;

public class TikTokActivity extends AppCompatActivity {
    private ActivityTikTokBinding binding;
    TikTokActivity activity;
    CommonClassForAPI commonClassForAPI;
    private String VideoUrl;
    private ClipboardManager clipBoard;
    boolean IsWithWaternark = true;
    private MyApplication myApplication;

    private InterstitialAd mInterstitialAd;
    private ImageView img,imgPicture;
    private TextView tvName,tvDescription,tvKeywords,tvCommentCount,tvDownloadNow,tvPast;
    private ProgressBar progressBar;
    private RelativeLayout relDetailsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tik_tok);
        myApplication = (MyApplication) getApplication();
        img = findViewById(R.id.img);
        imgPicture = findViewById(R.id.imgPicture);
        tvName = findViewById(R.id.tvName);
        tvDescription = findViewById(R.id.tvDescription);
        tvKeywords = findViewById(R.id.tvKeywords);
        tvCommentCount = findViewById(R.id.tvCommentCount);
        tvDownloadNow = findViewById(R.id.tvDownloadNow);
        tvPast = findViewById(R.id.tvPast);
        relDetailsContainer = findViewById(R.id.relDetailsContainer);
        progressBar = findViewById(R.id.progressBar);
        activity = this;
        commonClassForAPI = CommonClassForAPI.getInstance(activity);
        Utils.createFileFolder();
        initViews();
        AdsUtils.showGoogleBannerAd(TikTokActivity.this, binding.adView);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.admob_interstitial_ad));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {

                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {

                GetTikTokData();
                // Code to be executed when the interstitial ad is closed.
            }
        });





    }

    protected synchronized MyApplication getMainApp() {
        if (myApplication == null) {
            myApplication = MyApplication.getInstance();
        }
        return myApplication;
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
        assert activity != null;
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
        PasteText();

        try {
            URL url = new URL(binding.etText.getText().toString());
            String host = url.getHost();
            if (host.contains("tiktok.com")) {
                //Utils.showProgressDialog(activity);
                progressBar.setVisibility(View.VISIBLE);
                img.setVisibility(View.VISIBLE);
                new callGetTikTokDefaultData().execute(binding.etText.getText().toString());
            } else {
//                Utils.setToast(activity, "Enter Valid Url");
                img.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

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

        binding.layoutHowTo.imHowto1.setImageResource(R.drawable.tt1);
        binding.layoutHowTo.imHowto2.setImageResource(R.drawable.tt2);
        binding.layoutHowTo.imHowto3.setImageResource(R.drawable.tt3);
        binding.layoutHowTo.imHowto4.setImageResource(R.drawable.tt4);
        binding.layoutHowTo.tvHowTo1.setText("1. Open TikTok");
        binding.layoutHowTo.tvHowTo3.setText("1. Open TikTok");
        if (!SharePrefs.getInstance(activity).getBoolean(SharePrefs.ISSHOWHOWTOTT)) {
            SharePrefs.getInstance(activity).putBoolean(SharePrefs.ISSHOWHOWTOTT, true);
            binding.layoutHowTo.LLHowToLayout.setVisibility(View.VISIBLE);
        } else {
            binding.layoutHowTo.LLHowToLayout.setVisibility(View.GONE);
        }


        binding.tvWithMark.setOnClickListener(v -> {
            IsWithWaternark = true;
            String LL = binding.etText.getText().toString();
            if (LL.equals("")) {
                Utils.setToast(activity, "Enter Url");
            } else if (!Patterns.WEB_URL.matcher(LL).matches()) {
                Utils.setToast(activity, "Enter Valid Url");
            } else {
                showInterstitial();
            }
            getMainApp().trackFireBaseEvent("WITH_WATERMARK","CLICK","TRUE");
        });

        binding.tvWithoutMark.setOnClickListener(v -> {
            IsWithWaternark = false;
            String LL = binding.etText.getText().toString();
            if (LL.equals("")) {
                Utils.setToast(activity, "Enter Url");
            } else if (!Patterns.WEB_URL.matcher(LL).matches()) {
                Utils.setToast(activity, "Enter Valid Url");
            } else {
                showInterstitial();
            }

            getMainApp().trackFireBaseEvent("WITHOUT_WATERMARK","CLICK","TRUE");
        });

        binding.LLOpenTikTok.setOnClickListener(v -> {
            Intent launchIntent = activity.getPackageManager().getLaunchIntentForPackage("com.zhiliaoapp.musically.go");
            Intent launchIntent1 = activity.getPackageManager().getLaunchIntentForPackage("com.zhiliaoapp.musically");
            if (launchIntent != null) {
                activity.startActivity(launchIntent);
            } else if (launchIntent1 != null) {
                activity.startActivity(launchIntent1);
            } else {
                Utils.setToast(activity, "App Not Available.");
            }

        });

        binding.tvPast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
                PasteText();
                if (!binding.etText.getText().toString().equals("")) {
                    try {
                        URL url = new URL(binding.etText.getText().toString());
                        String host = url.getHost();
                        if (host.contains("tiktok.com")) {
                            //Utils.showProgressDialog(activity);
                            progressBar.setVisibility(View.VISIBLE);
                            img.setVisibility(View.VISIBLE);
                            new callGetTikTokDefaultData().execute(binding.etText.getText().toString());
                        } else {
//                Utils.setToast(activity, "Enter Valid Url");
                            img.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void GetTikTokData() {
        try {
            Utils.createFileFolder();
            URL url = new URL(binding.etText.getText().toString());
            String host = url.getHost();
            if (host.contains("tiktok.com")) {
                Utils.showProgressDialog(activity);
                new callGetTikTokData().execute(binding.etText.getText().toString());
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
                    if (clipBoard.getPrimaryClip().getItemAt(0).getText().toString().contains("tiktok.com")) {
                        binding.etText.setText(clipBoard.getPrimaryClip().getItemAt(0).getText().toString());
                    }

                } else {
                    ClipData.Item item = clipBoard.getPrimaryClip().getItemAt(0);
                    if (item.getText().toString().contains("tiktok.com")) {
                        binding.etText.setText(item.getText().toString());
                    }

                }
            } else {
                if (CopyIntent.contains("tiktok.com")) {
                    binding.etText.setText(CopyIntent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class callGetTikTokDefaultData extends AsyncTask<String, Void, Document> {
        Document tikDoc;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Document doInBackground(String... urls) {
            try {
                tikDoc = Jsoup.connect(urls[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "doInBackground: Error");
            }
            return tikDoc;
        }

        protected void onPostExecute(Document result) {
            progressBar.setVisibility(View.GONE);
            relDetailsContainer.setVisibility(View.VISIBLE);
            tvDownloadNow.setVisibility(View.VISIBLE);
            try {
                String URL = result.select("script[id=\"videoObject\"]").last().html();
                String URL1 = result.select("script[id=\"__NEXT_DATA__\"]").last().html();

                if (!URL.equals("")) {
                    try {
                        JSONObject jsonObject = new JSONObject(URL);
                        new JSONObject(URL1);
                        Log.e("JSON_OBJECT",jsonObject.toString());
                        VideoUrl = jsonObject.getString("contentUrl");
                        JSONArray array = jsonObject.getJSONArray("thumbnailUrl");
                        String url = array.get(0).toString();
                        Glide.with(getApplicationContext()).load(url).into(img);
                        Glide.with(getApplicationContext()).load(url).into(imgPicture);
                        tvName.setText(jsonObject.getString("name"));
                        tvDescription.setText(jsonObject.getString("description"));
                        tvKeywords.setText(jsonObject.getString("keywords"));
                        tvCommentCount.setText(jsonObject.getString("commentCount"));
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


    class callGetTikTokData extends AsyncTask<String, Void, Document> {
        Document tikDoc;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Document doInBackground(String... urls) {
            try {
                tikDoc = Jsoup.connect(urls[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "doInBackground: Error");
            }
            return tikDoc;
        }

        protected void onPostExecute(Document result) {
            Utils.hideProgressDialog(activity);
            try {
                String URL = result.select("script[id=\"videoObject\"]").last().html();
                String URL1 = result.select("script[id=\"__NEXT_DATA__\"]").last().html();

                if (!URL.equals("")) {
                    try {
                        JSONObject jsonObject = new JSONObject(URL);
                        new JSONObject(URL1);
                        Log.e("JSON_OBJECT",jsonObject.toString());
                        VideoUrl = jsonObject.getString("contentUrl");
                        if (IsWithWaternark){
                            Utils.startDownload(VideoUrl, Utils.RootDirectoryTikTok, activity, "tiktok_"+System.currentTimeMillis() + ".mp4");
                        }else {
                            new GetDownloadLinkWithoutWatermark().execute();
                        }
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
            return new File(new URL(url).getPath()).getName() + ".mp4";
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return System.currentTimeMillis() + ".mp4";
        }
    }
    private class GetDownloadLinkWithoutWatermark extends AsyncTask<String, String, String> {
        String resp;

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {
            try {
                this.resp = withoutWatermark(VideoUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resp;
        }

        protected void onPostExecute(String url) {
            try {
                Utils.startDownload(url, Utils.RootDirectoryTikTok, activity, "tiktok_"+System.currentTimeMillis() + ".mp4");
                VideoUrl="";
                binding.etText.setText("");
            } catch (Exception e) {
                Utils.setToast(activity, "Error Occurred");
            }
        }
    }



    public String withoutWatermark(String url) {
        try {
            HttpURLConnection httpConn = (HttpURLConnection) new URL(url).openConnection();
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            int resCode = httpConn.getResponseCode();
            BufferedReader rd = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            StringBuffer result = new StringBuffer();
            String str;
            while (true) {
                str = rd.readLine();
                if (str != null) {
                    result.append(str);
                    if (result.toString().contains("vid:")) {
                        try {
                            String VideoIdString = result.substring(result.indexOf("vid:"));
                            String VID = VideoIdString.substring(0, 4);

                            if (VID.equals("vid:")) {
                                break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {

                }
            }
            String VideoId = result.substring(result.indexOf("vid:"));
            String FinalVID = VideoId.substring(4, VideoId.indexOf("%")).replaceAll("[^A-Za-z0-9]","").trim();
            String finalVideoUrl = "http://api2.musical.ly/aweme/v1/playwm/?video_id=" + FinalVID;
            return finalVideoUrl;

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private void showInterstitial() {
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {


            GetTikTokData();

        }
    }


}