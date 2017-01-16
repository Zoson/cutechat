package com.druson.cycle.service.picture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by zoson on 4/2/15.
 */
public class CameraService implements Camera.PreviewCallback{
    private final String TAG = "CameraService";
    Camera camera;
    byte[] imageByte;
    Camera.Parameters parameters;
    int width;
    int height;
    int count = 0;
    int frequency = 10;
    boolean if_getImg = false;
    CatchImageCallback cb;
    HandleBitmapThread handleBitmapThread;

    public CameraService(){
    }

    public void init(int frequency,CatchImageCallback catchImageCallback,SurfaceHolder surfaceHolder){
        camera = Camera.open(1);
        parameters = camera.getParameters();
        parameters.setPreviewSize(352, 288);
        camera.setParameters(parameters);
        width = parameters.getPreviewSize().width;
        height = parameters.getPreviewSize().height;
        try {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageByte = ("").getBytes();
        setCallbackForImage(catchImageCallback, frequency);
        handleBitmapThread = new HandleBitmapThread();
        handleBitmapThread.start();
    }

    public void startPreview(){
        handleBitmapThread = new HandleBitmapThread();
        handleBitmapThread.start();
        camera.startPreview();
        camera.setDisplayOrientation(90);
        camera.setPreviewCallback(this);
    }

    public void setCallbackForImage(CatchImageCallback cb,int frequency){
        this.cb =cb;
        this.frequency = frequency;
    }
    @Override
    public void onPreviewFrame(final byte[] data, Camera camera) {
        if (cb == null)return;
        if (count == frequency){
            if (handleBitmapThread != null){
                Message message = handleBitmapThread.handler.obtainMessage();
                message.what = HandleBitmapThread.GETDATA;
                message.obj = data;
                handleBitmapThread.handler.sendMessage(message);
            }
            count = 0;
        }
        count++;
    }

    class HandleBitmapThread extends Thread{

        static final int GETDATA = 0x1;
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case GETDATA:
                        handleDataToImageByte((byte[]) msg.obj);
                        break;
                }
            }
        };

        public HandleBitmapThread(){

        }

        public void handleDataToImageByte(byte[] data){
            Log.i(TAG, "handleDataToImageByte");
            int imageFormat = parameters.getPreviewFormat();
            Rect rect = new Rect(0,0,width,height);
            YuvImage yuvImage = new YuvImage(data,imageFormat,width,height,null);
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                yuvImage.compressToJpeg(rect,100,outputStream);
                imageByte = outputStream.toByteArray();
            }catch (Exception e){
                e.printStackTrace();
            }
            Bitmap mp = BitmapFactory.decodeByteArray(imageByte,0,imageByte.length);
            Matrix m = new Matrix();
            m.setRotate(270);
            Bitmap mpp = Bitmap.createBitmap(mp,0,0,width,height,m,true);
            imageByte = BitmapToBytes(mpp);
            cb.getBytes(imageByte);
        }
        @Override
        public void run() {
            Looper.prepare();
            Looper.loop();
        }
    }

    public Camera getCamera(){
        return camera;
    }
    public byte[] getImageByte(){
        return imageByte;
    }
    public byte[] BitmapToBytes(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG,100,baos);
        return baos.toByteArray();
    }
    public void deleteCamera(){
        camera.setPreviewCallbackWithBuffer(null);
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    public void stopCatch(){
        handleBitmapThread = null;
        camera.stopPreview();
    }

    public void startCatch(){
        handleBitmapThread = new HandleBitmapThread();
        handleBitmapThread.start();
        camera.startPreview();
    }

    public interface CatchImageCallback{
    	public void getBytes(byte[] bytes);
    }
}
