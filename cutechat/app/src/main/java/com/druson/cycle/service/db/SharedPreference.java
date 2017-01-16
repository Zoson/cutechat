package com.druson.cycle.service.db;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zoson on 6/26/15.
 */
public class SharedPreference {
    Context context;
    String name = "sp";
    SharedPreferences mySharedPreferences;
    public SharedPreference(Context context,String name) {
        this.context = context;
        mySharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public SharedPreferences getSharedPreferences(){
        return mySharedPreferences;
    }

    public void setName(String name){
        this.name = name;
    }

    public void set(String key, String value){
        if (value.equals("")||value.equals(" ")||value.equals("null")){
            return;
        }
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String get(String key,String defaultString) {
        String value =mySharedPreferences.getString(key, defaultString);
        return value;
    }



}
