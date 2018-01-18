package com.ildar.circlecamera;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ildar.circlecamera.util.CameraUtil;
import com.ildar.circlecamera.util.CommonUtil;
import com.ildar.circlecamera.util.ImageUtil;
import com.ildar.circlecamera.util.PermissionUtil;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SurfaceHolder.Callback{

    private final static String TAG = "MainActivity";

    // See activity_main.xml, 100dp - the size of SurfaceView
    private final int IMAGE_SIZE = CommonUtil.convertDipToPx(100);

    private SurfaceView surfaceView;

    private SurfaceHolder surfaceHolder;

    private ImageView imageView;

    private Button button;

    Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        surfaceView = findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        imageView = findViewById(R.id.imageView);
        button = findViewById(R.id.button);

        button.setOnClickListener(this);


        PermissionUtil.checkPermission(this, Manifest.permission.CAMERA);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                takePhoto();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        openCamera();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (camera != null) camera.release();
    }

    private void openCamera(){
        camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);

        // One have to configure camera each time you open it...unfortunately
        CameraUtil.configureSquareCamera(this, camera, IMAGE_SIZE);
    }

    private void takePhoto(){
        Log.d(TAG, "takePhoto");
        if(camera == null) {
            Log.d(TAG, "Camera is null");
            return;
        }


        camera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        System.out.println("onPictureTaken()");
                        Bitmap bitmap;

                        bitmap = ImageUtil.processImage(
                                data,
                                camera.getParameters().getPictureSize().width,
                                camera.getParameters().getPictureSize().height,
                                IMAGE_SIZE
                        );

                        //bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                        imageView.setImageBitmap(bitmap);


                        // Take photos infinitely:
                        try {
                            camera.setPreviewDisplay(surfaceHolder);
                            camera.startPreview();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
        );

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated");
        try {
            // Important https://stackoverflow.com/a/30616683/3405101
            Log.d(TAG, this.surfaceHolder+" "+camera);
            camera.setPreviewDisplay(this.surfaceHolder);
            camera.startPreview();
        } catch (IOException e) {

        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged");
        camera.stopPreview();
        try {
            camera.setPreviewDisplay(this.surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed");
    }
}
