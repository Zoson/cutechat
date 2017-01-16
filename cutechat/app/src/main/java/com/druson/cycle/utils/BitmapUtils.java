package com.druson.cycle.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.widget.ImageView;

import com.init.cutechat.R;

/**
 * Created by Zoson on 16/5/1.
 */
public class BitmapUtils {

    static {
        System.loadLibrary("bitmaputils");
    }
    public static native int[] stackBlur(int[] pixs,int w,int h,int radius);


}
