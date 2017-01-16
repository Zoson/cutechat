package com.init.cutechat.Application.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.init.cutechat.R;
import com.init.cutechat.domain.context.CCallback;
import com.init.cutechat.domain.context.FaceChatManager;
import com.init.cutechat.domain.enity.Face;
import com.init.cutechat.ui.CuteFaceView;
import com.init.cutechat.ui.FaceTestLayout;
import com.init.cutechat.ui.FaceView;
import com.init.cutechat.ui.ViewVisitor;
import com.init.cutechat.ui.component.HorizontalListView;

/**
 * Created by Zoson on 16/4/25.
 */
public class FaceTestActivity extends Activity {

    CuteFaceView cfv_test;
    SurfaceView sf_camera;
    Button bt_controll;
    HorizontalListView hlv_choice;
    FaceView fv_points;
    FaceChatManager faceManager;
    FaceidentifyGet faceidentifyGet;
    FeaturesAdapter featuresAdapter;
    FaceTestLayout faceTestLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facetest);
        findView();
        setListener();
        initData();
    }

    private void findView(){
        cfv_test = (CuteFaceView)findViewById(R.id.cfv_test);
        sf_camera = (SurfaceView)findViewById(R.id.fv_test);
        bt_controll = (Button)findViewById(R.id.bt_home);
        hlv_choice = (HorizontalListView)findViewById(R.id.lv_choice);
        fv_points = (FaceView)findViewById(R.id.fv_points);
        faceTestLayout = (FaceTestLayout)findViewById(R.id.faceTestLayout);
        ViewVisitor.setBackground(this,faceTestLayout,R.drawable.background);
        ViewVisitor.setTranslucent(getWindow());
    }

    private void setListener(){
        bt_controll.setOnClickListener(new View.OnClickListener() {
            boolean isChecked = true;
            @Override
            public void onClick(View view) {
                if (isChecked){
                    faceManager.stopFaceIdentify();
                    isChecked = false;
                }else{
                    faceManager.startFaceIdentify(sf_camera.getHolder(),faceidentifyGet);
                    isChecked = true;
                }
            }
        });
        sf_camera.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                faceManager.startFaceIdentify(holder, faceidentifyGet);

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }

    private void initData(){
        faceidentifyGet = new FaceidentifyGet();
        faceManager = FaceChatManager.getInstance();
        featuresAdapter = new FeaturesAdapter(this, faceManager.getFeaturesSet(), new FeaturesAdapter.Callback() {
            @Override
            public void drawFace(Face face) {
                cfv_test.drawCuteFace(face);
            }
        });
        hlv_choice.setAdapter(featuresAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        if (sf_camera.getHolder()!=null){
//            faceManager.startFaceIdentify(sf_camera.getHolder(),faceidentifyGet);
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //faceManager.stopFaceIdentify();
    }

    class FaceidentifyGet implements CCallback{
        @Override
        public void response(int state, Object ob) {
            Face face = (Face)ob;
            fv_points.DrawPoints(face.getPoints());
            //cfv_facechat.drawCuteFace((Face)ob);
        }
    }
}
