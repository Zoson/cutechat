package com.init.cutechat.domain.context;

import android.content.Context;

import com.init.cutechat.domain.enity.ContextData;
import com.druson.cycle.service.picture.CameraService;
import com.druson.cycle.service.db.DataBaseOperator;
import com.druson.cycle.service.db.SharedPreference;
import com.druson.cycle.service.faceidentify.FaceIdentify;
import com.druson.cycle.service.sound.SoundSerivce;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Zoson on 16/2/5.
 */
public class ServiceManager {
    private Context mContext;
    private Map<Integer,Object> services;
    public ServiceManager(Context context){
        services = new HashMap<>();
        this.mContext = context;
    }

    public Object getService(int serviceName) {
        Object service = services.get(serviceName);
        if (service==null){
            switch (serviceName){
                case FACEIDENTIFY:
                    service = new FaceIdentify(mContext);
                    break;
                case CAMERA:
                    service = new CameraService();
                    break;
                case SOUNDSERVICE:
                    service = new SoundSerivce();
                    ((SoundSerivce)service).setPath(ContextData.AppDataPath);
                    break;
                case SHAREPREFERENCE:
                    service = new SharedPreference(mContext,ContextData.AppName).getSharedPreferences();
                    break;
                case MSGSERVICE:
                    //待处理

                    break;
                case DATABASE:
                    service = new DataBaseOperator(mContext);
                    break;
            }
            services.put(serviceName,service);
        }
        return service;
    }

    public void addService(int name,Object service){
        services.put(name,service);
    }

    public static final int FACEIDENTIFY = 0x0;
    public static final int CAMERA = 0x1;
    public static final int IMSERVICE = 0x2;
    public static final int SOUNDSERVICE = 0x3;
    public static final int SHAREPREFERENCE = 0x4;
    public static final int MSGSERVICE = 0x5;
    public static final int DATABASE = 0x6;

}
