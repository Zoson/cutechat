package com.init.cutechat.domain.enity;

import com.init.cutechat.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zoson on 16/2/17.
 */
public class FeaturesSet {
    private List<Integer> eye_left;
    private List<Integer> eye_right;
    private List<Integer> mouth;
    private List<Integer> eyebrow_left;
    private List<Integer> eyebrow_right;

    public FeaturesSet(){
        //eye_brow_left
        eyebrow_left = new ArrayList<>();
        eyebrow_left.add(R.drawable.eyebrows_angry_l_b);
        eyebrow_left.add(R.drawable.eyebrows_detest_l_b);
        eyebrow_left.add(R.drawable.eyebrows_astonished_l_b);
        eyebrow_left.add(R.drawable.eyebrows_frightened_l_b);
        eyebrow_left.add(R.drawable.eyebrows_happy_l_b);
        eyebrow_left.add(R.drawable.eyebrows_sad_l_b);

        eyebrow_right = new ArrayList<>();
        eyebrow_right.add(R.drawable.eyebrows_angry_r_b);
        eyebrow_right.add(R.drawable.eyebrows_detest_r_b);
        eyebrow_right.add(R.drawable.eyebrows_astonished_r_b);
        eyebrow_right.add(R.drawable.eyebrows_frightened_r_b);
        eyebrow_right.add(R.drawable.eyebrows_happy_r_b);
        eyebrow_right.add(R.drawable.eyebrows_sad_r_b);

        eye_left = new ArrayList<>();
        eye_left.add(R.drawable.eyes_angry_l_b);
        eye_left.add(R.drawable.eyes_detest_l_b);
        eye_left.add(R.drawable.eyes_astonished_l_b);
        eye_left.add(R.drawable.eyes_frightened_l_b);
        eye_left.add(R.drawable.eyes_happy_l_b);
        eye_left.add(R.drawable.eyes_sad_l_b);

        eye_right = new ArrayList<>();
        eye_right.add(R.drawable.eyes_angry_r_b);
        eye_right.add(R.drawable.eyes_detest_r_b);
        eye_right.add(R.drawable.eyes_astonished_r_b);
        eye_right.add(R.drawable.eyes_frightened_r_b);
        eye_right.add(R.drawable.eyes_happy_r_b);
        eye_right.add(R.drawable.eyes_sad_r_b);

        mouth = new ArrayList<>();
        mouth.add(R.drawable.mouth_angry_b);
        mouth.add(R.drawable.mouth_detest_b);
        mouth.add(R.drawable.mouth_astonished_b);
        mouth.add(R.drawable.mouth_frightened_b);
        mouth.add(R.drawable.mouth_happy_b);
        mouth.add(R.drawable.mouth_sad_b);

    }

    public List<Integer> getEye_left() {
        return eye_left;
    }

    public List<Integer> getEye_right() {
        return eye_right;
    }

    public List<Integer> getMouth() {
        return mouth;
    }

    public List<Integer> getEyebrow_left() {
        return eyebrow_left;
    }

    public List<Integer> getEyebrow_right() {
        return eyebrow_right;
    }


}
