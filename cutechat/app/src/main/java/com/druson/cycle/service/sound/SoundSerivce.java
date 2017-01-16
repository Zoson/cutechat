package com.druson.cycle.service.sound;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.io.File;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import mp3.Main;

/**
 * Created by Zoson on 16/4/28.
 */
public class SoundSerivce implements ISoundSerivce,IAudioRecorder,IAudioPlayer{

    protected int sampleRate = 16000;
    protected int channel = 1;
    protected float newPitch = 0;
    protected float newRate = 1;
    protected float newTempo = 0;
    protected SoundTouch soundTouch;
    protected Queue<String> q_play;
    protected IAudioRecorder audioWavRecord;
    protected IAudioPlayer audioPlayer;
    protected SoundTouchThread soundTouchThread;
    protected SoundListener listener;
    protected Main mp3tranfer;
    protected String path = Environment.getExternalStorageDirectory().getAbsolutePath();
    protected String source = "source";
    protected String target = "target";


    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public void setNewPitch(float newPitch) {
        this.newPitch = newPitch;
    }

    public void setNewRate(float newRate) {
        this.newRate = newRate;
    }

    public void setNewTempo(float newTempo) {
        this.newTempo = newTempo;
    }

    public SoundSerivce() {
        soundTouchThread = new SoundTouchThread();
        soundTouchThread.start();
        soundTouch = new SoundTouch();
        mp3tranfer = new Main();
        audioWavRecord = new AudioWavRecord(path,source);
        audioPlayer = new AudioPlayer(path,target,"mp3");
        q_play = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void setSoundFileName(String source, String target) {
        this.source = source;
        this.target = target;
        audioPlayer.setPlayPath(path,target,"mp3");
        audioWavRecord.setRecordPath(path,source);
    }

    @Override
    public int startPlay() {
        return audioPlayer.startPlay();
    }

    @Override
    public void stopPlay() {
        audioPlayer.stopPlay();
    }

    @Override
    public void setPlayPath(String path) {
        audioPlayer.setPlayPath(path);
    }

    @Override
    public void setPlayPath(String path, String name) {
        audioPlayer.setPlayPath(path,name);
    }

    public void setPath(String path){
        this.path = path;
        audioPlayer.setPlayPath(path);
        audioWavRecord.setRecordPath(path,source);
    }

    @Override
    public void setSoundListener(SoundListener listener) {
        this.listener = listener;
    }

    @Override
    public void setSoundArr(int sampleRate, int channel, float newPitch, float newRate, float newTempo) {
        this.sampleRate = sampleRate;
        this.channel = channel;
        this.newPitch = newPitch;
        this.newRate = newRate;
        this.newTempo = newTempo;
    }

    public void start() {
        audioWavRecord.setRecordPath(path, source);
        soundTouchThread.handler.sendEmptyMessage(START);
    }

    public void stop(){
        soundTouchThread.handler.sendEmptyMessage(STOP);
    }

    @Override
    public void play() {
        soundTouchThread.handler.sendEmptyMessage(PLAY);
    }

    @Override
    public int startRecordAndFile() {
        return audioWavRecord.startRecordAndFile();
    }

    @Override
    public void stopRecordAndFile() {
        audioWavRecord.stopRecordAndFile();
    }

    @Override
    public void setRecordPath(String path, String filename) {
        this.path = path;
        this.source = filename;
        audioWavRecord.setRecordPath(path, filename);
    }

    @Override
    public void setPlayPath(String path, String name, String type) {
        audioPlayer.setPlayPath(path,name,type);
    }

    class SoundTouchThread extends Thread {
        @Override
        public void run() {
            Looper.prepare();
            handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what){
                        case START:
                            audioWavRecord.startRecordAndFile();
                            break;
                        case STOP:
                            audioWavRecord.stopRecordAndFile();
                            startSoundTouch();
                            audioTranfer();
                            //startPlay();
                            break;
                        case REPLAY:
                            startSoundTouch();
                            audioTranfer();
                            startPlay();
                            break;
                        case PLAY:
                            startPlay();
                            break;
                    }
                    if (listener!=null){
                        listener.soundStateOfEnd(msg.what,path+"/"+target+".mp3");
                    }
                }
            };
            Looper.loop();
        }

        public Handler handler;
    }


    public void audioTranfer(){
        try {
            mp3tranfer.convertWAVToMP3(path +"/"+ target);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        File mp3File = new File(path + "/"+target+".mp3");
        if (mp3File.length() == 0) {
            int retryTimes = 0;
            while (true) {
                // sleep(2000);
                mp3File = new File(path + "target.mp3");
                if (mp3File.length() > 0 || retryTimes == 50)
                    break;
                retryTimes++;
            }
            if (mp3File.length() == 0) {
                try {
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public void replay(){
        soundTouchThread.handler.sendEmptyMessage(REPLAY);
    }

    public void startSoundTouch(){
        soundTouch.setSampleRate(sampleRate);
        soundTouch.setChannels(channel);
        soundTouch.setPitchSemiTones(newPitch);//0为正常 -10-0-10
        soundTouch.setSpeed(newRate);// 1正常速度,2为两倍
        soundTouch.setTempo((newTempo+100) * 0.01f); //0为正常 -50-100
        soundTouch.processFile(path+"/"+source+".wav", path+"/"+target+".wav");
    }

}
