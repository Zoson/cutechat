package com.init.cutechat.domain.context;

import com.init.cutechat.domain.enity.User;

/**
 * Created by Zoson on 16/5/2.
 */
public interface IMChatListener {

    public void getCall(String sender);
    public void getSystemMessage();

}
