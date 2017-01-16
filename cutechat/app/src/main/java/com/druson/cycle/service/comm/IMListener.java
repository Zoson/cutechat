package com.druson.cycle.service.comm;

/**
 * Created by zoson on 4/19/15.
 */
public interface IMListener {
    public static final int IM_TXT = 0x1;
    public static final int IM_FILE = 0x2;
    public static final int IM_VOICE = 0x3;
    
    public boolean getMessage(IMMsg imMsg);
}
