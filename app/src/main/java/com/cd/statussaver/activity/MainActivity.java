package com.cd.statussaver.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.cd.statussaver.R;
import com.cd.statussaver.databinding.ActivityMainBinding;
import com.cd.statussaver.util.ClipboardListener;
import com.cd.statussaver.util.Utils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.cd.statussaver.util.Utils.createFileFolder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    MainActivity activity;
    ActivityMainBinding binding;
    boolean doubleBackToExitPressedOnce = false;
    private ClipboardManager clipBoard;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    String CopyKey = "";
    String CopyValue = "";


    InterstitialAd interstitial;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        activity = this;

        initViews();
    }
    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
        assert activity != null;
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
    }
    public void initViews() {
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
        if (activity.getIntent().getExtras() != null) {
            for (String key : activity.getIntent().getExtras().keySet()) {
                CopyKey = key;
                String value = activity.getIntent().getExtras().getString(CopyKey);
                if (CopyKey.equals("android.intent.extra.TEXT")) {
                    CopyValue = activity.getIntent().getExtras().getString(CopyKey);
                    callText(value);
                } else {
                    CopyValue="";
                    callText(value);
                }
            }
        }
        if (clipBoard != null) {
            clipBoard.addPrimaryClipChangedListener(new ClipboardListener() {
                @Override
                public void onPrimaryClipChanged() {
                    try {
                        showNotification(Objects.requireNonNull(clipBoard.getPrimaryClip().getItemAt(0).getText()).toString());
                    } catch (
                            Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        if (Build.VERSION.SDK_INT >= 23) {
            checkPermissions(0);
        }

        binding.rvInsta.setOnClickListener(this);
        binding.rvWhatsApp.setOnClickListener(this);
        binding.rvTikTok.setOnClickListener(this);
        binding.rvFB.setOnClickListener(this);
        binding.rvTwitter.setOnClickListener(this);
        binding.rvGallery.setOnClickListener(this);
        binding.rvAbout.setOnClickListener(this);
        binding.rvShareApp.setOnClickListener(this);
        binding.rvRateApp.setOnClickListener(this);
        binding.rvMoreApp.setOnClickListener(this);

        createFileFolder();

    }

    private void callText(String CopiedText) {
        try {
            if (CopiedText.contains("instagram.com")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(101);
                } else {
                    callInstaActivity();
                }
            } else if (CopiedText.contains("facebook.com")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(104);
                } else {
                    callFacebookActivity();
                }
            } else if (CopiedText.contains("tiktok.com")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(103);
                } else {
                    callTikTokActivity();
                }
            }else if (CopiedText.contains("twitter.com")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(106);
                } else {
                    callTwitterActivity();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        Intent i = null;

        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(getString(R.string.admob_interstitial_ad));
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitial.loadAd(adRequest);


        switch (v.getId()) {
            case R.id.rvInsta:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(101);
                } else {
                    callInstaActivity();
                }
                break;
            case R.id.rvWhatsApp:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(102);
                } else {
                    callWhatsappActivity();
                }
                break;
            case R.id.rvTikTok:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(103);
                } else {
                    callTikTokActivity();
                }
                break;
            case R.id.rvFB:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(104);
                } else {
                    callFacebookActivity();
                }
                break;
            case R.id.rvGallery:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(105);
                } else {
                    callGalleryActivity();
                }
                break;
            case R.id.rvTwitter:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(106);
                } else {
                    callTwitterActivity();
                }
                break;
            case R.id.rvAbout:
                i = new Intent(activity, AboutUsActivity.class);
                startActivity(i);
                break;
            case R.id.rvShareApp:
                Utils.ShareApp(activity);
                break;
            case R.id.rvRateApp:
                Utils.RateApp(activity);
                break;
            case R.id.rvMoreApp:
                Utils.MoreApp(activity);
                break;

        }
    }

    public void callInstaActivity() {
        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Intent i = new Intent(activity, InstagramActivity.class);
                i.putExtra("CopyIntent", CopyValue);
                startActivity(i);
            }


            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                Intent i = new Intent(activity, InstagramActivity.class);
                i.putExtra("CopyIntent", CopyValue);
                startActivity(i);
            }


            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                interstitial.show();
            }
        });

    }

    public void callWhatsappActivity() {


        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();

                Intent i = new Intent(activity, WhatsappActivity.class);
                startActivity(i);
            }


            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);

                Intent i = new Intent(activity, WhatsappActivity.class);
                startActivity(i);
            }


            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                interstitial.show();
            }
        });


    }

    public void callTikTokActivity() {

        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();

                Intent i = new Intent(activity, TikTokActivity.class);
                i.putExtra("CopyIntent",CopyValue);
                startActivity(i);
            }


            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);

                Intent i = new Intent(activity, TikTokActivity.class);
                i.putExtra("CopyIntent",CopyValue);
                startActivity(i);
            }


            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                interstitial.show();
            }
        });



    }

    public void callFacebookActivity() {

        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();


                Intent i = new Intent(activity, FacebookActivity.class);
                i.putExtra("CopyIntent",CopyValue);
                startActivity(i);
            }


            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);


                Intent i = new Intent(activity, FacebookActivity.class);
                i.putExtra("CopyIntent",CopyValue);
                startActivity(i);
            }


            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                interstitial.show();
            }
        });


    }
    public void callTwitterActivity() {

        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();

                Intent i = new Intent(activity, TwitterActivity.class);
                i.putExtra("CopyIntent",CopyValue);
                startActivity(i);
            }


            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);


                Intent i = new Intent(activity, TwitterActivity.class);
                i.putExtra("CopyIntent",CopyValue);
                startActivity(i);
            }


            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                interstitial.show();
            }
        });


    }


    public void callGalleryActivity() {


        Intent i = new Intent(activity, GalleryActivity.class);
        startActivity(i);
    }

    public void showNotification(String Text) {
        if (Text.contains("instagram.com")||Text.contains("facebook.com")||Text.contains("tiktok.com")||Text.contains("twitter.com")) {
            Intent intent = new Intent(activity, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("Notification", Text);
            PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(getResources().getString(R.string.app_name),
                        getResources().getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
                mChannel.enableLights(true);
                mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                notificationManager.createNotificationChannel(mChannel);
            }
            NotificationCompat.Builder notificationBuilder;
            notificationBuilder = new NotificationCompat.Builder(activity, getResources().getString(R.string.app_name))
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setColor(getResources().getColor(R.color.black))
                    .setLargeIcon(BitmapFactory.decodeResource(activity.getResources(),
                            R.mipmap.ic_launcher_round))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentTitle("Copied text")
                    .setContentText(Text)
                    .setChannelId(getResources().getString(R.string.app_name))
                    .setFullScreenIntent(pendingIntent, true);
            notificationManager.notify(1, notificationBuilder.build());
        }
    }

    private boolean checkPermissions(int type) {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(activity, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) (activity),
                    listPermissionsNeeded.toArray(new
                            String[listPermissionsNeeded.size()]), type);
            return false;
        } else {
            if (type == 101) {
                callInstaActivity();
            } else if (type == 102) {
                callWhatsappActivity();
            } else if (type == 103) {
                callTikTokActivity();
            } else if (type == 104) {
                callFacebookActivity();
            } else if (type == 105) {
                callGalleryActivity();
            } else if (type == 106) {
                callTwitterActivity();
            }

        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callInstaActivity();
            } else {
            }
            return;
        } else if (requestCode == 102) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callWhatsappActivity();
            } else {
            }
            return;
        } else if (requestCode == 103) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callTikTokActivity();
            } else {
            }
            return;
        } else if (requestCode == 104) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callFacebookActivity();
            } else {
            }
            return;
        } else if (requestCode == 105) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callGalleryActivity();
            } else {
            }
            return;
        }
         else if (requestCode == 106) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callTwitterActivity();
            } else {
            }
            return;
        }

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        this.doubleBackToExitPressedOnce = true;
        Utils.setToast(activity, "Please click BACK again to exit");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }

}
