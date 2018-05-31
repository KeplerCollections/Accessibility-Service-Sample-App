package com.socialrehab.android;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.util.Log;

import com.kepler.projectsupportlib.Logger;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.socialrehab.android.MyAccessibilityService.PACKAGE_FB_KATANA;
import static com.socialrehab.android.MyAccessibilityService.PACKAGE_FB_KATANA_INT;
import static com.socialrehab.android.MyAccessibilityService.PACKAGE_IG;
import static com.socialrehab.android.MyAccessibilityService.PACKAGE_IG_INT;
import static com.socialrehab.android.MyAccessibilityService.PACKAGE_NAMES;
import static com.socialrehab.android.MyAccessibilityService.PACKAGE_WHATS_APP;
import static com.socialrehab.android.MyAccessibilityService.PACKAGE_WHATS_APP_INT;

public class UStats {
    public static final String TAG = UStats.class.getSimpleName();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("M-d-yyyy HH:mm:ss");
    private static Map<String, List<Integer>> stringListMap = new LinkedHashMap<>();
    private static Calendar calendar;

    @SuppressWarnings("ResourceType")
    public static void getStats(Context context) {
        UsageStatsManager usm = (UsageStatsManager) context.getSystemService("usagestats");
        int interval = UsageStatsManager.INTERVAL_DAILY;
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.HOUR, 0);
        calendar.add(Calendar.MINUTE, 0);
        long startTime = calendar.getTimeInMillis();

        Log.d(TAG, "Range start:" + dateFormat.format(startTime));
        Log.d(TAG, "Range end:" + dateFormat.format(endTime));

        UsageEvents uEvents = usm.queryEvents(startTime, endTime);
        while (uEvents.hasNextEvent()) {
            UsageEvents.Event e = new UsageEvents.Event();
            uEvents.getNextEvent(e);

            if (e != null) {
                Log.d(TAG, "Event: " + e.getPackageName() + "\t" + e.getTimeStamp());
            }
        }
    }

    private static List<UsageStats> getUsageStatsList(Context context) {
        UsageStatsManager usm = getUsageStatsManager(context);
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
//        calendar.add(Calendar.YEAR, -1);
        calendar.add(Calendar.DAY_OF_YEAR, -3);
        long startTime = calendar.getTimeInMillis();

        Log.e(TAG, "Range start:" + dateFormat.format(startTime));
        Log.e(TAG, "Range end:" + dateFormat.format(endTime));

        List<UsageStats> usageStatsList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);
        return usageStatsList;
    }

    private static int getMinutesFromMillis(long milliseconds) {
        if (milliseconds > 0)
            return (int) TimeUnit.MILLISECONDS.toMinutes(milliseconds);
        else return 0;
    }

    public static Map<String, List<Integer>> getRefindUsageStatsList(Context context) {
        setMap();
        List<UsageStats> usageStatsList = getUsageStatsList(context);
        for (UsageStats u : usageStatsList) {
//            Log.d(TAG, "Pkg: " + u.getPackageName() +  "\t" + "ForegroundTime: "
//                    + u.getTotalTimeInForeground()) ;
            switch (u.getPackageName()) {
                case PACKAGE_FB_KATANA:
                    calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(u.getFirstTimeStamp());
                    setList(new DateFormatSymbols().getShortWeekdays()[calendar.get(Calendar.DAY_OF_WEEK)],
                            PACKAGE_FB_KATANA_INT,
                            u.getTotalTimeInForeground());
                    break;
                case PACKAGE_IG:
                    calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(u.getFirstTimeStamp());
                    setList(new DateFormatSymbols().getShortWeekdays()[calendar.get(Calendar.DAY_OF_WEEK)],
                            PACKAGE_IG_INT, u.getTotalTimeInForeground());
                    break;
                case PACKAGE_WHATS_APP:
                    calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(u.getFirstTimeStamp());
                    setList(new DateFormatSymbols().getShortWeekdays()[calendar.get(Calendar.DAY_OF_WEEK)],
                            PACKAGE_WHATS_APP_INT, u.getTotalTimeInForeground());
                    break;
            }
        }
        return stringListMap;
    }

    private static void setMap() {
        stringListMap.clear();
        calendar = Calendar.getInstance();
        stringListMap.put(new DateFormatSymbols().getShortWeekdays()[calendar.get(Calendar.DAY_OF_WEEK)], new ArrayList<>(Arrays.asList(new Integer[]{0, 0, 0})));
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        stringListMap.put(new DateFormatSymbols().getShortWeekdays()[calendar.get(Calendar.DAY_OF_WEEK)], new ArrayList<>(Arrays.asList(new Integer[]{0, 0, 0})));
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        stringListMap.put(new DateFormatSymbols().getShortWeekdays()[calendar.get(Calendar.DAY_OF_WEEK)], new ArrayList<>(Arrays.asList(new Integer[]{0, 0, 0})));
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        stringListMap.put(new DateFormatSymbols().getShortWeekdays()[calendar.get(Calendar.DAY_OF_WEEK)], new ArrayList<>(Arrays.asList(new Integer[]{0, 0, 0})));
    }

    private static void setList(String key, int index, long value) {
        if (stringListMap.containsKey(key)) {
            List<Integer> list = stringListMap.get(key);
            list.set(index, getMinutesFromMillis(value));
            stringListMap.put(key, list);
        }
    }

    public static void printUsageStats(List<UsageStats> usageStatsList) {
        for (UsageStats u : usageStatsList) {
            if (PACKAGE_NAMES.contains(u.getPackageName())) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(u.getFirstTimeStamp());
                Log.e(TAG, "Pkg: " + u.getPackageName());
                Log.i(TAG, "\nForegroundTime: " + u.getTotalTimeInForeground()
                        + ", " + "getFirstTimeStamp: " + u.getFirstTimeStamp()
                        + ", " + "getLastTimeStamp: " + u.getLastTimeStamp()
                        + ", " + "getLastTimeUsed: " + u.getLastTimeUsed()
                );
                Logger.e(new DateFormatSymbols().getShortWeekdays()[calendar.get(Calendar.DAY_OF_WEEK)]);
            }
        }

    }

    public static void printCurrentUsageStatus(Context context) {
        printUsageStats(getUsageStatsList(context));
    }

    @SuppressWarnings("ResourceType")
    private static UsageStatsManager getUsageStatsManager(Context context) {
        UsageStatsManager usm = (UsageStatsManager) context.getSystemService("usagestats");
        return usm;
    }
}