package tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.activity;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.R;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.databinding.ActivityWebviewBinding;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util.AdsUtils;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util.ObservableWebView;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util.Settings;


public class WebviewAcitivity extends AppCompatActivity {

    ActivityWebviewBinding binding;
    String IntentURL, IntentTitle="";
    private Settings settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_webview);
        IntentURL = getIntent().getStringExtra("URL");
        IntentTitle = getIntent().getStringExtra("Title");
        binding.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        settings = new Settings(this);
        if (!settings.getSubscriptionState()) {
            //check if user enable the in-app subscribed
            AdsUtils.showGoogleBannerAd(WebviewAcitivity.this, binding.adView);
        }
        binding.TVTitle.setText(IntentTitle);
        LoadPage(IntentURL);
        binding.swipeRefreshLayout.setEnabled(false);

//        binding.swipeRefreshLayout.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        binding.swipeRefreshLayout.setRefreshing(true);
//                                        LoadPage(IntentURL);
//                                    }
//                                }
//        );

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadPage(IntentURL);

            }
        });

//        binding.webView1.setOnScrollChangedCallback(new ObservableWebView.OnScrollChangedCallback() {
//
//            @Override
//            public void onScroll(int l, int t, int oldl, int oldt) {
////                int tek = (int) Math.floor(binding.webView1.getContentHeight() * binding.webView1.getScale());
////                if(tek - binding.webView1.getScrollY() == binding.webView1.getHeight())
//
//                Log.e("WEB_LOG: ","WEB_LOG l :"+l+" "+"WEB_LOG t:"+t);
////                    Toast.makeText(WebviewAcitivity.this, "End", Toast.LENGTH_SHORT).show();
//            }
//        });

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            binding.webView1.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//                @Override
//                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//
//                        Toast.makeText(WebviewAcitivity.this, "Scrolled", Toast.LENGTH_SHORT).show();
//
//
//                }
//            });
//        }


    }

    public void LoadPage(String Url){
        binding.webView1.setWebViewClient(new MyBrowser());
        binding.webView1.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    binding.swipeRefreshLayout.setRefreshing(false);
                } else {
                    binding.swipeRefreshLayout.setRefreshing(true);
                }
            }
        });
        binding.webView1.getSettings().setLoadsImagesAutomatically(true);
        binding.webView1.getSettings().setJavaScriptEnabled(true);
        binding.webView1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        binding.webView1.loadUrl(Url);
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            Log.e("URL","URL : "+url);
            return true;
        }

//        @Override
//        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            super.onPageStarted(view, url, favicon);
//            binding.tvNoInternet.setVisibility(View.GONE);
//            binding.webView1.setVisibility(View.GONE);
//        }
//
//        @Override
//        public void onPageFinished(WebView view, String url) {
//            super.onPageFinished(view, url);
//            binding.tvNoInternet.setVisibility(View.GONE);
//            binding.webView1.setVisibility(View.VISIBLE);
//        }

//        @Override
//        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//            Log.e("URL","request : "+request+ " "+error);
//            super.onReceivedError(view, request, error);
//            binding.webView1.setVisibility(View.GONE);
//            binding.tvNoInternet.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
//            Log.e("URL","request : "+request+ " "+errorResponse);
//            super.onReceivedHttpError(view, request, errorResponse);
//            binding.webView1.setVisibility(View.GONE);
//            binding.tvNoInternet.setVisibility(View.VISIBLE);
//
//
//        }
    }

}
