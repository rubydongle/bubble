package com.nkanaev.comics;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;


public class MainApplication extends Application {
    private static final String TAG = "ruby";
    private static Context mContext;


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getAppContext() {
        return mContext;
    }

    public static SharedPreferences getPreferences() {
        return mContext.getSharedPreferences(Constants.SETTINGS_NAME, 0);
    }
}
