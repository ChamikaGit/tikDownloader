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

}

