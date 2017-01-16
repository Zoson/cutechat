package com.init.cutechat.Application.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.init.cutechat.domain.context.CContext;
import com.init.cutechat.domain.context.ServiceManager;

/**
 * Created by Zoson on 16/5/9.
 */
public class MsgClient extends Service {

    private IMServiceProxy imServiceProxy;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        imServiceProxy = (IMServiceProxy) CContext.baseContext.getServiceManager().getService(ServiceManager.MSGSERVICE);
        return imServiceProxy;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (imServiceProxy!=null){
            imServiceProxy.unbindServer();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

}
