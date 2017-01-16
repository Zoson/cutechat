package com.init.cutechat.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.druson.cycle.utils.ThreadPool;
import com.init.cutechat.R;

import org.opencv.core.Point;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zoson on 3/24/15.
 */
public class FaceView extends SurfaceView implements SurfaceHolder.Callback{
    private final String TAG = "faceView";
    private SurfaceHolder holder;
    private Context context;
    private Canvas canvas;
    private Paint rPaint = new Paint();
    private float center_x;
    private float center_y;
    private Boolean isComplete = true;
    private float x = 0f;
    private float y =0f;
    private int fada = 10;
    private PointF[] m_vPoint;

    private int width;
    private int height;
    private int nictation = 10;

    private int color_theme ;
    private int color_theme_2;

    public FaceView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public FaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public FaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public void init(){
        this.holder = this.getHolder();
        setZOrderOnTop(true);
        holder.setFormat(PixelFormat.TRANSPARENT);
        holder.addCallback(this);
        rPaint = new Paint();
        rPaint.setColor(Color.BLACK);
        center_x = getWidth()/2;
        center_y = getHeight()/2;
        color_theme = context.getResources().getColor(R.color.theme_color);
        color_theme_2 = context.getResources().getColor(R.color.theme_color_2);
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.holder = holder;
        this.width = getWidth();
        this.height = getHeight();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public void DrawPoints(final int[] points) {
        ThreadPool.start(new Runnable() {
            @Override
            public void run() {
                canvas = holder.lockCanvas();
                if (canvas == null) {
                    Log.e(TAG, "canvas is null");
                    return;
                }
                m_vPoint = new PointF[points.length / 2];
                for (int i = 0; i < points.length / 2; i++) {
                    m_vPoint[i] = new PointF(0, 0);
                    m_vPoint[i].x = points[2 * i];
                    m_vPoint[i].y = points[2 * i + 1];

                }

                if (holder == null || canvas == null) {
                    return;
                }
                Paint paint = new Paint();
                paint.setColor(Color.WHITE);
                rPaint.setColor(Color.BLACK);
                //canvas.drawRect(0, 0, getWidth(), getHeight(), paint);

                drawBackground(canvas);

                canvas.drawLine(m_vPoint[0].x, m_vPoint[0].y, m_vPoint[2].x, m_vPoint[2].y, rPaint);
                //脸颊
                Path path_face = new Path();
                path_face.moveTo(m_vPoint[0].x, m_vPoint[0].y);
                for (int i = 0; i < 12; i++) {
                    canvas.drawLine(m_vPoint[i].x, m_vPoint[i].y, m_vPoint[i + 1].x, m_vPoint[i + 1].y, rPaint);
                    //path_face.lineTo(m_vPoint[i].x, m_vPoint[i].y);
                }
                //canvas.drawPath(path_face, rPaint);

                //眉毛
                Path path_eyebrow_left = new Path();
                Paint paint_eyebrow = new Paint();
                paint_eyebrow.setColor(Color.BLACK);
                path_eyebrow_left.moveTo(m_vPoint[16].x, m_vPoint[16].y);
                for (int i = 17; i <= 21; i++) {
                    path_eyebrow_left.lineTo(m_vPoint[i].x, m_vPoint[i].y);
                }
                path_eyebrow_left.moveTo(m_vPoint[16].x, m_vPoint[16].y);
                canvas.drawPath(path_eyebrow_left, paint_eyebrow);

                Path path_eyebrow_right = new Path();
                path_eyebrow_right.moveTo(m_vPoint[22].x, m_vPoint[22].y);
                for (int i = 23; i <= 27; i++) {
                    path_eyebrow_right.lineTo(m_vPoint[i].x, m_vPoint[i].y);
                }
                path_eyebrow_right.lineTo(m_vPoint[22].x, m_vPoint[22].y);
                canvas.drawPath(path_eyebrow_right, paint_eyebrow);

                //鼻子
                Path path_nose = new Path();
                Paint paint_nose = new Paint();
                paint_nose.setColor(Color.GRAY);
                path_nose.moveTo(m_vPoint[21].x, m_vPoint[21].y);
                path_nose.lineTo(m_vPoint[50].x, m_vPoint[50].y);
                path_nose.lineTo(m_vPoint[51].x, m_vPoint[51].y);
                path_nose.lineTo(m_vPoint[56].x, m_vPoint[56].y);
                path_nose.lineTo(m_vPoint[53].x, m_vPoint[53].y);
                path_nose.lineTo(m_vPoint[48].x, m_vPoint[48].y);
                path_nose.lineTo(m_vPoint[22].x, m_vPoint[22].y);
                canvas.drawPath(path_nose, paint_nose);

                //mouth_outer
                Path path_mouth_outer = new Path();
                Paint paint_mouth_outer = new Paint();
                paint_mouth_outer.setColor(Color.RED);
                path_mouth_outer.moveTo(m_vPoint[59].x, m_vPoint[59].y);
                path_mouth_outer.lineTo(m_vPoint[60].x, m_vPoint[60].y);
                path_mouth_outer.lineTo(m_vPoint[61].x, m_vPoint[61].y);
                path_mouth_outer.lineTo(m_vPoint[62].x, m_vPoint[62].y);
                path_mouth_outer.lineTo(m_vPoint[63].x, m_vPoint[63].y);
                path_mouth_outer.lineTo(m_vPoint[64].x, m_vPoint[64].y);
                path_mouth_outer.lineTo(m_vPoint[65].x, m_vPoint[65].y);
                path_mouth_outer.lineTo(m_vPoint[72].x, m_vPoint[72].y);
                path_mouth_outer.lineTo(m_vPoint[73].x, m_vPoint[73].y);
                path_mouth_outer.lineTo(m_vPoint[74].x, m_vPoint[74].y);
                path_mouth_outer.lineTo(m_vPoint[75].x, m_vPoint[75].y);
                path_mouth_outer.lineTo(m_vPoint[76].x, m_vPoint[76].y);
                canvas.drawPath(path_mouth_outer, paint_mouth_outer);

                //mouth_inner
                Path path_mouth_inner = new Path();
                Paint paint_mouse_inner = new Paint();
                paint_mouse_inner.setColor(Color.WHITE);
                path_mouth_inner.moveTo(m_vPoint[59].x, m_vPoint[59].y);
                path_mouth_inner.lineTo(m_vPoint[68].x, m_vPoint[68].y);
                path_mouth_inner.lineTo(m_vPoint[67].x, m_vPoint[67].y);
                path_mouth_inner.lineTo(m_vPoint[66].x, m_vPoint[66].y);
                path_mouth_inner.lineTo(m_vPoint[65].x, m_vPoint[65].y);
                path_mouth_inner.lineTo(m_vPoint[71].x, m_vPoint[71].y);
                path_mouth_inner.lineTo(m_vPoint[70].x, m_vPoint[70].y);
                path_mouth_inner.lineTo(m_vPoint[69].x, m_vPoint[69].y);
                canvas.drawPath(path_mouth_inner, paint_mouse_inner);

                //eye
                Paint paint_eye = new Paint();
                paint_eye.setColor(Color.WHITE);
                Path path_eye_left = new Path();
                path_eye_left.moveTo(m_vPoint[30].x, m_vPoint[30].y);
                for (int i = 31; i <= 37; i++) {
                    path_eye_left.lineTo(m_vPoint[i].x, m_vPoint[i].y);
                    canvas.drawLine(m_vPoint[i - 1].x, m_vPoint[i - 1].y, m_vPoint[i].x, m_vPoint[i].y, rPaint);
                }
                canvas.drawPath(path_eye_left, paint_eye);
                canvas.drawCircle(m_vPoint[38].x, m_vPoint[38].y, m_vPoint[38].y - m_vPoint[32].y, rPaint);


                Path path_eye_right = new Path();
                path_eye_right.moveTo(m_vPoint[40].x, m_vPoint[40].y);
                for (int i = 41; i <= 47; i++) {
                    path_eye_right.lineTo(m_vPoint[i].x, m_vPoint[i].y);
                    canvas.drawLine(m_vPoint[i - 1].x, m_vPoint[i - 1].y, m_vPoint[i].x, m_vPoint[i].y, rPaint);
                }
                canvas.drawPath(path_eye_right, paint_eye);
                canvas.drawCircle(m_vPoint[39].x, m_vPoint[39].y, m_vPoint[39].y - m_vPoint[42].y, rPaint);

                holder.unlockCanvasAndPost(canvas);
            }
        });
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

    public PointF getPoint(PointF point,PointF point1,float a){
        PointF newP = new PointF();
        float k = (point.y-point1.y)/(point.x-point1.x);
        float b = point.y - k*point.x;
        float y = k*a+b;
        newP.x = a;
        newP.y = y;
        return newP;
    }


}
