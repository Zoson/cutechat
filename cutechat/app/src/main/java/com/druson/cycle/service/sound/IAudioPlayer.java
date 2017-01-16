package com.druson.cycle.service.sound;

/**
 * Created by Zoson on 16/4/30.
 */
public interface IAudioPlayer {
    public static int OK = 0x0;
    public static int IS_DIR = 0x1;
    public static int IS_NOTFILE = 0x2;
    public static int NOTPATH = 0x3;
    public static int PLAYERROR = 0x4;
    public int startPlay();
    public void stopPlay();
    public void setPlayPath(String path);
    public void setPlayPath(String path,String name);
    public void setPlayPath(String path,String name,String type);
}
