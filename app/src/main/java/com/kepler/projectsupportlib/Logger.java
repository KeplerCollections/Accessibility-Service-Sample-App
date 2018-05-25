package com.kepler.projectsupportlib;

import android.util.Log;

import com.manyainternational.socialrehab.BuildConfig;


/**
 * Created by special on 5/12/17.
 */

public class Logger {

    public static final int DIALOG_ERROR = 1;
    public static final int DIALOG_ALERT = 2;
    public static final int DIALOG_CONFIRM = 3;
    private static final String TAG = Logger.class.getSimpleName();


    /* print */
    public static void print(Exception e) {
        if (BuildConfig.DEBUG) {
            e.printStackTrace();
        } else {
            // Crashlytics.logException(e);
        }
    }

    public static void print(Throwable e) {
        if (BuildConfig.DEBUG) {
            e.printStackTrace();
        } else {
            // Crashlytics.logException(e);
        }
    }

    public static void print(String msg) {
        if (BuildConfig.DEBUG) {
            System.out.println(msg);
        }
    }

    /*error */
    public static void e(String TAG, String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, msg);
        }
    }

    /*debug*/
    public static void d(String TAG, String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public static void d(String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, msg);
        }
    }


    /*info*/
    public static void i(String TAG, String msg) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, msg);
        }
    }

    public static void i(String msg) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, msg);
        }
    }
}
