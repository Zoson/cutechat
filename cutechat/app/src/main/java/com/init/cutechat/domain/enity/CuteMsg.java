package com.init.cutechat.domain.enity;

import com.druson.cycle.enity.Enity;

import org.json.JSONException;

/**
 * Created by Zoson on 16/4/30.
 */
public class CuteMsg extends Enity {
    private String sender;
    private String recevice;
    private int state;
    private String content = "";
    private long time;

    public CuteMsg(){

    }
    public CuteMsg(String sender,String recevice){
        this.sender = sender;
        this.recevice = recevice;
        time = System.currentTimeMillis();
    }

    public int getState(){
        return state;
    }

    public static CuteMsg genObjectByJson(String json) throws JSONException {
        CuteMsg cuteMsg = new CuteMsg();
        cuteMsg.initByJson(json);
        return cuteMsg;
    }

    public void setState(int state){
        this.state = state;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }

}
