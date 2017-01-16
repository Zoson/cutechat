package com.druson.cycle.service.sound;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.File;

/**
 * Created by Zoson on 16/4/30.
 */
public class AudioPlayer implements MediaPlayer.OnCompletionListener,IAudioPlayer{

    MediaPlayer mediaPlayer;
    String path;
    String filename;
    String type;
    public AudioPlayer(){

    }

    public AudioPlayer(String path,String filename,String type){
        setPlayPath(path,filename,type);
    }

    public AudioPlayer(String path,String filename){
        setPlayPath(path,filename);
    }

    public AudioPlayer(String path){
        setPlayPath(path);
    }


    public synchronized int startPlay() {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(this);
            StringBuilder sb = new StringBuilder();
            if (path!=null){
                sb.append(path);
            }else{
                return NOTPATH;
            }
            File file = new File(sb.toString());
            if (file.exists()&&file.isFile()){
                System.out.println("AudioPlayer play path = "+path);
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepare();
                mediaPlayer.start();
            }else{
                if (filename!=null){
                    sb.append("/");
                    sb.append(filename);
                    if (type!=null){
                        sb.append(".");
                        sb.append(type);
                    }
                }
                File file1 = new File(sb.toString());
                if (file1.isDirectory()){
                    return IS_DIR;
                }else if (!file1.isFile()){
                    return IS_NOTFILE;
                }
                System.out.println("AudioPlayer play path = "+path+ "/" + filename + "." +type);
                mediaPlayer.setDataSource(path + "/" + filename + "." +type);
                mediaPlayer.prepare();
                mediaPlayer.start();
            }


        } catch (Exception e) {
            return PLAYERROR;
        }
        return OK;
    }

    public void stopPlay(){
        if (mediaPlayer!=null){
            if (mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
        }
    }

    @Override
    public void setPlayPath(String path) {
        this.path = path;
    }

    @Override
    public void setPlayPath(String path, String filename) {

        String[] files = filename.split(".");
        if (files.length>=2){
            StringBuilder sb = new StringBuilder();
            for (int i=0;i<files.length-1;i++){
                sb.append(files[i]);
            }
            this.filename = sb.toString();
            this.type = files[files.length-1];
        }else{
            this.filename = filename;
            this.type = "mp3";
        }
    }

    @Override
    public void setPlayPath(String path, String filename, String type) {
        this.filename = filename;
        this.path = path;
        String[] types = type.split(".");
        if (types.length>=2){
            this.type = types[types.length-1];
        }else{
            this.type = type;
        }
    }



    @Override
    public void onCompletion(MediaPlayer mp) {
        System.out.println("MediaPlayer release");
        if (mediaPlayer==null)return;
        mediaPlayer.release();
    }
}
