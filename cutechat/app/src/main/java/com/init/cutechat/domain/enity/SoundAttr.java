package com.init.cutechat.domain.enity;

import com.druson.cycle.enity.Enity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Zoson on 16/4/28.
 */
public class SoundAttr extends Enity {
    public static String TAG = "soundattr";
    public int sampleRate = 16000;//设置声音的采样频率
    public int channel = 1;//设置声音的声道
    public float newPitch = 0;//设置声音的pitch
    public float newRate = 1;//设置声音的速率
    public float newTempo = 0;//变速不变调

    private SoundAttr(){

    }

    public static SoundAttr getDefault(){
        return new SoundAttr();
    }

    public static SoundAttr genSoundAttrbyhandleJson(String json) throws JSONException {
        SoundAttr soundAttr = new SoundAttr();
        soundAttr.initByJson(json);
        return soundAttr;
    }
}
