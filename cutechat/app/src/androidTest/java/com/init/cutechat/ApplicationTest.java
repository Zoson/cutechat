package com.init.cutechat;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.druson.cycle.service.mlface.MLFace;
import com.init.cutechat.domain.enity.Face;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void test_mlface(){
        MLFace face = new MLFace();
        System.out.println("testSvm flag = "+ face.testSvm());
    }

}