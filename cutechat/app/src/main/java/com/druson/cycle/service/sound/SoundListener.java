package com.druson.cycle.service.sound;

/**
 * Created by Zoson on 16/4/30.
 */
public interface SoundListener {

    public static final  int START = ISoundSerivce.START;
    public static final int STOP = ISoundSerivce.STOP;
    public static final int REPLAY = ISoundSerivce.REPLAY;
    public void soundStateOfEnd(int state,String path);
}
