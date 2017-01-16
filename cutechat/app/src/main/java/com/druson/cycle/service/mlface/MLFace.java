package com.druson.cycle.service.mlface;

/**
 * Created by Zoson on 16/5/23.
 */
public class MLFace {

    static {
        System.loadLibrary("mlface");
    }
    public native int[] handleFacePoint(int[] points);

    public native float testSvm();
}
