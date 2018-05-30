package com.manyainternational.socialrehab;

/**
 * Created by amit on 10/1/18.
 */

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

import java.util.Iterator;
import java.util.List;

/**
 * Foreground service. Creates a sos view.
 * The pending intent allows to go back to the settings activity.
 */
public class OverLayService extends Service {
    private final static int FOREGROUND_ID = 999;
    private OverLayLayer mHeadLayer;

    public static void startSOSService(Context mContext) {
//        if (isServiceRunning(mContext, SOSService.class.getName()))
//            stopSOSService(mContext);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(mContext))
            mContext.startService(new Intent(mContext, OverLayService.class));
    }

    public static void stopSOSService(Context mContext) {
        mContext.stopService(new Intent(mContext, OverLayService.class));
    }

    //    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        logServiceStarted();
//
//        initHeadLayer();
//
//        PendingIntent pendingIntent = createPendingIntent();
//        Notification notification = createNotification(pendingIntent);
//
//        startForeground(FOREGROUND_ID, notification);
//
//        return START_STICKY;
//    }

    private static boolean isServiceRunning(Context mContext, String serviceName) {
        boolean serviceRunning = false;
        ActivityManager am = (ActivityManager) mContext.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> l = am.getRunningServices(50);
        Iterator<ActivityManager.RunningServiceInfo> i = l.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningServiceInfo runningServiceInfo = i
                    .next();

            if (runningServiceInfo.service.getClassName().equals(serviceName)) {
                serviceRunning = true;

                if (runningServiceInfo.foreground) {
                    //service run in foreground
                }
            }
        }
        return serviceRunning;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        logServiceStarted();
        initHeadLayer();
    }

    @Override
    public void onDestroy() {
        destroyHeadLayer();
        stopForeground(true);
        logServiceEnded();
    }

    private void initHeadLayer() {
        mHeadLayer = new OverLayLayer(this);
    }

    private void destroyHeadLayer() {
        mHeadLayer.destroy();
        mHeadLayer = null;
    }

//    private PendingIntent createPendingIntent() {
//        Intent intent = new Intent(this, HomeActivity.class);
//        return PendingIntent.getActivity(this, 0, intent, 0);
//    }
//
//    private Notification createNotification(PendingIntent intent) {
//        return new Notification.Builder(this)
//                .setContentTitle(getText(R.string.notificationTitle))
//                .setContentText(getText(R.string.notificationText))
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentIntent(intent)
//                .build();
//    }

    private void logServiceStarted() {
//        Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();
    }

    private void logServiceEnded() {
//        Toast.makeText(this, "Service ended", Toast.LENGTH_SHORT).show();
    }
}
