package tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.google.ads.mediation.facebook.FacebookAdapter;
import com.google.ads.mediation.facebook.FacebookExtras;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnFailureListener;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;

import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.MyApplication;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.R;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.databinding.ActivityMainBinding;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.dialogFragment.ExitDialogFragment;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.dialogFragment.HowToUseDialogFragment;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util.AdsUtils;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util.ClipboardListener;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util.Settings;
import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util.Utils.createFileFolder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ExitDialogFragment.OnItemClickListener {
    private MyApplication myApplication;
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
    private ExitDialogFragment exitDialogFragment;
    private Settings settings;
    private int reviewCount = 0;
    private AppUpdateManager appUpdateManager;
    private int RequestUpdate = 1;
    private ReviewInfo reviewInfo;
    private ReviewManager manager;
    private final int REQUEST_CODE_TIKTOK = 52;
    private NativeAd unifiedNativeAdObj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        myApplication = (MyApplication) getApplication();
        activity = this;

        AdsUtils.showGoogleBannerAd(MainActivity.this, binding.adView);
//        MediationTestSuite.launch(MainActivity.this);

        initViews();
        checkUpdate();
        settings = new Settings(this);
        reviewCount = settings.getReviewCount();
        reviewCount++;
        settings.setReviewCount(reviewCount);
        if (settings.getReviewCount() > 2) {
            intiReview();
        }
    }

    private void loadNativeAd() {

//        AdLoader.Builder builder =  new AdLoader.Builder(MainActivity.this,getString(R.string.admob_native_ad));
//
//        //for facebook
//        Bundle extras = new FacebookExtras().setNativeBanner(true).build();
//
//        builder.withAdListener(new AdListener(){
//
//            @Override
//            public void onAdFailedToLoad(LoadAdError loadAdError) {
//                super.onAdFailedToLoad(loadAdError);
//            }
//        });
//
//        builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
//            @Override
//            public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
//                if (isDestroyed()) {
//                    nativeAd.destroy();
//                    Log.d("TAG", "Native Ad Destroyed");
//                    return;
//                }
//                unifiedNativeAdObj = nativeAd;
//            }
//        });
//
////        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
////            @Override
////            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
////
////                unifiedNativeAdObj = unifiedNativeAd;
////            }
////        });
//        builder.withNativeAdOptions(new NativeAdOptions.Builder().build());
//
//        AdLoader adLoader = builder.build();
//        adLoader.loadAd(new AdRequest.Builder()
//                .addNetworkExtrasBundle(FacebookAdapter.class, extras)
//                .build());

        Bundle extras = new FacebookExtras().setNativeBanner(true).build();

        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(false)
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        AdLoader adLoader = new AdLoader.Builder(MainActivity.this, getString(R.string.admob_native_ad))
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                        if (isDestroyed()) {
                            nativeAd.destroy();
                            Log.d("TAG", "Native Ad Destroyed");
                            return;
                        }
                        if (nativeAd.getMediaContent().hasVideoContent()) {
                            float mediaAspectRatio = nativeAd.getMediaContent().getAspectRatio();
                            float duration = nativeAd.getMediaContent().getDuration();

                            nativeAd.getMediaContent().getVideoController().setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                                @Override
                                public void onVideoStart() {
                                    super.onVideoStart();
                                }

                                @Override
                                public void onVideoPlay() {
                                    super.onVideoPlay();
                                }

                                @Override
                                public void onVideoPause() {
                                    super.onVideoPause();
                                }

                                @Override
                                public void onVideoEnd() {
                                    super.onVideoEnd();
                                }

                                @Override
                                public void onVideoMute(boolean b) {
                                    super.onVideoMute(b);
                                }
                            });
                        }

                        unifiedNativeAdObj = nativeAd;
                    }
                })

                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);

                        new CountDownTimer(10000, 1000) {

                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                                loadNativeAd();
                            }
                        }.start();
                    }
                })
//                .withNativeAdOptions(new NativeAdOptions.Builder().build())
                .withNativeAdOptions(adOptions)
                .build();

        adLoader.loadAd(new AdRequest.Builder()
                .addNetworkExtrasBundle(FacebookAdapter.class, extras)
                .build());
    }

    private void checkUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(this);
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {
                if ((result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE)
                        && result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(result, AppUpdateType.IMMEDIATE, MainActivity.this, RequestUpdate);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void intiReview() {
        manager = ReviewManagerFactory.create(MainActivity.this);
        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                reviewInfo = task.getResult();
            } else {
                // There was some problem, continue regardless of the result.
                reviewInfo = null;
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
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {
                if (result.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(result, AppUpdateType.IMMEDIATE, MainActivity.this, RequestUpdate);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        loadNativeAd();
        exitDialogFragment = new ExitDialogFragment(this, unifiedNativeAdObj);
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
                    CopyValue = "";
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

        binding.rvTikTok.setOnClickListener(this);
        binding.rvGallery.setOnClickListener(this);
        binding.rvAbout.setOnClickListener(this);
        binding.rvShareApp.setOnClickListener(this);
        binding.rvRateApp.setOnClickListener(this);
        binding.rvMoreApp.setOnClickListener(this);

        createFileFolder();

    }

    private void callText(String CopiedText) {
        try {
            if (CopiedText.contains("tiktok.com")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(103);
                } else {
                    callTikTokActivity();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        Intent i = null;

        switch (v.getId()) {

            case R.id.rvTikTok:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(103);
                } else {
                    callTikTokActivity();
                }
                getMainApp().trackFireBaseEvent("TIKTOK_BUTTON", "CLICK", "TRUE");
                break;
            case R.id.rvGallery:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(105);
                } else {
                    callGalleryActivity();
                }
                getMainApp().trackFireBaseEvent("GALLERY_BUTTON", "CLICK", "TRUE");
                break;

            case R.id.rvAbout:
//                i = new Intent(activity, AboutUsActivity.class);
//                startActivity(i);
                HowToUseDialogFragment howToUseDialogFragment = new HowToUseDialogFragment();
                howToUseDialogFragment.show(getSupportFragmentManager(), "HowToUseDialogFragment");
                getMainApp().trackFireBaseEvent("ABOUT_BUTTON", "CLICK", "TRUE");

                break;
            case R.id.rvShareApp:
                Utils.ShareApp(activity);
                getMainApp().trackFireBaseEvent("SHARE_BUTTON", "CLICK", "TRUE");
                break;
            case R.id.rvRateApp:
                Utils.RateApp(activity);
                getMainApp().trackFireBaseEvent("RATE_BUTTON", "CLICK", "TRUE");
                break;
            case R.id.rvMoreApp:
                Utils.MoreApp(activity);
                getMainApp().trackFireBaseEvent("MORE_BUTTON", "CLICK", "TRUE");
                break;

        }
    }


    public void callTikTokActivity() {

        Intent i = new Intent(activity, TikTokNewActivity.class);
        i.putExtra("CopyIntent", CopyValue);
        startActivityForResult(i, REQUEST_CODE_TIKTOK);


    }

    public void callGalleryActivity() {


        Intent i = new Intent(activity, GalleryActivity.class);
        startActivity(i);
    }

    public void showNotification(String Text) {
//        if (Text.contains("instagram.com") || Text.contains("facebook.com") || Text.contains("tiktok.com") || Text.contains("twitter.com")) {
        if (Text.contains("tiktok.com")) {
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
                    .setColor(getResources().getColor(R.color.white))
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
            if (type == 103) {
                callTikTokActivity();
            } else if (type == 105) {
                callGalleryActivity();
            }

        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        if (requestCode == 103) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callTikTokActivity();
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

    }

    @Override
    public void onBackPressed() {
        this.doubleBackToExitPressedOnce = true;
        if (doubleBackToExitPressedOnce) {
            exitDialogFragment.show(getSupportFragmentManager(), "ExitDialogFragment");
        }
        //Utils.setToast(activity, "Please click BACK again to exit");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }

    @Override
    public void onYesClick(Dialog dialog) {
        dialog.dismiss();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_CODE_TIKTOK) {
            if (resultCode == Activity.RESULT_OK) {
                if (reviewInfo != null) {
                    Utils.setToast(MainActivity.this, "Give a best rate to us!");
                    Task<Void> flow = manager.launchReviewFlow(activity, reviewInfo);
                    flow.addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void result) {

                        }
                    });
                    flow.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                        }
                    });
                }
            }
        }
    }
}
