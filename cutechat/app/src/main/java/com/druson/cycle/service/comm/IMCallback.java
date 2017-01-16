package com.druson.cycle.service.comm;

/**
 * Created by Zoson on 16/3/12.
 */
public interface IMCallback {
    public final static int SUCC = 0x1;
    public final static int FAIL = 0x2;
    public void opResult(int state, String msg);
}
