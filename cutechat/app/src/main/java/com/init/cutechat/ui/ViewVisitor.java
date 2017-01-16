package com.init.cutechat.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.init.cutechat.domain.enity.ContextData;
import com.druson.cycle.utils.BitmapUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Zoson on 16/5/2.
 */
public class ViewVisitor {

    public static Bitmap cache;
    public static void setTranslucent(Window window){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public static Bitmap getStackBlurBitmap(Bitmap cache){
        int w = cache.getWidth();
        int h = cache.getHeight();
        int[] pixels = new int[w*h];
        cache.getPixels(pixels, 0, w, 0, 0, w, h);
        int[] pixels2 = BitmapUtils.stackBlur(pixels, w, h, 100);
        cache.recycle();
        final Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels2, 0, w, 0, 0, w, h);
        return bitmap;
    }

    public static void  setBackground(Context context, final View view,int res){
        if (cache != null){
            view.setBackground(new BitmapDrawable(cache));
            return;
        }
        String path = ContextData.AppDataPath+"/"+ContextData.backgroundPicName;
        File file = new File(path);
        if (file.exists()&&file.isFile()){
            cache = BitmapFactory.decodeFile(path);
            view.setBackground(new BitmapDrawable(cache));
        }else{
            long time = System.currentTimeMillis();
            cache = BitmapFactory.decodeResource(context.getResources(), res);
            int w = cache.getWidth();
            int h = cache.getHeight();
            int[] pixels = new int[w*h];
            cache.getPixels(pixels, 0, w, 0, 0, w, h);
            int[] pixels2 = BitmapUtils.stackBlur(pixels, w, h, 100);
            cache.recycle();
            final Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels2, 0, w, 0, 0, w, h);
            time = System.currentTimeMillis()-time;
            System.out.println("time === " + time);
            view.post(new Runnable() {
                @Override
                public void run() {
                    view.setBackground(new BitmapDrawable(bitmap));
                }
            });
            FileOutputStream out;
            ByteArrayOutputStream baos;
            try {
                baos = new ByteArrayOutputStream();
                out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                out.write(baos.toByteArray());
                out.flush();
                out.close();
                baos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }


}
