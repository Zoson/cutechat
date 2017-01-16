package com.init.cutechat.Application.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.init.cutechat.R;
import com.init.cutechat.ui.MyPopupView;

/**
 * Created by Zoson on 16/5/8.
 */
public class MyPopupWindow extends PopupWindow {

    RelativeLayout rl_person;
    RelativeLayout rl_face_test;
    RelativeLayout rl_voice_test;
    RelativeLayout rl_record;
    RelativeLayout rl_settings;
    RelativeLayout rl_exit;
    MyPopupView rootView;
    Context context;
    public MyPopupWindow(Context context,MyPopupView view){
        super(view, WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.MATCH_PARENT);
        rootView = view;
        this.context = context;
        findView();
        setListener();
        initData();
    }

    private void findView(){
        rl_exit = (RelativeLayout)rootView.findViewById(R.id.rl_exit);
        rl_face_test = (RelativeLayout)rootView.findViewById(R.id.rl_face_test);
        rl_voice_test = (RelativeLayout)rootView.findViewById(R.id.rl_voice_test);
        rl_record = (RelativeLayout)rootView.findViewById(R.id.rl_record);
        rl_settings = (RelativeLayout)rootView.findViewById(R.id.rl_settings);
        rl_person = (RelativeLayout)rootView.findViewById(R.id.rl_person);
    }

    private void setListener(){
        rl_face_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,FaceTestActivity.class);
                context.startActivity(intent);
            }
        });
        rl_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,SettingActivity.class);
                context.startActivity(intent);
            }
        });
        rl_voice_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,SoundChangeActivity.class);
                context.startActivity(intent);
            }
        });
        rl_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,PersonActivity.class);
                context.startActivity(intent);
            }
        });
    }

    private void initData(){

    }
}
