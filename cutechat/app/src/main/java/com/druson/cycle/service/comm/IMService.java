package com.druson.cycle.service.comm;

import android.os.Environment;
import android.util.Log;

import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.filetransfer.FileTransfer;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.offline.OfflineMessageManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by zoson on 4/19/15.
 */
public class IMService implements ChatManagerListener,ChatMessageListener,FileTransferListener{
    private final static String TAG = "IMService";
    private XMPPTCPConnectionConfiguration connectionConfiguration;
    private XMPPTCPConnection connection;
    public String xmpp_host = "120.25.12.205";//  119.29.22.244 120.25.12.205
    private int xmpp_port = 5222;
    public String xmpp_service_name = "120.25.12.205";
    public  String resource = "Spark";
    private Map<String,IMListener> listensers;
    private IMListener systemListener;
    private IMListener grobalListener;
    private ChatManager chatManager;
    private FileTransferManager fileTransferManager;
    private AccountManager accountManager;
    private OfflineMessageManager offlineMessageManager;
    private Roster roster;
    private String path = Environment.getExternalStorageDirectory().getAbsolutePath();

    private String account;
    private String password;

    public final static int OP = 0x7;
    public final static int MSG = 0x8;

    public void setHost(String host){
        xmpp_host = host;
    }

    public void setResource(String res){
        resource = res;
    }

    private static IMService instance ;


    public static IMService init(){
        if (instance==null){
            instance = new IMService();
        }
        return instance;
    }

    public IMService(){
        XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
        builder.setServiceName(xmpp_service_name);
        builder.setHost(xmpp_host);
        builder.setPort(xmpp_port);
        builder.setSecurityMode(XMPPTCPConnectionConfiguration.SecurityMode.disabled);
        builder.setDebuggerEnabled(true);
        builder.setCompressionEnabled(true);
        connectionConfiguration = builder.build();
        connection = new XMPPTCPConnection(connectionConfiguration);
        SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");
        listensers = new HashMap<>();
        connection = new XMPPTCPConnection(connectionConfiguration);
    }

    public void setPath(String path){
        this.path = path;
    }

    public void addListener(String account,IMListener imListener){
        listensers.put(account, imListener);
    }

    public void addGrobalListener(IMListener imListener){
        this.grobalListener = imListener;
    }

    public void rmListener(String listener){
        listensers.remove(listener);
    }

    public void addBroadcast(IMListener imListener){
        this.systemListener = imListener;
    }

    public void rmAllListeners(){
        listensers.clear();
    }

    public void disConnect(){
        connection.disconnect();
    }

    @Override
    public void chatCreated(Chat chat, boolean b) {
        chat.addMessageListener(this);
    }

    @Override
    public void processMessage(Chat chat, Message message) {
        System.out.println(TAG+" processMessage "+message.getType());
        if (message==null||message.getBody()==null)return;
        IMMsg msg = new IMMsg();
        msg.content = message.getBody();
        msg.sender = message.getFrom().split("@")[0];
        msg.address = message.getFrom().split("@")[1].split("/")[0];
        msg.type = IMListener.IM_TXT;
        notifyListeners(msg);
    }

    public void processMessage(Message msg){
        processMessage(null, msg);
    }

    private void notifyListeners(IMMsg msg){
        for (String key:listensers.keySet()){
            System.out.println("who "+ key+"  getMessage === "+msg.getContent());
            IMListener imListener = listensers.get(key);
            if (imListener!=null){
                imListener.getMessage(msg);
            }
        }
    }

    public String addXmppHost(String user){
        return user+"@"+xmpp_host+"/"+resource;
    }

    public IMOpState regXmpp(String account, String password, String username){
        IMOpState opResultInfo = new IMOpState();
        try {
            accountManager.createAccount(account,password);
            opResultInfo.result = "注册成功";
            opResultInfo.state = IMCallback.SUCC;
        } catch (SmackException.NoResponseException e) {
            opResultInfo.state = IMCallback.FAIL;
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            opResultInfo.state = IMCallback.FAIL;
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            opResultInfo.state = IMCallback.FAIL;
            e.printStackTrace();
        }
        return opResultInfo;
    }

    public IMOpState tryLogin(String account,String password){
        IMOpState opResultInfo;
        if (connection.isConnected()){
            String user = connection.getUser();
            System.out.println("trylog "+user+"----------");
            if(user!=null&&user.split("@")[0].equals(account)){
                opResultInfo = new IMOpState();
                opResultInfo.state =  IMOpState.SUCC;
                opResultInfo.result = "用户已经登录";
            }else{
                System.out.println("trylogin "+account+" "+password);
                connection.disconnect();
                opResultInfo = login(account,password);
            }
        }else{
            connection.disconnect();
            opResultInfo = login(account,password);
        }
        return opResultInfo;
    }


    public IMOpState login(String account,String password){
        IMOpState opResultInfo = new IMOpState();
        try{
            try {
                connection.connect();
            } catch (SmackException e) {
                e.printStackTrace();
                opResultInfo.result = "连接出错";
                opResultInfo.state = IMOpState.FAIL;
                return opResultInfo;
            } catch (IOException e) {
                e.printStackTrace();
                opResultInfo.result = "连接出错";
                opResultInfo.state = IMOpState.FAIL;
                return opResultInfo;
            }
            accountManager = AccountManager.getInstance(connection);
            chatManager = ChatManager.getInstanceFor(connection);
            fileTransferManager = FileTransferManager.getInstanceFor(connection);
            chatManager.addChatListener(this);
        }catch (XMPPException e){
            opResultInfo.state = IMCallback.FAIL;
            opResultInfo.result = "服务器错误";
            e.printStackTrace();
            return opResultInfo;
        }

        try {
            try {
                connection.login(account,password,resource);
            } catch (SmackException e) {
                e.printStackTrace();
                opResultInfo.result = "连接出错";
                opResultInfo.state = IMOpState.FAIL;
                return opResultInfo;
            } catch (IOException e) {
                e.printStackTrace();
                opResultInfo.result = "连接出错";
                opResultInfo.state = IMOpState.FAIL;
                return opResultInfo;
            }

            offlineMessageManager = new OfflineMessageManager(connection);
                /*
                处理离线消息
                 */
            List<Message> list = null;
            try {
                list = offlineMessageManager.getMessages();
            } catch (SmackException.NoResponseException e) {
                e.printStackTrace();
                opResultInfo.result = "获取好友失败";
                opResultInfo.state = IMOpState.SUCC;
                return opResultInfo;
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
                opResultInfo.result = "获取好友失败";
                opResultInfo.state = IMOpState.SUCC;
                return opResultInfo;
            }
            Map<String,ArrayList<Message>> offlineMsgs = new HashMap<>();
            Iterator<Message> it = list.iterator();
            while(it.hasNext()) {
                Message msg = it.next();
                processMessage(msg);
            }
            try {
                offlineMessageManager.deleteMessages();
            } catch (SmackException.NoResponseException e) {
                e.printStackTrace();
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            }

            Presence presence = new Presence(Presence.Type.available);
            try {
                connection.sendPacket(presence);//上线了
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            }
        }catch (IllegalStateException e){
            opResultInfo.state = IMCallback.FAIL;
            opResultInfo.result = "服务器出错";
            e.printStackTrace();
            return opResultInfo;
        }catch (XMPPException e){
            opResultInfo.state = IMCallback.FAIL;
            opResultInfo.result = "登录失败";
            e.printStackTrace();
            return opResultInfo;
        }

        if (connection.isConnected()){
            opResultInfo.state = IMCallback.SUCC;
            opResultInfo.result = "登录成功";
        }else{
            opResultInfo.result = "登录出错";
            opResultInfo.state = IMOpState.FAIL;
            return opResultInfo;
        }
        chatManager = ChatManager.getInstanceFor(connection);
        roster = Roster.getInstanceFor(connection);
        roster.addRosterListener(new UserRoasterListener());
        chatManager.addChatListener(IMService.this);
        fileTransferManager.addFileTransferListener(IMService.this);
        return opResultInfo;
    }

    public IMOpState sendMessage(String sender,String content){
        IMOpState opResultInfo = new IMOpState();
        if (!connection.isConnected()){
            try {
                try {
                    connection.connect();
                    connection.login();
                } catch (SmackException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (XMPPException e) {
                e.printStackTrace();
                opResultInfo.result = "网络错误";
                opResultInfo.state = IMCallback.FAIL;
                return opResultInfo;
            }
        }
        Chat chat = null;
        chat = chatManager.createChat(addXmppHost(sender),null);

        if (chat != null) {
            System.out.println("send message:" + content + " to " + sender);
            Log.i(TAG, "send message:" + content + " to " + sender);
            try {
                chat.sendMessage(content);
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
                e.printStackTrace();
                opResultInfo.result = "发送失败";
                opResultInfo.state = IMCallback.FAIL;
            }
            opResultInfo.state = IMCallback.SUCC;
            opResultInfo.result = "发送成功";
        }else{
            opResultInfo.result = "用户不存在";
            opResultInfo.state = IMCallback.FAIL;
        }
        return opResultInfo;
    }

    public IMOpState sendFile(String sender,String path){
        IMOpState opResultInfo = new IMOpState();
        if (!connection.isConnected()){
            try {
                connection.connect();
            } catch (SmackException e) {
                e.printStackTrace();
                opResultInfo.result = "网络错误";
                opResultInfo.state = IMCallback.FAIL;
                return opResultInfo;
            } catch (IOException e) {
                opResultInfo.result = "网络错误";
                opResultInfo.state = IMCallback.FAIL;
                e.printStackTrace();
                return opResultInfo;
            } catch (XMPPException e) {
                opResultInfo.result = "网络错误";
                opResultInfo.state = IMCallback.FAIL;
                e.printStackTrace();
                return  opResultInfo;
            }
        }
        Presence presence = Roster.getInstanceFor(connection).getPresence(addXmppHost(sender));
        Log.i(TAG, "SendFile Presence = " + presence);
        System.out.println("SendFile Presence = "+presence+" ---------------------");
        //if (presence.getType()!= Presence.Type.unavailable){
        if (presence !=null){
            OutgoingFileTransfer transfer = fileTransferManager.createOutgoingFileTransfer(presence.getFrom());
            try {
                transfer.sendFile(new File(path), "语音");
            } catch (SmackException e) {
                e.printStackTrace();
            }
            while(!transfer.isDone());
            opResultInfo.result = path;
            System.out.println("发送成功");
            opResultInfo.state = IMCallback.SUCC;

        }else{
            opResultInfo.result = "对方不在线";
            opResultInfo.state = IMCallback.FAIL;
        }
        return opResultInfo;
    }

    //文件接收
    @Override
    public void fileTransferRequest(FileTransferRequest fileTransferRequest) {
        //System.out.println("fileTransfer文件接收开始main Thread");
        //ThreadPool.start(new RecFileRunnable(fileTransferRequest));
        Log.i(TAG, "fileTransferRequest::开始接收文件");
        //System.out.println("fileTransferRequest::开始接收文件");
        IncomingFileTransfer incomingFileTransfer = fileTransferRequest.accept();
        long startTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fromUser = fileTransferRequest.getRequestor().split("@")[0];
        String address = fileTransferRequest.getRequestor().split("@")[0].split("/")[0];
        String fileName = fileTransferRequest.getFileName();
        String dirpath = path+"/"+fromUser;
        String filepath = dirpath + "/it/"+fileName;
        System.out.println("fileTransferRequest filepath ==== "+filepath);
        File file = new File(filepath);
        File dir = new File(dirpath);
        if (!dir.exists()){
            dir.mkdir();
        }
        if (file.exists())file.delete();
        try {
            incomingFileTransfer.recieveFile(file);
        } catch (SmackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        boolean isRepeat = false;
        while (!incomingFileTransfer.getStatus().equals(FileTransfer.Status.complete)){
            if (incomingFileTransfer.getStatus().equals(FileTransfer.Status.error)){
                System.out.println(sdf.format(new Date())+"error!!!"+incomingFileTransfer.getError());
                incomingFileTransfer.cancel();
                try {
                    if (isRepeat)break;
                    isRepeat = true;
                    incomingFileTransfer.recieveFile(file);
                } catch (SmackException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                double progress = incomingFileTransfer.getProgress();
                progress*=100;
                //System.out.println(sdf.format(new Date())+"status="+incomingFileTransfer.getStatus());
                //System.out.println(sdf.format(new Date())+"progress="+sdf.format(progress)+"%");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //System.out.println("used " + ((System.currentTimeMillis() - startTime) / 1000) + " seconds  ");
        IMMsg msg = new IMMsg();
        msg.type = IMListener.IM_VOICE;
        msg.content = filepath;
        msg.sender = fromUser;
        msg.address = address;
        long length = fileTransferRequest.getFileSize();
        Log.i(TAG, "fileTransferRequest::文件大小:" + length + " 文件发起者:" + fileTransferRequest.getRequestor() + " 文件MIME类型:" + fileTransferRequest.getMimeType());
        System.out.println("接收成功");
        notifyListeners(msg);
    }

    public boolean isConnected(){
        return connection.isConnected();
    }

    public List<String> getAllUsers() {
        Roster roster = Roster.getInstanceFor(connection);
        //List<RosterEntry> EntriesList = new ArrayList<RosterEntry>();
        List<String> names = new ArrayList<>();
        Collection<RosterEntry> rosterEntry = roster.getEntries();
        Iterator<RosterEntry> i = rosterEntry.iterator();
        while (i.hasNext()) {
            String account = i.next().getUser().split("@")[0];
            names.add(account);
        }
        return names;
    }

    public void addUser(String account,String name){
        try {
            if (roster==null)roster = Roster.getInstanceFor(connection);
            roster.createEntry(addXmppHost(account),addXmppHost(name),null);
        } catch (SmackException.NotLoggedInException e) {
            e.printStackTrace();
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    public void removeUser(String account){
        try {
            RosterEntry rosterEntry = roster.getEntry(addXmppHost(account));
            roster.removeEntry(rosterEntry);
        } catch (SmackException.NotLoggedInException e) {
            e.printStackTrace();
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    protected void reconnect() throws IOException, XMPPException, SmackException {
        if (connection.isConnected()){
            connection.connect();
            connection.login(account,password,resource);
        }
    }

    class UserRoasterListener implements RosterListener{

        @Override
        public void entriesAdded(Collection<String> collection) {
            System.out.println("entriesAdded");
            int len = collection.size();
            System.out.println("entries size === "+len);
            Iterator<String> it = collection.iterator();
            for (int i=0;i<len;i++){
                System.out.println("collection content === " +it.next());
            }
        }

        @Override
        public void entriesUpdated(Collection<String> collection) {
            System.out.println("entriesUpdated");
            int len = collection.size();
            System.out.println("entries size === "+len);
            Iterator<String> it = collection.iterator();
            for (int i=0;i<len;i++){
                System.out.println("collection content === " +it.next());
            }
        }

        @Override
        public void entriesDeleted(Collection<String> collection) {
            System.out.println("entriesDeleted");
            int len = collection.size();
            System.out.println("entries size === "+len);
            Iterator<String> it = collection.iterator();
            for (int i=0;i<len;i++){
                System.out.println("collection content === " +it.next());
            }
        }

        @Override
        public void presenceChanged(Presence presence) {
            System.out.println("presenceChange");
        }
    }
}
