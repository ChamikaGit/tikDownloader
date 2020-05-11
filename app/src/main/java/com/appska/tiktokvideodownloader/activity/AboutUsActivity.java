package com.appska.tiktokvideodownloader.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.appska.tiktokvideodownloader.util.AdsUtils;
import com.appska.tiktokvideodownloader.R;
import com.appska.tiktokvideodownloader.databinding.ActivityAboutUsBinding;
import static com.appska.tiktokvideodownloader.util.Utils.PrivacyPolicyUrl;

public class AboutUsActivity extends AppCompatActivity {

    ActivityAboutUsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about_us);
        AdsUtils.showGoogleBannerAd(AboutUsActivity.this,binding.adView);
        binding.RLPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  = new Intent(AboutUsActivity.this,WebviewAcitivity.class);
                i.putExtra("URL",PrivacyPolicyUrl);
                i.putExtra("Title","Privacy Policy");
                startActivity(i);
            }
        });
        binding.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
