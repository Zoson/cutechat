package com.init.cutechat.domain.context;

/**
 * Created by Zoson on 16/5/12.
 */
public interface IFaceChatSession {

    public final static int FC_SPREAKING = 0x1;
    public final static int FC_VOICE = 0x2;
    public final static int FC_EMOJI = 0x3;
    public final static int FC_HUNGUP = 0x4;
    public final static int FC_CALLING = 0x5;
    public final static int FC_ACCEPT = 0x6;
    public final static int FC_REJECT = 0x7;
    public final static int FC_HEARTBEAT = 0x8;
    public final static int FC_DISCONNECT = 0x9;

    public void onCreate();
    public void onStart();
    public void onStop();
    public void onDestory();
}
