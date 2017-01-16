package com.init.cutechat.Application.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.init.cutechat.R;
import com.init.cutechat.ui.ViewVisitor;

/**
 * Created by Zoson on 16/5/2.
 */
public class SettingActivity extends Activity {

    LinearLayout ll_setting;
    RelativeLayout rl_call_alert;
    RelativeLayout rl_call_music;
    RelativeLayout rl_auto_play;
    RelativeLayout rl_clean_cahce;
    RelativeLayout rl_more;
    RelativeLayout rl_about;
    RelativeLayout rl_exit;
    Switch sw_call_alert;
    Switch sw_call_music;
    Switch sw_auto_play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        findView();
        setListener();
        initData();
    }

    private void findView(){
        ll_setting = (LinearLayout)findViewById(R.id.ll_setting);
        rl_more = (RelativeLayout)findViewById(R.id.rl_more);
        rl_call_alert = (RelativeLayout)findViewById(R.id.rl_call_alert);
        rl_call_music = (RelativeLayout)findViewById(R.id.rl_call_music);
        rl_auto_play = (RelativeLayout)findViewById(R.id.rl_auto_play);
        rl_about = (RelativeLayout)findViewById(R.id.rl_about);
        rl_exit = (RelativeLayout)findViewById(R.id.rl_exit);
        rl_clean_cahce = (RelativeLayout)findViewById(R.id.rl_clean_cache);
        ViewVisitor.setTranslucent(getWindow());

        sw_auto_play = (Switch)findViewById(R.id.sw_auto_play);
        sw_call_alert = (Switch)findViewById(R.id.sw_call_alert);
        sw_call_music = (Switch)findViewById(R.id.sw_call_music);
        //ViewVisitor.setBackground(this, ll_setting, R.drawable.background);
        ViewVisitor.setTranslucent(getWindow());
    }

    private void setListener(){
        rl_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        rl_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        rl_clean_cahce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        rl_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        rl_call_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        rl_call_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        rl_auto_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        sw_auto_play.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        sw_call_music.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        sw_call_alert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
    }

    private void initData(){

    }

}
