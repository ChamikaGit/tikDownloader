package tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.UUID;

public class Settings {
    private static final String PREF_NAME = "tikdownloader.tiktokvideodownloader.tiktokvideonowatermark.util";
    private Context context;

    public Settings(Context context) {
        this.context = context;
    }

    private SharedPreferences getPref() {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public String getDeviceId() {
        String value = getPref().getString("DeviceId", "");
        if (value.length() == 0) {
            getPref().edit().putString("DeviceId", UUID.randomUUID().toString()).commit();
            value = getPref().getString("DeviceId", "");
        }
        return value;
    }

    public void setAccessToken(String value) {
        getPref().edit().putString("AccessToken", value).commit();
    }

    public String getAccessToken() {
        String value = getPref().getString("AccessToken", "");
        return value;
    }

    public void setBaseUrl(String url) {
        getPref().edit().putString("BaseUrl", url).commit();
    }

    public String getBaseUrl() {
        String value = getPref().getString("BaseUrl", "");
        return value;
    }

    public void setReviewCount(int count) {
        getPref().edit().putInt("ReviewCount", count).commit();
    }

    public int getReviewCount() {
        int value = getPref().getInt("ReviewCount", 0);
        return value;
    }

    public void setLocationState(boolean value) {
        getPref().edit().putBoolean("LocationState", value).commit();
    }

    public boolean getLocationState() {
        boolean value = getPref().getBoolean("LocationState", true);
        return value;
    }

    public void setAdShowCount(int count) {
        getPref().edit().putInt("AdShowCount", count).commit();
    }

    public int getAdShowCount() {
        int value = getPref().getInt("AdShowCount", 0);
        return value;
    }

    public void setAdShowCountGallery(int count) {
        getPref().edit().putInt("AdShowCountGallery", count).commit();
    }

    public int getAdShowCountGallery() {
        int value = getPref().getInt("AdShowCountGallery", 0);
        return value;
    }

    public void setSubscriptionState(boolean value) {
        getPref().edit().putBoolean("SubscriptionState", value).commit();
    }

    public boolean getSubscriptionState() {
        boolean value = getPref().getBoolean("SubscriptionState", false);
        return value;
    }

    public void setCurrentSubscribedItem(String value) {
        getPref().edit().putString("currentSubscribedItem", value).commit();
    }

    public String getCurrentSubscribedItem() {
        String value = getPref().getString("currentSubscribedItem", "");
        return value;
    }


    public void setSubscribedItemWeeklyPrice(String value) {
        getPref().edit().putString("subscribedItemWeeklyPrice", value).commit();
    }

    public String getSubscribedItemWeeklyPrice() {
        String value = getPref().getString("subscribedItemWeeklyPrice", "");
        return value;
    }

    public void setSubscribedItemMonthlyPrice(String value) {
        getPref().edit().putString("subscribedItemMonthlyPrice", value).commit();
    }

    public String getSubscribedItemMonthlyPrice() {
        String value = getPref().getString("subscribedItemMonthlyPrice", "");
        return value;
    }

    public void setSubscriptionPopUpCount(int count) {
        getPref().edit().putInt("subscriptionPopUpCount", count).commit();
    }

    public int getSubscriptionPopUpCount() {
        int value = getPref().getInt("subscriptionPopUpCount", 5);
        return value;
    }

    public void setDisclaimerAgree(boolean value) {
        getPref().edit().putBoolean("DisclaimerAgree", value).commit();
    }

    public boolean getDisclaimerAgree() {
        boolean value = getPref().getBoolean("DisclaimerAgree", false);
        return value;
    }

}

