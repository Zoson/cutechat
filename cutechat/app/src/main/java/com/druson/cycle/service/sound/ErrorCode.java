package com.druson.cycle.service.sound;

import android.content.res.Resources;

/**
 * Created by Zoson on 16/4/28.
 */

public class ErrorCode
{
    public final static int SUCCESS
            = 1000;
    public final static int E_NOSDCARD
            = 1001;
    public final static int E_STATE_RECODING
            = 1002;
    public final static int E_UNKOWN
            = 1003;


    public static String getErrorInfo( int vType)
            throws Resources.NotFoundException
    {
        switch(vType)
        {
            case SUCCESS:
                return "success";
            case E_NOSDCARD:
                return "没有SD卡，无法存储录音数据";
            case E_STATE_RECODING:
                return "正在录音中，请先停止录音";
            case E_UNKOWN:
            default:
                return "无法识别的错误";
        }
    }

}

