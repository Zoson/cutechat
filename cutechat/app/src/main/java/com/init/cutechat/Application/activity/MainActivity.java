package com.init.cutechat.Application.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.init.cutechat.R;
import com.init.cutechat.domain.context.InfoProvider;
import com.init.cutechat.ui.MyPopupView;
import com.init.cutechat.ui.ViewVisitor;

import java.util.List;

/**
 * Created by Zoson on 16/4/30.
 */
public class MainActivity extends Activity {

    RelativeLayout ll_main;
    ListView lv_names;
    InfoProvider infoProvider;
    List<String> ls_accounts;
    //FaceChatManager chatManager;
    Button bt_popupview;
    MyPopupView myPopupView;
    MyPopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        findView();
        setListener();
        initData();
    }

    public void findView(){
        lv_names = (ListView)findViewById(R.id.lv_names);
        ll_main = (RelativeLayout)findViewById(R.id.ll_Main);
        bt_popupview = (Button)findViewById(R.id.bt_popupview);
        ViewVisitor.setBackground(this, ll_main, R.drawable.background);
        overridePendingTransition(R.anim.opacity_0_1, R.anim.opacity_1_0);
        ViewVisitor.setTranslucent(getWindow());

    }

    public void setListener(){
        lv_names.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("on item click");
                String account = ls_accounts.get(position);
                Intent intent = new Intent(MainActivity.this,FaceChatActivity.class);
                intent.putExtra(FaceChatActivity.STATE,FaceChatActivity.CALLING);
                intent.putExtra(FaceChatActivity.ACCOUNT,account);
                startActivity(intent);
            }
        });

        bt_popupview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myPopupView==null){
                    myPopupView = (MyPopupView)LayoutInflater.from(MainActivity.this).inflate(R.layout.popupwindow_mainpage,null);
                }
                if (popupWindow==null){
                    popupWindow = new MyPopupWindow(MainActivity.this,myPopupView);
                    popupWindow.setFocusable(true);
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.setTouchable(true);
                }
                getWindow().getDecorView().setAlpha(0.6f);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        getWindow().getDecorView().setAlpha(1.0f);
                    }
                });
                myPopupView.translateLeftToRight();
                popupWindow.showAtLocation(v.getRootView(), Gravity.NO_GRAVITY, 0, 0);
            }
        });
    }

    public void initData(){
        infoProvider = InfoProvider.get();
        ls_accounts = infoProvider.getAccounts();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.listitem_test,R.id.text1,ls_accounts);
        lv_names.setAdapter(arrayAdapter);
    }

}
