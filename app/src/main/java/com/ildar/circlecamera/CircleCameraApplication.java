package com.ildar.circlecamera;

import android.app.Application;

/**
 * Created by ildar on 18.01.2018.
 */

public class CircleCameraApplication extends Application {

    private static CircleCameraApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static CircleCameraApplication getInstance(){
        return instance;
    }
}
