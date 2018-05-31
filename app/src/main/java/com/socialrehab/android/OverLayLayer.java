package com.socialrehab.android;

/**
 * Created by amit on 10/1/18.
 */

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.socialrehab.R;

import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 * Creates the SOS layer view which is displayed directly on window manager.
 * It means that the view is above every application's view on your phone -
 * until another application does the same.
 */
public class OverLayLayer extends View {
    private Context mContext;
    private FrameLayout mFrameLayout;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams params;

    public OverLayLayer(Context context) {
        super(context);

        mContext = context;
        mFrameLayout = new FrameLayout(mContext);
        mFrameLayout.setBackgroundResource(R.drawable.hover_on);
        addToWindowManager();
    }

    private void addToWindowManager() {
//        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.TYPE_PHONE,
//                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
//                PixelFormat.TRANSLUCENT);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ){
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.RGB_565);

        }else{
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.RGB_565);

        }
        params.gravity = Gravity.CENTER;

        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.addView(mFrameLayout, params);

        ViewHolder viewHolder = new ViewHolder();
        viewHolder.bindView(mFrameLayout);
        viewHolder.view.setOnTouchListener(new OnTouchListener() {
            private int initX, initY;
            private int initTouchX, initTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getRawX();
                int y = (int) event.getRawY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initX = params.x;
                        initY = params.y;
                        initTouchX = x;
                        initTouchY = y;
                        return true;

                    case MotionEvent.ACTION_UP:
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        params.x = initX + (x - initTouchX);
                        params.y = initY + (y - initTouchY);

                        // Invalidate layout
                        mWindowManager.updateViewLayout(mFrameLayout, params);
                        return true;
                }
                return false;
            }
        });
    }


    /**
     * Removes the view from window manager.
     */
    public void destroy() {
        mWindowManager.removeView(mFrameLayout);
    }

    private class ViewHolder {
        private final LayoutInflater layoutInflater;
        private final Vibrator v;
        int dot = 200; // Length of a Morse Code "dot" in milliseconds
        int dash = 500; // Length of a Morse Code "dash" in milliseconds
        long[] pattern = {0, dot, dash, dot};
        LinearLayout view;
        TextView textView;
        Button button;
        private int counter = 15;
        private Timer mTimer1;
        private TimerTask mTt1;
        private Handler mTimerHandler = new Handler();

        public ViewHolder() {
            layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = (Vibrator) mContext.getSystemService(VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
        }

        // Here is the place where you can inject whatever layout you want.
        public void bindView(FrameLayout mFrameLayout) {

            layoutInflater.inflate(R.layout.overlay, mFrameLayout);

            // Support dragging the image view
            view = (LinearLayout) mFrameLayout.findViewById(R.id.view);
            textView = (TextView) mFrameLayout.findViewById(R.id.textView);

        }
    }
}