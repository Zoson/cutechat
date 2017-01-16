package com.druson.cycle.service.sound;

/**
 * Created by Zoson on 16/4/29.
 */
public interface ISoundSerivce {
    public static final  int START = 0x1;
    public static final int STOP = 0x2;
    public static final int REPLAY = 0x3;
    public static final int PLAY = 0x4;
    public void setSoundFileName(String source,String target);
    public void setSoundArr(int sampleRate,int channel,float newPitch,float newRate,float newTempo);
    public void start();
    public void stop();
    public void play();
    public void replay();
    public void setPath(String path);
    public void setSoundListener(SoundListener listener);
}
