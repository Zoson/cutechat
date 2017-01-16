package com.init.cutechat.domain.context;

import com.init.cutechat.domain.enity.Face;

/**
 * Created by Zoson on 16/5/2.
 */
public interface FaceChatSessionListener {
    public void sendVoiceSucc();
    public void sendVoiceing(int process);
    public void sendVoiceFail(String error);
    public void recVoiceSucc(String path);
    public void recVoiceFail();
    public void recVoiceing(int process);

    public void sendTxtSucc();
    public void sendTxtFail(String error);
    public void recTxtSucc(String content);

    public void callSucc();
    public void callFail();
    public void hangUp();
    public void itSpeaking();
    public void recEmoji(Face face);

    public void connectError();
    public void tryCalling();

    public void disconnect();
}
