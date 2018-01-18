package com.ildar.circlecamera;

import android.Manifest;
import android.graphics.Bitmap;
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

    private CardView previewCardView;

    private Button button;

    Camera camera;

    boolean cameraActive = false;

    boolean cameraConfigured = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        surfaceView = findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        previewCardView = findViewById(R.id.previewCardView);
        imageView = findViewById(R.id.imageView);
        button = findViewById(R.id.button);

        button.setOnClickListener(this);


        PermissionUtil.checkPermission(this, Manifest.permission.CAMERA);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                if(cameraActive){
                    takePhoto();
                    button.setText("Open Camera");
                    cameraActive = false;
                }else{
                    openCamera();
                    button.setText("Take photo");
                    cameraActive = true;
                }
                openCamera();
                break;

            default:
                break;
        }
    }

    private void openCamera(){
        camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);

        try {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();

        if(!cameraConfigured){
            CameraUtil.configureSquareCamera(this, camera, IMAGE_SIZE);
            cameraConfigured = true;
        }
    }

    private void takePhoto(){
        camera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        System.out.println("onPictureTaken()");
                        Bitmap bitmap = ImageUtil.processImage(
                                data,
                                camera.getParameters().getPictureSize().width,
                                camera.getParameters().getPictureSize().height,
                                IMAGE_SIZE
                        );

                        imageView.setImageBitmap(bitmap);
                        camera.release();
                    }
                }
        );
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated");
//        try {
//            // Important https://stackoverflow.com/a/30616683/3405101
//            Log.d(TAG, this.surfaceHolder+" "+camera);
//            camera.setPreviewDisplay(this.surfaceHolder);
//            camera.startPreview();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged");
//        camera.stopPreview();
//        try {
//            camera.setPreviewDisplay(this.surfaceHolder);
//            camera.startPreview();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed");
    }
}
