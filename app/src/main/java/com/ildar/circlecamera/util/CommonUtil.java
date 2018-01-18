package com.ildar.circlecamera.util;

import android.content.res.Resources;
import android.util.TypedValue;

import com.ildar.circlecamera.CircleCameraApplication;

/**
 * Created by ildar on 18.01.2018.
 */

public class CommonUtil {

    /**
     * Source https://stackoverflow.com/a/6327095/3405101
     * @param dip
     * @return
     */
    public static int convertDipToPx(int dip){
        Resources resources = CircleCameraApplication.getInstance().getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, resources.getDisplayMetrics()));
    }



    public static <T> String printArray(T[] array, ToString<T> toString){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0; i<array.length; i++) stringBuilder.append(toString.toString(array[i])).append(", ");

        return stringBuilder.toString();
    }

    public interface ToString<T>{
        String toString(T t);
    }

}
