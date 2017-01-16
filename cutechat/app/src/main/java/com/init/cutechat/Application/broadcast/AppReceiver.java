package com.init.cutechat.Application.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.widget.Toast;

import com.init.cutechat.Application.service.MsgServer;

/**
 * Created by Zoson on 16/5/9.
 */
public class AppReceiver extends BroadcastReceiver {

    public final static String BOOT_ACTION = "android.intent.action.BOOT_COMPLETED";
    public final static String CONN_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("App Receiver action "+intent.getAction());

        switch (intent.getAction()){
            case Intent.ACTION_MEDIA_MOUNTED:
            case Intent.ACTION_MEDIA_EJECT:
            case BOOT_ACTION:
                System.out.println("App Receiver action "+intent.getAction()+" init MsgServer");
                Intent intent1 = new Intent(context,MsgServer.class);
                intent1.putExtra(MsgServer.SIGNAL,MsgServer.SERVER_INIT);
                context.startService(intent1);
                break;
            case CONN_CHANGE_ACTION:
                Parcelable parcelableExtra = intent
                        .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (null != parcelableExtra) {
                    NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                    NetworkInfo.State state = networkInfo.getState();
                    boolean isConnected = state == NetworkInfo.State.CONNECTED;// 当然，这边可以更精确的确定状态
                    if (isConnected) {
                        System.out.println("App Receiver action "+intent.getAction()+" start MsgServer");
                        Intent intent2 = new Intent(context, MsgServer.class);
                        intent2.putExtra(MsgServer.SIGNAL,MsgServer.WIFI_STATE_ENABLE);
                        context.startService(intent2);
                    } else {
//                        System.out.println("App Receiver action "+intent.getAction()+" stop MsgServer");
//                        Intent intent2 = new Intent(context, MsgServer.class);
//                        intent2.putExtra(MsgServer.SIGNAL,MsgServer.WIFI_STATE_DISABLE);
                        //context.stopService(intent2);
                    }
                }
                break;
        }

    }
}