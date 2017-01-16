package com.init.cutechat.domain.context;

/**
 * Created by Zoson on 16/2/7.
 */
public interface CCallback {
    public static final int SUCC = 0x0;
    public static final int FAIL = 0x1;
    public void response(int state,Object ob);
}
