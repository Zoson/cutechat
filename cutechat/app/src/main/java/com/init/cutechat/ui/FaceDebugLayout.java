package com.init.cutechat.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.init.cutechat.R;

/**
 * Created by Zoson on 16/2/5.
 */
public class FaceDebugLayout extends RelativeLayout {
    private Context context;
    private FaceView mFaceView;
    private SurfaceView mCameraView;
    private Button mBt_controll;
    public FaceDebugLayout(Context context) {
        super(context);
    }

    public FaceDebugLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FaceDebugLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FaceDebugLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

}
