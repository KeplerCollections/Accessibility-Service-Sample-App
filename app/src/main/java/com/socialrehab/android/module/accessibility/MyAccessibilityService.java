package com.socialrehab.android.module.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.view.accessibility.AccessibilityEvent;

import com.kepler.projectsupportlib.Logger;
import com.socialrehab.android.support.SharedPref;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by amit on 23/1/18.
 */

public class MyAccessibilityService extends AccessibilityService {
    /*packages*/
    public static final String PACKAGE_WHATS_APP = "com.whatsapp";
    public static final String PACKAGE_FB_KATANA = "com.facebook.katana";
    public static final String PACKAGE_IG = "com.instagram.android";
    public static final String PACKAGE_TWITTER = "com.twitter.android";
    public static final String PACKAGE_YOUTUBE = "com.google.android.youtube";
    public static final int PACKAGE_WHATS_APP_INT = 0;
    public static final int PACKAGE_FB_KATANA_INT = 1;
    public static final int PACKAGE_IG_INT = 2;
    public static final int PACKAGE_TWITTER_INT = 3;
    public static final int PACKAGE_YOUTUBE_INT = 4;
    /**/
    public static final List<String> PACKAGE_NAMES = new ArrayList<String>() {{
        add(PACKAGE_FB_KATANA);
        add(PACKAGE_IG);
        add(PACKAGE_WHATS_APP);
        add(PACKAGE_YOUTUBE);
        add(PACKAGE_TWITTER);
    }};
    public static boolean last_package = false;
    private SharedPref sharedPref;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPref = SharedPref.getInstance(getApplicationContext());
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        Logger.e(accessibilityEvent.toString());
        try {
            String startTime = null;
            String endTime = null;
            switch (String.valueOf(accessibilityEvent.getPackageName())) {
                case PACKAGE_FB_KATANA:
                    if (sharedPref.isFacebookEnabled()) {
                        startTime = sharedPref.facebookStartTime();
                        endTime = sharedPref.facebookEndTime();
                    }
                    break;
                case PACKAGE_IG:
                    if (sharedPref.isInstagramEnabled()) {
                        startTime = sharedPref.instagramStartTime();
                        endTime = sharedPref.instagramEndTime();
                    }
                    break;
                case PACKAGE_WHATS_APP:
                    if (sharedPref.isWhatsAppEnabled()) {
                        startTime = sharedPref.whatsAppStartTime();
                        endTime = sharedPref.whatsAppEndTime();
                    }
                    break;
                case PACKAGE_TWITTER:
                    if (sharedPref.isTwitterEnabled()) {
                        startTime = sharedPref.twitterStartTime();
                        endTime = sharedPref.twitterEndTime();
                    }
                    break;
                case PACKAGE_YOUTUBE:
                    if (sharedPref.isYoutubeEnabled()) {
                        startTime = sharedPref.youtubeStartTime();
                        endTime = sharedPref.youtubeEndTime();
                    }
                    break;
                case "com.socialrehab.android":
//                    last_package = false;
                    break;
                case "com.android.systemui":
                    if (accessibilityEvent.getText().size() > 0 && accessibilityEvent.getText().get(0).equals("Notification shade."))
                        break;
                    if (last_package && accessibilityEvent.getClassName().equals("com.android.systemui.recents.RecentsActivity"))
                        OverLayService.startSOSService(getApplicationContext());
                    else {
                        OverLayService.stopSOSService(getApplicationContext());
                        last_package = false;
                    }
                    break;
//                case "com.indiashopps.android":
//                    if(sharedPref.isInstagramEnabled()){
//                        startTime=sharedPref.instagramStartTime();
//                        endTime=sharedPref.instagramEndTime();
//                    }
//                    break;
                default:
                    if (accessibilityEvent.getText() == null ||
                            accessibilityEvent.getText().isEmpty() ||
                            !(accessibilityEvent.getText().get(0).toString().toLowerCase().contains("home screen") ||
                                    accessibilityEvent.getText().get(0).toString().toLowerCase().contains("apps"))) {
                        last_package = false;
                    }
                    OverLayService.stopSOSService(getApplicationContext());
            }
            if (startTime == null || endTime == null)
                return;
            SimpleDateFormat dateFormat = new SimpleDateFormat("kk:mm");
            Date dateStartTime = dateFormat.parse(startTime);
            Date dateEndTime = dateFormat.parse(endTime);
            Date currentTime = dateFormat.parse(dateFormat.format(Calendar.getInstance().getTime()));

            if (currentTime.after(dateStartTime) && currentTime.before(dateEndTime)) {
                //checkes whether the current time is between 14:49:00 and 20:11:13.
                last_package = true;
                OverLayService.startSOSService(getApplicationContext());
            } else {
                OverLayService.stopSOSService(getApplicationContext());
            }
        } catch (ParseException e) {
            Logger.print(e);
        }

    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {
        Logger.d("***** onServiceConnected");
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        info.notificationTimeout = 100;

        this.setServiceInfo(info);

    }
}
