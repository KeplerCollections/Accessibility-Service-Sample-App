package com.manyainternational.socialrehab;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.kepler.projectsupportlib.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by amit on 23/1/18.
 */

public class MyAccessibilityService extends AccessibilityService {
    /*packages*/
    public static final String PACKAGE_FB_KATANA = "com.facebook.katana";
    public static final String PACKAGE_IG = "com.instagram.android";
    public static final String PACKAGE_WHATS_APP = "com.whatsapp";
    /**/
    /*packages constants*/
    public static final int PACKAGE_FB_KATANA_INT = 0;
    public static final int PACKAGE_WHATS_APP_INT = 1;
    public static final int PACKAGE_IG_INT = 2;
    //    public static final List<String> PACKAGE_NAMES = new ArrayList<String>() {{
//        add(PACKAGE_FB_KATANA);
//        add(PACKAGE_IG);
//        add(PACKAGE_WHATS_APP);
//        add("com.indiashopps.android");
//    }};
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
                    if(sharedPref.isFacebookEnabled()){
                        startTime=sharedPref.facebookStartTime();
                        endTime=sharedPref.facebookEndTime();
                    }
                    break;
                case PACKAGE_IG:
                    if(sharedPref.isInstagramEnabled()){
                        startTime=sharedPref.instagramStartTime();
                        endTime=sharedPref.instagramEndTime();
                    }
                    break;
                case PACKAGE_WHATS_APP:
                    if(sharedPref.isWhatsAppEnabled()){
                        startTime=sharedPref.whatsAppStartTime();
                        endTime=sharedPref.whatsAppEndTime();
                    }
                    break;
                case "com.indiashopps.android":
                    if(sharedPref.isInstagramEnabled()){
                        startTime=sharedPref.instagramStartTime();
                        endTime=sharedPref.instagramEndTime();
                    }
                    break;
                default:
                    OverLayService.stopSOSService(getApplicationContext());
            }
            if (startTime == null || endTime == null)
                return;
            Date time1 = new SimpleDateFormat("kk:mm").parse(startTime);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(time1);

            Date time2 = new SimpleDateFormat("kk:mm").parse(endTime);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(time2);

            SimpleDateFormat d = new SimpleDateFormat("kk:mm");
            Date x = d.parse(d.format(Calendar.getInstance().getTime()));

            if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {
                //checkes whether the current time is between 14:49:00 and 20:11:13.
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
}
