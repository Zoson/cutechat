package com.init.cutechat.Application.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.init.cutechat.R;
import com.init.cutechat.domain.context.CCallback;
import com.init.cutechat.domain.context.CContext;
import com.init.cutechat.domain.context.LaunchManager;
import com.init.cutechat.ui.ViewVisitor;

/**
 * Created by Zoson on 16/5/10.
 */
public class LoadingActivity extends Activity{

    private LaunchManager launchManager ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewVisitor.setBackground(this, getWindow().getDecorView(), R.drawable.background);
        overridePendingTransition(R.anim.opacity_0_1, R.anim.opacity_1_0);
        launchManager = LaunchManager.get();
//        launchManager.tryAutoLogin(new CCallback() {
//            @Override
//            public void response(int state, Object ob) {
//                if (ob !=null) Toast.makeText(LoadingActivity.this,ob+"",Toast.LENGTH_SHORT).show();
//                switch (state) {
//                    case SUCC:
//                        Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
//                        startActivity(intent);
//                        break;
//                    case FAIL:
//
//                        break;
//                }
//            }
//        });
        Intent intent1 = new Intent(LoadingActivity.this,LoginActivity.class);
        startActivity(intent1);

    }

}
