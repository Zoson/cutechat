package com.init.cutechat.Application.activity;

import android.app.Activity;
import android.os.Bundle;

import com.init.cutechat.R;
import com.init.cutechat.ui.ViewVisitor;

/**
 * Created by Zoson on 16/5/8.
 */
public class PersonActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        ViewVisitor.setTranslucent(getWindow());
    }
}
