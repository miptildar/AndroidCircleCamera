package com.ildar.circlecamera.util;

import android.content.Context;
import android.hardware.Camera;
import android.view.Surface;
import android.view.WindowManager;

import java.util.Arrays;
import java.util.Comparator;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by ildar on 18.01.2018.
 */

public class CameraUtil {

    public static void configureSquareCamera(Context context, Camera camera, int IMAGE_SIZE){
        Camera.Parameters camParams = camera.getParameters();

        // SIZE ISSUES
        // We are looking for a Preview size which parameters (width, height) are the closest to square:  | width - height |  ->  min
        Camera.Size[] previewSizes = new Camera.Size[camParams.getSupportedPreviewSizes().size()];
        previewSizes = camParams.getSupportedPreviewSizes().toArray(previewSizes);
        Arrays.sort(previewSizes, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size size1, Camera.Size size2) {
                int delta1 = Math.abs(size1.height - size1.width);
                int delta2 = Math.abs(size2.height - size2.width);

                if(delta1 > delta2) return 1;
                else if(delta1 < delta2) return -1;
                return 0;
            }
        });

        Camera.Size targetPreviewSize = null;
        for(int i=0; i<previewSizes.length; i++){
            if(previewSizes[i].height >= IMAGE_SIZE && previewSizes[i].width >= IMAGE_SIZE){
                targetPreviewSize = previewSizes[i];
                break;
            }
        }

        if(targetPreviewSize == null) throw new RuntimeException("Problems with Camera preview size");
        camParams.setPreviewSize(targetPreviewSize.width, targetPreviewSize.height);


        // Now lets find a Picture size which parameters (width, height) are the closest to square: | width - height |  ->  min
        Camera.Size targetPictureSize = null;
        Camera.Size[] pictureSizes = new Camera.Size[camParams.getSupportedPictureSizes().size()];
        pictureSizes = camParams.getSupportedPictureSizes().toArray(pictureSizes);
        Arrays.sort(pictureSizes, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size size1, Camera.Size size2) {
                int delta1 = Math.abs(size1.height - size1.width);
                int delta2 = Math.abs(size2.height - size2.width);

                if(delta1 > delta2) return 1;
                else if(delta1 < delta2) return -1;
                return 0;
            }
        });
        for(Camera.Size pictureSize: pictureSizes){
            if(pictureSize.width >= IMAGE_SIZE && pictureSize.height >= IMAGE_SIZE){
                targetPictureSize = pictureSize;
                break;
            }
        }

        if(targetPictureSize == null) throw new RuntimeException("Problems with Camera picture size");
        camParams.setPictureSize(targetPictureSize.width, targetPictureSize.height);


        // ROTATION ISSUES
        camParams.set("orientation", "portrait");

        Camera.CameraInfo camInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_FRONT, camInfo);
        int cameraRotationOffset = camInfo.orientation;

        int rotation = ((WindowManager)context.getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (camInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (camInfo.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {
            result = (camInfo.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);

        int rotate = (360 + cameraRotationOffset + degrees) % 360;
        camParams.setRotation(rotate);

        camera.setParameters(camParams);
    }

}
