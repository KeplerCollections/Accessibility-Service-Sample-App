package com.socialrehab.android.module.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.kepler.projectsupportlib.Logger;

import static com.socialrehab.android.module.accessibility.TaskHandler.EXTRA_CLASS_NAME;

/**
 * Created by amit on 23/1/18.
 */

public class MyAccessibilityService extends AccessibilityService {


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        Logger.e(accessibilityEvent.toString());
        TaskHandler.handle(getApplicationContext(),accessibilityEvent.getPackageName().toString(),
                getText(accessibilityEvent),accessibilityEvent.getClassName().toString());
//        Intent intent = new Intent(getApplicationContext(), TaskHandler.class);
//        intent.putExtra(Intent.EXTRA_PACKAGE_NAME, accessibilityEvent.getPackageName());
//        intent.putExtra(Intent.EXTRA_TEXT, getText(accessibilityEvent));
//        intent.putExtra(EXTRA_CLASS_NAME, accessibilityEvent.getClassName());
//        startService(intent);
    }

    private String getText(AccessibilityEvent accessibilityEvent) {
        try {
            return accessibilityEvent.getText().get(0).toString().trim()
                    ;
        } catch (Exception e) {

        }
        return "";
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
        Logger.e("unbindService");
        Toast.makeText(getApplicationContext(),"unbindService",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onInterrupt() {
        Toast.makeText(getApplicationContext(),"onInterrupt",Toast.LENGTH_SHORT).show();
        Logger.e("onInterrupt");
    }

    @Override
    protected void onServiceConnected() {
        Logger.d("***** onServiceConnected");
//        AccessibilityServiceInfo info = getServiceInfo();
//        info.flags = AccessibilityServiceInfo.DEFAULT;
//        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
//        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;
//        info.notificationTimeout = 100;
//        this.setServiceInfo(info);
        super.onServiceConnected();
    }


}
