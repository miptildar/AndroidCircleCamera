package com.ildar.circlecamera.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by ildar on 18.01.2018.
 */

public class ImageUtil {

    public static Bitmap processImage(byte[] data, int pictureWidth, int pictureHeight, int imageSize){

        // Load the bitmap from the byte array
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);


        // Crop the image into a square
        int croppedWidth = (pictureWidth > pictureHeight) ? pictureHeight : pictureWidth;
        int croppedHeight = (pictureWidth > pictureHeight) ? pictureHeight : pictureWidth;
        Bitmap cropped = Bitmap.createBitmap(bitmap, 0, 0, croppedWidth, croppedHeight);


        // Scale down to the output size
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(cropped, imageSize, imageSize, true);
        bitmap.recycle();
        cropped.recycle();

        return scaledBitmap;
    }


    public static byte[] convertBitmapToByteArray(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

}
