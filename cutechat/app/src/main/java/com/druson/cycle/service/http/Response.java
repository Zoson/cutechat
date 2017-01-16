package com.druson.cycle.service.http;
/**
 * Created by Zoson on 16/5/7.
 */
public class Response{

    int state;
    String data_string;
    byte[] data_bytes;

    protected Response(){

    }

    public byte[] getData_bytes() {
        return data_bytes;
    }

    public int getState() {
        return state;
    }

    public String getData_string() {
        return data_string;
    }
}
