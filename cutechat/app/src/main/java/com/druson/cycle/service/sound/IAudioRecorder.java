package com.druson.cycle.service.sound;

/**
 * Created by Zoson on 16/4/30.
 */
public interface IAudioRecorder {
    public int startRecordAndFile();
    public void stopRecordAndFile();
    public void setRecordPath(String path,String filename);
}
