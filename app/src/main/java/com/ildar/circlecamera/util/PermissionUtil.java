package com.ildar.circlecamera.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by ildar on 18.01.2018.
 */

public class PermissionUtil {

    private static final String TAG = "Permissions";

    public static boolean checkPermission(Activity activity, String permissionType){
        if(ContextCompat.checkSelfPermission(activity, permissionType ) == PackageManager.PERMISSION_GRANTED) return true;
        requestPermission(activity, permissionType);
        return false;
    }

    public static void requestPermission(Activity activity, String permissionType){
        ActivityCompat.requestPermissions(activity, new String[]{permissionType}, 1);
    }

}
