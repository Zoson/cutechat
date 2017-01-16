package com.init.cutechat.Application.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.init.cutechat.R;
import com.init.cutechat.domain.context.CCallback;
import com.init.cutechat.domain.context.InfoProvider;
import com.init.cutechat.domain.context.LaunchManager;
import com.init.cutechat.domain.enity.User;
import com.init.cutechat.ui.LoginView;
import com.init.cutechat.ui.ViewVisitor;

/**
 * Created by Zoson on 16/2/15.
 */
public class LoginActivity extends Activity {

    private TextView bt_login;
    private EditText et_account;
    private EditText et_password;
    private LoginView loginView;
    private TextView bt_reg;
    private LaunchManager launchManager;
    private InfoProvider infoProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findView();
        setListener();
        initData();
    }

    private void findView(){
        bt_login = (TextView)findViewById(R.id.bt_login);
        bt_reg = (TextView)findViewById(R.id.bt_register);
        et_account = (EditText)findViewById(R.id.et_account);
        et_password = (EditText)findViewById(R.id.et_password);
        et_account.setText("zzx");
        et_password.setText("zzx");
        loginView = (LoginView)findViewById(R.id.loginView);
        ViewVisitor.setBackground(this,loginView,R.drawable.background);
        overridePendingTransition(R.anim.opacity_0_1, R.anim.opacity_1_0);
        ViewVisitor.setTranslucent(getWindow());
    }

    private void setListener(){
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("bt_loginclcik");
                String account = et_account.getText().toString();
                String password = et_password.getText().toString();
                if (account.isEmpty()||password.isEmpty()){
                    Toast.makeText(LoginActivity.this,"请填写完整",Toast.LENGTH_SHORT).show();
                    return;
                }
                launchManager.tryLogin(account, password, new CCallback() {
                    @Override
                    public void response(int state, Object ob) {
                        Toast.makeText(LoginActivity.this,""+ob,Toast.LENGTH_SHORT).show();
                        if (state==SUCC){
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });

            }
        });
        bt_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("bt_reg");
                String account = et_account.getText().toString();
                String password = et_password.getText().toString();
                if (account.isEmpty()||password.isEmpty()){
                    Toast.makeText(LoginActivity.this,"请填写完整",Toast.LENGTH_SHORT).show();
                    return;
                }
                launchManager.tryReg(account, password, new CCallback() {
                    @Override
                    public void response(int state, Object ob) {
                        Toast.makeText(LoginActivity.this,""+ob,Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }

    private void initData(){
        launchManager = LaunchManager.get();
        infoProvider = InfoProvider.get();
        User user = infoProvider.getMasterUser();
        if (user !=null){
            et_account.setText(user.getAccount());
            et_password.setText(user.getPassword());
        }
    }
}
