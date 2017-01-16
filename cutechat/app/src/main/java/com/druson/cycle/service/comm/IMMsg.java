package com.druson.cycle.service.comm;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Zoson on 16/5/9.
 */
public class IMMsg implements Parcelable{
    public static final int TXT = IMListener.IM_TXT;
    public static final int FIFLE = IMListener.IM_FILE;

    int type;
    String sender;
    String content;
    String address;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeString(sender);
        dest.writeString(content);
        dest.writeString(address);
    }

    public void readFromParcel(Parcel in){
        type = in.readInt();
        sender = in.readString();
        content = in.readString();
        address = in.readString();

    }

    public IMMsg(Parcel in){
        readFromParcel(in);
    }

    public IMMsg(){

    }

    public static Creator<IMMsg> CREATOR = new Creator<IMMsg>() {
        @Override
        public IMMsg createFromParcel(Parcel source) {
            return new IMMsg(source);
        }

        @Override
        public IMMsg[] newArray(int size) {
            return new IMMsg[0];
        }
    };


    public int getType() {
        return type;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public String getAddress() {
        return address;
    }
}
