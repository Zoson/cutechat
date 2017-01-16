package com.druson.cycle.service.comm;

/**
 * Created by Zoson on 15/11/29.
 */
public interface IIMService {
    public int TEXT = 0x0;
    public int IMAGE = 0X1;
    public int FILE = 0x2;
    public int VOICE = 0x3;
    public int VIDEO =0x4;
    public void login(String account, String password);
    public void changePassword(String account, String oldps, String newps);
    public void setPath(String path);
    public void sendMessage(int type, String recevicer, String content);
    public void sendFile(String sender, String content);
    public void logOut();
    public int createChat();
    public void register(String account, String password);
    public void addUser();
}
