package com.init.cutechat.domain.context;


import android.view.SurfaceHolder;

import com.druson.cycle.service.comm.IMCallback;
import com.druson.cycle.service.comm.IMListener;
import com.druson.cycle.service.comm.IMMsg;
import com.init.cutechat.Application.service.IMServiceProxy;
import com.init.cutechat.domain.enity.ContextData;
import com.init.cutechat.domain.enity.CuteMsg;
import com.init.cutechat.domain.enity.Face;
import com.init.cutechat.domain.enity.FeaturesSet;
import com.init.cutechat.domain.enity.User;
import com.druson.cycle.service.picture.CameraService;
import com.druson.cycle.service.faceidentify.FaceIdentify;
import com.druson.cycle.service.sound.SoundListener;
import com.druson.cycle.service.sound.SoundSerivce;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Zoson on 16/2/7.
 */
public class FaceChatManager extends CContext{
    public static String TAG = "FaceChatManager";
    private User user;
    private FaceIdentify mFaceIdentify;
    private CameraService mCameraService;
    private IMServiceProxy imService;
    private CCallback mCallBack;
    private List<FaceReceiver> faceGetListeners;
    private IMChatListener imChatListener;
    private boolean isStart = false;
    private boolean isInit = false;
    private SoundSerivce soundSerivce;
    private Map<String,FaceChatSession> mChatSessionMap;
    private FaceChatSession onFocusSession;
    private InfoProvider infoProvider;

    private List<IFaceChatSession> onCreateSessions;
    private List<IFaceChatSession> onStartSessions;
    private List<IFaceChatSession> onStopSessions;
    private List<IFaceChatSession> onDestorySessions;

    private static FaceChatManager self;

    private FaceChatManager(){
        onCreateSessions = new ArrayList<>();
        onStartSessions = new ArrayList<>();
        onStopSessions = new ArrayList<>();
        onDestorySessions = new ArrayList<>();

        infoProvider = InfoProvider.get();
        imService = (IMServiceProxy)getAppService(ServiceManager.MSGSERVICE);
        user = infoProvider.getMasterUser();
        soundSerivce = (SoundSerivce)getAppService(ServiceManager.SOUNDSERVICE);
        imService.addBroadcast(new IMSystemListener());
        imService.addGrobalListener(new IMGrobalListener());
        soundSerivce.setSoundListener(new SoundStateListener());

        faceGetListeners = new ArrayList<>();
        mChatSessionMap = new HashMap();
    }

    public void addFaceReceiver(FaceReceiver faceReceiver){
        faceGetListeners.add(faceReceiver);
    }

    public void removeFaceReceiver(FaceReceiver faceReceiver){
        for (int i=0;i<faceGetListeners.size();i++){
            if (faceReceiver == faceGetListeners.get(i)){
                faceGetListeners.remove(i);
            }
        }
    }

    public void removeAllFaceReceiver(){
        faceGetListeners.clear();
    }

    public void notifyFaceReceivers(Face face){
        for (int i=0;i<faceGetListeners.size();i++){
            faceGetListeners.get(i).getFacePoints(face);
        }
    }

    public void setIMChatListener(IMChatListener imChatListener){
        this.imChatListener = imChatListener;
    }

    public FaceChatSession createSession(final String recevice, final FaceChatSessionListener faceChatListener){
        FaceChatSession session = mChatSessionMap.get(recevice);
        if (session==null){
            session = new FaceChatSession(user.getAccount(),recevice,faceChatListener);
            mChatSessionMap.put(recevice,session);
        }
        session.setChatListener(faceChatListener);
        CuteMsg cuteMsg = new CuteMsg(user.getAccount(),recevice);
        cuteMsg.setState(IFaceChatSession.FC_CALLING);
        imService.addUser(recevice, recevice);
        imService.sendMessage(recevice, cuteMsg.toJsonString(), new IMCallback() {
            @Override
            public void opResult(int state, String msg) {
                if (faceChatListener==null)return;
                if (state == FAIL){
                    faceChatListener.connectError();
                    imService.removeUser(recevice);
                }else{
                    faceChatListener.tryCalling();
                }
            }
        });
        return session;
    }

    public void rejectCaller(String caller){
        CuteMsg cuteMsg = new CuteMsg(user.getAccount(),caller);
        cuteMsg.setState(IFaceChatSession.FC_REJECT);
        imService.sendMessage(caller, cuteMsg.toJsonString(), new IMCallback() {
            @Override
            public void opResult(int state, String msg) {
                System.out.println("rejectCaller ==== "+msg);
            }
        });
    }

    public FaceChatSession acceptCaller(String caller){
        FaceChatSession session = mChatSessionMap.get(caller);
        if (session==null){
            session = new FaceChatSession(this.user.getAccount(),caller,null);
            mChatSessionMap.put(caller,session);
        }
        CuteMsg cuteMsg = new CuteMsg(user.getAccount(),caller);
        cuteMsg.setState(FaceChatSession.FC_ACCEPT);
        imService.sendMessage(caller, cuteMsg.toJsonString(), new IMCallback() {
            @Override
            public void opResult(int state, String msg) {
                System.out.println("acceptCaller ==== "+msg);
            }
        });
        return session;
    }

    public static FaceChatManager getInstance(){
        if (self==null){
            synchronized (TAG){
                if (self==null){
                    self = new FaceChatManager();
                }
            }
        }
        return self;
    }

    public boolean lockSession(FaceChatSession faceChatSession){
        if (onFocusSession==null){
            this.onFocusSession = faceChatSession;
            return true;
        }
        return false;
    }

    public void closeSession(FaceChatSession faceChatSession){
        if (onFocusSession == null)return;
        if (faceChatSession.equals(onFocusSession)){
            onFocusSession = null;
        }
    }

    public void stopFaceIdentify(){
        if (isStart==false)return;
        System.out.println("stopFaceIdentify");
        isStart = false;
        mCameraService.stopCatch();
        mFaceIdentify.stopIdentify();
    }

    public void startFaceIdentify(SurfaceHolder sf,CCallback cCallback){
        if (isStart == true)return;
        if (isInit == false) {
            mFaceIdentify = (FaceIdentify)getAppService(ServiceManager.FACEIDENTIFY);
            mCameraService = (CameraService)getAppService(ServiceManager.CAMERA);
            isInit = true;
        }
        mCameraService.init(8,new CatchImage(),sf);
        this.mCallBack = cCallback;
        mCameraService.startPreview();
        mFaceIdentify.startIdentify(new FacePointGet());
        isStart = true;
    }

    public FeaturesSet getFeaturesSet(){
        return infoProvider.getFeaturesSet();
    }

    class IMGrobalListener implements IMListener {
        @Override
        public boolean getMessage(IMMsg imMsg) {
            boolean isHandle = false;
            System.out.println("FaceChatManager rece message "+imMsg.getContent());
            FaceChatSession faceChatSession = mChatSessionMap.get(imMsg.getSender());
            switch (imMsg.getType()){
                case IM_TXT:
                    CuteMsg cuteMsg = null;
                    try {
                        cuteMsg = CuteMsg.genObjectByJson(imMsg.getContent());
                        if (cuteMsg.getState()==FaceChatSession.FC_CALLING){
                            System.out.println("FaceChatManager Calling message");
                            if (imChatListener!=null){
                                imChatListener.getCall(imMsg.getSender());
                            }
                            isHandle = true;
                        }else {
                            return false;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return isHandle = true;
                    }
                    break;
            }
            return isHandle;
        }
    }

    //待处理
    class IMSystemListener implements IMListener{

        @Override
        public boolean getMessage(IMMsg imMsg) {
            return false;
        }
    }

    class CatchImage implements CameraService.CatchImageCallback{
        @Override
        public void getBytes(byte[] bytes) {
            // TODO Auto-generated method stub
            mFaceIdentify.setImageByte(bytes);
        }
    }

    class FacePointGet implements FaceIdentify.FaceIdentifyCallback{
        @Override
        public void getFeaturePoints(int[] points) {
            Face face = new Face(points);
            notifyFaceReceivers(face);
            mCallBack.response(CCallback.SUCC, face);
        }
    }

    class SoundStateListener implements SoundListener{

        @Override
        public void soundStateOfEnd(int state,String path) {
            if (state==STOP){
                if (onFocusSession!=null){
                    imService.sendFile(onFocusSession.getRecevicer(), path , onFocusSession);
                }
            }
        }
    }


}
