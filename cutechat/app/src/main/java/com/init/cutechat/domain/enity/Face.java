package com.init.cutechat.domain.enity;

import com.druson.cycle.enity.Enities;
import com.druson.cycle.enity.Enity;
import com.init.cutechat.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by zoson on 15-10-13.
 */
public class Face extends Enity {

    private int[] m_points;
    private int m_res_eye_left = R.drawable.eyes_l_0_b;
    private int m_res_eye_right = R.drawable.eyes_r_0_b;
    private int m_res_eyebrow_left = R.drawable.eyebrows_l_0_b;
    private int m_res_eyebrow_right = R.drawable.eyebrows_r_0_b;
    private int m_res_nose ;
    private int m_res_mouth = R.drawable.mouth_0_b;


    public Face(Enities enities){
        super(enities);
    }

    public Face(){

    }

    public Face(int []points){
        this.m_points = points;
    }

    public int[] getPoints(){
        int[] temp = new int[m_points.length];
        for(int i=0;i<m_points.length;i++){
            if (i%2 == 0){
                temp[i] = m_points[i]*1/5 +20;
            }else{
                temp[i] = m_points[i]*1/5 +30;
            }
        }
        return temp;
    }

    public int[] getOriginalPoints(){
        return m_points;
    }

    public int getLeftEye(){
        return m_res_eye_left;
    }

    public int getRightEye(){
        return m_res_eye_right;
    }

    public int getLeftEyeBrow(){
        return m_res_eyebrow_left;
    }

    public int getRightEyeBrow(){
        return m_res_eyebrow_right;
    }

    public int getNose(){
        return m_res_nose;
    }

    public int getMouth(){
        return m_res_mouth;
    }

    public void setNose(int m_res_nose) {
        this.m_res_nose = m_res_nose;
    }

    public void setMouth(int m_res_mouth) {
        this.m_res_mouth = m_res_mouth;
    }

    public void setEye_left(int m_res_eye_left) {
        this.m_res_eye_left = m_res_eye_left;
    }

    public void setEye_right(int m_res_eye_right) {
        this.m_res_eye_right = m_res_eye_right;
    }

    public void setEyebrow_left(int m_res_eyebrow_left) {
        this.m_res_eyebrow_left = m_res_eyebrow_left;
    }

    public void setEyebrow_right(int m_res_eyebrow_right) {
        this.m_res_eyebrow_right = m_res_eyebrow_right;
    }
}
