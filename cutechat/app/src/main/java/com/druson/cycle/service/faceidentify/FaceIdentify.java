package com.druson.cycle.service.faceidentify;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.init.cutechat.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Queue;

public class FaceIdentify{
    private final String TAG = "FaceIdentify";
    private boolean debug = false;
    private Context mContext = null;
    private File dataDir = null;
    private File f_frontalface = null;
    private File f_lefteye = null;
    private File f_righteye = null;
    private File f_testface = null;
    private float ratioW = 0;
  	private float ratioH = 0;
    private Bitmap mImage = null;
    private FaceIdentifyCallback mFaceIdentifyCallback;
    private final String filename = "face.jpg";

    private Queue<byte[]> mImageByteQueue;
    public byte[] mImageBytes = null;

    static {
        System.loadLibrary("faceidentify");
    }

    private Process_t process_t;
    private boolean isIdentifying = false;

    public native int[] FindFaceLandmarks(float ratioW, float ratioH);

    public FaceIdentify(Context context){
        this.mContext = context;
        mImageByteQueue = new ArrayDeque<>();
        if (!isDataFileInLocalDir()) {
            putDataFileInLocalDir(R.raw.haarcascade_frontalface_alt2, f_frontalface);
            putDataFileInLocalDir(R.raw.haarcascade_mcs_lefteye, f_lefteye);
            putDataFileInLocalDir(R.raw.haarcascade_mcs_righteye, f_righteye);
            f_testface = new File(dataDir,filename);
            putDataFileInLocalDir(R.drawable.testface, f_testface);

        }

        Bitmap temp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.testface);
        if (debug) Log.e(TAG, "Original Image: "+temp.getWidth()+"X"+ temp.getHeight());
        mImage = Bitmap.createScaledBitmap(temp, 352,288, true);
        ratioW = 3.0f;
		ratioH = 3.0f;

        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_10, mContext, mLoaderCallback);
        process_t = new Process_t();
        process_t.start();
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(mContext) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    if (debug) Log.e(TAG, "OpenCV loaded successfully");
                    startNextIdentify();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };;

    private boolean isDataFileInLocalDir() {
        boolean ret = false;
        try {
            dataDir = mContext.getDir("cutechat", Context.MODE_PRIVATE);
            f_frontalface = new File(dataDir, "haarcascade_frontalface_alt2.xml");
            f_lefteye     = new File(dataDir, "haarcascade_mcs_lefteye.xml");
            f_righteye    = new File(dataDir, "haarcascade_mcs_righteye.xml");

            ret = f_frontalface.exists() && f_lefteye.exists() && f_righteye.exists();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public void putDataFileInLocalDir() {
        mImageBytes = mImageByteQueue.poll();
        if(mImageBytes == null)return;
        if (debug) Log.e(TAG, "putDataFileInLocalDir: "+f_testface.toString());
        try {
            if (f_testface==null||mImageBytes == null){
                f_testface = new File(dataDir, filename);
                putDataFileInLocalDir(R.drawable.testface, f_testface);
                return;
            }
            FileOutputStream os = new FileOutputStream(f_testface);
            os.write(mImageBytes);
            mImageBytes = null;
            os.close();
            processing();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (debug) Log.e(TAG, "putDataFileInLocalDir: done!");
    }
    private void putDataFileInLocalDir(int id, File f) {

        if (debug) Log.e(TAG, "putDataFileInLocalDir: "+f.toString());
        try {
            InputStream is = mContext.getResources().openRawResource(id);
            FileOutputStream os = new FileOutputStream(f);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (debug) Log.e(TAG, "putDataFileInLocalDir: done!");
    }

    public void processing() {
        isIdentifying = true;
        Log.i(TAG,"processing");
        int[] points = FindFaceLandmarks(ratioW, ratioH);
        if (debug) Log.e(TAG, ""+points.length);
        if ((points[0] == -1) && (points[1] == -1)) {

        } else if ((points[0] == -2) && (points[1] == -2)) {

        } else if ((points[0] == -3) && (points[1] == -3)) {

        } else {
            if (mFaceIdentifyCallback != null){
                mFaceIdentifyCallback.getFeaturePoints(points);
            }
        }
        isIdentifying = false;
    }

    public void setImageByte(byte[] bit){
        if (mImageByteQueue.size()>20){
            return;
        }
        Log.i(TAG,"Queue size = "+mImageByteQueue.size());
        this.mImageByteQueue.add(bit);
        startNextIdentify();
    }


    class Process_t extends Thread{

        static final int IDENTIFY = 0x1;
        public Handler handler;
        @Override
        public void run() {
            Looper.prepare();
            handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what){
                        case IDENTIFY:
                            putDataFileInLocalDir();
                            break;
                    }
                }
            };
            Looper.loop();
        }
    }

    public void stopIdentify(){
        process_t = null;
        mFaceIdentifyCallback = null;
    }

    public void startIdentify(FaceIdentifyCallback faceIdentifyCallback){
        this.mFaceIdentifyCallback = faceIdentifyCallback;
        if (process_t!=null)return;
        process_t = new Process_t();
        process_t.start();
    }

    protected void startNextIdentify(){
        if (process_t!=null){
            Message message = process_t.handler.obtainMessage();
            message.what = Process_t.IDENTIFY;
            process_t.handler.sendMessage(message);
        }
    }

    public interface FaceIdentifyCallback{
        public void getFeaturePoints(int[] points);
    }
}
