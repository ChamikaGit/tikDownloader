package tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;


import tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.R;

public class FullScreenAdManagerAdMob {

//    public static void initFullScreenAds(android.content.Context context) {
//        Utils utils = new Utils(context);
//        if (utils.isNetworkAvailable()) {
//            if (interstitialAd == null) {
//                interstitialAd = new InterstitialAd(context);
//                interstitialAd.setAdUnitId(context.getString(R.string.admob_interstitial_ad));
//            }
//            loadFullScreenAd(context);
//        }
//    }
//
//    public static InterstitialAd interstitialAd;
//
//    public static void loadFullScreenAd(android.content.Context context) {
//        Utils utils = new Utils(context);
//        if (interstitialAd != null && !interstitialAd.isLoaded() && utils.isNetworkAvailable()) {
//            interstitialAd.loadAd(new AdRequest.Builder()
//                    .build());
//        }
//    }
//
//    public static void fullScreenAdsCheckPref(final android.content.Context context, final GetBackPointer getBackPointer) {
//
//        int getCount = new Settings(context).getAdShowCount();
//        int newCount = getCount + 1;
//        new Settings(context).setAdShowCount(newCount);
////        int getCount = SharedPreferencesHelper.getInstance().getInt(all_prefs.prefName, 0);
////        int newCount = getCount + 1;
////        SharedPreferencesHelper.getInstance().setInt(all_prefs.prefName, newCount);
////        if (getCount != 0 && getCount % all_prefs.value == 0 && interstitialAd != null && interstitialAd.isLoaded()) {
//        if (getCount != 0 && getCount % 2 == 0) {
//            if (interstitialAd != null && interstitialAd.isLoaded()) {
//                interstitialAd.show();
//                interstitialAd.setAdListener(new AdListener() {
//                    @Override
//                    public void onAdClosed() {
//                        loadFullScreenAd(context);
//                        if (getBackPointer != null)
//                            getBackPointer.returnAction();
//                        super.onAdClosed();
//                    }
//                });
//            } else if (getBackPointer != null)
//                getBackPointer.returnAction();
//        } else {
//            if (getBackPointer != null)
//                getBackPointer.returnAction();
//        }
//    }
//
//
//    public interface GetBackPointer {
//        public void returnAction();
//    }


}
