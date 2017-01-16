package com.init.cutechat.domain.context;

import com.init.cutechat.domain.enity.ContextData;
import com.init.cutechat.domain.enity.SoundAttr;
import com.druson.cycle.service.sound.ISoundSerivce;
import com.druson.cycle.service.sound.SoundListener;
import com.druson.cycle.service.sound.SoundSerivce;

/**
 * Created by Zoson on 16/4/29.
 */
public class SoundManager extends CContext implements ISoundSerivce{

    SoundSerivce soundSerivce;
    String voiceTestSourceName;
    String voiceTestTargetName;
    ContextData contextData;
    SoundListener listener;
    SoundAttr soundAttr;
    InfoProvider infoProvider;

    public SoundManager(){
        infoProvider = InfoProvider.get();
        this.voiceTestSourceName = "source";
        this.voiceTestTargetName = "target";
        contextData = getContextData();
        soundSerivce = (SoundSerivce)getAppService(ServiceManager.SOUNDSERVICE);
        soundSerivce.setPath(ContextData.AppDataPath);
        soundSerivce.setSoundFileName(voiceTestSourceName, voiceTestTargetName);
        soundAttr = infoProvider.getSoundAttr();
    }


    public SoundAttr getSoundAttr(){
        return soundAttr;
    }

    @Override
    public void setSoundFileName(String source, String target) {
        soundSerivce.setSoundFileName(source, target);
    }

    public void setSoundArr(float newPitch, float newRate, float newTempo){
        setSoundArr(soundAttr.sampleRate, soundAttr.channel, newPitch, newRate, newTempo);
    }


    @Override
    public void setSoundArr(int sampleRate, int channel, float newPitch, float newRate, float newTempo) {
        soundAttr.sampleRate = sampleRate;
        soundAttr.channel = channel;
        soundAttr.newPitch = newPitch;
        soundAttr.newRate = newRate;
        soundAttr.newTempo = newTempo;
        infoProvider.changeSoundAttr(soundAttr);
        soundSerivce.setSoundArr(sampleRate, channel, newPitch, (newRate + 1) / 10, newTempo);
    }

    @Override
    public void play() {
        soundSerivce.play();
    }

    @Override
    public void start() {
        soundSerivce.start();
    }

    @Override
    public void stop() {
        soundSerivce.stop();
    }

    @Override
    public void replay() {
        soundSerivce.replay();
    }

    @Override
    public void setPath(String path) {
        soundSerivce.setPath(path);
    }

    @Override
    public void setSoundListener(SoundListener listener) {
        soundSerivce.setSoundListener(listener);
    }

}
