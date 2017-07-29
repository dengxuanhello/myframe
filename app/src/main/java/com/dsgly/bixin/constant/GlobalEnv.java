package com.dsgly.bixin.constant;

import android.content.pm.PackageManager;

import com.dsgly.bixin.APP;

/**
 * Created by dengxuan on 2017/7/22.
 */

public class GlobalEnv {
    public static String getVersionName(){
        try {
            return APP.getContext().getPackageManager().getPackageInfo(APP.getContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String getVersionCode(){
        try {
            return String.valueOf(APP.getContext().getPackageManager().getPackageInfo(APP.getContext().getPackageName(), 0).versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
}
