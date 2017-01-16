package com.init.cutechat.domain.context;

import android.content.SharedPreferences;
import com.druson.cycle.service.comm.IMCallback;
import com.init.cutechat.Application.service.IMServiceProxy;
import com.init.cutechat.domain.enity.ContextData;
import com.init.cutechat.domain.enity.User;

/**
 * Created by druson on 2016/2/25.
 */
public class LaunchManager extends CContext {

    private ContextData mContextData;
    private IMServiceProxy mImService;
    private SharedPreferences sharedPreferences;
    private User mMasteruser;
    private InfoProvider infoProvider;
    private static LaunchManager intance = new LaunchManager();
    public static LaunchManager get(){
        return intance;
    }

    public LaunchManager(){
        mImService = (IMServiceProxy)getAppService(ServiceManager.MSGSERVICE);
        mContextData = getContextData();
        sharedPreferences = (SharedPreferences)getAppService(ServiceManager.SHAREPREFERENCE);
        infoProvider = InfoProvider.get();
    }

    public void tryAutoLogin(final CCallback cCallback){
        mMasteruser = infoProvider.getMasterUser();
        if (mMasteruser != null){
            mImService.login(mMasteruser.getAccount(), mMasteruser.getPassword(), new IMCallback() {
                @Override
                public void opResult(int state, String msg) {
                    if (state==SUCC){
                        cCallback.response(CCallback.SUCC,"登录成功");
                    }else{
                        cCallback.response(CCallback.FAIL,msg);
                    }
                }
            });
        }else{
            cCallback.response(CCallback.FAIL,null);
        }
    }

    public void tryLogin(final String account, final String password, final CCallback cCallback){
        mImService.login(account, password, new IMCallback() {
            @Override
            public void opResult(int state, String msg) {
                if (state==IMCallback.SUCC){
                    User user = new User();
                    user.setAccount(account);
                    user.setPassword(password);
                    infoProvider.setMasterUser(user);
                    cCallback.response(CCallback.SUCC, "登录成功");
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("autologin",true);
                    editor.putString("account",account);
                    editor.putString("password",password);
                    editor.commit();
                }else{
                    cCallback.response(CCallback.FAIL,msg);
                }
            }
        });
    }

    public void tryReg(final String account,final String password, final CCallback callback){
        mImService.register(account, password, new IMCallback() {
            @Override
            public void opResult(int state, String msg) {
                if (state==IMCallback.SUCC) {
                    createUser(account, password);
                    callback.response(CCallback.SUCC, "注册成功");
                } else {
                    callback.response(CCallback.FAIL, msg);
                }
            }
        });
    }

    protected void createUser(String account,String password){
        mMasteruser = new User(null);
        mMasteruser.setPassword(password);
        mMasteruser.setAccount(account);
        infoProvider.setMasterUser(mMasteruser);
    }

}
