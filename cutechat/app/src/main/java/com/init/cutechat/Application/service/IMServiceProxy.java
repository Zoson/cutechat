package com.init.cutechat.Application.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.widget.Toast;

import com.druson.cycle.ipc.IMsgClient;
import com.druson.cycle.ipc.IMsgServer;
import com.druson.cycle.service.comm.IMCallback;
import com.druson.cycle.service.comm.IMListener;
import com.druson.cycle.service.comm.IMMsg;
import com.druson.cycle.service.comm.IMOpState;
import com.druson.cycle.utils.ThreadPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Zoson on 16/5/9.
 */
public class IMServiceProxy extends IMsgClient.Stub{
    private IMListener grobalListener;
    private IMListener systemListener;
    private Map<String,IMListener> listeners;
    private IMsgServer stub;
    private Context mContext;
    private String path ;
    private ServiceConnection serviceConnection;

    public final static int OP = 0x1;
    public final static int MSG = 0x2;

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case OP:
                    IMOpState imOpState = (IMOpState)msg.obj;
                    if (imOpState.imCallback == null)return;
                    imOpState.imCallback.opResult(imOpState.state,imOpState.result);
                    break;
                case MSG:
                    IMMsg imMsg = (IMMsg)msg.obj;
                    notifyListeners(imMsg);
                    break;
            }
        }
    };

    public IMServiceProxy(Context context) {
        this.mContext = context;
        listeners = new HashMap<>();
        startServer();
    }

    public void startServer(){
        synchronized (IMServiceProxy.this){
            Intent intent = new Intent(mContext, MsgServer.class);
            ServiceConnection serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    IMsgServer msgServer = IMServiceStub.asInterface(service);
                    stub = msgServer;
                    try {
                        stub.setPath(path);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            };
            mContext.bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE);
        }
    }


    public void sendMessage(String sender, String content,IMCallback imCallback) {
        System.out.println("IMServiceProxy sender "+sender + " content "+content);
        ThreadPool.start(new SendMessageRunnable(sender, content, imCallback));
    }

    class SendMessageRunnable implements Runnable{
        String sender;
        String content;
        IMCallback imCallback;
        public SendMessageRunnable(String sender,String content,IMCallback imCallback){
            this.content = content;
            this.sender = sender;
            this.imCallback = imCallback;
        }
        @Override
        public void run() {
            Message msg = Message.obtain();
            msg.what = OP;
            try {
                IMOpState imOpState = stub.sendMessage(sender, content);
                msg.obj = imOpState;
                imOpState.imCallback = imCallback;
            } catch (RemoteException e) {
                e.printStackTrace();
                IMOpState imOpState = genImOpError();
                imOpState.imCallback = imCallback;
                msg.obj = imOpState;

            }
            handler.sendMessage(msg);
        }
    }

    public void sendFile(String sender, String filepath,IMCallback imCallback) {
        ThreadPool.start(new SendFileRunnable(sender,filepath,imCallback));
    }

    class SendFileRunnable implements Runnable{

        String sender;
        String filepath;
        IMCallback imCallback;
        public SendFileRunnable(String sender,String filepath,IMCallback imCallback){
            this.sender = sender;
            this.filepath = filepath;
            this.imCallback = imCallback;
        }
        @Override
        public void run() {
            Message msg = Message.obtain();
            msg.what = OP;
            try {
                IMOpState imOpState = stub.sendFile(sender, filepath);
                msg.obj = imOpState;
                imOpState.imCallback = imCallback;
            } catch (RemoteException e) {
                e.printStackTrace();
                IMOpState imOpState = genImOpError();
                imOpState.imCallback = imCallback;
                msg.obj = imOpState;
            }
            handler.sendMessage(msg);

        }
    }

    public void login(String account, String password,IMCallback imCallback){
        ThreadPool.start(new LoginRunnable(account,password,imCallback));
    }

    class LoginRunnable implements Runnable{

        String account;
        String password;
        IMCallback imCallback;
        public LoginRunnable(String account,String password,IMCallback imCallback){

            this.account = account;
            this.password = password;
            this.imCallback = imCallback;
        }

        @Override
        public void run() {
            int runC = 0;
            Message msg = Message.obtain();
            msg.what = OP;
            while(stub==null){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (runC>3){
                    IMOpState imOpState = new IMOpState(IMOpState.FAIL,"软件出错");
                    imOpState.imCallback = imCallback;
                    msg.obj = imOpState;
                    handler.sendMessage(msg);
                    return;
                }
                ++runC;
            }

            try {
                IMOpState imOpState = stub.login(account, password);
                msg.obj = imOpState;
                imOpState.imCallback = imCallback;
            } catch (RemoteException e) {
                e.printStackTrace();
                IMOpState imOpState = genImOpError();
                imOpState.imCallback = imCallback;
                msg.obj = imOpState;
            }
            handler.sendMessage(msg);
        }
    }

    public void register(String account, String password,IMCallback imCallback) {
        ThreadPool.start(new RegisterRunnable(account,password,imCallback));
    }

    class RegisterRunnable implements Runnable{

        String account;
        String password;
        IMCallback imCallback;
        public RegisterRunnable(String account,String password,IMCallback imCallback){
            this.account = account;
            this.password = password;
            this.imCallback = imCallback;
        }
        @Override
        public void run() {
            Message msg = Message.obtain();
            msg.what = OP;
            try {
                IMOpState imOpState = stub.register(account,password);
                msg.obj = imOpState;
                imOpState.imCallback = imCallback;
            } catch (RemoteException e) {
                e.printStackTrace();
                IMOpState imOpState = genImOpError();
                imOpState.imCallback = imCallback;
                msg.obj = imOpState;
            }
            handler.sendMessage(msg);
        }
    }

    public void logout() {
        try {
            stub.logout();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void addUser(String sender,String name){
        try {
            stub.addUser(sender, name);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void removeUser(String user){
        try {
            stub.removeUser(user);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void getMessage(IMMsg msg) throws RemoteException {
        Message message = Message.obtain();
        message.what = MSG;
        message.obj = msg;
        handler.sendMessage(message);
    }

    public void notifyListeners(IMMsg msg){
        System.out.println("IMServiceProxy msg recevice " + msg.getContent());
        if (msg.getSender().equals("admin")){
            if (systemListener==null)return;
            systemListener.getMessage(msg);
        }else{
            IMListener imListener = listeners.get(msg.getSender());
            if (imListener!=null){
                if (imListener.getMessage(msg)&&grobalListener!=null){
                    grobalListener.getMessage(msg);
                }
            }else if (grobalListener!=null){
                grobalListener.getMessage(msg);
            }
        }
    }

    public void addGrobalListener(IMListener imListener){
        this.grobalListener = imListener;
    }

    public void addBroadcast(IMListener imListener){
        this.systemListener = imListener;
    }

    public void addIMListener(String name,IMListener imListener){
        this.listeners.put(name,imListener);
    }

    public void removeIMListener(String n){
        this.listeners.remove(n);
    }

    public void setPath(String path){
        this.path = path;
    }

    private IMOpState genImOpError(){
        return new IMOpState(IMOpState.FAIL,"软件运行出错");
    }

    public List<String> getAllUsers(){
        if (stub==null)return null;
        try {
            return stub.getAllUsers();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void disconnect(String msg){
        Toast.makeText(mContext,"IMServiceProxy disconnect",Toast.LENGTH_SHORT).show();
    }

    public void unbindServer(){
        mContext.unbindService(serviceConnection);
    }

}
