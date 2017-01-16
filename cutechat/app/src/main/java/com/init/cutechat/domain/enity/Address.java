package com.init.cutechat.domain.enity;

import com.druson.cycle.enity.Enity;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

/**
 * Created by Zoson on 16/5/10.
 */
public class Address extends Enity {
    String country;
    String province;
    String city;
    String more;

    public Address(){

    }
}
