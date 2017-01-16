package com.init.cutechat.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

/**
 * Created by Zoson on 16/5/8.
 */
public class MyPopupView extends LinearLayout {
    public MyPopupView(Context context) {
        super(context);
    }

    public MyPopupView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyPopupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyPopupView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void translateLeftToRight(){
        Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,-1.0f,Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,0.0f);
        animation.setFillAfter(true);
        animation.setDuration(200);
        startAnimation(animation);
    }

    public void translateRightToLeft(){

    }

}
