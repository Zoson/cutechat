package com.init.cutechat.Application.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.init.cutechat.R;
import com.init.cutechat.domain.context.CCallback;
import com.init.cutechat.domain.context.FaceChatSessionListener;
import com.init.cutechat.domain.context.FaceChatManager;
import com.init.cutechat.domain.context.FaceChatSession;
import com.init.cutechat.domain.enity.Face;
import com.init.cutechat.ui.CuteFaceView;
import com.init.cutechat.ui.FaceChatLayout;
import com.init.cutechat.ui.FaceView;
import com.init.cutechat.ui.ViewVisitor;

/**
 * Created by druson on 2016/3/8.
 */
public class FaceChatActivity extends Activity{

    FaceChatLayout rl_faceChat;
    CuteFaceView cfv_it;
    CuteFaceView cfv_me;
    FaceView fv_it;
    SurfaceView sf_camera;
    TextView tv_state;
    Button bt_talk;
    ImageView im_itvoice;
    //ImageView im_mevoice;
    public static final  String ACCOUNT = "account";
    public static final String STATE = "state";
    public static final String CALLING = "calling";
    public static final String ACCEPT = "accept";
    FaceChatManager manager;
    FaceChatSession session;

    Animation alpha_0_1;
    Animation alpha_1_0;
    Bitmap itsCommentlight;
    Bitmap itsCommentdark;
    //Bitmap myCommentlight;
    //Bitmap myCommentdark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facechat);
        findView();
        setListener();
        initData();
        overridePendingTransition(R.anim.opacity_0_1, R.anim.opacity_1_0);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void findView(){
        rl_faceChat = (FaceChatLayout)getWindow().getDecorView().findViewById(R.id.rl_facechat);
        cfv_it = (CuteFaceView)findViewById(R.id.cfv_it);
        cfv_me = (CuteFaceView)findViewById(R.id.cfv_me);
        bt_talk = (Button)findViewById(R.id.bt_home);
        tv_state = (TextView)findViewById(R.id.tv_state);
        im_itvoice = (ImageView)findViewById(R.id.im_itvoice);
        fv_it = (FaceView)findViewById(R.id.fv_it);
        sf_camera = (SurfaceView)findViewById(R.id.sf_camera);
        //im_mevoice = (ImageView)findViewById(R.id.im_mevoice);


        ViewVisitor.setTranslucent(getWindow());
        ViewVisitor.setBackground(this, rl_faceChat, R.drawable.background);
        itsCommentdark = BitmapFactory.decodeResource(getResources(),R.drawable.hiscommentdark);
        itsCommentlight = BitmapFactory.decodeResource(getResources(),R.drawable.hiscommentlight);
//        myCommentdark = BitmapFactory.decodeResource(getResources(),R.drawable.hiscommentdark);
//        myCommentlight = BitmapFactory.decodeResource(getResources(),R.drawable.hiscommentlight);
        alpha_0_1 = AnimationUtils.loadAnimation(this, R.anim.opacity_0_1);
        alpha_1_0 = AnimationUtils.loadAnimation(this,R.anim.opacity_1_0);
    }

    private void setListener(){
        im_itvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.playItsVoice();
                //im_itvoice.setVisibility(View.INVISIBLE);
                im_itvoice.setImageBitmap(itsCommentdark);
            }
        });
//        im_mevoice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                session.playMyVoice();
//                //im_mevoice.setVisibility(View.INVISIBLE);
//                im_mevoice.setImageBitmap(myCommentdark);
//            }
//        });
        bt_talk.setOnTouchListener(new View.OnTouchListener() {
            boolean passed = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!passed) {
                            session.recordVoice();
                            passed = true;
                            //im_mevoice.setImageBitmap(myCommentdark);
                            //im_mevoice.startAnimation(alpha_0_1);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_OUTSIDE:
                        if (passed) {
                            session.stopRecord();
                        }
                        passed = false;
                        break;
                }
                return true;
            }
        });
    }

    private void initData(){
        Intent intent = getIntent();
        String account = intent.getStringExtra(FaceChatActivity.ACCOUNT);
        String state = intent.getStringExtra(FaceChatActivity.STATE);

        System.out.println("FaceChatActivity account "+account+" state "+state);
        if (account==null||account.equals("null")||account.equals("")){
            Toast.makeText(this,"没有此用户",Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (state == null||state.equals("null")||state.equals("")){
            Toast.makeText(this,"非法启动",Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        manager = FaceChatManager.getInstance();
        if (state.equals(CALLING)){
            session = manager.createSession(account, new ChatListener());
        }else if(state.equals(ACCEPT)){
            System.out.println("FaceChatActivity accept");
            session = manager.acceptCaller(account);
            session.setChatListener(new ChatListener());
            sf_camera.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    manager.startFaceIdentify(sf_camera.getHolder(), new CCallback() {
                        @Override
                        public void response(int state, Object ob) {
                        }
                    });
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {

                }
            });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        session.startSession();
    }

    @Override
    protected void onStop() {
        super.onStop();
        session.closeSession();
        manager.stopFaceIdentify();
    }


    class ChatListener implements FaceChatSessionListener {

        @Override
        public void sendVoiceSucc() {
            Toast.makeText(FaceChatActivity.this,"sendVoiceSucc",Toast.LENGTH_SHORT).show();
//            im_mevoice.setVisibility(View.VISIBLE);
//            im_mevoice.setImageBitmap(myCommentlight);
//            im_mevoice.startAnimation(alpha_0_1);
        }

        @Override
        public void sendVoiceing(int process) {
            Toast.makeText(FaceChatActivity.this,"sendVoiceing "+process,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void sendVoiceFail(String error) {
            Toast.makeText(FaceChatActivity.this,"sendVoiceFail",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void recVoiceSucc(String path) {
            Toast.makeText(FaceChatActivity.this,"recVoiceSucc",Toast.LENGTH_SHORT).show();
            tv_state.setText("对方说完了");
            im_itvoice.setVisibility(View.VISIBLE);
            im_itvoice.setImageBitmap(itsCommentlight);
            im_itvoice.startAnimation(alpha_0_1);
        }

        @Override
        public void recVoiceFail() {
            Toast.makeText(FaceChatActivity.this,"recVoiceFail",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void recVoiceing(int process) {
            Toast.makeText(FaceChatActivity.this,"recVoiceing",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void sendTxtSucc() {
            Toast.makeText(FaceChatActivity.this,"sendTxtSucc",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void sendTxtFail(String error) {
            Toast.makeText(FaceChatActivity.this,"sendTxtFail",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void recTxtSucc(String content) {
            Toast.makeText(FaceChatActivity.this,"recTxtSucc",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void callSucc() {
            Toast.makeText(FaceChatActivity.this,"callSucc",Toast.LENGTH_SHORT).show();
            tv_state.setText("成功拨通");
            manager.startFaceIdentify(sf_camera.getHolder(), new CCallback() {
                @Override
                public void response(int state, Object ob) {

                }
            });
        }

        @Override
        public void hangUp() {
            Toast.makeText(FaceChatActivity.this,"hangUp",Toast.LENGTH_SHORT).show();
            FaceChatActivity.this.finish();
        }

        @Override
        public void itSpeaking() {
            Toast.makeText(FaceChatActivity.this,"itSpeaking",Toast.LENGTH_SHORT).show();
            tv_state.setText("对方正在说话中");
        }

        @Override
        public void recEmoji(Face face) {
            fv_it.DrawPoints(face.getPoints());
        }

        @Override
        public void connectError() {
            Toast.makeText(FaceChatActivity.this,"connectError",Toast.LENGTH_SHORT).show();
        }

        public void tryCalling(){
            Toast.makeText(FaceChatActivity.this,"tryCalling",Toast.LENGTH_SHORT).show();
            tv_state.setText("正在拨通中");
        }

        public void callFail(){
            Toast.makeText(FaceChatActivity.this,"callFail",Toast.LENGTH_SHORT).show();
            FaceChatActivity.this.finish();
        }

        public void disconnect(){

        }
    }



}
