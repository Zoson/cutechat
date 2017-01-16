package com.init.cutechat.domain.context;

import android.content.SharedPreferences;

import com.druson.cycle.utils.FileUtils;
import com.init.cutechat.Application.service.IMServiceProxy;
import com.init.cutechat.domain.enity.ContextData;
import com.init.cutechat.domain.enity.FeaturesSet;
import com.init.cutechat.domain.enity.Settings;
import com.init.cutechat.domain.enity.SoundAttr;
import com.init.cutechat.domain.enity.User;

import org.json.JSONException;

import java.io.File;
import java.util.List;

/**
 * Created by Zoson on 16/4/30.
 */
public class InfoProvider extends CContext {
    IMServiceProxy imService;
    SharedPreferences spService;
    ContextData contextData;
    Settings settings;
    SoundAttr soundAttr;
    User masterUser;

    public FeaturesSet getFeaturesSet() {
        return featuresSet;
    }

    FeaturesSet featuresSet;

    private static InfoProvider instance;

    public static InfoProvider get(){
        if (instance == null){
            instance = new InfoProvider();
        }
        return instance;
    }

    protected InfoProvider(){
        imService = (IMServiceProxy)getAppService(ServiceManager.MSGSERVICE);
        spService = (SharedPreferences)getAppService(ServiceManager.SHAREPREFERENCE);
        contextData = getContextData();
        loadData();
    }

    public void setMasterUser(User user){
        this.masterUser = user;
    }

    public User getMasterUser(){
        return masterUser;
    }

    private void loadData(){
        File appDataPath = new File(ContextData.AppDataPath);
        if (!appDataPath.exists()){
            appDataPath.mkdir();
        }

        featuresSet = new FeaturesSet();

        boolean autologin = spService.getBoolean("autologin",false);
        if (autologin){
            masterUser = new User();
            masterUser.setAccount(spService.getString("account",""));
            masterUser.setPassword(spService.getString("password", ""));
        }

        String str_settings = spService.getString(Settings.TAG, "null");
        try {
            settings = Settings.genSettingByHanldJson(str_settings);
        } catch (JSONException e) {
            e.printStackTrace();
            settings = Settings.getDefault();
        }

        String str_soundattr = spService.getString(SoundAttr.TAG, "null");
        try {
            soundAttr = SoundAttr.genSoundAttrbyhandleJson(str_soundattr);
        } catch (JSONException e) {
            e.printStackTrace();
            soundAttr = SoundAttr.getDefault();
        }
    }

    public List<String> getAccounts(){
        return imService.getAllUsers();
    }

    public void changeSetting(Settings settings){
        SharedPreferences.Editor editor = spService.edit();
        editor.putString(Settings.TAG,settings.toJsonString());
        editor.commit();
    }

    public void changeSoundAttr(SoundAttr soundAttr){
        SharedPreferences.Editor editor = spService.edit();
        editor.putString(SoundAttr.TAG,soundAttr.toJsonString());
        editor.commit();
    }

    public Settings getSettings(){
        return settings;
    }

    public SoundAttr getSoundAttr(){
        return soundAttr;
    }

    public void cleanCache(){
        FileUtils.deleteDir(ContextData.AppDataPath);
    }

    public void loginOut(){

    }

}
