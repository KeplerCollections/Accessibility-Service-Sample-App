package com.socialrehab.android.support;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPref {

    private static final String DEFAULT_START_TIME = "10:0";
    private static final String DEFAULT_END_TIME = "19:0";
    private static SharedPreferences sharedPreferences;

    private SharedPref() {

    }

    public static SharedPref getInstance(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return new SharedPref();
    }

    /*whats app*/

    public boolean isWhatsAppEnabled() {
        return sharedPreferences.getBoolean("pref_whatsapp_enabled", false);
    }

    public String whatsAppStartTime() {
        return sharedPreferences.getString("pref_whatsapp_start_time", DEFAULT_START_TIME);
    }

    public String whatsAppEndTime() {
        return sharedPreferences.getString("pref_whatsapp_end_time", DEFAULT_END_TIME);
    }

    /*facebook*/

    public boolean isFacebookEnabled() {
        return sharedPreferences.getBoolean("pref_facebook_enabled", false);
    }

    public String facebookStartTime() {
        return sharedPreferences.getString("pref_facebook_start_time", DEFAULT_START_TIME);
    }

    public String facebookEndTime() {
        return sharedPreferences.getString("pref_facebook_end_time", DEFAULT_END_TIME);
    }

    /*instagram*/

    public boolean isInstagramEnabled() {
        return sharedPreferences.getBoolean("pref_instagram_enabled", false);
    }

    public String instagramStartTime() {
        return sharedPreferences.getString("pref_instagram_start_time", DEFAULT_START_TIME);
    }

    public String instagramEndTime() {
        return sharedPreferences.getString("pref_instagram_end_time", DEFAULT_END_TIME);
    }

    /*twitter*/

    public boolean isTwitterEnabled() {
        return sharedPreferences.getBoolean("pref_twitter_enabled", false);
    }

    public String twitterStartTime() {
        return sharedPreferences.getString("pref_twitter_start_time", DEFAULT_START_TIME);
    }

    public String twitterEndTime() {
        return sharedPreferences.getString("pref_twitter_end_time", DEFAULT_END_TIME);
    }

    /*facebook*/

    public boolean isYoutubeEnabled() {
        return sharedPreferences.getBoolean("pref_youtube_enabled", false);
    }

    public String youtubeStartTime() {
        return sharedPreferences.getString("pref_youtube_start_time", DEFAULT_START_TIME);
    }

    public String youtubeEndTime() {
        return sharedPreferences.getString("pref_youtube_end_time", DEFAULT_END_TIME);
    }

    // show case
    public boolean isShowCaseShowed() {
        return sharedPreferences.getBoolean("pref_show_case", false);
    }

    public void setShowCaseShowed() {
        sharedPreferences.edit().putBoolean("pref_show_case", true).apply();
    }

    /*password*/
    public boolean isPasswordEnabled() {
        return sharedPreferences.getBoolean("pref_password_enable", false);
    }

    public String getPassword() {
        return sharedPreferences.getString("pref_password", "1234");
    }

    public String getEmail() {
        return sharedPreferences.getString("pref_email", null);
    }

    public void savePE(String password, String emailId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("pref_email", emailId);
        editor.putString("pref_password", password);
        editor.apply();
    }

}
