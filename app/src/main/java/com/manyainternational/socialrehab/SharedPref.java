package com.manyainternational.socialrehab;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPref {

    private static final String DEFAULT_START_TIME = "10:0";
    private static final String DEFAULT_END_TIME = "18:0";
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

    public boolean isWhatsAppEnabled(){
        return sharedPreferences.getBoolean("pref_whatsapp_enabled",false);
    }

    public String whatsAppStartTime(){
        return sharedPreferences.getString("pref_whatsapp_start_time",DEFAULT_START_TIME);
    }

    public String whatsAppEndTime(){
        return sharedPreferences.getString("pref_whatsapp_end_time",DEFAULT_END_TIME);
    }

    /*facebook*/

    public boolean isFacebookEnabled(){
        return sharedPreferences.getBoolean("pref_facebook_enabled",false);
    }

    public String facebookStartTime(){
        return sharedPreferences.getString("pref_facebook_start_time",DEFAULT_START_TIME);
    }

    public String facebookEndTime(){
        return sharedPreferences.getString("pref_facebook_end_time",DEFAULT_END_TIME);
    }

    /*instagram*/

    public boolean isInstagramEnabled(){
        return sharedPreferences.getBoolean("pref_instagram_enabled",false);
    }

    public String instagramStartTime(){
        return sharedPreferences.getString("pref_instagram_start_time",DEFAULT_START_TIME);
    }

    public String instagramEndTime(){
        return sharedPreferences.getString("pref_instagram_end_time",DEFAULT_END_TIME);
    }

    // show case
    public boolean isShowCaseShowed(){
        return sharedPreferences.getBoolean("pref_show_case",false);
    }

    public void setShowCaseShowed(){
        sharedPreferences.edit().putBoolean("pref_show_case",true).apply();
    }


}
