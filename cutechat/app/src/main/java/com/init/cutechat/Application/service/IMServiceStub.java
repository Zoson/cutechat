package com.init.cutechat.Application.service;

import android.os.RemoteException;

import com.druson.cycle.ipc.IMsgClient;
import com.druson.cycle.ipc.IMsgServer;
import com.druson.cycle.service.comm.IMOpState;
import com.druson.cycle.service.comm.IMService;

import java.util.List;

/**
 * Created by Zoson on 16/5/9.
 */
public class IMServiceStub extends IMsgServer.Stub{

    private IMService imService;
    private IMsgClient msgClient;

    public IMServiceStub(){
        imService = IMService.init();
    }

    @Override
    public IMOpState sendMessage(String sender, String content) throws RemoteException {
        return imService.sendMessage(sender,content);
    }

    @Override
    public IMOpState sendFile(String sender, String filepath) throws RemoteException {
        return imService.sendFile(sender, filepath);
    }

    @Override
    public IMOpState login(String account, String password) throws RemoteException {
        return imService.tryLogin(account, password);
    }

    @Override
    public IMOpState register(String account, String password) throws RemoteException {
        return imService.regXmpp(account, account, password);
    }

    @Override
    public void logout() throws RemoteException {
        imService.disConnect();
    }

    @Override
    public void setHost(String host) throws RemoteException {
        imService.setHost(host);
    }

    @Override
    public void setResource(String res) throws RemoteException {
        imService.setResource(res);
    }

    public void setPath(String path){
        imService.setPath(path);
    }

    public void addClient(IMsgClient msgClient){
        this.msgClient = msgClient;
    }

    public void addUser(String sender,String name){
        imService.addUser(sender,name);
    }

    public void removeUser(String sender){
        imService.removeUser(sender);
    }

    public List<String> getAllUsers(){
        return imService.getAllUsers();
    }

}
