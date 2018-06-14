package com.socialrehab.android.module.accessibility;

import android.content.Context;

import com.kepler.projectsupportlib.Logger;
import com.socialrehab.android.support.SharedPref;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TaskHandler {
    /*packages*/
    public static final String EXTRA_CLASS_NAME = "class_name";


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
    private static SharedPref sharedPref;

    private TaskHandler() {
    }


    public static void handle(Context context, String package_name, String extra_text, String class_name) {
        try {
            sharedPref = SharedPref.getInstance(context);
            Logger.e("package_name : " + package_name
                    + ", extra_text : " + extra_text
                    + ", class_name : " + class_name);
            String startTime = null;
            String endTime = null;
            switch (package_name) {
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
                    if (extra_text.equalsIgnoreCase("Notification shade."))
                        break;
                    if (last_package && class_name.equals("com.android.systemui.recents.RecentsActivity"))
                        OverLayService.startSOSService(context);
                    else {
                        OverLayService.stopSOSService(context);
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
                    if (extra_text != null && !extra_text.isEmpty()) {
                        if (!(extra_text.toLowerCase().contains("home screen") ||
                                extra_text.toLowerCase().contains("apps") ||
                                extra_text.toLowerCase().contains("folder closed"))) {
                            last_package = false;
                        }
                        if (!extra_text.toLowerCase().contains("folder closed"))
                            OverLayService.stopSOSService(context);
                    } else {
                        last_package = false;
                        OverLayService.stopSOSService(context);
                    }
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
                OverLayService.startSOSService(context);
            } else {
                OverLayService.stopSOSService(context);
            }
        } catch (ParseException e) {
            Logger.print(e);
        }

    }
}
