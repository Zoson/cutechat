package com.init.cutechat.domain.enity;

import com.druson.cycle.enity.Enity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Zoson on 16/5/7.
 */
public class Settings extends Enity {
    public static String TAG = "settings";
    boolean isCallAlert = true;
    boolean isAutoPlay = true;
    boolean isPlayMusic = true;

    static String callAlert = "isCallAlert";
    static String autoPlay = "isAutoPlay";
    static String playMusic = "isPlayMusic";

    private Settings(){}

    public static Settings genSettingByHanldJson(String json) throws JSONException {
        Settings settings = new Settings();
        settings.initByJson(json);
        return settings;
    }

    public static Settings getDefault(){
        return new Settings();
    }

}
