package com.init.cutechat.domain.context;

import android.content.Context;

import com.init.cutechat.Application.service.IMServiceProxy;
import com.init.cutechat.domain.enity.ContextData;

/**
 * Created by Zoson on 16/2/5.
 */
public class CContext {

    public static CContext baseContext;
    ContextData mContextData;
    ServiceManager mServiceManager;
    Context mContext;

    public static void init(Context context){
        if (baseContext==null){
            baseContext = new CContext(context);
        }
    }


    private CContext(Context context){
        this.mContext = context;
        mContextData = new ContextData();
        mServiceManager = new ServiceManager(mContext);
    }

    public CContext(){

    }

    public void initServices(){
        IMServiceProxy imServiceProxy = new IMServiceProxy(mContext);
        imServiceProxy.setPath(ContextData.AppDataPath);
        //imServiceProxy.setHost(ContextData.openfire);
        //imServiceProxy.setResource(ContextData.resource);
        mServiceManager.addService(ServiceManager.MSGSERVICE,imServiceProxy);
    }

    public void startContext(){
        InfoProvider infoProvider = InfoProvider.get();
        FaceChatManager faceChatManager = FaceChatManager.getInstance();
    }

    public Context getAppContext(){
        return mContext;
    }

    public Object getAppService(int service){
        return baseContext.mServiceManager.getService(service);
    }



    public ContextData getContextData(){
        return baseContext.mContextData;
    }

    public ServiceManager getServiceManager(){
        return baseContext.mServiceManager;
    }


}
