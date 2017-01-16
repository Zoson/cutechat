package com.init.cutechat.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.druson.cycle.utils.ThreadPool;
import com.init.cutechat.R;
import com.init.cutechat.domain.enity.Face;

/**
 * Created by Zoson on 16/2/16.
 */
public class CuteFaceView extends SurfaceView implements SurfaceHolder.Callback{
    final String TAG = "CuteFaceView";
    SurfaceHolder sfHolder;
    Context context;
    Resources mRes;
    Paint mPaint;
    Bitmap face;
    int width;
    int height;
    int bm_width;
    int bm_heigt;
    float draw_x;
    float draw_y;
    float bm_scale;
    int color_theme;
    int color_theme_2;
    Matrix matrix;
    int radio = 5;

    public CuteFaceView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public CuteFaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public CuteFaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public CuteFaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init();
    }

    public void init(){
        mRes = context.getResources();
        mPaint = new Paint();
        sfHolder = this.getHolder();
        setZOrderOnTop(true);
        sfHolder.setFormat(PixelFormat.TRANSPARENT);
        sfHolder.addCallback(this);
        color_theme = context.getResources().getColor(R.color.theme_color);
        color_theme_2 = context.getResources().getColor(R.color.theme_color_2);
    }

    public void drawCuteFace(final Face face){
        ThreadPool.start(new Runnable() {
            @Override
            public void run() {
                if (sfHolder == null) return;
                final Canvas canvas = sfHolder.lockCanvas();
                if (canvas == null) return;
                drawBackground(canvas);
                drawEmptyFace(canvas);
                Bitmap eyebrow_left = scaleBitmap(BitmapFactory.decodeResource(mRes, face.getLeftEyeBrow()));
                Bitmap eyebrow_right = scaleBitmap(BitmapFactory.decodeResource(mRes, face.getRightEyeBrow()));
                Bitmap eye_left = scaleBitmap(BitmapFactory.decodeResource(mRes, face.getLeftEye()));
                Bitmap eye_right = scaleBitmap(BitmapFactory.decodeResource(mRes, face.getRightEye()));
                //Bitmap nose = BitmapFactory.decodeResource(mRes,face.getNose());
                Bitmap mouth = scaleBitmap(BitmapFactory.decodeResource(mRes, face.getMouth()));

                canvas.drawBitmap(eyebrow_left, draw_x, draw_y, mPaint);
                canvas.drawBitmap(eyebrow_right, draw_x, draw_y, mPaint);
                canvas.drawBitmap(eye_left, draw_x, draw_y, mPaint);
                canvas.drawBitmap(eye_right, draw_x, draw_y, mPaint);
                canvas.drawBitmap(mouth, draw_x, draw_y, mPaint);
                //canvas.drawBitmap(nose,0,0,mPaint);

                eyebrow_left.recycle();
                eyebrow_right.recycle();
                eye_left.recycle();
                eye_right.recycle();
                //nose.recycle();
                mouth.recycle();

                sfHolder.unlockCanvasAndPost(canvas);
            }
        });
    }

    public void drawEmptyFace(Canvas canvas){
        //Canvas canvas = sfHolder.lockCanvas();
        if (face==null){
            face = BitmapFactory.decodeResource(mRes, R.drawable.face_b);
            scaleBitmap(face);
            face = Bitmap.createBitmap(face, 0, 0, face.getWidth(), face.getHeight(), matrix, true);
            draw_x = (float)this.getWidth()/(radio*2);
            draw_y = (float)(this.getHeight()-face.getHeight())-(float)this.getWidth()/(radio*2);
        }
        drawBackground(canvas);
        canvas.drawBitmap(face, draw_x, draw_y, mPaint);
        //sfHolder.unlockCanvasAndPost(canvas);
    }

    public Bitmap scaleBitmap(Bitmap bitmap){
        if (matrix==null){
            matrix = new Matrix();
            bm_scale = ((float)this.getWidth()*(radio-1)/radio)/bitmap.getWidth();
            matrix.postScale(bm_scale,bm_scale);
        }
        bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        return bitmap;
    }

    public void drawBackground(Canvas canvas){
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ALPHA_8);
        bitmap.eraseColor(Color.argb(0, 255, 255, 255));
        Paint paint = new Paint();
        paint.setColor(Color.argb(0, 255, 255, 255));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        bitmap.recycle();
        //canvas.drawRect(0, 0, this.getWidth(), this.getHeight(), paint);
        paint.setColor(color_theme);
        canvas.drawCircle((float) width/2, (float) height / 2, (float) width / 2, paint);
        paint.setColor(color_theme_2);
        canvas.drawCircle((float) width / 2, (float) height / 2, (float) width / 2 - 10, paint);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        sfHolder = surfaceHolder;
        //this.setZOrderOnTop(true);//设置画布  背景透明
        this.width = getWidth();
        this.height = getHeight();
        Canvas canvas = sfHolder.lockCanvas();
        drawEmptyFace(canvas);
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }


}
