package com.druson.cycle.service.http;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Zoson on 16/5/7.
 */
public class Request {

    public String url;
    public String api;
    public Map<String,String> params;
    public Map<String,String> fileparams;
    public int reqType = HttpRequest.GET;

    public Request(){
        params = new HashMap<>();
        fileparams = new HashMap<>();
    }

    public boolean isTranferFile(){
        if (fileparams != null){
            return true;
        }else{
            return false;
        }
    }

    public void putParams(String key,String value){
        params.put(key,value);
    }

    public void putFileParams(String key,String value){
        fileparams.put(key,value);
    }

    public String getStringParams(){
        StringBuilder sb = new StringBuilder();
        for (String key:params.keySet()){
            sb.append(key);
            sb.append("=");
            sb.append(params.get(key));
            sb.append("&");
        }
        sb.delete(sb.length()-1,sb.length());
        return sb.toString();
    }

    public static class Builder{
        Request request;
        public Builder(){
            request = new Request();
        }
        public Builder setUrl(String url){
            request.url = url;
            return this;
        }
        public Builder setParams(Map params){
            request.params = params;
            return this;
        }
        public Builder setFileParams(Map fileParams){
            request.fileparams = fileParams;
            return this;
        }
    }

}
