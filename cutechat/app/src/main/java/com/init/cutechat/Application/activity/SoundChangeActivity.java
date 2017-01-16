package com.init.cutechat.Application.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.init.cutechat.R;
import com.init.cutechat.domain.context.SoundManager;
import com.init.cutechat.domain.enity.SoundAttr;
import com.init.cutechat.ui.ViewVisitor;

/**
 * Created by Zoson on 16/4/29.
 */
public class SoundChangeActivity extends Activity {

    SeekBar sb_pitch;
    SeekBar sb_rate;
    SeekBar sb_tempo;
    Button bt_start;
    Button bt_replay;
    SoundManager soundManager;
    SoundAttr soundAttr;
    RelativeLayout rl_sound_change;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_sound);
        findView();
        setListener();
        initDate();
    }

    private void findView(){
        sb_pitch = (SeekBar)findViewById(R.id.sb_pitch);
        sb_rate = (SeekBar)findViewById(R.id.sb_rate);
        sb_tempo = (SeekBar)findViewById(R.id.sb_tempo);
        bt_start = (Button)findViewById(R.id.bt_home);
        bt_replay = (Button)findViewById(R.id.bt_other);
        rl_sound_change = (RelativeLayout)findViewById(R.id.rl_sound_change);
        ViewVisitor.setBackground(this,rl_sound_change,R.drawable.background);
        ViewVisitor.setTranslucent(getWindow());
    }

    private void setListener(){
        bt_start.setOnTouchListener(new View.OnTouchListener() {
            boolean passed = false;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        if (!passed){
                            setData();
                            soundManager.start();
                            passed = true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_OUTSIDE:
                        if (passed){
                            soundManager.stop();
                        }
                        passed = false;
                        break;
                }
                return true;
            }
        });

        bt_replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();
                soundManager.replay();
            }
        });
    }

    public void setData(){
        int rate = sb_rate.getProgress();
        int tempo = sb_tempo.getProgress();
        int pitch = sb_pitch.getProgress();
        soundManager.setSoundArr(pitch,rate,tempo);

    }

    private void initDate(){
        soundManager = new SoundManager();
        soundAttr = soundManager.getSoundAttr();
        sb_pitch.setProgress((int)soundAttr.newPitch);
        sb_tempo.setProgress((int)soundAttr.newTempo);
        sb_rate.setProgress((int)soundAttr.newRate);
    }
}
