package com.init.cutechat.domain.context;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.druson.cycle.service.comm.IMCallback;
import com.druson.cycle.service.comm.IMListener;
import com.druson.cycle.service.comm.IMMsg;
import com.druson.cycle.utils.ThreadPool;
import com.init.cutechat.Application.service.IMServiceProxy;
import com.init.cutechat.domain.enity.ContextData;
import com.init.cutechat.domain.enity.CuteMsg;
import com.init.cutechat.domain.enity.Face;
import com.init.cutechat.domain.enity.User;
import com.druson.cycle.service.sound.SoundSerivce;

import org.json.JSONException;

import java.io.File;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Zoson on 16/4/29.
 */
public class FaceChatSession extends CContext implements IMListener,IMCallback,FaceReceiver,IFaceChatSession {

    private FaceChatManager manager;
    private User user;
    private IMServiceProxy imService;
    private SoundSerivce soundSerivce;
    private String sender;
    private String recevicer;
    private Queue<CuteMsg> itsmessages;
    private Queue<CuteMsg> mymessages;
    private FaceChatSessionListener listener;
    private InfoProvider infoProvider;
    private int heartBeatSecond = 3;
    private int holdSession = 4;
    private Boolean isConnect = false;
    private SessionHanlder hanlder;
    private HeartBeatRunnable heartBeatRunnable;
    private DetectSessionRunnable detectSessionRunnable;

    protected FaceChatSession(String sender,String recevice,FaceChatSessionListener listener){
        this.manager = FaceChatManager.getInstance();
        infoProvider = InfoProvider.get();
        this.sender = sender;
        this.recevicer = recevice;
        this.user = infoProvider.getMasterUser();
        imService = (IMServiceProxy)getAppService(ServiceManager.MSGSERVICE);
        soundSerivce = (SoundSerivce)getAppService(ServiceManager.SOUNDSERVICE);
        imService.addIMListener(recevice, this);
        this.itsmessages = new ConcurrentLinkedQueue<>();
        this.mymessages = new ConcurrentLinkedQueue<>();
        this.listener = listener;
        File file = new File(ContextData.AppDataPath+"/"+recevice);
        if (!file.exists()){
            file.mkdir();
        }
        File file1 = new File(ContextData.AppDataPath+"/"+recevice+"/me");
        if (!file1.exists()){
            file1.mkdir();
        }
        File file2 = new File(ContextData.AppDataPath+"/"+recevice+"/it");
        if (!file2.exists()){
            file2.mkdir();
        }
        hanlder = new SessionHanlder();
        heartBeatRunnable = new HeartBeatRunnable();
        detectSessionRunnable = new DetectSessionRunnable();
    }

    public void startSession(){
        imService.addIMListener(recevicer, this);
        manager.lockSession(this);
        manager.addFaceReceiver(this);
    }

    public void closeSession(){
        manager.closeSession(this);
        manager.removeFaceReceiver(this);
        CuteMsg cuteMsg = new CuteMsg(sender,recevicer);
        cuteMsg.setState(FC_HUNGUP);
        imService.sendMessage(recevicer, cuteMsg.toJsonString(), new IMCallback() {
            @Override
            public void opResult(int state, String msg) {

            }
        });
        imService.removeIMListener(recevicer);

    }

    @Override
    public boolean getMessage(IMMsg imMsg) {
        boolean isHandle =false;
        switch (imMsg.getType()){
            case IM_TXT:
                CuteMsg cuteMsg = null;
                try {
                    cuteMsg = CuteMsg.genObjectByJson(imMsg.getContent());
                    isHandle = handleCuteMsg(cuteMsg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case IM_VOICE:
                CuteMsg cuteMsg1 = new CuteMsg(this.sender,recevicer);
                cuteMsg1.setContent(imMsg.getContent());
                cuteMsg1.setState(IFaceChatSession.FC_VOICE);
                itsmessages.offer(cuteMsg1);
                listener.recVoiceSucc(imMsg.getContent());
                playItsVoice();
                isHandle = true;
                break;
        }
        return isHandle;
    }

    public boolean handleCuteMsg(CuteMsg cuteMsg){
        boolean isHandle  = true;
        int state = cuteMsg.getState();
        if (listener == null)return false;
        switch (state){
            case FC_SPREAKING:
                listener.itSpeaking();
                break;
            case FC_HUNGUP:
                listener.hangUp();
                imService.removeIMListener(recevicer);
                break;
            case FC_EMOJI:
                Face face = new Face();
                try {
                    face.initByJson(cuteMsg.getContent());
                    listener.recEmoji(face);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case FC_ACCEPT:
                listener.callSucc();
                heartBeatRunnable.tryStart();
                ThreadPool.start(heartBeatRunnable);
                ThreadPool.start(detectSessionRunnable);
                break;
            case FC_REJECT:
                listener.callFail();
                break;
            case FC_CALLING:
                isHandle = false;
                break;
            case FC_HEARTBEAT:
                synchronized (isConnect){
                    isConnect = true;
                }
                break;
            case FC_DISCONNECT:
                listener.disconnect();
                heartBeatRunnable.tryStop();
                break;
        }
        return isHandle;
    }

    public void playItsVoice(){
        while(!itsmessages.isEmpty()){
            String path = itsmessages.poll().getContent();
            System.out.println("FaceChatSession playItsVoice == path == "+path);
            playVoice(path);
        }
    }

    public void playMyVoice(){
        while (!mymessages.isEmpty()){
            String path = mymessages.poll().getContent();
            System.out.println("FaceChatSession playmesVoice == path == "+path);
            playVoice(path);
        }

    }

    public void recordVoice(){
        String voiceName = ""+System.currentTimeMillis();
        CuteMsg cuteMsg = new CuteMsg(sender,recevicer);
        cuteMsg.setState(FC_SPREAKING);
        imService.sendMessage(recevicer, cuteMsg.toJsonString(), new IMCallback() {
            @Override
            public void opResult(int state, String msg) {

            }
        });
        startRecorderVoice(recevicer,voiceName);
    }

    public void stopRecord(){
        soundSerivce.stop();
    }

    @Override
    public void opResult(int state, String msg) {
        if (state == SUCC){
            listener.sendVoiceSucc();
            CuteMsg cuteMsg1 = new CuteMsg(this.sender,recevicer);
            cuteMsg1.setContent(msg);
            cuteMsg1.setState(IFaceChatSession.FC_VOICE);
            mymessages.add(cuteMsg1);
        }
    }

    public void setChatListener(FaceChatSessionListener faceChatListener){
        this.listener = faceChatListener;
    }

    public String getRecevicer() {
        return recevicer;
    }

    @Override
    public void getFacePoints(Face face) {
        CuteMsg cuteMsg = new CuteMsg(user.getAccount(),recevicer);
        cuteMsg.setState(FC_EMOJI);
        String faceJson = face.toJsonObject().toString();
        cuteMsg.setContent(faceJson);
        String cuteMsgStr = cuteMsg.toJsonString();
        //imService.sendMessage(recevicer, cuteMsgStr, null);
    }

    @Override
    public void onCreate() {
        imService.addIMListener(recevicer, this);
        manager.lockSession(this);
        manager.addFaceReceiver(this);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestory() {
        manager.closeSession(this);
        manager.removeFaceReceiver(this);
        CuteMsg cuteMsg = new CuteMsg(sender,recevicer);
        cuteMsg.setState(FC_HUNGUP);
        String cuteMsgStr = cuteMsg.toJsonString();
        imService.sendMessage(recevicer, cuteMsgStr, new IMCallback() {
            @Override
            public void opResult(int state, String msg) {

            }
        });

        imService.removeIMListener(recevicer);
    }

    protected void playVoice(String path){
        soundSerivce.setPlayPath(path);
        soundSerivce.play();
    }

    protected void startRecorderVoice(String recevice,String name){
        soundSerivce.setSoundFileName("s" + name, "t" + name);
        soundSerivce.setPath(ContextData.AppDataPath + "/" + recevice + "/me");
        soundSerivce.start();
    }

    public void setHeartBeatSecond(int second){
        this.heartBeatSecond = second;
    }

    class SessionHanlder extends Handler{

        public final static int CUTEMSG = 0x1;
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case CUTEMSG:
                    CuteMsg cuteMsg = (CuteMsg)msg.obj;
                    if (cuteMsg==null)return;
                    handleCuteMsg(cuteMsg);
                    break;
            }
        }
    }

    class DetectSessionRunnable implements Runnable{

        @Override
        public void run() {
            while (true){
                synchronized (isConnect){
                    if (isConnect){
                        isConnect = false;
                    }else{
                        CuteMsg cuteMsg = new CuteMsg();
                        cuteMsg.setState(FC_DISCONNECT);
                        Message message = Message.obtain();
                        message.obj = cuteMsg;
                        message.what = SessionHanlder.CUTEMSG;
                        hanlder.sendMessage(message);
                        break;
                    }
                    try {
                        Thread.sleep(holdSession*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    class HeartBeatRunnable implements Runnable{

        public boolean start = false;
        @Override
        public void run() {
            while(start){
                try {
                    System.out.println("SessionThread sendMessage ");
                    Thread.sleep(heartBeatSecond * 1000);
                    CuteMsg cuteMsg = new CuteMsg(user.getAccount(),recevicer);
                    cuteMsg.setState(FC_HEARTBEAT);
                    imService.sendMessage(recevicer,cuteMsg.toJsonString(),null);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void tryStart(){
            start = true;
        }

        public void tryStop(){
            start = false;
        }
    }

}
