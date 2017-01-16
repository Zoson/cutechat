package com.init.cutechat.Application.activity;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.init.cutechat.R;
import com.init.cutechat.domain.enity.Face;
import com.init.cutechat.domain.enity.FeaturesSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Zoson on 16/2/17.
 */
public class FeaturesAdapter extends BaseAdapter {
    private List<Integer> mlist;
    private FeaturesSet featuresSet;
    private Context context;
    private int index = 0;
    private Face face;
    private Callback callback;
    private Map<Integer,Boolean> states=new HashMap<Integer,Boolean>();
    public FeaturesAdapter(Context context,FeaturesSet featuresSet,Callback callback){
        this.context = context;
        this.featuresSet = featuresSet;
        this.mlist = featuresSet.getEyebrow_left();
        this.face = new Face();
        this.callback = callback;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int i) {
        return mlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        view = LayoutInflater.from(context).inflate(R.layout.horizontal_list_item,null);
        viewHolder = new ViewHolder();
        viewHolder.im = (ImageView)view.findViewById(R.id.img_list_item);
        viewHolder.im.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),mlist.get(i)));
        viewHolder.tv_choice = (TextView)view.findViewById(R.id.text_list_item);
        view.setTag(viewHolder);
        viewHolder.im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Integer key:states.keySet()){
                    states.put(key,false);
                }
                selectFeatures(i);
            }
        });
        viewHolder.tv_choice.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                selectFeatures(i);
                nextList();
                viewGroup.removeAllViewsInLayout();
                notifyDataSetChanged();
            }
        });
        return view;
    }

    class ViewHolder{
        ImageView im;
        TextView tv_choice;
    }

    public void selectFeatures(int i) {
        switch (index){
            case 0:face.setEyebrow_left(mlist.get(i));break;
            case 1:face.setEyebrow_right(mlist.get(i));break;
            case 2:face.setEye_left(mlist.get(i));break;
            case 3:face.setEye_right(mlist.get(i));break;
            case 4:face.setMouth(mlist.get(i));break;
        }
        callback.drawFace(face);
    }

    public Face getFace(){
        return face;
    }

    public void nextList(){
        index++;
        if (index>4)index = 0;
        switch (index){
            case 0:mlist = featuresSet.getEyebrow_left();break;
            case 1:mlist = featuresSet.getEyebrow_right();break;
            case 2:mlist = featuresSet.getEye_left();break;
            case 3:mlist = featuresSet.getEye_right();break;
            case 4:mlist = featuresSet.getMouth();break;
        }
    }

    public interface Callback{
        public void drawFace(Face face);
    }
}
