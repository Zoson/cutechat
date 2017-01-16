package com.init.cutechat.domain.enity;

import com.druson.cycle.enity.Enities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by druson on 2016/2/25.
 */
public class User extends Enities {

    String account;
    String password;
    String name;
    String sex;
    int birth_year;

    public User(Enities parent) {
        super(parent);
    }

    public User(){

    }

    @Override
    public void initByJson(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
    }

    @Override
    public String getEnityName() {
        return this.getClass().getSimpleName();
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public int getBirth_year() {
        return birth_year;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setBirth_year(int birth_year) {
        this.birth_year = birth_year;
    }
}
