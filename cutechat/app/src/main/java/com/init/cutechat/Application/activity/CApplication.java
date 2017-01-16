package com.init.cutechat.Application.activity;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.init.cutechat.R;
import com.init.cutechat.domain.context.CContext;
import com.init.cutechat.domain.context.IMChatListener;
import com.init.cutechat.domain.context.FaceChatManager;

/**
 * Created by Zoson on 16/2/5.
 */
public class CApplication extends Application implements IMChatListener {

    FaceChatManager chatManager;
    @Override
    public void onCreate() {
        super.onCreate();
        CContext.init(this);
        if (getCurProcessName().equals(this.getPackageName())){
            CContext.baseContext.initServices();
            CContext.baseContext.startContext();
            chatManager = FaceChatManager.getInstance();
            chatManager.setIMChatListener(this);
        }else if (getCurProcessName().equals(this.getCurProcessName()+":remote")){

        }

    }

    String getCurProcessName() {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) this
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    @Override
    public void getCall(final String sender) {
        System.out.println("sender 来电 "+sender);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, FaceChatActivity.class);
        intent.putExtra(FaceChatActivity.ACCOUNT, sender);
        intent.putExtra(FaceChatActivity.STATE,FaceChatActivity.ACCEPT);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
        Notification notification = new Notification.Builder(this).setTicker(sender+"来电").setContentTitle("FaceChat").setContentText(sender + "来电").setSmallIcon(R.drawable.ico).setContentIntent(pendingIntent).build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(1,notification);
    }

    @Override
    public void getSystemMessage() {

    }
}
