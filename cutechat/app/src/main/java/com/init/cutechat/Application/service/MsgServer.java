package com.init.cutechat.Application.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.druson.cycle.ipc.IMsgClient;
import com.druson.cycle.service.comm.IMListener;
import com.druson.cycle.service.comm.IMMsg;
import com.druson.cycle.service.comm.IMService;


/**
 * Created by Zoson on 16/5/9.
 */
public class MsgServer extends Service implements IMListener {
    public static final String TAG = MsgServer.class.getSimpleName();

    public final static String SIGNAL = "signal";
    public final static String SERVER_INIT = "init";
    public final static String WIFI_STATE_ENABLE = "wifi_enable";
    public final static String WIFI_STATE_DISABLE = "wifi_disable";

    private IMServiceStub msgHandler = new IMServiceStub();
    private IMsgClient msgClient = null;
    private IMService imService;
    private WifiManager wifiManager;
    private ServiceConnection serviceConnection;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        imService = imService.init();
        imService.addListener(TAG, this);
        if (wifiManager.getWifiState()==WifiManager.WIFI_STATE_ENABLED){
            connectIMService();
        }else{
            Toast.makeText(this,"网络WIFI断开,无法连接",Toast.LENGTH_SHORT).show();
        }
        startClient();
        return msgHandler;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent!=null){
            String signal = intent.getStringExtra(SIGNAL);
            switch (signal){
                case SERVER_INIT:
                    imService = IMService.init();
                    imService.addListener(TAG,this);
                    break;
                case WIFI_STATE_ENABLE:
                    connectIMService();
                    startClient();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    protected void connectIMService(){
        if (imService==null)imService = IMService.init();
//        if (!imService.isConnected()){
//            SharedPreferences sp = (SharedPreferences)CContext.baseContext.getAppService(ServiceManager.SHAREPREFERENCE);
//            boolean autologin = sp.getBoolean("autologin", false);
//            if (autologin) {
//                final String account = sp.getString("account", null);
//                final String password = sp.getString("password", null);
//                if (account != null && password != null) {
//                    ThreadPool.start(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                msgHandler.login(account, password);
//                            } catch (RemoteException e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//                    });
//                }
//            }
//        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("MsgServer destory");
        if (msgClient!=null){
            try {
                msgClient.disconnect("网络断开");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        if (serviceConnection !=null){
            unbindService(serviceConnection);
        }
    }

    public void startClient(){
        startClient(null);
    }

    public void startClient(final Runnable runnable){
        if (msgClient == null){
            Intent intent1 = new Intent(this,MsgClient.class);
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    msgClient = IMServiceProxy.asInterface(service);
                    msgHandler.addClient(msgClient);
                    if (runnable != null)runnable.run();
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            };
            bindService(intent1, serviceConnection,Service.BIND_AUTO_CREATE);
        }else {
            if (runnable!=null){
                runnable.run();
            }
        }
    }


    @Override
    public boolean getMessage(final IMMsg imMsg) {
        startClient(new Runnable() {
            @Override
            public void run() {
                try {
                    msgClient.getMessage(imMsg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        return true;
    }

}
