package com.druson.cycle.service.comm;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Zoson on 16/5/9.
 */
public class IMOpState implements Parcelable{

    public static final int SUCC = 0x1;
    public static final int FAIL = 0x2;
    public int state;
    public String result;
    public IMCallback imCallback;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(state);
        dest.writeString(result);
    }

    public void readFromParce(Parcel in){
        state = in.readInt();
        result = in.readString();
    }

    public IMOpState(){

    }

    public IMOpState(int state,String result){
        this.state = state;
        this.result = result;
    }

    public IMOpState(Parcel in){
        readFromParce(in);
    }

    public static Creator<IMOpState> CREATOR = new Creator<IMOpState>() {
        @Override
        public IMOpState createFromParcel(Parcel source) {
            return new IMOpState(source);
        }

        @Override
        public IMOpState[] newArray(int size) {
            return new IMOpState[0];
        }
    };
}
